package com.project.Week_Mission.app.cart.repository;

import com.project.Week_Mission.app.cart.entity.CartItem;
import com.project.Week_Mission.app.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findCartItemListByMemberIdAndProductId(long memberId, long productId);

    Optional<CartItem> findCartItemByMemberIdAndProductId(long memberId, long productId);


    List<CartItem> findAllByMemberId(long memberId);
}
