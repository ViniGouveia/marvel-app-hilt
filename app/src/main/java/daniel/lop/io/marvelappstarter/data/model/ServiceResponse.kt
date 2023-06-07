package daniel.lop.io.marvelappstarter.data.model

import java.io.Serializable

data class ServiceResponse<T>(
    val data: ServiceResponseData<T>
) : Serializable

data class ServiceResponseData<T>(
    val results: T
) : Serializable
