package com.codurance.training.tasks

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter

fun main() {
    val appConfig = AppConfig(BufferedReader(InputStreamReader(System.`in`)), PrintWriter(System.out))
    TaskList(appConfig.`in`, appConfig.out, appConfig.commands).run()
}
