package com.param.global.dto;

import java.util.List;

public class NursingInitialAssessmentDto {
	private Integer initialAssessmentId;

	private String communicationAndCognitionDesc;
	
	private String invasiveLinesCatheterTubesDrainsDesc;
	
	private String psychosocialDesc;
	
	private String restAndSleepDesc;
	
	private String nutritionAssessmentDesc;
	
	private String fallRiskAssessmentDesc;
	
	private String examinationDesc;
	
	private Integer patientId;
	
	private Integer encounterId;
	
	private Integer admissionId;
	
	private String assessmentStartTime;
	
	private String assessmentEndTime;
	
	private Integer arrivedBy;
	
	private Integer unitId;

	private Integer organizationId;
	
	private Character status;

	private String createdDate;

	private Integer createdBy;

	private Integer updatedBy;
	
	private Character isSubmit;

	private String updatedDate;
	private Integer languageSpoken;
	private Integer translatorRequired;
	private String levelOfConsciousness;
	private String orientation;
	private Integer time;
	private Integer place;
	private Integer person;
	private Integer speech;
	private Integer hearing;
	private Integer vision;
	private Integer eyes;
	private Integer rightEye;
	private Integer rightPresent;
	private String rightEyeText;
	private Integer leftEye;
	private Integer leftPresent;
	private String leftEyeText;
	private Integer typeId;
	private Integer statusId;
	private String otherStatus;
	private String dateOfPlacement;
	private Integer behaviorId;
	private String behaviorDesc;
	private Integer psychoEmoDistressTd;
	private Integer spiritualDistressId;
	private Integer amilyDistressId;
	private Integer painId;
	private String painLocation;
	private List<Integer> typeOfPain;
	private Integer throbbing;
	private Integer stabbing;
	private Integer dull;
	private Integer tolerable;
	private Integer constant;
	private Integer occassional;
	private Integer intermittent;
	private Integer others;
	private String typeOfPainDesc;
	private List<Integer> aggravatingFactors;
	private Integer cough;
	private Integer bowelMovement;
	private Integer walking;
	private Integer running;
	private Integer standing;
	private Integer sitting;
	private Integer changingBodyPositions;
	private String aggravatingFactorsDesc;
	private List<Integer> relievingFactors;
	private Integer medication;
	private Integer changeOfPosition;
	private Integer rest;
	private Integer lying;
	private Integer applicationOfHotPack;
	private String relievingFactorsDesc;
	private Integer painScore;
	private Integer vitals;
	private Integer temperature;
	private Integer Pulse;
	private Integer Respiration;
	private Integer bps;
	private Integer bpd;
	private Integer height;
	private Integer weight;
	private Integer bmi;
	private Integer spo2;
	private Integer headCircumference;
	private Integer oral;
	private List<Integer> oralCavity;
	private Integer mucositis;
	private Integer mrynessOfMouth;
	private Integer ulceration;
	private Integer halitosis;
	private Integer bleedingGums;
	private Integer cogatedTongue;
	private Integer cleftPalate;
	private Integer cleftLip;
	private String oralCavityDesc;
	private Integer dentalHygiene;
	private Integer motorDeficits;
	private String motorDeficitsText;
	private Integer sensoryDeficits;
	private String sensoryDeficitsText;
	private Integer impairedDecisionMaking;
	private String impairedDecisionMakingText;
	private Integer sensation;
	private Integer gcsScore;
	private Integer pulseType;
	private List<Integer> heartSound;
	private Integer s1s2HeardNormally;
	private Integer s3Heard;
	private Integer s4Heard;
	private Integer paceMaker;
	private String paceMakerText;
	private List<Integer> presenceOf;
	private Integer edema;
	private Integer pallor;
	private Integer cyanosis;
	private Integer swelling;
	private Integer clubbing;
	private Integer coldExtremeties;
	private String presenceOfText;
	private Integer hemoptysis;
	private Integer shortnessOfBreath;
	private Integer oxygenSupplement;
	private String oxygenSupplementText;
	private Integer ifPresentSpecify;
	private Integer breathingPattern;
	private List<Integer> lungAuscultationAirEntry;
	private Integer bilateralEntry;
	private Integer reducedAirEntryB;
	private Integer reducedAirEntryL;
	private Integer reducedAirEntryR;
	private Integer noAirEntryB;
	private Integer noAirEntryL;
	private Integer noAirEntryR;
	private List<Integer> breathSounds;
	private Integer vesicular;
	private Integer crepitations;
	private Integer wheezing;
	private Integer rhonchi;
	private Integer crackles;
	private Integer stridor;
	private String breathSoundsText;
	private Integer tracheostomy;
	private Integer gastrointestinal;
	private Integer nausea;
	private Integer vomiting;
	private Integer dysphagia;
	private Integer indigestion;
	private Integer abdominalDistention;
	private Integer abdominalCramp;
	private Integer heartBurn;
	private Integer abdominalGirth;
	private Integer bowelSounds;
	private List<Integer> bowelPattern;
	private Integer normal;
	private Integer constipation;
	private Integer diarrhoea;
	private String  bowelPatternText;
	private String  drainsAndTubingsText;
	private Integer lastDayOfBowelMovement;
	private Integer incisionalWound;
	private Integer ostomies;
	private Integer stomaStatus;
	private Integer drainsAndTubings;
	private Integer integumentary;
	private List<Integer> skinTemperature;
	private Integer warm;
	private Integer cold;
	private Integer clammySensation;
	private String skinTemperatureText;
	private Integer pressureSore;
	private Integer stageI;
	private Integer locationOfPressureSore;
	private Integer musculoskeletal;
	private Integer ambulation;
	private Integer anyLimitationsInMovementsOfJoints;
	private String anyLimitationsInMovementsOfJointsText;
	private Integer adlScore;
	private Integer genitourinary;
	private Integer urineOutput;
	private Integer urineFrequency;
	private String urineFrequencyText;
	private Integer haematuria;
	private String haematuriaText;
	private Integer dysuria;
	private String dysuriaText;
	private Integer vaginalPenileDischarge;
	private String vaginalPenileDischargeText;
	private String lastMenstrualPeriod;
	private String painAssessment;
	private Integer sleepDisturbance;
	private Integer useOfSedatives;
	private String sleepDisturbanceText;
	private String useOfSedativesText;
	private String stomaStatusText;


	public Integer getInitialAssessmentId() {
		return initialAssessmentId;
	}

	public void setInitialAssessmentId(Integer initialAssessmentId) {
		this.initialAssessmentId = initialAssessmentId;
	}

	public String getCommunicationAndCognitionDesc() {
		return communicationAndCognitionDesc;
	}

	public void setCommunicationAndCognitionDesc(String communicationAndCognitionDesc) {
		this.communicationAndCognitionDesc = communicationAndCognitionDesc;
	}

	public String getInvasiveLinesCatheterTubesDrainsDesc() {
		return invasiveLinesCatheterTubesDrainsDesc;
	}

	public void setInvasiveLinesCatheterTubesDrainsDesc(String invasiveLinesCatheterTubesDrainsDesc) {
		this.invasiveLinesCatheterTubesDrainsDesc = invasiveLinesCatheterTubesDrainsDesc;
	}

	public String getPsychosocialDesc() {
		return psychosocialDesc;
	}

	public void setPsychosocialDesc(String psychosocialDesc) {
		this.psychosocialDesc = psychosocialDesc;
	}

	

	public String getRestAndSleepDesc() {
		return restAndSleepDesc;
	}

	public void setRestAndSleepDesc(String restAndSleepDesc) {
		this.restAndSleepDesc = restAndSleepDesc;
	}

	public String getNutritionAssessmentDesc() {
		return nutritionAssessmentDesc;
	}

	public void setNutritionAssessmentDesc(String nutritionAssessmentDesc) {
		this.nutritionAssessmentDesc = nutritionAssessmentDesc;
	}

	public String getFallRiskAssessmentDesc() {
		return fallRiskAssessmentDesc;
	}

	public void setFallRiskAssessmentDesc(String fallRiskAssessmentDesc) {
		this.fallRiskAssessmentDesc = fallRiskAssessmentDesc;
	}

	public String getExaminationDesc() {
		return examinationDesc;
	}

	public void setExaminationDesc(String examinationDesc) {
		this.examinationDesc = examinationDesc;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public String getAssessmentStartTime() {
		return assessmentStartTime;
	}

	public void setAssessmentStartTime(String assessmentStartTime) {
		this.assessmentStartTime = assessmentStartTime;
	}

	public String getAssessmentEndTime() {
		return assessmentEndTime;
	}

	public void setAssessmentEndTime(String assessmentEndTime) {
		this.assessmentEndTime = assessmentEndTime;
	}

	public Integer getArrivedBy() {
		return arrivedBy;
	}

	public void setArrivedBy(Integer arrivedBy) {
		this.arrivedBy = arrivedBy;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getLanguageSpoken() {
		return languageSpoken;
	}

	public void setLanguageSpoken(Integer languageSpoken) {
		this.languageSpoken = languageSpoken;
	}

	public Integer getTranslatorRequired() {
		return translatorRequired;
	}

	public void setTranslatorRequired(Integer translatorRequired) {
		this.translatorRequired = translatorRequired;
	}

	public String getLevelOfConsciousness() {
		return levelOfConsciousness;
	}

	public void setLevelOfConsciousness(String levelOfConsciousness) {
		this.levelOfConsciousness = levelOfConsciousness;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public Integer getPlace() {
		return place;
	}

	public void setPlace(Integer place) {
		this.place = place;
	}

	public Integer getPerson() {
		return person;
	}

	public void setPerson(Integer person) {
		this.person = person;
	}

	public Integer getSpeech() {
		return speech;
	}

	public void setSpeech(Integer speech) {
		this.speech = speech;
	}

	public Integer getHearing() {
		return hearing;
	}

	public void setHearing(Integer hearing) {
		this.hearing = hearing;
	}

	public Integer getVision() {
		return vision;
	}

	public void setVision(Integer vision) {
		this.vision = vision;
	}

	public Integer getEyes() {
		return eyes;
	}

	public void setEyes(Integer eyes) {
		this.eyes = eyes;
	}

	public Integer getRightEye() {
		return rightEye;
	}

	public void setRightEye(Integer rightEye) {
		this.rightEye = rightEye;
	}

	public Integer getRightPresent() {
		return rightPresent;
	}

	public void setRightPresent(Integer rightPresent) {
		this.rightPresent = rightPresent;
	}

	public String getRightEyeText() {
		return rightEyeText;
	}

	public void setRightEyeText(String rightEyeText) {
		this.rightEyeText = rightEyeText;
	}

	public Integer getLeftEye() {
		return leftEye;
	}

	public void setLeftEye(Integer leftEye) {
		this.leftEye = leftEye;
	}

	public Integer getLeftPresent() {
		return leftPresent;
	}

	public void setLeftPresent(Integer leftPresent) {
		this.leftPresent = leftPresent;
	}

	public String getLeftEyeText() {
		return leftEyeText;
	}

	public void setLeftEyeText(String leftEyeText) {
		this.leftEyeText = leftEyeText;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getOtherStatus() {
		return otherStatus;
	}

	public void setOtherStatus(String otherStatus) {
		this.otherStatus = otherStatus;
	}

	public String getDateOfPlacement() {
		return dateOfPlacement;
	}

	public void setDateOfPlacement(String dateOfPlacement) {
		this.dateOfPlacement = dateOfPlacement;
	}

	public Integer getBehaviorId() {
		return behaviorId;
	}

	public void setBehaviorId(Integer behaviorId) {
		this.behaviorId = behaviorId;
	}

	public String getBehaviorDesc() {
		return behaviorDesc;
	}

	public void setBehaviorDesc(String behaviorDesc) {
		this.behaviorDesc = behaviorDesc;
	}

	public Integer getPsychoEmoDistressTd() {
		return psychoEmoDistressTd;
	}

	public void setPsychoEmoDistressTd(Integer psychoEmoDistressTd) {
		this.psychoEmoDistressTd = psychoEmoDistressTd;
	}

	public Integer getSpiritualDistressId() {
		return spiritualDistressId;
	}

	public void setSpiritualDistressId(Integer spiritualDistressId) {
		this.spiritualDistressId = spiritualDistressId;
	}

	public Integer getAmilyDistressId() {
		return amilyDistressId;
	}

	public void setAmilyDistressId(Integer amilyDistressId) {
		this.amilyDistressId = amilyDistressId;
	}

	public Integer getPainId() {
		return painId;
	}

	public void setPainId(Integer painId) {
		this.painId = painId;
	}

	public String getPainLocation() {
		return painLocation;
	}

	public void setPainLocation(String painLocation) {
		this.painLocation = painLocation;
	}

	public List<Integer> getTypeOfPain() {
		return typeOfPain;
	}

	public void setTypeOfPain(List<Integer> typeOfPain) {
		this.typeOfPain = typeOfPain;
	}

	public Integer getThrobbing() {
		return throbbing;
	}

	public void setThrobbing(Integer throbbing) {
		this.throbbing = throbbing;
	}

	public Integer getStabbing() {
		return stabbing;
	}

	public void setStabbing(Integer stabbing) {
		this.stabbing = stabbing;
	}

	public Integer getDull() {
		return dull;
	}

	public void setDull(Integer dull) {
		this.dull = dull;
	}

	public Integer getTolerable() {
		return tolerable;
	}

	public void setTolerable(Integer tolerable) {
		this.tolerable = tolerable;
	}

	public Integer getConstant() {
		return constant;
	}

	public void setConstant(Integer constant) {
		this.constant = constant;
	}

	public Integer getOccassional() {
		return occassional;
	}

	public void setOccassional(Integer occassional) {
		this.occassional = occassional;
	}

	public Integer getIntermittent() {
		return intermittent;
	}

	public void setIntermittent(Integer intermittent) {
		this.intermittent = intermittent;
	}

	public Integer getOthers() {
		return others;
	}

	public void setOthers(Integer others) {
		this.others = others;
	}

	public String getTypeOfPainDesc() {
		return typeOfPainDesc;
	}

	public void setTypeOfPainDesc(String typeOfPainDesc) {
		this.typeOfPainDesc = typeOfPainDesc;
	}

	public List<Integer> getAggravatingFactors() {
		return aggravatingFactors;
	}

	public void setAggravatingFactors(List<Integer> aggravatingFactors) {
		this.aggravatingFactors = aggravatingFactors;
	}

	public Integer getCough() {
		return cough;
	}

	public void setCough(Integer cough) {
		this.cough = cough;
	}

	public Integer getBowelMovement() {
		return bowelMovement;
	}

	public void setBowelMovement(Integer bowelMovement) {
		this.bowelMovement = bowelMovement;
	}

	public Integer getWalking() {
		return walking;
	}

	public void setWalking(Integer walking) {
		this.walking = walking;
	}

	public Integer getRunning() {
		return running;
	}

	public void setRunning(Integer running) {
		this.running = running;
	}

	public Integer getStanding() {
		return standing;
	}

	public void setStanding(Integer standing) {
		this.standing = standing;
	}

	public Integer getSitting() {
		return sitting;
	}

	public void setSitting(Integer sitting) {
		this.sitting = sitting;
	}

	public Integer getChangingBodyPositions() {
		return changingBodyPositions;
	}

	public void setChangingBodyPositions(Integer changingBodyPositions) {
		this.changingBodyPositions = changingBodyPositions;
	}

	public String getAggravatingFactorsDesc() {
		return aggravatingFactorsDesc;
	}

	public void setAggravatingFactorsDesc(String aggravatingFactorsDesc) {
		this.aggravatingFactorsDesc = aggravatingFactorsDesc;
	}

	public List<Integer> getRelievingFactors() {
		return relievingFactors;
	}

	public void setRelievingFactors(List<Integer> relievingFactors) {
		this.relievingFactors = relievingFactors;
	}

	public Integer getMedication() {
		return medication;
	}

	public void setMedication(Integer medication) {
		this.medication = medication;
	}

	public Integer getChangeOfPosition() {
		return changeOfPosition;
	}

	public void setChangeOfPosition(Integer changeOfPosition) {
		this.changeOfPosition = changeOfPosition;
	}

	public Integer getRest() {
		return rest;
	}

	public void setRest(Integer rest) {
		this.rest = rest;
	}

	public Integer getLying() {
		return lying;
	}

	public void setLying(Integer lying) {
		this.lying = lying;
	}

	public Integer getApplicationOfHotPack() {
		return applicationOfHotPack;
	}

	public void setApplicationOfHotPack(Integer applicationOfHotPack) {
		this.applicationOfHotPack = applicationOfHotPack;
	}

	public String getRelievingFactorsDesc() {
		return relievingFactorsDesc;
	}

	public void setRelievingFactorsDesc(String relievingFactorsDesc) {
		this.relievingFactorsDesc = relievingFactorsDesc;
	}

	public Integer getPainScore() {
		return painScore;
	}

	public void setPainScore(Integer painScore) {
		this.painScore = painScore;
	}

	public Integer getVitals() {
		return vitals;
	}

	public void setVitals(Integer vitals) {
		this.vitals = vitals;
	}

	public Integer getTemperature() {
		return temperature;
	}

	public void setTemperature(Integer temperature) {
		this.temperature = temperature;
	}

	public Integer getPulse() {
		return Pulse;
	}

	public void setPulse(Integer pulse) {
		Pulse = pulse;
	}

	public Integer getRespiration() {
		return Respiration;
	}

	public void setRespiration(Integer respiration) {
		Respiration = respiration;
	}

	public Integer getBps() {
		return bps;
	}

	public void setBps(Integer bps) {
		this.bps = bps;
	}

	public Integer getBpd() {
		return bpd;
	}

	public void setBpd(Integer bpd) {
		this.bpd = bpd;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getBmi() {
		return bmi;
	}

	public void setBmi(Integer bmi) {
		this.bmi = bmi;
	}

	public Integer getSpo2() {
		return spo2;
	}

	public void setSpo2(Integer spo2) {
		this.spo2 = spo2;
	}

	public Integer getHeadCircumference() {
		return headCircumference;
	}

	public void setHeadCircumference(Integer headCircumference) {
		this.headCircumference = headCircumference;
	}

	public Integer getOral() {
		return oral;
	}

	public void setOral(Integer oral) {
		this.oral = oral;
	}

	public List<Integer> getOralCavity() {
		return oralCavity;
	}

	public void setOralCavity(List<Integer> oralCavity) {
		this.oralCavity = oralCavity;
	}

	public Integer getMucositis() {
		return mucositis;
	}

	public void setMucositis(Integer mucositis) {
		this.mucositis = mucositis;
	}

	public Integer getMrynessOfMouth() {
		return mrynessOfMouth;
	}

	public void setMrynessOfMouth(Integer mrynessOfMouth) {
		this.mrynessOfMouth = mrynessOfMouth;
	}

	public Integer getUlceration() {
		return ulceration;
	}

	public void setUlceration(Integer ulceration) {
		this.ulceration = ulceration;
	}

	public Integer getHalitosis() {
		return halitosis;
	}

	public void setHalitosis(Integer halitosis) {
		this.halitosis = halitosis;
	}

	public Integer getBleedingGums() {
		return bleedingGums;
	}

	public void setBleedingGums(Integer bleedingGums) {
		this.bleedingGums = bleedingGums;
	}

	public Integer getCogatedTongue() {
		return cogatedTongue;
	}

	public void setCogatedTongue(Integer cogatedTongue) {
		this.cogatedTongue = cogatedTongue;
	}

	public Integer getCleftPalate() {
		return cleftPalate;
	}

	public void setCleftPalate(Integer cleftPalate) {
		this.cleftPalate = cleftPalate;
	}

	public Integer getCleftLip() {
		return cleftLip;
	}

	public void setCleftLip(Integer cleftLip) {
		this.cleftLip = cleftLip;
	}

	public String getOralCavityDesc() {
		return oralCavityDesc;
	}

	public void setOralCavityDesc(String oralCavityDesc) {
		this.oralCavityDesc = oralCavityDesc;
	}

	public Integer getDentalHygiene() {
		return dentalHygiene;
	}

	public void setDentalHygiene(Integer dentalHygiene) {
		this.dentalHygiene = dentalHygiene;
	}

	public Integer getMotorDeficits() {
		return motorDeficits;
	}

	public void setMotorDeficits(Integer motorDeficits) {
		this.motorDeficits = motorDeficits;
	}

	public String getMotorDeficitsText() {
		return motorDeficitsText;
	}

	public void setMotorDeficitsText(String motorDeficitsText) {
		this.motorDeficitsText = motorDeficitsText;
	}

	public Integer getSensoryDeficits() {
		return sensoryDeficits;
	}

	public void setSensoryDeficits(Integer sensoryDeficits) {
		this.sensoryDeficits = sensoryDeficits;
	}

	public String getSensoryDeficitsText() {
		return sensoryDeficitsText;
	}

	public void setSensoryDeficitsText(String sensoryDeficitsText) {
		this.sensoryDeficitsText = sensoryDeficitsText;
	}

	public Integer getImpairedDecisionMaking() {
		return impairedDecisionMaking;
	}

	public void setImpairedDecisionMaking(Integer impairedDecisionMaking) {
		this.impairedDecisionMaking = impairedDecisionMaking;
	}

	public String getImpairedDecisionMakingText() {
		return impairedDecisionMakingText;
	}

	public void setImpairedDecisionMakingText(String impairedDecisionMakingText) {
		this.impairedDecisionMakingText = impairedDecisionMakingText;
	}

	public Integer getSensation() {
		return sensation;
	}

	public void setSensation(Integer sensation) {
		this.sensation = sensation;
	}

	public Integer getGcsScore() {
		return gcsScore;
	}

	public void setGcsScore(Integer gcsScore) {
		this.gcsScore = gcsScore;
	}

	public Integer getPulseType() {
		return pulseType;
	}

	public void setPulseType(Integer pulseType) {
		this.pulseType = pulseType;
	}

	public List<Integer> getHeartSound() {
		return heartSound;
	}

	public void setHeartSound(List<Integer> heartSound) {
		this.heartSound = heartSound;
	}

	public Integer getS1s2HeardNormally() {
		return s1s2HeardNormally;
	}

	public void setS1s2HeardNormally(Integer s1s2HeardNormally) {
		this.s1s2HeardNormally = s1s2HeardNormally;
	}

	public Integer getS3Heard() {
		return s3Heard;
	}

	public void setS3Heard(Integer s3Heard) {
		this.s3Heard = s3Heard;
	}

	public Integer getS4Heard() {
		return s4Heard;
	}

	public void setS4Heard(Integer s4Heard) {
		this.s4Heard = s4Heard;
	}

	public Integer getPaceMaker() {
		return paceMaker;
	}

	public void setPaceMaker(Integer paceMaker) {
		this.paceMaker = paceMaker;
	}

	public String getPaceMakerText() {
		return paceMakerText;
	}

	public void setPaceMakerText(String paceMakerText) {
		this.paceMakerText = paceMakerText;
	}

	public List<Integer> getPresenceOf() {
		return presenceOf;
	}

	public void setPresenceOf(List<Integer> presenceOf) {
		this.presenceOf = presenceOf;
	}

	public Integer getEdema() {
		return edema;
	}

	public void setEdema(Integer edema) {
		this.edema = edema;
	}

	public Integer getPallor() {
		return pallor;
	}

	public void setPallor(Integer pallor) {
		this.pallor = pallor;
	}

	public Integer getCyanosis() {
		return cyanosis;
	}

	public void setCyanosis(Integer cyanosis) {
		this.cyanosis = cyanosis;
	}

	public Integer getSwelling() {
		return swelling;
	}

	public void setSwelling(Integer swelling) {
		this.swelling = swelling;
	}

	public Integer getClubbing() {
		return clubbing;
	}

	public void setClubbing(Integer clubbing) {
		this.clubbing = clubbing;
	}

	public Integer getColdExtremeties() {
		return coldExtremeties;
	}

	public void setColdExtremeties(Integer coldExtremeties) {
		this.coldExtremeties = coldExtremeties;
	}

	public String getPresenceOfText() {
		return presenceOfText;
	}

	public void setPresenceOfText(String presenceOfText) {
		this.presenceOfText = presenceOfText;
	}

	public Integer getHemoptysis() {
		return hemoptysis;
	}

	public void setHemoptysis(Integer hemoptysis) {
		this.hemoptysis = hemoptysis;
	}

	public Integer getShortnessOfBreath() {
		return shortnessOfBreath;
	}

	public void setShortnessOfBreath(Integer shortnessOfBreath) {
		this.shortnessOfBreath = shortnessOfBreath;
	}

	public Integer getOxygenSupplement() {
		return oxygenSupplement;
	}

	public void setOxygenSupplement(Integer oxygenSupplement) {
		this.oxygenSupplement = oxygenSupplement;
	}

	public String getOxygenSupplementText() {
		return oxygenSupplementText;
	}

	public void setOxygenSupplementText(String oxygenSupplementText) {
		this.oxygenSupplementText = oxygenSupplementText;
	}

	public Integer getIfPresentSpecify() {
		return ifPresentSpecify;
	}

	public void setIfPresentSpecify(Integer ifPresentSpecify) {
		this.ifPresentSpecify = ifPresentSpecify;
	}

	public Integer getBreathingPattern() {
		return breathingPattern;
	}

	public void setBreathingPattern(Integer breathingPattern) {
		this.breathingPattern = breathingPattern;
	}

	public List<Integer> getLungAuscultationAirEntry() {
		return lungAuscultationAirEntry;
	}

	public void setLungAuscultationAirEntry(List<Integer> lungAuscultationAirEntry) {
		this.lungAuscultationAirEntry = lungAuscultationAirEntry;
	}

	public Integer getBilateralEntry() {
		return bilateralEntry;
	}

	public void setBilateralEntry(Integer bilateralEntry) {
		this.bilateralEntry = bilateralEntry;
	}

	public Integer getReducedAirEntryB() {
		return reducedAirEntryB;
	}

	public void setReducedAirEntryB(Integer reducedAirEntryB) {
		this.reducedAirEntryB = reducedAirEntryB;
	}

	public Integer getReducedAirEntryL() {
		return reducedAirEntryL;
	}

	public void setReducedAirEntryL(Integer reducedAirEntryL) {
		this.reducedAirEntryL = reducedAirEntryL;
	}

	public Integer getReducedAirEntryR() {
		return reducedAirEntryR;
	}

	public void setReducedAirEntryR(Integer reducedAirEntryR) {
		this.reducedAirEntryR = reducedAirEntryR;
	}

	public Integer getNoAirEntryB() {
		return noAirEntryB;
	}

	public void setNoAirEntryB(Integer noAirEntryB) {
		this.noAirEntryB = noAirEntryB;
	}

	public Integer getNoAirEntryL() {
		return noAirEntryL;
	}

	public void setNoAirEntryL(Integer noAirEntryL) {
		this.noAirEntryL = noAirEntryL;
	}

	public Integer getNoAirEntryR() {
		return noAirEntryR;
	}

	public void setNoAirEntryR(Integer noAirEntryR) {
		this.noAirEntryR = noAirEntryR;
	}

	public List<Integer> getBreathSounds() {
		return breathSounds;
	}

	public void setBreathSounds(List<Integer> breathSounds) {
		this.breathSounds = breathSounds;
	}

	public Integer getVesicular() {
		return vesicular;
	}

	public void setVesicular(Integer vesicular) {
		this.vesicular = vesicular;
	}

	public Integer getCrepitations() {
		return crepitations;
	}

	public void setCrepitations(Integer crepitations) {
		this.crepitations = crepitations;
	}

	public Integer getWheezing() {
		return wheezing;
	}

	public void setWheezing(Integer wheezing) {
		this.wheezing = wheezing;
	}

	public Integer getRhonchi() {
		return rhonchi;
	}

	public void setRhonchi(Integer rhonchi) {
		this.rhonchi = rhonchi;
	}

	public Integer getCrackles() {
		return crackles;
	}

	public void setCrackles(Integer crackles) {
		this.crackles = crackles;
	}

	public Integer getStridor() {
		return stridor;
	}

	public void setStridor(Integer stridor) {
		this.stridor = stridor;
	}

	public String getBreathSoundsText() {
		return breathSoundsText;
	}

	public void setBreathSoundsText(String breathSoundsText) {
		this.breathSoundsText = breathSoundsText;
	}

	public Integer getTracheostomy() {
		return tracheostomy;
	}

	public void setTracheostomy(Integer tracheostomy) {
		this.tracheostomy = tracheostomy;
	}

	public Integer getGastrointestinal() {
		return gastrointestinal;
	}

	public void setGastrointestinal(Integer gastrointestinal) {
		this.gastrointestinal = gastrointestinal;
	}

	public Integer getNausea() {
		return nausea;
	}

	public void setNausea(Integer nausea) {
		this.nausea = nausea;
	}

	public Integer getVomiting() {
		return vomiting;
	}

	public void setVomiting(Integer vomiting) {
		this.vomiting = vomiting;
	}

	public Integer getDysphagia() {
		return dysphagia;
	}

	public void setDysphagia(Integer dysphagia) {
		this.dysphagia = dysphagia;
	}

	public Integer getIndigestion() {
		return indigestion;
	}

	public void setIndigestion(Integer indigestion) {
		this.indigestion = indigestion;
	}

	public Integer getAbdominalDistention() {
		return abdominalDistention;
	}

	public void setAbdominalDistention(Integer abdominalDistention) {
		this.abdominalDistention = abdominalDistention;
	}

	public Integer getAbdominalCramp() {
		return abdominalCramp;
	}

	public void setAbdominalCramp(Integer abdominalCramp) {
		this.abdominalCramp = abdominalCramp;
	}

	public Integer getHeartBurn() {
		return heartBurn;
	}

	public void setHeartBurn(Integer heartBurn) {
		this.heartBurn = heartBurn;
	}

	public Integer getAbdominalGirth() {
		return abdominalGirth;
	}

	public void setAbdominalGirth(Integer abdominalGirth) {
		this.abdominalGirth = abdominalGirth;
	}

	public Integer getBowelSounds() {
		return bowelSounds;
	}

	public void setBowelSounds(Integer bowelSounds) {
		this.bowelSounds = bowelSounds;
	}

	public List<Integer> getBowelPattern() {
		return bowelPattern;
	}

	public void setBowelPattern(List<Integer> bowelPattern) {
		this.bowelPattern = bowelPattern;
	}

	public Integer getNormal() {
		return normal;
	}

	public void setNormal(Integer normal) {
		this.normal = normal;
	}

	public Integer getConstipation() {
		return constipation;
	}

	public void setConstipation(Integer constipation) {
		this.constipation = constipation;
	}

	public Integer getDiarrhoea() {
		return diarrhoea;
	}

	public void setDiarrhoea(Integer diarrhoea) {
		this.diarrhoea = diarrhoea;
	}

	public Integer getLastDayOfBowelMovement() {
		return lastDayOfBowelMovement;
	}

	public void setLastDayOfBowelMovement(Integer lastDayOfBowelMovement) {
		this.lastDayOfBowelMovement = lastDayOfBowelMovement;
	}

	public Integer getIncisionalWound() {
		return incisionalWound;
	}

	public void setIncisionalWound(Integer incisionalWound) {
		this.incisionalWound = incisionalWound;
	}

	public Integer getOstomies() {
		return ostomies;
	}

	public void setOstomies(Integer ostomies) {
		this.ostomies = ostomies;
	}

	public Integer getStomaStatus() {
		return stomaStatus;
	}

	public void setStomaStatus(Integer stomaStatus) {
		this.stomaStatus = stomaStatus;
	}

	public Integer getDrainsAndTubings() {
		return drainsAndTubings;
	}

	public void setDrainsAndTubings(Integer drainsAndTubings) {
		this.drainsAndTubings = drainsAndTubings;
	}

	public Integer getIntegumentary() {
		return integumentary;
	}

	public void setIntegumentary(Integer integumentary) {
		this.integumentary = integumentary;
	}

	public List<Integer> getSkinTemperature() {
		return skinTemperature;
	}

	public void setSkinTemperature(List<Integer> skinTemperature) {
		this.skinTemperature = skinTemperature;
	}

	public Integer getWarm() {
		return warm;
	}

	public void setWarm(Integer warm) {
		this.warm = warm;
	}

	public Integer getCold() {
		return cold;
	}

	public void setCold(Integer cold) {
		this.cold = cold;
	}

	public Integer getClammySensation() {
		return clammySensation;
	}

	public void setClammySensation(Integer clammySensation) {
		this.clammySensation = clammySensation;
	}

	public String getSkinTemperatureText() {
		return skinTemperatureText;
	}

	public void setSkinTemperatureText(String skinTemperatureText) {
		this.skinTemperatureText = skinTemperatureText;
	}

	public Integer getPressureSore() {
		return pressureSore;
	}

	public void setPressureSore(Integer pressureSore) {
		this.pressureSore = pressureSore;
	}

	public Integer getLocationOfPressureSore() {
		return locationOfPressureSore;
	}

	public void setLocationOfPressureSore(Integer locationOfPressureSore) {
		this.locationOfPressureSore = locationOfPressureSore;
	}

	public Integer getMusculoskeletal() {
		return musculoskeletal;
	}

	public void setMusculoskeletal(Integer musculoskeletal) {
		this.musculoskeletal = musculoskeletal;
	}

	public Integer getAmbulation() {
		return ambulation;
	}

	public void setAmbulation(Integer ambulation) {
		this.ambulation = ambulation;
	}

	public Integer getAnyLimitationsInMovementsOfJoints() {
		return anyLimitationsInMovementsOfJoints;
	}

	public void setAnyLimitationsInMovementsOfJoints(Integer anyLimitationsInMovementsOfJoints) {
		this.anyLimitationsInMovementsOfJoints = anyLimitationsInMovementsOfJoints;
	}

	public String getAnyLimitationsInMovementsOfJointsText() {
		return anyLimitationsInMovementsOfJointsText;
	}

	public void setAnyLimitationsInMovementsOfJointsText(String anyLimitationsInMovementsOfJointsText) {
		this.anyLimitationsInMovementsOfJointsText = anyLimitationsInMovementsOfJointsText;
	}

	public Integer getAdlScore() {
		return adlScore;
	}

	public void setAdlScore(Integer adlScore) {
		this.adlScore = adlScore;
	}

	public Integer getGenitourinary() {
		return genitourinary;
	}

	public void setGenitourinary(Integer genitourinary) {
		this.genitourinary = genitourinary;
	}

	public Integer getUrineOutput() {
		return urineOutput;
	}

	public void setUrineOutput(Integer urineOutput) {
		this.urineOutput = urineOutput;
	}

	public Integer getUrineFrequency() {
		return urineFrequency;
	}

	public void setUrineFrequency(Integer urineFrequency) {
		this.urineFrequency = urineFrequency;
	}

	public String getUrineFrequencyText() {
		return urineFrequencyText;
	}

	public void setUrineFrequencyText(String urineFrequencyText) {
		this.urineFrequencyText = urineFrequencyText;
	}

	public Integer getHaematuria() {
		return haematuria;
	}

	public void setHaematuria(Integer haematuria) {
		this.haematuria = haematuria;
	}

	public String getHaematuriaText() {
		return haematuriaText;
	}

	public void setHaematuriaText(String haematuriaText) {
		this.haematuriaText = haematuriaText;
	}

	public Integer getDysuria() {
		return dysuria;
	}

	public void setDysuria(Integer dysuria) {
		this.dysuria = dysuria;
	}

	public String getDysuriaText() {
		return dysuriaText;
	}

	public void setDysuriaText(String dysuriaText) {
		this.dysuriaText = dysuriaText;
	}

	public Integer getVaginalPenileDischarge() {
		return vaginalPenileDischarge;
	}

	public void setVaginalPenileDischarge(Integer vaginalPenileDischarge) {
		this.vaginalPenileDischarge = vaginalPenileDischarge;
	}

	public String getVaginalPenileDischargeText() {
		return vaginalPenileDischargeText;
	}

	public void setVaginalPenileDischargeText(String vaginalPenileDischargeText) {
		this.vaginalPenileDischargeText = vaginalPenileDischargeText;
	}

	public String getLastMenstrualPeriod() {
		return lastMenstrualPeriod;
	}

	public void setLastMenstrualPeriod(String lastMenstrualPeriod) {
		this.lastMenstrualPeriod = lastMenstrualPeriod;
	}

	public String getPainAssessment() {
		return painAssessment;
	}

	public void setPainAssessment(String painAssessment) {
		this.painAssessment = painAssessment;
	}

	public Integer getSleepDisturbance() {
		return sleepDisturbance;
	}

	public void setSleepDisturbance(Integer sleepDisturbance) {
		this.sleepDisturbance = sleepDisturbance;
	}

	public Integer getUseOfSedatives() {
		return useOfSedatives;
	}

	public void setUseOfSedatives(Integer useOfSedatives) {
		this.useOfSedatives = useOfSedatives;
	}

	public String getSleepDisturbanceText() {
		return sleepDisturbanceText;
	}

	public void setSleepDisturbanceText(String sleepDisturbanceText) {
		this.sleepDisturbanceText = sleepDisturbanceText;
	}

	public String getUseOfSedativesText() {
		return useOfSedativesText;
	}

	public void setUseOfSedativesText(String useOfSedativesText) {
		this.useOfSedativesText = useOfSedativesText;
	}

	public String getStomaStatusText() {
		return stomaStatusText;
	}

	public void setStomaStatusText(String stomaStatusText) {
		this.stomaStatusText = stomaStatusText;
	}

	public Character getIsSubmit() {
		return isSubmit;
	}

	public void setIsSubmit(Character isSubmit) {
		this.isSubmit = isSubmit;
	}

	public String getBowelPatternText() {
		return bowelPatternText;
	}

	public void setBowelPatternText(String bowelPatternText) {
		this.bowelPatternText = bowelPatternText;
	}

	public String getDrainsAndTubingsText() {
		return drainsAndTubingsText;
	}

	public void setDrainsAndTubingsText(String drainsAndTubingsText) {
		this.drainsAndTubingsText = drainsAndTubingsText;
	}

	public Integer getStageI() {
		return stageI;
	}

	public void setStageI(Integer stageI) {
		this.stageI = stageI;
	}
	

	
	

}
