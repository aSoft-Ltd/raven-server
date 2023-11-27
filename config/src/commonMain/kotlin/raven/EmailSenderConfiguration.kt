package raven

import kotlinx.serialization.Serializable
import kotlinx.serialization.StringFormat
import sanity.EventBus

/**
 * mail.smtp.host=smtp.sendgrid.net
 * mail.smtp.port=465
 * mail.smtp.user=apikey
 * mail.smtp.password=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
 * mail.smtp.auth=true
 * mail.smtp.starttls.enable=true
 * mail.smtp.debug=true
 * mail.smtp.socketFactory.port=465
 * mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory
 * mail.smtp.socketFactory.fallback=false
 */
@Serializable
class EmailSenderConfiguration(
    val type: String? = null,
    val host: String? = null,
    val port: Int? = null,
    val user: String? = null,
    val password: String? = null
) {
    fun toOptions(
        bus: EventBus,
        topic: BusEmailTopic,
        codec: StringFormat
    ): Any? {
        val s = type ?: return null
        return when {
            s.contains(EmailSenderType.Bus.name, ignoreCase = true) -> BusEmailOptions(bus, topic, codec)
            s.contains(EmailSenderType.Console.name, ignoreCase = true) -> ConsoleEmailSenderOptions()
            s.contains(EmailSenderType.Smtp.name, ignoreCase = true) -> SmtpMailerOptions(
                host = host ?: throw smtpMustHave("host"),
                user = user ?: throw smtpMustHave("user"),
                port = port ?: 465,
                password = password ?: throw smtpMustHave("password")
            )

            else -> throw IllegalArgumentException("Unsupported mail sender type. Available options are ${EmailSenderType.values()}")
        }
    }

    private fun smtpMustHave(key: String) = IllegalArgumentException("smtp sender must have a $key configuration")

    fun toSender(bus: EventBus, topic: BusEmailTopic, codec: StringFormat): EmailSender? = when (val options = toOptions(bus, topic, codec)) {
        null -> null
        is ConsoleEmailSenderOptions -> ConsoleEmailSender(options)
        is BusEmailOptions -> BusEmailSender(options)
        is SmtpMailerOptions -> SmtpEmailSender(options)
        else -> throw IllegalArgumentException("Unsupported mailing option")
    }
}