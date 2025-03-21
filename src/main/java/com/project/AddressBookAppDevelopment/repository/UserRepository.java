package com.project.AddressBookAppDevelopment.repository;

import com.project.AddressBookAppDevelopment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * FROM AUTH_USER WHERE EMAIL = :email", nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);

    Optional<User> findByResetToken(String resetToken);

}