angular
		.module('myApp')
		.controller(
				'shiftClosingController',
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
						'reportService',
						'PaginationService',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,reportService,PaginationService) {
							

							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$rootScope.loginpage = true;
							$scope.saveSummery = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.handoverByUser=cookieObject.userName;
							
							$scope.counterMstObj = JSON.parse(sessionStorage.getItem('counterMstObj'));
							if($scope.counterMstObj != undefined){
							$scope.counterId = $scope.counterMstObj.counterId!=""?$scope.counterMstObj.counterId:0;
							}else{
								$scope.counterId=0;
							}
							
							
							 $scope.pagination = {
					                    currentPage: 1,
					                    totalPages: 0,
					                    pageSize: '10',
					                    search: '',
					                    searchData: PaginationService.searchData,
					                    setTotalPages: PaginationService.setTotalPages,
					                    getPagesList: PaginationService.getPagesList,
					                    setPage: PaginationService.setPage,
					                    setFirstPage: PaginationService.setFirstPage,
					                    setPreviousPage: PaginationService.setPreviousPage,
					                    setNextPage: PaginationService.setNextPage,
					                    setLastPage: PaginationService.setLastPage,
					                    sortBy: PaginationService.sortBy
					                  }
							 
							// Globle scope variable 
							
							// cash opening balance amount 
							
							$scope.totalShiftCloseBalance=0;
	
							$scope.cashAmount=0;
							
							$scope.cashDate=moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
							
							$scope.totalCashReciept = 0;
							
							$scope.totalBankReciept = 0;
							
							$scope.refundAmount=0;
							
							$scope.totalchequeReciept = 0;
							
							$scope.totalCardReciept = 0;
							
							$scope.totalCard = 0;
							
							$scope.totalIntrimCash = 0;
							
							$scope.totalIntrimCheque = 0;
							
							$scope.totalIntrimCard = 0;
							
							//cash denomination total
							$scope.totalAmount = 0;
							
							$scope.saveCash = false;
							$scope.saveBank = false;
							$scope.saveCard = false;
							$scope.searchWord="";
							
							/**NEW **/
							$scope.listOfCashIds = [];
							$scope.listOfBankIds = [];
							$scope.listOfCardIds = [];

							$scope.toDatepicker = new Date();
							$scope.fromDatepicker = new Date();
							
							$scope.openFromDtPicker = function() {
								$scope.fromDtpicker.opened = true;
							};

							$scope.fromDtpicker = {
									opened : false
									
							};
				
							$scope.fromDtOptions = {
									formatYear : 'yyyy',
    								showWeeks : false,
    								maxDate:new Date()
							};
							
							
							$scope.openFromDatePickerNew = function() {
								$scope.fromDatepickerNew.opened = true;
							};

							$scope.fromDatepickerNew = {
									opened : false
									
							};
							
							$scope.openToDtPicker = function() {
								$scope.toDtpicker.opened = true;
							};

							$scope.toDtpicker = {
									opened : false
							};
							
							$scope.toDtpickerOptions = {
									formatYear : 'yyyy',
    								showWeeks : false,
    								maxDate:new Date()
							};
							
							
							//Get User List 
							$scope.getusersList = function(){
						  		var URIuser = BASE_URL + ROOT_URL +"/adt/getUsersByNameOnType";
						  		var datauser = {
						  				organizationId:$scope.organizationId,
						  				unitId:$scope.unitId
						  		}
						  		
						  		GenericService.serviceAction("POST",URIuser,datauser).then(function(response){
						  			$scope.usersList = [];
						  			if(response.data.status == "success"){
						  				$rootScope.stopLoader();					
							  			
								  			$scope.usersList=response.data.listObject;

						  			console.log("usersList",$scope.getusersList);
						  			}
						  		});
						  		}
						  		$scope.getusersList();
						  	
							
							$scope.getCashHandoverToLoger = function(){
								
								var data = {
										
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										createdBy:$scope.userId
								}
								
								var URI = BASE_URL + ROOT_URL+"/api/global/getCashHandoverDetails";
								GenericService
								.serviceAction("POST", URI, data)
								.then(
										function(response) {
											$scope.CashHandoverToLog = [];
											
											
											if (response.data.status == "success") {
												$scope.CashHandoverToLog = response.data.listObject;
												
												
												
												angular.forEach($scope.CashHandoverToLog,function(value,index) {
											
													$scope.cashAmount =value.cashHandoverAmountlog;
													$scope.cashDate =value.createdDate;
												});

												// To get Cash Receipt List
												
												
													var data = {
															
															unitId : $scope.unitId,
															organizationId : $scope.organizationId,
															createdBy:$scope.userId,
															openingDate:$scope.cashDate,
															currentDate:moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
													}
													
													var URI = BASE_URL + ROOT_URL+"/api/global/getCashReceipts";
													GenericService
														.serviceAction("POST", URI, data)
														.then(
																function(response) {
																	$scope.cashReceipt = [];
																	
																	
																	if (response.data.status == "success") {
																		$scope.cashReceipt = response.data.listObject;
																		
																		angular.forEach($scope.cashReceipt,function(value,index) {
																			var newArr = {};
																			newArr.paymentReceiptDetailsId = value.paymentReceiptDetailsId;
																			newArr.paymentRefundId = null;
																			newArr.interimHandoverId = null;
																			
																			$scope.listOfCashIds.push(newArr);
																			$scope.totalCashReciept +=value.amount;
																		});
																		console.log("getCashReceipts",$scope.cashReceipt);
																	}
																});
												
												
												// To get Bank Receipts
											
													var data = {
															
															unitId : $scope.unitId,
															organizationId : $scope.organizationId,
															createdBy:$scope.userId,
															openingDate:$scope.cashDate,
															currentDate:moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
															
													}
													
													var URI = BASE_URL + ROOT_URL+"/api/global/getBankReceipts";
													GenericService
														.serviceAction("POST", URI, data)
														.then(
																function(response) {
																	$scope.bankReceipt = [];
																	
																	
																	if (response.data.status == "success") {
																		$scope.bankReceipt = response.data.listObject;
																		
																		
																		
																		angular.forEach($scope.bankReceipt,function(value,index) {
																			var newArr = {};
																			newArr.paymentReceiptDetailsId = value.paymentReceiptDetailsId;
																			newArr.interimHandoverId = null;
																			
																			$scope.listOfBankIds.push(newArr);
																			$scope.totalBankReciept +=value.amount;
																		});
																		console.log("getBankReceipts",$scope.bankReceipt);
																	}
																});
												
													
														// To get Refund  list
													
														
														var data = {
																
																unitId : $scope.unitId,
																organizationId : $scope.organizationId,
																createdBy:$scope.userId,
																openingDate:$scope.cashDate,
																currentDate:moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
														}
														
														var URI = BASE_URL + ROOT_URL+"/api/global/getRefundList";
														GenericService
															.serviceAction("POST", URI, data)
															.then(
																	function(response) {
																		$scope.refundList = [];
																		
																		
																		if (response.data.status == "success") {
																			$scope.refundList = response.data.listObject;
																			
																			
																			
																			angular.forEach($scope.refundList,function(value,index) {
																				var newArr = {};
																				newArr.paymentReceiptDetailsId = null;
																				newArr.paymentRefundId = value.paymentRefundId;
																				newArr.interimHandoverId = null;
																				
																				$scope.listOfCashIds.push(newArr);
																				$scope.refundAmount +=value.amount;
																			});
																			console.log($scope.refundList);
																		}
																	});
													
														// To get Cheque details
														
															
															var data = {
																	
																	unitId : $scope.unitId,
																	organizationId : $scope.organizationId,
																	createdBy:$scope.userId,
																	openingDate:$scope.cashDate,
																	currentDate:moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
															}
															
															var URI = BASE_URL + ROOT_URL+"/api/global/getChequeInHand";
															GenericService
																.serviceAction("POST", URI, data)
																.then(
																		function(response) {
																			$scope.cheque = [];
																			
																			
																			if (response.data.status == "success") {
																				$scope.cheque = response.data.listObject;
																				
																				
																				
																				angular.forEach($scope.cheque,function(value,index) {
																			
																					$scope.totalchequeReciept +=value.amount;
																				});
																				console.log("getChequeInHand",$scope.cheque);
																			}
																		});
														
														
															// To get card receipts details
															
																var data = {
																		
																		unitId : $scope.unitId,
																		organizationId : $scope.organizationId,
																		createdBy:$scope.userId,
																		openingDate:$scope.cashDate,
																		currentDate:moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
																}
																
																var URI = BASE_URL + ROOT_URL+"/api/global/getCardReceipts";
																GenericService
																	.serviceAction("POST", URI, data)
																	.then(
																			function(response) {
																				$scope.cardReceipts = [];
																				
																				
																				if (response.data.status == "success") {
																					$scope.cardReceipts = response.data.listObject;
																					
																					angular.forEach($scope.cardReceipts,function(value,index) {
																						var newArr = {};
																						newArr.paymentReceiptDetailsId = value.paymentReceiptDetailsId;
																						newArr.interimHandoverId = null;
																						
																						$scope.listOfCardIds.push(newArr);
																						$scope.totalCardReciept +=value.amount;
																					});
																					console.log("getCardReceipts",$scope.cardReceipts);
																				}
																			});
															
																// To get card In Hand
																
																	var data = {
																			
																			unitId : $scope.unitId,
																			organizationId : $scope.organizationId,
																			createdBy:$scope.userId,
																			openingDate:$scope.cashDate,
																			currentDate:moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
																			
																	}
																	
																	var URI = BASE_URL + ROOT_URL+"/api/global/getCardInHand";
																	GenericService
																		.serviceAction("POST", URI, data)
																		.then(
																				function(response) {
																					$scope.card = [];
																					
																					
																					if (response.data.status == "success") {
																						$scope.card = response.data.listObject;
																						
																						
																						
																						angular.forEach($scope.card,function(value,index) {
																					
																							$scope.totalCard +=value.amount;
																						});
																						console.log("getCardInHand",$scope.card);
																					}
																				});
																
																	// To get Intrim Cash Amount 
																	
																		var data = {
																				
																				unitId : $scope.unitId,
																				organizationId : $scope.organizationId,
																				createdBy:$scope.userId,
																				openingDate:$scope.cashDate,
																				currentDate:moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
																				
																		}
																		
																		var URI = BASE_URL + ROOT_URL+"/api/global/getIntrimCash";
																		GenericService
																			.serviceAction("POST", URI, data)
																			.then(
																					function(response) {
																						$scope.intrimCash = [];
																						
																						
																						if (response.data.status == "success") {
																							$scope.intrimCash = response.data.listObject;
																							
																							angular.forEach($scope.intrimCash,function(value,index) {
																								var newArr = {};
																								newArr.paymentReceiptDetailsId = null;
																								newArr.paymentRefundId = null;
																								newArr.interimHandoverId = value.id;
																								
																								$scope.listOfCashIds.push(newArr);
																								$scope.totalIntrimCash +=value.cashHandoverAmount;
																							});
																							console.log("getIntrimCash",$scope.intrimCash);
																						}
																					});
																	
											
																		// To get Intrim Cheque Amount 
																		
																			var data = {
																					
																					unitId : $scope.unitId,
																					organizationId : $scope.organizationId,
																					createdBy:$scope.userId,
																					openingDate:$scope.cashDate,
																					currentDate:moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
																					
																			}
																			
																			var URI = BASE_URL + ROOT_URL+"/api/global/getIntrimCheque";
																			GenericService
																				.serviceAction("POST", URI, data)
																				.then(
																						function(response) {
																							$scope.intrimCheque = [];
																							
																							
																							if (response.data.status == "success") {
																								$scope.intrimCheque = response.data.listObject;
																								
																								angular.forEach($scope.intrimCheque,function(value,index) {
																									var newArr = {};
																									newArr.paymentReceiptDetailsId = null;
																									newArr.interimHandoverId = value.id;
																									
																									$scope.listOfBankIds.push(newArr);
																									$scope.totalIntrimCheque +=value.cashHandoverAmount;
																								});
																								console.log("getIntrimCheque",$scope.intrimCheque);
																							}
																						});
																		
																	
																			// To get getIntrim Card Amount 
																			
																				var data = {
																						
																						unitId : $scope.unitId,
																						organizationId : $scope.organizationId,
																						createdBy:$scope.userId,
																						openingDate:$scope.cashDate,
																						currentDate:moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
																						
																				}
																				
																				var URI = BASE_URL + ROOT_URL+"/api/global/getIntrimCard";
																				GenericService
																					.serviceAction("POST", URI, data)
																					.then(
																							function(response) {
																								$scope.intrimCard = [];
																								
																								
																								if (response.data.status == "success") {
																									$scope.intrimCard = response.data.listObject;
																									
																									angular.forEach($scope.intrimCard,function(value,index) {
																										var newArr = {};
																										newArr.paymentReceiptDetailsId = null;
																										newArr.interimHandoverId = value.id;
																										
																										$scope.listOfCardIds.push(newArr);
																										$scope.totalIntrimCard +=value.cashHandoverAmount;
																									});
																									console.log("getIntrimCheque",$scope.intrimCard);
																								}
																							});
																			
																			
																					
																		
												console.log($scope.CashHandoverToLog);
											}
										});
							}
							
							$scope.getCashHandoverToLoger();
							console.log("$scope.cashAmount",$scope.cashAmount);
							
							
							
							// To get Denomination list
							
							$scope.getDenominationList = function()
							{

								var data1 = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId
								}
								var URI1 = BASE_URL + ROOT_URL+ GETDENOMINATIONLIST;
									GenericService
									.serviceAction(METHOD_POST, URI1, data1)
									.then(
											function(response) {
												$scope.denominationList = [];

												if (response.data.status == "success") {
													//$scope.denominationList = response.data.listObject;
											
													angular
													.forEach(response.data.listObject,function(value,index) {
														var newArr = {};
														newArr.denominationId = value.denominationId;
														newArr.denomination = value.denomination;
														newArr.qty = 0;
														newArr.amount = 0;
														$scope.denominationList.push(newArr);
														});
													
														console.log(response.data.denominationList);
												}
											});
							}
							
							
							$scope.getDenominationList();
					
					
							$scope.calculateFun =function(item,idx)
							{
								console.log("item",item);
								console.log("index",idx);
								$scope.denominationList[idx].amount = item.denomination * (item.qty==undefined?0:item.qty);
								$scope.totalAmount=0;
								angular.forEach($scope.denominationList,function(value,index) {
							
									$scope.totalAmount +=value.amount;
								});
								
							}
							
							// save operations of shift closing  
							
							
							$scope.actual=0;
							
							$scope.saveCashAmount = function(){
								
								$rootScope.startLoader();
								$scope.saveCash = true;
								$scope.totalSystemAmount = $scope.totalAmount + $scope.totalIntrimCash;
								$scope.actual= $scope.cashAmount + $scope.totalCashReciept - $scope.refundAmount;
								$scope.difference=$scope.actual - $scope.totalSystemAmount;
								
								var data = {
										
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										createdBy:$scope.userId,
										ichAmount:$scope.totalIntrimCash,
										//toBeHandover:$scope.cashAmount,
										toBeHandover:$scope.totalAmount,
										actualAmount:$scope.actual,
										modeOfPayment:"cash",
										amount:$scope.totalSystemAmount, //system amount
										difference:$scope.difference,
										refundAmount:$scope.refundAmount,
										listOfIds:$scope.listOfCashIds,
										createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
								}
							
								var URI = BASE_URL + ROOT_URL+"/api/global/saveShiftCloseBalance";
								GenericService
									.serviceAction("POST", URI, data)
									.then(
											function(response) {
												
												
												if (response.data.status == 'success') {
													$rootScope.stopLoader();
													$scope.totalCashReciept = 0;
													$scope.totalIntrimCash = 0;
													$scope.refundAmount = 0;
													$scope.totalAmount = 0;
													$scope.totalCardReciept=0;
													$scope.totalIntrimCard=0;
													$scope.totalCard=0;
													$scope.totalBankReciept=0;
													$scope.totalIntrimCheque=0;
													$scope.totalchequeReciept=0;
													$scope.totalDifference=0;
													$scope.saveCash = false;
													$scope.totalShiftCloseBalance=0;
													$scope.getDenominationList();
													$scope.totalAmount=0;
													//$scope.getShiftCloseBlaSheet();
													$scope.validateShiftClosingBalance();
													$scope.getDenominationList();
													$scope.totalAmount=0;
													$scope.getCashHandoverToLoger();
													
													growl.success(response.data.message,
															{
																title : 'Success!'
															});
												} else if (response.data.status == 'error') {
													$rootScope.stopLoader();
													growl.error(
															response.data.message,
															{
																title : 'Error!'
															});
												}
														
									});
							}
							
							
							
							
							$scope.saveBankAmount = function(){
								
								$scope.saveBank = true;
								$rootScope.startLoader();
								$scope.systemAmount = $scope.totalIntrimCheque + $scope.totalchequeReciept;
								$scope.difference=$scope.totalBankReciept - $scope.systemAmount;
								
								var data = {
										
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										createdBy:$scope.userId,
										ichAmount:$scope.totalIntrimCheque,
										toBeHandover:$scope.totalchequeReciept,
										actualAmount:$scope.totalBankReciept,
										modeOfPayment:"cheque",
										amount:$scope.systemAmount, //system amt
										difference:$scope.difference,
										listOfIds:$scope.listOfBankIds,
										createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
								}
								var URI = BASE_URL + ROOT_URL+"/api/global/saveShiftCloseBalance";
								GenericService
									.serviceAction("POST", URI, data)
									.then(
											function(response) {
												if (response.data.status == 'success') {
													$rootScope.stopLoader();
													
													$scope.saveBank = false;
													$scope.totalBankReciept=0;
													$scope.totalIntrimCheque=0;
													$scope.totalchequeReciept=0;
													$scope.totalCashReciept = 0;
													$scope.totalIntrimCash = 0;
													$scope.refundAmount = 0;
													$scope.totalAmount = 0;
													$scope.totalCardReciept=0;
													$scope.totalIntrimCard=0;
													$scope.totalCard=0;
													$scope.totalDifference=0;
													$scope.totalShiftCloseBalance=0;
													$scope.getDenominationList();
													$scope.totalAmount=0;
													//$scope.getShiftCloseBlaSheet();
													$scope.validateShiftClosingBalance();
													$scope.getDenominationList();
													$scope.totalAmount=0;
													$scope.getCashHandoverToLoger();
													growl.success(response.data.message,
															{
																title : 'Success!'
															});
												} else if (response.data.status == 'error') {
													$rootScope.stopLoader();
													growl.error(
															response.data.message,
															{
																title : 'Error!'
															});
												}
											});
							}
							
							
							
								$scope.saveCardAmount = function(){
									$scope.saveCard=true;
									$rootScope.startLoader();
									$scope.systemAmount=$scope.totalIntrimCard + $scope.totalCard;
									$scope.difference=$scope.totalCardReciept - $scope.systemAmount;
									
								var data = {
										
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										createdBy:$scope.userId,
										ichAmount:$scope.totalIntrimCard,
										toBeHandover:$scope.totalCard,
										actualAmount:$scope.totalCardReciept,
										modeOfPayment:"card",
										amount:$scope.systemAmount,  //system amount
										difference:$scope.difference,
										listOfIds:$scope.listOfCardIds,
										createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
								};
								var URI = BASE_URL + ROOT_URL+"/api/global/saveShiftCloseBalance";
								GenericService
									.serviceAction("POST", URI, data)
									.then(
											function(response) {
												
												
												if (response.data.status == 'success') {
													$rootScope.stopLoader();
													$scope.totalCardReciept=0;
													$scope.totalIntrimCard=0;
													$scope.totalCard=0;
													$scope.totalBankReciept=0;
													$scope.totalIntrimCheque=0;
													$scope.totalchequeReciept=0;
													$scope.totalCashReciept = 0;
													$scope.totalIntrimCash = 0;
													$scope.refundAmount = 0;
													$scope.totalAmount = 0;
													$scope.totalDifference=0;
													$scope.saveCard=false;
													$scope.totalShiftCloseBalance=0;
													$scope.getDenominationList();
													$scope.totalAmount=0;
													//$scope.getShiftCloseBlaSheet();
													$scope.validateShiftClosingBalance();
													$scope.getCashHandoverToLoger();
													growl.success(response.data.message,
															{
																title : 'Success!'
															});
												} else if (response.data.status == 'error') {
													$rootScope.stopLoader();
													growl.error(
															response.data.message,
															{
																title : 'Error!'
															});
												}
											
											});
							}
							
								
								$scope.totalDifference=0;
								$scope.getShiftCloseBlaSheet = function(){
									
									var data = {
											unitId : $scope.unitId,
											organizationId : $scope.organizationId,
											createdBy:$scope.userId
									}
									
									var URI = BASE_URL + ROOT_URL+"/api/global/getShiftClosingBalanceSheet";
									GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.ShiftCloseBlaSheet = [];
													if (response.data.status == "success") {
														$scope.ShiftCloseBlaSheet = response.data.listObject;
														
														var num= 0;
														angular.forEach($scope.ShiftCloseBlaSheet,function(value,index) {
															
															$scope.totalDifference +=value.difference;
															$scope.totalShiftCloseBalance +=value.amountCash;
															
														});
														console.log($scope.ShiftCloseBlaSheet);
													}
												});
								}
								
								$scope.validateShiftClosingBalance = function(){
									
									var data = {
											unitId : $scope.unitId,
											organizationId : $scope.organizationId,
											createdBy:$scope.userId
									}
									
									var URI = BASE_URL + ROOT_URL+"/api/global/validateShiftClosingBalance";
									GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.ShiftCloseBlaSheet = [];
												
													if (response.data.status == "success") {
														$scope.ShiftCloseBlaSheet = response.data.listObject;
														
														var num= 0;
														/*if($scope.ShiftCloseBlaSheet == null){
															
															$scope.saveCash = true;
															$scope.saveBank = true;
															$scope.saveCard = true;
															$scope.saveSummery = true;
															
															growl.warning("Shift Is not opened",{
																
																title : 'warning'
															});
														}else{*/
															$("#shiftClosingModal").modal("show");
															
															if($scope.ShiftCloseBlaSheet.length > 0){
																$scope.totalDifference=0;
																angular.forEach($scope.ShiftCloseBlaSheet,function(value,index) {

																	$scope.totalDifference +=value.difference;
																	$scope.totalShiftCloseBalance +=value.amountCash;
																	
																	if(value.modeOfPayment=="cash"){
																		$scope.saveCash = true;
																		num += 1;
																	}else if(value.modeOfPayment=="cheque"){
																		$scope.saveBank = true;
																		num += 1;
																	}else if(value.modeOfPayment=="card"){
																		$scope.saveCard=true;
																		num += 1;
																	}
																	
																	if(num>=3){
																		$scope.saveSummery = false;
																		}else{
																			$scope.saveSummery = true;
																		}
																});
																
															}
															
														/*}*/
													}
												});
								}
								
							//	$scope.validateShiftClosingBalance();
								
							//	$scope.shiftCloseDt = moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
								$scope.savesummery = function(){
							//		$scope.shiftCloseDt =moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
									
									if($scope.handoverToshift == null || $scope.handoverToshift == "" ){
										
										$rootScope.stopLoader();
										growl.warning(
												"Please select handover To ",
												{
													title : 'warning!'
												});
									}else{
									var data = {
											
											unitId : $scope.unitId,
											organizationId : $scope.organizationId,
											createdBy:$scope.userId,
											amount:$scope.totalShiftCloseBalance,
											difference:$scope.totalDifference,
											handoverTo:$scope.handoverToshift,
											handoverBy:$scope.userId,
											cashierName:$scope.handoverByUser,
											createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
									}
									
									var URI = BASE_URL + ROOT_URL+"/api/global/saveShiftClosedetails";
									GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													
													
													if (response.data.status == 'success') {
														
														$scope.totalCardReciept=0;
														$scope.totalIntrimCard=0;
														$scope.totalCard=0;
														$scope.totalBankReciept=0;
														$scope.totalIntrimCheque=0;
														$scope.totalchequeReciept=0;
														$scope.totalCashReciept = 0;
														$scope.totalIntrimCash = 0;
														$scope.refundAmount = 0;
														$scope.totalAmount = 0;
														$scope.totalDifference=0;
														$scope.totalShiftCloseBalance=0;
														//$scope.getShiftCloseBlaSheet();
													//	$scope.validateShiftClosingBalance();
														$scope.getCashHandoverToLoger();
														$scope.getshiftClosingRecords();
														$rootScope.stopLoader();
														growl.success(response.data.message,
																{
																	title : 'Success!'
																});
														// $scope.$broadcast('show-errors-reset');
													} else if (response.data.status == 'error') {
														$rootScope.stopLoader();
														growl.error(
																response.data.message,
																{
																	title : 'Error!'
																});
													} else if (response.data.code == "409") {
														$rootScope.stopLoader();
														growl.error(
																response.data.message,
																{
																	title : 'Error!'
																});
													}
												});
									}
								}
								/***************Shift Closing Report**************Chetana*********/
								$scope.printShiftClosing=function()
								{
									
										var data =  {
												 "appName": "HIS",
								                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
								                 "reportDesignFilePath": "get_Shiftclosing.rpt",
								                 "reportFormat": "PDF",
								                 "exportFileName": "ShiftClosing",
								                 "reportParams": 
								                 [
													{
										                  "paramName": "FromDate",
										                  "paramType": "string",
										                  "paramValue": moment($scope.fromDatepicker).format('YYYY-MM-DD')
									                },
									                {
										                  "paramName": "ToDate",
										                  "paramType": "string",
										                  "paramValue": moment($scope.toDatepicker).format('YYYY-MM-DD')
									                },
									               
												      {
											             "paramName": "handover_by",
											             "paramType": "Integer",
											             "paramValue": ($scope.userId!=null ? $scope.userId:0)
												       }
											    ]

									 };
									
									
								reportService.generate(REPORT_API_URI1,data);
								
							}

								/*************Shift Closing Report END*************************/	
								//to get shift closing records
								$scope.getshiftClosingRecords = function(){
									
									var data = {
											
											unitId : $scope.unitId,
											organizationId : $scope.organizationId,
											handoverBy:$scope.userId,
											fromDate: moment($scope.fromDatepicker).format('YYYY-MM-DD'),
											toDate: moment($scope.toDatepicker).format('YYYY-MM-DD'),	
											handoverTo: $scope.handoverToUser==undefined ? 0 :$scope.handoverToUser,
											closingNumber:$scope.closingNumber ,
											
												
									}
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL+"/api/global/getshiftClosingRecords";
									GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													
													$scope.pagination.data = [];
													
													$scope.shiftClosingRecords = [];
													
													if (response.data.status == "success") {
														$rootScope.stopLoader();
														$scope.shiftClosingRecords = response.data.listObject;
														
														if($scope.shiftClosingRecords.length > 0){            
								                            PaginationService.searchData($scope.shiftClosingRecords , $scope.pagination);
								                          }
														
														
													}else
													{
														$rootScope.stopLoader();									
														growl.warning(response.data.message,{
															
															title : ERROR_MSG
														});
									
												}
													
													
												});
								}
								
								$scope.getshiftClosingRecords();
								
								$scope.firstTabLevel = 1 ;	
								
								$scope.thirdTabLevel = 1 ;	
								
								
								$scope.isSetFirstTabLevel = function(tabNum)
								{
									return $scope.firstTabLevel === tabNum;
								};
								$scope.setFirstTabLevel(1);
								
								
								$scope.setThirdTabLevel= function(newTab)
								{
									$scope.thirdTabLevel = newTab;
								};
								$scope.isSetThirdTabLevel = function(tabNum)
								{
									return $scope.thirdTabLevel === tabNum;
								};
								
								$scope.setFifthTabLevel= function(newTab)
								{
									$scope.FifthTabLevel = newTab;
								};
								$scope.isSetFifthTabLevel = function(tabNum)
								{
									return $scope.FifthTabLevel === tabNum;
								};
								
								$scope.setSixthTabLevel= function(newTab)
								{
									$scope.SixthTabLevel = newTab;
								};
								$scope.isSetSixthTabLevel = function(tabNum)
								{
									return $scope.SixthTabLevel === tabNum;
								};
								
								$scope.setSeventhTabLevel= function(newTab)
								{
									$scope.SeventhTabLevel = newTab;
								};
								$scope.isSetSeventhTabLevel = function(tabNum)
								{
									return $scope.SeventhTabLevel === tabNum;
								};
								
								
								
								$scope.setFirstTabLevel= function(newTab)
								{
									
									if(newTab!=1){
										$scope.setThirdTabLevel(1);
									}
									
									$scope.firstTabLevel = newTab;
								};
								
								//$scope.setThirdTabLevel(1);
								$scope.openShiftClosingModal = function(){
									$scope.validateShiftClosingBalance();
								}
						}]);



							