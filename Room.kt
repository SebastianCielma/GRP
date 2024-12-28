package classesAndMain // Class coded by Damian, method coded by Viktor

// Room.kt
data class Room(val building: String, val roomNumber: String, val computers: List<Computer>)

private val rooms: MutableList<Room> = mutableListOf()

// modify a room
fun modifyRoom(rooms: MutableList<Room>) {
    println("Enter the number of the room you want to modify")
    val roomNumber = readlnOrNull()?.uppercase()


    if (roomNumber.isNullOrEmpty()) {
        println("Room number cannot be empty.")
        return
    }

    val room = rooms.find { it.roomNumber == roomNumber }

    if (room != null) {
        println("Choose a new OS for the room: (1) Windows, (2) Mac, (3) Linux")
        val osChoice = readlnOrNull()?.toIntOrNull()

        val newOSType = when (osChoice) {
            1 -> OS.WINDOWS
            2 -> OS.MAC
            3 -> OS.LINUX
            else -> {
                println("Invalid OS choice. No changes made.")
                return
            }
        }
        room.computers.forEach { it.operatingSystem = newOSType }
        println("Room $roomNumber has been updated")
    } else {
        println("Room $roomNumber not found.")
    }
}



// delete a room
fun deleteRoom(rooms: MutableList<Room>) {
    println("Enter the number of the room you want to delete")
    val roomNumber = readlnOrNull()?.uppercase()


    if (roomNumber.isNullOrEmpty()) {
        println("Room number cannot be empty.")
        return
    }

    val room = rooms.find { it.roomNumber == roomNumber }

    if (room != null) {
        rooms.remove(room)
        println("Room $roomNumber has been deleted.")
    } else {
        println("Room $roomNumber not found.")
    }
}