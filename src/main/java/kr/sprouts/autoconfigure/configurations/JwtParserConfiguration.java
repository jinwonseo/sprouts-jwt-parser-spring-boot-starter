package kr.sprouts.autoconfigure.configurations;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtParserConfiguration {

    public JwtParserConfiguration() {
        LoggerFactory.getLogger(JwtParserConfiguration.class)
                .info(String.format("Initialized %s", JwtParserConfiguration.class.getName()));
    }
}
