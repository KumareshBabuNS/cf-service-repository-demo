package io.pivotal.cfservicebroker.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "services")
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class Service {
    @Id
    private String id;

    @Column(nullable = false)
    @JsonSerialize
    private String name;

    @Column(nullable = false)
    @JsonSerialize
    private String description;

    @Column(nullable = false)
    @JsonSerialize
    private boolean bindable;

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

    public boolean isBindable() {
        return bindable;
    }

    public void setBindable(boolean bindable) {
        this.bindable = bindable;
    }


    public String toString() {
    	return "Service(id: "+id+" name: " + name + " description: " + description + " bindable: " + bindable + ")";
    }


}
