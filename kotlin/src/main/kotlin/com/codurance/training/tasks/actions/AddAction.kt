package com.codurance.training.tasks.actions

import com.codurance.training.tasks.Task
import java.io.PrintWriter

class AddAction(val tasks: MutableMap<String, MutableList<Task>>, private val out: PrintWriter) :
    Action {

    private var lastId: Long = 0

    override fun execute(args: String?) {
        checkNotNull(args)
        add(args)
    }

    private fun add(commandLine: String) {
        val subcommandRest = commandLine.split(" ".toRegex(), 2).toTypedArray()
        val subcommand = subcommandRest[0]
        if (subcommand == "project") {
            addProject(subcommandRest[1])
        } else if (subcommand == "task") {
            val projectTask = subcommandRest[1].split(" ".toRegex(), 2).toTypedArray()
            addTask(projectTask[0], projectTask[1])
        }
    }

    private fun addProject(name: String) {
        tasks[name] = mutableListOf()
    }

    private fun addTask(project: String, description: String) {
        tasks[project]?.add(Task(nextId(), description))
            ?: out.println("Could not find a project with the name \"${project}\".")
    }

    private fun nextId(): Long = ++lastId
}
