package dev.kaestle.descendants.model

import java.time.LocalDate

/**
 * This class represents a Child
 *
 * @param birthday The birthday of the child
 * @param height The height of the child
 * @param name The name of the child
 * @param parents A list of the child's parents, represented as [Parent]s
 */
class Child(
    birthday: LocalDate,
    height: Int,
    name: String,
    val parents: List<Parent>
) : Person(birthday, height, name) {
    /**
     * Prints the parents of this child to the console
     */
    fun printParents() {
        this.parents.forEach { parent ->
            parent.printToConsole("PARENT")
        }
    }

    /**
     * Prints the grandparents of this child to the console
     */
    fun printGrandparents() {
        this.parents.flatMap { parent -> parent.parents }.forEach { grandParent ->
            grandParent.printToConsole("GRANDPARENT")
        }
    }
}