package com.nals.work.repository;

import com.nals.work.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author HanhLe
 */
@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {
}
