package com.kd.spring_user_service.service;

import com.kd.spring_user_service.model.CustomUserDetails;
import com.kd.spring_user_service.model.UserModel;
import com.kd.spring_user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserModel> findUser = userRepository.findByUsername(username);

        if(findUser.isEmpty()) {
            findUser = userRepository.findByEmail(username);
        }

        if(findUser.isPresent()) {
            var user = findUser.get();
            return new CustomUserDetails(findUser.get());

        }

        throw new UsernameNotFoundException("User not found with username or email: " + username);
    }
}
