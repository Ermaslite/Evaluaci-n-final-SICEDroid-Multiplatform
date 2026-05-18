package com.example.sicedroidmultiplatform.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CalificacionFinal(
    @SerialName("materia") val materia: String,
    @SerialName("calif") val calificacion: String = "0"
)
