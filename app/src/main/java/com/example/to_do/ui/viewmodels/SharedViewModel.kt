package com.example.to_do.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_do.data.models.Priority
import com.example.to_do.data.models.ToDoTask
import com.example.to_do.data.repositories.ToDoRepository
import com.example.to_do.util.Action
import com.example.to_do.util.Constants
import com.example.to_do.util.RequestState
import com.example.to_do.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _action = MutableStateFlow<Action>(Action.NO_ACTION)
    val action : StateFlow<Action> = _action

    /* New Task*/
    private val _newTaskId: MutableState<Int> = mutableIntStateOf(0)
    val newTaskId: State<Int> = _newTaskId

    private val _newTaskTitle: MutableState<String> = mutableStateOf("")
    val newTaskTitle: State<String> = _newTaskTitle

    private val _newTaskDescription: MutableState<String> = mutableStateOf("")
    val newTaskDescription: State<String> = _newTaskDescription

    private val _newTaskPriority: MutableState<Priority> = mutableStateOf(Priority.NONE)
    val newTaskPriority: State<Priority> = _newTaskPriority

    fun setNewTaskTitle(taskTitle: String) {
        if (taskTitle.length <= Constants.MAX_TASK_TITLE_LENGTH) _newTaskTitle.value = taskTitle
    }
    fun setNewTaskId(taskId: Int) {
        _newTaskId.value = taskId
    }
    fun setNewTaskDescription(taskDescription: String) {
        _newTaskDescription.value = taskDescription
    }
    fun setNewTaskPriority(taskPriority: Priority) {
        _newTaskPriority.value = taskPriority
    }
    fun clearNewTaskState(){
        _newTaskId.value = 0
        _newTaskTitle.value = ""
        _newTaskDescription.value = ""
        _newTaskPriority.value = Priority.LOW
    }

    fun updateTaskFields(toDoTask: ToDoTask?){
        if (toDoTask == null){ // Create New Task
            clearNewTaskState()
        }else{ //Existing Task
            fillNewTaskState(toDoTask)
        }
    }
    fun fillNewTaskState(toDoTask: ToDoTask){
        _newTaskId.value = toDoTask.id
        _newTaskTitle.value = toDoTask.title
        _newTaskDescription.value = toDoTask.description
        _newTaskPriority.value = toDoTask.priority
    }

    fun validateFields(): Boolean{
        return _newTaskTitle.value.isNotEmpty() && _newTaskDescription.value.isNotEmpty()
    }
    /* New Task End*/


    fun getAllTask() {
        //viewModelScope is a coroutine scope which tied to lifecycle of sharedViewModel
        try {
            viewModelScope.launch {
                _allTasks.value = RequestState.Loading
                //delay(1000)
                toDoRepository.getAllTasks.collect {
                    _allTasks.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _allTasks.value = RequestState.Error(e)
        }

    }

    fun addTask() {
        viewModelScope.launch {
            if (validateFields()){
                toDoRepository.addTask(
                        ToDoTask(
                            title = _newTaskTitle.value,
                            description = _newTaskDescription.value,
                            priority = _newTaskPriority.value
                        )

                )
                clearNewTaskState()
            }
        }

    }

    fun handleDatabaseAction(action: Action){

        _action.value = action

         when(action){
            Action.ADD -> addTask()
            Action.UPDATE -> updateTask()
            Action.DELETE -> deleteTask()
             Action.DELETE_ALL -> deleteAllTask()
             Action.UNDO -> {}
            else -> {}
        }
    }
    fun deleteTask() {
        viewModelScope.launch {
            if (_selectedTask.value is RequestState.Success){
                toDoRepository.deleteTask((_selectedTask.value as RequestState.Success<ToDoTask?>).data!!)
            }

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

    fun updateTask() {
        if (validateFields()){
            viewModelScope.launch {
                toDoRepository.updateTask(
                    ToDoTask(
                        id = _newTaskId.value,
                        title = _newTaskTitle.value,
                        description = _newTaskDescription.value,
                        priority = _newTaskPriority.value
                    )
                )
                clearNewTaskState()
            }

        }

    }


    fun getSelectedTask(taskId: Int) {
        try {
            viewModelScope.launch {
                _selectedTask.value = RequestState.Loading
                toDoRepository.getSelectedTask(taskId).collect{ selectedTask ->
                    _selectedTask.value = RequestState.Success(selectedTask)
                    updateTaskFields(selectedTask)
                }
            }
        }catch (e: Exception){
            _selectedTask.value = RequestState.Error(e)
            updateTaskFields(null)
        }
    }


}