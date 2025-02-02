package doroteatomic.personal_tracker.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import doroteatomic.personal_tracker.Habit



@Composable
fun AddHabitScreen(onSave: (Habit, List<String>) -> Unit, navController: NavController) {
    var habitName by remember { mutableStateOf("") }
    var habitColor by remember { mutableStateOf(Color(0xFFD7D5FF)) }
    var goalsText by remember { mutableStateOf("") }
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
                text = "Add a Habit",
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
            value = habitName,
            onValueChange = { habitName = it },
            placeholder = { Text("Add title") },
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
            value = goalsText,
            onValueChange = { goalsText = it },
            placeholder = { Text("Set goals") },
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
                        onClick = { habitColor = color },
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
                val goalsList = goalsText.split(",").map { it.trim() } // Split goals by commas
                val habit = Habit(
                    name = habitName,
                    color = habitColor.toArgb(),
                    goals = goalsList
                )
                SaveHabit(habit)
                navController.popBackStack() // Navigate back after saving
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White)
        ) {
            Text(
                text = "Save Habit",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

fun SaveHabit(habit: Habit) {
    val db = FirebaseFirestore.getInstance()


    if (habit.name.isEmpty()) {
        db.collection("habits")
            .add(habit)
            .addOnSuccessListener {
                Log.d("Firestore", "Habit saved successfully!")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding habit", e)
            }
    } else {
        db.collection("habits")
            .document(habit.name)
            .set(habit)
            .addOnSuccessListener {
                Log.d("Firestore", "Habit updated successfully!")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error updating habit", e)
            }
    }
}

