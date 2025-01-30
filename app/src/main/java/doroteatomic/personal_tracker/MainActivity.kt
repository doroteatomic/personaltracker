package doroteatomic.personal_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import doroteatomic.personal_tracker.screens.AddHabitScreen
import doroteatomic.personal_tracker.screens.HabitsScreen
import doroteatomic.personal_tracker.screens.HomeScreen
import doroteatomic.personal_tracker.screens.MoodScreen
import doroteatomic.personal_tracker.screens.AddMoodScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "homeScreen") {
        composable("homeScreen") {
            HomeScreen(navController = navController)
        }
        composable("habitsScreen") {
            HabitsScreen(navController = navController)
        }
        composable("addHabitScreen") {
            AddHabitScreen(
                navController = navController,
                onSave = { habit, goals ->
                    println("New Habit Saved: ${habit.name}")
                    println("Goals: $goals")
                    navController.popBackStack()
                }
            )
        }



        composable("moodScreen") {
            MoodScreen(navController = navController)
        }
        composable("addMoodScreen") {
            AddMoodScreen(
                navController = navController,
                onSave = { mood ->
                    println("New Mood Saved: ${mood.date}")
                    navController.navigate("moodScreen")

                }
            )
        }

    }
}

