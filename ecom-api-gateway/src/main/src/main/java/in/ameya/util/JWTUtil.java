package in.ameya.util;

import java.util.Date;
import java.util.HashMap;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import in.ameya.securities.service.EncryptionService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


@Component
public class JWTUtil {

	private static final HashMap<Integer, String> activeJwts = new HashMap<Integer, String>();
	private static final String KEY = "COBAPI@#AMEYA@2022$2023$2024";
	private final SecretKey SECRET_KEY;
	
	@Autowired
	public JWTUtil(EncryptionService encryptor) {
		StringBuilder sb = new StringBuilder(KEY);
		try {
			sb.append(encryptor.encrypt(KEY));
			sb.append(getClass().getPackage().getImplementationVersion());
		} catch (Exception e) {
			sb.append(java.util.Base64.getEncoder().encodeToString(KEY.getBytes()));
		}
		SECRET_KEY = Keys.hmacShaKeyFor(sb.toString().getBytes());
	}
	
	
    public String extractUsername(String token) {
    	try {
    		Claims body = getClaim(token);
    		return body.getSubject();
		} catch (ExpiredJwtException e) {
			return null;
		}
    }

	private Claims getClaim(String token) {
		Claims body = Jwts
				.parser()
				.verifyWith(SECRET_KEY)
				.build()
				.parseSignedClaims(token)
				.getPayload();
		return body;
	}

    public Date extractExpiration(String token) 
    {
    	Claims body = getClaim(token);
    	return body.getExpiration();
    }

    private Boolean isTokenExpired(String token) 
    {
        return extractExpiration(token).before(new Date());
    }


    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

	public String generateToken(UserDetails userDetails, String ipAddress) {
		return createToken(userDetails.getUsername(), ipAddress);
	}
	
	private String createToken(String subject, String ipAddress) {
        return Jwts
        		.builder()
        		.subject(subject)
        		.issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000)) // 1 Hour
                .audience().add(ipAddress).and()
                .signWith(SECRET_KEY, Jwts.SIG.HS512)
                .compact();
	}

	public Boolean checkAudience(String token, String ipAddress) {
    	Claims body = getClaim(token);
    	return body.getAudience().contains(ipAddress);
    }
	
    public Boolean validateToken(String token, UserDetails userDetails, String ipAddress) {
        return (
        		extractUsername(token).equals(userDetails.getUsername()) 
        		&& checkAudience(token, ipAddress)
        		&& !isTokenExpired(token)
        		);
    }
    
    
    private class N15Clock implements Clock {
		@Override
		public Date now() {
			Date n15 = new Date();
    		n15.setTime(n15.getTime() + 15);
    		return n15;
		}
    }
    
    public Boolean verifyExpiryN15(String token) {
    	try {
    		Jwts.parser().clockSkewSeconds(0).clock(new N15Clock()).build()
    		.parse(token.substring(0, token.lastIndexOf(".") + 1));
    		
			return true;
		} catch (Exception e) {
			return false;
		}
    }
    
    public String setToken(Integer userId, String token) {
    	if(token == null) {
    		return activeJwts.remove(userId);
    	}
    	
    	return activeJwts.put(userId, token);
    }
    
    public String getToken(Integer userId) {
    	String token = activeJwts.get(userId);
    	if(token == null || isTokenExpired(token)) {
    		activeJwts.remove(userId);
    		return null;
    	}
    	return token;
    }
    
    public Boolean checkActive(Integer userId, String token) {
    	String currentToken = activeJwts.get(userId);
    	return token.equalsIgnoreCase(currentToken);
    }

}
