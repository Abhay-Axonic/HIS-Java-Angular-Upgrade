package com.param.global.org.common.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.param.adt.admission.model.NutritionalAssessmentScore;
import com.param.adt.employeesurvey.EmployeeSurvey;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.org.common.dto.EmployeeSurveyDto;
import com.param.global.org.common.dto.EmployeeSurveyListDto;
import com.param.global.unit.dto.CabinMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;


@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class EmployeeSurveyDaoImpl extends GenericDao<EmployeeSurvey, Integer>implements IEmployeeSurveyDao,ICommonConstants{

	private static final Logger logger = Logger.getLogger(EmployeeSurveyDaoImpl.class);

	@Autowired
    private SessionFactory sessionFactory;
	
	public EmployeeSurveyDaoImpl() {
		super(EmployeeSurvey.class);
		
	}

	
	

	@Override
	public Response saveEmployeeSurvey(EmployeeSurveyDto employeeSurveyDto) throws ApplicationException {
		
		try{
			for(EmployeeSurveyListDto idx: employeeSurveyDto.getSatisfactionList()){
				EmployeeSurvey employeeSurvey = new EmployeeSurvey();
				employeeSurvey.setPointId(idx.getId());
				employeeSurvey.setSelectedOption(idx.getSelectedOption());
				employeeSurvey.setOrganizationId(employeeSurveyDto.getOrganizationId());
				employeeSurvey.setUnitId(employeeSurveyDto.getUnitId());
				employeeSurvey.setCreatedBy(employeeSurveyDto.getCreatedBy());
				employeeSurvey.setUpdatedBy(employeeSurveyDto.getUpdatedBy());
				employeeSurvey.setCreatedDate(GlobalCommonDateUtils.getDate(employeeSurveyDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				employeeSurvey.setUserId(employeeSurveyDto.getUserId());
				employeeSurvey.setUpdatedDate(GlobalCommonDateUtils.getDate(employeeSurveyDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				employeeSurvey.setStatus(employeeSurveyDto.getStatus());
				employeeSurvey.setRating(employeeSurveyDto.getRating());
				save(employeeSurvey);
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);


			} catch (HibernateException he) {
				logger.error("Error while saving employee survey", he);
				} catch (Exception e) {
					logger.error("Error while saving employee survey", e);
				}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response updateEmployeeSurvey(EmployeeSurveyDto employeeSurveyDto) throws ApplicationException {
		try {
			for(EmployeeSurveyListDto idx: employeeSurveyDto.getSatisfactionList()){
			EmployeeSurvey employeeSurvey=findById(employeeSurveyDto.getEmployeeSurveyId());
			employeeSurvey.setPointId(idx.getId());
			employeeSurvey.setSelectedOption(idx.getSelectedOption());
			employeeSurvey.setOrganizationId(employeeSurveyDto.getOrganizationId());
			employeeSurvey.setUnitId(employeeSurveyDto.getUnitId());
			employeeSurvey.setUpdatedBy(employeeSurveyDto.getUpdatedBy());
			employeeSurvey.setUpdatedDate(GlobalCommonDateUtils.getDate(employeeSurveyDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
		 
		update(employeeSurvey);
			}
	return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

	} catch (HibernateException he) {
		logger.error("Error while updating employee survey", he);
	} catch (Exception e) {
		logger.error("Error while updating employee survey", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}



    @Transactional
	@Override
	public Response getEmployeeSurveyById(Integer userId, Integer organizationId , Integer unitId)
			throws ApplicationException {
		try {
			List<EmployeeSurveyListDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_EMPLOYEE_SURVEY_BY_ID")
					.setInteger("userId", userId)
					.setInteger("organizationId", organizationId)
					.setInteger("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(EmployeeSurveyListDto.class)).list();
	
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			throw new ApplicationException(he.getMessage(), he);
		} catch (Exception e) {
			throw e;
		}
	}




	@Override
	public Response inactiveSavedEmployeeSurveyList(List<EmployeeSurveyListDto> employeeSurveyListDto) {
		try {
			
            for (EmployeeSurveyListDto item : employeeSurveyListDto) {
            	
            	EmployeeSurvey employeeSurvey = findById(item.getEmployeeSurveyId());
                if (employeeSurvey != null) {
            	employeeSurvey.setStatus('I');
            	employeeSurvey.setUpdatedBy(item.getUpdatedBy());
                sessionFactory.getCurrentSession().update(employeeSurvey);
                }
            }

            return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
        } catch (Exception e) {
            throw e;
        }
		
	}

	
	
}
