package com.param.lis.microbiology.transaction.service;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.microbiology.transaction.dao.IMicrobiologyDao;
import com.param.lis.microbiology.transaction.dao.IMicrobiologyTemplateDao;
import com.param.lis.microbiology.transaction.dto.TMicroSampleMediaDto;
import com.param.lis.transaction.dto.BioChemParamDto;
import com.param.lis.transaction.dto.LabSampleDetailsMasterDto;
import com.param.lis.transaction.dto.LabSampleMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MicrobiologyServiceImpl implements IMicrobiologyService, ICommonConstants, IError {
	@Autowired
	IMicrobiologyDao iMicrobiologyDao;
	
	@Autowired
	IMicrobiologyTemplateDao iMicrobiologyTemplateDao;
	
	final static Logger logger = Logger.getLogger(MicrobiologyServiceImpl.class);

	@Override
	@Transactional
	public Response microAcceptancePending(BioChemParamDto bioChemParamDto) throws ApplicationException {
		try {
			return iMicrobiologyDao.microAcceptancePending(bioChemParamDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response microAcceptancePendingCount(BioChemParamDto bioChemParamDto) throws ApplicationException {
		try {
			return iMicrobiologyDao.microAcceptancePendingCount(bioChemParamDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response microChemistryAcceptRejectSample(List<LabSampleDetailsMasterDto> listlabSampleDetailsMasterDto)
			throws ApplicationException {
		try {
			return iMicrobiologyDao.microChemistryAcceptRejectSample(listlabSampleDetailsMasterDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getMicroChemistryWorklist(BioChemParamDto bioChemParamDto) throws ApplicationException {
		try {
			return iMicrobiologyDao.getMicroChemistryWorklist(bioChemParamDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getMicroChemistryWorklistCount(BioChemParamDto bioChemParamDto) throws ApplicationException {
		try {
			return iMicrobiologyDao.getMicroChemistryWorklistCount(bioChemParamDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response sendForIncubationObjservation(List<LabSampleDetailsMasterDto> listlabSampleDetailsMasterDto)
			throws ApplicationException {
		try {
			
			for (LabSampleDetailsMasterDto labSampleDetailsMasterDto : listlabSampleDetailsMasterDto) {

				Integer labSampleDtlsId = labSampleDetailsMasterDto.getLabSampleDtlsId();
				
				Integer orgId = labSampleDetailsMasterDto.getOrgId();
				
				Integer orgUniId = labSampleDetailsMasterDto.getOrgUnitId();
				
				Response res = iMicrobiologyDao.getAddedMediaForIncuabation(labSampleDtlsId, orgId, orgUniId);
				if(res.getListObject()== null || res.getListObject().size()==0) {
					return new Response(ERROR, ERR_500, "Please add media Against all selected list ", null, null);
				}
			}
			
			
			return iMicrobiologyDao.sendForIncubationObjservation(listlabSampleDetailsMasterDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateLabSampleDetailsMaster(LabSampleDetailsMasterDto labSampleDetailsMasterDto)
			throws ApplicationException {
		try {
			return iMicrobiologyDao.updateLabSampleDetailsMaster(labSampleDetailsMasterDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getAddedMediaForIncuabation(Integer labSampleDtlsId, Integer orgId, Integer orgUniId)
			throws ApplicationException {
		try {
			return iMicrobiologyDao.getAddedMediaForIncuabation(labSampleDtlsId, orgId, orgUniId);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response addMediaForIncuabation(List<TMicroSampleMediaDto> listTMicroSampleMediaDto)
			throws ApplicationException {
		try {
			
			Integer labSampleDtlsId = listTMicroSampleMediaDto.getFirst().getLabSampleDtlsId();
			
			Integer orgId = listTMicroSampleMediaDto.getFirst().getOrgId();
			
			Integer orgUniId = listTMicroSampleMediaDto.getFirst().getOrgUnitId();
			
			
			Response res = iMicrobiologyDao.getAddedMediaForIncuabation(labSampleDtlsId, orgId, orgUniId);
			if(res.getListObject()!= null || res.getListObject().size()>0) {
				List<TMicroSampleMediaDto> dtoList = res.getListObject();
				for (TMicroSampleMediaDto tMicroSampleMediaDto : listTMicroSampleMediaDto) {
					
					for (TMicroSampleMediaDto tMicroSampleMediaDto2 : dtoList) {
						
						if(tMicroSampleMediaDto.getMediaId() == tMicroSampleMediaDto2.getMediaId()) {
							
							tMicroSampleMediaDto.settSampleMediaId(tMicroSampleMediaDto2.gettSampleMediaId());
							tMicroSampleMediaDto.setUpdatedBy(tMicroSampleMediaDto.getCreatedBy());
							tMicroSampleMediaDto.setUpdatedDate(TimeZoneComponent.getDateByzone(orgUniId).getTime());
						}
					}
					
				}
				 return iMicrobiologyDao.addMediaForIncuabation(listTMicroSampleMediaDto);
			}else {
				return iMicrobiologyDao.addMediaForIncuabation(listTMicroSampleMediaDto);
			}
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response microAcceptancePendingSearch(SearchDto searchDto) throws ApplicationException {
		try {
			return iMicrobiologyDao.microAcceptancePendingSearch(searchDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getMicroChemistryWorklistSearch(SearchDto searchDto) throws ApplicationException {
		try {
			return iMicrobiologyDao.getMicroChemistryWorklistSearch(searchDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getMicroTempletWorkbeanchSearch(SearchDto searchDto) throws ApplicationException {
		try {
			return iMicrobiologyDao.getMicroTempletWorkbeanchSearch(searchDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateLabDetailsStatus(SearchDto searchDto) throws ApplicationException {
		try {
			Response res =  iMicrobiologyDao.updateLabDetailsStatus(searchDto);
			if(searchDto.getOrderStatusId() == 41) {
				Response res1 = iMicrobiologyTemplateDao.updatetMcrobiologyTemplate(searchDto);
				if(res1.getObject()!=null) {
					res.setObject(res1.getObject());
				}
			}
			return res;
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response<LabSampleMasterDto, Integer> getDetailsBySampleDetailId(Integer labSampleDetailId) {
		try {
			return iMicrobiologyDao.getDetailsBySampleDetailId(labSampleDetailId);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

}
