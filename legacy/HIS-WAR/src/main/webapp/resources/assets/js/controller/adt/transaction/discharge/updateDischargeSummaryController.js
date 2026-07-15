
angular.module('myApp')
		.controller("updateDischargeSummaryController",
		['$scope','$rootScope','$state','$timeout','$cookies','GenericService','BillingGenericService','PagerService','promiseFactory','$sessionStorage','$filter','growl','reportService','GenericServiceParamHeader','$stateParams',
		function($scope,$rootScope,$state,$timeout,$cookies, GenericService,BillingGenericService,PagerService,promiseFactory,$sessionStorage,$filter,growl,reportService,GenericServiceParamHeader,$stateParams){
	
			$rootScope.loginpage = true;
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
	
			$scope.detailObj = $stateParams.dischargeSummObj;
			var isEmpty=angular.equals('{}',$scope.detailObj);
           if(isEmpty==true){
        	   $state.go('dischargedPatients');
        	   return;
			}
           
           
			$scope.commonListCount;
			$scope.dischargeId=null;
			
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
			$scope.doctorId = $scope.detailObj.doctorId;
			$scope.dischargeSummaryId=$scope.detailObj.dischargeSummaryId;
			$scope.admissionId=$scope.detailObj.admissionId;
			$scope.patientId=$scope.detailObj.patientId;
			$scope.genderId=$scope.detailObj.genderId;
			
			 var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
			 if (cookieObjectAppConfigSecond != undefined) {
			 $scope.dischargeSummaryDataViewOptionId = cookieObjectAppConfigSecond.dischargeSummaryDataViewOptionId;
			 }
			 $scope.dischargeSummaryDataViewOptionIdTest = 1;
			 
			$scope.loadingMessage="";
			$scope.showModal = false;
			$scope.buttonClicked = "";
			$scope.popUpFlag = true;
			$scope.showPatientHeader='Y';

//reviewPopupstart
$scope.cleanHtml = function (html) {
	if (!html) return '';

	let text = html;

	// Handle unordered list <ul><li>
	text = text.replace(/<ul[^>]*>/gi, '\n');
	text = text.replace(/<\/ul>/gi, '\n');
	text = text.replace(/<li[^>]*>/gi, '• ');
	text = text.replace(/<\/li>/gi, '\n');

	// Handle ordered list <ol><li>
	let index = 1;
	text = text.replace(/<ol[^>]*>([\s\S]*?)<\/ol>/gi, function (match, content) {
		index = 1;
		return content.replace(/<li[^>]*>([\s\S]*?)<\/li>/gi, function (_, liText) {
			return (index++) + '. ' + liText + '\n';
		});
	});

	// Paragraphs & breaks
	text = text.replace(/<\/p>/gi, '\n');
	text = text.replace(/<br\s*\/?>/gi, '\n');
	text = text.replace(/<p[^>]*>/gi, '');

	// Cleanup
	text = text.replace(/&nbsp;/gi, ' ');
	text = text.replace(/<[^>]+>/g, '');
	text = text.replace(/\n\s*\n+/g, '\n');

	return text.trim();
};

// --------------------------------------------
// CHECK IF CONTENT EXISTS
// --------------------------------------------
$scope.hasContent = function (html) {
	return $scope.cleanHtml(html).length > 0;
};
             //reviewPopupend



			 $scope.printDischargeSumReportP=function(flag)
			 {
				  if(flag== true){
					var obj =$scope.detailObj					
					$scope.dischargeSummaryId=obj.dischargeSummaryId;
					$scope.admissionId=obj.admissionId;
					if($scope.dischargeSummaryId!=null && $scope.dischargeSummaryId!=undefined){
						if(obj.isHtml!= null && obj.isHtml == 'Y'){
							$("#printHeaderReportModal").show();
						}else{
							// $scope.sendDischargeSummaryToMyLife($scope.dischargeSummaryId, obj);
							var data =  
							{
									"appName": "HIS",
									"appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
									"reportDesignFilePath": "Dischargesummary.rpt",
									"reportFormat": "PDF",
									"exportFileName": "pathologyReport",
									"reportParams": [
									  {
										 "paramName": "discharge_summary_id",
										 "paramType": "String",
										 "paramValue": $scope.dischargeSummaryId
									  }
									  ,
									  {
											 "paramName": "selectedHeader",
											 "paramType": "Integer",
											 "paramValue": 1
										  }
									]
							  };
							reportService.generate(REPORT_API_URI1,data);
	
						}
					   
					   }
					else{
						growl.error("Discharge Summary Not Saved",
								{
									title : 'Error!'
								});
						}
				  } 
		 }






			//getFieldList by template id
			$scope.dischargeTemplateId=null;
			$scope.getFieldsByTemplateId = function(templateId)
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
				$scope.dischargeFieldMasterList=[];
					//var URI = BASE_URL + ROOT_URL + "/adt/discharge/getFieldTemplateSummaryByTemplateId/3/1/1";
				var URI = BASE_URL + ROOT_URL + "/adt/discharge/getFieldTemplateSummaryByTemplateId/"+$scope.dischargeTemplateId+"/"+$scope.organizationId+"/"+$scope.unitId;
					GenericService.serviceAction("GET",URI,data).then(function(response) {
					if (response.data.status == "success")
						{
						$scope.dischargeFieldMasterList = response.data.listObject;
					    console.log("dischargeFieldMasterList",$scope.dischargeFieldMasterList);
					    
						$timeout( function(){
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
												$scope.dischargeId=response.data.listObject[0].dischargeId;
												if(response.data.listObject[0].isDischargeSummaryRelease=='Y'){
													$scope.isSaveDisable=true;
													$scope.disableReleaseBtn=true;
													$scope.disableFollowupBtn=true;
													$scope.disablePrintBtn=false;
												}else{
													$scope.isSaveDisable=false;
													if($scope.dischargeId!=null)
													$scope.disableReleaseBtn=false;
													else
													$scope.disableReleaseBtn=true;	
													$scope.disablePrintBtn=true;
													if($scope.isFollowUpAppointmentTaken=='N')
														$scope.disableFollowupBtn=false;
												}
												
										}
										else{
											$scope.isSaveDisable=false;
											$scope.disableReleaseBtn=true;
											$scope.disableFollowupBtn=true;
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
			                     		 	$scope.newArr.dischargeSummaryDataViewOptionId = value1.dischargeSummaryDataViewOptionId;
			                     		 	$scope.newArr.dischargeSummaryTemplateListdata = [];
			                     		 	$scope.newArr.dischargeSummaryTemplateListdata = value1.dischargeSummaryTemplateListdata;
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
								                     		    newArr.dischargeSummaryDataViewOptionId = 1;
								                     		 	newArr.dischargeSummaryTemplateListdata = [];
								                     		 	newArr.dischargeFieldId = value.dischargeFieldId;
																$scope.DischargeSummaryFinalList.push(newArr);
																
															}
														
			                     });
			                  console.log("Final discharge Summary Array",$scope.DischargeSummaryFinalList);
			                  $rootScope.stopLoader();
			                  
			              	angular.forEach($scope.DischargeSummaryFinalList ,function(value,key){
								$scope.rowList = [];
								$scope.columnList = [];
								$scope.columnHeaderList = [];
								$scope.comObj = {};
								value.savedList = [];
								value.rowList = [];
								value.columnHeaderList = [];
								
								value.columnHeaderListPersonalHistory=[];
								value.rowListPersonalHistory=[];
								value.columnListPersonalHistory=[];
								value.savedListPersonalHistory = [];
								
								value.columnListFamilyHistory=[];
								value.columnHeaderFamilyHistory=[];
								value.rowListFamilyHistory=[];
								value.savedListFamilyHistory = [];
								
								value.columnHeaderSurgicalHistory=[];
								value.rowListSurgicalHistory=[];
								value.columnListSurgicalHistory=[];
								value.savedListSurgicalHistory = [];
								
								value.dischargeSummaryDataViewOptionIdTest = value.dischargeSummaryDataViewOptionId;
								
								
								if(value.dischargeFieldId == 1){
									if(value.dischargeSummaryTemplateListdata != undefined && value.dischargeSummaryTemplateListdata != null && value.dischargeSummaryTemplateListdata.length > 0){
										$scope.comObj.numberOfRowAllergies = value.dischargeSummaryTemplateListdata[0].numberOfRow;
										$scope.comObj.numberOfCoulmnAllergies = value.dischargeSummaryTemplateListdata[0].numberOfCoulmn;
										value.dischargeFieldDescription = "";
										value.columnListAllergies=[];
										value.columnHeaderListAllergies=[];
										value.rowListAllergies=[];
										value.savedListAllergies = [];
										
										$scope.columnListAllergies=[];
										$scope.columnHeaderListAllergies=[];
										$scope.rowListAllergies=[];
										$scope.savedListAllergies = [];
										
											for(var i = 0; i < $scope.comObj.numberOfRowAllergies ;i++){
												var newArr = {};
												newArr.rowId = i;
												newArr.coulmnId = i;
												newArr.rowName = '';
												$scope.rowListAllergies.push(newArr);
										     }
											
											for(var i = 0; i < $scope.comObj.numberOfCoulmnAllergies ;i++){
												var newArr = {};
												newArr.coulmnId = i;
												newArr.rowId = i;
												newArr.columnName = '';
												newArr.inputValue = '';
												$scope.columnListAllergies.push(newArr);
												$scope.columnHeaderListAllergies.push(newArr);
										     }
											
											angular.forEach($scope.rowListAllergies,function(value11,key11){
												angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
													if(value11.rowId == value11.rowId){
														value11.rowName = value1.rowName;
													}
												});
											});
											angular.forEach($scope.columnListAllergies,function(value12,key12){
												angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
													if(value12.coulmnId == value1.coulmnId){
														value12.columnName = value1.columnName;
													}
												});
											});
											angular.forEach($scope.columnHeaderListAllergies,function(value13,key13){
												angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
													if(value13.coulmnId == value1.coulmnId){
														value13.columnName = value1.columnName;
													}
												});
											});
							
										value.rowListAllergies=$scope.rowListAllergies;
										value.columnListAllergies=$scope.columnListAllergies;
										value.columnHeaderListAllergies=$scope.columnHeaderListAllergies;
										var date = new Date();
										$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
										
										if(value.dischargeSummaryTemplateListdata != undefined && value.dischargeSummaryTemplateListdata != null && value.dischargeSummaryTemplateListdata.length > 0){
											value.savedListAllergies = value.dischargeSummaryTemplateListdata;
											value.tabelNameAllergis =  value.dischargeSummaryTemplateListdata[0].tableName;
											angular.forEach(value.savedListAllergies,function(v,k){
												v.createdDate =$scope.dt;
												v.updatedDate =$scope.dt;
											});
											value.createdDate =$scope.dt;
											value.updatedDate =$scope.dt;
										}
										}
								}
								
								
								if(value.dischargeFieldId == 2){
									if(value.dischargeSummaryTemplateListdata != undefined && value.dischargeSummaryTemplateListdata != null && value.dischargeSummaryTemplateListdata.length > 0){
										$scope.personalHistorySavedData = [];
										$scope.familyHistorySavedData = [];
										$scope.surgicalHistorySavedData = [];
										$scope.medicationHistorySavedData = [];
										$scope.medicalHistorySavedData = [];
										$scope.vaccineHistorySavedData = [];
										$scope.travelHistorySavedData = [];
										$scope.treatmentHistorySavedData = [];
										angular.forEach(value.dischargeSummaryTemplateListdata,function(v,key){
											if(v.tableTypeId ==2){
												$scope.personalHistorySavedData.push(v);
											}
											if(v.tableTypeId ==3){
												$scope.familyHistorySavedData.push(v);
											}
											if(v.tableTypeId ==4){
												$scope.surgicalHistorySavedData.push(v);
											}
											/*if(v.tableTypeId ==4){
												$scope.medicationHistorySavedData.push(v);
											}*/
											if(v.tableTypeId ==5){
												$scope.medicalHistorySavedData.push(v);
											}
											if(v.tableTypeId ==7){
												$scope.vaccineHistorySavedData.push(v);
											}
											if(v.tableTypeId ==8){
												$scope.travelHistorySavedData.push(v);
											}
											if(v.tableTypeId ==9){
												$scope.treatmentHistorySavedData.push(v);
											}
										});
										
										if($scope.personalHistorySavedData.length > 0){
											$scope.rowListPersonalHistory = [];
											$scope.columnListPersonalHistory = [];
											$scope.columnHeaderListPersonalHistory = [];
											$scope.savedListPersonalHistory = [];
											value.rowListPersonalHistory= [];
											value.columnListPersonalHistory= [];
											value.columnHeaderListPersonalHistory= [];
											value.savedListPersonalHistory= [];
											$scope.comObj.numberOfRowPersonalHistory = $scope.personalHistorySavedData[0].numberOfRow;
											$scope.comObj.numberOfCoulmnPersonalHistory = $scope.personalHistorySavedData[0].numberOfCoulmn;
											
											
												for(var i = 0; i < $scope.comObj.numberOfRowPersonalHistory ;i++){
													var newArr = {};
													newArr.rowId = i;
													newArr.coulmnId = i;
													newArr.rowName = '';
													newArr.inputbox = false;
													$scope.rowListPersonalHistory.push(newArr);
											     }
												for(var i = 0; i < $scope.comObj.numberOfCoulmnPersonalHistory ;i++){
													var newArr = {};
													newArr.coulmnId = i;
													newArr.rowId = i;
													newArr.columnName = '';
													newArr.inputValue = '';
													newArr.inputbox = false;
													$scope.columnListPersonalHistory.push(newArr);
													$scope.columnHeaderListPersonalHistory.push(newArr);
											     }
												
												angular.forEach($scope.rowListPersonalHistory,function(value11,key11){
													angular.forEach($scope.personalHistorySavedData,function(value1,key1){
														if(value11.rowId == value11.rowId){
															value11.rowName = value1.rowName;
														}
													});
												});
												angular.forEach($scope.columnListPersonalHistory,function(value12,key12){
													angular.forEach($scope.personalHistorySavedData,function(value1,key1){
														if(value12.coulmnId == value1.coulmnId){
															value12.columnName = value1.columnName;
														}
													});
												});
												angular.forEach($scope.columnHeaderListPersonalHistory,function(value13,key13){
													angular.forEach($scope.personalHistorySavedData,function(value1,key1){
														if(value13.coulmnId == value1.coulmnId){
															value13.columnName = value1.columnName;
														}
													});
												});
								
											value.rowListPersonalHistory=$scope.rowListPersonalHistory;
											value.columnListPersonalHistory=$scope.columnListPersonalHistory;
											value.columnHeaderListPersonalHistory=$scope.columnHeaderListPersonalHistory;
											var date = new Date();
											$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
											
											if($scope.personalHistorySavedData != undefined && $scope.personalHistorySavedData != null && $scope.personalHistorySavedData.length > 0){
												value.savedListPersonalHistory = $scope.personalHistorySavedData;
												value.tableNamePersonalHistory =  $scope.personalHistorySavedData[0].tableName;
												angular.forEach(value.savedListPersonalHistory,function(v,k){
													v.createdDate =$scope.dt;
													v.updatedDate =$scope.dt;
												});
												value.createdDate =$scope.dt;
												value.updatedDate =$scope.dt;
											}
										}
										
										if($scope.familyHistorySavedData.length > 0){
											$scope.rowListFamilyHistory = [];
											$scope.columnListFamilyHistory = [];
											$scope.columnHeaderListFamilyHistory = [];
											$scope.savedListFamilyHistory = [];
											value.rowListFamilyHistory= [];
											value.columnListFamilyHistory= [];
											value.columnHeaderListFamilyHistory= [];
											value.savedListFamilyHistory= [];
											$scope.comObj.numberOfRowFamilyHistory = $scope.familyHistorySavedData[0].numberOfRow;
											$scope.comObj.numberOfCoulmnFamilyHistory = $scope.familyHistorySavedData[0].numberOfCoulmn;
											
											
												for(var i = 0; i < $scope.comObj.numberOfRowFamilyHistory ;i++){
													var newArr = {};
													newArr.rowId = i;
													newArr.coulmnId = i;
													newArr.rowName = '';
													newArr.inputbox = false;
													$scope.rowListFamilyHistory.push(newArr);
											     }
												for(var i = 0; i < $scope.comObj.numberOfCoulmnFamilyHistory ;i++){
													var newArr = {};
													newArr.coulmnId = i;
													newArr.rowId = i;
													newArr.columnName = '';
													newArr.inputValue = '';
													newArr.inputbox = false;
													$scope.columnListFamilyHistory.push(newArr);
													$scope.columnHeaderListFamilyHistory.push(newArr);
											     }
												
												angular.forEach($scope.rowListFamilyHistory,function(value11,key11){
													angular.forEach($scope.familyHistorySavedData,function(value1,key1){
														if(value11.rowId == value11.rowId){
															value11.rowName = value1.rowName;
														}
													});
												});
												angular.forEach($scope.columnListFamilyHistory,function(value12,key12){
													angular.forEach($scope.familyHistorySavedData,function(value1,key1){
														if(value12.coulmnId == value1.coulmnId){
															value12.columnName = value1.columnName;
														}
													});
												});
												angular.forEach($scope.columnHeaderListFamilyHistory,function(value13,key13){
													angular.forEach($scope.familyHistorySavedData,function(value1,key1){
														if(value13.coulmnId == value1.coulmnId){
															value13.columnName = value1.columnName;
														}
													});
												});
								
											value.rowListFamilyHistory=$scope.rowListFamilyHistory;
											value.columnListFamilyHistory=$scope.columnListFamilyHistory;
											value.columnHeaderListFamilyHistory=$scope.columnHeaderListFamilyHistory;
											var date = new Date();
											$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
											
											if($scope.familyHistorySavedData != undefined && $scope.familyHistorySavedData != null && $scope.familyHistorySavedData.length > 0){
												value.savedListFamilyHistory = $scope.familyHistorySavedData;
												value.tableNameFamilyHistory =  $scope.familyHistorySavedData[0].tableName;
												angular.forEach(value.savedListFamilyHistory,function(v,k){
													v.createdDate =$scope.dt;
													v.updatedDate =$scope.dt;
												});
												value.createdDate =$scope.dt;
												value.updatedDate =$scope.dt;
											}
										}
										
										if($scope.surgicalHistorySavedData.length > 0){
											$scope.rowListSurgicalHistory = [];
											$scope.columnListSurgicalHistory = [];
											$scope.columnHeaderListSurgicalHistory = [];
											$scope.savedListSurgicalHistory = [];
											
											value.rowListSurgicalHistory= [];
											value.columnListSurgicalHistory= [];
											value.columnHeaderListSurgicalHistory= [];
											value.savedListSurgicalHistory= [];
											
											$scope.comObj.numberOfRowSurgicalHistory = $scope.surgicalHistorySavedData[0].numberOfRow;
											$scope.comObj.numberOfCoulmnSurgicalHistory = $scope.surgicalHistorySavedData[0].numberOfCoulmn;
											
											
												for(var i = 0; i < $scope.comObj.numberOfRowSurgicalHistory ;i++){
													var newArr = {};
													newArr.rowId = i;
													newArr.coulmnId = i;
													newArr.rowName = '';
													newArr.inputbox = false;
													$scope.rowListSurgicalHistory.push(newArr);
											     }
												for(var i = 0; i < $scope.comObj.numberOfCoulmnSurgicalHistory ;i++){
													var newArr = {};
													newArr.coulmnId = i;
													newArr.rowId = i;
													newArr.columnName = '';
													newArr.inputValue = '';
													newArr.inputbox = false;
													$scope.columnListSurgicalHistory.push(newArr);
													$scope.columnHeaderListSurgicalHistory.push(newArr);
											     }
												
												angular.forEach($scope.rowListSurgicalHistory,function(value11,key11){
													angular.forEach($scope.surgicalHistorySavedData,function(value1,key1){
														if(value11.rowId == value11.rowId){
															value11.rowName = value1.rowName;
														}
													});
												});
												angular.forEach($scope.columnListSurgicalHistory,function(value12,key12){
													angular.forEach($scope.surgicalHistorySavedData,function(value1,key1){
														if(value12.coulmnId == value1.coulmnId){
															value12.columnName = value1.columnName;
														}
													});
												});
												angular.forEach($scope.columnHeaderListSurgicalHistory,function(value13,key13){
													angular.forEach($scope.surgicalHistorySavedData,function(value1,key1){
														if(value13.coulmnId == value1.coulmnId){
															value13.columnName = value1.columnName;
														}
													});
												});
								
											value.rowListSurgicalHistory=$scope.rowListSurgicalHistory;
											value.columnListSurgicalHistory=$scope.columnListSurgicalHistory;
											value.columnHeaderListSurgicalHistory=$scope.columnHeaderListSurgicalHistory;
											var date = new Date();
											$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
											
											if($scope.surgicalHistorySavedData != undefined && $scope.surgicalHistorySavedData != null && $scope.surgicalHistorySavedData.length > 0){
												value.savedListSurgicalHistory = $scope.surgicalHistorySavedData;
												value.tableNameSurgicalHistory =  $scope.surgicalHistorySavedData[0].tableName;
												angular.forEach(value.savedListSurgicalHistory,function(v,k){
													v.createdDate =$scope.dt;
													v.updatedDate =$scope.dt;
												});
												value.createdDate =$scope.dt;
												value.updatedDate =$scope.dt;
											}
										}
										
										if($scope.medicationHistorySavedData.length > 0){
											$scope.rowListMedicationHistory = [];
											$scope.columnListMedicationHistory = [];
											$scope.columnHeaderListMedicationHistory = [];
											$scope.savedListMedicationHistory = [];
											
											value.rowListMedicationHistory= [];
											value.columnListMedicationHistory= [];
											value.columnHeaderListMedicationHistory= [];
											value.savedListMedicationHistory= [];
											
											$scope.comObj.numberOfRowMedicationHistory = $scope.medicationHistorySavedData[0].numberOfRow;
											$scope.comObj.numberOfCoulmnMedicationHistory = $scope.medicationHistorySavedData[0].numberOfCoulmn;
											
											
												for(var i = 0; i < $scope.comObj.numberOfRowMedicationHistory ;i++){
													var newArr = {};
													newArr.rowId = i;
													newArr.coulmnId = i;
													newArr.rowName = '';
													newArr.inputbox = false;
													$scope.rowListMedicationHistory.push(newArr);
											     }
												for(var i = 0; i < $scope.comObj.numberOfCoulmnMedicationHistory ;i++){
													var newArr = {};
													newArr.coulmnId = i;
													newArr.rowId = i;
													newArr.columnName = '';
													newArr.inputValue = '';
													newArr.inputbox = false;
													$scope.columnListMedicationHistory.push(newArr);
													$scope.columnHeaderListMedicationHistory.push(newArr);
											     }
												
												angular.forEach($scope.rowListMedicationHistory,function(value11,key11){
													angular.forEach($scope.medicationHistorySavedData,function(value1,key1){
														if(value11.rowId == value11.rowId){
															value11.rowName = value1.rowName;
														}
													});
												});
												angular.forEach($scope.columnListMedicationHistory,function(value12,key12){
													angular.forEach($scope.medicationHistorySavedData,function(value1,key1){
														if(value12.coulmnId == value1.coulmnId){
															value12.columnName = value1.columnName;
														}
													});
												});
												angular.forEach($scope.columnHeaderListMedicationHistory,function(value13,key13){
													angular.forEach($scope.medicationHistorySavedData,function(value1,key1){
														if(value13.coulmnId == value1.coulmnId){
															value13.columnName = value1.columnName;
														}
													});
												});
								
											value.rowListMedicationHistory=$scope.rowListMedicationHistory;
											value.columnListMedicationHistory=$scope.columnListMedicationHistory;
											value.columnHeaderListMedicationHistory=$scope.columnHeaderListMedicationHistory;
											var date = new Date();
											$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
											value.tableNameMedicationHistory ='Medication History';
											
											if($scope.medicationHistorySavedData != undefined && $scope.medicationHistorySavedData != null && $scope.medicationHistorySavedData.length > 0){
												value.savedListMedicationHistory = $scope.medicationHistorySavedData;
												value.tableNameMedicationHistory =  $scope.medicationHistorySavedData[0].tableName;
												angular.forEach(value.savedListMedicationHistory,function(v,k){
													v.createdDate =$scope.dt;
													v.updatedDate =$scope.dt;
												});
												value.createdDate =$scope.dt;
												value.updatedDate =$scope.dt;
											}
										}
										
										if($scope.medicalHistorySavedData.length > 0){
											$scope.rowListMedicalHistory = [];
											$scope.columnListMedicalHistory = [];
											$scope.columnHeaderMedicalHistory = [];
											$scope.savedListMedicalHistory = [];
											value.rowListMedicalHistory= [];
											value.columnListMedicalHistory= [];
											value.columnHeaderMedicalHistory= [];
											value.savedListMedicalHistory= [];
											$scope.comObj.numberOfRowMedicalHistory = $scope.medicalHistorySavedData[0].numberOfRow;
											$scope.comObj.numberOfCoulmnMedicalHistory = $scope.medicalHistorySavedData[0].numberOfCoulmn;
											
											
												for(var i = 0; i < $scope.comObj.numberOfRowMedicalHistory ;i++){
													var newArr = {};
													newArr.rowId = i;
													newArr.coulmnId = i;
													newArr.rowName = '';
													newArr.inputbox = false;
													$scope.rowListMedicalHistory.push(newArr);
											     }
												for(var i = 0; i < $scope.comObj.numberOfCoulmnMedicalHistory ;i++){
													var newArr = {};
													newArr.coulmnId = i;
													newArr.rowId = i;
													newArr.columnName = '';
													newArr.inputValue = '';
													newArr.inputbox = false;
													$scope.columnListMedicalHistory.push(newArr);
													$scope.columnHeaderMedicalHistory.push(newArr);
											     }
												
												angular.forEach($scope.rowListMedicalHistory,function(value11,key11){
													angular.forEach($scope.medicalHistorySavedData,function(value1,key1){
														if(value11.rowId == value11.rowId){
															value11.rowName = value1.rowName;
														}
													});
												});
												angular.forEach($scope.columnListMedicalHistory,function(value12,key12){
													angular.forEach($scope.medicalHistorySavedData,function(value1,key1){
														if(value12.coulmnId == value1.coulmnId){
															value12.columnName = value1.columnName;
														}
													});
												});
												angular.forEach($scope.columnHeaderMedicalHistory,function(value13,key13){
													angular.forEach($scope.medicalHistorySavedData,function(value1,key1){
														if(value13.coulmnId == value1.coulmnId){
															value13.columnName = value1.columnName;
														}
													});
												});
								
											value.rowListMedicalHistory=$scope.rowListMedicalHistory;
											value.columnListMedicalHistory=$scope.columnListMedicalHistory;
											value.columnHeaderMedicalHistory=$scope.columnHeaderMedicalHistory;
											var date = new Date();
											$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
											
											if($scope.medicalHistorySavedData != undefined && $scope.medicalHistorySavedData != null && $scope.medicalHistorySavedData.length > 0){
												value.savedListMedicalHistory = $scope.medicalHistorySavedData;
												value.tableNameMedicalHistory =  $scope.medicalHistorySavedData[0].tableName;
												angular.forEach(value.savedListMedicalHistory,function(v,k){
													v.createdDate =$scope.dt;
													v.updatedDate =$scope.dt;
												});
												value.createdDate =$scope.dt;
												value.updatedDate =$scope.dt;
											}
										}
										
										if($scope.vaccineHistorySavedData.length > 0){
											$scope.rowListVaccinationHistory = [];
											$scope.columnListVaccinationHistory = [];
											$scope.columnHeaderListVaccinationHistory = [];
											$scope.savedListVaccinationHistory = [];
											value.rowListVaccinationHistory= [];
											value.columnListVaccinationHistory= [];
											value.columnHeaderListVaccinationHistory= [];
											value.savedListVaccinationHistory= [];
											$scope.comObj.numberOfRowVaccineHistory = $scope.vaccineHistorySavedData[0].numberOfRow;
											$scope.comObj.numberOfCoulmnVaccineHistory = $scope.vaccineHistorySavedData[0].numberOfCoulmn;
											
											
												for(var i = 0; i < $scope.comObj.numberOfRowVaccineHistory ;i++){
													var newArr = {};
													newArr.rowId = i;
													newArr.coulmnId = i;
													newArr.rowName = '';
													newArr.inputbox = false;
													$scope.rowListVaccinationHistory.push(newArr);
											     }
												for(var i = 0; i < $scope.comObj.numberOfCoulmnVaccineHistory ;i++){
													var newArr = {};
													newArr.coulmnId = i;
													newArr.rowId = i;
													newArr.columnName = '';
													newArr.inputValue = '';
													newArr.inputbox = false;
													$scope.columnListVaccinationHistory.push(newArr);
													$scope.columnHeaderListVaccinationHistory.push(newArr);
											     }
												
												angular.forEach($scope.rowListVaccinationHistory,function(value11,key11){
													angular.forEach($scope.vaccineHistorySavedData,function(value1,key1){
														if(value11.rowId == value11.rowId){
															value11.rowName = value1.rowName;
														}
													});
												});
												angular.forEach($scope.columnListVaccinationHistory,function(value12,key12){
													angular.forEach($scope.vaccineHistorySavedData,function(value1,key1){
														if(value12.coulmnId == value1.coulmnId){
															value12.columnName = value1.columnName;
														}
													});
												});
												angular.forEach($scope.columnHeaderListVaccinationHistory,function(value13,key13){
													angular.forEach($scope.vaccineHistorySavedData,function(value1,key1){
														if(value13.coulmnId == value1.coulmnId){
															value13.columnName = value1.columnName;
														}
													});
												});
								
											value.rowListVaccinationHistory=$scope.rowListVaccinationHistory;
											value.columnListVaccinationHistory=$scope.columnListVaccinationHistory;
											value.columnHeaderListVaccinationHistory=$scope.columnHeaderListVaccinationHistory;
											var date = new Date();
											$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
											
											if($scope.vaccineHistorySavedData != undefined && $scope.vaccineHistorySavedData != null && $scope.vaccineHistorySavedData.length > 0){
												value.savedListVaccinationHistory = $scope.vaccineHistorySavedData;
												value.tableNameVaccinationHistory =  $scope.vaccineHistorySavedData[0].tableName;
												angular.forEach(value.savedListVaccinationHistory,function(v,k){
													v.createdDate =$scope.dt;
													v.updatedDate =$scope.dt;
												});
												value.createdDate =$scope.dt;
												value.updatedDate =$scope.dt;
											}
										}
										
										if($scope.travelHistorySavedData.length > 0){
											$scope.rowListTravelHistory = [];
											$scope.columnListTravelHistory = [];
											$scope.columnHeaderListTravelHistory = [];
											$scope.savedListTravelHistory = [];
											
											value.rowListTravelHistory= [];
											value.columnListTravelHistory= [];
											value.columnHeaderListTravelHistory= [];
											value.savedListTravelHistory= [];
											
											$scope.comObj.numberOfRowVaccineHistory = $scope.travelHistorySavedData[0].numberOfRow;
											$scope.comObj.numberOfCoulmnVaccineHistory = $scope.travelHistorySavedData[0].numberOfCoulmn;
											
											
												for(var i = 0; i < $scope.comObj.numberOfRowVaccineHistory ;i++){
													var newArr = {};
													newArr.rowId = i;
													newArr.coulmnId = i;
													newArr.rowName = '';
													newArr.inputbox = false;
													$scope.rowListTravelHistory.push(newArr);
											     }
												for(var i = 0; i < $scope.comObj.numberOfCoulmnVaccineHistory ;i++){
													var newArr = {};
													newArr.coulmnId = i;
													newArr.rowId = i;
													newArr.columnName = '';
													newArr.inputValue = '';
													newArr.inputbox = false;
													$scope.columnListTravelHistory.push(newArr);
													$scope.columnHeaderListTravelHistory.push(newArr);
											     }
												
												angular.forEach($scope.rowListTravelHistory,function(value11,key11){
													angular.forEach($scope.travelHistorySavedData,function(value1,key1){
														if(value11.rowId == value11.rowId){
															value11.rowName = value1.rowName;
														}
													});
												});
												angular.forEach($scope.columnListTravelHistory,function(value12,key12){
													angular.forEach($scope.travelHistorySavedData,function(value1,key1){
														if(value12.coulmnId == value1.coulmnId){
															value12.columnName = value1.columnName;
														}
													});
												});
												angular.forEach($scope.columnHeaderListTravelHistory,function(value13,key13){
													angular.forEach($scope.travelHistorySavedData,function(value1,key1){
														if(value13.coulmnId == value1.coulmnId){
															value13.columnName = value1.columnName;
														}
													});
												});
								
											value.rowListTravelHistory=$scope.rowListTravelHistory;
											value.columnListTravelHistory=$scope.columnListTravelHistory;
											value.columnHeaderListTravelHistory=$scope.columnHeaderListTravelHistory;
											var date = new Date();
											$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
											
											if($scope.travelHistorySavedData != undefined && $scope.travelHistorySavedData != null && $scope.travelHistorySavedData.length > 0){
												value.savedListTravelHistory = $scope.travelHistorySavedData;
												value.tableNameTravelHistory =  $scope.travelHistorySavedData[0].tableName;
												angular.forEach(value.savedListTravelHistory,function(v,k){
													v.createdDate =$scope.dt;
													v.updatedDate =$scope.dt;
												});
												value.createdDate =$scope.dt;
												value.updatedDate =$scope.dt;
											}
										}
										
										if($scope.treatmentHistorySavedData.length > 0){
											$scope.rowListTreatmentHistory = [];
											$scope.columnListTreatmentHistory = [];
											$scope.columnHeaderListTreatmentHistory = [];
											$scope.savedListTreatmentHistory = [];
											
											value.rowListTreatmentHistory= [];
											value.columnListTreatmentHistory= [];
											value.columnHeaderListTreatmentHistory= [];
											value.savedListTreatmentHistory= [];
											
											$scope.comObj.numberOfRowVaccineHistory = $scope.treatmentHistorySavedData[0].numberOfRow;
											$scope.comObj.numberOfCoulmnVaccineHistory = $scope.treatmentHistorySavedData[0].numberOfCoulmn;
											
											
												for(var i = 0; i < $scope.comObj.numberOfRowVaccineHistory ;i++){
													var newArr = {};
													newArr.rowId = i;
													newArr.coulmnId = i;
													newArr.rowName = '';
													newArr.inputbox = false;
													$scope.rowListTreatmentHistory.push(newArr);
											     }
												for(var i = 0; i < $scope.comObj.numberOfCoulmnVaccineHistory ;i++){
													var newArr = {};
													newArr.coulmnId = i;
													newArr.rowId = i;
													newArr.columnName = '';
													newArr.inputValue = '';
													newArr.inputbox = false;
													$scope.columnListTreatmentHistory.push(newArr);
													$scope.columnHeaderListTreatmentHistory.push(newArr);
											     }
												
												angular.forEach($scope.rowListTreatmentHistory,function(value11,key11){
													angular.forEach($scope.treatmentHistorySavedData,function(value1,key1){
														if(value11.rowId == value11.rowId){
															value11.rowName = value1.rowName;
														}
													});
												});
												angular.forEach($scope.columnListTreatmentHistory,function(value12,key12){
													angular.forEach($scope.treatmentHistorySavedData,function(value1,key1){
														if(value12.coulmnId == value1.coulmnId){
															value12.columnName = value1.columnName;
														}
													});
												});
												angular.forEach($scope.columnHeaderListTreatmentHistory,function(value13,key13){
													angular.forEach($scope.treatmentHistorySavedData,function(value1,key1){
														if(value13.coulmnId == value1.coulmnId){
															value13.columnName = value1.columnName;
														}
													});
												});
								
											value.rowListTreatmentHistory=$scope.rowListTreatmentHistory;
											value.columnListTreatmentHistory=$scope.columnListTreatmentHistory;
											value.columnHeaderListTreatmentHistory=$scope.columnHeaderListTreatmentHistory;
											var date = new Date();
											$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
											
											if($scope.treatmentHistorySavedData != undefined && $scope.treatmentHistorySavedData != null && $scope.treatmentHistorySavedData.length > 0){
												value.savedListTreatmentHistory = $scope.treatmentHistorySavedData;
												value.tableNameTreatmentHistory =  $scope.treatmentHistorySavedData[0].tableName;
												angular.forEach(value.savedListTreatmentHistory,function(v,k){
													v.createdDate =$scope.dt;
													v.updatedDate =$scope.dt;
												});
												value.createdDate =$scope.dt;
												value.updatedDate =$scope.dt;
											}
										}
										
										}
									
									
									
								}
								
								if(value.dischargeFieldId == 3){	
									
									$scope.vitalSavedData = [];
									$scope.generalExaminationSavedData = [];
									$scope.systemicExaminationSavedData = [];
									
									angular.forEach(value.dischargeSummaryTemplateListdata,function(v,key){
										
										if(v.tableTypeId ==11){
											$scope.vitalSavedData.push(v);
										}
										if(v.tableTypeId ==12){
											$scope.generalExaminationSavedData.push(v);
										}
										if(v.tableTypeId ==13){
											$scope.systemicExaminationSavedData.push(v);
										}
									});
									
									
									if($scope.vitalSavedData.length > 0){
									$scope.rowListVital = [];
									$scope.columnListVital = [];
									$scope.columnHeaderListVital = [];
									$scope.savedListVital = [];
									
									value.rowListVital= [];
									value.columnListVital= [];
									value.columnHeaderListVital= [];
									value.savedListVital= [];
									
									$scope.comObj.numberOfRowVital = $scope.vitalSavedData[0].numberOfRow;
									$scope.comObj.numberOfCoulmnVital = $scope.vitalSavedData[0].numberOfCoulmn;
									
									
										for(var i = 0; i < $scope.comObj.numberOfRowVital ;i++){
											var newArr = {};
											newArr.rowId = i;
											newArr.coulmnId = i;
											newArr.rowName = '';
											$scope.rowListVital.push(newArr);
									     }
										for(var i = 0; i < $scope.comObj.numberOfCoulmnVital ;i++){
											var newArr = {};
											newArr.coulmnId = i;
											newArr.rowId = i;
											newArr.columnName = '';
											newArr.inputValue = '';
											$scope.columnListVital.push(newArr);
											$scope.columnHeaderListVital.push(newArr);
									     }
										
										angular.forEach($scope.rowListVital,function(value11,key11){
											angular.forEach($scope.vitalSavedData,function(value1,key1){
												if(value11.rowId == value11.rowId){
													value11.rowName = value1.rowName;
												}
											});
										});
										angular.forEach($scope.columnListVital,function(value12,key12){
											angular.forEach($scope.vitalSavedData,function(value1,key1){
												if(value12.coulmnId == value1.coulmnId){
													value12.columnName = value1.columnName;
												}
											});
										});
										angular.forEach($scope.columnHeaderListVital,function(value13,key13){
											angular.forEach($scope.vitalSavedData,function(value1,key1){
												if(value13.coulmnId == value1.coulmnId){
													value13.columnName = value1.columnName;
												}
											});
										});
						
									value.rowListVital=$scope.rowListVital;
									value.columnListVital=$scope.columnListVital;
									value.columnHeaderListVital=$scope.columnHeaderListVital;
									var date = new Date();
									$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
									
									if($scope.vitalSavedData != undefined && $scope.vitalSavedData != null && $scope.vitalSavedData.length > 0){
										value.savedListVital = $scope.vitalSavedData;
										value.tableNameVital =  $scope.vitalSavedData[0].tableName;
										angular.forEach(value.savedListVital,function(v,k){
											v.createdDate =$scope.dt;
											v.updatedDate =$scope.dt;
										});
										value.createdDate =$scope.dt;
										value.updatedDate =$scope.dt;
									}
								}
								
								}
								
			                    if(value.dischargeFieldId == 4){
			                    	
									if(value.dischargeSummaryTemplateListdata != undefined && value.dischargeSummaryTemplateListdata != null && value.dischargeSummaryTemplateListdata.length > 0){
										$scope.comObj.numberOfRowOperativeDetails = value.dischargeSummaryTemplateListdata[0].numberOfRow;
										$scope.comObj.numberOfCoulmnOperativeDetails = value.dischargeSummaryTemplateListdata[0].numberOfCoulmn;
										value.dischargeFieldDescription = "";
										value.columnListOperativeDetails=[];
										value.columnHeaderListOperativeDetails=[];
										value.rowListOperativeDetails=[];
										value.savedListOperativeDetails = [];
										
										$scope.columnListOperativeDetails=[];
										$scope.columnHeaderListOperativeDetails=[];
										$scope.rowListOperativeDetails=[];
										$scope.savedListOperativeDetails = [];
										
											for(var i = 0; i < $scope.comObj.numberOfRowOperativeDetails ;i++){
												var newArr = {};
												newArr.rowId = i;
												newArr.coulmnId = i;
												newArr.rowName = '';
												$scope.rowListOperativeDetails.push(newArr);
										     }
											
											for(var i = 0; i < $scope.comObj.numberOfCoulmnOperativeDetails ;i++){
												var newArr = {};
												newArr.coulmnId = i;
												newArr.rowId = i;
												newArr.columnName = '';
												newArr.inputValue = '';
												$scope.columnListOperativeDetails.push(newArr);
												$scope.columnHeaderListOperativeDetails.push(newArr);
										     }
											
											angular.forEach($scope.rowListOperativeDetails,function(value11,key11){
												angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
													if(value11.rowId == value11.rowId){
														value11.rowName = value1.rowName;
													}
												});
											});
											angular.forEach($scope.columnListOperativeDetails,function(value12,key12){
												angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
													if(value12.coulmnId == value1.coulmnId){
														value12.columnName = value1.columnName;
													}
												});
											});
											angular.forEach($scope.columnHeaderListOperativeDetails,function(value13,key13){
												angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
													if(value13.coulmnId == value1.coulmnId){
														value13.columnName = value1.columnName;
													}
												});
											});
							
										value.rowListOperativeDetails=$scope.rowListOperativeDetails;
										value.columnListOperativeDetails=$scope.columnListOperativeDetails;
										value.columnHeaderListOperativeDetails=$scope.columnHeaderListOperativeDetails;
										var date = new Date();
										$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
										value.tableNameOperativeDetails = 'Operative Details';
										value.dischargeFieldDescription = '';
										if(value.dischargeSummaryTemplateListdata != undefined && value.dischargeSummaryTemplateListdata != null && value.dischargeSummaryTemplateListdata.length > 0){
											value.savedListOperativeDetails = value.dischargeSummaryTemplateListdata;
											value.tableNameOperativeDetails =  value.dischargeSummaryTemplateListdata[0].tableName;
											angular.forEach(value.savedListOperativeDetails,function(v,k){
												v.createdDate =$scope.dt;
												v.updatedDate =$scope.dt;
											});
											value.createdDate =$scope.dt;
											value.updatedDate =$scope.dt;
										}
										}
								}
								
								if(value.dischargeFieldId == 5){
									if(value.dischargeSummaryTemplateListdata != undefined && value.dischargeSummaryTemplateListdata != null && value.dischargeSummaryTemplateListdata.length > 0){
										$scope.comObj.numberOfRowMediAdmini = value.dischargeSummaryTemplateListdata[0].numberOfRow;
										$scope.comObj.numberOfCoulmnMediAdmini = value.dischargeSummaryTemplateListdata[0].numberOfCoulmn;
										value.dischargeFieldDescription = "";
										value.columnListMediAdmini=[];
										value.columnHeaderListMediAdmini=[];
										value.rowListMediAdmini=[];
										value.savedListMediAdmini = [];
										
										$scope.columnListMediAdmini=[];
										$scope.columnHeaderListMediAdmini=[];
										$scope.rowListMediAdmini=[];
										$scope.savedListMediAdmini = [];
										
											for(var i = 0; i < $scope.comObj.numberOfRowMediAdmini ;i++){
												var newArr = {};
												newArr.rowId = i;
												newArr.coulmnId = i;
												newArr.rowName = '';
												$scope.rowListMediAdmini.push(newArr);
										     }
											
											for(var i = 0; i < $scope.comObj.numberOfCoulmnMediAdmini ;i++){
												var newArr = {};
												newArr.coulmnId = i;
												newArr.rowId = i;
												newArr.columnName = '';
												newArr.inputValue = '';
												$scope.columnListMediAdmini.push(newArr);
												$scope.columnHeaderListMediAdmini.push(newArr);
										     }
											
											angular.forEach($scope.rowListMediAdmini,function(value11,key11){
												angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
													if(value11.rowId == value11.rowId){
														value11.rowName = value1.rowName;
													}
												});
											});
											angular.forEach($scope.columnListMediAdmini,function(value12,key12){
												angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
													if(value12.coulmnId == value1.coulmnId){
														value12.columnName = value1.columnName;
													}
												});
											});
											angular.forEach($scope.columnHeaderListMediAdmini,function(value13,key13){
												angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
													if(value13.coulmnId == value1.coulmnId){
														value13.columnName = value1.columnName;
													}
												});
											});
							
										value.rowListMediAdmini=$scope.rowListMediAdmini;
										value.columnListMediAdmini=$scope.columnListMediAdmini;
										value.columnHeaderListMediAdmini=$scope.columnHeaderListMediAdmini;
										var date = new Date();
										$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
										
										if(value.dischargeSummaryTemplateListdata != undefined && value.dischargeSummaryTemplateListdata != null && value.dischargeSummaryTemplateListdata.length > 0){
											value.savedListMediAdmini = value.dischargeSummaryTemplateListdata;
											value.tableNameMediAdmini =  value.dischargeSummaryTemplateListdata[0].tableName;
											angular.forEach(value.savedListMediAdmini,function(v,k){
												v.createdDate =$scope.dt;
												v.updatedDate =$scope.dt;
											});
											value.createdDate =$scope.dt;
											value.updatedDate =$scope.dt;
										}
										}
								}
								
								
								if(value.dischargeFieldId == 6){
			                    	
									if(value.dischargeSummaryTemplateListdata != undefined && value.dischargeSummaryTemplateListdata != null && value.dischargeSummaryTemplateListdata.length > 0){
										$scope.comObj.numberOfRowSurgeriesProceduresPerformed = value.dischargeSummaryTemplateListdata[0].numberOfRow;
										$scope.comObj.numberOfCoulmnSurgeriesProceduresPerformed = value.dischargeSummaryTemplateListdata[0].numberOfCoulmn;
										value.dischargeFieldDescription = "";
										value.columnListSurgeriesProceduresPerformed=[];
										value.columnHeaderListSurgeriesProceduresPerformed=[];
										value.rowListSurgeriesProceduresPerformed=[];
										value.savedListSurgeriesProceduresPerformed = [];
										
										$scope.columnListSurgeriesProceduresPerformed=[];
										$scope.columnHeaderListSurgeriesProceduresPerformed=[];
										$scope.rowListSurgeriesProceduresPerformed=[];
										$scope.savedListSurgeriesProceduresPerformed = [];
										
											for(var i = 0; i < $scope.comObj.numberOfRowSurgeriesProceduresPerformed ;i++){
												var newArr = {};
												newArr.rowId = i;
												newArr.coulmnId = i;
												newArr.rowName = '';
												$scope.rowListSurgeriesProceduresPerformed.push(newArr);
										     }
											
											for(var i = 0; i < $scope.comObj.numberOfCoulmnSurgeriesProceduresPerformed ;i++){
												var newArr = {};
												newArr.coulmnId = i;
												newArr.rowId = i;
												newArr.columnName = '';
												newArr.inputValue = '';
												$scope.columnListSurgeriesProceduresPerformed.push(newArr);
												$scope.columnHeaderListSurgeriesProceduresPerformed.push(newArr);
										     }
											
											angular.forEach($scope.rowListSurgeriesProceduresPerformed,function(value11,key11){
												angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
													if(value11.rowId == value11.rowId){
														value11.rowName = value1.rowName;
													}
												});
											});
											angular.forEach($scope.columnListSurgeriesProceduresPerformed,function(value12,key12){
												angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
													if(value12.coulmnId == value1.coulmnId){
														value12.columnName = value1.columnName;
													}
												});
											});
											angular.forEach($scope.columnHeaderListSurgeriesProceduresPerformed,function(value13,key13){
												angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
													if(value13.coulmnId == value1.coulmnId){
														value13.columnName = value1.columnName;
													}
												});
											});
							
										value.rowListSurgeriesProceduresPerformed=$scope.rowListSurgeriesProceduresPerformed;
										value.columnListSurgeriesProceduresPerformed=$scope.columnListSurgeriesProceduresPerformed;
										value.columnHeaderListSurgeriesProceduresPerformed=$scope.columnHeaderListSurgeriesProceduresPerformed;
										var date = new Date();
										$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
										value.tableNameSurgeriesProceduresPerformed = 'Surgeries/Procedures Performed';
										value.dischargeFieldDescription = '';
										if(value.dischargeSummaryTemplateListdata != undefined && value.dischargeSummaryTemplateListdata != null && value.dischargeSummaryTemplateListdata.length > 0){
											value.savedListSurgeriesProceduresPerformed = value.dischargeSummaryTemplateListdata;
											value.tableNameSurgeriesProceduresPerformed =  value.dischargeSummaryTemplateListdata[0].tableName;
											angular.forEach(value.savedListSurgeriesProceduresPerformed,function(v,k){
												v.createdDate =$scope.dt;
												v.updatedDate =$scope.dt;
											});
											value.createdDate =$scope.dt;
											value.updatedDate =$scope.dt;
										}
										}
								}
								
								
								if(value.dischargeFieldId == 7){
									if(value.dischargeSummaryTemplateListdata != undefined && value.dischargeSummaryTemplateListdata != null && value.dischargeSummaryTemplateListdata.length > 0){
										$scope.comObj.numberOfRowConsultOrder = value.dischargeSummaryTemplateListdata[0].numberOfRow;
										$scope.comObj.numberOfCoulmnConsultOrder = value.dischargeSummaryTemplateListdata[0].numberOfCoulmn;
										value.dischargeFieldDescription = "";
										value.columnListConsultOrder=[];
										value.columnHeaderListConsultOrder=[];
										value.rowListConsultOrder=[];
										value.savedListConsultOrder = [];
										
										$scope.columnListConsultOrder=[];
										$scope.columnHeaderListConsultOrder=[];
										$scope.rowListConsultOrder=[];
										$scope.savedListConsultOrder = [];
										
											for(var i = 0; i < $scope.comObj.numberOfRowConsultOrder ;i++){
												var newArr = {};
												newArr.rowId = i;
												newArr.coulmnId = i;
												newArr.rowName = '';
												$scope.rowListConsultOrder.push(newArr);
										     }
											
											for(var i = 0; i < $scope.comObj.numberOfCoulmnConsultOrder ;i++){
												var newArr = {};
												newArr.coulmnId = i;
												newArr.rowId = i;
												newArr.columnName = '';
												newArr.inputValue = '';
												$scope.columnListConsultOrder.push(newArr);
												$scope.columnHeaderListConsultOrder.push(newArr);
										     }
											
											angular.forEach($scope.rowListConsultOrder,function(value11,key11){
												angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
													if(value11.rowId == value11.rowId){
														value11.rowName = value1.rowName;
													}
												});
											});
											angular.forEach($scope.columnListConsultOrder,function(value12,key12){
												angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
													if(value12.coulmnId == value1.coulmnId){
														value12.columnName = value1.columnName;
													}
												});
											});
											angular.forEach($scope.columnHeaderListConsultOrder,function(value13,key13){
												angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
													if(value13.coulmnId == value1.coulmnId){
														value13.columnName = value1.columnName;
													}
												});
											});
							
										value.rowListConsultOrder=$scope.rowListConsultOrder;
										value.columnListConsultOrder=$scope.columnListConsultOrder;
										value.columnHeaderListConsultOrder=$scope.columnHeaderListConsultOrder;
										var date = new Date();
										$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
										
										if(value.dischargeSummaryTemplateListdata != undefined && value.dischargeSummaryTemplateListdata != null && value.dischargeSummaryTemplateListdata.length > 0){
											value.savedListConsultOrder = value.dischargeSummaryTemplateListdata;
											value.tableNameConsultOrder =  value.dischargeSummaryTemplateListdata[0].tableName;
											angular.forEach(value.savedListConsultOrder,function(v,k){
												v.createdDate =$scope.dt;
												v.updatedDate =$scope.dt;
											});
											value.createdDate =$scope.dt;
											value.updatedDate =$scope.dt;
										}
										}
								}
								
								if(value.dischargeFieldId == 8){
									if(value.dischargeSummaryTemplateListdata != undefined && value.dischargeSummaryTemplateListdata != null && value.dischargeSummaryTemplateListdata.length > 0){
										$scope.comObj.numberOfRowDiagnosis  = value.dischargeSummaryTemplateListdata[0].numberOfRow;
										$scope.comObj.numberOfCoulmnDiagnosis  = value.dischargeSummaryTemplateListdata[0].numberOfCoulmn;
										value.dischargeFieldDescription = "";
										value.columnListDiagnosis =[];
										value.columnHeaderListDiagnosis =[];
										value.rowListDiagnosis =[];
										value.savedListDiagnosis  = [];
										
										$scope.columnListDiagnosis =[];
										$scope.columnHeaderListDiagnosis =[];
										$scope.rowListDiagnosis =[];
										$scope.savedListDiagnosis  = [];
										
											for(var i = 0; i < $scope.comObj.numberOfRowDiagnosis  ;i++){
												var newArr = {};
												newArr.rowId = i;
												newArr.coulmnId = i;
												newArr.rowName = '';
												$scope.rowListDiagnosis .push(newArr);
										     }
											
											for(var i = 0; i < $scope.comObj.numberOfCoulmnDiagnosis  ;i++){
												var newArr = {};
												newArr.coulmnId = i;
												newArr.rowId = i;
												newArr.columnName = '';
												newArr.inputValue = '';
												$scope.columnListDiagnosis .push(newArr);
												$scope.columnHeaderListDiagnosis .push(newArr);
										     }
											
											angular.forEach($scope.rowListDiagnosis ,function(value11,key11){
												angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
													if(value11.rowId == value11.rowId){
														value11.rowName = value1.rowName;
													}
												});
											});
											angular.forEach($scope.columnListDiagnosis ,function(value12,key12){
												angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
													if(value12.coulmnId == value1.coulmnId){
														value12.columnName = value1.columnName;
													}
												});
											});
											angular.forEach($scope.columnHeaderListDiagnosis ,function(value13,key13){
												angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
													if(value13.coulmnId == value1.coulmnId){
														value13.columnName = value1.columnName;
													}
												});
											});
							
										value.rowListDiagnosis =$scope.rowListDiagnosis ;
										value.columnListDiagnosis =$scope.columnListDiagnosis ;
										value.columnHeaderListDiagnosis =$scope.columnHeaderListDiagnosis ;
										var date = new Date();
										$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
										
										if(value.dischargeSummaryTemplateListdata != undefined && value.dischargeSummaryTemplateListdata != null && value.dischargeSummaryTemplateListdata.length > 0){
											value.savedListDiagnosis  = value.dischargeSummaryTemplateListdata;
											value.tableNameDiagnosis =  value.dischargeSummaryTemplateListdata[0].tableName;
											angular.forEach(value.savedListDiagnosis ,function(v,k){
												v.createdDate =$scope.dt;
												v.updatedDate =$scope.dt;
											});
											value.createdDate =$scope.dt;
											value.updatedDate =$scope.dt;
										}
										}
								}
								
								if(value.dischargeFieldId == 9){
									if(value.dischargeSummaryTemplateListdata != undefined && value.dischargeSummaryTemplateListdata != null && value.dischargeSummaryTemplateListdata.length > 0){
										$scope.comObj.numberOfRowAdmisssionReason  = value.dischargeSummaryTemplateListdata[0].numberOfRow;
										$scope.comObj.numberOfCoulmnAdmisssionReason  = value.dischargeSummaryTemplateListdata[0].numberOfCoulmn;
										value.dischargeFieldDescription = "";
										value.columnListAdmisssionReason =[];
										value.columnHeaderListAdmisssionReason =[];
										value.rowListAdmisssionReason =[];
										value.savedListAdmisssionReason  = [];
										
										$scope.columnListAdmisssionReason =[];
										$scope.columnHeaderListAdmisssionReason =[];
										$scope.rowListAdmisssionReason =[];
										$scope.savedListAdmisssionReason  = [];
										
											for(var i = 0; i < $scope.comObj.numberOfRowAdmisssionReason  ;i++){
												var newArr = {};
												newArr.rowId = i;
												newArr.coulmnId = i;
												newArr.rowName = '';
												$scope.rowListAdmisssionReason .push(newArr);
										     }
											
											for(var i = 0; i < $scope.comObj.numberOfCoulmnAdmisssionReason  ;i++){
												var newArr = {};
												newArr.coulmnId = i;
												newArr.rowId = i;
												newArr.columnName = '';
												newArr.inputValue = '';
												$scope.columnListAdmisssionReason .push(newArr);
												$scope.columnHeaderListAdmisssionReason .push(newArr);
										     }
											
											angular.forEach($scope.rowListAdmisssionReason ,function(value11,key11){
												angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
													if(value11.rowId == value11.rowId){
														value11.rowName = value1.rowName;
													}
												});
											});
											angular.forEach($scope.columnListAdmisssionReason ,function(value12,key12){
												angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
													if(value12.coulmnId == value1.coulmnId){
														value12.columnName = value1.columnName;
													}
												});
											});
											angular.forEach($scope.columnHeaderListAdmisssionReason ,function(value13,key13){
												angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
													if(value13.coulmnId == value1.coulmnId){
														value13.columnName = value1.columnName;
													}
												});
											});
							
										value.rowListAdmisssionReason =$scope.rowListAdmisssionReason ;
										value.columnListAdmisssionReason =$scope.columnListAdmisssionReason ;
										value.columnHeaderListAdmisssionReason =$scope.columnHeaderListAdmisssionReason ;
										var date = new Date();
										$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
										
										if(value.dischargeSummaryTemplateListdata != undefined && value.dischargeSummaryTemplateListdata != null && value.dischargeSummaryTemplateListdata.length > 0){
											value.savedListAdmisssionReason  = value.dischargeSummaryTemplateListdata;
											value.tableNameAdmisssionReason =  value.dischargeSummaryTemplateListdata[0].tableName;
											angular.forEach(value.savedListAdmisssionReason ,function(v,k){
												v.createdDate =$scope.dt;
												v.updatedDate =$scope.dt;
											});
											value.createdDate =$scope.dt;
											value.updatedDate =$scope.dt;
										}
										}
								}
								
								if(value.dischargeFieldId == 10){	
									if(value.dischargeSummaryTemplateListdata != undefined && value.dischargeSummaryTemplateListdata != null && value.dischargeSummaryTemplateListdata.length > 0){
									$scope.investigationSavedData = [];
									$scope.treatmentHistorySavedData = [];
									angular.forEach(value.dischargeSummaryTemplateListdata,function(v,key){
										
										if(v.tableTypeId ==21){
											$scope.investigationSavedData.push(v);
										}
										if(v.tableTypeId ==22){
											$scope.treatmentHistorySavedData.push(v);
										}
									});
									
									
									if($scope.investigationSavedData.length > 0){
									$scope.rowListInvestigation = [];
									$scope.columnListInvestigation = [];
									$scope.columnHeaderListInvestigation = [];
									$scope.savedListInvestigation = [];
									value.dischargeFieldDescription = "";
									value.rowListInvestigation= [];
									value.columnListInvestigation= [];
									value.columnHeaderListInvestigation= [];
									value.savedListInvestigation= [];
									
									$scope.comObj.numberOfRowInvestigation = $scope.investigationSavedData[0].numberOfRow;
									$scope.comObj.numberOfCoulmnInvestigation = $scope.investigationSavedData[0].numberOfCoulmn;
									
									
										for(var i = 0; i < $scope.comObj.numberOfRowInvestigation ;i++){
											var newArr = {};
											newArr.rowId = i;
											newArr.coulmnId = i;
											newArr.rowName = '';
											$scope.rowListInvestigation.push(newArr);
									     }
										for(var i = 0; i < $scope.comObj.numberOfCoulmnInvestigation ;i++){
											var newArr = {};
											newArr.coulmnId = i;
											newArr.rowId = i;
											newArr.columnName = '';
											newArr.inputValue = '';
											$scope.columnListInvestigation.push(newArr);
											$scope.columnHeaderListInvestigation.push(newArr);
									     }
										
										angular.forEach($scope.rowListInvestigation,function(value11,key11){
											angular.forEach($scope.investigationSavedData,function(value1,key1){
												if(value11.rowId == value11.rowId){
													value11.rowName = value1.rowName;
												}
											});
										});
										angular.forEach($scope.columnListInvestigation,function(value12,key12){
											angular.forEach($scope.investigationSavedData,function(value1,key1){
												if(value12.coulmnId == value1.coulmnId){
													value12.columnName = value1.columnName;
												}
											});
										});
										angular.forEach($scope.columnHeaderListInvestigation,function(value13,key13){
											angular.forEach($scope.investigationSavedData,function(value1,key1){
												if(value13.coulmnId == value1.coulmnId){
													value13.columnName = value1.columnName;
												}
											});
										});
						
									value.rowListInvestigation=$scope.rowListInvestigation;
									value.columnListInvestigation=$scope.columnListInvestigation;
									value.columnHeaderListInvestigation=$scope.columnHeaderListInvestigation;
									var date = new Date();
									$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
									
									if($scope.investigationSavedData != undefined && $scope.investigationSavedData != null && $scope.investigationSavedData.length > 0){
										value.savedListInvestigation = $scope.investigationSavedData;
										value.tableNameInvestigation =  $scope.investigationSavedData[0].tableName;
										angular.forEach(value.savedListInvestigation,function(v,k){
											v.createdDate =$scope.dt;
											v.updatedDate =$scope.dt;
										});
										value.createdDate =$scope.dt;
										value.updatedDate =$scope.dt;
									}
								}
									
									if($scope.treatmentHistorySavedData.length > 0){
									$scope.rowListTreatmentHistory = [];
									$scope.columnListTreatmentHistory = [];
									$scope.columnHeaderListTreatmentHistory = [];
									$scope.savedListTreatmentHistory = [];
									
									value.rowListTreatmentHistory= [];
									value.columnListTreatmentHistory= [];
									value.columnHeaderListTravelHistory= [];
									value.savedListTreatmentHistory= [];
									
									$scope.comObj.numberOfRowVaccineHistory = $scope.treatmentHistorySavedData[0].numberOfRow;
									$scope.comObj.numberOfCoulmnVaccineHistory = $scope.treatmentHistorySavedData[0].numberOfCoulmn;
									
									
										for(var i = 0; i < $scope.comObj.numberOfRowVaccineHistory ;i++){
											var newArr = {};
											newArr.rowId = i;
											newArr.coulmnId = i;
											newArr.rowName = '';
											newArr.inputbox = false;
											$scope.rowListTreatmentHistory.push(newArr);
									     }
										for(var i = 0; i < $scope.comObj.numberOfCoulmnVaccineHistory ;i++){
											var newArr = {};
											newArr.coulmnId = i;
											newArr.rowId = i;
											newArr.columnName = '';
											newArr.inputValue = '';
											newArr.inputbox = false;
											$scope.columnListTreatmentHistory.push(newArr);
											$scope.columnHeaderListTreatmentHistory.push(newArr);
									     }
										
										angular.forEach($scope.rowListTreatmentHistory,function(value11,key11){
											angular.forEach($scope.treatmentHistorySavedData,function(value1,key1){
												if(value11.rowId == value11.rowId){
													value11.rowName = value1.rowName;
												}
											});
										});
										angular.forEach($scope.columnListTreatmentHistory,function(value12,key12){
											angular.forEach($scope.treatmentHistorySavedData,function(value1,key1){
												if(value12.coulmnId == value1.coulmnId){
													value12.columnName = value1.columnName;
												}
											});
										});
										angular.forEach($scope.columnHeaderListTreatmentHistory,function(value13,key13){
											angular.forEach($scope.treatmentHistorySavedData,function(value1,key1){
												if(value13.coulmnId == value1.coulmnId){
													value13.columnName = value1.columnName;
												}
											});
										});
						
									value.rowListTreatmentHistory=$scope.rowListTreatmentHistory;
									value.columnListTreatmentHistory=$scope.columnListTreatmentHistory;
									value.columnHeaderListTreatmentHistory=$scope.columnHeaderListTreatmentHistory;
									var date = new Date();
									$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
									
									if($scope.treatmentHistorySavedData != undefined && $scope.treatmentHistorySavedData != null && $scope.treatmentHistorySavedData.length > 0){
										value.savedListTreatmentHistory = $scope.treatmentHistorySavedData;
										value.tableNameTreatmentHistory =  $scope.treatmentHistorySavedData[0].tableName;
										angular.forEach(value.savedListTreatmentHistory,function(v,k){
											v.createdDate =$scope.dt;
											v.updatedDate =$scope.dt;
										});
										value.createdDate =$scope.dt;
										value.updatedDate =$scope.dt;
									}
								}
									}
								
								}
								
								if(value.dischargeFieldId == 11){
									if(value.dischargeSummaryTemplateListdata != undefined && value.dischargeSummaryTemplateListdata != null && value.dischargeSummaryTemplateListdata.length > 0){
										$scope.comObj.numberOfRowTransferOfCare  = value.dischargeSummaryTemplateListdata[0].numberOfRow;
										$scope.comObj.numberOfCoulmnTransferOfCare  = value.dischargeSummaryTemplateListdata[0].numberOfCoulmn;
										
										value.columnListTransferOfCare =[];
										value.columnHeaderListTransferOfCare =[];
										value.rowListTransferOfCare =[];
										value.savedListTransferOfCare  = [];
										value.dischargeFieldDescription = "";
										$scope.columnListTransferOfCare =[];
										$scope.columnHeaderListTransferOfCare =[];
										$scope.rowListTransferOfCare =[];
										$scope.savedListTransferOfCare  = [];
										
											for(var i = 0; i < $scope.comObj.numberOfRowTransferOfCare  ;i++){
												var newArr = {};
												newArr.rowId = i;
												newArr.coulmnId = i;
												newArr.rowName = '';
												$scope.rowListTransferOfCare .push(newArr);
										     }
											
											for(var i = 0; i < $scope.comObj.numberOfCoulmnTransferOfCare  ;i++){
												var newArr = {};
												newArr.coulmnId = i;
												newArr.rowId = i;
												newArr.columnName = '';
												newArr.inputValue = '';
												$scope.columnListTransferOfCare .push(newArr);
												$scope.columnHeaderListTransferOfCare .push(newArr);
										     }
											
											angular.forEach($scope.rowListTransferOfCare ,function(value11,key11){
												angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
													if(value11.rowId == value11.rowId){
														value11.rowName = value1.rowName;
													}
												});
											});
											angular.forEach($scope.columnListTransferOfCare ,function(value12,key12){
												angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
													if(value12.coulmnId == value1.coulmnId){
														value12.columnName = value1.columnName;
													}
												});
											});
											angular.forEach($scope.columnHeaderListTransferOfCare ,function(value13,key13){
												angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
													if(value13.coulmnId == value1.coulmnId){
														value13.columnName = value1.columnName;
													}
												});
											});
							
										value.rowListTransferOfCare =$scope.rowListTransferOfCare ;
										value.columnListTransferOfCare =$scope.columnListTransferOfCare ;
										value.columnHeaderListTransferOfCare =$scope.columnHeaderListTransferOfCare ;
										var date = new Date();
										$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
										
										if(value.dischargeSummaryTemplateListdata != undefined && value.dischargeSummaryTemplateListdata != null && value.dischargeSummaryTemplateListdata.length > 0){
											value.savedListTransferOfCare  = value.dischargeSummaryTemplateListdata;
											value.tableNameTransferOfCare =  value.dischargeSummaryTemplateListdata[0].tableName;
											angular.forEach(value.savedListTransferOfCare ,function(v,k){
												v.createdDate =$scope.dt;
												v.updatedDate =$scope.dt;
											});
											value.createdDate =$scope.dt;
											value.updatedDate =$scope.dt;
										}
										}
								}
								
					               if(value.dischargeFieldId == 12){
				                    	
										if(value.dischargeSummaryTemplateListdata != undefined && value.dischargeSummaryTemplateListdata != null && value.dischargeSummaryTemplateListdata.length > 0){
											$scope.comObj.numberOfRowDischargeMedication = value.dischargeSummaryTemplateListdata[0].numberOfRow;
											$scope.comObj.numberOfCoulmnDischargeMedication = value.dischargeSummaryTemplateListdata[0].numberOfCoulmn;
											
											value.columnListDischargeMedication=[];
											value.columnHeaderListDischargeMedication=[];
											value.rowListDischargeMedication=[];
											value.savedListDischargeMedication = [];
											value.dischargeFieldDescription = "";
											$scope.columnListDischargeMedication=[];
											$scope.columnHeaderListDischargeMedication=[];
											$scope.rowListDischargeMedication=[];
											$scope.savedListDischargeMedication = [];
											
												for(var i = 0; i < $scope.comObj.numberOfRowDischargeMedication ;i++){
													var newArr = {};
													newArr.rowId = i;
													newArr.coulmnId = i;
													newArr.rowName = '';
													$scope.rowListDischargeMedication.push(newArr);
											     }
												
												for(var i = 0; i < $scope.comObj.numberOfCoulmnDischargeMedication ;i++){
													var newArr = {};
													newArr.coulmnId = i;
													newArr.rowId = i;
													newArr.columnName = '';
													newArr.inputValue = '';
													$scope.columnListDischargeMedication.push(newArr);
													$scope.columnHeaderListDischargeMedication.push(newArr);
											     }
												
												angular.forEach($scope.rowListDischargeMedication,function(value11,key11){
													angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
														if(value11.rowId == value11.rowId){
															value11.rowName = value1.rowName;
														}
													});
												});
												angular.forEach($scope.columnListDischargeMedication,function(value12,key12){
													angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
														if(value12.coulmnId == value1.coulmnId){
															value12.columnName = value1.columnName;
														}
													});
												});
												angular.forEach($scope.columnHeaderListDischargeMedication,function(value13,key13){
													angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
														if(value13.coulmnId == value1.coulmnId){
															value13.columnName = value1.columnName;
														}
													});
												});
								
											value.rowListDischargeMedication=$scope.rowListDischargeMedication;
											value.columnListDischargeMedication=$scope.columnListDischargeMedication;
											value.columnHeaderListDischargeMedication=$scope.columnHeaderListDischargeMedication;
											var date = new Date();
											$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
											value.tableNameDischargeMedication = 'Discharge Medications';
											value.dischargeFieldDescription = '';
											if(value.dischargeSummaryTemplateListdata != undefined && value.dischargeSummaryTemplateListdata != null && value.dischargeSummaryTemplateListdata.length > 0){
												value.savedListDischargeMedication = value.dischargeSummaryTemplateListdata;
												value.tableNameDischargeMedication =  value.dischargeSummaryTemplateListdata[0].tableName;
												angular.forEach(value.savedListDischargeMedication,function(v,k){
													v.createdDate =$scope.dt;
													v.updatedDate =$scope.dt;
												});
												value.createdDate =$scope.dt;
												value.updatedDate =$scope.dt;
											}
											}
									}
									
					               
								if(value.dischargeFieldId == 109){
									if(value.dischargeSummaryTemplateListdata != undefined && value.dischargeSummaryTemplateListdata != null && value.dischargeSummaryTemplateListdata.length > 0){
										$scope.comObj.numberOfRowObservation  = value.dischargeSummaryTemplateListdata[0].numberOfRow;
										$scope.comObj.numberOfCoulmnObservation  = value.dischargeSummaryTemplateListdata[0].numberOfCoulmn;
										
										value.columnListObservation =[];
										value.columnHeaderListObservation =[];
										value.rowListObservation =[];
										value.savedListObservation  = [];
										value.dischargeFieldDescription = "";
										$scope.columnListObservation =[];
										$scope.columnHeaderListObservation =[];
										$scope.rowListObservation =[];
										$scope.savedListObservation  = [];
										
											for(var i = 0; i < $scope.comObj.numberOfRowObservation  ;i++){
												var newArr = {};
												newArr.rowId = i;
												newArr.coulmnId = i;
												newArr.rowName = '';
												$scope.rowListObservation .push(newArr);
										     }
											
											for(var i = 0; i < $scope.comObj.numberOfCoulmnObservation  ;i++){
												var newArr = {};
												newArr.coulmnId = i;
												newArr.rowId = i;
												newArr.columnName = '';
												newArr.inputValue = '';
												$scope.columnListObservation .push(newArr);
												$scope.columnHeaderListObservation .push(newArr);
										     }
											
											angular.forEach($scope.rowListObservation ,function(value11,key11){
												angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
													if(value11.rowId == value11.rowId){
														value11.rowName = value1.rowName;
													}
												});
											});
											angular.forEach($scope.columnListObservation ,function(value12,key12){
												angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
													if(value12.coulmnId == value1.coulmnId){
														value12.columnName = value1.columnName;
													}
												});
											});
											angular.forEach($scope.columnHeaderListObservation ,function(value13,key13){
												angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
													if(value13.coulmnId == value1.coulmnId){
														value13.columnName = value1.columnName;
													}
												});
											});
							
										value.rowListObservation =$scope.rowListObservation ;
										value.columnListObservation =$scope.columnListObservation ;
										value.columnHeaderListObservation =$scope.columnHeaderListObservation ;
										var date = new Date();
										$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
										
										if(value.dischargeSummaryTemplateListdata != undefined && value.dischargeSummaryTemplateListdata != null && value.dischargeSummaryTemplateListdata.length > 0){
											value.savedListObservation  = value.dischargeSummaryTemplateListdata;
											value.tabelNameObservation =  value.dischargeSummaryTemplateListdata[0].tableName;
											angular.forEach(value.savedListObservation ,function(v,k){
												v.createdDate =$scope.dt;
												v.updatedDate =$scope.dt;
											});
											value.createdDate =$scope.dt;
											value.updatedDate =$scope.dt;
										}
										}
								}
								
								if(value.dischargeFieldId == 13){
									if(value.dischargeSummaryTemplateListdata != undefined && value.dischargeSummaryTemplateListdata != null && value.dischargeSummaryTemplateListdata.length > 0){
										$scope.comObj.numberOfRow = value.dischargeSummaryTemplateListdata[0].numberOfRow;
										$scope.comObj.numberOfCoulmn = value.dischargeSummaryTemplateListdata[0].numberOfCoulmn;
										
											for(var i = 0; i < $scope.comObj.numberOfRow ;i++){
												var newArr = {};
												newArr.rowId = i;
												newArr.coulmnId = i;
												newArr.rowName = '';
												newArr.inputbox = false;
												$scope.rowList.push(newArr);
										     }
											console.log("$scope.rowList",$scope.rowList);
											
											for(var i = 0; i < $scope.comObj.numberOfCoulmn ;i++){
												var newArr = {};
												newArr.coulmnId = i;
												newArr.rowId = i;
												newArr.columnName = '';
												newArr.inputValue = '';
												newArr.inputbox = false;
												$scope.columnList.push(newArr);
												$scope.columnHeaderList.push(newArr);
										     }
											console.log("$scope.columnList",$scope.columnList);
											$scope.tableName = response.data.listObject[0].tableName;
											
											angular.forEach($scope.rowList,function(value11,key11){
												angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
													if(value11.rowId == value11.rowId){
														value11.rowName = value1.rowName;
													}
												});
											});
											angular.forEach($scope.columnList,function(value12,key12){
												angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
													if(value12.coulmnId == value1.coulmnId){
														value12.columnName = value1.columnName;
													}
												});
											});
											angular.forEach($scope.columnHeaderList,function(value13,key13){
												angular.forEach(value.dischargeSummaryTemplateListdata,function(value1,key1){
													if(value13.coulmnId == value1.coulmnId){
														value13.columnName = value1.columnName;
													}
												});
											});
							
										value.rowList=$scope.rowList;
										value.columnList=$scope.columnList;
										value.columnHeaderList=$scope.columnHeaderList;
										value.dischargeFieldDescription = "";
										var date = new Date();
										$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
										
										if(value.dischargeSummaryTemplateListdata != undefined && value.dischargeSummaryTemplateListdata != null && value.dischargeSummaryTemplateListdata.length > 0){
											value.savedList = value.dischargeSummaryTemplateListdata;
											value.tableNameComplaint =  value.dischargeSummaryTemplateListdata[0].tableName;
											angular.forEach(value.savedList,function(v,k){
												v.createdDate =$scope.dt;
												v.updatedDate =$scope.dt;
											});
											value.createdDate =$scope.dt;
											value.updatedDate =$scope.dt;
										}
										}
								}
						
								
						if(value.dischargeFieldId == 18){	
							if(value.dischargeSummaryTemplateListdata != undefined && value.dischargeSummaryTemplateListdata != null && value.dischargeSummaryTemplateListdata.length > 0){
									$scope.instructionSavedData = [];
									$scope.followUpSavedData = [];
									
									angular.forEach(value.dischargeSummaryTemplateListdata,function(v,key){
										if(v.tableTypeId ==26){
											$scope.instructionSavedData.push(v);
										}
										if(v.tableTypeId ==27){
											$scope.followUpSavedData.push(v);
										}
									});
									
									value.dischargeFieldDescription = "";
									if($scope.instructionSavedData.length > 0){
									$scope.rowListInstructions = [];
									$scope.columnListInstructions = [];
									$scope.columnHeaderListInstructions = [];
									$scope.savedListInstructions = [];
									
									value.rowListInstructions= [];
									value.columnListInstructions= [];
									value.columnHeaderListInstructions= [];
									value.savedListInstructions= [];
									
									$scope.comObj.numberOfRowInstructions = $scope.instructionSavedData[0].numberOfRow;
									$scope.comObj.numberOfCoulmnInstructions = $scope.instructionSavedData[0].numberOfCoulmn;
									
									
										for(var i = 0; i < $scope.comObj.numberOfRowInstructions ;i++){
											var newArr = {};
											newArr.rowId = i;
											newArr.coulmnId = i;
											newArr.rowName = '';
											$scope.rowListInstructions.push(newArr);
									     }
										for(var i = 0; i < $scope.comObj.numberOfCoulmnInstructions ;i++){
											var newArr = {};
											newArr.coulmnId = i;
											newArr.rowId = i;
											newArr.columnName = '';
											newArr.inputValue = '';
											$scope.columnListInstructions.push(newArr);
											$scope.columnHeaderListInstructions.push(newArr);
									     }
										
										angular.forEach($scope.rowListInstructions,function(value11,key11){
											angular.forEach($scope.instructionSavedData,function(value1,key1){
												if(value11.rowId == value11.rowId){
													value11.rowName = value1.rowName;
												}
											});
										});
										angular.forEach($scope.columnListInstructions,function(value12,key12){
											angular.forEach($scope.instructionSavedData,function(value1,key1){
												if(value12.coulmnId == value1.coulmnId){
													value12.columnName = value1.columnName;
												}
											});
										});
										angular.forEach($scope.columnHeaderListInstructions,function(value13,key13){
											angular.forEach($scope.instructionSavedData,function(value1,key1){
												if(value13.coulmnId == value1.coulmnId){
													value13.columnName = value1.columnName;
												}
											});
										});
						
									value.rowListInstructions=$scope.rowListInstructions;
									value.columnListInstructions=$scope.columnListInstructions;
									value.columnHeaderListInstructions=$scope.columnHeaderListInstructions;
									var date = new Date();
									$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
									value.tabelNameInstructions = 'Advice';
									value.dischargeFieldDescription = '';
									if($scope.instructionSavedData != undefined && $scope.instructionSavedData != null && $scope.instructionSavedData.length > 0){
										value.savedListInstructions = $scope.instructionSavedData;
										value.tabelNameInstructions =  $scope.instructionSavedData[0].tableName;
										angular.forEach(value.savedListInstructions,function(v,k){
											v.createdDate =$scope.dt;
											v.updatedDate =$scope.dt;
										});
										value.createdDate =$scope.dt;
										value.updatedDate =$scope.dt;
									}
								}
									
									if($scope.followUpSavedData.length > 0){
										$scope.rowListfollowUp = [];
										$scope.columnListfollowUp = [];
										$scope.columnHeaderListfollowUp = [];
										$scope.savedListfollowUp = [];
										
										value.rowListfollowUp= [];
										value.columnListfollowUp= [];
										value.columnHeaderListfollowUp= [];
										value.savedListfollowUp= [];
										
										$scope.comObj.numberOfRowfollowUp = $scope.followUpSavedData[0].numberOfRow;
										$scope.comObj.numberOfCoulmnfollowUp = $scope.followUpSavedData[0].numberOfCoulmn;
										
										
											for(var i = 0; i < $scope.comObj.numberOfRowfollowUp ;i++){
												var newArr = {};
												newArr.rowId = i;
												newArr.coulmnId = i;
												newArr.rowName = '';
												$scope.rowListfollowUp.push(newArr);
										     }
											for(var i = 0; i < $scope.comObj.numberOfCoulmnfollowUp ;i++){
												var newArr = {};
												newArr.coulmnId = i;
												newArr.rowId = i;
												newArr.columnName = '';
												newArr.inputValue = '';
												$scope.columnListfollowUp.push(newArr);
												$scope.columnHeaderListfollowUp.push(newArr);
										     }
											
											angular.forEach($scope.rowListfollowUp,function(value11,key11){
												angular.forEach($scope.followUpSavedData,function(value1,key1){
													if(value11.rowId == value11.rowId){
														value11.rowName = value1.rowName;
													}
												});
											});
											angular.forEach($scope.columnListfollowUp,function(value12,key12){
												angular.forEach($scope.followUpSavedData,function(value1,key1){
													if(value12.coulmnId == value1.coulmnId){
														value12.columnName = value1.columnName;
													}
												});
											});
											angular.forEach($scope.columnHeaderListfollowUp,function(value13,key13){
												angular.forEach($scope.followUpSavedData,function(value1,key1){
													if(value13.coulmnId == value1.coulmnId){
														value13.columnName = value1.columnName;
													}
												});
											});
							
										value.rowListfollowUp=$scope.rowListfollowUp;
										value.columnListfollowUp=$scope.columnListfollowUp;
										value.columnHeaderListfollowUp=$scope.columnHeaderListfollowUp;
										var date = new Date();
										$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
										value.tabelNamefollowUp = 'Follow up';
										value.dischargeFieldDescription = '';
										if($scope.followUpSavedData != undefined && $scope.followUpSavedData != null && $scope.followUpSavedData.length > 0){
											value.savedListfollowUp = $scope.followUpSavedData;
											value.tabelNamefollowUp =  $scope.followUpSavedData[0].tableName;
											angular.forEach(value.savedListfollowUp,function(v,k){
												v.createdDate =$scope.dt;
												v.updatedDate =$scope.dt;
											});
											value.createdDate =$scope.dt;
											value.updatedDate =$scope.dt;
										}
									}
							     }
								}
							});
						
						console.log($scope.DischargeSummaryFinalList);
						
			                         });
			         },500);
						
						}
					$rootScope.stopLoader();
					
				});
			
			});
			
			}
			
			$scope.getFieldsByTemplateId($scope.detailObj.templateId);
			
			$scope.blankTempalteList = function(){
				$state.go('dischargedPatients');
				return;
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
			$scope.treatmentHistoryList = [];
			
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
				
				//treatmentHistory
				if(historyScreenId==7){
					if(info.treamentHistoryCurrentChk == 1){
						$scope.treatmentHistoryList.push(info);
						
					}else{
						
						$scope.treatmentHistoryList.splice(info,1);
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
			$scope.treatmentHistoryList1=[];
			$scope.addInvestigation=function(info,idx,tabId){
				if(tabId==0){
					if(info.investigationChk== 1){
						info.orderId=info.orderId+"-"+idx;
						$scope.addedInvestigationList.push(info);
						
					}else{
						$scope.addedInvestigationList.splice(info,1);
					}	
				}
				
				if(tabId==1){
					if(info.treamentHistoryCurrentChk1 == 1){
						$scope.treatmentHistoryList1.push(info);
						
					}else{
						
						$scope.treatmentHistoryList1.splice(info,1);
					}
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
			
			//add all vital data
			$scope.allChkSel=0;
			$scope.addAllVitals=function(){
				 $scope.allChkSel=$("#allVitalChk").prop("checked")==true?1:0;
				if($scope.allChkSel== 1){
					angular.forEach($scope.patientViatalList,function(value,index){
						if(value.value!=0){
						value.vitalChk=1;
						$scope.addedVitalList.push(value);
						}
					});
				}else{
					angular.forEach($scope.patientViatalList,function(value,index){
						value.vitalChk=0;
					});
					$scope.addedVitalList=[];
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
			
			
			//Advice
			$scope.addedInstructions=[];
			$scope.addInstructions=function(info,idx){
				if(info.instructionChk== 1){
					$scope.addedInstructions.push(info);
				}else{
					$scope.addedInstructions.splice(info,1);
				}
			}
			
			$scope.addedFollowupAppointment=[];
			$scope.addFollowUpApp=function(info,idx){
				if(info.followUpChk== 1){
					$scope.addedFollowupAppointment.push(info);
				}else{
					$scope.addedFollowupAppointment.splice(info,1);
				}
			}
			
			$scope.allObservationChkSel=0;
			$scope.addedObservation = [];
			$scope.addAllObservation=function(info,idx){
				if($scope.chkFlag == false){
					$scope.addedObservation=[];
					$scope.chkFlag=true;
				}
				if(info.observationChk== 1){
					$scope.addedObservation.push(info);
				}else{
					$scope.addedObservation.splice(info,1);
				}
			}
			
			
			//Add complainst
			$scope.addedComplaintList=[];
			$scope.addComplaints=function(info,idx){
				if(info.complaintChk== 1){
					$scope.addedComplaintList.push(info);
				}else{
					$scope.addedComplaintList.splice(info,1);
				}
			}
			
			$scope.addedHabitList=[];
			$scope.addHabits=function(info,idx){
				if(info.habitChk== 1){
					$scope.addedHabitList.push(info);
				}else{
					$scope.addedHabitList.splice(info,1);
				}
			}
			
			$scope.addData=function(){
				
				
            $scope.dischargeSummaryDataViewOptionIdTest = $scope.dischargeSummaryDataViewOptionId;
				
				if($scope.dischargeSummaryDataViewOptionIdTest ==1){
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
						if(value.isNoKnownAllergies=='Y'){
							allergyData += "<tr><td>No Allergy</td><td>"+((value.allergyId!=null)?value.allergyName:value.genericName)+"</td><td>" +
							""+value.severityDesc+"</td><td>"+value.natureOfReaction+"</td><td>"+value.type+"</td><td>"+value.remark+"</td></tr> ";
						}else{
							allergyData += "<tr><td>"+value.allergyTypeName+"</td><td>"+((value.allergyId!=null)?value.allergyName:value.genericName)+"</td><td>" +
							""+value.severityDesc+"</td><td>"+value.natureOfReaction+"</td><td>"+value.type+"</td><td>"+value.remark+"</td></tr> ";	
						}
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
							"  <th width='12%'>Relation</th> <th width='14%'>Date</th> <th width='20%'>Remark</th> </tr>";
					
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
							surgicalHistoryData += "<tr><td>"+ ((value.surgeryNameFormulary != null ) ? value.surgeryNameFormulary : value.surgeryName)+"</td><td> "+value.surgenName+"</td><td>"+((value.month!=null)?$scope.months[value.month].text:"")+"</td><td>"+value.year+"</td><td>" +
											""+value.remark+"</td></tr>";
						}else{
						surgicalHistoryData += "<tr><td>No Significant Surgical History </td><td></td> <td></td> <td></td> <td></td></tr>";
						}
					});
					surgicalHistoryData +="</table>";
					var surgicalHistoryDataNew=surgicalHistoryData.replace(/null/g," ");
					//$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription=surgicalHistoryDataNew;
					
					//for medication history
					
					var medicationHistoryData =  "<table><tr bgcolor='#2e6ea2' color='#FFFFFF'><th width='14%'>Brand Name</th><th width='15%'>Generic Name</th> <th width='8%'>Dose</th><th width='14%'>Frequency</th>" +
					"<th width='8%'>ROA</th><th width='6%'>Qty</th> <th width='15%'>Start Date </th><th width='15%'>End Date</th>  <th width='11%'>Instruction</th></tr>";
					angular.forEach($scope.medicationHistoryList,function(value,key){
						var doseDesc="";
						if(value.doseId==null){
							doseDesc=value.doseDesc+" "+value.strengthDesc;
						}else{
							doseDesc=value.doseId+" "+value.doseUnit;
						}
						if(value.instructionId == null){
							instructionDesc=value.instructionText;
						}else{
							instructionDesc=value.instructionDesc;
						}
						
						if(value.isNoSignificantMedication == 'Y'){
							value.itemName="No Significant Medication History";
						}
						medicationHistoryData += "<tr><td>"+value.itemName+"</td><td> "+value.genericName+" </td><td> "+doseDesc+"</td><td>" +
								"  "+value.frequencyDesc+"</td><td> "+value.roaDesc+" </td><td>"+value.orderQty+"</td><td >"+value.startDate+"</td><td>"+value.endDate+"</td><td>  "+instructionDesc+"</td></tr>";
					});
					medicationHistoryData +="</table>";
					var medicationHistoryDataNew=medicationHistoryData.replace(/null/g," ");
					//$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription=medicationHistoryDataNew;
					
					
					//Medical History
					var medicalHistoryData =  "<table><tr bgcolor='#2e6ea2' color='#FFFFFF'><th width='30%'>ICD 10 Description</th> <th width='20%'>ICD Code</th> " +
							"<th width='20%'>Since</th> <th width='30%'>Remark</th></tr>";
					angular.forEach($scope.medicalHistoryList,function(value,key){
						if(value.isNoSignificantStatus=='N' || value.isNoSignificantStatus==undefined){
							var since="";
							if(value.since==undefined)
								since=value.resolvedDays+" Day(s)";
							else
								since=value.since;
						   medicalHistoryData += "<tr><td>"+value.diagnosisName+"</td><td> "+value.code+"</td><td>"+since+"</td><td>"+value.remark+"</td></tr>";
						}else{
							medicalHistoryData += "<tr><td>No Significant Medical History</td><td> "+value.code+"</td><td>"+value.since+"</td><td>"+value.remark+"</td></tr>";
						}
					});
					medicalHistoryData +="</table>";
					var medicalHistoryDataNew=medicalHistoryData.replace(/null/g," ");
					//$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription=medicalHistoryDataNew;
					
					//immunization history list
					var immunizationHistoryData =  "<table><tr bgcolor='#2e6ea2' color='#FFFFFF'><th width='10%'>Age</th> <th width='15%'>Generic Name</th> <th width='10%'>Series</th> <th width='12%'>Planned Date</th> <th width='15%'>Actual Administration Date</th> <th width='15%'>Location</th><th width='20%'>Remark</th></tr>";
					angular.forEach($scope.immunizationHistoryList,function(value,key){
						immunizationHistoryData += "<tr><td>"+value.minAge+" "+value.minDay+"</td><td>"+value.genericName+"</td><td>"+value.abbreviation+" "+value.series+"</td><td>"+value.calculatedDateShow+"</td><td>"+value.plannedDate+"</td><td>"+value.location+"</td><td>"+value.remarks+"</td></tr>";
					});
					immunizationHistoryData +="</table>";
					var immunizationHistoryDataNew=immunizationHistoryData.replace(/null/g," ");
					//$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription=immunizationHistoryDataNew;
					
					//Travel List
					var travelHistoryData =  "<table><tr bgcolor='#2e6ea2' color='#FFFFFF'><th width='80%'>Remark</th> <th width='20%'>When</th></tr>";
					angular.forEach($scope.travelHistoryList,function(value,key){
						if(value.isNoSignificantTravel == 'Y'){
							value.description="No Significant Travel History";
						}
						travelHistoryData += "<tr><td>"+value.description+"</td><td>"+value.whenTravel+"</td></tr>";
					});
					travelHistoryData +="</table>";
					var travelHistoryDataNew=travelHistoryData.replace(/null/g," ");
					
					//Treatment history List
					var treatmentHistoryData =  "<table><tr style='padding-top:100px;'><th width='15%'>Date</th><th width='27%'>Treatment / Investigations</th> " +
					"<th width='28%'>Treatment Details / Impression</th></tr>";
					angular.forEach($scope.treatmentHistoryList,function(value,key){
						if(value.isNoSignificantStatus == 'Y'){
							value.treatementName="No Significant Treatment/Investigation History";
						}
						treatmentHistoryData += "<tr><td>"+value.treatDate+"</td><td>"+value.treatementName+"</td><td>"+value.remark+"</td></tr>";
					});
					treatmentHistoryData +="</table>";
					var treatmentHistoryDataNew=treatmentHistoryData.replace(/null/g," ");
					
//					//Habits history data
//					var habitData =  "<table><tr style='padding-top:100px;'><th width='10%'>Type</th><th width='15%'>Status</th> " +
//					"<th width='40%'>Details</th><th width='30%'>Remark</th></tr>";
//					angular.forEach($scope.addedHabitList,function(value,key){
//						habitData += "<tr><td>"+value.treatDate+"</td><td>"+value.treatementName+"</td><td>"+value.remark+"</td></tr>";
//					});
//					treatmentHistoryData +="</table>";
//					var treatmentHistoryDataNew=treatmentHistoryData.replace(/null/g," ");
//					
					
					var finalData="";
					if($scope.personalHistoryList.length > 0){
						finalData+="<br><B>Personal History</B>"+personalHistoryDataNew;
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
						finalData=finalData+"<B>Vaccination History</B>"+immunizationHistoryDataNew;
					}
					if($scope.travelHistoryList.length > 0){
						finalData=finalData+"<B>Travel History</B>"+travelHistoryDataNew;
					}
					if($scope.treatmentHistoryList.length > 0){
						finalData=finalData+"<B>Treatment / Investigations</B>"+treatmentHistoryDataNew;
					}
					if($scope.patientHabitList.length>0){
						var habitData=$('#lifestylehabits').html();
						finalData=finalData+"<B>Lifestyle History</B>"+habitData;	
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
					var dischargeMedicationData = "<table><tr bgcolor='#2e6ea2' color='#FFFFFF'><th width='20%'>Brand Name</th> <th width='16%'>Generic Name</th>" +
							"<th width='6%'>Dose</th><th width='10%'>Frequency</th>" +
							" <th width='8%'>Duration</th><th width='10%'>ROA</th>" +
					"  <th width='4%'>Qty</th> <th style='width:10%;'>Instruction</th></tr>";/*<th width='10%'>Start Date </th><th width='10%'>End Date</th>*/
					
					
					angular.forEach($scope.addedDischargeMedicationlist,function(value,key){
						if(value.instructionDesc != null){
							value.instructionDesc = value.instructionDesc;
						}else{
							value.instructionDesc = value.instructionText;
						}
						
						dischargeMedicationData += "<tr><td>"+value.itemName+"</td><td>"+value.genericName+"</td><td>"+value.doseDesc +' '+ value.strengthDesc+"</td><td>"+value.frequencyDesc+"" +
						"</td><td>"+value.duration+"</td><td>"+value.roa_details+"</td><td>"+value.quantity+"</td><td>"+value.instructionDesc+"</td></tr>";
					});/*<td>"+value.startDate+"</td><td>"+value.endDate+"</td>*/
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
						diagnosisData += "<tr><td>"+value.code+"</td><td>"+value.diagnosisName+" </td><td> "+(value.type=='1' ?"Provisional":"Final")+","+(value.diagnosisType==1?"Primary":"Secondary")+"</td><td> "+(value.isNotifiable=='Y' ?"Notifiable":" ")+(value.isComorbidity == 'Y' ?"Comorbidity":" ")+"</td><td> "+value.remark+" </td></tr>";
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
					var medicationData = "<table><tr bgcolor='#2e6ea2' color='#FFFFFF'><th width='16%'>Brand Name</th><th width='16%'>Generic Name</th> <th width='7%'>Dose</th><th width='10%'>Frequency</th>" +
							"<th width='8%'>ROA</th><th width='15%'>Start Date </th><th width='15%'>End Date</th><th style='width:12%;'>Instruction</th></tr>";
					angular.forEach($scope.addedMedicationList,function(value,key){
						if(value.instructionDesc != null){
							value.instructionDesc = value.instructionDesc;
						}else{
							value.instructionDesc = value.instructionText;
						}
						 var strength = (value.strengthDesc != undefined && value.strengthDesc != null) ? value.strengthDesc:'';
							if(value.doseId==null){
								value.dose=value.doseDesc +" "+ strength;
							}else{
								value.dose=value.doseId+" "+value.doseUnit;
							}
						medicationData += "<tr><td>"+value.itemName+"</td><td>"+value.genericName+"</td><td>"+value.dose +
								"</td><td>"+value.frequencyDesc+"</td><td>"+value.roa_details+"</td><td>"+value.startDate+"</td><td>"+value.endDate+"</td><td>"+value.instructionDesc+"</td></tr>";
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
					//if($scope.addedInvestigationList.length>0){
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
					
					var investigationData ="<table  ord-det-ids='"+ordDetIds+"' ><tr bgcolor='#2e6ea2' color='#FFFFFF'><th width='20%'>Created Date</th><th width='60%'>Investigation</th> <th width='20%'>Status</th></tr>";
					angular.forEach($scope.addedInvestigationList,function(value,key){
						investigationData += "<tr><td>"+value.createdDate+"</td><td>"+value.serviceStandardName+"</td><td>"+((value.statusName==null)?((value.serviceRendered==1)?"Completed":"Pending"):value.statusName)+"</td></tr>";
					});
					investigationData+="</table>";
					var investigationDataNew=investigationData.replace(/null/g," ");
					
					//Treatment history List
					var treatmentHistoryData =  "<table><tr style='padding-top:100px;'><th width='15%'>Date</th><th width='27%'>Treatment / Investigations</th> " +
					"<th width='28%'>Treatment Details / Impression</th></tr>";
					angular.forEach($scope.treatmentHistoryList1,function(value,key){
						treatmentHistoryData += "<tr><td>"+value.treatDate+"</td><td>"+value.treatementName+"</td><td>"+value.remark+"</td></tr>";
					});
					treatmentHistoryData +="</table>";
					var treatmentHistoryDataNew=treatmentHistoryData.replace(/null/g," ");
					
					var finalTabData="";
					if($scope.addedInvestigationList.length>0){
						finalTabData+=investigationDataNew;
					}
					
					if($scope.treatmentHistoryList1.length>0){
						finalTabData+="<B>Treatment / Investigations</B>"+treatmentHistoryDataNew;
					}
					
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription=finalTabData;
					//}
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
					var vitalData = "<table ><tr bgcolor='#2e6ea2' color='#FFFFFF'><th width='15%' >Created Date</th><th width='40%' >Vital Name</th> <th width='15%' >Value</th></tr>";
					angular.forEach($scope.addedVitalList,function(value1,key){
						vitalData += "<tr><td style='width:10%;'>"+value1.createdDate+"</td><td style='width:30%;'> "+value1.vitalName+"</td><td style='width:30%;'> "+value1.value+" "+value1.unitDesc+"</td></tr>";
						//vitalData += value1.createdDate+" - "+value1.vitalName+" - "+value1.value+" - "+value1.unitDesc+" - "+(value1.doctorName!=null?value1.doctorName:value1.employeeName)+"<br>";
					});
					vitalData +="</table>";
					var vitalDataNew=vitalData.replace(/null/g," ");
					}
					
					if($scope.getSavedGeneralExamList.length>0){
					var generalExamination = "";
					angular.forEach($scope.getSavedGeneralExamList,function(value1,key){
						generalExamination += "<table >";
						generalExamination += "<tr><th align='left'><span >"+value1.systemName+"</span>&nbsp;&nbsp;&nbsp;<span style='margin-left:40px;'>"+(value1.isNADValue =='Y'?'NAD':'')+"</span></th></tr>" +
						//		"<tr><table><tr><td>"+value1.observationName+"</td></tr>";//<td>"+value1.remark+"</td>
						     "<tr><td>"+value1.observationName+"</td></tr>";//<td>"+value1.remark+"</td>
						if(value1.systemObervationPropertyId != null){
							//generalExamination +="<tr><table><tr><td>"+value1.propertyName+"</td><td>"+value1.remark+"</td></tr></table></tr>";
							generalExamination +="<tr><td>"+value1.propertyName+"&nbsp;&nbsp;&nbsp;"+value1.remark+"</td></tr>";
						}
						//generalExamination +="</table></tr>";
						generalExamination +="</table>";
					});
					
					
					generalExamination +="<div>Descriptive Examination :<textarea rows='3'>"+$scope.descriptiveGeneralExamination+"</textarea></div>";
					var generalExaminationNew=generalExamination.replace(/null/g," ");
					}
					
					//systemic examination start

				if($scope.getSavedPhysicalExamList.length>0){
				  var physicalExamination = "";
					angular.forEach($scope.getSavedPhysicalExamList,function(value1,key){
						physicalExamination += "<table >";
						physicalExamination += "<tr><th align='left'><span >"+value1.systemName+"</span>&nbsp;&nbsp;&nbsp;<span style='margin-left:40px;'>"+(value1.isNADValue =='Y'?'NAD':'')+"</span></th></tr>" +
								"<tr><td>"+value1.observationName+"</td></tr>";//<td>"+value1.remark+"</td>
						if(value1.systemObervationPropertyId != null){
							physicalExamination +="<tr><td>"+value1.propertyName+"&nbsp;&nbsp;&nbsp;"+value1.remark+"</td></tr>";
						}
						//physicalExamination +="</table></tr>";
						physicalExamination += "</table >"
					});
					
					physicalExamination +="<div>Descriptive Examination :<textarea rows='3'>"+$scope.descriptivePhysicalExamination+"</textarea></div>";
					var physicalExaminationNew=physicalExamination.replace(/null/g," ");
				}
				
				if($scope.getSavedPhysicalExamList.length>0 || $scope.getSavedGeneralExamList.length>0 || $scope.addedVitalList.length > 0){
					var allExaminationData="";
					if($scope.addedVitalList.length > 0){
						allExaminationData+="<B>Vitals</B>"+vitalDataNew;
					}
					
					if($scope.getSavedPhysicalExamList.length>0){
						allExaminationData+="<B>Systemic Examination</B>"+physicalExaminationNew;
					}else{
						if($scope.descriptivePhysicalExamination!=undefined && $scope.descriptivePhysicalExamination!="" ){
							physicalExaminationNew="<div>Descriptive Examination :<textarea rows='3'>"+$scope.descriptivePhysicalExamination+"</textarea></div>";
							allExaminationData+="<B>Systemic Examination</B>"+physicalExaminationNew;
						}
					}
					
					if($scope.getSavedGeneralExamList.length>0){
						allExaminationData+="<B>General Examination</B>"+generalExaminationNew;
					}else{
						if($scope.descriptiveGeneralExamination!=undefined && $scope.descriptiveGeneralExamination!="" ){
							generalExaminationNew="<div>Descriptive Examination :<textarea rows='3'>"+$scope.descriptiveGeneralExamination+"</textarea></div>";
							allExaminationData+="<B>Systemic Examination</B>"+generalExaminationNew;
						}
					}
					
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription=allExaminationData;
				}
					$('#closeCommonDischargeSummaryModal').trigger('click');
				}
				
	         //Advice
					if ($scope.fieldId == 18) {
						if ($scope.addedInstructions.length > 0) {
							$scope.id = $scope.commonObjForAdd.dischargeFieldId;
							$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription = "";
							var instructionData = "<table ><tr bgcolor='#2e6ea2' color='#FFFFFF'><th width='25%' >Advice</th><th width='75%'>Note</th> </tr>";
							angular.forEach($scope.addedInstructions,
											function(value1, key) {
												instructionData += "<tr><td style='width:30%;'>"
														+ (value1.instructionDetailsId!=null ? value1.instructionDesc:value1.advicedInstruction)
														+ "</td><td style='width:70%;'> "
														+ value1.notes
														+ "</td></tr>";
											});
							instructionData += "</table>";
							var instructionDataNew = instructionData.replace(/null/g, " ");
						}
						
						if ($scope.addedFollowupAppointment.length > 0) {
							$scope.id = $scope.commonObjForAdd.dischargeFieldId;
							$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription = "";
							var followUpApp = "<table ><tr bgcolor='#2e6ea2' color='#FFFFFF'><th width='50%' >Appointment Date</th><th width='50%'>Time</th> </tr>";
							angular.forEach($scope.addedFollowupAppointment,
											function(value1, key) {
								             followUpApp += "<tr><td style='width:50%;'>"
														+ value1.appointmentDate
														+ "</td><td style='width:50%;'> "
														+ value1.fromTimeString
														+ "</td></tr>";
											});
							followUpApp += "</table>";
							var followUpAppNew = followUpApp.replace(/null/g, " ");
						}
						var finalDataAdviceAndFollowUp="";
						if($scope.addedInstructions.length > 0){
							finalDataAdviceAndFollowUp+="<B>Advice</B>"+instructionDataNew;
						}
						if($scope.addedFollowupAppointment.length > 0){
							finalDataAdviceAndFollowUp+="<B>Follow up</B>"+followUpAppNew;
						}
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription = finalDataAdviceAndFollowUp;

						$('#closeCommonDischargeSummaryModal').trigger('click');
					}
				
					//Get complaint list
					if($scope.fieldId==13){
						if($scope.addedComplaintList.length > 0){
						$scope.id=$scope.commonObjForAdd.dischargeFieldId;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						var complaintData = "<table><tr bgcolor='#2e6ea2' color='#FFFFFF' width='14%'><th>Date</th><th width='20%'>ICPC System</th>" +
								"<th width='20%'>ICPC Code</th><th width='25%'>ICPC Description</th><th width='8%'>Since</th><th width='10%'>HOPC</th></tr>";
						angular.forEach($scope.addedComplaintList,function(value,key){
							var diagName="";
							if(value.icpcCodeText!=''){
								diagName=value.icpcCodeText;
							}else{
								diagName=value.diagnosisName;
							}
							complaintData += "<tr><td>"+value.createdDate+" </td><td>"+value.icpcSystemDesc+" </td><td>"+value.icpcCode+" </td><td>"+diagName+" </td>" +
									"<td>"+value.since+' '+value.duration+" </td><td>"+value.remark+" </td></tr>";
						});
						complaintData+="</table>";
						var complaintDataNew=complaintData.replace(/null/g," ");
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription=complaintDataNew;
						}
						$('#closeCommonDischargeSummaryModal').trigger('click');
					}
			}else{

				//New tabular code sneha
				//allergis
		       if($scope.fieldId==1){
					if($scope.addedAllergyList.length > 0){
						$scope.id=$scope.commonObjForAdd.dischargeFieldId;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = 0;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = 0;
						 var columnCountAllergies = 0;
						angular.forEach($scope.columnsToPrint, function(value, key){
							if(value.isColumnRequired == true){
								columnCountAllergies++;
							}
						});
					
						$scope.columnsnNameAllergies = [];
		               angular.forEach($scope.columnsToPrint, function(value, key){
							if(value.isColumnRequired == true){
								$scope.columnsnNameAllergies.push(value);
							}
							
						});
		
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = columnCountAllergies;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = $scope.addedAllergyList.length;
						
						  $scope.rowListAllergies = [];
							var iiiRow  = 0;
							 angular.forEach($scope.addedAllergyList, function(value, index){
								 var newArrRow = {};
								 newArrRow.coulmnId = iiiRow;
								 newArrRow.rowId = iiiRow;
								 var allergyTypeName = '';
								if(value.isNoKnownAllergies=='Y'){
											allergyTypeName = "No Allergy&nbsp";
								}else{
											allergyTypeName = value.allergyTypeName;
								}
							 
								 newArrRow.allergyTypeName = allergyTypeName;
								 newArrRow.allergyName = (value.allergyName != undefined ? value.allergyName :value.genericName);
								 newArrRow.severityDesc = value.severityDesc!= null ? value.severityDesc :"";
								 newArrRow.natureOfReaction = value.natureOfReaction!= null ? value.natureOfReaction :"";
								 newArrRow.type = value.type!= null ? value.type :"";
								 newArrRow.remark = value.remark != null ? value.remark :"";
								 $scope.rowListAllergies.push(newArrRow);
								 iiiRow++;
								});
							$scope.columnListAllergies = [];
							$scope.columnHeaderListAllergies = []
							var iii  = 0;
							$scope.tabelNameAllergis = '';
							 angular.forEach($scope.columnsnNameAllergies, function(value, index){
										var newArr = {};
										newArr.coulmnId = iii;
										newArr.dischSumTabColMstId = value.dischSumTabColMstId;
										newArr.columnName = value.columnName;
										newArr.tableName = value.tableName;
										$scope.tabelNameAllergis = value.tableName;
										newArr.inputValue = '';
										$scope.columnListAllergies.push(newArr);
										$scope.columnHeaderListAllergies.push(newArr);
										iii++;
								});
							$scope.savedListAllergies=[];
							angular.forEach($scope.rowListAllergies,function(valueAllergies1,key1){
								angular.forEach($scope.columnListAllergies,function(colAllergies2,key2){
									var allergyData = "";
								
									if(colAllergies2.dischSumTabColMstId == 1){
										allergyData = valueAllergies1.allergyTypeName;
									}
									if(colAllergies2.dischSumTabColMstId == 2){
										allergyData = (valueAllergies1.allergyName != undefined ? valueAllergies1.allergyName :valueAllergies1.genericName);
									}
									if(colAllergies2.dischSumTabColMstId == 3){
										allergyData = valueAllergies1.severityDesc;
									}
									if(colAllergies2.dischSumTabColMstId == 4){
										allergyData = valueAllergies1.natureOfReaction;
									}
									if(colAllergies2.dischSumTabColMstId == 5){
										allergyData = valueAllergies1.type;
									}
									if(colAllergies2.dischSumTabColMstId == 6){
										allergyData = valueAllergies1.remark;
									}
											
								
									var data = {
											inputValue:	allergyData,
											tableTypeId :1,// allergirs
											coulmnId:colAllergies2.coulmnId,
											columnName:colAllergies2.columnName,
											rowId:valueAllergies1.rowId,
											rowName:allergyData,
											tableName:colAllergies2.tableName,
											numberOfRow:$scope.addedAllergyList.length,
											numberOfCoulmn:columnCountAllergies,
											organizationId:$scope.organizationId,
											unitId:$scope.unitId,
											createdBy:$scope.userId,
											updatedBy:$scope.userId,
											status:'A',
											createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
									   };
											$scope.savedListAllergies.push(data);
								});
							});

						console.log("$scope.savedList",$scope.savedList );
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableTypeId=1;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableTypeId=1;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tabelNameAllergis=$scope.tabelNameAllergis;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListAllergies=$scope.rowListAllergies;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListAllergies=$scope.columnListAllergies;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderListAllergies=$scope.columnHeaderListAllergies;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListAllergies=$scope.savedListAllergies;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeSummaryDataViewOptionIdTest=2;
						
						}
						$('#closeCommonDischargeSummaryModal').trigger('click');
					}
				//Add History Data
				if($scope.fieldId==2){
					
					//personal history
					if($scope.personalHistoryList.length > 0){
						$scope.id=$scope.commonObjForAdd.dischargeFieldId;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = 0;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = 0;
						
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderListPersonalHistory=[];
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListPersonalHistory=[];
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListPersonalHistory=[];
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListPersonalHistory = [];
						
						 var columnCountPersonal = 0;
						angular.forEach($scope.tempListForPrsonalCol, function(value, key){
							if(value.isColumnRequired == true){
								columnCountPersonal++;
							}
						});
						$scope.columnsnNamePersonalHistory = [];
		               angular.forEach($scope.tempListForPrsonalCol, function(value, key){
							if(value.isColumnRequired == true){
								$scope.columnsnNamePersonalHistory.push(value);
							}
							
						});
		
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = columnCountPersonal;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = $scope.personalHistoryList.length;
						
						$scope.rowListPersonalHistory = [];
							var iiiRow  = 0;
							 angular.forEach($scope.personalHistoryList, function(value, index){
								 var newArrRow = {};
								 newArrRow.coulmnId = iiiRow;
								 newArrRow.rowId = iiiRow;
								 newArrRow.rowName = value.createdDate!= null ? value.createdDate :"";
								 newArrRow.createdDate = value.createdDate!= null ? value.createdDate :"";
								 newArrRow.description = value.description!= null ? value.description :"";
								 newArrRow.remark = value.remark!= null ? value.remark :"";
								 newArrRow.inputbox = false;
								 $scope.rowListPersonalHistory.push(newArrRow);
								 iiiRow++;
								});
						    $scope.columnListPersonalHistory = [];
						    $scope.columnHeaderListPersonalHistory = [];
							
							var iii  = 0;
							$scope.tableNamePersonalHistory = '';
							 angular.forEach($scope.columnsnNamePersonalHistory, function(value, index){
										var newArr = {};
										newArr.coulmnId = iii;
										newArr.dischSumTabColMstId = value.dischSumTabColMstId;
										newArr.columnName = value.columnName;
										newArr.tableName = value.tableName;
										$scope.tableNamePersonalHistory = value.tableName;
										newArr.inputValue = '';
										newArr.inputbox = false;
										$scope.columnListPersonalHistory.push(newArr);
										$scope.columnHeaderListPersonalHistory.push(newArr);
										iii++;
								});
							$scope.savedListPersonalHistory=[];
							angular.forEach($scope.rowListPersonalHistory,function(value1,key1){
								angular.forEach($scope.columnListPersonalHistory,function(value2,key2){
									var input = "";
									
									if(value2.dischSumTabColMstId == 7){
										input =value1.createdDate != null ?value1.createdDate:"";
									}
									if(value2.dischSumTabColMstId == 8){
										input = value1.description != null ?value1.description :"";
									}
									if(value2.dischSumTabColMstId == 9){
										input = value1.remark != null ? value1.remark:"";
									}
								
									var data = {
											inputValue:	input,
											tableTypeId :2,// personal history
											coulmnId:value2.coulmnId,
											columnName:value2.columnName,
											rowId:value1.rowId,
											rowName:input,
											tableName : $scope.tableNamePersonalHistory,
											numberOfRow:$scope.personalHistoryList.length,
											numberOfCoulmn:columnCountPersonal,
											organizationId:$scope.organizationId,
											unitId:$scope.unitId,
											createdBy:$scope.userId,
											updatedBy:$scope.userId,
											status:'A',
											createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
									   };
											$scope.savedListPersonalHistory.push(data);
								});   
							});

						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableTypeId=2;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListPersonalHistory=$scope.rowListPersonalHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableNamePersonalHistory=$scope.tableNamePersonalHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListPersonalHistory=$scope.columnListPersonalHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderListPersonalHistory=$scope.columnHeaderListPersonalHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListPersonalHistory=$scope.savedListPersonalHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeSummaryDataViewOptionIdTest=2;
						
					}
					//family history code
					if($scope.familyHistoryList.length > 0){
						$scope.id=$scope.commonObjForAdd.dischargeFieldId;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = 0;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = 0;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListFamilyHistory=[];
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderFamilyHistory=[];
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListFamilyHistory=[];
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListFamilyHistory = [];

						
						 var columnCountFamily = 0;
						angular.forEach($scope.tempListForFamilyCol, function(value, key){
							if(value.isColumnRequired == true){
								columnCountFamily++;
							}
						});
						$scope.columnsnNameFamilyHistory = [];
		               angular.forEach($scope.tempListForFamilyCol, function(value, key){
							if(value.isColumnRequired == true){
								$scope.columnsnNameFamilyHistory.push(value);
							}
							
						});
		
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = columnCountFamily;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = $scope.familyHistoryList.length;
						
						$scope.rowListFamilyHistory = [];
							var iiiRow  = 0;
							
							 angular.forEach($scope.familyHistoryList, function(fam, index){
								 var newArrRow = {};
								 newArrRow.coulmnId = iiiRow;
								 newArrRow.rowId = iiiRow;
								 newArrRow.diagnosisName = fam.diagnosisName!= null ? fam.diagnosisName :"";
								 newArrRow.diagnosisCode = fam.diagnosisCode!= null ? fam.diagnosisCode :"";
								 newArrRow.desc = fam.desc!= null ? fam.desc :"";
								 newArrRow.createdDate = fam.createdDate!= null ? fam.createdDate :"";
								 newArrRow.remark = fam.remark!= null ? fam.remark :"";
								 $scope.rowListFamilyHistory.push(newArrRow);
								 iiiRow++;
								});
						    $scope.columnListFamilyHistory = [];
						    $scope.columnHeaderFamilyHistory = [];
							
							var iii  = 0;
							$scope.tableNameFamilyHistory = '';
							 angular.forEach($scope.columnsnNameFamilyHistory, function(fam2, index){
										var newArr = {};
										newArr.coulmnId = iii;
										newArr.dischSumTabColMstId = fam2.dischSumTabColMstId;
										newArr.columnName = fam2.columnName;
										newArr.tableName = fam2.tableName;
										$scope.tableNameFamilyHistory = fam2.tableName;
										newArr.inputValue = '';
										$scope.columnListFamilyHistory.push(newArr);
										$scope.columnHeaderFamilyHistory.push(newArr);
										iii++;
								});
							$scope.savedListFamilyHistory=[];
							angular.forEach($scope.rowListFamilyHistory,function(rowFam1,key1){
								angular.forEach($scope.columnListFamilyHistory,function(colFam1,key2){
									var input = "";
									if(colFam1.dischSumTabColMstId == 10){
										input = rowFam1.diagnosisName != null ? rowFam1.diagnosisName : rowFam1.diagnosisName;
									}
									if(colFam1.dischSumTabColMstId == 11){
										input = rowFam1.diagnosisCode != null ? rowFam1.diagnosisCode:'';
									}
									if(colFam1.dischSumTabColMstId == 12){
										input = rowFam1.desc!=null ? rowFam1.desc:"";
									}
									if(colFam1.dischSumTabColMstId == 13){
										input = rowFam1.createdDate;
									}
									if(colFam1.dischSumTabColMstId == 14){
										input = rowFam1.remark;
									}
									
									var data = {
											inputValue:	input,
											tableTypeId :3,// family history
											coulmnId:colFam1.coulmnId,
											columnName:colFam1.columnName,
											rowId:rowFam1.rowId,
											rowName:input,
											tableName:$scope.tableNameFamilyHistory,
											numberOfRow:$scope.familyHistoryList.length,
											numberOfCoulmn:columnCountFamily,
											organizationId:$scope.organizationId,
											unitId:$scope.unitId,
											createdBy:$scope.userId,
											updatedBy:$scope.userId,
											status:'A',
											createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
									   };
											$scope.savedListFamilyHistory.push(data);
								});   
							});

						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableTypeId=3;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListFamilyHistory=$scope.rowListFamilyHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableNameFamilyHistory=$scope.tableNameFamilyHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListFamilyHistory=$scope.columnListFamilyHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderListFamilyHistory=$scope.columnHeaderFamilyHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListFamilyHistory = $scope.savedListFamilyHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeSummaryDataViewOptionIdTest=2;
						
					}
					
					//Surgical History code
					if($scope.surgicalHistoryList.length > 0){
						$scope.id=$scope.commonObjForAdd.dischargeFieldId;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = 0;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = 0;
						
						
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListSurgicalHistory=[];
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListSurgicalHistory=[];
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderListSurgicalHistory=[];
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListSurgicalHistory = [];
						
						 var columnCountSurgical = 0;
						angular.forEach($scope.tempListForSurgicalCol, function(value, key){
							if(value.isColumnRequired == true){
								columnCountSurgical++;
							}
						});
						$scope.columnsnNameSurgicalHistory = [];
		               angular.forEach($scope.tempListForSurgicalCol, function(value, key){
							if(value.isColumnRequired == true){
								$scope.columnsnNameSurgicalHistory.push(value);
							}
							
						});
		
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = columnCount;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = $scope.surgicalHistoryList.length;
						
						
						
						
						$scope.rowListSurgicalHistory = [];
							var iiiRow  = 0;
							 angular.forEach($scope.surgicalHistoryList, function(sur, index){
								 var newArrRow = {};
								 newArrRow.coulmnId = iiiRow;
								 newArrRow.rowId = iiiRow;
								 newArrRow.surgeryNameFormulary = sur.surgeryNameFormulary!= null ? sur.surgeryNameFormulary :"";
								 newArrRow.surgeryName = sur.surgeryName!= null ? sur.surgeryName :"";
								 newArrRow.surgenName = sur.surgenName!= null ? sur.surgenName :"";
								 newArrRow.month = sur.month!= null ? sur.month :"";
								 newArrRow.year = sur.year!= null ? sur.year :"";
								 newArrRow.remark = sur.remark!= null ? sur.remark :"";
								 newArrRow.inputbox = false;
								 $scope.rowListSurgicalHistory.push(newArrRow);
								 iiiRow++;
								});
						    $scope.columnListSurgicalHistory = [];
						    $scope.columnHeaderSurgicalHistory = [];
							
							var iii  = 0;
							$scope.tableNameSurgicalHistory = '';
							 angular.forEach($scope.columnsnNameSurgicalHistory, function(sur2, index){
										var newArr = {};
										newArr.coulmnId = iii;
										newArr.dischSumTabColMstId = sur2.dischSumTabColMstId;
										newArr.columnName = sur2.columnName;
										newArr.tableName = sur2.tableName;
										$scope.tableNameSurgicalHistory = sur2.tableName;
										newArr.inputValue = '';
										newArr.inputbox = false;
										$scope.columnListSurgicalHistory.push(newArr);
										$scope.columnHeaderSurgicalHistory.push(newArr);
										iii++;
								});
							$scope.savedListSurgicalHistory=[];
							angular.forEach($scope.rowListSurgicalHistory,function(rowSur1,key1){
								angular.forEach($scope.columnListSurgicalHistory,function(colSur1,key2){
									var input = "";
									   if(colSur1.dischSumTabColMstId == 15){
										   input = (rowSur1.surgeryNameFormulary != null && rowSur1.surgeryNameFormulary != "") ? rowSur1.surgeryNameFormulary : rowSur1.surgeryName;
										}
										if(colSur1.dischSumTabColMstId == 16){
											input = rowSur1.surgenName;
										}
										if(colSur1.dischSumTabColMstId == 17){
											input = rowSur1.month!=null ? rowSur1.month:"";
										}
										if(colSur1.dischSumTabColMstId == 18){
											input = rowSur1.year;
										}
										if(colSur1.dischSumTabColMstId == 19){
											input = rowSur1.remark;
										}
									var data = {
											inputValue:	input,
											tableTypeId :4,// surgical history
											coulmnId:colSur1.coulmnId,
											columnName:colSur1.columnName,
											rowId:rowSur1.rowId,
											rowName:input,
											tableName : $scope.tableNameSurgicalHistory ,
											numberOfRow:$scope.surgicalHistoryList.length,
											numberOfCoulmn:columnCountSurgical,
											organizationId:$scope.organizationId,
											unitId:$scope.unitId,
											createdBy:$scope.userId,
											updatedBy:$scope.userId,
											status:'A',
											createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
									   };
											$scope.savedListSurgicalHistory.push(data);
								});   
							});

						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableTypeId=4;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListSurgicalHistory=$scope.rowListSurgicalHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableNameSurgicalHistory=$scope.tableNameSurgicalHistory ;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListSurgicalHistory=$scope.columnListSurgicalHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderListSurgicalHistory=$scope.columnHeaderSurgicalHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListSurgicalHistory = $scope.savedListSurgicalHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeSummaryDataViewOptionIdTest=2;
						
					}
					
					
					
					//Medication History code
					if($scope.medicationHistoryList.length > 0){
						$scope.id=$scope.commonObjForAdd.dischargeFieldId;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = 0;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = 0;
						
						
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListMedicationHistory=[];
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListMedicationHistory=[];
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderListMedicationHistory=[];
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListMedicationHistory = [];
						
						 var columnCountMedicationHistory = 0;
						angular.forEach($scope.tempListForMedHistoryCol, function(value, key){
							if(value.isColumnRequired == true){
								columnCountMedicationHistory++;
							}
						});
						$scope.columnsnNameMedicationHistory = [];
		               angular.forEach($scope.tempListForMedHistoryCol, function(value, key){
							if(value.isColumnRequired == true){
								$scope.columnsnNameMedicationHistory.push(value);
							}
							
						});
		
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = columnCountMedicationHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = $scope.medicationHistoryList.length;
						
						
						
						
						$scope.rowListMedicationHistory = [];
							var iiiRow  = 0;
							 angular.forEach($scope.medicationHistoryList, function(medHistory, index){
								 var newArrRow = {};
								 newArrRow.coulmnId = iiiRow;
								 newArrRow.rowId = iiiRow;
								var doseDesc="";
								if(medHistory.doseId==null){
									doseDesc=medHistory.doseDesc;
								}else{
									doseDesc=medHistory.doseId+" "+medHistory.doseUnit;
								}
								if(medHistory.instructionId == null){
									instructionDesc=medHistory.instructionText;
								}else{
									instructionDesc=medHistory.instructionDesc;
								}
								
								if(medHistory.isNoSignificantMedication == 'Y'){
									medHistory.itemName="No Significant Medication History";
								}
								 newArrRow.itemName = medHistory.itemName!= null ? medHistory.itemName :"";
								 newArrRow.genericName = medHistory.genericName!= null ? medHistory.genericName :"";
								 newArrRow.formulationType = medHistory.formulationType!= null ? medHistory.formulationType :"";
								 newArrRow.doseDesc = doseDesc!= null ? doseDesc :"";
								 newArrRow.frequencyDesc = medHistory.frequencyDesc!= null ? medHistory.frequencyDesc :"";
								 newArrRow.duration = medHistory.duration!= null ? medHistory.duration :"";
								 newArrRow.roaDesc = medHistory.roaDesc!= null ? medHistory.roaDesc :"";
								 newArrRow.orderQty = medHistory.orderQty!= null ? medHistory.orderQty :"";
								 newArrRow.startDate = medHistory.startDate!= null ? medHistory.startDate :"";
								 newArrRow.endDate = medHistory.endDate!= null ? medHistory.endDate :"";
								 newArrRow.instructionDesc = instructionDesc!= null ? instructionDesc :"";
								 $scope.rowListMedicationHistory.push(newArrRow);
								 iiiRow++;
								});
						    $scope.columnListMedicationHistory = [];
						    $scope.columnHeaderMedicationHistory = [];
							
							var iii  = 0;
							$scope.tableNameMedicationHistory = '';
							 angular.forEach($scope.columnsnNameMedicationHistory, function(medHis2, index){
										var newArr = {};
										newArr.coulmnId = iii;
										newArr.dischSumTabColMstId = medHis2.dischSumTabColMstId;
										newArr.columnName = medHis2.columnName;
										newArr.tableName = medHis2.tableName;
										$scope.tableNameMedicationHistory =  medHis2.tableName;
										newArr.inputValue = '';
										$scope.columnListMedicationHistory.push(newArr);
										$scope.columnHeaderMedicationHistory.push(newArr);
										iii++;
								});
							$scope.savedListMedicationHistory=[];
							angular.forEach($scope.rowListMedicationHistory,function(rowMediHist1,key1){
								angular.forEach($scope.columnListMedicationHistory,function(colMediHist1,key2){
									var medicationHistoryData = "";
												
									if(colMediHist1.dischSumTabColMstId == 20){
										medicationHistoryData = rowMediHist1.itemName;
									}
									if(colMediHist1.dischSumTabColMstId == 21){
										medicationHistoryData = rowMediHist1.genericName;
									}
									if(colMediHist1.dischSumTabColMstId == 22){
										medicationHistoryData = rowMediHist1.formulationType;
									}
									if(colMediHist1.dischSumTabColMstId == 23){
										medicationHistoryData = rowMediHist1.doseDesc;
									}
									if(colMediHist1.dischSumTabColMstId == 24){
										medicationHistoryData = rowMediHist1.frequencyDesc;
									}
									if(colMediHist1.dischSumTabColMstId == 25){
										medicationHistoryData = rowMediHist1.duration;
									}
									if(colMediHist1.dischSumTabColMstId == 26){
										medicationHistoryData = rowMediHist1.roaDesc;
									}
									if(colMediHist1.dischSumTabColMstId ==27 ){
										medicationHistoryData = rowMediHist1.orderQty;
									}
									if(colMediHist1.dischSumTabColMstId == 28){
										medicationHistoryData = rowMediHist1.startDate;
									}
									if(colMediHist1.dischSumTabColMstId == 29){
										medicationHistoryData = rowMediHist1.endDate;
									}
									if(colMediHist1.dischSumTabColMstId == 30){
										medicationHistoryData = rowMediHist1.instructionDesc;
									}
									
									var data = {
											inputValue:	medicationHistoryData,
											tableTypeId :5,// medication history
											coulmnId:colMediHist1.coulmnId,
											columnName:colMediHist1.columnName,
											rowId:rowMediHist1.rowId,
											rowName:medicationHistoryData,
											tableName:$scope.tableNameMedicationHistory,
											numberOfRow:$scope.medicationHistoryList.length,
											numberOfCoulmn:columnCountMedicationHistory,
											organizationId:$scope.organizationId,
											unitId:$scope.unitId,
											createdBy:$scope.userId,
											updatedBy:$scope.userId,
											status:'A',
											createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
									   };
											$scope.savedListMedicationHistory.push(data);
								});   
							});

						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableTypeId=4;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListMedicationHistory=$scope.rowListMedicationHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableNameMedicationHistory=$scope.tableNameMedicationHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListMedicationHistory=$scope.columnListMedicationHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderListMedicationHistory=$scope.columnHeaderMedicationHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListMedicationHistory = $scope.savedListMedicationHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeSummaryDataViewOptionIdTest=2;
						
					}
					//Medical History code
					if($scope.medicalHistoryList.length > 0){
						$scope.id=$scope.commonObjForAdd.dischargeFieldId;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = 0;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = 0;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListMedicalHistory=[];
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListMedicalHistory=[];
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderMedicalHistory=[];
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListMedicalHistory = [];
						 var columnCountMedical = 0;
						angular.forEach($scope.tempListForMedicalCol, function(value, key){
							if(value.isColumnRequired == true){
								columnCountMedical++;
							}
						});
						$scope.columnsnNameMedicalHistory = [];
		               angular.forEach($scope.tempListForMedicalCol, function(value, key){
							if(value.isColumnRequired == true){
								$scope.columnsnNameMedicalHistory.push(value);
							}
							
						});
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = columnCountMedical;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = $scope.medicalHistoryList.length;
						$scope.rowListMedicalHistory = [];
							var iiiRow  = 0;
							 angular.forEach($scope.medicalHistoryList, function(medical, index){
								 var newArrRow = {};
								 newArrRow.coulmnId = iiiRow;
								 newArrRow.rowId = iiiRow;
								var since="";
								if(medical.since==undefined)
									since=medical.resolvedDays+" Day(s)";
								else
									since=medical.since;
								 newArrRow.diagnosisName = medical.diagnosisName!= null ? medical.diagnosisName :"";
								 newArrRow.code = medical.code!= null ? medical.code :"";
								 newArrRow.since = medical.since!= null ? medical.since:""+" "+medical.duration!= null ? medical.duration:"";
								 newArrRow.duration = medical.duration!= null ? medical.duration :"";
								 newArrRow.remark = medical.remark!= null ? medical.remark :"";
								 newArrRow.inputbox = false;
								 $scope.rowListMedicalHistory.push(newArrRow);
								 iiiRow++;
								});
						    $scope.columnListMedicalHistory = [];
						    $scope.columnHeaderMedicalHistory = [];
							
							var iii  = 0;
							$scope.tableNameMedicalHistory = '';
							 angular.forEach($scope.columnsnNameMedicalHistory, function(medical2, index){
										var newArr = {};
										newArr.coulmnId = iii;
										newArr.dischSumTabColMstId = medical2.dischSumTabColMstId;
										newArr.columnName = medical2.columnName;
										newArr.tableName = medical2.tableName;
										$scope.tableNameMedicalHistory  = medical2.tableName;
										newArr.inputValue = '';
										newArr.inputbox = false;
										$scope.columnListMedicalHistory.push(newArr);
										$scope.columnHeaderMedicalHistory.push(newArr);
										iii++;
								});
							$scope.savedListMedicalHistory=[];
							angular.forEach($scope.rowListMedicalHistory,function(rowMedical1,key1){
								angular.forEach($scope.columnListMedicalHistory,function(colMedical1,key2){
									var input = "";
								
										if(colMedical1.dischSumTabColMstId == 31){
											input = rowMedical1.diagnosisName;
										}
										if(colMedical1.dischSumTabColMstId == 32){
											input = rowMedical1.code;
										}
										if(colMedical1.dischSumTabColMstId == 33){
											input = (rowMedical1.since!= null ? rowMedical1.since:"")+" "+(rowMedical1.duration!= null ? rowMedical1.duration:"");
										}
										if(colMedical1.dischSumTabColMstId == 34){
											input = rowMedical1.remark;
										}
										
								
									var data = {
											inputValue:	input,
											tableTypeId :6,// Medical history
											coulmnId:colMedical1.coulmnId,
											columnName:colMedical1.columnName,
											rowId:rowMedical1.rowId,
											rowName:input,
											tableName:$scope.tableNameMedicalHistory,
											numberOfRow:$scope.medicalHistoryList.length,
											numberOfCoulmn:columnCountMedical,
											organizationId:$scope.organizationId,
											unitId:$scope.unitId,
											createdBy:$scope.userId,
											updatedBy:$scope.userId,
											status:'A',
											createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
									   };
											$scope.savedListMedicalHistory.push(data);
								});   
							});

						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableTypeId=4;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListMedicalHistory=$scope.rowListMedicalHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableNameMedicalHistory=$scope.tableNameMedicalHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListMedicalHistory=$scope.columnListMedicalHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderMedicalHistory=$scope.columnHeaderMedicalHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListMedicalHistory = $scope.savedListMedicalHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeSummaryDataViewOptionIdTest=2;
						
					}
					
					
					//Vaccination History code
					if($scope.immunizationHistoryList.length > 0){
						$scope.id=$scope.commonObjForAdd.dischargeFieldId;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = 0;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = 0;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListVaccinationHistory=[];
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListVaccinationHistory=[];
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderListVaccinationHistory=[];
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListVaccinationHistory = [];
						 var columnCountVaccinationHistory = 0;
						angular.forEach($scope.vaccineMapperListCol, function(value, key){
							if(value.isColumnRequired == true){
								columnCountVaccinationHistory++;
							}
						});
						$scope.columnsnNameVaccinationHistory = [];
		               angular.forEach($scope.vaccineMapperListCol, function(value, key){
							if(value.isColumnRequired == true){
								$scope.columnsnNameVaccinationHistory.push(value);
							}
							
						});
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = columnCountVaccinationHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = $scope.immunizationHistoryList.length;
						$scope.rowListVaccinationHistory = [];
							var iiiRow  = 0;
							 angular.forEach($scope.immunizationHistoryList, function(vacci, index){
								 var newArrRow = {};
								 newArrRow.coulmnId = iiiRow;
								 newArrRow.rowId = iiiRow;
								 newArrRow.minAge = vacci.minAge!= null ? vacci.minAge :"";
								 newArrRow.minDay = vacci.minDay!= null ? vacci.minDay :"";
								 newArrRow.genericName = vacci.genericName!= null ? vacci.genericName :"";
								 newArrRow.abbreviation = vacci.abbreviation!= null ? vacci.abbreviation:"";
								 newArrRow.series = vacci.series!= null ? vacci.series :"";
								 newArrRow.calculatedDateShow = vacci.calculatedDateShow!= null ? vacci.calculatedDateShow :"";
								 newArrRow.plannedDate = vacci.plannedDate!= null ? vacci.plannedDate :"";
								 newArrRow.location = vacci.location!= null ? vacci.location :"";
								 newArrRow.remarks = vacci.remarks!= null ? vacci.remarks :"";
								 $scope.rowListVaccinationHistory.push(newArrRow);
								 iiiRow++;
								});
						    $scope.columnListVaccinationHistory = [];
						    $scope.columnHeaderListVaccinationHistory = [];
							
							var iii  = 0;
							$scope.tableNameVaccinationHistory = '';
							 angular.forEach($scope.columnsnNameVaccinationHistory, function(vaccine2, index){
										var newArr = {};
										newArr.coulmnId = iii;
										newArr.dischSumTabColMstId = vaccine2.dischSumTabColMstId;
										newArr.columnName = vaccine2.columnName;
										newArr.tableName = vaccine2.tableName;
										$scope.tableNameVaccinationHistory = vaccine2.tableName;
										newArr.inputValue = '';
										$scope.columnListVaccinationHistory.push(newArr);
										$scope.columnHeaderListVaccinationHistory.push(newArr);
										iii++;
								});
							$scope.savedListVaccinationHistory=[];
							angular.forEach($scope.rowListVaccinationHistory,function(rowVaccine1,key1){
								angular.forEach($scope.columnListVaccinationHistory,function(colVaccine1,key2){
									  var input = "";
									 if(colVaccine1.dischSumTabColMstId == 35){
										 input = (rowVaccine1.minAge!=null ? rowVaccine1.minAge :"")+" "+(rowVaccine1.minDay!=null ? rowVaccine1.minDay :"");
										}
										if(colVaccine1.dischSumTabColMstId == 36){
											input = (rowVaccine1.genericName!=null ? rowVaccine1.genericName :"");
										}
										if(colVaccine1.dischSumTabColMstId == 37){
											input = ((rowVaccine1.abbreviation!=null ? rowVaccine1.abbreviation :"")+" "+(rowVaccine1.series!=null ? rowVaccine1.series :""));
										}
										if(colVaccine1.dischSumTabColMstId == 38){
											input = (rowVaccine1.calculatedDateShow);
										}
										if(colVaccine1.dischSumTabColMstId == 39){
											input = (rowVaccine1.plannedDate!=null ? rowVaccine1.plannedDate :"");
										}
										if(colVaccine1.dischSumTabColMstId == 40){
											input = (rowVaccine1.location!=null ? rowVaccine1.location :"");
										}
										if(colVaccine1.dischSumTabColMstId ==41 ){
											input = (rowVaccine1.remarks!=null ? rowVaccine1.remarks :"");
										}
								
									var data = {
											inputValue:	input,
											tableTypeId :7,// vaccination history
											coulmnId:colVaccine1.coulmnId,
											columnName:colVaccine1.columnName,
											rowId:rowVaccine1.rowId,
											rowName:input,
											tableName:$scope.tableNameVaccinationHistory,
											numberOfRow:$scope.immunizationHistoryList.length,
											numberOfCoulmn:columnCountVaccinationHistory,
											organizationId:$scope.organizationId,
											unitId:$scope.unitId,
											createdBy:$scope.userId,
											updatedBy:$scope.userId,
											status:'A',
											createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
									   };
											$scope.savedListVaccinationHistory.push(data);
								});   
							});

						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableTypeId=7;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListVaccinationHistory=$scope.rowListVaccinationHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableNameVaccinationHistory=$scope.tableNameVaccinationHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListVaccinationHistory=$scope.columnListVaccinationHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderListVaccinationHistory=$scope.columnHeaderListVaccinationHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListVaccinationHistory = $scope.savedListVaccinationHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeSummaryDataViewOptionIdTest=2;
						
					}
					
					//Travel History
					if($scope.travelHistoryList.length > 0){
						$scope.id=$scope.commonObjForAdd.dischargeFieldId;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = 0;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = 0;
						
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListTravelHistory=[];
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListTravelHistory=[];
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderListTravelHistory=[];
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListTravelHistory = [];
						
						 var columnCountTravHistory = 0;
						angular.forEach($scope.tempListForTravelCol, function(value, key){
							if(value.isColumnRequired == true){
								columnCountTravHistory++;
							}
						});
						$scope.columnsnNameTravHistory = [];
		               angular.forEach($scope.tempListForTravelCol, function(value, key){
							if(value.isColumnRequired == true){
								$scope.columnsnNameTravHistory.push(value);
							}
							
						});
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = columnCountTravHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = $scope.travelHistoryList.length;
						$scope.rowListTravelHistory = [];
							var iiiRow  = 0;
							 angular.forEach($scope.travelHistoryList, function(travel, index){
								 var newArrRow = {};
								 newArrRow.coulmnId = iiiRow;
								 newArrRow.rowId = iiiRow;
								if(travel.isNoSignificantTravel == 'Y'){
									travel.description="No Significant Travel History";
								}
								 newArrRow.description = travel.description!= null ? travel.description :"";
								 newArrRow.whenTravel = travel.whenTravel!= null ? travel.whenTravel :"";
								 $scope.rowListTravelHistory.push(newArrRow);
								 iiiRow++;
								});
						    $scope.columnListTravelHistory = [];
						    $scope.columnHeaderListTravelHistory = [];
							
							var iii  = 0;
							$scope.tableNameTravelHistory = '';
							 angular.forEach($scope.columnsnNameTravHistory, function(vaccine2, index){
										var newArr = {};
										newArr.coulmnId = iii;
										newArr.dischSumTabColMstId = vaccine2.dischSumTabColMstId;
										newArr.columnName = vaccine2.columnName;
										newArr.tableName = vaccine2.tableName;
										$scope.tableNameTravelHistory = vaccine2.tableName;
										newArr.inputValue = '';
										$scope.columnListTravelHistory.push(newArr);
										$scope.columnHeaderListTravelHistory.push(newArr);
										iii++;
								});
							$scope.savedListTravelHistory=[];
							angular.forEach($scope.rowListTravelHistory,function(rowTravel1,key1){
								angular.forEach($scope.columnListTravelHistory,function(colTravel1,key2){
									  var input = "";
									  
										if(colTravel1.dischSumTabColMstId == 42){
											input = (rowTravel1.description!= null ? rowTravel1.description:"");
										}
										if(colTravel1.dischSumTabColMstId == 43){
											input = (rowTravel1.whenTravel!= null ? rowTravel1.whenTravel:"");
										}
							
									var data = {
											inputValue:	input,
											tableTypeId :8,// travel history
											coulmnId:colTravel1.coulmnId,
											columnName:colTravel1.columnName,
											rowId:rowTravel1.rowId,
											rowName:input,
											tableName:$scope.tableNameTravelHistory ,
											numberOfRow:$scope.travelHistoryList.length,
											numberOfCoulmn:columnCountTravHistory,
											organizationId:$scope.organizationId,
											unitId:$scope.unitId,
											createdBy:$scope.userId,
											updatedBy:$scope.userId,
											status:'A',
											createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
									   };
											$scope.savedListTravelHistory.push(data);
								});   
							});

						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableTypeId=7;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListTravelHistory=$scope.rowListTravelHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableNameTravelHistory=$scope.tableNameTravelHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListTravelHistory=$scope.columnListTravelHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderListTravelHistory=$scope.columnHeaderListTravelHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListTravelHistory = $scope.savedListTravelHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeSummaryDataViewOptionIdTest=2;
						
					}
					
					//Treatment / Investigations
					if($scope.treatmentHistoryList.length > 0){
						$scope.id=$scope.commonObjForAdd.dischargeFieldId;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = 0;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = 0;
						
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListTreatmentHistory=[];
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListTreatmentHistory=[];
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderListTreatmentHistory =[];
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListTreatmentHistory = [];
						
						 var columnCountTreatmentHistory = 0;
						angular.forEach($scope.tempListForTreatementCol, function(value, key){
							if(value.isColumnRequired == true){
								columnCountTreatmentHistory++;
							}
						});
						$scope.columnsnNameTreatmentHistory = [];
		               angular.forEach($scope.tempListForTreatementCol, function(value, key){
							if(value.isColumnRequired == true){
								$scope.columnsnNameTreatmentHistory.push(value);
							}
							
						});
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = columnCountTreatmentHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = $scope.treatmentHistoryList.length;
						$scope.rowListTreatmentHistory = [];
							var iiiRow  = 0;
							 angular.forEach($scope.treatmentHistoryList, function(tretment, index){
								 var newArrRow = {};
								 newArrRow.coulmnId = iiiRow;
								 newArrRow.rowId = iiiRow;
								 newArrRow.treatementName = tretment.treatementName != null ? tretment.treatementName :"";
								 newArrRow.treatDate = tretment.treatDate!= null ? tretment.treatDate :"";
								 newArrRow.remark = tretment.remark!= null ? tretment.remark :"";
								 $scope.rowListTreatmentHistory.push(newArrRow);
								 iiiRow++;
								});
						    $scope.columnListTreatmentHistory = [];
						    $scope.columnHeaderListTreatmentHistory  = [];
							
							var iii  = 0;
							$scope.tableNameTreatmentHistory = '';
							 angular.forEach($scope.columnsnNameTreatmentHistory, function(treatment2, index){
										var newArr = {};
										newArr.coulmnId = iii;
										newArr.dischSumTabColMstId = treatment2.dischSumTabColMstId;
										newArr.columnName = treatment2.columnName;
										newArr.tableName = treatment2.tableName;
										$scope.tableNameTreatmentHistory  = treatment2.tableName;
										newArr.inputValue = '';
										$scope.columnListTreatmentHistory.push(newArr);
										$scope.columnHeaderListTreatmentHistory .push(newArr);
										iii++;
								});
							$scope.savedListTreatmentHistory=[];
							angular.forEach($scope.rowListTreatmentHistory,function(rowTreatment1,key1){
								angular.forEach($scope.columnListTreatmentHistory,function(colTreatment1,key2){
									  var input = "";
											
											if(colTreatment1.dischSumTabColMstId == 44	){
												input = rowTreatment1.treatDate;
											}
											if(colTreatment1.dischSumTabColMstId == 45){
												input = rowTreatment1.treatementName;
											}
											if(colTreatment1.dischSumTabColMstId == 46){
												input = rowTreatment1.remark;
											}
							
									var data = {
											inputValue:input	,
											tableTypeId :9,// tretment history
											coulmnId:colTreatment1.coulmnId,
											columnName:colTreatment1.columnName,
											rowId:rowTreatment1.rowId,
											rowName:input,
											tableName:$scope.tableNameTreatmentHistory,
											numberOfRow:$scope.treatmentHistoryList.length,
											numberOfCoulmn:columnCountTreatmentHistory,
											organizationId:$scope.organizationId,
											unitId:$scope.unitId,
											createdBy:$scope.userId,
											updatedBy:$scope.userId,
											status:'A',
											createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
									   };
											$scope.savedListTreatmentHistory.push(data);
								});   
							});

						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableTypeId=7;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListTreatmentHistory=$scope.rowListTreatmentHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableNameTreatmentHistory=$scope.tableNameTreatmentHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListTreatmentHistory=$scope.columnListTreatmentHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderListTreatmentHistory =$scope.columnHeaderListTreatmentHistory ;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListTreatmentHistory = $scope.savedListTreatmentHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeSummaryDataViewOptionIdTest=2;
						
					}
					
					
	     		if($scope.patientHabitList.length>0){
						var finalData="";
						var habitData=$('#lifestylehabits').html();
						finalData=finalData+"<B>Lifestyle History</B>"+habitData;	
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription=finalData;
					}
					
					
					console.log("$scope.DischargeSummaryFinalList",$scope.DischargeSummaryFinalList);
						$('#closeCommonDischargeSummaryModal').trigger('click');
				}
				//Examination VITAL DATA
				if($scope.fieldId==3){
					

					if($scope.addedVitalList.length > 0){
						$scope.id=$scope.commonObjForAdd.dischargeFieldId;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = 0;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = 0;
						 var columnCountVital = 0;
						angular.forEach($scope.tempListForVitalCol, function(value, key){
							if(value.isColumnRequired == true){
								columnCountVital++;
							}
						});
					
						$scope.columnsnNameVital = [];
		               angular.forEach($scope.tempListForVitalCol, function(value, key){
							if(value.isColumnRequired == true){
								$scope.columnsnNameVital.push(value);
							}
							
						});
		
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = columnCountVital;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = $scope.addedVitalList.length;
						
						  $scope.rowListVital = [];
							var iiiRow  = 0;
							 angular.forEach($scope.addedVitalList, function(value, index){
								 var newArrRow = {};
								 newArrRow.coulmnId = iiiRow;
								 newArrRow.rowId = iiiRow;
								 newArrRow.createdDate = (value.createdDate != null ? value.createdDate :'');
								 newArrRow.vitalName = value.vitalName!= null ? value.vitalName :"";
								 newArrRow.value = value.value+" "+(value.unitDesc != undefined ? value.unitDesc :"");
								 $scope.rowListVital.push(newArrRow);
								 iiiRow++;
								});
							$scope.columnListVital = [];
							$scope.columnHeaderListVital = []
							var iii  = 0;
							$scope.tableNameVital = '';
							 angular.forEach($scope.columnsnNameVital, function(value, index){
										var newArr = {};
										newArr.coulmnId = iii;
										newArr.dischSumTabColMstId = value.dischSumTabColMstId;
										newArr.columnName = value.columnName;
										newArr.tableName = value.tableName;
										$scope.tableNameVital = value.tableName;
										newArr.inputValue = '';
										$scope.columnListVital.push(newArr);
										$scope.columnHeaderListVital.push(newArr);
										iii++;
								});
							$scope.savedListVital=[];
							angular.forEach($scope.rowListVital,function(valueVital1,key1){
								angular.forEach($scope.columnListVital,function(colVital2,key2){
									var vitalData = '';
									
									if(colVital2.dischSumTabColMstId == 51){
										vitalData = valueVital1.createdDate;
									}
									if(colVital2.dischSumTabColMstId == 52){
										vitalData = valueVital1.vitalName;
									}
									if(colVital2.dischSumTabColMstId == 53){
										vitalData = valueVital1.value;
									}
									
									var data = {
											inputValue:	vitalData,
											tableTypeId :11,// vital
											coulmnId:colVital2.coulmnId,
											columnName:colVital2.columnName,
											rowId:valueVital1.rowId,
											rowName:vitalData,
											tableName:$scope.tableNameVital,
											numberOfRow:$scope.addedVitalList.length,
											numberOfCoulmn:columnCountVital,
											organizationId:$scope.organizationId,
											unitId:$scope.unitId,
											createdBy:$scope.userId,
											updatedBy:$scope.userId,
											status:'A',
											createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
									   };
											$scope.savedListVital.push(data);
								});
							});

						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableTypeId=7;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListVital=$scope.rowListVital;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableNameVital=$scope.tableNameVital;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListVital=$scope.columnListVital;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderListVital=$scope.columnHeaderListVital;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListVital=$scope.savedListVital;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeSummaryDataViewOptionIdTest=2;
					}
						if($scope.getSavedGeneralExamList.length>0){
							var generalExamination = "";
							var generalExaminationTest = "";
							
							$scope.getSavedGeneralExamListTest = [];
							angular.forEach($scope.getSavedGeneralExamList, function(a) {
								var found = $scope.getSavedGeneralExamListTest.find(ar=>ar.systemId == a.systemId);
								if(found == undefined){
									$scope.getSavedGeneralExamListTest.push(a);
								}else{
									a.systemName = '';
									$scope.getSavedGeneralExamListTest.push(a);
								}
							});
							
							angular.forEach($scope.getSavedGeneralExamListTest,function(value1,key){
								generalExamination += "<table >";
								var ree = "";
								var generalExaminationTest = "";
								if(value1.systemObervationPropertyId == null){
									var ree = value1.remark;
								}
								

								if(value1.systemObervationPropertyId != null){
									generalExaminationTest +=value1.propertyName+"&nbsp;&nbsp;&nbsp;"+value1.remark;
								}
								
								if(value1.systemName == ''){
									generalExamination +="<tr><td>"+value1.observationName+"&nbsp;&nbsp;&nbsp;"+ree+"&nbsp;&nbsp;&nbsp;"+generalExaminationTest+"&nbsp;</td></tr>";//<td>"+value1.remark+"&nbsp;</td>
								}else{
									generalExamination += "<tr><th align='left'><span >"+value1.systemName+"</span>&nbsp;&nbsp;&nbsp;<span style='margin-left:40px;'>"+(value1.isNADValue =='Y'?'NAD':'')+"</span></th></tr>" +
									     "<tr><td>"+value1.observationName+"&nbsp;&nbsp;&nbsp;"+ree+"&nbsp;&nbsp;&nbsp;"+generalExaminationTest+"&nbsp;</td></tr>";//<td>"+value1.remark+"&nbsp;</td>
								}
								
								generalExamination +="</table>";
							});
							
							
							generalExamination +="<div>Descriptive Examination :<textarea rows='3'>"+$scope.descriptiveGeneralExamination+"</textarea></div>";
							var generalExaminationNew=generalExamination.replace(/null/g," ");
							}
							
							//systemic examination start

						if($scope.getSavedPhysicalExamList.length>0){
						  var physicalExamination = "";
						  var physicalExaminationTest = "";
						  
						  $scope.getSavedPhysicalExamListTest = [];
							angular.forEach($scope.getSavedPhysicalExamList, function(a) {
								var found = $scope.getSavedPhysicalExamListTest.find(ar=>ar.systemId == a.systemId);
								if(found == undefined){
									$scope.getSavedPhysicalExamListTest.push(a);
								}else{
									a.systemName = '';
									$scope.getSavedPhysicalExamListTest.push(a);
								}
							});
							
							angular.forEach($scope.getSavedPhysicalExamListTest,function(value1,key){
								physicalExamination += "<table >";
								var ree = "";
								var physicalExaminationTest = "";
								if(value1.systemObervationPropertyId == null){
									var ree = value1.remark;
								}

								if(value1.systemObervationPropertyId != null){
									physicalExaminationTest +=value1.propertyName+"&nbsp;&nbsp;&nbsp;"+value1.remark;
								}
								
								if(value1.systemName == ''){
									physicalExamination +=
			                        "<tr><td>"+value1.observationName+"&nbsp;&nbsp;&nbsp;"+ree+"&nbsp;&nbsp;&nbsp;"+physicalExaminationTest+"&nbsp;</td></tr>";//<td>"+value1.remark+"&nbsp;</td>
								}else{
									physicalExamination += "<tr><th align='left'><span >"+value1.systemName+"</span>&nbsp;&nbsp;&nbsp;<span style='margin-left:40px;'>"+(value1.isNADValue =='Y'?'NAD':'')+"</span></th></tr>" +
			                        "<tr><td>"+value1.observationName+"&nbsp;&nbsp;&nbsp;"+ree+"&nbsp;&nbsp;&nbsp;"+physicalExaminationTest+"&nbsp;</td></tr>";//<td>"+value1.remark+"&nbsp;</td>
								}
								physicalExamination += "</table >"
							});
							
							physicalExamination +="<div>Descriptive Examination :<textarea rows='3'>"+$scope.descriptivePhysicalExamination+"</textarea></div>";
							var physicalExaminationNew=physicalExamination.replace(/null/g," ");
						}
						
						if($scope.getSavedPhysicalExamList.length>0 || $scope.getSavedGeneralExamList.length>0 || $scope.addedVitalList.length > 0){
							var allExaminationData="";
							if($scope.getSavedPhysicalExamList.length>0){
								allExaminationData+="<B>Systemic Examination</B>"+physicalExaminationNew;
							}else{
								if($scope.descriptivePhysicalExamination!=undefined && $scope.descriptivePhysicalExamination!="" ){
									physicalExaminationNew="<div>Descriptive Examination :<textarea rows='3'>"+$scope.descriptivePhysicalExamination+"</textarea></div>";
									allExaminationData+="<B>Systemic Examination</B>"+physicalExaminationNew;
								}
							}
							
							if($scope.getSavedGeneralExamList.length>0){
								allExaminationData+="<B>General Examination</B>"+generalExaminationNew;
							}else{
								if($scope.descriptiveGeneralExamination!=undefined && $scope.descriptiveGeneralExamination!="" ){
									generalExaminationNew="<div>Descriptive Examination :<textarea rows='3'>"+$scope.descriptiveGeneralExamination+"</textarea></div>";
									allExaminationData+="<B>Systemic Examination</B>"+generalExaminationNew;
								}
							}
							
							$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription=allExaminationData;
						}
							$('#closeCommonDischargeSummaryModal').trigger('click');
						console.log("$scope.DischargeSummaryFinalList",$scope.DischargeSummaryFinalList);
					$('#closeCommonDischargeSummaryModal').trigger('click');
					
				}
				//Operative Details
				if($scope.fieldId==4){
					if($scope.addedOrderedProcedureList.length > 0){
						$scope.id=$scope.commonObjForAdd.dischargeFieldId;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = 0;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = 0;
						 var columnCountOperativeDetails = 0;
						angular.forEach($scope.tempListForOrd, function(value, key){
							if(value.isColumnRequired == true){
								columnCountOperativeDetails++;
							}
						});
					
						$scope.columnsnNameOperativeDetails = [];
		               angular.forEach($scope.tempListForOrd, function(value, key){
							if(value.isColumnRequired == true){
								$scope.columnsnNameOperativeDetails.push(value);
							}
							
						});
		
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = columnCountOperativeDetails;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = $scope.addedOrderedProcedureList.length;
						
						  $scope.rowListOperativeDetails = [];
							var iiiRow  = 0;
							 angular.forEach($scope.addedOrderedProcedureList, function(value, index){
								 var newArrRow = {};
								 newArrRow.coulmnId = iiiRow;
								 newArrRow.rowId = iiiRow;
								 newArrRow.specialityName = (value.specialityName!=null ? value.specialityName :"");
								 newArrRow.procedureName = (value.procedureName!=null ? value.procedureName :"");
								 newArrRow.preferedDateTime = (value.preferedDateTime!=null ? value.preferedDateTime :"");
								 newArrRow.urgency = (value.urgency == 0 ?"Normal":"Urgent");
								 newArrRow.requestReason = (value.requestReason!=null ? value.requestReason :"");
								 newArrRow.isIcuRequired = (value.isIcuRequired=='Y' ?"Yes":"No");
								 newArrRow.isBloodComponentRequired = (value.isBloodComponentRequired=='Y' ?"Yes":"No");
								 newArrRow.procedureName = (value.procedureName!=null ? value.procedureName :"");
								 newArrRow.isPacRequired = (value.isPacRequired=='Y' ?"Yes":"No");
								 newArrRow.isPrimarySecondary = (value.isPrimarySecondary=='Y'?"Primary":"Secondary");
								 $scope.rowListOperativeDetails.push(newArrRow);
								 iiiRow++;
								});
							$scope.columnListOperativeDetails = [];
							$scope.columnHeaderListOperativeDetails = []
							var iii  = 0;
							$scope.tableNameOperativeDetails = '';
							 angular.forEach($scope.columnsnNameOperativeDetails, function(value, index){
										var newArr = {};
										newArr.coulmnId = iii;
										newArr.dischSumTabColMstId = value.dischSumTabColMstId;
										newArr.columnName = value.columnName;
										newArr.tableName = value.tableName;
										$scope.tableNameOperativeDetails = value.tableName;
										newArr.inputValue = '';
										$scope.columnListOperativeDetails.push(newArr);
										$scope.columnHeaderListOperativeDetails.push(newArr);
										iii++;
								});
							$scope.savedListOperativeDetails=[];
							angular.forEach($scope.rowListOperativeDetails,function(valueOperativeDetails1,key1){
								angular.forEach($scope.columnListOperativeDetails,function(colOperativeDetails2,key2){
									var operativeProcedureData = "";
									
									if(colOperativeDetails2.dischSumTabColMstId == 56){
										operativeProcedureData =  (valueOperativeDetails1.specialityName!=null ? valueOperativeDetails1.specialityName :"");
									}
									if(colOperativeDetails2.dischSumTabColMstId == 57){
										operativeProcedureData = (valueOperativeDetails1.procedureName!=null ? valueOperativeDetails1.procedureName :"");
									}
									if(colOperativeDetails2.dischSumTabColMstId == 58){
										operativeProcedureData = (valueOperativeDetails1.preferedDateTime!=null ? valueOperativeDetails1.preferedDateTime :"");
									}
									if(colOperativeDetails2.dischSumTabColMstId == 59){
										operativeProcedureData = (valueOperativeDetails1.urgency == 0 ?"Normal":"Urgent");
									}
									if(colOperativeDetails2.dischSumTabColMstId == 60){
										operativeProcedureData = (valueOperativeDetails1.requestReason!=null ? valueOperativeDetails1.requestReason :"");
									}
									if(colOperativeDetails2.dischSumTabColMstId == 61){
										operativeProcedureData = (valueOperativeDetails1.isIcuRequired=='Y' ?"Yes":"No");
									}
									if(colOperativeDetails2.dischSumTabColMstId == 62){
										operativeProcedureData = (valueOperativeDetails1.isBloodComponentRequired=='Y' ?"Yes":"No");
									}
									if(colOperativeDetails2.dischSumTabColMstId == 63){
										operativeProcedureData = (valueOperativeDetails1.procedureName!=null ? value.procedureName :"");
									}
									if(colOperativeDetails2.dischSumTabColMstId == 64){
										operativeProcedureData = (valueOperativeDetails1.isPacRequired=='Y' ?"Yes":"No");
									}
									
									if(colOperativeDetails2.dischSumTabColMstId == 65){
										operativeProcedureData = (valueOperativeDetails1.isPrimarySecondary=='Y'?"Primary":"Secondary");
									}
								
									var data = {
											inputValue:	operativeProcedureData,
											tableTypeId :14,// Operative Details
											coulmnId:colOperativeDetails2.coulmnId,
											columnName:colOperativeDetails2.columnName,
											rowId:valueOperativeDetails1.rowId,
											rowName:operativeProcedureData,
											tableName:$scope.tableNameOperativeDetails,
											numberOfRow:$scope.addedOrderedProcedureList.length,
											numberOfCoulmn:columnCountOperativeDetails,
											organizationId:$scope.organizationId,
											unitId:$scope.unitId,
											createdBy:$scope.userId,
											updatedBy:$scope.userId,
											status:'A',
											createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
									   };
											$scope.savedListOperativeDetails.push(data);
								});
							});

						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableTypeId=15;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableNameOperativeDetails=$scope.tableNameOperativeDetails;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListOperativeDetails=$scope.rowListOperativeDetails;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListOperativeDetails=$scope.columnListOperativeDetails;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderListOperativeDetails=$scope.columnHeaderListOperativeDetails;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListOperativeDetails=$scope.savedListOperativeDetails;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeSummaryDataViewOptionIdTest=2;
						console.log("$scope.DischargeSummaryFinalList",$scope.DischargeSummaryFinalList);
						$('#closeCommonDischargeSummaryModal').trigger('click');
						}
						}
				//Push Medication Data
				if($scope.fieldId==5){
					if($scope.addedMedicationList.length > 0){
					$scope.id=$scope.commonObjForAdd.dischargeFieldId;
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = 0;
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = 0;
					 var columnCountMediAdmini = 0;
					angular.forEach($scope.columnsToPrint, function(value, key){
						if(value.isColumnRequired == true){
							columnCountMediAdmini++;
						}
					});
				
					$scope.columnsnNameMediAdmini = [];
	               angular.forEach($scope.columnsToPrint, function(value, key){
						if(value.isColumnRequired == true){
							$scope.columnsnNameMediAdmini.push(value);
						}
						
					});
	
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = columnCountMediAdmini;
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = $scope.addedMedicationList.length;
					
					  $scope.rowListMediAdmini = [];
						var iiiRow  = 0;
						 angular.forEach($scope.addedMedicationList, function(value, index){
							 var newArrRow = {};
							 newArrRow.coulmnId = iiiRow;
							 newArrRow.rowId = iiiRow;
							 var ffd = ''
							if(value.instructionDesc != null){
								ffd = value.instructionDesc;
							}else{
								ffd = value.instructionText;
							}
							 newArrRow.instructionDesc = ffd;
							 newArrRow.itemName = (value.itemName != undefined ? value.itemName :'');
							 newArrRow.genericName = value.genericName!= null ? value.genericName :"";
							 
							 
							 var strength = (value.strengthDesc != undefined && value.strengthDesc != null) ? value.strengthDesc:'';
								if(value.doseId==null){
									value.dose=value.doseDesc +" "+ strength;
								}else{
									value.dose=value.doseId+" "+value.doseUnit;
								}
								
							 newArrRow.doseDesc = value.dose!= null ? value.dose :"";
							 newArrRow.frequencyDesc = value.frequencyDesc!= null ? value.frequencyDesc :"";
							 newArrRow.roa_details = value.roa_details != null ? value.roa_details :"";
							 newArrRow.startDate = value.startDate != null ? value.startDate :"";
							 newArrRow.endDate = value.endDate != null ? value.endDate :"";
							 newArrRow.formulationType = value.formulationType!= null ? value.formulationType:"";
							 $scope.rowListMediAdmini.push(newArrRow);
							 iiiRow++;
							});
						$scope.columnListMediAdmini = [];
						$scope.columnHeaderListMediAdmini = []
						var iii  = 0;
						$scope.tableNameMediAdmini = '';
						 angular.forEach($scope.columnsnNameMediAdmini, function(value, index){
									var newArr = {};
									newArr.coulmnId = iii;
									newArr.dischSumTabColMstId = value.dischSumTabColMstId;
									newArr.columnName = value.columnName;
									newArr.tableName = value.tableName;
									$scope.tableNameMediAdmini=value.tableName;
									newArr.inputValue = '';
									$scope.columnListMediAdmini.push(newArr);
									$scope.columnHeaderListMediAdmini.push(newArr);
									iii++;
							});
						$scope.savedListMediAdmini=[];
						angular.forEach($scope.rowListMediAdmini,function(valueMediAdmini1,key1){
							angular.forEach($scope.columnListMediAdmini,function(colMediAdmini2,key2){
							      	var medicationData = "";
										if(colMediAdmini2.dischSumTabColMstId == 66){
											medicationData = valueMediAdmini1.itemName;
										}
										if(colMediAdmini2.dischSumTabColMstId == 67){
											medicationData = valueMediAdmini1.genericName;
										}
										if(colMediAdmini2.dischSumTabColMstId == 68){
											medicationData = valueMediAdmini1.formulationType;
										}
										if(colMediAdmini2.dischSumTabColMstId == 70){
											medicationData = valueMediAdmini1.doseDesc;
										}
										if(colMediAdmini2.dischSumTabColMstId == 71){
											medicationData = valueMediAdmini1.frequencyDesc;
										}
										if(colMediAdmini2.dischSumTabColMstId == 69){
											medicationData = valueMediAdmini1.roa_details;
										}
										if(colMediAdmini2.dischSumTabColMstId == 72){
											medicationData = valueMediAdmini1.startDate;
										}
										if(colMediAdmini2.dischSumTabColMstId == 73){
											medicationData = valueMediAdmini1.endDate;
										}
										if(colMediAdmini2.dischSumTabColMstId == 74){
											medicationData = valueMediAdmini1.instructionDesc;
										}
							
								var data = {
										inputValue:	medicationData,
										tableTypeId :5,// MediAdmini
										coulmnId:colMediAdmini2.coulmnId,
										columnName:colMediAdmini2.columnName,
										rowId:valueMediAdmini1.rowId,
										rowName:medicationData,
										tableName:$scope.tableNameMediAdmini,
										numberOfRow:$scope.addedMedicationList.length,
										numberOfCoulmn:columnCountMediAdmini,
										organizationId:$scope.organizationId,
										unitId:$scope.unitId,
										createdBy:$scope.userId,
										updatedBy:$scope.userId,
										status:'A',
										createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
								   };
										$scope.savedListMediAdmini.push(data);
							});
						});

					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableTypeId=15;
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListMediAdmini=$scope.rowListMediAdmini;
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableNameMediAdmini=$scope.tableNameMediAdmini;
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListMediAdmini=$scope.columnListMediAdmini;
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderListMediAdmini=$scope.columnHeaderListMediAdmini;
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListMediAdmini=$scope.savedListMediAdmini;
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeSummaryDataViewOptionIdTest=2;
					console.log("$scope.DischargeSummaryFinalList",$scope.DischargeSummaryFinalList);
					$('#closeCommonDischargeSummaryModal').trigger('click');
					}
					}
				//Surgeries/Procedures Performed 
				if($scope.fieldId==6){
					if($scope.addedSurgeryAndProcedureList.length > 0){
						$scope.id=$scope.commonObjForAdd.dischargeFieldId;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = 0;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = 0;
						 var columnCountSurgeriesProceduresPerformed = 0;
						angular.forEach($scope.surgeryAndProcedureCol, function(value, key){
							if(value.isColumnRequired == true){
								columnCountSurgeriesProceduresPerformed++;
							}
						});
					
						$scope.columnsnNameSurgeriesProceduresPerformed = [];
		               angular.forEach($scope.surgeryAndProcedureCol, function(value, key){
							if(value.isColumnRequired == true){
								$scope.columnsnNameSurgeriesProceduresPerformed.push(value);
							}
							
						});
		
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = columnCountSurgeriesProceduresPerformed;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = $scope.addedSurgeryAndProcedureList.length;
						
						  $scope.rowListSurgeriesProceduresPerformed = [];
							var iiiRow  = 0;
							 angular.forEach($scope.addedSurgeryAndProcedureList, function(value, index){
								 var newArrRow = {};
								 newArrRow.coulmnId = iiiRow;
								 newArrRow.rowId = iiiRow;
								 newArrRow.procedureName = (value.procedureName!=null ? value.procedureName :"");
								 newArrRow.preferedDateTime = (value.preferedDateTime!=null ? value.preferedDateTime :"");
								 newArrRow.requestReason = (value.requestReason!=null ? value.requestReason :"");
								 $scope.rowListSurgeriesProceduresPerformed.push(newArrRow);
								 iiiRow++;
								});
							$scope.columnListSurgeriesProceduresPerformed = [];
							$scope.columnHeaderListSurgeriesProceduresPerformed = []
							var iii  = 0;
							$scope.tableNameSurgeriesProceduresPerformed = '';
							 angular.forEach($scope.columnsnNameSurgeriesProceduresPerformed, function(value, index){
										var newArr = {};
										newArr.coulmnId = iii;
										newArr.dischSumTabColMstId = value.dischSumTabColMstId;
										newArr.columnName = value.columnName;
										newArr.tableName = value.tableName;
										$scope.tableNameSurgeriesProceduresPerformed = value.tableName;
										newArr.inputValue = '';
										$scope.columnListSurgeriesProceduresPerformed.push(newArr);
										$scope.columnHeaderListSurgeriesProceduresPerformed.push(newArr);
										iii++;
								});
							$scope.savedListSurgeriesProceduresPerformed=[];
							angular.forEach($scope.rowListSurgeriesProceduresPerformed,function(valueSurgeriesProceduresPerformed1,key1){
								angular.forEach($scope.columnListSurgeriesProceduresPerformed,function(colSurgeriesProceduresPerformed2,key2){
									var surgeryProcedureData = "";
									
									
									if(colSurgeriesProceduresPerformed2.dischSumTabColMstId == 75){
										surgeryProcedureData = (valueSurgeriesProceduresPerformed1.procedureName!=null ? valueSurgeriesProceduresPerformed1.procedureName :"");
									}
									if(colSurgeriesProceduresPerformed2.dischSumTabColMstId == 76){
										surgeryProcedureData = (valueSurgeriesProceduresPerformed1.preferedDateTime!=null ? valueSurgeriesProceduresPerformed1.preferedDateTime :"");
									}
									if(colSurgeriesProceduresPerformed2.dischSumTabColMstId == 83){
										surgeryProcedureData = (valueSurgeriesProceduresPerformed1.requestReason!=null ? valueSurgeriesProceduresPerformed1.requestReason :"");
									}
									var data = {
											inputValue:	surgeryProcedureData,
											tableTypeId :14,// Surgeries Procedures Performed
											coulmnId:colSurgeriesProceduresPerformed2.coulmnId,
											columnName:colSurgeriesProceduresPerformed2.columnName,
											rowId:valueSurgeriesProceduresPerformed1.rowId,
											rowName:surgeryProcedureData,
											tableName:$scope.tableNameSurgeriesProceduresPerformed,
											numberOfRow:$scope.addedSurgeryAndProcedureList.length,
											numberOfCoulmn:columnCountSurgeriesProceduresPerformed,
											organizationId:$scope.organizationId,
											unitId:$scope.unitId,
											createdBy:$scope.userId,
											updatedBy:$scope.userId,
											status:'A',
											createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
									   };
											$scope.savedListSurgeriesProceduresPerformed.push(data);
								});
							});

						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableTypeId=15;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableNameSurgeriesProceduresPerformed=$scope.tableNameSurgeriesProceduresPerformed;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListSurgeriesProceduresPerformed=$scope.rowListSurgeriesProceduresPerformed;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListSurgeriesProceduresPerformed=$scope.columnListSurgeriesProceduresPerformed;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderListSurgeriesProceduresPerformed=$scope.columnHeaderListSurgeriesProceduresPerformed;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListSurgeriesProceduresPerformed=$scope.savedListSurgeriesProceduresPerformed;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeSummaryDataViewOptionIdTest=2;
						console.log("$scope.DischargeSummaryFinalList",$scope.DischargeSummaryFinalList);
						$('#closeCommonDischargeSummaryModal').trigger('click');
						}
						}
				
				//add consult/Referral
				if($scope.fieldId==7){
					if($scope.addedConsultOrdersList.length > 0){
						$scope.id=$scope.commonObjForAdd.dischargeFieldId;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = 0;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = 0;
						 var columnCountConsultOrder = 0;
						angular.forEach($scope.columnsToPrint, function(value, key){
							if(value.isColumnRequired == true){
								columnCountConsultOrder++;
							}
						});
					
						$scope.columnsnNameConsultOrder = [];
		               angular.forEach($scope.columnsToPrint, function(value, key){
							if(value.isColumnRequired == true){
								$scope.columnsnNameConsultOrder.push(value);
							}
							
						});
		
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = columnCountConsultOrder;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = $scope.addedConsultOrdersList.length;
						
						  $scope.rowListConsultOrder = [];
							var iiiRow  = 0;
							 angular.forEach($scope.addedConsultOrdersList, function(value, index){
								 var newArrRow = {};
								 newArrRow.coulmnId = iiiRow;
								 newArrRow.rowId = iiiRow;
								 newArrRow.refToDoctor = (value.refToDoctor != undefined ? value.refToDoctor :'');
								 newArrRow.refByDoctor = value.refByDoctor!= null ? value.refByDoctor :"";
								 newArrRow.specialityName = value.specialityName!= null ? value.specialityName :"";
								 newArrRow.notes = value.notes!= null ? value.notes :"";
								 $scope.rowListConsultOrder.push(newArrRow);
								 iiiRow++;
								});
							$scope.columnListConsultOrder = [];
							$scope.columnHeaderListConsultOrder = []
							var iii  = 0;
							$scope.tableNameConsultOrder = '';
							 angular.forEach($scope.columnsnNameConsultOrder, function(value, index){
										var newArr = {};
										newArr.coulmnId = iii;
										newArr.dischSumTabColMstId = value.dischSumTabColMstId;
										newArr.columnName = value.columnName;
										newArr.tableName = value.tableName;
										$scope.tableNameConsultOrder = value.tableName;
										newArr.inputValue = '';
										$scope.columnListConsultOrder.push(newArr);
										$scope.columnHeaderListConsultOrder.push(newArr);
										iii++;
								});
							$scope.savedListConsultOrder=[];
							angular.forEach($scope.rowListConsultOrder,function(valueConsultOrder1,key1){
								angular.forEach($scope.columnListConsultOrder,function(colConsultOrder2,key2){
									var consultData = '';
									if(colConsultOrder2.dischSumTabColMstId == 78	){
										consultData = valueConsultOrder1.refToDoctor;
									}
									if(colConsultOrder2.dischSumTabColMstId == 79){
										consultData = valueConsultOrder1.refByDoctor;
									}
									if(colConsultOrder2.dischSumTabColMstId == 80){
										consultData = valueConsultOrder1.specialityName;
									}
									if(colConsultOrder2.dischSumTabColMstId == 81){
										consultData = valueConsultOrder1.notes;
									}
								
									var data = {
											inputValue:	consultData,
											tableTypeId :7,// MediAdmini
											coulmnId:colConsultOrder2.coulmnId,
											columnName:colConsultOrder2.columnName,
											rowId:valueConsultOrder1.rowId,
											rowName:consultData,
											tableName:$scope.tableNameConsultOrder,
											numberOfRow:$scope.addedConsultOrdersList.length,
											numberOfCoulmn:columnCountConsultOrder,
											organizationId:$scope.organizationId,
											unitId:$scope.unitId,
											createdBy:$scope.userId,
											updatedBy:$scope.userId,
											status:'A',
											createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
									   };
											$scope.savedListConsultOrder.push(data);
								});
							});

						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableTypeId=7;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListConsultOrder=$scope.rowListConsultOrder;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableNameConsultOrder=$scope.tableNameConsultOrder;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListConsultOrder=$scope.columnListConsultOrder;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderListConsultOrder=$scope.columnHeaderListConsultOrder;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListConsultOrder=$scope.savedListConsultOrder;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeSummaryDataViewOptionIdTest=2;
						console.log("$scope.DischargeSummaryFinalList",$scope.DischargeSummaryFinalList);
						$('#closeCommonDischargeSummaryModal').trigger('click');
						}
						}
				//add diagnosis data
				if($scope.fieldId==8){
					if($scope.addedDiagnosisList.length > 0){
						$scope.id=$scope.commonObjForAdd.dischargeFieldId;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = 0;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = 0;
						 var columnCountDiagnosis = 0;
						angular.forEach($scope.columnsToPrint, function(value, key){
							if(value.isColumnRequired == true){
								columnCountDiagnosis++;
							}
						});
					
						$scope.columnsnNameDiagnosis = [];
		               angular.forEach($scope.columnsToPrint, function(value, key){
							if(value.isColumnRequired == true){
								$scope.columnsnNameDiagnosis.push(value);
							}
							
						});
		
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = columnCountDiagnosis;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = $scope.addedDiagnosisList.length;
						
						  $scope.rowListDiagnosis = [];
							var iiiRow  = 0;
							 angular.forEach($scope.addedDiagnosisList, function(value, index){
								 var newArrRow = {};
								 newArrRow.coulmnId = iiiRow;
								 newArrRow.rowId = iiiRow;
								 newArrRow.code = (value.code != undefined ? value.code :'');
								 newArrRow.isComorbidity = value.isComorbidity!= null ? value.isComorbidity :"";
								 newArrRow.diagnosisName = value.diagnosisName!= null ? value.diagnosisName :"";
								 newArrRow.diagnosisType = value.diagnosisType!= null ? value.diagnosisType :"";
								 newArrRow.remark = value.remark!= null ? value.remark :"";
								 $scope.rowListDiagnosis.push(newArrRow);
								 iiiRow++;
								});
							$scope.columnListDiagnosis = [];
							$scope.columnHeaderListDiagnosis = []
							var iii  = 0;
							$scope.tableNameDiagnosis = '';
							 angular.forEach($scope.columnsnNameDiagnosis, function(value, index){
										var newArr = {};
										newArr.coulmnId = iii;
										newArr.dischSumTabColMstId = value.dischSumTabColMstId;
										newArr.columnName = value.columnName;
										newArr.tableName = value.tableName;
										$scope.tableNameDiagnosis = value.tableName;
										newArr.inputValue = '';
										$scope.columnListDiagnosis.push(newArr);
										$scope.columnHeaderListDiagnosis.push(newArr);
										iii++;
								});
							$scope.savedListDiagnosis=[];
							angular.forEach($scope.rowListDiagnosis,function(valueDiagnosis1,key1){
								angular.forEach($scope.columnListDiagnosis,function(colDiagnosis2,key2){
									 var diagnosisData = '';
									
									if(colDiagnosis2.dischSumTabColMstId == 85){
										diagnosisData = (valueDiagnosis1.code!= null ? valueDiagnosis1.code :"");
									}
									if(colDiagnosis2.dischSumTabColMstId == 88){
										diagnosisData = (valueDiagnosis1.isComorbidity);
									}
									if(colDiagnosis2.dischSumTabColMstId == 86){
										diagnosisData = (valueDiagnosis1.diagnosisName!= null ? valueDiagnosis1.diagnosisName :"");
									}
									if(colDiagnosis2.dischSumTabColMstId == 87){
										diagnosisData = (valueDiagnosis1.diagnosisType);
									}
									if(colDiagnosis2.dischSumTabColMstId == 89){
										diagnosisData = (valueDiagnosis1.remark!= null ? valueDiagnosis1.remark :" ");
									}
								
									var data = {
											inputValue:	diagnosisData,
											tableTypeId :19,// MediAdmini
											coulmnId:colDiagnosis2.coulmnId,
											columnName:colDiagnosis2.columnName,
											rowId:valueDiagnosis1.rowId,
											rowName:diagnosisData,
											tableName:$scope.tableNameDiagnosis,
											numberOfRow:$scope.addedDiagnosisList.length,
											numberOfCoulmn:columnCountDiagnosis,
											organizationId:$scope.organizationId,
											unitId:$scope.unitId,
											createdBy:$scope.userId,
											updatedBy:$scope.userId,
											status:'A',
											createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
									   };
											$scope.savedListDiagnosis.push(data);
								});
							});

						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableTypeId=19;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListDiagnosis=$scope.rowListDiagnosis;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableNameDiagnosis=$scope.tableNameDiagnosis;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListDiagnosis=$scope.columnListDiagnosis;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderListDiagnosis=$scope.columnHeaderListDiagnosis;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListDiagnosis=$scope.savedListDiagnosis;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeSummaryDataViewOptionIdTest=2;
						console.log("$scope.DischargeSummaryFinalList",$scope.DischargeSummaryFinalList);
						$('#closeCommonDischargeSummaryModal').trigger('click');
						}
						}
				//Get addmission reason list
				if($scope.fieldId==9){
					if($scope.addedAdmisssionReasonList.length > 0){
						$scope.id=$scope.commonObjForAdd.dischargeFieldId;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = 0;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = 0;
						 var columnCountAdmisssionReason = 0;
						angular.forEach($scope.columnsToPrint, function(value, key){
							if(value.isColumnRequired == true){
								columnCountAdmisssionReason++;
							}
						});
					
						$scope.columnsnNameAdmisssionReason = [];
		               angular.forEach($scope.columnsToPrint, function(value, key){
							if(value.isColumnRequired == true){
								$scope.columnsnNameAdmisssionReason.push(value);
							}
							
						});
		
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = columnCountAdmisssionReason;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = $scope.addedAdmisssionReasonList.length;
						
						  $scope.rowListAdmisssionReason = [];
							var iiiRow  = 0;
							 angular.forEach($scope.addedAdmisssionReasonList, function(value, index){
								 var newArrRow = {};
								 newArrRow.coulmnId = iiiRow;
								 newArrRow.rowId = iiiRow;
								 newArrRow.admissionReason = (value.admissionReason != undefined ? value.admissionReason :'');
								 $scope.rowListAdmisssionReason.push(newArrRow);
								 iiiRow++;
								});
							$scope.columnListAdmisssionReason = [];
							$scope.columnHeaderListAdmisssionReason = []
							var iii  = 0;
							$scope.tableNameAdmisssionReason = '';
							 angular.forEach($scope.columnsnNameAdmisssionReason, function(value, index){
										var newArr = {};
										newArr.coulmnId = iii;
										newArr.dischSumTabColMstId = value.dischSumTabColMstId;
										newArr.columnName = value.columnName;
										newArr.tableName = value.tableName;
										$scope.tableNameAdmisssionReason = value.tableName;
										newArr.inputValue = '';
										$scope.columnListAdmisssionReason.push(newArr);
										$scope.columnHeaderListAdmisssionReason.push(newArr);
										iii++;
								});
							$scope.savedListAdmisssionReason=[];
							angular.forEach($scope.rowListAdmisssionReason,function(valueAdmisssionReason1,key1){
								angular.forEach($scope.columnListAdmisssionReason,function(colAdmisssionReason2,key2){
									var addmisionData = '';
									if(colAdmisssionReason2.dischSumTabColMstId == 90){
										addmisionData = valueAdmisssionReason1.admissionReason;
									}
								
									var data = {
											inputValue:	addmisionData,
											tableTypeId :20,// MediAdmini
											coulmnId:colAdmisssionReason2.coulmnId,
											columnName:colAdmisssionReason2.columnName,
											rowId:valueAdmisssionReason1.rowId,
											rowName:addmisionData,
											tableName:$scope.tableNameAdmisssionReason,
											numberOfRow:$scope.addedAdmisssionReasonList.length,
											numberOfCoulmn:columnCountAdmisssionReason,
											organizationId:$scope.organizationId,
											unitId:$scope.unitId,
											createdBy:$scope.userId,
											updatedBy:$scope.userId,
											status:'A',
											createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
									   };
											$scope.savedListAdmisssionReason.push(data);
								});
							});

						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableTypeId=20;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListAdmisssionReason=$scope.rowListAdmisssionReason;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableNameAdmisssionReason=$scope.tableNameAdmisssionReason;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListAdmisssionReason=$scope.columnListAdmisssionReason;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderListAdmisssionReason=$scope.columnHeaderListAdmisssionReason;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListAdmisssionReason=$scope.savedListAdmisssionReason;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeSummaryDataViewOptionIdTest=2;
						console.log("$scope.DischargeSummaryFinalList",$scope.DischargeSummaryFinalList);
						$('#closeCommonDischargeSummaryModal').trigger('click');
						}
						}
				//Investigation Data
				if($scope.fieldId==10){
					
					if($scope.addedInvestigationList.length > 0){
						$scope.id=$scope.commonObjForAdd.dischargeFieldId;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = 0;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = 0;
						
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListInvestigation=[];
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListInvestigation=[];
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderListInvestigation =[];
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListInvestigation = [];
						
						 var columnCountInvestigation = 0;
						angular.forEach($scope.finalCurrentEncounterOrderListsColumn, function(value, key){
							if(value.isColumnRequired == true){
								columnCountInvestigation++;
							}
						});
						$scope.columnsnNameInvestigation = [];
		               angular.forEach($scope.finalCurrentEncounterOrderListsColumn, function(value, key){
							if(value.isColumnRequired == true){
								$scope.columnsnNameInvestigation.push(value);
							}
							
						});
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = columnCountInvestigation;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = $scope.addedInvestigationList.length;
						$scope.rowListInvestigation = [];
							var iiiRow  = 0;
							 angular.forEach($scope.addedInvestigationList, function(invest, index){
								 var newArrRow = {};
								 newArrRow.coulmnId = iiiRow;
								 newArrRow.rowId = iiiRow;
								 newArrRow.createdDate = invest.createdDate != null ? invest.createdDate :"";
								 newArrRow.specialityName = invest.specialityName!= null ? invest.specialityName :"";
								 newArrRow.serviceStandardName = invest.serviceStandardName!= null ? invest.serviceStandardName :"";
								 newArrRow.serviceStandardCode = invest.serviceStandardCode!= null ? invest.serviceStandardCode :"";
								 newArrRow.statusName = ((invest.statusName==null)?((invest.serviceRendered==1)?"Completed":"Pending"):invest.statusName);
								 $scope.rowListInvestigation.push(newArrRow);
								 iiiRow++;
								});
						    $scope.columnListInvestigation = [];
						    $scope.columnHeaderListInvestigation  = [];
							
							var iii  = 0;
                             $scope.tableNameInvestigation = '';
							 angular.forEach($scope.columnsnNameInvestigation, function(treatment2, index){
										var newArr = {};
										newArr.coulmnId = iii;
										newArr.dischSumTabColMstId = treatment2.dischSumTabColMstId;
										newArr.columnName = treatment2.columnName;
										newArr.tableName = treatment2.tableName;
										$scope.tableNameInvestigation = treatment2.tableName;
										newArr.inputValue = '';
										$scope.columnListInvestigation.push(newArr);
										$scope.columnHeaderListInvestigation .push(newArr);
										iii++;
								});
							$scope.savedListInvestigation=[];
							angular.forEach($scope.rowListInvestigation,function(rowInvestigation1,key1){
								angular.forEach($scope.columnListInvestigation,function(colInvestigation1,key2){
									
									var investigationData = '';
									
									if(colInvestigation1.dischSumTabColMstId == 91){
										investigationData = rowInvestigation1.createdDate;
									}
									if(colInvestigation1.dischSumTabColMstId == 92){
										investigationData = rowInvestigation1.specialityName;
									}
									if(colInvestigation1.dischSumTabColMstId == 93){
										investigationData = rowInvestigation1.serviceStandardName;
									}
									if(colInvestigation1.dischSumTabColMstId == 94){
										investigationData = rowInvestigation1.serviceStandardCode;
									}
									if(colInvestigation1.dischSumTabColMstId == 95){
										investigationData = rowInvestigation1.statusName;
									}
							
									var data = {
											inputValue:investigationData	,
											tableTypeId :21,// tretment history
											coulmnId:colInvestigation1.coulmnId,
											columnName:colInvestigation1.columnName,
											rowId:rowInvestigation1.rowId,
											rowName:investigationData,
											tableName:$scope.tableNameInvestigation,
											numberOfRow:$scope.addedInvestigationList.length,
											numberOfCoulmn:columnCountInvestigation,
											organizationId:$scope.organizationId,
											unitId:$scope.unitId,
											createdBy:$scope.userId,
											updatedBy:$scope.userId,
											status:'A',
											createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
									   };
											$scope.savedListInvestigation.push(data);
								});   
							});

						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableTypeId=21;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListInvestigation=$scope.rowListInvestigation;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableNameInvestigation=$scope.tableNameInvestigation;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListInvestigation=$scope.columnListInvestigation;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderListInvestigation =$scope.columnHeaderListInvestigation ;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListInvestigation = $scope.savedListInvestigation;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeSummaryDataViewOptionIdTest=2;
						
					}
					
					
					if($scope.treatmentHistoryList1.length > 0){
						$scope.id=$scope.commonObjForAdd.dischargeFieldId;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = 0;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = 0;
						
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListTreatmentHistory=[];
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListTreatmentHistory=[];
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderListTreatmentHistory =[];
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListTreatmentHistory = [];
						
						 var columnCountTreatmentHistory = 0;
						angular.forEach($scope.treatmentHistoryCurrentListColumn, function(value, key){
							if(value.isColumnRequired == true){
								columnCountTreatmentHistory++;
							}
						});
						$scope.columnsnNameTreatmentHistory = [];
		               angular.forEach($scope.treatmentHistoryCurrentListColumn, function(value, key){
							if(value.isColumnRequired == true){
								$scope.columnsnNameTreatmentHistory.push(value);
							}
							
						});
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = columnCountTreatmentHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = $scope.treatmentHistoryList1.length;
						$scope.rowListTreatmentHistory = [];
							var iiiRow  = 0;
							 angular.forEach($scope.treatmentHistoryList1, function(tretment, index){
								 var newArrRow = {};
								 newArrRow.coulmnId = iiiRow;
								 newArrRow.rowId = iiiRow;
								 newArrRow.treatementName = tretment.treatementName != null ? tretment.treatementName :"";
								 newArrRow.treatDate = tretment.treatDate!= null ? tretment.treatDate :"";
								 newArrRow.remark = tretment.remark!= null ? tretment.remark :"";
								 $scope.rowListTreatmentHistory.push(newArrRow);
								 iiiRow++;
								});
						    $scope.columnListTreatmentHistory = [];
						    $scope.columnHeaderListTreatmentHistory  = [];
							
							var iii  = 0;
							$scope.tableNameTreatmentHistory = '';
							 angular.forEach($scope.columnsnNameTreatmentHistory, function(treatment2, index){
										var newArr = {};
										newArr.coulmnId = iii;
										newArr.dischSumTabColMstId = treatment2.dischSumTabColMstId;
										newArr.columnName = treatment2.columnName;
										newArr.tableName = treatment2.tableName;
										$scope.tableNameTreatmentHistory = treatment2.tableName;
										newArr.inputValue = '';
										$scope.columnListTreatmentHistory.push(newArr);
										$scope.columnHeaderListTreatmentHistory .push(newArr);
										iii++;
								});
							$scope.savedListTreatmentHistory=[];
							angular.forEach($scope.rowListTreatmentHistory,function(rowTreatment1,key1){
								angular.forEach($scope.columnListTreatmentHistory,function(colTreatment1,key2){
									  var input = "";
											
											if(colTreatment1.dischSumTabColMstId == 96	){
												input = rowTreatment1.treatDate;
											}
											if(colTreatment1.dischSumTabColMstId == 97){
												input = rowTreatment1.treatementName;
											}
											if(colTreatment1.dischSumTabColMstId == 98){
												input = rowTreatment1.remark;
											}
							
									var data = {
											inputValue:input	,
											tableTypeId :22,// tretment history
											coulmnId:colTreatment1.coulmnId,
											columnName:colTreatment1.columnName,
											rowId:rowTreatment1.rowId,
											rowName:input,
											tableName:$scope.tableNameTreatmentHistory,
											numberOfRow:$scope.treatmentHistoryList1.length,
											numberOfCoulmn:columnCountTreatmentHistory,
											organizationId:$scope.organizationId,
											unitId:$scope.unitId,
											createdBy:$scope.userId,
											updatedBy:$scope.userId,
											status:'A',
											createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
									   };
											$scope.savedListTreatmentHistory.push(data);
								});   
							});

						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableTypeId=22;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListTreatmentHistory=$scope.rowListTreatmentHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableNameTreatmentHistory=$scope.tableNameTreatmentHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListTreatmentHistory=$scope.columnListTreatmentHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderListTreatmentHistory =$scope.columnHeaderListTreatmentHistory ;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListTreatmentHistory = $scope.savedListTreatmentHistory;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeSummaryDataViewOptionIdTest=2;
						
					}
					
					console.log("$scope.DischargeSummaryFinalList",$scope.DischargeSummaryFinalList);
					$('#closeCommonDischargeSummaryModal').trigger('click');
				}
				
				//Transfer of care
				if($scope.fieldId==11){

					if($scope.addedTransferOfCareList.length > 0){
						$scope.id=$scope.commonObjForAdd.dischargeFieldId;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = 0;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = 0;
						 var columnCountTransferOfCare = 0;
						angular.forEach($scope.columnsToPrint, function(value, key){
							if(value.isColumnRequired == true){
								columnCountTransferOfCare++;
							}
						});
					
						$scope.columnsnNameTransferOfCare = [];
		               angular.forEach($scope.columnsToPrint, function(value, key){
							if(value.isColumnRequired == true){
								$scope.columnsnNameTransferOfCare.push(value);
							}
							
						});
		
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = columnCountTransferOfCare;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = $scope.addedTransferOfCareList.length;
						
						  $scope.rowListTransferOfCare = [];
							var iiiRow  = 0;
							 angular.forEach($scope.addedTransferOfCareList, function(value, index){
								 var newArrRow = {};
								 newArrRow.coulmnId = iiiRow;
								 newArrRow.rowId = iiiRow;
								 newArrRow.note = (value.note != undefined ? value.note :'');
								 $scope.rowListTransferOfCare.push(newArrRow);
								 iiiRow++;
								});
							$scope.columnListTransferOfCare = [];
							$scope.columnHeaderListTransferOfCare = []
							var iii  = 0;
							$scope.tableNameTransferOfCare = '';
							 angular.forEach($scope.columnsnNameTransferOfCare, function(value, index){
										var newArr = {};
										newArr.coulmnId = iii;
										newArr.dischSumTabColMstId = value.dischSumTabColMstId;
										newArr.columnName = value.columnName;
										newArr.tableName = value.tableName;
										$scope.tableNameTransferOfCare = value.tableName;
										newArr.inputValue = '';
										$scope.columnListTransferOfCare.push(newArr);
										$scope.columnHeaderListTransferOfCare.push(newArr);
										iii++;
								});
							$scope.savedListTransferOfCare=[];
							angular.forEach($scope.rowListTransferOfCare,function(valueTransferOfCare1,key1){
								angular.forEach($scope.columnListTransferOfCare,function(colTransferOfCare2,key2){
									var transferOfCareData = '';
									if(colTransferOfCare2.dischSumTabColMstId == 99){
										transferOfCareData = (valueTransferOfCare1.note !=null ? valueTransferOfCare1.note :"");
									}
									
								
									var data = {
											inputValue:	transferOfCareData,
											tableTypeId :23,// transfer of care
											coulmnId:colTransferOfCare2.coulmnId,
											columnName:colTransferOfCare2.columnName,
											rowId:valueTransferOfCare1.rowId,
											rowName:transferOfCareData,
											tableName:$scope.tableNameTransferOfCare,
											numberOfRow:$scope.addedTransferOfCareList.length,
											numberOfCoulmn:columnCountTransferOfCare,
											organizationId:$scope.organizationId,
											unitId:$scope.unitId,
											createdBy:$scope.userId,
											updatedBy:$scope.userId,
											status:'A',
											createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
									   };
											$scope.savedListTransferOfCare.push(data);
								});
							});

						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableTypeId=7;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListTransferOfCare=$scope.rowListTransferOfCare;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableNameTransferOfCare=$scope.tableNameTransferOfCare;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListTransferOfCare=$scope.columnListTransferOfCare;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderListTransferOfCare=$scope.columnHeaderListTransferOfCare;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListTransferOfCare=$scope.savedListTransferOfCare;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeSummaryDataViewOptionIdTest=2;
						console.log("$scope.DischargeSummaryFinalList",$scope.DischargeSummaryFinalList);
						$('#closeCommonDischargeSummaryModal').trigger('click');
						}
						
				}
				//add  discharge Medications
				if($scope.fieldId==12){
					if($scope.addedDischargeMedicationlist.length > 0){
						$scope.id=$scope.commonObjForAdd.dischargeFieldId;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = 0;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = 0;
						 var columnCountDischargeMedication = 0;
						angular.forEach($scope.columnsToPrint, function(value, key){
							if(value.isColumnRequired == true){
								columnCountDischargeMedication++;
							}
						});
					
						$scope.columnsnNameDischargeMedication = [];
		               angular.forEach($scope.columnsToPrint, function(value, key){
							if(value.isColumnRequired == true){
								$scope.columnsnNameDischargeMedication.push(value);
							}
							
						});
		
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = columnCountDischargeMedication;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = $scope.addedDischargeMedicationlist.length;
						
						  $scope.rowListDischargeMedication = [];
							var iiiRow  = 0;
							 angular.forEach($scope.addedDischargeMedicationlist, function(value, index){
								 var newArrRow = {};
								 newArrRow.coulmnId = iiiRow;
								 newArrRow.rowId = iiiRow;
								 if(value.instructionDesc != null){
									value.instructionDesc = value.instructionDesc;
								 }else{
									value.instructionDesc = value.instructionText;
								 }
								 
								 newArrRow.itemName = (value.itemName!=null ? value.itemName :"");
								 newArrRow.genericName = (value.genericName!=null ? value.genericName :"");
								 newArrRow.doseDesc = (value.doseDesc);
								 newArrRow.frequencyDesc =(value.frequencyDesc != null ? value.frequencyDesc :"");
								 newArrRow.duration = (value.duration!=null ? value.duration :"");
								 newArrRow.roa_details = (value.roa_details!=null ? value.roa_details :"");
								 newArrRow.quantity = (value.quantity!=null ? value.quantity :"");
								 newArrRow.duration = (value.duration!=null ? value.duration :"");
								 newArrRow.instructionDesc = (value.instructionDesc!=null ? value.instructionDesc :"");
								 $scope.rowListDischargeMedication.push(newArrRow);
								 iiiRow++;
								});
							$scope.columnListDischargeMedication = [];
							$scope.columnHeaderListDischargeMedication = []
							var iii  = 0;
							$scope.tableNameDischargeMedication = '';
							 angular.forEach($scope.columnsnNameDischargeMedication, function(value, index){
										var newArr = {};
										newArr.coulmnId = iii;
										newArr.dischSumTabColMstId = value.dischSumTabColMstId;
										newArr.columnName = value.columnName;
										newArr.tableName = value.tableName;
										$scope.tableNameDischargeMedication = value.tableName;
										newArr.inputValue = '';
										$scope.columnListDischargeMedication.push(newArr);
										$scope.columnHeaderListDischargeMedication.push(newArr);
										iii++;
								});
							$scope.savedListDischargeMedication=[];
							angular.forEach($scope.rowListDischargeMedication,function(valueDischargeMedication1,key1){
								angular.forEach($scope.columnListDischargeMedication,function(colDischargeMedication2,key2){
									var dischargeMedicationData = "";
									
									if(colDischargeMedication2.dischSumTabColMstId == 100	){
										dischargeMedicationData =valueDischargeMedication1.itemName;
									}
									if(colDischargeMedication2.dischSumTabColMstId == 101){
										dischargeMedicationData = valueDischargeMedication1.genericName;
									}
									if(colDischargeMedication2.dischSumTabColMstId == 102){
										dischargeMedicationData = valueDischargeMedication1.doseDesc ;
									}
									if(colDischargeMedication2.dischSumTabColMstId == 103){
										dischargeMedicationData = valueDischargeMedication1.frequencyDesc;
									}
									if(colDischargeMedication2.dischSumTabColMstId == 104){
										dischargeMedicationData = valueDischargeMedication1.duration;
									}
									if(colDischargeMedication2.dischSumTabColMstId == 105){
										dischargeMedicationData = valueDischargeMedication1.roa_details;
									}
									if(colDischargeMedication2.dischSumTabColMstId == 106){
										dischargeMedicationData = valueDischargeMedication1.quantity;
									}
									if(colDischargeMedication2.dischSumTabColMstId == 107){
										dischargeMedicationData = valueDischargeMedication1.instructionDesc;
									}
								
									var data = {
											inputValue:	dischargeMedicationData,
											tableTypeId :24,// discharge  medicarion
											coulmnId:colDischargeMedication2.coulmnId,
											columnName:colDischargeMedication2.columnName,
											rowId:valueDischargeMedication1.rowId,
											rowName:dischargeMedicationData,
											tableName:$scope.tableNameDischargeMedication,
											numberOfRow:$scope.addedDischargeMedicationlist.length,
											numberOfCoulmn:columnCountDischargeMedication,
											organizationId:$scope.organizationId,
											unitId:$scope.unitId,
											createdBy:$scope.userId,
											updatedBy:$scope.userId,
											status:'A',
											createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
									   };
											$scope.savedListDischargeMedication.push(data);
								});
							});

						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableTypeId=15;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableNameDischargeMedication=$scope.tableNameDischargeMedication;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListDischargeMedication=$scope.rowListDischargeMedication;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListDischargeMedication=$scope.columnListDischargeMedication;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderListDischargeMedication=$scope.columnHeaderListDischargeMedication;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListDischargeMedication=$scope.savedListDischargeMedication;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeSummaryDataViewOptionIdTest=2;
						console.log("$scope.DischargeSummaryFinalList",$scope.DischargeSummaryFinalList);
						$('#closeCommonDischargeSummaryModal').trigger('click');
						}
						}
				//Get complaint list
				if($scope.fieldId==13){
					if($scope.addedComplaintList.length > 0){
					$scope.id=$scope.commonObjForAdd.dischargeFieldId;
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = 0;
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = 0;
					 var columnCount = 0;
					angular.forEach($scope.allComplaintListCol, function(value, key){
						if(value.isColumnRequired == true){
							columnCount++;
						}
					});
				
					$scope.columnsnName = [];
	               angular.forEach($scope.allComplaintListCol, function(value, key){
						
						if(value.isColumnRequired == true){
							$scope.columnsnName.push(value);
						}
						
					});
	
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = columnCount;
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = $scope.addedComplaintList.length;
					
					$scope.rowList = [];
						$scope.rowList = [];
						var iiiRow  = 0;
						 angular.forEach($scope.addedComplaintList, function(value, index){
							 var newArrRow = {};
							 newArrRow.coulmnId = iiiRow;
							 newArrRow.rowId = iiiRow;
							 newArrRow.rowName = value.createdDate!= null ? value.createdDate :"";
							 newArrRow.createdDate = value.createdDate!= null ? value.createdDate :"";
							 newArrRow.icpcSystemDesc = value.icpcSystemDesc!= null ? value.icpcSystemDesc :"";
							 newArrRow.icpcCode = value.icpcCode!= null ? value.icpcCode :"";
							 newArrRow.icpcCodeText = value.icpcCodeText!= null ? value.icpcCodeText :"";
							 newArrRow.since = value.since!= null ? value.since :"";
							 newArrRow.remark = value.remark != null ? value.remark :"";
							 newArrRow.inputValue = value.createdDate!= null ? value.createdDate :"";
							 $scope.rowList.push(newArrRow);
							 iiiRow++;
							});
					    $scope.columnList = [];
					    $scope.columnHeaderList = [];
						$scope.columnList = [];
						$scope.columnHeaderList = []
						var iii  = 0;
						$scope.tableNameComplaint = '';
						 angular.forEach($scope.columnsnName, function(value, index){
									var newArr = {};
									newArr.coulmnId = iii;
									newArr.dischSumTabColMstId = value.dischSumTabColMstId;
									newArr.columnName = value.columnName;
									newArr.tableName = value.tableName;
									$scope.tableNameComplaint = value.tableName;
									newArr.inputValue = '';
									$scope.columnList.push(newArr);
									$scope.columnHeaderList.push(newArr);
									iii++;
							});
						console.log("$scope.columnList",$scope.columnList );
						$scope.savedList=[];
						angular.forEach($scope.rowList,function(value1,key1){
							angular.forEach($scope.columnList,function(value2,key2){
								var input = "";
								if(value2.dischSumTabColMstId == 108){
									input = value1.createdDate!= null ? value1.createdDate :"";
								}
								if(value2.dischSumTabColMstId == 109){
									input = value1.icpcSystemDesc!= null ? value1.icpcSystemDesc :"";
								}
								if(value2.dischSumTabColMstId == 110){
									input = value1.icpcCode!= null ? value1.icpcCode :"";
								}
								if(value2.dischSumTabColMstId == 111){
									input = value1.icpcCodeText!= null ? value1.icpcCodeText :"";
								}
								if(value2.dischSumTabColMstId == 112){
									input = value1.since!= null ? value1.since :"";
								}
								if(value2.dischSumTabColMstId == 113){
									input = value1.remark != null ? value1.remark :"";
								}
								var data = {
										inputValue:	input,
										tableTypeId :1,// complaint
										coulmnId:value2.coulmnId,
										columnName:value2.columnName,
										rowId:value1.rowId,
										rowName:value1.rowName,
										tableName:$scope.tableNameComplaint,
										numberOfRow:$scope.addedComplaintList.length,
										numberOfCoulmn:columnCount,
										organizationId:$scope.organizationId,
										unitId:$scope.unitId,
										createdBy:$scope.userId,
										updatedBy:$scope.userId,
										status:'A',
										createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
								   };
										$scope.savedList.push(data);
							});
						});

					console.log("$scope.savedList",$scope.savedList );
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableTypeId=1;
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowList=[];
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnList=[];
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderList=[];
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowList=$scope.rowList;
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableNameComplaint=$scope.tableNameComplaint;
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnList=$scope.columnList;
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderList=$scope.columnHeaderList;
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedList=$scope.savedList;
					$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeSummaryDataViewOptionIdTest=$scope.dischargeSummaryDataViewOptionIdTest;
					
					}
					$('#closeCommonDischargeSummaryModal').trigger('click');
				}
				
				
				if($scope.fieldId == 18){

					if($scope.addedInstructions.length > 0){
						$scope.id=$scope.commonObjForAdd.dischargeFieldId;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = 0;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = 0;
						 var columnCountInstructions = 0;
						angular.forEach($scope.adviceListCol.reverse(), function(value, key){
							if(value.isColumnRequired == true){
								columnCountInstructions++;
							}
						});
					
						$scope.columnsnNameInstructions = [];
		               angular.forEach($scope.adviceListCol.reverse(), function(value, key){
							if(value.isColumnRequired == true){
								$scope.columnsnNameInstructions.push(value);
							}
							
						});
		
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = columnCountInstructions;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = $scope.addedInstructions.length;
						
						  $scope.rowListInstructions = [];
							var iiiRow  = 0;
							 angular.forEach($scope.addedInstructions, function(value, index){
								 var newArrRow = {};
								 newArrRow.coulmnId = iiiRow;
								 newArrRow.rowId = iiiRow;
								 if(value.instructionDesc != undefined &&  value.instructionDesc != null ){
									 newArrRow.instructionDesc = (value.instructionDesc != undefined ? value.instructionDesc :'');
								 }else{
									 newArrRow.instructionDesc = (value.advicedInstruction != undefined ? value.advicedInstruction :'');
								 }
								 newArrRow.notes = (value.notes != undefined ? value.notes :'');
								 $scope.rowListInstructions.push(newArrRow);
								 iiiRow++;
								});
							$scope.columnListInstructions = [];
							$scope.columnHeaderListInstructions = []
							var iii  = 0;
							$scope.tabelNameInstructions = '';
							 angular.forEach($scope.columnsnNameInstructions, function(value, index){
										var newArr = {};
										newArr.coulmnId = iii;
										newArr.dischSumTabColMstId = value.dischSumTabColMstId;
										newArr.columnName = value.columnName;
										newArr.tableName = value.tableName;
										$scope.tabelNameInstructions = value.tableName;
										newArr.inputValue = '';
										$scope.columnListInstructions.push(newArr);
										$scope.columnHeaderListInstructions.push(newArr);
										iii++;
								});
							$scope.savedListInstructions=[];
							angular.forEach($scope.rowListInstructions,function(valueInstructions1,key1){
								angular.forEach($scope.columnListInstructions,function(colInstructions2,key2){
									var instructionData = "";
									
									if(colInstructions2.dischSumTabColMstId == 114){
										instructionData =(valueInstructions1.instructionDesc);
									}
									if(colInstructions2.dischSumTabColMstId == 115){
										instructionData =(valueInstructions1.notes);
									}
								
									var data = {
											inputValue:	instructionData,
											tableTypeId :26,// Advice
											coulmnId:colInstructions2.coulmnId,
											columnName:colInstructions2.columnName,
											rowId:valueInstructions1.rowId,
											rowName:instructionData,
											tableName:$scope.tabelNameInstructions,
											numberOfRow:$scope.addedInstructions.length,
											numberOfCoulmn:columnCountInstructions,
											organizationId:$scope.organizationId,
											unitId:$scope.unitId,
											createdBy:$scope.userId,
											updatedBy:$scope.userId,
											status:'A',
											createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
									   };
											$scope.savedListInstructions.push(data);
								});
							});

						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableTypeId=7;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListInstructions=$scope.rowListInstructions;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListInstructions=$scope.columnListInstructions;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tabelNameInstructions=$scope.tabelNameInstructions;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderListInstructions=$scope.columnHeaderListInstructions;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListInstructions=$scope.savedListInstructions;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeSummaryDataViewOptionIdTest=2;
						console.log("$scope.DischargeSummaryFinalList",$scope.DischargeSummaryFinalList);
						$('#closeCommonDischargeSummaryModal').trigger('click');
						}
						
					if($scope.addedFollowupAppointment.length > 0){
						$scope.id=$scope.commonObjForAdd.dischargeFieldId;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = 0;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = 0;
						 var columnCountfollowUp = 0;
						angular.forEach($scope.followupCol, function(value, key){
							if(value.isColumnRequired == true){
								columnCountfollowUp++;
							}
						});
					
						$scope.columnsnNamefollowUp = [];
		               angular.forEach($scope.followupCol, function(value, key){
							if(value.isColumnRequired == true){
								$scope.columnsnNamefollowUp.push(value);
							}
							
						});
		
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = columnCountfollowUp;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = $scope.addedFollowupAppointment.length;
						
						  $scope.rowListfollowUp = [];
							var iiiRow  = 0;
							 angular.forEach($scope.addedFollowupAppointment, function(value, index){
								 var newArrRow = {};
								 newArrRow.coulmnId = iiiRow;
								 newArrRow.rowId = iiiRow;
								 newArrRow.appointmentDate = (value.appointmentDate != undefined ? value.appointmentDate :'');
								 newArrRow.fromTimeString = (value.fromTimeString != undefined ? value.fromTimeString :'');
								 $scope.rowListfollowUp.push(newArrRow);
								 iiiRow++;
								});
							$scope.columnListfollowUp = [];
							$scope.columnHeaderListfollowUp = []
							var iii  = 0;
							$scope.tabelNamefollowUp = '';
							 angular.forEach($scope.columnsnNamefollowUp, function(value, index){
										var newArr = {};
										newArr.coulmnId = iii;
										newArr.dischSumTabColMstId = value.dischSumTabColMstId;
										newArr.columnName = value.columnName;
										newArr.tableName = value.tableName;
										$scope.tabelNamefollowUp = value.tableName;
										newArr.inputValue = '';
										$scope.columnListfollowUp.push(newArr);
										$scope.columnHeaderListfollowUp.push(newArr);
										iii++;
								});
							$scope.savedListfollowUp=[];
							angular.forEach($scope.rowListfollowUp,function(valuefollowUp1,key1){
								angular.forEach($scope.columnListfollowUp,function(colfollowUp2,key2){
									  var followUpApp = ''
										if(colfollowUp2.dischSumTabColMstId == 116){
											followUpApp = (valuefollowUp1.appointmentDate != null ? valuefollowUp1.appointmentDate :"")
										}
										if(colfollowUp2.dischSumTabColMstId == 117){
											followUpApp = (valuefollowUp1.fromTimeString != null ? valuefollowUp1.fromTimeString :"")
										}
								
									var data = {
											inputValue:	followUpApp,
											tableTypeId :27,// Follow up
											coulmnId:colfollowUp2.coulmnId,
											columnName:colfollowUp2.columnName,
											rowId:valuefollowUp1.rowId,
											rowName:followUpApp,
											tableName:$scope.tabelNamefollowUp,
											numberOfRow:$scope.addedFollowupAppointment.length,
											numberOfCoulmn:columnCountfollowUp,
											organizationId:$scope.organizationId,
											unitId:$scope.unitId,
											createdBy:$scope.userId,
											updatedBy:$scope.userId,
											status:'A',
											createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
									   };
											$scope.savedListfollowUp.push(data);
								});
							});

						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableTypeId=7;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListfollowUp=$scope.rowListfollowUp;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListfollowUp=$scope.columnListfollowUp;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tabelNamefollowUp=$scope.tabelNamefollowUp;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderListfollowUp=$scope.columnHeaderListfollowUp;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListfollowUp=$scope.savedListfollowUp;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeSummaryDataViewOptionIdTest=2;
						console.log("$scope.DischargeSummaryFinalList",$scope.DischargeSummaryFinalList);
						$('#closeCommonDischargeSummaryModal').trigger('click');
						}
				}
				
				if($scope.fieldId == 109){

					if($scope.addedObservation.length > 0){
						$scope.id=$scope.commonObjForAdd.dischargeFieldId;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = 0;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = 0;
						 var columnCountObservation = 0;
						angular.forEach($scope.observationColumns.reverse(), function(value, key){
							if(value.isColumnRequired == true){
								columnCountObservation++;
							}
						});
					
						$scope.columnsnNameObservation = [];
		               angular.forEach($scope.observationColumns, function(value, key){
							if(value.isColumnRequired == true){
								$scope.columnsnNameObservation.push(value);
							}
							
						});
		
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfCoulmn = columnCountObservation;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].numberOfRow = $scope.addedObservation.length;
						
						  $scope.rowListObservation = [];
							var iiiRow  = 0;
							 angular.forEach($scope.addedObservation, function(value, index){
								 var newArrRow = {};
								 newArrRow.coulmnId = iiiRow;
								 newArrRow.rowId = iiiRow;
								 var dsc='';
								if(value.employeeName != null){
									dsc =(value.employeeName != null ? value.employeeName :"");
								}else{
									dsc = (value.doctorName != null ? value.doctorName :"");
								}
								 newArrRow.createdTime = (value.createdTime != undefined ? value.createdTime :'');
								 newArrRow.createdDate = (value.createdDate != undefined ? value.createdDate :'');
								 newArrRow.observationDesc = (value.observationDesc != undefined ? value.observationDesc :'');
								 newArrRow.observationChk = (value.observationChk != undefined ? value.observationChk :'');
								 newArrRow.employeeName = (dsc != undefined ? dsc :'');
								 $scope.rowListObservation.push(newArrRow);
								 iiiRow++;
								});
							$scope.columnListObservation = [];
							$scope.columnHeaderListObservation = []
							var iii  = 0;
							$scope.tabelNameObservation = '';
							 angular.forEach($scope.columnsnNameObservation, function(value, index){
										var newArr = {};
										newArr.coulmnId = iii;
										newArr.dischSumTabColMstId = value.dischSumTabColMstId;
										newArr.columnName = value.columnName;
										newArr.tableName = value.tableName;
										$scope.tabelNameObservation = value.tableName;
										newArr.inputValue = '';
										$scope.columnListObservation.push(newArr);
										$scope.columnHeaderListObservation.push(newArr);
										iii++;
								});
							$scope.savedListObservation=[];
							angular.forEach($scope.rowListObservation,function(valueObservation1,key1){
								angular.forEach($scope.columnListObservation,function(colObservation2,key2){
									
									var observationData = ';'
										if(colObservation2.dischSumTabColMstId == 120){
											observationData = (valueObservation1.createdDate != null ? valueObservation1.createdDate :"");
										}
										if(colObservation2.dischSumTabColMstId == 121){
											observationData = (valueObservation1.createdTime != null ? valueObservation1.createdTime :"");
										}
										if(colObservation2.dischSumTabColMstId == 122){
											observationData = (valueObservation1.observationDesc!= null ? valueObservation1.observationDesc :"");
										}
										if(colObservation2.dischSumTabColMstId == 123){
												observationData = (valueObservation1.employeeName != null ? valueObservation1.employeeName :"");
											}
								
									var data = {
											inputValue:	observationData,
											tableTypeId :23,// transfer of care
											coulmnId:colObservation2.coulmnId,
											columnName:colObservation2.columnName,
											rowId:valueObservation1.rowId,
											rowName:observationData,
											tableName:$scope.tabelNameObservation,
											numberOfRow:$scope.addedObservation.length,
											numberOfCoulmn:columnCountObservation,
											organizationId:$scope.organizationId,
											unitId:$scope.unitId,
											createdBy:$scope.userId,
											updatedBy:$scope.userId,
											status:'A',
											createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
									   };
											$scope.savedListObservation.push(data);
								});
							});

						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeFieldDescription="";
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tableTypeId=7;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].rowListObservation=$scope.rowListObservation;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].tabelNameObservation=$scope.tabelNameObservation;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnListObservation=$scope.columnListObservation;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].columnHeaderListObservation=$scope.columnHeaderListObservation;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].savedListObservation=$scope.savedListObservation;
						$scope.DischargeSummaryFinalList[$scope.dischargeSummaryIndex].dischargeSummaryDataViewOptionIdTest=2;
						console.log("$scope.DischargeSummaryFinalList",$scope.DischargeSummaryFinalList);
						$('#closeCommonDischargeSummaryModal').trigger('click');
						}
						
				}
				
				/*//Advice
				if ($scope.fieldId == 18) {
					
				}
				if($scope.fieldId == 109){
								
				}*/
			
			}
			}
				
			
			
			$scope.modalHeader='';
			$scope.fieldId='';
			
			
			//for disch summary
			$scope.getColumnsDetails = function(info,index) {
				$rootScope.startLoader();
				$scope.columnsDetailsList=[];
				var data = {
							organizationId:$scope.organizationId,
							unitId:$scope.unitId,
							dischargeFieldId:info.dischargeFieldId,
						   };
				
				var URI = BASE_URL + ROOT_URL +  "/adt/getDischSumColumnsDetailsForPrint";
				console.log("URI", URI);
				GenericService.serviceAction("POST", URI,data).then(function(response) {
					$rootScope.stopLoader();
					
					$scope.columnsDetailsList = response.data.listObject;
					$scope.columnJson=$scope.columnsDetailsList[0].dischargeFieldColumnJson;
					$scope.columnsToPrint=JSON.parse($scope.columnJson);
					$scope.columnsToPrint = $filter('orderBy')($scope.columnsToPrint, 'dischSumTabColMstId', false) ;
					console.log("$scope.columnsToPrint",$scope.columnsToPrint);
					$scope.tempListForFamilyCol=[];
					$scope.tempListForTravelCol=[];
					$scope.tempListForMedHistoryCol=[];
					$scope.tempListForTreatementCol=[];
					$scope.tempListForSurgicalCol=[];
					$scope.tempListForVaccinationCol=[];
					$scope.tempListForLifeStyleCol=[];
					$scope.tempListForVitalCol=[];
					$scope.tempListForMedicalCol=[];
					$scope.tempListForPrsonalCol=[];
					$scope.tempListForOrd=[];
					$scope.treatmentHistoryCurrentListCol=[];
					$scope.adviceListCol=[];
					$scope.followupCol=[];
					$scope.patDigColumns=[];
					$scope.observationColumns=[];
					$scope.finalCurrentEncounterOrderListsColumn=[];
					$scope.surgeryAndProcedureCol=[];
					$scope.allComplaintListCol=[];
					$scope.treatmentHistoryCurrentListColumn=[];
					$scope.vaccineMapperListCol=[];

					angular.forEach($scope.columnsToPrint, function(value, key){
						if(value.dischargeFieldId == 2 && value.tableId == 2 && value.isColumnRequired == true){
							var newArr = {};
							newArr.columnName = value.columnName,
							newArr.dischSumTabColMstId = value.dischSumTabColMstId,

							newArr.dischargeFieldId = value.dischargeFieldId,
							newArr.isColumnRequired = value.isColumnRequired,
							newArr.tableId = value.tableId,
							newArr.tableName = value.tableName
							$scope.tempListForPrsonalCol.push(newArr);
							console.log("$scope.tempListForPrsonalCol",$scope.tempListForPrsonalCol);
						}
						if(value.dischargeFieldId == 2 && value.tableId == 3 && value.isColumnRequired == true){
							var newArr = {};
							newArr.columnName = value.columnName,
							newArr.dischSumTabColMstId = value.dischSumTabColMstId,

							newArr.dischargeFieldId = value.dischargeFieldId,
							newArr.isColumnRequired = value.isColumnRequired,
							newArr.tableId = value.tableId,
							newArr.tableName = value.tableName
							$scope.tempListForFamilyCol.push(newArr);
							console.log("$scope.tempListForFamilyCol",$scope.tempListForFamilyCol);
						}
						if(value.dischargeFieldId == 2 && value.tableId == 4 && value.isColumnRequired == true){
							var newArr = {};
							newArr.columnName = value.columnName,
							newArr.dischSumTabColMstId = value.dischSumTabColMstId,

							newArr.dischargeFieldId = value.dischargeFieldId,
							newArr.isColumnRequired = value.isColumnRequired,
							newArr.tableId = value.tableId,
							newArr.tableName = value.tableName
							$scope.tempListForSurgicalCol.push(newArr);
							console.log("$scope.tempListForSurgicalCol",$scope.tempListForSurgicalCol);
						}
						if(value.dischargeFieldId == 2 && value.tableId == 5 && value.isColumnRequired == true){
							var newArr = {};
							newArr.columnName = value.columnName,
							newArr.dischSumTabColMstId = value.dischSumTabColMstId,

							newArr.dischargeFieldId = value.dischargeFieldId,
							newArr.isColumnRequired = value.isColumnRequired,
							newArr.tableId = value.tableId,
							newArr.tableName = value.tableName
							$scope.tempListForMedHistoryCol.push(newArr);
							console.log("$scope.tempListForMedHistoryCol",$scope.tempListForMedHistoryCol);
						}
						if(value.dischargeFieldId == 2 && value.tableId == 6 && value.isColumnRequired == true){
							var newArr = {};
							newArr.columnName = value.columnName,
							newArr.dischSumTabColMstId = value.dischSumTabColMstId,

							newArr.dischargeFieldId = value.dischargeFieldId,
							newArr.isColumnRequired = value.isColumnRequired,
							newArr.tableId = value.tableId,
							newArr.tableName = value.tableName
							$scope.tempListForMedicalCol.push(newArr);
							console.log("$scope.tempListForMedicalCol",$scope.tempListForMedicalCol);
						}
						if(value.dischargeFieldId == 2 && value.tableId == 7 && value.isColumnRequired == true){
							var newArr = {};
							newArr.columnName = value.columnName,
							newArr.dischSumTabColMstId = value.dischSumTabColMstId,

							newArr.dischargeFieldId = value.dischargeFieldId,
							newArr.isColumnRequired = value.isColumnRequired,
							newArr.tableId = value.tableId,
							newArr.tableName = value.tableName
							$scope.tempListForVaccinationCol.push(newArr);
							console.log("$scope.tempListForVaccinationCol",$scope.tempListForVaccinationCol);
						}
						if(value.dischargeFieldId == 2 && value.tableId == 8 && value.isColumnRequired == true){
							var newArr = {};
							newArr.columnName = value.columnName,
							newArr.dischSumTabColMstId = value.dischSumTabColMstId,

							newArr.dischargeFieldId = value.dischargeFieldId,
							newArr.isColumnRequired = value.isColumnRequired,
							newArr.tableId = value.tableId,
							newArr.tableName = value.tableName
							$scope.tempListForTravelCol.push(newArr);
							console.log("$scope.tempListForTravelCol",$scope.tempListForTravelCol);
						}
						if(value.dischargeFieldId == 2 && value.tableId == 9 && value.isColumnRequired == true){
							var newArr = {};
							newArr.columnName = value.columnName,
							newArr.dischSumTabColMstId = value.dischSumTabColMstId,

							newArr.dischargeFieldId = value.dischargeFieldId,
							newArr.isColumnRequired = value.isColumnRequired,
							newArr.tableId = value.tableId,
							newArr.tableName = value.tableName
							$scope.tempListForTreatementCol.push(newArr);
							console.log("$scope.tempListForTreatementCol",$scope.tempListForTreatementCol);
						}
						if(value.dischargeFieldId == 2 && value.tableId == 10 && value.isColumnRequired == true){
							var newArr = {};
							newArr.columnName = value.columnName,
							newArr.dischSumTabColMstId = value.dischSumTabColMstId,

							newArr.dischargeFieldId = value.dischargeFieldId,
							newArr.isColumnRequired = value.isColumnRequired,
							newArr.tableId = value.tableId,
							newArr.tableName = value.tableName
							$scope.tempListForLifeStyleCol.push(newArr);
							console.log("$scope.tempListForLifeStyleCol",$scope.tempListForLifeStyleCol);
						}
						if(value.dischargeFieldId == 3 && value.tableId == 11 && value.isColumnRequired == true){
							var newArr = {};
							newArr.columnName = value.columnName,
							newArr.dischSumTabColMstId = value.dischSumTabColMstId,

							newArr.dischargeFieldId = value.dischargeFieldId,
							newArr.isColumnRequired = value.isColumnRequired,
							newArr.tableId = value.tableId,
							newArr.tableName = value.tableName
							$scope.tempListForVitalCol.push(newArr);
							console.log("$scope.tempListForVitalCol",$scope.tempListForVitalCol);
						}
						
						if(value.dischargeFieldId == 4 && value.tableId == 14 && value.isColumnRequired == true){
							var newArr = {};
							if(value.dischSumTabColMstId != 56 && value.dischSumTabColMstId != 63){
								newArr.columnName = value.columnName,
								newArr.dischSumTabColMstId = value.dischSumTabColMstId,

								newArr.dischargeFieldId = value.dischargeFieldId,
								newArr.isColumnRequired = value.isColumnRequired,
								newArr.tableId = value.tableId,
								newArr.tableName = value.tableName
								$scope.tempListForOrd.push(newArr);
							}
							
						}
						if(value.dischargeFieldId == 2 && value.tableId == 9 && value.isColumnRequired == true){
							var newArr = {};
							newArr.columnName = value.columnName,
							newArr.dischSumTabColMstId = value.dischSumTabColMstId,

							newArr.dischargeFieldId = value.dischargeFieldId,
							newArr.isColumnRequired = value.isColumnRequired,
							newArr.tableId = value.tableId,
							newArr.tableName = value.tableName
							$scope.treatmentHistoryCurrentListCol.push(newArr);
						}
						if(value.dischargeFieldId == 18 && value.tableId == 26 && value.isColumnRequired == true){
							var newArr = {};
							newArr.columnName = value.columnName,
							newArr.dischSumTabColMstId = value.dischSumTabColMstId,

							newArr.dischargeFieldId = value.dischargeFieldId,
							newArr.isColumnRequired = value.isColumnRequired,
							newArr.tableId = value.tableId,
							newArr.tableName = value.tableName
							$scope.adviceListCol.push(newArr);
						}
						if(value.dischargeFieldId == 18 && value.tableId == 27 && value.isColumnRequired == true){
							var newArr = {};
							newArr.columnName = value.columnName,
							newArr.dischSumTabColMstId = value.dischSumTabColMstId,

							newArr.dischargeFieldId = value.dischargeFieldId,
							newArr.isColumnRequired = value.isColumnRequired,
							newArr.tableId = value.tableId,
							newArr.tableName = value.tableName
							$scope.followupCol.push(newArr);
						}
						if(value.dischargeFieldId == 8 && value.tableId == 19 && value.isColumnRequired == true){
							var newArr = {};
							newArr.columnName = value.columnName,
							newArr.dischSumTabColMstId = value.dischSumTabColMstId,

							newArr.dischargeFieldId = value.dischargeFieldId,
							newArr.isColumnRequired = value.isColumnRequired,
							newArr.tableId = value.tableId,
							newArr.tableName = value.tableName
							$scope.patDigColumns.push(newArr);
						}
						if(value.dischargeFieldId == 10 && value.tableId == 21 && value.isColumnRequired == true){
							var newArr = {};
							newArr.columnName = value.columnName,
							newArr.dischSumTabColMstId = value.dischSumTabColMstId,

							newArr.dischargeFieldId = value.dischargeFieldId,
							newArr.isColumnRequired = value.isColumnRequired,
							newArr.tableId = value.tableId,
							newArr.tableName = value.tableName
							$scope.finalCurrentEncounterOrderListsColumn.push(newArr);
						}
						if(value.dischargeFieldId == 10 && value.tableId == 22 && value.isColumnRequired == true){
							var newArr = {};
							newArr.columnName = value.columnName,
							newArr.dischSumTabColMstId = value.dischSumTabColMstId,

							newArr.dischargeFieldId = value.dischargeFieldId,
							newArr.isColumnRequired = value.isColumnRequired,
							newArr.tableId = value.tableId,
							newArr.tableName = value.tableName
							$scope.treatmentHistoryCurrentListColumn.push(newArr);
						}
						
						if(value.dischargeFieldId == 6 && value.tableId == 16 && value.isColumnRequired == true){
							var newArr = {};
							if(value.dischSumTabColMstId != 82 && value.dischSumTabColMstId != 77){
							newArr.columnName = value.columnName,
							newArr.dischSumTabColMstId = value.dischSumTabColMstId,

							newArr.dischargeFieldId = value.dischargeFieldId,
							newArr.isColumnRequired = value.isColumnRequired,
							newArr.tableId = value.tableId,
							newArr.tableName = value.tableName
							$scope.surgeryAndProcedureCol.push(newArr);
							}
						}
						if(value.dischargeFieldId == 13 && value.tableId == 25 && value.isColumnRequired == true){
							var newArr = {};
							
							newArr.columnName = value.columnName,
							newArr.dischSumTabColMstId = value.dischSumTabColMstId,

							newArr.dischargeFieldId = value.dischargeFieldId,
							newArr.isColumnRequired = value.isColumnRequired,
							newArr.tableId = value.tableId,
							newArr.tableName = value.tableName
							$scope.allComplaintListCol.push(newArr);
						}
						if(value.dischargeFieldId == 2 && value.tableId == 7  && value.isColumnRequired == true){
							var newArr = {};
							
							newArr.columnName = value.columnName,
							newArr.dischSumTabColMstId = value.dischSumTabColMstId,

							newArr.dischargeFieldId = value.dischargeFieldId,
							newArr.isColumnRequired = value.isColumnRequired,
							newArr.tableId = value.tableId,
							newArr.tableName = value.tableName
							$scope.vaccineMapperListCol.push(newArr);
						}
						if(value.dischargeFieldId == 109 && value.tableId == 29  && value.isColumnRequired == true){
							var newArr = {};
							
							newArr.columnName = value.columnName,
							newArr.dischSumTabColMstId = value.dischSumTabColMstId,

							newArr.dischargeFieldId = value.dischargeFieldId,
							newArr.isColumnRequired = value.isColumnRequired,
							newArr.tableId = value.tableId,
							newArr.tableName = value.tableName
							$scope.observationColumns.push(newArr);
						}
					});
					$scope.showCoversheetData(info,index);
				});
				
			}	
			
		$scope.showCoversheetData=function(obj,index){
			$scope.modalHeader=obj.dischargeFieldName;
			$scope.dischargeSummaryIndex=index;
			$scope.fieldId=obj.dischargeFieldId;	
			if($scope.fieldId==1 || $scope.fieldId==2 || $scope.fieldId==3 || $scope.fieldId==4 || $scope.fieldId==5 || $scope.fieldId==6 || $scope.fieldId==7 ||
					$scope.fieldId==8 || $scope.fieldId==9 ||  $scope.fieldId==10 ||  $scope.fieldId==11 || $scope.fieldId==12 || $scope.fieldId==13 || $scope.fieldId==18 || $scope.fieldId==109 ){
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
			$scope.allComplaintList=[];
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
			
			 //Get Observation
			if($scope.fieldId==109){
				$scope.observationList = [];
				
				var data = {
					organizationId : $scope.organizationId,
					unitId : $scope.unitId,
					admissionId : $scope.admissionId,

				};
				var URI = BASE_URL + ROOT_URL
						+ "/api/opd/getObservationList";
				GenericServiceParamHeader
						.serviceAction(METHOD_POST, URI, data,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									if (response.data.status == "success") {
										$scope.observationList = response.data.listObject;
										angular.forEach($scope.observationList,function(value,index)
												{
												value.createdDate =moment(value.createdDate,'DD/MM/YYYY').format('DD-MMM-YYYY');
												});
										$scope.observationListNew =[];
										angular.forEach($scope.observationList,function(value,index)
										{
											var newArr={};
											angular.forEach($scope.columnsToPrint,function(value1,key) {
												
												newArr.isColumnRequired = value1.isColumnRequired ;
												if(value1.dischSumTabColMstId == 120){
													newArr.createdDate = value.createdDate != null ? value.createdDate :"";
												}
												if(value1.dischSumTabColMstId == 121){
													newArr.createdTime = value.createdTime != null ? value.createdTime :"";
												}
												if(value1.dischSumTabColMstId == 122){
													newArr.observationDesc = value.observationDesc!= null ? value.observationDesc :"";
												}
												if(value1.dischSumTabColMstId == 123){
													if(value.employeeName != null){
														newArr.employeeName = value.employeeName != null ? value.employeeName :"";
													}else{
														newArr.employeeName = value.doctorName != null ? value.doctorName :"";
													}
												}
											});
											$scope.observationListNew.push(newArr);
											
										});
										
										if($scope.observationList.length==0)
											$scope.loadingMessage="No record found";
										angular.forEach($scope.observationList,function(value,key) {
											var flag = false;
											angular.forEach($scope.addedObservation,function(value1,key1) {
												if(value1.observationId == value.observationId ){
														flag = true;
												}
											});
										if(flag== true){
											value.observationChk=1;
										}else{
											value.observationChk=0;
										}
											
							        });
									}
								});
			}
			// <---------------HistoryList--------------->
	
			//get habit details data
			var data = {};
			$scope.habitTypeList = [ {
				"habitId" : 6,
				"habitName" : "Alcohol"
			}, {
				"habitId" : 1,
				"habitName" : "Smoking"
			}, {
				"habitId" : 2,
				"habitName" : "Tobacco chewing"
			}, {
				"habitId" : 3,
				"habitName" : "Gutkha, Pan Masala"
			}, {
				"habitId" : 4,
				"habitName" : "Recreation Drug"
			}, {
				"habitId" : 5,
				"habitName" : "Exercise"
			},

			];
		
			$scope.getActiveHabitDetails=function(){
				$rootScope.startLoader();
				   var headerObj = {
							unit_id : $scope.unitId,
							organization_id : $scope.organizationId
						};
				   
						var paramObjForPer = {
						patientId:$scope.patientId,
						encounterId : $scope.admissionId,
			    		roleId :2
						};
						
						var data = {};
						
			var URI = BASE_URL + ROOT_URL
					+ GETCURRENTPATIENTHABITDETAILS;
			GenericServiceParamHeader
					.serviceAction(METHOD_GET, URI, data,
							NOTIFICATION_MSG_STATUS_FALSE,
							paramObjForPer, headerObj)
					.then(
							function(response) {
								console.log(response);
								// $rootScope.startLoader();
								if (response.data.status == "success")
									$scope.patientHabitList = response.data.listObject;

								// START TEST
								$scope.habitFinalList = [];
								console .log( "HabitList", $scope.habitTypeList);
								console .log( "PatientHabitList", response.data.listObject);
								angular.forEach( $scope.habitTypeList, function( value, key) {
													var checkFlag = false;
													angular.forEach( $scope.patientHabitList, function( value1, key1) {
														var flag = false;
															angular.forEach($scope.addedHabitList,function(value1,key1) {
																if(value1.patientHabitDetailsId == value.patientHabitDetailsId ){
																		flag = true;
																}
															});
															if(flag== true){
																	value.habitChk=1;
															}else{
																	value.habitChk=0;
															}
																		if (value.habitId == value1.habitId) {
																			checkFlag = true;
																			$scope.childObj = [];
																			$scope.newArr = {};
																			$scope.newArr.habitId = value1.habitId;
																			$scope.newArr.habitTypeId = value1.habitTypeId;
																			$scope.newArr.remark = value1.remark;
																			$scope.newArr.cigarettesPerDay = value1.cigarettesPerDay;
																			$scope.newArr.yearsSmoked = value1.yearsSmoked;
																			$scope.newArr.leftWhen = value1.leftWhen;
																			$scope.newArr.duration = value1.durationOfLeftWhen;
																			$scope.newArr.packYear = value1.packYear;
																			$scope.newArr.gmsPerDay = value1.gmsPerDay;
																			$scope.newArr.yearsUsed = value1.yearsUsed;
																			$scope.newArr.mlsPerDay = value1.mlsPerDay;
																			$scope.newArr.frequency = value1.frequency;
																			$scope.newArr.typeOfExercise = value1.typeOfExercise;
																			$scope.newArr.mlsPerDay = value1.mlsPerDay;
																			$scope.newArr.isReviewedFlag = value1.isReviewedFlag;
																			$scope.newArr.isEnterInErrorStatus = value1.isEnterInErrorStatus;
																			if ($scope.roleId == 2) {
																				$scope.newArr.patientHabitDetailsId = value1.patientHabitDetailsId;
																			}
																			if ($scope.roleId == 3) {
																				$scope.newArr.patientHabitDetailsId = value1.patientHabitDetailsId;
																			}
																			$scope.newArr.drug = value1.drug;
																			if (value1.habitId == 5) {

																				var res = value1.typeOfExercise
																						.split(",");
																				// alert(res.length);
																				$scope.patient.exercise = [];
																				var tdata = {};
																				for (var j = 0; j < res.length; j++) {
																					if (res[j] != '') {
																						$scope.patient.exercise
																								.push(res[j]);
																					}
																				}
																				
                                                                                var check = $scope.patient.exercise;
																				
																				if(check.length == 4){
																					$scope.habObj.checkAlltest = 'Y';
																				}
																				// $scope.patient.exercise.push(tdata);
																				$scope.newArr.exerciseList = [
																						"Yoga",
																						"Weight training",
																						"Cardio",
																						"Sports", ];
																				
																				
																			}
																			$scope.childObj.push($scope.newArr);
																		}

																	});
													if (checkFlag == true) {
														var data = {
															"habitId" : value.habitId,
															"habitName" : value.habitName,
															"childHabitObj" : $scope.childObj
														}
														$scope.habitFinalList.push(data);
													} else {

														$scope.childObj = [];
														var newArr = {};
														newArr.habitId = value.habitId;
														newArr.habitTypeId = '';
														newArr.remark = '';
														newArr.cigarettesPerDay = '';
														newArr.yearsSmoked = '';
														newArr.leftWhen = '';
														newArr.duration = '';
														newArr.packYear = '';
														newArr.gmsPerDay = '';
														newArr.yearsUsed = '';
														newArr.mlsPerDay = '';
														newArr.frequency = '';
														newArr.typeOfExercise = '';
														newArr.mlsPerDay = '';
														newArr.drug = '';
														newArr.isReviewedFlag = 'N';
														newArr.isEnterInErrorStatus = 'N';
														if ($scope.roleId == 2) {
															newArr.patientHabitDetailsId = '';
														}

														if (value.habitId == 5) {
															newArr.exerciseList = [
																	"Yoga",
																	"Weight training",
																	"Cardio",
																	"Sports", ];

														}

														$scope.childObj.push(newArr);
														var data = {
															"habitId" : value.habitId,
															"habitName" : value.habitName,
															"childHabitObj" : $scope.childObj
														}
														$scope.habitFinalList.push(data);
														$rootScope.stopLoader();
													}

												});

								console.log("Final Arr Habit",$scope.habitFinalList);
							
								// END TEST
							});
			}
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
														
													      	var headerObj = {
																unitId : $scope.unitId,
																organizationId : $scope.organizationId,
																prescriptionDate : moment(new Date()).format('YYYY-MM-DD')
															};
														
															var paramObjForPer = {
																	patientId :  $scope.patientId,
																	admissionId : $scope.admissionId,
																};
															 
													    		var URI = BASE_URL + ROOT_URL + "/api/opd/getMedicationDetailsForMedicationHistory";
													    		GenericServiceParamHeader.serviceAction(METHOD_GET, URI, data,
																		NOTIFICATION_MSG_STATUS_FALSE,paramObjForPer,headerObj)
													    		.then(
													    			function(response) {
													    				if (response.data.status == "success") {
													    					$scope.medicationListForMedicalHistoryList = response.data.listObject;
													    					angular.forEach($scope.medicationListForMedicalHistoryList,function(value,key) {
													    						
													    						 var splitEndDate=(value.endDate).split('/');
																				 var splitStartDate=(value.startDate).split('/');
																				 var parseEndDate=splitEndDate[2]+"-"+splitEndDate[1]+"-"+splitEndDate[0];
																			     var parseStartDate=splitStartDate[2]+"-"+splitStartDate[1]+"-"+splitStartDate[0];
																				 var endDate = new Date(parseEndDate);
														                         var startDate = new Date(parseStartDate);
														                         var duration = Math.floor((Date.UTC(endDate.getFullYear(), endDate.getMonth(),
														                        		 endDate.getDate()) - Date.UTC(startDate.getFullYear(), 
														                        				 startDate.getMonth(), startDate.getDate()))
														                                 / (1000 * 60 * 60 * 24));
														                         value.duration=duration;
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
														var medicalHistoryCounter=1;
														angular.forEach($scope.medicalHistoryOldList,function(value,key) {
															medicalHistoryCounter++;
															value.medicalHistoryCounter=medicalHistoryCounter;
															var flag = false;
															angular.forEach($scope.medicalHistoryList,function(value1,key1) {
																if(value1.medicalHistoryCounter == value.medicalHistoryCounter ){
																		flag = true;
																}
															});
														if(flag== true){
																value.medicalHistoryChk=1;
														}else{
																value.medicalHistoryChk=0;
														}
															
											        });
														
														var headerObjDia = {
																'unit_id' : $scope.unitId,
																'organization_id' : $scope.organizationId,
																'patient_id' : $scope.patientId,
																'createdDate' :moment(new Date()).format('YYYY-MM-DD'),
																'Content-Type' : 'application/json',
															};
															$scope.patientDiagnosisListForMedicalHistory =[];
															var URI = BASE_URL + ROOT_URL
																	+ "/api/opd/getPatientDiagnosisByPatientIdForMedicalHistory";
															GenericServiceParamHeader .serviceAction(METHOD_GET, URI, {}, NOTIFICATION_MSG_STATUS_FALSE,
																			data, headerObjDia)
																	.then( function(response) {
																				console.log(response);
																				if (response.data.status == "success")
																					$scope.patientDiagnosisListForMedicalHistory = response.data.listObject;
																				console.log( "patientDiagnosisListForMedicalHistory", $scope.patientDiagnosisListForMedicalHistory);

																				angular.forEach($scope.patientDiagnosisListForMedicalHistory,function(value,key) {
																					medicalHistoryCounter++;
																					value.medicalHistoryCounter=medicalHistoryCounter;
																					var flag = false;
																					angular.forEach($scope.medicalHistoryList,function(value1,key1) {
																						if(value1.medicalHistoryCounter == value.medicalHistoryCounter ){
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
							       
		                        
								    $scope.getVaccineData = function(){
								    //$timeout( function(){
			                    		var paramObjForVaccine = {
			                    				
			                    				patientId:$scope.patientId,
			                    				organizationId:$scope.organizationId,
								        		unitId : $scope.unitId
			                    				
			                    		}
			                    		
			                    		console.log("QQQQ",paramObjForVaccine);
			                    		 
			                            var URI = BASE_URL + ROOT_URL+ "/api/opd/getListOfVaccineImmunizationMapper";
			                        GenericService.serviceAction("POST", URI, paramObjForVaccine)
			                            .then(function(response){console.log(response);
			                            if (response.data.status == "success")
			                                $scope.immunizationHistoryCurrentList = [];
			                                $scope.vaccineMapperList  = response.data.listObject;
			                                 angular.forEach($scope.vaccineMapperList, function(value1 , key1) {
			                                	 var checkFlag = false;
			                                	 var obj={};
			                                	 angular.forEach($scope.activevaccine, function(value , key) {
			                                	  if(value.vaccinationId == value1.vaccinationMasterId)
			                                		 {
			                                		  obj=value;
			                                		 }
			                                	
			                                 		});
																		$scope.dateOptionsAdm=[];
																		$scope.dateOptionsVacc=[];
																		 
																		var newArr = {};
																		newArr.vaccinationId = obj.vaccinationId;
				                                            		 	newArr.name = obj.name;
				                                            		 	newArr.doseDesc = obj.doseDesc;
				                                            		 	newArr.description = obj.description;
				                                            		 	newArr.siteName = obj.siteName;
				                                            		 	newArr.minAge = obj.minAge;
				    		                                		 	newArr.minDay = obj.minDay;
				    		                                		 	newArr.maxAge = obj.maxAge;
				    		                                		 	newArr.maxDay = obj.maxDay;
				    		                                		 	newArr.genericName = obj.genericName;
				    		                                		 	newArr.series = obj.series;
				    		                                			newArr.abbreviation = obj.abbreviation;
				    		                                			var array = obj.minAge.split('-');
				                                            		 	var age=array[0];
				                            						    var duration=obj.minDay;
				                            						   // var age1 = $rootScope.getChangedFormatedDateShalseTwo($scope.patientDetails.birthDate);
				            									
				            											newArr.location =value1.location;
				        	                                		 	newArr.calculatedDateShow =value1.calculatedDate;
				        	                                			newArr.calculatedDate =value1.calculatedDate;
				        	                                			newArr.plannedDate =value1.plannedDate;
				        	                                			
				        	                                			newArr.remarks = value1.remarks;
				        	                                		 	newArr.vaccineFlagStatus = value1.vaccineFlagStatus;
																		$scope.immunizationHistoryCurrentList.push(newArr);
			                                });
			                                 angular.forEach($scope.immunizationHistoryCurrentList,function(value,key) {
													var flag = false;
													angular.forEach($scope.immunizationHistoryList,function(value1,key1) {
														if(value1.vaccinationId == value.vaccinationId ){
																flag = true;
														}
													});
												if(flag== true){
														value.immunizationHistoryChk=1;
												}else{
														value.immunizationHistoryChk=0;
												}
													
									        });
			                                 
			                             console.log("Vaccination history",JSON.stringify($scope.immunizationHistoryCurrentList));
			                                    });
			                   // },4000);
							}
						 $scope.getVaccineData();    
						 
						/*		var URI = BASE_URL
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
								*/
								
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
								
								/*Investigation History start*/
								var headerObjForInv = {
										unit_id : $scope.unitId,
										organization_id : $scope.organizationId
									};

									var paramObjForInvHistory = {
										patientId : $scope.patientId,
										created_date:moment(new Date()).format('DD-MM-YYYY')
									};
									
								var URI = BASE_URL
										+ ROOT_URL
										+ "/api/opd/getPatientTreatmentHistoryForDischargeSummary";
								GenericServiceParamHeader
										.serviceAction(METHOD_GET, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE,
												paramObjForInvHistory, headerObjForInv)
										.then(
												function(response) {
													console.log(response);
													
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.treatmentHistoryCurrentList = response.data.listObject;

														angular.forEach($scope.treatmentHistoryCurrentList,function(value,key) {
															var flag = false;
															angular.forEach($scope.treatmentHistoryList,function(value1,key1) {
																if(value1.treatmentHistoryId == value.treatmentHistoryId ){
																		flag = true;
																}
															});
														if(flag== true){
																value.treamentHistoryCurrentChk=1;
														}else{
																value.treamentHistoryCurrentChk=0;
														}
														});
														
												});
						
								
				//get saved data of habits
				$scope.getActiveHabitDetails();			
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
									var strength = (value.strengthDesc != undefined && value.strengthDesc != null) ? value.strengthDesc:'';
									if(value.doseId==null){
										value.dose=value.doseDesc +" "+ strength;
									}else{
										value.dose=value.doseId+" "+value.doseUnit;
									}
									
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
									var data = {
											unitId:$scope.unitId,
	                       				   organizationId:$scope.organizationId,
	                       				   patientId:$scope.patientId,
								     };
									console.log("jj", paramObjForVital);
									// return false;
									var URI = BASE_URL + ROOT_URL
											//+ GETCURRENTAPPOINTMENTVITALMAPPER;
									+ "/api/opd/getListOfVitalsByPatientIdForCoversheet";
									GenericServiceParamHeader
											.serviceAction(METHOD_POST, URI, data,
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
															$scope.descriptivePhysicalExamination="";
															if (response.data.status == "success")
																$scope.getSavedPhysicalExamList = response.data.listObject;
															if($scope.getSavedPhysicalExamList.length>0)
																$scope.descriptivePhysicalExamination=$scope.getSavedPhysicalExamList[0].descriptiveExamination;
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
															$scope.descriptiveGeneralExamination="";
															if (response.data.status == "success")
																$scope.getSavedGeneralExamList = response.data.listObject;
															if($scope.getSavedGeneralExamList.length>0)
																$scope.descriptiveGeneralExamination=$scope.getSavedGeneralExamList[0].descriptiveExamination;
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
	  
		/*Investigation History start*/
		var headerObjForInv = {
				unit_id : $scope.unitId,
				organization_id : $scope.organizationId
			};

			var paramObjForInvHistory = {
				patientId : $scope.patientId,
				created_date:moment(new Date()).format('DD-MM-YYYY')
			};
			
		var URI = BASE_URL
				+ ROOT_URL
				+ "/api/opd/getPatientTreatmentHistoryForDischargeSummary";
		GenericServiceParamHeader
				.serviceAction(METHOD_GET, URI, data,
						NOTIFICATION_MSG_STATUS_FALSE,
						paramObjForInvHistory, headerObjForInv)
				.then(
						function(response) {
							console.log(response);
							
							// $rootScope.startLoader();
							if (response.data.status == "success")
								$scope.treatmentHistoryCurrentList1 = response.data.listObject;

								angular.forEach($scope.treatmentHistoryCurrentList1,function(value,key) {
									var flag = false;
									angular.forEach($scope.treatmentHistoryList1,function(value1,key1) {
										if(value1.treatmentHistoryId == value.treatmentHistoryId ){
												flag = true;
										}
									});
								if(flag== true){
										value.treamentHistoryCurrentChk1=1;
								}else{
										value.treamentHistoryCurrentChk1=0;
								}
								});
								
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
									
									 var splitEndDate=(value.endDate).split('/');
									 var splitStartDate=(value.startDate).split('/');
									 var parseEndDate=splitEndDate[2]+"-"+splitEndDate[1]+"-"+splitEndDate[0];
								     var parseStartDate=splitStartDate[2]+"-"+splitStartDate[1]+"-"+splitStartDate[0];
									 var endDate = new Date(parseEndDate);
			                         var startDate = new Date(parseStartDate);
			                         var duration = Math.floor((Date.UTC(endDate.getFullYear(), endDate.getMonth(),
			                        		 endDate.getDate()) - Date.UTC(startDate.getFullYear(), 
			                        				 startDate.getMonth(), startDate.getDate()))
			                                 / (1000 * 60 * 60 * 24));
			                       
			                      /* if(duration>30){
			                    	   var month=Math.floor(duration/30);
			                    	   var days=duration%30;
			                    	   if(days>0){
			                    		   value.duration=duration+" M "+days+" D"; 
			                    	   }else{
			                    		   value.duration=duration+" M"; 
			                    	   }
			                       }else{
			                    	   value.duration=duration+" D"; 
			                       }*/
			                        				 
			                        value.duration=duration+" days"; 
			                         
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
	  
 //advice and follow up 
   if($scope.fieldId==18){ 
	   $scope.allInstructions=[];
	   $scope.followupAppointmentList=[];
		var data = {
			encounterId:$scope.admissionId
		  }
		var URI = BASE_URL + ROOT_URL + GETINSTRUCTIONS + "/2";

		GenericServiceParamHeader
				.serviceAction(METHOD_POST, URI, data,
						NOTIFICATION_MSG_STATUS_FALSE)
				.then(
						function(response) {
							if (response.data.status == "success") {
								$scope.allInstructions = response.data.listObject;
								if($scope.allInstructions.length==0)
									$scope.loadingMessage="No record found";
								angular.forEach($scope.allInstructions,function(value,key) {
									var flag = false;
									angular.forEach($scope.addedInstructions,function(value1,key1) {
										if(value1.instructionId == value.instructionId ){
												flag = true;
										}
									});
								if(flag== true){
										value.instructionChk=1;
								}else{
										value.instructionChk=0;
								}
									
					      });
							}
						});
						
						var followUpObj={
										"admissionId":$scope.admissionId
										}
						var URI2=BASE_URL + ROOT_URL + "/adt/discharge/getFollowUpAppointment";
						GenericService.serviceAction("POST", URI2, followUpObj).then(function(response) {
							if (response.data.status == "success") {
								$scope.followupAppointmentList = response.data.listObject;
								if($scope.followupAppointmentList.length==0)
									$scope.loadingMessage="No record found";
								angular.forEach($scope.followupAppointmentList,function(value,key) {
									var flag = false;
									angular.forEach($scope.addedFollowupAppointment,function(value1,key1) {
										if(value1.appointmentId == value.appointmentId ){
												flag = true;
										}
									});
								if(flag== true){
										value.followUpChk=1;
								}else{
										value.followUpChk=0;
								}
									
					        });
							
							}
						});	
	}
 
 //get Complaint list
   if($scope.fieldId==13){ 
	   $scope.allComplaintList=[];
	   var headerObjComp = {
				unit_id : $scope.unitId,
				organization_id : $scope.organizationId
			};

			var paramObjCom = {
				admissionId : $scope.admissionId,
				roleId :2
			};
		var URI = BASE_URL + ROOT_URL + GET_COMPLAINT_ADMISSION;
		GenericServiceParamHeader
				.serviceAction(METHOD_GET, URI, "",
						NOTIFICATION_MSG_STATUS_FALSE,paramObjCom, headerObjComp)
				.then(
						function(response) {
							if (response.data.status == "success") {
								$scope.allComplaintList = response.data.listObject;
								if($scope.allComplaintList.length==0)
									$scope.loadingMessage="No record found";
								angular.forEach($scope.allComplaintList,function(value,key) {
									var flag = false;
									angular.forEach($scope.addedComplaintList,function(value1,key1) {
										if(value1.complaintAdmissionId == value.complaintAdmissionId ){
												flag = true;
										}
									});
								if(flag== true){
										value.complaintChk=1;
								}else{
										value.complaintChk=0;
								}
									
					      });
							}
						});
						
						var followUpObj={
										"admissionId":$scope.admissionId
										}
						var URI2=BASE_URL + ROOT_URL + "/adt/discharge/getFollowUpAppointment";
						GenericService.serviceAction("POST", URI2, followUpObj).then(function(response) {
							if (response.data.status == "success") {
								$scope.followupAppointmentList = response.data.listObject;
								if($scope.followupAppointmentList.length==0)
									$scope.loadingMessage="No record found";
								angular.forEach($scope.followupAppointmentList,function(value,key) {
									var flag = false;
									angular.forEach($scope.addedFollowupAppointment,function(value1,key1) {
										if(value1.appointmentId == value.appointmentId ){
												flag = true;
										}
									});
								if(flag== true){
										value.followUpChk=1;
								}else{
										value.followUpChk=0;
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
						
						var obj = angular.copy(data1);
						obj.dischargeFieldId = value.dischargeFieldId;
						obj.dischargeFieldDescription= value.dischargeFieldDescription;
						obj.dischargeSummaryDataViewOptionId =value.dischargeSummaryDataViewOptionIdTest;
						obj.patientId =$scope.patientId,
						obj.admissionId =$scope.admissionId,
						obj.departmentId =$scope.departmentId,
						obj.doctorId =$scope.doctorId,
						obj.dischargeTemplateId =$scope.dischargeTemplateId,
						obj.organizationId =$scope.organizationId,
						obj.unitId =$scope.unitId,
						obj.createdBy =$scope.createdBy,
						obj.updatedBy =$scope.updatedBy,
						obj.status ='A',
						obj.createdDate =$scope.dt,
						obj.updatedDate =$scope.dt,
						obj.dischargeSummaryTemplateListdata = [];
						if(value.savedList != undefined && value.savedList.length > 0){
							obj.dischargeSummaryTemplateListdata = value.savedList;
						}
						if(value.savedListAllergies != undefined && value.savedListAllergies.length > 0){
							obj.dischargeSummaryTemplateListdata = obj.dischargeSummaryTemplateListdata.concat(value.savedListAllergies);
						}
						if(value.savedListPersonalHistory != undefined && value.savedListPersonalHistory.length > 0){
							obj.dischargeSummaryTemplateListdata = obj.dischargeSummaryTemplateListdata.concat(value.savedListPersonalHistory);
						}
						if(value.savedListFamilyHistory != undefined && value.savedListFamilyHistory.length > 0){
							obj.dischargeSummaryTemplateListdata = obj.dischargeSummaryTemplateListdata.concat(value.savedListFamilyHistory);
						}
						if(value.savedListSurgicalHistory != undefined && value.savedListSurgicalHistory.length > 0){
							obj.dischargeSummaryTemplateListdata = obj.dischargeSummaryTemplateListdata.concat(value.savedListSurgicalHistory);
						}
						if(value.savedListMedicationHistory != undefined && value.savedListMedicationHistory.length > 0){
							obj.dischargeSummaryTemplateListdata = obj.dischargeSummaryTemplateListdata.concat(value.savedListMedicationHistory);
						}
						if(value.savedListMedicalHistory != undefined && value.savedListMedicalHistory.length > 0){
							obj.dischargeSummaryTemplateListdata = obj.dischargeSummaryTemplateListdata.concat(value.savedListMedicalHistory);
						}
						if(value.savedListVaccinationHistory != undefined && value.savedListVaccinationHistory.length > 0){
							obj.dischargeSummaryTemplateListdata = obj.dischargeSummaryTemplateListdata.concat(value.savedListVaccinationHistory);
						}
						if(value.savedListTravelHistory != undefined && value.savedListTravelHistory.length > 0){
							obj.dischargeSummaryTemplateListdata = obj.dischargeSummaryTemplateListdata.concat(value.savedListTravelHistory);
						}
						if(value.savedListTreatmentHistory != undefined && value.savedListTreatmentHistory.length > 0){
							obj.dischargeSummaryTemplateListdata = obj.dischargeSummaryTemplateListdata.concat(value.savedListTreatmentHistory);
						}
						if(value.savedListMediAdmini != undefined && value.savedListMediAdmini.length > 0){
							obj.dischargeSummaryTemplateListdata = obj.dischargeSummaryTemplateListdata.concat(value.savedListMediAdmini);
						}
						if(value.savedListConsultOrder != undefined && value.savedListConsultOrder.length > 0){
							obj.dischargeSummaryTemplateListdata = obj.dischargeSummaryTemplateListdata.concat(value.savedListConsultOrder);
						}
						if(value.savedListDiagnosis != undefined && value.savedListDiagnosis.length > 0){
							obj.dischargeSummaryTemplateListdata = obj.dischargeSummaryTemplateListdata.concat(value.savedListDiagnosis);
						}
						if(value.savedListAdmisssionReason != undefined && value.savedListAdmisssionReason.length > 0){
							obj.dischargeSummaryTemplateListdata = obj.dischargeSummaryTemplateListdata.concat(value.savedListAdmisssionReason);
						}
						if(value.savedListInvestigation != undefined && value.savedListInvestigation.length > 0){
							obj.dischargeSummaryTemplateListdata = obj.dischargeSummaryTemplateListdata.concat(value.savedListInvestigation);
						}
						if(value.savedListTransferOfCare != undefined && value.savedListTransferOfCare.length > 0){
							obj.dischargeSummaryTemplateListdata = obj.dischargeSummaryTemplateListdata.concat(value.savedListTransferOfCare);
						}
						if(value.savedListObservation != undefined && value.savedListObservation.length > 0){
							obj.dischargeSummaryTemplateListdata = obj.dischargeSummaryTemplateListdata.concat(value.savedListObservation);
						}
						if(value.savedListVital != undefined && value.savedListVital.length > 0){
							obj.dischargeSummaryTemplateListdata = obj.dischargeSummaryTemplateListdata.concat(value.savedListVital);
						}
						if(value.savedListOperativeDetails != undefined && value.savedListOperativeDetails.length > 0){
							obj.dischargeSummaryTemplateListdata = obj.dischargeSummaryTemplateListdata.concat(value.savedListOperativeDetails);
						}
						if(value.savedListSurgeriesProceduresPerformed != undefined && value.savedListSurgeriesProceduresPerformed.length > 0){
							obj.dischargeSummaryTemplateListdata = obj.dischargeSummaryTemplateListdata.concat(value.savedListSurgeriesProceduresPerformed);
						}
						if(value.savedListDischargeMedication != undefined && value.savedListDischargeMedication.length > 0){
							obj.dischargeSummaryTemplateListdata = obj.dischargeSummaryTemplateListdata.concat(value.savedListDischargeMedication);
						}
						if(value.savedListInstructions != undefined && value.savedListInstructions.length > 0){
							obj.dischargeSummaryTemplateListdata = obj.dischargeSummaryTemplateListdata.concat(value.savedListInstructions);
						}
						if(value.savedListfollowUp != undefined && value.savedListfollowUp.length > 0){
							obj.dischargeSummaryTemplateListdata = obj.dischargeSummaryTemplateListdata.concat(value.savedListfollowUp);
						}
						
 				      $scope.fieldListTemplate.push(obj);
					
		         });
				
			
					
				
				console.log("fieldlist",$scope.fieldListTemplate);
				
				var data={	
						
						patientId:$scope.patientId,
						admissionId:$scope.admissionId,
						departmentId:$scope.departmentId,
						doctorId:$scope.doctorId,
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
			
//				if ($scope.popUpFlag) {
////					$scope.showModal = !$scope.showModal;
////					$("#confirmSave").show();
//				} else {
				var URI = BASE_URL + ROOT_URL +"/adt/discharge/saveDischargeSummTabularTemplateData";
				console.log("Save-URI-",URI);
				console.log(" save OBJ",data); 
				GenericService.serviceAction("POST",URI,data).then(function(response){
					$scope.dischargeSummaryId=response.data.object.dischargeSummaryId;
					if (response.data.status == 'success')
					{
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
						//$scope.initMaster();
						$rootScope.stopLoader();
						$("#confirmSave").hide();
						 $('.modal-backdrop').remove();
						$('body').removeClass("modal-open");
						$state.go('dischargedPatients');
						$scope.DischargeSummaryFinalList=[];
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
					
//			      }
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
						reportService.generate(REPORT_API_URI1, data);
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
						reportService.generate(REPORT_API_URI1,data);
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
					growl.warning("Final Report not released!!",
							{
								title : 'Warning!'
							});

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
						
						
					
					//vaccination list
					$scope.activevaccine=[];
					 $scope.getAllActivevaccine = function()
				        {
						 
						 if($scope.genderId != undefined){
							 	$rootScope.startLoader();
					        	$scope.activevaccine = [];
					    		var URI = BASE_URL + ROOT_URL + "/api/billing/getActiveVaccinationMasterByGenderId/"+$scope.organizationId+"/"+$scope.genderId;
					    		GenericServiceParamHeader.serviceAction("GET", URI)
					    		.then(
					    			function(response) {
					    				if (response.data.status == "success") {
					    					$scope.activevaccine = response.data.listObject;
					    					console.log("ffrefre>>>>>>>>>>",$scope.activevaccine);
					    					
					    				}
					    			}); 
						 }
					   
				        }
					 $scope.getAllActivevaccine();
					 
					 
					    $scope.flagVaccineValidate = false;
					    $scope.validateVaccination = function(flagVaccine){
					    if(flagVaccine != ""){
					    $scope.flagVaccineValidate=true;
					    }
					    else{
					    $scope.flagVaccineValidate=false;
					    }

					    }
					    
					  //get outSource service  Report 
						$scope.patientDocumentList = [];
						 $scope.getPatientDocumentList = function(labSampleDtlsId){
						    $rootScope.startLoader();
						    var data ={
							  "unitId":$scope.unitId,
							  "organisationId":$scope.organizationId,
							  "labSampleDtlsId":labSampleDtlsId
						    }
							var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
							BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							$scope.patientDocumentList = [];
							if (response.data.status == "success") 
							{
								$scope.patientDocumentList = response.data.listObject;				            
							        
							    $rootScope.stopLoader();
							}
							else if(response.data.status == "error")
							{
								$rootScope.stopLoader();
							}
						  });
						}						
								
						$scope.getOutsourceServiceReport = function(obj){
							$('#outSourceServiceModal').modal('show');
							$scope.getPatientDocumentList(obj.labSampleDtlsId);
						}
						
						$scope.openImage = function(index)
                        { 
							$rootScope.startLoader();
							var imageUrl = $scope.patientDocumentList[index].documentPath;
							var href = $rootScope.currentHrefDoccument();
							$.fancybox.open({
								width: 600, // or whatever
								height: 320,
								href:href+imageUrl,
								type: 'iframe'
							});
							$rootScope.stopLoader();
                        }

			$scope.recognitionInProgress = false;
			$scope.recordingState = {};

			var recognition;
			var activeIndex = null;
			var textAreaIndex = null;
			var activeEditorId = null;
			var isFirstRecognitionChunk = true;

			$scope.initSpeechRecognition = function() {

				if (!('webkitSpeechRecognition' in window || 'SpeechRecognition' in window)) {
					alert("Speech Recognition not supported in this browser");
					return;
				}

				var SpeechRecognition =
					window.SpeechRecognition || window.webkitSpeechRecognition;

				recognition = new SpeechRecognition();

				recognition.lang = 'en-IN';
				recognition.continuous = true;
				recognition.interimResults = false;

				recognition.onresult = function (event) {
					var transcript = '';

					for (var i = event.resultIndex; i < event.results.length; i++) {
						if (event.results[i].isFinal) {
							transcript += event.results[i][0].transcript;
						}
					}

					if (!transcript) return;

					if (isFirstRecognitionChunk) {
						transcript =
							transcript.charAt(0).toUpperCase() + transcript.slice(1);
						isFirstRecognitionChunk = false;
					}

					$scope.insertTextIntoCKEditor(activeEditorId, transcript);

				};

				recognition.onerror = function (e) {
					console.error("Speech error:", e);
					if (activeIndex !== null) {
						$scope.recordingState[activeIndex] = false;
					}
					activeIndex = null;
					textAreaIndex = null;
					activeEditorId = null;
					isFirstRecognitionChunk = true;
					try { recognition.stop(); } catch (err) {}
				};

				recognition.onend = function () {
					// auto-restart only if user didn’t stop manually
					if ($scope.recordingState[activeIndex]) {
						recognition.start();
					}
				};
			}

			$scope.initSpeechRecognition();

			$scope.startRecording = function (info, index) {

				if (!recognition) return;

				if (activeIndex !== null && activeIndex === index) {
					$scope.recordingState[index] = false;
					activeIndex = null;
					recognition.stop();
					console.log("Voice recognition stopped.");
					return;
				}

				if (
					info.dischargeSummaryDataViewOptionIdTest !== 1 &&
					!(info.dischargeSummaryDataViewOptionIdTest === 2 &&
						info.dischargeFieldDescription)
				) {
					console.warn('CKEditor not visible for index', index);
					return;
				}
				activeIndex = index;
				activeEditorId = 'ckidx' + index;
				textAreaIndex = index;
				isFirstRecognitionChunk = true;

				try {
					recognition.start();
					$scope.recordingState[index] = true;
					console.log("Started listening");
				} catch (e) {
					console.warn("Recognition already started");
				}
			};

			$scope.insertTextIntoCKEditor = function(textareaId, text) {

				$scope.DischargeSummaryFinalList[textAreaIndex].dischargeFieldDescription = $scope.DischargeSummaryFinalList[textAreaIndex].dischargeFieldDescription +' '+ text;

			}

		}]);
