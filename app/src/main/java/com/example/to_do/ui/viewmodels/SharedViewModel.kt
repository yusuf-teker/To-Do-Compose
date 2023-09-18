package com.example.to_do.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_do.data.models.ToDoTask
import com.example.to_do.data.repositories.ToDoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val toDoRepository: ToDoRepository
) : ViewModel() {

    private val _allTasks = MutableStateFlow<List<ToDoTask>>(emptyList())
    val allTasks: StateFlow<List<ToDoTask>> = _allTasks


    fun getAllTask(){
        //viewModelScope is a coroutine scope which tied to lifecycle of sharedViewModel
        viewModelScope.launch {
            toDoRepository.getAllTasks.collect{
                _allTasks.value = it
            }
        }
    }

}