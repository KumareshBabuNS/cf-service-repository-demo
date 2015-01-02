package io.pivotal.cfservicebroker.repository;

import java.util.List;

import io.pivotal.cfservicebroker.model.Plan;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

public interface PlanRepository extends CrudRepository<Plan, String> {
	public List<Plan> findByServiceName(String serviceName);
	public List<Plan> findByServiceName(String serviceName, Sort sort);
	public Plan findOneByNameAndServiceName(String name, String serviceName);
}
