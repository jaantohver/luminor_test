import com.luminor.test.Settings

//TODO Called this class networking, but I don't know if I'll have the time to implement a BE component
//Keeping it local storage only for now, remove this comment later
class Networking(private val settings: Settings) {
    fun registerUser(email: String, password: String): Boolean {
        if (settings.getString("USER_$email") != null) return false
        settings.putString("USER_$email", password)
        return true
    }

    fun loginUser(email: String, password: String): Boolean {
        val savedPassword = settings.getString("USER_$email")
        return savedPassword == password
    }
}