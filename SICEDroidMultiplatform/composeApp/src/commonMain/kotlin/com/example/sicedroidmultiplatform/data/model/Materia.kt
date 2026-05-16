package com.example.sicedroidmultiplatform.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Materia(
    @SerialName("Materia") val nombre: String,
    @SerialName("Docente") val docente: String = "",
    @SerialName("Lunes") val lunes: String = "",
    @SerialName("Martes") val martes: String = "",
    @SerialName("Miercoles") val miercoles: String = "",
    @SerialName("Jueves") val jueves: String = "",
    @SerialName("Viernes") val viernes: String = "",
    @SerialName("Sabado") val sabado: String = "",
    @SerialName("Aula") val aula: String = "",
    @SerialName("Grupo") val grupo: String = ""
)
