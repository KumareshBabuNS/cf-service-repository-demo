package io.pivotal.cfservicebroker.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.pivotal.cfservicebroker.model.Credential;
import io.pivotal.cfservicebroker.model.Plan;
import io.pivotal.cfservicebroker.model.Service;
import io.pivotal.cfservicebroker.model.ServiceBinding;
import io.pivotal.cfservicebroker.model.ServiceInstance;
import io.pivotal.cfservicebroker.repository.CredentialRepository;
import io.pivotal.cfservicebroker.repository.PlanRepository;
import io.pivotal.cfservicebroker.repository.ServiceBindingRepository;
import io.pivotal.cfservicebroker.repository.ServiceInstanceRepository;
import io.pivotal.cfservicebroker.repository.ServiceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceController {

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    PlanRepository planRepository;

    @Autowired
    CredentialRepository credentialRepository;

    @Autowired
    ServiceInstanceRepository serviceInstanceRepository;

    @Autowired
    ServiceBindingRepository serviceBindingRepository;

    @RequestMapping("/v2/catalog")
    public Map<String, List<Map<String, Object>>> catalog() {
    	System.out.println("XXXXXXXXXXXXXXXXXXXXXX catalog XXXXXXXXXXXXXXXXXXXXXX");
        
    	Iterable<Service> services = serviceRepository.findAll(new Sort("name"));
    	List<Map<String, Object>> jsonServices = new ArrayList<>();
    	for(Service s:services) {
    		List<Plan> plans = planRepository.findByServiceName(s.getName(), new Sort("name"));
    		List<Map<String, Object>> jsonPlans = new ArrayList<Map<String, Object>>();
    		for(Plan p:plans) {
    			Map<String, Object> jsonAttributes = new HashMap<>();
    			jsonAttributes.put("id", p.getId());
    			jsonAttributes.put("name", p.getName());
    			jsonAttributes.put("description", p.getDescription());

        		Map<String, String> metadata = new HashMap<>();
        		jsonAttributes.put("metadata", metadata);
    			
    			jsonPlans.add(jsonAttributes);
    		}
    		Map<String, Object> jsonAttributes = new HashMap<>();
    		jsonAttributes.put("id", s.getId());
    		jsonAttributes.put("name", s.getName());
    		jsonAttributes.put("description", s.getDescription());
    		jsonAttributes.put("bindable", s.isBindable());
    		jsonAttributes.put("plans", jsonPlans);

    		Map<String, String> metadata = new HashMap<>();
    		jsonAttributes.put("metadata", metadata);
    		
    		jsonServices.add(jsonAttributes);
    	}
    	
    	Map<String, List<Map<String, Object>>> result = new HashMap<>();
    	result.put("services", jsonServices);
    	return result;
    }
    
    @RequestMapping(value = "/v2/service_instances/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> createServiceInstance(@PathVariable("id") String id, 
    									                @RequestBody ServiceInstance serviceInstance) {
    	System.out.println("XXXXXXXXXXXXXXXXXXXXXX createServiceInstance XXXXXXXXXXXXXXXXXXXXXX");

    	if(!serviceRepository.exists(serviceInstance.getServiceId())) {
    		//Service ID didn't match
    		return new ResponseEntity<>("{\"description\":\"Service Repository " + serviceInstance.getServiceId() + " does not exist!\"}", HttpStatus.BAD_REQUEST);
    	}
    	if(!planRepository.exists(serviceInstance.getPlanId())) {
    		//Plan ID didn't match
    		return new ResponseEntity<>("{\"description\":\"Plan " + serviceInstance.getPlanId() + " does not exist!\"}", HttpStatus.BAD_REQUEST);
    	}
    	
    	serviceInstance.setId(id);

        boolean exists = serviceInstanceRepository.exists(id);
        if (exists) {
        	ServiceInstance existing = serviceInstanceRepository.findOne(id);
            if (existing.equals(serviceInstance)) {
                return new ResponseEntity<>("{}", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("{}", HttpStatus.CONFLICT);
            }
        } else {
            serviceInstanceRepository.save(serviceInstance);
            return new ResponseEntity<>("{}", HttpStatus.CREATED);
        }
    }

    @RequestMapping(value = "/v2/service_instances/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteServiceInstance(@PathVariable("id") String id,
                                                        @RequestParam("service_id") String serviceId,
                                                        @RequestParam("plan_id") String planId) {
    	System.out.println("XXXXXXXXXXXXXXXXXXXXXX deleteServiceInstance XXXXXXXXXXXXXXXXXXXXXX");
    	if(serviceInstanceRepository.exists(id)) {
    		serviceInstanceRepository.delete(id);
            return new ResponseEntity<>("{}", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("{}", HttpStatus.GONE);
        }
    }
    
    @RequestMapping(value = "/v2/service_instances/{serviceInstanceId}/service_bindings/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Object> createBinding(@PathVariable("serviceInstanceId") String serviceInstanceId,
                                                @PathVariable("id") String id,
                                                @RequestBody ServiceBinding serviceBinding) {
    	System.out.println("XXXXXXXXXXXXXXXXXXXXXX createBinding XXXXXXXXXXXXXXXXXXXXXX");

        if (!serviceInstanceRepository.exists(serviceInstanceId)) {
            return new ResponseEntity<Object>("{\"description\":\"Service instance " + serviceInstanceId + " does not exist!\"", HttpStatus.BAD_REQUEST);
        }

        serviceBinding.setId(id);
        serviceBinding.setServiceInstanceId(serviceInstanceId);

        if (serviceBindingRepository.exists(id)) {
        	ServiceBinding existing = serviceBindingRepository.findOne(id);
            if (!existing.equals(serviceBinding)) {
            	return new ResponseEntity<Object>("{}", HttpStatus.CONFLICT);
            }
        } else {
            serviceBindingRepository.save(serviceBinding);
        }

        Plan plan = planRepository.findOne(serviceBinding.getPlanId());
        List<Credential> credentialKeys = credentialRepository.findByPlanNameAndServiceName(plan.getName(), plan.getServiceName());
        return new ResponseEntity<Object>(wrapCredentials(credentialKeys), HttpStatus.CREATED);
    }

    private Map<String, Object> wrapCredentials(List<Credential> credentialKeys) {
    	Map<String, String> credentials = new HashMap<>();
    	for(Credential ck:credentialKeys) {
    		credentials.put(ck.getKey(), ck.getValue());
    	}
    	
        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("credentials", credentials);
        return wrapper;
    }

    @RequestMapping(value = "/v2/service_instances/{serviceInstanceId}/service_bindings/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteBinding(@PathVariable("serviceInstanceId") String serviceInstanceId,
                                                @PathVariable("id") String id,
                                                @RequestParam("service_id") String serviceId,
                                                @RequestParam("plan_id") String planId) {
    	System.out.println("XXXXXXXXXXXXXXXXXXXXXX deleteBinding XXXXXXXXXXXXXXXXXXXXXX");

        if (serviceBindingRepository.exists(id)) {
            serviceBindingRepository.delete(id);
            return new ResponseEntity<>("{}", HttpStatus.OK);
        } else {
        	return new ResponseEntity<>("{}", HttpStatus.GONE);
        }
    }
}
