angular
		.module('myApp')
		.controller(
				'addNewKitIndentController',
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
								
								
								$scope.dummyKitIndentObj = {
										/*code : "",
										name : "",
										surgeon: "",
										kitIndentList: [],
										createdby : $scope.userId,
										updated_by : $scope.userId*/
									};

								$scope.kitIndentObj = $scope.dummyKitIndentObj;
								
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
								
								
								if($stateParams.indentNo){
									$scope.getKitIndentByIndentNo();
																		
								}
								

							}
							
							
							//For Search Kit For Indent
							
                            $scope.kitDetailsList = [];
                            $scope.loadingMsg="";
                            $scope.showLoadingMsg='N';
							$scope.searchKitByName = function() 
							{
								$scope.showLoadingMsg='Y';
								$scope.loadingMsg="Loading...";
								if($scope.isInstrument){
									var URI = BASE_URL + ROOT_URL
									+ SEARCHINSTRUMENTBYKITNAME + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.kitIndentObj.toStoreId + "&name=" + $scope.searchKitField;// search instrument 
							
									var data = {};

									// 	GET instrument list by search
									if($scope.searchKitField === "") {
										$scope.loadingMsg="No record found";
										$scope.kitDetailsList = [] ;
									}
							
									else {
										GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.kitDetailsList = response.data.listObject;
														if($scope.kitDetailsList.length==0)
															$scope.loadingMsg="No record found";
													}
												});	
									}
									
								}else{
										var URI = BASE_URL + ROOT_URL
										+ SEARCHKITBYKITNAME + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.kitIndentObj.toStoreId + "&name=" + $scope.searchKitField;// search instrument 
							
										var data = {};

										// GET instrument list by search
										if($scope.searchKitField === "") {
											$scope.loadingMsg="No record found";
											$scope.kitDetailsList = [] ;
										}
							
										else {
											GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) {
														if (response.data.status == "success") {
															$scope.kitDetailsList = response.data.listObject;
															if($scope.kitDetailsList.length==0)
																$scope.loadingMsg="No record found";
														}
													});	
										}
								}
								

							}
							
							
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveKitIndent();
							}
							
							$scope.clearSearchField = function(){
								$scope.showLoadingMsg='N';
								$scope.kitDetailsList = [];
								$scope.searchKitField = "";
								
							}
							
							
							//For Selected Kit Details
							$scope.selectedKit = {};
							$scope.tempKitData = [];
							
                            $scope.selectKitInfo = function(kitInfo) {
                            	
                            	var index = $scope.tempKitData.indexOf(kitInfo);
                            	
                            	if(index != -1) {
									$scope.tempKitData.splice(index, 1);
								}
								else {
									$scope.tempKitData.push(kitInfo);
								}
                            }		
                            
                            $scope.kitList = [];
							
                            $scope.showKitInfo = function() {
                            	$scope.showLoadingMsg='N';
                            	$scope.kitList = angular.copy($scope.tempKitData);             	
                            	$scope.searchKitField = "";
                            	$scope.kitDetailsList = "";
                            	$scope.tempKitData = [];
                            }
							     
                            
                            $scope.changeToStore = function(){
                            	$scope.kitList = []; 
                            }
                           
                           //For Instrument Modal 
                            $scope.getInstrumentInfo = function(data) {
								$scope.indentKitInstrumentInfo = data;
							}

                            $scope.kitIndentObj = {
									
									updatedBy: $scope.userId,
									createdBy : $scope.userId,
									fromStoreId : "",
									toStoreId: "",
									quantity: "",
									remarks: ""
									
							};
							
							$scope.isSaveDisabled = function(){
								
								var flag = false;
								
								if($scope.kitIndentObj.fromStoreId == undefined){
									flag = true
								}
								if($scope.kitIndentObj.toStoreId == undefined){
									flag = true
								}
								if($scope.kitIndentObj.priorityId == undefined){
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
							
							$scope.isGetKitDisabled = function(){
								
								var flag = false;
								
								if($scope.kitIndentObj.fromStoreId == undefined){
									flag = true
								}
								if($scope.kitIndentObj.toStoreId == undefined){
									flag = true
								}
								
								if(flag){
									return true;
								}else{
									return false;
								}
							
							}
							
							
							$scope.removeKit = function(kitInfo){
                            	var index = $scope.kitList.indexOf(kitInfo);
                            	if(index != -1)
                            	{
                            		$scope.kitList.splice(index, 1);
                            	}
                            }
							
							
							
							
							
							$scope.isInstrument = false;
							
							$scope.selectedKitIndents = [];
							
							//Function for editing the entries
							$scope.editKitIndent = function(kitObj) {
								$scope.kitIndentObj = kitObj;
								$scope.kitIndentObj.id = kitObj.id;
								$scope.selectedKitIndents = kitObj.kitIndentList;
								$scope.kitIndentObj.updatedBy = $scope.userId;
								$scope.kitIndentObj.createdBy = $scope.userId;
								$scope.onEditKitIndent = true;
							}
							
							$scope.saveKitIndent = function() {
								$scope.$broadcast('show-errors-check-validity');
								
									var URI = BASE_URL + ROOT_URL
									+ ADDNEWKITINDENT;

											$rootScope.startLoader();
											$scope.kitIndentObj.updatedBy = $scope.userId;
											$scope.kitIndentObj.createdBy = $scope.userId;
											$scope.kitIndentObj.kitList = $scope.kitList;
											$scope.kitIndentObj.organizationId = $scope.organizationId;
											$scope.kitIndentObj.unitId = $scope.unitId;
											
											GenericService
												.serviceAction("POST",URI,$scope.kitIndentObj)
												.then(function(response) {
															if (response.data.status == "success") {
																$('.modal-backdrop').remove();
																NavigateToKitIndent();
																$rootScope.stopLoader();
																growl.success('Kit Indent Added Successfully...!!',{title : 'Success!'});
																$scope.$broadcast('show-errors-reset');
															} else {
																$rootScope.stopLoader();
																growl.error('Kit Indent already exits...!!',{title : 'Error!'});
															}
														});
									}
								
							
							var NavigateToKitIndent = function(){
								$state.go('KitIndent');
							}

							$scope.clearSearchDetails = function(){
								$scope.kitDetailsList = [];
								$scope.kitList = [];
							}
							
							$scope.kitIndentListByIndentNo = [];
							
							$scope.getKitIndentByIndentNo = function() {
						
								data = {};
								var URI = BASE_URL + ROOT_URL
								+ GETKITINDENTBYINDENTNO + '?indentNo=' + $stateParams.indentNo;
						
								GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.kitIndentListByIndentNo = response.data.listObject;
											}
											
										});
							}
							
							$scope.init();
							
													
						}]);



