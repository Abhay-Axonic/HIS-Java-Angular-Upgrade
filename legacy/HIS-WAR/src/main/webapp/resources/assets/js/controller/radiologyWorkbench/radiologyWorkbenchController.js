'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:radiologyWorkbenchController
 * @description #radiologyWorkbenchController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'radiologyWorkbenchController',
				[
						'$scope',
						'$cookies',
						'$rootScope',
						'GenericService',
						'BillingGenericService',
						'$state',
						'$timeout',
						'$interval',
						'GenericServiceParamHeader',
						'$compile',
						'reportService',
						'PaginationService',
						'PagerService',
						'growl',
						'$window',
						function($scope, $cookies, $rootScope, GenericService,
								BillingGenericService, $state, $timeout,
								$interval, GenericServiceParamHeader,$compile,reportService,PaginationService,PagerService,growl,$window) {

							//alert("radiologyWorkbenchController");
							var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
							 if(cookieObjectAppConfig == undefined){
							  $state.go('login');
							  return;
							 }
							 else
							  {
							   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
							   
							  }
							 var self = this;
							 
							$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
							
							if(cookieObjectAppConfig.radiologySpecialityId!=null || cookieObjectAppConfig.radiologySpecialityId !="" || cookieObjectAppConfig.radiologySpecialityId!=undefined)
							{
								$scope.radiologySpecialityId = cookieObjectAppConfig.radiologySpecialityId;
							}else{
								growl.warning("Radiology department is not configured...!!",{
									title : 'Warning!'
								});
								return false;
							}
							if (cookieObjectAppConfigSecond != undefined) {
							$scope.mandatoryScan = cookieObjectAppConfigSecond.mandatoryScan;
							}
							/* init() function for form object create. */
							$scope.init = function() {

								$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,
									maxDate : new Date()
								};
								$scope.todateOptions =
								{
									formatYear : 'yyyy',
									showWeeks : false,
									maxDate : new Date(),
									minDate : new Date(),
								};

								
								$scope.orderSchDateOptions =
								{
									formatYear : 'yyyy',
									showWeeks : false,
								};

								//Date Object For Search DOB
								$scope.openDatePickerSearch = function() {
									$scope.datepickerSearch.opened = true;
								};

								$scope.datepickerSearch = {
									opened : false
								};

								//Date Object For Reshedule
								$scope.openDatePickerReschedule = function() {
									$scope.datepickerReschedule.opened = true;
								};

								$scope.datepickerReschedule = {
									opened : false
								};

								//Date For From Date on Search

								$scope.openDatePickerFromDate = function() {
									$scope.datepickerFromDatef.opened = true;
								};

								$scope.datepickerFromDatef = {
									opened : false
								};

								//Date For Schedule Date on Search

								$scope.openDatePickerorderSchDate = function() {
									$scope.orderSchDated.opened = true;
								};

								$scope.orderSchDated = {
									opened : false
								};

								$scope.openDatePickerToDate = function() {
									$scope.datepickerToDatet.opened = true;
								};

								$scope.datepickerToDatet = {
									opened : false
								};

								
								$scope.datepickerFromDate = new Date();
								$scope.datepickerToDate = new Date();
								
								$scope.onOrderSchDate = function() {
									$scope.datepickerFromDate = undefined;
									$scope.datepickerToDate = undefined;
								};
								$scope.onOrderDate = function() {
									$scope.orderSchDate = undefined;
								};

								$scope.dateOptionsSlot = {
										formatYear : 'yyyy',
										showWeeks : false,
										minDate : new Date()
									};
								
									$scope.openDatePicker = function() {
										$scope.datepicker.opened = true;
									};

									$scope.datepicker = {
										opened : false
									};
									
									$scope.datepicker = new Date();

								var cookieObject = $cookies
										.getObject('cookieObject');
								if (cookieObject == undefined) {
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.roleId = 2;
								
								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								$scope.isBillManipulationAllowedUserLevel = cookieObject.isBillManipulationAllowedUserLevel;
								$scope.isBillManipulationAllowedAppLevel = cookieObjectAppConfigSecond.isBillManipulationAllowedAppLevel;
								$scope.isBillManipulationAllowed = ($scope.isBillManipulationAllowedUserLevel && $scope.isBillManipulationAllowedAppLevel =='Y') ? true : false;
								if($scope.isBillManipulationAllowed == true){
									$scope.filterDummyService = 1;
								}else{
									$scope.filterDummyService = 2;
								}
								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup

								$scope.searchKeyword ="";
								//start radiology
								$scope.visitTypeList = [ {
									id : 3,
									label : " Day Care"
								}, {
									id : 4,
									label : " ER"
								}, {
									id : 1,
									label : " OP "
								}, {
									id : 2,
									label : " IP "
								} ];
								$scope.selectedVisitType = [];
								$scope.selectedModalityList = [];
								
								$scope.departmentList= [];
								$scope.selectedDepartMentList = [];
								
								 $scope.pagination = {
					                        currentPage: 1,
					                        totalPages: 0,
					                        pageSize: '10',
					                        search: '',
					                        searchData: PaginationService.searchData,
					                        setTotalPages: PaginationService.setTotalPages,
					                        getPagesList: PaginationService.getPagesList,
					                        setPage: PaginationService.setPage,
					                        setFirstPage: PaginationService.setFirstPage,
					                        setPreviousPage: PaginationService.setPreviousPage,
					                        setNextPage: PaginationService.setNextPage,
					                        setLastPage: PaginationService.setLastPage,
					                        sortBy: PaginationService.sortBy
					                      }
								 
								var commonObj = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId
									};
								
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEMODALITYLIST;
								GenericService
								.serviceAction("POST", URI, commonObj)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.modalityList = [];
												angular.forEach(response.data.listObject,
																function(value,index) {
																	var data = {};
																	data.id = value.modalityId;
																	data.label = value.modalityDesc;
																	$scope.modalityList.push(data);
																});
												console.log("ActiveModalityMasterList",$scope.modalityList);
											}
										});	

								//end radiology 
								   $scope.multiselectObj = {
								    		"id":"",
								    		"label":""
								    }
								    
								$scope.getDepartMentList = function(orgId,orgUnitId) {
									try {
										


								    	 var specialityIDS = [];
								    	 specialityIDS.push($scope.radiologySpecialityId);
											
								         var data = {
								       		  organizationId:$scope.organizationId,
								       		  unitId:$scope.unitId,
								       		  specialityIdList:specialityIDS
								         }
								         console.log(data);
								         var URI = BASE_URL + ROOT_URL+ "/adt/subspeciality/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/specialityId/"+$scope.radiologySpecialityId;
									BillingGenericService
											.serviceAction("GET", URI,
													"",
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														console.log(response);
														// $rootScope.startLoader();
														if (response.data.status == "success")
															{
															$scope.departmentList = [];
															$scope.subSpecialityMaterList = response.data.listObject;
															angular.forEach($scope.subSpecialityMaterList, function(value, key) {
																var multiselectObj = angular.copy($scope.multiselectObj);
																multiselectObj.id = value.subSpecialityMasterId;
																multiselectObj.label = 	value.subSpecialityMasterName;
																	$scope.departmentList.push(multiselectObj);
																});
															
															}
															
													});
									   } catch (e) {
										console.log(e.message)
									}
								}
								$scope.getDepartMentList($scope.organizationId,$scope.unitId);
								var data = {organizationId : $scope.organizationId};
								var headerObj = {
									organization_id : $scope.organizationId,
									unit_id : $scope.unitId
								}
								var paramObj = {};

							

								//For Get Active Reason List
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEREASONLIST;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.reasonList = [];

													if (response.data.status == "success") {
														$scope.reasonList = response.data.listObject;

													}
												});

								$scope.encounterSearchObject = {
									'patientId' : 0,
									'organizationId' : $scope.organizationId,
									'unitId' : $scope.unitId,
									'serviceId':0
								}
								
							    var specDocObj = {
						    			organizationId:$scope.organizationId,
						    			userId:$scope.userId
						        }
							    var URI = BASE_URL + ROOT_URL+ "/adt/getSpecialityListByUserIdByUnitId";
						        BillingGenericService.serviceAction(METHOD_POST, URI,specDocObj,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
												if (response.data.status == "success"){
													$scope.staffSpecialityList = [];
													$scope.staffSubSpecialityList = [];
				                                    $scope.staffSpecialityList = response.data.listObject;
												    $scope.staffSubSpecialityList = response.data.object;
												    console.log("speciality",$scope.staffSpecialityList);
												    console.log("specialitySub",$scope.staffSubSpecialityList);
												}
			                                 });
							}

							$scope.init();

							
							 $scope.clinObj = {
										clinicalNote :"",
										showclinicalNote : ""
								}
							 
							 $scope.comorbidityFlag = false;
								$scope.getPatientDiagnosis = function(datap) {
									
									 $scope.comorbidityFlag = false;
									var data = {};
									var headerObjDia = {
										'unit_id' : $scope.unitId,
										'organization_id' : $scope.organizationId,
										'patient_id' : datap.patientId,
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
														console.log( "patientDiagnosisList", $scope.patientDiagnosisList);
														 $scope.complaintDelete=true;
														if($scope.patientDiagnosisList.length > 0)
														{
														  $scope.complaintDelete=false;
														  $scope.comorbidityFlag = true;
														}
														
														if(datap.visitTypeId == 1){
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
														}else{
															 $scope.finalString="";
					                                         angular.forEach($scope.patientDiagnosisList,function(value,index){
					                                        	 if(index == 0){
					                                        		 if(value.type == 1){
					                                        			 var ext =  "(Prov)";
					                                        		 }else{
					                                        			 var ext =  "(F)";
					                                        		 }
					                                        		 if(value.diagnosisType == 1){
						                                        			var ext2 = "(P)" ;
						                                        		 }else{
						                                        			 var ext2 = "(S)" ;
						                                        		 }
					                                        		 
					                                        	   var String = value.diagnosisName +""+ext+"" +ext2;
					                                        	   $scope.finalString = $scope.finalString.concat(String);
					                                        	 }
					                                        	 else{
					                                        		 if(value.type == 1){
					                                        			 var ext =  "(Prov)";
					                                        		 }else{
					                                        			 var ext =  "(F)";
					                                        		 }
					                                        		 if(value.diagnosisType == 1){
						                                        			var ext2 = "(P)" ;
						                                        		 }else{
						                                        			 var ext2 = "(s)" ;
						                                        		 }
					                                        		   var String = value.diagnosisName +""+ext+"" +ext2;
						                                        	   $scope.finalString = $scope.finalString.concat(" , "+String);
					                                        	 }
					                                         });
					                                      
														}
				                                         
													});
								}
								
								
								$scope.showClinicalNote = function(item) {
									
									$scope.clinObj.showclinicalNote =item.ordRemarks;
									$scope.getPatientDiagnosis(item);
								}
								
								$scope.myFunction = function(){
                                    //document.getElementById("myTextarea").readOnly = "true";
                                  var textarea = document.getElementById("myTextArea");
                                  textarea.disabled = true;
                                  }
						    
							$scope.radSpecialityId = 0;
							$scope.radSubSpecialityId = 0;
							
							$scope.selectedSpecialityId="";
							$scope.getModalityBySubSpeciality = function(item) {
								
							console.log($scope.nephrologySpecialityId)
								$scope.slotCheckTime = moment(new Date()).format("HH:mm");
								$scope.fromTimeString = "";
								$scope.modalityMasterList = [];
								$scope.admissionId = item.admissionId;
								$scope.radSpecialityId = item.specialityId;
								$scope.radSubSpecialityId = item.subSpecialityId;
								$scope.orderDetailsId = item.orderDetailsId;
								$scope.patientId = item.patientId;
								$scope.visitTypeIdSlot = item.visitTypeId;

								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									specialityId : item.specialityId,
									subSpecialityId :item.subSpecialityId
								}
								console.log(data);
								//$rootScope.startLoader();
								//return false
								if(item.specialityId==cookieObjectAppConfig.hemodialysisSpecialityId){
									$scope.selectedSpecialityId=item.specialityId;
									$scope.modalityMasterList= null;
									var dataObj = {
											unitId : $scope.unitId,
											organizationId : $scope.organizationId
									};
									console.log(data);
									var URI = BASE_URL + ROOT_URL
									+ "/api/adt/getDialysisChair";								
									GenericService
									.serviceAction("POST", URI, dataObj)
							.then(
									function(response) {
										console.log(response);
										$scope.bedList = [];
										if (response.data.status == "success")
											$scope.bedList = response.data.listObject;
										console.log($scope.chairList)
									});
								}
								
								
								if(item.specialityId!=cookieObjectAppConfig.hemodialysisSpecialityId){
									$scope.selectedSpecialityId=item.specialityId;
									$scope.bedList = null;
									var URI = BASE_URL + ROOT_URL
										+ GETMODALITYBYSUBSPECIALITYID;
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													$scope.modalityMasterList = [];
													if (response.data.status == "success") {
														$scope.modalityMasterList = response.data.listObject;
													 console.log($scope.modalityMasterList)
													}

												});
								}
							}
								
							
							//For Date Difference For Modality Start
					       	$scope.daysDiffrence = function(date) {
					       	    var today = new Date();
					       	    var dd = today.getDate();
					       	    var mm = today.getMonth() + 1; //January is 0!
					       	    var yyyy = today.getFullYear();
					       	    if (dd < 10) {
					       	      dd = '0' + dd
					       	    }
					       	    if (mm < 10) {
					       	      mm = '0' + mm
					       	    }
					       	    today = yyyy + '/' + mm + '/' + dd;
					       	    $scope.today = today;
					       	    var date2 = new Date(today);
					       	    var date1 = new Date(date);
					       	    var timeDiff = Math.abs(date2.getTime() - date1.getTime());
					       	    $scope.dayDifference = Math.ceil(timeDiff / (1000 * 3600 * 24));
					       	    return $scope.dayDifference;
					       	  }
					       	//For Date Difference for Modality END
							//Function For Get Date Wise Slots
							  $scope.itemId="";
							  $scope.itemObj={};
							  $scope.globleObj={};
							  $scope.loadingMsg="";
							$scope.getSlotsOfDate = function(item) {
								$scope.slotDetailsList = [];
								$scope.loadingMsg="Loading....";
								if(item!=undefined){
									$scope.itemObj=JSON.parse(item);
									$scope.globleObj=JSON.parse(item);
								}else{
									$scope.itemObj=$scope.globleObj;
								}
								console.log(item)
								
								
								$scope.mobilityId="";
								$scope.itemId= $scope.itemObj.itemId;
								$scope.mobilityId=$scope.itemObj.modalityId
								console.log($scope.itemObj.itemId);
								$scope.slotDetailsList = [];
								$scope.fromTimeString = "";
								 
								if($scope.itemObj.bedId > 0 || $scope.mobilityId > 0)
								{
								var dt = moment($scope.datepicker).format('YYYY-MM-DD');
								$scope.diffDaysMod = $scope.daysDiffrence(moment($scope.datepicker).format('YYYY/MM/DD'));
									var data = {
										subSpecialityId : $scope.radSubSpecialityId,
										specialityId : $scope.radSpecialityId,
										appointmentDate : dt,
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										visitTypeId : $scope.visitTypeIdSlot
										
									};
									
									if($scope.itemObj.bedId!=null && $scope.itemObj.bedId!=undefined){
										console.log($scope.itemId)
										data.slotTypeId =8;
										data.bedId=$scope.itemObj.bedId;
									}
									else{
										
										data.slotTypeId =2;
										data.modalityId=$scope.mobilityId;	
									}
								console.log(data);
								//return false;
								var URI = BASE_URL + ROOT_URL + GETDAILYSLOTS;
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													
													if (response.data.status == "success") {
														$scope.slotDetailsList = response.data.listObject;
														if($scope.slotDetailsList.length==0){
															$scope.loadingMsg="Slots are not created for selected date...!";
														}
														console.log($scope.slotDetailsList);
						}

												});
							}
								else
									{
									growl.error("Please Select Modality",{
										title : ERROR_MSG
									});
									return false;
										
									}

							}
							$scope.clearDetails=function(){
								$scope.fromTimeString = "";
								$scope.fromTimeStringSlot="";
								$scope.slotId="";
								 $scope.modalityId="";
								 $scope.slotDetailsList=[];
								 $scope.loadingMsg="";
							}
							
							$scope.slotBooking = function(item)
							{
								$scope.fromTimeStringSlot = item.fromTimeString;
								$scope.fromTimeStringSlotShow = item.fromTimeString2;
								 $scope.slotId = item.slotId;
							}
							
                           $scope.saveAppointment = function(){
								
								if($scope.modalityId=="" || $scope.modalityId==undefined){
									growl.error("Please Select Modality",{
										title : ERROR_MSG
									});
									return false;
								}
								if($scope.slotId=="" || $scope.slotId==undefined){
									growl.error("Please Select Slots",{
										title : ERROR_MSG
									});
									return false;
								}
								
								$rootScope.startLoader();
								$scope.currentDate = new Date();
								$scope.currentDate = moment(
										$scope.currentDate).format(
										'DD-MM-YYYY HH:mm:ss');
								$scope.appointmentDate = moment(
										$scope.datepicker).format(
										'DD/MM/YYYY');
								var data = {
									slotId : $scope.slotId,
									remark : "",
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									createdDate : $scope.currentDate,
									updatedDate : $scope.currentDate,
									appointmentDate2 : $scope.appointmentDate,
									createdBy : $scope.userId,
									updatedBy : $scope.userId,
									status : 'A',
									slotStatusId : 4,
									priorityId : 1,
									allowedOverBookingSlots : 0,
									admissionId : $scope.admissionId,
									orderDetailsId:$scope.orderDetailsId,
									appointmentStatusId:1,
									patientId :$scope.patientId
								}
								if($scope.selectedSpecialityId==cookieObjectAppConfig.hemodialysisSpecialityId){
									data.appointmentTypeId=6;
								}else{
									data.appointmentTypeId=2;
								}
									
										var URI = BASE_URL + ROOT_URL
											+ "/api/opd/scheduleRadiologyAppointmentForInPatient";
								
								console.log(data);
								//return false; 

								BillingGenericService
										.serviceAction(METHOD_POST, URI,
												data,
												NOTIFICATION_MSG_STATUS_TRUE)
										.then(
												function(response) {
													console.log(response);
													if (response.data.status == "success") {
														angular.element('#info').modal('hide');
													     $rootScope.stopLoader();
														$scope.clearDetails();
														$scope.searchEncounter();
														$rootScope.stopLoader();
													}
													else if(response.data.status == "error") 
													{
															angular
															.element(
																	'#info')
															.modal(
																	'hide');
															$rootScope.stopLoader();
													}
												});
							}
							//Search Appointment List By Filters
                           
                       	//For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.searchEncounter();
							};
							
							$scope.searchEncounter = function() {
								
								console.log($scope.selectedVisitType);
								console.log($scope.selectedModalityList);
								var visitList =[];
								var modalityList =[];
								var deptList =[];
								
								angular.forEach($scope.selectedVisitType,
										function(value,index) {
									         visitList.push(value.id);
										});
								angular.forEach($scope.selectedModalityList,
										function(value,index) {
									      modalityList.push(value.id);
										});
								angular.forEach($scope.selectedDepartMentList,
										function(value,index) {
									      deptList.push(value.id);
										});
								$rootScope.startLoader();
								$scope.encounterSearchObject.fromDate = moment(
										$scope.datepickerFromDate).format(
										'YYYY-MM-DD');
								$scope.encounterSearchObject.toDate = moment(
										$scope.datepickerToDate).format(
										'YYYY-MM-DD');
								if($scope.orderSchDate != undefined && $scope.orderSchDate != null){
									$scope.encounterSearchObject.orderSchDate = moment($scope.orderSchDate).format('YYYY-MM-DD');
									$scope.encounterSearchObject.fromDate = null;
									$scope.encounterSearchObject.toDate  = null;
								}else{
									$scope.encounterSearchObject.orderSchDate = null;
								}
								$scope.encounterSearchObject.visitList = visitList;
								$scope.encounterSearchObject.modalityList = modalityList;
								$scope.encounterSearchObject.deptList = deptList;
								$scope.encounterSearchObject.radiologySpecialityId =$scope.radiologySpecialityId;
								$scope.encounterSearchObject.offset =$scope.offset;
								$scope.encounterSearchObject.noOfRecordsPerPage =$scope.noOfRecordsPerPage;
								
								console.log($scope.encounterSearchObject);

								var paramObj = {};
								var headers = {};
								if($scope.encounterSearchObject.patientId==0 || $scope.encounterSearchObject.patientId==undefined || $scope.encounterSearchObject.patientId==""){
									$scope.patientName = '';
									$scope.uhidNumber = '';
								}

								//return false;
//								var URI = BASE_URL + ROOT_URL
//										+ GETSEARCHEDLISTMODALITY;
								var URI = BASE_URL + ROOT_URL+ "/api/opd/RIS/getRadiologyWorkbenchWorklistPagination";
								GenericServiceParamHeader
										.serviceAction(METHOD_POST, URI,
												$scope.encounterSearchObject,
												NOTIFICATION_MSG_STATUS_FALSE,
												"", "")
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													$scope.pagination.data=[];
													$scope.allEncounterList = [];
													$scope.allEncounterListTest = [];
													$scope.allEncounterListForDummyFilter=[];
													if (response.data.status == "success" && response.data.listObject.length>0)
														{
														$scope.allEncounterListTest = response.data.listObject;
														
														if($scope.staffSubSpecialityList != null && $scope.staffSubSpecialityList.length > 0 ){
															angular.forEach($scope.allEncounterListTest, function(value, key) {
																angular.forEach($scope.staffSubSpecialityList ,function(value1,key1){
																	if(value.subSpecialityId == value1.subSpecialityMasterId){
																		$scope.allEncounterList.push(value);
																	}
																
																});		
															 });
														}else{
															$scope.allEncounterList = response.data.listObject;
														}
														
														$scope.allEncounterListForDummyFilter = $scope.allEncounterList;
								                        if($scope.isBillManipulationAllowed==false)
								                        {
								                        	$scope.allEncounterList = [];
								                            angular.forEach(response.data.listObject,function(obj,key)
								                             {
								                                if(obj.isDummyService==false)
								                                {
								                                	$scope.allEncounterList.push(obj);
								                                }
								                             });
								                        }
								                        $scope.filterByDummyServices();
														
														
														  if(!!response && !!response.data && response.data.listObject.length > 0){            
										                         PaginationService.searchData($scope.allEncounterList, $scope.pagination);
										                       }
//														    $scope.allEncounterList = response.data.listObject;
//															$scope.commonListCount = response.data.object;
//									            			$scope.pager = {};
//									            			$scope.page;
//									            			$scope.setPage(1,false);
														}
														$rootScope.stopLoader();
														$scope.encounterSearchObject.serviceId =0;
														});

							}
							$scope.searchEncounterMaster = function(orgId, offset, noOfRecordsPerPage) {
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
								
								console.log($scope.selectedVisitType);
								console.log($scope.selectedModalityList);
								var visitList =[];
								var modalityList =[];
								var deptList =[];
								
								angular.forEach($scope.selectedVisitType,
										function(value,index) {
									         visitList.push(value.id);
										});
								angular.forEach($scope.selectedModalityList,
										function(value,index) {
									      modalityList.push(value.id);
										});
								angular.forEach($scope.selectedDepartMentList,
										function(value,index) {
									      deptList.push(value.id);
										});
								
								$rootScope.startLoader();
								$scope.encounterSearchObject.fromDate = moment(
										$scope.datepickerFromDate).format(
										'YYYY-MM-DD');
								$scope.encounterSearchObject.toDate = moment(
										$scope.datepickerToDate).format(
										'YYYY-MM-DD');
								if($scope.orderSchDate != undefined && $scope.orderSchDate != null){
									$scope.encounterSearchObject.orderSchDate = moment($scope.orderSchDate).format('YYYY-MM-DD');
									$scope.encounterSearchObject.fromDate = null;
									$scope.encounterSearchObject.toDate  = null;
								}
								
								$scope.encounterSearchObject.visitList = visitList;
								$scope.encounterSearchObject.modalityList = modalityList;
								$scope.encounterSearchObject.deptList = deptList;
								$scope.encounterSearchObject.radiologySpecialityId =$scope.radiologySpecialityId;
								$scope.encounterSearchObject.offset = offset;
								$scope.encounterSearchObject.noOfRecordsPerPage = noOfRecordsPerPage;
								
								console.log($scope.encounterSearchObject);

								var paramObj = {};
								var headers = {};
								if($scope.encounterSearchObject.patientId==0 || $scope.encounterSearchObject.patientId==undefined || $scope.encounterSearchObject.patientId==""){
									$scope.patientName = '';
									$scope.uhidNumber = '';
								}

								//return false;
//								var URI = BASE_URL + ROOT_URL
//										+ GETSEARCHEDLISTMODALITY;
								var URI = BASE_URL + ROOT_URL+ "/api/opd/RIS/getRadiologyWorkbenchWorklistPagination";
								GenericServiceParamHeader
										.serviceAction(METHOD_POST, URI,
												$scope.encounterSearchObject,
												NOTIFICATION_MSG_STATUS_FALSE,
												"", "")
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													$scope.pagination.data=[];
													$scope.allEncounterList = [];
                                                    $scope.allEncounterListTest = [];
                                                    $scope.allEncounterListForDummyFilter=[];
													if (response.data.status == "success")
														{
	                                                    $scope.allEncounterListTest = [];
                                                        $scope.allEncounterListTest = response.data.listObject;
                                                        
                                                        if($scope.staffSubSpecialityList != null && $scope.staffSubSpecialityList.length > 0 ){
                                                            angular.forEach($scope.allEncounterListTest, function(value, key) {
                                                                angular.forEach($scope.staffSubSpecialityList ,function(value1,key1){
                                                                    if(value.subSpecialityId == value1.subSpecialityMasterId){
                                                                        $scope.allEncounterList.push(value);
                                                                    }
                                                                
                                                                });        
                                                             });
                                                        }else{
                                                            $scope.allEncounterList = response.data.listObject;
                                                        }
                                                        
                                                        $scope.allEncounterListForDummyFilter = $scope.allEncounterList;
								                        if($scope.isBillManipulationAllowed==false)
								                        {
								                        	$scope.allEncounterList = [];
								                            angular.forEach(response.data.listObject,function(obj,key)
								                             {
								                                if(obj.isDummyService==false)
								                                {
								                                	$scope.allEncounterList.push(obj);
								                                }
								                             });
								                        }
								                        $scope.filterByDummyServices();
                                                        if(!!response && !!response.data && response.data.listObject.length > 0){            
									                         PaginationService.searchData($scope.allEncounterList, $scope.pagination);
									                       }

														}else{
															
															growl.error(response.data.message,{
																title : ERROR_MSG
															});
															return false;
															
														}
														$rootScope.stopLoader();
														$scope.encounterSearchObject.serviceId =0;
															
														});

							
								


							}
							
							
							
					        $scope.filterByDummyServices= function()
					         {
					             $rootScope.startLoader();
					             $scope.allEncounterList =[]
					             if($scope.filterDummyService==1)//all 
					             {
					            	 $scope.allEncounterList = angular.copy($scope.allEncounterListForDummyFilter);
					             }else if($scope.filterDummyService==2)//real
					             {
					                 angular.forEach($scope.allEncounterListForDummyFilter, function (value, key) 
					                 {
					                     if(value.isDummyService==false)
					                    	 $scope.allEncounterList.push(value);
					                 });
					             }else if($scope.filterDummyService==3 )//dummy
					             {
					                 angular.forEach($scope.allEncounterListForDummyFilter, function (value, key) 
					                 {
					                     if(value.isDummyService==true)
					                    	 $scope.allEncounterList.push(value);
					                 });
					             }
					             PaginationService.searchData($scope.allEncounterList, $scope.pagination);
					             $rootScope.stopLoaderPopup();
					             $rootScope.stopLoader();
					         }

							

						/*	$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages) {
									return;
								}
								$scope.pager = PagerService.GetPager( $scope.commonListCount, page, $scope.noOfRecordsPerPage);
								if (flag) {
									$scope.searchEncounterMaster($scope.organizationId, $scope.pager.startIndex, $scope.pager.pageSize);
								}
							}*/
							
							
							$scope.searchEncounter();

							$scope.clearSearchObject = function() {
								$scope.encounterSearchObject = {
									'organizationId' : $scope.organizationId,
									'unitId' : $scope.unitId,
									'patientId' : 0,
									'serviceId' : 0
								}

								$scope.selectedVisitType = [];
								$scope.selectedModalityList = [];
								$scope.selectedDepartMentList = [];

								$scope.patientName = undefined;
								$scope.uhidNumber = undefined;
								$scope.search = undefined;
								$scope.datepickerFromDate = new Date();
								$scope.datepickerToDate = new Date();
								$scope.orderSchDate = undefined;
							}

							$scope.searchGroupList = function(keyword) {

								var data = {
									patientName : keyword,
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								}
								console.log(data);

								var URI = BASE_URL + ROOT_URL
										+ PATIENTAUTOFILLSEARCH;

								return BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													if (response.data.status == "success")
														return response.data.listObject;
												});

							}

							$scope.selectPatientId = function(info, model,
									label) {

								$scope.patientName = info.patientName;
								$scope.uhidNumber = info.uhidNumber;
								$scope.mobileNumber = info.mobileNumber;

								$scope.encounterSearchObject.patientId = info.patientId;
							}
							



							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveAppointmentSchedule();
							}

							$scope.acceptRequest = function(item) {
								console.log("Accept",item);
								//alert("accept");
								/*var acceptObj={
										
							    		 currentDate : moment(new Date()).format('YYYY-MM-DD HH:MM:SS'),// new Date().getTime(),
							    		 orderDetailsId : item.orderDetailsId ,
							    		 specialityId :  10,
							    		 updatedBy : $scope.userId
								}*/
								/*if(item.visitTypeId!=1){
									item.admissionId=item.encounterId
									item.encounterId=undefined;
								}*/
								
								
								
								if($scope.visitTypeId == 1){
									var data = {
										    "orgId":$scope.organizationId,
										    "unitId":$scope.unitId,
										    "billingMasterId":$scope.billingMasterId
										}
									var URI = BASE_URL + ROOT_URL + "/api/billing/getDataForCheckBillStatusByBillNumber";
									GenericService.serviceAction("POST",URI,data).then(function(response){
										if (response.data.status == 'success')
										{
											$scope.checkBillList=response.data.listObject;
											console.log($scope.checkBillList);
											$scope.billStatusId = $scope.checkBillList[0].billStatusId;
											$scope.paymentEntitlementId = $scope.checkBillList[0].paymentEntitlementId;
											if($scope.paymentEntitlementId != 2  && $scope.paymentEntitlementId != 3){
											if($scope.billStatusId == 3 && $scope.paymentEntitlementId == 1 && $scope.checkFlagBillSettle == false){
												$rootScope.startLoader();
												item.isCritical='N';
												item.unitId=$scope.unitId;
												item.organizationId=$scope.organizationId;
												item.userId=$scope.userId;
												item.isPACSRequired=cookieObjectAppConfig.isPACSRequired;
												item.specialityId=$scope.radiologySpecialityId;
												item.updatedBy = $scope.userId;
												item.currentDate =moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
												
												var URI = BASE_URL + ROOT_URL + "/api/opd/acceptRadiologyRequest";
												BillingGenericService.serviceAction(METHOD_POST,URI,item, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
													//$scope.encounterList = response.data.listObject;
													if(response.data.status == "success"){
														$rootScope.stopLoader();
														$scope.init();
														$scope.searchEncounter();
													}else if(response.data.code = 409){
														$rootScope.stopLoader();
														$scope.init();
														$scope.searchEncounter();
													}
												});
											}else{
												if($scope.checkFlags == 1){
													$scope.checkFlagBillSettle = true;
													$('#checkBillSettlePopUp').modal('show');
												}else{
													$rootScope.startLoader();
													item.isCritical='N';
													item.unitId=$scope.unitId;
													item.organizationId=$scope.organizationId;
													item.userId=$scope.userId;
													item.isPACSRequired=cookieObjectAppConfig.isPACSRequired;
													item.specialityId=$scope.radiologySpecialityId;
													item.updatedBy = $scope.userId;
													item.currentDate =moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
													
													var URI = BASE_URL + ROOT_URL + "/api/opd/acceptRadiologyRequest";
													BillingGenericService.serviceAction(METHOD_POST,URI,item, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
														//$scope.encounterList = response.data.listObject;
														if(response.data.status == "success"){
															$rootScope.stopLoader();
															$scope.init();
															$scope.searchEncounter();
														}else if(response.data.code = 409){
															$rootScope.stopLoader();
															$scope.init();
															$scope.searchEncounter();
														}
													});
												}
											}
										}else{
											$rootScope.startLoader();
											item.isCritical='N';
											item.unitId=$scope.unitId;
											item.organizationId=$scope.organizationId;
											item.userId=$scope.userId;
											item.isPACSRequired=cookieObjectAppConfig.isPACSRequired;
											item.specialityId=$scope.radiologySpecialityId;
											item.updatedBy = $scope.userId;
											item.currentDate =moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
											
											var URI = BASE_URL + ROOT_URL + "/api/opd/acceptRadiologyRequest";
											BillingGenericService.serviceAction(METHOD_POST,URI,item, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
												//$scope.encounterList = response.data.listObject;
												if(response.data.status == "success"){
													$rootScope.stopLoader();
													$scope.init();
													$scope.searchEncounter();
												}else if(response.data.code = 409){
													$rootScope.stopLoader();
													$scope.init();
													$scope.searchEncounter();
												}
											});
										}
										}
									});
								}else{
									$rootScope.startLoader();
									item.isCritical='N';
									item.unitId=$scope.unitId;
									item.organizationId=$scope.organizationId;
									item.userId=$scope.userId;
									item.isPACSRequired=cookieObjectAppConfig.isPACSRequired;
									item.specialityId=$scope.radiologySpecialityId;
									item.updatedBy = $scope.userId;
									item.currentDate =moment(new Date()).format('YYYY-MM-DD HH:MM:SS');
									
									var URI = BASE_URL + ROOT_URL + "/api/opd/acceptRadiologyRequest";
									BillingGenericService.serviceAction(METHOD_POST,URI,item, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
										//$scope.encounterList = response.data.listObject;
										if(response.data.status == "success"){
											$rootScope.stopLoader();
											$scope.init();
											$scope.searchEncounter();
										}else if(response.data.code = 409){
											$rootScope.stopLoader();
											$scope.init();
											$scope.searchEncounter();
										}
									});
								}
							
								
								
								
								
								
								
								
								
								
								
								
								
								
								
								
							
								
							}
							
							
							$scope.cancelBeforBillSettle = function()
							{
								$scope.checkFlagBillSettle = false;
								$('#checkBillSettlePopUp').modal('hide');
							}
							
							$scope.continueBeforBillSettle = function()
							{
								$scope.checkFlags = 2;
								$scope.checkFlagBillSettle = false;
								$('#checkBillSettlePopUp').modal('hide');
								$scope.acceptRequest($scope.collectionObjSSSSSSS);
							}
							
							$scope.collectionObj = {};
							$scope.collectionObjSSSSSSS = {};
							$scope.validateUhid = "";
							$scope.checkFlagBillSettle = false;
							$scope.checkFlags = 1;
							
							$scope.validateWorkListDetails = function(obj){
								    $scope.billingMasterId = obj.billingMasterId;
								    $scope.visitTypeId = obj.visitTypeId;
									$scope.checkFlags = 1;
									$scope.checkFlagBillSettle = false;
									$scope.collectionObjSSSSSSS =obj; 
								if($scope.mandatoryScan != null && $scope.mandatoryScan =="Y"){
									$scope.collectionObj = obj; 
									$scope.validateUhid = "";
									$('#validatePatient1').modal('show');
								}else{
									$scope.acceptRequest(obj);
								}
								
							}
							
							$scope.closevalidatePop = function(){
								$('#validatePatient1').modal('hide');
							}
							
							$scope.validateEquealPatient = function(event,barcode){
								 var enterCode = 13;
								 if (!barcode && event.which === enterCode) {
								        barcode = $scope.validateUhid;
								      }
									 if(!!barcode){
										 
										 if($scope.collectionObj.uhidNumber.toLowerCase() == $scope.validateUhid.toLowerCase() ){
												$('#validatePatient1').modal('hide');
												$scope.acceptRequest($scope.collectionObj);
											}else{
												growl.warning('Please enter valid patient number',
														{
															title : 'Warning!'
														});
											}
										 
									 }
									
							}
							
							

							$scope.rejectRequest = function(obj) {
								 $scope.noteReject = '';
								 $scope.rejectReqObj=obj;
								 
								console.log($scope.rejectReqObj);
								
								
							}
							$scope.saveAcceptReject=function(){
								console.log("reason Id",$scope.rejectReasonId);								
								if($scope.rejectReasonId==undefined || $scope.rejectReasonId=="")
									{
										growl.warning('Please select reason...!!',
												{
													title : 'Warning!'
												});
										return false;
									}
								else
									{
									if(Object.keys($scope.rejectReqObj).length !=0 ){
										$('#accepatReject').modal('hide');
										var data1={
													"encounterId": $scope.rejectReqObj.encounterId,
													"admissionId":$scope.rejectReqObj.admissionId,
													"userId":$scope.userId,
													"reasonId":$scope.rejectReasonId,
													"isCritical":'Y',
													"orderDetailsId":$scope.rejectReqObj.orderDetailsId,
													"unitId":$scope.unitId,
													"organizationId":$scope.organizationId,
													"visitTypeId":$scope.rejectReqObj.visitTypeId,
													"isPreOrPostBilling":$scope.isPreOrPostBilling,
													"specialityId":$scope.rejectReqObj.specialityId
										        }
									       
												 $rootScope.startLoader();
									    		var URI = BASE_URL + ROOT_URL+ "/api/opd/rejectRadiologyRequest";
									    		BillingGenericService.serviceAction(METHOD_POST,URI,data1, NOTIFICATION_MSG_STATUS_TRUE)
												.then(
														function(response) {
																$scope.init();
																$scope.searchEncounter();
																$scope.rejectReqObj = {};
																$scope.rejectReasonId = "";
																$rootScope.stopLoader();
														});
//											}
										
										}
									}
								}
								  
						
							$scope.getToDate=function(){
								$scope.todateOptions.minDate=$scope.datepickerFromDate.getTime();
								
								if(moment($scope.datepickerToDate).isBefore(moment($scope.datepickerFromDate)))
								{
									 $scope.datepickerToDate=new Date($scope.datepickerFromDate.getTime());
								}
							}
							
							$scope.search="";
							$scope.searchServices = function(searchKeyword){
								$scope.servicesList=[];
								
								var serData = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										searchKeyword : searchKeyword
								}
								var URI = BASE_URL + ROOT_URL + "/global/serviceMaster";
								
								//var URI = BASE_URL + ROOT_URL + "/global/serviceMaster/"+searchKeyword+"/unit/"+$scope.unitId+"/org/"+$scope.organizationId;
								return BillingGenericService.serviceAction(METHOD_POST,URI,serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									if(response.data.status == "success"){
										$scope.servicesList = response.data.listObject;
									}
									return $scope.servicesList;
								});
							}
							
							$scope.addServiceToTempServiceList = function($item, $model, $label){
								$scope.encounterSearchObject.serviceId = $item.serviceMasterId;
								
							}
							$scope.checkPatient = function()
							{
								if($scope.patientName ==undefined)
								{
									$scope.encounterSearchObject.patientId =0;
								}
							}
							/*$scope.checkService = function()
							{
								if($scope.serviceName ==undefined)
								{
									$scope.encounterSearchObject.serviceId =0;
								}
							}*/
							
							/****Template Save*****/
							
					        $scope.templateTransaction = {
				        			  templateTransactionId : "",
				        			  templateId : "",
				        			  title : "",
				        			  specialityId : "",
				        			  subSpecialityId : "",
				        			  diagnosisId:"",
				        			  templateObject : "",
				        			  orderDetailsId : "",
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
				        			  updatedDate : ""
				        }
					    	$scope.viewTemplate = function(orderDetailsId)
					    	{
					    		
					        	$rootScope.startLoader();
					        	var data={
					        		  orgId : $scope.organizationId,
					        		  unitId : $scope.unitId,
					        		  doctorId : 0,
					        		  specialityId : 0,
					        		  templateTypeId : 10,
					        		  orderDetailsId : orderDetailsId,
					        		  patientId : '',
					        		  visitTypeId: '',
					        		  encounterId: ''
					        		}
					        	
					        	console.log('data',JSON.stringify(data));
					        	var URI = BASE_URL + ROOT_URL +GLOBAL+'/petient/quesionnaaire/details';
					    		BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
					    				{
					    	
					    			   if(response.data.status=='success')
					    			      {
					    				   $scope.saveTrasactionFlag = false;
					    				   	$scope.templateTransactionObject = response.data.listObject[0];
					    				  	if($scope.templateTransactionObject==undefined || $scope.templateTransactionObject ==null ||
							    				   	 $scope.templateTransactionObject =="" ){
						    				  		growl.warning("Please add questionnaire...!",{
														title : "warning"
													});
									        		return;
							    				   	 }
					    				   	/*document.getElementById("htmlContent").innerHTML = JSON.parse($scope.templateTransactionObject.templateObject).html;
					    				   	$scope.obj = JSON.parse($scope.templateTransactionObject.templateObject).obj;
					    				   	var element = angular.element(document.getElementById("htmlContent"));
											$compile(element)($scope);*/
					    				  	document.getElementById("htmlContent").innerHTML = $scope.templateTransactionObject.templateObject;
					    				   	$('#questionnaireTemplateModal').modal('show');
					    				   	$rootScope.stopLoader();
					    			      }
					    			   else if(response.data.status=='error'){
					    				   $rootScope.stopLoader();
					    			   }
					    		});
								   
								$scope.saveTrasactionFlag = false;
					    	}
					    	
					        $scope.saveTemplateTransaction = function()
					        {
					        		$rootScope.startLoader();
					        		
									var templateTransaction = angular.copy($scope.templateTransaction);
									templateTransaction.templateId = $scope.template.templateId;
									templateTransaction.title= $scope.template.title;
									templateTransaction.specialityId = $scope.radionLogyServiceObj.specialityId;
									templateTransaction.subSpecialityId = '';
									templateTransaction.templateObject = $scope.template.templateObject;
									templateTransaction.encounterId=$scope.radionLogyServiceObj.encounterId;
									templateTransaction.doctorId=$scope.radionLogyServiceObj.doctorId;
									templateTransaction.patientId=$scope.radionLogyServiceObj.patientId;
									templateTransaction.orderDetailsId=$scope.radionLogyServiceObj.orderDetailsId;
									templateTransaction.isQuestionary='Y';
									var jsonObject = angular.equals({},$scope.template.templateObject.obj)?JSON.parse($scope.template.templateObject.obj):{};
									var templateObj = JSON.parse($scope.template.templateObject);
									templateObj.obj = $scope.obj;
									templateTransaction.jsonObject = JSON.stringify(jsonObject);
									templateTransaction.templateObject = JSON.stringify(templateObj);
									templateTransaction.templateTransactionId = $scope.radionLogyServiceObj.templateTransactionId;
					        	    console.log('templateTransaction',JSON.stringify(templateTransaction));
						    		
					        	    var URI = BASE_URL + ROOT_URL + "/global/template/transaction";
					        	    BillingGenericService.serviceAction(METHOD_POST,URI,templateTransaction,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
						    			if (response.data.status == "success")
						    			{
						    				$('#questionnaireTemplateModal').modal('hide');
											$scope.templateList=[];
										}
										$rootScope.stopLoader();
						    		});
					        }
					        
					        /** Template List*/
					        
					       	$scope.getTemplateList = function(radionLogyServiceObj) 
					    	{
					       		$scope.radionLogyServiceObj = radionLogyServiceObj;
					       		console.log('radionLogyServiceObj',JSON.stringify(radionLogyServiceObj));
					       		
					    		$rootScope.startLoader();
					        	var data={
					        		  orgId : $scope.organizationId,
					        		  unitId : $scope.unitId,
					        		  doctorId : $scope.radionLogyServiceObj.doctorId,
					        		  specialityId : $scope.radionLogyServiceObj.specialityId,
					        		  subSpecialityId : $scope.radionLogyServiceObj.subSpecialityId,
					        		  templateTypeId : 10,
					        		  patientId : '',
					        		  visitTypeId: '',
					        		  encounterId: ''
					        		}
					        	
					        	console.log('data',JSON.stringify(data));
					        	var URI = BASE_URL + ROOT_URL +GLOBAL+'/petient/quesionnaaire/radiology';
					    		BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
					    				{
					    	
					    			   if(response.data.status=='success')
					    			      {
					    				   $scope.saveTrasactionFlag = true;
					    				   	$scope.templateTransactionObject = response.data.listObject[0];
					    				   	$scope.template = response.data.listObject[0];
					    				   	if($scope.template != undefined){
					    				  /* 	document.getElementById("htmlContent").innerHTML = JSON.parse($scope.template.templateObject).html;
					    				 	$scope.obj = JSON.parse($scope.template.templateObject).obj;
					    				   	$('#questionnaireTemplateModal').modal('show');
								    		var element = angular.element(document.getElementById("htmlContent"));
											   $compile(element)($scope);*/
					    				   		
					    				   		
					    				   		
					    				   		
					    				   		try{
					    				   			var templateTransaction = angular.copy($scope.templateTransaction);
													templateTransaction.templateId = $scope.template.templateId;
													templateTransaction.title= $scope.template.title;
													templateTransaction.specialityId = $scope.radionLogyServiceObj.specialityId;
													templateTransaction.subSpecialityId = '';
													templateTransaction.templateObject = $scope.template.templateObject;
													templateTransaction.encounterId=$scope.radionLogyServiceObj.encounterId;
													templateTransaction.doctorId=$scope.radionLogyServiceObj.doctorId;
													templateTransaction.patientId=$scope.radionLogyServiceObj.patientId;
													templateTransaction.orderDetailsId=$scope.radionLogyServiceObj.orderDetailsId;
													templateTransaction.isQuestionary='Y';
                                                    templateTransaction.patientName = $scope.radionLogyServiceObj.patientName !== undefined && $scope.radionLogyServiceObj.patientName !== null ? $scope.radionLogyServiceObj.patientName : "";
                                                    templateTransaction.doctorName = $scope.radionLogyServiceObj.doctorName !== undefined && $scope.radionLogyServiceObj.doctorName !== null ? $scope.radionLogyServiceObj.doctorName : "";
                                                    templateTransaction.uhidNumber = $scope.radionLogyServiceObj.uhidNumber !== undefined && $scope.radionLogyServiceObj.uhidNumber !== null ? $scope.radionLogyServiceObj.uhidNumber : "";
                                                    templateTransaction.dob = $scope.radionLogyServiceObj.dob !== undefined && $scope.radionLogyServiceObj.dob !== null ? $scope.radionLogyServiceObj.dob : "";
                                                    templateTransaction.ageString = $scope.radionLogyServiceObj.ageString !== undefined && $scope.radionLogyServiceObj.ageString !== null ? $scope.radionLogyServiceObj.ageString : $scope.radionLogyServiceObj.ageYear;
                                                    templateTransaction.doa = $scope.radionLogyServiceObj.doa !== undefined && $scope.radionLogyServiceObj.doa !== null ? $scope.radionLogyServiceObj.doa : "";
                                                    templateTransaction.pdd = $scope.radionLogyServiceObj.pdd !== undefined && $scope.radionLogyServiceObj.pdd !== null ? $scope.radionLogyServiceObj.pdd : "";
                                                    templateTransaction.admissionNumber = $scope.radionLogyServiceObj.admissionNumber !== undefined && $scope.radionLogyServiceObj.admissionNumber !== null ? $scope.radionLogyServiceObj.admissionNumber : "";
                                                    templateTransaction.gender = $scope.radionLogyServiceObj.genderCode !== undefined && $scope.radionLogyServiceObj.genderCode !== null ? $scope.radionLogyServiceObj.genderCode : "";

													  var URI = BASE_URL + ROOT_URL + NEW_TEMPLATE_SAVE;
														BillingGenericService.serviceAction(METHOD_POST, URI,templateTransaction,
																NOTIFICATION_MSG_STATUS_FALSE)
														.then(function(response) {
																	if (response.data.status == "success"){
																		window.open(response.data.object+"&formType=digital&patientName="+$scope.radionLogyServiceObj.patientName+
                                                                               "&doctorName="+$scope.radionLogyServiceObj.doctorName+
                                                                               "&uhid="+$scope.radionLogyServiceObj.uhidNumber+
                                                                               "&dob="+$scope.radionLogyServiceObj.dob+
                                                                               "&age="+$scope.radionLogyServiceObj.ageString+
                                                                               "&gender="+$scope.radionLogyServiceObj.genderCode+
                                                                               "&admissionNo="+$scope.radionLogyServiceObj.admissionNumber+
                                                                               "&admissionDate="+$scope.radionLogyServiceObj.doa+
                                                                               "&dischargeDate="+$scope.radionLogyServiceObj.pdd,'_blank');
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
					    				   	}else{
					    				   		growl.warning("Template is not mapped...!",{
													title : "warning"
												});
					    				   		$rootScope.stopLoader();
								        		return;
								        		
					    				   	}
					    				   	$rootScope.stopLoader();
					    			      }
					    			   else if(response.data.status=='error'){
					    				   $rootScope.stopLoader();
					    			   }
					    		});
					    	}
							

					       	//for Radiology Reports
					       	$scope.getReport = function(rediologyObj)
					    	{
					       		$scope.radioLogyReportObj = rediologyObj;
					        	$rootScope.startLoader();
					        	if(rediologyObj.templateTransactionId==null)
					        		{
					        		var data={
							        		  orgId : $scope.organizationId,
							        		  unitId : $scope.unitId,
							        		  doctorId : 0,
							        		  specialityId : 0,
							        		  templateTypeId : 13,
							        		  orderDetailsId : '',
							        		  patientId : '',
							        		  visitTypeId: '',
							        		  encounterId: '',
							        		  serviceId : rediologyObj.serviceId	  
							        		}
							        	console.log('data',JSON.stringify(data));
							        	var URI = BASE_URL + ROOT_URL +'/api/global/templates/byService';
							    		BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
							    				{
							    	
							    			   if(response.data.status=='success')
							    			      {
							    				 	$scope.templateListRadiology = response.data.listObject;
							    				 	$scope.radiologyReport = {};
							    					$('#addtemplateModalRadio').modal('show');
							    				   	$rootScope.stopLoader();
							    			      }
							    			   else if(response.data.status=='error'){
							    				   $rootScope.stopLoader();
							    			   }
							    		});
										   
					        		}
					        	else{
					        		$scope.radiologyReport = {};	
					        		$scope.radiologyReport.templateObject  = rediologyObj.templateObject;
					        		$scope.radioLogyReportObj.templateTransactionId =  rediologyObj.templateTransactionId;
					        		$scope.radiologyReport.templateFormatId  = rediologyObj.templateFormatId;
					        		$scope.radiologyReport.templateId = '';
					        		$scope.radiologyReport.title= '';
					        		
					        		if(rediologyObj.templateFormatId==1)
					        			{
					        			document.getElementById("htmlContentReport").innerHTML = JSON.parse(rediologyObj.templateObject).html;
						        		$scope.obj =JSON.parse(rediologyObj.templateObject).obj;
							    		var element = angular.element(document.getElementById("htmlContentReport"));
										   $compile(element)($scope);
					        			}
					        		else if(rediologyObj.templateFormatId==2)
					        			{
					        			 $scope.value =   rediologyObj.templateObject;
					        			}
					        	  	$('#radiologyReport').modal('show');
			    				   	$rootScope.stopLoader();
					        	    }
					        
					    	}
					       	$scope.selectTemplate  = function(radiologyReport)
					    	{
					    		$scope.radiologyReport = radiologyReport;
					    	}
					       	$scope.addTemplate = function()
					    	{
					       		if($scope.radiologyReport!=null && $scope.radiologyReport!=undefined)
					       			{
					       			$scope.radioLogyReportObj.templateFormatId = 	$scope.radiologyReport.templateFormatId;
			    				   	if($scope.radiologyReport.templateFormatId==1)
			    				   		{
			    				   	 	$scope.radiologyReport.templateObject  = response.data.listObject[0].templateObject;
			    				     	$scope.radiologyReport.templateFormatId  = $scope.radiologyReport.templateObject.templateFormatId;
				    				   	document.getElementById("htmlContentReport").innerHTML = JSON.parse($scope.radiologyReport.templateObject).html;
							    		var element = angular.element(document.getElementById("htmlContentReport"));
										   $compile(element)($scope);
										   

				    				   		/*$scope.radiologyReport.templateObject  = $scope.radiologyReport.templateObject;
				    				     	$scope.radiologyReport.templateFormatId  = $scope.radiologyReport.templateFormatId;
											   $scope.value =   JSON.parse($scope.radiologyReport.templateObject).html;*/
			    				   		}
			    				    else if($scope.radiologyReport.templateFormatId==2)
			    			    	  {
			    				    	 $scope.value =   $scope.radiologyReport.templateObject;
			    			    	  }
			    				   	$('#addtemplateModalRadio').modal('hide');
			    				   	$('#radiologyReport').modal('show');
					       			}
					       		else{
						    			growl.warning("Please select Template...!",{
											title : ERROR_MSG
										});
						        		return;
					       		}
					        	
							}
					       	
					
					       	
					        $scope.updateReport = function()
					        {
					        		$rootScope.startLoader();
					        		
									var templateTransaction = angular.copy($scope.templateTransaction);
									templateTransaction.templateTransactionId = $scope.radioLogyReportObj.templateTransactionId;
									templateTransaction.templateId = $scope.radiologyReport.templateId;
									templateTransaction.title= $scope.radiologyReport.title;
									templateTransaction.specialityId = $scope.radioLogyReportObj.specialityId;
									templateTransaction.subSpecialityId = '';
									templateTransaction.templateObject = $scope.radiologyReport.templateObject;
									templateTransaction.encounterId=$scope.radioLogyReportObj.encounterId;
									templateTransaction.doctorId=$scope.radioLogyReportObj.doctorId;
									templateTransaction.patientId=$scope.radioLogyReportObj.patientId;
									templateTransaction.orderDetailsId=$scope.radioLogyReportObj.orderDetailsId;
									if($scope.radiologyReport.templateFormatId==1)
									{	
									var jsonObject = angular.equals({},$scope.radiologyReport.templateObject.obj)?JSON.parse($scope.radiologyReport.templateObject.obj):{};
									var templateObj = JSON.parse($scope.radiologyReport.templateObject);
									templateObj.obj = $scope.obj;
									templateTransaction.jsonObject = JSON.stringify(jsonObject);
									templateTransaction.templateObject = JSON.stringify(templateObj);
									}
									else if($scope.radiologyReport.templateFormatId==2) {
										templateTransaction.templateObject = $scope.value;
									}
					        	    console.log('templateTransaction',JSON.stringify(templateTransaction));
						    		
					        	    var URI = BASE_URL + ROOT_URL + "/global/template/transaction";
					        	    BillingGenericService.serviceAction(METHOD_PUT,URI,templateTransaction,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
						    			if (response.data.status == "success")
						    			{
						    				$('#radiologyReport').modal('hide');
						    				$('#addtemplateModalRadio').modal('hide');
											$scope.templateList=[];
											$scope.radiologyReport = {};
											$scope.searchEncounter();
										}
										$rootScope.stopLoader();
						    		});
					        }
					        
					        $scope.getClassForColor = function(info)
							{
								   
								   if(info.isCritical=='Y'){
									   return 'td-bg-red';
								   }else if (info.isVip=='Y') 
								   {
									    return 'td-bg-blue';
								   }else if(info.encounterTypeId==6){
									   return 'td-bg-green';
								   }
							}
					        
					        $scope.printReport=function(obj)
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
					    			             "paramValue": obj.templateTransactionId
					    		              }
					    		            ]
					    		 };
					    	  reportService.generate(REPORT_API_URI1,data);
					    	}
					        
					        //For Form F Start
					        
					        
					        FroalaEditor.DefineIcon('insert', {
							    NAME: 'plus',
							    SVG_KEY: 'add'
							  });
							  FroalaEditor.RegisterCommand('insert', {
							    title: 'Insert HTML',
							    focus: true,
							    undo: true,
							    refreshAfterCallback: true,
							    callback: function() {
							      this.html.insert(`<input type="checkbox"> `);
							    }
							  });
							$scope.FroalaEditor =   new FroalaEditor('div#froala-editor', {
							   // toolbarButtons: ['insert', 'print', 'getPDF', 'html'],
								key: "AVB8B-21A3A1D3E1E2B2uigecgyqC-7frspB2twG2suc1A6B5A4B3A3C2D2G3C5B4==",
							    toolbarButtons: ['fullscreen', 'bold', 'italic', 'underline', 'strikeThrough', 'subscript', 'superscript', 'fontFamily', 'fontSize', 'color', 'emoticons', 'inlineStyle', 'paragraphStyle', 'paragraphFormat', 'align', 'formatOL', 'formatUL', 'outdent','print', 'getPDF', 'html','insert'],
							    events: {
							      'contentChanged': function() {
							        let editor = this
							        editor.html.get(true);
							       // console.log('abc',editor.html.get(true))
							        $scope.value = editor.html.get(true);
							      }
							    },
							    
							  })
							
							
					    	$scope.fifthTabLevel = 1;

							$scope.setFifthTabLevel = function(newTab) {
								$scope.fifthTabLevel = newTab;
							};

							$scope.isSetFifthTabLevel = function(tabNum) {
								return $scope.fifthTabLevel === tabNum;
							};
							
					    	$scope.redirectFormFObject = {};
							//For Test Templ
							$scope.getConsentTemp = function(item){
								$scope.redirectFormFObject = item;
								$scope.selectedPatientId = item.patientId;
								$scope.selectedAppointmentId = item.appointmentId;
							 var data={
									 //appointmentId : item.appointmentId,
				    				  consentTypeId : 1000,
				    				  encounterId : item.encounterId,
				    				   admissionId : item.admissionId
					        		}
							 
							 
							 
							 $scope.templateObj = { 
									 "consentTransactionId":"",
										"consentMasterId":"",
										"admissionId":null,
										"encounterId":"",
										"templateDesc":"",
										"status":"A",
										"createdBy":$scope.userId,
										"createdDate":"",
										"updatedBy":$scope.userId,
										"updatedDate":"",
										"unitId":$scope.unitId,
										"organizationId":$scope.organizationId,
										"consentTypeId":"1000",
										"appointmentId" : item.appointmentId,
										"encounterId" : item.encounterId,
					    				"admissionId" : item.admissionId
									}
							// $scope.selectedTemplates = [];
							 
							 
					        	$rootScope.startLoader();
				        	    var URI = BASE_URL + ROOT_URL + "/adt/consentTemplate";
				        	    GenericService.serviceAction(METHOD_POST,URI,data).then(function(response) {
					    			if (response.data.status == "success")
					    			{
					    				$scope.templateList = [];
					    			 	$scope.templateList = response.data.listObject;
					    			 	$('#concentTemplateOPD').modal('show');
										$rootScope.stopLoader();
									}
					    			else if(response.data.status == "error")
					    				{
					    				$rootScope.stopLoader();
					    				}
									
					    		});
							}
							
							
							$scope.viewTemplate  = function(templateObj,index)
					    	{
								$scope.FroalaEditor.html.set('');
									$scope.value = templateObj.templateDesc;
									$scope.selectedForm.templateDesc = templateObj.templateDesc;
									$scope.FroalaEditor.html.insert($scope.selectedForm.templateDesc);
									$scope.templateIndex = index;
									$('#consentEditPopup').modal('show');
					    	}
							
							
							$scope.redirectFunctionToForm = function(item,idx){
//								/$scope.redirectFormFObject
								$scope.selectedForm = item;
								item.patientId = $scope.selectedPatientId;
								item.encounterId = $scope.redirectFormFObject.encounterId;
								item.admissionId  =  $scope.redirectFormFObject.admissionId;
								item.orderDetailsId  = $scope.redirectFormFObject.orderDetailsId;
								
						        	$rootScope.startLoader();
					        	    var URI = BASE_URL + ROOT_URL + "/api/opd/appendCensentData";
					        	    GenericService.serviceAction(METHOD_POST,URI,item).then(function(response) {
						    			if (response.data.status == "success")
						    			{
						    				
						    				$rootScope.stopLoader();
						    				item.templateDesc = response.data.object.templateDesc;
						    				$scope.selectedForm.templateDesc = item.templateDesc;
						    				$scope.viewTemplate(item,idx);
						    				 /*angular.element('#concentTemplateOPD').modal('hide');
											 $(".modal-backdrop").hide();
											 
											 
											 
											console.log($scope.redirectFormFObject);
											$state.go("formF", {
												itemObj: item,
											});*/
										}
						    			else if(response.data.status == "error")
					    				{
						    				$rootScope.stopLoader();
					    				}
										
						    		});
								
							}
							
							$scope.redirectFunctionToFormSelfId = function(item,idx){
								
								 angular.element('#concentTemplateOPD').modal('hide');
								 $(".modal-backdrop").hide();
								
								item.patientId = $scope.selectedPatientId;
								item.encounterId = $scope.redirectFormFObject.encounterId;
								item.admissionId  =  $scope.redirectFormFObject.admissionId;
								item.orderDetailsId  = $scope.redirectFormFObject.orderDetailsId;
					        	$rootScope.startLoader();
				        	    var URI = BASE_URL + ROOT_URL + "/api/opd/appendCensentData";
				        	    GenericService.serviceAction(METHOD_POST,URI,item).then(function(response) {
					    			if (response.data.status == "success")
					    			{
					    				
					    				$rootScope.stopLoader();
					    				item.templateDesc = response.data.object.templateDesc;
					    				//$scope.selectedForm.templateDesc = item.templateDesc;
					    				//$scope.viewTemplate(item,idx);
					    				//console.log($scope.redirectFormFObject);
					    				item.appointmentId =$scope.selectedAppointmentId;
										$state.go("formFSelfId", {
											itemObj: item,
										});
									}
					    			else if(response.data.status == "error")
				    				{
					    				$rootScope.stopLoader();
				    				}
									
					    		});
							}

							$scope.printTemplate = function()
							{
								console.log("hh",$scope.FroalaEditor.html.get(true));
								 
									var templateSaveList = [];
										            var obj = angular.copy($scope.templateObj);
										            obj.consentTransactionId = $scope.selectedForm.consentTransactionId;
										            obj.consentMasterId =$scope.selectedForm.consentMasterId; 
										            obj.templateDesc =$scope.FroalaEditor.html.get(true)//$scope.selectedForm.templateDesc;
										            templateSaveList.push(obj);
							        	
							         $rootScope.startLoader();
						    		 $('#concentTemplateOPD').modal('hide');
						    		 $('#consentEditPopup').modal('hide');
						        	    var URI = BASE_URL + ROOT_URL + "/adt/patientConsent";
						        	    GenericService.serviceAction(METHOD_POST,URI,templateSaveList).then(function(response) {
							    			if (response.data.status == "success")
							    			{
												$rootScope.stopLoader();
													
													    var divContents = $scope.FroalaEditor.html.get(true);  
											            var printWindow = window.open('', '', 'height=500,width=700');  
											            printWindow.document.write('<html><head><title>FORM F</title>');  
											            printWindow.document.write('</head><body >');  
											            printWindow.document.write(divContents);  
											            printWindow.document.write('</body></html>');  
											            printWindow.document.close();  
											            printWindow.print();  
											            
													
													 /*var data =  
													  {
												            "appName": "HIS",
												            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
												            "reportDesignFilePath": "getConsent.rpt",
												            "reportFormat": "PDF",
												            "exportFileName": "consentRpt",
												            "reportParams": [
												              {
													             "paramName": "admission_id",
													             "paramType": "Integer",
													             "paramValue": $scope.itemSelecteds.admissionId
												              },
												              {
														             "paramName": "consent_master_id",
														             "paramType": "Integer",
														             "paramValue": value.consentMasterId
													           },
													           {
														             "paramName": "consent_type_id",
														             "paramType": "Integer",
														             "paramValue": 1//admission consent
													           }
												            ]
												         };
													 reportService.generate(REPORT_API_URI1,data);*/
													
											}
							    			else if(response.data.status == "error")
							    				{
							    				$rootScope.stopLoader();
							    				}
							    		});
									
									 
								
								
							}
					        
					        //For Form F End
					       	
							$scope.patientDocumnetInformation = function(info) {
								$scope.patientDocumentFlipBook ={};
								$scope.patientDocumentFlipBook =info;
								
									var data =
									{
									"encounterId":$scope.patientDocumentFlipBook.encounterId,
									"patientId":$scope.patientDocumentFlipBook.patientId,
									"unitId":$scope.unitId,
									"organisationId":$scope.organizationId,
									"userId":$scope.userId
									}
									
								var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
								BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$scope.patientDocumentListTest = [];
										if (response.data.status == "success") 
										{
											$scope.patientDocumentListTest = response.data.listObject;
											//console.log($scope.patientDocumentListTest.length);
											if($scope.patientDocumentListTest != null && $scope.patientDocumentListTest.length >0){
												$('#patientDocumentOption').modal('show');
											}
											$rootScope.stopLoader();
										}
										else if(response.data.status == "error")
											{
											$rootScope.stopLoader();
											}
									});
						
					          };
				          
				          $scope.convertDateToTimestamp = function(dateString) {
				                let parts = dateString.split("-");
				                let day = parseInt(parts[0], 10);
				                let month = parseInt(parts[1], 10) - 1; 
				                let year = parseInt(parts[2], 10);
				                let date = new Date(year, month, day);
				                let timestampInMilliseconds = date.getTime();
				                let timestampInSeconds = timestampInMilliseconds / 1000;
				                return timestampInSeconds;
				            };
				            
					     		$scope.generateFlipbookData = function() {
					                  let flipbookDatalist = [];

						     			var filteredlist=[];
						     		    angular.forEach($scope.patientDocumentListTest,function(filterObj,index){
						     		    	
						     		    	if(filterObj.documentPath != null){
						     				var flipbookDataobj={};
						     				 let createdDate = filterObj.createdDate;
						     	            let timestamp = $scope.convertDateToTimestamp(createdDate);
						     				flipbookDataobj.timestamp=timestamp;
						     				flipbookDataobj.url=filterObj.documentPath;
						     				flipbookDataobj.tags=[];
						     				flipbookDataobj.tags.push("Document");
						     				flipbookDataobj.title=filterObj.documentName != null ? filterObj.documentName :"Lab Report" ; 
						     				flipbookDatalist.push(flipbookDataobj);
						     		    	}
						     				
						     			});

					                let data = {
					                    flipbookData: flipbookDatalist,
					                    logo: $scope.firstImgaa,
					                    privacy: "public"
					                };

					                let baseUrlLocation = $window.location.href;
					                let temp = baseUrlLocation.split('/');
					                let BASE_URL_CASESHEET = baseUrlLocation.replace(temp[temp.length - 1], "");

					                let generateFlipbookurl = "https://feqituyulh.execute-api.ap-south-1.amazonaws.com/dev/api/generateFlipbook";
					                
					        		try {
					        			var param={};
					        				$.ajax({
					        			        url : generateFlipbookurl,
					        					type : "POST",
					        					headers: {'X-Api-Key': "apikey_zHOw4R758ELytSeHFII99YibXj2sWHR6u9mxKdipN/g="},
					        					contentType : "application/json",
					        					dataType : "json",
					        					data : JSON.stringify(data),
					        			        //headers:getHeaders(),
					        					success : function(response){
					        						if(response.flipbookUrl!=undefined &&  response.flipbookUrl!=null  &&  response.flipbookUrl!="" ){
					        							  window.open(''+response.flipbookUrl, '_blank');
					        						}else{
					        							$.notify("Error occurred","error");
					        						}
					        			    	}
					        			});
					        	     } catch (e) {
					        	 		console.log(e );
					        		}
					            };

							
						} ]);