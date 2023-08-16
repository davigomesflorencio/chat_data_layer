@file:OptIn(ExperimentalComposeUiApi::class)

package com.davi.messagewearable.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import com.davi.messagewearable.presentation.theme.containerTextField
import com.davi.messagewearable.presentation.theme.orange
import com.davi.messagewearable.presentation.viewmodels.ClientDataViewModel
import kotlinx.coroutines.launch

@Composable
fun WearableMessageApp(
    viewModel: ClientDataViewModel,
    sendMessage: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    var text by remember { mutableStateOf("") }
    val listState = rememberLazyListState(0)
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxSize(),
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize(),
        ) {
            ListMessages(messages = viewModel.messages, listState)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                TextField(
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = containerTextField, unfocusedIndicatorColor = orange,
                        focusedContainerColor = containerTextField, focusedIndicatorColor = orange
                    ),
                    textStyle = TextStyle(
                        color = Color.White
                    ),
                    value = text,
                    onValueChange = {
                        text = it
                        viewModel.textfield = it
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (text.isNotEmpty()) {
                                sendMessage()
                                viewModel.textfield = ""
                                text = ""
                                focusManager.clearFocus()
                                coroutineScope.launch {
                                    listState.scrollToItem(viewModel.messages.lastIndex + 2, -40)
                                }
                            }
                        }
                    ),
                    label = { Text(text = "Mensagem", color = orange, fontSize = 10.sp) },
                    placeholder = { Text(text = "Digite a mensagem", color = Color.White, fontSize = 10.sp) },
                    modifier = Modifier
                        .padding(top = 5.dp, bottom = 3.dp)
                )
            }
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = orange),
                onClick = {
                    if (text.isNotEmpty()) {
                        sendMessage()
                        viewModel.textfield = ""
                        text = ""
                        coroutineScope.launch {
                            listState.scrollToItem(viewModel.messages.lastIndex + 1, -40)
                        }
                    }
                })
            {
                Text(text = "Enviar", color = Color.White)
            }
        }
    }
}

//@Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true)
//@Composable
//fun WearableMessageAppPreview() {
//    WearableMessageApp()
//}