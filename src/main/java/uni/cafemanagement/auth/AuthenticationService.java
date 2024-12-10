package uni.cafemanagement.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uni.cafemanagement.config.JwtService;
import uni.cafemanagement.exception.ApiRequestException;
import uni.cafemanagement.model.User;
import uni.cafemanagement.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ApiRequestException("User with email " + request.getEmail() + " already exists");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstname());
        user.setLastName(request.getLastname());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Zapis użytkownika do bazy danych
        userRepository.save(user);

        // Generowanie tokenu JWT
        var jwtToken = jwtService.generateToken(
                user,
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().toString());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword())
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiRequestException("User with the email " + request.getEmail() + " not found"));
        var jwtToken = jwtService.generateToken(
                user,
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().toString()
                );
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse changePassword(ChangePasswordRequest changePasswordRequest) {
        var user = userRepository.findByEmail(changePasswordRequest.getEmail())
                .orElseThrow(() -> new ApiRequestException("User with the email " + changePasswordRequest.getEmail() + " not found"));
        if (passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            userRepository.save(user);
            var jwtToken = jwtService.generateToken(
                    user,
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getRole().toString());
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } else {
            throw new ApiRequestException("Old password is incorrect");
        }
    }

}