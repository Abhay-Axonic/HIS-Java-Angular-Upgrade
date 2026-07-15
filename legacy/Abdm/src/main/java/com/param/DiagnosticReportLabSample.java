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
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Composition.CompositionStatus;
import org.hl7.fhir.r4.model.Composition.SectionComponent;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Signature;
import org.hl7.fhir.r4.model.Base64BinaryType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;

/**
 * The DiagnosticReportLabSample class populates, validates, parse and serializes Clinical Artifact - DiagnosticReport Lab
 */
public class DiagnosticReportLabSample {

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
		Bundle diagnosticReportLabBundle = populateDiagnosticReportLabBundle();

		// Validate it. Validate method return result of validation in boolean
		// If validation result is true then parse, serialize operations are performed	
		if(validate(diagnosticReportLabBundle))
		{
			System.out.println("Validated populated DiagnosticReportLab bundle successfully");

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
			String serializeBundle = parser.encodeResourceToString(diagnosticReportLabBundle);

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
			System.out.println("Failed to validate populated DiagnosticReportLab bundle");
		}
	}
	
	// Populate Composition for DiagnosticReport Lab
		public static Composition populateDiagnosticReportRecordLabCompositionResource() {
			Composition composition = new Composition();

			// Set logical id of this artifact
			composition.setId("df810c39-55e7-441c-8569-d6ab77aa1c66");

			// Set metadata about the resource - Version Id, Lastupdated Date, Profile
			Meta meta = composition.getMeta();
			meta.setVersionId("1");
			meta.setLastUpdatedElement(new InstantType("2020-07-09T15:32:26.605+05:30"));
			meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DiagnosticReportRecord");

		    composition.setLanguage("en-IN");

			// Set version-independent identifier for the Composition
			Identifier identifier = composition.getIdentifier();
			identifier.setSystem("https://ndhm.in/phr");
			identifier.setValue("645bb0c3-ff7e-4123-bef5-3852a4784813");

			// Status can be preliminary | final | amended | entered-in-error
			composition.setStatus(CompositionStatus.FINAL);

			// Kind of composition ("Diagnostic studies report")
			CodeableConcept type = composition.getType();
			type.addCoding(new Coding("http://snomed.info/sct", "721981007", "Diagnostic studies report"));
			type.setText("Diagnostic Report- Lab");

			// Set subject - Who and/or what the composition/DiagnosticReport record is
			// about
			composition.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));

			// Set Timestamp
			composition.setDateElement(new DateTimeType("2017-05-27T11:46:09+05:30"));

			// Set author - Who and/or what authored the composition/DiagnosticReport record
			composition.addAuthor(new Reference().setReference("urn:uuid:3bc96820-c7c9-4f59-900d-6b0ed1fa558e"));

			// Set a Human Readable name/title
			composition.setTitle("Diagnostic Report- Lab");

			// Composition is broken into sections / DiagnosticReport Lab record contains
			// single section to define the relevant DiagnosticReport
			// Entry is a reference to data that supports this section
			Reference reference1 = new Reference();
			reference1.setReference("urn:uuid:0a57316f-7d56-424c-b03b-4a4664127d6a");
			reference1.setType("DiagnosticReport");

			Reference reference2 = new Reference();
			reference2.setReference("urn:uuid:15851b05-ed16-4de2-832d-21ace5660d32");
			reference2.setType("DocumentReference");

			SectionComponent section = new SectionComponent();
			section.setTitle("Hematology report");
			section.setCode(new CodeableConcept(new Coding("http://snomed.info/sct", "4321000179101", "Hematology report")))
					.addEntry(reference1).addEntry(reference2);
			composition.addSection(section);

			return composition;
		}

	static Bundle populateDiagnosticReportLabBundle()
	{
		Bundle diagnosticReportBundle = new Bundle();

		// Set logical id of this artifact
		diagnosticReportBundle.setId("DiagnosticReport-Lab-example-01");

		// Set metadata about the resource - Version Id, Lastupdated Date, Profile
		Meta meta = diagnosticReportBundle.getMeta();
		meta.setVersionId("1");
		meta.setLastUpdatedElement(new InstantType("2020-07-09T15:32:26.605+05:30"));
		meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DocumentBundle");

		// Set Confidentiality as defined by affinity domain
		meta.addSecurity(new Coding("http://terminology.hl7.org/CodeSystem/v3-Confidentiality", "V", "very restricted"));

		// Set version-independent identifier for the Composition
		Identifier identifier = diagnosticReportBundle.getIdentifier();
		identifier.setValue("3cf54fc4-0178-4127-bb99-b20711404881");
		identifier.setSystem("http://hip.in");

		// Set Bundle Type
		diagnosticReportBundle.setType(BundleType.DOCUMENT);

		// Set Timestamp
		diagnosticReportBundle.setTimestampElement(new InstantType("2020-07-09T15:32:26.605+05:30"));

		// Add resources entries for bundle with Full URL
		List<BundleEntryComponent> listBundleEntries = diagnosticReportBundle.getEntry();

		BundleEntryComponent bundleEntry1 = new BundleEntryComponent();
		bundleEntry1.setFullUrl("urn:uuid:df810c39-55e7-441c-8569-d6ab77aa1c66");
		bundleEntry1.setResource(populateDiagnosticReportRecordLabCompositionResource());

		BundleEntryComponent bundleEntry2 = new BundleEntryComponent();
		bundleEntry2.setFullUrl("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe");
		bundleEntry2.setResource(ResourcePopulator.populatePatientResource());

		BundleEntryComponent bundleEntry3 = new BundleEntryComponent();
		bundleEntry3.setFullUrl("urn:uuid:3bc96820-c7c9-4f59-900d-6b0ed1fa558e");
		bundleEntry3.setResource(ResourcePopulator.populatePractitionerResource());

		BundleEntryComponent bundleEntry4 = new BundleEntryComponent();
		bundleEntry4.setFullUrl("urn:uuid:0a57316f-7d56-424c-b03b-4a4664127d6a");
		bundleEntry4.setResource(ResourcePopulator.populateDiagnosticReportLabResource());

		BundleEntryComponent bundleEntry5 = new BundleEntryComponent();
		bundleEntry5.setFullUrl("urn:uuid:6a7ce76d-8d49-4395-9a15-41abefa65430");
		bundleEntry5.setResource(ResourcePopulator.populateOrganizationResource());

		BundleEntryComponent bundleEntry6 = new BundleEntryComponent();
		bundleEntry6.setFullUrl("urn:uuid:15851b05-ed16-4de2-832d-21ace5660d32");
		bundleEntry6.setResource(ResourcePopulator.populateDocumentReferenceResource());
		
		BundleEntryComponent bundleEntry7 = new BundleEntryComponent();
		bundleEntry7.setFullUrl("urn:uuid:46923585-2f65-4deb-8c51-56b2a5b5c14b");
		bundleEntry7.setResource(ResourcePopulator.populateCholesterolObservationResource());
		
		BundleEntryComponent bundleEntry8 = new BundleEntryComponent();
		bundleEntry8.setFullUrl("urn:uuid:969f3ed4-f0e2-48ff-ae34-e72b6a3ae9d6");
		bundleEntry8.setResource(ResourcePopulator.populateTriglycerideObservationResource());
		
		BundleEntryComponent bundleEntry9 = new BundleEntryComponent();
		bundleEntry9.setFullUrl("urn:uuid:bdada6cb-c93e-42a8-b7fe-0a2490800cfd");
		bundleEntry9.setResource(ResourcePopulator.populateCholesterolInHDLObservationResource());
		
		BundleEntryComponent bundleEntry10 = new BundleEntryComponent();
		bundleEntry10.setFullUrl("urn:uuid:e0702f22-e242-47a1-81aa-3cafe82669e2");
		bundleEntry10.setResource(ResourcePopulator.populateSpecimenResource());

		BundleEntryComponent bundleEntry11 = new BundleEntryComponent();
		bundleEntry11.setFullUrl("urn:uuid:a7a398a8-ffbe-4636-906f-bbe03399e3ba");
		bundleEntry11.setResource(ResourcePopulator.populateThirdServiceRequestResource());

		BundleEntryComponent bundleEntry12 = new BundleEntryComponent();
		bundleEntry12.setFullUrl("urn:uuid:947c60c9-20f6-41d8-9fce-b5da822a3087");
		bundleEntry12.setResource(ResourcePopulator.populateSecondPractitionerResource());

	
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
		listBundleEntries.add(bundleEntry11);
		listBundleEntries.add(bundleEntry12);


		diagnosticReportBundle.setSignature(new Signature(new InstantType("2020-07-09T07:42:33+10:00"), new Reference("Practitioner-01")).addType(new Coding("urn:iso-astm:E1762-95:2013", "1.2.840.10065.1.12.1.1", "Author's Signature")).setSigFormat("image/jpeg").setDataElement(new Base64BinaryType("/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAIBAQIBAQICAgICAgICAwUDAwMDAwYEBAMFBwYHBwcGBwcICQsJCAgKCAcHCg0KCgsMDAwMBwkODw0MDgsMDAz/2wBDAQICAgMDAwYDAwYMCAcIDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAz/wAARCAA4AW8DASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD9/KK/GT/guT/wcPfHr/gnd/wUM074LfCTwF8PPFUWoaJp11bpq+lajqOpX97dSyosUKW11DnO2NVQIzFieTkAeEeI/wDg6E/bu/Zcu9K8SfHH9k3S//9k=")));
		return diagnosticReportBundle;
	}

	/**
	 * This method initiates loading of FHIR default profiles and NDHM profiles for validation 
	 * @throws IOException
	 */
	static void init() throws DataFormatException, IOException
	{

		/*
		 * Load NPM Package containing ABDM FHIR Profiles
		 * Copy NPM Package.tgz (<package_name>.tgz) at "src/main/resource"
		 * Download Package : https://nrces.in/ndhm/fhir/r4/package.tgz
		 */

		NpmPackageValidationSupport npmValidationSupport = new NpmPackageValidationSupport(ctx);
		// If you possess a package with a distinct name, kindly modify the package name here correspondingly.
		npmValidationSupport.loadPackageFromClasspath("classpath:<Enter the package name>");
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
