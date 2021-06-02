package com.dumontsean.services.registries;

import com.dumontsean.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface UserRegistry extends JpaRepository<User, Long> {

}
