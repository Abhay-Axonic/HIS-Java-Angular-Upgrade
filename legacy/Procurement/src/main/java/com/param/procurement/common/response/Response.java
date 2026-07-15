package com.param.procurement.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.param.procurement.common.constant.ApiError;
import com.param.global.common.ServiceException;
import org.apache.log4j.Logger;



/**
 * Base class for Response
 * @author Nilesh
 *
 */


@JsonInclude(Include.NON_NULL)
public class Response {

	final static Logger logger = Logger.getLogger(Response.class);

	//private static final long serialVersionUID = 1L;

	//private static final int STATUS_SUCCESS = 0;
	public static final String SUCCESS = "Success!!";

	@JsonInclude(Include.NON_DEFAULT)
	private int id; // 0 = SUCCESS ELSE ERROR_CODE
	private String summary;
	private String msg;
	private Object list;
	private int Code;
	private String message;
	private String stackTrace;

	/**
	 * default constructor
	 */
	public Response() {

	}

	public Response(int id, String msg) {
		this.id = id;
		this.msg = msg;
	}
	public Response(int Code, String message, String summary, String stackTrace)
	{		this.Code = Code;
		this.message = message;
		this.summary = summary;
		this.stackTrace = stackTrace;
	}
	
	public static Response getSuccessResponse() {
		return new Response();
	}

	public static Response getCreateResponse(int id, String msg)
	{
		Response response = getSuccessResponse();
		response.setId(id);
		response.setMsg(msg);
		return response;
	}
	
	public static Response getPatchResponse(String msg) {
		Response response = getSuccessResponse();
		response.setMsg(msg);
		return response;
	}

	public static Response getResponse(Object list) {
		Response response = getSuccessResponse();
		response.setData(list);
		return response;
	}

	/*public static Response getSuccessResponse(Object data, String msg) {
		Response response = getSuccessResponse();
		response.setData(data);
		response.setMsg(msg);
		return response;
	}*/

	public static Response getFailureResponse(int Code, String message, String summary, String stackTrace) {
		//String msg = error.getSummary() != null ? error.getSummary() : error.getMessage();
		
		return new Response(Code, message,summary,stackTrace);
	}
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return list;
	}

	public void setData(Object list) {
		this.list = list;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@Override
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this);
		} catch (Exception e) {
			logger.error("toString failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


}
