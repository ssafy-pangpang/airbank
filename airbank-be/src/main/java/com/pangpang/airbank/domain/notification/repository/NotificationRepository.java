package com.pangpang.airbank.domain.notification.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.pangpang.airbank.domain.notification.domain.Notification;
import com.pangpang.airbank.domain.notification.dto.NotificationElement;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, Long> {
	@Query("{receiverId:  ?0, senderId:  ?1}")
	List<NotificationElement> findByReceiverIdAndSenderId(Long receiverId, Long senderId);
}
