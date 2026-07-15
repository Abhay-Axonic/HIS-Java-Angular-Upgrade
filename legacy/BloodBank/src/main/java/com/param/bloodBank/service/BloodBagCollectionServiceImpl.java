package com.param.bloodBank.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.bloodBank.dao.IBloodBagCollectionDao;
import com.param.bloodBank.dao.IDonorRegistrationDao;
import com.param.bloodBank.dao.IDonorScreeningDetailDao;
import com.param.bloodBank.dto.BloodBagCollectionDto;
import com.param.bloodBank.dto.DonorScreeningDetailDto;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IGlobalUnitMasterDao;

import in.param.exception.ApplicationException;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes","unchecked","unused"})
public class BloodBagCollectionServiceImpl implements IBloodBagCollectionService,ICommonConstants,IBillingCommonConstants{

	final static Logger logger = Logger.getLogger(BloodBagCollectionServiceImpl.class);


	@Autowired
	private IBloodBagCollectionDao iBloodBagCollectionDao; 
	
	@Autowired
	private IDonorScreeningDetailDao donorScreeningDetailDao;
	
	@Autowired
	private IDonorRegistrationDao iDonorRegistrationDao;

	@Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;
	
	@Override
	public Response saveBloodBagCollection(BloodBagCollectionDto bloodBagCollectionDto) throws ApplicationException {
		try {
			//Long incrementedId = iBloodBagCollectionDao.getNextValBagCollectionId();
			String bagNumber = iGlobalUnitMasterDao.getGeneratedNo(0, bloodBagCollectionDto.getUnitId(), BLOOD_BAG_NUMBER);
			bloodBagCollectionDto.setBloodBagNo(bagNumber);
			Response res =  iBloodBagCollectionDao.saveBloodBagCollection(bloodBagCollectionDto);
			
			if(res.getStatus().equals("success")) {
					iBloodBagCollectionDao.updateScreeningStatus(bloodBagCollectionDto);
			}
			
			 return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, res.getObject());
			 
			 
		} catch (Exception e) {
			logger.error("saveBloodBagCollection failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response searchBloodBag(BloodBagCollectionDto bloodBagCollectionDto) throws ApplicationException {
		try {
			return iBloodBagCollectionDao.searchBloodBag(bloodBagCollectionDto);
		} catch (Exception e) {
			logger.error("searchBloodBag failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	/*private String generateBloodBagNo(Long incrementedId) throws ApplicationException {
		try {
			
			//System.out.println(incrementedId);
			String bagNoPrefix="BN-";
			StringBuilder bagNo=new StringBuilder(bagNoPrefix);
			
				int length = Long.valueOf(incrementedId).toString().length();
				bagNo = length == 1 ? (bagNo.append("000000").append(incrementedId))
						: length == 2 ? (bagNo.append("00000").append(incrementedId))
								: length == 3 ? (bagNo.append("0000").append(incrementedId))
										: length == 4 ? (bagNo.append("000").append(incrementedId))
												: length == 5 ? (bagNo.append("00").append(incrementedId))
														: length == 6 ? (bagNo.append("0").append(incrementedId))
																: length == 7 ? (bagNo.append(incrementedId)) : bagNo.append("");
				return bagNo.toString();
			} catch (Exception e) {
				
			}
			
		return "error";
	}
*/
	@Override
	@Transactional
	public Response getDonorAndScreeningAndBagCollectionDetail(BloodBagCollectionDto bloodBagCollectionDto)
			throws ApplicationException {
		try {
			Response res =  donorScreeningDetailDao.getOnlyAcceptedDonorscreening(bloodBagCollectionDto);
			List<DonorScreeningDetailDto> finalList = new ArrayList();
			Response res1 = donorScreeningDetailDao.getRegistreredAutologousDonor(bloodBagCollectionDto);
			
			Response res2 = iBloodBagCollectionDao.getSavedBloodBagDetailList(bloodBagCollectionDto);
			if(res.getListObject()!=null && res.getListObject().size()>0)
			{
			 finalList.addAll(res.getListObject());
			}
			if(res1.getListObject()!=null && res1.getListObject().size()>0)
			{
				 finalList.addAll(res1.getListObject());
			}
			if(res2.getListObject()!=null && res2.getListObject().size()>0)
			{
				 finalList.addAll(res2.getListObject());
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, finalList, null);
		} catch (Exception e) {
			logger.error("getDonorAndScreeningAndBagCollectionDetail failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBagCollectionDetailById(BloodBagCollectionDto bloodBagCollectionDto)
			throws ApplicationException {
		try {
			return iBloodBagCollectionDao.getBagCollectionDetailById(bloodBagCollectionDto);
		} catch (Exception e) {
			logger.error("getBagCollectionDetailById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response getExaminationListByCollectionId(BloodBagCollectionDto bloodBagCollectionDto)
			throws ApplicationException {
		try {
			return iBloodBagCollectionDao.getExaminationListByCollectionId(bloodBagCollectionDto);
		} catch (Exception e) {
			logger.error("getExaminationListByCollectionId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
}
