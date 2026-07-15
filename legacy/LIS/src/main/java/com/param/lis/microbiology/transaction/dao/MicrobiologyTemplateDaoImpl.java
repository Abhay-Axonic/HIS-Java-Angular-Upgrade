package com.param.lis.microbiology.transaction.dao;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.transaction.MicrobiologyTemplate;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.transaction.dto.MicrobiologyTemplateDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unused","unchecked" })
public class MicrobiologyTemplateDaoImpl extends GenericDao<MicrobiologyTemplate, Integer>
		implements IMicrobiologyTemplateDao, ICommonConstants, ITransactionConstants, IError {

	@Autowired
	private SessionFactory sessionFactory;

	public MicrobiologyTemplateDaoImpl() {
		super(MicrobiologyTemplate.class);
	}

	@Autowired
	private DozerBeanMapper mapper;

	final static Logger logger = Logger.getLogger(MicrobiologyDaoImpl.class);

	@Override
	public Response saveTemplate(MicrobiologyTemplateDto dto) throws ApplicationException {
		try {
				
				MicrobiologyTemplate master =  new MicrobiologyTemplate();
				master.setIsFinalRelease(dto.getIsFinalRelease()!= null ? dto.getIsFinalRelease(): null);
				master.setIsProvisionalRelease(dto.getIsProvisionalRelease()!=null ? dto.getIsProvisionalRelease(): null);
				master.setTitle(dto.getTitle());
				master.setTemplateObject(dto.getTemplateObject());
				master.setTemplateId(dto.getTemplateId());
				master.setLabSampleDtlsId(dto.getLabSampleDtlsId());
//				master.setCreatedBy(dto.getCreatedBy());
				master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
				master.setOrganizationId(dto.getOrganizationId());
				master.setStatus(ACTIVE);
				master.setUpdatedBy(dto.getUpdatedBy());
				master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
				master.setUnitId(dto.getUnitId());
				master.setIsReportRecall(dto.getIsReportRecall());
				if(dto.getMicrobiologyTemplateId() != null) {
					
					MicrobiologyTemplate mstr  = (MicrobiologyTemplate) sessionFactory.getCurrentSession().get(MicrobiologyTemplate.class,dto.getMicrobiologyTemplateId());
					if(dto.getIsProvisionalRelease()!=null && dto.getIsProvisionalRelease()=='Y' && ( dto.getIsFinalRelease()==null || dto.getIsFinalRelease()== 'N')) {
						master.setReportEntryDate(mstr.getReportEntryDate()!=null ? mstr.getReportEntryDate() :TimeZoneComponent.getDateByzone(dto.getUnitId()));
					}
					master.setCreatedBy(mstr.getCreatedBy());
					if(dto.getIsFinalRelease()!=null && dto.getIsFinalRelease()== 'Y') {
						master.setReportEntryDate(mstr.getReportEntryDate()!=null ? mstr.getReportEntryDate(): TimeZoneComponent.getDateByzone(dto.getUnitId()));
						master.setReportValidationDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
						master.setValidatedBy(mstr.getValidatedBy() != null ? mstr.getValidatedBy() : dto.getCreatedBy());
					}
					sessionFactory.getCurrentSession().evict(mstr);
					master.setMicrobiologyTemplateId(dto.getMicrobiologyTemplateId());
					master = update(master);
					if(master.getReportEntryDate()!=null) {
						dto.setReportEntryDate(GlobalCommonDateUtils.getStringDate(master.getReportEntryDate(),"dd/MM/yyyy hh:mm aa"));
					}
					if(master.getReportValidationDate()!=null) {
						dto.setReportValidationDate(GlobalCommonDateUtils.getStringDate(master.getReportValidationDate(),"dd/MM/yyyy hh:mm aa"));
					}
					return new Response<>(SUCCESS, null, "Updated SuccessFully", null, dto);
				}else {
					master.setCreatedBy(dto.getCreatedBy());
					if(dto.getIsProvisionalRelease()!=null && dto.getIsProvisionalRelease()=='Y') {
						master.setReportEntryDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
					} 
					master = save(master);
					if(master.getReportEntryDate()!=null) {
						dto.setReportEntryDate(GlobalCommonDateUtils.getStringDate(master.getReportEntryDate(),"dd/MM/yyyy hh:mm aa"));
					}
					return new Response<>(SUCCESS, null, "Save Successfully", null, dto);
				}
				
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response getTemplate(MicrobiologyTemplateDto microbiologyTemplateDto) throws ApplicationException {
		try {
			MicrobiologyTemplateDto dto=(MicrobiologyTemplateDto) sessionFactory.getCurrentSession().createSQLQuery("SELECT " + 
					"	microTemp.microbiology_template_id as \"microbiologyTemplateId\"," + 
					"	microTemp.template_id as \"templateId\"," + 
					"	microTemp.lab_sample_dtls_id as \"labSampleDtlsId\"," + 
					"	microTemp.title as \"title\"," + 
					"	microTemp.template_object as \"templateObject\"," + 
					"	microTemp.is_provisional_release as \"isProvisionalRelease\"," + 
					"	microTemp.is_final_release as \"isFinalRelease\" ,"
					+ " tempmaster.template_format_id as \"templateFormatId\", "
					+ " tempmaster.template_type_id as \"templateTypeId\" " + 
					" FROM " + 
					"	lab.t_microbiology_template microTemp"
					+ " inner join  public.t_template_master tempmaster on microTemp.template_id = tempmaster.template_id " + 
					" WHERE " + 
					"	microTemp.lab_sample_dtls_id =:labsampleDetilId ")
					.setParameter("labsampleDetilId",microbiologyTemplateDto.getLabSampleDtlsId())
					.setResultTransformer(Transformers.aliasToBean(MicrobiologyTemplateDto.class))
					.uniqueResult();
		 return new Response(SUCCESS, null, null, null, dto);	
	} catch (HibernateException he) {
		throw he;
	} catch (Exception e) {
		throw e;
	}
	}
	
	@Override
	public Response updatetMcrobiologyTemplate(SearchDto searchDto) throws ApplicationException {
		try {

			MicrobiologyTemplate mstr = findById(searchDto.getMicrobiologyTemplateId());
			mstr.setReportReleaseDate(searchDto.getReportReleaseDate() != null
					? GlobalCommonDateUtils.getDate(searchDto.getReportReleaseDate(),"dd/MM/yyyy hh:mm aa"):TimeZoneComponent.getDateByzone(searchDto.getOrgUnitId()));
			update(mstr);
			MicrobiologyTemplateDto dto = new MicrobiologyTemplateDto();
			dto.setReportReleaseDate(GlobalCommonDateUtils.getStringDate(mstr.getReportReleaseDate(),"dd/MM/yyyy hh:mm aa"));
			 return new Response(SUCCESS, null, null, null, dto);	

		} catch (Exception e) {
			throw e;
		}
	}
}
