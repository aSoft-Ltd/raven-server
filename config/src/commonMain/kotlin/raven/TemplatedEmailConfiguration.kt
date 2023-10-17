package raven

import java.io.File
import kotlinx.serialization.Serializable

@Serializable
class TemplatedEmailConfiguration(
    val name: String? = null,
    val address: String? = null,
    val subject: String? = null,
    val template: String? = null
) {
    fun toOptions(service: String) = TemplatedEmailOptions(
        address = AddressInfo(
            name = name ?: service.replaceFirstChar { it.uppercaseChar() },
            email = address ?: throw IllegalArgumentException("$service email address is required")
        ),
        subject = subject ?: throw IllegalArgumentException("$service subject is required"),
        template = template(service)
    )

    private fun template(service: String) : String {
        val path = template ?: throw IllegalArgumentException("$service template is required")
        val file = File(path)
        if(!file.exists()) throw IllegalArgumentException("$service could not location template at $template")
        return file.readText()
    }
}