package classesAndMain

import java.util.*
import kotlin.system.exitProcess

// Coded by Alex

open class Interfaces_Class() {

    // Hardcoded rooms and computers
    val rooms = mutableListOf<Room>(
        Room(
            "JM", "606", mutableListOf<Computer>(
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
    val bookings = bookingSystem.bookings
    val users = UserList_Class()

    fun existingUser() {
        println("Do you have an account already? (Y/N)")
        val choice = readln().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        when (choice) {
            "Y" -> loginInterface()
            "N" -> { println("What would you like your username to be?")
                val username = readln()
                println("What would you like your password to be?")
                val password = readln()
                println("Is this an Admin account? (Y/N)")
                val perms = readln()
                var permissions = false
                when (perms) {
                    "Y" -> permissions = true
                    "N" -> permissions = false
                }
                users.signUp(username, password, permissions)
            }
        }
        existingUser()
    }

    fun loginInterface() {
        val userList = UserList_Class()
        var user: User_Class = User_Class("Null", "Null", false) // Placeholder user class
        var userPass: String? = "None"
        while (true) {
            println("\nEnter your username: ")
            val userLogin = readln()
            for (s in userList.users) {
                if (s.username == userLogin) {
                    println("\nEnter your password $userLogin")
                    userPass = readln()
                    user = s
                    break
                }
            }
            if (user != null) {
                if (user.password == userPass) {
                    break
                } else {
                    println("\nInvalid username or password")
                }
            }
        }
        if (user.permissions == true) {
            adminInterface(user)
        } else {
            studentInterface(user)
        }

    }

    fun studentInterface(user: User_Class) {
        BookingInterface(user.username)
        println("Successfully logged out!")
        loginInterface()
    }

    fun adminInterface(user: User_Class) {
        println(
            "1. Bookings\n" +
                    "2. Rooms\n" +
                    "3. Edit Users\n" +
                    "3. Logout \n"
        )
        val choice = readln()

        if (choice == "1") {
            choice1Interface(user)
        } else if (choice == "2") {
            choice2Interface(user)
        } else if (choice == "3") {
            choice3Interface(user)
        } else if (choice == "4") {
            println("Successfully logged out!")
            loginInterface()
        }

    }

    fun choice1Interface(user: User_Class) {
        println(
            "1. Create/Manage/Delete personal bookings\n" +
                    "2. See created bookings per day\n" +
                    "3. See all bookings\n" +
                    "4. Go back"
        )
        val choice = readln()
        when (choice) {
            "1" -> {
                BookingInterface(user.username)
            }

            "2" -> {
                choice1SubMenu1()
                choice1Interface(user)
            }

            "3" -> {
                choice1SubMenu2()
                choice1Interface(user)
            }

            "4" -> adminInterface(user)
        }
    }

    fun choice1SubMenu1() {

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

    fun choice1SubMenu2() {
        bookings.printAll()
    }

    fun choice2Interface(user: User_Class) {
        println(
            "1. Add a room" +
                    "\n2. Modify an existing room" +
                    "\n3. Remove a room" +
                    "\n4. Go back"
        )
        val choice = readln()

        when (choice) {
            "1" -> { /* Add a room */ }
                "2" -> { modifyRoom(rooms) }
                "3" -> { deleteRoom(rooms) }
                "4" -> adminInterface(user)
            }
        }

    fun choice3Interface(user: User_Class) {
        println("1. Display all users\n" +
                "2. Modify current users\n" +
                "3. Go back")
        val choice = readln()
        when (choice) {
            "1" -> users.displayUsers()
            "2" -> users.modifyUser(user)
            "3" -> adminInterface(user)
        }
    }

    fun BookingInterface(user: String) {  // Coded by Damian, slight changes by Alex to integrate

        // Loop for main menu
        while (true) {
            println("\n1. Search and Book a Computer")
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
                    val roomNumber =
                        buildingRoom.dropWhile { it.isLetter() } // Take the numeric part as room number

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
                    break
                }

                else -> println("Invalid choice. Please try again.")
            }
        }
    }
}