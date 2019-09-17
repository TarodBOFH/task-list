package com.codurance.training.tasks

data class Task(val id: Long, val description: String) {
    var isDone: Boolean = false
}
