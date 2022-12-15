package kr.sprouts.autoconfigure.utilities;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.lang.NonNull;

import java.security.Key;

public class JwtParser {

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
                JwtHelper.convertToSecretKey(base64UrlEncodedSecret),
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

    public static Claims claims(@NonNull String base64UrlEncodedSecret, @NonNull String claimsJws) {
        return JwtParser.claims(
                JwtHelper.convertToSecretKey(base64UrlEncodedSecret),
                claimsJws
        );
    }
}
