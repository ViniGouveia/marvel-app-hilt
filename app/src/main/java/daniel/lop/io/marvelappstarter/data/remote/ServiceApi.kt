package daniel.lop.io.marvelappstarter.data.remote

import daniel.lop.io.marvelappstarter.data.model.ServiceResponse
import daniel.lop.io.marvelappstarter.data.model.character.CharacterModel
import daniel.lop.io.marvelappstarter.data.model.comics.ComicModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServiceApi {

    @GET("characters")
    suspend fun list(
        @Query("nameStartsWith") nameStartsWith: String? = null
    ): Response<ServiceResponse<List<CharacterModel>>>

    @GET("characters/{characterId}/comics")
    suspend fun getComics(
        @Path(value = "characterId", encoded = true) characterId: Int
    ): Response<ServiceResponse<List<ComicModel>>>
}
