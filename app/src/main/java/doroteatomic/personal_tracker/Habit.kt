package doroteatomic.personal_tracker

import com.google.firebase.firestore.PropertyName

data class Habit(
    var id: String = "",

    @PropertyName("habit_name")
    var name: String = "",
    var color: Int = 0,
    var goals: List<String> = emptyList()
)
