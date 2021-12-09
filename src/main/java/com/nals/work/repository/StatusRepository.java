package com.nals.work.repository;

import com.nals.work.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author HanhLe
 */
@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {
}
