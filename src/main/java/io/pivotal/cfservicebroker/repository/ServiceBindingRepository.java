package io.pivotal.cfservicebroker.repository;

import java.util.List;

import io.pivotal.cfservicebroker.model.ServiceBinding;

import org.springframework.data.repository.CrudRepository;

public interface ServiceBindingRepository extends CrudRepository<ServiceBinding, String> {
	public List<ServiceBinding> findByPlanIdAndServiceId(String planId, String serviceId);
}
