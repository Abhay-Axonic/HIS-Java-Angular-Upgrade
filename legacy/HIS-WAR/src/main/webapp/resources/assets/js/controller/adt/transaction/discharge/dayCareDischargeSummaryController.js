
angular.module('myApp')
		.controller("dayCareDischargeSummaryController",
		['$scope','$rootScope','$state','$timeout','$cookies','GenericService','BillingGenericService','PagerService','promiseFactory','$sessionStorage','growl','reportService','GenericServiceParamHeader',
		function($scope,$rootScope,$state,$timeout,$cookies, GenericService,BillingGenericService,PagerService,promiseFactory,$sessionStorage,growl,reportService,GenericServiceParamHeader){
	
			$rootScope.loginpage = true;
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
	
			/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.totalNoOfRecords;
			//$scope.searchString='';
			$scope.commonListCount;
			$scope.dischargeSummaryId;
			
			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			$scope.masterObj = {};
			$scope.unitId = cookieObject.unitId;
			$scope.organizationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
			$scope.doctorId = cookieObject.doctorId;
			
			var cookieObject,cookieObjectAppConfig;
			[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
			$scope.patientCategoryId = cookieObjectAppConfig.patientCategoryId;

			$scope.dateOptions = {
					formatYear : 'yyyy',
					showWeeks : false
				};
			$scope.openDatePicker = function() {
				$scope.datepicker.opened = true;
			};
			
			$scope.loadingMessage="";
			// for popup
			$scope.showModal = false;
			$scope.buttonClicked = "";
			$scope.popUpFlag = true;
			$scope.isSaveDisable=true;
			$scope.disablePrintBtn=true;
			$scope.disableReleaseBtn=true;
			// end for popup
			
			//For Get Active Gender List
			$scope.getSexMasterList = function() {
				var URI = BASE_URL + ROOT_URL + GETACTIVEGENDERLIST;
				BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					$scope.sexMasterList = response.data.listObject;
				});
			}
			
			$scope.getIdentificationTypeList = function(){
				var URI = BASE_URL + ROOT_URL + GETACTIVEIDENTIFICATION;
				BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success")
						$scope.identificationTypeList = response.data.listObject;
				});
			}
			
		
			$scope.getCardTypesList = function(){
				var URI = BASE_URL + ROOT_URL + "/api/billing/cardTypeMaster";
				var param = {orgnisationId:$scope.organizationId,unitId:$scope.unitId};
				BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						$scope.cardTypeList = response.data.listObject;
					}
				});
			}
			
			$scope.initPaymentDetailsObj = function(){
				$scope.paymentDetails = angular.copy({paymentModeId:1,cashAmount:"",bankId:"",chequeNumber:"",chequeDate:"",chequeAmount:"",credCardHolderName:"",credCardTypeId:"",credAmount:"",credCardNumber:"",credExpiryDate:"",debCardHolderName:"",debCardTypeId:"",debAmount:"",debCardNumber:"",debExpiryDate:""});
			}
	   
			
			$scope.patientSearchObj = {
				      "uhIdNumber"      : "",
				      "patientName"     : "",
				      "genderId"        : "",
				      "mobileNo"        : "",
				      "birthDate"       : "",
				      "identificationNo":"",
				      "patientType"   :"1",
				      "unitId"          :$scope.unitId,
				      "organizationId"  :$scope.organizationId
				    }
			
			$scope.searchPatient = function()
			{
				$rootScope.startLoader();
				$scope.DischargeSummaryFinalList=[];
				$scope.patientTemplateList=[];
				var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
				var patSearchObj = angular.copy($scope.patientSearchObj);
				if($scope.patientSearchObj.birthDate != "" && typeof($scope.patientSearchObj.birthDate) != "undefined"){
					var dt = new Date($scope.patientSearchObj.birthDate);
					patSearchObj.birthDate = dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + dt.getFullYear();
				} 
				BillingGenericService.serviceAction(METHOD_POST,URI, patSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					$scope.searchPatientList = [];
					if (response.data.status == "success")
						{
						$scope.patientList = response.data.listObject;
						patSearchObj = {};
						
						$scope.initPaymentDetailsObj();
						$scope.listPaymentDetails = [];
						$scope.patientDetailsList = [];
						$scope.patientDetails = {};
						$rootScope.stopLoader();
						}
					else{
						$rootScope.stopLoader();
					}
					
				});
			}
			
			//getFieldList by template id
			
			
			$scope.dischargeTemplateId=null;
			$scope.getFieldByTemplateId = function(templateId)
			{
				$rootScope.startLoader();
				$scope.dischargeTemplateId=templateId;
				var data={};
				$scope.DischargeSummaryFinalList=[];
				  var data={	
						   admissionId:$scope.admissionId,
							//dischargeTemplateId:$scope.dischargeTemplateId,
							organizationId:$scope.organizationId,
							unitId:$scope.unitId
						}
				  $scope.dischargeSummaryFieldList=[];
				  $scope.savedDischargeTemplateId="";
	               var URI = BASE_URL + ROOT_URL +"/adt/discharge/getSavedDischargeSummaryByAdmissionId";
					GenericService.serviceAction("POST",URI,data).then(function(response){
							if (response.data.status == 'success'){
								$scope.savedDischargeTemplateId=response.data.listObject[0].dischargeTemplateId;
							}
				if($scope.savedDischargeTemplateId!=""){
				if($scope.savedDischargeTemplateId!=$scope.dischargeTemplateId){
					 growl.warning(
								'Discharge Summary Generated Under '+response.data.listObject[0].dischargeTemplateName+" template",
								{
									title : 'Warning!'
								});
					    $scope.isSaveDisable=true;
						$scope.disableReleaseBtn=true;
						$scope.disablePrintBtn=true;
						$rootScope.stopLoader();
					return false;
				 }
				}
				
				$scope.dischargeFieldMasterList=[];
					//var URI = BASE_URL + ROOT_URL + "/adt/discharge/getFieldTemplateSummaryByTemplateId/3/1/1";
				var URI = BASE_URL + ROOT_URL + "/adt/discharge/getFieldTemplateSummaryByTemplateId/"+$scope.dischargeTemplateId+"/"+$scope.organizationId+"/"+$scope.unitId;
					GenericService.serviceAction("GET",URI,data).then(function(response) {
					if (response.data.status == "success")
						{
						$scope.dischargeFieldMasterList = response.data.listObject;
					    console.log("dischargeFieldMasterList",$scope.dischargeFieldMasterList);
					    
						$timeout( function(){
							$rootScope.startLoader();
							  var data={	
									   admissionId:$scope.admissionId,
										dischargeTemplateId:$scope.dischargeTemplateId,
										organizationId:$scope.organizationId,
										unitId:$scope.unitId
									}
							  $scope.dischargeSummaryFieldList=[];
				               var URI = BASE_URL + ROOT_URL +"/adt/discharge/getFieldDischargeSummaryByTemplateIdAndPatientId";
								GenericService.serviceAction("POST",URI,data).then(function(response){
										if (response.data.status == 'success'){
											$scope.dischargeSummaryFieldList = response.data.listObject;
												$scope.dischargeSummaryId=response.data.listObject[0].dischargeSummaryId;
												if(response.data.listObject[0].isDischargeSummaryRelease=='Y'){
													$scope.isSaveDisable=true;
													$scope.disableReleaseBtn=true;
													$scope.disablePrintBtn=false;
												}else{
													$scope.isSaveDisable=false;
													$scope.disableReleaseBtn=false;
													$scope.disablePrintBtn=true;
												}
										}
										else{
											$scope.isSaveDisable=false;
											$scope.disableReleaseBtn=true;
											$scope.disablePrintBtn=true;
										}
			                 console.log("dischargeSummaryMapperList",$scope.dischargeSummaryFieldList);
			                 console.log("dischargeFieldMasterList",$scope.dischargeFieldMasterList);
			                  angular.forEach($scope.dischargeFieldMasterList, function(value , key) {
			                 	 var checkFlag = false;
			                 	 if($scope.dischargeSummaryFieldList.length>0){
			                      angular.forEach($scope.dischargeSummaryFieldList, function(value1 , key1) {
			                    	  	                    	
			                     	 if(value.dischargeFieldId == value1.dischargeFieldId && value.dischargeTemplateId==value1.dischargeTemplateId)
			                     		 {
			                     		 	checkFlag = true;
			                     		 	$scope.newArr = {};
			                     		 	$scope.newArr.dischargeTemplateId = value.dischargeTemplateId;
			                     		 	$scope.newArr.dischargeFieldDescription = value1.dischargeFieldDescription;
			                     		 	$scope.newArr.dischargeFieldName = value.dischargeFieldName;
			                     		 	$scope.newArr.dischargeFieldId = value.dischargeFieldId;
			                     		 	
			                     		 }
			                     	
			                      		});
			                 	 }
			                  
														if (checkFlag == true) {
															$scope.DischargeSummaryFinalList.push($scope.newArr);
															} else {
																 
															var newArr = {};
																//$scope.newArr.patientId = value.patientId;
							                     		 	   //$scope.newArr.departmentId = value.departmentId;
								                     		 	newArr.dischargeTemplateId = value.dischargeTemplateId;
								                     		 	newArr.dischargeFieldDescription = value.dischargeFieldDescription;
								                     		 	newArr.dischargeFieldName = value.dischargeFieldName;
								                     		 	newArr.dischargeFieldId = value.dischargeFieldId;
																$scope.DischargeSummaryFinalList.push(newArr);
																
															}
														
			                     });
			                  console.log("Final discharge Summary Array",JSON.stringify($scope.DischargeSummaryFinalList));
			                  $rootScope.stopLoader();
			                         });
								
								
			         },500);
						
						}
					$rootScope.stopLoader();
					
				});
			
			});
			
			}
			

			$scope.admissionId=null;
			$scope.departmentId=null;
			//get Patient Id,department id and template id
			$scope.patientId=null;
			$scope.getPatientDetailsById = function(obj){
				$scope.isSaveDisable=true;
				$scope.disableReleaseBtn=true;
				$scope.disablePrintBtn=true;
				$scope.patientId=obj.patientId;
				$scope.patientDetailsList = [];
				$scope.patientDetails = {};
				$scope.masterObj = {};
				$scope.DischargeSummaryFinalList=[];
				$scope.patientTemplateList=[];
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + "/api/global/patient/billing/"+$scope.patientId;
				BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						$scope.patientDetailsList = response.data.listObject;
						$scope.patientDetails = response.data.listObject[0];
						console.log("PatientDetailList:",$scope.patientDetailsList);
						console.log("PatientDetail:",$scope.patientDetails);
						$rootScope.stopLoader();
					}
				});
				//var URI = BASE_URL + ROOT_URL + "/adt/discharge/getDischargeSummaryTemplateByPatientId/11/1/1";
				//get speciality id and from speciality Id get temp id discharge_summary_template_master
				var URI = BASE_URL + ROOT_URL + "/adt/discharge/getDischargeSummaryTemplateByPatientId/"+obj.admissionId+"/"+$scope.organizationId+"/"+$scope.unitId;
				BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						$scope.patientTemplateList = response.data.listObject;
						$scope.admissionId=response.data.listObject[0].admissionId;
						$scope.departmentId=response.data.listObject[0].departmentId;
						console.log("patientTemplateList:",$scope.patientTemplateList);
						$rootScope.stopLoader();
					}
				});
			
			}
			
			$scope.months = [];
			$scope.months.push({
				value : 1,
				text : 'January'
			});
			$scope.months.push({
				value : 2,
				text : 'February'
			});
			$scope.months.push({
				value : 3,
				text : 'March'
			});
			$scope.months.push({
				value : 4,
				text : 'April'
			});
			$scope.months.push({
				value : 5,
				text : 'May'
			});
			$scope.months.push({
				value : 6,
				text : 'June'
			});
			$scope.months.push({
				value : 7,
				text : 'July'
			});
			$scope.months.push({
				value : 8,
				text : 'August'
			});
			$scope.months.push({
				value : 9,
				text : 'September'
			});
			$scope.months.push({
				value : 10,
				text : 'October'
			});
			$scope.months.push({
				value : 11,
				text : 'November'
			});
			$scope.months.push({
				value : 12,
				text : 'December'
			});
			
			$scope.commonObjForAdd = {};
			$scope.addedAllergyList = [];
			$scope.dischargeSummaryIndex=[];
			//Allery List
			$scope.addAllergy = function(info,idx){
				
					if(info.allergyChk == 1){
						$scope.addedAllergyList.push(info);
						
					}else{
						
						$scope.addedAllergyList.splice(info,1);
					}
			}
			
			//History List
			$scope.personalHistoryList=[];
			$scope.familyHistoryList=[];
			$scope.surgicalHistoryList=[];
			$scope.medicationHistoryList=[];
			$scope.medicalHistoryList=[];
			$scope.travelHistoryList=[];
			$scope.immunizationHistoryList=[];
			
			$scope.addHistory=function(info,idx,historyScreenId){
				if(historyScreenId==0){
					if(info.personalHistoryCurrentChk == 1){
						$scope.personalHistoryList.push(info);
						
					}else{
						
						$scope.personalHistoryList.splice(info,1);
					}
				}
				
				if(historyScreenId==1){
					if(info.familyHistoryCurrentChk == 1){
						$scope.familyHistoryList.push(info);
						
					}else{
						
						$scope.familyHistoryList.splice(info,1);
					}
				}
				if(historyScreenId==2){
				//Surgical History
					if(info.surgicalHistoryChk == 1){
						$scope.surgicalHistoryList.push(info);
						
					}else{
						
						$scope.surgicalHistoryList.splice(info,1);
					}
				}
			//Medication History
				if(historyScreenId==3){
					if(info.MedicationHistoryChk == 1){
						$scope.medicationHistoryList.push(info);
						
					}else{
						
						$scope.medicationHistoryList.splice(info,1);
					}
				}
				
				//Medical History
				if(historyScreenId==4){
					if(info.medicalHistoryChk == 1){
						$scope.medicalHistoryList.push(info);
						
					}else{
						
						$scope.medicalHistoryList.splice(info,1);
					}
				}
				//Immunization History 
				if(historyScreenId==5){
					if(info.immunizationHistoryChk == 1){
						$scope.immunizationHistoryList.push(info);
						
					}else{
						
						$scope.immunizationHistoryList.splice(info,1);
					}
				}
				//Travel History
				if(historyScreenId==6){
					if(info.travelHistoryChk == 1){
						$scope.travelHistoryList.push(info);
						
					}else{
						
						$scope.travelHistoryList.splice(info,1);
					}
				}
				
			}
			
			//Diagnosis List
			$scope.addedDiagnosisList=[];
			$scope.addDiagnosis=function(info,idx){
				
				$scope.patientDiagnosisList[idx].diagnosisChk=info.diagnosisChk;
				
				if(info.diagnosisChk == 1){
					$scope.addedDiagnosisList.push(info);
					
				}else{
					$scope.addedDiagnosisList.splice(info,1);
				}
			}
			
			//push medication Data
			$scope.addedMedicationList=[];
			$scope.addMedication=function(obj,idx){
				$scope.detailedMedications[idx].medicationChk=obj.medicationChk;
				if(obj.medicationChk== 1){
					$scope.addedMedicationList.push(obj);
				}else{
					$scope.addedMedicationList.splice(obj,1);
				}
			}
			
			//added Consult/Referrals
			$scope.addedConsultOrdersList=[];
			$scope.addConsultReferral=function(info,idx){
				if(info.consultReferalChk== 1){
					$scope.addedConsultOrdersList.push(info);
					
				}else{
					$scope.addedConsultOrdersList.splice(info,1);
				}
			}
			//added Investigation Data
			$scope.addedInvestigationList=[];
			$scope.addInvestigation=function(info,idx){
				if(info.investigationChk== 1){
					info.orderId=info.orderId+"-"+idx;
					$scope.addedInvestigationList.push(info);
					
				}else{
					$scope.addedInvestigationList.splice(info,1);
				}
			}
			
			//add vital data
			$scope.addedVitalList=[];
			$scope.addExaminations=function(info,idx){
				if(info.vitalChk== 1){
					$scope.addedVitalList.push(info);
					
				}else{
					$scope.addedVitalList.splice(info,1);
				}
			}
			
			//Addmission reason list
			$scope.addedAdmisssionReasonList=[];
			$scope.addAdmissionReason=function(info,idx){
				if(info.admissionReasonChk== 1){
					$scope.addedAdmisssionReasonList.push(info);
					
				}else{
					$scope.addedAdmisssionReasonList.splice(info,1);
				}
			}
			
			
			//Transfer Of care
			$scope.addedTransferOfCareList=[];
			$scope.addTransferOfCare=function(info,idx){
				if(info.trasferOfCareChk== 1){
					$scope.addedTransferOfCareList.push(info);
					
				}else{
					$scope.addedTransferOfCareList.splice(info,1);
				}
			}
			//Discharge Medications
			$scope.addedDischargeMedicationlist=[];
			$scope.dischargeMedications=function(info,idx){
				if(info.dischargeMedicationChk== 1){
					$scope.addedDischargeMedicationlist.push(info);
					
				}else{
					$scope.addedDischargeMedicationlist.splice(info,1);
				}
			}
			
		//Operative Procedure
			$scope.addedOrderedProcedureList=[];
			$scope.addOperativeProcedure=function(info,idx){
				if(info.operativeChk== 1){
					$scope.addedOrderedProcedureList.push(info);
					
				}else{
					$scope.addedOrderedProcedureList.splice(info,1);
				}
			}
			
			//Surgery and Procedure
			$scope.addedSurgeryAndProcedureList=[];
			$scope.addSurgeryAndProcedure=function(info,idx){
				if(info.surgeryAndProcedureChk== 1){
					$scope.addedSurgeryAndProcedureList.push(info);
					
				}else{
					$scope.addedSurgeryAndProcedureList.splice(info,1);
				}
			}
			
			$scope.addData=function(){
				
				/*$scope.allergyCurrentList=[];
				$scope.allergyOldList=[];
				$scope.orderedProcedureList=[];
				$scope.surgeryAndProcedureList=[];
				$scope.admissionReasonList=[];
				
				$scope.personalHistoryOldList=[];
				$scope.personalHistoryCurrentList=[];
				$scope.familyHistoryOldList=[];
				$scope.familyHistoryCurrentList=[];
				$scope.surgicalHistoryOldList=[];
				$scope.surgicalHistoryCurrentList=[];
				$scope.detailedMedicationsHistory=[];
				$scope.medicalHistoryOldList=[];
				
				$scope.medicalHistoryCurrentList=[];
				$scope.immunizationHistoryCurrentList=[];
				$scope.immunizationHistoryOldList=[];
				$scope.travelHistoryOldList=[];
				$scope.travelHistoryCurrentList=[];
				
				$scope.detailedMedications=[];
				$scope.patientViatalList=[];
				$scope.patientDiagnosisList=[];
				$scope.currentEncounterOrderLists=[];
				$scope.finalCurrentEncounterOrderLists=[];*/
				//Add Allergy Data
				if($scope.fieldId==1){
					
					/*if($scope.allergyCurrentList.length==0 ||
					$scope.allergyOldList.length==0){
						 growl.warning(
									'No record found !!',
									{
										title : 'Warning!'
									});
						return false;
					} */
					
					if($scope.addedAllergyList.length > 0){
					$scope.id=$scope.commonObjForAdd.dischargeFieldId;
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
					var allergyData = "<table><tr bgcolor='#2e6ea2' color='#FFFFFF'><th width='10%'>Allergy Type</th> <th width='20%'>Allergen</th> " +
							" <th width='20%'>Severity</th> <th width='20%'>Nature of Reaction</th> <th width='10%'>Type</th>  " +
							" <th width='20%'>Remark</th> </tr>";
					angular.forEach($scope.addedAllergyList,function(value,key){
						allergyData += "<tr><td>"+value.allergyTypeName+"</td><td>"+((value.allergyId!=null)?value.allergyName:value.genericName)+"</td><td>" +
								""+value.severityDesc+"</td><td>"+value.natureOfReaction+"</td><td>"+value.type+"</td><td>"+value.remark+"</td></tr> ";
					});
					allergyData +="</table>";
					//var allergyDataNew=allergyData.replace("undefined "," ").replace("null"," ");
					var allergyDataNew=allergyData.replace(/null/g," ");
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription=allergyDataNew;
					}
					$('#closeCommonDischargeSummaryModal').trigger('click');
				}
				
				
				//Add Operative Procedure
				if($scope.fieldId==4){
					if($scope.addedOrderedProcedureList.length > 0){
					$scope.id=$scope.commonObjForAdd.dischargeFieldId;
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
					var operativeProcedureData = "<table><tr bgcolor='#2e6ea2' color='#FFFFFF'><th width='10%'>Speciality</th> <th width='40%'>Procedure Name</th> " +
							"<th width='20%'>Preferred Date Time</th> <th width='10%'>Is ICU Required</th> <th width='10%'>Is Blood Component Required</th>" +
							" <th width='10%'>Is Pack Required</th> </tr>";
					angular.forEach($scope.addedOrderedProcedureList,function(value,key){
						operativeProcedureData +="<tr><td>"+ value.specialityName+"</td><td> "+value.procedureName+"</td><td>"+value.preferedDateTime+"</td><td> " +
								" "+(value.isIcuRequired=='Y' ?"Yes":"No")+"</td><td>  "+(value.isBloodComponentRequired=='Y' ?"Yes":"No")+"" +
										"</td><td>  "+(value.isPacRequired=='Y' ?"Yes":"No")+"</td></tr>";
										//""+(value.isPrimarySecondary=='Y' ?"Yes":"No")+" <br>";
					});
					operativeProcedureData +="</table>";
					//var operativeProcedureDataNew=operativeProcedureData.replace("undefined "," ").replace("null"," ");
					var operativeProcedureDataNew=operativeProcedureData.replace(/null/g," ");
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription=operativeProcedureDataNew;
				}
					$('#closeCommonDischargeSummaryModal').trigger('click');
					/*else{
					 growl.warning(
								'Select atleast one record',
								{
									title : 'Warning!'
								});
				}*/
				
			}
				
				//add surgery and procedure 
				if($scope.fieldId==6){
					if($scope.addedSurgeryAndProcedureList.length > 0){
					$scope.id=$scope.commonObjForAdd.dischargeFieldId;
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
					var surgeryProcedureData = "<table><tr bgcolor='#2e6ea2' color='#FFFFFF'><th width='40%'>Procedure Name</th> <th width='25%'>Procedure Preferred Time</th> <th width='35%'>Reason for request</th> </tr> " ;
					
					angular.forEach($scope.addedSurgeryAndProcedureList,function(value,key){ 
						surgeryProcedureData += "<tr><td>"+value.procedureName+" </td><td> "+value.preferedDateTime+" </td><td> "+value.requestReason+" </td><tr>";
					});
					surgeryProcedureData +="</table>";
					//var surgeryProcedureDataNew=surgeryProcedureData.replace("undefined "," ").replace("null"," ");
					 var surgeryProcedureDataNew=surgeryProcedureData.replace(/null/g," ");
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription=surgeryProcedureDataNew;
				}	
					$('#closeCommonDischargeSummaryModal').trigger('click');
				}
				
				//Add History Data
				if($scope.fieldId==2){
					
					/*if($scope.personalHistoryList.length==0 || $scope.personalHistoryList.length==0 || $scope.surgicalHistoryList.length==0 ||
							$scope.medicationHistoryList.length==0 || $scope.medicalHistoryList.length==0 ||  
							$scope.immunizationHistoryList.length==0 || $scope.travelHistoryList){
						 growl.warning(
									'Select atleast one record',
									{
										title : 'Warning!'
									});
						 return false;
					}*/
					$scope.id=$scope.commonObjForAdd.dischargeFieldId;
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
					var familyHistoryData = "<table><tr bgcolor='#2e6ea2' color='#FFFFFF'><th width='25%'>ICD 10</th> <th width='25%'>ICD Code</th>" +
							"  <th width='10%'>Relation</th> <th width='11%'>Date</th> <th width='30%'>Remark</th> </tr>";
					
					angular.forEach($scope.familyHistoryList,function(value,key){
						if(value.isNoFamilyHistoryStatus=='N'){
						familyHistoryData += "<tr><td>"+value.diagnosisName+"</td><td>"+value.diagnosisCode+"</td><td>"+value.desc+"" +
								"</td><td>"+value.createdDate+"</td><td>"+value.remark+"</td></tr>";
						}else{
							familyHistoryData += "<tr><td>No Significant Family History</td><td></td><td>" +
							"</td><td>"+value.createdDate+"</td><td>"+value.remark+"</td></tr>";
						}
					});
					familyHistoryData +="</table>";
					var familyHistoryDataNew=familyHistoryData.replace(/null/g," ");
					//$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription=familyHistoryDataNew;
					
					//for personal history Data
					var personalHistoryData =  "<table><tr bgcolor='#2e6ea2' color='#FFFFFF'><th width='50%'>Description</th> <th width='50%'>Remark</th> </tr>";
					angular.forEach($scope.personalHistoryList,function(value,key){
						personalHistoryData +="<tr><td>"+ value.description+"</td><td> "+value.remark+" </td></tr>";
					});
					personalHistoryData +="</table>";
					var personalHistoryDataNew=personalHistoryData.replace(/null/g," ");
					
					//for surgical data
					var surgicalHistoryData =  "<table><tr bgcolor='#2e6ea2' color='#FFFFFF'><th width='20%'>Surgery</th> " +
							"<th width='20%'>Surgeon Name</th> <th width='10%'>Month</th> <th width='10%'>Year</th> " +
					"<th width='40%'>Remark</th> </tr>";
					angular.forEach($scope.surgicalHistoryList,function(value,key){
						if(value.isNoSignificantStatus=='N'){
							surgicalHistoryData += "<tr><td>"+ ((value.surgeryNameFormulary != null) ? value.surgeryNameFormulary : value.surgeryName)+"</td><td> "+value.surgenName+"</td><td>"+((value.month!=null)?$scope.months[value.month].text:"")+"</td><td>"+value.year+"</td><td>" +
											""+value.remark+"</td></tr>";
						}else{
						surgicalHistoryData += "<tr><td>No Significant Surgical History </td><td></td> <td></td> <td></td> <td></td></tr>";
						}
					});
					surgicalHistoryData +="</table>";
					var surgicalHistoryDataNew=surgicalHistoryData.replace(/null/g," ");
					//$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription=surgicalHistoryDataNew;
					
					//for medication history
					
					var medicationHistoryData =  "<table><tr bgcolor='#2e6ea2' color='#FFFFFF'><th width='10%'>Brand Name</th><th width='18%'>Generic Name</th> <th width='10%'>ROA</th><th width='7%'>Dose</th><th width='12%'>Frequency</th>" +
					" <th width='12%'>Start Date </th><th width='12%'>End Date</th> <th width='5%'>Quantity</th> <th width='14%'>Instruction</th></tr>";
					angular.forEach($scope.medicationHistoryList,function(value,key){
						medicationHistoryData += "<tr><td>"+value.itemName+"</td><td> "+value.genericName+" </td><td> "+value.roaDesc+" </td><td> "+value.doseId+" "+value.doseUnit+"</td><td>" +
								"  "+value.frequencyDesc+"</td><td >"+value.startDate+"</td><td>"+value.endDate+"</td><td>"+value.orderQty+"</td><td>  "+value.instructionDesc+"</td></tr>";
					});
					medicationHistoryData +="</table>";
					var medicationHistoryDataNew=medicationHistoryData.replace(/null/g," ");
					//$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription=medicationHistoryDataNew;
					
					
					//Medical History
					var medicalHistoryData =  "<table><tr bgcolor='#2e6ea2' color='#FFFFFF'><th width='30%'>ICD 10 Description</th> <th width='20%'>ICD Code</th> " +
							"<th width='20%'>Since</th> <th width='30%'>Remark</th></tr>";
					angular.forEach($scope.medicalHistoryList,function(value,key){
						if(value.isNoSignificantStatus=='N'){
						medicalHistoryData += "<tr><td>"+value.diagnosisName+"</td><td> "+value.code+"</td><td>"+value.since+"</td><td>"+value.remark+"</td></tr>";
						}else{
							medicalHistoryData += "<tr><td>No Significant Medical History</td><td> "+value.code+"</td><td>"+value.since+"</td><td>"+value.remark+"</td></tr>";
						}
					});
					medicalHistoryData +="</table>";
					var medicalHistoryDataNew=medicalHistoryData.replace(/null/g," ");
					//$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription=medicalHistoryDataNew;
					
					//immunization history list
					var immunizationHistoryData =  "<table><tr bgcolor='#2e6ea2' color='#FFFFFF'><th width='50%'>Drug Name</th> <th width='20%'>Is Administered</th> <th width='30%'>Date</th></tr>";
					angular.forEach($scope.immunizationHistoryList,function(value,key){
						immunizationHistoryData += "<tr><td>"+value.drugName+"</td><td>"+value.isAdministeredStatus+"</td><td>"+value.immunizationDate+"</td></tr>";
					});
					immunizationHistoryData +="</table>";
					var immunizationHistoryDataNew=immunizationHistoryData.replace(/null/g," ");
					//$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription=immunizationHistoryDataNew;
					
					//Travel List
					var travelHistoryData =  "<table><tr bgcolor='#2e6ea2' color='#FFFFFF'><th width='80%'>Remark</th> <th width='20%'>When</th></tr>";
					angular.forEach($scope.travelHistoryList,function(value,key){
						travelHistoryData += "<tr><td>"+value.description+"</td><td>"+value.whenTravel+"</td></tr>";
					});
					travelHistoryData +="</table>";
					var travelHistoryDataNew=travelHistoryData.replace(/null/g," ");
					
					var finalData="";
					if($scope.personalHistoryList.length > 0){
						finalData+="<B>Personal History</B>"+personalHistoryDataNew;
					}
					if($scope.familyHistoryList.length > 0){
						finalData+="<B>Family History</B>"+familyHistoryDataNew;
					}
					if($scope.surgicalHistoryList.length > 0){
						finalData=finalData+"<B>Surgical History</B>"+surgicalHistoryDataNew;
					}
					if($scope.medicationHistoryList.length > 0){
						finalData=finalData+"<B>Medication History</B>"+medicationHistoryDataNew;
					}
					if($scope.medicalHistoryList.length > 0){
						finalData=finalData+"<B>Medical History</B>"+medicalHistoryDataNew;
					}
					if($scope.immunizationHistoryList.length > 0){
						finalData=finalData+"<B>Immunization History</B>"+immunizationHistoryDataNew;
					}
					if($scope.travelHistoryList.length > 0){
						finalData=finalData+"<B>Travel History</B>"+travelHistoryDataNew;
					}
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription=finalData;
					$('#closeCommonDischargeSummaryModal').trigger('click');		
				}
				
				//add consult/Referral
				if($scope.fieldId==7){
					if($scope.addedConsultOrdersList.length>0){
					$scope.id=$scope.commonObjForAdd.dischargeFieldId;
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
					var consultData = "<table><tr bgcolor='#2e6ea2' color='#FFFFFF'> <th width='25%'>Referred To</th> <th width='25%'>Referred By</th> " +
					"<th width='20%'>Department Name</th> <th width='30%'>Note</th></tr>";
			
						angular.forEach($scope.addedConsultOrdersList,function(value,key){
							consultData +="<tr><td>"+value.refToDoctor+" </td><td> "+value.refByDoctor+" </td><td> "+value.specialityName+"</td><td> "+value.notes+" </td></tr>";
						});
					consultData+="</table>";
					var consultDataNew=consultData.replace(/null/g," ");
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription=consultDataNew;
					}
					$('#closeCommonDischargeSummaryModal').trigger('click');
				}
				
				//add  discharge Medications
				if($scope.fieldId==12){
					if($scope.addedDischargeMedicationlist.length > 0){
					$scope.id=$scope.commonObjForAdd.dischargeFieldId;
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
					var dischargeMedicationData = "<table><tr bgcolor='#2e6ea2' color='#FFFFFF'><th width='10%'>Brand Name</th><th width='20%'>Generic Name</th> <th width='10%'>ROA</th><th width='7%'>Dose</th><th width='15%'>Frequency</th>" +
					" <th width='11%'>Start Date </th><th width='11%'>End Date</th> <th width='7%'>Quantity</th> <th style='width:14%;'>Instruction</th></tr>";
					angular.forEach($scope.addedDischargeMedicationlist,function(value,key){
						dischargeMedicationData += "<tr><td>"+value.itemName+"</td><td>"+value.genericName+"</td><td>"+value.roa_details+"</td><td>"+value.doseDesc +' '+ value.strengthDesc+"" +
						"</td><td>"+value.frequencyDesc+"</td><td>"+value.startDate+"</td><td>"+value.endDate+"</td><td>" +
								""+value.quantity+"</td><td>"+value.instructionDesc+"</td></tr>";
					});
					dischargeMedicationData+="</table>";
					var dischargeMedicationDataNew=dischargeMedicationData.replace(/null/g," ");
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription=dischargeMedicationDataNew;
					}
					$('#closeCommonDischargeSummaryModal').trigger('click');
				}
				
				//add diagnosis data
				if($scope.fieldId==8){
					if($scope.addedDiagnosisList.length>0){
					$scope.id=$scope.commonObjForAdd.dischargeFieldId;
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
					var diagnosisData = "<table><tr bgcolor='#2e6ea2' color='#FFFFFF'><th width='20%'>ICD Code</th> <th width='30%'>Description</th> " +
							" <th width='20%'>Diagnosis Type</th>  <th width='20%'>Disease Type</th> <th width='20%'>Remark</th> </tr>";
					angular.forEach($scope.addedDiagnosisList,function(value,key){
						diagnosisData += "<tr><td>"+value.code+"</td><td>"+value.diagnosisName+" </td><td> "+(value.type=='1' ?"Provisional":"Final")+"</td><td> "+(value.isNotifiable=='Y' ?"Notifiable":" ")+(value.isComorbidity == 'Y' ?"Comorbidity":" ")+"</td><td> "+value.remark+" </td></tr>";
					});
					diagnosisData+="</table>";
					var diagnosisDataNew=diagnosisData.replace(/null/g," ");
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription=diagnosisDataNew;
					}
					$('#closeCommonDischargeSummaryModal').trigger('click');
				}
				
				//Push Medication Data
				if($scope.fieldId==5){
					if($scope.addedMedicationList.length > 0){
					$scope.id=$scope.commonObjForAdd.dischargeFieldId;
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
					var medicationData = "<table><tr bgcolor='#2e6ea2' color='#FFFFFF'><th width='10%'>Brand Name</th><th width='20%'>Generic Name</th> <th width='10%'>ROA</th><th width='5%'>Dose</th><th width='15%'>Frequency</th>" +
							"<th width='13%'>Start Date </th><th width='13%'>End Date</th><th style='width:14%;'>Instruction</th></tr>";
					angular.forEach($scope.addedMedicationList,function(value,key){
						medicationData += "<tr><td>"+value.itemName+"</td><td>"+value.genericName+"</td><td>"+value.roa_details+"</td><td>"+value.doseDesc +' '+ value.strengthDesc+"" +
								"</td><td>"+value.frequencyDesc+"</td><td>"+value.startDate+"</td><td>"+value.endDate+"</td><td>"+value.instructionDesc+"</td></tr>";
					});
					medicationData+="</table>";
					var medicationDataNew=medicationData.replace(/null/g," ");
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription=medicationDataNew;
					}
					$('#closeCommonDischargeSummaryModal').trigger('click');
				}
				
				
				//Get addmission reason list
				if($scope.fieldId==9){
					if($scope.addedAdmisssionReasonList.length > 0){
					$scope.id=$scope.commonObjForAdd.dischargeFieldId;
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
					var addmisionData = "<table><tr bgcolor='#2e6ea2' color='#FFFFFF'><th>Admission Reason</th></tr>";
					angular.forEach($scope.addedAdmisssionReasonList,function(value,key){
						addmisionData += "<tr><td>"+value.admissionReason+" </td></tr>";
					});
					addmisionData+="</table>";
					var addmisionDataNew=addmisionData.replace(/null/g," ");
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription=addmisionDataNew;
					}
					$('#closeCommonDischargeSummaryModal').trigger('click');
				}
				
				//Investigation Data
				if($scope.fieldId==10){
					if($scope.addedInvestigationList.length>0){
					$scope.id=$scope.commonObjForAdd.dischargeFieldId;
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
					
					var ordDetIds="";
					angular.forEach($scope.addedInvestigationList,function(value,key){
						if(key==(($scope.addedInvestigationList.length)-1)){
							ordDetIds+=value.orderDetailsId;
						}else{
							 ordDetIds+=value.orderDetailsId+",";
						}
					});
					
					var investigationData ="<table  ord-det-ids='"+ordDetIds+"'><tr bgcolor='#2e6ea2' color='#FFFFFF'><th width='20%'>Created Date</th><th width='60%'>Investigation</th> <th width='20%'>Status</th></tr>";
					angular.forEach($scope.addedInvestigationList,function(value,key){
						investigationData += "<tr><td>"+value.createdDate+"</td><td>"+value.serviceStandardName+"</td><td>"+((value.sampleStatusId==8 )? "Completed":value.statusName)+"</td></tr>";
					});
					investigationData+="</table>";
					var investigationDataNew=investigationData.replace(/null/g," ");
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription=investigationDataNew;
					}
					$('#closeCommonDischargeSummaryModal').trigger('click');
				}
				
				//Transfer of care
				if($scope.fieldId==11){
					if($scope.addedTransferOfCareList.length > 0){
					$scope.id=$scope.commonObjForAdd.dischargeFieldId;
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
					var transferOfCareData = "<table><tr bgcolor='#2e6ea2' color='#FFFFFF'><th>Discharge Note</th></tr>";
					angular.forEach($scope.addedTransferOfCareList,function(value,key){
						transferOfCareData += "<tr><td>"+value.dischargeSummaryNote+" </td></tr>";
					});
					transferOfCareData+="</table>";
					var transferOfCareDataNew=transferOfCareData.replace(/null/g," ");
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription=transferOfCareDataNew;
					}
					$('#closeCommonDischargeSummaryModal').trigger('click');
				}
				//VITAL DATA
				if($scope.fieldId==3){
					if($scope.addedVitalList.length > 0){
					$scope.id=$scope.commonObjForAdd.dischargeFieldId;
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
					//var vitalData="";
					var vitalData = "<table ><tr bgcolor='#2e6ea2' color='#FFFFFF'><th width='15%' >Created Date</th><th width='40%' >Vital Name</th> <th width='15%' >Value</th><th width='30%'>Recorded By</th></tr>";
					angular.forEach($scope.addedVitalList,function(value1,key){
						vitalData += "<tr><td style='width:10%;'>"+value1.createdDate+"</td><td style='width:30%;'> "+value1.vitalName+"</td><td style='width:30%;'> "+value1.value+" "+value1.unitDesc+"</td><td style='width:30%;'>"+(value1.doctorName!=null?value1.doctorName:value1.employeeName)+"</td></tr>";
						//vitalData += value1.createdDate+" - "+value1.vitalName+" - "+value1.value+" - "+value1.unitDesc+" - "+(value1.doctorName!=null?value1.doctorName:value1.employeeName)+"<br>";
					});
					vitalData +="</table>";
					var vitalDataNew=vitalData.replace(/null/g," ");
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription=vitalDataNew;
					}
					$('#closeCommonDischargeSummaryModal').trigger('click');
				}
				
			}
				
			
			
			$scope.modalHeader='';
			$scope.fieldId='';
			
		$scope.showCoversheetData=function(obj,index){
			$scope.modalHeader=obj.dischargeFieldName;
			$scope.dischargeSummaryIndex=index;
			$scope.fieldId=obj.dischargeFieldId;	
			if($scope.fieldId==1 || $scope.fieldId==2 || $scope.fieldId==3 || $scope.fieldId==4 || $scope.fieldId==5 || $scope.fieldId==6 || $scope.fieldId==7 ||
					$scope.fieldId==8 || $scope.fieldId==9 ||  $scope.fieldId==10 ||  $scope.fieldId==11 || $scope.fieldId==12 ){
				$('#commonDischargeSummaryModal').modal();
			}else{
				return false;
			}
			$scope.commonObjForAdd=obj;
			$scope.loadingMessage="Loading....";
			$scope.allergyCurrentList=[];
			$scope.patientViatalList=[];
			$scope.orderedProcedureList=[];
			$scope.detailedMedications=[];
			$scope.surgeryAndProcedureList=[];
			$scope.consultOrdersList=[];
			$scope.patientDiagnosisList=[];
			$scope.admissionReasonList=[];
			$scope.currentEncounterOrderLists=[];
			$scope.finalCurrentEncounterOrderLists=[];
			$scope.allTransferOfcareNoteslist=[];
			$scope.DischargeMedicationlist=[];
			//Get Coversheet Data <--------Allergy Data------------->
			var headerObj = {
					unit_id : $scope.unitId,
					organization_id : $scope.organizationId
				};

			var paramObjForPer = {
					patientId : $scope.patientId,
					admissionId :$scope.admissionId,
					roleId :2
				};
			
			//for old patient coversheet data
			$scope.dt = moment(new Date()).format('DD-MM-YYYY');

				var paramObjForHistList = {
					patientId : $scope.patientId,
					created_date:$scope.dt
				};
				
			if($scope.fieldId==1){
			var URI = BASE_URL + ROOT_URL+"/api/opd/getCurrentAllergyPatientMapperForCoversheet";
			GenericServiceParamHeader.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE,paramObjForHistList, headerObj).then(function(response) {
						if (response.data.status == "success")
							$scope.allergyCurrentList = response.data.listObject;
						console.log("allergyCurrentList",$scope.allergyCurrentList);
						/*if($scope.allergyCurrentList.length==0)
							$scope.loadingMessage="No record found";*/
						
						angular.forEach($scope.allergyCurrentList,function(value,key) {
							var flag = false;
							angular.forEach($scope.addedAllergyList,function(value1,key1) {
								if(value1.allergyId == value.allergyId ){
										flag = true;
								}
							});
						if(flag== true){
								value.allergyChk=1;
						}else{
								value.allergyChk=0;
						}
							
			         });
				
					});
			
			
				
			var data = {};
			var URI = BASE_URL + ROOT_URL
					+ GETOLDALLERGYPATIENTMAPPER;
			GenericServiceParamHeader
					.serviceAction(METHOD_GET, URI, "",
							NOTIFICATION_MSG_STATUS_FALSE,
							paramObjForHistList, headerObj)
					.then(
							function(response) {
								if (response.data.status == "success")
									$scope.allergyOldList = response.data.listObject;
								   angular.forEach($scope.allergyOldList,function(value,key) {
									var flag = false;
									angular.forEach($scope.addedAllergyList,function(value1,key1) {
										if(value1.allergyId == value.allergyId ){
												flag = true;
										}
									});
								if(flag== true){
										value.allergyChk=1;
								}else{
										value.allergyChk=0;
								}
									
					         });
							});
			}

			//Get operative details
		if($scope.fieldId==4){
			var d = {};
			$scope.orderedProcedureList = [];

			var URI = BASE_URL
					+ ROOT_URL
					+ "/api/procedure/getProcedureOrderListByAdmissionId/"
					+ $scope.admissionId+"/0/0";

			GenericService
					.serviceAction("GET", URI, d)
					.then(
							function(response) {
								$scope.templateListSickList = [];

								if (response.data.status == "success") {
									$scope.dPrdProcedureList = response.data.listObject;
									$scope.orderedProcedureList = response.data.listObject;
									if($scope.orderedProcedureList.length==0)
										$scope.loadingMessage="No record found";
									console.log("$scope.orderedProcedureList",$scope.orderedProcedureList);
									
									angular.forEach($scope.orderedProcedureList,function(value,key) {
										var flag = false;
										angular.forEach($scope.addedOrderedProcedureList,function(value1,key1) {
											if(value1.procedureOrderId == value.procedureOrderId ){
													flag = true;
											}
										});
									if(flag== true){
											value.operativeChk=1;
									}else{
											value.operativeChk=0;
									}
										
						         });
								}
							});
			}
			
			//get surgeries and procedure performed
         if($scope.fieldId==6){
        	 var URI = BASE_URL + ROOT_URL
				+ "/api/procedure/getProcedureOrderListForDischargeSummary/"+$scope.admissionId+"/"+$scope.organizationId+"/"+$scope.unitId;
		GenericService
				.serviceAction(
						"GET",URI,{})
				.then(
						function(response) {
							// $rootScope.startLoader();
							if (response.data.status == "success")
								$scope.surgeryAndProcedureList = response.data.listObject;
								if($scope.surgeryAndProcedureList.length==0)
									$scope.loadingMessage="No record found";
							 	angular.forEach($scope.surgeryAndProcedureList,function(value,key) {
									var flag = false;
									angular.forEach($scope.addedSurgeryAndProcedureList,function(value1,key1) {
										if(value1.proReqId == value.proReqId ){
												flag = true;
										}
									});
								if(flag== true){
										value.surgeryAndProcedureChk=1;
								}else{
										value.surgeryAndProcedureChk=0;
								}
									
					   });
						});
			}
      
       //Get Admission Reason
			if($scope.fieldId==9){
				
				var dataObjForReason={
						admissionId : $scope.admissionId,
						organizationId : $scope.organizationId,
							unitId : $scope.unitId
				};
				
				var URI = BASE_URL + ROOT_URL+ "/adt/discharge/getAdmissionReason";
				GenericService.serviceAction("POST",URI, dataObjForReason).then(function(response) {
							if (response.data.status == "success")
								$scope.admissionReasonList = response.data.listObject;
							if($scope.admissionReasonList.length==0)
								$scope.loadingMessage="No record found";
							console.log("admissionReasonList",$scope.admissionReasonList);
							angular.forEach($scope.admissionReasonList,function(value,key) {
								var flag = false;
								angular.forEach($scope.addedAdmisssionReasonList,function(value1,key1) {
									if(value1.reasonId == value.reasonId ){
											flag = true;
									}
								});
							if(flag== true){
									value.admissionReasonChk=1;
							}else{
									value.admissionReasonChk=0;
							}
								
				});
					
						});
				}
			// <---------------HistoryList--------------->
	

								// For Get Active History Personal
			if($scope.fieldId==2){
				
								var URI = BASE_URL
										+ ROOT_URL
										+ GETOLDPATIENTPERSONALHISTORYDETAILS;
								GenericServiceParamHeader
										.serviceAction(
												METHOD_GET,
												URI,
												"",
												NOTIFICATION_MSG_STATUS_FALSE,
												paramObjForHistList, headerObj)
										.then(
												function(response) {
													if (response.data.status == "success")
														$scope.personalHistoryOldList = response.data.listObject;
													 	angular.forEach($scope.personalHistoryOldList,function(value,key) {
															var flag = false;
															angular.forEach($scope.personalHistoryList,function(value1,key1) {
																if(value1.personalHistoryDetailsId == value.personalHistoryDetailsId ){
																		flag = true;
																}
															});
														if(flag== true){
																value.personalHistoryCurrentChk=1;
														}else{
																value.personalHistoryCurrentChk=0;
														}
															
											   });
												});

								var URI = BASE_URL
										+ ROOT_URL
										+  "/api/opd/getCurrentPatientPersonalHistoryDetailsForCoversheet";
								GenericServiceParamHeader
										.serviceAction(
												METHOD_GET,
												URI,
												"",
												NOTIFICATION_MSG_STATUS_FALSE,
												paramObjForHistList, headerObj)
										.then(
												function(response) {
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.personalHistoryCurrentList = response.data.listObject;
													 	angular.forEach($scope.personalHistoryCurrentList,function(value,key) {
															var flag = false;
															angular.forEach($scope.personalHistoryList,function(value1,key1) {
																if(value1.personalHistoryDetailsId == value.personalHistoryDetailsId ){
																		flag = true;
																}
															});
														if(flag== true){
																value.personalHistoryCurrentChk=1;
														}else{
																value.personalHistoryCurrentChk=0;
														}
															
											   });
												});
								// END Get Personal History

								var URI = BASE_URL + ROOT_URL
										+ GETOLDPATIENTFAMILYHISTORY;
								GenericServiceParamHeader
										.serviceAction(
												METHOD_GET,
												URI,
												"",
												NOTIFICATION_MSG_STATUS_FALSE,
												paramObjForHistList, headerObj)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.familyHistoryOldList = response.data.listObject;
													 	console.log("familyHistoryOldList",$scope.familyHistoryOldList);

													 	angular.forEach($scope.familyHistoryOldList,function(value,key) {
															var flag = false;
															angular.forEach($scope.familyHistoryList,function(value1,key1) {
																if(value1.familyHistoryId == value.familyHistoryId ){
																		flag = true;
																}
															});
														if(flag== true){
																value.familyHistoryCurrentChk=1;
														}else{
																value.familyHistoryCurrentChk=0;
														}
															
											   });
													 	
												});

								var URI = BASE_URL + ROOT_URL
										+ "/api/opd/getCurrentPatientFamilyHistoryForCoversheet";;
								GenericServiceParamHeader
										.serviceAction(
												METHOD_GET,
												URI,
												"",
												NOTIFICATION_MSG_STATUS_FALSE,
												paramObjForHistList, headerObj)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.familyHistoryCurrentList = response.data.listObject;
													 	console.log("familyHistoryCurrentList",$scope.familyHistoryCurrentList);
													 	
													 	angular.forEach($scope.familyHistoryCurrentList,function(value,key) {
															var flag = false;
															angular.forEach($scope.familyHistoryList,function(value1,key1) {
																if(value1.familyHistoryId == value.familyHistoryId ){
																		flag = true;
																}
															});
														if(flag== true){
																value.familyHistoryCurrentChk=1;
														}else{
																value.familyHistoryCurrentChk=0;
														}
															
											   });
												
												});
								

								//Surgical  History Data
								var URI = BASE_URL + ROOT_URL
										+ GETOLDPATIENTSURGICALHISTORY;
								GenericServiceParamHeader
										.serviceAction(
												METHOD_GET,
												URI,
												"",
												NOTIFICATION_MSG_STATUS_FALSE,
												paramObjForHistList, headerObj)
										.then(
												function(response) {
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.surgicalHistoryOldList = response.data.listObject;
														console.log("surgicalHistoryOldList",$scope.surgicalHistoryOldList);
														
														angular.forEach($scope.surgicalHistoryOldList,function(value,key) {
															var flag = false;
															angular.forEach($scope.surgicalHistoryList,function(value1,key1) {
																if(value1.surgeryId == value.surgeryId ){
																		flag = true;
																}
															});
														if(flag== true){
																value.surgicalHistoryChk=1;
														}else{
																value.surgicalHistoryChk=0;
														}
															
											   });
												});

								var URI = BASE_URL + ROOT_URL
										+ "/api/opd/getCurrentPatientSurgicalHistoryForCoversheet";;
								GenericServiceParamHeader
										.serviceAction(
												METHOD_GET,
												URI,
												"",
												NOTIFICATION_MSG_STATUS_FALSE,
												paramObjForHistList, headerObj)
										.then(
												function(response) {
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.surgicalHistoryCurrentList = response.data.listObject;
														console.log("surgicalHistoryCurrentList",$scope.surgicalHistoryCurrentList);
														
													 	angular.forEach($scope.surgicalHistoryCurrentList,function(value,key) {
															var flag = false;
															angular.forEach($scope.surgicalHistoryList,function(value1,key1) {
																if(value1.surgeryId == value.surgeryId ){
																		flag = true;
																}
															});
														if(flag== true){
																value.surgicalHistoryChk=1;
														}else{
																value.surgicalHistoryChk=0;
														}
															
											   });
													 	
												});
								
								//Medication History Data
								var patientId = $scope.patientId;
								var dateObj = moment(new Date()).format(
										'YYYY-MM-DD');
								var dataForMedicationObj = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									patientId : $scope.patientId,
									admissionId : $scope.admissionId

								};
								var URI = BASE_URL + ROOT_URL
										+ "/api/opd/getMedicationHistoryList";
								GenericServiceParamHeader
										.serviceAction(METHOD_POST, URI, dataForMedicationObj,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.detailedMedicationsHistory = response.data.listObject; 
														console.log("$scope.detailedMedicationsHistoryyy",$scope.detailedMedicationsHistory);
														
														angular.forEach($scope.detailedMedicationsHistory,function(value,key) {
															var flag = false;
															angular.forEach($scope.medicationHistoryList,function(value1,key1) {
																if(value1.itemId == value.itemId ){
																		flag = true;
																}
															});
														if(flag== true){
																value.MedicationHistoryChk=1;
														}else{
																value.MedicationHistoryChk=0;
														}
											   });
													
													}
												});

								//Medical History Data

								var URI = BASE_URL
										+ ROOT_URL
										+ GETOLDPATIENTMEDICALHISTORYDETAILS;
								GenericServiceParamHeader
										.serviceAction(
												METHOD_GET,
												URI,
												"",
												NOTIFICATION_MSG_STATUS_FALSE,
												paramObjForHistList, headerObj)
										.then(
												function(response) {
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.medicalHistoryOldList = response.data.listObject;
														console.log("medicalHistoryOldList",$scope.medicalHistoryOldList);
														
														angular.forEach($scope.medicalHistoryOldList,function(value,key) {
															var flag = false;
															angular.forEach($scope.medicalHistoryList,function(value1,key1) {
																if(value1.patientMedicalHistoryId == value.patientMedicalHistoryId ){
																		flag = true;
																}
															});
														if(flag== true){
																value.medicalHistoryChk=1;
														}else{
																value.medicalHistoryChk=0;
														}
															
											     });
														
												});

								var URI = BASE_URL
										+ ROOT_URL
										+ "/api/opd/getCurrentPatientMedicalHistoryDetailsForCoversheet";
								GenericServiceParamHeader
										.serviceAction(
												METHOD_GET,
												URI,
												"",
												NOTIFICATION_MSG_STATUS_FALSE,
												paramObjForHistList, headerObj)
										.then(
												function(response) {
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.medicalHistoryCurrentList = response.data.listObject;
														console.log("medicalHistoryCurrentList",$scope.medicalHistoryCurrentList);
														
														angular.forEach($scope.medicalHistoryCurrentList,function(value,key) {
															var flag = false;
															angular.forEach($scope.medicalHistoryList,function(value1,key1) {
																if(value1.patientMedicalHistoryId == value.patientMedicalHistoryId ){
																		flag = true;
																}
															});
														if(flag== true){
																value.medicalHistoryChk=1;
														}else{
																value.medicalHistoryChk=0;
														}
															
											   });
														
												});
								
								//Immunization History
								var URI = BASE_URL
								+ ROOT_URL
								+ "/api/opd/getCurrentImmunizationHistoryMapperForCoversheet";;
						GenericServiceParamHeader
								.serviceAction(
										METHOD_GET,
										URI,
										"",
										NOTIFICATION_MSG_STATUS_FALSE,
										paramObjForHistList, headerObj)
								.then(
										function(response) {
											console.log(response);
											// $rootScope.startLoader();
											if (response.data.status == "success") {
												$scope.immunizationHistoryCurrentList = response.data.listObject;
												
												angular.forEach($scope.immunizationHistoryCurrentList,function(value,key) {
													var flag = false;
													angular.forEach($scope.immunizationHistoryList,function(value1,key1) {
														if(value1.immunizationHistoryMapperId == value.immunizationHistoryMapperId ){
																flag = true;
														}
													});
												if(flag== true){
														value.immunizationHistoryChk=1;
												}else{
														value.immunizationHistoryChk=0;
												}
													
									   });
											}
											
										});
									
									var URI = BASE_URL + ROOT_URL
									+ GETOLDIMMUNIZATIONHISTORYMAPPER;
							GenericServiceParamHeader
									.serviceAction(
											METHOD_GET,
											URI,
											"",
											NOTIFICATION_MSG_STATUS_FALSE,
											paramObjForHistList, headerObj)
									.then(
											function(response) {
												console.log(response);
												// $rootScope.startLoader();
												if (response.data.status == "success")
													$scope.immunizationHistoryOldList = response.data.listObject;
												
												angular.forEach($scope.immunizationHistoryOldList,function(value,key) {
													var flag = false;
													angular.forEach($scope.immunizationHistoryList,function(value1,key1) {
														if(value1.immunizationHistoryMapperId == value.immunizationHistoryMapperId ){
																flag = true;
														}
													});
												if(flag== true){
														value.immunizationHistoryChk=1;
												}else{
														value.immunizationHistoryChk=0;
												}
													
									   });
												
											});
								
								
								//Travel History Data

								var URI = BASE_URL + ROOT_URL
										+ GETOLDPATIENTTRAVELHISTORYDETAILS;
								GenericServiceParamHeader
										.serviceAction(
												METHOD_GET,
												URI,
												"",
												NOTIFICATION_MSG_STATUS_FALSE,
												paramObjForHistList, headerObj)
										.then(
												function(response) {
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.travelHistoryOldList = response.data.listObject;
														console.log("travelHistoryOldList",$scope.travelHistoryOldList);
														
														angular.forEach($scope.travelHistoryOldList,function(value,key) {
															var flag = false;
															angular.forEach($scope.travelHistoryList,function(value1,key1) {
																if(value1.travelHistoryDetailsId == value.travelHistoryDetailsId ){
																		flag = true;
																}
															});
														if(flag== true){
																value.travelHistoryChk=1;
														}else{
																value.travelHistoryChk=0;
														}
															
											        });
												
												});

								var URI = BASE_URL
										+ ROOT_URL
										+"/api/opd/getCurrentPatientTravelHistoryDetailsForCoversheet";;
								GenericServiceParamHeader
										.serviceAction(
												METHOD_GET,
												URI,
												"",
												NOTIFICATION_MSG_STATUS_FALSE,
												paramObjForHistList, headerObj)
										.then(
												function(response) {
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.travelHistoryCurrentList = response.data.listObject;
														console.log("travelHistoryCurrentList",$scope.travelHistoryCurrentList);
														
														angular.forEach($scope.travelHistoryCurrentList,function(value,key) {
															var flag = false;
															angular.forEach($scope.travelHistoryList,function(value1,key1) {
																if(value1.travelHistoryDetailsId == value.travelHistoryDetailsId ){
																		flag = true;
																}
															});
														if(flag== true){
																value.travelHistoryChk=1;
														}else{
																value.travelHistoryChk=0;
														}
															
											   });
												});
							
			}
		
			
		//<------------Get Medication List------------>
	if($scope.fieldId==5){
		$scope.detailedMedications = [];
		var patientId = $scope.patientId;
		var dateObj = moment(new Date()).format(
				'YYYY-MM-DD');
		
		/*var URI = BASE_URL + ROOT_URL
				+ GETDETAILEDMEDICATION + "?patientId="
				+ patientId + "&date=" + dateObj;*/
		/*var URI = BASE_URL + ROOT_URL + "/api/opd/getMedicationDetailsByDoctorId" + "?patientId=" +
			patientId + "&date=" + dateObj +"&doctorId="+ $scope.doctorId;*/
		var medicationobj = {
				//patientId:$scope.patientId,
				admissionId:$scope.admissionId,
				unitId:$scope.unitId,
				organizationId:$scope.organizationId,
				dischargeMedicationFlag:false
		};

		var URI = BASE_URL + ROOT_URL
				+ "/api/opd/getDischargeMedication";
		GenericService.serviceAction("POST",URI,medicationobj).then(function(response){
							if (response.data.status == "success") {
								$scope.detailedMedications = response.data.listObject;
								if($scope.detailedMedications.length==0)
									$scope.loadingMessage="No record found";
								angular.forEach($scope.detailedMedications,function(value,key) {
									var flag = false;
									angular.forEach($scope.addedMedicationList,function(value1,key1) {
										if(value1.prescriptionId == value.prescriptionId ){
												flag = true;
										}
									});
								if(flag== true){
										value.medicationChk=1;
								}else{
										value.medicationChk=0;
								}
									
					});
								console.log("detailedMedications",$scope.detailedMedications);
							}
						});
	}
		//   <---- vital List--->
	if($scope.fieldId==3){
									var paramObjForVital = {
										patientId : $scope.patientId,
										encounterId : $scope.admissionId,
										roleId : 2,
										flag : 'Y' // for admission
									}
									var headerObj = {
										"unit_id" : $scope.unitId,
										"organization_id" : $scope.organizationId
									};
									var data = {};
									console.log("jj", paramObjForVital);
									// return false;
									var URI = BASE_URL + ROOT_URL
										//	+ GETCURRENTAPPOINTMENTVITALMAPPER;
									+ "/api/opd/getListOfVitalsByPatientIdForCoversheet";
									GenericServiceParamHeader
											.serviceAction(METHOD_GET, URI, data,
													NOTIFICATION_MSG_STATUS_FALSE,
													paramObjForVital, headerObj)
											.then(
													function(response) {
														if (response.data.status == "success")
															$scope.vitalFinalList = [];
														$scope.patientViatalList = response.data.listObject;
														if($scope.patientViatalList.length==0)
															$scope.loadingMessage="No record found";
														console.log("patientViatalList",$scope.patientViatalList);
														
														angular.forEach($scope.patientViatalList,function(value,key) {
															var flag = false;
															angular.forEach($scope.addedVitalList,function(value1,key1) {
																if(value1.vitalId == value.vitalId ){
																		flag = true;
																}
															});
														if(flag== true){
																value.vitalChk=1;
														}else{
																value.vitalChk=0;
														}
															
											});

														});
	
										// <---Physical Examination--->
									
									var paramObjForGen1 = {
										'patientId' : $scope.patientId,
										'typeId' : 2,
									// 'genderId':commonObj.genderId
									};
										var URI = BASE_URL + ROOT_URL
												+ GETGENERALEXAMLIST;
										GenericServiceParamHeader
												.serviceAction(
														METHOD_GET,
														URI,
														data,
														NOTIFICATION_MSG_STATUS_FALSE,
														paramObjForGen1, headerObj)
												.then(
														function(response) {
															if (response.data.status == "success")
																$scope.getSavedPhysicalExamList = response.data.listObject;
																console.log("getSavedPhysicalExamList",$scope.getSavedPhysicalExamList);
														});

	//  <--------------General Examination----------------->
									
									var data = {};
									var paramObjForGen = {
										'patientId' : $scope.patientId,
										'typeId' : 1,
									// 'genderId':commonObj.genderId
									};

										var URI = BASE_URL + ROOT_URL
												+ GETGENERALEXAMLIST;
										GenericServiceParamHeader
												.serviceAction(
														METHOD_GET,
														URI,
														data,
														NOTIFICATION_MSG_STATUS_FALSE,
														paramObjForGen, headerObj)
												.then(
														function(response) {
															if (response.data.status == "success")
																$scope.getSavedGeneralExamList = response.data.listObject;
																console.log("getSavedGeneralExamList",$scope.getSavedGeneralExamList);
														});
	}		
	
					//<--------------------Diagnosis List by patient id---------------->
	  if($scope.fieldId==8){
					var data = {};
								var headerObjDia = {
									'unit_id' : $scope.unitId,
									'organization_id' : $scope.organizationId,
									'patient_id' : $scope.patientId,
									'Content-Type' : 'application/json',
								};
								var URI = BASE_URL + ROOT_URL
										+ GETPATIENTDIAGNOSISBYPATIENTID;
								GenericServiceParamHeader
										.serviceAction(METHOD_GET, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE,
												data, headerObjDia)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.patientDiagnosisList = response.data.listObject;
													console.log("patientDiagnosisList",$scope.patientDiagnosisList);
													if($scope.patientDiagnosisList.length==0)
														$scope.loadingMessage="No record found";
													$scope.comorbidityFlag = false;
													$scope.notifiableFlag = false;

													if ($scope.patientDiagnosisList.length > 0) {
														
														angular.forEach($scope.patientDiagnosisList,function(value,key) {
																			if (value.isNotifiable == 'Y') {
																				$scope.notifiableFlag = true;
																			} else if (value.isComorbidity == 'Y') {
																				$scope.comorbidityFlag = true;
																			}
																			var flag = false;
																			angular.forEach($scope.addedDiagnosisList,function(value1,key1) {
																				
																				if(value1.diagnosisId == value.diagnosisId ){
																					
																						flag = true;
																				}
																			});
																		if(flag== true){
																				value.diagnosisChk=1;
																		}else{
																				value.diagnosisChk=0;
																		}
																			
															});
													}

												});
	   }					

	  //Investigation 
	  function isEmpty(obj) {
  	    for(var key in obj) {
  	        if(obj.hasOwnProperty(key))
  	            return true;
  	    }
  	    return false;
  	}
 if($scope.fieldId==10){
	  var data = {};
		var paramObj = {};
		var headers = {
			//'patientId' : $scope.patientId,
			'patientId' : $scope.admissionId,
			'flag' : false,
			'Content-Type' : 'application/json'
		};
		// var encounterId =
		// $scope.encounterDetailsObj.encounterId;
		
		var URI = BASE_URL + ROOT_URL
				+ GETCURRENTINVESTIGATION;
		GenericServiceParamHeader
				.serviceAction(
						METHOD_GET,
						URI,
						data,
						NOTIFICATION_MSG_STATUS_FALSE,
						paramObj, headers)
				.then(
						function(response) {
							if (response.data.status == "success") 
							{
								var promise={};
								$scope.currentEncounterOrderLists = response.data.listObject;
								angular.forEach(response.data.listObject,function(value,index){
										var paramObj = {};
										//$('#panelDetailsModal').modal('show');
										if(value.isPanel=='Y'){
										 promise=$scope.getPanelOrderedList(value,index);
										}
										
			          });
		
			if(isEmpty(promise)==false){

				$scope.finalCurrentEncounterOrderLists=	$scope.currentEncounterOrderLists;	
				if($scope.finalCurrentEncounterOrderLists.length==0)
					$scope.loadingMessage="No record found";
				$scope.countFlag=0;						
				angular.forEach($scope.finalCurrentEncounterOrderLists,function(value,key) {
				if(value.sampleStatusId==8){
				$scope.countFlag=1;
				}
				
				var flag = false;
				angular.forEach($scope.addedInvestigationList,function(value1,key1) {
				if(value1.orderId == (value.orderId+"-"+key) ){
				flag = true;
				}
				});
				if(flag== true){
				value.investigationChk=1;
				}else{
				value.investigationChk=0;
	          }

	       });
		
		}else{
		promise.then(function(response) {					
			$scope.finalCurrentEncounterOrderLists=	$scope.currentEncounterOrderLists;	
			if($scope.finalCurrentEncounterOrderLists.length==0)
				$scope.loadingMessage="No record found";
			$scope.countFlag=0;						
			angular.forEach($scope.finalCurrentEncounterOrderLists,function(value,key) {
			if(value.sampleStatusId==8){
			$scope.countFlag=1;
			}
			
			var flag = false;
			angular.forEach($scope.addedInvestigationList,function(value1,key1) {
			if(value1.orderId == (value.orderId+"-"+key) ){
			flag = true;
			}
			});
			if(flag== true){
			value.investigationChk=1;
			}else{
			value.investigationChk=0;
          }

       });
	});
	}
		}
		});
	  
	 }
 
 $scope.getPanelOrderedList=function(value,index){
		var headers = {
			'panelId' : value.panelId,
			'orderDetailsId' : value.orderDetailsId,
			'Content-Type' : 'application/json'
		};
		var data = {};
		var paramObj = {};
		var URI = BASE_URL + ROOT_URL
				+ GETCURRENTINVESTIGATIONDETAILS;
		 return GenericServiceParamHeader
				.serviceAction(METHOD_GET, URI, data,
						NOTIFICATION_MSG_STATUS_FALSE,
						paramObj, headers)
				.then(
						function(response1) {
							if (response1.data.status == "success") {
								$scope.inveDetails = response1.data.listObject;
								angular.forEach($scope.inveDetails,function(value1,index){
									$scope.currentEncounterOrderLists.push(value1);
								});
							} else if (response1.data.status == "error") {
								$rootScope.stopLoader();
							}
			     return response1;
             });
   }
 
 //Consults/Referrals
 if($scope.fieldId==7){
			var dataForConsult = {
				// unitId: $scope.selectedValues.unitId,
				// organizationId : $scope.organizationId,
				admissionId : $scope.admissionId
			}

			var URI = BASE_URL
					+ ROOT_URL
					+ "/api/opd/consultDoctorListByAdmission";

			GenericServiceParamHeader
					.serviceAction(METHOD_POST, URI, dataForConsult,
							NOTIFICATION_MSG_STATUS_FALSE)
					.then(
							function(response) {
								if (response.data.status == "success") {
									$scope.consultOrdersList = response.data.listObject;
									console.log("consultOrdersListresulttt",$scope.consultOrdersList);
									if($scope.consultOrdersList.length==0)
										$scope.loadingMessage="No record found";
									angular.forEach($scope.consultOrdersList,function(value,key) {
										var flag = false;
										angular.forEach($scope.addedConsultOrdersList,function(value1,key1) {
											if(value1.admissionId == value.admissionId ){
													flag = true;
											}
										});
									if(flag== true){
											value.consultReferalChk=1;
									}else{
											value.consultReferalChk=0;
									}
										
						});
								}
							});
 }
 
 
 //Discharge Medications
 if($scope.fieldId==12){
		var DischargeMedicationlist=[];
		var medicationobj = {
				//patientId:$scope.patientId,
				admissionId:$scope.admissionId,
				unitId:$scope.unitId,
				organizationId:$scope.organizationId,
				dischargeMedicationFlag:true
		};

		var URI = BASE_URL + ROOT_URL
				+ "/api/opd/getDischargeMedication";
		BillingGenericService
				.serviceAction("POST",URI,medicationobj
						)
				.then(
						function(response) {
							if (response.data.status == "success") {
								$scope.DischargeMedicationlist = response.data.listObject;
								if($scope.DischargeMedicationlist.length==0)
									$scope.loadingMessage="No record found";
								console.log("DischargeMedicationlist list="
												+ JSON.stringify($scope.DischargeMedicationlist));
								angular.forEach($scope.DischargeMedicationlist,function(value,key) {
									var flag = false;
									angular.forEach($scope.addedDischargeMedicationlist,function(value1,key1) {
										if(value1.prescriptionId == value.prescriptionId ){
												flag = true;
										}
									});
								if(flag== true){
										value.dischargeMedicationChk=1;
								}else{
										value.dischargeMedicationChk=0;
								}
									
					   });

							}
						});
 }
 //Transfer Of Care
 if($scope.fieldId==11){
 var allTransferOfcareNoteslist=[];
	var dataTransferOfCareObj = {
			admissionId:$scope.admissionId
	};

	var URI = BASE_URL + ROOT_URL
			+ "/adt/allTransferOfcareNotes";
	GenericService.serviceAction("POST", URI,dataTransferOfCareObj)
			.then(
					function(response) {
						if (response.data.status == "success") {
							$scope.allTransferOfcareNoteslist = response.data.listObject;
							console.log("allTransferOfcareNotes list="
											+ JSON.stringify($scope.allTransferOfcareNoteslist));
							if($scope.allTransferOfcareNoteslist.length==0)
								$scope.loadingMessage="No record found";
							angular.forEach($scope.allTransferOfcareNoteslist,function(value,key) {
								var flag = false;
								angular.forEach($scope.addedTransferOfCareList,function(value1,key1) {
									if(value1.transferOfCareId == value.transferOfCareId ){
											flag = true;
									}
								});
							if(flag== true){
									value.trasferOfCareChk=1;
							}else{
									value.trasferOfCareChk=0;
							}
								
				});

						}
					});

 }
	  
		}
		
		
			
			//for date
			$scope.getFormattedDate = function(date,isTime){
				var tempDate = new Date(date);
				var date = tempDate.getDate().toString().length == 1 ? "0"+tempDate.getDate().toString() : tempDate.getDate().toString();
				var month =  (tempDate.getMonth() + 1).toString().length == 1 ? "0"+(tempDate.getMonth() + 1).toString() : (tempDate.getMonth() + 1).toString();
				var year = tempDate.getFullYear();
				if(isTime)
					return date + "-" + month + "-" + year + " "+tempDate.getHours()+":"+tempDate.getMinutes()+":"+getSeconds();
				else
					return date + "-" + month + "-" + year + " 00:00:00";
			}
			
			

			$scope.init = function(){
				
				$scope.identificationTypeList = [];
				$scope.sexMasterList = [];
				$scope.getSexMasterList();
				$scope.getIdentificationTypeList();
				$scope.patientDetailsList = [];
				$scope.patientDetails = {}
				$scope.billing = true;
				$scope.deposit = {};
				$scope.paymentDetails = {};
				$scope.initPaymentDetailsObj();
				$scope.listPaymentDetails = [];
				$scope.getCardTypesList();
				$scope.dueAmount = parseFloat(0).toFixed(2);
				$scope.visitNoList = [];
			}
			$scope.init();
			
			/************************Save Template**********************/
			$scope.PopupOkBtn = function() {
				$scope.popUpFlag = false;
				$scope.save();
			}
		     /**************************get list of template******************/
			$scope.getTemplateList = function() {

				var URI = BASE_URL + ROOT_URL+ "";
				var data = {
                           organizationId : $scope.organizationId,
                           specialityId : $scope.specialityId 
                 }
				GenericService.serviceAction("POST", URI, data).then(function(response) {
							if (response.data.status == "success") {
								$rootScope.stopLoader();
								$scope.getTemplatelist = [];
								$scope.getTemplatelist = response.data.listObject;
								console.log("getTemplatelist",$scope.getTemplatelist);
							}
						});
			}
			$scope.getTemplateList();
			
			/*************************FieldMaster and dischargeSummaryFieldMasterList*****************************/
			
		
			
			/*****************************Save for Discharge Summary***********/
			$scope.PopupOkBtn = function() {
				$scope.popUpFlag = false;
				$scope.save(true);
			}
			
			
			
			$scope.save = function(){
				
				if($scope.admissionId==null){
					growl.warning("Select Patient",
							{
								title : 'Warning!'
							});
					return false;
				}
				if($scope.dischargeTemplateId==null){
					growl.warning("Select Template",
							{
								title : 'Warning!'
							});
					return false;
				}
				$rootScope.startLoader();
					if($scope.popUpFlag==false )
					{
					  $rootScope.startLoader();
					}
				
			
				var date = new Date();
				$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
				$scope.fieldListTemplate =[];
				console.log("fieldlist dsfdsf",$scope.DischargeSummaryFinalList);
				var data1={	
						"dischargeFieldId":"",
						"dischargeFieldDescription":""
					}
				angular.forEach($scope.DischargeSummaryFinalList, function(value,key) {
					if(value.dischargeFieldDescription){
						
						var obj = angular.copy(data1);
						obj.dischargeFieldId = value.dischargeFieldId;
						obj.dischargeFieldDescription= value.dischargeFieldDescription;
 				      $scope.fieldListTemplate.push(obj);
					}
					
		         });
				
				console.log("fieldlist",$scope.fieldListTemplate);
				
				var data={	
						
						patientId:$scope.patientId,
						admissionId:$scope.admissionId,
						departmentId:$scope.departmentId,
						dischargeTemplateId:$scope.dischargeTemplateId,
						organizationId:$scope.organizationId,
						unitId:$scope.unitId,
						createdBy:$scope.createdBy,
						updatedBy:$scope.updatedBy,
						status:'A',
						createdDate:$scope.dt,
						updatedDate:$scope.dt,
						dischargeSummaryTemplateList:$scope.fieldListTemplate
					}
		      if($scope.fieldListTemplate.length>0){
			
				if ($scope.popUpFlag) {
					$scope.showModal = !$scope.showModal;

				} else {
				var URI = BASE_URL + ROOT_URL +"/adt/discharge/savePatientDischargeSummary";
				console.log("Save-URI-",URI);
				console.log(" save OBJ",data); 
				GenericService.serviceAction("POST",URI,data).then(function(response){
					$scope.dischargeSummaryId=response.data.object.dischargeSummaryId;
					if (response.data.status == 'success')
					{
						$scope.disableReleaseBtn=false;
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
						//$scope.initMaster();
						$rootScope.stopLoader();
						
						$scope.DischargeSummaryFinalList=[];
						$scope.patientDetailsList=[];
						//$scope.patientTemplateList=[];
						$scope.listPaymentDetails = [];
						$scope.dischargeTemplateId=null;
						//$scope.admissionId=null;
						$scope.masterObj.templateId=null;
					} else if(response.data.status == 'error')
					{
						growl.error(response.data.message,
								{
									title : 'Error!'
								});
						$rootScope.stopLoader();
					}
					$rootScope.stopLoader();
					});
					
			      }
		}
		else{
			growl.error("Fill at least one field's Discharge Summary Discription",
					{
						title : 'Error!'
					});
			$rootScope.stopLoader();
		}
				
			
		}
			/***************Discharge Summary Report**************Chetana*********/
			$scope.printDischargeSumReport=function()
			{
				//$scope.save();
				if($scope.dischargeSummaryId!=null){
					var data =  {
				            "appName": "HIS",
				            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
				            "reportDesignFilePath": "Dischargesummary.rpt",
				            "reportFormat": "PDF",
				            "exportFileName": "DischargeSummary",
				            "reportParams": [
				              {
					             "paramName": "Discharge_summary_Id",
					             "paramType": "Integer",
					             "paramValue": $scope.dischargeSummaryId
				              },
				              {
						             "paramName": "selectedHeader",
						             "paramType": "Integer",
						             "paramValue":  1
					              }
				            ]
				 };
					reportService.generate(REPORT_API_URI1,data);
				}
				else{
					growl.error("can not print the report",
							{
								title : 'Error!'
							});
					}
				
		}
			var labConfig  =    $scope.checkLabConfig();

			$scope.rePrintInvestigation = function(labObj, idx,printFlag) {
				if (labObj.sampleStatusId == 8) {

					if (labConfig.biochemistryId == labObj.subDeptId
							|| labConfig.clinicalPathologyId == labObj.subDeptId
							|| labConfig.hematologyId == labObj.subDeptId) {
						var data = {
							"appName" : "HIS",
							"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
							"reportDesignFilePath" : "pathologyReport.rpt",
							"reportFormat" : "PDF",
							"exportFileName" : "pathologyReport",
							"reportParams" : [ {
								"paramName" : "labtesresid",
								"paramType" : "Integer",
								"paramValue" : labObj.labTestResId
							},{
					             "paramName": "isOriginal",
					             "paramType": "Integer",
					             "paramValue": printFlag
				           } ]
						};
						reportService.generate(REPORT_API_URI1,
								data);
					} else if (labConfig.microbiologyId == labObj.subDeptId) {
						var data = {
							"appName" : "HIS",
							"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
							"reportDesignFilePath" : "pathologyReport_Sensitivity.rpt",
							"reportFormat" : "PDF",
							"exportFileName" : "pathologyReport",
							"reportParams" : [ {
								"paramName" : "lab_sample_dtls_id",
								"paramType" : "Integer",
								"paramValue" : labObj.labSampleDtlsId
							},{
					             "paramName": "isOriginal",
					             "paramType": "Integer",
					             "paramValue": printFlag
				           } ]
						};
						reportService.generate(REPORT_API_URI1,
								data);
					} else if (labConfig.histopathologyId == labObj.subDeptId
							|| labConfig.cystopathologyId == labObj.subDeptId) {
						var data = {
							"appName" : "HIS",
							"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
							"reportDesignFilePath" : "GetHistopathologyReport.rpt",
							"reportFormat" : "PDF",
							"exportFileName" : "pathologyReport",
							"reportParams" : [ {
								"paramName" : "labtesresid",
								"paramType" : "Integer",
								"paramValue" : labObj.labSampleDtlsId
							},{
					             "paramName": "isOriginal",
					             "paramType": "Integer",
					             "paramValue": printFlag
				           } ]
						};
					}else if(labConfig.serologyId==labObj.subDeptId)
				  {
					var data =  
					{
				            "appName": "HIS",
				            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
				            "reportDesignFilePath": "SerologyReport.rpt",
				            "reportFormat": "PDF",
				            "exportFileName": "pathologyReport",
				            "reportParams": [
				              {
					             "paramName": "lab_sample_dtls_id",
					             "paramType": "Integer",
					             "paramValue": labObj.labSampleDtlsId
				              },
				              {
						             "paramName": "isOriginal",
						             "paramType": "Integer",
						             "paramValue": printFlag
					           }
				            ]
				      };
					reportService.generate(REPORT_API_URI1,data);
					}
				}else if(labObj.templateTransactionId!=null &&labObj.templateTransactionId>0)
				   {
	    			var data =  {
	    		            "appName": "HIS",
	    		            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
	    		            "reportDesignFilePath": "RadiologyReport.rpt",
	    		            "reportFormat": "PDF",
	    		            "exportFileName": "RadiologyReport",
	    		            "reportParams": [
	    		              {
	    			             "paramName": "template_transaction_id",
	    			             "paramType": "Integer",
	    			             "paramValue": labObj.templateTransactionId
	    		              },{
					        		"paramName": "isOriginal",
					        		"paramType": "Integer",
					        		"paramValue": 1
					        		},{
					        		"paramName": "isHeader",
					        		"paramType": "Integer",
					        		"paramValue": 0
					        		}
	    		            ]
	    		    };
	    	           reportService.generate(REPORT_API_URI1,data);
					}
				
				else {
					growl.error("Final Report not released!!",
							{
								title : 'Error!'
							});

				}
			}

		/*CKEDITOR.replace( 'editor1', {
		    on: {
		        instanceReady: function() {
		            this.dataProcessor.htmlFilter.addRules({
		                elements: {
		                    p: function( element ) {
		                        // If there's no class, assing the custom one:
		                        if ( !element.attributes.class )
		                            element.attributes.class = pClass;

		                        // It there's some other class, append the custom one:
		                        else if ( !element.attributes.class.match( pClassRegexp ) )
		                            element.attributes.class += ' ' + pClass;

		                        // Add the subsequent id:
		                        if ( !element.attributes.id )
		                            element.attributes.id = 'paragraph_' + ++idCounter;
		                    }
		                }
		            });
		        }
		    }
		});*/
								
								$scope.isRMO = cookieObject.isRMO;
								var rmoDoctorOP=JSON.parse(localStorage.getItem('rmoMappedOPDoctorList'));
								var rmoDoctorIP=JSON.parse(localStorage.getItem('rmoMappedIPWard'));
								 rmoDoctorOP= rmoDoctorOP != undefined ? rmoDoctorOP : [];
								 rmoDoctorIP= rmoDoctorIP != undefined ? rmoDoctorIP : [];
								 
								$scope.rmoVisitType=$cookies.getObject('rmoVisit');
								 $scope.rmoMappedOpDoctorList=[];
								$scope.rmoMappedWardList=[];
								if($scope.isRMO=='Y' && (rmoDoctorOP==null || rmoDoctorIP==null)){
									 growl.warning(
												'RMO roster is not defined !!',
												{
													title : 'Warning!'
												});
									return false;
								}/*else if($scope.isRMO=='Y' && $scope.rmoVisitType!=3){
									var message="RMO is allocated to "+(($scope.rmoVisitType==1) ? "OP":($scope.rmoVisitType==2) ?"IP":"ER");
									 growl.warning(message,
												{
													title : 'Warning!'
												});
									return false;
								}*/
								else{
									 $scope.rmoMappedOpDoctorList= rmoDoctorOP!=undefined ? rmoDoctorOP :[];
									 $scope.rmoMappedWardList= rmoDoctorIP!=undefined ? rmoDoctorIP: [];
								}	
								
					    $scope.doctorId=cookieObject.doctorId;

								$scope.getDischagePatientListByDoctorIdList = function() {
								$scope.dischagePatientList = [];
								var data = {
									"organizationId" : $scope.organizationId,
									"unitId" : $scope.unitId
									//"doctorId" : $scope.doctorId
								}
								
								if($scope.isRMO=='Y'){
									data.rmoMappedWardList=$scope.rmoMappedWardList;
								}else{
									data.doctorId=$scope.doctorId;
								}
								
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL
										+ "/adt/getDischagePatientListByDoctorId";
								 if( $scope.doctorId!=undefined && $scope.doctorId!=null){
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													if (response.data.status == "success") {
														angular.forEach(response.data.listObject,function(value,index){
															if(value.visitTypeId==3){
																value.visitType="Day Care";
															$scope.dischagePatientList.push(value);
															}
														});
													}
													$rootScope.stopLoader();
												});

							}else{
								$rootScope.stopLoader();
								growl.warning("only doctor can access this page",
										{
											title : 'Warning!'
										});
							}
			                
			                }
							$scope.getDischagePatientListByDoctorIdList();
							
							$scope.showFinalReleasePopup=function(){
								  $scope.$broadcast('show-errors-reset');
								 $('#doctorPinModal').modal('show');
							}
							$scope.closePopup=function(){
								 $('#doctorPinModal').modal('hide');
							}
							
							$scope.doctorPin="";
							$scope.isReleasePopupBtn=false;
							$scope.checkDoctorPin= function()
							{
								
								$scope.$broadcast('show-errors-check-validity');
								if (this.doctorPinForm.$valid)
								{
									$scope.isReleasePopupBtn=true;
                                    var data={
									"organizationId":$scope.organizationId,
									"unitId":$scope.unitId,
									"doctorPin":$scope.doctorPin,
									"doctorId":$scope.doctorId
                                    }
								 var URI = BASE_URL + ROOT_URL + "/api/global/validateDoctorPinByDoctorId";
								 GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) 
												{
													console.log(response);
													if (response.data.status == "success")
														{
														 $rootScope.startLoader();
														var data1={
																"organizationId":$scope.organizationId,
																"unitId":$scope.unitId,
																"dischargeSummaryId":$scope.dischargeSummaryId,
																"admissionId":$scope.admissionId
							                                    }
														 var URI1 = BASE_URL + ROOT_URL + "/adt/discharge/updateDischargeSummaryReportStatus";
														 
														GenericService.serviceAction("POST", URI1, data1).then(function(response){
															if (response.data.status == "success")
															{
																$scope.isSaveDisable=true;
																$scope.disableReleaseBtn=true;
																$scope.disablePrintBtn=false;
																$rootScope.stopLoader();
																growl.success("Report released",
																		{
																			title : 'Success!'
																		});
																
															}
															else{
																$rootScope.stopLoader();
															}
														 });
													
														  $('#doctorPinModal').modal('hide');
														  $scope.$broadcast('show-errors-reset');
														  $scope.isReleasePopupBtn=false;
														  
														}
													else if(response.data.status == "error")
														{
														$scope.isReleasePopupBtn=false;
														growl
														.error(
																response.data.message,
																{
																	title : 'Error!'
																});
														   $rootScope.stopLoader();
														}
												});
								}
								else{
									 $('#doctorPinModal').modal('show');
								}
							}
			
							
							$scope.getClassForColor = function(info)
							{

								   if(info.isCriticalOrder==1){
									   return 'td-bg-red';
								   }else if (info.isVip=='Y') 
								   {
									    return 'td-bg-blue';
								   }else if(info.encounterTypeId==6){
									   return 'td-bg-green';
								   }
							}
							
			            //show patient header and template 
							$scope.admissionId=null;
							$scope.departmentId=null;
							//get Patient Id,department id and template id
							$scope.patientId=null;
							$scope.showPatientHeader='N';
							$scope.showPatientHeaderData = function(patientDetailsObj)
							{
								if(patientDetailsObj!=null)
								  {
									//patientDetailsObj.startConsultanceStartTime = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'); 
									console.log("patientDetails",JSON.stringify(patientDetailsObj));
									
									
									$scope.showPatientHeader='Y';
									console.log('patientDetailsObj',
											patientDetailsObj);
									$scope.patientDetails = {};
									$scope.patientDetails.patientName = patientDetailsObj.pFirstName;
									$scope.patientDetails.uhIdNumber = patientDetailsObj.uhidnumber;
									$scope.patientDetails.genderCode = patientDetailsObj.genderCode;
									$scope.patientDetails.age = patientDetailsObj.age;
									$scope.patientDetails.birthDate = patientDetailsObj.dob;
									$scope.patientDetails.admissionId = patientDetailsObj.admissionId;
									$scope.patientDetails.patientId = patientDetailsObj.patientId;
									$scope.patientDetails.visitTypeId = patientDetailsObj.visitTypeId;
									$scope.patientDetails.specialityId = patientDetailsObj.specialityId;
									$scope.patientDetails.doctorId = patientDetailsObj.doctorId;
									$scope.patientDetails.treatingDoctorSpecialityId = "";
									$scope.patientDetails.treatingDoctorId = "";
									$scope.visitTypeId = $scope.patientDetails.visitTypeId;
									$scope.patientDetails.isTreatingDoctor = 'Y';
									$scope.patientDetails.genderId = patientDetailsObj.genderId;
									$scope.patientDetails.prefixId = patientDetailsObj.prefixId;
									$scope.patientDetails.dischargeStatusId = patientDetailsObj.dischargeStatusId;
									$scope.patientDetails.createdDate = patientDetailsObj.createdDate;
									$scope.patientDetails.medicoLegalId = patientDetailsObj.medicoLegalId;
									$scope.patientDetails.createdDate = patientDetailsObj.createdDate;
									$scope.patientDetails.medicoLegalId = patientDetailsObj.medicoLegalId;
									$scope.patientDetails.createdDate = patientDetailsObj.createdDate;
									$scope.patientDetails.medicoLegalId = patientDetailsObj.medicoLegalId;
									$scope.patientDetails.dateOfAdmission = patientDetailsObj.doa;
									$scope.patientDetails.patientDischargeDate = patientDetailsObj.pdd;
									$scope.patientDetails.bedNumber = patientDetailsObj.bedNumber;
									$scope.patientDetails.doctorFullName = patientDetailsObj.dFirstName;
									var age = $rootScope.getAge($scope.patientDetails.birthDate);
									$scope.patientDetails.age =age;
								
									
								  }
							  else
							     {
								    growl.error("Something went Wrong.",{title : 'Error!'});
							     }
								
									$scope.isSaveDisable=true;
									$scope.disableReleaseBtn=true;
									$scope.disablePrintBtn=true;
									$scope.patientId=patientDetailsObj.patientId;
									$scope.patientDetailsList = [];
									$scope.masterObj = {};
									$scope.DischargeSummaryFinalList=[];
									$scope.patientTemplateList=[];
									$rootScope.startLoader();
									
									//var URI = BASE_URL + ROOT_URL + "/adt/discharge/getDischargeSummaryTemplateByPatientId/11/1/1";
									//get speciality id and from speciality Id get temp id discharge_summary_template_master
									var URI = BASE_URL + ROOT_URL + "/adt/discharge/getDischargeSummaryTemplateByPatientId/"+patientDetailsObj.admissionId+"/"+$scope.organizationId+"/"+$scope.unitId;
									BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										if (response.data.status == "success"){
											$scope.patientTemplateList = response.data.listObject;
											$scope.admissionId=response.data.listObject[0].admissionId;
											$scope.departmentId=response.data.listObject[0].departmentId;
											console.log("patientTemplateList:",$scope.patientTemplateList);
											$rootScope.stopLoader();
										}
									});
								
							}	
							
							
					//Generate Discharge Card
						$scope.printDischargeCard=function(){
							if($scope.dischargeSummaryId!=null){ 
								var elementId = document.getElementById('dischargeCardId');
							    if(elementId!=null)
							    	{
							    	 document.body.removeChild(elementId);
							    	}
							   	
							var URI = BASE_URL + ROOT_URL + "/adt/discharge/dischargeSummaryCard";
						     var form = null;
						     form = document.createElement("form");
						     form.setAttribute("id", "dischargeCardId");
						     form.setAttribute("name", "Discharge Card");
						     form.setAttribute("method", "post");
						     form.setAttribute("action", URI);
						     form.setAttribute("target", "_blank");
						     
						     
						     var hiddenField1 = document.createElement("input"); 
						     hiddenField1.setAttribute("type", "hidden");
						     hiddenField1.setAttribute("name", "admissionId");
						     hiddenField1.setAttribute("value",$scope.admissionId);
						     
						     var hiddenField2 = document.createElement("input"); 
						     hiddenField2.setAttribute("type", "hidden");
						     hiddenField2.setAttribute("name", "dischargeSummaryId");
						     hiddenField2.setAttribute("value",$scope.dischargeSummaryId);
						     
						     var hiddenField3 = document.createElement("input"); 
						     hiddenField3.setAttribute("type", "hidden");
						     hiddenField3.setAttribute("name", "orgUnitId");
						     hiddenField3.setAttribute("value",$scope.unitId);
						     
						     var hiddenField4 = document.createElement("input"); 
						     hiddenField4.setAttribute("type", "hidden");
						     hiddenField4.setAttribute("name", "orgId");
						     hiddenField4.setAttribute("value", $scope.organizationId);
						     
						     form.appendChild(hiddenField1);
						     form.appendChild(hiddenField2);
						     form.appendChild(hiddenField3);
						     form.appendChild(hiddenField4);
						     
						     document.body.appendChild(form);
						     $("form[name='Discharge Card']").submit();
						     }
							else{
								growl.error("can not print the report",
										{
											title : 'Error!'
										});
								}
							
						}
												
}]);