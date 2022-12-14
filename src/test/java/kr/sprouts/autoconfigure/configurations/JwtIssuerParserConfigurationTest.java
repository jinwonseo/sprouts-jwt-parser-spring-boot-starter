package kr.sprouts.autoconfigure.configurations;

import kr.sprouts.autoconfigure.components.JwtHelper;
import kr.sprouts.autoconfigure.components.JwtParser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class JwtIssuerParserConfigurationTest {
    private final ApplicationContextRunner applicationContextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(JwtHelperConfiguration.class))
            .withConfiguration(AutoConfigurations.of(JwtParserConfiguration.class));

    @Test
    public void bean_test() {
        this.applicationContextRunner.run(
                context -> {
                    assertThat(context).hasSingleBean(JwtHelperConfiguration.class);
                    assertThat(context).hasSingleBean(JwtHelper.class);
                    assertThat(context).hasSingleBean(JwtParserConfiguration.class);
                    assertThat(context).hasSingleBean(JwtParser.class);
                }
        );
    }
}