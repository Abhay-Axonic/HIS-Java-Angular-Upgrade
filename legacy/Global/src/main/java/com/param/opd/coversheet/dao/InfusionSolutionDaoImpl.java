package com.param.opd.coversheet.dao;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dto.InfusionAdditiveDto;
import com.param.opd.coversheet.dto.InfusionSolutionDto;
import com.param.opd.coversheet.dto.PrescriptionDetailsDto;
import com.param.opd.coversheet.model.InfusionAdditiveMaster;
import com.param.opd.coversheet.model.InfusionSolutionMaster;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class InfusionSolutionDaoImpl extends GenericDao<InfusionSolutionMaster, Integer> implements IInfusionSolutionDao,ICommonConstants
{
	final static Logger logger = Logger.getLogger(InfusionSolutionDaoImpl.class);


	public InfusionSolutionDaoImpl() {
		super(InfusionSolutionMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	DozerBeanMapper mapper;
	
	@Override
	public Response saveInfusionSolution(InfusionSolutionDto infusionSolutionDto) throws ApplicationException {
		try {

			InfusionSolutionMaster infusionSolutionMaster = mapper.map(infusionSolutionDto, InfusionSolutionMaster.class,
					"InfusionSolutionDto_to_InfusionSolutionMaster");
			infusionSolutionMaster = save(infusionSolutionMaster);
			infusionSolutionDto.setInfusionSolutionId(infusionSolutionMaster.getInfusionSolutionId());
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, infusionSolutionDto);
		} catch (HibernateException he) {
		logger.error("saveInfusionSolution failed", he);
		} catch (Exception e) {
		logger.error("saveInfusionSolution failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
	
	@Override
	public Response saveInfusionAdditive(InfusionAdditiveDto infusionAdditiveDto) throws ApplicationException {
		try {

			InfusionAdditiveMaster infusionAdditiveMaster = mapper.map(infusionAdditiveDto, InfusionAdditiveMaster.class,
					"InfusionAdditiveDto_to_InfusionAdditiveMaster");
			Integer infusionAdditiveId =(Integer) sessionFactory.getCurrentSession().save(infusionAdditiveMaster);
			infusionAdditiveDto.setInfusionadditiveId(infusionAdditiveId);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, infusionAdditiveDto);
		} catch (HibernateException he) {
		logger.error("saveInfusionAdditive failed", he);
		} catch (Exception e) {
		logger.error("saveInfusionAdditive failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	public Response getInfusionSolutionItemList(InfusionSolutionDto infusionSolutionDto) throws ApplicationException {
		try {
			List<InfusionSolutionDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INFUSION_SOLUSION_ITEM_LIST")
					.setParameter("organizationId", infusionSolutionDto.getOrganizationId())
					.setParameter("unitId", infusionSolutionDto.getUnitId()) 
					.setParameter("name", "%" + infusionSolutionDto.getName().toLowerCase() + "%")
					.setResultTransformer(Transformers.aliasToBean(InfusionSolutionDto.class)).list();
	
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getInfusionSolutionItemList failed", he);
		} catch (Exception e) {
		logger.error("getInfusionSolutionItemList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response getInfusionAdditiveItemList(InfusionSolutionDto infusionSolutionDto) throws ApplicationException {
		try {
			List<InfusionSolutionDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INFUSION_ADDITIVE_ITEM_LIST")
					.setParameter("organizationId", infusionSolutionDto.getOrganizationId())
					.setParameter("unitId", infusionSolutionDto.getUnitId())
					.setParameter("name", "%" + infusionSolutionDto.getName().toLowerCase() + "%")
					.setResultTransformer(Transformers.aliasToBean(InfusionSolutionDto.class)).list();
	
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getInfusionAdditiveItemList failed", he);
		} catch (Exception e) {
		logger.error("getInfusionAdditiveItemList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	

	@Override
	public Response getListOfInfusion(InfusionSolutionDto infusionSolutionDto) throws ApplicationException {
		try {
			List<InfusionSolutionDto> list2=new ArrayList<>();
			List<InfusionSolutionDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LIST_OF_INFUSION_SOLUSION")
					.setParameter("organizationId", infusionSolutionDto.getOrganizationId())
					.setParameter("unitId", infusionSolutionDto.getUnitId())
					.setParameter("patientId", infusionSolutionDto.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(InfusionSolutionDto.class)).list();
			
			for(InfusionSolutionDto dto : list) {
				
				List<InfusionAdditiveDto> list1=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_ADDITIVE_LIST_BY_ID")
						.setParameter("organizationId", infusionSolutionDto.getOrganizationId())
						.setParameter("unitId", infusionSolutionDto.getUnitId())
						.setParameter("infusionSolutionId", dto.getInfusionSolutionId())
						.setResultTransformer(Transformers.aliasToBean(InfusionAdditiveDto.class)).list();
				dto.setInfusionAdditiveDtosList(list1);
				
				
		         String query = " select cast(pd.id  as int4)as \"prescriptionDetailsid\",  "
		         		+ " pd.item_id as \"itemId\" "
		         		+  " from pharmacy.t_prescription_detail pd " 
		         		+  "  where pd.infusion_solution_id=:infusionSolutionId"
		         		+ " AND pd.is_medication_stop = 'N'";
			
		         List<PrescriptionDetailsDto> list3  = sessionFactory.getCurrentSession()
					    .createSQLQuery(query)
						.setParameter("infusionSolutionId", dto.getInfusionSolutionId())
						.setResultTransformer(Transformers.aliasToBean(PrescriptionDetailsDto.class)).list();
				dto.setPrescriptionDetailsDto(list3);
				
				list2.add(dto);
			}
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getListOfInfusion failed", he);
		} catch (Exception e) {
		logger.error("getListOfInfusion failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAdditiveListById(InfusionSolutionDto infusionSolutionDto) throws ApplicationException {
		try {
			List<InfusionAdditiveDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ADDITIVE_LIST_BY_ID")
					.setParameter("organizationId", infusionSolutionDto.getOrganizationId())
					.setParameter("unitId", infusionSolutionDto.getUnitId())
					.setParameter("infusionSolutionId", infusionSolutionDto.getInfusionSolutionId())
					.setResultTransformer(Transformers.aliasToBean(InfusionAdditiveDto.class)).list();
	
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getAdditiveListById failed", he);
		} catch (Exception e) {
		logger.error("getAdditiveListById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateInfusionStopFlag(InfusionSolutionDto infusionSolutionDto) throws ApplicationException {
        try {
			
			Session session=sessionFactory.getCurrentSession();
			session.createSQLQuery(" update opd.m_infusion_solution  set is_start_Flag = '"+infusionSolutionDto.getIsStartFlag()
			+"' , justification_stop_infusion = '"+infusionSolutionDto.getJustificationStopInfusion()
			+"' , updated_date = '"+TimeZoneComponent.getDateByzone(infusionSolutionDto.getUnitId())
			+ "' where patient_id="+infusionSolutionDto.getPatientId()
			+ " and infusion_solution_id ="+infusionSolutionDto.getInfusionSolutionId())
			.executeUpdate();
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			
			} catch (HibernateException he) {
			logger.error("updateInfusionStopFlag failed", he);
			} catch (Exception e) {
			logger.error("updateInfusionStopFlag failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public List<PrescriptionDetailsDto> getPresDetIdAndPresId(Integer infusionSolutionId) {
		String query="select cast(tpd2.prescription_id as int) as \"prescriptionId\",cast(tpd2.id as int) as \"prescriptionDetailsid\" from pharmacy.t_prescription_detail tpd2 where tpd2.infusion_solution_id =:infusionSolutionId";
		List<PrescriptionDetailsDto> list3  = sessionFactory.getCurrentSession()
			    .createSQLQuery(query)
				.setParameter("infusionSolutionId", infusionSolutionId)
				.setResultTransformer(Transformers.aliasToBean(PrescriptionDetailsDto.class)).list();
		return (List<PrescriptionDetailsDto>) list3;
	}

	@Override
	public PrescriptionDetailsDto updatePresDetIdAndPresId(PrescriptionDetailsDto prescriptionDetailsDto) {
        sessionFactory.getCurrentSession().createSQLQuery(" update pharmacy.t_issue set status_id = 9 where prescription_id ="+prescriptionDetailsDto.getPrescriptionId())
        .executeUpdate();
        
        sessionFactory.getCurrentSession().createSQLQuery(" update pharmacy.t_issue_detail set status_id = 9 where prescription_detail_id ="+prescriptionDetailsDto.getPrescriptionDetailsid())
        .executeUpdate();
		return null;
	}

	

}
