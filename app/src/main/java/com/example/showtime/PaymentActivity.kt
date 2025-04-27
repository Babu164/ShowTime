package com.example.showtime

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.showtime.ui.theme.ShowTimeTheme
import com.google.firebase.firestore.FirebaseFirestore

class PaymentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val movieTitle = intent.getStringExtra("movieTitle") ?: "Movie"
        val showTime = intent.getStringExtra("showTime") ?: "Showtime"
        val selectedSeats = intent.getStringArrayListExtra("selectedSeats") ?: arrayListOf()

        setContent {
            ShowTimeTheme {
                PaymentScreen(movieTitle, showTime, selectedSeats)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(movieTitle: String, showTime: String, selectedSeats: List<String>) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val pricePerSeat = 200
    val totalPrice = selectedSeats.size * pricePerSeat
    var showDialog by remember { mutableStateOf(false) }

    var cardNumber by remember { mutableStateOf("") }
    var cardHolder by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    val isCardNumberValid = cardNumber.length == 16 && cardNumber.all { it.isDigit() }
    val isCardHolderValid = cardHolder.length >= 3 && cardHolder.all { it.isLetter() || it.isWhitespace() }
    val isExpiryValid = expiryDate.matches(Regex("^(0[1-9]|1[0-2])/\\d{2}$"))
    val isCvvValid = cvv.length == 3 && cvv.all { it.isDigit() }
    val isFormValid = isCardNumberValid && isCardHolderValid && isExpiryValid && isCvvValid

    Scaffold(
        containerColor = Color(0xFFFFF8E1),
        topBar = {
            TopAppBar(
                title = { Text("Payment", color = Color(0xFFD4AF37), fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = { backDispatcher?.onBackPressed() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFFD4AF37))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Movie: $movieTitle", fontSize = 16.sp)
            Text("Showtime: $showTime", fontSize = 16.sp)
            Text("Seats: ${selectedSeats.joinToString()}", fontSize = 16.sp)
            Text("Total: ₹$totalPrice", fontSize = 18.sp, color = Color.Black)

            Divider(thickness = 1.dp)

            // Payment Fields (same)
            OutlinedTextField(
                value = cardHolder,
                onValueChange = { cardHolder = it },
                label = { Text("Card Holder Name") },
                isError = cardHolder.isNotEmpty() && !isCardHolderValid,
                modifier = Modifier.fillMaxWidth()
            )
            if (cardHolder.isNotEmpty() && !isCardHolderValid) {
                Text("Name should be at least 3 letters", color = Color.Red, fontSize = 12.sp)
            }

            OutlinedTextField(
                value = cardNumber,
                onValueChange = { cardNumber = it.take(16) },
                label = { Text("Card Number") },
                isError = cardNumber.isNotEmpty() && !isCardNumberValid,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            if (cardNumber.isNotEmpty() && !isCardNumberValid) {
                Text("Card number must be 16 digits", color = Color.Red, fontSize = 12.sp)
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = expiryDate,
                    onValueChange = { expiryDate = it.take(5) },
                    label = { Text("Expiry MM/YY") },
                    isError = expiryDate.isNotEmpty() && !isExpiryValid,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                OutlinedTextField(
                    value = cvv,
                    onValueChange = { cvv = it.take(3) },
                    label = { Text("CVV") },
                    isError = cvv.isNotEmpty() && !isCvvValid,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (isFormValid) {
                        // Save booking to Firebase
                        val bookingData = hashMapOf(
                            "movieTitle" to movieTitle,
                            "showTime" to showTime,
                            "seats" to selectedSeats,
                            "totalPrice" to totalPrice,
                            "timestamp" to System.currentTimeMillis()
                        )
                        db.collection("bookings")
                            .add(bookingData)
                            .addOnSuccessListener {
                                showDialog = true
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Failed to save booking", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(context, "Please fix form errors", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37)),
                modifier = Modifier.fillMaxWidth(),
                enabled = isFormValid
            ) {
                Text("Pay ₹$totalPrice", fontSize = 16.sp)
            }
        }

        if (showDialog) {
            Dialog(onDismissRequest = { showDialog = false }) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = Color.White,
                    tonalElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Booking Confirmed!", fontSize = 20.sp, color = Color.Black)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Your ticket is confirmed!", fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                showDialog = false
                                val intent = Intent(context, BookingHistoryActivity::class.java)
                                context.startActivity(intent)
                                if (context is ComponentActivity) {
                                    context.finish()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
                        ) {
                            Text("View Bookings")
                        }
                    }
                }
            }
        }
    }
}
