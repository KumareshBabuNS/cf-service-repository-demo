package io.pivotal.cfservicebroker.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;

@Entity
@Table(name = "service_bindings")
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
public class ServiceBinding {

    @Id
    private String id;

    @Column(nullable = false)
    private String serviceInstanceId;

    @JsonSerialize
    @JsonProperty("service_id")
    @Column(nullable = false)
    private String serviceId;

    @JsonSerialize
    @JsonProperty("plan_id")
    @Column(nullable = false)
    private String planId;

    @JsonSerialize
    @JsonProperty("app_guid")
    @Column(nullable = false)
    private String appGuid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceInstanceId() {
        return serviceInstanceId;
    }

    public void setServiceInstanceId(String serviceInstanceId) {
        this.serviceInstanceId = serviceInstanceId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getAppGuid() {
        return appGuid;
    }

    public void setAppGuid(String appGuid) {
        this.appGuid = appGuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceBinding that = (ServiceBinding) o;

        if (!appGuid.equals(that.appGuid)) return false;
        if (!id.equals(that.id)) return false;
        if (!serviceInstanceId.equals(that.serviceInstanceId)) return false;
        if (!planId.equals(that.planId)) return false;
        if (!serviceId.equals(that.serviceId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + serviceInstanceId.hashCode();
        result = 31 * result + serviceId.hashCode();
        result = 31 * result + planId.hashCode();
        result = 31 * result + appGuid.hashCode();
        return result;
    }
    
    public String toString() {
    	return "ServiceBinding(id: "+id+" instanceid: " + serviceInstanceId + " serviceid: " + serviceId + " planid: " + planId + "appguid: " + appGuid + ")";
    }

}
