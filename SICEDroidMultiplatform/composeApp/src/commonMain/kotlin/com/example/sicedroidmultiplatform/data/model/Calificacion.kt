package com.example.sicedroidmultiplatform.data.model

data class Calificacion(
    val materia: String,
    val notas: List<Pair<String, String>> // Lista de (Unidad, Nota)
)
