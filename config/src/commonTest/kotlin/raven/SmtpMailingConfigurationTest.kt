package raven

import kommander.expect
import kommander.toBe
import kotlin.test.Test
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import net.peanuuutz.tomlkt.Toml

class SmtpMailingConfigurationTest {

    @Serializable
    class TestConfig(
        val mail: MailingConfiguration?
    )

    private val scope = CoroutineScope(SupervisorJob())
    private val codec = Toml { ignoreUnknownKeys = true }

    @Test
    fun should_be_able_to_configure_smtp_mailing_options() {
        val raw = """
            [mail]
            type = "smtp"
            host = "host"
            user = "user"
            password = "password"
        """.trimIndent()

        val config = codec.decodeFromString<TestConfig>(raw)
        val options = config.mail?.toOptions(scope)
        expect(options).toBe<SmtpMailerOptions>()
    }

    @Test
    fun should_be_able_to_configure_smtp_mailer() {
        val raw = """
            [mail]
            type = "smtp"
            host = "host"
            user = "user"
            password = "password"
        """.trimIndent()

        val config = codec.decodeFromString<TestConfig>(raw)
        val mailer = config.mail?.toMailer(scope)
        expect(mailer).toBe<SmtpMailer>()
    }
}