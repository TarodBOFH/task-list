package com.codurance.training.tasks

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter

class TaskList(private val `in`: BufferedReader, private val out: PrintWriter) : Runnable {

    private val tasks = mutableMapOf<String, MutableList<Task>>()

    private var lastId: Long = 0

    override fun run() {
        while (true) {
            out.print("> ")
            out.flush()
            val command = try {
                `in`.readLine()
            } catch (e: IOException) {
                throw RuntimeException(e)
            }

            if (command == QUIT) {
                break
            }
            execute(command)
        }
    }

    private fun execute(commandLine: String) {
        val commandRest = commandLine.split(" ".toRegex(), 2).toTypedArray()
        val command = commandRest[0]
        when (command) {
            "show" -> show()
            "add" -> add(commandRest[1])
            "check" -> check(commandRest[1])
            "uncheck" -> uncheck(commandRest[1])
            "help" -> help()
            else -> error(command)
        }
    }

    private fun show() {
        tasks.forEach {
            out.println(it.key)
            it.value.forEach { task ->
                out.println("    [${when (task.isDone) { true -> 'x' else -> ' ' }}] ${task.id}: ${task.description}")
            }
            out.println()
        }
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

    private fun check(idString: String) {
        setDone(idString, true)
    }

    private fun uncheck(idString: String) {
        setDone(idString, false)
    }

    private fun setDone(idString: String, done: Boolean) {
        val id = Integer.parseInt(idString)
        tasks.values.flatten().find { it.id == id.toLong() }?.apply { isDone = done }
            ?: out.println("Could not find a task with an ID of $id.")
    }

    private fun help() {
        out.println("Commands:")
        out.println("  show")
        out.println("  add project <project name>")
        out.println("  add task <project name> <task description>")
        out.println("  check <task ID>")
        out.println("  uncheck <task ID>")
        out.println()
    }

    private fun error(command: String) {
        out.println("I don't know what the command \"${command}\" is.")
    }

    private fun nextId(): Long = ++lastId
}

private const val QUIT = "quit"

fun main() {
    val `in` = BufferedReader(InputStreamReader(System.`in`))
    val out = PrintWriter(System.out)
    TaskList(`in`, out).run()
}
