package org.anonymous.global.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger API 설정
 *
 * - API 문서 자동 생성 도구
 *
 */
// 제목 & 설명
@Configuration
@OpenAPIDefinition(info = @Info(title = "카드 API", description = "카드 API 제공"))
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi openApiGroup() {

        return GroupedOpenApi.builder()
                .group("카드 API v1") // Group 이름 -> group("설명")
                .pathsToMatch("/**") // 경로 패턴 지정 (api 문서에 속하는 모든 경로)
                .build();
    }
}