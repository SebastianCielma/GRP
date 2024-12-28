package src_Inactive

import classesAndMain.OS
import classesAndMain.Computer
import classesAndMain.Booking
import classesAndMain.BookingSystem
import classesAndMain.Room

fun main() {
    // Hardcoded rooms and computers
    val user = "Null"
    val rooms = listOf(
        Room(
            "JM", "606", listOf(
                Computer(1, OS.WINDOWS),
                Computer(2, OS.MAC),
                Computer(3, OS.LINUX),
                Computer(4, OS.WINDOWS),
                Computer(5, OS.MAC),
                Computer(6, OS.LINUX)
            )
        ),
        Room(
            "JM", "607", listOf(
                Computer(1, OS.MAC),
                Computer(2, OS.LINUX),
                Computer(3, OS.WINDOWS),
                Computer(4, OS.LINUX)
            )
        ),
        Room(
            "JM", "608", listOf(
                Computer(1, OS.WINDOWS),
                Computer(2, OS.MAC),
                Computer(3, OS.LINUX),
                Computer(4, OS.MAC),
                Computer(5, OS.WINDOWS)
            )
        )
    )
    val bookingSystem = BookingSystem(rooms)

    // Loop for main menu
    while (true) {
        println("\nMain Menu:")
        println("1. Search and Book a Computer")
        println("2. View My Bookings")
        println("3. Cancel a Booking")
        println("4. Exit")
        print("Choose an option: ")

        val choice = readLine()?.toIntOrNull() ?: 0

        when (choice) {
            1 -> {
                // Search and Book a Computer
                println("Enter building name and room number ('JM606', 'JM607', 'JM608'):")
                val buildingRoom = readLine()?.trim() ?: ""

                val building = buildingRoom.takeWhile { it.isLetter() } // Take the letter part as building
                val roomNumber = buildingRoom.dropWhile { it.isLetter() } // Take the numeric part as room number

                if (building.isEmpty() || roomNumber.isEmpty()) {
                    println("Invalid building/room format.")
                    continue
                }

                println("Choose Operating System: 1. WINDOWS, 2. MAC, 3. LINUX")
                val osChoice = readLine()?.toIntOrNull() ?: 1
                val selectedOS = when (osChoice) {
                    1 -> OS.WINDOWS
                    2 -> OS.MAC
                    3 -> OS.LINUX
                    else -> {
                        println("Invalid choice, defaulting to WINDOWS.")
                        OS.WINDOWS
                    }
                }

                // Search available rooms
                val availableRooms = bookingSystem.searchRooms(building, selectedOS)
                val matchingRooms = availableRooms.filter { it.roomNumber == roomNumber }

                if (matchingRooms.isEmpty()) {
                    println("No rooms available with $selectedOS OS in building $building and room $roomNumber.")
                } else {
                    println("Available rooms in $building with $selectedOS OS:")

                    matchingRooms.forEach { room ->
                        println("Room: ${room.roomNumber}, Building: ${room.building}")
                        room.computers.filter { it.operatingSystem == selectedOS }.forEach {
                            println("  Computer ID: ${it.id}")
                        }
                    }

                    println("Enter computer ID to book:")
                    val computerId = readLine()?.toIntOrNull() ?: -1

                    if (computerId <= 0) {
                        println("Invalid computer ID. Please try again.")
                        continue
                    }

                    println("Enter the day to book (Students can reserve a computer for the following week only Monday to Friday):")
                    val day = readLine()?.trim() ?: ""

                    if (day.isEmpty()) {
                        println("Invalid day. Please try again.")
                        continue
                    }

                    println("Reservations are  two hour slots (9-11am, 11am-1pm, 1-3pm, 3-5pm).")
                    val timeSlot = readLine()?.trim() ?: ""

                    if (timeSlot.isEmpty()) {
                        println("Invalid time slot. Please try again.")
                        continue
                    }
                    val user = "stop erroring"
                    // Book the computer
                    val bookingResult = bookingSystem.bookComputer(user, roomNumber, computerId, day, timeSlot)
                    println(bookingResult)
                }
            }

            2 -> {
                // View all previous bookings
                val bookings = bookingSystem.viewBookings(user)
                if (bookings.isEmpty()) {
                    println("You have no previous bookings.")
                } else {
                    println("Your previous bookings:")
                    bookings.forEach { booking ->
                        println("Room: ${booking.roomNumber}, Computer ID: ${booking.computerId}, Day: ${booking.day}, Time Slot: ${booking.timeSlot}")
                    }
                }
            }

            3 -> {
                // Cancel a booking
                println("Enter the room number of the booking to cancel:")
                val roomNumber = readLine()?.trim() ?: ""

                println("Enter the computer ID of the booking to cancel:")
                val computerId = readLine()?.toIntOrNull() ?: -1

                if (computerId <= 0) {
                    println("Invalid computer ID. Please try again.")
                    continue
                }

                println("Enter the day of the booking to cancel (e.g., Monday, Tuesday):")
                val day = readLine()?.trim() ?: ""

                println("Enter the time slot of the booking to cancel (e.g., 10:00AM-12:00PM):")
                val timeSlot = readLine()?.trim() ?: ""

                // Cancel the booking
                val cancelResult = bookingSystem.cancelBooking(roomNumber, computerId, day, timeSlot)
                println(cancelResult)
            }

            4 -> {
                println("Exit the program!")
                break
            }

            else -> println("Invalid choice. Please try again.")
        }
    }
}
