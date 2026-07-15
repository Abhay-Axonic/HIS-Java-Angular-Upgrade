'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:mapOfBedERController
 * @description #mapOfBedERController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'mapOfBedERController',
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
						'$state',
						'$timeout',
						'$stateParams',
						'PagerService',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService,commonDetailService, GenericServiceParamHeader,growl,
								$state,$timeout,$stateParams,PagerService) {

							/* init() function for form object create. */
							var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
							
							$scope.getBedListForScreen = function()
							{
								$rootScope.startLoader();
								var bedListRequest = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										radiologySpecialityId : $scope.radiologySpecialityId,
										hemodialysisSpecialityId : $scope.hemodialysisSpecialityId,
										labSpecialityId : $scope.labSpecialityId,
										isIp : 'N'
									};
								if($scope.doctorRole == 1 || $scope.nurseRole == 1){
									bedListRequest.isClinical = 1;
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
												 if($scope.patientBedList === undefined || $scope.patientBedList === null || $scope.patientBedList.length===0){
                                                    $scope.patientBedList = [];

                                                    if (response.data.status == "success") {
                                                        $scope.patientBedList = response.data.listObject;

                                                        $scope.Listnew = [];
                                                        var uniqueBedIds = [];

                                                        angular.forEach(response.data.listObject[0].bedList, function(value) {
                                                          if (uniqueBedIds.indexOf(value.bedId) === -1) {
                                                            uniqueBedIds.push(value.bedId);
                                                            $scope.Listnew.push(value);
                                                          }
                                                        });

                                                        $scope.patientBedList[0].bedList=$scope.Listnew;
                                                        $scope.patientBedList[0].bedListNew=$scope.Listnew;
                                                        console.log("gg",$scope.patientBedList);
                                                        console.log("patientBedList",$scope.patientBedList.bedListNew);
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
//												 if($scope.patientBedList === undefined || $scope.patientBedList === null || $scope.patientBedList.length===0){
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
//												}
												});
								}

										
							}
							
							
							
							$scope.init = function() {
							     $scope.bedManagementDivFlag=false;
                                 $scope.erDivFlag=true;
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;

								$scope.hideIcons = true;
								var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');

								var labConfig = JSON.parse(localStorage.getItem('labConfig'));
								 if(cookieObjectAppConfig == undefined){
								  $state.go('login');
								  return;
								 }
								$scope.radiologySpecialityId = cookieObjectAppConfig.radiologySpecialityId;
								$scope.hemodialysisSpecialityId = cookieObjectAppConfig.hemodialysisSpecialityId;
								if (labConfig != undefined) {
								$scope.labSpecialityId = labConfig.labSpecialityId;
								}
								$scope.nurseRole = cookieObjectAppConfig.nurseRole;	 
								$scope.doctorRole = cookieObjectAppConfig.doctorRole;	 
								$scope.roleName = cookieObjectAppConfig.roleName;
								$scope.isShowPatientByBed=cookieObjectAppConfig.isShowPatientByBed;
								$scope.patientSearchDiv=true;
								$rootScope.linkHideShow=true;
								console.log("cookieObjectAppConfig",cookieObjectAppConfig);
								/** Variables for pagination */
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.commonListCount;

								$rootScope.loginpage = true;
								
								$scope.patientSearchDiv=true;
								$rootScope.linkHideShow=false;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								
								var cookieObject, cookieObjectAppConfig;
								[ cookieObject, cookieObjectAppConfig ] = $scope
										.checkCookies();
							
								$scope.specialityId = cookieObjectAppConfig.specialityId;
								$scope.role = cookieObjectAppConfig.roleName;
								// end for popup
								
								$scope.bedsearch = {
										patientId : 0,
										tPatientId : 0,
										erBedTypeAllocation : 0,
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										isIp : 'N'
									}
								$scope.groupName = "";

								$scope.itemSelecteds = {};

								var data = {};
								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								
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
						
						/*var URI = BASE_URL + ROOT_URL
						+ GETERMAPOFBEDLIST;
				GenericService
						.serviceAction("POST", URI, commonObj)
						.then(
								function(response) {
									$scope.patientBedList = [];

									if (response.data.status == "success") {
										$scope.patientBedListTEMP = response.data.listObject;
										
										angular .forEach($scope.patientBedListTEMP, function( value, index) {
											      var newArr = {};
											      newArr.patientId = value.patientId;
											      newArr.dob = value.dob;
											      newArr.pFirstName = value.patientName;
											      newArr.genderCode = value.genderName;
											      newArr.uhidnumber = value.uhidnumber;
											      newArr.doa = value.doa;
											      newArr.doctorId = value.doctorId;
											      newArr.wardId = value.wardId;
											      newArr.wardName = value.wardName;
											      newArr.pdd = value.pdd;
											      newArr.dFirstName = value.dFirstName;
											      newArr.paymentEntitlementDesc = value.paymentEntitlementDesc;
											      newArr.bedNumber = value.bedNumber;
											      newArr.admissionDetailsId = value.admissionDetailsId;
											      newArr.admissionId  = value.admissionId;
											      newArr.admissionNumber = value.admissionNumber;
											      newArr.bedId = value.bedId;
											      newArr.dFirstName  = value.dFirstName;
											      newArr.tPatientId = value.tPatientId;
											      newArr.visitTypeId = value.visitTypeId;
											      newArr.floorId = value.floorId;
												  newArr.patientCategoryId = value.patientCategoryId
												  newArr.prefixId  = value.prefixId;
												  newArr.roomId  = value.roomId;
												  newArr.genderId = value.genderId;
												  newArr.imaging = value.imaging;
												  newArr.labOrder = value.labOrder;
												  newArr.totalScore = value.totalScore;
												  newArr.nbm = value.nbm;
												  newArr.procedureOrder = value.procedureOrder;
												  newArr.allergy = value.allergy;
												  newArr.specialityId =$scope.specialityId;
													 var age = $rootScope.getAgeEr(value.dob);
													 newArr.age =age;
											      $scope.patientBedList.push(newArr);
											      console.log("$scope.patientBedList",$scope.patientBedList);
												  });
									}
								});*/
						
								//$scope.getBedListForScreen();

							}

							$scope.init();
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
							
							$scope.set_color = function(totalScore) {
							   /* if(totalScore <= 2){
							        return {"background-color": "#0cac26"};
							    }
							    else if(totalScore >= 2 &&  totalScore <=6 ){
							        return {"background-color": "#f0793a"};
							        
							    }else if(totalScore> 6){
							        return {"background-color": "#d11b1b"};
							        
							    }*/
							};
							/*
							$scope.statego= function(){
						
								$state.go('bedManagement');
							}
							*/
							

							
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
							/**END IP Sample Collection**/
							
							/**Allergy**/
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
							/**END Allergy**/
							
							/** Drug Administration**/
							/*$scope.drugAdminstrator = function (mapList) {
								commonDetailService.setDataId(mapList.admissionId);
								$state.go('drugAdministrator');
							}*/
							
							
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
								$scope.wardListForGetBedList = [];
								$scope.wardListForGetBedList = nursingStationsWardsList;
								$scope.getBedListForScreen();
							}
							
							
							$scope.drugAdminstrator = function (mapList) {
								commonDetailService.setDataIdForBedManagment(mapList.admissionId, mapList.uhidnumber);
								$state.go('drugAdministrator');
							}
							/**End**/
							/**procedure**/
							$scope.getOrderedProcedureList=function(mapList){
								
								if(!angular.equals({},$stateParams.obj))
								{
									$scope.admissionObj =  mapList;
									$scope.patientDetails.patientId= $scope.admissionObj.patientId;
							    }else{
							    	$scope.patientDetails.patientId = mapList.patientId;
							    }

								var URI = BASE_URL + ROOT_URL
								+ "/adt/getListOfOrderedProcedure/"+$scope.patientDetails.patientId+"/"+$scope.organizationId+"/"+$scope.unitId;
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
							/**end**/
							$scope.searchGroupList = function(keyword) {

								var data = {
									firstName : keyword,
									unitId : $scope.unitId,
									// visitType :
									// $scope.selectedPageNameForAdmissionNote,
									organizationId : $scope.organizationId
								}
								console.log(data);

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
									if(info.patientid != null)
									$scope.bedsearch.patientId  = info.patientid;
									if(info.tPatientId !=null)
									$scope.bedserach.tPatientId = info.tPatientId;
							}
							
							$scope.gotoCoversheet=function(patientDetailsObj){
								
								if(patientDetailsObj!=null)
								  {
									patientDetailsObj.startConsultanceStartTime = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'); 
									console.log("patientDetails",JSON.stringify(patientDetailsObj));
									if($scope.roleName == "doctor"){
												$state.go('IPDCoverSheet',
													{
													patientDetailsObj :patientDetailsObj
													});
									}
									else if($scope.roleName=="nurse"){
										
										$state.go('erNursingCoversheet',
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
							
							$scope.patientSearchByMultipleCriteria = function() {

								// $scope.bedsearch.isVirtual =
								// ($scope.bedsearch.isVirtual == true) ? "Y":
								// "N";
								$scope.bedsearchER = {
										
										radiologySpecialityId : $scope.radiologySpecialityId,
										hemodialysisSpecialityId : $scope.hemodialysisSpecialityId,
										labSpecialityId : $scope.labSpecialityId,
										floorId : 0,
										nursingStationId :0,
										bedCategoryId : 0,
										wardId : 0,
										patientId : $scope.bedsearch.patientId,
										//tPatientId : 0,
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										isIp : 'N'

									}
								console.log($scope.bedsearchER);
								if($scope.doctorRole == 1)
								{
										$scope.hideIcons = true;
										$scope.bedsearchER.isClinical = 1;
										$scope.bedsearchER.doctorId =$scope.doctorIdForBedList;
								}else if($scope.nurseRole ==1){
									$scope.hideIcons = true;
									$scope.bedsearchER.isClinical = 1;
									$scope.bedsearchER.wardList = $scope.wardListForGetBedList;
								}else{
									$scope.hideIcons = false;
								}
								//return false;
								var URI = BASE_URL + ROOT_URL
//										+ GETERADMITTEDPATIENTBYMULTIPLECRITERIA;
								+ "/api/adt/bed/management/search";
								GenericService
										.serviceAction("POST", URI,
												$scope.bedsearchER)
										.then(
												function(response) {
													$scope.patientBedList =[];
													
													if (response.data.status == "success") {
														
														$scope.patientBedList = response.data.listObject;
														
														$scope.Listnew = [];
														$scope.roomListNew = [];
														var uniqueBedIds = [];

														angular.forEach(response.data.listObject[0].bedList, function(value) {
														  if (uniqueBedIds.indexOf(value.bedId) === -1) {
														    uniqueBedIds.push(value.bedId);
														    $scope.Listnew.push(value);
														  }
														});

														uniqueBedIds = [];
//														angular.forEach(response.data.listObject[0].roomList, function(value) {
//															if (uniqueBedIds.indexOf(value.bedId) === -1) {
//																uniqueBedIds.push(value.bedId);
//																$scope.roomListNew.push(value);
//															}
//														});

														$scope.patientBedList[0].bedList=$scope.Listnew;
														$scope.patientBedList[0].bedListNew=$scope.Listnew;
//														$scope.patientBedList[0].roomList=$scope.roomListNew;
//														$scope.patientBedList[0].roomListNew=$scope.roomListNew;
														console.log("gg",$scope.patientBedList);
														console.log("patientBedList",$scope.patientBedList.bedListNew);
//														console.log("patientBedList",$scope.patientBedList.roomListNew);

														/*$scope.wardId=$scope.patientBedListTEMP[0].wardId;
														$scope.wardName=$scope.patientBedListTEMP[0].wardName;
														angular .forEach($scope.patientBedListTEMP, function( value, index) {
															var newwh={};
															newwh.wardId = value.wardId;
															newwh.wardName = value.wardName;
															
															angular .forEach(value.bedList, function( value1, index) {
																$scope.bedList=[];
															      var newArr = {};
															     
															      newArr.patientId = value1.patientId;
															      newArr.dob = value1.dob;
															      newArr.pFirstName = value1.pFirstName;
															      newArr.genderCode = value1.genderCode;
															      newArr.uhidnumber = value1.uhidnumber;
															      newArr.doa = value1.doa;
															      newArr.pdd = value1.pdd;
															      newArr.dFirstName = value1.dFirstName;
															      newArr.paymentEntitlementDesc = value1.paymentEntitlementDesc;
															      newArr.bedNumber = value1.bedNumber;
															      newArr.admissionDetailsId = value1.admissionDetailsId;
															      newArr.admissionId  = value1.admissionId;
															      newArr.admissionNumber = value1.admissionNumber;
															      newArr.bedId = value1.bedId;
															      newArr.dFirstName  = value1.dFirstName;
															      newArr.tPatientId = value1.tPatientId;
															      newArr.visitTypeId = value1.visitTypeId;
															      newArr.floorId = value1.floorId;
															      newArr.patientCategoryId = value1.patientCategoryId
															      newArr.prefixId  = value1.prefixId;
															      newArr.genderId = value1.genderId;
															      $scope.bedList.push(newArr);
															      console.log("patientBedList",$scope.patientBedList);
															 });
																newwh.bedList =$scope.bedList;
															 $scope.patientBedList.push(newwh);
														});*/

														$scope.bedsearch= {
																patientId : 0,
																tPatientId : 0,
																erBedTypeAllocation : 0,
																organizationId : $scope.organizationId,
																unitId : $scope.unitId,

															}
														$scope.groupName = "";
														
														if($scope.isShowPatientByBed=='Y')
														{
															$scope.commonWardListOrBedList=$scope.mappedNurseBedList;
														}
														else{
															$scope.commonWardListOrBedList=$scope.wardListForView;
														}
														
														
														
//														$scope.groupName = "";

													}/*else{
														$rootScope.stopLoader();
														
														$scope.bedsearchER = {
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
													}*/
												});

							}
							$scope.patientSearchByMultipleCriteria();
							
							
							
							
							
							
							/*$scope.popupData = function(item)
							{
								
								if(item.patientId > 0)
									{
									 var age = $rootScope.getAge(item.dob);
									
									 $scope.patientFullName =item.pFirstName+' '+age+'/'+item.genderCode; 
									}
								else
									{
									 $scope.patientFullName =item.pFirstName+' '+item.dob+'/'+item.genderCode;
									}
								$scope.uhidNum = item.uhidnumber;
								$scope.dateOfAdmission = item.doa;
								$scope.patientDischargeDate = item.pdd;
								$scope.bedNumber = item.bedNumber;
								$scope.doctorFullName = item.dFirstName;
								$scope.billing = true;
								sessionStorage.setItem("admissionNoteObjER",JSON.stringify(item));
								
							}
							*/
							
							$scope.popupData = function(item)
							{
								//console.log(item);
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
								$scope.patientDetails.genderCode = item.genderCode;
								$scope.patientDetails.genderId = item.genderId;
							
								sessionStorage
								.setItem("admissionNoteObjER",JSON.stringify(item));
								
								var bedManagmentERCookiesData = {
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
											dob:item.dob,
											patientName:item.pFirstName,
											doa:item.doa,
											pdd:item.pdd,
											bedCategoryId:item.bedCategoryId,
											billingBedCategoryId:item.billingBedCategoryId,
											bedId:item.bedId,
											wardId:item.wardId,
											floorId:item.floorId,
											roomId:item.roomId
											
										};
								
								sessionStorage.setItem('bedManagmentCookies', JSON.stringify(bedManagmentERCookiesData));
								$scope.billing = true;
//								$scope.getAllPatientDiagnosis(item.patientId);
								$rootScope.allergydata(item);
//								$rootScope.allergydata($scope.admissionObj);
							}
							
							$scope.callAdmissionNote = function()
							{
								angular
								.element(
										'#patientServices')
								.modal('hide');
								
								//$rootScope.getAdmissionNote('ER');
								$timeout( function()
										{ 
									$rootScope.getAdmissionNote('ER');
									}, 2000);
								
							}

							// For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.initERAdmittedPatientList(
										$scope.organizationId, $scope.offset,
										$scope.noOfRecordsPerPage);
							};
							$scope.initERAdmittedPatientList = function(orgId,
									offset, noOfRecordsPerPage) {
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ GETERADMITTEDPATIENTLIST;
								var URI1 = BASE_URL + ROOT_URL
										+ COUNTERADMITTEDPATIENT;

								var data = {
									organizationId : orgId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								var data1 = {
									organizationId : $scope.organizationId
								}

								// For Get ErPatient List Using Offset
								/*
								 * GenericService .serviceAction("POST", URI,
								 * commonObj) .then( function(response) {
								 * $scope.listOfERAdmittedPatientList = [];
								 * $scope.ERadmittedPatientList = [];
								 * 
								 * if (response.data.status == "success") {
								 * $scope.ERadmittedPatientList =
								 * response.data.listObject; angular .forEach(
								 * $scope.ERadmittedPatientList, function(
								 * value, index) {
								 * 
								 * var newArr = {}; newArr.specialityName =
								 * value.specialityName; newArr.dFirstName =
								 * value.dFirstName; newArr.dMiddleName =
								 * value.dMiddleName; newArr.dFirstName =
								 * value.dFirstName; newArr.dLastName =
								 * value.dLastName; newArr.pFirstName =
								 * value.pFirstName; newArr.pMiddleName =
								 * value.pMiddleName; newArr.pLastName =
								 * value.pLastName; newArr.reasonDesc =
								 * value.reasonDesc; newArr.dob = value.dob;
								 * newArr.datepicker = value.doa;
								 * newArr.datepickerPdd = value.pdd;
								 * newArr.genderCode = value.genderCode;
								 * newArr.uhidnumber = value.uhidnumber;
								 * newArr.patientId = value.patientId;
								 * newArr.paymentEntitlementDesc =
								 * value.paymentEntitlementDesc;
								 * newArr.bedNumber = value.bedNumber;
								 * newArr.admissionId = value.admissionId;
								 * newArr.prefixCode = value.prefixCode;
								 * newArr.identificationNo =
								 * value.identificationNo;
								 * $scope.listOfERAdmittedPatientList
								 * .push(newArr);
								 * 
								 * });
								 *  } });
								 */

								// For Count ErPatient List
								/*
								 * GenericService .serviceAction("POST", URI1,
								 * data1) .then( function(response) { if
								 * (response.data.status == "success") {
								 * console.log(response.data);
								 * $scope.commonListCount =
								 * response.data.object; $scope.setPage(1,
								 * false);
								 *  } });
								 */
							}

							$scope.getERAdmittedPatientList = function(orgId,
									offset, noOfRecordsPerPage) {
								console.log("offset", offset);
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ GETERADMITTEDPATIENTLIST;
								var data = {
									organizationId : $scope.organizationId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}

								console.log("URI", URI);
								/*
								 * GenericService .serviceAction("POST", URI,
								 * commonObj) .then( function(response) {
								 * $scope.listOfERAdmittedPatientList = [];
								 * $scope.ERadmittedPatientList = [];
								 * 
								 * if (response.data.status == "success") {
								 * $scope.ERadmittedPatientList =
								 * response.data.listObject; angular .forEach(
								 * $scope.ERadmittedPatientList, function(
								 * value, index) {
								 * 
								 * var newArr = {}; newArr.specialityName =
								 * value.specialityName; newArr.dFirstName =
								 * value.dFirstName; newArr.dMiddleName =
								 * value.dMiddleName; newArr.dFirstName =
								 * value.dFirstName; newArr.dLastName =
								 * value.dLastName; newArr.pFirstName =
								 * value.pFirstName; newArr.pMiddleName =
								 * value.pMiddleName; newArr.pLastName =
								 * value.pLastName; newArr.reasonDesc =
								 * value.reasonDesc; newArr.dob = value.dob;
								 * newArr.datepicker = value.doa;
								 * newArr.datepickerPdd = value.pdd;
								 * newArr.genderCode = value.genderCode;
								 * newArr.uhidnumber = value.uhidnumber;
								 * newArr.patientId = value.patientId;
								 * newArr.paymentEntitlementDesc =
								 * value.paymentEntitlementDesc;
								 * newArr.bedNumber = value.bedNumber;
								 * newArr.admissionId = value.admissionId;
								 * newArr.prefixCode = value.prefixCode;
								 * newArr.identificationNo =
								 * value.identificationNo;
								 * $scope.listOfERAdmittedPatientList
								 * .push(newArr);
								 * 
								 * });
								 *  } });
								 */
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
									$scope.getERAdmittedPatientList(
											$scope.organizationId,
											$scope.pager.startIndex,
											$scope.pager.pageSize);
								}
							}

							$scope.initERAdmittedPatientList(
									$scope.organizationId, $scope.offset,
									$scope.noOfRecordsPerPage);

							// For Paginations End

							$scope.search = {
								allocationTypeId : 0,
								patientId : 0
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

							$scope.searchGroupList = function(keyword) {

								var data = {
									firstName : keyword,
									unitId : $scope.unitId,
									// visitType :
									// $scope.selectedPageNameForAdmissionNote,
									organizationId : $scope.organizationId
								}
								console.log(data);

								var URI = BASE_URL + ROOT_URL
//										+ GETPATIENTBYKEYWORD;
									 +"/adt/getPatientListLikeForERMapOfBed";
								return GenericService.serviceAction("POST",
										URI, data).then(function(response) {

									if (response.data.status == "success") {
										return response.data.listObject;

									}
								});

							}

							$scope.selectPatientId = function(info, model,
									label) {
								$scope.search.patientId = info.patientId
							}

							$scope.getERAdmittedPatientByAllocationPatientId = function() {
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									allocationType : $scope.search.allocationTypeId,
									patientId : $scope.search.patientId
								};
								console.log(data);
								/*
								 * var URI = BASE_URL + ROOT_URL +
								 * GETERADMITTEDPATIENTLISTBYALLOCATIONPATIENT;
								 * GenericService .serviceAction("POST", URI,
								 * data) .then( function(response) {
								 * $scope.listOfAdmittedPatientList = [];
								 * $scope.admittedPatientList = [];
								 * 
								 * if (response.data.status == "success") {
								 * $scope.admittedPatientList =
								 * response.data.listObject; angular .forEach(
								 * $scope.admittedPatientList, function( value,
								 * index) {
								 * 
								 * var newArr = {}; newArr.specialityName =
								 * value.specialityName; newArr.dFirstName =
								 * value.dFirstName; newArr.dMiddleName =
								 * value.dMiddleName; newArr.dFirstName =
								 * value.dFirstName; newArr.dLastName =
								 * value.dLastName; newArr.pFirstName =
								 * value.pFirstName; newArr.pMiddleName =
								 * value.pMiddleName; newArr.pLastName =
								 * value.pLastName; newArr.reasonDesc =
								 * value.reasonDesc; newArr.dob = value.dob;
								 * newArr.datepicker = value.doa;
								 * newArr.datepickerPdd = value.pdd;
								 * newArr.genderCode = value.genderCode;
								 * newArr.uhidnumber = value.uhidnumber;
								 * newArr.patientId = value.patientId;
								 * newArr.paymentEntitlementDesc =
								 * value.paymentEntitlementDesc;
								 * newArr.bedNumber = value.bedNumber;
								 * newArr.admissionId = value.admissionId;
								 * newArr.prefixCode = value.prefixCode;
								 * newArr.identificationNo =
								 * value.identificationNo;
								 * $scope.listOfAdmittedPatientList
								 * .push(newArr);
								 * 
								 * });
								 *  }
								 * //$scope.initRelationMasterList($scope.organizationId,
								 * $scope.offset, $scope.noOfRecordsPerPage);
								 * });
								 */
							}

							$scope.selectPatient = function(item) {

								$scope.itemSelecteds = [];
								// If checkbox is checked
								if ($scope.admittedPatient) {
									$scope.itemSelecteds = item;
									$scope.visitorPassPopUpFlag = true;

								}
							}

							
							var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
							
							/*$scope.getwardList = function(){
								   var URI =  BASE_URL + ROOT_URL+"/api/adt/getActiveWardListForER";
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.wardListForView = [];
		
													if (response.data.status == "success") {
														$scope.wardListForView = response.data.listObject;
														console.log("wardListForView",$scope.wardListForView);
													}
												});
								}
							$scope.getwardList();
							*/
							
							
							
							
							$scope.addCollection = function(mapList)
							{

								if(mapList!=null)
								  {
												/*$state.go('mapOfBedForER.MEWSnew',
													{
													 obj : mapList,
													});*/
												
												if(!angular.equals({},$stateParams.obj))
												{
												$scope.admissionObj = mapList,
												$scope.patientDetails.patientName = $scope.admissionObj.pFirstName;
												$scope.patientDetails.genderCode= ($scope.admissionObj.genderId==1)?"M":($scope.admissionObj.genderId==2?"F":($scope.admissionObj.genderId==3?"OTR":"-"));
//												$scope.patientDetails.age= moment().diff($scope.admissionObj.dob, 'years');
//												$scope.patientDetails.uhIdNumber= $scope.admissionObj.uhidnumber;
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
//												$scope.patientDetails.age= moment().diff($scope.admissionObj.dob, 'years');
//												$scope.patientDetails.uhIdNumber= $scope.admissionObj.uhidnumber;
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
												$scope.patientDetails.mapOfBedCheck= 'ER';
												 $scope.getMewsList();
											}
//												$state.go('mapOfBedForER.MEWShistory',
//														{
//													obj : mapList,
//														});
											
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

							
							 $scope.addCollection2 = function(mapList)
							 {

							 	if(mapList!=null)
							 	  {

                                                mapList.patientFromEr = true;
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
							 
                            if ($scope.patientDetails && $scope.patientDetails.patientId != null && $scope.patientDetails.patientId !== "") {
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
							 
							 $scope.hidetabDetails=function(info){
									var visible= $("#bedlistshow_"+info.wardId).is(":hidden"); 
									 if(visible==true){
										 $("#bedlistshow_"+info.wardId).removeClass("hidden");
									 }else{
										 $("#bedlistshow_"+info.wardId).addClass("hidden");
									 }

//									 {{info.wardName}}
								 }

                                 $scope.hideTabDetailsRoom=function(info){
                                     var visibleRoom= $("#roomListshow_"+info.roomId).is(":hidden");
                                     if(visibleRoom==true){
                                         $("#roomListshow_"+info.roomId).removeClass("hidden");
                                     }else{
                                         $("#roomListshow_"+info.roomId).addClass("hidden");
                                     }
//                                     {{info.wardName}}
                                 }
							 
								 var data={};
								var URI = BASE_URL + ROOT_URL 
								+ "/adt/getCountOnMewsMapper/"+$scope.patientDetails.patientId+"/"+$scope.organizationId+"/"+$scope.unitId ;
							GenericService.serviceAction("GET", URI,data).then(function(response) {
								
									if (response.data.status == 'success') {
										$scope.finalscoreList=response.data.listObject;
										console.log("score", $scope.finalscoreList);
									}
								});
								}
								

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
									  if(($scope.systolic !='' || parseInt($scope.systolic)== 0) &&
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


        $scope.goToPOCWorklist = function(mapList){
            if(mapList!=null)
            {
                cookieObjectAppConfigSecond.pocWorklistObj = {};
                cookieObjectAppConfigSecond.pocWorklistObj = mapList;
                localStorage.setItem('cookieObjectAppConfigSecond', JSON.stringify(cookieObjectAppConfigSecond));

                $state.go('pocWorklist',
                {
                    obj : mapList,
                });
            }
            else
           {
                growl.error("Something went Wrong.",{title : 'Error!'});
           }
        }
							 
				
						} ]);
