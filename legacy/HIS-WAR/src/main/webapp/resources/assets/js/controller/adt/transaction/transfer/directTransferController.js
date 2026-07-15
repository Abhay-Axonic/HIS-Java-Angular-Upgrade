'use strict';

/**
 * @Author By Vivek Satle
 * @name myApp.controller:initiateBedTransferController
 * @description #initiateBedTransferController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'directTransferController',
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
						'PagerService',
						'reportService',
						'$window',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, BillingGenericService ,growl,
								$state,$stateParams,PagerService, reportService,$window) {
							
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
							$rootScope.loginpage = true;
							$scope.roleName = cookieObjectAppConfig.roleName;
							$scope.nurseRole = cookieObjectAppConfig.nurseRole;	 
							$scope.doctorRole = cookieObjectAppConfig.doctorRole;
							$scope.isShowPatientByBed=cookieObjectAppConfig.isShowPatientByBed;
							$scope.skipHousekeeping = cookieObjectAppConfig.skipHousekeeping;
							$scope.skipTransferAcceptance = cookieObjectAppConfig.skipTransferAcceptance;
							
							
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
							 }
							 
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
								    
								    
								    //=====================================================================================OK function
								    
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
													 $scope.wardList=response.data.object; 
													 commonObjInitiat.wardList=response.data.object; 
													 $scope.initiateReqSetNoOfRecords($scope.initiateReqPager.pageSize);
													 $scope.initiateReqPager.startIndex=0;
													 $scope.initiateReqPager.pageSize=10;
													 $scope.getInitiateReqList($scope.organizationId, $scope.unitId,$scope.initiateReqPager.startIndex,$scope.initiateReqPager.pageSize,commonObjInitiat);
													 $scope.initInitiateRequestList($scope.organizationId, $scope.unitId,$scope.initiateReqPager.startIndex,$scope.initiateReqPager.pageSize,commonObjInitiat);
													 $scope.init();
//													 window.location.reload();
													 /*$scope.getAdmittedPatientList();
													 $scope.getNewAdmissionList();
													 $scope.getTransferListTap();
													 $scope.getICUTransferList();*/
												}
										 });
									}
									}

								    //=====================================================================================
								 
							
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

								    $scope.searchObj = commonObjInitiat;

                                    var commonObjInitiatCount = {
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
									
								 $scope.initInitiateRequestList = function(orgId,
											orgUnitId, offset, noOfRecordsPerPage,obj) {
										try {
											$rootScope.startLoader();
											$scope.listOfAdmittedPatientInitiatList = [];
											$scope.admittedPatientInitiatList = [];
											var commonObjInitiatForBM  = {};
											var URI  = "";
											var URI1 = "";
											
											if(obj == undefined){
											 commonObjInitiatForBM = {
													admissionId:$scope.bedManagmentCookiesObj.admissionId,
													organizationId:$scope.organizationId,
													unitId : $scope.unitId,
													offset:offset,
													noOfRecordsPerPage:noOfRecordsPerPage,
													wardList: $scope.wardListForGetBedList
													};
											 URI = BASE_URL + ROOT_URL + GETINITIATETRANSFERREQUESTFORBM;
											}else{
												obj.offset = offset;
												obj.noOfRecordsPerPage = noOfRecordsPerPage;
                                                obj.searchKeyword = ($scope.searchKeyword != null) ? $scope.searchKeyword : null;

												if(obj.admission!=undefined){
													 URI = BASE_URL + ROOT_URL + GETINITIATETRANSFERREQUESTFORBM;
												}else{

                                                    commonObjInitiatCount.searchKeyword = ($scope.searchKeyword != null) ? $scope.searchKeyword : null

													URI = BASE_URL + ROOT_URL +GETINITIATETRANSFERREQUEST;
													URI1 = BASE_URL + ROOT_URL +GETINITIATETRANSFERREQUESTCOUNT;
												}
												commonObjInitiatForBM = obj;
											}

											GenericService.serviceAction("POST", URI, commonObjInitiatForBM).then(
													function(response) {
																$rootScope.stopLoader();
																$scope.admittedPatientInitiatList  = response.data.listObject;
																$scope.listOfAdmittedPatientInitiatList=[];
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
																	newArr.genderId = value.genderId;
																	newArr.visitTypeId = value.visitTypeId;
                                                                    newArr.toBillingBedCategoryId = value.toBillingBedCategoryId;
                                                                    newArr.isIcuSelected =value.isIcuSelected;
																	if(commonObjInitiatForBM.admissionId == undefined){
//																		if(value.visitTypeId != 4){		//resolve HIS-12287		
																			$scope.listOfAdmittedPatientInitiatList.push(newArr);
//																			}
																		}else{
																			$scope.listOfAdmittedPatientInitiatList.push(newArr);
																		}
																	});
																$scope.setInitiateReqPage(1,false);
															});


                                            if(URI1 != undefined && URI1 != null && URI1 != ""){

                                                 GenericService.serviceAction("POST", URI1, commonObjInitiatCount).then(
                                                         function(response) {
                                                                    $rootScope.stopLoader();
                                                                    $scope.initiateReqListCount = response.data.object;
                                                                    $scope.setInitiateReqPage(1,false);
                                                                    console.log("commonListCount",response.data.object);
                                                        }
                                                 );
                                            }
										} catch (e) {
											console.log(e.message);
										}
									}
								 

                                 $scope.searchBedTransfer = function(searchKeyword, searchObj){
                                       try {
                                           $scope.loadingMsg="Loading...";
                                           if(searchKeyword.length>0){
                                               $rootScope.startLoader();
                                               $scope.listOfAdmittedPatientInitiatList = [];
                                               $scope.admittedPatientInitiatList = [];
                                               var commonObjInitiatForBM  = {};
                                               var URI  = "";
                                               var URI1 = "";

                                               if(commonObjInitiat == undefined){
                                                     commonObjInitiatForBM = {
                                                            admissionId:$scope.bedManagmentCookiesObj.admissionId,
                                                            organizationId:$scope.organizationId,
                                                            unitId : $scope.unitId,
                                                            offset:$scope.initiateReqOffset,
                                                            noOfRecordsPerPage:$scope.initiateReqPerPageRecords,
                                                            wardList: $scope.wardListForGetBedList
                                                            };
                                                     URI = BASE_URL + ROOT_URL + GETINITIATETRANSFERREQUESTFORBM;
                                               }else{

                                                    commonObjInitiat.offset = $scope.initiateReqOffset;
                                                    commonObjInitiat.noOfRecordsPerPage = $scope.initiateReqPerPageRecords;
                                                   if(commonObjInitiat.admission!=undefined){
                                                        URI = BASE_URL + ROOT_URL + GETINITIATETRANSFERREQUESTFORBM;
                                                   }else{

                                                        commonObjInitiatCount.searchKeyword = searchKeyword;

                                                        URI = BASE_URL + ROOT_URL +GETINITIATETRANSFERREQUEST;
                                                        URI1 = BASE_URL + ROOT_URL +GETINITIATETRANSFERREQUESTCOUNT;
                                                   }
                                                   commonObjInitiat.searchKeyword = searchKeyword;
                                                   commonObjInitiatForBM = commonObjInitiat;
                                               }

                                               GenericService.serviceAction("POST", URI, commonObjInitiatForBM).then(
                                                       function(response) {
                                                                   $rootScope.stopLoader();
                                                                   $scope.admittedPatientInitiatList  = response.data.listObject;
                                                                   $scope.listOfAdmittedPatientInitiatList=[];
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
                                                                       newArr.genderId = value.genderId;
                                                                       newArr.visitTypeId = value.visitTypeId;
                                                                       newArr.toBillingBedCategoryId = value.toBillingBedCategoryId;
                                                                       newArr.isIcuSelected =value.isIcuSelected;
                                                                       if(commonObjInitiatForBM.admissionId == undefined){
               //																		if(value.visitTypeId != 4){		//resolve HIS-12287
                                                                               $scope.listOfAdmittedPatientInitiatList.push(newArr);
               //																			}
                                                                           }else{
                                                                               $scope.listOfAdmittedPatientInitiatList.push(newArr);
                                                                           }
                                                                       });

                                                                   $scope.setInitiateReqPage(1,false);
                                                      });

                                               if(URI1 != undefined && URI1 != null && URI1 != ""){

                                                GenericService.serviceAction("POST", URI1, commonObjInitiatCount).then(
                                                        function(response) {
                                                                   $rootScope.stopLoader();
                                                                   $scope.initiateReqListCount = response.data.object;
                                                                   $scope.setInitiateReqPage(1,false);
                                                                   console.log("commonListCount",response.data.object);
                                                        });
                                                }
                                            }

                                            if(searchKeyword == undefined || searchKeyword == null || searchKeyword ==""){
                                                  if($scope.bedManagmentCookiesObj != null){
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

                                       } catch (e) {
                                           console.log(e.message);
                                       }
                                   }


                                 if($scope.bedManagmentCookiesObj != null){
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

							
								 $scope.initiateReqSetNoOfRecords = function() {
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
								
							}
							$scope.init();
							//$scope.init();
							$scope.admissionRequestOrAdmissionObj={
									doctorId : '',
									specialityId:''
							}
							
							$scope.getselectedDoctor= function(){
								$scope.itemSelecteds.doctorId = $scope.admissionRequestOrAdmissionObj.doctorId ;
							}
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
//								if($scope.itemSelecteds.doctorId != null && $scope.itemSelecteds.doctorId != 0){
									$scope.submitRequest();
								/*}else{
									angular.element('#doctorSelectionModal').modal('show');
								}*/
								
							}
							

							var commonObj = {
								unitId : $scope.unitId,
								organizationId : $scope.organizationId
							};
							
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
					
					$scope.getDoctorBySpeciality = function(id) {
                         $scope.specialityId = id ;
						var data = {
							specialityId : id,
							unitId : $scope.unitId,
							organizationId : $scope.organizationId
						};
						var URI = BASE_URL + ROOT_URL
								+ GETDOCTORBYSPECIALITYID;
						
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
									
									$scope.wardListForDirectTransfer = [];
									var data = {};
									
									var URI = BASE_URL + ROOT_URL
									+ "/api/adt/getWardForDirectTransfer/"+item.genderId+"/"+item.visitTypeId+"/"+$scope.unitId;
									GenericService
									.serviceAction("GET", URI, data)
									.then(
											function(response) {
												$scope.wardListForDirectTransfer = [];

												if (response.data.status == "success") {
													$scope.wardListForDirectTransfer = response.data.listObject;

												}
											});

								}
							}

							$scope.getBedByWard = function(item){
								item.toBedId = undefined;
								var data = 
								{
										wardId : item.toWardId,
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
							
							
							
							//sneha
							
						
							
							
							
							$scope.universalSearchForTransferList = function(searchString) {
								
								$scope.nursingWardList = [];
								var nursingStationsWardsList = JSON.parse(sessionStorage.getItem('nursingStationsWardsList'));
								   $scope.nursingWardList =nursingStationsWardsList; 
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
									commonObjForTransfer.bedList=$scope.nurseBedList;
									commonObjForTransfer.isShowPatientByBed='N';

								if($scope.nurseRole == 1 && $scope.nursingWardList.length > 0){
									
									var URI = BASE_URL + ROOT_URL+ "/adt/universalSearchForTransferRequestList";
									$rootScope.startLoader();
							GenericService.serviceAction("POST", URI, commonObjForTransfer).then(
											function(response) {
												if (response.data.status == "success") {
													
													$scope.searchPatientList = [];
													$scope.searchPatientList = response.data.listObject;
													console.log($scope.itemSelecteds);
													$scope.currentDate = new Date();
													$scope.currentDate = moment($scope.currentDate)
															.format('DD-MM-YYYY HH:mm:ss');

													
													var selectedBedObject = $.grep($scope.bedListByWard, function (bedList) {
											            return bedList.bedId == $scope.itemSelecteds.toBedId;
											        });
													
													
													var data = {
															organizationId : $scope.organizationId,
															unitId : $scope.unitId,
															transferId : $scope.itemSelecteds.transferId,
															updatedBy : $scope.userId,
															updatedDate : $scope.currentDate,
															transferRequestId : $scope.savedDataObject.transferRequestId,
															workstationNote : $scope.itemSelecteds.remark,
															toBedId : $scope.searchPatientList[0].toBedId,
															admissionId : $scope.itemSelecteds.admissionId,
															toFloorId : $scope.searchPatientList[0].toFloorId,
															toWardId : $scope.savedDataObject.toWardId,
															toRoomId :  $scope.searchPatientList[0].toRoomId,
															admissionDetailsId : $scope.itemSelecteds.admissionDetailsId,
															fromBedId : $scope.searchPatientList[0].fromBedId,
															toBedCategoryId: $scope.savedDataObject.toBedCategoryId,
															toBillingBedCategoryId:$scope.searchPatientList[0].toBillingBedCategoryId,
															nursingStationId:$scope.searchPatientList[0].fromNursingStationId, // it's for from nursingstation
															fromFloorId : $scope.searchPatientList[0].fromFloorId,
															fromWardId : $scope.savedDataObject.fromWardId,
															fromRoomId :$scope.searchPatientList[0].fromRoomId,
															skipHousekeeping : $scope.skipHousekeeping!=null ? $scope.skipHousekeeping : 'Y',
													};
													
													if($scope.itemSelecteds.visitTypeId == 4){
														data.doctorId = $scope.itemSelecteds.doctorId,
														data.newVisitTypeId = 2;
														data.specialityId = $scope.specialityId ;
													}
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
																			growl.success('Transferred successfully...!!',
																							{
																								title : 'Success!'
																							});
																			
																			
																			$scope.init();
																		} else {
																			growl.error( 'Something went wrong...!!',
																							{
																								title : 'Error!'
																							});
																		}

																	});
												}else{
													$rootScope.stopLoader();
												}
											});
									}
								}
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
									if($scope.itemSelecteds.reasonId=="" || $scope.itemSelecteds.reasonId ==undefined || $scope.itemSelecteds.reasonId =="Select Bed"|| $scope.itemSelecteds.reasonId =="Select Bed No." ){
										$rootScope.stopLoader();
										growl.warning('Please select Reason...!!',
												{
													title : 'warning!'
												});
										return false;
									} 
									if($scope.itemSelecteds.doctorId != null && $scope.itemSelecteds.doctorId != 0){
										angular.element('#doctorSelectionModal').modal('hide');
									}else{
										angular.element('#doctorSelectionModal').modal('show');
										return false;
									}
									if ($scope.popUpFlag) {
										if($scope.itemSelecteds.isIcuSelected==true)
											{
											$scope.showModal = !$scope.showModal;
											}
										else
											{
											$scope.popUpFlag=false;
											angular.element('#bedTransferId').modal('show');
											}
										
									} else {
										$rootScope.startLoader();
										$scope.currentDate = new Date();
										$scope.currentDate = moment(
												$scope.currentDate).format(
												'DD-MM-YYYY HH:mm:ss');

										var data = {
                                            treatingDoctorId : $scope.itemSelecteds.doctorId != null && $scope.itemSelecteds.doctorId != 0 ? $scope.itemSelecteds.doctorId :$scope.admissionRequestOrAdmissionObj.doctorId ,
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
											transferStatusId :7,
											toWardId : $scope.itemSelecteds.toWardId,
											
											
										}
										
										var selectedBedObject = $.grep($scope.bedListByWard, function (bedList) {
								            return bedList.bedId == $scope.itemSelecteds.toBedId;
								        });
										
										if($scope.itemSelecteds.visitTypeId == 4){
											$scope.newVisitTypeId =2;
											data.newVisitTypeId = 2;
											data.specialityId = $scope.specialityId ;
										}
										treatingDoctorId : $scope.itemSelecteds.doctorId 
										data.toBedId = selectedBedObject[0].bedId;
										data.toBedCategoryId = selectedBedObject[0].bedCategoryId;
										data.toBillingBedCategoryId = $scope.itemSelecteds.toBillingBedCategoryId;
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
																
																$scope.savedDataObject = response.data.object;
																
																if($scope.skipTransferAcceptance == 'Y'){
																	
																	if($scope.itemSelecteds != undefined && $scope.itemSelecteds.transferId != null){
																		var data = {
																				"appName" : "HIS",
																				"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
																				"reportDesignFilePath" : "BedTransferPrint.rpt",
																				"reportFormat" : "PDF",
																				"exportFileName" : "BedTransferPrint",
																				"reportParams" : [ {
																					"paramName" : "transfer_id",
																					"paramType" : "Integer",
																					"paramValue" : $scope.itemSelecteds.transferId

																				} ]
																			};
																			reportService.generate(REPORT_API_URI1, data);
																	}
																	
																	$scope.universalSearchForTransferList($scope.itemSelecteds.uhidnumber);
																
																}else{
																		
																	growl.success( 'Transfer Request sent Successfully...!!',
																			{
																				title : 'Success!'
																			});
															       $scope.init();
																}
																
															} else {
																growl.error( 'Something went wrong...!!',
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
								var data = {
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
								
								if($scope.itemSelecteds.visitTypeId == 4){
									data.doctorId = $scope.itemSelecteds.doctorId,
									data.newVisitTypeId = 2;
									data.specialityId = $scope.specialityId ;
								}
								console.log(data);
								//return false;
								var URI = BASE_URL + ROOT_URL
										+ ACCEPTFINALTRANSFERREQUEST;
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
                                    var URI  = "";

                                    if(obj == undefined){
                                     commonObjInitiatForBM = {
                                            admissionId:$scope.bedManagmentCookiesObj.admissionId,
                                            organizationId:$scope.organizationId,
                                            unitId : $scope.unitId,
                                            offset:offset,
                                            noOfRecordsPerPage:noOfRecordsPerPage,
                                            wardList: $scope.wardListForGetBedList
                                            };
                                     URI = BASE_URL + ROOT_URL + GETINITIATETRANSFERREQUESTFORBM;
                                    }else{
                                        obj.offset = offset;
                                        obj.noOfRecordsPerPage = noOfRecordsPerPage;
                                        obj.searchKeyword = ($scope.searchKeyword != null) ? $scope.searchKeyword : null;

                                        if(obj.admission!=undefined){
                                             URI = BASE_URL + ROOT_URL + GETINITIATETRANSFERREQUESTFORBM;
                                        }else{

                                            URI = BASE_URL + ROOT_URL +GETINITIATETRANSFERREQUEST;
                                        }
                                        commonObjInitiatForBM = obj;
                                    }

                                    GenericService.serviceAction("POST", URI, commonObjInitiatForBM).then(
                                            function(response) {
                                                        $rootScope.stopLoader();
                                                        $scope.admittedPatientInitiatList  = response.data.listObject;
                                                        $scope.listOfAdmittedPatientInitiatList=[];
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
                                                            newArr.genderId = value.genderId;
                                                            newArr.visitTypeId = value.visitTypeId;
                                                            newArr.toBillingBedCategoryId = value.toBillingBedCategoryId;
                                                            newArr.isIcuSelected =value.isIcuSelected;
                                                            if(commonObjInitiatForBM.admissionId == undefined){
//																		if(value.visitTypeId != 4){		//resolve HIS-12287
                                                                    $scope.listOfAdmittedPatientInitiatList.push(newArr);
//																			}
                                                                }else{
                                                                    $scope.listOfAdmittedPatientInitiatList.push(newArr);
                                                                }
                                                            });
                                                    });


                                } catch (e) {
                                    console.log(e.message);
                                }
							}
							
							
							$scope.initiateReqPager = {};
							$scope.initiateReqPage;
							$scope.setInitiateReqPage = function(page, flag)
							{

								if($scope.initiateReqPager.totalPages==0){
                                    $scope.initiateReqPager.totalPages=undefined;
                                }

								if (page < 1 || page > $scope.initiateReqPager.totalPages)
								{
									return;
								}
								
								$scope.initiateReqPager = PagerService.GetPager($scope.initiateReqListCount, page,$scope.initiateReqPerPageRecords,$scope.unitId);
								
								if (flag) {
									if($scope.bedManagmentCookiesObj != null)
									{
										$scope.getInitiateReqList($scope.organizationId, $scope.unitId,$scope.initiateReqPager.startIndex,$scope.initiateReqPager.pageSize);
									}else{
										if($scope.nurseRole == 1 && $scope.wardList.length > 0){
											
											$scope.getInitiateReqList($scope.organizationId, $scope.unitId,$scope.initiateReqPager.startIndex,$scope.initiateReqPager.pageSize,commonObjInitiat);
										}
									}
								}
							}
							
							
							$scope.closePopUp = function()
							{
								$scope.popUpFlag=true;
							}
							$scope.getChangeBillingBed = function(info){
								
								angular.forEach($scope.bedListByWard,function(value,index) {
									
									if(info.toBedId == value.bedId){
										if(value.hierarchyId<info.hierarchyId)
										{
											$scope.bedTransferMessage="You are upgrading to a higher category bed. Do you want to continue?";
										}
										else
										{
											$scope.bedTransferMessage="You are Downgrading to a lower category bed. Do you want to continue?";
										}

										info.toBillingBedCategoryId = value.billingBedCategoryId;
										$scope.itemSelecteds.isIcuSelected=value.isIcuSelected;
									}
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

