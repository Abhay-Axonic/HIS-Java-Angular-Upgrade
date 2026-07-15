package com.param.fhir.model;

import java.util.ArrayList;
import java.util.List;

public class Meta {

	  private String versionId;
	  private String lastUpdated;
	  private List<String> profile = new ArrayList<>();
	  private List<SecurityNew> security;


	 // Getter Methods 

	  public String getVersionId() {
	    return versionId;
	  }

	  public String getLastUpdated() {
	    return lastUpdated;
	  }

	 // Setter Methods 

	  public void setVersionId( String versionId ) {
	    this.versionId = versionId;
	  }

	  public void setLastUpdated( String lastUpdated ) {
	    this.lastUpdated = lastUpdated;
	  }

	public List<String> getProfile() {
		return profile;
	}

	public void setProfile(List<String> profile) {
		this.profile = profile;
	}

	public List<SecurityNew> getSecurity() {
		return security;
	}

	public void setSecurity(List<SecurityNew> security) {
		this.security = security;
	}

	


	
}
