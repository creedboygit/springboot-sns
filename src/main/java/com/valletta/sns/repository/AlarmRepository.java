package com.valletta.sns.repository;

import com.valletta.sns.model.entity.AlarmEntity;
import com.valletta.sns.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<AlarmEntity, Integer> {

    Page<AlarmEntity> findAllByUser(UserEntity user, Pageable pageable);
}
