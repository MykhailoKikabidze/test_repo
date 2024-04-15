package CheckUserData

fun isPasswordValid(password: String): Boolean {
    val minLength = 8
    val maxLength = 20
    val hasUppercase = password.any { it.isUpperCase() }
    val hasLowercase = password.any { it.isLowerCase() }
    val hasDigit = password.any { it.isDigit() }
    val hasSpecialChar = password.any { it.isLetterOrDigit().not() }

    return password.length in minLength..maxLength
            && hasUppercase
            && hasLowercase
            && hasDigit
            && hasSpecialChar
}
fun  isEmailValid(email: String):Boolean {
    val regex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,})+\$")
    return regex.matches(email)
}

fun isUserNameValid(username: String): Boolean {
    // Define your username criteria
    val minLength = 3
    val maxLength = 20
    val allowedCharacters = Regex("^[a-zA-Z0-9._-]+\$")

    // Perform validation based on criteria
    return username.length in minLength..maxLength
            && allowedCharacters.matches(username)
}