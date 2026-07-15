package com.param.bloodBank.dao;

import com.param.bloodBank.dto.BloodBagCollectionDto;
import com.param.global.common.Response;
import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface IBloodBagCollectionDao {

	public Response saveBloodBagCollection(BloodBagCollectionDto bloodBagCollectionDto) throws ApplicationException;

	public Response searchBloodBag(BloodBagCollectionDto bloodBagCollectionDto) throws ApplicationException;

	public Long getNextValBagCollectionId()  throws ApplicationException;

	public void updateForwordedTo(int bloodBagCollectionId)throws ApplicationException;

	public Response getSavedBloodBagDetailList(BloodBagCollectionDto bloodBagCollectionDto)throws ApplicationException;

	public Response getBagCollectionDetailById(BloodBagCollectionDto bloodBagCollectionDto)throws ApplicationException;

	public Response getExaminationListByCollectionId(BloodBagCollectionDto bloodBagCollectionDto)throws ApplicationException;
	
	public Response updateScreeningStatus(BloodBagCollectionDto bloodBagCollectionDto)throws ApplicationException;

}
