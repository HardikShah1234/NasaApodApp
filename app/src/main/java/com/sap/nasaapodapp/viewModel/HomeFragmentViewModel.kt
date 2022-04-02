package com.sap.nasaapodapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sap.nasaapodapp.data.model.NasaPhotos
import com.sap.nasaapodapp.repository.ApodRepository
import com.sap.nasaapodapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val repository: ApodRepository
) : ViewModel() {

    var apodImageResponse: MutableLiveData<NetworkResult<NasaPhotos>> = MutableLiveData()
    var apodData: MutableLiveData<NasaPhotos> = MutableLiveData()

    init {
        getApodData(5)
    }

    fun getApodData(count: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getData(count)
                NetworkResult.Loading(null)
                val responseList: NetworkResult<NasaPhotos> = when {
                    response.message().toString().contains("timeout") -> {
                        NetworkResult.Error(data = null, message = "Time Out")
                    }
                    response.isSuccessful -> {
                        val apodResponse = response.body()
                        apodData.postValue(apodResponse!!)
                        NetworkResult.Success(data = apodResponse)
                    }
                    else -> {
                        NetworkResult.Error(message = "Could Not Fetch Results")
                    }
                }
                apodImageResponse.postValue(responseList)
            } catch (e: Exception) {
                apodImageResponse.postValue(NetworkResult.Error(message = e.message!!))
            }
        }
    }


/*    private fun checkInternetConnection(): Boolean {
        val connectivityManager =
            getApplication(Application().baseContext).getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when{
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }*/
}