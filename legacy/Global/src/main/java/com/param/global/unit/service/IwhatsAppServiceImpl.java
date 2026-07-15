package com.param.global.unit.service;

import java.io.IOException;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.global.unit.dto.whatsAppRequestDto;

import in.param.exception.ApplicationException;

@Transactional   
@SuppressWarnings("rawtypes")
public interface IwhatsAppServiceImpl {

	public Response sendWhatsAppMessage(whatsAppRequestDto wDhatsappRequestDto) throws ApplicationException;

	public Response sendWhatsAppMessageTwilio(whatsAppRequestDto whatsAppRequestDto)throws ApplicationException;

	public Response sendWhatsAppMessageVonage(whatsAppRequestDto wDhatsappRequestDto) throws ApplicationException;

	public Response sendWhatsAppMessageHelo(whatsAppRequestDto wDhatsappRequestDto) throws ApplicationException;

	public Response authontificationKeyGenerationForHeloAPI(whatsAppRequestDto wDhatsappRequestDto) throws ApplicationException;

	public Response sendWhatsAppMessageHeloBulkMessages(whatsAppRequestDto wDhatsappRequestDto) throws ApplicationException;

	public Response sendWhatsAppMessagePinbot(whatsAppRequestDto wDhatsappRequestDto) throws ApplicationException;

	public Response sendOTPOverMail(String trim, String otpResult) throws ApplicationException,IOException;

	public Response sendOrgCreationResponse(OrganizationMasterDto organizationMasterForWebDto) throws ApplicationException,IOException;

	public Response sendOrgCreationFailureResponse(OrganizationMasterDto organizationMasterForWebDto) throws ApplicationException, IOException;

}
