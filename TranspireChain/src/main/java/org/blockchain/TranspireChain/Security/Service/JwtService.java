package org.blockchain.TranspireChain.Security.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.blockchain.TranspireChain.Security.Model.MyUserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String secret = "bfzymUZLzF6r6AoEHR7E9TzoogbExS0D4ZXHmLqSayUJIf2xw18BntBHYoQvqJjnNZpBbjTdgz94tN5BIQxJjuWjLqb5sh4x";
    public String createToken(
            Long userId,
            String username,
            String email,
            String phoneNumber,
            String address){
        Claims claims = Jwts.claims()
                .subject(email)
                .add("userId", userId)
                .add("username", username)
                .add("phoneNumber", phoneNumber)
                .add("address", address)
                .build();
        return generateToken(claims);
    }
    public String createToken(
            Long memberId,
            String username,
            String email,
            String phoneNumber,
            String address,
            Long departmentId,
            String departmentName){
        Claims claims = Jwts.claims()
                .subject(email)
                .add("memberId", memberId)
                .add("username", username)
                .add("phoneNumber", phoneNumber)
                .add("address", address)
                .add("departmentId", departmentId)
                .add("departmentName", departmentName)
                .build();
        return generateToken(claims);
    }

    private String generateToken(Claims claims) {
        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + getExpiration()))
                .signWith(getSecretKey())
                .compact();
    }

//    public Map<String, String> getFromToken(String token){
//        Claims claims =extractAllClaim(token.substring(7));
//        Map<String, String> claimsMap = new HashMap<>();
//        claimsMap.put("email", String.valueOf(claims.getSubject()));
//
//        return claimsMap;
//    }

    public int getExpiration() {
        return (1000 * 60 * 60 * 24);
    }

    public static SecretKey getSecretKey(){
        byte[] secretBytes =secret.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(secretBytes, 0, secretBytes.length, "HmacSHA256");
    }
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaim(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaim(String token){
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, MyUserDetails userDetails) {
        System.out.println(userDetails.getEmail());
        return (userDetails.getEmail().equals(extractUsername(token))); //&& isTokenExpired(token));
    }
}
