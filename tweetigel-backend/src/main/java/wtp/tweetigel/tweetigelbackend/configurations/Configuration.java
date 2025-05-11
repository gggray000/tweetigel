package wtp.tweetigel.tweetigelbackend.configurations;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@org.springframework.context.annotation.Configuration
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        name = "basicAuth"
        ,
        scheme = "basic")
public class Configuration {
}
