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
import org.hl7.fhir.r4.model.Reference;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;

public class WellnessRecordSample {

	// The FHIR context is the central starting point for the use of the HAPI FHIR
	// API
	// It should be created once, and then used as a factory for various other types
	// of objects (parsers, clients, etc.)
	static FhirContext ctx = FhirContext.forR4();
	static FhirInstanceValidator fhirInstanceValidator;
	static FhirValidator validator;

	public static void main(String[] args) throws DataFormatException, IOException {
		// Initialize validation support and loads all required profiles
		init();

		Bundle WellnessRecordBundle = populateWellnessRecordBundle();

		if (true) {
			System.out.println("Validated populated WellnessRecord bundle successfully");

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
			String serializeBundle = parser.encodeResourceToString(WellnessRecordBundle);

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
			if (validate(resource)) {
				System.out.println("Validated parsed file successfully");
				System.out.println("wellness Record bundle"+serializeBundle);
			} else {
				System.out.println("Failed to validate parsed file");
			}
		} else {
			System.out.println("Failed to validate populated WellnessRecord bundle");
		}
	}

	// Populate Composition for WellnessRecord
	public static Composition populateWellnessRecordCompositionResource() {
		Composition composition = new Composition();

		// Set logical id of this artifact
		composition.setId("df810c39-55e7-441c-8569-d6ab77aa1c66");

		// Set metadata about the resource - Version Id, Lastupdated Date, Profile
		Meta meta = composition.getMeta();
		meta.setVersionId("1");
		meta.setLastUpdatedElement(new InstantType("2020-07-09T15:32:26.605+05:30"));
		meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/WellnessRecord");

		// Set language of the resource content
		composition.setLanguage("en-IN");

        //Set Narrative 
		composition.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-IN\" lang=\"en-IN\"><p><b>Generated Narrative with Details</b></p><p><b>id</b>: oxygen-saturation</p><p><b>meta</b>: </p><p><b>identifier</b>: o1223435-10</p><p><b>status</b>: final</p><p><b>category</b>: Vital Signs <span>(Details : {http://terminology.hl7.org/CodeSystem/observation-category code 'vital-signs' = 'Vital Signs', given as 'Vital Signs'})</span></p><p><b>code</b>: Oxygen saturation in Arterial blood <span>(Details : {LOINC code '2708-6' = 'Oxygen saturation in Arterial blood', given as 'Oxygen saturation in Arterial blood'}; )</span></p><p><b>subject</b>: <a>Patient/1</a></p><p><b>effective</b>: 29/09/2020 9:30:10 AM</p><p><b>value</b>: 95 %<span> (Details: UCUM code % = '%')</span></p><p><b>interpretation</b>: Normal (applies to non-numeric results) <span>(Details : {http://terminology.hl7.org/CodeSystem/v3-ObservationInterpretation code 'N' = 'Normal', given as 'Normal'})</span></p><p><b>device</b>: <a>DeviceMetric/example</a></p><h3>ReferenceRanges</h3><p><b>Low:</b> 90 %<span> (Details: UCUM code % = '%')</span></p><p><b>High:</b>99 %<span> (Details: UCUM code % = '%')</span></p></div>");

		// Set version-independent identifier for the Composition
		Identifier identifier = composition.getIdentifier();
		identifier.setSystem("https://ndhm.in/phr");
		identifier.setValue("645bb0c3-ff7e-4123-bef5-3852a4784813");

		// Status can be preliminary | final | amended | entered-in-error
		composition.setStatus(CompositionStatus.FINAL);

		// Kind of composition ("Wellness record")
		composition.getType().setText("Wellness Record");

		// Set subject - Who and/or what the composition/Wellness record is about
		composition.setSubject(new Reference().setReference("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe").setDisplay("ABC"));

		// Set Timestamp
		composition.setDateElement(new DateTimeType("2020-07-09T15:32:26.605+05:30"));

		// Set author - Who and/or what authored the composition/Wellness record
		composition.addAuthor(new Reference().setReference("urn:uuid:3bc96820-c7c9-4f59-900d-6b0ed1fa558e").setDisplay("Dr DEF"));

		// Set a Human Readable name/title
		composition.setTitle("Wellness Record");

		// Composition is broken into sections / wellness record contains single
		// section to define the relevant observation
		// Entry is a reference to data that supports this section
		SectionComponent section1 = new SectionComponent();
		section1.setTitle("Vital Signs");
		section1.addEntry(new Reference().setReference("urn:uuid:a51edaff-4f28-4597-b0c1-7a4d153b7ce6"))
				.addEntry(new Reference().setReference("urn:uuid:9d7e3f3a-c3e7-47e1-b46f-0a816aeea86d"))
				.addEntry(new Reference().setReference("urn:uuid:31259ff7-7bc5-48d5-bf4f-51cc4ee01c6b"))
				.addEntry(new Reference().setReference("urn:uuid:baa5f4ae-c86a-40b4-93f6-460c01fb944b"))
				.addEntry(new Reference().setReference("urn:uuid:9cca6bcd-75b6-4d1a-a562-01e139d8ad59"));

		SectionComponent section2 = new SectionComponent();
		section2.setTitle("Body Measurement");
		section2.addEntry(new Reference().setReference("urn:uuid:7d4e2fa9-bb97-4d0a-9d95-042048f2708b"))
				.addEntry(new Reference().setReference("urn:uuid:6f41dcd7-dfdb-46ae-bdf2-ddeb1d9d15f2"))
				.addEntry(new Reference().setReference("urn:uuid:69a050d0-6a98-42cc-82bc-183f46b9e6da"));

		SectionComponent section3 = new SectionComponent();
		section3.setTitle("Physical Activity");
		section3.addEntry(new Reference().setReference("urn:uuid:1d83373a-29cc-44b7-9db3-5afe03d0728d"))
				.addEntry(new Reference().setReference("urn:uuid:1ff6a4df-775a-4934-a035-b70e65c1b0d9"))
				.addEntry(new Reference().setReference("urn:uuid:4a0e0b0d-a941-4000-b944-4346054dc2c2"));

		SectionComponent section4 = new SectionComponent();
		section4.setTitle("General Assessment");
		section4.addEntry(new Reference().setReference("urn:uuid:566aff6a-b2f7-4407-8260-a2a961c50126"))
				.addEntry(new Reference().setReference("urn:uuid:db29db74-e263-42ef-b75e-2915db52d5a6"))
				.addEntry(new Reference().setReference("urn:uuid:fb589f2c-a3cd-48fd-86c8-9570aa8f7545"))
				.addEntry(new Reference().setReference("urn:uuid:0797230b-ee0d-4d64-bb26-81e39dd82a0a"));

		SectionComponent section5 = new SectionComponent();
		section5.setTitle("Women Health");
		section5.addEntry(new Reference().setReference("urn:uuid:230ade51-9daf-4467-895b-6a32a97c484c"))
				.addEntry(new Reference().setReference("urn:uuid:6b820f25-347f-4f19-abdf-ccebebce1ca5"));

		SectionComponent section6 = new SectionComponent();
		section6.setTitle("Lifestyle");
		section6.addEntry(new Reference().setReference("urn:uuid:3eb83708-d95b-47c2-994d-d384ffea27bd"))
				.addEntry(new Reference().setReference("urn:uuid:d2fbdc51-7f16-4791-a2b5-6c63d4d80ec5"));

		SectionComponent section7 = new SectionComponent();
		section7.setTitle("Document Reference");
		section7.addEntry(new Reference().setReference("urn:uuid:15851b05-ed16-4de2-832d-21ace5660d32"));

		composition.addSection(section1);
		composition.addSection(section2);
		composition.addSection(section3);
		composition.addSection(section4);
		composition.addSection(section5);
		composition.addSection(section6);
		composition.addSection(section7);

		return composition;

	}

	static Bundle populateWellnessRecordBundle() {

		Bundle WellnessRecordBundle = new Bundle();

		// Set logical id of this artifact
		WellnessRecordBundle.setId("WellnessRecord-example-01");

		// Set metadata about the resource - Version Id, Lastupdated Date, Profile
		Meta meta = WellnessRecordBundle.getMeta();
		meta.setVersionId("1");
		meta.setLastUpdatedElement(new InstantType("2020-07-09T15:32:26.605+05:30"));
		meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DocumentBundle");

		// Set Confidentiality as defined by affinity domain
		meta.addSecurity(
				new Coding("http://terminology.hl7.org/CodeSystem/v3-Confidentiality", "V", "very restricted"));

		// Set version-independent identifier for the Composition
		Identifier identifier = WellnessRecordBundle.getIdentifier();
		identifier.setSystem("http://hip.in");
		identifier.setValue("305fecc2-4ba2-46cc-9ccd-efa755aff51d");

		// Set Bundle Type
		WellnessRecordBundle.setType(BundleType.DOCUMENT);

		// Set Timestamp
		WellnessRecordBundle.setTimestampElement(new InstantType("2020-07-09T15:32:26.605+05:30"));

		// Add resources entries for bundle with Full URL

		List<BundleEntryComponent> listBundleEntries = WellnessRecordBundle.getEntry();

		BundleEntryComponent bundleEntry1 = new BundleEntryComponent();
		bundleEntry1.setFullUrl("urn:uuid:df810c39-55e7-441c-8569-d6ab77aa1c66");
		bundleEntry1.setResource(populateWellnessRecordCompositionResource());

		BundleEntryComponent bundleEntry2 = new BundleEntryComponent();
		bundleEntry2.setFullUrl("urn:uuid:3bc96820-c7c9-4f59-900d-6b0ed1fa558e");
		bundleEntry2.setResource(ResourcePopulator.populatePractitionerResource());

		BundleEntryComponent bundleEntry3 = new BundleEntryComponent();
		bundleEntry3.setFullUrl("urn:uuid:1efe03bf-9506-40ba-bc9a-80b0d5045afe");
		bundleEntry3.setResource(ResourcePopulator.populateSecondPatientResource());

		BundleEntryComponent bundleEntry4 = new BundleEntryComponent();
		bundleEntry4.setFullUrl("urn:uuid:a51edaff-4f28-4597-b0c1-7a4d153b7ce6");
		bundleEntry4.setResource(ResourcePopulator.populateRespitaryRateResource());

		BundleEntryComponent bundleEntry5 = new BundleEntryComponent();
		bundleEntry5.setFullUrl("urn:uuid:9d7e3f3a-c3e7-47e1-b46f-0a816aeea86d");
		bundleEntry5.setResource(ResourcePopulator.populateHeartRateResource());

		BundleEntryComponent bundleEntry6 = new BundleEntryComponent();
		bundleEntry6.setFullUrl("urn:uuid:baa5f4ae-c86a-40b4-93f6-460c01fb944b");
		bundleEntry6.setResource(ResourcePopulator.populateBodyTemperatureResource());

		BundleEntryComponent bundleEntry7 = new BundleEntryComponent();
		bundleEntry7.setFullUrl("urn:uuid:7d4e2fa9-bb97-4d0a-9d95-042048f2708b");
		bundleEntry7.setResource(ResourcePopulator.populateBodyHeightResource());

		BundleEntryComponent bundleEntry8 = new BundleEntryComponent();
		bundleEntry8.setFullUrl("urn:uuid:6f41dcd7-dfdb-46ae-bdf2-ddeb1d9d15f2");
		bundleEntry8.setResource(ResourcePopulator.populateBodyWeightResource());

		BundleEntryComponent bundleEntry9 = new BundleEntryComponent();
		bundleEntry9.setFullUrl("urn:uuid:69a050d0-6a98-42cc-82bc-183f46b9e6da");
		bundleEntry9.setResource(ResourcePopulator.populateBMIResource());

		BundleEntryComponent bundleEntry10 = new BundleEntryComponent();
		bundleEntry10.setFullUrl("urn:uuid:9cca6bcd-75b6-4d1a-a562-01e139d8ad59");
		bundleEntry10.setResource(ResourcePopulator.populateBloodPressureResource());

		BundleEntryComponent bundleEntry11 = new BundleEntryComponent();
		bundleEntry11.setFullUrl("urn:uuid:4a0e0b0d-a941-4000-b944-4346054dc2c2");
		bundleEntry11.setResource(ResourcePopulator.populateStepCountResource());

		BundleEntryComponent bundleEntry12 = new BundleEntryComponent();
		bundleEntry12.setFullUrl("urn:uuid:1d83373a-29cc-44b7-9db3-5afe03d0728d");
		bundleEntry12.setResource(ResourcePopulator.populateCaloriesBurnedResource());

		BundleEntryComponent bundleEntry13 = new BundleEntryComponent();
		bundleEntry13.setFullUrl("urn:uuid:1ff6a4df-775a-4934-a035-b70e65c1b0d9");
		bundleEntry13.setResource(ResourcePopulator.populateSleepDurationResource());

		BundleEntryComponent bundleEntry14 = new BundleEntryComponent();
		bundleEntry14.setFullUrl("urn:uuid:566aff6a-b2f7-4407-8260-a2a961c50126");
		bundleEntry14.setResource(ResourcePopulator.populateBodyFatMassResource());

		BundleEntryComponent bundleEntry15 = new BundleEntryComponent();
		bundleEntry15.setFullUrl("urn:uuid:db29db74-e263-42ef-b75e-2915db52d5a6");
		bundleEntry15.setResource(ResourcePopulator.populateBloodGlucoseResource());

		BundleEntryComponent bundleEntry16 = new BundleEntryComponent();
		bundleEntry16.setFullUrl("urn:uuid:0797230b-ee0d-4d64-bb26-81e39dd82a0a");
		bundleEntry16.setResource(ResourcePopulator.populateFluidIntakeResource());

		BundleEntryComponent bundleEntry17 = new BundleEntryComponent();
		bundleEntry17.setFullUrl("urn:uuid:fb589f2c-a3cd-48fd-86c8-9570aa8f7545");
		bundleEntry17.setResource(ResourcePopulator.populateCalorieIntakeResource());

		BundleEntryComponent bundleEntry18 = new BundleEntryComponent();
		bundleEntry18.setFullUrl("urn:uuid:230ade51-9daf-4467-895b-6a32a97c484c");
		bundleEntry18.setResource(ResourcePopulator.populateAgeOfMenarcheResource());

		BundleEntryComponent bundleEntry19 = new BundleEntryComponent();
		bundleEntry19.setFullUrl("urn:uuid:6b820f25-347f-4f19-abdf-ccebebce1ca5");
		bundleEntry19.setResource(ResourcePopulator.populateLastMenstrualPeriodResource());

		BundleEntryComponent bundleEntry20 = new BundleEntryComponent();
		bundleEntry20.setFullUrl("urn:uuid:3eb83708-d95b-47c2-994d-d384ffea27bd");
		bundleEntry20.setResource(ResourcePopulator.populateDietTypeResource());

		BundleEntryComponent bundleEntry21 = new BundleEntryComponent();
		bundleEntry21.setFullUrl("urn:uuid:d2fbdc51-7f16-4791-a2b5-6c63d4d80ec5");
		bundleEntry21.setResource(ResourcePopulator.populateTobaccoSmokingStatusResource());

        BundleEntryComponent bundleEntry22 = new BundleEntryComponent();
		bundleEntry22.setFullUrl("urn:uuid:31259ff7-7bc5-48d5-bf4f-51cc4ee01c6b");
		bundleEntry22.setResource(ResourcePopulator.populateOxygenSaturationResource());

		BundleEntryComponent bundleEntry23 = new BundleEntryComponent();
		bundleEntry23.setFullUrl("urn:uuid:6a7ce76d-8d49-4395-9a15-41abefa65430");
		bundleEntry23.setResource(ResourcePopulator.populateOrganizationResource());

		BundleEntryComponent bundleEntry24 = new BundleEntryComponent();
		bundleEntry24.setFullUrl("urn:uuid:364cad65-3038-40df-a8c6-2139d87e1b19");
		bundleEntry24.setResource(ResourcePopulator.populateSecondOrganizationResource());
		
		BundleEntryComponent bundleEntry25 = new BundleEntryComponent();
		bundleEntry25.setFullUrl("urn:uuid:15851b05-ed16-4de2-832d-21ace5660d32");
		bundleEntry25.setResource(ResourcePopulator.populateSecondDocumentReferenceResource());

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
		listBundleEntries.add(bundleEntry13);
		listBundleEntries.add(bundleEntry14);
		listBundleEntries.add(bundleEntry15);
		listBundleEntries.add(bundleEntry16);
		listBundleEntries.add(bundleEntry17);
		listBundleEntries.add(bundleEntry18);
		listBundleEntries.add(bundleEntry19);
		listBundleEntries.add(bundleEntry20);
		listBundleEntries.add(bundleEntry21);
		listBundleEntries.add(bundleEntry22);
		listBundleEntries.add(bundleEntry23);
		listBundleEntries.add(bundleEntry24);
        listBundleEntries.add(bundleEntry25);


		return WellnessRecordBundle;

	}

	/**
	 * This method initiates loading of FHIR default profiles and NDHM profiles for
	 * validation
	 * 
	 * @throws IOException
	 */
	static void init() throws DataFormatException, IOException {

		/*
		 * Load NPM Package containing ABDM FHIR Profiles, CodeSystem and ValueSets Copy
		 * NPM Package.tgz (<package_name>.tgz) at "src/main/resource" Download Package
		 * : https://nrces.in/ndhm/fhir/r4/package.tgz
		 */

		NpmPackageValidationSupport npmValidationSupport = new NpmPackageValidationSupport(ctx);
		/*
		 * If you possess a package with a distinct name, kindly modify the package name
		 * here correspondingly.
		 */
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
