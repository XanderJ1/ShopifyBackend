package shopify.Security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;
import shopify.Services.UserService;
import shopify.Utility.RSAKeyProperties;

import java.net.http.HttpClient;
import java.security.interfaces.RSAPrivateKey;

@Configuration
public class SecurityConfig {


    public RSAKeyProperties keys;

    SecurityConfig(RSAKeyProperties keys ){
        this.keys = keys;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures authentication controller to use DAO authentication provider
     * with userService and password encoder
     * @param userService The service responsible for retrieving user details.
     * @return Authentication manager instance
     */
    @Bean
    AuthenticationManager authenticationManager(UserService userService){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    /**
     * Configures the security filter chain to manage authentication and authorization
     * Allows all /users (For testing purposes), /auth endpoints
     * @param http The HttpSecurity instance for security configurations
     * @return The configured SecurityFilterChain.
     * @throws Exception If an error occurs
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth ->{
                            auth.requestMatchers("api/v1/users/**").permitAll();
                            auth.requestMatchers("api/v1/products/**").permitAll();
                            auth.requestMatchers("auth/**").permitAll();
                            auth.requestMatchers("/api/v1/products").permitAll();
                            auth.anyRequest().authenticated();
                })
                .oauth2ResourceServer(oauth -> oauth.jwt( jwt -> jwt
                        .decoder(jwtDecoder())
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())
                ))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    /**
     * Bean for decoding JWT tokens using the application's private RSA key.
     * @return A JwtDecoder instance for validating JWT tokens.
     */
    @Bean
    public JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(keys.getPublicKey()).build();
    }

    /**
     * Bean for encoding JWT tokens using the application's private RSA key.
     * @return A JwtEncoder instance for signing JWT tokens.
     */
    @Bean
    public JwtEncoder jwtEncoder(){
        JWK jwk = new RSAKey.Builder(keys.getPublicKey())
                .privateKey(keys.getPrivateKey()).build();
        JWKSource<SecurityContext> jwks =new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    /**
     * Configures JWT tokens to extract roles from the token
     * @return A JwtAuthenticationConverter instance for processing JWT claims
     */
    JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtAuthConverter = new JwtGrantedAuthoritiesConverter();
        jwtAuthConverter.setAuthoritiesClaimName("roles");
        jwtAuthConverter.setAuthorityPrefix("ROLES_");
        JwtAuthenticationConverter jwt = new JwtAuthenticationConverter();
        jwt.setJwtGrantedAuthoritiesConverter(jwtAuthConverter);
        return jwt;
    }

    /**
     * Configures Cross-Origin Resource Sharing (CORS) settings to allow frontend interactions
     * @return A CCorsConfiguration defining the allowed headers, methods and origin
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedOrigin("http://localhost:5173");
        corsConfiguration.addAllowedOrigin("http://127.0.0.1:5500");
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    WebClient webClient(WebClient.Builder webclient){
        return webclient.build();
    }
}
