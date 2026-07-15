angular
		.module('myApp')
		.controller(
				'detailsComponentSeparationWorklistController',
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
							
							$scope.componentSeparationObj = $stateParams.componentSeparationObj;
							
							$scope.detailsObj = {
									technicianId : $scope.userId,
									remark:"",
									bagWeight: "",
									comObj : {},
									bloodBagNo : ""
								};
							
							console.log("$scope.componentSeparationObj",$scope.componentSeparationObj);
							$scope.collectedVolume = 0;
							$scope.dt = moment(new Date()).format('YYYY-MM-DD hh:mm:ss');
							
							//get component list
							$scope.componentList = [];
							$scope.getComponentList = function(){
								try{
								
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/bloodBank/getActiveBloodComponentMaster"+"/"+ $scope.organizationId +"/"+ $scope.unitId;
									console.log("EDIT",URI);
									GenericService.serviceAction("GET",URI).then(function(response){
										console.log("Edit",response);
										
										if(response.data.status == "success"){
											$rootScope.stopLoader();
											$scope.componentList = response.data.listObject;
											
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
							
							$scope.getComponentList();							
							
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
												}
											});
								}catch(e){
									console.log("Exception-",e);
								}
							}
							$scope.getEmployeeList();
							
							$scope.getComponentCode = function(obj){
								var bagNo = $scope.componentSeparationObj.bloodBagNo;
								bagNo = bagNo +"-"+obj.bloodComponentCode;
								$scope.detailsObj.bloodBagNo = bagNo;
							}
							
							//add into grid
							$scope.componentDetailsList = [];
							$scope.addComponent = function(){
								 $rootScope.startLoader();
								 if($scope.detailsObj.comObj.bloodComponentId == undefined || $scope.detailsObj.comObj.bloodComponentId == null || $scope.detailsObj.comObj.bloodComponentId == ""){
									 growl.warning("Please Select Component",
												{
													title : 'warning!'
												});
										$rootScope.stopLoader();
										return false;
								 }
								 
								 if($scope.detailsObj.bagWeight == undefined || $scope.detailsObj.bagWeight == null || $scope.detailsObj.bagWeight == ""){
									 growl.warning("Please Enter bag Weight",
												{
													title : 'warning!'
												});
										$rootScope.stopLoader();
										return false;
								 }
								 if(parseInt($scope.componentSeparationObj.bloodBagCapacity) < parseInt($scope.detailsObj.bagWeight) || parseInt($scope.detailsObj.bagWeight) == 0 ){
									 
									 growl.warning("Please Enter Valid bag Weight",
												{
													title : 'warning!'
												});
										$rootScope.stopLoader();
										return false;
								 }
								 $scope.collectedVolume =  $scope.collectedVolume + parseInt($scope.detailsObj.bagWeight) ;
								 
								 if(parseInt($scope.componentSeparationObj.bloodBagCapacity) <  parseInt($scope.collectedVolume)){
									 growl.warning("CollectedVolume is not greater than Blood Bag Capacity",
												{
													title : 'warning!'
												});
										$rootScope.stopLoader();
										 $scope.collectedVolume =  $scope.collectedVolume - parseInt($scope.detailsObj.bagWeight) ;
										return false;
								 }
								 
								 if($scope.componentDetailsList.length>0){
									 var isExist = false;
									 angular.forEach($scope.componentDetailsList,function(value,index){
										 if(value.componentId == $scope.detailsObj.comObj.bloodComponentId){
											  isExist = true;
										 }
									 });
									 if(isExist){
										 growl.warning("Selected Component is Already Exist ",
													{
														title : 'warning!'
													});
											$rootScope.stopLoader();
											return false;
									 }
								 }
								 var newArr = {};
								 newArr.bagNo = $scope.detailsObj.bloodBagNo;
								 newArr.componentName =  $scope.detailsObj.comObj.bloodComponentDesc;
								 newArr.componentId =  $scope.detailsObj.comObj.bloodComponentId;
								 newArr.bagWeight = $scope.detailsObj.bagWeight;
								 newArr.remark = $scope.detailsObj.remark;
								 $scope.componentDetailsList.push(newArr);
								 
								 $scope.detailsObj = {
											remark:"",
											bagWeight: "",
											comObj : {},
											bloodBagNo : "",
											technicianId : $scope.userId
										};
								 $rootScope.stopLoader();
							}
							
							$scope.removeComponent = function(index){
								 $scope.collectedVolume =  $scope.collectedVolume - parseInt($scope.componentDetailsList[index].bagWeight) ;
								$scope.componentDetailsList.splice(index,1);
							}
							
							$scope.saveComponentSeparationDetails = function(){
								try{
									 $rootScope.startLoader();
									if($scope.detailsObj.technicianId == ""){
										growl.warning("Please Select Technician",
												{
													title : 'warning!'
												});
										$rootScope.stopLoader();
										return false;
									}
									
									if( parseInt($scope.collectedVolume) != parseInt($scope.componentSeparationObj.bloodBagCapacity) ){
										growl.warning("Collected Volume should be equal to bag Capacity",
												{
													title : 'warning!'
												});
										$rootScope.stopLoader();
										return false;
									}
									
									$scope.finalLsit = [];
									if($scope.componentDetailsList!=null && $scope.componentDetailsList.length>0){
									
										angular.forEach($scope.componentDetailsList,function(value,index){
											var newArr = {};
											newArr.componentId = value.componentId;
											newArr.bloodBagNo = value.bagNo;
											newArr.bagWeight = value.bagWeight;
											newArr.remark = value.remark;
											newArr.createdBy = $scope.userId;
											newArr.createdDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
											newArr.status = "A";
											newArr.updatedBy = $scope.userId;
											newArr.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
											newArr.organizationId = $scope.organizationId;
											newArr.unitId = $scope.unitId;
											$scope.finalLsit.push(newArr);
										});
									}
									  
									var data={
											bloodComponentSeparationId : null,
											redCellId : $scope.componentSeparationObj.redCellId,
											transfusionTransmittedDiseasesId : $scope.componentSeparationObj.transfusionTransmittedDiseasesId ,
											bloodGroupId : $scope.componentSeparationObj.confirmationBloodGroupId,
											bagCapacity : $scope.componentSeparationObj.bloodBagCapacity ,
											collectedVolume : $scope.collectedVolume ,
											seperationDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											technicianId : $scope.detailsObj.technicianId,
											createdBy:$scope.userId,
									 		createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									 		status:"A",
									 		updatedBy:$scope.userId,
									 		updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									 		organizationId:$scope.organizationId,
									 		unitId:$scope.unitId,
									 		bloodComponentSeparationDetailsDtoList : $scope.finalLsit
									};
								

									var URI = BASE_URL + ROOT_URL + "/global/blood/component/separation/saveBloodComponentSeparation";

									GenericService.serviceAction(METHOD_POST, URI , data)
											.then(
												function(response) {
														if (response.data.status == "success") {
															growl.success(response.data.message,
																	{
																		title : 'Success!'
																	});
															$scope.detailsObj = {
																	technicianId : $scope.userId,
																	remark:"",
																	bagWeight: "",
																	comObj : {},
																	bloodBagNo : ""
																};
															$scope.componentDetailsList = [];
															 $scope.finalLsit = [];
															$state.go('bloodBankComponentSeparationWorklist');
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
							