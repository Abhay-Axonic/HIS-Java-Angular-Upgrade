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
				'ordersTabController',
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
					'$filter',
					'coversheetTemplateService',
					function($scope, $cookies, $rootScope, GenericService,
							BillingGenericService, $state, $timeout,$interval,
							$stateParams,GenericServiceParamHeader,growl,$compile,reportService,GenericServiceFileUpload,servicesFactory,CONSTANTS,$filter,coversheetTemplateService) {

						/***************tab wise code start****************/
						

						   
						    console.log('$stateParams.appointmentObj',$stateParams.appointmentObj);
						    
						/* init() function for form object create. */
						    
						    //For cylic
							var cookieObject, cookieObjectAppConfig;
							[ cookieObject, cookieObjectAppConfig ] = $scope.checkCookies();


							if(cookieObjectAppConfig.morningFromTime==null){
								$scope.docMorningStartTime = "00:00";
							}else{
								$scope.docMorningStartTime = cookieObjectAppConfig.morningFromTime;
							}
                          if(cookieObjectAppConfig.morningToTime==null){
                        	  $scope.docMorningEndTime = "13:30";
							}else{
								$scope.docMorningEndTime=cookieObjectAppConfig.morningToTime;
							}
                          if(cookieObjectAppConfig.eveningFromTime==null){
                        	  $scope.docEveningStartTime = "13:30";
							}else{
								 $scope.docEveningStartTime =cookieObjectAppConfig.eveningFromTime;
							}
                          if(cookieObjectAppConfig.eveningToTime==null){
                        	  $scope.docEveningEndTime = "23:59";
						   }else{
							   $scope.docEveningEndTime = cookieObjectAppConfig.eveningToTime;
							}
						$scope.init = function() {
							
							 $scope.dateLetter = new Date();
								$scope.deptWiseAppointment='N';
							
								$scope.medicationObj = {
										itemId : "",
										searchItem :"",
										searchItemFre:"",
										favorite_status : false,
										medicationStartDate : new Date(),
										roaId : '',
										dose : '',
										doseUnitId : '',
										durationCount : "",
										durationUnit:'Day',
										instructionId : '',
										medicationEndDate : '',
										frequencyId : '',
										quantity : '',
										uomTypeId : '',
										conversions : '',
										strength : '',
										priorityId : 2,
										isStat : false,
										priorityCode: 'NR',
										isGeneric:'N',
										isInstruction:'N',
										nonFormularyAdvice:'N',
										rate:'',
										prescribedDosageUnitList:[]
									};
								
								$scope.initMedicationObj = function() {
									$scope.searchItemFre = "";
									$scope.searchItem = "";
									$scope.registrationNumber = "";
									$scope.saveBtnFlag = true;
									$scope.updateScope = true;
									$scope.updateBtnFlag = false;
									$scope.roa_options=[];
									$scope.medicationObj = {
										itemId : "",
										searchItem : "",
										formulationTypeId : '',
										favorite_status : false,
										medicationStartDate : new Date(),
										roaId : '',
										dose : '',
										doseUnitId : '',
										durationCount : '',
										durationUnit : 'Day',
										instructionId : '',
										medicationEndDate : '',
										frequencyId : '',
										quantity : '',
										uomTypeId : '',
										conversions : '',
										strength : '',
										priorityId : '2',
										priorityCode : 'NR',
										isGeneric:'N',
										isInstruction:'N',
										nonFormularyAdvice:'N',
										searchItemFre :"",
										isStat : false,
										favorite_status : false,
										isDischarge : false,
										nonFormularyAdvice:'N',
										prescribedDosageUnitList:[]

									};
								}
								
								$scope.medicationDetails = [];
								$scope.favouritMedi = {};
								$scope.itemSelecteds = {};
							
							//$scope.medicationHistoryDetails =[];
							//to show empty row in Medication History 
							$scope.emptyMedicationHistoryRow = {
									itemId:"",
									medicationStartDate:'',
									genericName:'',
									itemName:'',
									genericId:'',
									doctorId:'',
									roaId:'',
									durationCount:'',
									medicationName:'',
									dose:'',
									doseUnitId:'',
									durationUnit:'Day',
									instructionId:'',
									medicationEndDate:'',
									frequencyId:'',
									name:'',
									status:'A',
									authorisedDate:'',
									quantity:'',
									storeId:'',
									nonFormulary:'N',
									genericName:"",
									searchItem:"",
									ongingMedHist:false,
									isMedicationContinue:'N',
									isMedicationStopHis:'N'
							};
							$scope.medicationHistoryDetails = [$scope.emptyMedicationHistoryRow];
							
                          

							$scope.msgRecord="No records found...!!";
							$scope.secondTabLevel = 1;
							// $scope.setCurrentTab(1);
							
							// $scope.itemSelectedList.items=[];
							$scope.itemSelectedList = {
									items: []
								  };
							
							$scope.itemSelectedListActiveMed = {
									itemsMed: [],
									selectAllActMedi:'N'
								  };
							
							$scope.instructionSelectedList = {
									items: []
								  };
							
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
						    
                                if($scope.isAppointmentRequired  == undefined || $scope.isAppointmentRequired == null)
                                   {
                                     $scope.isAppointmentRequired = "N";
                                   }
                               else
                                   {
                                     $scope.isAppointmentRequired = $scope.isAppointmentRequired;
                                   }
							// Start get 50 Previous Year with Current Year
							// and month
							
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
							
							
							// -------------- function to check create opd
							// cover sheet based on appointment
							// -------------//
							
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
//										$scope.isCritical = $scope.encounterDetailsObj.isCritical;
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
							     BillingGenericService.serviceAction(METHOD_POST, URI,commonObj,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												console.log(response);
												// $rootScope.startLoader();
												if (response.data.status == "success")
													$scope.specialityList = response.data.listObject;
											});
							     
					        var headerObj = {
										    unit_id : $scope.unitId,
											organization_id : $scope.organizationId
										     };
                   
										// object for Complaints

				     
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
//					    		if($stateParams.obj.vitalsList != undefined){
//					    			$scope.vitalsList=$stateParams.obj.vitalsList;
//					    		}
					    		if($stateParams.obj.currentEncounterOrderLists!= undefined ){
					    			$scope.currentEncounterOrderLists = $stateParams.obj.currentEncounterOrderLists;
					    		}
					    		if($stateParams.obj.familyHistoryCurrentListDoctor != undefined){
					    			$scope.familyHistoryCurrentListDoctor=$stateParams.obj.familyHistoryCurrentListDoctor;
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
					    		if($stateParams.obj.vitalFinalList!= undefined ){
					    			$scope.vitalFinalList = $stateParams.obj.vitalFinalList;
					    		}
					    		if($stateParams.obj.patientViatalList != undefined){
					    			$scope.patientViatalList=$stateParams.obj.patientViatalList;
					    		}
					    		if($stateParams.obj.patientViatalListTrends!= undefined ){
					    			$scope.patientViatalListTrends = $stateParams.obj.patientViatalListTrends;
					    		}
					    		if($stateParams.obj.vitalDataTrends != undefined){
					    			$scope.vitalDataTrends=$stateParams.obj.vitalDataTrends;
					    		}
					    		if($stateParams.obj.vitalLabel!= undefined ){
					    			$scope.vitalLabel = $stateParams.obj.vitalLabel;
					    		}
					    		if($stateParams.obj.habitTypeList != undefined){
					    			$scope.habitTypeList=$stateParams.obj.habitTypeList;
					    		}
					    		if($stateParams.obj.patientHabitList!= undefined ){
					    			$scope.patientHabitList = $stateParams.obj.patientHabitList;
					    		}
					    		if($stateParams.obj.complaintsPatientList!= undefined ){
					    			$scope.complaintsPatientList = $stateParams.obj.complaintsPatientList;
					    		}
					    		if($stateParams.obj.medicalHistoryCurrentList!= undefined ){
					    			$scope.medicalHistoryCurrentList = $stateParams.obj.medicalHistoryCurrentList;
					    		}
								}
					    		var commonObj = {
					    				/*
										 * isMedication : 'N',
										 * organizationId :
										 * $scope.organizationId
										 */
										
											"organizationId": $scope.organizationId,
											"specialityId": $scope.encounterDetailsObj.specialityId,
											"unitId": $scope.unitId
											
									}
					    		
					    		var URI = BASE_URL + ROOT_URL
								+ "/api/common/getSpecialityByDept";
								
									BillingGenericService
											.serviceAction(METHOD_POST, URI,
													commonObj,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														console.log(response);
														// $rootScope.startLoader();
														if (response.data.status == "success")
															$scope.instructions = response.data.listObject;
													});
					    		var patientId = $scope.encounterDetailsObj.patientId;
					    		var encounterId = $scope.encounterDetailsObj.encounterId;
					    		// enable disable growth
								// chart==========================================
								$scope.checkGrowthChartTab= function()
								{
									if(cookieObjectAppConfig.roleName=="doctor")
									{
										if($scope.paediatricsSpecialityId==$scope.encounterDetailsObj.specialityId)
											{
											  return true;
											}
										else
										   {
											return false;
										   }
									}
									if(cookieObjectAppConfig.roleName=="nurse")
									{
									  var growthFlag = false;
									  sessionStorage.setItem('specialtyNursingStationCookies', JSON.stringify($scope.departmentNursingStationListC));
									  
										var nursingStationSpecialityObj = JSON.parse(sessionStorage.getItem('specialtyNursingStationCookies'));
										if(nursingStationSpecialityObj!=null&&nursingStationSpecialityObj.departmentNursingStationlist!=null)
											{
											angular.forEach(nursingStationSpecialityObj.departmentNursingStationlist, function(value, key) 
											{
												if($scope.paediatricsSpecialityId==value)
													{
													  return true;
													}
											 });
											return false;
											}
									}
									if(cookieObjectAppConfig.roleName=="admin")
									{
									   return true;
									}
								}   
					     console.log("noSignificantPersonal",$scope.noSignificantPersonal);
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
						$scope.isCriticalObj={isCritical:'N'};
                        $scope.isCritical  = 'N';
					/*************************Init End****************************/	


						/** ***** END TAB FOR ALL PAGES IN TREE LEVEL ****** */
						
						/**FOR SET CODE START****** */
						$scope.tab = 9;
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
						/************Complete consultation**************/
						
						$scope.updateEncounterDetailsMultilingual=function(info){
							var headerObjS={
									'organization_id': $scope.organizationId,
									'unit_id':$scope.unitId,
									'Content-Type':'application/json'
							};
							var paramObjForPer={};
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
															$rootScope.stopLoader();
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
//		                                		pulse rate
		                                		if(parseInt(value.value) != 0){
		                                		$scope.finalVitalObjFromAllVitalList.pulseRate = value.value;
		                                		$scope.finalVitalObjFromAllVitalList.createdDatepulseRate = value.createdDate;
		                                		$scope.finalVitalObjFromAllVitalList.createdTimepulseRate = value.createdTime;
		                                		$scope.finalVitalObjFromAllVitalList.unitDescpulseRate = value.unitDesc;
		                                		}
		                                	}
		                                	if(value.vitalId == 3){
//		                                		BP
		                                		if(parseInt(value.value) != 0){
		                                		$scope.finalVitalObjFromAllVitalList.bps = value.value;
		                                		$scope.finalVitalObjFromAllVitalList.createdDatebps = value.createdDate;
		                                		$scope.finalVitalObjFromAllVitalList.createdTimebps = value.createdTime;
		                                		$scope.finalVitalObjFromAllVitalList.unitDescbps = value.unitDesc;
		                                		}
		                                	}
		                                	if(value.vitalId == 14){
//		                                		SPo2
		                                		if(parseInt(value.value) != 0){
		                                		$scope.finalVitalObjFromAllVitalList.bloodOxygen = value.value;
		                                		$scope.finalVitalObjFromAllVitalList.createdDatebloodOxygen = value.createdDate;
		                                		$scope.finalVitalObjFromAllVitalList.createdTimebloodOxygen = value.createdTime;
		                                		$scope.finalVitalObjFromAllVitalList.unitDescbloodOxygen = value.unitDesc;
		                                		}
		                                	}
		                                	if(value.vitalId == 7){
//		                                		SPo2
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
				                       				
//				                       				encounterId : $scope.encounterIdVital,
				                            		   unitId:$scope.unitId,
					                       				organizationId:$scope.organizationId,
					                       				patientId:$scope.encounterDetailsObj.patientId,
//				                       				roleId:$scope.roleId,
//				                       				flag:'N' 
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
//					                                            		 	newArr.vitalValue = 0;
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
					             
					             /*shubhangi*/
					             

					     		// Get Allergen by Allergy Type Id
									$scope.getAllergenListByAllergyId = function(id)
									{
										
										var isGeneric = $.grep($scope.allergyTypeList, function (disCat) {
								            return disCat.allergyTypeId == id;
								        })[0].isGeneric;
										if(isGeneric == 'N'){
										$scope.allergyShowFlag = true;	
										var paramObjForPer = {
												allergy_type_id:id
										};
										var data = {};
										var headerObj = {
												'organization_id':$scope.organizationId,
												'unit_id':$scope.unitId,
												'Content-Type':'application/json'
										};
										var URI = BASE_URL + ROOT_URL
										+ GETALLERGYMASTERBYALLERYTYPEID;
									GenericServiceParamHeader
										.serviceAction(METHOD_GET, URI,
												data,
												NOTIFICATION_MSG_STATUS_FALSE,paramObjForPer,headerObj)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.allergenList = response.data.listObject;
												});
									}
									else
										{
										$scope.allergyShowFlag = false;
										}
									
									
									}
					             
					             $scope.getActiveHabitDetails=function(){
									$rootScope.startLoader();
									   var headerObj = {
												unit_id : $scope.unitId,
												organization_id : $scope.organizationId
											};
									   
											var paramObjForPer = {
											patientId:$scope.encounterDetailsObj.patientId,
											encounterId : $scope.encounterDetailsObj.encounterId,
								    		roleId : $scope.roleId
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
								$scope.getActiveHabitDetails();
							
							// STARY HABIT
							
					             $scope.updateStatusForAcronymsHabit = function(item)
									{
									$scope.habitAcronymsObj = [];
										var perObj = {
											
												  	"habitId": item.habitId,
											        "habitTypeId":item.habitTypeId,
											        "remark":item.remark,
											        "cigarettesPerDay":item.cigarettesPerDay,
											        "yearsSmoked":item.yearsSmoked,
											        "gmsPerDay":item.gmsPerDay,
											        "yearsUsed":item.yearsUsed,
											        "mlsPerDay":item.mlsPerDay,
											        "frequency":item.frequency,
											        "leftWhen":item.leftWhen,
											        "packYear":item.packYear,
											        "typeOfExercise": item.typeOfExercise,
											        "isReviewedFlag":item.isReviewedFlag,
											        "isEnterInErrorStatus":item.isEnterInErrorStatus,
											        "status":item.status,
											        "patientHabitDetailsId":item.patientHabitDetailsId
												
									};
									var paramObj = {};
									
									$scope.habitAcronymsObj.push(perObj);
									var headers={
											'organization_id': $scope.organizationId,
											'unit_id':$scope.unitId,
											'Content-Type':'application/json'
									};
									
											var data = {
											
											createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedBy:$scope.userId,
											createdBy:$scope.userId,
											status:'A',
											encounterId : $scope.encounterDetailsObj.encounterId,
											patientId : $scope.encounterDetailsObj.patientId,
											roleId : $scope.roleId,
											listOfPatientHabitDetailsDto : $scope.habitAcronymsObj
											}
									
											
											console.log(data);
											// return false;
											var URI = BASE_URL + ROOT_URL
											+ UPDATEENTERERRORSTATUSPATIENTHABITDETAILS;
									
											GenericServiceParamHeader
											.serviceAction(METHOD_POST, URI,
													data,
													NOTIFICATION_MSG_STATUS_TRUE,paramObj,headers)
											.then(
													function(response) {
														console.log(response);
														// $rootScope.startLoader();
														if (response.data.status == "success")													
															$scope.init();
															return response.data.listObject;
													});
								}
					              $scope.medication = {
											
											itemId : '',
											doseDesc:'',
											startDate:'',
											frequencyDesc : '',
											frequencyId :'',
											genericName :'',
											instructionDesc :'',
											instructionId : '',
											itemName :'',
											orderQty : '',
											roaId :'',
											roaDesc :'',
											startDate : '',
											strengthDesc :'',
											statflag :'',
									};
					              
					              $scope.getFrequencyMasterData = function() {
								        var URI = CONSTANTS.MASTER.GLOBAL.CLINICAL.FREQUENCY_API +$scope.organizationId;
								        var data = {};
								        $rootScope.startLoader();
								        GenericService.serviceAction("GET", URI, data).then(function(response) {
								          $scope.frequencyMasterDataList1 = [];
								          $scope.frequencyMasterDataList = response.data;
								          angular.forEach($scope.frequencyMasterDataList ,function(value,index){
								        	  if(index == 0){
								        		  if(value.status == true){
									        		  $scope.frequencyMasterDataList1.push(value);
									        	  } 
								        	  }
								          });
								        });
								      }
								    $scope.getFrequencyMasterData();
								    
					              $scope.checkgenericObj = {};
					              $scope.itemStrength=""; 
									$scope.prescribedDosageUnitList=[];
									$scope.medicationObj.isDoseCalculationApplicable=false;
							        $scope.addMedicationItemList = function($item, $model, $label,idx){
							        	$scope.medicationDetails=[];
										console.log($item);
										$scope.itemStrength="";
										$rootScope.startLoader();
										$scope.medicationObj.itemStrength=$item.strength+" "+($item.unitDesc!=null ? $item.unitDesc : " ") ;
										$scope.medicationObj.doseUnitId=$item.strengthUnitId;
										$scope.medicationObj.dose=$item.strength;
										$scope.medicationObj.rate=$item.rate;
										$scope.medicationObj.itemId = ($item.drugId != undefined && $item.drugId !=null) ? $item.drugId :$item.itemId;
										$scope.medicationObj.genericName = "";
										$scope.medicationObj.genericId = $item.genericId;
										$scope.medicationObj.searchGeneric= $item.genericName;
										$scope.medicationObj.medicationName= $item.medicationName!=undefined ? $item.medicationName :$item.itemName;
										$scope.medicationObj.searchItem= $item.medicationName;
										$scope.medicationObj.prescribedDosageUnitList=[];
										$scope.medicationObj.durationCount =5;
										$scope.medicationObj.formulationCounterIndex="";
										$scope.medicationObj.durationUnit = "Day";
										var sDT =  moment((new Date()),"DD-MM-YYYY").toDate();
	                                    var startDT =  moment(sDT).format('YYYY-MM-DD');
	                                    $scope.medicationObj.medicationStartDate = startDT;
	                                    
	                                    if($scope.medicationObj.durationUnit == "Day") {
											var durationCount = parseInt($scope.medicationObj.durationCount);
											}
										    else if($scope.medicationObj.durationUnit == "Week") {
												var durationCount = parseInt($scope.medicationObj.durationCount)
												$scope.medicationObj.durationCount = durationCount* 7;
											}
											else if($scope.medicationObj.durationUnit == "Month") {
												var durationCount = parseInt($scope.medicationObj.durationCount)
												$scope.medicationObj.durationCount = durationCount* 30;
											}
											else if($scope.medicationObj.durationUnit == "Year") {
												var durationCount = parseInt($scope.medicationObj.durationCount)
												var months = durationCount * 12
												$scope.medicationObj.durationCount = months* 30;
											}
									
									if ($scope.medicationObj.durationCount && $scope.medicationObj.durationUnit && $scope.medicationObj.medicationStartDate) {
										if($scope.medicationObj.durationUnit == "Day") {
											var endDate = moment($scope.medicationObj.medicationStartDate).add($scope.medicationObj.durationCount-1, 'days').format('YYYY-MM-DD');
										}
										else if($scope.medicationObj.durationUnit == "Week") {
											var days = $scope.medicationObj.durationCount * 7
											var endDate = moment($scope.medicationObj.medicationStartDate).add($scope.medicationObj.durationCount, 'days').format('YYYY-MM-DD');
										}
										else if($scope.medicationObj.durationUnit == "Month") {
											var days = $scope.medicationObj.durationCount * 30
											var endDate = moment($scope.medicationObj.medicationStartDate).add($scope.medicationObj.durationCount, 'days').format('YYYY-MM-DD');
										}
										else if($scope.medicationObj.durationUnit == "Year") {
											var days = $scope.medicationObj.durationCount * 365;
											var endDate = moment($scope.medicationObj.medicationStartDate).add($scope.medicationObj.durationCount, 'days').format('YYYY-MM-DD');
										}
										$scope.medicationObj.medicationEndDate = endDate;
									}    
									angular.forEach($scope.frequencyMasterDataList1,function(value,key){
										$scope.itemaa= {}
										$scope.itemaa.frequency_id=value.id,
										$scope.itemaa.frequency=value.description,
										$scope.itemaa.noOfAdministrationTime=value.noOfAdministrationTime,
										$scope.model=value.description;
										$scope.label=value.description;
									})
									
									$scope.medicationObj.favorite_status =false;
									$scope.addFrequencyItemList($scope.itemaa, $scope.model, $scope.label,idx,$scope.medicationObj);
										
		                               $scope.favAvailableflag = false;
										
										if($scope.favouriteMedications.length > 0){
											var found = $scope.favouriteMedications.find(ar=>ar.itemId == $scope.medicationObj.itemId);
											   if(found==undefined)
												   {
												      console.log("testOrderSet" ,$scope.medicationDetails);
												   }
											   else{
												   $scope.favAvailableflag = true;
											   }
										}

										var formulationTypeId = $item.formulationTypeId;
										var URI = BASE_URL
												+ ROOT_URL
												+ "/api/opd/getRoaList"
												+ "?unitId="
												+ $scope.unitId
												+ "&orgId="
												+ $scope.organizationId
												+ "&formulationTypeId="
												+ formulationTypeId
												+ "&genericId="
												+ $item.genericId
												+ "&patientId="
												+ $scope.encounterDetailsObj.patientId;

										GenericServiceParamHeader
												.serviceAction(METHOD_GET, URI)
												.then(
														function(response) {
															if (response.data.status == "success") {
																$rootScope.stopLoader();
																var responseList = response.data.listObject;
																$scope.medicaRoaList = responseList;
																if (response.data.object == 0) {
																	if (response.data.listObject.length !== 0) {

																		$scope.roa_options = responseList;
																	}
																	angular.forEach($scope.roa_options,function(value,key){
																		if(value.roa_id != null){
																			 $scope.medicationObj.roaId =(value.roa_id)
																				.toString();
										                                 }
																	})
																	 
																	$scope.medicationObj.genericName = $item.genericName;
																	$scope.medicationObj.formulationTypeId = $item.formulationTypeId;
																	$scope.medicationObj.strength = $item.strength;
																} else {
																	$scope.checkgenericObj = {
																		genericName : $item.genericName,
																		formulationTypeId : $item.formulationTypeId,
																		strength : $item.strength,
																	}
																	angular.element( '#confirmAddItemPopup').modal('show');
																}
															}
															
														});
										
										
										if($scope.favAvailableflag == true){
											var idxFav = $scope.favouriteMedications.findIndex(item => item.itemId == $scope.medicationObj.itemId);
											
											angular.forEach($scope.favouriteMedications,function(value,index){
												if(idxFav == index){
													$scope.itemSelectedList.items=[];
													$scope.itemSelectedList.items.push(value);
												}
											});
											// alert();
											console.log("cssc", $scope.itemSelecteds);
											$scope.medicationExist =false;
											if( $scope.itemSelectedList.items.length > 0){
												
											
												$rootScope.startLoader();
												
											angular.forEach($scope.itemSelectedList.items, function(value, key){
												if(value.doctorFavoriteId != undefined ){
												$scope.medicationObj = {};
												
											     if ($scope.medicationObj.instructionId != null) {
																			var instructionDesc = $.grep( $scope.medicationInstructions, function(
																									disCat) {
																								return disCat.instructionId == $scope.medicationObj.instructionId;
																							})[0].instructionDesc;

																			$scope.medicationObj.instructionDesc = instructionDesc;
																		} else {
																			$scope.medicationObj.instructionDesc = "No instruction";
																		}
												$scope.medicationObj.instructionDesc =value.instructionDesc;
												
												$scope.medicationObj.itemId = value.itemId;
												$scope.medicationObj.doctorFavoriteId=value.doctorFavoriteId;
												$scope.medicationObj.quantity = value.quantity;
												$scope.medicationObj.durationCount = value.durationCount;
												$scope.searchItem = value.itemName;
												$scope.medicationObj.frequencyId = value.frequencyId;
												$scope.medicationObj.frequencyDesc = value.frequencyDesc;
												$scope.model=value.frequencyDesc;
												$scope.label=value.frequencyDesc;
												
												$scope.medicationObj.instructionDesc = value.instructionDesc;
												$scope.medicationObj.instructionId = value.instructionId;
												$scope.medicationObj.durationCount = value.durationCount;
												$scope.medicationObj.durationUnit = value.durationUnit;
												$scope.medicationObj.quantity = value.orderQty;
												$scope.medicationObj.medicationStartDate = new Date();
												$scope.medicationObj.prescribeDose =value.dosageForm;

												$scope.medicationObj.itemName = value.itemName;
												$scope.medicationObj.roaDesc = value.roaDesc;
												$scope.medicationObj.genericName = value.genericName;
												$scope.medicationObj.searchGeneric = value.genericName;
												$scope.medicationObj.genericId = value.genericId;
												$scope.medicationObj.formulationTypeId = value.formulationTypeId;
												$scope.medicationObj.isGeneric ='N';
												$scope.medicationObj.nonFormularyAdvice ='N';
												$scope.medicationObj.isInstruction='N';
												$scope.medicationObj.priorityCode='NR';
												$scope.medicationObj.searchItem =value.itemName;;
												$scope.medicationObj.searchItemFre = value.frequencyDesc;
												$scope.medicationObj.frequencyId = value.frequencyId;
												$scope.medicationObj.rate = value.rate;
												$scope.medicationObj.totalRate= parseInt($scope.medicationObj.rate * (($scope.medicationObj.quantity!=undefined && $scope.medicationObj.quantity!="") ? $scope.medicationObj.quantity :0));
												$scope.medicationObj.prescribedDosageUnitList=[];
												$scope.frequency_id=value.frequencyId;
												$scope.frequency=$scope.medicationObj.searchItemFre;
												$scope.medicationObj.noOfAdministrationTime=value.noOfAdministrationTime;
												$scope.medicationObj.formulationDosageMappId=value.formulationDosageMappId;
												$scope.noOfAdministrationTime=value.noOfAdministrationTime;
												$scope.item={
														frequency_id:$scope.frequency_id,
														frequency:$scope.medicationObj.searchItemFre,
														noOfAdministrationTime:$scope.noOfAdministrationTime
												}
												
												$scope.model=value.frequencyDesc;
												$scope.label=value.frequencyDesc;
												$scope.medicationObj.favorite_status =false;
												
												$scope.addFrequencyItemList($scope.item, $scope.model, $scope.label,idx,$scope.medicationObj);
												var formulationTypeId = value.formulationTypeId;
												var URI = BASE_URL
														+ ROOT_URL
														+ "/api/opd/getRoaList"
														+ "?unitId="
														+ $scope.unitId
														+ "&orgId="
														+ $scope.organizationId
														+ "&formulationTypeId="
														+ formulationTypeId
														+ "&genericId="
														+ value.genericId
														+ "&patientId="
														+ $scope.encounterDetailsObj.patientId;

												GenericServiceParamHeader
														.serviceAction(METHOD_GET, URI)
														.then(
																function(response) {
																	if (response.data.status == "success") {
																		if (response.data.listObject.length !== 0) {
																			var responseList = response.data.listObject;
																			$scope.roa_options = responseList;
																			
																		}
																	}
																});

												 if(value.roaId != null){
													 $scope.medicationObj.roaId =(value.roaId)
														.toString();
				                                 }
												$scope.medicationObj.dose = value.dose;
												$scope.medicationObj.doseUnitId = value.doseUnitId!=null ? value.doseUnitId.toString():0;
												$scope.medicationObj.itemStrength=value.dose+" "+value.doseDesc;
												$scope.medicationObj.unit = value.doseDesc;
												$scope.medicationObj.prescriptionDoseUnit =value.dosageForm;
												var sDT =  moment((new Date()),"DD-MM-YYYY").toDate();
			                                    var eDT = moment(value.stopDate,"DD-MM-YYYY").toDate();
			                                    var startDT =  moment(sDT).format('YYYY-MM-DD');
			                                    var endDT = moment(eDT).format('YYYY-MM-DD');
			                                    $scope.medicationObj.medicationStartDate = startDT;
			                                    
			                                    if($scope.medicationObj.durationUnit == "Day") {
													var durationCount = parseInt($scope.medicationObj.durationCount);
													}
												    else if($scope.medicationObj.durationUnit == "Week") {
														var durationCount = parseInt($scope.medicationObj.durationCount)
														$scope.medicationObj.durationCount = durationCount* 7;
													}
													else if($scope.medicationObj.durationUnit == "Month") {
														var durationCount = parseInt($scope.medicationObj.durationCount)
														$scope.medicationObj.durationCount = durationCount* 30;
													}
													else if($scope.medicationObj.durationUnit == "Year") {
														var durationCount = parseInt($scope.medicationObj.durationCount)
														var months = durationCount * 12
														$scope.medicationObj.durationCount = months* 30;
													}
											
											if ($scope.medicationObj.durationCount && $scope.medicationObj.durationUnit && $scope.medicationObj.medicationStartDate) {
												if($scope.medicationObj.durationUnit == "Day") {
													var endDate = moment($scope.medicationObj.medicationStartDate).add($scope.medicationObj.durationCount-1, 'days').format('YYYY-MM-DD');
												}
												else if($scope.medicationObj.durationUnit == "Week") {
													var days = $scope.medicationObj.durationCount * 7
													var endDate = moment($scope.medicationObj.medicationStartDate).add($scope.medicationObj.durationCount, 'days').format('YYYY-MM-DD');
												}
												else if($scope.medicationObj.durationUnit == "Month") {
													var days = $scope.medicationObj.durationCount * 30
													var endDate = moment($scope.medicationObj.medicationStartDate).add($scope.medicationObj.durationCount, 'days').format('YYYY-MM-DD');
												}
												else if($scope.medicationObj.durationUnit == "Year") {
													var days = $scope.medicationObj.durationCount * 365;
													var endDate = moment($scope.medicationObj.medicationStartDate).add($scope.medicationObj.durationCount, 'days').format('YYYY-MM-DD');
												}
												$scope.medicationObj.medicationEndDate = endDate;
											}                                    
			                                    
											if($scope.medicationDetails.length>0){
			                                    $scope.medicationObj.medicationStartDate = moment($scope.medicationObj.medicationStartDate).format('YYYY-MM-DD');
			                                    angular.forEach($scope.medicationDetails,function(value,index){
			                                        
			                                        if ( $scope.medicationObj.searchItem == value.itemName
			                                                && $scope.medicationObj.medicationStartDate == value.medicationStartDate
//			                                                && $scope.medicationObj.medicationEndDate == value.medicationEndDate
			                                                && $scope.medicationObj.durationCount == value.durationCount
			                                                && $scope.medicationObj.searchItemFre == value.frequencyDesc
			                                                && $scope.medicationObj.doseUnitId == value.doseUnitId
			                                                && $scope.medicationObj.roaId == value.roaId
			                                                && $scope.medicationObj.quantity == value.quantity) {
			                                            
			                                            $scope.medicationExist =true;
			                                        }
			                                    });
			                                }
			                                
			                               if($scope.detailedMedications.length>0){
			                                    $scope.medicationObj.medicationStartDate = moment($scope.medicationObj.medicationStartDate).format('YYYY-MM-DD');
			                                    angular.forEach($scope.detailedMedications,function(value,index){
			                                        
			                                        var sDT =  moment(value.startDate,"DD-MM-YYYY").toDate();
			                                        var eDT = moment(value.endDate,"DD-MM-YYYY").toDate();
			                                        var startDT =  moment(sDT).format('YYYY-MM-DD');
			                                        var endDT = moment(eDT).format('YYYY-MM-DD');
			                                        
			                                        if ( $scope.medicationObj.searchItem == value.itemName
			                                                && $scope.medicationObj.medicationStartDate ==  startDT
//			                                                && $scope.medicationObj.medicationEndDate == endDT
			                                                && $scope.medicationObj.searchItemFre == value.frequencyDesc
			                                                && $scope.medicationObj.dose== value.doseDesc
			                                                && $scope.medicationObj.doseUnitId == value.doseUnitId
			                                                && $scope.medicationObj.quantity == value.orderQty
			                                                && $scope.medicationObj.roaId == value.roaId
			                                                && $scope.medicationObj.isStat == value.statflag) {
			                                            
			                                            $scope.medicationExist =true;
			                                        }
			                                    });
			                                    
			                                }
			                                
			                                
			                                if($scope.medicationExist){
			                                    growl.warning(
			                                            "Medication Already Exist",
			                                            {
			                                                title : 'Warning!'
			                                            });
			                                    $scope.medicationObj.medicationStartDate = new Date();
			                                    //added by chetana
			                                    if ($scope.medicationObj.isDischarge ==true) {
			   										
			   										$scope.medicationObj.isDischarge = "discharge";
			   										 $scope.flagDisch=true;
			   										} else {
			   											$scope.medicationObj.isDischarge = false;
			   										}
			   								 if ($scope.medicationObj.isStat ==true) {
			   										
			   										$scope.medicationObj.isStat = "stat";
			   										$scope.flagStat=true;
			   										} else {
			   											$scope.medicationObj.isStat = false;
			   										}
			                            $rootScope.stopLoader();
			                            return false;
			                            }
			                                
											$scope.medicationDetails.push($scope.medicationObj);
												 
												 $scope.totalSumOfRate = 0;
													angular.forEach($scope.medicationDetails , function(value1,key1){
														$scope.totalSumOfRate+=value1.totalRate;
												   	});
												 console.log("Listtttt",$scope.medicationDetails);
												
				
												 //get formuation dosage list
												angular.forEach($scope.medicationDetails, function(value1, key1){
													 var dataobj = {
																organizationId : $scope.organizationId,
																unitId : $scope.unitId,
																formulationTypeId:value1.formulationTypeId
														};
														$scope.medicationObj.prescribeDose="";
														var URI = BASE_URL + ROOT_URL+ "/api/global/getFormulationTypeToDosageMapperListByFormulationTypeId";
														GenericService.serviceAction("POST", URI, dataobj).then(function(response) {
																	$rootScope.stopLoader();
																	$scope.medicationObj.prescribedDosageUnitList=[];
																	$scope.counterFormulation=1;
																	$scope.medicationObj.formulationCounterIndex="";
																	angular.forEach(response.data.listObject,function(value,index){
																		var unitObj={};
																		unitObj.formulationDosageFromUnit=value.fromConversionUnit;
																		unitObj.formulationDosageFromUnitId=value.fromConversionFactorUnit;
																		unitObj.formulationDosageToUnit=value.toConversionUnit;
																		unitObj.formulationDosageToUnitId=value.toConversionFactorUnit;
																		unitObj.counterIndex=$scope.counterFormulation++;
																		unitObj.formulationDosageFromConversionValue=value.fromConversionFactor;
																		unitObj.formulationDosageToConversionValue=value.toConversionFactor;
																		unitObj.formulationFactorId=value.factorId;
																		unitObj.formulationDosage=value.dosageForm;
																		unitObj.formulationDosageMappId=value.formulationDosageMappId;
																		value1.prescribedDosageUnitList.push(unitObj);
																	});
																	
																	angular.forEach(response.data.listObject,function(value,index){
																		var unitObj={};
																		if(value.factorId==1){
																			unitObj.formulationDosageFromUnit=value.toConversionUnit;
																			unitObj.formulationDosageFromUnitId=value.toConversionFactorUnit;
																			unitObj.formulationDosageToUnit=value.fromConversionUnit;
																			unitObj.formulationDosageToUnitId=value.fromConversionFactorUnit;
																			unitObj.counterIndex=$scope.counterFormulation++;
																			unitObj.formulationDosageFromConversionValue=value.toConversionFactor;
																			unitObj.formulationDosageToConversionValue=value.fromConversionFactor;
																			unitObj.formulationFactorId=value.factorId;
																			unitObj.formulationDosage=value.dosageForm;
																			unitObj.formulationDosageMappId=value.formulationDosageMappId;
																			//value1.formulationDosageMappId=value.formulationDosageMappId;
																			value1.prescribedDosageUnitList.push(unitObj);
																		}
																	});
																	var list=[];
																	if(value1.prescribedDosageUnitList.length>0){
																		 list=$.grep(value1.prescribedDosageUnitList, function (obj) {
																			 return value1.formulationDosageMappId === obj.formulationDosageMappId;
																	    });
																		if(list.length>0){	
																			value1.formulationCounterIndex=list[0].counterIndex;
																		}
																	}
																	$scope.itemSelecteds = [];
														});
														
												});
												
												
											}
											});
//											$scope.initMedicationObj();
											$scope.saveMedication();
											$scope.itemSelectedList.items=[];
//											}
											}else{
												growl.warning("Please select medication ",
														{
															title : 'Warning!'
														});
												
												$rootScope.stopLoader();	
											}
	
										}
										if( $scope.favAvailableflag == false){
											$timeout(function() {
												 $scope.addEmptyRow();
											}, 1000);
										}
										
									}
							        
							        $scope.checkgenericObj = {};
							        $scope.addMedicationGenericList = function($item, $model, $label,idx){
							    		console.log("itemList",$item);
							    		$rootScope.startLoader();
							    		$scope.medicationDetails[idx].itemId = $item.drugId;
							    		$scope.medicationDetails[idx].medicationName = $item.medicationName;
							    		$scope.medicationDetails[idx].searchItem = $item.medicationName;
							    		$scope.medicationDetails[idx].genericName = "";
							    		$scope.medicationDetails[idx].genericId = $item.genericId;
							    		
							    		$scope.medicationDetails[idx].itemStrength=$item.strength+" "+($item.unitDesc!=null ? $item.unitDesc : " " );
							    		$scope.medicationDetails[idx].doseUnitId=$item.strengthUnitId;
							    		$scope.medicationDetails[idx].dose=$item.strength;
							    		$scope.medicationDetails[idx].prescribedDosageUnitList=[]; 
							    		
							    		var formulationTypeId = $item.formulationTypeId;
										var URI = BASE_URL + ROOT_URL + "/api/opd/getRoaList" + "?unitId=" + 
										$scope.unitId + "&orgId=" + $scope.organizationId + "&formulationTypeId=" + formulationTypeId+ "&genericId=" + $item.genericId+ "&patientId=" +$scope.encounterDetailsObj.patientId;
									
										GenericServiceParamHeader.serviceAction(METHOD_GET, URI).then(function(response) {
										if(response.data.status == "success"){
											$rootScope.stopLoader();
											var responseList = response.data.listObject;
											$scope.medicaRoaList = responseList;
											
											console.log("$scope.medicaRoaList", $scope.medicaRoaList);
											if(response.data.object == 0)
												{
													if(response.data.listObject.length !== 0) {
														$scope.medicationDetails[idx].roaList=responseList;
													}
													$scope.showMedicationOptions = true
										    		$scope.medicationDetails[idx].genericName = $item.genericName;
										    		$scope.medicationDetails[idx].formulationTypeId = $item.formulationTypeId;
										    		$scope.medicationDetails[idx].strength = $item.strength;
										    		$scope.medicationDetails[idx].favorite_status = false;
												}
											else{
												$scope.checkgenericObj = {
														genericName:$item.genericName,
														formulationTypeId :$item.formulationTypeId, 
														strength :$item.strength,
														medicationIndx : idx
												}
												angular.element('#confirmAddItemPopup').modal('show');
											}
										}
									});
										
										//get formuation dosage list
										var dataobj = {
												organizationId : $scope.organizationId,
												unitId : $scope.unitId,
												formulationTypeId:$item.formulationTypeId
										};
										 $scope.medicationDetails[idx].prescribeDose="";
										 $scope.medicationDetails[idx].itemStrength=$item.strength+" "+$item.unitDesc;
										var URI = BASE_URL + ROOT_URL+ "/api/global/getFormulationTypeToDosageMapperListByFormulationTypeId";
										GenericService.serviceAction("POST", URI, dataobj).then(function(response) {
													$rootScope.stopLoader();
													 $scope.medicationDetails[idx].prescribedDosageUnitList=[];
													$scope.counterFormulation=1;
													 $scope.medicationDetails[idx].formulationCounterIndex="";
													angular.forEach(response.data.listObject,function(value,index){
														var unitObj={};
														unitObj.formulationDosageFromUnit=value.fromConversionUnit;
														unitObj.formulationDosageFromUnitId=value.fromConversionFactorUnit;
														unitObj.formulationDosageToUnit=value.toConversionUnit;
														unitObj.formulationDosageToUnitId=value.toConversionFactorUnit;
														unitObj.counterIndex=$scope.counterFormulation++;
														unitObj.formulationDosageFromConversionValue=value.fromConversionFactor;
														unitObj.formulationDosageToConversionValue=value.toConversionFactor;
														unitObj.formulationFactorId=value.factorId;
														unitObj.formulationDosage=value.dosageForm;
														unitObj.formulationDosageMappId=value.formulationDosageMappId;
														 $scope.medicationDetails[idx].prescribedDosageUnitList.push(unitObj);
													});
													
													angular.forEach(response.data.listObject,function(value,index){
														var unitObj={};
														if(value.factorId==1){
															unitObj.formulationDosageFromUnit=value.toConversionUnit;
															unitObj.formulationDosageFromUnitId=value.toConversionFactorUnit;
															unitObj.formulationDosageToUnit=value.fromConversionUnit;
															unitObj.formulationDosageToUnitId=value.fromConversionFactorUnit;
															unitObj.counterIndex=$scope.counterFormulation++;
															unitObj.formulationDosageFromConversionValue=value.toConversionFactor;
															unitObj.formulationDosageToConversionValue=value.fromConversionFactor;
															unitObj.formulationFactorId=value.factorId;
															unitObj.formulationDosage=value.dosageForm;
															unitObj.formulationDosageMappId=value.formulationDosageMappId;
															 $scope.medicationDetails[idx].prescribedDosageUnitList.push(unitObj);
														}
													});
													var list=[];
													if( $scope.medicationDetails[idx].prescribedDosageUnitList.length>0 &&  $scope.medicationDetails[idx].prescribedDosageUnitList[0].formulationFactorId==1){
														 list=$.grep($scope.medicationDetails[idx].prescribedDosageUnitList, function (obj) {
													        return $scope.medicationDetails[idx].doseUnitId === obj.formulationDosageFromUnitId;
													        
													    });
														 
														 
														if(list.length>0){	
															$scope.medicationDetails[idx].formulationCounterIndex=list[0].counterIndex;
														}
													}
													
													if($scope.medicationDetails[idx].prescribedDosageUnitList.length>0 &&  $scope.medicationDetails[idx].prescribedDosageUnitList[0].formulationFactorId== 2  ){
														$scope.medicationDetails[idx].prescribeDose='';
													}
										});
										
										var itemId = $item.drugId;
										var URI = BASE_URL + ROOT_URL + "/api/opd/getUOMList" + "?unitId=" + 
										$scope.unitId + "&orgId=" + $scope.organizationId + "&itemId=" + itemId;
									
										GenericServiceParamHeader.serviceAction(METHOD_GET, URI).then(function(response) {
										if(response.data.status == "success"){
											if(response.data.listObject.length !== 0) {
												$scope.medicationDetails[idx].uomTypeId = response.data.listObject[0].uomTypeId;
												$scope.medicationDetails[idx].conversions = response.data.listObject[0].conversions;
											}
										}
									});
										
										$scope.showMedicationOptions = false;
							    	}
							      
							        
							        $scope.searchMedicationFreItems = function(searchKeyword,ind){

							        	$scope.itemList=[];
							        	var unitId = $scope.unitId
										var orgId = $scope.organizationId
										
										var URI = BASE_URL + ROOT_URL + "/api/opd/getFrequencyList" + "?unitId=" + 
											unitId + "&orgId=" + orgId + "&searchPar=" + searchKeyword
							    		return BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
							    			if(response.data.status == "success"){
							    				$scope.itemList = response.data.listObject;
							    			}
							    			if($scope.itemList.length > 0)
							    				{
							    				return $scope.itemList;
							    				}else{
							    					$scope.medicationDetails[ind].searchItemFre='';
							    					$scope.medicationDetails[ind].frequencyId='';
							    				}
							    			
							    		});
							    	}
							        $scope.searchMedicationFreItemsHistory = function(searchKeyword,ind){

							        	$scope.itemList=[];
							        	var unitId = $scope.unitId
										var orgId = $scope.organizationId
										
										var URI = BASE_URL + ROOT_URL + "/api/opd/getFrequencyList" + "?unitId=" + 
											unitId + "&orgId=" + orgId + "&searchPar=" + searchKeyword
							    		return BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
							    			if(response.data.status == "success"){
							    				$scope.itemList = response.data.listObject;
							    			}
							    			if($scope.itemList.length > 0)
							    				{
							    				return $scope.itemList;
							    				}else{
							    					$scope.medicationHistoryDetails[ind].searchItemFre='';
							    					$scope.medicationHistoryDetails[ind].frequencyId='';
							    				}
							    			
							    		});
							    	}
							        $scope.searchMedicationItems = function(searchKeyword){
										if(searchKeyword.length >= 3){
											$scope.itemList=[];
											var unitId = $scope.unitId
											var orgId = $scope.organizationId
											var data={};
											
											var Arr = searchKeyword.split("|");
											searchKeyword = Arr[0];
											searchKeyword = Arr[0];
											var regex=/[^a-z]/gi;
											searchKeyword=searchKeyword.replace(regex," ");
												var URI = BASE_URL + ROOT_URL + GETMEDICATIONDETAILS + "?unitId=" + 
												unitId + "&orgId=" + orgId + "&searchDrug=" + searchKeyword
											return BillingGenericService.serviceAction(METHOD_GET,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
												if(response.data.status == "success"){
													$scope.itemList = response.data.listObject;
													console.log("$scope.itemList",$scope.itemList);
												}
												return $scope.itemList;
											});
										}
							    	}
							        
							        $scope.searchGenericItems = function(searchKeyword){

							        	$scope.genericList=[];
							        	var unitId = $scope.unitId
										var orgId = $scope.organizationId
										 var data={};
										
							        	var Arr = searchKeyword.split("|");
							        	searchKeyword = Arr[0];
							        	
							        	
										var URI = BASE_URL + ROOT_URL + "/api/opd/getGenericListForGenericSearch" + "?unitId=" + 
											unitId + "&orgId=" + orgId + "&searchDrug=" + searchKeyword
							    		return BillingGenericService.serviceAction(METHOD_GET,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
							    			if(response.data.status == "success"){
							    				$scope.genericList = response.data.listObject;
							    				console.log("$scope.genericList",$scope.genericList);
							    			}
							    			return $scope.genericList;
							    		});
							    	}
							        
							    	$scope.showDetailsBtn=function(data){
										$scope.configDetails=data;
									}

									$scope.onDurationUnitChange = function(
											medicationObj, index) {
										$scope.calculateEndDate(medicationObj, index)
									}
							    	
									$scope.validateMedication = function(info){
										var isValid = true;
										if(!info.searchItemFre){
											isValid = false;
											growl.warning("Please enter frequency..!");
										}
										if(!info.durationUnit && !info.durationCount){
											isValid = false;
											growl.warning("Please enter duration..!");
										}
										if(!info.medicationStartDate){
											isValid = false;
											growl.warning("Please enter start date..!");
										}
										if(!info.medicationEndDateFormatted){
											isValid = false;
											growl.warning("Please enter end date..!");
										}
										return isValid;
									}

							    	$scope.updateBtnFlag = false;
									$scope.showUpdateBtn = function(info,idx){
										try
													{	
														$rootScope.startLoader();
														if(info.isNonFormulary == 'N'){
															var URI = BASE_URL + ROOT_URL + "/api/opd/getOPDMedicationListById/" +info.prescriptionId +"/"+ info.prescriptionDetailsid +"/"+$scope.encounterDetailsObj.patientId+"/"+info.itemId +"/"+ $scope.unitId+"/"+ $scope.organizationId ;
														}
														else{
															var URI = BASE_URL + ROOT_URL + "/api/opd/getOPDMedicationListByIdForNonFormulary/" +info.prescriptionId +"/"+ info.prescriptionDetailsid +"/"+$scope.encounterDetailsObj.patientId+"/"+ $scope.unitId+"/"+ $scope.organizationId ;
														}
														console.log("EDIT",URI);
														GenericService.serviceAction("GET",URI).then(function(response){
															console.log("Edit",response);	
															if(response.data.status == "success"){
																$scope.updateFlag = true;
																$scope.updateFlagValidation = true;
																$scope.saveFlag = false;
																$scope.saveBtnFlag = false;
																$scope.updateScope = true;
																$scope.updateBtnFlag = true;
																$rootScope.stopLoader();
																$scope.list = [];
																$scope.list = response.data.listObject;
																console.log("showlist",$scope.list );
																	
																$scope.medicationObj.prescriptionId = info.prescriptionId;
																$scope.medicationObj.prescriptionNo = info.prescriptionNo;
																$scope.medicationObj.frequencyDesc=response.data.listObject[0].frequencyDesc;
																$scope.medicationObj.itemId=response.data.listObject[0].itemId;
																$scope.medicationObj.searchItem=response.data.listObject[0].searchItem;
																$scope.medicationObj.medicationName = response.data.listObject[0].searchItem;
																$scope.medicationObj.genericId=response.data.listObject[0].genericId;
																$scope.medicationObj.doseUnitId=response.data.listObject[0].doseUnitId;
																$scope.medicationObj.dose=response.data.listObject[0].dose;
																$scope.medicationObj.itemStrength=response.data.listObject[0].dose+" "+(response.data.listObject[0].strengthDesc!=null ?response.data.listObject[0].strengthDesc : "");
																$scope.medicationObj.roaId= info.roaId;
																$scope.medicationObj.durationUnit='Day';
																$scope.medicationObj.frequencyId=response.data.listObject[0].frequencyId;
																$scope.medicationObj.searchItemFre=response.data.listObject[0].searchItemFre;
																$scope.medicationObj.genericName=response.data.listObject[0].genericName;
																$scope.medicationObj.searchGeneric=response.data.listObject[0].genericName;
																$scope.medicationObj.genericId=response.data.listObject[0].genericId;
																$scope.medicationObj.prescribeDose=response.data.listObject[0].prescribeDose;
																$scope.medicationObj.isDoseCalculationApplicable=response.data.listObject[0].isDoseCalculationApplicable;
																	
																$scope.medicationObj.doseCalculationApplicableData = response.data.listObject[0].doseCalculationApplicableData;
																$scope.medicationObj.doseCalculationApplicableData=JSON.parse($scope.medicationObj.doseCalculationApplicableData);
																angular.forEach($scope.medicationObj.doseCalculationApplicableData, function(val, key){
																	$scope.medicationObj.quantityForDoseCal=val.quantityForDoseCal;
																	$scope.medicationObj.doseForDoseCal=val.doseForDoseCal;
																	$scope.medicationObj.strengthForDoseCal=val.strengthForDoseCal;
																	$scope.medicationObj.searchItemFre=val.searchItemFre;
																	$scope.medicationObj.noOfAdministrationTimeForDoseCal=val.noOfAdministrationTimeForDoseCal;
																	$scope.medicationObj.durationForDoseCal=val.durationForDoseCal;
																	$scope.medicationObj.durationCountForDoseCal=val.durationCountForDoseCal;
																	$scope.freQua =$scope.medicationObj.noOfAdministrationTimeForDoseCal;
																	$scope.medicationObj.frequencyId = val.frequencyForDoseCal;
																	$scope.frequency_id=val.frequencyForDoseCal;
																	$scope.frequency=$scope.medicationObj.searchItemFre;
																	$scope.medicationObj.durationCount=$scope.medicationObj.durationCountForDoseCal;
																	$scope.medicationObj.duration=$scope.medicationObj.durationForDoseCal;
																	$scope.medicationObj.itemStrength=$scope.medicationObj.strengthForDoseCal !=null ? $scope.medicationObj.strengthForDoseCal:(item.strength+" "+(item.unitDesc!=null ? item.unitDesc : " ") );	
																});	
																
																$scope.medicationObj.rate=response.data.listObject[0].rate;
																$scope.medicationObj.isInstruction='N';
																if(response.data.listObject[0].instructionDesc != null){
																	$scope.medicationObj.instructionDesc=response.data.listObject[0].instructionDesc;
																	$scope.medicationObj.isInstruction='N';
																	$scope.medicationObj.instructionId=response.data.listObject[0].instructionId;
																}else{
																	$scope.medicationObj.isInstructionText=response.data.listObject[0].instructionText;
																	$scope.medicationObj.isInstruction='Y';
																	$scope.medicationObj.instructionId='';
																}
																
																if($scope.medicationObj.instructionId == ''&& $scope.medicationObj.isInstructionText == null){
																	$scope.medicationObj.isInstruction='N';
																}
																
																
																$scope.medicationObj.noOfAdministrationTime=response.data.listObject[0].noOfAdministrationTime;
																$scope.medicationObj.unit=response.data.listObject[0].strengthDesc;
																$scope.medicationObj.priorityId=response.data.listObject[0].priorityId;
																$scope.medicationObj.isStat=response.data.listObject[0].isStat;
																if($scope.medicationObj.isStat==true){
																	$scope.medicationObj.isStat="stat";
																}else{
																	$scope.medicationObj.isStat=false;
																}
																$scope.medicationObj.isDischarge=response.data.listObject[0].isDischargeMedication;
																if($scope.medicationObj.isDischarge==true){
																	$scope.medicationObj.isDischarge="discharge"
																}else{
																	$scope.medicationObj.isDischarge=false;
																}
																$scope.medicationObj.medicationStartDate=moment(response.data.listObject[0].medicationStartDate,"DD-MM-YYYY").toDate();
																$scope.medicationObj.medicationEndDate=moment(response.data.listObject[0].medicationEndDate,"DD-MM-YYYY").toDate();
																$scope.freQua =response.data.listObject[0].noOfAdministrationTime;
																$scope.medicationObj.formulationTypeId=response.data.listObject[0].formulationTypeId;
																$scope.medicationIndex=0;
																$scope.medicationObj.isGeneric='N';
																if(info.isNonFormulary == 'N'){
																	$scope.medicationObj.nonFormularyAdvice='N';
																}else{
																	$scope.medicationObj.nonFormularyAdvice = 'Y';
																	$scope.medicationObj.searchItem = null;
																}
																$scope.frequency_id=$scope.medicationObj.frequencyId;
																$scope.noOfAdministrationTime=$scope.medicationObj.noOfAdministrationTime;
																$scope.medicationObj.prescribedDosageUnitList=[];
																$scope.item={
																	frequency_id:$scope.frequency_id,
																	frequency:response.data.listObject[0].searchItemFre,
																	noOfAdministrationTime:$scope.noOfAdministrationTime
																}
																
																$scope.model=response.data.listObject[0].searchItemFre;
																$scope.label=response.data.listObject[0].searchItemFre;
																// $scope.addFrequencyItemList($scope.item, $scope.model, $scope.label);
																if(info.isNonFormulary == 'N'){
																	var formulationTypeId = response.data.listObject[0].formulationTypeId;
																	var genericId  =response.data.listObject[0].genericId;
																	var URI = BASE_URL + ROOT_URL + "/api/opd/getRoaList" + "?unitId=" + 
																	$scope.unitId + "&orgId=" + $scope.organizationId + "&formulationTypeId=" + 
																	formulationTypeId+ "&genericId=" + genericId+ "&patientId=" +$scope.encounterDetailsObj.patientId;
																	
																	$scope.prescriptionDtlObj=response.data.listObject[0];
																	GenericServiceParamHeader.serviceAction(METHOD_GET, URI).then(function(response) {
																		if(response.data.status == "success"){
																			$rootScope.stopLoader();
																			var responseList = response.data.listObject;
																			$scope.medicaRoaList = responseList;
																			if(response.data.object == 0)
																			{
																				if(response.data.listObject.length !== 0) {
																					
																				$scope.roa_options = responseList;
																				console.log("$scope.roa_options",$scope.roa_options);
																				}
																				$scope.showMedicationOptions = true
																				
																				if($scope.prescriptionDtlObj.prescribeDose!=null && $scope.prescriptionDtlObj.factorId!=null && $scope.prescriptionDtlObj.factorId==1){
																				$scope.medicationObj.prescribeDose=$scope.prescriptionDtlObj.prescribeDose;
																				}else if($scope.prescriptionDtlObj.prescribeDose !=undefined && $scope.prescriptionDtlObj.prescribeDose != null && $scope.prescriptionDtlObj.prescribeDose != ""){
																					$scope.medicationObj.prescribeDose=$scope.prescriptionDtlObj.prescribeDose;
																				}
																				
																				var dataobj = {
																					organizationId : $scope.organizationId,
																					unitId : $scope.unitId,
																					formulationTypeId:$scope.prescriptionDtlObj.formulationTypeId
																				};
																				var URI = BASE_URL + ROOT_URL+ "/api/global/getFormulationTypeToDosageMapperListByFormulationTypeId";
																				GenericService.serviceAction("POST", URI, dataobj).then(function(response) {
																					$rootScope.stopLoader();
																					$scope.medicationObj.prescribedDosageUnitList=[];
																					$scope.counterFormulation=1;
																					$scope.medicationObj.formulationCounterIndex="";
																					angular.forEach(response.data.listObject,function(value,index){
																						var unitObj={};
																						unitObj.formulationDosageFromUnit=value.fromConversionUnit;
																						unitObj.formulationDosageFromUnitId=value.fromConversionFactorUnit;
																						unitObj.formulationDosageToUnit=value.toConversionUnit;
																						unitObj.formulationDosageToUnitId=value.toConversionFactorUnit;
																						unitObj.counterIndex=$scope.counterFormulation++;
																						unitObj.formulationDosageFromConversionValue=value.fromConversionFactor;
																						unitObj.formulationDosageToConversionValue=value.toConversionFactor;
																						unitObj.formulationFactorId=value.factorId;
																						unitObj.formulationDosage=value.dosageForm;
																						unitObj.formulationDosageMappId=value.formulationDosageMappId;
																						$scope.medicationObj.prescribedDosageUnitList.push(unitObj);
																					});
																					
																					angular.forEach(response.data.listObject,function(value,index){
																						var unitObj={};
																						if(value.factorId==1){
																							unitObj.formulationDosageFromUnit=value.toConversionUnit;
																							unitObj.formulationDosageFromUnitId=value.toConversionFactorUnit;
																							unitObj.formulationDosageToUnit=value.fromConversionUnit;
																							unitObj.formulationDosageToUnitId=value.fromConversionFactorUnit;
																							unitObj.counterIndex=$scope.counterFormulation++;
																							unitObj.formulationDosageFromConversionValue=value.toConversionFactor;
																							unitObj.formulationDosageToConversionValue=value.fromConversionFactor;
																							unitObj.formulationFactorId=value.factorId;
																							unitObj.formulationDosage=value.dosageForm;
																							unitObj.formulationDosageMappId=value.formulationDosageMappId;
																							$scope.medicationObj.prescribedDosageUnitList.push(unitObj);
																						}
																					});
																					var list=[];
																					if($scope.medicationObj.prescribedDosageUnitList.length>0 && $scope.medicationObj.prescribedDosageUnitList[0].formulationFactorId==1){
																						list=$.grep($scope.medicationObj.prescribedDosageUnitList, function (obj) {
																							return  $scope.prescriptionDtlObj.prescribeDoseUnitId === obj.formulationDosageFromUnitId;
																						});
																						if(list.length>0){	
																							$scope.medicationObj.formulationCounterIndex=list[0].counterIndex;
																						}
																					}
																					
																					if($scope.medicationObj.prescribedDosageUnitList.length>0 && $scope.medicationObj.prescribedDosageUnitList[0].formulationFactorId==2){
																						list=$.grep($scope.medicationObj.prescribedDosageUnitList, function (obj) {
																							return $scope.prescriptionDtlObj.dosageForm === obj.formulationDosage;
																						});
																						if(list.length>0){	
																							$scope.medicationObj.formulationCounterIndex=list[0].counterIndex;
																						}
																					}
																					$scope.updateMedicationList(info, idx);
																				});
																			}
																		}
																	});
																}
																else
																{
																	$scope.medicationObj.prescribedDosageUnitList=[];
																	var URI = BASE_URL + ROOT_URL + "/api/opd/getRoaListNonFormulary" + "?unitId=" + 
																	$scope.unitId + "&orgId=" + $scope.organizationId +"&patientId=" +  $scope.encounterDetailsObj.patientId;
																
																	GenericServiceParamHeader.serviceAction(METHOD_GET, URI).then(function(response) {
																		if(response.data.status == "success"){
																				$scope.roa_options1 = response.data.listObject;
																				$scope.medicationObj.roaId=$scope.list[0].roa_id;
																		}
																	});
																}
																	
														}else if(response.data.status == 'error'){
															$scope.getDetailedMedication();
															$rootScope.stopLoader();
															growl.warning(response.data.message,
															{
																title : 'Warning!'
															});
														}
													});
															
												}catch(e){
													console.log("Exception-",e);
											}
									}	
									  
									  	/**Update Medication List*********/
									  	  
										  	 
									$scope.updateMedicationList = function(info, idx) {
										if($scope.validateMedication(info)){
											$scope.saveBtnFlag = false;
				      					$scope.updateScope = true;
				      					$scope.updateBtnFlag = true;
				      					
										
										console.log("$scope.medicationDetails",
												$scope.medicationDetails);
										
		                                if($scope.detailedMedications.length>0){
		                                    $scope.medicationObj.medicationStartDate = moment($scope.medicationObj.medicationStartDate).format('YYYY-MM-DD');
											$scope.medicationObj.medicationEndDate = moment($scope.medicationObj.medicationEndDate).format('YYYY-MM-DD');
		                                    angular.forEach($scope.detailedMedications,function(value,index){
		                                        
		                                        var sDT =  moment(value.startDate,"DD-MM-YYYY").toDate();
		                                        var eDT = moment(value.endDate,"DD-MM-YYYY").toDate();
		                                        var startDT =  moment(sDT).format('YYYY-MM-DD');
		                                        var endDT = moment(eDT).format('YYYY-MM-DD');
		                                        
		                                        if ( $scope.searchItem == value.itemName
		                                                && $scope.medicationObj.medicationStartDate ==  startDT
// && $scope.medicationObj.medicationEndDate == endDT
		                                                && $scope.searchItemFre == value.frequencyDesc
		                                                && $scope.medicationObj.doseUnitId == value.doseUnitId
		                                                && $scope.medicationObj.quantity == value.orderQty
		                                                && $scope.medicationObj.roaId == value.roaId
		                                                && $scope.medicationObj.isStat == value.statflag) {
		                                            
		                                            $scope.medicationExist =true;
		                                        }
		                                    });
		                                    
		                                }
		                                
		                                
		                                if($scope.medicationExist){
		                                    growl.warning(
		                                            "Medication Already Exist",
		                                            {
		                                                title : 'Warning!'
		                                            });
		                            $rootScope.stopLoader();
		                            return false;
		                            }
										$rootScope.startLoader();
										// var medicationDetails = $scope.medicationDetails;
										var medicationDetails1 = $scope.detailedMedications[idx];
										var medicationDetailList = [];
										var data = {
												  "prescriptionId":$scope.list[0].prescriptionId,
												  "prescriptionNo":$scope.list[0].prescriptionNo,
												  "prescriptionDetailsid":$scope.list[0].prescriptionDetailsid,
												  "prescriptionFrequencyId":$scope.list[0].prescriptionFrequencyId,
												  "organizationId": $scope.organizationId,
												  "unitId": $scope.unitId,
												  "visitTypeId":1,
												  "doctorId":$scope.encounterDetailsObj.doctorId,
												  "prescriptionDate":moment(new Date()).format('YYYY-MM-DD HH:MM:SS'),
												  "authorisedBy": $scope.encounterDetailsObj.doctorId,
												  "authorisedDate":moment(new Date()).format('YYYY-MM-DD HH:MM:SS'),
												  "addedBy": $scope.userId,
												  "addedDate":moment(new Date()).format('YYYY-MM-DD HH:MM:SS'),
												  "encounterId":$scope.encounterDetailsObj.encounterId,
												  "patientId":$scope.encounterDetailsObj.patientId,
												  "priorityId":2,
												  "deptId" : $scope.encounterDetailsObj.specialityId,
												  "isPharmacologicalValidationSkipIp": false,
												  "medicationType":$scope.medType
										};


										var flag = true;
										// if(medicationDetails.length > 0){
										// angular.forEach(medicationDetails,function(value, key) {

															var instructionId = medicationDetails1.instructionId;
															if (medicationDetails1.medicationStartDate != ''
																	&& medicationDetails1.medicationEndDate != ''
																	&& medicationDetails1.durationCount != ''
																	&& medicationDetails1.durationUnit != ''
// && value.instructionId != ''
																	&& medicationDetails1.quantity != '') {

																var medicationDetailsObj = {};
																medicationDetailsObj["itemId"] = medicationDetails1.itemId;
																medicationDetailsObj["prescriptionId"] = $scope.list[0].prescriptionId;
																medicationDetailsObj["prescriptionDetailsid"] = $scope.list[0].prescriptionDetailsid;
																medicationDetailsObj["prescriptionFrequencyId"] = $scope.list[0].prescriptionFrequencyId;
																medicationDetailsObj["storeId"] = 1;
																medicationDetailsObj["uomUnitId"] = medicationDetails1.uomTypeId;
																medicationDetailsObj["orderQty"] = medicationDetails1.orderQty;
																medicationDetailsObj["strength"] = medicationDetails1.doseDesc;
																medicationDetailsObj["prescriptionDate"] = moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
																medicationDetailsObj["authorisedBy"] = $scope.encounterDetailsObj.patientId;
																medicationDetailsObj["authorisedDate"] = moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
																medicationDetailsObj["dose"] = medicationDetails1.doseDesc;
																medicationDetailsObj["doseUnitId"] = medicationDetails1.doseUnitId;
																medicationDetailsObj["frequencyId"] = medicationDetails1.frequencyId;
																medicationDetailsObj["itemName"] = medicationDetails1.medicationName;
																medicationDetailsObj["genericName"] = medicationDetails1.genericName;
																medicationDetailsObj["routeId"] = medicationDetails1.roaId;
																medicationDetailsObj["duration"] = medicationDetails1.durationCount;
						                                        medicationDetailsObj["durationUnit"] = medicationDetails1.durationUnit;
																medicationDetailsObj["startDate"] = moment(medicationDetails1.medicationStartDate).format("YYYY-MM-DD HH:mm:ss");
																medicationDetailsObj["stopDate"] = moment(medicationDetails1.medicationEndDateFormatted).format("YYYY-MM-DD HH:mm:ss");
																
																   if(medicationDetails1.instructionId != '' && medicationDetails1.instructionId != undefined  && medicationDetails1.instructionId != null ){
																		medicationDetailsObj["instructionId"] = medicationDetails1.instructionId;
																		}else{
																		 medicationDetailsObj["instructionDesc"] = medicationDetails1.instructionDesc;
																		}
																if(medicationDetails1.isStat == 'stat'){
																	medicationDetails1.isStat = true;
																}
																else{
																	medicationDetails1.isStat = false;
																}
																medicationDetailsObj["statflag"] = medicationDetails1.isStat != null ? medicationDetails1.isStat : false;
																medicationDetailsObj["isNonFormulary"] = medicationDetails1.isNonFormulary;
																// medicationDetailsObj["isDischarge"]
																// =
																// value.isDischarge;
																if(medicationDetails1.isDischarge == true)
																	{
																	medicationDetailsObj["isDischarge"] = true;
																	}
																
																if($scope.medicationObj.prescribedDosageUnitList.length>0 && 
																	medicationDetails1.formulationCounterIndex!="" && medicationDetails1.formulationCounterIndex!=null
																	&& medicationDetails1.formulationCounterIndex!=undefined){
																		var list=$.grep(medicationDetails1.prescribedDosageUnitList, function (obj) {
																        	return medicationDetails1.formulationCounterIndex == obj.counterIndex;
																    	});
																	 if(list.length>0){
																		 if(list[0].formulationFactorId==1){
																			 medicationDetailsObj.prescribedDose=medicationDetails1.prescribeDose;
																			 medicationDetailsObj.prescribedDoseUnitId=medicationDetails1.prescribedDoseUnitId;
																			 medicationDetailsObj.factorId=list[0].formulationFactorId;
																			 medicationDetailsObj.formulationDosageMappId=list[0].formulationDosageMappId;
																		 }else{
																			 medicationDetailsObj.factorId=list[0].formulationFactorId;
																			 medicationDetailsObj.formulationDosageMappId=list[0].formulationDosageMappId;
																		 }
																	 }
																}
																
																medicationDetailList.push(medicationDetailsObj);
															} else {
																flag = false;
															}

														// });
										// }else{
										// 	flag = false;
										// }
										if (flag) {
											data.prescriptionDetailsDtosList = medicationDetailList;
											console.log("medicationDetailList", data);
											// return false;
											var URI = BASE_URL + ROOT_URL
													+ "/api/opd/medicationUpdate";

											GenericServiceParamHeader
													.serviceAction(METHOD_POST, URI,
															data,
															NOTIFICATION_MSG_STATUS_TRUE)
													.then(
															function(response) {
																if (response.data.status == "success") {
																	$scope.updateFlag = false;
																	$scope.updateFlagValidation = false;
																	$scope.saveFlag = true;
																	
																	$scope.medicationDetails = [];

																	$scope.medicationObj = {
																		itemId : "",
																		searchItemFre:"",
																		searchItem:"",
																		formulationTypeId : '',
																		favorite_status : false,
																		medicationStartDate:new Date(),
																		roaId : '',
																		dose : '',
																		doseUnitId : '',
																		durationCount : "",
																		durationUnit : 'Day',
																		instructionId : '',
																		medicationEndDate : '',
																		frequencyId : '',
																		quantity : '',
																		uomTypeId : '',
																		conversions : '',
																		strength : '',
																		priorityId : '',
																		isStat : false,
																		isDischarge:false,
																		priorityCode: 'NR',
																		searchItemFre :"",
																		isGeneric:'N',
																		isInstruction:'N',
																		nonFormularyAdvice:'N'

																	};
																	// $scope.initMedicationObj();
																	$scope.totalSumOfRate=0;
																	$scope.getAllActiveMedication();
																	 $scope.getDetailedMedication();
																	 $scope.searchItem = "";
																	 $scope.searchItemFre = "";
																	 $scope.medicationObj.itemStrength="";
																	 $scope.medicationObj.prescribedDosageUnitList=[];
																	 $scope.saveBtnFlag = true;
																	 $scope.updateBtnFlag = false;
																	$rootScope.stopLoader();
																} else {
																	$rootScope
																			.stopLoader();
																}
															});
										} else {
											/*growl.warning(
													     "Please add Medication and then save !!",
															{
																title : 'Warning!'
															});*/
											$scope.medicationDetailList = [];
											$rootScope.stopLoader();
											return false;
										}
	
										}
									}

									$scope.getFlagStat=function(){
										if($scope.medicationObj.isStat =="stat"){
											$scope.flagStat=true;
											$scope.flagDisch=false;
											$scope.medicationObj.isDischarge ="";
										}	
									}

									$scope.openDatePickerMedicationOrder = function(index) {
                                        $scope.datepickerMedicationOrder = {
                                            opened : [ false ],
                                        }
                                        if($scope.detailedMedications != undefined){
                                            angular.forEach($scope.detailedMedications, function(val) {
                                                $scope.datepickerMedicationOrder.opened.push(false);
                                            });
                                        }
                                        for (var i = $scope.detailedMedications.length - 1; i >= 0; i--) {
                                            if (i === index) {
                                                $scope.datepickerMedicationOrder.opened[i] = true;
                                            } else {
                                                $scope.datepickerMedicationOrder.opened[i] = false;
                                            }
                                        }
                                    }

                                    $scope.dateOptionsMedicationOrder = {
                                        formatYear : 'yyyy',
                                        showWeeks : false,
                                        maxDate:new Date(), //max 
                                    };

                                    $scope.openDatePickerMedicationOrderEndDate = function(index) {
                                        $scope.datepickerMedicationOrderEndDate = {
                                            opened : [ false ],
                                        }
                                        if($scope.detailedMedications != undefined){
                                            angular.forEach($scope.detailedMedications, function(val) {
                                                $scope.datepickerMedicationOrderEndDate.opened.push(false);
                                            });
                                        }
                                        for (var i = $scope.detailedMedications.length - 1; i >= 0; i--) {
                                            if (i === index) {
                                                $scope.datepickerMedicationOrderEndDate.opened[i] = true;
                                            } else {
                                                $scope.datepickerMedicationOrderEndDate.opened[i] = false;
                                            }
                                        }
                                    }

                                    $scope.dateOptionsMedicationOrderEndDate = {
                                        formatYear : 'yyyy',
                                        showWeeks : false,
                                        maxDate:new Date(), //max 
                                    };

                                    $scope.onStartDateChange = function(medicationObj, index) {
                                        $scope.calculateEndDate(medicationObj, index)
                                    }

                                    $scope.calculateEndDate = function(medicationObj, index) {
                                var currentMedication = medicationObj;
                                var medicationStartDate = currentMedication.medicationStartDate;
                                var indexOfCurrentMedication = index;
                                var durationCount = parseInt(currentMedication["durationCount"]);
                                var durationUnit = currentMedication["durationUnit"];
                                
                                     if(durationUnit == "Day") {
                                        var durationCount = parseInt(currentMedication["durationCount"]);
                                        }
                                        else if(durationUnit == "Week") {
                                            var durationCount = parseInt(currentMedication["durationCount"])
                                            var durationCount = durationCount* 7;
                                        }
                                        else if(durationUnit == "Month") {
                                            var durationCount = parseInt(currentMedication["durationCount"])
                                            var durationCount = durationCount* 30;
                                        }
                                        else if(durationUnit == "Year") {
                                            var durationCount = parseInt(currentMedication["durationCount"])
                                            var months = durationCount * 12
                                            var durationCount = months* 30;
                                        }
                                
                                if (durationCount && durationUnit && medicationStartDate) {
                                    if(durationUnit == "Day") {
                                        var endDate = moment(medicationStartDate).add(durationCount-1, 'days').format('YYYY-MM-DD');
                                    }
                                    else if(durationUnit == "Week") {
                                        var days = durationCount * 7
                                        var endDate = moment(medicationStartDate).add(durationCount, 'days').format('YYYY-MM-DD');
                                    }
                                    else if(durationUnit == "Month") {
                                        var days = durationCount * 30
                                        var endDate = moment(medicationStartDate).add(durationCount, 'days').format('YYYY-MM-DD');
                                    }
                                    else if(durationUnit == "Year") {
                                        var days = durationCount * 365;
                                        var endDate = moment(medicationStartDate).add(durationCount, 'days').format('YYYY-MM-DD');
                                    }
                                    currentMedication["medicationEndDate"] = endDate;
                                    currentMedication["medicationEndDateFormatted"] = moment(endDate).toDate();
                                    currentMedication["startDate"] = medicationStartDate;
                                    // $scope.medicationHistoryObj =
                                    // currentMedication
										$scope.medicationObj.durationCount = durationCount;
									$scope.medicationObj.durationUnit = durationUnit;
									$scope.medicationObj.medicationStartDate = medicationStartDate;
                                    $scope.medicationDetails[indexOfCurrentMedication] = currentMedication
                                    $scope.model=currentMedication.searchItemFre;
                                    $scope.label=currentMedication.searchItemFre;
                                    $scope.idx=indexOfCurrentMedication;
                                    // $scope.addFrequencyItemList($scope.storeItemOrder,$scope.model,$scope.label,$scope.idx);
                                    $scope.calculateFre($scope.idx);
                                }else{
                                    $scope.medicationObj.quantity ="";
                                }
                                
                                /*
                                 * $timeout(function() {
                                 * document.getElementById("quantitytest").focus(); },
                                 * 1000);
                                 */
                            }

								  
					          	$scope.saveBtnFlagMedication = true;
								$scope.updateBtnFlagMedication = false;
								$scope.updateScope = false;
								$scope.popUpFlag = true;

								$scope.savePatienMedicationDetails = function(){
									$rootScope.startLoader();
									$scope.saveBtnFlagMedication = true;
									$scope.updateBtnFlagMedication = false;
									$scope.updateScope = false;
									$scope.popUpFlag = true;
									console.log("$scope.medicationDetails",$scope.medicationDetails);
									$scope.medicationExist =false;
									
//							        if($scope.complaintsPatientList.length > 0){
							        	
							      
									var medicationDetails = $scope.medicationDetails;
									
									var medicationDetailList = [];
									
									var data = {
											  "organizationId": $scope.organizationId,
											  "unitId": $scope.unitId,
											  "employeeId" : $scope.employeeId,
											  "visitTypeId":1,
											  "doctorId":$scope.encounterDetailsObj.doctorId,
											  "prescriptionDate":moment(new Date()).format('YYYY-MM-DD HH:MM:SS'),
											  "authorisedBy": $scope.encounterDetailsObj.doctorId,
											  "authorisedDate":moment(new Date()).format('YYYY-MM-DD HH:MM:SS'),
											  "addedBy": $scope.userId,
											  "addedDate":moment(new Date()).format('YYYY-MM-DD HH:MM:SS'),
											  "encounterId":$scope.encounterDetailsObj.encounterId,
											  "patientId":$scope.encounterDetailsObj.patientId,
											  "priorityId":2,
											  "deptId" : $scope.encounterDetailsObj.specialityId
									};
									
									for (var i=0 ; i< medicationDetails.length; i++){
										
										if( $scope.medicationDetails.length > 0)
										{
											if(medicationDetails[i].isGeneric != 'Y'){
											 if(medicationDetails[i].itemId== "" && medicationDetails[i].nonFormularyAdvice == "N"){
												 growl.warning("Please select Brand Name and Route Of Administration ...!!",{
														title : 'Warning!'
													});
													$rootScope.stopLoader();
									        		return;
											 }
											 
											 if((medicationDetails[i].medicationName == "" || medicationDetails[i].medicationName == undefined )&& medicationDetails[i].nonFormularyAdvice == "Y" ){
												 growl.warning("Please Enter Brand Name..!",{
														title : 'Warning!'
													});
													$rootScope.stopLoader();
									        		return;
											 }
											}
											if(medicationDetails[i].isGeneric == 'Y' ){
												 if(medicationDetails[i].genericId== ""|| medicationDetails[i].genericId== undefined ){
													 growl.warning("Please select Gneric ...!!",{
															title : 'Warning!'
														});
														$rootScope.stopLoader();
										        		return;
												 }
												}
											
								/*	 if(medicationDetails[i].roaId == "")
												{
										        growl.warning(" Route Of Administration..!",{
													title : 'Warning!'
												});
												$rootScope.stopLoader();
								        		return;
												}
									    else if(medicationDetails[i].doseUnitId == "")
											{
										     growl.warning("Please select Dose and Unit..!",{
												title : 'Warning!'
											});
											$rootScope.stopLoader();
							        		return;
											}
										else */
											if(medicationDetails[i].frequencyId == "")
											{
											 growl.warning("Please select Frequency..!",{
												title : 'Warning!'
											});
											$rootScope.stopLoader();
							        		return;
											}
										else if(medicationDetails[i].quantity == 0)
										  {
											 growl.warning("Please Enter Duration..!",{
											title : 'Warning!'
										  });
										  $rootScope.stopLoader();
						        		  return;
										}else if(medicationDetails[i].durationUnit == ""){
											
											 growl.warning("Please select Day..!",{
													title : 'Warning!'
												  });
												  $rootScope.stopLoader();
								        		  return;
										}/*else if(medicationDetails[i].prescribedDosageUnitList.length > 0){
											if(medicationDetails[i].prescribedDosageUnitList[0].formulationFactorId == '2'){
												 if( medicationDetails[i].formulationCounterIndex == "" || 
													 medicationDetails[i].formulationCounterIndex == undefined ||
													 isNaN( medicationDetails[i].formulationCounterIndex)){
														
													 growl.warning("Please select prescribed dosage..!",{
															title : 'Warning!'
														  });
														  $rootScope.stopLoader();
										        		  return;
												 }
											}else if(medicationDetails[i].prescribedDosageUnitList[0].formulationFactorId == '1'){
												
												 if( medicationDetails[i].prescribeDose == "" ||
													 medicationDetails[i].prescribeDose == undefined || 
													 isNaN( medicationDetails[i].prescribeDose)){
														
													 growl.warning("Please select prescribed dosage..!",{
															title : 'Warning!'
														  });
														  $rootScope.stopLoader();
										        		  return;
												 }
												 if( medicationDetails[i].formulationCounterIndex == "" ||
													 medicationDetails[i].formulationCounterIndex == undefined || 
													 isNaN( medicationDetails[i].formulationCounterIndex)){
														
													 growl.warning("Please select prescribed dosage..!",{
															title : 'Warning!'
														  });
														  $rootScope.stopLoader();
										        		  return;
												 }
											}
											
											
										}*/
								
									
											
										var medicationStartDate = moment(medicationDetails[i].medicationStartDate).format('YYYY-MM-DD');
										
										var instructionId = medicationDetails[i].instructionId
										
										var medicationDetailsObj = {};
										medicationDetailsObj["itemId"] = medicationDetails[i].itemId;
										medicationDetailsObj["isMedicationStop"] = 'N';
										medicationDetailsObj["itemName"] = medicationDetails[i].medicationName;
										medicationDetailsObj["storeId"] = $scope.storeId; 
										medicationDetailsObj["genericName"] = medicationDetails[i].genericName;
										medicationDetailsObj["uomUnitId"] = medicationDetails[i].uomTypeId;
										medicationDetailsObj["orderQty"] = medicationDetails[i].quantity;
										medicationDetailsObj["strength"] = medicationDetails[i].strength;
										medicationDetailsObj["prescriptionDate"] = moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
										medicationDetailsObj["authorisedBy"] = $scope.encounterDetailsObj.doctorId;
										medicationDetailsObj["authorisedDate"] = moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
										medicationDetailsObj["dose"] = medicationDetails[i].dose;
										medicationDetailsObj["doseUnitId"] = medicationDetails[i].doseUnitId;
										medicationDetailsObj["frequencyId"] = medicationDetails[i].frequencyId;
										medicationDetailsObj["routeId"] = medicationDetails[i].roaId;
										medicationDetailsObj["roaDesc"] = medicationDetails[i].roa_details;
										medicationDetailsObj["startDate"] = medicationStartDate+" 00:00:00";
										medicationDetailsObj["stopDate"] = medicationDetails[i].medicationEndDate+" 00:00:00";
										if(medicationDetails[i].isInstruction == 'Y'){
											medicationDetails[i].instructionId = '';
										}
										if(medicationDetails[i].instructionId != ''){
											medicationDetailsObj["instructionId"] = medicationDetails[i].instructionId;
											medicationDetailsObj["instructionDesc"] = null;
										}else{
											medicationDetailsObj["instructionDesc"] = medicationDetails[i].isInstructionText;
										}
										
										
										medicationDetailsObj["statflag"] = medicationDetails[i].isStat!=null?medicationDetails[i].isStat:false;		
										medicationDetailsObj["isNonFormulary"] =  medicationDetails[i].nonFormularyAdvice;
										//medicationDetailsObj["remark"] =''; 
//										medicationDetailsObj["roaList"]=undefined;
										
										if(medicationDetails[i].prescribedDosageUnitList.length>0 && medicationDetails[i].formulationCounterIndex!="" && medicationDetails[i].formulationCounterIndex!=null
												&& medicationDetails[i].formulationCounterIndex!=undefined){
											 var list=$.grep(medicationDetails[i].prescribedDosageUnitList, function (obj) {
										        return medicationDetails[i].formulationCounterIndex === obj.counterIndex;
										    });
											 if(list.length>0){
												 if(list[0].formulationFactorId==1){
													 medicationDetailsObj.prescribedDose=medicationDetails[i].prescribeDose;
													 medicationDetailsObj.prescribedDoseUnitId=medicationDetails[i].prescribedDoseUnitId;
													 medicationDetailsObj.factorId=list[0].formulationFactorId;
													 medicationDetailsObj.formulationDosageMappId=list[0].formulationDosageMappId;
												 }else{
													 medicationDetailsObj.factorId=list[0].formulationFactorId;
													 medicationDetailsObj.formulationDosageMappId=list[0].formulationDosageMappId;
												 }
											 }
											
										}
										
									       if($scope.detailedMedications.length>0){
			                                    $scope.medicationObj.medicationStartDate = moment($scope.medicationObj.medicationStartDate).format('YYYY-MM-DD');
			                                    angular.forEach($scope.detailedMedications,function(value,index){
			                                        
			                                        var sDT =  moment(value.startDate,"DD-MM-YYYY").toDate();
			                                        var eDT = moment(value.endDate,"DD-MM-YYYY").toDate();
			                                        var startDT =  moment(sDT).format('YYYY-MM-DD');
			                                        var endDT = moment(eDT).format('YYYY-MM-DD');
			                                        
			                                        if ( medicationDetails[i].searchItem == value.itemName
			                                                && medicationStartDate ==  startDT
//			                                                && medicationDetails[i].medicationEndDate == endDT
			                                                && medicationDetails[i].searchItemFre == value.frequencyDesc
			                                                && medicationDetails[i].doseUnitId == value.doseUnitId
			                                                && medicationDetails[i].quantity == value.orderQty
			                                                && medicationDetails[i].roaId == value.roaId) {
			                                            
			                                            $scope.medicationExist =true;
			                                        }
			                                    });
			                                    
			                                }
									       
											
									        if($scope.medicationExist){
			                                    growl.warning( "Medication Already Exist",
			                                            {
			                                                title : 'Warning!'
			                                            });
			                                   $rootScope.stopLoader();
			                                  return false;
			                                 }
										medicationDetailList.push(medicationDetailsObj);
							
										}
										else{
											growl.warning("Please Enter all fields",
													{
														title : 'Warning!'
													});
											$rootScope.stopLoader();
											return;
										}
									}
									
									data.prescriptionDetailsDtosList = medicationDetailList;
									
								/*	if( data.prescriptionDetailsDtosList.length > 0 ){*/
									console.log("medicationDetailList",data);
									var URI = BASE_URL + ROOT_URL + SAVEPATIENTMEDICATION
									
									GenericServiceParamHeader.serviceAction(METHOD_POST, URI, 
											data, NOTIFICATION_MSG_STATUS_TRUE)
											.then(function(response) {
												if(response.data.status == "success"){
													$scope.medicationDetails = [];
													$scope.emptyMedicationRow = {
															itemId:"",
															favorite_status:false,
															medicationStartDate:new Date(),
															roaId:'',
															dose:'',
															doseUnitId:'',
															durationCount:'',
															durationUnit:'Day',
															instructionId:'',
															medicationEndDate:'',
															frequencyId:'',
															quantity:'',
															uomTypeId:'',
															conversions:'',
															strength :'',
															isStat:false,
															nonFormularyAdvice:'N',
															isGeneric:'N',
															isInstruction:'N',
															roaList:[],
													        checkFlagValidation:false,
															prescribedDosageUnitList:[]
														};
													$scope.medicationDetails.push($scope.emptyMedicationRow);
													$scope.selectedOrderSet=[];
													$scope.getAllActiveMedication();
													 $scope.getDetailedMedication();
										
													 $rootScope.stopLoader();
												}
									});
//							        }else{
//							        	growl.warning("Please enter the complaint to proceed with the medication ordering.",
//												{
//													title : 'warning!'
//												});
//										$rootScope.stopLoader();
//										return;
//							        }
								
								
								}   
					          
					             
								    $scope.addFrequencyItemList = function($item, $model, $label,idx,update){
							        	$scope.updateData=false;
							        	$scope.updateData=update;
										console.log($item);
										$scope.storeItemOrder=$item;
										$scope.medicationObj.searchItemFre=$item.frequency;
										$scope.freQua = 0;
										$scope.medicationObj.frequencyId = $item.frequency_id;
										$scope.freQua = $item.noOfAdministrationTime;
										if($scope.medicationObj.durationCount == ""){
											$scope.medicationObj.durationCount = '';
										}
										var dur = 0;
										if ($scope.medicationObj.durationCount == '') {
											dur = 0;
										} else {
											if($scope.medicationObj.durationUnit == "Day")
											{
												dur = $scope.medicationObj.durationCount;
											}
											else if($scope.medicationObj.durationUnit == "Week")
											{
												var  caldur = $scope.medicationObj.durationCount;
												dur = caldur * 7;
											}
							    			else if($scope.medicationObj.durationUnit == "Month")
											{
												var  caldur = $scope.medicationObj.durationCount;
												dur = caldur * 30;
											}
											else if($scope.medicationObj.durationUnit == "Year")
											{			
												var  caldur = $scope.medicationObj.durationCount;
												dur = caldur * 365;
											}
										}
										$scope.medicationObj.quantity = $item.noOfAdministrationTime * dur;
										if(!$item.frequencyId){
											// update.frequencyId = ($item.frequencyId != undefined || $item.frequencyId != null) ? $item.frequencyId : $item.frequency_id;
											update.frequencyId = $item.frequency_id;
										}
										else{
											// update.frequencyId = ($item.frequency_id != undefined || $item.frequency_id != null) ? $item.frequency_id : $item.frequencyId;
											update.frequencyId = $item.frequencyId;
										}
										update.frequencyDesc = $item.frequency;
										update.noOfAdministrationTime = $item.noOfAdministrationTime;
										$scope.calculateFre(idx);
										// $scope.showUpdateBtn(update);
										$timeout(function() {
											 document.getElementById("quantity").focus();
										}, 2000);
									}
								    
								       $scope.calculateFre = function(idx)
								        {
											
											var list=$.grep($scope.medicationObj.prescribedDosageUnitList, function (obj) {
										        return $scope.medicationObj.formulationCounterIndex == obj.counterIndex;
										    });
											var totalDuration="";
											if($scope.medicationObj.durationUnit=="Day"){
												totalDuration=parseInt($scope.medicationObj.durationCount)*1;
											}else if($scope.medicationObj.durationUnit=="Week"){
												totalDuration=parseInt($scope.medicationObj.durationCount)*7;
											}else if($scope.medicationObj.durationUnit=="Month"){
												totalDuration=parseInt($scope.medicationObj.durationCount)*30;
											}else{
												totalDuration=parseInt($scope.medicationObj.durationCount)*30*12;
											}
											if(list.length>0){	
												var presDoseObj=list[0];
												if(list.length>0  && presDoseObj.formulationFactorId==2){
													$scope.medicationObj.prescriptionDoseUnit=presDoseObj.formulationDosage;
												}else{
													$scope.medicationObj.prescriptionDoseUnit=presDoseObj.formulationDosageFromUnit;
												}
												$scope.medicationObj.prescribedDoseUnitId=presDoseObj.formulationDosageFromUnitId;
											}
											if(list.length>0  && $scope.medicationObj.prescribeDose!="" && $scope.medicationObj.prescribeDose!=undefined && $scope.medicationObj.prescribeDose!=null 
													&& $scope.medicationObj.formulationCounterIndex!="" && $scope.medicationObj.formulationCounterIndex!=undefined && $scope.medicationObj.formulationCounterIndex!=null){
												//for conversion factor
													if($scope.medicationObj.prescribedDoseUnitId==$scope.medicationObj.doseUnitId && $scope.medicationObj.dose!=0){
														$scope.medicationObj.quantity =Math.ceil(($scope.freQua* totalDuration*$scope.medicationObj.prescribeDose)/$scope.medicationObj.dose);
													}else{
														if(list.length>0 && $scope.medicationObj.dose!=0){	
														if(presDoseObj.formulationDosageToUnitId==$scope.medicationObj.doseUnitId){
															var convValue=Math.round(($scope.medicationObj.prescribeDose*presDoseObj.formulationDosageToConversionValue)/presDoseObj.formulationDosageFromConversionValue);
															 $scope.medicationObj.quantity = Math.ceil(($scope.freQua* totalDuration*convValue)/$scope.medicationObj.dose);
														}else{
														  $scope.medicationObj.quantity = Math.ceil(($scope.freQua* totalDuration*$scope.medicationObj.prescribeDose)/$scope.medicationObj.dose);
														}
													  }else{
														  $scope.medicationObj.quantity = Math.ceil(($scope.freQua
																	* totalDuration)); 
													  }
														
													}
											}else{
												//using frequency factor 
												if(list.length>0  && presDoseObj.formulationFactorId==2){
													var splitString=(presDoseObj.formulationDosage).split(" ");
													var convValue=0;
													var i;
													for(i=0;i<splitString.length;i++){
														var b=parseFloat(splitString[i]);
														if(Number.isNaN(b)==false){
															convValue=convValue+(math.number(math.fraction(splitString[i])));
														}
													}
													 $scope.medicationObj.quantity = Math.ceil($scope.freQua
																* totalDuration*(convValue==0?1:convValue)); 
												}else{
													$scope.medicationObj.quantity = Math.ceil(($scope.freQua * totalDuration));
													if($scope.medicationObj.quantity==null || isNaN($scope.medicationObj.quantity)){
														$scope.medicationObj.quantity=0;
													}
												}
											}
											angular.forEach($scope.detailedMedications,function(value,index){
												if(idx == index){
													$scope.medicationObj.orderQty=$scope.medicationObj.quantity;
													value.quantity=$scope.medicationObj.quantity;
													value.orderQty=$scope.medicationObj.quantity;
												}
											});
										
											/*if($scope.updateData ==true){
												$scope.updateMedicationList(idx);
											}*/
										}
								        //add medication
								       
								       $scope.addFavouriteMedication = function() {
											// alert();
											console.log("cssc", $scope.itemSelecteds);
											$scope.medicationExist =false;
											if( $scope.itemSelectedList.items.length > 0){
												
											
												$rootScope.startLoader();
												
											angular.forEach($scope.itemSelectedList.items, function(value, key){
												if(value.doctorFavoriteId != undefined ){
												$scope.medicationObj = {};
												
												$scope.medicationObj.isDoseCalculationApplicable = value.isDoseCalculationApplicable;
												$scope.medicationObj.doseCalculationApplicableData = value.doseCalculationApplicableData;
												$scope.medicationObj.doseCalculationApplicableData=JSON.parse($scope.medicationObj.doseCalculationApplicableData);
										        angular.forEach($scope.medicationObj.doseCalculationApplicableData, function(val, key){
										        	$scope.medicationObj.quantityForDoseCal=val.quantityForDoseCal;
										        	$scope.medicationObj.doseForDoseCal=val.doseForDoseCal;
										        	$scope.medicationObj.strengthForDoseCal=val.strengthForDoseCal;
										        	$scope.medicationObj.searchItemFre=val.searchItemFre;
										        	$scope.medicationObj.noOfAdministrationTimeForDoseCal=val.noOfAdministrationTimeForDoseCal;
										        	$scope.medicationObj.durationForDoseCal=val.durationForDoseCal;
										        	$scope.medicationObj.durationCountForDoseCal=val.durationCountForDoseCal;
										        	$scope.freQua =$scope.medicationObj.noOfAdministrationTimeForDoseCal;
													$scope.medicationObj.frequencyId = val.frequencyForDoseCal;
													$scope.frequency_id=val.frequencyForDoseCal;
													$scope.frequency=$scope.medicationObj.searchItemFre;
													$scope.medicationObj.durationCount=$scope.medicationObj.durationCountForDoseCal;
													$scope.medicationObj.duration=$scope.medicationObj.durationForDoseCal;
													$scope.medicationObj.itemStrength=$scope.medicationObj.strengthForDoseCal !=null ? $scope.medicationObj.strengthForDoseCal:(item.strength+" "+(item.unitDesc!=null ? item.unitDesc : " ") );
													
										        });	
										       // $scope.idx=indexOfCurrentMedication;
										        
											     if ($scope.medicationObj.instructionId != null) {
																			var instructionDesc = $.grep( $scope.medicationInstructions, function(
																									disCat) {
																								return disCat.instructionId == $scope.medicationObj.instructionId;
																							})[0].instructionDesc;

																			$scope.medicationObj.instructionDesc = instructionDesc;
																		} else {
																			$scope.medicationObj.instructionDesc = "No instruction";
																		}
												$scope.medicationObj.instructionDesc =value.instructionDesc;
												
												$scope.medicationObj.itemId = value.itemId;
												$scope.medicationObj.doctorFavoriteId=value.doctorFavoriteId;
												$scope.medicationObj.quantity = value.quantity;
												$scope.medicationObj.durationCount = value.durationCount;
												$scope.searchItem = value.itemName;
												$scope.medicationObj.frequencyId = value.frequencyId;
												$scope.medicationObj.frequencyDesc = value.frequencyDesc;
												$scope.model=value.frequencyDesc;
												$scope.label=value.frequencyDesc;
												
												$scope.medicationObj.instructionDesc = value.instructionDesc;
												$scope.medicationObj.instructionId = value.instructionId;
												$scope.medicationObj.durationCount = value.durationCount;
												$scope.medicationObj.durationUnit = value.durationUnit;
												$scope.medicationObj.quantity = value.orderQty;
												$scope.medicationObj.medicationStartDate = new Date();
												// $scope.medicationObj.prescribeDose
												// =value.dosageForm;

												$scope.medicationObj.itemName = value.itemName;
												$scope.medicationObj.roaDesc = value.roaDesc;
												$scope.medicationObj.genericName = value.genericName;
												$scope.medicationObj.searchGeneric = value.genericName;
												$scope.medicationObj.genericId = value.genericId;
												$scope.medicationObj.formulationTypeId = value.formulationTypeId;
												$scope.medicationObj.isGeneric ='N';
												$scope.medicationObj.nonFormularyAdvice ='N';
												$scope.medicationObj.isInstruction='N';
												$scope.medicationObj.priorityCode='NR';
												$scope.medicationObj.searchItem =value.itemName;;
												$scope.medicationObj.searchItemFre = value.frequencyDesc;
												$scope.medicationObj.frequencyId = value.frequencyId;
												$scope.medicationObj.rate = value.rate;
												$scope.medicationObj.totalRate= parseInt($scope.medicationObj.rate * (($scope.medicationObj.quantity!=undefined && $scope.medicationObj.quantity!="") ? $scope.medicationObj.quantity :0));
												$scope.medicationObj.prescribedDosageUnitList=[];
												$scope.frequency_id=value.frequencyId;
												$scope.frequency=$scope.medicationObj.searchItemFre;
												$scope.medicationObj.noOfAdministrationTime=value.noOfAdministrationTime;
												$scope.medicationObj.formulationDosageMappId=value.formulationDosageMappId;
												$scope.noOfAdministrationTime=value.noOfAdministrationTime;
												$scope.item={
														frequency_id:$scope.frequency_id,
														frequency:$scope.medicationObj.searchItemFre,
														noOfAdministrationTime:$scope.noOfAdministrationTime
												}
												
												$scope.model=value.frequencyDesc;
												$scope.label=value.frequencyDesc;
												$scope.medicationObj.favorite_status =false;
												
												$scope.addFrequencyItemList($scope.item, $scope.model, $scope.label);
												var formulationTypeId = value.formulationTypeId;
												var URI = BASE_URL
														+ ROOT_URL
														+ "/api/opd/getRoaList"
														+ "?unitId="
														+ $scope.unitId
														+ "&orgId="
														+ $scope.organizationId
														+ "&formulationTypeId="
														+ formulationTypeId
														+ "&genericId="
														+ value.genericId
														+ "&patientId="
														+ $scope.encounterDetailsObj.patientId;

												GenericServiceParamHeader
														.serviceAction(METHOD_GET, URI)
														.then(
																function(response) {
																	if (response.data.status == "success") {
																		if (response.data.listObject.length !== 0) {
																			var responseList = response.data.listObject;
																			$scope.roa_options = responseList;
																		}
																	}
																});

												 if(value.roaId != null){
													 $scope.medicationObj.roaId =(value.roaId)
														.toString();
				                                 }
												$scope.medicationObj.dose = value.dose;
			                                    $scope.medicationObj.strengthDesc = value.doseDesc;
			                                    $scope.medicationObj.isDischarge = value.isDischarge;
												$scope.medicationObj.doseUnitId = value.doseUnitId!=null ? value.doseUnitId.toString():0;
												$scope.medicationObj.itemStrength=value.dose+" "+value.doseDesc;
												$scope.medicationObj.unit = value.doseDesc;
												$scope.medicationObj.prescriptionDoseUnit =value.doseDesc;
												var sDT =  moment((new Date()),"DD-MM-YYYY").toDate();
			                                    var eDT = moment(value.stopDate,"DD-MM-YYYY").toDate();
			                                    var startDT =  moment(sDT).format('YYYY-MM-DD');
			                                    var endDT = moment(eDT).format('YYYY-MM-DD');
			                                    $scope.medicationObj.medicationStartDate = startDT;
			                                    
			                                    if($scope.medicationObj.durationUnit == "Day") {
													var durationCount = parseInt($scope.medicationObj.durationCount);
													}
												    else if($scope.medicationObj.durationUnit == "Week") {
														var durationCount = parseInt($scope.medicationObj.durationCount)
														$scope.medicationObj.durationCount = durationCount* 7;
													}
													else if($scope.medicationObj.durationUnit == "Month") {
														var durationCount = parseInt($scope.medicationObj.durationCount)
														$scope.medicationObj.durationCount = durationCount* 30;
													}
													else if($scope.medicationObj.durationUnit == "Year") {
														var durationCount = parseInt($scope.medicationObj.durationCount)
														var months = durationCount * 12
														$scope.medicationObj.durationCount = months* 30;
													}
											
											if ($scope.medicationObj.durationCount && $scope.medicationObj.durationUnit && $scope.medicationObj.medicationStartDate) {
												if($scope.medicationObj.durationUnit == "Day") {
													var endDate = moment($scope.medicationObj.medicationStartDate).add($scope.medicationObj.durationCount-1, 'days').format('YYYY-MM-DD');
												}
												else if($scope.medicationObj.durationUnit == "Week") {
													var days = $scope.medicationObj.durationCount * 7
													var endDate = moment($scope.medicationObj.medicationStartDate).add($scope.medicationObj.durationCount, 'days').format('YYYY-MM-DD');
												}
												else if($scope.medicationObj.durationUnit == "Month") {
													var days = $scope.medicationObj.durationCount * 30
													var endDate = moment($scope.medicationObj.medicationStartDate).add($scope.medicationObj.durationCount, 'days').format('YYYY-MM-DD');
												}
												else if($scope.medicationObj.durationUnit == "Year") {
													var days = $scope.medicationObj.durationCount * 365;
													var endDate = moment($scope.medicationObj.medicationStartDate).add($scope.medicationObj.durationCount, 'days').format('YYYY-MM-DD');
												}
												$scope.medicationObj.medicationEndDate = endDate;
												$scope.medicationObj.medicationEndDateFormatted = endDate;

											}                                    
			                                    
											if($scope.medicationDetails.length>0){
			                                    $scope.medicationObj.medicationStartDate = moment($scope.medicationObj.medicationStartDate).format('YYYY-MM-DD');
			                                    angular.forEach($scope.medicationDetails,function(value,index){
			                                        
			                                        if ( $scope.medicationObj.searchItem == value.itemName
			                                                && $scope.medicationObj.medicationStartDate == value.medicationStartDate
			// && $scope.medicationObj.medicationEndDate == value.medicationEndDate
			                                                && $scope.medicationObj.durationCount == value.durationCount
			                                                && $scope.medicationObj.searchItemFre == value.frequencyDesc
			                                                && $scope.medicationObj.doseUnitId == value.doseUnitId
			                                                && $scope.medicationObj.roaId == value.roaId
			                                                && $scope.medicationObj.quantity == value.quantity) {
			                                            
			                                            $scope.medicationExist =true;
			                                        }
			                                    });
			                                }
			                                
			                               if($scope.detailedMedications.length>0){
			                                    $scope.medicationObj.medicationStartDate = moment($scope.medicationObj.medicationStartDate).format('YYYY-MM-DD');
			                                    angular.forEach($scope.detailedMedications,function(value,index){
			                                        
			                                        var sDT =  moment(value.startDate,"DD-MM-YYYY").toDate();
			                                        var eDT = moment(value.endDate,"DD-MM-YYYY").toDate();
			                                        var startDT =  moment(sDT).format('YYYY-MM-DD');
			                                        var endDT = moment(eDT).format('YYYY-MM-DD');
			                                        
			                                        if ( $scope.medicationObj.searchItem == value.itemName
			                                                && $scope.medicationObj.medicationStartDate ==  startDT
			// && $scope.medicationObj.medicationEndDate == endDT
			                                                && $scope.medicationObj.searchItemFre == value.frequencyDesc
			                                                && $scope.medicationObj.dose== value.doseDesc
			                                                && $scope.medicationObj.doseUnitId == value.doseUnitId
			                                                && $scope.medicationObj.quantity == value.orderQty
			                                                && $scope.medicationObj.roaId == value.roaId
			                                                && $scope.medicationObj.isStat == value.statflag) {
			                                            
			                                            $scope.medicationExist =true;
			                                        }
			                                    });
			                                    
			                                }
			                                
			                                
			                                if($scope.medicationExist){
			                                    growl.warning(
			                                            "Medication Already Exist",
			                                            {
			                                                title : 'Warning!'
			                                            });
			                                    $scope.medicationObj.medicationStartDate = new Date();
			                                    // added by chetana
			                                    if ($scope.medicationObj.isDischarge ==true) {
			   										
			   										$scope.medicationObj.isDischarge = "discharge";
			   										 $scope.flagDisch=true;
			   										} else {
			   											$scope.medicationObj.isDischarge = false;
			   										}
			   								 if ($scope.medicationObj.isStat ==true) {
			   										
			   										$scope.medicationObj.isStat = "stat";
			   										$scope.flagStat=true;
			   										} else {
			   											$scope.medicationObj.isStat = false;
			   										}
			                            $rootScope.stopLoader();
			                            return false;
			                            }
			                                
											$scope.medicationDetails.push($scope.medicationObj);
												 
												 $scope.totalSumOfRate = 0;
													angular.forEach($scope.medicationDetails , function(value1,key1){
														$scope.totalSumOfRate+=value1.totalRate;
												   	});
												 console.log("Listtttt",$scope.medicationDetails);
												
				
												 // get formuation dosage list
												angular.forEach($scope.medicationDetails, function(value1, key1){
													 var dataobj = {
																organizationId : $scope.organizationId,
																unitId : $scope.unitId,
																formulationTypeId:value1.formulationTypeId
														};
													 if($scope.medicationObj.isDoseCalculationApplicable == true){
															$scope.medicationObj.prescribeDose=($scope.medicationObj.prescribeDose !=undefined && $scope.medicationObj.prescribeDose!= null && $scope.medicationObj.prescribeDose != "") ? $scope.medicationObj.prescribeDose: "";

														}else{
															$scope.medicationObj.prescribeDose="";
														}
														var URI = BASE_URL + ROOT_URL+ "/api/global/getFormulationTypeToDosageMapperListByFormulationTypeId";
														GenericService.serviceAction("POST", URI, dataobj).then(function(response) {
																	$rootScope.stopLoader();
																	$scope.medicationObj.prescribedDosageUnitList=[];
																	$scope.counterFormulation=1;
																	$scope.medicationObj.formulationCounterIndex="";
																	angular.forEach(response.data.listObject,function(value,index){
																		var unitObj={};
																		unitObj.formulationDosageFromUnit=value.fromConversionUnit;
																		unitObj.formulationDosageFromUnitId=value.fromConversionFactorUnit;
																		unitObj.formulationDosageToUnit=value.toConversionUnit;
																		unitObj.formulationDosageToUnitId=value.toConversionFactorUnit;
																		unitObj.counterIndex=$scope.counterFormulation++;
																		unitObj.formulationDosageFromConversionValue=value.fromConversionFactor;
																		unitObj.formulationDosageToConversionValue=value.toConversionFactor;
																		unitObj.formulationFactorId=value.factorId;
																		unitObj.formulationDosage=value.dosageForm;
																		unitObj.formulationDosageMappId=value.formulationDosageMappId;
																		value1.prescribedDosageUnitList.push(unitObj);
																	});
																	
																	angular.forEach(response.data.listObject,function(value,index){
																		var unitObj={};
																		if(value.factorId==1){
																			unitObj.formulationDosageFromUnit=value.toConversionUnit;
																			unitObj.formulationDosageFromUnitId=value.toConversionFactorUnit;
																			unitObj.formulationDosageToUnit=value.fromConversionUnit;
																			unitObj.formulationDosageToUnitId=value.fromConversionFactorUnit;
																			unitObj.counterIndex=$scope.counterFormulation++;
																			unitObj.formulationDosageFromConversionValue=value.toConversionFactor;
																			unitObj.formulationDosageToConversionValue=value.fromConversionFactor;
																			unitObj.formulationFactorId=value.factorId;
																			unitObj.formulationDosage=value.dosageForm;
																			unitObj.formulationDosageMappId=value.formulationDosageMappId;
																			// value1.formulationDosageMappId=value.formulationDosageMappId;
																			value1.prescribedDosageUnitList.push(unitObj);
																		}
																	});
																	var list=[];
																	if(value1.prescribedDosageUnitList.length>0){
																		 list=$.grep(value1.prescribedDosageUnitList, function (obj) {
																			 return value1.formulationDosageMappId === obj.formulationDosageMappId;
																	    });
																		if(list.length>0){	
																			value1.formulationCounterIndex=list[0].counterIndex;
																		}
																	}
																	$scope.itemSelecteds = [];
														});
														
												});
												
												
											}
											});
											$scope.initMedicationObj();
											$scope.itemSelectedList.items=[];
			// }
											}else{
												growl.warning("Please select medication ",
														{
															title : 'Warning!'
														});
												
												$rootScope.stopLoader();	
											}
											
										}
								   	
				                         $scope.removeFavouriteMedication = function() {
												if($scope.itemSelectedList.items.length > 0){

													angular.forEach($scope.itemSelectedList.items, function(value, key){
													$scope.medicationObjremove = {};
													$scope.medicationObjremove.doctorFavoriteId=value.doctorFavoriteId;
													$scope.medicationObjremove.favorite_status =true;
													$scope.updateMedicationFavouriteStatus($scope.medicationObjremove);
													$scope.itemSelecteds = [];
													});
													$scope.itemSelectedList.items =[];
													}else{
														growl.warning("Please select medication ",
																{
																	title : 'Warning!'
																});
														
														$rootScope.stopLoader();	
													}
												}
								        $scope.getFavoritesMedication = function()
								        {
								        	var data ={
								        			doctorId :$scope.encounterDetailsObj.doctorId,
								        			organizationId : $scope.organizationId,
								        			unitId : $scope.unitId
								        	};
								        	
								    		var URI = BASE_URL + ROOT_URL + GETFAVOURITEMEDICATION;
								    		
											GenericServiceParamHeader.serviceAction(METHOD_POST, URI, 
													data, NOTIFICATION_MSG_STATUS_FALSE)
													.then(function(response) {
														if(response.data.status == "success"){
															$scope.favouriteMedications = response.data.listObject;
															if($scope.favouriteMedications!=null && $scope.favouriteMedications.length>0){
																angular.forEach($scope.favouriteMedications,function(value,index){
																	if(value.strengthDesc == null ){
																		value.strengthDesc = "";
																	}
																	if(value.doseDesc == null){
																		value.doseDesc = "";
																	}
																});
															}
															console.log("$scope.fffffffffffffffaaaaaaaaaaaaavvvvvvvvvv",$scope.favouriteMedications);
														}
											});
								        }
								        $scope.getFavoritesMedication();
								        
								        
								        $rootScope.loginpage = true;
										$scope.saveBtnFlag = false;
										$scope.updateBtnFlag = true;

										// for popup
										$scope.showModal = false;
										$scope.buttonClicked = "";
											
											var dataDose = {
													organizationId:$scope.organizationId
											}
											
											
								        
								        $scope.updateFlag = false;
								        $scope.saveFlag = true;
								        
										$scope.addEmptyRow = function(idx) {
											 $scope.medicationExist =false;
											 
											 var isDischarge=false;
											 
											 if($scope.medicationDetails.length>0){
											 if($scope.medicationDetails[$scope.medicationDetails.length-1].isDischarge==false && isDischarge==true){
												 growl.warning(
															'You can add only normal medication',
															{
																title : 'Warning!'
															});
												return false;
											 }
											 
											 if($scope.medicationDetails[$scope.medicationDetails.length-1].isDischarge==true && isDischarge==false){
												 growl.warning(
															'You can add only discharge medication',
															{
																title : 'Warning!'
															});
												return false;
											 }
											
											 }
											 
											console.log("$scope.medicationDetails", $scope.medicationDetails);
											if ( ($scope.medicationObj.searchItem != undefined || $scope.medicationObj.medicationName != ""  &&  $scope.medicationObj.searchItem != "")
													&& $scope.medicationObj.medicationEndDate != undefined
													&& $scope.medicationObj.medicationEndDate != ''
													&& $scope.medicationObj.durationCount != undefined
													&& $scope.medicationObj.durationCount != ''
													&& $scope.medicationObj.quantity != 0
													&& $scope.medicationObj.quantity != undefined
													&& $scope.medicationObj.searchItemFre != ''
													) {
												
												   if($scope.medicationObj.searchItem != undefined &&  $scope.medicationObj.searchItem != ""){
													   var Arr = $scope.medicationObj.searchItem.split("|");
													   $scope.medicationObj.itemName = Arr[0];
													   if( $scope.medicationObj.roaId != '' && $scope.medicationObj.roaId != null){
														   var roaDesc = $
															.grep(
																	$scope.roa_options,
																	function(disCat) {
																		return disCat.roa_id == $scope.medicationObj.roaId;
																	})[0].roa_details;

													       $scope.medicationObj.roaDesc = roaDesc;  
													   }
														
														
												   }else{
													   
													   var Arr = $scope.medicationObj.medicationName.split("|");
													   $scope.medicationObj.searchItem = Arr[0];
													   $scope.medicationObj.itemName = $scope.medicationObj.medicationName; 
													  
													   $scope.medicationObj.medicationName = $scope.medicationObj.medicationName;
													   if( $scope.medicationObj.roaId != '' && $scope.medicationObj.roaId != null){
														var roaDesc = $
																.grep(
																		$scope.roa_options,
																		function(disCat) {
																			return disCat.roa_id == $scope.medicationObj.roaId;
																		})[0].roa_details;

														$scope.medicationObj.roaDesc = roaDesc;
													   }
												   }
												   
											var strengthDesc = "";	   
											  if( $scope.medicationObj.doseUnitId!=undefined &&  $scope.medicationObj.doseUnitId!=null &&  $scope.medicationObj.doseUnitId!=""){
											 strengthDesc = $.grep( $scope.doseList, function(disCat) {
																return disCat.id == $scope.medicationObj.doseUnitId;
															})[0].unit;
											  }
											$scope.medicationObj.strengthDesc = strengthDesc;
											$scope.medicationObj.frequencyDesc = $scope.medicationObj.searchItemFre;
											$scope.medicationObj.nonFormularyAdvice = $scope.medicationObj.nonFormularyAdvice;
											$scope.medicationObj.medicationStartDate = moment($scope.medicationObj.medicationStartDate).format('YYYY-MM-DD');
											$scope.medicationObj.favorite_status = $scope.medicationObj.favorite_status;
											 $scope.medicationObj.rate = $scope.medicationObj.rate;
											 if($scope.medicationObj.rate  == undefined){
			                                     $scope.medicationObj.rate = 0;
			                                 }
											 $scope.medicationObj.totalRate=0;
											 $scope.medicationObj.totalRate=$scope.medicationObj.rate * $scope.medicationObj.quantity;
											 
											if($scope.medicationObj.doctorFavoriteId != undefined){
											$scope.medicationObj.doctorFavoriteId=$scope.medicationObj.doctorFavoriteId
											}
											if($scope.medicationObj.isInstruction == 'Y') {
												$scope.medicationObj.instructionId = null;
												$scope.medicationObj.instructionDesc = null;
											}
											
											if ($scope.medicationObj.instructionId != null && $scope.medicationObj.instructionId!=""
												&& $scope.medicationObj.instructionId!=undefined) {
												var instructionDesc = $.grep($scope.medicationInstructions,
																function(disCat) {
																	return disCat.instructionId == $scope.medicationObj.instructionId;
																})[0].instructionDesc;

												$scope.medicationObj.instructionDesc = instructionDesc;
											} else if($scope.medicationObj.isInstruction == 'Y') {
												$scope.medicationObj.instructionDesc = $scope.medicationObj.isInstructionText;
											}else {
												$scope.medicationObj.instructionDesc = "No instruction";
											}
												$scope.medicationObj.priorityId =2;
											//added by chetana
											 if ($scope.medicationObj.isDischarge =="discharge") {
													
													$scope.medicationObj.isDischarge = true;
													} else {
														$scope.medicationObj.isDischarge = false;
													}
											 if ($scope.medicationObj.isStat =="stat") {
													
													$scope.medicationObj.isStat = true;
													} else {
														$scope.medicationObj.isStat = false;
													}
											
											$scope.medicationObj.frequencyId = $scope.medicationObj.frequencyId ;
											$scope.medicationObj.noOfAdministrationTime =$scope.freQua ;
											$scope.medicationObj.durationCount =$scope.medicationObj.durationCount ;
											
											$scope.medicationDetails.push($scope.medicationObj);
											
											console.log("test",$scope.medicationDetails);
											
										
											}else{
												growl.warning(
														"Please enter all mandatory fields.",
														{
															title : 'Warning!'
														});
												return false;
											}
											
//											angular.forEach($scope.medicationDetails , function(value,key){
//												
//												$scope.medicationObj.frequencyId = value.frequencyId;
//												$scope.medicationObj.frequencyDesc = value.frequencyDesc;
//												$scope.medicationObj.searchItemFre = value.frequencyDesc;
//												
//												$scope.item={
//														frequency_id:value.frequencyId,
//														frequency:value.frequencyDesc,
//														noOfAdministrationTime:value.noOfAdministrationTime
//												}
//												
//												$scope.model=value.frequencyDesc;
//												$scope.label=value.frequencyDesc;
//												$scope.medicationObj.favorite_status =false;
//												$scope.addFrequencyItemList($scope.item, $scope.model, $scope.label);
//												
//												$scope.medicationObj.instructionDesc = value.instructionDesc;
//												$scope.medicationObj.instructionId = value.instructionId;
////												$scope.medicationObj.medicationStartDate = value.startDate;
////												$scope.medicationObj.medicationEndDate = value.medicationEndDate;
//												$scope.medicationObj.durationCount = value.durationCount;
//												$scope.medicationObj.durationUnit = value.durationUnit;
//												$scope.medicationObj.medicationEndDateFormatted = value.medicationEndDateFormatted;
//												$scope.medicationObj.quantity = value.quantity;
////												$scope.medicationObj.medicationStartDate = new Date();
//												
//											});
											$scope.totalSumOfRate = 0;
											angular.forEach($scope.medicationDetails , function(value1,key1){
												$scope.totalSumOfRate+=value1.totalRate;
										   	});
										   
											$timeout(function() {
												$scope.saveMedication();
											}, 2000);
										}

										$scope.instructionList = function(){
											var commonObj = {
								    				isMedication : 'Y',
													organizationId : $scope.organizationId
												}

											var URI = BASE_URL + ROOT_URL
													+ GETMEDICATIONINSTRUCTIONS;
										
											BillingGenericService.serviceAction(METHOD_POST, URI,commonObj,
												NOTIFICATION_MSG_STATUS_FALSE).then(
												function(response) {
												console.log(response);
													// $rootScope.startLoader();
												if (response.data.status == "success")
													$scope.medicationInstructions = response.data.listObject;
											});
									
										}
										$scope.instructionList();
							        
								 
							        
							    	$scope.continueActiveMedictaion = function() {
										$scope.itemsMed=[];
										
										angular.forEach($scope.activeMedications,function(value,index){
											if(value.checkTrue == 'Y'){
												var newArr={};
												newArr.value=value;
												$scope.itemsMed.push(value);
											}
											
											 $scope.itemSelectedListActiveMed.itemsMed=$scope.itemsMed;
											 console.log("selectedActiveMedication",$scope.itemSelectedListActiveMed.itemsMed);
										});
										
										angular.forEach($scope.continuedMedicationHistoryList,function(value,index){
											if(value.checkTrue == 'Y'){
												var newArr={};
												newArr.value=value;
												$scope.itemsMed.push(value);
											}
											 $scope.itemSelectedListActiveMed.itemsMed=$scope.itemsMed;
											 console.log("selectedActiveMedication",$scope.itemSelectedListActiveMed.itemsMed);
										});
										
										var selectedFavMed =  $scope.itemSelectedListActiveMed.itemsMed;
										console.log("selectedFavMed[i].medicationName",selectedFavMed);
										
										if(selectedFavMed.length > 0 ){
											
											
											 angular.forEach(selectedFavMed, function(value, index) 
											    		{
									    		 
									    			    $scope.medicationObj.itemId = value.itemId;
									    			    $scope.medicationObj.prescriptionDetailsid = value.prescriptionDetailsid;
									    			    $scope.medicationObj.doseUnitId = value.doseUnitId;
									    			    if(value.doseDesc != null){
									    			    	$scope.medicationObj.dose = value.doseDesc;
									    			    }
									    			    else{
									    			    	$scope.medicationObj.dose = value.doseId;
									    			    }
									    			    
									    			    $scope.medicationObj.frequencyId = value.frequencyId;
									    			    $scope.medicationObj.formulationTypeId= value.formulationTypeId;
									    			    $scope.medicationObj.medicationStartDate = moment(new Date()).format('YYYY-MM-DD');
														$scope.medicationObj.itemName = value.itemName;
														$scope.medicationObj.roaDesc =value.roaDesc;
														
														$scope.medicationObj.frequencyDesc =value.frequencyDesc;
														$scope.medicationObj.priorityId =1;
														$scope.medicationObj.genericName = value.genericName;
														$scope.medicationObj.searchGeneric = value.genericName;
														$scope.medicationObj.genericId=value.genericId;
														$scope.medicationObj.searchItem = value.itemName;
														$scope.medicationObj.medicationName = value.itemName;
												        $scope.medicationObj.searchItemFre = value.frequencyDesc;
												        $scope.medicationObj.instructionId = value.instructionId;
												        $scope.medicationObj.isInstruction = 'N';
												        $scope.medicationObj.prescribedDosageUnitList = [];
												        $scope.medicationObj.roaList = [];
												        
												        if(value.instructionId != null){
												        	
												        	$scope.medicationObj.isInstruction='N',
												        	$scope.medicationObj.instructionDesc=value.instructionDesc,
												        	$scope.medicationObj.instructionId=value.instructionId
												         	 if($scope.medicationObj.instructionId != null){
																var instructionDesc = $
																		.grep(
																				$scope.medicationInstructions,
																				function(disCat) {
																					return disCat.instructionId == $scope.medicationObj.instructionId;
																				})[0].instructionDesc;

																$scope.medicationObj.instructionDesc = instructionDesc;
								                                }else{
								                                	$scope.medicationObj.instructionDesc = "No instruction";
								                                }
														 
							  							}else{
							  								$scope.medicationObj.isInstruction='Y',
							  								$scope.medicationObj.isInstructionText=value.instructionText,
							  								$scope.medicationObj.instructionId=''
							  							};
							  							if(value.instructionDesc == null && value.instructionText == null){
							  								$scope.medicationObj.isInstruction='N',
							  								$scope.medicationObj.instructionDesc=null,
							  								$scope.medicationObj.instructionText=null
							  							}
												        $scope.medicationObj.prescribedDosageUnitList = [];
												        $scope.medicationObj.nonFormularyAdvice=value.isNonFormulary;
												        if(value.isNonFormulary =='N'){
												        	 var URI = BASE_URL + ROOT_URL + "/api/opd/getRoaList" + "?unitId=" + 
															$scope.unitId + "&orgId=" + $scope.organizationId + "&formulationTypeId=" + $scope.medicationObj.formulationTypeId+ "&genericId=" + $scope.medicationObj.genericId+ "&patientId=" +$scope.encounterDetailsObj.patientId;
														
															GenericServiceParamHeader.serviceAction(METHOD_GET, URI).then(function(response) {
															if(response.data.status == "success")
															{
																var idx = $scope.medicationDetails.findIndex(item => item.prescriptionDetailsid == value.prescriptionDetailsid);
																$scope.medicationDetails[idx].roaList = response.data.listObject;
															}});
												        }else{
												        	        $scope.getROAForNonFormulary();
																	$scope.medicationObj.searchItem ="";
																	$scope.medicationObj.medicationName = value.itemName;
												        }
															
															
															//get formuation dosage list
															var dataobj = {
																	organizationId : $scope.organizationId,
																	unitId : $scope.unitId,
																	formulationTypeId:$scope.medicationObj.formulationTypeId
															};
															 $scope.medicationObj.prescribeDose="";
															 if(value.doseUnit == undefined){
																 $scope.medicationObj.strengthDesc =value.strengthDesc;
																	$scope.medicationObj.itemStrength=$scope.medicationObj.dose+" "+$scope.medicationObj.strengthDesc;	
																}else{
																	$scope.medicationObj.doseUnit =value.doseUnit;
																	$scope.medicationObj.itemStrength=$scope.medicationObj.dose+" "+$scope.medicationObj.doseUnit;
																}
															var URI = BASE_URL + ROOT_URL+ "/api/global/getFormulationTypeToDosageMapperListByFormulationTypeId";
															GenericService.serviceAction("POST", URI, dataobj).then(function(response) {
																		$rootScope.stopLoader();
																		 $scope.medicationObj.prescribedDosageUnitList=[];
																		$scope.counterFormulation=1;
																		 $scope.medicationObj.formulationCounterIndex="";
																		
																		var idx = $scope.medicationDetails.findIndex(item => item.prescriptionDetailsid == value.prescriptionDetailsid);
																		angular.forEach(response.data.listObject,function(value,index){
																			var unitObj={};
																			unitObj.formulationDosageFromUnit=value.fromConversionUnit;
																			unitObj.formulationDosageFromUnitId=value.fromConversionFactorUnit;
																			unitObj.formulationDosageToUnit=value.toConversionUnit;
																			unitObj.formulationDosageToUnitId=value.toConversionFactorUnit;
																			unitObj.counterIndex=$scope.counterFormulation++;
																			unitObj.formulationDosageFromConversionValue=value.fromConversionFactor;
																			unitObj.formulationDosageToConversionValue=value.toConversionFactor;
																			unitObj.formulationFactorId=value.factorId;
																			unitObj.formulationDosage=value.dosageForm;
																			unitObj.formulationDosageMappId=value.formulationDosageMappId;
																			$scope.medicationDetails[idx].prescribedDosageUnitList.push(unitObj);
																		});
																		
																		angular.forEach(response.data.listObject,function(value,index){
																			var unitObj={};
																			if(value.factorId==1){
																				unitObj.formulationDosageFromUnit=value.toConversionUnit;
																				unitObj.formulationDosageFromUnitId=value.toConversionFactorUnit;
																				unitObj.formulationDosageToUnit=value.fromConversionUnit;
																				unitObj.formulationDosageToUnitId=value.fromConversionFactorUnit;
																				unitObj.counterIndex=$scope.counterFormulation++;
																				unitObj.formulationDosageFromConversionValue=value.toConversionFactor;
																				unitObj.formulationDosageToConversionValue=value.fromConversionFactor;
																				unitObj.formulationFactorId=value.factorId;
																				unitObj.formulationDosage=value.dosageForm;
																				unitObj.formulationDosageMappId=value.formulationDosageMappId;
																				$scope.medicationDetails[idx].prescribedDosageUnitList.push(unitObj);
																			}
																		});
																		var list=[];
																		if( $scope.medicationDetails[idx].prescribedDosageUnitList.length>0 &&  $scope.medicationDetails[idx].prescribedDosageUnitList[0].formulationFactorId==1){
																			 list=$.grep($scope.medicationDetails[idx].prescribedDosageUnitList.prescribedDosageUnitList, function (obj) {
																		        return $scope.medicationDetails[idx].prescribedDosageUnitList.doseUnitId === obj.formulationDosageFromUnitId;
																		    });
																			if(list.length>0){	
																				$scope.medicationDetails[idx].prescribedDosageUnitList.formulationCounterIndex=list[0].counterIndex;
																			}
																		}
																		
																		
															});
																		
																	
																	$scope.medicationObj.roaId = value.roaId;
//																	$scope.medicationObj.medicationStartDate = new Date();
																	$scope.medicationObj.medicationStartDate =  moment(value.endDate,"DD-MM-YYYY").add(1, 'days').toDate();
																	$scope.medicationObj.isGeneric='N' ;
																	$scope.medicationObj.frequencyId=value.frequencyId;
																	$scope.medicationObj.frequencyDesc=value.frequencyDesc;
																	$scope.medicationObj.noOfAdministrationTime=value.noOfAdministrationTime;
																	$scope.medicationObj.durationUnit='Day';
														    		$scope.freQua = 0;
														    		$scope.freQua = value.noOfAdministrationTime;
														    		var dur = 0;
														    		$scope.medicationObj.quantity = value.noOfAdministrationTime * dur;
														    		var checkFlagValidation = false;
																    $scope.medicationObj.checkFlagValidation = checkFlagValidation;
																	var found = $scope.medicationDetails.find(ar=>ar.itemId==value.itemMasterId);
																	   if(found==undefined)
																		   {
																		      $scope.medicationDetails.push($scope.medicationObj);
																		      console.log("testOrderSet" ,$scope.medicationDetails);
																		   }
																	$scope.medicationObj={};
																	if($scope.medicationDetails[0].itemId == ""){
																		 $scope.medicationDetails.splice(0, 1);	
																	}		
														
											    	 });
										} else{
											growl.warning("Please select medication ",
													{
														title : 'Warning!'
													});
											
											$rootScope.stopLoader();	
										}
										
									}
							    	
							    	 $scope.getAllActiveMedication = function()
							         {
							         	$scope.activeMedications = [];
							         	var patientId = $scope.encounterDetailsObj.patientId;
							     		var dateObj = moment(new Date()).format('YYYY-MM-DD');
							     		/*
							 			 * var date = ("0" + dateObj.getDate()).slice(-2) + "-" +
							 			 * ("0"+(dateObj.getMonth()+1)).slice(-2) +"-" +
							 			 * dateObj.getFullYear();
							 			 */
							     		
							     		var URI = BASE_URL + ROOT_URL + GETACTIVEMEDICATION + "?patientId=" +
							     		  			patientId + "&date=" + dateObj+ "&unitId="+ $scope.unitId
							     		GenericServiceParamHeader.serviceAction(METHOD_GET, URI)
							     		.then(
							     			function(response) {
							     				if (response.data.status == "success") {
							     					$scope.activeMedications = response.data.listObject;
							     					angular.forEach($scope.activeMedications,function(value,key){
							     						value.checkTrue='N';
							     						if(value.strengthDesc == null){
							     							value.strengthDesc  = "";
							     						}
							     						
							     					});
							     				}
							     			});
							         }
							         $scope.getAllActiveMedication();
							         //Continue active medication
							         
							         
							         
							         $scope.itemSelectedListActiveMed = {
							 				itemsMed: [],
							 				selectAllActMedi:'N'
							 			  };
							         
							         $scope.selectAllActiveMedic=function(selectAllActMedi){
							 			if(selectAllActMedi == 'Y'){
							 				angular.forEach($scope.activeMedications,function(value,index){
							 					value.checkTrue='Y';
							 				});
							 				angular.forEach($scope.continuedMedicationHistoryList,function(value,index){
							 					value.checkTrue='Y';
							 				});
							 				}else{
							 					angular.forEach($scope.activeMedications,function(value,index){
							 						value.checkTrue='N';
							 					});
							 					angular.forEach($scope.continuedMedicationHistoryList,function(value,index){
							 						value.checkTrue='N';
							 					});
							 				}
							 		}
							         
							      // Advice Tab => Investigation By Default selected
										if ($scope.firstTabLevel == 9 && newTab == 1) {
											document.getElementById("searchServiceIdDiagnostic").focus();
											$scope.getInvestigationList();
											
											var commonObj = {
													unitId : $scope.unitId,
													organizationId : $scope.organizationId
												};
								    		// For Get Active Reason List
											var URI = BASE_URL + ROOT_URL + GETACTIVEREASONLIST;
											GenericService.serviceAction("POST", URI, commonObj)
													.then( function(response) {
																$scope.reasonList = [];

																if (response.data.status == "success") {
																	$scope.reasonList = response.data.listObject;
																	console.log("reasonList",$scope.reasonList );
																}
															});
											$scope.getInvestigationDetails = function(invObj)
											{
												$rootScope.startLoader();
												$('#panelDetailsModal').modal('show');
												var headers = {
														'panelId' : invObj.panelId,
														'orderDetailsId' : invObj.orderDetailsId,
														'Content-Type' : 'application/json'
													};
									    		var URI = BASE_URL + ROOT_URL + GETCURRENTINVESTIGATIONDETAILS ;
									    		GenericServiceParamHeader.serviceAction(METHOD_GET, URI,data,NOTIFICATION_MSG_STATUS_FALSE, paramObj,
														headers)
									    		.then(
									    			function(response) {
									    				if (response.data.status == "success") 
									    				{
									    					$scope.inveDetails = response.data.listObject;
									    					$rootScope.stopLoader();
									    				}
									    				else if(response.data.status == "error")
									    					{
									    					$rootScope.stopLoader();
									    					}
									    			});
											}
											
										}
										if ($scope.firstTabLevel == 9 && newTab == 2) {
											// get Medication List
											 $scope.getDetailedMedication();
											 $scope.flagCheckActiveMed=false;
											 $scope.medicationObj = {
														itemId : "",
														searchItem :"",
														searchItemFre:"",
														favorite_status : false,
														medicationStartDate : new Date(),
														roaId : '',
														dose : '',
														doseUnitId : '',
														durationCount : "",
														durationUnit:'Day',
														instructionId : '',
														medicationEndDate : '',
														frequencyId : '',
														quantity : '',
														uomTypeId : '',
														conversions : '',
														strength : '',
														priorityId : 2,
														isStat : false,
														priorityCode: 'NR',
														isGeneric:'N',
														isInstruction:'N',
														nonFormularyAdvice:'N',
														rate:'',
														prescribedDosageUnitList:[]

													};
											 
										}
										if ($scope.firstTabLevel == 9 && newTab == 3) {
											// get instruction List
											 $scope.getInstructions();
										}
										if ($scope.firstTabLevel == 9 && newTab == 4) {
											// Consult Order
											var dataOrganization = {
													organizationId : $scope.organizationId
												};
												  var URI = BASE_URL+ROOT_URL+UNITLIST;
												  GenericService.serviceAction("POST",URI,dataOrganization).then(function(response){
													  
														$scope.unitList = [];
														if (response.data.status == "success") {
															$scope.unitList = response.data.listObject;
														}
												 });
												  $scope.consultOrderList();
												  $scope.onChangeSearchType($scope.searchType);
										}
										
										 $scope.getDetailedMedication = function()
									        {
											 $scope.detailedMedications = [];
									        	/*
												 * var patientId = $scope.encounterDetailsObj.patientId; var
												 * doctorId = $scope.encounterDetailsObj.doctorId; var dateObj =
												 * moment(new Date()).format('YYYY-MM-DD');
												 */
											 var data ={};
											 var headerObj = {
														unitId : $scope.unitId,
														organizationId : $scope.organizationId
													};
												
												var paramObjForPer = {
														patientId:$scope.encounterDetailsObj.patientId,
														encounterId : $scope.encounterDetailsObj.encounterId,
														doctorId : $scope.encounterDetailsObj.doctorId ? $scope.encounterDetailsObj.doctorId :0 ,
												};
									    		/** ***************get detailed medication by DoctorId******** */
									    		/*
												 * var URI = BASE_URL + ROOT_URL + GETDETAILEDMEDICATION +
												 * "?patientId=" + patientId + "&date=" + dateObj
												 */
									    		var URI = BASE_URL + ROOT_URL + "/api/opd/getMedicationDetailsByDoctorId" ;
									      		
									    		GenericServiceParamHeader.serviceAction(METHOD_GET, URI, data,
														NOTIFICATION_MSG_STATUS_FALSE,paramObjForPer,headerObj)
									    		.then(
									    			function(response) {
									    				if (response.data.status == "success") {
									    					$scope.detailedMedications = response.data.listObject;
									    					if($scope.detailedMedications!=null && $scope.detailedMedications.length>0){
																angular.forEach($scope.detailedMedications,function(value,index){
																	value.searchItemFre = value.frequencyDesc; 
																	if(value.durationUnit ==  undefined || value.durationUnit == null){
																		value.durationUnit = 'Day';
																	}
																	if(value.statflag == true){
																		value.isStat="stat";
																	}
																	value.medicationEndDateFormatted = moment(value.endDate, "DD-MM-YYYY").toDate();
																	value.medicationStartDate = moment(value.startDate, "DD-MM-YYYY").toDate();
																	if(value.medicationStartDate == undefined || value.medicationStartDate == null){
																		value.medicationStartDate = moment(new Date()).toDate();
																	}
																	if(value.formulationDosageMappId != undefined && value.formulationDosageMappId != null){
																		value.formulationCounterIndex = value.formulationDosageMappId.toString();
																	}
																	
																});
															}
															$scope.roaList();
									    					console.log("ppppppppppp", $scope.detailedMedications);
									    				}
									    			});
									        }
										 $scope.getDetailedMedication();

										$scope.roaList = function(){
											if($scope.detailedMedications!=null && $scope.detailedMedications.length>0){
												angular.forEach($scope.detailedMedications,function(value,index){
													if(value.doseDesc == null){
														value.doseDesc = "";
													}
													if(value.strengthDesc == null){
														value.strengthDesc = "";
													}
													var URI = BASE_URL + ROOT_URL + "/api/opd/getRoaList" + "?unitId=" + 
													$scope.unitId + "&orgId=" + $scope.organizationId + "&formulationTypeId=" + 
													value.formulationTypeId+ "&genericId=" + 
													value.genericId+ "&patientId=" +$scope.encounterDetailsObj.patientId;
												
													GenericServiceParamHeader.serviceAction(METHOD_GET, URI).then(function(response) {
														if(response.data.status == "success")
														{
															// var idx = $scope.medicationDetails.findIndex(item => item.itemId == value.itemMasterId);
															$scope.roa_options = response.data.listObject;
															value.roa_options = $scope.roa_options;
															var dataobj = {
																organizationId : $scope.organizationId,
																unitId : $scope.unitId,
																formulationTypeId: value.formulationTypeId
															};
															var URI = BASE_URL + ROOT_URL+ "/api/global/getFormulationTypeToDosageMapperListByFormulationTypeId";
															GenericService.serviceAction("POST", URI, dataobj).then(function(response) {
																$rootScope.stopLoader();
																$scope.medicationObj.prescribedDosageUnitList=[];
																$scope.counterFormulation=1;
																$scope.medicationObj.formulationCounterIndex="";
																angular.forEach(response.data.listObject,function(value1,index){
																	var unitObj={};
																	unitObj.formulationDosageFromUnit=value1.fromConversionUnit;
																	unitObj.formulationDosageFromUnitId=value1.fromConversionFactorUnit;
																	unitObj.formulationDosageToUnit=value1.toConversionUnit;
																	unitObj.formulationDosageToUnitId=value1.toConversionFactorUnit;
																	unitObj.counterIndex=$scope.counterFormulation++;
																	unitObj.formulationDosageFromConversionValue=value1.fromConversionFactor;
																	unitObj.formulationDosageToConversionValue=value1.toConversionFactor;
																	unitObj.formulationFactorId=value1.factorId;
																	unitObj.formulationDosage=value1.dosageForm;
																	unitObj.formulationDosageMappId=value1.formulationDosageMappId;
																	$scope.medicationObj.prescribedDosageUnitList.push(unitObj);
																	value.prescribedDosageUnitList = $scope.medicationObj.prescribedDosageUnitList;
																});
																					
																angular.forEach(response.data.listObject,function(value2,index){
																	var unitObj={};
																	if(value.factorId==1){
																		unitObj.formulationDosageFromUnit=value2.toConversionUnit;
																		unitObj.formulationDosageFromUnitId=value2.toConversionFactorUnit;
																		unitObj.formulationDosageToUnit=value2.fromConversionUnit;
																		unitObj.formulationDosageToUnitId=value2.fromConversionFactorUnit;
																		unitObj.counterIndex=$scope.counterFormulation++;
																		unitObj.formulationDosageFromConversionValue=value2.toConversionFactor;
																		unitObj.formulationDosageToConversionValue=value2.fromConversionFactor;
																		unitObj.formulationFactorId=value2.factorId;
																		unitObj.formulationDosage=value2.dosageForm;
																		unitObj.formulationDosageMappId=value2.formulationDosageMappId;
																		$scope.medicationObj.prescribedDosageUnitList.push(unitObj);
																		value.prescribedDosageUnitList = $scope.medicationObj.prescribedDosageUnitList;
																	}
																});
																var list=[];
																if($scope.medicationObj.prescribedDosageUnitList.length>0 && $scope.medicationObj.prescribedDosageUnitList[0].formulationFactorId==1){
																	list=$.grep($scope.medicationObj.prescribedDosageUnitList, function (obj) {
																		return  $scope.prescriptionDtlObj.prescribeDoseUnitId === obj.formulationDosageFromUnitId;
																	});
																	if(list.length>0){	
																		$scope.medicationObj.formulationCounterIndex=list[0].counterIndex;
																	}
																}
																					
																if($scope.medicationObj.prescribedDosageUnitList.length>0 && $scope.medicationObj.prescribedDosageUnitList[0].formulationFactorId==2){
																	list=$.grep($scope.medicationObj.prescribedDosageUnitList, function (obj) {
																		return $scope.prescriptionDtlObj.dosageForm === obj.formulationDosage;
																	});
																	if(list.length>0){	
																		$scope.medicationObj.formulationCounterIndex=list[0].counterIndex;
																	}
																}
															});
														}
													});
												});
											}
										}
										$scope.onDurationCountChange = function(medicationObj, index) {
											$scope.calculateEndDate(medicationObj, index);
										}
										 
							    	//delete medication
									$scope.deleteFunction = function(info){
										$scope.prescriptionId=info.prescriptionId;
										$scope.prescriptionDetailsid1=info.prescriptionDetailsid;
									}
									$scope.deleteMedicationPendingStatus = function(){
										$rootScope.startLoader();
										try{
//											$rootScope.startLoader();
											var date = new Date();
											$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
											var data={
												    updatedBy:$scope.userId,
													updatedDate:$scope.dt,
													prescriptionId:$scope.prescriptionId,
													organizationId:$scope.organizationId,
													justificationStop:$scope.justificationDeleteMedi,
													prescriptionDetailsid:$scope.prescriptionDetailsid1,
													
											};
											
//											if($scope.justificationDeleteMedi != null && $scope.justificationDeleteMedi !="" && $scope.justificationDeleteMedi != undefined){
											var URI = BASE_URL + ROOT_URL + "/api/opd/saveJustificationForMedicationDelete";
											GenericService.serviceAction("POST",URI,data).then(function(response){
												if (response.data.status == 'success')
												{
													$rootScope.stopLoader();
													growl.success("Deleted Successfully..!!",
															{
																title : 'Success!'
															});
													$scope.getAllActiveMedication();
													$scope.getDetailedMedication();
													$scope.getMedicationListForMedicalHistory();
													$scope.justificationDeleteMedi="";
//													$scope.$broadcast('show-errors-reset');
												} else if(response.data.status == 'error')
												{
													$rootScope.stopLoader();
													growl.warning(response.data.message,
															{
																title : 'Warning!'
															});
													$scope.getAllActiveMedication();
													$scope.getDetailedMedication();
													$scope.getMedicationListForMedicalHistory();
													$scope.justificationDeleteMedi="";
													
												}
												
											});

										}catch(e){
											console.log("Exception",e);
										}
										 $scope.removeMedicationOrdderset = function(){
												
												$scope.medication.checkFlagValidation=false;
												$scope.removeMedication($scope.medication);
											}}
									
							    	$scope.removeMedication = function(medication,index) {
										
										if(medication.checkFlagValidation != true){
										if($scope.medicationDetails.length == 1){
											$scope.medicationDetails[index].searchItem="",
											$scope.medicationDetails[index].medicationName="",
											$scope.medicationDetails[index].genericName='',
											$scope.medicationDetails[index].searchGeneric='',
											$scope.medicationDetails[index].roaId="",
											$scope.medicationDetails[index].itemId="",
											$scope.medicationDetails[index].dose="",
											$scope.medicationDetails[index].searchItemFre="",
											$scope.medicationDetails[index].doseUnitId="",
											$scope.medicationDetails[index].isInstructionText="",
											$scope.medicationDetails[index].durationUnit='Day',
											$scope.medicationDetails[index].medicationStartDate=new Date(),
											$scope.medicationDetails[index].medicationEndDate="",
											$scope.medicationDetails[index].medicationEndDateFormatted="",
											$scope.medicationDetails[index].quantity="",
											$scope.medicationDetails[index].instructionId="",
											$scope.medicationDetails[index].durationCount="",
											$scope.medicationDetails[index].isGeneric='N',
											$scope.medicationDetails[index].favorite_status=false,
											$scope.medicationDetails[index].nonFormularyAdvice='N',
											$scope.medicationDetails[index].isStat=false,
											$scope.medicationDetails[index].isGeneric='N',
											$scope.medicationDetails[index].isInstruction='N',
											$scope.medicationDetails[index].strength =''
											$scope.medicationDetails[index].itemStrength =''
											$scope.medicationDetails[index].prescribedDosageUnitList = [] 
											} 
									     else if($scope.medicationDetails.length > 1){
												
										   var indexOfMedication = $scope.medicationDetails.indexOf(medication);
										    $scope.medicationDetails.splice(indexOfMedication,1);
										   }
										}else{
											
											 $scope.medication= medication;
											 angular.element('#medicationOpd').modal('show');
										}
									}
							    	
							    	$scope.removeMedicationHistory = function(medication,index) {
										
										if($scope.medicationHistoryDetails.length == 1){
											$scope.medicationHistoryDetails[index].searchItem="",
											$scope.medicationHistoryDetails[index].genericName='',
											$scope.medicationHistoryDetails[index].roaId="",
											$scope.medicationHistoryDetails[index].dose="",
											$scope.medicationHistoryDetails[index].searchItemFre="",
											$scope.medicationHistoryDetails[index].doseUnitId="",
											$scope.medicationHistoryDetails[index].durationUnit="Day",
											$scope.medicationHistoryDetails[index].medicationStartDate="",
											$scope.medicationHistoryDetails[index].medicationEndDate="",
											$scope.medicationHistoryDetails[index].quantity="",
											$scope.medicationHistoryDetails[index].instructionId="",
											$scope.medicationHistoryDetails[index].durationCount=""
											} 
									     else if($scope.medicationHistoryDetails.length > 1){
												
										   var indexOfMedication = $scope.medicationHistoryDetails.indexOf(medication);
										    $scope.medicationHistoryDetails.splice(indexOfMedication,1);
										   }
								}
							    	
							    	
							    	 //cancel Medication
									$scope.stopMedication=function(item){
										$scope.prescriptionDetailsid=item.prescriptionDetailsid;
										
									}
									$scope.cancelMedication = function(justificationStop){
										$scope.justificationStop=justificationStop;
										$rootScope.startLoader();
										try{
//											$rootScope.startLoader();
											var date = new Date();
											$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
											var data={
												    updatedBy:$scope.userId,
													updatedDate:$scope.dt,
													isMedicationStop:'Y',
													justificationStop:$scope.justificationStop,
													prescriptionDetailsid:$scope.prescriptionDetailsid,
													
											};
											
											if(justificationStop != null && justificationStop !="" && justificationStop != undefined){
											var URI = BASE_URL + ROOT_URL + "/api/opd/saveJustificationForMedicationStop";
											GenericService.serviceAction("POST",URI,data).then(function(response){
												if (response.data.status == 'success')
												{
													$rootScope.stopLoader();
													growl.success(response.data.message,
															{
																title : 'Success!'
															});
													$scope.getAllActiveMedication();
													$scope.getDetailedMedication();
													$scope.getMedicationListForMedicalHistory();
													$scope.justificationStop=" ";
//													$scope.$broadcast('show-errors-reset');
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
											growl.warning("Please enter reason to discontinue medication.  ",
												{
													title : 'warning!'
												});
											$rootScope.stopLoader();
									}
										}catch(e){
											console.log("Exception",e);
										}
									}
									// end cancel
							   	 $scope.getMedicationListForMedicalHistory = function()
							        {
							   		 var data ={};
									 $scope.medicationListForMedicalHistoryList = [];
							      	var headerObj = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										prescriptionDate : moment(new Date()).format('YYYY-MM-DD')
									};
								
									var paramObjForPer = {
											patientId : $scope.encounterDetailsObj.patientId,
											encounterId : $scope.encounterDetailsObj.encounterId,

										};
									 
							    		var URI = BASE_URL + ROOT_URL + "/api/opd/getMedicationDetailsForMedicationHistory";
							    		GenericServiceParamHeader.serviceAction(METHOD_GET, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE,paramObjForPer,headerObj)
							    		.then(
							    			function(response) {
							    				if (response.data.status == "success") {
							    					$scope.medicationListForMedicalHistoryList = response.data.listObject;
							    					console.log("medicationListForMedicalHistoryList", $scope.medicationListForMedicalHistoryList)
							    				}
							    			});
							        }
								 $scope.getMedicationListForMedicalHistory();
								
								$scope.editMedication = function()
						        {
						        	$scope.updateFlag = true;
						        	$scope.saveFlag = false;
						        	$scope.medicationObj = $scope.medicationDetails[index];
						        	$scope.searchItem = $scope.medicationDetails[index].itemName;
						        	$scope.searchItemFre = $scope.medicationDetails[index].frequencyDesc;
						        	$scope.medicationIndex= index;
						        	
									var URI = BASE_URL + ROOT_URL + "/api/opd/getRoaList" + "?unitId=" + 
									$scope.unitId + "&orgId=" + $scope.organizationId + "&formulationTypeId=" + $scope.medicationDetails[index].formulationTypeId+ "&genericId=" + $scope.medicationDetails[index].genericId+ "&patientId=" +$stateParams.patientDetailsObj.patientId;
								
									GenericServiceParamHeader.serviceAction(METHOD_GET, URI).then(function(response) {
									if(response.data.status == "success"){
											$scope.roa_options = response.data.listObject;
												
									}
								});
						        }
						        $scope.updateMedication = function()
						        {
						        	
						        	$scope.medicationDetails[$scope.medicationIndex] = $scope.medicationObj;
						        	$scope.updateFlag = false;
						        	$scope.saveFlag = true;
						        }
								
								$scope.searchFavouriteMedication = function() {
									var searchString = $scope.favouriteMedicationString
									var searchResult = $scope.favouriteMedications.filter(function(item){ item.a.indexOf(searchString)!=-1})
									$scope.favouriteMedications = searchResult
								}
								
								$scope.selectedFavouriteMedications = []
								
								$scope.selectFavouriteMedication =function(medication) {
									var indexOfMedication = $scope.selectedFavouriteMedications.indexOf(medication)
									if (indexOfMedication == -1) {
										$scope.selectedFavouriteMedications.push(medication)
									}
									else {
										$scope.selectedFavouriteMedications.splice(indexOfMedication,1)
									}
								}
							    	
								
								
							        function refreshInvestigation()
									 {
							        	
										 
										 if($scope.secondTabLevel==1 && $scope.firstTabLevel == 9){
										   $rootScope.startLoader();
											var data={};
											var paramObj={};
											var headers = {
													'patientId' : $scope.encounterDetailsObj.patientId,
													'flag' : true,
													'Content-Type' : 'application/json',
													'unitId':$scope.unitId
												};
								    		var URI = BASE_URL + ROOT_URL + "/api/opd/getInvestigationsListForCoversheet" ;
								    		GenericServiceParamHeader.serviceAction(METHOD_GET, URI,data,NOTIFICATION_MSG_STATUS_FALSE, paramObj,
													headers)
								    		.then(
								    			function(response) {
								    				if (response.data.status == "success") {
								    					
								    					if(response.data.listObject!=null && $scope.currentEncounterOrderLists!=null)
								    					angular.forEach($scope.currentEncounterOrderLists, function(cValue,ckey) {
								    						angular.forEach(response.data.listObject, function(oalue,okey) {
									    						if(oalue.orderDetailsId==cValue.orderDetailsId)
									    							{
									    							   cValue = oalue;
									    							}
															});
														});
								    					$rootScope.stopLoader();
								    				}
								    				else if(response.data.status == "error")
								    					{
								    					   $rootScope.stopLoader();
								    					}
								    			});
										 }
									 }
							    	var labConfig  =    $scope.checkLabConfig();
								    $scope.datasetOverride = [{
								        yAxisID: 'y-axis-1'
								    }, {
								        yAxisID: 'y-axis-2'
								    }];
								    $scope.options = {
								        scales: {
								            yAxes: [{
								                id: 'y-axis-1',
								                type: 'linear',
								                display: true,
								                position: 'left'
								            }]
								        }
								    };
								    console.log('$stateParams.appointmentObj',$stateParams.appointmentObj);
									
							        
							       
							  
							 
									
									
								  	  
								  
										
										//binding data selected list

										$scope.getFrequentlyUsedMedications = function(){
//											$rootScope.startLoader();
											
											var data = {
														doctorId: $scope.encounterDetailsObj.doctorId,
														unitId: $scope.unitId,
														organizationId:$scope.organizationId,
													
													};
											$scope.getFrequentlyUsedMedications=[];
											var URI = BASE_URL + ROOT_URL +  "/api/opd/getFrequentlyUsedMedicationList";
											console.log("URI", URI);
											GenericService.serviceAction("POST", URI,data).then(function(response) {
												if(response.data.status == "success"){
													$scope.frequentlyUsedMedicationList = response.data.listObject;
													
//													$rootScope.stopLoader();
												}
												
											});
											}
										$scope.getFrequentlyUsedMedications();
					             //save button
										$scope.saveBtnFlagMedication = true;
										$scope.updateBtnFlagMedication = false;
										$scope.updateScope = false;
										$scope.popUpFlag = true;


										$scope.updateFlag = false;
										$scope.updateFlagValidation = false;
										$scope.saveFlag = true;
										
										$scope.saveMedication = function() 
										{
											/*if($scope.updateFlagValidation)
												{
												growl.warning("Please update Medication.", {
													title : 'Warning!'
												});
												return false;
											  }*/
											$scope.saveBtnFlag = true;
											$scope.updateBtnFlag = false;
											
											$scope.updateScope = false;
											$scope.popUpFlag = true;
											
											console.log("$scope.medicationDetails", $scope.medicationDetails);

											$rootScope.startLoader();
											var medicationDetails = $scope.medicationDetails;
											var medicationDetailList = [];

											var data = {
													  "organizationId": $scope.organizationId,
													  "unitId": $scope.unitId,
													  "employeeId" : $scope.employeeId,
													  "visitTypeId":1,
													  "doctorId":$scope.encounterDetailsObj.doctorId,
													  "prescriptionDate":moment(new Date()).format('YYYY-MM-DD  HH:mm:ss'),
													  "authorisedBy": $scope.encounterDetailsObj.doctorId,
													  "authorisedDate":moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
													  "addedBy": $scope.userId,
													  "addedDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
													  "encounterId":$scope.encounterDetailsObj.encounterId,
													  "patientId":$scope.encounterDetailsObj.patientId,
													  "priorityId":2,
													  "deptId" : $scope.encounterDetailsObj.specialityId,
													  "isFromOP" :true
													  
											};

											var flag = true;
											angular.forEach( medicationDetails, function(value, key) {

																var instructionId = value.instructionId;
																if (value.medicationStartDate != ''
																		&& value.medicationEndDate != ''
																		&& value.durationCount != ''
																		&& value.durationUnit != ''
																		&& value.quantity != '') {

																	var medicationDetailsObj = {};
																	medicationDetailsObj["itemId"] = value.itemId;
																	medicationDetailsObj["storeId"] = $scope.storeId;
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
																	medicationDetailsObj["authorisedBy"] = $scope.encounterDetailsObj.doctorId;
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
											if (medicationDetailList.length > 0) {
												data.prescriptionDetailsDtosList = medicationDetailList;
												console.log("medicationDetailList", data);
												var URI = BASE_URL + ROOT_URL
														+ SAVEPATIENTMEDICATION

												GenericServiceParamHeader
														.serviceAction(METHOD_POST, URI,
																data,
																NOTIFICATION_MSG_STATUS_TRUE)
														.then(
																function(response) {
																	if (response.data.status == "success") {
																		$scope.updateFlag = false;
																		$scope.updateFlagValidation = false;
																		$scope.saveFlag = true;
																		$scope.medicationDetails = [];
																		$scope.searchItem="";
																		$scope.searchItemFre="";
																		$scope.medicationIndex = undefined;
																		/*$scope.medicationObj = {
																			itemId : "",
																			searchItem:"",
																			searchItemFre:"",
																			formulationTypeId : '',
																			favorite_status : false,
																			medicationStartDate : new Date(),
																			roaId : '',
																			dose : '',
																			doseUnitId : '',
																			durationCount : "",
																			durationUnit : 'Day',
																			instructionId : '',
																			medicationEndDate : '',
																			frequencyId : '',
																			quantity : '',
																			uomTypeId : '',
																			conversions : '',
																			strength : '',
																			priorityId : '2',
																			priorityCode : 'NR',
																			isGeneric:'N',
																			isInstruction:'N',
																			nonFormularyAdvice:'N',
																			searchItemFre : "",
																			isStat : false,
																			isDischarge : false

																		};*/
//																		$scope.initMedicationObj();
																		$scope.init();
																		$scope.totalSumOfRate=0;
																		$scope.selectedOrderSet=[];
																		$scope.getAllActiveMedication();
																		$scope.getDetailedMedication();
																		$scope.medicationObj.itemStrength="";
																		$scope.medicationObj.prescribedDosageUnitList=[];
																		$rootScope.stopLoader();
																	} else {
																		$rootScope.stopLoader();
																	}
																});
												
											} else {
												/*growl.warning(
																"Please add Medication and then save !!",
																{
																	title : 'Warning!'
																});*/
												$scope.medicationDetailList = [];
												$rootScope.stopLoader();
												return false;
											}
										}
										
										
										/**
										 * ****************************Case Sheet
										 * Start***********************************
										 */
										$scope.setCaseSheetTab=function(){
			// 1.complaintsPatientList
											$scope.getActiveComplaintlist();
											$scope.getComplaintlistByPatientId();
			// 2.personal history
											$scope.getCurrentPatientpersonalHistory();
											$scope.getOldPatientpersonalHistory();
			// 3.family history
											$scope.getFamilyHistory();
											$scope.getOldFamilyHistory();
			// 4.surgical history
											$scope.getSurgicalHistory();
											$scope.getOldSurgicalHistory();
											$scope.getSugicalProcedureList();
			// 5.medication history
											$scope.getDetailedMedicationHistoryList();
			// 6.medical history
											$scope.getMedicalHistory();
											$scope.getVaccineData();
											$scope.getOldMedicalHistory();
											$scope.getPatientDiagnosisForMedicalHistory();
			// 7.treatment history
											$scope.getTreatmentHistory();
											$scope.getOldTreatmentHistory();
											$scope.getMedicalProcedureForTreatmentHistoryList();
											$scope.getDiagnosticsProceduresForTreatmentHistoryList();
			// 8.immunization
											$scope.getActiveImmunization();
											$scope.OldImmunizationHistory();
			// 9.travel history
											 $scope.travelCurrentHistory();
											 $scope.travelOldHistory();
			// 10.habit
											 $scope.getActiveHabitDetails();
			// 11.Allergy
											 $scope.getActiveAllergy();
			// 12.Diagnosis
											 $scope.getAllPatientDiagnosis();
											 $scope.getPatientDiagnosisForMedicalHistory();
			// 13.investigation
											 $scope.getInvestigationList();
			// 14.advice medication
											 $scope.getDetailedMedication();
			// 15.advice instuction
											 $scope.getInstructions();
											 $scope.getActiveGeneralExamination();
			                        	     $scope.getActivephysicalExamination();
			// 16.consult history
											 $scope.consultOrderList();
			// 17.advice admission
											  $scope.getOPDAdmissionDetails();
			// 18.procedure order
											 $scope.getOrderProcedureList();
			// 19.observation
											  $scope.getObservationList();
			// 20.last order
											  $scope.getLastEncounterDetails();
			// 22. Letters and Certificates
											  $scope.certificateListForCaseSheet = [];
											  $scope.lettersListForCaseSheet = [];
										       $scope.getActiveGeneralExamination();
										        $scope.getActivephysicalExamination()
											 /*
												 * $scope.certificateListForCaseSheet =
												 * $scope.getSavedLetterTemplateForCaseSheet(3);
												 * $scope.lettersListForCaseSheet =
												 * $scope.getSavedLetterTemplateForCaseSheet(2);
												 */
											   $scope.getSavedLetterTemplateForCaseSheet(3);
											   $scope.getSavedLetterTemplateForCaseSheet(2);
			// 21.vital list
												  $scope.vitalPatient();
												  
												  // nursing Note List
												  $scope.getNursingNoteList();
										}
										
										/**
										 * ****************************Case sheet
										 * End************************************
										 */
									  /*******service order*********/ 
										$scope.updateServiceRemark = function(info, ind){

											$rootScope.startLoader();
											try{

											var data = {

												    "orderDetailsId": info.orderDetailsId,

												    "ordRemarks":info.ordRemarks

												};

											
											var URI = BASE_URL + ROOT_URL + "/api/opd/updateDiagnosticsRemarks";
											GenericService.serviceAction("POST",URI,data).then(function(response){
											if (response.data.status == 'success')
											{
													growl.success(response.data.message,
											{
											title : 'Success!'
											});
											$rootScope.stopLoader();
											$scope.getInvestigationList();
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
										//search data
										$scope.checkServiceMappedWithTest = function(serviceId,specialityId)
										{
											     $rootScope.startLoaderPopup();
											     var labSpecialityId = 0;
											     if(specialityId==labConfig.labSpecialityId)
													{
											    	   labSpecialityId = specialityId;
													}
												var URI = BASE_URL + ROOT_URL + "/global/serviceMaster/checkService/"+labSpecialityId;
												var data = {serviceMasterId:serviceId,orgId:$scope.organizationId,unitId:$scope.unitId,encounterId:$scope.encounterDetailsObj.encounterId};
											    return BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
												{
													if(response.data.status == "success")
													{
														$rootScope.stopLoaderPopup();
														if(response.data.object==0)
														{
														   growl
															.warning('Service is Not mapped with Test!!',{
																		title : 'Warning!'
																	});
														   response.data.object = 1;
													     }
														//return response.data.object;
														return response.data.object+"##"+response.data.code;
													}
													else if(response.data.status == "error"){
														  $rootScope.stopLoaderPopup();
														  return response.data.object;
													   }
												 });
										}
										 $scope.search = '';
									     $scope.selectedForFuture = 'N';
									     $scope.searchServices = function(searchKeyword){
												$scope.servicesList=[];
												searchKeyword=searchKeyword+"---"+$scope.encounterDetailsObj.genderId;
												var serData = {
														organizationId : $scope.organizationId,
														unitId : $scope.unitId,
														searchKeyword : searchKeyword
												}
												//var URI = BASE_URL + ROOT_URL + "/global/serviceMaster/"+searchKeyword+"/unit/"+$scope.unitId+"/org/"+$scope.organizationId;
												var URI = BASE_URL + ROOT_URL + "/global/serviceMaster";
												return BillingGenericService.serviceAction(METHOD_POST,URI, serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
													if(response.data.status == "success"){
														$scope.servicesList = response.data.listObject;
													}
													return $scope.servicesList;
												});
											}
									     
									     $scope.getAutoRenderedServicesByPatient=function(encounterObj){
												
												$rootScope.startLoader();
												$scope.autoRenderedServicesBillingDetails=[];
												//var URI = BASE_URL + ROOT_URL + "/api/billing/serviceMaster/autoRendered";
												//var URI = BASE_URL + ROOT_URL + "/api/billing/serviceMaster/renderedServices";
												var URI = BASE_URL + ROOT_URL + "/api/billing/serviceMaster/orderedServices";
												var obj={
														"visitTypeId":1,
														"encounterId":encounterObj.encounterId
												}
												console.log("AUTORENDER",obj);
												//return false;
												BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
												{	
													if(response.data.status == "success")
													{
														$scope.autoRenderedServicesList = response.data.listObject;
														console.log("$scope.autoRenderedServicesList",$scope.autoRenderedServicesList);
														
														
														
														/*var indexT = response.data.listObject.findIndex(record => record.serviceMasterId === $scope.surchargeServiceId);
														
													    if (indexT >= 0) {
													    	$scope.isPatientClass = "Y";
													    }*/
													    
														//return false;
														  $scope.calculationTestPostBilling($scope.autoRenderedServicesList,1);
														  $rootScope.stopLoader();
													}
													else
														{
														 $rootScope.stopLoader();
														}
												});
											}
									     
									 	// for add to bill*****************************
								    	    
								    	    var URI = BASE_URL + ROOT_URL + "/api/billing/getBillData/"+$scope.encounterDetailsObj.encounterId;
								    		BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
								    				$scope.billNumber =response.data.billNumber;
								    				console.log("$scope.billNumberrrr", $scope.billNumber);
								    		});
								    	    
								    	  // *************************************
								    		//******************************
								    		var URI =BASE_URL + ROOT_URL +"/api/globals/strength/units/"+$scope.organizationId;
											GenericService.serviceAction("GET", URI, {}).then(
											function(response) {
											$scope.doseList = [];
											$scope.doseList = response.data;

											});
											//**************************************************************
								    		
									     $scope.serviceHideFlag=false;
									        $scope.addServiceToTempServiceList = function($item, $model, $label,investigation)
									        {
									        	console.log(investigation);
									        	var obj = {
														"encounterId" :$scope.encounterDetailsObj.encounterId,
														"contractId" :$scope.contractId,
														"serviceId" :$item.serviceMasterId,
														"currentDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
														"organizationId":$scope.organizationId,
														"unitId":$scope.unitId,
														"visitTypeId":1,
														"billingBedCategoryId":$scope.opdBillingCatId,
														"patientCategoryId":$scope.patientDetails.patientCategoryId,
														"defaultSelfTariffId":$scope.defaultSelfTariffId,
													    "selfPayeeId" :$scope.selfPayeeId,
													    "quantity":1,
													    "patientId":$scope.patientDetails.patientId,
													    "tariffId" :$scope.defaultSelfTariffId,
													    "paymentEntitlementId": $scope.encounterDetailsObj.paymentEntitlementTypeId
													};
												
												console.log('Base prise',obj)
												$rootScope.startLoaderPopup();
												var URI = BASE_URL + ROOT_URL + "/global/consumption/getSerivceRateGlobal";
												BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
												{
														if(response.data.object != "undefined" && response.data.object != null)
														{
															if(response.data.object.doesNotExist==null)
															{
																$rootScope.stopLoaderPopup();
																$scope.serviceHideFlag=true;
																var promise = $scope.checkServiceMappedWithTest($item.serviceMasterId,$item.specialityId);
													    		promise.then(function(obj) 
													    				{
													    					var  chkObj=obj.split("##");
													        	             if(chkObj[0]>0)
													        	            	 {
															        	            	 $scope.orderServiceId = $item.serviceMasterId;
															        	         		 $scope.orderServiceName = $item.serviceStandardName;
															        	         		 $scope.orderSpecialtyId = $item.specialityId;
															        	         		 $scope.orderSpecialtyName = $item.specialityName;
															        	         		 $scope.orderSubSpecialtyId = $item.subSpecialityId;
															        	             	 $scope.orderSubSpecialtyName = $item.subSpecialityName;
															        	             	 $scope.orderIsRefDoctorShare = $item.isRefDoctorShare;
															        	             	 $scope.status = $item.status;
															        	             	 $scope.selectedServiceTypeSpecialityId = $scope.orderSpecialtyId;
															        	             	 $scope.getSubSpecialityList($scope.orderSpecialtyId);
															        	             	 $scope.selectedSubServiceTypeSubSpecialityId = $scope.orderSubSpecialtyId;
															        	             	 $scope.isAppointable = $item.isAppointable;
															        	             	 $scope.orderDetailsIdP=chkObj[1]!=null && chkObj[1]!=undefined && chkObj[1]>0 ? chkObj[1] : undefined ; 
															        	             	$scope.addInvestigation();
													        	            	 }
													        	             else{

													        	            	 growl
													        	 				.warning(
													        	 						'Service is Not mapped with Test!!',
													        	 						{
													        	 							title : 'Warning!'
													        	 						});
													        	                 }
													    				 });
													    		
															
															}else{
																$rootScope.stopLoaderPopup();
																$scope.search = "";
																growl.warning("Service is not mapped with the tariff..!!",{
																	title : WARNING_MSG
																});
																return false;
															}
															
																
														}
															
													});
									        	}
									        
									        $scope.getSubSpecialityList = function(id)
									        {
									        
													var specialityType = [];
													specialityType.push(id);
															
													var data = {
														organizationId : $scope.organizationId,
														unitId : $scope.unitId,
														specialityIdList : specialityType
													}
													console.log(data);
													var URI = BASE_URL+ ROOT_URL+ GETSUBSPECIALITYBYSPECIALITYARRAYFORTARIFF;
													BillingGenericService
															.serviceAction(METHOD_POST,URI, data,NOTIFICATION_MSG_STATUS_FALSE)
															.then(function(response) {
																		console.log(response);
																		if (response.data.status == "success") {
																			
																			$scope.subSpecialityList = response.data.listObject;
																			
																		}
																	});
									        }
									        
									        $scope.addedServiceOrdersForDisplay = [];
									        var commonObjs = {
													unitId : $scope.unitId,
													organizationId : $scope.organizationId
												};
											// For Get Active Reason List
									        var URI = BASE_URL + ROOT_URL + GETACTIVEREASONLIST;
											GenericService.serviceAction("POST", URI, commonObjs)
													.then( function(response) {
																$scope.reasonList = [];

																if (response.data.status == "success") {
																	$scope.reasonList = response.data.listObject;
																	console.log("reasonList",$scope.reasonList );
																}
															});
											$scope.getDataForCancelOrder=function(investigation){
												
												$scope.orderDetailsIdf=investigation.orderDetailsId;
												$scope.serviceSpecialityId=investigation.subDeptId;
												$scope.appointmentId=investigation.appointmentId;
												$scope.slotId=investigation.slotId;
											}
											
											$scope.cancelPendingServicePrePost = function(reasonId) {
									    		
												
												if(reasonId == undefined || reasonId == "")
													{
													growl
													.warning(
															'Please select reason...!!',
															{
																title : 'Warning!'
															});
													}
												else
													{
													
												   $rootScope.startLoader();
												   var data = {};
												   data.orderDetailsId = $scope.orderDetailsIdf;
												   data.ordCancelReasonId = reasonId;
												   data.userId = $scope.userId;
												   data.encounterId = $scope.encounterDetailsObj.encounterId;
												   data.specialityId =$scope.serviceSpecialityId;
												   data.visitTypeId=$scope.patientDetails.visitTypeId;
												   data.preOrPostBilling=cookieObjectAppConfig.isPreOrPostBilling;
												   
									    		var URI = BASE_URL + ROOT_URL
												+ "/api/billing/bill/service/cancel";
									    		GenericService.serviceAction("POST", URI, data)
												.then(
														function(response) {
															if (response.data.status == "success") {
																growl.success( response.data.message,
																		{
																			title : SUCCESS_MSG
																			
																		});
																//cancel the radiology services start
															    if($scope.appointmentId!=undefined && $scope.appointmentId!=null && $scope.appointmentId !='' && $scope.slotId!=undefined && $scope.slotId!=null && $scope.slotId !=''){
															    	var URI1 = BASE_URL + ROOT_URL
																	+ "/api/opd/cancelScheduledAppointment";
															    	var data1={
															    			slotId : $scope.slotId,
															    			cancelReasonId:reasonId,
															    			cancelledBy : 'C',
															    			updatedBy : $scope.userId,
															    			updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
															    			appointmentId:$scope.appointmentId,
															    			organizationId: $scope.organizationId,
															    			unitId : $scope.unitId,
															    	}
															    	
														    		GenericService.serviceAction("POST", URI1, data1).then(function(response) {
														    			$scope.appointmentId=undefined;
														    			$scope.slotId=undefined;
																	});
															    }
															//cancel the radiology services end
															    $scope.getInvestigationList();
																$scope.setSecondTabLevel(1);
																$rootScope.stopLoader();
																
															}
															else
																{
																$rootScope.stopLoader();
																}
														});
													}
											
										}
											
											$scope.getInvestigationList=function(){
												$scope.dt = moment(new Date()).format('DD-MM-YYYY');
												 $rootScope.startLoader();
												var data={};
												var paramObj={};
												var headers = {
//														'patientId' : $scope.encounterDetailsObj.encounterId,
														'patientId' : $scope.encounterDetailsObj.patientId,
														'flag' : true,
														'Content-Type' : 'application/json',
														'unitId':$scope.unitId
													};
												$scope.currentEncounterOrderLists = [];
									    		var encounterId = $scope.encounterDetailsObj.encounterId;
//									    		var URI = BASE_URL + ROOT_URL + GETCURRENTINVESTIGATION ;
									    		var URI = BASE_URL + ROOT_URL + "/api/opd/getInvestigationsListForCoversheet" ;

									    		GenericServiceParamHeader.serviceAction(METHOD_GET, URI,data,NOTIFICATION_MSG_STATUS_FALSE, paramObj,
														headers)
									    		.then(
									    			function(response) {
									    				if (response.data.status == "success")
									    					$scope.currentEncounterOrderLists = response.data.listObject;
									    					$scope.currentEncounterOrderListsPkg = response.data.listObject;
									    					angular.forEach($scope.currentEncounterOrderLists,function(value,index)
									    						{
									    							value.createdDate = moment(value.createdDate,'DD/MM/YYYY').format('DD-MMM-YY');
									    							value.orderSchDate = moment(value.orderSchDate,'DD/MM/YYYY').format('DD-MMM-YY');
									    						});
									    					
									    				/*{$scope.currentEncounterOrderListsPkg = response.data.listObject;
									    					 $rootScope.stopLoader();
									    				}*/
									    			});
											}
											$scope.getInvestigationList();
									        //end search data
									        
										 $scope.getMostlyUsedServices = function()
				                            {
				                                var data ={
				                                        doctorId :$scope.encounterDetailsObj.doctorId,
				                                        unitId : $scope.unitId,
				                                        organizationId : $scope.organizationId
				                                };
				                                
				                                var URI = BASE_URL + ROOT_URL + "/api/opd/getMostlyUsedFavoritesServiceList";
				                                GenericServiceParamHeader.serviceAction(METHOD_POST, URI, 
				                                        data, NOTIFICATION_MSG_STATUS_FALSE)
				                                        .then(function(response) {
				                                            if(response.data.status == "success"){
				                                                $scope.mostlyUsedServices = [];
				                                                $scope.mostlyUsedServices = response.data.listObject;
				                                                console.log("Mostly used services",$scope.mostlyUsedServices);
				                                            }
				                                });
				                            }
				                         $scope.getMostlyUsedServices();
										
										
									        
									       
											
											//save
											$scope.checkServiceRenderOrNot = function()
											{
									        	    $rootScope.startLoader();
									        		var newArr = {};
									        		if($scope.orderServiceId!=undefined && $scope.orderServiceId !="" ){
									        		newArr.specialityId = $scope.orderSpecialtyId;
									            	newArr.serviceSpecialityId = $scope.orderSpecialtyId;
									          		newArr.serviceSubSpecialityId =$scope.orderSubSpecialtyId;
									        		newArr.subSpecialityId = $scope.orderSubSpecialtyId;
									        		newArr.serviceMasterId = $scope.orderServiceId;
									        		newArr.serviceStandardName = $scope.orderServiceName;
									        		newArr.specialityName = $scope.orderSpecialtyName;
									            	newArr.subSpecialityName = $scope.orderSubSpecialtyName;
									            	newArr.serviceRenderingDeptId = $scope.orderSubSpecialtyId;
									            	newArr.createdBy  = $scope.userId;
									        		newArr.createdDate = new Date().getTime();
									        		newArr.selfPayable =  "0";
									        		newArr.creditPayable =   "0";
									        		newArr.serviceRendered = "0";
									        		newArr.doctorId = $scope.encounterDetailsObj.doctorId;
									        		newArr.discountPercentage = 0;
									        		newArr.discount =0;
									        		newArr.quantity = 1;
									        		newArr.basePrice =  0;
									        		newArr.basePriceOld =  0;
									        		newArr.totalAmt = 0;
									        		newArr.concession = 0;
									            	newArr.selectedForFuture = $scope.selectedForFuture;
									            	newArr.payeeId = $scope.encounterDetailsObj.payeeId;
									    			newArr.apportionedPrice = null;
									        		newArr.packageId = null;
									        		newArr.packageTypeId = null; 
									        		newArr.packageEitherOrGroupId = null; 
									        		newArr.packageCapDetailsId = null;
									        		newArr.isCapService = null;
									        		newArr.status = 'A';
									        		newArr.isRefDoctorShare = null;
									        		newArr.packageEitherOrGroupId = null;
									        		newArr.isPackageService ='N';
									        		newArr.discountDetailsList = [];
									        		newArr.isRefDoctorShare = $scope.orderIsRefDoctorShare;
									        		newArr.isAppointable=$scope.isAppointable;
									        		newArr.investigationFutureDateToShowdddd = moment(new Date()).format('DD/MM/YYYY');
									        		if($scope.investigationFutureDate != "" && $scope.investigationFutureDate != undefined ){
									            		newArr.investigationFutureDate = $scope.investigationFutureDate
									            	
									            		newArr.investigationFutureDateToShowdddd = moment($scope.investigationFutureDate).format('DD/MM/YYYY')
									            		}
									            		
									            		if( $scope.investigationFutureDate == undefined && $scope.selectedForFuture == "Y"){
									            			
									            			growl.warning("Please select Date !!",
									    					   		{
									    								title : 'Warning!'
									    							});
									    							$rootScope.stopLoader();
									    							return false;
									                		}
									                		
									            		
									                    if( $scope.investigationFutureDate == ""  && $scope.selectedForFuture == "Y"){
									            			
									            			growl.warning("Please select Date !!",
									    					   		{
									    								title : 'Warning!'
									    							});
									    							$rootScope.stopLoader();
									    							return false;
									                		}
									                    $scope.dv = new Date();
									                    if($scope.investigationFutureDate != "" && $scope.investigationFutureDate != undefined ){
									                     if( $scope.investigationFutureDate < $scope.dv){
									            			
									            			growl.warning("Please select greater than todays date !!",
									    					   		{
									    								title : 'Warning!'
									    							});
									    							$rootScope.stopLoader();
									    							return false;
									                		}
									                    }
									        		
									        	/*	if($scope.search ==''||$scope.selectedServiceTypeSpecialityId == ''||$scope.selectedSubServiceTypeSubSpecialityId == '')
													{
													 growl.warning("Please select Service !!",
													   		{
																title : 'Warning!'
															});
															$rootScope.stopLoader();
															return false;
													}
									        		else{*/
															  $scope.addedServiceOrdersForDisplay.push(newArr);
															  $scope.search = '';
												            	$scope.selectedServiceTypeSpecialityId='';
												            	$scope.selectedSubServiceTypeSubSpecialityId='';
												            	$scope.selectedForFuture = 'N';
												            	$scope.investigationFutureDate='';
												        		$scope.fromDatepickerForFuture='';
												        		$scope.saveInvestigation();
//									        		}
									            	
										            	$rootScope.stopLoader();
										            	$scope.search = '';
										            	$scope.selectedServiceTypeSpecialityId='';
										            	$scope.selectedSubServiceTypeSubSpecialityId='';
										            	$scope.selectedForFuture = 'N';
										            	$scope.investigationFutureDate='';
										            	$scope.fromDatepickerForFuture='';
									        		}
									        		else 
									        		{
										        		growl.warning("Please select Service !!",
																{
																	title : 'Warning!'
																});
										        		$rootScope.stopLoader();
										        	}
											}
											$scope.editserviceHideFlag=false;
											
											$scope.editTempServicesForFutByIndex = function(item,indx) {
												
												$scope.editInvestigation=indx;
												
												$scope.editserviceHideFlag=true;
												$scope.model = item.serviceStandardName;
												$scope.label = item.serviceStandardName;
												$scope.search = item.serviceStandardName;
												$scope.addServiceToTempServiceList(item, $scope.model, $scope.label);
											}
											
											
											// code for package searching start
											$scope.searchPackages = function(keyword) {

												$scope.packageSearchObj = {
													organisationId : $scope.organizationId,
													unitId : $scope.unitId,
													visitTypeId : 1,
													bedBillingCategoryId : $scope.opdBillingCatId,
													paymentEntitlementId : $scope.encounterDetailsObj.paymentEntitlementTypeId,
													patientTypeId : $scope.encounterDetailsObj.patientCategoryId,
													serviceName : keyword,
													genderId : $scope.patientDetails.genderId
												}
												return servicesFactory.searchPackageForBilling($scope.packageSearchObj,$scope);

											}
											
											
											$scope.packageResultObj={};
											$scope.selectPackageService = function($item, $model, $label){
												if(typeof($item.packageTypeId) != "undefined" && $item.packageTypeId == "1")
												{
													var birthDateArr=$scope.patientDetails.birthDate.split("/");
													var age= $rootScope.getAge(birthDateArr[1]+"/"+birthDateArr[0]+"/"+birthDateArr[2]);
													var genderId=$scope.patientDetails.genderId;
													
													if(!($item.minAge <= age && $item.maxAge >= age)){
														$scope.packageSearchObj.serviceName='';
														growl.warning("Package is not yet  configure for this age.",{
															title : WARNING_MSG
														});
														return;
													}
												}
												
												if(typeof($item.packageTypeId) != "undefined" && $item.packageTypeId == "1"){
													$scope.getInvestigationList();
												}
												$scope.packageResultObj={};
												$scope.packageResultObj =  $item;
												$scope.forServicePayeeId  = $scope.selfPayeeId;
												
												if(parseInt($scope.contractId) == 0)
												{
													$scope.contractId = null;
												}
												$scope.packageResultObj.encounterId = $scope.encounterDetailsObj.encounterId;
												$scope.packageResultObj.contractId = $scope.contractId;
												$scope.packageResultObj.visitTypeId = 1;
												$scope.packageResultObj.billingBedCategoryId = $scope.opdBillingCatId;
												$scope.packageResultObj.patientCategoryId = $scope.encounterDetailsObj.patientCategoryId;
												$scope.packageResultObj.defaultSelfTariffId = $scope.defaultSelfTariffId;
											    $scope.packageResultObj.selfPayeeId = $scope.selfPayeeId;
											    $scope.packageResultObj.organizationId = $scope.organizationId;
											    $scope.packageResultObj.unitId = $scope.unitId;
													
											    var packageIds = [];
											    $scope.packageId = $item.packageMasterId;
											    packageIds.push($item.packageMasterId);
											    var packageId = $scope.listPackageOrder!=null && $scope.listPackageOrder !=undefined ? $scope.listPackageOrder.find(packageObj => packageObj.packageId == $scope.packageId) : undefined;
											    if(packageId==undefined){
													$rootScope.startLoader();
													var URI = BASE_URL + ROOT_URL + "/global/consumption/package";
													BillingGenericService.serviceAction(METHOD_POST,URI, $scope.packageResultObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
														if(response.data.status=="success"){
															console.log("res",response);
															$scope.addedServicePackageOrdersForDisplay=[];
															if(response.data.object != "undefined" && response.data.object != null)
																{
																$scope.forServicePayeeId = response.data.object.payeeId;
																$item.basePrice=response.data.object.serviceRate;
																$item.quantity=1;
																$item.coShare = response.data.object.coShare;
																$item.selfPayable = response.data.object.selfPayable;
																$item.creditPayable = response.data.object.creaditPayable;
																$item.contractId = response.data.object.contractId;
																$item.patientClassPercentage = response.data.object.patientClassPercentage;
																$item.markupStatus = response.data.object.markupStatus;
																$item.minRateEditable = response.data.object.minRateEditable;
																$item.maxRateEditable = response.data.object.maxRateEditable;
																$item.orderDetailsPayeeMapperDtosList=response.data.object.orderDetailsPayeeMapperDtosList;
																$item.isPackage = true;
																//for package
																$item.orderDetailsPackageMapperDtosList=response.data.object.orderDetailsPackageMapperDtosList;
																$item.payeeId=response.data.object.payeeId;
																
																
																var newArr = {};
								    			    			newArr.priorityId  = '2';
								    			    			newArr.isOutsourced ='N';
								    			    			newArr.specialityId = $item.specialityId;
								    			    			newArr.serviceSpecialityId = $item.subSpecialityId;
								    			    			newArr.serviceSubSpecialityId = $item.subSpecialityId;
								    			    			if($scope.isPreOrPostBilling == 'N')
								    			    			{
								    			    			newArr.serviceRendered = 1;
								    			    			}
								    			    		   else
								    			    			{
								    			    			 newArr.serviceRendered = "0";
								    			    			}
								    			    			newArr.serviceId =  $item.serviceMasterId;
								    			    			newArr.isDoctorRequired = $item.isDocReq;
								    			    			newArr.isRefDoctorShare = $item.isRefDoctorShare;
								    			    			newArr.isDiscountApplicable = "N";
								    			    			newArr.isQuantityEditable = "N";
								    			    			newArr.isRateEditable = "N";
								    			    			newArr.minRateEditable = 0;
								    			    			newArr.maxRateEditable = 0;
								    			    			newArr.quantity = 1;
								    			    			newArr.basePrice =  $item.basePrice;
								    			    			newArr.serviceAmt = $item.basePrice;
								    			    			newArr.totalAmt = $item.basePrice;
								    			    			newArr.selfPayable = $item.selfPayable;
								    			    			newArr.creditPayable =  $item.creditPayable;
								    			    			newArr.netAmt = 0;//code to check
								    			    			newArr.basePriceOld =  $item.basePrice;
								    			    			newArr.discountDisabledFlag = 'N';
								    			    			newArr.concession = 0;
								    			    			newArr.discountPercentage = 0;
								    			    			newArr.discount = 0;
								    			    			var dis =0;
								    			    			var disPer =0;
								    			    			newArr.taxPercentage = 0;
								    			    			newArr.discountDetailsList = [];
								    			    			newArr.oldNetPay = "0";
								    			    			newArr.createdBy  = $scope.userId;
								    			    			newArr.createdDate = new Date().getTime();
								    			    			newArr.packageTypeId = $item.packageTypeId;
								    			    			newArr.packageId = $item.packageMasterId;
								    			    			newArr.isPackageService = 'N';
								    			    			newArr.isPackage = true;
								    			    			newArr.packageOrNot=0;
								    			    			newArr.apportionedPrice = $item.basePrice;
								    			    			newArr.subSpecialityId = $item.subSpecialityId; 
								    			    			newArr.ordCancelled = 'N';
								    			    			newArr.ordDocSplId = $scope.encounterDetailsObj.specialityId;
								    			    			if($item.isRefDoctorShare=='Y')
								    			    				{
								    			    				 newArr.doctorId = 0;
								    			    	    		 newArr.doctorSpecialityId = 0;
								    			    				}
								    			    			newArr.orderDetailsPayeeMapperDtosList = $item.orderDetailsPayeeMapperDtosList;
								    			    			newArr.ordDocId = $scope.encounterDetailsObj.doctorId,
								    			    			newArr.orderDetailsPackageMapperDtosList =$item.orderDetailsPackageMapperDtosList;
								    			    			
								    			    			//push objects
																$scope.addedServicePackageOrdersForDisplay.push(newArr);
																
																var packageServiceList = [];
																if(typeof($item.packageTypeId) != "undefined" && $item.packageTypeId == "1"){
												    	    		var packageIds = [];
												    	    		packageIds.push($item.packageMasterId);
												    	    		//get ehc package services 
												    	    		var URI = BASE_URL + ROOT_URL + "/api/packages/bill/package/"+packageIds.toString();
												    				BillingGenericService.serviceAction("GET", URI, null).then(function(response){
												    					if(response.data.status == "success")
												    					{ 
												    						console.log("order list",$scope.currentEncounterOrderListsPkg);
												    						var ehcPackageServiceList = response.data.listObject[0].listTPackageServicesDetailsDto;
												    						angular.forEach($scope.currentEncounterOrderListsPkg,function(val1,j){							    								
												    							angular.forEach(ehcPackageServiceList,function(val,i){
												    								if(val1.serviceMasterId==val.serviceId && val1.serviceRendered==0 && val1.serviceIsBilled==0 && (val.isOrderDetailSet==undefined || val.isOrderDetailSet!=true) && val1.isPackageService=='N' && val1.orderStatus=='A'){
												    									val.orderDetailsId=val1.orderDetailsId;
												    									val.orderId=val1.orderId;
												    									val.isOrderDetailSet=true;
												    								}
												    							});
											    							});
												    						
												    						angular.forEach(ehcPackageServiceList,function(val,i){						    							
												    														    							
												    							var newArr = {};
												    			    			newArr.priorityId  = '2';
												    			    			newArr.isOutsourced ='N';
												    			    			newArr.specialityId = val.specialityId;
												    			    			newArr.serviceSpecialityId = val.specialityId;
												    			    			newArr.serviceSubSpecialityId = val.subSpecialityId;
												    			    			if($scope.isPreOrPostBilling == 'N')
												    			    			{
												    			    			newArr.serviceRendered = 1;
												    			    			}
												    			    		   else
												    			    			{
												    			    			 newArr.serviceRendered = "0";
												    			    			}
												    			    			newArr.serviceId = val.serviceId;
												    			    			newArr.serviceStandardCode = val.serviceCode;
												    			    			newArr.serviceStandardName = val.serviceName;
												    			    			newArr.isDoctorRequired = val.isDocReq;
												    			    			newArr.isRefDoctorShare = val.isRefDoctorShare;
												    			    			newArr.isDiscountApplicable = "N";
												    			    			newArr.isQuantityEditable = "N";
												    			    			newArr.isRateEditable = "N";
												    			    			newArr.minRateEditable = 0;
												    			    			newArr.maxRateEditable = 0;
												    			    			newArr.specialityName = val.specialityName;
												    			    			newArr.quantity = 1;
												    			    			newArr.basePrice =  val.servicePrice;;
												    			    			newArr.serviceAmt = val.servicePrice;
												    			    			newArr.totalAmt = val.servicePrice;
												    			    			newArr.netAmt = 0;
												    			    			newArr.basePriceOld = 0;
												    			    			newArr.discountDisabledFlag = 'N';
												    			    			newArr.concession = 0;
												    			    			newArr.discountPercentage = 0;
												    			    			newArr.discount = 0;
												    			    			newArr.coPayPer = 0;
												    			    			var dis =0;
												    			    			var disPer =0;
												    			    			newArr.taxPercentage = 0;
												    			    			newArr.discountDetailsList = [];
												    			    			newArr.selfPayable =  "0";
												    			    			newArr.creditPayable =   "0";
												    			    			newArr.oldNetPay = "0";
												    			    			newArr.createdBy  = $scope.userId;
												    			    			newArr.createdDate = new Date().getTime();
												    			    			newArr.packageMasterId = $item.packageMasterId;
												    			    			newArr.packageTypeId = $item.packageTypeId;
												    			    			newArr.packageId = $item.packageMasterId;
												    			    			newArr.isPackageService = 'Y';
												    			    			newArr.isPackage = false;
												    			    			newArr.packageOrNot=0;
												    			    			newArr.apportionedPrice = val.apportionedPrice;
												    			    			newArr.consultationSpecialityId=val.consultationSpecialityId;
												    			    			newArr.subSpecialityId = val.subSpecialityId; 
												    			    			newArr.ordCancelled = 'N';
												    			    			newArr.orderId = val.orderId!=undefined && val.orderId!=null ? val.orderId : "";
												    			    			newArr.orderDetailsId=val.orderDetailsId!=undefined && val.orderDetailsId!=null ? val.orderDetailsId : "";
												    			    			if(val.isRefDoctorShare=='Y')
												    			    				{
												    			    				 newArr.doctorId = 0;
												    			    	    		 newArr.doctorSpecialityId = 0;
												    			    				}
												    			    			var dataMappr = [{
												    			    				   orderDetailsId:val.orderDetailsId!=undefined && val.orderDetailsId!=null ? val.orderDetailsId : "",
												    			    					contractId: null,
												    			    					creditPayable: 0,
												    			    					organizationId: $scope.organizationId,
												    			    					payeeAmount: 0,
												    			    					payeeId : $scope.selfPayeeId,
												    			    					selfPayable: 0,
												    			    					serviceId: val.serviceId,
												    			    					status: 'A',
												    			    					unitId: $scope.unitId
												    			    			 }];
												    			    			
												    			    			newArr.orderDetailsPayeeMapperDtosList = dataMappr;
												    			    			
												    			    			// for Package
												    			    			var packageMppr = [{"id":"",
												    			    				"orderDetailsId":val.orderDetailsId!=undefined && val.orderDetailsId!=null ? val.orderDetailsId : "",
												    			    				"serviceId":val.serviceId,
												    			    				"packageId":$item.packageMasterId,
												    			    				"payeeId":"",
												    			    				"serviceAmount":val.servicePrice,	
												    			    				"packageAmount":val.servicePrice,
												    			    				"isPackageService":"Y",
												    			    				"isCapService":"N",
												    			    				"saleDetailsId":"",
												    			    				"itemId":"",
												    			    				"status":"A",
												    			    				"unitId":$scope.unitId,
												    			    				"organizationId": $scope.organizationId}];
												    			    			
												    			    			  newArr.orderDetailsPackageMapperDtosList = packageMppr;
												    			    			  $scope.addedServicePackageOrdersForDisplay.push(newArr);
												    						})
														    				
												    					}
												    				});
												    	    	  }

																}
															else
																{
																  growl.warning("Please add base price of this service and then add..!!",{
																	title : 'Warning!'
																  });
																return false
																}
															console.log("FINAL ITEM",$item);
														}
													});
													console.log('final dataaaaaaaaaa',$scope.addedServicePackageOrdersForDisplay);
													return false;
													
												}else{
													$scope.packageSearchObj.serviceName='';
													$scope.packageId = '';
													$scope.packageResultObj={};
													growl.warning("Package Already Added !",{
														title : 'Warning'
													});
													return false;
												}
												
												
											}
											
											// code for package searching end
											$scope.clearePackage = function()
											{
												if($scope.packageSearchObj.serviceName==undefined)
													{
													 $scope.packageResultObj={};
													 $scope.packageSearchObj.serviceName='';
													 $scope.packageId = '';
													} 
											}
											// save packages
											$scope.savepackage = function() 
											{
												if(angular.equals({}, $scope.packageResultObj)){
													growl.warning("Select Package !!", {
														title : 'Warning!'
													});
													$scope.packageSearchObj.serviceName='';
													$rootScope.stopLoader();
													return false;
												}
												
												/*if($scope.packageId==''||$scope.packageId==undefined)
													{
														growl.warning("Package is already Ordered!!", {
															title : 'Warning!'
														});
													  $rootScope.stopLoader();
													  return false;
													}*/
												var packageId = $scope.listPackageOrder.find(packageObj => packageObj.packageId == $scope.packageId );
											    if(packageId!=undefined)
													{
													growl.warning("Package is already Ordered!!", {
														title : 'Warning!'
													});
													$rootScope.stopLoader();
													return false;
													}
											  else{
												$rootScope.startLoader();
												var listOrderMasterDto = [];
												var finalData = {
													orgId : $scope.organizationId,
													orgUnitId : $scope.unitId,
												};
												var data = {

													currentDate : moment(new Date()).format(
															'YYYY-MM-DD HH:mm:ss'),
													createdDate : new Date().getTime(),
													updatedDate : new Date().getTime(),
													orgId : $scope.organizationId,
													orgUnitId : $scope.unitId,
													createdBy : $scope.userId,
													updatedBy : $scope.userId,
													patientId : $scope.encounterDetailsObj.patientId,
													encounterId : $scope.encounterDetailsObj.encounterId,
													defaultSelfTariffId : $scope.defaultSelfTariffId,
													paymentEntitlementTypeId :$scope.encounterDetailsObj.paymentEntitlementTypeId,
													doctorId : $scope.encounterDetailsObj.doctorId,
													priorityId : 2,
													visitTypeId : 1,
													orderDate : new Date().getTime(),
													orderStatus : 'A',
													orderStatusId : 1,
													contractId : $scope.encounterDetailsObj.contractId,
													payeeId : $scope.encounterDetailsObj.payeeId,
													billingBedCategoryId : $scope.opdBillingCatId,
													paymentEntitlementTypeId : $scope.encounterDetailsObj.paymentEntitlementTypeId,
													patientTypeId :$scope.encounterDetailsObj.patientCategoryId,
													isPreOrPostBilling :$scope.isPreOrPostBilling,
													pharmacyServiceId:$scope.pharmacyServiceId
												}
//												$scope.addedServicePackageOrdersForDisplay = [];
												var finalOrderServiceList = [];
												angular.forEach($scope.addedServicePackageOrdersForDisplay,
																function(value, key) {

																	var newArr = {};
//																	newArr.ordDocId = $scope.encounterDetailsObj.doctorId;
																	newArr.serviceId = value.serviceId;
//																	newArr.ordDocSplId = $scope.encounterDetailsObj.specialityId;
																	if($scope.configureConsultationSpecialityId!=undefined && $scope.configureConsultationSpecialityId!=null && $scope.configureConsultationSpecialityId>0 && value.specialityId!=undefined && value.specialityId!=null && value.specialityId>0 ){
																		
																		if($scope.configureConsultationSpecialityId==value.specialityId){
																			
																			if(value.consultationSpecialityId!=null && value.consultationSpecialityId!=undefined && value.consultationSpecialityId>0){
																				newArr.ordDocSplId = value.consultationSpecialityId;
																				newArr.ordDocId=undefined;
																			}else{
																				newArr.ordDocSplId = undefined;
																				newArr.ordDocId=undefined;
																			}
																			
																		}
																	}
																	
																	newArr.serviceRenderingDeptId = value.subSpecialityId;
																	newArr.apportionedPrice = value.apportionedPrice;
																	newArr.ordTotalAmt =  value.basePrice;
									    			    			newArr.serviceAmt = value.basePrice;
									    			    			newArr.selfPayable = value.selfPayable;
									    			    			newArr.creditPayable =  value.creditPayable;
																	newArr.packageId = value.packageId;
																	newArr.packageTypeId = value.packageTypeId;
																	newArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
																	newArr.packageCapDetailsId = value.packageCapDetailsId;
																	newArr.isCapService = value.isCapService;
																	newArr.packageConsumptionAmt = value.packageConsumptionAmt;
																	newArr.taxPer = value.taxPercentage;
																	newArr.status = 'A';
																	newArr.isRefDoctorShare = value.isRefDoctorShare;
																	newArr.isPackage = value.isPackage;
																	newArr.departmentId = value.specialityId;
																	newArr.subDepartmentId = value.subSpecialityId;
																	newArr.serviceRendered = 0;
																	newArr.isPackage = value.isPackage;
																	newArr.isPackageService = value.isPackageService;
																	newArr.orderDetailsId=value.orderDetailsId;
																	newArr.orderId=value.orderId;
																	newArr.isInvestigationOrder=value.orderDetailsId!="" && value.orderDetailsId!=undefined && value.orderDetailsId!=null ? 'Y' : 'N';
																	newArr.orderDetailsPackageMapperDtosList = value.orderDetailsPackageMapperDtosList;
																	newArr.orderDetailsPayeeMapperDtosList=value.orderDetailsPayeeMapperDtosList;
																	finalOrderServiceList.push(newArr);
																});
												data.listOrderDetailsMasterDto = finalOrderServiceList;

												listOrderMasterDto.push(data);
												finalData.listOrderMasterDto = listOrderMasterDto;
												console.log("INV", JSON.stringify(finalData));
												if ($scope.addedServicePackageOrdersForDisplay[0].packageId != undefined
														&& $scope.addedServicePackageOrdersForDisplay[0].packageId != "") {
													var URI = BASE_URL + ROOT_URL
															+ SAVEINVESTIGATION
													GenericServiceParamHeader
															.serviceAction(METHOD_POST, URI,finalData,NOTIFICATION_MSG_STATUS_TRUE).then(
																	function(response) {
																		if (response.data.status == "success") 
																		{
																			$scope.addedServiceOrdersForDisplay = [];
//																			$scope.getListPackageOrder();
																			$scope.packageSearchObj.serviceName = "";
																			$scope.selectedServiceTypeSpecialityId = "";
																			$scope.selectedSubServiceTypeSubSpecialityId = "";
																			$scope.selectedOrderSet=[];
																			$scope.packageId = "";
																			$scope.packageResultObj={};
																			$scope.init();
																			$scope.getListPackageOrder();
																			$scope.getActivePackagesList();
																			$scope.setSecondTabLevel(9);

																		}
																	});
												} else {
													growl.warning("Please add Package!!", {
														title : 'Warning!'
													});
													$rootScope.stopLoader();
												}
											  }
											}
											
										
											
											//getting packing list 
											$scope.getListPackageOrder = function() {

											    if($scope.encounterDetailsObj !== undefined && $scope.encounterDetailsObj !== null){
                                                    if($scope.encounterDetailsObj.patientId === undefined || $scope.encounterDetailsObj.patientId === null || $scope.encounterDetailsObj.patientId ===''){
                                                        $scope.encounterDetailsObj.patientId = 0;
                                                    }
                                                }
                                                else{
                                                    $scope.encounterDetailsObj.patientId = 0;
                                                }
												var URI = BASE_URL+ ROOT_URL
														+ "/api/packages/getPackageListForIPDPackageOrder/"+$scope.encounterDetailsObj.patientId+"/"+ $scope.encounterDetailsObj.encounterId
														+ "/" + $scope.organizationId + "/"+ $scope.unitId+"/1";

												GenericService.serviceAction("GET", URI).then(function(response) {
																	$rootScope.stopLoader();
																	$scope.listPackageOrder = [];
																	$scope.listPackageOrder = response.data.listObject !=null ? response.data.listObject : []  ;
																	console.log("ListPackageOrderresult",$scope.listPackageOrder);

																});
											}
											$scope.getListPackageOrder();
											
											//package transaction view start
											$scope.getAllPackageList=function(){
												
												  $('#commonPackageDetailsPopup').modal('show');
											}
											
											$scope.getPackagesDetails = function(packageId,consumptionMasterId)
											{
												 
												var data={};
												data[consumptionMasterId]=packageId;   
											
											//get package details
												var URI = BASE_URL + ROOT_URL + "/api/packages/bill/package";
												BillingGenericService.serviceAction("POST", URI, data).then(function(response){
													if(response.data.status == "success")
														{
														
														   console.log(response.data.listObject);
														   $scope.packageTypeId = response.data.listObject[0].mPackageMasterDto.packageTypeId;
														   if($scope.packageTypeId==1 || $scope.packageTypeId==5){
															   $scope.setFirstTabLevel(101);
														   }
														   else if($scope.packageTypeId==2){
															   $scope.setFirstTabLevel(401);
														   }
														   else if($scope.packageTypeId==3){
															   $scope.setFirstTabLevel(201);
														   }else if($scope.packageTypeId==4){
															   $scope.setFirstTabLevel(301);
														   }
														   $scope.packageDetailsResponse=response.data.listObject[0];
														   $('#packageServiceDetailsPopup').modal('show');
													     /*  if($scope.packageDetailsList!=undefined && $scope.packageDetailsList.length>0 && response.data.listObject!=null)
													    	   {
													    	   angular.forEach(response.data.listObject,function(value,key){
													    		   var found = $scope.packageDetailsList.find(ar=>ar.mPackageMasterDto.packageMasterId==value.mPackageMasterDto.packageMasterId);
													    		   if(found==undefined)
																   {
													    			   $scope.packageDetailsList.push(response.data.listObject.mPackageMasterDto);
																   }
																});
													    	   }
													       else{
													    	     $scope.packageDetailsList = response.data.listObject;
													          }*/
														}
														
												});
											}
											
											
											//Check Service in exist
											  var isServiceExistsForNotSelected;
												function checkServiceIdExistsForNotSelected(arr, val) {
													if(arr != null && arr.length >0){
													return arr.some(function(arr) {
																if(arr.serviceMasterId === val && arr.serviceRendered == 0 && arr.orderStatus == 'A')
																	{
																		return true;
																	}
																else
																	{
																		return false;
																	}
															});
													}else{
														 return false;
														}
												}
												
												 var isServiceExistsForNotSelected;
													function checkServiceIdExistsForNotSelectedTemeporaryAdd(arr, val) {
														if(arr != null && arr.length >0){
														return arr.some(function(arr) {
																	if(arr.serviceMasterId === val && arr.serviceRendered == 0 && arr.status == 'A')
																		{
																			return true;
																		}
																	else
																		{
																			return false;
																		}
																});
														}else{
															 return false;
															}
													}
											
								        $scope.addInvestigation = function(investigation) {
								        	$scope.editserviceHideFlag=false;
								        	//validate service in current added package
								        	    $rootScope.startLoader();
								        		var newArr = {};
								        		if($scope.orderServiceId!=undefined && $scope.orderServiceId !="" ){
								        		newArr.specialityId = $scope.orderSpecialtyId;
								            	newArr.serviceSpecialityId = $scope.orderSpecialtyId;
								          		newArr.serviceSubSpecialityId =$scope.orderSubSpecialtyId;
								        		newArr.subSpecialityId = $scope.orderSubSpecialtyId;
								        		newArr.serviceMasterId = $scope.orderServiceId;
								        		newArr.serviceStandardName = $scope.orderServiceName;
								        		newArr.specialityName = $scope.orderSpecialtyName;
								            	newArr.subSpecialityName = $scope.orderSubSpecialtyName;
								            	newArr.serviceRenderingDeptId = $scope.orderSubSpecialtyId;
								            	newArr.createdBy  = $scope.userId;
								        		newArr.createdDate = new Date().getTime();
								        		newArr.selfPayable =  "0";
								        		newArr.creditPayable =   "0";
								        		newArr.serviceRendered = "0";
								        		newArr.doctorId = $scope.encounterDetailsObj.doctorId;
								        		newArr.discountPercentage = 0;
								        		newArr.discount =0;
								        		newArr.quantity = 1;
								        		newArr.basePrice =  0;
								        		newArr.basePriceOld =  0;
								        		newArr.totalAmt = 0;
								        		newArr.concession = 0;
								            	newArr.selectedForFuture = $scope.selectedForFuture;
								            	newArr.payeeId = $scope.encounterDetailsObj.payeeId;
								    			newArr.apportionedPrice = null;
								        		newArr.packageId = null;
								        		newArr.packageTypeId = null; 
								        		newArr.packageEitherOrGroupId = null; 
								        		newArr.packageCapDetailsId = null;
								        		newArr.isCapService = null;
								        		newArr.status = 'A';
								        		newArr.isRefDoctorShare = null;
								        		newArr.packageEitherOrGroupId = null;
								        		newArr.isPackageService ='N';
								        		newArr.discountDetailsList = [];
								        		newArr.isRefDoctorShare = $scope.orderIsRefDoctorShare;
								        		newArr.isAppointable=$scope.isAppointable;
								        		newArr.orderDetailsIdP= $scope.orderDetailsIdP;
								        		newArr.investigationFutureDateToShowdddd = moment(new Date()).format('DD/MM/YYYY');
								        		if($scope.investigationFutureDate != "" && $scope.investigationFutureDate != undefined && $scope.selectedForFuture == "Y" ){
								        		newArr.investigationFutureDate = $scope.investigationFutureDate
								        	
								        		newArr.investigationFutureDateToShowdddd = moment($scope.investigationFutureDate).format('DD/MM/YYYY')
								        		}
								        		
								        		if( $scope.investigationFutureDate == undefined && $scope.selectedForFuture == "Y"){
								        			
								        			growl.warning("Please select Date !!",
													   		{
																title : 'Warning!'
															});
															$rootScope.stopLoader();
															return false;
								            		}
								            		
								        		
								                if( $scope.investigationFutureDate == ""  && $scope.selectedForFuture == "Y"){
								        			
								        			growl.warning("Please select Date !!",
													   		{
																title : 'Warning!'
															});
															$rootScope.stopLoader();
															return false;
								            		}
								                $scope.dv = new Date();
								                if($scope.investigationFutureDate != "" && $scope.investigationFutureDate != undefined && $scope.selectedForFuture == "Y"){
								                 if( $scope.investigationFutureDate < $scope.dv){
								        			
								        			growl.warning("Please select greater than todays date !!",
													   		{
																title : 'Warning!'
															});
															$rootScope.stopLoader();
															return false;
								            		}
								                }
								        		
								       /* 		if($scope.search ==''||$scope.selectedServiceTypeSpecialityId == ''||$scope.selectedSubServiceTypeSubSpecialityId == '')
												{
												 growl.warning("Please select Service !!",
												   		{
															title : 'Warning!'
														});
												 
												 	$scope.selectedForFuture = 'N';
									        		$scope.fromDatepickerForFuture='';
									        		$scope.investigationFutureDate = "";
									            	$rootScope.stopLoader();
									        		return false;
												}
								        		else{*/
								        			
								        			if($scope.editserviceHideFlag == true){
								        				$scope.addedServiceOrdersForDisplay.splice($scope.editInvestigation , 1);
								        			}
								        			isServiceExistsForNotSelected=checkServiceIdExistsForNotSelected($scope.currentEncounterOrderLists,$scope.orderServiceId)
													  if (!isServiceExistsForNotSelected) {
														  
														  isServiceExistsForNotSelected=checkServiceIdExistsForNotSelectedTemeporaryAdd($scope.addedServiceOrdersForDisplay,$scope.orderServiceId)
														  if (!isServiceExistsForNotSelected) {
															  $scope.addedServiceOrdersForDisplay.push(newArr);
															    $scope.search = '';
												            	$scope.selectedServiceTypeSpecialityId='';
												            	$scope.selectedSubServiceTypeSubSpecialityId='';
												            	$scope.selectedForFuture = 'N';
												            	$scope.investigationFutureDate='';
												        		$scope.fromDatepickerForFuture='';
												        		$scope.saveInvestigation();

														  }
														  else{
															  $scope.orderDetailsIdP=null;
															  angular.element('#pendingServiceAdd').modal('show');
														  }
														  
													  }
													  else
														  {
														    $scope.orderDetailsIdP=null;
														    angular.element('#pendingServiceAdd').modal('show');
														  }
								        			
								        			
//								        		  }
								            	   $rootScope.stopLoader();
								        		}
								        		else 
								        		{
									        		growl.warning("Please select Service !!",
															{
																title : 'Warning!'
															});
									        		$scope.selectedForFuture = 'N';
									        		$scope.fromDatepickerForFuture='';
									        		$scope.investigationFutureDate = "";
									            	$rootScope.stopLoader();
									        		return false;
									        	}
								 
										}
								        
								    	/**============================================================add order set ****/
								     	$scope.orderSetList = [];
								       	$scope.diagnosisList = [];
								       	$scope.selectedOrderSet = [];
								       	$scope.orderSetOrderList = [];
//								       	$scope.ordersetFlag=false;
//								       	$scope.ordersetButton=false;
								       	$scope.addbutDis=false;
								       	$scope.getOrderset = function(index) 
								    	{
								       		
								       		if($scope.diagnosisIdForOrderSet==0)
								       			{
								       			growl.warning("Please select Diagnosis first..!",{
								    				title : 'Warning!'
								    			 });
								       			return;
								       			}
								       		
								       		$scope.orderSetIndex = index;
								    		$rootScope.startLoader();
								        	var data={
								        		  ordersetMasterId:[]	,
								        		  orgId : $scope.organizationId,
								        		  unitId : $scope.unitId,
								        		  doctorId : $scope.encounterDetailsObj.doctorId,
								        		  specialityId : $scope.encounterDetailsObj.specialityId,
								        		  diagnosisId:$scope.diagnosisIdForOrderSet
								        		}
								        
								        	
								        	console.log('data',JSON.stringify(data));
								    		var URI = BASE_URL + ROOT_URL + GET_ORDER_SET;
								    		BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
								    				{
								    			   if(response.data.status=='success')
								    			      {
								    				    $scope.orderSetList = response.data.listObject;
								    				    console.log("yes order set ",$scope.orderSetList);
								    				
								    				    if($scope.orderSetList!=null && $scope.orderSetList.length>0)
														{
															$scope.counts=0;
															$scope.countcheck =$scope.orderSetList.length;
															$scope.selectedlist = $scope.selectedOrderSet;
															angular.forEach($scope.selectedlist,function(value,index){
																
																angular.forEach($scope.orderSetList,function(value1,index1){
																	if(value == value1.ordersetMasterId)
																		{
																		$scope.counts++;
																		}
																	
																});
															});
															
															if($scope.addSelectedOrderSet == true){
																if($scope.counts == $scope.countcheck){
																	$scope.addbutDis=true;
																}else{
																	$scope.addbutDis=false;
																}
															}
															$( '#addOrdersetModal').modal('show');
														}
								    				    else{
								    				    	growl.warning(
																	"No order set mapped with this diagnosis ..!",
																	{
																		title : 'Warning!'
																	});
								    				    	$rootScope.stopLoader();
															return;
								    				    }
								    				   	$rootScope.stopLoader();
								    			      }
								    			   else if(response.data.status=='error'){
								    				   $rootScope.stopLoader();
								    				   growl.warning("Something went wrong...",{
										    				title : 'Warning!'
										    			 });
								    			   }
								    				
								    		});
								    	}
								       	
								    	$scope.addDuplicateSerives =function()
										{
											$scope.addedServiceOrdersForDisplay = $scope.addedServiceOrdersForDisplay.concat($scope.dublicateSerivceList);
										}
								        
								    	$scope.totalOrderSetids = [];
										$scope.fromOrderSetServices = false;
										$scope.addSelectedOrderSet = false;
										$scope.addSelectedOrderSetPopUp = function(index)
										{
											if ($scope.selectedOrderSet.length == 0) {
												growl.warning("Please select Order set..!", {
													title : 'Warning!'
												});
												return;
											}
											else{ 
												$scope.addSelectedOrderSet = true;
												 $scope.dataListDiagnosis[index].selectedOrderSet = $scope.selectedOrderSet;
											    }
											
											var selectedOrderSet = [];
											var dataDiagnosisList = $scope.dataListDiagnosis;
											
											for (var i = 0; i < dataDiagnosisList.length; i++) 
											{
												if($scope.dataListDiagnosis[i].selectedOrderSet!=null && $scope.dataListDiagnosis[i].selectedOrderSet.length>0)
													{
													var data = {
															ordersetMasterId : $scope.dataListDiagnosis[i].selectedOrderSet,
															orgId : $scope.organizationId,
															unitId : $scope.unitId,
															doctorId : $scope.encounterDetailsObj.doctorId,
															specialityId :  $scope.encounterDetailsObj.specialityId,
															diagnosisId : $scope.dataListDiagnosis[i].diagnosisId
														}
													  selectedOrderSet.push(data);
													}
										
												
												$scope.dataListDiagnosis[i].diagnosisName = '';
												$scope.dataListDiagnosis[i].type = '';
												$scope.dataListDiagnosis[i].remark = '';
												$scope.dataListDiagnosis[i].diagnosisNameText = '';
												$scope.dataListDiagnosis[i].isDiagnosisName = 'N';
											}
											
											
											if(selectedOrderSet.length>0)
												{
												$scope.addOrderSetOrders(selectedOrderSet);
												}
										}
										

										$scope.addOrderSetOrders = function(orderSetObjList)
								       	{
											$scope.dublicateSerivceList = [];
							       		if($scope.selectedOrderSet.lengh==0)
						       			{
						       			growl.warning("Please select Order set..!",{
						    				title : 'Warning!'
						    			 });
						       			return;
						       			}
						    		$rootScope.startLoader();
						        	var data={
						        		  ordersetMasterId:$scope.selectedOrderSet	,
						        		  orgId : $scope.organizationId,
						        		  unitId : $scope.unitId,
						        		  doctorId : $scope.encounterDetailsObj.doctorId,
						        		  specialityId : $scope.encounterDetailsObj.specialityId,
						        		  diagnosisId:$scope.diagnosisIdForOrderSet
						        		}
						        
						        	
						    		var URI = BASE_URL + ROOT_URL + GET_ORDER_SET_ORDERS;
						    		console.log('dataget',JSON.stringify(data));
						    		console.log('URI',URI);
						    		BillingGenericService.serviceAction(METHOD_POST, URI, orderSetObjList,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
						    				{
						    			   if(response.data.status=='success')
						    			      {
						    				    $scope.orderSetOrderList = response.data.listObject;
						    				    console.log('$scope.orderSetOrderLis',JSON.stringify($scope.orderSetOrderList));
						    				    angular.forEach($scope.orderSetOrderList, function(value, key) 
						    				    		{
						    				    	
						    				    	  angular.forEach(value.ordersetInvestigations, function(value, key) 
								    				    		{
						    				    		  if($scope.addedServiceOrdersForDisplay!=null)
						    				    			  {
													        	var newArr = {};
													        	newArr.serviceId = value.serviceId;
																newArr.specialityId = value.specialityId;
																newArr.selectedForFuture = 'N';
																newArr.specialityName = value.specialityName;
																newArr.orderSetId = value.ordersetMasterId;
																newArr.subSpecialityId = value.subSpecilaityId;
																newArr.serviceStandardName = value.serviceStandardName;
																newArr.selectedForFuture = $scope.selectedForFuture;
																
																newArr.specialityId = value.specialityId;
																newArr.serviceSpecialityId = value.specialityId;
																newArr.subSpecialityId = value.subSpecialityId;
																
																newArr.serviceMasterId = value.serviceId;
																newArr.specialityName = value.specialityName;
																newArr.serviceRenderingDeptId = value.subSpecialityId;
																newArr.serviceSubSpecialityId = value.subSpecialityId;
																
																newArr.isAppointable = value.isAppointable;
																newArr.createdBy = $scope.userId;
																newArr.createdDate = new Date()
																		.getTime();
																newArr.selfPayable = "0";
																newArr.creditPayable = "0";
																newArr.serviceRendered = "0";
																newArr.doctorId = $scope.encounterDetailsObj.doctorId;
																newArr.discountPercentage = 0;
																newArr.discount = 0;
																newArr.quantity = 1;
																newArr.basePrice = 0;
																newArr.basePriceOld = 0;
																newArr.totalAmt = 0;
																newArr.concession = 0;
																newArr.payeeId = $scope.encounterDetailsObj.payeeId;
																newArr.apportionedPrice = null;
																newArr.packageId = null;
																newArr.packageTypeId = null;
																newArr.packageEitherOrGroupId = null;
																newArr.packageCapDetailsId = null;
																newArr.isCapService = null;
																newArr.status = 'A';
																newArr.isRefDoctorShare = value.isRefDoctorShare;
																newArr.packageEitherOrGroupId = null;
																newArr.isPackageService = 'N';
																newArr.discountDetailsList = [];
																$scope.fromOrderSetServices = true;
																newArr.investigationFutureDateToShowdddd = moment(new Date()).format('DD/MMM/YY');
																isServiceExistsForNotSelected=checkServiceIdExistsForNotSelected($scope.currentEncounterOrderLists,value.serviceId)
																	  if (!isServiceExistsForNotSelected) 
																	  {
																		  isServiceExistsForNotSelected=checkServiceIdExistsForNotSelectedTemeporaryAdd($scope.addedServiceOrdersForDisplay,value.serviceId)
																		  if (!isServiceExistsForNotSelected) 
																		  {
																			  $scope.addedServiceOrdersForDisplay.push(newArr);
																			  
																		  }
																		  else{
																			  $scope.addedServiceOrdersForDisplay.push(newArr);
																		  }
																	  }
																	  else
																		  {
																		    $scope.addedServiceOrdersForDisplay.push(newArr);
																		  }
						    				    			         }
								    				    	    });
								    				    
						    				    	  
						    				    	 angular.forEach(value.ordersetMedications, function(value, index1) 
								    				    		{
						    				    		  if($scope.medicationDetails!=null && $scope.medicationDetails.length==1 &&$scope.medicationDetails[0].itemId=='' )
					    				    			  {
					    				    			     $scope.medicationDetails = [];
					    				    			  }
								    				    		});
						    				    	  angular.forEach(value.ordersetMedications, function(value, index) 
								    				    		{
						    				    		  if($scope.medicationDetails!=null)
						    				    			  {
						    				    			    $scope.medicationObj.itemId = value.itemMasterId;
						    				    			    $scope.medicationObj.doseUnitId = value.doseUnitId;
						    				    			    $scope.medicationObj.dose = value.dose;
						    				    			    $scope.medicationObj.roaId = value.routeId;
						    				    			    $scope.medicationObj.frequencyId = value.frequencyId;
						    				    			    $scope.medicationObj.formulationTypeId= value.formulationTypeId;
						    				    			    $scope.medicationObj.medicationStartDate = moment(new Date()).format('YYYY-MM-DD');
																$scope.medicationObj.itemName = value.itemName;
																$scope.medicationObj.roaDesc =value.roaDesc;
																$scope.medicationObj.strengthDesc =value.strengthDesc;
																$scope.medicationObj.frequencyDesc =value.frequencyDesc;
																$scope.medicationObj.priorityId =1;
																$scope.medicationObj.genericName = value.genericName;
																$scope.medicationObj.searchGeneric = value.genericName;
																$scope.medicationObj.genericId=value.genericId;
																$scope.medicationObj.searchItem = value.itemName;
																$scope.medicationObj.medicationName = value.itemName;
														        $scope.medicationObj.searchItemFre = value.frequencyDesc;
														        $scope.medicationObj.instructionId = value.instructionId;
														        $scope.medicationObj.prescribedDosageUnitList=[];
																$scope.medicationObj.durationCount =5;
																$scope.medicationObj.formulationCounterIndex="";
																$scope.medicationObj.durationUnit = "Day";
																var sDT =  moment((new Date()),"DD-MM-YYYY").toDate();
							                                    var startDT =  moment(sDT).format('YYYY-MM-DD');
							                                    $scope.medicationObj.medicationStartDate = startDT;
							                                    
							                                    if($scope.medicationObj.durationUnit == "Day") {
																	var durationCount = parseInt($scope.medicationObj.durationCount);
																	}
																    else if($scope.medicationObj.durationUnit == "Week") {
																		var durationCount = parseInt($scope.medicationObj.durationCount)
																		$scope.medicationObj.durationCount = durationCount* 7;
																	}
																	else if($scope.medicationObj.durationUnit == "Month") {
																		var durationCount = parseInt($scope.medicationObj.durationCount)
																		$scope.medicationObj.durationCount = durationCount* 30;
																	}
																	else if($scope.medicationObj.durationUnit == "Year") {
																		var durationCount = parseInt($scope.medicationObj.durationCount)
																		var months = durationCount * 12
																		$scope.medicationObj.durationCount = months* 30;
																	}
															
															if ($scope.medicationObj.durationCount && $scope.medicationObj.durationUnit && $scope.medicationObj.medicationStartDate) {
																if($scope.medicationObj.durationUnit == "Day") {
																	var endDate = moment($scope.medicationObj.medicationStartDate).add($scope.medicationObj.durationCount-1, 'days').format('YYYY-MM-DD');
																}
																else if($scope.medicationObj.durationUnit == "Week") {
																	var days = $scope.medicationObj.durationCount * 7
																	var endDate = moment($scope.medicationObj.medicationStartDate).add($scope.medicationObj.durationCount, 'days').format('YYYY-MM-DD');
																}
																else if($scope.medicationObj.durationUnit == "Month") {
																	var days = $scope.medicationObj.durationCount * 30
																	var endDate = moment($scope.medicationObj.medicationStartDate).add($scope.medicationObj.durationCount, 'days').format('YYYY-MM-DD');
																}
																else if($scope.medicationObj.durationUnit == "Year") {
																	var days = $scope.medicationObj.durationCount * 365;
																	var endDate = moment($scope.medicationObj.medicationStartDate).add($scope.medicationObj.durationCount, 'days').format('YYYY-MM-DD');
																}
																$scope.medicationObj.medicationEndDate = endDate;
															}    
															angular.forEach($scope.frequencyMasterDataList1,function(value,key){
																$scope.itemaa= {}
																$scope.itemaa.frequency_id=value.id,
																$scope.itemaa.frequency=value.description,
																$scope.itemaa.noOfAdministrationTime=value.noOfAdministrationTime,
																$scope.model=value.description;
																$scope.label=value.description;
															})
															
															$scope.medicationObj.favorite_status =false;
															$scope.addFrequencyItemList($scope.itemaa, $scope.model, $scope.label,false);
														        
																  var URI = BASE_URL + ROOT_URL + "/api/opd/getRoaList" + "?unitId=" + 
																	$scope.unitId + "&orgId=" + $scope.organizationId + "&formulationTypeId=" + $scope.medicationObj.formulationTypeId+ "&genericId=" + $scope.medicationObj.genericId+ "&patientId=" +$scope.encounterDetailsObj.patientId;
																
																	GenericServiceParamHeader.serviceAction(METHOD_GET, URI).then(function(response) {
																	if(response.data.status == "success")
																	{
																		var idx = $scope.medicationDetails.findIndex(item => item.itemId == value.itemMasterId);
																		$scope.medicationDetails[idx].roaList = response.data.listObject;
																	}});
																	 if($scope.medicationObj.instructionId != null){
																			var instructionDesc = $
																					.grep(
																							$scope.medicationInstructions,
																							function(disCat) {
																								return disCat.instructionId == $scope.medicationObj.instructionId;
																							})[0].instructionDesc;

																			$scope.medicationObj.instructionDesc = instructionDesc;
											                                }else{
											                                	$scope.medicationObj.instructionDesc = "No instruction";
											                                }
																	 
																	
															
																		
																$scope.medicationObj.nonFormularyAdvice='N' ;
																$scope.medicationObj.isGeneric='N' ;
																$scope.medicationObj.isInstruction='N' ;
																
																$scope.medicationObj.frequencyId=value.frequencyId;
																$scope.medicationObj.frequencyDesc=value.frequencyDesc;
																$scope.medicationObj.noOfAdministrationTime=value.noOfAdministrationTime;
																$scope.medicationObj.durationUnit='Day';
													    		$scope.medicationObj.quantity = $scope.medicationObj.quantity;
													    		var checkFlagValidation = true;
															    $scope.medicationObj.checkFlagValidation = checkFlagValidation;
															    
																var found = $scope.medicationDetails.find(ar=>ar.itemId==value.itemMasterId);
																   if(found==undefined)
																	   {
																	      $scope.medicationDetails.push($scope.medicationObj);
																	      console.log("testOrderSet" ,$scope.medicationDetails);
																	      
																	   }
																   
																   
																$scope.medicationObj={};
						    				    			  }
						    				    		
								    				    	 });
						    				    	  
						    				  		
						    				    	  angular.forEach($scope.medicationDetails,function(value,idx){
						    				    		//get formuation dosage list
						    				    	$scope.prescribedDosageUnitListorderswet=[];
						    				  			var dataobj = {
																organizationId : $scope.organizationId,
																unitId : $scope.unitId,
																formulationTypeId: value.formulationTypeId
														};
														value.prescribeDose="";
														var URIForPresc = BASE_URL + ROOT_URL+ "/api/global/getFormulationTypeToDosageMapperListByFormulationTypeId";
														GenericService.serviceAction("POST", URIForPresc, dataobj).then(function(response) {
																	$rootScope.stopLoader();
																	$scope.counterFormulation=1;
																	$scope.medicationDetails[idx].formulationCounterIndex="";
																	$scope.prescribedDosageUnitListorderswet=[];
																	angular.forEach(response.data.listObject,function(value,index){
																		var unitObj={};
																		unitObj.formulationDosageFromUnit=value.fromConversionUnit;
																		unitObj.formulationDosageFromUnitId=value.fromConversionFactorUnit;
																		unitObj.formulationDosageToUnit=value.toConversionUnit;
																		unitObj.formulationDosageToUnitId=value.toConversionFactorUnit;
																		unitObj.counterIndex=$scope.counterFormulation++;
																		unitObj.formulationDosageFromConversionValue=value.fromConversionFactor;
																		unitObj.formulationDosageToConversionValue=value.toConversionFactor;
																		unitObj.formulationFactorId=value.factorId;
																		unitObj.formulationDosage=value.dosageForm;
																		unitObj.formulationDosageMappId=value.formulationDosageMappId;
																		$scope.prescribedDosageUnitListorderswet.push(unitObj);
																	});
																	
																	angular.forEach(response.data.listObject,function(value,index){
																		var unitObj={};
																		if(value.factorId==1){
																			unitObj.formulationDosageFromUnit=value.toConversionUnit;
																			unitObj.formulationDosageFromUnitId=value.toConversionFactorUnit;
																			unitObj.formulationDosageToUnit=value.fromConversionUnit;
																			unitObj.formulationDosageToUnitId=value.fromConversionFactorUnit;
																			unitObj.counterIndex=$scope.counterFormulation++;
																			unitObj.formulationDosageFromConversionValue=value.toConversionFactor;
																			unitObj.formulationDosageToConversionValue=value.fromConversionFactor;
																			unitObj.formulationFactorId=value.factorId;
																			unitObj.formulationDosage=value.dosageForm;
																			unitObj.formulationDosageMappId=value.formulationDosageMappId;
																			$scope.prescribedDosageUnitListorderswet.push(unitObj);
																		}
																	});
																	value.prescribedDosageUnitList=$scope.prescribedDosageUnitListorderswet;
																	var list=[];
																	if(value.prescribedDosageUnitList.length>0 && value.prescribedDosageUnitList[0].formulationFactorId==1){
																		 list=$.grep(value.prescribedDosageUnitList, function (obj) {
																	        return value.doseUnitId === obj.formulationDosageFromUnitId;
																	    });
																		if(list.length>0){	
																			value.formulationCounterIndex=list[0].counterIndex;
																		}
																	}
														       });
						    				    	        });
						    				    	    });
						    				    
									    				    $scope.saveInvestigation();
															$timeout(function() {
																$scope.saveMedication();
															}, 2000);
												
						    			/*		 $timeout(function () {

														if($scope.dublicateSerivceList.length>0)
														{
														   angular.element('#dublicateServiceAddOPD').modal('show');
														}

													 }, 3000);*/
						    				    $('#addOrdersetModal').modal('hide');
						    				   /* growl.success("Orders Added Successfully.",{
								    				title : SUCCESS_MSG
								    			 });*/
						    				   	$rootScope.stopLoader();
						    			      }
						    			   else if(response.data.status=='error'){
						    				   $rootScope.stopLoader();
						    				   growl.warning("Something went wrong...",{
								    				title : 'Warning!'
								    			 });
						    			   }
						    				
						    		});
							       	}
								        
//								        investigationFutureDate
								        $scope.saveInvestigation = function() {
								        	
								        	$rootScope.startLoader();
								        	var listOrderMasterDto = [];
								        	var finalData = {
								        			orgId : $scope.organizationId,
								        			orgUnitId:$scope.unitId
								        	};
								        	var data = {
								        			
								        			currentDate : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
								        			createdDate:new Date().getTime(),
								        			updatedDate:new Date().getTime(),
								        			orgId : $scope.organizationId,
								        			orgUnitId:$scope.unitId,
								        			createdBy  : $scope.userId,
								    				updatedBy : $scope.userId,
								    				patientId : $scope.encounterDetailsObj.patientId,
								    				encounterId : $scope.encounterDetailsObj.encounterId,
								    				defaultSelfTariffId : $scope.defaultSelfTariffId,
								    				paymentEntitlementTypeId : $scope.encounterDetailsObj.paymentEntitlementTypeId,
								    				doctorId :$scope.encounterDetailsObj.doctorId,
								    				ordDocSplId :$scope.encounterDetailsObj.specialityId,
								    				priorityId : 2,
								    				visitTypeId : 1,
								    				orderDate:new Date().getTime(),
								    				orderStatus:'A',
								    				orderStatusId:1,
								    				contractId : $scope.encounterDetailsObj.contractId,
								    				payeeId : $scope.encounterDetailsObj.payeeId,
								    				billingBedCategoryId:$scope.opdBillingCatId,
								    				patientTypeId:$scope.patientDetails.patientCategoryId,
								    				
								        	}
								        	var data1 = {
								        			
								        			currentDate : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
								        			createdDate:new Date().getTime(),
								        			updatedDate:new Date().getTime(),
								        			orgId : $scope.organizationId,
								        			orgUnitId:$scope.unitId,
								        			createdBy  : $scope.userId,
								    				updatedBy : $scope.userId,
								    				patientId : $scope.encounterDetailsObj.patientId,
								    				defaultSelfTariffId : $scope.defaultSelfTariffId,
								    				paymentEntitlementTypeId : $scope.encounterDetailsObj.paymentEntitlementTypeId,
								    				doctorId :$scope.encounterDetailsObj.doctorId,
								    				ordDocSplId :$scope.encounterDetailsObj.specialityId,
								    				priorityId : 2,
								    				visitTypeId : 1,
								    				orderDate:new Date().getTime(),
								    				orderStatus:'A',
								    				orderStatusId:1,
								    				billingBedCategoryId:$scope.opdBillingCatId,
								    				patientTypeId:$scope.patientDetails.patientCategoryId,
								        	}
								        	
								        	
								        	
								        	var finalOrderServiceList = [];
								        	var finalOrderServiceListForFuture = [];
								        	var justChkFlgFut = 0;
								        	var justChkFlgCurr = 0;
								        	angular.forEach($scope.addedServiceOrdersForDisplay, function(value , key) {
								        		var newArr = {};
								        		// newArr.encounterId =$scope.encounterDetailsObj.encounterId ;
								        		if(value.selectedForFuture == 'N')
								        			{
//								        		newArr.ordDocId =$scope.encounterDetailsObj.doctorId ;
								        		newArr.serviceId = value.serviceMasterId;
//								        		newArr.ordDocSplId =$scope.encounterDetailsObj.specialityId;
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
								        		newArr.procedureOrderMstId = $scope.proceduerReqId;
								        		finalOrderServiceList.push(newArr);
								        		justChkFlgCurr = 1;
								        		}
								        		if(value.selectedForFuture == 'Y')
								    			{
//								        			newArr.ordDocId =$scope.encounterDetailsObj.doctorId ;
								        			newArr.serviceId = value.serviceMasterId;
//								        			newArr.ordDocSplId =$scope.encounterDetailsObj.specialityId;
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
//								            		newArr.orderSchDate= moment(value.investigationFutureDate.getTime()).format('HH:mm:ss');
								            		newArr.nursingServiceOrderFlag ='N';
								            		newArr.procedureOrderMstId = $scope.proceduerReqId;
								        			finalOrderServiceListForFuture.push(newArr);
								        			justChkFlgFut = 1;
								        			
									    		}
								        	});
								        	data.listOrderDetailsMasterDto = finalOrderServiceList;
								        	data1.listOrderDetailsMasterDto = finalOrderServiceListForFuture;
								        	
								        	if(justChkFlgCurr == 1)
								        	listOrderMasterDto.push(data);
								        	
								        	if(justChkFlgFut == 1)
								        	listOrderMasterDto.push(data1);
								        	finalData.listOrderMasterDto = listOrderMasterDto;
								        	// add modality list
								        	var slotMasterDtoList = [];
								        	finalData.slotMasterDtoList=$scope.radiologyAppointmentList;
								        	console.log("INV",finalData);
								        	
								        	if(listOrderMasterDto.length > 0){
								        	 //return false;
											var URI = BASE_URL + ROOT_URL + SAVEINVESTIGATION
											GenericServiceParamHeader.serviceAction(METHOD_POST, URI, 
													finalData, NOTIFICATION_MSG_STATUS_TRUE)
													.then(function(response) {
														if(response.data.status == "success"){
															$scope.addedServiceOrdersForDisplay = [];
//															$scope.orderServiceId="";
//															$scope.init();
															$scope.getInvestigationList();
//															$scope.orderSetOrderList=[];
															$scope.selectedOrderSet=[];
															$scope.slotDetailsList = [];
															$scope.proceduerReqId = undefined;
															$scope.fromTimeStringSlot='';
															$scope.modalityMasterList = [];
															$scope.modalityId='';
															$scope.radiologyAppointmentList=[];
															$scope.fromOrderSetServices= false;
															$scope.editserviceHideFlag=false;
															$rootScope.stopLoader();
															
														}
														
											});
								        	}
								        	else {
								        		growl.warning("Please add investigation and then save !!",
														{
															title : 'Warning!'
														});
								        		$rootScope.stopLoader();
								        	}
								        	
										}
								        $scope.icpcMasterList=[];
						
										
/** ========================================================PROCEDURE* ORDER TAB* START====================================================================*/
								    	
										
										

										$scope.dateOptionsProc = {
											formatYear : 'yyyy',
											showWeeks : false,
											minDate : new Date(),
											
										};

										$scope.procedureOpen = {
											opened : false
										};

										$scope.openproc = function() {
											$scope.procedureOpen.opened = true;
										};
										
			                            // function for initializa clockpicker
			                            $scope.initClockPicker = function() {

			                                $('.clockpicker').clockpicker();
			                            }

			                        	// ********************start initial
										// assesment********************************************//
										// function for initializa clockpicker
										$scope.openDatePickerlastDayOfBowelMovement = function() {
											$scope.datepickerlastDayOfBowelMovement.opened = true;
									      };
									      
									      $scope.datepickerlastDayOfBowelMovement = {
													opened : false
											};
										
										$scope.openDatePickerMenstrual = function() {
											$scope.datepickerMenstrual.opened = true;
									      };

										$scope.datepickerMenstrual = {
												opened : false
										};
										
										
										$scope.initClockPicker = function() {
											$('.clockpicker').clockpicker();
										}
										$scope.minusCommunication = true;
										$scope.minusInvasive = true;
										$scope.minusPsychosocial = true;
										$scope.minusPain = true;
										$scope.minusRest = true;
										$scope.minusNutrition = true;
										$scope.minusFall = true;
										$scope.minusVitals = true;
										$scope.minusOral = true;
										$scope.minusNeurological = true;
										$scope.minusCardiovascular = true;
										$scope.minusRespiratory = true;
										$scope.minusGastrointestinal = true;
										$scope.minusIntegumentary = true;
										$scope.minusGenitourinary = true;
										$scope.minusMusculoskeletal = true;
										
										$scope.plusAndMinus= function(action,id){
											if(id == "Communication"){
												if(action == true){
													$scope.plusCommunication = true;
													$scope.minusCommunication = false;
												}else{
													$scope.plusCommunication = false;
													$scope.minusCommunication = true;
												}
											}
											if(id == "Invasive"){
												if(action == true){
													$scope.plusInvasive = true;
													$scope.minusInvasive = false;
												}else{
													$scope.plusInvasive = false;
													$scope.minusInvasive = true;
												}
											}
											if(id == "Psychosocial"){
												if(action == true){
													$scope.plusPsychosocial = true;
													$scope.minusPsychosocial = false;
												}else{
													$scope.plusPsychosocial = false;
													$scope.minusPsychosocial = true;
												}
											}
											if(id == "Pain"){
												if(action == true){
													$scope.plusPain = true;
													$scope.minusPain = false;
												}else{
													$scope.plusPain = false;
													$scope.minusPain = true;
												}
											}
											if(id == "Rest"){
												if(action == true){
													$scope.plusRest = true;
													$scope.minusRest = false;
												}else{
													$scope.plusRest = false;
													$scope.minusRest = true;
												}
											}
											if(id == "Nutrition"){
												if(action == true){
													$scope.plusNutrition = true;
													$scope.minusNutrition = false;
												}else{
													$scope.plusNutrition = false;
													$scope.minusNutrition = true;
												}
											}
											if(id == "Fall"){
												if(action == true){
													$scope.plusFall = true;
													$scope.minusFall = false;
												}else{
													$scope.plusFall = false;
													$scope.minusFall = true;
												}
											}
											if(id == "Vitals"){
												if(action == true){
													$scope.plusVitals = true;
													$scope.minusVitals = false;
												}else{
													$scope.plusVitals = false;
													$scope.minusVitals = true;
												}
											}
											if(id == "Oral"){
												if(action == true){
													$scope.plusOral = true;
													$scope.minusOral = false;
												}else{
													$scope.plusOral = false;
													$scope.minusOral = true;
												}
											}
											if(id == "Neurological"){
												if(action == true){
													$scope.plusNeurological = true;
													$scope.minusNeurological = false;
												}else{
													$scope.plusNeurological = false;
													$scope.minusNeurological = true;
												}
											}
											if(id == "Cardiovascular"){
												if(action == true){
													$scope.plusCardiovascular = true;
													$scope.minusCardiovascular = false;
												}else{
													$scope.plusCardiovascular = false;
													$scope.minusCardiovascular = true;
												}
											}
											if(id == "Respiratory"){
												if(action == true){
													$scope.plusRespiratory = true;
													$scope.minusRespiratory = false;
												}else{
													$scope.plusRespiratory = false;
													$scope.minusRespiratory = true;
												}
											}
											if(id == "Gastrointestinal"){
												if(action == true){
													$scope.plusGastrointestinal = true;
													$scope.minusGastrointestinal = false;
												}else{
													$scope.plusGastrointestinal = false;
													$scope.minusGastrointestinal = true;
												}
											}
											if(id == "Integumentary"){
												if(action == true){
													$scope.plusIntegumentary = true;
													$scope.minusIntegumentary = false;
												}else{
													$scope.plusIntegumentary = false;
													$scope.minusIntegumentary = true;
												}
											}
											if(id == "Musculoskeletal"){
												if(action == true){
													$scope.plusMusculoskeletal = true;
													$scope.minusMusculoskeletal = false;
												}else{
													$scope.plusMusculoskeletal = false;
													$scope.minusMusculoskeletal = true;
												}
											}
											if(id == "Genitourinary"){
												if(action == true){
													$scope.plusGenitourinary = true;
													$scope.minusGenitourinary = false;
												}else{
													$scope.plusGenitourinary = false;
													$scope.minusGenitourinary = true;
												}
											}
											
										}
										$scope.setNormalEmergency = function(tabValue) {
											$scope.procedureOrderMaster.urgency = tabValue;
										}

										$scope.initProcedureOrder = function() {

											$scope.rate = 0.00;
											$scope.procedureSubSpeciality = {specialityId:0,specialityName:"Select Speciality"};
											$scope.procedureDate = undefined;
			                                $scope.preferedTime = "";
											$scope.procedureSearch = '';
											$scope.updateProcCurrentIdx = -1;// edit
																				// Saved
																				// Procedure
																				// flag
											
											
								        	
								        	$scope.procedureReqMaster = {
								        		  proReqId : "",
								        		  primaryProcedureId : "",
								        		  patientId : "",
								        		  admissionId : "",
								        		  encounterId : "",
								        		  visitTypeId : "",
								        		  preferedDateTime : "",
								        		  preferedTime:"",
								        		  requestDate : "",
								        		  requestBy :$scope.userId,
								        		  procedureStatusId : "",
								        		  otCompletionDateTime : "",
								        		  otCompletionBy : "",
								        		  otBillCompletionDateTime : "",
								        		  otBillCompletionBy : "",
								        		  orgId : $scope.organizationId,
								        		  unitId : $scope.unitId,
								        		  createdBy : $scope.userId,
								        		  createdDate : "",
								        		  updatedBy : $scope.userId,
								        		  updatedDate : ""
								        		}
								        	
								    		$scope.procedureOrderMaster = angular.copy(
								    				{
								    						  procedureOrderId : "",
								    						  serviceId : "",
								    						  preferedDateTime : "",
								    						  preferedTime :"",
								    						  orgId : $scope.organizationId,
								    						  unitId : $scope.unitId,
								    						  patientId : "",
								    						  doctorId : "",
								    						  admissionId : "",
								    						  encounterId : "",
								    						  visitTypeId : "",
								    						  orderDateTime : "",
								    						  procedureStatusId : "1",
								    						  createdBy : $scope.userId,
								    						  createdDate : "",
								    						  updatedBy : $scope.userId,
								    						  updatedDate : "",
								    						  isPrimarySecondary : "N",
								    						  amount : "",
								    						  urgency : "0",
								    						  requestReason : "",
								    						  isIcuRequired : "N",
								    						  isBloodComponentRequired :"N",
								    						  isPacRequired : "N",
								    						  parentProcedureOrderId : "",
								    						  uhidNumber : "",
								    						  patientName : "",
								    						  dob : "",
								    						  cptCode : "",
								    						  service : "",
								    						  procedureType : "",
								    						  fromDate : "",
								    						  toDate : "",
								    						  status :'A',
								    						  genderCode : "",
								    						  age : "",
								    						  duration : "",
								    						  paymentEntitlementId : "",
								    						  patientTypeId : "",
								    						  billingBedCategoryId : "",
								    						  serviceCode : "",
								    						  speciality : "",
								    						  proReqId : 0,
								    						  specialityId : "",
								    						  specialityName : "",
								    						  procedureName : "",
								    						  procedureDuration:'',
								    						  procedureReqMasterDto : {},
								    				          listCalenderMapperDto:[],
								    				          procedureSequence : "",
								    					}
								    		);

											$scope.calandarSlotMppr = angular.copy({
								        		  surgicalCalenderMpprId : "",
								        		  specialityId : $scope.encounterDetailsObj.specialityId!=undefined?$scope.encounterDetailsObj.specialityId:"",
								        		  doctorId : $scope.encounterDetailsObj.doctorId,
								        		  procedureOrderId : "",
								        		  surgicalCalenderId : "",
								        		  patientId : $scope.encounterDetailsObj.patientId,
								        		  encounterId : $scope.encounterDetailsObj.encounterId,
								        		  admissionId : "",
								        		  status : "A",
								        		  createdBy : $scope.userId,
								        		  createdDate : "",
								        		  updatedBy : $scope.userId,
								        		  updatedDate : "",
								        		  organisationId : $scope.organizationId,
								        		  unitId : $scope.unitId,
								        		  isDeleted : "N"
								        		});		
											
											$scope.consentDetailList = [];
											$scope.checklistDetailList = [];
											$scope.staffDetailList = [];
											$scope.finalServiceList = [];
											$scope.equipmentDetailList = [];
											$scope.medicationList= [];
											$scope.implantList = [];
											// $scope.addServicesList = [];
											

										}

										$scope.procedureSequenceMasterList = [
											{
												id : 1,
												sequenceName : "Primary"
											},
											{
												id : 2,
												sequenceName : "Secondary"
											},
											{
												id : 3,
												sequenceName : "Tertiary"
											},
											{
												id : 4,
												sequenceName : "Quaternary"
											},
											{
												id : 5,
												sequenceName : "Quinary"
											}
											];
										
										$scope.procedureSequenceList = [
											{
												id : 0,
												sequenceName : " "
											}
											];
										
										$scope.procedureOrderedSequenceList = [
											{
												id : 0,
												sequenceName : " "
											}
											];
										// ------------ Get Speciality Master List
										// ------------//
										$scope.getSpecialityMasterList = function(){
											try{
												var URI = BASE_URL + ROOT_URL + GETACTIVESPECIALITY;
												var obj = {
															organizationId : $scope.organizationId,
															unitId         :$scope.unitId
													    }
												BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
													console.log(response);
													if(response.data.status == "success"){
														$scope.specialityMasterList = [];
														$scope.specialityMasterList.push({specialityId:0,specialityName:"Select Speciality"});
														$scope.specialityMasterList = $scope.specialityMasterList.concat(response.data.listObject);
													}
												});
											}catch (e) {
												console.log("Exception",e.message);
											}
										}
										$scope.getSpecialityMasterList();

										
										$scope.procedureSubSpeciality = {specialityId:0,specialityName:"Select Speciality"};
										$scope.selectSpeciality = function(selectedSubServiceType) {
											// var selectedSubServiceType =
											// JSON.parse($scope.procedureSubSpeciality);
											$scope.procedureOrderMaster.specialityName = selectedSubServiceType.specialityName;
											$scope.procedureOrderMaster.specialityId = selectedSubServiceType.specialityId;
											console.log("$scope.procedureOrderMaster.specialityName",$scope.procedureOrderMaster.specialityName);
										}

										$scope.procedureSearch = '';
										$scope.procedureDate;
										$scope.searchProcedures = function(searchKeyword) {
											$rootScope.startLoader();
											$scope.procedureMasterList = [];
											var selectedSubServiceType = $scope.procedureSubSpeciality;

											 $scope.regex = /^[A-Za-z0-9 ]+$/;
											 
												var isValid = $scope.regex.test(searchKeyword);
												if (!isValid) {
													
													$scope.procedureSearch = '';
													$rootScope.stopLoader();
													return false;
													
												} else {
											
											
												/*
												 * var URI = BASE_URL + ROOT_URL +
												 * "/global/procedureSearch/" +
												 * searchKeyword + "/unit/" + $scope.unitId +
												 * "/org/" + $scope.organizationId +
												 * "/speciality/" +
												 * selectedSubServiceType.specialityId
												 */
													
													var URI = BASE_URL + ROOT_URL + "/api/procedure/searchProcedureByNameOrCode";
													var data = {
															unitId : $scope.unitId,
															organizationId : $scope.organizationId,
															serviceName : searchKeyword
													};
													if(selectedSubServiceType.specialityId!=undefined && selectedSubServiceType.specialityId!=null 
															&& selectedSubServiceType.specialityId!="" && selectedSubServiceType.specialityId!=0){
														data.specialityId = selectedSubServiceType.specialityId;
													}
													console.log('URI', URI);
													return BillingGenericService
															.serviceAction(METHOD_POST, URI, data)
															.then(
																	function(response) {
																		$scope.procedureMasterList = [] ;
																		if (response.data.status == "success") {
																				angular.forEach(response.data.listObject,function(value, key) {
																					if(value.procedureTypeId != 3 ){
																						value.cptCode = value.serviceStandardCode;
																						$scope.procedureMasterList.push(value);
																					}
																					if(value.procedureTypeId == 3 && value.procedureConfigurationId != null){
																						$scope.procedureMasterList.push(value);
																					}
																				});
																				$rootScope.stopLoader();
																			}
																			$rootScope.stopLoader();
																			return $scope.procedureMasterList;
																	});
												}
										}

										
			/**
			 * ************************************************************** Patient
			 * Education Tab START
			 * ****************************************************************
			 */
										$scope.patientEduObj = {
												patientEducationTempale:"",
												procedureOrderPatientEducationId:null,
												organizationId:$scope.organizationId,
												unitId:$scope.unitId,
												createdBy:$scope.userId,
												updatedBy:$scope.userId,
												status:'A',
												createdDate:$scope.dt,
												updatedDate:$scope.dt
										};
										
			/**
			 * ************************************************************** Patient
			 * Education Tab END
			 * ****************************************************************
			 */

			/**
			 * ************************************************************** Select
			 * Procedure Start
			 * ****************************************************************
			 */
										$scope.currentIndex = "";// added procedure Index
																	// on Info icon(before
																	// proc save)
										$scope.consentDetailList = [];
										$scope.checklistDetailList = [];
										$scope.staffDetailList = [];
										$scope.finalServiceList = [];
										$scope.equipmentDetailList = [];
										$scope.addServicesList = [];
										$scope.diagnosticProcedureList = [];
										$scope.diagnosticOrNot = false;
										$scope.selectProcedureName = function($item,$model, $label) {
											$scope.diagnosticOrNot = false;
											$rootScope.startLoader();
											$scope.rate = 0.00;
											$scope.procedureOrderMaster.serviceId = $item.serviceMasterId;
											$scope.procedureOrderMaster.procedureName = $item.serviceStandardName;
											$scope.procedureDuration = $item.procedureDuration;
											
											$scope.procedureOrderMaster.specialityName =  $item.specialityName;
			                                $scope.procedureOrderMaster.specialityId =  $item.specialityId;
											
			                                var obj={
														"unitId":$scope.unitId,
														"organizationId":$scope.organizationId,
														"visitTypeId": $scope.encounterDetailsObj.visitTypeId,// by
																												// default
																												// its
																												// op
														"billingBedCategoryId":$scope.opdBillingCatId, // By
																										// default
																										// its
																										// OPD
																										// --->
																										// Id==1
														"patientTypeId": $scope.encounterDetailsObj.patientCategoryId, // need
																														// to
																														// add
																														// after
																														// patient
																														// registration
														"paymentEntitlementId": $scope.encounterDetailsObj.paymentEntitlementTypeId,
														"tariffId":$scope.defaultSelfTariffId,
														"orderDate":moment(new Date()).format('DD-MMM-YY HH:mm:ss'),
														"serviceList":[ $scope.procedureOrderMaster.serviceId]
											
													}
											console.log('Base prise',obj)
											$rootScope.startLoader();
											var URI = BASE_URL + ROOT_URL + "/api/unit/getBasePriceOfServicesFromTariffMasterByServiceList";
											BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
												if(response.data.status=="success")
												{
													$scope.rate = response.data.object.rate;
													console.log('rate',$scope.rate);
								                }
											});
											
											if($item.procedureTypeId == 3 && $scope.encounterDetailsObj.visitTypeId==1){
												$scope.getProcedureTemplate($item.serviceMasterId);
											}else{
												$scope.diagnosticSpecialityId= $item.specialityId;
												$scope.diagnosticSpecialityName= $item.specialityName;
												$scope.diagnosticSubSpecialityId= $item.subSpecialityId;
												$scope.diagnosticSubSpecialityName= $item.subSpecialityName;
												$scope.diagnosticOrNot = true;
												$scope.procedureDate = new Date();
												$scope.preferedTime = moment(new Date()).format('HH:mm');
											}
											
												
										}
										
										// get procedure configuration details by service id
										// on procedure type head search
										$scope.getProcedureTemplate = function(serviceMasterId){
											
											var URI = BASE_URL + ROOT_URL + "/api/otims/getProcedureConfigTemplate";
											var obj = {
													orgId:$scope.organizationId, 
													unitId:$scope.unitId,
													serviceId:serviceMasterId
													};
											BillingGenericService.serviceAction(METHOD_POST,URI, obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
												if (response.data.status == "success"){
												
													$scope.procedureTemplateObj = response.data.object;
													$scope.consentDetailList = response.data.object.consentDetailList;
													$scope.checklistDetailList = response.data.object.checklistDetailList;
													$scope.staffDetailList = response.data.object.staffDetailList;
													$scope.finalServiceList = response.data.object.serviceList;
													$scope.equipmentDetailList = response.data.object.equipmentDetailList;
													if(response.data.object.patientEduObj!=null){
														$scope.patientEduObj.patientEducationTempale = response.data.object.patientEduObj.patientEducationTempale;
														$scope.patientEduObj.procedureConfigPatientEducationId = response.data.object.patientEduObj.procedureConfigPatientEducationId;
													}
													console.log("$scope.consentDetailListsadvfbghjkl;",$scope.consentDetailList);
													
													$scope.medicationList=[];
													$scope.implantList=[];
													var date = new Date();
													$scope.dt = moment(date).format('DD-MMM-YY HH:mm:ss');
													
													// split implant and medications from
													// inventory list
													angular.forEach(response.data.object.inventoryItemList,function(value, key) {
														
														if(value.isImplant == true){
															var data ={}
															data.itemId = value.itemId;
															data.itemName = value.itemName;
															data.qty = value.qty;
															data.isItemOrKit = value.isItemOrKit;
															data.encounterId = $scope.encounterDetailsObj.encounterId;
															data.patientId = $scope.encounterDetailsObj.patientId;
															data.organizationId = $scope.organizationId;
															data.unitId = $scope.unitId;
															data.status = "A";
															data.createdBy = $scope.userId;
															data.createdDate = $scope.dt;
															data.updatedBy = $scope.userId;
															data.updatedDate = $scope.dt;
															$scope.implantList.push(data);
														}else{
															
															var data ={}
															data.itemId = value.itemId;
															data.itemName = value.itemName;
															data.qty = value.qty;
															data.isItemOrKit = value.isItemOrKit;
															data.encounterId = $scope.encounterDetailsObj.encounterId;
															data.patientId = $scope.encounterDetailsObj.patientId;
															data.organizationId = $scope.organizationId;
															data.unitId = $scope.unitId;
															data.status = "A";
															data.createdBy = $scope.userId;
															data.createdDate = $scope.dt;
															data.updatedBy = $scope.userId;
															data.updatedDate = $scope.dt;
															$scope.medicationList.push(data);
														}
													});
												} 
												
											// }
											});
										}
										
			/**
			 * ************************************************************** Select
			 * Procedure End
			 * ****************************************************************
			 */
										
			/**
			 * ************************************************************** consent Tab
			 * START ****************************************************************
			 */
										$scope.consentTypeObj={
											consentTypeMasterId:"",
											consentTypeMasterName:""
										};
										$scope.consentObj = {
												consentMasterId:"",
												consentMasterName:"",
												filePath:"",
												templateType:"",
												template:"",
												documentName:""
										};
										
										$scope.consentTypeList = [];
										$scope.getConsentTypes = function(){
											var URI = BASE_URL + ROOT_URL + "/adt/getActiveConsentTypeList/"+$scope.organizationId+"/"+$scope.unitId;
											BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
												if (response.data.status == "success")
													$scope.consentTypeList = response.data.listObject;
											});
										}
										
										$scope.getConsentTypes();
										
										$scope.consentList = [];
										$scope.getconsentListByTypeId = function(obj){
											$rootScope.startLoader();
											var URI = BASE_URL + ROOT_URL + "/adt/getConsentListByConsentTypeId";
											var obj = {
														consentTypeMasterId:obj.consentTypeMasterId,
														unitId : $scope.unitId
													};
											BillingGenericService.serviceAction(METHOD_POST,URI, obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
												if (response.data.status == "success"){
													$rootScope.stopLoader();
													$scope.consentList = response.data.listObject;
												console.log("$scope.consentList",$scope.consentList);
												}
											});
										}
										
										// $scope.consentDetailList = [];
										$scope.addConsentDetails = function(){
											try{
												$rootScope.startLoader();
												if($scope.consentTypeObj.consentTypeMasterId==""){
													$rootScope.stopLoader();
													growl.warning("Please select Consent Type",
															{
																title : 'warning!'
															});
												}else if($scope.consentObj.consentMasterId==""){
													$rootScope.stopLoader();
													growl.warning("Please select Consent",
															{
																title : 'warning!'
															});
												}else{
													
													var consentStatusFlag = false;
												var newArr = {};
												newArr.procedureOrderConsentId = null;
												newArr.consentTypeMasterName = $scope.consentTypeObj.consentTypeMasterName;
												newArr.consentId = $scope.consentObj.consentMasterId;
												newArr.consentMasterName = $scope.consentObj.consentMasterName;
												newArr.organizationId=$scope.organizationId,
												newArr.unitId=$scope.unitId,
												newArr.status="A",
												newArr.createdBy=$scope.userId,
												newArr.templateType=$scope.consentObj.templateType,
												newArr.documentName=$scope.consentObj.documentName,
												newArr.template=$scope.consentObj.template,
												newArr.filePath=$scope.consentObj.filePath,
												newArr.createdDate=$scope.dt,
												newArr.updatedBy=$scope.userId,
												newArr.updatedDate=$scope.dt,
												
												angular.forEach($scope.procedureOrderList[$scope.currentIndex].procedureOrderConsentList,function(value, key) {
													
													if(newArr.consentId == value.consentId && value.status == "I"){
														$scope.procedureOrderList[$scope.currentIndex].procedureOrderConsentList[idx].status="A";
													}else if(newArr.consentId == value.consentId && value.status == "A"){
														consentStatusFlag = true;
													}
												});
												
												if(consentStatusFlag == false){
													$scope.procedureOrderList[$scope.currentIndex].procedureOrderConsentList.push(newArr);
												}else{
													
													growl.warning("Already Consent Exist",
															{
																title : 'warning!'
															});
												}
												
												$scope.consentTypeObj.consentTypeMasterId = "";
												$scope.consentTypeObj.consentTypeMasterName = "";
												$scope.consentObj.consentMasterId = "";
												$scope.consentObj.consentMasterName = "";
												$rootScope.stopLoader();
												}
											}catch(e){
												$rootScope.stopLoader();
												console.log("Exception",e);
											}
										}
										
										$scope.removeConsent = function(idx){
											if($scope.procedureOrderList[$scope.currentIndex].procedureOrderConsentList[idx].procedureOrderConsentId!=null){
												$scope.procedureOrderList[$scope.currentIndex].procedureOrderConsentList[idx].status="I";
											}else{
												$scope.procedureOrderList[$scope.currentIndex].procedureOrderConsentList.splice(idx,1);
											}
										}
										
										$scope.consentCurrentIndex="";
										$scope.getCurrentIndex = function(index){
											$scope.consentCurrentIndex=index;
										}
			/**
			 * ************************************************************** consent Tab
			 * END ****************************************************************
			 */
										
			/**
			 * ************************************************************** checklist Tab
			 * START ****************************************************************
			 */
										
										$scope.checklistObj ={
												description:"",
												checklistMasterId:"",
												roleId:"",
												roleName:""
											}	
										$scope.stageObj ={
												stageId:"",
												stageName:""
											}	
											
											$scope.checklistNameList = [];
											$scope.getcheckListByTypeId = function(stageId){
												$rootScope.startLoader();
												var URI = BASE_URL + ROOT_URL + "/global/otims/getChecklistMasterByStageId";
												var obj = {
														stageId:stageId,
														organizationId:$scope.organizationId, 
														unitId:$scope.unitId
														};
												BillingGenericService.serviceAction(METHOD_POST,URI, obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
													if (response.data.status == "success"){
														$rootScope.stopLoader();
														$scope.checklistNameList = response.data.listObject;
													console.log("$scope.checklistNameList",$scope.checklistNameList);
													}
												});
											}
											$scope.addChecklistDetails = function(){
												try{
													$rootScope.startLoader();
													if($scope.stageObj.stageId==""){
														$rootScope.stopLoader();
														growl.warning("Please select Stage",
																{
																	title : 'warning!'
																});
													}else if($scope.checklistObj.checklistMasterId==""){
														$rootScope.stopLoader();
														growl.warning("Please select Checklist",
																{
																	title : 'warning!'
																});
													}else{
														var checkStatusFlag = false;
													var newArr = {};
													newArr.procedureOrderChecklistId = null;
													newArr.checklistId = $scope.checklistObj.checklistMasterId;
													newArr.description = $scope.checklistObj.description;
													newArr.stageId = $scope.stageObj.stageId;
													newArr.roleName = $scope.checklistObj.roleName;
													newArr.organizationId=$scope.organizationId;
													newArr.unitId=$scope.unitId;
													newArr.createdBy=$scope.userId;
													newArr.createdDate=$scope.dt;
													newArr.updatedBy=$scope.userId;
													newArr.status="A",
													newArr.updatedDate=$scope.dt;

													angular.forEach($scope.procedureOrderList[$scope.currentIndex].procedureOrderCheckList,function(value, key) {
														
														if(newArr.checklistId == value.checklistId && value.status == "I"){
															$scope.procedureOrderList[$scope.currentIndex].procedureOrderConsentList[idx].status="A";
														}else if(newArr.checklistId == value.checklistId && value.status == "A"){
															checkStatusFlag = true;
														}
													});
													
													if(checkStatusFlag == false){
														$scope.procedureOrderList[$scope.currentIndex].procedureOrderCheckList.push(newArr);
													}else{
														
														growl.warning("Already CheckList Exist",
																{
																	title : 'warning!'
																});
													}
													
													$scope.checklistObj.checklistMasterId = "";
													$scope.checklistObj.description = "";
													$scope.checklistObj.roleName = "";
													$scope.checklistObj.roleId = "";
													$scope.stageObj.stageId = "";
													$rootScope.stopLoader();
													}
												}catch(e){
													$rootScope.stopLoader();
													console.log("Exception",e);
												}
											}
											
											$scope.checklistItemList = [];
											$scope.getChecklistNameListById = function(info){
												try{
													$rootScope.startLoader();
													$scope.checklistItemList = [];
													var URI = BASE_URL + ROOT_URL + "/global/otims/getChecklistMasterDetailsByMasterId";
													var obj = {
															checklistMasterId:info.checklistId,
															organizationId:$scope.organizationId, 
															unitId:$scope.unitId
															};
													BillingGenericService.serviceAction(METHOD_POST,URI, obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
														if (response.data.status == "success"){
															$rootScope.stopLoader();
															$scope.checklistItemList = response.data.listObject;
														console.log("$scope.checklistItemList",$scope.checklistItemList);
														}
													});
												}catch(e){
													$rootScope.stopLoader();
													console.log("Exception",e);
												}
											}
											
											$scope.removeChecklist = function(idx){
												if($scope.procedureOrderList[$scope.currentIndex].procedureOrderCheckList[idx].procedureOrderChecklistId!=null){
													$scope.procedureOrderList[$scope.currentIndex].procedureOrderCheckList[idx].status="I";
													}else{
													$scope.procedureOrderList[$scope.currentIndex].procedureOrderCheckList.splice(idx,1);
												}
											}
			/**
			 * ************************************************************** checklist Tab
			 * END ****************************************************************
			 */
								
											
			/**
			 * ************************************************************** staff Tab END
			 * ********************************************************************
			 */
											$scope.staffObj ={
													roleId:"",
													roleName:""
											};
											$scope.commonStaffObj = {
													// staffNumber:1,
													staffNumber:"",
											}
											$scope.otimsRoleList = [];
											$scope.otimsRole = function() {
												$rootScope.startLoader();
												var data = {
														organisationId:$scope.organizationId,
														unitId:$scope.unitId
														};
												
												var URI = BASE_URL + ROOT_URL + "/api/global//getActiveOTRoleList";
												GenericService.serviceAction("POST", URI, data).then(function(response) {
													$rootScope.stopLoader();
													$scope.otimsRoleList = response.data.listObject;
													console.log("otimsRoleList",$scope.otimsRoleList);
												});
											}				
											$scope.otimsRole();
											$scope.addStaffDetails = function(){
												try{
													$rootScope.startLoader();
													if($scope.staffObj.roleId==""){
														$rootScope.stopLoader();
														growl.warning("Please select Role",
																{
																	title : 'warning!'
																});
													}else if($scope.commonStaffObj.staffNumber==""){
														$rootScope.stopLoader();
														growl.warning("Please Enter Number",
																{
																	title : 'warning!'
																});
													}else{
														
														var staffStatusFlag = false;
													var newArr = {};
													newArr.procedureOrderStaffId = null;
													newArr.roleId = $scope.staffObj.roleId;
													newArr.roleName = $scope.staffObj.roleName;
													newArr.staffNumber = $scope.commonStaffObj.staffNumber;
													newArr.organizationId=$scope.organizationId;
													newArr.unitId=$scope.unitId;
													newArr.createdBy=$scope.userId;
													newArr.createdDate=$scope.dt;
													newArr.updatedBy=$scope.userId;
													newArr.updatedDate=$scope.dt;
													newArr.status="A",
													
													angular.forEach($scope.procedureOrderList[$scope.currentIndex].procedureOrderStaffList,function(value, key) {
														
														if(newArr.roleId == value.roleId && value.status == "I"){
															$scope.procedureOrderList[$scope.currentIndex].procedureOrderStaffList[idx].status="A";
														}else if(newArr.roleId == value.roleId && value.status == "A"){
															staffStatusFlag = true;
														}
													});
													
													if(staffStatusFlag == false){
														$scope.procedureOrderList[$scope.currentIndex].procedureOrderStaffList.push(newArr);
													}else{
														
														growl.warning("Already Role Exist",
																{
																	title : 'warning!'
																});
													}
													$scope.staffObj.roleId = "";
													$scope.staffObj.roleName = "";
													$scope.commonStaffObj.staffNumber = "";
													$rootScope.stopLoader();
													}
												}catch(e){
													$rootScope.stopLoader();
													console.log("Exception",e);
												}
											}
											
											$scope.removeStaff = function(idx){
												if($scope.procedureOrderList[$scope.currentIndex].procedureOrderStaffList[idx].procedureOrderStaffId!=null){
													$scope.procedureOrderList[$scope.currentIndex].procedureOrderStaffList[idx].status="I";
													}else{
													$scope.procedureOrderList[$scope.currentIndex].procedureOrderStaffList.splice(idx,1);
												}
											}
											
			/**
			 * ************************************************************** staff Tab END
			 * **********************************************************************
			 */
											
			/**
			 * ************************************************************** Equipment Tab
			 * START ****************************************************************
			 */

												$scope.equipmentTypeObj = {
														id:"",
														category:""
												}
												
												$scope.equipmentNameObj = {
														id:"",
														name:""
												}
												
												$scope.equipmentTypeList = [];
												$scope.getEquipmentTypeList = function () {
													var URI = BASE_URL + ROOT_URL + "/api/global/assets/categories/2/"+$scope.unitId+"/"+$scope.organizationId; // 2
																																								// for
																																								// Fixed
																																								// asset
																																								// type
													BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
														$scope.equipmentTypeList = response.data;
														console.log("$scope.equipmentTypeList",$scope.equipmentTypeList);
														
													});
												   }
												
												$scope.getEquipmentTypeList();
												
												$scope.equipmentNameList = [];
												$scope.getEquipmentNamesByEquipmentId = function (equipmentTypeId) {
													$rootScope.startLoader();
													var URI = BASE_URL + ROOT_URL + "/api/unit/fixed/asset/getAssetNameListByAssetCategoryId/"+equipmentTypeId; 
													BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
														$scope.equipmentNameList = response.data;
														console.log("$scope.equipmentNameList",$scope.equipmentNameList);
														$rootScope.stopLoader();	
													});
												   }
												
												$scope.addEquipmentDetails = function(){
													try{
														$rootScope.startLoader();
														if($scope.equipmentTypeObj.id==""){
															$rootScope.stopLoader();
															growl.warning("Please select Equipment Type",
																	{
																		title : 'warning!'
																	});
														}else if($scope.equipmentNameObj.id==""){
															$rootScope.stopLoader();
															growl.warning("Please Select Equipment Name",
																	{
																		title : 'warning!'
																	});
														}else{
														var equpStatusFlag = false;
														var newArr = {};
														newArr.procedureOrderEquipmentId = null;
														newArr.equipmentTypeId = $scope.equipmentTypeObj.id;
														newArr.equipmentTypeName = $scope.equipmentTypeObj.category;
														newArr.itemId = $scope.equipmentNameObj.id;
														newArr.itemName =$scope.equipmentNameObj.name;
														newArr.organizationId=$scope.organizationId;
														newArr.unitId=$scope.unitId;
														newArr.createdBy=$scope.userId;
														newArr.createdDate=$scope.dt;
														newArr.updatedBy=$scope.userId;
														newArr.updatedDate=$scope.dt;
														newArr.qty = 1;
														newArr.status="A",
													
														angular.forEach($scope.procedureOrderList[$scope.currentIndex].procedureOrderEquipmentList,function(value, key) {
															
															if(newArr.itemId == value.itemId && value.status == "I"){
																$scope.procedureOrderList[$scope.currentIndex].procedureOrderEquipmentList[idx].status="A";
															}else if(newArr.itemId == value.itemId && value.status == "A"){
																equpStatusFlag = true;
															}
														});
														
														if(equpStatusFlag == false){
															$scope.procedureOrderList[$scope.currentIndex].procedureOrderEquipmentList.push(newArr);
														}else{
															
															growl.warning("Already Equipment Exist",
																	{
																		title : 'warning!'
																	});
														}
														$scope.equipmentTypeObj.id = "";
														$scope.equipmentTypeObj.category = "";
														$scope.equipmentNameObj.id = "";
														$scope.equipmentNameObj.name = "";
														$rootScope.stopLoader();
														}
													}catch(e){
														$rootScope.stopLoader();
														console.log("Exception",e);
													}
												}
												
												$scope.removeEquipment = function(index){
													if($scope.procedureOrderList[$scope.currentIndex].procedureOrderEquipmentList[index].procedureOrderEquipmentId!=null){
														$scope.procedureOrderList[$scope.currentIndex].procedureOrderEquipmentList[index].status="I";
														}else{
														$scope.procedureOrderList[$scope.currentIndex].procedureOrderEquipmentList.splice(index,1);
													}
												}
			/**
			 * ************************************************************** Equipment Tab
			 * END ****************************************************************
			 */
												

			/**
			 * ************************************************************** Services Tab
			 * START ****************************************************************
			 */

											/*
											 * $scope.search=""; $scope.searchServices =
											 * function(searchKeyword) { //need to change
											 * name (duplicate) $scope.servicesList=[];
											 * console.log("searchKeyword",searchKeyword);
											 * 
											 * var serData = { organizationId :
											 * $scope.organizationId, unitId :
											 * $scope.unitId, searchKeyword : searchKeyword,
											 * defaultSelfTariffId :
											 * $scope.defaultSelfTariffId,
											 * paymentEntitlementTypeId :
											 * $scope.encounterDetailsObj.paymentEntitlementTypeId,
											 * visitTypeId : 1, billingBedCategoryId :
											 * $scope.opdBillingCatId, patientCategoryId :
											 * $scope.encounterDetailsObj.patientCategoryId,
											 * encounterId :
											 * $scope.encounterDetailsObj.encounterId }
											 * 
											 * var URI = BASE_URL + ROOT_URL +
											 * "/api/unit/tariff/serviceMaster";
											 * 
											 * //var URI = BASE_URL + ROOT_URL +
											 * "/global/serviceMaster/"+searchKeyword+"/unit/"+$scope.unitId+"/org/"+$scope.organizationId;
											 * return
											 * BillingGenericService.serviceAction(METHOD_POST,URI,serData,
											 * NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
											 * if(response.data.status == "success"){
											 * $scope.servicesList =
											 * response.data.listObject; } return
											 * $scope.servicesList; }); }
											 */
											$scope.addServicesList = [];
											$scope.addServiceToServiceListTemp = function($item, $model, $label){
												  
												var flag = false;
												angular.forEach($scope.addServicesList,function(value,key){
													
													if(value.serviceId == $item.serviceMasterId){
														flag = true;
													}
													
												});
												
												if(flag == false){
													console.log($item);
													var newdata ={}
													newdata.serviceId = $item.serviceMasterId;
													newdata.serviceStandardCode = $item.serviceStandardCode;
													newdata.serviceStandardName = $item.serviceStandardName;
													newdata.specialityId = $item.specialityId;
													newdata.specialityName = $item.specialityName;
													newdata.subSpecialityId = $item.subSpecialityId;
													newdata.subSpecialityName = $item.subSpecialityName;
													newdata.status = "";
													$scope.addServicesList.push(newdata);
												}else{
													growl.warning("This service is  already added ",
															{
																title : 'warning!'
															});
												}
											 }
											$scope.specialityIdspecialityId = function(){
												var serviceFlag = false;
												angular.forEach($scope.addServicesList,function(value,index){
													
													var newdata ={}
													newdata.procedureOrderServiceId =null;
													newdata.serviceId = value.serviceId;
													newdata.organizationId=$scope.organizationId;
													newdata.unitId=$scope.unitId;
													newdata.createdBy=$scope.userId;
													newdata.updatedBy=$scope.userId;
													newdata.status='A';
													newdata.createdDate=$scope.dt;
													newdata.updatedDate=$scope.dt;
													newdata.specialityId=value.specialityId;
													newdata.subSpecialityId=value.subSpecialityId;
													newdata.serviceStandardCode = value.serviceStandardCode;
													newdata.serviceStandardName = value.serviceStandardName;
													newdata.specialityName = value.specialityName;
													newdata.subSpecialityName = value.subSpecialityName;
												
													angular.forEach($scope.procedureOrderList[$scope.currentIndex].procedureOrderServiceList,function(value1,index1){
														
														if(value1.serviceId == value.serviceId){
															serviceFlag = true;
														}
													});
													if(serviceFlag == false){
														$scope.procedureOrderList[$scope.currentIndex].procedureOrderServiceList.push(newdata);
													}
												});
												if(serviceFlag == true){
													growl.warning("This service is  already added ",
															{
																title : 'warning!'
															});
												}
												$scope.addServicesList = [];
											}
											
											$scope.removeTempAddedServices = function(index){
												
												$scope.addServicesList.splice(index,1);
											}
											
											$scope.removeService = function(index){
												
												if($scope.procedureOrderList[$scope.currentIndex].procedureOrderServiceList[index].procedureOrderServiceId!=null){
													$scope.procedureOrderList[$scope.currentIndex].procedureOrderServiceList[index].status="I";
													}else{
													$scope.procedureOrderList[$scope.currentIndex].procedureOrderServiceList.splice(index,1);
												}
											}
			/**
			 * ************************************************************** Services Tab
			 * END ****************************************************************
			 */	
			/**
			 * ****************************************************************Medication Tab Start
			 * ***********************************************
			 */	
											/**
											 * ****************************MEDICATION KIT
											 * START********************
											 */
										/*
										 * $scope.removeFromAddedKitList = function(medicationList,index){
										 * $scope.medicationList.splice(index, 1); }
										 */
											$scope.removeFromAddedKitList = function(index) {
												
												if($scope.procedureOrderList[$scope.currentIndex].medicationDetailsList[index].medicationDetailsId!=null){
													$scope.procedureOrderList[$scope.currentIndex].medicationDetailsList[index].status="I";
													}else{
													$scope.procedureOrderList[$scope.currentIndex].medicationDetailsList.splice(index,1);
												}
											}

											
											   $scope.medicationObj={
														qty:''
											   };
											   $scope.isItemOrKit = "N";
											   $scope.selectItemOrKit = function(isItemOrKit){
												 
												   $scope.isItemOrKit = isItemOrKit;
												   console.log("selectItemOrKit    ",$scope.isItemOrKit);
											   }
											   $scope.medicationList=[];
											   $scope.kitDetails=[];
											   $scope.selectImplantDetailsList=[]
							   
											   $scope.getListByName = function(name){
												  
												   $scope.items = [];
												   if($scope.isItemOrKit=='Y'){
													
													   var URI = BASE_URL + ROOT_URL +"/api/units/kit/master/search/"+name;
														
														return GenericService.serviceAction("GET",URI).then(function(response){
															if (!!response.data && response.data.length > 0)
																 $scope.kitDetails=[];
															angular.forEach(response.data , function(value,key){
															
																var newArr = {};
																	newArr.kitId = value.id;
																	newArr.kitName = value.description;
																	newArr.isItemOrKit = $scope.isItemOrKit;
																	$scope.kitDetails.push(newArr);
															});
															console.log("kitDetails", $scope.kitDetails);
															return $scope.kitDetails;
														});
												}else{
													var URI = BASE_URL + ROOT_URL +"/api/globals/items/item/search/"+name;
													
													return GenericService.serviceAction("GET",URI).then(function(response){
														if (!!response.data && response.data.length > 0)
																console.log("getListByName", response.data);
															return response.data;
														
														});
												   }
												}
												
							          /*
										 * $scope.selectMedication = function(info, model,label) {
										 * 
										 * console.log("info ",info); if(info.isItemOrKit=='Y'){
										 * $scope.encounterDetailsObj.isItemOrKit = info.isItemOrKit;
										 * $scope.encounterDetailsObj.itemId = info.itemId;
										 * $scope.encounterDetailsObj.itemName = info.itemName;
										 * $scope.encounterDetailsObj.kitId = info.kitId;
										 * $scope.implantObj.qty = info.qty;
										 * $scope.encounterDetailsObj.kitName = info.description; //
										 * info.isItemOrKit = info.isItemOrKit;
										 * $scope.medicationList.push(info); console.log("medicationList",
										 * $scope.medicationList); $scope.encounterDetailsObj.kitName=''; }
										 * else{ $scope.encounterDetailsObj.itemId = info.itemId;
										 * $scope.encounterDetailsObj.itemName = info.itemName;
										 * $scope.encounterDetailsObj.kitId = info.kitId;
										 * $scope.implantObj.qty = info.qty;
										 * $scope.encounterDetailsObj.kitName = info.description;
										 * $scope.encounterDetailsObj.isItemOrKit = $scope.isItemOrKit;
										 * //info.isItemOrKit = $scope.isItemOrKit;
										 * $scope.medicationList.push(info); console.log("medicationList
										 * 111", $scope.medicationList); $scope.encounterDetailsObj.itemName
										 * ='';
										 *  }
										 * 
										 *  }
										 */
							              
											$scope.selectMedication = function(info, model,label) {

												console.log("info  ", info);
												var date = new Date();
												$scope.dt = moment(date).format(
														'DD-MM-YYYY HH:mm:ss');
												
												if ($scope.isItemOrKit == 'Y') {
													var newArr = {};
													newArr.medicationDetailsId = null;
													newArr.kitId = info.kitId;
													newArr.itemName = info.itemName;
													newArr.qty = 1;
													newArr.isItemOrKit = info.isItemOrKit;
													newArr.encounterId = $scope.encounterDetailsObj.encounterId;
													newArr.patientId = $scope.encounterDetailsObj.patientId;
													newArr.organizationId =  $scope.organizationId;
													newArr.unitId = $scope.unitId;
													newArr.status ="A";
													newArr.createdBy = $scope.userId;
													newArr.createdDate = $scope.dt;
													newArr.updatedBy = $scope.userId;
													newArr.kitName = info.kitName;
													newArr.updatedDate = $scope.dt;
													$scope.procedureOrderList[$scope.currentIndex].medicationDetailsList.push(newArr);
													
													$scope.encounterDetailsObj.kitName = '';
												} else {
													var newArr = {};
													newArr.medicationDetailsId = null;
													newArr.itemId = info.itemId;
													newArr.itemName = info.itemName;
													newArr.qty = 1;
													newArr.isItemOrKit = info.isItemOrKit != undefined ? info.isItemOrKit : 'N';
													newArr.encounterId = $scope.encounterDetailsObj.encounterId;
													newArr.patientId = $scope.encounterDetailsObj.patientId;
													newArr.organizationId =  $scope.organizationId;
													newArr.unitId = $scope.unitId;
													newArr.status ="A";
													newArr.createdBy = $scope.userId;
													newArr.createdDate = $scope.dt;
													newArr.updatedBy = $scope.userId;
													newArr.updatedDate = $scope.dt;
													$scope.procedureOrderList[$scope.currentIndex].medicationDetailsList.push(newArr);
													
													$scope.encounterDetailsObj.itemName = '';
												}

											}
							              

												
							              $scope.saveMedicationDetails = function(procedureOrderId,index) 
											{
												$scope.$broadcast('show-errors-check-validity');
													try {
														var date = new Date();
														$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');	
														
														$rootScope.startLoader();
														$scope.finalMedicationList = [];
											angular.forEach($scope.finalMedications, function(value , key) {
												if(value.procedureIndex==index)
													{
											 	var data = {
																
											 			itemId:value.itemId,
														kitId:value.kitId,
														patientId:$scope.encounterDetailsObj.patientId,
														qty:value.qty,
														itemName : value.itemName,
														kitName : value.kitName,
														procedureOrderId : procedureOrderId,
														encounterId:$scope.encounterDetailsObj.encounterId,
													    organizationId:$scope.organizationId,
													     unitId:$scope.unitId,
													     status:"A",
														 createdBy:$scope.userId,
														 createdDate:$scope.dt,
														 updatedBy:$scope.userId,
														 updatedDate:$scope.dt,
												}
																
											 	$scope.finalMedicationList.push(data);
													}		
							            		});
													
												console.log("savemedicationList",$scope.finalMedicationList);
												
												
												var URI = BASE_URL + ROOT_URL+"/api/otims/saveMedicationDetails";
												
												 GenericService.serviceAction("POST", URI,$scope.finalMedicationList)
												.then(function(response) {	
													if (response.data.status == 'success')
													{
														$rootScope.stopLoader();
														growl.success(response.data.message,
																{
																	title : 'Success!'
																});
														$scope.$broadcast('show-errors-reset');
													} else if(response.data.status == 'error')
													{
														$rootScope.stopLoader();
														growl.warning(response.data.message,
																{
																	title : 'Warning!'
																});
													}
													
												});
											} catch (e) {
												console.log("Exception",e.message);
											}
											
									}
		/******************************************************************/
							  /**************************IMPLANT * Start****************************** */
							
										   $scope.implantObj={
												   qty:'',
													isItemOrKit : '',
													itemId : '',
													itemName : '',
													kitId : '',
													kitName : ''
													
										   };
									   $scope.implantObj.isItemOrKit = "N";
										   $scope.selectImplantItemOrKit = function(isItemOrKit){
											 
											   $scope.implantObj.isItemOrKit = isItemOrKit;
											   console.log("selectImplantItemOrKit    ",$scope.implantObj.isItemOrKit);
										   }
										  
									   $scope.getImplantListByName = function(name){
											   $scope.implantItems = [];
											   if( $scope.implantObj.isItemOrKit=='Y'){
												   var data={
															kitName : name,
															organizationId : $scope.organizationId,
															unitId : $scope.unitId
													};
												   var URI = BASE_URL + ROOT_URL + IMPLANT_SEARCH_KIT ;
													
													return GenericService.serviceAction(METHOD_POST,URI,data).then(function(response){
														if (response.data.status=='success')
															 $scope.implantKitDetails=[];
														angular.forEach(response.data.listObject , function(value,key){
														
															var newArr = {};
																newArr.kitId = value.kitId;
																newArr.kitName = value.kitName;
																newArr.isItemOrKit = $scope.implantObj.isItemOrKit;
																$scope.implantKitDetails.push(newArr);
														});
														console.log("implantKitDetails", $scope.implantKitDetails);
														return $scope.implantKitDetails;
													});
											}else{
												
												var URI = BASE_URL + ROOT_URL + IMPLANT_SEARCH_ITEMS;
												var data={
														itemName : name,
														organizationId : $scope.organizationId,
														unitId : $scope.unitId
												};
												
											return GenericService.serviceAction(METHOD_POST,URI,data).then(function(response){
													if (response.data.status == "success")
														return response.data.listObject;
													});
											   }
										   }
											
										   $scope.implantList= [] ;
										   $scope.selectImplant = function(info, model,label) {
												
												console.log("info  ",info);
												if(info.isItemOrKit=='Y'){
													$scope.implantObj.isItemOrKit = info.isItemOrKit;
													$scope.implantObj.itemId = info.itemId;
													$scope.implantObj.itemName = info.itemName;
													$scope.implantObj.kitId = info.kitId;
													$scope.implantObj.qty = info.qty;
													$scope.implantObj.kitName = info.description;
													$scope.implantList.push(info);
													console.log("implantList items from kit", $scope.implantList);
													$scope.implantObj.kitName='';
												}
												else{
													$scope.implantObj.itemId = info.itemId;
													$scope.implantObj.itemName = info.itemName;
													$scope.implantObj.kitId = info.kitId;
													$scope.implantObj.qty = info.qty;
													$scope.implantObj.kitName = info.description;
													$scope.implantObj.isItemOrKit = $scope.implantObj.isItemOrKit;
													$scope.implantList.push(info);
													console.log("implantList items ", $scope.implantList);
													$scope.implantObj.itemName ='';
													
												}
												
											}
										   
										   $scope.selectImplant = function(info, model, label) {

												var date = new Date();
												$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
												
												if (info.isItemOrKit == 'Y') {
													var newArr = {};
													newArr.implantDetailsId = null;
													// newArr.itemId = info.itemId;
													newArr.itemName = info.itemName;
													newArr.kitId = info.kitId;
													newArr.qty = 1;
													newArr.isItemOrKit = info.isItemOrKit;
													newArr.encounterId = $scope.encounterDetailsObj.encounterId;
													newArr.patientId = $scope.encounterDetailsObj.patientId;
													newArr.organizationId =  $scope.organizationId;
													newArr.unitId = $scope.unitId;
													newArr.status ="A";
													newArr.createdBy = $scope.userId;
													newArr.createdDate = $scope.dt;
													newArr.updatedBy = $scope.userId;
													newArr.updatedDate = $scope.dt;
													newArr.kitName =  info.kitName ;
													$scope.procedureOrderList[$scope.currentIndex].implantDetailsList.push(newArr);
													$scope.implantObj.itemName = '';
													
												} else {
													
													var newArr = {};
													newArr.implantDetailsId = null;
													newArr.itemId = info.itemId;
													newArr.itemName = info.itemName;
													newArr.qty = 1;
													newArr.isItemOrKit = info.isItemOrKit != undefined ? info.isItemOrKit : 'N';
													newArr.encounterId = $scope.encounterDetailsObj.encounterId;
													newArr.patientId = $scope.encounterDetailsObj.patientId;
													newArr.organizationId =  $scope.organizationId;
													newArr.unitId = $scope.unitId;
													newArr.status ="A";
													newArr.createdBy = $scope.userId;
													newArr.createdDate = $scope.dt;
													newArr.updatedBy = $scope.userId;
													newArr.updatedDate = $scope.dt;
													$scope.procedureOrderList[$scope.currentIndex].implantDetailsList.push(newArr);
													$scope.implantObj.itemName = '';

												}

											}
										   
										   $scope.finalImplantList = [];
											$scope.addImplantDetails = function(){
												console.log("len----",$scope.procedureOrderList.length);
												$scope.$broadcast('show-errors-check-validity');
												try {
													var date = new Date();
													$scope.dt = moment(date).format('DD-MMM-YY HH:mm:ss');	
													
													$rootScope.startLoader();
													$scope.finalImplantList = [];
													angular.forEach($scope.implantList, function(value , key) {
													var data = {
										 			itemId:value.itemId,
										 			itemName : value.itemName,
													kitId:value.kitId,
													kitName : value.kitName,
													isItemOrKit : value.isItemOrKit,
													patientId:$scope.encounterDetailsObj.patientId,
													qty:value.qty,
													encounterId:$scope.encounterDetailsObj.encounterId,
												    organizationId:$scope.organizationId,
												     unitId:$scope.unitId,
												     status:"A",
													 createdBy:$scope.userId,
													 createdDate:$scope.dt,
													 updatedBy:$scope.userId,
													 updatedDate:$scope.dt,
											}
										 	$scope.finalImplantList.push(data);
										 	$rootScope.stopLoader();		
											});
													$scope.implantList=[];
											console.log("finalImplantList",$scope.finalImplantList);
											$rootScope.stopLoader();		
										} catch (e) {
											console.log("Exception",e.message);
											$rootScope.stopLoader();		
										}
										
										}
											$scope.removeFromAddedImplantList = function(index) {
												if($scope.procedureOrderList[$scope.currentIndex].implantDetailsList[index].implantDetailsId!=null){
													$scope.procedureOrderList[$scope.currentIndex].implantDetailsList[index].status="I";
													}else{
													$scope.procedureOrderList[$scope.currentIndex].implantDetailsList.splice(index,1);
												}
											}
										
										// get addedImplants
										
										$scope.implantAgainstProcedure = [];
										$scope.getAddedImplants = function(idx){
												$scope.implantAgainstProcedure=[];
											console.log("ffff-----",$scope.finalImplants);
											 angular.forEach( $scope.finalImplants, function(value, index) 
										       		 {
												 	if(value.procedureIndex==idx)
												 	{
												 		var data ={
												 				itemId:value.itemId,
													 			itemName : value.itemName,
																kitId:value.kitId,
																kitName : value.kitName,
																isItemOrKit : value.isItemOrKit,
																patientId:$scope.encounterDetailsObj.patientId,
																qty:value.qty,
																encounterId:$scope.encounterDetailsObj.encounterId,
															    organizationId:$scope.organizationId,
															     unitId:$scope.unitId,
															     status:"A",
																 createdBy:$scope.userId,
																 createdDate:$scope.dt,
																 updatedBy:$scope.userId,
																 updatedDate:$scope.dt,
												 		};
												 		$scope.implantAgainstProcedure.push(data);
												 	}
												 console.log("implantAgainstProcedure",$scope.implantAgainstProcedure);
										       		 });
											
										     }
										
										  $scope.saveImplantDetails = function(procedureOrderId,index) 
											{
												$scope.$broadcast('show-errors-check-validity');
													try {
														var date = new Date();
														$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');	
														
														$rootScope.startLoader();
														$scope.finalImplantList = [];
											angular.forEach($scope.finalImplants, function(value , key) {
												if(value.procedureIndex==index)
													{
											 	var data = {
																
											 			itemId:value.itemId,
														kitId:value.kitId,
														patientId:$scope.encounterDetailsObj.patientId,
														qty:value.qty,
														itemName : value.itemName,
														kitName : value.kitName,
														procedureOrderId : procedureOrderId,
														encounterId:$scope.encounterDetailsObj.encounterId,
													    organizationId:$scope.organizationId,
													     unitId:$scope.unitId,
													     status:"A",
														 createdBy:$scope.userId,
														 createdDate:$scope.dt,
														 updatedBy:$scope.userId,
														 updatedDate:$scope.dt,
												}
																
											 	$scope.finalImplantList.push(data);
													}		
							            		});
													
												console.log("finalImplantList",$scope.finalImplantList);
												var URI = BASE_URL + ROOT_URL+"/api/otims/saveImplantdetails";
												
												 GenericService.serviceAction("POST", URI,$scope.finalImplantList)
												.then(function(response) {	
													if (response.data.status == 'success')
													{
														$rootScope.stopLoader();
														growl.success(response.data.message,
																{
																	title : 'Success!'
																});
														$scope.$broadcast('show-errors-reset');
													} else if(response.data.status == 'error')
													{
														$rootScope.stopLoader();
														growl.warning(response.data.message,
																{
																	title : 'Warning!'
																});
													}
													
												});
												
												
											} catch (e) {
												console.log("Exception",e.message);
											}
											
									}
										$scope.getImplantDetails = function(procedureOrderId){
											console.log("info.procedureDate",procedureOrderId);
												var dataDetails={
													organizationId:$scope.organizationId, 
													unitId:$scope.unitId,
													procedureOrderId :procedureOrderId
												};
												
												var URI = BASE_URL + ROOT_URL + GET_IMPLANT_DETAILS_BY_PROCEDURE_ORDER_ID;
												GenericService.serviceAction(METHOD_POST, URI, dataDetails).then(function(response) {
													console.log(response);
													$scope.implantDetails = [];
													if (response.data.status == "success"){
														$scope.implantDetails = response.data.listObject;
														console.log("implantDetails",$scope.implantDetails);
													// $scope.ImplantDetails=[];
													}
													
												});
										}
										
					              
					      
							/** *********************IMPLANT END********************* */
							
							/**    
							              
											
			/**
			 * ************************************************************** add Procedure
			 * Order Start ****************************************************************
			 */		
											
										
										$scope.procedureRequestMaster ={};	// temp obj for
																			// edit
																			// procedure
																			// (for
																			// isPrimarySecondary)
										$scope.procedureOrderList = [];
										$scope.addProcedureOrder = function() {
											$scope.procedureRequestMaster ={};	
											var checkProcedureExist=false; // to check
																			// procedure
																			// already exist
																			// or not
											var primeCheckflag = false;  // to make
																			// procedure
																			// defult
																			// primary add
																			// into
																			// procedureOrderList
																			// is greater
																			// than 0 length
																			// location
											
											// check for already Exist in current list
											if($scope.procedureOrderList.length>0){
												angular.forEach($scope.procedureOrderList,function(value,index){
													
													if(value.serviceId == $scope.procedureOrderMaster.serviceId && parseInt(value.procedureStatusId) == 1 && $scope.updateProcCurrentIdx != index){
														
														checkProcedureExist=true;
													}
												});
												}
											
											// check for already Exist in Saved list
											if($scope.orderedProcedureList.length>0){
												angular.forEach($scope.orderedProcedureList,function(value,index){
													
													if(value.serviceId == $scope.procedureOrderMaster.serviceId && parseInt(value.procedureStatusId) == 1 && $scope.updateProcCurrentIdx != index){
														
														checkProcedureExist=true;
													}
												});
												}
											
											if(checkProcedureExist){
												
												growl.warning("Procedure Already Exist", {
													title : 'warning!'
												});
												
												$scope.initProcedureOrder();
												$rootScope.stopLoader();
												
											}else{ 
												if($scope.procedureSearch=="" || $scope.procedureSearch==undefined){
													
													growl.warning("Please select Procedure", {
														title : 'warning!'
													});
													$rootScope.stopLoader();
													  return false;
												}else if($scope.procedureDate==undefined){
													
													growl.warning("Please select Procedure Date", {
														title : 'warning!'
													});
													$rootScope.stopLoader();
													  return false;
													
												}else if($scope.preferedTime==undefined || $scope.preferedTime==""){
			                                        growl.warning("Please select Procedure Time", {
			                                            title : 'warning!'
			                                        });
			                                        $rootScope.stopLoader();
			                                        return false;
												}
												else{
																							
												var procedureOrderMaster = angular.copy($scope.procedureOrderMaster);
												procedureOrderMaster.patientId = $scope.encounterDetailsObj.patientId;
												procedureOrderMaster.doctorId = $scope.encounterDetailsObj.doctorId;
												procedureOrderMaster.visitTypeId = $scope.encounterDetailsObj.visitTypeId;
												procedureOrderMaster.encounterId = $scope.encounterDetailsObj.encounterId;
												procedureOrderMaster.requestReason = $scope.procedureOrderMaster.requestReason;
												procedureOrderMaster.amount = $scope.rate;
												/*procedureOrderMaster.urgency =$scope.procedureOrderMaster.urgency;*/
											// procedureOrderMaster.preferedDateTime =
											// $scope.procedureDate;
			                                    procedureOrderMaster.preferedDateTime = moment($scope.procedureDate).format('DD-MM-YYYY');
			                                    procedureOrderMaster.preferedTime = $scope.preferedTime;
			                                    
			                                    if($scope.encounterDetailsObj.email==""){
													procedureOrderMaster.email = null;
												}else{
													procedureOrderMaster.email = $scope.encounterDetailsObj.email;
												}
			                                    
												
												procedureOrderMaster.listCalenderMapperDto = $scope.calandarSlotMpprList;
												procedureOrderMaster.implantDetailsList = $scope.implantList;
												procedureOrderMaster.medicationDetailsList = $scope.medicationList;
												procedureOrderMaster.procedureOrderConsentList = $scope.consentDetailList,
												procedureOrderMaster.procedureOrderCheckList = $scope.checklistDetailList,
												procedureOrderMaster.procedureOrderStaffList = $scope.staffDetailList,
												procedureOrderMaster.procedureOrderEquipmentList = $scope.equipmentDetailList,
												procedureOrderMaster.procedureOrderServiceList = $scope.finalServiceList,
												procedureOrderMaster.procedureOrderPatientEducationDto = $scope.patientEduObj
											
												if($scope.updateProcCurrentIdx!=-1 && $scope.updateProcCurrentIdx!=undefined){
													
													if(procedureOrderMaster.proReqId!=null && procedureOrderMaster.proReqId!=0){
														
														
														if(procedureOrderMaster.procedureSequence == 1){
															var procedureReqMaster = angular.copy($scope.procedureReqMaster);
															procedureReqMaster.patientId = $stateParams.patientDetailsObj.patientId;
															procedureReqMaster.visitTypeId = $stateParams.patientDetailsObj.visitTypeId;
															procedureReqMaster.admissionId = $stateParams.patientDetailsObj.admissionId;
															procedureReqMaster.preferedDateTime =  moment($scope.procedureDate).format('DD-MM-YYYY HH:mm:ss');
															procedureOrderMaster.procedureReqMasterDto = procedureReqMaster;
														}
														
														$scope.orderedProcedureList[$scope.updateProcCurrentIdx] = angular.copy(procedureOrderMaster);
														$scope.orderedProcedureList[$scope.updateProcCurrentIdx].isEdited = true;
													}else{
														

														if(procedureOrderMaster.procedureSequence == 1){
															var procedureReqMaster = angular.copy($scope.procedureReqMaster);
															procedureReqMaster.patientId = $stateParams.patientDetailsObj.patientId;
															procedureReqMaster.visitTypeId = $stateParams.patientDetailsObj.visitTypeId;
															procedureReqMaster.admissionId = $stateParams.patientDetailsObj.admissionId;
															procedureReqMaster.preferedDateTime =  moment($scope.procedureDate).format('DD-MM-YYYY HH:mm:ss');
															procedureOrderMaster.procedureReqMasterDto = procedureReqMaster;
														}
														
														$scope.procedureOrderList[$scope.updateProcCurrentIdx] = angular.copy(procedureOrderMaster);
													}
													
												}else{
													
													if(procedureOrderMaster.proReqId!=null && procedureOrderMaster.proReqId!=0){
													// add new procedure into saved request
														if($scope.orderedProcedureList.length == 0){
															var sequenceObj = $scope.procedureSequenceList.find(obj => {return obj.id == 1});
															procedureOrderMaster.procedureSequence = 1;
															procedureOrderMaster.isPrimarySecondary = 'Y';
															
															var procedureReqMaster = angular.copy($scope.procedureReqMaster);
															procedureReqMaster.patientId = $scope.encounterDetailsObj.patientId;
															procedureReqMaster.visitTypeId = $scope.encounterDetailsObj.visitTypeId;
															procedureReqMaster.encounterId = $scope.encounterDetailsObj.encounterId;
															procedureReqMaster.preferedDateTime =  moment($scope.procedureDate).format('DD-MMM-YY HH:mm:ss');
															procedureOrderMaster.procedureReqMasterDto = procedureReqMaster;
															
															if(sequenceObj == undefined){
																$scope.procedureSequenceList.push($scope.procedureSequenceMasterList[0]);
															}	
														}else{
															procedureOrderMaster.procedureSequence =  $scope.orderedProcedureList.length+1;
															var sequenceObj = $scope.procedureSequenceList.find(obj => {return obj.id == procedureOrderMaster.procedureSequence});
															if(sequenceObj == undefined){
																$scope.procedureSequenceList.push($scope.procedureSequenceMasterList[ $scope.orderedProcedureList.length]);
															}	
														}
														$scope.orderedProcedureList.push(procedureOrderMaster);
													}else{
														if($scope.procedureOrderList.length == 0){
															var sequenceObj = $scope.procedureOrderedSequenceList.find(obj => {return obj.id == 1});
															procedureOrderMaster.procedureSequence = 1;
															procedureOrderMaster.isPrimarySecondary = 'Y';
															
															var procedureReqMaster = angular.copy($scope.procedureReqMaster);
															procedureReqMaster.patientId = $scope.encounterDetailsObj.patientId;
															procedureReqMaster.visitTypeId = $scope.encounterDetailsObj.visitTypeId;
															procedureReqMaster.encounterId = $scope.encounterDetailsObj.encounterId;
															procedureReqMaster.preferedDateTime =  moment($scope.procedureDate).format('DD-MMM-YY HH:mm:ss');
															procedureOrderMaster.procedureReqMasterDto = procedureReqMaster;
															
															if(sequenceObj == undefined){
																$scope.procedureOrderedSequenceList.push($scope.procedureSequenceMasterList[0]);
															}	
														}else{
															procedureOrderMaster.procedureSequence =  $scope.procedureOrderList.length+1;
															var sequenceObj = $scope.procedureOrderedSequenceList.find(obj => {return obj.id == procedureOrderMaster.procedureSequence});
															if(sequenceObj == undefined){
																$scope.procedureOrderedSequenceList.push($scope.procedureSequenceMasterList[ $scope.procedureOrderList.length]);
															}	
														}
															$scope.procedureOrderList.push(procedureOrderMaster);
													}
												}

												$scope.pDate = $scope.procedureDate;
												$scope.initProcedureOrder();
													
												}		
											}
											console.log("$scope.procedureOrderList dfvbjkdfjbvfigbviufbv",$scope.procedureOrderList);
										}
										
										
										// change procedure sequence
										$scope.changeProcedureSequence = function(index,flag){
											if(flag == 1){
												var isprocedureSequenceExist = false;
												if($scope.orderedProcedureList!=null && $scope.orderedProcedureList.length>0){
													angular.forEach($scope.orderedProcedureList,function(v1,i1){
														angular.forEach($scope.procedureOrderList,function(value,idx){
															if(idx!=index){
																if(v1.proReqId == value.proReqId && v1.procedureSequence == value.procedureSequence){
																	isprocedureSequenceExist = true;
																}
															}
														});
													});
												}
												
												if(isprocedureSequenceExist){
													 growl.warning("Selected Procedure Sequence Is Already Exist", {
					                                        title : 'warning!'
					                                    });
													
													$scope.procedureOrderList[index].procedureSequence = $scope.procedureOrderedSequenceList[0].id;
													return false;
												}
												
												var isDuplicatePrimary = false;
												var isDuplicateSecondary = false;
												var isDuplicateTertiary = false;
												var isDuplicateQuaternary = false;
												var isDuplicateQuinary = false;
												
												angular.forEach($scope.procedureOrderList,function(value,idx){
													if(idx!=index && $scope.procedureOrderList[index].procedureSequence==1){
														if($scope.procedureOrderList[index].proReqId == value.proReqId && value.procedureSequence == 1){
															isDuplicatePrimary = true;
														}
													}else if(idx!=index && $scope.procedureOrderList[index].procedureSequence==2){
														if($scope.procedureOrderList[index].proReqId == value.proReqId && value.procedureSequence == 2){
															isDuplicateSecondary = true;
														}
													}else if(idx!=index && $scope.procedureOrderList[index].procedureSequence==3){
														if($scope.procedureOrderList[index].proReqId == value.proReqId && value.procedureSequence == 3){
															isDuplicateTertiary = true;
														}
													}else if(idx!=index && $scope.procedureOrderList[index].procedureSequence==4){
														if($scope.procedureOrderList[index].proReqId == value.proReqId && value.procedureSequence == 4){
															isDuplicateQuaternary = true;
														}
													}else if(idx!=index && $scope.procedureOrderList[index].procedureSequence==5){
														if($scope.procedureOrderList[index].proReqId == value.proReqId && value.procedureSequence == 5){
															isDuplicateQuinary = true;
														}
													}
												});
												
												if(isDuplicatePrimary){
													 growl.warning("Primary Procedure Is Already Exist", {
					                                        title : 'warning!'
					                                    });
													
													$scope.procedureOrderList[index].procedureSequence = $scope.procedureOrderedSequenceList[0].id;
													return false;
												}
												
												if(isDuplicateSecondary){
													 growl.warning("Secondary Procedure Is Already Exist", {
					                                        title : 'warning!'
					                                    });
													
													$scope.procedureOrderList[index].procedureSequence = $scope.procedureOrderedSequenceList[0].id;
													return false;
												}
												
												if(isDuplicateTertiary){
													 growl.warning("Tertiary Procedure Is Already Exist", {
					                                        title : 'warning!'
					                                    });
													
													$scope.procedureOrderList[index].procedureSequence = $scope.procedureOrderedSequenceList[0].id;
													return false;
												}
												
												if(isDuplicateQuaternary){
													 growl.warning("Quaternary Procedure Is Already Exist", {
					                                        title : 'warning!'
					                                    });
													
													$scope.procedureOrderList[index].procedureSequence = $scope.procedureOrderedSequenceList[0].id;
													return false;
												}
												
												if(isDuplicateQuinary){
													 growl.warning("Quinary Procedure Is Already Exist", {
					                                        title : 'warning!'
					                                    });
													
													$scope.procedureOrderList[index].procedureSequence = $scope.procedureOrderedSequenceList[0].id;
													return false;
												}
												$scope.procedureOrderList[index].procedureSequence = parseInt($scope.procedureOrderList[index].procedureSequence);
												if($scope.procedureOrderList[index].procedureSequence == 1){
													$scope.procedureOrderList[index].isPrimarySecondary = 'Y';
													
													var procedureReqMaster = angular.copy($scope.procedureReqMaster);
													procedureReqMaster.patientId = $scope.encounterDetailsObj.patientId;
													procedureReqMaster.visitTypeId = $scope.encounterDetailsObj.visitTypeId;
													procedureReqMaster.encounterId = $scope.encounterDetailsObj.encounterId;
													procedureReqMaster.preferedDateTime =  moment($scope.procedureDate).format('DD-MMM-YY HH:mm:ss');
													$scope.procedureOrderList[index].procedureReqMasterDto = procedureReqMaster;
													
												}else{
													$scope.procedureOrderList[index].isPrimarySecondary = 'N';
													$scope.procedureOrderList[index].procedureReqMasterDto = {};
												}
												
											}else{
												var isDuplicatePrimary = false;
												var isDuplicateSecondary = false;
												var isDuplicateTertiary = false;
												var isDuplicateQuaternary = false;
												var isDuplicateQuinary = false;
												angular.forEach($scope.orderedProcedureList,function(value,idx){
													if(idx!=index && $scope.orderedProcedureList[index].procedureSequence==1){
														if($scope.orderedProcedureList[index].proReqId == value.proReqId && value.procedureSequence == 1){
															isDuplicatePrimary = true;
														}
													}else if(idx!=index && $scope.orderedProcedureList[index].procedureSequence==2){
														if($scope.orderedProcedureList[index].proReqId == value.proReqId && value.procedureSequence == 2){
															isDuplicateSecondary = true;
														}
													}else if(idx!=index && $scope.orderedProcedureList[index].procedureSequence==3){
														if($scope.orderedProcedureList[index].proReqId == value.proReqId && value.procedureSequence == 3){
															isDuplicateTertiary = true;
														}
													}else if(idx!=index && $scope.orderedProcedureList[index].procedureSequence==4){
														if($scope.orderedProcedureList[index].proReqId == value.proReqId && value.procedureSequence == 4){
															isDuplicateQuaternary = true;
														}
													}else if(idx!=index && $scope.orderedProcedureList[index].procedureSequence==5){
														if($scope.orderedProcedureList[index].proReqId == value.proReqId && value.procedureSequence == 5){
															isDuplicateQuinary = true;
														}
													}
												});
												if(isDuplicatePrimary){
													 growl.warning("Primary Procedure Is Already Exist", {
					                                        title : 'warning!'
					                                    });
													
													$scope.orderedProcedureList[index].procedureSequence = $scope.procedureSequenceList[0].id;
													return false;
												}
												
												if(isDuplicateSecondary){
													 growl.warning("Secondary Procedure Is Already Exist", {
					                                        title : 'warning!'
					                                    });
													
													$scope.orderedProcedureList[index].procedureSequence = $scope.procedureSequenceList[0].id;
													return false;
												}
												
												if(isDuplicateTertiary){
													 growl.warning("Tertiary Procedure Is Already Exist", {
					                                        title : 'warning!'
					                                    });
													
													$scope.orderedProcedureList[index].procedureSequence = $scope.procedureSequenceList[0].id;
													return false;
												}
												
												if(isDuplicateQuaternary){
													 growl.warning("Quaternary Procedure Is Already Exist", {
					                                        title : 'warning!'
					                                    });
													
													$scope.orderedProcedureList[index].procedureSequence = $scope.procedureSequenceList[0].id;
													return false;
												}
												
												if(isDuplicateQuinary){
													 growl.warning("Quinary Procedure Is Already Exist", {
					                                        title : 'warning!'
					                                    });
													
													$scope.orderedProcedureList[index].procedureSequence = $scope.procedureSequenceList[0].id;
													return false;
												}
												
												if($scope.orderedProcedureList[index].procedureSequence == 1){
													$scope.orderedProcedureList[index].isPrimarySecondary = 'Y';
												}else{
													$scope.orderedProcedureList[index].isPrimarySecondary = 'N';
												}
												$scope.orderedProcedureList[index].isEdited = true;
											}
										}
										
										
										$scope.orderedProcedureList = [];
										$scope.addNewProc = function(){
											   if($scope.procedureDate==undefined){
				                                    growl.warning("Please select Procedure Date", {
				                                        title : 'warning!'
				                                    });
				                                }else if($scope.preferedTime==undefined || $scope.preferedTime==""){
			                                  growl.warning("Please select Procedure Time", {
			                                      title : 'warning!'
			                                  });
			                                  $rootScope.stopLoader();
			                                  return false;
												}else{
											var currTime = moment(new Date()).format('HH:mm');   
											/* var tempTime = currTime.spit(" ") */
											  var oldprocedureDate = moment($scope.procedureDate).format('YYYY-MM-DD');
											  var todayDates = moment(new Date()).format('YYYY-MM-DD');
											if(oldprocedureDate == todayDates && currTime > $scope.preferedTime){
												
												growl.warning("Please select valid Time", {
					                                  title : 'warning!'});
												 $rootScope.stopLoader();
					                              return false;
												
											}else{
												
												if($scope.orderedProcedureList.length>0){
													
													$scope.proReqIds = []; // for popup to
																			// select
																			// procedure
																			// request
																			// number
													    angular.forEach($scope.orderedProcedureList, function(value, key) {
													    	var newArr = {};
													    	newArr.proReqId = value.proReqId;
													    	  var proReqIdsCount = false;
													    	angular.forEach($scope.proReqIds, function(value1, key1) {
															    	
															    	if(value.proReqId == value1.proReqId){
															    		proReqIdsCount = true;
															    	}
															    });
													    	  var oldpreferedDate = moment(moment(value.preferedDateTime,'DD-MM-YYYY').toDate()).format('YYYY-MM-DD');
													    	  if(proReqIdsCount == false &&  oldpreferedDate == oldprocedureDate){
														    		
														    		$scope.proReqIds.push(newArr);	
														    	}
													    });
													    
													    if($scope.procedureOrderMaster.proReqId == 0 && $scope.proReqIds.length>0 && $scope.updateProcCurrentIdx == -1){
													    	
													    	angular.element('#addNewProcedure').modal('show');
													    }else{
															
															$scope.addProcedureOrder();
														}
												}else{
													
													$scope.addProcedureOrder();
												}
											
			                              $rootScope.stopLoader();
			                              return false;
											}
											
				                                }
										}
										
										
										$scope.addAllProcedure = function(){
											if($scope.diagnosticOrNot == true){
												var newArr = {};
												newArr.specialityId = $scope.diagnosticSpecialityId;
												newArr.serviceSpecialityId = $scope.diagnosticSpecialityId;
												newArr.serviceSubSpecialityId = $scope.diagnosticSubSpecialityId;
												newArr.subSpecialityId = $scope.diagnosticSubSpecialityId;
												newArr.serviceMasterId = $scope.procedureOrderMaster.serviceId;
												newArr.serviceStandardName = $scope.procedureOrderMaster.procedureName;
												newArr.specialityName = $scope.diagnosticSpecialityName;
												newArr.subSpecialityName = $scope.diagnosticSubSpecialityName;
												newArr.serviceRenderingDeptId = $scope.diagnosticSubSpecialityId;
												newArr.preferedDateTime = moment($scope.procedureDate).format('DD-MM-YYYY');
												newArr.preferedTime = $scope.preferedTime;
												newArr.requestReason = $scope.procedureOrderMaster.requestReason;
												$scope.diagnosticProcedureList.push(newArr);
												$scope.initProcedureOrder();
											}else{
												$scope.addNewProc();
											}
										}
										
			/**
			 * ************************************************************** add Procedure
			 * Order End ****************************************************************
			 */								
			                            $scope.idxForCancelOrderedProc = "";
			                            $scope.cancelProcedure = function(index,removeFlag){
			                                if(removeFlag == 2){  
			                                	
			                                	if($scope.orderedProcedureList[index].procedureOrderId==null || $scope.orderedProcedureList[index].procedureOrderId == 0 || $scope.orderedProcedureList[index].procedureOrderId == "")
			                                	{
			                                		$scope.removeProcedureOder(index,2);
			                                	}else{
			                                		$scope.idxForCancelOrderedProc = index;
			                                		angular.element('#cancelOrderedProcedure').modal('show');
			                                	}	
			                                }
			                            }

										
										$scope.tempProcList  = []; // removed procedures
																	// with inactive flag
										$scope.removeProcedureOder = function(index,removeFlag) {
											var procedureSequence;
											if(removeFlag == 1){  // removeFlag == 1 for
																	// procedureOrderList
												
												if($scope.procedureOrderList.length>0){
														procedureSequence = $scope.procedureOrderList[index].procedureSequence;
														$scope.procedureOrderedSequenceList.splice($scope.procedureOrderList.length,1);
														$scope.procedureOrderList.splice(index,1);
														angular.forEach($scope.procedureOrderList,function(value,idx){
															if(value.procedureSequence > procedureSequence){
																value.procedureSequence = value.procedureSequence -1;
															}
														});
													}
											}else if(removeFlag == 2){ // removeFlag == 2
																		// for
																		// orderedProcedureList

												if($scope.orderedProcedureList[index].procedureOrderId==null || $scope.orderedProcedureList[index].procedureOrderId == 0 || $scope.orderedProcedureList[index].procedureOrderId == "")
			                                	{ // when added new procedure in saved
													// list then remove it without selecting
													// reason
													procedureSequence = $scope.orderedProcedureList[index].procedureSequence;
													$scope.procedureSequenceList.splice($scope.orderedProcedureList.length,1);
													$scope.orderedProcedureList.splice(index,1);
													angular.forEach($scope.orderedProcedureList,function(value,idx){
														if(value.procedureSequence > procedureSequence){
															value.procedureSequence = value.procedureSequence -1;
															 value.isEdited = true;
														}
													});
													
			                                	}else{
			                                		if($scope.orderedProcedureList[$scope.idxForCancelOrderedProc].reasonId == "" || $scope.orderedProcedureList[$scope.idxForCancelOrderedProc].reasonId == null || $scope.orderedProcedureList[$scope.idxForCancelOrderedProc].reasonId == undefined){
			    										
			    										growl.warning("Please select reason..!!", {
			    											title : 'Warning!'
			    										});
			    										return false;
			    										
			    										}else{
			    										 angular.element('#cancelOrderedProcedure').modal('hide');
			    										 $scope.orderedProcedureList[index].status="I";
			    										 var obj = angular.copy($scope.orderedProcedureList[index]);
			    										 $scope.tempProcList.push(obj);
			    									
			    										  procedureSequence = $scope.orderedProcedureList[index].procedureSequence;
			    										 $scope.procedureSequenceList.splice($scope.orderedProcedureList.length,1);
			    										 $scope.orderedProcedureList.splice(index,1);
			    										 angular.forEach($scope.orderedProcedureList,function(value,idx){
			    											 if(value.procedureSequence > procedureSequence){
			    												 value.procedureSequence = value.procedureSequence -1;
			    												 value.isEdited = true;
			    											 }
			    										 });
			    										}
			                                	}
											}else{
												$scope.diagnosticProcedureList.splice(index, 1);
											}
											
										}
										
										$scope.editProcedureOder = function(procedreOrderObj,idx){
											
											if(procedreOrderObj.specialityId != ""){
												$scope.procedureSubSpeciality.specialityId = procedreOrderObj.specialityId;
											}
											$scope.procedureSubSpeciality.specialityName = procedreOrderObj.specialityName;
											$scope.procedureSearch = procedreOrderObj.procedureName;
											$scope.procedureOrderMaster.procedureName =  procedreOrderObj.procedureName;
											$scope.procedureOrderMaster.specialityId = procedreOrderObj.specialityId;
											$scope.procedureOrderMaster.specialityName = procedreOrderObj.specialityName;
											$scope.procedureOrderMaster.serviceId = procedreOrderObj.serviceId;
											$scope.procedureOrderMaster.isIcuRequired = procedreOrderObj.isIcuRequired;
											$scope.procedureOrderMaster.amount = procedreOrderObj.amount;
											$scope.procedureOrderMaster.createdBy =  procedreOrderObj.createdBy;
			                                $scope.preferedTime =  procedreOrderObj.preferedTime;
			                                var updateDate = procedreOrderObj.preferedDateTime.split(" ");
			                                var newdate = updateDate[0].split("-").reverse().join("-");
			                                $scope.procedureDate = moment(newdate,'DD-MMM-YY').toDate();
											$scope.procedureOrderMaster.createdDate =  procedreOrderObj.createdDate;
											$scope.procedureOrderMaster.isBloodComponentRequired =  procedreOrderObj.isBloodComponentRequired;
											$scope.procedureOrderMaster.isIcuRequired =  procedreOrderObj.isIcuRequired;
											$scope.procedureOrderMaster.isPacRequired =  procedreOrderObj.isPacRequired;
											$scope.procedureOrderMaster.isPrimarySecondary =  procedreOrderObj.isPrimarySecondary;
											$scope.procedureOrderMaster.orgId =  procedreOrderObj.orgId;
											$scope.procedureOrderMaster.status =  procedreOrderObj.status;
											$scope.procedureOrderMaster.unitId =  procedreOrderObj.unitId;
											$scope.procedureOrderMaster.updatedBy =  procedreOrderObj.updatedBy;
											$scope.procedureOrderMaster.updatedDate =  procedreOrderObj.updatedDate;
											// $scope.procedureDate =
											// procedreOrderObj.preferedDateTime;
											$scope.procedureOrderMaster.urgency = procedreOrderObj.urgency;
											$scope.procedureOrderMaster.requestReason = procedreOrderObj.requestReason;
											$scope.procedureOrderMaster.procedureSequence = procedreOrderObj.procedureSequence;
											// $scope.procedureReqMaster =
											// angular.copy($scope.procedureRequestMaster);
											$scope.procedureReqMaster.proReqId = procedreOrderObj.proReqId;
											
											$scope.procedureOrderMaster.proReqId = procedreOrderObj.proReqId;
											$scope.procedureOrderMaster.procedureOrderId = procedreOrderObj.procedureOrderId;
											$scope.implantList = procedreOrderObj.implantDetailsList;
											$scope.medicationList = procedreOrderObj.medicationDetailsList;
											$scope.consentDetailList = procedreOrderObj.procedureOrderConsentList;
											$scope.checklistDetailList = procedreOrderObj.procedureOrderCheckList;
											$scope.staffDetailList = procedreOrderObj.procedureOrderStaffList;
											$scope.equipmentDetailList = procedreOrderObj.procedureOrderEquipmentList;
											$scope.finalServiceList = procedreOrderObj.procedureOrderServiceList;
											$scope.patientEduObj = procedreOrderObj.procedureOrderPatientEducationDto;
											$scope.updateProcCurrentIdx = idx; // to get
																				// current
																				// saved
																				// procedure
																				// index
										}
										
								
			/**
			 * ************************************************************** Save Procedure
			 * Order Start ****************************************************************
			 * 
			 */							$scope.addedServiceOrders = [];
										$scope.addedServiceOrdersForDisplay = [];
										$scope.newAddedServiceOrdersForDisplay = [];	
										
										
										$scope.finalDiagnosticProcedureList = []; 
										$scope.generateDaignoticProcedure = function(){
											$scope.finalDiagnosticProcedureList = [];
											
												angular.forEach($scope.diagnosticProcedureList,function(value1,index1){
													
													var newArr = {};
													
													newArr.specialityId = value1.specialityId;
													newArr.serviceSpecialityId = value1.serviceSpecialityId;
													newArr.serviceSubSpecialityId = value1.serviceSubSpecialityId;
													newArr.subSpecialityId = value1.subSpecialityId;
													newArr.serviceMasterId = value1.serviceMasterId;
													newArr.serviceStandardName = value1.serviceStandardName;
													newArr.specialityName = value1.specialityName;
													newArr.subSpecialityName = value1.subSpecialityName;
													newArr.serviceRenderingDeptId = value1.serviceRenderingDeptId;
													newArr.createdBy = $scope.userId;
													newArr.createdDate = new Date().getTime();
													newArr.selfPayable = "0";
													newArr.creditPayable = "0";
													newArr.serviceRendered = "0";
													newArr.doctorId = $scope.encounterDetailsObj.doctorId;
													newArr.discountPercentage = 0;
													newArr.discount = 0;
													newArr.quantity = 1;
													newArr.basePrice = 0;
													newArr.basePriceOld = 0;
													newArr.totalAmt = 0;
													newArr.concession = 0;
													newArr.payeeId =  $scope.encounterDetailsObj.payeeId;
													newArr.apportionedPrice = null;
													newArr.packageId = null;
													newArr.packageTypeId = null;
													newArr.packageEitherOrGroupId = null;
													newArr.packageCapDetailsId = null;
													newArr.isCapService = null;
													newArr.status = 'A';
													newArr.isRefDoctorShare = $scope.orderIsRefDoctorShare == undefined ? null: $scope.orderIsRefDoctorShare;
													newArr.packageEitherOrGroupId = null;
													newArr.isPackageService = 'N';
													newArr.discountDetailsList = [];
													newArr.selectedForFuture ='N';
													newArr.isInvestigationOrder ='N';
													$scope.finalDiagnosticProcedureList.push(newArr);
													
												});

										}
										
										$scope.proceduerReqId = undefined;
										$scope.saveProcedureOrder = function() {
											
											console.log('$scope.procedureOrderList save Data',$scope.procedureOrderList);
											$rootScope.startLoader();
											var isEditableExist  = true;
											if($scope.orderedProcedureList.length>0){
												var isExistFound = $scope.orderedProcedureList.find(arr=>{ return (arr.isEdited) || (arr.procedureOrderId == "")}); // to
																																									// check
																																									// edited
																																									// or
																																									// added
																																									// new
																																									// procedure
																																									// with
																																									// same
																																									// (saved)request
																																									// id
																																									// then
																																									// allow
																																									// to
																																									// save
																																									// that
																																									// req
												if(isExistFound!=undefined){
													isEditableExist = false;
												}
											}
											if ($scope.procedureOrderList.length > 0 || $scope.tempProcList.length > 0 || !isEditableExist) { // 
												var counter=0;
												
												
												angular.forEach($scope.procedureOrderList,function(value,index){
													
													if(value.procedureOrderId == ""){
													
														angular.forEach(value.procedureOrderServiceList,function(value1,index1){
															
															var newArr = {};
															
															newArr.specialityId = value1.specialityId;
															newArr.serviceSpecialityId = value1.specialityId;
															newArr.serviceSubSpecialityId = value1.subSpecialityId;
															newArr.subSpecialityId = value1.subSpecialityId;
															newArr.serviceMasterId = value1.serviceId;
															newArr.serviceStandardName = value1.serviceStandardName;
															newArr.specialityName = value1.specialityName;
															newArr.subSpecialityName = value1.subSpecialityName;
															newArr.serviceRenderingDeptId = value1.subSpecialityId;
															newArr.createdBy = $scope.userId;
															newArr.createdDate = new Date().getTime();
															newArr.selfPayable = "0";
															newArr.creditPayable = "0";
															newArr.serviceRendered = "0";
															newArr.doctorId = $scope.encounterDetailsObj.doctorId;
															newArr.discountPercentage = 0;
															newArr.discount = 0;
															newArr.quantity = 1;
															newArr.basePrice = 0;
															newArr.basePriceOld = 0;
															newArr.totalAmt = 0;
															newArr.concession = 0;
															newArr.payeeId =  $scope.encounterDetailsObj.payeeId;
															newArr.apportionedPrice = null;
															newArr.packageId = null;
															newArr.packageTypeId = null;
															newArr.packageEitherOrGroupId = null;
															newArr.packageCapDetailsId = null;
															newArr.isCapService = null;
															newArr.status = 'A';
															newArr.isRefDoctorShare = $scope.orderIsRefDoctorShare == undefined ? null: $scope.orderIsRefDoctorShare;
															newArr.packageEitherOrGroupId = null;
															newArr.isPackageService = 'N';
															newArr.discountDetailsList = [];
															newArr.selectedForFuture ='N';
															newArr.isInvestigationOrder ='N';
															$scope.newAddedServiceOrdersForDisplay.push(newArr);
															
														});
														
													}
													
												});
												
												if($scope.newAddedServiceOrdersForDisplay.length>0){
													 angular.forEach($scope.newAddedServiceOrdersForDisplay, function(value, key) {

													        var exists = false;

													        angular.forEach($scope.addedServiceOrdersForDisplay, function(val2, key) {

													          if(angular.equals(value.serviceMasterId, val2.serviceMasterId)){ exists = true }; 

													        });

													        if(exists == false) { $scope.addedServiceOrdersForDisplay.push(value); }

													      });
													console.log("$scope.addedServiceOrdersForDisplaysssssss",$scope.addedServiceOrdersForDisplay);
												}
												
												// return false;
												if ($scope.diagnosticProcedureList.length > 0) {
													
													$scope.generateDaignoticProcedure();
													
													$scope.addedServiceOrdersForDisplay = $scope.addedServiceOrdersForDisplay.concat($scope.finalDiagnosticProcedureList);
												}
												
												// only one procedure should primary in same
												// procedure request
												angular.forEach($scope.procedureOrderList,function(value,index){
													if(value.procedureSequence == 1){
														var procedureReqMaster = angular.copy($scope.procedureReqMaster);
														procedureReqMaster.patientId = $scope.encounterDetailsObj.patientId;
														procedureReqMaster.visitTypeId = $scope.encounterDetailsObj.visitTypeId;
														procedureReqMaster.encounterId = $scope.encounterDetailsObj.encounterId;
														procedureReqMaster.preferedDateTime =  moment($scope.procedureDate).format('DD-MMM-YY HH:mm:ss');
														$scope.procedureOrderList[index].procedureReqMasterDto = procedureReqMaster;
														value.procedureReqMasterDto = procedureReqMaster;
													}
													if(value.procedureSequence ==1 && value.proReqId == 0){
														
														counter++;
														
													}else if(value.procedureSequence != 1 && value.proReqId != 0) {
														
														counter++;
														
													}else if(value.procedureSequence == 1 && value.proReqId != 0) {
														
														counter++;
													}
												});
												
												var saveFlagProcedureOrder = false;
												if(counter == 0 ){
													
													if($scope.tempProcList.length == 0){
													
														saveFlagProcedureOrder = true;
													}
												}
												 /*if ($scope.procedreOrderObj.urgency== undefined || $scope.procedreOrderObj.urgency == '') {
														growl.warning(
																"Please select priority..!", {
																	title : 'Warning!'
																});
														return;
													}*/
												if(saveFlagProcedureOrder && isEditableExist){
														
													growl.warning("Please select at least one Primary Procedure", {
															title : 'Warning!'
														});
														$rootScope.stopLoader();
												}else{
													$scope.newProcList = [];
													
													angular.forEach($scope.procedureOrderList,function(value,index){
														
														if(value.procedureSequence == 1){
															$scope.newProcList.push(value);
															
															var oldpreferedDateTime = value.preferedDateTime.split(" ");
															var oldpreferedDate = oldpreferedDateTime[0];
															
															$scope.procedureOrderList.splice(index,1);
															angular.forEach($scope.procedureOrderList,function(value1,index1){
																var oldprocedureDateTime = value1.preferedDateTime.split(" ");
																var oldprocedureDate = oldprocedureDateTime[0];
																if(value1.procedureSequence != 1 && oldpreferedDate == oldprocedureDate){
																	$scope.newProcList.push(value1);
																	$scope.procedureOrderList.splice(index1,1);
																}
															});
														}
													});
													
													if($scope.procedureOrderList.length>0){
														$scope.newProcList = $scope.newProcList.concat($scope.procedureOrderList); // concat
																																	// remaning
																																	// procedure
																																	// to
																																	// newProcList
													}
													if($scope.tempProcList.length>0){
														$scope.newProcList = $scope.newProcList.concat($scope.tempProcList); // concat
																																// removed
																																// procedure
																																// to
																																// newProcList
													}
													
													if($scope.orderedProcedureList.length>0){
														
														angular.forEach($scope.orderedProcedureList,function(value,index){
															
																if((value.isEdited != undefined && value.isEdited == true) || value.procedureOrderId == ""){
																
																	 $scope.newProcList.push(value); // concat
																										// remaning
																										// procedure
																										// to
																										// newProcList
															
																}
														});
													}
													
													if($scope.newProcList!=null && $scope.newProcList.length>0){
														var isExistFound = $scope.newProcList.find(arr=>{ return (arr.procedureSequence == 0 || arr.procedureSequence == '' )}); // check
																																													// for
																																													// blan
																																													// procedure
																																													// sequence
														if(isExistFound!=undefined){
															growl.warning("Please select Procedure Sequence ", {
																title : 'Warning!'
															});
															$rootScope.stopLoader();
															return false;
														}
													}
													
													console.log("$scope.newProcList",$scope.newProcList);
													var URI = BASE_URL + ROOT_URL+ "/api/procedure/procedureOrder";
													console.log('$scope.procedureOrderList',JSON.stringify($scope.newProcList));
													GenericServiceParamHeader
															.serviceAction(METHOD_POST, URI,
																	$scope.newProcList,
																	NOTIFICATION_MSG_STATUS_TRUE)
															.then(
																	function(response) {
																		
																		if (response.data.status == "success")
																		{
																			
																			$scope.procedureOrderList = [];
																			$scope.tempProcList  = [];
																			$scope.newProcList = [];
																			/*$scope.procedureOrderMaster =[];*/
																			$scope.proceduerReqId = response.data.object.proReqId;
																			$rootScope.stopLoader();
																			$scope.init();
																			$scope.diagnosticProcedureList = [];
																			$scope.setSecondTabLevel(6);
																			$scope.patientEduObj = {
																					patientEducationTempale:"",
																					procedureOrderPatientEducationId:null,
																					organizationId:$scope.organizationId,
																					unitId:$scope.unitId,
																					createdBy:$scope.userId,
																					updatedBy:$scope.userId,
																					status:'A',
																					createdDate:$scope.dt,
																					updatedDate:$scope.dt
																			};
																			// $scope.getDoctorySpecilaity();
																			if($scope.addedServiceOrdersForDisplay!=null  && $scope.addedServiceOrdersForDisplay!=undefined && $scope.addedServiceOrdersForDisplay.length>0){
																				$scope.saveInvestigation();
																			}
																			$timeout(function () {
																				 $scope.getOrderProcedureList();
																			 }, 7000);
																			$scope.procedureOrderedSequenceList = [
																				{
																					id : 0,
																					sequenceName : " "
																				}
																				];
																			$scope.procedureSequenceList = [
																				{
																					id : 0,
																					sequenceName : " "
																				}
																				];
																			$scope.initProcedureOrder();
																			
																		} else if (response.data.status == "error") {
																			$rootScope.stopLoader();
																			$scope.newProcList = [];
																		}

																	});
												}

												} else {
													
														if ($scope.diagnosticProcedureList.length > 0) {
														
														$scope.generateDaignoticProcedure();
														
														$scope.addedServiceOrdersForDisplay = $scope.addedServiceOrdersForDisplay.concat($scope.finalDiagnosticProcedureList);
														
														if($scope.addedServiceOrdersForDisplay!=null  && $scope.addedServiceOrdersForDisplay!=undefined && $scope.addedServiceOrdersForDisplay.length>0){
															$scope.saveInvestigation();
															$scope.diagnosticProcedureList = [];
															$timeout(function () {
																 $scope.getOrderProcedureList();
															 }, 7000);
															$scope.initProcedureOrder();
														}
														
													}else{
														growl.warning("Please add procedure", {
															title : 'Warning!'
														});
														$rootScope.stopLoader();
													}
													
											}
										}

										$scope.initProcedureOrder();
										
			/**
			 * ************************************************************** Save Procedure
			 * Order End ****************************************************************
			 */								
										$scope.diagnosticProcedureList1 = [];
										$scope.getOrderProcedureList = function() {
											$scope.startLoader();
											var d = {};
											var URI = BASE_URL
													+ ROOT_URL
													+ "/api/procedure/getProcedureOrderListByAdmissionId/0/"
													+ $scope.encounterDetailsObj.encounterId+"/0";

											GenericService.serviceAction("GET", URI, d).then(
															function(response) {
																if (response.data.status == "success") {
																	
																	$scope.stopLoader();
																	
																	if(response.data.listObject.length>0){
																		$scope.diagnosticProcedureList1 = [];
																		$scope.orderedProcedureList = [];
																		$scope.procedureSequenceList = [
																			{
																				id : 0,
																				sequenceName : " "
																			}
																			];
																		$scope.proSequenceIds = [];
																		angular.forEach(response.data.listObject,function(value,index){
																			if(value.procedureTypeId == 3){
																				var arr = {};
																				arr.procedureSequence = value.procedureSequence;
																				$scope.proSequenceIds.push(arr);
																				$scope.orderedProcedureList.push(value);
																			}else{
																				var newArr = {};
																				newArr.serviceStandardName = value.procedureName;
																				newArr.specialityName = value.specialityName;
																				newArr.preferedDateTime = value.preferedDateTime;
																				newArr.preferedTime = value.preferedTime;
																				newArr.requestReason = "";
																				$scope.diagnosticProcedureList1.push(newArr);
																			}
																		});
																		
																		if($scope.proSequenceIds.length>0){
																			$scope.proSequenceIds = $filter('orderBy')($scope.proSequenceIds, 'procedureSequence', false) ;
																			angular.forEach($scope.proSequenceIds , function(val , idx){
																				if(val.procedureSequence == 1){
																					var newArr = {
																							id : 1,
																							sequenceName : "Primary"
																					};
																					if($scope.procedureSequenceList.length>0){
																						var obj = $scope.procedureSequenceList.find(ar=>ar.id == 1);
																						if(obj == undefined){
																							$scope.procedureSequenceList.push(newArr);
																						}
																					}else{
																						$scope.procedureSequenceList.push(newArr);
																					}
																				}else if(val.procedureSequence == 2){
																					var newArr = {
																							id : 2,
																							sequenceName : "Secondary"
																					};
																					if($scope.procedureSequenceList.length>0){
																						var obj = $scope.procedureSequenceList.find(ar=>ar.id == 2);
																						if(obj == undefined){
																							$scope.procedureSequenceList.push(newArr);
																						}
																					}else{
																						$scope.procedureSequenceList.push(newArr);
																					}
																				}else if(val.procedureSequence == 3){
																					var newArr = {
																							id : 3,
																							sequenceName : "Tertiary"
																					};
																					if($scope.procedureSequenceList.length>0){
																						var obj = $scope.procedureSequenceList.find(ar=>ar.id == 3);
																						if(obj == undefined){
																							$scope.procedureSequenceList.push(newArr);
																						}
																					}else{
																						$scope.procedureSequenceList.push(newArr);
																					}
																				}else if(val.procedureSequence == 4){
																					var newArr = {
																							id : 4,
																							sequenceName : "Quaternary"
																					};
																					if($scope.procedureSequenceList.length>0){
																						var obj = $scope.procedureSequenceList.find(ar=>ar.id == 4);
																						if(obj == undefined){
																							$scope.procedureSequenceList.push(newArr);
																						}
																					}else{
																						$scope.procedureSequenceList.push(newArr);
																					}
																				}else if(val.procedureSequence == 5){
																					var newArr = {
																							id : 5,
																							sequenceName : "Quinary"
																					};
																					if($scope.procedureSequenceList.length>0){
																						var obj = $scope.procedureSequenceList.find(ar=>ar.id == 5);
																						if(obj == undefined){
																							$scope.procedureSequenceList.push(newArr);
																						}
																					}else{
																						$scope.procedureSequenceList.push(newArr);
																					}
																				}
																			});
																		}
																	}
																	console.log("$scope.orderedProcedureList",$scope.orderedProcedureList);
																}
															});
											$scope.stopLoader();
										}
										$scope.getOrderProcedureList();
										
										$scope.procedureDisabledFlag=false;
										$scope.getProcedureConfigDetails = function(index,indexFlag){
										
											$scope.setThirdTabLevel(40);
											$scope.currentIndex = "";
											$scope.orderedCurrentIndex = "";
											$scope.indexFlag="";
											$scope.procedureDisabledFlag=false;
											$scope.indexFlag=indexFlag;
											
											if(indexFlag == 1){
												$scope.currentIndex =index;
											}else{
												$scope.orderedCurrentIndex =index;
												$scope.procedureDisabledFlag=true;
											}
										}
										
										

										$scope.minusProcedureDetails = true;
										$scope.minusConsent = true;
										$scope.minusChecklist = true;
										$scope.minusStaff = true;
										$scope.minusEquipment = true;
										$scope.minusServices = true;
										$scope.minusPatientEducation = true;
										$scope.minusMedication = true;
										$scope.minusImplant = true;
										$scope.procedurePlusAndMinus= function(action,id){
											if(id == "ProcedureDetails"){
												if(action == true){
													$scope.plusProcedureDetails = true;
													$scope.minusProcedureDetails = false;
												}else{
													$scope.plusProcedureDetails = false;
													$scope.minusProcedureDetails = true;
												}
											}
											
											if(id == "Consent"){
												if(action == true){
													$scope.plusConsent = true;
													$scope.minusConsent = false;
												}else{
													$scope.plusConsent = false;
													$scope.minusConsent = true;
												}
											}
											
											if(id == "Checklist"){
												if(action == true){
													$scope.plusChecklist = true;
													$scope.minusChecklist = false;
												}else{
													$scope.plusChecklist = false;
													$scope.minusChecklist = true;
												}
											}
										
											if(id == "Staff"){
												if(action == true){
													$scope.plusStaff = true;
													$scope.minusStaff = false;
												}else{
													$scope.plusStaff = false;
													$scope.minusStaff = true;
												}
											}
											
											if(id == "Equipment"){
												if(action == true){
													$scope.plusEquipment = true;
													$scope.minusEquipment = false;
												}else{
													$scope.plusEquipment = false;
													$scope.minusEquipment = true;
												}
											}
											
											if(id == "Services"){
												if(action == true){
													$scope.plusServices = true;
													$scope.minusServices = false;
												}else{
													$scope.plusServices = false;
													$scope.minusServices = true;
												}
											}
											
											if(id == "PatientEducation"){
												if(action == true){
													$scope.plusPatientEducation = true;
													$scope.minusPatientEducation = false;
												}else{
													$scope.plusPatientEducation = false;
													$scope.minusPatientEducation = true;
												}
											}

											if(id == "Medication"){
												if(action == true){
													$scope.plusMedication = true;
													$scope.minusMedication = false;
												}else{
													$scope.plusMedication = false;
													$scope.minusMedication = true;
												}
											}
											
											if(id == "Implant"){
												if(action == true){
													$scope.plusImplant = true;
													$scope.minusImplant = false;
												}else{
													$scope.plusImplant = false;
													$scope.minusImplant = true;
												}
											}
										
										}
										
								     
										
				    /**===================================PROCEDURE ORDER TAB END ===============================================*/
										
										// ----------------------------Procedure order
										// cancellation START
										// --------------------------------------------------------------//
										
										$scope.procedureCancellationReasonList = [];
										$scope.getReasonListByReasonTypeId = function(){
											var URI = BASE_URL + ROOT_URL+ "/api/common/getReasonMasterListByReasonTypeId";	
											var data = {
													reasonTypeId : 16,
													organizationId : $scope.organizationId
														}
											GenericService.serviceAction("POST", URI, data).then(
															function(response) {
																$scope.procedureCancellationReasonList = [];
																if (response.data.status == "success") {
																	$scope.procedureCancellationReasonList = response.data.listObject;
																}
															});
										}
										$scope.getReasonListByReasonTypeId();
									
										// ----------------------------Procedure order
										// cancellation END
										// --------------------------------------------------------------//
									
									
										
									/*****************************************Cyclic Order
										// Start*************************************************************************/
										
										$scope.openDatePickerCyclicOrder = function() {
											$scope.datePickerCyclicOrder.opened = true;
										};

										$scope.datePickerCyclicOrder = {
											opened : false
										};
			                            $scope.dateOptionsCyclicOrder = {
			                            		formatYear : 'yyyy',
			    								showWeeks : false,
			    								minDate:new Date(), // max
			    							};
			                            // For Slot selection
			                            $scope.openDatePickerForSlotSelection = function() {
											$scope.datepickerForSlotSelection.opened = true;
										};

										$scope.datepickerForSlotSelection = {
											opened : false
										};
			                           // date select docotor
										$scope.openDatepickerFrom = function() {
											$scope.datepickerFrom.opened = true;
										};
										
										$scope.datepickerFrom = {
												opened : false
											};
			                            // ------------//
			                            $scope.dateOptionService = {
												formatYear : 'yyyy',
												showWeeks : false,
												minDate : new Date()
										};
			                            
										
										$scope.datePickerService = {
												opened : [ false ],

										};
										
										$scope.openDatePickerForService = function(index) {
											for (var i = $scope.serviceSlotListForReschedule.length - 1; i >= 0; i--) {
												if (i === index) {

													$scope.datePickerService.opened[i] = true;
												} else {
													$scope.datePickerService.opened[i] = false;
												}
											}
											
										};
										
										$scope.openDatePickerForServiceSave = function(index) {
											for (var i = $scope.setCreatedSlotListForServiceList.length - 1; i >= 0; i--) {
												if (i === index) {
													$scope.datePickerService.opened[i] = true;
												} else {
													$scope.datePickerService.opened[i] = false;
												}
											}
											angular.forEach($scope.setCreatedSlotListForServiceList,function(value){
												value.dateOptionservice = {
													formatYear : 'yyyy',
													showWeeks : false,
													minDate : new Date()
											};
											});
										};
										
			                            $scope.cyclic = {
												"patientId" : 0,
												"specialityId" :$scope.encounterDetailsObj.specialityId,
												"doctorId" : $scope.encounterDetailsObj.doctorId,
												"serviceId" : 0,
												"serviceName" : "",
												"datepickerFrom" : new Date(),
												"datepickerFromS" : new Date(),
												"duration" : 0,
												"durationS" : 0,
												"durationType" : "",
												"durationTypeS" : "",
												"sessions" : 0,
												"sessionsS" : 0,
												"morning" : "N",
												"morningS" : "N",
												"evening" : "N",
												"eveningS" : "N",
												"occurences" : 0,
												"occurencesS" : 0,
												"every" : "",
												"everyS" : "",
												"occurencesType" : "",
												"occurencesTypeS" : "",
												"overBooking" : "N",
												"overBookingS" : "N",
												 "serviceAmount": 0,
												 "serviceMasterId": 0,
												 "subSpecialityId":0,
												 "specialityId":0,
												 "taxId": 0,
												 "taxPercentage": 0,
												 "packageId" :0,
												 "packageConsumptionMasterId":0,
												 "packageConsumptionServiceDetailId":0,
												 "packageEoDetailId":0,
												 "billId":0,
												 "dayM1":'N',
												 "dayM2":'N',
												 "dayM3":'N',
												 "dayM4":'N',
												 "dayM5":'N',
												 "dayM6":'N',
												 "dayM7":'N',
												 "dayE1":'N',
												 "dayE2":'N',
												 "dayE3":'N',
												 "dayE4":'N',
												 "dayE5":'N',
												 "dayE6":'N',
												 "dayE7":'N',

												 "daySM1":'N',
												 "daySM2":'N',
												 "daySM3":'N',
												 "daySM4":'N',
												 "daySM5":'N',
												 "daySM6":'N',
												 "daySM7":'N',
												 "daySE1":'N',
												 "daySE2":'N',
												 "daySE3":'N',
												 "daySE4":'N',
												 "daySE5":'N',
												 "daySE6":'N',
												 "daySE7":'N',
												 "encounterId":"",
												 "packageDuration":"",
												 "packageValidityToDate":""
										}
			                            $scope.appointmentSearchObject = {
									    		 'doctorId':0,
									    		 'specialityId':0,
									    		 'organizationId':$scope.organizationId,
									    		 'unitId':$scope.unitId,
									    		 'patientId':0,
									    		 'specialityId':0,
									    		 'subSpecialityId':0,
									    		 'modalityId':0,
									    		 'visitTypeId':0
									     }
			                            
			                            $scope.checkMaxNumberOfSession = function(sessions){
			                            	if(sessions > 365){
			                            		  growl.warning(" You entered number of session greater than 365  ",
															{
																title : 'warning!'
															});
			                            		  $scope.cyclic.sessions = 0;
			                            		  $scope.cyclic.sessionsS = 0;
			                            	}
			                            }
			                             
										$scope.getDeptIdForServiceList=function(specialityObj){
											var object=JSON.parse(specialityObj);
											$scope.specialityIdC=object.specialityId;
										}
										
										$scope.getCyclicServicesList = function(specialityObj) {
											var object=JSON.parse(specialityObj);
											$scope.specialityIdC=object.specialityId;
											var data={
													specialityId:$scope.specialityIdC,
													organizationId:$scope.organizationId,
													unitId:$scope.unitId
											};
											var URI = BASE_URL + ROOT_URL
													+ "/api/opd/getDeptWiseServiceListForCyclicOrder";
											BillingGenericService
													.serviceAction(METHOD_POST, URI,
															data ,
															NOTIFICATION_MSG_STATUS_FALSE)
													.then(
															function(response) {
																if (response.data.status == "success")
																	$scope.servicesListForCyclic = response.data.listObject;
															});
										}
										
										
										$scope.getServiceDetails=function(ser){
											
										}
										
										// save cyclic appointment
			                            
										$scope.saveCyclicAppointments = function(){
											var cnt = 0;
											$scope.finalAppointmentSlotList=[];
											for(let i=0;i<$scope.setCreatedSlotList.length;i++){
												$scope.setCreatedSlotList[i].appointmentTypeId = 1; // doctorConsultaion
												$scope.setCreatedSlotList[i].generateToken = 'N';
												$scope.setCreatedSlotList[i].organizationId = $scope.organizationId;
												$scope.setCreatedSlotList[i].unitId = $scope.unitId;
												$scope.setCreatedSlotList[i].createdBy = $scope.userId;
												$scope.setCreatedSlotList[i].updatedBy = $scope.userId;
												$scope.setCreatedSlotList[i].status = 'A';
												$scope.setCreatedSlotList[i].slotStatusId = $scope.setCreatedSlotList[i].slotStatusId==1?4:$scope.setCreatedSlotList[i].slotStatusId;
												$scope.setCreatedSlotList[i].patientId = $scope.patientDetails.patientId;
												$scope.setCreatedSlotList[i].isFromCoversheet = 'N'; 
												$scope.setCreatedSlotList[i].appointmentDate =  $scope.setCreatedSlotList[i].appointmentDate2;
												$scope.setCreatedSlotList[i].appointmentDate2 =  null;
												$scope.setCreatedSlotList[i].isCyclicAppointment ='Y';
												$scope.finalAppointmentSlotList.push($scope.setCreatedSlotList[i]);
											}
											$rootScope.startLoader();
											var URI = BASE_URL + ROOT_URL + "/api/opd/cyclicAppointmentScheduling";
											// console.log(JSON.stringify($scope.setCreatedSlotList[i]));
											BillingGenericService.serviceAction(METHOD_POST, URI,$scope.finalAppointmentSlotList,NOTIFICATION_MSG_STATUS_FALSE)
											.then(function(response) {
												console.log(response);
												if (response.data.status == "success") {
														growl.success("Appointments Added Successfully",
																{
																	title : 'Success!'
																});
												  } 
												
												$scope.getPatientDetailsById();
												$rootScope.stopLoader();
												$scope.cyclic = {
														"patientId" : 0,
														"specialityId" : $scope.encounterDetailsObj.specialityId,
														"doctorId" : $scope.encounterDetailsObj.doctorId,
														"serviceId" : 0,
														"serviceName" : "",
														"datepickerFrom" : new Date(),
														"datepickerFromS" : new Date(),
														"duration" : 0,
														"durationS" : 0,
														"durationType" : "",
														"durationTypeS" : "",
														"sessions" : 0,
														"sessionsS" : 0,
														"morning" : "N",
														"morningS" : "N",
														"evening" : "N",
														"eveningS" : "N",
														"occurences" : 0,
														"occurencesS" : 0,
														"every" : "",
														"everyS" : "",
														"occurencesType" : "",
														"occurencesTypeS" : "",
														"overBooking" : "N",
														"overBookingS" : "N",
														 "serviceAmount": 0,
														 "serviceMasterId": 0,
														 "serviceStandardName": "",
														 "subSpecialityId":0,
														 "specialityId":0,
														 "taxId": 0,
														 "taxPercentage": 0,
														 "packageId" :0,
														 "packageConsumptionMasterId":0,
														 "packageConsumptionServiceDetailId":0,
														 "packageEoDetailId":0,
														 "billId":0,
														 "dayM1":'N',
														 "dayM2":'N',
														 "dayM3":'N',
														 "dayM4":'N',
														 "dayM5":'N',
														 "dayM6":'N',
														 "dayM7":'N',
														 "dayE1":'N',
														 "dayE2":'N',
														 "dayE3":'N',
														 "dayE4":'N',
														 "dayE5":'N',
														 "dayE6":'N',
														 "dayE7":'N',

														 "daySM1":'N',
														 "daySM2":'N',
														 "daySM3":'N',
														 "daySM4":'N',
														 "daySM5":'N',
														 "daySM6":'N',
														 "daySM7":'N',
														 "daySE1":'N',
														 "daySE2":'N',
														 "daySE3":'N',
														 "daySE4":'N',
														 "daySE5":'N',
														 "daySE6":'N',
														 "daySE7":'N',
														 "encounterId":"",
														 "packageDuration":"",
														 "packageValidityToDate":""
												}
//$scope.specialityObj={};
//$scope.init();
												});
										}
										
										// save service appointment
										$scope.saveCyclicServiceAppointments=function(){
											$scope.finalAppointmentSlotList=[];
											for(let i=0;i<$scope.setCreatedSlotListForServiceList.length;i++){
												var appointmentObj={};
												$scope.setCreatedSlotListForServiceList[i].appointmentTime = $scope.setCreatedSlotListForServiceList[i].appointmentTime; 
												$scope.setCreatedSlotListForServiceList[i].appointmentTypeId = 8; 
												$scope.setCreatedSlotListForServiceList[i].generateToken = 'N';
												$scope.setCreatedSlotListForServiceList[i].organizationId = $scope.organizationId;
												$scope.setCreatedSlotListForServiceList[i].unitId = $scope.unitId;
												$scope.setCreatedSlotListForServiceList[i].createdBy = $scope.userId;
												$scope.setCreatedSlotListForServiceList[i].updatedBy = $scope.userId;
												$scope.setCreatedSlotListForServiceList[i].status = 'A';
												$scope.setCreatedSlotListForServiceList[i].patientId = $scope.encounterDetailsObj.patientId;
												$scope.setCreatedSlotListForServiceList[i].encounterId = $scope.encounterDetailsObj.encounterId;
												$scope.setCreatedSlotListForServiceList[i].doctorId = $scope.encounterDetailsObj.doctorId;
												$scope.setCreatedSlotListForServiceList[i].visitTypeId = $scope.encounterDetailsObj.visitTypeId;
												$scope.setCreatedSlotListForServiceList[i].isFromCoversheet = 'Y';
												$scope.setCreatedSlotListForServiceList[i].appointmentStatusId=1;
												$scope.setCreatedSlotListForServiceList[i].serviceAmount=$scope.serviceAppObj.serviceAmount;
												$scope.setCreatedSlotListForServiceList[i].serviceMasterId=$scope.serviceAppObj.serviceMasterId;
												$scope.setCreatedSlotListForServiceList[i].subSpecialityId=$scope.serviceAppObj.subSpecialityId;
												$scope.setCreatedSlotListForServiceList[i].specialityId= $scope.serviceAppObj.specialityId;
												$scope.setCreatedSlotListForServiceList[i].serviceStandardName= $scope.serviceAppObj.serviceStandardName;
												$scope.setCreatedSlotListForServiceList[i].defaultSelfTariffId=$scope.defaultSelfTariffId;
												$scope.setCreatedSlotListForServiceList[i].taxId=$scope.serviceAppObj.taxId;
												$scope.setCreatedSlotListForServiceList[i].taxPercentage=$scope.serviceAppObj.taxPercentage;
												$scope.setCreatedSlotListForServiceList[i].appointmentDate2 =null;
												$scope.setCreatedSlotListForServiceList[i].appointmentDate =  moment($scope.setCreatedSlotListForServiceList[i].appointmentDate).format('YYYY-MM-DD');
												$scope.setCreatedSlotListForServiceList[i].cyclicOrdScheduledDate =  moment($scope.setCreatedSlotListForServiceList[i].appointmentDate).format('DD-MM-YYYY');
												$scope.setCreatedSlotListForServiceList[i].orderDetailsPackageMapperDto={};
												$scope.setCreatedSlotListForServiceList[i].orderDetailsPackageMapperDto.payeeId=$scope.selfPayeeId;
												
												appointmentObj=angular.copy($scope.setCreatedSlotListForServiceList[i]);
												delete appointmentObj.dateOptionservice;
												$scope.finalAppointmentSlotList.push(appointmentObj);
											}
											if($scope.cyclic.datepickerFromS == "" || $scope.cyclic.datepickerFromS == undefined || $scope.cyclic.datepickerFromS == null){
												  growl.warning(
				                                            "Please select Start Date.",
				                                            {
				                                                title : 'Warning!'
				                                            });
												  $scope.cyclic.datepickerFromS = new Date();
												  return false;
											}
											// return false;
											$rootScope.startLoader();
											// var URI = BASE_URL + ROOT_URL +
											// "/api/opd/generateCyclicServiceAppointment";
											var URI = BASE_URL + ROOT_URL + "/api/billing/generateCyclicServiceAppointment";
											// console.log(JSON.stringify($scope.setCreatedSlotList[i]));
											BillingGenericService.serviceAction(METHOD_POST, URI,$scope.finalAppointmentSlotList,NOTIFICATION_MSG_STATUS_FALSE)
											.then(function(response) {
												console.log(response);
												if (response.data.status == "success") {
														growl.success("Appointments Added Successfully",
																{
																	title : 'Success!'
																});
												  } 
												$scope.getServiceAppointmentByPatientId();
												// $scope.init();
												$scope.cyclic = {
														"patientId" : 0,
														"specialityId" : $scope.encounterDetailsObj.specialityId,
														"doctorId" : $scope.encounterDetailsObj.doctorId,
														"serviceId" : 0,
														"serviceName" : "",
														"datepickerFrom" : new Date(),
														"datepickerFromS" : new Date(),
														"duration" : 0,
														"durationS" : 0,
														"durationType" : "",
														"durationTypeS" : "",
														"sessions" : 0,
														"sessionsS" : 0,
														"morning" : "N",
														"morningS" : "N",
														"evening" : "N",
														"eveningS" : "N",
														"occurences" : 0,
														"occurencesS" : 0,
														"every" : "",
														"everyS" : "",
														"occurencesType" : "",
														"occurencesTypeS" : "",
														"overBooking" : "N",
														"overBookingS" : "N",
														 "serviceAmount": 0,
														 "serviceMasterId": 0,
														 "subSpecialityId":0,
														 "specialityId":0,
														 "taxId": 0,
														 "taxPercentage": 0,
														 "packageId" :0,
														 "packageConsumptionMasterId":0,
														 "packageConsumptionServiceDetailId":0,
														 "packageEoDetailId":0,
														 "billId":0,
														 "dayM1":'N',
														 "dayM2":'N',
														 "dayM3":'N',
														 "dayM4":'N',
														 "dayM5":'N',
														 "dayM6":'N',
														 "dayM7":'N',
														 "dayE1":'N',
														 "dayE2":'N',
														 "dayE3":'N',
														 "dayE4":'N',
														 "dayE5":'N',
														 "dayE6":'N',
														 "dayE7":'N',

														 "daySM1":'N',
														 "daySM2":'N',
														 "daySM3":'N',
														 "daySM4":'N',
														 "daySM5":'N',
														 "daySM6":'N',
														 "daySM7":'N',
														 "daySE1":'N',
														 "daySE2":'N',
														 "daySE3":'N',
														 "daySE4":'N',
														 "daySE5":'N',
														 "daySE6":'N',
														 "daySE7":'N',
														 "encounterId":"",
														 "packageDuration":"",
														 "packageValidityToDate":""
												}
												$scope.specialityObj={};
												$scope.deptWiseAppointment='N';
												$rootScope.stopLoader();
												});
										}
										
										// reschedule doctor appointment
										$scope.rescheduleCyclicAppointments=function(){

											$scope.rescheduleAppointmentSlotList=[];
											for(let i=0;i<$scope.setCreatedSlotList.length;i++){
												 if($scope.patientAppointmentSlotList[i].slotId != $scope.setCreatedSlotList[i].slotId){ 
												$scope.setCreatedSlotList[$scope.idx].reSchedule = 'Y';
												$scope.setCreatedSlotList[i].appointmentTypeId = 1; // doctorConsultaion
												$scope.setCreatedSlotList[i].generateToken = 'N';
												$scope.setCreatedSlotList[i].organizationId = $scope.organizationId;
												$scope.setCreatedSlotList[i].unitId = $scope.unitId;
												$scope.setCreatedSlotList[i].createdBy = $scope.userId;
												$scope.setCreatedSlotList[i].updatedBy = $scope.userId;
												$scope.setCreatedSlotList[i].status = 'A';
												$scope.setCreatedSlotList[i].slotStatusId=$scope.setCreatedSlotList[i].slotStatusId==1?4:$scope.setCreatedSlotList[i].slotStatusId;
												// $scope.setCreatedSlotList[i].slotStatusId
												// = 4;
												$scope.setCreatedSlotList[i].slotTypeId = 1;
												$scope.setCreatedSlotList[i].patientId = $scope.patientDetails.patientId;
												$scope.setCreatedSlotList[i].isFromCoversheet = 'N'; 
												$scope.setCreatedSlotList[i].appointmentDate2 = moment($scope.datepickerForSlotSelection).format('DD/MM/YYYY');
												$scope.setCreatedSlotList[i].appointmentDate = null;

												$scope.setCreatedSlotList[i].rescheduleReasonId = 1;
												$scope.setCreatedSlotList[i].rescheduledBy = 'C';
												$scope.setCreatedSlotList[i].isCyclicAppointment = 'Y';
												$scope.setCreatedSlotList[i].previousSlotId = $scope.patientAppointmentSlotList[i].slotId;
												$scope.setCreatedSlotList[i].doctorId = $scope.doctorId;
												$scope.setCreatedSlotList[i].specialityId = $scope.specialityId;
												$scope.setCreatedSlotList[i].appointmentId = $scope.patientAppointmentSlotList[i].appointmentId;
												$scope.rescheduleAppointmentSlotList.push($scope.setCreatedSlotList[i]);
											 }
											}
											if($scope.rescheduleAppointmentSlotList.length==0){
												 growl.warning(
															'No change in existing appointment',
															{
																title : 'Warning!'
															});
												return false;
											}
											$rootScope.startLoader();
											var URI = BASE_URL + ROOT_URL +  "/api/opd/rescheduleCyclicAppointment";
											// console.log(JSON.stringify($scope.setCreatedSlotList[i]));
											BillingGenericService.serviceAction(METHOD_POST, URI,$scope.rescheduleAppointmentSlotList,NOTIFICATION_MSG_STATUS_FALSE)
											.then(function(response) {
												console.log(response);
												if (response.data.status == "success") {
														growl.success("Appointments Updated Successfully",
																{
																	title : 'Success!'
																});
												} 
												$rootScope.stopLoader();
												});
											
										
										}
										// get patient doctor appointment
										$scope.getPatientDetailsById = function(){
											$scope.showAppNoRecFound='N';
											$scope.patientDetailsList = [];
											$scope.patientDetails1 = {};
											   var URI = BASE_URL + ROOT_URL + "/api/opd/getCyclicDocWiseAndDeptWiseAppointmentListByPatientId/"+$scope.encounterDetailsObj.patientId+"/"+$scope.organizationId;
				                                $scope.patientDetails.patientId=$scope.encounterDetailsObj.patientId;
											 BillingGenericService
												.serviceAction(METHOD_GET, URI, null,
														NOTIFICATION_MSG_STATUS_FALSE)
												.then(
														function(response) {
															$scope.serviceAppointmentList=[];
															$scope.appointmentList = [];
															if (response.data.status == "success"){
																angular.forEach(response.data.listObject,function(value,index){
																	if(value.doctorId!=null || value.specialityId!=null){
																		$scope.appointmentList.push(value);
																	}
																});
																if($scope.appointmentList.length==0){
																	$scope.showAppNoRecFound='Y';
																}
																// $scope.appointmentList
																// =
																// response.data.listObject;
																$scope.patientDetails1 = response.data.listObject[0];
															}
														});
										      }
										$scope.getPatientDetailsById();
										
										$scope.setOccurenceType = function(val){
											$scope.cyclic.occurencesType = val;
										}
										
										$scope.setOccurenceTypeS = function(val){
											$scope.cyclic.occurencesTypeS = val;
										}
										
										$scope.checkDateTime = function(slobObj)
										{
										  if($scope.currentDateCyclic==moment($scope.datepickerForSlotSelection).format('YYYY-MM-DD')){
											  if(slobObj.originalFromTime>$scope.currentTime)
												 return true;
											  else
												  return false;
										  }else{
											 return true;
										  }
											
										}
										
										$scope.getSlotsOfDate = function(obj){
										$scope.setCreatedTimeSlotList = [];
										var specId = obj.specialityId;
										
										if($scope.cyclic.morning === "Y" && $scope.cyclic.morning === "Y"){
											$scope.fromTime = $scope.docMorningStartTime;
											$scope.toTime = $scope.docEveningEndTime;
										}else if($scope.cyclic.morning === "Y"){
											$scope.fromTime = $scope.docMorningStartTime;
											$scope.toTime = $scope.docMorningEndTime;
										}else if($scope.cyclic.evening === "Y"){
											$scope.fromTime = $scope.docEveningStartTime;
											$scope.toTime = $scope.docEveningEndTime;
										}else{
											$scope.fromTime = $scope.docMorningStartTime;
											$scope.toTime = $scope.docEveningEndTime;
										}
										var data = {
												specialityId : obj.specialityId,
												doctorId :(obj.doctorId!=undefined && obj.doctorId>0)?obj.doctorId:0,
												appointmentDate :moment($scope.datepickerForSlotSelection).format('YYYY-MM-DD'),
												organizationId : $scope.organizationId,
												unitId : $scope.unitId,
												slotTypeId : 10,
												fromTime : $scope.fromTime,
												toTime : $scope.toTime,
												deptWiseAppointment : $scope.deptWiseAppointment
											};
									
										console.log(data);
										$rootScope.startLoader();
										var URI = BASE_URL + ROOT_URL + GETDAILYSLOTS;
										BillingGenericService
												.serviceAction(METHOD_POST, URI, data,
														NOTIFICATION_MSG_STATUS_FALSE)
												.then(function(response) {
													console.log(response.data.listObject);
													if (response.data.status == "success") {
																$scope.setCreatedTimeSlotList = response.data.listObject;
																$rootScope.stopLoader();
																if($scope.setCreatedTimeSlotList.length==0){
																	$scope.loadingMsg="Slots are not created for selected date...!";
																}
																// console.log($scope.setCreatedTimeSlotList);
															}
															$rootScope.stopLoader();

														});
									}
										$scope.getAppointmentDetailsById = function(info){
											$scope.setCreatedSlotList = [];
											$scope.patientAppointmentSlotList=[];
											$scope.appointmentSearchObject.appointmentTypeId = 1 ;
											$scope.appointmentSearchObject.patientId = info.patientId;
											$scope.appointmentSearchObject.doctorId = info.doctorId;
											$scope.appointmentSearchObject.specialityId=info.specialityId;
											// var URI = BASE_URL + ROOT_URL +
											// "/api/global/patient/billingCyclic/"+info.patientId;
											$scope.isAppointmentSchedule='N';
		     								$scope.appointmentSearchObject.isCyclicAppointment='Y';
											var URI = BASE_URL + ROOT_URL + SEARCHAPPOINTMENTSBYPATIENTID; 
											BillingGenericService
												.serviceAction(METHOD_POST, URI, $scope.appointmentSearchObject,
														NOTIFICATION_MSG_STATUS_FALSE)
												.then(
														function(response) {
															console.log(response);
															if (response.data.status == "success")
																$scope.setCreatedSlotList = response.data.listObject;
															    $scope.patientAppointmentSlotList = angular.copy($scope.setCreatedSlotList);
															    // $scope.patientAppointmentSlotList=response.data.listObject;
																$scope.cyclic.specialityName = info.specialityName;
																// $scope.cyclic.docName
																// = info.doctorName;
																
																$scope.doctorId = response.data.listObject[0].doctorId;
																$scope.specialityId = response.data.listObject[0].specialityId;
																$scope.cyclic.specialityId=response.data.listObject[0].specialityId;
																if($scope.doctorId!=null && $scope.doctorId>0){
																	$scope.cyclic.doctorId = response.data.listObject[0].doctorId;
																	$scope.cyclic.docName=response.data.listObject[0].doctorName;
																}else{
																	$scope.cyclic.docName="";
																}
																
																var spcObj = $.grep($scope.specialityList, function (disCat) {
														            return disCat.specialityId == $scope.specialityId;
														        })[0];
																 $scope.deptWiseAppointment=spcObj.deptWiseAppointment;
																 
																// $scope.cyclic.specialityId
																// =
																// response.data.listObject[0].specialityId;
																$scope.cyclic.fromDate = response.data.listObject[0].appointmentDate;
																$scope.cyclic.toDate = response.data.listObject[response.data.listObject.length-1].appointmentDate;
																$scope.cyclic.noOfSessions = response.data.listObject.length;
														});
										}
										
										//time sloat booking
										$scope.getTimeSlotsByDate= function(slots,index){
											$scope.idx = index;
											$scope.setCreatedTimeSlotList = [];
											// $scope.datepicker = new
											// Date(slots.appointmentDate2);
											var array = slots.appointmentDate2.split('/');
											var alignFillDate = "";
											alignFillDate = array[2] + '-' + array[1] + '-'+ array[0];
											$scope.datepickerForSlotSelection = new Date(alignFillDate);
											
											var array = $scope.cyclic.fromDate.split('/');
											var fromDate = "";
											fromDate = array[2] + '-' + array[1] + '-'+ array[0];
											var fromDate = new Date(fromDate);
											
											var array = $scope.cyclic.toDate.split('/');
											var toDate = "";
											toDate = array[2] + '-' + array[1] + '-'+ array[0];
											var toDate = new Date(toDate);
											
											$scope.hdnAppointmentId = slots.appointmentId;
											$scope.previousSlotId = slots.slotId;
											
											$scope.dateOptions1 = {
													formatYear : 'yyyy',
													showWeeks : false,
													minDate : fromDate,
													maxDate : toDate
												};
											for(let k=0;k<$scope.slotDetailsList.length;k++){
												 if(moment(slots.appointmentDate2).format('YYYY-MM-DD') === moment($scope.slotDetailsList[k].appointmentDate2).format('DD-MM-YYYY')){ 
												    	$scope.setCreatedTimeSlotList.push($scope.slotDetailsList[k]);
													}
											}
											$scope.getSlotsOfDate($scope.cyclic);
										}
										$scope.slotBooking = function(item){
											for(let k=0;k<$scope.setCreatedTimeSlotList.length;k++){
												   if(item.slotId === $scope.setCreatedTimeSlotList[k].slotId){ 
													// console.log(item.slotId+"="+$scope.setCreatedTimeSlotList[k]);
													   if($scope.setCreatedTimeSlotList[k].allowedOverBookingSlots>$scope.setCreatedTimeSlotList[k].slotPatientDetailsListsSize
															   && $scope.setCreatedTimeSlotList[k].slotPatientDetailsListsSize>0 && $scope.cyclic.overBooking=='Y'){
														   $scope.setCreatedTimeSlotList[k].slotStatusId=6;
													   }else{
														   // $scope.setCreatedTimeSlotList[k].slotStatusId=4;
													   }
													   
													   var slotExistObj=$.grep($scope.setCreatedSlotList, function (obj) {
												            return (obj.fromTimeString==item.fromTimeString 
																    && obj.appointmentDate==item.appointmentDate);
												        });
													   if(slotExistObj.length>0){
														   growl.warning(
																	'Slot already in use',
																	{
																		title : 'Warning!'
																	});
														return false;  
													   }
													   $('#time_slot_modal').modal('hide');
													   
													   $scope.setCreatedTimeSlotList[k].fromTimeString=$scope.setCreatedTimeSlotList[k].originalFromTime;
													 $scope.setCreatedSlotList[$scope.idx] = $scope.setCreatedTimeSlotList[k];
													 $scope.setCreatedSlotList[$scope.idx].reSchedule = 'Y';
												    	break;
													}
											}
											
										}
										
										$scope.getDoctorBySpeciality = function() {
										$scope.patientDetails.specialityId=$scope.encounterDetailsObj.specialityId;
										$scope.patientDetails.doctorId=$scope.encounterDetailsObj.doctorId;	
											var data = {
												specialityId : $scope.encounterDetailsObj.specialityId,
												unitId : $scope.unitId,
												organizationId : $scope.organizationId
											};
											$scope.startLoader();
											var URI = BASE_URL + ROOT_URL + "/api/global/getDoctorListBySpecialityId";
											BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
													.then(function(response) {
																console.log(response);
																if (response.data.status == "success"){
																	$scope.doctorList = response.data.listObject;
																	if($scope.encounterDetailsObj.doctorName==null && $scope.encounterDetailsObj.doctorId!=null){
																		var docList=$.grep($scope.doctorList, function (obj) {
																	        return $scope.encounterDetailsObj.doctorId == obj.doctorId;
																	    });
																		$scope.patientDetails.dFirstName=docList[0].doctorName; 
																	}
																	var spcObj = $.grep($scope.specialityList, function (disCat) {
															            return disCat.specialityId == $scope.encounterDetailsObj.specialityId;
															        })[0];
																	$scope.deptWiseAppointment=spcObj.deptWiseAppointment;
																}
																$scope.stopLoader();
															});
												}
											$scope.getDoctorBySpeciality();
												
											//**********************
										// ***********************************get
										// pending service against
										// patient************************
									$scope.serviceAppObj={};
									$scope.setServiceAppObj=function(obj){
										var serObject=JSON.parse(obj);
										$scope.serviceAppObj=serObject;
									}
									
										$scope.getPatPendingServices = function(obj){
											if(obj.serviceId==undefined || obj.serviceId==""){
												growl.warning("Please select service",{
													title : 'Warning!'
												});
												return false;
											}
											var serObj=JSON.parse(obj.serviceId);
											$scope.specialityName=serObj.specialityName;
											$scope.serviceStandardName=serObj.serviceStandardName;
											$scope.sessionsS=obj.sessionsS;
											$scope.durationS=obj.durationS;
											$scope.datepickerFromS=moment(obj.datepickerFromS).format("DD/MM/YYYY");

											$scope.slotDetailsList = [];
											$scope.setCreatedSlotListForServiceList = [];
											console.log(JSON.stringify(obj));
											if($scope.patientDetails.patientId == 0 || $scope.patientDetails.patientId == undefined){
												growl.warning("Please select Patient first!",{
													title : 'Warning!'
												});
												return false;
											}
											
											if(obj.sessionsS==0 || obj.sessionsS=="" || obj.sessionsS==undefined){
												growl.warning("Number of session can not be blank!",{
													title : 'Warning!'
												});
												return;
											}
											
											if(obj.occurencesTypeS!=undefined && obj.occurencesS!=undefined && obj.occurencesS>0 && obj.everyS!=undefined && obj.everyS>0){
												if(Number(obj.occurencesS)>Number(obj.sessionsS)){
													growl.warning("Number of occurences can not be greater than total sessions",{
														title : 'Warning!'
													});
													return;
												}
												
												if(obj.durationS!=undefined && obj.durationS!=0 && obj.durationS!=""){
												var recurrTotalDays=obj.everyS*(obj.occurencesTypeS=='d'?1:(obj.occurencesTypeS=='w'?7:30));	
												var durationTotalDays=obj.durationS*(obj.durationTypeS=='d'?1:(obj.durationTypeS=='w'?7:30));
												if(Number(recurrTotalDays)>Number(durationTotalDays)){
													growl.warning("Duration cannot be greater than total occurences",{
														title : 'Warning!'
													});
													return;
												}
											  }
											}
											

											   $scope.currentDate = new Date(obj.datepickerFromS);
											    $scope.startDate = new Date(obj.datepickerFromS);
											    var duraType=obj.durationTypeS == "d" || obj.durationTypeS == "" || obj.durationS==undefined?1:obj.durationTypeS == "w"?7:30;
											       if(obj.durationS!=undefined && obj.durationS!=0 && obj.durationS!=""){
											    		$scope.diffS = obj.durationS*duraType;
														$scope.toDateS = $scope.currentDate.getTime() + ($scope.diffS-1)*24*60*60*1000;
												    }else if(obj.everyS!=undefined && obj.everyS>0 && obj.occurencesS!=undefined && obj.occurencesS>0){
														var occuType=(obj.occurencesTypeS=="d" || obj.occurencesTypeS==""?1:obj.occurencesTypeS=="w"?7:30);
														var duration=Math.round((obj.everyS*occuType*obj.sessionsS)/obj.occurencesS);
														$scope.toDateS = $scope.currentDate.getTime() + duration*24*60*60*1000;
													}else{
														$scope.diffS = (((obj.durationS=="" || obj.durationS==0 || obj.durationS==undefined)?obj.sessionsS:obj.durationS));
														$scope.toDateS = $scope.currentDate.getTime() + ($scope.diffS-1)*24*60*60*1000;
													}
												
												
												if($scope.cyclic.daySM1=='Y' || $scope.cyclic.daySM2=='Y' || $scope.cyclic.daySM3=='Y'
								    				|| $scope.cyclic.daySM4=='Y' || $scope.cyclic.daySM5=='Y' || $scope.cyclic.daySM6=='Y'
								    				|| $scope.cyclic.daySM7=='Y'){
													$scope.cyclic.morningS='Y';
												}
												
												if($scope.cyclic.daySE1=='Y' || $scope.cyclic.daySE2=='Y' || $scope.cyclic.daySE3=='Y'
								    				|| $scope.cyclic.daySE4=='Y' || $scope.cyclic.daySE5=='Y' || $scope.cyclic.daySE6=='Y'
								    				|| $scope.cyclic.daySE7=='Y'){
													$scope.cyclic.eveningS='Y';
												}
												
												
												if($scope.cyclic.morningS === "Y" && $scope.cyclic.eveningS === "Y"){
													$scope.fromTime = $scope.docMorningStartTime;
													$scope.toTime = $scope.docEveningEndTime;
												}
												else if($scope.cyclic.morningS === "Y"){
													$scope.fromTime = $scope.docMorningStartTime;
													$scope.toTime = $scope.docMorningEndTime;
												}else if($scope.cyclic.eveningS === "Y"){
													$scope.fromTime = $scope.docEveningStartTime;
													$scope.toTime = $scope.docEveningEndTime;
												}else{
													$scope.fromTime = $scope.docMorningStartTime;
													$scope.toTime = $scope.docEveningEndTime;
												}
												if(obj.serviceId == "" || obj.serviceId ==undefined){
													growl.warning("Please select Service!",{
														title : 'Warning!'
													});	
													return;
												}
											
												$scope.cyclic.fromDateS = moment(obj.datepickerFromS).format('DD/MM/YYYY');
												$scope.cyclic.toDateS = moment($scope.toDateS).format('DD/MM/YYYY');
												$scope.cyclic.noOfSessionsS = obj.sessionsS;
												// $('#showDoctorModalService').modal();
							// *****************************Normal case appointment
							// generation************
							 if((obj.daySM1=='N'  && obj.daySM2=='N' && obj.daySM3=='N' && obj.daySM4=='N' && obj.daySM5=='N' 
									&& obj.daySM6=='N' && obj.daySM7=='N' && obj.daySE1=='N' && obj.daySE2=='N' &&obj.daySE3=='N' 
									&& obj.daySE4=='N' && obj.daySE5=='N' && obj.daySE6=='N' && obj.daySE7=='N')){							
									$scope.startDate = new Date($scope.currentDate.getTime()+60*60*1000);	
									var addWithEveryRec,noOfOccurences,reqPerRecOcc,totalWeek,totalDuration;
									$scope.addDays;
									var occuType=(obj.occurencesTypeS=="d" || obj.occurencesTypeS==""?1:obj.occurencesTypeS=="w"?7:30);
									var durationType=(obj.durationTypeS=="d" || obj.durationTypeS=="" || obj.durationTypeS==undefined?1:(obj.durationTypeS=="w"?7:30));
									if(obj.everyS!=undefined && obj.everyS>0 && obj.occurencesS!=undefined && obj.occurencesS>0
											&&(obj.durationS==undefined || obj.durationS==0 || obj.durationS=="")){
										  totalDuration=Math.round((obj.everyS*occuType*obj.sessionsS)/obj.occurencesS);
										  addWithEveryRec=obj.everyS*occuType;	
										  noOfOccurences=Number(obj.occurencesS);
										  reqPerRecOcc=noOfOccurences;
										// $scope.addDays=Math.floor(addWithEveryRec/(reqPerRecOcc==0?1:reqPerRecOcc));
										  $scope.addDays=Math.round(addWithEveryRec/(reqPerRecOcc==0?1:reqPerRecOcc));
									}else if(obj.everyS!=undefined && obj.everyS>0 && obj.occurencesS!=undefined && obj.occurencesS>0){
									  totalDuration=Number(obj.durationS)*durationType;	
									  addWithEveryRec=obj.everyS*occuType;	
									  noOfOccurences=Number(obj.occurencesS);
									  // reqPerRecOcc=Math.floor(noOfOccurences/obj.everyS);
									  reqPerRecOcc=noOfOccurences;
									  $scope.addDays=Math.round(addWithEveryRec/(reqPerRecOcc==0?1:reqPerRecOcc));
								
								  }else if(obj.durationS!=undefined && obj.durationS>0){
									  totalDuration=Number(obj.durationS)*durationType;
									   noOfOccurences=Number(obj.sessionsS);
									   if(obj.durationTypeS=="d" || obj.durationTypeS==""){
										   if(totalDuration>noOfOccurences)
									          addWithEveryRec=Math.round(totalDuration/noOfOccurences);
										   else
											   addWithEveryRec=1; 
									   }else{
										   addWithEveryRec=Math.round(totalDuration/noOfOccurences);
									   }
									   reqPerRecOcc=Math.floor(noOfOccurences/totalDuration);
									   $scope.addDays=addWithEveryRec;
								 }else{
									   totalDuration=Number(obj.sessionsS);
									   addWithEveryRec=1;
									   noOfOccurences=Number(obj.sessionsS);
									   reqPerRecOcc=1;
									   $scope.addDays=1;
								}
									
								 $scope.lastDate=new Date($scope.startDate.getTime()+(totalDuration-1)*24*60*60*1000);
								 
								if($scope.addDays==0){
									$scope.addDays=1;
								}
								if(reqPerRecOcc==0){
									reqPerRecOcc=1;
								}
								
								$scope.firstRecDate=$scope.startDate;
								 var backOneDate = new Date($scope.startDate.getTime()-1*24*60*60*1000);
								// for recurrence criteria
								$scope.lastRecDate = new Date(backOneDate.getTime() + addWithEveryRec*24*60*60*1000);
								$scope.currPerReqTakenCount=0;
								$scope.currentIterationDate=backOneDate;				  
								
							    $scope.tempDate=angular.copy($scope.currentIterationDate);
								var i,t,j,a,b,flagForLastDateApp=false,counterForApp=0,remainingOcc=0;
							 for(i=0;i<obj.sessionsS; i++){
								 
									 if(i==0){
										 $scope.currentIterationDate = new Date($scope.currentIterationDate.getTime() +1*24*60*60*1000);
									 }
									
									 if($scope.setCreatedSlotListForServiceList.length==obj.sessionsS){
										 break;
									 }
									 
								    // service appointment
								  if((moment($scope.currentIterationDate).format('YYYY-MM-DD')<=moment($scope.lastRecDate).format('YYYY-MM-DD'))){

									  var setCreatedSlotListForService = {
												'appointmentDate':'',
												'appointmentTime':moment($scope.currentDateCyclic).format('YYYY-MM-DD')==moment($scope.currentIterationDate).format('YYYY-MM-DD')?$scope.currentTime:$scope.fromTime
										};
									  
									    setCreatedSlotListForService.appointmentDate = moment($scope.currentIterationDate,'DD/MM/YYYY').toDate();
								    	$scope.setCreatedSlotListForServiceList.push(setCreatedSlotListForService); 
								    	$scope.currPerReqTakenCount=$scope.currPerReqTakenCount+1;
								  }
							       
								  if(remainingOcc>0 && flagForLastDateApp==true){
										var lastDateAppCurrReccObj=$scope.setCreatedSlotListForServiceList[$scope.setCreatedSlotListForServiceList.length-1];
										for(b=1;b<=remainingOcc;b++){
											if($scope.setCreatedSlotListForServiceList.length==obj.sessionsS){
												break;
											}
											if($scope.currPerReqTakenCount==reqPerRecOcc){
												break;
											}
									    	$scope.setCreatedSlotListForServiceList.push(lastDateAppCurrReccObj); 
									    	$scope.currPerReqTakenCount=$scope.currPerReqTakenCount+1;
										}
									}
								  
								  flagForLastDateApp=false,remainingOcc=0;
									// next recurence
									if(reqPerRecOcc==$scope.currPerReqTakenCount){
										var backDate=new Date($scope.lastRecDate.getTime());
										if(moment($scope.currentIterationDate).format('YYYY-MM-DD')<=moment($scope.lastRecDate).format('YYYY-MM-DD')){
											$scope.currentIterationDate=new Date(backDate.getTime()+1*24*60*60*1000);
											$scope.tempDate = angular.copy($scope.currentIterationDate);
										}
										 $scope.firstRecDate=new Date($scope.lastRecDate.getTime()+1*24*60*60*1000);
										 $scope.lastRecDate=new Date($scope.firstRecDate.getTime() + (addWithEveryRec-1)*24*60*60*1000);
										 $scope.currPerReqTakenCount=0;
										   if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastDate).format('YYYY-MM-DD')){
												break;
											}
									}else{
									   $scope.currentIterationDate=new Date($scope.currentIterationDate.getTime() + ($scope.addDays)*24*60*60*1000);
									   $scope.tempDate = angular.copy($scope.currentIterationDate);
									   if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')){
										   if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')
													&& $scope.currPerReqTakenCount>0){
												 remainingOcc=reqPerRecOcc-$scope.currPerReqTakenCount;
												flagForLastDateApp=true;
											}else{
											    var backDate=new Date($scope.lastRecDate.getTime());
												$scope.currentIterationDate=new Date(backDate.getTime()+1*24*60*60*1000);
												$scope.tempDate = angular.copy($scope.currentIterationDate);
								    			 $scope.firstRecDate=new Date($scope.lastRecDate.getTime()+1*24*60*60*1000);
								    			 $scope.lastRecDate=new Date($scope.firstRecDate.getTime() + (addWithEveryRec-1)*24*60*60*1000);
								    			 $scope.currPerReqTakenCount=0;
											}
										}
									   if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastDate).format('YYYY-MM-DD')){
											break;
										}
									}
									
							// if no slots added then keep counter as previous
									 i=$scope.setCreatedSlotListForServiceList.length==0?0:($scope.setCreatedSlotListForServiceList.length-1);
							}
									// ************Place all appointment on last
									// date*******

									        if(obj.sessionsS!=$scope.setCreatedSlotListForServiceList.length){
									        	 if($scope.setCreatedSlotListForServiceList.length==0){
													 growl.warning(
																'Provided conditions are incorrect',
																{
																	title : 'Warning!'
																});
													return false; 
												 }
												var lastDateAppObj=$scope.setCreatedSlotListForServiceList[$scope.setCreatedSlotListForServiceList.length-1];
												for(var K=0;K<obj.sessionsS;K++){
														if($scope.setCreatedSlotListForServiceList.length==obj.sessionsS){
															break;
														}
												    	$scope.setCreatedSlotListForServiceList.push(lastDateAppObj); 
													}
												 }
										        $('#showDoctorModalService').modal();   
							 	}else{

									// **********************Logic for preferred day
									// selection*************
							 		  $scope.startDate = new Date($scope.currentDate.getTime()+60*60*1000);	
						                $scope.endDate=angular.copy($scope.startDate);
											var addWithEveryRec,noOfOccurences,reqPerRecOcc,totalWeek,totalDuration;
											$scope.addDays;
											var occuType=(obj.occurencesTypeS=="d" || obj.occurencesTypeS==""?1:obj.occurencesTypeS=="w"?7:30);
											var durationType=(obj.durationTypeS=="d" || obj.occurencesTypeS==""?1:(obj.durationTypeS=="w"?7:30));	
											if(obj.everyS!=undefined && obj.everyS>0 && obj.occurencesS!=undefined && obj.occurencesS>0
														&&(obj.durationS==undefined || obj.durationS==0 || obj.durationS=="")){
													 totalDuration=Math.round((obj.everyS*occuType*obj.sessionsS)/obj.occurencesS);
													  addWithEveryRec=obj.everyS*occuType;	
													  noOfOccurences=Number(obj.occurencesS);
													  reqPerRecOcc=noOfOccurences;
													  $scope.addDays=Math.round(addWithEveryRec/(reqPerRecOcc==0?1:reqPerRecOcc));
											}else if(obj.everyS!=undefined && obj.everyS>0 && obj.occurencesS!=undefined && obj.occurencesS>0){
											  totalDuration=Number(obj.durationS)*durationType;	
											  addWithEveryRec=obj.everyS*occuType;	
											  noOfOccurences=Number(obj.occurencesS);
											  reqPerRecOcc=noOfOccurences;
											  $scope.addDays=Math.floor(addWithEveryRec/(reqPerRecOcc==0?1:reqPerRecOcc));
											}else if(obj.durationS!=undefined && obj.durationS>0){
											       totalDuration=Number(obj.durationS)*durationType;
											       noOfOccurences=Number(obj.sessionsS);
												   addWithEveryRec=7; 
												   var totalWeek=Math.ceil(totalDuration/7);
												   reqPerRecOcc=Math.floor(noOfOccurences/totalWeek);
												   $scope.addDays=Math.floor(totalDuration/noOfOccurences);
											}else{
												   totalDuration=Number(obj.sessionsS);
												   addWithEveryRec=1;
												   noOfOccurences=Number(obj.sessionsS);
												   reqPerRecOcc=1;
												   $scope.addDays=1
											}
											 $scope.lastDate=new Date($scope.endDate.getTime()+(totalDuration-1)*24*60*60*1000);
											 
											if($scope.addDays==0){
												$scope.addDays=1;
											}
											if(reqPerRecOcc==0){
												reqPerRecOcc=1;
											}
											
											// $scope.everyWeekFirstDateCounter=angular.copy($scope.endDate);
											$scope.firstRecDate=$scope.endDate;
											 var backOneDate = new Date($scope.endDate.getTime()-1*24*60*60*1000);
											// for recurrence criteria
											$scope.lastRecDate = new Date(backOneDate.getTime() + addWithEveryRec*24*60*60*1000);
											$scope.currPerReqTakenCount=0;
											// first week last date
											// $scope.lastDateofCurrWeek=new
											// Date(backOneDate.getTime() +
											// 7*24*60*60*1000)
											$scope.currentIterationDate=backOneDate;				  
									
											$scope.tempDate=angular.copy(new Date($scope.currentIterationDate.getTime() +1*24*60*60*1000));
									        var flagStop=0,i=0,t,j=0,a,b,flagForLastDateApp=false,counterForApp=0,remainingOcc=0;		
								
						    for(i=$scope.setCreatedSlotListForServiceList.length;i<=obj.sessionsS; i++){
								    	
												 if(i==0){
													 $scope.currentIterationDate = new Date($scope.currentIterationDate.getTime() +1*24*60*60*1000);
													 $scope.tempDate = angular.copy($scope.currentIterationDate);
												 }

													var flagObj=false;
													if(($scope.cyclic.daySM1 == "Y" && $scope.currentIterationDate.getDay()==1) || ($scope.cyclic.daySM2 == "Y" && $scope.currentIterationDate.getDay()==2) || ($scope.cyclic.daySM3 == "Y" && $scope.currentIterationDate.getDay()==3)
									    					|| ($scope.cyclic.daySM4 == "Y" && $scope.currentIterationDate.getDay()==4) || ($scope.cyclic.daySM5 == "Y" && $scope.currentIterationDate.getDay()==5) 
									    					|| ($scope.cyclic.daySM6 == "Y" && $scope.currentIterationDate.getDay()==6) || ($scope.cyclic.daySM7 == "Y" && $scope.currentIterationDate.getDay()==0) 
									    					||($scope.cyclic.daySE1 == "Y" && $scope.currentIterationDate.getDay()==1) || ($scope.cyclic.daySE2 == "Y" && $scope.currentIterationDate.getDay()==2) || ($scope.cyclic.daySE3 == "Y" && $scope.currentIterationDate.getDay()==3)
									    					|| ($scope.cyclic.daySE4 == "Y" && $scope.currentIterationDate.getDay()==4) || ($scope.cyclic.daySE5 == "Y" && $scope.currentIterationDate.getDay()==5) 
									    					|| ($scope.cyclic.daySE6 == "Y" && $scope.currentIterationDate.getDay()==6) || ($scope.cyclic.daySE7 == "Y" && $scope.currentIterationDate.getDay()==0)){
													      flagObj=true;
									    			}
													
											// main loop break if condition meets
											if(obj.sessionsS==$scope.setCreatedSlotListForServiceList.length){
												break;
											}	
											
											if(flagObj==true){
												if((moment($scope.currentIterationDate).format('YYYY-MM-DD')<=moment($scope.lastRecDate).format('YYYY-MM-DD'))){
													// for morning selection preference
													var setCreatedSlotListForService = {
															'appointmentDate':'',
															'appointmentTime':moment($scope.currentDateCyclic).format('YYYY-MM-DD')==moment($scope.currentIterationDate).format('YYYY-MM-DD')?$scope.currentTime:$scope.docMorningStartTime
													
													};
													
													if($scope.setCreatedSlotListForServiceList.length==obj.sessionsS){
														break;
													}
													var flagCurrenMorTakenCount=0;
													for(j=0;j<=1;j++){
														if($scope.currPerReqTakenCount==reqPerRecOcc){
															break;
														}
														var dayOfDate=moment($scope.currentIterationDate).format('dddd');
													if($scope.cyclic.daySM1 == "Y"){	
														if(dayOfDate=="Monday"){
											    			setCreatedSlotListForService.appointmentDate = moment($scope.currentIterationDate,'DD/MM/YYYY').toDate();
													    	$scope.setCreatedSlotListForServiceList.push(setCreatedSlotListForService); 
													    	flagCurrenMorTakenCount=1;
													    	break;
										    			}
										    		}
													
												 if($scope.cyclic.daySM2 == "Y"){
										    			if(dayOfDate=="Tuesday"){
											    			setCreatedSlotListForService.appointmentDate = moment($scope.currentIterationDate,'DD/MM/YYYY').toDate();
													    	$scope.setCreatedSlotListForServiceList.push(setCreatedSlotListForService); 
													    	flagCurrenMorTakenCount=1;
													    	break;
										    			}
										    		}
												 
												 if($scope.cyclic.daySM3 == "Y"){
										    			if(dayOfDate=="Wednesday"){
										    			setCreatedSlotListForService.appointmentDate = moment($scope.currentIterationDate,'DD/MM/YYYY').toDate();
												    	$scope.setCreatedSlotListForServiceList.push(setCreatedSlotListForService); 
												    	flagCurrenMorTakenCount=1;
												    	break;
										    			}
										    		}
												 
												 if($scope.cyclic.daySM4 == "Y"){
										    			if(dayOfDate=="Thursday"){
										    			setCreatedSlotListForService.appointmentDate = moment($scope.currentIterationDate,'DD/MM/YYYY').toDate();
												    	$scope.setCreatedSlotListForServiceList.push(setCreatedSlotListForService); 
												    	flagCurrenMorTakenCount=1;
												    	break;
										    			}
										    		}
												 
												 if($scope.cyclic.daySM5 == "Y"){
										    			if(dayOfDate=="Friday"){
										    			setCreatedSlotListForService.appointmentDate = moment($scope.currentIterationDate,'DD/MM/YYYY').toDate();
												    	$scope.setCreatedSlotListForServiceList.push(setCreatedSlotListForService);
												    	flagCurrenMorTakenCount=1;
												    	break;
										    			}
										    		}
												 
												 if($scope.cyclic.daySM6 == "Y"){
										    			if(dayOfDate=="Saturday"){
										    			setCreatedSlotListForService.appointmentDate = moment($scope.currentIterationDate,'DD/MM/YYYY').toDate();
												    	$scope.setCreatedSlotListForServiceList.push(setCreatedSlotListForService); 
												    	flagCurrenMorTakenCount=1;
												    	break;
										    			}
										    		}
												 
												 if($scope.cyclic.daySM7 == "Y"){
										    			if(dayOfDate=="Sunday"){
										    			setCreatedSlotListForService.appointmentDate = moment($scope.currentIterationDate,'DD/MM/YYYY').toDate();
												    	$scope.setCreatedSlotListForServiceList.push(setCreatedSlotListForService); 
												    	flagCurrenMorTakenCount=1;
												    	break;
										    			}
										    		}
												}
													$scope.currPerReqTakenCount=flagCurrenMorTakenCount+$scope.currPerReqTakenCount;
													// morning check end
													
													// evening preference
													var setCreatedSlotListForService = {
															'appointmentDate':'',
															'appointmentTime':moment($scope.currentDateCyclic).format('YYYY-MM-DD')==moment($scope.currentIterationDate).format('YYYY-MM-DD')?$scope.currentTime:$scope.docEveningStartTime
													
													};
													if($scope.setCreatedSlotListForServiceList.length==obj.sessionsS){
														break;
													}
													var flagEveCurTakenCount=0;
													var currentAppObj;
													for(j=0;j<=1;j++){
														if($scope.currPerReqTakenCount==reqPerRecOcc){
															break;
														}
														if($scope.cyclic.daySE1 == "Y"){
											    			if(dayOfDate=="Monday"){
											    			setCreatedSlotListForService.appointmentDate = moment($scope.currentIterationDate,'DD/MM/YYYY').toDate();
													    	$scope.setCreatedSlotListForServiceList.push(setCreatedSlotListForService); 
													    	flagEveCurTakenCount=1;
													    	break;
											    			}
											    		}
														
														if($scope.cyclic.daySE2 == "Y"){
											    			if(dayOfDate=="Tuesday"){
											    			setCreatedSlotListForService.appointmentDate = moment($scope.currentIterationDate,'DD/MM/YYYY').toDate();
													    	$scope.setCreatedSlotListForServiceList.push(setCreatedSlotListForService); 
													    	flagEveCurTakenCount=1;
													    	break;
											    			}
											    		}
														
														if($scope.cyclic.daySE3 == "Y"){
											    			if(dayOfDate=="Wednesday"){
											    			setCreatedSlotListForService.appointmentDate = moment($scope.currentIterationDate,'DD/MM/YYYY').toDate();
													    	$scope.setCreatedSlotListForServiceList.push(setCreatedSlotListForService); 
													    	flagEveCurTakenCount=1;
													    	break;
											    			}
											    		}
													
														if($scope.cyclic.daySE4 == "Y"){
											    			if(dayOfDate=="Thursday"){
											    			setCreatedSlotListForService.appointmentDate = moment($scope.currentIterationDate,'DD/MM/YYYY').toDate();
													    	$scope.setCreatedSlotListForServiceList.push(setCreatedSlotListForService); 
													    	flagEveCurTakenCount=1;
													    	break;
											    			}
											    		}
														
														if($scope.cyclic.daySE5 == "Y"){
											    			if(dayOfDate=="Friday"){
											    			setCreatedSlotListForService.appointmentDate = moment($scope.currentIterationDate,'DD/MM/YYYY').toDate();
													    	$scope.setCreatedSlotListForServiceList.push(setCreatedSlotListForService); 
													    	flagEveCurTakenCount=1;
													    	break;
											    			}
											    		}
														
														if($scope.cyclic.daySE6 == "Y"){
											    			if(dayOfDate=="Saturday"){
											    			setCreatedSlotListForService.appointmentDate = moment($scope.currentIterationDate,'DD/MM/YYYY').toDate();
													    	$scope.setCreatedSlotListForServiceList.push(setCreatedSlotListForService); 
													    	flagEveCurTakenCount=1;
													    	break;
											    			}
											    		}
														
														if($scope.cyclic.daySE7 == "Y"){
											    			if(dayOfDate=="Sunday"){
											    			setCreatedSlotListForService.appointmentDate = moment($scope.currentIterationDate,'DD/MM/YYYY').toDate();
													    	$scope.setCreatedSlotListForServiceList.push(setCreatedSlotListForService); 
													    	flagEveCurTakenCount=1;
													    	break;
											    			}
											    		}
													}	
													$scope.currPerReqTakenCount=$scope.currPerReqTakenCount+flagEveCurTakenCount;
										}
									}
						   					
										if(remainingOcc>0 && flagForLastDateApp==true){
											 if($scope.setCreatedSlotListForServiceList.length==0){
												 growl.warning(
															'Provided conditions are incorrect',
															{
																title : 'Warning!'
															});
												return false; 
											 }
												var lastDateAppCurrRecc=$scope.setCreatedSlotListForServiceList[$scope.setCreatedSlotListForServiceList.length-1];
												$scope.currentIterationDate=lastDateAppCurrRecc;
												for(b=1;b<=remainingOcc;b++){
													if($scope.setCreatedSlotListForServiceList.length==obj.sessionsS){
														break;
													}
													if($scope.currPerReqTakenCount==reqPerRecOcc){
														break;
													}
											    	$scope.setCreatedSlotListForServiceList.push(lastDateAppCurrRecc); 
												}
											}
											
											var appAvail=false;
									       if(counterForApp!=$scope.setCreatedSlotListForServiceList.length){
									    	   counterForApp = angular.copy($scope.setCreatedSlotListForServiceList.length);  
									    	   appAvail=true;
									       }		
											
									       
									       flagForLastDateApp=false,remainingOcc=0;
											// next recurence
											if($scope.setCreatedSlotListForServiceList.length % reqPerRecOcc==0 && $scope.currPerReqTakenCount>0){
												var backDate=new Date($scope.lastRecDate.getTime());
												
												/*
												 * if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')){
												 * $scope.currentIterationDate=new
												 * Date($scope.tempDate.getTime() +
												 * ($scope.addDays)*24*60*60*1000);
												 * $scope.tempDate =
												 * angular.copy($scope.currentIterationDate); }
												 */
												if(moment($scope.currentIterationDate).format('YYYY-MM-DD')<=moment($scope.lastRecDate).format('YYYY-MM-DD')){
													$scope.currentIterationDate=new Date(backDate.getTime()+1*24*60*60*1000);
													$scope.tempDate = angular.copy($scope.currentIterationDate);
												}
								    			 $scope.firstRecDate=new Date($scope.lastRecDate.getTime()+1*24*60*60*1000);
								    			 $scope.lastRecDate=new Date($scope.firstRecDate.getTime() + (addWithEveryRec-1)*24*60*60*1000);
								    			 $scope.currPerReqTakenCount=0;
								    			   if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastDate).format('YYYY-MM-DD')){
														break;
													}
											}else if(appAvail==false && $scope.addDays>1){
												var flagObj=false;
												for(a=1;a<=$scope.addDays;a++){
													if(a%2==1){
														$scope.currentIterationDate=new Date($scope.tempDate.getTime()-(a==1?1:Math.round(a/2))*24*60*60*1000);
													}else{
														$scope.currentIterationDate=new Date($scope.tempDate.getTime()+(a==2?1:a/2)*24*60*60*1000);	
													}
													if(moment($scope.currentIterationDate).format('YYYY-MM-DD')<moment($scope.firstRecDate).format('YYYY-MM-DD')){
														continue;
													}
													
													if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')){
														continue;
													}
													
													if(($scope.cyclic.daySM1 == "Y" && $scope.currentIterationDate.getDay()==1) || ($scope.cyclic.daySM2 == "Y" && $scope.currentIterationDate.getDay()==2) || ($scope.cyclic.daySM3 == "Y" && $scope.currentIterationDate.getDay()==3)
									    					|| ($scope.cyclic.daySM4 == "Y" && $scope.currentIterationDate.getDay()==4) || ($scope.cyclic.daySM5 == "Y" && $scope.currentIterationDate.getDay()==5) 
									    					|| ($scope.cyclic.daySM6 == "Y" && $scope.currentIterationDate.getDay()==6) || ($scope.cyclic.daySM7 == "Y" && $scope.currentIterationDate.getDay()==0) 
									    					||($scope.cyclic.daySE1 == "Y" && $scope.currentIterationDate.getDay()==1) || ($scope.cyclic.daySE2 == "Y" && $scope.currentIterationDate.getDay()==2) || ($scope.cyclic.daySE3 == "Y" && $scope.currentIterationDate.getDay()==3)
									    					|| ($scope.cyclic.daySE4 == "Y" && $scope.currentIterationDate.getDay()==4) || ($scope.cyclic.daySE5 == "Y" && $scope.currentIterationDate.getDay()==5) 
									    					|| ($scope.cyclic.daySE6 == "Y" && $scope.currentIterationDate.getDay()==6) || ($scope.cyclic.daySE7 == "Y" && $scope.currentIterationDate.getDay()==0)){
													      flagObj=true;
									    			}
													if(flagObj==true){
														$scope.tempDate = angular.copy($scope.currentIterationDate);
														break;
													}
												}
												
												if(moment($scope.tempDate).format('YYYY-MM-DD')>moment($scope.lastDate).format('YYYY-MM-DD')){
													break;
												}
												if(flagObj==false){
													if(moment($scope.tempDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')
															&& $scope.currPerReqTakenCount>0){
														 remainingOcc=reqPerRecOcc-$scope.currPerReqTakenCount;
														flagForLastDateApp=true;
													}else{
														if(moment($scope.tempDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')){
															  var backDate=new Date($scope.lastRecDate.getTime());
															$scope.currentIterationDate=new Date(backDate.getTime()+1*24*60*60*1000);
															$scope.tempDate = angular.copy($scope.currentIterationDate);
											    			 $scope.firstRecDate=new Date($scope.lastRecDate.getTime()+1*24*60*60*1000);
											    			 $scope.lastRecDate=new Date($scope.firstRecDate.getTime() + (addWithEveryRec-1)*24*60*60*1000);
											    			 $scope.currPerReqTakenCount=0;
														}else{
															$scope.currentIterationDate=new Date($scope.tempDate.getTime() + ($scope.addDays)*24*60*60*1000);
															$scope.tempDate = angular.copy($scope.currentIterationDate);
														}
													}
												}
											}else{
											   $scope.currentIterationDate=new Date($scope.currentIterationDate.getTime() + ($scope.addDays)*24*60*60*1000);
											   $scope.tempDate = angular.copy($scope.currentIterationDate);
											   if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')){
												   if($scope.currPerReqTakenCount>0){
														 remainingOcc=reqPerRecOcc-$scope.currPerReqTakenCount;
														flagForLastDateApp=true;
										        	}else{
												     var backDate=new Date($scope.lastRecDate.getTime());
													 $scope.currentIterationDate=new Date(backDate.getTime()+1*24*60*60*1000);
													 $scope.tempDate = angular.copy($scope.currentIterationDate);
									    			 $scope.firstRecDate=new Date($scope.lastRecDate.getTime()+1*24*60*60*1000);
									    			 $scope.lastRecDate=new Date($scope.firstRecDate.getTime() + (addWithEveryRec-1)*24*60*60*1000);
									    			 $scope.currPerReqTakenCount=0;
										        	}
												}
											   if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastDate).format('YYYY-MM-DD')){
													break;
												}
											}
											
											
											// if no slots added then keep counter as
											// previous
											 i=$scope.setCreatedSlotListForServiceList.length==0?0:($scope.setCreatedSlotListForServiceList.length-1);
										}// end of main loop
									        
									// ************************place all appointment on
									// last date*************************
												 if(obj.sessionsS!=$scope.setCreatedSlotListForServiceList.length){
													 if($scope.setCreatedSlotListForServiceList.length==0){
														 growl.warning(
																	'Provided conditions are incorrect',
																	{
																		title : 'Warning!'
																	});
														return false; 
													 }
														var lastDateAppObj=$scope.setCreatedSlotListForServiceList[$scope.setCreatedSlotListForServiceList.length-1];
														for(var K=0;K<obj.sessionsS;K++){
																if($scope.setCreatedSlotListForServiceList.length==obj.sessionsS){
																	break;
																}
																// setCreatedSlotListForService.appointmentDate
																// =
																// moment(lastDateAppCurrRecc,'DD/MM/YYYY').toDate();
														    	$scope.setCreatedSlotListForServiceList.push(lastDateAppObj); 
															}
														 }
								    			$('#showDoctorModalService').modal();   
									}
												
												
													$scope.stopLoader();		
													console.log($scope.setCreatedSlotListForServiceList);
										
										}
										if ($scope.firstTabLevel == 9 && newTab == 5) {
											
											$scope.getOPDAdmissionDetails();
											// get Admission List
											var URI = BASE_URL + ROOT_URL + GETADMISSIONREASONS;
								    		GenericServiceParamHeader.serviceAction(METHOD_GET, URI)
								    		.then(
								    			function(response) {
								    				if (response.data.status == "success") {
								    					$scope.reasonsForAdmission = response.data.listObject;

								    				}
								    			});
								    		
								    		var commonObj = {
													unitId : $scope.unitId,
													organizationId : $scope.organizationId
												};
								    		// For Get Active Reason List
											var URI = BASE_URL + ROOT_URL + GETACTIVEREASONLIST;
											GenericService.serviceAction("POST", URI, commonObj)
													.then( function(response) {
																$scope.reasonList = [];

																if (response.data.status == "success") {
																	$scope.reasonList = response.data.listObject;

																}
															});
								    		
								    		
		                                    var URI = BASE_URL + ROOT_URL+"/adt/bedCategory/availableBeds";

		// + GETACTIVEBEDCATEGORYLISTBYUNIT;
								
									BillingGenericService.serviceAction(METHOD_POST, URI,commonObj,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(function(response) {
														if (response.data.status == "success")
															$scope.bedCategoryList=[];
														$scope.allBedCategoryList=response.data.listObject;
														$scope.dayCareIpBillingBedCateList=response.data.object.dayCareIpBedCategoryList;
														angular.forEach($scope.allBedCategoryList,function(value,index){
															var flag=false;
															angular.forEach($scope.dayCareIpBillingBedCateList,function(value1,index){
																// for day Care
																if(value.bedCategoryId==value1.bedCategoryId && value1.visitTypeId==2){
																	flag=true;
																}
															});
															if(flag==true){
																$scope.bedCategoryList.push(value);
															}
														});
														console.log("bedCategoryList", $scope.bedCategoryList);
													});
									
							    	
									$scope.getDoctorBySpeciality = function(id) {

										var data = {
											specialityId : id,
											unitId : $scope.unitId,
											organizationId : $scope.organizationId
										};
										var URI = BASE_URL + ROOT_URL
												+ GETDOCTORBYSPECIALITYID;
										
										BillingGenericService
												.serviceAction(METHOD_POST, URI, data,
														NOTIFICATION_MSG_STATUS_FALSE)
												.then(
														function(response) {
															console.log(response);
															$scope.doctorList = [];
															// $rootScope.startLoader();
															if (response.data.status == "success")
																$scope.doctorList = response.data.listObject;
														});

									}
			                    
			                    	    $scope.getDoctorBySpecialityTreating = function(id) {

										var data = {
											specialityId : id,
											unitId : $scope.unitId,
											organizationId : $scope.organizationId
										};
										var URI = BASE_URL + ROOT_URL
												+ GETDOCTORBYSPECIALITYID;
										
										BillingGenericService
												.serviceAction(METHOD_POST, URI, data,
														NOTIFICATION_MSG_STATUS_FALSE)
												.then(
														function(response) {
															console.log(response);
															$scope.doctorTreatingList = [];
															// $rootScope.startLoader();
															if (response.data.status == "success")
																$scope.doctorTreatingList = response.data.listObject;
														});

									      }
											
										}
										 $scope.getDoctorBySpeciality = function(id) {

												var data1 = {
													specialityId : id,
													unitId : $scope.unitId,
													organizationId : $scope.organizationId
												};
												var URI = BASE_URL + ROOT_URL
														+ GETDOCTORBYSPECIALITYID;
												
												BillingGenericService
														.serviceAction(METHOD_POST, URI, data1,
																NOTIFICATION_MSG_STATUS_FALSE)
														.then(
																function(response) {
																	console.log(response);
																	$scope.doctorList = [];
																	// $rootScope.startLoader();
																	if (response.data.status == "success")
																		$scope.doctorList = response.data.listObject;
																});

											}
										  $scope.getDoctorBySpecialityMedication = function(id) {
									        	// alert();
												var data = {
													specialityId : id,
													unitId : $scope.unitId,
													organizationId : $scope.organizationId
												};
												var URI = BASE_URL + ROOT_URL
														+ GETDOCTORBYSPECIALITYID;
												
												BillingGenericService
														.serviceAction(METHOD_POST, URI, data,
																NOTIFICATION_MSG_STATUS_FALSE)
														.then(
																function(response) {
																	console.log(response);
																	$scope.doctorListMedication = [];
																	// $rootScope.startLoader();
																	if (response.data.status == "success")
																		$scope.doctorListMedication = response.data.listObject;
																});

											}
										// ************************************Save cyclic
										// order*************************************************//
										
										var commonObj1 = {
												unitId : $scope.unitId,
												organizationId : $scope.organizationId
											};
										$scope.getSpecialityListForCyclic = function() {
											var URI = BASE_URL + ROOT_URL
													+ GETACTIVESPECIALITY;
											BillingGenericService
													.serviceAction(METHOD_POST, URI,
															commonObj1,
															NOTIFICATION_MSG_STATUS_FALSE)
													.then(
															function(response) {
																if (response.data.status == "success")
																	$scope.specialityListForCyclic = response.data.listObject;
																angular.forEach($scope.specialityListForCyclic,function(value,key){
																	if(value.specialityId == $scope.patientDetails.specialityId ){
																		$scope.patientDetails.specialityName = value.specialityName;
																	}
																})
															});
										}
										$scope.getSpecialityListForCyclic();
										
										$scope.cyclicOrderDocSpeciality={};
										
										$scope.getDoctorySpecilaityForCyclic = function() {
											// getDoctorSpeciality For Procedure order
											var specDocObj = {
												unitId : $scope.unitId,
												organizationId : $scope.organizationId,
												doctorId : $scope.encounterDetailsObj.doctorId
											}
											console.log('uri', URI);
											var URI = BASE_URL + ROOT_URL
													+ GETSPECIALITYLISTBYDOCTORID;
											console.log('uri', URI);
											console.log('specDocObj', specDocObj);
											BillingGenericService
													.serviceAction(METHOD_POST, URI,
															specDocObj,
															NOTIFICATION_MSG_STATUS_FALSE)
													.then(
															function(response) {
																console.log(response);

																if (response.data.status == "success") {
																	$scope.docSpecialityListForCyclic = response.data.listObject;
																	   /*
																		 * if($scope.docSpecialityListForCyclic.length>0){
																		 * $scope.specialityObj.specialityName =
																		 * $scope.docSpecialityListForCyclic[0].specialityName;
																		 * $scope.specialityObj.specialityId =
																		 * $scope.docSpecialityListForCyclic[0].specialityId; }
																		 */
																}
															});
										};
										$scope.getDoctorySpecilaityForCyclic();
										$scope.getDoctorBySpecialityForCyclic = function(obj) {
											var object=JSON.parse(obj);
											var data = {
												specialityId : object.specialityId,
												unitId : $scope.unitId,
												organizationId : $scope.organizationId
											};
											$scope.startLoader();
											$scope.cyclic.doctorId=0;
											$scope.cyclic.specialityId=object.specialityId;
											$scope.deptWiseAppointment='N';
											var URI = BASE_URL + ROOT_URL + GETDOCTORBYSPECIALITYID;
											BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
													.then(function(response) {
																console.log(response);
																if (response.data.status == "success"){
																	$scope.doctorListForCyclic = response.data.listObject;
																	$scope.stopLoader();
																}else{
																	$scope.stopLoader();
																}
															});
												}
										
										// ****************************************Generate
										// doctor Schedule and department
										// schedule************************************************************
																	$scope.slotDetailsList = [];
																	$scope.setCreatedSlotList = [];
																	$scope.setCreatedTimeSlotList = [];
																	$scope.setCreatedSlotListForServiceList = [];
																	$scope.idx = 0;
																	$scope.toDate = moment(new Date()).format('DD-MM-YYYY');
																	$scope.diff = 0;
																	$scope.setCreatedSlotList.reSchedule = 'N';
																	$scope.setCreatedSlotList = [];    
																  // Generate schedule
																	// for doctor
																	$scope.isAppointmentSchedule='N';
																	
																	var currDate = new Date();
																     $scope.currentTime = moment(currDate).format('HH:mm');
																     $scope.currentDateCyclic=moment(currDate).format('YYYY-MM-DD');
															
																     $scope.checkDayExist=function(date){
															    			var flag=false;
															    			
															    			angular.forEach($scope.setCreatedSlotList,function(value,index){
															    				if(moment(value.appointmentDate2).format('YYYY-MM-DD')==moment(date).format('YYYY-MM-DD')){
																		    			flag=true;
															    				}
															    			});
															    			
															    			return flag;
															    		}
																     
																     $scope.checkDayExistInService=function(date){
															    			var flag=false;
															    			
															    			angular.forEach($scope.setCreatedSlotListForServiceList,function(value,index){
															    				if(moment(value.appointmentDate).format('YYYY-MM-DD')==moment(date).format('YYYY-MM-DD')){
																		    			flag=true;
															    				}
															    			});
															    			
															    			return flag;
															    		} 
											$scope.generateSchedule = function(obj){
												   $scope.cyclic.specialityId=$scope.encounterDetailsObj.specialityId;
													$scope.cyclic.doctorId=$scope.encounterDetailsObj.doctorId;
													$scope.cyclic.doctorName=obj.doctorId;
		// $scope.cyclic.datepickerFrom=moment(obj.datepickerFrom).format('YYYY-MM-DD');
													 var currDate = new Date();
												     $scope.currentTime = moment(currDate).format('HH:mm');
												     $scope.currentDateCyclic=moment(currDate).format('YYYY-MM-DD');
												     
													$scope.slotDetailsList = [];
													$scope.setCreatedSlotList = [];
													$scope.isAppointmentSchedule='Y';
													$scope.cyclic.noOfSessions = obj.sessions;
													$scope.cyclic.toDate="";
													if($scope.patientDetails.patientId == 0 || $scope.patientDetails.patientId == undefined){
														growl.warning("Please select Patient first",{
															title : 'Warning!'
														});
														return;
													}
													
													if(obj.specialityId.specialityId == 0 /*
																							 * ||
																							 * obj.doctorId ==
																							 * 0
																							 */){
														growl.warning("Please select Speciality and Doctor",{
															title : 'Warning!'
														});
														$scope.cyclic.specialityName="";
														$scope.cyclic.docName="";
														return;
													}
													
													if(obj.doctorId==null || obj.doctorId==0){
														$scope.cyclic.docName="";
													}
													
													if(obj.sessions==0 || obj.sessions=="" || obj.sessions==undefined){
														growl.warning("Number of session can not be blank",{
															title : 'Warning!'
														});
														return;
													}
												
													if(obj.sessions > 365){
														growl.warning(" You entered number of session greater than 365  ",{
															title : 'Warning!'
														});
														return;
													}
													if(obj.occurencesType!=undefined && obj.occurences!=undefined && obj.occurences>0 && obj.every!=undefined && obj.every>0){
														
														if(Number(obj.occurences)>Number(obj.sessions)){
															growl.warning("Number of occurences can not be greater than total sessions",{
																title : 'Warning!'
															});
															return;
														}
														
														 if(obj.duration!=undefined && obj.duration!=0 && obj.duration!=""){
																var recurrTotalDays=obj.every*(obj.occurencesType=='d'?1:(obj.occurencesType=='w'?7:30));	
																var durationTotalDays=obj.duration*(obj.durationType=='d'?1:(obj.durationType=='w'?7:30));
																if(Number(recurrTotalDays)>Number(durationTotalDays)){
																	growl.warning("Duration cannot be greater than total occurences",{
																		title : 'Warning!'
																	});
																	return;
																}
															}
													}	
												
													$scope.currentDate = new Date(obj.datepickerFrom);
												    $scope.startDate = new Date(obj.datepickerFrom);
												    
												    var duraType=obj.durationType == "d" || obj.durationType == "" || obj.duration==undefined?1:obj.durationType == "w"?7:30;
												    if(obj.duration!=undefined && obj.duration!=0 && obj.duration!=""){
											    		$scope.diff = obj.duration*duraType;
											    		$scope.toDate = $scope.currentDate.getTime() + ($scope.diff-1)*24*60*60*1000;
												    }else if(obj.every!=undefined && obj.every>0 && obj.occurences!=undefined && obj.occurences>0){
														var occuType=(obj.occurencesType=="d" || obj.occurencesType==""?1:obj.occurencesType=="w"?7:30);
														var duration=Math.round((obj.every*occuType*obj.sessions)/obj.occurences);
														$scope.toDate = $scope.currentDate.getTime() + duration*24*60*60*1000;
													}else{
														$scope.diff = (((obj.duration=="" || obj.duration==0 || obj.duration==undefined)?obj.sessions:obj.duration));
														$scope.toDate = $scope.currentDate.getTime() + ($scope.diff-1)*24*60*60*1000;
													}
												    
													if($scope.cyclic.dayM1=='Y' ||$scope.cyclic.dayM2=='Y' ||$scope.cyclic.dayM3=='Y' || $scope.cyclic.dayM4=='Y'
										    				|| $scope.cyclic.dayM5=='Y' || $scope.cyclic.dayM6=='Y' || $scope.cyclic.dayM7=='Y'){
														$scope.cyclic.morning='Y';
													}
													
													if($scope.cyclic.dayE1=='Y' ||$scope.cyclic.dayE2=='Y' ||$scope.cyclic.dayE3=='Y' || $scope.cyclic.dayE4=='Y'
										    				|| $scope.cyclic.dayE5=='Y' || $scope.cyclic.dayE6=='Y' || $scope.cyclic.dayE7=='Y'){
														$scope.cyclic.evening='Y';
													}
													
													if($scope.cyclic.evening === "Y" && $scope.cyclic.morning === "Y"){
														$scope.fromTime = $scope.docMorningStartTime;
														$scope.toTime = $scope.docEveningEndTime;
													}
													else if($scope.cyclic.evening === "Y"){
														$scope.fromTime = $scope.docEveningStartTime;
														$scope.toTime = $scope.docEveningEndTime;
													}else if($scope.cyclic.morning === "Y"){
														$scope.fromTime = $scope.docMorningStartTime;
														$scope.toTime = $scope.docMorningEndTime;
													}else{
														$scope.fromTime = $scope.docMorningStartTime;
														$scope.toTime = $scope.docEveningEndTime;
													}
													
												
													var data = {
															specialityId : $scope.encounterDetailsObj.specialityId,
															doctorId : (obj.doctorId!=undefined &&  obj.doctorId>0)?obj.doctorId:0,
															fromDate :moment(new Date($scope.startDate)).format('YYYY-MM-DD'),
															toDate :moment(new Date($scope.toDate)).format('YYYY-MM-DD'),
															organizationId : $scope.organizationId,
															unitId : $scope.unitId,
															slotTypeId : 10,
															fromTime : $scope.fromTime,
															toTime : $scope.toTime,
															deptWiseAppointment : $scope.deptWiseAppointment
														};
													
													console.log(data);
													$scope.cyclic.fromDate = moment(obj.datepickerFrom).format('DD/MM/YYYY');
													$scope.cyclic.toDate = moment($scope.toDate).format('DD/MM/YYYY');
													$scope.startLoader();
													var URI = BASE_URL + ROOT_URL + GETWEEKLYSLOTS;
													BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE)
															.then(function(response) {
																	console.log(JSON.stringify(response.data.listObject));
																		if (response.data.status == "success") {
																			
																			if(response.data.listObject.length==0){
																							if(obj.doctorId >0 ){
																								growl.warning("No slots available for selected doctor !",{
																									title : 'Warning!'
																								});
																							 return false;	
																							}else{
																								growl.warning("No slots available for selected department !",{
																									title : 'Warning!'
																								});
																							  return false;	
																							}
																						}
																						$scope.slotDetailsList = response.data.listObject;
																						$scope.slotDateList=response.data.object.appointmentDateList;
																						
																	 if((obj.dayM1=='N'  && obj.dayM2=='N' && obj.dayM3=='N' && obj.dayM4=='N' && obj.dayM5=='N' 
																		&& obj.dayM6=='N' && obj.dayM7=='N' && obj.dayE1=='N' && obj.dayE2=='N' &&obj.dayE3=='N' 
																		&& obj.dayE4=='N' && obj.dayE5=='N' && obj.dayE6=='N' && obj.dayE7=='N')){	
																		 
																		 $scope.endDate = new Date($scope.currentDate.getTime()+60*60*1000);	
																			var addWithEveryRec,noOfOccurences,reqPerRecOcc,totalWeek,totalDuration;
																			$scope.addDays;
																			var occuType=(obj.occurencesType=="d" || obj.occurencesType==""?1:obj.occurencesType=="w"?7:30);
																			var durationType=(obj.durationType=="d" || obj.occurencesType==""?1:(obj.durationType=="w"?7:30));	
																			if(obj.every!=undefined && obj.every>0 && obj.occurences!=undefined && obj.occurences>0
																						&&(obj.duration==undefined || obj.duration==0 || obj.duration=="")){
																					 totalDuration=Math.round((obj.every*occuType*obj.sessions)/obj.occurences);
																					  addWithEveryRec=obj.every*occuType;	
																					  noOfOccurences=Number(obj.occurences);
																					  reqPerRecOcc=noOfOccurences;
																					  $scope.addDays=Math.round(addWithEveryRec/(reqPerRecOcc==0?1:reqPerRecOcc));
																			}else if(obj.every!=undefined && obj.every>0 && obj.occurences!=undefined && obj.occurences>0){
																			  totalDuration=Number(obj.duration)*durationType;	
																			  addWithEveryRec=obj.every*occuType;	
																			  noOfOccurences=Number(obj.occurences);
																			  reqPerRecOcc=noOfOccurences;
																			  $scope.addDays=Math.round(addWithEveryRec/(reqPerRecOcc==0?1:reqPerRecOcc));
																			}else if(obj.duration!=undefined && obj.duration>0){
																			   totalDuration=Number(obj.duration)*durationType;
																			   noOfOccurences=Number(obj.sessions);
																			   if(obj.durationType=="d" || obj.durationType==""){
																				   if(totalDuration>noOfOccurences)
																			          addWithEveryRec=Math.round(totalDuration/noOfOccurences);
																				   else
																					   addWithEveryRec=1; 
																			   }else{
																				   addWithEveryRec=Math.round(totalDuration/noOfOccurences);
																			   }
																			   reqPerRecOcc=Math.floor(noOfOccurences/totalDuration);
																			   $scope.addDays=addWithEveryRec;
																			}else{
																				   totalDuration=Number(obj.sessions);
																				   addWithEveryRec=1;
																				   noOfOccurences=Number(obj.sessions);
																				   reqPerRecOcc=1;
																				   $scope.addDays=1
																			}
																			 $scope.lastDate=new Date($scope.endDate.getTime()+(totalDuration-1)*24*60*60*1000);
																			 
																			if($scope.addDays==0){
																				$scope.addDays=1;
																			}
																			if(reqPerRecOcc==0){
																				reqPerRecOcc=1;
																			}
																			
																			// $scope.everyWeekFirstDateCounter=angular.copy($scope.endDate);
																			$scope.firstRecDate=$scope.endDate;
																			 var backOneDate = new Date($scope.endDate.getTime()-1*24*60*60*1000);
																			// for
																			// recurrence
																			// criteria
																			$scope.lastRecDate = new Date(backOneDate.getTime() + addWithEveryRec*24*60*60*1000);
																			$scope.currPerReqTakenCount=0;
																			// first
																			// week last
																			// date
																			// $scope.lastDateofCurrWeek=new
																			// Date(backOneDate.getTime()
																			// +
																			// 7*24*60*60*1000)
																			$scope.currentIterationDate=backOneDate;				  

																			$scope.tempDate=angular.copy(new Date($scope.currentIterationDate.getTime() +1*24*60*60*1000));
																	     var flagStop=0,i=0,t,j=0,a,b,flagForLastDateApp=false,counterForApp=0,remainingOcc=0;

																			for(i;i<=obj.sessions; i++){
																					 if(i==0){
																						 $scope.currentIterationDate = new Date($scope.currentIterationDate.getTime() +1*24*60*60*1000);
																						// $scope.tempDate
																						// =
																						// angular.copy($scope.currentIterationDate);
																					 }
																					
																					 if($scope.setCreatedSlotList.length==obj.sessions){
																						 break;
																					 }
																					 
																					 if((moment($scope.currentIterationDate).format('YYYY-MM-DD')<=moment($scope.lastRecDate).format('YYYY-MM-DD'))){
																						// slot
																						// against
																						// doctor
																						// and
																						// department
																						for(j=0;j<$scope.slotDetailsList.length;j++){
																							   if(moment($scope.currentIterationDate).format('YYYY-MM-DD') === moment($scope.slotDetailsList[j].appointmentDate2).format('YYYY-MM-DD') 
																							    		&& (moment($scope.currentIterationDate).format('YYYY-MM-DD')== moment(new Date()).format('YYYY-MM-DD') 
																							    				? ($scope.slotDetailsList[j].originalFromTime>$scope.currentTime ? true:false):true)){ 
																						    	
																								// if
																								// overbooking
																								// allowed
																						    	if(($scope.cyclic.overBooking=='Y' && $scope.slotDetailsList[j].allowedOverBookingSlots>$scope.slotDetailsList[j].slotPatientDetailsListsSize) 
																						    			||($scope.slotDetailsList[j].slotStatusId==1 && $scope.slotDetailsList[j].isVip=='N'))
																						    	{
																						    	
																						    		if($scope.slotDetailsList[j].allowedOverBookingSlots>$scope.slotDetailsList[j].slotPatientDetailsListsSize 
																						    				&& $scope.cyclic.overBooking=='Y' && $scope.slotDetailsList[j].slotPatientDetailsListsSize>0){
																						    			$scope.slotDetailsList[j].slotStatusId=6;
																						    		}else{
																						    			$scope.slotDetailsList[j].slotStatusId=4;
																						    		}
																						    		
																						    		$scope.setCreatedSlotList.push($scope.slotDetailsList[j]);
																						    		$scope.currPerReqTakenCount=$scope.currPerReqTakenCount+1;
																								    break;		
																						      }
																							}
																						}
																					 }
																					 
																					 if(remainingOcc>0 && flagForLastDateApp==true){
																						   var lastDateAppCurrRecc=$scope.setCreatedSlotList[$scope.setCreatedSlotList.length-1].appointmentDate2;
																						   var lastDateAppDummyObj=$scope.setCreatedSlotList[$scope.setCreatedSlotList.length-1];
																							for(b=1;b<=remainingOcc;b++){
																								if($scope.currPerReqTakenCount==reqPerRecOcc){
																									break;
																								}
																								var currentAppObj;
																								for(j=0;j<$scope.slotDetailsList.length;j++){
																										if($scope.setCreatedSlotList.length==obj.sessions){
																											break;
																										}
																										if($scope.currPerReqTakenCount==reqPerRecOcc){
																											break;
																										}
																									currentAppObj=$scope.slotDetailsList[j];
																									  if(moment(lastDateAppCurrRecc).format('YYYY-MM-DD') === moment(currentAppObj.appointmentDate2).format('YYYY-MM-DD') 
																											  && (lastDateAppDummyObj.fromTimeString!=currentAppObj.fromTimeString)
																									    		&& (moment(lastDateAppCurrRecc).format('YYYY-MM-DD')== moment(new Date()).format('YYYY-MM-DD') 
																									    				? (currentAppObj.originalFromTime>$scope.currentTime ? true:false):true)){ 
																										  
																										  if((obj.overBooking=='Y' && currentAppObj.allowedOverBookingSlots>currentAppObj.slotPatientDetailsListsSize) 
																									    			||(currentAppObj.slotStatusId==1)){
																									    		
																											  if(currentAppObj.allowedOverBookingSlots>currentAppObj.slotPatientDetailsListsSize 
																									    				&& obj.overBooking=='Y' && currentAppObj.slotPatientDetailsListsSize>0){
																									    			currentAppObj.slotStatusId=6;
																									    		}else{
																									    			currentAppObj.slotStatusId=4;
																									    		}
																											  $scope.setCreatedSlotList.push(currentAppObj);
																											$scope.currPerReqTakenCount=$scope.currPerReqTakenCount+1;
																										  }
																									  }
																								}
																					        
																							}
																						}
																						
																						 
																						 if(counterForApp!=$scope.setCreatedSlotList.length){
																					    	   counterForApp = angular.copy($scope.setCreatedSlotList.length);  
																					       }		
																							
																					       
																					       flagForLastDateApp=false,remainingOcc=0;
																							// next
																							// recurence
																							if($scope.setCreatedSlotList.length % reqPerRecOcc==0 && $scope.currPerReqTakenCount>0){
																								var backDate=new Date($scope.lastRecDate.getTime());
																								
																								/*
																								 * if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')){
																								 * $scope.currentIterationDate=new
																								 * Date($scope.tempDate.getTime() +
																								 * ($scope.addDays)*24*60*60*1000);
																								 * $scope.tempDate =
																								 * angular.copy($scope.currentIterationDate); }
																								 */
																								if(moment($scope.currentIterationDate).format('YYYY-MM-DD')<=moment($scope.lastRecDate).format('YYYY-MM-DD')){
																									$scope.currentIterationDate=new Date(backDate.getTime()+1*24*60*60*1000);
																									$scope.tempDate = angular.copy($scope.currentIterationDate);
																								}
																				    			 $scope.firstRecDate=new Date($scope.lastRecDate.getTime()+1*24*60*60*1000);
																				    			 $scope.lastRecDate=new Date($scope.firstRecDate.getTime() + (addWithEveryRec-1)*24*60*60*1000);
																				    			 $scope.currPerReqTakenCount=0;
																				    			   if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastDate).format('YYYY-MM-DD')){
																										break;
																									}
																							}else if(counterForApp==$scope.setCreatedSlotList.length && $scope.addDays>1 && $scope.currPerReqTakenCount==0){
																								var flagObj=false;
																								for(a=1;a<=$scope.addDays;a++){
																									if(a%2==1){
																										$scope.currentIterationDate=new Date($scope.tempDate.getTime()-(a==1?1:Math.round(a/2))*24*60*60*1000);
																									}else{
																										$scope.currentIterationDate=new Date($scope.tempDate.getTime()+(a==2?1:a/2)*24*60*60*1000);	
																									}
																									if(moment($scope.currentIterationDate).format('YYYY-MM-DD')<moment($scope.firstRecDate).format('YYYY-MM-DD')){
																										continue;
																									}
																									
																									if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')){
																										continue;
																									}
																									
																									var list=$.grep($scope.slotDateList, function (obj) {
																								        return obj === moment($scope.currentIterationDate).format('DD-MM-YYYY');
																								    });
																									if(list.length>0){
																									      flagObj=true;
																					    			}
																									if(flagObj==true){
																										$scope.tempDate = angular.copy($scope.currentIterationDate);
																										break;
																									}
																								}
																								
																								if(moment($scope.tempDate).format('YYYY-MM-DD')>moment($scope.lastDate).format('YYYY-MM-DD')){
																									break;
																								}
																								if(flagObj==false){
																									if(moment($scope.tempDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')
																											&& $scope.currPerReqTakenCount>0){
																										 remainingOcc=reqPerRecOcc-$scope.currPerReqTakenCount;
																										flagForLastDateApp=true;
																									}else{
																										if(moment($scope.tempDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')){
																											  var backDate=new Date($scope.lastRecDate.getTime());
																											$scope.currentIterationDate=new Date(backDate.getTime()+1*24*60*60*1000);
																											$scope.tempDate = angular.copy($scope.currentIterationDate);
																							    			 $scope.firstRecDate=new Date($scope.lastRecDate.getTime()+1*24*60*60*1000);
																							    			 $scope.lastRecDate=new Date($scope.firstRecDate.getTime() + (addWithEveryRec-1)*24*60*60*1000);
																							    			 $scope.currPerReqTakenCount=0;
																										}else{
																											$scope.currentIterationDate=new Date($scope.tempDate.getTime() + ($scope.addDays)*24*60*60*1000);
																											$scope.tempDate = angular.copy($scope.currentIterationDate);
																										}
																									}
																								}
																							}else{
																							   $scope.currentIterationDate=new Date($scope.currentIterationDate.getTime() + ($scope.addDays)*24*60*60*1000);
																							   $scope.tempDate = angular.copy($scope.currentIterationDate);
																							   if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')){
																								        if($scope.currPerReqTakenCount>0){
																													 remainingOcc=reqPerRecOcc-$scope.currPerReqTakenCount;
																													flagForLastDateApp=true;
																										}else{
																											  var backDate=new Date($scope.lastRecDate.getTime());
																											 $scope.currentIterationDate=new Date(backDate.getTime()+1*24*60*60*1000);
																											 $scope.tempDate = angular.copy($scope.currentIterationDate);
																							    			 $scope.firstRecDate=new Date($scope.lastRecDate.getTime()+1*24*60*60*1000);
																							    			 $scope.lastRecDate=new Date($scope.firstRecDate.getTime() + (addWithEveryRec-1)*24*60*60*1000);
																							    			 $scope.currPerReqTakenCount=0;
																									}
																								}
																							   if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastDate).format('YYYY-MM-DD')){
																									break;
																								}
																							}
																												 
																						 if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastDate.getTime()+1*24*60*60*1000).format('YYYY-MM-DD')){
																							 break;
																						 }

																						 
																						// if
																						// no
																						// slots
																						// added
																						// then
																						// keep
																						// counter
																						// as
																						// previous
																						 i=$scope.setCreatedSlotList.length==0?0:($scope.setCreatedSlotList.length-1);
																				}
																										// ************Place
																										// all
																										// appointment
																										// on
																										// last
																										// date*******

																												        if(obj.sessions!=$scope.setCreatedSlotList.length){
																												       	 if($scope.setCreatedSlotList.length==0){
																															 growl.warning(
																																		'Provided conditions are incorrect',
																																		{
																																			title : 'Warning!'
																																		});
																															return false; 
																														 }
																															// if
																															// current
																															// recurrence
																															// iteration
																															// last
																															// day
																															// meet
																															var lastDateAppCurrRecc=$scope.setCreatedSlotList[$scope.setCreatedSlotList.length-1].appointmentDate2;
																															var lastDateAppDummyObj=$scope.setCreatedSlotList[$scope.setCreatedSlotList.length-1];
																															var currentAppObj;
																															for(j=0;j<$scope.slotDetailsList.length;j++){
																																	if($scope.setCreatedSlotList.length==obj.sessions){
																																		break;
																																	}
																																
																																currentAppObj=$scope.slotDetailsList[j];
																																  if(moment(lastDateAppCurrRecc).format('YYYY-MM-DD') === moment(currentAppObj.appointmentDate2).format('YYYY-MM-DD') 
																																		  && (lastDateAppDummyObj.fromTimeString!=currentAppObj.fromTimeString)
																																    		&& (moment(lastDateAppCurrRecc).format('YYYY-MM-DD')== moment(new Date()).format('YYYY-MM-DD') 
																																    				? (currentAppObj.originalFromTime>$scope.currentTime ? true:false):true)){ 
																																	  
																																	  if((obj.overBooking=='Y' && currentAppObj.allowedOverBookingSlots>currentAppObj.slotPatientDetailsListsSize) 
																																    			||(currentAppObj.slotStatusId==1)){
																																    		
																																		  if(currentAppObj.allowedOverBookingSlots>currentAppObj.slotPatientDetailsListsSize 
																																    				&& obj.overBooking=='Y' && currentAppObj.slotPatientDetailsListsSize>0){
																																    			currentAppObj.slotStatusId=6;
																																    		}else{
																																    			currentAppObj.slotStatusId=4;
																																    		}
																																		  $scope.setCreatedSlotList.push(currentAppObj);
																																	    	
																																	  }
																																  }
																															}
																												        }
																												        
																																
																												}else{
																						                            // **********************Logic
																													// for
																													// preferred
																													// day
																													// selection*************


																								                      $scope.endDate = new Date($scope.currentDate.getTime()+60*60*1000);	
																														var addWithEveryRec,noOfOccurences,reqPerRecOcc,totalWeek,totalDuration;
																														$scope.addDays;
																														var occuType=(obj.occurencesType=="d" || obj.occurencesType==""?1:obj.occurencesType=="w"?7:30);
																														var durationType=(obj.durationType=="d" || obj.occurencesType==""?1:(obj.durationType=="w"?7:30));	
																														if(obj.every!=undefined && obj.every>0 && obj.occurences!=undefined && obj.occurences>0
																																	&&(obj.duration==undefined || obj.duration==0 || obj.duration=="")){
																																 totalDuration=Math.round((obj.every*occuType*obj.sessions)/obj.occurences);
																																  addWithEveryRec=obj.every*occuType;	
																																  noOfOccurences=Number(obj.occurences);
																																  reqPerRecOcc=noOfOccurences;
																																  $scope.addDays=Math.round(addWithEveryRec/(reqPerRecOcc==0?1:reqPerRecOcc));
																														}else if(obj.every!=undefined && obj.every>0 && obj.occurences!=undefined && obj.occurences>0){
																														  totalDuration=Number(obj.duration)*durationType;	
																														  addWithEveryRec=obj.every*occuType;	
																														  noOfOccurences=Number(obj.occurences);
																														  reqPerRecOcc=noOfOccurences;
																														  $scope.addDays=Math.floor(addWithEveryRec/(reqPerRecOcc==0?1:reqPerRecOcc));
																														}else if(obj.duration!=undefined && obj.duration>0){
																														       totalDuration=Number(obj.duration)*durationType;
																														       noOfOccurences=Number(obj.sessions);
																															   addWithEveryRec=7; 
																															   var totalWeek=Math.ceil(totalDuration/7);
																															   reqPerRecOcc=Math.floor(noOfOccurences/totalWeek);
																															   $scope.addDays=Math.floor(totalDuration/noOfOccurences);
																														}else{
																															   totalDuration=Number(obj.sessions);
																															   addWithEveryRec=1;
																															   noOfOccurences=Number(obj.sessions);
																															   reqPerRecOcc=1;
																															   $scope.addDays=1
																														}
																														 $scope.lastDate=new Date($scope.endDate.getTime()+(totalDuration-1)*24*60*60*1000);
																														 
																														if($scope.addDays==0){
																															$scope.addDays=1;
																														}
																														if(reqPerRecOcc==0){
																															reqPerRecOcc=1;
																														}
																														
																														// $scope.everyWeekFirstDateCounter=angular.copy($scope.endDate);
																														$scope.firstRecDate=$scope.endDate;
																														 var backOneDate = new Date($scope.endDate.getTime()-1*24*60*60*1000);
																														// for
																														// recurrence
																														// criteria
																														$scope.lastRecDate = new Date(backOneDate.getTime() + addWithEveryRec*24*60*60*1000);
																														$scope.currPerReqTakenCount=0;
																														// first
																														// week
																														// last
																														// date
																														// $scope.lastDateofCurrWeek=new
																														// Date(backOneDate.getTime()
																														// +
																														// 7*24*60*60*1000)
																														$scope.currentIterationDate=backOneDate;				  
																												
																														$scope.tempDate=angular.copy(new Date($scope.currentIterationDate.getTime() +1*24*60*60*1000));
																												        var flagStop=0,i=0,t,j=0,a,b,flagForLastDateApp=false,counterForApp=0,remainingOcc=0;									
																										for(i=$scope.setCreatedSlotList.length;i<=obj.sessions; i++){
																														if(i==0){
																															 $scope.currentIterationDate = new Date($scope.currentIterationDate.getTime() +1*24*60*60*1000);
																															 $scope.tempDate = angular.copy($scope.currentIterationDate);
																														 }
																															var flagObj=false;
																															if(($scope.cyclic.dayM1 == "Y" && $scope.currentIterationDate.getDay()==1) || ($scope.cyclic.dayM2 == "Y" && $scope.currentIterationDate.getDay()==2) || ($scope.cyclic.dayM3 == "Y" && $scope.currentIterationDate.getDay()==3)
																											    					|| ($scope.cyclic.dayM4 == "Y" && $scope.currentIterationDate.getDay()==4) || ($scope.cyclic.dayM5 == "Y" && $scope.currentIterationDate.getDay()==5) 
																											    					|| ($scope.cyclic.dayM6 == "Y" && $scope.currentIterationDate.getDay()==6) || ($scope.cyclic.dayM7 == "Y" && $scope.currentIterationDate.getDay()==0) 
																											    					||($scope.cyclic.dayE1 == "Y" && $scope.currentIterationDate.getDay()==1) || ($scope.cyclic.dayE2 == "Y" && $scope.currentIterationDate.getDay()==2) || ($scope.cyclic.dayE3 == "Y" && $scope.currentIterationDate.getDay()==3)
																											    					|| ($scope.cyclic.dayE4 == "Y" && $scope.currentIterationDate.getDay()==4) || ($scope.cyclic.dayE5 == "Y" && $scope.currentIterationDate.getDay()==5) 
																											    					|| ($scope.cyclic.dayE6 == "Y" && $scope.currentIterationDate.getDay()==6) || ($scope.cyclic.dayE7 == "Y" && $scope.currentIterationDate.getDay()==0)){
																															      flagObj=true;
																											    			}
																															
																													// main
																													// loop
																													// break
																													// if
																													// condition
																													// meets
																													if(obj.sessions==$scope.setCreatedSlotList.length){
																														break;
																													}	
																													
																														if((moment($scope.currentIterationDate).format('YYYY-MM-DD')<=moment($scope.lastRecDate).format('YYYY-MM-DD'))){
																															// for
																															// morning
																															// selection
																															// preference
																															var currentAppObj;
																															var flagCurrenMorTakenCount=0;
																															for(j=0;j<$scope.slotDetailsList.length;j++){
																																if($scope.currPerReqTakenCount==reqPerRecOcc){
																																	break;
																																}
																																currentAppObj=$scope.slotDetailsList[j];
																																  if(moment($scope.currentIterationDate).format('YYYY-MM-DD') === moment(currentAppObj.appointmentDate2).format('YYYY-MM-DD') 
																																    		&& (moment($scope.currentIterationDate).format('YYYY-MM-DD')== moment(new Date()).format('YYYY-MM-DD') 
																																    				? (currentAppObj.originalFromTime>$scope.currentTime ? true:false):true)
																																    		&& (currentAppObj.originalFromTime>$scope.docMorningStartTime && currentAppObj.originalFromTime<$scope.docMorningEndTime)){ 
																																	  if((obj.overBooking=='Y' && currentAppObj.allowedOverBookingSlots>currentAppObj.slotPatientDetailsListsSize) 
																																    			||(currentAppObj.slotStatusId==1)){
																																    		if(currentAppObj.allowedOverBookingSlots>currentAppObj.slotPatientDetailsListsSize 
																																    				&& obj.overBooking=='Y' && currentAppObj.slotPatientDetailsListsSize>0){
																																    			currentAppObj.slotStatusId=6;
																																    		}else{
																																    			currentAppObj.slotStatusId=4;
																																    		}
																																    		
																																    		if(obj.dayM1 == "Y"){
																															    				if(currentAppObj.day == "Monday"){	
																																		    			$scope.setCreatedSlotList.push(currentAppObj);
																																		    			flagCurrenMorTakenCount=1;
																																		    			break;
																																	    			}
																															    		    }
																																    		
																																    		if(obj.dayM2 == "Y"){
																																    			if(currentAppObj.day == "Tuesday"){	
																																    			$scope.setCreatedSlotList.push(currentAppObj);
																																    			flagCurrenMorTakenCount=1;
																																    			break;
																																    			}
																																    		}
																																    		if(obj.dayM3 == "Y"){
																																    			if(currentAppObj.day == "Wednesday"){
																																    				$scope.setCreatedSlotList.push(currentAppObj);
																																    				flagCurrenMorTakenCount=1;
																																	    			break;	
																																    			}	
																																    		}
																																    		if(obj.dayM4 == "Y"){
																																    			if(currentAppObj.day == "Thursday"){
																																    				$scope.setCreatedSlotList.push(currentAppObj);
																																    				flagCurrenMorTakenCount=1;
																																	    			break;	
																																    			}	
																																    		}
																																    		if(obj.dayM5 == "Y"){
																																    			if(currentAppObj.day == "Friday"){	
																																    			$scope.setCreatedSlotList.push(currentAppObj);
																																    			flagCurrenMorTakenCount=1;
																																    			break;
																																    			}
																																    		}
																																    		if(obj.dayM6 == "Y"){
																																    			if(currentAppObj.day == "Saturday"){	
																																    			$scope.setCreatedSlotList.push(currentAppObj);
																																    			flagCurrenMorTakenCount=1;
																																    			break;
																																    			}
																																    		}
																																    		if(obj.dayM7 == "Y"){
																																    			if(currentAppObj.day == "Sunday"){	
																																    			$scope.setCreatedSlotList.push(currentAppObj);
																																    			flagCurrenMorTakenCount=1;
																																    			break;
																																    			}
																																    		}
																														
																																	  }
																																  }  
																															}
																															$scope.currPerReqTakenCount=$scope.currPerReqTakenCount+flagCurrenMorTakenCount;
																															// morning
																															// check
																															// end
																															// evening
																															// preference
																															var flagEveCurTakenCount=0;
																															var currentAppObj;
																															for(j=0;j<$scope.slotDetailsList.length;j++){
																																if($scope.currPerReqTakenCount==reqPerRecOcc){
																																	break;
																																}
																																currentAppObj=$scope.slotDetailsList[j];
																																/*
																																 * if(($scope.setCreatedSlotList.length==0?1:$scope.setCreatedSlotList.length)%perEveryOcc==0){
																																 * break; }
																																 */
																																  if(moment($scope.currentIterationDate).format('YYYY-MM-DD') === moment(currentAppObj.appointmentDate2).format('YYYY-MM-DD') 
																																    		&& (moment($scope.currentIterationDate).format('YYYY-MM-DD')== moment(new Date()).format('YYYY-MM-DD') 
																																    				? (currentAppObj.originalFromTime>$scope.currentTime ? true:false):true)
																																    		&& currentAppObj.originalFromTime>$scope.docEveningStartTime && (currentAppObj.originalFromTime<$scope.docEveningEndTime)){ 
																																	  
																																	  if((obj.overBooking=='Y' && currentAppObj.allowedOverBookingSlots>currentAppObj.slotPatientDetailsListsSize) 
																																    			||(currentAppObj.slotStatusId==1)){
																																    		
																																		  if(currentAppObj.allowedOverBookingSlots>currentAppObj.slotPatientDetailsListsSize 
																																    				&& obj.overBooking=='Y' && currentAppObj.slotPatientDetailsListsSize>0){
																																    			currentAppObj.slotStatusId=6;
																																    		}else{
																																    			currentAppObj.slotStatusId=4;
																																    		}
																																    		
																																    		if(obj.dayE1 == "Y"){
																															    				if(currentAppObj.day == "Monday"){	
																																		    			$scope.setCreatedSlotList.push(currentAppObj);
																																		    			flagEveCurTakenCount=1;
																																		    			break;
																																	    			}
																															    		    }
																															    		
																															    		if(obj.dayE2 == "Y"){
																															    			if(currentAppObj.day == "Tuesday"){	
																															    			$scope.setCreatedSlotList.push(currentAppObj);
																															    			flagEveCurTakenCount=1;
																															    			break;
																															    			}
																															    		}
																															    		if(obj.dayE3 == "Y"){
																															    			if(currentAppObj.day == "Wednesday"){
																															    				$scope.setCreatedSlotList.push(currentAppObj);
																															    				flagEveCurTakenCount=1;
																																    			break;	
																															    			}	
																															    		}
																															    		if(obj.dayE4 == "Y"){
																															    			if(currentAppObj.day == "Thursday"){
																															    				$scope.setCreatedSlotList.push(currentAppObj);
																															    				flagEveCurTakenCount=1;
																																    			break;	
																															    			}	
																															    		}
																															    		if(obj.dayE5 == "Y"){
																															    			if(currentAppObj.day == "Friday"){	
																															    			$scope.setCreatedSlotList.push(currentAppObj);
																															    			flagEveCurTakenCount=1;
																															    			break;
																															    			}
																															    		}
																															    		if(obj.dayE6 == "Y"){
																															    			if(currentAppObj.day == "Saturday"){	
																															    			$scope.setCreatedSlotList.push(currentAppObj);
																															    			flagEveCurTakenCount=1;
																															    			break;
																															    			}
																															    		}
																															    		if(obj.dayE7 == "Y"){
																															    			if(currentAppObj.day == "Sunday"){	
																															    			$scope.setCreatedSlotList.push(currentAppObj);
																															    			flagEveCurTakenCount=1;
																															    			break;
																															    			}
																															    		}
																																	  }
																																  }
																															}	
																															$scope.currPerReqTakenCount=$scope.currPerReqTakenCount+flagEveCurTakenCount;
																													}
																														
																														
																														if(remainingOcc>0 && flagForLastDateApp==true){
																														// if
																														// current
																														// recurrence
																														// iteration
																														// last
																														// day
																														// meet
																															var lastDateAppCurrRecc=$scope.setCreatedSlotList[$scope.setCreatedSlotList.length-1].appointmentDate2;
																															var lastDateAppDummyObj=$scope.setCreatedSlotList[$scope.setCreatedSlotList.length-1];
																															for(b=1;b<=remainingOcc;b++){
																																if($scope.setCreatedSlotList.length==obj.sessions){
																																	break;
																																}
																																if($scope.currPerReqTakenCount==reqPerRecOcc){
																																	break;
																																}
																															// evening
																															// preference
																															// check
																															var flagEveCurTakenCount=0;	
																															var currentAppObj;
																															for(j=0;j<$scope.slotDetailsList.length;j++){
																																if($scope.setCreatedSlotList.length==obj.sessions){
																																	break;
																																}
																																if($scope.currPerReqTakenCount==reqPerRecOcc){
																																	break;
																																}
																																currentAppObj=$scope.slotDetailsList[j];
																																  if(moment(lastDateAppCurrRecc).format('YYYY-MM-DD') === moment(currentAppObj.appointmentDate2).format('YYYY-MM-DD') 
																																		  && (lastDateAppDummyObj.fromTimeString!=currentAppObj.fromTimeString)
																																    		&& (moment($scope.currentIterationDate).format('YYYY-MM-DD')== moment(new Date()).format('YYYY-MM-DD') 
																																    				? (currentAppObj.originalFromTime>$scope.currentTime ? true:false):true)
																																    		&& currentAppObj.originalFromTime>$scope.docEveningStartTime && (currentAppObj.originalFromTime<$scope.docEveningEndTime)){ 
																																	  
																																	  if((obj.overBooking=='Y' && currentAppObj.allowedOverBookingSlots>currentAppObj.slotPatientDetailsListsSize) 
																																    			||(currentAppObj.slotStatusId==1)){
																																    		
																																		  if(currentAppObj.allowedOverBookingSlots>currentAppObj.slotPatientDetailsListsSize 
																																    				&& obj.overBooking=='Y' && currentAppObj.slotPatientDetailsListsSize>0){
																																    			currentAppObj.slotStatusId=6;
																																    		}else{
																																    			currentAppObj.slotStatusId=4;
																																    		}
																																    		
																																    		if(obj.dayE1 == "Y"){
																															    				if(currentAppObj.day == "Monday"){	
																																		    			$scope.setCreatedSlotList.push(currentAppObj);
																																		    			flagEveCurTakenCount=1;
																																		    			break;
																																	    			}
																															    		    }
																															    		
																															    		if(obj.dayE2 == "Y"){
																															    			if(currentAppObj.day == "Tuesday"){	
																															    			$scope.setCreatedSlotList.push(currentAppObj);
																															    			flagEveCurTakenCount=1;
																															    			break;
																															    			}
																															    		}
																															    		if(obj.dayE3 == "Y"){
																															    			if(currentAppObj.day == "Wednesday"){
																															    				$scope.setCreatedSlotList.push(currentAppObj);
																															    				flagEveCurTakenCount=1;
																															    				break;	
																															    			}	
																															    		}
																															    		if(obj.dayE4 == "Y"){
																															    			if(currentAppObj.day == "Thursday"){
																															    				$scope.setCreatedSlotList.push(currentAppObj);
																															    				flagEveCurTakenCount=1;
																															    				break;	
																															    			}	
																															    		}
																															    		if(obj.dayE5 == "Y"){
																															    			if(currentAppObj.day == "Friday"){	
																															    			$scope.setCreatedSlotList.push(currentAppObj);
																															    			flagEveCurTakenCount=1;
																															    			break;
																															    			}
																															    		}
																															    		if(obj.dayE6 == "Y"){
																															    			if(currentAppObj.day == "Saturday"){	
																															    			$scope.setCreatedSlotList.push(currentAppObj);
																															    			flagEveCurTakenCount=1;
																															    			break;
																															    			}
																															    		}
																															    		if(obj.dayE7 == "Y"){
																															    			if(currentAppObj.day == "Sunday"){	
																															    			$scope.setCreatedSlotList.push(currentAppObj);
																															    			flagEveCurTakenCount=1;
																															    			break;
																															    			}
																															    		}
																																	  }
																																  }
																															}
																															$scope.currPerReqTakenCount=$scope.currPerReqTakenCount+flagEveCurTakenCount;
																															
																															// for
																															// morning
																															// selection
																															// preference
																															var flagCurrenMorTakenCount=0;
																															var currentAppObj;
																															for(j=0;j<$scope.slotDetailsList.length;j++){
																																if($scope.setCreatedSlotList.length==obj.sessions){
																																	break;
																																}
																																if($scope.currPerReqTakenCount==reqPerRecOcc){
																																	break;
																																}
																																currentAppObj=$scope.slotDetailsList[j];
																																  if(moment(lastDateAppCurrRecc).format('YYYY-MM-DD') === moment(currentAppObj.appointmentDate2).format('YYYY-MM-DD') 
																																		  && (lastDateAppDummyObj.fromTimeString!=currentAppObj.fromTimeString)
																																    		&& (moment($scope.currentIterationDate).format('YYYY-MM-DD')== moment(new Date()).format('YYYY-MM-DD') 
																																    				? (currentAppObj.originalFromTime>$scope.currentTime ? true:false):true)
																																    		&& (currentAppObj.originalFromTime>$scope.docMorningStartTime && currentAppObj.originalFromTime<$scope.docMorningEndTime)){ 
																																	  if((obj.overBooking=='Y' && currentAppObj.allowedOverBookingSlots>currentAppObj.slotPatientDetailsListsSize) 
																																    			||(currentAppObj.slotStatusId==1)){
																																    		if(currentAppObj.allowedOverBookingSlots>currentAppObj.slotPatientDetailsListsSize 
																																    				&& obj.overBooking=='Y' && currentAppObj.slotPatientDetailsListsSize>0){
																																    			currentAppObj.slotStatusId=6;
																																    		}else{
																																    			currentAppObj.slotStatusId=4;
																																    		}
																																    		
																																    		if(obj.dayM1 == "Y"){
																															    				if(currentAppObj.day == "Monday"){	
																																		    			$scope.setCreatedSlotList.push(currentAppObj);
																																		    			flagCurrenMorTakenCount=1;
																																		    			break;
																																	    			}
																															    		    }
																																    		
																																    		if(obj.dayM2 == "Y"){
																																    			if(currentAppObj.day == "Tuesday"){	
																																    			$scope.setCreatedSlotList.push(currentAppObj);
																																    			flagCurrenMorTakenCount=1;
																																    			break;
																																    			}
																																    		}
																																    		if(obj.dayM3 == "Y"){
																																    			if(currentAppObj.day == "Wednesday"){
																																    				$scope.setCreatedSlotList.push(currentAppObj);
																																    				flagCurrenMorTakenCount=1;
																																    				break;	
																																    			}	
																																    		}
																																    		if(obj.dayM4 == "Y"){
																																    			if(currentAppObj.day == "Thursday"){
																																    				$scope.setCreatedSlotList.push(currentAppObj);
																																    				flagCurrenMorTakenCount=1;
																																    				break;	
																																    			}	
																																    		}
																																    		if(obj.dayM5 == "Y"){
																																    			if(currentAppObj.day == "Friday"){	
																																    			$scope.setCreatedSlotList.push(currentAppObj);
																																    			flagCurrenMorTakenCount=1;
																																    			break;
																																    			}
																																    		}
																																    		if(obj.dayM6 == "Y"){
																																    			if(currentAppObj.day == "Saturday"){	
																																    			$scope.setCreatedSlotList.push(currentAppObj);
																																    			flagCurrenMorTakenCount=1;
																																    			break;
																																    			}
																																    		}
																																    		if(obj.dayM7 == "Y"){
																																    			if(currentAppObj.day == "Sunday"){	
																																    			$scope.setCreatedSlotList.push(currentAppObj);
																																    			flagCurrenMorTakenCount=1;
																																    			break;
																																    			}
																																    		}
																														
																																	  }
																																  }  
																																	  
																															}// end
																																// of
																																// morning
																																// loop
																															$scope.currPerReqTakenCount=$scope.currPerReqTakenCount+flagCurrenMorTakenCount;
																															
																															
																														 }
																														}	
																											
																												
																													 var appAvail=false;
																													 if(counterForApp!=$scope.setCreatedSlotList.length){
																												    	   counterForApp = angular.copy($scope.setCreatedSlotList.length);  
																												    	   appAvail=true;
																												       }		
																														
																												       
																												       flagForLastDateApp=false,remainingOcc=0;
																														// next
																														// recurence
																														if($scope.setCreatedSlotList.length % reqPerRecOcc==0 && $scope.currPerReqTakenCount>0){
																															var backDate=new Date($scope.lastRecDate.getTime());
																															
																															/*
																															 * if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')){
																															 * $scope.currentIterationDate=new
																															 * Date($scope.tempDate.getTime() +
																															 * ($scope.addDays)*24*60*60*1000);
																															 * $scope.tempDate =
																															 * angular.copy($scope.currentIterationDate); }
																															 */
																															if(moment($scope.currentIterationDate).format('YYYY-MM-DD')<=moment($scope.lastRecDate).format('YYYY-MM-DD')){
																																$scope.currentIterationDate=new Date(backDate.getTime()+1*24*60*60*1000);
																																$scope.tempDate = angular.copy($scope.currentIterationDate);
																															}
																											    			 $scope.firstRecDate=new Date($scope.lastRecDate.getTime()+1*24*60*60*1000);
																											    			 $scope.lastRecDate=new Date($scope.firstRecDate.getTime() + (addWithEveryRec-1)*24*60*60*1000);
																											    			 $scope.currPerReqTakenCount=0;
																											    			   if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastDate).format('YYYY-MM-DD')){
																																	break;
																																}
																														}else if(appAvail==false && $scope.addDays>1 ){
																															var flagObj=false;
																															for(a=1;a<=$scope.addDays;a++){
																																if(a%2==1){
																																	$scope.currentIterationDate=new Date($scope.tempDate.getTime()-(a==1?1:Math.round(a/2))*24*60*60*1000);
																																}else{
																																	$scope.currentIterationDate=new Date($scope.tempDate.getTime()+(a==2?1:a/2)*24*60*60*1000);	
																																}
																																if(moment($scope.currentIterationDate).format('YYYY-MM-DD')<moment($scope.firstRecDate).format('YYYY-MM-DD')){
																																	continue;
																																}
																																
																																if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')){
																																	continue;
																																}
																																
																																var flagForSelAvail=false;
																										    					if((obj.dayM1 == "Y" && $scope.currentIterationDate.getDay()==1) || (obj.dayM2 == "Y" && $scope.currentIterationDate.getDay()==2) || (obj.dayM3 == "Y" && $scope.currentIterationDate.getDay()==3)
																												    					|| (obj.dayM4 == "Y" && $scope.currentIterationDate.getDay()==4) || (obj.dayM5 == "Y" && $scope.currentIterationDate.getDay()==5) 
																												    					|| (obj.dayM6 == "Y" && $scope.currentIterationDate.getDay()==6) || (obj.dayM7 == "Y" && $scope.currentIterationDate.getDay()==0)
																												    					|| (obj.dayE1 == "Y" && $scope.currentIterationDate.getDay()==1) || (obj.dayE2 == "Y" && $scope.currentIterationDate.getDay()==2) || (obj.dayE3 == "Y" && $scope.currentIterationDate.getDay()==3)
																												    					|| (obj.dayE4 == "Y" && $scope.currentIterationDate.getDay()==4) || (obj.dayE5 == "Y" && $scope.currentIterationDate.getDay()==5) 
																												    					|| (obj.dayE6 == "Y" && $scope.currentIterationDate.getDay()==6) || (obj.dayE7 == "Y" && $scope.currentIterationDate.getDay()==0)){
																										    						flagForSelAvail=true;
																												    			}	
																																
																										    					var list=$.grep($scope.slotDateList, function (obj) {
																															        return obj === moment($scope.currentIterationDate).format('DD-MM-YYYY');
																															    });
																																
																																if(list.length>0 && flagForSelAvail==true){
																																      flagObj=true;
																												    			}
																																
																																if(flagObj==true){
																																	$scope.tempDate = angular.copy($scope.currentIterationDate);
																																	break;
																																}
																															}
																															
																															if(moment($scope.tempDate).format('YYYY-MM-DD')>moment($scope.lastDate).format('YYYY-MM-DD')){
																																break;
																															}
																															if(flagObj==false){
																																if(moment($scope.tempDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')
																																		&& $scope.currPerReqTakenCount>0){
																																	 remainingOcc=reqPerRecOcc-$scope.currPerReqTakenCount;
																																	flagForLastDateApp=true;
																																}else{
																																	if(moment($scope.tempDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')){
																																		  var backDate=new Date($scope.lastRecDate.getTime());
																																		$scope.currentIterationDate=new Date(backDate.getTime()+1*24*60*60*1000);
																																		$scope.tempDate = angular.copy($scope.currentIterationDate);
																														    			 $scope.firstRecDate=new Date($scope.lastRecDate.getTime()+1*24*60*60*1000);
																														    			 $scope.lastRecDate=new Date($scope.firstRecDate.getTime() + (addWithEveryRec-1)*24*60*60*1000);
																														    			 $scope.currPerReqTakenCount=0;
																																	}else{
																																		$scope.currentIterationDate=new Date($scope.tempDate.getTime() + ($scope.addDays)*24*60*60*1000);
																																		$scope.tempDate = angular.copy($scope.currentIterationDate);
																																	}
																																}
																															}
																														}else{
																														   $scope.currentIterationDate=new Date($scope.currentIterationDate.getTime() + ($scope.addDays)*24*60*60*1000);
																														   $scope.tempDate = angular.copy($scope.currentIterationDate);
																														   if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastRecDate).format('YYYY-MM-DD')){
																															        if($scope.currPerReqTakenCount>0){
																																				 remainingOcc=reqPerRecOcc-$scope.currPerReqTakenCount;
																																				flagForLastDateApp=true;
																																	}else{
																																		  var backDate=new Date($scope.lastRecDate.getTime());
																																		 $scope.currentIterationDate=new Date(backDate.getTime()+1*24*60*60*1000);
																																		 $scope.tempDate = angular.copy($scope.currentIterationDate);
																														    			 $scope.firstRecDate=new Date($scope.lastRecDate.getTime()+1*24*60*60*1000);
																														    			 $scope.lastRecDate=new Date($scope.firstRecDate.getTime() + (addWithEveryRec-1)*24*60*60*1000);
																														    			 $scope.currPerReqTakenCount=0;
																																}
																															}
																														   if(moment($scope.currentIterationDate).format('YYYY-MM-DD')>moment($scope.lastDate).format('YYYY-MM-DD')){
																																break;
																															}
																														}
																													// if
																													// no
																													// slots
																													// added
																													// then
																													// keep
																													// counter
																													// as
																													// previous
																													 i=$scope.setCreatedSlotList.length==0?0:($scope.setCreatedSlotList.length-1);
																												}// end
																													// of
																													// main
																													// loop
																							        
																							// ************************place
																							// all
																							// appointment
																							// on
																							// last
																							// date*************************
																							      
																							        if(obj.sessions!=$scope.setCreatedSlotList.length){
																										// if
																										// current
																										// recurrence
																										// iteration
																										// last
																										// day
																										// meet
																							        	 if($scope.setCreatedSlotList.length==0){
																											 growl.warning(
																														'Provided conditions are incorrect',
																														{
																															title : 'Warning!'
																														});
																											return false; 
																										 }
																										var lastDateAppCurrRecc=$scope.setCreatedSlotList[$scope.setCreatedSlotList.length-1].appointmentDate2;
																										var lastDateAppDummyObj=$scope.setCreatedSlotList[$scope.setCreatedSlotList.length-1];
																										// evening
																										// preference
																										// check
																										var currentAppObj;
																										for(j=0;j<$scope.slotDetailsList.length;j++){
																												if($scope.setCreatedSlotList.length==obj.sessions){
																													break;
																												}
																											
																											currentAppObj=$scope.slotDetailsList[j];
																											  if(moment(lastDateAppCurrRecc).format('YYYY-MM-DD') === moment(currentAppObj.appointmentDate2).format('YYYY-MM-DD') 
																													  && (lastDateAppDummyObj.fromTimeString!=currentAppObj.fromTimeString)
																											    		&& (moment($scope.currentIterationDate).format('YYYY-MM-DD')== moment(new Date()).format('YYYY-MM-DD') 
																											    				? (currentAppObj.originalFromTime>$scope.currentTime ? true:false):true)
																											    		&& currentAppObj.originalFromTime>$scope.docEveningStartTime && (currentAppObj.originalFromTime<$scope.docEveningEndTime)){ 
																												  
																												  if((obj.overBooking=='Y' && currentAppObj.allowedOverBookingSlots>currentAppObj.slotPatientDetailsListsSize) 
																											    			||(currentAppObj.slotStatusId==1)){
																											    		
																													  if(currentAppObj.allowedOverBookingSlots>currentAppObj.slotPatientDetailsListsSize 
																											    				&& obj.overBooking=='Y' && currentAppObj.slotPatientDetailsListsSize>0){
																											    			currentAppObj.slotStatusId=6;
																											    		}else{
																											    			currentAppObj.slotStatusId=4;
																											    		}
																											    		
																											    		if(obj.dayE1 == "Y"){
																										    				if(currentAppObj.day == "Monday"){	
																													    			$scope.setCreatedSlotList.push(currentAppObj);
																												    			}
																										    		    }
																										    		
																										    		if(obj.dayE2 == "Y"){
																										    			if(currentAppObj.day == "Tuesday"){	
																										    			$scope.setCreatedSlotList.push(currentAppObj);
																										    			}
																										    		}
																										    		if(obj.dayE3 == "Y"){
																										    			if(currentAppObj.day == "Wednesday"){
																										    				$scope.setCreatedSlotList.push(currentAppObj);
																										    			}	
																										    		}
																										    		if(obj.dayE4 == "Y"){
																										    			if(currentAppObj.day == "Thursday"){
																										    				$scope.setCreatedSlotList.push(currentAppObj);
																										    			}	
																										    		}
																										    		if(obj.dayE5 == "Y"){
																										    			if(currentAppObj.day == "Friday"){	
																										    			$scope.setCreatedSlotList.push(currentAppObj);
																										    			}
																										    		}
																										    		if(obj.dayE6 == "Y"){
																										    			if(currentAppObj.day == "Saturday"){	
																										    			$scope.setCreatedSlotList.push(currentAppObj);
																										    			}
																										    		}
																										    		if(obj.dayE7 == "Y"){
																										    			if(currentAppObj.day == "Sunday"){	
																										    			$scope.setCreatedSlotList.push(currentAppObj);
																										    			}
																										    		}
																												  }
																											  }
																										}
																									
																										
																										// for
																										// morning
																										// selection
																										// preference
																										var currentAppObj;
																										for(j=0;j<$scope.slotDetailsList.length;j++){
																												// main
																												// loop
																												// check
																												// break
																												// if
																												// condition
																												// meets
																												if($scope.setCreatedSlotList.length==obj.sessions){
																													break;
																												}
																											
																											currentAppObj=$scope.slotDetailsList[j];
																											  if(moment(lastDateAppCurrRecc).format('YYYY-MM-DD') === moment(currentAppObj.appointmentDate2).format('YYYY-MM-DD') 
																													  && (lastDateAppDummyObj.fromTimeString!=currentAppObj.fromTimeString)
																											    		&& (moment($scope.currentIterationDate).format('YYYY-MM-DD')== moment(new Date()).format('YYYY-MM-DD') 
																											    				? (currentAppObj.originalFromTime>$scope.currentTime ? true:false):true)
																											    		&& (currentAppObj.originalFromTime>$scope.docMorningStartTime && currentAppObj.originalFromTime<$scope.docMorningEndTime)){ 
																												  if((obj.overBooking=='Y' && currentAppObj.allowedOverBookingSlots>currentAppObj.slotPatientDetailsListsSize) 
																											    			||(currentAppObj.slotStatusId==1)){
																											    		if(currentAppObj.allowedOverBookingSlots>currentAppObj.slotPatientDetailsListsSize 
																											    				&& obj.overBooking=='Y' && currentAppObj.slotPatientDetailsListsSize>0){
																											    			currentAppObj.slotStatusId=6;
																											    		}else{
																											    			currentAppObj.slotStatusId=4;
																											    		}
																											    		
																											    		if(obj.dayM1 == "Y"){
																										    				if(currentAppObj.day == "Monday"){	
																													    			$scope.setCreatedSlotList.push(currentAppObj);
																												    			}
																										    		    }
																											    		
																											    		if(obj.dayM2 == "Y"){
																											    			if(currentAppObj.day == "Tuesday"){	
																											    			$scope.setCreatedSlotList.push(currentAppObj);
																											    			}
																											    		}
																											    		if(obj.dayM3 == "Y"){
																											    			if(currentAppObj.day == "Wednesday"){
																											    				$scope.setCreatedSlotList.push(currentAppObj);
																											    			}	
																											    		}
																											    		if(obj.dayM4 == "Y"){
																											    			if(currentAppObj.day == "Thursday"){
																											    				$scope.setCreatedSlotList.push(currentAppObj);
																											    			}	
																											    		}
																											    		if(obj.dayM5 == "Y"){
																											    			if(currentAppObj.day == "Friday"){	
																											    			$scope.setCreatedSlotList.push(currentAppObj);
																											    			}
																											    		}
																											    		if(obj.dayM6 == "Y"){
																											    			if(currentAppObj.day == "Saturday"){	
																											    			$scope.setCreatedSlotList.push(currentAppObj);
																											    			}
																											    		}
																											    		if(obj.dayM7 == "Y"){
																											    			if(currentAppObj.day == "Sunday"){	
																											    			$scope.setCreatedSlotList.push(currentAppObj);
																											    			}
																											    		}
																									
																												  }
																											  }  
																												  
																										}// end
																											// of
																											// morning
																											// loop
																							      }  
																							  }
																							// ****************
																							// Logic
																							// for
																							// preferred
																							// day
																							// selection
																							// end***********************
																													
																							$scope.stopLoader();

																							console.log($scope.setCreatedSlotList);
																				}
											$('#showDoctorModal').modal();
										});
													
													
													
								if(obj.specialityId != 0 || obj.specialityId != undefined){
									angular.forEach($scope.specialityList,function(value,key)
											{	if(value.specialityId == obj.specialityId)
										  			{$scope.cyclic.specialityName = value.specialityName;
										  			// $scope.cyclic.specialityId =
													// value.specialityId;
										  			}
											});
								}
								
								if(obj.doctorId != 0 || obj.doctorId != undefined){
									angular.forEach($scope.doctorList,function(value,key)
											{	if(value.doctorId == obj.doctorId)
										  			{$scope.cyclic.docName = value.doctorName;
										  			// $scope.cyclic.doctorId =
													// value.doctorId;
										  			}
											});
								}
													
					}
										
										$scope.showServAppNoRecFound='N';
										$scope.showAppNoRecFound='N';
										// get patient service appointment
										$scope.getServiceAppointmentByPatientId=function(){
											$scope.showServAppNoRecFound='N';
											var URI = BASE_URL + ROOT_URL + "/api/global/patient/billingCyclic/"+$scope.encounterDetailsObj.patientId;
											 BillingGenericService
												.serviceAction(METHOD_GET, URI, null,
														NOTIFICATION_MSG_STATUS_FALSE)
												.then(
														function(response) {
															$scope.serviceAppointmentList=[];
															if (response.data.status == "success"){
																$scope.serviceAppointmentList=response.data.listObject;
																console.log("$scope.serviceAppointmentList",$scope.serviceAppointmentList);
																// $scope.cyclic.mobileNumber
																// =
																// response.data.listObject[0].mobileNumber;
																// $scope.cyclic.email =
																// response.data.listObject[0].email;
															}
															if($scope.serviceAppointmentList.length==0){
																$scope.showServAppNoRecFound='Y';
															}
														});
										      
										}
										$scope.getServiceAppointmentByPatientId();
										// get patient cyclic service appointment for
										// reschedule
										$scope.getPatientServiceAppointments=function(obj){
											$scope.patientCyclicServiceAppList = [];
											$scope.patientCyclicAppointmentSlotList=[];
											$scope.serviceSlotListForReschedule = [];
											$scope.appointmentSearchObject.appointmentTypeId = 1 ;
											$scope.appointmentSearchObject.patientId = $scope.patientDetails.patientId;
											$scope.appointmentSearchObject.serviceId =obj.serviceId;
											$scope.appointmentSearchObject.serviceStandardName =obj.serviceStandardName;

											// var URI = BASE_URL + ROOT_URL +
											// "/api/global/patient/billingCyclic/"+info.patientId;
											$scope.isAppointmentSchedule='N';
											var URI = BASE_URL + ROOT_URL + "/api/opd/searchCyclicServiceAppointmentsByPatienId"; 
											BillingGenericService
												.serviceAction(METHOD_POST, URI, $scope.appointmentSearchObject,
														NOTIFICATION_MSG_STATUS_FALSE)
												.then(
														function(response) {
															console.log(response);
															if (response.data.status == "success")
																angular.forEach(response.data.listObject,function(value,index){
																	value.appointmentDate=moment(value.appointmentDate,"DD/MM/YYYY").toDate();
																	$scope.serviceSlotListForReschedule.push(value);
																});
																
															    $scope.patientCyclicServiceAppList = angular.copy($scope.serviceSlotListForReschedule);
															 
														});
										
										}
										
										// reschedule cyclic service appointment
										$scope.rescheduleCyclicServiceAppointment=function(){
										$scope.rescheduleAppointmentSlotList=[];
										for(let i=0;i<$scope.serviceSlotListForReschedule.length;i++){
											 if(moment($scope.patientCyclicServiceAppList[i].appointmentDate).format('DD-MM-YYYY') != moment($scope.serviceSlotListForReschedule[i].appointmentDate).format('DD-MM-YYYY') 
													 || $scope.patientCyclicServiceAppList[i].appointmentTime != $scope.serviceSlotListForReschedule[i].appointmentTime ){ 
											
											var newArray={};
											newArray.organizationId = $scope.organizationId;
											newArray.unitId = $scope.unitId;
											newArray.createdBy = $scope.userId;
											newArray.updatedBy = $scope.userId;
											newArray.status = 'A';
											newArray.patientId = $scope.patientDetails.patientId;
											newArray.doctorId = $scope.encounterDetailsObj.doctorId;
											newArray.encounterId = $scope.encounterDetailsObj.encounterId;

											// $scope.serviceSlotListForReschedule[i].appointmentDate
											// =
											// moment($scope.datepicker).format('DD/MM/YYYY');
											newArray.appointmentDate = moment($scope.serviceSlotListForReschedule[i].appointmentDate).format('DD-MM-YYYY');
											newArray.cyclicOrdScheduledDate =  moment($scope.serviceSlotListForReschedule[i].appointmentDate).format('DD-MM-YYYY HH:mm:ss');
											newArray.serviceStandardName = $scope.patientCyclicServiceAppList[i].serviceCode;

											newArray.appointmentTime=$scope.serviceSlotListForReschedule[i].appointmentTime;
											newArray.appointmentId = $scope.patientCyclicServiceAppList[i].appointmentId;
											$scope.rescheduleAppointmentSlotList.push(newArray);
										 }
										}
										if($scope.rescheduleAppointmentSlotList.length==0){
											 growl.warning(
														'No change in existing appointment',
														{
															title : 'Warning!'
														});
											return false;
										}
										$rootScope.startLoader();
										var URI = BASE_URL + ROOT_URL +  "/api/opd/rescheduleCyclicServiceAppointment";
										// console.log(JSON.stringify($scope.setCreatedSlotList[i]));
										BillingGenericService.serviceAction(METHOD_POST, URI,$scope.rescheduleAppointmentSlotList,NOTIFICATION_MSG_STATUS_FALSE)
										.then(function(response) {
											console.log(response);
											if (response.data.status == "success") {
													growl.success("Appointments Updated Successfully",
															{
																title : 'Success!'
															});
											} 
											$rootScope.stopLoader();
											});
										
									}
										// ---------------------------------Cyclic Order
										// End------------------------------------------------//
										
								 /**************************Referal order start***************
								  * *************************************************************************************************************/
										
										$scope.getUnitsOfHosp = function() {
											var orgId = $scope.organizationId;
											var orgObj = {}
											orgObj["organizationId"] = orgId
											
											var URI = BASE_URL + ROOT_URL + UNITLIST
											
											GenericServiceParamHeader.serviceAction(METHOD_POST, URI, JSON.stringify(orgObj))
												.then(function(response) {
													if(response.data.status == "success"){
														$scope.unitList = response.data.listObject
													}
											})
										}
										$scope.getUnitsOfHosp();
										$scope.getDoctorsForDepartment = function() {
											var orgId = JSON.parse($scope.selectedValues.selectedHosp).organizationId
											var unitId = JSON.parse($scope.selectedValues.selectedUnit).unitId
											var deptId = JSON.parse($scope.selectedValues.selectedDept).departmentId
											
											var URI = BASE_URL + ROOT_URL + GETDOCTORS + "?orgId=" + 
											  orgId + "&unitId=" + unitId + "&departmentId=" + deptId
											
											  GenericServiceParamHeader.serviceAction(METHOD_GET, URI).then(function(response) {
												if(response.data.status == "success"){
													$scope.doctorsList = response.data.listObject
												}
											})
										}
									 
								        $scope.consultOrderList = function()
								        {
								        	var data = {
								        			encounterId:$scope.encounterDetailsObj.encounterId
								        	}
								        	
								        	var URI = BASE_URL + ROOT_URL + CONSULTDOCTORLISTBYENCOUNTER;
											
											GenericServiceParamHeader.serviceAction(METHOD_POST, URI,
													data, NOTIFICATION_MSG_STATUS_FALSE)
														.then(function(response) {
															if(response.data.status == "success"){
																$scope.consultOrdersList = response.data.listObject;
																console.log("consultOrdersList", $scope.consultOrdersList);
															}
											});
								        }

								        $scope.onChangeSearchType = function(type) {
											
											$scope.selectedValues.unitId ='';
											$scope.selectedValues.speciality ='';
											$scope.departmentList = [];
											$scope.doctorList = [];
											$scope.addedConsultOrderForDisplay = [];
											
											if (type == 'search') {
												$scope.searchType='search';
												$scope.selectedValues.unitId = $scope.unitId;
												$scope.getDepartmentsForHospAndUnit();
											} else if(type=='interHospital'){
												$scope.searchType='interHospital';
												$scope.getOutSideHospitalList();
											}else{
												$scope.searchType='globalSearch';
											}
											 $scope.consultOrderList();
											
										}
								        $scope.addedConsultOrderForDisplay = [];
										$scope.consultOrderFlagAdd=false;
										$scope.addConsultOrder = function(selectedValues) {
											$scope.consultOrderFlagAdd=true;
											console.log('dddd', $scope.selectedValues);
											if ($scope.searchType == 'globalSearch') {
												if ($scope.selectedValues.unitId == undefined || $scope.selectedValues.unitId == '') {
													growl.warning("Please select Unit..!",
															{
																title : 'Warning!'
															});
													return;
												} else if ($scope.selectedValues.specialityId == undefined || $scope.selectedValues.specialityId == '') {
													growl.warning(
															"Please select speciality..!",
															{
																title : 'Warning!'
															});
													return;
												} else if ($scope.selectedValues.doctorId == undefined || $scope.selectedValues.doctorId == '') {
													growl.warning(
															"Please select Doctor..!", {
																title : 'Warning!'
															});
													return;
												}else if ($scope.selectedValues.priority == undefined || $scope.selectedValues.priority == '') {
													growl.warning(
															"Please select priority..!", {
																title : 'Warning!'
															});
													return;
												}

											} else if ($scope.searchType == 'search') {
												if ($scope.selectedValues.specialityId == undefined || $scope.selectedValues.specialityId == '') {
													growl.warning(
															"Please select Speciality..!",
															{
																title : 'Warning!'
															});
													return;
												} else if ($scope.selectedValues.doctorId == undefined || $scope.selectedValues.doctorId == '') {
													growl.warning(
															"Please select Doctor..!", {
																title : 'Warning!'
															});
													return;
												}else if ( $scope.selectedValues.priority == undefined || $scope.selectedValues.priority == '') {
													growl.warning(
															"Please select priority..!", {
																title : 'Warning!'
															});
													return;
												}
												else if ($scope.selectedValues.doctorId == $scope.encounterDetailsObj.doctorId) {
													growl.warning(
															"You can not consult again to treating doctor..!", {
																title : 'Warning!'
															});
													return;
												}

											}else if($scope.searchType == "interHospital"){
												if ($scope.selectedValues.outsideHospitalId == undefined || $scope.selectedValues.outsideHospitalId == "") {
													growl.warning(
															"Please select hospital..!",
															{
																title : 'Warning!'
															});
													return;
												}
												
											}
											console.log('$scope.doctorList', $scope.doctorList);
											console.log('$scope.departmentList', $scope.departmentList);
											// return false;
											if($scope.selectedValues.doctorId!=undefined && $scope.selectedValues.doctorId!="" && $scope.selectedValues.doctorId!=null){
												var firstName = $
														.grep(
																$scope.doctorList,
																function(doc) {
																	return doc.doctorId == $scope.selectedValues.doctorId;
																})[0].firstName;
												$scope.firstName=firstName!=null && firstName!=undefined ? firstName:null ;
												var lastName = $
														.grep(
																$scope.doctorList,
																function(doc) {
																	return doc.doctorId == $scope.selectedValues.doctorId;
																})[0].lastName;
												var SpeName = $
														.grep(
																$scope.departmentList,
																function(doc) {
																	return doc.specialityId == $scope.selectedValues.specialityId;
																})[0].specialityName;
												
											}
											if($scope.selectedValues.outsideHospitalId!=undefined && $scope.selectedValues.outsideHospitalId!="" && $scope.selectedValues.outsideHospitalId!=null){
												var outsideHospitalDesc = $.grep($scope.outSideHospitalList,
														function(doc) {
															return doc.outsideHospitalId == $scope.selectedValues.outsideHospitalId;
														})[0].outsideHospitalDesc;
											}
											
											if($scope.selectedValues.outsideHosDocId!=undefined && $scope.selectedValues.outsideHosDocId!="" && $scope.selectedValues.outsideHosDocId!=null){
												var outsideHospitalDoctorName = $.grep($scope.outsideHospitalDoctorList,
														function(doc) {
															return doc.outsideHosDocId == $scope.selectedValues.outsideHosDocId;
														})[0].outsideHospitalDoctorName;
											}
											
											var consultOrderDisplayObj = {}
											consultOrderDisplayObj["patientId"] = $scope.encounterDetailsObj.patientId;
											consultOrderDisplayObj["patientName"] = $scope.encounterDetailsObj.patientName;
											consultOrderDisplayObj["refToSpecialityId"] = $scope.selectedValues.specialityId!="" ?$scope.selectedValues.specialityId :null;
											consultOrderDisplayObj["priorty"]=$scope.selectedValues.priorty!=undefined?$scope.selectedValues.priorty:null;
											consultOrderDisplayObj["refToDoctor"] =("Dr. "+ firstName!=undefined ? firstName:null+" "+lastName!=undefined?lastName:null)!=undefined?("Dr. "+ firstName!=undefined ? firstName:null+" "+lastName!=undefined?lastName:null):null;
											consultOrderDisplayObj["refToDoctorId"] = $scope.selectedValues.doctorId!="" ? $scope.selectedValues.doctorId :null;
											consultOrderDisplayObj["priority"]=$scope.selectedValues.priority!=undefined?$scope.selectedValues.priority:null;
											consultOrderDisplayObj["refByDoctor"] = $scope.encounterDetailsObj.doctorName!=undefined ?$scope.encounterDetailsObj.doctorName:null;
											consultOrderDisplayObj["refBySpecialityId"] = $scope.encounterDetailsObj.specialityId;
											consultOrderDisplayObj["specialityName"] = SpeName!=undefined ? SpeName:null;
											consultOrderDisplayObj["notes"] = "";
											consultOrderDisplayObj["organizationId"] = $scope.organizationId;
											consultOrderDisplayObj["unitId"] = $scope.unitId;
											consultOrderDisplayObj["refByDoctorId"] = $scope.encounterDetailsObj.doctorId;
											consultOrderDisplayObj["createdDate"] = moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
											consultOrderDisplayObj["updatedDate"] = moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
											consultOrderDisplayObj["createdBy"] = $scope.userId;
											consultOrderDisplayObj["updatedBy"] =$scope.userId;
											consultOrderDisplayObj["uhidNumber"] = $scope.encounterDetailsObj.uhidNumber;
											consultOrderDisplayObj["outsideHospitalId"] =$scope.selectedValues.outsideHospitalId != undefined ? $scope.selectedValues.outsideHospitalId : null ;
											consultOrderDisplayObj["outsideHospitalDesc"] =outsideHospitalDesc!=undefined ? outsideHospitalDesc:null;
											consultOrderDisplayObj["outsideHosDocId"] =$scope.selectedValues.outsideHosDocId!=undefined ? $scope.selectedValues.outsideHosDocId:null;
											consultOrderDisplayObj["outsideHospitalDoctorName"] =outsideHospitalDoctorName!=undefined ? outsideHospitalDoctorName:null;
											$scope.checkAlreadyExist=[];
											$scope.checkAlreadyExist.push(consultOrderDisplayObj);
											
											
			                               if($scope.searchType =='interHospital'){
			                            		 angular.forEach($scope.checkAlreadyExist, function(value, key) {
			 										if($scope.consultOrdersList!=undefined && $scope.consultOrdersList.length>0){
			 											var chkStatus= $scope.consultOrdersList.filter(function(item)
			 											{ 
			 												return (item.outsideHospitalId == value.outsideHospitalId );
			 			    									})
			 		    	    								if(chkStatus.length>0){
			 		    	    									$scope.consultOrderFlagAdd=false;
			 		    	    									growl.warning("Already exist",
			 		    			    	    							{
			 		    			    	    							title : 'Warning!'
			 		    			    	    							});
			 		    			    	    							$rootScope.stopLoader();
			 		    			    	    							return false;
			 		    	    								}
			 											
			 												
			 			    								}
			 											
			 										 });
			 								 if($scope.addedConsultOrderForDisplay.length>0){
			 	                                    angular.forEach($scope.checkAlreadyExist,function(value,index){
			 	                                        
			 	                                    	if($scope.addedConsultOrderForDisplay!=undefined && $scope.addedConsultOrderForDisplay.length>0){
			 												var chkStatus= $scope.addedConsultOrderForDisplay.filter(function(item)
			 												{ 
			 													return (item.outsideHospitalId == value.outsideHospitalId );
			 				    									})
			 			    	    								if(chkStatus.length>0){
			 			    	    									$scope.consultOrderFlagAdd=false;
			 			    	    									growl.warning("Already exist",
			 			    			    	    							{
			 			    			    	    							title : 'Warning!'
			 			    			    	    							});
			 			    			    	    							$rootScope.stopLoader();
			 			    			    	    							return false;
			 			    	    								}
			 												
			 												
			 				    								}
			 	                                    });
			 	                                }
											}else{
												 angular.forEach($scope.checkAlreadyExist, function(value, key) {
														if($scope.consultOrdersList!=undefined && $scope.consultOrdersList.length>0){
															var chkStatus= $scope.consultOrdersList.filter(function(item)
															{ 
																return (item.refToSpecialityId == value.refToSpecialityId
						                                                && item.refToDoctorId == value.refToDoctorId 
						                                                );
							    									})
						    	    								if(chkStatus.length>0){
						    	    									$scope.consultOrderFlagAdd=false;
						    	    									growl.warning("Already exist",
						    			    	    							{
						    			    	    							title : 'Warning!'
						    			    	    							});
						    			    	    							$rootScope.stopLoader();
						    			    	    							return false;
						    	    								}
							    								}
															
														 });
												 if($scope.addedConsultOrderForDisplay.length>0){
					                                    angular.forEach($scope.checkAlreadyExist,function(value,index){
					                                        
					                                    	if($scope.addedConsultOrderForDisplay!=undefined && $scope.addedConsultOrderForDisplay.length>0){
																var chkStatus= $scope.addedConsultOrderForDisplay.filter(function(item)
																{ 
																	return (item.refToSpecialityId == value.refToSpecialityId
							                                                && item.refToDoctorId == value.refToDoctorId 
							                                                );
								    									})
							    	    								if(chkStatus.length>0){
							    	    									$scope.consultOrderFlagAdd=false;
							    	    									growl.warning("Already exist",
							    			    	    							{
							    			    	    							title : 'Warning!'
							    			    	    							});
							    			    	    							$rootScope.stopLoader();
							    			    	    							return false;
							    	    								}
								    								}
					                                    });
					                                }
											}
											
											
											 
											 if($scope.consultOrderFlagAdd){
											 $scope.addedConsultOrderForDisplay.push(consultOrderDisplayObj);
											 $scope.selectedValues.specialityId='';
											 $scope.selectedValues.doctorId='';
											 $scope.selectedValues.priority='';
											 $scope.selectedValues.outsideHospitalId='';
											 $scope.selectedValues.outsideHospitalDesc='';
											 $scope.selectedValues.outsideHospitalDoctorName='';
											 $scope.selectedValues.outsideHosDocId='';
											 console.log("dddd0", $scope.addedConsultOrderForDisplay);
											 }
										   }
								        $scope.getDepartmentsForHospAndUnit = function() {
											
											var data = {unitId:$scope.selectedValues.unitId,
													organizationId:$scope.organizationId
													}
											
											var URI = BASE_URL + ROOT_URL + GETACTIVEUNITSPECIALITYLIST;
											  
											GenericServiceParamHeader.serviceAction(METHOD_POST, URI, data)
											.then(function(response) {
												if(response.data.status == "success"){
													$scope.departmentList = response.data.listObject
												}
										})
											
										}
								    	
								    	$scope.getDoctorsForDepartment = function() {
											var orgId = JSON.parse($scope.selectedValues.selectedHosp).organizationId
											var unitId = JSON.parse($scope.selectedValues.selectedUnit).unitId
											var deptId = JSON.parse($scope.selectedValues.selectedDept).departmentId
											
											var URI = BASE_URL + ROOT_URL + GETDOCTORS + "?orgId=" + 
											  orgId + "&unitId=" + unitId + "&departmentId=" + deptId
											
											  GenericServiceParamHeader.serviceAction(METHOD_GET, URI).then(function(response) {
												if(response.data.status == "success"){
													$scope.doctorsList = response.data.listObject
												}
											})
										}
								        $scope.getDoctorBySpecialityConsultDoctor = function(id) {

											var data1 = {
												specialityId : id,
												unitId :$scope.selectedValues.unitId,
												organizationId : $scope.organizationId
											};
											var URI = BASE_URL + ROOT_URL
													+ GETDOCTORBYSPECIALITYID;
											
											BillingGenericService
													.serviceAction(METHOD_POST, URI, data1,
															NOTIFICATION_MSG_STATUS_FALSE)
													.then(
															function(response) {
																console.log(response);
																$scope.doctorList = [];
																// $rootScope.startLoader();
																if (response.data.status == "success")
																	$scope.doctorList = response.data.listObject;
															});

										}
								        
										   //delete refral order table
										   $scope.removeConsultOrder = function(conOrd) {
					                        	 $rootScope.startLoader();
													var data = {
															consultDoctorId:conOrd.consultDoctorId,
															status:'I',
													};
													var URI = BASE_URL + ROOT_URL + "/api/opd/inactiveConsultDoctor";
													
													GenericService.serviceAction(METHOD_POST, URI,data)
																.then(function(response) {
																	if(response.data.status == "success"){
																		$rootScope.stopLoader();
																		growl.success('Removed Successfully',
																				{
																					title : 'Success!'
																				});
																		$scope.saveConsultOrder();
																		 $scope.consultOrderList();
																		/*$scope.onChangeSearchType();*/
																		$rootScope.stopLoader();
																	}
													});
												}
										  
										   
										   //updateremark
											$scope.updateConsultRemark = function(conOrd){

												$rootScope.startLoader();
												try{

												var data = {
														consultDoctorId:conOrd.consultDoctorId,
														notes:conOrd.notes
														
														}
												console.log(data);
												var URI = BASE_URL + ROOT_URL + "/api/opd/updateConsultDoctorNotes";
												GenericService.serviceAction(METHOD_POST, URI,data)
												.then(function(response){
												if (response.data.status == 'success')
												{
														growl.success(response.data.message,
												{
															title : 'Success!'
													});
														$rootScope.stopLoader();
														$scope.getLastEncounterDetails();
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
										  
										   /********************************inter-Hospital****************************/
										// Outside Hospital List
											$scope.getOutSideHospitalList = function() {
												$scope.outSideHospitalList=[];
												$rootScope.startLoader();
												var URI = BASE_URL + ROOT_URL + "/api/global/getActiveOutsideHospitalMaster/"+$scope.organizationId;
												console.log("URI", URI);
												GenericService.serviceAction("GET", URI).then(function(response) {
													$rootScope.stopLoader();
													$scope.outSideHospitalList = response.data.listObject;
												});
											}
											$scope.notes='';
										   $scope.saveConsultOrder = function() {

												$scope.consultOrderFlag=true;
												$rootScope.startLoader();
												if ($scope.addedConsultOrderForDisplay.length > 0) {
													var addedConsultOrder = $scope.addedConsultOrderForDisplay
													var consultOrder = []
													
													for (var i = 0; i < addedConsultOrder.length; i++) {
														var consultOrderObj = {}
														consultOrderObj["patientId"] = addedConsultOrder[i].patientId
														consultOrderObj["refToSpecialityId"] = addedConsultOrder[i].refToSpecialityId
														consultOrderObj["refBySpecialityId"] = addedConsultOrder[i].refBySpecialityId
														consultOrderObj["refToDoctorId"] = addedConsultOrder[i].refToDoctorId
			// consultOrderObj["priorty"] = addedConsultOrder[i].priorty
														consultOrderObj["referredStatus"] = 'Pending'
														consultOrderObj["createdBy"] = $scope.userId
														consultOrderObj["updatedBy"] = $scope.userId;
														consultOrderObj["encounterId"] = $scope.encounterDetailsObj.encounterId;
														consultOrderObj["visitTypeId"]=$scope.encounterDetailsObj.visitTypeId;
														consultOrderObj["unitId"] =$scope.unitId;
														consultOrderObj["referredUnitId"] = $scope.selectedValues.unitId;
														consultOrderObj["organizationId"] = $scope.organizationId;
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
													
													
													
						                               if($scope.searchType =='interHospital'){
						                            	   
						                            	   angular.forEach(consultOrder, function(value, key) {
															if($scope.consultOrdersList!=undefined && $scope.consultOrdersList.length>0){
																var chkStatus= $scope.consultOrdersList.filter(function(item)
																{ 
																	return (item.outsideHospitalId == value.outsideHospitalId
							                                                );
								    									})
							    	    								if(chkStatus.length>0){
							    	    									$scope.consultOrderFlag=false;
							    	    									growl.warning("Already exist",
							    			    	    							{
							    			    	    							title : 'Warning!'
							    			    	    							});
							    			    	    							$rootScope.stopLoader();
							    			    	    							return false;
							    	    								}
																
																
								    								}
																
															 });
						                            	   }else{
																 
																 angular.forEach(consultOrder, function(value, key) {
																	if($scope.consultOrdersList!=undefined && $scope.consultOrdersList.length>0){
																		var chkStatus= $scope.consultOrdersList.filter(function(item)
																		{ 
																			return (item.refToSpecialityId == value.refToSpecialityId
									                                                && item.refToDoctorId == value.refToDoctorId 
									                                                );
										    									})
									    	    								if(chkStatus.length>0){
									    	    									$scope.consultOrderFlag=false;
									    	    									growl.warning("Already exist",
									    			    	    							{
									    			    	    							title : 'Warning!'
									    			    	    							});
									    			    	    							$rootScope.stopLoader();
									    			    	    							return false;
									    	    								}
										    								}
																		
																	 });
																 }
													
											/*if($scope.consultOrderFlag)*/
						                    if ($scope.addedConsultOrderForDisplay.length > 0 || ($scope.notes!=null && $scope.notes!="")){
												$rootScope.startLoader();
													var URI = BASE_URL + ROOT_URL
															+ SAVECONSULTORDER;

													GenericServiceParamHeader
															.serviceAction(METHOD_POST, URI,
																	consultOrder,
																	NOTIFICATION_MSG_STATUS_TRUE)
															.then(
																	function(response) {
																		if (response.data.status == "success") {
																			$rootScope.stopLoader();
																			$scope.addedConsultOrderForDisplay = [];
																			$scope.selectedValues = {
																				unitId : '',
																				doctorId : '',
																				specialityId : ''
																			};
																			$scope.consultOrderList();
																		}
																	});
												}
													
												} /*else {
													growl.warning("Please add note", {
														title : 'Warning!'
													});
													$rootScope.stopLoader();
													return false;
												}*/
												$rootScope.stopLoader();
										   }
										   $scope.yesterday="";
				                             $scope.getLastEncounterDetails=function(){

				                            	var sdate = new Date();
											 	sdate.setDate(sdate.getDate() - 1);
											 	$scope.yesterday=sdate;
												
												var encounterObj={

														 unitId :$scope.unitId,
														 organizationId  :$scope.organizationId,
											    		 patientId : $scope.encounterDetailsObj.patientId ,
											    		 toDate:moment($scope.yesterday).format('YYYY-MM-DD')
												}
												
												var URI = BASE_URL + ROOT_URL + "/api/opd/getEncounterDetailsForCoversheet";

												BillingGenericService.serviceAction(METHOD_POST,URI,encounterObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){

													$scope.encounterList = response.data.listObject;
													console.log("$scope.encounterListttttttt", $scope.encounterList);
												});
												
												
											}
				                             $scope.getHospitalDoctorList = function(id) {
													
													var data = {
															organizationId:$scope.organizationId,
															unitId:$scope.unitId,
															outsideHospitalId:id,
															};
													
													var URI = BASE_URL + ROOT_URL + "/api/global/getOutsideHospitalDoctorMapperList";
													
													GenericService.serviceAction("POST", URI,data).then(function(response) {
														$scope.hospitalDoctorsList = response.data.listObject;
														console.log("doctList",$scope.hospitalDoctorsList);
														
														
													});
												}
				                             $scope.selectOutsideHospDoctorId = function(info, model,
					 									label,ind) {
					 								console.log(info);
					 								
					 								// $scope.outsideHospitalId
													// =$scope.selectedValues.outsideHospitalId;
					 								angular.forEach($scope.outsideHospitalDoctorList, function(val, key){
								    					if($scope.selectedValues.outsideHospitalDoctorName==val.outsideHospitalDoctorName){
								    						$scope.selectedValues.outsideHosDocId=val.outsideHosDocId;
								    						$scope.selectedValues.outsideHospitalId=val.outsideHospitalId;
								    					}
								    					
								    				});
					 								// $scope.outsideHosDocId =
													// $scope.selectedValues.outsideHosDocId;
					 							}
				                             $scope.searchOutsideHospitalDoctorList = function(searchKeyword){
				                            	 $scope.outsideHospitalDoctorList=[];
				                             	
				                             	var outsideHospitalId = $scope.selectedValues.outsideHospitalId;
				                             	
													var data = {
															outsideHospitalId:outsideHospitalId,
										        			searchDoctor:searchKeyword
															
															};
													
													var URI = BASE_URL + ROOT_URL + "/api/global/searchOutsideHospitalDoctorList";
													
													return GenericService.serviceAction("POST",URI,data).then(function(response){
										    			if(response.data.status == "success"){
										    				$scope.outsideHospitalDoctorList = response.data.listObject;
										    				
										    			}
										    			return $scope.outsideHospitalDoctorList;
													});
												}
								        /********************************vaccination start****************************************/
								        
								        $scope.datepickervaccinePicker = function(index) {

				                             for (var i = $scope.activevaccine.length - 1; i >= 0; i--) {
				                                 if (i === index) {
				                                     $scope.datepickervaccine.opened[i] = true;
				                                 } else {
				                                	
				                                     $scope.datepickervaccine.opened[i] = false;
				                                 }
				                             }
				                         };

				                         $scope.datepickervaccine = {
				                             opened : [ false ],

				                         };
				                         
				                         $scope.datepickerAdministration = function(index) {

				                             for (var i = $scope.activevaccine.length - 1; i >= 0; i--) {
				                                 if (i === index) {
				                                     $scope.datepickerAdm.opened[i] = true;
				                                 } else {
				                                	
				                                     $scope.datepickerAdm.opened[i] = false;
				                                 }
				                             }
				                         };
				                         
				                         $scope.datepickerAdm = {
				                             opened : [ false ],

				                         };
								        
								        $scope.activevaccine = [];
									    $scope.getAllActivevaccine = function()
								        {
									    	$rootScope.startLoader();
								        
								        	var patientId = $scope.encounterDetailsObj.patientId;
								    		var dateObj = moment(new Date()).format('YYYY-MM-DD');
								    		$scope.genderId = $scope.patientDetails.genderId;
								  
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
									    
									    $scope.getAllActivevaccine();
									    
									    $scope.printVaccinationChart=function()
									    {
									    //$scope.save();

									    var data = {
									    "appName": "HIS",
									    "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
									    "reportDesignFilePath": "getVaccinationChart.rpt",
									    "reportFormat": "PDF",
									    "exportFileName": "Certificate",
									    "reportParams": [
									    {
									    "paramName": "patient_id ",
									    "paramType": "Integer",
									    "paramValue": $scope.encounterDetailsObj.patientId

									    }
									    ]
									    };
									    reportService.generate(REPORT_API_URI1,data);

									    }
									    
									    
									    $scope.flagVaccineValidate=false;
									    $scope.validateVaccination = function(flagVaccine){
									    if(flagVaccine != "")
									    	/*{
									    $scope.flagVaccineValidate=true;
									    }
									    else*/if(flagVaccine != "false"){
									    $scope.flagVaccineValidate=true;
									    }

									    }
								        
								        $scope.getVaccineData = function(){
										    $scope.getAllActivevaccine();
										    $timeout( function(){
										    	 $rootScope.startLoader();
					                    		var paramObjForVaccine = {
					                    				
					                    				patientId:$scope.encounterDetailsObj.patientId,
					                    				organizationId:$scope.organizationId,
										        		unitId : $scope.unitId
					                    				
					                    		}
					                    		
					                    		console.log("QQQQ",paramObjForVaccine);
					                    		 
					                            var URI = BASE_URL + ROOT_URL+ "/api/opd/getListOfVaccineImmunizationMapper";
					                        GenericService.serviceAction("POST", URI, paramObjForVaccine)
					                            .then(function(response){console.log(response);
					                            if (response.data.status == "success")
					                                $scope.VaccineFinalList = [];
					                                $scope.vaccineMapperListForCaseSheet=[];
					                                $scope.vaccineMapperList  = response.data.listObject;
					                            console.log("vaccineMapperList",response.data.listObject);
					                            console.log("activevaccine",$scope.activevaccine);
					                            
					                            
					                        
												 
					                             angular.forEach($scope.activevaccine, function(value , key) {
					                            	 var checkFlag = false;
					                                 angular.forEach($scope.vaccineMapperList, function(value1 , key1) {
					                                	 if(value1.isNoSignificantVaccination == 'N'){
					                                		 $scope.noSignificantVaccination='N';
					                                	 }else{
					                                		 $scope.noSignificantVaccination='Y';
					                                	 }
					                                	
					                                	
					                                	 if(value.vaccinationId == value1.vaccinationMasterId)
					                                		 {
					                                		 	checkFlag = true;
					                                		 	value1.genericName=value.genericName;
					                                		 	$scope.newArr = {};
					                                		 	$scope.newArr.vaccinationId = value.vaccinationId;
					                                			$scope.newArr.name = value1.name;
					                                			$scope.newArr.name1 = value1.name;
					                                			$scope.newArr.itemId = value1.itemId;
					                                			$scope.newArr.listOfGenericIdWiseList1 = value1.listOfGenericIdWiseList;
					                                		 	$scope.newArr.minAge = value.minAge;
					                                		 	$scope.newArr.minDay = value.minDay;
					                                		 	$scope.newArr.maxAge = value.maxAge;
					                                		 	$scope.newArr.maxDay = value.maxDay;
					                                		 	$scope.newArr.genericName = value.genericName;
					                                		 	$scope.newArr.doseDesc = value.doseDesc;
					                                		 	$scope.newArr.strengthDesc = value.strengthDesc;
					                                		 	$scope.newArr.units = value.units;
					                                		 	$scope.newArr.description = value.description;
					                                		 	$scope.newArr.siteName = value.siteName;
					                                		 	$scope.newArr.series = value.series;
					                                		 	$scope.newArr.abbreviation = value.abbreviation;
					                                		 	$scope.newArr.genericId =value.genericId;
					                                		 	$scope.newArr.location =value1.location;
					                                		 	$scope.newArr.calculatedDateShow =value1.calculatedDate;
					                                			$scope.newArr.calculatedDate =value1.calculatedDate;
					                                			$scope.newArr.plannedDate =moment( value1.plannedDate,"DD-MM-YYYY").toDate();
					                                			$scope.newArr.dateOptionsAdm=
			        											{
			        												formatYear : 'yyyy',
			        												showWeeks : false,
			        												minDate :moment(value1.plannedDate,"DD-MM-YYYY").toDate(),
			        												maxDate : new Date(),
			        											};
					                                			if(value1.actualDate != null)
					                                				{
					                                		    	$scope.newArr.actualDate =moment(value1.actualDate,"DD-MM-YYYY").toDate();
					                                			    $scope.newArr.dateOptionsVacc =
				                    							  {
				                    								formatYear : 'yyyy',
				                    								showWeeks : false,
				                    								minDate : $scope.newArr.actualDate,
				                    							  };
					                                				}else{
					                                					
						                                			    $scope.newArr.dateOptionsVacc =
					                    							  {
					                    								formatYear : 'yyyy',
					                    								showWeeks : false,
					                    								minDate : $scope.newArr.plannedDate
					                    							  };
					                                				}
					                                			
					                                			
					                                			
					                                			$scope.newArr.remarks = value1.remarks;
					                                		 	$scope.newArr.vaccineFlagStatus = value1.vaccineFlagStatus;
					                                		 	$scope.vaccineMapperListForCaseSheet.push(value1);
					                                		 	
					                                		 }
					                                	
					                                 		});
																		if (checkFlag == true) {
																				$scope.VaccineFinalList.push($scope.newArr);
																			} else {
																				
																				
																				$scope.dateOptionsAdm=[];
																				$scope.dateOptionsVacc=[];
																				 
																				var newArr = {};
																				newArr.vaccinationId = value.vaccinationId;
									                                		 	newArr.genericId =value.genericId;
						                                            		 	newArr.name = value.name;
						                                            		 	newArr.name1 = value.name;
						                                            		 	newArr.itemId =value.itemId;
						                                            		 	newArr.strengthDesc = value.strengthDesc;
						    		                                		 	newArr.units = value.units;
						                                            		 	newArr.listOfGenericIdWiseList1 = value.listOfGenericIdList;
						                                            		 	newArr.doseDesc = value.doseDesc;
						                                            		 	newArr.description = value.description;
						                                            		 	newArr.siteName = value.siteName;
						                                            		 	newArr.minAge = value.minAge;
						    		                                		 	newArr.minDay = value.minDay;
						    		                                		 	newArr.maxAge = value.maxAge;
						    		                                		 	newArr.maxDay = value.maxDay;
						    		                                		 	newArr.genericName = value.genericName;
						    		                                		 	newArr.series = value.series;
						    		                                			newArr.abbreviation = value.abbreviation;
						    		                                			var array = value.minAge.split('-');
						                                            		 	var age=array[0];
						                            						    var duration=value.minDay;
						            											if(duration=="Days"){
						            												var pDate = moment($scope.patientDetails.birthDate,"DD-MM-YYYY").add('days',age).toDate();
							                            							var year=pDate.getFullYear();
							                            							var a=["01","02","03","04","05","06","07","08","09","10","11","12"];
							                            							var m=pDate.getMonth();
							                            							var month=a[m];
							                            							var dayss=pDate.getDate();
							                            							if(dayss < 9){
							                            								var days="0"+ pDate.getDate();
							                            							}else
							                            								{
							                            								var days = pDate.getDate();
							                            								}
							                            							var c=days+"/"+month+"/"+year;
							                            							var d=c.toString();
							                            							//alert("hiii:"+d);
//							                            							newArr.plannedDate = d;
							                            							var ps=days+"-"+month+"-"+year+" 00:00:00";
							                            							var dps=ps.toString();
							                            							newArr.calculatedDate=  dps;
							                            							newArr.calculatedDateShow=d;
							                            							$scope.calculatedDate=newArr.calculatedDate;
//							                            							newArr.plannedDate = moment(dps,"DD-MM-YYYY").toDate();
//							                            							newArr.plannedDateSave = dps;
						            											}
						            											if(duration=="Month"){
						            												var pDate = moment($scope.patientDetails.birthDate,"DD-MM-YYYY").add('months',age).toDate();
							                            							var year=pDate.getFullYear();
							                            							var a=["01","02","03","04","05","06","07","08","09","10","11","12"];
							                            							var m=pDate.getMonth();
							                            							var month=a[m];
							                            							var days=pDate.getDate();
							                            							var c=days+"/"+month+"/"+year;
							                            							var d=c.toString();
							                            							//alert("hiii:"+d);
//							                            							newArr.plannedDate = d;
							                            							var ps=days+"-"+month+"-"+year+" 00:00:00";
							                            							var dps=ps.toString();
							                            							newArr.calculatedDateShow= d;
							                            							newArr.calculatedDate=  dps;
							                            							$scope.calculatedDate=newArr.calculatedDate;
//							                            							newArr.plannedDate =moment(dps,"DD-MM-YYYY").toDate();
//							                            							newArr.plannedDateSave = dps;
						            											}
						            											if(duration=="Years"){
						            												var pDate = moment($scope.patientDetails.birthDate,"DD-MM-YYYY").add('year',age).toDate();
							                            							var year=pDate.getFullYear();
							                            							var a=["01","02","03","04","05","06","07","08","09","10","11","12"];
							                            							var m=pDate.getMonth();
							                            							var month=a[m];
							                            							var days=pDate.getDate();
							                            							var c=days+"/"+month+"/"+year;
							                            							var d=c.toString();
							                            							//alert("hiii:"+d);
//							                            							newArr.plannedDate = d;
							                            							var ps=days+"-"+month+"-"+year+" 00:00:00";
							                            							var dps=ps.toString();
							                            							newArr.calculatedDateShow= d;
							                            							newArr.calculatedDate=  dps;
							                            							$scope.calculatedDate=newArr.calculatedDate;
//							                            							newArr.plannedDate =moment(dps,"DD-MM-YYYY").toDate();
//							                            							newArr.plannedDateSave = dps;
						            											}
						            											if(duration=="Weeks"){
						            												var pDate = moment($scope.patientDetails.birthDate,"DD-MM-YYYY").add('days',age*7).toDate();
							                            							var year=pDate.getFullYear();
							                            							var a=["01","02","03","04","05","06","07","08","09","10","11","12"];
							                            							var m=pDate.getMonth();
							                            							var month=a[m];
							                            							var days=pDate.getDate();
							                            							var c=days+"/"+month+"/"+year;
							                            							var d=c.toString();
							                            							//alert("hiii:"+d);
//							                            							newArr.plannedDate = c;
							                            							var ps=days+"-"+month+"-"+year+" 00:00:00";
							                            							var dps=ps.toString();
							                            							newArr.calculatedDateShow= d;
							                            							newArr.calculatedDate=  dps;
							                            							$scope.calculatedDate=newArr.calculatedDate;
//							                            							newArr.plannedDate =moment(dps,"DD-MM-YYYY").toDate();
//							                            							newArr.plannedDateSave = dps;
						            											}
						            											
						            											
						            											/*var array = $scope.patientDetails.birthDate.split('-');
						            											var birthDate = "";
						            										    return birthDate = array[1]+'/'+array[1]+'/'+array[0];*/
						            											var birthDate = $rootScope.getFormatedDateeShase(newArr.calculatedDateShow);
						            											newArr.dateOptionsAdm=
						            											{
						            											formatYear : 'yyyy',
						            											showWeeks : false,
						            											minDate : new Date(birthDate),
						            											maxDate : new Date(),
						            											};
						            											console.log("dateOptionsAdm",$scope.dateOptionsAdm);
						            											newArr.actualDate = value.actualDate;
						            											
						    		                                			newArr.dateOptionsVacc =
						    		                    							{
						    		                    								formatYear : 'yyyy',
						    		                    								showWeeks : false,
						    		                    								minDate : new Date(),
						    		                    							};
						                                            		 	newArr.vaccineFlagStatus = 'N';
																				$scope.VaccineFinalList.push(newArr);
																			}
					                            	       
					                                });
					                            
					                             console.log("fffffffFinal Vaccine Array",JSON.stringify($scope.VaccineFinalList));
					                             
					                             $rootScope.stopLoader();
					                                                                                      
					                                    });
					                    
					                    },5000);
									}
								        $scope.getVaccineData();
								        
								        $scope.checkVaccineNextDoseDate=function(index){
		                                	
		                                    var dateObj = moment(new Date()).format('YYYY-MM-DD');
		                                    var vacDat=moment($scope.VaccineFinalList[index].actualDate).format('YYYY-MM-DD'); 
		                                    if(vacDat < dateObj){
		                                        growl.warning(
		                                                   "Next dose date should be greater than today's date.",
		                                                   {
		                                                       title : 'Warning!'
		                                                   });
		                                        $scope.VaccineFinalList[index].actualDate='';
		                                        $scope.flagVaccineValidate=false;
		                                        return false;
		                                    }
		                                }
								        
								        
								        $scope.showAndAddVaccination = function(info){
											if(info.genericName){
												$scope.VaccineFinalList.push(info);
												info.age = "";
												info.plannedDate = "";
												info.admissionDate = "";
												info.genericName = "";
												info.location = "";
												info.remark = "";
											}
											else{
												growl.warning("Please Enter Vaccine Details ", {
													title : 'Warning!'
												});
												return false;
											}
										}
		                                
								        $scope.saveVaccineHistory = function()
										{
										   $rootScope.startLoader();
											$scope.vaccineListDoctor = [];
											$scope.plannedDate=false;
											
											
											
											
											if($scope.noSignificantVaccination =='N'){
												angular.forEach($scope.VaccineFinalList, function(value , key) {
													
													if(value.actualDate != undefined && value.actualDate !="01/01/1970") {
														$scope.actualDate=moment(value.actualDate).format('DD-MM-YYYY HH:mm:ss');
													}else{
														$scope.actualDate=null;
													}
													if(value.calculatedDate != undefined){
														$scope.cDate=moment(value.calculatedDate,"DD-MM-YYYY").toDate()
														$scope.calculatedDate=moment($scope.cDate).format('DD-MM-YYYY HH:mm:ss');
													}else{
														$scope.calculatedDate=null;
													}
													if(value.name != null){
											 		/*	if(value.location != undefined)
										 				{
												 				if(value.plannedDate != undefined){*/
												 				var Data = {
												 						vaccinationMasterId: value.vaccinationId,
												 						itemId:value.itemId,
												 						location:value.location,
												 						vaccineFlagStatus : 'Y',
												 						plannedDate:moment(value.plannedDate).format('DD-MM-YYYY HH:mm:ss'),
												 						calculatedDate:$scope.calculatedDate,
												 						roleId:$scope.roleId,
												 						actualDate:$scope.actualDate,
												 						remarks: value.remarks,
												 						createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
																		updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
																		updatedBy:$scope.userId,
																		createdBy:$scope.userId,
																		status:'A',
																		patientId:$scope.encounterDetailsObj.patientId,
																		isNoSignificantVaccination:$scope.noSignificantVaccination,
																		encounterId: $scope.encounterDetailsObj.encounterId,
																		organizationId:$scope.organizationId,
																		unitId:$scope.unitId
																		
												 				}
												 				$scope.vaccineListDoctor.push(Data);
											 				/*}else{
											 					$scope.plannedDate=true;
											 				}*/
										 				/*}*/
												 }
					                     		});
												
												if($scope.plannedDate){
													 growl.warning(
					                                            " Please enter date of administration ",
					                                            {
					                                                title : 'Warning!'
					                                            });	
													 $rootScope.stopLoader();
									 			        return false;
												}
											
											
											if($scope.flagVaccineValidate){
											console.log("Doctor vaccine list:",$scope.vaccineListDoctor);
											var URI = BASE_URL + ROOT_URL+ "/api/opd/saveVaccineImmunizationMapper";
											 GenericService.serviceAction("POST", URI,$scope.vaccineListDoctor)
											.then(function(response) {
														console.log(response);
														 
														if (response.data.status == 'success') {
														
															growl.success(response.data.message,
																	{
																		title : 'Success!'
																	});
															$rootScope.stopLoader();
															// $scope.$broadcast('show-errors-reset');
														} else if (response.data.status == 'error') {
															
															growl.warning(
																	response.data.message,
																	{
																		title : 'Warning!'
																	});
															$rootScope.stopLoader();
														}
													});
												console.log("Save Vaccine list by Doctor ",$scope.vaccineListDoctor);
											}
											else{
											 growl.warning("Please Enter Vaccine Details ", {
													title : 'Warning!'
													});
													$rootScope.stopLoader();
											return false;
											}
											}else{

												angular.forEach($scope.VaccineFinalList, function(value , key) {
													
													if(value.actualDate != undefined && value.actualDate !="01/01/1970") {
														$scope.actualDate=moment(value.actualDate).format('DD-MM-YYYY HH:mm:ss');
													}else{
														$scope.actualDate=null;
													}
													if(value.calculatedDate != undefined){
														$scope.cDate=moment(value.calculatedDate,"DD-MM-YYYY").toDate()
														$scope.calculatedDate=moment($scope.cDate).format('DD-MM-YYYY HH:mm:ss');
													}else{
														$scope.calculatedDate=null;
													}
													
												 				var Data = {
												 						vaccinationMasterId: value.vaccinationId,
												 						itemId:value.itemId,
												 						location:value.location,
												 						vaccineFlagStatus : 'Y',
												 						plannedDate:moment(value.plannedDate).format('DD-MM-YYYY HH:mm:ss'),
												 						calculatedDate:$scope.calculatedDate,
												 						roleId:$scope.roleId,
												 						actualDate:$scope.actualDate,
												 						remarks: value.remarks,
												 						createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
																		updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
																		updatedBy:$scope.userId,
																		createdBy:$scope.userId,
																		status:'A',
																		patientId:$scope.encounterDetailsObj.patientId,
																		isNoSignificantVaccination:$scope.noSignificantVaccination,
																		encounterId: $scope.encounterDetailsObj.encounterId,
																		organizationId:$scope.organizationId,
																		unitId:$scope.unitId
																		
												 				}
												 				$scope.vaccineListDoctor.push(Data);
											 				
					                     		});
												
											var URI = BASE_URL + ROOT_URL+ "/api/opd/saveVaccineImmunizationMapper";
											 GenericService.serviceAction("POST", URI,$scope.vaccineListDoctor)
											.then(function(response) {
														console.log(response);
														 
														if (response.data.status == 'success') {
														
															growl.success(response.data.message,
																	{
																		title : 'Success!'
																	});
															$rootScope.stopLoader();
															// $scope.$broadcast('show-errors-reset');
														} else if (response.data.status == 'error') {
															
															growl.warning(
																	response.data.message,
																	{
																		title : 'Warning!'
																	});
															$rootScope.stopLoader();
														}
													});
											}
										}
								        
								        $scope.saveVaccine = function(info)
										{
								        	
										   $rootScope.startLoader();
											$scope.vaccineListDoctor = [];
											$scope.plannedDate=false;
											angular.forEach($scope.VaccineFinalList, function(value , key) {
												
												if(value.actualDate != undefined && value.actualDate !="01/01/1970") {
													$scope.actualDate=moment(value.actualDate).format('DD-MM-YYYY HH:mm:ss');
												}else{
													$scope.actualDate=null;
												}
												if(value.calculatedDate != undefined){
													$scope.cDate=moment(value.calculatedDate,"DD-MM-YYYY").toDate()
													$scope.calculatedDate=moment($scope.cDate).format('DD-MM-YYYY HH:mm:ss');
												}else{
													$scope.calculatedDate=null;
												}
												if(value.name != null){
										 			/*if(value.location != undefined)
									 				{*/
											 				/*if(value.plannedDate != undefined){*/
											 				var Data = {
											 						vaccinationMasterId: value.vaccinationId,
											 						itemId:value.itemId,
											 						location:value.location,
											 						vaccineFlagStatus : 'Y',
											 						plannedDate:moment(value.plannedDate).format('DD-MM-YYYY HH:mm:ss'),
											 						calculatedDate:$scope.calculatedDate,
											 						roleId:$scope.roleId,
											 						actualDate:$scope.actualDate,
											 						remarks: value.remarks,
											 						createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
																	updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
																	updatedBy:$scope.userId,
																	createdBy:$scope.userId,
																	status:'A',
																	patientId:$scope.encounterDetailsObj.patientId,
																	isNoSignificantVaccination:$scope.noSignificantVaccination,
																	encounterId: $scope.encounterDetailsObj.encounterId,
																	organizationId:$scope.organizationId,
																	unitId:$scope.unitId
																	
											 				}
											 				$scope.vaccineListDoctor.push(Data);
										 				/*}else{
										 					$scope.plannedDate=true;
										 				}*/
									 				/*}*/
											 }
				                     		});
											$scope.validateVaccination(info.plannedDate);
											if($scope.plannedDate){
												 growl.warning(
				                                            " Please enter date of administration ",
				                                            {
				                                                title : 'Warning!'
				                                            });	
												 $rootScope.stopLoader();
								 			        return false;
											}
											if($scope.flagVaccineValidate){
											console.log("Doctor vaccine list:",$scope.vaccineListDoctor);
											var URI = BASE_URL + ROOT_URL+ "/api/opd/saveVaccineImmunizationMapper";
											 GenericService.serviceAction("POST", URI,$scope.vaccineListDoctor)
											.then(function(response) {
														console.log(response);
														 
														if (response.data.status == 'success') {
														
															growl.success(response.data.message,
																	{
																		title : 'Success!'
																	});
															$rootScope.stopLoader();
															// $scope.$broadcast('show-errors-reset');
														} else if (response.data.status == 'error') {
															
															growl.warning(
																	response.data.message,
																	{
																		title : 'Warning!'
																	});
															$rootScope.stopLoader();
														}
													});
												console.log("Save Vaccine list by Doctor ",$scope.vaccineListDoctor);
											}
											else{
												$rootScope.stopLoader();
											}
											/*else{
											 growl.warning("Please Enter Vaccine Details ", {
													title : 'Warning!'
													});
													$rootScope.stopLoader();
											return false;
											}*/
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
			    	                         	
			    	                      	 var headerObj = {
			    	              					    unit_id : $scope.unitId,
			    	              						organization_id : $scope.organizationId
			    	              					     };
			    	                      	 
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
//						                                		pulse rate
						                                		if(parseInt(value.value) != 0){
						                                		$scope.finalVitalObjFromAllVitalList.pulseRate = value.value;
						                                		$scope.finalVitalObjFromAllVitalList.createdDatepulseRate = value.createdDate;
						                                		$scope.finalVitalObjFromAllVitalList.createdTimepulseRate = value.createdTime;
						                                		$scope.finalVitalObjFromAllVitalList.unitDescpulseRate = value.unitDesc;
						                                		}
						                                	}
						                                	if(value.vitalId == 3){
//						                                		BP
						                                		if(parseInt(value.value) != 0){
						                                		$scope.finalVitalObjFromAllVitalList.bps = value.value;
						                                		$scope.finalVitalObjFromAllVitalList.createdDatebps = value.createdDate;
						                                		$scope.finalVitalObjFromAllVitalList.createdTimebps = value.createdTime;
						                                		$scope.finalVitalObjFromAllVitalList.unitDescbps = value.unitDesc;
						                                		}
						                                	}
						                                	if(value.vitalId == 14){
//						                                		SPo2
						                                		if(parseInt(value.value) != 0){
						                                		$scope.finalVitalObjFromAllVitalList.bloodOxygen = value.value;
						                                		$scope.finalVitalObjFromAllVitalList.createdDatebloodOxygen = value.createdDate;
						                                		$scope.finalVitalObjFromAllVitalList.createdTimebloodOxygen = value.createdTime;
						                                		$scope.finalVitalObjFromAllVitalList.unitDescbloodOxygen = value.unitDesc;
						                                		}
						                                	}
						                                	if(value.vitalId == 7){
//						                                		SPo2
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
																		 
//																		$rootScope.stopLoader();
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
														  $('#confirmSave').modal('show');
														  
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

