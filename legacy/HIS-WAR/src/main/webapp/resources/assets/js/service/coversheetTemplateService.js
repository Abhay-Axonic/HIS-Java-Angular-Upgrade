angular.module('myApp').service('coversheetTemplateService', ['$http','$rootScope','$cookies','GenericServiceParamHeader' ,function ($http,$rootScope,$cookies,GenericServiceParamHeader) {

var complaintsPatientListDoctor=[];
var medicalHistoryCurrentListDoctor12=[];
var medicalHistoryCurrentListDoctor=[];
var surgicalHistoryCurrentListDoctor12=[];
var surgicalHistoryCurrentListDoctor1=[];
var allergyCurrentListDoctor1=[];
var treatmentHistoryCurrentListDoctor12=[];
var treatmentHistoryCurrentListDoctor =[];
var familyHistoryCurrentListDoctor12 =[];
var familyHistoryCurrentListDoctor12=[];
var familyHistoryCurrentListDoctor=[];
var personalHistoryCurrentListDoctor=[];
var travelHistoryCurrentListDoctor1=[];
var travelHistoryCurrentListDoctor12=[];
var savePatientVitalFinalArr=[];

var saveCovTempListData=function(data){
	if(data != null){

		
		/*****Complaints******/
		if(data.complaintTempList != null && data.complaintTempList.length > 0){
			
			var dataComp = {
					ceratedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updatedBy : data.updatedBy,
					createdBy : data.createdBy,
					status : 'A',
					roleId : data.roleId,
					encounterId : data.encounterId,
					patientId : data.patientId
				};
			angular.forEach(data.complaintTempList,function(value, key) {

								if (value.diagnosisId > 0) {
									/**commented by chetana for save free text complaints*/
									var dData = {
										diagnosisId : value.diagnosisId,
										duration : value.duration,
										since : value.since,
										remark : value.remark,
										isReviewedFlag : 'Y',
										complaintAppoId : '0',
										icpcSystemId : value.icpcSystemId,
										icpcCodeText : value.icpcCodeText
									}
									complaintsPatientListDoctor.push(dData);
									dataComp.listComplaintAppointmentDetailsDto = complaintsPatientListDoctor;

								} 
							});
							var paramObj = {};
//							var headers = {
//								'organization_id' : data.organizationId,
//								'unit_id' : data.unitId,
//								'Content-Type' : 'application/json'
//							};
							var URI = BASE_URL+ ROOT_URL+ SAVECOMPLAINTSLIST + "/"+ $scope.unitId +"/" + $scope.organizationId;
//							data.complaintSaveListLength = dataComp.listComplaintAppointmentDetailsDto.length;
					
							GenericServiceParamHeader.serviceAction(METHOD_POST,URI,dataComp,NOTIFICATION_MSG_STATUS_FALSE,paramObj,headers)
									.then(function(response) {
												if (response.data.status == "success"){
													
												}
									});
			
		}
		/*****Medical History******/
		if(data.medicalTempList != null && data.medicalTempList.length > 0){
//			data.medicalHistoryCurrentListDoctor12=[];
			var dataMed = {					
					created_date:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updated_date:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updated_by:data.userId,
					created_by:data.userId,
					status:'A',
					encounterId :data.encounterId,
					patientId :data.patientId,
					roleId : data.roleId
				};
			angular.forEach(data.medicalTempList, function(value, key) {
				if (value.isNoSignificantStatus == 'Y') {
					var dataList = {};
					dataList.isNoSignificantStatus ='Y',
					dataList.diagnosisId ='',
					dataList.since ="",
					dataList.duration ="",
					dataList.remark ="",
					dataList.patientMedicalHistoryId = value.patientMedicalHistoryId,
					dataList.isEnterInError ="N",
					dataList.isReviewedFlag ="N",
					dataList.diagnosisNameText= '',
					dataList.isDiagnosisName ="N",
					dataList.diagnosisCode= '',
					medicalHistoryCurrentListDoctor12.push(dataList);
				}
			});
			if(medicalHistoryCurrentListDoctor12.length > 0){
				dataMed.listOfPatientMedicalHistoryDto =[];
				dataMed.listOfPatientMedicalHistoryDto = medicalHistoryCurrentListDoctor.concat(medicalHistoryCurrentListDoctor12);
			}
			var paramObj = {};
			var headers={
				'organization_id': data.organizationId,
				'unit_id':data.unitId,
				'Content-Type':'application/json'
			};
			var URI = BASE_URL + ROOT_URL + SAVEPATIENTMEDICALHISTORYDETAILS;

			GenericServiceParamHeader.serviceAction(METHOD_POST, URI,dataMed,NOTIFICATION_MSG_STATUS_TRUE,paramObj,
			headers).then(function(response) {
				console.log(response);
				// $rootScope.startLoader();
				if (response.data.status == "success"){
					
				}
			
			});
			}
		/*****surgical History******/
		if(data.surgicalTempList != null && data.surgicalTempList.length > 0){
//			data.surgicalHistoryCurrentListDoctor12=[];
			var dataSurge = {					
					created_date:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updated_date:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updated_by:data.userId,
					created_by:data.userId,
					status:'A',
					encounterId : data.encounterId,
					patientId : data.patientId,
					roleId : data.roleId
				};
			angular.forEach(data.surgicalTempList, function(value, key) {
				if (value.isNoSignificantStatus == 'Y') {
					var dataTest = {};
					dataTest.isNoSignificantStatus='Y', 
					dataTest.surgeryId= 0,
					dataTest.surgenName="",
					dataTest.month="", 
					dataTest.year="", 
					dataTest.remark="",
					dataTest.isEnterInErrorStatus='N',
					dataTest.isReviewedFlag='N',
					dataTest.surgicalHistoryDetailsId = value.surgicalHistoryDetailsId,
					surgicalHistoryCurrentListDoctor12.push(dataTest);
				}
			});
									
			if(surgicalHistoryCurrentListDoctor12.length > 0){
				dataTest.listOfPatientSurgicalHistoryDto =[];
				dataTest.listOfPatientSurgicalHistoryDto = surgicalHistoryCurrentListDoctor1.concat(surgicalHistoryCurrentListDoctor12);
			}
			
			var paramObj = {};
			var headers={
				'organization_id': data.organizationId,
				'unit_id':data.unitId,
				'Content-Type':'application/json'
			};
			var URI = BASE_URL + ROOT_URL + SAVEPATIENTSURGICALHISTORY;
			GenericServiceParamHeader.serviceAction(METHOD_POST, URI,dataTest,
			NOTIFICATION_MSG_STATUS_TRUE,paramObj,headers).then(function(response) {
				console.log(response);
				// $rootScope.startLoader();
				if (response.data.status == "success")	                 
				{
					
				}	
			});
		}
		/*****surgical History******/
		if(data.lifeTempStyleList != null && data.lifeTempStyleList.length > 0){
			
		}
		/*****surgical History******/
		if(data.allergyTempList != null && data.allergyTempList.length > 0){

				angular.forEach(data.allergyTempList, function(value , key) {
						if(value.allergyTypeId > 0){
			 				var dData = {
			 						isNoKnownAllergies:value.isNoKnownAllergies,
						    	 	allergyTypeId:value.allergyTypeId,
						    	 	allergyId:value.allergyId,
						    	 	genericId:value.genericId,
						    	 	month:value.month,
						    	 	year:value.year,
						    	 	type:value.type,
						    	 	natureOfReaction:value.natureOfReaction,
						    	 	severityId:value.severityId,
						    	 	remark:value.remark,
						    	 	comments:value.comments,
						    	 	isEnterInErrorStatus:value.isEnterInErrorStatus,
									isReviewedFlag:value.isReviewedFlag,
			 				}
			 				
			 				allergyCurrentListDoctor1.push(dData);
						}
					});
				dData.listOfAllergyPatientMapperDto = allergyCurrentListDoctor1;
				
				if($scope.allergyCurrentListDoctor1.length > 0 ){
						var paramObj = {};
						var headers={
								'organization_id': data.organizationId,
								'unit_id':data.unitId,
								'Content-Type':'application/json'
						};
						var URI = BASE_URL + ROOT_URL
						+ SAVEALLERGYPATIENTMAPPER;
				
						GenericServiceParamHeader
						.serviceAction(METHOD_POST, URI,
								dData,
								NOTIFICATION_MSG_STATUS_TRUE,paramObj,headers)
						.then(
								function(response) {
									console.log(response);
									// $rootScope.startLoader();
									if (response.data.status == "success"){
										 
									}
										
								});
				}
				
				
						
			
		}
		/*****medication History******/
		if(data.medicationHistoryTempList != null && data.medicationHistoryTempList.length > 0){
			var medicationHistoryDetails = data.medicationHistoryTempList;
			var medicationHistoryList = [];
			var dataMed = 
			{
				"organizationId": data.organizationId,
				"unitId": data.unitId,
				"encounterId":data.encounterId,
				"patientId":data.patientId,
			};
			
			for (var i=0 ; i< data.medicationHistoryTempList.length; i++){
			var medicationStartDate = moment(medicationHistoryDetails[i].medicationStartDate).format('DD-MM-YYYY');
			var instructionId = medicationHistoryDetails[i].instructionId;
						
			var medicationDetailsHistoryObj = {};
			medicationDetailsHistoryObj["itemId"] = medicationHistoryTempList[i].itemId;
			medicationDetailsHistoryObj["storeId"] = 1; 
			medicationDetailsHistoryObj["orderQty"] = medicationHistoryTempList[i].quantity;
			medicationDetailsHistoryObj["doctorId"] = data.doctorId;
			medicationDetailsHistoryObj["genericName"] = medicationHistoryTempList[i].genericName;
			medicationDetailsHistoryObj["genericId"] = medicationHistoryTempList[i].genericId;
			medicationDetailsHistoryObj["duration"] = medicationHistoryTempList[i].durationCount;
			medicationDetailsHistoryObj["itemName"] = medicationHistoryTempList[i].medicationName;
			medicationDetailsHistoryObj["authorisedDate"] = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
			medicationDetailsHistoryObj["doseId"] = medicationHistoryTempList[i].dose;
			medicationDetailsHistoryObj["doseUnitId"] = medicationHistoryTempList[i].doseUnitId;
			medicationDetailsHistoryObj["frequencyId"] = medicationHistoryTempList[i].frequencyId;
			medicationDetailsHistoryObj["durationUnit"] = medicationHistoryTempList[i].durationUnit;
			medicationDetailsHistoryObj["roaId"] = medicationHistoryTempList[i].roaId;
			medicationDetailsHistoryObj["startDate"] = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
			medicationDetailsHistoryObj["endDate"] = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
			medicationDetailsHistoryObj["createdDate"] = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
			medicationDetailsHistoryObj["updatedDate"] = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
			medicationDetailsHistoryObj["createdBy"] =data.userId;
			medicationDetailsHistoryObj["updatedBy"] = data.userId;
			medicationDetailsHistoryObj["status"] = 'A';
			medicationDetailsHistoryObj["instructionId"] = medicationHistoryTempList[i].instructionId;
			medicationDetailsHistoryObj["isNonFormulary"] = medicationHistoryTempList[i].nonFormulary;
			medicationDetailsHistoryObj["isMedicationContinue"] = medicationHistoryTempList[i].isMedicationContinue;
			medicationDetailsHistoryObj["isMedicationStopHis"] = medicationHistoryTempList[i].isMedicationStopHis;
			medicationDetailsHistoryObj["isNoSignificantMedication"] = data.noSignificantMedicationHist;
			medicationHistoryList.push(medicationDetailsHistoryObj);
			}
			dataMed.medicationHistoryList = medicationHistoryList;
			
			var URI = BASE_URL + ROOT_URL + "/api/opd/saveMedicationHistory"
								
			GenericServiceParamHeader.serviceAction(METHOD_POST, URI, dataMed, NOTIFICATION_MSG_STATUS_TRUE)
			.then(function(response) {
				if(response.data.status == "success"){
					{
						
					}
												
				}
			});
		}
		/*****treatment History******/
		if(data.treatmentTempList != null && data.treatmentTempList.length > 0){
			data.treatmentHistoryCurrentListDoctor12=[];
			var dataTreat = {					
					created_date:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updated_date:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updated_by:data.userId,
					created_by:data.userId,
					status:'A',
					encounterId : data.encounterId,
					patientId : data.patientId,
					roleId : data.roleId
				};
			angular.forEach(data.treatmentHistoryCurrentList, function(value, key) {
				if (value.isNoSignificantStatus == 'Y') {
					var dataTest = {};
					dataTest.month="",
					dataTest.year="",
					dataTest.remark ="",
					dataTest.isEnterInErrorStatus ='N',
					dataTest.isReviewedFlag ='N',
					dataTest.treatementName = "",
					dataTest.treatDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					dataTest.surgenName = "",
					dataTest.isNoSignificantStatus ='Y',
					dataTest.treatmentHistoryId = value.treatmentHistoryId,
					treatmentHistoryCurrentListDoctor12.push(dataTest);
				}
			});						
			if(treatmentHistoryCurrentListDoctor12.length > 0){
				dataTreat.listOfPatientTreatmentHistoryDto =[];
				dataTreat.listOfPatientTreatmentHistoryDto = treatmentHistoryCurrentListDoctor.concat(treatmentHistoryCurrentListDoctor12);
			}	
			var paramObj = {};
			var headers={
				'organization_id': data.organizationId,
				'unit_id':data.unitId,
				'Content-Type':'application/json'
			};
			var URI = BASE_URL + ROOT_URL + "/api/opd/savePatientTreatmentHistoryDetails";
			GenericServiceParamHeader.serviceAction(METHOD_POST, URI,dataTreat,NOTIFICATION_MSG_STATUS_TRUE,paramObj,headers).then(function(response) {
				console.log(response);
				// $rootScope.startLoader();
				if (response.data.status == "success")
					{
					
					}
			});
		}
		/*****family History******/
		if(data.familyTempList != null && data.familyTempList.length > 0){
//			familyHistoryCurrentListDoctor12=[];
			var dataFam = {										
					createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updatedBy:data.userId,
					createdBy:data.userId,
					status:'A',
					encounterId : data.encounterId,
					patientId : data.patientId,
					roleId : data.roleId,									
				};
			angular.forEach(data.familyHistoryCurrentList, function(value, key) {
				if (value.isNoFamilyHistoryStatus == 'Y') {
					var dataTest = {};
					dataTest.isNoFamilyHistoryStatus = 'Y',
					dataTest.diagnosisId = "",
					dataTest.relationId = "",
					dataTest.remark = "",
					dataTest.familyHistoryId = value.familyHistoryId,
					dataTest.isEnterInErrorStatus = "N",
					dataTest.isReviewedFlag = "N",
					dataTest.isDiagnosisName = "N",
					dataTest.diagnosisNameText = "",
					dataTest.diagnosisCode = "",
					familyHistoryCurrentListDoctor12.push(dataTest);
				}
			});
		
			if(familyHistoryCurrentListDoctor12.length > 0){
				dataFam.listOfPatientFamilyHistoryDtos=[];
				dataFam.listOfPatientFamilyHistoryDtos = familyHistoryCurrentListDoctor.concat(familyHistoryCurrentListDoctor12);
			}
			var paramObj = {};
			var headers={
				'organization_id': data.organizationId,
				'unit_id':data.unitId,
				'Content-Type':'application/json'
			};
			// return false;
			var URI = BASE_URL + ROOT_URL + SAVEPATIENTFAMILYHISTORY;

			GenericServiceParamHeader.serviceAction(METHOD_POST, URI,dataFam,NOTIFICATION_MSG_STATUS_TRUE,
			paramObj,headers).then(function(response) {
				console.log(response);
				// $rootScope.startLoader();
				if (response.data.status == "success")
			      {
					
			      }
			});
		}
		/*****personal History******/
		if(data.personalTempList != null && data.personalTempList.length > 0){
//			data.personalHistoryCurrentListDoctor=[];
			var dataPer = {		
					createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updatedBy:data.userId,
					createdBy:data.userId,
					status:'A',
					encounterId : data.encounterId,
					patientId : data.patientId,
					roleId : data.roleId,	
				}
			angular.forEach(data.personalTempList, function(value , key) {
				if(( value.description != "") && (value.remark =="" || value.remark !="")){
					var dData = {
						isNoSignificantHistoryStatus : value.isNoSignificantHistoryStatus,
						description:value.description,
						remark : value.remark,
						isEnterInErrorStatus:value.isEnterInErrorStatus,
						isReviewedFlag:value.isReviewedFlag
					}
					personalHistoryCurrentListDoctor.push(dData);
					dataPer.listOfPatientPersonalHistoryDtos = personalHistoryCurrentListDoctor;

				}
				});
				
			var paramObj = {};
			var headers={
				'organization_id': data.organizationId,
				'unit_id':data.unitId,
				'Content-Type':'application/json'
			};
			var URI = BASE_URL + ROOT_URL + SAVEPATIENTPERSONALHISTORYDETAILS;
			GenericServiceParamHeader.serviceAction(METHOD_POST, URI,dataPer,
			NOTIFICATION_MSG_STATUS_TRUE,paramObj,headers).then(
			function(response) {
				console.log(response);
				// $rootScope.startLoader();
				if (response.data.status == "success")
					{
					
					}
			});
		}
		/*****surgical History******/
		if(data.vaccinationTempList != null && data.vaccinationTempList.length > 0){
			
		}
		/*****travel History******/
		if(data.travelTempList != null && data.travelTempList.length > 0){
//			data.travelHistoryCurrentListDoctor12=[];
			var dataTravel = {		
					createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updatedBy:data.userId,
					createdBy:data.userId,
					status:'A',
					encounterId: data.encounterId,
					patientId : data.patientId,
					roleId:data.roleId
					// listOfPatientTravelHistoryDtos:data.savePatientTravelHistory	
				};
			angular.forEach(data.travelTempList, function(value, key) {
				if (value.description != "") {
					var dData = {
						description : value.description,
						whenTravel : value.whenTravel,
						isEnterInErrorStatus : value.isEnterInErrorStatus,
						isReviewedFlag : value.isReviewedFlag,
						isNoSignificantTravel:'N'
					}
					travelHistoryCurrentListDoctor1.push(dData);
					dataTravel.listOfPatientTravelHistoryDtos = travelHistoryCurrentListDoctor1;
						
				} 
				if(data.travelHistoryCurrentListDoctor12.length >0){
					dataTravel.listOfPatientTravelHistoryDtos=[];
					dataTravel.listOfPatientTravelHistoryDtos = travelHistoryCurrentListDoctor12.concat(travelHistoryCurrentListDoctor1);
				}
				var headers = {
						'organization_id' : data.organizationId,
						'unit_id' : data.unitId,
						'Content-Type' : 'application/json'
					};
					console.log("testData",data);
					//return false;
					
						var URI = BASE_URL + ROOT_URL + SAVEPATIENTTRAVELHISTORYDETAILS;
						$rootScope.startLoader();
						GenericServiceParamHeader.serviceAction(METHOD_POST,URI,dataTravel,NOTIFICATION_MSG_STATUS_TRUE,
						paramObj, headers).then(function(response) {
							console.log(response);
							// $rootScope.startLoader();
							$rootScope.stopLoader();
							if (response.data.status == "success")
								{
								
								}
						});
					
			});
		}
		/*****Vitals History******/
		if(data.vitalTempList != null && data.vitalTempList.length > 0){
			var paramObj = {};
			var headers={
//					'organization_id': data.organizationId,
//					'unit_id':data.unitId,
					'Content-Type':'application/json'
			};
			
			angular.forEach(data.vitalTempList, function(value, key) {
				  if(value.vitalValue == "")
					  {
					  data.counter++;
					  }
				  
				  var dataVital = {
							vitalId : value.vitalId,
							value : value.vitalValue,
							isReviewedFlag : value.isReviewedFlag,
							vitalMethodList: value.vitalMethodList,
							vitalPositionList: value.vitalPositionList,
							vitalLocationList: value.vitalLocationList,
							vitalSiteList: value.vitalSiteList,
							vitalQualityList: value.vitalQualityList,
							vitalCuffList: value.vitalCuffList,
							
						}
				  if(data.roleId == 2)
		 		{
					  dataVital.appointmentVitalMapperId =value.appointmentVitalMapperId;
		 		}
				  
				  $scope.savePatientVitalFinalArr.push(dataVital);
	      });
			var dataTest = {
					 patientId : data.patientId,
	    		 	encounterId : data.encounterId,
	    		 	roleId : data.roleId,
	    		 	createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updatedBy:data.userId,
					createdBy:data.userId,
					status:'A',
					isEnteredInError:'N',
					isPostFacto:data.isPostFacto,
	               postFactoDate:moment(data.postFactoDate).format('DD-MM-YYYY HH:mm:ss'),
	               postFactoTime:data.postFactoTime!="" ? data.postFactoTime+":00" :currtimePost,
	               listAppointmentVitalMapperDtos : $scope.savePatientVitalFinalArr
			  }
			 var URI = BASE_URL + ROOT_URL
				+ SAVEAPPOINTMENTVITALMAPPER + "/"+ $scope.unitId +"/" + $scope.organizationId;
		
				GenericServiceParamHeader
				.serviceAction(METHOD_POST, URI,
						dataTest,
						NOTIFICATION_MSG_STATUS_TRUE,paramObj,headers)
				.then(
						function(response) {
							console.log(response);
							/* $rootScope.startLoader();*/
							if (response.data.status == "success")
								{
								
								}
						});
		}
		/*****general Examination History******/
		/*if(data.generalExaminationTempList != null && data.generalExaminationTempList.length > 0){
			var data = {
					organizationId: data.organizationId,
					unitId: data.unitId,
					typeId: parseInt(data.typeId),
					patientId: data.patientId,
					status: 'A',
					encounterId: data.encounterId,
					admissionId: null,
					createdBy: data.userId,
					descriptiveExamination: null,
					isNADValue: item.isNADValue,
					remark: item.remark,
					status: 'A',
					systemId: item.systemId,
					systemObervationPropertyId: item.systemObervationPropertyId,
					systemObervationId: item.systemObervationId,
					updatedBy: data.userId,
					updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					generalExamAppoMapperId: item.generalExamAppoMapperId
				}
				var URI = BASE_URL + ROOT_URL + "/api/opd/saveGeneralAndSystemicExaminationMapperForNewCoversheet";
				$rootScope.startLoader();
				GenericService.serviceAction("POST", URI, data).then(function(response) {
					if (response.data.status == "success"){
						$rootScope.stopLoader();
						data.getExaminationListData = response.data.listObject;
						growl.success("Updated Successfully..!");
						data.getExaminationList();
					}
				});
		}*/
		/*****systemic examination History******/
		/*if(data.systemicExaminationTempList != null && data.systemicExaminationTempList.length > 0){
			data.typeId="";
			var data = {
					organizationId: data.organizationId,
					unitId: data.unitId,
					typeId: parseInt(data.typeId),
					patientId: data.patientId,
					status: 'A',
					encounterId: data.encounterId,
					admissionId: null,
					createdBy: data.userId,
					descriptiveExamination: null,
					isNADValue: item.isNADValue,
					remark: item.remark,
					status: 'A',
					systemId: item.systemId,
					systemObervationPropertyId: item.systemObervationPropertyId,
					systemObervationId: item.systemObervationId,
					updatedBy: data.userId,
					updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					generalExamAppoMapperId: item.generalExamAppoMapperId
				}
				var URI = BASE_URL + ROOT_URL + "/api/opd/saveGeneralAndSystemicExaminationMapperForNewCoversheet";
				$rootScope.startLoader();
				GenericService.serviceAction("POST", URI, data).then(function(response) {
					if (response.data.status == "success"){
						$rootScope.stopLoader();
						data.getExaminationListData = response.data.listObject;
						growl.success("Updated Successfully..!");
						data.getExaminationList();
					}
				});
		}*/
		/*****diagnosis History******/
		if(data.diagnosisTempList != null && data.diagnosisTempList.length > 0){
			var dataDia = {
					
					created_date:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updated_date:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updated_by:data.userId,
					created_by:data.userId,
					status:'A',
					listDiagnosisPatientAppointmentDetailsDto:data.diagnosisTempList,
					encounterId: data.encounterId,
					justificationCancel: 'N',
					
			};
			
			var paramObj = {};
			var headers={
					'organization_id': data.organizationId,
					'unit_id':data.unitId,
					'Content-Type':'application/json',
					'patient_id' : data.patientId,
			};
			// return false;
			
			var URI = BASE_URL + ROOT_URL
			+ SAVEPATIENTDIAGNOSIS;

			GenericServiceParamHeader
			.serviceAction(METHOD_POST, URI,
					dataDia,
					NOTIFICATION_MSG_STATUS_FALSE,paramObj,headers)
			.then(
					function(response) {
						console.log(response);
					/*	 $rootScope.startLoader();*/
						if (response.data.status == "success")
							{
							}
							});
		}
		/*****diagnostics ******/
		if(data.diagnosticsTempList != null && data.diagnosticsTempList.length > 0){
			var finalData = {
	    			orgId : data.organizationId,
	    			orgUnitId:data.unitId
	    	};
			
	    	var dataOrd = {
	    			
	    			currentDate : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
	    			createdDate:new Date().getTime(),
	    			updatedDate:new Date().getTime(),
	    			orgId : data.organizationId,
	    			orgUnitId:data.unitId,
	    			createdBy  : data.userId,
					updatedBy : data.userId,
					patientId : data.patientId,
					encounterId : data.encounterId,
					defaultSelfTariffId : data.defaultSelfTariffId,
					paymentEntitlementTypeId : data.paymentEntitlementTypeId,
					doctorId :data.doctorId,
					ordDocSplId :data.specialityId,
					priorityId : 2,
					visitTypeId : 1,
					orderDate:new Date().getTime(),
					orderStatus:'A',
					orderStatusId:1,
					contractId : data.contractId,
					payeeId : data.payeeId,
					billingBedCategoryId:data.opdBillingCatId,
					patientTypeId:data.patientDetails.patientCategoryId,
					
	    	}
	    	var data1 = {
	    			
	    			currentDate : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
	    			createdDate:new Date().getTime(),
	    			updatedDate:new Date().getTime(),
	    			orgId : data.organizationId,
	    			orgUnitId:data.unitId,
	    			createdBy  : data.userId,
					updatedBy : data.userId,
					patientId : data.patientId,
					defaultSelfTariffId : data.defaultSelfTariffId,
					paymentEntitlementTypeId : data.paymentEntitlementTypeId,
					doctorId :data.doctorId,
					ordDocSplId :data.specialityId,
					priorityId : 2,
					visitTypeId : 1,
					orderDate:new Date().getTime(),
					orderStatus:'A',
					orderStatusId:1,
					billingBedCategoryId:data.opdBillingCatId,
					patientTypeId:data.patientDetails.patientCategoryId,
	    	}
	    	
	    	var finalOrderServiceList = [];
	    	var finalOrderServiceListForFuture = [];
	    	var justChkFlgFut = 0;
	    	var justChkFlgCurr = 0;
	    	angular.forEach(data.diagnosticsTempList, function(value , key) {
	    		var newArr = {};
	    		// newArr.encounterId =data.encounterId ;
	    		if(value.selectedForFuture == 'N')
	    			{
//	    		newArr.ordDocId =data.doctorId ;
	    		newArr.serviceId = value.serviceMasterId;
//	    		newArr.ordDocSplId =data.specialityId;
	    		newArr.serviceRenderingDeptId = value.subSpecialityId;
	    		newArr.apportionedPrice = value.apportionedPrice;
	    		newArr.isPackageService = value.isPackageService;
	    		newArr.packageId = value.packageId;
	    		newArr.packageTypeId = value.packageTypeId; 
	    		newArr.packageEitherOrGroupId = value.packageEitherOrGroupId; 
	    		newArr.packageCapDetailsId = value.packageCapDetailsId;
	    		newArr.isCapService = value.isCapService;
	    		newArr.packageConsumptionAmt = value.packageConsumptionAmt;
	    		newArr.status = 'A';
	    		newArr.isRefDoctorShare = value.isRefDoctorShare;
	    		newArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
	    		newArr.isPackageService =value.isPackageService;
	    		newArr.departmentId = value.specialityId;
	    		newArr.subDepartmentId = value.subSpecialityId;
	    		newArr.isInvestigationOrder  ='Y';
	    		newArr.orderDetailsId=value.orderDetailsIdP;
	    		newArr.orderSetId = value.orderSetId;
	    		if(value.orderDetailsIdP!=undefined && value.orderDetailsIdP!=null && value.orderDetailsIdP > 0){
	    			newArr.packageTypeId=1;        		
	    		}
	    		newArr.orderSchDate=new Date().getTime();
	    		newArr.nursingServiceOrderFlag ='N';
	    		newArr.procedureOrderMstId = data.proceduerReqId;
	    		finalOrderServiceList.push(newArr);
	    		justChkFlgCurr = 1;
	    		}
	    		if(value.selectedForFuture == 'Y')
				{
//	    			newArr.ordDocId =data.doctorId ;
	    			newArr.serviceId = value.serviceMasterId;
//	    			newArr.ordDocSplId =data.specialityId;
	    			newArr.serviceRenderingDeptId = value.subSpecialityId;
	    	  		newArr.apportionedPrice = value.apportionedPrice;
	        		newArr.isPackageService = value.isPackageService;
	        		newArr.packageId = value.packageId;
	        		newArr.orderSetId = value.orderSetId;
	        		newArr.packageTypeId = value.packageTypeId; 
	        		newArr.packageEitherOrGroupId = value.packageEitherOrGroupId; 
	        		newArr.packageCapDetailsId = value.packageCapDetailsId;
	        		newArr.isCapService = value.isCapService;
	        		newArr.packageConsumptionAmt = value.packageConsumptionAmt;
	        		newArr.status = 'A';
	        		newArr.isRefDoctorShare = value.isRefDoctorShare;
	        		newArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
	        		newArr.isPackageService =value.isPackageService;
	        		newArr.orderSetId = value.orderSetId;
	        		newArr.orderSchDate=new Date(value.investigationFutureDate).getTime(),
//	        		newArr.orderSchDate= moment(value.investigationFutureDate.getTime()).format('HH:mm:ss');
	        		newArr.nursingServiceOrderFlag ='N';
	        		newArr.procedureOrderMstId = data.proceduerReqId;
	    			finalOrderServiceListForFuture.push(newArr);
	    			justChkFlgFut = 1;
	    			
	    		}
	    	});
	    	dataOrd.listOrderDetailsMasterDto = finalOrderServiceList;
	    	data1.listOrderDetailsMasterDto = finalOrderServiceListForFuture;
	    	
	    	if(justChkFlgCurr == 1)
	    	listOrderMasterDto.push(dataOrd);
	    	
	    	if(justChkFlgFut == 1)
	    	listOrderMasterDto.push(data1);
	    	finalData.listOrderMasterDto = listOrderMasterDto;
	    	// add modality list
	    	var slotMasterDtoList = [];
	    	finalData.slotMasterDtoList=dataOrd.radiologyAppointmentList;
	    	console.log("INV",finalData);
	    	
	    	if(listOrderMasterDto.length > 0){
	    	 //return false;
			var URI = BASE_URL + ROOT_URL + SAVEINVESTIGATION
			GenericServiceParamHeader.serviceAction(METHOD_POST, URI, 
					finalData, NOTIFICATION_MSG_STATUS_TRUE)
					.then(function(response) {
						if(response.data.status == "success"){
							{
								
							}
						}
						
			});
	    	}
		}
		/*****order medicaton******/
		if(data.medicationTempList != null && data.medicationTempList.length > 0){
			var dataMedi = {
					  "organizationId": data.organizationId,
					  "unitId": data.unitId,
					  "employeeId" : data.employeeId,
					  "visitTypeId":1,
					  "doctorId":data.doctorId,
					  "prescriptionDate":moment(new Date()).format('YYYY-MM-DD  HH:mm:ss'),
					  "authorisedBy": data.doctorId,
					  "authorisedDate":moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
					  "addedBy": data.userId,
					  "addedDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
					  "encounterId":data.encounterId,
					  "patientId":data.patientId,
					  "priorityId":2,
					  "deptId" : data.specialityId,
					  "isFromOP" :true
					  
			};
			angular.forEach(data.medicationTempList, function(value, key) {

				var instructionId = value.instructionId;
				if (value.medicationStartDate != ''
						&& value.medicationEndDate != ''
						&& value.durationCount != ''
						&& value.durationUnit != ''
						&& value.quantity != '') {

					var medicationDetailsObj = {};
					medicationDetailsObj["itemId"] = value.itemId;
					medicationDetailsObj["storeId"] = data.storeId;
					medicationDetailsObj["uomUnitId"] = value.uomTypeId;
					medicationDetailsObj["isMedicationStop"] = 'N';
					medicationDetailsObj["orderQty"] = value.quantity;
					medicationDetailsObj["strength"] = value.strength;
	                medicationDetailsObj["durationCount"] = value.durationCount;
	                medicationDetailsObj["durationUnit"] = value.durationUnit;

					medicationDetailsObj["prescriptionDate"] = moment(
							new Date())
							.format(
									'YYYY-MM-DD HH:MM:SS');
					medicationDetailsObj["authorisedBy"] = data.doctorId;
					medicationDetailsObj["authorisedDate"] = moment(
							new Date())
							.format(
									'YYYY-MM-DD HH:MM:SS');
					medicationDetailsObj["dose"] = value.dose;
					medicationDetailsObj["itemName"] = value.medicationName;
					medicationDetailsObj["genericName"] = value.genericName;
					medicationDetailsObj["doseUnitId"] = value.doseUnitId;
					medicationDetailsObj["frequencyId"] = value.frequencyId;
					medicationDetailsObj["routeId"] = value.roaId;
					medicationDetailsObj["isNonFormulary"] = "N";
					medicationDetailsObj["startDate"] = value.medicationStartDate
							+ " 00:00:00";
					medicationDetailsObj["stopDate"] = value.medicationEndDate
							+ " 00:00:00";
					medicationDetailsObj["statflag"] = value.isStat!=null?value.isStat:false;
					medicationDetailsObj.isDischarge = value.isDischarge;
					if(value.prescribedDosageUnitList != undefined){
					if(value.prescribedDosageUnitList.length>0 && value.formulationCounterIndex!="" && value.formulationCounterIndex!=null
							&& value.formulationCounterIndex!=undefined){
						 var list=$.grep(value.prescribedDosageUnitList, function (obj) {
					        return value.formulationCounterIndex === obj.counterIndex;
					    });
						 if(list.length>0){
							 if(list[0].formulationFactorId==1){
								 medicationDetailsObj.prescribedDose=value.prescribeDose;
								 medicationDetailsObj.prescribedDoseUnitId=value.prescribedDoseUnitId;
								 medicationDetailsObj.factorId=list[0].formulationFactorId;
								 medicationDetailsObj.formulationDosageMappId=list[0].formulationDosageMappId;
							 }else{
								 medicationDetailsObj.factorId=list[0].formulationFactorId;
								 medicationDetailsObj.formulationDosageMappId=list[0].formulationDosageMappId;
							 }
						 }
						
					}
					}
					if(value.instructionId != '' && value.instructionId != undefined  && value.instructionId != null ){
						medicationDetailsObj["instructionId"] = value.instructionId;
						}else{
						 medicationDetailsObj["instructionDesc"] = value.instructionDesc;
						}
					medicationDetailList.push(medicationDetailsObj);
				} else {
					flag = false;
				}
				
			});
			if (data.medicationTempList.length > 0) {
				dataMedi.prescriptionDetailsDtosList = data.medicationTempList;
				console.log("medicationDetailList", data);
				var URI = BASE_URL + ROOT_URL
						+ SAVEPATIENTMEDICATION

				GenericServiceParamHeader
						.serviceAction(METHOD_POST, URI,
								dataMedi,
								NOTIFICATION_MSG_STATUS_TRUE)
						.then(
								function(response) {
									if (response.data.status == "success") {
										
									}
								});
				
			}
		}
		/*****advice*****/
		if(data.adviceTempList != null && data.adviceTempList.length > 0){
			var dataAdvice = {
					unitId: data.unitId,
					organizationId:data.organizationId,
					patientId: data.patientId,
					encounterId: data.encounterId,
					createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					createdBy: data.userId,
					updatedBy: data.userId,
					status:'A',
					coversheetInstructionDtoslist:data.addedInstructions	
			};
			
		if(data.advicedInstruction != null && data.advicedInstruction !=""){
			var newInstruction={};
			newInstruction.instructionDesc=data.advicedInstruction;
			newInstruction.advicedInstruction=data.advicedInstruction;
			newInstruction.unitId= data.unitId,
			newInstruction.organizationId=data.organizationId,
			newInstruction.patientId=data.patientId,
			newInstruction.encounterId= data.encounterId,
			newInstruction.createdDate=moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
			newInstruction.updatedDate= moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
			newInstruction.createdBy= data.userId,
			newInstruction.updatedBy= data.userId,
			newInstruction.status='A',
			dataAdvice.addedInstructions.push(newInstruction);
		}
			
			if (data.addedInstructions.length > 0 || (data.advicedInstruction!=null && data.advicedInstruction!="")) {
			var URI = BASE_URL + ROOT_URL + SAVEINSTRUCTIONS;
			
			GenericServiceParamHeader.serviceAction(METHOD_POST, URI,
					dataAdvice, NOTIFICATION_MSG_STATUS_TRUE)
						.then(function(response) {
							if(response.data.status == "success"){
								
							}
			});
			}
		}
		/*****surgical History******/
		if(data.consultOrderTempList != null && data.consultOrderTempList.length > 0){
			if (data.addedConsultOrderForDisplay.length > 0) {
			var addedConsultOrder = data.addedConsultOrderForDisplay
			var consultOrder = []
			for (var i = 0; i < addedConsultOrder.length; i++) {
				var consultOrderObj = {}
				consultOrderObj["patientId"] = addedConsultOrder[i].patientId
				consultOrderObj["refToSpecialityId"] = addedConsultOrder[i].refToSpecialityId
				consultOrderObj["refBySpecialityId"] = addedConsultOrder[i].refBySpecialityId
				consultOrderObj["refToDoctorId"] = addedConsultOrder[i].refToDoctorId
	// consultOrderObj["priorty"] = addedConsultOrder[i].priorty
				consultOrderObj["referredStatus"] = 'Pending'
				consultOrderObj["createdBy"] = data.userId
				consultOrderObj["updatedBy"] = data.userId;
				consultOrderObj["encounterId"] = data.encounterId;
				consultOrderObj["visitTypeId"]=data.visitTypeId;
				consultOrderObj["unitId"] =data.unitId;
				consultOrderObj["referredUnitId"] = data.selectedValues.unitId;
				consultOrderObj["organizationId"] = data.organizationId;
				consultOrderObj["notes"] = addedConsultOrder[i].notes;
				consultOrderObj["priority"] = addedConsultOrder[i].priority;
				consultOrderObj["createdDate"] = moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
				consultOrderObj["updatedDate"] = moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
				consultOrderObj["status"] = 'A';
				consultOrderObj["refByDoctorId"] = addedConsultOrder[i].refByDoctorId;
				consultOrderObj["outsideHospitalId"] = addedConsultOrder[i].outsideHospitalId;
				consultOrderObj["outsideHosDocId"] =addedConsultOrder[i].outsideHosDocId;

				consultOrder.push(consultOrderObj)
			}
			
			var URI = BASE_URL + ROOT_URL
			+ SAVECONSULTORDER;

	GenericServiceParamHeader
			.serviceAction(METHOD_POST, URI,
					consultOrder,
					NOTIFICATION_MSG_STATUS_TRUE)
			.then(
					function(response) {
						if (response.data.status == "success") {
							
						}
					});
			}
		}
		

		return response;
	}
	
};
return {
	saveCovTempListData: saveCovTempListData,
	complaintsPatientListDoctor: complaintsPatientListDoctor,
	medicalHistoryCurrentListDoctor12:medicalHistoryCurrentListDoctor12,
	medicalHistoryCurrentListDoctor:medicalHistoryCurrentListDoctor,
	surgicalHistoryCurrentListDoctor12 :surgicalHistoryCurrentListDoctor12,
	surgicalHistoryCurrentListDoctor1 :surgicalHistoryCurrentListDoctor1,
	allergyCurrentListDoctor1 : allergyCurrentListDoctor1,
	treatmentHistoryCurrentListDoctor12 : treatmentHistoryCurrentListDoctor12,
	treatmentHistoryCurrentListDoctor : treatmentHistoryCurrentListDoctor,
	familyHistoryCurrentListDoctor12 : familyHistoryCurrentListDoctor12,
	familyHistoryCurrentListDoctor : familyHistoryCurrentListDoctor,
	personalHistoryCurrentListDoctor : personalHistoryCurrentListDoctor,
	travelHistoryCurrentListDoctor1 : travelHistoryCurrentListDoctor1,
	travelHistoryCurrentListDoctor12 : travelHistoryCurrentListDoctor12,
	savePatientVitalFinalArr : savePatientVitalFinalArr,
  };
}]);