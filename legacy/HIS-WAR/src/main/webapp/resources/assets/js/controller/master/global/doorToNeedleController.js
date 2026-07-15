/**
 * @author sneha dalave
 */

(function () {
	  "use strict";
						function doorToNeedleController($scope, $rootScope, $state, $cookies,
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
								
								
						    $scope.initPatient = function()
						     {
							    $scope.list= [];
						       }
						 $scope.initPatient();
							$scope.searchGroupListPatient = function(keyword) {

								var data = {
									firstName : keyword,
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								}

								var URI = BASE_URL + ROOT_URL
										+ GETPATIENTBYKEYWORD;
								return GenericService.serviceAction("POST",
										URI, data).then(function(response) {

									if (response.data.status == "success") {
										return response.data.listObject;

									}
								});

							}
							
							$scope.selectPatientIdOnSearch = function(info, model,
									label) {
									if(info.patientId != null)
									$scope.patientId  = info.patientId;
									if(info.tPatientId !=null)
									$scope.tPatientId = info.tPatientId;
							}
							
							
						
							$scope.patientList=[];
							$scope.patientDetails.visitTypeId = 1
							$scope.getPatientDetailsByIndex = function(index){}	
							
							// Date For From Date on Search
							
							$scope.flagDigByGen=false;
							$scope.searchGroupList = function(keyword,index) {
								  $scope.DiagnosisByGenderList=[];
										
										var data = {
												organizationId : $scope.organizationId,
												diagnosisName : keyword,
												};
										
										console.log(data);
										$scope.varOr = "|";
										var URI = BASE_URL + ROOT_URL
												+ "/api/opd/getActiveEmergencyDiagnosis";
										
										return GenericService.serviceAction("POST", URI,data)
												.then(
														function(response) {
															console.log(response);
															// $rootScope.startLoader();
															if (response.data.status == "success")
																 $scope.DiagnosisByGenderList = response.data.listObject;
															if($scope.DiagnosisByGenderList.length == 0){
																
																$scope.flagDigByGen=true;
																var newArr = {};
																newArr.diagnosisName="Diagnosis not applicable";
																newArr.diagnosisId='';
																$scope.DiagnosisByGenderList.push(newArr);
																$scope.varOr="";
																return response.data.listObject= $scope.DiagnosisByGenderList;
															} else{
																$scope.flagDigByGen=false;
															}
																return response.data.listObject;
																
														});

									}
							
							
							// Selected TypeHead on Diagnosis
							$scope.selectDignosisIdForDiagnosis = function(info, model,
									label,ind) {
								
								console.log(info);
								$scope.diagnosisId = info.diagnosisId;
								$scope.diagonisIdforTemplate = $scope.dataListDiagnosis[ind].diagnosisId;
								$scope.selectedOrderSet = [];
							}
							
							$scope.openDatePicker = function() {
								$scope.datepicker.opened = true;
							};

							$scope.datepicker = {
									opened : false
							};
							
//							$scope.fromDatepicker = new Date();
//							$scope.toDatepicker = new Date();
							var date = new Date();
							 $scope.dateOptionsFromDate = {
									formatYear : 'yyyy',
									showWeeks : false,
									maxDate : new Date( ),
									minDate : date.setDate((new Date()).getDate() - 90),
									
								};
							var dateOptionsTodate = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : new Date(),
									
								};
							$scope.openFromDatePicker = function() {
								$scope.fromDatepickerOpen.opened = true;
								
							};

							$scope.fromDatepickerOpen = {
									opened : false
							};
				
							$scope.openToDatePicker = function() {
								$scope.toDatepickerOpen.opened = true;
								
							};

							$scope.toDatepickerOpen = {
									opened : false
							};
							
							 $scope.openDatePickerSearch = function() {
									$scope.datepicker2.opened = true;
									};

									$scope.datepicker2 = {
									opened : false
									};
							
							
							$scope.searchPatientDetailsOfDiagnosisFromAdmission = function()
							{
								$rootScope.startLoader();
								if($scope.fromDatepicker != undefined){
									$scope.from = moment($scope.fromDatepicker).format('YYYY-MM-DD');
								}else{
									$scope.from=null;
								}
								
                               if($scope.toDatepicker != undefined){
                            	   $scope.to = moment($scope.toDatepicker).format('YYYY-MM-DD');
								}else{
									$scope.to=null;
								}
                               var data = {
                            		    fromDate:$scope.from,
                            		    toDate:$scope.to,
                            		    patientId:$scope.patientId,
                            		    diagnisisId:$scope.diagnosisId,
                            		    unitId : $scope.unitId,
								  }
								console.log("data",data);
                        
								var URI = BASE_URL + ROOT_URL + "/api/opd/doorToNiddle";
								BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success")
										{
										$scope.list = response.data.listObject;
										console.log("data saved",$scope.list);
										angular.forEach($scope.list ,function(value,key){
											angular.forEach(value.patientList ,function(value1,key1){
												angular.forEach(value1.medicationDetails ,function(value2,key2){
													if(value2.administrationTime != null){
														var startDate= new Date(value2.admissionTime); 
												         
												         var startMiliSecond = startDate.getMilliseconds();
												         
												         startDate = startDate - startMiliSecond;

												         var endDate= new Date(value2.administrationTime);
												         
												         var endMiliSecond= endDate.getMilliseconds();
												         
												         endDate = endDate - endMiliSecond;
												      
												         var milisecondsDiff = endDate-startDate;
												        
												         value2.difference= Math.round((milisecondsDiff/1000)/60);
													}
												});
											});
										});
										$rootScope.stopLoader();
										
										}
									
								});
							}
							$scope.searchPatientDetailsOfDiagnosisFromAdmission();
							
							$scope.getInformation = function(info)
							{
								$rootScope.startLoader();
                               var data = {
                            		   "admissionId": info.admissionId
								  }
								console.log("data",data);
                        
								var URI = BASE_URL + ROOT_URL + "/adt/getAdmisssoinTAT";
								BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success")
										{
										$scope.informationOfTransaction = [];
										$scope.informationOfTransaction = response.data.listObject;
										console.log("data saved",$scope.list);
										$rootScope.stopLoader();
										}
									
								});
							}
				
						} 
						function config($stateProvider) {
						    $stateProvider.state("doorToNeedle",
						    		{
						   	 url: '/doorToNeedle',
							 templateUrl: 'views/master/GlobalMaster/door_to_needle.html',
							 controller: "doorToNeedleController",
							 title: 'Door To Needle',
                                 });	
							  }
							
							  angular
							    .module("myApp")
							    .config(config)
							    .controller("doorToNeedleController", doorToNeedleController);
							})();
