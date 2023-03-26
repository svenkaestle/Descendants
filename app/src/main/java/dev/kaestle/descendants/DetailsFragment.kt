package dev.kaestle.descendants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dev.kaestle.descendants.adapter.ListWithHeaderAdapter
import dev.kaestle.descendants.databinding.FragmentDetailsBinding
import dev.kaestle.descendants.model.Grandparent
import dev.kaestle.descendants.model.Parent
import dev.kaestle.descendants.model.Person
import dev.kaestle.descendants.viewmodel.SharedViewModel

/**
 * The [Fragment] to show a persons details.
 */
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val args: DetailsFragmentArgs by navArgs()
    private var currentPerson: Person? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // save the current person globally based of the 'personIndex' argument
        currentPerson = sharedViewModel.persons.value?.get(args.personIndex)

        // fill the details form if the current person is not null
        currentPerson?.let {
            binding.tilName.editText?.setText(it.name)
            binding.tilAge.apply {
                editText?.setText(it.getCurrentAgeInYears().toString())
                suffixText = if (it.getCurrentAgeInYears() == 1) getString(R.string.year) else getString(R.string.years)
            }
            binding.tilHeight.editText?.setText(it.height.toString())
        }

        with(binding.rvChildren) {
            var childrenWithHeaders = listOf<Any>()
            // add a 'Children' header and all children to the list if the person is a parent
            if (currentPerson is Parent && (currentPerson as Parent).children.isNotEmpty()) {
                childrenWithHeaders = childrenWithHeaders.plus(getString(R.string.children_hint))
                childrenWithHeaders = childrenWithHeaders.plus((currentPerson as Parent).children)
            }
            // add a 'Children' and a 'Grandchildren' header and all (grand-)children to the list if the person is a grandparent
            else if (currentPerson is Grandparent && (currentPerson as Grandparent).children.isNotEmpty()) {
                childrenWithHeaders = childrenWithHeaders.plus(getString(R.string.children_hint))
                childrenWithHeaders = childrenWithHeaders.plus((currentPerson as Grandparent).children)
                // only add grandchildren if there are any and build a flat distinct list
                val grandchildren = (currentPerson as Grandparent).children.flatMap { it.children }.distinct()
                if (grandchildren.isNotEmpty()) {
                    childrenWithHeaders = childrenWithHeaders.plus(getString(R.string.grandparents_hint))
                    childrenWithHeaders = childrenWithHeaders.plus(grandchildren)
                }
            }
            val listAdapter = ListWithHeaderAdapter(childrenWithHeaders)

            layoutManager = LinearLayoutManager(requireContext())
            adapter = listAdapter
        }

        binding.btnDelete.setOnClickListener {
            deletePerson()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Calls the view model to delete the current person
     */
    private fun deletePerson() {
        // delete the current person if there is one
        currentPerson?.let {
            sharedViewModel.deletePerson(it)
            findNavController().navigate(R.id.action_DetailsFragment_to_ListFragment)
        }
    }
}