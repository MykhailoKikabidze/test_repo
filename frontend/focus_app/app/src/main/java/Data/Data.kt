package Data

import ApiRequest.*
import androidx.appcompat.app.AppCompatActivity
import StatiscticsFunction.*
import android.content.Context
import android.content.Context.MODE_PRIVATE

var categoryName=""
var activityName=""
var userLogin=""
var categoriesNames:List<String> = listOf()
var activityNamesStatic:List<String> = listOf()
var userEmail=""
data class User(val name: String, val imageUrl: String)

 fun getSavedUserEmail(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences("prefs", MODE_PRIVATE)
    return sharedPreferences.getString("userEmail", null)
}

 fun SaveUserEmail(context: Context, user_email: String?) {
    val sharedPreferences = context.getSharedPreferences("prefs", MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("userEmail", user_email)
    editor.apply()
}
