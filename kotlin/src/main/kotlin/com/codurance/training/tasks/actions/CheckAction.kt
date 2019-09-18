package com.codurance.training.tasks.actions

import com.codurance.training.tasks.Task
import java.io.PrintWriter

class CheckAction(
    private val tasks:Map<String, MutableList<Task>>,
    private val out: PrintWriter,
    private val status: Boolean
) : Action {
    override fun execute(args: String?) {
        checkNotNull(args)
        setDone(args, status)
    }

    private fun setDone(idString: String, done: Boolean) {
        val id = Integer.parseInt(idString)
        tasks.values.flatten().find { it.id == id.toLong() }?.apply { isDone = done }
            ?: out.println("Could not find a task with an ID of $id.")
    }
}
