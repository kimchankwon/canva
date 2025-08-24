package com.interview.canva.repository;

import com.interview.canva.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Basic CRUD operations are automatically provided by JpaRepository
    // You can add custom query methods here if needed
}
