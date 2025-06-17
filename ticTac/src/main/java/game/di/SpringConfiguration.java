package game.di;
import game.datasource.model.repository.DataRepository;
import game.datasource.records.repository.RecordsRepository;
import game.datasource.records.service.RecordsService;
import game.datasource.user.repository.UserRepository;
import game.domain.service.logicService;
import game.domain.user.service.UserService;
import game.web.filter.AuthFilter;
import game.web.user.model.UserWeb;
import game.web.webSocket.WebSocketService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.annotation.SessionScope;

@Configuration
public class SpringConfiguration {
    @Bean
    public logicService minimax () {
        return new logicService();
    }

    @Bean
    public UserService authorization (UserRepository userRepository) {
        return new UserService(userRepository);
    }

    @Bean
    public RecordsService recordsService (RecordsRepository recordsRepository, WebSocketService webSocketService) {
        return new RecordsService(recordsRepository, webSocketService);
    }

    @Bean
    @SessionScope
    public UserWeb userWeb () {
        return new UserWeb();
    }

    @Bean
    public AuthFilter authFilter (UserService userService) {
        return new AuthFilter(userService);
    }

    @Bean
    public WebSocketService webSocketHandler (DataRepository dataRepository) { return new WebSocketService(dataRepository);}

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthFilter authFilter) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/login", "/favicon.ico").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .maximumSessions(1)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
