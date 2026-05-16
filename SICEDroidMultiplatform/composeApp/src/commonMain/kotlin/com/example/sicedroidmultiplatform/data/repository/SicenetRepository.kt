package com.example.sicedroidmultiplatform.data.repository

import com.example.sicedroidmultiplatform.data.remote.SicenetService
import com.example.sicedroidmultiplatform.data.remote.SoapRequestBuilder
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class SicenetRepository {
    private val client = SicenetService.client

    suspend fun login(matricula: String, contrasenia: String, tipo: String): String? {
        return try {
            val response = client.post("https://sicenet.surguanajuato.tecnm.mx/ws/wsalumnos.asmx") {
                header("Content-Type", "text/xml; charset=utf-8")
                header("SOAPAction", "\"http://tempuri.org/accesoLogin\"")
                setBody(SoapRequestBuilder.buildLoginBody(matricula, contrasenia, tipo.uppercase()))
            }
            if (response.status == HttpStatusCode.OK) {
                val body = response.bodyAsText()
                body.substringAfter("<accesoLoginResult>").substringBefore("</accesoLoginResult>")
                    .replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"")
            } else null
        } catch (e: Exception) { null }
                header("Host", "sicenet.surguanajuato.tecnm.mx")
                header("SOAPAction", "\"http://tempuri.org/accesoLogin\"")
                // El servidor espera ALUMNO o DOCENTE en mayúsculas según el esquema SOAP
                val body = SoapRequestBuilder.buildLoginBody(matricula, contrasenia, tipo.uppercase())
                setBody(body)
            }
            
            val responseBody = response.bodyAsText()
            if (response.status == HttpStatusCode.OK) {
                // Extraer el JSON del tag XML <accesoLoginResult>
                if (responseBody.contains("<accesoLoginResult>")) {
                    responseBody.substringAfter("<accesoLoginResult>").substringBefore("</accesoLoginResult>")
                        .replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"")
                } else {
                    responseBody
                }
            } else {
                println("DEBUG_LOGIN_HTTP_ERROR: ${response.status}")
                println("DEBUG_LOGIN_ERROR_BODY: $responseBody")
                null
            }
        } catch (e: Exception) {
            println("DEBUG_LOGIN_EXCEPTION: ${e.message}")
            null
        }
    }

    suspend fun getProfile(): String? {
        return try {
            val response = client.post("https://sicenet.surguanajuato.tecnm.mx/ws/wsalumnos.asmx") {
                header("Content-Type", "text/xml; charset=utf-8")
                header("SOAPAction", "\"http://tempuri.org/getAlumnoAcademicoWithLineamiento\"")
                setBody(SoapRequestBuilder.buildProfileBody())
            }
            if (response.status == HttpStatusCode.OK) {
                val body = response.bodyAsText()
                body.substringAfter("<getAlumnoAcademicoWithLineamientoResult>")
                    .substringBefore("</getAlumnoAcademicoWithLineamientoResult>")
                    .replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"")
            } else null
        } catch (e: Exception) { null }
    }

    suspend fun getCargaAcademica(): String? {
        return try {
            val response = client.post("https://sicenet.surguanajuato.tecnm.mx/ws/wsalumnos.asmx") {
                header("Content-Type", "text/xml; charset=utf-8")
                header("SOAPAction", "\"http://tempuri.org/getCargaAcademicaByAlumno\"")
                setBody(SoapRequestBuilder.buildCargaBody())
            }
            if (response.status == HttpStatusCode.OK) {
                val body = response.bodyAsText()
                body.substringAfter("<getCargaAcademicaByAlumnoResult>")
                    .substringBefore("</getCargaAcademicaByAlumnoResult>")
                    .replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"")
            } else null
        } catch (e: Exception) { null }
                header("Host", "sicenet.surguanajuato.tecnm.mx")
                header("SOAPAction", "\"http://tempuri.org/getAlumnoAcademicoWithLineamiento\"")
                setBody(SoapRequestBuilder.buildProfileBody())
            }
            val responseBody = response.bodyAsText()
            if (response.status == HttpStatusCode.OK) {
                if (responseBody.contains("<getAlumnoAcademicoWithLineamientoResult>")) {
                    responseBody.substringAfter("<getAlumnoAcademicoWithLineamientoResult>")
                        .substringBefore("</getAlumnoAcademicoWithLineamientoResult>")
                        .replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"")
                } else {
                    responseBody
                }
            } else {
                println("DEBUG_PROFILE_ERROR_BODY: $responseBody")
                null
            }
        } catch (e: Exception) {
            println("DEBUG_PROFILE_EXCEPTION: ${e.message}")
            null
        }
    }
}
