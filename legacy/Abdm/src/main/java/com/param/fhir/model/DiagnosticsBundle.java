package com.param.fhir.model;

import java.util.ArrayList;
import java.util.List;

public class DiagnosticsBundle {
	  private String resourceType;
	  private String id;
	  Meta MetaObject;
	  MyIdentifier IdentifierObject;
	  private String type;
	  private String timestamp;
	  private List<EntryObject> entry;
	  Signature SignatureObject;


	  public MyIdentifier getIdentifierObject() {
		return IdentifierObject;
	}

	public void setIdentifierObject(MyIdentifier identifierObject) {
		IdentifierObject = identifierObject;
	}

	public String getResourceType() {
	    return resourceType;
	  }

	  public String getId() {
	    return id;
	  }

	  public Meta getMeta() {
	    return MetaObject;
	  }


	  public String getType() {
	    return type;
	  }

	  public String getTimestamp() {
	    return timestamp;
	  }

	  public Signature getSignature() {
	    return SignatureObject;
	  }

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


	  public void setType( String type ) {
	    this.type = type;
	  }

	  public void setTimestamp( String timestamp ) {
	    this.timestamp = timestamp;
	  }

	  public void setSignature( Signature signatureObject ) {
	    this.SignatureObject = signatureObject;
	  }

	public List<EntryObject> getEntry() {
		return entry;
	}

	public void setEntry(List<EntryObject> entry) {
		this.entry = entry;
	}
	  
	  
	}

