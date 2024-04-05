package ApiRequest

data class ResponseMessage(val message: String)

data class ResponseList(
    val categoriesList: List<Category> // Убедитесь, что этот список соответствует структуре JSON
)

data class Category(
    val name: String // И другие возможные поля
)

data class User(
    val login: String,
    val email: String,
    val password: String
)

data class Error(
    val num: Int,
    val description: String
)