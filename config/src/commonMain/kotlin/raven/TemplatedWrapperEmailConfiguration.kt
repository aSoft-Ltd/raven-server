package raven

import kotlinx.serialization.Serializable

@Serializable
class TemplatedWrapperEmailConfiguration(
    val email: TemplatedEmailConfiguration? = null
) {
    fun toOptions(
        brand: String,
        domain: String,
        address: String,
        service: String
    ) = email?.toOptions(brand, domain, address, service)
}