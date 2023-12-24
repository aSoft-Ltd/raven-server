package raven

import kollections.List
import koncurrent.Later
import koncurrent.later.then
import koncurrent.later.andThen
import koncurrent.later.andZip
import koncurrent.later.zip
import koncurrent.later.catch
import koncurrent.later
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FlixServerMailer(val options: FlixServerMailerOptions) : Mailer {

    val flow = MutableStateFlow<EmailMessage?>(null)

    override fun send(draft: EmailDraft, from: AddressInfo, to: List<AddressInfo>): Later<EmailMessage> = options.scope.later {
        draft.toMessage(from, to).also { flow.emit(it) }
    }
}