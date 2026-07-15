package com.param;
import java.util.Date;
import java.util.List;

import org.apache.poi.hpsf.Section;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Composition.CompositionStatus;
import org.hl7.fhir.r4.model.Composition.SectionComponent;
public class PrescriptionComposition {
       @SuppressWarnings("unchecked")
	public static Composition populatePrescriptionCompositionResource(){
    	   
    	   
    	    Composition composition =new Composition();
    	    
    	    composition.setId("PrescriptionRecord");
    	    Meta meta = new Meta();
    	    meta.setVersionId("1");
    	    meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/PrescriptionRecord");
    	   
    	    
    	    composition.setMeta(meta);
    	    
    	    // set status : preliminary |final|amended | enterred-in-error
    	    composition.setStatus(CompositionStatus.FINAL);
    	    
    	    
    	    // set type : Kind of composition
    	    composition.setType(new CodeableConcept( new Coding ("http://snomed.info/sct","440545006","Prescription record")));
    	    // set subject Who and/or what the composition/Prescription record is about
    	    composition.setSubject(new Reference("Patient/Patient-01"));
    	    
    	    //set date :Mandatory
    	    composition.setDate(new Date());
    	    
    	    // set author Who and/or what authored the composition/Presciption record
    	    
    	    composition.setAuthor((List<Reference>) new Reference("Practictioner/Practictioner-01"));
    	    
    	    
    	    // set title :Human Readable name/title
    	    composition.setTitle("Prescription Record");
    	    
    	    
    	    //create section 
    	    SectionComponent sectionComponent = new SectionComponent();
    	    sectionComponent.setCode(new CodeableConcept( new Coding ("http://snomed.info/sct","440545006","Prescription record")));
    	    
    	    
    	    //SEET REFERENCE 
    	    
    	    Reference   reference1 = new  Reference();
    	    reference1.setReference("Medication/Medication-01");
    	    reference1.setType("MedicationRequest");
    	    
    	    Reference   reference2 = new  Reference();
    	    reference2.setReference("Binary/Binary-01");
    	    reference1.setType("Binary");
    	    
    	    
    	    sectionComponent.addEntry(reference1).addEntry(reference2);
    	    composition.addSection(sectionComponent);
    	    
    	    
    	 return composition;  
       }
}
