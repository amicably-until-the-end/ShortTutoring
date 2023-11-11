package org.softwaremaestro.presenter.login.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.univcert.api.UnivCert
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.common.BaseResult.Error
import org.softwaremaestro.domain.common.BaseResult.Success
import org.softwaremaestro.domain.login.entity.TeacherRegisterVO
import org.softwaremaestro.domain.login.usecase.TeacherRegisterUseCase
import org.softwaremaestro.presenter.BuildConfig
import org.softwaremaestro.presenter.login.Animal
import org.softwaremaestro.presenter.login.univGet.UnivGetService
import org.softwaremaestro.presenter.util.UIState
import javax.inject.Inject

@HiltViewModel
class TeacherRegisterViewModel @Inject constructor(
    private val teacherRegisterUseCase: TeacherRegisterUseCase
) :
    ViewModel() {

    val _schoolName: MutableLiveData<String?> = MutableLiveData(null)
    val schoolName: LiveData<String?> get() = _schoolName

    val _schoolNameSuggestions = MutableLiveData<List<String>>()
    val schoolNameSuggestions: LiveData<List<String>> get() = _schoolNameSuggestions

    val _major: MutableLiveData<String?> = MutableLiveData(null)
    val major: LiveData<String?> get() = _major

    val _name: MutableLiveData<String> = MutableLiveData()
    val name: LiveData<String> get() = _name

    val _bio: MutableLiveData<String> = MutableLiveData("모든 학생이 깨달음을 얻을 수 있도록!")
    val bio: LiveData<String> get() = _bio

    val _image: MutableLiveData<String> = MutableLiveData(Animal.getRandom().mName)
    val image: LiveData<String> get() = _image

    val _schoolNameAndMajorProper = MediatorLiveData<Boolean>()
    val schoolNameAndMajorProper: MediatorLiveData<Boolean> get() = _schoolNameAndMajorProper

    private val _sendEmailResult: MutableLiveData<Boolean> = MutableLiveData()
    val sendEmailResult: LiveData<Boolean> get() = _sendEmailResult

    private val _checkEmailResult: MutableLiveData<Boolean> = MutableLiveData()
    val checkEmailResult: LiveData<Boolean> get() = _checkEmailResult

    private val _teacherSignupState = MutableLiveData<UIState<String>>()
    val teacherSignupState: LiveData<UIState<String>> get() = _teacherSignupState

    private val _teacherInputProper = MediatorLiveData<Boolean>()
    val teacherInputProper: MediatorLiveData<Boolean> get() = _teacherInputProper

    private val service = UnivGetService()

    init {
        with(_schoolNameAndMajorProper) {
            addSource(_schoolName) {
                postValue(
                    !_schoolName.value.isNullOrEmpty() &&
                            !_major.value.isNullOrEmpty()
                )
            }

            addSource(_major) {
                postValue(
                    !_schoolName.value.isNullOrEmpty() &&
                            !_major.value.isNullOrEmpty()
                )
            }
        }

        with(_teacherInputProper) {
            addSource(_name) {
                postValue(
                    !_name.value.isNullOrEmpty() &&
                            !_bio.value.isNullOrEmpty() &&
                            !_image.value.isNullOrEmpty()
                )
            }

            addSource(_bio) {
                postValue(
                    !_name.value.isNullOrEmpty() &&
                            !_bio.value.isNullOrEmpty() &&
                            !_image.value.isNullOrEmpty()
                )
            }

            addSource(_image) {
                postValue(
                    !_name.value.isNullOrEmpty() &&
                            !_bio.value.isNullOrEmpty() &&
                            !_image.value.isNullOrEmpty()
                )
            }
        }
    }

    fun suggestSchoolNames(schoolName: String) {
        viewModelScope.launch {
            service.getUnivs(schoolName)
                .collect { result ->
                    _schoolNameSuggestions.value =
                        when (result) {
                            is Success -> result.data.distinct()
                            is Error -> emptyList()
                        }
                }
        }
    }

    fun registerTeacher() {
        viewModelScope.launch {
            teacherRegisterUseCase.execute(
                TeacherRegisterVO(
                    bio = bio.value!!,
                    name = name.value!!,
                    schoolName = schoolName.value!!,
                    schoolDepartment = major.value!!,
                    profileImg = image.value!!
                )
            )
                .onStart {
                    _teacherSignupState.value = UIState.Loading
                }
                .catch {
                    _teacherSignupState.value = UIState.Failure
                }
                .collect { result ->
                    when (result) {
                        is Success -> _teacherSignupState.value = UIState.Success(result.data)
                        is Error -> _teacherSignupState.value = UIState.Failure
                    }
                }

        }
    }

    fun sendVerificationMail(emailAddress: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val sendResult = UnivCert.certify(
                BuildConfig.UNIVCERT_API_KEY,
                emailAddress,
                schoolName.value,
                false,
            )
            Log.d("email", sendResult.toString())
            if (sendResult["success"] == true) {
                _sendEmailResult.postValue(true)
            } else {
                _sendEmailResult.postValue(false)
            }
        }
    }

    fun checkEmailCode(email: String, code: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val checkResult = UnivCert.certifyCode(
                BuildConfig.UNIVCERT_API_KEY,
                email,
                schoolName.value,
                code
            )
            if (checkResult["success"] == true) {
                _checkEmailResult.postValue(true)
            } else {
                _checkEmailResult.postValue(false)
            }
        }
    }


}