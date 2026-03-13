package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.DTO.LoginRequestDTO;
import com.example.LibraryManagement.DTO.LoginResponseDTO;
import com.example.LibraryManagement.DTO.RegisterRequestDTO;
import com.example.LibraryManagement.Entity.User;
import com.example.LibraryManagement.JWT.JwtService;
import com.example.LibraryManagement.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	// =============================
	// REGISTER NORMAL USER
	// =============================
	public User registerNormalUser(RegisterRequestDTO request) {

		if (userRepository.findByUsername(request.getUsername()).isPresent()) {
			throw new RuntimeException("User already exists");
		}

		Set<String> roles = new HashSet<>();
		roles.add("USER"); // store without ROLE_ prefix

		User user = new User();
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRoles(roles);

		return userRepository.save(user);
	}

	// =============================
	// REGISTER ADMIN USER
	// =============================
	public User registerAdminUser(RegisterRequestDTO request) {

		if (userRepository.findByUsername(request.getUsername()).isPresent()) {
			throw new RuntimeException("Admin already exists");
		}

		Set<String> roles = new HashSet<>();
		roles.add("ADMIN"); // store without ROLE_ prefix
		roles.add("USER");  // admin also has user access

		User user = new User();
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRoles(roles);

		return userRepository.save(user);
	}

	// =============================
	// LOGIN USER
	// =============================
	public LoginResponseDTO login(LoginRequestDTO request) {

		// authenticate username & password
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getUsername(),
						request.getPassword()
				)
		);

		// fetch user from DB
		User user = userRepository.findByUsername(request.getUsername())
				.orElseThrow(() -> new RuntimeException("User not found"));

		// generate JWT
		String token = jwtService.generateToken(user);

		return LoginResponseDTO.builder()
				.token(token)
				.username(user.getUsername())
				.roles(user.getRoles())
				.build();
	}
}