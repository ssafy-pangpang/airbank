package com.example.myapplication.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.model.PATCHMembersRequest
import com.example.myapplication.viewmodel.SignUpViewModel

@Composable
fun SignUpScreen(
    navController: NavController
) {
    val viewModel: SignUpViewModel = viewModel()


    var phoneNumber by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val TAG = "SIGN UP"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            singleLine = true,
            placeholder = {
                Text(text = "Phone Number")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        TextField(
            value = name,
            onValueChange = { name = it },
            singleLine = true,
            placeholder = {
                Text(text = "Name")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )



        Spacer(modifier = Modifier.height(16.dp))

        // User Role Selection
        Column(Modifier.selectableGroup()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { role = "CHILD" }
            ) {
                RadioButton(
                    selected = role == "CHILD",
                    onClick = { role = "CHILD" }
                )
                Text(text = "아이", modifier = Modifier.padding(start = 8.dp))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { role = "PARENT" }
            ) {
                RadioButton(
                    selected = role == "PARENT",
                    onClick = { role = "PARENT" }
                )
                Text(text = "부모", modifier = Modifier.padding(start = 8.dp))
            }
        }

        // Show a warning message if showError is true
        if (showError) {
            Text(
                text = "Please fill in all required fields",
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }


        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (phoneNumber.isNotEmpty() && name.isNotEmpty() && role.isNotEmpty()) {
                    val requestDTO = PATCHMembersRequest(
                        name = name,
                        phoneNumber = phoneNumber,
                        role = role
                    )
                    viewModel.signUpUser(requestDTO,navController)
                } else {  // Set showError to true to display the warning message (required fields unfilled)
                    showError = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Sign Up")
        }
    }
}
