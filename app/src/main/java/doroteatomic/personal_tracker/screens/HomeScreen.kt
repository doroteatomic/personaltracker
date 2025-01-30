package doroteatomic.personal_tracker.screens

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController


@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Pink Card for the Title
        Box(
            modifier = Modifier
                .width(338.dp)  // Set width to 338dp
                .height(647.dp)  // Set height to 647dp
                .clip(RoundedCornerShape(41.dp))  // Rounded corners with radius of 41
                .background(Color(0xFFFFD6E8))  // Pink color for the background
        ) {
            Text(
                text = "LET'S\nBUILD\nSOME\nHABITS",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                color = Color.Black,
                textAlign = TextAlign.Start,  // Align text to the start (left)
                modifier = Modifier
                    .align(Alignment.TopStart)  // Align the text to the top-left corner
                    .padding(start = 20.dp, top = 20.dp)  // Add some padding from the ed
            )
        }


        // Start Button
        Button(
            onClick = { navController.navigate("habitsScreen") },
            colors = ButtonDefaults.buttonColors(
                Color(0xFFFFF6CC), contentColor = Color.Black
            ),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .width(347.dp)
                .height(122.dp)
        ) {
            Text(
                text = "START",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic
            )
        }
    }
}



