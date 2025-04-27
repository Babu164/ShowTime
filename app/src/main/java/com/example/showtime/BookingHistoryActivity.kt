package com.example.showtime

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.showtime.ui.theme.ShowTimeTheme
import com.google.firebase.firestore.FirebaseFirestore

data class Booking(
    val movieTitle: String = "",
    val showTime: String = "",
    val seats: List<String> = emptyList(),
    val totalPrice: Int = 0
)

class BookingHistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowTimeTheme {
                BookingHistoryScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingHistoryScreen() {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    var bookings by remember { mutableStateOf(listOf<Booking>()) }

    // Fetch booking data from Firestore
    LaunchedEffect(Unit) {
        db.collection("bookings")
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { result ->
                bookings = result.documents.mapNotNull { doc ->
                    doc.toObject(Booking::class.java)
                }
            }
    }

    Scaffold(
        containerColor = Color(0xFFFFF8E1),
        topBar = {
            TopAppBar(
                title = { Text("Booking History", color = Color(0xFFD4AF37)) },
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
    ) { padding ->
        if (bookings.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No Bookings Yet", color = Color.Gray, fontSize = 18.sp)
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .padding(padding)
                    .background(Color.White)
            ) {
                items(bookings) { booking ->
                    BookingCard(booking)
                }
            }
        }
    }
}

@Composable
fun BookingCard(booking: Booking) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Movie: ${booking.movieTitle}", fontSize = 18.sp, color = Color.Black)
            Text("Showtime: ${booking.showTime}", fontSize = 16.sp, color = Color.DarkGray)
            Text("Seats: ${booking.seats.joinToString()}", fontSize = 16.sp, color = Color.DarkGray)
            Text("Total Paid: ₹${booking.totalPrice}", fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, color = Color.Black)
        }
    }
}
