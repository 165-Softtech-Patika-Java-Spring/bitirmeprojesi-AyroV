package com.softtech.webapp.security.jwt;


import com.softtech.webapp.user.entity.User;
import com.softtech.webapp.user.service.UserEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserEntityService userEntityService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userEntityService.findByUsername(username);
        return JwtUserDetails.create(user);
    }

    public UserDetails loadUserByUserId(Long id) {
        User user = userEntityService.getByIdWithControl(id);
        return JwtUserDetails.create(user);
    }
}
