package kr.sprouts.autoconfigure.configurations;

import kr.sprouts.autoconfigure.components.JwtParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtParserConfiguration {
    private final Logger logger = LoggerFactory.getLogger(JwtParserConfiguration.class);

    public JwtParserConfiguration() {
        this.logger.info(String.format("Initialized %s", JwtParserConfiguration.class.getName()));
    }

    @Bean
    public JwtParser jwtParser() {
        return new JwtParser();
    }
}
