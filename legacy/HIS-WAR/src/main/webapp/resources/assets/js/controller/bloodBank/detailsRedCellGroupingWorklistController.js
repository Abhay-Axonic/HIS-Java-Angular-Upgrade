angular
		.module('myApp')
		.controller(
				'detailsRedCellGroupingWorklistController',
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
							
							$scope.redCellGroupObj = $stateParams.redCellGroupObj;
							
							$scope.detailsObj = {
									groupingTechnicianId : $scope.userId,
									groupingBloodGroupObj : "",
									groupingIrregularAntibodies:"",
									checkedCellGrouping : false,
									resultA : 'N' ,
									resultB : 'N',
									resultAB : 'N',
									resultD : 'N',
									resultA1 : 'N',
									resultH : 'N'
								};
							
							console.log("$scope.redCellGroupObj",$scope.redCellGroupObj);
							
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
							
							$scope.saveRedCellGrouping = function(){
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
									
									if($scope.detailsObj.groupingBloodGroupObj == undefined || $scope.detailsObj.groupingBloodGroupObj == "" || $scope.detailsObj.groupingBloodGroupObj.bloodGroupId == undefined || $scope.detailsObj.groupingBloodGroupObj.bloodGroupId== null 
											|| $scope.detailsObj.groupingBloodGroupObj.bloodGroupId==""){
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
											redCellId:$scope.redCellGroupObj.redCellId != null ? $scope.redCellGroupObj.redCellId: null,
											groupingBloodGroupId:$scope.detailsObj.groupingBloodGroupObj.bloodGroupId,
											groupingTechnicianId:$scope.detailsObj.groupingTechnicianId,
											groupingIrregularAntibodies:$scope.detailsObj.groupingIrregularAntibodies,
											checkedCellGrouping:$scope.detailsObj.checkedCellGrouping, //boolean
											resultA : $scope.detailsObj.resultA, // char
											resultB : $scope.detailsObj.resultB,
											resultAB : $scope.detailsObj.resultAB,
											resultD : $scope.detailsObj.resultD,
											resultA1 :$scope.detailsObj.resultA1,
											resultH : $scope.detailsObj.resultH,
											groupingStatus:'Y',
											createdBy:$scope.userId,
									 		createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									 		status:"A",
									 		updatedBy:$scope.userId,
									 		updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									 		organizationId:$scope.organizationId,
									 		unitId:$scope.unitId,
									 		transfusionTransmittedDiseasesId:$scope.redCellGroupObj.transfusionTransmittedDiseasesId
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
																	groupingTechnicianId : $scope.userId,
																	bloodGroupObj : "",
																	irregularAntibodies:"",
																	checkedCellGrouping : false,
																	resultA : 'N' ,
																	resultB : 'N',
																	resultAB : 'N',
																	resultD : 'N',
																	resultA1 : 'N',
																	resultH : 'N'
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
							