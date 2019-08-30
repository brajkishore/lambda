package com.yroots.lambda.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.yroots.lambda.domain.EmailAccount;

@RepositoryRestResource(collectionResourceRel = "email_accounts", path = "email_accounts")
public interface EmailAccountRepository extends PagingAndSortingRepository<EmailAccount,String> {

}
