package org.example.recrutement.Repository;

import org.example.recrutement.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
     User findByEmail(String email);

    boolean existsByEmail(String email);
     User findByUsername(String username);

}
