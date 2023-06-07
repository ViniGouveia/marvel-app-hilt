package daniel.lop.io.marvelappstarter.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import daniel.lop.io.marvelappstarter.R
import daniel.lop.io.marvelappstarter.data.model.character.CharacterModel
import daniel.lop.io.marvelappstarter.databinding.FragmentDetailsCharacterBinding
import daniel.lop.io.marvelappstarter.ui.adapters.ComicAdapter
import daniel.lop.io.marvelappstarter.ui.base.BaseFragment
import daniel.lop.io.marvelappstarter.ui.state.ResourceState
import daniel.lop.io.marvelappstarter.util.hide
import daniel.lop.io.marvelappstarter.util.limitDescription
import daniel.lop.io.marvelappstarter.util.loadImage
import daniel.lop.io.marvelappstarter.util.show
import daniel.lop.io.marvelappstarter.util.toast
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class DetailsCharacterFragment :
    BaseFragment<FragmentDetailsCharacterBinding, DetailsCharacterViewModel>() {

    override val viewModel: DetailsCharacterViewModel by viewModels()
    private val args: DetailsCharacterFragmentArgs by navArgs()
    private val comicAdapter by lazy { ComicAdapter() }

    private lateinit var characterModel: CharacterModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        characterModel = args.character
        viewModel.fetch(characterModel.id)
        setupRecyclerView()
        onLoadedCharacter(characterModel)
        collectObserver()
        descriptionCharacter()
    }

    private fun descriptionCharacter() {
        binding.tvDescriptionCharacterDetails.setOnClickListener {
            onShowDialog(characterModel)
        }
    }

    private fun onShowDialog(characterModel: CharacterModel) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(characterModel.name)
            .setMessage(characterModel.description)
            .setNegativeButton(getString(R.string.close_dialog)) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun collectObserver() = lifecycleScope.launch {
        viewModel.details.collect { result ->
            when (result) {
                is ResourceState.Success -> {
                    binding.progressBarDetail.hide()
                    result.data?.let {
                        if (it.data.results.isNotEmpty()) comicAdapter.comic =
                            it.data.results.toList()
                        else toast(getString(R.string.empty_list_comics))
                    }
                }

                is ResourceState.Error -> {
                    binding.progressBarDetail.hide()
                    result.message?.let {
                        Timber.tag("DetailsCharacterFrag").e("Error -> $it")
                        toast(message = it)
                    }
                }

                is ResourceState.Loading -> {
                    binding.progressBarDetail.show()
                }

                else -> {}
            }
        }
    }

    private fun onLoadedCharacter(characterModel: CharacterModel) = with(binding) {
        tvNameCharacterDetails.text = characterModel.name
        tvDescriptionCharacterDetails.text = characterModel.description.ifEmpty {
            requireContext().getString(
                R.string.text_description_empty
            )
        }.limitDescription(100)
        loadImage(
            imgCharacterDetails,
            characterModel.thumbnail.path,
            characterModel.thumbnail.extension
        )
    }

    private fun setupRecyclerView() = with(binding) {
        rvComics.apply {
            adapter = comicAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_details, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                viewModel.insert(characterModel)
                toast(getString(R.string.saved_successfully))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailsCharacterBinding =
        FragmentDetailsCharacterBinding.inflate(inflater, container, false)
}
