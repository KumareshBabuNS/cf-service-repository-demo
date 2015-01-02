package io.pivotal.cfservicebroker.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "plans")
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class Plan {

    @Id
    private String id;

    @Column(nullable = false)
    @JsonSerialize
    private String serviceName;
    
    @Column(nullable = false)
    @JsonSerialize
    private String name;

    @Column(nullable = false)
    @JsonSerialize
    private String description;
    
    @Transient
    @JsonSerialize
    private Map<Object, Object> credentials;
    
    @Transient
    @JsonSerialize
    private List<Credential> credentialList;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public List<Credential> getCredentialList() {
		return credentialList;
	}

	public void setCredentialList(List<Credential> credentialList) {
		this.credentialList = credentialList;
	}
	
	public Map<Object, Object> getCredentials() {
		return credentials;
	}

	public void setCredentials(Map<Object, Object> credentials) {
		this.credentials = credentials;
	}

	public void setCredentials(List<Credential> credentialList) {
		credentials = new HashMap<>();
		for(Credential c:credentialList) {
			credentials.put(c.getKey(), c.getValue());
		}
	}
	
	public String toString() {
    	return "Plan(id: "+id+ " serviceId: " + serviceName + " name: " + name + " description: " + description + ")";
    }
}
