package raven

import kollections.List
import koncurrent.Later
import koncurrent.later
import kotlinx.coroutines.channels.Channel

class FlixServerMailer(val options: FlixServerMailerOptions) : Mailer {

    val channel = Channel<EmailMessage>()

    override fun send(draft: EmailDraft, from: AddressInfo, to: List<AddressInfo>): Later<EmailMessage> = options.scope.later {
        draft.toMessage(from, to).also { channel.send(it) }
    }
}