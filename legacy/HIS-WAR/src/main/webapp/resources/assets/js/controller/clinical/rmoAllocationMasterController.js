'use strict';

/**
 * @Author By  Sidheshwar Lamture
 * @name myApp.controller:nurseAllocationMsasterController
 * @description #nurseAllocationMsasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'rmoAllocationMasterController',
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
										rmoDoctorId : "",
										organizationId :$scope.organizationId,
										unitId :$scope.unitId,
										createdBy :$scope.userId,
										updatedBy :$scope.userId,
										createdDate :$scope.dt,
										updatedDate :$scope.dt,
										status :'A'
								};
								
								$scope.rmoAllocOpObj={
									specialityId : "",
									doctorId : ""
								};
								
								
								$scope.rmoAllocIpObj={
										wardId : ""
								};
								
								
							}
							$scope.initMaster();
							
							$scope.selectedDoctorList = [];
							$scope.selectedIpWardList = [];
							$scope.selectedDayCareWardList = [];
							$scope.selectedErWardList = [];
							
							// console.log($scope.selectedbedList)

							$scope.doctorIdList = [];
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
								console.log($scope.visitTypeId)
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

								var URI1 = BASE_URL + ROOT_URL+ GETRMODOCTORALLOCATIONMASTERLIST;
								var URI2 = BASE_URL + ROOT_URL+ GETRMODOCTORALLOCATIONMASTERCOUNT;

								GenericService.serviceAction("POST", URI1, data1).then(function(response) {
											if (response.data.status == "success") {
												$rootScope.stopLoader();
												if($scope.visitTypeId==1){
													$scope.rmoAllocationOpList=response.data.listObject;;
												}else{
													$scope.rmoAllocationIpList=response.data.listObject;;
												}
												console.log("rmoAllocationOpList1:",$scope.rmoAllocationOpList);
												console.log("rmoAllocationIpList1:",$scope.rmoAllocationIpList);
											}
										});
								
								GenericService.serviceAction("POST", URI2, data2).then(function(response) {
											if (response.data.status == "success") {
												$rootScope.stopLoader();
												$scope.commonListCount = response.data.object;
												$scope.setPage(1,true);
												console.log("rmoAllocationMasterListcount",response.data.object);
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

								var URI = BASE_URL + ROOT_URL+ GETRMODOCTORALLOCATIONMASTERLIST;
								console.log("URI", URI);
								GenericService.serviceAction("POST", URI, data).then(function(response) {
											$rootScope.stopLoader();
											$scope.rmoAllocationOpList=[];
											$scope.rmoAllocationIpList=[];
											if($scope.visitTypeId==1){
												$scope.rmoAllocationOpList=response.data.listObject;;
											}else{
												$scope.rmoAllocationIpList=response.data.listObject;;
											}
											console.log("rmoAllocationOpList:",$scope.rmoAllocationOpList);
											console.log("rmoAllocationIpList:",$scope.rmoAllocationIpList);
								});
								
								
							}
							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 && page > $scope.pager.totalPages) {
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

							  
							$scope.rmoDoctorList=[];
							$scope.getRMODoctorList=function(){
								var data= {
										organizationId:$scope.organizationId,
									 	unitId:$scope.unitId
									};
					
							var URI1 = BASE_URL + ROOT_URL +"/api/global/getRMODoctorList"; 
							GenericService.serviceAction("POST",URI1,data).then(function(response){
								if(response.data.status == "success"){
									$scope.rmoDoctorList=response.data.listObject;
									console.log("Rmo Doctor List ::", $scope.rmoDoctorList);
								}
							});
								
							}
							$scope.getRMODoctorList();
							
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
						$scope.doctorIdList=[];
						GenericService.serviceAction("POST",URI,data)
								.then(
										function(response) {
											console.log(response);
											$scope.doctorList = [];
											// $rootScope.startLoader();
											if (response.data.status == "success")
												$scope.doctorList = response.data.listObject;
													angular
															.forEach(
																	$scope.doctorList,
																	function(
																			value,
																			index) {
																		var bed = {};
																		bed.id = value.doctorId;
																		bed.label = value.firstName+" "+value.lastName;
																		$scope.doctorIdList
																				.push(bed);

																	});
										});

					};
					
				
					
					$scope.getWardMasterList = function() {
						$scope.selectedWardList = [];
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
											// $rootScope.startLoader();
											if (response.data.status == "success")
												$scope.wardList = response.data.listObject;
											angular.forEach($scope.wardList,function(value,index) {
											var wardObj = {};
											wardObj.id = value.wardId;
											wardObj.label = value.wardName;
											//$scope.wardIdList.push(wardObj);
											if(value.isDayCare=='Y'){
												$scope.wardIdListDayCare.push(wardObj);
											}
											
											if(value.isIpWard=='Y'){
												$scope.wardIdListIp.push(wardObj);
											}
											
											if(value.isEr=='Y'){
												$scope.wardIdListEr.push(wardObj);
											}
											
										
											});
										});

					};	
               $scope.getWardMasterList();
               
           
               $scope.showMappedDoctorList=function(rmoAllocationId){
            	   var data = {
							unitId : $scope.unitId,
							organizationId : $scope.organizationId,
							rmoAllocationId : rmoAllocationId
						};
						var URI = BASE_URL + ROOT_URL
								+ "/api/unit/clinical/getDoctorListByRmoallocationId";
						
						GenericService.serviceAction("POST",URI,data)
								.then(
										function(response) {
											console.log(response);
											$scope.doctorList = [];
											// $rootScope.startLoader();
											if (response.data.status == "success")
												$scope.mappedDoctorList = response.data.listObject;
										});

               }
				
               $scope.showMappedWardList = function(id) {
					try {
						$scope.mappedWardList=[];
						$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL+ GETRMODOCTORALLOCATIONMASTERBYID + S+ id + S + $scope.organizationId+ S + $scope.unitId;
						console.log("EDIT", URI);
						GenericService.serviceAction("GET", URI).then(function(response) {
									console.log("Edit",response);
									if (response.data.status == "success") {
										$scope.mappedWardList=response.data.object.selectedWardList
						
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

				//for SAVE data

				$scope.save = function() {
					try {

						/*if ($scope.popUpFlag == false) {
							$rootScope.startLoader();
						}*/
						var date = new Date();
						$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
						$scope.$broadcast('show-errors-check-validity');
					
						
						if (($scope.visitTypeId!=1 && $scope.rmoAllocIp.$valid) || ($scope.visitTypeId==1 && $scope.rmoAllocOp.$valid)) {
							if($scope.visitTypeId==1){
								if($scope.selectedDoctorList.length==0){
									 growl.warning(
												'Select doctor!!',
												{
													title : 'Warning!'
												});
									return false;
								}
							}else{
								if(($scope.visitTypeId==2 && $scope.selectedIpWardList.length==0) || 
										($scope.visitTypeId==3 && $scope.selectedDayCareWardList.length==0)
										|| ($scope.visitTypeId==4 && $scope.selectedErWardList.length==0)){
									growl.warning(
											'Select ward !!',
											{
												title : 'Warning!'
											});
								   return false;
								}
								 
							}
							
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
									rmoDoctorId: $scope.commonObj.rmoDoctorId,
									visitTypeId : $scope.visitTypeId
								};
							if($scope.visitTypeId==1){
								data.specialityId=$scope.rmoAllocOpObj.specialityId;
								data.doctorList=$scope.selectedDoctorList;
							}else{
								if($scope.visitTypeId==2){
									data.selectedWardList=$scope.selectedIpWardList;
								}else if($scope.visitTypeId==3){
									data.selectedWardList=$scope.selectedDayCareWardList;
								}else{
									data.selectedWardList=$scope.selectedErWardList;
								}
								
								//data.wardId=$scope.rmoAllocIpObj.wardId;
							}
							var URI = BASE_URL + ROOT_URL+ SAVERMODOCTORALLOCATIONMASTER;
							console.log("Save-URI-", URI);
							console.log(" save OBJ", data);

							GenericService.serviceAction("POST", URI,data).then(function(response) {

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
									$scope.selectedDoctorList = [];
									$scope.selectedWardList = [];
									$scope.selectedIpWardList = [];
									$scope.selectedDayCareWardList = [];
									$scope.selectedErWardList = [];
								    } else if (response.data.status == 'error') {
									growl.error(response.data.message,
											{
												title : 'Error!'
											});
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
				$scope.isActiveForOp='N';
				$scope.isActiveForIp='N';
				$scope.showUpdateBtn = function(id) {
					try {
						$rootScope.startLoader();
						$scope.saveBtnFlag = false;
						$scope.updateScope = true;
						$scope.updateBtnFlag = true;
						$scope.isActiveForOp='N';
						$scope.isActiveForIp='N';
						$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL+ GETRMODOCTORALLOCATIONMASTERBYID + S+ id + S + $scope.organizationId+ S + $scope.unitId;
						console.log("EDIT", URI);
						GenericService.serviceAction("GET", URI).then(function(response) {
									console.log("Edit",response);
									if (response.data.status == "success") {
										
										if(response.data.object.specialityId!=null)
										$scope.getDoctorBySpeciality(response.data.object.specialityId);
						
									$scope.commonObj.rmoAllocationId= response.data.object.rmoAllocationId;	
									$scope.rmoAllocOpObj.specialityId= response.data.object.specialityId;
									$scope.savedDoctorList=[];
									angular.forEach(response.data.object.doctorList,function(value,index){
										var obj={};
										obj.id=value.doctorId;
										obj.label=value.doctorName;
										$scope.savedDoctorList.push(obj);
									});
									$scope.savedWardList=[];
									angular.forEach(response.data.object.selectedWardList,function(value,index){
										var obj={};
										obj.id=value.wardId;
										obj.label=value.wardName;
										$scope.savedWardList.push(obj);
									});
									$scope.commonObj.rmoDoctorId= response.data.object.rmoDoctorId;
									$scope.rmoAllocIpObj.wardId= response.data.object.wardId;
									$scope.visitTypeId=	response.data.object.visitTypeId;
									if($scope.visitTypeId==1){
										$scope.isActiveForOp='Y';
									}else{
										$scope.isActiveForIp='Y';
									}
									$scope.selectedDoctorList=$scope.savedDoctorList;
									if($scope.visitTypeId==2)
									$scope.selectedIpWardList=$scope.savedWardList;
									if($scope.visitTypeId==3)
									$scope.selectedDayCareWardList=$scope.savedWardList;
									if($scope.visitTypeId==4)
									$scope.selectedErWardList=$scope.savedWardList;
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
						$scope.com.ipVisitUpdate=id;
					}
					console.log("ip vis",$scope.com.ipVisitUpdate);
					angular.element('#allocationTransferPopup').modal('show'); 
				}
				
				$scope.getVisitTypeForOp=function(id){
					if(id!=undefined && id!="" && id!=null){
						$scope.com.opvisitTypeIdForUpdate=id;
					}
					angular.element('#allocationTransferPopup').modal('show'); 
				}
				
				$scope.allocationTransfer=function(){
					$('#allocationTransferPopup').hide('hide');
					//$(".modal-backdrop").hide();
				}
				$scope.com={
						opvisitTypeIdForUpdate:""
				}
				$scope.com={
						ipVisitUpdate:""
				}
				$scope.stillOnSameAllocation=function(){
					$('#allocationTransferPopup').hide('hide');
					$scope.com.opvisitTypeIdForUpdate="";
					$scope.com.ipVisitUpdate="";
					//$scope.com.opvisitTypeIdForUpdate=$scope.visitTypeId.toString();
				
					
				}
				//For update 
				$scope.update = function() {
					try {
						
                            var date = new Date();
						     $scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
						     $rootScope.startLoader();
						     var data = {
										organizationId :$scope.organizationId,
										unitId :$scope.unitId,
										createdBy :$scope.userId,
										updatedBy :$scope.userId,
										createdDate :$scope.dt,
										updatedDate :$scope.dt,
										status :'A',
										//visitTypeId : $scope.visitTypeIdForUpdate!=0 ? $scope.visitTypeIdForUpdate:$scope.visitTypeId ,
										rmoDoctorId : $scope.commonObj.rmoDoctorId,
										rmoAllocationId :$scope.commonObj.rmoAllocationId
									};
						     
								if($scope.visitTypeId==1){
									data.visitTypeId= $scope.com.opvisitTypeIdForUpdate!='' && $scope.com.opvisitTypeIdForUpdate!=undefined ? $scope.com.opvisitTypeIdForUpdate : $scope.visitTypeId;
									data.specialityId=$scope.rmoAllocOpObj.specialityId;
									data.doctorList=$scope.selectedDoctorList;
									//data.doctorList=$scope.rmoAllocOpObj.doctorId;
								}else{
									data.visitTypeId= $scope.com.ipVisitUpdate!='' && $scope.com.ipVisitUpdate!=undefined ? $scope.com.ipVisitUpdate : $scope.visitTypeId;
									//data.wardId=$scope.rmoAllocIpObj.wardId;
									if($scope.visitTypeId==2){
										data.selectedWardList=$scope.selectedIpWardList;
									}else if($scope.visitTypeId==3){
										data.selectedWardList=$scope.selectedDayCareWardList;
									}else{
										data.selectedWardList=$scope.selectedErWardList;
									}
								}
							
						var URI = BASE_URL + ROOT_URL+ UPDATERMODOCTORALLOCATIONMASTER;
						console.log("Update-URI-", URI);
						console.log("Update OBJ",  data);
						GenericService.serviceAction("POST", URI,  data).then(function(response) {

							if (response.data.status == 'success') {
								if($scope.com.opvisitTypeIdForUpdate!=null && $scope.com.opvisitTypeIdForUpdate!="" ){
									var message="RMO is allocated to "+$scope.visitTypeList[$scope.com.opvisitTypeIdForUpdate-1].visitType+" and deallocated from "+ $scope.visitTypeList[$scope.visitTypeId-1].visitType;
									growl.success(message,
											{
										title : 'Success!'
											});	
								}else if($scope.com.ipVisitUpdate!=null && $scope.com.ipVisitUpdate!="" ){
									var message="RMO is allocated to "+$scope.visitTypeList[$scope.com.ipVisitUpdate-1].visitType+" and deallocated from "+$scope.visitTypeList[$scope.visitTypeId-1].visitType;
									growl.success(message,
											{
										title : 'Success!'
											});	
								}else{
								growl.success(response.data.message,
										{
									title : 'Success!'
										});
								}
								// $scope.$broadcast('show-errors-reset');
								$scope.initMaster();
								$scope.selectedDoctorList = [];
								$scope.selectedWardList = [];
								$scope.selectedIpWardList = [];
								$scope.selectedDayCareWardList = [];
								$scope.selectedErWardList = [];
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
									visitTypeId : $scope.visitTypeId,
									rmoAllocationId : id
								};
						var URI = BASE_URL + ROOT_URL+ UPDATERMODOCTORALLOCATIONMASTERSTATUS;
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
