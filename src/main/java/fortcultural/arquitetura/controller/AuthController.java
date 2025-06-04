package fortcultural.arquitetura.controller;

import fortcultural.arquitetura.dto.*;
import fortcultural.arquitetura.model.entity.User;
import fortcultural.arquitetura.model.enums.TypeUser;
import fortcultural.arquitetura.security.JwtUtil;
import fortcultural.arquitetura.service.impl.UserDetailsServiceImpl;
import fortcultural.arquitetura.service.interfaces.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.dao.DataIntegrityViolationException;
import jakarta.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(JwtUtil jwtUtil,
                          AuthenticationManager authenticationManager,
                          UserDetailsServiceImpl userDetailsService,
                          UserService userService,
                          PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Autentica com email e senha
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            // Carrega detalhes do usuário para obter roles
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());

            // Extrai roles do usuário
            String roles = userDetails.getAuthorities().stream()
                    .map(authority -> authority.getAuthority())
                    .collect(Collectors.joining(","));

            // Gera token com roles
            String token = jwtUtil.generateTokenWithRoles(authentication.getName(), roles);

            // Retorna resposta estruturada
            LoginResponse response = new LoginResponse(
                    token,
                    authentication.getName(),
                    roles,
                    jwtUtil.getExpirationTime()
            );

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "INVALID_CREDENTIALS",
                    "Email ou senha inválidos"
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        } catch (Exception e) {
            // Log temporário para debug
            e.printStackTrace();
            ErrorResponse errorResponse = new ErrorResponse(
                    "AUTHENTICATION_ERROR",
                    "Erro interno no servidor: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            // Verifica se o email já existe
            if (userService.existsByEmail(registerRequest.getEmail())) {
                ErrorResponse errorResponse = new ErrorResponse(
                        "EMAIL_ALREADY_EXISTS",
                        "Email já está em uso"
                );
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
            }

            // Cria novo usuário
            User newUser = new User();
            newUser.setName(registerRequest.getName());
            newUser.setEmail(registerRequest.getEmail());
            newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            newUser.setType(registerRequest.getType() != null ? TypeUser.valueOf(registerRequest.getType()) : TypeUser.USER);

            // Salva o usuário
            User savedUser = userService.createUser(newUser);

            // Retorna resposta de sucesso
            RegisterResponse response = new RegisterResponse(
                    "Usuário criado com sucesso",
                    savedUser.getEmail(),
                    savedUser.getName()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (DataIntegrityViolationException e) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "EMAIL_ALREADY_EXISTS",
                    "Email já está em uso"
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "REGISTRATION_ERROR",
                    "Erro interno no servidor durante o registro"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}