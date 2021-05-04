package com.softwarestudiogroup1.uts.eRestaurant.models;

import java.util.Optional;

import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ManagerRepository extends JpaRepository<Manager, Integer> {
    
    /**
     * Retrieve {@link Manager} from data store if username and password match
     * 
     * @param username Manager username
     * @param password Manager password
     * @return An optional Manager, username/password may not be found
     */

    @Query("SELECT manager FROM Manager manager WHERE LOWER(manager.username) = LOWER(?1) AND manager.password = ?2")
    @Transactional(readOnly = true)
    Optional<Manager> findByUserNameAndPassword(String username, String password);
}
