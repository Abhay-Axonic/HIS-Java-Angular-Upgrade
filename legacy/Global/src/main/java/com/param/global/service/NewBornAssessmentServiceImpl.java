package com.param.global.service;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.INewBornApgarDao;
import com.param.global.dao.INewBornAssessmentDao;
import com.param.global.dto.NewBornApgarDto;
import com.param.global.dto.NewBornAssessmentDto;
import com.param.global.model.NewBornAssessmentApgar;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({ "rawtypes", "unchecked" })
@Service
public class NewBornAssessmentServiceImpl implements INewBornAssessmentService, ICommonConstants {
	final static Logger logger = Logger.getLogger(NewBornAssessmentServiceImpl.class);


	@Autowired
	private INewBornAssessmentDao iNewBornAssessmentDao;
	
	@Autowired
	private INewBornApgarDao iNewBornApgarDao;
	
	
	@Override 
	public Response saveNewBornAssessment(NewBornAssessmentDto assessmentDto) throws ApplicationException {
		try {
			Response res = iNewBornAssessmentDao.saveNewBornAssessment(assessmentDto);
			if(res.getObject()!=null && assessmentDto.getApgarArrList()!=null) {
				int id = (Integer)res.getObject();
				Iterator<NewBornApgarDto> itr = assessmentDto.getApgarArrList().iterator();
				while(itr.hasNext()) {
					NewBornApgarDto dto=itr.next();
					try {
						NewBornAssessmentApgar apagr = new NewBornAssessmentApgar();
						apagr.setAssessmentId(id);
						apagr.setColorScore(dto.getColorScore());
						apagr.setHeartRateScore(dto.getHeartRateScore());
						apagr.setMuscleScore(dto.getMuscleScore());
						apagr.setReflexScore(dto.getReflexScore());
						apagr.setRespirationScore(dto.getRespirationScore());
						apagr.setTime(dto.getTime());
						apagr.setTotalScore(dto.getTotalScore());
						apagr.setApgarRemark(dto.getApgarRemark());
						apagr.setStatus('A');
						apagr.setCreatedBy(assessmentDto.getCreatedBy());
						apagr.setCreatedDate(assessmentDto.getCreatedDate().toString());
						apagr.setUpdatedBy(assessmentDto.getUpdatedBy());
						apagr.setUpdatedDate(assessmentDto.getUpdatedDate().toString());
						apagr.setOrganizationId(assessmentDto.getOrganizationId());
						apagr.setUnitId(assessmentDto.getUnitId());
						
						iNewBornAssessmentDao.saveNewBornAssessmentApgar(apagr);
						new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
					} catch (Exception e) {
					logger.error("saveNewBornAssessment: apgar record save failed", e);
					}
					new Response(ERROR, null, null, null, null);
				}
			}
			return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
		} catch (Exception e) {
		logger.error("saveNewBornAssessment failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	

	@Override
	public Response getNewBornAssessmentDetails(NewBornAssessmentDto assessmentDto) throws ApplicationException {
		try {
			return iNewBornAssessmentDao.getNewBornAssessmentDetails(assessmentDto);
			}catch (Exception e) {
			logger.error("getNewBornAssessmentDetails failed", e);
			}
			return new Response(ERROR, null, null, null, null);
		}


	@Override 
	public Response updateNewBornAssessment(NewBornAssessmentDto assessmentDto) throws ApplicationException {
		try {
/*            Iterator<NewBornApgarDto> itr = assessmentDto.getApgarArrList().iterator();
            while(itr.hasNext()) {
                NewBornApgarDto dto=itr.next();
                if(dto.getApgarId()==0) {
                	dto.setAssessmentId(assessmentDto.getAssessmentId());
                	dto.setCreatedBy(assessmentDto.getUpdatedBy());
                	dto.setCreatedDate(assessmentDto.getUpdatedDate());
                	dto.setOrganizationId(assessmentDto.getOrganizationId());
                	dto.setUnitId(assessmentDto.getUnitId());
                }else {
					dto.setUpdatedBy(assessmentDto.getUpdatedBy());
					dto.setUpdatedDate(assessmentDto.getUpdatedDate());
                }
                iNewBornApgarDao.updateNewBornApgar(dto);
            }*/
			return iNewBornAssessmentDao.updateNewBornAssessment(assessmentDto);
		} catch (Exception e) {
		logger.error("updateNewBornAssessment failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
}
