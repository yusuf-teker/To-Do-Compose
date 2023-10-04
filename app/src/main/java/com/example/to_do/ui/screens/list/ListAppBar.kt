package com.example.to_do.ui.screens.list


import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.ContentAlpha
import com.example.to_do.R
import com.example.to_do.components.DisplayAlertDialog
import com.example.to_do.components.PriorityItem
import com.example.to_do.data.models.Priority
import com.example.to_do.ui.theme.LARGE_PADDING
import com.example.to_do.ui.theme.TOP_APP_BAR_HEIGHT
import com.example.to_do.ui.theme.topAppBarBackgroundColor
import com.example.to_do.ui.theme.topAppBarContentColor
import com.example.to_do.ui.viewmodels.SharedViewModel
import com.example.to_do.util.Action
import com.example.to_do.util.SearchAppBarState

@Composable
fun ListAppBar(
    sharedViewModel: SharedViewModel, searchAppBarState: SearchAppBarState, searchTextState: String
) {
    Log.d("ListAppBar searchAppBarState -> ", "$searchAppBarState")
    when (searchAppBarState) {
        SearchAppBarState.CLOSED -> {
            DefaultListAppBar(onSearchClicked = {
                sharedViewModel.setSearchAppBarState(SearchAppBarState.OPENED)
            }, onSortClicked = {
                sharedViewModel.persistSortState(it)
            }, onDeleteAllClicked = {
                sharedViewModel.setAction(Action.DELETE_ALL)
            })
        }

        else -> {
            SearchAppBar(text = searchTextState, onTextChange = { newText ->
                sharedViewModel.setSearchTextState(newText)
                sharedViewModel.searchDatabase(newText)
            },

                onSearchClicked = {
                    sharedViewModel.searchDatabase(it)
                }, onCloseClicked = {
                    sharedViewModel.clearSearchAppBarState()
                })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultListAppBar(
    onSearchClicked: () -> Unit, onSortClicked: (Priority) -> Unit, onDeleteAllClicked: () -> Unit
) {

    TopAppBar(title = {
        Text(text = "Tasks", color = MaterialTheme.colorScheme.topAppBarContentColor)
    },
        colors = topAppBarColors(containerColor = MaterialTheme.colorScheme.topAppBarBackgroundColor),
        actions = {
            ListAppBarActions(onSearchClicked, onSortClicked, onDeleteAllClicked)
        }

    )
}

@Composable
fun ListAppBarActions(
    onSearchClicked: () -> Unit, onSortClicked: (Priority) -> Unit, onDeleteAllConfirmed: () -> Unit
) {
    var openDeleteAllDialog by remember {
        mutableStateOf(false)
    }
    DisplayAlertDialog(title = "Remove all tasks?",
        message = "All tasks will be removed. Are you sure?",
        openDialog = openDeleteAllDialog,
        onCloseDialog = { openDeleteAllDialog = false },
        onPositiveButtonClicked = {
            onDeleteAllConfirmed()
            openDeleteAllDialog = false
        }

    )

    SearchAction(onSearchClicked = onSearchClicked)
    SortAction(onSortClicked = onSortClicked)
    DeleteAllAction(onDeleteAllClicked = {openDeleteAllDialog = true})
}

@Composable
fun SearchAction(onSearchClicked: () -> Unit) {
    IconButton(onClick = onSearchClicked) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = stringResource(R.string.search_tasks),
            tint = MaterialTheme.colorScheme.topAppBarContentColor
        )
    }
}

@Composable
fun SortAction(onSortClicked: (Priority) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    IconButton(onClick = { expanded = true }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_filter_list),
            contentDescription = "Sort Tasks",
            tint = MaterialTheme.colorScheme.topAppBarContentColor
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            Priority.values().slice(setOf(0,2,3)).forEach{
                DropdownMenuItem(text = { PriorityItem(priority = it) }, onClick = {
                    expanded = false
                    onSortClicked(it)
                })
            }
        }
    }
}

@Composable
fun DeleteAllAction(
    onDeleteAllClicked: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    IconButton(onClick = { expanded = true }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_vertical_menu),
            contentDescription = stringResource(R.string.delete_all),
            tint = MaterialTheme.colorScheme.topAppBarContentColor
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = {
                Text(
                    text = stringResource(R.string.delete_all),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(LARGE_PADDING)
                )
            }, onClick = {
                expanded = false
                onDeleteAllClicked()
            })

        }
    }
}

@Composable
@Preview
fun DefaultListAppBarPreview() {
    DefaultListAppBar(onSearchClicked = {}, onSortClicked = {}, onDeleteAllClicked = {})
}

@Composable
@Preview
fun SearchAppBarPreview() {
    SearchAppBar(text = "", onTextChange = {}, onCloseClicked = { /*TODO*/ }, onSearchClicked = {})
}

@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(TOP_APP_BAR_HEIGHT),
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.topAppBarBackgroundColor
    ) {
        TextField(modifier = Modifier.fillMaxWidth(), value = text, onValueChange = {
            onTextChange(it)
        }, placeholder = {
            Text(
                modifier = Modifier.alpha(ContentAlpha.medium),
                text = "Search",
                color = Color.White
            )
        }, textStyle = TextStyle(
            color = MaterialTheme.colorScheme.topAppBarContentColor,
            fontSize = MaterialTheme.typography.bodySmall.fontSize
        ), singleLine = true, leadingIcon = {
            IconButton(modifier = Modifier.alpha(ContentAlpha.disabled), onClick = {}) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Icon",
                    tint = MaterialTheme.colorScheme.topAppBarContentColor
                )
            }
        }, trailingIcon = {
            IconButton(onClick = {
               if (text.isEmpty())
                   onTextChange("")
               else
                   onCloseClicked()
            }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close Icon",
                    tint = MaterialTheme.colorScheme.topAppBarContentColor
                )
            }
        }, keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ), keyboardActions = KeyboardActions(onSearch = {//keyboardSearchClicked
            onSearchClicked(text)
        }), colors = TextFieldDefaults.colors(
            cursorColor = MaterialTheme.colorScheme.topAppBarContentColor,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent
        )
        )
    }
}