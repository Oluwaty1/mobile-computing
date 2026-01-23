package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.border
import androidx.compose.material3.MaterialTheme
import android.content.res.Configuration
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier=Modifier.fillMaxSize()) { innerpadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerpadding)
                    ) {
                        // FIX 2: Call the Conversation composable here
                        Conversation(messages = SampleData.conversationSample)
                    }
                }
            }
        }
    }
}

data class Message(val author: String, val body: String)

@Composable
fun MessageCard(msg: Message, modifier: Modifier = Modifier) {
    Row(modifier = modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(R.drawable.profile_picture),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        var isExpanded by remember { mutableStateOf(false) }

        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Text(
                text = msg.author,
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


@Composable
fun Conversation(messages: List<Message>) {
    LazyColumn {
        items(messages) { message ->
            MessageCard(msg = message)
        }
    }
}

object SampleData {
    // Sample conversation data
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

@Preview
@Composable
fun PreviewConversation() {
    MyApplicationTheme {
        Conversation(SampleData.conversationSample)
    }
}
