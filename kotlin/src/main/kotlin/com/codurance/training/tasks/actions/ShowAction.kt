package com.codurance.training.tasks.actions

import com.codurance.training.tasks.Task
import java.io.PrintWriter

class ShowAction(val tasks: Map<String, MutableList<Task>>, private val out: PrintWriter) :
    Action {

    override fun execute(args: String?) = show()

    private fun show() {
        tasks.forEach {
            out.println(it.key)
            it.value.forEach { task ->
                out.println("    [${when (task.isDone) {
                    true -> 'x'
                    else -> ' '
                }}] ${task.id}: ${task.description}")
            }
            out.println()
        }
    }
}
