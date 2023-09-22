package com.pangpang.airbank.domain.notification.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.pangpang.airbank.domain.notification.domain.NotificationGroup;

@Repository
public interface NotificationGroupRepository extends MongoRepository<NotificationGroup, String> {
}
