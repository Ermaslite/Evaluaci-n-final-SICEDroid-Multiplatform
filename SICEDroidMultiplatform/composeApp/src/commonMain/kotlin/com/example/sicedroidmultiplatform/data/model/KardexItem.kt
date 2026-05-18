package com.example.sicedroidmultiplatform.data.model

import kotlinx.serialization.Serializable

@Serializable
data class KardexItem(
    val materia: String = "",
    val nivel: String = "",
    val creditos: String = "0",
    val calificacion: String = "0",
    val acreditacion: String = "",
    val periodo: String = ""
)
