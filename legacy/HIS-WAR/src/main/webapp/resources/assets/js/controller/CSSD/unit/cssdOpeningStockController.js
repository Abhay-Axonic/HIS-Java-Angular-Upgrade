angular
		.module('myApp')
		.controller(
				'cssdOpeningStockController',
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
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, PagerService,$stateParams) {
							
							$scope.init = function() {
								var cookieObject = $cookies
								.getObject('cookieObject');
								if (cookieObject == undefined) {
									$state.go('login');
									return;
								}
								$scope.disableField=false;
								$scope.action="add";
								$scope.cssdOpeningBalanceId="";
								$scope.openingBalanceObj=$stateParams.openingBalanceObj;
								if($scope.openingBalanceObj!=undefined && $scope.openingBalanceObj!=null){
									
									if($scope.openingBalanceObj.openingBalanceId!=null){
										var URI = BASE_URL + ROOT_URL+ "/api/cssd/stock/opening/balances/getCssdOpeningBalanceImplantById";
										var objForGetList={
												id :$scope.openingBalanceObj.openingBalanceId
										};
										$scope.cssdOpeningBalanceId=$scope.openingBalanceObj.openingBalanceId;
										GenericService
										.serviceAction("POST", URI, objForGetList)
										.then(
												function(response) {
										$scope.finalKitList = [];

											if (response.data.status == "success") {
												$scope.storeId=response.data.listObject[0].storeId;
												if(response.data.listObject[0].isKit=='Y'){
													$scope.selectedRadioOption=1;
												}else{
													$scope.selectedRadioOption=2;
												}
												angular.forEach(response.data.listObject,function(value,index){
													if(value.isSterilize==true){
													value.sterilizedExpiryDate=moment(value.sterilizedExpiryDate,'DD/MM/YYYY').toDate();
													}
													if(value.isKit=='Y'){
														angular.forEach(response.data.listObject[index].kitInstrumentList,function(value1,index1){
															if(value.kitQty > 1){
															value1.originalInstrumentqty=(value1.quantity)/value.kitQty;
															}else{
																value1.originalInstrumentqty=value1.quantity;
															}
														});
													}
												});
												$scope.finalKitList = response.data.listObject;
												console
													.log(
															"kit List",
															$scope.finalKitList);

										}
									  });	
									}
									
									if($scope.openingBalanceObj.action=='add'){
										
									}else if($scope.openingBalanceObj.action=='edit'){
										$scope.disableField=true;
										$scope.action="update";
									}else{
										$scope.disableField=true;
										$scope.action="view";
									}
									
								}
								
								$scope.datePickerForToDate = new Date();
								
								
							    $scope.openDatePicker = function(index) {

		                             for (var i = $scope.finalKitList.length - 1; i >= 0; i--) {
		                                 if (i === index) {
		                                	 $scope.datepickerForSterilizedExpiryDate.opened[i] = true;
		                                 } else {
		                                	
		                                	 $scope.datepickerForSterilizedExpiryDate.opened[i] = false;
		                                 }
		                             }
		                         }
							    
								$scope.dateOptions = {
										formatYear : 'yyyy',
										showWeeks : false,
										minDate :new Date()
									};
								
							    $scope.datepickerForSterilizedExpiryDate = {
	                                    opened : [ false ],

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
								
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.storeId="";
								$rootScope.loginpage = true;
								$scope.visitTypeId =1;
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL+ "/cssd/getCssdStoreList";
								var objForStore={
										organizationId :$scope.organizationId,
										unitId : $scope.unitId,
										userId :cookieObject.userId
								};
								GenericService
								.serviceAction("POST", URI, objForStore)
								.then(
										function(response) {
								$scope.cssdStoreList = [];
								
									if (response.data.status == "success") {
										$scope.cssdStoreList = response.data.listObject;
										/******For initial cssd store selection*********/
										
										var cssdStoreObj = JSON.parse(sessionStorage.getItem('selectedCssdStore'));
										if (cssdStoreObj != undefined) {
										$scope.storeId = cssdStoreObj.storeId;
										}

										/***********************************************/
										console
											.log(
													"cssd Store List",
													$scope.cssdStoreList);

								}
									$rootScope.stopLoader();										
							});
								
							}
							
							$scope.init();
							$scope.selectedRadioOption="";

							$scope.showKitModal=function(){
                            	if($scope.selectedRadioOption==""){
                            		 growl.warning(
												'Select either kit or instrument',
												{
													title : 'Warning!'
												});
                            		return false;
                            	}
                            	$scope.tempKitList=[];
								$scope.kitDetailsList = [];
                           	 $('#searchKitModal').modal();
                            }
                           
							$scope.selectedKit = {};
							$scope.tempKitList=[];
                            $scope.selectKitInfo = function(kitInfo,idx) {
                            		if(kitInfo.isSelected==true){
                            			$scope.tempKitList.push(kitInfo);
                            		}else{
                            			$scope.tempKitList.splice(idx,1);
                            		}
                            	/*$scope.kitData.push(kitInfo);
                            	$scope.getPreSterilizationEquipmentList(kitInfo);*/
                            	
                            }		
                            
	
							//search kit by kitname
                            $scope.kitDetailsList = [];
							$scope.isInstrument = false;
							$scope.loadingMsg="";
							$scope.showLoadingMsg='N';
							$scope.searchKitByName = function() 
							{
								$scope.kitDetailsList = [];
								$scope.showLoadingMsg='Y';
								$scope.loadingMsg="Loading...";
								if($scope.selectedRadioOption==1){
									var URI = BASE_URL + ROOT_URL
									+ SEARCHKITBYKITNAME + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&name=" + $scope.searchKitField;// search instrument	
									
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
														
														angular.forEach(response.data.listObject,function(value,index){
															value.isSelected=false;
															value.isSterilize=false;
															value.kitQty=1;
															value.isKit='Y';
															angular.forEach(value.kitInstrumentList,function(value1,index1){
															value1.originalInstrumentqty=value1.quantity;
															});
															value.sterilizedExpiryDate="";
														});
														$scope.kitDetailsList = response.data.listObject;
														if($scope.kitDetailsList.length==0)
															$scope.loadingMsg="No record found";
													}
												});	
									}
									
								}else{
									
									var URI = BASE_URL + ROOT_URL
									+ SEARCHINSTRUMENTASKIT + "?organizationId=" + $scope.organizationId + "&unitId=" + $scope.unitId + "&storeId=" + $scope.storeId + "&name=" + $scope.searchKitField;// search instrument	
									
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
														angular.forEach(response.data.listObject,function(value,index){
															value.isSelected=false;
															value.isSterilize=false;
															value.kitQty=1;
															value.isKit='N';
															//value.sterilizedDate="";
															//value.sterilizedExpiryDate=moment(new Date()).format('DD/MM/YYYY');
															value.sterilizedExpiryDate="";
														});
														$scope.kitDetailsList = response.data.listObject;
														if($scope.kitDetailsList.length==0)
															$scope.loadingMsg="No record found";
													}
												});	
									}
								}
							}
							
						       $scope.finalKitList = [];
							  $scope.showKitInfo = function() {
								  if($scope.tempKitList.length==0){
		                           		 growl.warning(
														'Select kit!!',
														{
															title : 'Warning!'
														});
											return false;
		                           	}
		                            	$scope.showLoadingMsg='N';
		                            	if($scope.finalKitList.length > 0)
		                            	$('#verifyKitModal').modal();
		                            	else{
		                            	 $('#searchKitPopup').trigger('click');
		                            	  $scope.finalKitList=$scope.tempKitList;
		                            	}
	                            	//$('#verifyKitModal').modal();
	                            	$scope.searchKitField = "";
	                            }
							    
							    $scope.closeVerifyModalFun=function(){
							    	  $scope.finalKitList=$scope.tempKitList;
	                            	$('#closeBtnVerifyModal').trigger('click');
	                            	$('#searchKitPopup').trigger('click');
	                            }
							    
							    $scope.maintainKitInstrumentList=[];
							    $scope.selectedKitInstrumentList=[];
							    $scope.showInstrumentList=function(obj){
							    	 $scope.selectedKitInstrumentList=[];
							    	 $scope.maintainKitInstrumentList=[];
							    	// $scope.maintainKitInstrumentList=obj.kitInstrumentList;
							    	//$scope.selectedKitInstrumentList=$scope.maintainKitInstrumentList;
							    	 angular.forEach(obj.kitInstrumentList,function(value,index){
							    		 value.quantity=(value.originalInstrumentqty)*(obj.kitQty);
							    		 $scope.selectedKitInstrumentList.push(value);
							    	 });
							    }
							    
							    $scope.showInstrumentListPopup=function(obj){
							    	 $scope.selectedKitInstrumentList=[];
							    	 $scope.maintainKitInstrumentList=[];
							    	// $scope.maintainKitInstrumentList=obj.kitInstrumentList;
							    	//$scope.selectedKitInstrumentList=$scope.maintainKitInstrumentList;
							    	 angular.forEach(obj.kitInstrumentList,function(value,index){
							    		 value.quantity=(value.originalInstrumentqty)*(obj.kitQty);
							    		 $scope.selectedKitInstrumentList.push(value);
							    	 });
							    	$('#kitDetailsModal').modal();
							    }
							    
							    //Save 
							    $scope.saveCssdStock=function(){
							    	if($scope.finalKitList.length==0){
							    		growl.warning(
												'Add either Kit or Instrument',
												{
													title : 'Warning!'
												});
									  return false;
							    	}else{
							    		var flagForQty=false;
							    		var flagForSterilize=false;
							    		var flagForSterilizeExpiryIncorrect=false;
							    		angular.forEach($scope.finalKitList,function(value,index){
							    			//value.sterilizedExpiryDate=moment(value.sterilizedExpiryDate).format('DD-MM-YYYY');
							    			if(value.kitQty==0){
							    				flagForQty=true;
							    			}
							    			if(value.isSterilize==true ){
							    				if(value.sterilizedExpiryDate==undefined || value.sterilizedExpiryDate==""){
							    					flagForSterilize=true;
							    				}else{
							    				if(moment(value.sterilizedExpiryDate).format('DD-MM-YYYY')==moment(new Date()).format('DD-MM-YYYY')){
							    					flagForSterilizeExpiryIncorrect=true;
							    				}
							    			}
							    			}
							    		});
							    		
							    		if(flagForQty==true && flagForSterilize==true ){
							    			growl.warning(
													'Quantiy and Sterilization Expiry Date is required',
													{
														title : 'Warning!'
													});
										return false;	
							    		}
							    		if(flagForQty==true ){
							    			growl.warning(
													'Quantiy is required',
													{
														title : 'Warning!'
													});
										return false;	
							    		}else if(flagForSterilize==true){
							    			growl.warning(
													'Sterilization Expiry Date is required',
													{
														title : 'Warning!'
													});
										return false;	
							    		}else if(flagForSterilizeExpiryIncorrect==true){
							    			growl.warning(
													'Sterilization expiry date can not be current date',
													{
														title : 'Warning!'
													});
										   return false;
							    		}else{
							    			//process save call
							    			 $scope.finalKitInstrumentList=[];
							    			angular.forEach($scope.finalKitList,function(value,index){
							    			angular.forEach($scope.finalKitList[index].kitInstrumentList,function(value1,index){
	                            				value1.isKit=value.isKit;
	                            				value1.isSterilize=value.isSterilize;
	                            				value1.code=value.code;
	                            				value1.name=value.name;
	                            				if(value1.isSterilize==true){
	                            				 value1.sterilizedExpiryDate= moment(new Date(value.sterilizedExpiryDate)).format('DD-MM-YYYY')+" 00:00:00" ;
	                            				}
	                            				if(value.isKit=='N'){
	                            					value1.quantity=value.kitQty;
	                            				}else{
	                            					value1.kitId=value.kitId;
		                            				value1.kitQty=value.kitQty;
	                            				}
	                            				 $scope.finalKitInstrumentList.push(value1);
	                            			});
							    			});
							    			$rootScope.startLoader();
							    			var data={
							    					  storeId:$scope.storeId,
							    					  organizationId:$scope.organizationId,
							    					  unitId:$scope.unitId ,
							    					  created_by :$scope.userId,
							    					  updated_by :$scope.userId,
							    					  kitInstrumentList:$scope.finalKitInstrumentList,
							    					  updated_date:moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
							    			};
							    			
							    			if($scope.cssdOpeningBalanceId!=""){
							    				var URI = BASE_URL + ROOT_URL+ "/api/cssd/stock/opening/balances/update";
							    				data.cssdOpeningBalanceId=$scope.cssdOpeningBalanceId;
							    			}else{
							    			var URI = BASE_URL + ROOT_URL+ "/api/cssd/stock/opening/balances/save";
							    			}
							    			//return false;
							    			GenericService.serviceAction("POST", URI,data).then(
													function(response) {
														if (response.data.status == 'success') {
															if($scope.cssdOpeningBalanceId!=""){
																growl.success("Updated Successfully",
																		{
																			title : 'Success!'
																		});
															}else{
															growl.success("Saved Successfully",
																	{
																		title : 'Success!'
																	});
															// $scope.$broadcast('show-errors-reset');
															}
															
														} 
														$rootScope.stopLoader();
														//$scope.storeId="";
														$scope.selectedRadioOption="";
														$scope.finalKitList=[]; 
														$state.go('cssdFixedAssetMaster');
													});
							    			
							    		}
							    	}
							    	
							    	
							    	
							    }
							    
						}]);



