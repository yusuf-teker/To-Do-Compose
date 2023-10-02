package com.example.to_do.util

object Constants {
    const val DATABASE_TABLE = "todo_table"
    const val DATABASE_NAME = "todo_name"

    const val SPLASH_SCREEN = "splash"
    const val LIST_SCREEN = "list/{action}"
    const val TASK_SCREEN = "task/{taskId}"

    const val LIST_ARGUMENT_KEY = "action"
    const val TASK_ARGUMENT_KEY = "taskId"

    const val CREATE_NEW_TASK_ID = -1
    const val MAX_TASK_TITLE_LENGTH = 30

    const val SPLASH_SCREEN_DELAY = 3000L

    const val PREFERENCE_NAME = "todo_preferences"
    const val PREFERENCE_KEY_SORT = "sort_state"
}