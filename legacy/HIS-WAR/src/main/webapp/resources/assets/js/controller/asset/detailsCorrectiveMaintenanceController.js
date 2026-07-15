angular
		.module('myApp')
		.controller(
				'detailsCorrectiveMaintenanceController',
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
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,$stateParams) {
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							
							$rootScope.loginpage = true;
							$scope.saveBtnFlag = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.detailsObj = $stateParams.detailsObj;
							$scope.serialNoObj = {
									batchId:"",
									serialNumber:""
							};
							$scope.serialNoObj.serialNumber = $stateParams.detailsObj.serialNumber;
							$scope.serialNoObj.batchId = $stateParams.detailsObj.batchId;
							
							$scope.editflag = true;
							if($scope.detailsObj.editflag == 0 ){
								$scope.editflag = false;
							}
							
							$scope.detailsObj.downDateTime = moment($scope.detailsObj.downDateTime ,"DD-MM-YYYY").toDate();
							$scope.downDateTime = moment($scope.detailsObj.downDateTime ,"DD-MM-YYYY").toDate();
							$scope.downTime = $stateParams.detailsObj.downTime;
							$scope.detailsObj.resolutionExpectedDateTime = moment($scope.detailsObj.resolutionExpectedDateTime ,"DD-MM-YYYY").toDate();
							
							$scope.openFromDatePicker = function() {
								$scope.fromDatepicker.opened = true;
							};

							$scope.fromDatepicker = {
									opened : false
							};
							
							 $scope.fromdateOptions ={
										formatYear : 'yyyy',
										showWeeks : false,
										minDate : moment(new Date()).subtract(3, 'days').toDate(),
							 };
								 
							$scope.openToDatePicker = function() {
								$scope.toDatepicker.opened = true;
							};

							$scope.toDatepicker = {
									opened : false
							};

							 $scope.todateOptions ={
										formatYear : 'yyyy',
										showWeeks : false,
										minDate : new Date(),
							 };
							
							$scope.initClockPicker = function() {

									$('.clockpicker').clockpicker();
							}
								
							$scope.getSerialNumbers = function(){
								   var URI = BASE_URL+ROOT_URL+"/api/assetManagement/maintenance/getSerialNoByItemId/"+$scope.detailsObj.itemId+"/"+$scope.unitId+"/"+$scope.organizationId;
						            $rootScope.startLoader();
						            GenericService.serviceAction("GET", URI, {}).then(
						                function (response) {
						                	$scope.serialNumberList = response.data.listObject;
						                    $rootScope.stopLoader();
						                },
						                function (err) {
						                    $rootScope.stopLoader();
						                });
							}
							
							$scope.getSerialNumbers(); 
							
							$scope.changedowndate = function(){
								
								if($scope.detailsObj.isDown == false){
									
									$scope.detailsObj.downDateTime ="";
									$scope.detailsObj.downTime = "";
									
								}else{
									
									$scope.detailsObj.downDateTime = $scope.downDateTime ;
									$scope.detailsObj.downTime = $scope.downTime;
									
								}
							}
								
							$scope.saveCorectiveMaintenance=function(){
								$scope.detailsObj.serialNumber = $scope.serialNoObj.serialNumber;
								$scope.detailsObj.batchId =  $scope.serialNoObj.batchId;
								
								if($scope.detailsObj.downDateTime != undefined || $scope.detailsObj.downDateTime != null || $scope.detailsObj.downDateTime != ""){
									$scope.detailsObj.downDateTime = moment($scope.detailsObj.downDateTime).format('DD-MM-YYYY HH:mm:ss');
								}
								$scope.detailsObj.resolutionExpectedDateTime = moment($scope.detailsObj.resolutionExpectedDateTime).format('DD-MM-YYYY HH:mm:ss');
								$scope.detailsObj.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
								$scope.detailsObj.updatedBy = $scope.userId;
										$rootScope.startLoader();
										var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/saveCorrectiveMaintenance";
									
										GenericService.serviceAction("POST",URI,$scope.detailsObj).then(function(response){
											
											if (response.data.status == 'success')
											{
												$state.go('correctiveMaintenance');
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
										});
							}
							
							$scope.manufactureDataList = [];
							$scope.vendorDataList = [];
							$scope.itemDataList = [];
							$scope.insuranceDataList = [];
							$scope.getPMSDetails = function(){
								if( $scope.serialNoObj.batchId == null ||  $scope.serialNoObj.batchId == undefined   || $scope.serialNoObj.batchId == ""){
									growl.warning("Please select Serial Number",
											{
												title : 'Warning!'
											});
									return false;
								}
								   var data = {
										   itemId : $scope.detailsObj.itemId,
										   unitId:	$scope.unitId,
										   organizationId:	$scope.organizationId,
										   batchId : $scope.serialNoObj.batchId 
								   };
								var URI = BASE_URL+ROOT_URL+"/api/assetManagement/maintenance/getPMSDetailsByItemId";
								$scope.manufactureDataList = [];
								$scope.vendorDataList = [];
								$scope.itemDataList = [];
								
								$scope.insuranceDataList = [];
						        $rootScope.startLoader();
						            GenericService.serviceAction("POST", URI, data).then(
						                function (response) {
						                	$scope.manufactureDataList = response.data.object.manufacturerList;
						                	$scope.vendorDataList = response.data.object.vendorList;
						                	$scope.insuranceDataList = response.data.object.insuranceList;
						                	if(response.data.object.accessoryList.length>0){
						                		angular.forEach(response.data.object.accessoryList,function(value,index){
						                			var newArr = {};
						                			newArr.itemCode = value.assetCode;
						                			newArr.itemName = value.assetName;
						                			newArr.quantity = value.quantity;
						                			newArr.serialNumber = value.serialNumber;
						                			$scope.itemDataList.push(newArr);
						                		});
						                	}
						                    $rootScope.stopLoader();
						                    angular.element('#PMSDetailsModal').modal('show');
						                },
						                function (err) {
						                    $rootScope.stopLoader();
						                });
							}
							//$scope.getPMSDetails();
}]);
