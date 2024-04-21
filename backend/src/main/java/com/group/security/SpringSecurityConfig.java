package com.group.security;


import com.group.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SpringSecurityConfig {

    private final PersonService personService;

    private final JwtRequestFilter jwtRequestFilter;

    private final MyAuthenticationSuccessHandler successHandler;

    @Value("${client.url}")
    private String clientURL;

    @Value("${server.port}")
    private String serverURL;

    @Autowired
    public SpringSecurityConfig(PersonService personService,
                                JwtRequestFilter jwtRequestFilter,
                                MyAuthenticationSuccessHandler successHandler) {
        this.personService = personService;
        this.jwtRequestFilter = jwtRequestFilter;
        this.successHandler=successHandler;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable).
//                cors(CorsConfigurer::disable).
                sessionManagement(
                        httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)).
                exceptionHandling(
                        httpSecurityExceptionHandlingConfigurer ->
                                httpSecurityExceptionHandlingConfigurer
                                        .authenticationEntryPoint(
                                                new RestAuthenticationEntryPoint())).
                authorizeHttpRequests(request -> request.
                        requestMatchers("/oauth2/**").permitAll().
                        requestMatchers("/api//auth").permitAll().
                        requestMatchers("/api/registration").permitAll().
                        requestMatchers("/api/tasks").authenticated().
                        requestMatchers("/api/user").hasRole("ADMIN").anyRequest().permitAll()).

                oauth2Login(oauth2 -> oauth2
                .successHandler(successHandler)).

                requiresChannel(requiresChannel ->
                        requiresChannel
                                .anyRequest().requiresInsecure()
//                                .anyRequest().requiresSecure()


                ).addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowedOrigins(Arrays.asList(clientURL, serverURL));
//        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
//        corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfiguration);
//        return source;
//    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(personService);

        return authenticationProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
