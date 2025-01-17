package com.itemManagement.repository;

import com.itemManagement.entity.Item;
import com.itemManagement.entity.User;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {

    Page<Item> findAll(Pageable pageable);
    Page<Item> findAllByStatusTrue(Pageable pageable);
    List<Item> findAllByStatusTrue();
    Page<Item> findByNameContainingIgnoreCaseAndStatusTrue(String name,Pageable pageable);
    Page<Item> findByPriceAndStatusTrue(BigDecimal price, Pageable pageable);
    Page<Item> findByDescriptionContainingIgnoreCaseAndStatusTrue(String keyword, Pageable pageable);
    Optional<Item>  findByIdAndStatusTrue(Long id);





}
