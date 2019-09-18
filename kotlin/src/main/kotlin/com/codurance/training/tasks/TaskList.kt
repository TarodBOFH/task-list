package com.codurance.training.tasks

import com.codurance.training.tasks.actions.Action
import java.io.BufferedReader
import java.io.IOException
import java.io.PrintWriter

private const val QUIT = "quit"

class TaskList(
    private val `in`: BufferedReader,
    private val out: PrintWriter,
    private val commands: Map<String, Action>
) : Runnable {

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

        commands[command]?.execute(params) ?: out.println("I don't know what the command \"$command\" is.")
    }
}
