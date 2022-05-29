package com.login.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtUtil {

	private String SECRET_KEY = "secret";

	public String extractUsername(String token) {
		log.info("af) util -> JwtUtil extract username token :" + token);
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		log.info("ag) util -> JwtUtil extract expiration token :" + token);
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		log.info("ag) util -> JwtUtil extract claims token :" + token + " claims :" + claims);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		log.info("ah) util -> JwtUtil extract all claims token :" + token);
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		log.info("ai) util -> JwtUtil isTokenEpired token :" + token);
		return extractExpiration(token).before(new Date());
	}

	public String generateToken(UserDetails userDetails) {
		log.info("aj) util -> JwtUtil generateToken userDetails : " + userDetails);
		Map<String, Object> claims = new HashMap<>();
		claims.put("rights", userDetails.getAuthorities());
		log.info("ak) util -> JwtUtil generateToken userDetails : " + userDetails + " and claims : " + claims);
		return createToken(claims, userDetails.getUsername());
	}

	private String createToken(Map<String, Object> claims, String subject) {
		log.info("al) util -> JwtUtil createToken subject : " + subject + " and claims : " + claims);
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		log.info("am) util -> JwtUtil validateToken token : " + token + " username : " + username
				+ " and userDetails : " + userDetails);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}