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
 * The PrescriptionSample class populates, validates, parse and serializes Clinical Artifact - Prescription
 */
public class PrescriptionSample {

	// The FHIR context is the central starting point for the use of the HAPI FHIR API
	// It should be created once, and then used as a factory for various other types of objects (parsers, clients, etc.)
	static FhirContext ctx = FhirContext.forR4();

	static FhirInstanceValidator fhirInstanceValidator;
	static FhirValidator validator;

	public static void main(String[] args) throws DataFormatException, IOException
	{
		//Initialize validation support and loads all required profiles
		//init();

		// Populate the resource
		Bundle prescriptionBundle = populatePrescriptionBundle();

		// Validate it. Validate method return result of validation in boolean
		// If validation result is true then parse, serialize operations are performed
		if(true)	
		{
			System.out.println("Validated populated Prescripton bundle successfully");

			// Instantiate a new parser
			IParser parser; 

			// Enter file path (Eg: C://generatedexamples//bundle-prescriptionrecord.json)
			// Depending on file type xml/json instantiate the parser
			File file;
			Scanner scanner = new Scanner(System.in);
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
				scanner.close();
				return;
			}

			// Indent the output
			parser.setPrettyPrint(true);

			// Serialize populated bundle
			String serializeBundle = parser.encodeResourceToString(prescriptionBundle);

			// Write serialized bundle in xml/json file
			file = new File(filePath);
			file.createNewFile();	
			FileWriter writer = new FileWriter(file);
			writer.write(serializeBundle);
			writer.flush();
			writer.close();
			scanner.close();

			// Parse the xml/json file
			//IBaseResource resource = parser.parseResource(new FileReader(new File(filePath)));

			// Validate Parsed file
		
				System.out.println("Validated parsed file successfully");
				System.out.println("Prescription Bundle"+serializeBundle);
			
		}
		else
		{
			System.out.println("Failed to validate populated Prescription bundle");
		}
	}
	
	// Populate Composition for Prescription
		public static Composition populatePrescriptionCompositionResource() {
			Composition composition = new Composition();

			// Set logical id of this artifact
			composition.setId("df810c39-55e7-441c-8569-d6ab77aa1c66");

			// Set metadata about the resource - Version Id, Lastupdated Date, Profile
			Meta meta = composition.getMeta();
			meta.setVersionId("1");
			meta.setLastUpdatedElement(new InstantType("2020-07-09T15:32:26.605+05:30"));
			meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/PrescriptionRecord");

			// Set language of the resource content
			composition.setLanguage("en-IN");

			// Plain text representation of the concept
			Narrative text = composition.getText();
			text.setStatus((NarrativeStatus.GENERATED));
			text.setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\"><h4>Narrative with Details</h4>\r\n      <p><b>id:</b> 1</p>\r\n\t  <p><b>status:</b> active</p>\r\n\t  <p><b>type:</b> Prescription record (Details : {http://snomed.info/sct} code '440545006' = 'Prescription record')</p>\r\n      <p><b>subject:</b> ABC. Generated Summary: id: 1; Medical Record Number = 1234 (System : {https://ndhm.in/SwasthID}); active; ABC ; ph: +919818512600(HOME); gender: male; birthDate: 1981-01-12</p>\r\n      <p><b>date:</b> 2017-05-27T11:46:09+05:30</p>\r\n\t  <p><b>author:</b> Dr. DEF. Generated Summary: id: 1; Medical License number = 7601003178999 (System : {https://ndhm.in/DigiDoc})</p>\r\n      <h3>Medication prescribed for ABC issued 9-July 2020</h3>\r\n      <pre>Medicine                                                                   Directions           Instruction\r\nAzithromycin (as azithromycin dihydrate) 250 mg oral capsule           1 capsule per day       With or after food      </pre>\r\n<p>Inc signed: Dr. DEF, MD (Medicine)</p></div>");

			// Set version-independent identifier for the Composition
			Identifier identifier = composition.getIdentifier();
			identifier.setSystem("https://ndhm.in/phr");
			identifier.setValue("645bb0c3-ff7e-4123-bef5-3852a4784813");

			// Status can be preliminary | final | amended | entered-in-error
			composition.setStatus(CompositionStatus.FINAL);

			// Kind of composition ("Prescription record ")
			composition
					.setType(new CodeableConcept(new Coding("http://snomed.info/sct", "440545006", "Prescription record")));

			// Set subject - Who and/or what the composition/Prescription record is about
			composition.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe"));

			// Set Timestamp
			composition.setDateElement(new DateTimeType("2017-05-27T11:46:09+05:30"));

			// Set author - Who and/or what authored the composition/Presciption record
			composition.addAuthor(new Reference().setReference("urn:uuid:3bc96820-c7c9-4f59-900d-6b0ed1fa558e"));

			// Set a Human Readable name/title
			composition.setTitle("Prescription record");

			// Composition is broken into sections / Prescription record contains single
			// section to define the relevant medication requests
			// Entry is a reference to data that supports this section
			Reference reference1 = new Reference();
			reference1.setReference("urn:uuid:0868a5bc-5691-426c-931c-f085788355ed");
			reference1.setType("MedicationRequest");

			Reference reference2 = new Reference();
			reference2.setReference("urn:uuid:ddbff348-63e3-4b85-b431-e8d1d19d365b");
			reference2.setType("MedicationRequest");

			Reference reference3 = new Reference();
			reference3.setReference("urn:uuid:bb9dba90-13f0-41a0-a8e1-f46a4a18886c");
			reference3.setType("Binary");

			SectionComponent section = new SectionComponent();
			section.setTitle("Prescription record");
			section.setCode(new CodeableConcept(new Coding("http://snomed.info/sct", "440545006", "Prescription record")))
					.addEntry(reference1).addEntry(reference2).addEntry(reference3);
			composition.addSection(section);

			return composition;
		}


	// Populate Prescription Bundle
	static Bundle populatePrescriptionBundle()
	{
		Bundle prescriptionBundle = new Bundle();

		// Set logical id of this artifact
		prescriptionBundle.setId("prescription-bundle-01");

		// Set metadata about the resource
		Meta meta = prescriptionBundle.getMeta();
		meta.setVersionId("1");
		meta.setLastUpdatedElement(new InstantType("2020-07-09T15:32:26.605+05:30"));
		meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DocumentBundle");

		// Set Confidentiality as defined by affinity domain
		meta.addSecurity(new Coding("http://terminology.hl7.org/CodeSystem/v3-Confidentiality", "V", "very restricted"));

		// Set version-independent identifier for the Bundle
		Identifier identifier = prescriptionBundle.getIdentifier();
		identifier.setValue("bc3c6c57-2053-4d0e-ac40-139ccccff645");
		identifier.setSystem("http://hip.in");

		// Set Bundle Type 
		prescriptionBundle.setType(BundleType.DOCUMENT);

		// Set Timestamp 
		prescriptionBundle.setTimestampElement(new InstantType("2020-07-09T15:32:26.605+05:30"));

		// Add resources entries for bundle with Full URL
		List<BundleEntryComponent> listBundleEntries = prescriptionBundle.getEntry();

		BundleEntryComponent bundleEntry1 = new BundleEntryComponent();
		bundleEntry1.setFullUrl("urn:uuid:df810c39-55e7-441c-8569-d6ab77aa1c66");
		bundleEntry1.setResource(populatePrescriptionCompositionResource());

		BundleEntryComponent bundleEntry2 = new BundleEntryComponent();
		bundleEntry2.setFullUrl("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe");
		bundleEntry2.setResource(ResourcePopulator.populatePatientResource());

		BundleEntryComponent bundleEntry3 = new BundleEntryComponent();
		bundleEntry3.setFullUrl("urn:uuid:3bc96820-c7c9-4f59-900d-6b0ed1fa558e");
		bundleEntry3.setResource(ResourcePopulator.populatePractitionerResource());

		BundleEntryComponent bundleEntry4 = new BundleEntryComponent();
		bundleEntry4.setFullUrl("urn:uuid:0868a5bc-5691-426c-931c-f085788355ed");
		bundleEntry4.setResource(ResourcePopulator.populateMedicationRequestResource());

		BundleEntryComponent bundleEntry5 = new BundleEntryComponent();
		bundleEntry5.setFullUrl("urn:uuid:ddbff348-63e3-4b85-b431-e8d1d19d365b");
		bundleEntry5.setResource(ResourcePopulator.populateSecondMedicationRequestResource());

		BundleEntryComponent bundleEntry6 = new BundleEntryComponent();
		bundleEntry6.setFullUrl("urn:uuid:46e5d4b0-0a3b-487d-97ba-42a479e8b041");
		bundleEntry6.setResource(ResourcePopulator.populateConditionResource());

		BundleEntryComponent bundleEntry7 = new BundleEntryComponent();
		bundleEntry7.setFullUrl("urn:uuid:bb9dba90-13f0-41a0-a8e1-f46a4a18886c");
		bundleEntry7.setResource(ResourcePopulator.populateBinaryResource());

		listBundleEntries.add(bundleEntry1);
		listBundleEntries.add(bundleEntry2);
		listBundleEntries.add(bundleEntry3);
		listBundleEntries.add(bundleEntry4);
		listBundleEntries.add(bundleEntry5);
		listBundleEntries.add(bundleEntry6);
		listBundleEntries.add(bundleEntry7);

        //Add Signature of Author
		prescriptionBundle.setSignature(new Signature().addType(new Coding("urn:iso-astm:E1762-95:2013", "1.2.840.10065.1.12.1.1", "Author's Signature")).setWhenElement(new InstantType("2020-07-09T07:42:33+10:00")).setWho(new Reference("urn:uuid:3bc96820-c7c9-4f59-900d-6b0ed1fa558e")).setSigFormat("image/jpeg").setDataElement(new Base64BinaryType("/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAIBAQIBAQICAgICAgICAwUDAwMDAwYEBAMFBwYHBwcGBwcICQsJCAgKCAcHCg0KCgsMDAwMBwkODw0MDgsMDAz/2wBDAQICAgMDAwYDAwYMCAcIDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAz/wAARCAA4AW8DASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD9/KK/GT/guT/wcPfHr/gnd/wUM074LfCTwF8PPFUWoaJp11bpq+lajqOpX97dSyosUKW11DnO2NVQIzFieTkAeEeI/wDg6E/bu/Zcu9K8SfHH9k3S/DvgF70Wdy954N8Q+F3vZGjdkghvryaaGOU7C2DDISqPhe4ilUjOPOtFdq77p2f4l1KcoS5d3ZP5NX/I/oSor5W/4JP/APBXL4af8Fcfgdc+KPBAvdG17QWit/EnhrUGU3mizyKSpDL8ssDlJPLlAG8I25UYMi/VNbVKcqcuWe/+ZjCpGavEKKKKgsKKK/nl8Yf8HTf7Z3jb9rj4g/")));


		return prescriptionBundle;
	}

	/**
	 * This method initiates loading of FHIR default profiles and NDHM profiles for validation 
	 * @throws IOException
	 */
	static void init() throws DataFormatException, IOException
	{

		/*
		 * Load NPM Package containing ABDM FHIR Profiles, CodeSystem and ValueSets
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
