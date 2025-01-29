package io.github.mateus81.mensagensapi.util;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


// Classe utilitária JWT
@Component
public class JwtUtil {

	private final Key key;
	private final long expirationTime;
	
	public JwtUtil(@Value("${jwt.secret}")String secretKey, @Value("${jwt.expiration}")long expirationTime) {
		if(secretKey.getBytes().length < 32) {
			throw new IllegalArgumentException("A chave secreta deve ter pelo menos 32 bytes");
		}
		this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
		this.expirationTime = expirationTime;
	}
	
	public String generateToken(String username) {
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+ expirationTime))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}
	
	public String extractUsername(String token) {
		try {
			return getClaims(token).getSubject();
		} catch(ExpiredJwtException e) {
			return e.getClaims().getSubject();
		}
	}
	
	public boolean validateToken(String token, UserDetails userDetails) {
		String extractedUsername = extractUsername(token);
		return (userDetails.getUsername().equals(extractedUsername) && !isTokenExpired(token));
	}
	
	private Claims getClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}
	
	private boolean isTokenExpired(String token){
		try {
			return getClaims(token).getExpiration().before(new Date());
		} catch(ExpiredJwtException e) {
			return true;
		}
	}
	
	public String refreshAccessToken(String refreshToken) throws Exception {
		try {
			String username = extractUsername(refreshToken);
			return generateToken(username);
		} catch(ExpiredJwtException e) {
			String username = e.getClaims().getSubject();
			return generateToken(username);
		} catch(Exception ex) {
			throw new Exception("Refresh token is invalid or expired");
		}
    }
}
