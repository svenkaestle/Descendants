package dev.kaestle.descendants.model

import android.util.Log
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

/**
 * This abstract class represents a base Person
 *
 * @param id The unique id of the person
 * @param birthday The birthday of the person
 * @param height The height of the person
 * @param name The name of the person
 */
abstract class Person (
    val id: String,
    val birthday: LocalDate,
    val height: Int,
    val name: String
) : Comparable<Person>{
    /**
     * Calculates the current age in years of a person.
     *
     * @return The current age in years as an Int value.
     */
    fun getCurrentAgeInYears(): Int {
        return Period.between(
            birthday,
            LocalDate.now()
        ).years
    }

    fun printToConsole(tag: String) {
        Log.d(tag, this.toString())
    }

    /**
     * Compares two persons based on their name. In case of same name, comparison will based on their height.
     */
    override fun compareTo(other: Person): Int {
        return compareValuesBy(this, other,
            Person::name, Person::height)
    }

    /**
     * Defines the standard string representation that will be returned in the format of:
     *
     *  Name: John Doe
     *  Birthday: 01.01.1970
     *  Height: 180
     */
    override fun toString(): String {
        return String.format(
            "Name: %s%nBirthday: %s%nHeight: %s",
            this.name,
            this.birthday.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
            this.height
        )
    }
}