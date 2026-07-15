'use strict';

/**
 * @Author By Dinesh Jagatap
 * @name myApp.controller:unitServiceBasePriceController
 * @description #unitServiceBasePriceController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'unitServiceBasePriceController',
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
						'PagerService',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService,
								BillingGenericService, growl, $state,
								PagerService) {

							// for From date picker
							$scope.dateOptions = {
								formatYear : 'yyyy',
								showWeeks : false
							};

							$scope.openDatePicker = function() {
								$scope.datepicker.opened = true;
							};

							$scope.datepicker = {
								opened : false
							};
							// for To date picker
							$scope.openDatePickerTo = function() {
								$scope.datepickerTo.opened = true;
							};

							$scope.datepickerTo = {
								opened : false
							};						
							
							/* init() function for form object create. */
							
							$scope.init = function() {
								
								$scope.unitBasePriceObj={
										subSpecialityId:"",
										specialityId:"",
										//fromDate:new Date(),
										//toDate:new Date()
								}
								
								/*$scope.unitId = 1;
								$scope.organizationId = 1;
								$scope.userId = cookieObject.userId;
								if ($scope.userId == null) {
									$rootScope.loginpage = false;
									$state.go('login');
								}*/
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;

								$rootScope.loginpage = true;
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup					
							
							}
							$scope.init();
							
							// For Paginations Start
							$scope.setNoOfRecords = function() {
								$scope.initDesignationList(
										$scope.organizationId, $scope.offset,
										$scope.noOfRecordsPerPage,$scope.unitId);
							};
							$scope.initDesignationList = function(orgId,offset, noOfRecordsPerPage,unitId) {
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ "/api/unit/billing/getServiceBasePriceList";
								var URI1 = BASE_URL + ROOT_URL
										+ "/api/unit/billing/countServiceBasePriceMaster";

								var data = {
									orgnisationId : orgId,
									unitId : $scope.unitId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage
								}
								console.log("data", data);

								var data1 = {
									orgnisationId : $scope.organizationId
								}

								// For Get designation List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(function(response) {
													$scope.unitBasePriceList = [];

													if (response.data.status == "success") {
														$scope.unitBasePriceList = response.data.listObject;
														console.log("unitBasePriceList",$scope.unitBasePriceList);
													}
												});

								// For Count designation List
								GenericService
										.serviceAction("POST", URI1, data1)
										.then(
												function(response) {
													if (response.data.status == "success") {
														console.log(response.data);
														$scope.commonListCount = response.data.object;
														if($scope.updateBtnFlag==true){
															$scope.setPage($scope.pageNo,true);
														}
														else{
														    $scope.setPage(1,false);
															$scope.saveBtnFlag = true;
															$scope.updateBtnFlag=false;

														}

													}
												});
							}
							var pageOffset='';
							$scope.getDesignationList = function(orgId,offset, noOfRecordsPerPage,unitId) {
								console.log("offset", offset);
								pageOffset=offset;
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
										: 10;
								var URI = BASE_URL + ROOT_URL
										+ "/api/unit/billing/getServiceBasePriceList";
								var data = {
									orgnisationId : $scope.organizationId,
									offset : offset,
									noOfRecordsPerPage : noOfRecordsPerPage,
									unitId:unitId
								}

								console.log("URI", URI);
								// For Get designation List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.unitBasePriceList = [];

													if (response.data.status == "success") {
														$scope.unitBasePriceList = response.data.listObject;
														console.log("unitBasePriceList",$scope.unitBasePriceList);
													}
												});
							}

							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages) {
									return;
								}
								$scope.pager = PagerService.GetPager(
										$scope.commonListCount, page,
										$scope.noOfRecordsPerPage,$scope.unitId);
								if (flag) {
									$scope.getDesignationList($scope.organizationId,$scope.pager.startIndex,
											$scope.pager.pageSize,$scope.unitId);
								}
							}

							$scope.initDesignationList($scope.organizationId,
									$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);

							// For Paginations End
							
							// For Speciality under modality
							/*$scope.activeSpeciality=function(){
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
													$scope.speacilityList = [];
													if (response.data.status == "success") {														
														$scope.speacilityList=response.data.listObject;																		
													}
													console.log("speciality",$scope.speacilityList);
//													$scope.getSpecialityAndSubspeciality();
												});	
							}
							$scope.activeSpeciality();							
							$scope.getSpecialityId = function(specialityId) {
										var data = {
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											specialityId : specialityId
										}
										var URI = BASE_URL+ ROOT_URL+ GETSUBSPECIALITYBYSPECIALITYID;
										BillingGenericService
												.serviceAction(METHOD_POST,URI, data,NOTIFICATION_MSG_STATUS_FALSE)
												.then(function(response) {
													$scope.subSpecialityList = [];
															if (response.data.status == "success") {																
																$scope.subSpecialityList=response.data.listObject;																			
															   }
															console.log("subspeciality",$scope.subSpecialityList);
														});
									

										$scope.getSpecialityAndSubspeciality();
							}
							$scope.getSpecialityAndSubspeciality = function() {
								var specialistList = [];
								specialistList.push($scope.unitBasePriceObj.specialityId);
								var subSpecialistList = [];
								subSpecialistList.push($scope.unitBasePriceObj.subSpecialityId);
							
								var data = {
									unitId : $scope.unitId,
									orgnisationId : $scope.organizationId,
									specialityIdList : specialistList,
									subSpecialityIdList : subSpecialistList
								}
								var URI = BASE_URL
										+ ROOT_URL
										+ "/global/searchUnitServiceByMultipleSpecialityAndSubSpeciality";
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.serviceList = [];
														angular.forEach(response.data.listObject,
																		function(value,index) {
																			var data = {};
																			data.id = value.serviceId;
																			data.label = value.serviceStandardName;
																			$scope.serviceList.push(data);
																		});
														console.log('serviceList',$scope.serviceList);
													}
												});
								// }
							}	*/		 
							  
													
							$scope.showUpdateBtn = function(id) {

								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								var data = {

								}
								var URI = BASE_URL + ROOT_URL
										+ "/api/unit/billing/getServiceBasePriceListById" + "/"+ id ;
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													if (response.data.status == "success") {
														console.log("edit",response.data.listObject);
														$scope.unitBasePriceObj.specialityId=response.data.listObject[0].specialityId.toString();
														$scope.unitBasePriceObj.specialityName=response.data.listObject[0].specialityName;
														$scope.unitBasePriceObj.subSpecialityId=response.data.listObject[0].subSpecialityMasterId.toString();
														$scope.unitBasePriceObj.subSpecialityMasterName=response.data.listObject[0].subSpecialityMasterName;
														$scope.unitBasePriceObj.serviceStandardCode=response.data.listObject[0].serviceStandardCode;
														$scope.unitBasePriceObj.serviceName=response.data.listObject[0].serviceName;
														$scope.unitBasePriceObj.serviceMasterId=response.data.listObject[0].serviceMasterId;
														$scope.unitBasePriceObj.basePrice=response.data.listObject[0].basePrice;
														//$scope.unitBasePriceObj.fromDate=(response.data.listObject[0].fromDate !=null?new Date(response.data.listObject[0].fromDate):new Date());
														//$scope.unitBasePriceObj.toDate=(response.data.listObject[0].toDate !=null?new Date(response.data.listObject[0].toDate):new Date());
														$scope.unitBasePriceObj.serviceBasePriceId=response.data.listObject[0].serviceBasePriceId;														
													} else {
														alert("Error!!");
													}
												});
							}
							$scope.updateStatus=function(id,type){
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								type = (type == "A") ? "A" : "I";
								var data = {
									serviceBasePriceId : id,
									status : type,
									updatedBy : $scope.userId,
									updatedDate : $scope.currentDate
								}
								var URI = BASE_URL + ROOT_URL
										+ "/api/unit/billing/updateServiceBasePriceMasterStatus";
								GenericService.serviceAction("POST", URI, data)
										.then(function(response) {
													if (response.data.status == "success") {
														$scope.init();
														growl.success(response.data.message,
																		{
																			title : 'Success!'
																		});
													} else {
														growl.error(response.data.message,
																		{
																			title : 'Error!'
																		});
													}
												});
							}
							$scope.pageNo='';
							$scope.updateUnitBaseService=function(){
								$rootScope.startLoader();
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								var data = {
										serviceBasePriceId : $scope.unitBasePriceObj.serviceBasePriceId,
										//toDate :moment($scope.unitBasePriceObj.toDate).format('DD-MM-YYYY HH:mm:ss'),								
										//fromDate : moment($scope.unitBasePriceObj.fromDate).format('DD-MM-YYYY HH:mm:ss'),
										basePrice:$scope.unitBasePriceObj.basePrice,
										serviceMasterId:$scope.unitBasePriceObj.serviceMasterId,
									    updatedBy : $scope.userId,
									    updatedDate : $scope.currentDate
								}				
//								console.log("update",data); return false;
//								if ($scope.designationForm.$valid) {
									var URI = BASE_URL + ROOT_URL+"/api/unit/billing/updateServiceBasePriceMaster";
									GenericService.serviceAction("POST", URI, data)
											.then(function(response) {
														if (response.data.status == "success") {
															/*$scope.init();*/
															$scope.saveBtnFlag = true;
															$scope.updateBtnFlag = false;
															$scope.unitBasePriceObj={}
															$rootScope.stopLoader();
															growl.success(response.data.message,
																			{
																				title : 'Success!'
																			});
														} else {
															growl.error(response.data.message,
																			{
																				title : 'Error!'
																			});
															$rootScope.stopLoader();
														}
														
														$scope.offset=pageOffset;
														console.log($scope.offset)
														$scope.initDesignationList(
																        $scope.organizationId,
																		$scope.offset,
																		$scope.noOfRecordsPerPage);
														$scope.pageNo=($scope.offset/10)+1;
													    $scope.setPage($scope.pageNo,true)
													    console.log("pageNo"+$scope.pageNo)
														
													});
//								}
							}
							
							$scope.searchunitServicebasePrice = function(searchKeyword) {
								//orgId,offset, noOfRecordsPerPage,unitId
								if(searchKeyword!=''){
								
								var URI = BASE_URL + ROOT_URL
										+ "/api/unit/billing/getServiceBasePriceListSearch";
								var data = {
										orgnisationId : $scope.organizationId,
										       unitId : $scope.unitId,
										  serviceName : searchKeyword
								}
								$scope.unitBasePriceList=[];
								$scope.loadingMsg="Loading...";
								console.log("URI", URI);
								// For Get designation List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.unitBasePriceList = [];

													if (response.data.status == "success") {
														$scope.unitBasePriceList = response.data.listObject;
//														console.log("unitBasePriceList",$scope.unitBasePriceList);
														if($scope.unitBasePriceList.length==0)
															$scope.loadingMsg="No record found...";
													}
												});
								}else{
									$scope.initDesignationList($scope.organizationId,
											$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
								}
								
							}
							
							

						} ]);
