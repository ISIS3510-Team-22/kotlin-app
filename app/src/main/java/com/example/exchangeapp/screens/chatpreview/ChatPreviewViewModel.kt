package com.example.exchangeapp.screens.chatpreview

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangeapp.INFO_SCREEN
import com.example.exchangeapp.MENU_SCREEN
import com.example.exchangeapp.SIGN_UP_SCREEN
import com.example.exchangeapp.model.service.AccountService
import com.example.exchangeapp.model.service.User
import com.example.exchangeapp.model.service.UserRepository
import com.example.exchangeapp.model.service.impl.ChatService
import com.example.exchangeapp.model.service.impl.LocationService
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
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

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
        fetchUserNames()
        fetchUsers()


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





