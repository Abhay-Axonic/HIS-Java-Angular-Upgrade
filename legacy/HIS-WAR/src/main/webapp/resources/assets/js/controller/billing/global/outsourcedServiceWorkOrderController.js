  (function () {
  "use strict";

  function outsourcedServiceWorkOrderController($scope,$rootScope,BillingGenericService,$cookies,$state,$timeout,growl,paymentCollFactory,reportService,GenericService,$location,loyaltiCardCallFactory,$interval,GenericServiceParamHeader,$window,GenericServiceFileUpload) {
		try{
			$rootScope.loginpage = true;
			$scope.page = "billList";
			$scope.dateOptions = {
					formatYear : 'yyyy',
					showWeeks : false,
					//minDate : new Date()
				};
			
			$scope.dateOptionsTrancs = {
					formatYear : 'yyyy',
					showWeeks : false,
					minDate : new Date()
				};
			
			
			$scope.dateOptionsTo = {
					formatYear : 'yyyy',
					showWeeks : false,
					minDate : new Date()
				};
			
			
			
			$scope.cardExpiryOptions = {
					formatYear : 'yyyy',
					showWeeks : false,
					minDate : new Date(),
				};
			$scope.chequedDateOptions = {
					formatYear : 'yyyy',
					showWeeks : false,
					minDate : moment(new Date()).subtract(90, 'days').toDate(),
				};
			$scope.openDatePicker = function() { 
				$scope.datepicker.opened = true;
			};
			$scope.openDatePickerTo = function() {
				$scope.datepickerTo.opened = true;
			};
			
			$scope.openDatePickerPatiSearch = function() {
				$scope.datepicker1.opened = true;
			};

			$scope.datepicker1 = {
					opened : false
				};
			
			$scope.datepicker = {
				opened : false
			};
			$scope.datepickerTo = {
					opened : false
				};
			$scope.dateOptions1 = {
					formatYear : 'yyyy',
					showWeeks : false,
					minDate : new Date()
				};
			$scope.openDatePicker3 = function() { 
				$scope.datepicker3.opened = true;
			};
			$scope.datepicker3 = {
					opened : false
				};
			
			
			
			
			$scope.mpesaRigths = JSON.parse(localStorage.getItem('mpesaRigths'));
			var cookieObject = $cookies.getObject('cookieObject');
			$scope.mpesaRequired = cookieObject.mpesaRequired;
			if (cookieObject == undefined) {
				$state.go('login');
				return;
			}
			
			var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
			var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
			if (cookieObjectAppConfigSecond != undefined) {
			$scope.cashAmountValidation =  cookieObjectAppConfig.cashAmountValidation;
			$scope.cashAmountLimit =  cookieObjectAppConfig.cashAmountLimit;
			$scope.payGatewayProviderId = cookieObjectAppConfig.payGatewayProviderId;
			$scope.exceededBillAmountSettlement=cookieObjectAppConfigSecond.exceededBillAmountSettlement;
			}
			 if(cookieObjectAppConfig == undefined){
			  $state.go('login');
			  return;
			 }
			 else
			  {
			   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
			   
			  }
			$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
			$scope.pharmacyServiceId = cookieObjectAppConfig.pharmacyServiceId;
			$scope.isAppointmentRequired = cookieObjectAppConfig.isAppointmentRequired;
			$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
			$scope.selfPayeeIdCookies = cookieObjectAppConfig.selfPayeeId;
			$scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
			$scope.isPinPadRequired = cookieObjectAppConfig.isPinPadRequired;
			$scope.registeredPinPadDevice= cookieObjectAppConfig.registeredPinPadDevice;
			$scope.isGstVisible = cookieObjectAppConfig.isGstVisible;
			$scope.dateFormat = cookieObjectAppConfig.dateFormat;
			$scope.billSettlement = cookieObject.billSettlement;
			$scope.ipBillSettlement = cookieObject.ipBillSettlement;
			$scope.pinpadAmounFlag = false;
			$scope.excessAmount = parseFloat(0).toFixed(2);	
			$scope.unitId = cookieObject.unitId;
			$scope.userId = cookieObject.userId;
			$scope.organizationId = cookieObject.organizationId;
			$scope.userId =$scope.userId;
			$scope.appointmentId = 0;
			$scope.encounterId = 0;
			$scope.validatTextFieldFlag = true;
			$scope.addToBillFlag = true;
			$scope.skipMpesaValidation = false;
			$scope.hitMpesaApi = false;
			$scope.fileObj = {};
			$scope.fileObj.file = null;
			$scope.insurancePayeeExist = false;
			$scope.selfPayeeExist = false;
			$scope.cashCollectedAmount = 0;
			$scope.DepositObj = {};
			$scope.DepositObj.userEnterDepositAmount = 0;
		    $scope.flagForSale=false;
		    $scope.flagForPay=false;
		    $scope.flagForBharatQr=false;
		    $scope.objPosMachine={
					isSale:false, 
					isSMSpay : false,
					isBharatQr:false, 
					posMachineDeviceId:"",
					deviceName:"",
					machineConfigDetails:"",
					mobileNumberForPOS:""
			};
		    
		    $scope.workOrderDetails = {
					'deliveryDate':new Date(),
					'billTypeId':3,
					'contractTypeId':3,
					'againstTypeId':1
			}
		  
		    
		    
			 $scope.getOrderlist = function(){
				var URI = BASE_URL + ROOT_URL + API + "/procurements/workOrderRequest/dropdowns/" + $scope.unitId + "/" + $scope.organizationId + "/" + $scope.userId;
				BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.status == 200){
						$scope.getOrderlist = response.data.contractType;
					}
					console.log("getOrderlist",$scope.getOrderlist);
				});
			}
			 $scope.getOrderlist ();
			 
			  $scope.requestTypeslist=[];
			 $scope.getRequestTypes = function(){ 
				 var URI=BASE_URL + ROOT_URL +"/api/procurements/WOCreation/dropdowns/" + $scope.unitId + "/" + $scope.organizationId + "/" + $scope.userId;
			 GenericService.serviceAction("GET", URI, {}).then(
				        function (response) {
				        	  $scope.requestTypeslist=[];
				          $scope.requestTypeslist = response.data.requestType;
				          

				          $rootScope.stopLoader();
				        },
				        function (err) {
				          $rootScope.stopLoader();
				        });
			 
			 }
			 $scope.getRequestTypes();
			 
			 
			 $scope.getStoreLists = [{
					"store": "All",
					"id": 0
				}],
				
				$scope.getStoreList=[];
				 $scope.getStorelist = function(){
					var URI = BASE_URL + ROOT_URL + API + "/procurements/WOCreation/dropdowns/" + $scope.unitId + "/" + $scope.organizationId + "/" + $scope.userId;
					BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						if(response.status == 200){
							$scope.getStoreList = $scope.getStoreLists.concat(response.data.store);
						}
						console.log("getStorelist",$scope.getStoreList);
					});
				}
				 $scope.getStorelist ();
			 
			 
			 $scope.clearWorkorderObj=function(){
				 $scope.workOrderDetails = {
							'deliveryDate':new Date(),
							'billTypeId':'',
							'contractTypeId':3,
							'againstTypeId':1
					}
				 $scope.workOrderItemDetailsList = [];
				 
				 $scope.init();
				 
			 }
			/**================================COUNTER CODE START======================================*/
			$scope.isCounterApplicable = JSON.parse(sessionStorage.getItem('isCounterApplicable'));
			$scope.isUserCounterApplicable = JSON.parse(sessionStorage.getItem('isUserCounterApplicable'));
			if($scope.isCounterApplicable=='Y' && $scope.isUserCounterApplicable=='Y')
				{
				     var flag = $scope.getCounterDetails();
				     if(flag)
				    	 {
				    	 	$rootScope.counterId = $rootScope.counterMstObj.counterId!=""?$rootScope.counterMstObj.counterId:"";
				    	 	$rootScope.counterDesc = $rootScope.counterMstObj.counterDesc!=""?$rootScope.counterMstObj.counterDesc:"";
				    	 }
				     else
				     {
				    	 $rootScope.counterList = JSON.parse(localStorage.getItem('counterList'));
				     }
				}else{
					$rootScope.counterId = 0;
				}
			/*$scope.confirmCounter = function()
			 {
				$scope.counterMstObj = $cookies.getObject('counterMstObj');
				$rootScope.counterId = $scope.counterMstObj.counterId!=""?$scope.counterMstObj.counterId:"";
			 }*/
			$scope.confirmCounter = function()
			 {
				/*$scope.counterMstObj = $cookies.getObject('counterMstObj');
				$rootScope.counterId = $scope.counterMstObj.counterId!=""?$scope.counterMstObj.counterId:"";*/
				$scope.counterMstObj = JSON.parse(sessionStorage.getItem('counterMstObj'));
				if($scope.counterMstObj == undefined || $scope.counterMstObj == null || $scope.counterMstObj.counterId == undefined){
					growl.warning('Select Counter');
					return false;
				}else if($scope.counterMstObj.counterId > 0){
					$scope.counterId = $scope.counterMstObj.counterId!=""?$scope.counterMstObj.counterId:"";
					angular.element('#counterDetailsModal').modal('hide');
				}
			 }
			/**================================COUNTER CODE END======================================*/
			
			
			
			//vivek added
			$scope.payeeId =$scope.selfPayeeIdCookies;
			$scope.consumedAmt = parseFloat(0);
			$scope.receivedFrom = "";
			$scope.identificationNumber = "";
			
			$scope.depositeList = {
				    depositeIds: [1]
				  };
			
			$scope.depositeDisabledFlag = false;
			$scope.depositeSettleBtnFlag = true;
			$scope.isDisable=false;
			$scope.Readonlybtn = false;
			//vivek added
			
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
			
			
			$scope.searchBills = function(billSearch)
			{
				$scope.searchBillFilter = '';
				var billSearchObj = angular.copy(billSearch);
				billSearchObj.patientId = $scope.billSearch.patientId;
			
				if($scope.billSearch.fromDate != '' && $scope.billSearch.toDate != '' && typeof($scope.billSearch.fromDate) != "undefined" && typeof($scope.billSearch.toDate) != "undefined"){
					billSearchObj.fromDate = $rootScope.getFormattedDateMisc($scope.billSearch.fromDate,false);
					billSearchObj.toDate = $rootScope.getFormattedDateMisc($scope.billSearch.toDate,false);
				}
				
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + "/api/services/workOrder/pending/list";
				BillingGenericService.serviceAction(METHOD_POST,URI,billSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						$scope.billList = response.data.listObject;
						
						$rootScope.stopLoader();
					}else{
						$rootScope.stopLoader();
					}
				});
			
		}
			
			$scope.initBillSearchObj = function(){
					//$scope.billSearch = {fromDate:"",toDate:"",visitTypeId:"",billNo:"",visitNo:"",billStatusId:"",patientId:"",orgId:$scope.organizationId,unitId:$scope.unitId,isOtcReg:false};
				$scope.billSearch = {fromDate:new Date(),toDate:new Date(),billNumber:"",visitNumber:"",selfPayeeId:$scope.selfPayeeIdCookies,patientId:"",unitId:$scope.unitId};
				$scope.dateOptionsTo.minDate = $scope.billSearch.fromDate;
			}
			
			$scope.init = function(){
				$scope.paymentEntitlementId = (1).toString();
				$scope.billing = true;
				$scope.billSearch = {};
				$scope.initBillSearchObj();
				$scope.billList = [];
				$scope.payeeList = [];
				$scope.paymentDetailsList=[];
				$scope.listPaymentDetails = [];
				$scope.availableDeposits = [];
				$scope.activeDeposit = 0;
				$scope.billDisabled = false;
				$scope.isFinancialClearance = false;
				$scope.transferToActiveDepositeFlag = false;
				$scope.searchGroupDetailsFilter="";
				$scope.searchGroupPrePostFilter="";
				$scope.searchServiceFilter="";
				$scope.isDisable=true;
	    		$scope.isDisableCompany=true;
	    		//this.$parent.isFinancialClearence = 'N';
	    		$('#isFinancialClearance_check').prop('checked', false);
	    		
	    		 $scope.workOrderDetails = {
	 					'deliveryDate':new Date(),
	 					'billTypeId':2,
	 					'contractTypeId':3,
	 					'againstTypeId':1,
	 					'storeId':''
	 			}
	 		  
	    		 
				$scope.patientSearchObj = {
						"uhIdNumber"      : "",
						"patientName"     : "",
						"genderId"        : "",
						"mobileNo"        : "",
						"birthDate"       : "",
						"identificationNo":"",
						"unitId"          :$scope.unitId,
						"organizationId"  :$scope.organizationId,
				}
				//check wether redirected from ipd charges
				$scope.searchBills($scope.billSearch);
				
				var URI = BASE_URL + ROOT_URL
				+ GETACTIVEIDENTIFICATION;
		GenericService
				.serviceAction("GET", URI, {})
				.then(
						function(response) {
							$scope.identificationTypeList = [];

							if (response.data.status == "success") {
								$scope.identificationTypeList = response.data.listObject;
								

							}
						});
		
				var URI = BASE_URL + ROOT_URL
				+ "/billing/getUsersDiscRights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/userId/"+$scope.userId;
				BillingGenericService.serviceAction(METHOD_GET, URI,"",
						NOTIFICATION_MSG_STATUS_FALSE)
				.then(function(response) {
							console.log(response);
							// $rootScope.startLoader();
							if (response.data.status == "success")
								{
								$scope.userDiscountObject ={};
								$scope.userDiscountObject = response.data.object;
								if($scope.userDiscountObject != null){
						    		
									if($scope.userDiscountObject.discountType == 1){
										$scope.userDiscountObject.fromDiscountPer = $scope.userDiscountObject.fromDiscountAmount;
										$scope.userDiscountObject.toDiscountPer = $scope.userDiscountObject.toDiscountAmount;
									}
						    	}
									 
								}
								
						});
		
		
			}
			$scope.initTotalAmounts=function(){
				$scope.totalPaymentAmount=parseFloat(0).toFixed(2);
				$scope.dueAmount=parseFloat(0).toFixed(2);
				$scope.refundAmount=parseFloat(0).toFixed(2);
				$scope.totalAmountSummary=parseFloat(0).toFixed(2);
				$scope.totalConcessionSummary=parseFloat(0).toFixed(2);
				$scope.netPayableSummary=parseFloat(0).toFixed(2);
				$scope.totalPayingAmount=parseFloat(0).toFixed(2);
			}
			
			$scope.firstTabLevel = 1 ;
			
			$scope.transferToActiveDeposit = "N";
			
			$scope.setFirstTabLevel= function(newTab)
			{
				$scope.firstTabLevel = newTab;
			};
			$scope.isSetFirstTabLevel = function(tabNum)
			{
				return $scope.firstTabLevel === tabNum;
			};
			
			
			$scope.searchGroupList = function(keyword) {

				var data = {
					patientName : keyword,
					unitId : $scope.unitId,
					organizationId : $scope.organizationId
				}
				console.log(data);

				var URI = BASE_URL + ROOT_URL
						+ PATIENTAUTOFILLSEARCH;

				return BillingGenericService
						.serviceAction(METHOD_POST, URI, data,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									console.log(response);
									// $rootScope.startLoader();
									if (response.data.status == "success")
										return response.data.listObject;
								});

			}
			$scope.selectPatientId = function(info, model,
					label) {

				$scope.billSearch.patientId = info.patientId;
			}
			
			$rootScope.searchPatient = function(patientsearchObj){
				if($scope.patientSearchObj.uhidNumber == '' && $scope.patientSearchObj.patientName == '' )
				{
					//alert();
					growl.warning(SearchFilter_REQUIRED,{title : "WARNING!"
					});
					return false;
				}
					
				else{
						//alert("else AlERT");
					}
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
				//var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";HIS-21596
				var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteriaWithDeathPatient";
				
				var patSearchObj = angular.copy($scope.patientSearchObj);
				patSearchObj.patientType = 1;
				if($scope.patientSearchObj.dob != "" && typeof($scope.patientSearchObj.dob) != "undefined"){
					var dt = new Date($scope.patientSearchObj.dob);
					patSearchObj.birthDate = dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + dt.getFullYear();
				} 
				BillingGenericService.serviceAction(METHOD_POST,URI, patSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					$scope.searchPatientList = [];
					if (response.data.status == "success")
						$scope.patientList = response.data.listObject;
					
					if(response.data.listObject==null){
						growl.warning(response.data.message,{
							title : ERROR_MSG
						});
						$scope.patientSearchObj.birthDate="";
						$scope.patientList=[];
						return false;
					}
					
					if($scope.patientList.length == 0){
						//alert();
						growl.warning("No Result Found!",{
							title : ERROR_MSG
						});
						$scope.patientSearchObj.birthDate="";
						$scope.patientList=[];
					}
					$scope.patientSearchObj.birthDate="";
					patSearchObj = {};
				});
			}
			
			
			 $scope.getTaxlist=function(){
				 $scope.selectedTaxList=[];
				 var URI = BASE_URL + ROOT_URL + "/api/globals/tax/org/"+$scope.organizationId;
					GenericService.serviceAction("GET", URI, {}).then(function(response) {
						if(response.status == 200){
						    $scope.selectedTaxList=response.data;
						    
						}
					});
			 }
			 $scope.getTaxlist();
			
			$scope.getDetailsByBillId = function(bill){
				
				$rootScope.startLoader();
				var data = {
						    "unitId":$scope.unitId,
						    "billId":bill.billId,
						    "opBillingBedCategoryId":$scope.opdBillingCatId,
							"admissionId":bill.admissionId,
							"encounterId":bill.encounterId
						};
				var URI = BASE_URL + ROOT_URL + "/api/services/workOrder/patient/details";
				BillingGenericService.serviceAction(METHOD_POST,URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					$scope.workOrderItemDetailsList = [];
					if (response.data.status == "success")
						$rootScope.stopLoader();
						$scope.workOrderItemDetailsList = response.data.listObject;
						$scope.billId = bill.billId;
						$scope.admissionId = bill.admissionId;
						$scope.encounterId = bill.encounterId;
						$scope.workOrderDetails = {
			 					'deliveryDate':new Date(),
			 					'billTypeId':2,
			 					'contractTypeId':3,
			 					'againstTypeId':1,
			 					'storeId':''
			 			}
						
						angular.element('#outsourcedserviceworkorderPopup').modal('show');
					
					if(response.data.listObject==null){
						$rootScope.stopLoader();
						growl.warning(response.data.message,{
							title : ERROR_MSG
						});
						//$scope.patientSearchObj.birthDate="";
						//$scope.patientList=[];
						return false;
					}
					
				});
				
			}
			
			
			$scope.calculate=function(workOrderItemDetail,index,blurpoint){
				
				if(workOrderItemDetail.itemQuantity!=undefined && workOrderItemDetail.itemQuantity!="" && workOrderItemDetail.itemRate!=undefined && workOrderItemDetail.itemRate!="" )
				{
					workOrderItemDetail.itemMrp=parseFloat( parseFloat(workOrderItemDetail.itemQuantity) * parseFloat(workOrderItemDetail.itemRate)).toFixed(2);	;
				
					if(workOrderItemDetail.itemDiscAmount!=undefined &&  workOrderItemDetail.itemDiscAmount!=""  && blurpoint=='DA' ){
						
						if(parseFloat(workOrderItemDetail.itemDiscAmount) > workOrderItemDetail.itemMrp ||	parseFloat(workOrderItemDetail.itemDiscAmount )==0){
							growl.warning("Please Enter Valid Discount Amount  .",
									{
										title : 'warning!'
									});
							workOrderItemDetail.itemDiscPercentage=0;
							workOrderItemDetail.itemDiscAmount=0;
						}else{
							workOrderItemDetail.itemDiscPercentage= parseFloat(  parseFloat(workOrderItemDetail.itemDiscAmount) /(parseFloat(workOrderItemDetail.itemMrp) )* parseFloat(100));
						}
						
					}
					
					if(workOrderItemDetail.itemDiscPercentage!=undefined &&  workOrderItemDetail.itemDiscPercentage!=""  && blurpoint=='DP'){
						if(parseFloat(workOrderItemDetail.itemDiscPercentage) > 100 ||	parseFloat(workOrderItemDetail.itemDiscPercentage )==0){
							growl.warning("Please Enter Valid Discount Percentage  .",
									{
										title : 'warning!'
									});
							workOrderItemDetail.itemDiscPercentage=0;
							workOrderItemDetail.itemDiscAmount=0;
							
						}else{
							workOrderItemDetail.itemDiscAmount= parseFloat(  parseFloat(workOrderItemDetail.itemMrp) *(parseFloat(workOrderItemDetail.itemDiscPercentage) )/ parseFloat(100)).toFixed(2);	
						}
						
					}
					
                       if(workOrderItemDetail.itemTax!=undefined &&  workOrderItemDetail.itemTax!=""  && blurpoint=='TAX'){
						
						if(workOrderItemDetail.itemDiscAmount==0){
							workOrderItemDetail.itemTaxAmount = parseFloat((parseFloat(workOrderItemDetail.itemMrp) * parseFloat(workOrderItemDetail.itemTax) )/parseFloat(100)).toFixed(2);	
						}else{
							
							workOrderItemDetail.itemTaxAmount = parseFloat((parseFloat(workOrderItemDetail.itemMrp - workOrderItemDetail.itemDiscAmount) * parseFloat(workOrderItemDetail.itemTax) )/parseFloat(100)).toFixed(2);	
						}
						
					}
					
					if(workOrderItemDetail.itemDiscAmount!=undefined &&  workOrderItemDetail.itemDiscAmount!=""  ){
							
							if(parseFloat(workOrderItemDetail.itemDiscAmount) > workOrderItemDetail.itemMrp ||	parseFloat(workOrderItemDetail.itemDiscAmount )==0){
								growl.warning("Please Enter Valid Discount Amount  .",
										{
											title : 'warning!'
										});
								workOrderItemDetail.itemDiscPercentage=0;
								workOrderItemDetail.itemDiscAmount=0;
							}else{
								workOrderItemDetail.itemDiscPercentage= parseFloat(  parseFloat(workOrderItemDetail.itemDiscAmount) /(parseFloat(workOrderItemDetail.itemMrp) )* parseFloat(100)).toFixed(2);	
							}
							
						}
						
					
					if(workOrderItemDetail.itemDiscPercentage!=undefined &&  workOrderItemDetail.itemDiscPercentage!="" ){
						if(parseFloat(workOrderItemDetail.itemDiscPercentage) > 100 ||	parseFloat(workOrderItemDetail.itemDiscPercentage )==0){
							growl.warning("Please Enter Valid Discount Percentage  .",
									{
										title : 'warning!'
									});
							workOrderItemDetail.itemDiscPercentage=0;
							workOrderItemDetail.itemDiscAmount=0;
							
						}else{
							workOrderItemDetail.itemDiscAmount= parseFloat(  parseFloat(workOrderItemDetail.itemMrp) *(parseFloat(workOrderItemDetail.itemDiscPercentage) )/ parseFloat(100)).toFixed(2);	
						}
						
					}
					
					
                   
                    if(workOrderItemDetail.itemTax!=undefined &&  workOrderItemDetail.itemTax!=""  ){
						//for tax already given
						if(workOrderItemDetail.itemDiscAmount==0){
							workOrderItemDetail.itemTaxAmount = parseFloat((parseFloat(workOrderItemDetail.itemMrp) * parseFloat(workOrderItemDetail.itemTax) )/parseFloat(100)).toFixed(2);	
						}else{
							
							workOrderItemDetail.itemTaxAmount = parseFloat((parseFloat(workOrderItemDetail.itemMrp - workOrderItemDetail.itemDiscAmount) * parseFloat(workOrderItemDetail.itemTax) )/parseFloat(100)).toFixed(2);	
						}
						
					}
                    
				
                    if(workOrderItemDetail.itemTax==0 &&  blurpoint=='TAX'){
						
						workOrderItemDetail.itemTaxAmount =parseFloat(0);
					}
				}
				
				
				//$scope.calculateFinalAmount();
				
			}
			
			$scope.calculationServiceObject = function(index){
				
				//$scope.workOrderItemDetailsList[index].itemMrp= parseFloat($scope.workOrderItemDetailsList[index].itemQuantity * $scope.workOrderItemDetailsList[index].itemRate).toFixed(2);
				
				$scope.workOrderItemDetailsList[index].itemNetAmount=  parseFloat(($scope.workOrderItemDetailsList[index].itemMrp -(parseFloat($scope.workOrderItemDetailsList[index].itemDiscAmount)) + parseFloat($scope.workOrderItemDetailsList[index].itemTaxAmount))).toFixed(2);
		        
				
			}
			
			 //$scope.selectedTax="";
			
			 
			 $scope.enterQty=function(qty,index){
					
				 if(qty == '' || qty == undefined || qty == '.'){
					 qty = 0;
				 }
				 $scope.workOrderItemDetailsList[index].itemQuantity=qty;
				 
				 $scope.workOrderItemDetailsList[index].itemMrp= parseFloat($scope.workOrderItemDetailsList[index].itemQuantity * $scope.workOrderItemDetailsList[index].itemRate).toFixed(2);
				 
				 $scope.workOrderItemDetailsList[index].itemDiscAmount =((parseFloat($scope.workOrderItemDetailsList[index].itemMrp))*(isNaN($scope.workOrderItemDetailsList[index].itemDiscPercentage) ? 0: $scope.workOrderItemDetailsList[index].itemDiscPercentage)*0.01).toFixed(2);
				 
				 $scope.workOrderItemDetailsList[index].itemTaxAmount=parseFloat(((parseFloat($scope.workOrderItemDetailsList[index].itemMrp) - parseFloat($scope.workOrderItemDetailsList[index].itemDiscAmount)) * $scope.workOrderItemDetailsList[index].itemTax)/100).toFixed(2);
				 
				 $scope.calculationServiceObject(index);
				 
				 
				}	
			 
			 $scope.enterRate=function(rate,index){
					
				 if(rate == '' || rate == undefined || rate == '.'){
					 rate = 0;
				 }
				 $scope.workOrderItemDetailsList[index].itemRate= rate;
				 
				 $scope.workOrderItemDetailsList[index].itemMrp= parseFloat($scope.workOrderItemDetailsList[index].itemQuantity * $scope.workOrderItemDetailsList[index].itemRate).toFixed(2);
				 
				 $scope.workOrderItemDetailsList[index].itemDiscAmount =((parseFloat($scope.workOrderItemDetailsList[index].itemMrp))*(isNaN($scope.workOrderItemDetailsList[index].itemDiscPercentage) ? 0: $scope.workOrderItemDetailsList[index].itemDiscPercentage)*0.01).toFixed(2);
				 
				 $scope.workOrderItemDetailsList[index].itemTaxAmount=parseFloat(((parseFloat($scope.workOrderItemDetailsList[index].itemMrp) - parseFloat($scope.workOrderItemDetailsList[index].itemDiscAmount)) * $scope.workOrderItemDetailsList[index].itemTax)/100).toFixed(2);
				 
				 
				 $scope.calculationServiceObject(index);
					
				}
			 
			 $scope.selectVendor =function(vendorId,workOrderItemDetail,selectedindex){
					
				 
					var vandorObj = $.grep(workOrderItemDetail.vendorList, function (vendrObj) {
			            return vendrObj.vendorId == vendorId;
			        })[0];
					
					
				 $scope.workOrderItemDetailsList[selectedindex].vendorId=vandorObj.vendorId;
				 $scope.workOrderItemDetailsList[selectedindex].itemRate= vandorObj.rate;
				 $scope.enterRate(vandorObj.rate,selectedindex)
				 
				 
				 
				 $scope.calculationServiceObject(selectedindex);
			}
			 
			 $scope.enterDisPer=function(discPer,index){
					
				 if(discPer == '' || discPer == undefined || discPer == '.'|| discPer > 100){
					 discPer = 0;
				 }
				 $scope.workOrderItemDetailsList[index].itemDiscPercentage= discPer;
				 
				 $scope.workOrderItemDetailsList[index].itemDiscAmount =((parseFloat($scope.workOrderItemDetailsList[index].itemMrp))*(isNaN(discPer) ? 0: discPer)*0.01).toFixed(2);
				 
				 
				 $scope.workOrderItemDetailsList[index].itemTaxAmount=parseFloat(((parseFloat($scope.workOrderItemDetailsList[index].itemMrp) - parseFloat($scope.workOrderItemDetailsList[index].itemDiscAmount)) * $scope.workOrderItemDetailsList[index].itemTax)/100).toFixed(2);
				
				 
				 $scope.calculationServiceObject(index);
					
				}
			 
			 $scope.enterDisAmt=function(disAmt,index){
					
				 if(disAmt == '' || disAmt == undefined || disAmt == '.'){
					 disAmt = 0;
				 }
				 
				 if(disAmt > $scope.workOrderItemDetailsList[index].itemMrp){
						
		    			growl.warning("Discount is not allowed more then Total Amount",{
								title : ERROR_MSG
							});
		    			return false;
				 }
				 $scope.workOrderItemDetailsList[index].itemDiscAmount= disAmt;	
				 $scope.workOrderItemDetailsList[index].itemDiscPercentage = isNaN(((parseFloat($scope.workOrderItemDetailsList[index].itemDiscAmount)/(parseFloat($scope.workOrderItemDetailsList[index].itemMrp)))*100).toFixed(2)) ? parseFloat(0).toFixed(2) : ((parseFloat($scope.workOrderItemDetailsList[index].itemDiscAmount)/(parseFloat($scope.workOrderItemDetailsList[index].itemMrp)))*100).toFixed(2);
				 
				 
				 
				 $scope.workOrderItemDetailsList[index].itemTaxAmount=parseFloat(((parseFloat($scope.workOrderItemDetailsList[index].itemMrp) - parseFloat($scope.workOrderItemDetailsList[index].itemDiscAmount)) * $scope.workOrderItemDetailsList[index].itemTax)/100).toFixed(2);
				
				 
				 $scope.calculationServiceObject(index);
					
				}
			 
			 
			 $scope.setTaxWorkorderItem = function(taxId,index){
				 
				 var taxObjj = $.grep($scope.selectedTaxList, function (taxObj) {
			            return taxObj.id == taxId;
			        })[0];
					
					
				 $scope.workOrderItemDetailsList[index].itemTaxId=taxObjj.id;
				 if(taxObjj.taxPercentage == '' || taxObjj.taxPercentage == undefined || taxObjj.taxPercentage == '.'){
					 taxObjj.taxPercentage = 0;
				 }
				 
				 
				 $scope.workOrderItemDetailsList[index].itemTax= taxObjj.taxPercentage;
				 
				 $scope.workOrderItemDetailsList[index].itemTaxAmount=parseFloat(((parseFloat($scope.workOrderItemDetailsList[index].itemMrp)- parseFloat($scope.workOrderItemDetailsList[index].itemDiscAmount)) * taxObjj.taxPercentage)/100).toFixed(2);
				 
				 $scope.calculationServiceObject(index);
			 }
			 
			 
			 
			 $scope.saveWorkOrderCreationRequest=function(){
             	
             	 if($scope.workOrderDetails.deliveryDate==undefined || $scope.workOrderDetails.deliveryDate==""){
				    	  growl.warning("Please select delivary date ",
									{
										title : 'warning!'
									});
							return false;
				          }
             	 
             	 else if($scope.workOrderDetails.storeId==undefined || $scope.workOrderDetails.storeId==''){
			    	  growl.warning("Please select store ",
								{
									title : 'warning!'
								});
						return false;
			          }
             	 
             	 else if($scope.workOrderDetails.billTypeId==undefined || $scope.workOrderDetails.billTypeId==''){
				    	  growl.warning("Please select Bill type",
									{
										title : 'warning!'
									});
							return false;
				          }
             	 
             	 else if($scope.workOrderDetails.contractTypeId==undefined ||$scope.workOrderDetails.contractTypeId==''){
				    	  growl.warning("Please select Order Type",
									{
										title : 'warning!'
									});
							return false;
				          }
             	 
             	 else if($scope.workOrderDetails.againstTypeId==undefined || $scope.workOrderDetails.againstTypeId==''){
			    	  growl.warning("Please select Agaist Type",
								{
									title : 'warning!'
								});
						return false;
			          }else{
			        	  $scope.Readonlybtn = true;
			        	  angular.element('#outsourcedserviceworkorderPopup').modal('hide');
             
			      var data = {
             			"contractTypeId": $scope.workOrderDetails.contractTypeId,
             			"billTypeId": $scope.workOrderDetails.billTypeId,
             			"deliveryDate": moment($scope.workOrderDetails.deliveryDate).format('DD-MM-YYYY HH:mm:ss'),
             			"guaranteeWarranty": $scope.workOrderDetails.guaranteeWarranty,
             			"paymentTerms": $scope.workOrderDetails.paymentTerms,
             			"status": "A",
             			"createdBy": $scope.userId,
             			"orgId":$scope.organizationId,
             			"unitId":$scope.unitId,
             			"billId":$scope.billId,
             			"encounterId":$scope.encounterId,
             			"admissionId":$scope.admissionId,
             			"storeId":$scope.workOrderDetails.storeId,
             			"againstTypeId":$scope.workOrderDetails.againstTypeId
             		}
             	
             	 data.requestTaxDetailsDto= [];
             	 data.requestDocumentDto=[];
             	 data.requestDetailsDto=[];
             	 //console.log($scope.workOrderItemDetailsList);
             	 var reqDetails = [];
             	angular.forEach($scope.workOrderItemDetailsList,function(value,key){
             		if(value.workorderRequestId==null && value.itemRate>0)
         			{
         			
             		var reqdetObj = {
             					"itemId": "",
             					"storeId": 0,
             					"itemQuantity": value.itemQuantity,
             					"itemRate": value.itemRate,
             					"itemMrp": value.itemMrp,
             					"itemDiscPercentage":value.itemDiscPercentage,
             					"itemDiscAmount": value.itemDiscAmount,
             					"itemTaxId":value.itemTaxId,
             					"itemTax":value.itemTax,
             					"itemTaxAmount": value.itemTaxAmount,
             					"itemNetAmount": value.itemNetAmount,
             					"itemRemark": value.itemRemark,
             					"status": "A",
             					"createdDate": "",
             					"createdBy": $scope.userId,
             					"updatedDate": "",
             					"updatedBy": $scope.userId,
             					"unitId":$scope.unitId,
             					"orgId": $scope.organizationId,
             					"orderDetailsId":value.orderDetailsId,
             					"vendorId":	value.vendorId,
             					"workorderRequestId": value.workorderRequestId,
             					"workorderRequestNumber":value.workorderRequestNumber
             				};
             			
             			reqdetObj.taxpercentObj = {};
             			
             			if(value.itemTaxId>0){
             				 var taxObjj = $.grep($scope.selectedTaxList, function (taxObj) {
                 				 return taxObj.id == value.itemTaxId;
         			        })[0];
                 			 
                 			 
                 			var dataTax = {
                 					"id": taxObjj.id,
                 					"code": taxObjj.code,
                 					"name": taxObjj.name,
                 					"taxPercentage": taxObjj.taxPercentage,
                 					"status": true,
                 					items:[]
                 			};
                 			
                 			var taxitems = [];
                 			angular.forEach(taxObjj.items,function(value1,key){
                 				var taxItemObj= {
                 						"id": value1.id,
                 						"name": value1.name,
                 						"taxPercentage":value1.taxPercentage
                 				}
                 				taxitems.push(taxItemObj);
                 				
                 			});
                 			
                 			dataTax.items = taxitems;
                 			
                 			reqdetObj.taxpercentObj  = dataTax;
             			}
             			
             			
             			reqDetails.push(reqdetObj);
         			}
		            });
             	
             	 data.requestDetailsDto= reqDetails;
             	 
             	 
             	// $rootScope.startLoader();
             	console.log("finalsaveObj", data);
             	//return false;
             	$rootScope.startLoaderPopup();
             	var URI = BASE_URL + ROOT_URL + API + "/services/workOrder/save";
             	GenericService.serviceAction (METHOD_POST,URI, data
                  ).then(function(response){
						if(response.status == 200){
							growl.success("Saved successfully",
									{
										title : 'Success!'
									});
							$rootScope.stopLoaderPopup();
							$scope.searchBills($scope.billSearch)
						}else{
							growl.success("Something went wrong",
									{
										title : 'Error!'
									});
						}
						$scope.Readonlybtn = false;
					});
             	
             	$rootScope.stopLoaderPopup();
             }
			} 
			 
			
			$scope.getCompanyMasterList = function(companyTypeId) 
			{
				if(companyTypeId != "" && companyTypeId!=1)
				{
					if(companyTypeId==3)
					{
						$scope.isDisable=true;
						$scope.isDisableCompany=false;
						$scope.billSearch.associateCompanyId = '';
			    		$scope.billSearch.companyId = '';
						
					}else{
						$scope.isDisableCompany=false;
						$scope.isDisable=false;
					}
					$rootScope.startLoader();
					  var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyTypeId:companyTypeId};
					    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getCompanyMasterList"
					    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							if (response.data.status == "success"){
								 $scope.companyMasterList = response.data.listObject;
								 //console.log("companyMasterList",JSON.stringify($scope.companyMasterList));
							}
							$rootScope.stopLoader();
						});
				}else{
					$scope.billSearch.associateCompanyId = '';
		    		$scope.billSearch.payeeId = '';
		    		$scope.isDisable=true;
		    		$scope.isDisableCompany=true;
				}
			}
			
			$scope.getAssociateCompanyMasterList = function(companyId) 
			{
				$scope.tempcompanyId=companyId;
				$rootScope.startLoader();
				var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyId:companyId};
				var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getAssociateCompanyMaster"
				BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
				{
					if (response.data.status == "success")
					{
						 $scope.associateCompanyMasterList = response.data.listObject;
					}
					$rootScope.stopLoader();
				});
			}
			
			
			$scope.getPatientDetailsById = function(patientId){
				$rootScope.startLoader();
				$scope.patientDetailsList = [];
				$scope.patientDetails = {}
				var URI = BASE_URL + ROOT_URL + "/api/global/patient/billing/"+patientId;
				BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						$scope.billSearch = {fromDate:new Date(),toDate:new Date(),visitTypeId:"",billNo:"",visitNo:"",billStatusId:"",patientId:"",orgId:$scope.organizationId,unitId:$scope.unitId};
						$scope.patientDetailsList = response.data.listObject;
						
						$scope.patientDetails = response.data.listObject[0];
						$scope.patientDetails.patientName = $scope.patientDetails.firstName + " " + $scope.patientDetails.lastName;
						$scope.patientDetails.genderCode = $scope.patientDetails.genderName.charAt(0).toUpperCase();
						$scope.patientDetails.age = $scope.patientDetails.ageString
						$scope.patientDetails.mobileNo = $scope.patientDetails.mobileNumber;
						$scope.billSearch.patientId = $scope.patientDetails.patientId; 
						$('.patienttSearchResult').toggle();
						// for billList to be set blank
						
						$scope.billList=[];
					}
				});
				$rootScope.stopLoader();
			}
			

			$scope.getAutoRenderedServicesByPatient=function(){
				
				$rootScope.startLoaderPopup();
				$scope.pendingServiceListPrePost = [];

				var commonObj = {
						unitId : $scope.unitId,
						organizationId : $scope.organizationId
					};
				
				var ReasonURI = BASE_URL + ROOT_URL
				+ GETACTIVEREASONLIST;
				GenericService.serviceAction("POST", ReasonURI, commonObj)
				.then(
						function(response) {
							$scope.reasonList = [];
							if (response.data.status == "success") {
								$scope.reasonList = response.data.listObject;
							}
				});
				
				
				var URI = null;
				
				if($scope.isPreOrPostBilling=='Y')
					URI = BASE_URL + ROOT_URL + "/api/billing/service/preBilling/orderedServices";
				else
					URI = BASE_URL + ROOT_URL + "/api/billing/serviceMaster/orderedServices";
				
				var obj={
						"visitTypeId":1,
						"encounterId":$scope.encounterId,
						"billId" : $scope.billId
				}
				console.log("AUTORENDER",obj);
				//return false;
				BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
				{	
					
					if(response.data.status == "success")
					{
						var tempAutoRendServList = [];
						angular.forEach(response.data.listObject,function(value,key){
				            value.orderDateString = moment(value.orderDateString,'YYYY-MM-DD').format($scope.dateFormat.toUpperCase());
				            });
						angular.forEach(response.data.listObject,function(value,index)
						{
					         if(value.isPackage==false)
				        	 {
				        	     var found = value.orderDetailsPackageMapperDtosList.find(element => element.isPackageService == 'Y');
				        	     if(found==undefined){
				        	    	 	value.packageMasterId = value.packageId;
				        	    	 	value.isServices = false;
				        	    	 	tempAutoRendServList.push(value);
				        	        }
				        	 }
					         var data = value.orderDetailsPayeeMapperDtosList.find(element => element.isInvoiceGenerated == 'Y');
					         value.isInvoiceGenerated = data!=null ? data.isInvoiceGenerated : 'N';
						});
						$scope.pendingServiceListPrePost = tempAutoRendServList;
						if($scope.pendingServiceListPrePost.length > 0){
							$scope.addToBillFlag = false; //HIS-18083
							/*if($scope.billDisabled==true){
								$scope.addToBillFlag = false;
							}else{
								$scope.addToBillFlag = false;
							}*/
							
							$scope.pendingCountY = $scope.pendingServiceListPrePost.length;
						}
						  
						$rootScope.stopLoaderPopup();
					}
					else
					{
						$rootScope.stopLoaderPopup();
					}	
				});
			}
			
			// Search Surgen with Typehead
			$scope.searchGroupSurgen = function(keyword) {

				var data = {};
				var paramObj = {
					doctor_name : keyword
				};
				var headerObj = {
					unit_id : $scope.unitId,
					organization_id : $scope.organizationId
				}
				console.log(data);

				var URI = BASE_URL + ROOT_URL
						+ GETDOCTORMASTERLISTAUTOFILLSERACH;

				return GenericServiceParamHeader
						.serviceAction(METHOD_GET, URI, data,
								NOTIFICATION_MSG_STATUS_FALSE,
								paramObj, headerObj)
						.then(
								function(response) {
									console.log(response);
									// $rootScope.startLoader();
									if (response.data.status == "success")
										return response.data.listObject;
								});

			}
			
			$scope.selectDoctor = function($item, $model, $label, idx) {
				$scope.search.doctorName = $item.doctorName;
				$scope.search.doctorId = $item.doctorId;
			}

			
			$scope.cancelPendingService = function(reasonId,item) {
	    		
				
				if(angular.isUndefined(reasonId))
					{
					growl
					.warning(
							'Please select reason...!!',
							{
								title : 'Warning!'
							});
					}
				else
					{
					
					$rootScope.startLoader();
				   var data = {};
				   var cancelList =[];
				   var orderedServicesDtosList = {};
				   var data = 
					{
						encounterId :item.encounterId,
						admissionId :item.admissionId,
						subSpecialityId : item.serviceSubSpecialityId,
						contractId : item.contractId,
						orderDetailsId:item.orderDetailsId,
						creaditPayable : item.creditPayable,
						ordCancelReasonId : reasonId,
						createdBy : $scope.userId,
						orderDetailsPayeeMapperDtosList : item.orderDetailsPayeeMapperDtosList,
						orderDetailsPackageMapperDtosList : item.orderDetailsPackageMapperDtosList,
						saleId :item.saleId
					}
					console.log('AddCapAmt',data);
					//return false;
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL +  "/global/consumption/service/cancel";
					BillingGenericService
							.serviceAction(METHOD_POST,
									URI, data,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										if (response.data.status == "success")
										{
											$scope.getPendingServicesByEncounterId();
											//$scope.billDisabled = false;
											/*$scope.transferToActiveDeposit = "N";
											this.transferToActiveDeposit = "N";*/
											$scope.billDetails($scope.currentBill);
											$scope.calculateTotalDueRefAmount();
											$scope.listPaymentDetails = [];
											$scope.searchBills($scope.billSearch);
											growl
											.success(
													'Cancelled Successfully!!',
													{
														title : 'Success!'
													});
											$rootScope.stopLoader();
										}
									});
					}
			
		}
			$scope.addToBill = function()
			{
				
				$scope.addToBillFlag = true;
				var serviceIdList =  [];
				angular.forEach($scope.pendingServiceListPrePost,function(value,index){
					if(value.isServices){
						serviceIdList.push(value.orderDetailsId);
					}
				});
				console.log('sdcsd',serviceIdList);
				if(serviceIdList.length > 0)
				{
					$rootScope.startLoaderPopup();
					var URI = BASE_URL + ROOT_URL
					+ "/api/billing/post/bill/service";
		    		GenericService.serviceAction("POST", URI, serviceIdList)
					.then(
							function(response) {
								if (response.data.status == "success") {
									
									$scope.getAutoRenderedServicesByPatient();
									$scope.searchBills($scope.billSearch);
									$rootScope.stopLoaderPopup();
									growl
									.success(
											'Services added in bill..!!',
											{
												title : 'Success'
											});
									
									$('#billListDetails').modal('hide');
									
									$scope.addToBillFlag = false;
									
								}
								else
									{
									$rootScope.stopLoaderPopup();
									growl
									.warning(
											'Issue with add services..!!',
											{
												title : 'warning'
											});
									$scope.addToBillFlag = false;
									}
							});
				}else
					{
						growl
						.warning(
								'The list is empty..!!',
								{
									title : 'warning'
								});
						$scope.addToBillFlag = false;
					}
				
				
			}
			
			$scope.cancelPendingServicePrePost = function(reasonId,item) {
	    		
				
				if(angular.isUndefined(reasonId))
					{
					growl
					.warning(
							'Please select reason...!!',
							{
								title : 'Warning!'
							});
					}
				else
					{
					$rootScope.startLoaderPopup();
				  // $rootScope.startLoader();
				   var data = {};
				   data.orderDetailsId = item.orderDetailsId;
				   data.ordCancelReasonId = reasonId;
				   data.userId = $scope.userId;
				   data.encounterId = $scope.encounterId;
				   data.specialityId =item.serviceSpecialityId;
				   data.visitTypeId=$scope.visitTypeId;
				   data.preOrPostBilling=$scope.isPreOrPostBilling;
				   data.pharmacyServiceId=$scope.pharmacyServiceId;
				   data.tariffId=$scope.defaultSelfTariffId;
				   data.opdBillingCatId = $scope.opdBillingCatId;
				  // data.orderDetailsPayeeMapperDtosList = item.orderDetailsPayeeMapperDtosList;
				   
	    		var URI = BASE_URL + ROOT_URL
				+ "/api/billing/bill/service/cancel";
	    		GenericService.serviceAction("POST", URI, data)
				.then(
						function(response) {
							if (response.data.status == "success") 
							{
								growl
								.success(
										response.data.message,
										{
											title : 'Success!'
										});
								$scope.getAutoRenderedServicesByPatient();
								//$rootScope.stopLoader();
								$rootScope.stopLoaderPopup();
							}
							else
								{
								$rootScope.stopLoaderPopup();
								}
						});
					}
			
		}

			
			
			
			$scope.getPendingServicesByEncounterId = function(){
				$rootScope.startLoader();
				
				var commonObj = {
						unitId : $scope.unitId,
						organizationId : $scope.organizationId
					};
				
				var ReasonURI = BASE_URL + ROOT_URL
				+ GETACTIVEREASONLIST;
				GenericService.serviceAction("POST", ReasonURI, commonObj)
				.then(
						function(response) {
							$scope.reasonList = [];
							if (response.data.status == "success") {
								$scope.reasonList = response.data.listObject;
							}
				});
				
				
				$scope.patientDetailsList = [];
				$scope.patientDetails = {}
				var URI = BASE_URL + ROOT_URL + "/api/opd/op/pending/services/encounter/"+$scope.encounterId;
				BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						$scope.pendingServicesList = [];
						$scope.pendingServicesList = response.data.listObject;
						if($scope.pendingServicesList.length > 0)
							{
								$scope.billDisabled = true;
								$scope.pendingCountN = $scope.pendingServicesList.length;
							}
						else
							{
							  $scope.billDisabled = false;
							}
					}
				});
				$rootScope.stopLoader();
			}
			
			//ADDED LATER GANESH
			$scope.getHospitalBankList = function()
			{
				var URI = BASE_URL + ROOT_URL + "/api/billing/hospitalBank/"+$scope.organizationId+"/"+$scope.unitId;
				BillingGenericService.serviceAction(METHOD_GET,URI,{},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
			       {
					if (response.data.status == "success")
					{
						$scope.hospitalBankList = response.data.listObject;
						
						/*
						
						angular.forEach(response.data.listObject,function(value,key)
					    	    {
									if(value.isDefault == 'Y')
										{
										$scope.$parent.$parent.paymentDetails.hospitalAccountId = value.id;
										}
					    	    });*/
					}
				  });
			}
			$scope.getHospitalBankList();
			
			$scope.eTransferObj={
					transBankId:"",
					transferAmount:"",
					transTransactionDate:"",
					transactionNo:"",
					transactionobj:{
							id:"",
							name:""
					}	
			}
			
			
			$scope.transactionTypesList=[];
			 $scope.getTransactionTypes = function(){
				var URI = BASE_URL + ROOT_URL + API + BILLING + "/transactiontypes/"+$scope.organizationId+"/"+$scope.unitId;
				BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						$scope.transactionTypesList = response.data.listObject;
					}
					
					console.log("transactionTypesList",$scope.transactionTypesList);
				});
			}
			 
			 $scope.getTransactionTypes();
			 
			 
			
			/*** loyalty Card****========================================================================*/
			$scope.getLoyaltyCardList = function(patientId){
				var data = {
						"patientId" : patientId,
						  "organisationId" : $scope.organizationId,
						  "unitId" : $scope.unitId,
						  "status" : "A",
						  "isDeleted" : "N",
						  "isUserDefined":"Y"
				}
				var URI = BASE_URL + ROOT_URL + "/api/loyaltyCard/getLoyaltiCardDetailsByPatientId";
				BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success")
					{
						$scope.send =false;
						$scope.resend =false;
						if(response.data.listObject!=null && response.data.listObject.length>0){
							$scope.loyaltyCardObj = response.data.listObject[0];					
							$scope.paymentDetails.cardBalAmount =  $scope.loyaltyCardObj.cardAmount;
							$scope.paymentDetails.loyaltyCardNo = $scope.loyaltyCardObj.cardNo;
						}else{
							$scope.paymentDetails.cardBalAmount = "";
							$scope.paymentDetails.loyaltyCardNo = "";
						}
						
					}
				});
			}
			
			$scope.checkAmt = function(amt)
			{
				if(isNaN(Number(parseFloat(amt))))
					{
					 $scope.paymentDetails.loyaltyCardAmt= 0.00;
					}
				else{
					 $scope.paymentDetails.loyaltyCardAmt= amt;
				}
			}
			
			$scope.getLoyaltyCardByNo = function(cardNo)
			{
				if(cardNo!="" && cardNo.toString().length==16)
					{
					var data = {
							"patientId" : '',
							  "organisationId" : $scope.organizationId,
							  "unitId" : $scope.unitId,
							  "status" : "A",
							  "isDeleted" : "N",
							  "isUserDefined":"Y",
							  "cardNo":cardNo
					}
					var URI = BASE_URL + ROOT_URL + "/api/loyaltyCard/getPatientDetailsByLoyaltiCardNo";
					BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success")
						{
							$scope.send =false;
							$scope.resend =false;
							if(response.data.listObject!=null && response.data.listObject.length>0)
							{
								var cardAmtToSub = 0;
						    	$scope.loyaltyCardObj = response.data.listObject[0];
							    $scope.paymentDetails.loyaltyCardNo = $scope.loyaltyCardObj.cardNo;
							    if($scope.listPaymentDetails!=null && $scope.listPaymentDetails.length>0)
							    	{
							    	   angular.forEach($scope.listPaymentDetails,function(value,key)
							    	    {
							    		   if(value.paymentModeId==6 && value.cardNumber==cardNo)
							    			   {
							    			     cardAmtToSub = cardAmtToSub + parseFloat(value.amount); 
							    			   }
											
										});
							    	}
							      var cardBalAmountTmp = $scope.loyaltyCardObj.cardAmount-cardAmtToSub;
							     $scope.paymentDetails.cardBalAmount = parseFloat(cardBalAmountTmp).toFixed(2); 
							}
						}
					});
					}

			}
			
	/*		$socpe.count ;
			if(flag)
				{  
				$socpe.count=0;
				$socpe.count=+1;
				}
			else if(!flag)
				{
				$socpe.count=-1;
				}*/
			$scope.generateOtp = function(flag)
			{
				if($scope.paymentDetails.cardBalAmount==0||$scope.paymentDetails.cardBalAmount=='')
					{
					growl.warning("Please Recharge Your card...!!",
							{
								title : 'Warning!'
							});
					return false;
		               	}
				if($scope.paymentDetails.loyaltyCardAmt==0||$scope.paymentDetails.loyaltyCardAmt==''||$scope.paymentDetails.loyaltyCardAmt==undefined||
						$scope.paymentDetails.loyaltyCardAmt==null)
					{
					growl.warning("Amount Should be greater than zero...!!",
							{
								title : 'Warning!'
							});
					    return false;
					 }
				if(parseFloat($scope.paymentDetails.loyaltyCardAmt)>parseFloat($scope.paymentDetails.cardBalAmount))
					{
					growl.warning("Insufficient fund in your account...!!",
							{
								title : 'Warning!'
							});
					    return false;
					 }
				if($scope.loyaltyCardObj.mobileNumber==''||$scope.loyaltyCardObj.mobileNumber==undefined||
						$scope.loyaltyCardObj.mobileNumber==null)
					{
					growl.warning("Mobile number not Found...!!",
							{
								title : 'Warning!'
							});
					    return false;
					 }
				
					$rootScope.startLoader();
					if($scope.paymentDetails.loyaltyCardNo!="" && $scope.paymentDetails.loyaltyCardNo.toString().length==16)
						{
						var data = {
								  "patientId" : '',
								  "organisationId" : $scope.organizationId,
								  "unitId" : $scope.unitId,
								  "status" : "A",
								  "isDeleted" : "N",
								  "isUserDefined":"Y",
								  "cardNo":$scope.paymentDetails.loyaltyCardNo,
								  "mobileNumber":$scope.loyaltyCardObj.mobileNumber,
								  "patientLoyaltyCardMpprId":$scope.loyaltyCardObj.patientLoyaltyCardMpprId,
								  "resendOTP" :flag,
								  "email":$scope.loyaltyCardObj.email,
								  "firstName":$scope.loyaltyCardObj.firstName,
								  "consumeAmount":$scope.paymentDetails.loyaltyCardAmt
						}
						var URI = BASE_URL + ROOT_URL + "/api/loyaltyCard/generateOTP";
						BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							if (response.data.status == "success")
							{
									$rootScope.stopLoader();
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
						}

			}
			
			$scope.sendOTP = function(flag)
			{
				loyaltiCardCallFactory.sendOTP(flag,$scope);
			}
			$scope.reSendOTP = function(flag)
			{
				loyaltiCardCallFactory.sendOTP(flag,$scope);
				//$scope.resend = (flag=='N')?true:false;
				//$scope.send =false;
			}
			
			
			$scope.clearLoylty = function(){
				
				$scope.send = false;
				$scope.validatTextFieldFlag = true;
				$scope.paymentDetails.otp ="";
				$scope.calculateTotalDueRefAmount();
				$scope.getLoyaltyCardList($scope.selectedPatientId);
			}
			
			$scope.validateOtp = function()
			{
				
				if($scope.paymentDetails.otp==0||$scope.paymentDetails.otp==''||$scope.paymentDetails.otp==undefined||
						$scope.paymentDetails.otp==null)
					{
					growl.warning("Please enter valid OTP",
							{
								title : 'Warning!'
							});
					    return false;
					 }
				else{
				$rootScope.startLoader();
					var data = {
							  "patientId" : '',
							  "organisationId" : $scope.organizationId,
							  "unitId" : $scope.unitId,
							  "status" : "A",
							  "isDeleted" : "N",
							  "consumeAmount":$scope.paymentDetails.loyaltyCardAmt,
							  "isUserDefined":"Y",
							  "otp": $scope.paymentDetails.otp,
							  "cardNo":$scope.paymentDetails.loyaltyCardNo,
							  "patientLoyaltyCardMpprId":$scope.loyaltyCardObj.patientLoyaltyCardMpprId,
							  "authKey":$scope.loyaltyCardObj.authKey
					}
					var URI = BASE_URL + ROOT_URL + "/api/loyaltyCard/validateOTP";
					BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success")
						{
								$rootScope.stopLoader();
								growl.success(response.data.message,
										{
											title : 'Success!'
										});
									$scope.loyaltyCardObj.authKey = 0;
								 	$scope.send=false;
								 	$scope.resend=false;
								 	$scope.validatTextFieldFlag = true;
									$scope.isBillList = 0;
									var paymentObj = angular.copy($scope.paymentDetails);
	                                paymentCollFactory.addPaymentDetails($scope);
									$scope.paymentDetails.loyaltyCardNo = $scope.loyaltyCardObj.cardNo;
									$scope.paymentDetails.cardBalAmount = paymentObj.cardBalAmount - parseFloat(paymentObj.loyaltyCardAmt);
								
							} else if(response.data.status == 'error')
							{
								$rootScope.stopLoader();
								growl.error(response.data.message,
										{
											title : 'Error!'
										});
						}
			
					});
				}
			}
			
		
			$scope.loyaltyCardObj ={
					
					"loyaltyCardNo" :"",
					"loyaltyCardBalance" :"",
					"authKey":0
			}
			
			
			
			
			/**====================================================================================*/
			$scope.amountPaidflag = true;
			$scope.selectedUhid = "";
			$scope.selectedPatientId = "";
			$scope.amountPaid ="";
			$scope.patientEmail= "";
			$scope.patientName= "";
			$scope.firstName= "";
			$scope.mobileNumber
			$scope.billDetails = function(bill){
				 var promise={};
				 promise=$scope.getBillReferenceData(bill.billingMasterId);
				 promise.then(function(response) {
					 $scope.billRefData=response;
					 if($scope.billRefData.intermediateSelfDue!=null &&   $scope.billRefData.intermediateSelfDue>0){
							
							$scope.billDisabled=true;
						
							growl.warning("Self content of Intermediate bill(s) of this admission is not yet settled ..!!",{
								title : WARNING_MSG
							});
							
							
						}
						
						$scope.billDetailsPatientInfo = {};
						$scope.billDetailsPatientInfo.patientName = bill.patientName;
						$scope.billDetailsPatientInfo.uhidNumber=bill.uhidNumber;
						$scope.billDetailsPatientInfo.birthDate=moment(bill.birthDate,'DD/MM/YYYY').format($scope.dateFormat.toUpperCase());
						$scope.billDetailsPatientInfo.ageString=bill.ageString;
						$scope.billDetailsPatientInfo.imagePath=bill.imagePath;
						$scope.billDetailsPatientInfo.genderId=bill.genderId;
						$scope.patientEmail = bill.email;
						$scope.patientName = bill.patientName;
						$scope.firstName= bill.firstName;
						if(bill.mobileNo!= null){
							$scope.mobileNumber = parseInt(bill.mobileNo);
							 $scope.countryCallingCode = parseInt(bill.countryCallingCode);
							$scope.razorPayObject.razorMobileNo = $scope.mobileNumber;
							$scope.objPosMachine.mobileNumberForPOS = $scope.mobileNumber;
						}
						
						$scope.setFirstTabLevel(1);
						$scope.excessAmount = parseFloat(0).toFixed(2);	
						$scope.transferToActiveDeposit = 'N'
						$scope.amountPaid = bill.amountPaid;
						$scope.selectedUhid = bill.uhidNumber;
						$scope.selectedPatientId = bill.patientId;
						$scope.rountOffString = '';
						console.log("BILL",bill);
						$scope.validatTextFieldFlag = true;
						//alert("bill.outstanding"+bill.outstanding);
						//$scope.billDisabled = true;
						//$scope.payeeId = "";
						$scope.billServicesList=[];
						$scope.paymentReceiptsDetailsList=[];
						$scope.listPaymentDetails = [];
						$scope.payeeList = [];
						$scope.totalAmtQuaBase = 0;
						$scope.totalAmtConcession = 0;
						$scope.totalAmtDiscount = 0;
						$scope.totalAmtGst = 0;
						$scope.totalNetPayableAmt = 0;
						$scope.payeeDueAmtFinal = 0;
						$scope.payeeDueAmtStatic = 0;
						$scope.patientIdForReport = bill.patientId;
						$scope.visitTypeId = bill.visitTypeId;
						$scope.referalDocId =  $scope.billRefData.referalDocId;
						$scope.consumedAmt = parseFloat(0);
						$scope.payeeId = $scope.selfPayeeIdCookies;
						$scope.isPaidAppointment = $scope.billRefData.isPaidAppointment!=null ? $scope.billRefData.isPaidAppointment : 'N';
						$scope.billDisabled = (bill.billStatusId == 3 || ( $scope.billRefData.intermediateSelfDue!=null &&   $scope.billRefData.intermediateSelfDue>0)) ? true : false;
						$scope.billId = bill.billingMasterId;
						$scope.isInterimOrFinalBill = bill.isInterimOrFinalBill;
						$scope.isNursingAssessmentRequired = bill.isNursingAssessmentRequired;
						$scope.disallowedAmount = bill.disallowedAmount;
						$scope.disallowedAgainstInvoiceDtlsId = bill.disallowedAgainstInvoiceDtlsId;
						$scope.doctorId = bill.doctorId;
						$scope.totalAmount = bill.netAmount; 
						$scope.dueAmount = bill.outstanding;
						$scope.oldDueAmount = bill.outstanding;
						$scope.billDueAmount = bill.outstanding;
						$scope.totalSelfPayable = bill.totalSelfPayable;
						$scope.totalCreditPayable=bill.totalCreditPayable;
						$scope.currentBill = bill;
						$scope.admissionId =bill.visitTypeId != 1 ? $scope.billRefData.visitId : null;
						$scope.isFinancialClearanceFlag = $scope.billRefData.isFinancialClearance;
						//this.$parent.isFinancialClearence = bill.isFinancialClearance;
						
						//Vivek added
						$scope.encounterId =$scope.billRefData.visitId;
						$scope.appointmentId = $scope.billRefData.appointmentId;
						//End Vivek Added
						
						$scope.pendingCountY = 0;
						$scope.pendingCountN = 0;
						if($scope.visitTypeId == 1 && $scope.isPreOrPostBilling == 'N'){
							$scope.getPendingServicesByEncounterId();
						}
						if($scope.visitTypeId == 1 && $scope.isPreOrPostBilling == 'Y'){
							$scope.getAutoRenderedServicesByPatient();
						}
						
						$scope.getCashCollectedAmount(bill.visitTypeId,$scope.billRefData.visitId);
						
						//get payer details
						$scope.initPaymentDetails();
						$scope.mpesaObject={};
						$scope.getLoyaltyCardList($scope.selectedPatientId);
						var URI = BASE_URL + ROOT_URL + "/api/billing/payee/bill/"+bill.billingMasterId;
						BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							if (response.data.status == "success")
							{
								$scope.insurancePayeeExist = false;
								$scope.selfPayeeExist = false;
								$scope.payeeList = response.data.listObject;
								$scope.billServicesList = response.data.object.listObject;
								 angular.forEach($scope.billServicesList,function(value,key){
							            value.orderDateString = moment(value.orderDateString,'DD/MM/YYYY').format($scope.dateFormat.toUpperCase());
							            });
								$scope.paymentReceiptsDetailsList=response.data.object.object;
								 angular.forEach($scope.paymentReceiptsDetailsList,function(value,key){
							            value.dateOfReciept = moment(value.dateOfReciept,'DD/MM/YYYY').format($scope.dateFormat.toUpperCase());
							            });
								var totalAmtQuaBase = parseFloat(0);
								var totalAmtConcession = parseFloat(0);
								var totalAmtDiscount = parseFloat(0);
								var totalAmtGst = parseFloat(0);
								var totalNetPayableAmt = parseFloat(0);
								var discountRoundOff = parseFloat(0);
								var totalSelfPayable = parseFloat(0);
								var totalCreditPayable = parseFloat(0);
								var totalPackageConsumptionAmt = parseFloat(0);
								
								angular.forEach($scope.billServicesList,function(value,key)
										{
									angular.forEach(value.orderDetailsPackageMapperDtosList,function(value1,key)
											{
											  if(value1.isPackageService=='N')
												  {
												  
												  if(value.packageConsumptionAmt == 0){
													  totalAmtQuaBase = totalAmtQuaBase + parseFloat(value.grossAmount);
												  }else{
													  totalAmtQuaBase = totalAmtQuaBase + parseFloat(value1.serviceAmount);
												  }
												    
													totalAmtConcession = totalAmtConcession + parseFloat(value.concession);
													totalAmtDiscount = totalAmtDiscount + parseFloat(value.discount);
													totalAmtGst = totalAmtGst + parseFloat(value.taxAmount);
													totalNetPayableAmt = totalNetPayableAmt + parseFloat(value.netAmt);
												    totalSelfPayable = totalSelfPayable + parseFloat(value.selfPayable);
													totalCreditPayable = totalCreditPayable + parseFloat(value.creditPayable);
													discountRoundOff = parseFloat(value.roundOff);
													totalPackageConsumptionAmt = totalPackageConsumptionAmt + parseFloat(value.packageConsumptionAmt);
												  }
											});
									
								});
								
								$scope.totalAmtQuaBase =totalAmtQuaBase;
								$scope.totalAmtConcession = totalAmtConcession;
								$scope.totalAmtDiscount =totalAmtDiscount+Number(discountRoundOff);
								$scope.rountOffString = Number(discountRoundOff)!=null ? '(RoundOff : '+discountRoundOff+')' :'';
								$scope.totalAmtGst = (totalAmtGst).toFixed(2);
								$scope.totalNetPayableAmt = totalNetPayableAmt-(Number(discountRoundOff));
								$scope.totalPackageConsumptionAmt = totalPackageConsumptionAmt;
								
								angular.forEach($scope.payeeList,function(value1,key)
								{
									if(value1.payeeId == $scope.selfPayeeIdCookies)
									{
										$scope.payeeDueAmtFinal = Number(parseFloat(value1.dueAmount).toFixed(2));
										$scope.payeeDueAmtStatic = Number(parseFloat(value1.dueAmount).toFixed(2));
										$scope.selfPayeeExist = true;
									}else{
										$scope.insurancePayeeExist = true;
									}
									
								});
								
								angular.forEach($scope.payeeList,function(value,key){
									
									if(value.payeeId==$scope.payeeId && (value.billAmount==0 || value.dueAmount==0))
										$scope.billDisabled = true;
								})
								
								
								
								//get available active deposits by patient
								var URI = BASE_URL + ROOT_URL + "/api/billing/unit/deposit/patient/"+$scope.organizationId+"/"+$scope.unitId+"/"+bill.patientId+"/"+$scope.billRefData.visitId+"/"+bill.visitTypeId+"/0/false";
								BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success")
									{
										
										$scope.consumedAmt=0;
//										$scope.availableDeposits = response.data.listObject;
										
										$scope.availableDeposits =[]; 
										
										angular.forEach(response.data.listObject , function(value,key){
											if(bill.visitTypeId != 1){
												if(value.visitTypeId != 1){
													$scope.availableDeposits.push(value);
												}
											}else{
												if(value.depositTypeId == 2){
													if(parseInt($scope.billRefData.visitId) == value.encounterId){
														$scope.availableDeposits.push(value);
													}
												}else{
													$scope.availableDeposits.push(value);
												}
											
											}
										});
										
										var activeDeposit = parseFloat(0);
										var activeDepositPatient = parseFloat(0);
										var billAmt = parseFloat($scope.payeeDueAmtStatic);
										var i = 0
										angular.forEach($scope.availableDeposits,function(value,key)
										{
											$scope.availableDeposits[i].oldConsumedDeposit = value.consumedDeposit;
											$scope.availableDeposits[i].oldAvailableDeposit =  value.availableDeposit;
											/*if(value.depositTypeId != '1')
										     {
											    activeDeposit = activeDeposit + parseFloat(value.availableDeposit);
											    
											 if(billAmt >0){ 
											   
											   if(billAmt >= value.availableDeposit)
													{
												        value.consumedDeposit = value.consumedDeposit+value.availableDeposit;
														$scope.availableDeposits[i].depositeFlag = 'Y';
														$scope.transferToActiveDepositeFlag = true;
														$scope.dueAmount = ($scope.dueAmount - value.availableDeposit).toFixed(2);
														billAmt = billAmt-value.availableDeposit;
														$scope.availableDeposits[i].availableDeposit = parseFloat(0);
													}							
												else if(billAmt < value.availableDeposit)
													{
														
														value.availableDeposit = parseFloat(value.availableDeposit)- parseFloat(billAmt);
														value.consumedDeposit = value.consumedDeposit + parseFloat(billAmt);
														$scope.availableDeposits[i].depositeFlag = 'Y';
														$scope.dueAmount = ($scope.dueAmount - billAmt).toFixed(2);
														$scope.payeeDueAmtFinal = 0;
														billAmt = 0;
														//$scope.dueAmount = ($scope.dueAmount -$scope.consumedAmt).toFixed(2);
													}
											    }else{
											    	$scope.availableDeposits[i].depositeFlag = 'N';
											    }
											}
											else
												{*/
												activeDeposit = activeDeposit + parseFloat(value.availableDeposit);
												$scope.availableDeposits[i].depositeFlag = 'N';
												activeDepositPatient = activeDepositPatient + parseFloat(value.availableDeposit);
												/*}*/
											i++;
											
										});
										$scope.activeDeposit = activeDeposit;
										$scope.useActiveDepo = true;
										
									}
									$scope.calculateTotalDueRefAmount();
								});
								
								if($scope.billRefData.isFinancialClearance=='Y'){
									$('#isFinancialClearance_check').prop('checked', true);
								}else{
									$('#isFinancialClearance_check').prop('checked',false);
								}
								
								if($scope.billRefData.isFinancialClearance=='Y')
									$("#isFinancialClearance_check").attr("disabled", true);
								else
									$("#isFinancialClearance_check").attr("disabled", false);
							}
						});
						 
				 });
				
			}
			
			$scope.checkAllServicePrePostFlag = 'N';
			$scope.checkAllServicePrePost = function()
			{
				if(this.checkAllServicePrePostFlag == 'Y')
				{
					angular.forEach($scope.pendingServiceListPrePost,function(value,key)
					{
						if(value.isInvoiceGenerated=='N')
							value.isServices = true;
					});
				}
				else{
					angular.forEach($scope.pendingServiceListPrePost,function(value,key)
					{
						if(value.isInvoiceGenerated=='N')
							value.isServices = false;
					});
				}
				
			}
			
			$scope.getBillReferenceData =function(billId){
				var URI = BASE_URL + ROOT_URL + "/api/billing/getBillAdditionalData/"+billId;
				return BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						return response.data.listObject[0];
					}
				});
			}
			//Start For Claim
			$scope.billDetailsForClaim = function(bill){
						
				$scope.skipMpesaValidation = false;
				$scope.pinpadAmounFlag = false;
				var promise={};
				 promise=$scope.getBillReferenceData(bill.billingMasterId);
				 promise.then(function(response) {
					 $scope.billRefData=response;
					 bill.visitId=$scope.billRefData.visitId;
					    console.log("BILL",bill);
						$scope.claimBill = bill;
						//alert("bill.outstanding"+bill.outstanding);
						//$scope.billDisabled = true;
						//$scope.payeeId = "";
						$scope.billServicesListClaim=[];
						//$scope.paymentReceiptsDetailsList=[];
						//$scope.listPaymentDetails = [];
						//$scope.payeeList = [];
						$scope.totalAmtQuaBase = 0;
						$scope.totalAmtConcession = 0;
						$scope.totalAmtDiscount = 0;
						$scope.totalAmtGst = 0;
						$scope.totalNetPayableAmt = 0;
						$scope.totalSelfPayable = 0;
						$scope.totalCreditPayable = 0;
						$scope.totalPackageConsumptionAmt = 0;
						
						/*$scope.payeeDueAmtFinal = 0;
						 * 
						$scope.payeeDueAmtStatic = 0;
						$scope.patientIdForReport = bill.patientId;
						$scope.visitTypeId = bill.visitTypeId;*/
						
						//$scope.consumedAmt = parseFloat(0);
						
						//$scope.payeeId = $scope.selfPayeeIdCookies;
						
						//$scope.billDisabled = bill.billStatusId == 3 ? true : false;
						$scope.billId = bill.billingMasterId;
						/*$scope.totalAmount = bill.netAmount; 
						$scope.dueAmount = bill.outstanding;
						$scope.oldDueAmount = bill.outstanding;
						$scope.billDueAmount = bill.outstanding;
						$scope.currentBill = bill;
						$scope.admissionId = bill.visitTypeId == 2 ? bill.visitId : null;
						$scope.isFinancialClearance = false;
						$('#isFinancialClearance').prop('checked',false);
						//Vivek added
						$scope.encounterId = bill.visitId;
						$scope.appointmentId = bill.appointmentId;
						//End Vivek Added
						//get payer details
						$scope.initPaymentDetails();*/
						//$scope.getLoyaltyCardList(bill.patientId);
						
						
						var URI = BASE_URL + ROOT_URL + "/api/billing/payee/bill/"+bill.billingMasterId;
						BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							if (response.data.status == "success"){
								//$scope.payeeList = response.data.listObject;
								$scope.billServicesListClaim = response.data.object.listObject;
								angular.forEach($scope.billServicesListClaim,function(value,key){
						            value.orderDateString = moment(value.orderDateString,'YYYY-MM-DD').format($scope.dateFormat.toUpperCase());
						            });
								//$scope.paymentReceiptsDetailsList=response.data.object.object;
								var totalAmtQuaBase = parseFloat(0);
								var totalAmtConcession = parseFloat(0);
								var totalAmtDiscount = parseFloat(0);
								var totalAmtGst = parseFloat(0);
								var totalNetPayableAmt = parseFloat(0);
								var totalSelfPayableAmtClaim = parseFloat(0);
								var totalCreditPayableAmtClaim = parseFloat(0);
								var totalPackagePayableAmt= parseFloat(0);
								angular.forEach($scope.billServicesListClaim,function(value,key){
									
									var isPackageServ=false;
									var packagePayableAmount=parseFloat(0);
									angular.forEach(value.orderDetailsPackageMapperDtosList,function(value1,key1){
										if(value1.isPackageService == 'Y'){
											isPackageServ=true;
											packagePayableAmount=packagePayableAmount+parseFloat(value1.serviceAmount);
										}
								
									});
									if(isPackageServ==true){
										value.netAmt=0;
										value.packagePayableAmount=packagePayableAmount;
									}else{
										value.packagePayableAmount=packagePayableAmount;
									}
									totalPackagePayableAmt=totalPackagePayableAmt+parseFloat(value.packagePayableAmount);
									totalAmtQuaBase = totalAmtQuaBase + parseFloat(value.grossAmount);
									totalAmtConcession = totalAmtConcession + parseFloat(value.concession);
									totalAmtDiscount = totalAmtDiscount + parseFloat(value.discount);
									totalAmtGst = totalAmtGst + parseFloat(value.taxAmount);
									totalNetPayableAmt = totalNetPayableAmt + parseFloat(value.netAmt);
									totalSelfPayableAmtClaim = totalSelfPayableAmtClaim + parseFloat(value.selfPayable);
									totalCreditPayableAmtClaim = totalCreditPayableAmtClaim + parseFloat(value.creditPayable);
								});
								
								$scope.totalAmtQuaBase =totalAmtQuaBase;
								$scope.totalAmtConcession = totalAmtConcession;
								$scope.totalAmtDiscount =totalAmtDiscount;
								$scope.totalAmtGst = (totalAmtGst).toFixed(2);
								$scope.totalNetPayableAmt = totalNetPayableAmt;
								$scope.totalCreditPayableAmtClaim = totalCreditPayableAmtClaim;
								$scope.totalSelfPayableAmtClaim = totalSelfPayableAmtClaim;
								$scope.totalPackageConsumptionAmt=totalPackagePayableAmt;
							}
						}); 
				 })
			}

					$scope.sendToSmartCard = function()
					{
						
						$rootScope.startLoader();
						console.log($scope.claimBill);
						
						var URI = BASE_URL + ROOT_URL + "/api/global/patient/billing/"+$scope.claimBill.patientId;
						BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							if (response.data.status == "success"){
								$scope.patientDetailsListForClaim = response.data.listObject;

								var data = {
										billDate : $scope.claimBill.billDate,
										billNumber : $scope.claimBill.billNumber,
										billingMasterId : $scope.claimBill.billingMasterId,
										visitDate : $scope.claimBill.visitDate,
										visitNo : $scope.claimBill.visitNo,
										visitId : $scope.claimBill.visitId,
										uhidNumber : $scope.claimBill.uhidNumber,
										patientId : $scope.claimBill.patientId,
										visitTypeId : $scope.claimBill.visitTypeId,
										totalSelfPayableAmtClaim : $scope.totalSelfPayableAmtClaim,
										totalCreditPayableAmtClaim : $scope.totalCreditPayableAmtClaim,
										totalNetPayableAmount : $scope.totalNetPayableAmt,
										firstName : $scope.patientDetailsListForClaim[0].firstName,
										middleName : $scope.patientDetailsListForClaim[0].middleName,
										lastName : $scope.patientDetailsListForClaim[0].lastName,
										dob : $scope.patientDetailsListForClaim[0].dob,
										genderName :$scope.patientDetailsListForClaim[0].genderName,
										unitId:$scope.unitId,
										organizationId:$scope.organizationId,
										pharamcy:false,
										pharamcyServiceId:$scope.pharmacyServiceId
								}
								
								var saveOrderDetailsObj = [];
								angular.forEach($scope.billServicesListClaim,function(value,key){
						            value.orderDateString = moment(value.orderDateString,'YYYY-MM-DD').format($scope.dateFormat.toUpperCase());
						            });
								angular.forEach($scope.billServicesListClaim, function (value, key) {
									var saveArr = {};
									saveArr.netAmount = value.netAmt;
									saveArr.specialityName = value.specialityName;
									saveArr.specialityId = value.specialityId;
									saveArr.serviceName = value.serviceStandardName;
									saveArr.serviceCode = value.serviceStandardCode;
									saveArr.orderQty = value.quantity;
									saveArr.serviceId = value.serviceId;
									saveArr.saleId = value.saleId;
									saveArr.orderId = value.orderId;
									saveOrderDetailsObj.push(saveArr);
								});
								
								if($scope.totalSelfPayableAmtClaim > 0)
									{
									var saveArr = {};
									saveArr.netAmount = parseFloat(0)-parseFloat($scope.totalSelfPayableAmtClaim);
									saveArr.specialityName = "COPAY";
									saveArr.orderQty = "1";
									saveArr.specialityId = 0;
									saveArr.serviceName ="COPAY";
									saveArr.serviceCode ="COPAY";
									saveOrderDetailsObj.push(saveArr);
									}
								data.orderDetailsMasterDtosList = saveOrderDetailsObj;
								
								console.log("data",data);
								
								var URL = BASE_URL + ROOT_URL+"/api/billing/submitSmartCard";
								GenericService.serviceAction("POST", URL,data).then(function(response) {
									if(response.data.status == "success"){
										
										growl.success("Data sent to smartcard system.",{
											title : "SUCCESS!"
										});
										$('#billListDetailsClaim').modal('hide');
										   $rootScope.stopLoader();
										}else if(response.data.status == "error")
										{
											$rootScope.stopLoader();
											growl.warning("There is no data available!",{
											title : ERROR_MSG
										});
									}
								});
								
							}
						});
						
						
						
						
					}
					//End For Claim

			
			//-----------------payment Collection----------------//
			$scope.initPaymentDetails=function(){
				$scope.paymentDetails={
						"paymentRecieptDetailsId":"",
						"paymentModeId"			:1,
						"paymentModeName"		:"Cash",
						"amount"				:"",
						"bankId"				:"",
						"bankName"				:"",
						"accountNumber"			:"",
						"checkNumber"			:"",
						"cardNo"				:"",
						"cardHolderName"		:"",
						"dateOfTransaction"		:new Date().getTime(),
						"expiryDate"			:"",
						"paymentVoucherNo"		:"",
						"cvv"					:"",
						"unitId"				:$scope.unitId,
						"orgnisationId"			:$scope.organizationId,
						"status"				:"A",
						"createdDate"			:new Date().getTime(),
						"createdBy"				:$scope.userId,
						"updatedDate"			:new Date().getTime(),
						"updatedBy"				:$scope.userId,
						"chequeDate"			:"",
						"cardTypeId"			:"",
						"isCancle"				:"I",
						"loyaltyCardAmt"        :'',
						"cardBalAmount"         :'',
						"loyaltyCardNo"         :'',
						'otp'                   :'',
						'hospitalAccountId'     : '',
						'transactionNo'         : ""
				}
			};
			$scope.initPaymentDetails();
			
			$scope.paymentModes = {
					1: "Cash",
					2: "Cheque",
					3: "Credit Card",
					4: "E - Transfer",
					5: "M-Pesa",
					6: "Loyalty Card",
					8: "E-Transfer"	
			}
			$scope.addBtnId=false;
			$scope.setPaymentModeId=function(paymentModeId){
				
				if(paymentModeId==3 && $scope.isPinPadRequired =='Y'){
					$scope.addBtnId=true;
				//	 $scope.pinpadAmounFlag =true;
				}else if(paymentModeId==6){
					$scope.addBtnId=true;
				}
				else{
					$scope.addBtnId=false;
					 $scope.pinpadAmounFlag =false;
				}
				$scope.paymentDetails.paymentModeId=paymentModeId;
				$scope.paymentDetails.paymentModeName=$scope.paymentModes[paymentModeId];
			}
			
			$scope.setBankName=function(bankId){
				angular.forEach($scope.bankList, function (value, key) {
					if(value.bankId == bankId){
						$scope.paymentDetails.bankName = value.bankName;
						return;
					}
				});
				
			}
			
			$scope.getBankList = function(){
				var obj={
						"unitId":$scope.unitId,
						"orgnisationId" : $scope.organizationId
				}
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + API + BILLING + "/bankMaster";
				BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						$scope.bankList = response.data.listObject;
					}
				});
			}
			
			$scope.getBankList();
			
			$scope.getCardTypeList = function(){
				var obj={
						"unitId":$scope.unitId,
						"orgnisationId" : $scope.organizationId
				}
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + API + BILLING + "/cardTypeMaster";
				BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						$scope.cardTypeList = response.data.listObject;
					}
				});
			}
			$scope.getCardTypeList();
			
			
			//Ganesh Start
			$scope.calculateTotalDueRefAmount = function()
			{
				var amount = 0;
				var dueAmount = $rootScope.parseFloatNum($scope.dueAmount);
				$scope.consumedAmt = 0;
				var payeeDueAmtFinal = $rootScope.parseFloatNum($scope.payeeDueAmtFinal);
				
				angular.forEach($scope.listPaymentDetails,function(value,key){
					amount = amount + parseFloat(value.amount); 
				});
				
				if($scope.availableDeposits!=null &&$scope.availableDeposits.length>0)
				{
					angular.forEach($scope.availableDeposits,function(value,key)
							{
								if(value.depositeFlag=='Y')
									{
									  $scope.consumedAmt = $scope.consumedAmt + parseFloat(value.consumedDeposit- value.oldConsumedDeposit); 
									}
							});
				}
				var tempDueAmt = parseFloat($scope.payeeDueAmtStatic) - parseFloat(amount) - parseFloat($scope.consumedAmt);
				var temppayeeDueAmtFinal = $scope.payeeDueAmtStatic - amount - $scope.consumedAmt;
				$scope.payeeDueAmtFinal = temppayeeDueAmtFinal >= 0 ? temppayeeDueAmtFinal : 0;
				$scope.payeeDueAmtFinal = parseFloat($scope.payeeDueAmtFinal).toFixed(2);
				var df = (parseFloat($scope.payeeDueAmtStatic) - (parseFloat(amount) + parseFloat($scope.consumedAmt)));
				if(df > 0)
					{
					$scope.dueAmount = tempDueAmt >= 0 ? tempDueAmt : 0;
					$scope.paymentDetails.cashAmount = $rootScope.parseFloatNum($scope.dueAmount);
				    $scope.paymentDetails.chequeAmount = parseFloat($scope.dueAmount).toFixed(2);
				    $scope.paymentDetails.credAmount = parseFloat($scope.dueAmount).toFixed(2);
				    $scope.eTransferObj.transferAmount= parseFloat($scope.dueAmount).toFixed(2);
				    $scope.paymentDetails.loyaltyCardAmt =  parseFloat($scope.dueAmount).toFixed(2);
				    $scope.razorPayObject.razorAmount =  parseFloat($scope.dueAmount).toFixed(2);
					}
				else
					{
					$scope.dueAmount = parseFloat(0.00).toFixed(2);
					$scope.paymentDetails.cashAmount = parseFloat($scope.dueAmount).toFixed(2);
				    $scope.paymentDetails.chequeAmount = parseFloat($scope.dueAmount).toFixed(2);
				    $scope.paymentDetails.credAmount = parseFloat($scope.dueAmount).toFixed(2);
				    $scope.eTransferObj.transferAmount= parseFloat($scope.dueAmount).toFixed(2);
				    $scope.paymentDetails.loyaltyCardAmt = parseFloat($scope.dueAmount).toFixed(2);
				    $scope.razorPayObject.razorAmount =  parseFloat($scope.dueAmount).toFixed(2);
					}
				$scope.refundAmount = temppayeeDueAmtFinal >= 0 ? 0 : Math.abs(temppayeeDueAmtFinal);
				$scope.amountCollected = amount;
				
				if($scope.consumedAmt > 0)
					{
					  $scope.depositeSettleBtnFlag = false;
					}
				else
					{
					$scope.depositeSettleBtnFlag = true;
					}
			}
			
			$scope.changePayee = function(payeeId){
				$scope.listPaymentDetails = [];
				//alert("bill.outstandingChabge"+$scope.dueAmount);
				
				$scope.calculateTotalDueRefAmount();
				$scope.payeeId = payeeId;
				if(payeeId > $scope.selfPayeeIdCookies)
					{
					  $scope.billDisabled = true;
					}
				else
					{
					$scope.billDisabled = false;
					}
			}
			/******21-05-19*Chetana****/
			
			$scope.changeToDate = function()
			{
				$scope.dateOptionsTo.minDate = new Date($scope.billSearch.fromDate);
				$scope.billSearch.toDate = new Date($scope.billSearch.fromDate);
					 
			}
			
			$scope.checkDateValid = function(){
				
				//if($scope.pendingAdmissionList[index].datepickerPdd == undefined)
				if(!angular.isDate($scope.billSearch.fromDate))
					{
						growl.warning("From date is invalid",{
							title : WARNING_MSG
						});
						$scope.billSearch.fromDate= new Date();
						$scope.billSearch.toDate= new Date();
						$scope.dateOptionsTo.minDate = new Date()
						return false;
					}
			}
			
				$scope.checkDateValidTo = function(){
				
				//if($scope.pendingAdmissionList[index].datepickerPdd == undefined)
				if(!angular.isDate($scope.billSearch.toDate))
					{
						growl.warning("To date is invalid",{
							title : WARNING_MSG
						});
						$scope.billSearch.toDate= new Date($scope.billSearch.fromDate);
						return false;
					}
			}
			
			$scope.getSignBinaryData = function()
			{
				//$("#id_description_iframe").contents().find("body").html()
				
				 var binaryValueData = $('iframe[name=Demo]').contents().find('#finalDataBinary').val();
				 $scope.binaryValueDataForSing = binaryValueData;
				 
				 angular.element('#digitalSignaturePopup').modal('hide');
			}
			
			
			$scope.uploadEncounterDoc = function(Obj)
			{
				 var fd = new FormData();
			        fd.append('organizationId', $scope.organizationId);
			        fd.append('billId',Obj);
			        fd.append('isDeposite',false);
			        fd.append('isPharmacy',false);
			        fd.append('isMiscellaneous',false);
			        fd.append('unitId', $scope.unitId);
			        fd.append('documentName',$scope.fileObj.file.name);
			        fd.append('file',$scope.fileObj.file );
			        var URI1 = BASE_URL + ROOT_URL + "/billing/PanCard/uploadDoc";
			        return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
					{	
			        	$scope.fileObj = {};
						$scope.fileObj.file = null;
						angular.element("input[type='file']").val(null);
						return response;
						
					});
			}	
			
			
			/***********/
			// NEW FOR VIVEK
			$scope.settleBill = function(){
				if(this.amountReceivedFrom!=undefined){
					$scope.receivedFrom = this.amountReceivedFrom;
					this.amountReceivedFrom = "";
				}
				if(this.identificaitonNumber!=undefined){
					$scope.identificationNumber = this.identificaitonNumber; 
					this.identificaitonNumber = "";
				}
				
				
				var collectedAmount = parseFloat(0).toFixed();
				angular.forEach($scope.listPaymentDetails,function(value,key){
					value.receivedFrom=$scope.listPaymentDetails.amountReceivedFrom!=undefined ? $scope.listPaymentDetails.amountReceivedFrom :($scope.listPaymentDetails.amountReceivedFrom!=undefined ? $scope.listPaymentDetails.amountReceivedFrom:'');
					$scope.receivedFrom=value.receivedFrom;
					if(value.paymentModeId == 1){
						collectedAmount = parseFloat(collectedAmount) + parseFloat(value.amount);
					}
				});
				$scope.receivedFrom=$scope.listPaymentDetails.amountReceivedFrom;
				if($scope.cashAmountValidation != null && collectedAmount >=$scope.cashAmountValidation && $scope.fileObj.file == null){
					$('#uploadpanCardDetailsIPD').modal('show');
						return false;
				}
				$('#uploadpanCardDetailsIPD').modal('hide');
				$('#collectPayment').modal('hide');
				$('#billListDetails').modal('hide');
				$rootScope.startLoader();
				$scope.billDisabled = true;
				console.log("LISt CHECK",$scope.availableDeposits);
				
				var billPayeeDetailsId = "";
				var payeeDueAmount = 0;
				//Get Bank Name
				angular.forEach($scope.payeeList,function(value,key){
					if(value.payeeId == $scope.selfPayeeIdCookies)
						{
						billPayeeDetailsId = value.billPayeeDetailsId;
					    payeeDueAmount = value.dueAmount;
						}
				})
				
				$scope.finalAvailableDeposits = [];
				var i = 0;
				angular.forEach($scope.availableDeposits,function(value,key){
					if(value.depositeFlag == 'Y')
						{
						//availableDeposit : A
						//depositAmount : A
						//depositMasterId : A 
						//organizationId : A 
						//unitId:A
						//depConsumptionDate: currentDate
						//billId : V
						//billPayeeDetailsId : V
						//consumedAmount : consumedDeposit
						var newArr = {};
						newArr.availableDeposit = $scope.availableDeposits[i].availableDeposit;
						newArr.depositAmount = $scope.availableDeposits[i].depositAmount;
						newArr.depositMasterId =$scope.availableDeposits[i].depositMasterId;
						newArr.remark =$scope.availableDeposits[i].remark;
						newArr.organizationId  =  $scope.organizationId;
						newArr.unitId = $scope.unitId;
						newArr.depConsumptionDate =moment(new Date()).format('DD-MM-YYYY HH:MM:SS'); 
						newArr.billId = $scope.billId;	
						newArr.billPayeeDetailsId = billPayeeDetailsId;
						newArr.consumedAmount =  (parseFloat($scope.availableDeposits[i].consumedDeposit).toFixed(2) - $scope.availableDeposits[i].oldConsumedDeposit);
						newArr.createdBy = $scope.userId;
						$scope.finalAvailableDeposits.push(newArr);
						//$scope.finalAvailableDeposits.push($scope.availableDeposits[i]);
						}
					i++;
					
				});
				console.log("$scope.finalAvailableDeposits",$scope.finalAvailableDeposits);
				console.log("$scope.listPaymentDetails",$scope.listPaymentDetails);
				
				
				//return false;
				var param = {
						
						totalSelfPayable : $scope.totalSelfPayable,
						totalCreditPayable : $scope.totalCreditPayable,
						doctorId : $scope.doctorId,
						isInterimOrFinalBill : $scope.isInterimOrFinalBill,
						billingMasterId : $scope.billId,
						billPayeeDetailsId : billPayeeDetailsId,
						dateOfReciept : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
						payeeId : $scope.payeeId,
						//listBillingPaymentDetailsReqDto : $scope.listPaymentDetails,
						paymentReceiptDetailsDtosList : $scope.listPaymentDetails,
						orgnizationId : $scope.organizationId,
						unitId : $scope.unitId,
						createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
						updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
						updatedBy:$scope.userId,
						createdBy:$scope.userId,
						receiptType:1,
						receivedFrom:$scope.receivedFrom,
						identificationNumber : $scope.identificationNumber,
						remark : "",
						amount : (parseFloat($scope.amountCollected) + parseFloat($scope.consumedAmt)).toFixed(2) ,
						//consumedAmount : $scope.consumedAmt,
						//totalCollection : $scope.amountCollected,
						totalDepositConsumption : $scope.consumedAmt,
						isPreOrPostBilling:$scope.isPreOrPostBilling,
						isFinancialClearance : $scope.isFinancialClearance ? 'Y' : 'N',
						appointmentId:$scope.appointmentId,
						encounterId : $scope.encounterId,
						counterId : $rootScope.counterId,
						paymentDepositDetailsList : $scope.finalAvailableDeposits,
						payeeDueAmount :parseFloat(payeeDueAmount).toFixed(2),
						admissionId : $scope.admissionId,
						signetureImage:$scope.binaryValueDataForSing,
						isTransferToActiveDeposit : this.transferToActiveDeposit,
						patientId : $scope.patientIdForReport,
						visitTypeId : $scope.visitTypeId,
						referalDocId : $scope.referalDocId,
						isNursingAssessmentRequired : $scope.isNursingAssessmentRequired,
						disallowedAmount : $scope.disallowedAmount,
						disallowedAgainstInvoiceDtlsId : $scope.disallowedAgainstInvoiceDtlsId,
						outstanding : $scope.oldDueAmount,
						isPaidAppointment : $scope.isPaidAppointment
				}
				
				if($scope.payeeId == '' || typeof($scope.payeeId) == "undefined"){
					$scope.billDisabled = false;
					growl.warning("Please select at least one payee..!!",{
						title : 'Warning!'
					});
					return;
				}
				
				if(($scope.finalAvailableDeposits.length == 0)&&($scope.listPaymentDetails.length == 0 || $scope.listPaymentDetails == 'undefined')){
					$scope.billDisabled = false;
					growl.warning("Please select at least one payment mode..!!",{
						title : 'Warning!'
					});
					return;
				}
				
				if(($scope.amountCollected == '' || $scope.amountCollected <= 0) && ($scope.consumedAmt == '' || $scope.consumedAmt <= 0)){
					$scope.billDisabled = false;
					growl.warning("Amount can't be zero..!!",{
						title : 'Warning!'
					});
					return;
				}
				
				console.log("param",param);
				//return false;
				
				var URI = BASE_URL + ROOT_URL + API + BILLING + "/payments";
				BillingGenericService.serviceAction(METHOD_POST,URI, param, NOTIFICATION_MSG_STATUS_TRUE).then(function(response)
				{
					if(response.data.status == "success"){
						//collectPayment
						//billListDetails
						$scope.billDisabled = false;
						$scope.validatTextFieldFlag = true;
						if($scope.fileObj.file != null){
							$scope.uploadEncounterDoc(response.data.object.billId);
						}
						$scope.excessAmount = parseFloat(0).toFixed(2);
						$scope.transferToActiveDeposit = 'N'
						this.transferToActiveDeposit = "N";
						$scope.billDetails($scope.currentBill);
						$scope.calculateTotalDueRefAmount();
						$scope.listPaymentDetails = [];
						$scope.mpesaObject={};
						$scope.receivedFrom = "";
						$scope.identificaitonNumber = "";
						$scope.searchBills($scope.billSearch);
						$scope.receiptId=response.data.object.recieptId;
						$scope.depositConsumptionId=response.data.object.depositConsumptionId;
						$scope.processDocShare();
						console.log(response);
						if(response.data.object != null)
						{
							var URIVALI = BASE_URL + ROOT_URL + "/api/billing/receipt/printLog/0/"+$scope.userId+"/"+response.data.object.recieptId;
							BillingGenericService.serviceAction(METHOD_GET,URIVALI, {},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
							{
								$rootScope.stopLoader();
								if (response.data.status == "success")
								{
									var data =  
									  {
											  
							                 "appName": "HIS",
							                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
							                 "reportDesignFilePath": "OPDPaymentReceipt.rpt",
							                 "reportFormat": "PDF",
							                 "exportFileName": "PaymentReciept",
							                 "reportParams": [
											                 	{
													                  "paramName": "@BillID",
													                  "paramType": "Integer",
													                  "paramValue": $scope.currentBill.billingMasterId
												                },
												                {
													                  "paramName": "@PaymentID",
													                  "paramType": "Integer",
													                  "paramValue": response.data.object.recieptId
												                },
												                {
													                  "paramName": "@dep_consumption_id",
													                  "paramType": "Integer",
													                  "paramValue": (response.data.object.depositConsumptionId!=null ? response.data.object.depositConsumptionId :0)
												                },
												                {
													                  "paramName": "@isDuplicate",
													                  "paramType": "Integer",
													                  "paramValue": 0
													            }
												              ]
									  };
									reportService.generate(REPORT_API_URI1,data);
								}
							});
							
							
							}
				}else{
						$rootScope.stopLoader();
						$scope.searchBills($scope.billSearch);
				}
				});
			}
			
			/*$scope.processDocShare = function() {
				  $rootScope.startLoader();
				    var URI = BASE_URL + ROOT_URL + "/api/billing/process/doctorShare/"+$scope.unitId
				    BillingGenericService.serviceAction(METHOD_GET,URI,"",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success"){
							$rootScope.stopLoader();
						}
						
					});
				  }*/
			
			
			$scope.closePopup = function(){
				angular.element('#outsourcedserviceworkorderPopup').modal('hide');
			}
			
			
			/*Vivek start*/
			
			
			$scope.directCollectPayment = function(bill){
				
				$scope.billDetails(bill);
				
				$scope.getBillFundDetailsByMasterId(bill);
				
				  $timeout( function(){
					  $scope.settlementClick();
	            },1000);
				  
				
			}
			
			
			$scope.settlementClick=function()
			{
				setTimeout(function(){
		           	 $('#setCash').trigger('click')
		            }, 300);
				$scope.flagForSMSpay = false;
				$scope.flagForBharatQr = false;
				$scope.flagForSale = false;
				$scope.smsPay = {};
				$scope.bharatQr = {};
				$scope.sale = {};
				$scope.objPosMachine.deviceName = "";
				$scope.objPosMachine.isSale = false;
				$scope.objPosMachine.isBharatQr = false;
				$scope.objPosMachine.isSMSpay = false;
//				$scope.setFirstTabLevel(1);
				
				if($scope.visitTypeId == 1){
					if($scope.billSettlement  == false){
						growl.warning("user do not have bill settlements rights",
								{
									title : 'Error!'
								});
					    
						return false;
					}
				}
				
				if($scope.visitTypeId == 2){
					if($scope.ipBillSettlement  == false){
						growl.warning("user do not have bill settlement rights",
								{
									title : 'Error!'
								});
					
						return false;
					}
				}
				if($scope.payeeId > 0)
					{
					     $scope.setFirstTabLevel(1);
						 $scope.transferToActiveDeposit = 'N'
						 $scope.excessAmount = parseFloat(0).toFixed(2);
						 $scope.send=false;
						 $scope.resend=false;
						 $scope.validatTextFieldFlag = true;
						 $scope.receivedFrom = "";
						$scope.identificationNumber = "";
						 $scope.setPaymentModeId(1);
						 $scope.calculateTotalDueRefAmount();
						 $scope.getLoyaltyCardList($scope.selectedPatientId);
						 $scope.paymentDetails.cashAmount = parseFloat($scope.payeeDueAmtFinal).toFixed(2);
							angular.element('#collectPayment').modal('show');
							
							/*angular.forEach($scope.payeeList,function(value,key){
								if(value.payeeId == '1')
									{
								      $scope.payeeDueAmtFinal = value.dueAmount;
									}
							})*/
							
					}
				else
					{
						growl.warning("Please select payer..!!",{
							title : WARNING_MSG
						});
						return false;
					}
			}
			/**********************/
			$scope.printBill = function(bill,IsSummary,printType)
			{
				
				$("#bill").show();
				$scope.billingMasterId=bill.billingMasterId;
				$scope.patientId=bill.patientId;
				$scope.visitTypeId=bill.visitTypeId;
				$scope.IsSummary=IsSummary;
				$scope.printType=printType; // 0 - original 1- Duplicate
				
			}
			$scope.printBillNew=function()
			{
				$rootScope.startLoader();
				var URIVALI = BASE_URL + ROOT_URL + "/api/billing/printLog/"+$scope.printType+"/"+$scope.userId+"/"+$scope.billingMasterId;
				BillingGenericService.serviceAction(METHOD_GET,URIVALI, {},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
				{
					
					angular.forEach($scope.billList,function(value,key)
					{
						if(value.billingMasterId==$scope.billingMasterId)
							value.isPrint='Y'
					});
					$rootScope.stopLoader();
					if (response.data.status == "success")
					{
						if($scope.visitTypeId==1)
						{
						var data =  {
				                 "appName": "HIS",
				                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
				                 "reportDesignFilePath": "opdbillreceipt_listview.rpt",
				                 "reportFormat": "PDF",
				                 "exportFileName": "PatientBill",
				                 "reportParams": [
				                   {
					                  "paramName": "@billid",
					                  "paramType": "Integer",
					                  "paramValue": $scope.billingMasterId
				                   },
				                   {
					                  "paramName": "@isDuplicate",
					                  "paramType": "Integer",
					                  "paramValue": $scope.printType!=null ? $scope.printType : 0
					               }
				                 ]
				      };
						}else{
							var data =  {
						  			
					                 "appName": "HIS",
					                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
					                 "reportDesignFilePath": "Ipdbill_listview.rpt",
					                 "reportFormat": "PDF",
					                 "exportFileName": "IPDBillReciept",
					                 "reportParams": [
					                	{
							                  "paramName": "Billid",
							                  "paramType": "Integer",
							                  "paramValue": $scope.billingMasterId
						                },
						                {
							                  "paramName": "@patient_id",
							                  "paramType": "Integer",
							                  "paramValue": $scope.patientId
						                },
						                {
							                  "paramName": "IsSummary",
							                  "paramType": "String",
							                  "paramValue": $scope.IsSummary
						                },
						                {
							                  "paramName": "@isDuplicate",
							                  "paramType": "Integer",
							                  "paramValue": $scope.printType!=null ? $scope.printType : 0
							            }
			                ]
							};
						}
						reportService.generate(REPORT_API_URI1,data);
						$("#bill").hide();
					}
					
				});
				
			}
			
			
			$scope.getCashCollectedAmount = function(visitId,id){

				$rootScope.startLoader();
					
					var data = {};
					
					if(visitId ==1 ){
						data.encounterId = id;
					}else{
						data.admissionId = id;
					}
					
					var URI = BASE_URL + ROOT_URL + "/api/billing/cashPaidAmount";
					BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success")
						{
								$rootScope.stopLoader();
								$scope.cashCollectedAmount = response.data.object;
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							$scope.cashCollectedAmount = 0;
						}
			
					});
				
			}

			
			
			//Ganesh Start
			
		
			$scope.addBtnDisabled = false;
			$scope.depositeSub = function(item,idx)
			{
				 $("#userEnterDeposit").modal("hide");
				 
				if($scope.payeeDueAmtFinal > 0 || item.depositeFlag == 'N'){
				var consumedAmt = 0;
				if(item.depositeFlag == 'Y')
					{
					
					 if(item.availableDeposit == 0 || item.availableDeposit == '00' || item.availableDeposit < 0 || parseFloat($scope.DepositObj.userEnterDepositAmount) > parseFloat(item.availableDeposit)){
							growl.warning("Available deposit amount shuold be greater than user enter amount.",{
								title : WARNING_MSG
							});
							return false;
						}
					 $scope.availableDeposits[idx].remark = $scope.DepositObj.remark;
						if($scope.dueAmount == 0)
						{
							$scope.availableDeposits[idx].depositeFlag = 'N';
						}
						else if($scope.dueAmount >= item.availableDeposit)
						{
							$scope.availableDeposits[idx].consumedDeposit  = parseFloat(parseFloat($scope.availableDeposits[idx].consumedDeposit)  + parseFloat($scope.DepositObj.userEnterDepositAmount)).toFixed(2)// parseFloat(userEnterDepositAmount);
							$scope.availableDeposits[idx].availableDeposit = ($scope.availableDeposits[idx].availableDeposit) - parseFloat($scope.DepositObj.userEnterDepositAmount).toFixed(2);
							$scope.dueAmount = (parseFloat($scope.dueAmount)) - parseFloat($scope.DepositObj.userEnterDepositAmount).toFixed(2);
						}
					else if($scope.dueAmount <= item.availableDeposit)
						{
							$scope.availableDeposits[idx].availableDeposit = parseFloat(item.availableDeposit)- parseFloat($scope.DepositObj.userEnterDepositAmount).toFixed(2);
							$scope.availableDeposits[idx].consumedDeposit =parseFloat(parseFloat(item.consumedDeposit) + parseFloat($scope.DepositObj.userEnterDepositAmount)).toFixed(2);
							$scope.dueAmount = (parseFloat($scope.dueAmount))- parseFloat($scope.DepositObj.userEnterDepositAmount).toFixed(2);
						}
						$scope.DepositObj.remark=""; 
						$scope.addBtnDisabled = true;
					}
				if(item.depositeFlag == 'N')
				{
					$scope.availableDeposits[idx].remark = $scope.DepositObj.remark;
							if($scope.availableDeposits[idx].availableDeposit > 0)
								{
								$scope.availableDeposits[idx].availableDeposit = parseFloat(item.oldAvailableDeposit).toFixed(2);
								$scope.availableDeposits[idx].consumedDeposit = parseFloat(item.oldConsumedDeposit);
								$scope.dueAmount = (parseFloat($scope.dueAmount)+ parseFloat(item.oldAvailableDeposit-item.availableDeposit)).toFixed(2);
								}
							else{
							$scope.availableDeposits[idx].availableDeposit =  parseFloat($scope.availableDeposits[idx].availableDeposit + parseFloat(item.consumedDeposit-item.oldConsumedDeposit)).toFixed(2);
							$scope.availableDeposits[idx].consumedDeposit = parseFloat(item.oldConsumedDeposit);
							$scope.dueAmount = (parseFloat($scope.dueAmount)+ parseFloat($scope.availableDeposits[idx].availableDeposit)).toFixed(2);
							}
							
							
							var totalListamount = 0;
							angular.forEach($scope.listPaymentDetails,function(value,key)
									{
								totalListamount = totalListamount+parseInt(value.amount);
									});

							if(parseInt($scope.totalAmount) >=totalListamount){
								
								$scope.excessAmount = parseFloat(0).toFixed(2);
							}else{
								var dummyAmount = parseFloat(totalListamount).toFixed(2) - parseFloat($scope.totalAmount).toFixed(2);
								$scope.excessAmount = parseFloat(dummyAmount).toFixed(2);
							}
							$scope.DepositObj.remark="";		
				}
				
				var avalForFlag = false;
				var i = 1;
				angular.forEach($scope.availableDeposits,function(value,key){
					if(value.depositeFlag == 'Y')
						{
						avalForFlag = true;
						}else
							{
								if($scope.transferToActiveDepositeFlag == false)
									{
									avalForFlag == false;
									}
							}
					if(i == $scope.availableDeposits.length)
						{
						$scope.transferToActiveDepositeFlag  = avalForFlag;
						}
					i++;
					
				})
				
				$scope.calculateTotalDueRefAmount();
				
			}
				else
				{
					growl.warning("Due amount is zero(0).",{
						title : WARNING_MSG
					});
					$scope.availableDeposits[idx].depositeFlag = 'N';
					return false;
				}
				 
				
			}
			
			$scope.depositeObject = undefined;
			$scope.depositeIndex = undefined;
			$scope.depositeSubPopup = function(item,idx){
				$scope.DepositObj.userEnterDepositAmount = 0;
				if(item.depositeFlag == 'Y' && $scope.dueAmount > 0){
					$scope.addBtnDisabled = false;
					if($scope.dueAmount > item.availableDeposit){
						$scope.DepositObj.userEnterDepositAmount = item.availableDeposit;
					}else{
						$scope.DepositObj.userEnterDepositAmount = parseFloat($scope.dueAmount).toFixed(2);
					}
					
					$scope.depositeObject = item;
					$scope.depositeIndex = idx;
					 $("#userEnterDeposit").modal("show");
					 
				}else{
					$scope.depositeSub(item,idx);
					 $("#userEnterDeposit").modal("hide");
				}
				
			}
			
			$scope.closeDepositPop = function(){
				$scope.availableDeposits[$scope.depositeIndex].depositeFlag = 'N';
				 $("#userEnterDeposit").modal("hide");
			}
			
			$scope.checkEnterDepositeAmount = function(){
				
				if(parseFloat($scope.DepositObj.userEnterDepositAmount) > parseFloat($scope.depositeObject.availableDeposit)){
					growl.warning("Enter amount should not be greater available deposit ",{
						title : WARNING_MSG
					});
					$scope.DepositObj.userEnterDepositAmount =0;
					return;
				}
				
				if(parseFloat($scope.DepositObj.userEnterDepositAmount) > parseFloat($scope.dueAmount)){
					growl.warning("Enter amount should not be greater due amount",{
						title : WARNING_MSG
					});
					$scope.DepositObj.userEnterDepositAmount =0;
					return;
					
				}
				
			}
			
			$scope.depositeEnableDisabled = function(){
				if($scope.transferToActiveDeposit == 'Y')
					{
					  $scope.depositeDisabledFlag = true;
					}
				else
					{
					  $scope.depositeDisabledFlag = false;
					}
			}
			
			//Ganesh Start
			$scope.checkAciveDepo  = function(useActiveDepo)
			{
				 $scope.activeDeposit = 0;
				if(useActiveDepo == true)
					{
					   
						var i = 0;
						angular.forEach($scope.availableDeposits,function(value,key)
						{
							if(value.depositTypeId == '1')
						   {
							   $scope.activeDeposit = $scope.activeDeposit + parseFloat(value.availableDeposit);
							   $scope.availableDeposits[i].depositeFlag = 'N';
							   if(value.depositeFlag == 'N')
							   {
								   value.consumedDeposit = value.oldConsumedDeposit;
								   value.availableDeposit = value.oldAvailableDeposit;
							   }
							}else{
								$scope.activeDeposit = $scope.activeDeposit + parseFloat(value.depositAmount);
							}
							
							i++;
						});
					}
				else
					{
					var i = 0;
					var consumedAmt= 0;
						angular.forEach($scope.availableDeposits,function(value,key)
						{
							if(value.depositTypeId == '1')
							{
								if(value.depositeFlag == 'Y')
								{
									consumedAmt = consumedAmt - (value.consumedDeposit);
								}
								$scope.availableDeposits[i].availableDeposit = $scope.availableDeposits[i].oldAvailableDeposit;
								$scope.availableDeposits[i].consumedDeposit =$scope.availableDeposits[i].oldConsumedDeposit
								$scope.activeDeposit = $scope.activeDeposit + parseFloat(value.oldAvailableDeposit);
								$scope.availableDeposits[i].depositeFlag = 'N';
							
							}else{
									$scope.activeDeposit = $scope.activeDeposit + parseFloat(value.oldAvailableDeposit);
							}
							$scope.dueAmount = (parseFloat($scope.dueAmount)+ parseFloat(consumedAmt)).toFixed(2);
							i++;
						});
					}
				$scope.calculateTotalDueRefAmount();
			}
			/*Ganesh end*/
			
			
			$scope.closeDigitaslSig = function()
			{
				 angular.element('#digitalSignaturePopup').modal('hide');
			}
			
			$scope.validFclearance = function(flag){
				var amount = 0;
				$scope.listPaymentDetails.forEach((elem) => {amount = parseFloat(amount) + parseFloat(elem.amount)});
				
				if(($scope.currentBill.amountPaid > 0 || amount > 0) && flag ){
					$scope.isFinancialClearance = true;
					$('#isFinancialClearance').prop('checked',true);
				}else if(($scope.currentBill.amountPaid <= 0 || amount <=0) && flag){
					$scope.isFinancialClearance = false;
					$('#isFinancialClearance').prop('checked',false);
					growl.error("Please pay some amount to get financial clearance",{
						title : ERROR_MSG
					});
				}else{
					$scope.isFinancialClearance = false;
					$('#isFinancialClearance').prop('checked',false);
				}
			}
			
			
			//Check isActiveSelected
			  var isActiveSelected;
				function checkActiveSelected(arr) {
					return arr
							.some(function(arr) {
								if(arr.depositeFlag === 'Y')
									{
										return true;
									}
								else
									{
										return false;
									}
							});
				}
				
			$scope.isFinancialClearanceCheck = {};	
			$scope.addPaymentDetails = function(){
				$scope.isBillList = 0;
				var exAmount = '';
				
				var cashLimit = 0;
				angular.forEach($scope.listPaymentDetails,function(value,key){
					
					if(value.paymentModeId == 1){
						cashLimit = parseFloat(cashLimit) + parseFloat(value.amount);
					}
				});
				
				if($scope.paymentDetails.paymentModeId == 1){
					cashLimit = cashLimit + parseFloat($scope.paymentDetails.cashAmount);
					cashLimit = cashLimit + $scope.cashCollectedAmount;
				}
				
				if(($scope.cashAmountLimit != undefined && $scope.cashAmountLimit !=null ) && cashLimit >  $scope.cashAmountLimit){
					growl.warning("System can not accept more than "+$scope.cashAmountLimit +" in cash amount",{
						title : WARNING_MSG
					});
					return;
				}
				
				var trancastionId = false;
				angular.forEach($scope.listPaymentDetails,function(value,key){
					
					if(value.paymentModeId == 12 && value.transactionNo == $scope.razorPayObject.razorUniqueId){
						trancastionId =true;
					}
				});
				
				if(trancastionId){
					growl.warning("Trancastion id should not be same",{
						title : WARNING_MSG
					});
					return;
				}
				
				if($scope.paymentDetails.paymentModeId == 1){
					
					exAmount = $scope.paymentDetails.cashAmount;
					
				}else if($scope.paymentDetails.paymentModeId == 2){
					
					exAmount =  $scope.paymentDetails.chequeAmount;
					
				}else if($scope.paymentDetails.paymentModeId == 3){
					
					exAmount = $scope.paymentDetails.credAmount;
					
				}else if($scope.paymentDetails.paymentModeId == 4){
					
					exAmount = $scope.paymentDetails.debAmount;
					
				}else if($scope.paymentDetails.paymentModeId == 5){
					
					exAmount = $scope.mpesaObject.mpesaAmount;
					
				}else if($scope.paymentDetails.paymentModeId == 6){
					
					exAmount = $scope.paymentDetails.loyaltyCardAmt;
					
				}else if($scope.paymentDetails.paymentModeId == 8){
					
					exAmount = $scope.eTransferObj.transferAmount;
				}else if($scope.paymentDetails.paymentModeId == 12){
					
					exAmount = $scope.razorPayObject.razorPaidAmount;
				}
				if(parseFloat(exAmount) <= parseFloat($scope.payeeDueAmtFinal))
				{
						$scope.transferToActiveDepositFact = this.transferToActiveDeposit;
						paymentCollFactory.addPaymentDetails($scope);
				}else{
					
					
					isActiveSelected=checkActiveSelected($scope.availableDeposits)
					  if (!isActiveSelected) {
						  if(exAmount == undefined || exAmount == '' || exAmount == 0 || exAmount == null){
							  growl.warning("Amount should be greater than ZERO(0)..!! ",{
									title : WARNING_MSG
								});
								return false;
						  }
						  else{
							  var fixAmount = parseFloat(exAmount).toFixed(2) - parseFloat($scope.payeeDueAmtFinal).toFixed(2); 
							  $scope.excessAmount = parseFloat(fixAmount).toFixed(2);
	                          $scope.isFinancialClearanceCheck = document.getElementById('isFinancialClearance_check');
	                          if($scope.isFinancialClearanceCheck!=undefined && $scope.isFinancialClearanceCheck!=null &&
	                        		   $scope.isFinancialClearanceCheck.checked == true){
	                        	  $("#confirmSave").modal("show");
	                          }else{
	                        	  if(fixAmount>0)
	      						{
	      						  if($scope.exceededBillAmountSettlement==true)
	      							  {
	      							  $("#excessAmountDepositPopup").modal("show");
	      							  }
	      						  else
	      							  {
	      							    growl.warning("Entered amount is greater than current amount");
	      							    $scope.excessAmount = parseFloat(0.0).toFixed(2);
	      							    $scope.paymentDetails.chequeAmount= parseFloat($scope.dueAmount).toFixed(2);
	      								$scope.paymentDetails.cashAmount = parseFloat($scope.dueAmount).toFixed(2);
	      								$scope.paymentDetails.credAmount = parseFloat($scope.dueAmount).toFixed(2);
	      								$scope.paymentDetails.loyaltyCardAmt = parseFloat($scope.dueAmount).toFixed(2);
	      								$scope.mpesaObject.mpesaAmount = parseFloat($scope.dueAmount).toFixed(2);
	      								$scope.eTransferObj.transferAmount = parseFloat($scope.dueAmount).toFixed(2);
	      								$scope.razorPayObject.razorAmount = parseFloat($scope.dueAmount).toFixed(2);
	      							  }
	      						}
	                          }
							
						  }
					  }
					  else
						  {
						         $scope.paymentDetails.cashAmount = $scope.dueAmount;
								  growl.warning("You can select either transfer to active deposit or consume from deposit..!!",{
										title : WARNING_MSG
									});
									return false;
									
						  }
					
				}
			
				
			}
			var parentChild = 0;
			$scope.addExAmount = function(){
				$scope.isBillList = 0;
				if( parentChild == 0 ){
					$scope.transferToActiveDeposit = 'Y';
					$scope.transferToActiveDepositFact = $scope.transferToActiveDeposit;
				}else{
					
					$scope.transferToActiveDeposit = 'Y';
					$scope.transferToActiveDepositFact = $scope.transferToActiveDeposit;
				}
				
				paymentCollFactory.addPaymentDetails($scope);
				$("#excessAmountDepositPopup").modal("hide");
				parentChild += 1;
			}
			
			$scope.changeExAmount = function(){
				$scope.excessAmount = parseFloat(0.0).toFixed(2);
			}
			
			$scope.removePaymentDetails = function(index){
				paymentCollFactory.removePaymentDetails($scope,index);
				var amount = 0;
				$scope.listPaymentDetails.forEach((elem) => {amount = parseFloat(amount) + parseFloat(elem.amount)});
				if(amount <= 0 && $scope.currentBill.amountPaid <= 0){
					$scope.isFinancialClearance = false;
					$('#isFinancialClearance').prop('checked',false);
				}
				
				var tempObj = (index==undefined)?null:$scope.listPaymentDetails[index];
				if(tempObj!=null && (tempObj.cardNo == $scope.paymentDetails.loyaltyCardNo))
					{
					 $scope.paymentDetails.cardBalAmount = $scope.paymentDetails.cardBalAmount +parseFloat(tempObj.amount);
					 $scope.send=false;
					 $scope.resend=false;
					}
				
				var totalListamount = 0;
				angular.forEach($scope.listPaymentDetails,function(value,key)
						{
							totalListamount = totalListamount+parseInt(value.amount);
						});
				
				if(parseInt($scope.totalAmount) >=totalListamount){
					$scope.transferToActiveDeposit = 'N';
					$scope.excessAmount = parseFloat(0).toFixed(2);
				}else{
					var dummyAmount = parseFloat(totalListamount).toFixed(2) - parseFloat($scope.totalAmount).toFixed(2);
					$scope.excessAmount = parseFloat(dummyAmount).toFixed(2);
				}
			}
			
			//Thumb search
			
			$scope.thumbSearch = function()
			{
				 try {
					    $rootScope.startLoader();
			           	var quality = 60; //(1 to 100) (recommanded minimum 55)
			               var timeout = 10; // seconds (minimum=10(recommanded), maximum=60, unlimited=0 )
			               var res = CaptureFinger(quality, timeout);	
			               if (res.httpStaus) 
			               {
			            	   if (res.data.ErrorCode == "0") 
			                   {
			                       $scope.thumbImage = res.data.BitmapData;
			                       var URI = BASE_URL + ROOT_URL + "/api/global/searchByFingerPrint";
			                       
			                       var param = 
			                       {
			                    		   organizationId:$scope.organizationId,
			                    		   unitId:$scope.unitId,
			                    		   thumbImage:res.data.BitmapData
			                    		  }; 
			                       }
			            	   BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			           			$scope.searchPatientList = [];
			           			if (response.data.status == "success")
			           				{
			           				
			           				$scope.patientList = response.data.listObject;
			        				if($scope.patientList.length == 0){
			        					//alert();
			        					growl.warning("No Result Found!",{
			        						title : ERROR_MSG
			        					});
			        				}
			        				patSearchObj = {};
			           				
			           				$rootScope.stopLoader();
			           				}
			           			else if(response.data.status == "error"){
			           				$rootScope.stopLoader();
			           			}
			           			
			           		});
			                   
			               }
			               else 
			               {
			                   alert(res.err);
			                   $rootScope.stopLoader();
			               }
			           }
			           catch (e) {
			               alert(e);
			               $rootScope.stopLoader();
			           }
			}
			
			/********************Report***********************Added By Chetana*******************/
			
			$scope.rePrintOPDBillReceipt=function(index,isReprint){
				
				var URIVALI = BASE_URL + ROOT_URL + "/api/billing/receipt/printLog/"+isReprint+"/"+$scope.userId+"/"+$scope.paymentReceiptsDetailsList[index].receiptId;
				BillingGenericService.serviceAction(METHOD_GET,URIVALI, {},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
				{
					$rootScope.stopLoader();
					if (response.data.status == "success")
					{
						var data =  {
								  
				                 "appName": "HIS",
				                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
				                 "reportDesignFilePath": "OPDPaymentReceipt_listview.rpt",
				                 "reportFormat": "PDF",
				                 "exportFileName": "PaymentReciept",
				                 "reportParams": [
				                	 {
						                  "paramName": "@BillID",
						                  "paramType": "Integer",
						                  "paramValue": $scope.paymentReceiptsDetailsList[index].billingMasterId
					                },
					                {
						                  "paramName": "@PaymentID",
						                  "paramType": "Integer",
						                  "paramValue": $scope.paymentReceiptsDetailsList[index].receiptId
					                },
					                {
						                  "paramName": "@dep_consumption_id",
						                  "paramType": "Integer",
						                  "paramValue": ($scope.paymentReceiptsDetailsList[index].depositConsumptionId!=null ? $scope.paymentReceiptsDetailsList[index].depositConsumptionId : 0 )
					                },
					                {
						                  "paramName": "@isDuplicate",
						                  "paramType": "Integer",
						                  "paramValue": isReprint !=null ? isReprint : 1
						            }
					                ]
						};
						reportService.generate(REPORT_API_URI1,data);
					}
				});
				  
			}
				
			/***********************************************************************************/
			/* *************************************Mpesa Integration start****************************************** */
			$scope.mpesaObject={"transactionId":null,"mobileNo":null,"mpesaAmount":null,"resTransactionId":null};
			$scope.validateMpesaDetails = function(mpesaObject){
				$rootScope.startLoaderPopup();
				console.log("MPesa Object:",mpesaObject);
				$scope.skipMpesaValidation = true;
				var data ={
						organizationId :$scope.organizationId,
						unitId : $scope.unitId,
						createdBy : $scope.userId,
						createdDate : moment(new Date()).format("DD-MM-YYYY"),
						updatedDate :  moment(new Date()).format("DD-MM-YYYY"),
						updatedBy:$scope.userId,
						mpesaTxcode:$scope.mpesaObject.transactionId,
						uhid: $scope.selectedUhid,
						mpesaSendermobile:$scope.mpesaObject.mobileNo,
						mpesaAmount:$scope.mpesaObject.mpesaAmount
				};
			
				if($scope.mpesaObject.transactionId!=null && $scope.mpesaObject.mobileNo!=null){
				$rootScope.startLoader();
				var URI9 = BASE_URL + ROOT_URL + "/Billing/validateAmountByNumber";
				BillingGenericService.serviceAction(METHOD_POST,URI9, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					
					if (response.data.status == "success")
						{
						
							$scope.mpesaObject.mpesaAmount = response.data.object.mpesaAmount;
							$scope.mpesaObject.resTransactionId = response.data.object.mpesaTxcode;
							$scope.addPaymentDetails();
							$rootScope.stopLoaderPopup();
						}
					else if(response.data.status == "error"){
						
						growl.error(response.data.message,{
							title : ERROR_MSG
						});
						$scope.mpesaObject.mpesaAmount=null;
						$rootScope.stopLoaderPopup();
						$scope.skipMpesaValidation = false;
					}
					$rootScope.stopLoaderPopup();
				});
				}else{
					$scope.skipMpesaValidation = false;
					growl.error("Enter both transaction id and mobile number",{
						title : ERROR_MSG
					});
					$rootScope.stopLoaderPopup();
				}
			}
			
			/*$scope.mobileNumber ="";
			$scope.validateMpesaAmount ="";*/
			
			$scope.mpesaPaymentRequest = function(validateMpesaAmount,mobileNumber,countryCallingCode){
				
				if(validateMpesaAmount == undefined || validateMpesaAmount == null){
					
					growl.warning("Please Enter Amount",{
						title : "warning!!!"
					});
					
				}else if(validateMpesaAmount > $scope.payeeDueAmtFinal){
					
					growl.warning("Amount should be equel or less than due amount...!!",{
						title : "warning!!!"
					});
					
				}else{
					$scope.skipMpesaValidation = true;
					var data ={
							organizationId :$scope.organizationId,
							unitId : $scope.unitId,
							createdBy : $scope.userId,
							createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
							updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
							updatedBy:$scope.userId,
							uhidNumber: $scope.selectedUhid,
							phoneNumber:parseInt(countryCallingCode+""+mobileNumber),
							partyA:mobileNumber,
							amount:validateMpesaAmount
					};
				
					if((mobileNumber != undefined && mobileNumber != null && mobileNumber != "") ||
							(countryCallingCode != undefined && countryCallingCode != null && countryCallingCode != "")){
						$rootScope.startLoaderPopup();
					var URIVALI = BASE_URL + ROOT_URL + "/Billing/requestSTKPushPayment";
					BillingGenericService.serviceAction(METHOD_POST,URIVALI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						
						if (response.data.status == "success")
							{
							
							growl.success(response.data.message,{
								title : SUCCESS_MSG
							});
							
							$scope.hitMpesaApi = true;
							
							$rootScope.stopLoaderPopup();
							angular.element('#paymentReqModal').modal('hide');
							}
						else if(response.data.status == "error"){
							
							growl.error(response.data.message != null ? response.data.message : "There is problem in Mpesa payment gatway",{
								title : ERROR_MSG
							});
							$rootScope.stopLoaderPopup();
							$scope.skipMpesaValidation = false;
						}
						$rootScope.stopLoaderPopup();
					});
					}else{
						growl.error("Please enter mobile code and mobile number. ",{
							title : ERROR_MSG
						});
						$scope.skipMpesaValidation = false;
						$rootScope.stopLoaderPopup();
					}
				}
				
				
				
			}
			
		$scope.validateSTKPushPayment = function(){
				
				var data ={
						organizationId :$scope.organizationId,
						unitId : $scope.unitId,
						uhidNumber: $scope.selectedUhid
						
				};
				
				$scope.skipMpesaValidation = true;
				
				$rootScope.startLoaderPopup();
				var URIVALI = BASE_URL + ROOT_URL + "/Billing/validateSTKPushPayment";
				BillingGenericService.serviceAction(METHOD_POST,URIVALI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					
					if (response.data.status == "success")
						{
						$scope.mpesaObject.mpesaAmount = response.data.object.amount;
						$scope.mpesaObject.transactionId = response.data.object.transactionId;
						$scope.mpesaObject.resTransactionId = response.data.object.transactionId;
						$scope.mpesaObject.mobileNo = response.data.object.phoneNumber;
						
						if($scope.mpesaObject.transactionId!= null ){
							$scope.hitMpesaApi = false;
							$scope.addPaymentDetails();
							$scope.skipMpesaValidation = false;
						}
						
						growl.success(response.data.message,{
							title : SUCCESS_MSG
						});
						$rootScope.stopLoaderPopup();
						}
					else if(response.data.status == "error"){
						
						growl.error(response.data.message != null ? response.data.message :"Please wait",{
							title : ERROR_MSG
						});
						$rootScope.stopLoaderPopup();
					}
					
				});
				
			}
		

		$interval(refreshPage,1000000);
		function refreshPage()
		{
			if($scope.hitMpesaApi == true){
				$scope.validateSTKPushPayment();
			}
			
		}
			
			
			/* *************************************Mpesa Integration END****************************************** */
		$scope.foundActiveDeposit = parseFloat(0);
		$scope.payablefundAmount= parseFloat(0);
		$scope.fundbillMasterId= 0;
		$scope.acceptanceFunfListByPatient = [];
		
		$scope.getAllApprovedFundRequest = function(billObject){
			$scope.acceptanceFunfListByPatient = [];
			var data ={
					orgId :$scope.organizationId,
					unitId  : $scope.unitId,
					patientId : billObject.patientId
					
			};
			$scope.payablefundAmount= parseFloat(0);
			$scope.payablefundAmount=billObject.outstanding;
			$scope.fundbillMasterId= 0;
			$scope.fundbillMasterId=billObject.billingMasterId
			console.log( billObject);
			$rootScope.startLoaderPopup();
			var URIVALI = BASE_URL + ROOT_URL + "/api/billing/getAllFundApprovalRequestByPatient";
			BillingGenericService.serviceAction(METHOD_POST,URIVALI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				
				if (response.data.status == "success")
					{
					  $rootScope.stopLoaderPopup();
					  if(response.data.listObject!= null && response.data.listObject.length>0){
						  $("#bill_fund_DetailsList").modal('show'); 	
		                  $scope.acceptanceFunfListByPatient=response.data.listObject;
		  
		                 angular.forEach($scope.acceptanceFunfListByPatient,function(value,key)
					     {
			               value.billsanctionApprovedflagforCheck=false;
		   		           value.billAcceptancePaidAmount="";
		   		           value.billingMasterId=billObject.billingMasterId;
		   		           value.billFundconsumptionTypeId= 1;
					     });
		             	var URI = BASE_URL + ROOT_URL + "/api/billing/unit/deposit/patient/"+$scope.organizationId+"/"+$scope.unitId+"/"+billObject.patientId+"/"+billObject.visitId+"/"+billObject.visitTypeId+"/0/false";
		        		BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
		        			if (response.data.status == "success")
		        			{
		        				$scope.foundActiveDeposit = parseFloat(0);
		        				if(response.data.listObject!= null && response.data.listObject.length>0){
		        				
					        				angular.forEach(response.data.listObject,function(value,key)
					        				{
					        					$scope.foundActiveDeposit = $scope.foundActiveDeposit + value.availableDeposit;
					        					
					        				});
					        				
		        				}
		        				console.log( $scope.foundActiveDeposit); 
		        				if($scope.foundActiveDeposit > 0){
		        					var data={}
		        					data.billsanctionApprovedflagforCheck=false;
		        					data.billAcceptancePaidAmount="";
		        					data.billingMasterId=billObject.billingMasterId;
		        					data.patientId=billObject.patientId;
		        					data.billFundconsumptionTypeId= 2;
		        					data.fundrequestNumber="-";
		        					data.trustName="Total Deposit";
		        					data.requestApprovalAmount=$scope.foundActiveDeposit;
		        					data.approvalBillConsumeAmount=parseFloat(0);
		        					$scope.acceptanceFunfListByPatient.push(data);
		        						    
		        				}
		        			}
		        		});
		                
					  
					  }else{
						  growl.warning("No Sanction Approval of patient. ",
									{
										title : 'warning!'
									});
					  }
					  
					}
				else if(response.data.status == "error"){
					
					growl.error(response.data.message,{
						title : ERROR_MSG
					});
					$rootScope.stopLoaderPopup();
				}
				
			});
			 //get available active deposits by patient
			
		
		}
		
		
		$scope.finalCheckedfundAmount=parseFloat(0);
		$scope.pendingfundAmount=parseFloat(0);
		
		$scope.getcheckedBillFundDetails =function(index){
			$scope.finalCheckedfundAmount=parseFloat(0);
			$scope.pendingfundAmount=$scope.payablefundAmount;
			var  validamt=0;
			var greateramt=0;
			var zeroamt=0;
			angular.forEach($scope.acceptanceFunfListByPatient,function(value,key){
				if(value.billsanctionApprovedflagforCheck==true && key!=index &&(value.billAcceptancePaidAmount!=undefined &&  value.billAcceptancePaidAmount!="" && value.billAcceptancePaidAmount > 0 )){
					$scope.pendingfundAmount= $scope.pendingfundAmount - parseFloat( value.billAcceptancePaidAmount);
					$scope.finalCheckedfundAmount=parseFloat($scope.finalCheckedfundAmount)+ parseFloat( value.billAcceptancePaidAmount);
				}
			});
			
			
				
				angular.forEach($scope.acceptanceFunfListByPatient,function(value,key){
		            
					 if(value.billsanctionApprovedflagforCheck==true){
				    	   var availablefundbalance=parseFloat (value.requestApprovalAmount) - parseFloat(value.approvalBillConsumeAmount);
				    	   if(key==index && $scope.pendingfundAmount > 0 && (value.billAcceptancePaidAmount!=undefined &&  value.billAcceptancePaidAmount!="")){
							      if(value.billAcceptancePaidAmount>0){
								    		   if(value.billAcceptancePaidAmount > availablefundbalance ){
								    			   validamt++;
								    			   value.billAcceptancePaidAmount ="";
								    		   }else if(value.billAcceptancePaidAmount > $scope.pendingfundAmount ){
								    			   greateramt++;
								    			   value.billAcceptancePaidAmount ="";
								    		   }else{
								    			   
								    			   $scope.finalCheckedfundAmount = parseFloat($scope.finalCheckedfundAmount)+parseFloat(value.billAcceptancePaidAmount);
									    		   $scope.pendingfundAmount=parseFloat($scope.pendingfundAmount) - parseFloat( value.billAcceptancePaidAmount);
								    		   }
					    		   }else{
					    			   zeroamt++;
					    			   value.billAcceptancePaidAmount ="";
					    			   
					    		   }
			    		   
			    	        }  else if(key==index && $scope.pendingfundAmount > 0 &&(value.billAcceptancePaidAmount==undefined ||  value.billAcceptancePaidAmount==""))
					    	   {
					    		  if(availablefundbalance >= $scope.pendingfundAmount ) {
					    		   value.billAcceptancePaidAmount= parseFloat($scope.pendingfundAmount);
					    		   $scope.pendingfundAmount=parseFloat($scope.pendingfundAmount) - parseFloat(value.billAcceptancePaidAmount);
					    		   $scope.finalCheckedfundAmount = parseFloat($scope.finalCheckedfundAmount) + parseFloat(value.billAcceptancePaidAmount);
					    		  }else{
					    			   value.billAcceptancePaidAmount=availablefundbalance;
						    		   $scope.finalCheckedfundAmount =parseFloat( $scope.finalCheckedfundAmount) + parseFloat( availablefundbalance);
						    		   $scope.pendingfundAmount=parseFloat($scope.pendingfundAmount) - parseFloat(value.billAcceptancePaidAmount);
					    		  }
					    		  
					    		  
					    	   }else if (key==index){
					    		   growl.warning("selected amount equal to Net Payable amount ",
											{
												title : 'warning!'
											});
					    		   value.billAcceptancePaidAmount="";
					    		   value.billsanctionApprovedflagforCheck=false;
					    	   }
					    	   	
				         }else{
				        	   value.billAcceptancePaidAmount="";
				    		   value.billsanctionApprovedflagforCheck=false; 
				         }
					
					
						});
			
			if(validamt>0){
				 growl.warning("Please enter valid Amount ",
							{
								title : 'warning!'
							});
			}
	        if(zeroamt>0){
	        	growl.warning("Entered Amount is not zero ",
						{
							title : 'warning!'
						});

			}
	        if(greateramt>0){
	        	growl.warning("Entered amount is  greater than available Amount ",
						{
							title : 'warning!'
						});

	        }
			
		}
		
		
		$scope.saveBillFundDetails = function(){
			
			$scope.saveBillFundDetailslist=[];
			var fundtypeselectedcount=0;
			$scope.totalselectedamt=parseFloat(0);
			$scope.fundcashAmount=parseFloat(0);
			var zeroEntered=0;
			
			
			angular.forEach($scope.acceptanceFunfListByPatient,function(value,key){
				if(value.billsanctionApprovedflagforCheck==true && ( value.billAcceptancePaidAmount ==0 )){
					zeroEntered++;
				}
				if(value.billsanctionApprovedflagforCheck==true && (value.billAcceptancePaidAmount!=undefined  &&  value.billAcceptancePaidAmount!="" && value.billAcceptancePaidAmount !=0 )){
					$scope.totalselectedamt=parseFloat($scope.totalselectedamt)+parseFloat( value.billAcceptancePaidAmount);
					$scope.saveBillFundDetailslist.push(value);
					if(value.billFundconsumptionTypeId==1){
						fundtypeselectedcount++;
					}
					
				}
			});
			
			if(zeroEntered>0){
				growl.warning("Selected amount is not zero ",
						{
							title : 'warning!'
						});
				return;
				
			}
			if(fundtypeselectedcount==0){
				growl.warning("Please select At least one Trust Fund Amount",
						{title : 'warning!' });
				return;
			}
			
			if($scope.totalselectedamt > $scope.payablefundAmount ){
				growl.warning("Please select valid amount",
						{title : 'warning!' });
				return;
			}
			
			if(parseFloat($scope.totalselectedamt) != parseFloat($scope.payablefundAmount)){
				var remainingAmount=parseFloat($scope.payablefundAmount) - parseFloat($scope.totalselectedamt); 
					var data={};
					data.billAcceptancePaidAmount= parseFloat (remainingAmount);
					data.billingMasterId=$scope.fundbillMasterId;
					data.billFundconsumptionTypeId= 3;
				    $scope.saveBillFundDetailslist.push(data);
			}
			
			var data ={
					orgId :$scope.organizationId,
					unitId : $scope.unitId,
					billFundSanctionDetailsDto :$scope.saveBillFundDetailslist,
					selectedFundAmount: $scope.totalselectedamt,
					billpayableAmount:  $scope.payablefundAmount,
					createdBy : $scope.userId,
					billingMasterId : $scope.fundbillMasterId
			};
			$("#bill_fund_DetailsList").modal('hide');
			$rootScope.startLoader();
			var URIVALI = BASE_URL + ROOT_URL + "/api/billing/saveBillFundDetails";
			BillingGenericService.serviceAction(METHOD_POST,URIVALI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success")
					{
					growl.success(response.data.message,{
						title : SUCCESS_MSG
					});
					$rootScope.stopLoader();
					}
				else if(response.data.status == "error"){
					
					growl.error(response.data.message,{
						title : ERROR_MSG
					});
					$rootScope.stopLoader();
				}
				
			});
			
		}
		
		$scope.getBillFundDetailsByMasterId = function(billObject){
			
			/*var data ={
					orgId :$scope.organizationId,
					unitId : $scope.unitId,
					billingMasterId: billObject.billingMasterId
					
			};
			$rootScope.startLoader();
			var URIVALI = BASE_URL + ROOT_URL + "/api/billing/getBillFundDetailsByBillMasterId";
			BillingGenericService.serviceAction(METHOD_POST,URIVALI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				
				if (response.data.status == "success")
					{  
					    $scope.getBillFundDetailsListByMasterId=[];
					    if(response.data.listObject!= null && response.data.listObject.length>0){
					      $scope.getBillFundDetailsListByMasterId =response.data.listObject;
					      console.log($scope.getBillFundDetailsListByMasterId);
					    }
					   $rootScope.stopLoader();
					}
				else if(response.data.status == "error"){
					
					growl.error(response.data.message,{
						title : ERROR_MSG
					});
					$rootScope.stopLoader();
				}
				
			});*/
			
		}
		
		$scope.sendMail = function(bill){
			
			var data ={
			organisationId :$scope.organizationId,
			unitId : $scope.unitId,
			billingMasterId: bill.billingMasterId,
			orderDate: bill.billDate,
			billNumber: bill.billNumber,
			ordDoctorId: bill.doctorId,
			outstanding: bill.outstanding,
			patientId: bill.patientId,
			netAmount: bill.netAmount,
			creditPayable: bill.totalCreditPayable,
			selfPayable: bill.totalSelfPayable,
			visitTypeId: bill.visitTypeId
			};
			$rootScope.startLoader();
			var URIVALI = BASE_URL + ROOT_URL + "/api/billing/triggerMail";
			BillingGenericService.serviceAction(METHOD_POST,URIVALI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				
				if (response.data.status == "success")
					{  
					growl.success("Mail send Successfully",{
						title : SUCCESS_MSG
					});
					    
					   $rootScope.stopLoader();
					}
				else if(response.data.status == "error"){
					
					growl.error(response.data.message,{
						title : ERROR_MSG
					});
					$rootScope.stopLoader();
				}
				
			});

		}
			
		$scope.checkValidatePinPad = function()
		{
			if(($scope.registeredPinPadDevice != undefined || $scope.registeredPinPadDevice != null || $scope.registeredPinPadDevice != "") 
					&& $scope.registeredPinPadDevice == "POS Machine"){
				$scope.getResposeList=[];
				$scope.getGeneratedResponse=[];
				$rootScope.startLoaderPopup();
				var data = {
						patientId : $scope.patientIdForReport,
						organizationId : $scope.organizationId,
						unitId : $scope.unitId,
						transactionAmount:$scope.paymentDetails.credAmount,
				}
				
				
				var URI = BASE_URL + ROOT_URL + "/api/updatePOSMachineDetails";
				BillingGenericService.serviceAction(METHOD_POST,URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					
					if (response.data.status == "success")
						{
							console.log("saved Card Details");
							if(response.data.object != null && response.data.object.listObject.length > 0){
								$scope.getResposeList=response.data.object.listObject;
								$scope.getGeneratedResponse = JSON.parse($scope.getResposeList[0].generatedReponse);
								$scope.paymentDetails.credCardHolderName=$scope.getGeneratedResponse.cardHolderName;
//								$scope.paymentDetails.credCardTypeId=$scope.getGeneratedResponse.cardType;
								$scope.paymentDetails.credAmount=$scope.getGeneratedResponse.transactionAmount;
								if($scope.flagForBharatQr == false){
									$scope.paymentDetails.credCardNumber=$scope.getGeneratedResponse.transactionCardNumber.slice($scope.getGeneratedResponse.transactionCardNumber.length - 4);
								}else{
									$scope.paymentDetails.credCardNumber=$scope.getGeneratedResponse.transactionCardNumber;
								}
								$scope.paymentDetails.transactionNo=$scope.getGeneratedResponse.transactionID;
								$scope.paymentDetails.credExpiryDate=moment($scope.getGeneratedResponse.expiryDate).toDate();
								$scope.paymentDetails.requestedUniqueId=$scope.getGeneratedResponse.billNumber/*$scope.getGeneratedResponse.invoiceNumber*/;
								$scope.paymentDetails.creditDebitCardType=$scope.getGeneratedResponse.creditDebitCardType;
								$scope.paymentDetails.transactionTypeName=$scope.getGeneratedResponse.transactionTypeName;
								$scope.paymentDetails.transactionDetails=($scope.paymentDetails.creditDebitCardType!= null && $scope.paymentDetails.creditDebitCardType!= "" ? $scope.paymentDetails.creditDebitCardType + ' || ':"")
								+ ($scope.paymentDetails.credCardNumber!= null && $scope.paymentDetails.credCardNumber!= "" ? $scope.paymentDetails.credCardNumber + ' || ':"")+ ($scope.paymentDetails.transactionNo!= null && $scope.paymentDetails.transactionNo!= "" ? $scope.paymentDetails.transactionNo + ' || ':"") + ($scope.paymentDetails.transactionTypeName!= null && $scope.paymentDetails.transactionTypeName!= "" ? $scope.paymentDetails.transactionTypeName:"")
								
								$scope.getGeneratedResponse.cardType=angular.lowercase($scope.getGeneratedResponse.cardType);
								angular.forEach($scope.cardTypeList,function(value, key){
									value.cardTypeName1=angular.lowercase(value.cardTypeName);
									if(value.cardTypeName1 == $scope.getGeneratedResponse.cardType){
										$scope.paymentDetails.credCardTypeId=value.cardTypeId;
									}
								});
								$scope.addPaymentDetails();
								
								$rootScope.stopLoaderPopup();
							}
							else {
								
								growl.error("Please wait user did not paid amount.",{
									title : ERROR_MSG
								});
								$rootScope.stopLoaderPopup();
							}

						}
					else if(response.data.status == "error"){
						
						growl.error("Please wait user did not paid amount.",{
							title : ERROR_MSG
						});
						$rootScope.stopLoader();
					}
					
				});
			}else{
			//alert($location.host());
			//var IAddress = $location.host();
			//alert("IP ADRRESS");
			//alert(IAddress);
			var data ={
					organizationId :$scope.organizationId,
					unitId : $scope.unitId,
					createdBy : $scope.userId,
					createdDate : moment(new Date()).format("DD-MM-YYYY HH:mm:ss"),
					tillId:'50002',
					patientId: $scope.selectedPatientId,
					//ip : IAddress
			};
			$scope.pinpadAmounFlag = true;
			$rootScope.startLoader();
			var URI9 = BASE_URL + ROOT_URL + "/Billing/savePinPanDetaild";
			BillingGenericService.serviceAction(METHOD_POST,URI9, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				
				if (response.data.status == "success")
					{
					
						$scope.uniqueNumber = response.data.object.uniqueNumber;
						$scope.pinpadTransectionId = response.data.object.pinpadTransectionId;
						$scope.ipAddress = response.data.object.ipAddress;
						var dataForPinPad = {
							    amount: parseFloat($scope.dueAmount) * 100,
								//"amount":100,
								//tellerId: "50002",
								tranKey: $scope.uniqueNumber
						}
						
					/*	
						$http.post('http://localhost:9991/api/sale',dataForPinPad)
							    .then(function(response) {
							            alert();
							    });*/
						
						/* return $http({
				                headers: {
				                    "Accept": "application/json;odata=verbose",
				                    "content-type": "application/json;odata=verbose"
				                },
				                method: "POST",
				                url: "http://localhost:9991/api/sale",
				                data: {
				                    __metadata: {
				                        "type": "SP.Data.SongsListItem"
				                    },
				                    "amount": "100",
				                    "tellerId": "50002",
				                    "tranKey": "201903282"
				                }


				            }).then(saveContact)
				            .catch(function(message) {
				                console.log("addQuestion() error:" + message);
				            });
						
						 function saveContact(data, status, headers, config) {
					            alert("Item added successfully");
					        }*/
						
						/*var URI = "http://localhost:9991/api/sale";
						var headerObjDia = {
								'Content-Type':'application/json',
				     };
					 var data={};
	               
	             GenericServiceParamHeader
	                 .serviceAction(METHOD_POST, URI,
	                		 dataForPinPad,
	                         NOTIFICATION_MSG_STATUS_FALSE,data,headerObjDia)
	                 .then(
	                         function(response) {
	                             console.log(response);
	                             alert();
	                         });*/
	             
	             
//						/$scope.ipAddress+
						var urlprotocol = $location.protocol();
						//var finalUrl = "http://"+$scope.ipAddress+":9991/api/sale";
						var finalUrl = "http://127.0.0.1:9991/api/sale";
						var URI = finalUrl;
						BillingGenericService.serviceAction(METHOD_POST,URI,dataForPinPad,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							/*if (response.data.status == "success"){
								console.log("ree",response);
								console.log("reew",response.data);
								console.log("reer",response.data.rspCode);*/
							if (response !=  undefined){
								if(response.data.rspCode == '100' || response.data.rspCode == '003' || response.data.rspCode == '000')
									{
										$scope.paymentDetails.credCardHolderName = response.data.cardholderName;
										$scope.paymentDetails.credAmount = parseFloat(response.data.amount)/100;
										$scope.schema = response.data.scheme;
										var pan = response.data.pan;
										var lastFour = pan.substr(pan.length - 4);
										$scope.paymentDetails.credCardNumber = lastFour;
										$scope.paymentDetails.transactionNo = response.data.retRefNr;
										
										var cardTypeId = $.grep($scope.cardTypeList, function (card) {
						    	            return card.pinpadScheme == $scope.schema;
						    	        })[0].cardTypeId;
										$scope.paymentDetails.credCardTypeId = cardTypeId;
										
										$scope.resString = JSON.stringify(response);
										console.log("resString",$scope.resString); 
										
										var dataUPDATE = {
												response : $scope.resString,
												pinpadTransectionId : $scope.pinpadTransectionId,
												patientId : $scope.selectedPatientId,
												organizationId : $scope.organizationId,
												unitId : $scope.unitId,
												createdDate : moment(new Date()).format("YYYY-MM-DD"),
												createdBy: $scope.userId,
												status:'A'
										}
										
										
										var URIUPDATE = BASE_URL + ROOT_URL + "/Billing/updatePinpadDetails";
										BillingGenericService.serviceAction(METHOD_POST,URIUPDATE, dataUPDATE,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
											
											if (response.data.status == "success")
												{
													console.log("saved Card Details");
												    $scope.addPaymentDetails();
												}
											else if(response.data.status == "error"){
												
												growl.error("Somting wrong with save data of card..",{
													title : ERROR_MSG
												});
												$rootScope.stopLoader();
											}
											
										});
										 $scope.pinpadAmounFlag = false;
										growl.success(response.data.message,{
											title : "SUCCESS!"
										});
										
									
									}
								else
									{
									 $scope.pinpadAmounFlag = false;
										growl.error(response.data.message,{
											title : ERROR_MSG
										});
										return false;
									}
							} else{
								 
								 $scope.pinpadAmounFlag = false;
								 growl.warning("Pin Pad Not Configure",{
										title : "Warning"
									});
									$rootScope.stopLoader();
									return false;
							 }
						});
						
						$rootScope.stopLoader();
					}
				else {
					 $scope.pinpadAmounFlag = false;
					growl.error("Someting went wrong..!!",{
						title : ERROR_MSG
					});
					$rootScope.stopLoader();
				}
				
			});
		}
		}
			$scope.cancelExAmount = function(){
	          	$("#excessAmountDepositPopup").modal("hide");
				$('#"confirmSave"').modal('hide');
				$scope.paymentDetails.cashAmount = parseFloat($scope.dueAmount).toFixed(2);
				$scope.excessAmount = parseFloat(0.0).toFixed(2);
			}
			
			$scope.cancelPaymentReqModal = function(){
				angular.element('#paymentReqModal').modal('hide');
			}
			
			$scope.clearMpesaFields = function(){
				$scope.skipMpesaValidation = false;
				$scope.mpesaObject={"transactionId":null,"mobileNo":null,"mpesaAmount":null,"resTransactionId":null};
			}
			$scope.clearCardFields =  function(){
				$scope.pinpadAmounFlag = false;
				$scope.flagForSale=false;
				 $scope.flagForSMSpay=false;
				 $scope.flagForBharatQr=false;
				 $scope.objPosMachine.deviceName = undefined;
				 $scope.objPosMachine.isSale = false;
				 $scope.objPosMachine.isBharatQr = false;
				 $scope.objPosMachine.isSMSpay = false;
				$scope.paymentDetails = angular.copy({paymentModeId:$scope.paymentDetails.paymentModeId,cashAmount:"",bankId:"",chequeNumber:"",chequeDate:"",chequeAmount:"",credCardHolderName:"",credCardTypeId:"",credAmount:"",credCardNumber:"",credExpiryDate:"",debCardHolderName:"",debCardTypeId:"",debAmount:"",debCardNumber:"",debExpiryDate:"",hospitalAccountId:"",cardBalAmount:$scope.paymentDetails.cardBalAmount,loyaltyCardNo:$scope.paymentDetails.loyaltyCardNo,loyaltyCardAmt:""});
				$scope.calculateTotalDueRefAmount();
			}
			
			
			$scope.checkFinancialClearance = function() 
			{
				
				if($scope.isInterimOrFinalBill == 2){
				var valChe = $('#isFinancialClearance_check').val();
				if($('#isFinancialClearance_check').is(":checked"))
				{
					if(($scope.userDiscountObject != null &&  parseFloat($scope.userDiscountObject.fromFinancialClearanceAmount) <= $scope.totalNetPayableAmt && parseFloat($scope.userDiscountObject.toFinancialClearanceAmount) >=  $scope.totalNetPayableAmt) )
			    	{
						$("#confirmSave").modal("show");
			    	}else{
			    		//this.$parent.isFinancialClearence ='N'
			    		$('#isFinancialClearance_check').prop('checked', false);
			    		if($scope.userDiscountObject != null){
			    			growl.warning("User have rights for approving financial clearance from "+$scope.userDiscountObject.fromFinancialClearanceAmount+" to "+$scope.userDiscountObject.toFinancialClearanceAmount+"..!!",{
								title : WARNING_MSG
							});
			    		}else{
			    			growl.warning("User have no rights for approving financial clearance..!!",{
								title : WARNING_MSG
							});
			    		}
		    			
			    	}
				}
			}else{
				growl.warning("Financial clearance can be provided for final bill only.",{
					title : WARNING_MSG
				});
				$('#isFinancialClearance_check').prop('checked', false);
			}
			}
			
			$scope.cancelClearance = function(){
				$('#confirmSave').modal('hide');
				//this.$parent.isFinancialClearence ='N';
				$('#isFinancialClearance_check').prop('checked', false);
			}
			
			$scope.proceedForClearance = function(){
				
				$rootScope.startLoader();
				var data = {
						updatedBy : $scope.userId,
						updatedDate : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
						admissionId: $scope.admissionId,
						encounterId:$scope.visitTypeId == 1 ? $scope.encounterId : null,
						billId : $scope.billId,
				}
				
				var URI = BASE_URL + ROOT_URL + "/api/billing/approveRequestForFinancialClearance";
				
				GenericService
				.serviceAction("POST", URI, data)
				.then(function(response) {
							$rootScope.stopLoader();
							if (response.data.status == "success") {
								
								$('#confirmSave').modal('hide');
								$('#billListDetails').modal('hide');
								growl.success("Approved !!!!",{
									title : 'success'
								});
								$scope.init();
								
							}
						});
				
			}
			
			// Search Surgen with Typehead
			$scope.searchGroupSurgen = function(keyword) {

				var data = {};
				var paramObj = {
					doctor_name : keyword
				};
				var headerObj = {
					unit_id : $scope.unitId,
					organization_id : $scope.organizationId
				}

				var URI = BASE_URL + ROOT_URL+ GETDOCTORMASTERLISTAUTOFILLSERACH;

				return GenericServiceParamHeader.serviceAction(METHOD_GET, URI, data,
								NOTIFICATION_MSG_STATUS_FALSE,paramObj, headerObj).then(
								function(response) {
									if (response.data.status == "success")
										return response.data.listObject;
								});
			}
			
			$scope.selectDoctor = function($item, $model, $label, idx) {
				$scope.doctorName = $item.doctorName;
				$scope.billSearch.doctorId = $item.doctorId;
			}
			
			
			  $scope.getNewDoctorList=function(){
		    	  var obj={
		    			  unitId : $scope.unitId,
					   		organizationId : $scope.organizationId,
		    	  }
				    	  var URI = BASE_URL + ROOT_URL + "/adt/getDoctorListForOPDBilling";
				
				GenericService
				.serviceAction("POST", URI, obj)
				.then(
				function(response) {
				$scope.doctorsList = [];
				
				if (response.data.status == "success") {
				$scope.doctorsList = response.data.listObject;
				
				}
				});
		      }
			  $scope.getNewDoctorList();
			  
			 $scope.razorPayObject={"razorMobileNo":null,"razorAmount":null,"razorPayRequired":true,"razorUniqueId":null,"razorPaidAmount":null};
			 $scope.sendPaymentLink = function(){
				 if($scope.exceededBillAmountSettlement==false)
				  {
				  if(parseFloat($scope.dueAmount)<parseFloat($scope.razorPayObject.razorAmount))
					  {
					  $scope.razorPayObject.razorAmount= parseFloat($scope.dueAmount).toFixed(2);
					  growl.warning("Entered amount is greater than current amount");
					  return false;
					  }
				  }
					if($scope.razorPayObject.razorMobileNo == undefined || $scope.razorPayObject.razorMobileNo == null || $scope.razorPayObject.razorMobileNo =='')
						{
						growl.error("Please enter mobile Number",
								{
									title : 'Error!'
								});
						    return false;
						 }
					else if($scope.razorPayObject.razorAmount == undefined || $scope.razorPayObject.razorAmount == null || $scope.razorPayObject.razorAmount =='')
					{
						growl.error("Please enter amount",
								{
									title : 'Error!'
								});
						    return false;
						 }
					else{
					$rootScope.startLoader();
						var data = {
								  "organizationId" : $scope.organizationId,
								  "unitId" : $scope.unitId,
								  "mobileNo":$scope.razorPayObject.razorMobileNo,
								  "razorAmount":$scope.razorPayObject.razorAmount,
								  "callbackUrl":BASE_URL + ROOT_URL + "/getRazerPay",
								  "email":$scope.patientEmail,
								  "name":$scope.patientName,
								  
						}
						var URI = BASE_URL + ROOT_URL + "/razorPayPaymentLink";
						BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							if (response.data.status == "success")
							{
									$rootScope.stopLoader();
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
					}
					
				}
				
				
				
				$scope.validateRazerPay = function(){
					

					if($scope.razorPayObject.razorMobileNo == undefined || $scope.razorPayObject.razorMobileNo == null || $scope.razorPayObject.razorMobileNo =='')
						{
						growl.error("Please enter mobile Number",
								{
									title : 'Error!'
								});
						    return false;
						 }
					else {
					$rootScope.startLoader();
						var data = {
								  "organizationId" : $scope.organizationId,
								  "unitId" : $scope.unitId,
								  "mobileNo":$scope.razorPayObject.razorMobileNo,
								  
						}
						var URI = BASE_URL + ROOT_URL + "/validateRazorPayPaymentLink";
						BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							if (response.data.status == "success")
							{
									$rootScope.stopLoader();
									$scope.razorPayObject.razorPaidAmount = response.data.object.razorAmount;
									$scope.razorPayObject.razorUniqueId = response.data.object.uniqueId;
									paymentCollFactory.addPaymentDetails($scope);
									$scope.calculateTotalDueRefAmount();
								} else if(response.data.status == 'error')
								{
									$rootScope.stopLoader();
									growl.error(response.data.message,
											{
												title : 'Error!'
											});
							}
				
						});
					}
				}
				
				
				
			$scope.PayByRazorPayment = function(){
				if($scope.exceededBillAmountSettlement==false)
				  {
				  if(parseFloat($scope.dueAmount)<parseFloat($scope.razorPayObject.razorAmount))
					  {
					  $scope.razorPayObject.razorAmount= parseFloat($scope.dueAmount).toFixed(2);
					  growl.warning("Entered amount is greater than current amount");
					  return false;
					  }
				  }
				if($scope.razorPayObject.razorMobileNo == undefined || $scope.razorPayObject.razorMobileNo == null || $scope.razorPayObject.razorMobileNo =='')
					{
					growl.error("Please enter mobile Number",
							{
								title : 'Error!'
							});
					    return false;
					 }
				else if($scope.razorPayObject.razorAmount == undefined || $scope.razorPayObject.razorAmount == null || $scope.razorPayObject.razorAmount =='')
				{
					growl.error("Please enter amount",
							{
								title : 'Error!'
							});
					    return false;
					 }
				else{
				$rootScope.startLoader();
					var data = {
							  "organizationId" : $scope.organizationId,
							  "unitId" : $scope.unitId,
							  "mobileNo":$scope.razorPayObject.razorMobileNo,
							  "razorAmount":$scope.razorPayObject.razorAmount,
							  "callbackUrl":BASE_URL + ROOT_URL + "/getRazerPay",
							  "email":$scope.patientEmail,
							  "name":$scope.patientName,
							  
					}
					var URI = BASE_URL + ROOT_URL + "/razorPayPaymentLink";
					BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success")
						{
								$rootScope.stopLoader();
								$window.open(response.data.object.shortUrl, '_blank');
								
							} else if(response.data.status == 'error')
							{
								$rootScope.stopLoader();
								growl.error(response.data.message,
										{
											title : 'Error!'
										});
						}
			
					});
				}
			}
			
			
			
			 $scope.sendPaymentLinkCCAvenue = function(){
					
					if($scope.razorPayObject.razorMobileNo == undefined || $scope.razorPayObject.razorMobileNo == null || $scope.razorPayObject.razorMobileNo =='')
						{
						growl.error("Please enter mobile Number",
								{
									title : 'Error!'
								});
						    return false;
						 }
					else if($scope.razorPayObject.razorAmount == undefined || $scope.razorPayObject.razorAmount == null || $scope.razorPayObject.razorAmount =='')
					{
						growl.error("Please enter amount",
								{
									title : 'Error!'
								});
						    return false;
						 }
					else{
					$rootScope.startLoader();
						var data = {
								  "organizationId" : $scope.organizationId,
								  "unitId" : $scope.unitId,
								  "mobileNo":$scope.razorPayObject.razorMobileNo,
								  "requestAmount":$scope.razorPayObject.razorAmount,
								  "emailId":$scope.patientEmail,
								  "name":$scope.patientName,
								  "patientId": $scope.selectedPatientId, 
								  "createdBy": $scope.userId, 
								  "updatedBy": $scope.userId,
								  
						}
						var URI = BASE_URL + ROOT_URL + "/sendCCAvenueLink";
						BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							if (response.data.status == "success")
							{
									$rootScope.stopLoader();
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
					}
					
				}
				
				
				
				$scope.validateRazerPayCCAvenue = function(){
					

					if($scope.razorPayObject.razorMobileNo == undefined || $scope.razorPayObject.razorMobileNo == null || $scope.razorPayObject.razorMobileNo =='')
						{
						growl.error("Please enter mobile Number",
								{
									title : 'Error!'
								});
						    return false;
						 }
					else {
					$rootScope.startLoader();
						var data = {
								  "organizationId" : $scope.organizationId,
								  "unitId" : $scope.unitId,
								  "mobileNo":$scope.razorPayObject.razorMobileNo,
								  "patientId": $scope.selectedPatientId, 
								  
						}
						var URI = BASE_URL + ROOT_URL + "/validateCCAvenuePayment";
						BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							if (response.data.status == "success")
							{
									$rootScope.stopLoader();
									$scope.razorPayObject.razorPaidAmount = response.data.object.paidAmount;
									$scope.razorPayObject.razorUniqueId = response.data.object.orderNo;
									//paymentCollFactory.addPaymentDetails($scope);
									$scope.addPaymentDetails();
									$scope.calculateTotalDueRefAmount();
								} else if(response.data.status == 'error')
								{
									$rootScope.stopLoader();
									growl.error(response.data.message,
											{
												title : 'Error!'
											});
							}
				
						});
					}
				}
				
				
				
			$scope.PayByRazorPaymentCCAvenue = function(){
				
				if($scope.razorPayObject.razorMobileNo == undefined || $scope.razorPayObject.razorMobileNo == null || $scope.razorPayObject.razorMobileNo =='')
					{
					growl.error("Please enter mobile Number",
							{
								title : 'Error!'
							});
					    return false;
					 }
				else if($scope.razorPayObject.razorAmount == undefined || $scope.razorPayObject.razorAmount == null || $scope.razorPayObject.razorAmount =='')
				{
					growl.error("Please enter amount",
							{
								title : 'Error!'
							});
					    return false;
					 }
				else{
				$rootScope.startLoader();
					var data = {
							  "organizationId" : $scope.organizationId,
							  "unitId" : $scope.unitId,
							  "mobileNo":$scope.razorPayObject.razorMobileNo,
							  "requestAmount":$scope.razorPayObject.razorAmount,
							  "emailId":$scope.patientEmail,
							  "name":$scope.patientName,
							  "patientId": $scope.selectedPatientId, 
							  "createdBy": $scope.userId, 
							  "updatedBy": $scope.userId,
							  
					}
					var URI = BASE_URL + ROOT_URL + "/sendCCAvenueLink";
					BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success")
						{
								$rootScope.stopLoader();
								
								  $timeout( function(){
									  $window.open(response.data.object.payLink, '_blank');
					            },4000);
								
								
								
							} else if(response.data.status == 'error')
							{
								$rootScope.stopLoader();
								growl.error(response.data.message,
										{
											title : 'Error!'
										});
						}
			
					});
				}
			}
			/******POS machine***********/
			$scope.getPOSMachineList=function(){
				$scope.pOSMachineList=[];
				 var data={
							   organizationId:$scope.organizationId,
			 			}
			           var URI = BASE_URL + ROOT_URL + "/api/getPOSMachineDeviceMasterList";
				 
					BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						
						$scope.otReservationList = [];
						if (response.data.status == "success")
						{
							$scope.pOSMachineList  = response.data.listObject;
						}
					});
			}
			$scope.getPOSMachineList();
		    
			$scope.selectedDevice=function(obj){
				
				if($scope.isPinPadRequired == 'Y' && $scope.registeredPinPadDevice == "POS Machine"){
				$scope.configDetail=[];
				angular.forEach($scope.pOSMachineList, function(value, key){
					if(value.deviceName == obj.deviceName.toString()){
						$scope.machineConfigDetails=value.machineConfigDetails;
						$scope.uploadLink=value.uploadLink;
					}
				});
					
				$scope.configDetail=JSON.parse($scope.machineConfigDetails);
				
				$scope.custUserId = $scope.configDetail.custUserId;
				$scope.deviceid = obj.deviceName;
				$scope.password = $scope.configDetail.password;
				}

			}
			

			
			
			$scope.getFlagSale=function(){
				if($scope.objPosMachine.isSale =="isSale"){
					$scope.objPosMachine.isSale = true;
					$scope.flagForSale=true;
					 $scope.flagForSMSpay=false;
					 $scope.flagForBharatQr=false;
					 $scope.objPosMachine.isBharatQr ="";
					 $scope.objPosMachine.isSMSpay=""
				}
				
			}
		 
		 $scope.getFlagBharatQr=function(){
			
				if($scope.objPosMachine.isBharatQr =="isBharatQr"){
					$scope.objPosMachine.isBharatQr = true;
					$scope.flagForBharatQr=true;
					 $scope.flagForSale=false;
					 $scope.flagForSMSpay=false;
					 $scope.objPosMachine.isSale ="";
					 $scope.objPosMachine.isSMSpay="";
				}
			}
		 $scope.getFlagSMSpay=function(){
				
				if($scope.objPosMachine.isSMSpay =="isSMSpay"){
					$scope.objPosMachine.isSMSpay = true
					$scope.flagForBharatQr=false;
					 $scope.flagForSale=false;
					 $scope.flagForSMSpay=true;
					 $scope.objPosMachine.isSale ="";
					 $scope.objPosMachine.isBharatQr ="";
				}
			}
		 
			 $scope.sendRequestForPOSMachine=function(){

					if($scope.objPosMachine.deviceName != undefined && $scope.objPosMachine.deviceName !=null && $scope.objPosMachine.deviceName != "" ){
						$rootScope.startLoaderPopup();
						if($scope.flagForSale == true){
						$scope.sale=
//						{

						/*"deviceid":($scope.objPosMachine.deviceName).toString(),
						 "custUserId":$scope.custUserId,
						 "password":$scope.password,
						 "custMobileNo":$scope.patientDetails.mobileNumber,
						 "invoice":Date.now().toString(),
						 "amount":$scope.paymentDetails.credAmount,
						 "transactionType":"Sale"
						}*/
								{
									  "userName": $scope.custUserId,
									  "password": $scope.password,
									  "customerMobileNo":$scope.mobileNumber,
									  "customerEmail": $scope.patientEmail,
									  "invoiceNo": Date.now().toString(),
									  "transactionAmount": $scope.paymentDetails.credAmount,
									  "transactionType": "1",  
									  "payerName": $scope.firstName,
									  "autoInitiateFlag": "N"
									}


						$scope.selectedJson=JSON.stringify($scope.sale);
						
					}
					if($scope.flagForBharatQr == true){
						$scope.bharatQr=
						{
								  "userName": $scope.custUserId,
								  "password": $scope.password,
								  "customerMobileNo":$scope.mobileNumber,
								  "customerEmail": $scope.patientEmail,
								  "invoiceNo": Date.now().toString(),
								  "transactionAmount": $scope.paymentDetails.credAmount,
								  "transactionType": "24",  
								  "payerName": $scope.firstName,
								  "autoInitiateFlag": "N"
								}

						$scope.selectedJson=JSON.stringify($scope.bharatQr);
					}
					if($scope.flagForSMSpay == true){
						$scope.smsPay=
						{
								  "userName": $scope.custUserId,
								  "password": $scope.password,
								  "customerMobileNo":$scope.objPosMachine.mobileNumberForPOS != null ? $scope.objPosMachine.mobileNumberForPOS :$scope.mobileNumber,
								  "customerEmail": $scope.patientEmail,
								  "invoiceNo": Date.now().toString(),
								  "transactionAmount": $scope.paymentDetails.credAmount,
								  "transactionType": "19",  
								  "payerName": $scope.firstName,
								  "autoInitiateFlag": "N"
								}

						$scope.selectedJson=JSON.stringify($scope.smsPay);
					}
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					var data={
							patientId:$scope.patientIdForReport,
							transactionAmount:parseInt($scope.paymentDetails.credAmount),
							organizationId:$scope.organizationId,
							unitId:$scope.unitId,
							createdBy:$scope.createdBy,
							updatedBy:$scope.updatedBy,
							status:'A',
							createdDate:$scope.dt,
							updatedDate:$scope.dt,
							posMachineDeviceId:$scope.deviceid,
							selectedPaymentModeJson:$scope.selectedJson,
						}
			        var URI = BASE_URL + ROOT_URL + "/api/savePOSMachineDeviceMaster";
				 
					BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {

						if(response.data.status != "success"){
							growl.error(response.data.message);
							$scope.flagForSMSpay == false;
							$scope.flagForBharatQr == false;
							$scope.flagForSale == false;
							$scope.smsPay = {};
							$scope.bharatQr = {};
							$scope.sale = {};
							$scope.objPosMachine.deviceName = "";
							 $scope.objPosMachine.isSale = false;
							 $scope.objPosMachine.isBharatQr = false;
							 $scope.objPosMachine.isSMSpay = false;
						}else{
							growl.success(response.data.message);
						}

						$rootScope.stopLoaderPopup();
					});
			 }else{
				 growl.warning("Please select device.");
				 $rootScope.stopLoader();
			 }
			 }
			 /*********************/
			$scope.getPandCard = function(billId){

				var URI = BASE_URL + ROOT_URL + "/billing/PanCard/url/"+billId+"/"+0; //1 for Deposit screen
				BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if (response.data.status == "success")
					{
						if(response.data.object != null ){
							var imageUrl = response.data.object.docUrl;
							$window.open(imageUrl, '_blank');
						}else{
							growl.warning("pancard is not uploaded",
									{
										title : 'Warning!'
									});
						}
						
					} 

				});
			
			}
			
			
			$scope.init();
			$scope.initTotalAmounts();
			
		}catch(e){
			console.log(e.message);
		}
	
		$scope.patientDocumnetInformation = function(info) {
			$scope.patientDocumentFlipBook ={};
			$scope.patientDocumentFlipBook =info;
			
			
			
				var data =
				{
				"encounterId":$scope.patientDocumentFlipBook.encounterId,
				"patientId":$scope.patientDocumentFlipBook.patientId,
				"unitId":$scope.unitId,
				"organisationId":$scope.organizationId,
				"userId":$scope.userId
				}
				
				
			var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
			BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					$scope.patientDocumentListTest = [];
					if (response.data.status == "success") 
					{
						$scope.patientDocumentListTest = response.data.listObject;
						if($scope.patientDocumentListTest != null && $scope.patientDocumentListTest.length >0){
							$('#patientDocumentOption').modal('show');
						}
						$rootScope.stopLoader();
					}
					else if(response.data.status == "error")
						{
						$rootScope.stopLoader();
						}
				});
	
          };
          
          $scope.convertDateToTimestamp = function(dateString) {
                let parts = dateString.split("-");
                let day = parseInt(parts[0], 10);
                let month = parseInt(parts[1], 10) - 1; 
                let year = parseInt(parts[2], 10);
                let date = new Date(year, month, day);
                let timestampInMilliseconds = date.getTime();
                let timestampInSeconds = timestampInMilliseconds / 1000;
                return timestampInSeconds;
            };
            
	     		$scope.generateFlipbookData = function() {
	                  let flipbookDatalist = [];

		     			var filteredlist=[];
		     		    angular.forEach($scope.patientDocumentListTest,function(filterObj,index){
		     		    	
		     		    	if(filterObj.documentPath != null){
		     				var flipbookDataobj={};
		     				 let createdDate = filterObj.createdDate;
		     	            let timestamp = $scope.convertDateToTimestamp(createdDate);
		     				flipbookDataobj.timestamp=timestamp;
		     				flipbookDataobj.url=filterObj.documentPath;
		     				flipbookDataobj.tags=[];
		     				flipbookDataobj.tags.push("Document");
		     				flipbookDataobj.title=filterObj.documentName != null ? filterObj.documentName :"Lab Report" ; 
		     				flipbookDatalist.push(flipbookDataobj);
		     		    	}
		     				
		     			});

	                let data = {
	                    flipbookData: flipbookDatalist,
	                    logo: $scope.firstImgaa,
	                    privacy: "public"
	                };

	                let baseUrlLocation = $window.location.href;
	                let temp = baseUrlLocation.split('/');
	                let BASE_URL_CASESHEET = baseUrlLocation.replace(temp[temp.length - 1], "");

	                let generateFlipbookurl = "https://feqituyulh.execute-api.ap-south-1.amazonaws.com/dev/api/generateFlipbook";
	                
	        		try {
	        			var param={};
	        				$.ajax({
	        			        url : generateFlipbookurl,
	        					type : "POST",
	        					headers: {'X-Api-Key': "apikey_zHOw4R758ELytSeHFII99YibXj2sWHR6u9mxKdipN/g="},
	        					contentType : "application/json",
	        					dataType : "json",
	        					data : JSON.stringify(data),
	        			        //headers:getHeaders(),
	        					success : function(response){
	        						if(response.flipbookUrl!=undefined &&  response.flipbookUrl!=null  &&  response.flipbookUrl!="" ){
	        							  window.open(''+response.flipbookUrl, '_blank');
	        						}else{
	        							$.notify("Error occurred","error");
	        						}
	        			    	}
	        			});
	        	     } catch (e) {
	        	 		console.log(e );
	        		}
	            };
						
							
  }
  
  
  function config($stateProvider) {
    $stateProvider.state("outsourcedServiceWorkOrder", {
      url: "/outsourcedServiceWorkOrder",
      templateUrl: 'views/master/UnitMaster/billingMaster/outsourced-service-workorder.html',
      controller: "outsourcesService.Controller",
      title: 'Services Work Order',
      resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 1063);
				 	});
					if(arr.length <= 0){
						$state.go("accessDenied")
						deferred.reject();
					 } else {
						deferred.resolve();
					 }
		        });
		
		        return deferred.promise;
		      }
		 },
      controllerAs: "vm"
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("outsourcesService.Controller", outsourcedServiceWorkOrderController);
})();
