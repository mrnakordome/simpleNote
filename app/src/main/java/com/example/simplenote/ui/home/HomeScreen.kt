package com.example.simplenote.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplenote.ui.theme.AppPurple

// --- مدل خیلی ساده برای نمایش نمونه یادداشت‌ها
data class NoteUi(
    val id: String,
    val title: String,
    val body: String,
    val tint: Color
)

// نمونه داده (برای وقتی API نداریم)
private val sampleNotes = listOf(
    NoteUi(
        id = "1",
        title = "💡 New Product\nIdea Design",
        body = "Create a mobile app UI Kit that provide a basic notes functionality but with some improvement.",
        tint = Color(0xFFFFF3C4)
    ),
    NoteUi(
        id = "2",
        title = "💡 New Product\nIdea Design",
        body = "There will be a choice to select what kind of notes that user needed...",
        tint = Color(0xFFFFE7A0)
    )
)

@Composable
fun HomeScreen(
    // وقتی بعداً API وصل شد این‌ها رو می‌گیریم
    initialNotes: List<NoteUi> = sampleNotes,
    onAddNote: () -> Unit = {},
    onOpenSettingsSystem: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(BottomTab.Home) }
    var notes by remember { mutableStateOf(initialNotes) }
    var query by remember { mutableStateOf("") }

    val filtered = remember(query, notes) {
        if (query.isBlank()) notes
        else notes.filter { it.title.contains(query, true) || it.body.contains(query, true) }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // فعلاً فقط placeholder — می‌تونی اینجا به صفحه‌ی “New Note” ببری
                    onAddNote()
                    // برای تست Empty/Non-Empty می‌تونی این‌ها را کامنت/آن‌کامنت کنی:
                    // notes = emptyList()        // حالت خالی
                    // notes = sampleNotes         // حالت پر
                },
                containerColor = AppPurple,
                contentColor = Color.White,
                shape = CircleShape
            ) { Icon(Icons.Default.Add, contentDescription = "Add") }
        },
        bottomBar = {
            BottomBar(
                selected = selectedTab,
                onSelect = {
                    selectedTab = it
                    if (it == BottomTab.Settings) onOpenSettingsSystem()
                }
            )
        }
    ) { padding ->
        when (selectedTab) {
            BottomTab.Home -> HomeContent(
                modifier = Modifier.padding(padding),
                query = query,
                onQuery = { query = it },
                notes = filtered
            )
            BottomTab.Settings -> SettingsContent(modifier = Modifier.padding(padding))
        }
    }
}

private enum class BottomTab { Home, Settings }

@Composable
private fun BottomBar(
    selected: BottomTab,
    onSelect: (BottomTab) -> Unit
) {
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
            selected = selected == BottomTab.Settings,
            onClick = { onSelect(BottomTab.Settings) },
            icon = { Icon(Icons.Default.Settings, null) },
            label = { Text("Settings") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AppPurple,
                selectedTextColor = AppPurple,
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
    notes: List<NoteUi>
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp)
    ) {

        // Search
        OutlinedTextField(
            value = query,
            onValueChange = onQuery,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            placeholder = { Text("Search...") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(),
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
            // Empty state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 56.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // فعلاً بدون عکس — هر وقت خواستی painterResource(...) اضافه کن
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
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            // Grid of notes (2 ستونه)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(notes, key = { it.id }) { note ->
                    NoteCard(note)
                }
            }
        }
    }
}

@Composable
private fun NoteCard(note: NoteUi) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .padding(10.dp)
    ) {
        // رنگ پس‌زمینه‌ی ملایم داخل کارت
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(note.tint, RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Text(
                text = note.title,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E1E1E),
                lineHeight = 20.sp
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = note.body,
                color = Color(0xFF4C4C55),
                fontSize = 13.sp,
                lineHeight = 18.sp,
                maxLines = 6,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun SettingsContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Settings (placeholder)",
            color = AppPurple,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
