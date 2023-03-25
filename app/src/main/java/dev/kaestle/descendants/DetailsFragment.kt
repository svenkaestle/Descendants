package dev.kaestle.descendants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dev.kaestle.descendants.databinding.FragmentDetailsBinding
import dev.kaestle.descendants.model.Person
import dev.kaestle.descendants.utils.Utils
import dev.kaestle.descendants.viewmodel.SharedViewModel
import java.time.format.DateTimeFormatter

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
            binding.tilBirthday.editText?.setText(it.birthday.format(DateTimeFormatter.ofPattern(getString(R.string.date_format))))
            binding.tilHeight.editText?.setText(it.height.toString())
            binding.tilType.editText?.setText(Utils.getPersonType(it).value)
        }

        binding.btnDelete.setOnClickListener {
            deletePerson()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun deletePerson() {
        // delete the current person if there is one
        currentPerson?.let {
            sharedViewModel.deletePerson(it)
            findNavController().navigate(R.id.action_DetailsFragment_to_ListFragment)
        }
    }
}