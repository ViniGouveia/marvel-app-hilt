package daniel.lop.io.marvelappstarter.data.model.comics

import daniel.lop.io.marvelappstarter.data.model.ThumbnailModel
import java.io.Serializable

data class ComicModel(
    val id: Int,
    val title: String,
    val description: String,
    val thumbnail: ThumbnailModel
) : Serializable
