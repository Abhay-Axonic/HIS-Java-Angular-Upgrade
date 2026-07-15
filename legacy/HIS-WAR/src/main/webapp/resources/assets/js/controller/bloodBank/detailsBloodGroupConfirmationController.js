angular
		.module('myApp')
		.controller(
				'detailsBloodGroupConfirmationController',
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
							$scope.searchKeyword="";
							
							$scope.bloodConfirmationObj = $stateParams.bloodConfirmationObj;
							
							$scope.detailsObj = {
									confirmationTechnicianId : $scope.userId,
									confirmationBloodGroupObj : "",
									bloodConfirmationStatus : 2,
								};
							
							console.log("$scope.bloodConfirmationObj",$scope.bloodConfirmationObj);
							
							//get Blood group list
							$scope.bloodGroupList = [];
							$scope.getBloodGroupList = function(){
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL+ "/global/bloodBank/getActiveBloodGroupMaster/"+$scope.organizationId +"/"+ $scope.unitId;;
									GenericService
									.serviceAction(METHOD_GET, URI )
									.then(
											function(response) {

												if (response.data.status == "success") {
													$rootScope.stopLoader();
													$scope.bloodGroupList = response.data.listObject;
													console.log("bloodGroupList ",response.data.listObject);
												}
											});
							}
							
							$scope.getBloodGroupList();
							
							
							$scope.usersList = [];
							$scope.getEmployeeList = function(){
								
								try{
									$rootScope.startLoader();
									
									var data = {
											organizationId:$scope.organizationId,
											unitId:$scope.unitId
									};
									var URI = BASE_URL + ROOT_URL
									+ GETUSERSLIST;
							GenericService.serviceAction("POST", URI, data)
									.then(
											function(response) {
												$scope.usersList = [];
												if (response.data.status == "success") {
													$scope.usersList = response.data.listObject;
													console.log("$scope.usersList",$scope.usersList);
												}
											});
								}catch(e){
									console.log("Exception-",e);
								}
							}
							$scope.getEmployeeList();
							
							$scope.redCellBloodObj = {};
							$scope.getRedBloodCellDetails = function(redCellId){
								try{
									$rootScope.startLoader();
									$scope.redCellBloodObj = {};
									var data = {
											redCellId:redCellId,
									};
									var URI = BASE_URL + ROOT_URL + "/api/bloodbank/redcell/getRedCellDetails";
							GenericService.serviceAction("POST", URI, data)
									.then(
											function(response) {
												if (response.data.status == "success") {
													$scope.redCellBloodObj = response.data.object;
												}
											});
								}catch(e){
									console.log("Exception-",e);
								}
							}
							if($scope.bloodConfirmationObj.redCellId!=null && $scope.bloodConfirmationObj.redCellId!=undefined){
								$scope.getRedBloodCellDetails($scope.bloodConfirmationObj.redCellId);
							}
							
							$scope.changeFinalStatus = function(){
								if($scope.redCellBloodObj.groupingBloodGroupId == $scope.redCellBloodObj.serumBloodGroupId ){
									if($scope.detailsObj.confirmationBloodGroupObj.bloodGroupId == $scope.redCellBloodObj.groupingBloodGroupId){
										$scope.detailsObj.bloodConfirmationStatus = 1;
									}else{
										$scope.detailsObj.bloodConfirmationStatus = 2;
									}
								}else{
									$scope.detailsObj.bloodConfirmationStatus = 2;
								}
							}
							
							$scope.saveBloodGroupConfirmation = function(){
								try{
									 $rootScope.startLoader();
									if($scope.detailsObj.groupingTechnicianId == ""){
										growl.warning("Please Select Technician",
												{
													title : 'warning!'
												});
										$rootScope.stopLoader();
										return false;
									}
									
									if($scope.detailsObj.confirmationBloodGroupObj == undefined ||$scope.detailsObj.confirmationBloodGroupObj == "" || 
											$scope.detailsObj.confirmationBloodGroupObj.bloodGroupId == undefined || $scope.detailsObj.confirmationBloodGroupObj.bloodGroupId== null 
											|| $scope.detailsObj.confirmationBloodGroupObj.bloodGroupId==""){
										growl.warning("Please Select Blood Group",
												{
													title : 'warning!'
												});
										$rootScope.stopLoader();
										return false;
									}
									
									if($scope.detailsObj.groupingIrregularAntibodies == ""){
										growl.warning("Please Enter Irregular Antibodies",
												{
													title : 'warning!'
												});
										$rootScope.stopLoader();
										return false;
									}
									  
									var data={
											redCellId:$scope.bloodConfirmationObj.redCellId,
											confirmationBloodGroupId:$scope.detailsObj.confirmationBloodGroupObj.bloodGroupId,
											confirmationTechnicianId:$scope.detailsObj.confirmationTechnicianId,
											bloodConfirmationStatus: $scope.detailsObj.bloodConfirmationStatus, //accepted  or rejected
									 		updatedBy:$scope.userId,
									 		updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									};
									
									var URI = BASE_URL + ROOT_URL + "/api/bloodbank/redcell/saveOrUpdateRedCell";

									GenericService.serviceAction(METHOD_POST, URI , data)
											.then(
												function(response) {
														if (response.data.status == "success") {
															growl.success(response.data.message,
																	{
																		title : 'Success!'
																	});
															$scope.detailsObj = {
																	confirmationTechnicianId : $scope.userId,
																	confirmationBloodGroupObj : "",
																	bloodConfirmationStatus : 2,
																};
															$state.go('redCellGroupingWorklist');
														} else if(response.data.status == 'error')
														{
															growl.error(response.data.message,
																	{
																		title : 'Error!'
																	});
														}
														$rootScope.stopLoader();
										});
									}catch(e){
										console.log("Exception-",e);
									}
							}
			}]);
							