package com.example.wishlistapp

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.wishlistapp.data.Wish

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    navController: NavController,
    viewModel: WishViewModel
) {
    val context = LocalContext.current
    Scaffold(
        topBar = { AppBarView(title = "WishList") {
            Toast.makeText(context, "Button Clicked", Toast.LENGTH_LONG).show()
        }},
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddScreen.route + "/0L")
                },
                modifier = Modifier.padding(all = 20.dp),
                contentColor = Color.White,
                containerColor = colorResource(id = R.color.app_bar_color)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) {
        val wishList = viewModel.getAllWishes.collectAsState(initial = listOf())
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            items(wishList.value, key = {wish -> wish.id}) { wish ->
                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = { value ->
                        when(value) {
                            SwipeToDismissBoxValue.StartToEnd -> {
                                val id = wish.id
                                navController.navigate(Screen.AddScreen.route + "/$id")
                            }
                            SwipeToDismissBoxValue.EndToStart -> {
                                viewModel.deleteWish(wish)
                            }
                            else -> Unit
                        }
                        true
                    }
                )
                LaunchedEffect(dismissState.currentValue) {
                    when(dismissState.currentValue) {
                        SwipeToDismissBoxValue.EndToStart -> {
                            dismissState.reset()
                        }
                        SwipeToDismissBoxValue.StartToEnd -> {
                            dismissState.reset()
                        }
                        else -> Unit
                    }
                }
                SwipeToDismissBox(
                    state = dismissState,
                    backgroundContent = {
                        val color by animateColorAsState(
                            when(dismissState.currentValue) {
                                SwipeToDismissBoxValue.StartToEnd -> Color.Green
                                SwipeToDismissBoxValue.EndToStart -> Color.Black
                                else -> Color.Transparent
                            },
                            label = ""
                        )
                        val alignment = when(dismissState.currentValue) {
                            SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                            SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                            else -> Alignment.Center
                        }
                        Box(
                            Modifier
                                .background(color)
                                .fillMaxSize()
                                .padding(top = 14.dp, start = 8.dp, end = 8.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentAlignment = alignment
                        ) {
                            when (dismissState.currentValue) {
                                SwipeToDismissBoxValue.EndToStart -> {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = null,
                                        tint = Color.Black
                                    )
                                }
                                SwipeToDismissBoxValue.StartToEnd -> {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = null,
                                        tint = Color.Black
                                    )
                                }
                                else -> Unit
                            }
                        }
                    },
                    enableDismissFromStartToEnd = true,
                    enableDismissFromEndToStart = true,
                    content = {
                        WishItem(wish = wish) {
                            val id = wish.id
                            navController.navigate(Screen.AddScreen.route + "/$id")
                        }
                    }
                )
            }
        }
    }
}
@Composable
fun WishItem(wish: Wish, function: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 14.dp, start = 8.dp, end = 8.dp)
            .shadow(elevation = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = wish.title,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = wish.description,
            )
        }
    }
}