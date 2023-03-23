package dev.kaestle.descendants.model

import java.time.LocalDate

/**
 * This class represents a Grandparent
 *
 * @param birthday The birthday of the grandparent
 * @param height The height of the grandparent
 * @param name The name of the grandparent
 * @param children A list of the grandparent's children, represented as [Parent]s
 */
class Grandparent(
    birthday: LocalDate,
    height: Int,
    name: String,
    val children: List<Parent>
) : Person(birthday, height, name) {
    /**
     * Prints the children of this grandparent to the console
     */
    fun printChildren() {
        this.children.forEach { child ->
            child.printToConsole("CHILD")
        }
    }

    /**
     * Prints the grandchildren of this grandparent to the console
     */
    fun printGrandchildren() {
        this.children.flatMap { child -> child.children }.forEach { grandchild ->
            grandchild.printToConsole("GRANDCHILD")
        }
    }
}