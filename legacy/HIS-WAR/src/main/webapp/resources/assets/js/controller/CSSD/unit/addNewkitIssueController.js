angular
		.module('myApp')
		.controller(
				'addNewkitIssueController',
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
								
								
								/******For initial cssd store selection*********/
								
								var cssdStoreObj = JSON.parse(sessionStorage.getItem('selectedCssdStore'));
								if (cssdStoreObj != undefined) {
								$scope.storeId = cssdStoreObj.storeId;
								}

								/***********************************************/
								
								
								$scope.selectedKitIssueFromStoreId = $scope.storeId;
								
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								
								$scope.onEditKitIssue = false;
								
								$scope.datePickerForFromDate = new Date();
								
								$scope.datePickerForToDate = new Date();
								
								$scope.openDatePickerForFromDate = function() {
									$scope.datePickerForFromDate.opened = true;
								
								};
								
								$scope.openDatePickerForToDate = function() {
									$scope.datePickerForToDate.opened = true;
								
								};
								
								
								
								
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

								$scope.kitIssueFromStoreObj = {
										
										organizationId: $scope.organizationId,
										unitId : cookieObject.unitId
								};
								
								
								
								$scope.sterReqFromStoreObj = {
										
										organizationId: $scope.organizationId,
										unitId : cookieObject.unitId
								};
								
								//For Issue From Store By User Id 
																
							/*	var URI = BASE_URL + ROOT_URL
								+ GETKITINDENTTOSTORE + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;*/
								
								var URI = BASE_URL + ROOT_URL+ "/cssd/getCssdStoreList";
								var objForStore={
										organizationId :$scope.organizationId,
										unitId : $scope.unitId,
										userId :cookieObject.userId
								};
								GenericService
								.serviceAction("POST", URI,objForStore)
								.then(
										function(response) {
								$scope.kitIssueFromStoreList = [];

									if (response.data.status == "success") {
										$scope.kitIssueFromStoreList = response.data.listObject;
										console
											.log(
													"kitIssueFromStoreList",
													$scope.kitIssueFromStoreList);

								}
																		
							});
								
								
								
								//For Issue To Store By CSSD
																
								var URI = BASE_URL + ROOT_URL
								+ GETKITINDENTFROMSTORE + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&userId=" + $scope.userId;
								
								GenericService
								.serviceAction("GET", URI, $scope.storeMasterObj)
								.then(
										function(response) {
								$scope.kitIssueToStoreList = [];

									if (response.data.status == "success") {
										$scope.kitIssueToStoreList = response.data.listObject;
										console
											.log(
													"kitIssueToStoreList",
													$scope.kitIssueToStoreList);

								}
																		
							});
								

							}
							
							
							// function for popup ok button call
							$scope.PopupOkBtn = function() {
								$scope.popUpFlag = false;
								$scope.saveKitIssue();
							}
							
							//For With Kit Indent Modal
							
							$scope.getFromAndToStoreListForKitIndent = function() {
								
								
								//For Kit Indent Modal From Store By User Id 
								
								var URI = BASE_URL + ROOT_URL
								+ GETKITINDENTFROMSTORE + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&userId=" + $scope.userId;
								
								GenericService
								.serviceAction("GET", URI, $scope.kitIssueFromStoreObj)
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
								
								
								// For Get Indent By Multiple Fields
								
								$scope.getKitIndentByMultipleFieldsForKitIssue = function() 
								{
									
									var fromDate ='';
									
									fromDate = ("0" + $scope.datePickerForFromDate.getDate()).slice(-2) +'-'+("0" + ($scope.datePickerForFromDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForFromDate.getFullYear();
									
									var toDate ='';
									
									toDate = ("0" + ($scope.datePickerForToDate.getDate()+1)).slice(-2) +'-'+("0" + ($scope.datePickerForToDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForToDate.getFullYear();
																									
									
									var kitIndentUrl = BASE_URL + ROOT_URL 
									+ GETKITINDENTBYMULTIPLEFIELDSFORKITISSUE + "?fromDate=" + fromDate + "&toDate=" + toDate + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
									
									
									
									if($scope.selectedKitIndentFromStoreId != undefined )
									{
										kitIndentUrl = kitIndentUrl + "&fromStore=" + $scope.selectedKitIndentFromStoreId; 
									}
									
									if($scope.selectedKitIndentToStoreId != undefined )
									{
										kitIndentUrl =  kitIndentUrl + "&toStore=" + $scope.selectedKitIndentToStoreId;
									}
								
									if($scope.selectedKitIndentStatus != undefined )
									{
										kitIndentUrl =  kitIndentUrl + "&status=" + $scope.selectedKitIndentStatus;
									}
									
									GenericService
									.serviceAction("GET", kitIndentUrl)
									.then(
											function(response) {
												$scope.kitIndentList = [];
												if (response.data.status == "success") {
													$scope.kitIndentList = response.data.listObject;
													console
															.log(
																	"kitIndentList",
																	$scope.kitIndentList);

												}
											});

								}	
								
								
								
								//For Kit Indent Modal To Store By CSSD
								
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
								
								$scope.selectedKitIndentToStoreId = $scope.selectedKitIssueFromStoreId;
								$scope.selectedKitIndentFromStoreId = $scope.selectedKitIssueToStoreId;
								
							}
							
							//For Against Sterilization Request Modal
							
							$scope.getFromAndToStoreListForAgainstSterReq = function() {
								
								
								//For Sterilization Request From Store By User Id 
								
								var URI = BASE_URL + ROOT_URL
								+ GETKITINDENTFROMSTORE + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&userId=" + $scope.userId;
								
								GenericService
								.serviceAction("GET", URI, $scope.sterReqFromStoreObj)
								.then(
										function(response) {
								$scope.sterReqFromStoreList = [];

									if (response.data.status == "success") {
										$scope.sterReqFromStoreList = response.data.listObject;
										console
											.log(
													"sterReqFromStoreList",
													$scope.sterReqFromStoreList);

								}
																		
							});
								
								
								// For Get Sterilization Request By Multiple Fields
								
								$scope.getSterilizationRequestByMultipleFieldsForKitIssue = function() 
								{
									
									var fromDate ='';
									
									fromDate = ("0" + $scope.datePickerForFromDate.getDate()).slice(-2) +'-'+("0" + ($scope.datePickerForFromDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForFromDate.getFullYear();
									
									var toDate ='';
									
									toDate = ("0" + ($scope.datePickerForToDate.getDate()+1)).slice(-2) +'-'+("0" + ($scope.datePickerForToDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForToDate.getFullYear();
																									
									
									var sterilizationRequestUrl = BASE_URL + ROOT_URL 
									+ GETSTERILIZATIONREQBYMULTIPLEFIELDSFORKITISSUE + "?fromDate=" + fromDate + "&toDate=" + toDate + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
									
									
									
									if($scope.selectedSterReqFromStoreId != undefined )
									{
										sterilizationRequestUrl = sterilizationRequestUrl + "&requestingStore=" + $scope.selectedSterReqFromStoreId; 
									}
									
									if($scope.selectedSterReqToStoreId != undefined )
									{
										sterilizationRequestUrl =  sterilizationRequestUrl + "&issueingStore=" + $scope.selectedSterReqToStoreId;
									}
																	
									
									GenericService
									.serviceAction("GET", sterilizationRequestUrl)
									.then(
											function(response) {
												$scope.sterilizationRequestList = [];
												if (response.data.status == "success") {
													$scope.sterilizationRequestList = response.data.listObject;
													
													
													console
															.log(
																	"sterilizationRequestList",
																	$scope.sterilizationRequestList);

												}
											});

								}	
								
								
								
								//For Kit Indent Modal To Store By CSSD
								
								var URI = BASE_URL + ROOT_URL
								+ GETKITINDENTTOSTORE + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								
								GenericService
								.serviceAction("GET", URI, $scope.storeMasterObj)
								.then(
										function(response) {
								$scope.sterReqToStoreList = [];

									if (response.data.status == "success") {
										$scope.sterReqToStoreList = response.data.listObject;
										console
											.log(
													"sterReqToStoreList",
													$scope.sterReqToStoreList);

								}
																		
							});
								
								
								
								$scope.selectedSterReqToStoreId = $scope.selectedKitIssueFromStoreId;
								$scope.selectedSterReqFromStoreId = $scope.selectedKitIssueToStoreId;
								
							}
							
							//For Get Kit List By Indent Id
							$scope.getKitListByIndentNo = function(info){
																
								var URI = BASE_URL + ROOT_URL
								+ GETKITLISTBYINDENTNO + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&indentNo=" + info.indentNo; 
								
								 var data = {};

						
							GenericService
							.serviceAction("GET", URI, data)
							.then(
									function(response) {
										$scope.kitInKitIndentList = [];
										if (response.data.status == "success") {
											$scope.kitInKitIndentList = response.data.listObject;
											console
													.log(
															"kitInKitIndentList",
															$scope.kitInKitIndentList);
	
										}
									});
								
							}
							
							
							//For Instrument Modal With Indent
							
							$scope.indentKitInstrumentInfo = {};
                            $scope.getInstrumentInfo = function(kitInfo) {
								$scope.indentKitInstrumentInfo = kitInfo;
							}
							
                                                     
                            
                          //For Kits in Kit indent
							$scope.list = [];
                           							
							$scope.getKitsInKitIndentList = function(info) 
							{
								$scope.list = info.kitList;
							} 
                            
                          //For Selected Kit Details
							$scope.selectedKitOnWithIndent = {};
							$scope.tempKitDataOnWithIndent = {};
							$scope.selectedIndentObj = {};
							
                            $scope.selectKitOnWithIndent = function(info) {
                            	
                            	$scope.tempKitDataOnWithIndent = info; 
                            	$scope.selectedIndentObj = info;
                            	$scope.getKitsInKitIndentList(info);
                            	
                            }		
                            
                            
                            // For Kit With Indent
                            $scope.addNewKitIssueList = [];
							
                            $scope.showKitInfoOnAddNewKitIssue = function() {
                            	
                            	$scope.selectedKitOnWithIndent = $scope.tempKitDataOnWithIndent; 
                            	
                            	$scope.addNewKitIssueList = $scope.list;
                            	
                            }
							
							//For Search Kit For Indent
							
                            $scope.kitDetailsList = [];
							
                            $scope.loadingMsg="";
                            $scope.isShowLoadingMsg='N';
							$scope.searchKitByName = function() 
							{
								 $scope.isShowLoadingMsg='Y';
								 $scope.loadingMsg="Loading...";
								var URI = BASE_URL + ROOT_URL
										+ SEARCHKITBYKITNAME + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.storeId + "&name=" + $scope.searchKitField;// search instrument 
								
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
													if($scope.kitDetailsList.length==0)
														$scope.loadingMsg="No record found"
												}
											});	
								}

							}
							
							$scope.clearPreviousSearchKitData=function(){
								 $scope.isShowLoadingMsg='N';
								 $scope.kitDetailsList = [] ;
								 $scope.searchKitField ="";
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
                            	                            	                           	
                            	/*$scope.getPreSterilizationEquipmentList(kitInfo);*/
                            	
                            }		
                            
                           // $scope.addNewKitIssueList = [];
							
                            $scope.showKitInfo = function() {
                            	$scope.addNewKitIssueList = angular.copy($scope.tempKitData);
                            	$scope.kitDetailsList = [];
                            	$scope.tempKitData = [];
                            	$scope.searchKitField = "";
                            }
                            
                            
                            
                            
                            
                            
                            $scope.removeKit = function(kitInfo){
                            	var index = $scope.addNewKitIssueList.indexOf(kitInfo);
                            	if(index != -1)
                            	{
                            		$scope.addNewKitIssueList.splice(index, 1);
                            	}
                            }
                            
                            $scope.removeKitDetails = function(kitInfo,kitDetails){
                            	var index = $scope.addNewKitIssueList.indexOf(kitInfo);
                            	if(index != -1)
                            	{
                            		var subIndex = $scope.addNewKitIssueList[index].subKitList.indexOf(kitDetails);
                            		if(subIndex != -1)
                            		{
                            			$scope.addNewKitIssueList[index].subKitList.splice(subIndex, 1);
                            			if($scope.withIndent == 2)
                            			{
                            				$scope.addNewKitIssueList[index].quantity = $scope.addNewKitIssueList[index].quantity - 1;
                            			}
                            		}
                            	}
                            }
                      
							
                            
                            // Function to update status of kit indent
                            
                            $scope.updateKitIndentStatus = function ()
                            {
                            	var URI = BASE_URL + ROOT_URL
								+ UPDATEKITINDENTSTATUS ;// update kit indent status 
								var data = {};

						
							GenericService
							.serviceAction("PUT", URI, $scope.selection)
							.then(
									function(response) {
										if (response.data.status == "success") {
											growl.success('Kit Indent Saved Successfully...!!',{
												title : 'Success!'
											});
											$scope.$broadcast('show-errors-reset');
											$scope.init();
										} else {
											growl.error('Something went wrong !',{
												title : 'Error!'
											});
										}
									});
                            	
                            	
                            	
                            }
                            
                                                       
							
                            //For With Kit Indent Modal
                            
                            $scope.withIndentModalKitList = [];
                            $scope.className = "";
                            
                            
                            $scope.showKitRow = function (info){
                            	
                            	$scope.withIndentModalKitList = info.kitList;
                            	
                            	$scope.className = ".selectedServicesList-"+info.indentNo;
                            	angular.element($scope.className).toggle();
                            	//document.getElementById("indentNo-"+info.indentNo).toggle();
                            	
                            	for(var i = 0; i < $scope.kitIndentList.length; i++)
                            	{
                            		if(angular.element(".selectedServicesList-"+$scope.kitIndentList[i].indentNo)[0].style.display != "none" && $scope.kitIndentList[i].indentNo != info.indentNo )
                            			{
                            				angular.element(".selectedServicesList-"+$scope.kitIndentList[i].indentNo).toggle();
                            			}
                            		
                            	}
                            	
                            	
                            	
                            }
                            
                            
                            
                            
                            //For Against Sterilization Request
                            
                            $scope.againstSterReqModalKitList = [];
                            $scope.classNameForSterReq = "";
                            
                            
                            $scope.showKitRowForSterReq = function (requestInfo){
                            	
                            	$scope.againstSterReqModalKitList = requestInfo.kitList;
                            	
                            	$scope.classNameForSterReq = ".selectedServicesList-"+requestInfo.requestNo;
                            	angular.element($scope.classNameForSterReq).toggle();
                            	//document.getElementById("indentNo-"+info.indentNo).toggle();
                            	
                            	for(var i = 0; i < $scope.sterilizationRequestList.length; i++)
                            	{
                            		if(angular.element(".selectedServicesList-"+$scope.sterilizationRequestList[i].requestNo)[0].style.display != "none" && $scope.sterilizationRequestList[i].requestNo != requestInfo.requestNo )
                            			{
                            				angular.element(".selectedServicesList-"+$scope.sterilizationRequestList[i].requestNo).toggle();
                            			}
                            		
                            	}
                            	
                            	
                            	
                            }
                            
                            
                          //For Kits in Sterilization Request
							$scope.listForSterReq = [];
                           							
							$scope.getKitsInSterilizationRequest = function(requestInfo) 
							{
								$scope.listForSterReq = requestInfo.kitList;
								
								for(var i = 0;i < $scope.listForSterReq.length;i++ ){
									$scope.listForSterReq[i].code = $scope.listForSterReq[i].kitCode;
									$scope.listForSterReq[i].name =$scope.listForSterReq[i].kitName;
								}
								
							} 
                            
                            
                            
                          //For Selected Kit Details For Sterilization Request
							$scope.selectedKitAgainstSterReq = {};
							$scope.tempKitDataAgainstSterReq = {};
							$scope.selectedSterReqObj = {};
							
                            $scope.selectKitAgainstSterReq = function(requestInfo) {
                            	
                            	$scope.tempKitDataAgainstSterReq = requestInfo; 
                            	$scope.selectedSterReqObj = requestInfo;
                            	$scope.getKitsInSterilizationRequest(requestInfo);
                            	
                            }
                            
                            
                            
                            
                            //For Kit Against Sterilization Request
                            
                            $scope.showKitInfoOnAddNewKitIssueAganistSterReq = function() {
                            	
                            	$scope.selectedKitAgainstSterReq = $scope.tempKitDataAgainstSterReq; 
                            	
                            	$scope.addNewKitIssueList = $scope.listForSterReq;
                            	
                            }
                            
                            
                            
                            $scope.indentKitInstrumentInfo = {};
                            $scope.getInstrumentInfo = function(kitInfo) {
								$scope.indentKitInstrumentInfo = kitInfo;
							}
                            
                            
                         
                            $scope.currentAddKitObj = {};
                            $scope.tempKitDataForSterilizedKits = [];
                            
                          //To get sterilized kits by kit id
							$scope.getSterilizedKitsByKitId = function(info){
								$scope.loadingMsg="Loading..";
								$scope.sterilizedKitsList = [];
								$scope.tempKitDataForSterilizedKits = [];
								$scope.kitNameLable = info.name;
								
								$scope.currentAddKitObj = info;
								
								var condition = "";
								
								if($scope.withIndent == 1 && info.quantity != undefined )
								{
									condition = "&limit="+info.quantity; 
								}								
									
								var URI = BASE_URL + ROOT_URL
								+ GETSTERILIZEDKITSBYKITID + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.storeId + "&kitId=" + info.kitId + condition; 
								
								// var data = {};

						
							GenericService
							.serviceAction("GET", URI )
							.then(
									function(response) {
										if (response.data.status == "success") {
											$scope.sterilizedKitsList = response.data.listObject;
											if($scope.sterilizedKitsList.length==0)
											$scope.loadingMsg="No Sterilized Kits are available !";
											/*if( !($scope.sterilizedKitsList.length > 0) )
											{
												if(confirm("No Sterilized Kits are available ! ") == true )
												{
													$('#addKitModal').modal('hide');
												}
													
											}*/
											console
													.log(
															"sterilizedKitsList",
															$scope.sterilizedKitsList);
	
										}
									});
								
							}
                           
							
							//To remove a kit from a list
							
							/*$scope.removeKit = function (index) {
								//Find the record using Index from Array.
								var name = $scope.sterilizedKitsList[index].kitName;
								if ($window.confirm("Do you want to delete: " + name)) {
									//Remove the item from Array using Index.
									$scope.sterilizedKitsList.splice(index, 1);
								}
							}*/
							
							
							
							
							//For Selected Sterilized Kit Details
							$scope.selectedSterilizedKit = {};
							
							
                            $scope.selectSterilizedKitInfo = function(sterilizedKitInfo) {
                            	
                            	var index = $scope.tempKitDataForSterilizedKits.indexOf(sterilizedKitInfo);
                            	
                            	if(index != -1) {
									$scope.tempKitDataForSterilizedKits.splice(index, 1);
								}
								else {
									$scope.tempKitDataForSterilizedKits.push(sterilizedKitInfo);
								}
                            	                            	                           	
                            	/*$scope.getPreSterilizationEquipmentList(kitInfo);*/
                            	
                            }		
                            
                           // $scope.addNewKitIssueList = [];
							
                            $scope.showSelectedSterilizedKits = function() {
                            	
                                /*$scope.selectedKit = $scope.tempKitData;*/
                            	
                            	for(var i = 0; i < $scope.addNewKitIssueList.length; i++)
                            	{
                            		if($scope.addNewKitIssueList[i].kitId == $scope.currentAddKitObj.kitId )
                            		{
                            			$scope.addNewKitIssueList[i].subKitList = $scope.tempKitDataForSterilizedKits;
                            			if($scope.withIndent == 2)
                            				{
                            					$scope.addNewKitIssueList[i].quantity = $scope.tempKitDataForSterilizedKits.length;
                            				}
                            			
                            			break;
                            		}
                            	}
                            	
                            	console.log("addNewKitIssueList",$scope.addNewKitIssueList)
                            	
                            }
							
							
							
							
							$scope.sterilizedKitsList = [];
                            $scope.classNameForSterilizedKits = "";
                            
                            
                            $scope.showSterilizedKitListRow = function (data){
                            	
                            	$scope.subKitList = data.subKitList;
                            	
                            	$scope.classNameForSterilizedKits = ".subKitRow-"+ data.kitId;
                            	angular.element($scope.classNameForSterilizedKits).toggle();
                            	//document.getElementById("indentNo-"+info.indentNo).toggle();
                            	
                            	for(var i = 0; i < $scope.addNewKitIssueList.length; i++)
                            	{
                            		if(angular.element("subKitRow-"+$scope.addNewKitIssueList[i].kitId)[0] != undefined &&  angular.element("subKitRow-"+$scope.addNewKitIssueList[i].kitId)[0].style.display != "none" && $scope.addNewKitIssueList[i].kitId != data.kitId )
                            			{
                            				angular.element("subKitRow-"+$scope.addNewKitIssueList[i].kitId).toggle();
                            			}
                            		
                            	}
                            	
                            	
                            	
                            }
                            
                            
                           /* $scope.finalKitList = [];
                            
                            $scope.selectKitsToIssue = function(data){
                            	
                            	var index = $scope.finalKitList.indexOf(data);
                            	
                            	if(index != -1) {
									$scope.finalKitList.splice(index, 1);
								}
								else {
									$scope.finalKitList.push(data);
								}
                            }*/
                            
                            
                            
                            $scope.kitIssueObj = {
									
									updatedBy: $scope.userId,
									createdBy : $scope.userId,
									fromStoreId : "",
									toStoreId: "",
									carrier: "",
									remark: "",
									indentNo: "",
									
							};
                            
                            
                            
                            $scope.isOkDisabled = function(){
                            	if($scope.tempKitDataForSterilizedKits == undefined){
                            		return true;
                            	}else if($scope.tempKitDataForSterilizedKits != undefined && $scope.tempKitDataForSterilizedKits.length < 1)
                            	{
                            		return true;
                            	}else{
                            		return false;
                            	}
                            		
                            }
                            
                            
                            
                            
                            $scope.isSaveDisabled = function() {
                                var flag = 0;
                                
                                if( $scope.selectedKitIssueFromStoreId == undefined ){
                                	return true;
                                }
                                if($scope.selectedKitIssueToStoreId == undefined ){
                                	return true;
                                }
                                if($scope.kitIssueObj.carrier == undefined ){
                                	return true;
                                }
                                if($scope.withIndent == undefined){
                                	return true;
                                }
                            	if($scope.addNewKitIssueList.length < 1)
                                {
                                	return true;
                                }else{
                                
                                	for(var i = 0;i < $scope.addNewKitIssueList.length;i++){
                                		
                                		if($scope.addNewKitIssueList[i].subKitList == undefined)
                                		{
                                			flag = 1;
                                			break;
                                		}
                                		else if($scope.addNewKitIssueList[i].subKitList != undefined && $scope.addNewKitIssueList[i].subKitList.length  < 1 ){
                                    		flag = 1;
                                    		break;
                                    	}
                                    }
                                	
                                	if(flag == 1)
                                	{
                                		return true;
                                	}else {
                                		return false;
                                	}
                                	
                                }
                             }
							
                            	
                            
                            
                            $scope.saveKitIssue = function() {
								$scope.$broadcast('show-errors-check-validity');
									
								$scope.popUpFlag = false;
								
								if ($scope.addNewKitIssueForm.$valid) {
									var URI = BASE_URL + ROOT_URL
									+ ADDNEWKITISSUE;
									
									if ($scope.onEditKitIssue) {
										$scope.updateKitIssue()
									} else { 
										if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;
	
										} else {
											// $scope.data = {};
											$rootScope.startLoader();
											$scope.kitIssueObj.updatedBy = $scope.userId;
											$scope.kitIssueObj.createdBy = $scope.userId;
											$scope.kitIssueObj.kitList = $scope.addNewKitIssueList;
											$scope.kitIssueObj.organizationId = $scope.organizationId;
											$scope.kitIssueObj.unitId = $scope.unitId;
											$scope.kitIssueObj.withIndent = $scope.withIndent;
											
											if($scope.withIndent == 1){
											$scope.kitIssueObj.indentNo = $scope.selectedIndentObj.indentNo;
											}
											
											else if($scope.withIndent == 3){
												$scope.kitIssueObj.requestNo = $scope.selectedSterReqObj.requestNo;
											}
											
											$scope.kitIssueObj.fromStoreId = $scope.selectedKitIssueFromStoreId;
											$scope.kitIssueObj.toStoreId = $scope.selectedKitIssueToStoreId;
											
											
											GenericService
												.serviceAction("POST",URI,$scope.kitIssueObj)
												.then(function(response) {
															if (response.data.status == "success") {
																$('.modal-backdrop').remove();
																NavigateToKitIssue();
																$rootScope.stopLoader();
																growl.success('Kit Issue Added Successfully...!!',{title : 'Success!'});
															} else {
																$rootScope.stopLoader();
																growl.error('Something Went Wrong...!!',{title : 'Error!'});
															}
														});
									      }
									   }
									}
								}
                            
                            var NavigateToKitIssue = function(){
                            	$state.go('kitIssue');
                            }
                            
                            $scope.init();
							
								
						
						}]);



