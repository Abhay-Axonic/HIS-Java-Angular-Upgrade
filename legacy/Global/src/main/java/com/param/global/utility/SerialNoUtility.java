package com.param.global.utility;

import java.math.BigInteger;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.param.global.common.ServiceException;

@Service
public class SerialNoUtility {

	final static Logger logger = Logger.getLogger(SerialNoUtility.class);

	public String generateDepositNo(Long id){
		try{
			String depNo = "RE-";
			String strId = String.valueOf(id);
			int length = strId.length();
			depNo = depNo + (length == 1 ? "000"+strId : length == 2 ? "00"+strId : length == 3 ? "0"+strId : strId);
			return depNo;
		}catch(Exception e){
			logger.error("generateDepositNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

    public String numberToString(BigInteger inputNumber,String prefix,Integer width)
    {
      StringBuilder generatedNumber = new StringBuilder();
        try
            {

              Integer numberLength = inputNumber.toString().length();
                if (!inputNumber.equals(BigInteger.ZERO))
                    {
                        generatedNumber.append(prefix);
                        for (int i = 0; i < (width-numberLength); i++)
                        {
                          generatedNumber.append("0");
                        }
                        generatedNumber.append(inputNumber);
                    }
            }
        catch (Exception e)
            {
                logger.error("numberToString failed", e);
                throw new ServiceException(e.getMessage());
            }
       return  generatedNumber.toString();
    }

}
