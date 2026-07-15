(function () {
	"use strict";
						function correctiveMaintenanceReturnWorklistController($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,$stateParams,PaginationService) {
							
							var cookieObject = $cookies.getObject('cookieObject');
							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
							$scope.assetMaintenanceSpecialityId = cookieObjectAppConfig.assetMaintenanceSpecialityId;
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$rootScope.loginpage = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.detailsObj = $stateParams.detailsObj;
							
							console.log("$scope.detailsObj",$scope.detailsObj);
							
							$scope.newStoreId = 0;
							var cookieObjectStore = JSON.parse(sessionStorage.getItem('selectedTopStore'));
							if(cookieObjectStore!=undefined){
								$scope.newStoreId = cookieObjectStore.storeId;
							}
							$scope.toDate= moment().toDate();
							$scope.fromDate= moment().toDate();
							$scope.rejectReasonId = "";
							$scope.noteReject = "";
							$scope.dateOptions =
							{
								formatYear : 'yyyy',
								showWeeks : false,
								maxDate:new Date(),
								minDate : moment(new Date()).subtract(180, 'days').toDate()
							};

						    
						    $scope.dateOptions1 =
							{
								formatYear : 'yyyy',
								showWeeks : false,
								minDate:new Date()
							};
							
							$scope.openFromDatePicker = function() {
								$scope.fromDatepicker.opened = true;
							};

							$scope.fromDatepicker = {
									opened : false
							};
				
							$scope.openToDatePicker = function() {
								$scope.toDatepicker.opened = true;
							};

							$scope.toDatepicker = {
									opened : false
							};
						
							$rootScope.loginpage = true;
							$scope.saveBtnFlag = true;
							
							$scope.searchKeyword="";
							$scope.requestCriticalityId=0;
							$scope.statusId = 0;  //3 for assigned o for all
							
							$scope.storeId = 0;
							$scope.storeList = [];
							$scope.pagination = {
				                    currentPage: 1,
				                    totalPages: 0,
				                    pageSize: '10',
				                    search: '',
				                    searchData: PaginationService.searchData,
				                    setTotalPages: PaginationService.setTotalPages,
				                    getPagesList: PaginationService.getPagesList,
				                    setPage: PaginationService.setPage,
				                    setFirstPage: PaginationService.setFirstPage,
				                    setPreviousPage: PaginationService.setPreviousPage,
				                    setNextPage: PaginationService.setNextPage,
				                    setLastPage: PaginationService.setLastPage,
				                    sortBy: PaginationService.sortBy
				                  }
							
							$scope.getStoreList = function(){
								
								 var URI =  BASE_URL+ROOT_URL+"/api/global/getDetailedStoreList/"+$scope.userId+"/"+$scope.organizationId+"/"+$scope.unitId;
						            $rootScope.startLoader();
						            GenericService.serviceAction("GET", URI, {}).then(
						                function (response) {
						                    $scope.storeList = response.data.listObject;
						                    $rootScope.stopLoader();
											console.log("$scope.storeList",$scope.storeList);
						                });
							}
							
							$scope.getStoreList();
							
							var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
							var URI = BASE_URL + ROOT_URL+ "/api/common/getActiveReasonMasterList";
							GenericService.serviceAction(METHOD_POST, URI,commonObj,NOTIFICATION_MSG_STATUS_FALSE).then(
									function(response) {
										console.log(response);
										if (response.data.status == "success")
											$scope.reasonList = response.data.listObject;
									});
				
							$scope.getReplaceDetails = function(){
								$scope.replaceDetailsList = [];
								$rootScope.startLoader();
								var data = {
										correctiveMaintenanceScheduleId: $scope.detailsObj.correctiveMaintenanceScheduleId,
										unitId:$scope.unitId,
										organizationId:$scope.organizationId
								}
								var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/getreplaceItemById";
								GenericService.serviceAction("POST",URI,data).then(function(response){
									if (response.data.status == 'success'){
										$rootScope.stopLoader();
										$scope.replaceDetailsList = response.data.listObject;
										console.log("$scope.replaceDetailsList",$scope.replaceDetailsList);
										if($scope.replaceDetailsList.length>0){
											$scope.replaceBtn=true;
											$scope.returnBtn=false;
										}
										}
									});
							}
							
							
							$scope.corectiveMaintenanceReturnWorklist = [];
							$scope.getCorectiveMaintenanceReturnWorklist = function(){
							$scope.corectiveMaintenanceReturnWorklist = [];
								var data = {
									 	"fromDate":moment($scope.fromDate).format('YYYY-MM-DD'),	
								        "toDate":moment($scope.toDate).format('YYYY-MM-DD'),
								        "replaceStatusId":$scope.statusId,
								        "unitId":$scope.unitId ,
								        "organizationId":$scope.organizationId,
								        "storeId":$scope.storeId,
								        "createdBy":$scope.userId,
									};
								   var URI = BASE_URL+ROOT_URL+"/api/assetManagement/maintenance/getreplaceDetailOnReturnByMultipleCrit";
						            $rootScope.startLoader();
						            GenericService.serviceAction("POST", URI, data).then(
						                function (response) {
						                	
						                	angular.forEach(response.data.listObject,function(value1,index){
						                		if(value1.replaceStatusId==1)
						                			{
						                			value1.isAcceptOrReject = 'N';
						                			}
						                		if(value1.replaceStatusId==2)
						                			{
						                			value1.isAcceptOrReject = 'A';
						                			}
						                		if(value1.replaceStatusId==3)
						                			{
						                			value1.isAcceptOrReject = 'R';
						                			}
						                		$scope.corectiveMaintenanceReturnWorklist.push(value1);
											});
						                	
						                	$scope.pagination.data = [];
						                	if($scope.corectiveMaintenanceReturnWorklist.length > 0){            
					                            PaginationService.searchData($scope.corectiveMaintenanceReturnWorklist , $scope.pagination);
					                          }
						                	
						                    $rootScope.stopLoader();
						                    console.log("$scope.corectiveMaintenanceReturnWorklist ",$scope.corectiveMaintenanceReturnWorklist );
						                },
						                function (err) {
						                    $rootScope.stopLoader();
						                });
							}
							
							$scope.getCorectiveMaintenanceReturnWorklist();
							
							
							
							$scope.detailsOfCMObj = {};
							$scope.getdetailsOfCM = function(info){
								var data = {
									 	"correctiveMaintenanceScheduleId":info.correctiveMaintenanceSchedulingId,
								        "unitId":$scope.unitId ,
								        "organizationId":$scope.organizationId
									};
								   var URI = BASE_URL+ROOT_URL+"/api/assetManagement/maintenance/getdetailsOfCM";
						            $rootScope.startLoader();
						            GenericService.serviceAction("POST", URI, data).then(
						                function (response) {
						                	$scope.detailsOfCMObj = response.data.object;
						                	$scope.detailsOfCMObj.newSerialNo = info.newSerialNo;
								            $scope.detailsOfCMObj.requestedSerialNo = info.requestedSerialNo;
								            if($scope.detailsOfCMObj.isCompleted){
								            	$scope.detailsOfCMObj.underObservation = false;
								            }else{
								            	$scope.detailsOfCMObj.underObservation = true;
								            }
						                    $rootScope.stopLoader();
						                    $("#complaintInformationModal").modal("show");
						                    console.log("$scope.detailsOfCMObj ",$scope.detailsOfCMObj );
						                },
						                function (err) {
						                    $rootScope.stopLoader();
						                });
						            
							}
							
							$scope.acceptReturnObj = {};
							$scope.getAcceptDetils = function(info){
								
							 	$scope.acceptReturnObj.newSerialNo = info.newSerialNo;
					            $scope.acceptReturnObj.requestedSerialNo = info.requestedSerialNo;
								$scope.acceptReturnObj.itemName = info.itemName;
								$scope.acceptReturnObj.assetReplacementId = info.assetReplacementId;
								$scope.acceptReturnObj.itemCode = info.itemCode;
								$scope.acceptReturnObj.assetName = info.assetName;
							    $("#acceptBtnReturnModal").modal("show");
							}
							
							$scope.saveAcceptance = function(){
								  $rootScope.startLoader();
								var commonObj = {
										assetReplacementId : $scope.acceptReturnObj.assetReplacementId,
									};
								var URI = BASE_URL + ROOT_URL+ "/api/assetManagement/maintenance/returnAsset";
								GenericService.serviceAction(METHOD_POST, URI,commonObj,NOTIFICATION_MSG_STATUS_FALSE).then(
										function(response) {
											console.log(response);
											if (response.data.status == "success"){
												$scope.acceptReturnObj = {};
												 $rootScope.stopLoader();
													$scope.getCorectiveMaintenanceReturnWorklist();
													growl.success("success");
											}else{
												 $rootScope.stopLoader();
												growl.error("something went wrong ..!!",{
													title : "error"
												});
											}
												
										});
								
							}
							
							
							$scope.rejectReturnObj = {};
							$scope.rejectDetils = function(info){
								
								$scope.rejectReturnObj.assetReplacementId = info.assetReplacementId;
							    $("#accepatReject").modal("show");
							}
							
							$scope.currentInx = "";
							$scope.showRejecPopup = function(info,index){
								$scope.currentInx = index;
								if($scope.corectiveMaintenanceReturnWorklist[index].isAcceptOrReject == 'A'){
									$scope.getAcceptDetils(info);
								}else if($scope.corectiveMaintenanceReturnWorklist[index].isAcceptOrReject == 'R'){
									$scope.rejectDetils(info);
								}
							}
							
							$scope.saveAcceptReject = function(){
								
							
								if($scope.rejectReasonId == ""){
									
									growl.warning("Please select reason..!!",
											{
												title : 'warning!'
											});
									$scope.corectiveMaintenanceReturnWorklist[$scope.currentInx].isAcceptOrReject = 'N';
								} else if($scope.noteReject == "")
								{
									growl.warning("Please Enter reason note..!!",
											{
												title : 'warning!'
											});
									$scope.corectiveMaintenanceReturnWorklist[index].isAcceptOrReject = 'N';
								} else {
								
									$rootScope.startLoader();
									var commonObj = {
											assetReplacementId : $scope.rejectReturnObj.assetReplacementId,
											reasonId:$scope.rejectReasonId,
											reasonNote:$scope.noteReject
										};
									var URI = BASE_URL + ROOT_URL+ "/api/assetManagement/maintenance/rejectAsset";
									GenericService.serviceAction(METHOD_POST, URI,commonObj,NOTIFICATION_MSG_STATUS_FALSE).then(
											function(response) {
												console.log(response);
												if (response.data.status == "success"){
													$scope.rejectReturnObj = {};
													 $rootScope.stopLoader();
														$scope.getCorectiveMaintenanceReturnWorklist();
														growl.success("success"); 
												}else{
													 $rootScope.stopLoader();
													growl.error("something went wrong ..!!",{
														title : "error"
													});
												}
													
											});
								}
							}
							
							
}
function config($stateProvider) {
	$stateProvider.state('correctiveMaintenanceReturnWorklist', {
		url: '/correctiveMaintenanceReturnWorklist',
		templateUrl: 'views/master/UnitMaster/assetManagement/corrective-maintenance-return-worklist.html',
		controller: "correctiveMaintenanceReturnWorklistController",
		title: 'Corrective Maintenance Return Worklist',
	})	
}

angular
.module("myApp")
.config(config)
.controller("correctiveMaintenanceReturnWorklistController", correctiveMaintenanceReturnWorklistController);
})();
