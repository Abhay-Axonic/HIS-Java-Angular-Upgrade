angular
		.module('myApp')
		.controller(
				'addNewKitReturnController',
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
															
								
								$scope.kitReturnFromStoreObj = {
										
										organizationId: $scope.organizationId,
										unitId : cookieObject.unitId
								};
								
								
								//For From Store By User Id
								var URI = BASE_URL + ROOT_URL
								+ GETKITINDENTFROMSTORE + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&userId=" + $scope.userId;
								
								/*var URI = BASE_URL + ROOT_URL
								+ GETKITINDENTFROMSTORE + '/' + $scope.userId;*/
								
								GenericService
								.serviceAction("GET", URI, $scope.kitReturnFromStoreObj)
								.then(
										function(response) {
								$scope.kitReturnFromStoreList = [];

									if (response.data.status == "success") {
										$scope.kitReturnFromStoreList = response.data.listObject;
										console
											.log(
													"kitReturnFromStoreList",
													$scope.kitReturnFromStoreList);

								}
																		
							});
								
								
								//To store is cssd
								/*var URI = BASE_URL + ROOT_URL
								+ GETKITINDENTTOSTORE + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;*/
								var URI = BASE_URL + ROOT_URL+ "/cssd/getCssdStoreList";
								/*var URI = BASE_URL + ROOT_URL
								+ GETKITINDENTTOSTORE;*/
								var objForStore={
										organizationId :$scope.organizationId,
										unitId : $scope.unitId,
										userId :$scope.userId
								};
								GenericService
								.serviceAction("POST", URI, objForStore)
								.then(
										function(response) {
								$scope.kitReturnToStoreList = [];

									if (response.data.status == "success") {
										$scope.kitReturnToStoreList = response.data.listObject;
										console
											.log(
													"kitReturnToStoreList",
													$scope.kitReturnToStoreList);

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
							
							$scope.searchKitByName = function() 
							{
								var URI = BASE_URL + ROOT_URL
										+ GETKITLISTFORKITRETURN + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&fromStoreId=" + $scope.selectedFromStoreId + "&toStoreId=" + $scope.selectedToStoreId + "&name=" + $scope.searchKitField;// search instrument 
								
								/*var URI = BASE_URL + ROOT_URL
								+ SEARCHKITBYKITNAME + "?name=" + $scope.searchKitField;*/
								
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
								$scope.saveKitReturn();
							}
							
							$scope.isSaveDisabled = function(){
								var flag = 0;
								
								if($scope.selectedFromStoreId == undefined || $scope.selectedFromStoreId == ""){
									flag = 1;
								}
								if($scope.selectedToStoreId == undefined || $scope.selectedToStoreId == ""){
									flag = 1;
								}
								if($scope.kitList == undefined ){
									flag = 1;
								}
								if($scope.kitList != undefined && $scope.kitList.length < 1 ){
									flag = 1;
								}
								
								if(flag == 1){
									return true;
								}else{
									return false;
								}
								
							}
							
							
							
							$scope.isGetKitDisabled = function(){
								var flag = 0;
								
								if($scope.selectedFromStoreId == undefined || $scope.selectedFromStoreId == ""){
									flag = 1;
								}
								if($scope.selectedToStoreId == undefined || $scope.selectedToStoreId == ""){
									flag = 1;
								}
								
								if(flag == 1){
									return true;
								}else{
									return false;
								}
							}
							
														
							
							//For View Pre Kit Details
							/*$scope.list = [];
                           							
							$scope.getPreSterilizationEquipmentList = function(kitInfo) 
							{
																
								$scope.list = kitInfo.kitInstrumentList;
							}*/
							
							
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
                            	                            	                           	
                            	/*$scope.getPreSterilizationEquipmentList(kitInfo);*/
                            	
                            }		
                            
                            $scope.kitList = [];
							
                            $scope.showKitInfo = function() {
                            	
                                             	
                            	/*$scope.selectedKit = $scope.tempKitData;*/ 
                            	
                            	angular.copy($scope.tempKitData,$scope.kitList);
                            	$scope.kitDetailsList = [];
                            	$scope.tempKitData = [];
                            	$scope.searchKitField = "";
                            	
                            }
							                              
                           
                           //For Instrument Modal 
                            $scope.getInstrumentInfo = function(data) {
								$scope.indentKitInstrumentInfo = data;
							}
							
                            
                            //To Remove kit
                            $scope.remove = function(index) {
								var newDataList = [];
								var le = $scope.kitList.length;
								var i = 1;
								if (le != i) {
									angular.forEach(
											$scope.kitList,
											function(v) {
												if (index != i) {
													newDataList.push(v);
												}
												i++;
											});

									$scope.kitList = newDataList;
								}
							};
							
							
							$scope.removeKit = function(kitInfo){
                            	var index = $scope.kitList.indexOf(kitInfo);
                            	if(index != -1)
                            	{
                            		$scope.kitList.splice(index, 1);
                            	}
                            }
							
							
							$scope.kitReturnObj = {
									
									updatedBy: $scope.userId,
									createdBy : $scope.userId,
									fromStoreId : "",
									toStoreId: "",
									carrier: "",
									remark: ""
									
							};
							
							
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
							
							
							
							
							$scope.saveKitReturn = function() {
								$scope.$broadcast('show-errors-check-validity');
							
								if ($scope.addNewKitReturnForm.$valid) {
									var URI = BASE_URL + ROOT_URL
									+ ADDNEWKITRETURN;
									
									/*if ($scope.onEditKitReturn) {
										$scope.updateKitReturn()
									} else { 
										if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;
	
										} else {*/
											// $scope.data = {};
									$scope.finalKitList=[];
									angular.forEach($scope.kitList,function(value,index){
										var obj={};
										obj.autoclaveDate=value.autoclaveDate;
										obj.autoclaveNumber=value.autoclaveNumber;
										obj.batchNumber=value.batchNumber;
										obj.expiryDate=value.expiryDate;
										obj.kitCode=value.kitCode;
										obj.kitId=value.kitId;
										obj.kitName=value.kitName;
										obj.reasonForReturn=value.reasonForReturn;
										obj.transitionNumber=value.transitionNumber;
										$scope.finalKitList.push(obj);
									});
											$rootScope.startLoader();
											
											$scope.kitReturnObj.updatedBy = $scope.userId;
											$scope.kitReturnObj.createdBy = $scope.userId;
											$scope.kitReturnObj.subKitList = $scope.finalKitList;
											$scope.kitReturnObj.organizationId = $scope.organizationId;
											$scope.kitReturnObj.unitId = $scope.unitId;
											$scope.kitReturnObj.fromStoreId = $scope.selectedFromStoreId;
											$scope.kitReturnObj.toStoreId = $scope.selectedToStoreId;
											
											GenericService
												.serviceAction("POST",URI,$scope.kitReturnObj)
												.then(function(response) {
															if (response.data.status == "success") {
																$('.modal-backdrop').remove();
																NavigateToKitReturn();
																$rootScope.stopLoader();
																growl.success('Kit Return Added Successfully...!!',{title : 'Success!'});
															} else {
																$rootScope.stopLoader();
																growl.error('Something Went Wrong!!!',{title : 'Error!'});
															}
														});
									    //  }
									  // }
									}
								}
							
							
							var NavigateToKitReturn = function(){
								$state.go('KitReturn');
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



