angular
		.module('myApp')
		.controller(
				'updateKitIssueController',
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
								
								$scope.onEditKitIssue = false;
								
								$scope.datePickerForFromDate = new Date();
								
								$scope.datePickerForToDate = new Date();
								
								/*$scope.today = new Date();
								
								$scope.datePickerForFromDate = {
										maxDate : $scope.today,
										opened : false
										};
								
								$scope.datePickerForToDate = {
										maxDate : $scope.today,
										opened : false
										};*/
								
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
								
								var URI = BASE_URL + ROOT_URL
								+ GETKITINDENTTOSTORE + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
								
								GenericService
								.serviceAction("GET", URI, $scope.kitIssueFromStoreObj)
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
								
									if($stateParams.issueNo)
									{
										$scope.getKitIssueByIssueNo();
									}
								
								
																

							}
							
							
							
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
								
								$scope.getKitIndentByMultipleFields = function() 
								{
									
									var fromDate ='';
									
									fromDate = ("0" + $scope.datePickerForFromDate.getDate()).slice(-2) +'-'+("0" + ($scope.datePickerForFromDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForFromDate.getFullYear();
									
									var toDate ='';
									
									toDate = ("0" + $scope.datePickerForToDate.getDate()).slice(-2) +'-'+("0" + ($scope.datePickerForToDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForToDate.getFullYear();
																									
									
									var kitIndentUrl = BASE_URL + ROOT_URL 
									+ GETKITINDENTBYMULTIPLEFIELDS + "?fromDate=" + fromDate + "&toDate=" + toDate + "&organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId;
									
									
									
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
													
													for(var i = 0; i < $scope.kitIndentList.length; i++ )
													{
														for(var j = 0; j < $scope.kitIndentList[i].kitList.length; j++ )
														{
															$scope.kitIndentList[i].kitList[j].kitCode = $scope.kitIndentList[i].kitList[j].code;
															$scope.kitIndentList[i].kitList[j].kitName = $scope.kitIndentList[i].kitList[j].name;
															
														}
														
													}
													
													
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
									
									toDate = ("0" + $scope.datePickerForToDate.getDate()).slice(-2) +'-'+("0" + ($scope.datePickerForToDate.getMonth()+1)).slice(-2)+'-'+$scope.datePickerForToDate.getFullYear();
																									
									
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
								
								
								
								$scope.selectedKitIndentToStoreId = $scope.selectedKitIssueFromStoreId;
								$scope.selectedKitIndentFromStoreId = $scope.selectedKitIssueToStoreId;
								
							}
							
							
							
							
							
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
							
							
							//For Search Kit For Indent
							
                            $scope.kitDetailsList = [];
							
							$scope.searchKitByName = function() 
							{
								
								var URI = BASE_URL + ROOT_URL
										+ SEARCHKITBYKITNAME + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&name=" + $scope.searchKitField;// search instrument 
								
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
								$scope.updateKitIssue();
							}
							
							
							//For Selected Kits In Search Dropdown
							//$scope.selectedKitInSearch = {};
							//$scope.tempKitList = [];
							
							$scope.selectKit = function(kits) {
								
								var index = $scope.kitsNameList.indexOf(kits);
								
								if(index != -1) {
									$scope.kitsNameList.splice(index, 1);
								}else {
									$scope.kitsNameList.push(kits);
								}
								
							}
							// To Show in A table							
							//$scope.kitsNameList = [];
							
						/*	$scope.showSelectedKitList = function() {
								
								$scope.selectedKitList = $scope.kitsNameList;
							}
						*/	
							
							
							
							//For Selected Kit Details
							//$scope.selectedKit = {};
						
							$scope.finalKitList = [];
							
							//For Selected Kit Details
							$scope.selectedKit = {};
							$scope.tempKitData = [];
							
                            $scope.selectKitInfo = function(kitInfo) {
                            	
                            	var index = $scope.tempKitData.indexOf(kitInfo);
                            	
                            	kitInfo.kitCode = kitInfo.code;
                            	kitInfo.kitName = kitInfo.name;
                            	
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
                            	
                                             	
                            	/*$scope.selectedKit = $scope.tempKitData;*/ 
                            	
                            	
                            	$scope.kitIssueObj.kitList = $scope.tempKitData;
                            	
                            }
                            
                            
                            // To Show On Search Kit Modal
                            
                            
                            /*$scope.selectedKitList = function() {
                            	
                            	$scope.kitsNameList = $scope.kitList;
                            	
                            }*/
							   
                            
                           /* $scope.indentKitInstrumentInfo = [];
                           //For Instrument Modal 
                            $scope.getInstrumentInfo = function(data) {
								
								
								var URI = BASE_URL + ROOT_URL
								+ GETKITWITHINSTRUMENT + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&kitId=" + data.kitId;
								var data = {};

								
									GenericService
									.serviceAction("GET", URI, data)
									.then(
											function(response) {
												if (response.data.status == "success") {
													$scope.indentKitInstrumentInfo = response.data.object;
												}
											});	
								
								
								
							}*/
                            
                            
                            //For Instrument Modal 
							
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
                            	
                            	/*info.code = info.kitCode;
                            	info.name = info.kitName;*/
                            	
                            	
                            	$scope.tempKitDataOnWithIndent = info; 
                            	$scope.selectedIndentObj = info;
                            	$scope.getKitsInKitIndentList(info);
                            	
                            }		
                            
                            /*$scope.addNewKitIssueList = [];
							
                            $scope.showKitInfoOnAddNewKitIssue = function() {
                            	
                            	$scope.selectedKitOnWithIndent = $scope.tempKitDataOnWithIndent; 
                            	
                            	$scope.addNewKitIssueList = $scope.list;
                            	
                            }*/
                            
                            $scope.addNewKitIssueList = [];
							
                            $scope.showKitInfoOnAddNewKitIssue = function() {
                            	
                            	$scope.selectedKitOnWithIndent = $scope.tempKitDataOnWithIndent; 
                            	
                            	$scope.kitIssueObj.kitList = $scope.list;
                            	
                            }
							
							
							
							
							/*$scope.kitIssueObj = {
									
									updatedBy: $scope.userId,
									issueNo: "",
									fromStoreId : "",
									toStoreId: "",
									remark: ""
									
							};*/
							
                            $scope.kitIssueObj = {};
								
													
							$scope.kitsNameList = [];
							$scope.addNewKitIssueList = [];
							
							
							$scope.currentAddKitObj = {};
                            $scope.tempKitDataForSterilizedKits = [];
                            
                          //To get sterilized kits by kit id
							$scope.getSterilizedKitsByKitId = function(info){
								
								$scope.tempKitDataForSterilizedKits = [];
								$scope.kitNameLable = info.kitName;
								
								$scope.currentAddKitObj = info;
								
								var condition = "";
								
								if($scope.withIndent && info.quantity != undefined )
								{
									condition = "&limit="+info.quantity; 
								}								
									
								var URI = BASE_URL + ROOT_URL
								+ GETSTERILIZEDKITSBYKITID + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&kitId=" + info.kitId + condition; 
								
								// var data = {};

						
							GenericService
							.serviceAction("GET", URI )
							.then(
									function(response) {
										$scope.sterilizedKitsList = [];
										if (response.data.status == "success") {
											$scope.sterilizedKitsList = response.data.listObject;
											console
													.log(
															"sterilizedKitsList",
															$scope.sterilizedKitsList);
	
										}
									});
								
							}
							
							
							//For Selected Sterilized Kit Details
							$scope.selectedSterilizedKit = {};
							
							
                            $scope.selectSterilizedKitInfo = function(sterilizedKitInfo) {
                            	
                            	var index = $scope.tempKitDataForSterilizedKits.indexOf(sterilizedKitInfo);
                            	
                            	sterilizedKitInfo.kitCode = sterilizedKitInfo.code;
                            	sterilizedKitInfo.kitName = sterilizedKitInfo.name;
                            	
                            	if(index != -1) {
									$scope.tempKitDataForSterilizedKits.splice(index, 1);
									
								}
								else {
									$scope.tempKitDataForSterilizedKits.push(sterilizedKitInfo);
								}
                            	                            	                           	
                            	/*$scope.getPreSterilizationEquipmentList(kitInfo);*/
                            	
                            }		
                            
                           // $scope.addNewKitIssueList = [];
                            
                            
                            
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
							
                            $scope.showSelectedSterilizedKits = function() {
                            	
                                /*$scope.selectedKit = $scope.tempKitData;*/
                            	
                            	for(var i = 0; i < $scope.kitIssueObj.kitList.length; i++)
                            	{
                            		if($scope.kitIssueObj.kitList[i].kitId == $scope.currentAddKitObj.kitId )
                            		{
                            			$scope.kitIssueObj.kitList[i].subKitList = $scope.tempKitDataForSterilizedKits;
                            			$scope.kitIssueObj.kitList[i].quantity = $scope.tempKitDataForSterilizedKits.length;
                            			break;
                            		}
                            	}
                            	
                            	console.log("kitIssueObj.kitList",$scope.kitIssueObj.kitList)
                            	
                            }
							
							
							
							$scope.getKitIssueByIssueNo = function() {
								
								 data = {};
								 
								var URI = BASE_URL + ROOT_URL
									+ VIEWKITBYISSUENO + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&issueNo=" + $stateParams.issueNo;
																													
								GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											if (response.data.status == "success") {
												$scope.kitIssueObj = response.data.object;
												$scope.selectedKitIssueFromStoreId = $scope.kitIssueObj.fromStoreId;
												$scope.selectedKitIssueToStoreId = $scope.kitIssueObj.toStoreId;
												$scope.withIndent = $scope.kitIssueObj.withIndent;
												$scope.selectedRemark = $scope.kitIssueObj.remark;
												$scope.selectedKitIssueCarrier = $scope.kitIssueObj.carrier;
												$scope.addNewKitIssueList = $scope.kitIssueObj.kitList;
											}
										});
							}
							
							
							$scope.sterilizedKitsList = [];
                            $scope.classNameForSterilizedKits = "";
                            
                            
                            $scope.showSterilizedKitListRow = function (data){
                            	
                            	$scope.subKitList = data.subKitList;
                            	
                            	$scope.classNameForSterilizedKits = ".subKitRow-"+ data.kitId;
                            	angular.element($scope.classNameForSterilizedKits).toggle();
                            	//document.getElementById("indentNo-"+info.indentNo).toggle();
                            	
                            	for(var i = 0; i < $scope.kitIssueObj.kitList.length; i++)
                            	{
                            		if(angular.element("subKitRow-"+$scope.kitIssueObj.kitList[i].kitId)[0] != undefined &&  angular.element("subKitRow-"+$scope.kitIssueObj.kitList[i].kitId)[0].style.display != "none" && $scope.kitIssueObj.kitList[i].kitId != data.kitId )
                            			{
                            				angular.element("subKitRow-"+$scope.kitIssueObj.kitList[i].kitId).toggle();
                            			}
                            		
                            	}
                            	
                            	
                            	
                            }
                            
                            
                            $scope.finalKitList = [];
                            
                            $scope.selectKitsToIssue = function(data){
                            	
                            	var index = $scope.finalKitList.indexOf(data);
                            	
                            	if(index != -1) {
									$scope.finalKitList.splice(index, 1);
								}
								else {
									$scope.finalKitList.push(data);
								}
                            }
							
                            
                            
                            //===========+++===================//
                            
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
                            
                            
                            
                            $scope.updateKitIssue = function() {
								$scope.$broadcast('show-errors-check-validity');
									
								
								
								if ($scope.updateKitIssueForm.$valid) {
									var URI = BASE_URL + ROOT_URL
									+ UPDATEKITISSUE;
									
									/*if ($scope.onEditKitIndent) {
										$scope.updateKitIndent()
									} else {*/ 
										/*if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;
	
										} else {*/
											// $scope.data = {};
											
											$scope.kitIssueObj.updatedBy = $scope.userId;
											//$scope.kitIndentObj.indentNo = $scope.indentNo;
											$scope.kitIssueObj.fromStoreId = $scope.selectedKitIssueFromStoreId;
											$scope.kitIssueObj.toStoreId = $scope.selectedKitIssueToStoreId;
											$scope.kitIssueObj.remark = $scope.selectedRemark;
											$scope.kitIssueObj.carrier = $scope.selectedKitIssueCarrier;
											$scope.kitIssueObj.kitList = $scope.finalKitList;
											$scope.kitIssueObj.organizationId = $scope.organizationId;
											$scope.kitIssueObj.unitId = $scope.unitId;
											if($scope.withIndent){
												$scope.kitIssueObj.indentNo = $scope.selectedIndentObj.indentNo;
											}
											
											
											
											GenericService
												.serviceAction("POST",URI,$scope.kitIssueObj)
												.then(function(response) {
															if (response.data.status == "success") {
																
																growl.success('Kit Issue Updated Successfully...!!',
																				{
																					title : 'Success!'
																				});
																$location.path('/kitIssue');
																$scope.$broadcast('show-errors-reset');
																
																//$scope.init();
																

															} else {
																growl.error(
																				'Something Went Wrong...!!',
																				{
																					title : 'Error!'
																				});
															}
														});
									     // }
									   //}
									}
								}
							
							
							$scope.init();
							
													
						}]);



