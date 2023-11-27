package raven

import kommander.expect
import kommander.toBe
import kotlin.test.Test
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import net.peanuuutz.tomlkt.Toml
import sanity.LocalBus

class BusEmailSenderConfigurationTest {

    @Serializable
    class TestConfig(
        val mail: EmailSenderConfiguration?
    )

    private val topic = BusEmailTopic()
    private val bus = LocalBus()
    private val codec = Toml { ignoreUnknownKeys = true }

    @Test
    fun should_be_able_to_configure_flix_mailing_options() {
        val raw = """
            [mail]
            type = "bus"
        """.trimIndent()

        val config = codec.decodeFromString<TestConfig>(raw)
        val options = config.mail?.toOptions(bus,topic,codec)
        expect(options).toBe<BusEmailOptions>()
    }

    @Test
    fun should_be_able_to_configure_flix_mailer() {
        val raw = """
            [mail]
            type = "bus"
        """.trimIndent()

        val config = codec.decodeFromString<TestConfig>(raw)
        val mailer = config.mail?.toSender(bus, topic, codec)
        expect(mailer).toBe<BusEmailSender>()
    }
}