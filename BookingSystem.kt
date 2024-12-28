package classesAndMain // Coded by Damian, slight change by Alex to integrate

// BookingSystem.kt
class BookingSystem(val rooms: List<Room>) {
    val bookings = BookingList_Class()

    // Search rooms by building and operating system
    fun searchRooms(building: String, os: OS): List<Room> {
        return rooms.filter { room ->
            room.building.equals(building, ignoreCase = true) && room.computers.any { it.operatingSystem == os }
        }
    }

    // Book a computer in a room for a specific day and time slot
    fun bookComputer(student: String, roomNumber: String, computerId: Int, day: String, timeSlot: String): String {
        // Check if this computer is already booked for that time
        val existingBooking = bookings.bookings.find { it.roomNumber == roomNumber && it.computerId == computerId && it.day == day && it.timeSlot == timeSlot }
        if (existingBooking != null) {
            return "This computer is already booked for that time slot."
        }
        // Add the new booking
        bookings.bookings.add(Booking(student, roomNumber, computerId, day, timeSlot))
        println(bookings.bookings)
        return "Booking the computer! ID: $roomNumber-$computerId"

    }

    // View all previous bookings
    fun viewBookings(user: String): List<Booking> {
        return bookings.returnUserBookings(user)
    }

    // Cancel a specific booking
    fun cancelBooking(roomNumber: String, computerId: Int, day: String, timeSlot: String): String {
        val bookingToCancel = bookings.bookings.find { it.roomNumber == roomNumber && it.computerId == computerId && it.day == day && it.timeSlot == timeSlot }
        return if (bookingToCancel != null) {
            bookings.bookings.remove(bookingToCancel)
            "Booking canceled for $roomNumber-$computerId on $day at $timeSlot."
        } else {
            "No booking found to cancel."
        }
    }

    fun interfaceClassChoice1SubMenu1() {
        while (true) {
            println(
                "Which room would you like to view?" +
                        "\n\"JM606\", \"JM607\", \"JM608\""
            )
            var roomChoice = readln()
            when (roomChoice) {
                "JM606" -> {
                    roomChoice = "606"
                }

                "JM607" -> {
                    roomChoice = "607"
                }

                "JM608" -> {
                    roomChoice = "608"
                }

                else -> println("Invalid room")
            }
            print("Which day of the week would you like to see the bookings for?")
            val choice2 = readln()
            when (choice2) {
                "Monday" -> {
                    println(bookings.returnBookingsByDay("Monday", roomChoice))
                    break
                }

                "Tuesday" -> {
                    println(bookings.returnBookingsByDay("Tuesday", roomChoice))
                    break
                }

                "Wednesday" -> {
                    println(bookings.returnBookingsByDay("Wednesday", roomChoice))
                    break
                }

                "Thursday" -> {
                    println(bookings.returnBookingsByDay("Thursday", roomChoice))
                    break
                }

                "Friday" -> {
                    println(bookings.returnBookingsByDay("Friday", roomChoice))
                    break
                }

                else -> println("Invalid day")
            }
        }
    }

    fun interfaceClassChoice1SubMenu2() {
        bookings.printAll()
    }
}
