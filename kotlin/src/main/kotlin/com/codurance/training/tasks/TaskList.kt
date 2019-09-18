package com.codurance.training.tasks

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter

private const val QUIT = "quit"


class AppConfig(
    val `in`: BufferedReader,
    val out: PrintWriter
) {
    val tasks = mutableMapOf<String, MutableList<Task>>()
    val commands = mapOf(
        "show" to ShowAction(tasks, out),
        "help" to HelpAction(out),
        "add" to AddAction(tasks, out),
        "check" to CheckAction(tasks, out, true),
        "uncheck" to CheckAction(tasks, out, false)
    )
    val errorAction = CommandAction {
        checkNotNull(it)
        out.println("I don't know what the command \"${it}\" is.")
    }
}

fun main() {
    val appConfig = AppConfig(BufferedReader(InputStreamReader(System.`in`)), PrintWriter(System.out))

    TaskList(appConfig.`in`, appConfig.out, appConfig.commands, appConfig.errorAction).run()
}


class TaskList(
    private val `in`: BufferedReader,
    private val out: PrintWriter,
    private val commands: Map<String, Action>,
    private val errorAction: Action) : Runnable {

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
        val (command, params) = commandLine.split(" ".toRegex(), 2).let { it[0] to it.getOrNull(1) }

        commands[command]?.execute(params) ?: errorAction.execute(command)
    }
}
