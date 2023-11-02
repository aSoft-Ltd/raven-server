package raven

import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class MailFactoryConfiguration(
    @SerialName("sender")
    val senders: List<MailingConfiguration>
) {
    fun toFactory(scope: CoroutineScope) = MailSenderFactory().apply {
        addAll(senders.mapNotNull { it.toMailer(scope) })
    }
}