package com.param.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

public class Response<T, ID extends Serializable>{
	private Class<T> entityClass;

	protected Class<T> getEntityClass() {
		return entityClass;
	}
	
	public Response(final Class<T> entityClass) {
		this.entityClass = entityClass;
	}
	
	private String status;
	private String code;
	private String message;
	private List<T> stores;
	private List<T> listObject;
	private List<T> listObject2;
	public List<T> getStores() {
		return stores;
	}

	public void setStores(List<T> stores) {
		this.stores = stores;
	}

	private Object object;
	private Object resObject;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String exceptionMessage;

	

	public List<T> getListObject2() {
		return listObject2;
	}

	public void setListObject2(List<T> listObject2) {
		this.listObject2 = listObject2;
	}

	public Object getResObject() {
		return resObject;
	}

	public void setResObject(Object resObject) {
		this.resObject = resObject;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the listObject
	 */
	public List<T> getListObject() {
		return listObject;
	}
	/**
	 * @param listObject the listObject to set
	 */
	public void setListObject(List<T> listObject) {
		this.listObject = listObject;
	}

	public Response(String status, String code, String message,
					List<T> listObject, Object object) {
		this.status = status;
		this.message = message;
		this.code = code;
		this.listObject = listObject;
		this.object=object;
	}
	
	public Response(String status,String code, String message,
			List<T> listObject, Object object,Object resObject) {
		this.status = status;
		this.message = message;
		this.code = code;
		this.listObject = listObject;
		this.object=object;
		this.resObject=resObject;
	}
	
	public Response(String status,List<T> stores) {
		this.status = status;	
		this.stores = stores;
		}

	public Response(String status,String code, String message,
			List<T> listObject, Object object, String exceptionMessage) {
		this.status = status;
		this.message = message;
		this.code = code;
		this.listObject = listObject;
		this.object=object;
		this.exceptionMessage = exceptionMessage;
	}


	
	public Response(String status,String code, String message,
			List<T> listObject, Object object,Object resObject,List<T> listObject2) {
		this.status = status;
		this.message = message;
		this.code = code;
		this.listObject = listObject;
		this.object=object;
		this.resObject=resObject;
		this.listObject2=listObject2;
	}
	
	public Response(String status, Object object) {
		this.status = status;
		this.object=object;
	}
}
