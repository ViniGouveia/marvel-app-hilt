package daniel.lop.io.marvelappstarter.ui.state

sealed class ResourceState<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : ResourceState<T>(data)
    class Error<T>(data: T? = null, message: String?) : ResourceState<T>(data, message)
    class Loading<T> : ResourceState<T>()
    class Empty<T> : ResourceState<T>()
}
