package com.codurance.training.tasks

interface Action {
    fun execute(args: String?) {}
}

class CommandAction(private val action: (String?) -> Unit) : Action {
    override fun execute(args: String?) = action.invoke(args)
}

class CommandRegistry {
    val registry = mutableMapOf<String, CommandAction>()
}
