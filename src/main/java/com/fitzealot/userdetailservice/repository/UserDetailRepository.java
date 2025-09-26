package com.fitzealot.userdetailservice.repository;

import com.fitzealot.userdetailservice.model.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailRepository extends JpaRepository<UserDetails,String> {
}
