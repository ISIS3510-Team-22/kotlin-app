package com.example.exchangeapp.screens.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.exchangeapp.MENU_SCREEN
import com.example.exchangeapp.screens.ExchangeAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor() : ExchangeAppViewModel() {
    private  val _webPageUrls = MutableLiveData<List<String>>()
    val webPageUrl : LiveData<List<String>> = _webPageUrls

    init {
        _webPageUrls.value = listOf(
            "https://internacionalizacion.uniandes.edu.co/programas-movilidad-academica/intercambio-semestral",
            "https://sistemas.uniandes.edu.co/es/isis-oportunidades/informacion-general-oportunidades#tab-intercambios-en-el-exterior",
            "https://mecanica.uniandes.edu.co/es/estudiantes/pregrado/intercambio-internacional"
        )
    }

    fun onMenuClick(open: (String) -> Unit) {
        launchCatching {
            open(MENU_SCREEN)
        }
    }

}