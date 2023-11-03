package raven

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import sanity.EventBus

@Serializable
class MailFactoryConfiguration(
    @SerialName("sender")
    val senders: List<MailingConfiguration>
) {
    fun toFactory(bus: EventBus) = MailSenderFactory().apply {
        addAll(senders.mapNotNull { it.toSender(bus) })
    }
}