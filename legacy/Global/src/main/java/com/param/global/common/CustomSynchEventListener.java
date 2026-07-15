package com.param.global.common;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.param.global.dao.IScheduleLogsDao;
import com.param.global.dto.ScheduleLogsDto;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Component
public class CustomSynchEventListener implements ApplicationListener<ScheduleLogsDto>{
	final static Logger logger = Logger.getLogger(CustomSynchEventListener.class);


	@Autowired
	private IScheduleLogsDao iScheduleLogsDao;
	static Integer recordId=0;
	static String tableName="A";
	
	@Override
	@Transactional
	public void onApplicationEvent(ScheduleLogsDto event) {
		 try{
			
			 if(event.getRecordId().equals(recordId) && event.getTableName().equals(tableName) ) {
			     //Response res=iScheduleLogsDao.isRecordIdAndTableNameExist(event);
			     //if(res.getObject()==null) {
				 //System.out.println("-----* Inserting log table data * -----");
				 iScheduleLogsDao.saveScheduleLogs(event);
				 //System.out.println("-----* Log table Data Inserted for * -----\n" + event + "\nObject available for garbage collection : " + event.hashCode() + "\n -----***------");
			     }
			//}
			 recordId=event.getRecordId();
			 tableName=event.getTableName();
			 event = null;
			
		 }catch(Exception e){
		 logger.error("onApplicationEvent failed", e);
		 }
	}

}
