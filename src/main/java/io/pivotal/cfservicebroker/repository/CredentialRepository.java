package io.pivotal.cfservicebroker.repository;

import java.util.List;

import io.pivotal.cfservicebroker.model.Credential;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

public interface CredentialRepository extends CrudRepository<Credential, String> {
	public List<Credential> findByPlanNameAndServiceName(String planName, String serviceName);
	public List<Credential> findByPlanNameAndServiceName(String planName, String serviceName, Sort sort);
	public List<Credential> findByServiceName(String serviceName);
}
