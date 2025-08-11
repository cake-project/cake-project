package com.cakemate.cake_platform.domain.order.repository;

import com.cakemate.cake_platform.domain.order.entity.Order;
import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import com.cakemate.cake_platform.domain.store.ranking.dto.StoreRankingResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // 소비자의 주문 목록 조회에서 사용합니다.
    Page<Order> findByRequestFormCustomerId(Long customerId, Pageable pageable);

    // 소비자의 주문 상세 조회에서 사용합니다.
    Optional<Order> findByCustomerIdAndId(Long customerId, Long orderId);

    // 가게의 주문 목록 조회에서 사용합니다.
    Page<Order> findByStoreId(Long storeId, Pageable pageable);

    // 가게의 주문 상세 조회에서 사용합니다.
    Optional<Order> findByStoreIdAndId(Long storeId, Long OrderId);

    // 이미 주문이 생성된 견적서인지 확인할 때 사용합니다.
    boolean existsByProposalForm(ProposalForm proposalForm);

    @Query(value = """
        SELECT 
            DENSE_RANK() OVER (ORDER BY COUNT(o.id) DESC) AS store_rank,
            s.id AS storeId,
            s.name AS storeName,
            COUNT(o.id) AS orderCount
        FROM orders o
        JOIN stores s ON o.store_id = s.id
        WHERE o.created_at >= :startDate
        GROUP BY s.id, s.name
        ORDER BY orderCount DESC
        LIMIT 10
        """, nativeQuery = true)
    List<StoreRankingResponseDto> findWeeklyTopStores(@Param("startDate") LocalDateTime startDate);
}
