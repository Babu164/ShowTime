package com.example.showtime

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.showtime.ui.theme.ShowTimeTheme
import kotlin.jvm.java

class BookingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowTimeTheme {
                BookingScreen()
            }
        }
    }
}

data class Movie(
    val title: String = "",
    val posterUrl: String = "",
    val showTimes: List<String> = emptyList()
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BookingScreen() {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }

    val movies = listOf(
        Movie("RRR", "rrr", listOf("Screen 2 - 10:00 AM", "Screen 1 - 1:00 PM", "Screen 3 - 5:30 PM")),
        Movie("Pushpa 2", "pushpa2", listOf("Screen 3 - 11:30 AM", "Screen 2 - 2:30 PM", "Screen 1 - 7:30 PM")),
        Movie("Salaar", "salaar", listOf("Screen 1 - 10:45 AM", "Screen 2 - 3:15 PM", "Screen 3 - 8:00 PM")),
        Movie("Barbie", "barbie", listOf("Screen 1 - 10:00 AM", "Screen 2 - 1:00 PM", "Screen 3 - 5:00 PM")),
        Movie("Dune Part 2", "dune2", listOf("Screen 2 - 9:00 AM", "Screen 1 - 12:00 PM", "Screen 3 - 4:00 PM"))
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Now Showing", fontSize = 24.sp, color = Color(0xFFD4AF37)) },
                actions = {
                    IconButton(
                        onClick = {
                            val intent = Intent(context, ProfileActivity::class.java)
                            context.startActivity(intent)
                        }
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = Color(0xFFD4AF37)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        val intent = Intent(context, SeatActivity::class.java)
                        context.startActivity(intent)
                    },
                    icon = { Icon(Icons.Default.EventSeat, contentDescription = "Seat") },
                    label = { Text("Seat") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = {
                        selectedTab = 2
                        Toast.makeText(context, "Payment page coming soon", Toast.LENGTH_SHORT).show()
                    },
                    icon = { Icon(Icons.Default.Payment, contentDescription = "Payment") },
                    label = { Text("Payment") }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(movies) { movie ->
                    MovieCard(movie)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MovieCard(movie: Movie) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Image(
                painter = painterResource(
                    id = context.resources.getIdentifier(movie.posterUrl, "drawable", context.packageName)
                ),
                contentDescription = "${movie.title} Poster",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = movie.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(10.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                movie.showTimes.forEach { time ->
                    AssistChip(
                        onClick = {
                            val intent = Intent(context, SeatActivity::class.java).apply {
                                putExtra("movieTitle", movie.title)
                                putExtra("showTime", time)
                            }
                            context.startActivity(intent)
                        },
                        label = { Text(text = time) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookingScreenPreview() {
    ShowTimeTheme {
        BookingScreen()
    }
}
