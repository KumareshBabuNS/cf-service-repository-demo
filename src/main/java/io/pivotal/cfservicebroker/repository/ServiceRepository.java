package io.pivotal.cfservicebroker.repository;

import io.pivotal.cfservicebroker.model.Service;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface ServiceRepository extends PagingAndSortingRepository<Service, String> {
	public Service findOneByName(String name);
}
