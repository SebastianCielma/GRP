import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation

class ReservationSystemGUI {
    private val interfaceClass = Interfaces_Class()
    
    @Composable
    fun App() {
        var currentScreen by remember { mutableStateOf("login") }
        var currentUser by remember { mutableStateOf<User_Class?>(null) }
        
        MaterialTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                when (currentScreen) {
                    "login" -> LoginScreen(
                        onLoginSuccess = { user ->
                            currentUser = user
                            currentScreen = "main"
                        },
                        onRegisterClick = {
                            currentScreen = "register"
                        }
                    )
                    "register" -> RegisterScreen(
                        onRegisterSuccess = {
                            currentScreen = "login"
                        },
                        onBackClick = {
                            currentScreen = "login"
                        }
                    )
                    "main" -> MainScreen(
                        user = currentUser!!,
                        onLogout = {
                            currentUser = null
                            currentScreen = "login"
                        }
                    )
                }
            }
        }
    }

    @Composable
    private fun LoginScreen(
        onLoginSuccess: (User_Class) -> Unit,
        onRegisterClick: () -> Unit
    ) {
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var error by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "System Rezerwacji Sal",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            if (error.isNotEmpty()) {
                Text(
                    text = error,
                    color = MaterialTheme.colors.error,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = {
                    val user = interfaceClass.users.users.find { 
                        it.username == username && it.password == password 
                    }
                    if (user != null) {
                        onLoginSuccess(user)
                    } else {
                        error = "Nieprawidłowa nazwa użytkownika lub hasło"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Zaloguj się")
            }

            TextButton(
                onClick = onRegisterClick,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Zarejestruj się")
            }
        }
    }

    @Composable
    private fun MainScreen(user: User_Class, onLogout: () -> Unit) {
        var currentTab by remember { mutableStateOf(0) }
        
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("System Rezerwacji Sal") },
                    actions = {
                        TextButton(
                            onClick = onLogout,
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colors.onPrimary
                            )
                        ) {
                            Text("Wyloguj")
                        }
                    }
                )
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding)) {
                if (user.permissions) {
                    AdminPanel(user)
                } else {
                    StudentPanel(user)
                }
            }
        }
    }
}
