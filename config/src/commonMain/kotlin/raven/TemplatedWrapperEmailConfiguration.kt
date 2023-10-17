package raven

import kotlinx.serialization.Serializable

@Serializable
class TemplatedWrapperEmailConfiguration(
    val email: TemplatedEmailConfiguration? = null
) {
    fun toOptions(service: String) = email?.toOptions(service)
}