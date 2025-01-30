package doroteatomic.personal_tracker

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.firebase.firestore.PropertyName


data class Mood(
    @PropertyName("date") var date: String = "",
    @PropertyName("color") var color: Int = Color.Gray.toArgb(),
    var emotions: List<String> = emptyList()
)
