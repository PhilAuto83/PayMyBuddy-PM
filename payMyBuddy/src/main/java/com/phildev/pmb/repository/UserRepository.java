package com.phildev.pmb.repository;

import com.phildev.pmb.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    User findByEmail(String email);

    @Query(
            value ="UPDATE user SET email=?1 WHERE email=?2;",
            nativeQuery= true
    )

    User update(String newEmail, String currentEmail);
}
