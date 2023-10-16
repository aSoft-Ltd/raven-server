package raven

import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.Serializable

@Serializable
class MailingConfiguration(
    val sender: String? = null,
    val host: String? = null,
    val user: String? = null,
    val password: String? = null
) {
    fun toOptions(scope: CoroutineScope): Any? {
        val s = sender ?: return null
        return when {
            s.contains(MailSender.Flix.name, ignoreCase = true) -> FlixServerMailerOptions(scope)
            s.contains(MailSender.Mock.name, ignoreCase = true) -> MockMailerOptions(scope = scope)
            s.contains(MailSender.Smtp.name, ignoreCase = true) -> SmtpMailerOptions(
                host = host ?: throw smtpMustHave("host"),
                user = user ?: throw smtpMustHave("user"),
                password = password ?: throw smtpMustHave("password")
            )

            else -> throw IllegalArgumentException("Unsupported mailing sender")
        }
    }

    private fun smtpMustHave(key: String) = IllegalArgumentException("smtp sender must have a $key configuration")

    fun toMailer(scope: CoroutineScope): Mailer = when (val options = toOptions(scope)) {
        is FlixServerMailerOptions -> FlixServerMailer(options)
        is MockMailerOptions -> MockMailer(options)
        is SmtpMailerOptions -> SmtpMailer(options)
        else -> throw IllegalArgumentException("Unsupported mailing option")
    }
}