package co.touchlab.kampkit.models

sealed class DataState<out T> {
    data class Success<T>(val data: T) : DataState<T>()
    data class Error(val exception: String) : DataState<Nothing>()
    object Empty : DataState<Nothing>()
    data class Loading<T>(val lastDataState: DataState<T>? = null) : DataState<T>()
}
