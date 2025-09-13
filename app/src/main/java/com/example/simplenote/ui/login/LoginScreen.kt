package com.example.simplenote.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import com.example.simplenote.ui.theme.AppPurple

@Composable
fun LoginScreen(
    onLogin: (email: String, pass: String) -> Unit,
    onRegisterClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var pass  by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(Modifier.height(8.dp))

        Text(
            text = "Let’s Login",
            fontSize = 34.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1E1E1E),
        )
        Text(
            text = "And notes your idea",
            fontSize = 16.sp,
            color = Color(0xFF9A9AA0),
            modifier = Modifier.padding(top = 6.dp, bottom = 20.dp)
        )

        Label("Email Address")
        OutlinedTextField(
            value = email, onValueChange = { email = it },
            placeholder = { Text("Example: johndoe@gmail.com", color = Color(0xFFB9B9BF)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(12.dp),
            colors = fieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Label("Password")
        OutlinedTextField(
            value = pass, onValueChange = { pass = it },
            placeholder = { Text("********", color = Color(0xFFB9B9BF)) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(12.dp),
            colors = fieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(22.dp))

        Button(
            onClick = { onLogin(email, pass) /* فعلاً هیچی انجام نده */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(100),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppPurple,
                contentColor = Color.White
            )
        ) {
            Text("Login", fontSize = 18.sp)
            Spacer(Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
        }

        // --- OR Divider
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 18.dp)
                .fillMaxWidth()
        ) {
            Divider(Modifier.weight(1f), color = Color(0xFFE3E3EA))
            Text("  Or  ", color = Color(0xFF9A9AA0))
            Divider(Modifier.weight(1f), color = Color(0xFFE3E3EA))
        }

        TextButton(
            onClick = onRegisterClick,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Don’t have any account? Register here",
                color = AppPurple
            )
        }
    }
}

@Composable
private fun Label(text: String) {
    Text(text, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E1E1E))
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color(0xFFE3E3EA),
    unfocusedBorderColor = Color(0xFFE3E3EA),
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    cursorColor = AppPurple
)
