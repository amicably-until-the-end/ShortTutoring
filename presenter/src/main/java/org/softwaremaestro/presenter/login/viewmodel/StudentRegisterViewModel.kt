package org.softwaremaestro.presenter.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.common.BaseResult.Error
import org.softwaremaestro.domain.common.BaseResult.Success
import org.softwaremaestro.domain.login.entity.StudentRegisterVO
import org.softwaremaestro.domain.login.usecase.StudentRegisterUseCase
import org.softwaremaestro.presenter.login.Animal
import org.softwaremaestro.presenter.util.UIState
import javax.inject.Inject

@HiltViewModel
class StudentRegisterViewModel @Inject constructor(
    private val studentRegisterUseCase: StudentRegisterUseCase
) :
    ViewModel() {

    // ToSFragment에서 사용하는 변수
    private val _agreeOnToS = MutableLiveData<Boolean?>(null)
    val agreeOnToS: LiveData<Boolean?> get() = _agreeOnToS

    private val _agreeOnPrivacyPolicy = MutableLiveData<Boolean?>(null)
    val agreeOnPrivacyPolicy: LiveData<Boolean?> get() = _agreeOnPrivacyPolicy

    val agreeAll = MediatorLiveData<Boolean?>(null)

    // RegisterRoleFragment에서 사용하는 변수
    private val _role = MutableLiveData<Int?>(null)
    val role: LiveData<Int?> get() = _role

    private val _schoolLevel = MutableLiveData<String?>(null)
    val schoolLevel: LiveData<String?> get() = _schoolLevel

    private val _schoolGrade = MutableLiveData<Int?>(null)
    val schoolGrade: LiveData<Int?> get() = _schoolGrade

    private val _schoolLevelAndGradeProper = MediatorLiveData<Boolean>()
    val schoolLevelAndGradeProper: MediatorLiveData<Boolean> get() = _schoolLevelAndGradeProper

    private val _name = MutableLiveData<String?>(null)
    val name: LiveData<String?> get() = _name

    private val _bio = MutableLiveData("student-bio")
    val bio: LiveData<String> get() = _bio

    val _image: MutableLiveData<String> = MutableLiveData(Animal.getRandom().mName)
    val image: LiveData<String> get() = _image

    private val _studentSignupState = MutableLiveData<UIState<String>>()
    val studentSignupState: LiveData<UIState<String>> get() = _studentSignupState

    private val _studentNameAndImageProper = MediatorLiveData<Boolean>()
    val studentNameAndImageProper: MediatorLiveData<Boolean> get() = _studentNameAndImageProper

    init {
        with(agreeAll) {
            addSource(agreeOnToS) {
                postValue(agreeOnToS.value == true && agreeOnPrivacyPolicy.value == true)
            }

            addSource(agreeOnPrivacyPolicy) {
                postValue(agreeOnToS.value == true && agreeOnPrivacyPolicy.value == true)
            }
        }

        with(_schoolLevelAndGradeProper) {
            addSource(_schoolLevel) {
                postValue(!_schoolLevel.value.isNullOrEmpty() && _schoolGrade.value != null)
            }

            addSource(_schoolGrade) {
                postValue(!_schoolLevel.value.isNullOrEmpty() && _schoolGrade.value != null)
            }
        }

        with(_studentNameAndImageProper) {
            addSource(_name) {
                postValue(
                    !_name.value.isNullOrEmpty() && !_image.value.isNullOrEmpty()
                )
            }

            addSource(_image) {
                postValue(
                    !_name.value.isNullOrEmpty() && !_image.value.isNullOrEmpty()
                )
            }
        }
    }

    fun registerStudent() {
        viewModelScope.launch {
            studentRegisterUseCase.execute(
                StudentRegisterVO(
                    name = name.value!!,
                    bio = bio.value!!,
                    schoolLevel = schoolLevel.value!!,
                    schoolGrade = schoolGrade.value!!,
                    profileImg = image.value!!
                )
            )
                .onStart {
                    _studentSignupState.value = UIState.Loading
                }
                .catch {
                    _studentSignupState.value = UIState.Failure
                }
                .collect { result ->
                    when (result) {
                        is Success -> _studentSignupState.value = UIState.Success(result.data)
                        is Error -> _studentSignupState.value = UIState.Failure
                    }
                }
        }
    }

    fun setAgreeOnToS(b: Boolean) {
        _agreeOnToS.value = b
    }

    fun setAgreeOnPrivacyPolicy(b: Boolean) {
        _agreeOnPrivacyPolicy.value = b
    }

    fun setRole(role: Int) {
        _role.value = role
    }

    fun setName(name: String) {
        _name.value = name
    }

    fun setSchoolLevel(schoolLevel: String) {
        _schoolLevel.value = schoolLevel
    }

    fun setSchoolGrade(schoolGrade: Int) {
        _schoolGrade.value = schoolGrade
    }
}