angular.module("myApp").controller("cancelledBillListController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','paymentCollFactory','reportService','GenericService','PagerService', '$location','loyaltiCardCallFactory','$timeout',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,growl,paymentCollFactory,reportService,GenericService,PagerService,$location,loyaltiCardCallFactory,$timeout){
	try{
		$rootScope.loginpage = true;
		$scope.page = "billList";
		var date = new Date();
		$scope.dateOptions = {
				formatYear : 'yyyy',
				showWeeks : false,
				//minDate : moment(new Date()).subtract(171, 'days').toDate(),
//				minDate : date.setDate((new Date()).getDate() - 180),
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
		$scope.offset = 0;
		$scope.noOfRecordsPerPage = 10;
		$scope.totalNoOfRecords;
		$scope.searchKeyword;
		$scope.commonListCount;
		$scope.insurancePayeeExist = false;
		$scope.selfPayeeExist = false;
		
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
		
		$scope.virtualStoreId = undefined;
		  $scope.getVirtualStore=function(){
		    	 
			  var obj={
					  unitId : $scope.unitId,
	    	  }
			    	  var URI = BASE_URL + ROOT_URL + "/api/global/getVirtualStoreId";
			
			GenericService.serviceAction("POST", URI, obj).then(function(response) {
			$scope.orderedMedicationList = [];
			
					if (response.data.status == "success") {
						$scope.virtualStoreId = response.data.listObject[0].virtualStoreId;
					
					}
			});
	      }
		  $scope.getVirtualStore();
		
		
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
		
	
		
		//For Paginations Start
		$scope.setNoOfRecords = function() {
			$scope.searchBills($scope.billSearch);
		};
		$scope.searchBills = function(billSearch) {
			$scope.searchBillFilter = '';
			offset = $scope.offset != null ? $scope.offset : 0;
			noOfRecordsPerPage = $scope.noOfRecordsPerPage != null ? $scope.noOfRecordsPerPage : 10;
			
			$rootScope.startLoader();
			var billSearchObj = angular.copy(billSearch);
			billSearchObj.patientId = $scope.billSearch.patientId;
			billSearchObj.billStatusId = billSearch.billStatusId == '-' ? null : billSearch.billStatusId;
			if($scope.billSearch.isOtcReg)
				billSearchObj.isOtcReg = 'Y';
			else
				billSearchObj.isOtcReg = 'N';
			
			if($scope.billSearch.fromDate != '' && $scope.billSearch.toDate != '' && typeof($scope.billSearch.fromDate) != "undefined" && typeof($scope.billSearch.toDate) != "undefined"){
				billSearchObj.fromDate = $rootScope.getFormattedDate($scope.billSearch.fromDate,false);
				billSearchObj.toDate = $rootScope.getFormattedDate($scope.billSearch.toDate,false);
			}
			billSearchObj.offset = offset;
			billSearchObj.noOfRecordsPerPage = noOfRecordsPerPage;
			
			var URI = BASE_URL + ROOT_URL + "/api/billing/cancelledBill/filter";
			BillingGenericService.serviceAction(METHOD_POST,URI,billSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					$scope.billList = response.data.listObject;
					$scope.commonListCount = response.data.object;
					$scope.pager = {};
					$scope.page;
					$scope.setPage(1,false);
					
					$rootScope.stopLoader();
				}else{
					$rootScope.stopLoader();
				}
			});


		}

		$scope.getsearchBills = function(billSearch,orgId,offset, noOfRecordsPerPage) {
			console.log("offset", offset);
			var data = "";
			offset = offset != null ? offset : 0;
			noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;
			$scope.searchBillFilter = '';
			$rootScope.startLoader();
			var billSearchObj = angular.copy(billSearch);
			billSearchObj.patientId = $scope.billSearch.patientId;
			billSearchObj.billStatusId = billSearch.billStatusId == '-' ? null : billSearch.billStatusId;
			if($scope.billSearch.isOtcReg)
				billSearchObj.isOtcReg = 'Y';
			else
				billSearchObj.isOtcReg = 'N';
			
			if($scope.billSearch.fromDate != '' && $scope.billSearch.toDate != '' && typeof($scope.billSearch.fromDate) != "undefined" && typeof($scope.billSearch.toDate) != "undefined"){
				billSearchObj.fromDate = $rootScope.getFormattedDate($scope.billSearch.fromDate,false);
				billSearchObj.toDate = $rootScope.getFormattedDate($scope.billSearch.toDate,false);
			}
			billSearchObj.offset = offset;
			billSearchObj.noOfRecordsPerPage = noOfRecordsPerPage;
			
			
			var URI = BASE_URL + ROOT_URL + "/api/billing/cancelledBill/filter";
			BillingGenericService.serviceAction(METHOD_POST,URI,billSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					$scope.billList = response.data.listObject;
					$rootScope.stopLoader();
				}else{
					$rootScope.stopLoader();
				}
			});
		}

		$scope.pager = {};
		$scope.page;

		$scope.setPage = function(page, flag) {
			if (page < 1 || page > $scope.pager.totalPages) {
				return;
			}
			$scope.pager = PagerService.GetPager( $scope.commonListCount, page, $scope.noOfRecordsPerPage);
			if (flag) {
				$scope.getsearchBills($scope.billSearch,$scope.organizationId, $scope.pager.startIndex, $scope.pager.pageSize);
			}
		}

//		$scope.searchBills(billSearch);

		// For Paginations End
		
		
		$scope.initBillSearchObj = function(){
				//$scope.billSearch = {fromDate:"",toDate:"",visitTypeId:"",billNo:"",visitNo:"",billStatusId:"",patientId:"",orgId:$scope.organizationId,unitId:$scope.unitId,isOtcReg:false};
			$scope.billSearch = {fromDate:new Date(),toDate:new Date(),visitTypeId:"",billNo:"",visitNo:"",billStatusId:"",patientId:"",orgId:$scope.organizationId,unitId:$scope.unitId,isOtcReg:false,cancelledBillShow:"N"};
			$scope.dateOptionsTo.minDate = $scope.billSearch.fromDate;
		}
		
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
		
		$scope.transferToActiveDeposit = "N";
		
		$scope.setFirstTabLevel= function(newTab)
		{
			$scope.firstTabLevel = newTab;
		};
		$scope.isSetFirstTabLevel = function(tabNum)
		{
			return $scope.firstTabLevel === tabNum;
		};
		$scope.loadMoreData = function()
        {
            $scope.offset+=200;
            $scope.loadMoreFlag=true;
            $rootScope.searchPatient($scope.patientSearchObj);
        }
        $scope.offset=0;
        $scope.loadMoreFlag=false;
        $scope.noOfRecordsPerPage1=200;
        $scope.patientList = [];
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
			if($scope.loadMoreFlag==true)
            {
            	$scope.patientSearchObj.offset=$scope.offset;
            $scope.loadMoreFlag=false;
            }
        else
            {
        	$scope.patientSearchObj.offset=0;
            $scope.loadMoreFlag=false;
            $scope.patientList = [];
            }
            $scope.patientSearchObj.noOfRecordsPerPage=$scope.noOfRecordsPerPage1;
			var patSearchObj = angular.copy($scope.patientSearchObj);
			patSearchObj.patientType = 1;
			if($scope.patientSearchObj.dob != "" && typeof($scope.patientSearchObj.dob) != "undefined"){
				var dt = new Date($scope.patientSearchObj.dob);
				patSearchObj.birthDate = dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + dt.getFullYear();
			} 
			BillingGenericService.serviceAction(METHOD_POST,URI, patSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				$scope.searchPatientList = [];
				if (response.data.status == "success")
				angular.forEach(response.data.listObject,function(value,index)
						{
					$scope.patientList.push(value);
						})
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
		
		$scope.getPatientDetailsById = function(patientId){
			$rootScope.startLoader();
			$scope.patientDetailsList = [];
			$scope.patientDetails = {}
			var URI = BASE_URL + ROOT_URL + "/api/global/patient/billing/"+patientId;
			BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					$scope.billSearch = {fromDate:new Date(),toDate:new Date(),visitTypeId:"",billNo:"",visitNo:"",billStatusId:"",patientId:"",orgId:$scope.organizationId,unitId:$scope.unitId,cancelledBillShow:"N"};
					$scope.patientDetailsList = response.data.listObject;
					$scope.patientDetails = response.data.listObject[0];
					$scope.patientDetails.patientName = $scope.patientDetails.firstName + " " + $scope.patientDetails.lastName;
					$scope.patientDetails.genderCode = $scope.patientDetails.genderName.charAt(0).toUpperCase();
					$scope.patientDetails.age = $scope.patientDetails.ageString
					$scope.patientDetails.mobileNo = $scope.patientDetails.mobileNumber;
					$scope.countryCallingCode =$scope.patientDetails.countryCallingCode;

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
		$scope.billDetails = function(bill){
			
			
			if(bill.intermediateSelfDue!=null &&  bill.intermediateSelfDue>0){
				
				$scope.billDisabled=true;
			
				growl.warning("Self content of Intermediate bill(s) of this admission is not yet settled ..!!",{
					title : WARNING_MSG
				});
				
				
			}
			
			$scope.billDetailsPatientInfo = {};
			$scope.billDetailsPatientInfo.patientName = bill.patientName;
			$scope.billDetailsPatientInfo.uhidNumber=bill.uhidNumber;
			$scope.billDetailsPatientInfo.birthDate=bill.birthDate;
			$scope.billDetailsPatientInfo.ageString=bill.ageString;
			$scope.billDetailsPatientInfo.imagePath=bill.imagePath;
			$scope.billDetailsPatientInfo.genderId=bill.genderId;
			
			
			$scope.setFirstTabLevel(1);
			$scope.excessAmount = parseFloat(0).toFixed(2);	
			$scope.transferToActiveDeposit = 'N'
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
			$scope.getLoyaltyCardList($scope.selectedPatientId);
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
					var totalPackageConsumptionAmt = parseFloat(0);
					
					
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
						// totalPackageConsumptionAmt = totalPackageConsumptionAmt + parseFloat(value.packageConsumptionAmt);
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
					var URI = BASE_URL + ROOT_URL + "/api/billing/unit/deposit/patient/"+$scope.organizationId+"/"+$scope.unitId+"/"+bill.patientId+"/"+bill.visitId+"/"+bill.visitTypeId+"/0/false";
					BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success")
						{
							
							$scope.consumedAmt=0;
							$scope.availableDeposits = response.data.listObject;
							var activeDeposit = parseFloat(0);
							var activeDepositPatient = parseFloat(0);
							var billAmt = parseFloat($scope.payeeDueAmtStatic);
							var i = 0
							angular.forEach($scope.availableDeposits,function(value,key)
							{
								$scope.availableDeposits[i].oldConsumedDeposit = value.consumedDeposit;
								$scope.availableDeposits[i].oldAvailableDeposit =  value.availableDeposit;
							/*	if(value.depositTypeId != '1')
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
		
		//Start For Claim
		$scope.billDetailsForClaim = function(bill){
					

			
			
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
					//$scope.paymentReceiptsDetailsList=response.data.object.object;
					var totalAmtQuaBase = parseFloat(0);
					var totalAmtConcession = parseFloat(0);
					var totalAmtDiscount = parseFloat(0);
					var totalAmtGst = parseFloat(0);
					var totalNetPayableAmt = parseFloat(0);
					var totalSelfPayableAmtClaim = parseFloat(0);
					var totalCreditPayableAmtClaim = parseFloat(0);
					angular.forEach($scope.billServicesListClaim,function(value,key){
						
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
				}
			});
					
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
									pharamcy:false
							}
							
							var saveOrderDetailsObj = [];
							angular.forEach($scope.billServicesListClaim, function (value, key) {
								var saveArr = {};
								saveArr.netAmount = value.netAmt;
								saveArr.specialityName = value.specialityName;
								saveArr.specialityId = value.specialityId;
								saveArr.serviceName = value.serviceStandardName;
								saveArr.orderQty = value.quantity;
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
				}
			else
				{
				$scope.dueAmount = parseFloat(0.00).toFixed(2);
				$scope.paymentDetails.cashAmount = parseFloat($scope.dueAmount).toFixed(2);
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
			$scope.dateOptionsTo.maxDate = moment(new Date($scope.billSearch.fromDate)).add(180, 'days').toDate();
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
		/***********/
		// NEW FOR VIVEK
		$scope.settleBill = function(){
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
					outstanding : $scope.oldDueAmount
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
			$scope.billDisabled = false;
			$scope.transferToActiveDeposit = "N";
			this.transferToActiveDeposit = "N";
			$scope.calculateTotalDueRefAmount();
			$scope.listPaymentDetails = [];
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

		
		/*Vivek start*/
		
		$scope.redirectToNewOPDBilling = function(bill,type){
			
			var redirectObj = angular.copy(bill);
			redirectObj.billEditType = type;
			redirectObj.roundOffAmt = $scope.discountRoundOff;
			if(redirectObj.visitTypeId == 1){
				redirectObj.encounterId = redirectObj.visitId;
				
				$state.go("newOPDBilling",{
					newOpdBillObject :redirectObj
					
				});
			}else{
				
			/*	if(bill.isDischarge == 'N' && bill.dischargeStatusId != null && bill.dischargeStatusId == 6 ){
					growl.warning("Patient is not discharged ..!!",{
						title : WARNING_MSG
					});
					return false;
				}*/
				redirectObj.admissionId = redirectObj.visitId
				redirectObj.genderCode = redirectObj.genderName;
				$state.go("IPDCharges",{
					patientDetailsObj :redirectObj
				});
			}
			
			 
		}
		
		$scope.settlementClick=function()
		{
			if($scope.payeeId > 0)
				{
				     $scope.setFirstTabLevel(1);
					 $scope.transferToActiveDeposit = 'N'
					 $scope.excessAmount = parseFloat(0).toFixed(2);
					 $scope.send=false;
					 $scope.resend=false;
					 $scope.validatTextFieldFlag = true;
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
		$scope.printBill = function(bill,IsSummary,billReopened)
		{
			
			$("#bill").show();
			$scope.billingMasterId=bill.billingMasterId;
			$scope.patientId=bill.patientId;
			$scope.visitTypeId=bill.visitTypeId;
			$scope.IsSummary=IsSummary;
			$scope.billReopened = billReopened;
			
		}
		$scope.printBillNew=function(){
			//alert(bill.billingMasterId);
			
			if($scope.billReopened == null || $scope.billReopened == 'N'){
				
				var URIVALI = BASE_URL + ROOT_URL + "/api/billing/printLog/1/"+$scope.userId+"/"+$scope.billingMasterId;
				BillingGenericService.serviceAction(METHOD_GET,URIVALI, {},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
				{
					if (response.data.status == "success")
					{

						if($scope.visitTypeId==1){
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
					                },{
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
						                 "reportDesignFilePath": "Ipdbill_Cancelled.rpt",
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
							                }
							                ,{
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
				
			}else{
				
				if($scope.visitTypeId==1){
					var data =  {
			                 "appName": "HIS",
			                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
			                 "reportDesignFilePath": "opdbillreceipt_Cancelled.rpt",
			                 "reportFormat": "PDF",
			                 "exportFileName": "Ipdbill_Cancelled",
			                 "reportParams": [
			                   {
			                  "paramName": "@billid",
			                  "paramType": "Integer",
			                  "paramValue": $scope.billingMasterId
			                },{
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
				                 "reportDesignFilePath": "Ipdbill_Cancelled.rpt",
				                 "reportFormat": "PDF",
				                 "exportFileName": "Ipdbill_Cancelled",
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
					                }
					                ,{
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
		
		
		$scope.getCancelDetails = function(billDetails)
		{
			$scope.reasonDesc="";
			$scope.remark="";
			
			$scope.reasonDesc=billDetails.reasonDesc;
			$scope.remark=billDetails.remark;
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
			
			
		$scope.addPaymentDetails = function(){
			$scope.isBillList = 0;
			var exAmount = '';
			
			
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
						$("#confirmSave").modal("show");
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
			$("#confirmSave").modal("hide");
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
		
		$scope.rePrintOPDBillReceipt=function(index){
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
				
				growl.warning("Amount should be equel or less than due amount...!!",{
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
						phoneNumber:parseInt($scope.countryCallingCode+""+mobileNumber),
						partyA:mobileNumber,
						amount:validateMpesaAmount
				};
			
				if((mobileNumber != undefined && mobileNumber != null && mobileNumber != "") ||
						($scope.countryCallingCode != undefined && $scope.countryCallingCode != null && $scope.countryCallingCode != "")){
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
		
		var data ={
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
			
		});
		
	}
	
	
		
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
		
		$scope.gotoMedicationDetails = function(bill){ 

			$timeout(function () { 
					$state.go('medicinesAddSub', 
							{  
								obj : bill 
							});
			}, 2000); 
		}        
		
		
	}catch(e){
		console.log(e.message);
	}
}]);