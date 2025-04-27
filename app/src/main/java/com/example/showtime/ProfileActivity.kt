package com.example.showtime

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.showtime.ui.theme.ShowTimeTheme
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowTimeTheme {
                ProfileScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var showPrivacyDialog by remember { mutableStateOf(false) }

    // Load user profile data when the screen opens
    LaunchedEffect(Unit) {
        db.collection("users").document("profile")
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    name = document.getString("name") ?: ""
                    email = document.getString("email") ?: ""
                    address = document.getString("address") ?: ""
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to load profile", Toast.LENGTH_SHORT).show()
            }
    }

    Scaffold(
        containerColor = Color(0xFFFFF8E1),
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile", color = Color(0xFFD4AF37)) },
                navigationIcon = {
                    IconButton(onClick = {
                        val intent = Intent(context, BookingActivity::class.java)
                        context.startActivity(intent)
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFFD4AF37))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Address") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val profileData = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "address" to address
                    )
                    db.collection("users").document("profile")
                        .set(profileData)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Profile Saved!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Failed to save profile", Toast.LENGTH_SHORT).show()
                        }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save", fontSize = 16.sp)
            }

            // Privacy Policy Button
            Button(
                onClick = { showPrivacyDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Privacy Policy")
            }

            if (showPrivacyDialog) {
                AlertDialog(
                    onDismissRequest = { showPrivacyDialog = false },
                    title = { Text("Privacy Policy", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                    text = {
                        Text(
                            text = "Your privacy is our top priority. Your name, email, and address are securely stored. " +
                                    "We strictly adhere to industry standards to safeguard your data and never share your details without consent.",
                            fontSize = 16.sp
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = { showPrivacyDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
                        ) {
                            Text("OK")
                        }
                    },
                    containerColor = Color.White
                )
            }

            // Sign Out Button
            Button(
                onClick = {
                    val intent = Intent(context, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                    Toast.makeText(context, "Signed Out Successfully", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Out", fontSize = 16.sp)
            }
        }
    }
}
