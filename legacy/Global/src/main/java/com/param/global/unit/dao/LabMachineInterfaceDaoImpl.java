package com.param.global.unit.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.global.MedallRequestResponseDetails;
import com.param.entity.lis.unit.LabMachineInterface;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dto.MedallRequestResponseDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class LabMachineInterfaceDaoImpl extends GenericDao<LabMachineInterface, Integer> implements ILabMachineInterfaceDao,ICommonConstants{

	final static Logger logger = Logger.getLogger(LabMachineInterfaceDaoImpl.class);

	public LabMachineInterfaceDaoImpl() {
		super(LabMachineInterface.class);
	}

	@Override
	public Response saveLabMachineInterface(MedallRequestResponseDto med) throws ApplicationException {
		Throwable resultObj=null;
		try {
			List<MedallRequestResponseDto> emptySampleNoList=new ArrayList<MedallRequestResponseDto>();
			for (MedallRequestResponseDto dto : med.getOrderDetailsListById()) {
				if(dto.getSampleNo() == null) {
					emptySampleNoList.add(dto);
				}else {
					for (MedallRequestResponseDto medallRequestResponseDto : med.getTestDetailsList()) {
						if((!dto.getMedallServiceId().equals(null) && dto.getMedallServiceId().equals(medallRequestResponseDto.getCorpbillingid()))){
							LabMachineInterface labMachineInterface=new LabMachineInterface();
							labMachineInterface.setParameterName(medallRequestResponseDto.getParameterName() != null ? medallRequestResponseDto.getParameterName() : "");
							labMachineInterface.setParameterUnit(medallRequestResponseDto.getParameterUnit() != null ? medallRequestResponseDto.getParameterUnit() : "");
							labMachineInterface.setParameterValue(medallRequestResponseDto.getParameterValue() != null ? medallRequestResponseDto.getParameterValue().toString(): "");
							labMachineInterface.setMinValue(medallRequestResponseDto.getMinValue() != null ? medallRequestResponseDto.getMinValue() : 0.0);
							labMachineInterface.setMaxValue(medallRequestResponseDto.getMaxValue() != null ? medallRequestResponseDto.getMaxValue(): 0.0);
							labMachineInterface.setHighValue(medallRequestResponseDto.getHighValue() != null ? medallRequestResponseDto.getHighValue(): 0.0);
							labMachineInterface.setLowValue(medallRequestResponseDto.getLowValue() != null ? medallRequestResponseDto.getLowValue(): 0.0);
							labMachineInterface.setNote(medallRequestResponseDto.getNote());
							labMachineInterface.setUpdatedBy(dto.getUpdatedBy());
							labMachineInterface.setUpdatedDate(dto.getUpdatedDate() != null ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "yyyy-MM-dd HH:mm:ss"):GlobalCommonDateUtils.getDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
							labMachineInterface.setMachineName(med.getMachineName() != null ? med.getMachineName() : "");
							labMachineInterface.setResultDateTime(medallRequestResponseDto.getResultDateTime() != null ? GlobalCommonDateUtils.getDate(medallRequestResponseDto.getResultDateTime(), "yyyy-MM-dd HH:mm:ss"): GlobalCommonDateUtils.getDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
							labMachineInterface.setOrganisationId(dto.getOrganizationId() != null ? dto.getOrganizationId() : null);
							labMachineInterface.setUnitId(dto.getUnitId() != null ? dto.getUnitId() :null);
							labMachineInterface.setCreatedBy(dto.getAddedBy()!= null ? dto.getAddedBy() :null);
							labMachineInterface.setCreatedDate(dto.getAddedDate() != null ? GlobalCommonDateUtils.getDate(dto.getAddedDate(), "yyyy-MM-dd HH:mm:ss"):GlobalCommonDateUtils.getDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
							labMachineInterface.setStatus('A');
							labMachineInterface.setSampleNo(dto.getSampleNo() != null ? dto.getSampleNo(): null);
							save(labMachineInterface);
							
						}
					}
				}
				
				
			}
			
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, emptySampleNoList, med);
		
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			resultObj=he.getCause();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			resultObj=e.getCause();

		}
		return new Response<>(ERROR, ERROR_CODE, null, null, resultObj);
	}
}
