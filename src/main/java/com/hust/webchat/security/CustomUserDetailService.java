package com.hust.webchat.security;

import com.hust.webchat.domain.User;
import com.hust.webchat.repository.UserRepository;
import com.hust.webchat.util.Constants;
import com.hust.webchat.web.error.UserNotActivatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component("userDetailsService")
public class CustomUserDetailService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(CustomUserDetailService.class);
    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsernameAndStatus(username, Constants.ENTITY_STATUS.ACTIVE)
                .map(user -> createSpringSecurityUser(username, user))
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " was not found in the " +
                        "database"));
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String username,
                                                                                        User user) {
        if (!user.getActivated()) {
            throw new UserNotActivatedException("User " + username + " was not activated");
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(Arrays.asList(new SimpleGrantedAuthority(AuthoritiesConstants.USER)));

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                grantedAuthorities);
    }
}
