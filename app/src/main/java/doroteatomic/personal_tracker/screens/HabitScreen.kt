package doroteatomic.personal_tracker.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import doroteatomic.personal_tracker.Habit
import doroteatomic.personal_tracker.fetchHabitsFromFirestore
import kotlinx.coroutines.launch

@Composable
fun HabitsScreen(navController: NavController) {
    var habits by remember { mutableStateOf<List<Habit>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var sortAscending by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            fetchHabitsFromFirestore(
                onSuccess = { retrievedHabits ->
                    habits = retrievedHabits
                    isLoading = false
                },
                onFailure = { e ->
                    isLoading = false
                }
            )
        }
    }

   fun deleteHabit(habit: Habit) {
        val db = FirebaseFirestore.getInstance()

        db.collection("habits").document(habit.name).delete()
            .addOnSuccessListener {
                habits = habits.filterNot { it == habit }
            }
            .addOnFailureListener { exception ->
                Log.e("DeleteHabit", "Greška prilikom brisanja: ", exception)
            }
    }

    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }

                Text(
                    text = "daily habits",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                IconButton(onClick = { navController.navigate("addHabitScreen") }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Habit",
                        tint = Color.Black
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { sortAscending = !sortAscending }) {
                    Text(
                        text = if (sortAscending) "Sort: Low → High" else "Sort: High → Low",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                    Icon(
                        imageVector = if (sortAscending) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropDown,
                        contentDescription = "Sort Order",
                        tint = Color.Gray
                    )
                }
            }

            val sortedHabits =
                habits.sortedBy { if (sortAscending) it.goals.size else -it.goals.size }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(sortedHabits) { habit ->
                    HabitCard(habit = habit, onDelete = { habitToDelete ->
                        deleteHabit(habitToDelete)
                    })
                }
            }

            Spacer(modifier = Modifier.weight(0.1f))

            Button(
                onClick = {
                    navController.navigate("moodScreen")
                },
                colors = ButtonDefaults.buttonColors(
                    Color(0xFFFFF6CC), contentColor = Color.Black
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(122.dp)
            ) {
                Text(
                    text = "MOODBAR",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}


@Composable
fun HabitCard(habit: Habit, onDelete: (Habit) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(color = Color(habit.color), shape = RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = habit.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            IconButton(
                onClick = { onDelete(habit) },
                modifier = Modifier
                    .size(24.dp)
                    .padding(top = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Habit",
                    tint = Color.DarkGray
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Top
        ) {
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "Goals: ${habit.goals.size}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                habit.goals.forEach { goal ->
                    Text(
                        text = "- $goal",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}


