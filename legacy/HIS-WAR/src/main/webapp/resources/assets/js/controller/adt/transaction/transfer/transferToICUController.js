'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:transferToICUController
 * @description #transferToICUController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'transferToICUController',
				[
						'$scope',
						'$http',
						'$localStorage',
						'$sessionStorage',
						'$cookies',
						'$rootScope',
						'GenericService',
						'growl',
						'$state',
						'$stateParams',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state,$stateParams) {
							
								$scope.disableFlagTC = true;
							  var isAlreadyExists;
								function checkAlreadyExistsOrNot(arr,temp) {
									return arr
											.some(function(arr) {
												if(parseInt(temp.id) === parseInt(arr.transferStatusId))
													{
														return true;
													}
												else
													{
														return false;
													}
											});
								}
						    $scope.icuFlag=false;
							$scope.reasonFlag=false;
							$scope.noteFlag=true;
							 
							 $scope.dateOptions = {							 
								      formatYear: 'yyyy',
								      showWeeks: false,
								      maxDate:new Date()
								  };
								 $scope.openDatePicker = function() {
									// alert("in");
								      $scope.datepicker.opened = true;
								    };
								    $scope.datepicker = {
										      opened: false
										    };

								    $scope.getSexMasterList = function() {
								    	var data = {
								    			organizationId : $scope.organizationId
										};
								    	$scope.sexMasterList=[];
										var URI = BASE_URL + ROOT_URL + GETACTIVEGENDERLIST;
										GenericService.serviceAction("GET", URI, data).then(function(response) {
											$scope.sexMasterList = response.data.listObject;
											console.log($rootScope.sexMasterList);
										});
									}
								    $scope.getSexMasterList();
									
								    $scope.getIdentificationTypeList = function(){
										var data = {
												organizationId : $scope.organizationId
											};
										$scope.identificationTypeList=[];
										var URI = BASE_URL + ROOT_URL + GETACTIVEIDENTIFICATION;
										GenericService.serviceAction("GET", URI, data).then(function(response) {
											if (response.data.status == "success")
												$scope.identificationTypeList = response.data.listObject;
											console.log($rootScope.identificationTypeList);
										});
									}
									$scope.getIdentificationTypeList();
									
							/* init() function for form object create. */
							$scope.init = function() {
								
								  var cookieObject = $cookies
									.getObject('cookieObject');
							if (cookieObject == undefined) {
								$state.go('login');
								return;
							}
							
							 	var cookieObjectAppConfig = $cookies
							.getObject('cookieObjectAppConfig');
							if (cookieObjectAppConfig == undefined) {
								$state.go('login');
								return;
							} else {
								console.log('cookieObjectAppConfig',
										cookieObjectAppConfig);

							}
							
					
							$scope.isRMO=cookieObject.isRMO;
							if($scope.isRMO==undefined){
								$scope.isRMO='N';
							}
							$scope.showTrasnferReqBtn=false;
							$scope.admissionId = '';
							var rmoDoctorOP=JSON.parse(localStorage.getItem('rmoMappedOPDoctorList'));
							var rmoDoctorIP=JSON.parse(localStorage.getItem('rmoMappedIPWard'));
							 rmoDoctorOP= rmoDoctorOP != undefined ? rmoDoctorOP : [];
							 rmoDoctorIP= rmoDoctorIP != undefined ? rmoDoctorIP : [];
							 
							console.log("rmoDoctorOP"+rmoDoctorOP)
							console.log("rmoDoctorIP"+$scope.rmoDoctorIP)
							$scope.rmoVisitType=$cookies.getObject('rmoVisit');
							 $scope.rmoMappedOpDoctorList=[];
							var rmoMappedWard=null;
							if($scope.isRMO=='Y' && (rmoDoctorOP==null || rmoDoctorIP==null)){
								 growl.warning(
										 'RMO roster is not defined !!',
											{
												title : 'Warning!'
											});
								return false;
							}
							else{
								 $scope.rmoMappedOpDoctorList= rmoDoctorOP!=undefined ? rmoDoctorOP :[];
								rmoMappedWard= $scope.rmoDoctorIP!=undefined ? $scope.rmoDoctorIP: null;
							}
						   
                                $scope.billing = false;
                                //$scope.comorbidityFlag = false;

							/*	var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}*/
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.cookieDoctorId = cookieObject.doctorId;
								
								
								if(!angular.equals({},$stateParams.bedManagmentObj))
								{
									$scope.bedManagmentCookiesObj=$stateParams.bedManagmentObj;
								}
								
								
								if ($scope.bedManagmentCookiesObj != null) 
								{
									//$scope.bedManagmentCookiesObj = JSON.parse($cookies.get('bedManagmentCookies'));
									//$cookies.remove('bedManagmentCookies');
									//console.log($scope.bedManagmentCookiesObj);
									$scope.icuFlag=true;
									$scope.reasonFlag=true;
									$scope.noteFlag=false;
									
									$scope.patientDetails = {};
									$scope.patientDetails.uhidNumber=$scope.bedManagmentCookiesObj.uhidNum;
									$scope.patientDetails.patientName =$scope.bedManagmentCookiesObj.patientName;
									$scope.patientDetails.genderCode = $scope.bedManagmentCookiesObj.genderCode;
									$scope.patientDetails.genderId = $scope.bedManagmentCookiesObj.genderId;
									$scope.patientDetails.age = $scope.bedManagmentCookiesObj.age;
									$scope.patientDetails.birthDate =  $scope.bedManagmentCookiesObj.dob;
									$scope.patientDetails.dob =  $scope.bedManagmentCookiesObj.dob;
									$scope.patientFullName = $scope.bedManagmentCookiesObj.patientFullName;
								    $scope.uhidNum = $scope.bedManagmentCookiesObj.uhidNum;
									$scope.patientDetails.dateOfAdmission = $scope.bedManagmentCookiesObj.doa;
									$scope.patientDetails.patientDischargeDate = $scope.bedManagmentCookiesObj.pdd;
									$scope.bedNumber = $scope.bedManagmentCookiesObj.bedNumber;
									$scope.patientDetails.bedNumber = $scope.bedManagmentCookiesObj.bedNumber;
									$scope.patientDetails.doctorFullName = $scope.bedManagmentCookiesObj.doctorFullName;
									$scope.wardName = $scope.bedManagmentCookiesObj.wardName;
									$scope.patientDetails.wardName = $scope.bedManagmentCookiesObj.wardName;
									$scope.admissionId  =  $scope.bedManagmentCookiesObj.admissionId;
									$scope.patientDetails.admissionId = $scope.bedManagmentCookiesObj.admissionId;
									$scope.patientDetails.doctorId = $scope.bedManagmentCookiesObj.doctorId;
									$scope.doctorId = $scope.bedManagmentCookiesObj.doctorId;
									$scope.patientDetails.wardId = $scope.bedManagmentCookiesObj.wardId;
									$scope.patientDetails.admissionId = $scope.bedManagmentCookiesObj.admissionId;
									$scope.patientDetails.bedCategoryId = $scope.bedManagmentCookiesObj.bedCategoryId;
									$scope.patientDetails.billingBedCategoryId = $scope.bedManagmentCookiesObj.billingBedCategoryId;
									$scope.patientDetails.floorId = $scope.bedManagmentCookiesObj.floorId;
									$scope.patientDetails.bedId = $scope.bedManagmentCookiesObj.bedId;
									$scope.patientDetails.roomId = $scope.bedManagmentCookiesObj.roomId;
									$scope.patientDetails.doctorId = $scope.bedManagmentCookiesObj.doctorId;
									console.log("$scope.admissionId"+$scope.admissionId)
									console.log("$scope.bedManagmentCookiesObj.admissionId"+$scope.bedManagmentCookiesObj.admissionId)
									var commonObjTransfer = {
											unitId : $scope.unitId,
											organizationId : $scope.organizationId,
											doctorId : $scope.patientDetails.doctorId,
											admissionId:$scope.admissionId
											
										};
									
									
									var URI = BASE_URL + ROOT_URL
									+ GETICUTRANSFERPATIENTLIST;
							GenericService
									.serviceAction("POST", URI, commonObjTransfer)
									.then(
											function(response) {
												$scope.transferICUList = [];

												if (response.data.status == "success") {
													$scope.transferICUList = response.data.listObject;
													
													var temp = {id :10};
													isAlreadyExists=checkAlreadyExistsOrNot($scope.transferICUList,temp)
													if (isAlreadyExists) {
														$scope.disableFlagTC = true;
														$scope.icuFlag=false;
														$scope.reasonFlag=false;
														$scope.noteFlag=true;
														growl
														.warning(
																'Transfer Of ICU request for selected patient is already raised...!!!',
																{
																	title : 'Warning'
																});
													}
													else
													{
													   $scope.disableFlagTC = false;
													}

												}
											});
									
									
								  } else {
									  $scope.disableFlagTC = false;
									 /* var commonObjTransfer = {
												unitId : $scope.unitId,
												organizationId : $scope.organizationId,
												doctorId : $scope.patientDetails.doctorId==undefined ? $scope.cookieDoctorId : $scope.patientDetails.doctorId
											};
										
										
										var URI = BASE_URL + ROOT_URL
										+ GETICUTRANSFERPATIENTLIST;
								GenericService
										.serviceAction("POST", URI, commonObjTransfer)
										.then(
												function(response) {
													$scope.transferICUList = [];

													if (response.data.status == "success") {
														$scope.transferICUList = response.data.listObject;

													}
												});*/
								$scope.patientList=[];
								$scope.patientDetails ={};
									//$scope.bedManagmentCookiesObj = null;
								}
								
									    
							

								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								$scope.itemSelected = {};
								$scope.patientSelecteds = {};
								$scope.beditemSelecteds ={};
					


								var data = {
										organizationId : $scope.organizationId,
										unitId:$scope.unitId
								};
								
								
								
						
					//	var URI = BASE_URL + ROOT_URL+ GETACTIVEUNITICUTYPELIST;
								var URI = BASE_URL + ROOT_URL+'/adt/getActiveICUTypeList';	
				GenericService
						.serviceAction("POST", URI, data)
						.then(
								function(response) {
									$scope.icuTypeList = [];

									if (response.data.status == "success") {
										$scope.icuTypeList = response.data.listObject;

									}
								});
				
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
	

						
		
		
							}
							
							
						
							$scope.init();
							
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveIcuTransferRequest();
							}
							
							
							$scope.getDoctorByOpenClose = function()
							{
								console.log($scope.icuTypeId)
								$scope.doctorListForIcu = [];
								$scope.itemSelected =[];
								$scope.itemSelected = JSON.parse($scope.icuTypeId);
								if($scope.itemSelected.isCloseICU == 'Y')
									{
									
									var data = {
											organizationId : $scope.organizationId,
											unitId:$scope.unitId
									};
									
									var URI = BASE_URL + ROOT_URL
									+ GETDOCTORFORICU;
							GenericService
									.serviceAction("POST", URI, data)
									.then(
											function(response) {
												

												if (response.data.status == "success") {
													$scope.doctorListForIcu = response.data.listObject;

												}
											});
							
									
									}
								else
									{
									
										var data = {doctorId:$scope.patientSelecteds.doctorId,
												firstName:$scope.patientSelecteds.dFirstName};
										    
												$scope.doctorListForIcu.push(data);
										}
									
									
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
										+ GETADMITTEDPATIENTBYKEWWORD;
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
							
							$scope.selectPatientId = function(info, model,
									label) {
								
								console.log(info);
								$scope.patientSelecteds = [];
								$scope.patientSelecteds = info;
								console.log($scope.patientSelecteds);
								
								if($scope.patientSelecteds.patientId > 0)
									{
									 $scope.age = $rootScope.getAge($scope.patientSelecteds.dob);
									}
								else
									{
									$scope.age = $scope.patientSelecteds.dob;
									}
								$scope.patientFullName =$scope.patientSelecteds.patientName+' '+$scope.age+'/'+$scope.patientSelecteds.genderName;
								
								$scope.UHIDNumber = $scope.patientSelecteds.uhidnumber;

								
							}
							$scope.initget = function()
							{
							var commonObjTransfer = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId,
									doctorId : $scope.patientDetails.doctorId==undefined ? $scope.cookieDoctorId : $scope.patientDetails.doctorId,
									admissionId:$scope.admissionId
									
								};
							console.log(commonObjTransfer+"AAA"+$scope.admissionId)
							
							var URI = BASE_URL + ROOT_URL
							+ GETICUTRANSFERPATIENTLIST;
					GenericService
							.serviceAction("POST", URI, commonObjTransfer)
							.then(
									function(response) {
										$scope.transferICUList = [];

										if (response.data.status == "success") {
											$scope.transferICUList = response.data.listObject;

										}
									});
							}
							$scope.saveIcuTransferRequest = function()
							{
								console.log($scope.patientSelecteds);
								if ($scope.admissionId=="" || $scope.admissionId == null || $scope.admissionId == undefined) {
									growl
											.warning(
													'Please select patient...!!',
													{
														title : 'Warning!'
													});
								}else if ($scope.itemSelected.icutypeMasterId=="" || $scope.itemSelected.icutypeMasterId == null || $scope.itemSelected.icutypeMasterId == undefined) {
									growl
									.warning(
											'Please select ICU type...!!',
											{
												title : 'Warning!'
											});
								}else if ($scope.reasonId=="" || $scope.reasonId == null || $scope.reasonId == undefined || $scope.reasonId == "Select Reason") {
									growl
									.warning(
											'Please select reason...!!',
											{
												title : 'Warning!'
											});
								} else {
								if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;

								} else {
									
								$rootScope.startLoader();
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								
								//console.log("hngh"+$scope.icuTypeId[0].icuTypeId);
								var data = {
										//treatingDoctorId : $scope.icuDoctorId,
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										admissionId : $scope.admissionId,
										fromWardId : $scope.patientDetails.wardId,
										reasonId : $scope.reasonId,
										note : $scope.note,
										authorizedBy : $scope.userId,
										fromBedCategoryId :$scope.patientDetails.bedCategoryId,//ask to milind sir
										createdBy : $scope.userId,
										createdDate : $scope.currentDate,
										updatedBy : $scope.userId,
										updatedDate : $scope.currentDate,
										transferTypeId : 3,
										fromBillingBedCategoryId : $scope.patientDetails.billingBedCategoryId,//ask to milind sir
										fromFloorId:$scope.patientDetails.floorId,
										//transferStatusId:10,//for Send to ADT
										icuTypeId:$scope.itemSelected.icutypeMasterId,
										fromBedId:$scope.patientDetails.bedId,
										fromRoomId : $scope.patientDetails.roomId,
										doctorId : $scope.patientDetails.doctorId 
								
										
									};
								if($scope.admissionId=="" ||$scope.admissionId==null){
									data.admissionId=$scope.bedManagmentCookiesObj.admissionId;
								}
								
								console.log(data)
								//var array =$scope.icuTypeId.split('_');
								if($scope.itemSelected.isCloseICU == 'Y')
								{
									//data.transferStatusId = 3;
									data.transferStatusId = 10;
								}
							else
								{
									data.transferStatusId = 10;
									
									}
								console.log(data);
								//return false;
								var URI = BASE_URL + ROOT_URL
										+ SAVEICUREQUEST;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Transfer to ICU request raised successfully...!!',
																		{
																			title : 'Success!'
																		});
//														$scope.init();
														$scope.reasonId = '';
														$scope.note = '';
														$scope.icuTypeId = {};
														$scope.itemSelected = {};
														var commonObjTransfer = {
																unitId : $scope.unitId,
																organizationId : $scope.organizationId,
																doctorId : $scope.patientDetails.doctorId==undefined ? $scope.cookieDoctorId : $scope.patientDetails.doctorId,
																admissionId:$scope.admissionId
																
															};
														console.log(commonObjTransfer+"AAA"+$scope.admissionId)
														
														var URI = BASE_URL + ROOT_URL
														+ GETICUTRANSFERPATIENTLIST;
												GenericService
														.serviceAction("POST", URI, commonObjTransfer)
														.then(
																function(response) {
																	$scope.transferICUList = [];

																	if (response.data.status == "success") {
																		$scope.transferICUList = response.data.listObject;
																		var temp = {id :10};
																		isAlreadyExists=checkAlreadyExistsOrNot($scope.transferOfCareRequestList,temp)
																		if (isAlreadyExists) {
																			$scope.disableFlagTC = true;
																			$scope.icuFlag=false;
																			$scope.reasonFlag=false;
																			$scope.noteFlag=true;
																		}
																		else
																			{
																			   $scope.disableFlagTC = false;
																			}

																	}
																});
														
														$rootScope.stopLoader();
														
														$scope.icuFlag=false;
														$scope.reasonFlag=false;
														$scope.noteFlag=true;
													} else {
														$rootScope.stopLoader();
														growl
																.error(
																		'Something went wrong...!!',
																		{
																			title : 'Error!'
																		});
														$scope.icuFlag=false;
														$scope.reasonFlag=false;
														$scope.noteFlag=true;
													}
												});

								  
								}
							}
						}
							$scope.displayNote=function(note)
							{
								$scope.noteDoctor = note;
								
							}
						
						//Search patient
							$scope.patientList=[];
							$scope.searchPatientTransfer = function(){
								$rootScope.startLoader();
								//var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
								var patientsearchObj=$scope.patientSearchObj;
								patientsearchObj.organizationId =$scope.organizationId;
								patientsearchObj.unitId =$scope.unitId;
								patientsearchObj.visitTypeId =2;
								patientsearchObj.doctorId=$scope.cookieDoctorId;
								patientsearchObj.wardList =[];
								patientsearchObj.bedList =[];
								
								if(patientsearchObj.uhIdNumber=="" && patientsearchObj.patientName=="" 
									&& patientsearchObj.genderId=="" && patientsearchObj.mobileNo=="" 
									&& patientsearchObj.birthDate=="" && patientsearchObj.identificationNo=="" )
									{
										growl.warning("Please enter atleast one search criteria..!",{title : "WARNING!"});
										$scope.patientList = [];
										$rootScope.stopLoader();
										return false;
									}
									if(patientsearchObj.patientName!="" && patientsearchObj.patientName.length < 3)
									{
										growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
										$scope.patientList = [];
										$rootScope.stopLoader();
										return false;
									}
								
								if($scope.rmoDoctorIP!=undefined || $scope.rmoDoctorIP!=null){
									patientsearchObj.wardList.push($scope.rmoDoctorIP)
								}
								
								var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByForTransferICU";
								
								if(patientsearchObj.birthDate != "" && typeof(patientsearchObj.birthDate) != "undefined"){
									patientsearchObj.birthDate = moment(patientsearchObj.birthDate).format('YYYY-MM-DD');
								}
								
								GenericService.serviceAction("POST",URI, patientsearchObj).then(function(response){
									$scope.patientList = response.data.listObject;
									$rootScope.stopLoader();
								});
							}		
							
						
							$scope.getPatientDetailsById = function(patientId){
								$rootScope.startLoader();
								var index = $scope.patientList.map(function (obj) { return obj.patientId; }).indexOf(patientId);
								//$scope.initPatientSelected();
								$scope.icuFlag=true;
								$scope.reasonFlag=true;
								$scope.noteFlag=false;
								
								$scope.patientDetails ={};
								$scope.admissionDetails = {};
								$scope.patientDetails.uhidNumber=($scope.patientList[index].uhIdNumber);
								$scope.patientDetails.patientName = ($scope.patientList[index].patientName);
								$scope.patientDetails.genderCode = ($scope.patientList[index].genderId);
								$scope.patientDetails.age = ($scope.patientList[index].age);
								$scope.patientDetails.birthDate = ($scope.patientList[index].birthDate);
								$scope.patientDetails.dob = ($scope.patientList[index].birthDate);
								//$scope.patientDetails.dateOfAdmission = ($scope.patientList[index].doa);
								//$scope.patientDetails.patientDischargeDate = ($scope.patientList[index].pdd);
								//$scope.patientDetails.wardName = ($scope.patientList[index].wardNo);
								//$scope.patientDetails.bedNo = ($scope.patientList[index].bedNumber);
								
							    $scope.uhidNum = ($scope.patientList[index].uhIdNumber);
								$scope.patientDetails.patientId=($scope.patientList[index].patientId);
								$scope.patientFullName=($scope.patientList[index].patientName)+ " "+$scope.patientList[index].age	+ "/"+ $scope.patientList[index].genderCode;
								$scope.admissionId = ($scope.patientList[index].admissionId);
								$scope.visitTypeId = $scope.patientList[index].visitTypeId;
								
								//$scope.icuFlag=true;
								//get admission details by id
								if($scope.admissionId != null && typeof($scope.admissionId) != "undefined")
								{
								 var URI = BASE_URL + ROOT_URL + "/api/ipd/admissions/patient/"+$scope.admissionId;
									GenericService.serviceAction("GET",URI, null, {}).then(function(response){
										if(response.data.status == "success"){
											$scope.admissionDetails = response.data.object;
											$rootScope.stopLoader();
											if($scope.admissionDetails.dischargeStatusId!=null){
												$scope.showTrasnferReqBtn=true;
												growl
												.warning(
														'You cannot transfer this patient as discharge process for this patient is already initiated !!!',
														{
															title : 'WARNING!'
														});
											}else{
												$scope.showTrasnferReqBtn=false;
											}
											$scope.patientDetails.dateOfAdmission = $scope.admissionDetails.doa;
											$scope.patientDetails.patientDischargeDate = $scope.admissionDetails.pdd;
											$scope.bedNumber = $scope.admissionDetails.bedNo;
											$scope.patientDetails.bedNumber = $scope.admissionDetails.bedNo;
											//$scope.patientDetails.doctorFullName = $scope.admissionDetails.doctorFullName;
											$scope.wardName = $scope.admissionDetails.wardNo;
											$scope.patientDetails.wardName = $scope.admissionDetails.wardNo;
											$scope.admissionId  =  $scope.admissionDetails.admissionId;
											$scope.patientDetails.admissionId = $scope.admissionDetails.admissionId;
											$scope.patientDetails.doctorId = $scope.admissionDetails.doctorId;
											$scope.doctorId = $scope.admissionDetails.doctorId;
											$scope.patientDetails.wardId = $scope.admissionDetails.wardId;
											$scope.patientDetails.bedCategoryId = $scope.admissionDetails.bedCategoryId;
											$scope.patientDetails.billingBedCategoryId = $scope.admissionDetails.bedBillingCategoryId;
											$scope.patientDetails.floorId = $scope.admissionDetails.floorId;
											$scope.patientDetails.bedId = $scope.admissionDetails.bedId;
											$scope.patientDetails.roomId = $scope.admissionDetails.roomId;
											$scope.patientDetails.doctorFullName = $scope.admissionDetails.doctorName;
											sessionStorage.setItem('bedManagmentCookies', null)
										}
									});
								}
								var commonObjTransfer = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										doctorId : $scope.patientDetails.doctorId==undefined ? $scope.cookieDoctorId : $scope.patientDetails.doctorId,
										admissionId:$scope.admissionId
										
									};
								console.log(commonObjTransfer+"AAA"+$scope.admissionId)
								
								var URI = BASE_URL + ROOT_URL
								+ GETICUTRANSFERPATIENTLIST;
						GenericService
								.serviceAction("POST", URI, commonObjTransfer)
								.then(
										function(response) {
											$scope.transferICUList = [];

											if (response.data.status == "success") {
												$scope.transferICUList = response.data.listObject;
												var temp = {id :10};
												isAlreadyExists=checkAlreadyExistsOrNot($scope.transferICUList,temp)
												if (isAlreadyExists) {
													$scope.disableFlagTC = true;
													    $scope.icuFlag=false;
														$scope.reasonFlag=false;
														$scope.noteFlag=true;
												}
												else
													{
													   $scope.disableFlagTC = false;
													}
												
											}
										});
							}

						} ]);
