package org.softwaremaestro.presenter.util

sealed class UIState<out T>(val _data: T?) {
    object Pending : UIState<Nothing>(null)
    object Loading : UIState<Nothing>(null)

    object Failure : UIState<Nothing>(null)
    data class Success<T>(val data: T) : UIState<T>(_data = data)
}