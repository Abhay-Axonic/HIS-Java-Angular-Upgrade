package com.param.test;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import com.param.global.common.ServiceException;

@Controller
@RequestMapping("/otims")
public class Test {

	private static final Logger logger = Logger.getLogger(Test.class);

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public @ResponseBody String test() {
		try {
			return "Hello";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	public static void main(String args[])
	{
		/* URI url= UriComponentsBuilder.fromHttpUrl("http://o1xk7s8z3f.execute-api.ap-south-1.amazonaws.com/QA/api/generateFormUrl")
		            .queryParam("templateId","5255-552")
		 .queryParam("age","5")
		 .queryParam("gender","M").build().toUri();		 
		 ;
		 

		Map<Integer,Boolean> completeSalesReturnSaleIds = new HashMap<>();
		completeSalesReturnSaleIds.put(1,false);
		completeSalesReturnSaleIds.put(2,true);*/



		Map<String, Integer> map = new HashMap<>();
		map.put("Alice", 90);
		map.put("Bob", 85);
		map.put("Charlie", 95);
		map.put("David", 88);

		Map.Entry<String, Integer> maxEntry = map.entrySet()
				.stream()
				.max(Map.Entry.comparingByValue())
				.orElse(null);

		if (maxEntry != null) {
			System.out.println("Max Key: " + maxEntry.getKey());
			System.out.println("Max Value: " + maxEntry.getValue());
		}

		List<Integer> numbers = Arrays.asList(45, 32, 88, 12, 67);

		int w = numbers.stream().max(Integer::compare).orElse(null);

		System.out.println(w);

	}
	
	
}
