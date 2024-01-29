@file:OptIn(ExperimentalCoroutinesApi::class)

import kommander.expect
import koncurrent.later.await
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import raven.Address
import raven.SendEmailParams
import raven.SmtpEmailSender
import raven.SmtpMailerOptions
import java.util.Properties
import kotlin.test.Test

class SmtpEmailSenderTest {
    val prop = Properties().apply {
        val inStream = this@SmtpEmailSenderTest::class.java.getResourceAsStream("sendgrid.properties")
        println(inStream)
        load(inStream)
    }

    val options = SmtpMailerOptions.create(prop)

    val sender = SmtpEmailSender(options)

//    @Test
//    fun should_send_an_email() = runTest {
//        val cfg = options.toProperties()
//        println(cfg)
//        val message = sender.send(
//            draft = EmailDraft(
//                subject = "Test Draft",
//                body = "This is a test email"
//            ),
//            from = Email("support@picortex.com"),
//            to = Email("andylamax@programmer.net"),
//        ).await()
//        expect(message).toBeNonNull()
//    }

    @Test
    fun should_send_html() = runTest {
        val from = Address(
            name = "PiCortex Verifier",
            email = "picapital.verifier@picortex.com"
        )

        val to = Address(
            name = "Anderson Lameck",
            email = "andylamax@programmer.net"
        )

        val params = SendEmailParams(
            from = from,
            to = to,
            subject = "Test Email",
            body = "<html><body><b>This is a test email</b>&nbsp;not bold</body></html>",
        )

        val message = sender.send(params).await()
        expect(message).toBeNonNull()
    }

//    @Test
//    fun should_send_html_with_attachments() = runTest {
//        val inputStream = SmtpEmailSenderTest::class.java.getResourceAsStream("Vonage_Guide.pdf")!!;
//        val message = sender.send(
//            draft = EmailDraft(
//                subject = "Test Draft",
//                body = "<html><body><b>This is a test email</b>&nbsp;not bold</body></html>",
//                attachments = iListOf(
//                    ByteArrayAttachment(
//                        content = inputStream.readAllBytes(),
//                        name = "Vonage_Guide.pdf",
//                        type = "application/pdf"
//                    )
//                )
//            ),
//            from = Email("support@picortex.com"),
//            to = Email("andylamax@programmer.net"),
//        ).await()
//        expect(message).toBeNonNull()
//    }
}