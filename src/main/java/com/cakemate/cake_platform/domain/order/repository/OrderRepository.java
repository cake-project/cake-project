package com.cakemate.cake_platform.domain.order.repository;

import com.cakemate.cake_platform.domain.order.entity.Order;
import com.cakemate.cake_platform.domain.proposalForm.entity.ProposalForm;
import com.cakemate.cake_platform.domain.store.ranking.dto.StoreOrderCount;
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

    Optional<Order> findByOrderNumber(String orderNumber);

    @Query("select new com.cakemate.cake_platform.domain.store.ranking.dto.StoreOrderCount(s.id, o.storeName, count(o))" +
            "from Order o " +
            "join o.store s " +
            "where o.createdAt >= :startDate " +
            "group by s.id, o.storeName " +
            "order by count(o) desc"
    )
    List<StoreOrderCount> findWeeklyTopStores(@Param("startDate") LocalDateTime startDate);
}
