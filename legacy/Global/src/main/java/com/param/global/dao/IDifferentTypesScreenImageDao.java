package com.param.global.dao;

import com.param.entity.model.master.DifferentTypesScreenImageDTO;
import org.springframework.web.multipart.MultipartFile;

import com.param.global.common.Response;

import scala.collection.generic.BitOperations.Int;

@SuppressWarnings({ "rawtypes" })
public interface IDifferentTypesScreenImageDao {

	Response saveScreenImage(DifferentTypesScreenImageDTO differentTypesScreenImageDTO, MultipartFile inputFile) throws Exception;

	public Response saveScreenApplicationName(DifferentTypesScreenImageDTO differentTypesScreenImageDTO) throws Exception;

	Response getScreenImageById(Integer organisationId, Integer imageTypeId) throws Exception;

}
