package com.param.lis.global.common;

import java.math.BigInteger;
import java.util.List;
import java.util.StringJoiner;

import org.apache.log4j.Logger;

import com.param.global.common.ServiceException;
import com.param.lis.global.common.obj;
public class StringCommonMethods {

	private static final Logger logger = Logger.getLogger(StringCommonMethods.class);

	public static String convertToStringJoiner (List<obj> list)
	{
		StringJoiner stringJoiner = null;
		try {
			if(list.size()>0)
			{
			 stringJoiner  = new StringJoiner(",");
			for (obj obj : list) {
				stringJoiner.add(obj.getId().toString());
			 }
			return stringJoiner.toString();
			}
			else {
				return "0";
			}
		}
		catch (Exception e)
		{
			logger.error("Error converting list to StringJoiner", e);
			throw new ServiceException(e.getMessage());
		}
	}

	   public static String generateSampleNumber (BigInteger sampleNo,String prefix,Integer width)
	    {
	        StringBuilder sampleNumber = null;
	        try {

	            if(sampleNo!=null && sampleNo.compareTo(BigInteger.ZERO)==1)
	            {
	              sampleNumber = new StringBuilder();
	              sampleNumber.append(prefix);
	              for(int i=0;i<(width-sampleNo.toString().length());i++)
	              {
	                sampleNumber.append("0");
	              }
	              sampleNumber.append(sampleNo.toString());
	              return sampleNumber.toString();
	            }
	        }
	        catch (Exception e)
	        {
	            logger.error("Error generating sample number", e);
	            throw new ServiceException(e.getMessage());
	        }
	        return "";
	    }

	   
}
