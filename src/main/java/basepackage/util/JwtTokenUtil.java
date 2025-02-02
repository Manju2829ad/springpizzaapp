package basepackage.util;

import java.util.Base64;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {

    private static final String BASE64_SECRET_KEY = "g57+Dnw6c1+4kZb9H5yHm2CojxMzGKk+dIVyUep0KZCUN+EUwTZfPQl1ko5K5DAGIs2PBk6JjWI++qezr6rqgw=="; // Replace with your actual secret key

    private final String secret;
    private final long expirationTime;

    public JwtTokenUtil(@Value("${jwt.secret}") String secret,
                        @Value("${jwt.expiration.time:86400000}") // Default to 24 hours
                        long expirationTime) {
        this.secret = secret; // Direct secret without encoding, as we're using it for HMAC
        this.expirationTime = expirationTime;
    }

    public String generateTokenUsingEmail(String email) {
        return generateToken(email);
    }

    public String generateTokenUsingMobile(String mobileNo) {
        return generateToken(mobileNo);
    }

    private String generateToken(String identifier) {
        return Jwts.builder()
                .setSubject(identifier)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, createHmacKey())  // Using HMAC for signing
                .compact();
    }

    private SecretKeySpec createHmacKey() {
        return new SecretKeySpec(secret.getBytes(), "HmacSHA256");
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(createHmacKey())  // Use HMAC key for validation
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;  // If parsing fails, token is invalid
        }
    }
    
    public SecretKey getSecretKey() {
        byte[] decodedKey = Base64.getDecoder().decode(BASE64_SECRET_KEY);
        return new SecretKeySpec(decodedKey, "HmacSHA256");
    }
}
