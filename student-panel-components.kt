@Composable
fun StudentPanel(user: User_Class) {
    var currentTab by remember { mutableStateOf(0) }
    val bookingSystem = BookingSystem(Interfaces_Class().rooms)

    Column {
        // Tabs
        TabRow(selectedTabIndex = currentTab) {
            Tab(
                selected = currentTab == 0,
                onClick = { currentTab = 0 }
            ) {
                Text("Wyszukaj i Zarezerwuj", modifier = Modifier.padding(8.dp))
            }
            Tab(
                selected = currentTab == 1,
                onClick = { currentTab = 1 }
            ) {
                Text("Moje Rezerwacje", modifier = Modifier.padding(8.dp))
            }
        }

        // Content
        when (currentTab) {
            0 -> SearchAndBookScreen(user, bookingSystem)
            1 -> UserBookingsScreen(user, bookingSystem)
        }
    }
}

@Composable
fun SearchAndBookScreen(user: User_Class, bookingSystem: BookingSystem) {
    var building by remember { mutableStateOf("") }
    var roomNumber by remember { mutableStateOf("") }
    var selectedOS by remember { mutableStateOf<OS?>(null) }
    var selectedComputer by remember { mutableStateOf<Int?>(null) }
    var selectedDay by remember { mutableStateOf("") }
    var selectedTimeSlot by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<Room>>(emptyList()) }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Building and Room Input
        OutlinedTextField(
            value = building,
            onValueChange = { building = it },
            label = { Text("Budynek (np. JM)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = roomNumber,
            onValueChange = { roomNumber = it },
            label = { Text("Numer sali (np. 606)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // OS Selection
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
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
                    Text(
                        os.name,
                        color = if (selectedOS == os) 
                            MaterialTheme.colors.onPrimary 
                        else 
                            MaterialTheme.colors.onSurface
                    )
                }
            }
        }

        // Search Button
        Button(
            onClick = {
                if (building.isNotEmpty() && roomNumber.isNotEmpty() && selectedOS != null) {
                    searchResults = bookingSystem.searchRooms(building, selectedOS!!)
                        .filter { it.roomNumber == roomNumber }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Wyszukaj")
        }

        // Search Results
        if (searchResults.isNotEmpty()) {
            Text(
                "Dostępne komputery:",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Available Computers
            LazyColumn {
                searchResults.forEach { room ->
                    items(
                        room.computers
                            .filter { it.operatingSystem == selectedOS }
                            .map { it.id }
                    ) { computerId ->
                        Button(
                            onClick = { selectedComputer = computerId },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Text("Komputer $computerId")
                        }
                    }
                }
            }

            // Day Selection
            val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")
            DropdownMenu(
                expanded = selectedComputer != null,
                onDismissRequest = { selectedComputer = null }
            ) {
                days.forEach { day ->
                    DropdownMenuItem(onClick = {
                        selectedDay = day
                    }) {
                        Text(day)
                    }
                }
            }

            // Time Slot Selection
            val timeSlots = listOf("9-11am", "11am-1pm", "1-3pm", "3-5pm")
            if (selectedDay.isNotEmpty()) {
                DropdownMenu(
                    expanded = selectedDay.isNotEmpty(),
                    onDismissRequest = { selectedDay = "" }
                ) {
                    timeSlots.forEach { slot ->
                        DropdownMenuItem(onClick = {
                            selectedTimeSlot = slot
                            // Make reservation
                            message = bookingSystem.bookComputer(
                                user.username,
                                roomNumber,
                                selectedComputer!!,
                                selectedDay,
                                slot
                            )
                        }) {
                            Text(slot)
                        }
                    }
                }
            }
        }

        // Message display
        if (message.isNotEmpty()) {
            Text(
                text = message,
                color = if (message.contains("Booking")) 
                    MaterialTheme.colors.primary 
                else 
                    MaterialTheme.colors.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
fun UserBookingsScreen(user: User_Class, bookingSystem: BookingSystem) {
    var bookings by remember { mutableStateOf(bookingSystem.viewBookings(user.username)) }
    var selectedBooking by remember { mutableStateOf<Booking?>(null) }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Twoje rezerwacje",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (bookings.isEmpty()) {
            Text("Nie masz żadnych rezerwacji")
        } else {
            LazyColumn {
                items(bookings) { booking ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { selectedBooking = booking },
                        elevation = 4.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Sala: ${booking.roomNumber}")
                            Text("Komputer: ${booking.computerId}")
                            Text("Dzień: ${booking.day}")
                            Text("Godziny: ${booking.timeSlot}")
                            
                            Button(
                                onClick = {
                                    message = bookingSystem.cancelBooking(
                                        booking.roomNumber,
                                        booking.computerId,
                                        booking.day,
                                        booking.timeSlot
                                    )
                                    bookings = bookingSystem.viewBookings(user.username)
                                },
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text("Anuluj rezerwację")
                            }
                        }
                    }
                }
            }
        }

        if (message.isNotEmpty()) {
            Text(
                text = message,
                color = if (message.contains("canceled")) 
                    MaterialTheme.colors.primary 
                else 
                    MaterialTheme.colors.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}
