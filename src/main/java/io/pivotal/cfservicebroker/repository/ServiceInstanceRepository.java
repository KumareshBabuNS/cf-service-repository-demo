package io.pivotal.cfservicebroker.repository;

import java.util.List;

import io.pivotal.cfservicebroker.model.ServiceInstance;

import org.springframework.data.repository.CrudRepository;

public interface ServiceInstanceRepository extends CrudRepository<ServiceInstance, String> {
	public List<ServiceInstance> findByPlanIdAndServiceId(String planId, String serviceId);
}
