package daniel.lop.io.marvelappstarter.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import daniel.lop.io.marvelappstarter.data.model.ServiceResponse
import daniel.lop.io.marvelappstarter.data.model.character.CharacterModel
import daniel.lop.io.marvelappstarter.repository.MarvelRepository
import daniel.lop.io.marvelappstarter.ui.state.ResourceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SearchCharacterViewModel @Inject constructor(
    private val repository: MarvelRepository
) : ViewModel() {

    private val _searchCharacter =
        MutableStateFlow<ResourceState<ServiceResponse<List<CharacterModel>>>>(ResourceState.Empty())
    val searchCharacter: StateFlow<ResourceState<ServiceResponse<List<CharacterModel>>>> =
        _searchCharacter

    fun fetch(nameStartsWith: String) = viewModelScope.launch {
        safeFetch(nameStartsWith)
    }

    private suspend fun safeFetch(nameStartsWith: String) {
        _searchCharacter.value = ResourceState.Loading()
        try {
            val response = repository.list(nameStartsWith)
            _searchCharacter.value = handleResponse(response)
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _searchCharacter.value =
                    ResourceState.Error(message = "Erro na rede")

                else -> _searchCharacter.value = ResourceState.Error(message = "Erro na conversão")
            }
        }
    }

    private fun handleResponse(response: Response<ServiceResponse<List<CharacterModel>>>): ResourceState<ServiceResponse<List<CharacterModel>>> {
        if (response.isSuccessful) {
            response.body()?.let { value ->
                return ResourceState.Success(value)
            }
        }
        return ResourceState.Error(message = response.message())
    }
}
