package dev.kaestle.descendants

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dev.kaestle.descendants.databinding.FragmentAddBinding
import dev.kaestle.descendants.model.PersonType
import dev.kaestle.descendants.viewmodel.SharedViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * The [Fragment] to add a new person.
 */
class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val typeAdapter = ArrayAdapter(requireContext(), R.layout.type_item, PersonType.values().map { type -> type.value })
        binding.actvType.setAdapter(typeAdapter)

        binding.btnSave.setOnClickListener {
            addNewPerson()
        }

        binding.tilBirthday.editText?.setOnClickListener {
            val calendar = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // populate calendar with the selected values
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                // set the birthday's text value to the selected date as formatted value
                binding.tilBirthday.editText?.setText(SimpleDateFormat(getString(R.string.date_format), Locale.GERMANY).format(calendar.time))
            }

            // show the date picker dialog
            DatePickerDialog(requireActivity(), dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Uses the values of the form to create and add a new person
     */
    private fun addNewPerson() {
        // get the string values of the input fields
        val birthday = binding.tilBirthday.editText?.text.toString()
        val height = binding.tilHeight.editText?.text.toString()
        val name = binding.tilName.editText?.text.toString()
        val type = binding.tilType.editText?.text.toString()

        // set and error to every empty field and return
        if (birthday.isEmpty() || height.isEmpty() || name.isEmpty() || type.isEmpty()) {
            binding.tilBirthday.error = if (birthday.isEmpty()) "Birthday is required!" else null
            binding.tilHeight.error = if (height.isEmpty()) "Height is required!" else null
            binding.tilName.error = if (name.isEmpty()) "Name is required!" else null
            binding.tilType.error = if (type.isEmpty()) "Type is required!" else null
            return
        }

        // add a new person if there was no error and return back to the main fragment
        sharedViewModel.addPerson(
            birthday = LocalDate.parse(birthday, DateTimeFormatter.ofPattern(getString(R.string.date_format))),
            height = height.toInt(),
            name = name,
            type = PersonType.valueOf(type.uppercase())
        )
        findNavController().navigate(R.id.action_addFragment_to_ListFragment)
    }
}