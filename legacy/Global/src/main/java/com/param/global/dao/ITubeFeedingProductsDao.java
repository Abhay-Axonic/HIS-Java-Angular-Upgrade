package com.param.global.dao;


import com.param.global.common.Response;
import com.param.global.dto.TubeFeedingProductsDto;

import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes"})
public interface ITubeFeedingProductsDao {
public Response saveTubeFeedingProducts( TubeFeedingProductsDto tubeFeedingProductsDto) throws ApplicationException;
	
	public Response updateTubeFeedingProducts(TubeFeedingProductsDto tubeFeedingProductsDto) throws ApplicationException;
	
	public Response updateTubeFeedingProductsStatus( TubeFeedingProductsDto tubeFeedingProductsDto) throws ApplicationException;
	
	public Response getTubeFeedingProductsList(TubeFeedingProductsDto tubeFeedingProductsDto) throws ApplicationException;
	
	public Response getTubeFeedingProductsById(Integer tubeFeedingProductsId, Integer organisationId,  Integer unitId) throws ApplicationException;
	
	public Response getActiveTubeFeedingProducts(TubeFeedingProductsDto tubeFeedingProductsDto) throws ApplicationException;
	
	public Response getTubeFeedingProductsCount(TubeFeedingProductsDto tubeFeedingProductsDto) throws ApplicationException;
	
	public Response checkTubeFeedingProductsAvailable(TubeFeedingProductsDto tubeFeedingProductsDto) throws ApplicationException;
	
	public Response getTubeFeedingProductsByNameNotId(TubeFeedingProductsDto tubeFeedingProductsDto) throws ApplicationException;

}
