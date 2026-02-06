package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()

                val viewModel = remember { ProfileViewModel(applicationContext) }

                NavHost(navController = navController, startDestination = "list_screen") {
                    composable("list_screen") {
                        ListScreen(navController, viewModel)
                    }
                    composable("add_screen") {
                        AddNameScreen(
                            onBack = { navController.popBackStack() },
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ListScreen(navController: NavController, viewModel: ProfileViewModel) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add_screen") }) {
                Icon(Icons.Default.Add, contentDescription = "Add Item")
            }
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            Conversation(
                messages = SampleData.conversationSample,
                currentName = viewModel.userName,
                currentImagePath = viewModel.profileImagePath
            )
        }
    }
}


@Composable
fun AddNameScreen(onBack: () -> Unit, viewModel: ProfileViewModel) {
    // Photo Picker Launcher
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                viewModel.updateImage(uri)
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
                .clickable {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            if (viewModel.profileImagePath != null) {
                AsyncImage(
                    model = File(viewModel.profileImagePath!!),
                    contentDescription = "Selected Profile",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(Icons.Default.Person, contentDescription = "Empty", tint = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.userName,
            onValueChange = { newName ->
                viewModel.updateName(newName)
            },
            label = { Text("Author Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack) { Text("Back to List") }
    }
}


data class Message(val author: String, val body: String)

@Composable
fun Conversation(messages: List<Message>, currentName: String, currentImagePath: String?) {
    LazyColumn {
        items(messages) { message ->
            MessageCard(msg = message, displayedName = currentName, imagePath = currentImagePath)
        }
    }
}

@Composable
fun MessageCard(msg: Message, displayedName: String, imagePath: String?, modifier: Modifier = Modifier) {
    Row(modifier = modifier.padding(all = 8.dp)) {

        if (imagePath != null) {
            AsyncImage(
                model = File(imagePath),
                contentDescription = "Profile",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        var isExpanded by remember { mutableStateOf(false) }

        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Text(
                text = displayedName, // Use the dynamic name from ViewModel
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


object SampleData {
    val conversationSample = listOf(
        Message(
            "Michael",
            "Hello there..."
        ),
        Message(
            "Michael",
            """List of subjects I'm taking:
Mathematics
English
Finnish
Biology
Chemistry
Physics
History
Geography
Computer Science""".trimMargin()
        ),
        Message(
            "Michael",
            """I think Mathematics is my favorite subject.
It's so much fun!""".trimMargin()
        ),
        Message(
            "Michael",
            "I want to know which subjects you're taking"
        ),
        Message(
            "Michael",
            """Hey,I really think you should take Maths, it's great!
You are smart so i believe you would get good grades.
We can always study together if it gets difficult.
Two heads are better than one :)""".trimMargin()
        ),
        Message(
            "Michael",
            "The deadline is very close :)"
        ),
        Message(
            "Michael",
            "I really do not want you to miss out on anything"
        ),
        Message(
            "Michael",
            "I have a good feeling about the new semester"
        ),
        Message(
            "Michael",
            "Think about how much fun we could have after this semester"
        ),
        Message(
            "Michael",
            "You already know what is coming this summer"
        ),
        Message(
            "Michael",
            "I can't contain my excitement but I am also quite worried about what is ahead of us"
        ),
        Message(
            "Michael",
            "I do not think we should handout this weekend since we need to prepare for the new semester"
        ),
        Message(
            "Michael",
            "To make up for it, we could have lunch together during lunchtime on Monday"
        ),
        Message(
            "Michael",
            "I will see you on Monday at the bus stop. Have a great weekend!"
        ),
    )
}