package com.yroots.lambda.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.yroots.lambda.domain.SMSAccount;

@RepositoryRestResource(collectionResourceRel = "sms_accounts", path = "sms_accounts")
public interface SmsAccountRepository extends PagingAndSortingRepository<SMSAccount,String> {

}
