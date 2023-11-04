package com.example.Project.User;

//User Repo

import java.util.Optional;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

}
