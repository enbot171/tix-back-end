package com.example.Project.Controller;


import com.example.Project.Entity.User;
import com.example.Project.Repository.UserRepository;
import com.example.Project.Request.SignUpRequest;
import com.example.Project.Response.MessageResponse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SignUpTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @AfterEach
    void tearDown() {
        // clear the database after each test
        userRepository.deleteAll();
    }
    @Test
    public void testRegisterUserWithEmailExists() {
        // Arrange ***
        SignUpRequest signUpRequest = new SignUpRequest("Bob", "bob@gmail.com", "80808080", "password");


        // Act ***
        //inserts first signup into db
        authController.registerUser(signUpRequest);
        
        //tries to signup using existing email
        ResponseEntity<?> responseEntity = authController.registerUser(signUpRequest);
        

        // Assert ***
        MessageResponse expectedResponse = new MessageResponse("Error: Email is already in use");
        ResponseEntity<?> expectedEntity = ResponseEntity.badRequest().body(expectedResponse);
        assertResponseEntitiesEqual(expectedEntity, responseEntity);
    }

    // Helper method to assert that two ResponseEntity objects are equal
    private void assertResponseEntitiesEqual(ResponseEntity<?> expected, ResponseEntity<?> actual) {
        assertEquals(expected.getStatusCode(), actual.getStatusCode());
        assertEquals(expected.getBody(), actual.getBody());
    }
}