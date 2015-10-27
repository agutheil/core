package com.mightymerce.core.repository;

import com.mightymerce.core.domain.CustomerOrder;
import com.mightymerce.core.domain.enumeration.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Spring Data JPA repository for the CustomerOrder entity.
 */
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder,Long> {

    @Query("select customerOrder from CustomerOrder customerOrder where customerOrder.user.login = ?#{principal.username}")
    List<CustomerOrder> findByUserIsCurrentUser();

    Page<CustomerOrder> findByUserId(Long id, Pageable pageable);

    List<CustomerOrder> findByUserIdAndOrderStatus(Long id, OrderStatus orderStatus, Sort sort);

    // Order Count

    @Query(value = "" +
        " SELECT count(id) as orderCount " +
        " FROM customerorder co " +
        " where co.user_id = :userId " +
        " and co.placed_on between subdate(current_date , 1) and current_date ", nativeQuery = true)
    Long getOrderCountPastDay (@Param("userId") Long userId);

    @Query(value = "" +
        " SELECT count(id) as orderCount " +
        " FROM customerorder co " +
        " where co.user_id = :userId " +
        " and co.placed_on >= date(current_date) - interval 7 day ", nativeQuery = true)
    Long getOrderCountPast7Day (@Param("userId") Long userId);

    @Query(value = "" +
        " SELECT count(id) as orderCount " +
        " FROM customerorder co " +
        " where co.user_id = :userId " +
        " and year(co.placed_on) = year(current_date) and month(co.placed_on) = month(current_date) ", nativeQuery = true)
    Long getOrderCountCurrentMonth (@Param("userId") Long userId);

    @Query(value = "" +
        " SELECT count(id) as orderCount " +
        " FROM customerorder co " +
        " where co.user_id = :userId " +
        " and co.placed_on between :startDate and :endDate ", nativeQuery = true)
    Long getOrderCountCustom (@Param("userId") Long userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    // Average Cart

    @Query(value = "" +
        " SELECT avg(total_amount) as averageCart " +
        " FROM customerorder co " +
        " where co.user_id = :userId " +
        " and co.placed_on between subdate(current_date , 1) and current_date ", nativeQuery = true)
    Double getAverageCartPastDay (@Param("userId") Long userId);

    @Query(value = "" +
        " SELECT avg(total_amount) as averageCart " +
        " FROM customerorder co " +
        " where co.user_id = :userId " +
        " and co.placed_on >= date(current_date) - interval 7 day ", nativeQuery = true)
    Double getAverageCartPast7Day (@Param("userId") Long userId);

    @Query(value = "" +
        " SELECT avg(total_amount) as averageCart " +
        " FROM customerorder co " +
        " where co.user_id = :userId " +
        " and year(co.placed_on) = year(current_date) and month(co.placed_on) = month(current_date) ", nativeQuery = true)
    Double getAverageCartCurrentMonth (@Param("userId") Long userId);

    @Query(value = "" +
        " SELECT avg(total_amount) as averageCart " +
        " FROM customerorder co " +
        " where co.user_id = :userId " +
        " and co.placed_on between :startDate and :endDate ", nativeQuery = true)
    Double getAverageCartCustom (@Param("userId") Long userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    // Revenue

    @Query(value = "" +
        " SELECT sum(total_amount) as revenue " +
        " FROM customerorder co " +
        " where co.user_id = :userId " +
        " and payment_status = 'Paid' " +
        " and co.placed_on between subdate(current_date , 1) and current_date ", nativeQuery = true)
    Double getRevenuePastDay (@Param("userId") Long userId);

    @Query(value = "" +
        " SELECT sum(total_amount) as revenue " +
        " FROM customerorder co " +
        " where co.user_id = :userId " +
        " and payment_status = 'Paid' " +
        " and co.placed_on >= date(current_date) - interval 7 day ", nativeQuery = true)
    Double getRevenuePast7Day (@Param("userId") Long userId);

    @Query(value = "" +
        " SELECT sum(total_amount) as revenue " +
        " FROM customerorder co " +
        " where co.user_id = :userId " +
        " and payment_status = 'Paid' " +
        " and year(co.placed_on) = year(current_date) and month(co.placed_on) = month(current_date) ", nativeQuery = true)
    Double getRevenueCurrentMonth (@Param("userId") Long userId);

    @Query(value = "" +
        " SELECT sum(total_amount) as revenue " +
        " FROM customerorder co " +
        " where co.user_id = :userId " +
        " and payment_status = 'Paid' " +
        " and co.placed_on between :startDate and :endDate ", nativeQuery = true)
    Double getRevenueCustom (@Param("userId") Long userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    // Sales Volume

    @Query(value = "" +
        " SELECT sum(total_amount) as salesVolume " +
        " FROM customerorder co " +
        " where co.user_id = :userId " +
        " and payment_status = 'Paid' " +
        " and co.placed_on between subdate(current_date , 1) and current_date ", nativeQuery = true)
    Double getSalesVolumePastDay (@Param("userId") Long userId);

    @Query(value = "" +
        " SELECT sum(total_amount) as salesVolume " +
        " FROM customerorder co " +
        " where co.user_id = :userId " +
        " and payment_status = 'Paid' " +
        " and co.placed_on >= date(current_date) - interval 7 day ", nativeQuery = true)
    Double getSalesVolumePast7Day (@Param("userId") Long userId);

    @Query(value = "" +
        " SELECT sum(total_amount) as salesVolume " +
        " FROM customerorder co " +
        " where co.user_id = :userId " +
        " and payment_status = 'Paid' " +
        " and year(co.placed_on) = year(current_date) and month(co.placed_on) = month(current_date) ", nativeQuery = true)
    Double getSalesVolumeCurrentMonth (@Param("userId") Long userId);

    @Query(value = "" +
        " SELECT sum(total_amount) as salesVolume " +
        " FROM customerorder co " +
        " where co.user_id = :userId " +
        " and payment_status = 'Paid' " +
        " and co.placed_on between :startDate and :endDate ", nativeQuery = true)
    Double getSalesVolumeCustom (@Param("userId") Long userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    // Return Volume

    @Query(value = "" +
        " SELECT sum(total_amount) as returnVolume " +
        " FROM customerorder co " +
        " where co.user_id = :userId " +
        " and order_status = 'Returned' " +
        " and co.placed_on between subdate(current_date , 1) and current_date ", nativeQuery = true)
    Double getReturnVolumePastDay (@Param("userId") Long userId);

    @Query(value = "" +
        " SELECT sum(total_amount) as returnVolume " +
        " FROM customerorder co " +
        " where co.user_id = :userId " +
        " and order_status = 'Returned' " +
        " and co.placed_on >= date(current_date) - interval 7 day ", nativeQuery = true)
    Double getReturnVolumePast7Day (@Param("userId") Long userId);

    @Query(value = "" +
        " SELECT sum(total_amount) as returnVolume " +
        " FROM customerorder co " +
        " where co.user_id = :userId " +
        " and order_status = 'Returned' " +
        " and year(co.placed_on) = year(current_date) and month(co.placed_on) = month(current_date) ", nativeQuery = true)
    Double getReturnVolumeCurrentMonth (@Param("userId") Long userId);

    @Query(value = "" +
        " SELECT sum(total_amount) as returnVolume " +
        " FROM customerorder co " +
        " where co.user_id = :userId " +
        " and order_status = 'Returned' " +
        " and co.placed_on between :startDate and :endDate ", nativeQuery = true)
    Double getReturnVolumeCustom (@Param("userId") Long userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
