package com.fitzealot.userdetailservice.service;

import com.fitzealot.userdetailservice.model.dto.UserDetailsDTO;

public interface UserDetailService {

    public void createUserDetails(UserDetailsDTO userDetailsDTO);

    public UserDetailsDTO getUserDetails(String username);

}
