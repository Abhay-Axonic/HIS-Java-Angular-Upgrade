package com.param.adt.discharge.service;

import java.io.ByteArrayOutputStream;

import javax.transaction.Transactional;

import com.param.adt.discharge.dto.DischargeSummaryDto;
import com.param.global.common.Response;
import com.param.global.dto.AdmissionDto;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IDischargeSummaryService {

	public Response savePatientDischargeSummary(DischargeSummaryDto dischargeSummaryDto);

	public Response getDischargeSummaryTemplateByPatientId(Integer patientId,Integer organizationId, Integer unitId);

	public Response getPatientDischargeSummaryList(DischargeSummaryDto dischargeSummaryDto);

	public Response getFieldTemplateSummaryByTemplateId(Integer templateId,Integer organizationId, Integer unitId);

	public Response getPatientDischargeSummaryFieldList(DischargeSummaryDto dischargeSummaryDto);

	public Response getAdmissionReason(DischargeSummaryDto dischargeSummaryDto);

	public Response updateDischargeSummaryReportStatus(DischargeSummaryDto dischargeSummaryDto);

	public Response getSavedDischargeSummaryByAdmissionId(DischargeSummaryDto dischargeSummaryDto);

	public ByteArrayOutputStream getDischargeSummaryPdf(DischargeSummaryDto dischargeSummaryDto);

	public Response searchPatientDischargeSummary(DischargeSummaryDto dischargeSummaryDto);

	public Integer getDischargeSummaryIdByAdmissionId(Integer admissionId);

	public Response getFollowUpAppointment(DischargeSummaryDto dischargeSummaryDto);

	public Response getPatientDischargeSummaryTemplateData(DischargeSummaryDto dischargeSummaryDto);

	public Response saveDischargeSumTabColumnsPrintMapper(DischargeSummaryDto dischargeSummaryDto);

	public Response getDischargeSummaryPrintType(DischargeSummaryDto dischargeSummaryDto);

	public Response updateExtendPdd(AdmissionDto dto);

	public Response updateAdmittedPatientList(AdmissionDto dto);

	public Response sendDischargeSummaryReportToMyLife(DischargeSummaryDto dto);
}
