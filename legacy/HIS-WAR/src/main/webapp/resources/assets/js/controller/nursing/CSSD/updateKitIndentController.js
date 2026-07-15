angular
		.module('myApp')
		.controller(
				'updateKitIndentController',
				[
						'$scope',
						'$http',
						'$localStorage',
						'$sessionStorage',
						'$cookies',
						'$rootScope',
						'GenericService',
						'growl',
						'$state',
						'PagerService',
						'$stateParams',
						'$location',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, PagerService,$stateParams, $location) {
							
							$scope.init = function() {
								var cookieObject = $cookies
								.getObject('cookieObject');
								if (cookieObject == undefined) {
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								
								$scope.onEditKitIndent = false;
								
								
								if ($scope.userId == null) {
									$rootScope.loginpage = false;
									$state.go('login');
								}

								$rootScope.loginpage = true;
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
							
								$scope.kitIndentFromStoreObj = {
										
										organizationId: $scope.organizationId,
										unitId : cookieObject.unitId
								};
								
								
								//For From Store By User Id
								var URI = BASE_URL + ROOT_URL
								+ GETKITINDENTFROMSTORE + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&userId=" + $scope.userId;
																							
								GenericService
								.serviceAction("GET", URI, $scope.kitIndentFromStoreObj)
								.then(
										function(response) {
								$scope.kitIndentFromStoreList = [];

									if (response.data.status == "success") {
										$scope.kitIndentFromStoreList = response.data.listObject;
										console
											.log(
													"kitIndentFromStoreList",
													$scope.kitIndentFromStoreList);

								}
																		
							});
								
								
								//For To Store By CSSD
								var URI = BASE_URL + ROOT_URL
								+ GETKITINDENTTOSTORE + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
																								
								GenericService
								.serviceAction("GET", URI, $scope.storeMasterObj)
								.then(
										function(response) {
								$scope.kitIndentToStoreList = [];

									if (response.data.status == "success") {
										$scope.kitIndentToStoreList = response.data.listObject;
										console
											.log(
													"kitIndentToStoreList",
													$scope.kitIndentToStoreList);

								}
																		
							});
								
								//For Indent priority
								var URI = BASE_URL + ROOT_URL
								+ "/cssd/getIndentPriorityList" + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
														
								GenericService
								.serviceAction("GET", URI)
								.then(
										function(response) {
								$scope.kitIndentPriorityList = [];

									if (response.data.status == "success") {
										$scope.kitIndentPriorityList = response.data.listObject;
										console
											.log(
													"kitIndentPriorityList",
													$scope.kitIndentPriorityList);

								}
																		
							});
								
								$scope.getKitIndentByIndentNo();
																

							}
							
							
							//For Search Kit For Indent
                            $scope.kitDetailsList = [];
							
							$scope.searchKitByName = function() 
							{
								var URI = BASE_URL + ROOT_URL
										+ SEARCHKITBYKITNAME + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&name=" + $scope.searchKitField;// search instrument 
								var data = {};

								// GET instrument list by search
								if($scope.searchKitField === "") {
									
									$scope.kitDetailsList = [] ;
								}
								
								else {
									GenericService
									.serviceAction("GET", URI, data)
									.then(
											function(response) {
												if (response.data.status == "success") {
													$scope.kitDetailsList = response.data.listObject;
												}
											});	
								}

							}
							
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.updateKitIndent();
							}
							
							//For Selected Kits In Search Dropdown
							$scope.selectKit = function(kits) {
								
								var index = $scope.kitsNameList.indexOf(kits);
								
								if(index != -1) {
									$scope.kitsNameList.splice(index, 1);
								}else {
									$scope.kitsNameList.push(kits);
								}
								
							}
							
							//For Selected Kit Details
							$scope.finalKitList = [];
							
                            $scope.selectKitInfo = function(kitInfo) {
                            	
                            	var index = $scope.finalKitList.indexOf(kitInfo);
                            	
                            	if(index != -1) {
									$scope.finalKitList.splice(index, 1);
								}
								else {
									$scope.finalKitList.push(kitInfo);
								}
                            	                            	
                            }		
                            
                            $scope.showKitInfo = function() {
                            	$scope.kitList = angular.copy($scope.finalKitList);
                            }
                            
                            $scope.indentKitInstrumentInfo = [];
                           //For Instrument Modal 
                            $scope.getInstrumentInfo = function(data) {
								var URI = BASE_URL + ROOT_URL
								+ GETKITWITHINSTRUMENT + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.selectedToStore + "&kitId=" + data.kitId;
								var data = {};
								
									GenericService
									.serviceAction("GET", URI, data)
									.then(
											function(response) {
												if (response.data.status == "success") {
													$scope.indentKitInstrumentInfo = response.data.object;
												}
											});	
							}
							
                            $scope.removeKit = function(kitInfo){
                            	var index = $scope.kitList.indexOf(kitInfo);
                            	if(index != -1)
                            	{
                            		$scope.kitList.splice(index, 1);
                            	}
                            }
                            
                            
                            $scope.isGetKitDisabled = function(){
								
								var flag = false;
								
								if($scope.selectedFromStore == undefined){
									flag = true
								}
								if($scope.selectedToStore == undefined){
									flag = true
								}
								
								if(flag){
									return true;
								}else{
									return false;
								}
							
							}
                            
                            
                            $scope.isUpdateDisabled = function(){
								
								var flag = false;
								
								if($scope.selectedFromStore == undefined){
									flag = true
								}
								if($scope.selectedToStore == undefined){
									flag = true
								}
								if($scope.kitList != undefined && $scope.kitList.length > 0){
									for(var i = 0; i < $scope.kitList.length; i++){
										if($scope.kitList[i].quantity == undefined || $scope.kitList[i].quantity == ""){
											flag = true;
											break;
											}
									}
								}else{
									flag = true;
								}
								
								
								if(flag){
									return true;
								}else{
									return false;
								}
							
							}
                            
							
							$scope.kitIndentObj = {
									
									updatedBy: $scope.userId,
									indentNo: "",
									fromStoreId : "",
									toStoreId: "",
									remarks: ""
									
							};
							
							$scope.remove = function(index) {
								var newDataList = [];
								var le = $scope.kitsNameList.length;
								var i = 1;
								if (le != i) {
									angular.forEach(
											$scope.kitsNameList,
											function(v) {
												if (index != i) {
													newDataList.push(v);
												}
												i++;
											});

									$scope.kitsNameList = newDataList;
								}
							};
							
							
							$scope.updateKitIndent = function() {
								$scope.$broadcast('show-errors-check-validity');
								
								if ($scope.updateKitIndentForm.$valid) {
									var URI = BASE_URL + ROOT_URL
									+ UPDATEKITINDENT;
									
										if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;
	
										} else {
											$scope.kitIndentObj.updatedBy = $scope.userId;
											$scope.kitIndentObj.fromStoreId = $scope.selectedFromStore;
											$scope.kitIndentObj.toStoreId = $scope.selectedToStore;
											$scope.kitIndentObj.remarks = $scope.selectedKitIndentRemark;
											$scope.kitIndentObj.kitList = $scope.kitList;
											$scope.kitIndentObj.organizationId = $scope.organizationId;
											$scope.kitIndentObj.unitId = $scope.unitId;
											
											GenericService
												.serviceAction("PUT",URI,$scope.kitIndentObj)
												.then(function(response) {
															if (response.data.status == "success") {
																
																growl.success('Kit Indent Updated Successfully...!!',
																				{
																					title : 'Success!'
																				});
																$location.path('/KitIndent');
																$scope.$broadcast('show-errors-reset');

															} else {
																growl.error(
																				'Kit Indent already exits...!!',
																				{
																					title : 'Error!'
																				});
															}
														});
									      }
									}
								}
													
							$scope.kitsNameList = [];
							$scope.kitList = [];
							
							
							$scope.getKitIndentByIndentNo = function() {
						
								data = {};
								var URI = BASE_URL + ROOT_URL
								+ GETKITINDENTBYINDENTNO + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + '&indentNo=' + $stateParams.indentNo;
						
								GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.kitIndentObj = response.data.object;
												$scope.kitList = response.data.object.kitList
												
												$scope.selectedFromStore = response.data.object.fromStoreId;
												$scope.selectedToStore = response.data.object.toStoreId;
												$scope.selectedKitIndentRemark = response.data.object.remarks;
												//$scope.kitList = editKitIndent.kitList;
												$scope.kitsNameList = angular.copy($scope.kitList);
												/*$scope.kitsNameList = $scope.kitList;*/
												$scope.kitIndentObj.priorityId = response.data.object.priorityId;
											}
											
										});
							}
							$scope.init();
													
						}]);



