package com.param.lis.global.common;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import com.param.global.dto.UnitServiceMasterDetailsDto;

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
	private List<T> listObject;
	private T object;
	private List<T> listObject1;
	private T object1;
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
	
	public Response(String status,String code, String message,
			List<T> listObject, T object) {
		this.status = status;
		this.message = message;
		this.code = code;
		this.listObject = listObject;
		this.object = object;
	}
	
	public Response(String status,String code, String message,
			List<T> listObject, T object , List<T> listObject1) {
		this.status = status;
		this.message = message;
		this.code = code;
		this.listObject = listObject;
		this.object = object;
		this.listObject1 = listObject1;
	}
	
	public Response(String status,String code, String message,
			List<T> listObject) {
		this.status = status;
		this.message = message;
		this.code = code;
		this.listObject = listObject;
	}

	public Response(String status, String code, String message,List<T> listObject, T object , T Object1) {
		this.status = status;
		this.message = message;
		this.code = code;
		this.listObject = listObject;
		this.object = object;
		this.object1 = Object1;
	}

	public T getObject()
		{
				return object;
		}

	public void setObject(T object)
		{
				this.object = object;
		}

	public List<T> getListObject1() {
		return listObject1;
	}

	public void setListObject1(List<T> listObject1) {
		this.listObject1 = listObject1;
	}

	public T getObject1() {
		return object1;
	}

	public void setObject1(T object1) {
		this.object1 = object1;
	}

	
}
