'use strict';

/**
 * @Author By  Kaustubh
 * @name myApp.controller:drCreditNote
 */
angular
		.module('myApp')
		.controller(
				'drCreditNote',
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
						'GenericServiceFileUpload',
						'growl',
						'Pubnub',
						'$stateParams','PagerService',
						function($scope, $cookies, $rootScope, GenericService,
								BillingGenericService, $state, $timeout,$interval,GenericServiceParamHeader,GenericServiceFileUpload,growl,Pubnub,$stateParams,PagerService) {
						
							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;
							$scope.SearchName="";
							
							$scope.firstTabLevel  = 1;
							
							$scope.init = function()
							{
								$rootScope.loginpage = true;
								
								// for datePicker
								$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,
									
								};
								
								
								$scope.openDatePickerFrom = function() {
									$scope.datepickerFrom.opened = true;
								};

								$scope.datepickerFrom = {
									opened : false
								};
								
								
								$scope.openDatePickerTo = function() {
									$scope.datepickerTo.opened = true;
								};

								$scope.datepickerTo = {
									opened : false
								};
								
								$scope.datepickerFrom = new Date();
								$scope.datepickerTo = new Date();
								
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								
								var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
								 if(cookieObjectAppConfig == undefined){
								  $state.go('login');
								  return;
								 }
								 else
								  {
								   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
								   
								  }
								
									
								$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
								$scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.status = 1;
								
								
								
							}
							
							$scope.init();
							
							
							$scope.searchDoctorCreditShare = function()
							{
								$rootScope.startLoader();
									var commonObj = {
											unitId:$scope.unitId,
											orgId:$scope.organizationId,
											fromDate: $rootScope.getFormattedDate($scope.datepickerFrom,false),
											toDate : $rootScope.getFormattedDate($scope.datepickerTo,false),
											 noOfRecordsPerPage : $scope.noOfRecordsPerPage,
											 offset : $scope.offset,
											 status : $scope.status
										};
										var URI = BASE_URL + ROOT_URL + "/api/docShare/cr/bills/pending";
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.doctorCreditShareList = [];
													$rootScope.stopLoader();
													if (response.data.status == "success") {
														$scope.doctorCreditShareList = response.data.listObject;
														if(response.data.listObject.length>0){
															if(response.data.object!=null && response.data.object >0)
														      	$scope.commonListCount= response.data.object;
														
															     $scope.setPage(1,false);
														}else{
															$scope.commonListCount= 1;
														     $scope.setPage(1,false);
														}
													}
													else
														{
													     	$rootScope.stopLoader();
														}
												});
							}
							
							$scope.searchDoctorCreditShare();
							
							
							$scope.searchDoctorCreditShareNextBtn = function(offset,noOfRecordsPerPage)
							{
								$rootScope.startLoader();
									var commonObj = {
											unitId:$scope.unitId,
											orgId:$scope.organizationId,
											fromDate: $rootScope.getFormattedDate($scope.datepickerFrom,false),
											toDate : $rootScope.getFormattedDate($scope.datepickerTo,false),
											 noOfRecordsPerPage : noOfRecordsPerPage,
											 offset : offset,
											 status : $scope.status
										};
										var URI = BASE_URL + ROOT_URL + "/api/docShare/cr/bills/pending";
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.doctorCreditShareList = [];
	
													if (response.data.status == "success") {
														$scope.doctorCreditShareList = response.data.listObject;
														if(response.data.object!=null && response.data.object >0)
														         $scope.commonListCount= response.data.object;
														
														$rootScope.stopLoader();
	
													}
													else
														{
													     	$rootScope.stopLoader();
														}
												});
							}
							//search keyup
							$scope.searchDoctorCreditShareKeyup = function(searchKeyword)
							{
								if(searchKeyword!=''){
									$rootScope.startLoader();
									
									var commonObj = {
											unitId:$scope.unitId,
											orgId:$scope.organizationId,
											fromDate: $rootScope.getFormattedDate($scope.datepickerFrom,false),
											toDate : $rootScope.getFormattedDate($scope.datepickerTo,false),
											 noOfRecordsPerPage : $scope.noOfRecordsPerPage,
											 offset : $scope.offset,
											 searchText: searchKeyword,
											 status : $scope.status
										};
										var URI = BASE_URL + ROOT_URL + "/api/docShare/cr/bills/pendingSearch";
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.doctorCreditShareList = [];
													$rootScope.stopLoader();
													if (response.data.status == "success") {
														$scope.doctorCreditShareList = response.data.listObject;
														if(response.data.listObject.length>0){
															if(response.data.object!=null && response.data.object >0)
														      	$scope.commonListCount= response.data.object;
														
															     $scope.setPage(1,false);
														}else{
															$scope.commonListCount= 1;
														     $scope.setPage(1,false);
														}
													}
													else
														{
														
													     	$rootScope.stopLoader();
														}
												});
								}else{
									$scope.searchDoctorCreditShare();
								}
								
								
							}
							
							//Check Service in inclusion
							  var isExists;
								function checkExistsOrNot(arr, val) {
									return arr
											.some(function(arr) {
												if(val.doctorId === arr.doctorId || val.referralId === arr.referralId)
													{
														return true;
													}
												else
													{
														return false;
													}
											});
								}
								
								
								$scope.setFirstTabLevel= function(newTab)
								{
									$scope.firstTabLevel = newTab;
								}
								
								$scope.isSetFirstTabLevel = function(tabNum)
								{
									
									return $scope.firstTabLevel === tabNum;
								};
								
							$scope.initPopupData = function()
							{
								$scope.doctorId  = '';
								$scope.amount = 0;
								$scope.remark = '';
								$scope.referralId  = '';
								
							}
							$scope.add = function()
							{
							    console.log($scope.slectedObject.patientPayable);
							    var tmpRemaingAmt = 0.00;
							    if($scope.totalCreditAmt>0)
							    	{
							    		tmpRemaingAmt = parseFloat($scope.slectedObject.totalSelfPayable)-parseFloat($scope.totalCreditAmt) ;
							    	}else{
							    		tmpRemaingAmt = parseFloat($scope.slectedObject.totalSelfPayable);
							    	}
							     

								if(($scope.referralId == '' || $scope.referralId == null || $scope.referralId == undefined) && ($scope.doctorId == '' || $scope.doctorId == null || $scope.doctorId == undefined))
									{
										var msg = '';
										if($scope.firstTabLevel == 1){
											msg = "Please select doctor..!!";
										}else{
											msg = "Please select referral..!!";
										}
										growl.warning(msg,{
											title : WARNING_MSG
										});
										return false;
									}	
								else if($scope.amount == '' || $scope.amount == 0 || $scope.amount == undefined)
									{
										growl.warning("Amount should be greater then ZERO(0)..!!",{
											title : WARNING_MSG
										});
										return false;
									}else
										{
										
									/*	var totalCreditAmtTemp = parseFloat($scope.totalCreditAmt) +  parseFloat($scope.amount);
										if(totalCreditAmtTemp > $scope.slectedObject.patientPayable)
											{
													var remTemp = $scope.slectedObject.patientPayable - $scope.totalCreditAmt;
													 growl.warning("You can add only upto patients outstanding, i.e "+parseFloat(remTemp).toFixed(2)+"...!!",{
															title : WARNING_MSG
														});
														return false;
											}*/
										var totalCreditAmtTemp =  parseFloat($scope.amount);
									//	if(totalCreditAmtTemp > $scope.slectedObject.patientPayable)
                                        if(totalCreditAmtTemp >tmpRemaingAmt)

											{
													var remTemp = $scope.slectedObject.patientPayable;
													growl.warning("You can add only upto patients outstanding, i.e "+parseFloat(tmpRemaingAmt).toFixed(2)+"...!!",{

															title : WARNING_MSG
														});
														return false;
											}
										else{
												if($scope.doctorId>0 && $scope.firstTabLevel == 1)
												{
													var temp = {doctorId:$scope.doctorId};
													isExists=checkExistsOrNot($scope.genreateDoctorCreditList,temp)
													  if (!isExists) 
													  {
														  var docName = $.grep($scope.doctorList, function (docList) {
													            return docList.doctorId == $scope.doctorId;
													           })[0].doctorName;
														  
																var data = {
																		doctorId:$scope.doctorId,
																		doctorCredit:$scope.amount,
																		doctorName : docName,
																		note:$scope.remark,
																		billId:$scope.slectedObject.billingMasterId,
																		orgUnitId:$scope.unitId,
																		orgId:$scope.organizationId,
																		updatedBy:$scope.userId,
																		createdBy:$scope.userId,
																		
																}
																$scope.totalCreditAmt = parseFloat($scope.totalCreditAmt) + parseFloat($scope.amount);
																$scope.genreateDoctorCreditList.push(data);
																$scope.initPopupData();
													  }
													  else
														  {
														      $scope.initPopupData();
															  growl.warning("Doctor already exist..!!",{
																	title : WARNING_MSG
																});
																return false;
														  }
													}
												if($scope.referralId>0 && $scope.firstTabLevel == 2)
												{
													var temp = {referralId:$scope.referralId};
													isExists=checkExistsOrNot($scope.genreateDoctorCreditList,temp)
													  if (!isExists) 
													  {
														  var docName = $.grep($scope.referralList, function (refList) {
													            return refList.referralId == $scope.referralId;
													           })[0].referralName;
														  
																var data = {
																		referralId:$scope.referralId,
																		doctorCredit:$scope.amount,
																		doctorName : docName,
																		note:$scope.remark,
																		billId:$scope.slectedObject.billingMasterId,
																		orgUnitId:$scope.unitId,
																		orgId:$scope.organizationId,
																		updatedBy:$scope.userId,
																		createdBy:$scope.userId,
																		
																}
																$scope.totalCreditAmt = parseFloat($scope.totalCreditAmt) + parseFloat($scope.amount);
																$scope.genreateDoctorCreditList.push(data);
																$scope.initPopupData();
													  }
													  else
														  {
														      $scope.initPopupData();
															  growl.warning("Referral already exist..!!",{
																	title : WARNING_MSG
																});
																return false;
														  }
												}
												
										}
										//
										
											
										}
							}
							
							
							$scope.generatedCreditForDoc = function(item)
							{
								$rootScope.startLoaderPopup();
								$scope.totalCreditAmtTempry = parseFloat(0);
								$scope.slectedObject ={};
								$scope.slectedObject = item;
								$scope.totalCreditAmt = item.totalCredit;
								$scope.totalCreditAmtTempry = item.totalCredit;
								
								var URI = BASE_URL + ROOT_URL + "/api/docShare/cr/doctorCrs/"+item.doctorCrId;
								GenericService
										.serviceAction("GET", URI, {})
										.then(
												function(response) {
													$scope.genreatedDoctorList = [];

													if (response.data.status == "success") {
														
														angular.forEach(response.data.listObject,function(value,index){
															value.oldDocCreditAmt = value.doctorCredit
															$scope.genreatedDoctorList.push(value);
														});
														
														$rootScope.stopLoaderPopup();

													}
													else
														{
													     	$rootScope.stopLoaderPopup();
														}
												});
							}
							
							$scope.removeItemAdded = function(item,idx)
							{
								$rootScope.startLoaderPopup();
								var data = {
										totalCredit :parseFloat($scope.totalCreditAmt)-parseFloat(item.oldDocCreditAmt),
										doctorCrDetailsId:item.doctorCrDetailsId,
										doctorCrId:item.doctorCrId,
										//outstanding :parseFloat($scope.slectedObject.outstanding) + parseFloat(item.oldDocCreditAmt)
										
								}
								console.log("data",data);
								//return false;
								var URI = BASE_URL + ROOT_URL + "/api/docShare/cr/inactive";
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													//$scope.doctorCreditShareList = [];

													if (response.data.status == "success") {
														$rootScope.stopLoaderPopup();
														$scope.searchDoctorCreditShare();
														$scope.init();
														
														
														$scope.totalCreditAmt =parseFloat($scope.totalCreditAmt)-parseFloat(item.oldDocCreditAmt);
														$scope.totalCreditAmtTempry = parseFloat($scope.totalCreditAmtTempry)-parseFloat(item.oldDocCreditAmt);
														$scope.slectedObject.outstanding =  parseFloat($scope.slectedObject.outstanding) + parseFloat(item.oldDocCreditAmt);
														$scope.genreatedDoctorList.splice(idx,1);
														//angular.element('#generatedCreditDocModal').modal('hide');
													}
													else
														{
													     	$rootScope.stopLoaderPopup();
														}
												});
								
							}
							
							$scope.getCalculateDocAmt = function(item,idx)
							{
								
								$scope.totalCreditAmtForUpdate = $scope.totalCreditAmtTempry;
								if(item.doctorCredit > item.oldDocCreditAmt)
									{
										var tempAmt = parseFloat(item.doctorCredit) - parseFloat(item.oldDocCreditAmt);
										
										var totalCreditAmtTemp = parseFloat($scope.totalCreditAmtForUpdate) +  parseFloat(tempAmt);
										if(totalCreditAmtTemp > $scope.slectedObject.patientPayable)
											{
											
													var remTemp = $scope.slectedObject.patientPayable - $scope.totalCreditAmtForUpdate;
													var remTempAmt = parseFloat(item.oldDocCreditAmt) + parseFloat(remTemp);
													 growl.warning("You can add only upto patients outstanding, i.e "+parseFloat(remTempAmt).toFixed(2)+"...!!",{
															title : WARNING_MSG
														});
													 $scope.genreatedDoctorList[idx].doctorCredit = item.oldDocCreditAmt;
														return false;
														
											}
										else{
											   $scope.totalCreditAmtForUpdate  = parseFloat($scope.totalCreditAmtForUpdate) - parseFloat(item.oldDocCreditAmt);
											   $scope.totalCreditAmtForUpdate  = parseFloat($scope.totalCreditAmtForUpdate) + parseFloat(item.doctorCredit);
											   $scope.totalCreditAmtTempry = $scope.totalCreditAmtForUpdate;
										}
										
									}
								else
									{
									   $scope.totalCreditAmtForUpdate  = parseFloat($scope.totalCreditAmtForUpdate) - parseFloat(item.oldDocCreditAmt);
									   $scope.totalCreditAmtForUpdate  = parseFloat($scope.totalCreditAmtForUpdate) + parseFloat(item.doctorCredit);
									   $scope.totalCreditAmtTempry = $scope.totalCreditAmtForUpdate;
									}
								
								
								
							}
							$scope.getDocList = function(item)
							{
								
								$rootScope.startLoaderPopup();
								$scope.initPopupData();
								$scope.slectedObject ={};
								$scope.slectedObject = item;
								$scope.genreateDoctorCreditList = [];
								$scope.totalCreditAmt = item.totalCredit;
							
									var URI = BASE_URL + ROOT_URL + "/api/docShare/cr/doctorCr/doctorsList/"+item.billingMasterId;
							GenericService
									.serviceAction("GET", URI, {})
									.then(
											function(response) {
												$scope.doctorList = [];

												if (response.data.status == "success") {
													$scope.doctorList = response.data.listObject;
													$scope.referralList = response.data.object;
													$rootScope.stopLoaderPopup();

												}
												else
													{
												     	$rootScope.stopLoaderPopup();
													}
											});
								
							}
							
							$scope.removeItem = function(idx)
							{
								$scope.totalCreditAmt = parseFloat($scope.totalCreditAmt) - parseFloat($scope.genreateDoctorCreditList[idx].doctorCredit);
								$scope.genreateDoctorCreditList.splice(idx,1);
							}
							
							
							$scope.saveGeneratedDoctorCreditNote = function()
							{
								if(Object.keys($scope.genreatedDoctorList).length > 0){
								$rootScope.startLoaderPopup();
								var commonObj = {
										orgUnitId:$scope.unitId,
										orgId:$scope.organizationId,
										updatedBy:$scope.userId,
										createdBy:$scope.userId,
										billId:$scope.slectedObject.billingMasterId,
										doctorCrId:$scope.slectedObject.doctorCrId,
										octorShareCrNoteDetailsDtosList: $scope.genreatedDoctorList,
										totalCredit :  $scope.totalCreditAmtTempry
									};
								console.log("commonObj",commonObj);
							//	return false;
									var URI = BASE_URL + ROOT_URL + "/api/docShare/cr/request/doctorCr";
							GenericService
									.serviceAction("POST", URI, commonObj)
									.then(
											function(response) {
												$scope.doctorCreditShareList = [];

												if (response.data.status == "success") {
													 $scope.genreatedDoctorList = [];
													
													$scope.searchDoctorCreditShare();
													$scope.init();
													$rootScope.stopLoaderPopup();
													angular.element('#generatedCreditDocModal').modal('hide');
												}
												else
													{
												     	$rootScope.stopLoaderPopup();
													}
											});
								}
								else
								{
									 growl.warning("Please add doctor..!!",{
											title : WARNING_MSG
										});
										return false;
								}
							}
							
							
							
							$scope.saveGenerateCreditNote = function()
							{
								if(Object.keys($scope.genreateDoctorCreditList).length > 0){
								$rootScope.startLoaderPopup();
								var commonObj = {
										orgUnitId:$scope.unitId,
										orgId:$scope.organizationId,
										updatedBy:$scope.userId,
										createdBy:$scope.userId,
										billId:$scope.slectedObject.billingMasterId,
										doctorCrId:$scope.slectedObject.doctorCrId,
										octorShareCrNoteDetailsDtosList: $scope.genreateDoctorCreditList,
										totalCredit : $scope.totalCreditAmt
									
									};
									var URI = BASE_URL + ROOT_URL + "/api/docShare/cr/request/doctorCr";
							GenericService
									.serviceAction("POST", URI, commonObj)
									.then(
											function(response) {
												$scope.doctorCreditShareList = [];

												if (response.data.status == "success") {
													 $scope.genreateDoctorCreditList = [];
													
													$scope.searchDoctorCreditShare();
													$scope.init();
													$rootScope.stopLoaderPopup();
													angular.element('#generateCreditNoteModal').modal('hide');
												}
												else
													{
												     	$rootScope.stopLoaderPopup();
													}
											});
								}else
									{
										 growl.warning("Please add doctor..!!",{
												title : WARNING_MSG
											});
											return false;
									}
							}
							
							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages) {
									return;
								}
								$scope.pager = PagerService.GetPager(
										$scope.commonListCount, page,
										$scope.noOfRecordsPerPage);
								if (flag) {
									$scope.searchDoctorCreditShareNextBtn( $scope.pager.startIndex, $scope.pager.pageSize);
								}
							}
							$scope.setNoOfRecords = function() {
								$scope.searchDoctorCreditShare();
							};
							
			
							
						} ]);