package dev.kaestle.descendants.model

import android.net.Uri
import java.time.LocalDate

/**
 * This class represents a Child
 *
 * @param id The unique id of the child
 * @param imageUri The uri to the image of the child
 * @param birthday The birthday of the child
 * @param height The height of the child
 * @param name The name of the child
 * @param parents A list of the child's parents, represented as [Parent]s
 */
class Child(
    id: String,
    imageUri: Uri? = null,
    birthday: LocalDate,
    height: Int,
    name: String,
    var parents: List<Parent>
) : Person(id, imageUri, birthday, height, name) {
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