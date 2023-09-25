package com.example.to_do.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_do.data.models.ToDoTask
import com.example.to_do.data.repositories.ToDoRepository
import com.example.to_do.util.RequestState
import com.example.to_do.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val toDoRepository: ToDoRepository
) : ViewModel() {


    private val _searchAppBarState: MutableState<SearchAppBarState> =
        mutableStateOf(SearchAppBarState.CLOSED)
    val searchAppBarState: State<SearchAppBarState> = _searchAppBarState

    private val _searchTextState: MutableState<String> = mutableStateOf("")
    val searchTextState: State<String> = _searchTextState

    fun setSearchTextState(text: String) {
        _searchTextState.value = text
    }

    fun setSearchAppBarState(searchAppBarState: SearchAppBarState) {
        _searchAppBarState.value = searchAppBarState
    }

    private val _allTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val allTasks: StateFlow<RequestState<List<ToDoTask>>> = _allTasks

    private val _selectedTask: MutableStateFlow<RequestState<ToDoTask?>> = MutableStateFlow(RequestState.Idle)
    val selectedTask: StateFlow<RequestState<ToDoTask?>> = _selectedTask

    fun getAllTask() {
        //viewModelScope is a coroutine scope which tied to lifecycle of sharedViewModel
        try {
            viewModelScope.launch {
                _allTasks.value = RequestState.Loading
                delay(1000)
                toDoRepository.getAllTasks.collect {
                    _allTasks.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _allTasks.value = RequestState.Error(e)
        }

    }

    fun addTask(toDoTask: ToDoTask) {
        viewModelScope.launch {
            toDoRepository.addTask(toDoTask)
        }
    }

    fun deleteTask(toDoTask: ToDoTask) {
        viewModelScope.launch {
            toDoRepository.deleteTask(toDoTask = toDoTask)
        }
    }

    fun deleteAllTask() {
        viewModelScope.launch {
            toDoRepository.deleteAllTask()
        }
    }

    fun searchDatabase(searchText: String) {
        viewModelScope.launch {
            toDoRepository.searchDatabase(query = searchText)
        }
    }

    fun updateTask(toDoTask: ToDoTask) {
        viewModelScope.launch {
            toDoRepository.updateTask(toDoTask)
        }
    }


    fun getSelectedTask(taskId: Int) {
        try {
            viewModelScope.launch {
                _selectedTask.value = RequestState.Loading
                toDoRepository.getSelectedTask(taskId).collect{ selectedTask ->
                    _selectedTask.value = RequestState.Success(selectedTask)
                }
            }
        }catch (e: Exception){
            _selectedTask.value = RequestState.Error(e)
        }
    }


}