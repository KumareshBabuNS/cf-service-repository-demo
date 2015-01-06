package io.pivotal.cfservicebroker.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

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


    @RequestMapping("/admin/service")
    public Iterable<Service> listServices() {
    	System.out.println("XXXXXXXXXXXXXXXXXXXXXX listServices XXXXXXXXXXXXXXXXXXXXXX");
    	return serviceRepository.findAll(new Sort("name"));
    }

    @RequestMapping(value = "/admin/service/{serviceName}", method = RequestMethod.GET)
    public Service getService(@PathVariable("serviceName") String serviceName) {
    	System.out.println("XXXXXXXXXXXXXXXXXXXXXX getService XXXXXXXXXXXXXXXXXXXXXX");
    	return serviceRepository.findOneByName(serviceName);
    }

    @RequestMapping(value = "/admin/service/{serviceName}", method = RequestMethod.DELETE)
    public void deleteService(@PathVariable("serviceName") String serviceName) {
    	System.out.println("XXXXXXXXXXXXXXXXXXXXXX deleteService XXXXXXXXXXXXXXXXXXXXXX");
    	//Delete the credentials
    	credentialRepository.delete(credentialRepository.findByServiceName(serviceName));
    	//Delete the plans
    	planRepository.delete(planRepository.findByServiceName(serviceName));
    	//Delete the service
    	serviceRepository.delete(serviceRepository.findOneByName(serviceName));
    }

    @RequestMapping(value = "/admin/service/{serviceName}", method = RequestMethod.PUT)
    public ResponseEntity<Service> storeService(@PathVariable("serviceName") String serviceName,
    											@RequestBody Service service) {
    	System.out.println("XXXXXXXXXXXXXXXXXXXXXX storeService XXXXXXXXXXXXXXXXXXXXXX");

    	if(service.getName() == null) {
			service.setName(serviceName);
		} else if(!service.getName().equals(serviceName)) {
			return new ResponseEntity<Service>(HttpStatus.CONFLICT);
		}

    	if(service.getId() == null) {
        	Service prevVersion = serviceRepository.findOneByName(serviceName);
        	if(prevVersion == null) {
        		service.setId(UUID.randomUUID().toString());
        	} else {
        		service.setId(prevVersion.getId());
        	}
    	}

    	return new ResponseEntity<Service>(serviceRepository.save(service), HttpStatus.OK);
    }

    @RequestMapping(value = "/admin/service/{serviceName}/plan", method = RequestMethod.GET)
    public List<Plan> listPlansForService(@PathVariable("serviceName") String serviceName) {
    	System.out.println("XXXXXXXXXXXXXXXXXXXXXX listPlansForService XXXXXXXXXXXXXXXXXXXXXX");
    	List<Plan> plans = planRepository.findByServiceName(serviceName, new Sort("name"));
    	for(Plan plan:plans) {
    		plan.setCredentials(credentialRepository.findByPlanNameAndServiceName(plan.getName(), serviceName, new Sort("key")));
    	}

    	return plans;
    }

    @RequestMapping(value = "/admin/service/{serviceName}/plan/{planName}", method = RequestMethod.GET)
    public Plan getPlan(@PathVariable("serviceName") String serviceName,
    						@PathVariable("planName") String planName) {
    	System.out.println("XXXXXXXXXXXXXXXXXXXXXX getPlan XXXXXXXXXXXXXXXXXXXXXX");
    	Plan result = planRepository.findOneByNameAndServiceName(planName, serviceName);
    	if(result != null) {
    		result.setCredentials(credentialRepository.findByPlanNameAndServiceName(result.getName(), serviceName, new Sort("key")));
    	}
    	return result;
    }

    @RequestMapping(value = "/admin/service/{serviceName}/plan/{planName}/credential", method = RequestMethod.GET)
    public Iterable<Credential> listCredentials(@PathVariable("serviceName") String serviceName,
    												@PathVariable("planName") String planName) {
    	System.out.println("XXXXXXXXXXXXXXXXXXXXXX listCredentials XXXXXXXXXXXXXXXXXXXXXX");
    	return credentialRepository.findByPlanNameAndServiceName(planName, serviceName, new Sort("key"));
    }

    @RequestMapping(value = "/admin/service/{serviceName}/plan/{planName}/serviceinstance", method = RequestMethod.GET)
    public Iterable<ServiceInstance> listServiceInstances(@PathVariable("serviceName") String serviceName,
    												        @PathVariable("planName") String planName) {
    	System.out.println("XXXXXXXXXXXXXXXXXXXXXX listServiceInstances XXXXXXXXXXXXXXXXXXXXXX");
    	Service service = serviceRepository.findOneByName(serviceName);
    	Plan plan = planRepository.findOneByNameAndServiceName(planName, serviceName);
    	if(plan == null || service == null) {
    		return new ArrayList<ServiceInstance>(0);
    	} else {
    		return serviceInstanceRepository.findByPlanIdAndServiceId(plan.getId(), service.getId());
    	}
    }

    @RequestMapping(value = "/admin/service/{serviceName}/plan/{planName}/servicebinding", method = RequestMethod.GET)
    public Iterable<ServiceBinding> listServiceBindings(@PathVariable("serviceName") String serviceName,
    												        @PathVariable("planName") String planName) {
    	System.out.println("XXXXXXXXXXXXXXXXXXXXXX listServiceBindings XXXXXXXXXXXXXXXXXXXXXX");
    	Service service = serviceRepository.findOneByName(serviceName);
    	Plan plan = planRepository.findOneByNameAndServiceName(planName, serviceName);
    	if(plan == null || service == null) {
    		return new ArrayList<ServiceBinding>(0);
    	} else {
    		return serviceBindingRepository.findByPlanIdAndServiceId(plan.getId(), service.getId());
    	}
    }

    @RequestMapping(value = "/admin/service/{serviceName}/plan/{planName}", method = RequestMethod.DELETE)
    public void deletePlan(@PathVariable("serviceName") String serviceName,
							@PathVariable("planName") String planName) {
    	System.out.println("XXXXXXXXXXXXXXXXXXXXXX deletePlan XXXXXXXXXXXXXXXXXXXXXX");

    	//Delete the credentials
    	credentialRepository.delete(credentialRepository.findByPlanNameAndServiceName(planName, serviceName));
    	//Delete the plan
    	planRepository.delete(planRepository.findOneByNameAndServiceName(planName, serviceName));
    }

    @RequestMapping(value = "/admin/service/{serviceName}/plan/{planName}", method = RequestMethod.PUT)
    public ResponseEntity<Plan> storePlan(@PathVariable("serviceName") String serviceName,
    						@PathVariable("planName") String planName,
    						@RequestBody Plan plan) {
    	System.out.println("XXXXXXXXXXXXXXXXXXXXXX storePlan XXXXXXXXXXXXXXXXXXXXXX");
    	if(serviceRepository.findOneByName(serviceName) == null) {
    		//Service doesn't exist
			return new ResponseEntity<Plan>(HttpStatus.CONFLICT);
    	}

    	if(plan.getName() == null) {
    		plan.setName(planName);
		} else if(!plan.getName().equals(planName)) {
			//Mismatch between JSON input and URL
			return new ResponseEntity<Plan>(HttpStatus.CONFLICT);
		}

    	if(plan.getServiceName() == null) {
    		plan.setServiceName(serviceName);
		} else if(!plan.getServiceName().equals(serviceName)) {
			//Mismatch between JSON input and URL
			return new ResponseEntity<Plan>(HttpStatus.CONFLICT);
		}

    	List<Credential> newCredentials = plan.getCredentials();
    	for(Credential newCredential:newCredentials) {
    		if(newCredential.getServiceName() == null) {
    			newCredential.setServiceName(serviceName);
    		} else if(!newCredential.getServiceName().equals(serviceName)) {
    			//Mismatch between JSON input and URL
    			return new ResponseEntity<Plan>(HttpStatus.CONFLICT);
    		}
    		if(newCredential.getPlanName() == null) {
    			newCredential.setPlanName(planName);
    		} else if(!newCredential.getPlanName().equals(planName)) {
    			//Mismatch between JSON input and URL
    			return new ResponseEntity<Plan>(HttpStatus.CONFLICT);
    		}
    		newCredential.setId(UUID.randomUUID().toString());
    	}

    	Plan existingPlan = planRepository.findOneByNameAndServiceName(planName, serviceName);
    	if(existingPlan == null) {
        	plan.setId(UUID.randomUUID().toString());
    	} else {
    		plan.setId(existingPlan.getId());
    	}

    	//Store plan
    	planRepository.save(plan);
    	//Make sure the credentials are stored as well!
    	//First delete existing credentials
    	credentialRepository.delete(credentialRepository.findByPlanNameAndServiceName(planName, serviceName));
    	//Then store the new ones
    	credentialRepository.save(newCredentials);

    	return new ResponseEntity<Plan>(getPlan(serviceName, planName), HttpStatus.OK);
    }
}
