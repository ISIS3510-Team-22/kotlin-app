package com.example.exchangeapp.screens.chatpreview

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.exchangeapp.MENU_SCREEN
import com.example.exchangeapp.model.service.AccountService
import com.example.exchangeapp.model.service.User
import com.example.exchangeapp.model.service.UserRepository
import com.example.exchangeapp.model.service.impl.ChatService
import com.example.exchangeapp.model.service.impl.LocationService
import com.example.exchangeapp.model.service.module.Chat
import com.example.exchangeapp.model.service.module.Message
import com.example.exchangeapp.screens.ExchangeAppViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

@HiltViewModel
class ChatPreviewViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val chatService: ChatService,
    private val accountService: AccountService,
    private val locationService: LocationService,
    private val firestore: FirebaseFirestore
) : ExchangeAppViewModel() {

    private val _userNames = MutableStateFlow<List<String>>(emptyList())
    val userNames: StateFlow<List<String>> = _userNames.asStateFlow()
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()
    val currentUserId = accountService.currentUserId
    var chatId = ""



    init {
        Firebase.analytics.logEvent("Chat_Prev_Screen",null)


    }



    fun updateInfo(internet: Boolean, context: Context) {
        if (internet) {
            Log.d("TREX", "Entrando a guardar los datos")
            fetchUserNames()
            fetchUsers()
            saveSnapshotToCache(context)
            Log.d("TREX", "Entrando a guardar los datos")
        } else {
            // Cargar usuarios desde el caché
            Log.d("TREX", "Entrando a extraer datos")
            fetchUsersFromCache(context)
            errorMessage.value = "No internet connection"
        }
    }






    private fun fetchUserNames() {
        viewModelScope.launch {
            userRepository.getUserNames()
                .collect { names ->
                    _userNames.value = names
                }
        }
    }

    private fun fetchUsers(){
        viewModelScope.launch {
            userRepository.getUsers()
                .collect { users ->
                    _users.value = users
                }
        }
    }

    fun saveSnapshotToCache(context: Context) {
        viewModelScope.launch {
            // Usa la lista de usuarios en el ViewModel
            val users = firestore.collection("users").get().await()
            val chats = firestore.collection("chats").get().await()

            val documentUserList = users.documents.map { document ->
                User(
                    id = document.id,
                    name = document.getString("name") ?: "",
                    email = document.getString("email") ?: "",
                    lat = document.getDouble("lat"),
                    long = document.getDouble("long"),
                    dis = document.getDouble("dis") ?: 0.0
                )
            }

            val documentChatList = chats.documents.map { document ->

                val messages = document.reference.collection("messages").get().await()

                val messageList = messages.documents.map { msgDocument ->
                    Message(
                        id = msgDocument.id,
                        message = msgDocument.getString("message") ?: "",
                        senderId = msgDocument.getString("senderId") ?: "Unknown",
                        receiverId = msgDocument.getString("receiverId") ?: "Unknown",
                        timestamp = msgDocument.getLong("timestamp") ?: 0L
                    )
                }

                Chat(
                    id = document.id,
                    users = (document.get("users") as? List<String> ?: emptyList()),
                    messages = messageList

                )
            }

            // Serializa la lista de usuarios a JSON
            val jsonStringUsers = Json.encodeToString(documentUserList)
            val jsonStringChats = Json.encodeToString(documentChatList)

            // Escribe el JSON en un archivo en el directorio de caché
            val cacheDir = context.cacheDir
            val fileUsers = File(cacheDir, "user_snapshot.json")
            val fileChats = File(cacheDir, "chat_snapshot.json")

            fileUsers.writeText(jsonStringUsers)
            Log.d("TREX", "Datos de usuarios guardados en caché")
            fileChats.writeText(jsonStringChats)
        }
    }

    private fun getSnapshotFromCache(context: Context): List<User>? {
        return try {
            // Leer el contenido del archivo JSON
            val cacheDir = context.cacheDir
            val file = File(cacheDir, "user_snapshot.json")

            // Verificar si el archivo existe
            if (file.exists()) {
                val jsonString = file.readText()
                // Deserializar el JSON a una lista de objetos User
                Json.decodeFromString<List<User>>(jsonString)
            } else {
                Log.d("TREX", "No se encontró el archivo de caché")
                null
            }
        } catch (e: Exception) {
            // Manejar cualquier excepción, como errores de lectura o deserialización
            Log.e("TREX", "Error al leer el caché: ${e.message}")
            null
        }
    }



    private fun fetchUsersFromCache(context: Context) {
        viewModelScope.launch {
            // Lee los usuarios del caché

            val cachedUsers = getSnapshotFromCache(context)

            // Si la lista no es nula, actualiza el estado de los usuarios
            if (cachedUsers != null && cachedUsers.isNotEmpty()) {
                _users.value = cachedUsers
                Log.d("TREX", cachedUsers.toString())
            } else {
                // Manejar el caso donde no hay usuarios en caché
                _users.value = emptyList() // O puedes mostrar un mensaje de error
            }
        }
    }





    fun calculateDistance(
        lat1: Double, lon1: Double, lat2: Double, lon2: Double
    ): Double {
        val earthRadius = 6371.0 // Radio de la Tierra en kilómetros

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c // Distancia en kilómetros
    }

    fun updateUserDistances(
        users: List<User>,
        // Parámetro para identificar al usuario actual
    ): List<User> {
        // Buscamos al usuario actual en la lista de usuarios
        val currentUser = users.find { it.id == currentUserId }

        // Verificamos si encontramos al usuario y si tiene latitud y longitud válidas
        val currentLat = currentUser?.lat ?: return emptyList()
        val currentLon = currentUser.long ?: return emptyList()

        // Recorremos la lista de usuarios y actualizamos el campo 'dis' con la distancia
        return users.map { user ->
            val distance = calculateDistance(currentLat, currentLon, user.lat ?: 0.0, user.long ?: 0.0)

            user.copy(dis = distance) // Actualizamos el campo dis en el objeto User
        }
    }




    fun getMessagesAndSetupChat(userName: String, onChatCreated: (String) -> Unit) {
        viewModelScope.launch {
            // Obtiene el userId de la persona con la que se va a chatear
            val otherUserId = chatService.getUserIdByName(userName)
            if (otherUserId != null) {
                chatService.createChat(otherUserId, onChatCreated, userName, currentUserId)
                chatId =
                    if (currentUserId < otherUserId) "$currentUserId-$otherUserId" else "$otherUserId-$currentUserId"

            }
        }
    }

    fun fetchCurrentLocation(onLocationFetched: (Location?) -> Unit) {
        viewModelScope.launch {

            val location = locationService.getCurrentLocation()
            if (location != null) {
                Log.d("PERMISSION", "Ubicación obtenida ViewModel: ${location.latitude}, ${location.longitude}")
            }
            onLocationFetched(location) // Devolver la ubicación obtenida
        }
    }

    fun updateUserLocationInFirestore() {
        viewModelScope.launch {
            // Obtener la ubicación actual del usuario
            val location = locationService.getCurrentLocation()

            if (location != null) {
                // Crear un mapa con la latitud y longitud

                try {
                    // Actualizar la ubicación en Firestore en la colección "users"
                    firestore.collection("users")
                        .document(currentUserId)
                        .update(
                            "lat", location.latitude,
                            "long", location.longitude
                        )
                        .addOnSuccessListener {
                            Log.d("Firestore", "Ubicación actualizada correctamente")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Error al actualizar la ubicación", e)
                        }
                } catch (e: Exception) {
                    Log.e("Firestore", "Error: ${e.message}")
                }
            } else {
                Log.e("Location", "Ubicación o ID de usuario no disponible")
            }
        }
    }

    fun handleLocationUpdate(users: List<User>, onLocationUpdated: (List<User>) -> Unit) {
        fetchCurrentLocation { location ->
            if (location != null) {
                Log.d(
                    "PERMISSION",
                    "Location obtained ViewModel: ${location.latitude}, ${location.longitude}"
                )
            }

            viewModelScope.launch {
                try {
                    updateUserLocationInFirestore()
                    val updatedUsers = updateUserDistances(users).sortedBy { it.dis }
                    onLocationUpdated(updatedUsers)
                    Log.d("USERS", updatedUsers.toString())

                } catch (e: Exception) {
                    Log.e("PERMISSION", "Error updating location", e)
                }
            }
        }
    }


    fun onMenuClick(open: (String) -> Unit){
        open(MENU_SCREEN)
    }
}





