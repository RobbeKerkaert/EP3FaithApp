package com.example.faith.ui.reaction_edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.faith.MainActivity
import com.example.faith.R
import com.example.faith.database.FaithDatabase
import com.example.faith.databinding.ReactionEditFragmentBinding
import com.example.faith.domain.Reaction

class ReactionEditFragment : Fragment() {

    private lateinit var viewModel: ReactionEditViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // For action bar title
        (activity as MainActivity).supportActionBar?.title = "Reaction Edit"

        // Necessary starter things
        val binding = DataBindingUtil.inflate<ReactionEditFragmentBinding>(
            inflater,
            R.layout.reaction_edit_fragment,
            container,
            false
        )

        val arguments = ReactionEditFragmentArgs.fromBundle(requireArguments())

        val application = requireNotNull(this.activity).application
        val reactionDataSource = FaithDatabase.getInstance(application).reactionDatabaseDao

        val viewModelFactory = ReactionEditViewModelFactory(arguments.reactionId, reactionDataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ReactionEditViewModel::class.java)

        // OnClickListeners
        binding.submitEditButton.setOnClickListener {
            editData(binding)
            it.findNavController().navigate(ReactionEditFragmentDirections.actionReactionEditFragmentToPostDetailFragment(arguments.postId))
        }

        return binding.root
    }

    private fun editData(binding: ReactionEditFragmentBinding) {
        val textValue = binding.editReactionText.getText().toString()

        if (!textValue.isNullOrEmpty()) {
            val reaction = Reaction(
                0,
                0,
                0,
                textValue
            )
            viewModel.editReaction(reaction)
        }
    }
}
