package com.gestion.biblioteca.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    //Clave secreta para firmar y verificar el token. Es la del properties
    @Value("${jwt.secret}")
    private String secretString;
    private Key secretKey;

    //Obtiene clave secreta. La que se coloca en properties
    private Key getSigningKey() {
        if (secretKey == null) {
            secretKey = Keys.hmacShaKeyFor(secretString.getBytes());
        }
        return secretKey;
    }


    //Genera Token
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        return createToken(claims, userDetails.getUsername());
    }

    //Crea Token con Claims y Sujeto
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims) //Carga útil (claims)
                .setSubject(subject) //Quién es el token (username/DNI)
                .setIssuedAt(new Date(System.currentTimeMillis())) //Fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) //Expira en 10 horas
                .signWith(getSigningKey()) //Firma el token con la clave secreta
                .compact(); //Compacta el JWT en su formato String final
    }


    //Verifica si el token es válido para un usuario dado y si no ha expirado
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    //Extraer Username (DNI) del Token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //Extraer Fecha de Expiración del Token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //Extraer un Claim específico
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //Extraer Todos los Claims
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //Verificar si el Token ha Expirado
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
