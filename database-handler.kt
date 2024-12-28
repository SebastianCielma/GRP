object DatabaseHandler {
    private const val DATABASE_NAME = "reservation_system.db"
    
    fun initializeDatabase() {
        Database.connect(
            "jdbc:sqlite:$DATABASE_NAME",
            driver = "org.sqlite.JDBC"
        )
        
        transaction {
            // Tworzenie tabel jeśli nie istnieją
            SchemaUtils.create(Users, Rooms, Reservations)
            
            // Dodawanie przykładowych danych jeśli baza jest pusta
            if (User.all().count() == 0) {
                User.new {
                    username = "Admin"
                    password = "Admin"
                    isAdmin = true
                }
                User.new {
                    username = "Student1"
                    password = "Student1"
                    isAdmin = false
                }
            }
            
            if (Room.all().count() == 0) {
                // Dodawanie sal
                val room606 = Room.new {
                    building = "JM"
                    number = "606"
                }
                
                // Dodawanie komputerów do sali
                for (i in 1..6) {
                    Computer.new {
                        id = i
                        room = room606
                        operatingSystem = when (i % 3) {
                            0 -> OS.LINUX
                            1 -> OS.WINDOWS
                            else -> OS.MAC
                        }
                    }
                }
                
                // Podobnie dla pozostałych sal...
            }
        }
    }
    
    fun saveBooking(booking: Booking) {
        transaction {
            Reservation.new {
                student = booking.student
                roomNumber = booking.roomNumber
                computerId = booking.computerId
                day = booking.day
                timeSlot = booking.timeSlot
            }
        }
    }
    
    fun loadBookings(): List<Booking> {
        return transaction {
            Reservation.all().map { 
                Booking(
                    it.student,
                    it.roomNumber,
                    it.computerId,
                    it.day,
                    it.timeSlot
                )
            }.toList()
        }
    }
}
