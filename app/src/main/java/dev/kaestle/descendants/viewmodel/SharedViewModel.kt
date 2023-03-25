package dev.kaestle.descendants.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.kaestle.descendants.model.*
import java.time.LocalDate

class SharedViewModel : ViewModel() {

    private val _persons = MutableLiveData<List<Person>>(listOf())
    val persons: LiveData<List<Person>> = _persons

    /**
     * Adds a new [Person] ([Child], [Parent] or [Grandparent]) depending on the given [PersonType]
     *
     * @param birthday The birthday of the new person
     * @param height The height of the new person
     * @param name The name of the new person
     * @param type The type of the new person
     */
    fun addPerson(birthday: LocalDate, height: Int, name: String, type: PersonType) {
        var newPerson: Person
        when(type) {
            PersonType.CHILD -> {
                newPerson = Child(
                    birthday = birthday,
                    height = height,
                    name = name,
                    parents = listOf()
                )
            }
            PersonType.PARENT -> {
                newPerson = Parent(
                    birthday = birthday,
                    height = height,
                    name = name,
                    parents = listOf(),
                    children = listOf()
                )
            }
            PersonType.GRANDPARENT -> {
                newPerson = Grandparent(
                    birthday = birthday,
                    height = height,
                    name = name,
                    children = listOf()
                )
            }
        }

        _persons.value = _persons.value?.plus(newPerson) ?: listOf(newPerson)
    }

    /**
     * This will delete the given [Person] from the list of persons
     *
     * @param person The person to delete
     */
    fun deletePerson(person: Person) {
        _persons.value = _persons.value?.minus(person)
    }
}