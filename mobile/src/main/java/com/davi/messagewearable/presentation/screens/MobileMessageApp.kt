package com.davi.messagewearable.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.davi.messagewearable.presentation.theme.containerTextField
import com.davi.messagewearable.presentation.theme.orange
import com.davi.messagewearable.presentation.viewmodels.DataViewModel
import kotlinx.coroutines.launch

@Composable
fun MobileMessageApp(
    viewModel: DataViewModel, sendMessage: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    var text by remember { mutableStateOf("") }
    val listState = rememberLazyListState()


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(7f)
                .padding(horizontal = 10.dp, vertical = 10.dp)
        ) {

            items(viewModel.messages.size) {
                val message = viewModel.messages[it].message
                val timestamp = viewModel.messages[it].timestamp
                val my = viewModel.messages[it].my
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
                            fontSize = 16.sp,
                            modifier = Modifier.padding(vertical = 3.dp, horizontal = 5.dp)
                        )
                    }
                }
                Spacer(Modifier.height(10.dp))
            }
        }
        TextField(
            value = text,
            onValueChange = {
                text = it
                viewModel.textfield = it
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = containerTextField, unfocusedIndicatorColor = orange,
                focusedContainerColor = containerTextField, focusedIndicatorColor = orange
            ),
            label = { Text(text = "Mensagem", color = orange, fontSize = 16.sp) },
            placeholder = { Text(text = "Digite a mensagem", color = Color.White, fontSize = 16.sp) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    if (text.isNotEmpty()) {
                        sendMessage()
                        viewModel.textfield = ""
                        text = ""
                        focusManager.clearFocus()
                        coroutineScope.launch {
                            listState.scrollToItem(viewModel.messages.lastIndex +1, 20)
                        }
                    }
                },
            ),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp)
        )
        Button(
            onClick = {
                if (text.isNotEmpty()) {
                    sendMessage()
                    viewModel.textfield = ""
                    text = ""
                    coroutineScope.launch {
                        listState.scrollToItem(viewModel.messages.lastIndex +1, 20)
                    }
                }
            }, modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Text(text = "Enviar", color = Color.White, fontSize = 16.sp)
        }
    }

}

//@Preview
//@Composable
//fun MainAppPreview() {
//    MobileMessageApp(
//        messages = emptyList(),
//        sendMessage = {}
//    )
//}