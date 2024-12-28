@Composable
fun AdminPanel(user: User_Class) {
    var currentTab by remember { mutableStateOf(0) }
    val bookingSystem = BookingSystem(Interfaces_Class().rooms)

    Column {
        TabRow(selectedTabIndex = currentTab) {
            Tab(
                selected = currentTab == 0,
                onClick = { currentTab = 0 }
            ) {
                Text("Zarządzanie Rezerwacjami", modifier = Modifier.padding(8.dp))
            }
            Tab(
                selected = currentTab == 1,
                onClick = { currentTab = 1 }
            ) {
                Text("Zarządzanie Użytkownikami", modifier = Modifier.padding(8.dp))
            }
            Tab(
                selected = currentTab == 2,
                onClick = { currentTab = 2 }
            ) {
                Text("Zarządzanie Salami", modifier = Modifier.padding(8.dp))
            }
        }

        when (currentTab) {
            0 -> BookingManagementScreen(bookingSystem)
            1 -> UserManagementScreen(user)
            2 -> RoomManagementScreen()
        }
    }
}

@Composable
fun BookingManagementScreen(bookingSystem: BookingSystem) {
    var selectedRoom by remember { mutableStateOf("") }
    var selectedDay by remember { mutableStateOf("") }
    var bookings by remember { mutableStateOf<List<Booking>>(emptyList()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Room Selection
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("606", "607", "608").forEach { room ->
                Button(
                    onClick = { selectedRoom = room },
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                ) {
                    Text("JM$room")
                }
            }
        }

        // Day Selection
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday").forEach { day ->
                Button(
                    onClick = {
                        selectedDay = day
                        bookings = bookingSystem.bookings.returnBookingsByDay(day, selectedRoom)
                    },
                    modifier = Modifier.weight(1f).padding(horizontal = 2.dp)
                ) {
                    Text(day.substring(0, 3))
                }
            }
        }

        // Bookings Display
        LazyColumn {
            items(bookings) { booking ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Student: ${booking.student}")
                        Text("Komputer: ${booking.computerId}")
                        Text("Godziny: ${booking.timeSlot}")
                    }
                }
            }
        }
    }
}

@Composable
fun UserManagementScreen(adminUser: User_Class) {
    val userList = remember { UserList_Class() }
    var users by remember { mutableStateOf(userList.users) }
    var showAddUserDialog by remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<User_Class?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(
            onClick = { showAddUserDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Dodaj Użytkownika")
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(users) { user ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { selectedUser = user },
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Nazwa: ${user.username}")
                        Text("Admin: ${if (user.permissions) "Tak" else "Nie"}")
                    }
                }
            }
        }

        if (showAddUserDialog) {
            AddUserDialog(
                onDismiss = { showAddUserDialog = false },
                onUserAdded = { username, password, isAdmin ->
                    userList.signUp(username, password, isAdmin)
                    users = userList.users
                    showAddUserDialog = false
                }
            )
        }

        selectedUser?.let { user ->
            EditUserDialog(
                user = user,
                onDismiss = { selectedUser = null },
                onUserModified = { newUsername, newPassword ->
                    userList.modifyUser(adminUser)
                    users = userList.users
                    selectedUser = null
                }
            )
        }
    }
}

@Composable
fun RoomManagementScreen() {
    val rooms = remember { mutableStateOf(Interfaces_Class().rooms) }
    var selectedRoom by remember { mutableStateOf<Room?>(null) }
    var showModifyDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Zarządzanie Salami",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {
            items(rooms.value) { room ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { 
                            selectedRoom = room
                            showModifyDialog = true 
                        },
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Sala: ${room.building}${room.roomNumber}")
                        Text("Liczba komputerów: ${room.computers.size}")
                        Text("Systemy operacyjne: ${room.computers.map { it.operatingSystem }.distinct().joinToString()}")
                    }
                }
            }
        }

        if (showModifyDialog && selectedRoom != null) {
            ModifyRoomDialog(
                room = selectedRoom!!,
                onDismiss = { 
                    showModifyDialog = false
                    selectedRoom = null
                },
                onRoomModified = { newOS ->
                    modifyRoom(rooms.value)
                    showModifyDialog = false
                    selectedRoom = null
                }
            )
        }
    }
}

// Dialogi
@Composable
fun AddUserDialog(
    onDismiss: () -> Unit,
    onUserAdded: (String, String, Boolean) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isAdmin by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Dodaj Użytkownika") },
        text = {
            Column {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Nazwa użytkownika") }
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Hasło") }
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isAdmin,
                        onCheckedChange = { isAdmin = it }
                    )
                    Text("Administrator")
                }
            }
        },
        confirmButton = {
            Button(onClick = { onUserAdded(username, password, isAdmin) }) {
                Text("Dodaj")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Anuluj")
            }
        }
    )
}

@Composable
fun EditUserDialog(
    user: User_Class,
    onDismiss: () -> Unit,
    onUserModified: (String, String) -> Unit
) {
    var newUsername by remember { mutableStateOf(user.username) }
    var newPassword by remember { mutableStateOf(user.password) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edytuj Użytkownika") },
        text = {
            Column {
                OutlinedTextField(
                    value = newUsername,
                    onValueChange = { newUsername = it },
                    label = { Text("Nazwa użytkownika") }
                )
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Hasło") }
                )
            }
        },
        confirmButton = {
            Button(onClick = { onUserModified(newUsername, newPassword) }) {
                Text("Zapisz")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Anuluj")
            }
        }
    )
}

@Composable
fun ModifyRoomDialog(
    room: Room,
    onDismiss: () -> Unit,
    onRoomModified: (OS) -> Unit
) {
    var selectedOS by remember { mutableStateOf<OS?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Modyfikuj Salę ${room.building}${room.roomNumber}") },
        text = {
            Column {
                Text("Wybierz nowy system operacyjny dla wszystkich komputerów:")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OS.values().forEach { os ->
                        Button(
                            onClick = { selectedOS = os },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = if (selectedOS == os) 
                                    MaterialTheme.colors.primary 
                                else 
                                    MaterialTheme.colors.surface
                            )
                        ) {
                            Text(os.name)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { selectedOS?.let { onRoomModified(it) } },
                enabled = selectedOS != null
            ) {
                Text("Zapisz")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Anuluj")
            }
        }
    )
}
