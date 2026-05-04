package com.example.mythbusters

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class Hack(val statement: String, val isReal: Boolean, val explanation: String, fontSize: TextUnit)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MYTHBUSTERSApp()
        }
    }
}

@Composable
fun MYTHBUSTERSApp() {
    val hacks = remember {
        listOf(
            Hack("Putting a phone in rice fixes water damage.", false, "Rice doesn't help and can leave dust inside.",fontSize = 28.sp),
            Hack("White vinegar can remove hard water stains.", true, "The acid breaks down calcium deposits safely.",fontSize = 28.sp),
            Hack("Using a laser pointer can blind a pilot.", true, "It is extremely dangerous and illegal.",fontSize = 28.sp)
        )
    }

    val screen = remember { mutableStateOf("WELCOME") }
    val currentIndex = remember { mutableStateOf(0) }
    val score = remember { mutableStateOf(0) }
    val feedback = remember { mutableStateOf("") }
    val hasAnswered = remember { mutableStateOf(false) }
    val bgColor = remember { mutableStateOf(Color.White) }

    fun startQuiz() {
        screen.value = "QUIZ"
        currentIndex.value = 0
        score.value = 0
        feedback.value = ""
        hasAnswered.value = false
        bgColor.value = Color.White
    }

    fun checkAnswer(userChoice: Boolean) {
        if (hasAnswered.value == false) {
            val currentHack = hacks[currentIndex.value]
            if (userChoice == currentHack.isReal) {
                score.value = score.value + 1
                feedback.value = "Correct! " + currentHack.explanation
                bgColor.value = Color.Green
            } else {
                feedback.value = "Wrong! " + currentHack.explanation
                bgColor.value = Color.Red
            }
            hasAnswered.value = true
        }
    }

    fun nextQuestion() {
        if (currentIndex.value < hacks.size - 1) {
            currentIndex.value = currentIndex.value + 1
            feedback.value = ""
            hasAnswered.value = false
            bgColor.value = Color.White
        } else {
            screen.value = "SCORE"
            bgColor.value = Color.White
        }
    }

    Column (modifier = Modifier.fillMaxSize().background(bgColor.value).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

        if (screen.value == "WELCOME") {
            Text("MYTHBUSTERS", style = MaterialTheme.typography.headlineLarge, fontSize = 36.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Test your common sense against viral internet rumors.", fontSize = 35.sp)
            Text("Answer the following questions to test your knowledge.", fontSize = 35.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { startQuiz() }) {
                Text("Start", fontSize = 28.sp)
            }
        }

        if (screen.value == "QUIZ") {

            val currentHack = hacks[currentIndex.value]
            Text("Question " + (currentIndex.value + 1), fontSize = 28.sp)
            Text(currentHack.statement, fontSize = 28.sp)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { checkAnswer(true) }) {
                    Text("True ", fontSize = 25.sp)
                }
                Button(onClick = { checkAnswer(false) }) {
                    Text("False", fontSize = 25.sp)
                }
            }

            Text(feedback.value, fontSize = 28.sp)

            if (hasAnswered.value) {
                Button(onClick = { nextQuestion() }) {
                    Text("Next", fontSize = 28.sp)
                }
            }
        }

        if (screen.value == "SCORE") {
            Text("Quiz Finished!", fontSize = 28.sp)
            Text("Total Score: " + score.value + " / " + hacks.size, fontSize = 28.sp)

            if (score.value >= 2) {
                Text("Master Hacker!",fontSize = 25.sp)
            } else {
                Text("Stay Safe Online!", fontSize = 25.sp)
            }

            Button(onClick = { screen.value = "REVIEW" }) {
                Text("Review Answers", fontSize = 25.sp)
            }
        }

        if (screen.value == "REVIEW") {
            Text("Review Mode", fontSize = 23.sp)
            for (h in hacks) {
                Column {
                    Text("Statement: " + h.statement, fontSize = 20.sp)
                    Text("Answer: " + h.isReal, fontSize = 20.sp)
                    Text("Reason: " + h.explanation, fontSize = 20.sp)
                    Text("---", fontSize = 20.sp)
                }
            }
            Button(onClick = {
                screen.value = "WELCOME"
                bgColor.value = Color.Blue
            }) {
                Text("Back to Home", fontSize = 20.sp)
            }
        }
    }
}
