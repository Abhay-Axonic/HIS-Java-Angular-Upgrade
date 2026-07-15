/**
 * @author sneha dalave
 * @author chetna dhokne
 */

angular.module('myApp')
		.controller(
				"inputOutputChartHistoryController",
				[
						'$scope',
						'$rootScope',
						'$state',
						'$cookies',
						'GenericService',
						'PagerService',
						'promiseFactory',
						'$sessionStorage',
						'growl',
						'$interval',
						'$stateParams',
						'GenericServiceParamHeader',
						'BillingGenericService',
						'$window',
						function($scope, $rootScope, $state, $cookies,
								GenericService, PagerService, promiseFactory,
								$sessionStorage, growl,$interval,$stateParams,GenericServiceParamHeader,BillingGenericService,$window) {

							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "Sample";

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.admissionObj = {};
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;

							/*** Check user is Loggedin */
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.doctorId = cookieObject.doctorId;
							$scope.createdBy = cookieObject.userId;
							$scope.updatedBy = cookieObject.userId;
							$scope.roleName = cookieObjectAppConfig.roleName;
							
							$scope.stopAndSaveDisabled = true;
							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
							 if(cookieObjectAppConfig == undefined){
							  $state.go('login');
							  return;
							 }
							 else
							  {
							   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
							   
							  }
							
								// function for initializa clockpicker
								$scope.initClockPicker = function() {

									$('.clockpicker').clockpicker();
								}

								$scope.nursingWardList = [];
//								var nursingStationsWardsList = $cookies.getObject('nursingStationsWardsList');
//								 if(nursingStationsWardsList == undefined){
//									 growl.warning(
//												'No ward mapped with selected nursing station !!',
//												{
//													title : 'Success!'
//												});
//								 }
//								 else
//								  {
//								   console.log('nursingStationsWardsList',nursingStationsWardsList);
//								   $scope.nursingWardList =nursingStationsWardsList; 
//								  
//								  console.log('hshsajsa',$scope.nursingWardList);
//								  }
								
								if($scope.roleName == "doctor"){
								 $scope.init = function(){
										
										$scope.saveBtnFlag = true;
										$scope.updateBtnFlag = false;
										
										$scope.startFlag=true;
										$scope.stopFlag=true;
							
					           	 }
						    $scope.init();
						    $scope.initPatient = function()
						     {
							$scope.patientDetails = {
									"patientName" : "",
									"genderCode" : "",
									"age" : "",
									"uhIdNumber" : "",
									"dob" : "",
									"doctorFullName" : "",
									"dateOfAdmission" : "",
									"patientDischargeDate" : "",
									"wardName" : "",
									"bedNumber" : "",
									"imagePath":"",
									"patientId":"",
									"visitTypeId":"",
									"admissionId":"",
									"genderId":"",
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
								      "organizationId"  :$scope.organizationId,
								      "doctorId" :$scope.doctorId
								    }
						       }
						 $scope.initPatient();
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
							
							$scope.allergydata=function(patientDetails){
								var cookieObject = $cookies.getObject('cookieObject');
								var headerObj = {
										unit_id : cookieObject.unitId,
										organization_id : cookieObject.organizationId
									};
								$scope.dt = moment(new Date()).format('DD-MM-YYYY');
									var paramObjForPer = {
										patientId :  patientDetails.patientId,
										admissionId: patientDetails.admissionId,
										roleId : 3
									};
									
									var paramObjForHistList = {
											patientId :  patientDetails.patientId,
										};
									var data = {};
								
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
												   }else
												   {
														 $scope.allergyFlag = false;
													}
												  
											   });
												
											}else{
												 $scope.allergyFlag = false;
											}
											
										});
								}
							$scope.getAllergyDescriptions = function() {
							    if ($scope.activeAllergyListForIcon && $scope.activeAllergyListForIcon.length > 0) {
							        return $scope.activeAllergyListForIcon
							            .filter(value => value.isNoKnownAllergies !== 'Y') 
							            .map(value => value.allergyDesc) 
							            .join(', '); 
							    }
							    return ''; 
							};
					   
							$scope.searchPatient = function()
							{
								$rootScope.startLoader();
								$scope.DischargeSummaryFinalList=[];
								$scope.patientTemplateList=[];
//								var URI = BASE_URL + ROOT_URL + "/adt/searchListOfPatientsByWardId";
								var URI = BASE_URL + ROOT_URL + "/adt/searchListOfPatientsByDoctorIdForIOChart";
								var patSearchObj = angular.copy($scope.patientSearchObj);
								if($scope.patientSearchObj.birthDate != "" && typeof($scope.patientSearchObj.birthDate) != "undefined"){
									var dt = new Date($scope.patientSearchObj.birthDate);
									patSearchObj.birthDate = (dt.getMonth() + 1) + "-" + dt.getDate() + "-" + dt.getFullYear();
								} 
								BillingGenericService.serviceAction(METHOD_POST,URI, patSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									$scope.searchPatientList = [];
									if (response.data.status == "success")
										{
										$scope.patientList = response.data.listObject;
										patSearchObj = {};
										$scope.listPaymentDetails = [];
										$scope.patientDetailsList = [];
										$scope.patientDetails = {};
										$scope.activeAllergyListForIcon = [];
										 $scope.allergyFlag = false;
										$rootScope.stopLoader();
										}
									else{
										$rootScope.stopLoader();
									}
									
								});
							}
							$scope.patientList=[];
							$scope.patientDetails.visitTypeId = 1
							$scope.getPatientDetailsByIndex = function(index){
									$scope.patientDetails = {};
									$scope.patientDetails.age=($scope.patientList[index].age);
									$scope.patientDetails.patientId=($scope.patientList[index].patientId);
									$scope.patientDetails.patientName=($scope.patientList[index].patientName);
									$scope.patientDetails.admissionId=($scope.patientList[index].admissionId);
									$scope.patientDetails.genderCode=($scope.patientList[index].genderCode);
									$scope.patientDetails.uhIdNumber=($scope.patientList[index].uhIdNumber);
									$scope.patientDetails.birthDate=($scope.patientList[index].birthDate);
									$scope.patientDetails.patientProfile = ($scope.patientList[index].patientProfile);
									$scope.patientDetails.genderId = ($scope.patientList[index].genderId);
									$scope.patientDetails.dateOfAdmissionInput = ($scope.patientList[index].createdDate);
									$scope.patientDetails.bednumber = ($scope.patientList[index].bedNumber);
									$scope.patientDetails.wardName = $scope.patientList[index].wardName;
									$scope.patientDetails.admissionDate = ($scope.patientList[index].createdDate);
									$scope.patientDetails.admissionTime = ($scope.patientList[index].createdTime);
									$scope.patientDetails.pdd = $scope.patientList[index].pdd;
									$scope.patientDetails.visitTypeId = 1;
									$scope.patientDetails.inputOutputchart=true;
									console.log("admissionDate",$scope.patientDetails.admissionDate);
									console.log("admissionTime",$scope.patientDetails.admissionTime);
									$scope.getPatientDiagnosis();
									$scope.allergydata();
									$scope.getIntakeOutputDatesByPatientId();
									$scope.dateOptions = {
											
											showWeeks : false,
									        minDate:moment($scope.patientDetails.admissionDate, "DD-MM-YYYY").toDate(),
										};
							}	
							
							// Date For From Date on Search
							

							$scope.openDatePicker = function() {
								$scope.datepicker.opened = true;
							};

							$scope.datepicker = {
									opened : false
							};
							
							
							$scope.openFromDatePicker = function() {
								$scope.fromDatepicker3.opened = true;
								
							};

							$scope.fromDatepicker3 = {
									opened : false
							};
				
							$scope.openToDatePicker = function() {
								$scope.toDatepicker.opened = true;
								
							};

							$scope.toDatepicker = {
									opened : false
							};
							
							 $scope.openDatePickerSearch = function() {
									$scope.datepicker2.opened = true;
									};

									$scope.datepicker2 = {
									opened : false
									};
							
							$scope.getPatientDiagnosis = function() {
								$scope.patientDiagnosisListIcon =[];
								var headerObjDia = {
									'unit_id' : $scope.unitId,
									'organization_id' : $scope.organizationId,
									'patient_id' : $scope.patientDetails.patientId,
									'Content-Type' : 'application/json',
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
	                                         	$scope.comorbidityFlag = false;
	                                         	$scope.notifiableFlag = false;
												
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
																	$scope.comorbidityFlag = true;
																}
															});
														}
	                                         
	                                     });
							}
							
							
							 $scope.allergyFlag = false;
							$scope.allergydata=function(){

								 $scope.dt = moment(new Date()).format('DD-MM-YYYY');
									var headerObj = {
											unit_id : $scope.unitId,
											organization_id : $scope.organizationId
										};

										var paramObjForHistList = {
											patientId : $scope.patientDetails.patientId,
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
											
										}else{
											   $scope.allergyFlag = false;
										   }
										
									});
							}
							
							$scope.getIntakeOutputDatesByPatientId = function() {
								$scope.intakeAndOutputDate =[];
									
								var data={
										organizationId : $scope.organizationId,
										patientId : $scope.patientDetails.patientId,
								};
	                             var URI = BASE_URL + ROOT_URL
	                             + "/api/opd/getIntakeAndOutputDateByPatientId";
	                         GenericServiceParamHeader
	                             .serviceAction(METHOD_POST, URI, data,
	                                     NOTIFICATION_MSG_STATUS_FALSE)
	                             .then(
	                                     function(response) {
	                                         console.log(response);
	                                         if (response.data.status == "success")
	                                        	 $scope.intakeAndOutputDate = response.data.listObject;
	                                         angular.forEach($scope.intakeAndOutputDate,function(value,key) {
	                                        	    $scope.intakeDate = value.intakeDate +" "+value.intakeTime;
	                                        	    if(value.outputDate != null){
	                                        	    	$scope.outputDate = value.outputDate +" "+value.outputTime;
	                                        	    }
	                                         		
												});
	                                         console.log("intakeAndOutputDate",$scope.intakeAndOutputDate);
	                                     });
							}
							
							$scope.searchPatientDetailsOfIOchartHistory = function()
							{
								$rootScope.startLoader();
                               var data = {
                            		   fromDate:moment($scope.fromDatepicker).format('DD-MM-YYYY'),
                            		   toDate:moment($scope.toDatepicker).format('DD-MM-YYYY'),
                            		    patientId:$scope.patientDetails.patientId,
                            		    organizationId:$scope.organizationId,
                            		    unitId : $scope.unitId
								  }
								console.log("data",data);
                        
								var URI = BASE_URL + ROOT_URL + "/api/opd/getIntakeOutputChartHistoryListByDate";
								BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									$scope.searchPatientList = [];
									if (response.data.status == "success")
										{
										$scope.patientDetailsOfIOchartHistory=[];
										$scope.patientDetailsOfIOchartHistory = response.data.object;
										console.log("patientDetailsOfIOchartHistory",$scope.patientDetailsOfIOchartHistory );
										angular.forEach($scope.patientDetailsOfIOchartHistory, function(value , index) {
											
											 var forDateIntake=moment(value.intakeDate,"DD-MM-YYYY").toDate();
											 value.intakeDate =moment(forDateIntake).format('DD/MM/YYYY');
											
											angular.forEach(value.intakeOutputChartMasterDetailsList, function(value1 , index1) {
											$scope.newArr={}
											$scope.CumulativeBalance += parseInt(value1.balance);
										     $scope.patientDetailsOfIOchartHistory[index].CumulativeBalance=$scope.CumulativeBalance;
											 if(value1.intakeShiftName != null ){
												 $scope.newArr.intakeFlag=true;
												 $scope.newArr.shift=value1.intakeShiftName;
											 }
											
											 if(value1.outputShiftName != null ){
												 $scope.newArr.shift=value1.outputShiftName;
												 $scope.newArr.outputFlag=true;
											 }
											
											 $scope.patientDetailsOfIOchartHistory[index].intakeOutputChartMasterDetailsList[index1].shift=$scope.newArr.shift;
											 $scope.patientDetailsOfIOchartHistory[index].intakeOutputChartMasterDetailsList[index1].intakeFlag=$scope.newArr.intakeFlag;
											 $scope.patientDetailsOfIOchartHistory[index].intakeOutputChartMasterDetailsList[index1].outputFlag=$scope.newArr.outputFlag;
											
											})
										});
										console.log("final list",$scope.patientDetailsOfIOchartHistory);
										
										$scope.sortedListMorning=[];
										$scope.sortedListEvening=[];
										$scope.sortedListNight=[];
										$scope.sortedListGeneral=[];
										angular.forEach($scope.patientDetailsOfIOchartHistory, function(value4 , index1) {
//											var newArrMorning={};
//											var newArrEvening={};
//											var newArrNight={};
//											var newArrGeneral={};
											$scope.sortedListMorning=[];
											$scope.sortedListEvening=[];
											$scope.sortedListNight=[];
											$scope.sortedListGeneral=[];
											angular.forEach(value4.intakeOutputChartMasterDetailsList, function(value3 , index) {
												
											if(value3.shift== "Morning" || value3.shift== "morning"){
												var newArrMorning={};
												newArrMorning.shift=value3.shift;
												if(value3.intakeFlag != null){
													newArrMorning.intakeFlag=value3.intakeFlag;
												}
												if(value3.outputFlag != null){
													newArrMorning.outputFlag=value3.outputFlag;
												}
												if(value3.outputShiftId != null){
													newArrMorning.outputShiftId=value3.outputShiftId;
												}
												if(value3.intakeShiftId != null){
													newArrMorning.intakeShiftId=value3.intakeShiftId;
												}
												if(value3.startIntakeTime != null){
													newArrMorning.startIntakeTime=value3.startIntakeTime;
												}
												if(value3.startOutputTime != null){
													newArrMorning.startOutputTime=value3.startOutputTime;
												}
												if(value3.totalIntake != null){
													newArrMorning.totalIntake=value3.totalIntake;
												}
												if(value3.totalOutput != null){
													newArrMorning.totalOutput=value3.totalOutput;
												}
												if(value3.startIntakeQuantity != null){
													newArrMorning.startIntakeQuantity=value3.startIntakeQuantity;
												}
												if(value3.totalOutput != null){
													newArrMorning.startOutputQuantity=value3.startOutputQuantity;
												}
												$scope.sortedListMorning.push(newArrMorning);
											}
                                             if(value3.shift== "Evening" || value3.shift== "evening"){
                                            	 var newArrEvening={};
                                            	 newArrEvening.shift=value3.shift;
												if(value3.intakeFlag != null){
													newArrEvening.intakeFlag=value3.intakeFlag;
												}
												if(value3.outputFlag != null){
													newArrEvening.outputFlag=value3.outputFlag;
												}
												if(value3.outputShiftId != null){
													newArrEvening.outputShiftId=value3.outputShiftId;
												}
												if(value3.intakeShiftId != null){
													newArrEvening.intakeShiftId=value3.intakeShiftId;
												}
												if(value3.startIntakeTime != null){
													newArrEvening.startIntakeTime=value3.startIntakeTime;
												}
												if(value3.startOutputTime != null){
													newArrEvening.startOutputTime=value3.startOutputTime;
												}
												if(value3.totalIntake != null){
													newArrEvening.totalIntake=value3.totalIntake;
												}
												if(value3.totalOutput != null){
													newArrEvening.totalOutput=value3.totalOutput;
												}
												if(value3.startIntakeQuantity != null){
													newArrEvening.startIntakeQuantity=value3.startIntakeQuantity;
												}
												if(value3.totalOutput != null){
													newArrEvening.startOutputQuantity=value3.startOutputQuantity;
												}
												$scope.sortedListEvening.push(newArrEvening);
											}
											if(value3.shift== "Night" || value3.shift== "night"){
												 var newArrNight={};
												 newArrNight.shift=value3.shift;
												if(value3.intakeFlag != null){
													newArrNight.intakeFlag=value3.intakeFlag;
												}
												if(value3.outputFlag != null){
													newArrNight.outputFlag=value3.outputFlag;
												}
												if(value3.outputShiftId != null){
													newArrNight.outputShiftId=value3.outputShiftId;
												}
												if(value3.intakeShiftId != null){
													newArrNight.intakeShiftId=value3.intakeShiftId;
												}
												if(value3.startIntakeTime != null){
													newArrNight.startIntakeTime=value3.startIntakeTime;
												}
												if(value3.startOutputTime != null){
													newArrNight.startOutputTime=value3.startOutputTime;
												}
												if(value3.totalIntake != null){
													newArrNight.totalIntake=value3.totalIntake;
												}
												if(value3.totalOutput != null){
													newArrNight.totalOutput=value3.totalOutput;
												}
												if(value3.startIntakeQuantity != null){
													newArrNight.startIntakeQuantity=value3.startIntakeQuantity;
												}
												if(value3.totalOutput != null){
													newArrNight.startOutputQuantity=value3.startOutputQuantity;
												}
												$scope.sortedListNight.push(newArrNight);
											}
											if(value3.shift== "General" || value3.shift== "general"){
												 var newArrGeneral={};
												 newArrGeneral.shift=value3.shift;
												if(value3.intakeFlag != null){
													newArrGeneral.intakeFlag=value3.intakeFlag;
												}
												if(value3.outputFlag != null){
													newArrGeneral.outputFlag=value3.outputFlag;
												}
												if(value3.outputShiftId != null){
													newArrGeneral.outputShiftId=value3.outputShiftId;
												}
												if(value3.intakeShiftId != null){
													newArrGeneral.intakeShiftId=value3.intakeShiftId;
												}
												if(value3.startIntakeTime != null){
													newArrGeneral.startIntakeTime=value3.startIntakeTime;
												}
												if(value3.startOutputTime != null){
													newArrGeneral.startOutputTime=value3.startOutputTime;
												}
												if(value3.totalIntake != null){
													newArrGeneral.totalIntake=value3.totalIntake;
												}
												if(value3.totalOutput != null){
													newArrGeneral.totalOutput=value3.totalOutput;
												}
												if(value3.startIntakeQuantity != null){
													newArrGeneral.startIntakeQuantity=value3.startIntakeQuantity;
												}
												if(value3.totalOutput != null){
													newArrGeneral.startOutputQuantity=value3.startOutputQuantity;
												}
												$scope.sortedListGeneral.push(newArrGeneral);
											}
											
											
											
											//code for Morning Shift
											$scope.calculatedTotalIntakeMorning=0;
											$scope.calculatedTotalOutputMorning=0;
											$scope.calculatedtotalMorning=[];
											var newList={};
											var newList1={};
											$scope.sum=0;
											 $scope.sum1 =0;
											 $scope.sum2 = 0;
											angular.forEach($scope.sortedListMorning, function(value , index) {
													if(value.startIntakeQuantity != null){
														$scope.calculatedTotalIntakeMorning += parseInt(value.startIntakeQuantity);
														newList.calculatedTotalIntakeMorning=$scope.calculatedTotalIntakeMorning;
														newList.intakeFlag=value.intakeFlag;
														newList.intakeShiftId=value.intakeShiftId;
														newList.startIntakeTime=value.startIntakeTime;
													}
													if(value.startOutputQuantity != null){
														$scope.calculatedTotalOutputMorning += parseInt(value.startOutputQuantity);
														newList1.calculatedTotalOutputMorning=$scope.calculatedTotalOutputMorning;
														newList1.outputFlag=value.outputFlag;
														newList1.outputShiftId=value.outputShiftId;
														newList1.startOutputTime=value.startOutputTime;
													}
													 
													});
											
											 $scope.sum = $scope.calculatedTotalIntakeMorning -  $scope.calculatedTotalOutputMorning;
												$scope.calculatedtotalMorning.push(newList);
												$scope.calculatedtotalMorning.push(newList1);
												console.log( "morning sum",$scope.sum);
												
											//code for Evening Shift
											$scope.calculatedTotalIntakeEvening=0;
											$scope.calculatedTotalOutputEvening=0;
											$scope.calculatedtotalEvening=[];
											var newList={};
											var newList1={};
											 $scope.sumEvening=0;
											 $scope.sumEvening1 =0;
											 $scope.sumEvening2 = 0;
												angular.forEach($scope.sortedListEvening, function(value , index) {
													if(value.startIntakeQuantity != null){
														$scope.calculatedTotalIntakeEvening += parseInt(value.startIntakeQuantity);
														newList.calculatedTotalIntakeEvening=$scope.calculatedTotalIntakeEvening;
														newList.intakeFlag=value.intakeFlag;
														newList.intakeShiftId=value.intakeShiftId;
														newList.startIntakeTime=value.startIntakeTime;
													}
													if(value.startOutputQuantity != null){
														$scope.calculatedTotalOutputEvening += parseInt(value.startOutputQuantity);
														newList1.calculatedTotalOutputEvening=$scope.calculatedTotalOutputEvening;
														newList1.outputFlag=value.outputFlag;
														newList1.outputShiftId=value.outputShiftId;
														newList1.startOutputTime=value.startOutputTime;
													}
													 
													});
												 $scope.sumEvening = $scope.calculatedTotalIntakeEvening -  $scope.calculatedTotalOutputEvening;
												$scope.calculatedtotalEvening.push(newList);
												$scope.calculatedtotalEvening.push(newList1);
												console.log( "evening sum",$scope.sumEvening);
												
												
											//code for Nigth Shift
										$scope.calculatedTotalIntake=0;
										$scope.calculatedTotalOutput=0;
										$scope.calculatedtotal=[];
										var newList={};
										var newList1={};
										$scope.sumNight=0;
										 $scope.sumNight1 =0;
										 $scope.sumNight2 = 0;
											angular.forEach($scope.sortedListNight, function(value , index) {
												if(value.startIntakeQuantity != null){
													$scope.calculatedTotalIntake += parseInt(value.startIntakeQuantity);
													newList.calculatedTotalIntake=$scope.calculatedTotalIntake;
													newList.intakeFlag=value.intakeFlag;
													newList.intakeShiftId=value.intakeShiftId;
													newList.startIntakeTime=value.startIntakeTime;
												}
												if(value.startOutputQuantity != null){
													$scope.calculatedTotalOutput += parseInt(value.startOutputQuantity);
													newList1.calculatedTotalOutput=$scope.calculatedTotalOutput;
													newList1.outputFlag=value.outputFlag;
													newList1.outputShiftId=value.outputShiftId;
													newList1.startOutputTime=value.startOutputTime;
												}
									
												});
											 $scope.sumNight = $scope.calculatedTotalIntake -  $scope.calculatedTotalOutput;
											$scope.calculatedtotal.push(newList);
											$scope.calculatedtotal.push(newList1);
											console.log( "night sum",$scope.sumNight);
											
											
											
											//code for General Shift
											$scope.calculatedTotalIntakeGeneral=0;
											$scope.calculatedTotalOutputGeneral=0;
											$scope.calculatedtotalGeneral=[];
											var newList={};
											var newList1={};
											$scope.sumGeneral=0;
											 $scope.sumGeneral1 =0;
											 $scope.sumGeneral2 = 0;
											angular.forEach($scope.sortedListGeneral, function(value , index) {
													if(value.startIntakeQuantity != null){
														$scope.calculatedTotalIntakeGeneral += parseInt(value.startIntakeQuantity);
														newList.calculatedTotalIntakeGeneral=$scope.calculatedTotalIntakeGeneral;
														newList.intakeFlag=value.intakeFlag;
														newList.intakeShiftId=value.intakeShiftId;
														newList.startIntakeTime=value.startIntakeTime;
													}
													if(value.startOutputQuantity != null){
														$scope.calculatedTotalOutputGeneral += parseInt(value.startOutputQuantity);
														newList1.calculatedTotalOutputGeneral=$scope.calculatedTotalOutputGeneral;
														newList1.outputFlag=value.outputFlag;
														newList1.outputShiftId=value.outputShiftId;
														newList1.startOutputTime=value.startOutputTime;
													}
													 
													});
											
											 $scope.sumGeneral = $scope.calculatedTotalIntakeGeneral -  $scope.calculatedTotalOutputGeneral;
												$scope.calculatedtotalGeneral.push(newList);
												$scope.calculatedtotalGeneral.push(newList1);
												console.log( "morning sum",$scope.sum);
											
											
											});
											 $scope.patientDetailsOfIOchartHistory[index1].calculatedtotalMorning=$scope.calculatedtotalMorning;
											 $scope.patientDetailsOfIOchartHistory[index1].calculatedtotalEvening=$scope.calculatedtotalEvening;
											 $scope.patientDetailsOfIOchartHistory[index1].calculatedtotal=$scope.calculatedtotal;
											 $scope.patientDetailsOfIOchartHistory[index1].calculatedtotalGeneral=$scope.calculatedtotalGeneral;
											 $scope.patientDetailsOfIOchartHistory[index1].sum= $scope.sum;
											 $scope.patientDetailsOfIOchartHistory[index1].sumEvening= $scope.sumEvening;
											 $scope.patientDetailsOfIOchartHistory[index1].sumNight= $scope.sumNight;
											 $scope.patientDetailsOfIOchartHistory[index1].sumGeneral= $scope.sumGeneral;
											 
											 
											
										});
										
										
										
										
										
										
										console.log("sorted list Morning",$scope.sortedListMorning);
										console.log("sorted list Evening",$scope.sortedListEvening);
										console.log("sorted list Night",$scope.sortedListNight);
										console.log("sorted list General",$scope.calculatedtotalGeneral);
										console.log("calculatedtotal",$scope.calculatedtotal);
										console.log("calculatedtotalEvening",$scope.calculatedtotalEvening);
										console.log("calculatedtotalMorning",$scope.calculatedtotalMorning);
										$rootScope.stopLoader();
										}
									else{
										$rootScope.stopLoader();
									}
									
								});
							}
							/*
							  $rootScope.startLoaderPopup();
							  $rootScope.stopLoaderPopup();*/

							
							$scope.searchIntakeChartHistory = function(info,data)
							{
								
								console.log("info",info);
								if(info.intakeShiftId != null){
									$scope.shiftId=info.intakeShiftId;
								}else{
									 growl.error("No history found",
												{
													title : 'Error!'
												});
									 $rootScope.stopLoaderPopup();
										return false;
								}
								$rootScope.startLoaderPopup();
								 var b = moment(data.intakeDate,"DD-MM-YYYY").toDate();
								 var getB=moment(b).format('DD-MM-YYYY');
                               var data = {
                            		    intakeDate:getB,
//                            		    startIntakeTime:info.startIntakeTime,
                            		    shiftId:$scope.shiftId,
                            		    patientId:$scope.patientDetails.patientId,
                            		    organizationId:$scope.organizationId,
                            		    unitId : $scope.unitId
								  }
                        
								var URI = BASE_URL + ROOT_URL + "/api/opd/getIntakeHistoryList";
								BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									$scope.searchPatientList = [];
									if (response.data.status == "success")
										{
										$scope.intakeChartHistory=[];
										$scope.intakeChartHistory = response.data.listObject;
										
										$scope.balanceIntake= 0
										angular.forEach($scope.intakeChartHistory, function(value , index) {
										$scope.balanceIntake += parseInt(value.startIntakeQuantity);
										});
										$rootScope.stopLoaderPopup();
										}
									else{
										$rootScope.stopLoaderPopup();
									}
									
								});
							}
							
							$scope.searchOutputChartHistory = function(info,data)
							{
								
								console.log("info",info);
								if(info.outputShiftId != null){
									$scope.shiftId=info.outputShiftId;
								}
								else
								{
									growl.error("No history found",
											{
												title : 'Error!'
											});
									$rootScope.stopLoaderPopup();
									return false;
								}
								$rootScope.startLoaderPopup();
								 var b = moment(data.intakeDate,"DD-MM-YYYY").toDate();
								 var getB=moment(b).format('DD-MM-YYYY');
                               var data = {
                            		    intakeDate:getB,
//                            		    startOutputTime:info.startOutputTime,
                            		    shiftId:$scope.shiftId,
                            		    patientId:$scope.patientDetails.patientId,
                            		    organizationId:$scope.organizationId,
                            		    unitId : $scope.unitId
								  }
								var URI = BASE_URL + ROOT_URL + "/api/opd/getOutputHistoryList";
								BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									$scope.searchPatientList = [];
									if (response.data.status == "success")
										{
										$scope.outputChartHistory=[];
										$scope.outputChartHistory = response.data.listObject;
										$scope.balanceOutput= 0
										angular.forEach($scope.outputChartHistory, function(value , index) {
										$scope.balanceOutput += parseInt(value.startOutputQuantity);
										});
										console.log("$scope.outputChartHistory",$scope.outputChartHistory);
										$rootScope.stopLoaderPopup();
										}
									else{
										$rootScope.stopLoaderPopup();
									}
									
								});
							}
							
							
						   
								}else{
									 growl.warning(
												'Patient Accessible to Doctor Only !!',
												{
													title : 'Success!'
												});
								}
					           
				
						} ]);