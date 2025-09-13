package com.example.simplenote.ui.note

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplenote.ui.theme.AppPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    viewModel: NoteViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var title by remember(uiState.note?.id) { mutableStateOf(uiState.note?.title ?: "") }
    var content by remember(uiState.note?.id) { mutableStateOf(uiState.note?.description ?: "") }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.actionSuccess) {
        if (uiState.actionSuccess) {
            onNavigateBack()
        }
    }

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    if (showDeleteDialog) {
        DeleteNoteDialog(
            onConfirm = {
                viewModel.deleteNote()
                showDeleteDialog = false
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.saveNote(title, content) }) {
                        Icon(Icons.Default.Done, contentDescription = "Save Note", tint = AppPurple)
                    }
                    if (uiState.note != null) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Note")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            uiState.note?.let {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    // This is a simplified date. For better formatting, use a proper date parsing library.
                    Text("Last edited on ${it.updatedAt.substring(11, 16)}", fontSize = 14.sp, color = Color.Gray)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            BasicTextField(
                value = title,
                onValueChange = { title = it },
                textStyle = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground),
                cursorBrush = SolidColor(AppPurple),
                decorationBox = { innerTextField ->
                    if (title.isEmpty()) {
                        Text("Title", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    }
                    innerTextField()
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            BasicTextField(
                value = content,
                onValueChange = { content = it },
                textStyle = TextStyle(fontSize = 16.sp, lineHeight = 24.sp, color = MaterialTheme.colorScheme.onBackground),
                cursorBrush = SolidColor(AppPurple),
                modifier = Modifier.fillMaxSize(),
                decorationBox = { innerTextField ->
                    if (content.isEmpty()) {
                        Text("Feel Free to Write Here...", fontSize = 16.sp, color = Color.Gray)
                    }
                    innerTextField()
                }
            )
        }
    }
}