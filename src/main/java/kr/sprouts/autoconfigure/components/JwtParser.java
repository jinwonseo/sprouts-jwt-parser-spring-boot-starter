package kr.sprouts.autoconfigure.components;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.security.Key;

public class JwtParser {
    private final Logger logger = LoggerFactory.getLogger(JwtParser.class);

    public JwtParser() {
        this.logger.info(String.format("Initialized %s", JwtParser.class.getName()));
    }

    public static Claims claims(@NonNull String requireIssuer, @NonNull String requireSubject, @NonNull String requireAudience, @NonNull Key key, @NonNull String claimsJws) {
        return Jwts.parserBuilder()
                .requireIssuer(requireIssuer)
                .requireSubject(requireSubject)
                .requireAudience(requireAudience)
                .setSigningKey(key)
                .build()
                .parseClaimsJws(claimsJws)
                .getBody();
    }

    public static Claims claims(@NonNull String requireIssuer, @NonNull String requireSubject, @NonNull String requireAudience, @NonNull String base64UrlEncodedSecret, @NonNull String claimsJws) {
        return JwtParser.claims(
                requireIssuer,
                requireSubject,
                requireAudience,
                JwtHelper.convertToKey(base64UrlEncodedSecret),
                claimsJws
        );
    }

    public static Claims claims(@NonNull Key key, @NonNull String claimsJws) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(claimsJws)
                .getBody();
    }

    public static Claims claims(@NonNull String base64UrlEncodedSecret, String claimsJws) {
        return JwtParser.claims(
                JwtHelper.convertToKey(base64UrlEncodedSecret),
                claimsJws
        );
    }
}
