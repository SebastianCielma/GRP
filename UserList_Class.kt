package classesAndMain // Coded by Viktor, hard coded users by Alex

class UserList_Class() {

    val users = mutableListOf<User_Class>(
        User_Class("Admin", "Admin", true),
        User_Class("Student1", "Student1", false),
        User_Class("Student2", "Student2", false),
        User_Class("Null", "Null", false)
    )

    fun signUp(username: String, password: String, permissions: Boolean) {
        val newUser = User_Class(username, password, permissions)
        users.add(newUser)
        println("User $username created successfully.")
    }

    // code for testing
    fun displayUsers() {
        if (users.isEmpty()) {
            println("No users found.")
            return
        }
        println("List of users:")
        users.forEach { println("Username: ${it.username},Password: ${it.password}, Permissions: ${it.permissions}") }
    }


    fun modifyUser(admin: User_Class) {
        if (!admin.permissions) {
            println("You do not have permission to modify user details.")
            return
        }

        println("Enter the username of the user you want to modify:")
        val username = readLine()
        val user = users.find { it.username == username }

        if (user != null) {
            println("User '$username' found.")
            println("Do you want to change the username (y/n)?")
            val changeUsername = readLine()

            if (changeUsername == "y") {
                println("Enter the new username:")
                val newUsername = readLine()?.trim()
                if (newUsername != null && newUsername.isNotEmpty()) {
                    user.username = newUsername
                    println("Username updated to '$newUsername'.")
                } else {
                    println("Invalid username. No changes made.")
                }
            }

            println("Do you want to change the password (y/n)?")
            val changePassword = readLine()

            if (changePassword == "y") {
                println("Enter the new password for user '$username':")
                val newPassword = readLine()?.trim()
                if (newPassword != null && newPassword.isNotEmpty()) {
                    user.password = newPassword
                    println("Password updated successfully.")
                } else {
                    println("Invalid password. No changes made.")
                }
            }
        } else {
            println("User '$username' not found.")
        }



        fun deleteUser(admin: User_Class) {
            if (!admin.permissions) {
                println("You do not have permission to delete users.")
                return
            }

            println("Enter the username of the user you want to delete:")
            val username = readLine()?.trim()
            val user = users.find { it.username == username }

            if (user != null) {
                users.remove(user)
                println("User '$username' deleted successfully.")
            } else {
                println("User '$username' not found.")
            }
        }
    }
}



