package com.example.sicedroidmultiplatform.data.remote

object SoapRequestBuilder {

    fun buildLoginBody(matricula: String, contrasenia: String, tipoUsuario: String): String {
        // XML sin espacios en blanco innecesarios para máxima compatibilidad con servidores .NET antiguos
        return """<soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><accesoLogin xmlns="http://tempuri.org/"><strMatricula>$matricula</strMatricula><strContrasenia>$contrasenia</strContrasenia><tipoUsuario>$tipoUsuario</tipoUsuario></accesoLogin></soap:Body></soap:Envelope>"""
    }

    fun buildProfileBody(): String {
        return """<?xml version="1.0" encoding="utf-8"?><soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><getAlumnoAcademicoWithLineamiento xmlns="http://tempuri.org/" /></soap:Body></soap:Envelope>"""
    }

    fun buildCargaBody(): String {
        return """<?xml version="1.0" encoding="utf-8"?><soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><getCargaAcademicaByAlumno xmlns="http://tempuri.org/" /></soap:Body></soap:Envelope>"""
    }
}
