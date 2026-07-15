angular.module("myApp").controller("creditNoteController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','reportService','GenericService',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,growl,reportService,GenericService){
	try{
	
	$rootScope.loginpage = true;
	
	
	$scope.dateOptions = {
			formatYear : 'yyyy',
			showWeeks : false
		};
	
	 $scope.dateOptionsSearch = {
				formatYear : 'yyyy',
				showWeeks : false,
				maxDate : new Date()
			};
	$scope.openDatePicker = function() {
		$scope.datepicker.opened = true;
	};

	$scope.datepicker = {
		opened : false
	};
	
	$scope.openDatePickerPatiSearch = function() {
		$scope.datepicker1.opened = true;
	};

	$scope.datepicker1 = {
			opened : false
		};
	
	
	$scope.searchBillDetails="";
	$scope.searchOrderDetails="";
	$scope.openFromDate = function() {
		$scope.fromDatePicker.opened = true;
	};

	$scope.fromDatePicker = {
		opened : false
	};
	
	$scope.openToDate = function() {
		$scope.toDatePicker.opened = true;
	};

	$scope.toDatePicker = {
			opened : false
		};
	
	
	var cookieObject = $cookies.getObject('cookieObject');
	if (cookieObject == undefined) {
		$state.go('login');
		return;
	}
	var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
    if (cookieObjectAppConfig == undefined) {
    	$state.go('applicationConfiguration');
        return;
    } else {
        console.log('cookieObjectAppConfig', cookieObjectAppConfig);

    }
    $scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
    $scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
    $scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
    $scope.selfPayeeIdCookies = cookieObjectAppConfig.selfPayeeId;
	$scope.pharmacyServiceId = cookieObjectAppConfig.pharmacyServiceId;
	$scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
	$scope.surchargeServiceId = cookieObjectAppConfig.surchargeServiceId;
	$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
	
	$scope.unitId = cookieObject.unitId;
	$scope.organizationId = cookieObject.organizationId;
	$scope.userId = cookieObject.userId;
	
	$scope.discountRadio = 1;
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
	/**================================COUNTER CODE END======================================*/
	
	
	        $scope.creditNoteApproval = false;
	        $scope.creditNoteType = null;
	        $scope.discountType = null;
	        $scope.fromDiscountAmount = null;
	        $scope.toDiscountAmount = null;
			$scope.getCreditNoteRightByUser = function()
			{
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL+"/billing/creditNote/rights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/user/"+$scope.userId;
				console.log('URI',URI);
			    return BillingGenericService.serviceAction(METHOD_GET, URI,"",
			        NOTIFICATION_MSG_STATUS_FALSE)
			      .then(function(response) {
			         if (response.data.status == "success")
			          {
			        	 var obj  = response.data.object;
			        	 if(obj!=null)
				    	  {
				    	     $scope.creditNoteMstObj.isApproved='Y';
				    		 $scope.creditNoteMstObj.isCreditNoteApproval=true;
				    		 $scope.creditNoteApproval = true;
				    		 $scope.fromCreditNoteAmount = obj.fromCreditNoteAmount;
				    		 $scope.toCreditNoteAmount =  obj.toCreditNoteAmount;
				    		 $scope.creditNoteType = obj.creditNoteType;
				    		 $scope.discountType = obj.discountType;
				 	         $scope.fromDiscountAmount = obj.fromDiscountAmount;
				 	         $scope.toDiscountAmount = obj.toDiscountAmount;
				    	  }
			          }
			         else if(response.data.status == "error")
			        	 {
			        	   $scope.creditNoteApproval = false;
			        	 }
			        });
			}
			$scope.getListOfUser = function()
				{			
			          var URI = BASE_URL + ROOT_URL	
								      + "/billing/users/creditNote/rights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/amt/"+$scope.totalCrAmountR;
								       BillingGenericService.serviceAction(METHOD_GET, URI,"",
								        NOTIFICATION_MSG_STATUS_FALSE)
								      .then(function(response)
								        {
								    	  		if (response.data.status == "success")
								                 {
								        	       $scope.userList = response.data.listObject;
								        		 }
								        	 else{
								        		 $scope.userList =null;
								        	    }
				                       });		
				}
	   
			$scope.getCreditNoteRightByUser();
			
			
	var getAmount = function(num)
	{
		return isNaN(Number(num))?0:(Number(parseFloat(num).toFixed(2)));
		//return isNaN(Number(num))?0:(Math.floor(num*100)/100);
	}
	
	$scope.getDiscountPerFromDiscount = function(billObj)
	{
		if(getAmount(billObj.previousDiscount)>0)
			{
			   return getAmount((billObj.previousDiscount*100)/billObj.grossAmount);
			}
		else{
			return 0;
		}
	}
	
	var commonObj = {
			unitId : $scope.unitId,
			organizationId : $scope.organizationId
		};
	
	$scope.againstServiceOrDeposit = 1;
	
	
	var URI = BASE_URL + ROOT_URL
	+ GETACTIVEREASONLIST;

	BillingGenericService
	.serviceAction(METHOD_POST, URI,
			commonObj,
			NOTIFICATION_MSG_STATUS_FALSE)
	.then(
			function(response) {
				console.log(response);
				// $rootScope.startLoader();
				if (response.data.status == "success")
					$scope.reasonList = response.data.listObject;
			});
	
	/**Bill Wise==================================================*/
	$scope.initCreditNoteWiseDiscount = function()
	{
		$scope.creditNoteWiseDiscout = {
				"discountGivenBy":"",
				"discountCategoryId" : "",
				"discountAmountCreditNoteWise" : 0.00,
				"discountPercentageCreditNoteWise" : 0.00
		}		
	}
	$scope.initCreditNoteWiseDiscount();
	
	$scope.initCreditNotMastObj = function(){

		$scope.totalAmountSummary = 0;
		$scope.totalConcessionSummary = 0;
		$scope.totalPreviousDiscount = 0;
		$scope.netPayableSummary = 0;
		$scope.totalDiscountSummary = 0;
		$scope.totalGstTaxSummary = 0;
		$scope.totaCrAmount= 0;
		$scope.totaCrAmount = 0;
		$scope.totalRefundAmount= 0;
		$scope.totalBalanceAmount= 0;
		$scope.finalTotalCrAmount= 0.00;
		$scope.initCreditNoteWiseDiscount();
		$scope.signRoundOff = 0;
		
	    $scope.billWiseDiscountObj = {
	            "discountGivenBy": "",
	            "discountCategoryId": "",
	            "discountAmountBillWise": "",
	            "discountPercentageBillWise": ""
	        }
	    
		$scope.creditNoteMstObj = angular.copy(
				{
					  creditNoteId : "",
					  creditNoteDate : "",
					  billId : "",
					  billPayeeId : "",
					  crNoteAgainst : "",
					  creditNoteNo : "",
					  crTotalAmount : 0.00,
					  //crAmount : 0.00,
					  crTaxAmount : 0.00,
					  crNetAmount : 0.00,
					  crOutAdjustmentAmount : 0.00,
					  crDeuAmount : 0.00,
					  crReasonId : "",
					  ceRemark : "",
					  creditNoteRemark:"",
					  unitId : $scope.unitId,
					  orgnisationId : $scope.organizationId,
					  status : 'A',
					  createdDate : "",
					  createdBy : $scope.userId,
					  updatedDate : "",
					  updatedBy : $scope.userId,
					  cancelOrDiscount:'',
					  requestSentTo :'',
					  isCreditNoteApproval:false,
					  isApproved:'N',
					  patientId:'',
					  counterId :$rootScope.counterId,
					  discountRoundoff:0,
					  reqDiscId:'',
					  listCreditNoteDetails : []
					}
				
				 );
	    
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
						}
						
				});
	}

	
	$scope.initCreditNoteDetailsObj = function()
	{
		$scope.creditNoteDetailsObj = angular.copy(
				{
					  creditNoteDetailId : "",
					  creditNoteId : "",
					  crNoteDate : "",
					  orderDtlIdOld : "",
					  orderDetailId : "",
					  orderDetailIdNew :"",	
					  crDiscountAmount : 0.00,
					  crServiceRateAmount : 0.00,
					  crAdditionalDiscount : 0.00,
					  crTaxAmount : 0.00,
					  //crAmount :0.00,
					  crNetAmount : 0.00,
					  billingDetailsId : '',
					  organisationId : $scope.organizationId,
					  unitId : $scope.unitId,
					  createdDate : "",
					  createdBy : $scope.userId,
					  updatedBy : $scope.userId,
					  updatedDate : "",
					  orderId:"",
					  orderDetailsId:"",
					  listCreditNoteDiscountDetailsDto:[]	  
					}
				)
	}
	
	$scope.creditNoteDiscoutDetailsObj = 
	{
		  creditNoteDiscountDtlId : '',
		  creditNoteDetailId : '',
		  billingDetailsId:'',
		  discountCategoryId : '',
		  discountCategory:'',
		  discountPercentage : 0.0,
		  discountAmount : 0.0,
		  discountGivenBy : '',
		  unitId : $scope.unitId,
		  orgnisationId : $scope.organizationId,
		  createdDate : '',
		  createdBy : $scope.userId,
		  updatedDate : '',
		  updatedBy : $scope.userId,
		}
	


	$scope.creditNoteDiscountDetails=
		{
			 "creditNoteDiscountDtlId" : '',
			  "creditNoteDetailId" : '',
			  "billingDetailsId":"",
			  "discountCategoryId" : '',
			  "discountCategory":'',
			  "discountPercentage" : '',
			  "discountAmount" : '',
			  "discountGivenBy" : $scope.userId,
			  "unitId" : $scope.unitId,
			  "orgnisationId" : $scope.organizationId,
			  "createdDate" : '',
			  "createdBy" : $scope.userId,
			  "updatedDate" : '',
			  "updatedBy" : $scope.userId
		}
	
	

	
	$scope.patientSearchObj = {
		      "uhIdNumber"      : "",
		      "patientName"     : "",
		      "genderId"        : "",
		      "mobileNo"        : "",
		      "birthDate"       : "",
		      "identificationTypeId":"",
		      "identificationNo":"",
		      "patientType"   :"1",
		      "unitId"          :$scope.unitId,
		      "organizationId"  :$scope.organizationId
		    }
	
	
	//For Get Active Gender List
	$scope.getSexMasterList = function() {
		var URI = BASE_URL + ROOT_URL + GETACTIVEGENDERLIST;
		BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			$scope.sexMasterList = response.data.listObject;
		});
	}
	
	$scope.getIdentificationTypeList = function(){
		var URI = BASE_URL + ROOT_URL + GETACTIVEIDENTIFICATION;
		BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			if (response.data.status == "success")
				$scope.identificationTypeList = response.data.listObject;
		});
	}
	
	$scope.getBankList = function(){
		var URI = BASE_URL + ROOT_URL + "/api/billing/bankMaster";
		var param = {orgnisationId:$scope.organizationId,unitId:$scope.unitId};
		BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			if (response.data.status == "success"){
				$scope.bankList = response.data.listObject;
			}
		});
	}
	
	$scope.getCardTypesList = function(){
		var URI = BASE_URL + ROOT_URL + "/api/billing/cardTypeMaster";
		var param = {orgnisationId:$scope.organizationId,unitId:$scope.unitId};
		BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			if (response.data.status == "success"){
				$scope.cardTypeList = response.data.listObject;
			}
		});
	}
	
	$scope.getDiscountCategoryList = function(){
		var URI = BASE_URL + ROOT_URL + "/api/billing/discountCategories/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId;
		BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			$scope.discountCategoryList = response.data.listObject;
		});
	}

	
	$scope.getSpecialityList =function(){
	var URI = BASE_URL + ROOT_URL+ GETACTIVESPECIALITY;
    BillingGenericService.serviceAction(METHOD_POST, URI,commonObj,	NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success")
					   $scope.specialityMasterList = response.data.listObject;
			});
    
 
    
	}
	
	   $scope.groupWiseServicesSelectedList = {
				services: []
		}
	$scope.groupWiseCheckAll= function(chkFlg) {
		$scope.disPerPopupForGroup = '';
		$scope.disAmtPopupForGroup = '';
		$scope.disCategoryIdForGroup = '';
		if(chkFlg == 'Y')
		$scope.groupWiseServicesSelectedList.services = $scope.groupWiseDiscountServicesList.map(function(item) { return item.serviceMasterId; });
 };

	
	$scope.serachGroupWiseServicesClear = function()
    {
    	$scope.serviceSpecialityIdForGroup = '';
    	$scope.serviceSubSpecialityIdForGroup = '';
    	$scope.chkFlgGroup = 'N';
    	$scope.groupWiseServicesSelectedList.services = [];
    	$scope.groupWiseDiscountServicesList  = [];
    	
    	$scope.disCategoryIdForGroup = '';
    	$scope.disPerPopupForGroup= 0;
    	$scope.disAmtPopupForGroup= 0;
    	//$scope.groupWiseDiscountServicesList  = $scope.autoRenderedServicesFinalList;
    	//$scope.groupWiseDiscountServicesList = angular.copy($scope.autoRenderedServicesFinalList);
    }
	
	
	  $scope.getDisPerPopupForGroup = function(disPer)
	    {
	    	var len = $scope.groupWiseServicesSelectedList.services.length;
	    	if(len == 0)
			{
				growl.warning("Please select at least one service!",{
					title : WARNING_MSG
				});
				$scope.disPerPopupForGroup = '';
	    		return false;
			}
	    	else
	    		{
	    		  var totalAmtPopupAllServices = 0;
		    		angular.forEach($scope.groupWiseDiscountServicesList, function (value, key) 
		    			{
		    			if($scope.groupWiseServicesSelectedList.services.some(serviceMasterId=>serviceMasterId==value.serviceMasterId && value.isDiscountApplicable=='Y'))
		    				{
		    				
		    				   totalAmtPopupAllServices = Number(totalAmtPopupAllServices) + Number(value.grossAmount);
		    				}
						
			    	});
	    	}
	    		
	    	var calDicAmt = (totalAmtPopupAllServices*disPer/100).toFixed(2);	
			$scope.disAmtPopupForGroup = calDicAmt;
	    }
	    
	    $scope.getDisAmtPopupForGroup = function(disAmt)
	    {
	    	var len = $scope.groupWiseServicesSelectedList.services.length;
	    	if(len == 0)
			{
				growl.warning("Please select at least one service!",{
					title : WARNING_MSG
				});
				$scope.disAmtPopupForGroup  = '';
	    		return false;
			}
	    	else
	    		{
	    		
	    		var totalAmtPopupAllServices = 0;
		    		angular.forEach($scope.groupWiseDiscountServicesList, function (value, key) {
		    			if($scope.groupWiseServicesSelectedList.services.some(serviceMasterId=>serviceMasterId==value.serviceMasterId))
		    				{
		    					totalAmtPopupAllServices =Number(totalAmtPopupAllServices) + Number(value.grossAmount);
		    				}
						
			    	});
	    		
	    		var disPer = ((parseFloat(disAmt)/totalAmtPopupAllServices)*100).toFixed(2);
	    		$scope.disPerPopupForGroup = disPer;
	    		}
	    	
			
	    }
	
	$scope.serachGroupWiseServices = function()
    {
		
		$scope.dummyList = angular.copy($scope.billDetailsListClone);
    	if($scope.serviceSpecialityIdForGroup > 0 && $scope.serviceSubSpecialityIdForGroup > 0)
    		{
    			$scope.groupWiseDiscountServicesList = [];
    			//alert("Both Grater");
    			var i = 0;
    			angular.forEach($scope.dummyList, function (value, key) {
        			if(value.specialityId == $scope.serviceSpecialityIdForGroup && value.subSpecialityMasterId == $scope.serviceSubSpecialityIdForGroup)
        				{
        					$scope.groupWiseDiscountServicesList.push(value);
        				}
    				
    	    		i++;
    	    	});
    			
    			
    			
    		}
    	else if($scope.serviceSpecialityIdForGroup > 0)
    		{
	    		$scope.groupWiseDiscountServicesList = [];
				//alert("Both Grater");
				var i = 0;
				angular.forEach($scope.dummyList, function (value, key) {
	    			if(value.specialityId == $scope.serviceSpecialityIdForGroup)
	    				{
	    					$scope.groupWiseDiscountServicesList.push(value);
	    				}
					
		    		i++;
		    	});
    		}
    }
	
    $scope.getSubSpecialityListById = function(spId)
    {

    	 var specialityIDS = [];
    	 specialityIDS.push(spId);
			
         var data = {
       		  organizationId:$scope.organizationId,
       		  unitId:$scope.unitId,
       		  specialityIdList:specialityIDS
         }
         console.log(data);
         var URI = BASE_URL + ROOT_URL+ "/adt/subspeciality/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/specialityId/"+spId;
	BillingGenericService
			.serviceAction("GET", URI,
					"",
					NOTIFICATION_MSG_STATUS_FALSE)
			.then(
					function(response) {
						console.log(response);
						// $rootScope.startLoader();
						if (response.data.status == "success")
							{
							$scope.subSpecialityMaterList = response.data.listObject;
							}
							
					});
    }
	
    $scope.clearDiscount = function()
    {
    	$scope.disPerPopupForGroup = '';
		$scope.disAmtPopupForGroup = '';
		$scope.disCategoryIdForGroup = '';
    }
    
    $scope.addDiscountInDiscountListForGroup = function()
    {
    	if($scope.disPerPopupForGroup == undefined || $scope.disPerPopupForGroup == '' )
    		{
	    		growl.warning("All fields are required.",{
					title : WARNING_MSG
				});
	    		return false;
    		}
    	else if($scope.disAmtPopupForGroup == undefined || $scope.disAmtPopupForGroup == '')
    		{
	    		growl.warning("All fields are required.",{
					title : WARNING_MSG
				});
	    		return false;
    		}
    	else if($scope.disCategoryIdForGroup == undefined || $scope.disCategoryIdForGroup == '')
		{
    		growl.warning("All fields are required.",{
				title : WARNING_MSG
			});
    		return false;
		}
    	else
    		{
    		var len = $scope.groupWiseServicesSelectedList.services.length;
    			if(len == 0)
    				{
	    				growl.warning("Please select at least one service!",{
	    					title : WARNING_MSG
	    				});
	    	    		return false;
    				}
    			else
    				{
    				
    				var discountPer = $scope.disPerPopupForGroup;
    				var discount = $scope.disAmtPopupForGroup;
    				
    				
    				var devide = len==0?1:len;
    				angular.forEach($scope.groupWiseDiscountServicesList,function(value,key)
    						 {
    					
    					       if(value.isDiscountApplicable=='Y' && parseFloat(value.refundAmount)==0)
    					    	   {
    					    	     value.discountDetailsList = [];
    						         value.discountPercentage = (discountPercentageF==""||discountPercentageF==undefined)?0.00:discountPercentageF;
    						         value.discount = (discountF==""||discountF==undefined)?0.00:discountF;
    						         
    						    	 if(discountPer>0)
    			    		    	 {
    						    		 var discountF = getAmount((getAmount(value.grossAmount)*(discountPer/100)));
    						    		 var discountPercentageF = discountPer;
    						   
    			    		    		 value.discountPercentage = discountPercentageF;
    			    		     		 value.discount =  discountF;
    						    		 
    			    		    	 }
    								var creditNoteDiscountDetails = angular.copy($scope.creditNoteDiscountDetails);
    								creditNoteDiscountDetails.discountCategoryId = $scope.disCategoryIdForGroup;
    								creditNoteDiscountDetails.discountPercentage = value.discountPercentage;
    								creditNoteDiscountDetails.discountAmount = value.discount;
    								//creditNoteDiscountDetails.discountGivenBy = $scope.creditNoteWiseDiscout.discountGivenBy ;
    								value.discountDetailsList.push(creditNoteDiscountDetails);
    					    	   }
    						});
    				
	    				
    			}
    			
    		}
    	
    	
    }

    
	$scope.init = function()
	{
		$scope.billMstId =null;
		$scope.discountCategoryList = [];
		$scope.listPaymentDetails = [];
		$scope.getDiscountCategoryList();
		$scope.identificationTypeList = [];
		$scope.sexMasterList = [];
		$scope.getSexMasterList();
		$scope.getIdentificationTypeList();
		$scope.patientDetailsList = [];
		$scope.patientDetails = {};
		$scope.billing = true;
		$scope.getBankList();
		$scope.getCardTypesList();
		$scope.visitNoList = [];
		$scope.initCreditNotMastObj();
		$scope.initCreditNoteDetailsObj();
	}
	
	$scope.init();
	

	
	$scope.searchPatient = function(patientSearchObj)
	{
		$scope.patientSearchObj.unitId = cookieObject.unitId;
		$scope.patientSearchObj.organizationId = cookieObject.organizationId;
		if(patientSearchObj.patientName!="" && patientSearchObj.patientName.length < 3)
		{
			growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
			$scope.patientList = [];
			return false;
		}	
		var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
		var patSearchObj = angular.copy($scope.patientSearchObj);
		if($scope.patientSearchObj.birthDate != "" && typeof($scope.patientSearchObj.birthDate) != "undefined"){
			var dt = new Date($scope.patientSearchObj.birthDate);
//			patSearchObj.birthDate = dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + dt.getFullYear();
			patSearchObj.birthDate = dt.getFullYear() + "-" +(dt.getMonth() + 1)+ "-" +dt.getDate();
		}
		$rootScope.startLoader();
		BillingGenericService.serviceAction(METHOD_POST,URI, patSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			$scope.searchPatientList = [];
			console.log("response",response);
			if (response.data.status == "success"){
				$scope.patientList = response.data.listObject;
			     patSearchObj = {};
			}
			if(response.data.listObject==null){
				growl.warning(response.data.message,{
					title : ERROR_MSG
				});
				$scope.patientSearchObj.birthDate="";
				$scope.patientList=[];
				return false;
			}
			$scope.patientSearchObj.birthDate="";
			$rootScope.stopLoader();
		});
		$scope.billList = [];
		$scope.payeeList = [];
		$scope.billDetailsList =  [];
		$scope.billDetailsListClone =  [];
		$scope.patientDetails = {};
	    $scope.requestDiscountFlag = false;
		$scope.requestDiscountDiv = false;
	}
	
	$scope.getPatientDetailsById = function(patientId){
		
		$rootScope.startLoader();
		$scope.patientDetailsList = [];
		$scope.patientDetails = {};
		$rootScope.startLoader();
		
		$scope.billDetailsListClone = [];
		$scope.billList = [];
		$scope.initCreditNotMastObj();
		$scope.initCreditNoteDetailsObj();
		$scope.billMstId =null;
		
		$scope.requestStatusObject = null;
		$scope.requestApporivedDiscountDiv = false;
		$scope.discountDisabledFlagForRequest = false;
		$scope.requestAppUser = 0;
		$scope.approvedAmtApp = 0;
		$scope.requestAmtApp =0;
		$scope.statusApp = "";
		$scope.discountRadio = 2; 
		 $scope.requestStatusObject  = null;
		 $scope.creditNoteApproval = false;
		
		var URI = BASE_URL + ROOT_URL + "/api/global/patient/billing/"+patientId;
		BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			if (response.data.status == "success"){
				$scope.patientDetailsList = response.data.listObject;
				$scope.patientDetails = response.data.listObject[0];
				var index = $scope.patientList.findIndex(x => x.patientId ==patientId);
				$scope.patientDetails.age=($scope.patientList[index].age);
				$scope.patientDetails.patientName=($scope.patientList[index].patientName);
				$scope.patientDetails.genderCode=($scope.patientList[index].genderCode);
				$scope.patientDetails.uhIdNumber=($scope.patientList[index].uhIdNumber);
				$scope.patientDetails.patientId=($scope.patientList[index].patientId);
				$scope.patientDetails.birthDate=($scope.patientList[index].birthDate);
				$scope.creditNoteMstObj.patientId = patientId;
				if($scope.requestStatusObject==null){
					   $scope.requestDiscountFlag = false;
					   $scope.requestDiscountDiv = false;
					}
				$scope.getBillListByPatientId(patientId);
				$rootScope.stopLoader();
			}
			else if(response.data.status == "error")
				{
				  $rootScope.stopLoader();
				}
			
		});
	}
	
	
	
	$scope.getBillListByPatientId = function(patientId){
		$scope.billList = [];
		$rootScope.startLoader();
		var URI = BASE_URL + ROOT_URL + "/api/billing/billList/"+$scope.organizationId+"/"+$scope.unitId+ "/"+patientId+"/N";
		BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			if (response.data.status == "success")
			{
				$scope.billList = response.data.listObject;
				if($scope.billList!=null&&$scope.billList.length>0)
					$(".anchor-tag").parents().next(".old-encounter-details").toggle();
				$rootScope.stopLoader();
			}
			else if(response.data.status == "error")
				{
				   $rootScope.stopLoader();
				}
			
		});
	}
	
	
	
	
	
	$scope.getBillDetailsByBillId = function(billObj)
	{
		$scope.billMstId = billObj.billingMasterId;
		$scope.visitTypeId = billObj.visitTypeId;
		$scope.admissionId = (billObj.admissionId==null)?billObj.encounterId:billObj.admissionId;
		$scope.againstServiceOrDeposit = 1;
		$scope.billDetailsList = [];
		$scope.billDetailsListClone = [];
		$scope.payeeList = [];
		$rootScope.startLoader();
		$scope.cancelDiscount();
		var URI1 = BASE_URL + ROOT_URL + "/api/billing/cr/payee/bill/"+billObj.billingMasterId+"/"+billObj.visitTypeId+"/"+$scope.admissionId;
		BillingGenericService.serviceAction(METHOD_GET,URI1,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
		 {
			if (response.data.status == "success")
			{
				$scope.visitTypeId=billObj.visitTypeId;
				$scope.payeeList = response.data.listObject;
				if($scope.payeeList!=null && $scope.payeeList.length>0)
					{
					angular
					.forEach(
							$scope.payeeList,
							function(value, key) 
							{
								if($scope.selfPayeeId==value.payeeId)
									{
									   $scope.creditNoteMstObj.billPayeeId = $scope.selfPayeeId;
									}
							});
					}
				
				$scope.billDetailsList = angular.copy(response.data.object);
				$scope.billDetailsListClone = angular.copy(response.data.object);
				$scope.calculateTotalAmount($scope.billDetailsListClone);
				
				console.log($scope.billDetailsListClone);
				
				$scope.requestStatusObject = {};
				var data1 = {};
				if(billObj.visitTypeId==1)
					{
					  $scope.encounterId = billObj.encounterId;
					    data1 = {
					    		encounterId : $scope.encounterId,
					    		requestType : 6
						}
					}
				else if(billObj.visitTypeId==2||billObj.visitTypeId==3||billObj.visitTypeId==4)
					{
					    $scope.admissionId = billObj.admissionId;
					    data1 = {
					    		admissionId : $scope.admissionId,
					    		requestType : 6
						}
					}
				
				var URI1 = BASE_URL + ROOT_URL + "/api/billing/getDiscReqDtlsForOPBilling/";
				BillingGenericService.serviceAction(METHOD_POST,URI1, data1, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						if(response.data.listObject.length > 0){
						$scope.requestStatusObject = response.data.listObject[0];
						$scope.creditNoteMstObj.reqDiscId = $scope.requestStatusObject.reqDiscId;
						if($scope.requestStatusObject.reqStatus == 2)	
							{
							$scope.againstServiceOrDeposit=2;
							$scope.requestDiscountFlag = true;
							 $scope.creditNoteApproval = true;
							$scope.requestApporivedDiscountDiv = true;
							$scope.discountDisabledFlagForRequest = true;
							$scope.requestAppUser = $scope.requestStatusObject.requestSentToEmployee;
							$scope.approvedAmtApp = $scope.requestStatusObject.approvedDiscAmt;
							$scope.requestAmtApp =$scope.requestStatusObject.reqDisAmt;
							$scope.statusApp = "Approved.";
							$scope.discountRadio = 2;
							$scope.creditNoteWiseDiscout.discountCategoryId =  $scope.requestStatusObject.discType;
							
							var totalNetA = 0;
							angular.forEach($scope.billDetailsListClone,function(value,key)
		    						 {

		                                   totalNetA = totalNetA + value.remainingRefundAmt;
		    						 	
		    						 });
							var totalDisPercentage =((parseFloat($scope.requestStatusObject.approvedDiscAmt)/totalNetA)*100).toFixed(2);
							
				    		if(totalDisPercentage > 0){
				    		$scope.billWiseDiscountObj.discountPercentageBillWise = totalDisPercentage;
				    		$scope.billWiseDiscountObj.discountAmountBillWise = parseFloat($scope.requestStatusObject.approvedDiscAmt).toFixed(2);
				    		
				    		 $scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise= totalDisPercentage;
				    	     $scope.creditNoteWiseDiscout.discountAmountCreditNoteWise = parseFloat($scope.requestStatusObject.approvedDiscAmt).toFixed(2);
							
				    		}
				    		
				    		$scope.creditNoteWiseDiscountApply();
				    		}
						else if($scope.requestStatusObject.reqStatus == 1)
							{
							    $scope.againstServiceOrDeposit=2;
							    $scope.creditNoteApproval = false;
							    $scope.requestDiscountFlag = true;
							    $scope.discountDisabledFlagForRequest = true;
							    $scope.requestApporivedDiscountDiv = true;
							    $scope.requestAppUser = $scope.requestStatusObject.requestSentToEmployee;
								$scope.approvedAmtApp = $scope.requestStatusObject.approvedDiscAmt;
								$scope.requestAmtApp =$scope.requestStatusObject.reqDisAmt;
								$scope.statusApp = "Pending.";
								$scope.discountRadio = 2;
							}
						else
							{
								$scope.requestStatusObject = null;
								$scope.requestApporivedDiscountDiv = false;
								$scope.discountDisabledFlagForRequest = false;
								$scope.requestAppUser = 0;
								$scope.approvedAmtApp = 0;
								$scope.requestAmtApp =0;
								$scope.statusApp = "";
								$scope.discountRadio = 2; 
								 $scope.requestStatusObject  = null;
								 $scope.creditNoteApproval = false;
							}
						}
						
						else{
							$scope.requestStatusObject = null;
							$scope.requestApporivedDiscountDiv = false;
							$scope.discountDisabledFlagForRequest = false;
							$scope.requestAppUser = 0;
							$scope.approvedAmtApp = 0;
							$scope.requestAmtApp =0;
							$scope.statusApp = "";
							$scope.discountRadio = 2; 
							 $scope.requestStatusObject  = null;
							 $scope.creditNoteApproval = false;
							
							}
						}
				});
				
				$rootScope.stopLoader();
			}
			else if(response.data.status == "error")
				{
				 growl.warning(response.data.message,{
					title : WARNING_MSG
				 });
				  $rootScope.stopLoader();
				}
			
		});
		
		
/*		var URI = BASE_URL + ROOT_URL + "/api/billing/billDetails/"+$scope.organizationId+"/"+$scope.unitId+ "/"+billMasterId;
		console.log("URI",URI);
		BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
		{
			if (response.data.status == "success"){*/
				
		/*	}
			$rootScope.stopLoader();
		});*/
	}
	
	
	$scope.getDiscountListForInfo = function(billingDetailsObj){
		$scope.creditNoteDiscountDetailsList = [];
		$rootScope.startLoader();
		$scope.serciveNameView = billingDetailsObj.serviceStandardName;
		$scope.totalAmtView = billingDetailsObj.netAmt;
		$scope.totalDiscountAmtView = 0;
		
		var URI = BASE_URL + ROOT_URL + "/api/billing/creditnote/discount/details/"+billingDetailsObj.billingDetailsId;
		BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			if (response.data.status == "success"){
				$scope.creditNoteDiscountDetailsList = response.data.listObject;
				if($scope.creditNoteDiscountDetailsList!=null && $scope.creditNoteDiscountDetailsList.length>0)
	           		{
					angular.forEach(creditNoteDiscountDetailsList,function(value,key)
							 {
						       $scope.totalDiscountAmtView = $scope.totalDiscountAmtView+value.discountAmount;
							 });   
			
					}
				$rootScope.stopLoader();
			}
			else if(response.data.status == "error")
				{
				  $rootScope.stopLoader();
				}
		
		});
	}
	
	$scope.cancelDiscount = function()
	{
		$scope.creditNoteWiseDiscout.discountAmountCreditNoteWise =0;
		$scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise = 0;
		
		$scope.billDetailsListClone = angular.copy($scope.billDetailsList);
		
		if($scope.requestStatusObject==null){
		   $scope.requestDiscountFlag = false;
		   $scope.requestDiscountDiv = false;
		   angular.forEach($scope.billDetailsListClone,function(cloneObj,key)
					 {
          	            cloneObj.discountDisabledFlag ='N';
					 });
		}
		$scope.calculateTotalAmount($scope.billDetailsListClone);
	}
	
	
	$scope.calculateTotalAmount = function(inputList)
	{
		$scope.totalAmountSummary=0.00;
		$scope.totalAmountSummaryDis=0.00;
		$scope.totalConcessionSummary=0.00;
		$scope.netPayableSummary=0.00;
		$scope.totalDiscountSummary=0.00;
		$scope.totalGstTaxSummary=0.00;
		$scope.totaCrAmount=0.00;
		$scope.totaCrTaxAmount=0.00;
		$scope.totalRefundAmount=0.00;
		$scope.totalBalanceAmount=0.00;
		$scope.finalTotalCrAmount= 0.00;
		$scope.totalCrAmountR= 0.00;
		$scope.discountRoundOff= 0.00;
		$scope.totalPreviousDiscount=0.00;
		$scope.totalremainingRefundAmt=0.00;
		$scope.totaCrAmountGrid=0.00;
		
   	   angular.forEach(inputList,function(value,key)
		 {
   		   
   		   if(value.serviceMasterId != $scope.surchargeServiceId){
   		   if(parseFloat(value.creditPayable) > 0 || parseFloat(value.packageConsumptionAmt) > 0 && parseFloat(value.refundAmount)==0){
   			value.isDiscountApplicable='N';
   		   }
   		    value.discountDisabledFlag = value.discountDisabledFlag==null?'N':value.discountDisabledFlag;
   		    
   		    value.discountDetailsList= (value.discountDetailsList==null)?[]:value.discountDetailsList;
   		    value.discountPercentage = (value.discountPercentage==null||value.discountPercentage==""||isNaN(value.discountPercentage))?parseFloat(0).toFixed(2):Number(parseFloat(value.discountPercentage).toFixed(2));
   		    value.discount = (value.discount==null||value.discount==""||isNaN(value.discount))?parseFloat(0):getAmount(value.discount);
   		    
   		   if(value.discountPercentage==0.00 && value.discount>0.00)
		    	{
		    	 value.discountPercentage = (value.discount*100)/(value.remainingRefundAmt);
		    	}
		    else if(value.discount==0.00 && value.discountPercentage>0.00)
		    	{
		    	  value.discount = getAmount(value.remainingRefundAmt)*(value.discountPercentage/100);
		    	  
		    	}else if(value.discount==0.00 && value.discountPercentage==0)
		    	{
			    	  value.discount = getAmount(value.remainingRefundAmt)*(value.discountPercentage/100);
			    }
   		      //value.discountPercentage =  getAmount(value.discountPercentage);
   		      value.discount = getAmount(value.discount);
   		    
   		    
   		    if(value.discountPercentage>100)
   		    	{
   		    	growl.warning("Percentage Should be less than 100%.",{
					title : WARNING_MSG
				});
   		    	value.discountPercentage = 0.00;
   		    	value.discount = 0.00;
   		    	$scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise = 0;
   		    	$scope.creditNoteWiseDiscout.discountAmountCreditNoteWise=0;
   		    	}
   		  
   		    if($scope.againstServiceOrDeposit==1)
   		    	{
   		    	//value.remainingRefundAmt = getAmount(value.netAmt);
   		    	value.discountPercentage =  0;
     		    value.discount = 0;
   		    	$scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise=0;
   			    $scope.creditNoteWiseDiscout.discountAmountCreditNoteWise=0;
   			    value.discountCategoryId = '';
   		    	value.refundAmount = getAmount(value.refundAmount);
   		    	  if(value.isSelected == true)
   		    		  {
   		    		  
   		    		  var txtAmt = 0;
   		    		 if(parseFloat(value.taxPercentage)>0){
   		    			txtAmt = (parseFloat(value.remainingRefundAmt) * parseFloat(value.taxPercentage))/(100 + parseFloat(value.taxPercentage));
			    		 var netA = parseFloat(value.remainingRefundAmt) - txtAmt;
			    		 
			    		 value.crTaxAmount = getAmount(txtAmt);
			    		 value.crAmount =getAmount(netA);
			    		 value.crNetAmount = getAmount(netA);
			    		 value.crAmount = value.crNetAmount;
			    		 
   		    		 }else{
   		    			value.crAmount =getAmount(value.remainingRefundAmt);
        		           value.balanceAmount =0;
        		           value.crTaxAmount = (value.taxAmount);
     	   		          value.crNetAmount = getAmount(value.remainingRefundAmt);
   		    		 }
   		    	       
   		    		  }
   		    	  else{
   		    		     //value.balanceAmount=getAmount(value.netAmt-value.refundAmount);
   		    		     value.crAmount = 0;
   		    		     value.crNetAmount = 0;
   		    		     value.crTaxAmount = 0;
   		    	      }
   		    	  
   		    	}
   		    else if($scope.againstServiceOrDeposit==2 && value.isDiscountApplicable=='Y' && parseFloat(value.remainingRefundAmt) > 0 )
   		    	{
   		    	
   		     //crTotalAmount
   		    		
   		            /*value.crAmount =getAmount(value.discount);
   		          
   		          if(value.taxPercentage>0){
   		            value.crTaxAmount = getAmount((value.crAmount)*((value.taxPercentage/100)));
   		          }
   		          else{
   		        	 value.crTaxAmount =0;
   		          }
    		      value.crNetAmount = getAmount(getAmount(value.crAmount)+getAmount(value.crTaxAmount));
   		          value.balanceAmount = getAmount((value.netAmt-value.crAmount-value.crTaxAmount-getAmount(value.refundAmount)));*/
   		    	
   		    	 var txtAmt = 0;
   		    	 /*&& value.discount==0.00*/
   		    	 if(parseFloat(value.discountPercentage) > 0){
   		    		 
	   		    	 if(parseFloat(value.taxPercentage)>0){
	   		    		 
			    		txtAmt = (parseFloat(value.remainingRefundAmt) * parseFloat(value.taxPercentage))/(100 + parseFloat(value.taxPercentage));
			    		
			    		 var netA = parseFloat(value.remainingRefundAmt) - txtAmt;
			    		
			    		 value.discount = (getAmount(value.remainingRefundAmt))*(value.discountPercentage/100);
			    		 value.discountPercentage =  getAmount(value.discountPercentage);
			    		 value.crTaxAmount = (getAmount(txtAmt))*(value.discountPercentage/100);
			    		 
			    		 value.crNetAmount = getAmount(getAmount(value.discount)-getAmount(value.crTaxAmount));
			    		 
			    		 //value.remainingRefundAmt = parseFloat(value.remainingRefundAmt)-parseFloat(value.crNetAmount);
			    		 
			    		 value.crAmount = value.crNetAmount;
	   		    	 }else{
	   		    		value.crTaxAmount =0;
	   		    		value.taxPercentage = 0;
	   		    		txtAmt = 0;
	   		    		value.crTaxAmount = 0;
			    		
			    		 var netA = parseFloat(value.remainingRefundAmt) - txtAmt;
			    		
			    		 value.discount = (getAmount(value.remainingRefundAmt))*(value.discountPercentage/100);
			    		 value.discountPercentage =  getAmount(value.discountPercentage);
			    		 value.crNetAmount = getAmount(getAmount(value.discount)-getAmount(value.crTaxAmount));
			    		 
			    		 //value.remainingRefundAmt = parseFloat(value.remainingRefundAmt)-parseFloat(value.crNetAmount);
			    		 
			    		 value.crAmount = value.crNetAmount;
			    		 
	   		    	 }
	   		    	/*parseFloat(value.discountPercentage) == 0 &&*/
   		    	 }else if(parseFloat(value.discount) > 0){
   		    		 
   		    		 
   		    		if(parseFloat(value.taxPercentage)>0){
	   		    		 
			    		txtAmt = (parseFloat(value.remainingRefundAmt) * parseFloat(value.taxPercentage))/(100 + parseFloat(value.taxPercentage));
			    		
			    		 var netA = parseFloat(value.remainingRefundAmt) - txtAmt;
			    		 
			    		 value.discountPercentage = parseFloat((parseFloat(value.discount)/parseFloat(value.remainingRefundAmt)) * 100).toFixed(2);
			    		
			    		 value.crTaxAmount = (getAmount(txtAmt))*(parseFloat(value.discountPercentage)/100);
			    		 
			    		 value.crNetAmount = getAmount(parseFloat(value.discount)-parseFloat(value.crTaxAmount));
			    		 
			    		// value.remainingRefundAmt = parseFloat(value.remainingRefundAmt)-parseFloat(value.crNetAmount);
			    		 value.crAmount = value.crNetAmount;
	   		    	 }else{
	   		    		value.crTaxAmount =0;
	   		    		//value.crAmount =0;
	   		    		value.taxPercentage = 0;
	   		    		txtAmt = 0;
	   		    		value.crTaxAmount = 0;
			    		
			    		 var netA = parseFloat(value.remainingRefundAmt) - txtAmt;
			    		
			    		 value.discountPercentage = parseFloat((parseFloat(value.discount)/parseFloat(value.remainingRefundAmt)) * 100).toFixed(2);
			    		 
			    		 value.crNetAmount = getAmount(getAmount(value.discount)-getAmount(value.crTaxAmount));
			    		 
			    		// value.remainingRefundAmt = parseFloat(value.remainingRefundAmt)-parseFloat(value.crNetAmount);
			    		 value.crAmount = value.crNetAmount;
			    		 
	   		    	 }
   		    		
   		    	 }
		    		
   		          
   		   /*	if(getAmount(value.discountPercentage)+getAmount(value.previousDiscountPer)>100)
	    		{
	    	     growl.warning("Discount limit is Exceded.",{
		          title : WARNING_MSG
	            });
   		    	  value.crAmount=0;
   		    	  value.discount=0;
   		    	  value.discountPercentage=0;
   		    	  value.crTaxAmount=0;
   		    	  value.crNetAmount=0;
   		    	  value.refundAmount =getAmount(value.refundAmount);
   		    	  value.balanceAmount = (value.netAmt-value.crAmount-value.crTaxAmount-value.refundAmount);
   		    	  $scope.discountRoundOff = 0;
   		    	  $scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise = 0;
	        	  $scope.creditNoteWiseDiscout.discountAmountCreditNoteWise=0;
   		    	  
	    		}
   		          
   		          if(Math.sign(getAmount(value.balanceAmount))==-1)
   		        	  {
   		        	  
   		        	$scope.discountRoundOff = $scope.discountRoundOff +getAmount(value.balanceAmount);
			   		      
   		    	 }*/
   		    	}
   	   
   	   if(isNaN(getAmount(value.crAmount)))
   		   {
   		      value.crAmount = 0;
   		   }
   	   else{
   		   value.crAmount = value.crAmount;
   		      
   	       }
   	   if(isNaN(getAmount(value.crNetAmount)))
   		   {
   		    value.crNetAmount = 0;
   		   }
   	   else if(value.discount==0.00 && value.discountPercentage==0 && $scope.againstServiceOrDeposit==2) {
   		   	
   		   value.crNetAmount = 0;
   		   value.crTaxAmount = 0;
   		   value.crAmount =0;

  	   }else{
  		    value.crNetAmount = value.crNetAmount;
  	      }
  	   
   	   
   	   value.grossAmount = getAmount(value.grossAmount);
	   value.concession = getAmount(value.concession);
	   value.previousDiscount = getAmount(value.previousDiscount);
	   value.netAmt = getAmount(value.netAmt);
	   value.discount = getAmount(value.discount);
	   value.taxAmount = getAmount(value.taxAmount);
	   value.discountPercentage = getAmount(value.discountPercentage);
       value.crNetAmount = getAmount(value.crNetAmount);
       value.crAmount = getAmount(value.crAmount);
       value.balanceAmount  = getAmount(value.balanceAmount);
       value.crTaxAmount = getAmount(value.crTaxAmount);
       value.refundAmount = getAmount(value.refundAmount);
   	   
   	   $scope.totalAmountSummary = parseFloat($scope.totalAmountSummary) + getAmount(value.grossAmount);
   	   if(value.isDiscountApplicable=='Y' && parseFloat(value.remainingRefundAmt) >0 ){
       $scope.totalAmountSummaryDis= parseFloat($scope.totalAmountSummaryDis) +getAmount(value.remainingRefundAmt);
   	   }
  	   $scope.totalConcessionSummary = parseFloat($scope.totalConcessionSummary) + getAmount(value.concession);
  	   $scope.totalPreviousDiscount = parseFloat($scope.totalPreviousDiscount)+getAmount(value.previousDiscount);
  	   $scope.netPayableSummary = parseFloat($scope.netPayableSummary) + getAmount(value.netAmt);
  	   $scope.totalDiscountSummary = (parseFloat($scope.totalDiscountSummary) + getAmount(value.discount)).toFixed(2);
  	   $scope.totalGstTaxSummary = parseFloat($scope.totalGstTaxSummary) + getAmount(value.taxAmount);
   	   $scope.totaCrAmount= getAmount($scope.totaCrAmount) +getAmount(value.crAmount) +getAmount(value.crTaxAmount);
   	   $scope.totaCrAmountGrid = getAmount($scope.totaCrAmountGrid) +getAmount(value.crAmount);
   	   $scope.totaCrTaxAmount=getAmount($scope.totaCrTaxAmount) +getAmount(value.crTaxAmount);
   	   
   	   $scope.totalCrAmountR = getAmount($scope.totalCrAmountR) + getAmount(value.crNetAmount)+getAmount(value.crTaxAmount);
 	   $scope.totalRefundAmount= getAmount($scope.totalRefundAmount) + getAmount(value.refundAmount);
 	   $scope.totalBalanceAmount= getAmount($scope.totalBalanceAmount) + getAmount(value.balanceAmount);
 	   
 	  $scope.totalremainingRefundAmt=getAmount($scope.totalremainingRefundAmt) + getAmount(value.remainingRefundAmt);
   	   
 	  console.log($scope.totalCrAmountR,'',getAmount(value.crNetAmount));
       
      
   	  
		 }
	
		});
   	   
   	 $scope.totalCrAmountR = parseFloat($scope.totalCrAmountR).toFixed(2);
     $scope.totalRefundAmount = parseFloat($scope.totalRefundAmount).toFixed(2);  
     $scope.totalBalanceAmount= parseFloat($scope.totalBalanceAmount).toFixed(2);
     $scope.totaCrAmount = parseFloat($scope.totaCrAmount).toFixed(2);
     $scope.signRoundOff =""; 
     
     if($scope.againstServiceOrDeposit==2){
    	 $scope.isBillWiseDiscountApplied = true; 
     }
     
     if(getAmount($scope.creditNoteWiseDiscout.discountAmountCreditNoteWise)>0 && $scope.discountRoundOff==0 &&  $scope.isBillWiseDiscountApplied)
    	 {
    	  $scope.discountRoundOff = parseFloat(getAmount($scope.creditNoteWiseDiscout.discountAmountCreditNoteWise)-getAmount($scope.totalDiscountSummary)).toFixed(2);
    	 }
    
     if(Math.sign(getAmount($scope.discountRoundOff))==0||Math.sign(getAmount($scope.discountRoundOff))==1)
    	 {
    	   $scope.signRoundOff ="+("+$scope.discountRoundOff+")" ; 
    	 }
     else{
    	 var tempDisocuntRoundOff = getAmount($scope.discountRoundOff);
    	  $scope.signRoundOff ="-("+Math.abs(tempDisocuntRoundOff)+")" ; 
     }
     
   	   
       $scope.finalTotalCrAmount =   getAmount($scope.totalCrAmountR)+getAmount($scope.discountRoundOff);
   	   $scope.finalTotalCrAmount = parseFloat($scope.finalTotalCrAmount).toFixed(2);
   	   console.log("inputList",inputList);
	}
	
	
	$scope.calculateTotalAmountAgainstService = function()
	{
		$scope.calculateTotalAmount($scope.billDetailsListClone);
	};
	
	
	

	
	$scope.creditNoteWiseDiscountApply = function()
	{
		
		if($scope.billMstId==null||$scope.billMstId=='')
		{
		  growl.warning("Please select bill.",{
			title : WARNING_MSG
		  });
		  $scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise = 0;
    	  $scope.creditNoteWiseDiscout.discountAmountCreditNoteWise=0;
		  return false;
		}
		if($scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise == '' && $scope.isBillWiseDiscountApplied)
		{
    		growl.warning("Please apply discount first.",{
				title : WARNING_MSG
			});
    		return false;
			
		}
		if(parseFloat($scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise) >100)
		{
		  	growl.warning("Percentage Should be less than 100%.",{
				title : WARNING_MSG
			});
		    	$scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise = 0;
		    	$scope.creditNoteWiseDiscout.discountAmountCreditNoteWise=0;
			
		}
  
	else if($scope.creditNoteWiseDiscout.discountCategoryId == '')
		{
    		growl.warning("Discount Category is required.",{
				title : WARNING_MSG
			});
    		return false;
		}
	else{
		
		
			var discountApplicableCount = $scope.billDetailsListClone.filter(obj => obj.isDiscountApplicable=='Y');
			discountApplicableCount = discountApplicableCount==0?0:discountApplicableCount.length;
		    if(discountApplicableCount==0)
		    	{
		    	growl.warning("No service is applicable for discount...!!",{
					title : WARNING_MSG
				 });
		    	 return;
		    	}
		
		    var totalRemainingAmount = 0;
		    	angular.forEach($scope.billDetailsListClone,function(value,key)
					 {
				       if(value.isDiscountApplicable=='Y' && parseFloat(value.remainingRefundAmt) >0)
				    	   {
				    	    totalRemainingAmount = totalRemainingAmount + parseFloat(value.remainingRefundAmt);
				    	   }
					 })
			
					  var userRigth = false;
					 
					 if( $scope.creditNoteType !=null && $scope.creditNoteType == 1){
							
						 if(parseFloat($scope.toCreditNoteAmount) == 100){
							 
							 userRigth = true;
							 
						 }else{
							 var calculatePerAmount = (totalRemainingAmount / 100)*$scope.toCreditNoteAmount;
							 
							 if(parseFloat(calculatePerAmount) >= parseFloat($scope.creditNoteWiseDiscout.discountAmountCreditNoteWise)){
								 userRigth = true; 
							 }
						 }
						
					 }else if($scope.userDiscountObject != null && $scope.creditNoteType !=null && $scope.creditNoteType == 2 && parseFloat($scope.fromCreditNoteAmount) < parseFloat($scope.creditNoteWiseDiscout.discountAmountCreditNoteWise) && parseFloat($scope.toCreditNoteAmount) > parseFloat($scope.creditNoteWiseDiscout.discountAmountCreditNoteWise)){
							userRigth = true;
					}
		    
		if( $scope.requestStatusObject == null && userRigth == false )
    	  {
    	   $scope.billWiseDiscountObj.requestAmountBillWise = $scope.creditNoteWiseDiscout.discountAmountCreditNoteWise;
    		var URI = BASE_URL + ROOT_URL
    		+ "/billing/getUsersListHavingCreditRights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/amt/"+$scope.creditNoteWiseDiscout.discountAmountCreditNoteWise+"/per/"+$scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise;
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
                var discountPer = $scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise;
                var discount = $scope.creditNoteWiseDiscout.discountAmountCreditNoteWise;
				var result = $scope.billDetailsListClone.filter(obj => obj.isDiscountApplicable=='Y');
				
				var devide = result==0?1:result.length;
				if($scope.totalAmountSummaryDis>0 && discount>0)
				      {
				        var discountPercentageF = Number(parseFloat(((discount*100)/($scope.totalAmountSummaryDis))).toFixed(2));
				      }
				angular.forEach($scope.billDetailsListClone,function(value,key)
						 {
					       if(value.isDiscountApplicable=='Y' && parseFloat(value.remainingRefundAmt) >0)
					    	   {
							    value.discountCategoryId= $scope.creditNoteWiseDiscout.discountCategoryId;
					    	    value.discountDetailsList = [];
						        value.discountPercentage = (discountPercentageF==""||discountPercentageF==undefined)?0.00:discountPercentageF;
						        value.discount = 0;
							/*	var creditNoteDiscountDetails = angular.copy($scope.creditNoteDiscountDetails);
								creditNoteDiscountDetails.discountCategoryId = $scope.creditNoteWiseDiscout.discountCategoryId;
								creditNoteDiscountDetails.discountPercentage = value.discountPercentage;
								creditNoteDiscountDetails.discountAmount =  value.discount;
								creditNoteDiscountDetails.discountGivenBy = $scope.creditNoteWiseDiscout.discountGivenBy ;
								value.discountDetailsList.push(creditNoteDiscountDetails);*/
					    	   }
						});
		   $scope.calculateTotalAmount($scope.billDetailsListClone);
		   $scope.requestDiscountDiv = true;
		   $scope.isBillWiseDiscountApplied = true;
		   growl.warning("Please send discount request to user",{
				title : WARNING_MSG
			});
    		return false;
		}
		else{
			//for DD
			
    		$scope.billWiseDiscountObj.requestAmountBillWise = 0;
    		$scope.billWiseDiscountObj.requestUserId = "";
    		$scope.requestDiscountDiv = false;
    		$scope.requestDiscountFlag = false;
    		//For DD
    		var discountPer = $scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise;
    		var discount = $scope.creditNoteWiseDiscout.discountAmountCreditNoteWise;
    		
    		var result = $scope.billDetailsListClone.filter(obj => obj.isDiscountApplicable=='Y');
    		var devide = result==0?1:result.length;
    		if($scope.totalAmountSummaryDis>0 && discount>0)
       	      {
       	        var discountPercentageF = Number(parseFloat(((discount*100)/($scope.totalAmountSummaryDis))).toFixed(2));
       	      }
    		
    		if($scope.requestStatusObject!=null && $scope.requestStatusObject.requestDiscountDetailsDtosList!=null  && $scope.requestStatusObject.requestDiscountDetailsDtosList.length>0)
    			{
    			if($scope.requestStatusObject.reqStatus == 1){
    				
    				 growl.warning("Discount request is already pending, please cancel and apply fresh discount...!!",{
    						title : WARNING_MSG
    					});
    		    		return false;
    				
    			}else if($scope.requestStatusObject.reqStatus == 2){
    				
    				if(parseFloat($scope.creditNoteWiseDiscout.discountAmountCreditNoteWise) > parseFloat($scope.requestStatusObject.approvedDiscAmt))
    					{
    					 growl.warning("Approved amount is "+parseFloat($scope.requestStatusObject.approvedDiscAmt)+", if you want to apply more discount then cancel pending request and apply again...!! ",{
     						title : WARNING_MSG
     					});
     		    		return false;
    					
    					}else{
    			$scope.isBillWiseDiscountApplied = false;
    			angular.forEach($scope.billDetailsListClone,function(value,key)
       				 {
       			       if(value.isDiscountApplicable=='Y' && parseFloat(value.remainingRefundAmt) >0 )
       			    	   {
       			    	        value.discountDetailsList = [];
       			    	        var discountObj = $scope.requestStatusObject.requestDiscountDetailsDtosList.find(service=>(service.serviceId==value.serviceMasterId));
       			    	    	if(discountObj!=undefined)
       			    	    		{
       			    	    		    value.discountCategoryId= $scope.creditNoteWiseDiscout.discountCategoryId;
       			    	    		    value.discountPercentage = parseFloat((parseFloat(discountObj.approvedDiscAmt)/parseFloat(value.remainingRefundAmt)) * 100).toFixed(2);
	       			    	           // value.discountPercentage = discountPercentageF;
	            				        value.discount = 0;
	            						/*var creditNoteDiscountDetails = angular.copy($scope.creditNoteDiscountDetails);
	            						creditNoteDiscountDetails.discountCategoryId = $scope.creditNoteWiseDiscout.discountCategoryId;
	            						creditNoteDiscountDetails.discountPercentage = value.discountPercentage;
	            						creditNoteDiscountDetails.discountAmount =  value.discount;
	            						creditNoteDiscountDetails.discountGivenBy = $scope.creditNoteWiseDiscout.discountGivenBy ;
	            						value.discountDetailsList.push(creditNoteDiscountDetails);*/
       			    	    		}
       				  
       			    	   }
       				});
    			}
    			
    			
    			}
    			}
    		else
    		{
    			 $scope.isBillWiseDiscountApplied = true;
    			angular.forEach($scope.billDetailsListClone,function(value,key)
          				 {
          			       if(value.isDiscountApplicable=='Y' && parseFloat(value.remainingRefundAmt) >0)
          			    	   {
          			    	    value.discountCategoryId= $scope.creditNoteWiseDiscout.discountCategoryId;
          			    	    value.discountDetailsList = [];
          				        value.discountPercentage = (discountPercentageF==""||discountPercentageF==undefined)?0.00:discountPercentageF;
          				        value.discount = 0;
          						/*var creditNoteDiscountDetails = angular.copy($scope.creditNoteDiscountDetails);
          						creditNoteDiscountDetails.discountCategoryId = $scope.creditNoteWiseDiscout.discountCategoryId;
          						creditNoteDiscountDetails.discountPercentage = value.discountPercentage;
          						creditNoteDiscountDetails.discountAmount =  value.discount;
          						creditNoteDiscountDetails.discountGivenBy = $scope.creditNoteWiseDiscout.discountGivenBy ;
          						value.discountDetailsList.push(creditNoteDiscountDetails);*/
          			    	   }
          				});
    		}
    		$scope.calculateTotalAmount($scope.billDetailsListClone);
		}
	  }
		
	}
	
	
	$scope.saveDiscountRequest = function()
	{
		
		
		if($scope.billMstId==null||$scope.billMstId=='')
		{
		  growl.warning("Please select bill.",{
			title : WARNING_MSG
		  });
		  $scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise = 0;
    	  $scope.creditNoteWiseDiscout.discountAmountCreditNoteWise=0;
		  return false;
		}
		
		if($scope.billWiseDiscountObj.requestUserId==null||$scope.billWiseDiscountObj.requestUserId=='')
			{
			  growl.warning("Please select a user...!!",{
				title : WARNING_MSG
			  });
    		  return false;
			}
		var netAmt = $scope.billDetailsListClone.reduce(function(prev, cur) {
			  return prev + cur.netAmt;
			}, 0);
		
		var date = moment().format("DD-MM-YYYY HH:mm:ss");
		$rootScope.startLoader();
		var requestDistCountList = $scope.billDetailsListClone;
		var requestDiscountObject ={};
		var requestDiscountDetailsList =[];
		
		requestDiscountObject.organizationId = $scope.organizationId;
		requestDiscountObject.unitId = $scope.unitId;
		requestDiscountObject.visitTypeId = $scope.visitTypeId;
		requestDiscountObject.encounterId = $scope.encounterId;
		requestDiscountObject.admissionId = $scope.admissionId;
		requestDiscountObject.patientId = $scope.creditNoteMstObj.patientId;
		requestDiscountObject.netAmount = netAmt;
		requestDiscountObject.reqDisAmt = $scope.creditNoteWiseDiscout.discountAmountCreditNoteWise;
		requestDiscountObject.approvedDiscAmt = 0;
		requestDiscountObject.requestSentBy = $scope.userId;
		requestDiscountObject.requestSentTo = $scope.billWiseDiscountObj.requestUserId;
		requestDiscountObject.createdBy = $scope.userId;
		requestDiscountObject.updatedBy = $scope.userId;
		requestDiscountObject.reqStatus = "1";
		requestDiscountObject.discType = $scope.creditNoteWiseDiscout.discountCategoryId;
		requestDiscountObject.requestType = "6";
		//requestDiscountObject.billId =$scope.billMstId;
		requestDiscountObject.createdDate =date;
		requestDiscountObject.requestDate =date;
		
		var discountPer = $scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise;
		var discount = $scope.creditNoteWiseDiscout.discountAmountCreditNoteWise;
		
		var result = $scope.billDetailsListClone.filter(obj => obj.isDiscountApplicable=='Y');
		var devide = result==0?1:result.length;
		if($scope.totalAmountSummaryDis>0 && discount>0)
   	      {
   	        var discountPercentageF = Number(parseFloat(((discount*100)/($scope.totalAmountSummaryDis))).toFixed(2));
   	      }
		
		angular.forEach(requestDistCountList,function(value,key)
				 {
			       if(value.isDiscountApplicable=='Y' && value.discount>0)
			    	   {
			    	    value.discountDetailsList = [];
			    	    if($scope.isBillWiseDiscountApplied)
			    	    	{
			    	    	    value.discountPercentage = (discountPercentageF==""||discountPercentageF==undefined)?0.00:discountPercentageF;
						        value.discount = getAmount((Number(value.remainingRefundAmt) * Number(value.discountPercentage))/100);
			    	    	}
						var cNDetails = angular.copy($scope.creditNoteDiscountDetails);
						cNDetails.discountCategoryId = $scope.creditNoteWiseDiscout.discountCategoryId;
						cNDetails.discountPercentage = value.discountPercentage;
						cNDetails.discountAmount =  value.discount;
						cNDetails.discountGivenBy = $scope.creditNoteWiseDiscout.discountGivenBy;
						
						var detailsObj = {};
						detailsObj.organizationId= $scope.organizationId; 
						detailsObj.unitId= $scope.unitId; 
						detailsObj.orderId= value.orderId; 
						detailsObj.orderDetailsId= value.orderDetailsId; 
						detailsObj.serviceId= value.serviceMasterId; 
						detailsObj.netAmount= value.netAmt; 
						detailsObj.reqDisAmt= value.discount; 
						detailsObj.approvedDiscAmt= 0; 
						detailsObj.createdBy= $scope.userId; 
						detailsObj.createdDate = date;
						requestDiscountDetailsList.push(detailsObj);
			    	   }
				});
		
	  requestDiscountObject.requestDiscountDetailsDtosList = requestDiscountDetailsList;
	  
	  var URI = BASE_URL + ROOT_URL+ "/api/billing/requestDiscount";
      BillingGenericService.serviceAction(METHOD_POST, URI,requestDiscountObject,
				NOTIFICATION_MSG_STATUS_FALSE)
		.then(function(response) {
					console.log(response);
					if (response.data.status == "success")
						{
						   $rootScope.stopLoader();
						   growl.success(
									"Discount request sent sucessfully...!!",
									{
										title : 'Success!'
									});
						    $scope.requestDiscountFlag = true;
						    $scope.discountDisabledFlagForRequest  = true;
						    $scope.requestDiscountFlag = false;
						    $scope.requestDiscountDiv = false;
						    $scope.totalCrAmountR = 0;
						    $scope.finalTotalCrAmount = 0;
						    $scope.init();
						    $scope.billDetailsListClone = [];
							$scope.patientDetails = {};
							$scope.billDetailsList = [];
							$scope.billList = [];
							$scope.initCreditNotMastObj();
							$scope.initCreditNoteDetailsObj();
						}
					else if(response.data.status == "error")
						{
						  $rootScope.stopLoader();
						}
					
				});
	}
	

	
	/** Group Wise Discount start======================================*/
	
	$scope.specialityMasterListForGroup = [];
	    //Check Product Wise Category
		  var isGroupExists;
			function checkGroupExistsOrNot(arr, val) {
				return arr
						.some(function(arr) {
							return (val.specialityId === arr.specialityId);
						});
			}
			
	
	$scope.groupWiseDiscountPopup = function()
    {
		
		if($scope.againstServiceOrDeposit == 2)
			{
			$scope.groupWiseServicesSelectedList.services = [];
			$scope.serachGroupWiseServicesClear();
			$('#groupWiseDiscountModalCN').modal('show');
	    	$scope.totalGroupWiseAmount =0;
	    	$scope.totalGroupWiseDiscount =0;
		    $scope.chkFlgGroup = 'N';
		    
			console.log($scope.billDetailsListClone);
	    	angular.forEach($scope.billDetailsListClone, function (value, key) {
	    		
	    		var temp = {specialityId :value.specialityId,specialityName:value.specialityName};
	    		if($scope.specialityMasterListForGroup.length > 0)
	    			{
				    	isGroupExists=checkGroupExistsOrNot($scope.specialityMasterListForGroup,temp)
						if (!isGroupExists) {
							$scope.specialityMasterListForGroup.push(temp);
						}
	    	       }
	    		else
	    			{
	    			   $scope.specialityMasterListForGroup.push(temp);
	    			}
	    		
	    	});
			} 
		else{
			growl.warning("Please select against discount.",{
				title : WARNING_MSG
			});
		}
		
    }
	
	
	
	$scope.checkAllService = function() 
	{
		if ($scope.selectedAllService) 
		{
			$scope.selectedAllService = true;
		} else {
			$scope.selectedAllService = false;
		}
		angular
				.forEach(
						$scope.billDetailsListClone,
						function(billDetailsObj) {
							billDetailsObj.isSelected = $scope.selectedAll;
						});
	}
	
	$scope.checkAllServiceMain = function() 
	{
		if ($scope.selectedAllService) 
		{
			$scope.selectedAllService = true;
		} else {
			$scope.selectedAllService = false;
		}
		angular
				.forEach(
						$scope.billDetailsListClone,
						function(billDetailsObj) 
						{
							if(billDetailsObj.isPackageService != 'Y' && billDetailsObj.refundAmount==0 && billDetailsObj.serviceRendered==0)
								{
								  billDetailsObj.isSelected = $scope.selectedAllService;
								}
							
						});
		$scope.calculateTotalAmountAgainstService();
	}
	
	
	
	$scope.checkAll = function() 
	{
		if ($scope.selectedAll) 
		{
			$scope.selectedAll = true;
		} else {
			$scope.selectedAll = false;
		}
		angular
				.forEach(
						$scope.groupWiseDiscountServicesList,
						function(billDetailsObj) {
							billDetailsObj.isSelected = $scope.selectedAll;
						});
	}
	
	$scope.calculatGroupWisePer = function()
	{
		var discountPer = 0.00;
		var discount = $scope.serviceWiseDiscountObj.serviceDiscountAmount;
		$scope.serviceWiseDiscountObj.serviceDiscountPer=0.00;
		
		$scope.totalGroupWiseAmount =0;
    	$scope.totalGroupWiseDiscount =0;
		
		angular.forEach($scope.groupWiseDiscountServicesList,function(value,key)
				 {
			      $scope.totalGroupWiseAmount = getAmount($scope.totalGroupWiseAmount)+getAmount(value.balanceAmount);
				});
		if(discountPer==0.00 && discount>0.00)
    	{
		  discountPer = (discount*100)/($scope.totalGroupWiseAmount);
    	  $scope.serviceWiseDiscountObj.serviceDiscountPer =getAmount(discountPer);
    	}
		
		
	}
	
	$scope.calucateGroupWiseAmount = function()
	{
		var discountPer = $scope.serviceWiseDiscountObj.serviceDiscountPer;
		$scope.serviceWiseDiscountObj.serviceDiscountAmount=0.00;
		
		
		var discount =0.00;
		$scope.totalGroupWiseAmount =0;
    	$scope.totalGroupWiseDiscount =0;
		
		angular.forEach($scope.groupWiseDiscountServicesList,function(value,key)
				 {
			      $scope.totalGroupWiseAmount = getAmount($scope.totalGroupWiseAmount)+getAmount(value.balanceAmount);
				});
		if (discount==0.00 && discountPer>0.00)
    	{
		   discount = ($scope.totalGroupWiseAmount)*(discountPer/100);
		   $scope.serviceWiseDiscountObj.serviceDiscountAmount = getAmount(discount);
    	}
	}
	
	

	
	
	$scope.saveGroupDiscountToFinalList = function()
	{
		angular.forEach($scope.groupWiseDiscountServicesList,function(groupObj,key)
				 {
			     angular.forEach($scope.billDetailsListClone,function(cloneObj,key)
					 {
				       if(cloneObj.serviceMasterId==groupObj.serviceMasterId)
				    	   {
				    	    cloneObj.discountPercentage = groupObj.discountPercentage;
				    	    cloneObj.discount = getAmount(groupObj.discount);
				    	    cloneObj.discountDetailsList = groupObj.discountDetailsList;
				    	   }
					});
				});
		$scope.calculateTotalAmount($scope.billDetailsListClone);
	}
	
	/** Group Wise Discount end======================================*/
	

	/**bill Wise End ==================================================*/
	
	/**Service Wise==================================================*/
	
	$scope.initServiceDiscountObj = function()
	{
	 $scope.serviceWiseDiscountObj =
	 {
		"serviceDiscountPer":"",
		"serviceDiscountAmount":"",
		"serviceDiscoutCategoryId":"",
		"serviceDiscountGiveyBy":""
	 }
	}
	$scope.initServiceDiscountObj();
	
	
	
	
	$scope.serviceObj={};
	$scope.serviceViseDiscountPopup = function(index,serviceObj)
	{
		$scope.serviceObj=serviceObj;
		$scope.serviceIndex = index;
		$scope.discountDetailsList = [];	
		$scope.serciveNamePop = $scope.serviceObj.serviceStandardName;
		$scope.totalAmtPopup = $scope.serviceObj.grossAmount;
		//$scope.concession = $scope.serviceObj.concession==null?0.00:$scope.serviceObj.concession;
		if(serviceObj.discountDetailsList.length>0)
			{
			 $scope.discountDetailsList=serviceObj.discountDetailsList;
			 
			 $scope.totalDiscountAmtPopup = getAmount(serviceObj.discount);
			 $scope.totalDiscountPer = getAmount(serviceObj.discountPercentage);
			 //check
			 $scope.grossAmount = getAmount(serviceObj.grossAmount);
			}
		else
		{
			$scope.totalDiscountAmtPopup = parseFloat(0).toFixed(2);
			$scope.totalDiscountPer = parseFloat(0).toFixed(2);
			$scope.grossAmount = $scope.serviceObj.grossAmount;
		}
		
		
		
	}
	
	
	$scope.calculatServiceWisePer = function()
	{
		var discountPer = 0.00;
		var discount = $scope.serviceWiseDiscountObj.serviceDiscountAmount;
		$scope.serviceWiseDiscountObj.serviceDiscountPer=0.00;
		if(discountPer==0.00 && discount>0.00)
    	{
			/*discountPer = (($scope.netAmt-$scope.concession)*100)/(discount);*/
		 discountPer = (($scope.grossAmount)*100)/(discount);
		 $scope.serviceWiseDiscountObj.serviceDiscountPer = getAmount(discountPer);
    	}
	}
	
	$scope.calucateServiceWiseAmount = function()
	{
		if(parseFloat($scope.serviceWiseDiscountObj.serviceDiscountPer) > 100){
			
			growl.warning("Percentage cannot be more than 100%.",{
				title : WARNING_MSG
			});
			$scope.serviceWiseDiscountObj.serviceDiscountPer = 0;
			$scope.serviceWiseDiscountObj.serviceDiscountAmount = 0;
			return false;
		}else{
		var discountPer = $scope.serviceWiseDiscountObj.serviceDiscountPer;
		var discount =0.00;
		$scope.serviceWiseDiscountObj.serviceDiscountAmount=0.00;
		   if (discount==0.00 && discountPer>0.00)
	    	{
			   discount = ($scope.grossAmount)*(discountPer/100);
	    	 $scope.serviceWiseDiscountObj.serviceDiscountAmount =getAmount(discount);
	    	}
		}
	}
	
	
	$scope.addServiceViseDiscont = function()
	{
		var discountPer = $scope.serviceWiseDiscountObj.serviceDiscountPer;
		var discount = $scope.serviceWiseDiscountObj.serviceDiscountAmount;
		
		$scope.discountCategoryList.filter
		
			$scope.headerObj = $scope.discountCategoryList
					.filter(function(discoutObj) {
						if (discoutObj.id == $scope.serviceWiseDiscountObj.serviceDiscoutCategoryId)
						{
							return discoutObj;
						}
					});
		
		var creditNoteDiscountDetails = angular.copy($scope.creditNoteDiscountDetails);
		creditNoteDiscountDetails.discountCategoryId = $scope.serviceWiseDiscountObj.serviceDiscoutCategoryId;
		creditNoteDiscountDetails.discountPercentage = discountPer;
		creditNoteDiscountDetails.discountAmount = discount;
		creditNoteDiscountDetails.discountGivenBy = $scope.serviceWiseDiscountObj.serviceDiscountGiveyBy;
		creditNoteDiscountDetails.discountCategory = $scope.headerObj[0].type;
		$scope.discountDetailsList.push(creditNoteDiscountDetails);
		
		$scope.totalDiscountAmtPopup = getAmount($scope.totalDiscountAmtPopup)+getAmount(discount);
		$scope.totalDiscountPer = getAmount($scope.totalDiscountPer)+getAmount(discountPer);
		 
		$scope.billDetailsListClone[$scope.serviceIndex].discountDisabledFlag='Y';
		$scope.initServiceDiscountObj();
	}
	$scope.saveServiceWiseDiscount = function()
	{
		$scope.billDetailsListClone[$scope.serviceIndex].discountDetailsList=[];
		$scope.billDetailsListClone[$scope.serviceIndex].discountPercentage = 0.00;
		$scope.billDetailsListClone[$scope.serviceIndex].discount = 0.00;
		
		$scope.billDetailsListClone[$scope.serviceIndex].discountDetailsList = $scope.discountDetailsList;
		$scope.billDetailsListClone[$scope.serviceIndex].discountPercentage = getAmount($scope.totalDiscountPer);
		$scope.billDetailsListClone[$scope.serviceIndex].discount = getAmount($scope.totalDiscountAmtPopup);
		$scope.calculateTotalAmount($scope.billDetailsListClone);
	}
	$scope.getRemoveDiscount = function(Index)
	{
		$scope.discountDetailsList.splice(Index, 1);
		angular.forEach($scope.discountDetailsList,function(value,key)
				{
			        $scope.totalDiscountAmtPopup=0.00;
			        $scope.totalDiscountPer=0.00;
			        $scope.totalDiscountAmtPopup = getAmount($scope.totalDiscountAmtPopup)+getAmount(value.discountAmount);
					$scope.totalDiscountPer = getAmount($scope.totalDiscountPer)+getAmount(value.discountPercentage);
				});
		if($scope.discountDetailsList.length==0)
			{
			 $scope.billDetailsListClone[$scope.serviceIndex].discountDisabledFlag='N';
		    }
	
	}
	
	
	/**==================================================*/

	
		$scope.selectedIndex = 0;
	    $scope.getDiscountPerCal = function(itemObj,idx)
	    {
	    	$scope.selectedIndex = idx;
	      $scope.isBillWiseDiscountApplied=false;
	      $scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise=0;
	  	  $scope.creditNoteWiseDiscout.discountAmountCreditNoteWise=0;
	  	  $scope.creditNoteWiseDiscout.discountCategoryId == '';
	  	  if($scope.requestStatusObject==null){
			   $scope.requestDiscountFlag = false;
			   $scope.requestDiscountDiv = false;
			}else{
				 $scope.getCancelDiscountReqForDiscount();
			}
	      
	      
		  $scope.billDetailsListClone[idx].discountPercentage = 0.00;
			  
		  $scope.calculateTotalAmount($scope.billDetailsListClone);
	    }
	    
	    $scope.selectedIndex = 0;
	    $scope.getDiscountCal = function(itemObj,idx)
	    {
	    	$scope.selectedIndex = idx;
	    	 $scope.isBillWiseDiscountApplied=false;
	    	 $scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise=0;
		  	 $scope.creditNoteWiseDiscout.discountAmountCreditNoteWise=0;
		  	 $scope.creditNoteWiseDiscout.discountCategoryId == '';
			  if($scope.requestStatusObject==null){
				   $scope.requestDiscountFlag = false;
				   $scope.requestDiscountDiv = false;
				}else{
					 $scope.getCancelDiscountReqForDiscount();
				}
			  
	    	 $scope.billDetailsListClone[idx].discount = 0.00;
	    	// var discount = (getAmount(itemObj.grossAmount))*(itemObj.discountPercentage/100);
	    	// $scope.billDetailsListClone[idx].discount = getAmount(discount);
	    	 $scope.calculateTotalAmount($scope.billDetailsListClone);
	    }
	
	    
	    $scope.getCancelDiscountReqForDiscount = function()
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
						
							$scope.requestApporivedDiscountDiv = false;
							$scope.discountDisabledFlagForRequest = false;
							$scope.requestAppUser = 0;
							$scope.approvedAmtApp = 0;
							$scope.requestAmtApp =0;
							$scope.statusApp = "";
							$scope.discountRadio = 2; 
							 $scope.requestStatusObject  = null;
				    		
						}
					});
			
		}
	    
	$scope.getFormattedDate = function(date,isTime){
		var tempDate = new Date(date);
		var date = tempDate.getDate().toString().length == 1 ? "0"+tempDate.getDate().toString() : tempDate.getDate().toString();
		var month =  (tempDate.getMonth() + 1).toString().length == 1 ? "0"+(tempDate.getMonth() + 1).toString() : (tempDate.getMonth() + 1).toString();
		var year = tempDate.getFullYear();
		if(isTime)
			return date + "-" + month + "-" + year + " "+tempDate.getHours()+":"+tempDate.getMinutes()+":"+getSeconds();
		else
			return date + "-" + month + "-" + year + " 00:00:00";
	}
	
	 $scope.getCancelDiscountReq = function()
		{
			$rootScope.startLoaderPopup();
			var data = {
					reqDiscId :$scope.requestStatusObject.reqDiscId,
			}
			
			var URI = BASE_URL + ROOT_URL + "/api/billing/cancelRequest";
			
			BillingGenericService
			.serviceAction("POST", URI, data)
			.then(function(response) {
						$rootScope.stopLoaderPopup();
						if (response.data.status == "success") {
							
							growl.success("Cancelled !!!!",{
								title : 'success'
							});
							$scope.creditNoteWiseDiscout.discountAmountCreditNoteWise =0;
							$scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise = 0;
						    $scope.requestDiscountFlag = false;
						    $scope.requestDiscountDiv = false;
						    $scope.calculateTotalAmount($scope.billDetailsListClone);
							$scope.requestApporivedDiscountDiv = false;
							$scope.discountDisabledFlagForRequest = false;
							$scope.requestAppUser = 0;
							$scope.approvedAmtApp = 0;
							$scope.requestAmtApp =0;
							$scope.statusApp = "";
							$scope.discountRadio = 2; 
							$scope.requestStatusObject  = null;
							$scope.cancelDiscount();
				    		angular.element('#discountCancellationModalReq').modal('hide');
						}
					});
			
		}
	
	$scope.showCreditAmount= function()
	{
		
		 var totalRemainingAmount = $scope.billDetailsListClone[$scope.selectedIndex].remainingRefundAmt;
		 var DisPer = $scope.billDetailsListClone[$scope.selectedIndex].discountPercentage;
		 var userRigth = false;
		 var checkPer = false;
			angular.forEach($scope.billDetailsListClone,function(value, key) 
					{
						if(value.discountPercentage != null && value.discountPercentage != 0){
							 checkPer = true;
						}
					});
		 
		 if(checkPer == false && $scope.againstServiceOrDeposit==2)
		 {
			 growl.warning("Discount should be greater than zero..!!",{
					title : WARNING_MSG
				});
	    		return false;
		 }
		 if( $scope.creditNoteType !=null && $scope.creditNoteType == 1){
				
			 if(parseFloat($scope.toCreditNoteAmount) == 100){
				 
				 userRigth = true;
				 
			 }else{
				 var calculatePerAmount = (totalRemainingAmount / 100)*$scope.toCreditNoteAmount;
				 
				 if(parseFloat(calculatePerAmount) > parseFloat($scope.creditNoteWiseDiscout.totalDiscountSummary)){
					 userRigth = true; 
				 }
			 }
			
		}else if($scope.userDiscountObject != null &&  $scope.creditNoteType !=null && $scope.creditNoteType == 2 && parseFloat($scope.fromCreditNoteAmount) < parseFloat($scope.totalDiscountSummary) && parseFloat($scope.toCreditNoteAmount) > parseFloat($scope.totalDiscountSummary)){
			userRigth = true;
		}
		
		
		if(($scope.requestStatusObject == null) && userRigth == false && $scope.againstServiceOrDeposit==2)
    	{
			$scope.discountRadio=2;
			$scope.discountDisabledFlagForRequest =true;
    	    $scope.creditNoteWiseDiscout.discountAmountCreditNoteWise = $scope.totalDiscountSummary;
    	    $scope.billWiseDiscountObj.requestAmountBillWise = $scope.totalDiscountSummary;
    		var URI = BASE_URL + ROOT_URL
    		+ "/billing/getUsersListHavingCreditRights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/amt/"+$scope.creditNoteWiseDiscout.discountAmountCreditNoteWise+"/per/"+DisPer;
                   BillingGenericService.serviceAction(METHOD_GET, URI,"",
    				NOTIFICATION_MSG_STATUS_FALSE)
    		.then(function(response) {
    					console.log(response);
    					if (response.data.status == "success")
    						{
    						  $scope.userDiscountList =[];
    						  $scope.userDiscountList = response.data.listObject;
    						}
    						
    				});
                   
                   /*angular.forEach($scope.billDetailsListClone,function(cloneObj,key)
      					 {
                	       cloneObj.discountDisabledFlag ='Y';
      					});
*/                   
		   $scope.requestDiscountDiv = true;
		   $scope.isBillWiseDiscountApplied = false;
		   growl.warning("Please send a discount request to user..!!",{
				title : WARNING_MSG
			});
    		return false;
		 }
		
		if($scope.patientDetails.patientId==""||$scope.patientDetails.patientId==null)
		{
		    growl.warning("Please select patient...!!",{
			   title : WARNING_MSG
		    });
		     return false;
		}
		if($scope.billMstId==""||$scope.billMstId==null)
			{
			growl.warning("Please select bill...!!",{
				title : WARNING_MSG
			});
			return false;
			}
		if($scope.creditNoteMstObj.billPayeeId==""||$scope.creditNoteMstObj.billPayeeId==null)
			{
			  growl.warning("Please select payer...!!",{
				title : WARNING_MSG
			  });
    		  return false;
			}
		if($scope.requestDiscountDiv && $scope.againstServiceOrDeposit==2)
		{
		  growl.warning("Please send discount request...!!",{
			title : WARNING_MSG
		  });
		  return false;
		}
		if($scope.totaCrAmount==0||$scope.totaCrAmount==null||$scope.totaCrAmount==undefined)
		{
		  growl.warning("Credit note amount should be greater than zero...!!",{
			title : WARNING_MSG
		  });
		  return false;
		}
		if($scope.creditNoteMstObj.crReasonId==''||$scope.creditNoteMstObj.crReasonId==null)
		{
		 growl.warning("Please select credit note reason...!!",{
				title : WARNING_MSG
			  });
 		 return false;
		}
		else{
			
			if($scope.requestStatusObject != null &&  $scope.requestStatusObject.reqStatus == 2  && $scope.againstServiceOrDeposit == 2 ){ 
				 userRigth = true;
			}
			
			
					if($scope.againstServiceOrDeposit==1){
						
						if( $scope.creditNoteType !=null && $scope.creditNoteType == 1 && parseFloat($scope.toCreditNoteAmount) == 100 && $scope.finalTotalCrAmount >0){
							
							$scope.creditNoteMstObj.isApproved='Y';
				    		 $scope.creditNoteMstObj.isCreditNoteApproval=true;
				    	     $('#creditNoteSave').modal('show');
						
					}else{
						
						  if($scope.creditNoteApproval || (parseFloat($scope.fromCreditNoteAmount) <= parseFloat($scope.totalCrAmountR) && parseFloat($scope.toCreditNoteAmount) >= parseFloat($scope.totalCrAmountR)) && $scope.finalTotalCrAmount >0)
				    	  {
				    	  
				    	     $scope.creditNoteMstObj.isApproved='Y';
				    		 $scope.creditNoteMstObj.isCreditNoteApproval=true;
				    	     $('#creditNoteSave').modal('show');
				    	  }
				      else{
				    		
					        		 $scope.getListOfUser();
					        		  $('#creditNoteRequest').modal('show');
						        	  $scope.creditNoteMstObj.isApproved='N';
						    		  $scope.creditNoteMstObj.isCreditNoteApproval=false;
				          }
					}
						
					}else if($scope.againstServiceOrDeposit == 2 && userRigth == true && $scope.finalTotalCrAmount >0){
						
						$scope.creditNoteMstObj.isApproved='Y';
			    		 $scope.creditNoteMstObj.isCreditNoteApproval=true;
			    	     $('#creditNoteSave').modal('show');
					}
						    
	       }
	}
	
	$scope.disableCreditNoteSaveBtn = false;
	$scope.saveCreditNote = function()
	{
		$scope.disableCreditNoteSaveBtn = true;
		$('#creditNoteSave').modal('hide');
		$('#creditNoteRequest').modal('hide');
		if($scope.creditNoteMstObj.isApproved=='N' && $scope.creditNoteMstObj.requestSentTo=='')
		{		
			 growl.warning("Please select user.",{
  				title : WARNING_MSG
  			 });
			 $scope.disableCreditNoteSaveBtn = false;
     		 return false;
		}
		else{
		   if($scope.againstServiceOrDeposit==1)
		    {
			   $scope.disableCreditNoteSaveBtn = true;
			  $scope.saveAgainstService();
			  $scope.disableCreditNoteSaveBtn = false;
		    }
		else if($scope.againstServiceOrDeposit==2)
			{
			       $scope.disableCreditNoteSaveBtn = true;
				   $scope.saveCreditNoteAgainstDeposit();
				   $scope.disableCreditNoteSaveBtn = false;
		    }
		}
	}
	
	
	
	$scope.setTabValue = function(againstServiceOrDeposit)
	{
		 $scope.isBillWiseDiscountApplied=false;
	      $scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise=0;
	  	  $scope.creditNoteWiseDiscout.discountAmountCreditNoteWise=0;
	  	  $scope.creditNoteWiseDiscout.discountCategoryId == '';
	  	  if($scope.requestStatusObject==null){
			   $scope.requestDiscountFlag = false;
			   $scope.requestDiscountDiv = false;
			}
	      
	  	  
		$scope.againstServiceOrDeposit = againstServiceOrDeposit;
		$scope.calculateTotalAmount($scope.billDetailsListClone);
	}
	
	
	$scope.saveCreditNoteAgainstDeposit = function()
	{
		$rootScope.startLoader();
		var crTotalAmount = 0.00;
		//var crAmount =0.00;
		var crTaxAmount = 0.00;
		var crNetAmount = 0.00;
		var crDeuAmount = 0.00;
		var crOutAdjustmentAmount = 0.00;
		
		 var selfPayable = 0.00;
		 var creditPayable = 0.00;
		 var packageConsumptionAmt =  0.00;
		
		var creditNoteDetailsList = [];
		var flag = false;
		angular
		.forEach(
				$scope.billDetailsListClone,
				function(value, key) 
				{
					
				  if(value.discount>0&&value.discountPercentage>0 && value.isDiscountApplicable=='Y')
					{
					var creditNoteDetailsObj = angular.copy($scope.creditNoteDetailsObj);
					creditNoteDetailsObj.orderDetailId = value.orderDetailId;
					
					creditNoteDetailsObj.crTaxAmount = value.crTaxAmount;
					//creditNoteDetailsObj.crAmount = value.crAmount;
					creditNoteDetailsObj.crNetAmount= value.crNetAmount;
					creditNoteDetailsObj.crDiscountAmount = value.previousDiscount;
					creditNoteDetailsObj.crAdditionalDiscount = value.discount;
					creditNoteDetailsObj.crServiceRateAmount = value.grossAmount;
					
					creditNoteDetailsObj.orderId = value.orderId
					creditNoteDetailsObj.orderDetailsId = value.orderDetailsId;
					creditNoteDetailsObj.billingDetailsId =   value.billingDetailsId;
					
					
					 crTotalAmount = getAmount(crTotalAmount)+ getAmount(value.crAmount);
					 crTaxAmount = getAmount(crTaxAmount)+getAmount(value.crTaxAmount);
					 //crAmount = getAmount(crAmount)+getAmount(value.crAmount);
					 crNetAmount =getAmount(crNetAmount)+ (getAmount(value.crNetAmount) +  getAmount(value.crTaxAmount));
					 crDeuAmount = getAmount(crDeuAmount)+(getAmount(value.crNetAmount) +  getAmount(value.crTaxAmount));
					 
					 selfPayable = getAmount(selfPayable)+getAmount(value.selfPayable);
					 creditPayable = getAmount(creditPayable)+getAmount(value.creditPayable);
					 packageConsumptionAmt = getAmount(packageConsumptionAmt)+getAmount(value.packageConsumptionAmt);
					
					if (value.discountDetailsList.length>0) 
					{
						creditNoteDetailsObj.listCreditNoteDiscountDetailsDto = value.discountDetailsList;
						flag = true;
					}
					else
					{
						var creditNoteDiscountDetails = angular.copy($scope.creditNoteDiscountDetails);
						creditNoteDiscountDetails.discountCategoryId = value.discountCategoryId;
						creditNoteDiscountDetails.discountPercentage = value.discountPercentage;
						creditNoteDiscountDetails.discountAmount = value.discount;
						creditNoteDiscountDetails.discountGivenBy = $scope.userId;
						creditNoteDiscountDetails.discountCategory = "";
						creditNoteDetailsObj.listCreditNoteDiscountDetailsDto.push(creditNoteDiscountDetails);
						flag = true;
					}
				    	creditNoteDetailsList.push(creditNoteDetailsObj);
					}
				});
		
		if(flag)
			{
			var creditNoteMstObj = angular.copy($scope.creditNoteMstObj);
			creditNoteMstObj.counterId = $rootScope.counterId; 
			creditNoteMstObj.billId = $scope.billDetailsListClone[0].billingMasterId;
			
			creditNoteMstObj.crTotalAmount = getAmount(crTotalAmount);
			creditNoteMstObj.crTaxAmount = getAmount(crTaxAmount);
			//creditNoteMstObj.crAmount = getAmount(crAmount);
			creditNoteMstObj.crNetAmount = getAmount(crNetAmount) + getAmount($scope.discountRoundOff);
			creditNoteMstObj.crDeuAmount =	getAmount(crDeuAmount)+ getAmount($scope.discountRoundOff);
			creditNoteMstObj.discountRoundoff = $scope.discountRoundOff;
			
			
			creditNoteMstObj.crOutAdjustmentAmount = crOutAdjustmentAmount;
			
			creditNoteMstObj.status = 'A';
			creditNoteMstObj.cancelOrDiscount = 2;
			
		
			creditNoteMstObj.listCreditNoteDetails = creditNoteDetailsList;
			creditNoteMstObj.billPayeeId = $scope.creditNoteMstObj.billPayeeId;
			
			//creditNoteMstObj.selfPayable = getAmount(selfPayable);
			creditNoteMstObj.selfPayable = getAmount(crNetAmount) + getAmount($scope.discountRoundOff);
			creditNoteMstObj.creditPayable = getAmount(creditPayable);
			creditNoteMstObj.packageConsumptionAmt = getAmount(packageConsumptionAmt);
			
		/*	creditNoteDetailsObj.selfPayable = value.selfPayable;
			creditNoteDetailsObj.creditPayable = value.creditPayable;
			creditNoteDetailsObj.packageConsumptionAmt = value.packageConsumptionAmt;*/
			
			
			console.log("creditNoteMstObj",JSON.stringify(creditNoteMstObj));
			//return false;
			var URI = BASE_URL + ROOT_URL + "/api/billing/creditnote/deposit";
			BillingGenericService.serviceAction(METHOD_POST,URI,creditNoteMstObj,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
				if (response.data.status == "success")
				{
					$rootScope.stopLoader();
					$scope.patientDetailsList = [];
					$scope.patientDetails = {};
					$scope.billDetailsListClone = [];
					$scope.billList = [];
					$scope.initCreditNotMastObj();
					$scope.initCreditNoteDetailsObj();
					$scope.billMstId =null;
					$scope.requestDiscountDiv = false;
					//ForReport Print
					var data =  {
			                 "appName": "HIS",
			                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
			                 "reportDesignFilePath": "OPD_CreditNote.rpt",
			                 "reportFormat": "PDF",
			                 "exportFileName": "CreditNote",
			                 "reportParams": [
			                   {
			                  "paramName": "@credit_note_id",
			                  "paramType": "Integer",
			                  "paramValue": response.data.object
			                }
			                 ]
			      };
					
					$state.go('refundAgainstCR');
					reportService.generate(REPORT_API_URI1,data);
					
					$rootScope.stopLoader();
				}
				else if(response.data.status == "error")
					{
					   $rootScope.stopLoader();
					} 
				
			 });
			}
		else{
			growl.warning("Please select a service...!!",{
				title : WARNING_MSG
			});
			$rootScope.stopLoader();
			return;
		 }
		
		
	}
	
	
	//totalAmountSummary
	

    $scope.funDiscountPercentageBillWise = function()
    {
    	//alert($scope.billWiseDiscountObj.discountPercentageBillWise);
    	/*if($scope.autoRenderedServicesFinalList.length > 0){
    		var totalDisAmount =(((parseFloat($scope.totalAmountSummary) - parseFloat($scope.totalConcessionSummary))*(parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise)/100))).toFixed(2);
    		if(totalDisAmount > 0){
    		$scope.billWiseDiscountObj.discountPercentageBillWise = parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise).toFixed(2);
    		$scope.billWiseDiscountObj.discountAmountBillWise = totalDisAmount;
    		}
    	}
    	totalAmountSummary*/
    	
    	
    	var devide = ($scope.billDetailsListClone!=null&&$scope.billDetailsListClone.length>0)?$scope.billDetailsListClone.length:1
    	var totalDisAmount =((getAmount($scope.totalAmountSummaryDis)*(getAmount($scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise	)/100)));
    	$scope.creditNoteWiseDiscout.discountAmountCreditNoteWise = getAmount(totalDisAmount);
    }
    
    $scope.funDiscountAmountBillWise = function()
    {
    	//alert($scope.billWiseDiscountObj.discountAmountBillWise);
    	/*if($scope.autoRenderedServicesFinalList.length > 0){
    		var totalDisPercentage =((parseFloat($scope.billWiseDiscountObj.discountAmountBillWise)/(parseFloat($scope.totalAmountSummary)-parseFloat($scope.totalConcessionSummary)))*100).toFixed(2);
    		if(totalDisPercentage > 0){
    		$scope.billWiseDiscountObj.discountPercentageBillWise = totalDisPercentage;
    		$scope.billWiseDiscountObj.discountAmountBillWise = parseFloat($scope.billWiseDiscountObj.discountAmountBillWise).toFixed(2);
    		}
    	}
    	*/
    	var devide = ($scope.billDetailsListClone!=null&&$scope.billDetailsListClone.length>0)?$scope.billDetailsListClone.length:1
    	var totalDisPercentage =parseFloat($scope.totalAmountSummaryDis)>0?((Number(parseFloat($scope.creditNoteWiseDiscout.discountAmountCreditNoteWise))/(parseFloat($scope.totalAmountSummaryDis)))*100):0;
    	totalDisPercentage = Number(parseFloat(totalDisPercentage).toFixed(2));
    	$scope.creditNoteWiseDiscout.discountPercentageCreditNoteWise = getAmount(totalDisPercentage);
    	
    }
    
    
	$scope.saveAgainstService = function()
	{
		    $rootScope.startLoader();
			var creditNoteDetailsList = [];
		
			var crTotalAmount = 0.00;
			var crTaxAmount = 0.00;
			//var crAmount =0.00;
			var crNetAmount = 0.00;
			var crDeuAmount = 0.00;
			var crOutAdjustmentAmount = 0.00;
			
			var selfPayable =  0.00;
			var creditPayable =  0.00;
			var packageConsumptionAmt =  0.00;
			
			var flag = false;
			$scope.orderObj = {};
			angular
			.forEach(
					$scope.billDetailsListClone,
					function(value, key) 
					{
						if (value.isSelected == true) 
						{
							var creditNoteDetailsObj = angular.copy($scope.creditNoteDetailsObj);
							creditNoteDetailsObj.orderDetailId = value.orderDetailId;
							
							creditNoteDetailsObj.crTaxAmount = value.taxAmount;
							//creditNoteDetailsObj.crAmount = value.amount;
							creditNoteDetailsObj.crNetAmount= value.netAmt;
							creditNoteDetailsObj.crDiscountAmount = value.previousDiscount;
							creditNoteDetailsObj.crAdditionalDiscount = 0.00;
							creditNoteDetailsObj.crServiceRateAmount = value.grossAmount;
							
							
							 crTotalAmount = getAmount(crTotalAmount)+ getAmount(value.grossAmount);
							 crTaxAmount = getAmount(crTaxAmount)+getAmount(value.taxAmount);
							 //crAmount = getAmount(crAmount)+getAmount(value.amount);
							/* crNetAmount =getAmount(crNetAmount)+ getAmount(value.netAmt);
							 crDeuAmount = getAmount(crDeuAmount)+getAmount(value.netAmt);*/
							 
							 crNetAmount =getAmount(crNetAmount)+ (getAmount(value.crNetAmount) +  getAmount(value.crTaxAmount));
							 crDeuAmount = getAmount(crDeuAmount)+(getAmount(value.crNetAmount) +  getAmount(value.crTaxAmount));
							 
							 selfPayable = getAmount(selfPayable)+getAmount(value.selfPayable);
							 creditPayable = getAmount(creditPayable)+getAmount(value.creditPayable);
							 packageConsumptionAmt = getAmount(packageConsumptionAmt)+getAmount(value.packageConsumptionAmt);
							
							creditNoteDetailsObj.orderId = value.orderId
							creditNoteDetailsObj.orderDetailsId = value.orderDetailsId;
							creditNoteDetailsObj.billingDetailsId =   value.billingDetailsId;
							
							creditNoteDetailsObj.selfPayable = value.selfPayable;
							creditNoteDetailsObj.creditPayable = value.creditPayable;
							creditNoteDetailsObj.packageConsumptionAmt = value.packageConsumptionAmt;
							creditNoteDetailsObj.isPackage =value.package;
							creditNoteDetailsObj.packageId =value.packageId;
							creditNoteDetailsList.push(creditNoteDetailsObj);
							
							  if(flag == false){
								  $scope.orderObj.orderId =  value.orderId;
								  $scope.orderObj.patientId = $scope.creditNoteMstObj.patientId;
								  $scope.orderObj.createdBy =  creditNoteDetailsObj.createdBy;
								  $scope.orderObj.orgId =  creditNoteDetailsObj.organisationId;
								  $scope.orderObj.orgUnitId =  creditNoteDetailsObj.unitId;
	                            }
							flag = true;
						}
					});
			
			if(flag)
				{
				var creditNoteMstObj = angular.copy($scope.creditNoteMstObj);
				creditNoteMstObj.billId = $scope.billDetailsList[0].billingMasterId;
				creditNoteMstObj.counterId = $rootScope.counterId; 
				creditNoteMstObj.crTotalAmount = getAmount(crTotalAmount);
				creditNoteMstObj.crTaxAmount =getAmount(crTaxAmount);
				//creditNoteMstObj.crAmount =getAmount(crAmount);
				creditNoteMstObj.crNetAmount =getAmount(crNetAmount);
				creditNoteMstObj.crOutAdjustmentAmount = getAmount(crOutAdjustmentAmount);
				creditNoteMstObj.crDeuAmount =	getAmount(crDeuAmount);
				
				creditNoteMstObj.status = 'A';
				creditNoteMstObj.cancelOrDiscount = 1;
				creditNoteMstObj.listCreditNoteDetails = creditNoteDetailsList;
				creditNoteMstObj.billPayeeId = $scope.creditNoteMstObj.billPayeeId;
				
				creditNoteMstObj.selfPayable = getAmount(selfPayable);
				creditNoteMstObj.creditPayable = getAmount(creditPayable);
				creditNoteMstObj.packageConsumptionAmt = getAmount(packageConsumptionAmt);
				
				console.log("creditNoteMstObj",JSON.stringify(creditNoteMstObj));
				//return false;
				
				var URI = BASE_URL + ROOT_URL + "/api/billing/creditnote/service";
				BillingGenericService.serviceAction(METHOD_POST,URI,creditNoteMstObj,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
					if (response.data.status == "success"){
						$rootScope.stopLoader();
						
						
						var URI11 = BASE_URL + ROOT_URL + "/LIS/BioChemistry/releaseMailOnCreditNote";
						BillingGenericService.serviceAction(METHOD_POST,URI11,$scope.orderObj,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
							if (response.data.status == "success"){
								$rootScope.stopLoader();
								
							}
							else if(response.data.status == "error")
							{
							   $rootScope.stopLoader();
							} 
						 });
						
						
						$scope.billDetailsListClone = [];
						$scope.patientDetails = {};
						$scope.billDetailsList = [];
						$scope.billList = [];
						$scope.initCreditNotMastObj();
						$scope.initCreditNoteDetailsObj();
						$scope.billMstId =null;
						//ForReport Print
						var data =  {
				                 "appName": "HIS",
				                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
				                 "reportDesignFilePath": "OPD_CreditNote.rpt",
				                 "reportFormat": "PDF",
				                 "exportFileName": "CreditNote",
				                 "reportParams": [
				                   {
				                  "paramName": "@credit_note_id",
				                  "paramType": "Integer",
				                  "paramValue": response.data.object
				                }
				                 ]
				      };
						$state.go('refundAgainstCR');
						reportService.generate(REPORT_API_URI1,data);
						
						$rootScope.stopLoader();
					}
					else if(response.data.status == "error")
					{
					   $rootScope.stopLoader();
					} 
				 });
				}
			else{
				growl.warning("Please select a service...!!",{
					title : WARNING_MSG
				});
				$rootScope.stopLoader();
				return;
			 }
		
	}
	
	
	
	}catch(e){
		console.log(e.message);
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
	                         patSearchObj = {};
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
	
}]);