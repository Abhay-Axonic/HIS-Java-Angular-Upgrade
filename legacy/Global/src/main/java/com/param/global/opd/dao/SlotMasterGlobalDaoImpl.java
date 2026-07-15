package com.param.global.opd.dao;

import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.sync.SlotIdReqDto;
import com.param.opd.unit.model.SlotMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
public class SlotMasterGlobalDaoImpl extends GenericDao<SlotMaster, Integer> implements ISlotMasterGlobalDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(SlotMasterGlobalDaoImpl.class);

	public SlotMasterGlobalDaoImpl() {
		super(SlotMaster.class);
	}

	@Override
	public Response getSlotIdForMyLifeAppSync(SlotIdReqDto dto) throws ApplicationException {
		try{
			Date appointmentDate = GlobalCommonDateUtils.getDate(dto.getAppointmentDate(),"dd-M-yyyy");
			Date timeSlot = GlobalCommonDateUtils.getDate(dto.getTimeSlot(),"HH:mm");
			SlotIdReqDto slotResult = (SlotIdReqDto)sessionFactory.getCurrentSession().getNamedQuery("GET_SLOT_ID_FOR_APP_SYNCING_FROM_MYLIFE").setParameter("orgId", dto.getOrgId())
											.setParameter("unitId", dto.getUnitId()).setParameter("specialtyId", dto.getSpecialtyId())
											.setParameter("appointmentDate", appointmentDate).setTime("timeSlot", timeSlot)
											.setParameter("globalDocId", dto.getDoctorId())
		 							   .setResultTransformer(Transformers.aliasToBean(SlotIdReqDto.class)).uniqueResult();
			
			if(slotResult != null) {
				dto.setSlotId(slotResult.getSlotId());
				dto.setDoctorId(slotResult.getDoctorId());
			}
			
			
			return new Response(SUCCESS, null, null, null, dto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Integer getSlotIdByAppointmentId(SlotIdReqDto slotIdReq) throws ApplicationException {
		try{
			Integer id = (Integer)sessionFactory.getCurrentSession().getNamedQuery("GET_SLOT_ID_BY_APP_ID")
					    //.setParameter("orgId", slotIdReq.getOrgId())
						//.setParameter("unitId", slotIdReq.getUnitId())
						.setParameter("appointmentId", slotIdReq.getAppointmentId()).uniqueResult();
			return id;
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@Override
	public String getAppDateTimeBySlotId(Integer slotId) {
		try {

			String query = " select "
					+ " concat(to_char(sl.appointment_date, 'YYYY-MM-DD'), ' ', to_char(sl.from_time, 'HH24:MI:SS')) "
					+ " from opd.m_slot_master sl where sl.slot_id =:slotId " ;

			String date = (String) sessionFactory.getCurrentSession().createSQLQuery(query).setParameter("slotId",slotId).uniqueResult();

			return date;

		} catch (HibernateException he) {
			logger.error("getAppDateTimeBySlotId failed", he);
		} catch (Exception e) {
			logger.error("getAppDateTimeBySlotId failed", e);
		}
		return null;
	}
	
}
