package com.param.CSSD.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.CSSD.dao.IKitIssueDao;
import com.param.CSSD.dto.IssuedKitTransitionMapperDto;
import com.param.CSSD.dto.KitIssueDto;
import com.param.global.common.Response;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class KitIssueServiceImpl implements IKitIssueService {

	final static Logger logger = Logger.getLogger(KitIssueServiceImpl.class);


	@Autowired
	private IKitIssueDao iKitIssueDao;

	@Override
	@Transactional
	public Response createKitIssue(KitIssueDto kitIssueDto) {
		try {
			return iKitIssueDao.createKitIssue(kitIssueDto);
		} catch (Exception e) {
			logger.error("createKitIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getKitIssueByMultipleFields(Date fromDate,Date toDate,Integer fromStore,Integer toStore,Integer issueNo,String status,Integer organizationId,Integer unitId) {
		try {
			return iKitIssueDao.getKitIssueByMultipleFields(fromDate,toDate,fromStore,toStore,issueNo,status,organizationId,unitId);
		} catch (Exception e) {
			logger.error("getKitIssueByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getKitIssueByMultipleFieldsForAcceptance(Date fromDate,Date toDate,Integer fromStore,Integer toStore,Integer issueNo,String status,Integer organizationId,Integer unitId) {
		try {
			return iKitIssueDao.getKitIssueByMultipleFieldsForAcceptance(fromDate,toDate,fromStore,toStore,issueNo,status,organizationId,unitId);
		} catch (Exception e) {
			logger.error("getKitIssueByMultipleFieldsForAcceptance failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getKitIssueByIssueNo(Integer issueNo,Integer organizationId,Integer unitId) {
		try {
			return iKitIssueDao.getKitIssueByIssueNo(issueNo,organizationId,unitId);
		} catch (Exception e) {
			logger.error("getKitIssueByIssueNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response updateKitIssue(KitIssueDto kitIssueDto) {
		try {
			return iKitIssueDao.updateKitIssue(kitIssueDto);
		} catch (Exception e) {
			logger.error("updateKitIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response updateKitIssueStatus(List<KitIssueDto> kitIssueDtoList) {
		try {
			return iKitIssueDao.updateKitIssueStatus(kitIssueDtoList);
		} catch (Exception e) {
			logger.error("updateKitIssueStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response cancelKitIssue(KitIssueDto kitIssueDto) {
		try {
			return iKitIssueDao.cancelKitIssue(kitIssueDto);
		} catch (Exception e) {
			logger.error("cancelKitIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response acceptRejectKits(KitIssueDto kitIssueDto) {
		try {
			List<IssuedKitTransitionMapperDto> subKitList = kitIssueDto.getSubKitList();
			
			if(!subKitList.isEmpty()) {
				for (IssuedKitTransitionMapperDto issuedKitTransitionMapperDto : subKitList) {
					if(issuedKitTransitionMapperDto.getIsAccepted()) {
						this.acceptKitIssue(kitIssueDto.getOrganizationId(),kitIssueDto.getUnitId(),kitIssueDto.getIssueNo(),issuedKitTransitionMapperDto.getTransitionNumber());
					}else if(issuedKitTransitionMapperDto.getIsRejected()) {
						this.rejectKitIssue(kitIssueDto.getOrganizationId(),
								kitIssueDto.getUnitId(),kitIssueDto.getIssueNo(),
								issuedKitTransitionMapperDto.getTransitionNumber(),
								issuedKitTransitionMapperDto.getRejectReasonId(),issuedKitTransitionMapperDto.getQuantity(),
								issuedKitTransitionMapperDto.getRejectReasonNote());
					}
				}
			}else {
				return new Response("error", null, "Something went wrong.", null, null);
			}
			return new Response("success", null, "Kit Acceptance Done Successfully.", null, null);
			
		} catch (Exception e) {
			logger.error("acceptRejectKits failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	@Transactional
	public Response acceptKitIssue(Integer organizationId,Integer unitId,Integer issueNo,Integer transitionNumber) {
		try {
			Response resp = iKitIssueDao.acceptKitIssue(organizationId,unitId,issueNo,transitionNumber);
			if(resp.getStatus().equals("success") )
			{
				iKitIssueDao.changeKitIssueStatus(organizationId,unitId,issueNo);
				return resp;
			}
		} catch (Exception e) {
			logger.error("acceptKitIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
		return new Response("ERROR", null, null, null, null);
	}
	
	@Override
	@Transactional
	public Response rejectKitIssue(Integer organizationId,Integer unitId,Integer issueNo,Integer transitionNumber,Integer rejectReasonId,Integer quantity,String rejectionNote) {
		try {
			Response resp = iKitIssueDao.rejectKitIssue(organizationId,unitId,issueNo,transitionNumber,rejectReasonId,quantity,rejectionNote);
			if(resp.getStatus().equals("success"))
			{
				iKitIssueDao.changeKitIssueStatus(organizationId,unitId,issueNo);
				return resp;
			}
			
		} catch (Exception e) {
			logger.error("rejectKitIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
		return new Response("ERROR", null, null, null, null);
	}
	
	
	
	@Override
	@Transactional
	public Response getKitIndentByMultipleFieldsForKitIssue(Date fromDate,Date toDate,Integer fromStore,Integer toStore,Integer indentNo,Integer organizationId,Integer unitId) {
		try {
			return iKitIssueDao.getKitIndentByMultipleFieldsForKitIssue(fromDate,toDate,fromStore,toStore,indentNo,organizationId,unitId);
		} catch (Exception e) {
			logger.error("getKitIndentByMultipleFieldsForKitIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	
	@Override
	@Transactional
	public Response getSterilizationRequestByMultipleFieldsForKitIssue(Date fromDate,Date toDate,Integer requestingStore,Integer issueingStore,Integer requestNo,Integer organizationId,Integer unitId) {
		try {
			return iKitIssueDao.getSterilizationRequestByMultipleFieldsForKitIssue(fromDate,toDate,requestingStore,issueingStore,requestNo,organizationId,unitId);
		} catch (Exception e) {
			logger.error("getSterilizationRequestByMultipleFieldsForKitIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	@Transactional
	public Response getKitIssueList() {
		try {
			return iKitIssueDao.getKitIssueList();
		} catch (Exception e) {
			logger.error("getKitIssueList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getKitListByIssueNo(Integer issueNo) {
		try {
			return iKitIssueDao.getKitListByIssueNo(issueNo);
		} catch (Exception e) {
			logger.error("getKitListByIssueNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getKitIssueByIssueNo(Integer issueNo) {
		try {
			return iKitIssueDao.getKitIssueByIssueNo(issueNo);
		} catch (Exception e) {
			logger.error("getKitIssueByIssueNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getKitListForKitIssue() {
		try {
			return iKitIssueDao.getKitListForKitIssue();
		} catch (Exception e) {
			logger.error("getKitListForKitIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	@Transactional
	public Response stopIssue(Integer batchNo) {
		try {
			return iKitIssueDao.stopIssue(batchNo);
		} catch (Exception e) {
			logger.error("stopIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	@Transactional
	public Response acceptKitIssue(Integer issueNo) {
		try {
			return iKitIssueDao.acceptKitIssue(issueNo);
		} catch (Exception e) {
			logger.error("acceptKitIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response rejectKitIssue(Integer issueNo,String reason) {
		try {
			return iKitIssueDao.rejectKitIssue(issueNo,reason);
		} catch (Exception e) {
			logger.error("rejectKitIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
}