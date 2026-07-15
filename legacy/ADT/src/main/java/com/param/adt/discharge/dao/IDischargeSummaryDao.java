package com.param.adt.discharge.dao;

import java.io.ByteArrayOutputStream;
import java.util.List;

import com.param.adt.discharge.dto.DischargeSummaryDto;
import com.param.global.common.Response;
import com.param.global.dto.AdmissionDto;

@SuppressWarnings("rawtypes")
public interface IDischargeSummaryDao {

	public Response savePatientDischargeSummary(DischargeSummaryDto dischargeSummaryDto);

	public Response getDischargeSummaryTemplateByPatientId(Integer patientId, Integer organizationId, Integer unitId);

	public Response getPatientDischargeSummaryList(DischargeSummaryDto dischargeSummaryDto);

	public Response getFieldTemplateSummaryByTemplateId(Integer templateId, Integer organizationId, Integer unitId);

	public Response saveDischargeSummaryDetails(DischargeSummaryDto dto);

	public Response getPatientDischargeSummaryFieldList(DischargeSummaryDto dischargeSummaryDto);

	public Response findByPatientIdTemplateId(DischargeSummaryDto dischargeSummaryDto);

	public Response getAdmissionReason(DischargeSummaryDto dischargeSummaryDto);

	public Response updateDischargeSummaryReportStatus(DischargeSummaryDto dischargeSummaryDto);

	public Response getSavedDischargeSummaryByAdmissionId(DischargeSummaryDto dischargeSummaryDto);

	public ByteArrayOutputStream createDischargePDF(DischargeSummaryDto dischargeSummaryDto);

	public Response searchPatientDischargeSummary(DischargeSummaryDto dischargeSummaryDto);

	public Integer getDischargeSummaryIdByAdmissionId(Integer admissionId);

	public Response getFollowUpAppointment(DischargeSummaryDto dischargeSummaryDto);

	public ByteArrayOutputStream createDischargePDF1(DischargeSummaryDto dischargeSummaryDto);

	public Response getPatientDischargeSummaryTemplateData(DischargeSummaryDto dischargeSummaryDto);

	public Response inactiveDischargeSummaryTemplates(DischargeSummaryDto dischargeSummaryDto);

	public Response getDischargeSummaryPrintType(DischargeSummaryDto dischargeSummaryDto);

	public Response updateExtendPdd(AdmissionDto dto);

	public Response updateAdmittedPatientList(AdmissionDto dto);


	public Response sendDischargeSummaryReportToMyLife(DischargeSummaryDto dto);
}
