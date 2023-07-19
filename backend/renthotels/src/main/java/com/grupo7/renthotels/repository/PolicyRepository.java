package com.grupo7.renthotels.repository;

import com.grupo7.renthotels.model.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyRepository extends JpaRepository<Policy, Long> {
}
