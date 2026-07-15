package com.param.billing.common;

/**@author Ganesh Chaudhari*/

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.param.global.otims.dto.OtCompletionInvConsumableDto;
import com.param.global.otims.dto.OtcCompletionInvImplantsDto;
import com.param.global.otims.dto.OtcCompletionInvMedicationDto;

public class JsonCheck<T>
{

	public String convertPojoToJson(T inputObject)
	{
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = null;
		try
		{
			jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(inputObject);
			//System.out.println("jsonString :" + jsonString);
		} catch (JsonProcessingException e)
		{
			
		}
		return jsonString;
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
			
		}
		return null;
	}

	public static void main(String[] args)
	{
			JsonCheck<OtcCompletionInvMedicationDto> jsonCheck  = new JsonCheck<>();
			OtcCompletionInvMedicationDto testParamMpprDto  = new OtcCompletionInvMedicationDto(); 
			jsonCheck.convertPojoToJson(testParamMpprDto);
			
			JsonCheck<OtcCompletionInvImplantsDto> jsonCheck1  = new JsonCheck<>();
			OtcCompletionInvImplantsDto testParamMpprDto1  = new OtcCompletionInvImplantsDto(); 
			jsonCheck1.convertPojoToJson(testParamMpprDto1);
			
			JsonCheck<OtCompletionInvConsumableDto> jsonCheck2  = new JsonCheck<>();
			OtCompletionInvConsumableDto testParamMpprDto2  = new OtCompletionInvConsumableDto(); 
			jsonCheck2.convertPojoToJson(testParamMpprDto2);
			
			/*JsonCheck<PaymentRefundDetailsDto> jsonCheck1  = new JsonCheck<>();
			PaymentRefundDetailsDto testParamMpprDto1  = new PaymentRefundDetailsDto(); 
			jsonCheck1.convertPojoToJson(testParamMpprDto1);*/
			
		
	}
}
