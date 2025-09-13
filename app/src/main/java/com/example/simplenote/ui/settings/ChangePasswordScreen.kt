package com.example.simplenote.ui.settings

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simplenote.ui.theme.AppPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    viewModel: SettingsViewModel,
    onNavigateBack: () -> Unit,
    onSubmitSuccess: () -> Unit
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var retypePassword by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState) {
        if (uiState.passwordChangeSuccess) {
            Toast.makeText(context, "Password changed successfully!", Toast.LENGTH_SHORT).show()
            onSubmitSuccess()
        }
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Change Password") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    if (newPassword.isNotBlank() && newPassword == retypePassword) {
                        viewModel.changePassword(currentPassword, newPassword)
                    } else {
                        Toast.makeText(context, "New passwords do not match or are empty.", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(100),
                colors = ButtonDefaults.buttonColors(containerColor = AppPurple, contentColor = Color.White)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("Submit New Password", fontSize = 16.sp)
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Please input your current password first", color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            PasswordTextField(
                label = "Current Password",
                value = currentPassword,
                onValueChange = { currentPassword = it }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text("Now, create your new password", color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            PasswordTextField(
                label = "New Password",
                value = newPassword,
                onValueChange = { newPassword = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
            PasswordTextField(
                label = "Retype New Password",
                value = retypePassword,
                onValueChange = { retypePassword = it }
            )
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun PasswordTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    Text(label, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    Spacer(Modifier.height(8.dp))
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("********", color = Color.LightGray) },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFE3E3EA),
            unfocusedBorderColor = Color(0xFFE3E3EA),
            cursorColor = AppPurple
        ),
        modifier = Modifier.fillMaxWidth()
    )
}