package com.param.opd.coversheet.dao;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.data.dto.AdministrationWorklistTimeDetailsDto;
import com.param.entity.model.opd.AdministrationWorklistTimeDetails;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "unchecked","rawtypes" })
public class AdministrationWorklistTimeDetailsDaoImpl extends GenericDao<AdministrationWorklistTimeDetails, Integer>implements IAdministrationWorklistTimeDetailsDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(AdministrationWorklistTimeDetailsDaoImpl.class);


	
	public AdministrationWorklistTimeDetailsDaoImpl() {
		super(AdministrationWorklistTimeDetails.class);
	}

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	DozerBeanMapper mapper;
	
	@Override
	public Response saveAdministratorWorkList(AdministrationWorklistTimeDetailsDto dto) throws ApplicationException {
		try {
			
			AdministrationWorklistTimeDetails medicationHistory=new AdministrationWorklistTimeDetails();
			medicationHistory.setStatus('A');
			medicationHistory.setSaleDetailsId(dto.getSaleDetailsId()!= null ? dto.getSaleDetailsId(): null);
			medicationHistory.setAdministrationDate(GlobalCommonDateUtils.getDate(dto.getAdministrationDate(), "dd-M-yyyy HH:mm:ss"));
			medicationHistory.setAdministrationRemark(dto.getAdministrationRemark() != null ? dto.getAdministrationRemark() : null);
			medicationHistory.setAdministrationTime(dto.getAdministrationTime());
			medicationHistory.setUnitId(dto.getUnitId());
			medicationHistory.setOrganizationId(dto.getOrganizationId());
			medicationHistory.setCreatedBy(dto.getCreatedBy());
			medicationHistory.setUpdatedBy(dto.getCreatedBy());
			medicationHistory.setPrescriptionDetailId(dto.getPrescriptionDetailId()!= null ? dto.getPrescriptionDetailId() : null);
			medicationHistory.setAdministratorQty((dto.getAdministratorQty()+1));
			medicationHistory.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			medicationHistory.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			if(dto.getAdministrationWorklistTimeDetailsId() != null) {
				medicationHistory.setAdministrationWorklistTimeDetailsId(dto.getAdministrationWorklistTimeDetailsId());
				update(medicationHistory);
			}else {
				save(medicationHistory);
			}
			
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		} catch (HibernateException he) {
		logger.error("saveAdministratorWorkList failed", he);
		} catch (Exception e) {
		logger.error("saveAdministratorWorkList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
	
	
	
}
