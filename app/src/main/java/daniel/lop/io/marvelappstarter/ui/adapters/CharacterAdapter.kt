package daniel.lop.io.marvelappstarter.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import daniel.lop.io.marvelappstarter.R
import daniel.lop.io.marvelappstarter.data.model.character.CharacterModel
import daniel.lop.io.marvelappstarter.databinding.ItemCharacterBinding
import daniel.lop.io.marvelappstarter.util.limitDescription
import daniel.lop.io.marvelappstarter.util.loadImage

class CharacterAdapter : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    inner class CharacterViewHolder(val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<CharacterModel>() {
        override fun areItemsTheSame(oldItem: CharacterModel, newItem: CharacterModel): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: CharacterModel, newItem: CharacterModel): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.name == newItem.name &&
                    oldItem.description == newItem.description &&
                    oldItem.thumbnail.path == newItem.thumbnail.path &&
                    oldItem.thumbnail.extension == newItem.thumbnail.extension
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    var character: List<CharacterModel>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(
            ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount() = character.size

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = character[position]
        holder.binding.apply {
            tvNameCharacter.text = character.name
            tvDescriptionCharacter.text =
                character.description.ifEmpty {
                    holder.itemView.context.getString(
                        R.string.text_description_empty
                    )
                }.limitDescription(100)

            loadImage(imgCharacter, character.thumbnail.path, character.thumbnail.extension)
        }

        holder.itemView.setOnClickListener { onItemClickListener?.let { it(character) } }
    }

    private var onItemClickListener: ((CharacterModel) -> Unit)? = null

    fun setOnClickListener(listener: (CharacterModel) -> Unit) {
        onItemClickListener = listener
    }

    fun getCharacterPosition(position: Int) = character[position]
}
