'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:appointmentListController
 * @description #appointmentListController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'appointmentListController',
				[
						'$scope',
						'$cookies',
						'$rootScope',
						'GenericService',
						'BillingGenericService',
						'$state',
						'$timeout',
						'$interval',
						'$filter',
						'growl',
						'reportService',
						'Pubnub',
						'servicesFactory',
						'PaginationService',
						'$window','$sce',
						function($scope, $cookies, $rootScope, GenericService,
								BillingGenericService, $state, $timeout,$interval,$filter,growl,reportService,Pubnub,servicesFactory,PaginationService,$window,$sce) {

							//alert("appointmentListController");
							$rootScope.loginpage = true;
							/**================================COUNTER CODE START======================================*/
							$scope.isCounterApplicable = JSON.parse(sessionStorage.getItem('isCounterApplicable'));
							$scope.isUserCounterApplicable = JSON.parse(sessionStorage.getItem('isUserCounterApplicable'));
							if($scope.isCounterApplicable=='Y' && $scope.isUserCounterApplicable=='Y')
								{
								     var flag = $scope.getCounterDetails();
								     if(flag)
								    	 {
								    	 //$rootScope.counterMstObj = $cookies.getObject('counterMstObj');
								    	 	$rootScope.counterId = $rootScope.counterMstObj.counterId!=""?$rootScope.counterMstObj.counterId:"";
								    	 	$rootScope.counterDesc = $rootScope.counterMstObj.counterDesc!=""?$rootScope.counterMstObj.counterDesc:"";
								    	 }
								     else
								     {
								    	 $rootScope.counterList = JSON.parse(localStorage.getItem('counterList'));
								     }
								}else{
									$rootScope.counterId = 0;
								}
							/**================================COUNTER CODE END======================================*/
							
							
							/* init() function for form object create. */
							$scope.init = function() {
								var date = new Date();
								$scope.dateOptionsToForm = {
										formatYear : 'yyyy',
										showWeeks : false,
										minDate : date.setDate((new Date()).getDate() - 90),
									};

								$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate: new Date()
								};
								$scope.dateOptionsReschdule = {
										formatYear : 'yyyy',
										showWeeks : false,
										minDate: new Date()
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
										$scope.datepickerFromDate.opened = true;
								};

								$scope.datepickerFromDate = {
										opened : false
								};
								
								//Date For To Date on Search
								
								$scope.openDatePickerToDate = function() {
										$scope.datepickerToDate.opened = true;
								};

								$scope.datepickerToDate = {
										opened : false
								};
								
								$scope.radioToken = 'R';
								$scope.tokenPatientId = 0;
								
								$scope.datepickerFromDate = new Date();
								$scope.datepickerToDate = new Date();

								/*** Check user is Loggedin */
								var cookieObject,cookieObjectAppConfig;
								[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.unitCountryId = cookieObject.unitCountryId;
								$scope.unitStateId = cookieObject.unitStateId;
								$scope.unitDistrictId = cookieObject.unitDistrictId;
								$scope.unitCityId = cookieObject.unitCityId;
								$scope.unitAreaId = cookieObject.unitAreaId;
								$scope.unitZipCode = cookieObject.unitZipCode;
								$scope.unitName=cookieObject.unitNme;
								$scope.isKiosk = cookieObjectAppConfig.isKiosk;
								$scope.isAppointmentRequired = cookieObjectAppConfig.isAppointmentRequired;
								$scope.isKioskAnnouncementRequired = cookieObjectAppConfig.isKioskAnnouncementRequired;
								$scope.isEHCReq =  cookieObjectAppConfig.isEHCReq;
								$scope.isRadiologyReq =  cookieObjectAppConfig.isRadiologyReq;
								$scope.isDocConsultReq =  cookieObjectAppConfig.isDocConsultReq;
								$scope.dateFormat =  cookieObjectAppConfig.dateFormat;
								$scope.selectedTemplates = [];
								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								$scope.disableCheckBox = false;
								$scope.slotTypeId = 1;

								$scope.selectAppointment = [];
								
								$scope.doctorSearchFlag = 'N';
								$scope.patientSearchFlag = 'N';
								
								$scope.listStatusId = 0;

								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								var data = {
										organizationId : $scope.organizationId
								};
								
								$scope.appointmentMasterObj = {};
								$scope.options =  {
								          width: 1,
								          height: 15,
								          displayValue: false,
								          font: 'monospace',
								          textAlign: 'center',
								          textPosition: 'bottom',
								          fontSize: 10,
								          backgroundColor: '#ffffff',
								          lineColor: '#000000',
								          margin: 0
								        };

								var doctorModalityObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId,
									appointmentTypeId : 1,
									appointmentStatusId : 0,
									appointmentDate:moment(new Date()).format('YYYY-MM-DD'),
									patientId:0,
									specialityId:0,
									doctorId:0,
									specialityId:0,
									subSpecialityId:0,
									modalityId:0,
									visitTypeId:0,
									isAppointmentRequired : $scope.isAppointmentRequired
								};

								//For Get Appointment List
								var URI = BASE_URL + ROOT_URL
										+ GETAPPOINTMENTSCHEDULELIST;
								BillingGenericService
										.serviceAction(METHOD_POST, URI,
												doctorModalityObj,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.appointmentList = response.data.listObject;
													 angular.forEach($scope.appointmentList,function(value,index) {
														 value.property = value.appointmentDate +" "+value.fromTimeString;
													 });
													    $scope.allCount =response.data.listObject.length;
													    //$scope.pendingCount = response.data.object[0].pending;
													    //$scope.checkInCount = response.data.object[0].checkIn;
													    //$scope.rescheduledAndCancelledCount  = response.data.object[0].rescheduledAndCancelled;
													    
													    $scope.pendingCount = 0;
													    $scope.checkInCount = 0;
													    $scope.rescheduledAndCancelledCount  = 0;
													    $scope.completeCount  = 0;
													    $scope.checkOutCount  = 0;
													    $scope.admittedCount  = 0;
													    $scope.inprocessCount = 0;
													    
													    angular.forEach($scope.appointmentList,function(value,index) {
																
													    	 switch (value.appointmentStatusId) {
													            case 1:
													            	 $scope.pendingCount +=1;
													                break;
													            case 2:
													            	$scope.rescheduledAndCancelledCount +=1;
													            	break;
													            case 3:
													            	$scope.rescheduledAndCancelledCount +=1;
													            	break;
													            case 5:
													            	$scope.checkInCount +=1;
													                break;
													            case 6:
													            	 $scope.completeCount +=1;
													            	 break;
													            case 7:
													            	 $scope.checkOutCount +=1;
													            	 break;
													            case 8:
													            	 $scope.inprocessCount +=1;
													            	 break;
													            default:

													        }
													    	 /*if(value.admissionId > 0)
													    		 {
													    		 	$scope.admittedCount +=1;
													    		 	
													    		 }*/
													    	 $scope.admittedCount=0;
														});
													    $scope.allCount += $scope.admittedCount;
												});
								
								var URI = BASE_URL + ROOT_URL
								+ BLOCKEDSCHEDULEAPPOINTMENTLIST;
						GenericService
								.serviceAction("POST", URI, doctorModalityObj)
								.then(
										function(response) {
											$scope.reScheduleObjList = [];

											if (response.data.status == "success") {
												$scope.reScheduleObjList = response.data.listObject;
												
												console.log("resdlist",$scope.reScheduleObjList);

											}
										});
								
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
								
								//For Get Active Speciaity List
									var URI = BASE_URL + ROOT_URL
										+ GETACTIVESPECIALITY;
								     BillingGenericService
										.serviceAction(METHOD_POST, URI,
												commonObj,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.specialityList = response.data.listObject;
													
												});
								     

										//For Get Active Speciaity List
											/*var URI = BASE_URL + ROOT_URL
												+ GETACTIVEMODALITYTYPELIST;
										     BillingGenericService
												.serviceAction(METHOD_POST, URI,
														commonObj,
														NOTIFICATION_MSG_STATUS_FALSE)
												.then(
														function(response) {
															console.log(response);
															// $rootScope.startLoader();
															if (response.data.status == "success")
																$scope.modalityTypeList = response.data.listObject;
														});*/
										     
								     
								     var URI = BASE_URL + ROOT_URL
										+ GETACTIVEGENDERLIST;
								BillingGenericService
										.serviceAction(METHOD_GET, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.sexMasterList = [];
													if (response.data.status == "success")
														$scope.sexMasterList = response.data.listObject;
													
												});
										

										var URI = BASE_URL + ROOT_URL
												+ GETACTIVEIDENTIFICATION;
										BillingGenericService
										.serviceAction(METHOD_GET, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.identificationList = [];
													if (response.data.status == "success")
														$scope.identificationList = response.data.listObject;
													
												});
										
								     
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
								     
								 	//Object For Patient Search
										$scope.patientSearchObj = {
											"uhidNumber" : "",
											"patientName" : "",
											"genderId" : "",
											"mobileNumber" : "",
											"dob" : "",
											"identificationNumber" : "",
											"identificationTypeId" : "",
											"unitId" : $scope.unitId,
											"organizationId" : $scope.organizationId,
											"registrationTypeIdList" :[1,2,3,4,5,6]
										}

							}

							$scope.init();
							
							
							$scope.sortBy = function (appointmentList, property ,reverse) {
								       $scope.appointmentList= [];
								       $scope.appointmentList = $filter('orderBy')(appointmentList, property, reverse);
								       console.log($scope.appointmentList);
							       return appointmentList = $scope.appointmentList;
							      
							    }
							 
							$scope.setObject = function(item) {
								$scope.selectAppointment = [];
								$scope.selectAppointment = item;
								
								$scope.datepickerReschedule = new Date($rootScope.getFormatedDateeShase(item.appointmentDate));
								$scope.reschedulledBy = "C";
								$scope.rescheduleReasonId = "";
							}
							
							$scope.getActivePatientTypeList = function()
						    {
						        var URI = BASE_URL + ROOT_URL +"/api/billing/getPatientType/"+$scope.organizationId+"/"+$scope.unitId;
						    GenericService
						        .serviceAction(METHOD_GET, URI,
						                NOTIFICATION_MSG_STATUS_FALSE)
						        .then(
						                function(response) {
						                    console.log(response);
						                    if (response.data.status == "success"){
						                    	 $scope.patientTypeList = [];
						                        $scope.patientTypeList = response.data.listObject;
						                        console.log("list",$scope.patientTypeList);
						                    }
						                    
						                });
						        
						    }
							$scope.getActivePatientTypeList();
							$scope.isTeleconsultationAppointment = false;
							$scope.checkValue = function(value){
								console.log(value);
								$scope.isTeleconsultationAppointment =value;
							}
							
							 $scope.closeModel=function(){
							 if($scope.clearbtn && $scope.saved){
							  $scope.appointmentMasterObj.referralTypeId='';
                             	$scope.appointmentMasterObj.referralDesc='';
                             	$scope.appointmentMasterObj.referralRemark='';
							   $scope.clearbtn=true;
							   $scope.saved=true;
							 }
							 else{
							 $scope.clearbtn=false;
                              $scope.saved=false;
							 }
							 angular.element('#editEncounterModal').modal('hide');
							 }

							$scope.editPatientVisitDetails = function(info)
						    {
						      if($scope.appointmentMasterObj.referralTypeId=="" || $scope.appointmentMasterObj.referralDesc == "")
                                                     {
                             							$scope.clearbtn=true
                                                         $scope.saved=true
                             						}
                             						else{
                             							$scope.clearbtn=false
                             								$scope.saved=false
                             						}
								console.log("info",info);
								$scope.appointmentMasterObj={};
								$scope.initKinDetails();
								$scope.updateEditPatientVisitDetailsBtn = false;
								$scope.appointmentMasterObj.patientId = info.patientId;
								$scope.appointmentMasterObj.encounterId = info.encounterId;
								$scope.appointmentMasterObj.remark = info.encRemark;
								$scope.appointmentMasterObj.referralId = info.referralId;
                                $scope.appointmentMasterObj.referalDetailsId = info.referalDetailsId;
                                $scope.appointmentMasterObj.referralDesc = info.referralDesc;
                                $scope.appointmentMasterObj.referralRemark = info.referralRemark;

								$scope.appointmentMasterObj.appointmentId =  info.appointmentId;




								if(info.isTeleconsultationAppointment == 1){
									$scope.isTeleconsultationAppointment = true;
								}else{
									$scope.isTeleconsultationAppointment = false;
								}
								
								if(info.sourceId !=null){
								$scope.appointmentMasterObj.sourceId = info.sourceId.toString();
								}
								if(info.patientClassId != null){
									$scope.appointmentMasterObj.patientClassId = info.patientClassId.toString();
								}
								if( info.referralTypeId != null){
									$scope.appointmentMasterObj.referralTypeId = info.referralTypeId.toString();
								}
							    if(info.referalDetailsId != null){
							    	$scope.appointmentMasterObj.referalDetailsId = info.referalDetailsId.toString();
							    }
							     if($scope.clearbtn && $scope.saved){
                                    //$scope.appointmentMasterObj.referralId = "";
                                    $scope.appointmentMasterObj.referralTypeId="";
                                    // $scope.appointmentMasterObj.referalDetailsId = info.referalDetailsId.toString();
                                      $scope.appointmentMasterObj.referralDesc = "";
                                      $scope.appointmentMasterObj.referralRemark = "";
                                      $scope.clearbtn=false;
                                      $scope.saved=false;
                                   }
								
								$scope.getKinDetailsByPatientId(); 
						    }
							$scope.saved=false;
							 var flagKin=false;
							 $scope.updateEditPatientVisitDetailsBtn = false;
							$scope.updateEditPatientVisitDetails = function()
						    {

								var checkKinPri = false;
								 var checkKinName = false;
								 var checkKinMobile=false;
								 var checkKinRelation=false;
								 var checkKinPhone=false;
								 var checkKinAddress=false;
								 var checkKinContry=false;
								 var checkKinState=false;
								 var checkKinDistrict=false;
								 var checkKinCity=false;
								 var checkKinArea=false;
								 var checkKinPost=false;
								 var checkKinIdenti=false;
								 var checkKinIdentiNo=false;
								 $scope.updateEditPatientVisitDetailsBtn = true;
								 angular.element('#editEncounterModal').modal('hide');
								$rootScope.startLoaderPopup();
								var data ={
										
										patientClassId:$scope.appointmentMasterObj.patientClassId,
										sourceId:$scope.appointmentMasterObj.sourceId,
										remark:$scope.appointmentMasterObj.remark,
										referralTypeId:$scope.appointmentMasterObj.referralTypeId,
										referalDetailsId:$scope.appointmentMasterObj.referalDetailsId,
										referralId : $scope.appointmentMasterObj.referralId,
										referralRemark : $scope.appointmentMasterObj.referralRemark,
										listKinDetailsDto : $scope.kinDetailsList,
										kinDetailsId : $scope.appointmentMasterObj.kinDetailsId,
										status:'A',
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										updatedBy : $scope.userId,
										createdBy : $scope.userId,
										encounterId : $scope.appointmentMasterObj.encounterId,
										patientId : $scope.appointmentMasterObj.patientId,
										appointmentId : $scope.appointmentMasterObj.appointmentId,
										isTeleconsultationAppointment : ($scope.isTeleconsultationAppointment == false?0:1)
										
								}
								angular.forEach($scope.kinDetailsList,function(value,key){

									if($scope.registrationFieldObj.kinprefix== "Y"){
										if(value.prefixId == undefined || value.prefixId == null || value.prefixId ==""){
											checkKinPri = true;
											$scope.updateEditPatientVisitDetailsBtn = false;
										}
									}
									 if($scope.registrationFieldObj.kinName== "Y"){
										if(value.kinName == undefined || value.kinName == null || value.kinName ==""){
											checkKinName = true;
											$scope.updateEditPatientVisitDetailsBtn = false;
										}
									}
									 if($scope.registrationFieldObj.relationId== "Y"){
											if(value.relationId == undefined || value.relationId == null || value.relationId ==""){
												checkKinRelation = true;
												$scope.updateEditPatientVisitDetailsBtn = false;
											}
										}
									 if($scope.registrationFieldObj.kinMobileNo== "Y"){
											if(value.mobileNo == undefined || value.mobileNo == null || value.mobileNo ==""){
												checkKinMobile = true;
												$scope.updateEditPatientVisitDetailsBtn = false;
											}
										}
									
									 if($scope.registrationFieldObj.phoneNo== "Y"){
											if(value.phoneNo == undefined || value.phoneNo == null || value.phoneNo ==""){
												checkKinPhone=true;
												$scope.updateEditPatientVisitDetailsBtn = false;
											}
										}
									 if($scope.registrationFieldObj.kinAddress== "Y"){
											if(value.address == undefined || value.address == null || value.address ==""){
												checkKinAddress=true;
												$scope.updateEditPatientVisitDetailsBtn = false;
											}
										}
									 if($scope.registrationFieldObj.countryId== "Y"){
											if(value.countryId == undefined || value.countryId == null || value.countryId ==""){
												checkKinContry=true;
												$scope.updateEditPatientVisitDetailsBtn = false;
											}
										}
									 if($scope.registrationFieldObj.stateId== "Y"){
											if(value.stateId == undefined || value.stateId == null || value.stateId ==""){
												checkKinState=true;
												$scope.updateEditPatientVisitDetailsBtn = false;
											}
										}
									 if($scope.registrationFieldObj.districtId== "Y"){
											if(value.districtId == undefined || value.districtId == null || value.districtId==""){
												checkKinDistrict=true;
												$scope.updateEditPatientVisitDetailsBtn = false;
											}
										}
										 if($scope.registrationFieldObj.cityId== "Y"){
											if(value.cityId == undefined || value.cityId == null || value.cityId ==""){
												checkKinCity=true;
												$scope.updateEditPatientVisitDetailsBtn = false;
											}
										}
										 if($scope.registrationFieldObj.areaId== "Y"){
											if(value.areaId == undefined || value.areaId == null || value.areaId ==""){
												checkKinArea=true;
												$scope.updateEditPatientVisitDetailsBtn = false;
											}
										}
										 if($scope.registrationFieldObj.postCode== "Y"){
											if(value.postCode == undefined || value.postCode == null ||value.postCode ==""){
												checkKinPost=true;
												$scope.updateEditPatientVisitDetailsBtn = false;
											}
										}
										 if($scope.registrationFieldObj.identificationId== "Y"){
											if(value.identificationId == undefined || value.identificationId == null ||value.identificationId ==""){
												checkKinIdenti=true;
												$scope.updateEditPatientVisitDetailsBtn = false;
											}
										}
										 if($scope.registrationFieldObj.identificationNo== "Y"){
											if(value.identificationNo == undefined || value.identificationNo == null || value.identificationNo ==""){
												checkKinIdentiNo=true;
												$scope.updateEditPatientVisitDetailsBtn = false;
											}
										}
							 });
								 if(checkKinPri == true){
									 growl.warning("Please Add Prefix For Next Of KIN",{
										});
									 flagKin = true;
									 $rootScope.stopLoaderPopup();
										return false;
								 }
								 if(checkKinName == true){
									 growl.warning("Please Add Name For Next Of KIN",{
										});
									 flagKin = true;
									 $rootScope.stopLoaderPopup();
										return false;
								 }
								 if(checkKinRelation == true){
									 growl.warning("Please Add Relation For Next Of KIN",{
										});
									 flagKin = true;
									 $rootScope.stopLoaderPopup();
										return false;
								 }
								 if(checkKinMobile == true){
									 growl.warning("Please Add Mobile No For Next Of KIN",{
										});
									 flagKin = true;
									 $rootScope.stopLoaderPopup();
										return false;
								 }
								 if(checkKinPhone == true){
									 growl.warning("Please Add Phone No For Next Of KIN",{
										});
									 flagKin = true;
									 $rootScope.stopLoaderPopup();
										return false;
								 }
								 if(checkKinAddress == true){
									 growl.warning("Please Add Address For Next Of KIN",{
										});
									 flagKin = true;
									 $rootScope.stopLoaderPopup();
										return false;
								 }
								 if(checkKinContry == true){
									 growl.warning("Please Add Country For Next Of KIN",{
										});
									 flagKin = true;
									 $rootScope.stopLoaderPopup();
										return false;
								 }
								 if(checkKinState == true){
									 growl.warning("Please Add State For Next Of KIN",{
										});
									 flagKin = true;
									 $rootScope.stopLoaderPopup();
										return false;
								 }
								 if(checkKinDistrict == true){
									 growl.warning("Please Add District For Next Of KIN",{
										});
									 flagKin = true;
									 $rootScope.stopLoaderPopup();
										return false;
								 }
								 if(checkKinCity == true){
									 growl.warning("Please Add City For Next Of KIN",{
										}); 
									 flagKin = true;
									 $rootScope.stopLoaderPopup();
										return false;
								 }
								 if(checkKinArea == true){
									 growl.warning("Please Add Area For Next Of KIN",{
										});
									 flagKin = true;
									 $rootScope.stopLoaderPopup();
										return false;
								 }
								 if(checkKinPost == true){
									 growl.warning("Please Add Post Code For Next Of KIN",{
										});
									 flagKin = true;
									 $rootScope.stopLoaderPopup();
										return false;
								 }
								 if(checkKinIdenti == true){
									 growl.warning("Please Add Identification Type For Next Of KIN",{
										});
									 flagKin = true;
									 $rootScope.stopLoaderPopup();
										return false;
								 }
								 if(checkKinIdentiNo == true){
									 growl.warning("Please Add Identification No For Next Of KIN",{
										});
									 flagKin = true;
									 $rootScope.stopLoaderPopup();
										return false;
								 }
								
								var URI = BASE_URL + ROOT_URL + "/api/opd/updateEncounterDetailsPatientByEncounterId";
								GenericService.serviceAction("POST",URI,data).then(function(response){
									if (response.data.status == 'success')
									{
										$rootScope.stopLoaderPopup();
										$scope.searchAppointment();
										$scope.saved=true;
										 $scope.updateEditPatientVisitDetailsBtn = false;
										//$scope.SearchObjectClear();
										$scope.getKinDetailsByPatientId();
										$scope.initKinDetails();
										growl.success('Updated Successfully',
												{
													title : 'Success!'
												});
										angular.element('#editEncounterModal').modal('hide');
									} 
								});
						    }
							$scope.getReferralList = function(){
								var URI = BASE_URL + ROOT_URL + GETACTIVEREFERRALTYPELIST;
								var obj={organizationId : $scope.organizationId};
									BillingGenericService.serviceAction(METHOD_POST, URI, obj).then(function(response) {
											$scope.referralTypeList = [];
											if (response.data.status == "success") {
												$scope.referralTypeList = response.data.listObject;
											}
									});
								};
							$scope.getReferralList();
							
							$scope.getSourceMasterList = function(){
								var URI = BASE_URL + ROOT_URL + GETACTIVEPATIENTSOURCELIST+"/"+$scope.organizationId;
								BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
											$scope.sourceMasterList = [];
											if (response.data.status == "success") {
												$scope.sourceMasterList = response.data.listObject;
											}
										});
							}
							$scope.getSourceMasterList();
							
							
					        $scope.searchByreferralId = function(searchKeyword,index){
					        	
								var data={
					        			organizationId: $scope.organizationId,
					        			referralTypeId:$scope.appointmentMasterObj.referralTypeId,
					        			desc: searchKeyword
					        	};
								var URI = BASE_URL + ROOT_URL + "/adt/getReferralListByReferralTypeId/forTypeSearch" 
								
					    		return GenericService.serviceAction("POST",URI,data).then(function(response){
					    			if(response.data.status == "success"){
					    				$scope.referralMasterList = [];
					    				$scope.referralMasterList = response.data.listObject;
					    			}
					    			return $scope.referralMasterList;
					    		});
					    	}
					        
					        
					    	$scope.selectReferralId = function(info, model, label,ind) {
								console.log(info);
								$scope.appointmentMasterObj.referralId = info.referralId;
					    	}
					    	
					    	$scope.initKinDetails = function(){
					  		  /* Add Next Of Kin Details */
					    		$scope.patientObj = {};
					  	    $scope.kinDetailsList = [];
					  	    $scope.kinDetails = {
					  				prefixId   		: '',
					  				kinName  		: '',
					  				relationId 		: '',
					  				mobileNo 		: '',
					  				phoneNo 		: '',
					  				expiry 			: '',
					  				identificationId: '',
					  				identificationNo: '',
					  				isGuarantor 	: 'N',
					  				address 		: '',
					  				countryId 		: $scope.unitCountryId!=undefined && $scope.unitCountryId !=null ? $scope.unitCountryId : '',
					  				stateId 		:  $scope.unitStateId!=undefined && $scope.unitStateId !=null ? $scope.unitStateId : '',
					  				districtId 		:  $scope.unitDistrictId!=undefined && $scope.unitDistrictId !=null ? $scope.unitDistrictId : '',
					  				cityId 			: $scope.unitCityId!=undefined && $scope.unitCityId !=null ? $scope.unitCityId : '',
					  				areaId 			: $scope.unitAreaId!=undefined && $scope.unitAreaId !=null ? $scope.unitAreaId : '',
					  				postCode 		: '',
					  				patientId       : '',
					  				unitId			: $scope.unitId,
					  				organizationId  : $scope.organizationId,
					  				createdBy		: $scope.userId,
					  				updatedBy		: $scope.userId,
//					  				expiryDateDisabled 		: true,
					  				createdDate		: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					  				updatedDate		: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
					  			};
					  		
					  		$scope.kinDetailsList.push(angular.copy($scope.kinDetails));
					  	}
					  	$scope.initKinDetails();
					  	
					  	
					    $scope.addNewKinDetails = function(){
					    	$scope.kinDetails.patientId = $scope.appointmentMasterObj.patientId;
					    	$scope.validateNexK=false;
					    	angular.forEach($scope.kinDetailsList,function(value,index){
					    		if(value.prefixId == "" || value.kinName == "" 
					    			|| value.relationId == "" || value.mobileNo == "")
					    		{
					    			$scope.validateNexK=true;
					    			growl.error("Please enter details ",{
										title : ERROR_MSG
										});
					    			return false;
					    		}
					    	});
					    	
					    	if(!$scope.validateNexK){
					    		$scope.kinDetailsList.push(angular.copy($scope.kinDetails));
					    	}
					    	
					    };
					    $scope.removeKinDetails = function(indexToBeDeleted){	
							  $scope.kinDetailsList.splice(indexToBeDeleted, 1);
					    };
					    
						//Prefix list
						$scope.getPrefixList = function(){
							var URI = BASE_URL + ROOT_URL + GETACTIVEPREFIXLIST;
							BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(
									function(response) {
										$scope.prefixList = [];
										if (response.data.status == "success") {
											$scope.prefixList = response.data.listObject;
										}
									});
						}
						$scope.getPrefixList();
						
						//get Reason List
						$scope.getReasonList = function(){
							$scope.commonObj = {
									"unitId"				:$scope.unitId,
									"organizationId"		:$scope.organizationId,
									
							}
						var URI = BASE_URL + ROOT_URL
								+ GETACTIVEREASONLIST;

						BillingGenericService.serviceAction(METHOD_POST, URI,$scope.commonObj,NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												console.log("ReasonList",response);
												// $rootScope.startLoader();
												if (response.data.status == "success")
													$scope.reasonList = response.data.listObject;
												
											});
						}
						$scope.getReasonList();
						
						// Country List
						$scope.getCountryList = function(){
							var URI = BASE_URL + ROOT_URL + GETACTIVECOUNTRYLIST+$scope.organizationId;
							BillingGenericService.serviceAction("GET", URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$scope.countryList = [];
										if (response.data.status == "success") {
											$scope.countryList = response.data.listObject;
										}
									});
						}
						$scope.getCountryList();
						//get calling code by using country id start
						$scope.getCallingCode=function(data){
							var URI = BASE_URL + ROOT_URL
							+ GETSINGLECOUNTRY;
							GenericService.serviceAction("POST", URI, data).then(
									function(response) {
										if (response.data.status == "success" && response.data.listObject.length >0) {
											$scope.countryCallingCode = response.data.listObject[0].countryCallingCode;
											
											$scope.patientObj.countryCallingCode = response.data.listObject[0].countryCallingCode;
											$scope.patientObj.whatsappCallingCode = response.data.listObject[0].countryCallingCode;
											
											//if(type=="PA"){
												$scope.permanentPhoneCode = response.data.listObject[0].countryCallingCode;
											//}else{
												$scope.phoneCode = response.data.listObject[0].countryCallingCode;
											//}
										} else {
											//alert("Error!!");
										}
									});
						}
						
						$scope.getNationalityByContryId=function(data){
							data.organizationId = $scope.organizationId;
							var URI = BASE_URL + ROOT_URL
							+ "/adt/getNationalityByCountryId";
							GenericService.serviceAction("POST", URI, data).then(
									function(response) {
										if (response.data.status == "success" && response.data.listObject.length > 0) {
											console.log("nationality List ",response.data.listObject[0]);
											$scope.patientObj.nationalityId = response.data.listObject[0].nationalityId;

										} else {
//											alert("Error!!");
										}
									});
						}
						// for get state by country ID
						$scope.getStateByCountry = function(id,type) {
							$scope.startLoader();
							var data = {
								countryId : id
							};								
							
							var dataorg = {
									countryId : id,
									organizationId : $scope.organizationId
								};	
							
							$scope.getCallingCode(dataorg,type);
						
							$scope.getNationalityByContryId(data);
							var URI = BASE_URL + ROOT_URL
									+ GETSTATELISTBYCOUNTRYID;
							BillingGenericService
							.serviceAction(METHOD_POST, URI, data,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										//$scope.stateList = [];
										if (response.data.status == "success"){
											if(type == 'P')
												{
												$scope.stateList = [];
												$scope.stateList = response.data.listObject;
												$scope.patientObj.stateId="";
												$scope.patientObj.districtId="";
												$scope.patientObj.cityId="";
												$scope.patientObj.areaId="";
												$scope.patientObj.zipCode="";
												/*if($scope.sameAsLocal == 'Y'){
													$scope.patientObj.permanentCountryId = id;
												}*/
												}
											else if(type == 'PA')
												{
												$scope.permanentStateList = [];
												$scope.permanentStateList = response.data.listObject;
												
												$scope.patientObj.permanentStateId="";
												$scope.patientObj.permanentDistrictId="";
												$scope.patientObj.permanentCityId="";
												$scope.patientObj.permanentAreaId="";
												$scope.patientObj.permanentZipCode="";
												}
											else if(type == 'L')
												{
												$scope.stateList = [];
												$scope.stateList = response.data.listObject;
												
												}else if(type == 'LTOP')
												{
													$scope.permanentStateList = [];
													$scope.permanentStateList = response.data.listObject;
													
												}else
												{
												$scope.CompanyStateList = [];
												$scope.CompanyStateList = response.data.listObject;
												}
										}
										$scope.stopLoader();
									});

						}
						
						
						$scope.getDistrictByState = function(id,type) {
							$scope.startLoader();
							var data = {
								stateId : id
							};
							var URI = BASE_URL + ROOT_URL
									+ GETDISTRICTLISTBYSTATEID;
							BillingGenericService
							.serviceAction(METHOD_POST, URI, data,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										
										if (response.data.status == "success")
										{
											//$scope.districtList = response.data.listObject;
											if(type == 'P')
											{
											$scope.districtList = [];
											$scope.districtList = response.data.listObject;
											$scope.patientObj.districtId="";
											$scope.patientObj.cityId="";
											$scope.patientObj.areaId="";
											$scope.patientObj.zipCode="";
											}
											else if(type == 'PA')
											{
											$scope.permanentDistrictList = [];
											$scope.permanentDistrictList = response.data.listObject;
											
											$scope.patientObj.permanentDistrictId="";
											$scope.patientObj.permanentCityId="";
											$scope.patientObj.permanentAreaId="";
											$scope.patientObj.permanentZipCode="";
											}else if(type == 'L')
											{
												$scope.districtList = [];
												$scope.districtList = response.data.listObject;
												
											}
										else  if(type == 'LTOP')
										{
											$scope.permanentDistrictList = [];
											$scope.permanentDistrictList = response.data.listObject;
											
										}else
											{
											$scope.companyDistrictList = [];
											$scope.companyDistrictList = response.data.listObject;
											}
										}
										$scope.stopLoader();
									});
							
						}
						
						
						$scope.getCityByDistrict = function(id,type) {
							$scope.startLoader();
							var data = {
								districtId : id
							};
							var URI = BASE_URL + ROOT_URL
									+ GETCITYLISTBYDISTRICTID;
							BillingGenericService
							.serviceAction(METHOD_POST, URI, data,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										
										if (response.data.status == "success")
											//$scope.cityList = response.data.listObject;
										{
											//$scope.districtList = response.data.listObject;
											if(type == 'P')
											{
											 $scope.cityList = [];
											 $scope.cityList = response.data.listObject;
											 $scope.patientObj.cityId="";
											$scope.patientObj.areaId="";
											$scope.patientObj.zipCode="";
											}
											else if(type == 'PA')
											{
											$scope.permanentCityList = [];
											$scope.permanentCityList = response.data.listObject;
											
											$scope.patientObj.permanentCityId="";
											$scope.patientObj.permanentAreaId="";
											$scope.patientObj.permanentZipCode="";
											}else if(type == 'L')
											{
												 $scope.cityList = [];
												 $scope.cityList = response.data.listObject;
												
											}else if(type == 'LTOP')
											{
												$scope.permanentCityList = [];
												$scope.permanentCityList = response.data.listObject;
											}	
										else
											{
											$scope.CompanyCityList = [];
											$scope.CompanyCityList = response.data.listObject;
											}
										}
										$scope.stopLoader();
									});
						}
						$scope.getAreaByCity = function(id,type) {
							$scope.startLoader();
							var data = {
								cityId : id
							};
							var URI = BASE_URL + ROOT_URL
									+ GETAREALISTBYCITY;
							BillingGenericService
							.serviceAction(METHOD_POST, URI, data,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										
										if (response.data.status == "success")
											//$scope.areaList = response.data.listObject;
										{
											if(type == 'P')
											{
											$scope.areaList = [];
											 $scope.areaList = response.data.listObject;
											console.log($scope.areaList)
											$scope.patientObj.areaId="";
											$scope.patientObj.zipCode="";
											}
											else if(type == 'PA')
											{
											$scope.permanentAreaList = [];
											$scope.permanentAreaList = response.data.listObject;
											
											$scope.patientObj.permanentAreaId="";
											$scope.patientObj.permanentZipCode="";
											}else if(type == 'L')
											{
												$scope.areaList = [];
												 $scope.areaList = response.data.listObject;
												console.log($scope.areaList)
											}else if(type == 'LTOP')
											{
												$scope.permanentAreaList = [];
												$scope.permanentAreaList = response.data.listObject;
											}	
										else
											{
											$scope.CompanyAreaList = [];
											$scope.CompanyAreaList = response.data.listObject;
											}
										}
										$scope.stopLoader();
									});
						}
						
						//relation list
						$scope.getRelationshipList = function(){
							var URI = BASE_URL + ROOT_URL + GETACTIVERELATIONLIST;
							BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$scope.relationList = [];
										if (response.data.status == "success") {
											$scope.relationList = response.data.listObject;
										}
									});
						}
						$scope.getRelationshipList();
						
						if($scope.unitCountryId > 0)
						{
						
						  $scope.getStateByCountry($scope.unitCountryId,'L');
						  $scope.getDistrictByState($scope.unitStateId ,'L');
						  $scope.getCityByDistrict($scope.unitDistrictId,'L');
						  $scope.getAreaByCity($scope.unitCityId,'L');
						}
						
					    	$scope.kinsList=[];
					    	
					    	//kin details
					    	$scope.getKinDetailsByPatientId = function(){
					    		console.log("$scope.appointmentMasterObj.patientId",$scope.appointmentMasterObj);
					    		var URI = BASE_URL + ROOT_URL + KIN_DETAILS + PATIENT + SLASH + $scope.appointmentMasterObj.patientId  + UNIT + SLASH +  $scope.unitId  + ORG + SLASH + $scope.organizationId;
					    		BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					    			if (response.data.status == "success") {
					    				$scope.kinsList = response.data.listObject;
					    			}
					    		});
					    	}
					    	
					    	$scope.getKinDetailsById = function(kinDetailsId){
					    		$rootScope.startLoader();
					    		var URI = BASE_URL + ROOT_URL + KIN_DETAILS + SLASH + kinDetailsId;
					    		BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					    					if (response.data.status == "success") {
					    						$scope.kinDetailsList[0]=(angular.copy(response.data.object));
					    						if($scope.kinDetailsList[0].countryId != null){
					    							$scope.getStateByCountry($scope.kinDetailsList[0].countryId);
					    						}
					    						if($scope.kinDetailsList[0].stateId != null){
					    						$scope.getDistrictByState($scope.kinDetailsList[0].stateId);
					    						}
					    						if($scope.kinDetailsList[0].districtId != null){
					    						$scope.getCityByDistrict($scope.kinDetailsList[0].districtId);
					    						}
					    						if($scope.kinDetailsList[0].cityId != null){
					    						$scope.getAreaByCity($scope.kinDetailsList[0].cityId);
					    						}
					    						if($scope.kinDetailsList[0].identificationId !== undefined && $scope.kinDetailsList[0].identificationId !== ''){
					    							var index = 0;
					    							var isExpiryRequired = $.grep($scope.identificationList, function (doc) {
			                                            return doc.identificationId == $scope.kinDetailsList[0].identificationId;
			                                        })[0].isExpiryRequired;
					    							
			                                        console.log("isExpiryRequired",isExpiryRequired);
			                                        
			                                        if(isExpiryRequired=='Y'){
			                                            angular.forEach($scope.kinDetailsList,function(value,idx){
			                                                if(index == idx){
			                                                    value.expiryDateDisabled = false;
			                                                }
			                                            });
			                                            
			                                            var isoDateString = $scope.kinDetailsList[0].expiry;
			                                            
			                                            var originalDateString = isoDateString; 
			                                            var date = new Date(originalDateString);
			                                            $scope.myDate = date.toISOString().split('T')[0];
			                                            $scope.kinDetailsList[0].expiry = new Date($scope.myDate);
			                                            console.log("New Expiry : " + $scope.kinDetailsList[0].expiry);
			                                            
			                                        }else{
			                                            angular.forEach($scope.kinDetailsList,function(value,idx){
			                                                if(index == idx){
			                                                value.expiryDateDisabled = true;
			                                                }
			                                            });
			                                        }
					    						}
					    						console.log($scope.kinDetailsList);
					    					}
					    				});
					    	}
					    	
					    	
					    	$scope.updateKinDetailsStatus = function(kinDetailsId){
					    		$rootScope.startLoader();
					    		var URI = BASE_URL + ROOT_URL + KIN_DETAILS + SLASH + kinDetailsId + STATUS + SLASH + 'I';
					    		BillingGenericService.serviceAction(METHOD_PUT, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					    			if (response.data.status == "success") {
					    				$scope.getKinDetailsByPatientId();
					    			}
					    		});
					    	}
					    	
					    	$scope.setKinId = function(kinId){
					    		$scope.appointmentMasterObj.kinDetailsId = kinId;
					    	}
					    	
							$scope.openDatePickerExpiry1 = function() {
								$scope.datepickerExpiry1.opened = true;
							};

							$scope.datepickerExpiry1 = {
								opened : false
							};
							$scope
							
						$scope.openDatePickerExpiry = function(index) {
							// alert("in");
							// $scope.expiry.opened = true;
							for (var i = $scope.kinDetailsList.length - 1; i >= 0; i--) {
								if (i === index) {

									$scope.expiry.opened[i] = true;
								} else {
									$scope.expiry.opened[i] = false;
								}
							}
						};

						$scope.expiry = {
							opened : [ false ],

						};
						
						//checking expire datepicker is show or not
						$scope.disableEnableExpiry = function(id,index)
						{
//							 var splittedStringArray = isExpiryVal.split("_");
//							if(splittedStringArray[1] == 'Y')
//							{
//								$scope.kinDetailsList[idx].isExpiry='Y';
//							}
//							else
//								{
//								$scope.kinDetailsList[idx].isExpiry='N';
//								}

                             if(id!=undefined && id!=null && id!=""){
                                        var isExpiryRequired = $.grep($scope.identificationList, function (doc) {
                                            return doc.identificationId == id;
                                        })[0].isExpiryRequired;
                                        console.log("isExpiryRequired",isExpiryRequired);
                                        if(isExpiryRequired=='Y'){
                                            angular.forEach($scope.kinDetailsList,function(value,idx){
                                                if(index == idx){
                                                    value.expiryDateDisabled = false;
                                                }
                                            });
                                        }else{
                                            angular.forEach($scope.kinDetailsList,function(value,idx){
                                                if(index == idx){
                                                value.expiryDateDisabled = true;
                                                }
                                            });
                                        }
                             		}
						}
						
					    	
							$scope.getSubSpecialityBySpecialityId = function(id)
							{
								var data = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId
									};
									
									//Condition For All Modality Or By Modality Type ID
									if(id > 0)
										{
											data.specialityId = id;
											var URI = BASE_URL + ROOT_URL
											+ GETSUBSPECIALITYBYSPECIALITYID;

										}
									else
									{
										var URI = BASE_URL + ROOT_URL
										+ GETALLACTIVESUBSPECIALITYLIST;
									}
									
									BillingGenericService
											.serviceAction(METHOD_POST, URI, data,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														console.log(response);
														$scope.subSpecialityList = [];
														// $rootScope.startLoader();
														if (response.data.status == "success")
															$scope.subSpecialityList = response.data.listObject;
													});
								
							}
							
							//Function For Get Doctor List By Speciality
							$scope.getDoctorBySpeciality = function(id) {
								var data = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								$scope.appointmentSearchObject.doctorId = 0;
								//Condition For All Doctor Or By Speciality ID
								if(id > 0)
									{
										data.specialityId = id;
										var URI = BASE_URL + ROOT_URL
										+ GETDOCTORBYSPECIALITYID;

									}
								else
								{
									var URI = BASE_URL + ROOT_URL
									+ GETACTIVEDOCTORLIST;
								}
								
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
							
							//Function For Get Modality List By Modality Type Id
							$scope.getModalityListById = function(id) {
								var data = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								
								//Condition For All Modality Or By Modality Type ID
								if(id > 0)
									{
										data.modalityTypeId = id;
										var URI = BASE_URL + ROOT_URL
										+ GETMODALITYLISTBYMODALITYTYPEID;

									}
								else
								{
									var URI = BASE_URL + ROOT_URL
									+ GETACTIVEMODALITYLIST;
								}
								
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.modalityList = [];
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.modalityList = response.data.listObject;
												});
									}
							

							//Function For Cancel Appointment
							$scope.cancelAppointment = function() {
								
								
								if($scope.cancelledBy==undefined || $scope.cancelledBy==null || $scope.cancelledBy==""){
									 growl.warning("Please select cancelled by...!! ",{title : "Warning!"});
									 return false;
								}
								if($scope.cancelReasonId==undefined || $scope.cancelReasonId==null || $scope.cancelReasonId==""){
									 growl.warning("Please select reason...!! ",{title : "Warning!"});
									 return false;
								}
								console.log("$scope.selectAppointment",$scope.selectAppointment);
								//return false;
								$rootScope.startLoaderPopup();
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
								var data = {
									'slotId' : $scope.selectAppointment.slotId,
									'appointmentId' : $scope.selectAppointment.appointmentId,
									'cancelReasonId' : $scope.cancelReasonId,
									'cancelledBy' : $scope.cancelledBy,
									'updatedBy' : $scope.userId,
									'unitId' : $scope.unitId,
									'organizationId' : $scope.organizationId,
									'updatedDate' : $scope.currentDate,
									'firstName' : $scope.selectAppointment.firstName,
									'mobileNumber' :$scope.selectAppointment.mobileNumber,
									'doctorName' :$scope.selectAppointment.doctorName,
									'fromTimeString' : $scope.selectAppointment.fromTimeString,
									'appointmentDate2' : $scope.selectAppointment.appointmentDate,
									'encounterId':$scope.selectAppointment.encounterId,
									'patientId' : $scope.selectAppointment.patientId,
									'doctorId':$scope.selectAppointment.doctorId,
								};

								console.log(data);
								//	return false;
								var URI = BASE_URL + ROOT_URL
										+ CANCELSCHEDULEAPPOINTMENT;
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													//$rootScope.startLoader();
													if (response.data.status == "success") {
														$rootScope.stopLoaderPopup();
														 growl.success("Appointment Cancelled Successfully.",{title : "SUCCESS!"});
														 
														 angular.element('#appCancelModal').modal('hide');
														//$scope.init();
														
														 $scope.setFirstTabLevel($scope.firstTabLevel);
													}
													else
														{
														$rootScope.stopLoaderPopup();
														 growl.error("Error with cancel appointment..!!",{title : "ERROR!"});
														//angular.element('#appCancelModal').modal('hide');
														}

												});
							}
							
							//For Show Remark On Popup
							$scope.displayRemark = function(remark,reasonDesc) {
								$scope.appointmentRemark = remark;
								$scope.reasonDesc = reasonDesc;
							}
							
							
							//Search Patient
							$scope.searchPatient = function()
							{
								var ConDate = $('#datepickerSearch').val();
								if(ConDate != '')
								$scope.patientSearchObj.dob = $rootScope.getChangedFormatedDateShalse(ConDate);
								
									//return false;
										var URI = BASE_URL + ROOT_URL
												+ GETTEMPORYPATIENTFORPREREGISTRATION;

										BillingGenericService
												.serviceAction(METHOD_POST,
														URI, $scope.patientSearchObj,
														NOTIFICATION_MSG_STATUS_FALSE)
												.then(
														function(response) {
															$scope.patientList = [];
															if (response.data.status == "success")
																$scope.patientList = response.data.listObject;
														});
									
							}
							
							$scope.searchPatientToken = function()
							{
								var ConDate = $('#datepickerSearch').val();
								if(ConDate != '')
								$scope.patientSearchObj.dob = $rootScope.getChangedFormatedDateShalse(ConDate);
								
									//return false;
										var URI = BASE_URL + ROOT_URL
												+ GETTEMPORYPATIENTFORPREREGISTRATION;

										BillingGenericService
												.serviceAction(METHOD_POST,
														URI, $scope.patientSearchObj,
														NOTIFICATION_MSG_STATUS_FALSE)
												.then(
														function(response) {
															$scope.patientListToken = [];
															if (response.data.status == "success")
																$scope.patientListToken = response.data.listObject;
														});
									
							}
							/**Modality Report*/
							
							$scope.printModalityReport = function(info) {
								var data =  {
								        "appName": "HIS",
								        "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
								        "reportDesignFilePath": "getModalityAppointmentSlip.rpt",

								        "reportFormat": "PDF",
								        "exportFileName": "appointmentSleep",
								        "reportParams": [
								          {
										 "paramName": "appointment_id",
								         "paramType": "Integer",
								         "paramValue": info.appointmentId
								       }
								        ]
								};
								reportService.generate(REPORT_API_URI1,data);

							}
						
							/*****/
							//Function For Selected Patient assign to patient Details Object
							$scope.getPatientDetailsByIndex = function(index) {
								//console.log($rootScope.patientList[index]);
								//return false;
								$scope.appointmentSearchObject.patientId = ($scope.patientList[index].patientId);
								$scope.patientName =($scope.patientList[index].firstName+' '+$scope.patientList[index].middleName+' '+$scope.patientList[index].lastName);
								$scope.uhidNumber = ($scope.patientList[index].uhidNumber);
								$scope.mobileNumber =  ($scope.patientList[index].mobileNumber);

							}
							
							//Function For Selected Patient assign to patient Details Object
							$scope.getPatientDetailsByIndexToken = function(index) {
								
								if($scope.patientListToken[index].admissionId > 0){
									growl.warning("Patient is admitted.",{title : "Warning!"});
								     return false;
								}
								else{
								$scope.tokenPatientId = ($scope.patientListToken[index].patientId);
								$scope.uhidNumberToken = ($scope.patientListToken[index].uhidNumber);
								}

							}
							
							
							
							//Search Appointment List By Filters
							$scope.searchAppointment = function()
							{
								//alert();
								$scope.doctorSearchFlag = 'Y';
								$scope.patientSearchFlag = 'N';
								$scope.appointmentSearchObject.fromDate = moment($scope.datepickerFromDate).format('YYYY-MM-DD');
								$scope.appointmentSearchObject.toDate = moment($scope.datepickerToDate).format('YYYY-MM-DD');
								$scope.appointmentSearchObject.appointmentTypeId  =$scope.slotTypeId ;
								$scope.appointmentSearchObject.parentPackageId  = $scope.parentPackageId ;
								$scope.appointmentSearchObject.isAppointmentRequired = $scope.isAppointmentRequired;
								//$scope.appointmentSearchObject.appointmentStatusId = 0;
								
								console.log($scope.appointmentSearchObject);
								
								if($scope.datepickerToDate < $scope.datepickerFromDate)
									{
									     growl.warning("To date should be equal or greater than from date .",{title : "Warning!"});
									     return false;
									}else{
										$rootScope.startLoader();
										var URI = BASE_URL + ROOT_URL + SEARCHAPPOINTMENTSBYCRITERIA;
								        BillingGenericService
										.serviceAction(METHOD_POST, URI, $scope.appointmentSearchObject,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) 
												{
													console.log(response);
													$scope.appointmentList = [];
													// $rootScope.startLoader();
													if (response.data.status == "success")
														{
														$rootScope.stopLoader();
														$scope.appointmentList = response.data.listObject;
														 angular.forEach($scope.appointmentList,function(value,index) {
															 value.property = value.appointmentDate +" "+value.fromTimeString;
														 });
														  
													    $scope.allCount =response.data.listObject.length;
													    //$scope.pendingCount = response.data.object[0].pending;
													    //$scope.checkInCount = response.data.object[0].checkIn;
													    //$scope.rescheduledAndCancelledCount  = response.data.object[0].rescheduledAndCancelled;
													    
													    $scope.pendingCount = 0;
													    $scope.checkInCount = 0;
													    $scope.rescheduledAndCancelledCount  = 0;
													    $scope.completeCount  = 0;
													    $scope.checkOutCount  = 0;
													    $scope.admittedCount  = 0;
													    $scope.inprocessCount = 0;
													    angular.forEach($scope.appointmentList,function(value,index) {
																
													    	 switch (value.appointmentStatusId) {
													            case 1:
													            	 $scope.pendingCount +=1;
													                break;
													            case 2:
													            	$scope.rescheduledAndCancelledCount +=1;
													            	break;
													            case 3:
													            	$scope.rescheduledAndCancelledCount +=1;
													            	break;
													            case 5:
													            	$scope.checkInCount +=1;
													                break;
													            case 6:
													            	 $scope.completeCount +=1;
													            	 break;
													            case 7:
													            	 $scope.checkOutCount +=1;
													            	 break;
													            case 8:
													            	 $scope.inprocessCount +=1;
													            	 break;
													            default:

													        }
													    	 
													    	/* if(value.admissionId > 0)
												    		 {
												    		 	$scope.admittedCount +=1;
												    		 }*/
																
														});
													     $scope.allCount += $scope.admittedCount;
													        $(".all").addClass("active");
															$(".pending").removeClass("active");
															$(".inpro").removeClass("active");
															$(".checkId").removeClass("active");
															$(".comp").removeClass("active");
															$(".res").removeClass("active");
													   
													     
														}   
														else if (response.data.status == "error") 
														{
															$rootScope.stopLoader();
														}
													
												});
								        var URI = BASE_URL + ROOT_URL
										+ BLOCKEDSCHEDULEAPPOINTMENTLIST;
								GenericService
										.serviceAction("POST", URI, $scope.appointmentSearchObject)
										.then(
												function(response) {
													$scope.reScheduleObjList = [];

													if (response.data.status == "success") 
													{
														
														$scope.reScheduleObjList = response.data.listObject;
														console.log("resdlist",$scope.reScheduleObjList);
													}
													else if (response.data.status == "error") 
													{
														
													}
												});
								          
								
									}
								//return false;
								
						
							}
							
							
							//Just Test
							

							/* $interval(function() {
								 //alert('Interval');
								 var statusId = $scope.listStatusId;
								if($scope.doctorSearchFlag == 'N')
									{
										$scope.commonAppointmentListForInterval();
										$scope.commonAppointmentListByStatusIdForInterval(statusId);
									// alert('All Record');
									}
								 else
									 {
									// alert('Search Record');
									 }
					          }, 5000);*/
							
						//Just Test End 
							$scope.commonAppointmentList  = function()
							{
								var doctorModalityObj = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										appointmentTypeId : $scope.slotTypeId,
										appointmentStatusId : 0,
										appointmentDate:moment(new Date()).format('YYYY-MM-DD'),
										patientId:0,
										specialityId:0,
										doctorId:0,
										modalityId:0,
										visitTypeId:0,
										isAppointmentRequired :$scope.isAppointmentRequired
									};

									//For Get Appointment List
									var URI = BASE_URL + ROOT_URL
											+ GETAPPOINTMENTSCHEDULELIST;
									BillingGenericService
											.serviceAction(METHOD_POST, URI,
													doctorModalityObj,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														console.log(response);
														// $rootScope.startLoader();
														if (response.data.status == "success")
															$scope.commonAppointmentLists = response.data.listObject;
														
														    $scope.allCount =response.data.listObject.length;
														    //$scope.pendingCount = response.data.object[0].pending;
														    //$scope.checkInCount = response.data.object[0].checkIn;
														    //$scope.rescheduledAndCancelledCount  = response.data.object[0].rescheduledAndCancelled;
														    
														    $scope.pendingCount = 0;
														    $scope.checkInCount = 0;
														    $scope.rescheduledAndCancelledCount  = 0;
														    $scope.completeCount  = 0;
														    $scope.checkOutCount  = 0;
														    $scope.admittedCount = 0;
														    $scope.inprocessCount = 0;
														    
														    angular.forEach($scope.commonAppointmentLists,function(value,index) {
																	
														    	 switch (value.appointmentStatusId) {
														            case 1:
														            	 $scope.pendingCount +=1;
														                break;
														            case 2:
														            	$scope.rescheduledAndCancelledCount +=1;
														            	break;
														            case 3:
														            	$scope.rescheduledAndCancelledCount +=1;
														            	break;
														            case 5:
														            	$scope.checkInCount +=1;
														                break;
														            case 6:
														            	 $scope.completeCount +=1;
														            	 break;
														            case 7:
														            	 $scope.checkOutCount +=1;
														            	 break;
														            case 8:
														            	 $scope.inprocessCount +=1;
														            	 break;
														            default:

														        }
														    	 
														    	/* if(value.admissionId > 0)
													    		 {
													    		 	$scope.admittedCount +=1;
													    		 	
													    		 }*/
																	
															});
														    $scope.allCount += $scope.admittedCount;
													});
							}
							
							$scope.commonAppointmentListByStatusId  = function(id)
							{
								var doctorModalityObj = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										appointmentTypeId :$scope.slotTypeId,
										appointmentStatusId : id,
										appointmentDate:moment(new Date()).format('YYYY-MM-DD'),
										patientId:0,
										specialityId:0,
										doctorId:0,
										modalityId:0,
										visitTypeId:0,
										isAppointmentRequired :$scope.isAppointmentRequired
										
									};

									//For Get Appointment List
									var URI = BASE_URL + ROOT_URL
											+ GETAPPOINTMENTSCHEDULELIST;
									BillingGenericService
											.serviceAction(METHOD_POST, URI,
													doctorModalityObj,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														console.log(response);
														// $rootScope.startLoader();
														if (response.data.status == "success")
															$scope.appointmentList = response.data.listObject;
														
													});
							}
							
							$scope.actibeTabApp = function(statusId)
							{
								if(statusId == 0)
								{
									
									$(".all").addClass("active");
									$(".pending").removeClass("active");
									$(".inpro").removeClass("active");
									$(".checkId").removeClass("active");
									$(".comp").removeClass("active");
									$(".res").removeClass("active");
								}
								if(statusId == 1)
								{
									
									$(".all").removeClass("active");
									$(".pending").addClass("active");
									$(".inpro").removeClass("active");
									$(".checkId").removeClass("active");
									$(".comp").removeClass("active");
									$(".res").removeClass("active");
								}
								if(statusId == 8)
								{
									
									$(".all").removeClass("active");
									$(".pending").removeClass("active");
									$(".inpro").addClass("active");
									$(".checkId").removeClass("active");
									$(".comp").removeClass("active");
									$(".res").removeClass("active");
								}
								if(statusId == 5)
								{
									
									$(".all").removeClass("active");
									$(".pending").removeClass("active");
									$(".inpro").removeClass("active");
									$(".checkId").addClass("active");
									$(".comp").removeClass("active");
									$(".res").removeClass("active");
								}
								if(statusId == 6)
								{
									
									$(".all").removeClass("active");
									$(".pending").removeClass("active");
									$(".inpro").removeClass("active");
									$(".checkId").removeClass("active");
									$(".comp").addClass("active");
									$(".res").removeClass("active");
								}
								if(statusId == 3)
								{
									
									$(".all").removeClass("active");
									$(".pending").removeClass("active");
									$(".inpro").removeClass("active");
									$(".checkId").removeClass("active");
									$(".comp").removeClass("active");
									$(".res").addClass("active");
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
								 $scope.uhidNumber = '';
								 $scope.patientName = '';
								 $scope.mobileNumber = '';
								
							}
						
							$scope.getPendingAppointment = function(statusId)
							{
								
								$scope.actibeTabApp(statusId);
								$scope.listStatusId = statusId;
										$scope.appointmentSearchObject.fromDate = moment($scope.datepickerFromDate).format('YYYY-MM-DD');
										$scope.appointmentSearchObject.toDate = moment($scope.datepickerToDate).format('YYYY-MM-DD');
										$scope.appointmentSearchObject.appointmentTypeId  = $scope.slotTypeId;
										$scope.appointmentSearchObject.appointmentStatusId = statusId;
										$scope.appointmentSearchObject.isAppointmentRequired = $scope.isAppointmentRequired;
										
										console.log($scope.appointmentSearchObject);
										var URI = BASE_URL + ROOT_URL + SEARCHAPPOINTMENTSBYCRITERIA;
								        BillingGenericService
										.serviceAction(METHOD_POST, URI, $scope.appointmentSearchObject,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.appointmentList = [];
													if (response.data.status == "success")
													$scope.allCount = response.data.listObject.length;
													
													    $scope.pendingCount = 0;
													    $scope.checkInCount = 0;
													    $scope.rescheduledAndCancelledCount  = 0;
													    $scope.completeCount  = 0;
													    $scope.checkOutCount  = 0;
													    $scope.admittedCount =0;
													    $scope.inprocessCount = 0;
													    
													    angular.forEach(response.data.listObject,function(value,index) {
																
													    	 switch (value.appointmentStatusId) {
													    	 	case 0:
													    		 $scope.allCount +=1;
													    		 break;
													            case 1:
													            	 $scope.pendingCount +=1;
													                break;
													            case 2:
													            	$scope.rescheduledAndCancelledCount +=1;
													            	break;
													            case 3:
													            	$scope.rescheduledAndCancelledCount +=1;
													            	break;
													            case 5:
													            	$scope.checkInCount +=1;
													                break;
													            case 6:
													            	 $scope.completeCount +=1;
													            	 break;
													            case 7:
													            	 $scope.checkOutCount +=1;
													            	 break;
													            case 8:
													            	 $scope.inprocessCount +=1;
													            	 break;
													            default:

													        }
													    	 if(statusId == value.appointmentStatusId){
													    		 $scope.appointmentList.push(value);
													    	 }
													    	 if(statusId == 0){
													    		 $scope.appointmentList.push(value);
													    	 }
													    	 
													    });
												});
										
										
							}
							$scope.getPendingAppointmentList = function(statusId)
							{
								
								$scope.actibeTabApp(statusId);
								$scope.listStatusId = statusId;
								//alert($scope.doctorSearchFlag);
								//alert($scope.patientSearchFlag);
									if($scope.doctorSearchFlag == 'N' && $scope.patientSearchFlag =='N')
									{
										
										$scope.commonAppointmentList();
										$scope.commonAppointmentListByStatusId(statusId);
									}
									else if($scope.doctorSearchFlag == 'Y')
										{
										$scope.appointmentSearchObject.fromDate = moment($scope.datepickerFromDate).format('YYYY-MM-DD');
										$scope.appointmentSearchObject.toDate = moment($scope.datepickerToDate).format('YYYY-MM-DD');
										$scope.appointmentSearchObject.appointmentTypeId  = $scope.slotTypeId;
										$scope.appointmentSearchObject.appointmentStatusId = statusId;
										$scope.appointmentSearchObject.isAppointmentRequired = $scope.isAppointmentRequired;
										
										console.log($scope.appointmentSearchObject);
										//return false; 
										var URI = BASE_URL + ROOT_URL + GETAPPOINTMENTSCHEDULELIST;
								        BillingGenericService
										.serviceAction(METHOD_POST, URI, $scope.appointmentSearchObject,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.appointmentList = [];
													// $rootScope.startLoader();
													if (response.data.status == "success")
														$scope.appointmentList = response.data.listObject;
													 													   
													switch (statusId) {
													
													case 0:
														$scope.allCount = $scope.appointmentList.length;
														break;
										            case 1:
										            	 $scope.pendingCount = $scope.appointmentList.length;
										                break;
										            case 2:
										            	$scope.rescheduledAndCancelledCount = $scope.appointmentList.length;
										            	break;
										            case 3:
										            	$scope.rescheduledAndCancelledCount = $scope.appointmentList.length;
										            	break;
										            case 5:
										            	$scope.checkInCount = $scope.appointmentList.length;
										                break;
										            case 6:
										            	 $scope.completeCount = $scope.appointmentList.length;
										            	 break;
										            case 7:
										            	 $scope.checkOutCount  = $scope.appointmentList.length;
										            	 break;
										            case 8:
										            	 $scope.inprocessCount  = $scope.appointmentList.length;
										            	 break;
										            default:

										        }
													
												});
										
										}
							}
							
							$scope.clearSearchObject = function()
							{
								$scope.appointmentSearchObject = {
							    		 'doctorId':0,
							    		 'specialityId':0,
							    		 'organizationId':$scope.organizationId,
							    		 'unitId':$scope.unitId,
							    		 'patientId':0,
							    		 'specialityId':0,
							    		 'subSpecialityId':0,
							    		 'modalityId':0,
							    		 'visitTypeId':0,
							    		 'parentPackageId':''
							     }
								
								$scope.patientName ='';
								$scope.uhidNumber = '';
								$scope.mobileNumber = '';
								$scope.packgeService = '';
								$scope.parentPackageId = '';
								$scope.datepickerFromDate = new Date();
								$scope.datepickerToDate = new Date();
							}
							$scope.clearbtn=false;
							$scope.SearchObjectClear = function(){
							$scope.clearbtn=true;
							$scope.appointmentMasterObj.referralTypeId='';
							$scope.appointmentMasterObj.referralDesc='';
							$scope.appointmentMasterObj.referralRemark='';
							}
							
							$scope.searchGroupList = function(keyword) {

								var data = {
									patientName : keyword,
									unitId : $scope.unitId,
									// visitType :
									// $scope.selectedPageNameForAdmissionNote,
									organizationId : $scope.organizationId
								}
								console.log(data);

								var URI = BASE_URL + ROOT_URL
										+ PATIENTAUTOFILLSEARCH;
								
								return BillingGenericService
										.serviceAction(METHOD_POST, URI,
												data,
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
								//alert(info.patientId);
								
								$scope.patientName = info.patientName;
								$scope.uhidNumber = info.uhidNumber;
								$scope.mobileNumber = info.mobileNumber;
								
								$scope.appointmentSearchObject.patientId = info.patientId;
							}
							
							//Function For Reshedule Appointment
							$scope.rescheduleAppointment = function() 
							{
								
								if($scope.reschedulledBy != '' && $scope.reschedulledBy != undefined &&  $scope.rescheduleReasonId > 0  && $scope.datepickerReschedule != '' && $scope.datepickerReschedule != undefined)
								{
								//alert(moment($scope.datepickerReschedule).format('DD-MM-YYYY'));
								var cookieObjectReschedule = {
									'slotId' : $scope.selectAppointment.slotId,
									'appointmentId' : $scope.selectAppointment.appointmentId,
									'rescheduleReasonId' : $scope.rescheduleReasonId,
									'reschedulledBy' : $scope.reschedulledBy,
									'rescheduleAppointmentDate' : moment($scope.datepickerReschedule).format('DD/MM/YYYY'),
									'patientId' : $scope.selectAppointment.patientId,
									'prefixId' : $scope.selectAppointment.prefixId,
									'firstName' : $scope.selectAppointment.firstName,
									'middleName' : $scope.selectAppointment.middleName,
									'lastName' : $scope.selectAppointment.lastName,
									'age' : $scope.selectAppointment.age,
									'dob' : $scope.selectAppointment.dob,
									'genderId' : $scope.selectAppointment.genderId,
									'uhIdNumber' : $scope.selectAppointment.uhidNumber,
									'mobileNumber' : $scope.selectAppointment.mobileNumber,
									'email' : $scope.selectAppointment.email,
									'remark' : $scope.selectAppointment.remark,
									'bloodGroup' : $scope.selectAppointment.bloodGroup, 
									'allowedOverBookingSlots':$scope.selectAppointment.allowedOverBookingSlots,
									'isPaidAppointment':$scope.selectAppointment.isPaidAppointment !=null ? 'Y' : 'N',
									'patientCategoryId' : $scope.selectAppointment.patientCategoryId,
									'deptWiseAppointment' : $scope.selectAppointment.deptWiseAppointment,
									'bedId' : $scope.selectAppointment.bedId,
									'isTeleconsultationAppointment':$scope.selectAppointment.isTeleconsultationAppointment,
									'orderDetailsId':$scope.selectAppointment.orderDetailsId
								};
								
						

								if ($scope.slotTypeId == 1) {
									cookieObjectReschedule.specialityId = $scope.selectAppointment.specialityId;
									cookieObjectReschedule.modalitySubSpecialityId = $scope.selectAppointment.modalitySubSpecialityId;
									cookieObjectReschedule.doctorId = $scope.selectAppointment.doctorId;
									cookieObjectReschedule.specialityName = $scope.selectAppointment.specialityName;
									cookieObjectReschedule.doctorName = $scope.selectAppointment.doctorName;
									cookieObjectReschedule.selectType = "doctor";
									cookieObjectReschedule.selectedSlotId = 1;
								} else if ($scope.slotTypeId == 2){
									
									cookieObjectReschedule.modalityId = $scope.selectAppointment.modalityId;
									cookieObjectReschedule.specialityName = $scope.selectAppointment.specialityName;
									cookieObjectReschedule.doctorName = $scope.selectAppointment.modalityDesc;
									cookieObjectReschedule.modalitySubSpecialityId = $scope.selectAppointment.modalitySubSpecialityId;
									cookieObjectReschedule.specialityId = $scope.selectAppointment.modalitySpecialityId;
									cookieObjectReschedule.selectedSlotId = 2;
									cookieObjectReschedule.selectType = "modality";
									cookieObjectReschedule.modalityDesc = $scope.selectAppointment.modalityDesc;
									cookieObjectReschedule.subSpecialityName = $scope.selectAppointment.subSpecialityName;
									
								}
								else if($scope.slotTypeId == 3)
									{
									cookieObjectReschedule.specialityId = $scope.selectAppointment.specialityId;
									cookieObjectReschedule.modalitySubSpecialityId = $scope.selectAppointment.modalitySubSpecialityId;
									cookieObjectReschedule.modalityId = $scope.selectAppointment.modalityId;
									cookieObjectReschedule.specialityName = $scope.selectAppointment.specialityName;
									cookieObjectReschedule.doctorName = $scope.selectAppointment.modalityDesc;
									cookieObjectReschedule.selectedSlotId = 3;
									cookieObjectReschedule.selectType = "modality";
									cookieObjectReschedule.modalityDesc = $scope.selectAppointment.modalityDesc;
									cookieObjectReschedule.subSpecialityName = $scope.selectAppointment.subSpecialityName;
									cookieObjectReschedule.serviceId = $scope.selectAppointment.serviceId;
									
										//return false;
									}
								else if($scope.slotTypeId == 8){
									cookieObjectReschedule.specialityId = $scope.selectAppointment.specialityId;
									cookieObjectReschedule.modalitySubSpecialityId = $scope.selectAppointment.modalitySubSpecialityId;
									cookieObjectReschedule.modalityId = $scope.selectAppointment.modalityId;
									cookieObjectReschedule.specialityName = $scope.selectAppointment.specialityName;
									cookieObjectReschedule.doctorName = $scope.selectAppointment.modalityDesc;
									cookieObjectReschedule.selectedSlotId = 8;
									cookieObjectReschedule.selectType = "Hemodialysis";
									cookieObjectReschedule.modalityDesc = $scope.selectAppointment.modalityDesc;
									cookieObjectReschedule.subSpecialityName = $scope.selectAppointment.subSpecialityName;
									cookieObjectReschedule.serviceId = $scope.selectAppointment.serviceId;
								}
								//return false;
								//$cookies.putObject('cookieObjectReschedule',cookieObjectReschedule);
								
								//console.log(cookieObjectReschedule);
								angular.element('#appRescheduleModal').modal('hide');
								//$state.go('appointmentScheduling.day');
								//return false;
								$timeout(function() {
									if($scope.slotTypeId != 3)
										{
											//$state.go('appointmentScheduling.day');
										//$state.go('appointmentScheduling');
										$state.go("appointmentScheduling",{
											appointmentObjectReschedule :cookieObjectReschedule
										});
											
										}
									else
										{
											//$state.go('appointmentScheduling');
										$state.go("appointmentScheduling",{
											appointmentObjectReschedule :cookieObjectReschedule
										});
										}
									
								}, 500);
							}
								else
									{
										 growl.warning("All fields are required..!!",{title : "Warning!"});
										 return false;
									}
							}

							$scope.getPatientDetailsPopUp = function(item) {
								$scope.patientMobileNumber = item.mobileNumber;
								$scope.patientEmail = item.email;

							}
							
							/**Modality Report*/
							
							$scope.printHealthCheckupReport = function(info) {
								var data =  {
								        "appName": "HIS",
								        "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
								        "reportDesignFilePath": "getHealthCheckupAppointmentSlip.rpt",

								        "reportFormat": "PDF",
								        "exportFileName": "appointmentSleep",
								        "reportParams": [
								          {
										 "paramName": "appointment_id",
								         "paramType": "Integer",
								         "paramValue": info.appointmentId
								       }
								        ]
								};
								reportService.generate(REPORT_API_URI1,data);

							}
						
							/*****/
							/******* START TAB FOR ALL PAGES IN TREE LEVEL *******/
							/*First Level*/
							$scope.firstTabLevel = 1;

							$scope.setFirstTabLevel = function(newTab) {
								$scope.actibeTabApp(0);
								if (newTab == 1) {
									$("#case-sheet").css("background-color",
											"#5F9EA0");
									$("#complaints").css("background-color",
											"#5ab7c8");
									$scope.slotTypeId = 1;
									$scope.secondTabLevel = 1;
									var doctorModalityObj = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										appointmentTypeId : newTab,
										appointmentStatusId : 0,
										appointmentDate:moment(new Date()).format('YYYY-MM-DD'),
										patientId:0,
										specialityId:0,
										doctorId:0,
										specialityId:0,
										subSpecialityId:0,
										modalityId:0,
										visitTypeId:0,
										isAppointmentRequired : $scope.isAppointmentRequired
									};

									var URI = BASE_URL + ROOT_URL
											+ GETAPPOINTMENTSCHEDULELIST;

									BillingGenericService
											.serviceAction(METHOD_POST, URI,
													doctorModalityObj,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														console.log(response);
														// $rootScope.startLoader();
														if (response.data.status == "success")
															$scope.appointmentList = response.data.listObject;
														 angular.forEach($scope.appointmentList,function(value,index) {
															 value.property = value.appointmentDate +" "+value.fromTimeString;
														 });
														$scope.allCount = response.data.listObject.length;
														
														  $scope.pendingCount = 0;
														    $scope.checkInCount = 0;
														    $scope.rescheduledAndCancelledCount  = 0;
														    $scope.completeCount  = 0;
														    $scope.checkOutCount  = 0;
														    $scope.admittedCount  = 0;
														    $scope.inprocessCount = 0;
														    
														    angular.forEach($scope.appointmentList,function(value,index) {
																	
														    	 switch (value.appointmentStatusId) {
														            case 1:
														            	 $scope.pendingCount +=1;
														                break;
														            case 2:
														            	$scope.rescheduledAndCancelledCount +=1;
														            	break;
														            case 3:
														            	$scope.rescheduledAndCancelledCount +=1;
														            	break;
														            case 5:
														            	$scope.checkInCount +=1;
														                break;
														            case 6:
														            	 $scope.completeCount +=1;
														            	 break;
														            case 7:
														            	 $scope.checkOutCount +=1;
														            	 break;
														            case 8:
														            	 $scope.inprocessCount +=1;
														            	 break;
														            default:

														        }
														    	 
														    	 /*if(value.admissionId > 0)
													    		 {
													    		 	$scope.admittedCount +=1;
													    		 	$scope.allCount += $scope.admittedCount;
													    		 }*/
																	
															});
														  
															var URI = BASE_URL + ROOT_URL
															+ BLOCKEDSCHEDULEAPPOINTMENTLIST;
													GenericService
															.serviceAction("POST", URI, doctorModalityObj)
															.then(
																	function(response) {
																		$scope.reScheduleObjList = [];

																		if (response.data.status == "success") {
																			$scope.reScheduleObjList = response.data.listObject;
																			

																		}
																	});
														    
													});

								} else if (newTab == 2) {
									$("#case-sheet").css("background-color",
											"#5ab7c8");
									$("#complaints").css("background-color",
											"#5F9EA0");
									$scope.slotTypeId = 2;
									$scope.secondTabLevel = 2;
									var doctorModalityObj = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										appointmentTypeId : newTab,
										appointmentStatusId : 0,
										appointmentDate:moment(new Date()).format('YYYY-MM-DD'),
										patientId:0,
										specialityId:0,
										doctorId:0,
										specialityId:0,
										subSpecialityId:0,
										modalityId:0,
										visitTypeId:0
									};
									var URI = BASE_URL + ROOT_URL + SEARCHAPPOINTMENTSBYCRITERIA;
									/*var URI = BASE_URL + ROOT_URL
											+ GETAPPOINTMENTSCHEDULELIST;*/

									BillingGenericService
											.serviceAction(METHOD_POST, URI,
													doctorModalityObj,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														console.log(response);
														// $rootScope.startLoader();
														if (response.data.status == "success")
															$scope.appointmentList = response.data.listObject;
														 angular.forEach($scope.appointmentList,function(value,index) {
															 value.property = value.appointmentDate +" "+value.fromTimeString;
														 });
														$scope.allCount = response.data.listObject.length;
														
														  $scope.pendingCount = 0;
														    $scope.checkInCount = 0;
														    $scope.rescheduledAndCancelledCount  = 0;
														    $scope.completeCount  = 0;
														    $scope.checkOutCount  = 0;
														    $scope.admittedCount =0;
														    $scope.inprocessCount = 0;
														    
														    angular.forEach($scope.appointmentList,function(value,index) {
																	
														    	 switch (value.appointmentStatusId) {
														            case 1:
														            	 $scope.pendingCount +=1;
														                break;
														            case 2:
														            	$scope.rescheduledAndCancelledCount +=1;
														            	break;
														            case 3:
														            	$scope.rescheduledAndCancelledCount +=1;
														            	break;
														            case 5:
														            	$scope.checkInCount +=1;
														                break;
														            case 6:
														            	 $scope.completeCount +=1;
														            	 break;
														            case 7:
														            	 $scope.checkOutCount +=1;
														            	 break;
														            case 8:
														            	 $scope.inprocessCount +=1;
														            	 break;
														            default:

														        }
														    	 
														    	 /*if(value.admissionId > 0)
													    		 {
													    		 	$scope.admittedCount +=1;
													    		 	$scope.allCount += $scope.admittedCount;
													    		 }
																	*/
															});
													});
								}
								else if (newTab == 3) {
									$("#case-sheet").css("background-color",
											"#5ab7c8");
									$("#complaints").css("background-color",
											"#5F9EA0");
									$scope.slotTypeId = 3;
									$scope.secondTabLevel = 111;

									var ehcObject = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										appointmentTypeId : newTab,
										appointmentStatusId : 0,
										appointmentDate:moment(new Date()).format('YYYY-MM-DD'),
										patientId:0,
										specialityId:0,
										doctorId:0,
										specialityId:0,
										subSpecialityId:0,
										modalityId:0,
										visitTypeId:0,
										isAppointmentRequired : $scope.isAppointmentRequired
										//packageId:0,
									};
									
									var URI = BASE_URL + ROOT_URL
											+ GETAPPOINTMENTSCHEDULELIST;

									BillingGenericService
											.serviceAction(METHOD_POST, URI,
													ehcObject,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														console.log(response);
														// $rootScope.startLoader();
														if (response.data.status == "success")
															$scope.appointmentList = response.data.listObject;
														 angular.forEach($scope.appointmentList,function(value,index) {
															 value.property = value.appointmentDate +" "+value.fromTimeString;
														 });
														$scope.allCount = response.data.listObject.length;
														
														  $scope.pendingCount = 0;
														    $scope.checkInCount = 0;
														    $scope.rescheduledAndCancelledCount  = 0;
														    $scope.completeCount  = 0;
														    $scope.checkOutCount  = 0;
														    $scope.admittedCount =0;
														    $scope.inprocessCount = 0;
														    angular.forEach($scope.appointmentList,function(value,index) {
																	
														    	 switch (value.appointmentStatusId) {
														            case 1:
														            	 $scope.pendingCount +=1;
														                break;
														            case 2:
														            	$scope.rescheduledAndCancelledCount +=1;
														            	break;
														            case 3:
														            	$scope.rescheduledAndCancelledCount +=1;
														            	break;
														            case 5:
														            	$scope.checkInCount +=1;
														                break;
														            case 6:
														            	 $scope.completeCount +=1;
														            	 break;
														            case 7:
														            	 $scope.checkOutCount +=1;
														            	 break;
														            case 8:
														            	 $scope.inprocessCount +=1;
														            	 break;
														            default:

														        }
														    	 
														    	 /*if(value.admissionId > 0)
													    		 {
													    		 	$scope.admittedCount +=1;
													    		 	$scope.allCount += $scope.admittedCount;
													    		 }*/
																	
															});
														    
													});
								}
								else if (newTab == 5) {
									$("#case-sheet").css("background-color",
											"#5ab7c8");
									$("#complaints").css("background-color",
											"#5F9EA0");
									$scope.slotTypeId = 8;
									console.log("appointmentTypeId")
									$scope.secondTabLevel = 1;
									var doctorModalityObj = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										appointmentTypeId : 8,
										appointmentStatusId :0,
										appointmentDate:moment(new Date()).format('YYYY-MM-DD'),
										patientId:0,
										doctorId:0,
										specialityId:0,
										subSpecialityId:0,
										modalityId:0,
										visitTypeId:0,
										isAppointmentRequired : $scope.isAppointmentRequired
									};
									
									var URI = BASE_URL + ROOT_URL
											+ GETAPPOINTMENTSCHEDULELIST;

									BillingGenericService
											.serviceAction(METHOD_POST, URI,
													doctorModalityObj,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														console.log(response);
														// $rootScope.startLoader();
														if (response.data.status == "success")
															$scope.appointmentList = response.data.listObject;
														 angular.forEach($scope.appointmentList,function(value,index) {
															 value.property = value.appointmentDate +" "+value.fromTimeString;
														 });
														$scope.allCount = response.data.listObject.length;
														
														  $scope.pendingCount = 0;
														    $scope.checkInCount = 0;
														    $scope.rescheduledAndCancelledCount  = 0;
														    $scope.completeCount  = 0;
														    $scope.checkOutCount  = 0;
														    $scope.admittedCount =0;
														    $scope.inprocessCount = 0;
														    
														    angular.forEach($scope.appointmentList,function(value,index) {
																	
														    	 switch (value.appointmentStatusId) {
														            case 1:
														            	 $scope.pendingCount +=1;
														                break;
														            case 2:
														            	$scope.rescheduledAndCancelledCount +=1;
														            	break;
														            case 3:
														            	$scope.rescheduledAndCancelledCount +=1;
														            	break;
														            case 5:
														            	$scope.checkInCount +=1;
														                break;
														            case 6:
														            	 $scope.completeCount +=1;
														            	 break;
														            case 7:
														            	 $scope.checkOutCount +=1;
														            	 break;
														            case 8:
														            	 $scope.inprocessCount +=1;
														            	 break;
														            default:

														        }
														    	 
//														    	 if(value.admissionId > 0)
//													    		 {
//													    		 	$scope.admittedCount +=1;
//													    		 	$scope.allCount += $scope.admittedCount;
//													    		 }
																	
															});
														    
													});
								}
									if(newTab == 7){

								
									$scope.slotTypeId = 7;
									$scope.secondTabLevel = 1;
									var doctorModalityObj = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										appointmentTypeId : newTab,
										appointmentStatusId : 0,
										appointmentDate:moment(new Date()).format('YYYY-MM-DD'),
										patientId:0,
										specialityId:0,
										doctorId:0,
										specialityId:0,
										subSpecialityId:0,
										modalityId:0,
										visitTypeId:0,
										isAppointmentRequired : $scope.isAppointmentRequired
									};

									var URI = BASE_URL + ROOT_URL + GETAPPOINTMENTSCHEDULELIST;

									BillingGenericService
											.serviceAction(METHOD_POST, URI,
													doctorModalityObj,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														console.log(response);
														if (response.data.status == "success")
															$scope.appointmentList = response.data.listObject;
														 angular.forEach($scope.appointmentList,function(value,index) {
															 value.property = value.appointmentDate +" "+value.fromTimeString;
														 });
													});

								
								}
								$scope.firstTabLevel = newTab;
							};

							$scope.isSetFirstTabLevel = function(tabNum) {
								return $scope.firstTabLevel === tabNum;
							};

							//Second Level
							$scope.secondTabLevel = 1;
							

							$scope.setSecondTabLevel = function(newTab) {
								$scope.secondTabLevel = newTab;
							};

							$scope.isSetSecondTabLevel = function(tabNum) {
								return $scope.secondTabLevel === tabNum;
							};

							//Third Level
							$scope.thirdTabLevel = 1;

							$scope.setThirdTabLevel = function(newTab) {
								$scope.thirdTabLevel = newTab;
							};

							$scope.isSetThirdTabLevel = function(tabNum) {
								return $scope.thirdTabLevel === tabNum;
							};

							//FORTH Level
							$scope.forthTabLevel = 1;

							$scope.setForthTabLevel = function(newTab) {
								$scope.forthTabLevel = newTab;
							};

							$scope.isSetForthTabLevel = function(tabNum) {
								return $scope.forthTabLevel === tabNum;
							};
							
							
							//Start New FORM F
							//Fifth Level
							$scope.fifthTabLevel = 1;

							$scope.setFifthTabLevel = function(newTab) {
								$scope.fifthTabLevel = newTab;
							};

							$scope.isSetFifthTabLevel = function(tabNum) {
								return $scope.fifthTabLevel === tabNum;
							};
							
							$scope.redirectFunctionToForm = function(item,idx){
//								/$scope.redirectFormFObject
								$scope.selectedForm = item;
								item.patientId = $scope.selectedPatientId;
						        	
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
							
							
							
							//End New Form GF
							/******* END TAB FOR ALL PAGES IN TREE LEVEL *******/

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveAppointmentSchedule();
							}

							//--------- function for goto Encounter Page for encouter creation -----------//
							
							$scope.gotoEncounter=function(appointmentObj){
//								
								
								if(appointmentObj.appointmentTypeId==1 && $scope.isDocConsultReq =='N')
								{
									growl.warning("System is not configured for doctor consultation...!!",{title : "Warning!"});
									return false;
								}
								
								if(appointmentObj.appointmentTypeId==3 && $scope.isEHCReq =='N')
								{
									growl.warning("System is not configured for EHC consultation...!!",{title : "Warning!"});
									return false;
								}
								
								if(appointmentObj.appointmentTypeId==2 && $scope.isRadiologyReq =='N')
								{
									growl.warning("System is not configured for modality appointments...!!",{title : "Warning!"});
									return false;
								}
								
								if(appointmentObj.admissionId>0)
									{
										growl.warning("Patient is currently admitted...!!",{title : "Warning!"});
										return false;
									}
								
								console.log(appointmentObj);
								
								var todatDate = moment(new Date()).format('DD/MM/YYYY');
								
								if(appointmentObj.appointmentDate != null && appointmentObj.appointmentDate != todatDate )
									{
									 growl.warning("Appointment date should be today'date!!! ",{title : "Warning!"});
									 return false;
									}
								else if(appointmentObj.patientId == null)
									{

									 	//growl.error("Patient selection functionality is yet to develop....sorry for the inconvinince !! ",{title : "Warning!"});
									     angular.element('#patientDetailsForAppointments').modal('show');

									     $scope.appointmentObjToken = appointmentObj;
									     $scope.patientListToken = [];
									     $scope.patientSearchObj = {
													"uhidNumber" : "",
													"patientName" : "",
													"genderId" : "",
													"mobileNumber" : "",
													"dob" : "",
													"identificationNumber" : "",
													"identificationTypeId" : "",
													"unitId" : $scope.unitId,
													"organizationId" : $scope.organizationId,
													"registrationTypeIdList" :[1,2,3,4,5,6]

												}

									     return false;
									}
								else if(appointmentObj.registrationTypeId != '5')
									{
									//alert('sv');
									//return false;
								var URI = BASE_URL + ROOT_URL
								+ "/api/opd/updateAppointmentStatus";
								
								var statusChangeObj={
										appointmentId : appointmentObj.appointmentId,
										appointmentStatusId: 8,  // In process
										updatedBy : $scope.userId,
										updatedDate : moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
								}		
								console.log("statusChangeObj",statusChangeObj);
								//return false;		
						GenericService
								.serviceAction("POST", URI, statusChangeObj)
								.then(
										function(response) {
											$scope.reScheduleObjList = [];

											if (response.data.status == "success") {
												
												$state.go("encounter.encounterDetails", {
													appointmentObj: appointmentObj,
												});

											}
										});
							}
								else
									{
									
										
									
									var URI = BASE_URL + ROOT_URL
									+ "/api/opd/updateAppointmentStatus";
									
									var statusChangeObj={
											appointmentId : appointmentObj.appointmentId,
											appointmentStatusId: 8,  // In process
											updatedBy : $scope.userId,
											updatedDate : moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
									}
									
									
									GenericService
									.serviceAction("POST", URI, statusChangeObj)
									.then(
											function(response) {
												$scope.reScheduleObjList = [];

												if (response.data.status == "success") {
													
													 $state.go("patientRegistration", {
														 patientRegistrationObj: appointmentObj,
														});

												}
											});
									
									
									}
								
								
							}
							
							
							$scope.tokenGenForWithoutKiosk = function(appointmentObj){
								
								var marArrDate = moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
								
                                var todatDate = moment(new Date()).format('DD/MM/YYYY'); 
								
								if(appointmentObj.appointmentDate != null && appointmentObj.appointmentDate != todatDate )
									{
									 growl.warning("Appointment date should be today'date!!! ",{title : "Warning!"});
									 return false;
									}
								if(appointmentObj.encounterId == null || appointmentObj.encounterId == 0 ){
									
									var URI = BASE_URL + ROOT_URL	+ "/api/opd/markArrival/"+appointmentObj.appointmentId+"/"+marArrDate+"/"+$scope.unitId;
									BillingGenericService.serviceAction(METHOD_GET,URI, "",NOTIFICATION_MSG_STATUS_FALSE)
											.then(function(response){
														console.log("response",response);
														if (response.data.status == "success")
															{
															
															$scope.searchAppointment($scope.appointmentSearchObject);
															
																  $timeout( function(){
																	  $scope.gotoEncounterWithoutKiosk(appointmentObj);
																  },2000);
															}
														else if(response.data.code == "409")
														{
															
														}
													});
									
								}else{
									
									URI = BASE_URL + ROOT_URL	+ "/api/opd/markArrival/paidAppointment";
									var data = {
												appointmentId:appointmentObj.appointmentId,
												currentDate : marArrDate,
										        unitId : $scope.unitId,
												organizationId : $scope.organizationId,
												encounterId : appointmentObj.encounterId,
												deptWiseAppointment : appointmentObj.deptWiseAppointment,
												doctorId : appointmentObj.doctorId,
												specialityId : appointmentObj.specialityId,
												isPaidAppointment : 'Y'
											
							 				}
									BillingGenericService.serviceAction(METHOD_POST,URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(
											function(response)
											{
												if (response.data.status == "success")
													{
													$scope.searchAppointment($scope.appointmentSearchObject);
													
													  $timeout( function(){
														  $scope.gotoEncounterWithoutKiosk(appointmentObj);
													  },2000);
													}
												else if(response.data.code == "409")
												{
													
												}
											});
								
									
								}
								
							
								
							}
							
							
							$scope.gotoEncounterWithoutKiosk=function(appointmentObj){
//								
								console.log(appointmentObj);
								
								
								if(appointmentObj.appointmentTypeId==1 && $scope.isDocConsultReq =='N')
								{
									growl.warning("System is not configured for doctor consultation...!!",{title : "Warning!"});
									return false;
								}
								
								if(appointmentObj.appointmentTypeId==3 && $scope.isEHCReq =='N')
								{
									growl.warning("System is not configured for EHC consultation...!!",{title : "Warning!"});
									return false;
								}
								
								if(appointmentObj.appointmentTypeId==2 && $scope.isRadiologyReq =='N')
								{
									growl.warning("System is not configured for modality appointments...!!",{title : "Warning!"});
									return false;
								}
								
								
								if(appointmentObj.admissionId>0)
								{
									growl.warning("Patient is currently admitted...!!",{title : "Warning!"});
									return false;
								}
								
								if($scope.isCounterApplicable=='Y' && angular.isUndefined($rootScope.counterId))
								{
									angular.element('#counterDetailsModal').modal('show');
									growl.warning("Please assign counter to the user ....!!",{title : "Warning!"});
									return false;
								}else{
									
									if(appointmentObj.isPaidAppointment=='Y' && appointmentObj.registrationTypeId != '5')
									{
										var URI = BASE_URL + ROOT_URL
										+ "/api/opd/updateAppointmentStatus";
										
										var statusChangeObj={
												appointmentId : appointmentObj.appointmentId,
												appointmentStatusId: 5,  // Checked In
												updatedBy : $scope.userId,
												updatedDate : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
												encounterId : appointmentObj.encounterId
										}

										GenericService
										.serviceAction("POST", URI, statusChangeObj)
										.then(
												function(response) {
													$scope.reScheduleObjList = [];

													if (response.data.status == "success") {
														
														angular.forEach($scope.appointmentList,function(value,index){
															
															if(value.appointmentId == appointmentObj.appointmentId)
																{
																	value.appointmentStatusId=5;
																	value.appointmentStatusDesc='Checked In';
																}
																
														});
														return false;
													}
												});
									}else if(appointmentObj.isPaidAppointment=='Y' && appointmentObj.registrationTypeId == '5')
									{
										
										var URI = BASE_URL + ROOT_URL
										+ "/api/opd/updateAppointmentStatus";
										
										var statusChangeObj={
												appointmentId : appointmentObj.appointmentId,
												appointmentStatusId: 8,  // In process
												updatedBy : $scope.userId,
												updatedDate : moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
										}

										GenericService
										.serviceAction("POST", URI, statusChangeObj)
										.then(
												function(response) {
													$scope.reScheduleObjList = [];

													if (response.data.status == "success") {
														
														 $state.go("patientRegistration", {
															 patientRegistrationObj: appointmentObj,
															});

													}
												});
										
									}else{
										var URI = BASE_URL + ROOT_URL
										+ "/api/opd/assignCounterId";
										
										var statusChangeObj={
												appointmentId : appointmentObj.appointmentId,
												counterId: $rootScope.counterId, 
												frontDeskArrival:'Y'
										}
										
										GenericService.serviceAction("POST",URI,statusChangeObj)
										.then(
												function(response) {
													if (response.data.status == "success") 
													{
														var todatDate = moment(new Date()).format('DD/MM/YYYY');
														
														if(appointmentObj.appointmentDate != null && appointmentObj.appointmentDate != todatDate )
															{
															 growl.error("Appointment date should be today'date!!! ",{title : "Warning!"});
															 return false;
															}
														else if(appointmentObj.patientId == null)
															{

															 	//growl.error("Patient selection functionality is yet to develop....sorry for the inconvinince !! ",{title : "Warning!"});
															     angular.element('#patientDetailsForAppointments').modal('show');

															     $scope.appointmentObjToken = appointmentObj;
															     $scope.patientListToken = [];
															     $scope.patientSearchObj = {
																			"uhidNumber" : "",
																			"patientName" : "",
																			"genderId" : "",
																			"mobileNumber" : "",
																			"dob" : "",
																			"identificationNumber" : "",
																			"identificationTypeId" : "",
																			"unitId" : $scope.unitId,
																			"organizationId" : $scope.organizationId,
																			"registrationTypeIdList" :[1,2,3,4,5,6]

																		}

															     return false;
															}
														else if(appointmentObj.registrationTypeId != '5')
															{
															//alert('sv');
															//return false;
														var URI = BASE_URL + ROOT_URL
														+ "/api/opd/updateAppointmentStatus";
														
														var statusChangeObj={
																appointmentId : appointmentObj.appointmentId,
																appointmentStatusId: 8,  // In process
																updatedBy : $scope.userId,
																updatedDate : moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
														}		
														console.log("statusChangeObj",statusChangeObj);
														//return false;		
												GenericService
														.serviceAction("POST", URI, statusChangeObj)
														.then(
																function(response) {
																	$scope.reScheduleObjList = [];

																	if (response.data.status == "success") {
																		
																		$state.go("encounter.encounterDetails", {
																			appointmentObj: appointmentObj,
																		});

																	}
																});
													}
														else
															{
															
															var URI = BASE_URL + ROOT_URL
															+ "/api/opd/updateAppointmentStatus";
															
															var statusChangeObj={
																	appointmentId : appointmentObj.appointmentId,
																	appointmentStatusId: 8,  // In process
																	updatedBy : $scope.userId,
																	updatedDate : moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
															}

															GenericService
															.serviceAction("POST", URI, statusChangeObj)
															.then(
																	function(response) {
																		$scope.reScheduleObjList = [];

																		if (response.data.status == "success") {
																			
																			 $state.go("patientRegistration", {
																				 patientRegistrationObj: appointmentObj,
																				});

																		}
														});
															
													}
												}
											});
									}
									
									}
							}
							
							//Check before Sequence
							  var isSequenceExists;
								function checkSequenceExistsOrNot(arr) {
									return arr
											.some(function(arr) {
												if(arr.counterId == null)
													{
														return true;
													}
												else
													{
														return false;
													}
											});
								}

								
							$scope.callPatient = function(info,idx)
							{
								
								if(info.admissionId>0)
								{
									growl.warning("Patient is currently admitted...!!",{title : "Warning!"});
									return false;
								}
								
								var calIndex = info.tokenNumber + 1;
								
								
								$scope.sequenceAppointmentObj = info;
								if(angular.isUndefined($rootScope.counterId))
								{
									growl.warning("Please assign a counter to the user..!!",{title : "Warning!"});
									angular.element('#counterDetailsModal').modal('show');
									return false;
								}
								else{
									
									angular.forEach($scope.appointmentList,function(value,index){
										if(info.tokenNumber  == 1 && info.doctorId == value.doctorId){
											if(value.tokenNumber <= calIndex){
												var datamessage = {
														triggerId : COUNTER_CHECK_IN,
														patientId : value.patientId,
														unitId : $scope.unitId,
														organizationId : $scope.organizationId
													}
												
													console.log(datamessage);

													var URI = BASE_URL + ROOT_URL
															+ "/getSmsUtilityByTriggerIdForInPatientCheckInForm";
													
													return BillingGenericService
															.serviceAction(METHOD_POST, URI,
																	datamessage,
																	NOTIFICATION_MSG_STATUS_FALSE)
															.then(
																	function(response) {
																		console.log(response);
																		// $rootScope.startLoader();
																		if (response.data.status == "success")
																			return response.data.listObject;
																	});
											}
											
										}
										else if  (calIndex == value.tokenNumber){
											var datamessage = {
													triggerId : COUNTER_CHECK_IN,
													patientId : value.patientId,
													unitId : $scope.unitId,
													organizationId : $scope.organizationId
												}
											
												console.log(datamessage);

												var URI = BASE_URL + ROOT_URL
														+ "/getSmsUtilityByTriggerIdForInPatientCheckInForm";
												
												return BillingGenericService
														.serviceAction(METHOD_POST, URI,
																datamessage,
																NOTIFICATION_MSG_STATUS_FALSE)
														.then(
																function(response) {
																	console.log(response);
																	// $rootScope.startLoader();
																	if (response.data.status == "success")
																		return response.data.listObject;
																});
										}
									})
									var newAppointmentListForSequence = [];
									var i = 0;
									var loopFlag = true;
									angular.forEach($scope.appointmentList,function(value,index){
										if(idx > i){
										newAppointmentListForSequence.push(value);
										i++;
										}
										if(loopFlag){
										if(i == idx){
											loopFlag = false;
											//isSequenceExists=checkSequenceExistsOrNot(newAppointmentListForSequence)
											//if (!isSequenceExists) {
												if(info.count < 3)
												{	
													if(angular.isUndefined($rootScope.counterId))
													{
														angular.element('#counterDetailsModal').modal('show');
														return false;
													}
													
													if(info.count < 2){
														
														$scope.spcMsg = ' Token number, '+info.tokenNumber+', please visit '+$rootScope.counterDesc+' , thank you .';
														if($scope.isKioskAnnouncementRequired!=null && $scope.isKioskAnnouncementRequired!=undefined &&
															$scope.isKioskAnnouncementRequired == 'Y'){
															$scope.speech(' Token number, '+info.tokenNumber+', please visit '+$rootScope.counterDesc+' , thank you .');
														}
														
														//$scope.$emit('customEvent', $scope.speechMsg);
													}
													if(info.count == 2)
														{
														$scope.spcMsg = ' Token number, '+info.tokenNumber+', please visit '+$rootScope.counterDesc+' , This is Your Last call, thank you .';
														if($scope.isKioskAnnouncementRequired!=null && $scope.isKioskAnnouncementRequired!=undefined &&
																$scope.isKioskAnnouncementRequired == 'Y'){
																$scope.speech(' Token number, '+info.tokenNumber+', please visit '+$rootScope.counterDesc+' , This is Your Last call, thank you .');
														}
																//$scope.$emit('sendTokenScreenEvent', $scope.speechMsg);
														}
													info.count = info.count + 1;
												
												
												var URI = BASE_URL + ROOT_URL + "/api/opd/updateCallCount/"+info.appointmentId+"/"+info.count;
												
												GenericService
												.serviceAction("GET",URI,'')
												.then(function(response) {
															console.log("reee",response);
															if (response.data.status == "success") {
																
																//New
																/*if(item.counterId == null){*/
																var URI = BASE_URL + ROOT_URL
																+ "/api/opd/assignCounterId";
																
																var statusChangeObj={
																		appointmentId : info.appointmentId,
																		counterId: $rootScope.counterId,
																		frontDeskArrival : "N",
																		remark : $scope.spcMsg,
																		organizationId:$scope.organizationId,
																		unitId:$scope.unitId
																}
																
																GenericService
																.serviceAction("POST",URI,statusChangeObj)
																.then(function(response) {
																			console.log("reee",response);
																			if (response.data.status == "success") {
																				//$scope.appointmentList[idx].counterId= $rootScope.counterId;
																				angular.forEach($scope.appointmentList, function(value, key) {
																		              
																		              if(value.appointmentId == info.appointmentId)
																		               {
																		                  value.counterId= $rootScope.counterId;
																		                  value.frontDeskArrival = "N"
																		               }
																		              });
																			}
																		});
																//New
																/*}*/
																
															}
														});
												}
											/*}else{
												angular.element('#beforeSequencePopup').modal('show');
												return;
											}*/
										}
									}
									});
									
								
								
								}
								
							}
							
							$scope.beforeSequnce = function()
							{
								var info = $scope.sequenceAppointmentObj;
								if(info.count < 3)
								{	
									if(angular.isUndefined($rootScope.counterId))
									{
										angular.element('#counterDetailsModal').modal('show');
										return false;
									}
									
									if(info.count < 2){
										$scope.spcMsg = ' Token number, '+info.tokenNumber+', please visit '+$rootScope.counterDesc+' , thank you .';
										if($scope.isKioskAnnouncementRequired!=null && $scope.isKioskAnnouncementRequired!=undefined &&
												$scope.isKioskAnnouncementRequired == 'Y'){
											$scope.speech(' Token number, '+info.tokenNumber+', please visit '+$rootScope.counterDesc+' , thank you .');
										}
									}
									if(info.count == 2){
										$scope.spcMsg = ' Token number, '+info.tokenNumber+', please visit '+$rootScope.counterDesc+' , This is Your Last call, thank you .';
										if($scope.isKioskAnnouncementRequired!=null && $scope.isKioskAnnouncementRequired!=undefined &&
												$scope.isKioskAnnouncementRequired == 'Y'){
											$scope.speech(' Token number, '+info.tokenNumber+', please visit '+$rootScope.counterDesc+' , This is Your Last call, thank you .');
										}
									}
									info.count = info.count + 1;
								
								
								var URI = BASE_URL + ROOT_URL + "/api/opd/updateCallCount/"+info.appointmentId+"/"+info.count;
								
								GenericService
								.serviceAction("GET",URI,'')
								.then(function(response) {
											console.log("reee",response);
											if (response.data.status == "success") {
												//New
											
												var URI = BASE_URL + ROOT_URL
												+ "/api/opd/assignCounterId";
												
												var statusChangeObj={
														appointmentId : info.appointmentId,
														counterId: $rootScope.counterId,
														frontDeskArrival : "N",
														remark : $scope.spcMsg,
														organizationId:$scope.organizationId,
														unitId:$scope.unitId
												}
												
												GenericService
												.serviceAction("POST",URI,statusChangeObj)
												.then(function(response) {
															console.log("reee",response);
															if (response.data.status == "success") {
																//$scope.appointmentList[idx].counterId= $rootScope.counterId;
																angular.forEach($scope.appointmentList, function(value, key) {
														              
														              if(value.appointmentId == info.appointmentId)
														               {
														                  value.counterId= $rootScope.counterId;
														               }
														              });
															}
														});
												//New
												
											}
										});
								}
								
							}
							$scope.redirectFormFObject = {};
							//For Test Templ
							$scope.getConsentTemp = function(item){
								$scope.redirectFormFObject = item;
								$scope.selectedPatientId = item.patientId;
								$scope.selectedAppointmentId = item.appointmentId;
							 var data={
									 appointmentId : item.appointmentId,
				    				  consentTypeId : 1000
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
										"appointmentId" : item.appointmentId
									}
							// $scope.selectedTemplates = [];
							 
							 $("#checkBx").prop("checked", false);
							 
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
				        	    
							/*$scope.getTempleteValue = function(template,idx){
								$scope.templateValue = '<center><b>Form F</b></center><div style="float: right;margin-top:-15px;">&nbsp;Sr. No.<table cellspacing="0" cellpadding="0" class="t1" style="border-collapse: collapse;"> <tbody> <tr> <td valign="top" class="td1" style="width: 9px; height: 10px; border-style: solid; border-width: 1px; border-color: rgb(0, 0, 0); padding: 4px;"><p class="p1" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; line-height: normal; font-family: Helvetica; min-height: 14px;">&nbsp;&nbsp;</p></td> <td valign="top" class="td2" style="width: 14px; height: 10px; border-style: solid; border-width: 1px; border-color: rgb(0, 0, 0); padding: 4px;"><p class="p1" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; line-height: normal; font-family: Helvetica; min-height: 14px;">&nbsp;&nbsp;</p></td> <td valign="top" class="td1" style="width: 9px; height: 10px; border-style: solid; border-width: 1px; border-color: rgb(0, 0, 0); padding: 4px;"><p class="p1" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; line-height: normal; font-family: Helvetica; min-height: 14px;">&nbsp;&nbsp;</p></td> <td valign="top" class="td1" style="width: 9px; height: 10px; border-style: solid; border-width: 1px; border-color: rgb(0, 0, 0); padding: 4px;"><p class="p1" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; line-height: normal; font-family: Helvetica; min-height: 14px;">&nbsp;&nbsp;</p></td> <td valign="top" class="td1" style="width: 9px; height: 10px; border-style: solid; border-width: 1px; border-color: rgb(0, 0, 0); padding: 4px;"><p class="p1" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; line-height: normal; font-family: Helvetica; min-height: 14px;"><br></p></td> </tr> </tbody> </table></div><br><p class="p3" style="margin: 0px; text-align: center; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);="" min-height:="" 11px;"=""><span class="s1" style="font-kerning: none;"></span><br><span class="s1" style="font-kerning: none;"><b>[See proviso to section 4(3), rule 9 (4) and rule (10(IA)]</b></span></p><p class="p5" style="margin: 0px 0px 0.6px; text-align: center; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><b>FORM FOR MAINTENANCE OF RECORD IN CASE OF PRENATAL DIAGNOSTIC TEST/PROCEDURE BY GENETIC CLINIC/ULTRASOUND CLINIC/IMAGING CENTRE</b></span></p><p class="p6" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);="" min-height:="" 11px;"=""><span class="s1" style="font-kerning: none;"></span><br></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><b>Section A:</b><span class="Apple-converted-space"><b>&nbsp;</b> </span>To be filed in fo all diagnostic Procedures/Test</span></p><p class="p6" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);="" min-height:="" 11px;"=""><span class="s1" style="font-kerning: none;"></span><br></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">1.<span class="Apple-tab-span" style="white-space: pre;"></span><b>RBKILKARI HEALTHCARE PVT. LTD., BHAGAT SINGH CHOWK, JHAJJAR</b></span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">2.<span class="Apple-tab-span" style="white-space: pre;"></span>Registration No.<span class="Apple-converted-space">&nbsp; </span>(Under PC &amp; PNDT Act, 1994):- <b>PNDT/JJR/64/25-11-2019 to 24-11-2024</b></span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">3.<span class="Apple-tab-span" style="white-space: pre;"></span>Patient;s Name:-&nbsp;<b><span data-id="pname" style="font-size:15px;color:blue;">ABC W/O XYZ</span></b>.&nbsp;</span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">&nbsp; &nbsp; &nbsp; &nbsp; Age:-&nbsp;<b data-id="page" style="font-size:15px;color:blue;">26 years, 4 months, and 17 days </b></span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">4.<span class="Apple-tab-span" style="white-space: pre;"></span>Total Number of living children:-&nbsp;<b data-id="tnc" style="font-size:15px;color:blue;">…………………………………………………………………………………………</b></span></p><p class="p8" style="margin: 0px 0px 0px 72px; text-indent: -36px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">(a)<span class="Apple-tab-span" style="white-space: pre;"></span>Number of living sons with age of each living son (in years and/or Months):-&nbsp;<b data-id="tnc1" style="font-size:15px;color:blue;">…………………………………………………………………………………………</b></span></p><p class="p8" style="margin: 0px 0px 0px 72px; text-indent: -36px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">(b).<span class="Apple-tab-span" style="white-space: pre;"></span>Number of living Daughter with age of each living daughter (in years and/or months):-&nbsp;<b data-id="tnc2" style="font-size:15px;color:blue;">…………………………………………………………………………………………</b><span class="Apple-converted-space">&nbsp;</span></span></p><p class="p8" style="margin: 0px 0px 0px 72px; text-indent: -36px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><span class="Apple-tab-span" style="white-space: pre;"></span>…………………………………………………………………………………………………………………..</span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">5.<span class="Apple-tab-span" style="white-space: pre;"></span>Husbands wifes/fathers/Mothers Name:- <b data-id="pcareof" style="font-size:15px;color:blue;">W/O XYZ</b>.</span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">6.<span class="Apple-tab-span" style="white-space: pre;"></span>Full postal address of the patient with contact No., if any:-&nbsp;<b data-id="padd" style="font-size:15px;color:blue;">RANIYA CALONY , CHAWNI, PIN-124103, Jhajjar, Haryana/हरियाणा, Mob. 123456789</b></span></p><p class="p9" style="margin: 0px 0px 0px 36px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">…………………………………………………………………………………………………………………………… ……………………………………………………………………………………………………………………………</span></p><p class="p10" style="margin: 0px 0px 0px 36px; text-indent: -36px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">7.<span class="Apple-tab-span" style="white-space: pre;"></span>(a)<span class="Apple-tab-span" style="white-space: pre;"></span>Referred by (full name and address of doctor (s)/Genetic Counseling Centre:- <b data-id="prefby" style="font-size:15px;color:blue;">Direct</b> <span class="Apple-converted-space">&nbsp;</span></span></p><p class="p11" style="margin: 0px 0px 0px 36px; text-indent: 36px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">………………………………………………………………………………………………………………….</span></p><p class="p11" style="margin: 0px 0px 0px 36px; text-indent: 36px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">(Referral slips to be preserved carefully with form F) …………………………………………………………</span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><span class="Apple-tab-span" style="white-space: pre;"></span>(b)<span class="Apple-tab-span" style="white-space: pre;"></span>Self-Referral by Gynecologist/Radiologist/Registered Medical Practitioner conducting the diagnostic:- <b data-id="prefby2" style="font-size:15px;color:blue;"></b></span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span>Procedures ……………………………………………………………………………………………………..</span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span>(Referral note with indications and case papers of the patients to be preserved with form F).</span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">8.<span class="Apple-tab-span" style="white-space: pre;"></span>Last menstrual period/week of pregnancy <b data-id="lmpdate" style="font-size:15px;color:blue;">……………………………………………………………………………….</b></span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><br></span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><br></span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><input type="checkbox" data-id="checkme" data-name="sectionb" checked="true"> <b>Section B: </b>to be filled in for performing non-invasive diagnostic procedures/Tests only.</span></p><div id="sectionb"><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><br></span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; line-height: normal;" times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-size: 13px; font-kerning: none;">9.<span class="Apple-tab-span" style="white-space: pre;"></span>Name of the doctor performing the procedure/s:-&nbsp;</span><span style="font-size: 13px;"><b style="color:blue;">Dr Arpan Gupta, MBBS,DGO,(Gynae &amp; Obs) HMC NO -15475</b></span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">10.<span class="Apple-tab-span" style="white-space: pre;"></span>Indication/s for diagnosis procedure <b style="color:blue;">USG FOR FWB</b>.</span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><span class="Apple-tab-span" style="white-space: pre;"></span>(Specify with reference to the request made in the referral slip or a self referral note)</span></p><p class="p9" style="margin: 0px 0px 0px 36px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">(Ultrasonograpy Prenatal diagnosis during pregnancy should only be performed when indicated. The following is the representative list of Indications for ultrasound during Pregnancy).<span class="Apple-converted-space">&nbsp;</span></span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><b>(Put a "Tick" against the appropriate Indication/s for ultrasound)</b></span></p><ol class="ol1" style="list-style-type: upper-roman; color: rgb(0, 0, 0); font-size: medium;"> <li class="li12" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";"=""> <input type="checkbox" data-id="checkme" data-name="c1"> <span class="s2" style="font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 8px; line-height: normal;"></span><span class="s1" style="font-kerning: none;">To diagnose intra-uterine and/or ectopic Pregnancy and confirm viability.</span></li> <li class="li12" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";"=""> <input type="checkbox" data-id="checkme" data-name="c2"> <span class="s2" style="font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 8px; line-height: normal;"></span><span class="s1" style="font-kerning: none;">Estimation of gestational age (dating).</span></li> <li class="li12" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";"=""> <input type="checkbox" data-id="checkme" data-name="c3"> <span class="s2" style="font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 8px; line-height: normal;"></span><span class="s1" style="font-kerning: none;">Detection of number of fetuses and their chorionicity.</span></li> <li class="li12" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";"=""> <input type="checkbox" data-id="checkme" data-name="c4"> <span class="s2" style="font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 8px; line-height: normal;"></span><span class="s1" style="font-kerning: none;">Suspected pregnancy with IUCD in situ or suspected pregnancy following contraceptive failure/MTP failure.</span></li> <li class="li12" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";"=""> <input type="checkbox" data-id="checkme" data-name="c5"> <span class="s2" style="font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 8px; line-height: normal;"></span><span class="s1" style="font-kerning: none;">Vaginal bleeding/leaking.</span></li> <li class="li12" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";"=""> <input type="checkbox" data-id="checkme" data-name="c6"> <span class="s2" style="font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 8px; line-height: normal;"></span><span class="s1" style="font-kerning: none;">Follow-Up of cases of abortion.</span></li> <li class="li12" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";"=""> <input type="checkbox" data-id="checkme" data-name="c7"> <span class="s2" style="font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 8px; line-height: normal;"></span><span class="s1" style="font-kerning: none;">Assessment of cervical canal and diameter of internal os.</span></li> <li class="li12" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";"=""> <input type="checkbox" data-id="checkme" data-name="c8"> <span class="s2" style="font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 8px; line-height: normal;"></span><span class="s1" style="font-kerning: none;">Discrepancy between uterine size and period of amenorrhea.<span class="Apple-converted-space">&nbsp;</span></span></li> <li class="li12" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";"=""> <input type="checkbox" data-id="checkme" data-name="c9"> <span class="s2" style="font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 8px; line-height: normal;"></span><span class="s1" style="font-kerning: none;">Any suspected adenxal or uterine pathology/abnormality.<span class="Apple-converted-space">&nbsp;</span></span></li> <li class="li12" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";"=""> <input type="checkbox" data-id="checkme" data-name="c10"> <span class="s2" style="font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 8px; line-height: normal;"></span><span class="s1" style="font-kerning: none;">Detection of chromosomal abnormalities, fetal structural defects and other abnormalities and their follow-up.</span></li> <li class="li12" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";"=""> <input type="checkbox" data-id="checkme" data-name="c11"> <span class="s2" style="font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 8px; line-height: normal;"></span><span class="s1" style="font-kerning: none;">To evaluate fetal presentation and position.</span></li> <li class="li12" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";"=""> <input type="checkbox" data-id="checkme" data-name="c12"> <span class="s2" style="font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 8px; line-height: normal;"></span><span class="s1" style="font-kerning: none;">Assessment of liquor ammii.<span class="Apple-converted-space">&nbsp;</span></span></li> <li class="li12" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";"=""> <input type="checkbox" data-id="checkme" data-name="c13"> <span class="s2" style="font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 8px; line-height: normal;"></span><span class="s1" style="font-kerning: none;">Preterm labour/preterm premature rupture of membranes.<span class="Apple-converted-space">&nbsp;</span></span></li> <li class="li12" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";"=""> <input type="checkbox" data-id="checkme" data-name="c14"> <span class="s2" style="font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 8px; line-height: normal;"></span><span class="s1" style="font-kerning: none;">Evaluation of placental position, thickness, grading and abnormalities (Placenta praevia, retro, placental hemorrhage, abnormal adherence etc.).<span class="Apple-converted-space">&nbsp;</span></span></li> <li class="li12" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";"=""> <input type="checkbox" data-id="checkme" data-name="c15"> <span class="s2" style="font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 8px; line-height: normal;"></span><span class="s1" style="font-kerning: none;">Evaluation of umbilical cord –presentation, insertion, nuchal encirclement, number of vessels and presence of true knot. <span class="Apple-converted-space">&nbsp;</span></span></li> <li class="li12" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";"=""> <input type="checkbox" data-id="checkme" data-name="c16"> <span class="s2" style="font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 8px; line-height: normal;"></span><span class="s1" style="font-kerning: none;">Evaluation of previous caesarean section scars.</span></li> <li class="li12" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";"=""> <input type="checkbox" data-id="checkme" data-name="c17" checked="true"> <span class="s2" style="font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 8px; line-height: normal;"></span><span class="s1" style="font-kerning: none;color:blue">Evaluation foetal growth parameters, foetal weight and foetal well being.<span class="Apple-converted-space">&nbsp;</span></span></li> <li class="li12" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";"=""> <input type="checkbox" data-id="checkme" data-name="c18"> <span class="s2" style="font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 8px; line-height: normal;"></span><span class="s1" style="font-kerning: none;">Colour flow mapping and duplex Doppler studies.<span class="Apple-converted-space">&nbsp;</span></span></li> <li class="li12" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";"=""> <input type="checkbox" data-id="checkme" data-name="c19"> <span class="s2" style="font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 8px; line-height: normal;"></span><span class="s1" style="font-kerning: none;">Ultrasound guided procedures such as medical termination of pregnancy, external cephalic version etc. and their follow-up.</span></li> <li class="li12" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";"=""> <input type="checkbox" data-id="checkme" data-name="c20"> <span class="s2" style="font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 8px; line-height: normal;"></span><span class="s1" style="font-kerning: none;">Adjunct to diagnostic and therapeutic invasive interventions such as chorionic villus sampling (CVS), amniocenteses, foetal blood sampling, foetal skin biopsy, amnioinfusion, intrauterine infusion, placement of shunts etc.<span class="Apple-converted-space">&nbsp;</span></span></li> <li class="li12" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";"=""> <input type="checkbox" data-id="checkme" data-name="c21"> <span class="s2" style="font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 8px; line-height: normal;"></span><span class="s1" style="font-kerning: none;">Observation of Intra –Partum events.</span></li> <li class="li12" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";"=""> <input type="checkbox" data-id="checkme" data-name="c22"> <span class="s2" style="font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 8px; line-height: normal;"></span><span class="s1" style="font-kerning: none;">Medical/Surgical conditions complicating pregnancy.</span></li> <li class="li12" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";"=""> <input type="checkbox" data-id="checkme" data-name="c23"> <span class="s2" style="font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 8px; line-height: normal;"></span><span class="s1" style="font-kerning: none;">Research/scientific studies in recognized institutions. <span class="Apple-converted-space">&nbsp;</span></span></li></ol><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">11.<span class="Apple-tab-span" style="white-space: pre;"></span>Procedures carried out (Non-invasive) (Put a "tick" on the appropriate procedure)</span></p><p class="p13" style="margin: 0px 0px 0px 18px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""> <span class="s1" style="font-kerning: none;"><span class="Apple-tab-span" style="white-space: pre;"></span>i). <input type="checkbox" data-id="checkme" data-name="c24" checked="true"><span class="Apple-tab-span" style="white-space: pre;"></span><b style="color:blue;">Ultrasound</b></span></p><p class="p14" style="margin: 0px 0px 0px 72px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">(Important Note: Ultrasound is not indicated/advised/performed to determine the sex of fetus except for diagnosis of sex-linked disease such as Duchene Muscular Dystrophy, Haemophilia A &amp; B etc.).<span class="Apple-converted-space">&nbsp;</span></span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><span class="Apple-tab-span" style="white-space: pre;"></span>&nbsp; &nbsp; &nbsp; ii). <input type="checkbox" data-id="checkme" data-name="c25"> <span class="Apple-tab-span" style="white-space: pre;"></span>Any other (Specify) ………………………………………………..</span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">12.<span class="Apple-tab-span" style="white-space: pre;"></span>Date on which declaration of pregnant women/person was obtained <b data-id="tdate" style="font-size:15px;color:blue;">25-08-2020</b> </span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">13.<span class="Apple-tab-span" style="white-space: pre;"></span>Date on which procedure carried out <b data-id="tdate" style="font-size:15px;color:blue;">25-08-2020</b></span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">14.<span class="Apple-tab-span" style="white-space: pre;"></span>Result of the non-invasive procedure carried out (report in brief of the test including ultrasound carried<span class="Apple-converted-space">&nbsp; </span>out).<span class="Apple-converted-space">&nbsp;</span></span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><span class="Apple-converted-space"><br></span></span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><span class="Apple-converted-space"><br></span></span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><span class="Apple-converted-space"><br></span></span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">15.<span class="Apple-tab-span" style="white-space: pre;"></span>The result of pre-natal diagnostic procedure was conveyed to <b data-id="pname" style="color:blue;">ABC W/O XYZ</b> on:- <b data-id="tdate" style="font-size:15px;color:blue;">25-08-2020</b></span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">16.<span class="Apple-tab-span" style="white-space: pre;"></span>Any indicative for MTP as per the abnormality I detected in the diagnostic procedure/ tests ……………………………</span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><span class="Apple-tab-span" style="white-space: pre;"></span>……………………………………………………………………………………………………………………………..</span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><br></span></p></div><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><input type="checkbox" data-id="checkme" data-name="sectionc" checked="true"> <b>Section C: </b>To be filled for performing invasive procedures/Test only.</span></p><div id="sectionc"><p class="p6" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);="" min-height:="" 11px;"=""><span class="s1" style="font-kerning: none;"></span><br></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">17.<span class="Apple-tab-span" style="white-space: pre;"></span>Name of the doctor/s performing the procedure/s ...............</span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">18.<span class="Apple-tab-span" style="white-space: pre;"></span>History of genetic/medical disease in the family (specify)………………………………………………………basis of<span class="Apple-converted-space">&nbsp;</span></span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><span class="Apple-tab-span" style="white-space: pre;"></span>Diagnosis ("Tick" on appropriate basis of diagnosis):</span></p><ol class="ol2" style="list-style-type: lower-alpha; color: rgb(0, 0, 0); font-size: medium;"> <li class="li12" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";"=""><span class="s1" style="font-kerning: none;"> <input type="checkbox" data-id="checkme" data-name="c30"> Clinical <span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span>(b). <input type="checkbox" data-id="checkme" data-name="c31"><span class="Apple-tab-span" style="white-space: pre;"></span>Metabolic disorders<span class="Apple-converted-space">&nbsp; &nbsp;</span></span>(c) <span class="Apple-converted-space" style="color: rgb(85, 85, 85);">&nbsp; </span><span style="color: rgb(85, 85, 85);"> </span><input type="checkbox" data-id="checkme" data-name="c32"><span style="color: rgb(85, 85, 85);"> Cytogenetic </span><span class="Apple-tab-span" style="color: rgb(85, 85, 85); white-space: pre;"></span><span class="Apple-tab-span" style="color: rgb(85, 85, 85); white-space: pre;"></span><span class="Apple-tab-span" style="color: rgb(85, 85, 85); white-space: pre;"></span><span class="Apple-tab-span" style="color: rgb(85, 85, 85); white-space: pre;"></span><span style="color: rgb(85, 85, 85);">(d)</span><span class="Apple-tab-span" style="color: rgb(85, 85, 85); white-space: pre;"></span><input type="checkbox" data-id="checkme" data-name="c33"><span style="color: rgb(85, 85, 85);"> other (e.g. radiological, ultrasonograpy etc. specify).</span><span class="Apple-converted-space" style="color: rgb(85, 85, 85);">&nbsp;</span></li></ol><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">19.<span class="Apple-tab-span" style="white-space: pre;"></span>Indication/s<span class="Apple-converted-space">&nbsp; </span>for the diagnosis procedure ("Tick" on appropriate indication/s):</span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><span class="Apple-tab-span" style="white-space: pre;"></span>A.<span class="Apple-tab-span" style="white-space: pre;"></span>Previous child/children with:-</span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span>(i)<span class="Apple-tab-span" style="white-space: pre;"></span><input type="checkbox" data-id="checkme" data-name="c34"> Chromosomal disorders<span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span>(ii)<span class="Apple-tab-span" style="white-space: pre;"></span><input type="checkbox" data-id="checkme" data-name="c35"> Metabolic disorders<span class="Apple-converted-space">&nbsp;</span></span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span>(iii)<span class="Apple-tab-span" style="white-space: pre;"></span> <input type="checkbox" data-id="checkme" data-name="c36"> Congenital anomaly <span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span>(iv)<span class="Apple-tab-span" style="white-space: pre;"></span><input type="checkbox" data-id="checkme" data-name="c37"> Mental disability<span class="Apple-converted-space">&nbsp;</span></span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span>(v)<span class="Apple-tab-span" style="white-space: pre;"></span><input type="checkbox" data-id="checkme" data-name="c38"> Haemoglobinopathy <span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span>(vi)<span class="Apple-tab-span" style="white-space: pre;"></span><input type="checkbox" data-id="checkme" data-name="c39"> Sex linked disorders</span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span>(vii)<span class="Apple-tab-span" style="white-space: pre;"></span><input type="checkbox" data-id="checkme" data-name="c40"> Single gene disorder <span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span>(viii)<span class="Apple-tab-span" style="white-space: pre;"></span><input type="checkbox" data-id="checkme" data-name="c41"> Any other (Specify)<span class="Apple-converted-space">&nbsp;</span></span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><span class="Apple-tab-span" style="white-space: pre;"></span>B.<span class="Apple-tab-span" style="white-space: pre;"></span><input type="checkbox" data-id="checkme" data-name="c42"> Advanced maternal age (35 Years)</span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><span class="Apple-tab-span" style="white-space: pre;"></span>C.<span class="Apple-tab-span" style="white-space: pre;"></span><input type="checkbox" data-id="checkme" data-name="c43"> Mother/father/sibling hag genetic disease (specify).</span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><span class="Apple-tab-span" style="white-space: pre;"></span>D.<span class="Apple-tab-span" style="white-space: pre;"></span><input type="checkbox" data-id="checkme" data-name="c44"> Other (Specify) …………………………………………………………………………………………………</span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">20.<span class="Apple-tab-span" style="white-space: pre;"></span>Date on which consent pregnant woman/person was obtained in Form G prescribed PC&amp;PNDT Act, 1994:</span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">21.<span class="Apple-tab-span" style="white-space: pre;"></span>Invasive procedures carried out ("Tick" on appropriate indication/s)</span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><span class="Apple-tab-span" style="white-space: pre;"></span>i.<span class="Apple-tab-span" style="white-space: pre;"></span><input type="checkbox" data-id="checkme" data-name="c45"> Amniocentesis <span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span>ii. <input type="checkbox" data-id="checkme" data-name="c46"> Chorionic Villi aspiration<span class="Apple-converted-space">&nbsp;</span></span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><span class="Apple-tab-span" style="white-space: pre;"></span>iii.<span class="Apple-tab-span" style="white-space: pre;"></span><input type="checkbox" data-id="checkme" data-name="c47"> Fetal biopsy <span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span>&nbsp; &nbsp; &nbsp; &nbsp; iv. <input type="checkbox" data-id="checkme" data-name="c48"> Cordocentesis <span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span>v. Any other (Specify).</span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">22.<span class="Apple-tab-span" style="white-space: pre;"></span> Any complication/s of invasive procedure (Specify) …………………………………………………………………..</span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">23.<span class="Apple-tab-span" style="white-space: pre;"></span>Additional tests recommended (please mention if applicable):-</span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><span class="Apple-tab-span" style="white-space: pre;"></span>i.<span class="Apple-tab-span" style="white-space: pre;"></span><input type="checkbox" data-id="checkme" data-name="c50"> Chromosomal studies <span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span>ii. <input type="checkbox" data-id="checkme" data-name="c51"> Biochemical studies<span class="Apple-converted-space">&nbsp;</span></span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><span class="Apple-tab-span" style="white-space: pre;"></span>iii.<span class="Apple-tab-span" style="white-space: pre;"></span><input type="checkbox" data-id="checkme" data-name="c52"> Molecular studies <span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span>&nbsp; &nbsp; &nbsp; &nbsp;iv. <input type="checkbox" data-id="checkme" data-name="c53"> Pre-implantation gender diagnosis <span class="Apple-converted-space">&nbsp; </span>v. Any other (Specify).</span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">24.<span class="Apple-tab-span" style="white-space: pre;"></span>Result of the procedures/Tests carried out (Report in brief of the invasive test/procedures carried out) …………………</span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><span class="Apple-tab-span" style="white-space: pre;"></span>……………………………………………………………………………………………………………………………...</span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">25.<span class="Apple-tab-span" style="white-space: pre;"></span>Date on which procedures carried out <span data-id="tdates">……………………………………………………………………………….</span></span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">26.<span class="Apple-tab-span" style="white-space: pre;"></span>The result of pre-natal diagnostic procedures was conveyed to ………………………… on …………………………….</span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">27.<span class="Apple-tab-span" style="white-space: pre;"></span>Any indication for MTP a as per the abnormality detected in the diagnostic procedures/tests……………………………</span></p><p class="p6" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);="" min-height:="" 11px;"=""><span class="s1" style="font-kerning: none;"></span><br></p> <p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""> <span class="s1" style="font-kerning: none;">Date: <span data-id="tdate" style="font-size:15px;color:blue;">25-08-2020</span></span></p> <p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">Place: Jhajjar</span></p> <p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; float: right;" times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""> Name, signature and Registration number with seal of the<br> Gynecologist/Radiologist/Registered Medical<br> Practitioner, performing Diagnostic procedure/s </p> <p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><br></span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><br></span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><br></span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><br></span></p></div><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><b>Section D: </b>Declaration<br></p><p class="p6" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);="" min-height:="" 11px;"=""><span class="s1" style="font-kerning: none;"></span><br></p><p class="p4" style="margin: 0px; text-align: center; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><b>DECLARATION OF THE PERSON UNDERGOING PRENATAL DIAGNOSTIC TEST/PROCEDURE</b></span></p><p class="p6" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);="" min-height:="" 11px;"=""><span class="s1" style="font-kerning: none;"></span><br></p><p class="p15" style="margin: 0px; text-align: justify; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">I, Mrs./Mr. <b data-id="pname" style="font-size:15px;color:blue;">ABC W/O XYZ</b> declare that by undergoing …………………………………………………… prenatal diagnostic test/procedure. I do not want to know the sex of my faetus.<span class="Apple-converted-space">&nbsp;</span></span></p><p class="p16" style="margin: 0px; text-align: justify; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);="" min-height:="" 11px;"=""><span class="s1" style="font-kerning: none;"></span><br></p><p class="p15" style="margin: 0px; text-align: justify; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">Date: <span data-id="tdate" style="font-size:15px;color:blue;">25-08-2020</span><span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span> </span></p><p class="p15" style="margin: 0px; text-align: justify; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; float: right; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""> Signature/thumb impression of person <br> Undergoing prenatal diagnostic test/procedure</p><br><br><p class="p16" style="margin: 0px; text-align: justify; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);="" min-height:="" 11px;"=""><span class="s1" style="font-kerning: none;"></span><br></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="Apple-converted-space" style="text-align: justify;"><br></span></p><p class="p7" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><b style="text-align: justify;">In case of Thumb Impression:<span class="Apple-converted-space">&nbsp;</span></b><br></p><p class="p15" style="margin: 0px; text-align: justify; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">Identified by (Name) …………………………………. Age …………………………. Sex ………………………….. Ration (if any) ……………………………………………………. Address and contact No. …………………………………………&nbsp;</span></p><p class="p15" style="margin: 0px; text-align: justify; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">Signature of a person attesting thumb impression ……………………………… date ………………………</span></p><p class="p16" style="margin: 0px; text-align: justify; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);="" min-height:="" 11px;"=""><span class="s1" style="font-kerning: none;"></span><br></p><p class="p16" style="margin: 0px; text-align: justify; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);="" min-height:="" 11px;"=""><span class="s1" style="font-kerning: none;"></span><br></p><p class="p16" style="margin: 0px; text-align: justify; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);="" min-height:="" 11px;"=""><span class="s1" style="font-kerning: none;"></span><br></p><p class="p4" style="margin: 0px; text-align: center; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;"><b>DECLARATION OF DOCTOR/PERSON CONDUCTING PRENATAL DIAGNOSTIC PROCEDURE/TEST</b></span></p><p class="p3" style="margin: 0px; text-align: center; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);="" min-height:="" 11px;"=""><span class="s1" style="font-kerning: none;"><b></b></span><br></p><p class="p17" style="margin: 0px; text-align: justify; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: medium; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);="" min-height:="" 1px;"=""><span class="s1" style="font-kerning: none;"></span><br></p><p class="p15" style="margin: 0px; text-align: justify; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">I, <b style="color:blue;">Dr. Arpan Gupta</b> (Name of the person conducting ultrasonography/image scanning), declare that while conduction<span class="Apple-converted-space">&nbsp; </span>ultrasonography/image scanning of Mrs./Mr <b data-id="pname" style="font-size:15px;color:blue;">ABC W/O XYZ</b></span></p><p class="p18" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">(Name of the pregnant women or the person undergoing prenatal diagnostic procedure/Test), I have neither detected nor disclosed the sex of her foetus to anybody in any manner.</span></p><p class="p19" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);="" min-height:="" 11px;"=""><span class="s1" style="font-kerning: none;"></span><br></p><p class="p18" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">Date: <span data-id="tdate" style="font-size:15px;color:blue;">25-08-2020</span> <span class="Apple-tab-span" style="white-space: pre;"></span> <span class="Apple-tab-span" style="white-space: pre;"></span><span class="Apple-tab-span" style="white-space: pre;"></span> <span class="Apple-tab-span" style="white-space: pre;"></span> <span class="Apple-tab-span" style="white-space: pre;"></span></span></p><p class="p19" style="margin: 0px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);="" min-height:="" 11px;"=""><span class="s1" style="font-kerning: none;"></span><br></p><span style="float: right;"><p class="p20" style="margin: 0px 0px 0px 252px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""> Signature ….......................................................... <br><br> <span class="s1" style="font-kerning: none;">Name of the Capitals, Registration Number with seal of the<span class="Apple-converted-space">&nbsp;</span></span></p><p class="p20" style="margin: 0px 0px 0px 252px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">Gynecologist/Radiologist/Registered Medical Practitioner<span class="Apple-converted-space">&nbsp;</span></span></p><p class="p20" style="margin: 0px 0px 0px 252px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);"=""><span class="s1" style="font-kerning: none;">Conduction Diagnostic Procedure).</span></p><p class="p21" style="margin: 0px 0px 0px 252px; font-variant-numeric: normal; font-variant-east-asian: normal; font-stretch: normal; font-size: 13px; line-height: normal; font-family: " times="" new="" roman";="" color:="" rgb(0,="" 0,="" 0);="" min-height:="" 11px;"=""><span class="s1" style="font-kerning: none;"></span><br></p> </span>';
							}*/
							
							$scope.consentView=function(obj,index){

								obj.patientId = $scope.selectedPatientId;
								obj.appointmentId = $scope.selectedAppointmentId;
						        	
						        	$rootScope.startLoader();
					        	    var URI = BASE_URL + ROOT_URL + "/api/opd/appendCensentData";
					        	    GenericService.serviceAction(METHOD_POST,URI,obj).then(function(response) {
						    			if (response.data.status == "success")
						    			{
						    				/*$scope.templateList = [];
						    			 	$scope.templateList = response.data.listObject;
						    			 	$('#concentTemplate').modal('show');
											$rootScope.stopLoader();*/
						    				//$('#consentEditPopup').modal('show');
						    				$rootScope.stopLoader();
						    				obj.templateDesc = response.data.object.templateDesc;
						    				$scope.viewTemplate(obj,index)
										}
						    			else if(response.data.status == "error")
					    				{
						    				$rootScope.stopLoader();
					    				}
										
						    		});
					        	
							}
							
							
							$scope.saveTemplate = function()
							{
								
								
								 $scope.selectedTemplates[$scope.templateIndex].templateDesc = $scope.value;
								 $('#consentEditPopup').modal('hide');
									growl
									.success(
											'Concent Saved Successfully...!!',
											{
												title : 'Success!'
											});
							};
							
							$scope.viewTemplate  = function(templateObj,index)
					    	{
								
							
								$scope.FroalaEditor.html.set('');
									$scope.value = templateObj.templateDesc;
									$scope.selectedForm.templateDesc = templateObj.templateDesc;
									$scope.FroalaEditor.html.insert($scope.selectedForm.templateDesc);
									$scope.templateIndex = index;
									$('#consentEditPopup').modal('show');
							
								
								
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
						
							
							//For Test Temp
							
							$scope.toggleFunction=function(isAcceptOrReject,info,idx)
							{
								if(isAcceptOrReject=='A')
									{
									$scope.assignCounter(info,idx);
									}
								if(isAcceptOrReject=='R')
								{
									 
										var URI = BASE_URL + ROOT_URL + "/api/opd/updateToken/"+info.appointmentId+"/"+info.tokenNumber;
										GenericService
										.serviceAction("GET",URI,'')
										.then(function(response) {
													console.log("reee",response);
													if (response.data.status == "success") {
														//document.location.reload(true);
														$scope.searchAppointment($scope.appointmentSearchObject);
														$scope.disableCheckBox = true;
													}
												});
										
								}
							}
							
							$scope.assignCounter=function(item,idx)
							{
								
								if(item.admissionId>0)
								{
									growl.warning("Patient is currently admitted...!!",{title : "Warning!"});
									return false;
								}
								
								//alert(idx);
								//console.log("item",item);
								//alert($rootScope.counterId);
								if(angular.isUndefined($rootScope.counterId))
								{
									//growl.warning("Select counter first ",{title : "Warning!"});
									// return false;
									angular.element('#counterDetailsModal').modal('show');
									return false;
								}
								else
									{
									var URI = BASE_URL + ROOT_URL
									+ "/api/opd/assignCounterId";
									
									var statusChangeObj={
											appointmentId : item.appointmentId,
											counterId: $rootScope.counterId, 
											frontDeskArrival : "Y"
									}
									
									GenericService
									.serviceAction("POST",URI,statusChangeObj)
									.then(function(response) {
												console.log("reee",response);
												if (response.data.status == "success") {
													//$scope.appointmentList[idx].counterId= $rootScope.counterId;
													angular.forEach($scope.appointmentList, function(value, key) {
											              
											              if(value.appointmentId == item.appointmentId)
											               {
											                  value.counterId= $rootScope.counterId;
											                  value.frontDeskArrival = "Y"
											               }
											              });
												}
											});
									}
							}
							
							$scope.gotoCoverSheet = function(appointmentObj)
							{
								$state.go("OPDCoverSheet", {
									appointmentObj: appointmentObj,
								});
							}
							
							$scope.printAppointmentList = function(info) {
									var data =  {
									        "appName": "HIS",
									        "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
									        "reportDesignFilePath": "OPDAppointmentSlip.rpt",

									        "reportFormat": "PDF",
									        "exportFileName": "appointmentSleep",
									        "reportParams": [
									          {
											 "paramName": "@appointment_id",
									         "paramType": "Integer",
									         "paramValue": info.appointmentId
									       }
									        ]
									};
									reportService.generate(REPORT_API_URI1,data);

								}
							
							$scope.printOPDCasePaper=function(encounterId){
							    var data =  {
						                "appName": "HIS",
						                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
						                "reportDesignFilePath": "OPDCaseSheet_listview.rpt",
						                "reportFormat": "PDF",
						                "exportFileName": "OPDCoversheet",
						                "reportParams": [
									                {
									                  "paramName": "@EncounterId",
									                  "paramType": "Integer",
									                  "paramValue":  encounterId
									                }
									               
									                ]
							    };
							    console.log('data',data);
							    reportService.generate(REPORT_API_URI1,data);
							}
							
							
							$scope.saveToPatientByAppointmentToEncounter = function()
							{
								if($scope.radioToken == 'R')
									{
										if($scope.tokenPatientId == 0)
											{
											  growl.error("Please Select Patient..!!",{title : "Warning!"});
											  return false;
											}
										else
											{
												//goto encounter
											
											var URI = BASE_URL + ROOT_URL
											+ "/api/opd/assignPatient/"+$scope.appointmentObjToken.appointmentId+"/"+$scope.tokenPatientId;
											
											GenericService
											.serviceAction("GET",URI,"")
											.then(function(response) {
														console.log("reee",response);
														if (response.data.status == "success") {

															 angular.element('#patientDetailsForAppointments').modal('hide');
															 angular.element('#tokenNumberPopup').modal('hide');
															 $(".modal-backdrop").hide();
															 
															console.log("$scope.appointmentObjToken",$scope.appointmentObjToken);
															console.log("$scope.tokenPatientId",$scope.tokenPatientId);
															$scope.appointmentObjToken.patientId = $scope.tokenPatientId;
															$scope.appointmentObjToken.uhidNumber = $scope.uhidNumberToken; 
															
																$state.go("encounter.encounterDetails", {
																	appointmentObj: $scope.appointmentObjToken,
																});
														}
													});
											
											
											}
									}
								else
									{
									
									 angular.element('#patientDetailsForAppointments').modal('hide');
									 angular.element('#tokenNumberPopup').modal('hide');
									 $(".modal-backdrop").hide();
									 $scope.appointmentObjToken.patientId = 0;
									 $state.go("patientRegistration", {
										 patientRegistrationObj: $scope.appointmentObjToken,
										});
									}
							}
							
							
							$scope.clearSearchObject = function()
							{
								$scope.appointmentSearchObject = {
							    		 'doctorId':0,
							    		 'specialityId':0,
							    		 'organizationId':$scope.organizationId,
							    		 'unitId':$scope.unitId,
							    		 'patientId':0,
							    		 'specialityId':0,
							    		 'subSpecialityId':0,
							    		 'modalityId':0,
							    		 'visitTypeId':0,
							    		 'parentPackageId':"",
							     }
								
								$scope.patientName ='';
								$scope.uhidNumber = '';
								$scope.mobileNumber = '';
								$scope.packgeService = '';
								$scope.parentPackageId = '';
								$scope.datepickerFromDate = new Date();
								$scope.datepickerToDate = new Date();
								
							}
							
							
							$scope.getClassForColor = function(info)
							{
								  if(info.isCriticalOrder==1){
									   return 'td-bg-red';
								   }else if (info.isVip=='Y') 
								   {
									    return 'td-bg-blue';
								   }else if(info.appointmentTypeId==6){
									   return 'td-bg-green';
								   }
							}
							
							$scope.speech = function (theText) {
							      window.speechSynthesis.speak(new SpeechSynthesisUtterance(theText));
							  };
							  
							  $rootScope.topValue = 0;
								$scope.registerJustCheck = function(){
									var data = "Vivek_Satle"
									dataShare.sendData(data);
						    	}
								
								$scope.searchPackages = function(keyword)
								{
									    var packageSearchObj = {};
									
										packageSearchObj.organisationId = $scope.organizationId;
										packageSearchObj.unitId = $scope.unitId;
										packageSearchObj.visitTypeId = 1;//default op
										packageSearchObj.packageTypeId = 1;
										packageSearchObj.serviceName = keyword;
										packageSearchObj.serviceCode = keyword;
										return servicesFactory.searchPackageForEHC(packageSearchObj,$scope);
								}
								$scope.selectPackageService = function($item, $model, $label)
								{
									$scope.parentPackageId =  $item.parentPackageId;
								}
								
								$scope.printHemodialysisReport = function(info) {
									var data =  {
									        "appName": "HIS",
									        "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
									        "reportDesignFilePath": "getHemodialysisAppointmentslip.rpt",

									        "reportFormat": "PDF",
									        "exportFileName": "appointmentSleep",
									        "reportParams": [
									          {
											 "paramName": "appointment_id",
									         "paramType": "Integer",
									         "paramValue": info.appointmentId
									       }
									        ]
									};
									reportService.generate(REPORT_API_URI1,data);

								}
								
								//whatsapp and skype 
								$scope.whatsAppQRCode=function(countryCode,whatsappCallNo){	
									if(countryCode == null){
										var countrycode = "91"
									}else{
										var countrycode = countryCode;
									}
									var n = countryCode.length;
									
									var str = countrycode +whatsappCallNo;
									  var res = str.split("+");
									  
	                                if(res != null && res.length > 1){
	                                	var n = countryCode.length;
									    var qURL="https://wa.me/"+res[1];
									     $("#qrDetailsPopup").modal(); 
									     makeCodea(qURL);
	                                }else{
	                                	var n = countryCode.length;
									    var qURL="https://wa.me/"+res[0];
									     $("#qrDetailsPopup").modal(); 
									     makeCodea(qURL);
	                                }
									}
								
								var qrcode = new QRCode(document.getElementById("qrcode"), {
									width : 200,
									height : 200
								});

								function makeCodea(elText) {		
//								 	var qrcode = new QRCode(document.getElementById("qrcode"),{width : 200,height : 200});
									if (elText=='') {
										return;
									}
									qrcode.makeCode(elText);
								}
								
								$scope.skypeCall=function(skypeId){
									if(skypeId!='' && skypeId!=null){
										var url ="skype:"+ skypeId +"?call";
										window.open(url,"_blank");
									}else{
										growl.error("Patient has invalid skype Id",
												{
													title : 'Error!'
												});
									}
								}
								
								 /*$.FroalaEditor.DefineIcon('insert', {NAME: 'plus', SVG_KEY: 'add'});
								 $.FroalaEditor.RegisterCommand('insert', {
								    title: 'Insert HTML',
								    focus: true,
								    undo: true,
								    refreshAfterCallback: true,
								    callback: function () {
								    	var id=  Math.floor(Math.random() * 26) + Date.now();
								      this.html.insert(`<input  type='checkbox' ng-model='abc'>`);
								    }
								  });
								 
								 
								 $scope.froalaOptions = {
									        toolbarButtons : ["insert","print"],
									   	    events: {
										      'contentChanged': function() {
										        let editor = this
										        editor.html.get(true);
										        console.log(editor.html.get(true))
										      }
										    },
										    angularIgnoreAttrs: ['class', 'ng-model', 'id']
									    }*/
								
								//new FroalaEditor('#froala-editor', {key: "AVB8B-21A3A1D3E1E2B2uigecgyqC-7frspB2twG2suc1A6B5A4B3A3C2D2G3C5B4=="})
								//$('#froala-editor').FroalaEditor({key: "AVB8B-21A3A1D3E1E2B2uigecgyqC-7frspB2twG2suc1A6B5A4B3A3C2D2G3C5B4=="})
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


								$scope.redirectToOPDBilling = function(obj){
									var encounterObj={
											appointmentId : obj.appointmentId,
											specialityId : obj.deptId,
											patientId : obj.patientId,
											encounterId :obj.encounterId,
											visitTypeId:obj.visitTypeId,
											doctorId:obj.doctorId,
											doctorName:obj.doctorName,
											specialityName: obj.specialityName,
											paymentEntitlementTypeId:obj.paymentEntitlementTypeId,
											referralId : obj.referralId,
											patientCategoryId : obj.patientCategoryId,
											referralDesc : obj.referralDesc,
											counterId : $rootScope.counterId
									}
									
									
									$state.go('newOPDBilling',{
										newOpdBillObject : encounterObj
										});
								}
								
								$scope.customePatientDetailsObj ={};
								$scope.whoIsTested= {
									id:"1"
								};
								$scope.typeOfSite ={
										id :"1"
								};
								$scope.customDataDisplay = [];
								$scope.getPatientInformationDetails = function(info){
									var URI = BASE_URL + ROOT_URL+ "/api/global/getPatientInfoForHospitalVisit";
									var data={
											patientId : info.patientId,
											organizationId: $rootScope.organizationId, 
											unitId : $scope.unitId,
											encounterId : info.encounterId
									}
									GenericService.serviceAction("POST",URI,data).then(function(response) {
												console.log("reee",response);
												if (response.data.status == "success") {
													$scope.customePatientDetailsObj ={};
													$scope.whoIsTested= {
														id:"1"
													};
													$scope.typeOfSite ={
															id :"1"
													};
													$scope.customDataDisplay = [];
													$scope.customePatientDetailsObj =response.data.object;
													if($scope.customePatientDetailsObj!=null && $scope.customePatientDetailsObj.customDataPostDto!=null){
														if($scope.customePatientDetailsObj.customDataPostDto.who_tested!=null){
															if($scope.customePatientDetailsObj.customDataPostDto.who_tested=="Traveler"){
																$scope.whoIsTested.id = "1";
															}
															if($scope.customePatientDetailsObj.customDataPostDto.who_tested=="Case"){
																$scope.whoIsTested.id = "2";
															}
															if($scope.customePatientDetailsObj.customDataPostDto.who_tested=="Contact"){
																$scope.whoIsTested.id = "3";
															}
															if($scope.customePatientDetailsObj.customDataPostDto.who_tested=="Health Worker"){
																$scope.whoIsTested.id = "4";
															}
															if($scope.customePatientDetailsObj.customDataPostDto.who_tested=="Status"){
																$scope.whoIsTested.id = "5";
															}
														}
														if($scope.customePatientDetailsObj.customDataPostDto.type_of_site!=null){
															if($scope.customePatientDetailsObj.customDataPostDto.type_of_site=="Home"){
																$scope.typeOfSite.id = "1";
															}
															if($scope.customePatientDetailsObj.customDataPostDto.type_of_site=="Quarantine Center"){
																$scope.typeOfSite.id = "2";
															}
															if($scope.customePatientDetailsObj.customDataPostDto.type_of_site=="Institution"){
																$scope.typeOfSite.id = "3";
															}
															if($scope.customePatientDetailsObj.customDataPostDto.type_of_site=="POE"){
																$scope.typeOfSite.id = "4";
															}
															if($scope.customePatientDetailsObj.customDataPostDto.type_of_site=="Health Facility"){
																$scope.typeOfSite.id = "5";
															}
														}
													}
													
													if($scope.customePatientDetailsObj!=null &&  $scope.customePatientDetailsObj.customDataDisplay!=null){
														$scope.customePatientDetailsObj.customDataDisplay = $scope.customePatientDetailsObj.customDataDisplay.replace('{','');
														$scope.customePatientDetailsObj.customDataDisplay = $scope.customePatientDetailsObj.customDataDisplay.replace('}','');
														$scope.customePatientDetailsObj.customDataDisplay = $scope.customePatientDetailsObj.customDataDisplay.replaceAll('"','');
														var customDataDisplay = $scope.customePatientDetailsObj.customDataDisplay.split(',');
														
														$scope.customePatientDetailsObj.customDataDisplay = customDataDisplay;
														$scope.customDataDisplay = [];
														angular.forEach($scope.customePatientDetailsObj.customDataDisplay,function(value,index){
															var str = value.split(':');
															if(str!=null && str!=undefined){
																var newArr = {};
																newArr.keyName = str[0];
																newArr.value = str[1];
																$scope.customDataDisplay.push(newArr); 
															}
														});
													}
													
												}
											});
								}
						
								$scope.updateCustomDetails = function(){
									
									if($scope.typeOfSite.id == "1"){
										$scope.customePatientDetailsObj.customDataPostDto.type_of_site="Home";
									}else if($scope.typeOfSite.id == "2"){
										$scope.customePatientDetailsObj.customDataPostDto.type_of_site="Quarantine Center";
									}else if($scope.typeOfSite.id == "3"){
										$scope.customePatientDetailsObj.customDataPostDto.type_of_site="Institution";
									}else if($scope.typeOfSite.id == "4"){
										$scope.customePatientDetailsObj.customDataPostDto.type_of_site="POE";
									}else{
										$scope.customePatientDetailsObj.customDataPostDto.type_of_site="Health Facility";
									}
									
									if($scope.whoIsTested.id == "1"){
										$scope.customePatientDetailsObj.customDataPostDto.who_tested="Traveler";
									}else if($scope.whoIsTested.id == "2"){
										$scope.customePatientDetailsObj.customDataPostDto.who_tested="Case";
									}else if($scope.whoIsTested.id == "3"){
										$scope.customePatientDetailsObj.customDataPostDto.who_tested="Contact";
									}else if($scope.whoIsTested.id == "4"){
										$scope.customePatientDetailsObj.customDataPostDto.who_tested="Health Worker";
									}else{
										$scope.customePatientDetailsObj.customDataPostDto.who_tested="Status";
									}
									
									var customDataPost = JSON.stringify($scope.customePatientDetailsObj.customDataPostDto); 
									var URI = BASE_URL + ROOT_URL+ "/api/global/customDetails/updateCustomDetails";
									var data={
											customDetailsId : $scope.customePatientDetailsObj.customDetailsId,
											customDataPost : customDataPost
									}
									GenericService.serviceAction("POST",URI,data).then(function(response) {
												console.log("reee",response);
												if (response.data.status == "success") {
													$scope.customePatientDetailsObj={};
													$scope.whoIsTested= {
															id:"1"
														};
														$scope.typeOfSite ={
																id :"1"
														};
														$scope.customDataDisplay = [];
													growl.success("success",
		    												{
		    													title : 'Success!'
		    												});
												}
											});
								}
								$scope.isPRNRequired;
								$scope.wristBandList;
								$scope.renderInfo;
								$scope.callPrnFileSizeConfiguration = function(info)
								{
									
									$scope.renderInfo=info;
									var data={
											"organizationId" :$scope.organizationId,
											"unitId" :$scope.unitId
									}
									var URI = BASE_URL + ROOT_URL + "/api/opd/prnConfiguration";
									
									GenericService
									.serviceAction("POST",URI,data)
									.then(function(response) {
										console.log("response",response)
										 $('#Prn').modal('show');
										$scope.isPRNRequired=response.data.listObject[0];
										$scope.wristBandList=response.data.object;
										
									})
								}
								$scope.printRadiologyReport=function(info,printWithHeader){
							    	
							    	info.organizationId = $scope.organizationId;
							    	info.unitId =  $scope.unitId;
//							    	info.printWithHeader = printWithHeader;
							    	var URI = BASE_URL + ROOT_URL + "/LIS/transaction/wristBandPrnPrint";
							    	GenericService.serviceAction(METHOD_POST, URI, info,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
											{
								
										   if(response.data.status=='success')
										      {
											   
											   if($scope.isPRNRequired!=undefined && $scope.isPRNRequired!=null && $scope.isPRNRequired==true && response.data.object.templateDesc!=null){
													//JSPrinterManager call
//													print($scope.finalPRN);
													//For PRN Print manager
														$scope.printPRNPrint(response.data.object.templateDesc);
												}														
											   console.log("finalPRN",response.data.object.templateDesc);
										      }
										   else if(response.data.status=='error'){
											   $rootScope.stopLoader();
										   }
									});
								
							    }
								var labConfig  =    $scope.checkLabConfig();
							    $scope.prnUrl = labConfig.prnUrl;
								$scope.printPRNPrint= function(prnObject){						
									
									
							        var url = $scope.prnUrl;

							        var xhr = new XMLHttpRequest();
							        xhr.open("POST", url);

							        xhr.setRequestHeader("Content-Type", "text/plain");

							        xhr.onreadystatechange = function () {
							           if (xhr.readyState === 4) {
							              console.log(xhr.status);
							              console.log(xhr.responseText);
							           }};

							        var data = prnObject;
							        xhr.send(data);
							} 
								$scope.generateWristBand1 = function(info,labelName)
								{
									$scope.prn=info.labelName;
									$scope.prnFormat=info.prnFormat;
								}
								$scope.generateWristBand = function(info)
								{
									$('#wristBandAdmitted').modal('hide');
									info=$scope.renderInfo;
									if($scope.isPRNRequired==true)
						    		{
						    		
										var info1={
												patientId:info.patientId,
												templateDesc:$scope.prnFormat,
												admissionId:info.admissionId,
										        encounterId:info.encounterId,
										        wristBandPrnSize:$scope.prn,
								           }
						    		$scope.printRadiologyReport(info1,null);
										$('#Prn').modal('hide');
						    		}
									else
										{
										$('#wristBandAdmitted').modal('show');
									if(info.patientId > 0)
									{
									var age = $rootScope.getAge(info.dob);
									$scope.patientFullName = info.patientName
									+ " "
									+age
									+ "/"
									+ info.genderCode;
									}
								else
									{
									$scope.patientFullName = info.patientName
									+ " "
									+ info.dob
									+ "/"
									+ info.genderCode;
									}
									
									$scope.barcode = {
										        type: "code128",
										        code: info.uhidNumber,
										        hospital: $scope.unitName,
										        itemCode: info.encounterId.toString(),
										        batchCode: "123456",
										        patientFullName : $scope.patientFullName,
										        doctorName : (info.doctorName!= undefined && info.doctorName != null ) ?  info.doctorName : '',
										       
										      }
									
								}     
								}
								

								$scope.isVideoModalOpen = false;
						      	$scope.sndMeetingInvite1234 = function(info) {
						      		// if(info.videoCallUrl != null){
						      		// 	growl.warning("link already send on your registered mobile number and email",
									// 			{
									// 				title : 'Warning!'
									// 			});
						      		// 	return false;
						      		// }
						      		// if($scope.patientObj.email == null && $scope.patientObj.email == undefined && $scope.patientObj.email == ""){
				            		// 	growl.warning("please enter email!!",
									// 			{
									// 				title : 'Warning!'
									// 			});
				            		// }
    								// $rootScope.startLoader();
    								// $scope.date = moment(new Date()).format('DD-MM-YYYY');
    								// var data = {
    								// 		"patientId" : info.patientId,
									// 		"organizationId" :$scope.organizationId,
									// 		"unitId" :$scope.unitId,
									// 		"doctorId" :info.doctorId,
									// 		"email" : info.email,
									// 		"uhidNumber" : info.uhidNumber,
									// 		"dateTime" : info.appointmentDate+" "+ info.fromTimeString2
											
    								// };
    								// 		var URI = BASE_URL + ROOT_URL
    								// 		+ "/api/global/sendPatientSmsFromCoversheet";
    								// 		 GenericService.serviceAction("POST",
             						// 				URI, data).then(function(response) {
             						// 			if (response.data.status == "success") {
             						// 				$scope.searchAppointment();
             						// 				growl.success("Message sent successfully !!",
            						// 						{
            						// 							title : 'Success!'
            						// 						});
             						// 				 $rootScope.stopLoader();
             						// 			}
             						// 			var URL = response.data.object;
       								// 		  //  $window.open(URL, '_blank');

             						// 		});

								   $rootScope.startLoader();

										// Validation
										if (info.uuid == null ||
											info.uuid == undefined ||
											info.uuid == "") {

											growl.error(
												"Invalid video meeting link !!",
												{
													title : 'Error!'
												});

											$rootScope.stopLoader();

											return false;
										}

										// Set iframe URL
										$scope.videoJoinUrl =
											$sce.trustAsResourceUrl(
												"https://clinitalk.axonichealth.com/join/"
												+ info.uuid
											);

										// Open popup
										$scope.isVideoModalOpen = true;

										console.log($scope.isVideoModalOpen);

									

										var URI =
											BASE_URL
											+ ROOT_URL
											+ "/api/saveTwilioMsg";

										var data = {

											appointmentId : info.appointmentId,

											uuid : info.uuid,

											globalUhid : 0,

											hisUnitId : $scope.unitId,

											appointmentDate : info.appointmentDate,

											timeSlot : info.fromTimeString,
											doctorId:info.doctorId,

										};

										GenericService.serviceAction(
												"POST",
												URI,
												data)
											.then(function(response) {

												console.log("response", response);

												if (response.data.status == "success") {

													growl.success(
														"Success",
														{
															title : 'Success!'
														});

												} else {
														$rootScope.stopLoader();

													// growl.error(
													// 	"Something went wrong",
													// 	{
													// 		title : 'Error!'
													// 	});
												}
											});

												$rootScope.stopLoader();
    										

    							}

								$scope.isMinimized = false;

								$scope.toggleMinimize = function($event) {

									// Prevent drag event conflict
									if ($event) {
										$event.stopPropagation();
									}

									$scope.isMinimized =
										!$scope.isMinimized;
								};

								$scope.closeVideoModal = function($event) {

									// Prevent drag event conflict
									if ($event) {
										$event.stopPropagation();
									}

									$scope.isVideoModalOpen = false;

									$scope.videoJoinUrl = null;
								};
						      	
								$scope.printWristBand = function()
								{
									var printContents = $window.document.getElementById('opdWristCode').innerHTML;
							        var popupWin = $window.open('', '_blank', 'width=300,height=300');
							        popupWin.document.open();
							        popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print()">' + printContents + '</body></html>');
							        popupWin.document.close();
								}
								$scope.registrationFieldObj = {};
								var URI = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/getUnitApplicationRegistrationFieldConfig/"+$scope.organizationId+"/"+$scope.unitId;
								
								GenericService.serviceAction("GET", URI).then(function (response) {
									if (response.data.status == 'success') {
										
										$scope.fieldConfigList=response.data.listObject;
										if($scope.fieldConfigList.length>0)
											{
											 $scope.registrationFieldObj=$scope.fieldConfigList[0];
											 console.log("registrationFieldObj",$scope.registrationFieldObj);
											}else{
												$scope.registrationFieldObj={
														prefix: "Y",
														firstName: "Y",
														middleName: "N",
														lastName: "Y",
														birthDate: "Y",
														sex: "Y",
														callingCode: "Y",
														mobileNo: "Y",
														mobileDigit:"10",
														nationality:"Y",
														identificationDoc:"Y",
														race:"Y",
														maritalStatus:"Y",
														occupation:"Y",
														patientCategory:"Y",
														email:"N",
														sourceId:"N"
												}
											}
										
										

									} else if (response.data.status == 'error') {
										growl.error(response.data.message,
											{
												title: 'Error!'
											});
									}
								});
								
								
								$scope.showpaymentEntitlementDetails = function(info){
					                $rootScope.startLoader();
					                $scope.encounterDetails = {};
					                var newArr = {};
							        	newArr.encounterId = info.encounterId;
					                
					                var URI = BASE_URL + ROOT_URL + "/api/billing/getEncounterAndAdmissionDetails";
					                BillingGenericService.serviceAction("POST", URI, newArr, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					                    if (response.data.status == "success") {
					                        $scope.encounterDetails = response.data.object;
					                      //  angular.element('#paymentEntitlement').modal('show');
					                     	$('#paymentEntitlementPopUp').modal('show');
					                        $rootScope.stopLoader();
					                    } else if (response.data.status == "error") {
					                        $rootScope.stopLoader();
					                    }

					                });
					            };
								
								$scope.directCollectPayment = function(info){
									

					                $rootScope.startLoader();
					                $scope.encounterDetails = {};
					                var newArr = {};
							        	newArr.encounterId = info.encounterId;
					                
					                var URI = BASE_URL + ROOT_URL + "/api/billing/getBillIdByEncounterId";
					                BillingGenericService.serviceAction("POST", URI, newArr, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					                    if (response.data.status == "success") {
					                    	if(response.data.listObject != null)
					                    	$scope.billDetails = [];
					                        $scope.billDetails = response.data.listObject ;
					                        info.billId = $scope.billDetails[0].billingMasterId;
					                        $state.go("billList",{
												appointmentListObject :info
											});
					                        $rootScope.stopLoader();
					                    } else if (response.data.status == "error") {
					                        $rootScope.stopLoader();
					                    }

					                });
					            
									
								};
								
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



