package br.com.ecomerce.ecomerce.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private Environment env;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String[] PUBLIC_MATCHERS = {
            "/h2-console/**"
    };

    private static final String[] PUBLIC_MATCHERS_GET = {
            "/produtos/**",
            "/categorias/**",

    };

    private static final String[] PUBLIC_MATCHERS_POST = {
            "/clientes/**"
    };

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());

        return authProvider;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationManager authenticationManager
    ) throws Exception {

        // libera H2 console no profile test
        if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
            http.headers(headers ->
                    headers.frameOptions(frame -> frame.disable())
            );
        }

        JWTAuthenticationFilter authenticationFilter =
                new JWTAuthenticationFilter(
                        authenticationManager,
                        jwtUtil,
                        objectMapper
                );

        JWTAuthorizationFilter authorizationFilter =
                new JWTAuthorizationFilter(
                        authenticationManager,
                        jwtUtil,
                        userDetailsService
                );

        http

                .cors(cors -> {})

                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(PUBLIC_MATCHERS)
                        .permitAll()

                        .requestMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET)
                        .permitAll()

                        .requestMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST)
                        .permitAll()

                        .anyRequest()
                        .authenticated()
                )

                .addFilter(authenticationFilter);
                http.addFilter(authorizationFilter
                );


        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean public BCryptPasswordEncoder bCryptPasswordEncoder() { return new BCryptPasswordEncoder(); }
}

//@Configuration
//public class SecurityConfig {
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//    @Autowired
//    private JWTUtil jwtUtil;
//
//    private static final String[] PUBLIC_MATCHERS = {
//            "/h2-console/**"
//    };
//
//    private static final String[] PUBLIC_MATCHERS_GET = {
//            "/produtos/**",
//            "/categorias/**",
//            "/clientes/**"
//    };
//
//    @Autowired
//    private Environment env;
//
//
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
//            http.headers(headers -> headers.frameOptions(frame -> frame.disable()));
//        }
//
//        http
//                .cors(cors -> {})
//                .csrf(csrf -> csrf.disable())
//
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
//                        .requestMatchers(PUBLIC_MATCHERS).permitAll()
//                        .anyRequest().authenticated()
//
//                )
//
//                .addFilter(new JWTAuthenticationFilter(authenticationManager, jwtUtil));
//
////                .addFilter(new JWTAuthorizationFilter(
////                        authenticationManager,
////                        jwtUtil,
////                        userDetailsService
////                ))
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                );
//
//        return http.build();
//    }
//
//
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
//    }
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//}

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http
//                .csrf(csrf -> csrf.disable())
//
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(PUBLIC_MATCHERS).permitAll()
//                        .requestMatchers("/auth/**").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/clientes/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/categorias/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
//                        .anyRequest().authenticated()
//                )
//
//                .headers(headers -> headers
//                        .frameOptions(frame -> frame.disable())
//                )
//
//                .httpBasic(httpBasic -> httpBasic.disable())
//                .formLogin(form -> form.disable());
//
//        return http.build();
//    }

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
