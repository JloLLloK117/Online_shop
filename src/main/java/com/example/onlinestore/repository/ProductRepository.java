package com.example.onlinestore.repository;

import com.example.onlinestore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findByCategory(String category);
    List<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);

    List<Product> findByNameContainingIgnoreCase(String name);
    @Query("SELECT p FROM Product p " +
            "WHERE p.category = :category " +
            "AND (UPPER(p.name) LIKE UPPER(CONCAT('%', :query, '%')) " +
            "OR UPPER(p.description) LIKE UPPER(CONCAT('%', :query, '%')))")
    List<Product> searchByCategoryAndQuery(
            @Param("category") String category,
            @Param("query") String query
    );

}
