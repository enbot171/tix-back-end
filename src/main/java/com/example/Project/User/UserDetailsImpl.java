package com.example.Project.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private String id;

    private String fullname;

    private String email;

    private String mobile;

    private String password;

    private String verificationCode;

    private boolean isVerified;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(String id, String fullname, String email, String mobile, String password, String verificationCode, boolean isVerified,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.verificationCode = verificationCode;
        this.isVerified = isVerified;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user) {
        Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>(1);
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new UserDetailsImpl(
                user.getId(),
                user.getFullname(),
                user.getEmail(),
                user.getMobile(),
                user.getPassword(),
                user.getVerificationCode(),
                user.getVerificationStatus(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFullname() {
        return fullname;
    }

    public String getMobile() {
        return mobile;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getVerification() {
        return verificationCode;
    }

    public boolean isVerified() {
        return isVerified;
    }

    @Override // Username returns the email
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // return isVerified;
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}