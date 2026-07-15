package com.param.security.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.param.global.dto.UserMasterDto;
import com.param.global.utility.UserComponent;

import io.jsonwebtoken.*;

public class JwtTokenUtil {
	private static final String SECRET_KEY = "CliniVantage##2017";
	private static final long JWT_EXPIRATION = 8 * 60 * 60 * 1000; //valid for 24 hrs
	private static final long JWT_EXPIRATION_EXTERNAL_CALLS = 5 * 60 * 60 * 1000; //valid for 5 hrs for external calls
	private static final String userForOthers = "ClinivantageAdminUser"; 
	public static String generateToken(String username, Integer unitId) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, username, unitId);
	}
	
	public static String generateToken(Integer unitId) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, unitId);
	}

	private static String createToken(Map<String, Object> claims, String username, Integer unitId) {
		return Jwts.builder()
				.setClaims(claims)
				.claim("unitId", unitId)
				.setSubject(username.toLowerCase())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY)
				.compact();
	}
	
	private static String createToken(Map<String, Object> claims, Integer unitId) {
		return Jwts.builder()
				.setClaims(claims)
				.claim("unitId", unitId)
				.setSubject(userForOthers.toLowerCase())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_EXTERNAL_CALLS))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY)
				.compact();
	}

	public static boolean validateToken(String token,Integer userId, Integer unitId){
		
		String username = JwtTokenUtil.extractUsername(token);
		Integer tokenUnitId = JwtTokenUtil.extractUnitId(token);
		UserMasterDto masterDto = userId!=null ? UserComponent.getUsers().stream().filter(user->user.getUserId().equals(userId)).findAny().orElse(null) : null;
        if(username.equals(userForOthers.toLowerCase())){
        	return (tokenUnitId.equals(unitId) && !isTokenExpired(token)) ? true : false;
        }else if (masterDto == null) {
        	return false; 
        }else if(!(username.equals(masterDto.getUserName().toLowerCase()) && tokenUnitId.equals(unitId))){
        	return false;
        }else{
        	return (username.equals(masterDto.getUserName().toLowerCase()) && tokenUnitId.equals(unitId) && !isTokenExpired(token)) ? true : false;
        }
	}

	public static String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public static Integer extractUnitId(String token) {
		final Claims claims = extractAllClaims(token);
		return (Integer) claims.get("unitId");
    }

	public static Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private static Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	private static Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	public static void main(String[] args) {
		String token = generateToken(1);
		System.out.println(validateToken(token,null,1));
	}
	
	
}
