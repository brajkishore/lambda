package com.yroots.lambda.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.yroots.lambda.domain.ServiceAccount;

@RepositoryRestResource(collectionResourceRel = "service_accounts", path = "service_accounts")
public interface ServiceAccountRepository extends PagingAndSortingRepository<ServiceAccount,String> {

}
