'use strict';

/**
 * @Author By Vivek Satle
 * @name myApp.controller:patientAcceptanceController
 * @description #patientAcceptanceController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'patientAcceptanceController',
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
						'$timeout',
						'PagerService',
						'BillingGenericService',
						'reportService',
						'$window',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state,$timeout,PagerService,BillingGenericService,reportService, $window) {

							

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.acceptRequest();

							}

							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
							 if(cookieObjectAppConfig == undefined){
							  $state.go('login');
							  return;
							 }
							 
							 
							 $scope.dateFormat = cookieObjectAppConfig.dateFormat;
							 $scope.skipHousekeeping = cookieObjectAppConfig.skipHousekeeping;
							 var specialtyNursingStationCookies = JSON.parse(sessionStorage.getItem('specialtyNursingStationCookiesNur'));
								var nursingStationCookie = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
								$scope.nursingStationCookieShow = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
								 if(specialtyNursingStationCookies == undefined){
									 growl.warning(
												'No nursing station and specialty mapped with current user!!',
												{
													title : 'Success!'
												});
								 }
								 else
								  {
								   console.log('specialtyNursingStationCookies',specialtyNursingStationCookies);
								   console.log('nursingStationCookie',nursingStationCookie);
								   $scope.nursingStationDesc=nursingStationCookie;
								   $scope.departmentNursingStationMapperDtoslist = specialtyNursingStationCookies.departmentNursingStationlist;
									console.log("ggg",$scope.departmentNursingStationMapperDtoslist);
								  }
							 
									
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
													angular.element('#nursingStationModal').modal('hide');
//													$state.reload("patientAcceptance");
													var specialtyNursingStationCookies = JSON.parse(sessionStorage.getItem('specialtyNursingStationCookiesNur'));
													var nursingStationCookie = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
													$scope.nursingStationCookieShow = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
													 if(specialtyNursingStationCookies == undefined){
														 growl.warning(
																	'No nursing station and specialty mapped with current user!!',
																	{
																		title : 'Success!'
																	});
													 }
													 else
													  {
													   console.log('specialtyNursingStationCookies',specialtyNursingStationCookies);
													   console.log('nursingStationCookie',nursingStationCookie);
													   $scope.nursingStationDesc=nursingStationCookie;
													   $scope.departmentNursingStationMapperDtoslist = specialtyNursingStationCookies.departmentNursingStationlist;
														console.log("ggg",$scope.departmentNursingStationMapperDtoslist);
													  }
													 
													  $scope.nursingWardList =response.data.object; 
													 //$scope.getAdmittedPatientList();
													 
													 if($scope.firstTabLevel==1){
															$scope.getNewAdmissionList();
														}
														if ($scope.firstTabLevel == 2) {
															
															$scope.getTransferListTap();
														}
														if ($scope.firstTabLevel == 3) {
															
															$scope.getICUTransferList();
														}
														if ($scope.firstTabLevel == 4) {
															
															$scope.getAdmittedPatientList();
														}
													 
													// $scope.getNewAdmissionList();
													 //$scope.getTransferListTap();
													 //$scope.getICUTransferList();
												}
										 });
									}
									}
								 
								 
							$scope.displayNote = function(noteDoctor) {
								$scope.noteDoctor = noteDoctor;
							}

							$scope.reasonPopupDoctor = function(resonDesc,
									rejectNote) {

								$('#doctorReason').val(resonDesc);
								$('#doctorRejectReason').val(rejectNote);
							}
							$scope.reasonPopupADT = function(resonDesc,
									rejectNote) {

								$('#adtReason').val(resonDesc);
								$('#adtRejectReason').val(rejectNote);

							}
							$scope.visitTypeIdflag = "2";
//							*********************sneha**************************
							
							var cookieObject = $cookies
							.getObject('cookieObject');
							if (cookieObject == undefined) {
								$state.go('login');
								return;
							}
			                
							
							 if(cookieObject.nursingIpScan == true){
								 $scope.nursingIpScan = 'Y';
							 }else{
								 $scope.nursingIpScan = 'N';
							 }
							
							 $scope.collectionObj = {};
								$scope.validateUhid = "";
								$scope.validateWorkListDetails = function(obj){
									
									if($scope.nursingIpScan != null && $scope.nursingIpScan =="Y" ){
										$scope.collectionObj = obj; 
										$scope.validateUhid = "";
										$('#validatePatientAcceptance').modal('show');
										setTimeout(function(){
										    $('#patientValidate').focus();
										}, 500);
									}else{
										$scope.generateIpdCoverSheetUrl(obj);
									}
									
								}
								
								$scope.closevalidatePop = function(){
									$('#validatePatientAcceptance').modal('hide');
								}
								
								$scope.validateEquealPatient = function(event,barcode){
									 var enterCode = 13;
									 if (!barcode && event.which === enterCode) {
									        barcode = $scope.validateUhid;
									      }
										 if(!!barcode){
											 
											 if($scope.collectionObj.uhidnumber.toLowerCase() == $scope.validateUhid.toLowerCase() ){
													$('#validatePatientAcceptance').modal('hide');
													$('.modal-backdrop').remove()
			                                        $(document.body).removeClass("modal-open");
													$scope.generateIpdCoverSheetUrl($scope.collectionObj);
												}else{
													growl.warning('Please enter valid patient number',
															{
																title : 'Warning!'
															});
												}
											 
										 }
										
								}
								
							 
							 
							$scope.generateIpdCoverSheetUrl = function(patientDetailsObj)
							{
								//$rootScope.getTemplateCountByAdmissionOrEncounter(patientDetailsObj.patientId,patientDetailsObj.admissionId,null);
								if(patientDetailsObj!=null)
								  {
									
									patientDetailsObj.startConsultanceStartTime = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'); 
//									patientDetailsObj.mappedDataCoversheetByUserss = [];
//									patientDetailsObj.mappedDataCoversheetByUserss = $scope.mappedDataCoversheetByUser, 
									console.log("patientDetails",JSON.stringify(patientDetailsObj));
									
									if(patientDetailsObj.visitTypeId == 2){
												$state.go('nursingCoversheet',
													{
													patientDetailsObj :patientDetailsObj
													});
									}
									if(patientDetailsObj.visitTypeId == 3){
										$state.go('dayCareNursingCoversheet',
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
							
							// Nursing React Page
													
							$scope.gotoNursingInfo = function (info) {
								var baseUrlLocation = window.location.href;
								var temp = baseUrlLocation.split('//');
								var BASE_URL_CALL = temp[1].split('.');
								$scope.subDomain = BASE_URL_CALL[0];
								$scope.patientId = info.patientId;
								$scope.doctorId = info.doctorId;
								$scope.admissionId = info.admissionId;
								$scope.visitTypeId = info.visitTypeId;
								$scope.getNrsingInfoUrl();
							}

							$scope.getNrsingInfoUrl = function () {
								if($scope.subDomain == "qa" || $scope.subDomain == "219"){
									var New_Nursing = "https://nursingapp.his.clinivantage.co.in/?";
									var url_Nursing = "organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + 
										"&patientId=" + $scope.patientId + "&doctorId=" + $scope.doctorId + "&visitTypeId=" + $scope.visitTypeId +
										"&admissionId=" + $scope.admissionId + "&userId=" + $scope.userId + "&storeId=" + $scope.storeId + "&subDomain=" + $scope.subDomain;
								}
								else if($scope.subDomain == "smepp" || $scope.subDomain == "queenspp" || $scope.subDomain == "meditestpp" || $scope.subDomain == 'nchpp'){
									var New_Nursing = "https://nursingapp.his.clinivantage.co.in/?";
									var url_Nursing = "organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + 
									"&patientId=" + $scope.patientId + "&doctorId=" + $scope.doctorId + "&visitTypeId=" + $scope.visitTypeId +
									"&admissionId=" + $scope.admissionId + "&userId=" + $scope.userId + "&storeId=" + $scope.storeId + "&subDomain=" + $scope.subDomain;
								}
								else{
									var New_Nursing = "https://nursingapp.his.clinivantage.co.in/?";
									var url_Nursing = "organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + 
									"&patientId=" + $scope.patientId + "&doctorId=" + $scope.doctorId + "&visitTypeId=" + $scope.visitTypeId +
									"&admissionId=" + $scope.admissionId + "&userId=" + $scope.userId + "&storeId=" + $scope.storeId + "&subDomain=" + $scope.subDomain;
								}
								var url = New_Nursing + url_Nursing;
								return url;
							}
							
							// Nursing React Page End

							//for ICU Start

							$scope.bedAllocastionTransfer = function(item) {
								$scope.activeBedList = [];
								$scope.beditemSelecteds = {};
								if ($scope.itemSelecteds != null || $scope.itemSelecteds != undefined) 
									{
									if($scope.itemSelecteds.transferId == item.transferId)
										{
										
										
										var data = {
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											icuTypeId : item.icuTypeId
										}
										var URI = BASE_URL + ROOT_URL
												+ GETACTIVEBEDLISTBYICUTYPEID;
										GenericService
												.serviceAction("POST", URI, data)
												.then(
														function(response) {
															if (response.data.status == "success") {
																$scope.activeBedList = response.data.listObject;
															}
														});
										angular.element('#bedAllocationForTransfer').modal('show');
										
										}
									else
										{
										   growl.warning('Please select patient..!!',{title : 'Warning!'});
										   return false;
										}
									
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
								var data = {
										admissionId:info.admissionId,
										organizationId:$scope.organizationId,
										unitId : $scope.unitId
										};
								var URI = BASE_URL + ROOT_URL
								+ GETICUTRANSFERREQUESTLISTBYADMISSIONID;
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											$scope.listOfAdmittedPatientList = [];
											$scope.admittedPatientList = [];

											if (response.data.status == "success") { 

												if(response.data.listObject.length == 0)
													$scope.loadingMsg="No records found...!!";
												
												$scope.admittedPatientList = response.data.listObject;
												angular
														.forEach(
																$scope.admittedPatientList,
																function(
																		value,
																		index) {

																	var newArr = {};
																	newArr.patientFullName = value.patientName;
																	newArr.dob = value.dob;
																	newArr.genderCode = value.genderCode;
																	newArr.uhidnumber = value.uhidnumber;
																	newArr.patientId = value.patientId;
																	newArr.tPatientId = value.tPatientId;
																	newArr.fromBedId = value.fromBedId;
																	newArr.fromBedNumber = value.fromBedNumber;
																	newArr.admissionId = value.admissionId;
																	newArr.transferId = value.transferId;
																	newArr.prefixCode = value.prefixCode;
																	newArr.fromBedCategoryId = value.fromBedCategoryId;
																	newArr.fromBedCategoryDesc = value.fromBedCategoryDesc;
																	newArr.doctorId = value.doctorId;
																	newArr.admissionDetailsId = value.admissionDetailsId;
																	newArr.transferStatusDesc = value.transferStatusDesc;
																	newArr.fromWardId = value.fromWardId;
																	newArr.fromWardName = value.fromWardName;
																	newArr.transferStatusId = value.transferStatusId;
																	newArr.toBedCategoryId = value.toBedCategoryId;
																	newArr.toBedCategoryDesc = value.toBedCategoryDesc;
																	newArr.toWardId = value.toWardId;
																	newArr.toWardName = value.toWardName;
																	newArr.reasonDesc = value.reasonDesc;
																	newArr.adtRejectReason = value.adtRejectReason;
																	newArr.doctorReasonDesc = value.doctorReasonDesc;
																	newArr.doctorRejectReason = value.doctorRejectReason;
																	newArr.transferRequestId = value.transferRequestId;
																	newArr.toBedNumber = value.toBedNumber;
																	newArr.toBedId = value.toBedId;
																	newArr.doctorName = value.doctorName;
																	newArr.finalNote = value.finalNote;
																	newArr.fromRoomId = value.fromRoomId;
																	newArr.toRoomId = value.toRoomId;
																	newArr.fromFloorId = value.fromFloorId;
																	newArr.toFloorId = value.toFloorId;
																	newArr.toBillingBedCategoryId=value.toBillingBedCategoryId
																	newArr.remark = value.note;
																	newArr.createdDate = value.createdDate;
																	newArr.visitTypeId = value.visitTypeId;
																	$scope.listOfAdmittedPatientList
																			.push(newArr);

																});

											}
										});

						var URI = BASE_URL + ROOT_URL+ GETFINALTRANSFERREQUESTLISTBYADMISSIONID;
						GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											$scope.transferICUList = [];

											if (response.data.status == "success") 
											{
												$scope.transferICUList = response.data.listObject;
											}
											else if(response.data.status == "error")
												{
												
												}
										});
								
								
							}
							
							
							
							
							//code start from here for tab wise data fetching
							
//******************************************getting NEW ADMISSION LIST ***************************************************************
							$scope.getNewAdmissionList=function(){
								
								
								// For Paginations Start for nursing patient acceptance
								$scope.setNoOfRecords = function() {
									$scope.initNursingAcceptanceList($scope.organizationId,$scope.unitId, $scope.offset,$scope.noOfRecordsPerPage);
								};
								$scope.initNursingAcceptanceList = function(orgId,unitId,offset, noOfRecordsPerPage) {
									
									
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;
									
									var URI = BASE_URL + ROOT_URL +"/adt/nursing/patient/acceptance";
									var URI1 = BASE_URL + ROOT_URL+ "/adt/nursing/patient/acceptance/count";
									
									var commonObjForTransfer = {
											unitId : $scope.unitId,
											organizationId : $scope.organizationId,
											wardList : $scope.nursingWardList,
											offset : offset,
											noOfRecordsPerPage : noOfRecordsPerPage,
											isFromNursingAcceptancePage:'Y'
											};
									
									
									if($scope.isShowPatientByBed=='Y'){
										if($scope.nurseBedList.length>0){
											commonObjForTransfer.bedList=$scope.nurseBedList;
											commonObjForTransfer.isShowPatientByBed='Y';
										}else{
										   $scope.nursingWardList=[];
										}
									}
									else{
										commonObjForTransfer.bedList=$scope.nurseBedList;
										commonObjForTransfer.isShowPatientByBed='N';
									}

									if($scope.nurseRole == 1 && $scope.nursingWardList.length > 0)
									{
										
										var URI = BASE_URL + ROOT_URL
										+"/adt/nursing/patient/acceptance";
										$rootScope.startLoader();
										GenericService.serviceAction("POST", URI, commonObjForTransfer)
										.then(
												function(response) {
													$scope.newAdmittedPatientList = [];

													if (response.data.status == "success") 
													{
														if(response.data.listObject.length == 0)
															$scope.loadingMsg="No records found...!!";
														$scope.newAdmittedPatientList = response.data.listObject;
														console.log("MY NewAdmitted List is:"+$scope.newAdmittedPatientList);
														$rootScope.stopLoader();
													}else{
														$rootScope.stopLoader();
													}
												});
										}

									GenericService.serviceAction("POST", URI1, commonObjForTransfer).then(
													function(response) {
														if (response.data.status == "success") {
															console.log(response.data);
															$scope.commonListCount = response.data.object;
															$scope.setPage(1,false);

														}
													});
								}

								$scope.gettNursingAcceptanceList = function(orgId,unitId,offset, noOfRecordsPerPage) {
									console.log("offset", offset);
									var data = "";
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;
									var URI = BASE_URL + ROOT_URL + "/adt/nursing/patient/acceptance";
									
									var commonObjForTransfer = {
											unitId : $scope.unitId,
											organizationId : $scope.organizationId,
											wardList : $scope.nursingWardList,
											offset : offset,
											noOfRecordsPerPage : noOfRecordsPerPage,
											isFromNursingAcceptancePage:'Y'
											};
									
									
									if($scope.isShowPatientByBed=='Y'){
										if($scope.nurseBedList.length>0){
											commonObjForTransfer.bedList=$scope.nurseBedList;
											commonObjForTransfer.isShowPatientByBed='Y';
										}else{
										   $scope.nursingWardList=[];
										}
									}
									else{
										commonObjForTransfer.bedList=$scope.nurseBedList;
										commonObjForTransfer.isShowPatientByBed='N';
									}

									if($scope.nurseRole == 1 && $scope.nursingWardList.length > 0){
										
										var URI = BASE_URL + ROOT_URL
										+"/adt/nursing/patient/acceptance";
										$rootScope.startLoader();
										GenericService.serviceAction("POST", URI, commonObjForTransfer)
										.then(
												function(response) {
													$scope.newAdmittedPatientList = [];

													if (response.data.status == "success") 
													{
														if(response.data.listObject.length == 0)
															$scope.loadingMsg="No records found...!!";
														$scope.newAdmittedPatientList = response.data.listObject;
														console.log("MY NewAdmitted List is22:"+$scope.newAdmittedPatientList);
														$rootScope.stopLoader();
													}else{
														$rootScope.stopLoader();
													}
												});
										}
								}

								$scope.pager = {};
								$scope.page;

								$scope.setPage = function(page, flag) {
									if (page < 1 || page > $scope.pager.totalPages) {
										return;
									}
									$scope.pager = PagerService.GetPager($scope.commonListCount, page,$scope.noOfRecordsPerPage);
									if (flag) {
										$scope.gettNursingAcceptanceList($scope.organizationId,$scope.unitId,$scope.pager.startIndex,$scope.pager.pageSize);
									}
								}

								$scope.initNursingAcceptanceList($scope.organizationId,$scope.unitId,$scope.offset, $scope.noOfRecordsPerPage);

							    	// For Paginations End

								}
								
							//start for new admission 
							$scope.getRejectionData = function(item)
							{
								$scope.rejectionDataItem = item;
								
							}
							$scope.rejectBtnDisabled = false;
							$scope.saveNewAdmissionReject = function()
							{
								if($scope.rejectReasonId > 0){
								$rootScope.startLoaderPopup();
								$scope.rejectBtnDisabled = true;
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
								.format('DD-MM-YYYY HH:mm:ss');
								var data = {
										updatedDate: $scope.currentDate,
										updatedBy:$scope.userId,
										organizationId:$scope.organizationId,	
										unitId:$scope.unitId,
										admissionId:$scope.rejectionDataItem.admissionId,
										nursingRejectionNote:$scope.noteReject,
										bedId : $scope.rejectionDataItem.bedId,
										bedNumber : $scope.rejectionDataItem.bedNumber,
										admissionDetailsId : $scope.rejectionDataItem.admissionDetailsId,
										reasonId : $scope.rejectReasonId
								}
								
									var URI = BASE_URL+ ROOT_URL+ "/adt/nursing/patient/request/reject";
							GenericService.serviceAction("POST", URI,data).then(
											function(response) {
	
												if (response.data.status == "success") {
													growl.success('Admission Rejected...!!',{title : 'Success!'});
//													$timeout($scope.init(), 3000);
												
													angular.element('#rejectPopup')
													.modal('hide');
													$scope.rejectBtnDisabled = false;
													$rootScope.stopLoaderPopup();
												} else if(response.data.status == "error"){
													
													if(response.data.message!=null){
														growl.error(response.data.message,{title : 'Error!'});
													}else{
													 growl.error('Something went wrongs...!!',{title : 'Error!'});
													}
													$rootScope.stopLoaderPopup();
													$scope.rejectBtnDisabled = false;
												}
												$scope.getNewAdmissionList();
											});
							}else
								{
								   growl.warning('Please select reason..!!',{title : 'Warning!'});
								}
						
							}
							
							//get OT ReservationList By Patient ID
							$scope.encounterFlag = false;
							$scope.otReservationList = [];
							$scope.getOTReservationList = function(patientId){
								$rootScope.startLoader();
								$scope.otReservationList = [];
								 var URI = BASE_URL + ROOT_URL + "/api/otims/OTReservation/getOTReservationListByPatientId";
						         var param = {
						      		   patientId:patientId
						         };
							   BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success"){
										if(response.data.listObject.length>0){
											angular.forEach(response.data.listObject,function(value,index){
												value.isSelected = false;
												value.reasonId="";
												value.reasonNote="";
											});
											$scope.otReservationList = response.data.listObject;
											
												 $("#otReservationDetails").modal("show");
												 
										}else{
												 if($scope.encounterFlag == true){
														$scope.checkPendingServicesOrder();
													}else{
														
														angular.element('#acceptForPopup').modal('show');
													}
										}
										
										$rootScope.stopLoader();
										}else if(response.data.status == "error"){
										$rootScope.stopLoader();
									}
									
								});
							}

							$scope.getAcceptData = function(item)
							{
								$scope.acceptDataItem = item;
								 if(item.encounterId != null){
									 
									 $scope.encounterFlag = true;
									 
									}else{
										 $scope.encounterFlag = false;
									}
								$scope.getOTReservationList(item.patientId);
								
								
							}
							
							
							$scope.acceptPateintAdmission = function(){
								
								 if( $scope.encounterFlag == true){
										$scope.checkPendingServicesOrder();
									}else{
										
										angular.element('#acceptForPopup').modal('show');
									}
							}
							
							$scope.acceptBtnDisabled = false;
							$scope.pendingServicesList = [];
							
							$scope.checkPendingServicesOrder= function(){
								
								$rootScope.startLoader();
								
								var data1={
										encounterId: $scope.acceptDataItem.encounterId,
										visitTypeId : 1
								}
								var URI = BASE_URL + ROOT_URL + "/api/billing/service/preBilling/orderedServices";
								BillingGenericService.serviceAction(METHOD_POST, URI, data1, NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.pendingServicesList = [];
													//$scope.checkLengthServicesList = [];
													if (response.data.status == "success") {
														
														/*//$scope.checkLengthServicesList = response.data.listObject;
														if(//$scope.checkLengthServicesList.length > 0){
															angular.element('#pendingServicesModal').modal('show');
															$rootScope.stopLoader();
														}else{
																angular.element('#acceptForPopup').modal('show');
																$rootScope.stopLoader();
														}
														angular.forEach(response.data.listObject,function(value,index){
															value.checkOption='N';
															if(!value.isPackage)
																{
																  $scope.pendingServicesList.push(value);
																}
														});
														$rootScope.stopLoader();
														console.log("PENDING service List",$scope.pendingServicesList );*/
														
														if(response.data.listObject.length > 0){
															var i = 1;
															angular.forEach(response.data.listObject,function(value,index){
																
																var arr = (value.orderDetailsPackageMapperDtosList).filter(function(item){
											   	            	   return (item.isPackageService == 'N');
											   				 	});
																
																value.checkOption='N';
																if(!value.isPackage && (value.orderDetailsPackageMapperDtosList.length==1 && arr!=undefined && arr.length==1))
																	{
																	  $scope.pendingServicesList.push(value);
																	}
																if(i == response.data.listObject.length){
																	if($scope.pendingServicesList.length > 0)
																	{
																		//$("#pendingServicesModal").show();
																		angular.element('#pendingServicesModal').modal('show');
																		$rootScope.stopLoader();
																	}else{
																			//$("#acceptForPopup").show();
																			angular.element('#acceptForPopup').modal('show');
																			$rootScope.stopLoader();
																	}
																}
																i++;
															});
														}else{
															angular.element('#acceptForPopup').modal('show');
															$rootScope.stopLoader();
														}
															
															
															//Code change by Ganesh
															//$scope.pendingServicesList = response.data.listObject;
															$rootScope.stopLoader();
															console.log("PENDING service List",$scope.pendingServicesList );
													}

												});
							}
							
							
							
						
							$scope.checkService = function(item)
							{
								if($scope.pendingServicesList.length > 0){
									angular.element('#pendingServicesModal').modal('show');
									  return false;
									}else{
								angular.element('#acceptForPopup').modal('hide');
								$scope.saveNewAdmissionAccept();
									}
							}
							
							$scope.selectAllPendingServices=function(selectAllPending){
								if(selectAllPending == 'Y'){
									angular.forEach($scope.pendingServicesList,function(value,index){
										value.checkOption='Y';
									});
									}else{
										angular.forEach($scope.pendingServicesList,function(value,index){
											value.checkOption='N';
										});
									}
								console.log("checkAll",$scope.pendingServicesList);
							}
							
							$scope.checkAllTrue = function() {
								var pendingServicesLength = $scope.pendingServicesList.length;
								$scope.servicesCount=0;
								angular.forEach($scope.pendingServicesList,function(value,index){
									if(value.checkOption == 'Y'){
										$scope.servicesCount++;
									}
								});
								if($scope.servicesCount == pendingServicesLength){
									$scope.selectAllPending = 'Y';
								}else{
									$scope.selectAllPending = 'N';
								}
							};
							
							$scope.cancelPendingService = function(item)
							{
								if($scope.pendingServicesList.length > 0){
									//$("#pendingServicesModal").hide();
									angular.element('#pendingServicesModal').modal('hide');
									}
								angular.forEach($scope.pendingServicesList,function(value,index){
									value.checkOption='N';
								});
								$scope.pendingServicesList=[];
								//$scope.checkLengthServicesList=[];
								$scope.orderDetailsIds=[];
								$scope.saveNewAdmissionAccept();
								console.log("notRequiredAll",$scope.pendingServicesList);
								
							}
							
							$scope.saveAdmissionAccept= function(){
								
								$scope.orderDetailsIds=[];
								
								angular.forEach($scope.pendingServicesList,function(value,index){
									if(value.checkOption=='Y'){
										$scope.orderDetailsIds.push(value.orderDetailsId); 
									}
								  });
								 if(Object.keys($scope.orderDetailsIds).length >0)
								 {
									 $("#pendingServicesModal").hide();
										$scope.saveNewAdmissionAccept();
								 }else{
									 growl.warning("Please select at least one serivce..!!",{
											title : WARNING_MSG
										});
										return;
								 }
								
							}
							
							$scope.saveNewAdmissionAccept = function()
							{
								$rootScope.startLoader();
								$scope.acceptBtnDisabled = true;
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
								.format('DD-MM-YYYY HH:mm:ss');
								if($scope.pendingServicesList.length > 0){
								$("#pendingServicesModal").hide();
								
								}
								
								$scope.finalOtReservationList = [];
								angular.forEach($scope.otReservationList ,function(value,index){
									if(value.isSelected){
										$scope.finalOtReservationList.push(value);
									}
								});
								
								
								var data = {
										updatedDate: $scope.currentDate,
										updatedBy:$scope.userId,
										organizationId:$scope.organizationId,	
										unitId:$scope.unitId,
										admissionId:$scope.acceptDataItem.admissionId,
										bedNumber : $scope.acceptDataItem.bedNumber,
										doctorId : $scope.acceptDataItem.doctorId,
										patientId : $scope.acceptDataItem.patientId,
										encounterId : $scope.acceptDataItem.encounterId,
										visitTypeId : $scope.acceptDataItem.visitTypeId,
										orderDetailsIds:$scope.orderDetailsIds,
										otReservationList : $scope.finalOtReservationList
								};
								
								
								
								
								var URI = BASE_URL+ ROOT_URL+ "/adt/nursing/patient/request/accept";
								GenericService.serviceAction("POST", URI,data).then(
										function(response) {

											if (response.data.status == "success") 
											{
												growl.success('Admission accepted...!!',{title : 'Success!'});												
												angular.element('#acceptForPopup').modal('hide');
												$scope.acceptBtnDisabled = false;
//												$timeout($scope.init(), 3000);
												$scope.pendingServicesList=[];
												//$scope.checkLengthServicesList=[];
												$scope.orderDetailsIds=[];
												$rootScope.stopLoader();
												
											} else if(response.data.status == "error")
											{
												
												if(response.data.message!=null){
													growl.error(response.data.message,{title : 'Error!'});
												}else{
												 growl.error('Something went wrongs...!!',{title : 'Error!'});
												}
//												$timeout($scope.init(), 3000);
												$rootScope.stopLoader();
												$scope.pendingServicesList=[];
												//$scope.checkLengthServicesList=[];
												$scope.orderDetailsIds=[];
												$scope.acceptBtnDisabled = false;
											}
											$scope.getNewAdmissionList();
										});
						
							}
							
					
     //************************************** TRANSFER *************************************************************************
							
							$scope.getTransferListTap=function(){								
							
								
								// For Paginations Start for nursing patient acceptance
								$scope.setNoOfRecords = function() {
									$scope.initTransferList($scope.organizationId,$scope.unitId, $scope.offset,$scope.noOfRecordsPerPage);
								};
								$scope.initTransferList = function(orgId,unitId,offset, noOfRecordsPerPage) {
									
									
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;
									
									var URI = BASE_URL + ROOT_URL +GETFINALTRANSFERREQUESTLIST;
									var URI1 = BASE_URL + ROOT_URL+ "/adt/getFinalTransferRequestListForNursingPageCount";
									
									var commonObjForTransfer = {
											unitId : $scope.unitId,
											organizationId : $scope.organizationId,
											wardList : $scope.nursingWardList,
											offset : offset,
											noOfRecordsPerPage : noOfRecordsPerPage,
											isFromNursingAcceptancePage:'Y'
											};
									
									
									if($scope.isShowPatientByBed=='Y'){
										if($scope.nurseBedList.length>0){
											commonObjForTransfer.bedList=$scope.nurseBedList;
											commonObjForTransfer.isShowPatientByBed='Y';
										}else{
										   $scope.nursingWardList=[];
										}
									}
									else{
										commonObjForTransfer.bedList=$scope.nurseBedList;
										commonObjForTransfer.isShowPatientByBed='N';
									}

									if($scope.nurseRole == 1 && $scope.nursingWardList.length > 0){
										
										var URI = BASE_URL + ROOT_URL+ GETFINALTRANSFERREQUESTLIST;
										$rootScope.startLoader();
								GenericService.serviceAction("POST", URI, commonObjForTransfer)
										.then(function(response) {
													$scope.listOfAdmittedPatientList = [];
													$scope.admittedPatientList = [];

													if (response.data.status == "success") {
														$scope.admittedPatientList = response.data.listObject;
														angular.forEach($scope.admittedPatientList,
																		function(value,index) {

																			var newArr = {};
																			newArr.patientFullName = value.patientName;
																			newArr.dob = value.dob;
																			newArr.genderCode = value.genderCode;
																			newArr.uhidnumber = value.uhidnumber;
																			newArr.patientId = value.patientId;
																			newArr.tPatientId = value.tPatientId;
																			newArr.fromBedId = value.fromBedId;
																			newArr.fromBedNumber = value.fromBedNumber;
																			newArr.admissionId = value.admissionId;
																			newArr.transferId = value.transferId;
																			newArr.prefixCode = value.prefixCode;
																			newArr.fromBedCategoryId = value.fromBedCategoryId;
																			newArr.fromBedCategoryDesc = value.fromBedCategoryDesc;
																			newArr.doctorId = value.doctorId;
																			newArr.admissionDetailsId = value.admissionDetailsId;
																			newArr.transferStatusDesc = value.transferStatusDesc;
																			newArr.fromWardId = value.fromWardId;
																			newArr.fromWardName = value.fromWardName;
																			newArr.transferStatusId = value.transferStatusId;
																			newArr.toBedCategoryId = value.toBedCategoryId;
																			newArr.toBedCategoryDesc = value.toBedCategoryDesc;
																			newArr.toWardId = value.toWardId;
																			newArr.toWardName = value.toWardName;
																			newArr.reasonDesc = value.reasonDesc;
																			newArr.adtRejectReason = value.adtRejectReason;
																			newArr.doctorReasonDesc = value.doctorReasonDesc;
																			newArr.doctorRejectReason = value.doctorRejectReason;
																			newArr.transferRequestId = value.transferRequestId;
																			newArr.toBedNumber = value.toBedNumber;
																			newArr.toBedId = value.toBedId;
																			newArr.doctorName = value.doctorName;
																			newArr.finalNote = value.finalNote;
																			newArr.fromRoomId = value.fromRoomId;
																			newArr.toRoomId = value.toRoomId;
																			newArr.fromFloorId = value.fromFloorId;
																			newArr.toFloorId = value.toFloorId;
																			newArr.toBillingBedCategoryId=value.toBillingBedCategoryId
																			newArr.remark = value.note;
																			newArr.fromNursingStationId = value.nursingStationId;
																			newArr.fromBedCategoryId = value.fromBedCategoryId;
																			newArr.fromBillingBedCategoryId = value.fromBillingBedCategoryId;
	                                                                        newArr.ageStr = value.ageStr;
	                                                                        newArr.imagePath = value.imagePath;
	                                                                        newArr.prefixDesc = value.prefixDesc;
	                                                                        newArr.prefixId = value.prefixId;
	                                                                        newArr.wardId = value.wardId;
                                                                            newArr.roomId = value.roomId;
                                                                            newArr.bedId= value.bedId;
//                                                                            newArr.createdDate = moment(value.createdDate,'DD/MM/YYYY').format($scope.dateFormat.toUpperCase());
                                                                            newArr.createdDate = value.createdDate;
                                                                            newArr.visitTypeId = value.visitTypeId;
																			$scope.listOfAdmittedPatientList.push(newArr);

																		});
														$rootScope.stopLoader();
													}else{
														$rootScope.stopLoader();
													}
												});
										}

									GenericService.serviceAction("POST", URI1, commonObjForTransfer).then(
													function(response) {
														if (response.data.status == "success") {
															console.log(response.data);
															$scope.commonListCount = response.data.object;
															$scope.setPage(1,false);

														}
													});
								}

								$scope.getTransferList = function(orgId,unitId,offset, noOfRecordsPerPage) {
									console.log("offset", offset);
									var data = "";
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;
									var URI = BASE_URL + ROOT_URL + GETFINALTRANSFERREQUESTLIST;
									
									var commonObjForTransfer = {
											unitId : $scope.unitId,
											organizationId : $scope.organizationId,
											wardList : $scope.nursingWardList,
											offset : offset,
											noOfRecordsPerPage : noOfRecordsPerPage,
											isFromNursingAcceptancePage:'Y'
											};
									
									
									if($scope.isShowPatientByBed=='Y'){
										if($scope.nurseBedList.length>0){
											commonObjForTransfer.bedList=$scope.nurseBedList;
											commonObjForTransfer.isShowPatientByBed='Y';
										}else{
										   $scope.nursingWardList=[];
										}
									}
									else{
										commonObjForTransfer.bedList=$scope.nurseBedList;
										commonObjForTransfer.isShowPatientByBed='N';
									}

									if($scope.nurseRole == 1 && $scope.nursingWardList.length > 0){
										
										var URI = BASE_URL + ROOT_URL+ GETFINALTRANSFERREQUESTLIST;
										$rootScope.startLoader();
								GenericService.serviceAction("POST", URI, commonObjForTransfer).then(
												function(response) {
													$scope.listOfAdmittedPatientList = [];
													$scope.admittedPatientList = [];

													if (response.data.status == "success") 
													{
														
														if(response.data.listObject.length==0){
															$scope.loadingMsg="No records found...!!";
															$rootScope.stopLoader();
														}else{
														$scope.admittedPatientList = response.data.listObject;
														angular.forEach($scope.admittedPatientList,
																		function(value,index) {

																			var newArr = {};
																			newArr.patientFullName = value.patientName;
																			newArr.dob = value.dob;
																			newArr.genderCode = value.genderCode;
																			newArr.uhidnumber = value.uhidnumber;
																			newArr.patientId = value.patientId;
																			newArr.tPatientId = value.tPatientId;
																			newArr.fromBedId = value.fromBedId;
																			newArr.fromBedNumber = value.fromBedNumber;
																			newArr.admissionId = value.admissionId;
																			newArr.transferId = value.transferId;
																			newArr.prefixCode = value.prefixCode;
																			newArr.fromBedCategoryId = value.fromBedCategoryId;
																			newArr.fromBedCategoryDesc = value.fromBedCategoryDesc;
																			newArr.doctorId = value.doctorId;
																			newArr.specialityId = value.specialityId;
																			newArr.admissionDetailsId = value.admissionDetailsId;
																			newArr.transferStatusDesc = value.transferStatusDesc;
																			newArr.fromWardId = value.fromWardId;
																			newArr.fromWardName = value.fromWardName;
																			newArr.transferStatusId = value.transferStatusId;
																			newArr.toBedCategoryId = value.toBedCategoryId;
																			newArr.toBedCategoryDesc = value.toBedCategoryDesc;
																			newArr.toWardId = value.toWardId;
																			newArr.toWardName = value.toWardName;
																			newArr.reasonDesc = value.reasonDesc;
																			newArr.adtRejectReason = value.adtRejectReason;
																			newArr.doctorReasonDesc = value.doctorReasonDesc;
																			newArr.doctorRejectReason = value.doctorRejectReason;
																			newArr.transferRequestId = value.transferRequestId;
																			newArr.toBedNumber = value.toBedNumber;
																			newArr.toBedId = value.toBedId;
																			newArr.doctorName = value.doctorName;
																			newArr.finalNote = value.finalNote;
																			newArr.fromRoomId = value.fromRoomId;
																			newArr.toRoomId = value.toRoomId;
																			newArr.fromFloorId = value.fromFloorId;
																			newArr.toFloorId = value.toFloorId;
																			newArr.toBillingBedCategoryId=value.toBillingBedCategoryId
																			newArr.remark = value.note;
																			newArr.fromNursingStationId = value.nursingStationId;
																			newArr.fromBedCategoryId = value.fromBedCategoryId;
																			newArr.fromBillingBedCategoryId = value.fromBillingBedCategoryId;
	                                                                        newArr.ageStr = value.ageStr;
	                                                                        newArr.imagePath = value.imagePath;
	                                                                        newArr.prefixDesc = value.prefixDesc;
	                                                                        newArr.prefixId = value.prefixId;
	                                                                        newArr.wardId = value.wardId;
                                                                            newArr.roomId = value.roomId;
                                                                            newArr.bedId= value.bedId;
                                                                            newArr.createdDate = value.createdDate;
                                                                            newArr.visitTypeId = value.visitTypeId;
																			$scope.listOfAdmittedPatientList.push(newArr);

																		});
															$rootScope.stopLoader();
														}
													}else{
														$rootScope.stopLoader();
													}
												});
										}
								}

								$scope.pager = {};
								$scope.page;

								$scope.setPage = function(page, flag) {
									if (page < 1 || page > $scope.pager.totalPages) {
										return;
									}
									$scope.pager = PagerService.GetPager($scope.commonListCount, page,$scope.noOfRecordsPerPage);
									if (flag) {
										$scope.getTransferList($scope.organizationId,$scope.unitId,$scope.pager.startIndex,$scope.pager.pageSize);
									}
								}

								$scope.initTransferList($scope.organizationId,$scope.unitId,$scope.offset, $scope.noOfRecordsPerPage);

							    	// For Paginations End
								
							}
							
							
							$scope.getAdmissionDataAccept = function(item) {
								
								$rootScope.itemSelectedsAccept = [];
								$rootScope.itemSelectedsAccept = item;
								console.log($rootScope.itemSelectedsAccept);

							}

							$scope.saveAcceptance = function() {
								$rootScope.startLoaderPopup()
								console.log($rootScope.itemSelectedsAccept);
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								
								var tansferNote = $('#noteTransfer').val();
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									transferId : $scope.itemSelectedsAccept.transferId,
									updatedBy :  $scope.userId,
									updatedDate : $scope.currentDate,
									transferRequestId : $scope.itemSelectedsAccept.transferRequestId,
									workstationNote : tansferNote,
									toBedId : $scope.itemSelectedsAccept.toBedId,
									admissionId : $scope.itemSelectedsAccept.admissionId,
									toFloorId : $scope.itemSelectedsAccept.toFloorId,
									toWardId : $scope.itemSelectedsAccept.toWardId,
									toRoomId : $scope.itemSelectedsAccept.toRoomId,
									admissionDetailsId : $scope.itemSelectedsAccept.admissionDetailsId,
									fromBedId : $scope.itemSelectedsAccept.fromBedId,
									doctorId : $scope.itemSelectedsAccept.doctorId,
									specialityId : $scope.doctorSpecialityId,
									toBedCategoryId: $scope.itemSelectedsAccept.toBedCategoryId,
									toBillingBedCategoryId:$scope.itemSelectedsAccept.toBillingBedCategoryId,
									nursingStationId:$scope.itemSelectedsAccept.fromNursingStationId, // it's for from nursingstation
									fromFloorId : $scope.itemSelectedsAccept.fromFloorId,
									fromWardId : $scope.itemSelectedsAccept.fromWardId,
									fromRoomId :$scope.itemSelectedsAccept.fromRoomId,
									skipHousekeeping : $scope.skipHousekeeping!=null ? $scope.skipHousekeeping : 'Y',
									newVisitTypeId : $scope.itemSelectedsAccept.newVisitTypeId !=undefined ?  $scope.itemSelectedsAccept.newVisitTypeId : $scope.itemSelectedsAccept.visitTypeId,
								};

								console.log(data);
								//return false;
								var URI = BASE_URL + ROOT_URL
										+ ACCEPTFINALTRANSFERREQUEST;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") 
													{
														$rootScope.stopLoaderPopup();
														if($scope.itemSelectedsAccept != undefined && $scope.itemSelectedsAccept.transferId != null){
															var data = {
																	"appName" : "HIS",
																	"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
																	"reportDesignFilePath" : "BedTransferPrint.rpt",
																	"reportFormat" : "PDF",
																	"exportFileName" : "BedTransferPrint",
																	"reportParams" : [ {
																		"paramName" : "transfer_id",
																		"paramType" : "Integer",
																		"paramValue" : $scope.itemSelectedsAccept.transferId

																	} ]
																};
																reportService.generate(REPORT_API_URI1, data);
														}
														
														growl
																.success(
																		'Request accepted successfully...!!',
																		{
																			title : 'Success!'
																		});
														//commonNote
														angular.element(
																'#commonNote')
																.modal('hide');

														$('#noteTransfer').val('');
														$scope.getTransferListTap();
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
							}

							
							//get visit Type List by toBillingBedCat
							$scope.visitTypeListByToBillingBedCat = [];
							$scope.oldVisitType = "";
							$scope.newVisitType = "";
							$scope.obj = {
									"selectedVisitType":""
							}; 
							
							$scope.closeNotePopUp = function(){
								var radio = document.getElementById('accept_ward_transfer_accept_rdobtn_ID');
					            radio.checked = false;
							}
							
							$scope.closeNotePopUpRejet = function(){
					        	var radio = document.getElementById('accept_ward_transfer_reject_rdobtn_ID');
					            radio.checked = false;
							}
							
							$scope.acceptedAdmittedPatient = function(){
					        	var radio = document.getElementById('admitted_patient_list_accept_rdobtn_ID');
					            radio.checked = false;
							}
							
							$scope.rejectedAdmittedPatient = function(){
					        	var radio = document.getElementById('admitted_patient_list_reject_rdobtn_ID');
					            radio.checked = false;
							}
							
							$scope.rejectIcuTransfer = function(){
					        	var radio = document.getElementById('reject_transfer_icu_request');
					            radio.checked = false;
							}
							//$scope.selectedVisitType="";
							$scope.getVisitTypeListByBillingBedCatId = function(info){
								$scope.getAdmissionDataAccept(info);
								
								if(info.fromBillingBedCategoryId == info.toBillingBedCategoryId){
									//open final transfer popup
									angular.element('#commonNote').modal('show');
								}else{
									$scope.visitTypeListByToBillingBedCat = [];
									$scope.oldVisitType = "";
									$scope.newVisitType = "";
									$scope.obj = {
											"selectedVisitType":""
									}; 
									if(info.visitTypeId!=null && info.visitTypeId == 1){
										$scope.oldVisitType ="OP"; 
									}else if(info.visitTypeId!=null && info.visitTypeId == 2){
										$scope.oldVisitType = "IP";
									}else if(info.visitTypeId!=null && info.visitTypeId == 3){
										$scope.oldVisitType = "DC";
									}else if(info.visitTypeId!=null && info.visitTypeId == 4){
										$scope.oldVisitType = "ER";
									}
									
									var data = {
											organizationId : $scope.organizationId,
											billingBedCategoryId : info.toBillingBedCategoryId
									};
									
									var URI = BASE_URL + ROOT_URL+"/adt/getVisitTypeListByBillingBedCategory";
									$rootScope.startLoader();
									GenericService.serviceAction("POST", URI, data)
									.then(
											function(response) {
												$scope.visitTypeListByToBillingBedCat = [];
												if (response.data.status == "success") 
												{
													$scope.visitTypeListByToBillingBedCat = response.data.listObject;
													$rootScope.stopLoader();
												}else{
													$rootScope.stopLoader();
												}
											});
									
									 $timeout( function(){
									if($scope.visitTypeListByToBillingBedCat!=null && $scope.visitTypeListByToBillingBedCat.length>1){
										angular.element('#viewVisitTypeListForUpdate').modal('show');
									}else if($scope.visitTypeListByToBillingBedCat!=null && $scope.visitTypeListByToBillingBedCat.length==1){
										//give alert
										if($scope.visitTypeListByToBillingBedCat[0].id!=info.visitTypeId){
											if($scope.visitTypeListByToBillingBedCat[0].id!=null && $scope.visitTypeListByToBillingBedCat[0].id == 1){
												$scope.newVisitType ="OP"; 
											}else if($scope.visitTypeListByToBillingBedCat[0].id!=null && $scope.visitTypeListByToBillingBedCat[0].id == 2){
												$scope.newVisitType = "IP";
											}else if($scope.visitTypeListByToBillingBedCat[0].id!=null && $scope.visitTypeListByToBillingBedCat[0].id == 3){
												$scope.newVisitType = "DC";
											}else if($scope.visitTypeListByToBillingBedCat[0].id!=null && $scope.visitTypeListByToBillingBedCat[0].id == 4){
												$scope.newVisitType = "ER";
											}
											angular.element('#alertForVisitTypeChange').modal('show');
										}else{
											angular.element('#commonNote').modal('show');
										}
										
									}
									 },3000);
								}
								
							}
							
							$scope.updateNewVisitType = function(){
								if($scope.visitTypeListByToBillingBedCat!=null && $scope.visitTypeListByToBillingBedCat.length>1){
									if($scope.obj.selectedVisitType == undefined || $scope.obj.selectedVisitType == null 
											|| $scope.obj.selectedVisitType =='' || $scope.obj.selectedVisitType == 0){
										
										growl.warning('Please select visit Type ',
												{
													title : 'Warning!'
												});
										return false;
									}
									if($rootScope.itemSelectedsAccept.visitTypeId != $scope.obj.selectedVisitType){
										$rootScope.itemSelectedsAccept.newVisitTypeId = $scope.obj.selectedVisitType;
									}
								}else{
									if($scope.visitTypeListByToBillingBedCat[0].id!=undefined){
										$scope.obj.selectedVisitType = $scope.visitTypeListByToBillingBedCat[0].id;
										$rootScope.itemSelectedsAccept.newVisitTypeId = $scope.obj.selectedVisitType;
									}
								}
//								if($scope.itemSelectedsAccept.visitTypeId == 4){
									var specDocObj = {
							    			unitId:$scope.unitId,
							    			organizationId:$scope.organizationId,
							    			doctorId:$scope.itemSelectedsAccept.doctorId
							    }
							    var URI = BASE_URL + ROOT_URL
								+ GETSPECIALITYLISTBYDOCTORID;
								BillingGenericService.serviceAction(METHOD_POST, URI,specDocObj,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											if (response.data.status == "success")
												{
													$scope.doctorSpecialityId = response.data.listObject[0].specialityId;
												}
										});
//								}
								$scope.openCommonNotePopup();
							}
							
							
							$scope.openCommonNotePopup = function(){
								angular.element('#commonNote').modal('show');
							
							}
							
							$scope.saveAcceptReject = function() {

								
								if($('#rejectReasonIdDrpDN').val()==undefined ||$('#rejectReasonIdDrpDN').val()==null || $('#rejectReasonIdDrpDN').val()=='')
								{
									growl.warning('Please select reason..!!',
											{
												title : 'Warning!'
											});
									return false;
								}
								
								console.log($rootScope.itemSelectedsAccept);
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');

								angular.element('#accepatReject').modal('hide');
								
								var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										transferId : $scope.itemSelectedsAccept.transferId,
										updatedBy : $scope.userId,
										updatedDate : $scope.currentDate,
										transferRequestId : $scope.itemSelectedsAccept.transferRequestId,
										toBedId : $scope.itemSelectedsAccept.toBedId,
										adtRejectReason :$('#noteRejectDrpDN').val(),
										adtReasonId : $('#rejectReasonIdDrpDN').val(),
										admissionId:$scope.itemSelectedsAccept.admissionId,
										fromBedCategoryId:$scope.itemSelectedsAccept.fromBedCategoryId,
										fromBillingBedCategoryId:$scope.itemSelectedsAccept.fromBillingBedCategoryId,
										fromWardId:$scope.itemSelectedsAccept.fromWardId,
										fromRoomId:$scope.itemSelectedsAccept.fromRoomId,
										fromBedId:$scope.itemSelectedsAccept.fromBedId

									};
								
								

								console.log(data);
								//	return false;
								var URI = BASE_URL + ROOT_URL +  REJECTTRANSFERREQUESTFORADT;
										//+ REJECTFINALTRANSFERREQUEST;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Rejected Successfully...!!',
																		{
																			title : 'Success!'
																		});
														//accepatReject
														
														$scope.getTransferListTap();
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
							}
							
//****************************************getting ICU TRANSFER list***************************************************************************
							$scope.getICUTransferList=function(){
								
							
								
								// For Paginations Start for nursing patient acceptance
								$scope.setNoOfRecords = function() {
									$scope.initICUTransferList($scope.organizationId,$scope.unitId, $scope.offset,$scope.noOfRecordsPerPage);
								};
								$scope.initICUTransferList = function(orgId,unitId,offset, noOfRecordsPerPage) {
									
									
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;
									
									var URI = BASE_URL + ROOT_URL +GETICUTRANSFERPATIENTLIST;
									var URI1 = BASE_URL + ROOT_URL+ "/adt/getICUTransferRequestListCount";
									
									var commonObjForTransfer = {
											unitId : $scope.unitId,
											organizationId : $scope.organizationId,
											wardList : $scope.nursingWardList,
											offset : offset,
											noOfRecordsPerPage : noOfRecordsPerPage,
											isFromNursingAcceptancePage:'Y'
											};
									
									
									if($scope.isShowPatientByBed=='Y'){
										if($scope.nurseBedList.length>0){
											commonObjForTransfer.bedList=$scope.nurseBedList;
											commonObjForTransfer.isShowPatientByBed='Y';
										}else{
										   $scope.nursingWardList=[];
										}
									}
									else{
										commonObjForTransfer.bedList=$scope.nurseBedList;
										commonObjForTransfer.isShowPatientByBed='N';
									}

									if($scope.nurseRole == 1 && $scope.nursingWardList.length > 0){
										
										var URI = BASE_URL + ROOT_URL+ GETICUTRANSFERPATIENTLIST;
										$rootScope.startLoader();
								GenericService.serviceAction("POST", URI, commonObjForTransfer).then(
												function(response) {
													$scope.transferICUList = [];

													if (response.data.status == "success") {
														$scope.transferICUList = response.data.listObject;
														angular.forEach($scope.transferICUList,function(value,index)
														{
														value.createdDate =moment(value.createdDate,'DD/MM/YYYY').format($scope.dateFormat.toUpperCase());
														});


														$rootScope.stopLoader();
													}else{
														$rootScope.stopLoader();
													}
												});
										}

									GenericService.serviceAction("POST", URI1, commonObjForTransfer).then(
													function(response) {
														if (response.data.status == "success") {
															console.log(response.data);
															$scope.commonListCount = response.data.object;
															$scope.setPage(1,false);

														}
													});
								}

								$scope.gettICUTransferList = function(orgId,unitId,offset, noOfRecordsPerPage) {
									console.log("offset", offset);
									var data = "";
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;
									var URI = BASE_URL + ROOT_URL + GETICUTRANSFERPATIENTLIST;
									
									var commonObjForTransfer = {
											unitId : $scope.unitId,
											organizationId : $scope.organizationId,
											wardList : $scope.nursingWardList,
											offset : offset,
											noOfRecordsPerPage : noOfRecordsPerPage,
											isFromNursingAcceptancePage:'Y'
											};
									
									
									if($scope.isShowPatientByBed=='Y'){
										if($scope.nurseBedList.length>0){
											commonObjForTransfer.bedList=$scope.nurseBedList;
											commonObjForTransfer.isShowPatientByBed='Y';
										}else{
										   $scope.nursingWardList=[];
										}
									}
									else{
										commonObjForTransfer.bedList=$scope.nurseBedList;
										commonObjForTransfer.isShowPatientByBed='N';
									}

									if($scope.nurseRole == 1 && $scope.nursingWardList.length > 0){
										
										var URI = BASE_URL + ROOT_URL+ GETICUTRANSFERPATIENTLIST;
										$rootScope.startLoader();
								 GenericService.serviceAction("POST", URI, commonObjForTransfer).then(
												function(response) {
													$scope.transferICUList = [];

													if (response.data.status == "success") {
														$scope.transferICUList = response.data.listObject;
														$rootScope.stopLoader();
													}else{
														$rootScope.stopLoader();
													}
												});
										}
								}

								$scope.pager = {};
								$scope.page;

								$scope.setPage = function(page, flag) {
									if (page < 1 || page > $scope.pager.totalPages) {
										return;
									}
									$scope.pager = PagerService.GetPager($scope.commonListCount, page,$scope.noOfRecordsPerPage);
									if (flag) {
										$scope.gettICUTransferList($scope.organizationId,$scope.unitId,$scope.pager.startIndex,$scope.pager.pageSize);
									}
								}

								$scope.initICUTransferList($scope.organizationId,$scope.unitId,$scope.offset, $scope.noOfRecordsPerPage);

							    	// For Paginations End
								
							}
							
							$scope.selectItem = function(item) {
								console.log(item);
								$scope.itemSelecteds = [];
								// If checkbox is checked
								if ($scope.patientIcuTransfer) {
									$scope.itemSelecteds = item;
								}
							}

							$scope.selectItemBed = function(item) {
							
								console.log(item);
								$scope.beditemSelecteds = [];
								// If checkbox is checked
								if ($scope.bedTr) {
									$scope.beditemSelecteds = item;
								}
							}

							$scope.selectedBedObject = function() {
								console.log($scope.beditemSelecteds);
								if($scope.beditemSelecteds.bedId!=null){
									
										growl
										.success(
												'Bed Allocated Successfully...!!',
												{
													title : 'Success!'
												});
										angular.element('#bedAllocationForTransfer').modal('hide');
								}else
									{
										growl
										.warning(
												'Please Selecte Bed..!!',
												{
													title : 'Warning!'
												});
										return;
									}
								
							}
							
							$scope.rejectRequest = function(item){
								
								$scope.rejectRequestForICUObject = item;

							}
							
							$scope.rejectRequestForICU = function(){
								
									$rootScope.startLoader();
									$scope.currentDate = new Date();
									$scope.currentDate = moment(
											$scope.currentDate).format(
											'DD-MM-YYYY HH:mm:ss');
									if($scope.rejectReasonId == "" ||  $scope.rejectReasonId == null || $scope.rejectReasonId == undefined){
										growl.warning(
												'Please select reason...!!',
												{
													title : 'Warning!'
												});
										$rootScope.stopLoader();
										return false;
									}
									
									
	
									var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										updatedBy : $scope.userId,
										updatedDate : $scope.currentDate,
										transferId : $scope.rejectRequestForICUObject.transferId,
										transferRequestId : $scope.rejectRequestForICUObject.transferRequestId,
										admissionId :  $scope.rejectRequestForICUObject.admissionId,
										admissionDetailsId : $scope.rejectRequestForICUObject.admissionDetailsId,
										oldTransferId : $scope.rejectRequestForICUObject.oldTransferId,
										icuRejectReasonId : $scope.rejectReasonId,
										icuRejectionNote : $scope.rejectionNote
									}
									
									console.log(data);
									//return false;
									var URI = BASE_URL
											+ ROOT_URL
											+ REJECTFINALTRANSFERREQUESTFORICU;
									GenericService
											.serviceAction("POST", URI,
													data)
											.then(
													function(response) {
	
														if (response.data.status == "success") {
															growl
																	.success(
																			'Transfer Request rejected successfully...!!',
																			{
																				title : 'Success!'
																			});
	//														$scope.init();
															$scope.getICUTransferList();
															$rootScope.stopLoader();
														} else {
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
							$scope.acceptRequest = function() {

								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.warning(
													'Please select atleast one patient...!!',
													{
														title : 'Warning!'
													});
								} else {
									if (Object.keys($scope.beditemSelecteds).length === 0) {
										growl
												.warning(
														'Please Allocate bed before accept...!!',
														{
															title : 'Warning!'
														});
									} else {
										if ($scope.popUpFlag) {
											$scope.showModal = !$scope.showModal;

										} else {
											$rootScope.startLoader();
											$scope.currentDate = new Date();
											$scope.currentDate = moment(
													$scope.currentDate).format(
													'DD-MM-YYYY HH:mm:ss');

											var data = {
												organizationId : $scope.organizationId,
												unitId : $scope.unitId,
												toBedCategoryId : $scope.beditemSelecteds.bedCategoryId,
												toBedId : $scope.beditemSelecteds.bedId,
												toWardId : $scope.beditemSelecteds.wardId,
												toRoomId : $scope.beditemSelecteds.roomId,
												toBillingBedCategoryId : $scope.beditemSelecteds.billingBedCategoryId,
												updatedBy : $scope.userId,
												updatedDate : $scope.currentDate,
												transferId : $scope.itemSelecteds.transferId,
												transferRequestId : $scope.itemSelecteds.transferRequestId,
												transferTypeId : 3,
												admissionId :  $scope.itemSelecteds.admissionId,
												toFloorId : $scope.beditemSelecteds.floorId,
												fromBedId : $scope.itemSelecteds.fromBedId,
												admissionDetailsId : $scope.itemSelecteds.admissionDetailsId,
												toBedCategoryId: $scope.beditemSelecteds.bedCategoryId,
												toBillingBedCategoryId:$scope.beditemSelecteds.billingBedCategoryId,
												skipHousekeeping : $scope.skipHousekeeping!=null ? $scope.skipHousekeeping : 'Y'
											}
											console.log(data);
											//return false;
											var URI = BASE_URL
													+ ROOT_URL
													+ ACCEPTFINALTRANSFERREQUEST;
											GenericService
													.serviceAction("POST", URI,
															data)
													.then(
															function(response) {

																if (response.data.status == "success") {
																	growl
																			.success(
																					'Transfer Request accepted...!!',
																					{
																						title : 'Success!'
																					});
//																	$scope.init();
                                                                    $scope.getAdmittedPatientList();
                                                                    $('#noteTransfer').val('');
																	$scope.getICUTransferList();
																	$rootScope.stopLoader();
																} else {
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
									}

								}

							}

							//For ICU End
//*******************************************GETTING ADMITTED PATIENT LIST***********************************************************************
							$scope.getAdmittedPatientList=function(){
							
								
								
								// For Paginations Start for nursing patient acceptance
								$scope.setNoOfRecords = function() {
									$scope.initAdmittedPatientList($scope.organizationId,$scope.unitId, $scope.offset,$scope.noOfRecordsPerPage);
								};
								$scope.initAdmittedPatientList = function(orgId,unitId,offset, noOfRecordsPerPage) {
									
									
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;
									
									var URI = BASE_URL + ROOT_URL +"/adt/getAdmittedPatientListForIPDNursingWorklistForNursingPage";
									var URI1 = BASE_URL + ROOT_URL+ "/adt/getAdmittedPatientListForIPDNursingWorklistForNursingPageCount";
									
									var commonObjForTransfer = {
											unitId : $scope.unitId,
											organizationId : $scope.organizationId,
											wardList : $scope.nursingWardList,
											offset : offset,
											noOfRecordsPerPage : noOfRecordsPerPage,
											visitTypeId : parseInt($scope.visitTypeIdflag) ,
//											isFromNursingAcceptancePage:'Y'
											};
									
									
									if($scope.isShowPatientByBed=='Y'){
										if($scope.nurseBedList.length>0){
											commonObjForTransfer.bedList=$scope.nurseBedList;
											commonObjForTransfer.isShowPatientByBed='Y';
										}else{
										   $scope.nursingWardList=[];
										}
									}
									else{
										commonObjForTransfer.bedList=$scope.nurseBedList;
										commonObjForTransfer.isShowPatientByBed='N';
									}

									if($scope.nurseRole == 1 && $scope.nursingWardList.length > 0){
										
										var URI = BASE_URL + ROOT_URL+ "/adt/getAdmittedPatientListForIPDNursingWorklistForNursingPage";
										$rootScope.startLoader();
								GenericService
										.serviceAction("POST", URI, commonObjForTransfer)
										.then(
												function(response) {
													$scope.listOfAdmittedPatientListforAddmitted = [];
													$scope.admittedPatientList = [];

													if (response.data.status == "success") {
														
														if(response.data.listObject.length==0)
															$scope.loadingMsg="No records found...!!";
														$scope.admittedPatientList = response.data.listObject;
														angular.forEach($scope.admittedPatientList,
																		function(value,index) {

																			var newArr = {};
																			newArr.specialityName = value.specialityName;
																			newArr.doctorFullName = value.dFirstName;
																			newArr.pFirstName = value.pFirstName;
																			newArr.reasonDesc = value.reasonDesc;
																			newArr.dob = value.dob;
																			newArr.datepicker = moment(value.doa,'DD/MM/YYYY').format($scope.dateFormat.toUpperCase());
																			newArr.datepickerPdd = moment(value.pdd,'DD/MM/YYYY').format($scope.dateFormat.toUpperCase());
																			newArr.genderCode = value.genderCode;
																			newArr.genderId = value.genderId;
																			newArr.doctorId = value.doctorId;
																			newArr.specialityId = value.specialityId;
																			newArr.uhidnumber = value.uhidnumber;
																			newArr.createdTime = value.createdTime;
																			newArr.admissionNumber = value.admissionNumber;
																			newArr.patientId = value.patientId;
																			newArr.tPatientId = value.tPatientId;
																			newArr.paymentEntitlementDesc = value.paymentEntitlementDesc;
																			newArr.bedNumber = value.bedNumber;
																			newArr.admissionId = value.admissionId;
																			newArr.prefixCode = value.prefixCode;
																			newArr.identificationNo = value.identificationNo;
																			newArr.wardName = value.wardName;
																			newArr.concent = value.concent;
																			newArr.bedCategoryId=value.bedCategoryId;
																			newArr.patientCategoryId=value.patientCategoryId;
																			newArr.visitTypeId=value.visitTypeId;
																			newArr.paymentEntitlementId=value.paymentEntitlementId;
																			newArr.contractId = value.contractId;
																			newArr.dischargeStatusId=value.dischargeStatusId;
																			newArr.billingBedCategoryId = value.billingBedCategoryId;
																			newArr.isCriticalOrder = value.isCriticalOrder;
																			newArr.medicoLegalId = value.medicoLegalId;
																			newArr.isVip = value.isVip;
                                                                            newArr.dischargeId = value.dischargeId;
                                                                            newArr.ageStr = value.ageStr;
                                                                            if(value.imagePath!=undefined && value.imagePath!=null && value.imagePath!="" &&  value.bucketFlag!="" && value.bucketFlag==2){
                                                                                newArr.imagePath = "data:image/png;base64,"+value.imagePath;
                                                                            }else{
                                                                                newArr.imagePath = value.imagePath;
                                                                            }
                                                                            newArr.prefixDesc = value.prefixDesc;
                                                                            newArr.prefixId = value.prefixId;
                                                                            newArr.ageStr = value.ageStr;
                                                                            newArr.visitTypeId= value.visitTypeId;
                                                                            newArr.wardId = value.wardId;
                                                                            newArr.floorName = value.floorName;
                                                                            newArr.wardName = value.wardName;
                                                                            newArr.roomId = value.roomId;
                                                                            newArr.bedId= value.bedId;
                                                                            newArr.uuid= value.uuid;
                                                                            newArr.isIcuu= value.isIcuu;
                                                                            newArr.isFinalBill= value.isFinalBill;
                                                                            newArr.referenceNumber= value.referenceNumber;
                                                                            console.log("visitTypeData",value.visitTypeId);
                                                                            //ip
                                                                            if($scope.visitTypeIdflag == "2" ){
																			if(value.visitTypeId==2){
																			$scope.listOfAdmittedPatientListforAddmitted .push(newArr);
																			console.log("list data",$scope.listOfAdmittedPatientListforAddmitted );
																			}
                                                                            }
                                                                            //day care
                                                                            if($scope.visitTypeIdflag == "3" ){
    																			if(value.visitTypeId==3){
    																			$scope.listOfAdmittedPatientListforAddmitted .push(newArr);
    																			console.log("list data",$scope.listOfAdmittedPatientListforAddmitted );
    																			}
                                                                                }
                                                                            
																		});
														$rootScope.stopLoader();
													}else{
														$rootScope.stopLoader();
													}
												});
										}

									GenericService.serviceAction("POST", URI1, commonObjForTransfer).then(
													function(response) {
														if (response.data.status == "success") {
															console.log(response.data);
															$scope.commonListCount = response.data.object;
															$scope.setPage(1,false);

														}
													});
								}
								
							
								
								$scope.gettAdmittedPatientList = function(orgId,unitId,offset, noOfRecordsPerPage) {
									console.log("offset", offset);
									var data = "";
									offset = offset != null ? offset : 0;
									noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;
									var URI = BASE_URL + ROOT_URL +  "/adt/getAdmittedPatientListForIPDNursingWorklistForNursingPage";
									
									var commonObjForTransfer = {
											unitId : $scope.unitId,
											organizationId : $scope.organizationId,
											wardList : $scope.nursingWardList,
											offset : offset,
											noOfRecordsPerPage : noOfRecordsPerPage,
											visitTypeId : parseInt($scope.visitTypeIdflag) ,
//											isFromNursingAcceptancePage:'Y'
											};
									
									
									if($scope.isShowPatientByBed=='Y'){
										if($scope.nurseBedList.length>0){
											commonObjForTransfer.bedList=$scope.nurseBedList;
											commonObjForTransfer.isShowPatientByBed='Y';
										}else{
										   $scope.nursingWardList=[];
										}
									}
									else{
										commonObjForTransfer.bedList=$scope.nurseBedList;
										commonObjForTransfer.isShowPatientByBed='N';
									}

									if($scope.nurseRole == 1 && $scope.nursingWardList.length > 0){
										
										var URI = BASE_URL + ROOT_URL+  "/adt/getAdmittedPatientListForIPDNursingWorklistForNursingPage";
										$rootScope.startLoader();
								GenericService
										.serviceAction("POST", URI, commonObjForTransfer)
										.then(
												function(response) {
													$scope.listOfAdmittedPatientListforAddmitted = [];
													$scope.admittedPatientList = [];

													if (response.data.status == "success") {
														$scope.admittedPatientList = response.data.listObject;
														angular.forEach($scope.admittedPatientList,
																		function(value,index) {

																			var newArr = {};
																			newArr.specialityName = value.specialityName;
																			newArr.doctorFullName = value.dFirstName;
																			newArr.pFirstName = value.pFirstName;
																			newArr.reasonDesc = value.reasonDesc;
																			newArr.dob = value.dob;
																			newArr.datepicker = value.doa;
																			newArr.datepickerPdd = value.pdd;
																			newArr.genderCode = value.genderCode;
																			newArr.genderId = value.genderId;
																			newArr.doctorId = value.doctorId;
																			newArr.specialityId = value.specialityId;
																			newArr.uhidnumber = value.uhidnumber;
																			newArr.createdTime = value.createdTime;
																			newArr.admissionNumber = value.admissionNumber;
																			newArr.patientId = value.patientId;
																			newArr.tPatientId = value.tPatientId;
																			newArr.paymentEntitlementDesc = value.paymentEntitlementDesc;
																			newArr.bedNumber = value.bedNumber;
																			newArr.admissionId = value.admissionId;
																			newArr.prefixCode = value.prefixCode;
																			newArr.identificationNo = value.identificationNo;
																			newArr.wardName = value.wardName;
																			newArr.concent = value.concent;
																			newArr.bedCategoryId=value.bedCategoryId;
																			newArr.patientCategoryId=value.patientCategoryId;
																			newArr.visitTypeId=value.visitTypeId;
																			newArr.paymentEntitlementId=value.paymentEntitlementId;
																			newArr.contractId = value.contractId;
																			newArr.dischargeStatusId=value.dischargeStatusId;
																			newArr.billingBedCategoryId = value.billingBedCategoryId;
																			newArr.medicoLegalId = value.medicoLegalId;
																			newArr.dischargeId = value.dischargeId;
																			newArr.isCriticalOrder = value.isCriticalOrder;
																			newArr.isVip = value.isVip;
																			newArr.ageStr = value.ageStr;
	                                                                        newArr.imagePath = value.imagePath;
	                                                                        newArr.prefixDesc = value.prefixDesc;
	                                                                        newArr.prefixId = value.prefixId;
	                                                                        newArr.ageStr = value.ageStr;
	                                                                        newArr.wardId = value.wardId;
	                                                                        newArr.floorName = value.floorName;
                                                                            newArr.wardName = value.wardName;
                                                                            newArr.roomId = value.roomId;
                                                                            newArr.bedId= value.bedId;
                                                                            newArr.uuid= value.uuid;
                                                                            newArr.referenceNumber= value.referenceNumber;
                                                                            
//																			if(value.visitTypeId==2){
																			$scope.listOfAdmittedPatientListforAddmitted.push(newArr);
																			
//																			}

																		});
														$rootScope.stopLoader();
													}else{
														$rootScope.stopLoader();
													}
												});
										}
								}

								$scope.pager = {};
								$scope.page;

								$scope.setPage = function(page, flag) {
									if (page < 1 || page > $scope.pager.totalPages) {
										return;
									}
									$scope.pager = PagerService.GetPager($scope.commonListCount, page,$scope.noOfRecordsPerPage);
									if (flag) {
										$scope.gettAdmittedPatientList($scope.organizationId,$scope.unitId,$scope.pager.startIndex,$scope.pager.pageSize);
									}
								}

								$scope.initAdmittedPatientList($scope.organizationId,$scope.unitId,$scope.offset, $scope.noOfRecordsPerPage);

							    	// For Paginations End
							}
							
							$scope.firstTabLevel = 4 ;
							$scope.setFirstTabLevel = function(newTab) {
								$scope.firstTabLevel = newTab;
								if (newTab == '1') {
									
									$scope.getNewAdmissionList();
								}
								if (newTab == '2') {
									
									$scope.getTransferListTap();
								}
								if (newTab == '3') {
									
									$scope.getICUTransferList();
								}
								if (newTab == '4') {
									
									$scope.getAdmittedPatientList();
								}
							};

							$scope.isSetFirstTabLevel = function(tabNum) {
								return $scope.firstTabLevel === tabNum;
								
							};
							
							
							/* init() function for form object create. */
							$scope.init = function() 
							{
								
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.storeId = cookieObject.storeId;
								
								var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
								 if(cookieObjectAppConfig == undefined){
								  $state.go('login');
								  return;
								 }
								 else
								  {
								   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
								   
								  }
								 
									/** Variables for pagination */
									$scope.offset = 0;
									$scope.noOfRecordsPerPage = 10;
									$scope.totalNoOfRecords;
									$scope.searchKeyword;
									$scope.commonListCount;
								 
								$scope.roleName = cookieObjectAppConfig.roleName;
								$scope.nurseRole = cookieObjectAppConfig.nurseRole;	 
								$scope.doctorRole = cookieObjectAppConfig.doctorRole;
								$scope.skipAdmissionAcceptance = cookieObjectAppConfig.skipAdmissionAcceptance;
								$scope.doctorRole = cookieObjectAppConfig.doctorRole;
								
								$scope.isShowPatientByBed=cookieObjectAppConfig.isShowPatientByBed;
								
								$scope.nursingWardList = [];
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
								   $scope.nursingWardList =nursingStationsWardsList; 
								  
								   console.log('nursingWardList',$scope.nursingWardList);
								   
								  }
								
								    $scope.nurseBedList = [];
									var mappedNurseBedList = JSON.parse(sessionStorage.getItem('mappedNurseBedList'));
									 if( (mappedNurseBedList == undefined || mappedNurseBedList.length==0) && $scope.isShowPatientByBed=='Y'){
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
								$scope.bedTr = {};
								$scope.patientIcuTransfer = {};
								$scope.beditemSelecteds = {};

								var data = {};
								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId,
								};
								
								var commonObjForTransfer = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										wardList : $scope.nursingWardList
										};
								
								
								if($scope.isShowPatientByBed=='Y'){
									if($scope.nurseBedList.length>0){
										commonObjForTransfer.bedList=$scope.nurseBedList;
										commonObjForTransfer.isShowPatientByBed='Y';
									}else{
									   $scope.nursingWardList=[];
									}
								}
								else{
									commonObjForTransfer.bedList=$scope.nurseBedList;
									commonObjForTransfer.isShowPatientByBed='N';
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
								
								//$scope.getNewAdmissionList();
								$scope.getAdmittedPatientList();
							}
							
							$scope.init();
							
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
							
							
							/**************************************Ot Reservation Cancel START*****************************/
							
							$scope.currentIdx = '';
							$scope.getIndex = function(indx){
									$scope.currentIdx = indx;
									$("#confirmCancellation").modal("show");
							}
							$scope.patientId ="";
							$scope.cancelOTReservation = function(){
								try {
									$scope.startLoader();
									$scope.patientId = $scope.otReservationList[$scope.currentIdx].patientId;
									var data = {
											"proReqId":$scope.otReservationList[$scope.currentIdx].proReqId,
											"reasonId":$scope.otReservationList[$scope.currentIdx].reasonId,
											"reasonNote":$scope.otReservationList[$scope.currentIdx].reasonNote
										};
									var URI = BASE_URL + ROOT_URL+"/api/otims/OTReservation/cancelOTReservation";
									console.log("URI", URI);
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {
														if (response.data.status == "success")
														{
															$scope.stopLoader();
																growl.success("Reservation Cancelled Successfully.",
																		{
																			title : 'Success!'
																		});
																$("#otReservationDetails").modal("hide");
																$scope.getOTReservationList($scope.patientId);
															} else if(response.data.status == 'error')
															{
																$scope.stopLoader();
																growl.error(response.data.message,
																		{
																			title : 'Error!'
																		});
														}
													});
									
								} catch (e) {
									console.log(e.message)
									$scope.stopLoader();
								}

							}
							
							$scope.cancelledSelectedItem = function(flag){
								
								angular.forEach($scope.otReservationList ,function(value,index){
									value.isSelected = false;
									
								});
								
								 if( $scope.encounterFlag == true){
										$scope.checkPendingServicesOrder();
									}else{
										
										angular.element('#acceptForPopup').modal('show');
									}
								
							}
							
							$scope.checkForValidReservation = function(info){
								if(info.isSelected){
								if(info.scheduleDate<moment(new Date()).format('DD/MM/YYYY')){
									growl.warning("Procedure has been Expired,Please Reschedule Again",{
										title : WARNING_MSG
										});
								}
								}
							}
							/*********************************Ot Reservation cancel END*******************************/

							$scope.loadingMsg="";
							$scope.universalSearchForAdmittedPatientList = function(searchString) {
								$scope.listOfAdmittedPatientListforAddmitted = [];
								$scope.admittedPatientList = [];
								$scope.loadingMsg="Loading...";
								if(searchString!=''){
								var commonObjForTransfer = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										wardList : $scope.nursingWardList,
										offset : $scope.offset,
										noOfRecordsPerPage : $scope.noOfRecordsPerPage,
										patientName : searchString,
                                        visitTypeId : parseInt($scope.visitTypeIdflag) 
										};
								
								if($scope.isShowPatientByBed=='Y'){
									if($scope.nurseBedList.length>0){
										commonObjForTransfer.bedList=$scope.nurseBedList;
										commonObjForTransfer.isShowPatientByBed='Y';
									}else{
									   $scope.nursingWardList=[];
									}
								}
								else{
									commonObjForTransfer.bedList=$scope.nurseBedList;
									commonObjForTransfer.isShowPatientByBed='N';
								}

								if($scope.nurseRole == 1 && $scope.nursingWardList.length > 0){
									
									var URI = BASE_URL + ROOT_URL+  "/adt/admittedPatientListUniversalSearch";
									$rootScope.startLoader();
							GenericService
									.serviceAction("POST", URI, commonObjForTransfer)
									.then(
											function(response) {
												$scope.admittedPatientList=[];
												$scope.listOfAdmittedPatientListforAddmitted=[];
												if (response.data.status == "success") {
													if(response.data.listObject.length==0){
														$scope.loadingMsg="No records found...!!";
														$rootScope.stopLoader();
													}else{
													$scope.admittedPatientList = response.data.listObject;
													angular.forEach($scope.admittedPatientList,
																	function(value,index) {

																		var newArr = {};
																		newArr.specialityName = value.specialityName;
																		newArr.doctorFullName = value.dFirstName;
																		newArr.pFirstName = value.pFirstName;
																		newArr.reasonDesc = value.reasonDesc;
																		newArr.dob = value.dob;
//																		newArr.datepicker = value.doa;
																		newArr.datepicker = moment(value.doa,'DD/MM/YYYY').format($scope.dateFormat.toUpperCase());
																		newArr.datepickerPdd = moment(value.pdd,'DD/MM/YYYY').format($scope.dateFormat.toUpperCase());
																		newArr.createdTime = value.createdTime;
																		newArr.admissionNumber = value.admissionNumber;
//																		newArr.datepickerPdd = value.pdd;
																		newArr.genderCode = value.genderCode;
																		newArr.genderId = value.genderId;
																		newArr.doctorId = value.doctorId;
																		newArr.specialityId = value.specialityId;
																		newArr.uhidnumber = value.uhidnumber;
																		newArr.patientId = value.patientId;
																		newArr.tPatientId = value.tPatientId;
																		newArr.paymentEntitlementDesc = value.paymentEntitlementDesc;
																		newArr.bedNumber = value.bedNumber;
																		newArr.admissionId = value.admissionId;
																		newArr.prefixCode = value.prefixCode;
																		newArr.identificationNo = value.identificationNo;
																		newArr.wardName = value.wardName;
																	    newArr.floorName = value.floorName;
																		newArr.concent = value.concent;
																		newArr.bedCategoryId=value.bedCategoryId;
																		newArr.patientCategoryId=value.patientCategoryId;
																		newArr.visitTypeId=value.visitTypeId;
																		newArr.paymentEntitlementId=value.paymentEntitlementId;
																		newArr.contractId = value.contractId;
																		newArr.dischargeStatusId=value.dischargeStatusId;
																		newArr.billingBedCategoryId = value.billingBedCategoryId;
																		newArr.medicoLegalId = value.medicoLegalId;
																		newArr.dischargeId=value.dischargeId;
																		newArr.prefixDesc = value.prefixDesc;
																	    newArr.imagePath = value.imagePath;
																		newArr.ageStr = value.ageStr;
																		newArr.wardId = value.wardId;
                                                                        newArr.roomId = value.roomId;
                                                                        newArr.bedId= value.bedId;
                                                                        newArr.uuid= value.uuid;
                                                                        newArr.isIcuu= value.isIcuu;
                                                                        newArr.isFinalBill= value.isFinalBill;
                                                                    	newArr.isCriticalOrder = value.isCriticalOrder;
																		newArr.isVip = value.isVip;
                                                                        newArr.referenceNumber= value.referenceNumber;
//																		if(value.visitTypeId==2){
																		$scope.listOfAdmittedPatientListforAddmitted .push(newArr);
//																		}
																		
																	});
													}
													$rootScope.stopLoader();
												}else{
													$rootScope.stopLoader();
													$scope.loadingMsg="No records found...!!";
												}
											});
									}
							}else{
								$scope.initAdmittedPatientList($scope.organizationId,$scope.unitId, $scope.offset,$scope.noOfRecordsPerPage);
							}
						}

							
							$scope.universalSearchForNursingAcceptanceList = function(searchString) {
								$scope.loadingMsg="Loading...";
								$scope.newAdmittedPatientList = [];
								if(searchString!=''){
								var commonObjForTransfer = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										wardList : $scope.nursingWardList,
										offset : $scope.offset,
										noOfRecordsPerPage : $scope.noOfRecordsPerPage,
										isFromNursingAcceptancePage:'Y',
										patientName:searchString
										};
								
								if($scope.isShowPatientByBed=='Y'){
									if($scope.nurseBedList.length>0){
										commonObjForTransfer.bedList=$scope.nurseBedList;
										commonObjForTransfer.isShowPatientByBed='Y';
									}else{
									   $scope.nursingWardList=[];
									}
								}
								else{
									commonObjForTransfer.bedList=$scope.nurseBedList;
									commonObjForTransfer.isShowPatientByBed='N';
								}

								if($scope.nurseRole == 1 && $scope.nursingWardList.length > 0){
									
									var URI = BASE_URL + ROOT_URL
									+"/adt/nursing/patient/universalSearchForNursingAcceptance";
									$rootScope.startLoader();
									GenericService.serviceAction("POST", URI, commonObjForTransfer)
									.then(
											function(response) {
												if (response.data.status == "success") {
													if(response.data.listObject.length==0){
														$scope.loadingMsg="No records found...!!";
														$rootScope.stopLoader();
													}else{
													$scope.newAdmittedPatientList = response.data.listObject;
													console.log("MY NewAdmitted List is33:"+$scope.newAdmittedPatientList);
													$rootScope.stopLoader();
													}
												}else{
													$rootScope.stopLoader();
												}
											});
									}
								}else{
									$scope.initNursingAcceptanceList($scope.organizationId,$scope.unitId, $scope.offset,$scope.noOfRecordsPerPage);
								}
							}
							
							$scope.universalSearchForTransferList = function(searchString) {
								$scope.loadingMsg="Loading...";
								
								$scope.admittedPatientList = [];
								if(searchString!=''){
								var commonObjForTransfer = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										wardList : $scope.nursingWardList,
										offset : $scope.offset,
										noOfRecordsPerPage : $scope.noOfRecordsPerPage,
										isFromNursingAcceptancePage:'Y',
										patientName:searchString
										};
								
								if($scope.isShowPatientByBed=='Y'){
									if($scope.nurseBedList.length>0){
										commonObjForTransfer.bedList=$scope.nurseBedList;
										commonObjForTransfer.isShowPatientByBed='Y';
									}else{
									   $scope.nursingWardList=[];
									}
								}
								else{
									commonObjForTransfer.bedList=$scope.nurseBedList;
									commonObjForTransfer.isShowPatientByBed='N';
								}

								if($scope.nurseRole == 1 && $scope.nursingWardList.length > 0){
									
									var URI = BASE_URL + ROOT_URL+ "/adt/universalSearchForTransferRequestList";
									$rootScope.startLoader();
							GenericService.serviceAction("POST", URI, commonObjForTransfer).then(
											function(response) {
												if (response.data.status == "success") {
													if(response.data.listObject.length==0){
														$scope.loadingMsg="No records found...!!";
														$scope.listOfAdmittedPatientList =[];
														$rootScope.stopLoader();
													}else{
													$scope.admittedPatientList = response.data.listObject;
													$scope.listOfAdmittedPatientList = [];
													angular.forEach($scope.admittedPatientList,
																	function(value,index) {

																		var newArr = {};
																		newArr.patientFullName = value.patientName;
																		newArr.dob = value.dob;
																		newArr.genderCode = value.genderCode;
																		newArr.uhidnumber = value.uhidnumber;
																		newArr.patientId = value.patientId;
																		newArr.tPatientId = value.tPatientId;
																		newArr.fromBedId = value.fromBedId;
																		newArr.fromBedNumber = value.fromBedNumber;
																		newArr.admissionId = value.admissionId;
																		newArr.transferId = value.transferId;
																		newArr.prefixCode = value.prefixCode;
																		newArr.fromBedCategoryId = value.fromBedCategoryId;
																		newArr.fromBedCategoryDesc = value.fromBedCategoryDesc;
																		newArr.doctorId = value.doctorId;
																		newArr.admissionDetailsId = value.admissionDetailsId;
																		newArr.transferStatusDesc = value.transferStatusDesc;
																		newArr.fromWardId = value.fromWardId;
																		newArr.fromWardName = value.fromWardName;
																		newArr.transferStatusId = value.transferStatusId;
																		newArr.toBedCategoryId = value.toBedCategoryId;
																		newArr.toBedCategoryDesc = value.toBedCategoryDesc;
																		newArr.toWardId = value.toWardId;
																		newArr.toWardName = value.toWardName;
																		newArr.reasonDesc = value.reasonDesc;
																		newArr.adtRejectReason = value.adtRejectReason;
																		newArr.doctorReasonDesc = value.doctorReasonDesc;
																		newArr.doctorRejectReason = value.doctorRejectReason;
																		newArr.transferRequestId = value.transferRequestId;
																		newArr.toBedNumber = value.toBedNumber;
																		newArr.toBedId = value.toBedId;
																		newArr.doctorName = value.doctorName;
																		newArr.finalNote = value.finalNote;
																		newArr.fromRoomId = value.fromRoomId;
																		newArr.toRoomId = value.toRoomId;
																		newArr.fromFloorId = value.fromFloorId;
																		newArr.toFloorId = value.toFloorId;
																		newArr.toBillingBedCategoryId=value.toBillingBedCategoryId
																		newArr.remark = value.note;
																		newArr.fromNursingStationId = value.nursingStationId;
																		newArr.fromBedCategoryId = value.fromBedCategoryId;
																		newArr.fromBillingBedCategoryId = value.fromBillingBedCategoryId;
                                                                        newArr.ageStr = value.ageStr;
                                                                        newArr.imagePath = value.imagePath;
                                                                        newArr.prefixDesc = value.prefixDesc;
                                                                        newArr.prefixId = value.prefixId;
                                                                        newArr.wardId = value.wardId;
                                                                        newArr.roomId = value.roomId;
                                                                        newArr.bedId= value.bedId;
                                                                        newArr.createdDate = value.createdDate;
                                                                        newArr.visitTypeId = value.visitTypeId;
																		$scope.listOfAdmittedPatientList.push(newArr);

																	});
													}
													$rootScope.stopLoader();
												}else{
													$rootScope.stopLoader();
												}
											});
									}
								}else{
									$scope.initTransferList($scope.organizationId,$scope.unitId, $scope.offset,$scope.noOfRecordsPerPage);
								}
							}
							
							$scope.universalSearchForICUTransferList = function(searchString) {
								$scope.loadingMsg="Loading...";
								
								if(searchString!=''){
								var commonObjForTransfer = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										wardList : $scope.nursingWardList,
										offset : $scope.offset,
										noOfRecordsPerPage : $scope.noOfRecordsPerPage,
										isFromNursingAcceptancePage:'Y',
										patientName : searchString
										};
								
								
								if($scope.isShowPatientByBed=='Y'){
									if($scope.nurseBedList.length>0){
										commonObjForTransfer.bedList=$scope.nurseBedList;
										commonObjForTransfer.isShowPatientByBed='Y';
									}else{
									   $scope.nursingWardList=[];
									}
								}
								else{
									commonObjForTransfer.bedList=$scope.nurseBedList;
									commonObjForTransfer.isShowPatientByBed='N';
								}

								if($scope.nurseRole == 1 && $scope.nursingWardList.length > 0){
									
									var URI = BASE_URL + ROOT_URL + "/adt/universalSearchForICUTransferRequestList";
									$rootScope.startLoader();
							 GenericService.serviceAction("POST", URI, commonObjForTransfer).then(
											function(response) {
												if (response.data.status == "success") {
													if(response.data.listObject.length==0){
														$scope.loadingMsg="No records found...!!";
														$scope.transferICUList = [];
														$rootScope.stopLoader();
													}else{
														$scope.transferICUList = [];
														$scope.transferICUList = response.data.listObject;
														$rootScope.stopLoader();
													}
												}else{
													$rootScope.stopLoader();
												}
											});
									}
								}else{
									$scope.initICUTransferList($scope.organizationId,$scope.unitId,$scope.offset, $scope.noOfRecordsPerPage);
								}
							}
/**************************Due For Discharge****************************************************************************************/
							
							$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate: new Date()
								};
							$scope.openDatePicker = function() {
								$scope.datepicker.opened = true;
							};

							$scope.datepicker = {
								opened : false
							};
								
								
							$scope.getDischargeTypeList=function(){
								var dataDisc = {
										organizationId : $scope.organizationId
								};
								
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEDISCHARGETYPELIST;
						GenericService
								.serviceAction("POST", URI, dataDisc)
								.then(
										function(response) {
											$scope.dischargeTypeList = [];

											if (response.data.status == "success") {
												$scope.dischargeTypeList = response.data.listObject;

											}
										});
							}
							$scope.getDischargeTypeList();
							
						/*	$scope.checkDatePdd = function(){
								var new_date = new Date();
								 if(!angular.isDate($scope.datepicker)){
									 growl.warning("Pdd is invalid",{
											title : WARNING_MSG
										});
									 $scope.datepicker = new Date();
										return false;
								 }else if(new_date > $scope.datepicker)
									 {
									 growl
										.warning(
												"PDD should be equal or greater then today's date",
												{
													title : 'Warning!'
												});
									 $scope.datepicker = new Date();
										return false;
									 }
							}*/
							
							$scope.checkDatePdd = function(){
								var new_date = new Date();
								var new_date1=moment(new_date).format("DD-MMM-YYYY");
								var datepicker1=moment($scope.datepicker).format("DD-MMM-YYYY");
								 if(!datepicker1){
									 growl.warning("Pdd is invalid",{
											title : WARNING_MSG
										});
									 $scope.datepicker = new Date();
										return false;
								 }else if(new_date1 > datepicker1)
									 {
									 growl
										.warning(
												"PDD should be equal or greater then today's date",
												{
													title : 'Warning!'
												});
									 $scope.datepicker = new Date();
										return false;
									 }
							}
							
							 $scope.dueForDischarge = function(item)
								{
									$scope.note="";
									$scope.itemForDischargePatient = item;
									$scope.noOfHours = item.noOfHours;
									$scope.datepicker = new Date();
									$rootScope.startLoader();
								
									var data = {
											admissionId: item.admissionId,
									};
									
									var URI = BASE_URL+ ROOT_URL+ "/api/opd/getPatientReturnDetails";
									GenericService.serviceAction("POST", URI,data).then(
											function(response) {

												if (response.data.status == "success") 
												{
													if(response.data.listObject == null || response.data.listObject.length == 0){
														angular.element('#dueForDischarge').modal('show');
													}else{
														growl
														.warning(
																'Return Medication not accepted',
																{
																	title : 'Warning!'
																});
													}
													
													$rootScope.stopLoader();
													
												} else if(response.data.status == "error")
												{
													
													if(response.data.message!=null){
														growl.error(response.data.message,{title : 'Error!'});
													}else{
													 growl.error('Something went wrongs...!!',{title : 'Error!'});
													}
//													
												}
											});
							
								}
							 $scope.obh = {};
							 $scope.obh.isSendToMortury = 'N';
							 $scope.setMortury = function(){
								 if($scope.dischargeTypeId  != '3'){
									 $scope.obh.isSendToMortury = 'N'; 
								 }
								
							 }
							 
						       $scope.updatePatientDetailsInSQL = function(){
                                	 $rootScope.startLoader();
                                	  var data ={}; 
                                	   data.uhidnumber = $scope.itemForDischargePatient.uhidnumber;
                                	   data.unitId =$scope.unitId;
                                	   data.organizationId =$scope.organizationId;
                                	   data.updatedBy = $scope.userId;
                                	   data.createdBy = $scope.userId;
                                	
                                	   
                                     var URI = BASE_URL + ROOT_URL+"/api/global/updatePatientAsDeath";
                                     BillingGenericService
                                             .serviceAction(METHOD_POST,URI, data)
                                             .then(
                                                     function(response) {
                                                         if (response.data.status == "success"){
                                                        	 growl.success("Updated successfully",
             														{
             															title : 'Success!'
             														});
                                                             $rootScope.stopLoader();
                                                         }else{
                                                             $rootScope.stopLoader();
                                                         }
                                                     });
                                     }
						       
								$scope.updateExtendPdd = function()
								{
									var did = $('#dischargeTypeIdPopup').val();
									if(did == 0 || did == '')
									{
									growl
									.warning(
											'Please select discharge reason..!!',
											{
												title : 'Warning!'
											});
									return false;
									
									}
									/*else if($scope.reasonId == 0 || $scope.reasonId == '')
									{
									growl
									.warning(
											'Please select reason..!!',
											{
												title : 'Warning!'
											});
									return false;
									
									}*/
								//else if($scope.datepicker.opened == false)
									else if($scope.datepicker == undefined || $scope.datepicker == null )
									{
										growl
										.warning(
												'Please select future date..!!',
												{
													title : 'Warning!'
												});
										return false;
									}
								else{
									
									$rootScope.startLoader();
									
									$("#updateExtendPddId").removeClass("enabled");
									$("#updateExtendPddId").addClass("disabled");
									$("#"+$scope.itemForDischargePatient.admissionId+"").removeClass("enabled");
									$("#"+$scope.itemForDischargePatient.admissionId+"").addClass("disabled");
									$scope.currentDate = new Date();
									$scope.currentDate = moment($scope.currentDate)
											.format('DD-MM-YYYY HH:mm:ss');
									
									$scope.disDt = moment($scope.datepicker).format('DD-MM-YYYY');

									var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										reasonId : null,
										admissionId : $scope.itemForDischargePatient.admissionId,
										updatedBy : $scope.userId,
										updatedDate : $scope.currentDate,
										pdd :  $scope.disDt + " 00:00:00",
										noteCancel : $scope.note,
										dischargeTypeId : did,
										doctorId :  $scope.itemForDischargePatient.doctorId,
										status : 'A',
										destinationId :$scope.itemForDischargePatient.bedId,
										dischargeDate : $scope.disDt + " 00:00:00",
										dischargeStatusId: 1,
										visitTypeId : $scope.itemForDischargePatient.visitTypeId,
										createdBy : $scope.userId,
										createdDate : $scope.currentDate,
										patientId: $scope.itemForDischargePatient.patientId,
										isSendToMortury : $scope.obh.isSendToMortury ,

										
									};
									
									console.log(data);
									//return false;
									var URI = BASE_URL + ROOT_URL
											+ MARKDISCHARGEFROMCASESHEET;
									
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {

														if (response.data.status == "success") {
															$rootScope.stopLoader();
															
															growl.success('Discharge Request Raised Successfully...!!',
																			{
																				title : 'Success!'
																			});
															angular.element('#dueForDischarge').modal('hide');
															if(did == "3"){
																$scope.updatePatientDetailsInSQL();
															}
															
															$scope.init();
														} else {
															$rootScope.stopLoader();
															growl.error(response.data.message,
																			{
																				title : 'Error!'
																			});
														}
														$("#updateExtendPddId").removeClass("disabled");
														$("#updateExtendPddId").addClass("enabled");
														$("#"+$scope.itemForDischargePatient.admissionNumber+"").removeClass("disabled");
														$("#"+$scope.itemForDischargePatient.admissionNumber+"").addClass("enabled");
													});
								}
								}
							
								$scope.updateServicesNotRequired= function(){
									
									$scope.orderDetailsIds=[];
									
									angular.forEach($scope.pendingServicesList,function(value,index){
										if(value.checkOption=='Y'){
											$scope.orderDetailsIds.push(value.orderDetailsId); 
										}
									  });
									 if(Object.keys($scope.orderDetailsIds).length > 0)
									 {
										 $("#pendingServicesModal").hide();
										 $scope.updateServices();
									 }else{
										/* growl.warning("Please select at least one serivce..!!",{
												title : WARNING_MSG
											});
											return;*/
										  angular.element('#acceptForPopup').modal('show');
	                                         $scope.acceptBtnDisabled = false;
//	                                         $timeout($scope.init(), 3000);
	                                         $scope.pendingServicesList=[];
	                                         //$scope.checkLengthServicesList=[];
	                                         $scope.orderDetailsIds=[];
									 }
									
								}
							
								$scope.updateServices = function()
								{
									$rootScope.startLoader();
									$scope.acceptBtnDisabled = true;
									$scope.currentDate = new Date();
									$scope.currentDate = moment($scope.currentDate)
									.format('YYYY-MM-DD HH:mm:ss');
									if($scope.pendingServicesList.length > 0){
									$("#pendingServicesModal").hide();
									
									}
									
									
									var data = {
											updatedDate: $scope.currentDate,
											updatedBy:$scope.userId,
											organizationId:$scope.organizationId,	
											unitId:$scope.unitId,
											status:'I',
											ordCancelled:'Y',
											ordCancelDatetime:$scope.currentDate,
											ordCancelBy:$scope.userId,
											orderDetailsIds:$scope.orderDetailsIds,
									};
									
									
									
									var URI = BASE_URL+ ROOT_URL+ "/adt/updateNotRequiredServices";
									GenericService.serviceAction("POST", URI,data).then(
											function(response) {

												if (response.data.status == "success") 
												{
//													growl.success('Admission accepted...!!',{title : 'Success!'});												
													angular.element('#acceptForPopup').modal('show');
													$scope.acceptBtnDisabled = false;
//													$timeout($scope.init(), 3000);
													$scope.pendingServicesList=[];
													//$scope.checkLengthServicesList=[];
													$scope.orderDetailsIds=[];
													$rootScope.stopLoader();
													
												} else if(response.data.status == "error")
												{
													
													if(response.data.message!=null){
														growl.error(response.data.message,{title : 'Error!'});
													}else{
													 growl.error('Something went wrongs...!!',{title : 'Error!'});
													}
//													$timeout($scope.init(), 3000);
													$rootScope.stopLoader();
													$scope.pendingServicesList=[];
													//$scope.checkLengthServicesList=[];
													$scope.orderDetailsIds=[];
													$scope.acceptBtnDisabled = false;
												}
												$scope.getNewAdmissionList();
											});
							
								}
								
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
						          
									$scope.patientDocumnetInformationForBedTransfer = function(info) {
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
														$('#patientDocumentOptionForBedWardTransfer').modal('show');
													}
													$rootScope.stopLoader();
												}
												else if(response.data.status == "error")
													{
													$rootScope.stopLoader();
													}
											});
								
							          };
							          
										$scope.patientDocumnetInformationForAdmission = function(info) {
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
															$('#patientDocumentOptionForAdmission').modal('show');
														}
														$rootScope.stopLoader();
													}
													else if(response.data.status == "error")
														{
														$rootScope.stopLoader();
														}
												});
									
								          };
							          
											$scope.patientDocumnetInformationForIcuTransfer = function(info) {
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
																$('#patientDocumentOptionForIcuTransfer').modal('show');
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
