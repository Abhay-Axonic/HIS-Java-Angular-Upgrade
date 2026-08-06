package com.param.global.mpesa.Dao;

/**
 * LIVE METHODS (owner-verified 2026-08-06, fix/dead-code-removal): 
 *   validateMpesaTrancastion — the payment-mode-5 receipt validation, called by
 *     CompanyInvoiceCreationServiceImpl:575, MiscellaneousExpenseDaoImpl:174, and the
 *     admission payment flow (admissionController.js sets skipMpesaValidation for the
 *     M-Pesa payment mode) — DO NOT DELETE.
 *   updateCCAvenue — the CCAvenue settlement, called by BillingMasterServiceImpl:2082
 *     (CCAvenue is a live integration governed by BR-012) — DO NOT DELETE.
 * The surrounding STK-push/callback layer (MpesaMasterController, MpesaMasterServiceImpl,
 * Mpesa.java) was removed 2026-08-06 as abandoned-in-baseline (zero config, zero
 * transactions, production-only URL) — the named queries in this DAO still hit the
 * t_mpesa_master / t_mpesa_transaction_details tables, which back the live validation.
 */
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.transaction.model.PinPadDetails;
import com.param.entity.model.mpesa.MpesaMaster;
import com.param.entity.model.mpesa.MpesaTransactionDetails;
import com.param.global.billing.dto.PinPadDetailsDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.mpesa.dto.MpesaMasterDto;
import com.param.global.mpesa.dto.MpesaTransctionDetailsDto;
import com.param.global.mpesa.dto.ResultParameter;
import com.param.global.mpesa.dto.StkCallback;
import com.param.global.mpesa.dto.TransactionResultDto;

import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unused","unchecked"})
public class MpesaMasterDaoImpl implements IMpesaMasterDao,ICommonConstants  {

	final static Logger logger = Logger.getLogger(MpesaMasterDaoImpl.class);

	@Autowired
	private SessionFactory SessionFactory;
	
	@Override
	public Response validateAmountByNumber(MpesaMasterDto mpesaMasterDto) {
		try {
		
				  MpesaMaster mpesaMaster = new MpesaMaster();
				  mpesaMaster.setId(mpesaMasterDto.getId()!=null ? mpesaMasterDto.getId() : null);
				  mpesaMaster.setMpeasaAmount(mpesaMasterDto.getMpesaAmount());
				  mpesaMaster.setMpesaSendermobile(mpesaMasterDto.getMpesaSendermobile());
				  mpesaMaster.setMpesaTxcode(mpesaMasterDto.getMpesaTxcode());
				  mpesaMaster.setCreatedBy(mpesaMasterDto.getCreatedBy());
				  mpesaMaster.setUpdatedBy(mpesaMasterDto.getUpdatedBy());
				  mpesaMaster.setUhid(mpesaMasterDto.getUhid());
				  mpesaMaster.setUnitId(mpesaMasterDto.getUnitId());
				  mpesaMaster.setOrganizationId(mpesaMasterDto.getOrganizationId());
				  mpesaMaster.setStatus("A");
				  mpesaMaster.setIsChecked(false);
				  mpesaMaster.setCreatedDate(GlobalCommonDateUtils.getDate(mpesaMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				  mpesaMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(mpesaMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				  
				  SessionFactory.getCurrentSession().saveOrUpdate(mpesaMaster);
				  return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mpesaMasterDto);


		} catch (HibernateException he)
		{
			logger.error("validateAmountByNumber failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e)
		{
			logger.error("validateAmountByNumber failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response validateMpesaTrancastion(MpesaMasterDto mpesaMasterDto) {
		
		try {
			
			MpesaMasterDto	mpesaExistingDto = (MpesaMasterDto) SessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_UNIQUE_MPESA_AMOUNT")
					.setString("mpesaTxcode", mpesaMasterDto.getMpesaTxcode())
					/*.setString("mpesaSendermobile", mpesaMasterDto.getMpesaSendermobile())*/
					.setResultTransformer(Transformers.aliasToBean(MpesaMasterDto.class)).uniqueResult();
			
			if(mpesaExistingDto == null) {
				
				MpesaTransctionDetailsDto	mpesaTransctionDetailsDto = (MpesaTransctionDetailsDto) SessionFactory.getCurrentSession()
						.getNamedQuery("CHECK_UNIQUE_MPESA_TRANSCODE_IN_TRANSACTION_DETAILS")
						.setString("checkoutReqId", mpesaMasterDto.getMpesaTxcode())
						.setInteger("unitId", mpesaMasterDto.getUnitId())
						.setInteger("orgId", mpesaMasterDto.getOrganizationId())
						/*.setString("mpesaSendermobile", mpesaMasterDto.getMpesaSendermobile())*/
						.setResultTransformer(Transformers.aliasToBean(MpesaTransctionDetailsDto.class)).uniqueResult();
				
				if(mpesaTransctionDetailsDto.getUsed()) {
					return new Response(ERROR, ERROR_CODE, "Already Consumed", null, null);
				}else {	
					Integer result =SessionFactory.getCurrentSession().getNamedQuery("UPDATE_MPESA_STATUS")
							.setString("mpesaTxcode", mpesaMasterDto.getMpesaTxcode())
							.setInteger("updatedBy", mpesaMasterDto.getUpdatedBy())
							/*.setDate("updatedDate",GlobalCommonDateUtils.getDate(mpesaMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"))*/
							.executeUpdate();
					
					Integer result1 =SessionFactory.getCurrentSession().getNamedQuery("UPDATE_MPESA_TRANSCODE_IN_TRANSACTION_DETAILS")
							.setString("checkoutReqId", mpesaMasterDto.getMpesaTxcode())
							.setInteger("unitId", mpesaMasterDto.getUnitId())
							.setInteger("orgId", mpesaMasterDto.getOrganizationId())
							/*.setDate("updatedDate",GlobalCommonDateUtils.getDate(mpesaMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"))*/
							.executeUpdate();
					return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE,null, result);
				}
			}else {
			
				if(mpesaExistingDto.getStatus().equals("D")) {
					return new Response(ERROR, ERROR_CODE, "Already Consumed", null, null);
				}else {	
					Integer result =SessionFactory.getCurrentSession().getNamedQuery("UPDATE_MPESA_TRANSCODE_IN_TRANSACTION_DETAILS")
							.setString("checkoutReqId", mpesaMasterDto.getMpesaTxcode())
							.setInteger("unitId", mpesaMasterDto.getUnitId())
							.setInteger("orgId", mpesaMasterDto.getOrganizationId())
							/*.setDate("updatedDate",GlobalCommonDateUtils.getDate(mpesaMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"))*/
							.executeUpdate();
					
					Integer result1 =SessionFactory.getCurrentSession().getNamedQuery("UPDATE_MPESA_STATUS")
							.setString("mpesaTxcode", mpesaMasterDto.getMpesaTxcode())
							.setInteger("updatedBy", mpesaMasterDto.getUpdatedBy())
							/*.setDate("updatedDate",GlobalCommonDateUtils.getDate(mpesaMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"))*/
							.executeUpdate();
					return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE,null, result);
				}
			}
		
		} catch (Exception e)
		{
			logger.error("validateMpesaTrancastion failed", e);
			throw new ServiceException(e.getMessage());
		}

	}
	@Override
	public Response savePinPanDetaild(PinPadDetailsDto pinPadDetailsDto) throws ApplicationException {
		// TODO Auto-generated method stub
		try {
			
				
			     List<String> ipMac = getMACANDIP();
			     //System.out.println("asas"+ipMac.get(0));
			 
			 //HttpServletRequest request = ServletUtils.getCurrentRequest();
			 //String BASEURL = ServletUtils.getBaseUrl(request); 
			     
			
			     PinPadDetails pinPadDetailsForSave = new PinPadDetails();
			     pinPadDetailsForSave.setTillId(pinPadDetailsDto.getTillId());
			     pinPadDetailsForSave.setCreatedBy(pinPadDetailsDto.getCreatedBy());
			     pinPadDetailsForSave.setOrgnizationId(pinPadDetailsDto.getOrganizationId());
			     pinPadDetailsForSave.setPatientId(pinPadDetailsDto.getPatientId());
			     pinPadDetailsForSave.setUnitId(pinPadDetailsDto.getUnitId());
			     pinPadDetailsForSave.setCreatedDate(GlobalCommonDateUtils.getDate(pinPadDetailsDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			     pinPadDetailsForSave.setStatus('A');
			     Integer intId =  (Integer)SessionFactory.getCurrentSession().save(pinPadDetailsForSave);
			     //System.out.println("id id"+intId);
			     
			     pinPadDetailsDto.setPinpadTransectionId(intId);
				 DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("yyyyMMdd");
				 LocalDateTime currentDate = LocalDateTime.now();
				 //System.out.println(dtfDate.format(currentDate));
				 pinPadDetailsDto.setIpAddress(ipMac.getFirst());
				// pinPadDetailsDto.setIpAddress(pinPadDetailsDto.getIp());
				// pinPadDetailsDto.setIpAddress(BASEURL); 
			     pinPadDetailsDto.setUniqueNumber(dtfDate.format(currentDate)+intId.toString());
				 return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, pinPadDetailsDto);


		} catch (HibernateException he)
		{
			logger.error("savePinPanDetaild failed", he);
			throw new ServiceException(he.getMessage());
		}
	}

	@Override
	public Response updatePinpadDetails(PinPadDetailsDto pinPadDetailsDto) throws ApplicationException {
		try {
			
			SessionFactory.getCurrentSession().createSQLQuery("UPDATE billing.t_pinpad_transection_details set status='A', created_date = '"+pinPadDetailsDto.getCreatedDate()+"', "
					+ "patient_id =:patientId , "
					+ "orgnization_id =:organizationId, "
					+ "unit_id =:unitId, "
					+ "response =:response, "
					//+ "created_date =:createdDate, "
					+ "created_by =:createdBy "
					+ "WHERE pinpad_transection_id =:pinpadTransectionId ")
			.setInteger("patientId", pinPadDetailsDto.getPatientId())
			.setInteger("organizationId", pinPadDetailsDto.getOrganizationId())
			.setInteger("unitId", pinPadDetailsDto.getUnitId())
			.setString("response", pinPadDetailsDto.getResponse())
			//.setDate("createdDate", GlobalCommonDateUtils.getDate(pinPadDetailsDto.getCreatedDate(),"dd-M-yyyy HH:mm:ss"))
			.setInteger("createdBy", pinPadDetailsDto.getCreatedBy())
			.setInteger("pinpadTransectionId", pinPadDetailsDto.getPinpadTransectionId())
			.executeUpdate();
			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
			logger.error("updatePinpadDetails failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updatePinpadDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	public static List<String> getMACANDIP(){

        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            List<String> list = new LinkedList<String>();
            list.add(ip.getHostAddress());
            list.add(sb.toString());
            return list;
        } catch (Exception e) {
            logger.error("getMACANDIP failed", e);
            throw new ServiceException(e.getMessage());
        }
       }

	@Override
	public Response saveTransactionDate(TransactionResultDto mpesaMasterDto) throws ApplicationException {
		try {
			
			MpesaMasterDto	mpesaExistingDto = (MpesaMasterDto) SessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_UNIQUE_MPESA_AMOUNT")
					.setString("mpesaTxcode", mpesaMasterDto.getResult().getTransactionID())
					.setResultTransformer(Transformers.aliasToBean(MpesaMasterDto.class)).uniqueResult();
			  if(mpesaExistingDto==null) {
				  
				  MpesaMaster mpesaMaster = new MpesaMaster();
				  mpesaMaster.setMpesaTxcode(mpesaMasterDto.getResult().getTransactionID());
				  
				  for (ResultParameter parDto : mpesaMasterDto.getResult().getResultParameters().getResultParameter()) {
					  String value= "Amount";
					if(parDto.getKey().equals(value)) {
						Double inum = Double.parseDouble(parDto.getValue());
						 mpesaMaster.setMpeasaAmount(inum);
					}
				  }
				 
				  mpesaMaster.setStatus("A");
				  mpesaMaster.setIsChecked(false);
				  Session session = SessionFactory.openSession();
				  Transaction tr = session.beginTransaction();
				  session.save(mpesaMaster);
				  tr.commit();
				  session.close();
				  
				  return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mpesaMasterDto);
			  }
			  else
			  {
				  if(mpesaExistingDto.getStatus().equals("D")) {
					  return new Response(ERROR, ERROR_CODE, "Already Consumed", null, null);
				  }
				  return new Response(SUCCESS, SUCCESS, SUCCESS_MESSAGE, null, mpesaExistingDto);
			  }

		} catch (HibernateException he)
		{
			logger.error("saveTransactionDate failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e)
		{
			logger.error("saveTransactionDate failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getTransactionData(String mpesaTxcode) throws ApplicationException {
		try {
			
			MpesaMasterDto	mpesaExistingDto = (MpesaMasterDto) SessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_UNIQUE_MPESA_AMOUNT")
					.setString("mpesaTxcode", mpesaTxcode)
					/*.setString("mpesaSendermobile", mpesaMasterDto.getMpesaSendermobile())*/
					.setResultTransformer(Transformers.aliasToBean(MpesaMasterDto.class)).uniqueResult();
			
			
				  return new Response(SUCCESS, SUCCESS, SUCCESS_MESSAGE, null, mpesaExistingDto);

		} catch (HibernateException he)
		{
			logger.error("getTransactionData failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e)
		{
			logger.error("getTransactionData failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateTransactionDate(StkCallback stkDto) throws ApplicationException {
		try {
			
			SessionFactory.getCurrentSession().createSQLQuery("UPDATE billing.t_mpesa_transaction_details set transaction_id= '"+stkDto.getTransactionId()
					+ "' WHERE checkout_request_id =:CheckoutRequestID ")
			.setString("CheckoutRequestID", stkDto.getCheckoutRequestID())
			.executeUpdate();
			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
			logger.error("updateTransactionDate failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateTransactionDate failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveValidationData(StkCallback stkDto) throws ApplicationException {
		try {
			
			MpesaMaster mpesaTransactionDetails = new MpesaMaster();
			mpesaTransactionDetails.setMpeasaAmount(stkDto.getAmount());
			mpesaTransactionDetails.setStatus("A");
			mpesaTransactionDetails.setMpesaTxcode(stkDto.getTransactionId());
			mpesaTransactionDetails.setIsChecked(false);
			SessionFactory.getCurrentSession().saveOrUpdate(mpesaTransactionDetails);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, mpesaTransactionDetails);

	}catch(Exception e) {
			logger.error("saveValidationData failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateRazorPay(String transactionNo) throws ApplicationException {
		try {

			SessionFactory.getCurrentSession().createSQLQuery("update public.m_razorpay_order_log_details set is_used = true where unique_id =:transactionNo ")
			.setString("transactionNo", transactionNo)
			.executeUpdate();
			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
			logger.error("updateRazorPay failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateRazorPay failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateCCAvenue(String transactionNo, Integer billId) throws ApplicationException {
		try {

			SessionFactory.getCurrentSession().createSQLQuery("update billing.t_cc_avenue_log  set is_used = true , bill_id = "+billId+" where order_no =:transactionNo ")
			.setString("transactionNo", transactionNo)
			.executeUpdate();
			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
			logger.error("updateCCAvenue failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateCCAvenue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
