package com.nhom9.message.screens.reportscreens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nhom9.message.CommonDivider
import com.nhom9.message.MViewModel
import com.nhom9.message.TitleBarWithBack
import com.nhom9.message.data.ReportOption


@Composable
fun ReportOptionScreen(navController: NavController, viewModel: MViewModel, reportOptionIndex: String, userId: String) {
    val reportContent = remember {
        mutableStateOf("")
    }
    val nav = navController.previousBackStackEntry?.destination?.route
    val context = LocalContext.current
    val reportOption = ReportOption.entries[reportOptionIndex.toInt()]
    Column {
        TitleBarWithBack(navController = navController, text = "Report")
        CommonDivider()
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Text(
                text = reportOption.title,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(start = 20.dp)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text(
                text = "Examples of what to report:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 20.dp)
            )
        }
        CommonBaseRow(text = reportOption.text1)
        CommonBaseRow(text = reportOption.text2)
        CommonBaseRow(text = reportOption.text3)
        OutlinedTextField(
            value = reportContent.value,
            onValueChange = { reportContent.value = it },
            placeholder = {
                Text(
                    text = "Please explain your reasoning for this report"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(120.dp)
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    if (reportContent.value != "") {
                        viewModel.reportUser(userId, reportOption.title, reportContent.value)
                        Toast.makeText(
                            context, "Thank you for submitting a report", Toast.LENGTH_SHORT
                        ).show()
                        navController.popBackStack(nav!!, true)
                    } else {
                        Toast.makeText(context, "Please state your reasoning", Toast.LENGTH_SHORT)
                            .show()
                    }
                }, modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp)
            ) {
                Text(text = "Submit")
            }
        }
    }
}

@Composable
fun CommonBaseRow(text: String, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Check, contentDescription = null,
            modifier = Modifier.padding(start = 20.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}