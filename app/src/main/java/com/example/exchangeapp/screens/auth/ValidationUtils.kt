package com.example.exchangeapp.screens.auth

object ValidationUtils {
    private val emailRegex = """^[\w-.]+@([\w-]+\.)+[\w-]{2,4}${'$'}""".toRegex()
    private val passwordRegex = """^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@${'$'}%^&*-]).{6,}${'$'}""".toRegex()

    fun validateEmail(email: String): String {
        return if (!emailRegex.containsMatchIn(email)) {
            "Invalid email"
        } else {
            ""
        }
    }

    fun validatePassword(password: String): String {
        return if (!passwordRegex.containsMatchIn(password)) {
            "Use 6+ chars, 1 uppercase, 1 special"
        } else {
            ""
        }
    }

    fun validatePasswordsMatch(password: String, confirmPassword: String): String {
        return if (password != confirmPassword) {
            "Passwords do not match"
        } else {
            ""
        }
    }
}
