package com.param.fhir.model;

import java.util.ArrayList;

public class Signature {

	  ArrayList<Object> type = new ArrayList<Object>();
	  private String when;
	  Who WhoObject;
	  private String sigFormat;
	  private String data;


	 // Getter Methods 

	  public String getWhen() {
	    return when;
	  }

	  public Who getWho() {
	    return WhoObject;
	  }

	  public String getSigFormat() {
	    return sigFormat;
	  }

	  public String getData() {
	    return data;
	  }

	 // Setter Methods 

	  public void setWhen( String when ) {
	    this.when = when;
	  }

	  public void setWho( Who whoObject ) {
	    this.WhoObject = whoObject;
	  }

	  public void setSigFormat( String sigFormat ) {
	    this.sigFormat = sigFormat;
	  }

	  public void setData( String data ) {
	    this.data = data;
	  }
	
}
