package com.param;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.Appointment.AppointmentParticipantComponent;
import org.hl7.fhir.r4.model.Appointment.AppointmentStatus;
import org.hl7.fhir.r4.model.Appointment.ParticipationStatus;
import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.Base64BinaryType;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.CarePlan.CarePlanActivityComponent;
import org.hl7.fhir.r4.model.CarePlan.CarePlanIntent;
import org.hl7.fhir.r4.model.CarePlan.CarePlanStatus;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.DiagnosticReport.DiagnosticReportMediaComponent;
import org.hl7.fhir.r4.model.DiagnosticReport.DiagnosticReportStatus;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.DocumentReference.DocumentReferenceContentComponent;
import org.hl7.fhir.r4.model.DocumentReference.ReferredDocumentStatus;
import org.hl7.fhir.r4.model.Dosage;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Encounter.DiagnosisComponent;
import org.hl7.fhir.r4.model.Encounter.EncounterHospitalizationComponent;
import org.hl7.fhir.r4.model.Encounter.EncounterStatus;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.Enumerations.DocumentReferenceStatus;
import org.hl7.fhir.r4.model.FamilyMemberHistory;
import org.hl7.fhir.r4.model.FamilyMemberHistory.FamilyHistoryStatus;
import org.hl7.fhir.r4.model.FamilyMemberHistory.FamilyMemberHistoryConditionComponent;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.ImagingStudy;
import org.hl7.fhir.r4.model.ImagingStudy.ImagingStudySeriesComponent;
import org.hl7.fhir.r4.model.ImagingStudy.ImagingStudySeriesInstanceComponent;
import org.hl7.fhir.r4.model.ImagingStudy.ImagingStudyStatus;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.Immunization.ImmunizationStatus;
import org.hl7.fhir.r4.model.ImmunizationRecommendation;
import org.hl7.fhir.r4.model.ImmunizationRecommendation.ImmunizationRecommendationRecommendationComponent;
import org.hl7.fhir.r4.model.ImmunizationRecommendation.ImmunizationRecommendationRecommendationDateCriterionComponent;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Media;
import org.hl7.fhir.r4.model.Media.MediaStatus;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.MedicationRequest.MedicationRequestIntent;
import org.hl7.fhir.r4.model.MedicationRequest.MedicationRequestStatus;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.hl7.fhir.r4.model.MedicationStatement.MedicationStatementStatus;
import org.hl7.fhir.r4.model.Narrative.NarrativeStatus;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Observation.ObservationComponentComponent;
import org.hl7.fhir.r4.model.Observation.ObservationReferenceRangeComponent;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.PositiveIntType;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.hl7.fhir.r4.model.PractitionerRole.PractitionerRoleAvailableTimeComponent;
import org.hl7.fhir.r4.model.Procedure;
import org.hl7.fhir.r4.model.Procedure.ProcedureStatus;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.hl7.fhir.r4.model.ServiceRequest.ServiceRequestIntent;
import org.hl7.fhir.r4.model.ServiceRequest.ServiceRequestStatus;
import org.hl7.fhir.r4.model.Specimen;
import org.hl7.fhir.r4.model.Specimen.SpecimenCollectionComponent;
import org.hl7.fhir.r4.model.TimeType;
import org.hl7.fhir.r4.model.Timing;
import org.hl7.fhir.r4.model.Timing.TimingRepeatComponent;
import org.hl7.fhir.r4.model.Timing.UnitsOfTime;

/**
 * The FhirResourcePopulator class populates all the FHIR resources
 */
public class ResourcePopulator {

	// Populate Patient Resource Checked
	public static Patient populatePatientResource() {
		Patient patient = new Patient();
		patient.setId("1efe03bf-9506-40ba-bc9a-80b0d5045afe");
		patient.getMeta().setVersionId("1").setLastUpdatedElement(new InstantType("2020-07-09T14:58:58.181+05:30"))
				.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Patient");
		patient.getText().setStatus(NarrativeStatus.GENERATED)
				.setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\">ABC, 41 year, Male</div>");
		patient.addIdentifier()
				.setType(new CodeableConcept(
						new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "MR", "Medical record number")))
				.setSystem("https://healthid.ndhm.gov.in").setValue("22-7225-4829-5255");
		patient.addName().setText("ABC");
		patient.addTelecom().setSystem(ContactPointSystem.PHONE).setValue("+919818512600").setUse(ContactPointUse.HOME);
		patient.setGender(AdministrativeGender.MALE).setBirthDateElement(new DateType("1981-01-12"));
		return patient;
	}

    // Populate Patient Resource Checked
	public static Patient populateSecondPatientResource() {
		Patient patient = new Patient();
		patient.setId("1efe03bf-9506-40ba-bc9a-80b0d5045afe");
		patient.getMeta().setVersionId("1").setLastUpdatedElement(new InstantType("2020-07-09T14:58:58.181+05:30"))
				.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Patient");
		patient.getText().setStatus(NarrativeStatus.GENERATED)
				.setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\">ABC, 41 year, Female</div>");
		patient.addIdentifier()
				.setType(new CodeableConcept(
						new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "MR", "Medical record number")))
				.setSystem("https://healthid.ndhm.gov.in").setValue("22-7225-4829-5255");
		patient.addName().setText("ABC");
		patient.addTelecom().setSystem(ContactPointSystem.PHONE).setValue("+919818512600").setUse(ContactPointUse.HOME);
		patient.setGender(AdministrativeGender.FEMALE).setBirthDateElement(new DateType("1981-01-12"));
		return patient;
	}

	// Populate Practitioner Resource Checked
	public static Practitioner populatePractitionerResource() {
		Practitioner practitioner = new Practitioner();
		practitioner.setId("3bc96820-c7c9-4f59-900d-6b0ed1fa558e");
		practitioner.getMeta().setVersionId("1").setLastUpdatedElement(new InstantType("2019-05-29T14:58:58.181+05:30"))
				.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Practitioner");
		practitioner.getText().setStatus(NarrativeStatus.GENERATED)
				.setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\">Dr. DEF, MD (Medicine)</div>");
		practitioner.addIdentifier()
				.setType(new CodeableConcept(
						new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "MD", "Medical License number")))
				.setSystem("https://doctor.ndhm.gov.in").setValue("21-1521-3828-3227");
		practitioner.addName().setText("Dr. DEF");
		return practitioner;
	}


	// Populate Practitioner Resource Checked
	public static Practitioner populateSecondPractitionerResource() {
		Practitioner practitioner = new Practitioner();
		practitioner.setId("947c60c9-20f6-41d8-9fce-b5da822a3087");
		practitioner.getMeta().setVersionId("1").setLastUpdatedElement(new InstantType("2019-05-29T14:58:58.181+05:30"))
				.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Practitioner");
		practitioner.getText().setStatus(NarrativeStatus.GENERATED)
				.setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\">Dr. PQR, MD (Medicine)</div>");
		practitioner.addIdentifier()
				.setType(new CodeableConcept(
						new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "MD", "Medical License number")))
				.setSystem("https://doctor.ndhm.gov.in").setValue("25-1531-3528-3228");
		practitioner.addName().setText("Dr. PQR");
		return practitioner;
	}

	// Populate Condition Resource Checked
	public static Condition populateConditionResource() {
		Condition condition = new Condition();
		condition.setId("46e5d4b0-0a3b-487d-97ba-42a479e8b041");
		condition.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Condition");
		condition.getText().setStatus(NarrativeStatus.GENERATED)
				.setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\">Abdominal pain on 09-July 2020</div>");
		condition.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		condition.getCode().addCoding(new Coding("http://snomed.info/sct", "21522001", "Abdominal pain"))
				.setText("Abdominal pain");
		return condition;
	}

	// Populate Condition Resource Checked
	public static Condition populateSecondConditionResource() {
		Condition condition = new Condition();
		condition.setId("2462444a-d494-4380-83c7-790d40c1d36a");
		condition.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Condition");
		condition.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\">Foot has swollen</div>");
		condition.setClinicalStatus(new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/condition-clinical", "active",  "Active")));
		condition.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		condition.getCode().addCoding(new Coding("http://snomed.info/sct", "297142003", "Foot swelling")).setText("Foot swelling");
		return condition;
	}

	// Populate Condition Resource Checked
	public static Condition populateThirdConditionResource() {
		Condition condition = new Condition();
		condition.setId("e0b9c16d-d4f8-4c89-8427-53a94b7f6c24");
		condition.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Condition");
		condition.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\">Past Medical Problem of Diabetes mellitus type 2</div>");
		condition.setClinicalStatus(new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/condition-clinical", "recurrence", "Recurrence")));
		condition.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		condition.getCode().addCoding(new Coding("http://snomed.info/sct", "44054006", "Diabetes mellitus type 2"))
				.setText("Diabetes mellitus type 2");
		return condition;
	}

	// Populate Condition Resource Checked
	public static Condition populateFourConditionResource() {
		Condition condition = new Condition();
		condition.setId("f0c7d911-8bae-4e0a-86ca-92f1f0a4d29d");
		condition.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Condition");
		condition.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\">Past Medical Problem of Diabetes mellitus type 1</div>");
		condition.setClinicalStatus(new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/condition-clinical", "recurrence", "Recurrence")));
		condition.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		condition.getCode().addCoding(new Coding("http://snomed.info/sct", "46635009", "Diabetes mellitus type 1"))
				.setText("Diabetes mellitus type 1");
		return condition;
	}

	// Populate Condition Resource Checked
	public static Condition populateFiveConditionResource() {
		Condition condition = new Condition();
		condition.setId("46e5d4b0-0a3b-487d-97ba-42a479e8b041");
		condition.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Condition");
		condition.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\">History of aortoiliac atherosclerosis</div>");
		condition.setClinicalStatus(new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/condition-clinical", "recurrence", "Recurrence")));
		condition.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		condition.getCode().addCoding(new Coding("http://snomed.info/sct", "440700005", "History of aortoiliac atherosclerosis"))
				.setText("Patient complained about pain in left arm");
		return condition;
	}

		// Populate Condition Resource Checked
	public static Condition populateSixConditionResource() {
		Condition condition = new Condition();
		condition.setId("2462444a-d494-4380-83c7-790d40c1d36a");
		condition.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Condition");
		condition.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\">Myocardial infarction</div>");
		condition.setClinicalStatus(new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/condition-clinical", "active", "Active")));
		condition.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		condition.getCode().addCoding(new Coding("http://snomed.info/sct", "22298006", "Myocardial infarction"))
				.setText("pain in the chest, neck, back or arms, as well as fatigue, lightheadedness, abnormal heartbeat and anxiety.");
		return condition;
	}


	// Populate Binary Resource Checked
	public static Binary populateBinaryResource() {
		Binary binary = new Binary();
		binary.setId("bb9dba90-13f0-41a0-a8e1-f46a4a18886c");
		binary.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Binary");
		binary.setContentType("application/pdf");
		binary.setDataElement(new Base64BinaryType(
				"R0lGODlhfgCRAPcAAAAAAIAAAACAAICAAAAAgIAA oxrXyMY2uvGNcIyj    HOeoxkXBh44OOZdn8Ggu+DiPjwtJ2CZyUomCTRGO"));
		return binary;
	}

	// Populate Allergy Intolerance Resource Checked
	public static AllergyIntolerance populateAllergyIntoleranceResource() {
		AllergyIntolerance allergyIntolerance = new AllergyIntolerance();
		allergyIntolerance.setId("7095e56b-0003-40d6-b451-ce0632377c49");
		allergyIntolerance.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/AllergyIntolerance");
		allergyIntolerance.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\">\n      <p>No Known Allergy</p>\n      <p>recordedDate:2015-08-06</p>\n    </div>");
		allergyIntolerance.setClinicalStatus(new CodeableConcept(
				new Coding("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical", "active", "Active")));

		allergyIntolerance.setVerificationStatus(new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/allergyintolerance-verification", "confirmed", "Confirmed")));
		allergyIntolerance.getCode().addCoding(new Coding("http://snomed.info/sct", "716186003", "No known allergy"));
		allergyIntolerance.setPatient(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		allergyIntolerance.setRecordedDateElement(new DateTimeType("2020-07-09T15:37:31-06:00"));
		allergyIntolerance.setRecorder(new Reference("urn:uuid:3bc96820-c7c9-4f59-900d-6b0ed1fa558e"));
		allergyIntolerance.addNote().setText("The patient reports no other known allergy.");
		return allergyIntolerance;
	}

	// Populate Appointment Resource Checked
	public static Appointment populateAppointmentResource() {
		Appointment appointment = new Appointment();
		appointment.setId("4afbe26c-beb9-4125-bf32-837202b761da");
		appointment.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Appointment");
		appointment.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\">Brian MRI results discussion</div>");
		appointment.setStatus(AppointmentStatus.BOOKED);

        appointment.addServiceCategory(new CodeableConcept(new Coding("http://snomed.info/sct", "408443003", "General medical practice")));

		appointment.addServiceType(new CodeableConcept(new Coding("http://snomed.info/sct", "11429006","Consultation")));

		appointment.setAppointmentType(new CodeableConcept(new Coding("http://snomed.info/sct", "185389009", "Follow-up visit")));

		appointment.addReasonReference(new Reference().setReference("urn:uuid:2462444a-d494-4380-83c7-790d40c1d36a"));

		appointment.setDescription("Discussion on the results of your recent Lab Test and further consultation");

		appointment.setStartElement(new InstantType("2020-07-12T09:00:00Z"));
		appointment.setEndElement(new InstantType("2020-07-12T09:30:00Z"));

		appointment.setCreatedElement(new DateTimeType("2020-07-09T14:58:58.181+05:30"));

		appointment.addBasedOn(new Reference().setReference("urn:uuid:a7a398a8-ffbe-4636-906f-bbe03399e3ba"));

		appointment.getParticipant()
				.add(new AppointmentParticipantComponent().setActor(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"))
						.setStatus(ParticipationStatus.ACCEPTED)
						.setActor(new Reference().setReference("urn:uuid:3bc96820-c7c9-4f59-900d-6b0ed1fa558e"))
						.setStatus(ParticipationStatus.ACCEPTED));
		return appointment;
	}

    // Populate Appointment Resource Checked
	public static Appointment populateSecondAppointmentResource() {
		Appointment appointment = new Appointment();
		appointment.setId("4afbe26c-beb9-4125-bf32-837202b761da");
		appointment.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Appointment");
		appointment.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\">Follow up for further consultation</div>");
		appointment.setStatus(AppointmentStatus.BOOKED);

        appointment.addServiceCategory(new CodeableConcept(new Coding("http://snomed.info/sct", "408443003", "General medical practice")));

		appointment.addServiceType(new CodeableConcept(new Coding("http://snomed.info/sct", "11429006","Consultation")));

		appointment.setAppointmentType(new CodeableConcept(new Coding("http://snomed.info/sct", "185389009", "Follow-up visit")));

		appointment.addReasonReference(new Reference().setReference("urn:uuid:46e5d4b0-0a3b-487d-97ba-42a479e8b041"));

		appointment.setDescription("Discussion on the results of your recent Lab Test and further consultation");

		appointment.setStartElement(new InstantType("2020-07-12T09:00:00Z"));
		appointment.setEndElement(new InstantType("2020-07-12T09:30:00Z"));

		appointment.setCreatedElement(new DateTimeType("2020-07-09T14:58:58.181+05:30"));

		appointment.getParticipant()
				.add(new AppointmentParticipantComponent().setActor(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"))
						.setStatus(ParticipationStatus.ACCEPTED)
						.setActor(new Reference().setReference("urn:uuid:3bc96820-c7c9-4f59-900d-6b0ed1fa558e"))
						.setStatus(ParticipationStatus.ACCEPTED));
		return appointment;
	}


	// Populate Care Plan Resource checked
	public static CarePlan populateCarePlanResource() {
		CarePlan carePlan = new CarePlan();
		carePlan.setId("3a951d43-bea0-4bcf-8b6c-4e4363893846");
		carePlan.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/CarePlan");
		carePlan.setStatus(CarePlanStatus.ACTIVE);
		carePlan.setIntent(CarePlanIntent.PLAN);
		carePlan.addCategory(new CodeableConcept(new Coding("http://snomed.info/sct", "736368003", "Coronary heart disease care plan")));
		carePlan.setTitle("Coronary heart disease care plan");
		carePlan.setDescription("Treatment of coronary artery and related disease problems");
		carePlan.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe").setDisplay("ABC"));
		carePlan.addActivity(new CarePlanActivityComponent().addOutcomeReference(new Reference("urn:uuid:4afbe26c-beb9-4125-bf32-837202b761da")));
		return carePlan;
	}

	// Populate Diagnostic Report Imaging DCM Resource Checked
	public static DiagnosticReport populateDiagnosticReportImagingDCMResource() {
		DiagnosticReport diagnosticReportImaging = new DiagnosticReport();
		diagnosticReportImaging.setId("0a57316f-7d56-424c-b03b-4a4664127d6a");
		diagnosticReportImaging.getMeta().setVersionId("1")
				.setLastUpdatedElement(new InstantType("2020-07-09T14:58:58.181+05:30"))
				.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DiagnosticReportImaging");
		diagnosticReportImaging.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\">\n\t\t\t\t\t\t<h3>Diagnostic Report for ABC issued 9-July 2020 14:26</h3>\n\t\t\t\t\t\t<pre>\ncode: CT of head-neck\nImaging Study: HEAD and NECK CT DICOM imaging study\nConclusion: CT brains: large tumor sphenoid/clivus. </pre>\n\t\t\t\t\t\t<p>XYZ Lab Pvt.Ltd., Inc signed: Dr. DEF Radiologist</p>\n\t\t\t\t\t</div>");
		diagnosticReportImaging.getIdentifier().add(new Identifier().setSystem("https://xyz.com/lab/reports").setValue("5234342"));
		diagnosticReportImaging.addBasedOn(new Reference("urn:uuid:a7a398a8-ffbe-4636-906f-bbe03399e3ba"));
		diagnosticReportImaging.setStatus(DiagnosticReportStatus.FINAL);
		diagnosticReportImaging.addCategory(new CodeableConcept(new Coding("http://snomed.info/sct", "310128004", "Computerized tomography service")));
		diagnosticReportImaging.setCode(
				new CodeableConcept(new Coding("http://loinc.org", "82692-5", "CT Head and Neck WO contrast")).setText("CT Head and Neck WO contrast"));
		diagnosticReportImaging.setSubject(new Reference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		diagnosticReportImaging.setIssuedElement(new InstantType("2020-07-10T11:45:33+11:00"));
		diagnosticReportImaging.addPerformer(new Reference("urn:uuid:6a7ce76d-8d49-4395-9a15-41abefa65430"));
		diagnosticReportImaging.getResultsInterpreter()
		.add(new Reference().setReference("urn:uuid:3bc96820-c7c9-4f59-900d-6b0ed1fa558e").setDisplay("Dr. Def"));
		diagnosticReportImaging.addImagingStudy(new Reference("urn:uuid:97c2d8e5-771f-4c8c-b1a8-46e680196b89"));
		diagnosticReportImaging.getMedia()
				.add(new DiagnosticReportMediaComponent(new Reference().setReference("urn:uuid:a2398b75-d49c-4cfc-b579-569d7d2aef2d")));
		diagnosticReportImaging.setConclusion("CT brains: large tumor sphenoid/clivus.");
		diagnosticReportImaging.addConclusionCode(new CodeableConcept(new Coding("http://snomed.info/sct", "188340000", "Malignant tumor of craniopharyngeal duct")));
		diagnosticReportImaging.addPresentedForm(new Attachment().setContentType("application/pdf").setLanguage("en-IN").setDataElement(new Base64BinaryType("JVBERi0xLjcNCiW1tbW1DQoxIDAgb2JqDQo8PC9UeXBlL0NhdGFsb2cvUGFnZXMgMiAwIFIvTGFuZyhlbi1VUykgL1N0cnVjdFRyZWVSb290IDE1IDAgUi9NYXJrSW5mbzw8L01hcmtlZCB0cnVlPj4vTWV0YWRhdGEgNDcgMCBSL1ZpZXdlclByZWZlcmVuY2VzIDQ4IDAgUj4+DQplbmRvYmoNCjIgMCBvYmoNCjw8L1R5cGUvUGFnZXMvQ291bnQgMS9LaWRzWyAzIDAgUl0gPj4NCmVuZG9iag0KMyAwIG9iag0KPDwvVHlwZS9QYWdlL1BhcmVudCAyIDAgUi9SZXNvdXJjZXM8PC9Gb250PDwvRjEgNSAwIFIF5WfN43PmsHPmfIz5v9kP0IWspCFLGQhC1nIQhaykIUsZCELWchCdsTEz/6Ue8hCFrKQhSxkIQtZyEIWspCFLGQhC1nIQvb7mLaQ4oEhwDF/mwI")).setTitle("Diagnostic Report"));
		return diagnosticReportImaging;
	}

	// Populate Diagnostic Report Imaging Media Resource
	public static DiagnosticReport populateDiagnosticReportImagingMediaResource() {
		DiagnosticReport diagnosticReportImaging = new DiagnosticReport();
		diagnosticReportImaging.setId("DiagnosticReport-01");
		diagnosticReportImaging.getMeta().setVersionId("1")
				.setLastUpdatedElement(new InstantType("2020-07-09T14:58:58.181+05:30"))
				.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DiagnosticReportImaging");
		diagnosticReportImaging.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\">\n\t\t\t\t\t\t<h3>Diagnostic Report for ABC issued 9-July 2020 14:26</h3>\n\t\t\t\t\t\t<pre>\nTest: CT of head-neck\nConclusion: CT brains: large tumor sphenoid/clivus. </pre>\n\t\t\t\t\t\t<p>XYZ Lab Pvt.Ltd., Inc signed: Dr. DEF Radiologist</p>\n\t\t\t\t\t</div>");
		diagnosticReportImaging.addIdentifier(new Identifier().setSystem("https://xyz.com/lab/reports").setValue("5234342"));
		diagnosticReportImaging.addBasedOn(new Reference("urn:uuid:a7a398a8-ffbe-4636-906f-bbe03399e3ba"));
		diagnosticReportImaging.setStatus(DiagnosticReportStatus.FINAL);
		diagnosticReportImaging.addCategory(new CodeableConcept(new Coding("http://snomed.info/sct", "310128004", "Computerized tomography service")));
		diagnosticReportImaging.setCode(
				new CodeableConcept(new Coding("http://loinc.org", "82692-5", "CT Head and Neck WO contrast")).setText("CT Head and Neck WO contrast"));
		diagnosticReportImaging.setSubject(new Reference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		diagnosticReportImaging.setIssuedElement(new InstantType("2020-07-10T11:45:33+11:00"));
		diagnosticReportImaging.addPerformer(new Reference("urn:uuid:6a7ce76d-8d49-4395-9a15-41abefa65430"));
		diagnosticReportImaging.getResultsInterpreter()
		.add(new Reference().setReference("urn:uuid:3bc96820-c7c9-4f59-900d-6b0ed1fa558e").setDisplay("Dr. DEF"));
		diagnosticReportImaging.getMedia()
				.add(new DiagnosticReportMediaComponent(new Reference().setReference("urn:uuid:a2398b75-d49c-4cfc-b579-569d7d2aef2d")));
		diagnosticReportImaging.setConclusion("CT brains: large tumor sphenoid/clivus.");
		diagnosticReportImaging.addConclusionCode(new CodeableConcept(new Coding("http://snomed.info/sct", "188340000", "Malignant tumor of craniopharyngeal duct")));
		diagnosticReportImaging.addPresentedForm(new Attachment().setContentType("application/pdf").setLanguage("en-IN").setDataElement(new Base64BinaryType("JVBERi0xLjcNCiW1tbW1DQoxIDAgb2JqDQo8PC9UeXBlL0NhdGFsb2cvUGFnZXMgMiAwIFIvTGFuZyhlbi1VUykgL1N0cnVjdFRyZWVSb290IDE1IDAgUi9NYXJrSW5mbzw8L01hcmtlZCB0cnVlPj4vTWV0YWRhdGEgNDcgMCBSL1ZpZXdlclByZWZlcmVuY2VzIDQ4IDAgUj4+DQplbmRvYmoNCjIgMCBvYmoNCjw8L1R5cGUvUGFnZXMvQ291bnQgMS9LaWRzWyAzIDAgUl0gPj4NCmVuZG9iag0KMyAwIG9iag0KPDwvVHlwZS9QYWdlL1BhcmVudCAyIDAgUi9SZXNvdXJjZXM8PC9Gb250PDwvRjEgNSAwIFI+Pi9FeHRHU3RhdGU8PC9HUzcgNyAwIFIvR1M4IDggMCBSPj4vWE9iamVjdDw8L0ltYWdlOSA5IDAgUi9JbWFnZTExIDExIDAgUi9JbWFnZTEzIDEzIDAgUj4+L1Byb2NTZXRbL1BERi9UZXh0L0ltYWdlQi9JbWFnZUMvSW1hZ2VJXSA+Pi9NZWRpYUJveFsgMCAwIDYxMiA3OTJdIC9Db250ZW50cyA0IDAgUi9Hcm91cDw8L1R5cGUvR3JvdXAvUy9UcmFuc3BhcmVuY3kvQ1MvRGV2aWNlUkdCPj4vVGFicy9TL1N0cnVjdFBhcmVudHMgMD4")).setTitle("Diagnostic Report"));
		return diagnosticReportImaging;
	}

	// Populate Diagnostic Report Lab Resource Checked
	public static DiagnosticReport populateDiagnosticReportLabResource() {
		DiagnosticReport diagnosticReportLab = new DiagnosticReport();
		diagnosticReportLab.setId("0a57316f-7d56-424c-b03b-4a4664127d6a");
		diagnosticReportLab.getMeta().setVersionId("1")
				.setLastUpdatedElement(new InstantType("2020-07-09T15:32:26.605+05:30"))
				.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DiagnosticReportLab");
		diagnosticReportLab.addIdentifier(new Identifier().setSystem("https://xyz.com/lab/reports").setValue("5234342"));
		diagnosticReportLab.addBasedOn().setReference("urn:uuid:a7a398a8-ffbe-4636-906f-bbe03399e3ba");
		diagnosticReportLab.setStatus(DiagnosticReportStatus.FINAL);
		diagnosticReportLab.addCategory(new CodeableConcept(new Coding("http://snomed.info/sct", "708196005", "Hematology service")));
		diagnosticReportLab.setCode(
				new CodeableConcept(new Coding("http://loinc.org", "24331-1", "Lipid 1996 panel - Serum or Plasma")));
		diagnosticReportLab.setSubject(new Reference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		diagnosticReportLab.setIssuedElement(new InstantType("2020-07-10T11:45:33+11:00"));
		diagnosticReportLab.addPerformer(new Reference("urn:uuid:6a7ce76d-8d49-4395-9a15-41abefa65430"));
		diagnosticReportLab.addSpecimen().setReference("urn:uuid:e0702f22-e242-47a1-81aa-3cafe82669e2");
		diagnosticReportLab.getResultsInterpreter()
				.add(new Reference().setReference("urn:uuid:3bc96820-c7c9-4f59-900d-6b0ed1fa558e").setDisplay("Dr. DEF"));
		diagnosticReportLab.setConclusion("Elevated cholesterol/high density lipoprotein ratio");
		diagnosticReportLab.addResult(new Reference().setReference("urn:uuid:46923585-2f65-4deb-8c51-56b2a5b5c14b"))
				.addResult(new Reference().setReference("urn:uuid:969f3ed4-f0e2-48ff-ae34-e72b6a3ae9d6"))
				.addResult(new Reference().setReference("urn:uuid:bdada6cb-c93e-42a8-b7fe-0a2490800cfd"));
		return diagnosticReportLab;
	}

	// Populate Document Reference Resource checked
	public static DocumentReference populateDocumentReferenceResource() {
		DocumentReference documentReference = new DocumentReference();
		documentReference.setId("15851b05-ed16-4de2-832d-21ace5660d32");
		documentReference.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DocumentReference");
		documentReference.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Generated Narrative: DocumentReference</b><a name=\"1\"> </a></p><div style=\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\"><p style=\"margin-bottom: 0px\">Resource DocumentReference &quot;1&quot; </p><p style=\"margin-bottom: 0px\">Profile: <a href=\"StructureDefinition-DocumentReference.html\">DocumentReference</a></p></div><p><b>status</b>: current</p><p><b>docStatus</b>: final</p><p><b>type</b>: Laboratory report <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#4241000179101)</span></p><p><b>subject</b>: <a href=\"#Patient_1\">See above (Patient/1)</a></p><blockquote><p><b>content</b></p><h3>Attachments</h3><table class=\"grid\"><tr><td style=\"display: none\">-</td><td><b>ContentType</b></td><td><b>Language</b></td><td><b>Data</b></td><td><b>Title</b></td><td><b>Creation</b></td></tr><tr><td style=\"display: none\">*</td><td>application/pdf</td><td>en-IN</td><td>(base64 data - 302995 bytes)</td><td>Laboratory report</td><td>2019-05-29 14:58:58+0530</td></tr></table></blockquote></div>");
		documentReference.setStatus(DocumentReferenceStatus.CURRENT);
		documentReference.setDocStatus(ReferredDocumentStatus.FINAL);
		documentReference.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		documentReference
				.setType(new CodeableConcept(new Coding("http://snomed.info/sct", "4241000179101", "Laboratory report"))
						.setText("Laboratory report"));
		documentReference.getContent()
				.add(new DocumentReferenceContentComponent(new Attachment().setContentType("application/pdf")
						.setLanguage("en-IN").setTitle("Laboratory report")
						.setCreationElement(new DateTimeType("2019-05-29T14:58:58.181+05:30"))
						.setDataElement(new Base64BinaryType("JVBERi0xLjcNCiW1tbW1DQoxIDAgb2JqDQo8PC9UeXBlL0NhdGFsb2cvUGFnZXMgMiAwIFIvTGFuZyhlbi1VUykgL1N0cnVjdFRyZWVSb290IDE1IDAgUi9NYXJrSW5mbzw8L01hcmtlZCB0cnVlPj4vTWV0YWRhdGEgNDcgMCBSL1ZpZXdlclByZWZlcmVuY2VzIDQ4IDAgUj4+DQplbmRvYmoNCjIgMCBvYmoNCjw8L1R5cGUvUGFnZXMvQ291bnQgMS9LaWRzWyAzIDAgUl0gPj4NCmVuZG9iag0KMyAwIG9iag0KPDwvVHlwZS9QYWdlL1BhcmVudCAyIDAgUi9SZXNvdXJjZXM8PC9Gb250PDwvRjEgNSAwIFI"))));
		return documentReference;
	}

    // Populate Document Reference Resource checked
	public static DocumentReference populateSecondDocumentReferenceResource() {
		DocumentReference documentReference = new DocumentReference();
		documentReference.setId("15851b05-ed16-4de2-832d-21ace5660d32");
		documentReference.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DocumentReference");
		documentReference.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Generated Narrative: DocumentReference</b><a name=\"1\"> </a></p><div style=\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\"><p style=\"margin-bottom: 0px\">Resource DocumentReference &quot;1&quot; </p><p style=\"margin-bottom: 0px\">Profile: <a href=\"StructureDefinition-DocumentReference.html\">DocumentReference</a></p></div><p><b>status</b>: current</p><p><b>docStatus</b>: final</p><p><b>type</b>: Wellness Record <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> ()</span></p><p><b>subject</b>: <a href=\"#Patient_1\">See above (Patient/1)</a></p><blockquote><p><b>content</b></p><h3>Attachments</h3><table class=\"grid\"><tr><td style=\"display: none\">-</td><td><b>ContentType</b></td><td><b>Language</b></td><td><b>Data</b></td><td><b>Title</b></td><td><b>Creation</b></td></tr><tr><td style=\"display: none\">*</td><td>application/pdf</td><td>en-IN</td><td>(base64 data - 302995 bytes)</td><td>Laboratory report</td><td>2019-05-29 14:58:58+0530</td></tr></table></blockquote></div>");
		documentReference.setStatus(DocumentReferenceStatus.CURRENT);
		documentReference.setDocStatus(ReferredDocumentStatus.FINAL);
		documentReference.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		documentReference
				.setType(new CodeableConcept().setText("Wellness Record"));
		documentReference.getContent()
				.add(new DocumentReferenceContentComponent(new Attachment().setContentType("application/pdf")
						.setLanguage("en-IN").setTitle("Laboratory report")
						.setCreationElement(new DateTimeType("2019-05-29T14:58:58.181+05:30"))
						.setDataElement(new Base64BinaryType("JVBERi0xLjcNCiW1tbW1DQoxIDAgb2JqDQo8PC9UeXBlL0NhdGFsb2cvUGFnZXMgMiAwIFIvTGFuZyhlbi1VUykgL1N0cnVjdFRyZWVSb290IDE1IDAgUi9NYXJrSW5mbzw8L01hcmtlZCB0cnVlPj4vTWV0YWRhdGEgNDcgMCBSL1ZpZXdlclByZWZlcmVuY2VzIDQ4IDAgUj4+DQplbmRvYmoNCjIgMCBvYmoNCjw8L1R5cGUvUGFnZXMvQ291bnQgMS9LaWRzWyAzIDAgUl0gPj4NCmVuZG9iag0KMyAwIG9iag0KPDwvVHlwZS9QYWdlL1BhcmVudCAyIDAgUi9SZXNvdXJjZXM8PC9Gb250PDwvRjEgNSAwIFI"))));
		return documentReference;
	}

	// Populate Document Reference Resource checked
	public static DocumentReference populateThirdDocumentReferenceResource() {
		DocumentReference documentReference = new DocumentReference();
		documentReference.setId("15851b05-ed16-4de2-832d-21ace5660d32");
		documentReference.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DocumentReference");
		documentReference.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Generated Narrative: DocumentReference</b><a name=\"1\"> </a></p><div style=\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\"><p style=\"margin-bottom: 0px\">Resource DocumentReference &quot;1&quot; </p><p style=\"margin-bottom: 0px\">Profile: <a href=\"StructureDefinition-DocumentReference.html\">DocumentReference</a></p></div><p><b>status</b>: current</p><p><b>docStatus</b>: final</p><p><b>type</b>: Discharge Summary <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#373942005)</span></p><p><b>subject</b>: <a href=\"#Patient_1\">See above (Patient/1)</a></p><blockquote><p><b>content</b></p><h3>Attachments</h3><table class=\"grid\"><tr><td style=\"display: none\">-</td><td><b>ContentType</b></td><td><b>Language</b></td><td><b>Data</b></td><td><b>Title</b></td><td><b>Creation</b></td></tr><tr><td style=\"display: none\">*</td><td>application/pdf</td><td>en-IN</td><td>(base64 data - 302213 bytes)</td><td>Discharge Summary</td><td>2020-07-09 11:46:09+0530</td></tr></table></blockquote></div>");
		documentReference.setStatus(DocumentReferenceStatus.CURRENT);
		documentReference.setDocStatus(ReferredDocumentStatus.FINAL);
		documentReference.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		documentReference
				.setType(new CodeableConcept(new Coding("http://snomed.info/sct", "373942005", "Discharge Summary"))
						.setText("Discharge Summary"));
		documentReference.getContent()
				.add(new DocumentReferenceContentComponent(new Attachment().setContentType("application/pdf")
						.setLanguage("en-IN").setTitle("Discharge Summary")
						.setCreationElement(new DateTimeType("2019-05-29T14:58:58.181+05:30"))
						.setDataElement(new Base64BinaryType("JVBERi0xLjcNCiW1tbW1DQoxIDAgb2JqDQo8PC9UeXBlL0NhdGFsb2cvUGFnZXMgMiAwIFIvTGFuZyhlbi1VUykgL1N0cnVjdFRyZWVSb290IDE1IDAgUi9NYXJrSW5mbzw8L01hcmtlZCB0cnVlPj4vTWV0YWRhdGEgNDcgMCBSL1ZpZXdlclByZWZlcmVuY2VzIDQ4IDAgUj4+DQplbmRvYmoNCjIgMCBvYmoNCjw8L1R5cGUvUGFnZXMvQ291bnQgMS9LaWRzWyAzIDAgUl0gPj4NCmVuZG9iag0KMyAwIG9iag0KPDwvVHlwZS9QYWdlL1BhcmVudCAyIDAgUi9SZXNvdXJjZXM8PC9Gb250PDwvRjEgNSAwIFI"))));
		return documentReference;
	}


	// Populate Encounter Resource checked
	public static Encounter populateEncounterResource() {
		Encounter encounter = new Encounter();
		encounter.setId("d9f833de-ea59-4ad0-9ae3-2bfb8647193c");
		encounter.setStatus(EncounterStatus.FINISHED);
		encounter.getMeta().setLastUpdatedElement(new InstantType("2020-07-09T14:58:58.181+05:30"))
				.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Encounter");
		encounter.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\">Out Patient Consultation Encounter</div>");
		encounter.getIdentifier().add(new Identifier().setSystem("https://ndhm.in").setValue("S100"));
		encounter.setClass_(
				new Coding("http://terminology.hl7.org/CodeSystem/v3-ActCode", "AMB", "ambulatory"));
		encounter.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		encounter.setPeriod(new Period().setStartElement(new DateTimeType("2020-07-09T14:58:58.181+05:30")));

		List<DiagnosisComponent> diagnosisComponents=new ArrayList<>();

        diagnosisComponents.add(new DiagnosisComponent().setCondition(new Reference("urn:uuid:2462444a-d494-4380-83c7-790d40c1d36a")).setUse(new CodeableConcept(new Coding("http://snomed.info/sct", "33962009", "Chief complaint"))));

		diagnosisComponents.add(new DiagnosisComponent().setCondition(new Reference("urn:uuid:f0c7d911-8bae-4e0a-86ca-92f1f0a4d29d")).setUse(new CodeableConcept(new Coding("http://snomed.info/sct", "148006", "Preliminary diagnosis"))));
		
		encounter.setDiagnosis(diagnosisComponents);
		return encounter;
	}

	// Populate Encounter Resource checked
	public static Encounter populateSecondEncounterResource() {
		Encounter encounter = new Encounter();
		encounter.setId("d9f833de-ea59-4ad0-9ae3-2bfb8647193c");
		encounter.setStatus(EncounterStatus.FINISHED);
		encounter.getMeta().setLastUpdatedElement(new InstantType("2020-07-09T14:58:58.181+05:30"))
				.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Encounter");
		encounter.getIdentifier().add(new Identifier().setSystem("https://ndhm.in").setValue("S100"));
		encounter.setClass_(
				new Coding("http://terminology.hl7.org/CodeSystem/v3-ActCode", "IMP", "inpatient encounter"));
		encounter.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		encounter.setPeriod(new Period().setStartElement(new DateTimeType("2020-04-20T15:32:26.605+05:30"))
				.setEndElement(new DateTimeType("2020-05-01T15:32:26.605+05:30")));
		encounter
				.setHospitalization(new EncounterHospitalizationComponent().setDischargeDisposition(new CodeableConcept(
						new Coding("http://terminology.hl7.org/CodeSystem/discharge-disposition", "home", "Home"))
						.setText("Discharged to Home Care")));
		return encounter;
	}

	// Populate Family Member History Resource
	public static FamilyMemberHistory populateFamilyMemberHistoryResource() {
		FamilyMemberHistory familyMemberHistory = new FamilyMemberHistory();
		familyMemberHistory.setId("FamilyMemberHistory-01");
		familyMemberHistory.getMeta()
				.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/FamilyMemberHistory");
		familyMemberHistory.setStatus(FamilyHistoryStatus.COMPLETED);
		familyMemberHistory.getCondition()
				.add(new FamilyMemberHistoryConditionComponent(new CodeableConcept(new Coding("http://snomed.info/sct",
						"315619001", "FH myocardial infarction male first degree age known")).setText("Heart Attack"))
						.setContributedToDeath(true));
		return familyMemberHistory;
	}

	// Populate Imaging Study Resource Checked
	public static ImagingStudy populateImagingStudyResource() {
		ImagingStudy imagingStudy = new ImagingStudy();
		imagingStudy.setId("97c2d8e5-771f-4c8c-b1a8-46e680196b89");
		imagingStudy.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/ImagingStudy");
		imagingStudy.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\">HEAD and NECK CT DICOM imaging study</div>");
		imagingStudy.addIdentifier(new Identifier().setSystem("https://xyz.in/DCMServer").setValue("7897"));
		imagingStudy.setStatus(ImagingStudyStatus.AVAILABLE);
		imagingStudy.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		imagingStudy.getInterpreter().add(new Reference("urn:uuid:3bc96820-c7c9-4f59-900d-6b0ed1fa558e"));
		imagingStudy.setNumberOfSeries(1);
		imagingStudy.setNumberOfInstances(1);
		ImagingStudySeriesComponent img = new ImagingStudySeriesComponent();
		img.setUid("2.16.124.113543.6003.2588828330.45298.17418.2723805630").setNumber(1)
				.setModality(new Coding("http://snomed.info/sct", "429858000", "CT of head and neck"))
				.setDescription("CT Surview 180").setNumberOfInstances(1)
				.setBodySite(new Coding("http://snomed.info/sct", "774007", "Structure of head and/or neck"))
				.getInstance()
				.add(new ImagingStudySeriesInstanceComponent()
						.setUid("2.16.124.113543.6003.189642796.63084.16748.2599092903")
						.setSopClass(new Coding("urn:ietf:rfc:3986", "urn:oid:1.2.840.10008.5.1.4.1.1.2", ""))
						.setNumber(1).setTitle("CT of head and neck"));
		imagingStudy.getSeries().add(img);
		return imagingStudy;
	}

	// Populate Immunization Resource Checked
	public static Immunization populateImmunizationResource() {
		Immunization immunization = new Immunization();
		immunization.setId("4cb108c1-5590-45f5-98c0-ade5266bcd19");
		immunization.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Immunization");
		immunization.getMeta().setLastUpdatedElement(new InstantType("2020-10-10T14:58:58.181+05:30"))
				.setVersionId("1");
		immunization.getText().setStatus(NarrativeStatus.GENERATED);
		immunization.getText().setDivAsString(
				"<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Narrative with Details</b></p><p><b>id</b>: 1</p><p><b>status</b>: completed</p><p><b>vaccineCode</b>: COVID-19 antigen vaccine <span>(Details : {http://snomed.info/sct code '1119305005' = 'COVID-19 antigen vaccine'})</span></p><p><b>patient</b>: <a>Patient/1</a></p><p><b>occurrence</b>: 21/02/2021</p><p><b>primarySource</b>: true</p></div>");
		immunization.setStatus(ImmunizationStatus.COMPLETED);
		immunization.setVaccineCode(
				new CodeableConcept(new Coding("http://snomed.info/sct", "1119305005", "COVID-19 antigen vaccine")));
		immunization.setPatient(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		immunization.setOccurrence(new DateTimeType("2021-02-21"));
		immunization.setLotNumber("BSCD12344SS");
		immunization.setPrimarySource(true);
		return immunization;
	}

	// Populate Immunization Recommendation Resource Checked
	public static ImmunizationRecommendation populateImmunizationRecommendation() {
		ImmunizationRecommendation immunizationRecommendation = new ImmunizationRecommendation();

		immunizationRecommendation.setId("f0f0766f-1960-426a-94d2-dfe5e3e6549c");
		immunizationRecommendation.setPatient(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		immunizationRecommendation.setAuthority(new Reference().setReference("urn:uuid:364cad65-3038-40df-a8c6-2139d87e1b19"));
		immunizationRecommendation.setDateElement(new DateTimeType("2021-01-10T11:04:15.817-05:00"));
		immunizationRecommendation.getText().setStatus(NarrativeStatus.GENERATED);
		immunizationRecommendation.getText().setDivAsString(
				"<div xmlns=\"http://www.w3.org/1999/xhtml\">This is Immunization Recommendation for COVID-19 mRNA vaccine to be taken on 2021-05-10.</div>");
		ImmunizationRecommendationRecommendationComponent immnunizationRecommendationRecommendationComponent = new ImmunizationRecommendationRecommendationComponent();
		immnunizationRecommendationRecommendationComponent.addVaccineCode(
				new CodeableConcept(new Coding("http://snomed.info/sct", "1119305005", "COVID-19 antigen vaccine")));
		immnunizationRecommendationRecommendationComponent.setForecastStatus(new CodeableConcept(
				new Coding("http://terminology.hl7.org/CodeSystem/immunization-recommendation-status", "due", "Due")));
		immnunizationRecommendationRecommendationComponent.setDescription("First sequence in protocol");
		immnunizationRecommendationRecommendationComponent.setSeries("Vaccination Series 1");
		immnunizationRecommendationRecommendationComponent.setDoseNumber(new PositiveIntType().setValue(1));
		immnunizationRecommendationRecommendationComponent.setSeriesDoses(new PositiveIntType(2));
		immnunizationRecommendationRecommendationComponent
				.addSupportingImmunization(new Reference().setReference("urn:uuid:4cb108c1-5590-45f5-98c0-ade5266bcd19"));
		ImmunizationRecommendationRecommendationDateCriterionComponent dateCreationComponet = new ImmunizationRecommendationRecommendationDateCriterionComponent();
		dateCreationComponet
				.setCode(new CodeableConcept(new Coding("http://loinc.org", "30980-7", "Date vaccine due")));
		dateCreationComponet.setValueElement(new DateTimeType("2021-05-10T00:00:00-05:00"));
		immnunizationRecommendationRecommendationComponent.addDateCriterion(dateCreationComponet);
		immunizationRecommendation.getRecommendation().add(immnunizationRecommendationRecommendationComponent);

		return immunizationRecommendation;
	}

	// Populate Media Resource Checked
	public static Media populateMediaResource() {
		Media media = new Media();
		media.setId("a2398b75-d49c-4cfc-b579-569d7d2aef2d");
		media.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Media");
		media.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\">HEAD and NECK CT DICOM imaging study</div>");
		media.setStatus(MediaStatus.COMPLETED);
		media.setContent(new Attachment().setContentType("image/jpeg").setDataElement(new Base64BinaryType(
				"/9j/4AAQSkZJRgABAQEASABIAAD/4RBGaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLwA8P3hwYWNrZXQgYmVnaW49Iu+7vyIgaWQ9Ilc1TTBNcENlaGlIenJlU3pOVGN6a2M5ZCI/PiA8eDp4bXBtZXRhIHhtbG5zOng9ImFkb2JlOm5zOm1ldGEvIiB4OnhtcHRrPSJBZG9iZSBYTVAgQ29yZSA0LjIuMi1jMDYzIDUzLjM1MjYyNCwgMjAwOC8wNy8zMC0xODowNTo0MSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp"))
				.setTitle("Computed tomography (CT) of head and neck")
				.setCreationElement(new DateTimeType("2020-07-09T11:46:09+05:30")).setLanguage("en-IN"));
		media.setModality(
				new CodeableConcept(new Coding("http://snomed.info/sct", "429858000", "CT of head and neck")));
		media.setBodySite(
				new CodeableConcept(new Coding("http://snomed.info/sct", "774007", "Structure of head and/or neck")));
		media.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		media.setCreated(new DateTimeType("2020-07-10"));
		return media;
	}

	// Populate Medication Request Resource Checked
	public static MedicationRequest  populateMedicationRequestResource() {
		MedicationRequest medicationRequest = new MedicationRequest();
		medicationRequest.setId("0868a5bc-5691-426c-931c-f085788355ed");
		medicationRequest.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/MedicationRequest");
		medicationRequest.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Generated Narrative: MedicationRequest</b><a name=\"1\"> </a></p><div style=\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\"><p style=\"margin-bottom: 0px\">Resource MedicationRequest &quot;1&quot; </p><p style=\"margin-bottom: 0px\">Profile: <a href=\"StructureDefinition-MedicationRequest.html\">MedicationRequest</a></p></div><p><b>status</b>: active</p><p><b>intent</b>: order</p><p><b>medication</b>: Azithromycin 250 mg oral tablet <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#324252006)</span></p><p><b>subject</b>: <a href=\"#Patient_1\">See above (Patient/1: ABC)</a></p><p><b>authoredOn</b>: 2020-07-09</p><p><b>requester</b>: <a href=\"#Practitioner_1\">See above (Practitioner/1: Dr. DEF)</a></p><p><b>reasonCode</b>: Traveler's diarrhea <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#11840006)</span></p><p><b>reasonReference</b>: <a href=\"#Condition_1\">See above (Condition/1)</a></p><h3>DosageInstructions</h3><table class=\"grid\"><tr><td style=\"display: none\">-</td><td><b>Text</b></td><td><b>AdditionalInstruction</b></td><td><b>Timing</b></td><td><b>Route</b></td><td><b>Method</b></td></tr><tr><td style=\"display: none\">*</td><td>One tablet at once</td><td>With or after food <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#311504000)</span></td><td>Once per 1 days</td><td>Oral Route <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#26643006)</span></td><td>Swallow <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#421521009)</span></td></tr></table></div>");
		medicationRequest.setStatus(MedicationRequestStatus.ACTIVE);
		medicationRequest.setIntent(MedicationRequestIntent.ORDER);
		medicationRequest.setMedication(new CodeableConcept(new Coding("http://snomed.info/sct", "1145423002",
				"Azithromycin 250 mg oral tablet")));
		medicationRequest.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe").setDisplay("ABC"));
		medicationRequest.setAuthoredOnElement(new DateTimeType("2020-07-09"));
		medicationRequest
				.setRequester(new Reference().setReference("urn:uuid:3bc96820-c7c9-4f59-900d-6b0ed1fa558e").setDisplay("Dr DEF"));
		medicationRequest.getReasonCode()
				.add(new CodeableConcept(new Coding("http://snomed.info/sct", "11840006", "Traveler's diarrhea")));
		medicationRequest.getReasonReference().add(new Reference().setReference("urn:uuid:46e5d4b0-0a3b-487d-97ba-42a479e8b041"));
		medicationRequest.addDosageInstruction(new Dosage().setText("One tablet at once")
				.addAdditionalInstruction(
						new CodeableConcept(new Coding("http://snomed.info/sct", "311504000", "With or after food")))
				.setTiming(new Timing().setRepeat(
						new TimingRepeatComponent().setFrequency(1).setPeriod(1).setPeriodUnit(UnitsOfTime.D)))
				.setRoute(new CodeableConcept(new Coding("http://snomed.info/sct", "26643006", "Oral Route")))
				.setMethod(new CodeableConcept(new Coding("http://snomed.info/sct", "421521009", "Swallow"))));
		return medicationRequest;
	}

	// Populate Medication Request Resource Checked
	public static MedicationRequest populateSecondMedicationRequestResource() {
		MedicationRequest medicationRequest = new MedicationRequest();
		medicationRequest.setId("ddbff348-63e3-4b85-b431-e8d1d19d365b");
		medicationRequest.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/MedicationRequest");
		medicationRequest.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Generated Narrative: MedicationRequest</b><a name=\"2\"> </a></p><div style=\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\"><p style=\"margin-bottom: 0px\">Resource MedicationRequest &quot;2&quot; </p><p style=\"margin-bottom: 0px\">Profile: <a href=\"StructureDefinition-MedicationRequest.html\">MedicationRequest</a></p></div><p><b>status</b>: active</p><p><b>intent</b>: order</p><p><b>medication</b>: Paracetemol 500mg Oral Tab <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> ()</span></p><p><b>subject</b>: <a href=\"#Patient_1\">See above (Patient/1: ABC)</a></p><p><b>authoredOn</b>: 2020-07-09</p><p><b>requester</b>: <a href=\"#Practitioner_1\">See above (Practitioner/1: Dr. DEF)</a></p><p><b>reasonCode</b>: Ross river fever <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#602001)</span></p><p><b>reasonReference</b>: <a href=\"#Condition_1\">See above (Condition/1)</a></p><h3>DosageInstructions</h3><table class=\"grid\"><tr><td style=\"display: none\">-</td><td><b>Text</b></td></tr><tr><td style=\"display: none\">*</td><td>Take two tablets orally with or after meal once a day</td></tr></table></div>");
		medicationRequest.setStatus(MedicationRequestStatus.ACTIVE);
		medicationRequest.setIntent(MedicationRequestIntent.ORDER);
		medicationRequest.setMedication(new CodeableConcept().setText("Paracetemol 500mg Oral Tab"));
		medicationRequest.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe").setDisplay("ABC"));
		medicationRequest.setAuthoredOnElement(new DateTimeType("2020-07-09"));
		medicationRequest
				.setRequester(new Reference().setReference("urn:uuid:3bc96820-c7c9-4f59-900d-6b0ed1fa558e").setDisplay("Dr DEF"));
		medicationRequest.getReasonCode()
				.add(new CodeableConcept(new Coding("http://snomed.info/sct", "789400009", "Ross River disease")));
		medicationRequest.getReasonReference().add(new Reference().setReference("urn:uuid:46e5d4b0-0a3b-487d-97ba-42a479e8b041"));
		medicationRequest
				.addDosageInstruction(new Dosage().setText("Take two tablets orally with or after meal once a day"));
		return medicationRequest;
	}

    // Populate Medication Request Resource Checked
	public static MedicationRequest populateThirdMedicationRequestResource() {
		MedicationRequest medicationRequest = new MedicationRequest();
		medicationRequest.setId("0868a5bc-5691-426c-931c-f085788355ed");
		medicationRequest.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/MedicationRequest");
		medicationRequest.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Generated Narrative: MedicationRequest</b><a name=\"1\"> </a></p><div style=\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\"><p style=\"margin-bottom: 0px\">Resource MedicationRequest &quot;1&quot; </p><p style=\"margin-bottom: 0px\">Profile: <a href=\"StructureDefinition-MedicationRequest.html\">MedicationRequest</a></p></div><p><b>status</b>: active</p><p><b>intent</b>: order</p><p><b>medication</b>: Neomycin 5 mg/g cutaneous ointment <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#353231006)</span></p><p><b>subject</b>: <a href=\"#Patient_1\">See above (Patient/1: ABC)</a></p><p><b>authoredOn</b>: 2020-07-09</p><p><b>requester</b>: <a href=\"#Practitioner_1\">See above (Practitioner/1: Dr. DEF)</a></p><p><b>reasonReference</b>: <a href=\"#Condition_1\">See above (urn:uuid:2462444a-d494-4380-83c7-790d40c1d36a)</a></p><h3>DosageInstructions</h3><table class=\"grid\"><tr><td style=\"display: none\">-</td><td><b>AdditionalInstruction</b></td><td><b>Route</b></td></tr><tr><td style=\"display: none\">*</td><td>Twice a day <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#229799001)</span></td><td>Topical route <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#6064005)</span></td></tr></table></div>");
		medicationRequest.setStatus(MedicationRequestStatus.ACTIVE);
		medicationRequest.setIntent(MedicationRequestIntent.ORDER);
		medicationRequest.setMedication(new CodeableConcept(new Coding("http://snomed.info/sct", "353231006", "Neomycin 5 mg/g cutaneous ointment")).setText("Neomycin 5 mg/g cutaneous ointment"));
		medicationRequest.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe").setDisplay("ABC"));
		medicationRequest.setAuthoredOnElement(new DateTimeType("2020-07-09"));
		medicationRequest
				.setRequester(new Reference().setReference("urn:uuid:3bc96820-c7c9-4f59-900d-6b0ed1fa558e").setDisplay("Dr DEF"));
		medicationRequest.getReasonReference().add(new Reference().setReference("urn:uuid:2462444a-d494-4380-83c7-790d40c1d36a"));
		medicationRequest
				.addDosageInstruction(new Dosage().addAdditionalInstruction(new CodeableConcept(new Coding("http://snomed.info/sct", "229799001", "Twice a day"))).setRoute(new CodeableConcept(new Coding("http://snomed.info/sct", "6064005", "Topical route"))));
		return medicationRequest;
	}

	 // Populate Medication Request Resource Checked
	public static MedicationRequest populateFourMedicationRequestResource() {
		MedicationRequest medicationRequest = new MedicationRequest();
		medicationRequest.setId("0868a5bc-5691-426c-931c-f085788355ed");
		medicationRequest.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/MedicationRequest");
		medicationRequest.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString( "<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Generated Narrative: MedicationRequest</b><a name=\"1\"> </a></p><div style=\"display: inline-block; background-color: #d9e0e7; padding: 6px; margin: 4px; border: 1px solid #8da1b4; border-radius: 5px; line-height: 60%\"><p style=\"margin-bottom: 0px\">Resource MedicationRequest &quot;1&quot; </p><p style=\"margin-bottom: 0px\">Profile: <a href=\"StructureDefinition-MedicationRequest.html\">MedicationRequest</a></p></div><p><b>status</b>: active</p><p><b>intent</b>: order</p><p><b>medication</b>: Nitroglycerin 5 mg buccal tablet <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#765082007)</span></p><p><b>subject</b>: <a href=\"#Patient_1\">See above (Patient/1: ABC)</a></p><p><b>authoredOn</b>: 2020-07-09</p><p><b>requester</b>: <a href=\"#Practitioner_1\">See above (Practitioner/1: DEF)</a></p><p><b>reasonReference</b>: <a href=\"#Condition_2\">See above (Condition/2)</a></p><h3>DosageInstructions</h3><table class=\"grid\"><tr><td style=\"display: none\">-</td><td><b>Text</b></td><td><b>AdditionalInstruction</b></td><td><b>Timing</b></td><td><b>Route</b></td><td><b>Method</b></td></tr><tr><td style=\"display: none\">*</td><td>One tablet at once</td><td>With or after food <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#311504000)</span></td><td>Once per 1 days</td><td>Oral Route <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#26643006)</span></td><td>Swallow <span style=\"background: LightGoldenRodYellow; margin: 4px; border: 1px solid khaki\"> (<a href=\"https://browser.ihtsdotools.org/\">SNOMED CT</a>#421521009)</span></td></tr></table></div>");
		medicationRequest.setStatus(MedicationRequestStatus.ACTIVE);
		medicationRequest.setIntent(MedicationRequestIntent.ORDER);
		medicationRequest.setMedication(new CodeableConcept(new Coding("http://snomed.info/sct", "765082007", "Nitroglycerin 5 mg buccal tablet")).setText("Nitroglycerin 5 mg buccal tablet"));
		medicationRequest.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe").setDisplay("ABC"));
		medicationRequest.setAuthoredOnElement(new DateTimeType("2020-07-09"));
		medicationRequest
				.setRequester(new Reference().setReference("urn:uuid:3bc96820-c7c9-4f59-900d-6b0ed1fa558e").setDisplay("Dr DEF"));
		medicationRequest.getReasonReference().add(new Reference().setReference("urn:uuid:2462444a-d494-4380-83c7-790d40c1d36a"));
		medicationRequest
				.addDosageInstruction(new Dosage().setText("One tablet at once").addAdditionalInstruction(new CodeableConcept(new Coding("http://snomed.info/sct", "311504000", "With or after food"))).setRoute(new CodeableConcept(new Coding("http://snomed.info/sct", "26643006", "Oral Route"))).setTiming(new Timing().setRepeat(new TimingRepeatComponent().setFrequency(1).setPeriod(1).setPeriodUnit(UnitsOfTime.D))).setMethod(new CodeableConcept(new Coding("http://snomed.info/sct", "421521009", "Swallow"))));
		return medicationRequest;
	}


	// Populate Medication Statement Resource Checked
	public static MedicationStatement populateMedicationStatementResource() {
		MedicationStatement medicationStatement = new MedicationStatement();
		medicationStatement.setId("abbaf384-f26b-4655-9a7d-6b9d467aabc7");
		medicationStatement.getMeta()
				.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/MedicationStatement");
		medicationStatement.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\">Atenolol 500 microgram/mL solution for injection</div>");
		medicationStatement.setStatus(MedicationStatementStatus.COMPLETED);
		medicationStatement.setMedication(new CodeableConcept(
				new Coding("http://snomed.info/sct", "134463001", "Telmisartan 20 mg oral tablet")));
		medicationStatement.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		medicationStatement.setDateAssertedElement(new DateTimeType("2020-02-02T14:58:58.181+05:30"));
		return medicationStatement;
	}

	// Populate Observation Resource
	public static Observation populateObservationResource() {
		Observation observation = new Observation();
		observation.setId("Observation-01");
		observation.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Observation");
		observation.setStatus(ObservationStatus.FINAL);
		observation.setCode(new CodeableConcept(
				new Coding("http://loinc.org", "35200-5", "Cholesterol [Moles/​volume] in Serum or Plasma"))
				.setText("Cholesterol"));
		observation.setValue(new Quantity().setValueElement(new DecimalType("6.3")).setCode("258813002")
				.setUnit("mmol/L").setSystem("http://snomed.info/sct"));
		observation.getReferenceRange().add(
				new ObservationReferenceRangeComponent().setHigh(new Quantity().setValueElement(new DecimalType("6.3"))
						.setCode("258813002").setUnit("mmol/L").setSystem("http://snomed.info/sct")));
		observation.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		return observation;
	}

	// Populate Observation/Cholesterol Resource
	public static Observation populateCholesterolObservationResource() {
		Observation observation = new Observation();
		observation.setId("46923585-2f65-4deb-8c51-56b2a5b5c14b");
		observation.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Observation");
		observation.setStatus(ObservationStatus.FINAL);
		observation.setCode(new CodeableConcept(
				new Coding("http://loinc.org", "2093-3", "Cholesterol [Mass/volume] in Serum or Plasma"))
				.setText("Cholesterol [Mass/volume] in Serum or Plasma"));
		observation.setValue(new Quantity().setValueElement(new DecimalType("156")).setCode("258797006")
				.setUnit("mg/dL").setSystem("http://snomed.info/sct"));
		observation.getReferenceRange().add(
				new ObservationReferenceRangeComponent().setLow(new Quantity().setValueElement(new DecimalType("200.00"))
						.setCode("258797006").setUnit("mg/dL").setSystem("http://snomed.info/sct")));
		observation.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		observation.addPerformer().setReference("urn:uuid:6a7ce76d-8d49-4395-9a15-41abefa65430");
		return observation;
	}

	// Populate Observation/Triglyceride Resource
	public static Observation populateTriglycerideObservationResource() {
		Observation observation = new Observation();
		observation.setId("969f3ed4-f0e2-48ff-ae34-e72b6a3ae9d6");
		observation.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Observation");
		observation.setStatus(ObservationStatus.FINAL);
		observation.setCode(new CodeableConcept(
				new Coding("http://loinc.org", "2571-8", "Triglyceride [Mass/volume] in Serum or Plasma"))
				.setText("Triglycerides, serum by Enzymatic method"));
		observation.setValue(new Quantity().setValueElement(new DecimalType("146.00")).setCode("258797006")
				.setUnit("mg/dL").setSystem("http://snomed.info/sct"));
		observation.getReferenceRange().add(
				new ObservationReferenceRangeComponent().setLow(new Quantity().setValueElement(new DecimalType("150.00"))
						.setCode("258797006").setUnit("mg/dL").setSystem("http://snomed.info/sct")));
		observation.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		observation.addPerformer().setReference("urn:uuid:6a7ce76d-8d49-4395-9a15-41abefa65430");
		return observation;
	}
	
	    // Populate Observation/Cholesterol In HDL Resource
		public static Observation populateCholesterolInHDLObservationResource() {
			Observation observation = new Observation();
			observation.setId("bdada6cb-c93e-42a8-b7fe-0a2490800cfd");
			observation.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Observation");
			observation.setStatus(ObservationStatus.FINAL);
			observation.setCode(new CodeableConcept(
					new Coding("http://loinc.org", "2085-9", "Cholesterol in HDL [Mass/volume] in Serum or Plasma"))
					.setText("Cholesterol in HDL [Mass/volume] in Serum or Plasma"));
			observation.setValue(new Quantity().setValueElement(new DecimalType("45.00")).setCode("258797006")
					.setUnit("mg/dL").setSystem("http://snomed.info/sct"));
			observation.getReferenceRange().add(
					new ObservationReferenceRangeComponent().setHigh(new Quantity().setValueElement(new DecimalType("40.00"))
							.setCode("258797006").setUnit("mg/dL").setSystem("http://snomed.info/sct")));
			observation.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
			observation.addPerformer().setReference("urn:uuid:6a7ce76d-8d49-4395-9a15-41abefa65430");
			return observation;
		}

	// Populate Organization Resource Checked
	public static Organization populateOrganizationResource() {
		Organization organization = new Organization();
		organization.setId("6a7ce76d-8d49-4395-9a15-41abefa65430");
		organization.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Organization");
		organization.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\">XYZ Lab Pvt.Ltd. ph: +91 243 2634 1234, email:<a href=\"mailto:contact@labs.xyz.org\">contact@labs.xyz.org</a></div>");
		organization.getIdentifier()
				.add(new Identifier()
						.setType(new CodeableConcept(
								new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "PRN", "Provider number")))
						.setSystem("https://facility.ndhm.gov.in").setValue("4567878"));
		organization.setName("XYZ Lab Pvt.Ltd");
		List<ContactPoint> list = new ArrayList<ContactPoint>();
		ContactPoint contact1 = new ContactPoint();
		contact1.setSystem(ContactPointSystem.PHONE).setValue("+91 243 2634 1234").setUse(ContactPointUse.WORK);
		ContactPoint contact2 = new ContactPoint();
		contact2.setSystem(ContactPointSystem.EMAIL).setValue("contact@labs.xyz.org").setUse(ContactPointUse.WORK);
		list.add(contact1);
		list.add(contact2);
		organization.setTelecom(list);
		return organization;
	}

	// Populate Organization Resource Checked
	public static Organization populateSecondOrganizationResource() {
		Organization organization = new Organization();
		organization.setId("364cad65-3038-40df-a8c6-2139d87e1b19");
		organization.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Organization");
		organization.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\">UVW Hospital. ph: +91 273 2139 3632, email:<a href=\"mailto:contact@facility.uvw.org\">contact@facility.uvw.org</a></div>");
		organization.getIdentifier()
				.add(new Identifier()
						.setType(new CodeableConcept(
								new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "PRN", "Provider number")))
						.setSystem("https://facility.ndhm.gov.in").setValue("4567823"));
		organization.setName("UVW Hospital");
		List<ContactPoint> list = new ArrayList<ContactPoint>();
		ContactPoint contact1 = new ContactPoint();
		contact1.setSystem(ContactPointSystem.PHONE).setValue("+91 243 2634 3632").setUse(ContactPointUse.WORK);
		ContactPoint contact2 = new ContactPoint();
		contact2.setSystem(ContactPointSystem.EMAIL).setValue("contact@facility.uvw.org").setUse(ContactPointUse.WORK);
		list.add(contact1);
		list.add(contact2);
		organization.setTelecom(list);
		return organization;
	}

	// Populate Practitioner Role Resource
	public static PractitionerRole populatePractitionerRoleResource() {
		PractitionerRole practitionerRole = new PractitionerRole();
		practitionerRole.setId("PractitionerRole-01");
		practitionerRole.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/PractitionerRole");
		practitionerRole.getIdentifier()
				.add(new Identifier()
						.setType(new CodeableConcept(
								new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "EI", "Employee number")))
						.setValue("23").setSystem("http://www.ndhm.in/practitioners"));
		practitionerRole.setActive(true);
		practitionerRole.setPeriod(new Period().setStartElement(new DateTimeType("2012-01-01"))
				.setEndElement(new DateTimeType("2012-03-31")));
		practitionerRole
				.setPractitioner(new Reference().setReference("urn:uuid:3bc96820-c7c9-4f59-900d-6b0ed1fa558e").setDisplay("Dr DEF"));
		practitionerRole.setOrganization(new Reference().setReference("urn:uuid:6a7ce76d-8d49-4395-9a15-41abefa65430"));
		practitionerRole.getCode()
				.add(new CodeableConcept(new Coding("http://snomed.info/sct", "85733003", "General pathologist")));
		practitionerRole.getSpecialty().add(
				new CodeableConcept(new Coding("http://snomed.info/sct", "408443003", "General Medical Practice")));
		practitionerRole.addAvailableTime(new PractitionerRoleAvailableTimeComponent().setAllDay(true)
				.setAvailableStartTimeElement(new TimeType("09:00:00"))
				.setAvailableEndTimeElement(new TimeType("12:00:00")));
		return practitionerRole;
	}

	// Populate Procedure Resource checked
	public static Procedure populateProcedureResource() {
		Procedure procedure = new Procedure();
		procedure.setId("67e43c75-eadc-4d1c-b8c9-bca933811b58");
		procedure.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Procedure");
		procedure.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\">Assessment of diabetic foot ulcer</div>");
		procedure.setStatus(ProcedureStatus.COMPLETED);
		procedure.setCode(new CodeableConcept(
				new Coding("http://snomed.info/sct", "713130008", "Assessment of diabetic foot ulcer"))
				.setText("Assessment of diabetic foot ulcer"));
		procedure.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		procedure.setPerformed(new DateTimeType("2019-05-12"));
		procedure.getComplication()
				.add(new CodeableConcept(new Coding("http://snomed.info/sct", "394725008", "Diabetes medication review")));
		return procedure;
	}

	// Populate Procedure Resource Checked
	public static Procedure populateSecondProcedureResource() {
		Procedure procedure = new Procedure();
		procedure.setId("90e31223-fddd-4ab8-9fcc-b3cfe101dc6e");
		procedure.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Procedure");
		procedure.setStatus(ProcedureStatus.COMPLETED);
		procedure.setCode(new CodeableConcept(
				new Coding("http://snomed.info/sct", "232717009", "Coronary artery bypass grafting"))
				.setText("Coronary artery bypass grafting"));
		procedure.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		procedure.setPerformed(new DateTimeType("2019-05-12"));
		return procedure;
	}

	// Populate Procedure Resource Checked
    public static Procedure populateThirdProcedureResource() {
		Procedure procedure = new Procedure();
		procedure.setId("67e43c75-eadc-4d1c-b8c9-bca933811b58");
		procedure.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Procedure");
		procedure.setStatus(ProcedureStatus.COMPLETED);
		procedure.setCode(new CodeableConcept(
				new Coding("http://snomed.info/sct", "36969009", "Placement of stent in coronary artery"))
				.setText("Placement of stent in coronary artery"));
		procedure.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		procedure.setPerformed(new DateTimeType("2019-05-12"));
		procedure.addComplication(new CodeableConcept(new Coding("http://snomed.info/sct", "131148009", "Bleeding")));
		return procedure;
	}


	// Populate Service Request Resource Checked
	public static ServiceRequest populateServiceRequestResource() {
		ServiceRequest serviceRequest = new ServiceRequest();
		serviceRequest.setId("a7a398a8-ffbe-4636-906f-bbe03399e3ba");
		serviceRequest.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/ServiceRequest");
		serviceRequest.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\"><p><b>Generated Narrative with Details</b></p><p><b>id</b>: 1</p><p><b>status</b>: active</p><p><b>intent</b>: original-order</p><p><b>subject</b>: <a>ABC</a></p><p><b>occurrence</b>: 08/07/2020 9:33:27 AM</p><p><b>requester</b>: <a>Dr PQR</a></p></div>");
		serviceRequest.setStatus(ServiceRequestStatus.ACTIVE);
		serviceRequest.setIntent(ServiceRequestIntent.ORIGINALORDER);
		serviceRequest.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		serviceRequest.setOccurrence(new DateTimeType("2020-07-08T09:33:27+07:00"));
		serviceRequest.setRequester(new Reference().setReference("urn:uuid:947c60c9-20f6-41d8-9fce-b5da822a3087").setDisplay("Dr PQR"));
		return serviceRequest;
	}

	// Populate Service Request Resource Checked
    public static ServiceRequest populateSecondServiceRequestResource() {
		ServiceRequest serviceRequest = new ServiceRequest();
		serviceRequest.setId("a7a398a8-ffbe-4636-906f-bbe03399e3ba");
		serviceRequest.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/ServiceRequest");
		serviceRequest.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\">Service Request for fasting lipid profile</div>");
		serviceRequest.setStatus(ServiceRequestStatus.ACTIVE);
		serviceRequest.setIntent(ServiceRequestIntent.ORDER);
		serviceRequest.addCategory(new CodeableConcept(new Coding("http://snomed.info/sct", "108252007", "Laboratory procedure")));
		serviceRequest.setCode(new CodeableConcept(new Coding("http://snomed.info/sct", "252150008", "Fasting lipid profile")).setText("Fasting lipid profile"));
		serviceRequest.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		serviceRequest.setOccurrence(new DateTimeType("2020-07-08T09:33:27+07:00"));
		serviceRequest.setRequester(new Reference().setReference("urn:uuid:3bc96820-c7c9-4f59-900d-6b0ed1fa558e").setDisplay("Dr DEF"));
		return serviceRequest;
	}

	// Populate Service Request Resource Checked
	public static ServiceRequest populateThirdServiceRequestResource() {
		ServiceRequest serviceRequest = new ServiceRequest();
		serviceRequest.setId("a7a398a8-ffbe-4636-906f-bbe03399e3ba");
		serviceRequest.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/ServiceRequest");
		serviceRequest.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\"><p><b>Generated Narrative with Details</b></p><p><b>id</b>: 1</p><p><b>status</b>: active</p><p><b>intent</b>: original-order</p><p><b>subject</b>: <a>ABC</a></p><p><b>occurrence</b>: 08/07/2020 9:33:27 AM</p><p><b>requester</b>: <a>Dr PQR</a></p></div>");
		serviceRequest.setStatus(ServiceRequestStatus.ACTIVE);
		serviceRequest.setIntent(ServiceRequestIntent.ORIGINALORDER);
		serviceRequest.setCode(new CodeableConcept(new Coding("http://snomed.info/sct", "16254007", "Lipid Panel")));
		serviceRequest.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		serviceRequest.setOccurrence(new DateTimeType("2020-07-08T09:33:27+07:00"));
		serviceRequest.setRequester(new Reference().setReference("urn:uuid:947c60c9-20f6-41d8-9fce-b5da822a3087").setDisplay("Dr PQR"));
		return serviceRequest;
	}


	// Populate Specimen Resource Checked
	public static Specimen populateSpecimenResource() {
		Specimen specimen = new Specimen();
		specimen.setId("e0702f22-e242-47a1-81aa-3cafe82669e2");
		specimen.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Specimen");
		specimen.setType(new CodeableConcept(new Coding("http://snomed.info/sct", "119364003", "Serum specimen")));
		specimen.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		specimen.setReceivedTimeElement(new DateTimeType("2020-07-08T06:40:17Z"));
		specimen.setCollection(
				new SpecimenCollectionComponent().setCollected(new DateTimeType("2020-07-08T06:40:17Z")));
		return specimen;
	}

	// Populate urn:uuid:a51edaff-4f28-4597-b0c1-7a4d153b7ce6 Resource Checked
	public static Observation populateRespitaryRateResource() {
		Observation observation = new Observation();
		observation.setId("a51edaff-4f28-4597-b0c1-7a4d153b7ce6");
		observation.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationVitalSigns");
		observation.getText().setStatus(NarrativeStatus.GENERATED);
		observation.getText().setDivAsString(
			"<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\"><p><b>Generated Narrative with Details</b></p><p><b>id</b>: respiratory-rate</p><p><b>meta</b>: </p><p><b>status</b>: final</p><p><b>category</b>: Vital Signs <span>(Details : {http://terminology.hl7.org/CodeSystem/observation-category code 'vital-signs' = 'Vital Signs', given as 'Vital Signs'})</span></p><p><b>code</b>: Respiratory rate <span>(Details : {LOINC code '9279-1' = 'Respiratory rate', given as 'Respiratory rate'})</span></p><p><b>subject</b>: <a>Patient/1</a></p><p><b>effective</b>: 29/09/2020</p><p><b>value</b>: 26 breaths/minute<span> (Details: UCUM code /min = '/min')</span></p></div>");
		observation.setStatus(ObservationStatus.FINAL);
		observation.addCategory(new CodeableConcept(new Coding(
				"http://terminology.hl7.org/CodeSystem/observation-category", "vital-signs", "Vital Signs")));
		observation.addCategory().setText("Vital Signs");
		observation.setCode(new CodeableConcept(new Coding("http://loinc.org", "9279-1", "Respiratory rate"))
				.setText("Respiratory rate"));
		observation.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		observation.setValue(new Quantity().setValue(26).setUnit("breaths/minute")
				.setSystem("http://unitsofmeasure.org").setCode("/min"));
		observation.setEffective(new DateTimeType("2020-09-29"));
		return observation;
	}

	// Populate urn:uuid:9d7e3f3a-c3e7-47e1-b46f-0a816aeea86d Resource Checked
	public static Observation populateHeartRateResource() {
		Observation observation = new Observation();
		observation.setId("9d7e3f3a-c3e7-47e1-b46f-0a816aeea86d");
		observation.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationVitalSigns");
		observation.getText().setStatus(NarrativeStatus.GENERATED);
		observation.getText().setDivAsString(
			"<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\"><p><b>Generated Narrative with Details</b></p><p><b>id</b>: heart-rate</p><p><b>meta</b>: </p><p><b>status</b>: final</p><p><b>category</b>: Vital Signs <span>(Details : {http://terminology.hl7.org/CodeSystem/observation-category code 'vital-signs' = 'Vital Signs', given as 'Vital Signs'})</span></p><p><b>code</b>: Heart rate <span>(Details : {LOINC code '8867-4' = 'Heart rate', given as 'Heart rate'})</span></p><p><b>subject</b>: <a>Patient/1</a></p><p><b>effective</b>: 29/09/2020</p><p><b>value</b>: 44 beats/minute<span> (Details: UCUM code /min = '/min')</span></p></div>");
		observation.setStatus(ObservationStatus.FINAL);
		observation.addCategory(new CodeableConcept(new Coding(
				"http://terminology.hl7.org/CodeSystem/observation-category", "vital-signs", "Vital Signs")));
		observation.addCategory().setText("Vital Signs");
		observation.setCode(
				new CodeableConcept(new Coding("http://loinc.org", "8867-4", "Heart rate")).setText("Heart rate"));
		observation.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		observation.setEffective(new DateTimeType("2020-09-29"));
		observation.setValue(new Quantity().setValue(44).setUnit("beats/minute").setSystem("http://unitsofmeasure.org")
				.setCode("/min"));
		return observation;
	}

	// Populate urn:uuid:31259ff7-7bc5-48d5-bf4f-51cc4ee01c6b Resource Checked
	public static Observation populateOxygenSaturationResource() {
		Observation observation = new Observation();
		observation.setId("31259ff7-7bc5-48d5-bf4f-51cc4ee01c6b");
		observation.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationVitalSigns");
		observation.getText().setStatus(NarrativeStatus.GENERATED);
		observation.getText().setDivAsString(
			"<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\"><p><b>Generated Narrative with Details</b></p><p><b>id</b>: oxygen-saturation</p><p><b>meta</b>: </p><p><b>identifier</b>: o1223435-10</p><p><b>status</b>: final</p><p><b>category</b>: Vital Signs <span>(Details : {http://terminology.hl7.org/CodeSystem/observation-category code 'vital-signs' = 'Vital Signs', given as 'Vital Signs'})</span></p><p><b>code</b>: Oxygen saturation in Arterial blood <span>(Details : {LOINC code '2708-6' = 'Oxygen saturation in Arterial blood', given as 'Oxygen saturation in Arterial blood'}; )</span></p><p><b>subject</b>: <a>Patient/1</a></p><p><b>effective</b>: 29/09/2020 9:30:10 AM</p><p><b>value</b>: 95 %<span> (Details: UCUM code % = '%')</span></p><p><b>interpretation</b>: Normal (applies to non-numeric results) <span>(Details : {http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation code 'N' = 'Normal', given as 'Normal'})</span></p><p><b>device</b>: <a>DeviceMetric/example</a></p><h3>ReferenceRanges</h3><table><tr><td>-</td><td><b>Low</b></td><td><b>High</b></td></tr><tr><td>*</td><td>90 %<span> (Details: UCUM code % = '%')</span></td><td>99 %<span> (Details: UCUM code % = '%')</span></td></tr></table></div>");
		observation.addIdentifier(
				new Identifier().setSystem("http://goodcare.org/observation/id").setValue("o1223435-10"));
		observation.setStatus(ObservationStatus.FINAL);
		observation.addCategory(new CodeableConcept(new Coding(
				"http://terminology.hl7.org/CodeSystem/observation-category", "vital-signs", "Vital Signs")));
		observation.addCategory().setText("Vital Signs");
		observation.setCode(
				new CodeableConcept(new Coding("http://loinc.org", "2708-6", "Oxygen saturation in Arterial blood")));
		observation.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		observation.setEffective(new DateTimeType("2020-09-29T09:30:10+01:00"));
		observation
				.setValue(new Quantity().setValue(95).setUnit("%").setSystem("http://unitsofmeasure.org").setCode("%"));
		observation.addInterpretation(new CodeableConcept(
				new Coding("http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation", "N", "Normal"))
				.setText("Normal (applies to non-numeric results)"));
		observation.setDevice(new Reference().setReference("DeviceMetric/example"));
		observation.addReferenceRange(new ObservationReferenceRangeComponent()
				.setLow(new Quantity().setValue(90).setUnit("%").setSystem("http://unitsofmeasure.org").setCode("%"))
				.setHigh(new Quantity().setValue(95).setUnit("%").setSystem("http://unitsofmeasure.org").setCode("%")));
		return observation;
	}

	// Populate "Observation/body-temperature Resource Checked
	public static Observation populateBodyTemperatureResource() {
		Observation observation = new Observation();
		observation.setId("baa5f4ae-c86a-40b4-93f6-460c01fb944b");
		observation.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationVitalSigns");
		observation.getText().setStatus(NarrativeStatus.GENERATED);
		observation.getText().setDivAsString(
			"<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\"><p><b>Generated Narrative with Details</b></p><p><b>id</b>: body-temperature</p><p><b>meta</b>: </p><p><b>status</b>: final</p><p><b>code</b>: Body surface temperature <span>(Details : {LOINC code '61008-9' = 'Body surface temperature', given as 'Body surface temperature'})</span></p><p><b>subject</b>: <a>Patient/1</a></p><p><b>effective</b>: 2021-03-09</p><p><b>value</b>: 36.5 Cel</p></div>");
		observation.setStatus(ObservationStatus.FINAL);
		observation.setCode(new CodeableConcept(new Coding("http://loinc.org", "61008-9", "Body surface temperature"))
				.setText("Body surface temperature"));
		observation.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		observation.setEffective(new DateTimeType("2020-09-29"));
		observation.setValue(new Quantity().setValue(36.5).setUnit("Cel").setSystem("http://unitsofmeasure.org")
				.setCode("{Cel or degF}"));

		return observation;
	}

	// Populate urn:uuid:7d4e2fa9-bb97-4d0a-9d95-042048f2708b Resource Checked
	public static Observation populateBodyHeightResource() {
		Observation observation = new Observation();
		observation.setId("7d4e2fa9-bb97-4d0a-9d95-042048f2708b");
		observation.getMeta()
				.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationBodyMeasurement");
		observation.getText().setStatus(NarrativeStatus.GENERATED);
		observation.getText().setDivAsString(
			"<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\"><p><b>Generated Narrative with Details</b></p><p><b>id</b>: body-height</p><p><b>meta</b>: </p><p><b>status</b>: final</p><p><b>category</b>: Vital Signs <span>(Details : {http://terminology.hl7.org/CodeSystem/observation-category code 'vital-signs' = 'Vital Signs', given as 'Vital Signs'})</span></p><p><b>code</b>: Body height <span>(Details : {LOINC code '8302-2' = 'Body height', given as 'Body height'})</span></p><p><b>subject</b>: <a>Patient/1</a></p><p><b>effective</b>: 29/09/2020</p><p><b>value</b>: 66.899999999999991 in<span> (Details: UCUM code [in_i] = 'in_i')</span></p></div>");
		observation.setStatus(ObservationStatus.FINAL);
		observation.addCategory(new CodeableConcept(new Coding(
				"http://terminology.hl7.org/CodeSystem/observation-category", "vital-signs", "Vital Signs")));
		observation.addCategory().setText("Vital Signs");
		observation.setCode(
				new CodeableConcept(new Coding("http://loinc.org", "8302-2", "Body height")).setText("Body height"));
		observation.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		observation.setEffective(new DateTimeType("2020-09-29"));
		observation.setValue(new Quantity().setValue(66.89999999999999).setUnit("in")
				.setSystem("http://unitsofmeasure.org").setCode("[in_i]"));

		return observation;
	}

	// Populate urn:uuid:6f41dcd7-dfdb-46ae-bdf2-ddeb1d9d15f2 Resource Checked
	public static Observation populateBodyWeightResource() {
		Observation observation = new Observation();
		observation.setId("6f41dcd7-dfdb-46ae-bdf2-ddeb1d9d15f2");
		observation.getMeta()
				.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationBodyMeasurement");
		observation.getText().setStatus(NarrativeStatus.GENERATED);
		observation.getText().setDivAsString(
			"<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\"><p><b>Generated Narrative with Details</b></p><p><b>id</b>: body-weight</p><p><b>status</b>: final</p><p><b>category</b>: Vital Signs <span>(Details : {http://terminology.hl7.org/CodeSystem/observation-category code 'vital-signs' = 'Vital Signs', given as 'Vital Signs'})</span></p><p><b>code</b>: Body Weight <span>(Details : {LOINC code '29463-7' = 'Body weight', given as 'Body Weight'};)</span></p><p><b>subject</b>: <a>Patient/1</a></p><p><b>effective</b>: 29/09/2020</p><p><b>value</b>: 185 lbs<span> (Details: UCUM code [lb_av] = 'lb_av')</span></p></div>");
		observation.setStatus(ObservationStatus.FINAL);
		observation.addCategory(new CodeableConcept(new Coding(
				"http://terminology.hl7.org/CodeSystem/observation-category", "vital-signs", "Vital Signs")));
		observation.setCode(
				new CodeableConcept(new Coding("http://loinc.org", "29463-7", "Body weight")).setText("Body weight"));
		observation.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		observation.setEffective(new DateTimeType("2020-09-29"));
		observation.setValue(new Quantity().setValue(185).setUnit("lbs").setSystem("http://unitsofmeasure.org")
				.setCode("[lb_av]"));

		return observation;
	}

	// Populate urn:uuid:69a050d0-6a98-42cc-82bc-183f46b9e6da Resource Checked
	public static Observation populateBMIResource() {
		Observation observation = new Observation();
		observation.setId("69a050d0-6a98-42cc-82bc-183f46b9e6da");
		observation.getMeta()
				.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationBodyMeasurement");
		observation.getText().setStatus(NarrativeStatus.GENERATED);
		observation.getText().setDivAsString(
			"<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\"><p><b>Generated Narrative with Details</b></p><p><b>id</b>: bmi</p><p><b>meta</b>: </p><p><b>status</b>: final</p><p><b>category</b>: Vital Signs <span>(Details : {http://terminology.hl7.org/CodeSystem/observation-category code 'vital-signs' = 'Vital Signs', given as 'Vital Signs'})</span></p><p><b>code</b>: BMI <span>(Details : {LOINC code '39156-5' = 'Body mass index (BMI) [Ratio]', given as 'Body mass index (BMI) [Ratio]'})</span></p><p><b>subject</b>: <a>Patient/1</a></p><p><b>effective</b>: 29/09/2020</p><p><b>value</b>: 16.2 kg/m2<span> (Details: UCUM code kg/m2 = 'kg/m2')</span></p></div>");
		observation.setStatus(ObservationStatus.FINAL);
		observation.addCategory(new CodeableConcept(
				new Coding("http://terminology.hl7.org/CodeSystem/observation-category", "vital-signs", "Vital Signs"))
				.setText("vital Signs"));
		observation
				.setCode(new CodeableConcept(new Coding("http://loinc.org", "39156-5", "Body mass index (BMI) [Ratio]"))
						.setText("BMI"));
		observation.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		observation.setEffective(new DateTimeType("2020-09-29"));
		observation.setValue(
				new Quantity().setValue(16.2).setUnit("kg/m2").setSystem("http://unitsofmeasure.org").setCode("kg/m2"));

		return observation;
	}

	// Populate urn:uuid:4a0e0b0d-a941-4000-b944-4346054dc2c2 Resource Checked
	public static Observation populateStepCountResource() {
		Observation observation = new Observation();
		observation.setId("4a0e0b0d-a941-4000-b944-4346054dc2c2");
		observation.getMeta()
				.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationPhysicalActivity");
		observation.getText().setStatus(NarrativeStatus.GENERATED);
		observation.getText().setDivAsString(
			"<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\"><p><b>Narrative with Details</b></p><p><b>id</b>: StepCount</p><p><b>status</b>: final</p><p><b>code</b>: Number of steps in unspecified time Pedometer <span>(Details : LOINC code '55423-8' = 'Number of steps in unspecified time Pedometer', given as 'Number of steps in unspecified time Pedometer')</span></p><p><b>subject</b>: ABC</p><p><b>performer</b>: Dr. DEF, MD</p><p><b>value</b>: 10000 steps</p></div>");
		observation.setStatus(ObservationStatus.FINAL);
		observation.setCode(new CodeableConcept(
				new Coding("http://loinc.org", "55423-8", "Number of steps in unspecified time Pedometer"))
				.setText("Number of steps in unspecified time Pedometer"));
		observation.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		observation.setValue(new Quantity().setValue(10000).setUnit("steps").setSystem("http://unitsofmeasure.org")
				.setCode("{steps}"));

		return observation;
	}

	// Populate "urn:uuid:1d83373a-29cc-44b7-9db3-5afe03d0728d" Resource Checked
	public static Observation populateCaloriesBurnedResource() {
		Observation observation = new Observation();
		observation.setId("1d83373a-29cc-44b7-9db3-5afe03d0728d");
		observation.getMeta()
				.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationPhysicalActivity");
		observation.getText().setStatus(NarrativeStatus.GENERATED);
		observation.getText().setDivAsString(
			"<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\"><p><b>Narrative with Details</b></p><p><b>id</b>: CaloriesBurned</p><p><b>status</b>: final</p><p><b>code</b>: Calories burned <span>(Details : LOINC code '41981-2' = 'Calories burned', given as 'Calories burned')</span></p><p><b>subject</b>: ABC</p><p><b>performer</b>: Dr. DEF, MD</p><p><b>value</b>: 800 kcal<span> (Details: UCUM code kcal = 'kcal')</span></p></div>");
		observation.setStatus(ObservationStatus.FINAL);
		observation.setCode(new CodeableConcept(new Coding("http://loinc.org", "41981-2", "Calories burned"))
				.setText("Calories burned"));
		observation.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		observation.addPerformer(new Reference().setReference("urn:uuid:364cad65-3038-40df-a8c6-2139d87e1b19"));
		observation.setValue(
				new Quantity().setValue(800).setUnit("kcal").setSystem("http://unitsofmeasure.org").setCode("kcal"));

		return observation;
	}

	// Populate "urn:uuid:1ff6a4df-775a-4934-a035-b70e65c1b0d9" Resource Checked
	public static Observation populateSleepDurationResource() {
		Observation observation = new Observation();
		observation.setId("1ff6a4df-775a-4934-a035-b70e65c1b0d9");
		observation.getMeta()
				.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationPhysicalActivity");
		observation.getText().setStatus(NarrativeStatus.GENERATED);
		observation.getText().setDivAsString(
			"<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\"><p><b>Narrative with Details</b></p><p><b>id</b>: SleepDuration</p><p><b>status</b>: final</p><p><b>code</b>: Number of steps in unspecified time Pedometer <span>(Details : LOINC code '93832-4' = 'Sleep duration', given as 'Sleep duration')</span></p><p><b>subject</b>: ABC</p><p><b>value</b>: 8 h<span> (Details: UCUM code h = 'hours')</span></p></div>");
		observation.setStatus(ObservationStatus.FINAL);
		observation.setCode(new CodeableConcept(new Coding("http://loinc.org", "93832-4", "Sleep duration"))
				.setText("Sleep duration"));
		observation.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		observation
				.setValue(new Quantity().setValue(8).setUnit("h").setSystem("http://unitsofmeasure.org").setCode("h"));

		return observation;
	}

	// Populate "urn:uuid:566aff6a-b2f7-4407-8260-a2a961c50126" Resource Checked
	public static Observation populateBodyFatMassResource() {
		Observation observation = new Observation();
		observation.setId("566aff6a-b2f7-4407-8260-a2a961c50126");
		observation.getMeta()
				.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationGeneralAssessment");
		observation.getText().setStatus(NarrativeStatus.GENERATED);
		observation.getText().setDivAsString(
			"<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\"><p><b>Narrative with Details</b></p><p><b>id</b>: BodyFatMass</p><p><b>status</b>: final</p><p><b>code</b>: Body fat [Mass] Calculated <span>(Details : LOINC code '73708-0' = 'Body fat [Mass] Calculated', given as 'Body fat [Mass] Calculated')</span></p><p><b>subject</b>: ABC</p><p><b>performer</b>: Dr. DEF, MD</p><p><b>value</b>: 11 kg<span> (Details: UCUM code kg = 'kg')</span></p></div>");
		observation.setStatus(ObservationStatus.FINAL);
		observation.setCode(new CodeableConcept(new Coding("http://loinc.org", "73708-0", "Body fat [Mass] Calculated"))
				.setText("Body fat [Mass] Calculated"));
		observation.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		observation.addPerformer(new Reference().setReference("urn:uuid:6a7ce76d-8d49-4395-9a15-41abefa65430"));
		observation.setValue(
				new Quantity().setValue(11).setUnit("kg").setSystem("http://unitsofmeasure.org").setCode("kg"));

		return observation;
	}

	// Populate "urn:uuid:db29db74-e263-42ef-b75e-2915db52d5a6" Resource Checked
	public static Observation populateBloodGlucoseResource() {
		Observation observation = new Observation();
		observation.setId("db29db74-e263-42ef-b75e-2915db52d5a6");
		observation.getMeta()
				.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationGeneralAssessment");
		observation.getText().setStatus(NarrativeStatus.GENERATED);
		observation.getText().setDivAsString(
			"<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\"><p><b>Narrative with Details</b></p><p><b>id</b>: BloodGlucose</p><p><b>status</b>: final</p><p><b>code</b>: Glucose [Mass/volume] in Blood <span>(Details : LOINC code '2339-0' = 'Glucose [Mass/volume] in Blood', given as 'Glucose [Mass/volume] in Blood')</span></p><p><b>subject</b>: ABC</p><p><b>value</b>: 142 mg/dL<span> (Details: UCUM code mg/dL = 'mg/dL')</span></p></div>");
		observation.setStatus(ObservationStatus.FINAL);
		observation
				.setCode(new CodeableConcept(new Coding("http://loinc.org", "2339-0", "Glucose [Mass/volume] in Blood"))
						.setText("Glucose [Mass/volume] in Blood"));
		observation.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		observation.setValue(
				new Quantity().setValue(142).setUnit("mg/dL").setSystem("http://unitsofmeasure.org").setCode("mg/dL"));

		return observation;
	}

	// Populate "urn:uuid:0797230b-ee0d-4d64-bb26-81e39dd82a0a" Resource Checked
	public static Observation populateFluidIntakeResource() {
		Observation observation = new Observation();
		observation.setId("0797230b-ee0d-4d64-bb26-81e39dd82a0a");
		observation.getMeta()
				.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationGeneralAssessment");
		observation.getText().setStatus(NarrativeStatus.GENERATED);
		observation.getText().setDivAsString(
			"<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\"><p><b>Narrative with Details</b></p><p><b>id</b>: FluidIntake</p><p><b>status</b>: final</p><p><b>code</b>: Fluid intake oral Estimated <span>(Details : LOINC code '8999-5' = 'Fluid intake oral Estimated', given as 'Fluid intake oral Estimated')</span></p><p><b>subject</b>: ABC</p><p><b>value</b>: 3 Litres</p></div>");
		observation.setStatus(ObservationStatus.FINAL);
		observation.setCode(new CodeableConcept(new Coding("http://loinc.org", "8999-5", "Fluid intake oral Estimated"))
				.setText("Fluid intake oral Estimated"));
		observation.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		observation.setValue(new Quantity().setValue(3).setUnit("Litres").setSystem("http://unitsofmeasure.org")
				.setCode("{mL or Litres}"));

		return observation;
	}

	// Populate "urn:uuid:fb589f2c-a3cd-48fd-86c8-9570aa8f7545" Resource Checked
	public static Observation populateCalorieIntakeResource() {
		Observation observation = new Observation();
		observation.setId("fb589f2c-a3cd-48fd-86c8-9570aa8f7545");
		observation.getMeta()
				.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationGeneralAssessment");
		observation.getText().setStatus(NarrativeStatus.GENERATED);
		observation.getText().setDivAsString(
			"<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\"><p><b>Narrative with Details</b></p><p><b>id</b>: CalorieIntake</p><p><b>status</b>: final</p><p><b>code</b>: Calorie intake total <span>(Details : LOINC code '9052-2' = 'Calorie intake total', given as 'Calorie intake total')</span></p><p><b>subject</b>: ABC</p><p><b>value</b>: 1750 kcal<span> (Details: UCUM code kcal = 'kcal')</span></p></div>");
		observation.setStatus(ObservationStatus.FINAL);
		observation.setCode(new CodeableConcept(new Coding("http://loinc.org", "9052-2", "Calorie intake total"))
				.setText("Calorie intake total"));
		observation.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		observation.setValue(
				new Quantity().setValue(1750).setUnit("kcal").setSystem("http://unitsofmeasure.org").setCode("kcal"));

		return observation;
	}

	// Populate "urn:uuid:230ade51-9daf-4467-895b-6a32a97c484c" Resource Checked
	public static Observation populateAgeOfMenarcheResource() {
		Observation observation = new Observation();
		observation.setId("230ade51-9daf-4467-895b-6a32a97c484c");
		observation.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationWomenHealth");
		observation.getText().setStatus(NarrativeStatus.GENERATED);
		observation.getText().setDivAsString(
			"<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\"><p><b>Narrative with Details</b></p><p><b>id</b>: AgeOfMenarche</p><p><b>status</b>: final</p><p><b>code</b>: Age at menarche <span>(Details : LOINC code '42798-9' = 'Age at menarche', given as 'Age at menarche')</span></p><p><b>subject</b>: ABC</p><p><b>value</b>: 14 age</p></div>");
		observation.setStatus(ObservationStatus.FINAL);
		observation.setCode(new CodeableConcept(new Coding("http://loinc.org", "42798-9", "Age at menarche"))
				.setText("Age at menarche"));
		observation.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		observation.setValue(
				new Quantity().setValue(14).setUnit("age").setSystem("http://unitsofmeasure.org").setCode("{age}"));

		return observation;
	}

	// Populate "urn:uuid:6b820f25-347f-4f19-abdf-ccebebce1ca5" Resource Checked
	public static Observation populateLastMenstrualPeriodResource() {
		Observation observation = new Observation();
		observation.setId("6b820f25-347f-4f19-abdf-ccebebce1ca5");
		observation.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationWomenHealth");
		observation.getText().setStatus(NarrativeStatus.GENERATED);
		observation.getText().setDivAsString(
			"<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\"><p><b>Generated Narrative with Details</b></p><p><b>id</b>: LastMenstrualPeriod</p><p><b>meta</b>: </p><p><b>status</b>: final</p><p><b>code</b>: Last menstrual period start date <span>(Details : {LOINC code '8665-2' = 'Last menstrual period start date', given as 'Last menstrual period start date'})</span></p><p><b>subject</b>: <a>Patient/1</a></p><p><b>effective</b>: 2020-11-14</p><p><b>value</b>: 110120 MMDDYY</p></div>");
		observation.setStatus(ObservationStatus.FINAL);
		observation.setCode(
				new CodeableConcept(new Coding("http://loinc.org", "8665-2", "Last menstrual period start date"))
						.setText("Last menstrual period start date"));
		observation.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		observation.setEffective(new DateTimeType("2020-11-14"));
		observation.setValue(new Quantity().setValue(110120).setUnit("MMDDYY").setSystem("http://unitsofmeasure.org")
				.setCode("{MMDDYY}"));
		return observation;
	}

	// Populate "urn:uuid:3eb83708-d95b-47c2-994d-d384ffea27bd" Resource Checked
	public static Observation populateDietTypeResource() {
		Observation observation = new Observation();
		observation.setId("3eb83708-d95b-47c2-994d-d384ffea27bd");
		observation.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationLifestyle");
		observation.getText().setStatus(NarrativeStatus.GENERATED);
		observation.getText().setDivAsString(
				"<div xmlns=\"http://www.w3.org/1999/xhtml\"><p><b>Narrative with Details</b></p><p><b>id</b>: DietType</p><p><b>status</b>: final</p><p><b>code</b>: Diet [Type] <span>(Details : LOINC code '81663-7' = 'Diet [Type]', given as 'Diet [Type]')</span></p><p><b>subject</b>: ABC</p><p><b>value</b>: Vegan<span> (Details: SNOMED CT code 138045004 = 'Vegan diet', given as 'Vegan diet')</span></p></div>");
		observation.setStatus(ObservationStatus.FINAL);
		observation.setCode(
				new CodeableConcept(new Coding("http://loinc.org", "81663-7", "Diet [Type]")).setText("Diet [Type]"));
		observation.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		observation.getValueCodeableConcept().addCoding(new Coding("http://snomed.info/sct", "138045004", "Vegan diet"))
				.setText("Vegan diet");

		return observation;
	}

	// Populate "urn:uuid:d2fbdc51-7f16-4791-a2b5-6c63d4d80ec5" Resource Checked
	public static Observation populateTobaccoSmokingStatusResource() {
		Observation observation = new Observation();
		observation.setId("d2fbdc51-7f16-4791-a2b5-6c63d4d80ec5");
		observation.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationLifestyle");
		observation.getText().setStatus(NarrativeStatus.GENERATED);
		observation.getText().setDivAsString(
			"<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\"><p><b>Narrative with Details</b></p><p><b>id</b>: TobaccoSmokingStatus</p><p><b>status</b>: final</p><p><b>code</b>: Finding of tobacco smoking behavior <span>(Details : SNOMED CT code '365981007' = 'Finding of tobacco smoking behavior', given as 'Finding of tobacco smoking behavior')</span></p><p><b>subject</b>: ABC</p><p><b>value</b>: Never smoked tobacco<span> (Details: SNOMED CT code 266919005 = 'Never smoked tobacco', given as 'Never smoked tobacco')</span></p></div>");
		observation.setStatus(ObservationStatus.FINAL);
		observation.setCode(new CodeableConcept(
				new Coding("http://snomed.info/sct", "365981007", "Finding of tobacco smoking behavior"))
				.setText("Finding of tobacco smoking behavior"));
		observation.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		observation.getValueCodeableConcept()
				.addCoding(new Coding("http://snomed.info/sct", "266919005", "Never smoked tobacco"))
				.setText("Never smoked tobacco");

		return observation;
	}

	// Populate "urn:uuid:9cca6bcd-75b6-4d1a-a562-01e139d8ad59" Resource Checked
	public static Observation populateBloodPressureResource() {
		Observation observation = new Observation();
		observation.setId("9cca6bcd-75b6-4d1a-a562-01e139d8ad59");
		observation.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/ObservationVitalSigns");
		observation.getText().setStatus(NarrativeStatus.GENERATED);
		observation.getText().setDivAsString(
			"<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\"><p><b>Generated Narrative with Details</b></p><p><b>id</b>: blood-pressure</p><p><b>meta</b>: </p><p><b>identifier</b>: urn:uuid:187e0c12-8dd2-67e2-99b2-bf273c878281</p><p><b>basedOn</b>: </p><p><b>status</b>: final</p><p><b>category</b>: Vital Signs <span>(Details : {http://terminology.hl7.org/CodeSystem/observation-category code 'vital-signs' = 'Vital Signs', given as 'Vital Signs'})</span></p><p><b>code</b>: Blood pressure systolic &amp; diastolic <span>(Details : {LOINC code '85354-9' = 'Blood pressure panel with all children optional', given as 'Blood pressure panel with all children optional'})</span></p><p><b>subject</b>: <a>Patient/1</a></p><p><b>effective</b>: 29/09/2020</p><p><b>performer</b>: <a>Practitioner/1</a></p><p><b>interpretation</b>: Below low normal <span>(Details : {http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation code 'L' = 'Low', given as 'low'})</span></p><p><b>bodySite</b>: Right arm <span>(Details : {SNOMED CT code '368209003' = 'Right upper arm', given as 'Right arm'})</span></p><blockquote><p><b>component</b></p><p><b>code</b>: Systolic blood pressure <span>(Details : {LOINC code '8480-6' = 'Systolic blood pressure', given as 'Systolic blood pressure'};)</span></p><p><b>value</b>: 107 mmHg<span> (Details: UCUM code mm[Hg] = 'mmHg')</span></p><p><b>interpretation</b>: Normal <span>(Details : {http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation code 'N' = 'Normal', given as 'normal'})</span></p></blockquote><blockquote><p><b>component</b></p><p><b>code</b>: Diastolic blood pressure <span>(Details : {LOINC code '8462-4' = 'Diastolic blood pressure', given as 'Diastolic blood pressure'})</span></p><p><b>value</b>: 60 mmHg<span> (Details: UCUM code mm[Hg] = 'mmHg')</span></p><p><b>interpretation</b>: Below low normal <span>(Details : {http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation code 'L' = 'Low', given as 'low'})</span></p></blockquote></div>");
		observation.addIdentifier(new Identifier().setSystem("urn:ietf:rfc:3986")
				.setValue("urn:uuid:187e0c12-8dd2-67e2-99b2-bf273c878281"));
		observation.setStatus(ObservationStatus.FINAL);
		observation.addCategory(new CodeableConcept(new Coding(
				"http://terminology.hl7.org/CodeSystem/observation-category", "vital-signs", "Vital Signs")));
		observation.setCode(new CodeableConcept(
				new Coding("http://loinc.org", "85354-9", "Blood pressure panel with all children optional"))
				.setText("Blood pressure panel with all children optional"));
		observation.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));
		observation.setEffective(new DateTimeType("2020-09-29"));
		observation.addPerformer(new Reference().setReference("urn:uuid:3bc96820-c7c9-4f59-900d-6b0ed1fa558e"));
		observation.addInterpretation(new CodeableConcept(
				new Coding("http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation", "L", "low"))
				.setText("Below low normal"));
		observation.getBodySite().addCoding(new Coding("http://snomed.info/sct", "368209003", "Right arm"));
		List<ObservationComponentComponent> componentList = observation.getComponent();
		ObservationComponentComponent component = new ObservationComponentComponent();
		component.setCode(new CodeableConcept(new Coding("http://loinc.org", "8480-6", "Systolic blood pressure")));
		component.setValue(
				new Quantity().setValue(107).setUnit("mmHg").setSystem("http://unitsofmeasure.org").setCode("mm[Hg]"));
		component.addInterpretation(new CodeableConcept(
				new Coding("http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation", "N", "normal"))
				.setText("Normal"));

		ObservationComponentComponent component1 = new ObservationComponentComponent();
		component1.setCode(new CodeableConcept(new Coding("http://loinc.org", "8462-4", "Diastolic blood pressure")));
		component1.setValue(
				new Quantity().setValue(60).setUnit("mmHg").setSystem("http://unitsofmeasure.org").setCode("mm[Hg]"));
		component1.addInterpretation(new CodeableConcept(
				new Coding("http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation", "L", "low"))
				.setText("Below low normal"));

		componentList.add(component);
		componentList.add(component1);

		return observation;
	}

}
