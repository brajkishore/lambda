package com.yroots.lambda.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.yroots.lambda.domain.Category;

import io.swagger.annotations.Api;

@Api
@RepositoryRestResource(collectionResourceRel = "categories", path = "categories")
public interface CategoryRepository extends PagingAndSortingRepository<Category,String> {

}
