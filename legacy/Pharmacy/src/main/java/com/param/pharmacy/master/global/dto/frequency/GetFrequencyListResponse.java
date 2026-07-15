package com.param.pharmacy.master.global.dto.frequency;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.param.global.common.GlobalCommonDateUtils;

public class GetFrequencyListResponse {

	protected Integer id;

//	protected Integer slot;

	protected String administrationTime;

	protected String preBufferAdministrationTime;

	protected String postBufferAdministrationTime;
	
	protected String administrationTimeString;

	protected String preBufferAdministrationTimeString;

	protected String postBufferAdministrationTimeString;
	
	protected List<GetFrequencyListResponse> getparameter;
	

	/*public GetFrequencyListResponse(Integer id,String administrationTime,
			String preBufferAdministrationTime, String postBufferAdministrationTime) {
		super();
		this.id = id;
//		this.slot = slot;
		this.administrationTime = this.setAdministrationTime(administrationTime);
		this.preBufferAdministrationTime = this.setPreBufferAdministrationTime(preBufferAdministrationTime);
		this.postBufferAdministrationTime = this.setpostBufferAdministrationTime;
	}
*/
	
//	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	

	public String getAdministrationTime() {
		return administrationTime;
	}

	public void setAdministrationTime(String administrationTime) {
		this.administrationTime = administrationTime;
	}

	public String getPreBufferAdministrationTime() {
		return preBufferAdministrationTime;
	}

	public void setPreBufferAdministrationTime(String preBufferAdministrationTime) {
		this.preBufferAdministrationTime = preBufferAdministrationTime;
	}

	public String getPostBufferAdministrationTime() {
		return postBufferAdministrationTime;
	}

	public void setPostBufferAdministrationTime(String postBufferAdministrationTime) {
		this.postBufferAdministrationTime = postBufferAdministrationTime;
	}

	public String getAdministrationTimeString() {
		return administrationTimeString;
	}

	public void setAdministrationTimeString(String administrationTimeString) {
		this.administrationTimeString = administrationTimeString;
	}

	public String getPreBufferAdministrationTimeString() {
		return preBufferAdministrationTimeString;
	}

	public void setPreBufferAdministrationTimeString(String preBufferAdministrationTimeString) {
		this.preBufferAdministrationTimeString = preBufferAdministrationTimeString;
	}

	public String getPostBufferAdministrationTimeString() {
		return postBufferAdministrationTimeString;
	}

	public void setPostBufferAdministrationTimeString(String postBufferAdministrationTimeString) {
		this.postBufferAdministrationTimeString = postBufferAdministrationTimeString;
	}

	
		
		public List<GetFrequencyListResponse> getGetparameter() {
			return getparameter;
		}

		public void setGetparameter(List<GetFrequencyListResponse> getparameter) {
			this.getparameter = getparameter;
		}

		public void addGetparameter(GetFrequencyListResponse getparameter) {
			if (this.getparameter == null)
				this.getparameter = new ArrayList<GetFrequencyListResponse>();

			this.getparameter.add(getparameter);
		}
		
		
	/*public LocalTime getAdministrationTime() {
		return administrationTime;
	}

	
	public void setAdministrationTime(String administrationTime) {
		if (!administrationTime.equals("")) {
			this.administrationTime = GlobalCommonDateUtils.getLocalTime(administrationTime);
		} else {
			this.administrationTime = GlobalCommonDateUtils.getLocalTime("00:00");
		}
	}
	

	public LocalTime getPreBufferAdministrationTime() {
		return preBufferAdministrationTime;
	}

	public void setPreBufferAdministrationTime(String preBufferAdministrationTime) {
		if (!preBufferAdministrationTime.equals("")) {
			this.preBufferAdministrationTime = GlobalCommonDateUtils.getLocalTime(preBufferAdministrationTime);
		} else {
			this.preBufferAdministrationTime = GlobalCommonDateUtils.getLocalTime("00:00");
		}
	}
	

	public LocalTime getPostBufferAdministrationTime() {
		return postBufferAdministrationTime;
	}

	public void setPostBufferAdministrationTime(String postBufferAdministrationTime) {
		if (!postBufferAdministrationTime.equals("")) {
			this.postBufferAdministrationTime = GlobalCommonDateUtils.getLocalTime(postBufferAdministrationTime);
		} else {
			this.postBufferAdministrationTime = GlobalCommonDateUtils.getLocalTime("00:00");
		}
	}*/
	
	
	

	
	

}
