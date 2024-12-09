package com.pakenanya.mindsync.ui.screen.main.document

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pakenanya.mindsync.ui.screen.main.MainViewModel

@Composable
fun DocumentScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    mainViewModel: MainViewModel
) {

//    val viewModel: DocumentViewModel = hiltViewModel()

    val documentsData by mainViewModel.documentsData.observeAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp, start = 20.dp, bottom = 20.dp, end = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                "Documents", style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                documentsData?.let {
                    items(it.size) { index ->
                        DocumentItem(
                            document_id = it[index].id,
                            title = it[index].title,
                            description = "Summary: ${it[index].summary}",
                            navController = navController
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun DocumentItem(document_id: Int, title: String, description: String, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("document_detail/${document_id}") },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(description, style = MaterialTheme.typography.bodySmall)
        }
    }
}