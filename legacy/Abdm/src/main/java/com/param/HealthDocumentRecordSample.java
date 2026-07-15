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

public class HealthDocumentRecordSample {

	// The FHIR context is the central starting point for the use of the HAPI FHIR
	// API
	// It should be created once, and then used as a factory for various other types
	// of objects (parsers, clients, etc.)
	static FhirContext ctx = FhirContext.forR4();
	static FhirInstanceValidator fhirInstanceValidator;
	static FhirValidator validator;

	public static void main(String[] args) throws DataFormatException,IOException {

		// Initialize validation support and loads all required profiles
		init();

		Bundle populateHealthDocumentRecordBundle = populateHealthDocumentRecordBundle();

		if (true) {
			System.out.println("Validated populated HealthDocumentRecord bundle successfully");

			// Instantiate a new parser
			IParser parser;

			// Enter file path (Eg: C://generatedexamples//bundle-prescriptionrecord.json)
			// Depending on file type xml/json instantiate the parser
			File file;
			Scanner scanner = new Scanner(System.in);
			System.out.println("\nEnter file path to write bundle");
			String filePath ="D://Diagnostics.json";//scanner.nextLine();
			if (FilenameUtils.getExtension(filePath).equals("json")) {
				parser = ctx.newJsonParser();
			} else if (FilenameUtils.getExtension(filePath).equals("xml")) {
				parser = ctx.newXmlParser();
			} else {
				System.out.println("Invalid file extension!");
				if (scanner != null)
					scanner.close();
				return;
			}

			// Indent the output
			parser.setPrettyPrint(true);

			// Serialize populated bundle
			String serializeBundle = parser.encodeResourceToString(populateHealthDocumentRecordBundle);

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
				System.out.println("Validated parsed file successfully");
				System.out.println("Health Doccument "+serializeBundle);
		} else {
			System.out.println("Failed to validate populated HealthDocumentRecord bundle");
		}
	}
	
	// Populate Composition for HealthDocumentRecord
		public static Composition populateHealthDocumentRecordCompositionResource() {
			Composition composition = new Composition();

			// Set logical id of this artifact
			composition.setId("df810c39-55e7-441c-8569-d6ab77aa1c66");

			// Set metadata about the resource - Version Id, Lastupdated Date, Profile
			Meta meta = composition.getMeta();
			meta.setVersionId("1");
			meta.setLastUpdatedElement(new InstantType("2020-07-09T15:32:26.605+05:30"));
			meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/HealthDocumentRecord");

			// Set language of the resource content
			composition.setLanguage("en-IN");

			// Plain text representation of the concept
			Narrative text = composition.getText();
			text.setStatus(NarrativeStatus.GENERATED);
			text.setDivAsString(
				"<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\"><h4>Narrative with Details</h4><p>This is a Health Document of Patient ABC. Generated Summary: id: 1; Medical Record Number = 1234 (System : {https://healthid.ndhm.gov.in}); active; ABC ; ph: +919818512600(HOME); gender: male; birthDate: 1981-01-12</p></div>");
            composition.setText(text);
			// Set version-independent identifier for the Composition
			Identifier identifier = composition.getIdentifier();
			identifier.setSystem("https://ndhm.in/phr");
			identifier.setValue("645bb0c3-ff7e-4123-bef5-3852a4784813");
			
			// Status can be preliminary | final | amended | entered-in-error
			composition.setStatus(CompositionStatus.FINAL);

			// Kind of composition ("HealthDocument record")
			CodeableConcept type = composition.getType();
			type.addCoding(new Coding("http://snomed.info/sct", "419891008", "Record artifact"));
			type.setText("Record artifact");

			// Set subject - Who and/or what the composition/HealthDocument record is about
			composition.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe").setDisplay("ABC"));

			// Set Timestamp
			composition.setDateElement(new DateTimeType("2020-07-09T15:32:26.605+05:30"));

			// Set author - Who and/or what authored the composition/HealthDocument record
			composition.addAuthor(new Reference().setReference("urn:uuid:3bc96820-c7c9-4f59-900d-6b0ed1fa558e").setDisplay("Dr DEF"));

			// Set a Human Readable name/title
			composition.setTitle("Health Document");

			Reference reference1 = new Reference();
			reference1.setReference("urn:uuid:15851b05-ed16-4de2-832d-21ace5660d32");
			reference1.setType("DocumentReference");

			SectionComponent section = new SectionComponent();
			section.setTitle("HeathDocument record");
			section.addEntry(reference1);
			composition.addSection(section);

			return composition;

		}


	static Bundle populateHealthDocumentRecordBundle() {

		Bundle HealthDocumentRecordBundle = new Bundle();

		// Set logical id of this artifact
		HealthDocumentRecordBundle.setId("HealthDocumentRecord-example-01");

		// Set metadata about the resource - Version Id, Lastupdated Date, Profile
		Meta meta = HealthDocumentRecordBundle.getMeta();
		meta.setVersionId("1");
		meta.setLastUpdatedElement(new InstantType("2020-07-09T15:32:26.605+05:30"));
		meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DocumentBundle");

		// Set Confidentiality as defined by affinity domain
		meta.addSecurity(new Coding("http://terminology.hl7.org/CodeSystem/v3-Confidentiality", "V", "very restricted"));

		// Set version-independent identifier for the Composition
		Identifier identifier = HealthDocumentRecordBundle.getIdentifier();
		identifier.setSystem("http://hip.in");
		identifier.setValue("305fecc2-4ba2-46cc-9ccd-efa755aff51d");

		// Set Bundle Type
		HealthDocumentRecordBundle.setType(BundleType.DOCUMENT);

		// Set Timestamp
		HealthDocumentRecordBundle.setTimestampElement(new InstantType("2020-07-09T15:32:26.605+05:30"));

		// Add resources entries for bundle with Full URL

		List<BundleEntryComponent> listBundleEntries = HealthDocumentRecordBundle.getEntry();

		BundleEntryComponent bundleEntry1 = new BundleEntryComponent();
		bundleEntry1.setFullUrl("urn:uuid:df810c39-55e7-441c-8569-d6ab77aa1c66");
		bundleEntry1.setResource(populateHealthDocumentRecordCompositionResource());
		
		BundleEntryComponent bundleEntry2 = new BundleEntryComponent();
		bundleEntry2.setFullUrl("urn:uuid:3bc96820-c7c9-4f59-900d-6b0ed1fa558e");
		bundleEntry2.setResource(ResourcePopulator.populatePractitionerResource());

		BundleEntryComponent bundleEntry3 = new BundleEntryComponent();
		bundleEntry3.setFullUrl("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe");
		bundleEntry3.setResource(ResourcePopulator.populatePatientResource());

		BundleEntryComponent bundleEntry4 = new BundleEntryComponent();
		bundleEntry4.setFullUrl("urn:uuid:15851b05-ed16-4de2-832d-21ace5660d32");
		bundleEntry4.setResource(ResourcePopulator.populateSecondDocumentReferenceResource());

		listBundleEntries.add(bundleEntry1);
		listBundleEntries.add(bundleEntry2);
		listBundleEntries.add(bundleEntry3);
		listBundleEntries.add(bundleEntry4);
		
        //Add Signature of Author
		HealthDocumentRecordBundle.setSignature(new Signature().addType(new Coding("urn:iso-astm:E1762-95:2013", "1.2.840.10065.1.12.1.1", "Author's Signature")).setWhenElement(new InstantType("2020-07-09T07:42:33+10:00")).setWho(new Reference("urn:uuid:3bc96820-c7c9-4f59-900d-6b0ed1fa558e")).setSigFormat("image/jpeg").setDataElement(new Base64BinaryType("/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAIBAQIBAQICAgICAgICAwUDAwMDAwYEBAMFBwYHBwcGBwcICQsJCAgKCAcHCg0KCgsMDAwMBwkODw0MDgsMDAz/2wBDAQICAgMDAwYDAwYMCAcIDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAz/wAARCAA4AW8DASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD9/KK/GT/guT/wcPfHr/gnd/wUM074LfCTwF8PPFUWoaJp11bpq+lajqOpX97dSyosUKW11DnO2NVQIzFieTkAeEeI/wDg6E/bu/Zcu9K8SfHH9k3S/DvgF70Wdy954N8Q+F3vZGjdkghvryaaGOU7C2DDISqPhe4ilUjOPOtFdq77p2f4l1KcoS5d3ZP5NX/I/oSor5W/4JP/APBXL4af8Fcfgdc+KPBAvdG17QWit/EnhrUGU3mizyKSpDL8ssDlJPLlAG8I25UYMi/VNbVKcqcuWe/+ZjCpGavEKKKKgsKKK/nl8Yf8HTf7Z3jb9rj4g/")));


		return HealthDocumentRecordBundle;

	}



	/**
	 * This method initiates loading of FHIR default profiles and NDHM profiles for
	 * validation
	 * @throws IOException
	 */
	static void init() throws DataFormatException, IOException {

		/*
		 * Load NPM Package containing ABDM FHIR Profiles, CodeSystem and ValueSets
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
	static boolean validate(IBaseResource resource) {
		// Validate
		ValidationResult result = validator.validateWithResult(resource);

		// The result object now contains the validation results
		for (SingleValidationMessage next : result.getMessages()) {
			System.out.println(next.getSeverity().name() + " : " + next.getLocationString() + " " + next.getMessage());
		}

		return result.isSuccessful();
	}

}
