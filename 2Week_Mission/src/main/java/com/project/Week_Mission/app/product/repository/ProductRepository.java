package com.project.Week_Mission.app.product.repository;

import com.project.Week_Mission.app.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByOrderByIdDesc();

//    List<Product> findAllByMemberIdAndProductIdOrderByProduct_idDesc(long memberId, long productId);

//    List<Product> findProductsByMemberId(long memberId);

    Optional<Product> findProductByAuthorId(long authorId);
}
