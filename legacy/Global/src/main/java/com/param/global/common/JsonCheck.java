package com.param.global.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.param.global.billing.dto.CommonBillingItemsDto;

/**@author Ganesh Chaudhari*/
public class JsonCheck<T>
{
	final static Logger logger = Logger.getLogger(JsonCheck.class);

	public static <T> Response convertPojoToJson(T inputObject)
	{
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = null;
		try
		{
			jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(inputObject);
			//System.out.println("jsonString :" + jsonString);
		} catch (JsonProcessingException e)
		{
			logger.error("convertPojoToJson failed", e);
			throw new ServiceException(e.getMessage());
		}
		////System.out.println(jsonString);
		return null;
	}
	


	public static <T> Response mapJsonToObjectList(String json, Class clazz) throws Exception
	{
		List<T> list;
		ObjectMapper mapper = new ObjectMapper();
		//System.out.println("OLD =>" + json);
		//System.out.println("NEW =>" + json);
		TypeFactory t = TypeFactory.defaultInstance();
		list = (json != null) ? mapper.readValue(json, t.constructCollectionType(ArrayList.class, clazz)) : null;
		//System.out.println(list);
		//System.out.println(list.get(0).getClass());
		return null;
	}

	public static <T> Response mapJsonToObject(String json, Class<T> clazz) throws Exception
	{
		try
		{
			T tt;
			ObjectMapper mapper = new ObjectMapper();
			//System.out.println("OLD =>" + json);
			//System.out.println("NEW =>" + json);
			TypeFactory t = TypeFactory.defaultInstance();
			tt = mapper.readValue(json, clazz);
			return null;

		} catch (Exception e)
		{
			logger.error("mapJsonToObject failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
public static void main(String[] args) {
	try{
			JSONObject jsonString1 = new JSONObject();
			jsonString1.put("Position", "");
			jsonString1.put("BiometricData", "");
			JSONArray jsonArguments = new JSONArray();
			jsonArguments.put(jsonString1);
			JSONObject jsonString = new JSONObject();
			jsonString.put("Uuid", "0");
			jsonString.put("Group", "");
			jsonString.put("ReqInfo", "");
			jsonString.put("Biometrics", jsonArguments);
			//System.out.print("jsonString" + jsonString);
			
			
			
			JsonCheck<CommonBillingItemsDto> jsonCheck  = new JsonCheck<>();
			CommonBillingItemsDto dto  = new CommonBillingItemsDto(); 
			jsonCheck.convertPojoToJson(dto);

	}
	catch(Exception e)
	{
		logger.error("main failed", e);
		throw new ServiceException(e.getMessage());
	}
}

}
