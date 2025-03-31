package com.example.showtime

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.showtime.ui.theme.ShowTimeTheme

class SeatActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val movieTitle = intent.getStringExtra("movieTitle") ?: "Movie"
        val showTime = intent.getStringExtra("showTime") ?: "Showtime"

        setContent {
            ShowTimeTheme {
                SeatSelectionScreen(movieTitle, showTime)
            }
        }
    }
}

@Composable
fun SeatSelectionScreen(movieTitle: String, showTime: String) {
    val context = LocalContext.current
    val rows = 5
    val columns = 6
    val selectedSeats = remember { mutableStateListOf<String>() }

    Scaffold(
        containerColor = Color(0xFFFFF8E1)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 32.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$movieTitle - $showTime",
                fontSize = 20.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Select Your Seats",
                fontSize = 24.sp,
                color = Color(0xFFD4AF37)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Seat grid
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
                                .size(42.dp)
                                .background(
                                    if (isSelected) Color(0xFFD4AF37) else Color.LightGray,
                                    shape = CircleShape
                                )
                                .border(1.dp, Color.DarkGray, CircleShape)
                                .clickable {
                                    if (isSelected) {
                                        selectedSeats.remove(seatNumber)
                                    } else {
                                        if (selectedSeats.size < 3) {
                                            selectedSeats.add(seatNumber)
                                        } else {
                                            Toast
                                                .makeText(context, "Max 3 seats allowed!", Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = seatNumber, fontSize = 12.sp, color = Color.Black)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (selectedSeats.isNotEmpty()) {
                Text(
                    "Selected Seats: ${selectedSeats.joinToString()}",
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val intent = Intent(context, PaymentActivity::class.java)
                        intent.putExtra("movieTitle", movieTitle)
                        intent.putExtra("showTime", showTime)
                        intent.putStringArrayListExtra("selectedSeats", ArrayList(selectedSeats))
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
                ) {
                    Text("Book Tickets")
                }
            }
        }
    }
}
