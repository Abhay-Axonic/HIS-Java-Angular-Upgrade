package com.param.ER.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.discharge.model.Discharge;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.DischargeDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class ERDischargeDaoImpl extends GenericDao<Discharge, Integer> implements IERDischargeDao,ICommonConstants{

	public ERDischargeDaoImpl() {
		super(Discharge.class);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public Response getERDischargePatientList(DischargeDto dischargeDto) throws ApplicationException {
		try {
			Integer count=0;
			List<AdmissionDto> admissionDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ER_DISCHARGE_LIST")
					.setParameter("organizationId", dischargeDto.getOrganizationId())
					.setParameter("unitId", dischargeDto.getUnitId())
					.setParameterList("wardList", dischargeDto.getWardList())
					.setFirstResult(dischargeDto.getOffset() != null ? dischargeDto.getOffset()  : 0)
					.setMaxResults(dischargeDto.getNoOfRecordsPerPage() != null ? dischargeDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			
		      List<AdmissionDto> listcount  = sessionFactory.getCurrentSession()
		    		   .getNamedQuery("GET_ER_DISCHARGE_LIST")
		    		   .setParameter("organizationId", dischargeDto.getOrganizationId())
						.setParameter("unitId", dischargeDto.getUnitId())
						.setParameterList("wardList", dischargeDto.getWardList())
						.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
		     
		      
		      if(listcount != null && listcount.size()>0) {
		    	  count = listcount.size();
		      }
			return new Response(SUCCESS, null, null, admissionDtosList, count);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

}
