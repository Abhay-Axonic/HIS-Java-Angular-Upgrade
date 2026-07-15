package com.param.global.controller;
import java.io.Serializable;

import java.util.List;
public class ResponseTwilio<T, ID extends Serializable>{
	private Class<T> entityClass;

	protected Class<T> getEntityClass() {
		return entityClass;
	}
	
	public ResponseTwilio(final Class<T> entityClass) {
		this.entityClass = entityClass;
	}
	
	private String status;
	private String code;
	private String message;
	private List<T> listObject;
	private T object;
	private List<T> errorList;
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
	
	public ResponseTwilio(String status,String code, String message,
			List<T> listObject,T object) {
		this.status = status;
		this.message = message;
		this.code = code;
		this.listObject = listObject;
		this.object = object;
	}
	
	public ResponseTwilio(String status,String code, String message,
			List<T> listObject,T object, List<T> errorList) {
		this.status = status;
		this.message = message;
		this.code = code;
		this.listObject = listObject;
		this.object = object;
		this.errorList = errorList;
	}
	
	public ResponseTwilio(String status,String code, String message,
			List<T> listObject) {
		this.status = status;
		this.message = message;
		this.code = code;
		this.listObject = listObject;
	}
	
	public void Response(String status,String code, String message,
			List<T> listObject, T object) {
		this.status = status;
		this.message = message;
		this.code = code;
		this.listObject = listObject;
		this.object = object;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public List<T> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<T> errorList) {
		this.errorList = errorList;
	}
}
