package com.fitzealot.userservice.service.custom.impl;

import com.fitzealot.userservice.model.dto.UserDTO;
import com.fitzealot.userservice.model.entity.User;
import com.fitzealot.userservice.repository.UserRepository;
import com.fitzealot.userservice.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userDetailRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User byUsername = userDetailRepository.findByUsername(username);
        System.out.println(byUsername);

        if ( byUsername == null) {
            System.out.println("User not found with username: " + username);
            throw new UsernameNotFoundException(("User not found with username: " + username));
        }
        
    return new UserPrincipal(modelMapper.map(byUsername, UserDTO.class));



    }
}
