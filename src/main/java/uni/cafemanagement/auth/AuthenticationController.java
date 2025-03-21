package uni.cafemanagement.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/registerManager")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody ManagerRegisterRequest registerRequest) {
        return ResponseEntity.ok(authenticationService.registerManager(registerRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest loginRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(loginRequest));
    }

    @PutMapping("/change-password")
    public ResponseEntity<AuthenticationResponse> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        return ResponseEntity.ok(authenticationService.changePassword(changePasswordRequest));
    }

}