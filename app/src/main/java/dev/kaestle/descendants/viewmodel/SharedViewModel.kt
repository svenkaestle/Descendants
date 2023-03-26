package dev.kaestle.descendants.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.kaestle.descendants.model.*
import java.time.LocalDate
import java.util.*

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
    fun addPerson(birthday: LocalDate, height: Int, name: String, type: PersonType, children: List<Person>? = listOf(), parents: List<Person>? = listOf()) {
        var newPerson: Person
        when(type) {
            PersonType.CHILD -> {
                newPerson = Child(
                    id = getUUID(),
                    birthday = birthday,
                    height = height,
                    name = name,
                    parents = parents as List<Parent>
                )

                // add the new child to every selected parent
                _persons.value = _persons.value?.map { person ->
                    if (person in (newPerson as Child).parents) {
                        (person as Parent).children = person.children.plus(newPerson as Child)
                    }
                    person
                }
            }
            PersonType.PARENT -> {
                newPerson = Parent(
                    id = getUUID(),
                    birthday = birthday,
                    height = height,
                    name = name,
                    parents = parents as List<Grandparent>,
                    children = children as List<Child>
                )

                // add the new parent to every grandparent as child
                _persons.value = _persons.value?.map { person ->
                    if (person in (newPerson as Parent).children) {
                        (person as Child).parents = person.parents.plus(newPerson as Parent)
                    }
                    person
                }

                // add the new parent to every child as parent
                _persons.value = _persons.value?.map { person ->
                    if (person in (newPerson as Parent).parents) {
                        (person as Grandparent).children = person.children.plus(newPerson as Parent)
                    }
                    person
                }
            }
            PersonType.GRANDPARENT -> {
                newPerson = Grandparent(
                    id = getUUID(),
                    birthday = birthday,
                    height = height,
                    name = name,
                    children = children as List<Parent>
                )

                // add the new grandparent to every child as parent
                _persons.value = _persons.value?.map { person ->
                    if (person in (newPerson).children) {
                        (person as Parent).parents = person.parents.plus(newPerson)
                    }
                    person
                }
                newPerson.children.forEach { child ->
                    child.parents.plus(newPerson).distinct()
                }
            }
        }

        _persons.value = _persons.value?.plus(newPerson)?.sorted() ?: listOf(newPerson)
    }

    /**
     * This will delete the given [Person] from the list of persons
     *
     * @param person The person to delete
     */
    fun deletePerson(person: Person) {
        _persons.value = _persons.value?.minus(person)
    }

    /**
     * Returns a newly created UUID
     *
     * @return A UUID no other person has
     */
    private fun getUUID(): String {
        var uuid: String
        do {
            uuid = UUID.randomUUID().toString()
        } while (_persons.value?.find { person -> person.id == uuid } !== null)
        return uuid

    }
}