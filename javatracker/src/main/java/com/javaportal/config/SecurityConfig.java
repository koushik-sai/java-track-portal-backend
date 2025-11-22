//package com.javaportal.config;
//
//import javax.sql.DataSource;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
//import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
//
//import com.javaportal.service.EmployeeUserDetailsService;
//
//@Configuration
//public class SecurityConfig {
//
//    @Autowired
//    private DataSource dataSource;
//
//    @Autowired
//    private EmployeeUserDetailsService employeeUserDetailsService;
//
//    @Autowired
//    private LoginSuccessHandler loginSuccessHandler;
//
//    @Autowired
//    private LoginFailureHandler loginFailureHandler;
//
//    @Bean	
//    UserDetailsService userDetailsService() {
//        return employeeUserDetailsService;
//    }
//
//    @Bean
//    AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(employeeUserDetailsService);
//        provider.setPasswordEncoder(passwordEncoder);
//        return provider;
//    }
//
//    @Bean
//    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//
//    @Bean
//    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .cors(withDefaults())
//                .csrf(csrf -> csrf.disable())
//                .anonymous(anonymous -> anonymous
//                	    .principal("guestUser")
//                	    .authorities("ROLE_USER")
//                )
//                .authorizeHttpRequests(auth -> auth
//                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                                .requestMatchers("/api/query/**").permitAll()
//                                .requestMatchers("/api/auth/**").permitAll()
//                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
//                                .requestMatchers("/api/eventCoordinator/**").hasRole("EVENT_COORDINATOR")
//                                .requestMatchers("/api/manager/**").hasRole("MANAGER")
//                                .requestMatchers("/api/operationsAnchor/**").hasRole("OPERATIONS_ANCHOR")
//                                .requestMatchers("/api/teamMember/**").hasRole("TEAM_MEMBER")
//                                .requestMatchers("/api/notification/**").hasRole("MANAGER")
//                                .requestMatchers("/api/global/**").hasAnyRole("ADMIN", "MANAGER", "EVENT_COORDINATOR", "OPERATIONS_ANCHOR", "TEAM_MEMBER", "USER")
//                                .requestMatchers("/api/thoughts/**").hasAnyRole("ADMIN", "MANAGER", "EVENT_COORDINATOR", "OPERATIONS_ANCHOR", "TEAM_MEMBER")
//                                .requestMatchers("/api/**").denyAll()
//                                .anyRequest().authenticated()
//                )
//                .formLogin(form -> form
//                                .loginProcessingUrl("/login")
//                                .usernameParameter("emailId")
//                                .passwordParameter("password")
//                                .successHandler(loginSuccessHandler)
//                                .failureHandler(loginFailureHandler)
//                )
//                .rememberMe(rm -> rm
//                                .rememberMeParameter("rememberMe")
//                                .userDetailsService(employeeUserDetailsService)
//                                .tokenRepository(persistentTokenRepository())
//                                .tokenValiditySeconds(7 * 24 * 60 * 60)
//                                .key("uniqueAndSecret")
//                );
//
//        return http.build();
//    }
//
//    @Bean
//    PersistentTokenRepository persistentTokenRepository() {
//        JdbcTokenRepositoryImpl tokenRepo = new JdbcTokenRepositoryImpl();
//        tokenRepo.setDataSource(dataSource);
//        return tokenRepo;
//    }
//
//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.addAllowedOrigin("http://localhost:4200");
//        config.addAllowedMethod("*");
//        config.addAllowedHeader("*");
//        config.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }
//
//    @Bean
//    PasswordEncoder passwordEncoder() {
//    	return new BCryptPasswordEncoder();
//    }
//    
//}
//

package com.javaportal.config;

import static org.springframework.security.config.Customizer.withDefaults; // <--- static import

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
// use servlet (non-reactive) UrlBasedCorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.javaportal.service.EmployeeUserDetailsService;

@Configuration
public class SecurityConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private EmployeeUserDetailsService employeeUserDetailsService;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Bean	
    UserDetailsService userDetailsService() {
        return employeeUserDetailsService;
    }

    @Bean
    AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(employeeUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults()) // now resolves correctly to Customizer.withDefaults()
                .csrf(csrf -> csrf.disable())
                .anonymous(anonymous -> anonymous
                	    .principal("guestUser")
                	    .authorities("ROLE_USER")
                )
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .requestMatchers("/api/query/**").permitAll()
                                .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                .requestMatchers("/api/eventCoordinator/**").hasRole("EVENT_COORDINATOR")
                                .requestMatchers("/api/manager/**").hasRole("MANAGER")
                                .requestMatchers("/api/operationsAnchor/**").hasRole("OPERATIONS_ANCHOR")
                                .requestMatchers("/api/teamMember/**").hasRole("TEAM_MEMBER")
                                .requestMatchers("/api/notification/**").hasRole("MANAGER")
                                .requestMatchers("/api/global/**").hasAnyRole("ADMIN", "MANAGER", "EVENT_COORDINATOR", "OPERATIONS_ANCHOR", "TEAM_MEMBER", "USER")
                                .requestMatchers("/api/thoughts/**").hasAnyRole("ADMIN", "MANAGER", "EVENT_COORDINATOR", "OPERATIONS_ANCHOR", "TEAM_MEMBER")
                                .requestMatchers("/api/**").denyAll()
                                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                                .loginProcessingUrl("/login")
                                .usernameParameter("emailId")
                                .passwordParameter("password")
                                .successHandler(loginSuccessHandler)
                                .failureHandler(loginFailureHandler)
                )
                .rememberMe(rm -> rm
                                .rememberMeParameter("rememberMe")
                                .userDetailsService(employeeUserDetailsService)
                                .tokenRepository(persistentTokenRepository())
                                .tokenValiditySeconds(7 * 24 * 60 * 60)
                                .key("uniqueAndSecret")
                );

        return http.build();
    }

    @Bean
    PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepo = new JdbcTokenRepositoryImpl();
        tokenRepo.setDataSource(dataSource);
        return tokenRepo;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:4200");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }

}

