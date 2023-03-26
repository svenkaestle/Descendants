package dev.kaestle.descendants.model

import java.time.LocalDate

/**
 * This class represents a Parent
 *
 * @param id The unique id of the parent
 * @param birthday The birthday of the parent
 * @param height The height of the parent
 * @param name The name of the parent
 * @param children A list of the parent's parents, represented as [Grandparent]s
 * @param children A list of the parent's children, represented as [Child]ren
 */
class Parent(
    id: String,
    birthday: LocalDate,
    height: Int,
    name: String,
    var parents: List<Grandparent>,
    var children: List<Child>
) : Person(id, birthday, height, name) {
    /**
     * Prints the parents of this parent to the console
     */
    fun printParents() {
        this.parents.forEach { parent ->
            parent.printToConsole("PARENT")
        }
    }

    /**
     * Prints the children of this parent to the console
     */
    fun printChildren() {
        this.children.forEach { child ->
            child.printToConsole("CHILD")
        }
    }
}