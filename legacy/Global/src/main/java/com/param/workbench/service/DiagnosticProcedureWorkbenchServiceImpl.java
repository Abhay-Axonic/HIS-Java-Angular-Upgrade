package com.param.workbench.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.engine.hl7.Client;
import com.engine.hl7.v22.Header;
import com.engine.hl7.v22.OBR;
import com.engine.hl7.v22.ORC;
import com.engine.hl7.v22.PID;
import com.engine.hl7.v22.PV1;
import com.engine.hl7.v22.RadOrder;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IRoomModalityMasterDao;
import com.param.global.dto.PatientDetailsDto;
import com.param.global.dto.RadiologyOrdersListDto;
import com.param.global.dto.RoomModalityMasterDto;
import com.param.global.dto.TemplateReqDto;
import com.param.global.opd.dao.IEncounterMasterDao;
import com.param.global.service.IOrderDetailsMasterService;
import com.param.global.service.IPatientRegistrationService;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;
import com.param.workbench.dao.IDiagnosticProcedureWorkbenchDao;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings("rawtypes")
public class DiagnosticProcedureWorkbenchServiceImpl implements IDiagnosticProcedureWorkbenchService,ICommonConstants {
	final static Logger logger = Logger.getLogger(DiagnosticProcedureWorkbenchServiceImpl.class);

	@Autowired
	IDiagnosticProcedureWorkbenchDao iDiagnosticProcedureWorkbenchDao;

	@Autowired
	IEncounterMasterDao iEncounterMasterDao;

	@Autowired
	private SessionService sessionService;
	
	@Autowired
	IOrderDetailsMasterService iOrderDetailsMasterService;
	
	@Autowired
	IRoomModalityMasterDao iRoomModalityMasterDao;
	
	@Autowired
	IPatientRegistrationService iPatientRegistrationService;
	
	@Override
	@Transactional
	public Response getProcedureworkbench(RadiologyOrdersListDto radiologyOrdersListDto) throws ApplicationException {
		try {
			   return iDiagnosticProcedureWorkbenchDao.getProcedureworkbench(radiologyOrdersListDto);
		}catch(Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response acceptRequest(RadiologyOrdersListDto radiologyOrdersListDto) throws ApplicationException {
		try {
			
			Response res = null;
			
			List<Integer> orderDetailsId = new LinkedList<>();
			orderDetailsId.add(radiologyOrdersListDto.getOrderDetailsId());
			res = iOrderDetailsMasterService.renderAndPostRates(orderDetailsId);
			if(res.getStatus().equals(ERROR))
			{
				return res;
			}
			res = iEncounterMasterDao.acceptRadiologyRequest(radiologyOrdersListDto);
			Response mapperRes = iDiagnosticProcedureWorkbenchDao.saveDiagnosticProcedureMapper(radiologyOrdersListDto);
			if (res.getStatus().equals(SUCCESS)) 
			{
				if (radiologyOrdersListDto.getIsPACSRequired() == 'Y') {

					Header header = new Header();
					header.setMsh17_countrycode(radiologyOrdersListDto.getCountryCode());
					header.setMsh3_sendingapplication("HIS");
					header.setMsh12_versionid("1");
					header.setMsh13_sequencenumber("HIS001");
					header.setMsh4_sendingfacility("MPSHAH");
					header.setMsh5_receivingapplicaiton("MPSHAH");
					header.setMsh6_receivingfacility("TRS");

					header.setMsh7_datetime(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(radiologyOrdersListDto.getUnitId()), "yyyyMMddHHmmss"));
					header.setMsh11_processingid(radiologyOrdersListDto.getOrderNo());
					header.setMsh12_versionid("1");

					PID patientDemographics = new PID();

					patientDemographics.setPid2_patientid(radiologyOrdersListDto.getPatientId().toString());
					ArrayList<String> identifier = new ArrayList<>();
					identifier.add((radiologyOrdersListDto.getIdentificationNumber() != null
							&& radiologyOrdersListDto.getIdentificationNumber() != "")
									? radiologyOrdersListDto.getIdentificationNumber()
									: "123456");
					patientDemographics.setPid3_patientidentifier(identifier);
					patientDemographics.setPid5_patientname(radiologyOrdersListDto.getPatientName());

					// get patient Details by patient ID
					Response patientResponse = iPatientRegistrationService
							.getPatientDetailsById(radiologyOrdersListDto.getPatientId());
					if (patientResponse.getObject() != null) {
						PatientDetailsDto patientDtoRes = new PatientDetailsDto();
						patientDtoRes = (PatientDetailsDto) patientResponse.getObject();
						header.setMsh17_countrycode(
								patientDtoRes.getCountryCode() != null ? patientDtoRes.getCountryCode() : "IND");

						patientDemographics.setPid10_race(
								patientDtoRes.getRaceName() != null ? patientDtoRes.getRaceName() : "Asia");
						patientDemographics.setPid11_patientaddress(
								patientDtoRes.getAddress() != null ? patientDtoRes.getAddress() : "");
						patientDemographics.setPid16_maritalstatus(
								patientDtoRes.getMaritalStatus() != null ? patientDtoRes.getMaritalStatus() : "");
						patientDemographics.setPid28_nationality(
								patientDtoRes.getNationalityName() != null ? patientDtoRes.getNationalityName()
										: "Indiain");
						patientDemographics.setPid12_countrycode(
								patientDtoRes.getCountryCode() != null ? patientDtoRes.getCountryCode() : "IND");

						if (patientDtoRes.getDobPacs() != null && patientDtoRes.getDobPacs() != "") {
							patientDemographics.setPid7_dateofbirth(patientDtoRes.getDobPacs());
						}

						patientDemographics.setPid8_administrativesex(
								patientDtoRes.getGenderCode() != null ? patientDtoRes.getGenderCode() : "M");

					}

					PV1 patientVisit = new PV1();

					patientVisit.setPv1_2_patientclass(radiologyOrdersListDto.getPatientCategory() != null
							? radiologyOrdersListDto.getPatientCategory()
							: "National");
					patientVisit.setPv1_19_visitnumber(
							radiologyOrdersListDto.getVisitNumber() != null ? radiologyOrdersListDto.getVisitNumber()
									: "");
					patientVisit.setPv1_4_admissiontype(
							radiologyOrdersListDto.getVisitName() != null ? radiologyOrdersListDto.getVisitName()
									: "OP");

					ORC orderDetails = new ORC();
					orderDetails.setOrc_1_ordercontrol("NW"); // for new order it shold be "NW"
					orderDetails.setOrc_2_placerordernumber(radiologyOrdersListDto.getOrderNo());
					orderDetails.setOrc_5_orderstatus("Renderd");
					orderDetails.setOrc_9_datetimeoftransaction(
							GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(radiologyOrdersListDto.getUnitId()), "yyyyMMddHHmmss"));

					RoomModalityMasterDto roomModalityDto = new RoomModalityMasterDto();
					roomModalityDto.setModalityId(radiologyOrdersListDto.getModalityId());
					roomModalityDto.setOrganizationId(sessionService.getOrganizationId());
					roomModalityDto.setUnitId(sessionService.getUnitId());
					if (radiologyOrdersListDto.getModalityId() != null) {
						Response modalityRes = iRoomModalityMasterDao.getModalityDetailsByModalityId(roomModalityDto);
						if (modalityRes.getListObject() != null && modalityRes.getListObject().size() > 0) {
							roomModalityDto = (RoomModalityMasterDto) modalityRes.getListObject().get(0);
						}
					}

					OBR observation = new OBR();
					observation.setObr_2_placerordernumber(radiologyOrdersListDto.getOrderNo());
					observation.setObr_3_fillerordernumber(radiologyOrdersListDto.getOrderNo());
					observation.setObr_5_priority(radiologyOrdersListDto.getPriorityId().toString());
					observation.setObr_4_universalserviceidentifier(radiologyOrdersListDto.getAccessionNumber() != null
							? radiologyOrdersListDto.getAccessionNumber()
							: "-");
					observation.setObr_5_priority("2");
					observation.setObr_6_requesteddatetime(
							GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(radiologyOrdersListDto.getUnitId()), "yyyyMMddHHmmss"));
					observation.setObr_12_dangercode("Green");

					observation.setObr_24_diagnosticservicesectorid("CT@AECT");
					RadOrder radOrder = new RadOrder();
					radOrder.setHeader(header);
					radOrder.setObservation(observation);
					radOrder.setOrderDetails(orderDetails);
					radOrder.setPatientDemographics(patientDemographics);
					radOrder.setPatientVisit(patientVisit);

					// generating json

					String json = radOrder.GetRadOrderJson(radOrder);

					// validating json
					String status = radOrder.validateRadOrder(json);

					if (status.equals("true")) {
						// send json
						Client client = new Client();

						String response = client.SendtoWebhook(json, "WebReceive");
					} 
				} 
			}
			return res;

		}catch(Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response saveDiagnosticProcedureMapper(RadiologyOrdersListDto radiologyOrdersListDto)
			throws ApplicationException {
		try {
			 return  iDiagnosticProcedureWorkbenchDao.saveDiagnosticProcedureMapper(radiologyOrdersListDto);
		}catch(Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getProcedureResultEntry(RadiologyOrdersListDto radiologyOrdersListDto) throws ApplicationException {
		try {
			 return  iDiagnosticProcedureWorkbenchDao.getProcedureResultEntry(radiologyOrdersListDto);
		}catch(Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getTemplateByTemplateTypeId(TemplateReqDto templateReqDto) throws ApplicationException {
		try {
			 return  iDiagnosticProcedureWorkbenchDao.getTemplateByTemplateTypeId(templateReqDto);
		}catch(Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getProcedureResultValidation(RadiologyOrdersListDto radiologyOrdersListDto)
			throws ApplicationException {
		try {
			 return  iDiagnosticProcedureWorkbenchDao.getProcedureResultValidation(radiologyOrdersListDto);
		}catch(Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getProcedureWorkListForPrinting(RadiologyOrdersListDto radiologyOrdersListDto)
			throws ApplicationException {
		try {
			 return  iDiagnosticProcedureWorkbenchDao.getProcedureWorkListForPrinting(radiologyOrdersListDto);
		}catch(Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response printDiagnosisReport(RadiologyOrdersListDto radiologyOrdersListDto) throws ApplicationException {
		try {
			 return  iDiagnosticProcedureWorkbenchDao.printDiagnosisReport(radiologyOrdersListDto);
		}catch(Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

}
