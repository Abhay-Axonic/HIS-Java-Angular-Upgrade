angular
		.module('myApp')
		.controller(
				'detailsRedCellSerumWorklistController',
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
							$scope.redCellSerumObj = $stateParams.redCellSerumObj;
							
							$scope.detailsObj = {
									serumTechnicianId :$scope.userId,
									serumBloodGroupObj : "",
									serumIrregularAntibodies:"",
									checkedSerumGrouping : false,
									resultACell : 'N' ,
									resultBCell : 'N',
									resultOCell : 'N',
									resultOBCell : 'N',
								};
							
							console.log("$scope.redCellSerumObj",$scope.redCellSerumObj);
							
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
							
							$scope.saveRedCellSerum = function(){
								try{
									 $rootScope.startLoader();
									if($scope.detailsObj.serumTechnicianId == ""){
										growl.warning("Please Select Technician",
												{
													title : 'warning!'
												});
										$rootScope.stopLoader();
										return false;
									}
									
									if($scope.detailsObj.serumBloodGroupObj == undefined || $scope.detailsObj.serumBloodGroupObj == "" || $scope.detailsObj.serumBloodGroupObj.bloodGroupId == undefined || $scope.detailsObj.serumBloodGroupObj.bloodGroupId== null 
											|| $scope.detailsObj.serumBloodGroupObj.bloodGroupId==""){
										growl.warning("Please Select Blood Group",
												{
													title : 'warning!'
												});
										$rootScope.stopLoader();
										return false;
									}
									
									if($scope.detailsObj.serumIrregularAntibodies == ""){
										growl.warning("Please Enter Irregular Antibodies",
												{
													title : 'warning!'
												});
										$rootScope.stopLoader();
										return false;
									}
									  
									var data={
											redCellId:$scope.redCellSerumObj.redCellId != null ? $scope.redCellSerumObj.redCellId: null,
											serumBloodGroupId:$scope.detailsObj.serumBloodGroupObj.bloodGroupId,
											serumTechnicianId:$scope.detailsObj.serumTechnicianId,
											serumIrregularAntibodies:$scope.detailsObj.serumIrregularAntibodies,
											checkedSerumGrouping:$scope.detailsObj.checkedSerumGrouping, //boolean
											resultACell : $scope.detailsObj.resultACell, // char
											resultBCell : $scope.detailsObj.resultBCell,
											resultOCell : $scope.detailsObj.resultOCell,
											resultOBCell : $scope.detailsObj.resultOBCell,
											serumStatus:'Y',
											createdBy:$scope.userId,
									 		createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									 		status:"A",
									 		updatedBy:$scope.userId,
									 		updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									 		organizationId:$scope.organizationId,
									 		unitId:$scope.unitId,
									 		transfusionTransmittedDiseasesId:$scope.redCellSerumObj.transfusionTransmittedDiseasesId
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
																	serumTechnicianId : $scope.userId,
																	serumBloodGroupObj : "",
																	serumIrregularAntibodies:"",
																	checkedSerumGrouping : false,
																	resultACell : 'N' ,
																	resultBCell : 'N',
																	resultOCell : 'N',
																	resultOBCell : 'N',
																};
															$state.go('redCellSerumWorklist');
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
							