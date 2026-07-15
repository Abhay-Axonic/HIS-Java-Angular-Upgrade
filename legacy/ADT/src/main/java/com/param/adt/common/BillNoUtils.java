package com.param.adt.common;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.param.global.common.ServiceException;

@Service
@SuppressWarnings({"deprecation"})
public class BillNoUtils {

	final static Logger logger = Logger.getLogger(BillNoUtils.class);

	public String generateOpdBillNo(SessionObject sessionObject){
		String billNo = "";
		try{
			Date date = ADTCommonDateUtils.getDateByzone(sessionObject.getZoneDesc());
			billNo = "OP-"+new SimpleDateFormat("yy").format(date)+"-"+date.getDate()+date.getMinutes()+date.getSeconds();
			//System.out.println(billNo);
		}catch(Exception e){
			logger.error("generateOpdBillNo failed", e);
			throw new ServiceException(e.getMessage());
		}
		return billNo;
	}
}
