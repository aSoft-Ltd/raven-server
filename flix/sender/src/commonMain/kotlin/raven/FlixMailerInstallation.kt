package raven

import io.ktor.http.CacheControl
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.server.application.call
import io.ktor.server.request.receiveText
import io.ktor.server.response.cacheControl
import io.ktor.server.response.header
import io.ktor.server.response.respondBytesWriter
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.utils.io.writeStringUtf8
import koncurrent.later.await
import kotlinx.coroutines.launch

fun Routing.installMailer(mailer: FlixServerMailer, endpoint: FlixMailEndpoint) {
    get(endpoint.mailbox()) {
        call.response.cacheControl(CacheControl.NoCache(null))
        call.response.header(HttpHeaders.Connection, "Keep-Alive")
        call.respondBytesWriter(contentType = ContentType.Text.EventStream) {
            while (true) {
                val data = mailer.channel.receive()
                writeStringUtf8("id: 0\n")
                writeStringUtf8("event: message\n")
                writeStringUtf8("data: ${data.body}\n")
                writeStringUtf8("\n")
                flush()
            }
        }
    }

    post(endpoint.send()) {
        val text = call.receiveText()
        launch {
            mailer.send(
                draft = EmailDraft("Test Email", text),
                from = AddressInfo(name = "Raven Test Server", email = "server@raven.com"),
                to = AddressInfo(name = "Raven Test Client", email = "server@raven.com")
            ).await()
        }
        call.respondText(text)
    }
}