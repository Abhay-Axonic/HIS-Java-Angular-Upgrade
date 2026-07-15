/**
 * @author Ankit Landge
 */

angular
		.module('myApp')
		.controller(
				"fundInflowController",
				[	    	'$scope',
							'$http',
							'$localStorage',
							'$sessionStorage',
							'$cookies',
							'$rootScope',
							'GenericService',
							'PagerService',
							'promiseFactory',
							'BillingGenericService',
							'GenericServiceFileUpload',
							'reportService',
							'growl',
							'$state',
							'GenericServiceParamHeader',
							'$stateParams',
					function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService,promiseFactory,PagerService,
								BillingGenericService,GenericServiceFileUpload,reportService, growl, $state,GenericServiceParamHeader,
								$stateParams,$modalInstance) {

							$rootScope.loginpage = true;
						    $scope.saveBtnFlag = true;
							$scope.updateBtnFlag = false;
							//$scope.setPaymentModeId(1);
							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.commonList = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;
							
							

							$scope.visitTypeList = [ {
								id : 1,
								label : "OP"
							}, {
								id : 2,
								label : "IP"
							}, {
								id : 3,
								label : "Day Care"
							}, {
								id : 4,
								label : "ER"
							} ];
							$scope.selectedVisitType = [];
							$scope.patientList =[];
							$scope.patientDetails = {};
							 
							/** * Check user is Loggedin */
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$scope.unitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							console.info($scope.orgId)
								var data = {};
							//$(".selectedPageName").text("Machine Master");
							//Date For From Date on Search
							$scope.initCommon = function() {
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
								$scope.selectedtrustFundTypeId="";
								$scope.trustObj = {
									"trustId" : '',
									"trustName" : '',
									"trustAddress" : '',
									"countryId" :'',
									"stateId":'',
									"districtId" : '',
									"cityId" : '',
									"areaId" : '',
									"zipCode" : '',
									"contactPersonName" :'',
									"contactPersonEmail":'',
									"phoneNo" : '',
									"cellNo" : '',
									"validFrom" : '',
									"patientCategoryId" : '',
									"patientTypeId" : '',
									"disease" :'',
									"maxLimitPerPerson":'',
									"trustStatus" : "A",
									"createdBy" : $scope.userId,
									"createdDate" : "",
									"updatedBy" : $scope.userId,
									"updatedDate" : "",
									"orgId" : $scope.orgId,
									"unitId" : $scope.unitId,
									"toDate" :'',
									"fromDate" :'',
									"fundRemark" : '',
									"patientId" : '',
									"visitTypeId":[],
								}
							};
							$scope.initCommon();
							
							
							$scope.openDatePickerFromDate = function() {
								$scope.datepickerFromDate.opened = true;
							};

							$scope.datepickerFromDate = {
								opened : false
							};
							$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,
									maxDate : new Date()
								};
							$scope.dateOptions1 = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : new Date()
								};
							
							$scope.openDatePicker = function() { 
								$scope.datepicker.opened = true;
							};
							$scope.openDatePickerSearch = function() {
								$scope.datepicker2.opened = true;
							};
							
							$scope.datepicker2 = {
									opened : false
								};
							
							
							//Date For To Date on Search
							var visitList=[];
							angular.forEach($scope.paymentModeId,
									function(value,index) {
								     visitList.push(value.id);
									});
							
							// all dropdown fill
							$scope.getCountryList = function() {
							
									var data = {};
									
									var URI = BASE_URL + ROOT_URL
									+ GETACTIVECOUNTRYLIST+$scope.orgId;								
							BillingGenericService
							.serviceAction(METHOD_GET, URI, data,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
									
										$scope.countryList = [];
										if (response.data.status == "success")
											$scope.countryList = response.data.listObject;
									
									});
								}
						      $scope.getCountryList();
						      
						      
						      //
						    //get calling code by using country id start
								/*$scope.getCallingCode=function(data){
									var URI = BASE_URL + ROOT_URL
									+ GETSINGLECOUNTRY;
									GenericService.serviceAction("POST", URI, data).then(
											function(response) {
												if (response.data.status == "success") {
													//$scope.trustObj.countryCallingCode = response.data.listObject[0].countryCallingCode;

												} else {
													alert("Error!!");
												}
											});
								}*/
								//get calling code by using country id end
							
								$scope.getNationalityByContryId=function(data){
									data.organizationId = $scope.organizationId;
									var URI = BASE_URL + ROOT_URL
									+ "/adt/getNationalityByCountryId";
									GenericService.serviceAction("POST", URI, data).then(
											function(response) {
												if (response.data.status == "success") {
													console.log("nationality List ",response.data.listObject[0]);
												

												} else {
										}
											});
								}
								//get nationality code by using country id end
						  	$scope.getStateByCountry = function(id) {
								var data = {
									countryId : id
								};								
								
								var URI = BASE_URL + ROOT_URL
										+ GETSTATELISTBYCOUNTRYID;
								BillingGenericService
								.serviceAction(METHOD_POST, URI, data,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											//$scope.stateList = [];
											if (response.data.status == "success"){
												
													$scope.stateList = [];
													$scope.stateList = response.data.listObject;
													
											}
										});


							}
						  	//get discrict
						  	$scope.getDistrictByState = function(id,type) {
								var data = {
									stateId : id
								};
								var URI = BASE_URL + ROOT_URL
										+ GETDISTRICTLISTBYSTATEID;
								BillingGenericService
								.serviceAction(METHOD_POST, URI, data,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											
											if (response.data.status == "success")
											{
												
												$scope.districtList = [];
												$scope.districtList = response.data.listObject;
												
												
											}
										});
								

							}
						  	$scope.getCityByDistrict = function(id,type) {
                             
								var data = {
									districtId : id
								};
								var URI = BASE_URL + ROOT_URL
										+ GETCITYLISTBYDISTRICTID;
								BillingGenericService
								.serviceAction(METHOD_POST, URI, data,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											
											if (response.data.status == "success")
												//$scope.cityList = response.data.listObject;
											{
												//$scope.districtList = response.data.listObject;
												
												 $scope.cityList = [];
												 $scope.cityList = response.data.listObject;
												
												
											}
										});

							}
						  	
							$scope.getAreaByCity = function(id,type) {
							
								var data = {
									cityId : id
								};
								var URI = BASE_URL + ROOT_URL
										+ GETAREALISTBYCITY;
								BillingGenericService
								.serviceAction(METHOD_POST, URI, data,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											
											if (response.data.status == "success")
											 $scope.areaList = response.data.listObject;
											console.log( $scope.areaList)
											{
											
												$scope.areaList = [];
												 $scope.areaList = response.data.listObject;
												 console.log( $scope.areaList)
												
												
											}
										});

							}
							$scope.gethideshowBtn = function(id){
								if(id==1 || id==2){
									$("#add_daignosis_modal_Btn").css("display","none");
									$("#approve_Request_Details_modal_Btn").css("display","none");
								}
								if(id==3){
									$("#add_daignosis_modal_Btn").css("display","block");
									$("#approve_Request_Details_modal_Btn").css("display","none");
								}
								if(id==4){
									$("#add_daignosis_modal_Btn").css("display","none");
									$("#approve_Request_Details_modal_Btn").css("display","block");
								}
								
							}
					$scope.getpatientCategory = function() {
							
							var URI = BASE_URL + ROOT_URL
							+ GETACTIVEPATIENTCATEGORY;
					GenericService
							.serviceAction("GET", URI, data)
							.then(
									function(response) {
										$scope.patientCategoryList = [];

										if (response.data.status == "success") {
											$scope.patientCategoryList = response.data.listObject;
											console.log($scope.patientCategoryList);

										}
									});
					}
					$scope.getpatientCategory();
					
					
					//save function
					
					
					/* START FORFOR MACHINE MASTER */
				
					$scope.setNoOfRecords = function() {
						$scope.initTrustMasterList($scope.orgId,
								$scope.offset,
								$scope.noOfRecordsPerPage);
					};
					
					
					$scope.getTrustActiveList = function() {

						var URI = BASE_URL+ ROOT_URL+ "/api/billing/getActiveTrustList";
						var data = {
                                      orgId : $scope.orgId ,
								      unitId:$scope.unitId
								    
								   }
						GenericService.serviceAction("POST", URI, data).then(function(response) {

									$rootScope.stopLoader();
									$scope.getTrustActiveList = [];
									$scope.getTrustActiveList = response.data.listObject;
	                                
	                               

								});
					}
					$scope.getTrustActiveList();
					
					
					$scope.getPatientClassList = function() {
			        var URI = BASE_URL + ROOT_URL +"/api/billing/getPatientType/"+$scope.orgId +"/"+$scope.unitId;
			    GenericService
			        .serviceAction(METHOD_GET, URI,
			                NOTIFICATION_MSG_STATUS_FALSE)
			        .then(
			                function(response) {
			                    console.log(response);
			                    if (response.data.status == "success"){
			                        
			                        $scope.patientTypeList = response.data.listObject;
			                        console.log("list",$scope.patientTypeList);
			                    }
			                    
			                });

					}
					$scope.getPatientClassList();
					
							//Get trust List
						$scope.getTrustList = function() {
				        var URI = BASE_URL + ROOT_URL +"/api/billing/getTrustList/"+$scope.orgId +"/"+$scope.unitId;
				    GenericService
				        .serviceAction(METHOD_GET, URI,
				                NOTIFICATION_MSG_STATUS_FALSE)
				        .then(
				                function(response) {
				                    console.log(response);
				                    if (response.data.status == "success"){
				                        $scope.trustList = response.data.listObject;
				                        console.log("list",$scope.trustList);
				                    }
				                    
				                });

						}
						$scope.getTrustList();
						
						$scope.getTrustInflowDetailsByTrustId = function(id) {
							$rootScope.startLoader();
							var data = {
								  trustId : id,
								  orgId : $scope.orgId ,
							      unitId:$scope.unitId
							};
							 var URI = BASE_URL + ROOT_URL +"/api/billing/getAllfundInflowDetailsByTrust";
							BillingGenericService
							.serviceAction(METHOD_POST, URI, data,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
					                    console.log(response);
					                    if (response.data.status == "success"){
					                    	$rootScope.stopLoader();
					                    	$scope.commonTrustInflowlist=[];
					                    	$scope.commonTrustInflowlist=response.data.listObject;
					                    }
					                });
								
								
						}
						
						$scope.getTrustInflowAllocatedDetails =function (infoObj){
							$rootScope.startLoaderPopup();
							var data = {
									fundInFlowId : infoObj.fundInFlowId,
								  orgId : $scope.orgId ,
							      unitId:$scope.unitId
							};
							 var URI = BASE_URL + ROOT_URL +"/api/billing/getInflowAcceptedRequest";
							BillingGenericService
							.serviceAction(METHOD_POST, URI, data,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
					                    console.log(response);
					                    
					                    if (response.data.status == "success"){
					                    	$("#allocated_Request_Details_modal").modal('show');
					                    	$rootScope.stopLoaderPopup();
					                    	
					                    	$scope.getInflowAcceptedRequestList=[];
					                    	$scope.getInflowAcceptedRequestList=response.data.listObject;
					                    }
					                });
							
							
							
						}
						
						
						$scope.getTrustInflowVirtualAcceptanceDetails =function (infoObj){
							$rootScope.startLoaderPopup();
							var data = {
									fundInFlowId : infoObj.fundInFlowId,
								  orgId : $scope.orgId ,
							      unitId:$scope.unitId
							};
							 var URI = BASE_URL + ROOT_URL +"/api/billing/getVirtualRequestPayments";
							BillingGenericService
							.serviceAction(METHOD_POST, URI, data,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
					                    console.log(response);
					                    
					                    if (response.data.status == "success"){
					                    	$("#Virtual_Acceptance_Details_modal").modal('show');
					                    	$rootScope.stopLoaderPopup();
					                    	
					                    	$scope.getInflowVirtualAcceptedDetialsList=[];
					                    	$scope.getInflowVirtualAcceptedDetialsList=response.data.listObject;
					                    }
					                });
							
							
							
						}
						
		
						// to and from date picker
						$scope.trustObj.toDate= new Date();
						$scope.trustObj.fromDate= new Date();
						$scope.openDatePicker1 = function() { 
							$scope.datepicker1.opened = true;
						};
						$scope.datepicker1 = {
								opened : false
							};
						$scope.openDatePicker2 = function() { 
							$scope.datepicker2.opened = true;
						};
						$scope.datepicker2 = {
								opened : false
							};
						
						$scope.openDatePicker3 = function() { 
							$scope.datepicker3.opened = true;
						};
						$scope.datepicker3 = {
								opened : false
							};
						var trustId="";
						$scope.getTrustDetails = function(id) {
							  $scope.getTrustInflowDetailsByTrustId(id);
							  $scope.trustObj.trustId=id;
								
								
						}
						
						
						$scope.getTrustInflowPaymentDetails = function(Inflowobj){
							
							$rootScope.startLoaderPopup();
							var data = {
									fundInFlowId : Inflowobj.fundInFlowId,
								  orgId : $scope.orgId ,
							      unitId:$scope.unitId
							};
							 var URI = BASE_URL + ROOT_URL +"/api/billing/getInflowDetailsByFundInflowId";
							BillingGenericService
							.serviceAction(METHOD_POST, URI, data,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
					                    console.log(response);
					                    
					                    if (response.data.status == "success"){
					                    	$("#fundInflowDetailsModal").modal('show');
					                    	$rootScope.stopLoaderPopup();
					                    	
					                    	$scope.paymentDetails=[];
					                    	$scope.paymentDetails=response.data.listObject;
					                    }
					                });
							
						}
						
						$scope.collectionList = function(toDate,fromDate,trustId) {
							if(toDate==undefined || fromDate ==undefined ){
								$scope.trustObj.toDate= new Date();
								$scope.trustObj.fromDate= new Date();
							}
							    var dateTo= moment(toDate).format('YYYY-MM-DD');
								var dateFrom=  moment(fromDate).format('YYYY-MM-DD');
					
							var data={
								toDate:dateTo,
								fromDate:dateFrom,
								orgId:$scope.orgId,
								unitId:$scope.unitId,
								trustId:trustId			
							};			
				
				        var URI = BASE_URL + ROOT_URL +"/api/billing/getCollectionList/"+$scope.orgId +"/"+$scope.unitId;
				    GenericService
				        .serviceAction(METHOD_POST, URI,
				                data)
				        .then(
				                function(response) {
				                    if (response.data.status == "success"){
				                        $scope.CollectList = response.data.listObject;
				                        console.log("list",$scope.CollectList);
				                    }
				                   
				                });

					
						}
                    	$scope.collectionList(); 
						//-----------------payment collection Start---------------//
						$scope.getBankList = function(){
							var obj={
									"unitId":$scope.unitId,
									"orgnisationId" :  $scope.orgId
							}
							$rootScope.startLoader();
							var URI = BASE_URL + ROOT_URL + API + BILLING + "/bankMaster";
							BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
								if(response.data.status == "success"){
									$scope.bankList = response.data.listObject;
									console.log($scope.bankList)
								}
							});
						}
						
						$scope.getBankList();
						$scope.getHospitalBankList = function()
						{
							var URI = BASE_URL + ROOT_URL + "/api/billing/hospitalBank/"+$scope.orgId+"/"+$scope.unitId;
							BillingGenericService.serviceAction(METHOD_GET,URI,{},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
						       {
								if (response.data.status == "success")
								{
									$scope.hospitalBankList = response.data.listObject;
								}
							  });
						}
						$scope.getHospitalBankList();
						
						$scope.getCardTypesList = function(){
							var URI = BASE_URL + ROOT_URL + "/api/billing/cardTypeMaster";
							var param = {
									orgnisationId:$scope.orgId,
									unitId:$scope.unitId
									};
							BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
								if (response.data.status == "success"){
									$scope.cardTypeList = response.data.listObject;
								}
							});
						}
						$scope.getCardTypesList();
						
						$scope.transactionTypesList=[];
						 $scope.getTransactionTypes = function(){
							var URI = BASE_URL + ROOT_URL + API + BILLING + "/transactiontypes/"+$scope.orgId+"/"+$scope.unitId;
							BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
								if(response.data.status == "success"){
									$scope.transactionTypesList = response.data.listObject;
								}
								
								console.log("transactionTypesList",$scope.transactionTypesList);
							});
						}
						 $scope.getTransactionTypes ();
						 
						$scope.initPaymentDetails=function(){
							$scope.paymentDetails={
								    "paymentModeId"			:[],
								    "bankId"				:"",
									"accountNumber"			:"",
									"chequeNumber"			:"",
									"cardNo"				:"",
									"cardHolderName"		:"",
									"dateOfTransaction"		:new Date().getTime(),
									"expiryDate"			:"",
									"unitId"				:$scope.unitId,
									"orgId"			        :$scope.organizationId,
									"status"				:"A",
									"createdDate"			:new Date().getTime(),
									"createdBy"				:$scope.userId,
									"updatedDate"			:new Date().getTime(),
									"updatedBy"				:$scope.userId,
									"chequeDate"			:"",
									"cardTypeId"			:"",
									"isCancle"				:"I",
								    "otp"	                 :'',
								    'transactionNo'         : ""
									
							}
							
							$scope.patientSearchObj = {
									"uhIdNumber"      : "",
									"patientName"     : "",
									"genderId"        : "",
									"mobileNo"        : "",
									"birthDate"       : "",
									"identificationNo":"",
									"patientType"	  :"1",
									"unitId"          :$scope.unitId,
									"organizationId"  :$scope.organizationId
							}
							
						};
						$scope.initPaymentDetails();
						
						$scope.paymentModes = {
								1: "Cash",
								2: "Cheque",
								3: "Credit Card",
								8: "E - Transfer",
								5: "Loyalty Card"	
						}
						
						$scope.setPaymentModeId=function(paymentModeId){
							console.log(paymentModeId)
							$scope.paymentDetails.paymentModeId=paymentModeId;
							$scope.firstTabLevel = paymentModeId;
							$scope.isSetFirstTabLevel(paymentModeId);
							
							//$scope.paymentDetails.paymentModeName=$scope.paymentModes[paymentModeId];
						}
						
						$scope.firstTabLevel = 1 ;
						
						$scope.isSetFirstTabLevel = function(tabNum)
						{
							return $scope.firstTabLevel === tabNum;
						};
						
						
						$scope.setFirstTabLevel= function(newTab)
						{
							$scope.paymentDetails.paymentModeId = newTab;
							$scope.firstTabLevel = newTab;
						};
						
						$scope.setBankName=function(bankId){
							angular.forEach($scope.bankList, function (value, key) {
								if(value.bankId == bankId){
									$scope.paymentDetails.bankName = value.bankName;
									return;
								}
							});
							
						}
						$scope.initPaymentDetailsObj = function(){
							$scope.paymentDetails = angular.copy({paymentModeId:1,cashAmount:"",bankId:"",chequeNumber:"",chequeDate:"",chequeAmount:"",credCardHolderName:"",credCardTypeId:"",credAmount:"",credCardNumber:"",credExpiryDate:"",debCardHolderName:"",debCardTypeId:"",debAmount:"",debCardNumber:"",debExpiryDate:"",hospitalAccountId:"",cardBalAmount:$scope.paymentDetails.cardBalAmount,loyaltyCardNo:$scope.paymentDetails.loyaltyCardNo,loyaltyCardAmt:""});
							$scope.eTransferObj={};
						}
						
						/*var payId=[];
						angular.forEach($scope.paymentModeId,
								function(value,index) {
							    payId.push(value.id);
								});*/
						//billing fatory
						      $scope.listPaymentDetails = [];
							$scope.addPaymentDetails = function(scope){
								var amount=0;
								angular.forEach($scope.listPaymentDetails,function(value,key){
									console.log(value)
									console.log(value.amount)
									amount = amount + parseFloat(value.amount); 
									console.log(amount)
								});
								
							switch($scope.paymentDetails.paymentModeId){
									case 1 :
										console.log($scope.paymentDetails.cashAmount)
										if(typeof($scope.paymentDetails.cashAmount) == "undefined" || $scope.paymentDetails.cashAmount == "" || $scope.paymentDetails.cashAmount == parseFloat(0) || $scope.paymentDetails.cashAmount < parseFloat(0)){
											growl.error("Please insert an amount",{
												title : ERROR_MSG
											});
											return;
										}
										
										if($scope.paymentDetails.cashAmount!= null)
											{
											var dueamt= $scope.fundAmount - $scope.finalAmount -$scope.paymentDetails.cashAmount;
											   
											if(dueamt<0){
												   growl.error("Please insert valid amount",{
														title : ERROR_MSG
													});
												    $scope.paymentDetails.cashAmount= ($scope.fundAmount-$scope.finalAmount);
													$scope.paymentDetails.chequeAmount=($scope.fundAmount-$scope.finalAmount);
													$scope.paymentDetails.credAmount=($scope.fundAmount-$scope.finalAmount);
													$scope.paymentDetails.transferAmount=($scope.fundAmount-$scope.finalAmount);
												   return;
											   }
											 var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.paymentDetails.cashAmount).toFixed(2)};
											   $scope.listPaymentDetails.push(paymentDetails);
											   console.log($scope.listPaymentDetails)
											   
											    
											}
										break;
									case 2 :
										if(typeof($scope.paymentDetails.chequeAmount) == "undefined" || $scope.paymentDetails.chequeAmount == "" || $scope.paymentDetails.bankId == 0 || $scope.paymentDetails.chequeNumber == "" || typeof($scope.paymentDetails.chequeNumber) == "undefined" || typeof($scope.paymentDetails.chequeDate) == "undefined" || $scope.paymentDetails.chequeDate == "" || $scope.paymentDetails.chequeAmount == parseFloat(0) || $scope.paymentDetails.chequeAmount < parseFloat(0)){
											growl.error("Please select bank, chequeno, cheque date and amount",{
												title : ERROR_MSG
											});
											return;
										}
										
										var bankName = "";
										//Get Bank Name
										angular.forEach($scope.bankList,function(value,key){
											if($scope.paymentDetails.bankId == value.bankId)
												bankName = value.bankName;
										})
										
										if($scope.paymentDetails.chequeAmount!=null){
											
											var dueamt= $scope.fundAmount - $scope.finalAmount -$scope.paymentDetails.chequeAmount;
											   
											if(dueamt<0){
												   growl.error("Please insert valid amount",{
														title : ERROR_MSG
													});
												   $scope.paymentDetails=[];
												    $scope.paymentDetails.cashAmount= ($scope.fundAmount-$scope.finalAmount);
													$scope.paymentDetails.chequeAmount=($scope.fundAmount-$scope.finalAmount);
													$scope.paymentDetails.credAmount=($scope.fundAmount-$scope.finalAmount);
													$scope.paymentDetails.transferAmount=($scope.fundAmount-$scope.finalAmount);
												   return;
											   }
											var chequeDate = $rootScope.getFormattedDate($scope.paymentDetails.chequeDate,false);
											
											var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.paymentDetails.chequeAmount).toFixed(2),createdBy:$scope.userId, bankId:$scope.paymentDetails.bankId, chequeNumber:$scope.paymentDetails.chequeNumber, chequeDate:chequeDate,bankName:bankName};//, bankName:bankName
												$scope.listPaymentDetails.push(paymentDetails);
										}
										break;
									
									case 3 :
										if(typeof($scope.paymentDetails.credAmount) == "undefined" || $scope.paymentDetails.credAmount == "" ||$scope.paymentDetails.credAmount =="" ||$scope.paymentDetails.credCardNumber==undefined ||$scope.paymentDetails.credCardNumber==""){
											growl.error("Please select card number and amount",{
												title : ERROR_MSG
											});
											return;
										}
										if($scope.paymentDetails.credAmount!=null){
											var dueamt= $scope.fundAmount - $scope.finalAmount -$scope.paymentDetails.credAmount;
											   
											if(dueamt<0){
												   growl.error("Please insert valid amount",{
														title : ERROR_MSG
													});
												   $scope.paymentDetails=[];
												    $scope.paymentDetails.cashAmount= ($scope.fundAmount-$scope.finalAmount);
													$scope.paymentDetails.chequeAmount=($scope.fundAmount-$scope.finalAmount);
													$scope.paymentDetails.credAmount=($scope.fundAmount-$scope.finalAmount);
													$scope.paymentDetails.transferAmount=($scope.fundAmount-$scope.finalAmount);
													$scope.paymentDetails.paymentModeId=3;
												   return;
											   }
											
										var expiryDate =  $scope.getFormattedDate($scope.paymentDetails.credExpiryDate,false);
										var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.paymentDetails.credAmount).toFixed(2),createdBy:$scope.userId, cardHolderName:$scope.paymentDetails.credCardHolderName,cardTypeId:$scope.paymentDetails.credCardTypeId,cardNo:$scope.paymentDetails.credCardNumber,expiryDate:expiryDate,transactionNo:$scope.paymentDetails.transactionNo};
										$scope.listPaymentDetails.push(paymentDetails);
										$scope.paymentDetails.paymentModeId=3;
										}
										break;
										
									case 8 : 
										if(typeof($scope.paymentDetails.transactionobj==undefined||$scope.paymentDetails.transactionobj=="" ||$scope.paymentDetails.transferAmount) == "undefined" || $scope.paymentDetails.transferAmount == "" ||$scope.paymentDetails.transactionobj.id==0||$scope.paymentDetails.transactionNo ==0 || $scope.paymentDetails.transactionNo == undefined || $scope.paymentDetails.transBankId == ""|| $scope.paymentDetails.transBankId==0 ||$scope.paymentDetails.transactionNo==0||$scope.paymentDetails.transactionNo=="" || typeof($scope.paymentDetails.transTransactionDate) == "undefined" || $scope.paymentDetails.transTransactionDate == "" ){
											growl.error("Please select Name, card type,card number expiry date and amount",{
												title : ERROR_MSG
											});
											return;
										}
										
										var bankName = "";
										// Get Bank Name
										angular.forEach($scope.bankList,function(value,key){
											if($scope.paymentDetails.transBankId == value.bankId)
												bankName = value.bankName;
										})
										var dueamt= $scope.fundAmount - $scope.finalAmount -$scope.paymentDetails.transferAmount;
										   
										if(dueamt<0){
											   growl.error("Please insert valid amount",{
													title : ERROR_MSG
												});
											   $scope.paymentDetails=[];
											    $scope.paymentDetails.cashAmount= ($scope.fundAmount-$scope.finalAmount);
												$scope.paymentDetails.chequeAmount=($scope.fundAmount-$scope.finalAmount);
												$scope.paymentDetails.credAmount=($scope.fundAmount-$scope.finalAmount);
												$scope.paymentDetails.transferAmount=($scope.fundAmount-$scope.finalAmount);
											   return;
										   }	
									 var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.paymentDetails.transferAmount).toFixed(2), transactionType:$scope.paymentDetails.transactionobj.name, transactionTypeId:$scope.paymentDetails.transactionobj.id,transTransactionDate:moment($scope.paymentDetails.transTransactionDate).format('DD-MM-YYYY HH:mm:ss'),bankId:$scope.paymentDetails.transBankId,transactionNo:$scope.paymentDetails.transactionNo,createdBy:$scope.userId,bankName:bankName};
										   $scope.listPaymentDetails.push(paymentDetails);
										   console.log(paymentDetails)
										break;
										
									case 5:
										if($scope.mpesaObject.resTransactionId!=$scope.mpesaObject.transactionId){
											growl.warning("Invalid Transaction Id",{
												title : WARNING_MSG
											});
											$scope.mpesaObject.mpesaAmount=null;
											return;	
										}
										if($scope.mpesaObject.mpesaAmount == 0 || $scope.mpesaObject.mpesaAmount == null){
											growl.warning("Amount cannot be zero",{
												title : WARNING_MSG
											});
											return;
										}
										var invalidMpesa = false;
										angular.forEach($scope.listPaymentDetails,function(value,key){
											if($scope.mpesaObject.resTransactionId == value.cardNo)
											{
												growl.warning("Collected MPesa is already used in current tranasction",{
													title : WARNING_MSG
												});
												invalidMpesa = true;
											}
										});
										if(invalidMpesa == true)
											return;
										
										if($scope.isBillList == 0 || $scope.isBillList != undefined){
											if($scope.transferToActiveDepositFact == undefined || $scope.transferToActiveDepositFact == 'N'){
										if(parseFloat($scope.mpesaObject.mpesaAmount) <= parseFloat($scope.payeeDueAmtFinal))
										{
											var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.mpesaObject.mpesaAmount).toFixed(2),cardNo:$scope.mpesaObject.resTransactionId,transactionNo:$scope.mpesaObject.resTransactionId,createdBy:$scope.userId};
											   $scope.listPaymentDetails.push(paymentDetails);
										 }
										else {
											growl.error("Entered Amount Should not be greater than Self Due Amount",{
												title : ERROR_MSG
											});
											return 
										}
										}
											else
												{
												var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.mpesaObject.mpesaAmount).toFixed(2),cardNo:$scope.mpesaObject.transactionId,transactionNo:$scope.mpesaObject.transactionId,createdBy:$scope.userId};
												   $scope.listPaymentDetails.push(paymentDetails);
												}
										}
										$scope.mpesaObject={};
										break;
										
										
								}
								//$scope.initPaymentDetailsObj(angular.element(document.getElementById("uiView")).$scope());
								
								//calculate due amount by page
								switch($scope.page){
								case "billList":
									$scope.calculateTotalDueRefAmount();
									break;
								case "companyInvoiceSettlement":
									$scope.validCollectedAmount($scope.listPaymentDetails.length-1);
									break;
								}
								$scope.calculateTotalDueRefAmount();
								$scope.initPaymentDetailsObj();
								 $scope.setFirstTabLevel(1);
								$scope.setPaymentModeId(1);
								$scope.paymentDetails.cashAmount= ($scope.fundAmount-$scope.finalAmount);
								$scope.paymentDetails.chequeAmount=($scope.fundAmount-$scope.finalAmount);
								$scope.paymentDetails.credAmount=($scope.fundAmount-$scope.finalAmount);
								$scope.paymentDetails.transferAmount=($scope.fundAmount-$scope.finalAmount);
								
							}
							
							
							
							 // calculate ammount
							$scope.totalAmount;
							$scope.calculateTotalDueRefAmount = function()
							{
								$scope.totalAmount = 0;
								angular.forEach($scope.listPaymentDetails,function(value,key){
								
									$scope.totalAmount = $scope.totalAmount + parseFloat(value.amount); 
									$scope.finalAmount=$rootScope.parseFloatNum($scope.totalAmount);
								});
								
								console.log("final Amount ---"+$rootScope.parseFloatNum($scope.totalAmount))
								
									  
							};
							$scope.removePaymentDetails = function(paymentObj, index){
								$scope.totalAmount=$scope.totalAmount - parseFloat(paymentObj.amount);
								$scope.finalAmount=$rootScope.parseFloatNum($scope.totalAmount);
							    var tempObj = (index==undefined)?null:$scope.listPaymentDetails[index];
								if(tempObj!=null && (tempObj.cardNumber == $scope.paymentDetails.loyaltyCardNo))
									{
									 $scope.paymentDetails.cardBalAmount = $scope.paymentDetails.cardBalAmount +parseFloat(tempObj.amount);
									}
								$scope.listPaymentDetails.splice(index,1);
								
								$scope.paymentDetails.cashAmount= ($scope.fundAmount-$scope.finalAmount);
								$scope.paymentDetails.chequeAmount=($scope.fundAmount-$scope.finalAmount);
								$scope.paymentDetails.credAmount=($scope.fundAmount-$scope.finalAmount);
								$scope.paymentDetails.transferAmount=($scope.fundAmount-$scope.finalAmount);
							}
						
						
							
							$scope.temdiagnolist=[];
			     	$scope.collectAmount=function(id){
				             $scope.fundInFlowDetailsDto=[];
				             $scope.fundInFlowDetailsDto.fundInFlowDiagnosisDetails=[];
							 $scope.fundInFlowDetailsDto = $scope.listPaymentDetails;
							 var balaceamt=$scope.fundAmount - $scope.finalAmount;
							 var balaceflag=isNaN(balaceamt);
							 if(balaceamt >0 || balaceflag == true ){
								 growl.error("Please Collect valid amount",{
										title : ERROR_MSG
									});
									return; 
							 }
							 
							 if($scope.addDiagnosisList.length>0){
								 angular.forEach($scope.addDiagnosisList,function(value,index){
										$scope.temdiagnolist.push(value.diagnosisId);
								  });
								
								 $scope.fundInFlowDetailsDto.fundInFlowDiagnosisDetails=$scope.temdiagnolist;
							 }
								 
							 if(  $scope.selectedPatientId !=undefined ||  $scope.selectedPatientId !="" || $scope.selectedPatientId !=null ) {
							      $scope.fundInFlowDetailsDto.patientId=$scope.selectedPatientId;
							 }
							 if($scope.trustObj.fundRemark!=undefined || $scope.trustObj.fundRemark!="" ||$scope.trustObj.fundRemark!=null ) {
							      $scope.fundInFlowDetailsDto.fundRemark=$scope.trustObj.fundRemark;
							 }
						      angular.forEach($scope.fundInFlowDetailsDto,function(value,index){
						      value.trustId=id;
						      value.finalAmount=$scope.finalAmount;
						           
						      
						      value.orgId= $scope.orgId;
						      value.unitId=$scope.unitId;
						      value.createdBy=$scope.userId;
						      value.updatedBy=$scope.userId;
						      value.chequeDate=$scope.paymentDetails.chequeDate;
						      value.chequeAmount=$scope.paymentDetails.chequeAmount;
						      value.cashAmount=$scope.paymentDetails.cashAmount;
						      value.status='A'
						      value.createdDate=new Date();
						      value.updatedDate==new Date();
						     if($scope.selectedPatientId !=undefined || $scope.selectedPatientId !="" ||$scope.selectedPatientId!=null ) {
						    	 value.patientId= $scope.selectedPatientId;
						     }if($scope.trustObj.fundRemark!=undefined || $scope.trustObj.fundRemark!='' ||$scope.trustObj.fundRemark!=null ) {
						    	 value.fundRemark= $scope.trustObj.fundRemark;
						     }
						      value.transferAmount= $scope.paymentDetails.transferAmount;
						      value.createdBy  = $scope.paymentDetails.createdBy;
						      $scope.txDate = moment($scope.paymentDetails.transTransactionDate).format('DD-MM-YYYY  h:mm:ss ');
						      console.log( "$scope.txDate"  +$scope.txDate)
//						      value.transTransactionDate=  $scope.txDate;
//						      value.transactionNo= $scope.paymentDetails.transactionNo;
						      if($scope.paymentDetails.transactionobj!=null){
						    	  value.transactionTypeId = $scope.paymentDetails.transactionobj.id;  
						      }
			         
//						      value.cardAmount=$scope.paymentDetails.credAmount;
//						      value.cardHolderName= $scope.paymentDetails.credCardHolderName;
//						      value.cardNo =$scope.paymentDetails.credCardNumber;
//						      value.cardTypeId = $scope.paymentDetails.credCardTypeId;
//						      $scope.expiryDate =
//						      value.expiryDate = moment($scope.paymentDetails.credExpiryDate).format('DD-MM-YYYY  h:mm:ss ');
						      console.log($scope.paymentDetails)
//						      console.log(value)
						  
							   
						      });
						//return false;
						     $scope.$broadcast('show-errors-check-validity');
					              try {
					
										var URI = BASE_URL + ROOT_URL
										+ "/api/billing/saveFundDeatils";
										var data={};
										//for patient oriented AND Trust Oriented
										if($scope.selectedtrustFundTypeId==1 || $scope.selectedtrustFundTypeId==2){
											data={fundInFlowDetailsDto:$scope.fundInFlowDetailsDto,
													createdBy : $scope.userId,
													updatedBy : $scope.userId,
													fundtypeId: $scope.selectedtrustFundTypeId
											};
										}else if( $scope.selectedtrustFundTypeId==3){//agaist Diagnosis
											data={
													fundInFlowDetailsDto:$scope.fundInFlowDetailsDto,
													fundInFlowDiagnosisDetails :$scope.fundInFlowDetailsDto.fundInFlowDiagnosisDetails,
													createdBy : $scope.userId,
													updatedBy : $scope.userId,
													fundtypeId: $scope.selectedtrustFundTypeId
											};
										}else if($scope.selectedtrustFundTypeId==4){
											$scope.commonVitualTrustAccepted=[];
											angular.forEach($scope.commonVitualTrustAcceptanceList, function(value, key) { 
									   		     if(value.sanctionApprovedflagforCheck==true){
									   		    	 var dataobj={}
									   		    	dataobj.fundsanctionrequestApprovalDetailsId = value.fundsanctionrequestApprovalDetailsId
									   		    	dataobj.requestApprovalAmount  = value.requestApprovalAmount;
									   		    	dataobj.virtualAcceptancePaidAmmount=value.virtualAcceptancePaidAmmount;
									   		    	   $scope.commonVitualTrustAccepted.push(dataobj);
									   		      }
									   	     });
											
											data={
													fundInFlowDetailsDto : $scope.fundInFlowDetailsDto,
													fundSanctionAcceptanceRequestDto : $scope.commonVitualTrustAccepted,
													createdBy : $scope.userId,
													updatedBy : $scope.userId,
													fundtypeId: $scope.selectedtrustFundTypeId
											};
											
										}
												 
												
										$rootScope.startLoaderPopup();
										GenericService.serviceAction("POST", URI,
												data)
										.then(
												function(response) {
													if (response.data.status == 'success') {
														$rootScope.stopLoaderPopup();
												       $rootScope.stopLoader();
												       
												        $scope.getTrustDetails($scope.trustlistObj.trustId);
														$scope.paymentDetails=[];
														 $scope.listPaymentDetails = [];
														 $scope.trustlistObj.trustId="";
														 $scope.trustObj=[];
														 $scope.addDiagnosisList = [];
														 $scope.temdiagnolist=[];
														 $scope.fundInFlowDetailsDto.fundInFlowDiagnosisDetails=[];
														 $scope.finalAmount="";
														 $scope.fundAmount="";
														 $scope.selectedPatientId = "";
															$scope.trustObj.toDate= new Date();
															$scope.trustObj.fromDate= new Date();
															$scope.collectionList();
															$scope.selectedtrustFundTypeId="";
															$scope.finalCheckedAmount=0;
															$("#add_daignosis_modal_Btn").css("display","none");
															$("#approve_Request_Details_modal_Btn").css("display","none");
														growl.success("Fund Collected successfully",
																	{
																		title : 'Success!'
																	});
														$("#fundCollectPaymentclosebtn").trigger("click");
													$scope.$broadcast('show-errors-reset');
											
												} else if (response.data.status == 'error') {
													$rootScope.stopLoaderPopup();
													$scope.paymentDetails=[];
													 $scope.listPaymentDetails = [];
													 $scope.trustlistObj.trustId="";
													 $scope.trustObj=[];
													 $scope.addDiagnosisList = [];
													 $scope.temdiagnolist=[];
													 $scope.finalAmount="";
													 $scope.fundAmount="";
													 $scope.selectedPatientId = "";
													 $scope.fundInFlowDetailsDto.fundInFlowDiagnosisDetails=[];
													 $scope.trustObj.toDate= new Date();
													$scope.trustObj.fromDate= new Date();
													$scope.collectionList();
													$scope.selectedtrustFundTypeId="";
													$scope.finalCheckedAmount=0;
													$("#add_daignosis_modal_Btn").css("display","none");
													$("#approve_Request_Details_modal_Btn").css("display","none");
													growl.error(
																	response.data.message,
																	{
																		title : 'Error!'
																	});
													$("#fundCollectPaymentclosebtn").trigger("click");
												}

											});
									} catch (e) {
										console.log("Exception", e.message);
										$rootScope.stopLoaderPopup();
									}

					       }
			     	
			     	
			    	$scope.showFieldList = function(id){
						try{
							$scope.fieldMasterList=[];
							$scope.saveBtnFlag = false;
							$scope.updateScope = true;
							$scope.updateBtnFlag = true;
							var data={};
							var URI = BASE_URL + ROOT_URL + "/api/billing/getVisitType" +"/"+ id +"/"+ $scope.orgId +"/"+$scope.unitId;
							
							GenericService.serviceAction("GET",URI,data).then(function(response){
								
								if(response.data.status == "success"){
		 							$scope.fieldMasterList= response.data.listObject;
								}else if(response.data.status == 'error'){
									$rootScope.stopLoader();
									growl.error(response.data.message,
											{
												title : 'Error!'
											});
								}
							});
							
						}catch(e){
							console.log("Exception-",e);
						}
					}
			    	
			    	
			   
			    	$scope.searchPatient = function(patientsearchObj){
						
						if(patientsearchObj.uhIdNumber == "" && patientsearchObj.patientName == "" && patientsearchObj.genderId == "" && patientsearchObj.mobileNo == ""  && patientsearchObj.identificationTypeId == "" && patientsearchObj.identificationNo == "" && patientsearchObj.birthDate=="")
						{
							growl.warning(SearchFilter_REQUIRED,{title : "WARNING!"});
							return false;
						}
							
						else{
								//alert("else AlERT");
							}
						//patientsearchObj.birthDate = moment(patientsearchObj.birthDate).format('YYYY-MM-DD');
						if(patientsearchObj.patientName!="" && patientsearchObj.patientName.length < 3)
						{
							growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
							$scope.patientList = [];
							return false;
						}	
						if(patientsearchObj.birthDate != "" && typeof(patientsearchObj.birthDate) != "undefined"){
							patientsearchObj.birthDate = moment(patientsearchObj.birthDate).format('YYYY-MM-DD');
						} 
						
						$rootScope.startLoader();
						$scope.patientList =[];
						var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
						BillingGenericService.serviceAction(METHOD_POST,URI, patientsearchObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
							
							if(response.data.listObject==null){
								growl.warning(response.data.message,{
									title : ERROR_MSG
								});
								$scope.patientSearchObj.birthDate="";
								$scope.patientList=[];
								return false;
							}
							
							
							if(Object.keys(response.data.listObject).length  == 0){
								growl.warning("No Result Found!",{
									title : ERROR_MSG
								});
								$scope.patientSearchObj.birthDate="";
								return false;
							}
							else{
								$scope.patientList = [];
							angular.forEach(response.data.listObject, function (value, key) {
								 
								if(value.registrationTypeId != 5 && value.registrationTypeId !=2)
									{
									 $scope.patientList.push(value);
									}
								  
							  });
							$scope.patientSearchObj.birthDate="";
							}
							
						});
						
					}
			    	
			    	
			    	
			    	
			    	$scope.selectedPatientId = "";
					$scope.getPatientDetailsByIndex = function(index){
						$scope.abscondPatientFlag="";
						if($scope.patientList[index].isAbsconded==undefined || $scope.patientList[index].isAbsconded==null || $scope.patientList[index].isAbsconded=='N'){
//							$scope.initReferralObj();
							$scope.patientDetails = {};
							console.log("$scope.patientList",$scope.patientList[index]);
							///$rootScope.startLoader();
							$scope.patientDetails.age=($scope.patientList[index].age);
							$scope.patientDetails.patientId=($scope.patientList[index].patientId);
							$scope.patientDetails.patientName=($scope.patientList[index].patientName);
							$scope.patientDetails.genderCode=($scope.patientList[index].genderCode);
							$scope.patientDetails.uhIdNumber=($scope.patientList[index].uhIdNumber);
							$scope.patientDetails.birthDate=($scope.patientList[index].birthDate);
							$scope.patientDetails.birthDate=($scope.patientList[index].birthDate);
							$scope.patientDetails.patientProfile = ($scope.patientList[index].patientProfile);
							$scope.patientDetails.genderId = ($scope.patientList[index].genderId);
                            $scope.patientDetails.patientDischargeDate =  moment( $scope.datepickerPdd).format('DD/MM/YYYY');
                            $scope.patientDetails.dateOfAdmission = moment(new Date()).format('DD/MM/YYYY');
                            $scope.selectedPatientId = ($scope.patientList[index].patientId);
                            $scope.trustObj.patientId=$scope.patientList[index].patientId;
                            $(".patienttSearchResult").hide();
							$scope.selectedPatient = 'Y';
							
						 }
					}
			    	
					
					$scope.getDiagnosisListSearch = function(keyword) {

						var data = {};
						var paramObj = {
							diagnosisName : keyword
						};
						var headerObj = {
							unit_id : $scope.unitId,
							organization_id : $scope.organizationId
						}

						var URI = BASE_URL + ROOT_URL
								+ DIAGNOSISAUTOFILLSEARCH;

						return GenericServiceParamHeader
								.serviceAction(METHOD_GET, URI, data,
										NOTIFICATION_MSG_STATUS_FALSE,
										paramObj, headerObj)
								.then(
										function(response) {
											// $rootScope.startLoader();
											if (response.data.status == "success")
												return response.data.listObject;
										});

					}
					
					
					$scope.commonVitualTrustAcceptanceList=[];
					$scope.getFundApprovalRequestDetails =function(){
						
//						data-toggle="modal" data-target="#approve_Request_Details_modal"
						if($scope.trustObj.trustId==undefined || $scope.trustObj.trustId==""  ) 
						{
							growl.warning("Please select Trust",
									{
										title : 'warning!'
									});
							return;
						}else{
							$scope.fundAmount=0;
							$scope.finalCheckedAmount=0;
							$("#approve_Request_Details_modal").modal("show");
							$rootScope.startLoaderPopup();
							var data = {};
							
							if($scope.selectedPatientId != undefined && $scope.selectedPatientId != ""){
								data={  trustId : $scope.trustObj.trustId,
								        orgId : $scope.orgId ,
							            unitId:$scope.unitId,
							            patientId:$scope.selectedPatientId
							        };
								
							}else{
								data={  trustId : $scope.trustObj.trustId,
								        orgId : $scope.orgId ,
							            unitId:$scope.unitId
							        };
							}
							
								
							 var URI = BASE_URL + ROOT_URL +"/api/billing/getAllPendingVirtualAcceptance";
							BillingGenericService
							.serviceAction(METHOD_POST, URI, data,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
					                    console.log(response);
					                    if (response.data.status == "success"){
					                    	$rootScope.stopLoaderPopup();
					                    	$scope.commonVitualTrustAcceptanceList=[];
					                    	
					                    	$scope.commonVitualTrustAcceptanceList=response.data.listObject;
					                    	if($scope.commonVitualTrustAcceptanceList!=null){
					                    		angular.forEach($scope.commonVitualTrustAcceptanceList, function(value, key) { 
											   		value.sanctionApprovedflagforCheck=false;
											   		value.virtualAcceptancePaidAmmount="";
											   	 });
					                    	}
					                    	console.log($scope.commonVitualTrustAcceptanceList);
					                    }
					                });
							        
						}
						
					}
$scope.finalCheckedAmount=0;
$scope.getcheckedFundApprovalDetails =function(index){
	$scope.finalCheckedAmount=0;
	
	var zeroenterd=0;
	var envalidamount=0;
	$scope.fundAmount=0;
	    angular.forEach($scope.commonVitualTrustAcceptanceList, function(value, key) { 
   		     if(value.sanctionApprovedflagforCheck==true){
   		    	 
   		    	if(key==index && (value.virtualAcceptancePaidAmmount==undefined ||  value.virtualAcceptancePaidAmmount=="") ) {
   		    		value.virtualAcceptancePaidAmmount=parseFloat ( value.requestApprovalAmount - value.virtualPaidAmount);
   		    		$scope.finalCheckedAmount= parseFloat ($scope.finalCheckedAmount + value.virtualAcceptancePaidAmmount) ;
   		    	}else if(key==index && (value.virtualAcceptancePaidAmmount!=undefined ||  value.virtualAcceptancePaidAmmount!=""))
   		    	{
   		    		if(value.virtualAcceptancePaidAmmount==0 || value.virtualAcceptancePaidAmmount=="" ){
   		    			zeroenterd++;
   		    			value.virtualAcceptancePaidAmmount="";
   		    		}
   		    		  if(value.virtualAcceptancePaidAmmount> 0 && (parseFloat( value.virtualAcceptancePaidAmmount) >  parseFloat ( value.requestApprovalAmount - value.virtualPaidAmount)) )
   		    		  {
   		    			envalidamount++; 
   		    			value.virtualAcceptancePaidAmmount="";
   		    	      }else if(value.virtualAcceptancePaidAmmount!=""){
   		    	    	value.virtualAcceptancePaidAmmount=value.virtualAcceptancePaidAmmount;
   	   		    		$scope.finalCheckedAmount= parseFloat ($scope.finalCheckedAmount) + parseFloat(value.virtualAcceptancePaidAmmount);
   		    	     }
   		    		
   		    	}
   		    	
   		    	if(key!=index && (value.virtualAcceptancePaidAmmount!=undefined ||  value.virtualAcceptancePaidAmmount!=""))
   		    	{
   		    		$scope.finalCheckedAmount=parseFloat($scope.finalCheckedAmount + value.virtualAcceptancePaidAmmount);
   		    	}
   		    	
		    	
   		      }else{
   		    	value.virtualAcceptancePaidAmmount="";
   		      }
   		     
   		       
   	     });
	    if($scope.finalCheckedAmount > 0){
	    	$scope.fundAmount=$scope.finalCheckedAmount;
	    }
	    
	    if(zeroenterd>0){
	    	growl.warning("Entered amount  should not be  zero or empty.",
					{
						title : 'warning!'
					});
	    	
	    }
        if(envalidamount>0){
        	growl.warning("please enter valid amount ",
					{
						title : 'warning!'
					});
	    	
	    }
	    
						
}
					
					$scope.addDiagnosisList = [];
					$scope.addDiagnosisListTemp = function($item, $model, $label){
						  
						var flag = false;
						angular.forEach($scope.addDiagnosisList,function(value,key){
							
							if(value.diagnosisId == $item.diagnosisId){
								flag = true;
							}
						});
						
						if(flag == false){
				        	            	 var newdata ={};
												newdata.diagnosisId = $item.diagnosisId;
												newdata.diagnosisName = $item.diagnosisName;
												newdata.code = $item.code;
												$scope.addDiagnosisList.push(newdata);
												$scope.searchDiagnosis="";
						}else{
							$scope.searchDiagnosis="";
							growl.warning("This Diagnosis is  already added ",
									{
										title : 'warning!'
									});
						}
					 }
					
					$scope.removeTempAddedDiagnosis = function(index){
						
						$scope.addDiagnosisList.splice(index,1);
					}
					
					$scope.fundAmount='';
					$scope.setFundAmmount =function(){
					//	data-toggle="modal" data-target="#fundCollectPayment"
						$scope.commonVitualTrustAcceptanceCheckedList=[];
						if( $scope.commonVitualTrustAcceptanceList!= null && $scope.commonVitualTrustAcceptanceList.length>0 ){
							
							angular.forEach($scope.commonVitualTrustAcceptanceList, function(value, key) { 
					   		     if(value.sanctionApprovedflagforCheck==true){
					   		    	$scope.commonVitualTrustAcceptanceCheckedList.push(value);
					   		      }
					   	     });
						}
					
						
						if($scope.trustObj.trustId==undefined || $scope.trustObj.trustId==""  ) 
						{
							growl.warning("Please select Trust",
									{
										title : 'warning!'
									});
							return;
						}else if( $scope.selectedtrustFundTypeId==undefined || $scope.selectedtrustFundTypeId==""  ){
							growl.warning("Please select Fund Type",
									{
										title : 'warning!'
									});
							return;
						}else if( $scope.selectedtrustFundTypeId ==2 &&  $scope.selectedPatientId==""  ){
							growl.warning("Please Select Patient",
									{
										title : 'warning!'
									});
							return;
						}else if($scope.selectedtrustFundTypeId == 3 && $scope.addDiagnosisList.length==0){
							growl.warning("Please Select Diagnosis",
									{
										title : 'warning!'
									});
							return;
						}else if($scope.selectedtrustFundTypeId == 4 && $scope.commonVitualTrustAcceptanceCheckedList.length==0){
							growl.warning("Please Select Virtual accepted Request",
									{
										title : 'warning!'
									});
							return;
						}else if( $scope.fundAmount==undefined || $scope.fundAmount==""  ){
							growl.warning("Please enter Amount",
									{
										title : 'warning!'
									});
							return;
						}else{
							$scope.listPaymentDetails=[];
							$scope.finalAmount=0;
							$scope.calculateTotalDueRefAmount();
							$scope.initPaymentDetailsObj();
							$("#fundCollectPayment").modal();
							$scope.setFirstTabLevel(1);
							$scope.setPaymentModeId(1);
							 
							$scope.paymentDetails.cashAmount=parseFloat($scope.fundAmount);
							$scope.paymentDetails.chequeAmount=parseFloat($scope.fundAmount);
							$scope.paymentDetails.credAmount=parseFloat($scope.fundAmount);
							$scope.paymentDetails.transferAmount=parseFloat($scope.fundAmount);
							
						}
						
						
					}
					
				}
						
 ]);