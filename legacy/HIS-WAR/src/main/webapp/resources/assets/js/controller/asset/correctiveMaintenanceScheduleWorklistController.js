angular
		.module('myApp')
		.controller(
				'correctiveMaintenanceScheduleWorklistController',
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
								GenericService,$sessionStorage,$cookies, growl, $localStorage,$stateParams ,PaginationService ) {
							
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							
							$scope.toDate=new Date();
							$scope.fromDate=new Date();
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
								        "storeId":$scope.storeId,
								        "assignTo":$scope.userId,
								        "isWorklist":3 //from scheduling screen
									};
								   var URI = BASE_URL+ROOT_URL+"/api/assetManagement/maintenance/getCorrectiveMaintenanceWorklistListByMultipleCriteria";
						            $rootScope.startLoader();
						            GenericService.serviceAction("POST", URI, data).then(
						                function (response) {
						                	$scope.corectiveMaintenanceWorklist = response.data.listObject;
						                    $rootScope.stopLoader();
						                },
						                function (err) {
						                    $rootScope.stopLoader();
						                });
							}
							
							$scope.getCorectiveMaintenanceWorklist();
							
							$scope.schedulingList = [];
							$scope.getSchedulingList = function(){
							$scope.schedulingList = [];
								var data = {
									 	"fromDate":moment($scope.fromDate).format('YYYY-MM-DD'),	
								        "toDate":moment($scope.toDate).format('YYYY-MM-DD'),
								        "maintenanceScheduleStatusId":$scope.statusId,
								        "requestCriticalityId":$scope.requestCriticalityId,
								        "unitId":$scope.unitId ,
								        "organizationId":$scope.organizationId,
								        "storeId":$scope.storeId,
								        "assignTo":$scope.userId,
								      //  "isWorklist":3 //from scheduling screen
									};
								   var URI = BASE_URL+ROOT_URL+"/api/assetManagement/maintenance/getCorrectiveMaintenanceScheduleByMultipleCrit";
						            $rootScope.startLoader();
						            GenericService.serviceAction("POST", URI, data).then(
						                function (response) {
						                	
						                	$scope.pagination.data = [];
						                	
						                	$scope.schedulingList = response.data.listObject;
						                	if($scope.schedulingList.length > 0){            
					                            PaginationService.searchData($scope.schedulingList , $scope.pagination);
					                          }
						                    $rootScope.stopLoader();
						                },
						                function (err) {
						                    $rootScope.stopLoader();
						                });
							}
							
							$scope.getSchedulingList();
							
							$scope.getCorrectiveMaintenanceDetails = function(obj){
								
								$state.go('detailsCorrectiveMaintenanceScheduleWorklist',
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
							$scope.getCurrentIndex = function(index ,label){
								$scope.currentIndex=index;
								if(label==2){
									if($scope.schedulingList[$scope.currentIndex].complete){
									$scope.completionObj.completionDate = $scope.schedulingList[$scope.currentIndex].completionDate;
									$scope.completionObj.completionTime = $scope.schedulingList[$scope.currentIndex].completionTime;
									$scope.completionObj.isComplete = $scope.schedulingList[$scope.currentIndex].complete;
									$scope.completionObj.completionRemak = $scope.schedulingList[$scope.currentIndex].completionRemak;
									
								}
								}else{
									if($scope.corectiveMaintenanceWorklist[$scope.currentIndex].complete){
										$scope.completionObj.completionDate = $scope.corectiveMaintenanceWorklist[$scope.currentIndex].completionDate;
										$scope.completionObj.completionTime = $scope.corectiveMaintenanceWorklist[$scope.currentIndex].completionTime;
										$scope.completionObj.isComplete = $scope.corectiveMaintenanceWorklist[$scope.currentIndex].complete;
										$scope.completionObj.completionRemak = $scope.corectiveMaintenanceWorklist[$scope.currentIndex].completionRemak;
										
									}
								}
							}
							
							$scope.setColor = function(referenceId) {
								 if(referenceId!=null ){
								        return {"color": "red"};
								    }
								}	
}]);
