package com.param;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FilenameUtils;
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
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;

/**
 * The DiagnosticReportImagingMediaSample class populates, validates, parse and serializes Clinical Artifact - DiagnosticReport Imaging Media
 */
public class DiagnosticReportImagingMediaSample {

	// The FHIR context is the central starting point for the use of the HAPI FHIR API
	// It should be created once, and then used as a factory for various other types of objects (parsers, clients, etc.)
	static FhirContext ctx = FhirContext.forR4();

	static FhirInstanceValidator fhirInstanceValidator;
	static FhirValidator validator;

	public static void main(String[] args) throws DataFormatException, IOException
	{
		//Initialize validation support and loads all required profiles
		init();
		
		// Populate the resource
		Bundle diagnosticReportImagingMediaBundle = populateDiagnosticReportImagingMediaBundle();

		// Validate it. Validate method return result of validation in boolean
		// If validation result is true then parse, serialize operations are performed	
		if(validate(diagnosticReportImagingMediaBundle))
		{
			System.out.println("Validated populated DiagnosticReportImagingMedia bundle successfully");

			// Instantiate a new parser
			IParser parser; 

			// Enter file path (Eg: C://generatedexamples//bundle-prescriptionrecord.json)
			// Depending on file type xml/json instantiate the parser
			File file;
			Scanner scanner = new Scanner(System.in);
			System.out.println("\nEnter file path to write bundle");
			String filePath = scanner.nextLine();
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
				if(scanner!=null)
					scanner.close();
				return;
			}

			// Indent the output
			parser.setPrettyPrint(true);

			// Serialize populated bundle
			String serializeBundle = parser.encodeResourceToString(diagnosticReportImagingMediaBundle);

			// Write serialized bundle in xml/json file
			file = new File(filePath);
			file.createNewFile();	
			FileWriter writer = new FileWriter(file);
			writer.write(serializeBundle);
			writer.flush();
			writer.close();
			scanner.close();

			// Parse the xml/json file
			IBaseResource resource = parser.parseResource(new FileReader(new File(filePath)));

			// Validate Parsed file
			if(validate(resource)){
				System.out.println("Validated parsed file successfully");
			}
			else{
				System.out.println("Failed to validate parsed file");
			}
		}
		else
		{
			System.out.println("Failed to validate populated DiagnosticReportImagingMedia bundle");
		}
	}

	
	
	// Populate Composition for DiagnosticReport Media
		public static Composition populateDiagnosticReportRecordMediaCompositionResource() {
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
					"<div xmlns=\"http://www.w3.org/1999/xhtml\"><h4>Narrative with Details</h4>\r\n      <p><b>id:</b> 1</p>\r\n\t  <p><b>status:</b> final</p>\r\n\t  <p><b>category:</b> Computerized tomography service (Details : {http://snomed.info/sct} code '310128004' = 'Computerized tomography service')</p>\r\n\t  <p><b>code:</b> CT Head and Neck WO contrast (Details : {http://loinc.org} code '82692-5' = 'CT Head and Neck WO contrast')</p>\r\n\t  <p><b>subject:</b> ABC. Generated Summary: id: 1; Medical Record Number = 1234 (System : {https://healthid.ndhm.gov.in/}); active; ABC ; ph: +919818512600(HOME); gender: male; birthDate: 1981-01-12</p>\r\n\t  <p><b>issued:</b> 2020-07-09</p>\r\n\t  <p><b>performer:</b> XYZ Lab Pvt.Ltd.</p>\r\n\t  <p><b>resultInterpreter:</b> Dr. DEF. Generated Summary: id: 1; Medical License number = 7601003178999 (System : {doctor.ndhm.gov.in})</p>\n\t\t\t\t\t\t<h3>Diagnostic Report for ABC issued 9-July 2020 14:26</h3>\n\t\t\t\t\t\t<pre>\nTest: CT of head-neck\nConclusion: CT brains: large tumor sphenoid/clivus. </pre>\n\t\t\t\t\t\t<p>XYZ Lab Pvt.Ltd., Inc signed: Dr. DEF Radiologist</p>\n\t\t\t\t\t</div>");

			// Set version-independent identifier for the Composition
			Identifier identifier = composition.getIdentifier();
			identifier.setSystem("https://ndhm.in/phr");
			identifier.setValue("645bb0c3-ff7e-4123-bef5-3852a4784813");

			// Status can be preliminary | final | amended | entered-in-error
			composition.setStatus(CompositionStatus.FINAL);

			// Kind of composition ("Diagnostic studies report")
			composition.setType(
					new CodeableConcept(new Coding("http://snomed.info/sct", "721981007", "Diagnostic studies report")).setText("Diagnostic Report- Imaging Media"));

			// Set subject - Who and/or what the composition/DiagnosticReport record is
			// about
			composition.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));

			// Set Timestamp
			composition.setDateElement(new DateTimeType("2017-05-27T11:46:09+05:30"));

			// Set author - Who and/or what authored the composition/DiagnosticReport record
			composition.addAuthor(new Reference().setReference("urn:uuid:3bc96820-c7c9-4f59-900d-6b0ed1fa558e"));

			// Set a Human Readable name/title
			composition.setTitle("Diagnostic Report- Imaging Media");

			// Composition is broken into sections / DiagnosticReport Media record contains
			// single section to define the relevantDiagnosticReport Media
			// Entry is a reference to data that supports this section
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
	
	
	static Bundle populateDiagnosticReportImagingMediaBundle()
	{
		Bundle diagnosticReportBundle = new Bundle();

		// Set logical id of this artifact
		diagnosticReportBundle.setId("DiagnosticReport-Imaging-Media-example-02");

		// Set metadata about the resource - Version Id, Lastupdated Date, Profile
		Meta meta = diagnosticReportBundle.getMeta();
		meta.setVersionId("1");
		meta.setLastUpdatedElement(new InstantType("2020-07-09T15:32:26.605+05:30"));
		meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DocumentBundle");

		// Set Confidentiality as defined by affinity domain
		meta.addSecurity(new Coding("http://terminology.hl7.org/CodeSystem/v3-Confidentiality", "V", "very restricted"));

		// Set version-independent identifier for the Bundle
		Identifier identifier = diagnosticReportBundle.getIdentifier();
		identifier.setValue("9932f74d-2812-4be0-bd50-ed76eeab301d");
		identifier.setSystem("http://hip.in");

		// Set Bundle Type
		diagnosticReportBundle.setType(BundleType.DOCUMENT);

		// Set Timestamp
		diagnosticReportBundle.setTimestampElement(new InstantType("2020-07-09T15:32:26.605+05:30"));

		// Add resources entries for bundle with Full URL
		List<BundleEntryComponent> listBundleEntries = diagnosticReportBundle.getEntry();

		BundleEntryComponent bundleEntry1 = new BundleEntryComponent();
		bundleEntry1.setFullUrl("urn:uuid:df810c39-55e7-441c-8569-d6ab77aa1c66");
		bundleEntry1.setResource(populateDiagnosticReportRecordMediaCompositionResource());

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
		bundleEntry5.setResource(ResourcePopulator.populateDiagnosticReportImagingMediaResource());

		BundleEntryComponent bundleEntry6 = new BundleEntryComponent();
		bundleEntry6.setFullUrl("urn:uuid:a2398b75-d49c-4cfc-b579-569d7d2aef2d");
		bundleEntry6.setResource(ResourcePopulator.populateMediaResource());		

		BundleEntryComponent bundleEntry7 = new BundleEntryComponent();
		bundleEntry7.setFullUrl("urn:uuid:a7a398a8-ffbe-4636-906f-bbe03399e3ba");
		bundleEntry7.setResource(ResourcePopulator.populateServiceRequestResource());

		BundleEntryComponent bundleEntry8 = new BundleEntryComponent();
		bundleEntry8.setFullUrl("urn:uuid:947c60c9-20f6-41d8-9fce-b5da822a3087");
		bundleEntry8.setResource(ResourcePopulator.populateSecondPractitionerResource());

		BundleEntryComponent bundleEntry9 = new BundleEntryComponent();
		bundleEntry9.setFullUrl("urn:uuid:15851b05-ed16-4de2-832d-21ace5660d32");
		bundleEntry9.setResource(ResourcePopulator.populateDocumentReferenceResource());

		listBundleEntries.add(bundleEntry1);
		listBundleEntries.add(bundleEntry2);
		listBundleEntries.add(bundleEntry3);
		listBundleEntries.add(bundleEntry4);
		listBundleEntries.add(bundleEntry5);
		listBundleEntries.add(bundleEntry6);
		listBundleEntries.add(bundleEntry7);
		listBundleEntries.add(bundleEntry8);
		listBundleEntries.add(bundleEntry9);

		//Add Signature of Author
		diagnosticReportBundle.setSignature(new Signature().addType(new Coding("urn:iso-astm:E1762-95:2013", "1.2.840.10065.1.12.1.1", "Author's Signature")).setWhenElement(new InstantType("2020-07-09T07:42:33+10:00")).setWho(new Reference("urn:uuid:3bc96820-c7c9-4f59-900d-6b0ed1fa558e")).setSigFormat("image/jpeg").setDataElement(new Base64BinaryType("/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAIBAQIBAQICAgICAgICAwUDAwMDAwYEBAMFBwYHBwcGBwcICQsJCAgKCAcHCg0KCgsMDAwMBwkODw0MDgsMDAz/2wBDAQICAgMDAwYDAwYMCAcIDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAz/wAARCAA4AW8DASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Ea8Fae2oXqWUQlupwCFWKJGZVMjuyooZlXLDLKMkeU/8EvP+CsHwy/4K2fCTXvF/w1s/Fel23hrVP7Jv7HxFZw215FIYllRwIZpozGyscEPnKNkDAzTqOtOVt4pX8l8KfzGoKlFX2k3bzfxNH03X83P/AAbV/wDKxd8d/wDrz8V/+nm3r+kav5X/APgjv+3N8LP+Cff/AAXW+OPjj4veKP8AhEfC1zJ4m0qO9/s27v8AdcyatG6R+XaxSycrG5zt2jbyeRWeElGOYxcnb93W/GKSLxMXLBSSX26f5s/eL/gu/wDsu+Fv2qf+CVPxl0/xLptreT+FfDV94p0a5kT97pt/Y20k8U0bdVJCNG2PvJI6nhjX56f8GdvxZ1LxL/wTl+P3g+6uJptP8K6u17Yo7Fhbi8sG3omSQF3W27aABl2PJY4yf+C6H/B0V8Gvi1+x/wCK/hFDz4KeFdO19Z4Lq31LU5LnXbvTpoSxjktZbNsKKKKQwooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKAP/9k=")));


		return diagnosticReportBundle;
	}

	/**
	 * This method initiates loading of FHIR default profiles and NDHM profiles for validation 
	 * @throws IOException
	 */
	static void init() throws DataFormatException, IOException
	{

	/*
		 * Load NPM Package containing ABDM FHIR Profiles , Codesystem and ValueSets
		 * Copy NPM Package.tgz (<package_name>.tgz) at "src/main/resource"
		 * Download Package : https://nrces.in/ndhm/fhir/r4/package.tgz
		 */

		
		NpmPackageValidationSupport npmValidationSupport = new NpmPackageValidationSupport(ctx);
		// If you possess a package with a distinct name, kindly modify the package name here correspondingly.
		npmValidationSupport.loadPackageFromClasspath("classpath:package.tgz");

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
