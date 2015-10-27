package com.mightymerce.core.repository;

import com.mightymerce.core.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * Spring Data JPA repository for the Product entity.
 */
public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query("select product from Product product where product.user.login = ?#{principal.username}")
    List<Product> findByUserIsCurrentUser();

    Page<Product> findByUserId(Long id, Pageable pageable);

    @Query(value = "" +
        " SELECT co.product_id as id " +
        "     , p.title as title " +
        "     , count(co.product_id) as totalSale" +
        "     , p.main_image as mainImage " +
        " FROM customerorder co " +
        " left join product p on p.id = co.product_id " +
        " where p.user_id = :userId " +
        "     and co.user_id = :userId " +
        " group by co.product_id " +
        " order by totalSale desc " +
        "     , title", nativeQuery = true)
    List<Map<String, Object>> getProductsSold(@Param("userId") Long userId);

}
