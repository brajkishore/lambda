package com.yroots.lambda.dao;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.yroots.lambda.domain.Subscription;

@RepositoryRestResource(collectionResourceRel = "subscriptions", path = "subscriptions")
public interface SubscriptionRepository extends PagingAndSortingRepository<Subscription,String> {

	List<Subscription> findAllByTopicName(String topicName);
}
