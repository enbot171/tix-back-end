package com.example.Project.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.Project.Email.EmailSenderService;
import com.example.Project.Security.AuthEntryPointJwt;

import java.util.Optional;


@RestController
@CrossOrigin
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDetailsServiceImpl service;

    @Autowired
    EmailSenderService emailSender;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JWTUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();  

            if (!userDetails.isVerified()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Verify email before logging in."));
            } 

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

        // send verification email
        try {
            emailSender.sendVerificationEmail(user);
        } catch (Exception e ){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Verification email sent unsuccessfully."));
        }

        // save user again to change verification status to true
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Verification email sent. Please verify your email."));
    }

    @GetMapping("/verify") 
    public String verifyUser(@Param("code") String code) {
        if (service.verified(code)) {
            return "Verification Successful! You have registered successfully";
        } else {
            return "Verification Unsuccessful: Account already verified, or verification code is invalid";
        }
    }
    
    @GetMapping("/profile")
    public ResponseEntity<?> getUser(HttpServletRequest request){
        String jwtToken = jwtUtil.getJwtFromCookies(request);

        if (jwtToken != null && jwtUtil.validateJwtToken(jwtToken)) {
            String userEmail = jwtUtil.getUserNameFromJwtToken(jwtToken);
            Optional<User> optionalUser = userRepository.findByEmail(userEmail);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                UserInfoResponse userInfo = new UserInfoResponse(user.getId(), user.getFullname(), user.getEmail(), user.getMobile());
                return ResponseEntity.ok(userInfo);
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse("Please Login to continue"));
            }
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error"));
        }
    }
}
