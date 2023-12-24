package raven

import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import javax.mail.util.ByteArrayDataSource
import koncurrent.Later
import koncurrent.later.then
import koncurrent.later.andThen
import koncurrent.later.andZip
import koncurrent.later.zip
import koncurrent.later.catch
import koncurrent.later

class SmtpEmailSender(val options: SmtpMailerOptions) : EmailSender {

    private val authenticator by lazy {
        object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(options.user, options.password)
            }
        }
    }

    private val session by lazy { Session.getDefaultInstance(options.toProperties(), authenticator) }

    private fun Address.toInternetAddress() = if (name == null) {
        InternetAddress(email)
    } else {
        InternetAddress(email, name)
    }

    override fun supports(body: EmailContentType): Boolean = true

    override fun send(params: SendEmailParams): Later<SendEmailParams> = options.scope.later{
        val message = MimeMessage(session).apply {
            setFrom(params.from.toInternetAddress())
            addRecipients(Message.RecipientType.TO, params.to.map { it.toInternetAddress() }.toTypedArray())
            val multipart = MimeMultipart("mixed");
            subject = params.subject
            val messageBodyPart = MimeBodyPart();
            messageBodyPart.setContent(params.body, params.type.value)
            multipart.addBodyPart(messageBodyPart);

            params.attachments.forEachIndexed { index, attachment ->
                val attachmentBodyPart = MimeBodyPart()
                val dataSource = when (attachment) {
                    is ByteArrayAttachment -> ByteArrayDataSource(attachment.content, attachment.type)
                    is FileAttachment -> FileDataSource(attachment.content)
                    else -> error("Unsupported EmailAttachmentType ${attachment::class.simpleName}")
                }
                attachmentBodyPart.dataHandler = DataHandler(dataSource)
                attachmentBodyPart.setHeader("Content-ID", "<attachment-$index>")
                attachmentBodyPart.setHeader("Content-Type", attachment.type)
                attachmentBodyPart.fileName = attachment.name

                multipart.addBodyPart(attachmentBodyPart)
            }

            setContent(multipart)
        }

        Transport.send(message)
        params
    }

    override fun toString(): String = "SmtpEmailSender(host=${options.host},port=${options.port})"
}