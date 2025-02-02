package doroteatomic.personal_tracker.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import doroteatomic.personal_tracker.Habit
import doroteatomic.personal_tracker.Mood
import doroteatomic.personal_tracker.fetchMoodsFromFirestore
import kotlinx.coroutines.launch

@Composable
fun MoodScreen(navController: NavController) {
    var moods by remember { mutableStateOf<List<Mood>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            fetchMoodsFromFirestore(
                onSuccess = { retrievedMoods ->
                    moods = retrievedMoods
                    isLoading = false
                },
                onFailure = { e ->
                    isLoading = false
                }
            )
        }
    }

    fun deleteMood(mood: Mood) {
    val db = FirebaseFirestore.getInstance() 

    db.collection("moods").document(mood.id).delete()
        .addOnSuccessListener {
            moods = moods.filterNot { it == mood }
        }
        .addOnFailureListener { exception ->
            Log.e("DeleteMood", "Greška prilikom brisanja: ", exception)
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
                    text = "moodbar",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                IconButton(onClick = { navController.navigate("addMoodScreen") }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Mood",
                        tint = Color.Black
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(moods) { mood ->
                    MoodCard(mood = mood, onDelete = {moodToDelete ->
                        deleteMood(moodToDelete)
                    })
                }
            }
        }
    }
}

@Composable
fun MoodCard(mood: Mood, onDelete: (Mood) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(color = Color(mood.color), shape = RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ){
            Text(
                text = mood.date,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            IconButton(
                onClick = { onDelete(mood) },
                modifier = Modifier
                    .size(24.dp)
                    .padding(top = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Mood",
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
                    text = "Moods: ${mood.emotions.size}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                mood.emotions.forEach { emotion ->
                    Text(
                        text = "- $emotion",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.DarkGray
                    )
                }

            }

        }

    }
}




