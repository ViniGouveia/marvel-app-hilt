package daniel.lop.io.marvelappstarter.data.model.character

import androidx.room.Entity
import androidx.room.PrimaryKey
import daniel.lop.io.marvelappstarter.data.model.ThumbnailModel
import java.io.Serializable

@Entity(tableName = "characterModel")
data class CharacterModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: ThumbnailModel
) : Serializable
