package io.pivotal.cfservicebroker.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "credentials")
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class Credential {
    @Id
    private String id;

    @Column(nullable = false)
    @JsonSerialize
    private String planName;

    @Column(nullable = false)
    @JsonSerialize
    private String serviceName;

    @Column(nullable = false)
    @JsonSerialize
    private String key;

    @Column(nullable = false)
    @JsonSerialize
    private String value;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
    
    
}
