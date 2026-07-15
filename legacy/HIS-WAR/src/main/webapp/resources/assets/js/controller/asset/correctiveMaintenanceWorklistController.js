angular
		.module('myApp')
		.controller(
				'correctiveMaintenanceWorklistController',
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
							
//							$scope.toDate=new Date();
//							$scope.fromDate=new Date();
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
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							
							$scope.searchKeyword="";
							$scope.storeList = [];
							$scope.statusId =0;
							$scope.requestCriticalityId = 0;
							
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
							
							
							$scope.corectiveMaintenanceList = [];
					    	$scope.finalList = [];
							$scope.getCorectiveMaintenance = function(){
								$scope.corectiveMaintenanceList = [];
								var data = {
									 	"fromDate":moment($scope.fromDate).format('YYYY-MM-DD'),	
								        "toDate":moment($scope.toDate).format('YYYY-MM-DD'),
								        "maintenanceStatusId":$scope.statusId,
								        "requestCriticalityId":$scope.requestCriticalityId!=""?$scope.requestCriticalityId:0,
								        "storeId":0,
								        "unitId":$scope.unitId ,
								        "organizationId":$scope.organizationId,
								        "isWorklist":1
									};
								   var URI = BASE_URL+ROOT_URL+"/api/assetManagement/maintenance/getCorrectiveMaintenanceListByMultipleCriteria";
						            $rootScope.startLoader();
						            GenericService.serviceAction("POST", URI, data).then(
						                function (response) {
						                	$scope.corectiveMaintenanceList = response.data.listObject;
						                	$scope.finalList = [];
						                	
						                	angular.forEach($scope.corectiveMaintenanceList,function(value,key){
						                		var newArr = {};
						                		newArr.assetCode = value.assetCode;
						                		newArr.assetName = value.assetName;
						                		newArr.assetcategory = value.assetcategory;
						                		newArr.batchId = value.batchId;
						                		newArr.callLogNumber = value.callLogNumber;
						                		newArr.completionDate = value.completionDate;
						                		newArr.completionRemak = value.completionRemak;
						                		newArr.completionTime = value.completionTime;
						                		newArr.correctiveMaintenanceId = value.correctiveMaintenanceId;
						                		newArr.createdBy = value.createdBy;
						                		newArr.createdDate = value.createdDate;
						                		newArr.downDateTime = value.downDateTime;
						                		newArr.downTime = value.downTime;
						                		newArr.editflag = value.editflag;
						                		newArr.exportedToWorklist = value.exportedToWorklist;
						                		newArr.fromDate = value.fromDate;
						                		newArr.insuranceList = value.insuranceList;
						                		newArr.isComplete = value.isComplete;
						                		newArr.isDown = value.isDown;
						                		newArr.isWorklist = value.isWorklist;
						                		newArr.itemId = value.itemId;
						                		newArr.maintenanceStatusId = value.maintenanceStatusId;
						                		newArr.manufacturerList = value.manufacturerList;
						                		newArr.organizationId = value.organizationId;
						                		newArr.quantity = value.quantity;
						                		newArr.reOpen = value.reOpen;
						                		newArr.referenceId = value.referenceId;
						                		newArr.referenceLogNumber = value.referenceLogNumber;
						                		newArr.remark = value.remark;
						                		newArr.requestCriticalityId = value.requestCriticalityId;
						                		newArr.requestDateTime = value.requestDateTime;
						                		newArr.requestDetails = value.requestDetails;
						                		newArr.requestTypeId = value.requestTypeId;
						                		newArr.resolutionExpectedDateTime = value.resolutionExpectedDateTime;
						                		newArr.resolutionExpectedTime = value.resolutionExpectedTime;
						                		newArr.serialNumber = value.serialNumber;
						                		newArr.status = value.status;
						                		newArr.storeId = value.storeId;
						                		newArr.toDate = value.toDate;
						                		newArr.unitId = value.unitId; 
						                		newArr.updatedBy = value.updatedBy; 	
						                		newArr.updatedDate = value.updatedDate; 	
						                		newArr.requestedByName = value.userName; 	
						                		newArr.scheduleDate = '-';
						                		newArr.scheduleTime = '-';
						                		newArr.userName = '-';
						                		newArr.vendorList = value.vendorList; 	
						                		newArr.maintenanceStatusId = value.maintenanceStatusId; 
						                		newArr.maintenanceWorklistStatusId = value.maintenanceStatusId; 
						                		$scope.finalList.push(newArr);
							                		
						                	});
						                	console.log("data",$scope.finalList);
						                    $rootScope.stopLoader();
						                },
						                function (err) {
						                    $rootScope.stopLoader();
						                });
							}
							
							$scope.getCorectiveMaintenance();
							
							$scope.corectiveMaintenanceWorklist = [];
							$scope.getCorectiveMaintenanceWorklist = function(){
							$scope.corectiveMaintenanceWorklist = [];
								var data = {
									 	"fromDate":moment($scope.fromDate).format('YYYY-MM-DD'),	
								        "toDate":moment($scope.toDate).format('YYYY-MM-DD'),
								        "maintenanceWorklistStatusId":$scope.statusId,
								        "requestCriticalityId":$scope.requestCriticalityId,
								        "unitId":$scope.unitId ,
								        "organizationId":$scope.organizationId,
								        "isWorklist":1
									};
								   var URI = BASE_URL+ROOT_URL+"/api/assetManagement/maintenance/getCorrectiveMaintenanceWorklistListByMultipleCriteria";
						            $rootScope.startLoader();
						            GenericService.serviceAction("POST", URI, data).then(
						                function (response) {
						                	$scope.corectiveMaintenanceWorklist = response.data.listObject;
						                	
						                	$scope.pagination.data=[];
						                	
						                	angular.forEach($scope.corectiveMaintenanceWorklist,function(value,key){ 
						                		$scope.finalList.push(value); 
						                	});
						                	
						                	 if($scope.finalList.length > 0){            
						                         PaginationService.searchData($scope.finalList, $scope.pagination);
						                       }
						                    $rootScope.stopLoader();
						                },
						                function (err) {
						                    $rootScope.stopLoader();
						                });
							}
							
							$scope.getCorectiveMaintenanceWorklist();
							
							$scope.getCorrectiveMaintenanceDetails = function(obj){
								
								$state.go('detailsCorrectiveMaintenanceWorklist',
										{
									detailsObj: obj
										});
							}
							$scope.completionObj = {
									completionDate : new Date(),
									completionTime : "",
									isComplete : false,
									completionRemak : "",
									updatedBy : $scope.userId,
									updatedDate  :moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									correctiveMaintenanceId : null
							};
							
							
							$scope.currentIndex="";
							$scope.getCurrentIndex = function(info,index ,label){
								$scope.currentIndex=index;
								if(label==1){
									if($scope.corectiveMaintenanceList[$scope.currentIndex].isComplete){
									$scope.completionObj.completionDate = $scope.corectiveMaintenanceList[$scope.currentIndex].completionDate;
									$scope.completionObj.completionTime = $scope.corectiveMaintenanceList[$scope.currentIndex].completionTime;
									$scope.completionObj.isComplete = $scope.corectiveMaintenanceList[$scope.currentIndex].isComplete;
									$scope.completionObj.completionRemak = $scope.corectiveMaintenanceList[$scope.currentIndex].completionRemak;
									
								}
								}else{
									$scope.corectiveMaintenanceWorklist=info
									if($scope.corectiveMaintenanceWorklist.complete){
										$scope.completionObj.completionDate = $scope.corectiveMaintenanceWorklist.completionDate ;
										$scope.completionObj.completionTime = $scope.corectiveMaintenanceWorklist.completionTime;
										$scope.completionObj.isComplete = $scope.corectiveMaintenanceWorklist.complete;
										$scope.completionObj.completionRemak = $scope.corectiveMaintenanceWorklist.completionRemak;
										
									}
								}
							}
							
							$scope.setColor = function(referenceId) {
								 if(referenceId!=null ){
								        return {"color": "red"};
								    }
								}	
}]);
