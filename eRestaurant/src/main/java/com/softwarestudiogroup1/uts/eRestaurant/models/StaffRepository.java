package com.softwarestudiogroup1.uts.eRestaurant.models;

import java.util.Optional;

import com.softwarestudiogroup1.uts.eRestaurant.models.entities.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface StaffRepository extends JpaRepository<Staff, Integer> {

    /**
     * Retrieve {@link Staff} from data store if username and password match
     * 
     * @param username Staff username
     * @param password Staff password
     * @return An optional Staff, username/password may not be found
     */

    @Query("SELECT staff FROM Staff staff WHERE LOWER(staff.username) = LOWER(?1) AND staff.password = ?2")
    @Transactional(readOnly = true)
    Optional<Staff> findByUserNameAndLastName(String username, String password);
}
