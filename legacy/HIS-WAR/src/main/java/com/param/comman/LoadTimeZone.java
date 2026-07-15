package com.param.comman;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Repository;

import com.param.billing.common.Response;
import com.param.billing.global.transaction.dto.TDoctorShareRespDto;
import com.param.global.common.ServiceException;
import com.param.global.org.dto.UnitMasterDto;

@Repository
@SuppressWarnings("unchecked")
public class LoadTimeZone {

	static Map<Integer,String> timezoneMap = new HashMap<>();
	
	@Autowired
	SessionFactory sessionFactory;
	
	/*@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			List<UnitMasterDto> timezoneList = sessionFactory.getCurrentSession()
					.createSQLQuery("select unit_id as unitId ,unit_time_zone as unitTimeZone from m_unit_master where status='A' ")
					.setResultTransformer(Transformers.aliasToBean(UnitMasterDto.class)).list();
			
			for(UnitMasterDto dto : timezoneList)
			{
				timezoneMap.put(dto.getUnitId(),dto.getUnitTimeZone());
			}
			
		}catch(Exception e){
			
			throw new ServiceException(e.getMessage());
		}
		
	}*/
	
	
	
}
