package com.param.bloodBank.service;

import javax.transaction.Transactional;

import com.param.bloodBank.dto.BloodBagCollectionDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IBloodBagCollectionService {

	public Response saveBloodBagCollection(BloodBagCollectionDto bloodBagCollectionDto) throws ApplicationException;

	public Response searchBloodBag(BloodBagCollectionDto bloodBagCollectionDto) throws ApplicationException;

	public Response getDonorAndScreeningAndBagCollectionDetail(BloodBagCollectionDto bloodBagCollectionDto)throws ApplicationException;

	public Response getBagCollectionDetailById(BloodBagCollectionDto bloodBagCollectionDto)throws ApplicationException;

	public Response getExaminationListByCollectionId(BloodBagCollectionDto bloodBagCollectionDto) throws ApplicationException;


}
