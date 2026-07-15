package com.param.billing.global.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.global.dto.DiscountRightsMasterDto;
import com.param.billing.global.service.IDiscountRightsMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

@RestController
@SuppressWarnings({"rawtypes"})
@RequestMapping("/billing")
public class DiscountRightsMasterController implements ICommonConstants {
	@Autowired
	private IDiscountRightsMasterService iDiscountRightsMasterService;
	
	@RequestMapping(value = "/saveDiscountRightsMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveDiscountRightsMaster(@RequestBody DiscountRightsMasterDto discountRightsMasterDto){
		try{
			return iDiscountRightsMasterService.saveDiscountRightsMaster(discountRightsMasterDto);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateDiscountRightsMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateDiscountRightsMaster(@RequestBody DiscountRightsMasterDto discountRightsMasterDto){
		try{
			return iDiscountRightsMasterService.updateDiscountRightsMaster(discountRightsMasterDto);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateDiscountRightsMasterStatus" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateDiscountRightsMasterStatus(@RequestBody DiscountRightsMasterDto discountRightsMasterDto){
		try{
			return iDiscountRightsMasterService.updateDiscountRightsMasterStatus(discountRightsMasterDto);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getDiscountRightsMasterList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDiscountRightsMasterList(@RequestBody DiscountRightsMasterDto discountRightsMasterDto){
		try{
			return iDiscountRightsMasterService.getDiscountRightsMasterList(discountRightsMasterDto);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getDiscountRightsMasterById/{discountRightId}/{organisationId}/{unitId}" , method = RequestMethod.GET)
	public Response getDiscountRightsMasterById(@PathVariable ("discountRightId") Integer discountRightId,
												@PathVariable ("organisationId") Integer organisationId,
												@PathVariable ("unitId") Integer unitId){
		try{
			return iDiscountRightsMasterService.getDiscountRightsMasterById(discountRightId, organisationId, unitId);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getActiveDiscountRightsMaster" , method = RequestMethod.GET)
	public Response getActiveDiscountRightsMaster(){
		try{
			return iDiscountRightsMasterService.getActiveDiscountRightsMaster();
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getDiscountRightsMasterCount" , method = RequestMethod.GET)
	public Response getDiscountRightsMasterCount(){
		try{
			return iDiscountRightsMasterService.getDiscountRightsMasterCount();
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getUsersList/{orgId}/{unitId}" , method = RequestMethod.GET)
	public Response getUsersList(@PathVariable ("orgId") Integer orgId,@PathVariable ("unitId") Integer unitId){
		try{
			return iDiscountRightsMasterService.getUsersList(orgId,unitId);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getUsersDiscRights/orgId/{orgId}/unitId/{unitId}/userId/{userId}" , method = RequestMethod.GET)
	public Response getUsersDiscRights(@PathVariable ("userId") Integer userId,
												@PathVariable ("orgId") Integer orgId,
												@PathVariable ("unitId") Integer unitId){
		try{
			return iDiscountRightsMasterService.getUsersDiscRights(orgId, unitId, userId);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getUsersListHavingDiscRights/orgId/{orgId}/unitId/{unitId}/amt/{amount}/per/{percentage}" , method = RequestMethod.GET)
	public Response getUsersListHavingDiscRights(@PathVariable ("amount") BigDecimal amount,
												@PathVariable ("orgId") Integer orgId,
												@PathVariable ("unitId") Integer unitId,
												@PathVariable ("percentage") BigDecimal percentage){
		try{
			
			return iDiscountRightsMasterService.getUsersListHavingDiscRights(orgId, unitId, amount,percentage);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getUsersListHavingCreditRights/orgId/{orgId}/unitId/{unitId}/amt/{amount}/per/{percentage}" , method = RequestMethod.GET)
	public Response getUsersListHavingCreditRights(@PathVariable ("amount") BigDecimal amount,
												@PathVariable ("orgId") Integer orgId,
												@PathVariable ("unitId") Integer unitId,
												@PathVariable ("percentage") BigDecimal percentage){
		try{
			
			return iDiscountRightsMasterService.getUsersListHavingCreditRights(orgId, unitId, amount,percentage);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, null, null, null, null);
	}
	
	
	@RequestMapping(value = "/getUsersListHavingFinancialClearanceRights/orgId/{orgId}/unitId/{unitId}/amt/{amount}" , method = RequestMethod.GET)
	public Response getUsersListHavingFinancialClearanceRights(@PathVariable ("amount") BigDecimal amount,@PathVariable ("orgId") Integer orgId,
												@PathVariable ("unitId") Integer unitId){
		try{
			return iDiscountRightsMasterService.getUsersListHavingFinancialClearanceRights(orgId, unitId, amount);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/users/creditNote/rights/orgId/{orgId}/unitId/{unitId}/amt/{amount}" , method = RequestMethod.GET)
	public Response getUsersCreditNoteRights(@PathVariable ("orgId") Integer orgId,
												@PathVariable ("unitId") Integer unitId,@PathVariable ("amount") Double amount){
		try{
			return iDiscountRightsMasterService.getUsersCreditNoteRights(orgId, unitId,amount);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, null, null, null, null);
	}
	@RequestMapping(value = "/creditNote/rights/orgId/{orgId}/unitId/{unitId}/user/{userId}" , method = RequestMethod.GET)
	public Response isUserHaveCreditNoteRight(@PathVariable ("orgId") Integer orgId,
												@PathVariable ("unitId") Integer unitId,@PathVariable ("userId") Integer userId)
	{
		try{
			return iDiscountRightsMasterService.isUserHaveCreditNoteRight(orgId, unitId,userId);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/users/refund/rights/orgId/{orgId}/unitId/{unitId}" , method = RequestMethod.GET)
	public Response getUsersHavingERights(@PathVariable ("orgId") Integer orgId,
												@PathVariable ("unitId") Integer unitId){
		try{
			return iDiscountRightsMasterService.getUsersHavingRefundRights(orgId, unitId);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, null, null, null, null);
	}
	@RequestMapping(value = "/refund/rights/orgId/{orgId}/unitId/{unitId}/user/{userId}" , method = RequestMethod.GET)
	public Response isUserHavingRefundRight(@PathVariable ("orgId") Integer orgId,
												@PathVariable ("unitId") Integer unitId,@PathVariable ("userId") Integer userId)
	{
		try{
			return iDiscountRightsMasterService.isUserHavingRefundRight(orgId, unitId,userId);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/deposit/refund/orgId/{orgId}/unitId/{unitId}/user/{userId}/amount/{amount}" , method = RequestMethod.GET)
	public Response isUserHavingDepositRefundRight(@PathVariable ("orgId") Integer orgId,
											@PathVariable ("unitId") Integer unitId,@PathVariable ("userId") Integer userId,
												   @PathVariable ("amount") Double amount)
	{
		try{
			return iDiscountRightsMasterService.isUserHavingDepositRefundRight(orgId, unitId,userId,amount);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/users/refund/rights/orgId/{orgId}/unitId/{unitId}/amount/{amount}" , method = RequestMethod.GET)
	public Response getUserHavingRefundRigtsOfGivenAmount(@PathVariable ("orgId") Integer orgId,
										  @PathVariable ("unitId") Integer unitId,@PathVariable ("amount") Double amount){
		try{
			return iDiscountRightsMasterService.getUserHavingRefundRigtsOfGivenAmount(orgId, unitId,amount);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getUsersListByRoleId/{orgId}/{unitId}/{roleId}" , method = RequestMethod.GET)
	public Response getUsersList(@PathVariable ("orgId") Integer orgId,@PathVariable ("unitId") Integer unitId,@PathVariable ("roleId") Integer roleId){
		try{
			return iDiscountRightsMasterService.getUsersListByOrgUnit(orgId,unitId,roleId);
		}catch(Exception e){
			
			throw e;
		}
	//	return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getUsersListHavingDiscRightsForBadDebit/orgId/{orgId}/unitId/{unitId}/amt/{amount}" , method = RequestMethod.GET)
	public Response getUsersListHavingDiscRightsForBadDebit(@PathVariable ("amount") BigDecimal amount,
												@PathVariable ("orgId") Integer orgId,
												@PathVariable ("unitId") Integer unitId){
		try{
			
			return iDiscountRightsMasterService.getUsersListHavingDiscRightsForBadDebit(orgId, unitId, amount);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getUsersListHavingDiscRightsForCredit/orgId/{orgId}/unitId/{unitId}/amt/{amount}" , method = RequestMethod.GET)
	public Response getUsersListHavingDiscRightsForCredit(@PathVariable ("amount") BigDecimal amount,
												@PathVariable ("orgId") Integer orgId,
												@PathVariable ("unitId") Integer unitId){
		try{
			
			return iDiscountRightsMasterService.getUsersListHavingDiscRightsForCredit(orgId, unitId, amount);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, null, null, null, null);
	}
	@RequestMapping(value = "/users/expense/rights/{orgId}/{unitId}/{amount}" , method = RequestMethod.GET)
	public Response getUsersHavingExpenseRights(@PathVariable ("orgId") Integer orgId,
												@PathVariable ("unitId") Integer unitId,
												@PathVariable ("amount") BigDecimal amount){
		try{
			return iDiscountRightsMasterService.getUsersHavingExpenseRights(orgId, unitId,amount);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(ERROR, null, null, null, null);
	}
}
