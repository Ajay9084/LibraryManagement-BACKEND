package com.example.LibraryManagement.Security;

import com.example.LibraryManagement.JWT.JwtAuthenticationFilter;
import com.example.LibraryManagement.Service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.csrf(csrf -> csrf.disable())

				.authorizeHttpRequests(auth -> auth
						.requestMatchers("OPTIONS", "/**").permitAll()  // Allow all OPTIONS requests (CORS preflight)
						.requestMatchers("/auth/**").permitAll()   // login/register allowed
						.requestMatchers("/admin/**").hasRole("ADMIN")
						.requestMatchers("/issuerecords/**").authenticated()
						.requestMatchers("/books/**").authenticated()
						.anyRequest().authenticated()
				)

				.sessionManagement(session ->
						session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)

				.authenticationProvider(authenticationProvider())

				.addFilterBefore(jwtAuthenticationFilter,
						UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailService();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {

		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService());
		provider.setPasswordEncoder(passwordEncoder());

		return provider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(
			AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Arrays.asList(
				"http://localhost:3000",    // React dev server (port 3000)
				"http://localhost:5173",// Vite dev server (port 5173)
				"http://localhost:5174",
				"http://localhost:8080"     // Backend (for testing)
		));
		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(Arrays.asList("*"));
		config.setAllowCredentials(true);
		config.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}