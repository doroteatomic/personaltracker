package doroteatomic.personal_tracker.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import doroteatomic.personal_tracker.Habit
import doroteatomic.personal_tracker.Mood

@Composable
fun AddMoodScreen(onSave: (Mood) -> Unit,navController: NavController) {
    var moodDate by remember { mutableStateOf("") }
    var moodColor by remember { mutableStateOf(Color.Gray) }
    var moodEmote by remember { mutableStateOf("") }
    var showColorPicker by remember { mutableStateOf(false) }

    val colors = listOf(
        Color(0xFFD7D5FF), Color(0xFFFFD7EB), Color(0xFFE4FFC8),
        Color(0xFFD6FFE1), Color(0xFFD7F9FF), Color(0xFFFFE0B2),
        Color(0xFFFFECA0), Color(0xFFE4D1FF)
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }

            Text(
                text = "Add a Mood",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            IconButton(onClick = { navController.navigate("homeScreen") }) {
                Icon(Icons.Default.Home, contentDescription = "Home")
            }
        }

        TextField(
            value = moodDate,
            onValueChange = { moodDate = it },
            placeholder = { Text("Add date") },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(horizontal = 16.dp),
            colors =  OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                cursorColor = Color.Black,
                focusedBorderColor = Color.Transparent
            ),
            textStyle = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )

        TextField(
            value = moodEmote,
            onValueChange = { moodEmote = it },
            placeholder = { Text("How did I feel...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(horizontal = 16.dp),
            colors =  OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                cursorColor = Color.Black
            ),
            textStyle = TextStyle(
                fontSize = 18.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        )

        Button(
            onClick = { showColorPicker = !showColorPicker },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Text(
                text = "Pick a color",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        if (showColorPicker) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                colors.forEach { color ->
                    Button(
                        onClick = { moodColor = color },
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = color)
                    ) {}
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                val emotes = moodEmote.split(",").map { it.trim() } // Split goals by commas
                val mood = Mood(
                    date = moodDate,
                    color = moodColor.toArgb(),
                    emotions = emotes
                )
                saveMood(mood)
                navController.popBackStack() // Navigate back after saving
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White)
        ) {
            Text(
                text = "Save Mood",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

fun saveMood(mood: Mood) {
    val db = FirebaseFirestore.getInstance()

    val moodId = mood.date.ifEmpty { System.currentTimeMillis().toString() }

    val moodToSave = mood.copy(date = moodId)

    db.collection("moods")
        .document(moodToSave.date)
        .set(moodToSave)
        .addOnSuccessListener {
            Log.d("Firestore", "Mood saved successfully!")
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Error saving mood", e)
        }
}

