package ws.academy.auction.core.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@OpenAPIDefinition(
        info = @Info(title = "Auction API", version = "v1"),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final CustomJwtAuthenticationConverter customJwtAuthenticationConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(customJwtAuthenticationConverter)
                        )

        );
        httpSecurity.cors();
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(
                            "/api/v1/auth/login/",
                            "/api/v1/auth/password-recovery/",
                            "/api/v1/auth/reset-password/",
                            "/api/v1/auth/register/",
                            "/api/v1/auth/activate/"
                    ).anonymous();

                    auth.requestMatchers("/api/v1/auth/refresh-token/").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/lots/*/").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/auctions/").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/auctions/*/").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/auctions/*/lots/").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/auctions/*/lots/*/bids/").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/api/v1/auctions/*/participants/").permitAll();

                    auth.requestMatchers("/api/v1/auth/current-user/").hasAnyRole("ADMIN", "PARTICIPANT");

                    auth.requestMatchers("/api/v1/lots/").hasAnyRole("ADMIN", "PARTICIPANT");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/lots/*/").hasAnyRole("ADMIN", "PARTICIPANT");
                    auth.requestMatchers(HttpMethod.DELETE,
                            "/api/v1/lots/*/").hasAnyRole("ADMIN", "PARTICIPANT");

                    auth.requestMatchers("/api/v1/users/**").hasRole("ADMIN");

                    auth.requestMatchers(HttpMethod.POST, "/api/v1/auctions/").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.POST,
                            "/api/v1/auctions/*/lots/").hasAnyRole("ADMIN", "PARTICIPANT");
                    auth.requestMatchers(HttpMethod.DELETE,
                            "/api/v1/auctions/*/lots/").hasAnyRole("ADMIN", "PARTICIPANT");
                    auth.requestMatchers(HttpMethod.PATCH, "/api/v1/auctions/*/").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/auctions/*/").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/auctions/*/*/").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.POST,
                            "/api/v1/auctions/*/participants/").hasRole("PARTICIPANT");
                    auth.requestMatchers(HttpMethod.GET,
                            "/api/v1/auctions/*/participants/*/").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE,
                            "/api/v1/auctions/*/participants/*/").hasRole("PARTICIPANT");
                    auth.requestMatchers(HttpMethod.GET,
                            "/api/v1/auctions/*/participants/participate-application/")
                            .hasAnyRole("ADMIN", "PARTICIPANT");
                    auth.requestMatchers(HttpMethod.POST,
                            "/api/v1/auctions/*/participants/participate-application/").hasRole("PARTICIPANT");
                    auth.requestMatchers(HttpMethod.POST,
                            "/api/v1/auctions/*/participants/participate-application/*/").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.POST,
                            "/api/v1/auctions/*/lots/*/for-sale/").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.PUT, "/api/v1/auctions/*/lots/*/").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.DELETE, "/api/v1/auctions/*/lots/*/").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.POST, "/api/v1/auctions/*/lots/*/bids/").hasRole("PARTICIPANT");

                    auth.requestMatchers("/api/v1/auctions/*/participants/participate-application/")
                            .hasAnyRole("ADMIN", "PARTICIPANT");
                    auth.requestMatchers(HttpMethod.GET,
                            "/api/v1/auctions/participants/*/participate-application/").hasRole("ADMIN");

                    auth.requestMatchers(HttpMethod.GET,
                            "/api/v1/participant/*/purchases").hasAnyRole("ADMIN", "PARTICIPANT");
                    auth.requestMatchers(HttpMethod.POST,
                            "/api/v1/participant/*/account/").hasRole("PARTICIPANT");
                    auth.requestMatchers(HttpMethod.GET,
                            "/api/v1/participant/*/account/").hasAnyRole("ADMIN", "PARTICIPANT");
                    auth.requestMatchers(HttpMethod.PUT,
                            "/api/v1/participant/*/account/").hasRole("PARTICIPANT");
                    auth.requestMatchers(HttpMethod.POST,
                            "/api/v1/participant/*/account/withdraw/").hasRole("PARTICIPANT");
                    auth.requestMatchers(HttpMethod.GET,
                            "/api/v1/participant/*/account/transactions/").hasRole("PARTICIPANT");
                    auth.requestMatchers(HttpMethod.POST,
                            "/api/v1/auctions/*/lots/*/bid/").hasRole("PARTICIPANT");

                    auth.requestMatchers("/api/v1/file").hasAnyRole("ADMIN", "PARTICIPANT");

                    auth.anyRequest().authenticated();
                })
                .httpBasic(Customizer.withDefaults())
                .build();
    }
}
