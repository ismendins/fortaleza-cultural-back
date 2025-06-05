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
import org.springframework.security.core.GrantedAuthority;
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
    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.getEmail(),
                            loginRequestDTO.getPassword()
                    )
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestDTO.getEmail());

            String roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            String token = jwtUtil.generateTokenWithRoles(authentication.getName(), roles);
            LoginResponseDTO response = new LoginResponseDTO(
                    token,
                    authentication.getName(),
                    roles,
                    jwtUtil.getExpirationTime()
            );

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                    "INVALID_CREDENTIALS",
                    "Email ou senha inválidos"
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                    "AUTHENTICATION_ERROR",
                    "Erro interno no servidor: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDTO);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        try {
            if (userService.existsByEmail(registerRequestDTO.getEmail())) {
                ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                        "EMAIL_ALREADY_EXISTS",
                        "Email já está em uso"
                );
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDTO);
            }

            User newUser = new User();
            newUser.setName(registerRequestDTO.getName());
            newUser.setEmail(registerRequestDTO.getEmail());
            newUser.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
            newUser.setType(registerRequestDTO.getType() != null ? TypeUser.valueOf(registerRequestDTO.getType()) : TypeUser.USER);

            User savedUser = userService.createUser(newUser);

            RegisterResponseDTO response = new RegisterResponseDTO(
                    "Usuário criado com sucesso",
                    savedUser.getEmail(),
                    savedUser.getName()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (DataIntegrityViolationException e) {
            ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                    "EMAIL_ALREADY_EXISTS",
                    "Email já está em uso"
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDTO);
        } catch (Exception e) {
            ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                    "REGISTRATION_ERROR",
                    "Erro interno no servidor durante o registro"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDTO);
        }
    }
}