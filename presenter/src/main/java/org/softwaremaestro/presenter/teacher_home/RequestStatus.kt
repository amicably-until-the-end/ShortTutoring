package org.softwaremaestro.presenter.teacher_home

sealed class RequestStatus(val noti: String) {
    object SELECTED: RequestStatus("selected")
    object NOT_SELECTED: RequestStatus("not selected")
    object YET_SELECTED: RequestStatus("yet selected")
}
