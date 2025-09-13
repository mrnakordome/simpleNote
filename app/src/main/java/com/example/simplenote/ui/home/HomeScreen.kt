package com.example.simplenote.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simplenote.data.remote.response.Note
import com.example.simplenote.ui.theme.AppPurple
import kotlin.random.Random

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel(),
    onAddNote: () -> Unit,
    onNoteClick: (noteId: String) -> Unit,
    onOpenSettings: () -> Unit
) {
    val uiState by homeViewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(BottomTab.Home) }
    var query by remember { mutableStateOf("") }

    val filtered = remember(query, uiState.notes) {
        if (query.isBlank()) uiState.notes
        else uiState.notes.filter { it.title.contains(query, true) || it.description.contains(query, true) }
    }

    Scaffold(
        floatingActionButton = {
            if (selectedTab == BottomTab.Home) {
                FloatingActionButton(
                    onClick = onAddNote,
                    containerColor = AppPurple,
                    contentColor = Color.White,
                    shape = CircleShape
                ) { Icon(Icons.Default.Add, contentDescription = "Add Note") }
            }
        },
        bottomBar = {
            BottomBar(
                selected = selectedTab,
                onSelect = { tab ->
                    if (tab == BottomTab.Settings) {
                        onOpenSettings()
                    } else {
                        selectedTab = tab
                    }
                }
            )
        }
    ) { padding ->
        // The content area now only shows the Home content or a loader
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            HomeContent(
                modifier = Modifier.padding(padding),
                query = query,
                onQuery = { query = it },
                notes = filtered,
                onNoteClick = { onNoteClick(it.toString()) }
            )
        }
    }
}

private enum class BottomTab { Home, Settings }

@Composable
private fun BottomBar(selected: BottomTab, onSelect: (BottomTab) -> Unit) {
    BottomAppBar(
        tonalElevation = 0.dp,
        containerColor = Color.White,
        contentPadding = PaddingValues(horizontal = 24.dp)
    ) {
        NavigationBarItem(
            selected = selected == BottomTab.Home,
            onClick = { onSelect(BottomTab.Home) },
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AppPurple,
                selectedTextColor = AppPurple,
                indicatorColor = Color.Transparent
            )
        )
        Spacer(Modifier.weight(1f))
        NavigationBarItem(
            selected = false, // Settings tab is never in a "selected" state
            onClick = { onSelect(BottomTab.Settings) },
            icon = { Icon(Icons.Default.Settings, null) },
            label = { Text("Settings") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AppPurple,
                selectedTextColor = AppPurple,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    query: String,
    onQuery: (String) -> Unit,
    notes: List<Note>,
    onNoteClick: (noteId: Int) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQuery,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            placeholder = { Text("Search...") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFE7E7EE),
                unfocusedBorderColor = Color(0xFFE7E7EE),
                cursorColor = AppPurple
            )
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Notes",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E1E1E),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(16.dp))

        if (notes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 56.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(Modifier.height(140.dp))
                    Text(
                        "Start Your Journey",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1E1E1E)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Every big step start with small step.\nNotes your first idea and start your journey!",
                        color = Color(0xFF9A9AA0),
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(notes, key = { it.id }) { note ->
                    NoteCard(note, onClick = { onNoteClick(note.id) })
                }
            }
        }
    }
}

@Composable
private fun NoteCard(note: Note, onClick: () -> Unit) {
    val colors = listOf(Color(0xFFFFF3C4), Color(0xFFD4EFFF), Color(0xFFFFD6D6), Color(0xFFE4D4FF))
    val randomColor = remember(note.id) { colors[Random.nextInt(colors.size)] }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(randomColor, RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Text(
                text = note.title,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1E1E),
                lineHeight = 20.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = note.description,
                color = Color(0xFF4C4C55),
                fontSize = 13.sp,
                lineHeight = 18.sp,
                maxLines = 6,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}