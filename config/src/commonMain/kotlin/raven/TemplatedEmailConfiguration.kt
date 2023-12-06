package raven

import kotlinx.serialization.Serializable

@Serializable
class TemplatedEmailConfiguration(
    val name: String? = null,
    val address: String? = null,
    val subject: String? = null,
    val template: String? = null
)