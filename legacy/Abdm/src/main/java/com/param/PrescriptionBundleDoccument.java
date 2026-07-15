package com.param;

import java.util.List;

import org.hl7.fhir.r4.formats.IParser;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Patient;

import ca.uhn.fhir.context.FhirContext;

public class PrescriptionBundleDoccument {

	public static Bundle  populatePresccriptionBundle(){
		
		Bundle presccriptionBundle = new Bundle();
		presccriptionBundle.setId("prescription-bundle-01");


		Meta meta =presccriptionBundle.getMeta();
		meta.setVersionId("1");
		meta.setLastUpdatedElement(new InstantType("2020-07-09T15:32:26.605+05:30"));
		meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DocumentBundle");
		meta.addSecurity(new Coding("http://terminology.hl7.org/CodeSystem/v3-Confidentiality","V","very restricted"));
		Identifier identifier  = presccriptionBundle.getIdentifier();
		
		identifier.setValue("134cb222-1f6a-4458-b7c6-6b22377821d9");
		identifier.setSystem("http://hip.in");
		presccriptionBundle.setType(BundleType.DOCUMENT);
		presccriptionBundle.setTimestampElement(new InstantType("2020-07-09T15:32:26.605+05:30"));
		List<BundleEntryComponent>  listBundleEntry =presccriptionBundle.getEntry();
		
		BundleEntryComponent bundleEntryComponent =new BundleEntryComponent();
		bundleEntryComponent.setFullUrl("Composition/Compositon-1");
		bundleEntryComponent.setResource(PrescriptionComposition.populatePrescriptionCompositionResource());
		
		BundleEntryComponent bundleEntryComponent2 =new BundleEntryComponent();
		bundleEntryComponent2.setFullUrl("Patient/Patient-01");
		bundleEntryComponent2.setResource(ResourcePopulator.populatePatientResource());
		
		BundleEntryComponent bundleEntryComponent3 =new BundleEntryComponent();
		bundleEntryComponent3.setFullUrl("Practitioner/Practitioner-01");
		bundleEntryComponent3.setResource(ResourcePopulator.populatePractitionerResource());
		
		
		BundleEntryComponent bundleEntryComponent4 =new BundleEntryComponent();
		bundleEntryComponent4.setFullUrl("MedicationRequest/MedicationRequest-01");
		bundleEntryComponent4.setResource(ResourcePopulator.populateMedicationRequestResource());
		
		BundleEntryComponent bundleEntryComponent5 =new BundleEntryComponent();
		bundleEntryComponent5.setFullUrl("Binary/Binary-01");
		bundleEntryComponent5.setResource(ResourcePopulator.populateBinaryResource());
		
		listBundleEntry.add(bundleEntryComponent);
		listBundleEntry.add(bundleEntryComponent2);
		listBundleEntry.add(bundleEntryComponent3);
		listBundleEntry.add(bundleEntryComponent4);
		listBundleEntry.add(bundleEntryComponent5);
		
		
		
		return presccriptionBundle;
	}
	
	
	static FhirContext ctx = FhirContext.forR4();
	public static void main (String[] arg){
		ca.uhn.fhir.parser.IParser parser =ctx.newJsonParser().setPrettyPrint(true);
		
		String str =parser.encodeResourceToString(PrescriptionBundleDoccument.populatePresccriptionBundle());
		System.out.println("final bundle"+str);
	}
}
