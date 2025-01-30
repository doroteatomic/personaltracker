package doroteatomic.personal_tracker


import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore



fun fetchHabitsFromFirestore(
    onSuccess: (List<Habit>) -> Unit,
    onFailure: (Exception) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    db.collection("habits")
        .get()
        .addOnSuccessListener { result ->
            val habitsList = result.mapNotNull { document ->
                document.toObject(Habit::class.java)
            }
            onSuccess(habitsList)
        }
        .addOnFailureListener { exception ->
            onFailure(exception)
        }
}

fun fetchMoodsFromFirestore(onSuccess: (List<Mood>) -> Unit, onFailure: (Exception) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("moods").get()
        .addOnSuccessListener { result ->
            val moods = result.documents.mapNotNull { it.toObject(Mood::class.java) }
            onSuccess(moods)
        }
        .addOnFailureListener { exception ->
            Log.w("Firestore", "Error getting moods", exception)
            onFailure(exception)
        }
}




