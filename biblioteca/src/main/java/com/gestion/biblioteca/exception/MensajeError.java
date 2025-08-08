package com.gestion.biblioteca.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MensajeError {

    LIBRO_NOT_FOUND("El libro no ha sido encontrado", HttpStatus.NOT_FOUND),
    LIBRO_EXISTING("El libro ya existe en la base de datos", HttpStatus.BAD_REQUEST),
    LIBRO_NOT_FOUND_GOOGLE("EL libro no existe en google books", HttpStatus.NOT_FOUND),
    AUTOR_NOT_FOUND("El autor no existe en la base de datos", HttpStatus.NOT_FOUND),
    VOL_INFO("El volumInfo no puede ser null", HttpStatus.BAD_REQUEST),
    USUARIO_NOT_FOUND("El usuario no se encuentra en la base de datos", HttpStatus.NOT_FOUND),
    USUARIO_EXISTING("El usuario ya existe en la base de datos", HttpStatus.BAD_REQUEST),
    PRESTAMO_NOT_FOUND("El prestamo no existe", HttpStatus.NOT_FOUND),
    AUTOR_EXISTING("El autor ya existe", HttpStatus.BAD_REQUEST),
    LIBRO_NOT_AVAILABLE("El libro no se encuentra disponible", HttpStatus.NOT_FOUND),
    USUARIO_ES_DEUDOR("El usuario es deudor", HttpStatus.BAD_REQUEST ),
    AUTHENTICATION_ERROR("Error al intentar autenticar usuario", HttpStatus.UNAUTHORIZED),
    DIAS_ICORRECTOS("Los días de retraso deben ser un número positivo para crear una multa", HttpStatus.BAD_REQUEST),
    MULTA_NOT_FOUND("La multa no existe en la base de datos", HttpStatus.NOT_FOUND),
    MULTA_ALREADY_PAID("Esta multa ya ha sido pagada", HttpStatus.BAD_REQUEST),
    LIBRO_YA_DEVUELTO("El libro ya ha sido devuelto", HttpStatus.BAD_REQUEST );




    private final String mensaje;
    private final HttpStatus codigo;
}
