package com.param.opd.unit.dao;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.doctor.dao.IDoctorScheduleDao;
import com.param.global.dto.BlockSlotReqDto;
import com.param.global.dto.ScheduleMasterDto;
import com.param.opd.unit.model.SlotBlockUnblockDetail;
import com.param.opd.unit.model.SlotMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;


@Repository
@SuppressWarnings("rawtypes")
public class SlotBlockUnblockDetailDaoImpl extends GenericDao<SlotBlockUnblockDetail, Integer> implements ISlotBlockUnblockDetailDao, ICommonConstants {

	public SlotBlockUnblockDetailDaoImpl() {
		super(SlotBlockUnblockDetail.class);
	}

	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	IDoctorScheduleDao iDoctorScheduleDao;
	@Override
	public Response saveSlotBlockDetails(BlockSlotReqDto blockSlotReqDto)
			throws ApplicationException {
		try{
			
			List<ScheduleMasterDto> scheduleMasterDtosList = new LinkedList<ScheduleMasterDto>();
			for (Integer slotId : blockSlotReqDto.getListSlotId()) 
			{
				SlotBlockUnblockDetail slotBDetail = new SlotBlockUnblockDetail();
					slotBDetail.setSlotId(slotId);
					slotBDetail.setReasonId(blockSlotReqDto.getReasonId());
					slotBDetail.setRemark(blockSlotReqDto.getRemark());
					slotBDetail.setCreatedBy(blockSlotReqDto.getCreatedBy());
					slotBDetail.setCreatedDate(GlobalCommonDateUtils.getDate(blockSlotReqDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
					slotBDetail.setIsBlockUnblock(blockSlotReqDto.getIsBlockUnblock());
					slotBDetail.setOrganisationId(blockSlotReqDto.getOrganisationId());
					slotBDetail.setUnitId(blockSlotReqDto.getUnitId());
					slotBDetail.setStatus('A');
				save(slotBDetail);
				
				SlotMaster master = (SlotMaster) sessionFactory.getCurrentSession().get(SlotMaster.class,slotId);
				
				if(master.getSlotTypeId()==1)
				{
					if(blockSlotReqDto.getIsBlockUnblock()==1)
					{
						ScheduleMasterDto dto = new ScheduleMasterDto();	
							dto.setDoctorId(master.getDoctorId());				    	
							dto.setSpecialityId(master.getSpecialityId());				    	
							dto.setOrganisationId(master.getOrganizationId());				 
							dto.setUnitId(master.getUnitId());
							dto.setDeleteAllSlots('N');
							dto.setScheduleDate(GlobalCommonDateUtils.getStringDate(master.getAppointmentDate(),"dd-MM-yyyy"));
							dto.setFromTime(master.getFromTime().toString());
							dto.setToTime(master.getToTime().toString());
							dto.setAddRemove(1);
						scheduleMasterDtosList.add(dto);
					}else{
						
						ScheduleMasterDto dto = new ScheduleMasterDto();	
							dto.setDoctorId(master.getDoctorId());				    	
							dto.setSpecialityId(master.getSpecialityId());				    	
							dto.setOrganisationId(master.getOrganizationId());				 
							dto.setUnitId(master.getUnitId());
							dto.setDeleteAllSlots('N');
							dto.setScheduleDate(GlobalCommonDateUtils.getStringDate(master.getAppointmentDate(),"dd-MM-yyyy"));
							dto.setFromTime(master.getFromTime().toString());
							dto.setToTime(master.getToTime().toString());
							dto.setAddRemove(2);
						scheduleMasterDtosList.add(dto);
					}
				}
			}
			
			if(scheduleMasterDtosList.size()>0)
				iDoctorScheduleDao.saveDoctorSchedule(scheduleMasterDtosList,"DELETE");
			
			return new Response<>(SUCCESS, COMMON_SUCCESS, SUCCESS_MESSAGE, null, null);
		}catch(HibernateException he){
			throw he;
		}catch (ApplicationException ae) {
			throw ae;
		}
		
	}

	@Override
	public Response saveUpdatePrevSlotBlockDetails(BlockSlotReqDto blockSlotReqDto) throws ApplicationException {
		try{
			for (Integer slotId : blockSlotReqDto.getListSlotId()) {
				sessionFactory.getCurrentSession().
				createQuery("UPDATE SlotBlockUnblockDetail set status='I' "
						+ "WHERE slotId="+slotId
						+ "AND status='A' ").executeUpdate();
			}
			return new Response<>(SUCCESS, COMMON_SUCCESS, SUCCESS_MESSAGE, null, null);
			
		}catch(HibernateException he){
			throw he;
		}catch (ApplicationException ae) {
			throw ae;
		}
	}

}
