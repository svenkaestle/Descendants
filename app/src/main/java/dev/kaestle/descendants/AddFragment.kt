package dev.kaestle.descendants

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dev.kaestle.descendants.databinding.FragmentAddBinding
import dev.kaestle.descendants.model.*
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

    private var currentParents: List<Person> = listOf()
    private var currentChildren: List<Person> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val typeAdapter = ArrayAdapter(requireContext(), R.layout.simple_string_item, PersonType.values().map { type -> type.value })
        binding.actvType.apply {
            setAdapter(typeAdapter)
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    // Do nothing
                }

                override fun afterTextChanged(p0: Editable?) {
                    // Do nothing
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    initializeRelationshipDropdowns(PersonType.valueOf(s.toString().uppercase()))
                }
            })
        }

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

        binding.tilChildren.editText?.setOnClickListener {
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
            type = PersonType.valueOf(type.uppercase()),
            children = currentChildren,
            parents = currentParents
        )
        findNavController().navigate(R.id.action_addFragment_to_ListFragment)
    }

    /**
     * Returns the all parents, based on the given [PersonType]
     *
     * @param personType The [PersonType] of the selected [Person]
     * @return A list of [Parent]s if the [PersonType] was [PersonType.CHILD] or a list of [Grandparent]s if the [PersonType] was [PersonType.PARENT]
     */
    private fun getParents(personType: PersonType) : List<Person> {
        return when (personType) {
            PersonType.CHILD -> {
                sharedViewModel.persons.value?.filterIsInstance<Parent>() ?: listOf()
            }
            PersonType.PARENT -> {
                sharedViewModel.persons.value?.filterIsInstance<Grandparent>() ?: listOf()
            }
            else ->
                listOf()
        }
    }

    /**
     * Returns the all children, based on the given [PersonType]
     *
     * @param personType The [PersonType] of the selected [Person]
     * @return A list of [Child]ren if the [PersonType] was [PersonType.PARENT] or a list of [Parent]s if the [PersonType] was [PersonType.GRANDPARENT]
     */
    private fun getChildren(personType: PersonType) : List<Person> {
        return when (personType) {
            PersonType.PARENT -> {
                sharedViewModel.persons.value?.filterIsInstance<Child>() ?: listOf()
            }
            PersonType.GRANDPARENT -> {
                sharedViewModel.persons.value?.filterIsInstance<Parent>() ?: listOf()
            }
            else ->
                listOf()
        }
    }

    /**
     * Based on the given [PersonType] the drop down menus for children and parents are visible or not.
     * Furthermore, the selectable children and/or parents will be filtered.
     *
     * @param personType The [PersonType] of the selected [Person]
     */
    private fun initializeRelationshipDropdowns(personType: PersonType) {
        // reset current parents and current children lists
        currentChildren = listOf()
        currentParents = listOf()

        // get the filtered children/parents and reset the adapters
        val filteredChildren = getChildren(personType)
        val childrenAdapter = ArrayAdapter(requireContext(), R.layout.simple_string_item, filteredChildren.map { child ->  child.name})
        binding.mactvChildren.apply {
            setAdapter(childrenAdapter)
            threshold = 1
            setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        }

        val filteredParents = getParents(personType)
        val parentsAdapter = ArrayAdapter(requireContext(), R.layout.simple_string_item, filteredParents.map { parent -> parent.name })
        binding.mactvParents.apply {
            setAdapter(parentsAdapter)
            threshold = 1
            setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        }

        // hide children dropdown if current type is CHILD
        if (personType == PersonType.CHILD) {
            binding.tilChildren.visibility = View.GONE
        }

        // hide parents dropdown if current type is GRANDPARENT
        if (personType == PersonType.GRANDPARENT) {
            binding.tilParents.visibility = View.GONE
        }

        // show the parents dropdown list if the current type is CHILD or PARENT
        if (personType == PersonType.CHILD || personType == PersonType.PARENT) {
            binding.tilParents.apply {
                visibility = View.VISIBLE
                isEnabled = filteredParents.isNotEmpty()
                editText?.apply {
                    text = null
                    setOnClickListener {
                        getMultiChoiceAlertDialog(filteredChildren, getString(R.string.dialog_title_select_parents)) { persons ->
                            currentParents = persons
                            this.setText(persons.joinToString { it.name })
                        }
                    }
                }
            }
        }

        // show the children dropdown list if the current type is PARENT or GRANDPARENT
        if (personType == PersonType.PARENT || personType == PersonType.GRANDPARENT) {
            binding.tilChildren.apply {
                visibility = View.VISIBLE
                isEnabled = filteredChildren.isNotEmpty()
                editText?.apply {
                    text = null
                    setOnClickListener {
                        getMultiChoiceAlertDialog(filteredChildren, getString(R.string.dialog_title_select_children)) { persons ->
                            currentChildren = persons
                            this.setText(persons.joinToString { it.name })
                        }
                    }
                }
            }
        }
    }

    /**
     * Build and show an alert dialog that holds a multiple choice list of parents or children, based on the persons argument
     *
     * @param persons A list of [Person]s to select from
     * @param title The title of the dialog
     * @param positiveCallback This callback gets invoked on a positive button click and returns the list of selected [Person]s
     */
    private fun getMultiChoiceAlertDialog(persons: List<Person>, title: String, positiveCallback: (List<Person>) -> Unit) {
        val checkedPersons = persons.map { false }.toMutableList()
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMultiChoiceItems(persons.map { it.name }.toTypedArray(), null) { dialog, index, isChecked ->
            checkedPersons[index] = isChecked
        }
        builder.setPositiveButton("OK") { _, _ ->
            positiveCallback.invoke(persons.filterIndexed { index, _ -> checkedPersons[index] })
        }
        builder.setNeutralButton("Cancel") { _, _ ->
            // Do nothing
        }

        val alert = builder.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }
}