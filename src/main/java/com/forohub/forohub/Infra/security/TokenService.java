package com.forohub.forohub.Infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.forohub.forohub.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${API_SECURITY_SECRET}")
    private String apiSecret;

    /**
     * Genera un token JWT para el usuario.
     *
     * @param usuario El usuario para el que se generará el token.
     * @return El token JWT generado.
     * @throws IllegalArgumentException Si el usuario o sus datos son nulos.
     * @throws RuntimeException         Si hay un error al crear el token.
     */
    public String generarToken(Usuario usuario) {
        if (usuario == null || usuario.getLogin() == null || usuario.getId() == null) {
            throw new IllegalArgumentException("El usuario y sus datos no pueden ser nulos");
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            return JWT.create()
                    .withIssuer("forohub")
                    .withSubject(usuario.getLogin())
                    .withClaim("id", usuario.getId())
                    .withExpiresAt(generarFechaExpiracion(5))  // Expira en 5 horas
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error al generar el token: " + exception.getMessage(), exception);
        }
    }

    /**
     * Obtiene el subject (login) del token JWT.
     *
     * @param token El token JWT a verificar.
     * @return El subject (login) extraído del token.
     * @throws IllegalArgumentException Si el token es nulo o vacío.
     * @throws RuntimeException         Si hay un error al verificar el token.
     */
    public String getSubject(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("El token no puede ser nulo o vacío");
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .withIssuer("forohub")
                    .build()
                    .verify(token);

            String subject = decodedJWT.getSubject();
            if (subject == null) {
                throw new RuntimeException("El token no contiene un subject válido");
            }
            return subject;
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Error al verificar el token: " + exception.getMessage(), exception);
        }
    }

    /**
     * Genera la fecha de expiración para el token.
     *
     * @param horas Número de horas que el token será válido.
     * @return La fecha de expiración en formato Instant.
     */
    private Instant generarFechaExpiracion(int horas) {
        return LocalDateTime.now().plusHours(horas).atZone(ZoneId.of("America/New_York")).toInstant();
    }
}
