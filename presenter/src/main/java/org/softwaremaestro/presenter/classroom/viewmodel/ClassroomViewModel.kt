package org.softwaremaestro.presenter.classroom.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.herewhite.sdk.Room
import com.herewhite.sdk.domain.Promise
import com.herewhite.sdk.domain.SDKError
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.login.entity.UserVO
import org.softwaremaestro.domain.login.usecase.AutoLoginUseCase
import org.softwaremaestro.domain.login.usecase.LoginUseCase
import org.softwaremaestro.domain.login.usecase.SaveKakaoJWTUseCase
import javax.inject.Inject

@HiltViewModel
class ClassroomViewModel @Inject constructor(
) :
    ViewModel() {

    private val _scenePreviews = MutableLiveData<List<Bitmap>>()
    val scenePreviews: LiveData<List<Bitmap>> = _scenePreviews

    /**
     * 카카오 로그인
     */
    fun getPreviews(room: Room) {
        val scenes = room.scenes
        val previews = mutableListOf<Bitmap>()
        scenes.forEach {

            room.getScenePreviewImage("/${it.name}", object : Promise<Bitmap> {
                override fun then(t: Bitmap?) {
                    previews.add(t!!)
                }

                override fun catchEx(e: SDKError?) {
                    Log.d("agora", e.toString())
                }
            })
        }
        _scenePreviews.value = previews
    }

}