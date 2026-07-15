package com.param.fhir.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.param.fhir.model.FhirData.Complication;
import com.param.fhir.model.FhirData.Content;
import com.param.fhir.model.FhirData.DosageInstruction;
import com.param.fhir.model.FhirData.ReasonReference;
import com.param.fhir.model.FhirData.Appointment.AppointmentType;
import com.param.fhir.model.FhirData.Appointment.Participant;
import com.param.fhir.model.FhirData.Appointment.ServiceCategory;
import com.param.fhir.model.FhirData.Appointment.ServiceType;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FhirData {

    private List<Entry> entry;

    // Getters and Setters
    public List<Entry> getEntry() {
        return entry;
    }

    public void setEntry(List<Entry> entry) {
        this.entry = entry;
    }

    // Entry class
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Entry {
        private String fullUrl;
        private Resource resource;
        private String reference;
        
        //private List  

        public String getReference() {
			return reference;
		}

		public void setReference(String reference) {
			this.reference = reference;
		}

		// Getters and Setters
        public String getFullUrl() {
            return fullUrl;
        }

        public void setFullUrl(String fullUrl) {
            this.fullUrl = fullUrl;
        }

        public Resource getResource() {
            return resource;
        }

        public void setResource(Resource resource) {
            this.resource = resource;
        }
    }

 // Resource class
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ResourceComposition {
    	private Identifier identifier;

		public Identifier getIdentifier() {
			return identifier;
		}

		public void setIdentifier(Identifier identifier) {
			this.identifier = identifier;
		}
    	
    	
    }
    // Resource class
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Resource {
        private String resourceType;
        private String id;
        private Meta meta;
        private Text text;
		/*
		 * private Identifier identifiers; private List<Identifier> identifier;
		 */
        private Type type;
        
        private Subject subject;
        private Object identifier;  
        private String language;
        private String status;
        private String date;
        private String gender;
        private Object name;
        private String title;
        private List<Author> author;
        private List<Section> section;
        private List<Telecom> telecom;
       private Custodian custodian;
       
       private Period period;
       private Code code;
       private List<Diagnosis> diagnosis;
       private Encounter encounter;
      // private List<Name> names;
       private String birthDate;
       @JsonProperty("class")
       private Coding classObj;
       
       private ClinicalStatus clinicalStatus;
       
       private Patient patient;
       
       private String recordedDate;
       
       private Recorder recorder;
       
       private  List<Note> note;
       
       private List<ServiceCategory> serviceCategory;
       private List<ServiceType> serviceType;
       private AppointmentType appointmentType;
       private String description;
       private String start;
       private String end;
       private String created;
       private List<BasedOn> basedOn;
       private List<Participant> participantList;
       private String performedDateTime;

       private List<Complication> complication;
       
       private List<Category> category;
       
       private String intent;
       
       private String occurrenceDateTime;
       
       private Requestor requester;
       
       private MedicationCodeableConcept medicationCodeableConcept;
       
       private String dateAsserted;
       private String authoredOn;
       List<ReasonReference> reasonReference;
       
       List<DosageInstruction> dosageInstruction;
       
       private String docStatus;
       
       List<Content> content;
       
       VerificationStatus verificationStatus;
       
		/*
		 * @JsonProperty("identifier")
		 * 
		 * @JsonSerialize(using = IdentifierSerializer.class) public Identifier
		 * getIdentifier() { return identifier; }
		 * 
		 * @JsonProperty("identifier")
		 * 
		 * @JsonSerialize(using = IdentifierSerializer.class) public List<Identifier>
		 * getIdentifiers() { return identifiers; }
		 * 
		 * 
		 * // Setter for list of identifiers
		 * 
		 * @JsonProperty("identifier")
		 * 
		 * @JsonSerialize(using = IdentifierSerializer.class) public void
		 * setIdentifiers(List<Identifier> identifiers) { this.identifiers =
		 * identifiers; }
		 * 
		 * // Setter for identifier
		 * 
		 * @JsonProperty("identifier")
		 * 
		 * @JsonSerialize(using = IdentifierSerializer.class) public void
		 * setIdentifier(Identifier identifier) { this.identifier = identifier; }
		 */




		public VerificationStatus getVerificationStatus() {
		return verificationStatus;
	}

	public void setVerificationStatus(VerificationStatus verificationStatus) {
		this.verificationStatus = verificationStatus;
	}

		public Object getIdentifier() {
			return identifier;
		}

		public void setIdentifier(Object identifier) {
			this.identifier = identifier;
		}

		/*
		 * public boolean isSingleIdentifier() { return identifier instanceof
		 * Identifier; }
		 * 
		 * public boolean isIdentifierList() { return identifier instanceof List; }
		 */



		public String getDocStatus() {
		return docStatus;
	}

	public List<Content> getContent() {
			return content;
		}

		public void setContent(List<Content> content) {
			this.content = content;
		}

	public void setDocStatus(String docStatus) {
		this.docStatus = docStatus;
	}


		public List<DosageInstruction> getDosageInstruction() {
		return dosageInstruction;
	}



	public void setDosageInstruction(List<DosageInstruction> dosageInstruction) {
		this.dosageInstruction = dosageInstruction;
	}



		public List<ReasonReference> getReasonReference() {
		return reasonReference;
	}



	public void setReasonReference(List<ReasonReference> reasonReference) {
		this.reasonReference = reasonReference;
	}



		public String getAuthoredOn() {
		return authoredOn;
	}

	public void setAuthoredOn(String authoredOn) {
		this.authoredOn = authoredOn;
	}

		public String getDateAsserted() {
		return dateAsserted;
	}

	public void setDateAsserted(String dateAsserted) {
		this.dateAsserted = dateAsserted;
	}

		public MedicationCodeableConcept getMedicationCodeableConcept() {
		return medicationCodeableConcept;
	}

	public void setMedicationCodeableConcept(MedicationCodeableConcept medicationCodeableConcept) {
		this.medicationCodeableConcept = medicationCodeableConcept;
	}


		public Requestor getRequester() {
		return requester;
	}



	public void setRequester(Requestor requester) {
		this.requester = requester;
	}



		public String getOccurrenceDateTime() {
		return occurrenceDateTime;
	}

	public void setOccurrenceDateTime(String occurrenceDateTime) {
		this.occurrenceDateTime = occurrenceDateTime;
	}


		public List<Category> getCategory() {
		return category;
	}



	public void setCategory(List<Category> category) {
		this.category = category;
	}



		public String getIntent() {
		return intent;
	}

	public void setIntent(String intent) {
		this.intent = intent;
	}


		public List<Complication> getComplication() {
		return complication;
	}



	public void setComplication(List<Complication> complication) {
		this.complication = complication;
	}


		public String getPerformedDateTime() {
		return performedDateTime;
	}

	public void setPerformedDateTime(String performedDateTime) {
		this.performedDateTime = performedDateTime;
	}

		public List<Participant> getParticipant() {
		return participantList;
	}

	public void setParticipant(List<Participant> participantList) {
		this.participantList = participantList;
	}

		

		public List<BasedOn> getBasedOn() {
		return basedOn;
	}

	public void setBasedOn(List<BasedOn> basedOn) {
		this.basedOn = basedOn;
	}

		public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

		

		

		public AppointmentType getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(AppointmentType appointmentType) {
		this.appointmentType = appointmentType;
	}


		
		

		public List<ServiceType> getServiceType() {
		return serviceType;
	}



	public void setServiceType(List<ServiceType> serviceType) {
		this.serviceType = serviceType;
	}



		public List<ServiceCategory> getServiceCategory() {
		return serviceCategory;
	}



	public void setServiceCategory(List<ServiceCategory> serviceCategory) {
		this.serviceCategory = serviceCategory;
	}



		public List<Note> getNote() {
		return note;
	}

	public void setNote(List<Note> note) {
		this.note = note;
	}

		public Recorder getRecorder() {
		return recorder;
	}

	public void setRecorder(Recorder recorder) {
		this.recorder = recorder;
	}

		public String getRecordedDate() {
		return recordedDate;
	}

	public void setRecordedDate(String recordedDate) {
		this.recordedDate = recordedDate;
	}

		public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

		public Code getCode() {
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

		public ClinicalStatus getClinicalStatus() {
		return clinicalStatus;
	}

	public void setClinicalStatus(ClinicalStatus clinicalStatus) {
		this.clinicalStatus = clinicalStatus;
	}

		public Coding getClassObj() {
		return classObj;
	}

	public void setClassObj(Coding classObj) {
		this.classObj = classObj;
	}

		public List<Diagnosis> getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(List<Diagnosis> diagnosis) {
		this.diagnosis = diagnosis;
	}

		public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}


		public Encounter getEncounter() {
		return encounter;
	}

	public void setEncounter(Encounter encounter) {
		this.encounter = encounter;
	}

		public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

		

		

		public List<Telecom> getTelecom() {
		return telecom;
	}

	public void setTelecom(List<Telecom> telecom) {
		this.telecom = telecom;
	}

		


		public Object getName() {
		return name;
	}

	public void setName(Object name) {
		this.name = name;
	}

		public List<Section> getSection() {
		return section;
	}

	public void setSection(List<Section> section) {
		this.section = section;
	}

		public Custodian getCustodian() {
		return custodian;
	}

	public void setCustodian(Custodian custodian) {
		this.custodian = custodian;
	}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		// Getters and Setters
        public String getResourceType() {
            return resourceType;
        }

        public void setResourceType(String resourceType) {
            this.resourceType = resourceType;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Meta getMeta() {
            return meta;
        }

        public void setMeta(Meta meta) {
            this.meta = meta;
        }

        public Text getText() {
            return text;
        }

        public void setText(Text text) {
            this.text = text;
        }

		public String getLanguage() {
			return language;
		}

		public void setLanguage(String language) {
			this.language = language;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public Type getType() {
			return type;
		}

		public void setType(Type type) {
			this.type = type;
		}

		public Subject getSubject() {
			return subject;
		}

		public void setSubject(Subject subject) {
			this.subject = subject;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public List<Author> getAuthor() {
			return author;
		}

		public void setAuthor(List<Author> authors) {
			this.author = authors;
		}
		
    }

    // Meta class
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Meta {
        private String versionId;
        private String lastUpdated;
        private List<String> profile;

        // Getters and Setters
        public String getVersionId() {
            return versionId;
        }

        public void setVersionId(String versionId) {
            this.versionId = versionId;
        }

        public String getLastUpdated() {
            return lastUpdated;
        }

        public void setLastUpdated(String lastUpdated) {
            this.lastUpdated = lastUpdated;
        }

        public List<String> getProfile() {
            return profile;
        }

        public void setProfile(List<String> profile) {
            this.profile = profile;
        }
    }

    // Text class
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Text {
        private String status;
        private String div;

        // Getters and Setters
        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDiv() {
            return div;
        }

        public void setDiv(String div) {
            this.div = div;
        }
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Identifier {
        private String system;
        private String value;

		
		private Type type; 
		
		
		public String getSystem() {
			return system;
		}
		public void setSystem(String system) {
			this.system = system;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public Type getType() {
			return type;
		}
		public void setType(Type type) {
			this.type = type;
		}

      
    }
    
    


  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class Type {

    private List<Coding> coding;
    private String text;
    
    private String system;
    private String code;
    private String display;

    // Getters and Setters
    

    public String getText() {
        return text;
    }

    public List<Coding> getCoding() {
		return coding;
	}

	public void setCoding(List<Coding> coding) {
		this.coding = coding;
	}

	public void setText(String text) {
        this.text = text;
    }

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}
	
	
  }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Coding {
        private String system;
        private String code;
        private String display;

        // Getters and Setters
        public String getSystem() {
            return system;
        }

        public void setSystem(String system) {
            this.system = system;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDisplay() {
            return display;
        }

        public void setDisplay(String display) {
            this.display = display;
        }
    }


    
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Subject {

        private String reference;
        private String display;

        // Getters and Setters
        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public String getDisplay() {
            return display;
        }

        public void setDisplay(String display) {
            this.display = display;
        }
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Encounter {

        private String reference;

    	private String fullUrl;
        private Resource resource;
    	public String getFullUrl() {
    		return fullUrl;
    	}
    	public void setFullUrl(String fullUrl) {
    		this.fullUrl = fullUrl;
    	}
    	public Resource getResource() {
    		return resource;
    	}
    	public void setResource(Resource resource) {
    		this.resource = resource;
    	}
        // Getters and Setters
        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

    }
    
 // Author class
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Author {
        private String reference;
        private String display;

        // Getters and Setters
        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public String getDisplay() {
            return display;
        }

        public void setDisplay(String display) {
            this.display = display;
        }
    }
    
    
 @JsonInclude(JsonInclude.Include.NON_NULL)
public static class Custodian {

    private String reference;
    private String display;

    // Getters and Setters
    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public static class Section {

    private String title;
    private Code code;
    private List<Entry> entry;
    private List<SectionDetails> sectionDetails;
    private String recordedDate ;
    public String getRecordedDate() {
		return recordedDate;
	}

	public void setRecordedDate(String recordedDate) {
		this.recordedDate = recordedDate;
	}

	public List<SectionDetails> getSectionDetails() {
		return sectionDetails;
	}

	public void setSectionDetails(List<SectionDetails> sectionDetails) {
		this.sectionDetails = sectionDetails;
	}

	public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public List<Entry> getEntry() {
        return entry;
    }

    public void setEntry(List<Entry> entry) {
        this.entry = entry;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public static class Code {

    private List<Coding> coding;
    private String system;
    private String code;
    private String display;
    private String text;
    
    
    public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<Coding> getCoding() {
        return coding;
    }

    public void setCoding(List<Coding> coding) {
        this.coding = coding;
    }

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}
}

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public static class SectionDetails{
    public String title;
    public Code code;
    public ArrayList<Entry> entry;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Code getCode() {
		return code;
	}
	public void setCode(Code code) {
		this.code = code;
	}
	public ArrayList<Entry> getEntry() {
		return entry;
	}
	public void setEntry(ArrayList<Entry> entry) {
		this.entry = entry;
	}
    
}

@JsonInclude(JsonInclude.Include.NON_NULL)
public static class Name {

    private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
    
}


//==================================================================




// Practitioner class
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public static class Practitioner {
    private String fullUrl;
    private PractitionerResource resource;

    // Getters and Setters
    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public PractitionerResource getResource() {
        return resource;
    }

    public void setResource(PractitionerResource resource) {
        this.resource = resource;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public static class PractitionerResource {
    private String resourceType;
    private String id;
    private Meta meta;
    private Text text;
    private List<Identifier> identifier;
    private List<Name> name;

    // Getters and Setters
    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public List<Identifier> getIdentifier() {
        return identifier;
    }

    public void setIdentifier(List<Identifier> identifier) {
        this.identifier = identifier;
    }

    public List<Name> getName() {
        return name;
    }

    public void setName(List<Name> name) {
        this.name = name;
    }
}




@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public static class CodeNew {
    private String system;
    private String code;
    private String display;

    // Constructors
    public CodeNew() {}

    public CodeNew(String system, String code, String display) {
        this.system = system;
        this.code = code;
        this.display = display;
    }

    // Getters and Setters
    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}



@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public static class Telecom  {

  private String value;
  private String system;
  private String use;
public String getValue() {
	return value;
}
public void setValue(String value) {
	this.value = value;
}
public String getSystem() {
	return system;
}
public void setSystem(String system) {
	this.system = system;
}
public String getUse() {
	return use;
}
public void setUse(String use) {
	this.use = use;
}

  
}


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public static class Organization  {

	private String fullUrl;
    private Resource resource;
	public String getFullUrl() {
		return fullUrl;
	}
	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}
	public Resource getResource() {
		return resource;
	}
	public void setResource(Resource resource) {
		this.resource = resource;
	}


  
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public static class Patient  {

	private String fullUrl;
    private Resource resource;
    private String reference;

    // Getters and Setters
    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
	public String getFullUrl() {
		return fullUrl;
	}
	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}
	public Resource getResource() {
		return resource;
	}
	public void setResource(Resource resource) {
		this.resource = resource;
	}


  
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public static class Reference  {

	private String reference;

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}
   

  
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public static class Period  {

	private String start;

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

  
}


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public static class Diagnosis  {

	private Condition condition;
	
	private Use use;

	public Use getUse() {
		return use;
	}

	public void setUse(Use use) {
		this.use = use;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	

  
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public static class Condition  {

	private String reference;
	
	 private Meta meta;
	 private Subject subject;
	 
	 private Text text;
	 
	 private  ClinicalStatus clinicalStatus;
	 
	 private Code code;

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public Code getCode() {
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

	public ClinicalStatus getClinicalStatus() {
		return clinicalStatus;
	}

	public void setClinicalStatus(ClinicalStatus clinicalStatus) {
		this.clinicalStatus = clinicalStatus;
	}

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}
   

  
}


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public static class Use  {

	private String reference;
	private List<Coding> coding;

	public List<Coding> getCoding() {
		return coding;
	}

	public void setCoding(List<Coding> coding) {
		this.coding = coding;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}
   
}


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public static class EncounterClass {

    private List<Coding> coding;
    private String system;
    private String code;
    private String display;

    public List<Coding> getCoding() {
        return coding;
    }

    public void setCoding(List<Coding> coding) {
        this.coding = coding;
    }

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}
}


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public static class ClinicalStatus {

    private List<Coding> coding;
    private String system;
    private String code;
    private String display;

    public List<Coding> getCoding() {
        return coding;
    }

    public void setCoding(List<Coding> coding) {
        this.coding = coding;
    }

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public static class VerificationStatus {

    private List<Coding> coding;
    private String system;
    private String code;
    private String display;

    public List<Coding> getCoding() {
        return coding;
    }

    public void setCoding(List<Coding> coding) {
        this.coding = coding;
    }

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public static class Recorder  {

    private String reference;

    // Getters and Setters
    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

  
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public static class Note  {

    private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}


  
}


@JsonInclude(JsonInclude.Include.NON_NULL)
public static class AllergyIntolerance {

    private String resourceType;
    private String id;
    private Meta meta;
    private Text text;
    private ClinicalStatus clinicalStatus;
    private VerificationStatus verificationStatus;
    private Code code;
   // private Reference patient;
    private String recordedDate;
    private Recorder recorder;
    private List<Note> note;
    private Patient patient;

    // Getters and Setters
    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public ClinicalStatus getClinicalStatus() {
        return clinicalStatus;
    }

    public void setClinicalStatus(ClinicalStatus clinicalStatus) {
        this.clinicalStatus = clinicalStatus;
    }

    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

		/*
		 * public Reference getPatient() { return patient; }
		 * 
		 * public void setPatient(Reference patient) { this.patient = patient; }
		 */

    public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public String getRecordedDate() {
        return recordedDate;
    }

    public void setRecordedDate(String recordedDate) {
        this.recordedDate = recordedDate;
    }

    public Recorder getRecorder() {
        return recorder;
    }

    public void setRecorder(Recorder recorder) {
        this.recorder = recorder;
    }

    public List<Note> getNote() {
        return note;
    }

    public void setNote(List<Note> note) {
        this.note = note;
    }

    // Nested Classes

   
}


@JsonInclude(JsonInclude.Include.NON_NULL)
public static class Appointment {

    private String resourceType;
    private String id;
    private Meta meta;
    private Text text;
    private String status;
    private List<ServiceCategory> serviceCategory;
    private List<ServiceType> serviceType;
    private AppointmentType appointmentType;
    private List<Reference> reasonReference;
    private String description;
    private String start;
    private String end;
    private String created;
    private List<Reference> basedOn;
    private List<Participant> participant;

    // Getters and Setters
    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ServiceCategory> getServiceCategory() {
        return serviceCategory;
    }

    public void setServiceCategory(List<ServiceCategory> serviceCategory) {
        this.serviceCategory = serviceCategory;
    }

    public List<ServiceType> getServiceType() {
        return serviceType;
    }

    public void setServiceType(List<ServiceType> serviceType) {
        this.serviceType = serviceType;
    }

    public AppointmentType getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(AppointmentType appointmentType) {
        this.appointmentType = appointmentType;
    }

    public List<Reference> getReasonReference() {
        return reasonReference;
    }

    public void setReasonReference(List<Reference> reasonReference) {
        this.reasonReference = reasonReference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public List<Reference> getBasedOn() {
        return basedOn;
    }

    public void setBasedOn(List<Reference> basedOn) {
        this.basedOn = basedOn;
    }

    public List<Participant> getParticipant() {
        return participant;
    }

    public void setParticipant(List<Participant> participant) {
        this.participant = participant;
    }

     
    public static class ServiceCategory {
        private List<Coding> coding;

        public List<Coding> getCoding() {
            return coding;
        }

        public void setCoding(List<Coding> coding) {
            this.coding = coding;
        }
    }

    public static class ServiceType {
        private List<Coding> coding;

        public List<Coding> getCoding() {
            return coding;
        }

        public void setCoding(List<Coding> coding) {
            this.coding = coding;
        }
    }

    public static class AppointmentType {
        private List<Coding> coding;

        public List<Coding> getCoding() {
            return coding;
        }

        public void setCoding(List<Coding> coding) {
            this.coding = coding;
        }
    }

    public static class Participant {
        private Actor actor;
        private String status;
		public Actor getActor() {
			return actor;
		}
		public void setActor(Actor actor) {
			this.actor = actor;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}

       
    }
    
    
}

@JsonInclude(JsonInclude.Include.NON_NULL)
public static class Procedure {

   
    // Complication class
    

    private Meta meta;
    private Text text;
    private String status;
    private Code code;
    private Reference subject;
    private String performedDateTime;
    private List<Complication> complication;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public Reference getSubject() {
        return subject;
    }

    public void setSubject(Reference subject) {
        this.subject = subject;
    }

    public String getPerformedDateTime() {
        return performedDateTime;
    }

    public void setPerformedDateTime(String performedDateTime) {
        this.performedDateTime = performedDateTime;
    }

    public List<Complication> getComplication() {
        return complication;
    }

    public void setComplication(List<Complication> complication) {
        this.complication = complication;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
public static class ServiceRequest {

    private Meta meta;
    private Text text;
    private String status;
    private String intent;
    private List<Category> category;
    private Code code;
    private Reference subject;
    private String occurrenceDateTime;
    private Requestor requester;

   

    // Getters and Setters
    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public Reference getSubject() {
        return subject;
    }

    public void setSubject(Reference subject) {
        this.subject = subject;
    }

    public String getOccurrenceDateTime() {
        return occurrenceDateTime;
    }

    public void setOccurrenceDateTime(String occurrenceDateTime) {
        this.occurrenceDateTime = occurrenceDateTime;
    }

    public Requestor getRequester() {
        return requester;
    }

    public void setRequester(Requestor requester) {
        this.requester = requester;
    }

  
}

@JsonInclude(JsonInclude.Include.NON_NULL)
public static class Requestor {
	private String display;
    private String reference;
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	
   
}

@JsonInclude(JsonInclude.Include.NON_NULL)
public static class MedicationCodeableConcept {
    private List<Coding> coding;
    
    private String  text;

    public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<Coding> getCoding() {
        return coding;
    }

    public void setCoding(List<Coding> coding) {
        this.coding = coding;
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
public static class MedicationStatement {
    private Meta meta;
    private Text text;
    private String status;
    private MedicationCodeableConcept medicationCodeableConcept;
    private Reference subject;
    private String dateAsserted;
	public Meta getMeta() {
		return meta;
	}
	public void setMeta(Meta meta) {
		this.meta = meta;
	}
	public Text getText() {
		return text;
	}
	public void setText(Text text) {
		this.text = text;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public MedicationCodeableConcept getMedicationCodeableConcept() {
		return medicationCodeableConcept;
	}
	public void setMedicationCodeableConcept(MedicationCodeableConcept medicationCodeableConcept) {
		this.medicationCodeableConcept = medicationCodeableConcept;
	}
	public Reference getSubject() {
		return subject;
	}
	public void setSubject(Reference subject) {
		this.subject = subject;
	}
	public String getDateAsserted() {
		return dateAsserted;
	}
	public void setDateAsserted(String dateAsserted) {
		this.dateAsserted = dateAsserted;
	}


}


@JsonInclude(JsonInclude.Include.NON_NULL)
public static class Route {
    private List<Coding> coding;

    public List<Coding> getCoding() { return coding; }
    public void setCoding(List<Coding> coding) { this.coding = coding; }

    @Override
    public String toString() {
        return "Route{" +
                "coding=" + coding +
                '}';
    }
}

//Nested Classes
@JsonInclude(JsonInclude.Include.NON_NULL)
public static class AdditionalInstruction {
    private List<Coding> coding;

    public List<Coding> getCoding() { return coding; }
    public void setCoding(List<Coding> coding) { this.coding = coding; }

    @Override
    public String toString() {
        return "AdditionalInstruction{" +
                "coding=" + coding +
                '}';
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
public static class DosageInstruction {
    private List<AdditionalInstruction> additionalInstruction;
    private Route route;

    // Getters and Setters
    public List<AdditionalInstruction> getAdditionalInstruction() { return additionalInstruction; }
    public void setAdditionalInstruction(List<AdditionalInstruction> additionalInstruction) { this.additionalInstruction = additionalInstruction; }

    public Route getRoute() { return route; }
    public void setRoute(Route route) { this.route = route; }

  
}

@JsonInclude(JsonInclude.Include.NON_NULL)
public static class MedicationRequest {
    private Meta meta;
    private Text text;
    private String status;
    private String intent;
    private MedicationCodeableConcept medicationCodeableConcept;
    private Subject subject;
    private String authoredOn;
    private Requestor requester;
    private List<Reference> reasonReference;
    private List<DosageInstruction> dosageInstruction;
	public Meta getMeta() {
		return meta;
	}
	public void setMeta(Meta meta) {
		this.meta = meta;
	}
	public Text getText() {
		return text;
	}
	public void setText(Text text) {
		this.text = text;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getIntent() {
		return intent;
	}
	public void setIntent(String intent) {
		this.intent = intent;
	}
	public MedicationCodeableConcept getMedicationCodeableConcept() {
		return medicationCodeableConcept;
	}
	public void setMedicationCodeableConcept(MedicationCodeableConcept medicationCodeableConcept) {
		this.medicationCodeableConcept = medicationCodeableConcept;
	}
	public Subject getSubject() {
		return subject;
	}
	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	public String getAuthoredOn() {
		return authoredOn;
	}
	public void setAuthoredOn(String authoredOn) {
		this.authoredOn = authoredOn;
	}
	public Requestor getRequester() {
		return requester;
	}
	public void setRequester(Requestor requester) {
		this.requester = requester;
	}
	public List<Reference> getReasonReference() {
		return reasonReference;
	}
	public void setReasonReference(List<Reference> reasonReference) {
		this.reasonReference = reasonReference;
	}
	public List<DosageInstruction> getDosageInstruction() {
		return dosageInstruction;
	}
	public void setDosageInstruction(List<DosageInstruction> dosageInstruction) {
		this.dosageInstruction = dosageInstruction;
	}


}

@JsonInclude(JsonInclude.Include.NON_NULL)
public static class CodeableConcept {
    private List<Code> coding;
    private String text;

    public List<Code> getCoding() {
        return coding;
    }

    public void setCoding(List<Code> coding) {
        this.coding = coding;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
public static class Attachment {
    private String contentType;
    private String language;
    private String data; // Base64 encoded string
    private String title;
    private String creation;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
public static class DocumentReference {
    private Meta meta;
    private Text text;
    private String status;
    private String docStatus;
    private CodeableConcept type;
    private Reference subject;
    private List<Content> content;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDocStatus() {
        return docStatus;
    }

    public void setDocStatus(String docStatus) {
        this.docStatus = docStatus;
    }

    public CodeableConcept getType() {
        return type;
    }

    public void setType(CodeableConcept type) {
        this.type = type;
    }

    public Reference getSubject() {
        return subject;
    }

    public void setSubject(Reference subject) {
        this.subject = subject;
    }

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

}

@JsonInclude(JsonInclude.Include.NON_NULL)
public static class Content {
    private Attachment attachment;

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
public static class Signature {
    private List<Type> type;
    private String when;
    private Reference who;
    private String sigFormat;
    private String data; // Base64 encoded signature image

   

    public List<Type> getType() {
		return type;
	}

	public void setType(List<Type> type) {
		this.type = type;
	}

	public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public Reference getWho() {
        return who;
    }

    public void setWho(Reference who) {
        this.who = who;
    }

    public String getSigFormat() {
        return sigFormat;
    }

    public void setSigFormat(String sigFormat) {
        this.sigFormat = sigFormat;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}



@JsonInclude(JsonInclude.Include.NON_NULL)
public static class ReasonReference {
    private String reference;

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

   

}

@JsonInclude(JsonInclude.Include.NON_NULL)
public static class BasedOn {
    private String reference;

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

   

}



@JsonInclude(JsonInclude.Include.NON_NULL)
public static class Actor {
    private String reference;

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

   

}

@JsonInclude(JsonInclude.Include.NON_NULL)
public static class Complication {
    private List<Coding> coding;

    

    public List<Coding> getCoding() {
		return coding;
	}



	public void setCoding(List<Coding> coding) {
		this.coding = coding;
	}



	@Override
    public String toString() {
        return "Complication{" +
                "coding=" + coding +
                '}';
    }
}


// Nested Category class
@JsonInclude(JsonInclude.Include.NON_NULL)
public static class Category {
    private List<Coding> coding;

    // Getters and Setters
    public List<Coding> getCoding() {
        return coding;
    }

    public void setCoding(List<Coding> coding) {
        this.coding = coding;
    }
}

}
