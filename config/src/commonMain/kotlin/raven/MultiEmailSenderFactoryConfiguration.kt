package raven

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.StringFormat
import sanity.EventBus

@Serializable
class MultiEmailSenderFactoryConfiguration(
    @SerialName("sender")
    val senders: List<EmailSenderConfiguration>
) {
    fun toFactory(bus: EventBus, topic: BusEmailTopic, codec: StringFormat) = MultiEmailSenderFactory().apply {
        addAll(senders.mapNotNull { it.toSender(bus, topic, codec) })
    }
}