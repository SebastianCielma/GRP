fun main() {
    DatabaseHandler.initializeDatabase()
    
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "System Rezerwacji Sal",
            state = rememberWindowState(width = 1024.dp, height = 768.dp)
        ) {
            ReservationSystemGUI().App()
        }
    }
}
