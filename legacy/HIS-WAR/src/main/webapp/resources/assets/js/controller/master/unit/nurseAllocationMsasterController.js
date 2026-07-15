'use strict';

/**
 * @Author By  Sidheshwar Lamture
 * @name myApp.controller:nurseAllocationMsasterController
 * @description #nurseAllocationMsasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'nurseAllocationMsasterController',
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
						'PagerService',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, PagerService) {

							/* init() function for form object create. */
							$rootScope.loginpage = true;
							$scope.saveBtnFlag = true;
							$scope.updateBtnFlag = false;
							$scope.updateScope = false;
							$scope.popUpFlag = true;

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.commonList = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;
							
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$rootScope.loginpage = true;
								$scope.visitTypeId =1;
								
							var date = new Date();
							$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
							
							$scope.initMaster = function() {
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
								
								$scope.commonObj = {
										roleId : "",
										userId : "",
										organizationId :$scope.organizationId,
										unitId :$scope.unitId,
										createdBy :$scope.userId,
										updatedBy :$scope.userId,
										createdDate :$scope.dt,
										updatedDate :$scope.dt,
										status :'A'
								};
								
								$scope.nurseAllocOpObj={
									specialityId : "",
									doctorId : "",
									cabinId : ""
								};
								
								$scope.opvisitTypeIdForUpdate="";
								$scope.ipVisitUpdate="";
								
								$scope.nurseAllocIpObj={
										wardId : "",
										nursingStationId : ""
								};
								
								
							}
							$scope.initMaster();
							$scope.wardIdListIp = [];
							$scope.wardIdListEr = [];
							$scope.wardIdListDayCare = [];
						
							/** List And Pagination Start* */
							$scope.setNoOfRecords = function() {
								$scope.initMasterList($scope.organizationId,
										$scope.offset,
										$scope.noOfRecordsPerPage,
										$scope.unitId);
							};

							$scope.initMasterList = function(organizationId,offset, noOfRecordsPerPage, unitId) {
								$scope.$broadcast('show-errors-reset');
								console.log($scope.visitTypeId);
								$rootScope.startLoader();
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage:10;
								var data1 = {
										organizationId : organizationId,
										offset : offset,
										noOfRecordsPerPage : noOfRecordsPerPage,
										unitId : unitId,
										visitTypeId:$scope.visitTypeId
								};

								var data2 = {
										organizationId : organizationId,
										unitId : unitId,
										visitTypeId:$scope.visitTypeId
								};

								var URI1 = BASE_URL + ROOT_URL+ GETNURSEALLOCATIONMASTERLIST;
								var URI2 = BASE_URL + ROOT_URL+ GETNURSEALLOCATIONMASTERCOUNT;

								GenericService.serviceAction("POST", URI1, data1).then(function(response) {
											if (response.data.status == "success") {
												$rootScope.stopLoader();
												if($scope.visitTypeId==1){
													$scope.nurseAllocationOpList=response.data.listObject;;
												}else{
													$scope.nurseAllocationIpList=response.data.listObject;;
												}
												console.log("nurseAllocationOpList1:",$scope.nurseAllocationOpList);
												console.log("nurseAllocationIpList1:",$scope.nurseAllocationIpList);
											}
										});
								
								GenericService.serviceAction("POST", URI2, data2).then(function(response) {
											if (response.data.status == "success") {
												$rootScope.stopLoader();
												$scope.commonListCount = response.data.object;
												$scope.setPage(1,false);
												console.log("nurseAllocationMasterListcount",response.data.object);
											}
										});

							   }

							   $scope.masterList = function(organizationId,offset, noOfRecordsPerPage, unitId) {
								          $rootScope.startLoader();
								           console.log("offset", offset);
								          offset = offset != null ? offset : 0;
								           noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;

								var data = {
										organizationId : organizationId,
										offset : offset,
										noOfRecordsPerPage : noOfRecordsPerPage,
										unitId : unitId,
										visitTypeId:$scope.visitTypeId
								};

								var URI = BASE_URL + ROOT_URL+ GETNURSEALLOCATIONMASTERLIST;
								console.log("URI", URI);
								GenericService.serviceAction("POST", URI, data).then(function(response) {
											$rootScope.stopLoader();
											$scope.nurseAllocationOpList=[];
											$scope.nurseAllocationIpList=[];
											if($scope.visitTypeId==1){
												$scope.nurseAllocationOpList=response.data.listObject;;
											}else{
												$scope.nurseAllocationIpList=response.data.listObject;;
											}
											console.log("nurseAllocationOpList:",$scope.nurseAllocationOpList);
											console.log("nurseAllocationIpList:",$scope.nurseAllocationIpList);
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
										$scope.noOfRecordsPerPage,
										$scope.unitId);
								if (flag) {
									    $scope.masterList($scope.organizationId,
										$scope.pager.startIndex,
										$scope.pager.pageSize,
										$scope.unitId);
								}
							}

							$scope.initMasterList($scope.organizationId,
							$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);

							/** List And Pagination End* */

							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.save();
							}

							   $scope.roleList = [];
								$scope.getActiveRoleList = function(){
									var data= {
									};
							var roleTypeId=2;
							//var URI1 = BASE_URL + ROOT_URL +"/api/global/getActiveUserRoleMasterList"; 
							var URI1 = BASE_URL + ROOT_URL +"/api/global/getRoleNameByRoleTypeId/"+roleTypeId+"/"+$scope.organizationId+"/"+$scope.unitId;
							GenericService.serviceAction("GET",URI1,data).then(function(response){
										if(response.data.status == "success"){
											$scope.roleList=response.data.listObject;
											console.log("Role List::", $scope.roleList);
										}
									});
								}
							$scope.getActiveRoleList();
							
							$scope.getNurseListByRoleId=function(roleId){
								var data= {
										organisationId:$scope.organizationId,
									 	unitId:$scope.unitId,
									 	roleId:roleId
									};
					
							var URI1 = BASE_URL + ROOT_URL +"/api/global/getUserListByRoleId"; 
							GenericService.serviceAction("POST",URI1,data).then(function(response){
								if(response.data.status == "success"){
									$scope.nurseList=response.data.listObject;
									console.log("NurseList List::", $scope.nurseList);
								}
							});
								
							}
							
							$scope.getActiveSpecialityList=function(){
							var URI = BASE_URL + ROOT_URL
							+ GETACTIVESPECIALITY;
							var data = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								
							GenericService.serviceAction("POST",URI,data)
							.then(
									function(response) {
										console.log(response);
										// $rootScope.startLoader();
										if (response.data.status == "success")
											$scope.specialityList = response.data.listObject;
									});
							}
							$scope.getActiveSpecialityList();

					$scope.getDoctorBySpeciality = function(id) {

						var data = {
							specialityId : id,
							unitId : $scope.unitId,
							organizationId : $scope.organizationId
						};
						var URI = BASE_URL + ROOT_URL
								+ GETDOCTORBYSPECIALITYID;
						
						GenericService.serviceAction("POST",URI,data)
								.then(
										function(response) {
											console.log(response);
											$scope.doctorList = [];
											// $rootScope.startLoader();
											if (response.data.status == "success")
												$scope.doctorList = response.data.listObject;
										});

					};
					
					$scope.getDoctorCabinMapperByDoctorId = function(doctorId) {
						try {
							var data = "";
							$scope.getListObject=[];
							var URI = BASE_URL + ROOT_URL+GET_DETAILED_CABIN_LIST+S+doctorId+S+$scope.organizationId+S+$scope.unitId;
							console.log("URI", URI);
							GenericService
									.serviceAction("GET", URI, "")
									.then(
											function(response) {
												$scope.cabinList  = response.data.listObject;
												console.log("cabinList",JSON.stringify($scope.cabinList));
											});
						} catch (e) {
							console.log(e.message)
						}

					}
					
					$scope.getWardMasterList = function() {

						var data = {
							unitId : $scope.unitId,
							organizationId : $scope.organizationId
						};
						var URI = BASE_URL + ROOT_URL
								+ "/api/adt/getActiveWardMasterList";
						
						GenericService.serviceAction("POST",URI,data)
								.then(
										function(response) {
											console.log(response);
											$scope.doctorList = [];
											if (response.data.status == "success")
												$scope.wardList = response.data.listObject;
											angular.forEach($scope.wardList,function(value,index) {
												var wardObj = {};
												wardObj.wardId = value.wardId;
												wardObj.wardName = value.wardName;
												if(value.isDayCare=='Y'){
													$scope.wardIdListDayCare.push(wardObj);
												}
												
												if(value.isIpWard=='Y'){
													$scope.wardIdListIp.push(wardObj);
												}	
												$scope.wardIdListIp
												if(value.isEr=='Y'){
													$scope.wardIdListEr.push(wardObj);
												}
											
												});
											
											    console.log("$scope.wardList:",$scope.wardList);
											    console.log("$scope.wardIdListIp:",$scope.wardIdListIp);
											    console.log("$scope.wardIdListDayCare:",$scope.wardIdListDayCare);
											    console.log("$scope.wardIdListEr:",$scope.wardIdListEr);
										});

					};	
               $scope.getWardMasterList();
               
               $scope.getNursingStationByWardId=function(wardId){
            	  
            	 var URI3 = BASE_URL + ROOT_URL+ "/api/adt/unit/getNursingStationListByWardId"+S + wardId + S + $scope.organizationId+ S + $scope.unitId;;
   				var data = {}
   				
   				GenericService.serviceAction("GET", URI3, data).then(function(response) {
   					
   							$scope.nursingStationList = [];
   							$scope.nursingStationList = response.data.listObject;
   							console.log("nursingStationList:",$scope.nursingStationList);

   						});
               }
               
              
				
				//for SAVE data

				$scope.save = function() {
					try {

						var date = new Date();
						$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
						 $scope.$broadcast('show-errors-check-validity');
						if ($scope.nurseAlloForm.$valid || $scope.nurseAlloIPForm.$valid) {
						if ($scope.popUpFlag) {
							$scope.showModal = !$scope.showModal;

						} else {
							$rootScope.startLoader();
							var data = {
									organizationId :$scope.organizationId,
									unitId :$scope.unitId,
									createdBy :$scope.userId,
									updatedBy :$scope.userId,
									createdDate :$scope.dt,
									updatedDate :$scope.dt,
									status :'A',
									roleId : $scope.commonObj.roleId,
									userId : $scope.commonObj.userId,
									visitTypeId : $scope.visitTypeId
								};
							if($scope.visitTypeId==1){
								data.specialityId=$scope.nurseAllocOpObj.specialityId;
								data.doctorId=$scope.nurseAllocOpObj.doctorId;
								data.cabinId=$scope.nurseAllocOpObj.cabinId;
							}else{
								data.wardId=$scope.nurseAllocIpObj.wardId;
								data.nursingStationId=$scope.nurseAllocIpObj.nursingStationId;
							}
							var URI = BASE_URL + ROOT_URL+ SAVENURSEALLOCATIONMASTER;
							console.log("Save-URI-", URI);
							console.log(" save OBJ", data);

							GenericService.serviceAction("POST", URI,data).then(function(response) {

								if (response.data.status == 'success') {
									growl.success(response.data.message,
											{
												title : 'Success!'
											});

									$scope.$broadcast('show-errors-reset');
									$scope.initMaster();
									$scope.initMasterList(
											$scope.organizationId,
											$scope.offset,
											$scope.noOfRecordsPerPage,
											$scope.unitId);
								    } else if (response.data.status == 'error') {
									growl.error(response.data.message,
											{
												title : 'Error!'
											});
									$scope.initMaster();
									$scope.$broadcast('show-errors-reset');
								   }
								$rootScope.stopLoader();
							});
							
						}
					}	

					} catch (e) {
						console.log("Exception-", e);
					}
				}

				$scope.showDetailsBtn = function(data) {
					$scope.configDetails = data;
				}
				//op ip er day
				
				$scope.visitTypeList=[];
				$scope.visitTypeList.push({visitTypeId:1,visitType:'OP'});
				$scope.visitTypeList.push({visitTypeId:2,visitType:'IP'});
				$scope.visitTypeList.push({visitTypeId:3,visitType:'Day Care'});
				$scope.visitTypeList.push({visitTypeId:4,visitType:'ER'});
				
				$scope.showUpdateBtn = function(id) {
					try {
						$rootScope.startLoader();
						$scope.saveBtnFlag = false;
						$scope.updateScope = true;
						$scope.updateBtnFlag = true;
						$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL+ GETNURSEALLOCATIONMASTERBYID + S+ id + S + $scope.organizationId+ S + $scope.unitId;
						console.log("EDIT", URI);
						GenericService.serviceAction("GET", URI).then(function(response) {
									console.log("Edit",response);
									if (response.data.status == "success") {
										if(response.data.object.roleId!=null)
										$scope.getNurseListByRoleId(response.data.object.roleId);
										if(response.data.object.specialityId!=null)
										$scope.getDoctorBySpeciality(response.data.object.specialityId);
										if(response.data.object.doctorId!=null)
										$scope.getDoctorCabinMapperByDoctorId(response.data.object.doctorId);
										if(response.data.object.wardId!=null)
										$scope.getNursingStationByWardId(response.data.object.wardId);
						
										$scope.commonObj.nurseAllocationId= response.data.object.nurseAllocationId;	
									$scope.commonObj.roleId= response.data.object.roleId;
								    $scope.commonObj.userId= response.data.object.userId;
									$scope.nurseAllocOpObj.specialityId= response.data.object.specialityId;
									$scope.nurseAllocOpObj.doctorId= response.data.object.doctorId;
									$scope.nurseAllocOpObj.cabinId= response.data.object.cabinId;
									$scope.nurseAllocIpObj.wardId= response.data.object.wardId;
									$scope.nurseAllocIpObj.nursingStationId= response.data.object.nursingStationId;
									$scope.visitTypeId=	response.data.object.visitTypeId;
									$scope.finalVisitTypeList=[];
									angular.forEach($scope.visitTypeList,function(value,index){
										if($scope.visitTypeId!=value.visitTypeId){
											$scope.finalVisitTypeList.push(value);
										}
											
									});
									console.log("final list & id ",$scope.finalVisitTypeList+" ,"+$scope.visitTypeId);
									
									} else if (response.data.status == 'error') {
										growl.error(
												response.data.message,
												{
													title : 'Error!'
												});
									}
									$rootScope.stopLoader();
								});


					} catch (e) {
						console.log("Exception-", e);
					}
				}

				$scope.getVisitType = function(id) {
					if(id!=undefined && id!="" && id!=null){
						$scope.ipVisitUpdate=id;
					}
					console.log("ip vis",$scope.ipVisitUpdate);
				}
				
				$scope.getVisitTypeForOp=function(id){
					if(id!=undefined && id!="" && id!=null){
						$scope.opvisitTypeIdForUpdate=id;
					}
				}
				//For update 
				$scope.update = function() {
					try {
                            var date = new Date();
						     $scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
						     $scope.$broadcast('show-errors-check-validity');
						    if ($scope.nurseAlloForm.$valid || $scope.nurseAlloIPForm.$valid) {
						     $rootScope.startLoader();
						     var data = {
										organizationId :$scope.organizationId,
										unitId :$scope.unitId,
										createdBy :$scope.userId,
										updatedBy :$scope.userId,
										createdDate :$scope.dt,
										updatedDate :$scope.dt,
										status :'A',
										roleId : $scope.commonObj.roleId,
										userId : $scope.commonObj.userId,
										//visitTypeId : $scope.visitTypeIdForUpdate!=0 ? $scope.visitTypeIdForUpdate:$scope.visitTypeId ,
										nurseAllocationId : $scope.commonObj.nurseAllocationId
									};
						     
								if($scope.visitTypeId==1){
									data.visitTypeId= $scope.opvisitTypeIdForUpdate!='' ? $scope.opvisitTypeIdForUpdate : $scope.visitTypeId;
									data.specialityId=$scope.nurseAllocOpObj.specialityId;
									data.doctorId=$scope.nurseAllocOpObj.doctorId;
									data.cabinId=$scope.nurseAllocOpObj.cabinId;
								}else{
									data.visitTypeId= $scope.ipVisitUpdate!='' ? $scope.ipVisitUpdate : $scope.visitTypeId;
									data.wardId=$scope.nurseAllocIpObj.wardId;
									data.nursingStationId=$scope.nurseAllocIpObj.nursingStationId;
								}
						var URI = BASE_URL + ROOT_URL+ UPDATENURSEALLOCATIONMASTER;
						console.log("Update-URI-", URI);
						console.log("Update OBJ",  data);
						$rootScope.startLoader();
						GenericService.serviceAction("POST", URI,  data).then(function(response) {

							if (response.data.status == 'success') {
								if($scope.visitTypeId==1 && $scope.opvisitTypeIdForUpdate!="" ){
									var message="Nurse is allocated to "+$scope.visitTypeList[$scope.opvisitTypeIdForUpdate-1].visitType+" and deallocated from "+ $scope.visitTypeList[$scope.visitTypeId-1].visitType;
									growl.success(message,
											{
										title : 'Success!'
											});	
								}else if($scope.visitTypeId!=1 && $scope.ipVisitUpdate!="" ){
									var message="Nurse is allocated to "+$scope.visitTypeList[$scope.ipVisitUpdate].visitType+" and deallocated from "+$scope.visitTypeList[$scope.visitTypeId-1].visitType;
									growl.success(message,
											{
										title : 'Success!'
											});	
								}else{
								growl.success("Nurse allocation updated successfully",
										{
									title : 'Success!'
										});
								}
								
								// $scope.$broadcast('show-errors-reset');
								$scope.initMaster();
								$scope.initMasterList(
								$scope.organizationId,
								$scope.offset,
								$scope.noOfRecordsPerPage,
								$scope.unitId);
							} else if (response.data.status == 'error') {
								growl.error(
										response.data.message,
										{
											title : 'Error!'
										});
							}
							$rootScope.stopLoader();
						});
					}
					} catch (e) {
						console.log("Exception-", e);
					}
				}


				//update status function 
				$scope.updateStatus = function(id, status) {
					try {
						var date = new Date();
						$scope.dt = moment(date).format(
								'DD-MM-YYYY HH:mm:ss');

						 var data = {
									organizationId :$scope.organizationId,
									unitId :$scope.unitId,
									updatedBy :$scope.userId,
									updatedDate :$scope.dt,
									status :status,
									roleId : $scope.commonObj.roleId,
									userId : $scope.commonObj.userId,
									visitTypeId : $scope.visitTypeId,
									nurseAllocationId : id
								};
						var URI = BASE_URL + ROOT_URL+ UPDATENURSEALLOCATIONMASTERSTATUS;
						console.log("update status",data);
						// return false;
						GenericService.serviceAction("POST", URI,data).then(
										function(response) {
											if (response.data.status == 'success') {
												growl.success(response.data.message,
														{
															title : 'Success!'
														});
												// $scope.$broadcast('show-errors-reset');
												        $scope.initMaster();
												        $scope.initMasterList(
																$scope.organizationId,
																$scope.offset,
																$scope.noOfRecordsPerPage,
																$scope.unitId);
											} else if (response.data.status == 'error') {
												growl.error(
														response.data.message,
														{
															title : 'Error!'
														});
											}
											$rootScope.stopLoader();
										});
					} catch (e) {
						console.log("Exception", e);
					}
				}

				/** Crud Operations End* */
               
               
               
          
						} ]);
