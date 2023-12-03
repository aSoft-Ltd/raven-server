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
    fun toOptions(brand: String, domain: String, address: String, service: String) = TemplatedEmailOptions(
        brand = brand,
        domain = domain,
        from = Address(
            name = name ?: service.replaceFirstChar { it.uppercaseChar() },
            email = this.address ?: throw IllegalArgumentException("$service email address is required")
        ),
        subject = subject ?: throw IllegalArgumentException("$service subject is required"),
        template = template(service),
        address = address,
    )

    private fun template(service: String): EmailTemplate {
        val path = template ?: throw IllegalArgumentException("$service template is required")
        val html = File("$path.html")
        val plain = File("$path.txt")

        return when {
            html.exists() && plain.exists() -> EmailTemplate(html.readText(), plain.readText())
            html.exists() && !plain.exists() -> {
                val txt = html.readText()
                EmailTemplate(txt, txt)
            }

            !html.exists() && plain.exists() -> {
                val txt = plain.readText()
                EmailTemplate(txt, txt)
            }

            else -> {
                val message = "$service could not location template at $template.txt or $template.html"
                throw IllegalArgumentException(message)
            }
        }
    }
}