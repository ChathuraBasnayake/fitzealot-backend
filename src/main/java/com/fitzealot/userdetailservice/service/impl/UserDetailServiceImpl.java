package com.fitzealot.userdetailservice.service.impl;

import com.fitzealot.userdetailservice.model.dto.UserDetailsDTO;
import com.fitzealot.userdetailservice.model.entity.UserDetails;
import com.fitzealot.userdetailservice.repository.UserDetailRepository;
import com.fitzealot.userdetailservice.service.UserDetailService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailService {

    final private UserDetailRepository userDetailRepository;
    final private ModelMapper modelMapper;


    public void createUserDetails(UserDetailsDTO userDetailsDTO) {
        System.out.println(userDetailsDTO);
        UserDetails map = modelMapper.map(userDetailsDTO, UserDetails.class);
        System.out.println(map);
        userDetailRepository.save(map);

    }

    public UserDetailsDTO getUserDetails(String username) {
        UserDetails userDetails = userDetailRepository.findById(username).orElseThrow(() -> new RuntimeException("User not found"));

        return modelMapper.map(userDetails, UserDetailsDTO.class);
    }


}
