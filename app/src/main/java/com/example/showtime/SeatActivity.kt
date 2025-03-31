package com.example.showtime

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.showtime.ui.theme.ShowTimeTheme

class SeatActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowTimeTheme {
                SeatSelectionScreen()
            }
        }
    }
}

@Composable
fun SeatSelectionScreen() {
    val rows = 5
    val columns = 6
    val selectedSeats = remember { mutableStateListOf<String>() }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Select Your Seats", fontSize = 24.sp, color = Color(0xFFD4AF37))
        Spacer(modifier = Modifier.height(20.dp))

        // Seat Grid
        for (i in 1..rows) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (j in 1..columns) {
                    val seatNumber = "${('A' + i - 1)}$j"
                    val isSelected = selectedSeats.contains(seatNumber)
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                if (isSelected) Color(0xFFD4AF37) else Color.LightGray,
                                shape = CircleShape
                            )
                            .border(1.dp, Color.DarkGray, CircleShape)
                            .clickable {
                                if (isSelected) selectedSeats.remove(seatNumber)
                                else selectedSeats.add(seatNumber)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(seatNumber, fontSize = 12.sp, color = Color.Black)
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (selectedSeats.isNotEmpty()) {
            Text(
                "Selected: ${selectedSeats.joinToString()}",
                fontSize = 16.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { showDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
            ) {
                Text("Book Tickets")
            }
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
                    Text("Seats: ${selectedSeats.joinToString()}", fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { showDialog = false }) {
                        Text("OK")
                    }
                }
            }
        }
    }
}
