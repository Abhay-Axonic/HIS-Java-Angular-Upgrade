angular
		.module('myApp')
		.controller(
				'interimHandoverController',
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
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.handoverByUser=cookieObject.userName;
							$scope.saveBtn1=false;
							$scope.saveBtn2=false;
							$scope.saveBtn3=false;
							$scope.searchKeyword="";
							
							$scope.saveBtnFlag =true;
							$scope.updateBtnFlag = false;	
							$scope.editObj = {};
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
						  	
							$scope.getDenominationList = function()
							{

								var data1 = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId
								}
								var URI1 = BASE_URL + ROOT_URL+ GETDENOMINATIONLIST1;
									GenericService
									.serviceAction(METHOD_POST, URI1, data1)
									.then(
											function(response) {
												$scope.denominationList = [];

												if (response.data.status == "success") {
											
													angular
													.forEach(response.data.listObject,function(value,index) {
														var newArr = {};
														newArr.denominationId = value.denominationId;
														newArr.denomination = value.denomination;
														newArr.qty = 0;
														newArr.amount = 0;
														newArr.createdBy = $scope.userId;
														newArr.updatedBy = $scope.userId;
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
								$scope.totalAmount = 0;
								angular.forEach($scope.denominationList,function(value,index) {
							
									$scope.totalAmount +=value.amount;
								});
							}
							
							$scope.firstTabLevel = 1 ;	
							
							$scope.setFirstTabLevel= function(newTab)
							{
								$scope.firstTabLevel = newTab;
							};
							$scope.isSetFirstTabLevel = function(tabNum)
							{
								return $scope.firstTabLevel === tabNum;
							};
							$scope.setFirstTabLevel(1);
						
						//for date datepicker
					
							$scope.fromDatepicker = new Date();
							$scope.toDatepicker = new Date();
							$scope.datepicker = new Date();
							
							$scope.openDtPicker = function() {
								$scope.dtpicker.opened = true;
							};

							$scope.dtpicker = {
									opened : false
							};
							
							$scope.dtpickerOptions = {
									formatYear : 'yyyy',
    								showWeeks : false,
    								maxDate:new Date()
							};
							
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
				
							$scope.openToDtPicker = function() {
								$scope.toDtpicker.opened = true;
							};

							$scope.toDtpicker = {
									opened : false
							};
							
							$scope.toDtOptions = {
									formatYear : 'yyyy',
    								showWeeks : false,
    								maxDate:new Date()
							};
				
							$scope.cashAmount=0;
							
							$scope.cashDate=moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
							
							
							
							//to get shift closing records
							$scope.getshiftClosingRecords = function(){
								
								var data = {
										
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										createdBy: $scope.userId,
										fromDate: moment(new Date()).format('YYYY-MM-DD')
										
								}
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL+"/api/global/validateInterimByShiftClosing";
								GenericService
									.serviceAction("POST", URI, data)
									.then(
											function(response) {
												
												$scope.shiftClosingRecords = [];
												
												if (response.data.status == "success") {
													$rootScope.stopLoader();
													if(response.data.object==null){
														
														$scope.saveBtn1=true;
														$scope.saveBtn2=true;
														$scope.saveBtn3=true;
														growl.warning("Shift Is not opened",{
															
															title : 'warning'
														});
													}else{
														$("#InterimOpening").modal("show");
													}
													
												}
											});
							}
							
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
												
											
												if (response.data.status == "success") {
												
													$scope.CashHandoverToLog = response.data.listObject;
													
													angular.forEach($scope.CashHandoverToLog, function(value,index) {
														
														$scope.cashAmount =value.cashHandoverAmountlog;
														$scope.cashDate =value.createdDate;
														console.log("dd"+$scope.cashDate);
														console.log("cashAmount"+$scope.cashAmount);
														
														});
													
													if($scope.cashDate == 0){
														$scope.cashDate = moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
													}
													//get all cheques
													var dataForCheque={
															cashOpeingDate:$scope.cashDate,
															createdBy:$scope.userId,
															orgnizationId:$scope.organizationId,
															unitId:$scope.unitId,
															currentDate:moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
													}
													var CHEQUEURI = BASE_URL + ROOT_URL+"/adt/getChequeList";
													GenericService.serviceAction("POST", CHEQUEURI,dataForCheque)
													
													.then(function(response) {
														$scope.allChequeList = [];
														if (response.data.status == "success"){
						                              	  
						                              	  $scope.allChequeList=response.data.listObject;
						                              	  
														console.log($scope.allChequeList );
														}
													});
													
													
													//get all cards
													var dataForCard={
															cashOpeingDate:$scope.cashDate,
															createdBy:$scope.userId,
															orgnizationId:$scope.organizationId,
															unitId:$scope.unitId,
															currentDate:moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
													}
													var CARDURI = BASE_URL + ROOT_URL+"/adt/getCardTransactionList";
													GenericService.serviceAction("POST", CARDURI,dataForCard)
													
													.then(function(response) {
														$scope.allCardTransactionList = [];
														if (response.data.status == "success"){
						                              	  
						                              	  $scope.allCardTransactionList=response.data.listObject;
						                              	  
														console.log($scope.allCardTransactionList );
														}
													});
													
												}
												
											});
							}
						
							$scope.getCashHandoverToLoger();
							
					
						//for user validation1
							$scope.userSecurity =function(){
						
								
								var data = {
										organizationId: $scope.organizationId,
										unitId: $scope.unitId,
										handoverByPassword: $scope.handoverBySecurity,
										handoverToPassword: $scope.handoverToSecurity,
										cashHandoverToId: $scope.handoverTo,
										cashHandoverById:$scope.userId
								
								}
						
						var URI = BASE_URL + ROOT_URL+"/api/global/validateUser";
						
						GenericService.serviceAction(METHOD_POST, URI, data).then(function(response) {
									

									if (response.data.status == "success") {
										
										console.log("success");
										
										alert($scope.datepicker);
										var data1 = {
												
												date:moment($scope.datepicker).format('DD-MM-YYYY HH:mm:ss'),
												cobNumber:"",
												cashHandoverToId: $scope.handoverTo,
												cashHandoverById:$scope.userId,
												remarks:$scope.remarks,
												organizationId: $scope.organizationId,
												unitId: $scope.unitId,
												cashHandoverAmount:$scope.totalAmount,
												createdDate:moment($scope.date).format('DD-MM-YYYY HH:mm:ss'),
												updatedDate:moment($scope.date).format('DD-MM-YYYY HH:mm:ss'),
												createdBy:$scope.userId,
												updatedBy:$scope.userId,
												status:"A"
											}
										
										var URI2 = BASE_URL + ROOT_URL
										+ "/api/global/saveCashHandoverMaster";
										
										GenericService.serviceAction("POST", URI2, data1).then(function(response) {
													

													if (response.data.status == "success") {
														$scope.transactionId = response.data.object;
														console.log("success",response.data.object);
														
															$scope.finalList = [];
															angular.forEach($scope.denominationList, function(value , key) {
																
														 	var data2 = {
																			
														 					denomination:value.denomination,
														 					amount:value.amount,
														 					quantity:value.qty,
														 					createdBy:$scope.userId,
																			updatedBy:$scope.userId,
																			cashHandoverTransactionId:$scope.transactionId
																					}
																			
														 	$scope.finalList.push(data2);
														 				
								                     		});
																
															console.log("saveDenomination",$scope.finalList);
															
															
															var URI = BASE_URL + ROOT_URL+"/api/global/saveDenominationmaster";
															
															 GenericService.serviceAction("POST", URI,$scope.finalList)
															.then(function(response) {
																		console.log(response);
																		 
																		
																		if (response.data.status == 'success') {
																			$rootScope.stopLoader();
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
																console.log("SaveResult ",$scope.denominationList);
														
													}
													
													else
													{
														
														
															growl.error(response.data.message,
															{
																title : 'Error!'
															});
										
													}
											});
									}
									else
									{
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
									}
							});
						
					}
					
							//for user validation
							$scope.userSecurity1 =function(){
								
									if($scope.handoverTo==undefined || $scope.handoverTo==""){
										$rootScope.stopLoader();
										growl.warning("Please Enter Handover To ",
												{
													title : 'warning!'
												});
										return false;	
									}
									$scope.id =null;
									$scope.ichNumber = null;
									if($scope.editObj.id!=null && $scope.editObj.id!=undefined){
										$scope.id = $scope.editObj.id;
										$scope.ichNumber = $scope.editObj.ichNumber;
									}
									
									if($scope.tempDenominationList.length>0){
										$scope.denominationList = $scope.denominationList.concat($scope.tempDenominationList);
									}
									$scope.saveBtn1=true;
								
										var data1 = {
												id  : $scope.id,
												createdDate:moment($scope.datepicker).format('DD-MM-YYYY HH:mm:ss'),
												ichNumber:$scope.ichNumber,
												cashHandoverToId: $scope.handoverTo,
												cashHandoverById:$scope.userId,
												remarks:$scope.remarks,
									         	organizationId: $scope.organizationId,
												unitId: $scope.unitId,
												cashHandoverAmount:$scope.totalAmount,
												createdBy:$scope.userId,
												updatedBy:$scope.userId,
												status:"A",
												modeOfTransaction:"Cash",
												handoverByPassword: $scope.handoverBySecurity,
												handoverToPassword: $scope.handoverToSecurity,
												denominationDetailsMasterList : $scope.denominationList,
												statusId : 1 //for pending
											};
										
										var URI2 = BASE_URL + ROOT_URL + "/adt/saveInterimHandover";
										
										GenericService.serviceAction("POST", URI2, data1).then(function(response) {
											if (response.data.status == 'success') {
												$rootScope.stopLoader();
												$scope.saveBtn1=false;
												$scope.remarks="";
												$scope.grandTotal1=0;
												$scope.grandTotal=0;
												$scope.handoverTo = undefined;
												$scope.denominationList = [];
												$scope.getDenominationList();
												$scope.saveBtnFlag =true;
												$scope.updateBtnFlag = false;	
												$scope.editObj = {};
												$scope.totalAmount=0;
												$scope.handoverBySecurity="";
												$scope.handoverToSecurity="";
												$scope.getCashHandoverToLoger();
												$scope.getInterimHandoverList();
												
												growl.success(response.data.message,
													{
													title : 'Success!'
													});
										} else {
											$rootScope.stopLoader();
											$scope.saveBtn1=false;
											growl.error(
													response.data.message,
													{
														title : 'Error!'
													});
										}
									});
								
					}
						$scope.getInterimHandoverList = function()
						{
							$rootScope.startLoader();
								var interimdata = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										fromDate: moment($scope.fromDatepicker).format('YYYY-MM-DD'),
										toDate: moment($scope.toDatepicker).format('YYYY-MM-DD'),	
										cashHandoverById : $scope.userId,
										cashHandoverToId : $scope.handoverTo==undefined ? 0 :$scope.handoverTo,
										ichNumber:$scope.ichNumber	
								}
						var URIINTERIM = BASE_URL + ROOT_URL+ "/adt/getInterimTransactionList";
						GenericService
						.serviceAction(METHOD_POST, URIINTERIM, interimdata)
						.then(
								function(response) {
									
									$scope.pagination.data = [];
									
								$scope.interimHandoverList = [];
								if (response.data.status == "success") {
									$rootScope.stopLoader();
									$scope.interimHandoverList = response.data.listObject;
									

									if($scope.interimHandoverList.length > 0){            
			                            PaginationService.searchData($scope.interimHandoverList , $scope.pagination);
			                          }
									
									console.log($scope.interimHandoverList);
								}
						});
					}
					
					$scope.getInterimHandoverList();
					
					//get all denomination List
					$scope.getAllDenomination = function(info)
					{
						var dataDetails={
						
										interimHandoverTransactionId:info.id
								}
							
								var URIDENOMI = BASE_URL + ROOT_URL+"/adt/getDenominationDetailsList";
								return GenericService.serviceAction("POST", URIDENOMI,dataDetails)
								
								.then(function(response) {
									
									$scope.allDenominationlist = [];
                                      if (response.data.status == "success"){
                                    	  
                                    	  $scope.allDenominationlist=response.data.listObject;
                                    	  
                                    	  console.log($scope.allDenominationlist);
                                      }
                                      $scope.idate=$scope.allDenominationlist[0].createdDate;
                                      $scope.userName = info.handoverToUser;
                                 	 $scope.iamount= $scope.allDenominationlist[0].cashHandoverAmount;
                                 	 $scope.remark= $scope.allDenominationlist[0].remarks;
                                 	 $scope.modeOfTransaction= $scope.allDenominationlist[0].modeOfTransaction;
                                 	 return response;
                                      });
								
							}
							
					
							//calculate total amt of cheques
							$scope.totalList =[];
							$scope.grandTotal1 = 0;
							$scope.paymentRecieptDetailsIdList = [];
							$scope.calculateChequeTotal = function(amt,checkValue,paymentRecieptDetailsId)
							{
								if(checkValue!=0)
									{
									var newArr = {};
									newArr.paymentReceiptDetailsId = paymentRecieptDetailsId;
									$scope.paymentRecieptDetailsIdList.push(newArr);
									console.log("info",amt);
									console.log($scope.paymentRecieptDetailsIdList);
									$scope.grandTotal1 +=amt;
									
									
									}
								else{
									$scope.paymentRecieptDetailsIdList.splice(paymentRecieptDetailsId);
									$scope.grandTotal1 =$scope.grandTotal1 - amt;
								}
							}
							
							
							//calculate total amt of card
							$scope.totalList =[];
							$scope.grandTotal = 0;
							$scope.paymentRecieptDetailsIdList1 = [];
							$scope.calculateCardTotal = function(amt,checkValue,paymentRecieptDetailsId)
							{
								if(checkValue!=0)
									{
									var newArr = {};
									newArr.paymentReceiptDetailsId = paymentRecieptDetailsId;
									$scope.paymentRecieptDetailsIdList1.push(newArr);
									console.log("info",amt);
									console.log($scope.paymentRecieptDetailsIdList1);
									$scope.grandTotal +=amt;
									
									
									}
								else{
									$scope.paymentRecieptDetailsIdList1.splice(paymentRecieptDetailsId);
									
									$scope.grandTotal =$scope.grandTotal - amt;
								}
							}
							
												
							//for user validation
							$scope.userValidationForCheque =function(){	
								if($scope.handoverTo=="" || $scope.handoverTo==null ||$scope.handoverTo==0 ||$scope.handoverTo==undefined)
								{
									$scope.saveBtn2=false;
									growl.warning("Please select Handover To",
											{
												title : 'warning!'
											});
								}else{
									
									if($scope.paymentRecieptDetailsIdList.length>0){
									
									if($scope.handoverBySecurity==undefined || $scope.handoverBySecurity==""){
										$rootScope.stopLoader();
										$scope.saveBtn2=false;
										growl.warning("Please Enter Handover By Password",
												{
													title : 'warning!'
												});
										return false;	
									}
									
									if($scope.handoverToSecurity==undefined || $scope.handoverToSecurity==""){
										$rootScope.stopLoader();
										$scope.saveBtn2=false;
										growl.warning("Please Enter Handover To Password",
												{
													title : 'warning!'
												});
										return false;	
									}
									
									$scope.saveBtn2=true;
						
									var data1 = {
											createdDate:moment($scope.datepicker).format('DD-MM-YYYY HH:mm:ss'),
											ichNumber:"",
											cashHandoverToId: $scope.handoverTo,
											cashHandoverById:$scope.userId,
											remarks:$scope.remarks,
								         	organizationId: $scope.organizationId,
											unitId: $scope.unitId,
											cashHandoverAmount:$scope.grandTotal1,
											createdBy:$scope.userId,
											updatedBy:$scope.userId,
											status:"A",
											modeOfTransaction:"cheque",
											listOfIds:$scope.paymentRecieptDetailsIdList,
											handoverByPassword: $scope.handoverBySecurity,
											handoverToPassword: $scope.handoverToSecurity
										};
									
									var URI2 = BASE_URL + ROOT_URL
									+ "/adt/saveChequeInterimTransaction";
									
									GenericService.serviceAction("POST", URI2, data1).then(function(response) {
												if (response.data.status == "success") {
													$scope.saveBtn2=false;
													$rootScope.stopLoader();
													$scope.grandTotal1=0;
													$scope.grandTotal=0;
													$scope.remarks="";
													$scope.handoverTo=undefined;
													$scope.getCashHandoverToLoger();
													$scope.handoverBySecurity="";
													$scope.handoverToSecurity="";
													$scope.getInterimHandoverList();
													growl.success(response.data.message,
															{
																title : 'Success!'
															});
													
												}else
													{
													$rootScope.stopLoader();
													$scope.saveBtn2=false;
													growl.error(response.data.message,
																{
																	title : 'Error!'
																});
											
														}
									});
									}else{
										$rootScope.stopLoader();
										$scope.saveBtn2=false;
										growl.warning("Please Select Atleast one Transaction",
												{
													title : 'warning!'
												});
									}
							}
				}
							
			//save card transaction
							$scope.userValidationForCard =function(){	
								if($scope.handoverTo=="" || $scope.handoverTo==null ||$scope.handoverTo==0 ||$scope.handoverTo==undefined)
								{
									$scope.saveBtn3=false;
									growl.warning("Please select Handover To",
											{
												title : 'warning!'
											});
								}else{
									if($scope.paymentRecieptDetailsIdList1.length>0){
										
									$scope.saveBtn3=true;
									
									if($scope.handoverBySecurity==undefined || $scope.handoverBySecurity==""){
										$rootScope.stopLoader();
										$scope.saveBtn3=false;
										growl.warning("Please Enter Handover By Password",
												{
													title : 'warning!'
												});
										return false;	
									}
									
									if($scope.handoverToSecurity==undefined || $scope.handoverToSecurity==""){
										$rootScope.stopLoader();
										$scope.saveBtn3=false;
										growl.warning("Please Enter Handover To Password",
												{
													title : 'warning!'
												});
										return false;	
									}
					
									var data1 = {
											createdDate:moment($scope.datepicker).format('DD-MM-YYYY HH:mm:ss'),
											ichNumber:"",
											cashHandoverToId: $scope.handoverTo,
											cashHandoverById:$scope.userId,
											remarks:$scope.remarks,
								         	organizationId: $scope.organizationId,
											unitId: $scope.unitId,
											cashHandoverAmount:$scope.grandTotal,
											createdBy:$scope.userId,
											updatedBy:$scope.userId,
											status:"A",
											modeOfTransaction:"card",
											listOfIds:$scope.paymentRecieptDetailsIdList1,
											handoverByPassword: $scope.handoverBySecurity,
											handoverToPassword: $scope.handoverToSecurity
										};
									
									var URI2 = BASE_URL + ROOT_URL + "/adt/saveCardInterimTransaction";
									
									GenericService.serviceAction("POST", URI2, data1).then(function(response) {
												if (response.data.status == "success") {
													$scope.saveBtn3=false;
														$rootScope.stopLoader();
														$scope.grandTotal=0;
														$scope.grandTotal1=0;
														$scope.handoverTo=undefined;
														$scope.remarks="";
														$scope.getCashHandoverToLoger();
														$scope.getInterimHandoverList();
														$scope.totalAmount=0;
														$scope.handoverBySecurity="";
														$scope.handoverToSecurity="";
																growl.success(response.data.message,
																		{
																			title : 'Success!'
																		});
																
															} else {
																$rootScope.stopLoader();
																$scope.saveBtn3=false;
																growl.error(
																		response.data.message,
																		{
																			title : 'Error!'
																		});
															}
									});
									}else{
										$rootScope.stopLoader();
										$scope.saveBtn3=false;
										growl.warning("Please Select Atleast one Transaction",
												{
													title : 'warning!'
												});
									}
									
								}
				}
				
				//to hide window
						$scope.hideWindow = function(){
							
							$('#detailsInterimOpening').modal('hide');
							// angular.element('#denominationOk').modal('hide');
						}
				$scope.cancelValidate= function(){
					$scope.saveBtn1=false;
					$scope.saveBtn2=false;
					$scope.saveBtn3=false;
					$scope.handoverBySecurity="";
					$scope.handoverToSecurity="";
				}
				
				$scope.cancelInterim = function(){
					$scope.saveBtn1=false;
					$scope.saveBtn2=false;
					$scope.saveBtn3=false;
					$scope.totalAmount=0;
					$scope.grandTotal=0;
					$scope.grandTotal1=0;
					$scope.remarks="";
					$scope.handoverTo = undefined;
					$scope.getCashHandoverToLoger();
					$scope.paymentRecieptDetailsIdList1 = [];
					$scope.paymentRecieptDetailsIdList = [];
					$scope.denominationList = [];
					$scope.getDenominationList();
					$scope.handoverBySecurity="";
					$scope.handoverToSecurity="";
				}
				
				$scope.printInterimHandover=function(id)
				{
						var data =  {
								 "appName": "HIS",
				                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
				                 "reportDesignFilePath": "get_Cash_InterimHandover.rpt",
				                 "reportFormat": "PDF",
				                 "exportFileName": "ShiftInterimHandover",
				                 "reportParams": 
				                 [
									
					                {
							             "paramName": "id",
							             "paramType": "Integer",
							             "paramValue": (id!=null ?id:0)
								       },
								       
							    ]

					 };
					
				reportService.generate(REPORT_API_URI1,data);
			}
				
				$scope.counter = 0;		
				$scope.intrimHanoverObj = {};
				$scope.getcountofcheck = function(check,index){
						if($scope.interimHandoverList[index].statusId!=2){
						if(check){
							$scope.counter += 1;
							$scope.intrimHanoverObj.id = $scope.interimHandoverList[index].id;
							$scope.intrimHanoverObj.cashHandoverToId = $scope.interimHandoverList[index].cashHandoverToId;
						}else{
							$scope.counter -= 1;
						}
						}
				}
				
				$scope.updateIntrimHandoverStatus = function(){
				
					if($scope.handoverBySecurity==undefined || $scope.handoverBySecurity==""){
						$rootScope.stopLoader();
						growl.warning("Please Enter Handover By Password",
								{
									title : 'warning!'
								});
						return false;	
					}
					
					if($scope.handoverToSecurity==undefined || $scope.handoverToSecurity==""){
						$rootScope.stopLoader();
						growl.warning("Please Enter Handover To Password",
								{
									title : 'warning!'
								});
						return false;	
					}
					$scope.intrimHanoverObj.statusId = 2;
					$scope.intrimHanoverObj.handoverByPassword = $scope.handoverBySecurity;
					$scope.intrimHanoverObj.handoverToPassword = $scope.handoverToSecurity;
					$scope.intrimHanoverObj.updatedDate =  moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
					$scope.intrimHanoverObj.updatedBy = $scope.userId;
					$scope.intrimHanoverObj.cashHandoverById = $scope.userId;
					$scope.intrimHanoverObj.unitId = $scope.unitId;
					$scope.intrimHanoverObj.organizationId = $scope.organizationId;
					if($scope.intrimHanoverObj.id !=undefined && $scope.intrimHanoverObj.id!=null){
							$rootScope.startLoader();
							var URI = BASE_URL + ROOT_URL + "/adt/updateInterimStatusToComplete";
						
							GenericService.serviceAction("POST",URI,$scope.intrimHanoverObj).then(function(response){
								
								if (response.data.status == 'success')
								{
									$rootScope.stopLoader();
									$scope.getInterimHandoverList();
									$scope.handoverBySecurity="";
									 $scope.handoverToSecurity="";
									 $scope.intrimHanoverObj = {};
									 $scope.counter = 0;		
									growl.success(response.data.message,
											{
												title : 'Success!'
											});
								} else if(response.data.status == 'error')
								{
									$rootScope.stopLoader();
									growl.error(response.data.message,
											{
												title : 'Error!'
											});
								}
							}); 
						}else{
							
							growl.warning("Please Select Record",
									{
										title : 'warning'
									});
						}
							
					}
				
				$scope.tempDenominationList = [];
				$scope.editDetails = function(info){
					$scope.saveBtnFlag =false;
					$scope.updateBtnFlag =true;	
					$scope.tempDenominationList = [];
					var promise =  $scope.getAllDenomination(info);
					promise.then(function(response) {
						$scope.handoverTo = info.cashHandoverToId;
						$scope.remarks = info.remarks;
						$scope.totalAmount = info.cashHandoverAmount;
						$scope.editObj.id = info.id;
						$scope.editObj.ichNumber = info.ichNumber; 
						$scope.tempDenominationList = angular.copy($scope.allDenominationlist);
						angular.forEach(response.data.listObject,function(value,index){
							angular.forEach($scope.denominationList,function(value1,index1){
								if(value.denomination ==  value1.denomination){
									value1.qty = value.qty;
									value1.amount = value1.denomination * value1.qty;
								}
							});
							
						});
						
						console.log("$scope.denominationList",$scope.denominationList);
					});
					$scope.setFirstTabLevel(1);
					$("#InterimOpening").modal("show");
				}
				$scope.saveintri = function(){
					$("#saveInterimOpening1").modal("show");

				}
				$scope.intrimHanover = function(){
					$scope.saveBtnFlag =true;
					$scope.updateBtnFlag = false;	
					$scope.handoverTo = "";
					$scope.remarks = "";
					$scope.totalAmount = "";
					$scope.getDenominationList();
					$scope.getshiftClosingRecords();
				}	
			}]);
							
