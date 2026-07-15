package com.param.fhir.model;

import java.util.ArrayList;
import java.util.List;

public class OPConsultationBundle {

	  private String resourceType;
	  private String id;
	  Meta MetaObject;
	  MyIdentifier IdentifierObject;
	  private String type;
	  private String timestamp;
	  private List<FhirData.Entry>  entry ;
	  private com.param.fhir.model.FhirData.Signature signature;
	 // Signature SignatureObject;


	 // Getter Methods 

	  public String getResourceType() {
	    return resourceType;
	  }

	  public String getId() {
	    return id;
	  }

	  public Meta getMeta() {
	    return MetaObject;
	  }

	  public MyIdentifier getIdentifier() {
	    return IdentifierObject;
	  }

	  public String getType() {
	    return type;
	  }

	  public String getTimestamp() {
	    return timestamp;
	  }

	/*
	 * public Signature getSignature() { return SignatureObject; }
	 */

	 // Setter Methods 

	  public void setResourceType( String resourceType ) {
	    this.resourceType = resourceType;
	  }

	  public void setId( String id ) {
	    this.id = id;
	  }

	  public void setMeta( Meta metaObject ) {
	    this.MetaObject = metaObject;
	  }

	  public void setIdentifier( MyIdentifier identifierObject ) {
	    this.IdentifierObject = identifierObject;
	  }

	  public void setType( String type ) {
	    this.type = type;
	  }

	  public void setTimestamp( String timestamp ) {
	    this.timestamp = timestamp;
	  }

	public com.param.fhir.model.FhirData.Signature getSignature() {
		return signature;
	}

	public void setSignature(com.param.fhir.model.FhirData.Signature signature) {
		this.signature = signature;
	}

	public List<FhirData.Entry> getEntry() {
		return entry;
	}

	public void setEntry(List<FhirData.Entry> entry) {
		this.entry = entry;
	}


	/*
	 * public void setSignature( Signature signatureObject ) { this.SignatureObject
	 * = signatureObject; }
	 */
	  
	  
	}
	
