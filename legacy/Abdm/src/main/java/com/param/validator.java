package com.param;

import java.io.IOException;

import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.NpmPackageValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;


public class validator {
	
	static FhirContext ctx = FhirContext.forR4();
	
	public static void main (String[] args) throws IOException{
		NpmPackageValidationSupport npmPackageValidationSupport  = new NpmPackageValidationSupport(ctx);
		npmPackageValidationSupport.loadPackageFromClasspath("classpath:package.tgz");
		
		
		// create a validation support chain
		
		ValidationSupportChain validationSupportChain = new ValidationSupportChain(
				npmPackageValidationSupport,
				new DefaultProfileValidationSupport(ctx),
				new CommonCodeSystemsTerminologyService(ctx),
				new InMemoryTerminologyServerValidationSupport(ctx),
				new SnapshotGeneratingValidationSupport(ctx)
				);
		
		CachingValidationSupport validation = new CachingValidationSupport(validationSupportChain);
		
		FhirValidator validator =ctx.newValidator();
		
		FhirInstanceValidator instatanceValidator =new FhirInstanceValidator(validationSupportChain);
		
		validator.registerValidatorModule(instatanceValidator);
		
		
		ValidationResult  outCome = validator.validateWithResult(PrescriptionBundleDoccument.populatePresccriptionBundle());
		
		for (SingleValidationMessage next : outCome.getMessages()) {
			System.out.println(next.getSeverity()+"-"+next.getLocationString() +"-"+next.getMessage());
			
			
		}
		
		
	}

}
