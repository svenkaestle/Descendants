package dev.kaestle.descendants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dev.kaestle.descendants.adapter.ListAdapter
import dev.kaestle.descendants.databinding.FragmentListBinding
import dev.kaestle.descendants.viewmodel.SharedViewModel

/**
 * The [Fragment] to show all created persons.
 */
class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.rvPersons) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ListAdapter(sharedViewModel.persons.value?.sorted() ?: listOf())
        }

        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_ListFragment_to_addFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}