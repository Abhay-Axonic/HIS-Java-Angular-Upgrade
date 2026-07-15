package com.param.global.dao;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.AppointmentVitalMapperDtoSync;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.sync.AppointmentSyncSchedulingDto;
import com.param.opd.coversheet.model.AppointmentVitalMapper;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class IAppointmentVitalMapperDAOForSyncImpl extends GenericDao<AppointmentVitalMapper, Integer> implements IAppointmentVitalMapperDAOForSync, ICommonConstants {

	public IAppointmentVitalMapperDAOForSyncImpl() {
		super(AppointmentVitalMapper.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response getVitalByPatientIdAndVitalIdForSync(Integer recordId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getVitalForSyncById(Integer appointmentVitalMapperId) {
		try{
			AppointmentVitalMapperDtoSync dto = (AppointmentVitalMapperDtoSync)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_VITAL_MASTER_TO_SYNC_BY_ID").setInteger("id", appointmentVitalMapperId)
					.setResultTransformer(Transformers.aliasToBean(AppointmentVitalMapperDtoSync.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);
		}catch(Exception e){
			
			throw e;
		}
	}

}
