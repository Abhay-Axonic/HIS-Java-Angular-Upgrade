angular
		.module('myApp')
		.controller(
				'detailsCorrectiveMaintenanceWorklistController',
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
					'$timeout',
					function($scope, $rootScope, $state, $http,
							GenericService,$sessionStorage,$cookies, growl, $localStorage,$stateParams,$timeout) {
							
							
							var cookieObject = $cookies.getObject('cookieObject');
							
							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
							$scope.assetMaintenanceSpecialityId = cookieObjectAppConfig.assetMaintenanceSpecialityId;
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							
							
							$rootScope.loginpage = true;
							$scope.saveBtnFlag = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							
							$scope.openFromDatePicker = function() {
								$scope.fromDatepicker.opened = true;
							};

							$scope.fromDatepicker = {
									opened : false
							};
							
							 $scope.fromdateOptions ={
										formatYear : 'yyyy',
										showWeeks : false,
										minDate :new Date(),
							 };
							 
							// function for initializa clockpicker
								$scope.initClockPicker = function() {

									$('.clockpicker').clockpicker();
								}
							 $scope.init = function(){
									$scope.isDisabled=false;
									$scope.detailsObj = $stateParams.detailsObj;
									if($scope.detailsObj.maintenanceWorklistStatusId==4 || $scope.detailsObj.maintenanceWorklistStatusId==2){
										$scope.isDisabled=true;
									}
									if($scope.detailsObj.correctiveMaintenanceWorklistId != null){
										$scope.scheduleTime =$scope.detailsObj.scheduleTime;
										$scope.scheduleDate = moment($scope.detailsObj.scheduleDate,"DD-MM-YYYY").toDate();
										$scope.assignTo = $scope.detailsObj.assignTo;
										$scope.allocationRemark = $scope.detailsObj.allocationRemark;
										
									}else{
										var d = new Date();
										var minutes = 0;
										var hours =0;
										
										if(d.getHours()<9){
											hours = "0"+d.getHours();
										}else{
											hours = d.getHours();
										}
										
										if(d.getMinutes()<9){
											minutes = "0"+d.getMinutes();
										}else{
											minutes = d.getMinutes();
										}

										$scope.scheduleTime = hours+":"+minutes;
										$scope.scheduleDate =  moment(d,"DD-MM-YYYY").toDate();
										$scope.assignTo = "";
										$scope.allocationRemark = "";
									}
									console.log("$scope.detailsObj",$scope.detailsObj);
							 }
							 $scope.init();
								$scope.usersList = [];
								$scope.getEmployeeList = function(){
									
									try{
										$rootScope.startLoader();
										
										var data = {
												organizationId:$scope.organizationId,
												unitId:$scope.unitId,
												specialityId : $scope.assetMaintenanceSpecialityId 
										};
										var URI = BASE_URL + ROOT_URL
										+ "/api/global/getEmployeeListBySpecialityAndSubSpeciality";
								GenericService.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.usersList = [];
													if (response.data.status == "success") {
														$rootScope.stopLoader();
														$scope.usersList = response.data.listObject;
													}
												});
									}catch(e){
										$rootScope.stopLoader();
										console.log("Exception-",e);
									}
								}
								
								$scope.getEmployeeList();
							
							$scope.saveDetailsWorklist = function(){
								$scope.saveObj = {
										correctiveMaintenanceWorklistId : $scope.detailsObj.correctiveMaintenanceWorklistId == undefined?null: $scope.detailsObj.correctiveMaintenanceWorklistId,
										correctiveMaintenanceId : $scope.detailsObj.correctiveMaintenanceId,
										scheduleDate : moment($scope.scheduleDate).format('DD-MM-YYYY HH:mm:ss') ,
										scheduleTime : $scope.scheduleTime ,
										maintenanceWorklistStatusId : 3, //assigned
										assignTo: $scope.assignTo,
										allocationRemark: $scope.allocationRemark,
										createdBy:$scope.userId,
										createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										status:"A",
										updatedBy:$scope.userId,
										updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										unitId:$scope.unitId,
										organizationId:$scope.organizationId
								}
								
								if($scope.saveObj.assignTo == ""){
									
									growl.warning("Please Select Assign To",
											{
												title : 'warning!'
											});
									
								}else if($scope.saveObj.scheduleDate == "" || $scope.saveObj.scheduleDate == undefined){
									
									growl.warning("Please select scheduleDate",
											{
												title : 'warning!'
											});
									
								}else if($scope.saveObj.scheduleTime == ""){
									
									growl.warning("Please select scheduleTime",
											{
												title : 'warning!'
											});
									
								}else{
									$rootScope.startLoader();
									
									console.log("$scope.saveObj",$scope.saveObj);
									var URI = BASE_URL + ROOT_URL + "/api/assetManagement/maintenance/saveCorrectiveMaintenance/worklist";
									
									GenericService.serviceAction("POST",URI,$scope.saveObj).then(function(response){
											
										if (response.data.status == 'success'){
												
											$rootScope.stopLoader();
											$scope.init();
											growl.success(response.data.message,
													{
															title : 'Success!'
													});
											 $timeout(function () { 
												 $state.go('correctiveMaintenanceWorklist');
											 }, 3000);
											
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
}]);
