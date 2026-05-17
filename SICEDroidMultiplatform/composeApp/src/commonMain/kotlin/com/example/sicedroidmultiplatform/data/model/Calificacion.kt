package com.example.sicedroidmultiplatform.data.model

data class Calificacion(
    val materia: String,
    val notas: List<Pair<String, String>> // Relaciona el nombre de cada unidad con su calificación obtenida
)
