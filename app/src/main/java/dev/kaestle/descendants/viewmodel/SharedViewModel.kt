package dev.kaestle.descendants.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.kaestle.descendants.model.Person

class SharedViewModel : ViewModel() {

    private val _persons = MutableLiveData<List<Person>>(listOf())
    val persons: LiveData<List<Person>> = _persons

    fun addPerson(person: Person) {
        _persons.value = _persons.value?.plus(person) ?: listOf(person)
    }
}