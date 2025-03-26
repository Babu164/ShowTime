package com.example.showtime

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.showtime.ui.theme.ShowTimeTheme
import androidx.compose.foundation.layout.FlowRow


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
    val title: String,
    val posterResId: Int,
    val showTimes: List<String>
)

@Composable
fun BookingScreen() {
    val context = LocalContext.current
    val movies = remember {
        listOf(
            Movie("RRR", R.drawable.rrr, listOf("Screen 2 - 10:00 AM", "Screen 1 - 1:00 PM", "Screen 3 - 5:30 PM")),
            Movie("Pushpa 2", R.drawable.pushpa2, listOf("Screen 3 - 11:30 AM", "Screen 2 - 2:30 PM", "Screen 1 - 7:30 PM")),
            Movie("Salaar", R.drawable.salaar, listOf("Screen 1 - 10:45 AM", "Screen 2 - 3:15 PM", "Screen 3 - 8:00 PM")),
            Movie("Barbie", R.drawable.barbie, listOf("Screen 1 - 10:00 AM", "Screen 2 - 1:00 PM", "Screen 3 - 5:00 PM")),
            Movie("Dune Part 2", R.drawable.dune2, listOf("Screen 2 - 9:00 AM", "Screen 1 - 12:00 PM", "Screen 3 - 4:00 PM"))
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text("Now Showing", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37))

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 80.dp) // gives breathing space
        ) {
            items(movies) { movie ->
                MovieCard(movie)
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
                painter = painterResource(id = movie.posterResId),
                contentDescription = "${movie.title} Poster",
                modifier = Modifier
                    .fillMaxWidth()                    // take full card width
                    .height(220.dp)                   // increase height for better view
                    .clip(RoundedCornerShape(8.dp)),  // subtle rounded corners
                contentScale = ContentScale.Crop      // crop nicely to fill space
            )


            Spacer(Modifier.height(10.dp))

            Text(
                text = movie.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )

            Spacer(Modifier.height(10.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                movie.showTimes.forEach { time ->
                    AssistChip(
                        onClick = {
                            Toast.makeText(context, "Selected $time for ${movie.title}", Toast.LENGTH_SHORT).show()
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
