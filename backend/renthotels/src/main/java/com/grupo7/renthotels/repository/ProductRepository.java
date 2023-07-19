package com.grupo7.renthotels.repository;

import com.grupo7.renthotels.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> getByCategoryKind(@Param("kind") String kind);

    List<Product> getByCityCityDenomination(@Param("cityDenomination") String kind);

    Optional<Product> findByProductSku(Long productSku);
}
