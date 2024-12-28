package classesAndMain // Coded by Alex

class BookingList_Class() {

    val bookings: MutableList<Booking> = mutableListOf(
        Booking("Student1", "606", 1, "Monday", "9-11am"),
        Booking("Student1", "607", 2, "Tuesday", "1-3pm"),
        Booking("Student2", "608", 4, "Friday", "1-3pm"),
        Booking("Student1", "608", 3, "Friday", "9-11am"),
        Booking("Student2", "606", 3, "Monday", "3-5pm"),
        Booking("Student2", "608", 1, "Thursday", "9-11am"),
        Booking("Admin", "607", 2, "Tuesday", "11am-1pm")
    )

    fun returnUserBookings(user: String): MutableList<Booking> {
        val returnList = mutableListOf<Booking>()
        bookings.forEach{ booking ->
            if (booking.student == user) {
                returnList.add(booking)
            }
        }
        return returnList
    }

    fun returnBookingsByDay(day: String, room: String): MutableList<Booking>  {
        val returnList = mutableListOf<Booking>()
        for (b in bookings) {
            if (b.day == day && b.roomNumber == room) {
                returnList.add(b)
                }
        }
        return returnList
    }

    fun printAll() {
        for (b in bookings) {
            println("User: ${b.student}, Room: ${b.roomNumber}, Computer: ${b.computerId}, Day: ${b.day}, Timeslot: ${b.timeSlot}")
        }
    }

}