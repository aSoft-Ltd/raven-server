package raven

import kommander.expect
import kotlin.test.Test
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import net.peanuuutz.tomlkt.Toml
import sanity.LocalBus

class MultiEmailSenderFactoryConfigurationTest {

    @Serializable
    class TestConfig(
        val mail: MultiEmailSenderFactoryConfiguration?
    )

    private val topic = BusEmailTopic()
    private val bus = LocalBus()
    private val codec = Toml { ignoreUnknownKeys = true }

    @Test
    fun should_be_able_to_configure_multiple_mailing_options() {
        val raw = """
            [[mail.sender]]
            type = "bus"
            
            [[mail.sender]]
            type = "console"
            
            [[mail.sender]]
            type = "smtp"
            host = "host"
            user = "user"
            password = "password"
        """.trimIndent()

        val config = codec.decodeFromString<TestConfig>(raw)
        expect(config.mail?.toFactory(bus, topic, codec)).toBeNonNull()
    }
}