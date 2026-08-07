'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:bedManagementController
 * @description # bedManagementController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'bedManagementController',
				[
						'$scope',
						'$http',
						'$localStorage',
						'$sessionStorage',
						'$cookies',
						'$rootScope',
						'GenericService',
						'commonDetailService',
						'GenericServiceParamHeader',
						'growl',
						'$stateParams',
						'BillingGenericService',
						'$state',
						//'kendoDirectives',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, commonDetailService,GenericServiceParamHeader,
								growl,$stateParams,BillingGenericService,$state) {

							
							/**================================COUNTER CODE START======================================*/
							$scope.isCounterApplicable = JSON.parse(sessionStorage.getItem('isCounterApplicable'));
							$scope.isUserCounterApplicable = JSON.parse(sessionStorage.getItem('isUserCounterApplicable'));
							if($scope.isCounterApplicable=='Y' && $scope.isUserCounterApplicable=='Y')
								{
								     var flag = $scope.getCounterDetails();
								     if(flag)
								    	 {
								    	 	$rootScope.counterId = $rootScope.counterMstObj.counterId!=""?$rootScope.counterMstObj.counterId:"";
								    	 	$rootScope.counterDesc = $rootScope.counterMstObj.counterDesc!=""?$rootScope.counterMstObj.counterDesc:"";
								    	 }
								     else
								     {
								    	 $scope.counterList = JSON.parse(localStorage.getItem('counterList'));
								     }
								}else{
									$rootScope.counterId = 0;
								}
							$scope.confirmCounter = function()
							 {
								//$scope.counterMstObj = $cookies.getObject('counterMstObj');
								//$scope.counterId = $scope.counterMstObj.counterId!=""?$scope.counterMstObj.counterId:"";
								$scope.counterMstObj = JSON.parse(sessionStorage.getItem('counterMstObj'));
								if($scope.counterMstObj == undefined || $scope.counterMstObj == null || $scope.counterMstObj.counterId == undefined){
									growl.warning('Select Counter');
									return false;
								}else if($scope.counterMstObj.counterId > 0){
									$scope.counterId = $scope.counterMstObj.counterId!=""?$scope.counterMstObj.counterId:"";
									angular.element('#counterDetailsModal').modal('hide');
								}
							 }
							/**================================COUNTER CODE END======================================*/
							
							
							/* init() function for form object create. */
							$scope.init = function() {
							$scope.erDivFlag=false;
							 $scope.bedManagementDivFlag=true;

							//	$rootScope.updateBedMagementCount();
								$scope.dateOptions = {
										formatYear : 'yyyy',
										showWeeks : false
									};
									$scope.openDatePicker = function() {
										// alert("in");
										$scope.datepicker.opened = true;
									};

									$scope.datepicker = {
										opened : false
									};
								
									$scope.openDatePickerPdd = function() {
										// alert("in");
										$scope.datepickerPdd.opened = true;
									};

									$scope.datepickerPdd = {
										opened : false
									};

									var cookieObject = $cookies.getObject('cookieObject');
									if(cookieObject == undefined){
										$state.go('login');
										return;
									}
									$scope.unitId = cookieObject.unitId;
									$scope.organizationId = cookieObject.organizationId;
									$scope.userId = cookieObject.userId;
									$scope.doctorIdForBedList  = cookieObject.doctorId;
									$scope.hideIcons = true;
									var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
									var labConfig = JSON.parse(localStorage.getItem('labConfig'));
									 if(cookieObjectAppConfig == undefined){
									  $state.go('login');
									  return;
									 }
									 else
									  {
									   //console.log('cookieObjectAppConfig',cookieObjectAppConfig);
									   
									  }
									 
								$scope.radiologySpecialityId = cookieObjectAppConfig.radiologySpecialityId;
								$scope.hemodialysisSpecialityId = cookieObjectAppConfig.hemodialysisSpecialityId;
								$scope.optimizeTransferFlow=cookieObjectAppConfig.optimizeTransferFlow;
								if(labConfig != undefined && labConfig != null){
									$scope.labSpecialityId = labConfig.labSpecialityId;
								}else{
									$scope.labSpecialityId =null;
								}
								
								$scope.nurseRole = cookieObjectAppConfig.nurseRole;	 
								$scope.doctorRole = cookieObjectAppConfig.doctorRole;	 
								$scope.roleName = cookieObjectAppConfig.roleName;
								$scope.isShowPatientByBed=cookieObjectAppConfig.isShowPatientByBed;
								$scope.patientSearchDiv=true;
								$rootScope.linkHideShow=true;
								

								$rootScope.loginpage = true;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
							
								
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
								$scope.bedsearch = {
										floorId : 0,
										nursingStationId : 0,
										bedCategoryId : 0,
										wardId : 0,
										patientId : 0,
										tPatientId : 0,
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										isIp : 'Y'
									}

								var data = {};
								var commonObj = {
								unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								
								if(!$rootScope.storeReady()){ // store-accessor campaign: dashboard family — access-check only, its OWN popup (storePopupAcesss)
									$rootScope.storePopupAcesss();
								}
								$scope.nursingStationCookieShow = JSON.parse(sessionStorage.getItem('nursingStationDesc'));

								$scope.isWardChanged = function()
								{      
									//start for nursing
									var URI = BASE_URL+ROOT_URL+"/api/global/getDetailedNursingStationList/"+$scope.userId+"/"+$scope.organizationId+"/"+$scope.unitId;
									GenericService.serviceAction("GET",URI,{}).then(function(response){
										if (response.data.status == "success") {
											$scope.departmentNursingStationMapperDtoslist = [];			  
											$scope.departmentNursingStationMapperDtoslist = response.data.listObject;
											//alert($scope.roleName);
											if($scope.nurseRole == 1){
												if($scope.departmentNursingStationMapperDtoslist.length > 0)
												{
													if($scope.departmentNursingStationMapperDtoslist.length > 1){
														angular.element('#nursingStationModal').modal('show');
														//$('#nursingStationModal').modal({backdrop: 'static', keyboard: false});
													}
													else
													{
														var URI = BASE_URL+ROOT_URL+"/api/global/getDeptListByNursingStationId/"+$scope.departmentNursingStationMapperDtoslist[0].nursingStationId+"/"+$scope.organizationId+"/"+$scope.unitId;
														GenericService.serviceAction("GET",URI,{}).then(function(response){
															if (response.data.status == "success") {
																$scope.departmentNursingStationListC = {};
																// console.log("fff",response.data.object);
																$scope.departmentNursingStationListC.departmentNursingStationlist = response.data.listObject;					  
																sessionStorage.setItem('specialtyNursingStationCookies', JSON.stringify($scope.departmentNursingStationListC));
																sessionStorage.setItem('specialtyNursingStationCookiesNur', JSON.stringify($scope.departmentNursingStationListC));
																sessionStorage.setItem('nursingStationsWardsList', JSON.stringify(response.data.object));
																  sessionStorage.setItem('nursingStationId', JSON.stringify($scope.departmentNursingStationMapperDtoslist[0].nursingStationId));
																//$cookies.putObject('nursingStationDesc',$scope.departmentNursingStationMapperDtoslist.nursingStationDesc);
																sessionStorage.setItem('nursingStationDesc', JSON.stringify($scope.departmentNursingStationMapperDtoslist[0].nursingStationDesc));
																$scope.wardListForGetBedList = response.data.object;
															}
														});
													}
												}
											}
										}
									});        
								}

								$scope.getBedListForScreen = function()
								{
									$rootScope.startLoader();
									var bedListRequest = {
											unitId : $scope.unitId,
											organizationId : $scope.organizationId,
											radiologySpecialityId : $scope.radiologySpecialityId,
											hemodialysisSpecialityId : $scope.hemodialysisSpecialityId,
											labSpecialityId : $scope.labSpecialityId,
											isIp : 'Y'
										};
									if($scope.doctorRole == 1)
										{
										  $scope.hideIcons = true;
										  bedListRequest.doctorId =$scope.doctorIdForBedList;
										  $scope.bedsearch.doctorId =$scope.doctorIdForBedList;
										  var URI = BASE_URL + ROOT_URL
			                                //+ GETADMITTEDPATIENTLIST;
											+ "/api/adt/bed/management";
									GenericService
											.serviceAction("POST", URI, bedListRequest)
											.then(
													function(response) {
														//$scope.listOfAdmittedPatientList = [];
														$scope.patientBedList = [];

														if (response.data.status == "success") {
															$scope.patientBedList = response.data.listObject;
															console.log("gg",$scope.patientBedList)
															$scope.commonWardListOrBedList=[];
															$rootScope.stopLoader();
															if($scope.isShowPatientByBed=='Y')
															{
																$scope.commonWardListOrBedList=$scope.mappedNurseBedList;
															}
															else{
																$scope.commonWardListOrBedList=$scope.wardListForView;
															}
															 /*angular.forEach($scope.patientBedList, function(value, key) {
																  $scope.getOrderedProcedureList(value.patientId);
															 });*/
														}
														});
										}
									else if($scope.nurseRole == 1){
										$scope.hideIcons = true;
										bedListRequest.wardList = $scope.wardListForGetBedList;
										 $scope.bedsearch.wardList =$scope.wardListForGetBedList;
										var URI = BASE_URL + ROOT_URL
		                                //+ GETADMITTEDPATIENTLIST;
										+ "/api/adt/bed/management";
								GenericService
										.serviceAction("POST", URI, bedListRequest)
										.then(
												function(response) {
													//$scope.listOfAdmittedPatientList = [];
													$scope.patientBedList = [];

													if (response.data.status == "success") {
														$scope.patientBedList = response.data.listObject;
														console.log("gg",$scope.patientBedList)
														$scope.commonWardListOrBedList=[];
														$rootScope.stopLoader();
														if($scope.isShowPatientByBed=='Y')
														{
															$scope.commonWardListOrBedList=$scope.mappedNurseBedList;
														}
														else{
															$scope.commonWardListOrBedList=$scope.wardListForView;
														}
														 /*angular.forEach($scope.patientBedList, function(value, key) {
															  $scope.getOrderedProcedureList(value.patientId);
														 });*/
													}
													});
									}else{
										$scope.hideIcons = false;
										var URI = BASE_URL + ROOT_URL
										+ "/api/adt/bed/management";
								GenericService
										.serviceAction("POST", URI, bedListRequest)
										.then(
												function(response) {
													//$scope.listOfAdmittedPatientList = [];
													$scope.patientBedList = [];

													if (response.data.status == "success") {
														$scope.patientBedList = response.data.listObject;
														console.log("gg",$scope.patientBedList)
														$scope.commonWardListOrBedList=[];
														$rootScope.stopLoader();
														if($scope.isShowPatientByBed=='Y')
														{
															$scope.commonWardListOrBedList=$scope.mappedNurseBedList;
														}
														else{
															$scope.commonWardListOrBedList=$scope.wardListForView;
														}
														 /*angular.forEach($scope.patientBedList, function(value, key) {
															  $scope.getOrderedProcedureList(value.patientId);
														 });*/
													}
													});
									}
									
											
								}
								
								
								var nursingStationsWardsList = JSON.parse(sessionStorage.getItem('nursingStationsWardsList'));
								if(nursingStationsWardsList == undefined)
								{
									//start for nursing
									var URI = BASE_URL+ROOT_URL+"/api/global/getDetailedNursingStationList/"+$scope.userId+"/"+$scope.organizationId+"/"+$scope.unitId;
									  GenericService.serviceAction("GET",URI,{}).then(function(response){
										  if (response.data.status == "success") {
											  $scope.departmentNursingStationMapperDtoslist = [];
											  
												$scope.departmentNursingStationMapperDtoslist = response.data.listObject;
												//alert($scope.roleName);
												if($scope.nurseRole == 1){
												if($scope.departmentNursingStationMapperDtoslist.length > 0)
													{
														if($scope.departmentNursingStationMapperDtoslist.length > 1){
															angular.element('#nursingStationModal').modal('show');
															//$('#nursingStationModal').modal({backdrop: 'static', keyboard: false});
														}
														else
															{
															var URI = BASE_URL+ROOT_URL+"/api/global/getDeptListByNursingStationId/"+$scope.departmentNursingStationMapperDtoslist[0].nursingStationId+"/"+$scope.organizationId+"/"+$scope.unitId;
															  GenericService.serviceAction("GET",URI,{}).then(function(response){
																  if (response.data.status == "success") {
																	  $scope.departmentNursingStationListC = {};
																	 // console.log("fff",response.data.object);
																	  $scope.departmentNursingStationListC.departmentNursingStationlist = response.data.listObject;
																	  
																	  sessionStorage.setItem('specialtyNursingStationCookies', JSON.stringify($scope.departmentNursingStationListC));
																	  sessionStorage.setItem('specialtyNursingStationCookiesNur', JSON.stringify($scope.departmentNursingStationListC));
																	  sessionStorage.setItem('nursingStationsWardsList', JSON.stringify(response.data.object));
																	  sessionStorage.setItem('nursingStationId', JSON.stringify($scope.departmentNursingStationMapperDtoslist[0].nursingStationId));
																	  //$cookies.putObject('nursingStationDesc',$scope.departmentNursingStationMapperDtoslist.nursingStationDesc);
																	  sessionStorage.setItem('nursingStationDesc', JSON.stringify($scope.departmentNursingStationMapperDtoslist[0].nursingStationDesc));
																	  $scope.wardListForGetBedList = response.data.object;
																	  $scope.getBedListForScreen();
																	  
																  }
															 });
															}
													}
										  		}
											}
									 });
									//end for nursing
								}
								else
								{
									$scope.wardListForGetBedList = nursingStationsWardsList;
									$scope.getBedListForScreen();
								}
								
								  
								  
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
									
									//get bed list mapped to nurse
										var URI =  BASE_URL + ROOT_URL+"/api/global/getbedlistbyNurseId";
										var data={
												orgId:$scope.organizationId,
												unitId:$scope.unitId,
												nurseId:$scope.userId,
												shiftDate:moment(new Date()).format('DD-MM-YYYY')
										}
										GenericService
												.serviceAction("POST", URI, data)
												.then(
														function(response) {
															$scope.mappedNurseBedList = [];
				
															if (response.data.status == "success") {
																$scope.mappedNurseBedList = response.data.listObject;
																var i;
																var bedList=[];
																for(i=0;i<$scope.mappedNurseBedList.length;i++){
																	bedList.push($scope.mappedNurseBedList[i].bedId);
																}
																 sessionStorage.setItem('mappedNurseBedList', JSON.stringify(bedList));
																//console.log("mappedNurseBedList:",$scope.mappedNurseBedList);
															}
														});	
									
									// For Get Ward  List 
									   var URI =  BASE_URL + ROOT_URL+GETACTIVEWARDMASTERLIST;
									GenericService
											.serviceAction("POST", URI, commonObj)
											.then(
													function(response) {
														$scope.wardListForView = [];
			
														if (response.data.status == "success") {
															$scope.wardListForView = response.data.listObject;
															//console.log("wardListForView",$scope.wardListForView);
														}
													});
								  	
							   
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEBEDCATEGORYLISTBYUNIT;
						GenericService
								.serviceAction("POST", URI, commonObj)
								.then(
										function(response) {
											$scope.bedCategoryList = [];

											if (response.data.status == "success") {
												$scope.bedCategoryList = response.data.listObject;

											}
										});
								
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEFLOORLIST;
						GenericService
								.serviceAction("POST", URI, commonObj)
								.then(
										function(response) {
											$scope.activeFloorList = [];

											if (response.data.status == "success") {
												$scope.activeFloorList = response.data.listObject;

											}
										});
						$scope.patientBedList = [];
						
						//Comment By 12-8-2019 Start
						/*var bedListRequest = {
								unitId : $scope.unitId,
								organizationId : $scope.organizationId
							};
						bedListRequest.wardList = $scope.wardListForGetBedList;
								var URI = BASE_URL + ROOT_URL
                                //+ GETADMITTEDPATIENTLIST;
								+ "/api/adt/bed/management";
						GenericService
								.serviceAction("POST", URI, bedListRequest)
								.then(
										function(response) {
											//$scope.listOfAdmittedPatientList = [];
											$scope.patientBedList = [];


											if (response.data.status == "success") {
												$scope.patientBedList = response.data.listObject;
												console.log("gg",$scope.patientBedList)
												$scope.commonWardListOrBedList=[];
												if($scope.isShowPatientByBed=='Y')
												{
													$scope.commonWardListOrBedList=$scope.mappedNurseBedList;
												}
												else{
													$scope.commonWardListOrBedList=$scope.wardListForView;
												}
												 angular.forEach($scope.patientBedList, function(value, key) {
													  $scope.getOrderedProcedureList(value.patientId);
												 });
											}
											});*/
						//Comment By 12-8-2019 END
						 
						
						$scope.set_color = function(totalScore) {
						    /*if(totalScore <= 2){
						        return {"background-color": "green"};
						    }
						    else if(totalScore >= 2 &&  totalScore <=6 ){
						        return {"background-color": "orange"};
						        
						    }else if(totalScore> 6){
						        return {"background-color": "red"};
						        
						    }*/
						};
						
						$scope.statego= function(){
					
							$state.go('bedManagement');
						}
						
						
							}

							$scope.init();
							
							if($scope.doctorRole == 1)
							{
								$scope.getBedListForScreen();
							}
							
							if($scope.doctorRole != 1 && $scope.nurseRole != 1 )
							{
								$scope.getBedListForScreen();
							}
							
							
							
							$scope.departmentNursing = {};
							$scope.selectsetNursingStation = function()
							{
								if($scope.departmentNursing == undefined || $scope.departmentNursing.length == 0){
									growl.warning('Please select nursing station..!!');
									return false;
								}else{
								$scope.wardListForGetBedList=[];
								var data = JSON.parse($scope.departmentNursing);
								//console.log("$scope.departmentNursing",$scope.departmentNursing);
								//console.log("$scope.departmentNursing ID",data.nursingStationId);
								var URI = BASE_URL+ROOT_URL+"/api/global/getDeptListByNursingStationId/"+data.nursingStationId+"/"+$scope.organizationId+"/"+$scope.unitId;
								  GenericService.serviceAction("GET",URI,{}).then(function(response){
									  if (response.data.status == "success") {
										  $scope.departmentNursingStationListC = {};
										  //console.log('nursingStationsWardsList',response.data.object);
										    $scope.departmentNursingStationListC.departmentNursingStationlist = response.data.listObject;
											sessionStorage.setItem('specialtyNursingStationCookies', JSON.stringify($scope.departmentNursingStationListC));
											sessionStorage.setItem('specialtyNursingStationCookiesNur', JSON.stringify($scope.departmentNursingStationListC));
											sessionStorage.setItem('nursingStationsWardsList', JSON.stringify(response.data.object));
											sessionStorage.setItem('nursingStationDesc', JSON.stringify(data.nursingStationDesc));
											sessionStorage.setItem('nursingStationId', JSON.stringify(data.nursingStationId));
											$scope.wardListForGetBedList = response.data.object;
											$scope.nursingStationCookieShow = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
											$scope.getBedListForScreen();
											angular.element('#nursingStationModal').modal('hide');
										}
								 });
							}
							}
							
							$scope.getAdmittedPatientByIds = function(commonType) {

								//console.log($scope.bedsearch);

								var commonData = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								switch (commonType) {
								case 'floor': 

									commonData.floorId = $scope.bedsearch.floorId;

									var URI = BASE_URL + ROOT_URL
											+ GETWARDLISTBYFLOORIDORGUNITID;
									GenericService
											.serviceAction("POST", URI,
													commonData)
											.then(
													function(response) {
														$scope.wardList = [];

														if (response.data.status == "success") {
															$scope.wardList = response.data.listObject;

														}
													});
									break;
								case 'ward':

									commonData.wardId = $scope.bedsearch.wardId;

									var URI = BASE_URL
											+ ROOT_URL
											+ GETACTIVENURSINGSTATIONLISTBYWARDID;
									GenericService
											.serviceAction("POST", URI,
													commonData)
											.then(
													function(response) {
														$scope.nursingStationList = [];

														if (response.data.status == "success") {
															$scope.nursingStationList = response.data.listObject;

														}
													});
									break;
								default:

								}

							}
							
							
							$scope.searchGroupList = function(keyword) {

								var data = {
									firstName : keyword,
									unitId : $scope.unitId,
									// visitType :
									// $scope.selectedPageNameForAdmissionNote,
									organizationId : $scope.organizationId
								}
								//console.log(data);

								var URI = BASE_URL + ROOT_URL
										+ GETPATIENTBYKEYWORD;
								return GenericService.serviceAction("POST",
										URI, data).then(function(response) {

									if (response.data.status == "success") {
										// alert("ff");
										// console.log(response.data.listObject);
										return response.data.listObject;

									}
								});
								// return data="[{name:'test'}]";

							}
							
							$scope.selectPatientIdOnSearch = function(info, model,
									label) {
									if(info.patientId != null)
									$scope.bedsearch.patientId  = info.patientId;
									if(info.tPatientId !=null)
									$scope.bedserach.tPatientId = info.tPatientId;
							}
							
							
							$scope.patientSearchByMultipleCriteria = function() {

								$rootScope.startLoader();
								// $scope.bedsearch.isVirtual =
								// ($scope.bedsearch.isVirtual == true) ? "Y":
								// "N";
								//console.log($scope.bedsearch);
								//return false;
								$scope.bedsearch = {
										
										radiologySpecialityId : $scope.radiologySpecialityId,
										hemodialysisSpecialityId : $scope.hemodialysisSpecialityId,
										labSpecialityId : $scope.labSpecialityId,
										floorId : $scope.bedsearch.floorId,
										nursingStationId : $scope.bedsearch.nursingStationId,
										bedCategoryId : $scope.bedsearch.bedCategoryId,
										wardId : $scope.bedsearch.wardId,
										patientId : $scope.bedsearch.patientId,
										//tPatientId : 0,
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										isIp : 'Y'

									}
								if($scope.doctorRole == 1)
								{
										$scope.hideIcons = true;
										$scope.bedsearch.doctorId =$scope.doctorIdForBedList;
								}else if($scope.nurseRole ==1){
									$scope.hideIcons = true;
									$scope.bedsearch.wardList = $scope.wardListForGetBedList;
								}else{
									$scope.hideIcons = false;
								}

								var URI = BASE_URL + ROOT_URL
										+ "/api/adt/bed/management/search";
								GenericService
										.serviceAction("POST", URI,
												$scope.bedsearch)
										.then(
												function(response) {
													$scope.activeBedList = [];

													if (response.data.status == "success") {

														if(response.data.listObject.length>0)
														{
															$scope.patientBedList = response.data.listObject;
															console.log("gg",$scope.patientBedList)
															$scope.commonWardListOrBedList=[];
															$rootScope.stopLoader();
															if($scope.isShowPatientByBed=='Y')
															{
																$scope.commonWardListOrBedList=$scope.mappedNurseBedList;
															}
															else{
																$scope.commonWardListOrBedList=$scope.wardListForView;
															}
															
															
															$scope.bedsearch = {
																	floorId : 0,
																	nursingStationId : 0,
																	bedCategoryId : 0,
																	wardId : 0,
																	patientId : 0,
																	tPatientId : 0,
																	organizationId : $scope.organizationId,
																	unitId : $scope.unitId,

																}
															$scope.groupName = "";
														}else{
															$rootScope.stopLoader();
															
															$scope.bedsearch = {
																	floorId : 0,
																	nursingStationId : 0,
																	bedCategoryId : 0,
																	wardId : 0,
																	patientId : 0,
																	tPatientId : 0,
																	organizationId : $scope.organizationId,
																	unitId : $scope.unitId,

																}
															$scope.groupName = "";
															
															growl.warning("No record found...!!");
														}
														
														

													}
													else
														{
														$rootScope.stopLoader();
														}
												});

							}
							
							$scope.popupData = function(item)
							{
								//console.log(item);
								$scope.loadTreeView();
								$scope.patientDetails = {};
								if(item.patientId > 0)
									{
									 var age = $rootScope.getAge(item.dob);
									
									 $scope.patientDetails.patientName =item.pFirstName+' '+age+'/'+item.genderCode; 
									}
								else
									{
									$scope.patientDetails.patientName =item.pFirstName+' '+item.dob+'/'+item.genderCode;
									}
								$scope.patientDetails.uhidNumber = item.uhidnumber;
								$scope.patientDetails.dob = item.dob;
								$scope.patientDetails.dateOfAdmission = item.doa;
								$scope.patientDetails.patientDischargeDate = item.pdd;
								$scope.patientDetails.bedNumber = item.bedNumber;
								$scope.patientDetails.doctorFullName = item.dFirstName;
								$scope.patientDetails.wardName = item.wardName;
								$scope.patientDetails.bedNumber = item.bedNumber;
								$scope.patientDetails.genderId = item.genderId;
								sessionStorage
								.setItem("admissionNoteObjER",JSON.stringify(item));
								
								$scope.bedManagmentCookiesData = {
											admissionId:item.admissionId,
											type:'bm',
											patientFullName :item.pFirstName+' '+age+'/'+item.genderCode,
											uhidNum : item.uhidnumber,
											bedNumber : item.bedNumber,
											doctorFullName : item.dFirstName,
											wardName : item.wardName,
											patientId : item.patientId,
											tPatientId : item.tPatientId,
											visitTypeId : item.visitTypeId,
											doctorId : item.doctorId,
											dischargeId : item.dischargeId,
											age:age,
											genderCode:item.genderCode,
											genderId:item.genderId,
											dob:item.dob,
											patientName:item.pFirstName,
											doa:item.doa,
											pdd:item.pdd,
											bedCategoryId:item.bedCategoryId,
											billingBedCategoryId:item.billingBedCategoryId,
											bedId:item.bedId,
											wardId:item.wardId,
											floorId:item.floorId,
											roomId:item.roomId,
											dischargeStatusId:item.dischargeStatusId,
											patientConsumableTab: 2,
											isDeath : item.isDeath
										};
								
								sessionStorage.setItem('bedManagmentCookies', JSON.stringify($scope.bedManagmentCookiesData));
								$scope.billing = true;
								$scope.getAllPatientDiagnosis(item.patientId);
								$rootScope.allergydata(item);
							}
							
							
							
							$scope.callAdmissionNote = function()
							{
								angular
								.element(
										'#patientServices')
								.modal('hide');
								
								$rootScope.getAdmissionNote('ER');
								
							}

							
							$scope.getbedStatus = function()
							{
								var data = {
										organizationId:$scope.organizationId,
										unitId : $scope.unitId
								}
								
								var URI = BASE_URL + ROOT_URL
								+ "/api/adt/bedCategoryAvailabilityCount/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId;

							BillingGenericService
									.serviceAction(METHOD_GET, URI, data,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												console.log(response);
												$scope.bedStatusList = [];
												// $rootScope.startLoader();
												if (response.data.status == "success")
													{
													  $scope.bedStatusList =  response.data.listObject;
													}
													
													
											});
						
							}
							

							$scope.search = {
								allocationTypeId : 0,
								patientId : 0
							}

							$scope.searchGroupList = function(keyword) {

								var data = {
									firstName : keyword,
									unitId : $scope.unitId,
									// visitType :
									// $scope.selectedPageNameForAdmissionNote,
									organizationId : $scope.organizationId
								}
								//console.log(data);

								var URI = BASE_URL + ROOT_URL
										+ GETPATIENTBYKEYWORD;
								return GenericService.serviceAction("POST",
										URI, data).then(function(response) {

									if (response.data.status == "success") {
										return response.data.listObject;

									}
								});

							}

							
		                  $scope.getmappedDataCoversheetByUser = function(){
								
								var URI = BASE_URL + ROOT_URL + "/api/opd/getMappedCoversheetDataForUser";
								var data = {
										  userId:$scope.userId,
									      unitId:$scope.unitId
								}
								GenericService.serviceAction("POST",URI,data).then(function(response){
									if(response.data.status == "success"){
									$rootScope.stopLoader();					
									$scope.mappedDataCoversheetByUser = [];
									$scope.mappedDataCoversheetByUser = JSON.parse(response.data.listObject[0].tabAccessIds);
									localStorage.setItem("mappedDataCoversheetByUser", JSON.stringify($scope.mappedDataCoversheetByUser));
									console.log("mappedDataCoversheetByUser",$scope.mappedDataCoversheetByUser);
									}
								});
								}
			                $scope.getmappedDataCoversheetByUser(); 
			                
			                
			                
							$scope.selectPatientId = function(info, model,
									label) {
								$scope.search.patientId = info.patientId
							}
                             
							
							$scope.gotoCoversheet=function(patientDetailsObj){
								
									if(patientDetailsObj!=null)
									  {
										patientDetailsObj.startConsultanceStartTime = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'); 
										console.log("patientDetails",JSON.stringify(patientDetailsObj));
										if($scope.roleName=="doctor"){
													$state.go('IPDCoverSheet',
														{
														patientDetailsObj :patientDetailsObj
														});
										}
										else if($scope.roleName=="nurse"){
											
											$state.go('nursingCoversheet',
													{
													patientDetailsObj :patientDetailsObj
													});
										}
									  }
								  else
								     {
									    growl.error("Something went Wrong.",{title : 'Error!'});
								     }
								
								
								
							}
				
							$scope.getERAdmittedPatientByAllocationPatientId = function() {
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									allocationType : $scope.search.allocationTypeId,
									patientId : $scope.search.patientId
								};
							}

							$scope.selectPatient = function(item) {

								$scope.itemSelecteds = [];
								// If checkbox is checked
								if ($scope.admittedPatient) {
									$scope.itemSelecteds = item;
									$scope.visitorPassPopUpFlag = true;

								}
							}
							
							
							
						
							
							$scope.closePopUpWithRedirect = function(link)
							{
								$(".modal-backdrop").hide();
								if (sessionStorage.getItem('bedManagmentCookies') != null) {
								   $scope.bedManagmentCookiesObj = JSON.parse(sessionStorage.getItem('bedManagmentCookies'));
								}
								if(link != undefined)
									{
									 angular.element('#patientServices').modal('hide');
									 if(link == 'patientIndent'){
										if (sessionStorage.getItem('bedManagmentCookies') != null) {
											$scope.bedManagmentCookiesObj = JSON.parse(sessionStorage.getItem('bedManagmentCookies'));
											$scope.currentDate = new Date();
											$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
												commonDetailService.setDataIdForBedManagmentPatientIssueAcceptance($scope.bedManagmentCookiesObj.visitTypeId, $scope.bedManagmentCookiesObj.patientId, 
													$scope.bedManagmentCookiesObj.patientName, 
													$scope.bedManagmentCookiesObj.uhidNum, $scope.bedManagmentCookiesObj.wardId,$scope.bedManagmentCookiesObj.wardName,
													 $scope.bedManagmentCookiesObj.floorId,  $scope.bedManagmentCookiesObj.doctorId,  $scope.bedManagmentCookiesObj.doctorFullName);
											}
										$state.go('patientIndent');
									 }
									 if(link == 'patientIssueAcceptance'){
										//commonDetailService.setDataIdForBedManagment(mapList.admissionId, mapList.uhidnumber);
										if (sessionStorage.getItem('bedManagmentCookies') != null) {
											$scope.bedManagmentCookiesObj = JSON.parse(sessionStorage.getItem('bedManagmentCookies'));
											$scope.currentDate = new Date();
											$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
											//if($scope.bedManagmentCookiesObj.isDeath != null && $scope.bedManagmentCookiesObj.isDeath != 'N'){
												commonDetailService.setDataIdForBedManagmentPatientIssueAcceptance($scope.bedManagmentCookiesObj.visitTypeId, $scope.bedManagmentCookiesObj.patientId, 
													$scope.bedManagmentCookiesObj.patientName, 
													$scope.bedManagmentCookiesObj.uhidNum, $scope.bedManagmentCookiesObj.wardId,$scope.bedManagmentCookiesObj.wardName,
													 $scope.bedManagmentCookiesObj.floorId,  $scope.bedManagmentCookiesObj.doctorId,  $scope.bedManagmentCookiesObj.doctorFullName);
											}
										$state.go('patientIssueAcceptance');
									 }
									 if(link == 'consumableAcceptance'){
										//commonDetailService.setDataIdForBedManagment(mapList.admissionId, mapList.uhidnumber);
										if (sessionStorage.getItem('bedManagmentCookies') != null) {
											$scope.bedManagmentCookiesObj = JSON.parse(sessionStorage.getItem('bedManagmentCookies'));
											$scope.currentDate = new Date();
											$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
											//if($scope.bedManagmentCookiesObj.isDeath != null && $scope.bedManagmentCookiesObj.isDeath != 'N'){
												commonDetailService.setDataIdForBedManagmentPatientIssueAcceptance($scope.bedManagmentCookiesObj.visitTypeId, $scope.bedManagmentCookiesObj.patientId, 
													$scope.bedManagmentCookiesObj.patientName, 
													$scope.bedManagmentCookiesObj.uhidNum, $scope.bedManagmentCookiesObj.wardId,$scope.bedManagmentCookiesObj.wardName,
													 $scope.bedManagmentCookiesObj.floorId,  $scope.bedManagmentCookiesObj.doctorId,  $scope.bedManagmentCookiesObj.doctorFullName);
											}
										$state.go('consumableAcceptance');
									 }
									 if(link == 'salesReturn'){
										if (sessionStorage.getItem('bedManagmentCookies') != null) {
											$scope.bedManagmentCookiesObj = JSON.parse(sessionStorage.getItem('bedManagmentCookies'));
											$scope.currentDate = new Date();
											$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
												commonDetailService.setDataIdForBedManagmentSalesRetun($scope.bedManagmentCookiesObj.patientId, 
													$scope.bedManagmentCookiesObj.patientName, $scope.bedManagmentCookiesObj.uhidNum);
											}
										$state.go('patientIssueReturn');

									 }
									 if(link == 'billableConsumption'){
										if (sessionStorage.getItem('bedManagmentCookies') != null) {
											$scope.bedManagmentCookiesObj = JSON.parse(sessionStorage.getItem('bedManagmentCookies'));
											$scope.currentDate = new Date();
											$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
												commonDetailService.setDataIdForBedManagmentPatientIssueAcceptance($scope.bedManagmentCookiesObj.visitTypeId, $scope.bedManagmentCookiesObj.patientId, 
													$scope.bedManagmentCookiesObj.patientName, 
													$scope.bedManagmentCookiesObj.uhidNum, $scope.bedManagmentCookiesObj.wardId,$scope.bedManagmentCookiesObj.wardName,
													 $scope.bedManagmentCookiesObj.floorId,  $scope.bedManagmentCookiesObj.doctorId,  $scope.bedManagmentCookiesObj.doctorFullName);
											}
											
										$state.go('billableConsumption');
										

									 }
									 
									 if(link == 'pendingRejectedRejections'){
										if (sessionStorage.getItem('bedManagmentCookies') != null) {
											$scope.bedManagmentCookiesObj = JSON.parse(sessionStorage.getItem('bedManagmentCookies'));
											$scope.currentDate = new Date();
											$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
												commonDetailService.setDataIdForBedManagmentPatientIssueAcceptance($scope.bedManagmentCookiesObj.visitTypeId, $scope.bedManagmentCookiesObj.patientId, 
													$scope.bedManagmentCookiesObj.patientName, 
													$scope.bedManagmentCookiesObj.uhidNum, $scope.bedManagmentCookiesObj.wardId,$scope.bedManagmentCookiesObj.wardName,
													 $scope.bedManagmentCookiesObj.floorId,  $scope.bedManagmentCookiesObj.doctorId,  $scope.bedManagmentCookiesObj.doctorFullName);
											}
											$state.go('pendingRejectedRejections');

									 }
									 if(link == 'returnOfEmptyAmpoules'){
										if (sessionStorage.getItem('bedManagmentCookies') != null) {
											$scope.bedManagmentCookiesObj = JSON.parse(sessionStorage.getItem('bedManagmentCookies'));
											$scope.currentDate = new Date();
											$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
												commonDetailService.setDataIdForBedManagmentPatientIssueAcceptance($scope.bedManagmentCookiesObj.visitTypeId, $scope.bedManagmentCookiesObj.patientId, 
													$scope.bedManagmentCookiesObj.patientName, 
													$scope.bedManagmentCookiesObj.uhidNum, $scope.bedManagmentCookiesObj.wardId,$scope.bedManagmentCookiesObj.wardName,
													 $scope.bedManagmentCookiesObj.floorId,  $scope.bedManagmentCookiesObj.doctorId,  $scope.bedManagmentCookiesObj.doctorFullName);
											}
											$state.go('returnOfEmptyAmpoules');

									 }
									 if(link == 'dischargeMedicationReconciliation'){
										if (sessionStorage.getItem('bedManagmentCookies') != null) {
											$scope.bedManagmentCookiesObj = JSON.parse(sessionStorage.getItem('bedManagmentCookies'));
											$scope.currentDate = new Date();
											$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
												commonDetailService.setDataIdForBedManagmentPatientIssueAcceptance($scope.bedManagmentCookiesObj.visitTypeId, $scope.bedManagmentCookiesObj.patientId, 
													$scope.bedManagmentCookiesObj.patientName, 
													$scope.bedManagmentCookiesObj.uhidNum, $scope.bedManagmentCookiesObj.wardId,$scope.bedManagmentCookiesObj.wardName,
													 $scope.bedManagmentCookiesObj.floorId,  $scope.bedManagmentCookiesObj.doctorId,  $scope.bedManagmentCookiesObj.doctorFullName);
											}
											$state.go('dischargeMedicationReconciliation');

									 }

									  if(link == 'dischargeMedicationHandover'){
										if (sessionStorage.getItem('bedManagmentCookies') != null) {
											$scope.bedManagmentCookiesObj = JSON.parse(sessionStorage.getItem('bedManagmentCookies'));
											$scope.currentDate = new Date();
											$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
												commonDetailService.setDataIdForBedManagmentPatientIssueAcceptance($scope.bedManagmentCookiesObj.visitTypeId, $scope.bedManagmentCookiesObj.patientId, 
													$scope.bedManagmentCookiesObj.patientName, 
													$scope.bedManagmentCookiesObj.uhidNum, $scope.bedManagmentCookiesObj.wardId,$scope.bedManagmentCookiesObj.wardName,
													 $scope.bedManagmentCookiesObj.floorId,  $scope.bedManagmentCookiesObj.doctorId,  $scope.bedManagmentCookiesObj.doctorFullName);
											}
											$state.go('dischargeMedicationHandover');

									 }
									 
								     if(link == 'sendToMortuary')
								    	 {
												$scope.currentDate = new Date();
												$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
												if($scope.bedManagmentCookiesData.isDeath == 'Y')
												{
													var data = {
															admissionId : $scope.bedManagmentCookiesData.admissionId, 
															patientId:$scope.bedManagmentCookiesData.patientId,
															tPatientId:$scope.bedManagmentCookiesData.tPatientId,
															organizationId : $scope.organizationId,
															unitId : $scope.unitId,
															mortuaryStatusId : 1,
															visitTypeId :$scope.bedManagmentCookiesData.visitTypeId,
															createdBy : $scope.userId,
															updatedBy : $scope.userId,
															createdDate:$scope.currentDate,
															updatedDate:$scope.currentDate,
															status :'A',
														    doctorId : $scope.bedManagmentCookiesData.doctorId, 
													}
													
													//console.log(data);
													//return false;
													$rootScope.startLoader();
													var URI = BASE_URL + ROOT_URL
															+ SAVEMORTUARYREQUESTFROMMAPOFBED;
													GenericService
															.serviceAction("POST", URI,data)
															.then(
																	function(response) {
																		$rootScope.stoLoader();
																		if (response.data.status == "success") {
																			growl
																					.success(
																							'Mortuary Request Sent sucessfully...!!',
																							{
																								title : 'Success!'
																							});
																			$scope.init();
																		} else {
																			growl
																					.error(
																							'Something went wrong...!!',
																							{
																								title : 'Error!'
																							});
																		}

																	});
												}else{
													growl
													.warning(
															'Discharge type of patient is not death...!!',
															{
																title : 'Warning!'
															});
												}
												
								    	 }
								     
								    if(link != undefined){
									     if(link == 'pendingTransferRequest'||link == 'acceptanceOfTransferOfCare'  || link == 'initiateBedTransfer.initiateRequestTransferBed' || link == 'transferToOT.OTTransferRequest' || link=='transferForInvestigation.investigationTransferRequest' ||link=='transferRequest.initiateRequestTransferBed' )
									     {
									    	 if ($scope.bedManagmentCookiesData != null) {
													//$scope.bedManagmentCookiesObj = JSON.parse($cookies.get('bedManagmentCookies'));
													
													if($scope.bedManagmentCookiesData.dischargeId!=null)
													{
														growl
														.warning(
																'Transfer cannot be processed as discharge process for selected patient is already initiated..!!',
																{
																	title : 'WARNING!'
																});
													}else{
														 setTimeout(function () {
														     
															 $state.go(link, 
																{
																 	bedManagmentObj:$scope.bedManagmentCookiesData
																});
													      }, 1000);
													}
									    	 }
									     }
									     if(link == 'intiateTransferCare')
									    	 {
									    	 if ($scope.bedManagmentCookiesData != null) {
														//$scope.bedManagmentCookiesObj = JSON.parse($cookies.get('bedManagmentCookies'));
														
														if($scope.bedManagmentCookiesData.dischargeId!=null)
														{
															growl
															.warning(
																	'Transfer cannot be processed as discharge process for selected patient is already initiated..!!',
																	{
																		title : 'WARNING!'
																	});
														}
														else{
															 setTimeout(function () {
															     $state.go(link, 
																	{
																	 	bedManagmentObj:$scope.bedManagmentCookiesData
																	});
														      }, 1000);
														}
										    	 }
									    	 }
									     
									     //Initiate Discharge
									     if(link == 'patientForDischarge' || link == 'readyForBilling' || link == 'finalDischarge' 
									    	 || link == 'dueForDischarge' || link == 'cancelDischarge' )
								    	 {
									    	 //var message=(link == 'patientForDischarge') ?'Marked for Discharge':(link == 'readyForBilling')?'Ready':'';
									    	 if(link == 'dueForDischarge' && $scope.bedManagmentCookiesData.dischargeStatusId==1){
									    		 growl.warning(
															'Due for discharge request completed for selected patient!!!',
															{
																title : 'WARNING!'
															});
									    		 return false;
									    	 }else{
									    		 if(link == 'patientForDischarge' && $scope.bedManagmentCookiesData.dischargeStatusId>1){
									    			 growl.warning(
																'Mark for discharge request completed for selected patient!!!',
																{
																	title : 'WARNING!'
																});
										    		 return false; 
									    		 }else if(link == 'readyForBilling' && $scope.bedManagmentCookiesData.dischargeStatusId>1){
									    			 growl.warning(
																'Ready for billing request completed for selected patient!!!',
																{
																	title : 'WARNING!'
																});
										    		 return false; 
									    		 }else if(link == 'finalDischarge' && $scope.bedManagmentCookiesData.dischargeStatusId>3){
									    			 growl.warning(
																'Final discharge request completed for selected patient!!!',
																{
																	title : 'WARNING!'
																});
										    		 return false;
									    		 }else if(link == 'finalDischarge' && $scope.bedManagmentCookiesData.dischargeStatusId==1){
									    			 growl.warning(
																'Selected patient is not ready for final discharge!!!',
																{
																	title : 'WARNING!'
																});
										    		 return false;
									    		 }else if($scope.bedManagmentCookiesData.dischargeStatusId==null && link != 'dueForDischarge'){
									    			 growl.warning(
																'Discharge process is not initiated for selected patient!!!',
																{
																	title : 'WARNING!'
																});
										    		 return false;
									    			 
									    		 }
									    		 
									    	 }
													if($scope.bedManagmentCookiesData.visitTypeId == 3 && link !='cancelDischarge')
													{
														link=((link == 'patientForDischarge') ? 'patientForDischarge' 
															:(link == 'dueForDischarge') ? 'dueForDischarge'
															:(link == 'readyForBilling') ? 'readyForBilling':'readyForBilling');
													}
													
														 setTimeout(function () {
															 $state.go(link, 
																		{
																		 	bedManagmentObj:$scope.bedManagmentCookiesData
																		});
													      }, 1000);
								    	 }
									     
									     
									     if(link == 'pendingShortLeave'){
									    	 setTimeout(function () {
											     $state.go(link, 
															{
													 	bedManagmentObj:$scope.bedManagmentCookiesData
													});
										      }, 1000);
									     }
									     
									     if(link == 'shortLeaveDischarge'){
									    	 setTimeout(function () {
											     $state.go(link, 
															{
													 	bedManagmentObj:$scope.bedManagmentCookiesData
													});
										      }, 1000);
									     }
									     
									     if(link == 'shortLeaveStatus'){
									    	 setTimeout(function () {
											     $state.go(link, 
															{
													 	bedManagmentObj:$scope.bedManagmentCookiesData
													});
										      }, 1000);
									     }
									     if(link == 'transfer'){
									    	 setTimeout(function () {
											     $state.go(link, 
															{
													 	bedManagmentObj:$scope.bedManagmentCookiesData
													});
										      }, 1000);
									     }
									     /*if(link == 'abscondingPatient'){
									    	 setTimeout(function () {
											     $state.go(link, 
															{
													 	bedManagmentObj:$scope.bedManagmentCookiesData
													});
										      }, 1000);
									     }*/
									     
									     
									     if(link == 'transferToICU' ){
									    	 if($scope.bedManagmentCookiesObj.visitTypeId == 3){
									    		 
									    			growl
													.warning(
															'You cannot transfer day care patient to ICU..!!',
															{
																title : 'WARNING!'
															});
									    			
									    	 }else{
									    	 if (sessionStorage.getItem('bedManagmentCookies') != null) {
													$scope.bedManagmentCookiesObj = JSON.parse(sessionStorage.getItem('bedManagmentCookies'));
													
													if($scope.bedManagmentCookiesObj.dischargeId!=null)
													{
														growl
														.warning(
																'You cannot transfer this patient as discharge process for this patient is already initiated !!!',
																{
																	title : 'WARNING!'
																});
													}else{
														 setTimeout(function () {
															 $state.go(link, 
																		{
																 	bedManagmentObj:$scope.bedManagmentCookiesData
																});
													      }, 1000);
													}
									     }
									    	 }
									     }
								    }
								     else
								    	 {
								    	  setTimeout(function () {
											     $state.go(link);
										      }, 1000);
								    	 }
								   
									}
							}

							
							$scope.loadTreeView = function(){
								
						if($scope.doctorRole==1)
								{
									
									$scope.treeData = new kendo.data.HierarchicalDataSource({ data: [
								          
							            { text: "Doctor", items: [
								              { text: "Transfer", items: [
								            	  { 
								            		  text :"Pending Transf. Req.",link:"pendingTransferRequest"},
								            		  { text:"Accept. Transf. Care",link:"acceptanceOfTransferOfCare"},
								            		  { text:"Initiate Transfer of Care",link:"intiateTransferCare"},
								            		  { text:"Transfer To ICU",link:"transferToICU"},
								            	
								            	  ] },
								            
								              { text: "Discharge", items: [
							            	  	  { text :"Due for Discharge",link:"dueForDischarge"},
							            		  { text:"Cancel Discharge",link:"cancelDischarge"}
							            	  ]},
							            	  { text: "Order" , items: [
							            	  	  { text :"Pending Short Leave",link:"pendingShortLeave"},
							            		  { text:"Blood Request",link:"bloodRequest"},
							            		  { text:"Request Inter Consult",link:"requestInterConsult"},
							            		  { text:"Homecare Medication Approval",link:"homecareMedicationApproval"},
							            		  { text :"Pending Inter Consult",link:"pendingInterConsult"},
							            		  { text:"Crash Cart Administration Approval",link:"administrationApproval"},
							            		  { text:"Second Opinion Request",link:"requestForSecondOpinion"},
							            	
							            	  ]}
								            ] }
								           
							            
							          ]});
									
									
								}
								if($scope.nurseRole==1){
									if($scope.optimizeTransferFlow=='Y'){
										$scope.treeData = new kendo.data.HierarchicalDataSource({ data: [
									          
								            { text: "Nursing", items: [
								              { text: "Transfer", items: [
//								            	  	  { text :"Bed Transfer Request",link:"initiateBedTransfer.initiateRequestTransferBed"},
								            		  { text:"Transfer to OT",link:"transferToOT.OTTransferRequest"},
								            		  { text:"Bed Transfer",link:"transfer"},
								            		 /* { text:"Transfer for Investigation",link:"transferForInvestigation.investigationTransferRequest"},*/
//								            		  { text:"Ward Transfer Request",link:"transferRequest.initiateRequestTransferBed"}
								            	
								            	  ] },
								              { text: "Pharmacy" , items: [
								            	  	  { text :"Top Up Order",link:"patientIndent"},
								            		  { text:"Patient Issue Acceptance",link:"patientIssueAcceptance"},
								            		  { text:"Pending Rejected Rejection",link:"pendingRejectedRejections"},
								            		  { text:"Patient Issue Return",link:"salesReturn"},
								            		  { text :"Billable Consumption",link:"billableConsumption"},
								            		  { text:"Consumable Acceptance",link:"consumableAcceptance"},
								            		  { text:"Medication Reconciliation",link:"dischargeMedicationReconciliation"},
								            		  { text:"Medication Hand Over",link:"dischargeMedicationHandover"},
								            		//   { text :"Homecare Medication Entry",link:"homecareMedicationEntry"},
								            		  { text:"Return of Empty Ampoules",link:"returnOfEmptyAmpoules"}
								            	
								            	  ]},
								              { text: "Discharge", items: [
							            	  	  { text :"Marked for Discharge",link:"patientForDischarge"},
							            		  { text:"Ready for Billing",link:"readyForBilling"},
							            		  { text:"Final Discharge",link:"finalDischarge"},
							            		  { text:"Request Short Leave",link:"shortLeaveDischarge"},
							            		  { text:"Short Leave Status",link:"shortLeaveStatus"},
							            		 /* { text:"Absconding patient",link:"abscondingPatient"},*/
								            	  { text:"Send to mortuary",link:"sendToMortuary"} ]}
								            ] },
								            
								            
								          ]});
									}else{
										$scope.treeData = new kendo.data.HierarchicalDataSource({ data: [
									          
								            { text: "Nursing", items: [
								              { text: "Transfer", items: [
								            	  { 
								            		  text :"Bed Transfer Request",link:"initiateBedTransfer.initiateRequestTransferBed"},
								            		  { text:"Transfer to OT",link:"transferToOT.OTTransferRequest"},
								            		 /* { text:"Transfer for Investigation",link:"transferForInvestigation.investigationTransferRequest"},*/
								            		  { text:"Ward Transfer Request",link:"transferRequest.initiateRequestTransferBed"}
								            	
								            	  ] },
								              { text: "Pharmacy" , items: [
								            	  	  { text :"Top Up Order",link:"patientIndent"},
								            		  { text:"Patient Issue Acceptance",link:"patientIssueAcceptance"},
								            		  { text:"Pending Rejected Rejection",link:"pendingRejectedRejections"},
								            		  { text:"Patient Issue Return",link:"salesReturn"},
								            		  { text :"Billable Consumption",link:"billableConsumption"},
								            		  { text:"Consumable Acceptance",link:"consumableAcceptance"},
								            		  { text:"Medication Reconciliation",link:"dischargeMedicationReconciliation"},
								            		  { text:"Medication Hand Over",link:"dischargeMedicationHandover"},
								            		//   { text :"Homecare Medication Entry",link:"homecareMedicationEntry"},
								            		  { text:"Return of Empty Ampoules",link:"returnOfEmptyAmpoules"}
								            	
								            	  ]},
								              { text: "Discharge", items: [
							            	  	  { text :"Marked for Discharge",link:"patientForDischarge"},
							            		  { text:"Ready for Billing",link:"readyForBilling"},
							            		  { text:"Final Discharge",link:"finalDischarge"},
							            		  { text:"Request Short Leave",link:"shortLeaveDischarge"},
							            		  { text:"Short Leave Status",link:"shortLeaveStatus"},
							            		 /* { text:"Absconding patient",link:"abscondingPatient"},*/
								            	  { text:"Send to mortuary",link:"sendToMortuary"} ]}
								            ] },
								            
								            
								          ]});
									}
									
									
									
									
								}
								if($scope.roleName=='admin'){
									
									if($scope.optimizeTransferFlow=='Y'){
										$scope.treeData = new kendo.data.HierarchicalDataSource({ data: [
									          
								            { text: "Nursing", items: [
								              { text: "Transfer", items: [
//								            	      {text :"Bed Transfer Request",link:"initiateBedTransfer.initiateRequestTransferBed"},
								            		  { text:"Transfer to OT",link:"transferToOT.OTTransferRequest"},
								            		  { text:"Bed Transfer",link:"transfer"},
//								            		  { text:"Ward Transfer Request",link:"transferRequest.initiateRequestTransferBed"}
								            	 
								            	  ] },
								              { text: "Pharmacy" , items: [
								            	  	  { text :"Top Up Order",link:"patientIndent"},
								            		  { text:"Patient Issue Acceptance",link:"patientIssueAcceptance"},
								            		  { text:"Pending Rejected Rejection",link:"pendingRejectedRejections"},
								            		  { text:"Patient Issue Return",link:"salesReturn"},
								            		  { text :"Billable Consumption",link:"billableConsumption"},
								            		  { text:"Consumable Acceptance",link:"consumableAcceptance"},
								            		  { text:"Medication Reconciliation",link:"dischargeMedicationReconciliation"},
								            		  { text:"Medication Hand Over",link:"dischargeMedicationHandover"},
								            		//   { text :"Homecare Medication Entry",link:"homecareMedicationEntry"},
								            		  { text:"Return of Empty Ampoules",link:"returnOfEmptyAmpoules"}
								            	
								            	  ]},
								              { text: "Discharge", items: [
							            	  	  { text :"Marked for Discharge",link:"patientForDischarge"},
							            		  { text:"Ready for Billing",link:"readyForBilling"},
							            		  { text:"Final Discharge",link:"finalDischarge"},
							            		  { text:"Request Short Leave",link:"shortLeaveDischarge"},
							            		  { text:"Short Leave Status",link:"shortLeaveStatus"},
							            		 /* { text:"Absconding patient",link:"abscondingPatient"},*/
								            	  { text:"Send to mortuary",link:"sendToMortuary"} ]}
								            ] },
								            { text: "Doctor", items: [
									              { text: "Transfer", items: [
									            	  { 
									            		  text :"Pending Transf. Req.",link:"pendingTransferRequest"},
									            		  { text:"Accept. Transf. Care",link:"acceptanceOfTransferOfCare"},
									            		  { text:"Initiate Transfer of Care",link:"intiateTransferCare"},
									            		  { text:"Transfer To ICU",link:"transferToICU"},
									            	
									            	  ] },
									            
									              { text: "Discharge", items: [
								            	  	  { text :"Due for Discharge",link:"dueForDischarge"},
								            		  { text:"Cancel Discharge",link:"cancelForDischarge"}
								            	  ]},
								            	  { text: "Order" , items: [
								            	  	  { text :"Pending Short Leave",link:"pendingShortLeave"},
								            		  { text:"Blood Request",link:"bloodRequest"},
								            		  { text:"Request Inter Consult",link:"requestInterConsult"},
								            		  { text:"Homecare Medication Approval",link:"homecareMedicationApproval"},
								            		  { text :"Pending Inter Consult",link:"pendingInterConsult"},
								            		  { text:"Crash Cart Administration Approval",link:"administrationApproval"},
								            		  { text:"Second Opinion Request",link:"requestForSecondOpinion"},
								            	
								            	  ]}
									            ] },
									            { text: "Billing", items: [
									            	      { text :"Deposit",link:"deposit"},
									            		  { text:"Refund",link:"refund"},
									            		  { text:"IPD Charges",link:"IPDCharges.serviceDetailsForIPDCharge"},
									            		  { text:"IPD Billing",link:"IPDBilling"},
									            		  { text :"Intermediate Bill",link:"intermediateBill"},
									            		  { text:"Debit Note List",link:"debitNoteList"},
									            		  { text:"Credit Note",link:"creditNoteList"},
									            		  { text:"Bill Estimation",link:"billEstimation"},
									            	
									            	  ] },
							            
							            
							          ]});
								}else{
									$scope.treeData = new kendo.data.HierarchicalDataSource({ data: [
								          
							            { text: "Nursing", items: [
							              { text: "Transfer", items: [
							            	  { 
							            		  text :"Bed Transfer Request",link:"initiateBedTransfer.initiateRequestTransferBed"},
							            		  { text:"Transfer to OT",link:"transferToOT.OTTransferRequest"},
//							            		  { text:"Bed Transfer",link:"transfer"},
							            		  { text:"Ward Transfer Request",link:"transferRequest.initiateRequestTransferBed"}
							            	 
							            	  ] },
							              { text: "Pharmacy" , items: [
							            	  	  { text :"Top Up Order",link:"patientIndent"},
							            		  { text:"Patient Issue Acceptance",link:"patientIssueAcceptance"},
							            		  { text:"Pending Rejected Rejection",link:"pendingRejectedRejections"},
							            		  { text:"Patient Issue Return",link:"salesReturn"},
							            		  { text :"Billable Consumption",link:"billableConsumption"},
							            		  { text:"Consumable Acceptance",link:"consumableAcceptance"},
							            		  { text:"Medication Reconciliation",link:"dischargeMedicationReconciliation"},
							            		  { text:"Medication Hand Over",link:"dischargeMedicationHandover"},
							            		//   { text :"Homecare Medication Entry",link:"homecareMedicationEntry"},
							            		  { text:"Return of Empty Ampoules",link:"returnOfEmptyAmpoules"}
							            	
							            	  ]},
							              { text: "Discharge", items: [
						            	  	  { text :"Marked for Discharge",link:"patientForDischarge"},
						            		  { text:"Ready for Billing",link:"readyForBilling"},
						            		  { text:"Final Discharge",link:"finalDischarge"},
						            		  { text:"Request Short Leave",link:"shortLeaveDischarge"},
						            		  { text:"Short Leave Status",link:"shortLeaveStatus"},
						            		 /* { text:"Absconding patient",link:"abscondingPatient"},*/
							            	  { text:"Send to mortuary",link:"sendToMortuary"} ]}
							            ] },
							            { text: "Doctor", items: [
								              { text: "Transfer", items: [
								            	  { 
								            		  text :"Pending Transf. Req.",link:"pendingTransferRequest"},
								            		  { text:"Accept. Transf. Care",link:"acceptanceOfTransferOfCare"},
								            		  { text:"Initiate Transfer of Care",link:"intiateTransferCare"},
								            		  { text:"Transfer To ICU",link:"transferToICU"},
								            	
								            	  ] },
								            
								              { text: "Discharge", items: [
							            	  	  { text :"Due for Discharge",link:"dueForDischarge"},
							            		  { text:"Cancel Discharge",link:"cancelForDischarge"}
							            	  ]},
							            	  { text: "Order" , items: [
							            	  	  { text :"Pending Short Leave",link:"pendingShortLeave"},
							            		  { text:"Blood Request",link:"bloodRequest"},
							            		  { text:"Request Inter Consult",link:"requestInterConsult"},
							            		  { text:"Homecare Medication Approval",link:"homecareMedicationApproval"},
							            		  { text :"Pending Inter Consult",link:"pendingInterConsult"},
							            		  { text:"Crash Cart Administration Approval",link:"administrationApproval"},
							            		  { text:"Second Opinion Request",link:"requestForSecondOpinion"},
							            	
							            	  ]}
								            ] },
								            { text: "Billing", items: [
								            	      { text :"Deposit",link:"deposit"},
								            		  { text:"Refund",link:"refund"},
								            		  { text:"IPD Charges",link:"IPDCharges.serviceDetailsForIPDCharge"},
								            		  { text:"IPD Billing",link:"IPDBilling"},
								            		  { text :"Intermediate Bill",link:"intermediateBill"},
								            		  { text:"Debit Note List",link:"debitNoteList"},
								            		  { text:"Credit Note",link:"creditNoteList"},
								            		  { text:"Bill Estimation",link:"billEstimation"},
								            	
								            	  ] },
						            
						            
						          ]});
										}
									
								}
						}
						$scope.loadTreeView();
							
							
							/* Tree Structure */
							 

						         
							 	$scope.tab = 1 ;
								
								$scope.setTab = function(newTab)
								{
									$scope.tab = newTab;
								};
								
								$scope.isSet = function(tabNum)
								{
									return $scope.tab === tabNum;
								};
 
 /***************************************************/
  /******* START TAB FOR ALL PAGES IN TREE LEVEL *******/
/*First Level*/
$scope.firstTabLevel = 1 ;

$scope.setFirstTabLevel= function(newTab)
{
	$scope.firstTabLevel = newTab;
};

$scope.isSetFirstTabLevel = function(tabNum)
{
	return $scope.firstTabLevel === tabNum;
};

/*Second Level*/
$scope.secondTabLevel = 1 ;

$scope.setSecondTabLevel = function(newTab)
{
	$scope.secondTabLevel = newTab;
};

$scope.isSetSecondTabLevel = function(tabNum)
{
	return $scope.secondTabLevel === tabNum;
};

/*Third Level*/
$scope.thirdTabLevel = 1 ;

$scope.setThirdTabLevel = function(newTab)
{
	$scope.thirdTabLevel = newTab;
};

$scope.isSetThirdTabLevel = function(tabNum)
{
	return $scope.thirdTabLevel === tabNum;
};
/*FORTH Level*/
$scope.forthTabLevel = 1 ;

$scope.setForthTabLevel = function(newTab)
{
	$scope.forthTabLevel = newTab;
};

$scope.isSetForthTabLevel = function(tabNum)
{
	return $scope.forthTabLevel === tabNum;
};





/******* END TAB FOR ALL PAGES IN TREE LEVEL *******/
/****************************************************/


/** IP Sample Collection**/
$scope.generateSampleCollection = function(mapList)
{
	
	if(mapList!=null)
	  {
					$state.go('sampleCollection',
						{
						 obj : mapList,
						});
	  }
else
   {
	    growl.error("Something went Wrong.",{title : 'Error!'});
   }		
 	
}
/** IP Sample Collection**/
$scope.drugAdminstrator = function (mapList) {
	commonDetailService.setDataIdForBedManagment(mapList.admissionId, mapList.uhidnumber);
	$state.go('drugAdministrator');
}

$scope.allergy= function(mapList){
	
	if(mapList!=null)
	  {
					$state.go('allergies',
						{
						 obj : mapList,
						});
	  }
else
 {
	    growl.error("please select patient",{title : 'Error!'});
 }		
}


$scope.gotoBabyRegistration=function(){
	$state.go('newBornRegistration', {
		admissionObjForBaby: $scope.commonObjForTrans
	});
}

$scope.transferToNormalBed=function(){
	$rootScope.startLoader();
	$scope.currentDate = new Date();
	$scope.currentDate = moment(
			$scope.currentDate).format(
			'DD-MM-YYYY HH:mm:ss');
	var data = {
		organizationId : $scope.organizationId,
		unitId : $scope.unitId,
		admissionId : $scope.commonObjForTrans.admissionId,
		fromWardId : $scope.commonObjForTrans.wardId,
		fromBedId :$scope.commonObjForTrans.bedId,
		//fromRoomId :admissionObj.fromRoomId,
		createdBy : $scope.userId,
		createdDate : $scope.currentDate,
		updatedBy : $scope.userId,
		updatedDate : $scope.currentDate
	
	}
	var URI = BASE_URL + ROOT_URL
	+ "/adt/transferFromLaborBedToNormalBed";
GenericService
	.serviceAction("POST", URI,
			data)
	.then(
			function(response) {
				if (response.data.status == "success") {
					growl
							.success(
									'Patient Transferred...!!',
									{
										title : 'Success!'
									});
					//$scope.init();
					$state.reload();
					 $rootScope.stopLoader();
				} else if (response.data.status == "error")
				{
					growl
							.error(
									'Something went wrong...!!',
									{
										title : 'Error!'
									});
					 $rootScope.stopLoader();
				}

			});
}

$scope.commonObjForTrans={};
$scope.transferMessage="";
$scope.showLaborTransferPopup=function(admissionObj){
	$scope.commonObjForTrans = admissionObj;
	if($scope.commonObjForTrans.isLaborWard=='Y'){
		 $scope.transferMessage="Do you want to  transfer back from labor ward ?";
	}else{
	    $scope.transferMessage="Do you want to initiate transfer request to labor ward ?";
   }
	$('#laborTransferReqPopup').modal();
}

$scope.processLaborTransferRequest=function(){
	//$('#laborTransferReqPopup').hide();
	 $('#closeTrasferPopup').trigger('click');
	$scope.obgynTransferRequest();
}

$scope.obgynTransferRequest=function(){
	    if($scope.commonObjForTrans.isLaborWard=='Y'){
	    	// angular.element('#babyRegistrationPopup').modal('show'); 
	    	$rootScope.startLoader();
	    	$scope.currentDate = new Date();
	    	$scope.currentDate = moment(
	    			$scope.currentDate).format(
	    			'DD-MM-YYYY HH:mm:ss');
	    	var data = {
	    		organizationId : $scope.organizationId,
	    		unitId : $scope.unitId,
	    		admissionId : $scope.commonObjForTrans.admissionId,
	    		fromWardId : $scope.commonObjForTrans.wardId,
	    		fromBedId :$scope.commonObjForTrans.bedId,
	    		//fromRoomId :admissionObj.fromRoomId,
	    		createdBy : $scope.userId,
	    		createdDate : $scope.currentDate,
	    		updatedBy : $scope.userId,
	    		updatedDate : $scope.currentDate
	    	
	    	}
	    	var URI = BASE_URL + ROOT_URL
	    	+ "/adt/transferFromLaborBedToNormalBed";
	    GenericService
	    	.serviceAction("POST", URI,
	    			data)
	    	.then(
	    			function(response) {
	    				if (response.data.status == "success") {
	    					growl
	    							.success(
	    									'Patient Transferred...!!',
	    									{
	    										title : 'Success!'
	    									});
	    					//$scope.init();
	    					$state.reload();
	    					 $rootScope.stopLoader();
	    				} else if (response.data.status == "error")
	    				{
	    					growl
	    							.error(
	    									'Something went wrong...!!',
	    									{
	    										title : 'Error!'
	    									});
	    					 $rootScope.stopLoader();
	    				}

	    			});
	    	
	    }else{
	    	
			$rootScope.startLoader();
			$scope.currentDate = new Date();
			$scope.currentDate = moment(
					$scope.currentDate).format(
					'DD-MM-YYYY HH:mm:ss');
			var data = {
				treatingDoctorId :  $scope.commonObjForTrans.doctorId,
				organizationId : $scope.organizationId,
				unitId : $scope.unitId,
				admissionId :  $scope.commonObjForTrans.admissionId,
				fromWardId :  $scope.commonObjForTrans.wardId,
				authorizedBy : $scope.userId,
				fromBedCategoryId :  $scope.commonObjForTrans.bedCategoryId,
				createdBy : $scope.userId,
				createdDate : $scope.currentDate,
				updatedBy : $scope.userId,
				updatedDate : $scope.currentDate,
				transferTypeId : 6,
				transferStatusId : 4
				
			}
			
			var data2={
					organizationId : $scope.organizationId,
					unitId : $scope.unitId,
					admissionId :  $scope.commonObjForTrans.admissionId
			}
			var URI = BASE_URL + ROOT_URL
			+ "/adt/checkTransferRequestExistence";
			GenericService
			.serviceAction("POST", URI,
					data2)
			.then(
					function(response) {
			if (response.data.status == "success") {
				growl.warning(
						'Transfer request already exist...!!',
						{
							title : 'Warning'
						});
				 $rootScope.stopLoader();
				 return false;
			}else{
			var URI1 = BASE_URL + ROOT_URL
					+ "/adt/saveLaborTransferRequest";
			GenericService
					.serviceAction("POST", URI1,
							data)
					.then(
							function(response) {

								if (response.data.status == "success") {
									growl
											.success(
													'Transfer Request sent Successfully...!!',
													{
														title : 'Success!'
													});
									//$scope.init();
									 $rootScope.stopLoader();
								} else if (response.data.status == "error")
								{
									growl
											.error(
													'Something went wrong...!!',
													{
														title : 'Error!'
													});
									 $rootScope.stopLoader();
								}

							});
			         }
				});
	      }
	  

		}
/*<-------------------HEMODIALYSIS START---------------->*/
$scope.hemodialysisTransferRequest= function(admissionObj){
	
	 if(admissionObj.ishemodialysisward=='Y'){
	    	$scope.commonObjForTrans = admissionObj;
	    	// angular.element('#babyRegistrationPopup').modal('show'); 
	    	$rootScope.startLoader();
	    	$scope.currentDate = new Date();
	    	$scope.currentDate = moment(
	    			$scope.currentDate).format(
	    			'DD-MM-YYYY HH:mm:ss');
	    	var data = {
	    		organizationId : $scope.organizationId,
	    		unitId : $scope.unitId,
	    		admissionId : $scope.commonObjForTrans.admissionId,
	    		fromWardId : $scope.commonObjForTrans.wardId,
	    		fromBedId :$scope.commonObjForTrans.bedId,
	    		//fromRoomId :admissionObj.fromRoomId,
	    		createdBy : $scope.userId,
	    		createdDate : $scope.currentDate,
	    		updatedBy : $scope.userId,
	    		updatedDate : $scope.currentDate,
	    		isTransferReqFromHemodialysis :'Y',
	    		doctorId : $scope.commonObjForTrans.doctorId,
	    		specialityId :$scope.commonObjForTrans.specialityId,
	    		visitTypeId :2
	    	
	    	}
	    	var URI = BASE_URL + ROOT_URL
	    	+ "/adt/transferFromLaborBedToNormalBed";
	    GenericService
	    	.serviceAction("POST", URI,
	    			data)
	    	.then(
	    			function(response) {
	    				if (response.data.status == "success") {
	    					growl
	    							.success(
	    									'Patient Transferred...!!',
	    									{
	    										title : 'Success!'
	    									});
	    					//$scope.init();
	    					$state.reload();
	    					 $rootScope.stopLoader();
	    				} else if (response.data.status == "error")
	    				{
	    					growl
	    							.error(
	    									'Something went wrong...!!',
	    									{
	    										title : 'Error!'
	    									});
	    					 $rootScope.stopLoader();
	    				}

	    			});
	    	
	    }else{
	    	
			$rootScope.startLoader();
			$scope.currentDate = new Date();
			$scope.currentDate = moment(
					$scope.currentDate).format(
					'DD-MM-YYYY HH:mm:ss');
			var data = {
				treatingDoctorId : admissionObj.doctorId,
				organizationId : $scope.organizationId,
				unitId : $scope.unitId,
				admissionId : admissionObj.admissionId,
				fromWardId : admissionObj.wardId,
				authorizedBy : $scope.userId,
				fromBedCategoryId : admissionObj.bedCategoryId,
				createdBy : $scope.userId,
				createdDate : $scope.currentDate,
				updatedBy : $scope.userId,
				updatedDate : $scope.currentDate,
				transferTypeId : 7,
				transferStatusId : 4
				
			}
			
			var data2={
					organizationId : $scope.organizationId,
					unitId : $scope.unitId,
					admissionId : admissionObj.admissionId
			}
			var URI = BASE_URL + ROOT_URL
			+ "/adt/checkTransferRequestExistence";
			GenericService
			.serviceAction("POST", URI,
					data2)
			.then(
					function(response) {
			if (response.data.status == "success") {
				growl.warning(
						'Transfer request already exist...!!',
						{
							title : 'Warning'
						});
				 $rootScope.stopLoader();
				 return false;
			}else{
			var URI1 = BASE_URL + ROOT_URL
					+ "/adt/saveLaborTransferRequest";
			GenericService
					.serviceAction("POST", URI1,
							data)
					.then(
							function(response) {

								if (response.data.status == "success") {
									growl
											.success(
													'Transfer Request sent Successfully...!!',
													{
														title : 'Success!'
													});
									//$scope.init();
									 $rootScope.stopLoader();
								} else if (response.data.status == "error")
								{
									growl
											.error(
													'Something went wrong...!!',
													{
														title : 'Error!'
													});
									 $rootScope.stopLoader();
								}

							});
			         }
				});
	      }
	  

		}

/*<-------------------HEMODIALYSIS END---------------->*/
$scope.getOrderedProcedureList=function(mapList){
	
	if(!angular.equals({},$stateParams.obj))
	{
		$scope.admissionObj =  mapList;
        $scope.patientDetails.patientId= $scope.admissionObj.patientId;
    }

	var URI = BASE_URL + ROOT_URL
	+ "/adt/getListOfOrderedProcedure/"+mapList.patientId+"/"+$scope.organizationId+"/"+$scope.unitId;
	GenericService
	.serviceAction(METHOD_GET, URI)
	.then(
			function(response) {
				//console.log(response);
				if (response.data.status == "success")
					$scope.orderedProcedureList = response.data.listObject;
				//console.log("$scope.orderedProcedureList", $scope.orderedProcedureList);
			});
	}
$scope.addCollection = function(mapList)
{

	if(mapList!=null)
	  {
					$state.go('bedManagement.MEWSnew',
						{
						 obj : mapList,
						});
					
					if(!angular.equals({},$stateParams.obj))
					{
					$scope.admissionObj = mapList,
					$scope.patientDetails.patientName = $scope.admissionObj.pFirstName;
					$scope.patientDetails.genderCode= ($scope.admissionObj.genderId==1)?"M":($scope.admissionObj.genderId==2?"F":($scope.admissionObj.genderId==3?"OTR":"-"));
//					$scope.patientDetails.age= moment().diff($scope.admissionObj.dob, 'years');
//					$scope.patientDetails.uhIdNumber= $scope.admissionObj.uhidnumber;
					$scope.patientDetails.uhidNumber= $scope.admissionObj.uhidnumber;
					$scope.patientDetails.dob= $scope.admissionObj.dob;
					$scope.patientDetails.doctorFullName= $scope.admissionObj.dFirstName;
					$scope.patientDetails.dateOfAdmission= $scope.admissionObj.doa;
					$scope.patientDetails.patientDischargeDate= $scope.admissionObj.pdd;
					$scope.patientDetails.wardName= $scope.admissionObj.wardName;
					$scope.patientDetails.bedNumber= $scope.admissionObj.bedNumber;
					$scope.patientDetails.imagePath= "";
					$scope.patientDetails.patientId= $scope.admissionObj.patientId;
					$scope.patientDetails.visitTypeId= $scope.admissionObj.visitTypeId;
					$scope.patientDetails.admissionId= $scope.admissionObj.admissionId;
					$scope.patientDetails.genderId= $scope.admissionObj.genderId;
					$scope.getAllPatientDiagnosis( $scope.admissionObj.patientId);
					$rootScope.allergydata($scope.admissionObj);
					 $scope.getMewsList();
				}
	  }
else
   {
	    growl.error("Something went Wrong.",{title : 'Error!'});
   }		
 	
}



$scope.addCollection1 = function(mapList)
{

	if(mapList!=null)
	  {
					
					
					if(!angular.equals({},$stateParams.obj1))
					{
					$scope.admissionObj = mapList,
					$scope.patientDetails.patientName = $scope.admissionObj.pFirstName;
					$scope.patientDetails.genderCode= ($scope.admissionObj.genderId==1)?"M":($scope.admissionObj.genderId==2?"F":($scope.admissionObj.genderId==3?"OTR":"-"));
//					$scope.patientDetails.age= moment().diff($scope.admissionObj.dob, 'years');
//					$scope.patientDetails.uhIdNumber= $scope.admissionObj.uhidnumber;
					$scope.patientDetails.uhidNumber= $scope.admissionObj.uhidnumber;
					$scope.patientDetails.dob= $scope.admissionObj.dob;
					$scope.patientDetails.doctorFullName= $scope.admissionObj.dFirstName;
					$scope.patientDetails.dateOfAdmission= $scope.admissionObj.doa;
					$scope.patientDetails.patientDischargeDate= $scope.admissionObj.pdd;
					$scope.patientDetails.wardName= $scope.admissionObj.wardName;
					$scope.patientDetails.bedNumber= $scope.admissionObj.bedNumber;
					$scope.patientDetails.imagePath= "";
					$scope.patientDetails.patientId= $scope.admissionObj.patientId;
					$scope.patientDetails.visitTypeId= $scope.admissionObj.visitTypeId;
					$scope.patientDetails.admissionId= $scope.admissionObj.admissionId;
					$scope.patientDetails.genderId= $scope.admissionObj.genderId;
					$scope.getAllPatientDiagnosis( $scope.admissionObj.patientId);
					 $scope.getMewsList();
					$rootScope.allergydata($scope.admissionObj);
					$state.go('bedManagement.MEWShistory',
							{
						obj1 : mapList,
							});
				
				}
					
	  }
else
   {
	    growl.error("Something went Wrong.",{title : 'Error!'});
   }		
 	
}

$scope.getMewsList = function()
 {
	 $scope.mewsList = [];
		
		var URI = BASE_URL + ROOT_URL + "/adt/getMewsMapperList/"+$scope.patientDetails.patientId+"/"+$scope.organizationId+"/"+$scope.unitId
		GenericService.serviceAction(METHOD_GET, URI)
		.then(
			function(response) {
				if (response.data.status == "success") {
					$scope.mewsList = response.data.listObject;
					
					console.log("mewsList", $scope.mewsList);

				}
			});
 }

 $scope.addCollection2 = function(mapList)
 {

 	if(mapList!=null)
 	  {


 					if(!angular.equals({},$stateParams.obj1))
 					{
 					$scope.admissionObj = mapList,
 					$scope.patientDetails.patientName = $scope.admissionObj.pFirstName;
 					$scope.patientDetails.genderCode= ($scope.admissionObj.genderId==1)?"M":($scope.admissionObj.genderId==2?"F":($scope.admissionObj.genderId==3?"OTR":"-"));
 //					$scope.patientDetails.age= moment().diff($scope.admissionObj.dob, 'years');
 //					$scope.patientDetails.uhIdNumber= $scope.admissionObj.uhidnumber;
 					$scope.patientDetails.uhidNumber= $scope.admissionObj.uhidnumber;
 					$scope.patientDetails.dob= $scope.admissionObj.dob;
 					$scope.patientDetails.doctorFullName= $scope.admissionObj.dFirstName;
 					$scope.patientDetails.dateOfAdmission= $scope.admissionObj.doa;
 					$scope.patientDetails.patientDischargeDate= $scope.admissionObj.pdd;
 					$scope.patientDetails.wardName= $scope.admissionObj.wardName;
 					$scope.patientDetails.bedNumber= $scope.admissionObj.bedNumber;
 					$scope.patientDetails.imagePath= "";
 					$scope.patientDetails.patientId= $scope.admissionObj.patientId;
 					$scope.patientDetails.visitTypeId= $scope.admissionObj.visitTypeId;
 					$scope.patientDetails.admissionId= $scope.admissionObj.admissionId;
 					$scope.patientDetails.genderId= $scope.admissionObj.genderId;
 					$scope.getAllPatientDiagnosis( $scope.admissionObj.patientId);
 					 // $scope.getMewsList();
 					$rootScope.allergydata($scope.admissionObj);
 					$state.go('nursingScale',
 							{
 						obj : mapList,
 							});

 				}

 	  }
 else
    {
 	    growl.error("Something went Wrong.",{title : 'Error!'});
    }

 }


// $scope.nursing=function(){
// $state.go('nursingScaleScoreController');
// }


$scope.sum = 0;
$scope.systolic='';
$scope.heart='';
$scope.respiratory='';
$scope.selectedTemperature='';
$scope.avpu='';

$scope.commnadditionSys=function (systolic,oldValue){	
	$scope.systolic=systolic;
	
	if(oldValue!=""){						
		 $scope.sum -= parseInt(oldValue);
	}						
	 $scope.sum += systolic;
	 
	 console.log("sum", $scope.sum);
}
$scope.commnadditionHeart=function (heart,oldValue){					
	$scope.heart=heart;
	
	if(oldValue!=""){						
		 $scope.sum -= parseInt(oldValue);
	}						
	 $scope.sum += heart;
	 
	 console.log("sum", $scope.sum);
}

 $scope.commnadditionrespiratory=function (respiratory,oldValue){					
	 $scope.respiratory=respiratory;
		
	if(oldValue!=""){						
		 $scope.sum -= parseInt(oldValue);
	}						
	 $scope.sum += respiratory;
	 
	 console.log("sum", $scope.sum);
}

 $scope.commnadditiontemperature=function (selectedTemperature,oldValue){					
	 $scope.selectedTemperature=selectedTemperature;
		
		if(oldValue!=""){						
			 $scope.sum -= parseInt(oldValue);
		}						
		 $scope.sum += selectedTemperature;
		 
		 console.log("sum", $scope.sum);
	}
 $scope.commnadditionavpu=function (avpu,oldValue){					
	 $scope.avpu=avpu;
		if(oldValue!=""){						
			 $scope.sum -= parseInt(oldValue);
		}						
		 $scope.sum += avpu;
		 
		 console.log("sum", $scope.sum);
	}
 
	 
	/* var data={};
	var URI = BASE_URL + ROOT_URL 
	+ "/adt/getCountOnMewsMapper/"+$scope.patientDetails.patientId+"/"+$scope.organizationId+"/"+$scope.unitId ;
GenericService.serviceAction("GET", URI,data).then(function(response) {
	
		if (response.data.status == 'success') {
			$scope.finalscoreList=response.data.listObject;
			console.log("score", $scope.finalscoreList);
		}
	});*/

 
 $scope.save=function(){
	 var data ={
	 totalScore:$scope.sum,
	 systolicBloodPressureId:$scope.systolic,
	 heartRateId : $scope.heart,
	 temperatureId : $scope.selectedTemperature,
	 avpuScoreId : $scope.avpu,
	 respiratoryRateId : $scope.respiratory,
	 organizationId : $scope.organizationId,
	 unitId : $scope.unitId,
	 createdBy : $scope.userId,
	 updatedBy : $scope.userId,
	 status    : 'A',
	 createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
	 updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
	 patientId : $scope.patientDetails.patientId,
	 admissionId : $scope.patientDetails.admissionId
}
	  

	  if(data != null){

		  
		  if(($scope.systolic !='' || parseInt($scope.systolic) == 0)&&
				  ($scope.heart !='' || parseInt($scope.heart)== 0) &&
				  ($scope.respiratory != '' || parseInt($scope.respiratory)== 0) &&
				  ($scope.selectedTemperature !='' || parseInt($scope.selectedTemperature)== 0) &&
				  ($scope.avpu !='' || parseInt($scope.avpu)== 0)){
	  var URI = BASE_URL + ROOT_URL+ "/adt/saveMewsMapper";
	  GenericService.serviceAction("POST", URI,data).then(function(response) {

			if (response.data.status == 'success') {
				growl.success(response.data.message,
						{
							title : 'Success!'
						});
				if($scope.patientDetails.visitTypeId == 4 ){
					$state.reload('mapOfBedForER');
				}else{
					
					$state.reload('bedManagement');
				}
			
				return;

			    } else if (response.data.status == 'error') {
				growl.error(response.data.message,
						{
							title : 'Error!'
						});
			   }
		});
		  }else{
			  growl.error("Please Select MEWS",
						{
							title : 'Error!'
						});
		  }

	  }
	  else  {
		  growl.error("Please Select MEWS",
					{
						title : 'Error!'
					});
	  }
 }
 
 $scope.hidetabDetails=function(info){
	var visible= $("#bedlistshow_"+info.wardId).is(":hidden"); 
	 if(visible==true){
		 $("#bedlistshow_"+info.wardId).removeClass("hidden");
	 }else{
		 $("#bedlistshow_"+info.wardId).addClass("hidden");
	 }
//	 {{info.wardName}}
 }

	 
$scope.comorbidityFlag = false;
$scope.notifiableFlag = false;
$scope.getAllPatientDiagnosis=function(patientId){
	 var headerObjDia = {
			 	'unit_id' : $scope.unitId,
				'organization_id' : $scope.organizationId,
				'patient_id' : patientId,
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
                 	$scope.comorbidityFlag = false;
                 	$scope.notifiableFlag = false;
					$scope.billing=true;
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
							}else{
							 	$scope.comorbidityFlag = false;
			                 	$scope.notifiableFlag = false;
							}
                 
             });
	}

		$scope.mapBedDetails=function(admissionId){
			$scope.payeeDetailList= [];
			var data = {
					"admissionId" : admissionId,
					}
		
					var URI = BASE_URL + ROOT_URL + "/api/adt/getPaymentEntitlmentbyAdmissionId";
					BillingGenericService.serviceAction("POST",URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if (response.data.status == "success")
					{
					if(response.data.listObject != null ){
		
						$scope.payeeDetailsList=response.data.listObject;
						$("#Details").modal("show");
		
					}else{
					growl.warning("pancard is not uploaded",
					{
					title : 'Warning!'
					});
					}
		
					}
		
					});
		
		}

		$scope.getSurgeriesOfTheDay = function(){
			$scope.surgeriesOfTheDayList = []; 
			try {
				$scope.startLoader();
				var data = {
						"createdDate":moment(new Date()).format('YYYY-MM-DD'),
						"organizationId" : $scope.organizationId,
						"unitId" : $scope.unitId
					};
				var URI = BASE_URL + ROOT_URL+"/api/otims/dashboard/getSurgeriesOfTheDayList";
				console.log("URI", URI);
				GenericService
						.serviceAction("POST", URI, data)
						.then(
								function(response) {
									$scope.surgeriesOfTheDayList = response.data.listObject;
									$rootScope.stopLoader();
									
									console.log("$scope.surgeriesOfTheDayList",$scope.surgeriesOfTheDayList);
								});
			} catch (e) {
				console.log(e.message)
				$scope.stopLoader();
			}	
		}
		$scope.getSurgeriesOfTheDay();
		
		$scope.goToPreOperative = function(test){
			
			if($scope.surgeriesOfTheDayList != null && $scope.surgeriesOfTheDayList.length > 0){
				 var info = {};
				 var checkRecord = false;
				 angular.forEach($scope.surgeriesOfTheDayList,function(value,key){
					if(test.admissionId == value.admissionId){
						checkRecord = true;
						info = value;
					}
				 });

				 
					$("#orderModal").hide();
					$('.modal-backdrop').remove();
					$('body').removeClass("modal-open");
					if(checkRecord != false){
						info.mapOfBed = 2;
						   $state.go('OTchecklistDetails',
								{
							       checklistDetailsObj: info
								});
					}else{
						growl.warning("Procedure not configured for todays date..!!!",
								{
								title : 'Warning!'
								});
						return false;
					}
				
			}else{
				growl.warning("Procedure not configured for todays date..!!!",
						{
						title : 'Warning!'
						});
				return false;
			}
			
		}

        $scope.getICUMonitorData = function(patient){
            $scope.monitorDataList = [];
            try {

                angular.element('#icuMonitorDataModal').modal('show');
                $scope.startLoader();
                var data = {
                        "createdDate":moment(new Date()).format('YYYY-MM-DD'),
                        "organizationId" : $scope.organizationId,
                        "unitId" : $scope.unitId,
                        "admissionId" : patient.admissionId
                };
                var URI = BASE_URL + ROOT_URL+"/api/adt/bed/getIcuMonitorData";
    //                var URI = "http://localhost:8090/HIS-WAR"+"/api/otims/dashboard/getSurgeriesOfTheDayList";
                console.log("URI", URI);
                GenericService
                    .serviceAction("POST", URI, data)
                    .then(
                    function(response) {
                        $scope.monitorDataList = response.data.listObject;
                        $rootScope.stopLoader();

                        console.log("$scope.monitorDataList",$scope.monitorDataList);
                    });
            } catch (e) {
                angular.element('#icuMonitorDataModal').modal('hide');
                $(".modal-backdrop").hide();
                console.log(e.message)
                $scope.stopLoader();
            }
        }

} ]);
