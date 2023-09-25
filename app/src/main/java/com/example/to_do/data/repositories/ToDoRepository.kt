package com.example.to_do.data.repositories

import com.example.to_do.data.ToDoDao
import com.example.to_do.data.models.ToDoTask
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped//TodoRepository(which injected in sharedViewModel) will be alive as long as the shared view model
class ToDoRepository @Inject constructor(private val toDoDao: ToDoDao){

    val getAllTasks: Flow<List<ToDoTask>> = toDoDao.getAllTasks()
    val sortByLowPriority: Flow<List<ToDoTask>> = toDoDao.sortByLowPriority()
    val sortByHighPriority:  Flow<List<ToDoTask>> = toDoDao.sortByHighPriority()

    fun getSelectedTask(taskId: Int): Flow<ToDoTask> = toDoDao.getSelectedTask(taskId)

    suspend fun addTask(toDoTask: ToDoTask) {
        toDoDao.addTask(toDoTask = toDoTask)
    }

    suspend fun updateTask(toDoTask: ToDoTask){
        toDoDao.updateTask(toDoTask = toDoTask)
    }

    suspend fun deleteTask(toDoTask: ToDoTask){
        toDoDao.deleteTask(toDoTask = toDoTask)
    }
    suspend fun deleteAllTask(){
        toDoDao.deleteAllTasks()
    }

    fun searchDatabase(query: String): Flow<List<ToDoTask>>{
        return toDoDao.searchDatabase(searchQuery = query)
    }
}