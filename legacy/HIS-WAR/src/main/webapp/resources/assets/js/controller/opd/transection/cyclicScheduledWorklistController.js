'use strict';

angular
		.module('myApp')
		.controller(
				'cyclicScheduledWorklistController',
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
							//get scheduled appointment list
						    	var searchReqObj={};
						    		searchReqObj.fromDate=moment(new Date()).format('YYYY-MM-DD');
						    		searchReqObj.toDate=moment(new Date()).format('YYYY-MM-DD');
						    		searchReqObj.organizationId=$scope.organizationId;
						    		searchReqObj.unitId=$scope.unitId;
						    		 $scope.loadingMsg="Loading...";
						    	 var URI4 = BASE_URL + ROOT_URL + "/api/opd/getScheduledServiceAppointmentWorklist";
						    	GenericService.serviceAction("POST", URI4,searchReqObj).then(function(response) {
						    		if (response.data.status == "success"){
						    			if(response.data.listObject.length==0){
						    				$scope.loadingMsg="No appointments exist !!";
						    			}
						    			angular.forEach(response.data.listObject,function(value,index){
						    				/*value.specialityId=0;
						    				value.doctorId=0;*/
						    				var stringDate=moment(new Date()).format('DD/MM/YYYY');
						    				value.appointmentDate=moment(value.appointmentDate,'DD/MM/YYYY').toDate();
						    				value.appointmentTime=moment(stringDate+" "+value.appointmentTime+":00",'YYYY-MM-DD HH:mm').toDate();
						    				value.isSelected='N';
						    				if(value.specialityId!=null && value.doctorId!=null){
						    					$scope.getDoctorBySpeciality(value.specialityId,index);
						    				}else{
						    					value.doctorList=[];
						    				}
						    				//value.doctorList=[];
						    				$scope.scheduledAppointmentList.push(value);
						    			});
						    			$scope.copyOfScheduledAppointmentList=angular.copy($scope.scheduledAppointmentList);
						    		}
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
								/*if($scope.patientSearchObj.uhidNumber == '' && $scope.patientSearchObj.patientName == '' && $scope.patientSearchObj.genderId == '' && $scope.patientSearchObj.mobileNumber == '' && $('#datepickerSearch').val() == '' && $scope.patientSearchObj.identificationTypeId == '' && $scope.patientSearchObj.identificationNumber == '')
				           			{
											growl.error(SearchFilter_REQUIRED,{title : "WARNING!"
											});
											return false;
					           		}*/
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
                                  
							    	if($scope.searchObj.serviceId>0){
							    		searchReqObj.serviceId=$scope.searchObj.serviceId;
							    	}
							    	$rootScope.startLoader();
							    	 var URI = BASE_URL + ROOT_URL + "/api/opd/getScheduledServiceAppointmentWorklist";
							    	GenericService.serviceAction("POST", URI,searchReqObj).then(function(response) {
							    		if (response.data.status == "success"){
							    			if(response.data.listObject.length==0){
							    				$scope.loadingMsg="No appointments exist !!";
							    			}
							    			angular.forEach(response.data.listObject,function(value,index){
							    				/*value.specialityId=0;
							    				value.doctorId=0;*/
							    				var stringDate=moment(new Date()).format('DD/MM/YYYY');
							    				value.appointmentDate=moment(value.appointmentDate,'DD/MM/YYYY').toDate();
							    				value.appointmentTime=moment(stringDate+" "+value.appointmentTime+":00",'YYYY-MM-DD HH:mm').toDate();
							    				value.isSelected='N';
							    				if(value.specialityId!=null && value.doctorId!=null){
							    					$scope.getDoctorBySpeciality(value.specialityId,index);
							    				}else{
							    					value.doctorList=[];
							    				}
							    				$scope.scheduledAppointmentList.push(value);
							    			});
							    			$scope.copyOfScheduledAppointmentList=angular.copy($scope.scheduledAppointmentList);
							    		}
							    		$scope.searchObj.patientId=0;
							    		$rootScope.stopLoader();
							    	});
							    	
							     }
							     
							   //reschedule cyclic service appointment
									$scope.rescheduleCyclicServiceAppointment=function(){
										$scope.rescheduleAppointmentSlotList=[];
										for(let i=0;i<$scope.scheduledAppointmentList.length;i++){
										if($scope.scheduledAppointmentList[i].isSelected=='Y'){
											 if(moment($scope.copyOfScheduledAppointmentList[i].appointmentDate).format('DD-MM-YYYY') == moment($scope.scheduledAppointmentList[i].appointmentDate).format('DD-MM-YYYY') 
													 && moment($scope.copyOfScheduledAppointmentList[i].appointmentTime).format('HH:MM') == moment($scope.scheduledAppointmentList[i].appointmentTime).format('HH:MM') ){
												
												/* if((($scope.scheduledAppointmentList[i].doctorId==null || $scope.scheduledAppointmentList[i].specialityId==null) && $scope.copyOfScheduledAppointmentList[i].isDoctorShareReq=='Y')){
													 growl.warning(
																'Please select speciality and doctor to process cyclic appointment',
																{
																	title : 'Warning!'
																});
				                            		return false;
												 }*/
												 
											 }
											 
											 //else{ 
												if($scope.scheduledAppointmentList[i].appointmentDate==undefined){
													 growl.warning("Scheduled date is incorrect.",
																{
																	title : 'Warning!'
																});
													  return false;
												}
											  if(moment($scope.scheduledAppointmentList[i].appointmentDate).format('YYYY-MM-DD')<$scope.currentDate){
												  $scope.scheduledAppointmentList[i].appointmentDate=$scope.copyOfScheduledAppointmentList[i].appointmentDate;
												  growl.warning("Scheduled date can not be back date.",
															{
																title : 'Warning!'
															});
												  return false;
											  }	 
											var newArray={};
											newArray.organizationId = $scope.organizationId;
											newArray.unitId = $scope.unitId;
											newArray.createdBy = $scope.userId;
											newArray.updatedBy = $scope.userId;
											newArray.createdDate=moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
											newArray.status = 'A';
											newArray.patientId = $scope.scheduledAppointmentList[i].patientId;
											newArray.encounterId = $scope.scheduledAppointmentList[i].encounterId;
											newArray.serviceId= $scope.scheduledAppointmentList[i].serviceId;
											newArray.patientCategoryId=$scope.scheduledAppointmentList[i].patientCategoryId;
											newArray.defaultSelfTariffId=$scope.defaultSelfTariffId;
											newArray.orderDetailsId=$scope.scheduledAppointmentList[i].orderDetailsId;
											newArray.payeeId=$scope.selfPayeeId;
											newArray.bedId=$scope.scheduledAppointmentList[i].bedId!=null ? $scope.scheduledAppointmentList[i].bedId:$scope.opdBillingCatId;
											newArray.admissionId=$scope.scheduledAppointmentList[i].admissionId;
											newArray.visitTypeId=$scope.scheduledAppointmentList[i].visitTypeId;
											//$scope.serviceSlotListForReschedule[i].appointmentDate =  moment($scope.datepicker).format('DD/MM/YYYY');
											newArray.appointmentDate = moment($scope.scheduledAppointmentList[i].appointmentDate).format('DD-MM-YYYY');
											newArray.appointmentTime= moment($scope.scheduledAppointmentList[i].appointmentTime).format('HH:mm');
											newArray.appointmentId = $scope.scheduledAppointmentList[i].appointmentId;
											newArray.doctorId=$scope.scheduledAppointmentList[i].doctorId;
											newArray.isDoctorShareReq=$scope.scheduledAppointmentList[i].isDoctorShareReq;
											newArray.specialityId=$scope.scheduledAppointmentList[i].specialityId;
											newArray.serviceSpecialityId=$scope.scheduledAppointmentList[i].serviceSpecialityId;
											newArray.labSpecialityId=$scope.labSpecialityId;
											$scope.rescheduleAppointmentSlotList.push(newArray);
										 //}
										}	 
									 }
										
										if($scope.rescheduleAppointmentSlotList.length==0){
											 growl.warning(
														'Select at least one patient',
														{
															title : 'Warning!'
														});
											return false;
										}
										$rootScope.startLoader();
										var URI = BASE_URL + ROOT_URL +  "/api/opd/rescheduleCyclicServiceAppointment";
										//console.log(JSON.stringify($scope.setCreatedSlotList[i]));
										BillingGenericService.serviceAction(METHOD_POST, URI,$scope.rescheduleAppointmentSlotList,NOTIFICATION_MSG_STATUS_FALSE)
										.then(function(response) {
											console.log(response);
											if (response.data.status == "success") {
													growl.success("Appointments Updated Successfully",
															{
																title : 'Success!'
															});
											}
											
											$scope.init();
											$rootScope.stopLoader();
											});
										
									}
									
									$scope.selectDeselectCheckbox=function(){
										if($scope.allSelected=='Y'){
											angular.forEach($scope.scheduledAppointmentList,function(value,index){
							    				value.isSelected='Y';
							    			});
										}else{
											angular.forEach($scope.scheduledAppointmentList,function(value,index){
							    				value.isSelected='N';
							    			});
										}
									}
									
//							=================================================================================	
								/*	
															
								
									$scope.getDoctorBySpeciality = function (specialityId,idx) {

										var data = {
											specialityId: specialityId,
											unitId: $scope.unitId,  
											organizationId: $scope.organizationId
										};
										var URI = BASE_URL + ROOT_URL+ GETDOCTORBYSPECIALITYID;

										BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
											.then(
												function (response) {
													if (response.data.status == "success")
														{
														 if(idx!=null && idx!=undefined)
															 {
															 $scope.scheduledAppointmentList[idx].doctorList =  response.data.listObject
															 }
														}
													else if (response.data.status == "error")
													{
														$rootScope.stopLoader();
													}
												});

									}
								*/
									var currDate = new Date();
									$scope.currentDate=moment(currDate).format('YYYY-MM-DD');
									$scope.checkDate = function(appDate,idx)
									{
										$scope.originalSchDate=$scope.copyOfScheduledAppointmentList[idx].appointmentDate;
										if(appDate==undefined){
											 growl.warning("Scheduled date is incorrect.",
														{
															title : 'Warning!'
														});
											  return false;
										}
									  if(moment(appDate).format('YYYY-MM-DD')<$scope.currentDate){
										  $scope.scheduledAppointmentList[idx].appointmentDate=$scope.originalSchDate;
										  growl.warning("Scheduled date can not be back date.",
													{
														title : 'Warning!'
													});
										  return false;
									  }
								  }
						} 
						]);

