'use strict';

/**
 * @Author By Vivek Satle
 * @name myApp.controller:initiateBedTransferController
 * @description #initiateBedTransferController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'initiateBedTransferController',
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
						'PagerService',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state,$stateParams,PagerService) {
							
							/*if($cookies.get('bedManagmentCookies') != null)
							{
								$scope.bedManagmentCookiesObj =JSON.parse($cookies.get('bedManagmentCookies'));
								$cookies.remove('bedManagmentCookies');
							}
							else
								{
								$scope.bedManagmentCookiesObj =null;
								}*/
							
							
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							
							
							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
							 if(cookieObjectAppConfig == undefined){
							  $state.go('login');
							  return;
							 }
							 else
							  {
							   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
							   
							  }
							 
							$scope.roleName = cookieObjectAppConfig.roleName;
							$scope.nurseRole = cookieObjectAppConfig.nurseRole;	 
							$scope.doctorRole = cookieObjectAppConfig.doctorRole;
							$scope.isShowPatientByBed=cookieObjectAppConfig.isShowPatientByBed;
							$scope.skipHousekeeping = cookieObjectAppConfig.skipHousekeeping;
							$scope.skipTransferAcceptance =cookieObjectAppConfig. skipTransferAcceptance
							/** Variables for pagination */
							$scope.initiateReqOffset = 0;
							$scope.initiateReqPerPageRecords = 10;
							$scope.listOfAdmittedPatientInitiatList  = [];
							$scope.initiateReqTotalRecords;
							$scope.initiateReqSearchKeyword;
							$scope.initiateReqListCount;
							
							$scope.physicalTransferOffset = 0;
							$scope.physicalTransferPerPageRecords = 10;
							$scope.listOfAdmittedPatientList  = [];
							$scope.physicalTransferTotalRecords;
							$scope.physicalTransferSearchKeyword;
							$scope.physicalTransferListCount;
						 
							
							$scope.wardList = [];
							var nursingStationsWardsList = JSON.parse(sessionStorage.getItem('nursingStationsWardsList'));
							 if(nursingStationsWardsList == undefined){
								 growl.warning(
											'No ward mapped with selected nursing station !!',
											{
												title : 'Success!'
											});
							 }
							 else
							  {
							   console.log('nursingStationsWardsList',nursingStationsWardsList);
							   $scope.wardList = nursingStationsWardsList; 
							   /*console.log('nursingStationCookie',nursingStationCookie);
							   $scope.nursingStationDesc=nursingStationCookie;
							   $scope.departmentNursingStationMapperDtoslist = specialtyNursingStationCookies.departmentNursingStationlist;
								console.log("departmentNursingStationMapperDtoslist",$scope.departmentNursingStationMapperDtoslist);*/
							  }
							
							 $scope.nurseBedList = [];
								var mappedNurseBedList = JSON.parse(sessionStorage.getItem('mappedNurseBedList'));
								 if( (mappedNurseBedList == undefined || mappedNurseBedList.length==0) && $scope.isShowPatientByBed=='Y' ){
									 growl.warning(
												'No bed mapped with nurse !!',
												{
													title : 'Success!'
												});
								 }
								 else
								  {
								   $scope.nurseBedList =mappedNurseBedList; 
								  console.log('Nurse Bed List',$scope.nurseBedList);
								  }
							
							
									var commonObjInitiat = {
											unitId : $scope.unitId,
											organizationId : $scope.organizationId,
											wardList : $scope.wardList
										};
									
									
									var commonObjTransfer = {
											unitId : $scope.unitId,
											organizationId : $scope.organizationId,
											transferTypeId : 2,
											wardList : $scope.wardList
										};
									$scope.searchData = function(keyword)
									{
										$scope.searchFilter=keyword;
										$scope.initInitiateRequestList($scope.organizationId,
												$scope.unitId,$scope.initiateReqOffset,
												$scope.initiateReqPerPageRecords,commonObjInitiat);
									}
									$scope.searchPhysicalData = function(keyword){
                                       $scope.searchPhysicalFilter=keyword;
                                       $scope.initPhysicalTransfertList($scope.organizationId,
                                           $scope.unitId,$scope.physicalTransferOffset,
                                           $scope.physicalTransferPerPageRecords,commonObjTransfer);
                                    }

								 $scope.initInitiateRequestList = function(orgId,
											orgUnitId, offset, noOfRecordsPerPage,obj) {
										try {
											if(obj==undefined || obj==null)
												{
												obj=commonObjInitiat;
												}
											$rootScope.startLoader();
											$scope.initiateReqListCount =0;
											$scope.listOfAdmittedPatientInitiatList = [];
											$scope.admittedPatientInitiatList = [];
											var commonObjInitiatForBM  = {};
											var URI  = "";
										
											if(obj == undefined){
											 commonObjInitiatForBM = {
													unitId : $scope.unitId,
													offset:offset!=undefined && offset!=null ? offset :0,
													noOfRecordsPerPage:noOfRecordsPerPage!=undefined && noOfRecordsPerPage!=null ? noOfRecordsPerPage :100,
													organizationId:$scope.organizationId,
													admissionId:$scope.bedManagmentCookiesObj.admissionId,
													};
											 URI = BASE_URL + ROOT_URL + GETINITIATETRANSFERREQUESTFORBM;
											}else{
												obj.offset = offset!=undefined && offset!=null ? offset :0;
												obj.noOfRecordsPerPage = noOfRecordsPerPage!=undefined && noOfRecordsPerPage!=null ? noOfRecordsPerPage :100;
												if(obj.admission!=undefined){
													 URI = BASE_URL + ROOT_URL + GETINITIATETRANSFERREQUESTFORBM;
												}else{
													 URI = BASE_URL + ROOT_URL +GETINITIATETRANSFERREQUEST;
												}
												commonObjInitiatForBM = obj;
											}
											commonObjInitiatForBM.searchKeyword=$scope.searchFilter;
											GenericService.serviceAction("POST", URI, commonObjInitiatForBM).then(
													function(response) {
																$rootScope.stopLoader();
																$scope.admittedPatientInitiatList  = response.data.listObject;
																angular.forEach($scope.admittedPatientInitiatList,function(value,index) {

																	var newArr = {};
																	newArr.patientFullName = value.pFirstName;
																	newArr.dob = value.dob;
																	newArr.genderCode = value.genderCode;
																	newArr.uhidnumber = value.uhidnumber;
																	newArr.patientId = value.patientId;
																	newArr.tPatientId = value.tPatientId;
																	newArr.bedNumber = value.bedNumber;
																	newArr.admissionId = value.admissionId;
																	newArr.transferId = value.transferId;
																	newArr.hierarchyId = value.hierarchyId;
																	newArr.prefixCode = value.prefixCode;
																	newArr.bedCategoryId = value.bedCategoryId;
																	newArr.bedCategoryDesc = value.bedCategoryDesc;
																	newArr.doctorId = value.doctorId;
																	newArr.admissionDetailsId = value.admissionDetailsId;
																	newArr.transferStatusDesc = value.transferStatusDesc;
																	newArr.wardId = value.wardId;
																	newArr.wardName = value.wardName;
																	newArr.transferStatusId = value.transferStatusId;
																	newArr.reasonDesc = value.reasonDesc;
																	newArr.ageStr = value.ageStr;
																	newArr.prefixDesc = value.prefixDesc;
																	newArr.imagePath = value.imagePath;
																	newArr.selectedPatient = true;
																	
																	if(commonObjInitiatForBM.admissionId == undefined){
																		if(value.visitTypeId != 4){				
																			$scope.listOfAdmittedPatientInitiatList.push(newArr);
																			}
																		}else{
																			$scope.listOfAdmittedPatientInitiatList.push(newArr);
																		}
																	});
																$scope.initiateReqListCount = response.data.object;
																$scope.setInitiateReqPage(1,false);
															});
										} catch (e) {
											console.log(e.message);
										}
									}
								 
								 
								 $scope.initPhysicalTransfertList = function(orgId,
											orgUnitId, offset, noOfRecordsPerPage,obj) {
										try {
											$rootScope.startLoader();
											$scope.physicalTransferListCount =0;
											$scope.listOfAdmittedPatientList = [];
											$scope.admittedPatientList = [];
											var commonObjTransferBM  = {};
											var URI  = "";
											offset = offset != null ? offset : 0;
											noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
													: 10;
										
											if(obj == undefined){
											commonObjTransferBM = {
													unitId : $scope.unitId,
													organizationId : $scope.organizationId,
													admissionId:$scope.bedManagmentCookiesObj.admissionId,
													offset:offset,
													noOfRecordsPerPage:noOfRecordsPerPage
											};
											
											URI = BASE_URL + ROOT_URL + GETTRANSFERREQUESTLISTBYADMISSIONIDFORB2B;
											
											}else{
												obj.offset = offset;
												obj.noOfRecordsPerPage = noOfRecordsPerPage;
												if(obj.admission!=undefined){
													URI = BASE_URL + ROOT_URL + GETTRANSFERREQUESTLISTBYADMISSIONIDFORB2B;
												}else{
													URI = BASE_URL + ROOT_URL+ GETTRANSFERPATIENTLIST;
												}
												commonObjTransferBM = obj;
											}
                                            commonObjTransferBM.searchKeyword=$scope.searchPhysicalFilter;
											GenericService.serviceAction("POST", URI, commonObjTransferBM).then(
													function(response) {
															$rootScope.stopLoader();
																$scope.admittedPatientList  = response.data.listObject;
																angular.forEach($scope.admittedPatientList,function(value,index) {

																	var newArr = {};
																	newArr.patientFullName = value.pFirstName;
																	newArr.dob = value.dob;
																	newArr.genderCode = value.genderCode;
																	newArr.uhidnumber = value.uhidnumber;
																	newArr.patientId = value.patientId;
																	newArr.tPatientId = value.tPatientId;
																	newArr.bedNumber = value.bedNumber;
																	newArr.admissionId = value.admissionId;
																	newArr.transferId = value.transferId;
																	newArr.hierarchyId = value.hierarchyId;
																	newArr.prefixCode = value.prefixCode;
																	newArr.bedCategoryId = value.bedCategoryId;
																	newArr.bedCategoryDesc = value.bedCategoryDesc;
																	newArr.doctorId = value.doctorId;
																	newArr.admissionDetailsId = value.admissionDetailsId;
																	newArr.transferStatusDesc = value.transferStatusDesc;
																	newArr.wardId = value.wardId;
																	newArr.wardName = value.wardName;
																	newArr.transferStatusId = value.transferStatusId;
																	newArr.toBedCategoryDesc = value.toBedCategoryDesc;
																	newArr.toBedNumber = value.toBedNumber;
																	newArr.toWardName = value.toWardname;
																	newArr.rejectReasonDesc = value.rejectReasonDesc;
																	newArr.reasonDesc = value.reasonDesc;
																	newArr.adtReasonDesc = value.adtReasonDesc;
																	newArr.adtRejectReason =  value.adtRejectReason;
																	newArr.doctorReasonDesc =  value.doctorReasonDesc;
																	newArr.doctorRejectReason =  value.doctorRejectReason;
																	newArr.transferRequestId =  value.transferRequestId;
																	newArr.toBedId = value.toBedId;
																	newArr.toWardId = value.toWardId;
																	newArr.toFloorId = value.toFloorId;
																	newArr.toRoomId = value.toRoomId;
																	newArr.fromBedId = value.fromBedId;
																	newArr.toBedCategoryId = value.toBedCategoryId;
																	newArr.toBillingBedCategoryId = value.billingBedCategoryId;
																	newArr.transferTypeId = value.transferTypeId;
																	newArr.remark = value.note;
																	newArr.ageStr = value.ageStr;
																	newArr.prefixDesc = value.prefixDesc;
																	newArr.imagePath = value.imagePath;
																	newArr.fromFloorId = value.fromFloorId;
																	if(commonObjTransferBM.admissionId!=undefined){
																		$scope.listOfAdmittedPatientList.push(newArr);
																	}else{
																		newArr.fromBillingBedCategoryId  = value.fromBillingBedCategoryId;
																		newArr.fromWardId = value.fromWardId;
																		newArr.fromRoomId = value.fromRoomId;
																		$scope.listOfAdmittedPatientList.push(newArr);
																	}
																});
																$scope.physicalTransferListCount = response.data.object;
																$scope.setPhysicalTransferPage(1,false);
															});
										} catch (e) {
											console.log(e.message);
										}
									}
							
								 $scope.initiateReqSetNoOfRecords = function(noOfRecords) {
										$scope.initiateReqPerPageRecords = noOfRecords;
									 if($scope.bedManagmentCookiesObj != null)
										{
											$scope.initInitiateRequestList($scope.organizationId,
													$scope.unitId,$scope.initiateReqOffset,
													$scope.initiateReqPerPageRecords);
										}else{
											if($scope.nurseRole == 1 && $scope.wardList.length > 0){
												
												$scope.initInitiateRequestList($scope.organizationId,
														$scope.unitId,$scope.initiateReqOffset,
														$scope.initiateReqPerPageRecords,commonObjInitiat);
											}
										}
								 }
									$scope.physicalTransferSetNoOfRecords = function(noOfRecord) {
										$scope.physicalTransferPerPageRecords = noOfRecord;
										if($scope.bedManagmentCookiesObj != null)
										{
												$scope.initPhysicalTransfertList($scope.organizationId,
														$scope.unitId,$scope.physicalTransferOffset,
														$scope.physicalTransferPerPageRecords);
										}else{
											if($scope.nurseRole == 1 && $scope.wardList.length > 0){
												
													$scope.initPhysicalTransfertList($scope.organizationId,
															$scope.unitId,$scope.physicalTransferOffset,
															$scope.physicalTransferPerPageRecords,commonObjTransfer);
												}
										}
									};	
							/* init() function for form object create. */
							$scope.init = function() {
								
								if(!angular.equals({},$stateParams.bedManagmentObj))
								{
									$scope.bedManagmentCookiesObj=$stateParams.bedManagmentObj;
								}
								
								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup

								$scope.admittedPatient = {};
								$scope.itemSelecteds = {};
								$rootScope.itemSelectedsAccept = {};

								var data = {};
								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								
								if ($scope.isShowPatientByBed == 'Y') {
									if($scope.nurseBedList.length>0){
										commonObjInitiat.bedList=$scope.nurseBedList;
										commonObjTransfer.bedList=$scope.nurseBedList;
									}else{
										$scope.wardList=[];	
									}
								}

								
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEREASONLIST;
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.reasonList = [];

													if (response.data.status == "success") {
														$scope.reasonList = response.data.listObject;
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
								
								$scope.setFirstTabLevel(1);
								
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEBILLINGBEDCATEGORYLIST+"/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId;
								GenericService.serviceAction("GET", URI, {})
								.then(
										function(response) {
											$scope.billingBedList = [];

											if (response.data.status == "success") {
												$scope.billingBedList = response.data.listObject;
												

											}
										});
								
								
							}
							$scope.init();
							//$scope.init();

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.submitRequest();
							}

							$scope.selectPatient = function(item) {

								$scope.itemSelecteds = [];
								
								angular.forEach($scope.listOfAdmittedPatientInitiatList,function(value,key){
									if(value.patientId == item.patientId){
										value.selectedPatient=false;
									}
									else{
										value.selectedPatient=true;
									}
									
								})
								// If checkbox is checked
								if ($scope.admittedPatient) {
									$scope.itemSelecteds = item;
									
									var data = 
									{
											wardId : $scope.itemSelecteds.wardId,
											organizationId:$scope.organizationId,
											unitId:$scope.unitId
									}
									
									
									var URI = BASE_URL + ROOT_URL
									+ GETBEDLISTBYWARDIDFORBEDTOBED;
							GenericService
									.serviceAction("POST", URI, data)
									.then(
											function(response) {
												$scope.bedListByWard = [];

												if (response.data.status == "success") {
													$scope.bedListByWard = response.data.listObject;

												}
											});
							console.log($scope.itemSelecteds);

								}
							}
							
							
							$scope.changeBillingBedCatId = function(index,bedId){
								var selectedBedObject = $.grep($scope.bedListByWard, function (bedList) {
						            return bedList.bedId == $scope.itemSelecteds.toBedId;
						        });
								$scope.listOfAdmittedPatientInitiatList[index].billingBedCategoryId = selectedBedObject[0].billingBedCategoryId;
							}

							$scope.submitRequest = function() {

								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.error(
													'Please select atleast one patient...!!',
													{
														title : 'Error!'
													});
								} else {
									
									if($scope.itemSelecteds.toBedId=="" || $scope.itemSelecteds.toBedId ==undefined || $scope.itemSelecteds.toBedId =="Select Bed"|| $scope.itemSelecteds.toBedId =="Select Bed No." ){
										$rootScope.stopLoader();
										growl.warning('Please select Bed...!!',
												{
													title : 'warning!'
												});
										return false;
									} 
									
									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;
										
									} else {
										$rootScope.startLoader();
										$scope.currentDate = new Date();
										$scope.currentDate = moment(
												$scope.currentDate).format(
												'DD-MM-YYYY HH:mm:ss');

										var data = {
											treatingDoctorId : $scope.itemSelecteds.doctorId,
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											admissionId : $scope.itemSelecteds.admissionId,
											transferId : $scope.itemSelecteds.transferId,
											fromWardId : $scope.itemSelecteds.wardId,
											reasonId : $scope.itemSelecteds.reasonId,
											note : $scope.itemSelecteds.remark,
											authorizedBy : $scope.userId,
											fromBedCategoryId : $scope.itemSelecteds.bedCategoryId,
											transferTypeId : 2,
											createdBy : $scope.userId,
											createdDate : $scope.currentDate,
											updatedBy : $scope.userId,
											updatedDate : $scope.currentDate,
											transferStatusId :4
										}
										

										var selectedBedObject = $.grep($scope.bedListByWard, function (bedList) {
								            return bedList.bedId == $scope.itemSelecteds.toBedId;
								        });
										
										data.toBedId = selectedBedObject[0].bedId;
										data.toBedCategoryId = selectedBedObject[0].bedCategoryId;
										data.toBillingBedCategoryId = $scope.itemSelecteds.billingBedCategoryId;
										data.toRoomId = selectedBedObject[0].roomId;
										
										
										
										console.log(data);
										//return false;
										var URI = BASE_URL + ROOT_URL
												+ ADDTRANSFERREQUEST;
										GenericService
												.serviceAction("POST", URI,
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
																$scope.init();
																
															} else {
																growl
																		.error(
																				'Something went wrong...!!',
																				{
																					title : 'Error!'
																				});
															}
															$rootScope.stopLoader();

														});

									}
								}
							}

							$scope.finalTransfer = function() {
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									transferdId : $scope.transferdId,
									tranferRequestId : $scope.transferReqestId,

								};

							}
							$scope.adtReason = '';
							$scope.adtRejectReason = '';
							$scope.doctorReason = '';
							$scope.doctorRejectReason = '';
							$scope.reasonPopupDoctor = function(resonDesc,rejectNote)
							{
								
								$('#doctorReason').val(resonDesc);
								$('#doctorRejectReason').val(rejectNote);
							}
							$scope.reasonPopupADT = function(resonDesc,rejectNote)
							{
								
								$('#adtReason').val(resonDesc);
								$('#adtRejectReason').val(rejectNote);
								
							}
							
							$scope.getAdmissionDataAccept = function(item)
							{
								//alert("sc");
								//console.log(item);
								$rootScope.itemSelectedsAccept = [];
								$rootScope.itemSelectedsAccept = item;
								console.log($rootScope.itemSelectedsAccept);
								
							}
							
							
							$scope.saveAcceptance = function()
							{
								$rootScope.startLoader();
								console.log($rootScope.itemSelectedsAccept);
								$scope.currentDate = new Date();
								$scope.currentDate = moment(
										$scope.currentDate).format(
										'DD-MM-YYYY HH:mm:ss');
							
								var tansferNote = $('#noteTransfer').val();
								var data = {};
								//return false;
								var URI = BASE_URL + ROOT_URL
										+ ACCEPTFINALTRANSFERREQUEST;
								if($scope.skipTransferAcceptance!=undefined && $scope.skipTransferAcceptance!=null 
										&& $scope.skipTransferAcceptance == 'Y'){
									URI = BASE_URL + ROOT_URL + ACCEPTFINALTRANSFERREQUEST;
									 data = {
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											transferId:$scope.itemSelectedsAccept.transferId,
											updatedBy:$scope.userId,
											updatedDate:$scope.currentDate,
											transferRequestId:$scope.itemSelectedsAccept.transferRequestId,
											workstationNote :tansferNote,
											toBedId : $scope.itemSelectedsAccept.toBedId,
											admissionId:$scope.itemSelectedsAccept.admissionId,
											toFloorId:$scope.itemSelectedsAccept.toFloorId,
											toWardId:$scope.itemSelectedsAccept.toWardId,
											toRoomId:$scope.itemSelectedsAccept.toRoomId,
											admissionDetailsId:$scope.itemSelectedsAccept.admissionDetailsId,
											fromBedId : $scope.itemSelectedsAccept.fromBedId,
											toBedCategoryId :$scope.itemSelectedsAccept.toBedCategoryId,
											toBillingBedCategoryId :$scope.itemSelectedsAccept.toBillingBedCategoryId,
											skipHousekeeping : $scope.skipHousekeeping!=null ? $scope.skipHousekeeping : 'Y'
											
									};
		
								}else{
									 data = {
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											transferId:$scope.itemSelectedsAccept.transferId,
												updatedBy:$scope.userId,
												updatedDate:$scope.currentDate,
													transferRequestId:$scope.itemSelectedsAccept.transferRequestId,
														finalNote :tansferNote											
											
									};
									 URI = BASE_URL + ROOT_URL + ACCEPTTRANSFERREQUESTONREQUEST;
								}
								GenericService
										.serviceAction("POST", URI,
												data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Bed Transferred Successfully...!!',
																		{
																			title : 'Success!'
																		});
														//commonNote
														angular.element(
														'#commonNote')
														.modal('hide');
														$scope.init();
														$('#noteTransfer').val("");
														$scope.setFirstTabLevel(2);
														
													} else {
														growl
																.error(
																		'Something went wrong...!!',
																		{
																			title : 'Error!'
																		});
													}
													$rootScope.stopLoader();

												});
							}
							
							$scope.saveAcceptReject = function()
							{
								$rootScope.startLoader();
								console.log($scope.itemSelectedsAccept);
								//return false;
								$scope.currentDate = new Date();
								$scope.currentDate = moment(
										$scope.currentDate).format(
										'DD-MM-YYYY HH:mm:ss');
							
								//var noteReject = $('#noteReject').val();
								//var rejectReasonId = $('#rejectReasonId').val();
								var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										transferId:$scope.itemSelectedsAccept.transferId,
											updatedBy:$scope.userId,
											updatedDate:$scope.currentDate,
												transferRequestId:$scope.itemSelectedsAccept.transferRequestId,
												toBedId : $scope.itemSelectedsAccept.toBedId,
												adtRejectReason :$('#noteRejectDrpDN').val(),
												adtReasonId : $('#rejectReasonIdDrpDN').val(),
												admissionId:$scope.itemSelectedsAccept.admissionId,
												fromBedCategoryId:$scope.itemSelectedsAccept.bedCategoryId,
												fromBillingBedCategoryId:$scope.itemSelectedsAccept.fromBillingBedCategoryId,
												fromWardId:$scope.itemSelectedsAccept.fromWardId,
												fromRoomId:$scope.itemSelectedsAccept.fromRoomId,
												fromBedId:$scope.itemSelectedsAccept.fromBedId,
												
										
								};
								
								if(data.adtReasonId=="" || data.adtReasonId==undefined || data.adtReasonId==null){
									growl.warning('Please Select Reason..',
											{
												title : 'warning'
											});
									$rootScope.stopLoader();
									return false;
								}
								console.log(data);
								//return false;
								var URI = BASE_URL + ROOT_URL
										+ REJECTTRANSFERREQUESTFORADT; //REJECTTRANSFERREQUESTONREQUEST
								GenericService
										.serviceAction("POST", URI,
												data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Bed Transfer Request Aborted...!!',
																		{
																			title : 'Success!'
																		});
														$rootScope.stopLoader();
														//accepatReject
														angular.element(
														'#accepatReject')
														.modal('hide');
														//$scope.init();
														 $state.reload();
													} else {
														$rootScope.stopLoader();
														growl
																.error(
																		'Something went wrong...!!',
																		{
																			title : 'Error!'
																		});
													}
													

												});
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
								var commonObjInitiatForBM = {
										admissionId:info.admissionId,
										organizationId:$scope.organizationId,
										unitId : $scope.unitId
										};
								
							var commonObjTransferBM  = {
									admissionId:info.admissionId,
									organizationId:$scope.organizationId,
									unitId : $scope.unitId
							};
							
							$scope.initInitiateRequestList($scope.organizationId,
										$scope.unitId,$scope.initiateReqOffset,
										$scope.initiateReqPerPageRecords,commonObjInitiatForBM);

							$scope.initPhysicalTransfertList($scope.organizationId,
										$scope.unitId,$scope.physicalTransferOffset,
										$scope.physicalTransferPerPageRecords,commonObjTransferBM);
							}
							
							/*First Level*/
					    	$scope.firstTabLevel = 1 ;
							
							$scope.setFirstTabLevel= function(newTab)
							{
								$scope.firstTabLevel = newTab;
								if(newTab == 1){
									
									if($scope.bedManagmentCookiesObj != null)
									{
											$scope.initInitiateRequestList($scope.organizationId,
													$scope.unitId,$scope.initiateReqOffset,
													$scope.initiateReqPerPageRecords);
									}else{
										if($scope.nurseRole == 1 && $scope.wardList.length > 0){
											
												$scope.initInitiateRequestList($scope.organizationId,
														$scope.unitId,$scope.initiateReqOffset,
														$scope.initiateReqPerPageRecords,commonObjInitiat);
											}
									}
								}else if(newTab == 2){
									if($scope.bedManagmentCookiesObj != null)
									{
											$scope.initPhysicalTransfertList($scope.organizationId,
													$scope.unitId,$scope.physicalTransferOffset,
													$scope.physicalTransferPerPageRecords);
									}else{
										if($scope.nurseRole == 1 && $scope.wardList.length > 0){
											
												$scope.initPhysicalTransfertList($scope.organizationId,
														$scope.unitId,$scope.physicalTransferOffset,
														$scope.physicalTransferPerPageRecords,commonObjTransfer);
											}
									}
								}
							};
							$scope.setFirstTabLevel(1);
							$scope.isSetFirstTabLevel = function(tabNum)
							{
								return $scope.firstTabLevel === tabNum;
							};
	
							//initiate Request
							$scope.getInitiateReqList = function(orgId,
									orgUnitId,offset,noOfRecordsPerPage,obj) {
								try {
									$rootScope.startLoader();
									$scope.listOfAdmittedPatientInitiatList = [];
									$scope.admittedPatientInitiatList = [];
									var commonObjInitiatForBM  = {};
									var URI = "";
									
									if(obj == undefined){
										 commonObjInitiatForBM = {
                                                 unitId : $scope.unitId,
                                                 organizationId:$scope.organizationId,
                                                 admissionId:$scope.bedManagmentCookiesObj.admissionId,
                                                 offset:offset,
                                                 noOfRecordsPerPage:noOfRecordsPerPage
												};
										 URI = BASE_URL + ROOT_URL + GETINITIATETRANSFERREQUESTFORBM;
										}else{
											obj.offset = offset;
											obj.noOfRecordsPerPage = noOfRecordsPerPage;
											commonObjInitiatForBM = obj;
											
											URI = BASE_URL + ROOT_URL+ GETINITIATETRANSFERREQUEST;
										}
									console.log("URI", URI);

									GenericService
											.serviceAction(METHOD_POST, URI, commonObjInitiatForBM)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.admittedPatientInitiatList  = response.data.listObject;
														angular.forEach($scope.admittedPatientInitiatList,function(value,index) {

															var newArr = {};
															newArr.patientFullName = value.pFirstName;
															newArr.dob = value.dob;
															newArr.genderCode = value.genderCode;
															newArr.uhidnumber = value.uhidnumber;
															newArr.patientId = value.patientId;
															newArr.tPatientId = value.tPatientId;
															newArr.bedNumber = value.bedNumber;
															newArr.admissionId = value.admissionId;
															newArr.transferId = value.transferId;
															newArr.hierarchyId = value.hierarchyId;
															newArr.prefixCode = value.prefixCode;
															newArr.bedCategoryId = value.bedCategoryId;
															newArr.bedCategoryDesc = value.bedCategoryDesc;
															newArr.doctorId = value.doctorId;
															newArr.admissionDetailsId = value.admissionDetailsId;
															newArr.transferStatusDesc = value.transferStatusDesc;
															newArr.wardId = value.wardId;
															newArr.wardName = value.wardName;
															newArr.transferStatusId = value.transferStatusId;
															newArr.reasonDesc = value.reasonDesc;
															newArr.ageStr = value.ageStr;
															newArr.prefixDesc = value.prefixDesc;
															newArr.imagePath = value.imagePath;
															newArr.selectedPatient = true;
																			
															$scope.listOfAdmittedPatientInitiatList
																					.push(newArr);
															});
													});
														
								} catch (e) {
									console.log(e.message)
								}
							}
							
							
							$scope.setInitiateReqPage = function(page, flag, pager) {
							    $scope.initiateReqPager = {};
							    if (pager !== undefined) {
							        $scope.initiateReqPager.startIndex = pager.startIndex;
							        $scope.initiateReqPager.pageSize = pager.pageSize;
							    }

							    if (page < 1 || page > $scope.initiateReqPager.totalPages) {
							        return;
							    }

							    $scope.initiateReqPager = PagerService.GetPager($scope.initiateReqListCount, page, $scope.initiateReqPerPageRecords);

							    if (flag) {
							        if ($scope.bedManagmentCookiesObj != null) {
							           $scope.getInitiateReqList($scope.organizationId, $scope.unitId, $scope.initiateReqPager.startIndex, $scope.initiateReqPager.pageSize);
							           
							        } else {
							            if ($scope.nurseRole == 1 && $scope.wardList.length > 0) {
							               $scope.getInitiateReqList($scope.organizationId, $scope.unitId, $scope.initiateReqPager.startIndex, $scope.initiateReqPager.pageSize, commonObjInitiat);
							               
							            }
							        }
							    }
							}

					
							
							//physical Transfer
							$scope.getPhysicalTransferList = function(orgId,
									orgUnitId,offset,noOfRecordsPerPage,obj){
								try {
									$rootScope.startLoader();
									$scope.listOfAdmittedPatientList = [];
									$scope.admittedPatientList = [];
									var commonObjTransferBM  = {};
									var URI = "";
									if(obj == undefined){
										commonObjTransferBM = {
												unitId : $scope.unitId,
												organizationId : $scope.organizationId,
												admissionId:$scope.bedManagmentCookiesObj.admissionId,
												offset:offset,
												noOfRecordsPerPage:noOfRecordsPerPage
										};
										URI = BASE_URL + ROOT_URL + GETTRANSFERREQUESTLISTBYADMISSIONIDFORB2B;
										}else{
											obj.offset = offset;
											obj.noOfRecordsPerPage = noOfRecordsPerPage;
											commonObjTransferBM = obj;
											URI = BASE_URL + ROOT_URL+ GETTRANSFERPATIENTLIST;
										}

									console.log("URI", URI);
									GenericService
											.serviceAction(METHOD_POST, URI, commonObjTransferBM)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.admittedPatientList  = response.data.listObject;
														angular.forEach($scope.admittedPatientList,function(value,index) {

															var newArr = {};
															newArr.patientFullName = value.pFirstName;
															newArr.dob = value.dob;
															newArr.genderCode = value.genderCode;
															newArr.uhidnumber = value.uhidnumber;
															newArr.patientId = value.patientId;
															newArr.tPatientId = value.tPatientId;
															newArr.bedNumber = value.bedNumber;
															newArr.admissionId = value.admissionId;
															newArr.transferId = value.transferId;
															newArr.hierarchyId = value.hierarchyId;
															newArr.prefixCode = value.prefixCode;
															newArr.bedCategoryId = value.bedCategoryId;
															newArr.bedCategoryDesc = value.bedCategoryDesc;
															newArr.doctorId = value.doctorId;
															newArr.admissionDetailsId = value.admissionDetailsId;
															newArr.transferStatusDesc = value.transferStatusDesc;
															newArr.wardId = value.wardId;
															newArr.wardName = value.wardName;
															newArr.transferStatusId = value.transferStatusId;
															newArr.toBedCategoryDesc = value.toBedCategoryDesc;
															newArr.toBedNumber = value.toBedNumber;
															newArr.toWardName = value.toWardname;
															newArr.rejectReasonDesc = value.rejectReasonDesc;
															newArr.reasonDesc = value.reasonDesc;
															newArr.adtReasonDesc = value.adtReasonDesc;
															newArr.adtRejectReason =  value.adtRejectReason;
															newArr.doctorReasonDesc =  value.doctorReasonDesc;
															newArr.doctorRejectReason =  value.doctorRejectReason;
															newArr.transferRequestId =  value.transferRequestId;
															newArr.toBedId = value.toBedId;
															newArr.toWardId = value.toWardId;
															newArr.toFloorId = value.toFloorId;
															newArr.toRoomId = value.toRoomId;
															newArr.fromBedId = value.fromBedId;
															newArr.toBedCategoryId = value.toBedCategoryId;
															newArr.toBillingBedCategoryId = value.billingBedCategoryId;
															newArr.transferTypeId = value.transferTypeId;
															newArr.remark = value.note;
															newArr.ageStr = value.ageStr;
															newArr.prefixDesc = value.prefixDesc;
															newArr.imagePath = value.imagePath;
                                                            newArr.fromFloorId = value.fromFloorId;
															
															$scope.listOfAdmittedPatientList.push(newArr);
															});
													});
														
								} catch (e) {
									console.log(e.message)
								}
							}
							
							$scope.physicalTransferPager = {};
							$scope.physicalTransferPage;
							$scope.setPhysicalTransferPage = function(page, flag,pager)
							{
								if(pager !=undefined){
									$scope.physicalTransferPager.startIndex = pager.startIndex;
									$scope.physicalTransferPager.pageSize = pager.pageSize;
								}
								
								if (page < 1 || page > $scope.physicalTransferPager.totalPages)
								{
									return;
								}
								
								$scope.physicalTransferPager = PagerService.GetPager($scope.physicalTransferListCount, page,$scope.physicalTransferPerPageRecords);
								
								if (flag) {
									if($scope.bedManagmentCookiesObj != null)
									{
										$scope.getPhysicalTransferList($scope.organizationId, $scope.unitId,$scope.physicalTransferPager.startIndex,$scope.physicalTransferPager.pageSize);
									}else{
										if($scope.nurseRole == 1 && $scope.wardList.length > 0){
											
											$scope.getPhysicalTransferList($scope.organizationId, $scope.unitId,$scope.physicalTransferPager.startIndex,$scope.physicalTransferPager.pageSize,commonObjTransfer);
										}
									}
								}
							}

						} ]);

