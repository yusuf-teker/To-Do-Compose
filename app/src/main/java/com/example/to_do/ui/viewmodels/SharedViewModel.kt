package com.example.to_do.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_do.data.models.Priority
import com.example.to_do.data.models.ToDoTask
import com.example.to_do.data.repositories.DataStoreRepository
import com.example.to_do.data.repositories.ToDoRepository
import com.example.to_do.util.Action
import com.example.to_do.util.Constants
import com.example.to_do.util.RequestState
import com.example.to_do.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val toDoRepository: ToDoRepository, private val dataStoreRepository: DataStoreRepository
) : ViewModel() {


    private val _searchAppBarState: MutableState<SearchAppBarState> =
        mutableStateOf(SearchAppBarState.CLOSED)
    val searchAppBarState: State<SearchAppBarState> = _searchAppBarState

    private val _searchTextState: MutableState<String> = mutableStateOf("")
    val searchTextState: State<String> = _searchTextState

    private val _sortState = MutableStateFlow<RequestState<Priority>>(RequestState.Idle)
    val sortState: StateFlow<RequestState<Priority>> = _sortState

    fun readSortState() {

        try {
            viewModelScope.launch(Dispatchers.IO) {
                _sortState.value = RequestState.Loading
                dataStoreRepository.readSortState.map {
                    Priority.valueOf(it)
                }.collect {
                    _sortState.value = RequestState.Success(it)
                }

            }
        } catch (e: Exception) {
            _sortState.value = RequestState.Error(e)
        }
    }

    fun persistSortState(priority: Priority) {
        viewModelScope.launch {
            dataStoreRepository.persistSortState(priority = priority)
        }
    }

    val lowPriorityTasks: StateFlow<List<ToDoTask>> = toDoRepository.sortByLowPriority.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(), //minimum 1 abone olduğu sürece akış devam eder
        initialValue = emptyList()
    )


    val highPriorityTasks: StateFlow<List<ToDoTask>> = toDoRepository.sortByHighPriority.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    fun setSearchTextState(text: String) {
        _searchTextState.value = text
    }

    fun setSearchAppBarState(searchAppBarState: SearchAppBarState) {
        _searchAppBarState.value = searchAppBarState
    }

    private val _allTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val allTasks: StateFlow<RequestState<List<ToDoTask>>> = _allTasks

    private val _searchedTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val searchedTasks: StateFlow<RequestState<List<ToDoTask>>> = _searchedTasks

    private val _selectedTask: MutableStateFlow<RequestState<ToDoTask?>> =
        MutableStateFlow(RequestState.Idle)
    val selectedTask: StateFlow<RequestState<ToDoTask?>> = _selectedTask

    fun setSelectedTask(toDoTask: ToDoTask) {
        _selectedTask.value = RequestState.Success(toDoTask)
    }

    private val _lastDeletedTask: MutableStateFlow<ToDoTask?> = MutableStateFlow(null)
    val lastDeletedTask: StateFlow<ToDoTask?> = _lastDeletedTask


    private val _action = MutableStateFlow<Action>(Action.NO_ACTION)
    val action: StateFlow<Action> = _action

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

    fun setAction(action: Action) {
        _action.value = action
        handleDatabaseAction(action)
    }

    fun setNewTaskDescription(taskDescription: String) {
        _newTaskDescription.value = taskDescription
    }

    fun setNewTaskPriority(taskPriority: Priority) {
        _newTaskPriority.value = taskPriority
    }

    fun clearNewTaskState() {
        _newTaskId.value = 0
        _newTaskTitle.value = ""
        _newTaskDescription.value = ""
        _newTaskPriority.value = Priority.LOW
    }

    fun updateTaskFields(toDoTask: ToDoTask?) {
        clearNewTaskState()
        if (toDoTask != null) { //Existing Task
            fillNewTaskState(toDoTask)
        }
    }

    fun fillNewTaskState(toDoTask: ToDoTask) {
        _newTaskId.value = toDoTask.id
        _newTaskTitle.value = toDoTask.title
        _newTaskDescription.value = toDoTask.description
        _newTaskPriority.value = toDoTask.priority
    }

    fun validateFields(): Boolean {
        return _newTaskTitle.value.isNotEmpty() && _newTaskDescription.value.isNotEmpty()
    }/* New Task End*/


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
            if (validateFields()) {
                toDoRepository.addTask(
                    ToDoTask(
                        title = _newTaskTitle.value,
                        description = _newTaskDescription.value,
                        priority = _newTaskPriority.value
                    )

                )
            }
        }

    }

    private fun addTask(toDoTask: ToDoTask) {
        viewModelScope.launch {
            toDoRepository.addTask(toDoTask)
        }
    }

    private fun undoTask() {
        viewModelScope.launch {
            if (_lastDeletedTask.value != null && _action.value == Action.DELETE) {
                addTask(_lastDeletedTask.value!!)
                _action.value = Action.NO_ACTION
            }
        }
    }

    fun handleDatabaseAction(action: Action) {
        Log.d("SharedViewModel", "handleDatabaseAction - action -> $action ")

        when (action) {
            Action.ADD -> addTask()
            Action.UPDATE -> updateTask()
            Action.DELETE -> deleteTask()
            Action.DELETE_ALL -> deleteAllTask()
            Action.UNDO -> {}
            else -> {}
        }
    }

    private fun deleteTask() {
        Log.d("SharedViewModel", "deleteTask - deleted task -> ${_selectedTask.value} ")
        viewModelScope.launch {
            if (_selectedTask.value is RequestState.Success) {
                toDoRepository.deleteTask((_selectedTask.value as RequestState.Success<ToDoTask?>).data!!)
                _lastDeletedTask.value =
                    (selectedTask.value as RequestState.Success<ToDoTask?>).data!!
            } else {
                ToDoTask(
                    _newTaskId.value,
                    _newTaskTitle.value,
                    _newTaskDescription.value,
                    _newTaskPriority.value
                ).also {
                    toDoRepository.deleteTask(it)
                    _lastDeletedTask.value = it
                }

            }

        }
    }

    fun deleteAllTask() {
        viewModelScope.launch {
            toDoRepository.deleteAllTask()
        }
    }


    fun searchDatabase(searchText: String) {
        try {
            viewModelScope.launch {
                _searchedTasks.value = RequestState.Loading
                toDoRepository.searchDatabase(query = "%$searchText%").collect {
                    _searchedTasks.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _searchedTasks.value = RequestState.Error(e)
        }
        _searchAppBarState.value = SearchAppBarState.TRIGGERED
    }

    private fun updateTask() {
        if (validateFields()) {
            viewModelScope.launch {
                toDoRepository.updateTask(
                    ToDoTask(
                        id = _newTaskId.value,
                        title = _newTaskTitle.value,
                        description = _newTaskDescription.value,
                        priority = _newTaskPriority.value
                    )
                )
            }

        }

    }


    fun getSelectedTask(taskId: Int) {
        Log.d("SharedViewModel", "getSelectedTask - taskId -> ${taskId} ")
        try {
            viewModelScope.launch {
                _selectedTask.value = RequestState.Loading
                toDoRepository.getSelectedTask(taskId).collect { selectedTask ->
                    _selectedTask.value = RequestState.Success(selectedTask)
                    updateTaskFields(selectedTask)
                }
            }
        } catch (e: Exception) {
            _selectedTask.value = RequestState.Error(e)
            updateTaskFields(null)
        }
    }

    fun undoLastDeletedTask() {
        Log.d("SharedViewModel", "undoLastDeletedTask - selectedTask -> ${_selectedTask.value} ")
        undoTask()
    }

    fun clearSearchAppBarState() {
        setSearchTextState("")
        setSearchAppBarState(SearchAppBarState.CLOSED)
    }


}