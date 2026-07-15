package com.param.opd.coversheet.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.AdmissionOpdDTO;
import com.param.opd.model.AdmissionModel;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

/*
 * 
 * Created by Tushar
 */
@SuppressWarnings("rawtypes")
public interface IAdmissionDAO extends IGenericDao<AdmissionModel, Integer>{
	
	public AdmissionModel saveAdmission(AdmissionOpdDTO admissionOpdDTO)throws ApplicationException;
	
	public AdmissionModel updateAdmission(AdmissionOpdDTO admissionOpdDTO)throws ApplicationException;
	
	public AdmissionModel getAdmission(Integer admissionId,Integer instruction_post_id)throws ApplicationException;
	public Response getAdmissionsForBatchProcess(int startPoint, int maxResults, List<Integer> unitIds)throws ApplicationException;
	public Response getAdmissionByIdForBatchPServicePosting(int admissionId)throws ApplicationException;
	public Response getAdmissionsForPeriodicPosting(int startPoint, int maxResults, List<Integer> unitIds) throws ApplicationException;
}
