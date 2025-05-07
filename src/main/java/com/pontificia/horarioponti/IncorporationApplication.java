package com.pontificia.horarioponti;

import com.pontificia.horarioponti.utils.jwt.filter.JwtFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class IncorporationApplication {

    public static void main(String[] args) {
        SpringApplication.run(IncorporationApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilterBean(JwtFilter jwtFilter) {
        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtFilter);
        registrationBean.addUrlPatterns("/api/protected/*");
        return registrationBean;
    }
}
