package com.backendIntegrador.jwt;

import com.backendIntegrador.model.Client;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "586E3272357538782F413F4428472B4B6250655368566B597033733676397924";

    public String getToken( Client user ) {
        // Llama a la versión sobrecargada de getToken con claims vacíos y el UserDetails del usuario
        return getToken(new HashMap<>(), user);
    }

    // Método para generar un token JWT con claims adicionales y detalles del usuario
    private String getToken( Map<String, Object> extraClaims, Client user ) {


        // Obtener la fecha y hora actual en UTC
        LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("UTC"));

        // Agregar 7 días a la fecha actual para definir la expiración del token
        LocalDateTime expirationTime = currentTime.plus(7, ChronoUnit.DAYS);


        extraClaims.put("role", user.getAuthorities());
        extraClaims.put("clientName", user.getClientName());
        //extraClaims.put("isVerified", user.getIsVerified());

        return Jwts.builder()
                .setClaims(extraClaims)  // Agregar claims adicionales (si los hay)
                .setSubject(user.getEmail())  // Establecer el email de usuario como el "subject" del token
                .setIssuedAt(java.sql.Timestamp.valueOf(currentTime))  // Establecer la fecha y hora de emisión del token
                .setExpiration(java.sql.Timestamp.valueOf(expirationTime))  // Establecer la fecha y hora de expiración del token
                .signWith(getKey(), SignatureAlgorithm.HS256)  // Firmar el token utilizando una clave
                .compact();
    }

    // Método para obtener la clave utilizada para firmar el token
    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public String getEmailFromToken( String token ) {
        return getClaim(token, Claims::getSubject);
    }



    public Claims getAllClaims( String token ) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaim( String token, Function<Claims, T> claimsResolver ) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private LocalDateTime getExpiration( String token ) {
        Date expirationDate = getClaim(token, Claims::getExpiration);
        Instant instant = expirationDate.toInstant();
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = getEmailFromToken(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired( String token ) {
        return getExpiration(token).isBefore(LocalDateTime.now(ZoneId.of("UTC")));
    }

}
