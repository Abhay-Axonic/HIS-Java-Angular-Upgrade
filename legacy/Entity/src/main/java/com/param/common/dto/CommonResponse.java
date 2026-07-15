package com.param.common.dto;

import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CommonResponse<T> {
	
	protected Long id;
	protected List<Long> ids;
	protected String message;
	
	private String status;
	private String code;
	
	private BigInteger bigValue;
	
	private List<T> listObject;
	private Object object;
	private Object object1;
	public Object getObject1() {
		return object1;
	}

	public void setObject1(Object object1) {
		this.object1 = object1;
	}

	private String exceptionMessage;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<T> getListObject() {
		return listObject;
	}

	public void setListObject(List<T> listObject) {
		this.listObject = listObject;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public CommonResponse(String status,String code, String message,
			List<T> listObject, Object object) {
		this.status = status;
		this.message = message;
		this.code = code;
		this.listObject = listObject;
		this.object=object;
	}

	public CommonResponse() {
		// TODO Auto-generated constructor stub
	}

	public BigInteger getBigValue() {
		return bigValue;
	}

	public void setBigValue(BigInteger bigValue) {
		this.bigValue = bigValue;
	}

	
}
