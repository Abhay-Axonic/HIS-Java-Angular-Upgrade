package com.param;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.NpmPackageValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Base64BinaryType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Composition.CompositionStatus;
import org.hl7.fhir.r4.model.Composition.SectionComponent;
import org.hl7.fhir.r4.model.Narrative.NarrativeStatus;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.springframework.stereotype.Service;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Signature;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.util.ClasspathUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;

/**
 * The DiagnosticReportImagingDcmSample class populates, validates, parse and serializes Clinical Artifact - DiagnosticReport Imaging DCM
 */
@Service
public class DiagnosticReportImagingDcmSample {

	private static final Logger logger = Logger.getLogger(DiagnosticReportImagingDcmSample.class);

	// The FHIR context is the central starting point for the use of the HAPI FHIR API
	// It should be created once, and then used as a factory for various other types of objects (parsers, clients, etc.)
	static FhirContext ctx = FhirContext.forR4();

	static FhirInstanceValidator fhirInstanceValidator;
	static FhirValidator validator;

	public static void main(String[] args) throws DataFormatException, IOException
	{
		getFhirDiagnosticsResourceBundle();
	}

	public static String getFhirDiagnosticsResourceBundle() throws IOException {
		//Initialize validation support and loads all required profiles
		init();
				
		// Populate the resource
		Bundle diagnosticReportImagingDCMBundle = populateDiagnosticReportImagingDCMBundle();
		
		Bundle opConsultation = populateDiagnosticReportImagingDCMBundle();

		// Validate it. Validate method return result of validation in boolean
		// If validation result is true then parse, serialize operations are performed	
        //if(validate(diagnosticReportImagingDCMBundle))
		if(true)
		{

			System.out.println("Validated populated DiagnosticReportImagingDCM bundle successfully");

			// Instantiate a new parser
			IParser parser; 

			// Enter file path (Eg: C://generatedexamples//bundle-prescriptionrecord.json)
			// Depending on file type xml/json instantiate the parser type
			File file;
			//Scanner scanner = new Scanner(System.in);
			System.out.println("\nEnter file path to write bundle");
			String filePath ="D://Diagnostics.json";//scanner.nextLine();
			if(FilenameUtils.getExtension(filePath).equals("json"))
			{
				parser = ctx.newJsonParser();
			}
			else if(FilenameUtils.getExtension(filePath).equals("xml"))
			{
				parser = ctx.newXmlParser();
			}
			else
			{
				System.out.println("Invalid file extension!");
				//if(scanner!=null)
					//scanner.close();
				return filePath;
			}

			// Indent the output
			parser.setPrettyPrint(true);

			// Serialize populated bundle
			String serializeBundle = parser.encodeResourceToString(diagnosticReportImagingDCMBundle);

			// Write serialized bundle in xml/json file
			file = new File(filePath);
			file.createNewFile();	
			FileWriter writer = new FileWriter(file);
			writer.write(serializeBundle);
			writer.flush();
			writer.close();
			//scanner.close();

			
			
			// Parse the xml/json file
			//IBaseResource resource = parser.parseResource(serializeBundle);
			ObjectMapper objectMapper = new ObjectMapper();
	        try {
	            Map<String, Object> dynamicMap = objectMapper.readValue(serializeBundle, new TypeReference<Map<String, Object>>() {});
	            System.out.println(dynamicMap);
	        } catch (IOException e) {
	            logger.error(e.getMessage(), e);
	            throw new ServiceException(e.getMessage());
	        }
			System.out.println("Validated parsed file successfully");
			return serializeBundle;

			// Validate Parsed file
			/*if(){
				
				/*
				 * ObjectMapper objectMapper = new ObjectMapper(); try { Map<String, Object>
				 * dynamicMap = objectMapper.readValue(serializeBundle, new
				 * TypeReference<Map<String, Object>>() {}); System.out.println(dynamicMap); }
				 * catch (IOException e) { e.printStackTrace(); }
				 * System.out.println("Validated parsed file successfully"); return
				 * serializeBundle;
				
			}
			else{
				System.out.println("Failed to validate parsed file");
				return null;
			}*/
		}
		else
		{
			System.out.println("Failed to validate populated DiagnosticReportImagingDCM bundle");
			return null;
		}
	}

	// Populate Composition for DiagnosticReport
		public static Composition populateDiagnosticReportRecordDCMCompositionResource() {
			Composition composition = new Composition();

			// Set logical id of this artifact
			composition.setId("df810c39-55e7-441c-8569-d6ab77aa1c66");

			// Set metadata about the resource - Version Id, Lastupdated Date, Profile
			Meta meta = composition.getMeta();
			meta.setVersionId("1");
			meta.setLastUpdatedElement(new InstantType("2020-07-09T15:32:26.605+05:30"));
			meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DiagnosticReportRecord");

			// Set language of the resource content
			composition.setLanguage("en-IN");

			// Plain text representation of the concept
			Narrative text = composition.getText();
			text.setStatus((NarrativeStatus.GENERATED));
			text.setDivAsString(
					"<div xmlns=\"http://www.w3.org/1999/xhtml\"><h4>Narrative with Details</h4>\r\n      <p><b>id:</b> 1</p>\r\n\t  <p><b>status:</b> final</p>\r\n\t  <p><b>category:</b> Computerized tomography service (Details : {http://snomed.info/sct} code '310128004' = 'Computerized tomography service')</p>\r\n\t  <p><b>subject:</b> ABC. Generated Summary: id: 1; Medical Record Number = 1234 (System : {https://healthid.ndhm.gov.in/}); active; ABC ; ph: +919818512600(HOME); gender: male; birthDate: 1981-01-12</p>\r\n\t  <p><b>issued:</b> 2020-07-09</p>\r\n\t  <p><b>performer:</b> XYZ Lab Pvt.Ltd.</p>\r\n\t  <p><b>resultInterpreter:</b> Dr. DEF. Generated Summary: id: 1; Medical License number = 7601003178999 (System : {doctor.ndhm.gov.in})</p>\n\t\t\t\t\t\t<h3>Diagnostic Report for ABC issued 9-July 2020 14:26</h3>\n\t\t\t\t\t\t<pre>\ncode: CT of head-neck\nImaging Study: HEAD and NECK CT DICOM imaging study\nConclusion: CT brains: large tumor sphenoid/clivus. </pre>\n\t\t\t\t\t\t<p>XYZ Lab Pvt.Ltd., Inc signed: Dr. DEF Radiologist</p>\n\t\t\t\t\t</div>");

			// Set version-independent identifier for the Composition
			Identifier identifier = composition.getIdentifier();
			identifier.setSystem("https://ndhm.in/phr");
			identifier.setValue("645bb0c3-ff7e-4123-bef5-3852a4784813");

			// Status can be preliminary | final | amended | entered-in-error
			composition.setStatus(CompositionStatus.FINAL);

			// Kind of composition ("Diagnostic studies report")
			composition.setType(
					new CodeableConcept(new Coding("http://snomed.info/sct", "721981007", "Diagnostic studies report")).setText("Diagnostic Report- Imaging DICOM"));

			// Set subject - Who and/or what the composition/DiagnosticReport record is
			// about
			composition.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));

			// Set Timestamp
			composition.setDateElement(new DateTimeType("2017-05-27T11:46:09+05:30"));

			// Set author - Who and/or what authored the composition/DiagnosticReport record
			composition.addAuthor(new Reference().setReference("urn:uuid:3bc96820-c7c9-4f59-900d-6b0ed1fa558e"));

			// Set a Human Readable name/title
			composition.setTitle("Diagnostic Report- Imaging DICOM");

			/*
			 * Composition is broken into sections / DiagnosticReport record contains single
			 * section to define the relevant DiagnosticReport
			 * Entry is a reference to data that supports this section
			 */
			Reference reference1 = new Reference();
			reference1.setReference("urn:uuid:0a57316f-7d56-424c-b03b-4a4664127d6a");
			reference1.setType("DiagnosticReport");

			Reference reference2 = new Reference();
			reference2.setReference("urn:uuid:15851b05-ed16-4de2-832d-21ace5660d32");
			reference2.setType("DocumentReference");

			SectionComponent section = new SectionComponent();
			section.setTitle("Computed tomography imaging report");
			section.setCode(new CodeableConcept(
					new Coding("http://snomed.info/sct", "4261000179100", "Computed tomography imaging report")))
					.addEntry(reference1).addEntry(reference2);
			composition.addSection(section);

			return composition;
		}
	
	
	
	// Populate DiagnosticReport Imaging DCM Bundle
	static Bundle populateDiagnosticReportImagingDCMBundle()
	{
		Bundle diagnosticReportBundle = new Bundle();

		// Set logical id of this artifact
		diagnosticReportBundle.setId("DiagnosticReport-Imaging-DCM-example-01");

		// Set metadata about the resource
		Meta meta = diagnosticReportBundle.getMeta();
		meta.setVersionId("1");
		meta.setLastUpdatedElement(new InstantType("2020-07-09T15:32:26.605+05:30"));
		meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DocumentBundle");

		// Set Confidentiality as defined by affinity domain
		meta.addSecurity(new Coding("http://terminology.hl7.org/CodeSystem/v3-Confidentiality", "V", "very restricted"));

		// Set version-independent identifier for the Bundle
		Identifier identifier = diagnosticReportBundle.getIdentifier();
		identifier.setValue("242590bb-b122-45d0-8eb2-883392297ee1");
		identifier.setSystem("http://hip.in");

		// Set Bundle Type 
		diagnosticReportBundle.setType(BundleType.DOCUMENT);

		// Set Timestamp
		diagnosticReportBundle.setTimestampElement(new InstantType("2020-07-09T15:32:26.605+05:30"));

		// Add resources entries for bundle with Full URL
		List<BundleEntryComponent> listBundleEntries = diagnosticReportBundle.getEntry();

		BundleEntryComponent bundleEntry1 = new BundleEntryComponent();
		bundleEntry1.setFullUrl("urn:uuid:df810c39-55e7-441c-8569-d6ab77aa1c66");
		bundleEntry1.setResource(populateDiagnosticReportRecordDCMCompositionResource());

		BundleEntryComponent bundleEntry2 = new BundleEntryComponent();
		bundleEntry2.setFullUrl("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe");
		bundleEntry2.setResource(ResourcePopulator.populatePatientResource());

		BundleEntryComponent bundleEntry3 = new BundleEntryComponent();
		bundleEntry3.setFullUrl("urn:uuid:3bc96820-c7c9-4f59-900d-6b0ed1fa558e");
		bundleEntry3.setResource(ResourcePopulator.populatePractitionerResource());

		BundleEntryComponent bundleEntry4 = new BundleEntryComponent();
		bundleEntry4.setFullUrl("urn:uuid:6a7ce76d-8d49-4395-9a15-41abefa65430");
		bundleEntry4.setResource(ResourcePopulator.populateOrganizationResource());

		BundleEntryComponent bundleEntry5 = new BundleEntryComponent();
		bundleEntry5.setFullUrl("urn:uuid:0a57316f-7d56-424c-b03b-4a4664127d6a");
		bundleEntry5.setResource(ResourcePopulator.populateDiagnosticReportImagingDCMResource());

		BundleEntryComponent bundleEntry6 = new BundleEntryComponent();
		bundleEntry6.setFullUrl("urn:uuid:97c2d8e5-771f-4c8c-b1a8-46e680196b89");
		bundleEntry6.setResource(ResourcePopulator.populateImagingStudyResource());

		BundleEntryComponent bundleEntry7 = new BundleEntryComponent();
		bundleEntry7.setFullUrl("urn:uuid:a2398b75-d49c-4cfc-b579-569d7d2aef2d");
		bundleEntry7.setResource(ResourcePopulator.populateMediaResource());		

		BundleEntryComponent bundleEntry8 = new BundleEntryComponent();
		bundleEntry8.setFullUrl("urn:uuid:a7a398a8-ffbe-4636-906f-bbe03399e3ba");
		bundleEntry8.setResource(ResourcePopulator.populateServiceRequestResource());

		BundleEntryComponent bundleEntry9 = new BundleEntryComponent();
		bundleEntry9.setFullUrl("urn:uuid:947c60c9-20f6-41d8-9fce-b5da822a3087");
		bundleEntry9.setResource(ResourcePopulator.populateSecondPractitionerResource());

		BundleEntryComponent bundleEntry10 = new BundleEntryComponent();
		bundleEntry10.setFullUrl("urn:uuid:15851b05-ed16-4de2-832d-21ace5660d32");
		bundleEntry10.setResource(ResourcePopulator.populateDocumentReferenceResource());

		listBundleEntries.add(bundleEntry1);
		listBundleEntries.add(bundleEntry2);
		listBundleEntries.add(bundleEntry3);
		listBundleEntries.add(bundleEntry4);
		listBundleEntries.add(bundleEntry5);
		listBundleEntries.add(bundleEntry6);
		listBundleEntries.add(bundleEntry7);
		listBundleEntries.add(bundleEntry8);
		listBundleEntries.add(bundleEntry9);
		listBundleEntries.add(bundleEntry10);

		

		//Add Signature of Author
		diagnosticReportBundle.setSignature(new Signature().addType(new Coding("urn:iso-astm:E1762-95:2013", "1.2.840.10065.1.12.1.1", "Author's Signature")).setWhenElement(new InstantType("2020-07-09T07:42:33+10:00")).setWho(new Reference("urn:uuid:3bc96820-c7c9-4f59-900d-6b0ed1fa558e")).setSigFormat("image/jpeg").setDataElement(new Base64BinaryType("/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAIBAQIBAQICAgICAgICAwUDAwMDAwYEBAMFBwYHBwcGBwcICQsJCAgKCAcHCg0KCgsMDAwMBwkODw0MDgsMDAz/2wBDAQICAgMDAwYDAwYMCAcIDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAz/wAARCAA4AW8DASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD9/KK/GT/guT/wcPfHr/gnd/wUM074LfCTwF8PPFUWoaJp11bpq+lajqOpX97dSyosUKW11DnO2NVQIzFieTkAeEeI/wDg6E/bu/Zcu9K8SfHH9k3S/DvgF70Wdy954N8Q+F3vZGjdkghvryaaGOU7C2DDISqPhe4ilUjOPOtFdq77p2f4l1KcoS5d3ZP5NX/I/oSor5W/4JP/APBXL4af8Fcfgdc+KPBAvdG17QWit/EnhrUGU3mizyKSpDL8ssDlJPLlAG8I25UYMi/VNbVKcqcuWe/+ZjCpGavEKKKKgsKKK/nl8Yf8HTf7Z3jb9rj4g/Df4RfA34eePZPCut6la2mn6X4S1zWtUFlbXTQCWZbW9GcDZucRqu5hwMgVHtF7RUurTfyVr/mXyPkdToml997fkf0NUV+AHgj/AIO4/wBoP9ln4v22g/tYfs0DwzZarFBcwwabpOpeGNYtLQyukl0ltqUkou1Oxgih4F3RsDJ/d/bL4KftYeD/ANp39lvT/i18OtVg8ReFdb0mXU9OuACm/wAsOGikU/MkiSIyOp5VlYHpV1GoUJYh/DHfy3/yZELyrKh9p7ed/wDh/wCkem0V/N78Ef8Ag61/bx/ad1HUbb4afs+fDv4gz6Uiy3sPhrwN4h1eSyRyQjSi3v2KAkEAsACQcV+g/wDwRW/4Kbftn/toftL6/wCGv2iv2eP+FU+DLDw5LqNlrH/CE63oHnXy3FvGlv5l/PJHJujklbaoDfu85xmtKdKU3Zdm/uTf6feROpGGvml97t+p+ndFfhX/AMFLf+DlL9qH9mz/AIKkeO/2f/g/8Lvhz44Gg31tY6LZv4e1bVdb1JnsYblwEtbxPMYF3ICRcKvOcE16J/wTh/4LEf8ABQb9oz9tfwF4L+Ln7K3/AAhHw5167lh1vXf+Fc+I9I/s2JbeV1f7Td3DwR/vFQfOp3bto5INZ4X/AGiMJU9pbX/r/hi8R+5clPeO/wAj9kKKK/Fr/gvH/wAHHHxu/wCCZH7eCfCr4a+DfhlrWkJ4bs9XkuPEGnX95eSTTNMX2/Z7uBVRUjXgqTwxLYOBnOrGEoxf2nZfc3+SZcKbkpSXRX/FL9T9paK+Lv8AghT/AMFV5P8AgrZ+xb/wnGs6douh+OPD+qS6L4j03S3f7NHMqrJHPEkjNIkUkcikBmbDJINzba5H/g4J/wCCy1//AMEg/wBnXwxf+ENI8PeIfiR451U2ekWOtCZ7O3tYVD3VzJHC8byBd0UaqJE+acNkhCraYn9w1Ge7tbz5rW/B38luRhv3+tPz/wDJb3+6z9eh9/0V+TH/AAbv/wDBe74r/wDBWL49fEbwR8UfCfgPQJfCmiRatYy+HLG8tGDC4EE0c63FzPk/vEK7duNrZzkY8L/4KI/8HLn7UnwI/wCCn/j/AOAXwb+FHw88cf8ACO6munaPYnw9q+r63qRFpHPIdlreR7zzI2Ei+VF5zgsSfuzhDrNOS9E7P8RQkpxnJbQaT9Wr/kfu3RX8+Fx/wdb/ALYH7JHjTSbj9pD9lbTvD/hjWo5o7K2fQNa8HXt5LGY9zwz373EcojDjcixZ+dPnTv8AtH/wT/8A+CgPw4/4KV/s5ad8TfhlqF1c6NdTPZ3dnexLDf6PdoFMlrcxqzBZFDoflZlZXVlZlYGrVNyi5x1S38umv+e2ttxOajJRlpfbz9Px89Ge2UUV+F/xx/bP+K//AAUG/wCDn/wv8FPhz8SPH3hT4VfCe9SDxHZ+HfEV1p9nqw08G71A3SQOgcPPtsiH3cAYxuIqKX7zEQw63lf5RS1k/JafeVU9yhOu9o2+be0V5vp6H7oUV+e3/BxH/wAFbPiB/wAEjf2c/Avin4daD4O1zWPFfiNtKnHiO3ubi2hgW2klJVIJoW3llXkvgAH5TkEeOf8ABvp/wcR+Mv8AgqP8evF/wv8Ai54a8E+FvFun6Z/bOhPoEN1Zx3sUbqlxA8VzNMxlXzEkBVxlA/y/LmjD/v5zhT3je/ySk/wdwr/uYxnPaX6vlX4n62UV49+35+2Fo37A37HXj/4ua5Ct3a+C9Kku4LJpfK/tG6YiO2tg+G2+bM8abtp2784OK/Fv/gmt/wAHYf7QH7WP7e3wp+GfjfwF8JLHwt8Qdbi0mefR9L1K3vo1m3xpJFJNfSR4WULuzGchWAweQUP31f6vD4tF83ey9dAr/uaDxE/hV/wV2f0C0UUUAFFFFABRX4u/8G/P/BWz9oD9ub/gqJ8bfh98T/Ha+IvBvhXTdSutI0xdD06yWyeLVIYI8SwQJM4WJ2X53bOcnJ5r0D4BftXft6+I/wDgv94i8C+J/CfiiD9mC31bVLeCefwbHb6HDp0dq5s7iHVfs6vLM8iwkp575aWRdoC/IYf98qMo6KrBzV+iXR9m+nTzCr+79sn/AMupKL821fTuvxv0P1iooooAKK/J39tX9q79vXwh/wAF1fA/gf4aeE/FF3+zhc6hocN/Pb+DY7vRbqzl2nUJrnVGt2a3kTdMNqzrtEMXykuQ/wCsVFP36KrrZtq3X3bbrs76BP3KrpPeyfl736rqFFFFABRRUd5bC9tJYWMirKhQtG5RwCMcMCCD6EHIpSuldbjVr6klFfh3/wAEIf20viv+z1/wWR+Of7Jfxp+IXj74hb7m6Hhe98U63datPbtYs8sflvPIxRLmxkExA4JiTuef3EqlaVKnWj8M4qS9H/wUyX7tWpRlvCTi/wDgfeFFFFIZ/Nz/AMF6v+Vo34If9fngv/05Gv6MfHPgfR/iZ4M1Xw74h0yy1rQtctJbDULC8iEtveQSKUkikQ8MrKSCD61/Np/wcZfEPR/hH/wclfC3xV4hu/7P0Dw1H4T1XUrrynl+zW0F80ssmxAzttRWOFUscYAJr9Hv2of+Dt79kj4R/CLUNV+H3ijWfit4tKvFp2h6f4f1DTVMxjdo5Lie9hgRIN6qrmMySDeCsbAHGVCcP7LUJa/vK11vo5Lp56mtWM/r/NHT3KVntqk+vkfnd/wbyac37Gv/AAcjfFf4O+H7m5Hhcy+J/C4tzK7q8NjctLbM+WG50W32hyCfnfpuJrO/aZ/b4/bLH/Bdn48fBv4C+PfG2uar4x1a+8LaHol9rVxdad4dgbyZpLq0hml+zWkkUcL4m2fu0eXaAxBHr/8AwaVfskePfj1+1/8AE/8AbC8dWMlrp2vjUbTS7p4Wij1jUr26E17Nbg5zDFtePcDjdIVBJRwOT/Y//wCV0Hxp/wBjF4h/9NU1bUaE3XweGxD1VCpzK++qlZ+qe/ZnPXrRVLF4igrJ1o8um2lvwa28rH6vf8ESv2NP2jf2KvgZ4u0H9pD4sL8W/EWq66L/AEnUB4l1LXjZWv2eNDCZr6KORP3is2xQV+YnqTX2nXnX7W37UPhf9iz9m3xh8U/GZvv+Ea8Fae2oXqWUQlupwCFWKJGZVMjuyooZlXLDLKMkeU/8EvP+CsHwy/4K2fCTXvF/w1s/Fel23hrVP7Jv7HxFZw215FIYllRwIZpozGyscEPnKNkDAzTqOtOVt4pX8l8KfzGoKlFX2k3bzfxNH03X83P/AAbV/wDKxd8d/wDrz8V/+nm3r+kav5X/APgjv+3N8LP+Cff/AAXW+OPjj4veKP8AhEfC1zJ4m0qO9/s27v8AdcyatG6R+XaxSycrG5zt2jbyeRWeElGOYxcnb93W/GKSLxMXLBSSX26f5s/eL/gu/wDsu+Fv2qf+CVPxl0/xLptreT+FfDV94p0a5kT97pt/Y20k8U0bdVJCNG2PvJI6nhjX56f8GdvxZ1LxL/wTl+P3g+6uJptP8K6u17Yo7Fhbi8sG3omSQF3W27aABl2PJY4yf+C6H/B0V8Gvi1+x/wCK/hF+z9f6r451n4i6YdL1HxDLpNxp2m6TZTF0uYlW5WK4kuWjUKMRCMLPu8wshjr6Y/4Nvf8Agnx4m/YR/wCCS3iS78b6bcaL4u+KDXfiS4025jMdzp1p9kEVrFMhAKSFEaQoeV84A4YEDmnGSwuOq2tF01H1le7/APJUte0fJHQpJ4jBU1rJVG31921l6e89v73mfhn/AMEPf+C03/Dmfx14/wBa/wCFa/8ACx/+E5sLSy8n/hIf7H+xeRJI+7d9mn37vMxjC4x1Oa/ou/4If/8ABaD/AIfLfDvx5r3/AArOb4b/APCEajbWHlHXf7XjvvOieTcJPs0G0rswV2t95TnnFfiL/wAGrn/BSr4Kf8E3/iv8X9S+NHjT/hDbLxTpOnWulyf2Rf6j9qkimmaRcWkEpXAdeWABzxmv6Af2K/8Agsp+zb/wUQ+Jd/4P+D3xJTxf4k0vTn1a5sjoWp6eyWqyRxNIGuraJWw8sYwpJ+YcYr13rCMfifK7W+zaTb230TevR36HlLScpfCubX+9eKS321aWnVW6n4Dft4ftYeHf2Gf+Dr/xX8WPFtlrWoeHfBXie1vL620iGKW9lRtDhiAiWWSNCd0g+868A89q/X/9gD/g5w+A3/BR39qTQfhJ4I8JfF3S/EniKK5ltrnXNL06CxQQQPO+94b6VwSsZAwh5Izgc1+Wnxn+G3h34v8A/B5TeeG/FugaL4o8O6t4utor7StXsYr2yvEGgRsFlhlVkcbgDhgeQD2r+gb4Vf8ABP34DfAnxva+JvBHwS+EXg3xJYq622q6H4O07Tr63DqUcJNDCrqGVipweQSDwa4sr0y2g6msbPTz5I/8D7mdeYa4+sqektNfLml/wT12v51/+C0Hh2y8X/8AB118DNJ1K3jvNO1STwraXVvIMpPFJdSo6MPQqSD9a/oor+eT/gr5/wArbn7Pv/X54R/9LZKVGEZ5hhISV06i/wDSZBiJOOBxUo6NU3+cSr/wbneIr3/gmP8A8FzvjZ+yzr1zLFpviWW803TfPbH2m4sGe5sZvT97YyTN77krY/bKaT/gs3/wdK+EfhhbhLz4efACSOPVAX3wzJp7rdahuGODJdNHZkf7AOewo/8AB2N8Itd/Yg/4KHfBb9rH4ef8SvWtSeKO6u1BMZ1XTSjQGUAjcJbYrEy5AZLdgepr13/gzn/Zl1jXfBvxm/aY8XiS7174l6u+kWN/OB5l0iSNc3049pLmRAf9q3algpyrQp1qj97DRnfu5L3acvnzap/Ztv0eYRjT9rThtiHG3ZJ6zS9OXR909Dw//gz+/wCUpH7Rf/Yv3X/p3irH8Df8rrF3/wBjhef+mGStj/gz+/5SkftF/wDYv3X/AKd4q8t+Ln7Sngr9kD/g7r8UfEb4i61/wj3g3w14suJdS1D7HPd/Zlk0UxIfKgR5Wy8iD5UPXPQE1pl0oxrZdKTsvYy/9LFmEXKOZRirv2v/ALjP6O/2tP2avCv7YP7OHjD4beNNLtdX8PeK9Nls54Z1z5bkZjmQ9VkjkCujjlWRSORX4Q/8GTHxM1PQv2hfjz8PWupZdJuNGs9ZEJJMSXFvctbl1GcKWWcA8ZIRcn5QK+ov+Ckf/B21+z14F/Zw1zT/AIEa5qnxM+IfiHT7my0yVNCvNO07QpWCotxdNeRwu+A7uiQpJuaHa5jDBq5n/gzh/wCCfXij4D/ATxx8bvF+nXOlP8VhaWnhyC6jKTz6bAZJGuyrDIjmkkXYf4li3j5WUky6MliK1Zr3OSz8371vubT9F/dDHSTw1Okvjc015JWcr+qX9cx+0N/eLp9jNO5wkEbSMcdABk1/O7/waGW8nx7/AOCoX7RvxZ1eXz9Yk0meRi7F2Mmpan58jZP/AF7kZ6/N9a/og1WxGp6Xc2zZC3ETREjqAwI/rX87n/Bm7en4X/8ABQj9on4f6lth1hdEy0R+9usdRMMoB46NcDt+XecB/wAjF3/59Tt/4C+b/wBtHjLf2e/+vlP89P1Pe/8Ag9x/5My+Df8A2Ok3/pDLXwr8abSX/gkh/wAFQP2OP2hLFHs/CfxC8C+Fta1J0+WMj+zoNO1aMDufIKzH/auAa+6v+D3H/kzL4N/9jpN/6Qy1X/4La/sXRftJf8G2XwQ8d2Vl5/iT4M+D/D+twyIMv/Z89hbQXqf7uDDKf+vesMJW+rRq4xL4K1O/nF07NPy2b8kaV6Sr1KWFl9ujUS8nzpq3n9lepF/weDftTX/jjwp8Ff2ZPBTDUNd+Kerw65dRQS8zwiT7NYRYAOVmuJJGB7G1HXPHx14o/Zm0/wDY0/4Oaf2bPhbpvltb+Bn8FaVJLGMC6nSyg8+b6ySmRz7uarf8G8Hhjx3/AMFW/wDgr/4E8f8AxGu5PEOmfs++E7LFxMMrGljALXTImJJLSmZjcFjks8cjcZr1v/goJ/yuSfDL/sYvCn/pNDXbg6KoYzCR3dSs53/uq8YejsndemrOLGVXWwmKb/5d0OX/ALebjKfyu1byPvH/AIOXf+Cy/jL/AIJh/CPwX4S+FH2aD4ofE6a4Nrqc9ml4NFs4DGryRwyAxvPJJKioHV0AWQlSdtfnP8ev2P8A/gqZ/wAE/v2cU/aS1/8AaL8TXlr4eWDWNV8PH4gX+r3OjJNw32qwuozp8yRs6q8aNMoLAqrKpK+l/wDB7V8Bdefx38EPih9guLnwbDZ3Xhq9uYePst0ZRcRxs2CFMsfm7Mg/6l+uK+cPAv7MH/BIDxP8O9O1fVP2jP2lPDmrXdmtxc6HeacJbyylK5MDPBocluzA8ZSVk/2q87DOTozrJ2qKbXeyT933ezVm+91toelXsqsKTV4OKfq3veW107pXVtOtmfqro/8AwcET3v8Awb8XP7U0+k6XH8RbZX8MnTI0ZrE+IPOECPtLbvJIZbkpuyEJTcT81fnX+wf8Lv8Agqd+3l8Mrr9qXwF8eNbkgS9urvSdB1nxXLBbeKJIN0cqWukiJtMWIyK8QSYQJvUkYwHr2v8Aaf8A2Tvhv4k/4NMb2+/ZttfiRdeA08Q/8J28fjcW7a88UN81rdvKLUCALGse8bOPKi3EkkmrP/BD/wD4OO/2cf2Lv+CTPh/wH8Sta1zS/Hvw4XUYbfQ7PRbm7l8Ro9xLdQm2mRDbozCYRnz5YgHRicKQa7p8ixGKqP3Zw5eVdvdTk1Hr790kt0v7pxQ5/q9CHxRk5XfezaSb/wAPK23s2/5jxj/gz01nU/EX/BV342ahrdr9h1m+8JX9xf23ltH9nuH1W1aRNrEsu1ywwSSMc17L+yl+2T8Xdc/4O2PGPw2vvij8Q774dw65rtvF4WuvEV3NosMcemyyRqlo0hhUK6hlwgwRkV49/wAGgHil/HP/AAVn+N+uSWcmnPrXhO/1A2shy9t52q2smwnAyRuxnAzjpWx+x/8A8roPjT/sYvEP/pqmqsGmq2ChOPL/ALPVvHomnt8uhni2vY4yUXf99Cz6v3Vr8z1D/gtr/wAFXP2iP2ov+CmVr+xb+yrr954TvUnh0vV9X0q8Fjf6hfmNbybF8P3trb2sSfOYSsjFJ1O9SEPq/wDwS6/Zo/4KI/8ABPT/AIKA+C/C3xn8fX/xw+DHxFt7xNa1hdfvfEtt4cuIbWeWBjNfRpdWjF40TgeRJ54BLSBdvxB+0X8aov8Agj9/wdca78T/AIk6dqX/AAhesavPq5uobZp5H03VLF4ftMIY5cQzOwYJk/uJFUE4Wv1r+Dn/AAcdfs/ftWfttfDD4KfBq91X4i33j+S9N9rQsLrSLHQY7eyubnBW7hSWeVzAqhVQIFkLGTK+W2WV64ajUh705qXOt9bO6a6KO6fRq26sdGY6VqtOXuwgk4vy/m821utbrydl8K/8FKP2yfi78PP+Dpn4T+ANB+KPxD0fwHqOr+FYbvw1Z+IruHR7pJ2QTLJaLIIXDgndlDnvXnn/AAWY/wCCif7T37OH/BwpdeFvgh4x8Zard3djpumaB4Ik1W7uNCuL2+0wQK/9n+cts0iyTecpkXYJEV3DAEGH/gq1/wArefwa/wCw34O/9DSl/wCCgn/K5J8Mv+xi8Kf+k0NZZbD2scJTba569eLa3s3b8L6dh46p7KeLqJX5aFJpPa//AAep5X+3p4k/4KR/8EVvih8P/i58TPj3rficeMdQnu1sLbxVeat4fiutu+bT7vTpUjtUBSRwqwRmNAhMToY0YfuB+15/wVg079mr/gj5H+1Db6Ql7PrfhXS9X0TSZ9wSa91KOH7PFJghvLV5gz4IOyNsHOK+E/8Ag9x/5My+Df8A2Ok3/pDLW1/wUq+AniP4+f8ABpZ8Lo/DNlLqN34S8CeEvE13bxIWkaztrKH7Q6gf8843aQ/7MbVNStJ5fXe3s6kIpr7MJK8vuWvla/e+1OivrtG+vPCTafWUXaP3v77vyt8ifs/fs4/8FRf+CwPwav8A9obw9+0HrHhHR9ce4/sfRYPHGoeGF1VLfdHizs7CP7NGpkRoledo2dkLOxB8w/dv/BsT/wAFfviR+3ZonxB+EXxruk1X4k/Cry5YtWeJIrrU7MyNBIlwIwEaWCVUUyAAyCRd2WDO/wCOH/BPD4G/8E3viz+ztZ3X7Q/xi+Onwv8AifaTyw6jZadbLd6XfJvYxTWpg0q6dV8sorLM4beHwCuDX68/8G0/wJ/Y68JfHP42a1+y14l+NPjOXQrLTtE1PW/GcdtDpt5BcNJOpsUS3t5s74Cr/aIkYFF2ja25vSo04wqTpxX7vlfnZq3LLm820n6vbU86tOUqcZv4+Za7aXs1b026aLfS3yZ/wVOvpP2W/wDg7r+EPi3Sj9ml8Wal4WuLs44aO6P9lT8DPWGNu2ck/Wv6La/nM/4LIj/hev8Awdk/BDwzpn7650HUfB9jdKnDR7Lv7fJk88iGbd06Y+tf0Z1y4O39mU7f8/KtvS8bfK2x1Yv/AH+V9/Z0vyYUUUUgPlj9sn/gin+zH/wUB+LMXjn4t/DGLxV4qisItMF+uvappzNbxs7IjJa3MSMQZG+ZlLYIGcAAcN8L/wDg27/Yl+EPjmy8Q6V8BNAu7/T9/lRa3q2p65YvvRkPmWl7czW8vDHHmRttbDDDKCPuCiiCUPg03/Hf7+o5Ny+LUqaDoNj4V0Oy0zTLK007TdOgS1tLS1hWGC1hRQqRxooCqiqAAoAAAAFeAeCv+CT37P8A8PP2z9Q/aE0j4fra/GDVJ7i5udfbWtRl3yXERimYWzzm2UshI+WIYzxg19FUUXfP7T7Wqv1s99fPr3JsuT2f2dNOmm2nl07HK/G/4JeFv2kfhH4h8CeN9Hg8QeE/FVlJp2qafM7ot1C4wV3IVdD0IZGDKQCpBANcN+xX+wB8If8Agnd8OdQ8J/Bzwbb+DdC1W/bU7yBL66vpLm4KJHvaa5lllOFRQF3bRg4Ayc+x0UR91tx3as/NLVJ+V9Rv3kk+mq8ntdfLQK+GPiJ/wbX/ALFPxW8f634n134KRXWt+I7+fU9Qni8V65bJNcTSNJI4iivFjQFmJ2oqqM4AA4r7nopcq5ua2v8AX+Q+Z25eh8qfsq/8EQP2Uf2KvGJ8Q/Dz4KeFdO19Z4Lq31LU5LnXbvTpoSxjktZb+Wd7VwXOWgKFsLuJ2rj6l1Cwh1WwntbiMS29zG0UqN0dWGCD9Qamopz9+PJPVdmKPuy546PufAL/APBrp+wo7kn4GDk548ZeIAPy+3V7B+xX/wAEav2bf+Cd/wAS7/xh8HvhsnhDxJqmnPpNzenXdT1FntWkjlaMLdXMqrl4ozlQD8o5xX09RTjKUXeLt/wdPyFJKXxanzan/BIn9nhP21z+0Sfh7v8AjGbz+0P+Egk13UnxP9n+zbxbG4NsMRfKB5WBgEAEZr6SoopL3YKmvhWy6L0G9ZOb3e76hXzx8X/+CUvwD+PX7W/h/wCOvizwH/avxU8KvZyaXrf9t6jB9la0cvbn7PHcLbtsYk/NGc9819D0ULSSmt4u6fVPuuzB6xcHs9Gu67M8j/bO/YS+FH/BQj4WW3gr4weEovGHhqz1CPVILVr66smhuUR0WRZbaWOQfLI4xuwQ3INdB+zR+zL4G/Y8+CGh/Dj4b6BD4Z8F+HEkj0/TY7ia4EAkleaQmSZ3kctJI7EuxOW613lFC91OK2er83td99NAl7zTlrbby9D54/ZD/wCCUvwB/YO+KPijxn8KPh/F4T8S+Momh1e8XV7+9+1I0vnFQlxPIkYMnOI1XoB0AFef/tJf8EAf2Rv2uvjZrvxF+IPwii1zxj4mlSfU7+PxJrFiLp0jWMMYre7jiB2ouSqDJGTkkmvseilyq0Vb4VZeS7LsvIfM7yf8zu/N933Z8afAj/g3u/Y0/Zw8bf8ACQ+GvgL4Vm1TyfJQ69dXviKCH50cPHBqE88SShkXEqoHUZAYBmB+y6KKtyk0ot6IlRSd0tWFfz4/ELwjP/wR1/4OxfD3iicS2vw6/aHvpXS5YFYf+JuxinjLZx+51Ly5CD92NozgZBr+g6ippfu8TTxC+zdNd4yVpR8r6a7q2g6nv4edB/as0+0k9JedtdPM8T/bc/4J1/Bz/go14M0bw98ZvB//AAmOkeH71tRsLf8Ata+0/wCzzlDGX3Ws0TN8rEYYkc9M12j/ALOHgqb9nT/hU0mhRTfD3/hH/wDhFjo8s8siNpv2f7P9nMhYyn918u4vv77s813FFLlThKm1pLddHpbXvppr00HzNSjNbx2fVa307a6+p4J+w5/wTC+Bf/BNyz8Rw/BbwJF4MXxa8D6sw1W+1GS8MAkEQL3c0rKF82TAUgfOSRmqfj//AIJS/AH4o/tlaV+0Drvw/iv/AIu6JLbT2WvHV7+Pynt02QsbZJxbMUXABaI9BnJAr6HoqnJucajfvR2fVW0Vn0stCeVcsoW0luuj9e5gfE/4U+F/jd4Fv/C/jPw3oPi3w1qgRb3Sda0+K/sbsI6yL5kMqsj7XVWGQcMoI5Ar4kT/AINff2Fo70Tj4Fxb1fzAD4v14pnOfuG+249sY7Yr76oqUlGXPHcptuPI9uxzfgP4N+EPhZ8Nrfwb4Z8LeHPDvhG1hkt4dD0zTYbTToopCxkRbeNRGFYuxYbcEsc9TXyr4b/4N5/2MfCfxp/4T+y+AfhQa+L2XUFhmur240lJZN2dumyTtYqg3HZGIAkeF2Ku1cfZ1FUm1U9qvi79fvJsuT2X2e3T7jwn4Gf8E0fgj+zX+0340+MfgjwRHoXxG+Ifn/8ACQasmq30w1Dz51nl/wBHkmaCPdKisfLjXkccEisvwV/wSe/Z/wDh5+2fqH7QmkfD9bX4wapPcXNzr7a1qMu+S4iMUzC2ec2ylkJHyxDGeMGvoqilH3eXl05U4ryT3S7J9VsOXvc3NrzO782tm+7Xc8a/bJ/4J7/Bf/goL4Lt9B+MXw90LxvZWLFrOa5Elvfafl0dhb3cDJcQBzGm8RyKHCgNuHFcv+xd/wAEj/2cv+Cemt32q/CH4VaF4V1nUFKS6rJPc6nqKRkANFHc3css0UTYBaON1RiASCRmvo2iiHuX5NL7+YT9+3NrbbyPnX4of8Env2f/AIz/ALYOjfHvxN8P11T4seH5rS40/XG1rUYxbva/8e7fZknW2bZgfeiOe+aXx1/wSf8A2f8A4mftmab+0Frnw/S/+L2kT21zZ682s6iohkt4xHC32ZZxbEooGMxHkAnJ5r6Jooh7nLyacrbXk3u12b6vdhL3ubm15kk/NLZPul0Wx4r+23/wTw+Dv/BRnwNpHhv4y+Dl8Y6NoN//AGnYQf2ne6ebe4MbRlw9rNE5Gx2G0sV6HGQCPSPhN8KdA+Bvwr8O+CvC1h/Znhjwnplvo+lWXnST/ZbWCNYoo98jM77UVRudixxySa6KihaRcVtLV+bta776aegPVpvpovJb/mfEfxZ/4Nxf2KPjT49vvEmtfAbQLfUtR2GZNF1XUtDssqgQFLSyuYbeMkKCxSMbmJZssST73+yl+w/8Gf8Agnp8M7rRfhT4D8PeAtEWPzb+a0iaS7vVjMjhrq6kL3FwU3ybTLI5VTtXA4r2Gij3owcKbt+XlpoOVpzU6mv5/efz6/8ABCTwPd/8FTP+C+vxs/a0uobk+DPBWo3kmhzSKwWaW5R7Kwiyx6x2CO7AfdYx8DcK/oKoopq0aNLDw+GnFRXd+b9X+FvUmV5VqleW83f07JeS/NsKKKKQwooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKAP/9k=")));

		return diagnosticReportBundle;
	}

	/**
	 * This method initiates loading of FHIR default profiles and NDHM profiles for validation 
	 * @throws IOException
	 */
	static void init() throws DataFormatException, IOException
	{
		/*
		 * Load NPM Package containing ABDM FHIR Profiles , Codesystems, ValueSets 
		 * Copy NPM Package.tgz (<package_name>.tgz) at "src/main/resource"
		 * Download Package : https://nrces.in/ndhm/fhir/r4/package.tgz
		 */

		NpmPackageValidationSupport npmValidationSupport = new NpmPackageValidationSupport(ctx);
		// If you possess a package with a distinct name, kindly modify the package name here correspondingly.
		try{
			npmValidationSupport.loadPackageFromClasspath("classpath:package.tgz");
		}catch(Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		// Create a chain that will hold our modules
		ValidationSupportChain validationsupportchain = new ValidationSupportChain(

				npmValidationSupport, new DefaultProfileValidationSupport(ctx),
				new InMemoryTerminologyServerValidationSupport(ctx), new CommonCodeSystemsTerminologyService(ctx),
				new SnapshotGeneratingValidationSupport(ctx));

		CachingValidationSupport validationSupport = new CachingValidationSupport(validationsupportchain);

		validator = ctx.newValidator();
		fhirInstanceValidator = new FhirInstanceValidator(validationSupport);
		validator.registerValidatorModule(fhirInstanceValidator);

	}

	
	/**
	 * This method validates the FHIR resources 
	 */
	static boolean validate(IBaseResource resource)
	{
		// Validate
		ValidationResult result = validator.validateWithResult(resource);

		// The result object now contains the validation results
		for (SingleValidationMessage next : result.getMessages()) {
			System.out.println(next.getSeverity().name() + " : " + next.getLocationString() + " " + next.getMessage());
		}

		return result.isSuccessful();
	}
}
