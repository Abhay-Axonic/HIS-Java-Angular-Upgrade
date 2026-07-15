package com.param.global.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.CoversheetTemplateMasterDto;
import com.param.global.dto.NewEMRTemplateDto;
import com.param.global.model.CoversheetTemplateMaster;
import com.param.opd.coversheet.dto.MedicationHistoryDto;

import in.param.common.dao.GenericDao;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class NewEMRTemplateDaoImpl extends GenericDao<CoversheetTemplateMaster, Integer> implements INewEMRTemplateDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(NewEMRTemplateDaoImpl.class);



	public NewEMRTemplateDaoImpl() {
		super(CoversheetTemplateMaster.class);
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public Response checkAvailableCoversheetAsTemaplate(NewEMRTemplateDto newEMRTemplateDto) {
		try {
			String query = "select " + 
					"	cov.cov_template_id as \"covTemplateId\" ," + 
					"	cov.template_name as \"templateName\" " + 
					"from " + 
					"	opd.t_coversheet_template_master cov "
					+ " where cov.organization_id=:organizationId "
					+ " and cov.unit_id=:unitId "
					+ " and lower(cov.template_name) like '"+newEMRTemplateDto.getTemplateName()+"%'";
			
			
			List<MedicationHistoryDto> list=sessionFactory.getCurrentSession().createSQLQuery(query)
//					.setParameter("covTemplateId", coversheetTemplateMasterDto.getCovTemplateId())
					.setParameter("organizationId", newEMRTemplateDto.getOrganizationId())
				.setParameter("unitId", newEMRTemplateDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(CoversheetTemplateMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			
			
	} catch (HibernateException he) {
	logger.error("checkAvailableCoversheetAsTemaplate failed", he);
	} catch (Exception e) {
	logger.error("checkAvailableCoversheetAsTemaplate failed", e);
	}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response saveNewEMRTemplate(CoversheetTemplateMasterDto coversheetTemplateMasterDto) {
		try {
			if(coversheetTemplateMasterDto.getTypeId() != 1) {
				CoversheetTemplateMaster mst=new CoversheetTemplateMaster();
				mst.setTemplateName(coversheetTemplateMasterDto.getTemplateName());
				mst.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				mst.setUnitId(coversheetTemplateMasterDto.getUnitId());
				mst.setStatus('A');
				mst.setDoctorId(coversheetTemplateMasterDto.getDoctorId());
//				mst.setParentTemplateId(coversheetTemplateMasterDto.getParentTemplateId());
				mst.setTypeId(coversheetTemplateMasterDto.getTypeId());
				mst.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
				mst.setCreatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
				mst.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
				mst.setUpdatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
				 
				sessionFactory.getCurrentSession().save(mst);
				coversheetTemplateMasterDto.setCovTemplateId(mst.getCovTemplateId());
			}else {
				CoversheetTemplateMaster mst1=new CoversheetTemplateMaster();
				mst1.setTemplateName(coversheetTemplateMasterDto.getTemplateName());
				mst1.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				mst1.setUnitId(coversheetTemplateMasterDto.getUnitId());
				mst1.setStatus('A');
				mst1.setDoctorId(coversheetTemplateMasterDto.getDoctorId());
//				mst.setParentTemplateId(coversheetTemplateMasterDto.getParentTemplateId());
				mst1.setTypeId(coversheetTemplateMasterDto.getTypeId());
				mst1.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
				mst1.setCreatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
				mst1.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
				mst1.setUpdatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
				 
				sessionFactory.getCurrentSession().save(mst1);
				coversheetTemplateMasterDto.setCovTemplateId(mst1.getCovTemplateId());
				Integer id=coversheetTemplateMasterDto.getCovTemplateId();
				if(coversheetTemplateMasterDto.getTemplateTypeList()!= null && coversheetTemplateMasterDto.getTemplateTypeList().size() > 0) {
					for (CoversheetTemplateMasterDto dto : coversheetTemplateMasterDto.getTemplateTypeList()) {
						CoversheetTemplateMaster mst=new CoversheetTemplateMaster();
						mst.setTemplateName(coversheetTemplateMasterDto.getTemplateName());
						mst.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
						mst.setUnitId(coversheetTemplateMasterDto.getUnitId());
						mst.setStatus('A');
						mst.setDoctorId(coversheetTemplateMasterDto.getDoctorId());
						mst.setParentTemplateId(id);
						mst.setTypeId(coversheetTemplateMasterDto.getTypeId());
						mst.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
						mst.setCreatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
						mst.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
						mst.setUpdatedDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
						 
						sessionFactory.getCurrentSession().save(mst);
						coversheetTemplateMasterDto.setCovTemplateId(mst.getCovTemplateId());
					}
				}
			}
		return new Response(SUCCESS, SUCCESS_CODE, null, null, coversheetTemplateMasterDto);
		
		} catch (HibernateException he) {
		logger.error("saveNewEMRTemplate failed", he);
		} catch (Exception e) {
		logger.error("saveNewEMRTemplate failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
