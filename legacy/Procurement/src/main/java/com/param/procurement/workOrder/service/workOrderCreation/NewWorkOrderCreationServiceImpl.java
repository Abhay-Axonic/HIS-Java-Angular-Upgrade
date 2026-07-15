package com.param.procurement.workOrder.service.workOrderCreation;



import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.constants.Screen;
import com.param.global.common.DocUploadUtility;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.org.dao.IGlobalOrganisationMasterDao;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.global.pharmacy.dto.WorkOrderApprovalSettlementDto;
import com.param.global.pharmacy.dto.WorkOrderCreationApprovalDto;
import com.param.global.pharmacy.dto.WorkOrderCreationRequestDocumentDto;
import com.param.global.pharmacy.dto.WorkOrderCreationRequestDto;
import com.param.procurement.workOrder.dao.workOrderCreation.INewWorkOrderCreationDao;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unused","unchecked"})
public class NewWorkOrderCreationServiceImpl implements INewWorkOrderCreationService,ICommonConstants,IError{

	final static Logger logger = Logger.getLogger(NewWorkOrderCreationServiceImpl.class);

	private Integer screenId = Screen.WorkorderQuotationCreation.getScreen();

	@Autowired
	private INewWorkOrderCreationDao  iNewWorkOrderCreationDao;

	@Autowired
	private IGlobalOrganisationMasterDao iGlobalOrganisationMasterDao;

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private DocUploadUtility docUploadUtility;


	@Override
	@Transactional
	public Response saveCreationRequest(WorkOrderCreationRequestDto requestDto) throws ApplicationException {
		try {
			return iNewWorkOrderCreationDao.saveCreationRequest(requestDto);
		} catch (Exception e) {
			logger.error("saveCreationRequest failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response workorderRequestDocument(WorkOrderCreationRequestDocumentDto requestDocumentDto)
			throws ApplicationException {
		try {
			Date date = TimeZoneComponent.getDateByzone(requestDocumentDto.getUnitId());
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");

			if (requestDocumentDto.getUnitId() > 0) {
				Response orgRes = iGlobalOrganisationMasterDao
						.getOrganiazationFolderPath(requestDocumentDto.getOrgId());
				OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) orgRes.getObject();
				String docPath = "";
					docPath = docUploadUtility.uploadWorkorderRequestDocuments(requestDocumentDto.getFile(),
							organizationMasterDto.getOrganizationFolder(), requestDocumentDto);

					requestDocumentDto.setDocumentPath(docPath);
					requestDocumentDto.setCreatedDate(dateTime);
					requestDocumentDto.setUpdatedDate(dateTime);
				return iNewWorkOrderCreationDao.workorderRequestDocument(requestDocumentDto);
			 }
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		} catch (Exception e) {
			logger.error("workorderRequestDocument failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getCreationRequestList(WorkOrderCreationRequestDto requestDto) throws ApplicationException {
		try {
			return iNewWorkOrderCreationDao.getCreationRequestList(requestDto);
		} catch (Exception e) {
			logger.error("getCreationRequestList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getCreationRequestDetailsbyId(WorkOrderCreationRequestDto requestDto) throws ApplicationException {
		try {
			return iNewWorkOrderCreationDao.getCreationRequestDetailsbyId(requestDto);
		} catch (Exception e) {
			logger.error("getCreationRequestDetailsbyId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response requestChangeToPending(WorkOrderCreationRequestDto requestDto) throws ApplicationException {
		try {
			return iNewWorkOrderCreationDao.requestChangeToPending(requestDto);
		} catch (Exception e) {
			logger.error("requestChangeToPending failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response requestChangeToCancel(WorkOrderCreationRequestDto requestDto) throws ApplicationException {
		try {
			return iNewWorkOrderCreationDao.requestChangeToCancel(requestDto);
		} catch (Exception e) {
			logger.error("requestChangeToCancel failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response requestChangeToReject(WorkOrderCreationRequestDto requestDto) throws ApplicationException {
		try {
			return iNewWorkOrderCreationDao.worequestChangeToReject(requestDto);
		} catch (Exception e) {
			logger.error("requestChangeToReject failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getCreationApprovalDetailsbyId(WorkOrderCreationApprovalDto apptovalDto) {
		try {
			return iNewWorkOrderCreationDao.getCreationApprovalDetailsbyId(apptovalDto);
		} catch (Exception e) {
			logger.error("getCreationApprovalDetailsbyId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response saveCreationApprovalDetails(WorkOrderCreationApprovalDto apptovalDto) throws ApplicationException {
		try {
			return iNewWorkOrderCreationDao.saveCreationApprovalDetails(apptovalDto);
		} catch (Exception e) {
			logger.error("saveCreationApprovalDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	@Transactional
	public Response requestChangeToApproved(WorkOrderCreationRequestDto requestDto) throws ApplicationException {
		try {
			return iNewWorkOrderCreationDao.requestChangeToApproved(requestDto);
		} catch (Exception e) {
			logger.error("requestChangeToApproved failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getRequestApprovalList(WorkOrderCreationApprovalDto requestDto) throws ApplicationException {
		try {
			return iNewWorkOrderCreationDao.getRequestApprovalList(requestDto);
		} catch (Exception e) {
			logger.error("getRequestApprovalList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response saveWorkOrderSettle(WorkOrderApprovalSettlementDto requestDto) throws ApplicationException {
		try {

			return iNewWorkOrderCreationDao.saveWorkOrderSettle(requestDto);
		} catch (Exception e) {
			logger.error("saveWorkOrderSettle failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getRequestSettledList(WorkOrderApprovalSettlementDto requestDto) throws ApplicationException {
		try {

			return iNewWorkOrderCreationDao.getRequestSettledList(requestDto);
		} catch (Exception e) {
			logger.error("getRequestSettledList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getRequestSettledListDetails(WorkOrderApprovalSettlementDto requestDto)
			throws ApplicationException {
		try {

			return iNewWorkOrderCreationDao.getRequestSettledListDetails(requestDto);
		} catch (Exception e) {
			logger.error("getRequestSettledListDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response saveCreationCompletionDetails(WorkOrderCreationApprovalDto apptovalDto) {
		try {
			return iNewWorkOrderCreationDao.saveCreationCompletionDetails(apptovalDto);
		} catch (Exception e) {
			logger.error("saveCreationCompletionDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getCreationApprovalDetailsbyIdEdit(WorkOrderCreationApprovalDto apptovalDto) {
		try {
			return iNewWorkOrderCreationDao.getCreationApprovalDetailsbyIdEdit(apptovalDto);
		} catch (Exception e) {
			logger.error("getCreationApprovalDetailsbyIdEdit failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getCreationRequestListCompletion(WorkOrderCreationRequestDto requestDto) {
		try {
			return iNewWorkOrderCreationDao.getCreationRequestListCompletion(requestDto);
		} catch (Exception e) {
			logger.error("getCreationRequestListCompletion failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response requestChangeToApprovedCompletion(WorkOrderCreationRequestDto requestDto) {
		try {
			return iNewWorkOrderCreationDao.requestChangeToApprovedCompletion(requestDto);
		} catch (Exception e) {
			logger.error("requestChangeToApprovedCompletion failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getWOCompletedTransaction(WorkOrderCreationApprovalDto apptovalDto) {
		try {
			return iNewWorkOrderCreationDao.getWOCompletedTransaction(apptovalDto);
		} catch (Exception e) {
			logger.error("getWOCompletedTransaction failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getCreationApprovalDetailsbyIdSettlement(WorkOrderCreationApprovalDto apptovalDto) {
		try {
			return iNewWorkOrderCreationDao.getCreationApprovalDetailsbyIdSettlement(apptovalDto);
		} catch (Exception e) {
			logger.error("getCreationApprovalDetailsbyIdSettlement failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateorder(WorkOrderCreationApprovalDto apptovalDto) {
		try {
			return iNewWorkOrderCreationDao.updateorder(apptovalDto);
		} catch (Exception e) {
			logger.error("updateorder failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


}
