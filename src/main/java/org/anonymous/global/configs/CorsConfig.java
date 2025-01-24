package org.anonymous.global.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

/**
 * Cross Origin Resource Sharing 정책
 *
 * 다른 출처간의 자원의 공유 정책
 */
@Configuration
public class CorsConfig {

    @Value("${cors.allowed}")
    private String allowedOrigin;

    @Bean
    public CorsFilter corsFilter() {

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedMethod("*");
        config.addAllowedHeader("*");

        if (StringUtils.hasText(allowedOrigin)) {

            List<String> origins = Arrays.stream(allowedOrigin.split(",")).toList();

            config.setAllowedOrigins(origins);
            config.setAllowCredentials(true);

        } else {

            config.addAllowedOrigin("*");
        }



        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}