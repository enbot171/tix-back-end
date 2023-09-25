package com.example.Project.Controller;

import com.example.Project.Entity.User;
import com.example.Project.Repository.UserRepository;
import com.example.Project.Request.LoginRequest;
import com.example.Project.Request.SignUpRequest;
import com.example.Project.Response.MessageResponse;
import com.example.Project.Response.UserInfoResponse;
import com.example.Project.Service.JWTUtil;
import com.example.Project.Service.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JWTUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate (new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            ResponseCookie jwtCookie = jwtUtil.generateJwtCookie(userDetails);
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(new UserInfoResponse(userDetails.getId(), userDetails.getFullname(), userDetails.getEmail(), userDetails.getMobile()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Incorrect email or password. Type the correct email and password, and try again."));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use"));
        }

        // Create new user's account
        User user = new User(
                signupRequest.getFullname(),
                signupRequest.getEmail(),
                signupRequest.getMobile(),
                encoder.encode(signupRequest.getPassword()));

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Registration Successful"));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUser(HttpServletRequest request){
        String jwtToken = jwtUtil.getJwtFromCookies(request);

        if (jwtToken != null && jwtUtil.validateJwtToken(jwtToken)) {
            String userEmail = jwtUtil.getUserNameFromJwtToken(jwtToken);
            Optional<User> user = userRepository.findByEmail(userEmail);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse("Please Login to continue"));
            }
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error"));
        }
    }
}
