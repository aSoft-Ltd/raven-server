package raven

import kommander.expect
import kotlin.test.Test
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import net.peanuuutz.tomlkt.Toml

class MailFactoryConfigurationTest {

    @Serializable
    class TestConfig(
        val mail: MailFactoryConfiguration?
    )

    private val scope = CoroutineScope(SupervisorJob())
    private val codec = Toml { ignoreUnknownKeys = true }

    @Test
    fun should_be_able_to_configure_multiple_mailing_options() {
        val raw = """
            [[mail.sender]]
            type = "flix"
            
            [[mail.sender]]
            type = "mock"
            
            [[mail.sender]]
            type = "smtp"
            host = "host"
            user = "user"
            password = "password"
        """.trimIndent()

        val config = codec.decodeFromString<TestConfig>(raw)
        expect(config.mail?.toFactory(scope)).toBeNonNull()
    }
}