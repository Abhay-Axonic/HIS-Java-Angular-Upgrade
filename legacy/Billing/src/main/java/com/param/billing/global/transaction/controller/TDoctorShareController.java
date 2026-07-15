package com.param.billing.global.transaction.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.common.ICommonConstants;
import com.param.billing.common.Response;
import com.param.billing.global.transaction.dto.BillDetailsByBillIdDto;
import com.param.billing.global.transaction.dto.TDoctorShareLoggerReqDto;
import com.param.billing.global.transaction.dto.TDoctorShareProcessReqDto;
import com.param.billing.global.transaction.dto.TDoctorShareReqDto;
import com.param.billing.global.transaction.dto.TDoctorShareRespDto;
import com.param.billing.global.transaction.dto.TDoctorShareSplitLogsReqDto;
import com.param.billing.global.transaction.service.ITDoctorShareService;

@RestController
@RequestMapping(value="api/billing")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TDoctorShareController implements ICommonConstants{
	
	@Autowired
	private ITDoctorShareService iTDoctorShareService;
	
	/**
	 * TO GET DOCTOR SHARE LIST WITH DATE RANGE BY DOCTOR
	 * @author Suraj
	 * @param tDoctorShareDto
	 * @return listTDoctorShareRespDto
	 */
	@RequestMapping(value = "/getDoctorShareList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorShareList(@RequestBody TDoctorShareReqDto tDoctorShareDto) {
		Response<TDoctorShareRespDto, Integer> response = null;
		try {
			response =iTDoctorShareService.getDoctorShareList(tDoctorShareDto);
			return response;
		} catch (Exception e) {

			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@RequestMapping(value = "/getDoctorShareListCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorShareListCount(@RequestBody TDoctorShareReqDto tDoctorShareDto) {
		Response<TDoctorShareRespDto, Integer> response = null;
		try {
			response =iTDoctorShareService.getDoctorShareListCount(tDoctorShareDto);
			return response;
		} catch (Exception e) {

			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	/**
	 * TO POST DOCTOR SHARE INTO TDOCTORSHARE
	 * @author Suraj
	 * Save Doctor Share List
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/saveDoctorShareList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveDoctorShareList(@RequestBody TDoctorShareProcessReqDto reqDto) {
		Response<TDoctorShareProcessReqDto, Integer> response = null;
		try {
			//JsonCheck.mapJsonToObject(reqDto, TDoctorShareProcessReqDto.class);
			response =iTDoctorShareService.saveDoctorShareList(reqDto);
			return response;
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/saveDoctorRefeeralList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveDoctorReferralList(@RequestBody TDoctorShareProcessReqDto reqDto) {
		Response<TDoctorShareProcessReqDto, Integer> response = null;
		try {
			//JsonCheck.mapJsonToObject(reqDto, TDoctorShareProcessReqDto.class);
			response =iTDoctorShareService.saveDoctorReferralList(reqDto);
			return response;
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	/**
	 * @author Suraj 
	 * Get Doctor Share Payment Voucher List
	 * @param tDoctorShareDto
	 * @return listTDoctorShareRespDto
	 */
	@RequestMapping(value = "/getDoctorSharePaymentVoucherList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorSharePaymentVoucherList(@RequestBody TDoctorShareReqDto tDoctorShareDto) {
		Response<TDoctorShareRespDto, Integer> response = null;
		try {
			response =iTDoctorShareService.getDoctorSharePaymentVoucherList(tDoctorShareDto);
			return response;
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getDoctorReferralPaymentVoucherList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorReferralPaymentVoucherList(@RequestBody TDoctorShareReqDto tDoctorShareDto) {
		Response<TDoctorShareRespDto, Integer> response = null;
		try {
			response =iTDoctorShareService.getDoctorRefferalPaymentVoucherList(tDoctorShareDto);
			return response;
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	/**
	 * @author Suraj
	 * @param tDoctorShareDto
	 * @return listTDoctorShareRespDto
	 */
	@RequestMapping(value = "/getDoctorSharePaymentVoucherDtlsByDoctorShareId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorSharePaymentVoucherDtlsByDoctorShareId(@RequestBody TDoctorShareReqDto tDoctorShareDto) {
		Response<TDoctorShareRespDto, Integer> response = null;
		try {
			response =iTDoctorShareService.getDoctorSharePaymentVoucherDtlsByDoctorShareId(tDoctorShareDto);
			return response;
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getDoctorReferralPaymentVoucherDtlsByReferralId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorReferralPaymentVoucherDtlsByReferralId(@RequestBody TDoctorShareReqDto tDoctorShareDto) {
		Response<TDoctorShareRespDto, Integer> response = null;
		try {
			response =iTDoctorShareService.getDoctorReferralPaymentVoucherDtlsByReferralId(tDoctorShareDto);
			return response;
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	/**
	 * @author Suraj
	 * Save Doctor Share Payment Voucher List
	 * @param reqDto
	 * @return
	 */
	
	@RequestMapping(value = "/saveDoctorSharePaymentVoucherList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveDoctorSharePaymentVoucherList(@RequestBody TDoctorShareLoggerReqDto reqDto) {
		Response<TDoctorShareLoggerReqDto, Integer> response = null;
		try {
			//JsonCheck.mapJsonToObject(reqDto,TDoctorShareLoggerReqDto.class);
			response =iTDoctorShareService.saveDoctorSharePaymentVoucherList(reqDto);
			return response;
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/saveDoctorReferralPaymentVoucherList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveDoctorReferralPaymentVoucherList(@RequestBody TDoctorShareLoggerReqDto reqDto) {
		Response<TDoctorShareLoggerReqDto, Integer> response = null;
		try {
			//JsonCheck.mapJsonToObject(reqDto,TDoctorShareLoggerReqDto.class);
			response =iTDoctorShareService.saveDoctorReferralPaymentVoucherList(reqDto);
			return response;
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	
	
	/**
	 * @author Suraj
	 * Save Doctor Share Split 
	 * @param reqDto
	 * @return
	 */
	
	@RequestMapping(value = "/saveDoctorShareSplit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveDoctorShareSplit(@RequestBody TDoctorShareSplitLogsReqDto masterDto) {
		Response<TDoctorShareSplitLogsReqDto, Integer> response = null;
		try {
			response =iTDoctorShareService.saveDoctorShareSplit(masterDto);
			return response;
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/doctorShare/split/details/{docShareId}", method = RequestMethod.GET)
	public Response getDocShareSplitDetails(@PathVariable("docShareId") Integer docShareId) {
		try {
			return iTDoctorShareService.getDocShareSplitDetails(docShareId);
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/process/doctorShare/{unitId}", method = RequestMethod.GET)
	public synchronized Response getProcessDocShare(@PathVariable("unitId") Integer unitId) {
		try {
			return iTDoctorShareService.getProcessDocShare(unitId);
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/process/doctorShare/doctor/change", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response changeDoctor(@RequestBody TDoctorShareRespDto masterDto) {
		try {
			return iTDoctorShareService.changeDoctor(masterDto);
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@RequestMapping(value = "/process/doctorShare/doctor/inactive", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateStatus(@RequestBody TDoctorShareRespDto masterDto) {
		try {
			return iTDoctorShareService.updateStatus(masterDto);
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getPendingServicesForDoctorShareByBillId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getPendingServicesForDoctorShareByBillId(@RequestBody TDoctorShareReqDto tDoctorShareDto) {
		try {
			return iTDoctorShareService.getPendingServicesForDoctorShareByBillId(tDoctorShareDto);
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/processDocShareForAlreadyBilledServices", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response processDocShareForAlreadyBilledServices(@RequestBody List<BillDetailsByBillIdDto> billDetailsList) {
		try {
			Response res = iTDoctorShareService.processDocShareForAlreadyBilledServices(billDetailsList);
				iTDoctorShareService.getProcessDocShare((Integer)res.getObject());
			return res;
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getDoctorReferralList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorReferralList(@RequestBody TDoctorShareReqDto tDoctorShareDto) {
		Response<TDoctorShareRespDto, Integer> response = null;
		try {
			response =iTDoctorShareService.getDoctorReferralList(tDoctorShareDto);
			return response;
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/process/doctorRefeeral/doctor/change", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response changeDoctorRefeeral(@RequestBody TDoctorShareRespDto masterDto) {
		try {
			return iTDoctorShareService.changeDoctorRefeeral(masterDto);
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getDoctorShareProcessedVoucherList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorShareProcessedVoucherList(@RequestBody TDoctorShareReqDto tDoctorShareDto) {
		Response response = null;
		try {
			response =iTDoctorShareService.getDoctorShareProcessedVoucherList(tDoctorShareDto);
			return response;
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
}
