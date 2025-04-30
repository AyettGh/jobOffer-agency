package org.example.recrutement.Repository;

import org.example.recrutement.Entity.Application;
import org.example.recrutement.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application,Long> {

}
