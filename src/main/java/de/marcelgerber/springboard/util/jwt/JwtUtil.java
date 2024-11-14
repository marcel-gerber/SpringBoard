package de.marcelgerber.springboard.util.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Class providing methods for JWT-authentication
 */
public class JwtUtil {

    private static final MacAlgorithm ALGORITHM = Jwts.SIG.HS512;
    private static final SecretKey SECRET_KEY = ALGORITHM.key().build();

    // 1 hour in milliseconds
    private static final long EXPIRATION_TIME = 3600000;

    /**
     * Generates a new token for the provided username
     *
     * @param playerId String
     * @return JWT as String
     */
    public static String generateToken(String playerId) {
        return Jwts.builder()
                .subject(playerId)
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, ALGORITHM)
                .compact();
    }

    /**
     * Returns 'true' when the provided token is valid for the provided username
     *
     * @param token JWT as String
     * @param playerId String
     * @return String
     */
    public static boolean isTokenValid(String token, String playerId) {
        Claims claims = getClaims(token);
        return claims.getSubject().equals(playerId) && !claims.getExpiration().before(new Date());
    }

    /**
     * Returns the subject of the provided token
     *
     * @param token JWT as String
     * @return String
     */
    public static String getSubject(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Returns all Claims of the provided token
     *
     * @param token JWT as String
     * @return Claims
     */
    private static Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
