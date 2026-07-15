angular
		.module('myApp')
		.controller(
				'cashOpeningBalanceController',
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
							
							$scope.toDatepicker = new Date();
							$scope.fromDatepicker = new Date();
							$scope.datepicker = new Date();
							$scope.searchWord="";
							
							$scope.saveBtnFlag =true;
							$scope.updateBtnFlag =false;
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
														createdBy = $scope.userId,
														updatedBy = $scope.userId
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
							$scope.saveBtn=false;
					
						//for user validation
							$scope.userSecurity =function(){
								$rootScope.startLoader();
								if($scope.handoverTo=="" || $scope.handoverTo==null)
									{
									$rootScope.stopLoader();
									growl.warning("Please select Handover To",
											{
												title : 'warning!'
											});
									return false;
									}
								$scope.id =null;
								$scope.cobNumber = null;
								if($scope.editObj.id!=null && $scope.editObj.id!=undefined){
									$scope.id = $scope.editObj.id;
									$scope.cobNumber = $scope.editObj.cobNumber;
								}
								
								if($scope.tempDenominationList.length>0){
									$scope.denominationList = $scope.denominationList.concat($scope.tempDenominationList);
								}
										$scope.saveBtn=true;
										var data1 = {
												id : $scope.id,
												date:moment($scope.datepicker).format('DD-MM-YYYY HH:mm:ss'),
												cobNumber:$scope.cobNumber ,
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
												status:"A",
												denominationDetailsMasterList : $scope.denominationList,
												statusId : 1 //for pending
											};
										
										var URI2 = BASE_URL + ROOT_URL
										+ "/api/global/saveCashHandoverMaster";
										
										GenericService.serviceAction("POST", URI2, data1).then(function(response) {
													
											if (response.data.status == 'success') {
												$rootScope.stopLoader();
												$scope.saveBtn=false;
												$scope.handoverTo = undefined;
												$scope.denominationList = [];
												$scope.totalAmount=0;
												$scope.remarks="";
												$scope.handoverBySecurity="";
												$scope.handoverToSecurity="";
												$scope.getDenominationList();
												$scope.getCashhandoverList();
												growl.success(response.data.message,
												{
													title : 'Success!'
												});
												
												$scope.saveBtnFlag =true;
												$scope.updateBtnFlag =false;
																			
												$('#saveCashOpening').modal('hide');
												$('#cashOpening').modal('hide');
											} else{
												$rootScope.stopLoader();
												$scope.saveBtn=false;
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
										cashHandoverById : $scope.userId,
										cashHandoverToId : ($scope.handoverToUser==undefined || $scope.handoverToUser=="") ? 0 :$scope.handoverToUser,
										cobNumber:$scope.cobNumber	
								};
								var URICASH = BASE_URL + ROOT_URL+ "/api/global/getcashOpeningTransactionList";
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
							
							$scope.getalldenomination = function(info)
							{
								var dataDetails={
										
										createdDate: moment(info.createdDate).format('YYYY-DD-MM'),
										cashHandoverTransactionId:info.id
								}
								
								var URIDENOMI = BASE_URL + ROOT_URL+"/api/global/getListOfDenominationDetails";
								return GenericService.serviceAction("POST", URIDENOMI,dataDetails)
								
								.then(function(response) {
									
									$scope.alldenominationlist = [];
                                      if (response.data.status == "success"){
                                    	  
                                    	  $scope.alldenominationlist=response.data.listObject;
                                    	  
                                    	  console.log($scope.alldenominationlist);
                                    	 
                                      }
                                      $scope.cdate=$scope.alldenominationlist[0].createdDate;
                                 	 $scope.uname= $scope.alldenominationlist[0].userName;
                                 	 $scope.camount= $scope.alldenominationlist[0].cashHandoverAmount;
                                 	 $scope.remark= $scope.alldenominationlist[0].remarks;
                                 	 $scope.userName = info.handoverToUser;
                                 	 
                                 	return response;
                                      });
							
							}
							
							//to hide window
							$scope.hideWindow = function(){
								
								$('#detailsCashOpening').modal('hide');
							}
							
							$scope.cancelCashOpening = function(){
								$scope.saveBtn=false;
								$scope.denominationList = [];
								$scope.totalAmount=0;
								$scope.remarks="";
								$scope.handoverTo = undefined;
								$scope.getDenominationList();
								$scope.handoverBySecurity="";
								$scope.handoverToSecurity="";
							}
							
							$scope.cancelSecurity = function(){
								$scope.saveBtn=false;
								$scope.handoverBySecurity="";
								$scope.handoverToSecurity="";
							}
							
							$scope.printShiftOpening=function(id)
							{
								
									var data =  {
											 "appName": "HIS",
							                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
							                 "reportDesignFilePath": "get_Cash_Handover.rpt",
							                 "reportFormat": "PDF",
							                 "exportFileName": "ShiftOpening",
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
						$scope.cashHanoverObj = {};
						$scope.getcountofcheck = function(check,index){
								if($scope.CashhandoverList[index].statusId!=2){
								if(check){
									$scope.counter += 1;
									$scope.cashHanoverObj.id = $scope.CashhandoverList[index].id;
									$scope.cashHanoverObj.cashHandoverToId = $scope.CashhandoverList[index].cashHandoverToId;
								}else{
									$scope.counter -= 1;
								}
								}
						}
						
						$scope.updateCashOpeningStatus = function(){
						
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
							//$scope.cashHanoverObj.id = $scope.CashhandoverList[index].id;
							$scope.cashHanoverObj.statusId = 2;
							$scope.cashHanoverObj.handoverByPassword = $scope.handoverBySecurity;
							$scope.cashHanoverObj.handoverToPassword = $scope.handoverToSecurity;
							$scope.cashHanoverObj.updatedDate =  moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
							$scope.cashHanoverObj.updatedBy = $scope.userId;
							$scope.cashHanoverObj.cashHandoverById = $scope.userId;
							$scope.cashHanoverObj.unitId = $scope.unitId;
							$scope.cashHanoverObj.organizationId = $scope.organizationId;
							if($scope.cashHanoverObj.id !=undefined && $scope.cashHanoverObj.id!=null){
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/api/global/updateStatusToComplete";
								
									GenericService.serviceAction("POST",URI,$scope.cashHanoverObj).then(function(response){
										
										if (response.data.status == 'success')
										{
											$rootScope.stopLoader();
											$scope.getCashhandoverList();
											$scope.handoverBySecurity="";
											 $scope.handoverToSecurity="";
											 $scope.cashHanoverObj = {};
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
							var promise =  $scope.getalldenomination(info);
							promise.then(function(response) {
								$scope.handoverTo = info.cashHandoverToId;
								$scope.remarks = info.remarks;
								$scope.totalAmount = info.cashHandoverAmount;
								$scope.editObj.id = info.id;
								$scope.editObj.cobNumber = info.cobNumber; 
								$scope.tempDenominationList = angular.copy($scope.alldenominationlist);
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
						
						}
					
						$scope.cashOpening = function(){
							$scope.saveBtnFlag =true;
							$scope.updateBtnFlag = false;	
							$scope.handoverTo = "";
							$scope.remarks = "";
							$scope.totalAmount = "";
							$scope.getDenominationList();
							$("#cashOpening").modal("show");
						}
							
			}]);
							