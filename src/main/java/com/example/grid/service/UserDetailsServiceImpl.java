package com.example.grid.service;

import com.example.grid.data.db.User;
import com.example.grid.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository users;

    public UserDetailsServiceImpl(UserRepository users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = users.findById(username);
        if (!user.isPresent())
            throw new UsernameNotFoundException(username);
        return new CustomUserDetails(user.get().getUsername(), user.get().getPassword());
    }

    public static class CustomUserDetails implements UserDetails {
        private String username;
        private String password;

        public CustomUserDetails(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public Collection<? extends GrantedAuthority> getAuthorities() { return Collections.emptyList(); }
        public String getPassword() { return password; }
        public String getUsername() { return username; }
        public boolean isAccountNonExpired() { return true; }
        public boolean isAccountNonLocked() { return true; }
        public boolean isCredentialsNonExpired() { return true; }
        public boolean isEnabled() { return true; }
    }
}
