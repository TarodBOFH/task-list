package com.codurance.training.tasks

import java.io.PrintWriter

class HelpAction(private val out: PrintWriter) : Action {
    override fun execute(args: String?) = help()

    private fun help() {
        out.println("Commands:")
        out.println("  show")
        out.println("  add project <project name>")
        out.println("  add task <project name> <task description>")
        out.println("  check <task ID>")
        out.println("  uncheck <task ID>")
        out.println()
    }
}
