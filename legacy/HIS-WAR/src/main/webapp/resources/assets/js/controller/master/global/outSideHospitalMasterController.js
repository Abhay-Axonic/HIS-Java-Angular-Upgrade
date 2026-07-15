(function () {
	"use strict";
        function outSideHospitalMasterController($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService){
			$rootScope.loginpage = true;
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			$scope.popUpFlag = true;
	
			/**Variables for pagination */
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
			
		//	$scope.unitId = cookieObject.unitId;
			$scope.organizationId = cookieObject.organizationId;
			$scope.organizationId = cookieObject.organizationId;
			$scope.unitId = cookieObject.unitId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
			
			$scope.initMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
						outsideHospitalId:"",
						outsideHospitalCode: "",
			            outsideHospitalDesc: "",
			           };
				console.log("Common-init",$scope.common);
			}
			$scope.initMaster();
			
			
			/**Crud Operations Start**/
		
			/**List And Pagination Start**/
			$scope.setNoOfRecords = function() {
				$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
			};
			
			$scope.initMasterList = function(organizationId,offset, noOfRecordsPerPage,unitId)
			{
				$rootScope.startLoader();
				
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
				var data1 = {
					          organizationId:organizationId,
							  offset:offset,
							  noOfRecordsPerPage:noOfRecordsPerPage,
							 // unitId:unitId
							};
				
				var data2 = {
						      organizationId:organizationId,
						      //unitId:unitId
							};
				
				var URI1 = BASE_URL + ROOT_URL + "/api/global/getOutsideHospitalMasterList"; 
//				var URI2 = BASE_URL + ROOT_URL + "/api/global/getOutsideHospitalMasterCount";
					
				
				GenericService.serviceAction("POST",URI1,data1).then(function(response){
					if(response.data.status == "success"){
					$rootScope.stopLoader();					
					$scope.commonList = response.data.listObject;
					$scope.commonListCount = response.data.object;
					$scope.setPage(1,false);
					console.log("commonList",$scope.commonList);
					}
				});
				
//				GenericService.serviceAction("POST",URI2,data2).then(function(response){
//					if(response.data.status == "success"){					
//						$rootScope.stopLoader();
//						$scope.commonListCount = response.data.object;
//						$scope.setPage(1,false);
//						console.log("commonListCount",response.data.object);
//					}
//				});
				
			}		
			
			$scope.masterList = function(organizationId,offset, noOfRecordsPerPage,unitId) {
				$rootScope.startLoader();
				console.log("offset", offset);
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
				
				var data = {
						organizationId:organizationId,
						offset:offset,
						noOfRecordsPerPage:noOfRecordsPerPage,
						//unitId:unitId
						};
				
				var URI = BASE_URL + ROOT_URL + "/api/global/getOutsideHospitalMasterList";
				console.log("URI", URI);
				GenericService.serviceAction("POST", URI,data).then(function(response) {
					$rootScope.stopLoader();
					$scope.commonList = response.data.listObject;
					console.log("commonList",$scope.commonList);
				});
			}
			
			

			$scope.pager = {};
			$scope.page;

			$scope.setPage = function(page, flag) {
				if (page < 1 || page > $scope.pager.totalPages)
				{
					return;
				}
				$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage);
				if (flag)
				{
					$scope.masterList($scope.organizationId ,$scope.pager.startIndex, $scope.pager.pageSize);
				}
			}

			$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage);
			
			/**List And Pagination End**/
			
			// function for popup ok button call
			$scope.PopupOkBtn = function() {
				$scope.popUpFlag = false;
				$scope.save();
			}
		
			$scope.save = function(){
				
				try{
					$scope.$broadcast('show-errors-check-validity');
					if($scope.popUpFlag==false)
						{
						  $rootScope.startLoader();
						}
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					var data={	
							
							outsideHospitalCode: $scope.common.outsideHospitalCode,
				            outsideHospitalDesc: $scope.common.outsideHospitalDesc,
							organizationId:$scope.organizationId,
							unitId:$scope.unitId,
							createdBy:$scope.createdBy,
							updatedBy:$scope.updatedBy,
							status:'A',
							createdDate:$scope.dt,
							updatedDate:$scope.dt
						
						}
					if ($scope.siteOfAdminform.$valid) {
					if ($scope.popUpFlag) {
						$scope.showModal = !$scope.showModal;

					} else {
					var URI = BASE_URL + ROOT_URL + "/api/global/saveOutsideHospitalMaster";
					console.log("Save-URI-",URI);
					console.log(" save OBJ",data); 
					
					GenericService.serviceAction("POST",URI,data).then(function(response){
						
						if (response.data.status == 'success')
						{
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							
							$scope.$broadcast('show-errors-reset');
							$scope.initMaster();
							//$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage);
						} else if(response.data.status == 'error')
						{
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
					}
					$rootScope.stopLoader();
				}
					
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			
			
			
			
			
			
			$scope.showDetailsBtn=function(data){
				$scope.configDetails=data;
			}
			
			$scope.showUpdateBtn = function(id){
				try{
					$rootScope.startLoader();
					$scope.saveBtnFlag = false;
					$scope.updateScope = true;
					$scope.updateBtnFlag = true;
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/api/global/getOutsideHospitalMasterById" +S+ id +S+ $scope.organizationId +S+ $scope.unitId;
					console.log("EDIT",URI);
					GenericService.serviceAction("GET",URI).then(function(response){
						console.log("Edit",response);
						
						if(response.data.status == "success"){
							$rootScope.stopLoader();
							$scope.common = response.data.listObject[0];
							$scope.common.outsideHospitalId=response.data.listObject[0].outsideHospitalId;
						}else if(response.data.status == 'error'){
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
					
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			
			$scope.update = function(){
				try{
				
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');	
					$rootScope.startLoader();
					$scope.$broadcast('show-errors-check-validity');	
					if ($scope.siteOfAdminform.$valid) {
					var data={	
							    outsideHospitalId:$scope.common.outsideHospitalId,
							    outsideHospitalCode: $scope.common.outsideHospitalCode,
				                outsideHospitalDesc: $scope.common.outsideHospitalDesc,
								organizationId:$scope.organizationId,
								unitId:$scope.unitId,
								updatedBy:$scope.updatedBy,
								updatedDate:$scope.dt
							
						}
								
					var URI = BASE_URL + ROOT_URL + "/api/global/updateOutsideHospitalMaster";
					console.log("Update-URI-",URI);
					console.log("Update OBJ",data);
					$rootScope.startLoader(); 
					GenericService.serviceAction("POST",URI,data).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.$broadcast('show-errors-reset');
							$scope.initMaster();
							$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage);
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
					}
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			$scope.updateStatus = function(id,status){
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					$scope.common.updatedBy=$scope.updatedBy,
					$scope.common.updatedDate=$scope.dt,
					$scope.common.status=status,
					$scope.common.outsideHospitalId=id,
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/api/global/updateOutsideHospitalMasterStatus";
					console.log("update status",$scope.common);
					//return false;
					GenericService.serviceAction("POST",URI,$scope.common).then(function(response){
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
//							$scope.$broadcast('show-errors-reset');
							$scope.initMaster();
							$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
				}catch(e){
					console.log("Exception",e);
				}
			}
			
			//doctor
			
			$scope.getOutsideHopId=function(id){
				$scope.hospitalId=id;
				$scope.getHospitalDoctorsList($scope.hospitalId);
			}
			
			$scope.obj = {
					outsideHosDocId:"",
					outsideHospitalCode: "",
					hospDoctorName: "",
		           };
			
			$scope.saveDoctorForOutsideHospital = function(){
				
				try{
					if($scope.obj.hospDoctorName == "" || $scope.obj.hospDoctorName == null || $scope.obj.hospDoctorName ==undefined){
						growl.error("Enter doctor name",
								{
									title : 'Error!'
								});
						return false;
					}
					
				  $rootScope.startLoader();
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					var data={	
							outsideHosDocId :$scope.obj.outsideHosDocId,
							outsideHospitalId: $scope.hospitalId,
							hospDoctorName: $scope.obj.hospDoctorName,
							organizationId:$scope.organizationId,
							unitId:$scope.unitId,
							createdBy:$scope.createdBy,
							updatedBy:$scope.updatedBy,
							status:'A',
							createdDate:$scope.dt,
							updatedDate:$scope.dt
						
						}
//					if ($scope.siteOfAdminform.$valid) {
//					if ($scope.popUpFlag) {
						//$scope.showModal = !$scope.showModal;

//					} else {
					var URI = BASE_URL + ROOT_URL + "/api/global/saveOutsideHospitalDoctorMapper";
					
					GenericService.serviceAction("POST",URI,data).then(function(response){
						if (response.data.status == 'success')
						{
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							
//							$scope.$broadcast('show-errors-reset');
							$scope.getHospitalDoctorsList($scope.hospitalId);
							$scope.obj={};
							$scope.initMaster();
							$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage);
						} else if(response.data.status == 'error')
						{
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
							$scope.obj={};
						}
					});
//					}
					$rootScope.stopLoader();
//				}
					
				}catch(e){
					console.log("Exception-",e);
				}
			}

			$scope.hospitalDoctorsList=[];
			$scope.getHospitalDoctorsList = function(id) {
				$rootScope.startLoader();
				var data = {
						organizationId:$scope.organizationId,
						unitId:$scope.unitId,
						outsideHospitalId:id,
						};
				
				var URI = BASE_URL + ROOT_URL + "/api/global/getOutsideHospitalDoctorMapperList";
				
				GenericService.serviceAction("POST", URI,data).then(function(response) {
					$scope.hospitalDoctorsList = response.data.listObject;
					console.log("doctList",$scope.hospitalDoctorsList);
					$rootScope.stopLoader();
					
				});
			}
			
			
			$scope.updateStatusDoctor = function(id,status){
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					/*$scope.obj.updatedBy=$scope.updatedBy,
					$scope.obj.updatedDate=$scope.dt,
					$scope.obj.status=status,
					$scope.obj.outsideHosDocId=id,*/
					var data={
							updatedBy:$scope.updatedBy,
							updatedDate:$scope.dt,
							status:status,
							outsideHosDocId:id
					}
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/api/global/updateOutsideHospitalDoctorMapperStatus";
					
					GenericService.serviceAction("POST",URI,data).then(function(response){
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
//							$scope.getHospitalDoctorsList();
							$scope.initMaster();
							$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
				}catch(e){
					console.log("Exception",e);
				}
			}
			
			
			$scope.showUpdateBtnDoctor = function(id){
				try{
					$rootScope.startLoader();
					$scope.saveBtnFlag = false;
					$scope.updateScope = true;
					$scope.updateBtnFlag = true;
					$rootScope.startLoader();
					var data={
							outsideHosDocId:id,
							organizationId:$scope.organizationId,
							unitId:$scope.unitId,
					}
					var URI = BASE_URL + ROOT_URL + "/api/global/getOutsideHospitalDoctorMapperById";
					console.log("EDIT",URI);
					GenericService.serviceAction("POST",URI, data).then(function(response){
						console.log("Edit",response);
						
						if(response.data.status == "success"){
							$rootScope.stopLoader();
							$scope.obj = response.data.listObject[0];
							$scope.obj.outsideHosDocId=response.data.listObject[0].outsideHosDocId;
						}else if(response.data.status == 'error'){
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
					
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			/**Crud Operations End**/
}
function config($stateProvider) {
	$stateProvider.state('outSideHospitalMaster', {
		url: '/outSideHospitalMaster',
		templateUrl: 'views/master/GlobalMaster/out-side-hospital-master.html',
		controller: "outSideHospitalMasterController",
		title: 'Out Side Hospital Master',
   	})	
}

angular
.module("myApp")
.config(config)
.controller("outSideHospitalMasterController", outSideHospitalMasterController);
})();