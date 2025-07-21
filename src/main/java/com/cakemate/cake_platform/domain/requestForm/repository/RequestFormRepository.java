package com.cakemate.cake_platform.domain.requestForm.repository;

import com.cakemate.cake_platform.domain.requestForm.entity.RequestForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RequestFormRepository extends JpaRepository<RequestForm, Long> {

    Page<RequestForm> findByRegion(String region, Pageable pageable);
    Optional<RequestForm> findByIdAndIsDeletedFalse(Long id);
    List<RequestForm> findAllByIsDeletedFalse();
}
