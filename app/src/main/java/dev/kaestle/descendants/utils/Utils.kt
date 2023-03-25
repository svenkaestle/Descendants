package dev.kaestle.descendants.utils

import android.util.Log
import dev.kaestle.descendants.model.*

object Utils {
    /**
     * Prints the name total number of descendants for each person of the given list.
     * Will throw an [IllegalArgumentException] if the list holds [Child]ren.
     *
     * @param people A list of persons consisting of [Parent]s and/or [Grandparent]s
     */
    @JvmStatic
    fun <T : Person> getTotalDescendantsCount(people: List<T>) {
        if (people.any { it is Child}) {
            throw IllegalArgumentException("Children are not allowed as elements of the given list.")
        }
        people.forEach { person ->
            when (person) {
                (Parent::class) -> {
                    Log.d("PARENT", "Name: ${person.name}")
                    Log.d("PARENT", "Descendants: ${(person as Parent).children.size}")
                }
                (Grandparent::class) -> {
                    Log.d("GRANDPARENT", "Name: ${person.name}")
                    Log.d("GRANDPARENT", "Descendants: ${(person as Grandparent).children.size + (person as Grandparent).children.flatMap { child -> child.children }.size}")
                }
            }
        }
    }

    /**
     * Returns the [PersonType] for a given [Person].
     *
     * @param person A single person to determine the [PersonType]
     * @return The [PersonType] of the given [Person]
     */
    fun getPersonType(person: Person) : PersonType {
        return when(person) {
            is Child -> {
                PersonType.CHILD
            }
            is Parent -> {
                PersonType.PARENT
            }
            else -> {
                PersonType.GRANDPARENT
            }
        }
    }
}