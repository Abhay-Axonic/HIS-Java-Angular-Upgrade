angular
		.module('myApp')
		.controller(
				'correctiveMaintenanceController',
				[
						'$scope',
						'$rootScope',
						'$state',	
						'$http',
						'GenericService',
						'$sessionStorage',
						'$cookies',
						'growl',
						'$localStorage',
						'$stateParams',
						'PaginationService',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,$stateParams,PaginationService) {
							
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
//							$scope.toDate = new Date();
//							$scope.fromDate = new Date();
							$scope.toDate= moment().toDate();
							$scope.fromDate= moment().toDate();
							$scope.dateOptions =
							{
								formatYear : 'yyyy',
								showWeeks : false,
								maxDate:new Date(),
								minDate : moment(new Date()).subtract(180, 'days').toDate()
							};

						    
						    $scope.toDateOptions =
							{
								formatYear : 'yyyy',
								showWeeks : false,
								minDate:new Date()
							};
							
							
							$scope.reopenDate= moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
							$scope.reopenRemark = "";
							
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
							
							$scope.completionDate = {
									opened : false
							};
						
							$scope.openCompletionDatePicker = function() {
								$scope.completionDate.opened = true;
							};
							
							
						/*	$scope.openReopenDatePicker = function() {
								$scope.reopenDatepicker.opened = true;
							};

							$scope.reopenDatepicker = {
									opened : false
							};
*/
							
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
							 
							$scope.initClockPicker = function() {

								$('.clockpicker').clockpicker();
							} 
							
							$rootScope.loginpage = true;
							$scope.saveBtnFlag = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							
							$scope.completionObj = {
									completionDate : new Date(),
									completionTime : "",
									isComplete : false,
									completionRemak : "",
									updatedBy : $scope.userId,
									updatedDate  :moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									correctiveMaintenanceId : null
							};
							
							$scope.storeList = [];
							$scope.storeId = 0;
							$scope.statusId =0;
							$scope.getStoreList = function(){
								
								 var URI =  BASE_URL+ROOT_URL+"/api/global/getDetailedStoreList/"+$scope.userId+"/"+$scope.organizationId+"/"+$scope.unitId;
								 //http://localhost:8081/HIS-WAR/api/global/getDetailedStoreList/73/1/1
						            $rootScope.startLoader();
						            GenericService.serviceAction("GET", URI, {}).then(
						                function (response) {
						                    $scope.storeList = response.data.listObject;
						                    $rootScope.stopLoader();
											console.log("$scope.storeList",$scope.storeList);
						                });
							}
							
							$scope.getStoreList();
							
							$scope.corectiveMaintenanceList = [];
							$scope.getCorectiveMaintenance = function(){
								$scope.corectiveMaintenanceList = [];
								var data = {
									 	"fromDate":moment($scope.fromDate).format('YYYY-MM-DD'),	
								        "toDate":moment($scope.toDate).format('YYYY-MM-DD'),
								        "maintenanceStatusId":$scope.statusId,
								        "storeId": $scope.storeId,
								        "unitId":$scope.unitId ,
								        "organizationId":$scope.organizationId,
								        "createdBy" : $scope.userId,
								        "isWorklist":0
									};
								   var URI = BASE_URL+ROOT_URL+"/api/assetManagement/maintenance/getCorrectiveMaintenanceListByMultipleCriteria";
						            $rootScope.startLoader();
						            GenericService.serviceAction("POST", URI, data).then(
						                function (response) {
						                	$scope.corectiveMaintenanceList = response.data.listObject;
						                    $rootScope.stopLoader();
						                    $scope.completionObj = {
													completionDate : new Date(),
													completionTime : "",
													isComplete : false,
													completionRemak : "",
													updatedBy : $scope.userId,
													updatedDate  :moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
													correctiveMaintenanceId : null
											};
						                	angular.forEach($scope.corectiveMaintenanceList,function(value, index){
												
						                		value.check = false;
												
											})
											
											$scope.pagination.data = [];
						                	if($scope.corectiveMaintenanceList.length > 0){            
					                            PaginationService.searchData($scope.corectiveMaintenanceList , $scope.pagination);
					                          }
											
						                },
						                function (err) {
						                    $rootScope.stopLoader();
						                });
								
								
							}
							
							$scope.getCorectiveMaintenance();
							
							$scope.getDetails = function(obj,editflag){
								obj.editflag = editflag;
								$state.go('detailsCorrectiveMaintenance',
										{
									detailsObj: obj
										});
							}
						
							
							$scope.updateCorectiveMaintenance=function(){
								
								$scope.finalSaveList = [];
								angular.forEach($scope.corectiveMaintenanceList,function(value, index){
									
			                		if(value.check == true){
			                			
			                			var newdata ={}
			                			newdata.correctiveMaintenanceId = value.correctiveMaintenanceId;
			                			newdata.maintenanceStatusId =1;
			                			newdata.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
			                			newdata.updatedBy = $scope.userId;
			                			$scope.finalSaveList.push(newdata);
			                		}
									
								})
								
								if($scope.finalSaveList.length >0){
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/updateCorrectiveMaintenancePendingToNewRequest";
								
									GenericService.serviceAction("POST",URI,$scope.finalSaveList).then(function(response){
										
										if (response.data.status == 'success')
										{
											$state.go('correctiveMaintenance');
											$rootScope.stopLoader();
											$scope.getCorectiveMaintenance();
											growl.success(response.data.message,
													{
														title : 'Success!'
													});
										} else if(response.data.status == 'error')
										{
											$rootScope.stopLoader();
											growl.error(response.data.message,
													{
														title : 'Error!'
													});
										}
									}); 
								}else{
									
									growl.warning("Please Select atleast one request",
											{
												title : 'warning'
											});
								}
									
										
							}
							
							$scope.correctiveMaintenanceIdindex = "";
							$scope.cancelRequest = function(correctiveMaintenanceId){
								
								$scope.correctiveMaintenanceIdindex = correctiveMaintenanceId;
								
								$("#cancleCorrectiveMainteanence").modal("show");
								
							}
							
							$scope.finalCancelReq = function(){
								
								$rootScope.startLoader();
								var data = {
										correctiveMaintenanceId: $scope.correctiveMaintenanceIdindex,
										maintenanceStatusId : 6,
	                			        updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
	                		         	updatedBy : $scope.userId
								}
								var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/cancelCorrectiveMaintenanceRequest";
							
								GenericService.serviceAction("POST",URI,data).then(function(response){
									
									if (response.data.status == 'success')
									{
										$rootScope.stopLoader();
										$scope.getCorectiveMaintenance();
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
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
							
							$scope.currentIndex="";
							$scope.getCurrentIndex = function(index){
								$scope.completionObj = {
										completionDate : new Date(),
										completionTime : "",
										isComplete : false,
										completionRemak : "",
										updatedBy : $scope.userId,
										updatedDate  :moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										correctiveMaintenanceId : null
								};
								$scope.currentIndex=index;
								if($scope.corectiveMaintenanceList[$scope.currentIndex].isComplete){
									$scope.completionObj.completionDate = moment($scope.corectiveMaintenanceList[$scope.currentIndex].completionDate ,"DD-MM-YYYY").toDate(); ;
									$scope.completionObj.completionTime = $scope.corectiveMaintenanceList[$scope.currentIndex].completionTime;
									$scope.completionObj.isComplete = $scope.corectiveMaintenanceList[$scope.currentIndex].isComplete;
									$scope.completionObj.completionRemak = $scope.corectiveMaintenanceList[$scope.currentIndex].completionRemak;
									
								}
								
							}
							$scope.updateCompletionDetails = function(){
								$rootScope.startLoader();
								var finalCompletionObj = angular.copy($scope.completionObj);
								
								finalCompletionObj.correctiveMaintenanceId = $scope.corectiveMaintenanceList[$scope.currentIndex].correctiveMaintenanceId;
								finalCompletionObj.completionDate = moment($scope.completionObj.completionDate).format('DD-MM-YYYY HH:mm:ss');
								if(finalCompletionObj.completionDate == undefined){
									$rootScope.stopLoader();
									growl.warning("Please Select Completion Date",
											{
												title : 'warning'
											});
								}else if(finalCompletionObj.completionTime == ""){
									$rootScope.stopLoader();
									growl.warning("Please Select Completion Time",
											{
												title : 'warning'
											});
								}else if(finalCompletionObj.isComplete == ""){
									$rootScope.stopLoader();
									growl.warning("Please Select Is Complete",
											{
												title : 'warning'
											});
								}else{
									
								var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/CorrectiveMaintenanceRequestCompleteByRequester";
							
								GenericService.serviceAction("POST",URI,finalCompletionObj).then(function(response){
									
									if (response.data.status == 'success')
									{
										$rootScope.stopLoader();
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
										$scope.getCorectiveMaintenance();
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
							}
							
							$scope.reopenIndex="";
							$scope.reOpenstatus = false;
							$scope.getReopenReqIndex = function(index){
								$scope.reopenIndex = index; 	
								$scope.reOpenstatus = $scope.corectiveMaintenanceList[$scope.reopenIndex].reOpen;
								$scope.reopenRemark = $scope.corectiveMaintenanceList[$scope.reopenIndex].remark;
							}
							
							$scope.reopenRequest = function(){
								var down = moment($scope.corectiveMaintenanceList[$scope.reopenIndex].downDateTime,"DD-MM-YYYY").toDate();
								var resolutionDate = moment($scope.corectiveMaintenanceList[$scope.reopenIndex].resolutionExpectedDateTime,"DD-MM-YYYY").toDate();
								
								var data = {
										itemId : $scope.corectiveMaintenanceList[$scope.reopenIndex].itemId,
										storeId : $scope.corectiveMaintenanceList[$scope.reopenIndex].storeId,
										requestTypeId : $scope.corectiveMaintenanceList[$scope.reopenIndex].requestTypeId,
										requestCriticalityId : $scope.corectiveMaintenanceList[$scope.reopenIndex].requestCriticalityId,
										serialNumber : $scope.corectiveMaintenanceList[$scope.reopenIndex].serialNumber,
										isDown : $scope.corectiveMaintenanceList[$scope.reopenIndex].isDown,
										requestDateTime : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										downDateTime : moment(down).format('DD-MM-YYYY HH:mm:ss'),
										resolutionExpectedDateTime : moment(resolutionDate).format('DD-MM-YYYY HH:mm:ss'),
										requestDetails : $scope.corectiveMaintenanceList[$scope.reopenIndex].requestDetails,
										maintenanceStatusId : 5, //for pending = 5 , reopen=7
										remark : $scope.reopenRemark,
										createdBy  :$scope.userId,
										createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										status : "A",
										updatedBy : $scope.userId,
										updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										unitId : $scope.unitId,
										organizationId : $scope.organizationId ,
										referenceId : $scope.corectiveMaintenanceList[$scope.reopenIndex].correctiveMaintenanceId,
										batchId : $scope.corectiveMaintenanceList[$scope.reopenIndex].batchId,
										referenceLogNumber : $scope.corectiveMaintenanceList[$scope.reopenIndex].callLogNumber,
										downTime : $scope.corectiveMaintenanceList[$scope.reopenIndex].downTime,
										resolutionExpectedTime : $scope.corectiveMaintenanceList[$scope.reopenIndex].resolutionExpectedTime
								};
								
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/saveCorrectiveMaintenance";
							
								GenericService.serviceAction("POST",URI,data).then(function(response){
									
									if (response.data.status == 'success')
									{
										$scope.getCorectiveMaintenance();
										$scope.reopenRemark = "";
										$rootScope.stopLoader();
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
									} else if(response.data.status == 'error')
									{
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
									}
									$rootScope.stopLoader();
								});
							}
							$scope.counter = 0;		
							
							$scope.getcountofcheck = function(check){
								
								if(check){
									$scope.counter += 1;
								}else{
									$scope.counter -= 1;
								}
							}
							
							
							$scope.setColor = function(referenceId) {
							 if(referenceId!=null ){
							        return {"color": "red"};
							    }
							}	
}]);
