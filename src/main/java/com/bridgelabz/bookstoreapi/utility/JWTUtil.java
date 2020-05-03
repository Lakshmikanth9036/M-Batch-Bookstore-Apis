package com.bridgelabz.bookstoreapi.utility;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTUtil {
	
	@Value("${secret}")
	private String SECRET;

	private final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

	public String generateToken(Long id, Token expiration) {
		if (expiration.equals(Token.WITH_EXPIRE_TIME)) {
			return Jwts.builder().setSubject(String.valueOf(id))
					.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
					.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		} else {
			return Jwts.builder().setSubject(String.valueOf(id)).signWith(SignatureAlgorithm.HS512, SECRET).compact();
		}
	}

	public Long decodeToken(String jwt)  {
		Claims claim = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(jwt).getBody();
		Long id = Long.parseLong(claim.getSubject());
		return id;
	}
}