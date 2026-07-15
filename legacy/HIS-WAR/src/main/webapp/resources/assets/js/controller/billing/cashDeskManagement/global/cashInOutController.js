(function () {
  "use strict";
						function cashInOutController($scope, $rootScope, $state, $http,
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
							$scope.cashHandoverById = $scope.userId;
							$scope.toDatepicker = new Date();
							$scope.fromDatepicker = new Date();
							$scope.datepicker = new Date();
							$scope.hospitalAccountId = "";
							$scope.saveBtnFlag =true;
							$scope.updateBtnFlag =false;
							$scope.editObj = {};
							$scope.handoverBySecurity=null;
							$scope.handoverToSecurity=null;
							$scope.handoverfromUser==undefined
							$scope.handoverToUser==undefined
							
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
							 
							//Get Template List 
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
							
						  		
						  		$scope.getUsersCashInHand = function(){
							  		var URIuser = BASE_URL + ROOT_URL +"/api/global/getUserInhandCash";
							  		var datauser = {
							  				unitId : $scope.unitId,
							  				userId : $scope.userId,
							  				date : moment(new Date()).format('YYYY-MM-DD'),
							  		}
							  		
							  		GenericService.serviceAction("POST",URIuser,datauser).then(function(response){
							  			$scope.usersCashInHandList = [];
							  			if(response.data.status == "success"){
							  				$rootScope.stopLoader();					
							  				$scope.totalCash = 0;
									  			$scope.usersCashInHandList=response.data.listObject;
									  			var debitAmount = 0;
												var creditAmount = 0;
												angular.forEach($scope.usersCashInHandList,function(value,index) {
													
													debitAmount = debitAmount + parseFloat(value.debitAmount)
													creditAmount = creditAmount + parseFloat(value.creditAmount)
													
												});
												
												
												$scope.totalCash = parseFloat(parseFloat(debitAmount) - parseFloat(creditAmount)).toFixed(2);
							  			}
							  		});
							  		}
						  		
					
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
													if(response.data.listObject.length == 0){
														$scope.saveBtn=true;
													}
													angular
													.forEach(response.data.listObject,function(value,index) {
														var newArr = {};
														newArr.denominationId = value.denominationId;
														newArr.denomination = value.denomination;
														newArr.qty = 0;
														newArr.amount = 0;
														newArr.createdBy = $scope.userId,
														newArr.updatedBy = $scope.userId
														$scope.denominationList.push(newArr);
														});
												
														console.log(response.data.denominationList);
												}
											});
							}
							$scope.getDenominationList();
					
							
							$scope.getHospitalAccountList = function() {
								
								var data = {
										orgnizationId:$scope.organizationId,
										unitId:$scope.unitId,
										offset:0,
										noOfRecordsPerPage:50
										};
								
								var URI = BASE_URL + ROOT_URL + GETHOSPITALACCOUNTMASTERLIST;
								console.log("URI", URI);
								GenericService.serviceAction("POST", URI, data).then(function(response) {
									
									$scope.hospitalAccountList = [];
									
									angular.forEach( response.data.listObject,function(value,index) {
										
										if(value.status == 'A'){
											$scope.hospitalAccountList.push(value);
										}
										
									});
									
								});
							}
							
							$scope.getHospitalAccountList();
							
					
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
							
						
						//for date datepicker
							
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
							
							$scope.toDtpickerOptions = {
									formatYear : 'yyyy',
    								showWeeks : false,
    								maxDate:new Date()
							};
					
							$scope.userSecurity =function(){
								
								if($scope.cashTransactionTypeId==undefined || $scope.cashTransactionTypeId==null || $scope.cashTransactionTypeId=="")
								{
									growl.warning("Please select cash transaction type",
										{
											title : 'warning!'
										});
									return false;
								}
								
								if($scope.totalAmount ==undefined || $scope.totalAmount ==null || $scope.totalAmount =="" || $scope.totalAmount == 0 )
								{
									growl.warning("Please enter denomination ",
										{
											title : 'warning!'
										});
									return false;
								}
								
								
								if($scope.cashTransactionTypeId != 2){
									
									if($scope.hospitalAccountId==undefined || $scope.hospitalAccountId==null || $scope.hospitalAccountId=="")
									{
										growl.warning("Please select bank name ",
											{
												title : 'warning!'
											});
										return false;
									}
									if($scope.cashTransactionTypeId == 3){
										
										if($scope.totalAmount > $scope.totalCash){
											
											growl.warning("You can't transfer more cash than available cash. ",
													{
														title : 'Warning!'
													});
												return false;
										}else{
											$scope.saveCashInOut();
										}
										
									}else{
										$scope.saveCashInOut();
									}
									
									
								}else{
									
									if($scope.cashHandoverToId == undefined || $scope.cashHandoverToId == null || $scope.cashHandoverToId== "")
									{
										growl.warning("Please select handover to ",
											{
												title : 'warning!'
											});
										return false;
									}
									
									if($scope.cashHandoverToId == $scope.cashHandoverById)
									{
										growl.warning("Handover from and handover to should not be same",
											{
												title : 'warning!'
											});
										return false;
									}
									
									
									if($scope.cashTransactionTypeId == 2 || $scope.cashTransactionTypeId == 3){
										
										
										
										if($scope.totalAmount > $scope.totalCash){
											
											growl.warning("You can't transfer more cash than available cash. ",
													{
														title : 'Warning!'
													});
												return false;
										}
										
									}
									
									
									$("#saveCashOpening").modal("show");
									
								}
								
								
							}
							
							
							
							$scope.saveCashInOut = function(){
								
								if($scope.cashTransactionTypeId == 2){
									
									if($scope.handoverBySecurity==undefined || $scope.handoverBySecurity==null || $scope.handoverBySecurity=="")
									{
										growl.warning("Please enter handover from security ",
											{
												title : 'warning!'
											});
										return false;
									}
									
									if($scope.handoverToSecurity==undefined || $scope.handoverToSecurity==null || $scope.handoverToSecurity=="")
									{
										growl.warning("Please enter handover to security ",
											{
												title : 'warning!'
											});
										return false;
									}
								}
								$rootScope.startLoader();
							
										var data1 = {
												date:moment($scope.datepicker).format('DD-MM-YYYY HH:mm:ss'),
												cashHandoverToId: $scope.cashHandoverToId,
												cashHandoverById:$scope.cashHandoverById,
												remarks:$scope.remarks,
												organizationId: $scope.organizationId,
												unitId: $scope.unitId,
												cashHandoverAmount:$scope.totalAmount,
												createdDate:moment($scope.date).format('DD-MM-YYYY HH:mm:ss'),
												updatedDate:moment($scope.date).format('DD-MM-YYYY HH:mm:ss'),
												createdBy:$scope.userId,
												updatedBy:$scope.userId,
												status:"A",
												denominationDetailsMasterList : $scope.denominationList,
												statusId : 2 ,
												handoverByPassword : $scope.handoverBySecurity,
												handoverToPassword : $scope.handoverToSecurity,
												cashTransactionTypeId : $scope.cashTransactionTypeId,
												hospitalAccountId : $scope.hospitalAccountId
											};
										
										var URI2 = BASE_URL + ROOT_URL+ "/api/global/saveCashInOut";
										
										GenericService.serviceAction("POST", URI2, data1).then(function(response) {
													
											if (response.data.status == 'success') {
												$rootScope.stopLoader();
												$scope.denominationList = [];
												$scope.totalAmount=0;
												$scope.remarks="";
												$scope.handoverBySecurity=null;
												$scope.handoverToSecurity=null;
												$scope.getDenominationList();
												$scope.getCashhandoverList();
												growl.success(response.data.message,
												{
													title : 'Success!'
												});
																			
												$('#saveCashOpening').modal('hide');
												$('#cashOpening').modal('hide');
											} else{
												$rootScope.stopLoader();
												growl.error(response.data.message,
														{
															title : 'Error!'
														});
											}
																		
															
										});
								
							
								
							}
							
							$scope.CashhandoverList = [];
							$scope.getCashhandoverList = function()
							{
								$rootScope.startLoader();
								
								var cashdata = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId,
										fromDate: moment($scope.fromDatepicker).format('YYYY-MM-DD'),
										toDate: moment($scope.toDatepicker).format('YYYY-MM-DD'),	
										cashHandoverById : ($scope.handoverfromUser==undefined || $scope.handoverfromUser=="") ? 0 :$scope.handoverfromUser, 
										cashHandoverToId : ($scope.handoverToUser==undefined || $scope.handoverToUser=="") ? 0 :$scope.handoverToUser,
										cobNumber:$scope.cobNumber	
								};
								var URICASH = BASE_URL + ROOT_URL+ "/api/global/getcashInOutList";
									GenericService
									.serviceAction(METHOD_POST, URICASH, cashdata)
									.then(
											function(response) {
												$scope.CashhandoverList = [];
												$scope.pagination.data = [];
												if (response.data.status == "success") {
													$rootScope.stopLoader();
													$scope.CashhandoverList = response.data.listObject;
													
													if($scope.CashhandoverList.length > 0){            
							                            PaginationService.searchData($scope.CashhandoverList , $scope.pagination);
							                          }
													
													console.log($scope.CashhandoverList);
												}
												$rootScope.stopLoader();
											});
							}
							
							$scope.getCashhandoverList();
							
						
							//to hide window
							$scope.hideWindow = function(){
								
								$('#detailsCashOpening').modal('hide');
							}
							
							$scope.cancelCashOpening = function(){
								$scope.denominationList = [];
								$scope.totalAmount=0;
								$scope.remarks="";
								$scope.getDenominationList();
							}
							
							$scope.cancelSecurity = function(){
								$scope.handoverBySecurity=null;
								$scope.handoverToSecurity=null;
							}
							
					
						$scope.cashOpening = function(){
							$scope.totalAmount = 0;
							$scope.getDenominationList();
							$scope.getUsersCashInHand();
							$("#cashOpening").modal("show");
						}
						
						
						
						$scope.changeType = function(){
							
							if($scope.cashTransactionTypeId != 2){
								$scope.cashHandoverToId = $scope.userId;
							}else{
								$scope.cashHandoverToId = null;
								$scope.hospitalAccountId = null;
							}
							
							
						}
						
						$scope.updateStatus = function(id) {
							
							var data = {
										id:id,
									};
							
							var URI = BASE_URL + ROOT_URL + "/api/global/inActiveAndActiveEntry";
							console.log("URI", URI);
							GenericService.serviceAction("POST", URI, data).then(function(response) {
								
								
								if (response.data.status == 'success') {
									$rootScope.stopLoader();
									$scope.getCashhandoverList();
									growl.success("InActive sucsessfully",
									{
										title : 'Success!'
									});
																
								} else{
									$rootScope.stopLoader();
									growl.error(response.data.message,
											{
												title : 'Error!'
											});
								}
											
								
							});
						}
							
			}
function config($stateProvider) {
    $stateProvider.state('cashInOut', {
		 url: '/cashInOut',
		 templateUrl: 'views/master/UnitMaster/billingMaster/IPDCharge/cash-in-out.html',
		 controller: "cashInOutController",
		 title: 'Cash In Out',
		 resolve : {}
	})
  }

  angular
    .module("myApp")
    .config(config)
    .controller("cashInOutController", cashInOutController);
})();
							