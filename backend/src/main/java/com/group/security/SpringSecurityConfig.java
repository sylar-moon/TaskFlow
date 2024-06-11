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
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SpringSecurityConfig {

    private final PersonService personService;

    private final JwtRequestFilter jwtRequestFilter;

    private final MyAuthenticationSuccessHandler successHandler;

    private final PasswordEncoder passwordEncoder;

    private final ClientRegistrationRepository clientRegistrationRepository;

    @Value("${client.url}")
    private String clientURL;

    @Value("${server.port}")
    private String serverURL;

    @Autowired
    public SpringSecurityConfig(PersonService personService,
                                JwtRequestFilter jwtRequestFilter,
                                MyAuthenticationSuccessHandler successHandler,
                                PasswordEncoder passwordEncoder, ClientRegistrationRepository clientRegistrationRepository) {
        this.personService = personService;
        this.jwtRequestFilter = jwtRequestFilter;
        this.successHandler=successHandler;
        this.passwordEncoder=passwordEncoder;
        this.clientRegistrationRepository = clientRegistrationRepository;
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
                        requestMatchers("/api/user").authenticated().anyRequest().permitAll()).

                oauth2Login(oauth2 -> oauth2.authorizationEndpoint(authorizationEndpoint ->
                                authorizationEndpoint
                                        .authorizationRequestResolver(customAuthorizationRequestResolver(clientRegistrationRepository))
                        )
                .successHandler(successHandler)).

                requiresChannel(requiresChannel ->
                        requiresChannel
                                .anyRequest().requiresInsecure()
//                                .anyRequest().requiresSecure()


                ).addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }




    @Bean
    public DefaultOAuth2AuthorizationRequestResolver customAuthorizationRequestResolver(ClientRegistrationRepository repo) {
        DefaultOAuth2AuthorizationRequestResolver resolver = new DefaultOAuth2AuthorizationRequestResolver(
                repo, OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI);
        resolver.setAuthorizationRequestCustomizer(customizer ->
                customizer.additionalParameters(params -> params.put("prompt", "select_account")));
        return resolver;
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

        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setUserDetailsService(personService);

        return authenticationProvider;
    }



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
