package com.grupo7.renthotels.repository;

import com.grupo7.renthotels.model.Function;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface FunctionRepository extends JpaRepository<Function, Long> {
    Optional<Function> findByName(String name);

    Optional<Function> findByFunctionSku(Long functionSku);
}
