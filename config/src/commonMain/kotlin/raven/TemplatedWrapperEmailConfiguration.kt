package raven

import kotlinx.serialization.Serializable

@Serializable
class TemplatedWrapperEmailConfiguration(
    val email: TemplatedEmailConfiguration? = null
) {

    fun toAddress(service: String) = Address(
        name = email?.name ?: service.replaceFirstChar { it.uppercaseChar() },
        email = email?.address ?: throw IllegalArgumentException("$service email address is required")
    )

    fun toSubject(service: String) = email?.subject ?: throw IllegalArgumentException("$service subject is required")
}