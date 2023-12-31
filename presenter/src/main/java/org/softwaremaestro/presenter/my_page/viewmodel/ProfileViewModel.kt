package org.softwaremaestro.presenter.my_page.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.domain.profile.usecase.MyProfileGetUseCase
import org.softwaremaestro.domain.profile.usecase.ProfileUpdateUseCase
import org.softwaremaestro.presenter.util.Util.logError
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val myProfileGetUseCase: MyProfileGetUseCase,
    private val profileUpdateUseCase: ProfileUpdateUseCase,
) :
    ViewModel() {

    private val _id = MutableLiveData<String>()
    val id: LiveData<String> get() = _id

    private val _bio = MutableLiveData<String>()
    val bio: LiveData<String> get() = _bio

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    private val _univName = MutableLiveData<String>()
    val univName: LiveData<String> get() = _univName

    private val _major = MutableLiveData<String>()
    val major: LiveData<String> get() = _major

    private val _schoolLevel = MutableLiveData<String>()
    val schoolLevel: LiveData<String> get() = _schoolLevel

    private val _schoolGrade = MutableLiveData<Int>()
    val schoolGrade: LiveData<Int> get() = _schoolGrade

    private val _image: MutableLiveData<String> = MutableLiveData()
    val image: LiveData<String> get() = _image

    private val _followers: MutableLiveData<List<String>> = MutableLiveData()
    val followers: LiveData<List<String>> get() = _followers

    private val _followingCount: MutableLiveData<Int> = MutableLiveData()
    val followingCount: LiveData<Int> get() = _followingCount

    private val _rating: MutableLiveData<Float> = MutableLiveData()
    val rating: LiveData<Float> get() = _rating

    fun getMyProfile() {
        viewModelScope.launch {
            myProfileGetUseCase.execute()
                .catch { exception ->
                    logError(this@ProfileViewModel::class.java, exception.message.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> {
                            with(result.data) {
                                id?.let { _id.postValue(it) }
                                name?.let { _name.postValue(it) }
                                bio?.let { _bio.postValue(it) }
                                schoolName?.let { _univName.postValue(it) }
                                schoolDepartment?.let { _major.postValue(it) }
                                schoolLevel?.let { _schoolLevel.postValue(it) }
                                schoolGrade?.let { _schoolGrade.postValue(it) }
                                profileImage?.let { _image.postValue(it) }
                                followers?.let { _followers.postValue(it) }
                                following?.let { _followingCount.postValue(it.size) }
                                rating?.let { _rating.postValue(it) }
                            }
                        }

                        is BaseResult.Error -> logError(
                            this@ProfileViewModel::class.java,
                            result.toString()
                        )
                    }
                }
        }
    }

    fun updateProfile() {
        viewModelScope.launch {
            profileUpdateUseCase.execute()
                .catch { exception ->
                    logError(this@ProfileViewModel::class.java, exception.message.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> {
                            with(result.data) {
                                id?.let { _name.postValue(it) }
                                name?.let { _name.postValue(it) }
                                bio?.let { _bio.postValue(it) }
                                schoolName?.let { _univName.postValue(it) }
                                schoolDepartment?.let { _major.postValue(it) }
                                schoolLevel?.let { _schoolLevel.postValue(it) }
                                schoolGrade?.let { _schoolGrade.postValue(it) }
                                profileImage?.let { _image.postValue(it) }
                                followers?.let { _followers.postValue(it) }
                                following?.let { _followingCount.postValue(it.size) }
                            }
                        }

                        is BaseResult.Error -> logError(
                            this@ProfileViewModel::class.java,
                            result.toString()
                        )
                    }
                }
        }
    }

    fun setImage(image: String) {
        _image.postValue(image)
    }
}