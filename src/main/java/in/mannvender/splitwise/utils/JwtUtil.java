package in.mannvender.splitwise.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(JwtUtil.class);
    private final SecretKey secretKey;

    public JwtUtil(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

//    Map<String, Object> claims = new HashMap<>();
//        claims.put("user_id", user.getId());
//        claims.put("email", user.getEmail());
//        claims.put("roles", user.getRoles());
//    long timeInMillis = System.currentTimeMillis();
//        claims.put("iat", timeInMillis);
//        claims.put("exp", timeInMillis + 1000L * 60 * 60 * 24 * 365); // 1 year
//        claims.put("iss", "split_wiser");
//    String token = Jwts.builder().claims(claims).signWith(secretKey).compact();
    public Long extractUserId(String token){
        Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getBody();
        logger.info("Claims: " + claims);
        return Long.parseLong(claims.get("user_id").toString());
    }

    public Long extractExp(String token){
        Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getBody();
        return Long.parseLong(claims.get("exp").toString());
    }
}
