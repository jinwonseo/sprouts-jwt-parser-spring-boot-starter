package kr.sprouts.autoconfigure.components;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kr.sprouts.autoconfigure.configurations.JwtParserConfiguration;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.security.Key;
import java.security.PrivateKey;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtParserTest {
    private final ApplicationContextRunner applicationContextRunner
            = new ApplicationContextRunner().withConfiguration(AutoConfigurations.of(JwtParserConfiguration.class));

    private final Logger logger = LoggerFactory.getLogger(JwtParserTest.class);

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

        String base64UrlEncodedSecret = JwtHelper.base64urlEncodedSecretKeyFor(SignatureAlgorithm.HS512).getValue();
        Key key = JwtHelper.convertToKey(base64UrlEncodedSecret);

        String claimsJws = JwtCreator.create(claims, key, SignatureAlgorithm.HS512);
        Claims parsedClaims =  JwtParser.claims(key, claimsJws);

        logger.info(base64UrlEncodedSecret);
        logger.info(claimsJws);

        assertThat(claims.getId().equals(parsedClaims.getId())).isTrue();
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
        String claimsJws = JwtCreator.create(claims, privateKey, SignatureAlgorithm.RS256);

        assertThat(claims.getId().equals(JwtParser.claims(privateKey, claimsJws).getId())).isTrue();
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
        String claimsJws = JwtCreator.create(claims, privateKey, SignatureAlgorithm.RS256);

        try {
            Thread.sleep(validityInSeconds * 2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertThatThrownBy(() -> JwtParser.claims(privateKey, claimsJws)).isInstanceOf(ExpiredJwtException.class);
    }
}