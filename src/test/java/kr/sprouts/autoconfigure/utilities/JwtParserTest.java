package kr.sprouts.autoconfigure.utilities;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.security.PrivateKey;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtParserTest {

    private static String create(Claims claims, Key key, SignatureAlgorithm signatureAlgorithm) {
        return Jwts.builder().setClaims(claims).signWith(key, signatureAlgorithm).compact();
    }

    private static String create(Claims claims, String base64UrlEncodedSecret, SignatureAlgorithm signatureAlgorithm) {
        return Jwts.builder().setClaims(claims).signWith(JwtHelper.convertToSecretKey(base64UrlEncodedSecret), signatureAlgorithm).compact();
    }

    @Test
    void parse_hs512() {
        String id = UUID.randomUUID().toString();
        String issuer = UUID.randomUUID().toString();
        String subject = UUID.randomUUID().toString();
        String audience = UUID.randomUUID().toString();
        long validityInSeconds = 60L;

        LocalDateTime currentLocalDateTime = LocalDateTime.now();

        Claims claims = Jwts.claims();
        claims.setId(id);
        claims.setIssuer(issuer);
        claims.setSubject(subject);
        claims.setAudience(audience);
        claims.setNotBefore(Timestamp.valueOf(currentLocalDateTime));
        claims.setIssuedAt(Timestamp.valueOf(currentLocalDateTime));
        claims.setExpiration(Timestamp.valueOf(currentLocalDateTime.plusSeconds(validityInSeconds)));

        String base64UrlEncodedSecret = JwtHelper.base64urlEncodedSecretKeyFor(SignatureAlgorithm.HS512).value();

        assertThat(JwtParser.claims(base64UrlEncodedSecret, JwtParserTest.create(claims, base64UrlEncodedSecret, SignatureAlgorithm.HS512)).getId().equals(claims.getId())).isTrue();
    }

    @Test
    void parse_rs256() {
        String id = UUID.randomUUID().toString();
        String issuer = UUID.randomUUID().toString();
        String subject = UUID.randomUUID().toString();
        String audience = UUID.randomUUID().toString();
        long validityInSeconds = 60L;

        LocalDateTime currentLocalDateTime = LocalDateTime.now();

        Claims claims = Jwts.claims();
        claims.setId(id);
        claims.setIssuer(issuer);
        claims.setSubject(subject);
        claims.setAudience(audience);
        claims.setNotBefore(Timestamp.valueOf(currentLocalDateTime));
        claims.setIssuedAt(Timestamp.valueOf(currentLocalDateTime));
        claims.setExpiration(Timestamp.valueOf(currentLocalDateTime.plusSeconds(validityInSeconds)));

        PrivateKey privateKey = JwtHelper.keyPairFor(SignatureAlgorithm.RS256).getPrivate();

        assertThat(JwtParser.claims(privateKey, JwtParserTest.create(claims, privateKey, SignatureAlgorithm.RS256)).getId().equals(claims.getId())).isTrue();
    }

    @Test
    void invalid_jws() {
        String id = UUID.randomUUID().toString();
        String issuer = UUID.randomUUID().toString();
        String subject = UUID.randomUUID().toString();
        String audience = UUID.randomUUID().toString();
        long validityInSeconds = 1L;

        LocalDateTime currentLocalDateTime = LocalDateTime.now();

        Claims claims = Jwts.claims();
        claims.setId(id);
        claims.setIssuer(issuer);
        claims.setSubject(subject);
        claims.setAudience(audience);
        claims.setNotBefore(Timestamp.valueOf(currentLocalDateTime));
        claims.setIssuedAt(Timestamp.valueOf(currentLocalDateTime));
        claims.setExpiration(Timestamp.valueOf(currentLocalDateTime.plusSeconds(validityInSeconds)));

        PrivateKey privateKey = JwtHelper.keyPairFor(SignatureAlgorithm.RS256).getPrivate();

        assertThatThrownBy(() -> {
            Thread.sleep(validityInSeconds * 2000);
            JwtParser.claims(privateKey, JwtParserTest.create(claims, privateKey, SignatureAlgorithm.RS256));
        }).isInstanceOf(ExpiredJwtException.class);
    }
}