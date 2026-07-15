package com.param.lis.global.common;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ServiceException;
import com.param.lis.global.dto.BarcodeDto;
import com.param.utility.TimeZoneComponent;

public class CommonUtils
	{

	private static final Logger logger = Logger.getLogger(CommonUtils.class);

	public static final String query = "SELECT * FROM ABC WHERE a=:a";
		public static BigDecimal stringToBigDecimal(String inputNumber)
			{
				try
					{
						if (StringUtils.isEmpty(inputNumber))
							{
								return BigDecimal.ZERO;
							}
						else
							{
								return new BigDecimal(inputNumber);
							}
					}
				catch (Exception e)
					{
						logger.error("Exception in stringToBigDecimal", e);
						throw new ServiceException(e.getMessage());
					}

			}

	        public static String numberToString(BigInteger inputNumber,String prefix,Integer width)
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
	                        logger.error("Exception in numberToString", e);
	                        throw new ServiceException(e.getMessage());
	                    }
	               return  generatedNumber.toString();
	            }

	        public static String numberToStringWithLength(BigInteger inputNumber,Integer width)
            {
            StringBuilder generatedNumber = new StringBuilder();
                try
                    {
                      
                      Integer numberLength = inputNumber.toString().length();
                        if (!inputNumber.equals(BigInteger.ZERO))
                            {
                                for (int i = 0; i < (width-numberLength); i++) 
                                {
                                  generatedNumber.append("0");
                                }
                                generatedNumber.append(inputNumber);
                            }
                    }
                catch (Exception e)
                    {
                        logger.error("Exception in numberToStringWithLength", e);
                        throw new ServiceException(e.getMessage());
                    }
               return  generatedNumber.toString();
            }

	        public static String generateSampleEPLString(List<BarcodeDto> barcodeList)
            {
	            StringBuilder generateString = new StringBuilder();
	        try
                 {
	        	 String date = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(0).getTime(),"dd_MM_yyyy_HH_mm_ss");
	        	 FileWriter writer = new FileWriter("E:\\"+date+".txt", true);
                 generateString.append("I8,A,001 \n");
                	
                      for (Iterator<BarcodeDto> iterator = barcodeList.iterator(); iterator.hasNext();) {
						BarcodeDto barcodeDto = (BarcodeDto) iterator.next();
						generateString.append(" Q200,024 \n"
						+ "	        q831 \n"
						+ "	        rN \n"
						+ "	        S5 \n"
						+ "	        D10 \n"
						+ "	        ZT \n"
						+ "	        JF \n"
						+ "	        O \n"
						+ "	        R255,0 \n"
						+ "	        f100 \n"
						+ "	        N \n"
						+ "	        A307,177,2,2,1,1,N,\"{"+barcodeDto.getDeptName()+"}\" \n" 
						+ "	        B216,129,2,1,1,3,80,N,\"{"+barcodeDto.getSampleId()+"}\" \n "
						+ "	        A82,177,2,2,1,1,N,\"{"+barcodeDto.getUhid()+"} \n"
						+ "	        A200,151,2,2,1,1,N,\"{"+barcodeDto.getPatientDetails()+"}\" \n "
						+ "	        A316,17,2,2,1,1,N,\"{"+barcodeDto.getVisitType()+"}\" \n "
						+ "	        A193,18,2,2,1,1,N,\"{"+barcodeDto.getGenerationDateTime()+"}\"  \n"
						+ "	        P1");
						
					  }
                      writer.write(generateString.toString());
     	              writer.close();
                    }
                catch (Exception e)
                    {
                        logger.error("Exception in generateSampleEPLString", e);
                        throw new ServiceException(e.getMessage());
                    }
               return  generateString.toString();
            }
	        
	        
	     
	}      
	        


	   
