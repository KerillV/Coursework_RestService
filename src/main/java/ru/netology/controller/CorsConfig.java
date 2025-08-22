package ru.netology.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);              // разрешаем куки
        config.addAllowedOrigin("https://serp");       // фронт-домен
        config.addAllowedHeader("*");                  // разрешаем все заголовки
        config.addAllowedMethod("*");                  // разрешаем все методы (GET, POST...)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);  // применяем ко всем эндпоинтам
        return new CorsFilter(source);
    }
}
