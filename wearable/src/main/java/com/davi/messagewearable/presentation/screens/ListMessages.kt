package com.davi.messagewearable.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.davi.messagewearable.domain.model.MessageApp
import com.davi.messagewearable.presentation.theme.containerTextField
import com.davi.messagewearable.presentation.theme.orange

@Composable
fun ListMessages(messages: List<MessageApp>, listState: LazyListState) {

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(horizontal = 40.dp)
    ) {
        item {
            Text(text = "Olá mundo!", color = Color.White, fontSize = 10.sp)
        }
        items(messages.size) {
            val message = messages[it].message
            val timestamp = messages[it].timestamp
            val my = messages[it].my
            Row(
                horizontalArrangement = if (my) {
                    Arrangement.End
                } else {
                    Arrangement.Start
                },
                modifier = Modifier.fillMaxWidth()
            ) {
//                    Text(text = timestamp.toString(), color = Color.Blue, fontSize = 16.sp)
                Row(
                    modifier = Modifier
                        .background(
                            color = if (my) orange else containerTextField,
                            shape = RoundedCornerShape(20.dp)
                        ),
                ) {
                    Text(
                        text = message,
                        color = if (my) Color.Black else Color.White,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(vertical = 3.dp, horizontal = 5.dp)
                    )
                }
            }
            Spacer(Modifier.height(3.dp))
        }
        item {
            Spacer(Modifier.height(30.dp))
        }
    }
}