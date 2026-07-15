'use strict';

angular
		.module('myApp')
		.controller(
				'schedulingWorkingController',
				[
					'$scope',
					'$http',
					'$localStorage',
					'$sessionStorage',
					'$cookies',
					'$rootScope',
					'GenericService',
					'BillingGenericService',
					'growl',
					'$state',
					'$stateParams',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService,
								BillingGenericService, growl, $state,
								$stateParams,$modalInstance) {

							//alert("appointmentListController");
							
							/* init() function for form object create. */
							$scope.init = function() {

								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								
								var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
								if(cookieObjectAppConfig == undefined){
									$state.go('applicationConfiguration');
									return;
								}
								else
									{
										console.log('cookieObjectAppConfig',cookieObjectAppConfig);
										
									}
								$scope.cookieObjectAppConfig = cookieObjectAppConfig;
								var labConfig = $scope.checkLabConfig();
								$scope.labSpecialityId=labConfig.labSpecialityId;
								$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
								$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
								$scope.isSplitBillUnitFlag = cookieObjectAppConfig.isServiceWiseSplitBill;
								$scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
								$scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
								$scope.surchargeServiceId = cookieObjectAppConfig.surchargeServiceId;
								$scope.scheduledAppointmentList=[];
								$scope.copyOfScheduledAppointmentList=[];
								$scope.loadingMsg="";
								//$scope.tokenList = [];
								$rootScope.loginpage = true;
								$scope.allSelected='N';
								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								$scope.searchObj={
										"fromDate":new Date(),
										"toDate":new Date(),
										"visitTypeId":1,
										"serviceId":0,
										"patientId":0
								};
								
								//from date picker
								var date = new Date();
								$scope.dateOptionsFrom = {
										formatYear : 'yyyy',
										showWeeks : false,
										minDate : date.setDate((new Date()).getDate() - 90),
										//maxDate : new Date()
								};
								
								$scope.openDatepickerFrom = function() {
									$scope.datepickerFrom.opened = true;
								};
								
								$scope.datepickerFrom = {
										opened : false
									};
								
								//for to date
								$scope.dateOptionsTo = {
										formatYear : 'yyyy',
										showWeeks : false,
										minDate : new Date()
								};
								$scope.openDatepickerTo = function() {
									$scope.datepickerTo.opened = true;
								};
								
								$scope.datepickerTo = {
										opened : false
									};
								
								
								$scope.openDatePicker = function() {
									$scope.datepicker.opened = true;
								};

								$scope.datepicker = {
									opened : false
								};
								
								$scope.datepicker = new Date();
								
								$scope.dateObj = moment(new Date()).format('DD-MM-YYYY');
								$scope.date=new Date();
								// function for initializa clockpicker
								$scope.initClockPicker = function() {
									$('.clockpicker').clockpicker();
								}
								//$scope.datepicker =  moment(new Date()).format('DD/MM/YYYY');
								
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.unitCountryId = cookieObject.unitCountryId;
								$scope.unitStateId = cookieObject.unitStateId;
								$scope.unitDistrictId = cookieObject.unitDistrictId;
								$scope.unitCityId = cookieObject.unitCityId;
								$scope.unitAreaId = cookieObject.unitAreaId;
					
								$scope.toDate = moment(new Date()).format('DD-MM-YYYY');
							
								$scope.patientSearchObj = {
										"uhIdNumber"      : "",
										"patientName"     : "",
										"genderId"        : "",
										"mobileNo"        : "",
										"birthDate"       : "",
										"identificationNo":"",
										"patientType"	  :"1",
										"unitId"          :$scope.unitId,
										"organizationId"  :$scope.organizationId
								}
								
								$scope.searchType = 1;
								
								var data = {};
								
								var URI1 = BASE_URL + ROOT_URL + GETACTIVEGENDERLIST;
								BillingGenericService.serviceAction(METHOD_GET, URI1, data,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											console.log(response);
											$scope.genderList = [];
											if (response.data.status == "success")
												$scope.genderList = response.data.listObject;
										});
								
								var URI2 = BASE_URL + ROOT_URL + GETACTIVEIDENTIFICATION;
								BillingGenericService.serviceAction(METHOD_GET, URI2, data,
								NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
									console.log(response);
									$scope.identificationList = [];
									if (response.data.status == "success")
										$scope.identificationList = response.data.listObject;
								});
								
								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
										
								var URI3 = BASE_URL + ROOT_URL+ GETACTIVESPECIALITY;
								BillingGenericService.serviceAction(METHOD_POST, URI3,commonObj,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											// $scope.startLoader();
											if (response.data.status == "success")
												$scope.specialityList = response.data.listObject;
										});
							
						    	
						    	var commonObj = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId
									};
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEDOCTORLIST;
								GenericService
								.serviceAction("POST", URI, commonObj)
								.then(
										function(response) {
											$scope.doctorsList = [];

											if (response.data.status == "success") {
												$scope.doctorsList = response.data.listObject;
												
											}
										});		
							}
							$scope.init();
							
							//Function For Get Doctor List By Speciality
				      	$scope.getDoctorBySpeciality = function(id,index) {
								var data = {
									specialityId : id,
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								$scope.startLoader();
								var URI = BASE_URL + ROOT_URL + GETDOCTORBYSPECIALITYID;
								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
										.then(function(response) {
													console.log(response);
													if (response.data.status == "success"){
														$scope.scheduledAppointmentList[index].doctorList = response.data.listObject;
														$scope.stopLoader();
													}else{
														$scope.stopLoader();
													}
												});
									}
							
				    	$scope.getCyclicServicesList = function() {
				    		$scope.servicesListForCyclic =[];
							var data={
									organizationId:$scope.organizationId,
									unitId:$scope.unitId
							};
							var URI = BASE_URL + ROOT_URL
									+ "/api/opd/getServiceListForCyclicOrder";
							BillingGenericService .serviceAction(METHOD_POST, URI, data , NOTIFICATION_MSG_STATUS_FALSE)
									.then( function(response) {
												if (response.data.status == "success")
													$scope.servicesListForCyclic = response.data.listObject;
											});
						}
				    	$scope.getCyclicServicesList();
				    	 $scope.loadMoreData = function()
				            {
				                $scope.offset+=200;
				                $scope.loadMoreFlag=true;
				                $scope.searchPatient($scope.patientSearchObj);
				            }
				            $scope.offset=0;
				            $scope.loadMoreFlag=false;
				            $scope.noOfRecordsPerPage=200;
				            $scope.patientList = [];
							$scope.searchPatient = function()
							{
							
								if(($scope.patientSearchObj.uhidNumber == '' || $scope.patientSearchObj.uhIdNumber == "") 
										&& $scope.patientSearchObj.patientName == '' && $scope.patientSearchObj.genderId == '' 
										&& ($scope.patientSearchObj.mobileNumber == '' || $scope.patientSearchObj.mobileNo=="") 
										&& ( $('#datepickerSearch').val() == '' || $scope.patientSearchObj.birthDate=="")
										&& ($scope.patientSearchObj.identificationTypeId == '' || $scope.patientSearchObj.identificationTypeId == undefined)
										&& ($scope.patientSearchObj.identificationNumber == '' || $scope.patientSearchObj.identificationNo == ""))
									{
										growl.warning("Please enter atleast one search criteria..!",{title : "WARNING!"
									});
									return false;
									}
								
								else if($scope.patientSearchObj.patientName!="" && $scope.patientSearchObj.patientName.length < 3)
								{
									growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
									$scope.patientList = [];
									return false;
								}	
								else{
									$scope.startLoader();
									 if($scope.loadMoreFlag==true)
						                {
						                	$scope.patientSearchObj.offset=$scope.offset;
						                $scope.loadMoreFlag=false;
						                }
						            else
						                {
						            	$scope.patientSearchObj.offset=0;
						                $scope.loadMoreFlag=false;
						                $scope.patientList = [];
						                }
						                $scope.patientSearchObj.noOfRecordsPerPage=$scope.noOfRecordsPerPage;
											 	var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
											 	if($scope.patientSearchObj.birthDate != "" && typeof($scope.patientSearchObj.birthDate) != "undefined"){
													var dt = new Date($scope.patientSearchObj.birthDate);
													$scope.patientSearchObj.birthDate = (dt.getMonth() + 1) + "-" + dt.getDate() + "-" +  dt.getFullYear();
												} 
											 	
													BillingGenericService
															.serviceAction(METHOD_POST,
																	URI, $scope.patientSearchObj,
																	NOTIFICATION_MSG_STATUS_FALSE)
															.then(
																	function(response) {
																		if(response.data.listObject.length == 0){
																			$scope.stopLoader();
																			growl.warning("No Result Found!",{
																				title : ERROR_MSG
																			});
																		}
																		else{
																			$scope.stopLoader();

																			angular.forEach(response.data.listObject,function(value,key)
																					{
																				  		if(value.registrationTypeId != 5)
																				  			{
																				  			$scope.patientList.push(value);
																				  			}
																				  		
																					});
																			
																		}
																	});
										}
							}
							
							
							//get patient doctor appointment
							$scope.getPatientDetailsById = function(patientId){
								$scope.patientDetails = {};
								var URI = BASE_URL + ROOT_URL + "/api/global/patient/billing/"+patientId;
								 BillingGenericService
									.serviceAction(METHOD_GET, URI, null,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												if (response.data.status == "success"){
													$scope.patientDetails = response.data.listObject[0];
												}
												$scope.searchObj.patientId=response.data.listObject[0].patientId;
											});
							      
							}
							
							     var currDate = new Date();
							     $scope.currentTime = moment(currDate).format('HH:mm');
							     $scope.currentDateCyclic=moment(currDate).format('YYYY-MM-DD');
						
							     //get scheduled patient list by filter 
							     $scope.getScheduledPatientList=function(){
							    	 
							    	 
							    	 if($scope.searchObj.patientId == undefined ||  $scope.searchObj.patientId == null || $scope.searchObj.patientId == 0 ){
							    		 
							    		 growl.warning("Please select patient",{
												title : ERROR_MSG
											});
							    		 
							    		 return false;
								    	}
							    	 
							    	 
							    	var searchReqObj={};
							    	$scope.scheduledAppointmentList=[];
							    	$scope.copyOfScheduledAppointmentList=[];
							    	  $scope.loadingMsg="Loading...";
							    	searchReqObj.organizationId=$scope.organizationId;
						    		searchReqObj.unitId=$scope.unitId;
							    	if($scope.searchObj.fromDate!="" && $scope.searchObj.toDate!=""){
							    		searchReqObj.fromDate=moment($scope.searchObj.fromDate).format('YYYY-MM-DD');
							    		searchReqObj.toDate=moment($scope.searchObj.toDate).format('YYYY-MM-DD');
							    	}
							    
							    	if($scope.searchObj.patientId>0){
							    		searchReqObj.patientId=$scope.searchObj.patientId;
							    	}
                                  
							    	$rootScope.startLoader();
							    	 var URI = BASE_URL + ROOT_URL + "/api/global/unitServiceMasterDetailsForSchedulingWorking";
							    	GenericService.serviceAction("POST", URI,searchReqObj).then(function(response) {
							    		if (response.data.status == "success"){
							    			if(response.data.listObject.length==0){
							    				$scope.loadingMsg="No appointments exist !!";
							    			}
							    			angular.forEach(response.data.listObject,function(value,index){
							    			
							    				var stringDate=moment(new Date()).format('DD/MM/YYYY');
							    				value.appointmentDate=moment(value.appointmentDate,'DD/MM/YYYY').toDate();
							    				value.appointmentTime=moment(stringDate+" "+value.appointmentTime+":00",'YYYY-MM-DD HH:mm').toDate();
							    				value.isSelected='N';
							    				value.uhid =  $scope.patientDetails.uhidNumber;
							    				value.patientDetails = $scope.patientDetails.prefixCode+" "+$scope.patientDetails.firstName+" "+$scope.patientDetails.middleName+" "+$scope.patientDetails.lastName;
							    				$scope.scheduledAppointmentList.push(value);
							    			});
							    			$scope.copyOfScheduledAppointmentList=angular.copy($scope.scheduledAppointmentList);
							    		}
							    		
							    		$rootScope.stopLoader();
							    	});
							    	
							     }
							     
							     
							     $scope.getDetails = function(info){
							    	 $scope.serviceDetailsList = [];
							    		var searchReqObj={};
								    	  $scope.loadingMsg="Loading...";
								    	searchReqObj.organizationId=$scope.organizationId;
							    		searchReqObj.unitId=$scope.unitId;
							    		searchReqObj.orderList = info.orderList;
							    		$rootScope.startLoader();
								    	 var URI = BASE_URL + ROOT_URL + "/api/global/unitServiceMasterDetailsForSchedulingWorkingDetails";
								    	GenericService.serviceAction("POST", URI,searchReqObj).then(function(response) {
								    		if (response.data.status == "success"){
								    			$('#pending_service_modalq').modal('show'); 
								    			if(response.data.listObject == null || response.data.listObject.length==0){
								    				$scope.loadingMsg="No appointments exist !!";
								    			}else{
								    				$scope.serviceDetailsList = response.data.listObject;
								    			}
								    			
								    			
								    			
								    		}
								    		
								    		$rootScope.stopLoader();
								    	});
							    	 
							     }
							     
							     
							     $scope.modalityId="";
							     $scope.clearDetails=function(){
										$scope.fromTimeString = "";
										$scope.fromTimeStringSlot="";
										$scope.slotId="";
										 $scope.modalityId="";
										 $scope.slotDetailsList=[];
										 $scope.loadingMsg="";
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
							     
									
									$scope.slotBooking = function(item)
									{
										$scope.fromTimeStringSlot = item.fromTimeString;
										$scope.fromTimeStringSlotShow = item.fromTimeString2;
										 $scope.slotId = item.slotId;
									}
							 	$scope.radSpecialityId = 0;
								$scope.radSubSpecialityId = 0;
								$scope.tempObj = {};
								$scope.selectedSpecialityId="";
								
								$scope.serIndex = 0;
								$scope.getModalityBySubSpeciality = function(item,index) {
									$scope.serIndex = index;
									$scope.tempObj = item;
									$scope.slotCheckTime = moment(new Date()).format("HH:mm");
									$scope.fromTimeString = "";
									$scope.modalityMasterList = [];
									$scope.admissionId = item.admissionId;
									$scope.radSpecialityId = item.specialityId;
									$scope.radSubSpecialityId = item.subSpecialityId;
									$scope.patientId = item.patientId;
									var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										specialityId : item.specialityId,
										subSpecialityId :item.subSpecialityId
									}
									console.log(data);
									if(item.specialityId==$scope.cookieObjectAppConfig.hemodialysisSpecialityId){
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
									
									
									if(item.specialityId!=$scope.cookieObjectAppConfig.hemodialysisSpecialityId){
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
									
								$scope.slotId=="";
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
										var data = {
											slotId : $scope.slotId,
											remark : "",
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											createdDate : $scope.currentDate,
											updatedDate : $scope.currentDate,
											createdBy : $scope.userId,
											updatedBy : $scope.userId,
											status : 'A',
											slotStatusId : 4,
											priorityId : 1,
											allowedOverBookingSlots : 0,
											admissionId : $scope.tempObj.admissionId,
											orderDetailsId:$scope.tempObj.orderDetailsId,
											appointmentStatusId:1,
											patientId :$scope.searchObj.patientId,
											encounterId :  $scope.tempObj.encounterId,
											modalityDesc : $scope.itemObj.modalityDesc,
											timeSlotShow :$scope.fromTimeStringSlotShow,
											timeSlot :$scope.fromTimeStringSlot,
											slotDate :  moment($scope.datepicker).format('YYYY-MM-DD')
										}
										if($scope.selectedSpecialityId==$scope.cookieObjectAppConfig.hemodialysisSpecialityId){
											data.appointmentTypeId=6;
										}else{
											data.appointmentTypeId=2;
										}
										
										$scope.serviceDetailsList[$scope.serIndex].modalityDto = data;
										angular.element('#infoq').modal('hide');
									    $rootScope.stopLoader();
										$scope.clearDetails();
										
											
									} 
							     
									//save service appointment
									$scope.saveCyclicServiceAppointments=function(){
										 
										$scope.finalAppointmentSlotList=[];
											angular.forEach($scope.serviceDetailsList,function(value,index){
												if(value.modalityDto!= undefined){
													var appointmentObj={};
													
													appointmentObj.appointmentTypeId = 10; //cyclic appointment
													appointmentObj.generateToken = 'N';
													appointmentObj.organizationId = $scope.organizationId;
													appointmentObj.unitId = $scope.unitId;
													appointmentObj.createdBy = $scope.userId;
													appointmentObj.updatedBy = $scope.userId;
													appointmentObj.status = 'A';
													appointmentObj.patientId =$scope.searchObj.patientId;
													appointmentObj.isFromCoversheet = 'N'; 
													appointmentObj.isCyclicAppointment = 'Y';
													appointmentObj.appointmentStatusId=1;
													appointmentObj.serviceAmount=value.serviceAmount;
													appointmentObj.serviceMasterId=value.serviceMasterId;
													appointmentObj.billId=value.id;
													appointmentObj.subSpecialityId=value.subSpecialityId;
													appointmentObj.specialityId= value.specialityId;
													appointmentObj.defaultSelfTariffId=$scope.defaultSelfTariffId;
													appointmentObj.taxId=value.taxId;
													appointmentObj.taxPercentage=value.taxPercentage;
													appointmentObj.appointmentDate2 = null;
													appointmentObj.appointmentDate =  value.modalityDto.slotDate;
													appointmentObj.orderDetailsPackageMapperDto={};
													appointmentObj.orderDetailsPackageMapperDto.payeeId=$scope.selfPayeeId;
													appointmentObj.encounterId=value.encounterId;
													appointmentObj.admissionId=value.admissionId;
													appointmentObj.orderDetailsId=value.orderDetailsId;
													appointmentObj.orderId=value.orderId;
													appointmentObj.visitTypeId=value.visitTypeId;
													appointmentObj.slotId = value.modalityDto.slotId;
													appointmentObj.appointmentTime = value.modalityDto.timeSlot;
													if(value.packageId != undefined &&  value.packageId>0){
														appointmentObj.parentPackageId=value.packageId;
													}
													$scope.finalAppointmentSlotList.push(appointmentObj);
													
												}
												
											});
										
										$rootScope.startLoader();
										//var URI = BASE_URL + ROOT_URL + "/api/opd/generateCyclicServiceAppointment";
										var URI = BASE_URL + ROOT_URL + "/api/billing/generateCyclicServiceAppointment";
										//console.log(JSON.stringify($scope.setCreatedSlotList[i]));
										BillingGenericService.serviceAction(METHOD_POST, URI,$scope.finalAppointmentSlotList,NOTIFICATION_MSG_STATUS_FALSE)
										.then(function(response) {
											console.log(response);
											$('#pending_service_modalq').modal('hide'); 
											$scope.datepicker = new Date();
											if (response.data.status == "success") {
													growl.success("Appointments Added Successfully",
															{
																title : 'Success!'
															});
											  } 
											$rootScope.stopLoader();
											$scope.getScheduledPatientList();
											});
									}
									
								
						} 
					]);
