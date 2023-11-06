package com.example.Project.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    // Finds the user details by email
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found: " + email));

        return UserDetailsImpl.build(user);
    }

    // Finds user by verification code to check he has verified
    public boolean verified(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);

        if (user == null || user.isVerified()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setVerificationStatus(true);
            userRepository.save(user);
            return true;
        }
    }
}
