/************EDITED ************************/
'use strict';

/**
 * @Author Sneha Dalave
 * @Author Chetna Dhokne 
 * @name myApp.controller:OPDCoverSheetController
 * @description #OPDCoverSheetController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'complaintsTabController',
				[
						'$scope',
						'$cookies',
						'$rootScope',
						'GenericService',
						'BillingGenericService',
						'$state',
						'$timeout',
						'$interval',
						'$stateParams',
						'GenericServiceParamHeader',
						'growl',
						'$compile',
						'reportService',
						'GenericServiceFileUpload',
						'servicesFactory',
						'CONSTANTS',
						'coversheetTemplateService',
						function($scope, $cookies, $rootScope, GenericService,
								BillingGenericService, $state, $timeout,$interval,
								$stateParams,GenericServiceParamHeader,growl,$compile,reportService,GenericServiceFileUpload,servicesFactory,CONSTANTS,coversheetTemplateService) {

								var cookieObject, cookieObjectAppConfig;
								[ cookieObject, cookieObjectAppConfig ] = $scope.checkCookies();
								
							$scope.init = function() {

								
								 $scope.dateLetter = new Date();
									$scope.deptWiseAppointment='N';

								$scope.msgRecord="No records found...!!";
								
								$scope.allergyShowFlag = true;
								$scope.admissionObj={};
								//$scope.admissionObj.bedOptionFlexible = "N";
								$scope.admissionObj.admissionType = "medical";
								$scope.admissionObj.bedOptionFlexible = "N";
								$scope.searchType = "search";
								$scope.currentTab = "investigation";
								$scope.currentOrganizationList = {"organizationId":1,"organizationName":"RubbyHall"}
								$scope.currentUnitList = {"unitId":1,"unitName":"Rubby Unit1"}
								$scope.selectedValues = {unitId : '',doctorId:'',specialityId:''};
								$scope.cur=moment(new Date()).subtract(1, 'days').toDate();
								$scope.currentDateMedication = moment($scope.cur).format('YYYY-MM-DD')
								$scope.currentDate = moment(new Date()).format('YYYY-MM-DD');
								
								
						
								var cookieObject = $cookies
										.getObject('cookieObject');
								if (cookieObject == undefined) {
									$state.go('login');
									return;
								}
								
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.employeeId = cookieObject.employeeId;
								
								
								var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
								if(cookieObjectAppConfig == undefined){
									$state.go('applicationConfiguration');
									return;
								}
								else
									{
										console.log('cookieObjectAppConfig',cookieObjectAppConfig);
										
									}
								$scope.diagnosisRequireAppConfig = cookieObjectAppConfig.diagnosisRequire;
								$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
								$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
								$scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
								$scope.allergyButtonFlag = true;
								$scope.paediatricsSpecialityId = cookieObjectAppConfig.paediatricsSpecialityId;
								$scope.radiologySpecialityId=cookieObjectAppConfig.radiologySpecialityId;
								$scope.isAdviceAccess=cookieObjectAppConfig.isAdviceTabVisible;
								$scope.roleName = cookieObjectAppConfig.roleName;
								$scope.nurseRole = cookieObjectAppConfig.nurseRole;	 
								$scope.doctorRole = cookieObjectAppConfig.doctorRole;
								$scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
							    $scope.isAppointmentRequired = cookieObjectAppConfig.isAppointmentRequired;
							    $scope.configureConsultationSpecialityId=cookieObjectAppConfig.consultationSpecialityId;
							    $scope.pharmacyServiceId = cookieObjectAppConfig.pharmacyServiceId;
								 var year = new Date().getFullYear();
							      var range = [];

							      range.push(year);
						            
								$scope.patientDetails = 
									{
										uhIdNumber:"",
										patientName:"",
										genderCode:"",
										age:"",
										birthDate:"",
										encounterId:""
									};
								
								$scope.checkAppointment=function(){
									$scope.encounterDetailsObj = JSON.parse(sessionStorage.getItem('cookieEncounterObject')) || {};
									console.log("Encounter Object",$scope.encounterDetailsObj);
									
									if ($scope.encounterDetailsObj  != null) {
										    $scope.billIdForCheck = $scope.encounterDetailsObj.billId;
											$scope.patientDetails.patientName = $scope.encounterDetailsObj.patientName;
											$scope.patientDetails.specialityName = $scope.encounterDetailsObj.specialityName;
											$scope.patientDetails.uhIdNumber = $scope.encounterDetailsObj.uhidNumber;
											$scope.patientDetails.genderCode = $scope.encounterDetailsObj.genderCode;
											$scope.patientDetails.genderId = $scope.encounterDetailsObj.genderId;
											$scope.patientDetails.age = $scope.encounterDetailsObj.age;
											$scope.patientDetails.ageStr = $scope.encounterDetailsObj.age;
											$scope.patientDetails.visitTypeId = $scope.encounterDetailsObj.visitTypeId;
											$scope.patientDetails.birthDate = $scope.encounterDetailsObj.dob;
											$scope.patientDetails.visitDate = $scope.encounterDetailsObj.encounterDate;
											$scope.patientDetails.visitTime = $scope.encounterDetailsObj.consultationStartTime;
											$scope.patientDetails.encounterId = $scope.encounterDetailsObj.encounterId;
											$scope.roleId = $scope.encounterDetailsObj.roleId;
											$scope.patientDetails.patientCategoryId =$scope.encounterDetailsObj.patientCategoryId;
//											$scope.isCritical = $scope.encounterDetailsObj.isCritical;
											$scope.encounterIdVital =  $scope.encounterDetailsObj.encounterId;
											$scope.patientDetails.doctorFullName =  $scope.encounterDetailsObj.doctorName;
											if($scope.encounterDetailsObj.doctorName!=null)
											 $scope.patientDetails.dFirstName  =  $scope.encounterDetailsObj.doctorName;
											$scope.patientDetails.consultationStartTime =  $scope.encounterDetailsObj.consultationStartTime;
											
											$scope.encounterDetailsObj.treatingDoctorSpecialityId = "";
											$scope.encounterDetailsObj.treatingDoctorId = "";
											$scope.encounterDetailsObj.isTreatingDoctor = 'Y';
											
											//For year dropdown
											 var ya = new Date().getFullYear()
											var BirthData= $scope.patientDetails.birthDate.split('/');
											 $scope.birthYear=BirthData[2];
											 var birthMonth=BirthData[1].split('');
											 $scope.birthMonth=birthMonth[1];
											 $scope.YearDifference = ya-$scope.birthYear;
											  for (var i = 1; i < 100; i++) {
											        range.push(year - i);
											      }
											  
											  $scope.yearss = range;
											  $scope.years=[];
                                              angular.forEach($scope.yearss,function(value){
                                            	  if(parseInt($scope.birthYear) <= value){
                                            		  $scope.years.push(value);
                                            	  }
                                              })
											  console.log("KI",$scope.yearss);
											  console.log("$scope.yearsss",$scope.years);
											//For Contract
											if($scope.encounterDetailsObj.contractId != $scope.selfPayeeId)
											{
												$scope.contractId = $scope.encounterDetailsObj.contractId;
											}
											$scope.showHeadCircuVital=false;
											if($scope.patientDetails.age >= 1 &&  $scope.patientDetails.age <=3 ){
												$scope.showHeadCircuVital=true;
											}
									}
									else
										{
											$state.go('encounterQueueManagement');
											return;
										}
									
								}
								$scope.checkAppointment();
								

								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								$scope.compListFlag='Current';
								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								var data = {};
								
								var dataDose = {
										organizationId:$scope.organizationId
								}
								
								// For Get Active Speciaity List
									var URI = BASE_URL + ROOT_URL
										+ GETACTIVESPECIALITY;
								     BillingGenericService.serviceAction(METHOD_POST, URI,commonObj, NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													if (response.data.status == "success")
														$scope.specialityList = response.data.listObject;
												});
								     
						        var headerObj = {
											    unit_id : $scope.unitId,
												organization_id : $scope.organizationId
											     };
					     
								     // object for Complaints
								     $scope.dataListComplaints = [ {
								    	 	diagnosisId : '',
								    	 	icpcSystemId : '',
											since :'',
											duration:'Day',
											remark:'',
											isReviewedFlag : 'N',
											complaintAppoId : '0',
											systemName:	'',
											diagnosisName:'',
											icpcCodeText:'',
											  nos:'N',
										} ];
								     
								   //for Complaint and Diagnosis Template
								     $scope.templateListForSave=[];
									 var tempObj={};
		                        	 tempObj.templateObject={};
		                        	 tempObj.diagonisIdforTemplate=0;
		                        	 tempObj.icpcSystemId=0;
		                        	 tempObj.icpcId=0;
		                        	 $scope.templateListForSave.push(tempObj);
		                        	 
								     $scope.common = [ {
								    	 	diagnosisId : '',
								    	 	icpcSystemId : '',
										} ];
								
								     
						    		if ($scope.encounterDetailsObj == undefined) {
						    			return;
						    		}
						    		var patientId = $scope.encounterDetailsObj.patientId;
						    		var encounterId = $scope.encounterDetailsObj.encounterId;
						    		
						    		if (angular.equals({},$stateParams.obj)) {
										$state.go('OPDcoversheetNew');
										return;
									} else {
						    		if($stateParams.obj.newCasesheetList != undefined){
						    			$scope.newCasesheetList=$stateParams.obj.newCasesheetList;
						    		}
						    		if($stateParams.obj.objPrint!= undefined ){
						    			$scope.objPrint = $stateParams.obj.objPrint;
						    			$('#coversheetPdfFilePath1').attr('src',"data:application/pdf;base64,"+$scope.objPrint);
						    		}
						    		if($stateParams.obj.complaintsPatientList!= undefined ){
						    			$scope.complaintsPatientList = $stateParams.obj.complaintsPatientList;
						    			
						    		}
//						    		if($stateParams.obj.vitalsList != undefined){
//						    			$scope.vitalsList=$stateParams.obj.vitalsList;
//						    		}
						    		if($stateParams.obj.currentEncounterOrderLists!= undefined ){
						    			$scope.currentEncounterOrderLists = $stateParams.obj.currentEncounterOrderLists;
						    		}
						    		if($stateParams.obj.familyHistoryCurrentListDoctor != undefined){
						    			$scope.familyHistoryCurrentListDoctor=$stateParams.obj.familyHistoryCurrentListDoctor;
						    		}
						    		if($stateParams.obj.habitTypeList != undefined){
						    			$scope.habitTypeList=$stateParams.obj.habitTypeList;
						    		}
						    		if($stateParams.obj.patientHabitList!= undefined ){
						    			$scope.patientHabitList = $stateParams.obj.patientHabitList;
						    		}
						    		if($stateParams.obj.habitFinalList!= undefined ){
						    			$scope.habitFinalList = $stateParams.obj.habitFinalList;
						    		}
						    		if($stateParams.obj.patientHabitOldList != undefined){
						    			$scope.patientHabitOldList=$stateParams.obj.patientHabitOldList;
						    		}
						    		if($stateParams.obj.templateList!= undefined ){
						    			$scope.templateList = $stateParams.obj.templateList;
						    		}
						    		if($stateParams.obj.icpcSystemList != undefined){
						    			$scope.icpcSystemList=$stateParams.obj.icpcSystemList;
						    		}
						    		if($stateParams.obj.templateTransactionList!= undefined ){
						    			$scope.templateTransactionList = $stateParams.obj.templateTransactionList;
						    		}
						    		if($stateParams.obj.detailedMedicationsHistory != undefined){
						    			$scope.detailedMedicationsHistory=$stateParams.obj.detailedMedicationsHistory;
						    		}
						    		if($stateParams.obj.detailedMedications!= undefined ){
						    			$scope.detailedMedications = $stateParams.obj.detailedMedications;
						    		}
						    		if($stateParams.obj.allergySymbolList != undefined){
						    			$scope.allergySymbolList=$stateParams.obj.allergySymbolList;
						    		}
						    		if($stateParams.obj.allergyOldList!= undefined ){
						    			$scope.allergyOldList = $stateParams.obj.allergyOldList;
						    		}
						    		if($stateParams.obj.patientDiagnosisListIcon != undefined){
						    			$scope.patientDiagnosisListIcon=$stateParams.obj.patientDiagnosisListIcon;
						    		}
						    		if($stateParams.obj.patientDiagnosisList!= undefined ){
						    			$scope.patientDiagnosisList = $stateParams.obj.patientDiagnosisList;
						    		}
						    		if($stateParams.obj.complaintListByPatientId != undefined){
						    			$scope.complaintListByPatientId=$stateParams.obj.complaintListByPatientId;
						    		}
						    		if($stateParams.obj.pastComplaintListByPatientId!= undefined ){
						    			$scope.pastComplaintListByPatientId = $stateParams.obj.pastComplaintListByPatientId;
						    		}
						    		if($stateParams.obj.complaintsPatientListDoctor != undefined){
						    			$scope.complaintsPatientListDoctor=$stateParams.obj.complaintsPatientListDoctor;
						    		}
						    		/*if($stateParams.obj.patientViatalList != undefined){
						    			$scope.patientViatalList=$stateParams.obj.patientViatalList;
						    		}*/
						    		if($stateParams.obj.patientViatalListTrends!= undefined ){
						    			$scope.patientViatalListTrends = $stateParams.obj.patientViatalListTrends;
						    		}
						    		if($stateParams.obj.vitalDataTrends != undefined){
						    			$scope.vitalDataTrends=$stateParams.obj.vitalDataTrends;
						    		}
						    		if($stateParams.obj.vitalLabel!= undefined ){
						    			$scope.vitalLabel = $stateParams.obj.vitalLabel;
						    		}
						    		if($stateParams.obj.vitalFinalList!= undefined ){
						    			$scope.vitalFinalList = $stateParams.obj.vitalFinalList;
						    			
						    		}
						    		if($stateParams.obj.medicalHistoryCurrentList!= undefined ){
						    			$scope.medicalHistoryCurrentList = $stateParams.obj.medicalHistoryCurrentList;
						    		}
									}
						    		
						    		
							}
							/****For tab access*****/
				            var tabsList = JSON.parse(localStorage.getItem('mappedDataCoversheetByUser'));
							 
							$scope.mappedDataCoversheetByUserss = [];
							 $scope.mappedDataCoversheetByUserss =tabsList;
							 
						$scope.checkAccessCover = function (id) 
						{
								if (!$scope.mappedDataCoversheetByUserss || $scope.mappedDataCoversheetByUserss.length == 0) {
									return true;
								}
								var arr = $scope.mappedDataCoversheetByUserss.filter(function (item) 
								{
									return item.headerId == id;
								});
								return (arr.length > 0) ? true : false;
				
				        }
					$scope.test = [];
					$scope.checkAccessCoverSubTab = function (id,headerId) 
					{
							if (!$scope.mappedDataCoversheetByUserss || $scope.mappedDataCoversheetByUserss.length == 0) {
								return true;
							}
							var arr = $scope.mappedDataCoversheetByUserss.filter(function (item) 
							{
								if(headerId == item.headerId){
									if(item.selectedSubTabId.length > 0 ){
										$scope.test = [];
										$scope.test = angular.copy(item.selectedSubTabId);
									}
								}
							});
							if($scope.test.length > 0){
								
								var arr1 = $scope.test.filter(function (item1) {
									return item1.subTabId == id;
								});
								return (arr1.length > 0) ? true : false;
							}
				    }
							$scope.init();
							
						/*************************Init End****************************/	


							/** ***** END TAB FOR ALL PAGES IN TREE LEVEL ****** */
							
							/**FOR SET CODE START****** */
							$scope.tab = 1;
							$scope.isSet = function(tabNum){
								return $scope.tab === tabNum;
								};

								
							$scope.gotoNextSectionLevelOne = function(Tab){
								var obj={
										newCasesheetList:$scope.newCasesheetList,
										objPrint :$scope.objPrint,
										vitalsList:$scope.vitalsList,
										currentEncounterOrderLists : $scope.currentEncounterOrderLists,
										familyHistoryCurrentListDoctor : $scope.familyHistoryCurrentListDoctor,
										habitFinalList : $scope.habitFinalList,
										patientHabitOldList : $scope.patientHabitOldList,
										templateList : $scope.templateList,
										icpcSystemList :$scope.icpcSystemList,
										templateTransactionList: $scope.templateTransactionList,
										detailedMedicationsHistory: $scope.detailedMedicationsHistory,
										detailedMedications : $scope.detailedMedications,
										allergySymbolList :$scope.allergySymbolList,
										allergyOldList : $scope.allergyOldList,
										patientDiagnosisListIcon :$scope.patientDiagnosisListIcon,
										patientDiagnosisList: $scope.patientDiagnosisList,
										complaintListByPatientId : $scope.complaintListByPatientId,
										pastComplaintListByPatientId : $scope.pastComplaintListByPatientId,
										complaintsPatientListDoctor :$scope.complaintsPatientListDoctor,
										vitalFinalList :$scope.vitalFinalList,
										patientViatalList :$scope.patientViatalList,
										patientViatalListTrends :$scope.patientViatalListTrends,
										vitalDataTrends : $scope.vitalDataTrends,
										vitalLabel :$scope.vitalLabel,
										habitTypeList :$scope.habitTypeList,
										patientHabitList :$scope.patientHabitList,
										complaintsPatientList : $scope.complaintsPatientList,
										medicalHistoryCurrentList :$scope.medicalHistoryCurrentList
								};
								var id='';
								if(Tab == 1){
									var id= "complaint_section";
									$state.go('complaintsTab', {obj : obj});
								}
								if(Tab == 2){
									var id= "past_history_section";
									$state.go('medicalHistoryTab', {obj : obj});
								}
								if(Tab == 3){
									var id= "drug_history_tab";
									$state.go('drugHistoryTab', {obj : obj});
								}
								if(Tab == 4){
									var id= "social_history";
									$state.go('socialHistoryTab', {obj : obj});
								}
								if(Tab == 5){
									var id= "allergies_section";
									$state.go('allergiesTab', {obj : obj});
								}
								if(Tab == 6){
									var id= "vitals_section";
									$state.go('vitalsTab', {obj : obj});
								}
								if(Tab == 7){
									var id= "examination_tab";
									$state.go('examinationTab', {obj : obj});
								}
								if(Tab == 8){
									var id= "diagnosis_section";
									$state.go('diagnosisTab', {obj : obj});
								}
								if(Tab == 9){
									var id= "orders_section";
									$state.go('ordersTab', {obj : obj});
								}
								if(Tab == 10){
									var id= "notes_section";
									$state.go('notesTab', {obj : obj});
								}
								if(Tab == 11){
									var id= "advice_section";
									$state.go('adviceTab', {obj : obj});
								}
								if(Tab == 12){
									var id= "followUp_section";
									$state.go('follwUpTab', {obj : obj});
								}
								if(Tab == 13){
									var id= "attach_to_casesheet";
									$state.go('attachToCasesheetTab', {obj : obj});
								}
								if(Tab == 14){
									var id= "letters_section";
									$state.go('letterTab', {obj : obj});
								}
								
							}
							$scope.getDefaultTabOpen=function(tabNo){
								if(tabNo==5){
									$scope.setSecondTabLevel(5);
								}
								if(tabNo==1){
									
									$scope.setSecondTabLevel(1);
									
									 $timeout(function() {
//			              				 $("#searchServiceId").focus();
										 $(document).ready(function () {
											  $('#searchServiceId').focus();
											});
			              			}, 1000);
								}
								if(tabNo==21){
									$scope.setSecondTabLevel(21);
								}
							}
							/**FOR SET CODE END*******/
							$scope.encounterDetailsList = [];
							$scope.encounterDetails = {
								  encounterDocId : "",
								  encounterId : "",
								  admissionId:"",
								  visitTypeId:2,
								  patientId : "",
								  documentPath : "",
								  documentTypeId : "",
								  status : 'A',
								  createdDate : "",
								  createdBy : $scope.userId,
								  updatedDate : "",
								  updatedBy : $scope.userId,
								  unitId : $scope.unitId,
								  organisationId : $scope.organizationId,
								  file : {}
								}
						  
							/********************************complaints tab start**************************/
							var headerObj = {
								unit_id : $scope.unitId,
								organization_id : $scope.organizationId
							};
						     
							var paramObj = {
								encounterId : $scope.encounterDetailsObj != undefined ? $scope.encounterDetailsObj.encounterId : undefined,
								roleId : $scope.roleId
							};
							
						    var data ={};
						    
							/****For Get Active Complaints List*/
							$scope.getActiveComplaintlist = function() {

								var paramObj = {
									encounterId : $scope.encounterDetailsObj.encounterId,
									roleId : $scope.roleId,
                                    unit_id : $scope.unitId,
                                    organization_id : $scope.organizationId
								};
								var data = {};
								var URI = BASE_URL + ROOT_URL + GETCOMPLAINTSLIST;
								GenericServiceParamHeader.serviceAction(METHOD_GET, URI, data, NOTIFICATION_MSG_STATUS_FALSE, paramObj, headerObj)
										.then(function(response) {
													if (response.data.status == "success")
														$scope.complaintsPatientList = response.data.listObject;
													angular.forEach($scope.complaintsPatientList,function(value,key){
																		value.editComplaint = false;
													});
													$scope.complaintsPatientListDoctor = [];
													if ($scope.roleId == '2') {
														angular.forEach(response.data.listObject,function(value,key) {
															if (value.roleId == 3 && value.isReviewedFlag == 'N'){
																var data = {};
																data.diagnosisId = value.diagnosisId;
																data.since = value.since;
																data.duration = value.duration;
																data.remark = value.remark;
																		data.isReviewedFlag = 'N',
																		data.complaintAppoId = value.complaintAppoId;
																$scope.complaintsPatientListDoctor
																		.push(data);
															}
														});
													}
												});
							 }
						     $scope.getActiveComplaintlist();
						     
						     /****For Get Complaints List by patient id*/
							  $scope.getComplaintlistByPatientId = function() {
								$scope.complaintListByPatientId = [];
								var headerObj = {
									organization_id : $scope.organizationId
								};

								var paramObj = {
									patientId : $scope.encounterDetailsObj.patientId,
								};
								var data = {};
								var URI = BASE_URL + ROOT_URL + "/api/opd/getComplaintAppointmentByPatientId";
								GenericServiceParamHeader.serviceAction(METHOD_GET, URI, data,NOTIFICATION_MSG_STATUS_FALSE,paramObj, headerObj)
										.then(function(response){
											if (response.data.status == "success")
												$scope.complaintListByPatientId = response.data.listObject;
											$scope.complaintsPatientListDoctor1 = [];
													if ($scope.roleId == '2') {
														angular.forEach(response.data.listObject,function(value,key) {
															if (value.roleId == 3 && value.isReviewedFlag == 'N') {
																var data = {};
																data.diagnosisId = value.diagnosisId;
																data.since = value.since;
																data.duration = value.duration;
																data.remark = value.remark;
																		data.isReviewedFlag = 'N',
																		data.complaintAppoId = value.complaintAppoId;
																$scope.complaintsPatientListDoctor1
																		.push(data);
															}
														});
													}
												});
							 }
						     $scope.getComplaintlistByPatientId();
						     
						     /****For Get Past Complaints List by patient id*/
						 $scope.getPastComplaintlistByPatientId = function() {
								$scope.pastComplaintListByPatientId = [];
									var headerObj = {
										organization_id : $scope.organizationId
									};

								var paramObj = {
									patientId : $scope.encounterDetailsObj.patientId,
								};
								
								var data = {};
 								var URI = BASE_URL+ ROOT_URL+ "/api/opd/getPastComplaintAppointmentByPatientId";
								GenericServiceParamHeader.serviceAction(METHOD_GET, URI, data,NOTIFICATION_MSG_STATUS_FALSE,paramObj, headerObj)
										.then(function(response) {
											if (response.data.status == "success")
												$scope.pastComplaintListByPatientId = response.data.listObject;
										});
						    	}
						 
						     $scope.icpcMasterList=[];
							 $scope.icpcSystemList = [];
								var data = {
											specialityId : $scope.encounterDetailsObj.specialityId,
											organizationId : $scope.organizationId,
											unitId : $scope.unitId
										};

							var URI = BASE_URL + ROOT_URL + "/api/adt/getICPCSystemList";
							GenericServiceParamHeader.serviceAction(METHOD_POST, URI, data)
									.then(function(response) {
										if (response.data.status == "success") {
											$scope.icpcSystemList = response.data.listObject;
										}

									});
							
							
								$scope.text=function(info,index){
				    				if(info=='Y'){
				    				$scope.dataListComplaints[index].diagnosisId=51;
				    				$scope.dataListComplaints[index].icpcSystemId=1;
				    				}
				    				if(info=='N'){
				    					$scope.dataListComplaints[index].diagnosisId="";
				    					$scope.dataListComplaints[index].icpcCodeText="";
				    				}
				    				
				    			}
		    									
		    						//new save with voice
		    								$scope.complaintsPatientListDoctor = [];
		    								
		    								$scope.removeComplaint=function(info){
		    									$scope.complaintAppoId = info.complaintAppoId;
		    									$scope.isComplaintStop = "Y";
		    								}
		    								$scope.common=[];
		    								$scope.complaintDelete= true;
		    								$scope.cancelComplaint=function(){
		    								
		    									var date = new Date();
			    								$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
			    								var data ={
			    										
			    										updatedBy:$scope.userId,
					    								updatedDate:$scope.dt,
					    								complaintAppoId : $scope.complaintAppoId,
					    								isComplaintStop : $scope.isComplaintStop,
					    								justificationComplaint:$scope.justificationComplaint
			    								};
			    							if($scope.justificationComplaint != null && $scope.justificationComplaint !="" && $scope.justificationComplaint != undefined){
			    								var URI = BASE_URL + ROOT_URL + "/api/opd/updateComplaintAppointmentStatusToRemoveComplaint";
			    								GenericService.serviceAction("POST",URI,data).then(function(response){
			    									if (response.data.status == 'success')
			    									{
			    										
			    										$scope.justificationComplaint="";
			    										$rootScope.stopLoader();
			    										growl.success(response.data.message,
			    												{
			    													title : 'Success!'
			    												});
			    										$scope.getActiveComplaintlist();
			    										$scope.getComplaintlistByPatientId();
			    									} else if(response.data.status == 'error')
			    									{
			    										$rootScope.stopLoader();
			    										growl.warning(response.data.message,
			    												{
			    													title : 'Warning!'
			    												});
			    									}
			    								});
			    								
			    							}
			    							else{
			    								 growl.warning(" Please enter reason. ",
															{
																title : 'warning!'
															});
			    							}
		    									
		    								}
		    								
		    				/***Save complaints***/
							$scope.saveComplaints = function()
						    {
								var data = {
										ceratedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										updatedBy : $scope.userId,
										createdBy : $scope.userId,
										status : 'A',
										roleId : $scope.roleId,
										encounterId : $scope.encounterDetailsObj.encounterId,
										patientId : $scope.encounterDetailsObj.patientId
									};
									$scope.complaintsPatientListDoctor = [];
									$scope.validateSurHisRow = true;
									var jsonData = '';
									var myEle = document.getElementById("jsonmessage");
									if (myEle) {
										jsonData = myEle.value;
										if (jsonData != null
												&& jsonData != undefined
												&& jsonData != '') {
											jsonData = JSON.parse(jsonData);
										}
									}
									if (jsonData != null && jsonData != undefined
											&& jsonData != '') {
										$rootScope.startLoader();
										$scope.durDMY = '';
										if (jsonData.durationtype == 'months') {
											$scope.durDMY = 'Month';
										}
										if (jsonData.durationtype == 'years') {
											$scope.durDMY = 'Year';
										} else {
											$scope.durDMY = 'Day';
										}
		
										var icpcSystemId1 = $
												.grep(
														$scope.icpcSystemList,
														function(icpc) {
															return icpc.systemName == jsonData.ICPC_system;
														})[0].icpcSystemId;
		
										var URI = BASE_URL+ ROOT_URL+ "/api/opd/getMasterListOfICPCSystem/"+ icpcSystemId1 + "/"+ jsonData.ICPC;
		
										return BillingGenericService.serviceAction(METHOD_GET, URI,NOTIFICATION_MSG_STATUS_FALSE)
												.then(function(response) {
															if (response.data.status == "success") {
																$scope.icpcMasterList = response.data.listObject[0];
		
																var dData = {
																	diagnosisId : $scope.icpcMasterList.icpcId,
																	duration : $scope.durDMY,
																	since : jsonData.duration,
																	remark : '',
																	isReviewedFlag : 'Y',
																	complaintAppoId : '0',
																	icpcSystemId : icpcSystemId1,
																	icpcCodeText : ''
																}
																$scope.complaintsPatientListDoctor
																		.push(dData);
																data.listComplaintAppointmentDetailsDto = $scope.complaintsPatientListDoctor;
		
																var paramObj = {};
																var headers = {
																	'organization_id' : $scope.organizationId,
																	'unit_id' : $scope.unitId,
																	'Content-Type' : 'application/json'
																};
																var URI = BASE_URL+ ROOT_URL+ SAVECOMPLAINTSLIST + "/"+ $scope.unitId +"/" + $scope.organizationId;
																$scope.complaintSaveListLength = data.listComplaintAppointmentDetailsDto.length;
		
																GenericServiceParamHeader.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE,paramObj,headers)
																		.then(function(response) {
																					if (response.data.status == "success")
																						var i;
																					$scope.selectedTempList = [];
																					for (i = 0; i < $scope.complaintSaveListLength; i++) {
																						if (isEmpty($scope.templateListForSave[i].templateObject) == true) {
																							$scope.selectedTempList.push($scope.templateListForSave[i]);
																						}
																					}
																					if ($scope.selectedTempList.length > 0) {
																						growl.success("Complaint and assessment recorded successfully.",
																										{
																											title : 'Success!'
																										});
																					} else {
																						growl.success("Complaint recorded successfully.",
																										{
																											title : 'Success!'
																										});
																					}
																					var promise = {};
																					if ($scope.selectedTempList.length > 0)
																						promise = $scope.saveTemplateTransactionList($scope.selectedTempList);
																					if (isEmpty(promise) == false) {
																						$scope.getActiveComplaintlist();
																					} else {
																						promise.then(function(response) {
																									$rootScope.stopLoader();
																									$scope.getActiveComplaintlist();
																								});
																					}
	                                                                                $scope.diagnosisName= "";
																					$scope.getAllPatientDiagnosis();
																					$scope.init();
																					$scope.icpcMasterList = [];
																					$scope.dataListComplaints.diagnosisName = "";
																					$rootScope.stopLoader();
																				});
															} else {
																growl.warning("ICPC Not Found..!!",
																				{
																					title : 'Warning!'
																				});
																$rootScope.stopLoader();
															}
														});
		
									} else {
										// For Screen
										$rootScope.startLoader();
										if ($scope.roleId == 3) {
											angular.forEach($scope.dataListComplaints,function(value, key) {
												if (value.diagnosisId == 51 && value.nos == 'Y') {
																	if (value.icpcCodeText == "" && value.diagnosisName == "") {
																		growl.warning("Please enter the ICPC description.",
																						{
																							title : 'Warning!'
																						});
																		$rootScope.stopLoader();
																		$scope.validateSurHisRow = false;
																		return false;
																	}
																}
		
																if (value.diagnosisId > 0) {
																	// added by
																	// dinesh for
																	// already exist
																	// code start
																	if ($scope.complaintsPatientList != undefined && $scope.complaintsPatientList.length > 0) {
																		var chkStatus = $scope.complaintsPatientList .filter(function(item) {
																					return (item.diagnosisId == value.diagnosisId && (value.icpcCodeText == undefined || value.icpcCodeText == null || value.icpcCodeText == "") && (value.nos == 'N' || value.nos == undefined || value.nos == ""));
																				})
																		if (chkStatus.length > 0) {
																			growl.warning("Already exist",
																							{
																								title : 'Warning!'
																							});
																			$rootScope.stopLoader();
																			$scope.validateSurHisRow = false;
																			return false;
																		}
																	}
																	if (value.icpcCodeText != "") {
																		if ($scope.complaintsPatientList != undefined && $scope.complaintsPatientList.length > 0) {
																			var chkStatus = $scope.complaintsPatientList.filter(function(item) {
																						return (item.diagnosisId == value.diagnosisId && item.icpcCodeText == value.icpcCodeText);
																					})
																			if (chkStatus.length > 0) {
																				growl.warning("Already exist",
																								{
																									title : 'Warning!'
																								});
																				$rootScope.stopLoader();
																				$scope.validateSurHisRow = false;
																				return false;
																			}
																		}
																	}
																	// added by
																	// dinesh for
																	// already exist
																	// code end
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
																	$scope.complaintsPatientListDoctor
																			.push(dData);
																	data.listComplaintAppointmentDetailsDto = $scope.complaintsPatientListDoctor;
		
																} else {
																	growl.warning("Please Enter ICPC",
																					{
																						title : 'Warning!'
																					});
																	$scope.validateSurHisRow = false;
																	$rootScope.stopLoader();
																	return false;
																}
		
															});
											if ($scope.validateSurHisRow) {
												var paramObj = {};
//												var headers = {
//													'organization_id' : $scope.organizationId,
//													'unit_id' : $scope.unitId,
//													'Content-Type' : 'application/json'
//												};
												var URI = BASE_URL + ROOT_URL
														+ SAVECOMPLAINTSLIST + "/"+ $scope.unitId +"/" + $scope.organizationId;
												$scope.complaintSaveListLength = data.listComplaintAppointmentDetailsDto.length;
		
												GenericServiceParamHeader.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE,paramObj, headers)
														.then(function(response) {
																	if (response.data.status == "success")
																		// save
																		// complaint
																		// template
																		var i;
																	$scope.selectedTempList = [];
																	for (i = 0; i < $scope.complaintSaveListLength; i++) {
																		if (isEmpty($scope.templateListForSave[i].templateObject) == true) {
																			$scope.selectedTempList
																					.push($scope.templateListForSave[i]);
																		}
																	}
																	if ($scope.selectedTempList.length > 0) {
																		growl.success("Complaint and assessment recorded successfully.",
																						{
																							title : 'Success!'
																						});
																	} else {
																		growl.success("Complaint recorded successfully.",
																						{
																							title : 'Success!'
																						});
																	}
																	var promise = {};
																	if ($scope.selectedTempList.length > 0)
																		promise = $scope.saveTemplateTransactionList($scope.selectedTempList);
																	if (isEmpty(promise) == false) {
																		$scope.getActiveComplaintlist();
																	} else {
																		promise.then(function(response) {
																					$rootScope.stopLoader();
																					$scope.getActiveComplaintlist();
																				});
																	}
																	$scope.init();
																	$scope.getComplaintlistByPatientId();
																	$scope.getAllPatientDiagnosis();
																	$scope.icpcMasterList = [];
																	$scope.dataListComplaints.diagnosisName = '';
																	$scope.diagnosisName = "";
																	$scope.icpcSystemList = $scope.icpcSystemListTemporary;
																});
											}
										} else {
		
											angular.forEach($scope.dataListComplaints,function(value, key) {
												if (value.diagnosisId == 51 && value.nos == 'Y') {
																	if (value.icpcCodeText == "" && value.diagnosisName == "") {
																		growl.warning("Please enter the ICPC description.",
																						{
																							title : 'Warning!'
																						});
																		$rootScope.stopLoader();
																		$scope.validateSurHisRow = false;
																		return false;
																	}
																}
		
																if (value.diagnosisId > 0) {
		
																	// added by
																	// dinesh for
																	// already exist
																	// code start
																	// added by dinesh for
																	// already exist code start
						    	    								/**commented by chetana for save free text complaints*/
						    	    								if($scope.complaintsPatientList!=undefined && $scope.complaintsPatientList.length>0){
						    	    									var chkStatus= $scope.complaintsPatientList.filter(function(item){ return 
						    	    										(item.diagnosisId==value.diagnosisId && (value.icpcCodeText == undefined || value.icpcCodeText == null || value.icpcCodeText == "") && (value.nos=='N' || value.nos==undefined ||  value.nos == "" ) ); })
							    	    								if(chkStatus.length>0){
							    	    									growl.warning("Already exist",
							    			    	    							{
							    			    	    							title : 'Warning!'
							    			    	    							});
							    			    	    							$rootScope.stopLoader();
							    			    	    							$scope.validateSurHisRow = false;
							    			    	    							return false;
							    	    								}
						    	    								}
						    	    								
																	if (value.icpcCodeText != "") {
																		if ($scope.complaintsPatientList != undefined && $scope.complaintsPatientList.length > 0) {
																			var chkStatus = $scope.complaintsPatientList.filter(function(item) {
																						return (item.diagnosisId == value.diagnosisId && item.icpcCodeText == value.icpcCodeText);
																					})
																			if (chkStatus.length > 0) {
																				growl.warning("Already exist",
																								{
																									title : 'Warning!'
																								});
																				$rootScope.stopLoader();
																				$scope.validateSurHisRow = false;
																				return false;
																			}
																		}
																	}
																	// added by
																	// dinesh for
																	// already exist
																	// code end
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
																	$scope.complaintsPatientListDoctor.push(dData);
																	data.listComplaintAppointmentDetailsDto = $scope.complaintsPatientListDoctor;
		
																} else {
																	growl.warning("Please Enter ICPC",
																					{
																						title : 'Warning!'
																					});
																	$rootScope
																			.stopLoader();
																	$scope.validateSurHisRow = false;
																	return false;
																}
															});
											
											if ($scope.validateSurHisRow) {
												var paramObj = {};
//												var headers = {
//													'organization_id' : $scope.organizationId,
//													'unit_id' : $scope.unitId,
//													'Content-Type' : 'application/json'
//												};
												var URI = BASE_URL + ROOT_URL + SAVECOMPLAINTSLIST + "/"+ $scope.unitId +"/" + $scope.organizationId;
												$scope.complaintSaveListLength = data.listComplaintAppointmentDetailsDto.length;
												GenericServiceParamHeader.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE,paramObj, headers)
														.then(function(response) {
																	if (response.data.status == "success")
																		// save
																		// complaint
																		// template
																		var i;
																	$scope.selectedTempList = [];
																	for (i = 0; i < $scope.complaintSaveListLength; i++) {
																		if (isEmpty($scope.templateListForSave[i].templateObject) == true) {
																			$scope.selectedTempList.push($scope.templateListForSave[i]);
																		}
																	}
																	if ($scope.selectedTempList.length > 0) {
																		growl.success("Complaint and assessment recorded successfully.",
																						{
																							title : 'Success!'
																						});
																	} else {
																		growl.success("Complaint recorded successfully.",
																						{
																							title : 'Success!'
																						});
																	}
																	var promise = {};
																	if ($scope.selectedTempList.length > 0)
																		promise = $scope.saveTemplateTransactionList($scope.selectedTempList);
																	if (isEmpty(promise) == false) {
																		$scope.getActiveComplaintlist();
																		$scope.getComplaintlistByPatientId();
																	} else {
																		promise.then(function(response) {
																					$rootScope.stopLoader();
																					$scope.getActiveComplaintlist();
																					$scope.getComplaintlistByPatientId();
																				});
																	}
																	$scope.init();
																	$scope.getAllPatientDiagnosis();
																	$scope.getComplaintlistByPatientId();
																	$scope.icpcMasterList = [];
																	$scope.dataListComplaints.diagnosisName = "";
																	$scope.diagnosisName = "";
																	$scope.icpcSystemList = $scope.icpcSystemListTemporary;
																});
											}
										}
									}
									}
		    								
		    			
		    								 //--------Resolved Complaint-----------//
		    								$scope.selectedResolvedComplaintData=function(info){
		    									$scope.complaintAppoId= info.complaintAdmissionId;
		    								}
		    								
		    								$scope.openResolvedComplaintDatePicker = function(index) {
		    									$scope.resolvedComplaintDatepicker = {
		    										opened : [ ],
		    									};		
		    									if($scope.allergyCurrentList != undefined){
		    										angular.forEach($scope.complaintsPatientList, function(val) {
		    											$scope.resolvedComplaintDatepicker.opened.push(false);
		    										});
		    									}
		    									for (var i = 0; i < $scope.complaintsPatientList.length; i++) {
		    										if (i === index) {
		    											$scope.resolvedComplaintDatepicker.opened[i] = true;
		    										} else {
		    											$scope.resolvedComplaintDatepicker.opened[i] = false;
		    										}
		    									}
		    								};

		    								$scope.saveResolvedComplaintDate=function(resolvedComplaintDate, resolvedRemarkComplaint,id, index){
		    									$rootScope.startLoader();
		    									try{

		    										if($scope.visitTypeId==null){
		    										var data={
		    													complaintAppoId:id,
		    													resolvedDate:resolvedComplaintDate!= undefined ? moment(resolvedComplaintDate).format('YYYY-MM-DD HH:mm:ss'):moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
		    													resolvedRemark:resolvedRemarkComplaint != null ?resolvedRemarkComplaint :"Resolved",
		    													isResolved:'Y',
		    												
		    										};
		    										
		    										if((resolvedRemarkComplaint!= null && resolvedRemarkComplaint!="" && resolvedRemarkComplaint!= undefined)){
		    										var URI = BASE_URL + ROOT_URL + "/api/opd/saveResolvedComplaintAppointment";
		    										GenericService.serviceAction("POST",URI,data).then(function(response){
		    											if (response.data.status == 'success')
		    											{
		    												$rootScope.stopLoader();
		    												growl.success(response.data.message,
		    														{
		    															title : 'Success!'
		    														});
		    												$scope.resolvedComplaintDate="";
		    												$scope.resolvedRemarkComplaint="";
		    												$scope.getActiveComplaintlist();
		    												$scope.init();
		    												$scope.getComplaintlistByPatientId();
		    												$scope.getPastComplaintlistByPatientId();
//		    												$scope.getPatientDiagnosis();
		    											} else if(response.data.status == 'error')
		    											{
		    												$rootScope.stopLoader();
		    												growl.warning(response.data.message,
		    														{
		    															title : 'Warning!'
		    														});
		    											}
		    										});
		    									}else{
		    										growl.warning("Please enter date and remark.",
		    											{
		    												title : 'warning!'
		    											});
		    										$rootScope.stopLoader();
		    								}
		    										}else{
		    											var data={
		    													complaintAdmissionId:$scope.complaintAppoId,
		    													resolvedDate:resolvedComplaintDate!= undefined ? moment(resolvedComplaintDate).format('YYYY-MM-DD HH:mm:ss'):moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
				    											resolvedRemark:resolvedRemarkComplaint != null ?resolvedRemarkComplaint :"Resolved",
		    													isResolved:'Y',
		    												
		    										};
		    										
		    											if((resolvedRemarkComplaint!= null && resolvedRemarkComplaint!="" && resolvedRemarkComplaint!= undefined)){
		    										var URI = BASE_URL + ROOT_URL + "/api/ipd/saveResolvedComplaintAdmission";
		    										GenericService.serviceAction("POST",URI,data).then(function(response){
		    											if (response.data.status == 'success')
		    											{
		    												$rootScope.stopLoader();
		    												growl.success(response.data.message,
		    														{
		    															title : 'Success!'
		    														});
		    												$scope.resolvedComplaintDate="";
		    												$scope.resolvedRemarkComplaint="";
		    												$scope.getActiveComplaintlist();
		    												$scope.init();
		    												$scope.getComplaintlistByPatientId();
		    												$scope.getPastComplaintlistByPatientId();
//		    												$scope.getPatientDiagnosis();
		    											} else if(response.data.status == 'error')
		    											{
		    												$rootScope.stopLoader();
		    												growl.warning(response.data.message,
		    														{
		    															title : 'Warning!'
		    														});
		    											}
		    										});
		    									}else{
		    										growl.warning("Please enter date and remark.",
		    											{
		    												title : 'warning!'
		    											});
		    										$rootScope.stopLoader();
		    								}
		    										}
		    									
//		    										}
		    									}catch(e){
		    										console.log("Exception",e);
		    									}
		    								}
		    								 //---------------//
		    				
								var headerObj = {
							    		 unit_id : $scope.unitId,
											organization_id : $scope.organizationId
							     };
							     
							     var paramObj = {
							    		 encounterId : $scope.encounterDetailsObj.encounterId,
							    		 roleId : $scope.roleId
								    		 // AppointmentId : 14,
								     };
							     
							     console.log(headerObj);
								// START COMPLAINTS
								var headerObj = {
							    		 unit_id : $scope.unitId,
											organization_id : $scope.organizationId
							     };
							     
							     var paramObj = {
							    		 encounterId : $scope.encounterDetailsObj.encounterId,
							    		 roleId : $scope.roleId
								    		 // AppointmentId : 14,
								     };
							     
							     console.log(headerObj);
								
                                 $scope.checkedRadioComp = function(info,idx,val)
									{
										// alert(idx);
										if(val =='Day')
											{
											$('#comp_day_id'+idx).prop('checked', true);
                                            $scope.showUpdateComplaints(info,idx,val);

											}
										else if(val == 'Month')
											{
											$('#comp_day_id'+idx).prop('checked', true);
                                            $scope.showUpdateComplaints(info,idx,val);

											}
										else
											{
											$('#comp_day_id'+idx).prop('checked', true);
                                            $scope.showUpdateComplaints(info,idx,val);

											}
									
									}
							     
							     $scope.editUpdateComplaints = function(item,idx)
									{
							    	 angular.forEach($scope.complaintsPatientList, function(value,index) {
							    		 
							    		  value.editComplaint = false;
				                           if(index == idx){
				                        	   value.editComplaint = true;
				                        	   
				                        	   $timeout(function() {
						              				 $("#compIdFocus_"+idx).focus();
						              			}, 200);
				                           }
				                          
										});
							    	
									}
							     
							     
							     
							     $scope.getMostSavedComplaintList = function()
							        {
							        	var data ={
							        			organizationId :$scope.organizationId,
							        			unitId : $scope.unitId
							        	};
							        	
							    		var URI = BASE_URL + ROOT_URL + "/api/opd/getMostSavedComplaint;";
										GenericServiceParamHeader.serviceAction(METHOD_POST, URI, 
												data, NOTIFICATION_MSG_STATUS_FALSE)
												.then(function(response) {
													if(response.data.status == "success"){
														$scope.getMostSavedComplaintListt = [];
														$scope.getMostSavedComplaintListt = response.data.listObject;
														console.log("fav getMostSavedComplaintListt",$scope.getMostSavedComplaintListt);
													}
										});
							        }
							     $scope.getMostSavedComplaintList();
							     
							     $scope.updateComplaintIndex="";
								$scope.showUpdateComplaints = function(item,idx,val)
								{
									$scope.freeTextFlag=false;
									if(item.icpcCodeText != ""){
										$scope.freeTextFlag=true;
									}
									$scope.updateComplaintIndex=idx;
									$scope.complaintsUpdateObj = {
											diagnosisName:item.diagnosisName,
											diagnosisId : item.diagnosisId,
											duration: val != undefined ? val :item.duration,
											since:item.since,
											remark : item.remark,
											icpcSystemId  : item.icpcSystemId.toString(),
											icpcCodeText:item.icpcCodeText,
											systemName:item.systemName
											
									};
									$scope.diagnosisNameUpdate = item.diagnosisName;
									$scope.complaintAppoId = item.complaintAppoId;
									$scope.systemNameUpdate = item.systemName;
									$scope.itemRoleId = item.roleId;
									if(item.since != null || item.since != "" || item.remark != null || item.remark != ""){
										$scope.updateComplaints();
									}
									
								}
								
								$scope.selectDiagnosisIdForComplaintsUpdate = function(info, model,
										label,ind) {
									console.log(info);
								$scope.complaintsUpdateObj.diagnosisId = info.diagnosisId;
								
								}
								
								
								$scope.selecticpcSystemIdForComplaintsUpdate = function(info, model,
										label,ind) {
									console.log(info);
								$scope.complaintsUpdateObj.icpcSystemId = info.icpcSystemId;
								
								}
								
								 $scope.showConfirmationPopup=function(index){
									 if(($scope.complaintsPatientList[index].diagnosisId!=$scope.complaintsUpdateObj.diagnosisId)
											 || ($scope.complaintsPatientList[index].icpcSystemId != $scope.complaintsUpdateObj.icpcSystemId)){
		                           		 $('#confirmComplaintPopup').modal();
			                           	}else{
			                               $scope.updateComplaints();
			                           	}
		                            }
								$scope.updateComplaints = function()
								{
									$scope.dataListComplaints = [];
									 if(!$scope.freeTextFlag){
											$scope.dataListComplaints.push($scope.complaintsUpdateObj);
			                              }else{
			                            	if($scope.complaintsUpdateObj.diagnosisId == 51 && $scope.complaintsUpdateObj.icpcCodeText != ""){
			                            		$scope.dataListComplaints.push($scope.complaintsUpdateObj);
			                            	   }
			                            	else if($scope.complaintsUpdateObj.diagnosisId != 51 && $scope.complaintsUpdateObj.icpcCodeText !=""){
			                            		$scope.complaintsUpdateObj.icpcCodeText = "";
			                            		$scope.dataListComplaints.push($scope.complaintsUpdateObj);
			                            	}
			                            	else{
			                            		growl.warning(
			                            				"Please enter the ICPC description.",
					        	 						{
					        	 							title : 'Warning!'
					        	 						});
			                            		var data = {
			        									diagnosisId : '',
			        									since : '',
			        									duration : 'Day',
			        									remark : '',
			        									isReviewedFlag : 'N',
			        									complaintAdmissionId : '0',
			        									icpcCodeText:''
			        								};
			        								$scope.dataListComplaints.push(data);
			                            		$rootScope.stopLoader();
			                            		return false;
			                            	}
			                            }
									  $('#closeBtnVerifyModal').trigger('click');
		                                $('#closeUpdateComplaintModal').trigger('click');
											var data = {
											ceratedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedBy:$scope.userId,
											createdBy:$scope.userId,
											status:'A',
											roleId : $scope.roleId,
											encounterId: $scope.encounterDetailsObj.encounterId,
											patientId: $scope.encounterDetailsObj.patientId,
											complaintAppoId : $scope.complaintAppoId,
											listComplaintAppointmentDetailsDto:$scope.dataListComplaints
											
									};
										console.log("$scope.dataListComplaints", $scope.dataListComplaints)	
							
									console.log(data);
									var paramObj = {};
									var headers={
											'organization_id': $scope.organizationId,
											'unit_id':$scope.unitId,
											'Content-Type':'application/json'
									};
									
									var URI = BASE_URL + ROOT_URL
									+ UPDATECOMPLAINTAPPOINTMENT;
							
									GenericServiceParamHeader
									.serviceAction(METHOD_POST, URI,
											data,
											NOTIFICATION_MSG_STATUS_FALSE,paramObj,headers)
									.then(
											function(response) {
												console.log(response);
												if (response.data.status == "success")
													
													var i;
												$scope.selectedTempList=[];
												for(i=0;i<$scope.dataListComplaints.length;i++){
												 if(isEmpty($scope.templateListForSave[i].templateObject)==true){
													 $scope.selectedTempList.push($scope.templateListForSave[i]);
												  }
												}
												if($scope.selectedTempList.length > 0){
													 growl.success("Complaint and assessment updated successfully.",{
										    				title : 'Success!'
										    			 });
												}else{
													growl.success("Complaint updated successfully.",{
									    				title : 'Success!'
									    			 });
												}
												    var promise={};
													if($scope.selectedTempList.length > 0)
													promise=$scope.saveTemplateTransactionList($scope.selectedTempList);
													if(isEmpty(promise)==false){
														$scope.getActiveComplaintlist();
													}else{
												     	promise.then(function(response) {
												       $rootScope.stopLoader();
														$scope.getActiveComplaintlist();
													});
													}
													
													$scope.init();
													$scope.getComplaintlistByPatientId();
    												$scope.getPastComplaintlistByPatientId();
    												//$scope.getPatientDiagnosis();
												    //$scope.getActiveComplaintlist();
													return response.data.listObject;
											});
								}
						
							
							/********************************complaints tab End**************************/
                        	
						
			                    	
				           /****Patient diagnosis list ****/         
				           $scope.comorbidityFlag = false;
							$scope.getAllPatientDiagnosis=function(){
								 var headerObjDia = {
										 	'unit_id' : $scope.unitId,
											'organization_id' : $scope.organizationId,
											'patient_id' : $scope.encounterDetailsObj.patientId,
											'Content-Type':'application/json',
							     };
								 var data={};
	                             var URI = BASE_URL + ROOT_URL
	                             + GETPATIENTDIAGNOSISBYPATIENTID;
	                         GenericServiceParamHeader.serviceAction(METHOD_GET, URI, data,NOTIFICATION_MSG_STATUS_FALSE,data,headerObjDia)
	                             .then(function(response) {
	                                         if (response.data.status == "success")
	                                             $scope.patientDiagnosisList = response.data.listObject; 
	                                             $scope.complaintDelete=true;
	                                         if($scope.patientDiagnosisList.length > 0)
												{
												  $scope.complaintDelete=false;
												  $scope.comorbidityFlag = true;
												}
	                                         
	                                         $scope.finalString="";
	                                         angular.forEach($scope.patientDiagnosisList,function(value,index){
	                                        	 if(index == 0){
	                                        		 if(value.diagnosisType == 1){
	                                        			 var ext =  "(Prov)";
	                                        		 }else{
	                                        			 var ext =  "(F)";
	                                        		 }
	                                        		 
	                                        	   var String = value.diagnosisName +" "+ext+" ";
	                                        	   $scope.finalString= $scope.finalString.concat(String);
	                                        	 }
	                                        	 else{
	                                        		 
	                                        		 if(value.diagnosisType == 1){
	                                        			 var ext =  "(Prov)";
	                                        		 }else{
	                                        			 var ext =  "(F)";
	                                        		 }
	                                        		 
	                                        	      var String = value.diagnosisName +" "+ext+" ";
		                                        	   $scope.finalString= $scope.finalString.concat(","+String);
	                                        	 }
	                                         });
	                                        
	                                     });
								}
							$scope.getAllPatientDiagnosis();
							
							/********Patient diagnosis list for icon*************/
							$scope.getAllPatientDiagnosisForIcon=function(){
								$scope.patientDiagnosisListIcon =[];
								 var headerObjDia = {
										 	'unit_id' : $scope.unitId,
											'organization_id' : $scope.organizationId,
											'patient_id' : $scope.encounterDetailsObj.patientId,
											'Content-Type':'application/json',
							     };
								 var data={};
	                             var URI = BASE_URL + ROOT_URL
	                             + "/api/opd/getPatientDiagnosisByPatientIdForIcon";
	                         GenericServiceParamHeader
	                             .serviceAction(METHOD_GET, URI,
	                                     data,
	                                     NOTIFICATION_MSG_STATUS_FALSE,data,headerObjDia)
	                             .then(
	                                     function(response) {
	                                         console.log(response);
	                                         // $rootScope.startLoader();
	                                         if (response.data.status == "success")
	                                             $scope.patientDiagnosisListIcon = response.data.listObject; 
	                                         console.log("patientDiagnosisListIcon",$scope.patientDiagnosisListIcon);
//	                                         	$scope.comorbidityFlag = false;
	                                         	$scope.notifiableFlag = false;
	                                         	$scope.complaintDelete=true;
													if($scope.patientDiagnosisListIcon.length > 0)
														{
														    $scope.complaintDelete=false;
															angular.forEach($scope.patientDiagnosisListIcon,function(value,key) {
																if(value.isNotifiable == 'Y')
																{
																	$scope.notifiableFlag = true;
																}
																else if(value.isComorbidity == 'Y')
																{
//																	$scope.comorbidityFlag = true;
																}
															});
														}
	                                         
	                                     });
								}
							$scope.getAllPatientDiagnosisForIcon();
							
							
							/*************Patient General Examination list**************/
							
							
							var commonObj = JSON.parse(sessionStorage.getItem('cookieEncounterObject'));
							if (commonObj != undefined) {
								console.log("old encounter==="+commonObj.patientId);
								var paramObjForPer = {
									
										'patientId':commonObj.patientId
								};

								 var headerObj = {
							    		 unit_id : $scope.unitId,
											organization_id : $scope.organizationId
							     };
							
								 var data = {};
							 
								  var paramObjForGen = {
										
											'patientId':commonObj.patientId,
											'typeId':1,
											// 'genderId':commonObj.genderId
									};
						        
						        
							        $scope.getActiveGeneralExamination=function(){
									var URI = BASE_URL + ROOT_URL
							            + GETGENERALEXAMLIST;
							        GenericServiceParamHeader
							            .serviceAction(METHOD_GET, URI,
							                    data,
							                    NOTIFICATION_MSG_STATUS_FALSE,paramObjForGen,headerObj)
							            .then(
							                    function(response) {
							                        console.log(response);
							                        // $rootScope.startLoader();
							                        if (response.data.status == "success")
							                            $scope.getSavedGeneralExamList = response.data.listObject;
							                        angular.forEach($scope.getSavedGeneralExamList, function(value,key) {
							                        	$scope.descriptiveExamination=value.descriptiveExamination;
							                        });
							                         console.log("General Exam List", $scope.getSavedGeneralExamList);
							                    });
							        }
								  
							        var paramObjForGen1 = {
										
											'patientId':commonObj.patientId,
											'typeId':2,
											// 'genderId':commonObj.genderId
									};
							        $scope.getActivephysicalExamination=function(){
									var URI = BASE_URL + ROOT_URL
							            + GETGENERALEXAMLIST;
							        GenericServiceParamHeader
							            .serviceAction(METHOD_GET, URI,
							                    data,
							                    NOTIFICATION_MSG_STATUS_FALSE,paramObjForGen1,headerObj)
							            .then(
							                    function(response) {
							                        console.log(response);
							                        // $rootScope.startLoader();
							                        if (response.data.status == "success")
							                            $scope.getSavedPhysicalExamList = response.data.listObject;
							                        angular.forEach($scope.getSavedPhysicalExamList, function(value,key) {
							                        	$scope.sysDescriptiveExamination=value.descriptiveExamination;
							                        });
							                        console.log("physical Exam List", $scope.getSavedPhysicalExamList);
							                    });
							        }
							}

						 
							/****************tab wise code end*****************/
						        

								  /*******General Examn for Casesheet *******/
						        $scope.getGenericExaminationForCaseSheeet=function(){
						        	 var data = {
						        			 organizationId:$scope.organizationId,
						        			 patientId:$scope.encounterDetailsObj.patientId,
						        			 typeId:1
						        	 };
									var URI = BASE_URL + ROOT_URL
									+ "/api/opd/getListOfSystemsAndObservationForCaseSheet";
							        GenericServiceParamHeader
							            .serviceAction(METHOD_POST, URI,
							                    data,
							                    NOTIFICATION_MSG_STATUS_FALSE)
							            .then(
							                    function(response) {
							                        console.log(response);
							                        if (response.data.status == "success")
														$scope.getSavedListOfGeneralExam = response.data.listObject;
													angular.forEach($scope.getSavedListOfGeneralExam, function(value,key) {
							                        	$scope.descriptiveExamination=value.descriptiveExamination;
							                        });
							                        console.log("getSavedListOfGeneralExam", $scope.getSavedListOfGeneralExam);
							                    });
							        }
						        //$scope.getGenericExaminationForCaseSheeet();
								
								/*******Systemic Examn for Casesheet *********/
						        $scope.getSystemicExaminationForCaseSheeet=function(){
						        	 var data = {
						        			 organizationId:$scope.organizationId,
						        			 patientId:$scope.encounterDetailsObj.patientId,
						        			 typeId:2
						        	 };
									var URI = BASE_URL + ROOT_URL
									+ "/api/opd/getListOfSystemsAndObservationForCaseSheet";
							        GenericServiceParamHeader
							            .serviceAction(METHOD_POST, URI,
							                    data,
							                    NOTIFICATION_MSG_STATUS_FALSE)
							            .then(
							                    function(response) {
							                        console.log(response);
							                        if (response.data.status == "success")
							                            $scope.getSavedListOfSystemicExam = response.data.listObject;
							                        angular.forEach($scope.getSavedListOfSystemicExam, function(value,key) {
							                        	$scope.sysDescriptiveExamination=value.descriptiveExamination;
							                        });
							                        console.log("getSavedListOfSystemicExam", $scope.getSavedListOfSystemicExam);
							                    });
							        }
						      //  $scope.getSystemicExaminationForCaseSheeet();
						      
						 /*****************************************************/ 
							
							// for added diagnosis
								$scope.selectedOrderSet= [];
								
								// for added complaints
								 $scope.add = function(index) 
								 {
									 console.log("index add",index);
										var data = {
												diagnosisId : '',
												since :'',
												duration:'Day',
												remark:'',
												isReviewedFlag : '',
												complaintAppoId : '',
												icpcSystemId:'',
												icpcCodeText:'',
												diagnosisName:'',
				                                nos:'N',
									};
										var len = $scope.dataListComplaints.length;
										
										angular.forEach($scope.dataListComplaints,function(value,index1){
											if(index == index1){
												if(value.diagnosisId == "")
												{
													growl.warning("Please Enter Complaint.",
					    	    							{
					    	    							title : 'Warning!'
					    	    							});
													return false;
												}
												else{
													$scope.dataListComplaints.push(data);
												}
											}
										});
										$scope.icpcSystemList = $scope.icpcSystemListTemporary;
										//for Complaint Template
										 var tempObj={};
			                        	 tempObj.templateObject={};
			                        	 tempObj.diagonisIdforTemplate=0;
			                        	 tempObj.icpcSystemId=0;
			                        	 tempObj.icpcId=0;
			                        	 $scope.templateListForSave.push(tempObj);
									};
									
									// for remove complaints
									$scope.remove = function(index) {
										console.log("removeIndex",index);
										var newDataList = [];
										var le = $scope.dataListComplaints.length;
										var i = 1;
										if(le != i){
										angular.forEach($scope.dataListComplaints, function(v) {
											if (index != i) {
												newDataList.push(v);
											}
											i++;
										});
										$scope.dataListComplaints = newDataList;
										}
										
										//for remove complaint template
										var newTempList = [];
										var length = $scope.templateListForSave.length;
										var j = 1;
										if (length != j) {
											angular.forEach($scope.templateListForSave,
													function(v) {
														if (index != j) {
															newTempList.push(v);
														}
														j++;
													});

											$scope.templateListForSave = newTempList;
										}
										
									};
										
														
							// Search Vital By Dates
							$scope.msg="No records found...!!";
							// Selected TypeHead on Complaints
							
							$scope.claerICPCsystem = function(ind){
								$scope.temSystemId="";
								var tempICPC = $scope.dataListComplaints[ind].icpcSystemId
								if($scope.temSystemId !=""){
								$scope.temSystemId = tempICPC;
								}else if(tempICPC != $scope.temSystemId){
									$scope.dataListComplaints[ind].diagnosisId ="";
									$scope.dataListComplaints[ind].diagnosisName ="";
								}
							}
							
							
							$scope.selectPatientId = function(info, model, label,ind) {
								if(info == undefined || (info.icpcCodeText = undefined || info.icpcCodeText != null || info.icpcCodeText == "")){
									info=undefined;
									model=undefined;
									label=undefined;
									ind=undefined;
								}
								console.log(info);
								if(info != undefined || ($scope.diagnosisName != undefined && $scope.diagnosisName != null && $scope.diagnosisName != "")){
								var ind = 0;
								/** commented by chetana for save free text complaints*/
//								$scope.dataListComplaints[ind].diagnosisId= info.icpcId;
//								$scope.dataListComplaints[ind].diagnosisName= info.diagnosisName;
//								$scope.icpcId = info.icpcId;
								
								if(info == undefined && ($scope.diagnosisName != undefined || $scope.diagnosisName != null || $scope.diagnosisName != "")){
									$scope.dataListComplaints[ind].diagnosisId= 51;
									$scope.dataListComplaints[ind].icpcCodeText = $scope.diagnosisName;
									$scope.dataListComplaints[ind].icpcSystemId = 1;
									//$scope.icpcId = info.icpcId;
								}else if(info != undefined){
//									$scope.dataListComplaints[ind].diagnosisId= info.icpcId;
//									$scope.dataListComplaints[ind].diagnosisName= info.diagnosisName;
									$scope.dataListComplaints[ind].diagnosisId= info.icpcId;
									$scope.dataListComplaints[ind].diagnosisName= info.icpcDesc;
									$scope.dataListComplaints[ind].icpcSystemId = info.icpcSystemId;
									$scope.icpcId = info.icpcId;
								}else{
									growl.warning("Please enter complaint.",
			    							{
			    							title : 'Warning!'
			    							});
			    							$rootScope.stopLoader();
			    							return false;
								}
								
								if($scope.dataListComplaints[ind].icpcSystemId == null || $scope.dataListComplaints[ind].icpcSystemId == ""){
									$scope.searchKeywordCodeIndex = ind;
//									$scope.searchICPCByCode();
						        	$scope.icpcSystemList1=[];
						        	var icpcId = $scope.icpcId;
									var data={
						        			icpcId:icpcId,
						        	};
									var URI = BASE_URL + ROOT_URL + "/api/opd/getMasterListOfICPCSystemForCoversheetForCode" 
									return GenericService.serviceAction("POST", URI, data)
											.then(
													function(response) {
														if (response.data.status == "success") {
															$scope.icpcSystemList1 = response.data.listObject;

															if ($scope.icpcSystemList1.length == 0) {
																$scope.icpcId = 0;
																growl.warning(
																				"No system mapped with selected ICPC code",
																				{
																					title : 'Warning!'
																				});
																$rootScope.stopLoader();
																return false;
															} else {
																angular.forEach($scope.icpcSystemList1,function(value,key) {
																					$scope.flag = false;
																					angular.forEach($scope.icpcSystemListTemporary,function(value1,key) {
																										if (value.icpcSystemId == value1.icpcSystemId) {
																											$scope.flag = true;
																										}
																									});
																				});
																if ($scope.flag) {
																	if ($scope.icpcSystemList1.length > 0) {
																		$scope.dataListComplaints[ind].icpcSystemId = $scope.icpcSystemList1[0].icpcSystemId;
																	}
																	$scope.temComplaintList = [];
																	$scope.temComplaintList1 = [];
																	angular.forEach($scope.dataListComplaints,function(value,index1) {
																						if (index1 == ind) {
																							var newArr = {};
																							newArr.diagnosisId = value.diagnosisId;
																							newArr.icpcSystemId = value.icpcSystemId;
																							$scope.temComplaintList.push(newArr);
																						}
																					});
																	angular.forEach($scope.dataListComplaints,function(value,index2) {
																						if (index2 != ind) {
																							var newArr = {};
																							newArr.diagnosisId = value.diagnosisId;
																							newArr.icpcSystemId = value.icpcSystemId;
																							$scope.temComplaintList1.push(newArr);
																						}
																					});

																	angular.forEach($scope.temComplaintList,
																					function(value,key) {
																						if ($scope.dataListComplaints != undefined && $scope.dataListComplaints.length > 0) {
																							var chkStatus = $scope.temComplaintList1.filter(function(item) {
																										return (item.diagnosisId == value.diagnosisId && (value.nos == 'N' || value.nos == undefined || value.nos == ""));
																									})
																							if (chkStatus.length > 0) {
																								growl.warning("Complaint Already added",
																												{
																													title : 'warning!'
																												});
																								$scope.dataListComplaints[ind].diagnosisId = "";
																								$scope.dataListComplaints[ind].diagnosisName = "";
																								$rootScope.stopLoader();
																								return false;
																							}
																						}
																					});

																	if ($scope.dataListComplaints[ind].icpcSystemId != null && $scope.dataListComplaints[ind].icpcSystemId != "") {
																		$scope.saveComplaints();
																	}
																} else {
																	$scope.icpcSystemList = $scope.icpcSystemListTemporary;
																	growl.warning("system is not mapped with speciality of selected ICPC code",
																					{
																						title : 'Warning!'
																					});
																	$rootScope.stopLoader();
																	$scope.dataListComplaints[$scope.searchKeywordCodeIndex].diagnosisName = "";
																	$scope.dataListComplaints[$scope.searchKeywordCodeIndex].diagnosisId = "";
																	return false;
																}
															}
														}
													});
						    	
								}else{
									/**commented by chetana for save free text complaints*/
//									$scope.icpcSystemId = info.icpcSystemId;
//									$scope.dataListComplaints[ind].icpcSystemId= info.icpcSystemId;
									$scope.icpcSystemId = info != undefined ? info.icpcSystemId :$scope.dataListComplaints[ind].icpcSystemId;
									$scope.dataListComplaints[ind].icpcSystemId= info != undefined ? info.icpcSystemId :$scope.dataListComplaints[ind].icpcSystemId;
								
									//check duplicate recorded while adding 
									$scope.temComplaintList = [];
									$scope.temComplaintList1 = [];
									angular.forEach($scope.dataListComplaints, function(value , index1) {
										if(index1 == ind){
											var newArr ={};
											newArr.diagnosisId=value.diagnosisId;
											newArr.icpcSystemId=value.icpcSystemId;
											$scope.temComplaintList.push(newArr);
										}
									});
									angular.forEach($scope.dataListComplaints, function(value , index2) {
										if(index2 != ind){
											var newArr ={};
											newArr.diagnosisId=value.diagnosisId;
											newArr.icpcSystemId=value.icpcSystemId;
											$scope.temComplaintList1.push(newArr);
										}
									});
									
									angular.forEach( $scope.temComplaintList, function(value, key) {
								 	if($scope.dataListComplaints!=undefined && $scope.dataListComplaints.length>0){
										var chkStatus= $scope.temComplaintList1.filter(function(item){ return (item.diagnosisId==value.diagnosisId && (value.nos=='N' || value.nos==undefined ||  value.nos == "" ) ); })
										if(chkStatus.length>0){
											growl.warning("Complaint Already added",
			    	    							{
			    	    							title : 'warning!'
			    	    							});
											$scope.dataListComplaints[ind].diagnosisId ="";
											$scope.dataListComplaints[ind].diagnosisName ="";
			    	    							$rootScope.stopLoader();
			    	    							return false;
										}
									}
									});
									if($scope.dataListComplaints[ind].icpcSystemId != null && $scope.dataListComplaints[ind].icpcSystemId != ""){
										$scope.saveComplaints();	
									}
								}
								/**/
							}else{
								growl.warning("Please enter complaint.");
								return false;
							}
							
							}
							
							
							$scope.checkDuplicateComplaintNos = function(ind){
								
							
								$scope.temComplaintNosList = [];
								$scope.temComplaintNosList1 = [];
								angular.forEach($scope.dataListComplaints, function(value , index1) {
									if(index1 == ind){
										var newArr ={};
										newArr.icpcCodeText=value.icpcCodeText;
										$scope.temComplaintNosList.push(newArr);
									}
								});
								angular.forEach($scope.dataListComplaints, function(value , index2) {
									if(index2 != ind){
										var newArr ={};
										newArr.icpcCodeText=value.icpcCodeText;
										$scope.temComplaintNosList1.push(newArr);
									}
								});
								
								angular.forEach( $scope.temComplaintNosList, function(value, key) {
							 	if($scope.dataListComplaints!=undefined && $scope.dataListComplaints.length>0){
									var chkStatus= $scope.temComplaintNosList1.filter(function(item){ return (item.icpcCodeText == value.icpcCodeText && item.nos == value.nos);})
									if(chkStatus.length>0){
										growl.warning("Complaint Already added",
		    	    							{
		    	    							title : 'warning!'
		    	    							});
										$scope.dataListComplaints[ind].icpcCodeText ="";
		    	    							$rootScope.stopLoader();
		    	    							return false;
									}
								}
								});
							}
							$scope.selectPatientIdUpdate = function(info, model,
									label,ind) {
								console.log(info);
								$scope.complaintsUpdateObj.diagnosisId= info.icpcId;
								
								if($scope.complaintsUpdateObj.icpcSystemId == null || $scope.complaintsUpdateObj.icpcSystemId == ""){
									$scope.searchICPCByCodeUpdate()
								}
							}
							
							
							/***Patient Allergy list***/
                                
								$scope.getActiveAllergy = function(){
									 $scope.dt = moment(new Date()).format('DD-MM-YYYY');
										var headerObj = {
												unit_id : $scope.unitId,
												organization_id : $scope.organizationId
											};

											var paramObjForHistList = {
												patientId : $scope.encounterDetailsObj.patientId,
												created_date:$scope.dt
											};
									var data = {};
								var URI = BASE_URL + ROOT_URL + "/api/opd/getCurrentAllergyPatientMapperForCoversheet";
							GenericServiceParamHeader.serviceAction(METHOD_GET, URI,data,
										NOTIFICATION_MSG_STATUS_FALSE,paramObjForHistList,headerObj)
								.then(
										function(response) {
											console.log(response);
											if (response.data.status == "success")
											$scope.allergyCurrentList = response.data.listObject;
											$scope.allergyCurrentListDoctor = [];
											if($scope.roleId == '2' )
												{
													angular.forEach(response.data.listObject, function(value,key) {
														
															if(value.roleId == 3 && value.isReviewedFlag == 'N')
																{
															var data = {};
															data.isNoKnownAllergies = value.isNoKnownAllergies;
															data.remark = value.remark;
															data.isReviewedFlag = 'N';
															data.isEnterInErrorStatus='N';
															data.allergyId = value.allergyId;
															data.allergyTypeId= value.allergyTypeId;
															data.type = value.type;
															data.natureOfReaction = value.natureOfReaction;
															data.severityId  = value.severityId;
															data.month = value.month;
															data.year = value.year;
															data.allergyPatientMapperId = value.allergyPatientMapperId;
															$scope.allergyCurrentListDoctor.push(data);
													}
													});
												}
											if($scope.allergyCurrentList.length > 0)
											{
												$scope.allergySymbolList=[];
											   angular.forEach($scope.allergyCurrentList,function(value,index){
												   if(value.isNoKnownAllergies !='Y'){
													   $scope.allergySymbolList.push(value);
												   }
												   if($scope.allergySymbolList.length > 0){
													   $scope.allergyFlag = true;
												   }
												  
											   });
												
											}
											
										});
							
							var data = {};
							
							$scope.dt = moment(new Date()).format('DD-MM-YYYY');
							var headerObj = {
									unit_id : $scope.unitId,
									organization_id : $scope.organizationId
								};

								var paramObjForHistList = {
									patientId : $scope.encounterDetailsObj.patientId,
									created_date:$scope.dt
								};
						
							
							var URI = BASE_URL + ROOT_URL
							+ GETOLDALLERGYPATIENTMAPPER;
						GenericServiceParamHeader.serviceAction(METHOD_GET, URI,data,
									NOTIFICATION_MSG_STATUS_FALSE,paramObjForHistList,headerObj)
							.then(
									function(response) {
										console.log(response);
										
										if (response.data.status == "success")
										$scope.allergyOldList = response.data.listObject;
										
										if($scope.allergyOldList.length > 0)
										{
											$scope.allergySymbolList=[];
										   angular.forEach($scope.allergyOldList,function(value,index){
											   if(value.isNoKnownAllergies !='Y'){
												   $scope.allergySymbolList.push(value);
											   }
											   if($scope.allergySymbolList.length > 0){
												   $scope.allergyFlag = true;
											   }
											  
										   });
											
										}
									});
								}
								
								/***Patient Allergy list for icon***/
								//Chetana
								$scope.getActiveAllergyForIcon = function(){
									 $scope.dt = moment(new Date()).format('DD-MM-YYYY');
										var headerObj = {
												unit_id : $scope.unitId,
												organization_id : $scope.organizationId
											};

											var paramObjForHistList = {
												patientId : $scope.encounterDetailsObj.patientId,
											};
									var data = {};
								var URI = BASE_URL + ROOT_URL + "/api/opd/getPatientAllergyListForIcon";
							GenericServiceParamHeader.serviceAction(METHOD_GET, URI,data,
										NOTIFICATION_MSG_STATUS_FALSE,paramObjForHistList,headerObj)
								.then(
										function(response) {
											console.log(response);
											if (response.data.status == "success")
											$scope.activeAllergyListForIcon = response.data.listObject;
											console.log("list for allergy icon",$scope.activeAllergyListForIcon)
											if($scope.activeAllergyListForIcon.length > 0)
											{
												$scope.allergySymbolList=[];
											   angular.forEach($scope.activeAllergyListForIcon,function(value,index){
												   if(value.isNoKnownAllergies !='Y'){
													   $scope.allergySymbolList.push(value);
												   }
												   if($scope.allergySymbolList.length > 0){
													   $scope.allergyFlag = true;
												   }
											   });
												
											}
											
										});
								}
								$scope.getActiveAllergyForIcon();
							
							
		/************Complete consultation**************/
								
								
			$scope.updateEncounterDetailsMultilingual=function(info){
				var headerObjS={
						'organization_id': $scope.organizationId,
						'unit_id':$scope.unitId,
						'Content-Type':'application/json'
				};
				
				var URI = BASE_URL + ROOT_URL
				+ "/api/opd/updateEncounterDetailsMultilingual";
				GenericServiceParamHeader.serviceAction(METHOD_POST, URI, info,NOTIFICATION_MSG_STATUS_TRUE,paramObjForPer,headerObjS)
					.then( function(response) {
								if (response.data.status == "success")
								{
									$rootScope.stopLoader();
								}
							});
			}
								
		$scope.completeConsultation=function(roleId){
			
			var cookieWObject = JSON.parse(sessionStorage.getItem('cookieEncounterObject'));
			if (cookieWObject == undefined) {
				return;
			}
			var headerObj={
					'organization_id': $scope.organizationId,
					'unit_id':$scope.unitId,
					'Content-Type':'application/json'
			};
			
		
			$rootScope.startLoader();
			if(roleId == 3)
			{
				var data = {
						'assignmentStopTime':moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
						'isAssignmentCompStatus':'Y',
						'assignmentStopBy': $scope.userId,
						'assignmentStopRoleId': roleId,
						'encounterId': cookieWObject.encounterId,
						"patientId": cookieWObject.patientId,
						"appointmentId":cookieWObject.appointmentId,
						"updatedBy":$scope.userId,
						"childEncounterId":cookieWObject.childEncounterId
					}
			}
			else
				{
				var data = {
						'isConsultationStatus': "Y",
						'consultationStopTime': moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
						'consultationStopBy': $scope.userId,
						'consultationStopRoleId': roleId,
						'encounterId': cookieWObject.childEncounterId,
						"patientId": cookieWObject.patientId,
						"appointmentId":cookieWObject.appointmentId,
						"updatedBy":$scope.userId,
						"isFromCoverSheet":'Y',
						"childEncounterId":cookieWObject.childEncounterId
					}
				
				}
			
			
						console.log('SaveCoversheet',data)
						var datatvgsvx  = data;
						
						var paramObjForPer={};
						var URI = BASE_URL + ROOT_URL
						+ UPDATEENCOUNTERASSIGNMENT;
						GenericServiceParamHeader
							.serviceAction(METHOD_POST, URI,
									data,
									NOTIFICATION_MSG_STATUS_TRUE,paramObjForPer,headerObj)
							.then(
									function(response) {
										console.log(response);
										if (response.data.status == "success")
										{	
											
											$rootScope.stopLoaderPopup();
											$('#Complete_CaseSheet').modal('hide');
											$scope.printCaseSheet();
											$scope.updateEncounterDetailsMultilingual(datatvgsvx);
											sessionStorage.removeItem('cookieEncounterObject');
											if(roleId == 2)
											{
												$state.go("doctorDesk");
												$rootScope.stopLoaderPopup();
												}
												else{
													$state.go("encounterQueueManagement");
													$rootScope.stopLoaderPopup();
												}
											
										}
									});
			
			
		}

        //Start for ICPC Master By icpc system Id
        $scope.searchICPCBySystemId = function(searchKeyword){
        	$scope.icpcMasterList=[];
//        	$scope.searchKeywordCodeIndex = index;
        	var icpcSystemId = $scope.dataListComplaints[0].icpcSystemId;
        	var icpcId = $scope.dataListComplaints[0].diagnosisId;
			var data={
        			icpcSyatemId:icpcSystemId,
        			searchICPC:searchKeyword,
        	};
			var URI = BASE_URL + ROOT_URL + "/api/opd/getMasterListOfICPCSystemForCoversheet" 
			
    		return GenericService.serviceAction("POST",URI,data).then(function(response){
    			if(response.data.status == "success"){
    				$scope.icpcMasterList = response.data.listObject;
//    				if($scope.icpcMasterList.length == 0){
    					/*$scope.icpcId = 0;
    					growl.warning("No complaint found with the selected ICPC system",
    							{
    							title : 'Warning!'
    							});
    							$rootScope.stopLoader();
    							return false;*/
    				$scope.icpcMasterListText=[];
    				$scope.icpcMasterTextUniqueList = [];
    					var data={
    		        			organizationId:$scope.organizationId,
    		        			searchIcpcCodeText:searchKeyword,
    		        	};
    					var URI = BASE_URL + ROOT_URL + "/api/opd/searchFreeTextComplaint";
    					
    		    		return GenericService.serviceAction("POST",URI,data).then(function(response){
    		    			if(response.data.status == "success"){
    		    				$scope.icpcMasterListText = response.data.listObject;
    		    			}

    		    			$scope.icpcMasterListText.forEach(function(val) {

    		    	          var unique = true;

    		    	          $scope.icpcMasterTextUniqueList.forEach(function(val2) {

    		    	            if (_.isEqual(val.icpcDesc, val2.icpcDesc)) unique = false;

    		    	          });

    		    	          if (unique)  $scope.icpcMasterTextUniqueList.push(val);

    		    	        });

    		    	        $scope.list = $scope.icpcMasterTextUniqueList;
    		    			return $scope.icpcMasterList.concat($scope.icpcMasterListText);

    		    		});
//    				}
    			}
    		});
    	}
        
        $scope.searchICPCByCode = function(){
        	$scope.icpcSystemList1=[];
        	var icpcId = $scope.icpcId;
			var data={
        			icpcId:icpcId,
        	};
			var URI = BASE_URL + ROOT_URL + "/api/opd/getMasterListOfICPCSystemForCoversheetForCode" 
			
    		return GenericService.serviceAction("POST",URI,data).then(function(response){
    			if(response.data.status == "success"){
    				$scope.icpcSystemList1 = response.data.listObject;
    				
    				if($scope.icpcSystemList1.length == 0){
    					$scope.icpcId = 0;
    					growl.warning("No system mapped with selected ICPC code",
    							{
    							title : 'Warning!'
    							});
    							$rootScope.stopLoader();
    							return false;
    				}else{
    			
    			angular.forEach($scope.icpcSystemList1,function(value,key){
    				$scope.flag =false;
    				angular.forEach($scope.icpcSystemListTemporary,function(value1,key){
    					if(value.icpcSystemId == value1.icpcSystemId){
    						$scope.flag =true;
    					}
    				});
    			});
    			if($scope.flag){
    				
    				if($scope.icpcSystemList1.length > 0){
   					 $scope.dataListComplaints[$scope.searchKeywordCodeIndex].icpcSystemId = $scope.icpcSystemList1[0].icpcSystemId;
   				    }
    				
    			}else{
    				$scope.icpcSystemList = $scope.icpcSystemListTemporary;
    				  growl.warning("system is not mapped with speciality of selected ICPC code",
							{
							title : 'Warning!'
							});
							$rootScope.stopLoader();
							$scope.dataListComplaints[$scope.searchKeywordCodeIndex].diagnosisName="";
		    	        	$scope.dataListComplaints[$scope.searchKeywordCodeIndex].diagnosisId="";
							return false;
    			}
    			
    			
    				}
    				
    				
    				
    			}
    		});
    	}
        
        //Start for ICPC Update By icpc system Id
        $scope.searchICPCBySystemIdUpdate= function(searchKeyword){
        	$scope.icpcMasterList=[];
        	$scope.complaintsUpdateObj.icpcSystemId = $scope.complaintsUpdateObj.icpcSystemId.toString();
        	var data={
        			icpcSyatemId:$scope.complaintsUpdateObj.icpcSystemId,
        			searchICPC:searchKeyword
        	};
			var URI = BASE_URL + ROOT_URL + "/api/opd/getMasterListOfICPCSystemForCoversheet" 
			
    		return GenericService.serviceAction("POST",URI,data).then(function(response){
    			if(response.data.status == "success"){
    				$scope.icpcMasterList = response.data.listObject;
    				if($scope.icpcMasterList.length == 0){
    					$scope.icpcId = 0;
    					growl.warning("No complaint found with the selected ICPC system",
    							{
    							title : 'Warning!'
    							});
    							$rootScope.stopLoader();
    							return false;
    				}
    			}
    			return $scope.icpcMasterList;
    		});
    	}
        //END ICPC
        $scope.complaintFlag=false;
        $scope.MedComCount=0;
        $scope.checkComplaintList = function(){
        	
        	if( $scope.MedComCount == 0){
        	 if($scope.complaintsPatientList.length > 0){
        		 
        		 $scope.complaintFlag=true;
        		 
        	 }else{
        		 $scope.MedComCount++;
        		 growl.warning("Please enter the complaint to proceed with the medication ordering.",
							{
								title : 'warning!'
							});
        		 $scope.complaintFlag=false;
        	 }
        	}
        }
        

        $scope.diaFlag=false;
        $scope.diaFlagCount=0;
        $scope.checkComplaintDiaList = function(){
        	
        	if( $scope.diaFlagCount == 0){
        	 if($scope.complaintsPatientList.length > 0){
        		 $scope.diaFlag=true;
        		
        	 }else{
        		 $scope.diaFlagCount++;
        		 if($scope.roleName !="nurse"){
        		 growl.warning("Please enter the complaint to proceed with the diagnosis recording.",
							{
								title : 'warning!'
							});
        		 $scope.diaFlag=false;
        		 }
        	 }
        	}
        }
					        /**=================Template Consumptionb on examination=================*/
					        $scope.templateTransaction = {
					        			  templateTransactionId : "",
					        			  templateId : "",
					        			  title : "",
					        			  specialityId : "",
					        			  subSpecialityId : "",
					        			  diagnosisId:"",
					        			  templateObject : "",
					        			  admissionId : "",
					        			  encounterId : "",
					        			  doctorId : "",
					        			  patientId : "",
					        			  status : 'A',
					        			  signedBy : $scope.userId,
					        			  signedDate : "",
					        			  jsonObject : "",
					        			  orgId : $scope.organizationId,
					        			  unitId : $scope.unitId,
					        			  createdBy : $scope.userId,
					        			  createdDate : "",
					        			  updatedBy : $scope.userId,
					        			  updatedDate : "",
					        			  diagnosisId :"",
										  mIcpcSystemId:"",
										  mIcpcId:"",
					        }
					        
					        $scope.saveTrasactionFlag = false;
					        $scope.updateTrasactionFlag = false;
					        $scope.templateObj=false;
					        $scope.template={};
					        $scope.templateTransactionList = [];
							$scope.templateList=[];
							$scope.templateTransactionList =[];
						 	$scope.diagonisIdforTemplate = 0;
						 	$scope.icpcSystemId = 0;
							$scope.icpcId = 0;
					    	$scope.getOpdCoverSheetTempltes = function(templateTypeId) 
					    	{
					    		
					    		$rootScope.startLoader();
					        	var data={
					        		  orgId : $scope.organizationId,
					        		  unitId : $scope.unitId,
					        		  doctorId : $scope.encounterDetailsObj.doctorId,
					        		  specialityId :$scope.encounterDetailsObj.specialityId==undefined?0:$scope.encounterDetailsObj.specialityId,
					        		  templateTypeId : templateTypeId
					        		}
					        	
					        	console.log('data',JSON.stringify(data));
					    		var URI = BASE_URL + ROOT_URL + "/api"+GLOBAL+TEMPLATE_LIST_BY_SPECILATIY;
					    		BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
					    				{
					    	
					    			   if(response.data.status=='success')
					    			      {
					    				   	$scope.templateList = response.data.listObject;
					    				   	$('#addtemplateModal').modal('show');
					    				   	$rootScope.stopLoader();
					    			      }
					    			   else if(response.data.status=='error'){
					    				   $rootScope.stopLoader();
					    			   }
					    		});
					    	}
					    	$scope.selectTemplate  = function(templateObj)
					    	{
					    		$scope.template = templateObj;
					    	}
					    	
					    	$scope.addTemplate = function()
					    	{
							   try{
								   $('#addtemplateModal').modal('hide'); 
								   $rootScope.startLoader();
									  var templateTransaction = angular
										.copy($scope.templateTransaction);
								templateTransaction.templateId = $scope.template.templateId;
								templateTransaction.title = $scope.template.title;
								templateTransaction.specialityId = $scope.template.specialityId;
								templateTransaction.templateTransactionId=$scope.template.templateTransactionId==undefined ? 0 :$scope.template.templateTransactionId;
								templateTransaction.subSpecialityId = $scope.template.subSpecialityId;
								templateTransaction.templateTypeId = $scope.template.templateTypeId;
								templateTransaction.encounterId=$scope.encounterDetailsObj.encounterId;
								templateTransaction.doctorId = $scope.encounterDetailsObj.doctorId;
								templateTransaction.patientId = $scope.encounterDetailsObj.patientId;
								templateTransaction.diagnosisId = 0;
								templateTransaction.mIcpcSystemId = 0;
								templateTransaction.mIcpcId = 0;
                                templateTransaction.patientName = $scope.encounterDetailsObj.patientName !== undefined && $scope.encounterDetailsObj.patientName !== null ? $scope.encounterDetailsObj.patientName : "";
                                templateTransaction.doctorName = $scope.encounterDetailsObj.doctorName !== undefined && $scope.encounterDetailsObj.doctorName !== null ? $scope.encounterDetailsObj.doctorName : "";
                                templateTransaction.uhidNumber = $scope.encounterDetailsObj.uhidNumber !== undefined && $scope.encounterDetailsObj.uhidNumber !== null ? $scope.encounterDetailsObj.uhidNumber : $scope.encounterDetailsObj.uhidnumber;
                                templateTransaction.dob = $scope.encounterDetailsObj.dob !== undefined && $scope.encounterDetailsObj.dob !== null ? $scope.encounterDetailsObj.dob : "";
                                templateTransaction.ageString = $scope.encounterDetailsObj.age !== undefined && $scope.encounterDetailsObj.age !== null ? $scope.encounterDetailsObj.age : $scope.encounterDetailsObj.ageYear;
                                templateTransaction.doa = $scope.encounterDetailsObj.datepicker !== undefined && $scope.encounterDetailsObj.datepicker !== null ? $scope.encounterDetailsObj.datepicker : "";
                                templateTransaction.pdd = $scope.encounterDetailsObj.datepickerPdd !== undefined && $scope.encounterDetailsObj.datepickerPdd !== null ? $scope.encounterDetailsObj.datepickerPdd : "";
                                templateTransaction.admissionNumber = $scope.encounterDetailsObj.admissionNumber !== undefined && $scope.encounterDetailsObj.admissionNumber !== null ? $scope.encounterDetailsObj.admissionNumber : "";
                                templateTransaction.gender = $scope.encounterDetailsObj.genderCode !== undefined && $scope.encounterDetailsObj.genderCode !== null ? $scope.encounterDetailsObj.genderCode : "";

										  var URI = BASE_URL + ROOT_URL + NEW_TEMPLATE_SAVE;
											BillingGenericService.serviceAction(METHOD_POST, URI,templateTransaction,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(function(response) {
														if (response.data.status == "success"){
															window.open(response.data.object+"&formType=digital",'_blank');	
															 $rootScope.stopLoader();
														}
														else if(response.data.status == "error")
															{
															  $('#addtemplateModal').modal('hide');
															  $rootScope.stopLoader();
															}
														
													});		
									}catch(e){
													console.log(e.message);
												}
					    	 
							}
					    	
					    	
					    	$scope.viewTemplate = function(template)
					    	{
					    		$scope.saveTrasactionFlag = false;
								$scope.updateTrasactionFlag = false;
					    		$scope.obj = {};
					    		document.getElementById("htmlContent").innerHTML = JSON.parse(template.templateObject).html;
					    		
					    		$('#viewtemplateModal').modal('hide');
					    		$('#addviewTemplateModal').modal('show');
					    		$scope.obj = JSON.parse(template.templateObject).obj;
					    		var element = angular.element(document.getElementById("htmlContent"));
								   $compile(element)($scope);
								
					    	}
					    	
					    	function isEmpty(obj) {
					    	    for(var key in obj) {
					    	        if(obj.hasOwnProperty(key))
					    	            return true;
					    	    }
					    	    return false;
					    	}
					    	
					        $scope.saveTemplateTransaction = function()
					        {
								$rootScope.startLoader();

								var templateTransaction = angular
										.copy($scope.templateTransaction);
								templateTransaction.templateId = $scope.template.templateId;
								templateTransaction.title = $scope.template.title;
								templateTransaction.specialityId = $scope.template.specialityId;
								templateTransaction.templateTransactionId=$scope.template.templateTransactionId==undefined ? 0 :$scope.template.templateTransactionId;
								templateTransaction.subSpecialityId = $scope.template.subSpecialityId;
								var dataObj=document.getElementById("htmlContent").innerHTML;
								templateTransaction.templateObject = $scope.template.templateObject;
								templateTransaction.encounterId=$scope.encounterDetailsObj.encounterId;
								templateTransaction.doctorId = $scope.encounterDetailsObj.doctorId;
								templateTransaction.patientId = $scope.encounterDetailsObj.patientId;
								templateTransaction.diagnosisId = 0;
								templateTransaction.mIcpcSystemId = 0;
								templateTransaction.mIcpcId = 0;
						
								if(isEmpty($scope.template.templateObject.obj)) {
									var jsonObject = angular.equals({},
											$scope.template.templateObject.obj) ? JSON
											.parse($scope.template.templateObject.obj)
											: {};
								}
								var templateObj = $scope.template.templateObject;
								templateObj.html=dataObj;
								templateObj.obj = $scope.obj;
								templateTransaction.jsonObject = JSON
										.stringify(jsonObject);
								templateTransaction.templateObject = JSON
										.stringify(templateObj);
								console.log('templateTransaction', JSON
										.stringify(templateTransaction));
								var URI = BASE_URL + ROOT_URL
										+ "/global/template/transaction";
								GenericService
										.serviceAction("POST", URI,
												templateTransaction)
										.then(
												function(response) {
													if (response.data.status == "success") {
														if($scope.template.templateTypeId==4){
															growl.success("Complaint assessment done successfully.",{
											    				title : SUCCESS_MSG
											    			 });
														}else if($scope.template.templateTypeId==17){
															growl.success("Diagnosis assessment done successfully.",{
											    				title : SUCCESS_MSG
											    			 });
														}else if($scope.template.templateTypeId==19){
															growl.success("Personal History recorded successfully.",{
											    				title : SUCCESS_MSG
											    			 });
														}else if($scope.template.templateTypeId==5){
															growl.success("General Examination done successfully.",{
											    				title : SUCCESS_MSG
											    			 });
														}else if($scope.template.templateTypeId==6){
															growl.success("Systemic Examination done successfully.",{
											    				title : SUCCESS_MSG
											    			 });
														}else if($scope.template.templateTypeId==2){
															growl.success("Letter documented successfully.",{
											    				title : SUCCESS_MSG
											    			 });
														}else if($scope.template.templateTypeId==3){
															growl.success("Certificate documented successfully.",{
											    				title : SUCCESS_MSG
											    			 });
														}else{
															growl.success("Template saved successfully.",{
											    				title : SUCCESS_MSG
											    			 });
															
														}	
														if(templateTransaction.encounterId!=0)
															$rootScope.getTemplateCountByAdmissionOrEncounter(templateTransaction.patientId,null,templateTransaction.encounterId);
														$(
																'#addviewTemplateModal')
																.modal('hide');
														$('#addtemplateModal')
																.modal('hide');
														$scope.templateTransactionList = [];
														$scope.templateList = [];
														$scope.templateTransactionList = [];
													}
													$rootScope.stopLoader();
												});
							}
					        
					        /** Template List*/
					        
					       	$scope.getTemplateList = function(templateTypeId) 
					    	{
					    		
					    		$rootScope.startLoader();
					        	var data={
					        		  orgId : $scope.organizationId,
					        		  unitId : $scope.unitId,
					        		  doctorId : $scope.encounterDetailsObj.doctorId,
					        		  specialityId : $scope.encounterDetailsObj.specialityId,
					        		  templateTypeId : templateTypeId,
					        		  patientId : $scope.encounterDetailsObj.patientId,
					        		  visitTypeId: $scope.encounterDetailsObj.visitTypeId,
					        		  encounterId: $scope.encounterDetailsObj.encounterId
					        		}
					        	
					        	console.log('data',JSON.stringify(data));
					    		var URI = BASE_URL + ROOT_URL + "/global/petient/template";
					    		BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
					    				{
					    	
					    			   if(response.data.status=='success')
					    			      {
					    				   	$scope.templateTransactionList = response.data.listObject;
					    				   	$('#viewtemplateModal').modal('show');
					    				   	$rootScope.stopLoader();
					    			      }
					    			   else if(response.data.status=='error'){
					    				   $rootScope.stopLoader();
					    			   }
					    		});
					    	}
					       	
					      
					       	$scope.getDiagisicIndex = function(index)
					       	{
					       		$scope.diagonisIdforTemplate = 0;
					       		if($scope.dataListComplaints[index].diagnosisId=="")
					       			{
					       			growl.warning("Please select Diagnosis first..!",{
					    				title : 'Warning!'
					    			 });
					       			return;
					       			}
					       		else{
					       			$scope.diagonisIdforTemplate = $scope.dataListComplaints[index].diagnosisId;
					       		}
					       	}
					       	
					       	$scope.tempTemplateIndex="";
					       	$scope.getComplaintTemplates = function(templateTypeId,obj,index) 
					    	{
					       		$scope.tempTemplateIndex=index;
								$scope.icpcId=obj.diagnosisId;
                            	$scope.icpcSystemId=obj.icpcSystemId;
							$scope.diagonisIdforTemplate = 0;
							if ($scope.icpcSystemId == 0
									|| $scope.icpcId == 0) {
								growl.warning(
										"Please select ICPC first...!!", {
											title : 'Warning!'
											});
								return;
							}
					    		
					        	var data={
					        		  orgId : $scope.organizationId,
					        		  unitId : $scope.unitId,
					        		  doctorId : $scope.encounterDetailsObj.doctorId,
					        		  specialityId : $scope.encounterDetailsObj.specialityId,
					        		  templateTypeId : templateTypeId,
					        		  patientId : $scope.encounterDetailsObj.patientId,
					        		  visitTypeId: $scope.encounterDetailsObj.visitTypeId,
					        		  encounterId: $scope.encounterDetailsObj.encounterId,
					        		  diagnosisId:$scope.diagonisIdforTemplate,
					        		  mIcpcSystemId : $scope.icpcSystemId,
									  mIcpcId :  $scope.icpcId
					        		}
					        	
					        	var flag=false;
								angular.forEach($scope.templateListForSave,function(value,index){
									if(index==$scope.tempTemplateIndex){
										if(angular.equals({},value.templateObject)){
											value.diagonisIdforTemplate=0;
		                            		value.icpcSystemId=$scope.icpcSystemId;
		                            		value.icpcId=$scope.icpcId;
										}else{
											flag=true;
										}
                            		
									}
                            	});
								
								if(flag==true){
									growl.warning(
											"Template is already added with this complaint!! Click save to view",
											{
												title : 'Warning!'
											});
									return false;
								}
								
					        	
					        	$rootScope.startLoader();
					        	console.log('data',JSON.stringify(data));
					    		var URI = BASE_URL + ROOT_URL + "/api/global/templates/complaints";
					    		BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
					    				{
					    			   if(response.data.status=='success')
					    			      {
					    				   $scope.templateList = response.data.listObject;
					    				   if($scope.templateList.length==0){
												growl.warning(
														"No template mapped with selected ICPC code..!",
														{
															title : 'Warning!'
														});
												return false;
											}
					    				    
					    				   	$('#files').modal('show');
					    				   	$rootScope.stopLoader();
					    			      }
					    			   else if(response.data.status=='error'){
					    				   $rootScope.stopLoader();
					    				   growl.warning("Template is already filled..!",{
							    				title : 'Warning!'
							    			 });
					    			   }
					    				
					    		});
					    	}
					        $scope.getComplaintTemplateByIcpcId=function(templateTypeId,obj){
	                        	 var docId=$scope.encounterDetailsObj.doctorId;
									$rootScope.startLoader();
									var data = {
										orgId : $scope.organizationId,
										unitId : $scope.unitId,
										doctorId : $scope.encounterDetailsObj.doctorId,
										specialityId : $scope.encounterDetailsObj.specialityId == undefined ? 0
												: $scope.encounterDetailsObj.specialityId,
										templateTypeId : templateTypeId,
										patientId : $scope.encounterDetailsObj.patientId,
										visitTypeId : $scope.encounterDetailsObj.visitTypeId,
										encounterId : $scope.encounterDetailsObj.encounterId,
										mIcpcId :obj.diagnosisId
									}

									console.log('data', JSON.stringify(data));
									var URI = BASE_URL + ROOT_URL
											+ "/global/petient/template";
									BillingGenericService
											.serviceAction(METHOD_POST, URI, data,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {

														if (response.data.status == 'success') {
															$scope.templateTransactionList = response.data.listObject;
															$('#viewtemplateModal').modal('show');
															$rootScope.stopLoader();
														} else if (response.data.status == 'error') {
															$rootScope.stopLoader();
														}
													});
								
	                         }
    			$scope.text=function(info,index){
    				if(info=='Y'){
    				$scope.dataListComplaints[index].diagnosisId=51;
    				$scope.dataListComplaints[index].icpcSystemId=1;
    				}
    				if(info=='N'){
    					$scope.dataListComplaints[index].diagnosisId="";
    					$scope.dataListComplaints[index].icpcCodeText="";
    				}
    				
    			}
					
					$scope.onClick = function () {
						   $scope.myChartForSearch = false;
						   $scope.vitalnameGraphList=[];
	                    	 $scope.vitalname=[];
	                    	 $scope.createdTime=[];
	        				 $scope.vitalData=[];
	        				 $scope.vitalTime=[];
	        				 $scope.vitalLabel=[];
	        				 //for label
	        				 if($scope.patientViatalList.length > 0){
							 angular.forEach($scope.patientViatalList, function(value,key) {
								 $scope.flag=false;
								  if($scope.vitalnameGraphList.length > 0){
										angular.forEach($scope.vitalnameGraphList, function(value3,key) {
											if(value3.vitalName == value.vitalName){
	     										$scope.flag = true;
											}
										});
									}
									 if(!$scope.flag){
										var newArr={};
										 newArr.vitalName=value.vitalName;
										 newArr.unitDesc=value.unitDesc;
										 $scope.vitalnameGraphList.push(newArr);
									 }
									 console.log("$scope.vitalnameGraphList",$scope.vitalnameGraphList);
									 
							});
							 //for time array
							 angular.forEach($scope.patientViatalList, function(value,key) {
								 $scope.flagTime=false;
								  if($scope.createdTime.length > 0){
										angular.forEach($scope.createdTime, function(value3,key) {
											if(value3.createdTime1 == value.postFactoTime){
	     										$scope.flagTime = true;
											}
										});
									}
									 if(!$scope.flagTime){
										var newArr={};
										newArr.createdTime=value.createdDate +"   "+value.postFactoTime;
										 newArr.createdTime1=value.postFactoTime;
										 newArr.createdDate=value.createdDate;
										 $scope.createdTime.push(newArr);
									 }
									 console.log("$scope.createdTime",$scope.createdTime);
									 
							});
							 
							for (var i = 0; i < $scope.createdTime.length; i++) {
								$scope.vitalLabel.push($scope.createdTime[i].createdTime);
							}
							console.log("graph label",$scope.vitalLabel);
							
							//for data
							 angular.forEach($scope.createdTime, function(value1,index) {
  								$scope.vitalDataTrends=[];
  								 angular.forEach($scope.patientViatalList, function(value2,index) {
  									 
  									if(value1.createdDate == value2.createdDate){
  										$scope.vitalDataTrends.push(value2);
  									}
  								 });
  								value1.vitalDataTrends = $scope.vitalDataTrends;
  							 });
							 console.log("d",$scope.createdTime);
							$scope.finalVitalObjFromAllVitalList = {};
							var hh =  $scope.createdTime.length;
				             angular.forEach( $scope.createdTime[0].vitalDataTrends, function( value,key) {
                             	if(value.roleId == 2){
                             		if(value.vitalId == 6){
                             			if(parseInt(value.value) != 0){
                             				
                             				$scope.finalVitalObjFromAllVitalList.temp = value.value;
		                                		$scope.finalVitalObjFromAllVitalList.createdDatetemp = value.createdDate;
		                                		$scope.finalVitalObjFromAllVitalList.createdTimetemp = value.createdTime;
		                                		$scope.finalVitalObjFromAllVitalList.unitDesctemp = value.unitDesc;
                             			}
	                                		
	                                	}
	                                	if(value.vitalId == 4){
//	                                		pulse rate
	                                		if(parseInt(value.value) != 0){
	                                		$scope.finalVitalObjFromAllVitalList.pulseRate = value.value;
	                                		$scope.finalVitalObjFromAllVitalList.createdDatepulseRate = value.createdDate;
	                                		$scope.finalVitalObjFromAllVitalList.createdTimepulseRate = value.createdTime;
	                                		$scope.finalVitalObjFromAllVitalList.unitDescpulseRate = value.unitDesc;
	                                		}
	                                	}
	                                	if(value.vitalId == 3){
//	                                		BP
	                                		if(parseInt(value.value) != 0){
	                                		$scope.finalVitalObjFromAllVitalList.bps = value.value;
	                                		$scope.finalVitalObjFromAllVitalList.createdDatebps = value.createdDate;
	                                		$scope.finalVitalObjFromAllVitalList.createdTimebps = value.createdTime;
	                                		$scope.finalVitalObjFromAllVitalList.unitDescbps = value.unitDesc;
	                                		}
	                                	}
	                                	if(value.vitalId == 14){
//	                                		SPo2
	                                		if(parseInt(value.value) != 0){
	                                		$scope.finalVitalObjFromAllVitalList.bloodOxygen = value.value;
	                                		$scope.finalVitalObjFromAllVitalList.createdDatebloodOxygen = value.createdDate;
	                                		$scope.finalVitalObjFromAllVitalList.createdTimebloodOxygen = value.createdTime;
	                                		$scope.finalVitalObjFromAllVitalList.unitDescbloodOxygen = value.unitDesc;
	                                		}
	                                	}
	                                	if(value.vitalId == 7){
//	                                		SPo2
	                                		if(parseInt(value.value) != 0){
	                                		$scope.finalVitalObjFromAllVitalList.bpd = value.value;
	                                		$scope.finalVitalObjFromAllVitalList.createdDatebloodOxygen = value.createdDate;
	                                		$scope.finalVitalObjFromAllVitalList.createdTimebloodOxygen = value.createdTime;
	                                		$scope.finalVitalObjFromAllVitalList.unitDescbloodOxygen = value.unitDesc;
	                                		}
	                                	}
                             	}
                             	
                             });
	        				 }
	                    };
	                    
							$scope.finalVitalObjFromAllVitalList={
									"temp" :"",
	                        		"createdDatetemp" :"",
	                        		"createdTimetemp" :"",
	                        		"unitDesctemp" :"",
	                        		"pulseRate" :"",
	                        		"createdDatepulseRate" :"",
	                        		"createdTimepulseRate" :"",
	                        		"unitDescpulseRat":"",
	                        		"bps" :"",
	                        		"bpd" :"",
	                        		"createdDatebps" :"",
	                        		"createdTimebps" :"",
	                        		"unitDescbps" :"",
	                        	}
							
			                    $scope.checkFocus = function(){
									$timeout(function() {
										 document.getElementById("vitalssss_0").focus();
									}, 500);
								} 
			                    
			                    $scope.k = function(){
					            	 
			                    	  var headerObj = {
											    unit_id : $scope.unitId,
												organization_id : $scope.organizationId
											     };
			                    	
			                    	 var paramObjForVital = {
			                       				
//			                       				encounterId : $scope.encounterIdVital,
			                            		   unitId:$scope.unitId,
				                       				organizationId:$scope.organizationId,
				                       				patientId:$scope.encounterDetailsObj.patientId,
//			                       				roleId:$scope.roleId,
//			                       				flag:'N' 
			                       		}
			                    	 var data={
		                    			 	   unitId:$scope.unitId,
		                       				   organizationId:$scope.organizationId,
		                       				   patientId:$scope.encounterDetailsObj.patientId,
		                    	 			}
			                       		console.log("jj",paramObjForVital);
			                    	 $scope.finalVitalObjFromAllVitalList = {};
			                       		// return false;
			                               var URI = BASE_URL + ROOT_URL
			                               + "/api/opd/getListOfVitalsByPatientIdForCoversheet";
			                        GenericServiceParamHeader
			                            .serviceAction(METHOD_POST, URI,
			                                    data,
			                                    NOTIFICATION_MSG_STATUS_FALSE,data, paramObjForVital,headerObj)
			                            .then(
			                                    function(response) {
			                                        console.log(response);
			                             
			                            
			                            if (response.data.status == "success")
			                                $scope.vitalFinalList = [];
			                                $scope.patientViatalListTrends=[];
			                                $scope.patientViatalList  = response.data.listObject;
			                                angular.forEach( $scope.patientViatalList, function( value,key) {
			                                	var newArr = {};
			                                	newArr.createdDate = value.createdDate;
			                                	newArr.createdTime = value.createdTime;
			                                	newArr.data = {
			                                			"vitalId" : value.vitalId,
			                                	        "value"   : value.value,
			                                	}
			                                	$scope.patientViatalListTrends.push(newArr);
			                                });
			                                $scope.onClick();
			                            $scope.vitalsListFOR=[];
										if($scope.showHeadCircuVital == false){
											angular.forEach( $scope.vitalsList, function( value,key) {
												if(value.vitalId != 15){
													$scope.vitalsListFOR.push(value);	
												}
											});
											$scope.vitalsList=[];
											$scope.vitalsList=$scope.vitalsListFOR;
										}
										
			                             angular.forEach($scope.vitalsList, function(value , key) {
			                            	 var checkFlag = false;
			                                 angular.forEach($scope.patientViatalList, function(value1 , key1) {
			                                     	
			                                	
			                                	 if(value.vitalId == value1.vitalId)
			                                		 {
			                                		 	checkFlag = true;
			                                		 	$scope.newArr = {};
			                                		 	$scope.newArr.vitalId = value.vitalId;
			                                		 	$scope.newArr.minValue = value.minValue;
			                                		 	$scope.newArr.maxValue = value.maxValue;
			                                		 	$scope.newArr.vitalName = value.vitalName;
			                                		 	$scope.newArr.vitalIconPath = value.vitalIconPath;
			                                		 	$scope.newArr.unit = value.unit;
			                                		 	$scope.newArr.vitalValue = value1.value;
			                                		 	$scope.newArr.isReviewedFlag = value1.isReviewedFlag;
			                                		 	$scope.newArr.vitalOrderOpd  = value.vitalOrderOpd;
														$scope.newArr.checkQualifier = true;
			                                		 	if($scope.roleId == 2)
			                            		 		{
			                                		 		$scope.newArr.appointmentVitalMapperId = value1.appointmentVitalMapperId;
			                            		 		}
			                                		 	
			                                           
			                                		 }
			                                	
			                                 		});
																if (checkFlag == true) {
																		$scope.vitalFinalList.push($scope.newArr);
																	} else {
																		// alert("fdvfd");
																		var newArr = {};
																		newArr.vitalId = value.vitalId;
				                                            		 	newArr.minValue = value.minValue;
				                                            		 	newArr.maxValue = value.maxValue;
				                                            		 	newArr.vitalName = value.vitalName;
				                                            		 	newArr.vitalIconPath = value.vitalIconPath;
				                                            		 	newArr.unit = value.unit;
				                                            		 	newArr.vitalValue = value.vitalValue;
//				                                            		 	newArr.vitalValue = 0;
				                                            		 	newArr.isReviewedFlag = 'N';
				                                            		 	newArr.vitalOrderOpd  = value.vitalOrderOpd;
				                                            			newArr.checkQualifier = true;
				                                            		 	if($scope.roleId == 2)
			                                            		 		{
				                                            		 		newArr.appointmentVitalMapperId ='';
			                                            		 		}
																		$scope.vitalFinalList.push(newArr);
																	}
			                            	       
			                                });
			                             
			                            
			                             console.log("Final Vital Arr",JSON.stringify($scope.vitalFinalList));
			                             var datalistVital = $scope.vitalFinalList;
											for (var i = 0; i < datalistVital.length; i++) {
												$scope.vitalFinalList[i].vitalValue = '';
											}
			                           
			                                                                                      
			                                    });
			                        
			                        $scope.checkFocus();
			                    
				             }
				             $scope.k();
				             
				             $scope.setListDataComplaints=function(compListFlag){
				            	 if(compListFlag == "All"){
				            		 $scope.compListFlag=compListFlag;
				            	 }
				            	 if(compListFlag == "Resolved"){
				            		 $scope.compListFlag=compListFlag;
				            	 }
				            	 if(compListFlag == "Current"){
				            		 $scope.compListFlag=compListFlag;
				            	 }
				             }
							
				             /****************Save coversheet as template*******************/
					            $scope.coversheetTemplateName=""; 
								$scope.saveCoversheetAsTemplate=function(){
									$rootScope.startLoaderPopup();
						    		var data={
						    				patientId :$scope.encounterDetailsObj.patientId,
											encounterId :$scope.encounterDetailsObj.encounterId,
											createdBy :$scope.userId,
											createdDate :moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											templateName:$scope.coversheetTemplateName,
											doctorId :$scope.encounterDetailsObj.doctorId,
											updatedBy :$scope.userId,
											updatedDate :moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											organizationId :$scope.organizationId,
											unitId :$scope.unitId,
						    				status:"A",
						    		};
						    		
						    		var URI = BASE_URL + ROOT_URL
									+ "/api/opd/saveCoversheetAsTemaplate";
							GenericService
									.serviceAction("POST", URI, data)
									.then(
											function(response) {
												if (response.data.status == "success") {
													$scope.completeConsultation($scope.encounterDetailsObj.roleId);
													 
//													$rootScope.stopLoader();
												}
												
											});
								}
								
								//coversheet As temp list
								  $scope.getListOfCoversheetAStemplate= function(){
							        	$scope.listOfCoversheetAStemplate=[];
							        	var data={
							        			organizationId:$scope.organizationId,
							        			unitId:$scope.unitId
							        	};
										var URI = BASE_URL + ROOT_URL + "/api/opd/getCoversheetAsTemaplateList" 
										
							    		return GenericService.serviceAction("POST",URI,data).then(function(response){
							    			if(response.data.status == "success"){
							    				$scope.listOfCoversheetAStemplate = response.data.listObject;
							    			}
							    			console.log("listOfCoversheetAStemplate", $scope.listOfCoversheetAStemplate);
							    			return $scope.listOfCoversheetAStemplate;
							    		});
							    	}
								  $scope.getListOfCoversheetAStemplate();
								  
								//coversheet As temp list
								/*  $scope.getCovListByTemplateId= function(templateId){
							        	$scope.covListByTemplateId=[];
							        	var data={
							        			covTemplateId:templateId,
							        			organizationId:$scope.organizationId,
							        			unitId:$scope.unitId
							        	};
										var URI = BASE_URL + ROOT_URL + "/api/opd/getAllCoversheetListsByTemplateId" 
										
							    		GenericService.serviceAction("POST",URI,data).then(function(response){
							    			if(response.data.status == "success"){
							    				$scope.covListByTemplateId = response.data.object;
							    				$rootScope.stopLoader();
							    			}
							    			console.log("covListByTemplateId", $scope.covListByTemplateId);
							    				$('#exampleModal').modal('show');
							    			});
							    	}*/
								  
								  
								  
								  $scope.selectedTemplate=function(covTemplateId){
									  $scope.covListByTemplateId=[];
									  if(covTemplateId == 'custom'){
										  $('#customeEmr').modal('show');
										  return false;
									  }
									  else {
										  var data={
								        			covTemplateId:covTemplateId,
								        			organizationId:$scope.organizationId,
								        			unitId:$scope.unitId
								        	};
											var URI = BASE_URL + ROOT_URL + "/api/opd/getAllCoversheetListsByTemplateId" 
											
								    		GenericService.serviceAction("POST",URI,data).then(function(response){
								    			if(response.data.status == "success"){
								    				$scope.covListByTemplateId = response.data.object;
								    				$scope.getCustomeList();
								    				$scope.getCustomePatientId();
								    				$rootScope.stopLoader();
								    			}
								    			console.log("covListByTemplateId", $scope.covListByTemplateId);
								    				$('#templateCoversheteetModal').modal('show');
								    			});
									  }
									  
								  }
								  
								  $scope.confirmSave=function(info){
									  $('#templateCoversheteetModal').modal('hide');
									  $('#confirmSaveCovTemp').modal('show');
									  
								  }
								  
								  //for template lists save
								  $scope.covTempObject={
										  complaintTempList:[],
										  medicalTempList:[],
										 surgicalTempList:[],
										  lifeTempStyleList:[],
										   allergyTempList:[],
										   medicationHistoryTempList:[],
										   treatmentTempList:[],
										   familyTempList:[],
										   personalTempList:[],
										   vaccinationTempList:[],
										   travelTempList:[],
										   vitalTempList:[],
										   generalExaminationTempList:[],
										   systemicExaminationTempList:[],
										   diagnosisTempList:[],
										   diagnosticsTempList:[],
										   medicationTempList:[],
										   adviceTempList:[],
										   consultOrderTempList:[],
										   observationTempList:[],
										   ceratedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedBy:$scope.userId,
											createdBy:$scope.userId,
											status :'A',
											roleId : $scope.roleId,
											encounterId : $scope.encounterDetailsObj.encounterId,
											patientId : $scope.encounterDetailsObj.patientId,
											doctorId:$scope.encounterDetailsObj.doctorId
								  };
								  
								  $scope.saveListDataOfCovTemplate=function(info){
									  
									  $scope.covTempObject.ceratedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									  $scope.covTempObject.createdDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									  $scope.covTempObject.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									  $scope.covTempObject.updatedBy = $scope.userId,
									  $scope.covTempObject.createdBy = $scope.userId,
									  $scope.covTempObject.status = 'A',
									  $scope.covTempObject.roleId = $scope.roleId,
									  $scope.covTempObject.doctorId = $scope.encounterDetailsObj.doctorId,
									  $scope.covTempObject.encounterId = $scope.encounterDetailsObj.encounterId,
									  $scope.covTempObject.patientId = $scope.encounterDetailsObj.patientId
									  $scope.covTempObject.complaintTempList=info.complaintsPatientList;
									  $scope.covTempObject.medicalTempList=info.medicalTempList;
									  $scope.covTempObject.surgicalTempList=info.surgicalTempList;
									  $scope.covTempObject.lifeTempStyleList=info.lifeTempStyleList;
									  $scope.covTempObject. allergyTempList=info.allergyTempList;
									  $scope.covTempObject.medicationHistoryTempList=info.medicationHistoryTempList;
									  $scope.covTempObject.treatmentTempList=info.treatmentTempList;
									  $scope.covTempObject.familyTempList=info.familyTempList;
									  $scope.covTempObject.personalTempList=info.personalTempList;
									  $scope.covTempObject.vaccinationTempList=info.vaccinationTempList;
									  $scope.covTempObject.travelTempList=info.travelTempList;
									  $scope.covTempObject.vitalTempList=info.vitalTempList;
									  $scope.covTempObject.generalExaminationTempList=info.generalExaminationTempList;
									  $scope.covTempObject.systemicExaminationTempList=info.systemicExaminationTempList;
									  $scope.covTempObject.diagnosisTempList=info.diagnosisTempList;
									  $scope.covTempObject.diagnosticsTempList=info.diagnosticsTempList;
									  $scope.covTempObject.medicationTempList=info.medicationTempList;
									  $scope.covTempObject.adviceTempList=info.adviceTempList;
									  $scope.covTempObject.consultOrderTempList=info.consultOrderTempList;
									  $scope.covTempObject.observationTempList=info.observationTempList;
									 
									  
									  var response = coversheetTemplateService.saveCovTempListData($scope.covTempObject);
									  if(response == true){
										  growl.success("Added successfully.!");
										  $('#templateCoversheteetModal').modal('show');
									  }
								  }
								  
								  //for vital growth chart graph
								  $scope.getVitalList = function() {
										$scope.startLoader();
										
										  var headerObj = {
			                                      unit_id : $scope.unitId,
			                                      organization_id : $scope.organizationId
			                                  };

			                              
			                              var paramObjForPer = {
			                              };
			                              var data ={};
										
										var URI = BASE_URL + ROOT_URL
												+ GETVITALMASTERLIST;
										GenericServiceParamHeader
			                            .serviceAction(METHOD_GET, URI, data,
			                                    NOTIFICATION_MSG_STATUS_FALSE,
			                                    paramObjForPer, headerObj)
												.then(
														function(response) {
															console.log(response);
															// $rootScope.startLoader();
															if (response.data.status == "success")
																$scope.vitalsList = response.data.listObject;
															$scope.printListForVitals();
															console.log("Vital List", $scope.vitalsList);
															
														});
										$scope.stopLoader();
									}
									$scope.getVitalList();
								  $scope.printListForVitals=function(){
                                      $scope.vitalsListForPrint=[];
                                        angular.forEach($scope.vitalsList, function(val, key){
                                            if(val.vitalId == 1 || val.vitalId == 2 || val.vitalId == 15){
                                                $scope.vitalsListForPrint.push(val); 
                                            }
                                        });
                                        console.log( "hhhhhhhh",$scope.vitalsListForPrint);
                                  }
	    	                       
	    	                       $scope.printVitalGraph=function(){
                                      $('#myIframe').removeAttr('src');
                                      var data = {
                                               "appName": "HIS",
                                               "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                                               "reportDesignFilePath": "GrowthChart.rpt",
                                               "reportFormat": "PDF",
                                               "exportFileName": "Coversheet",
                                               "reportParams": [
                                                   {
                                                        "paramName": "vital_id",
                                                        "paramType": "String",
                                                        "paramValue": $scope.printVitalId.toString()
                                                  },
                                                  {
                                                      "paramName": "patient_id",
                                                      "paramType": "Integer",
                                                      "paramValue": $scope.encounterDetailsObj.patientId
                                                }
                                       ]
                                      
                            };
                                      var URI =REPORT_API_URI1;
                                       var form = null;
                                       
                                       form = document.createElement("form");
                                       form.setAttribute("id", "reportId");
                                       form.setAttribute("name", "formReportBook");
                                       form.setAttribute("method", "post");
                                       form.setAttribute("action", URI);
                                       form.setAttribute("target", "myIframeGrowth");
                                       
                                       var hiddenField1 = document.createElement("input"); 
                                       hiddenField1.setAttribute("type", "hidden");
                                       hiddenField1.setAttribute("name", "reportParam");
                                       hiddenField1.setAttribute("value", JSON.stringify(data));
                                       
                                       var hiddenField2 = document.createElement("input"); 
                                       hiddenField2.setAttribute("type", "hidden");
                                       hiddenField2.setAttribute("name", "orgId");
                                       hiddenField2.setAttribute("value",$scope.organizationId);
                                       
                                       var hiddenField3 = document.createElement("input"); 
                                       hiddenField3.setAttribute("type", "hidden");
                                       hiddenField3.setAttribute("name", "unitId");
                                       hiddenField3.setAttribute("value", $scope.unitId);
                                       
                                       form.appendChild(hiddenField1);
                                       form.appendChild(hiddenField2);
                                       form.appendChild(hiddenField3);
                                       
                                      
                                       document.body.appendChild(form);
                                       $("form[name='formReportBook']").submit();
                                       $rootScope.stopLoader();
                                  }
	    	                       
	    	                       $timeout( function(){
				                    	
	  		                    	 var paramObjForVital = {
	  		                       				
	  		                            		   unitId:$scope.unitId,
	  			                       				organizationId:$scope.organizationId,
	  			                       				patientId:$scope.encounterDetailsObj.patientId,
	  		                       		}
	  		                    	 var data={
	  	                    			 	   unitId:$scope.unitId,
	  	                       				   organizationId:$scope.organizationId,
	  	                       				   patientId:$scope.encounterDetailsObj.patientId,
	  	                    	 			}
	  		                       		console.log("jj",paramObjForVital);
	  		                       		// return false;
	  		                               var URI = BASE_URL + ROOT_URL
	  		                               + "/api/opd/getListOfVitalsByPatientIdForCoversheet";
	  		                        GenericServiceParamHeader
	  		                            .serviceAction(METHOD_POST, URI,
	  		                                    data,
	  		                                    NOTIFICATION_MSG_STATUS_FALSE,data, paramObjForVital,headerObj)
	  		                            .then(
	  		                                    function(response) {
	  		                                        console.log(response);
	  		                             
	  		                            
	  		                            if (response.data.status == "success")
	  		                                $scope.vitalFinalList = [];
	  		                                $scope.patientViatalList  = response.data.listObject;
	  		                                
	  		                                $scope.vitalDateList = [];
	  			                            $scope.vitalNameList = [];
	  			                            
	  			         
	  			                            angular.forEach($scope.patientViatalList, function(value,key) {
	  		     								 $scope.flagTime=false;
	  		     								  if($scope.vitalDateList.length > 0){
	  		     										angular.forEach($scope.vitalDateList, function(value3,key) {
	  		     											if(value3.createdTime1 == value.postFactoTime){
	  		    	     										$scope.flagTime = true;
	  		    											}
	  		     										});
	  		     									}
	  		     									 if(!$scope.flagTime){
	  													var newArr={};
	  													newArr.createdTime=value.createdDate +"   "+value.postFactoTime;
	  		     										 newArr.createdTime1=value.postFactoTime;
	  		     										 newArr.createdDate=value.createdDate;
	  		     										 value.sortedDateTime =value.createdDate +"   "+value.postFactoTime;
	  		     										$scope.vitalDateList.push(newArr);
	  		     									 }
	  		     									 console.log("$scope.createdTime",$scope.vitalDateList);
	  		     									 
	  		     							});
	  			                            
	  			                            $scope.vitalsListTest=[];
	  			                            angular.forEach($scope.vitalsList, function(value1 , key1) {
	  				                        angular.forEach($scope.patientViatalList, function(value,key) {
	  				                              if(value1.vitalId == value.vitalId){
	  				                            	  var newArr = {};
	  				                            	  newArr.value = value.value;
	  				                            	  newArr.vitalId = value.vitalId;
	  				                            	  newArr.appointmentVitalMapperId = value.appointmentVitalMapperId;
	  				                            	  newArr.sortedDateTime =value.createdDate +"   "+value.postFactoTime;
	  				                            	  $scope.vitalsListTest.push(newArr);
	  				                              }
	  				                        });
	  			                            });
	  			                            
	  			                            console.log("sortedDataList",$scope.vitalsListTest);
	  			                            
	  		                                if($scope.forthTabLevel == 12){
	  		                                	$scope.onClick();
	  		                                	}
	  		                                	// $scope.onClick();
	  		                            console.log("patientVitalList",response.data.listObject);
	  		                            console.log("vitalList",$scope.vitalsList);
	  		                            
	  		                            $scope.vitalsListFOR=[];
	  									if($scope.showHeadCircuVital == false){
	  										angular.forEach( $scope.vitalsList, function( value,key) {
	  											if(value.vitalId != 15){
	  												$scope.vitalsListFOR.push(value);	
	  											}
	  										});
	  										$scope.vitalsList=[];
	  										$scope.vitalsList=$scope.vitalsListFOR;
	  									}
	  									
	  		                             angular.forEach($scope.vitalsList, function(value , key) {
	  		                            	 var checkFlag = false;
	  		                                 angular.forEach($scope.patientViatalList, function(value1 , key1) {
	  		                                     	
	  		                                	
	  		                                	 if(value.vitalId == value1.vitalId)
	  		                                		 {
	  		                                		 	checkFlag = true;
	  		                                		 	$scope.newArr = {};
	  		                                		 	$scope.newArr.vitalId = value.vitalId;
	  		                                		 	$scope.newArr.minValue = value.minValue;
	  		                                		 	$scope.newArr.maxValue = value.maxValue;
	  		                                		 	$scope.newArr.vitalName = value.vitalName;
	  		                                		 	$scope.newArr.vitalIconPath = value.vitalIconPath;
	  		                                		 	$scope.newArr.unit = value.unit;
	  		                                		 	$scope.newArr.vitalValue = value1.value;
	  		                                		 	$scope.newArr.isReviewedFlag = value1.isReviewedFlag;
	  		                                		 	$scope.newArr.vitalOrderOpd  = value.vitalOrderOpd;
	  													$scope.newArr.checkQualifier = true;
	  		                                		 	if($scope.roleId == 2)
	  		                            		 		{
	  		                                		 		$scope.newArr.appointmentVitalMapperId = value1.appointmentVitalMapperId;
	  		                            		 		}
	  		                                		 	
	  		                                           
	  		                                		 }
	  		                                	
	  		                                 		});
	  															if (checkFlag == true) {
	  																	$scope.vitalFinalList.push($scope.newArr);
	  																} else {
	  																	// alert("fdvfd");
	  																	var newArr = {};
	  																	newArr.vitalId = value.vitalId;
	  			                                            		 	newArr.minValue = value.minValue;
	  			                                            		 	newArr.maxValue = value.maxValue;
	  			                                            		 	newArr.vitalName = value.vitalName;
	  			                                            		 	newArr.vitalIconPath = value.vitalIconPath;
	  			                                            		 	newArr.unit = value.unit;
	  			                                            		 	if(value.vitalValue != undefined ){
	  			                                            		 		newArr.vitalValue = value.vitalValue;
	  			                                            		 	}else{
	  			                                            		 		newArr.vitalValue =""
	  			                                            		 	}
	  //newArr.vitalValue = "";
	  			                                            		 	newArr.isReviewedFlag = 'N';
	  			                                            		 	newArr.vitalOrderOpd  = value.vitalOrderOpd;
	  			                                            			newArr.checkQualifier = true;
	  			                                            		 	if($scope.roleId == 2)
	  		                                            		 		{
	  			                                            		 		newArr.appointmentVitalMapperId ='';
	  		                                            		 		}
	  																	$scope.vitalFinalList.push(newArr);
	  																}
	  		                            	       
	  		                                });
	  		                             
	  		                            
	  		                             console.log("Final Vital Arr",JSON.stringify($scope.vitalFinalList));
	  		                             var counter=0;
	  		                                angular.forEach($scope.vitalFinalList,function(value,key){
	  		                                	if( counter == 0){
	  		                                		if(value.vitalId == 2){
	  				                                	angular.forEach($scope.patientViatalList, function(value1 , key1) {
	  				                                		 if(value.vitalId == value1.vitalId)
	  				                                   		 { 
	  				                                			 if(counter == 0){
	  				                                			 if(value1.value != "0"){
	  				                                				 value.vitalValue = value1.value;
	  					                                			 counter++;
	  				                                			 }
	  				                                			 }
	  				                                   		 }
	  				                                	 });
	  				                                	}
	  		                                	}
	  		                                	
	  		                                });
	  		                                var datalistVital = $scope.vitalFinalList;
	  										for (var i = 0; i < datalistVital.length; i++) {
	  											if($scope.vitalFinalList[i].vitalId != 2){
	  												$scope.vitalFinalList[i].vitalValue = '';
	  											}
	  										}
	  										console.log("refreshlist",$scope.vitalFinalList);
	  		                           
	  		                                                                                      
	  		                                    });
	  		                    
	  		                    },4000);
	    	                       
	    	                       $scope.onClick = function () {
									   $scope.myChartForSearch = false;
									   $scope.vitalnameGraphList=[];
				                    	 $scope.vitalname=[];
				                    	 $scope.createdTime=[];
				        				 $scope.vitalData=[];
				        				 $scope.vitalTime=[];
				        				 $scope.vitalLabel=[];
				        				 //for label
				        				 if($scope.patientViatalList.length > 0){
		     							 angular.forEach($scope.patientViatalList, function(value,key) {
		     								 $scope.flag=false;
		     								  if($scope.vitalnameGraphList.length > 0){
		     										angular.forEach($scope.vitalnameGraphList, function(value3,key) {
		     											if(value3.vitalName == value.vitalName){
		    	     										$scope.flag = true;
		    											}
		     										});
		     									}
		     									 if(!$scope.flag){
													var newArr={};
		     										 newArr.vitalName=value.vitalName;
		     										 newArr.unitDesc=value.unitDesc;
		     										 $scope.vitalnameGraphList.push(newArr);
		     									 }
		     									 console.log("$scope.vitalnameGraphList",$scope.vitalnameGraphList);
		     									 
		     							});
		     							// for data
			     							
		     							 angular.forEach($scope.vitalnameGraphList, function(value1,index) {
		     								$scope.vitalData=[];
		     								 angular.forEach($scope.patientViatalList, function(value2,index) {
		     									 
		     									if(value1.vitalName == value2.vitalName){
		     										$scope.vitalData.push(value2.value);
		     										
		     									}
		     								 });
		     								value1.vitalData = $scope.vitalData;
		     							 });
		     						
		     							 
		     							$scope.colorArray = [];
		     							 var letters =[ '#803690', '#00ADF9', '#DCDCDC', '#46BFBD', '#FDB45C', '#949FB1', '#4D5360'] ;  // "0F";
		     						    $scope.colorArray=[ '#803690', '#00ADF9', '#BE2439', '#46BFBD', '#FDB45C', '#006698', '#6A93B0','#992572','#F5FF00','#FF0000','#154889'] ;
		     							console.log("$scope.vitalname",$scope.vitalnameGraphList);
		     							console.log("colorArray",$scope.colorArray);
		     							$scope.dataForDataset=[];
		     							angular.forEach($scope.vitalnameGraphList, function(value,index) {
		     								var newArr = {};
		     								newArr.label = value.vitalName+" ("+ value.unitDesc+")";
		     								newArr.data = value.vitalData;
		     								newArr.backgroundColor =$scope.colorArray[index];
		     								newArr.borderColor =$scope.colorArray[index];
		     								newArr.fill = false;
		     								newArr.borderWidth = 2;
		     								$scope.dataForDataset.push(newArr);
		     							});
		     							console.log("$scope.dataForDatasetVital",$scope.dataForDataset);
				                      var ctx = document.getElementById('myChart').getContext('2d');
				                      var myChart = new Chart(ctx, {
				                    	  type: 'line',
				                    	  data: {
				                    		    labels: $scope.vitalLabel,
				                    		    datasets:$scope.dataForDataset
				                    		  },
				                    		  options: {
				                    			  title:{
				                    				  display: true,
				                    				  text: 'Vital Graph'
				                    			  },
				                    		        legend: {
				                    		        	 display: true,
				                    		            position: "top"
				                    		        },
				                    		        scales: {
				                    		        	animation:true,
				                    		            yAxes: [{
				                    		                ticks: {
				                    		                    fontColor: "rgba(0,0,0,0.5)",
				                    		                    fontStyle: "bold",
				                    		                    beginAtZero: true,
	// maxTicksLimit: 5,
				                    		                    padding: 10
				                    		                },
				                    		                gridLines: {
				                    		                    drawTicks: false,
				                    		                    display: false
				                    		                }

				                    		            }],
				                    		            xAxes: [{
				                    		                gridLines: {
				                    		                    zeroLineColor: "transparent"
				                    		                },
				                    		                ticks: {
				                    		                    padding: 10,
				                    		                    fontColor: "rgba(0,0,0,0.5)",
				                    		                    fontStyle: "bold"
				                    		                }
				                    		            }]
				                    		        }
				                    		    }

				                    	});
		     							 //for time array
		     							 angular.forEach($scope.patientViatalList, function(value,key) {
		     								 $scope.flagTime=false;
		     								  if($scope.createdTime.length > 0){
		     										angular.forEach($scope.createdTime, function(value3,key) {
		     											if(value3.createdTime1 == value.postFactoTime){
		    	     										$scope.flagTime = true;
		    											}
		     										});
		     									}
		     									 if(!$scope.flagTime){
													var newArr={};
													newArr.createdTime=value.createdDate +"   "+value.postFactoTime;
		     										 newArr.createdTime1=value.postFactoTime;
		     										 newArr.createdDate=value.createdDate;
		     										 $scope.createdTime.push(newArr);
		     									 }
		     									 console.log("$scope.createdTime",$scope.createdTime);
		     									 
		     							});
		     							 
		     							for (var i = 0; i < $scope.createdTime.length; i++) {
											$scope.vitalLabel.push($scope.createdTime[i].createdTime);
										}
		     							console.log("graph label",$scope.vitalLabel);
		     							
		     							//for data
		     							 angular.forEach($scope.createdTime, function(value1,index) {
			     								$scope.vitalDataTrends=[];
			     								 angular.forEach($scope.patientViatalList, function(value2,index) {
			     									 
			     									if(value1.createdDate == value2.createdDate){
			     										$scope.vitalDataTrends.push(value2);
			     									}
			     								 });
			     								value1.vitalDataTrends = $scope.vitalDataTrends;
			     							 });
		     							 console.log("d",$scope.createdTime);
		     							$scope.finalVitalObjFromAllVitalList = {};
		     							var hh =  $scope.createdTime.length;
		     				             angular.forEach( $scope.createdTime[0].vitalDataTrends, function( value,key) {
			                                	if(value.roleId == 2){
			                                		if(value.vitalId == 6){
			                                			if(parseInt(value.value) != 0){
			                                				
			                                				$scope.finalVitalObjFromAllVitalList.temp = value.value;
					                                		$scope.finalVitalObjFromAllVitalList.createdDatetemp = value.createdDate;
					                                		$scope.finalVitalObjFromAllVitalList.createdTimetemp = value.createdTime;
					                                		$scope.finalVitalObjFromAllVitalList.unitDesctemp = value.unitDesc;
			                                			}
				                                		
				                                	}
				                                	if(value.vitalId == 4){
//				                                		pulse rate
				                                		if(parseInt(value.value) != 0){
				                                		$scope.finalVitalObjFromAllVitalList.pulseRate = value.value;
				                                		$scope.finalVitalObjFromAllVitalList.createdDatepulseRate = value.createdDate;
				                                		$scope.finalVitalObjFromAllVitalList.createdTimepulseRate = value.createdTime;
				                                		$scope.finalVitalObjFromAllVitalList.unitDescpulseRate = value.unitDesc;
				                                		}
				                                	}
				                                	if(value.vitalId == 3){
//				                                		BP
				                                		if(parseInt(value.value) != 0){
				                                		$scope.finalVitalObjFromAllVitalList.bps = value.value;
				                                		$scope.finalVitalObjFromAllVitalList.createdDatebps = value.createdDate;
				                                		$scope.finalVitalObjFromAllVitalList.createdTimebps = value.createdTime;
				                                		$scope.finalVitalObjFromAllVitalList.unitDescbps = value.unitDesc;
				                                		}
				                                	}
				                                	if(value.vitalId == 14){
//				                                		SPo2
				                                		if(parseInt(value.value) != 0){
				                                		$scope.finalVitalObjFromAllVitalList.bloodOxygen = value.value;
				                                		$scope.finalVitalObjFromAllVitalList.createdDatebloodOxygen = value.createdDate;
				                                		$scope.finalVitalObjFromAllVitalList.createdTimebloodOxygen = value.createdTime;
				                                		$scope.finalVitalObjFromAllVitalList.unitDescbloodOxygen = value.unitDesc;
				                                		}
				                                	}
				                                	if(value.vitalId == 7){
//				                                		SPo2
				                                		if(parseInt(value.value) != 0){
				                                		$scope.finalVitalObjFromAllVitalList.bpd = value.value;
				                                		$scope.finalVitalObjFromAllVitalList.createdDatebloodOxygen = value.createdDate;
				                                		$scope.finalVitalObjFromAllVitalList.createdTimebloodOxygen = value.createdTime;
				                                		$scope.finalVitalObjFromAllVitalList.unitDescbloodOxygen = value.unitDesc;
				                                		}
				                                	}
			                                	}
			                                	
			                                });
				        				 }
				        				 };
				        				 
				        				 $scope.printCaseSheet=function(){

											  var data =  {
											                 "appName": "HIS",
											                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
											                 "reportDesignFilePath": "NewCoverSheet.rpt",
											                 "reportFormat": "PDF",
											                 "exportFileName": "Coversheet",
											                 "reportParams": [
											                	 {
													                  "paramName": "Patientid",
													                  "paramType": "Integer",
													                  "paramValue": $scope.encounterDetailsObj.patientId
												                },
												                {
													                  "paramName": "Unitid",
													                  "paramType": "Integer",
													                  "paramValue": $scope.unitId
												                },
												                {
														             "paramName": "Encounter_id",
														             "paramType": "Integer",
														             "paramValue": $scope.encounterDetailsObj.encounterId
															      }
									                 ]
									      };
											    reportService.generate(REPORT_API_URI1,data);
											}
				        				 
				        				 //previous casesheet
				        				 $scope.getEncounterListForNewCasesheet=function(){
				 							$scope.newCasesheetList =[];
				 							var sdate = new Date();
				 							sdate.setDate(sdate.getDate() - 1);
				 							$scope.yesterday=sdate;

				 							var encounterObj={

				 							unitId :$scope.unitId,
				 							organizationId :$scope.organizationId,
				 							patientId : $scope.encounterDetailsObj.patientId ,
				 							toDate:moment($scope.yesterday).format('YYYY-MM-DD')
				 							}

				 							var URI = BASE_URL + ROOT_URL + "/api/opd/getEncounterDetailsForCoversheet";

				 							BillingGenericService.serviceAction(METHOD_POST,URI,encounterObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){

				 							$scope.newCasesheetList = response.data.listObject;
				 							if($scope.newCasesheetList.length > 0){
				 								$scope.index = $scope.newCasesheetList.length;
				 								$scope.encounterIdPdf = $scope.newCasesheetList[$scope.index-1].encounterId;
				 							}
				 							// if($scope.newCasesheetList.length == 0){
				 							$scope.previousCaseSheetDate=new Date();
				 							$scope.previousCoversheet();
				 							// }
				 							console.log("$scope.encounterListttttttt", $scope.newCasesheetList);
				 							});


				 							}
				 							$scope.getEncounterListForNewCasesheet();
				 							
				 							$scope.objPrint="";
				 							$scope.previousCoversheet=function(){
				 							try{
				 							// index = index - 1;
				 							if($scope.index > 0){
				 							$rootScope.startLoader();
				 							$scope.index = $scope.index - 1;
				 							$scope.previousCaseSheetDate=$scope.newCasesheetList[$scope.index].encounterDateString;

				 							var data={
				 							"encounterId": $scope.encounterIdPdf,
				 							"encounterDateString": moment($scope.previousCaseSheetDate).format("YYYY-MM-DD"),
				 							"organizationId":$scope.organizationId,
				 							"unitId":$scope.unitId
				 							}
				 							var URI=BASE_URL + ROOT_URL + "/api/billing/unit/getPrintDateWiseCoversheet";
				 							GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data).then(function(response) {
				 							if(response.data.status == "success"){
				 							$scope.objPrint = response.data.object;
				 							$('#coversheetPdfFilePath1').attr('src',"data:application/pdf;base64,"+$scope.objPrint);
				 							$rootScope.stopLoader();

				 							}
				 							});
				 							}
				 							}catch(e){
				 							console.log(e.message);
				 							}
				 							}


				 							$scope.getNextCaseShhet=function(){
				 							try{
				 							// index = (index + 1) <= listCoversheet.length ?
				 							// (index + 1): listCoversheet.length;
				 							if($scope.index < $scope.newCasesheetList.length-1 ){
				 							$rootScope.startLoader();
				 							$scope.index = $scope.index + 1
				 							$scope.previousCaseSheetDate=$scope.newCasesheetList[$scope.index].encounterDateString;
				 							var data={
				 							"encounterId": $scope.encounterIdPdf,
				 							"encounterDateString": moment($scope.previousCaseSheetDate).format("YYYY-MM-DD"),
				 							"organizationId":$scope.organizationId,
				 							"unitId":$scope.unitId
				 							}
				 							var URI=BASE_URL + ROOT_URL + "/api/billing/unit/getPrintDateWiseCoversheet";
				 							GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data).then(function(response) {
				 							if(response.data.status == "success"){
				 							$scope.objPrint = response.data.object;
				 							$('#coversheetPdfFilePath1').attr('src',"data:application/pdf;base64,"+$scope.objPrint);
				 							$rootScope.stopLoader();
				 							}
				 							});
				 							}
				 							}catch(e){
				 							console.log(e);
				 							}
				 							}
				 							 $scope.dateOptionsNewCov =
				 								{
				 									formatYear : 'yyyy',
				 									showWeeks : false,
				 									maxDate : new Date()
				 								};
				 							$scope.openDatePickerNewCov = function() {
				 								$scope.datepickerNewCov.opened = true;
				 							};
				 	
				 							$scope.datepickerNewCov = {
				 									opened : false
				 							};

				 							$scope.getDateForCaseSheet=function(date){
				 								$rootScope.startLoader();
				 								var flag=false;
				 								angular.forEach($scope.newCasesheetList ,function(val, i){
				 									var selectDate=moment(date).format("DD-MM-YYYY");
				 									var existDate=moment(val.encounterDateString).format("DD-MM-YYYY");
				 									if(selectDate==existDate){
				 										
				 										var data={
				 							"encounterId": val.encounterId,
				 							"encounterDateString": selectDate,
				 							"organizationId":$scope.organizationId,
				 							"unitId":$scope.unitId
				 							}
				 							var URI=BASE_URL + ROOT_URL + "/api/billing/unit/getPrintDateWiseCoversheet";
				 							GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data).then(function(response) {
				 							if(response.data.status == "success"){
				 								$rootScope.stopLoader();
				 							$scope.objPrint = response.data.object;
//				 							$scope.previousCaseSheetDate=val.encounterDateString;
				 							$('#coversheetPdfFilePath1').attr('src',"data:application/pdf;base64,"+$scope.objPrint);
				 							
				 							

				 							}else{
				 								$rootScope.stopLoader();
				 							}
//				 							$rootScope.stopLoader();
				 							});
				 										
//				 										flag=true;
				 									}else{
				 										$rootScope.stopLoader();
				 									}
//				 									
				 								
				 								});
				 								
				 								
				 							}
				 							
				 							$scope.confirmSaveTemplate=function(){
				 								 $('#templateCoversheteetModal').modal('hide');
				 								 $('#confirmSaveCovTemp').modal('show');
				 							}
				 							
				 							 $scope.compListFlagCurrent ="Current";
											  $scope.selectedComplaintList=function(val){
												  if(val == 1){
													  $scope.compListFlagAll="All";
												  }
												  if(val == 2){
													  $scope.compListFlagResolved="Resolved";
													  $scope.compListFlagAll="";
													  $scope.compListFlagCurrent =""; 

												  }
												  if(val == 3){
													  $scope.compListFlagCurrent ="Current";
													  $scope.compListFlagAll="";
													  $scope.compListFlagResolved =""; 
												  }
												  
											  }
											  
											  /************************Custom EMR Code********************************************************************/
											  //For currentcasesheet
											  $scope.getCustomeList=function(){
													 $rootScope.startLoader();
													var data = {
															'doctorId' : $scope.encounterDetailsObj.doctorId,
															'unitId':$scope.unitId,
															'organizationId':$scope.organizationId,
															'patientId' : $scope.encounterDetailsObj.patientId,

														};
													/*console.log(headers);*/
													$scope.customList = [];
										    		var encounterId = $scope.encounterDetailsObj.encounterId;
										    		var URI = BASE_URL + ROOT_URL + "/api/global/unit/getCustomEMRCategorySubcategoryList" ;

										    		GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,NOTIFICATION_MSG_STATUS_FALSE)
										    		.then(
										    			function(response) {
										    				if (response.data.status == "success")
										    					$scope.customList = response.data.listObject;
										    			});
												}
											  $scope.getCustomeList();
												
												//Update Custom 
												$scope.updateCustom = function(info, index) {
													var URI = BASE_URL + ROOT_URL + "/api/global/unit/updateCustomEMRCategorySubcategory";
													var data = {
															customEmrConfigDetailsId:info.customEmrConfigDetailsId,
															createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
															updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
															updatedBy:$scope.userId,
															createdBy:$scope.userId,
															status:'A',
															patientId : $scope.encounterDetailsObj.patientId,
															doctorId :$scope.encounterDetailsObj.doctorId,
															encounterId: $scope.encounterDetailsObj.encounterId,
															organizationId:$scope.organizationId,
															cusEmrCategoryId:info.cusEmrCategoryId,
															cusEmrSubCategoryId:info.cusEmrSubCategoryId,
															unitId:$scope.unitId,
															remark:info.remark,
															
														
													}
													/*console.log("data",data);
													 return false;*/
													 
													$rootScope.startLoader();
													GenericService.serviceAction("POST", URI, data).then(function (response) {
														if (response.data.status == "success")
															
														{
															$scope.getCustomeList();
															$scope.getCustomePatientId();
															$rootScope.stopLoader();
															growl.success("Updated Successfully..!");
															
														}
														else if (response.data.status == "error") {
															growl.warning(response.data.message,{
																title : ERROR_MSG
															});
															$rootScope.stopLoader();
														}
													});
												}
												///
												$scope.getCustomePatientId = function(){

													$rootScope.startLoader();
													$scope.listOfAllCustomEMRData=[];
													try{

													var data = {

															patientId : $scope.encounterDetailsObj.patientId,
														    organizationId:$scope.organizationId,
														    unitId:$scope.unitId,

														};

													
													var URI = BASE_URL + ROOT_URL + "/api/global/unit/getAllListoCustomEMRByPatientId";
													GenericService.serviceAction("POST",URI,data).then(function(response){
													if (response.data.status == 'success')
													{
														$scope.listOfAllCustomEMRData=response.data.listObject;
													$rootScope.stopLoader();
													$scope.getCustomeList();
													
													} else if(response.data.status == 'error')
													{
													$rootScope.stopLoader();
													growl.warning(response.data.message,
													{
													title : 'Warning!'
													});
													}
													});
													

													}catch(e){
													console.log("Exception",e);
													}
													// }
												}
												$scope.getCustomePatientId();
												/********************Custom EMR code end*******************************/
						 }
						 ]);