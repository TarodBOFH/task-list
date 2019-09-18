package com.codurance.training.tasks

import com.codurance.training.tasks.actions.AddAction
import com.codurance.training.tasks.actions.CheckAction
import com.codurance.training.tasks.actions.HelpAction
import com.codurance.training.tasks.actions.ShowAction
import java.io.BufferedReader
import java.io.PrintWriter

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
}
