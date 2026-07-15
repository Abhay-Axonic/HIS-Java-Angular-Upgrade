angular.module("myApp").controller("badDebitListController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','paymentCollFactory','reportService','PaginationService','GenericService','$location','loyaltiCardCallFactory','CONSTANTS',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,growl,paymentCollFactory,reportService,PaginationService ,GenericService,$location,loyaltiCardCallFactory,CONSTANTS){
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
		
		var cookieObject = $cookies.getObject('cookieObject');
		if (cookieObject == undefined) {
			$state.go('login');
			return;
		}
		
		var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
		 if(cookieObjectAppConfig == undefined){
		  $state.go('login');
		  return;
		 }
		 else
		  {
		   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
		   
		  }
		$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
		$scope.isAppointmentRequired = cookieObjectAppConfig.isAppointmentRequired;
		$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
		$scope.selfPayeeIdCookies = cookieObjectAppConfig.selfPayeeId;
		$scope.badDebitDuration = cookieObjectAppConfig.badDebitDuration;
	
		$scope.isPinPadRequired = cookieObjectAppConfig.isPinPadRequired;
		$scope.pinpadAmounFlag = false;
		$scope.excessAmount = parseFloat(0).toFixed(2);	
		$scope.unitId = cookieObject.unitId;
		$scope.userId = cookieObject.userId;
		$scope.organizationId = cookieObject.organizationId;
		$scope.mpesaRequired = cookieObject.mpesaRequired;
		$scope.userId =$scope.userId;
		$scope.appointmentId = 0;
		$scope.encounterId = 0;
		$scope.validatTextFieldFlag = true;
		$scope.addToBillFlag = true;
		$scope.insurancePayeeExist = false;
		$scope.selfPayeeExist = false;
		

		 $scope.pagination= {
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
              };
		
		 
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
		$scope.payeeId = '';
		$scope.consumedAmt = parseFloat(0);
		$scope.receivedFrom = "";
		$scope.identificationNumber = "";
		
		$scope.depositeList = {
			    depositeIds: [1]
			  };
		
		$scope.depositeDisabledFlag = false;
		$scope.depositeSettleBtnFlag = true;
	
		
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
		
		
		$scope.getPaymentEntitlementList = function() {
			
			/*$rootScope.startLoader();
			var data = "";*/
			/*var URI = BASE_URL + ROOT_URL + "/global/paymententitlement/"+$scope.organizationId;
			BillingGenericService.serviceAction("GET", URI, data).then(function(response){
			*/	$scope.paymentEntitlementList= [
					{
						"id":1,
						"name":"Company"
					},
					{
						"id":2,
						"name":"Insurance"
					},
					{
						"id":3,
						"name":"Self"
					}
				]
			/*});*/
			
	     }
		$scope.getPaymentEntitlementList();
		
		
		$scope.getUserBadDebitRights = function()
		{
			var URI = BASE_URL + ROOT_URL + "/billing/getUsersDiscRights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/userId/"+$scope.userId;
	BillingGenericService.serviceAction(METHOD_GET, URI,"",
					NOTIFICATION_MSG_STATUS_FALSE)
			.then(function(response) {
						console.log(response);
						// $rootScope.startLoader();
						if (response.data.status == "success")
							{
							$scope.userDiscountObject ={};
							$scope.isRights='N';
							$scope.badDebitFrom = 0;
							$scope.badDebitTo =0;
							$scope.userDiscountObject = response.data.object;
							if($scope.userDiscountObject != null){
								if($scope.userDiscountObject.isBadDebit == 'Y'){
									$scope.isRights='Y';
									$scope.badDebitFrom = $scope.userDiscountObject.fromBadDebitAmount;
									$scope.badDebitTo = $scope.userDiscountObject.toBadDebitAmount;
								}
							}
								 
							}
							
					});
			//for UserList DD
		}
		
		$scope.getUserBadDebitRights();
		
		$scope.getCompanyMasterList = function(companyTypeId) {
			if(companyTypeId==3){
				
				$scope.isDisable=true;
				
			}else{
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
			  }
		
		$scope.getAssociateCompanyMasterList = function(companyId) {
			$scope.tempcompanyId=companyId;
			$rootScope.startLoader();
			  var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyId:companyId};
			    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getAssociateCompanyMaster"
			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						 $scope.associateCompanyMasterList = response.data.listObject;
						 // console.log("associateCompanyMasterList",JSON.stringify($scope.associateCompanyMasterList));
					}
					$rootScope.stopLoader();
				});
			  }
		
		$scope.searchBills = function(billSearch){
			
			
			var billSearchObj = angular.copy(billSearch);
			if(billSearchObj.paymentEntitlementId!=null && billSearchObj.paymentEntitlementId!=1 && billSearch.companyId==undefined && billSearch.companyId==null)
			{
				growl.warning("Please select company...!!",{title : "WARNING!"
				});
				return false;
			}

			$rootScope.startLoader();
			billSearchObj.patientId = $scope.billSearch.patientId;
			billSearchObj.billStatusId = billSearch.billStatusId == '-' ? null : billSearch.billStatusId;
			if($scope.billSearch.isOtcReg)
				billSearchObj.isOtcReg = 'Y';
			else
				billSearchObj.isOtcReg = 'N';
			
		/*	if($scope.billSearch.fromDate != '' && $scope.billSearch.toDate != '' && typeof($scope.billSearch.fromDate) != "undefined" && typeof($scope.billSearch.toDate) != "undefined"){
				billSearchObj.fromDate = $rootScope.getFormattedDate($scope.billSearch.fromDate,false);
				billSearchObj.toDate = $rootScope.getFormattedDate($scope.billSearch.toDate,false);
			}*/
			billSearchObj.fromDate = moment().subtract($scope.badDebitDuration, 'days').format('YYYY-MM-DD');
			if(billSearchObj.paymentEntitlementId==1)
				billSearchObj.payeeId=$scope.selfPayeeIdCookies;
			else
				billSearchObj.payeeId=billSearch.companyId;
			
			var URI = BASE_URL + ROOT_URL + "/api/billing/badDebitList/filter";
			BillingGenericService.serviceAction(METHOD_POST,URI,billSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					
					$scope.pagination.data=[];
					
					$scope.billList = response.data.listObject;
					
					if(!!response && !!response.data && response.data.listObject.length > 0){            
                        PaginationService.searchData($scope.billList, $scope.pagination);
                      }
					$rootScope.stopLoader();
				}else{
					$rootScope.stopLoader();
				}
			});
		
	}
		
		$scope.initBillSearchObj = function(){
				//$scope.billSearch = {fromDate:"",toDate:"",visitTypeId:"",billNo:"",visitNo:"",billStatusId:"",patientId:"",orgId:$scope.organizationId,unitId:$scope.unitId,isOtcReg:false};
			$scope.billSearch = {fromDate:new Date(),toDate:new Date(),visitTypeId:"",billNo:"",visitNo:"",billStatusId:"",patientId:"",orgId:$scope.organizationId,unitId:$scope.unitId,isOtcReg:false};
		}
		
		
	$scope.searchBillspharmacy= function(pharmacyBillSearchObj){
		$rootScope.stopLoader();
			var URI = BASE_URL + ROOT_URL+ "/api/pharmacy/sales/bills/badDebtBillsearch";
			GenericService.serviceAction("POST", URI, pharmacyBillSearchObj).then(function(response) {
				$scope.searchBadDebtList=[];
				$scope.searchBadDebtList = response.data;
				
				$scope.pagination.data=[];
				
				$scope.searchBadDebtList = response.data;
				
				if(!!response && !!response.data){            
                    PaginationService.searchData($scope.searchBadDebtList, $scope.pagination);
                  }
				
				console.log("$scope.searchNewItemList",$scope.searchBadDebtList);
				$rootScope.stopLoader();
			});
			
		}
	
	$scope.statusAdndSalesTypeDataList = function(){
		var URI = BASE_URL + ROOT_URL + API + "/pharmacy/sales/bills/dropdowns";
		BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			if(response.status == 200){
				$scope.salesTypeList =[];
				$scope.statusDataList=[];
				$scope.salesTypeList = response.data.salesType;
				$scope.statusDataList=response.data.status
				
			}
			console.log("salesTypeList",$scope.salesTypeList);
			console.log("statusDataList",$scope.statusDataList);
		});
	}
	$scope.statusAdndSalesTypeDataList();	
		
		$scope.init = function(){
			
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
			
			$scope.selectedpayeeDetailsObj={};
			$scope.pharmacyBillSearchObj = {
					"salesTypeId"      : 2,
					"billNo"           : "",
					"visitNo"          : "",
					"patientId"        : "",
					"statusId"         : "",
					"fromDate"         : moment().subtract($scope.badDebitDuration, 'days').format('YYYY-MM-DD')
			}
			//$scope.searchBillspharmacy($scope.pharmacyBillSearchObj );
			
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
		$scope.innerTabLevel = 1 ;
		
		$scope.transferToActiveDeposit = "N";
		
		$scope.setFirstTabLevel= function(newTab)
		{
			$scope.firstTabLevel = newTab;
		};
		$scope.isSetFirstTabLevel = function(tabNum)
		{
			return $scope.firstTabLevel === tabNum;
		};
		
		$scope.setInnerTabLevel= function(newTab)
		{
			$scope.innerTabLevel = newTab;
		};
		$scope.isInnerTabLevel = function(tabNum)
		{
			return $scope.innerTabLevel === tabNum;
		};
		
		$rootScope.searchPatient = function(patientsearchObj){
			if($scope.patientSearchObj.uhidNumber == '' && $scope.patientSearchObj.patientName == '' )
			{
				//alert();
				growl.error(SearchFilter_REQUIRED,{title : "WARNING!"
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
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
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
				
				patSearchObj = {};
			});
		}
		
		$scope.getPatientDetailsById = function(patientId){
			$rootScope.startLoader();
			$scope.patientDetailsList = [];
			$scope.patientDetails = {}
			var URI = BASE_URL + ROOT_URL + "/api/global/patient/billing/"+patientId;
			BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					$scope.billSearch = {fromDate:new Date(),visitTypeId:"",billNo:"",visitNo:"",billStatusId:"",patientId:"",orgId:$scope.organizationId,unitId:$scope.unitId};
					$scope.patientDetailsList = response.data.listObject;
					$scope.patientDetails = response.data.listObject[0];
					$scope.patientDetails.patientName = $scope.patientDetails.firstName + " " + $scope.patientDetails.lastName;
					$scope.patientDetails.genderCode = $scope.patientDetails.genderName.charAt(0).toUpperCase();
					$scope.patientDetails.age = $scope.patientDetails.ageString
					$scope.patientDetails.mobileNo = $scope.patientDetails.mobileNumber;
					$scope.billSearch.patientId = $scope.patientDetails.patientId; 
					$scope.pharmacyBillSearchObj.patientId=$scope.patientDetails.patientId; 
					
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
					"encounterId":$scope.encounterId
			}
			console.log("AUTORENDER",obj);
			//return false;
			BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
			{	
				
				if(response.data.status == "success")
				{
					var tempAutoRendServList = [];
					angular.forEach(response.data.listObject,function(value,index){
					/*	if(value.isPackageService == 'Y'){
							value.apportionedPrice = value.basePrice;
							value.basePrice = 0;
							value.netPay = 0;
							value.totalAmt = 0;
							value.selfPayable = 0;
						}*/
						value.packageMasterId = value.packageId;
						value.isServices = false;
						tempAutoRendServList.push(value);
					});
					$scope.pendingServiceListPrePost = tempAutoRendServList;
					if($scope.pendingServiceListPrePost.length > 0)
						$scope.addToBillFlag = false;
					  
					$rootScope.stopLoaderPopup();
				}
				else
					{
					$rootScope.stopLoaderPopup();
					}	
			});
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
			   orderedServicesDtosList.orderDetailsId = item.orderDetailsId;
			   orderedServicesDtosList.ordCancelReasonId = reasonId;
			   orderedServicesDtosList.ordCancelDatetime = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
			   orderedServicesDtosList.updatedBy = $scope.userId;
			   cancelList.push(orderedServicesDtosList);
			   data.orderedServicesDtosList = cancelList;
    		var URI = BASE_URL + ROOT_URL 
			+ "/api/billing/cancelUnRenderedServices";
    		  		
    		BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_TRUE).then(function(response)
			{	
    				if (response.data.status == "success") {
							$scope.getPendingServicesByEncounterId();
							//$scope.billDisabled = false;
							/*$scope.transferToActiveDeposit = "N";
							this.transferToActiveDeposit = "N";*/
							$scope.billDetails($scope.currentBill);
							$scope.calculateTotalDueRefAmount();
							$scope.listPaymentDetails = [];
							$scope.searchBills($scope.billSearch);
							$rootScope.stopLoader();
							
						}
						else
							{
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
						'Please select at least one service..!!',
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
			  // data.orderDetailsPayeeMapperDtosList = item.orderDetailsPayeeMapperDtosList;
			   
    		var URI = BASE_URL + ROOT_URL
			+ "/api/billing/bill/service/cancel";
    		GenericService.serviceAction("POST", URI, data)
			.then(
					function(response) {
						if (response.data.status == "success") {
							
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
						}
					else
						{
						  $scope.billDisabled = false;
						}
				}
			});
			$rootScope.stopLoader();
		}
		

		
	
		 
		
		
	
		
		/**====================================================================================*/
		$scope.amountPaidflag = true;
		$scope.selectedUhid = "";
		$scope.selectedPatientId = "";
		$scope.amountPaid ="";
		$scope.billDetails = function(bill){
			
			
			if(bill.intermediateSelfDue!=null &&  bill.intermediateSelfDue>0){
				
				$scope.billDisabled=true;
				
				growl.warning("Self content of itermediate bill(s) of this admission is not yet settled ..!!",{
					title : WARNING_MSG
				});
				
				
			}
			
			$scope.setFirstTabLevel(1);
			$scope.setInnerTabLevel(1);
			$scope.excessAmount = parseFloat(0).toFixed(2);	
			$scope.amountPaid = bill.amountPaid;
			$scope.selectedUhid = bill.uhidNumber;
			$scope.selectedPatientId = bill.patientId;
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
			$scope.referalDocId = bill.referalDocId;
			$scope.consumedAmt = parseFloat(0);
			$scope.payeeId = $scope.selfPayeeIdCookies;
			
			$scope.billDisabled = (bill.billStatusId == 3 || (bill.intermediateSelfDue!=null &&  bill.intermediateSelfDue>0)) ? true : false;
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
			$scope.admissionId = bill.visitTypeId != 1 ? bill.visitId : null;
			$scope.isFinancialClearance = false;
			$('#isFinancialClearance').prop('checked',false);
			//Vivek added
			$scope.encounterId = bill.visitId;
			$scope.appointmentId = bill.appointmentId;
			//End Vivek Added
			if($scope.visitTypeId == 1 && $scope.isPreOrPostBilling == 'N'){
				$scope.getPendingServicesByEncounterId();
			}
			
			//get payer details
			$scope.initPaymentDetails();
			$scope.mpesaObject={};
			//$scope.getLoyaltyCardList($scope.selectedPatientId);
			var URI = BASE_URL + ROOT_URL + "/api/billing/payee/bill/"+bill.billingMasterId;
			BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success")
				{
					$scope.insurancePayeeExist = false;
					$scope.selfPayeeExist = false;
					$scope.payeeList = response.data.listObject;
					$scope.billServicesList = response.data.object.listObject;
					$scope.paymentReceiptsDetailsList=response.data.object.object;
					var totalAmtQuaBase = parseFloat(0);
					var totalAmtConcession = parseFloat(0);
					var totalAmtDiscount = parseFloat(0);
					var totalAmtGst = parseFloat(0);
					var totalNetPayableAmt = parseFloat(0);
					var discountRoundOff = parseFloat(0);
					var totalSelfPayable = parseFloat(0);
					var totalCreditPayable = parseFloat(0);
//					/payee/bill
					angular.forEach($scope.payeeList,function(value1,key)
					{
						if(value1.payeeId == $scope.selfPayeeIdCookies)
						{
							$scope.selfPayeeExist = true;
						}else{
							$scope.insurancePayeeExist = true;
						}
						
					});
					
					angular.forEach($scope.billServicesList,function(value,key)
							{
						angular.forEach(value.orderDetailsPackageMapperDtosList,function(value1,key)
								{
								  if(value1.isPackageService=='N')
									  {
									    totalAmtQuaBase = totalAmtQuaBase + parseFloat(value.grossAmount);
										totalAmtConcession = totalAmtConcession + parseFloat(value.concession);
										totalAmtDiscount = totalAmtDiscount + parseFloat(value.discount);
										totalAmtGst = totalAmtGst + parseFloat(value.taxAmount);
										totalNetPayableAmt = totalNetPayableAmt + parseFloat(value.netAmt);
									    totalSelfPayable = totalSelfPayable + parseFloat(value.selfPayable);
										totalCreditPayable = totalCreditPayable + parseFloat(value.creditPayable);
										discountRoundOff = parseFloat(value.roundOff);
									  }
								});
					});
					
					$scope.totalAmtQuaBase =totalAmtQuaBase;
					$scope.totalAmtConcession = totalAmtConcession;
					$scope.totalAmtDiscount =totalAmtDiscount+Number(discountRoundOff);
					$scope.totalAmtGst = (totalAmtGst).toFixed(2);
					$scope.totalNetPayableAmt = totalNetPayableAmt+(Number(discountRoundOff));
				}
			});
			
		}
		
		$scope.checkAllServicePrePostFlag = 'N';
		$scope.checkAllServicePrePost = function()
		{
			if(this.checkAllServicePrePostFlag == 'Y'){
				angular.forEach($scope.pendingServiceListPrePost,function(value,key)
						{
							value.isServices = true;
						});
			}
			else{
				angular.forEach($scope.pendingServiceListPrePost,function(value,key)
						{
							value.isServices = false;
						});
			}
			
		}


		
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
				 $scope.pinpadAmounFlag =true;
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
			/*var amount = 0;
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
				}
			else
				{
				$scope.dueAmount = parseInt($scope.billDueAmount) - parseInt($scope.payeeDueAmtStatic);
				$scope.paymentDetails.cashAmount = $rootScope.parseFloatNum($scope.payeeDueAmtFinal);
			    $scope.paymentDetails.chequeAmount = parseFloat($scope.dueAmount).toFixed(2);
			    $scope.paymentDetails.credAmount = parseFloat($scope.dueAmount).toFixed(2);
			    $scope.eTransferObj.transferAmount= parseFloat($scope.dueAmount).toFixed(2);
			    $scope.paymentDetails.loyaltyCardAmt = parseFloat($scope.dueAmount).toFixed(2);
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
				}*/
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
				}
		}
		/******21-05-19*Chetana****/
		
		$scope.getSignBinaryData = function()
		{
			//$("#id_description_iframe").contents().find("body").html()
			
			 var binaryValueData = $('iframe[name=Demo]').contents().find('#finalDataBinary').val();
			 $scope.binaryValueDataForSing = binaryValueData;
			 
			 angular.element('#digitalSignaturePopup').modal('hide');
		}
		/***********/
		// NEW FOR VIVEK
		$scope.settleBill = function(){
			$('#collectPayment').modal('hide');
			$('#billListDetails').modal('hide');
			$("#pahrmacy_billListDetails").modal('hide');
		
			$rootScope.startLoader();
			$scope.billDisabled = true;
			console.log("LISt CHECK",$scope.availableDeposits);
			
			var billPayeeDetailsId = "";
			var payeeDueAmount = 0;
			//Get Bank Name
			angular.forEach($scope.payeeList,function(value,key){
				if(value.payeeId == $scope.payeeIdSettle)
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
					var newArr = {};
					newArr.availableDeposit = $scope.availableDeposits[i].availableDeposit;
					newArr.depositAmount = $scope.availableDeposits[i].depositAmount;
					newArr.depositMasterId =$scope.availableDeposits[i].depositMasterId;
					newArr.organizationId  =  $scope.organizationId;
					newArr.unitId = $scope.unitId;
					newArr.depConsumptionDate =moment(new Date()).format('DD-MM-YYYY HH:MM:SS'); 
					newArr.billId = $scope.billId;	
					newArr.billPayeeDetailsId = billPayeeDetailsId;
					newArr.consumedAmount = (parseFloat($scope.availableDeposits[i].consumedDeposit).toFixed(2) - $scope.availableDeposits[i].oldConsumedDeposit);
					newArr.createdBy = $scope.userId;
					$scope.finalAvailableDeposits.push(newArr);
					//$scope.finalAvailableDeposits.push($scope.availableDeposits[i]);
					}
				i++;
				
			});
			console.log("$scope.finalAvailableDeposits",$scope.finalAvailableDeposits);
			console.log("$scope.listPaymentDetails",$scope.listPaymentDetails);
			
			$scope.payeeId  = $scope.payeeIdSettle;
			
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
					remark : $scope.remarkObj.remark,
					amount :  $scope.listPaymentDetails[0].amount ,
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
					outstanding : $scope.oldDueAmount
					
			}
			
			/*if($scope.payeeId == '' || typeof($scope.payeeId) == "undefined"){
				$scope.billDisabled = false;
				growl.error("Please select a payee",{
					title : ERROR_MSG
				});
				return;
			}*/
			
			
			if(($scope.finalAvailableDeposits.length == 0)&&($scope.listPaymentDetails.length == 0 || $scope.listPaymentDetails == 'undefined')){
				$scope.billDisabled = false;
				growl.error("Please select at least one payment",{
					title : ERROR_MSG
				});
				return;
			}
			
         	if($scope.selectedpayeeDetailsObj.tSaleId!=undefined){
				//pharmacy bill
         		if($scope.requestStatusObject != null)
				{
				 $scope.getCancelDiscountReq(2);
				}
         		
         		$scope.listPaymentPharmacyDetails=[];
         		
         		
         	     var pharmacyBillparam = {
         	            billingMasterId: $scope.selectedpayeeDetailsObj.tSaleId,
         	            patientId :$scope.selectedpayeeDetailsObj.patientId,
         	            orgnizationId:  $scope.organizationId,
         	            unitId:$scope.unitId,
         	            createdBy: $scope.userId ,
         	            amountData: parseFloat($scope.listPaymentDetails[0].amount),
         	            paymentReceiptDetailsDtosList: $scope.listPaymentDetails,
         	           selfPayeeId:$scope.selfPayeeIdCookies,
         	          } 
         	    var URI = BASE_URL + ROOT_URL + API + "/pharmacy/sales/bills/save/bill";
 				BillingGenericService.serviceAction(METHOD_PATCH,URI, pharmacyBillparam, NOTIFICATION_MSG_STATUS_TRUE).then(function(response)
 				{
 					 if (response.data.message == "Sale Details saved successfully"){
 						growl.success("Sale Details saved successfully.",{
							title : SUCCESS_MSG
						});
 					      var data = {
 					    	        "appName": "HIS",
 					    	        "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
 					    	        "reportDesignFilePath": "get_salesbillsReceipt.rpt",
 					    	        "reportFormat": "PDF",
 					    	        "exportFileName": "BillSlip",
 					    	        "reportParams": [
 					    	          {
 					    	            "paramName": "Id",
 					    	            "paramType": "Integer",
 					    	            "paramValue": response.data.id
 					    	          }
 					    	        ]
 					    	      };
 					    	      reportService.generate(REPORT_API_URI1, data);
 					    	      
 						$scope.searchBillspharmacy($scope.pharmacyBillSearchObj );
 					 }
 				});
 				$scope.remark="";
				
			}else{
				console.log("param",param);
				//return false;
				if($scope.requestStatusObject != null)
					{
					 $scope.getCancelDiscountReq(2);
					}
				var URI = BASE_URL + ROOT_URL + API + BILLING + "/payments";
				BillingGenericService.serviceAction(METHOD_POST,URI, param, NOTIFICATION_MSG_STATUS_TRUE).then(function(response)
				{
					if(response.data.status == "success"){
						//collectPayment
						//billListDetails
						$scope.billDisabled = false;
						$scope.validatTextFieldFlag = true;
						$scope.excessAmount = parseFloat(0).toFixed(2);
						$scope.transferToActiveDeposit = 'N'
						this.transferToActiveDeposit = "N";
						$scope.billDetails($scope.currentBill);
						$scope.calculateTotalDueRefAmount();
						$scope.listPaymentDetails = [];
						$scope.mpesaObject={};
						$scope.searchBills($scope.billSearch);
						$scope.receiptId=response.data.object.recieptId;
						$scope.depositConsumptionId=response.data.object.depositConsumptionId;
						$scope.processDocShare();
						$scope.remarkObj = {
								remark : ""
						};
						console.log(response);
						if(response.data.object != null)
						{
							var URIVALI = BASE_URL + ROOT_URL + "/api/billing/receipt/printLog/0/"+$scope.userId+"/"+response.data.object.recieptId;
							BillingGenericService.serviceAction(METHOD_GET,URIVALI, {},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
							{
								$rootScope.stopLoader();
								$scope.remark="";
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
												                }
												              ]
									  };
									reportService.generate(REPORT_API_URI1,data);
								}
							});	  
							
							
							}
				}else{
						$rootScope.stopLoader();
				}
				});
			}
			
			
			/*if(($scope.amountCollected == '' || $scope.amountCollected <= 0) && ($scope.consumedAmt == '' || $scope.consumedAmt <= 0)){
				$scope.billDisabled = false;
				growl.error("Amount can't be zero",{
					title : ERROR_MSG
				});
				return;
			}*/
			
			
		}
		
		$scope.sendApproval = function(){
			
			if($('#requestUserId').val() == '' || $('#requestUserId').val() == null || $('#requestUserId').val() == undefined)
				{

					growl.warning("Please select user.",{
						title : WARNING_MSG
					});
					return false;
				}else{
					$rootScope.startLoaderPopup();
					$scope.payeeId  = $scope.payeeIdSettle;
					var data={};
					if($scope.selectedpayeeDetailsObj.tSaleId==undefined){//service bill request
						data= {
						         
						         visitTypeId:$scope.visitTypeId,
						         encounterId: $scope.encounterId,
						         admissionId:$scope.admissionId,
						         patientId:$scope.patientIdForReport,
						         reqDisAmt :  $scope.listPaymentDetails[0].amount,
								 requestSentTo :$('#requestUserId').val(),
						         selfPayeeId:$scope.payeeId,
						         organisationId : $scope.organizationId, //
								 unitId : $scope.unitId,
								 createdDate : new Date().getTime(),
								 updatedDate : new Date().getTime(),
								 updatedBy:$scope.userId,
								 createdBy:$scope.userId,
								 netAmount : $scope.payeeDueAmtPopup,
								 billingMasterId : $scope.billId
						         
						 };	
					}else{
						
						if($scope.selectedpayeeDetailsObj.tSaleId!=undefined){//pharmacy request
							data={
									 visitTypeId:1, //always gonna be OP
							         patientId:$scope.selectedpayeeDetailsObj.patientId,
							         reqDisAmt :  $scope.listPaymentDetails[0].amount,
									 requestSentTo :$('#requestUserId').val(),
							         selfPayeeId:$scope.selectedpayeeDetailsObj.payeeId,
							         organisationId : $scope.organizationId, //
									 unitId : $scope.unitId,
									 createdDate : new Date().getTime(),
									 updatedDate : new Date().getTime(),
									 updatedBy:$scope.userId,
									 createdBy:$scope.userId,
									 netAmount : $scope.payeeDueAmtPopup,
									 tSaleId:	$scope.selectedpayeeDetailsObj.tSaleId
							};
							if($scope.selectedpayeeDetailsObj.encounterId!=null){
								data.encounterId=$scope.selectedpayeeDetailsObj.encounterId ;
							}
							 if($scope.selectedpayeeDetailsObj.admissionId!=null) {
								 data.admissionId=$scope.selectedpayeeDetailsObj.admissionId;
							 }
							 
						  }
						
					}
					
					 if($scope.payeeId == $scope.selfPayeeIdCookies){
						 data.discType = 9; // for self requestType
					 }else{
						 data.discType = 10; // for company requestType
					 }
					 if($scope.remarkObj.remark != null && $scope.remarkObj.remark != undefined ){
						 data.remark = $scope.remarkObj.remark;
					 }
					
					 console.log(data);
					 
					 var URI = BASE_URL + ROOT_URL + "/api/billing/requestForBadDebit";
					    BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							if (response.data.status == "success"){
								$rootScope.stopLoaderPopup();
								growl.success("Request sent sucessfully.",{
									title : SUCCESS_MSG
								});
								
								$scope.closePopup();
								angular.element('#billListDetails').modal('hide');
								
								
							}else{
								growl.error("Something went wrong.",{
									title : ERROR_MSG
								});
								return false;
							}
							
						});
					 
					 
					 // netAmount:,
			         //requestType:,
				
						
				}
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
			angular.element('#collectPayment').modal('hide');
			$scope.billDisabled = false;
			$scope.transferToActiveDeposit = "N";
			this.transferToActiveDeposit = "N";
			$scope.calculateTotalDueRefAmount();
			$scope.listPaymentDetails = [];
			$scope.remark = '';
			$scope.requestUserId = '';
			$scope.mpesaObject={};
			$scope.searchBills($scope.billSearch);
		}
		
		/*$scope.checkAmount = function(amount)
		{
			if(this.transferToActiveDeposit == 'N'){
			if(parseFloat(amount) > parseFloat($scope.payeeDueAmtFinal))
				{
				growl.error("Entered amount should not be greater than self due amount...!!",{
					title : ERROR_MSG
				});
				$scope.paymentDetails.cashAmount = parseFloat($scope.payeeDueAmtFinal).toFixed(2);
				return;
					
				}
			}
		}
*/		
		// OLD FOR YOGESH
		/*$scope.settleBill = function(){
			//alert('SET');
			var param = {
					billingMasterId : $scope.billId,
					payeeId : $scope.payeeId,
					listBillingPaymentDetailsReqDto : $scope.listPaymentDetails,
					orgId : $scope.organizationId,
					unitId : $scope.unitId, 
					appointmentId:$scope.appointmentId,
					updatedBy:$scope.userId,
					createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					isPreOrPostBilling:$scope.isPreOrPostBilling,
					encounterId : $scope.encounterId,
					isFinancialClearance : $scope.isFinancialClearance ? 'Y' : 'N',
					admissionId : $scope.admissionId,
			}
			
			if($scope.payeeId == '' || typeof($scope.payeeId) == "undefined"){
				growl.error("Please select a payee",{
					title : ERROR_MSG
				});
				return;
			}
			
			if($scope.listPaymentDetails.length == 0 || $scope.listPaymentDetails == 'undefined'){
				growl.error("Please select at least one payment",{
					title : ERROR_MSG
				});
				return;
			}
			
			if($scope.amountCollected == '' || $scope.amountCollected <= 0){
				growl.error("Amount can't be zero",{
					title : ERROR_MSG
				});
				return;
			}
			console.log('settle obj',param);
			//return false;
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + API + BILLING + "/payments";
			BillingGenericService.serviceAction(METHOD_POST,URI, param, NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
				if(response.data.status == "success"){
					//collectPayment
					//billListDetails
					angular.element('#collectPayment').modal('hide');
					angular.element('#billListDetails').modal('hide');
					$scope.billDetails($scope.currentBill);
					$scope.calculateTotalDueRefAmount();
					$scope.listPaymentDetails = [];
					$scope.searchBills($scope.billSearch);
				}
			});
			$rootScope.stopLoader();
		}*/

		
		$scope.getUserRigthsList = function(dueAmt){
			var URI = BASE_URL + ROOT_URL
    		+ "/billing/getUsersListHavingDiscRightsForBadDebit/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/amt/"+dueAmt;
    BillingGenericService.serviceAction(METHOD_GET, URI,"",
    				NOTIFICATION_MSG_STATUS_FALSE)
    		.then(function(response) {
    					console.log(response);
    					// $rootScope.startLoader();
    					if (response.data.status == "success")
    						{
    						  $scope.userDiscountList =[];
    						  $scope.userDiscountList = response.data.listObject;
    						}
    				});
		}
		
		$scope.remarkObj = {
				remark : ""
		};
		$scope.settlementClick=function(item)
		{
			$('#requestUserId').val("");
			$('#remark').val("");
		
			$scope.showUserListFlag = 'N';
				if($scope.isRights == 'N'){
					$scope.showUserListFlag = 'Y';
					$scope.getUserRigthsList(item.dueAmount);
				
				}  
				if($scope.isRights == 'Y' && ($scope.badDebitFrom <= item.dueAmount && $scope.badDebitTo >= item.dueAmount) ){
					
				}else{
					$scope.showUserListFlag = 'Y';
					$scope.getUserRigthsList(item.dueAmount);
				}
				$scope.payeeDueAmtFinal = parseFloat(0);
				$scope.remarkObj.remark = '';
				$scope.listPaymentDetails = [];
				$scope.requestUserId = '';
				
//			 	     $scope.setFirstTabLevel(1);
					 $scope.transferToActiveDeposit = 'N'
					 $scope.excessAmount = parseFloat(0).toFixed(2);
					 $scope.send=false;
					 $scope.resend=false;
					 $scope.validatTextFieldFlag = true;
					 $scope.payeeDueAmtPopup = item.dueAmount;
					 $scope.payeeDueAmtFinal = item.dueAmount;
					 $scope.paymentDetails.cashAmount = parseFloat(item.dueAmount).toFixed(2);
					 $scope.payeeIdSettle = item.payeeId;
					
					//angular.element('#collectPayment').modal('show');
						
						
				//ForGetRequestObjectSTART
					 
					 $scope.requestStatusObject = {};
					 
					 $scope.requestApporivedDiscountDiv = false;
						$scope.discountDisabledFlagForRequest = false;
						$scope.requestAppUser = "";
						$scope.approvedAmtApp = "";
						$scope.requestAmtApp ="";
						$scope.statusApp = "";
						
						
					 var data1 = {};
					 if(item.tSaleId==undefined){
						 //getservice approvals
						  data1.billId = $scope.billId;
						  data1.requestType = 1;
					 }else{
						//getpharmacy approvals
						 data1.tSaleId = item.tSaleId;
						  data1.requestType = 1;
						 
					 }
					 
								
					 
					 
					 if(item.payeeId == $scope.selfPayeeIdCookies){
						 data1.requestType = 9; // for self requestType
					 }else{
						 data1.requestType = 10; // for company requestType
					 }
					 
						
						var URI1 = BASE_URL + ROOT_URL + "/api/billing/getBadDebitRequestObject";
						BillingGenericService.serviceAction(METHOD_POST,URI1, data1, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
							if(response.data.status == "success"){
								if(response.data.listObject.length > 0){
								$scope.requestStatusObject = response.data.listObject[0];
								if($scope.requestStatusObject.reqStatus == 2)
									{
									$scope.requestApporivedDiscountDiv = true;
									$scope.discountDisabledFlagForRequest = true;
									$scope.requestAppUser = $scope.requestStatusObject.requestSentToEmployee;
									$scope.approvedAmtApp = $scope.requestStatusObject.approvedDiscAmt;
									$scope.requestAmtApp =$scope.requestStatusObject.reqDisAmt;
									$scope.statusApp = "Approved.";
									$scope.reqStatus = 2;
									$scope.requestAmtRemark =$scope.requestStatusObject.remark;
									
									
						    		}
								else if($scope.requestStatusObject.reqStatus == 1)
									{
									    $scope.discountDisabledFlagForRequest = true;
									    $scope.requestApporivedDiscountDiv = true;
									    $scope.requestAppUser = $scope.requestStatusObject.requestSentToEmployee;
										$scope.approvedAmtApp = $scope.requestStatusObject.approvedDiscAmt;
										$scope.requestAmtApp =$scope.requestStatusObject.reqDisAmt;
										$scope.statusApp = "Pending.";
										$scope.reqStatus = 1;
										$scope.requestAmtRemark =$scope.requestStatusObject.remark;
									}
								else
									{
									$scope.requestStatusObject = null;
									}
								
								}
								
								else{
									$scope.requestStatusObject = null;
									}
								}
						});
						
				//END
			
		}
		
		
		 $scope.getCancelDiscountReq = function(type)
			{
				
				
				$rootScope.startLoaderPopup();
				var data = {
						reqDiscId :$scope.requestStatusObject.reqDiscId,
				}
				
				var URI = BASE_URL + ROOT_URL + "/api/billing/cancelRequest";
				
				GenericService
				.serviceAction("POST", URI, data)
				.then(function(response) {
							$rootScope.stopLoaderPopup();
							if (response.data.status == "success") {
								
						    	   if(type == 1){
								growl.success("Cancelled !!!!",{
									title : 'success'
								});
						    	   }
						    	$scope.showUserListFlag = 'Y'; 
								$scope.requestApporivedDiscountDiv = false;
								$scope.discountDisabledFlagForRequest = false;
								$scope.requestAppUser = 0;
								$scope.approvedAmtApp = 0;
								$scope.requestAmtApp =0;
								$scope.statusApp = "";
								$scope.reqStatus = '';
								$scope.requestUserId = '';
								$('#requestUserId').val(null);
					    		angular.element('#discountCancellationModalReq').modal('hide');
							}
						});
				
			}
		/**********************/
		$scope.printBill = function(bill,IsSummary)
		{
			
			$("#bill").show();
			$scope.billingMasterId=bill.billingMasterId;
			$scope.patientId=bill.patientId;
			$scope.visitTypeId=bill.visitTypeId;
			$scope.IsSummary=IsSummary;
			
		}
		$scope.printBillNew=function(){
			var URIVALI = BASE_URL + ROOT_URL + "/api/billing/printLog/1/"+$scope.userId+"/"+$scope.billingMasterId;
			BillingGenericService.serviceAction(METHOD_GET,URIVALI, {},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
			{
				if (response.data.status == "success"){
					
					if($scope.visitTypeId==1)
					{
						var data =  {
					                 "appName": "HIS",
					                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
					                 "reportDesignFilePath": "opdbillreceipt.rpt",
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
						                  "paramValue": 1
							           }
					                 ]
					      };
					}else{
						var data =  {
					  			
				                 "appName": "HIS",
				                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
				                 "reportDesignFilePath": "Ipdbill.rpt",
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
					                	"paramValue": 1
					                }
		                ]
						};
					}
					reportService.generate(REPORT_API_URI1,data);
					$("#bill").hide();
					
				}
			});
			
		}
		//Ganesh Start
		$scope.depositeSub = function(item,idx)
		{
			
			if($scope.payeeDueAmtFinal > 0 || item.depositeFlag == 'N'){
			var consumedAmt = 0;
			if(item.depositeFlag == 'Y')
				{
					if($scope.dueAmount == 0)
					{
						$scope.availableDeposits[idx].depositeFlag = 'N';
					}
					else if($scope.dueAmount >= item.availableDeposit)
						{
							consumedAmt = (parseFloat(consumedAmt)+parseFloat(item.availableDeposit)).toFixed(2);
							$scope.availableDeposits[idx].consumedDeposit = parseFloat(item.consumedDeposit)+parseFloat(item.availableDeposit);
							$scope.availableDeposits[idx].availableDeposit = parseFloat(0);
							$scope.dueAmount = (parseFloat($scope.dueAmount)- parseFloat(consumedAmt)).toFixed(2);
						}
					else if($scope.dueAmount <= item.availableDeposit)
						{
							consumedAmt = (parseFloat(consumedAmt)+parseFloat($scope.dueAmount)).toFixed(2);
							$scope.availableDeposits[idx].availableDeposit = parseFloat(item.availableDeposit)- parseFloat($scope.dueAmount);
							$scope.availableDeposits[idx].consumedDeposit =item.consumedDeposit + parseFloat($scope.dueAmount);
							$scope.dueAmount = (parseFloat($scope.dueAmount)- parseFloat($scope.dueAmount)).toFixed(2);
						}
					 
				}
			if(item.depositeFlag == 'N')
			{
						if($scope.availableDeposits[idx].availableDeposit > 0)
							{
							$scope.availableDeposits[idx].availableDeposit = parseFloat(item.oldAvailableDeposit);
							$scope.availableDeposits[idx].consumedDeposit = parseFloat(item.oldConsumedDeposit);
							$scope.dueAmount = (parseFloat($scope.dueAmount)+ parseFloat(item.oldAvailableDeposit-item.availableDeposit)).toFixed(2);
							}
						else{
						$scope.availableDeposits[idx].availableDeposit = $scope.availableDeposits[idx].availableDeposit + parseFloat(item.consumedDeposit-item.oldConsumedDeposit);
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
			
			
		$scope.addPaymentDetails = function(){
			
			$scope.isBillList = 0;
			
			var exAmount = $scope.paymentDetails.cashAmount;
			
			if(parseFloat(exAmount) == 0 || parseFloat(exAmount) < 0)
			{
					growl.warning("Amount ZERO(0) is not allowed.",{
						title : WARNING_MSG
					});
				
					$scope.paymentDetails.cashAmount = $scope.payeeDueAmtFinal;
					return false;
			
			}
			else
			{
					if(parseFloat(exAmount) <= parseFloat($scope.payeeDueAmtFinal))
					{
						if($scope.requestStatusObject == null){
							
							
							if($scope.listPaymentDetails.length == 0){
								
								 var paymentDetails = {paymentModeId:9, amount:parseFloat($scope.paymentDetails.cashAmount).toFixed(2),createdBy:$scope.userId};
								   $scope.listPaymentDetails.push(paymentDetails);
								   
							}else{
								$scope.listPaymentDetails[0].amount = parseFloat($scope.listPaymentDetails[0].amount) + parseFloat($scope.paymentDetails.cashAmount);
							}
							 
							
							if($scope.isRights == 'Y' && ($scope.badDebitFrom <= $scope.listPaymentDetails[0].amount && $scope.badDebitTo >= $scope.listPaymentDetails[0].amount) ){
								$scope.showUserListFlag = 'N';
							}else{
								$scope.showUserListFlag = 'Y';
								//$scope.getUserRigthsList(item.dueAmount);
							}
							   $scope.payeeDueAmtFinal = parseFloat($scope.payeeDueAmtFinal) - parseFloat(exAmount);
							   $scope.paymentDetails.cashAmount = $scope.payeeDueAmtFinal;
						}else{
								var tmpAmt = 0;
							if($scope.listPaymentDetails.length == 0){
								tmpAmt = $scope.paymentDetails.cashAmount;
							}else{
								tmpAmt = parseFloat($scope.listPaymentDetails[0].amount) + parseFloat($scope.paymentDetails.cashAmount);
							}
							
							if($scope.isRights == 'Y' && ($scope.badDebitFrom <= tmpAmt && $scope.badDebitTo >= tmpAmt) ){
									$scope.showUserListFlag = 'N';
									$scope.payeeDueAmtFinal = parseFloat($scope.payeeDueAmtFinal) - parseFloat($scope.paymentDetails.cashAmount);
									if($scope.listPaymentDetails.length == 0){
										
										 var paymentDetails = {paymentModeId:9, amount:parseFloat($scope.paymentDetails.cashAmount).toFixed(2),createdBy:$scope.userId};
										   $scope.listPaymentDetails.push(paymentDetails);
										   
									}else{
										$scope.listPaymentDetails[0].amount = parseFloat($scope.listPaymentDetails[0].amount) + parseFloat($scope.paymentDetails.cashAmount);
								}
								$scope.paymentDetails.cashAmount = $scope.payeeDueAmtFinal;
							}else if($scope.isRights == 'Y' && ($scope.badDebitFrom <= tmpAmt && $scope.badDebitTo <= tmpAmt) ){
								if($scope.reqStatus == 1){
									
									/*$scope.requestApporivedDiscountDiv = false;
									$scope.discountDisabledFlagForRequest = false;
									$scope.requestAppUser = 0;
									$scope.approvedAmtApp = 0;
									$scope.requestAmtApp =0;
									$scope.statusApp = "";
									$scope.reqStatus = '';*/
									if($scope.listPaymentDetails.length > 0)
										{
										$scope.payeeDueAmtFinal = parseFloat($scope.payeeDueAmtFinal) + parseFloat($scope.listPaymentDetails[0].amount);
										}
								    $scope.listPaymentDetails = [];
								    
									growl.warning("Request already pending, please cancel this first and generate new request,,Thank You.",{
										title : WARNING_MSG
									});
									$scope.showUserListFlag = 'Y';
									$scope.paymentDetails.cashAmount = $scope.payeeDueAmtFinal;
									return false;
									
								}
								else if($scope.reqStatus == 2)
									{
										if($scope.approvedAmtApp >= tmpAmt)
											{
												if($scope.listPaymentDetails.length == 0){
													 var paymentDetails = {paymentModeId:9, amount:parseFloat($scope.paymentDetails.cashAmount).toFixed(2),createdBy:$scope.userId};
													   $scope.listPaymentDetails.push(paymentDetails);
												}else{
													$scope.listPaymentDetails[0].amount = parseFloat($scope.listPaymentDetails[0].amount) + parseFloat($scope.paymentDetails.cashAmount);
												}
												
												$scope.showUserListFlag = 'N';
												$scope.payeeDueAmtFinal = parseFloat($scope.payeeDueAmtFinal) - parseFloat($scope.paymentDetails.cashAmount);
												$scope.paymentDetails.cashAmount = $scope.payeeDueAmtFinal;
											}else{
												growl.warning("Approved amount is  "+ $scope.approvedAmtApp,{
													title : WARNING_MSG
												});
												 $scope.paymentDetails.cashAmount = $scope.payeeDueAmtFinal;
												 return false;
											}
									}
							}else if($scope.isRights == 'N')
								{
										if($scope.reqStatus == 1){
											
											/*$scope.requestApporivedDiscountDiv = false;
											$scope.discountDisabledFlagForRequest = false;
											$scope.requestAppUser = 0;
											$scope.approvedAmtApp = 0;
											$scope.requestAmtApp =0;
											$scope.statusApp = "";
											$scope.reqStatus = '';*/
											if($scope.listPaymentDetails.length > 0)
												{
												$scope.payeeDueAmtFinal = parseFloat($scope.payeeDueAmtFinal) + parseFloat($scope.listPaymentDetails[0].amount);
												}
										    $scope.listPaymentDetails = [];
										    
											growl.warning("Request already pending, please cancel this first and generate new request,,Thank You.",{
												title : WARNING_MSG
											});
											$scope.showUserListFlag = 'Y';
											$scope.paymentDetails.cashAmount = $scope.payeeDueAmtFinal;
											return false;
											
										}
										else if($scope.reqStatus == 2)
											{
												if($scope.approvedAmtApp >= tmpAmt)
													{
														if($scope.listPaymentDetails.length == 0){
															 var paymentDetails = {paymentModeId:9, amount:parseFloat($scope.paymentDetails.cashAmount).toFixed(2),createdBy:$scope.userId};
															   $scope.listPaymentDetails.push(paymentDetails);
														}else{
															$scope.listPaymentDetails[0].amount = parseFloat($scope.listPaymentDetails[0].amount) + parseFloat($scope.paymentDetails.cashAmount);
														}
														
														$scope.showUserListFlag = 'N';
														$scope.payeeDueAmtFinal = parseFloat($scope.payeeDueAmtFinal) - parseFloat($scope.paymentDetails.cashAmount);
														$scope.paymentDetails.cashAmount = $scope.payeeDueAmtFinal;
													}else{
														growl.warning("Approved amount is  "+ $scope.approvedAmtApp,{
															title : WARNING_MSG
														});
														 $scope.paymentDetails.cashAmount = $scope.payeeDueAmtFinal;
														 return false;
													}
											}
								}
							
						}
						
					}else{  
								growl.warning("Amount should be less than or equal to "+ $scope.payeeDueAmtFinal,{
									title : WARNING_MSG
								});
								 $scope.paymentDetails.cashAmount = $scope.payeeDueAmtFinal;
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
			$("#confirmSave").modal("hide");
			parentChild += 1;
		}
		
		$scope.changeExAmount = function(){
			$scope.excessAmount = parseFloat(0.0).toFixed(2);
		}
		
		/*$scope.checkValueGreater = function(){
			
		}*/
		
		$scope.removePaymentDetails = function(index){
			
			var tempObj = (index==undefined)?null:$scope.listPaymentDetails[index];
		
			$scope.paymentDetails.cashAmount = parseFloat($scope.paymentDetails.cashAmount) + parseFloat(tempObj.amount);
			 $scope.payeeDueAmtFinal = parseFloat($scope.payeeDueAmtFinal) + parseFloat(tempObj.amount);
			$scope.listPaymentDetails.splice(index,1);
			
			if($scope.isRights == 'Y' && ($scope.badDebitFrom <=  $scope.payeeDueAmtFinal && $scope.badDebitTo >=  $scope.payeeDueAmtFinal) ){
				$scope.showUserListFlag = 'N';
			}else{
				$scope.showUserListFlag = 'Y';
			}
			
			
			//calculate due amount by page
			switch($scope.page){
			case "billList":
				//scope.calculateTotalDueRefAmount();
				break;
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
		
		$scope.rePrintOPDBillReceipt=function(index)
		{
			var URIVALI = BASE_URL + ROOT_URL + "/api/billing/receipt/printLog/1/"+$scope.userId+"/"+response.data.object.recieptId;
			BillingGenericService.serviceAction(METHOD_GET,URIVALI, {},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
			{
				$rootScope.stopLoader();
				if (response.data.status == "success")
				{
					 var data =  {
							  
			                 "appName": "HIS",
			                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
			                 "reportDesignFilePath": "OPDPaymentReceipt.rpt",
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
			console.log("MPesa Object:",mpesaObject);
			
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
						$rootScope.stopLoader();
					}
				else if(response.data.status == "error"){
					
					growl.error(response.data.message,{
						title : ERROR_MSG
					});
					$scope.mpesaObject.mpesaAmount=null;
					$rootScope.stopLoader();
				}
				
			});
			}else{
				growl.error("Enter both transaction id and mobile number",{
					title : ERROR_MSG
				});
			}
		}
		
		/*$scope.mobileNumber ="";
		$scope.validateMpesaAmount ="";*/
		
		$scope.mpesaPaymentRequest = function(validateMpesaAmount,mobileNumber,countryCallingCode){
			
			if(validateMpesaAmount > $scope.payeeDueAmtFinal){
				
				growl.warning("Mpesa amount is should be equel or less than due amount ",{
					title : "warning!!!"
				});
				
			}else{
				
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
				$rootScope.startLoader();
				var URIVALI = BASE_URL + ROOT_URL + "/Billing/requestSTKPushPayment";
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
				}else{
					growl.error("Please enter mobile code and mobile number. ",{
						title : ERROR_MSG
					});
				}
			}
			
			
			
		}
		
	$scope.validateSTKPushPayment = function(){
			
			var data ={
					organizationId :$scope.organizationId,
					unitId : $scope.unitId,
					uhidNumber: $scope.selectedUhid
					
			};
			$rootScope.startLoader();
			var URIVALI = BASE_URL + ROOT_URL + "/Billing/validateSTKPushPayment";
			BillingGenericService.serviceAction(METHOD_POST,URIVALI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				
				if (response.data.status == "success")
					{
					$scope.mpesaObject.mpesaAmount = response.data.object.amount;
					$scope.mpesaObject.transactionId = response.data.object.checkoutRequestId;
					$scope.mpesaObject.resTransactionId = response.data.object.checkoutRequestId;
					$scope.mpesaObject.mobileNo = response.data.object.phoneNumber;
					$scope.addPaymentDetails();
					growl.success(response.data.message,{
						title : SUCCESS_MSG
					});
					$rootScope.stopLoader();
					}
				else if(response.data.status == "error"){
					
					growl.error(response.data.message != null ? response.data.message :"Please wait",{
						title : ERROR_MSG
					});
					$rootScope.stopLoader();
				}
				
			});
			
		}
		
	$scope.getPharmacyBillDetails=function(pharmacyBill){
		var URI = BASE_URL + ROOT_URL +"/api/pharmacy/sales/bills/badDebtBillDetials/"+pharmacyBill.id;
		GenericService.serviceAction(METHOD_GET,URI, {}).then(function(response){
			if(response.status == 200){
				$scope.pharmacybillDetailsList =[];
				$scope.pharmacybillDetailsList = response.data;
				$scope.setFirstTabLevel(2);
				console.log("pharmacybillDetailsList",$scope.pharmacybillDetailsList);
				$("#pahrmacy_billListDetails").modal("show");
			}else{
				$("#pahrmacy_billListDetails").modal("show");
			}
			
		});
		
		
		
	} 
	
	$scope.selectedSalesObject=function(payee){
	
		$scope.selectedpayeeDetailsObj={};
		$scope.selectedpayeeDetailsObj=payee;
	}
	$scope.clearsalesObject=function(){
		$scope.selectedpayeeDetailsObj={};
	}
	
	$scope.getPharmacyPayeeBillDetails=function(pharmacyBill){
		var URI = BASE_URL + ROOT_URL +"/api/pharmacy/sales/bills/badDebtBillPayeeDetails/"+pharmacyBill.id;
		GenericService.serviceAction(METHOD_GET,URI, {}).then(function(response){
			if(response.status == 200){
				$scope.payeePharmacyList =[];
				if( response.data!=null && response.data.length > 0)
				       $scope.payeePharmacyList = response.data;
						if($scope.payeePharmacyList>0){
							angular.forEach($scope.payeePharmacyList, function(value, key) { 
								value.paidAmount = parseFloat(value.billAmount)-parseFloat(value.dueAmount);
							})
						}
						
				
				console.log("pharmacybillDetailsList",$scope.payeePharmacyList);
			}			
		});
		
		
		
	}
	
		/* *************************************Mpesa Integration END****************************************** */
		
	$scope.checkValidatePinPad = function()
	{
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
             
             
//					/$scope.ipAddress+
					var urlprotocol = $location.protocol();
					//var finalUrl = "http://"+$scope.ipAddress+":9991/api/sale";
					var finalUrl = "http://127.0.0.1:9991/api/sale";
					var URI = finalUrl;
					BillingGenericService.serviceAction(METHOD_POST,URI,dataForPinPad,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						/*if (response.data.status == "success"){
							console.log("ree",response);
							console.log("reew",response.data);
							console.log("reer",response.data.rspCode);*/
							
							if(response.data.rspCode == '100' || response.data.rspCode == '003' || response.data.rspCode == '000')
								{
									$scope.paymentDetails.credCardHolderName = response.data.cardholderName;
									$scope.paymentDetails.credAmount = parseFloat(response.data.amount)/100;
									$scope.schema = response.data.scheme;
									var pan = response.data.pan;
									var lastFour = pan.substr(pan.length - 4);
									$scope.paymentDetails.credCardNumber = lastFour;
									
									
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
											createdDate : moment(new Date()).format("DD-MM-YYYY"),
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
									
									growl.success(response.data.message,{
										title : "SUCCESS!"
									});
									
								
								}
							else
								{
									
									growl.error(response.data.message,{
										title : ERROR_MSG
									});
									return false;
								}
						/*}*/
					});
					
					$rootScope.stopLoader();
				}
			else {
				
				growl.error("Someting went wrong..!!",{
					title : ERROR_MSG
				});
				$rootScope.stopLoader();
			}
			
		});
	   }
		$scope.cancelExAmount = function(){
			$('#confirmSave').modal('hide');
			$scope.paymentDetails.cashAmount = parseFloat($scope.dueAmount).toFixed(2);
			$scope.excessAmount = parseFloat(0.0).toFixed(2);
		}
		$scope.init();
		$scope.initTotalAmounts();
		
	}catch(e){
		console.log(e.message);
	}
	
	
    // Method to get the list of Generic
	$scope.GetGenericList = function () {
		$scope.genericList=[];
        var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.GENERIC_MASTER.GENERIC_MASTER_API+ $scope.organizationId;
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
        	  $scope.genericList = response.data;
        	  $scope.GetGenericMaster();
            $rootScope.stopLoader();
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
          });
      }
    $scope.GetGenericList();
    
    $scope.genericMasterList = [];
    $scope.GetGenericMaster = function () {
        var arraylist = $scope.genericList.filter(x => x.status == true);
        for (let generic of arraylist){
        	$scope.genericMasterList.push(generic);
        }       
        $rootScope.stopLoader();
      } 

    $scope.GetStrengthUnitList = function () {

        var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY. ITEM_MASTER.UNIT_API + '/'+$scope.organizationId ;
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
            function (response) {
            	$scope.strengthUnitList = response.data;
              console.log("Strength Units : ",$scope.strengthUnitList);
              $rootScope.stopLoader();
            },
            function errorCallback(response) {
              console.log("Error");
              console.log("Strength Units : ",$scope.strengthUnitList);
              $rootScope.stopLoader();
            });
      }
    $scope.GetStrengthUnitList();
    
    
	
	$scope.GetGenericMaster = function () {
       var arraylist = $scope.genericList.filter(x => x.status == true);
       for (let generic of arraylist){
    	   $scope.genericMasterList.push(generic);
       }       
       $rootScope.stopLoader();
     }
     
     
     
     $scope.genericCombinationList=[];
	  $scope.ViewCombinationDetails = function(item) {
		  $scope.genericCombinationList = [];
	        var URI = CONSTANTS.MASTER.GLOBAL.PHARAMACY.GENERIC_MASTER.GENERIC_MASTER_IS_COMBINATION_DETAILS_API + item.genericId;
	  
	        $rootScope.startLoader();
	        GenericService.serviceAction("GET", URI, {}).then(
	          function (response) {
	            for(let combination of response.data) {
	              let combinationName = $scope.genericMasterList.filter(x => x.id == combination.subGenericId);
	              combination.name = combinationName.length > 0 ? combinationName[0].name : '-';
	              combination.code = item.code;
	              combination.genericName = item.name;
	            }
	            $scope.strengthUnitList.forEach(function (strength){
	                response.data.forEach(function (data){
	                  if(data.strength == strength.id){
	                    $scope.strength = strength.unit;
	                    var obj = {
	                      strength: "",
	                      code:"",
	                      genericId: "",
	                      genericName: "",
	                      id:"",
	                      name:"",
	                      strengthUnit:"",
	                      subGenericId: "",
	                    }
	                    obj.strength = $scope.strength;
	                    obj.code = data.code;
	                    obj.genericId = data.genericId;
	                    obj.genericName = data.genericName;
	                    obj.id = data.id;
	                    obj.name = data.name;
	                    obj.strengthUnit = data.strengthUnit;
	                    obj.subGenericId = data.subGenericId;
	                    $scope.genericCombinationList.push(obj);
	                  }
	                });
	              });
	            var popup = angular.element("#combinationDetails");
	            popup.modal("show");
	            $rootScope.stopLoader();
	          },
	          function errorCallback(response) {
	            $rootScope.stopLoader();
	          });
	      }
}]);