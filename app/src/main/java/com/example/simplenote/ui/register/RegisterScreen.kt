package com.example.simplenote.ui.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.simplenote.ui.theme.AppPurple
import androidx.compose.foundation.text.KeyboardOptions

@Composable
fun RegisterScreen(
    onRegister: (first: String, last: String, user: String, email: String, pass: String) -> Unit,
    onBackToLogin: () -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName  by remember { mutableStateOf("") }
    var username  by remember { mutableStateOf("") }
    var email     by remember { mutableStateOf("") }
    var password  by remember { mutableStateOf("") }
    var confirm   by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { onBackToLogin() }
                .padding(top = 8.dp, bottom = 16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = AppPurple
            )
            Spacer(Modifier.width(6.dp))
            Text("Back to Login", color = AppPurple, fontSize = 16.sp)
        }

        Text("Register", fontSize = 34.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1E1E1E))
        Text("And start taking notes", fontSize = 16.sp, color = Color(0xFF9A9AA0), modifier = Modifier.padding(top = 6.dp, bottom = 20.dp))

        Label("First Name")
        OutlinedTextField(firstName, { firstName = it }, placeholder = { Text("Example: Taha", color = Color(0xFFB9B9BF)) },
            singleLine = true, shape = RoundedCornerShape(12.dp), colors = fieldColors(), modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(16.dp))

        Label("Last Name")
        OutlinedTextField(lastName, { lastName = it }, placeholder = { Text("Example: Hamifar", color = Color(0xFFB9B9BF)) },
            singleLine = true, shape = RoundedCornerShape(12.dp), colors = fieldColors(), modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(16.dp))

        Label("Username")
        OutlinedTextField(username, { username = it }, placeholder = { Text("Example: @HamifarTaha", color = Color(0xFFB9B9BF)) },
            singleLine = true, shape = RoundedCornerShape(12.dp), colors = fieldColors(), modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(16.dp))

        Label("Email Address")
        OutlinedTextField(
            value = email, onValueChange = { email = it },
            placeholder = { Text("Example: hamifar.taha@gmail.com", color = Color(0xFFB9B9BF)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(12.dp),
            colors = fieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Label("Password")
        OutlinedTextField(
            value = password, onValueChange = { password = it },
            placeholder = { Text("*********", color = Color(0xFFB9B9BF)) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(12.dp),
            colors = fieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Label("Retype Password")
        OutlinedTextField(
            value = confirm, onValueChange = { confirm = it },
            placeholder = { Text("*********", color = Color(0xFFB9B9BF)) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            shape = RoundedCornerShape(12.dp),
            colors = fieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(22.dp))

        Button(
            onClick = { onRegister(firstName, lastName, username, email, password) /* placeholder */ },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(100),
            colors = ButtonDefaults.buttonColors(containerColor = AppPurple, contentColor = Color.White)
        ) {
            Text("Register", fontSize = 18.sp)
            Spacer(Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
        }

        Spacer(Modifier.height(18.dp))
    }
}

@Composable private fun Label(text: String) {
    Text(text, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E1E1E))
    Spacer(Modifier.height(8.dp))
}
@Composable private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color(0xFFE3E3EA),
    unfocusedBorderColor = Color(0xFFE3E3EA),
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    cursorColor = AppPurple
)
