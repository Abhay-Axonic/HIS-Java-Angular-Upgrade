package com.param.global.dao;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.*;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.PatientPOCMasterDTO;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.model.PatientPOCMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PatientPOCMasterDaoImpl extends GenericDao<PatientPOCMaster, Integer>
		implements IPatientPOCMasterDao, ICommonConstants, IBillingCommonConstants {

	private static final String TOKEN_URL = "https://apigwrfhppd.ril.com/oauth/1.0.0/oauth2/token";
	private static final String SMS_API_URL = "https://apigwrfhppd.ril.com/api/sms/1.0.0/api/sms";
	private final OkHttpClient client = new OkHttpClient();

	@Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;

	@Autowired
	private IPatientRegistrationDao iPatientRegistrationDao;

	@Autowired
	SessionFactory sessionFactory;

	public PatientPOCMasterDaoImpl() {
		super(PatientPOCMaster.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response savePOCMaster(PatientPOCMasterDTO patientPOCMasterDTO) throws Exception {

		try {

			PatientPOCMaster patientPOCMaster = new PatientPOCMaster();
			patientPOCMaster.setFirstName(patientPOCMasterDTO.getFirstName());
			patientPOCMaster.setMiddleName(patientPOCMasterDTO.getMiddleName());
			patientPOCMaster.setLastName(patientPOCMasterDTO.getLastName());
			patientPOCMaster.setEmailId(patientPOCMasterDTO.getEmailId());
			patientPOCMaster.setPrefixId(patientPOCMasterDTO.getPrefixId());
			patientPOCMaster.setMobileNo(patientPOCMasterDTO.getMobileNo());
			patientPOCMaster.setReasonId(patientPOCMasterDTO.getReasonId());
			patientPOCMaster.setRemark(patientPOCMasterDTO.getRemark());
			patientPOCMaster.setStatus('A');
			patientPOCMaster.setGenderId(patientPOCMasterDTO.getGenderId());
			patientPOCMaster.setCheckinDateTime(
					GlobalCommonDateUtils.getDate(patientPOCMasterDTO.getCheckinDateTime(), "dd-M-yyyy HH:mm:ss"));
			patientPOCMaster.setCreatedDate(
					GlobalCommonDateUtils.getDate(patientPOCMasterDTO.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			patientPOCMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(patientPOCMasterDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			patientPOCMaster.setCheckinStatusId(null);
			String uhidNo = null;
			String uhidNo1 = null;
			Long incrementedId = iPatientRegistrationDao.getNextValPatientIdInPoc();
			uhidNo1 = iGlobalUnitMasterDao.getGeneratedGlobalNo(0, patientPOCMasterDTO.getOrganizationId(),
					patientPOCMasterDTO.getUnitId(), generateUhidConfig(1));

			uhidNo = uhidNo1 + incrementedId;
			if (uhidNo != null && !uhidNo.isEmpty())
				patientPOCMaster.setPatientPOCUniqueNo(uhidNo);

			save(patientPOCMaster);

			// send sms code
			String query = "select uabc.sender_id as \"senderId\" from public.t_unit_application_billing_configuration uabc where uabc.organization_id =:organizationId and uabc.unit_id =:unitId ";

			List<UnitApplicationConfigurationDto> itemsList = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("organizationId", patientPOCMasterDTO.getOrganizationId())
					.setParameter("unitId", patientPOCMasterDTO.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(UnitApplicationConfigurationDto.class)).list();
			//generate token
			Map<String, String> POCToken = generatePOCToken();

			//send SMS
			generatePOCSMS(patientPOCMasterDTO, uhidNo, POCToken);

			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, patientPOCMaster);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}

	private int generateUhidConfig(int registrationTypeId) throws ApplicationException {
		Integer screenId = 0;
		try {

			if (registrationTypeId != 2)
				screenId = NORMAL_PATIENT_UHID;

			if (registrationTypeId == 5)
				screenId = PRE_REGESTRATION_UHID;

			if (registrationTypeId == 2)
				screenId = OTC_PATIENT_UHID;

		} catch (Exception e) {
			throw e;
		}

		return screenId;
	}

	private String generateUhid(int registrationTypeId) throws ApplicationException {
		try {
			Long incrementedId = iPatientRegistrationDao.getNextValPatientId();
			// System.out.println(incrementedId);
			String uhidPrefix = null;
			if (registrationTypeId != 2)
				uhidPrefix = "PC-18-";

			if (registrationTypeId == 5)
				uhidPrefix = "PR-18-";

			if (registrationTypeId == 2)
				uhidPrefix = "O-18-";

			StringBuilder uhid = new StringBuilder(uhidPrefix);

			int length = Long.valueOf(incrementedId).toString().length();
			uhid = length == 1 ? (uhid.append("000000").append(incrementedId))
					: length == 2 ? (uhid.append("00000").append(incrementedId))
							: length == 3 ? (uhid.append("0000").append(incrementedId))
									: length == 4 ? (uhid.append("000").append(incrementedId))
											: length == 5 ? (uhid.append("00").append(incrementedId))
													: length == 6 ? (uhid.append("0").append(incrementedId))
															: length == 7 ? (uhid.append(incrementedId))
																	: uhid.append("");
			return uhid.toString();
		} catch (Exception e) {
			throw e;
		}
	}

	private Map<String, String> generatePOCToken() throws JSONException, IOException {

		try{
			Map<String, String> tokenMap = new HashMap<>();

			RequestBody requestBody = new FormBody.Builder()
					.add("grant_type", "client_credentials")
					.build();

			String credentials = "NnhaZU5LRWFVVGU2UFA0YVRNMjg4bUVHa2ZRYTpwaFBodUxOVl9kV3UxUXpXR0g5UFI3NVN2WWdh";
			String authorizationHeader = "Basic " + credentials;

			Request request = new Request.Builder()
					.url(TOKEN_URL)
					.post(requestBody)
					.header("Authorization", authorizationHeader)
					.build();

			try (okhttp3.Response response = client.newCall(request).execute()) {
				if (!response.isSuccessful()) {
					throw new IllegalStateException("Unexpected response code: " + response);
				}
				String responseBody = response.body().string();
				JSONObject json = new JSONObject(responseBody);
				tokenMap.put("accessToken", json.getString("access_token"));
				tokenMap.put("scope", json.getString("scope"));
				tokenMap.put("token_type", json.getString("token_type"));
				tokenMap.put("expires_in", String.valueOf(json.getInt("expires_in")));
				tokenMap.put("code", String.valueOf(response.code()));

				return tokenMap;
			} catch (Exception e) {
				throw e;
			}
		}catch (Exception e){
			throw e;
		}
	}

	private void generatePOCSMS(PatientPOCMasterDTO patientPOCMasterDTO, String uhidNo, Map<String, String> POCToken) throws IOException {
		try{
			if ("200".equals(POCToken.get("code"))) {
				String messageForMobile = "Greetings from NAHAR MEDICAL CENTER, "+patientPOCMasterDTO.getFirstName()+" "+(patientPOCMasterDTO.getMiddleName()!=null?patientPOCMasterDTO.getMiddleName():"")+
						" "+patientPOCMasterDTO.getLastName()+". Your unique registration number is "+uhidNo+". For booking appointment call 9819260962";
				String API_KEY = "41194";
				String SECRET_KEY = "41194";
				String APPLICATION_ID = "41194";
				String TEMPLATE_ID = "RFHOSP_001EN";
				String TRANSACTION_ID = "1234557896";

				String jsonBody = "{\"APIKey\":\""+API_KEY+"\"," +
						"\"secretKey\":\""+SECRET_KEY+"\"," +
						"\"applicationId\":\""+APPLICATION_ID+"\"," +
						"\"templateId\":\""+TEMPLATE_ID+"\"," +
						"\"MobileNo\":\""+ patientPOCMasterDTO.getMobileNo() + "\"," +
						"\"Message\":\""+messageForMobile+"\"," +
						"\"ransactionID\":\""+TRANSACTION_ID+"\"," +
						"}";

				MediaType mediaType = MediaType.parse("application/json");
				RequestBody body = RequestBody.create(mediaType, jsonBody);

				Request request = new Request.Builder()
						.url(SMS_API_URL)
						.post(body)
						.addHeader("Cookie", "MYSSO=ce05b8b044ffa112128d6481319c7c7a74a0f40abc0dd5be639eb965141045cba827ae276532c5ab6a53757165426c3849e6cfa7e68dff88d4db7d0a79ad90d1914fc8a6e03f2022c823c73ec57c9ee45df33033531a34d7d0a8b1ca67b3e97b1e85c7409ce558d84c2267da2cb32e7a76d3ce933c0978af3887587d7264f9be15c5dac29ec577da2e511bf7862a21e2cc238a4d68e9f4591dd60b1e7808508c281d7ca3ad39dcc83ca2a42154523ca52bb5a97d92bc0ac4b8135b02a97ae7c98457c9d8f2f207dbf2fd64b64120c5701182a1efd63b71b69fa8a12e967a5799a33f7ea4dc52d132132483c66a0f04c665368af0748c2ed32b4f1bd1a4d56d61d244250f4681ea8bfa11f7c59235fb2e7c601cef1c27ade152b08b879146a9798b1932db5815fc0f2a9987ef96739c9ecfd68807940df94e3da268a1c7409cb415c4dd28c1ab94b6f3d0ad9439579cef; TS018cea1e=0138efca4e659b96731f085149247459df9cc28917e45f625fa1540657c103faf19731d23f1444888fe3c6ed8113d7660ad3cfe1bf; TS01c31594=0138efca4ef4e318bc958e2d1d96c880aed1bcc38e27992aac440a208306e976311fe98727430f61089fe67a212a2353180f57b781")
						.addHeader("Authorization", "Bearer " + POCToken.get("accessToken"))
						.addHeader("Content-Type", "application/json")
						.build();

				try (okhttp3.Response response = client.newCall(request).execute()) {
					// Handle the response if needed
				} catch (Exception e) {
					throw e;
				}
			}
		}catch (Exception e){
			throw e;
		}
	}

	@Override
	public Response searchPOCMaster(String patientSearch) {
		try {

			List<PatientPOCMasterDTO> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_POC_MASTER_BY_POC_UNIQUE_NUMBER").setParameter("patientSearch", patientSearch)
					.setResultTransformer(Transformers.aliasToBean(PatientPOCMasterDTO.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response updatePocMaster(PatientPOCMasterDTO patientPOCMasterDTO) {

		try {

			PatientPOCMaster patientPOCMaster=findById(patientPOCMasterDTO.getPatientPOCId());
			patientPOCMaster.setCheckinStatusId(patientPOCMasterDTO.getCheckinStatusId());

			if(patientPOCMaster.getCheckinStatusId()==1){
				patientPOCMaster.setCheckinDateTime(GlobalCommonDateUtils.getDate(patientPOCMasterDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			} else if (patientPOCMaster.getCheckinStatusId()==2) {
				patientPOCMaster.setPatientPreDateTime(GlobalCommonDateUtils.getDate(patientPOCMasterDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			} else if (patientPOCMaster.getCheckinStatusId()==3) {
				patientPOCMaster.setWheelInDateTime(GlobalCommonDateUtils.getDate(patientPOCMasterDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			} else if (patientPOCMaster.getCheckinStatusId()==4) {
				patientPOCMaster.setWheelOutDateTime(GlobalCommonDateUtils.getDate(patientPOCMasterDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			} else if (patientPOCMaster.getCheckinStatusId()==5) {
				patientPOCMaster.setPatientExitDateTime(GlobalCommonDateUtils.getDate(patientPOCMasterDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			}

			patientPOCMaster = update(patientPOCMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}

}