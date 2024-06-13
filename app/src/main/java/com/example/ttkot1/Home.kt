package com.example.ttkot1

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        data class Cat(val id: Int, val name: String, val des: String, val url: String)

        val initialCatList = listOf(
            Cat(1, "Tom", "Beos us", "https://cdn.chanhtuoi.com/uploads/2022/04/mon-leo-la-gi-5.jpg"),
            Cat(2, "Tom", "Beos us", "https://yt3.googleusercontent.com/kNeiEGO0rl3lQa3iQDmo_eXDYds5WNPVZr4odb3XxHM0LZ6ZZGYJEm4EByyjUbRBxUedEnOx=s900-c-k-c0x00ffffff-no-rj"),
            Cat(3, "Tom", "Beos us", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSYMWbd2N9DVdHJAaguIo1Oq1Q9__LfFDqI5w&s"),
            Cat(4, "Tom", "Beos us", "https://i.pinimg.com/236x/c8/4f/b5/c84fb5c1f1e67100cb1d215bea71a601.jpg"),
            Cat(5, "Tom", "Beos us", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQsDNQ17AmqguNqONSu8nNFvHkWxw8wf9LKAA&s"),
            Cat(6, "Tom", "Beos us", "https://cdn.chanhtuoi.com/uploads/2022/04/mon-leo-la-gi-5.jpg"),
            Cat(7, "Tom", "Beos us", "https://cdn.chanhtuoi.com/uploads/2022/04/mon-leo-la-gi-5.jpg"),
            Cat(8, "Tom", "Beos us", "https://cdn.chanhtuoi.com/uploads/2022/04/mon-leo-la-gi-5.jpg"),
            Cat(9, "Tom", "Beos us", "https://cdn.chanhtuoi.com/uploads/2022/04/mon-leo-la-gi-5.jpg"),
            Cat(10, "Tom", "Beos us", "https://cdn.chanhtuoi.com/uploads/2022/04/mon-leo-la-gi-5.jpg")
        )

        setContent {
            // Sử dụng mutableStateListOf để đảm bảo danh sách có thể phản ứng với các thay đổi
            val DanhSach = remember { mutableStateListOf<Cat>().apply { addAll(initialCatList) } }

            var showAddDialog by remember { mutableStateOf(false) }
            var showUDDialog by remember { mutableStateOf(false) }
            var newCatName by remember { mutableStateOf("") }
            var newCatDes by remember { mutableStateOf("") }
            var newCatUrl by remember { mutableStateOf("") }

            var selectedCat by remember { mutableStateOf<Cat?>(null) }

            var editingCat by remember { mutableStateOf<Cat?>(null) }
            Column {
                Button(onClick = {
                    showAddDialog = true
                }) {
                    Text(text = "Add")
                }

                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    columns = GridCells.Fixed(3)
                ) {
                    items(DanhSach.size) { viTri ->
                        val cat = DanhSach[viTri]
                        Column {
                            AsyncImage(
                                modifier = Modifier.size(90.dp, 150.dp),
                                contentScale = ContentScale.Crop,
                                model = cat.url, contentDescription = ""
                            )
                            Text(text = "${cat.id}")
                            Text(text = "${cat.name}")
                            Text(text = "${cat.des}")

                            // Xem chi tiết
                            Button(onClick = {
                                selectedCat = cat
                            }) {
                                Text(text = "Detail")
                            }

                            // Xóa
                            Button(onClick = {
                                DanhSach.remove(cat)
                            }) {
                                Text(text = "Delete")
                            }
                            Button(onClick = {
                                editingCat = cat // Đặt mèo được chỉnh sửa là cat hiện tại
                                newCatName = cat.name
                                newCatDes = cat.des
                                newCatUrl = cat.url
                                showUDDialog = true // Hiển thị dialog chỉnh sửa
                            }) {
                                Text(text = "update")
                            }
                        }
                    }
                }
            }

            // Dialog để thêm mèo mới
            if (showAddDialog) {
                AlertDialog(
                    onDismissRequest = { showAddDialog = false },
                    confirmButton = {
                        Button(onClick = {
                            // Thêm mèo mới vào danh sách
                            if (newCatName.isNotBlank() && newCatDes.isNotBlank() && newCatUrl.isNotBlank()) {
                                val newId = DanhSach.size + 1
                                DanhSach.add(Cat(newId, newCatName, newCatDes, newCatUrl))
                                // Đặt lại các biến sau khi thêm
                                newCatName = ""
                                newCatDes = ""
                                newCatUrl = ""
                                showAddDialog = false
                            }
                        }) {
                            Text("Add")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showAddDialog = false }) {
                            Text("Cancel")
                        }
                    },
                    title = { Text("Add New Cat") },
                    text = {
                        Column {
                            TextField(
                                value = newCatName,
                                onValueChange = { newCatName = it },
                                label = { Text("Name") }
                            )
                            TextField(
                                value = newCatDes,
                                onValueChange = { newCatDes = it },
                                label = { Text("Description") }
                            )
                            TextField(
                                value = newCatUrl,
                                onValueChange = { newCatUrl = it },
                                label = { Text("Image URL") }
                            )
                        }
                    }
                )
            }

            // Dialog để chỉnh sửa mèo
            if (showUDDialog) {
                AlertDialog(
                    onDismissRequest = { showUDDialog = false },
                    confirmButton = {
                        Button(onClick = {
                            // Cập nhật thông tin mèo
                            editingCat?.let { cat ->
                                val updatedCat = Cat(cat.id, newCatName, newCatDes, newCatUrl)
                                DanhSach[DanhSach.indexOf(cat)] = updatedCat
                                // Đặt lại các biến sau khi cập nhật
                                newCatName = ""
                                newCatDes = ""
                                newCatUrl = ""
                                showUDDialog = false
                                editingCat = null
                            }
                        }) {
                            Text("Update")
                        }
                    },
                    dismissButton = {
                        Button(onClick = {
                            showUDDialog = false
                            editingCat = null // Đặt lại editingCat khi hủy dialog
                        }) {
                            Text("Cancel")
                        }
                    },
                    title = { Text("Update Cat") },
                    text = {
                        Column {
                            TextField(
                                value = newCatName,
                                onValueChange = { newCatName = it },
                                label = { Text("Name") }
                            )
                            TextField(
                                value = newCatDes,
                                onValueChange = { newCatDes = it },
                                label = { Text("Description") }
                            )
                            TextField(
                                value = newCatUrl,
                                onValueChange = { newCatUrl = it },
                                label = { Text("Image URL") }
                            )
                        }
                    }
                )
            }

            // Hiển thị chi tiết mèo trong một dialog khác
            selectedCat?.let {
                AlertDialog(
                    onDismissRequest = { selectedCat = null },
                    confirmButton = {
                        Button(onClick = { selectedCat = null }) {
                            Text("Close")
                        }
                    },
                    title = { Text("Cat Details") },
                    text = {
                        Column {
                            AsyncImage(
                                modifier = Modifier.size(150.dp, 150.dp),
                                contentScale = ContentScale.Crop,
                                model = it.url, contentDescription = ""
                            )
                            Text(text = "Tên: ${it.name}")
                            Text(text = "Mô tả: ${it.des}")
                        }
                    }
                )
            }
        }
    }
}
