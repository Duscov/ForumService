package cohort_65.java.forumservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(authorize -> authorize
                // Открытые маршруты
                .requestMatchers(HttpMethod.GET, "/posts").permitAll()
                .requestMatchers(HttpMethod.POST, "/posts").permitAll()
                .requestMatchers("/account/register").permitAll()

                // Доступ только владельцу
                .requestMatchers(HttpMethod.PUT, "/user/{login}")
                .access(new WebExpressionAuthorizationManager("authentication.name == #login"))

                .requestMatchers(HttpMethod.PUT, "/password")
                .access(new WebExpressionAuthorizationManager("authentication.name == #login"))

                // Управление ролями — только администратор
                .requestMatchers(HttpMethod.PUT, "/user/{login}/role/{role}")
                .hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/user/{login}/role/{role}")
                .hasRole("ADMIN")

                // Создание поста — только если login == author
                .requestMatchers(HttpMethod.POST, "/post/{author}")
                .access(new WebExpressionAuthorizationManager("authentication.name == #author"))

                // Редактирование и удаление поста — владелец или модератор
                .requestMatchers(HttpMethod.PUT, "/post/{id}")
                .access(new WebExpressionAuthorizationManager("hasRole('MODERATOR') or @postSecurity.isOwner(authentication, #id)"))

                .requestMatchers(HttpMethod.DELETE, "/post/{id}")
                .access(new WebExpressionAuthorizationManager("hasRole('MODERATOR') or @postSecurity.isOwner(authentication, #id)"))

                // Комментарий — login == author
                .requestMatchers(HttpMethod.POST, "/post/{id}/comment/{author}")
                .access(new WebExpressionAuthorizationManager("authentication.name == #author"))

                // Остальные — просто аутентификация
                .anyRequest().authenticated()
        );
        return http.build();
    }

}