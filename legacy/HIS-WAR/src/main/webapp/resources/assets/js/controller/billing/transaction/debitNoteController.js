/**
 * @author Ganesh Chaudhari
 */
angular.module("myApp").controller("debitNoteController", ['$scope', '$rootScope', '$cookies', '$state', 'BillingGenericService', 'GenericService', '$stateParams', '$timeout', 'growl', 'paymentCollFactory', 'GenericServiceParamHeader','reportService','$state','servicesFactory', function($scope, $rootScope, $cookies, $state, BillingGenericService, GenericService, $stateParams, $timeout, growl, paymentCollFactory, GenericServiceParamHeader,reportService,$state,servicesFactory) {

    var addService = 0;
    var init = 1;
    var quantity = 2;
    var unitPrice = 3;
    var discountPer = 4;
    var discount = 5;
    var discountCat = 5;
    $scope.isPatientClass = 'N';
    //alert(parseFloat('6987').toFixed(2));
    
    
    
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
    
    $scope.labConfig = JSON.parse(localStorage.getItem('labConfig'));
    $scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
    $scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
    $scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
    $scope.selfPayeeIdCookies = cookieObjectAppConfig.selfPayeeId;
	$scope.pharmacyServiceId = cookieObjectAppConfig.pharmacyServiceId;
	$scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
	$scope.surchargeServiceId = cookieObjectAppConfig.surchargeServiceId;
	$scope.pharmacyServiceId = cookieObjectAppConfig.pharmacyServiceId;
	$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
	$scope.isSchemeDiscount = cookieObjectAppConfig.isSchemeDiscount;
	$scope.isGstVisible = cookieObjectAppConfig.isGstVisible;
	
    $scope.unitId = cookieObject.unitId;
    $scope.organizationId = cookieObject.organizationId;
    $scope.userId = cookieObject.userId;

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

    $scope.init = function() 
    {
      
     $scope.isBillWiseDiscountApplied = false;
	 $scope.isSplitBillFlag  =false;
	 $scope.isSplitBillApplied = false;
	 $scope.serviceViseSplit = false;
	 $scope.service = null;
     $scope.getDisRiqFlag = true;

    	$scope.isBillWiseDiscountApplied = false;;
    	$scope.renderingDate = [];
    	$scope.datepickerAddService = function(index) {

			$scope.renderingDate[index].opened = true;
        };
        $scope.dateOptions = 
           {
				formatYear : 'yyyy',
				showWeeks : false,
				maxDate:new Date()
			};
    	
    	$("#savedisablebtn").addClass("disabled");
    	$scope.AddServiceBtn = true;
    	$scope.payeeId='';
       
        $rootScope.loginpage = true;
        $scope.currentDate = new Date();
        $scope.serviceMasterIdCheck = {};
        $scope.itemSelecteds = {};
        $scope.autoRenderedServicesFinalList = [];
        $scope.removeDiscountList = [];
        $scope.patientList = [];
        $scope.removeServicesList = [];
        $scope.patientDetailsList = [];
 	    $scope.payeeList= [];
 	    $scope.billList= [];
 	    $scope.billing=true;
        
        $scope.finalBill = "N";
        $scope.roundOffAmt = parseFloat(0).toFixed(2);
        $scope.billRemark = '';
        
        $scope.transferToActiveDeposit = "N";
        $scope.discountRadio = 1;
        $scope.totalAmountSummary = "0.00";
        $scope.totalConcessionSummary = "0.00";
        $scope.netPayableSummary = "0.00";
        $scope.totalDiscountSummary = "0.00";
        $scope.totalGstTaxSummary = "0.00";
        $scope.totalCreditPayble = "0.00";
        $scope.totalSelfPayble = "0.00";
        $scope.totalSelfPaybleTemp = "0.00";
        
        $scope.groupWiseDisFlag = true;


        $scope.billing = true;
        $scope.dateOptions = 
           {
    			formatYear : 'yyyy',
    			showWeeks : false,
    			maxDate:new Date()
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
    	

        var commonObj = {
            unitId: $scope.unitId,
            organizationId: $scope.organizationId
        };

      //start for request discount amount DD
    	$scope.requestDiscountDiv = false;
    	$scope.requestApporivedDiscountDiv = false;
    	$scope.discountDisabledFlagForRequest = false;
    	//end for discount amount DD
    	 
    	
        // for popup
        $scope.showModal = false;
        $scope.buttonClicked = "";
        $scope.popUpFlag = true;
        // end for popup
        
        $scope.contractId = 0;
        
        $scope.patientSearchObj = {
            "uhIdNumber": "",
            "patientName": "",
            "genderId": "",
            "mobileNo": "",
            "birthDate": "",
            "identificationNo": "",
            "patientType": "1",
            "unitId": $scope.unitId,
            "organizationId": $scope.organizationId
        }

        
        $scope.patientDetails = {
				"patientName" : "",
				"genderCode" : "",
				"age" : "",
				"uhIdNumber" : "",
				"dob" : "",
				"doctorFullName" : "",
				"dateOfAdmission" : "",
				"patientDischargeDate" : "",
				"wardName" : "",
				"bedNumber" : "",
				"imagePath":"",
				"patientId":"",
				"visitTypeId":"",
				"admissionId":"",
				"genderId":"",
				"patientTypeId":"",
			}
        

        $scope.encounterDetails = {
            encounterId: ""
        }
        $scope.billObj ={};
        
        $scope.billingMasterObj = {
            "billingMasterId": "",
            "encounterId": $scope.billObj.encounterId,
            "admissionId": $scope.billObj.admissionId,
            "visitTypeId": $scope.billObj.visitTypeId,
            "patientId": $scope.patientDetails.patientId,
            "billNumber": "",
            "billDateTime": new Date().getTime(),
            "netAmount": "",
            "discountAmount": "",
            "grossAmount": "",
            "concessionAmount": "",
            "gst": "",
            "totalBillAmount": "",
            "outstanding": "",
            "roundOff": "",
            "isCreditBill": "",
            "unitId": $scope.unitId,
            "organisationId": $scope.organizationId,
            "status": "A",
            "createdDate": new Date().getTime(),
            "createdBy": $scope.userId,
            "updatedDate": new Date().getTime(),
            "updatedBy": $scope.userId,
            "bedTypeId": "1",
            "patientTypeId": "1",
            "paymentTypeId": "1",
            "isDiscountInPercentage": 'I',
            "isDiscountApplicable": "I",
            "isDrNote":"Y",
            "drNoteAgainstBillId":"",
            "listBillingDetailsDto": [],
			"counterId ":$rootScope.counterId,
            "listAutoRenderedServiesBillingDetails": [],
            //	"orderDetailsDiscountDto":"",
            "orderMasterDto": {
                "orderId": "",
                "orgId": $scope.organizationId,
                "orgUnitId": $scope.unitId,
                "orderNo": "",
                "orderDate": new Date().getTime(),
                "visitTypeId": $scope.billObj.visitTypeId,
                "encounterId": $scope.billObj.encounterId,
                "admissionId": $scope.billObj.admissionId,
                "ordDoctorId": "",
                "patientId": $scope.patientDetails.patientId,
                "priorityId": "2",
                "patientType": "",
                "wardId": "",
                "drCrId": "",
                "ordRemarks": "",
                "createdBy": "1",
                "createdDate": new Date().getTime(),
                "updatedBy": "1",
                "updatedDate": new Date().getTime(),
                "orderStatus": "A",
                "orderStatusId": "1",
                "listOrderDetailsMasterDto": []
            }

        }
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
        
        var URI = BASE_URL + ROOT_URL +
            GETACTIVESPECIALITY;
        BillingGenericService.serviceAction(METHOD_POST, URI, commonObj,
                NOTIFICATION_MSG_STATUS_FALSE)
            .then(function(response) {
                console.log(response);
                // $rootScope.startLoader();
                if (response.data.status == "success") {
                    $scope.specialityMasterList = response.data.listObject;

                }

            });
        
        var URI = BASE_URL + ROOT_URL +"/api/billing/getPatientType/"+$scope.organizationId+"/"+$scope.unitId;
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

        var URI = BASE_URL + ROOT_URL + GETACTIVEREASONLIST;

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

//        var URI = BASE_URL + ROOT_URL + GETACTIVEDOCTORLIST;
        var URI = BASE_URL + ROOT_URL + "/adt/getActiveDoctorList";
        GenericService
            .serviceAction("POST", URI, commonObj)
            .then(
                function(response) {
                    $scope.doctorsList = [];

                    if (response.data.status == "success") {
                        $scope.doctorsList = response.data.listObject;

                    }
                });


        var URI = BASE_URL + ROOT_URL + "/api/billing/discountCategories/orgId/" + $scope.organizationId + "/unitId/" + $scope.unitId;
        BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
            $scope.discountCategoryList = response.data.listObject;
        });

    	$scope.getSexMasterList = function() {
    		var URI = BASE_URL + ROOT_URL + GETACTIVEGENDERLIST;
    		BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
    			$scope.sexMasterList = response.data.listObject;
    		});
    	}
    	
    	$scope.getSexMasterList();
    	
    	$scope.getIdentificationTypeList = function(){
    		var URI = BASE_URL + ROOT_URL + GETACTIVEIDENTIFICATION;
    		BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
    			if (response.data.status == "success")
    				$scope.identificationTypeList = response.data.listObject;
    		});
    	}
    	$scope.getIdentificationTypeList();
    	
        $scope.servicesSelectedList = {
            services: []
        };

        $scope.groupWiseServicesSelectedList = {
            services: []
        }


        $scope.billWiseDiscountObj = {
            "discountGivenBy": "",
            "discountCategoryId": "",
            "discountAmountBillWise": "",
            "discountPercentageBillWise": ""
        }

        //yogesh
        $scope.searchPackageList = [];
        $scope.selectedPackageList = [];
        $scope.packageDetailsList = [];
        
        //list for packages
	    $scope.listPackageOrder = [];
    }
    
    $scope.init();//init All Data
    
    $scope.searchPatient = function(patientSearchObj) 
    {
    	$scope.requestStatusObject = {};
        $scope.getDisRiqFlag = true;

    	$scope.requestApporivedDiscountDiv = false;
		$scope.billWiseDiscountObj.discountPercentageBillWise = "";
		$scope.billWiseDiscountObj.discountAmountBillWise ="";
		$scope.billWiseDiscountObj.discountCategoryId ="";
        $scope.patientSearchObj.unitId = cookieObject.unitId;
        $scope.patientSearchObj.organizationId = cookieObject.organizationId;
        $scope.patientDetails = {};
        if(patientSearchObj.patientName!="" && patientSearchObj.patientName.length < 3)
		{
			growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
			$scope.patientList = [];
			return false;
		}	
        var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
        var patSearchObj = angular.copy($scope.patientSearchObj);
        if ($scope.patientSearchObj.birthDate != "" && typeof($scope.patientSearchObj.birthDate) != "undefined") {
            var dt = new Date($scope.patientSearchObj.birthDate);
//            patSearchObj.birthDate = dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + dt.getFullYear();
			patSearchObj.birthDate = dt.getFullYear() + "-" +(dt.getMonth() + 1)+ "-" +dt.getDate();

        }
        $rootScope.startLoader();
        BillingGenericService.serviceAction(METHOD_POST, URI, patSearchObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
            $scope.searchPatientList = [];
            console.log("response", response);
            if (response.data.status == "success") {
                $scope.patientList = response.data.listObject;
                $scope.billList = [];
                $scope.autoRenderedServicesFinalList = [];
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
            
            
            $scope.totalAmountSummary = "0.00";
			$scope.totalConcessionSummary = "0.00";
			$scope.netPayableSummary = "0.00";
			$scope.totalDiscountSummary = "0.00";
			$scope.totalGstTaxSummary = "0.00";
			$scope.totalCreditPayble = "0.00";
			$scope.totalSelfPayble = "0.00";
            
        });
    }

    $scope.getPatientDetailsById = function(patientId)
    {
        $scope.patientDetailsList = [];
        $scope.patientDetails = {};
        $rootScope.startLoader();
        $scope.requestDiscountDiv  = false;
        var URI = BASE_URL + ROOT_URL + "/api/global/patient/billing/" + patientId;
        BillingGenericService.serviceAction(METHOD_GET, URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
            if (response.data.status == "success") {
                $scope.patientDetailsList = response.data.listObject;
                $scope.patientDetails = response.data.listObject[0];
                $scope.billing = $scope.patientDetails.visitTypeId!=1?false:true;
                $scope.getBillListByPatientId(patientId);
                var index = $scope.patientList.findIndex(x => x.patientId ==patientId);
                $scope.getPatientDetailsByIndex(index);
                $('#billDtlsId').prop('checked', false);
                $scope.AddServiceBtn = true;
                $scope.billMasterIds = [];
                $scope.isBillWiseDiscountApplied = false;
            }
            $rootScope.stopLoader();
        });
    }


    $scope.getBillListByPatientId = function(patientId) {
        $scope.billList = [];
        $scope.autoRenderedServicesFinalList = [];
        $rootScope.startLoader();
        var URI = BASE_URL + ROOT_URL + "/api/billing/billList/" + $scope.organizationId + "/" + $scope.unitId + "/" + patientId+"/Y";
        BillingGenericService.serviceAction(METHOD_GET, URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
            if (response.data.status == "success") {
            	if(response.data.listObject.length>0){
            		angular.forEach(response.data.listObject,function(value,index){
            			
            			if(value.encounterStatus == null || value.encounterStatus == 'A'){
            				$scope.billList.push(value);
            			}
            		});
            	}
             //   $scope.billList = response.data.listObject;
                if ($scope.billList != null && $scope.billList.length > 0){
                    $(".anchor-tag").parents().next(".old-encounter-details").toggle();
                    
                    
                }
            }
            $rootScope.stopLoader();
        });
    }
    
    $scope.billObj = {};
    
    $scope.getBillDetailsByBillId = function(billObj)
	{
    	$scope.billMasterIds = [];
    	$scope.billMasterIds.push(billObj.billingMasterId);
    	$scope.autoRenderedServicesFinalList = [];
    	console.log("billObj",JSON.stringify(billObj));
    	$scope.isBillWiseDiscountApplied =false;
    	$scope.getDisRiqFlag = true;
    	$scope.billObj = billObj;
		$scope.payeeList = [];
		$rootScope.startLoader();
		$scope.AddServiceBtn = false;
		//$scope.payeeId = $scope.selfPayeeIdCookies
		
		var URI1 = BASE_URL + ROOT_URL + "/api/billing/payee/bill/"+billObj.billingMasterId;
		BillingGenericService.serviceAction(METHOD_GET,URI1,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
		 {
			
			 $scope.visitTypeId =billObj.visitTypeId;
			 $scope.billing =$scope.visitTypeId!=1?true:false;
			/*if (response.data.status == "success")
			{
				$scope.payeeList = response.data.listObject;
			}*/
			if(billObj.visitTypeId==1)
			{
				$scope.encounterDetails.encounterId = billObj.encounterId;
				
				$scope.getEncounterDetails(billObj.encounterId);
				
				var URL = BASE_URL + ROOT_URL +"/api/opd/sponsoredDetails/admission/0/encounter/"+billObj.encounterId;
				var URIDETENC = BASE_URL + ROOT_URL + "/api/opd/encounter/"+billObj.encounterId;
				
				
				BillingGenericService.serviceAction(METHOD_GET, URIDETENC, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						
						$scope.encounterDetailsENC = {};
						
						angular.forEach(response.data.object.patientSponsorDetailsDtosList,function(value,index){
							$scope.encounterDetailsENC.payeeId = value.payeeId;
							if($scope.encounterDetailsENC.payeeId != $scope.selfPayeeId && value.priorityId==1)
							{
								$scope.contractId = value.contractId;
								$scope.payeeId = $scope.encounterDetailsENC.payeeId;
							}
							else if ($scope.encounterDetailsENC.payeeId == $scope.selfPayeeId && value.priorityId==1)
							{
								$scope.contractId = 0;
								$scope.payeeId =  $scope.selfPayeeId;
							}
						});
							
					}
				});
				
				
			}
		    else if(billObj.visitTypeId==2 || billObj.visitTypeId==3 || billObj.visitTypeId==4)
			{
		    	$scope.getAdmissionDetails(billObj.admissionId);
		    	
		    	var URL = BASE_URL + ROOT_URL +"/api/opd/sponsoredDetails/admission/"+billObj.admissionId+"/encounter/0";
		    	var URIDETADM = BASE_URL + ROOT_URL + "/api/ipd/admissions/patient/"+billObj.admissionId;
		    	
		    	BillingGenericService.serviceAction(METHOD_GET,URIDETADM, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						$scope.admissionDetailsContract = response.data.object;
						
					if($scope.admissionDetailsContract.payeeId != $scope.selfPayeeId && value.priorityId==1)
					{
						$scope.contractId = $scope.admissionDetailsContract.contractId;
						$scope.payeeId =  $scope.admissionDetailsContract.payeeId;
					}
					else if ($scope.encounterDetailsENC.payeeId == $scope.selfPayeeId && value.priorityId==1)
					{
						$scope.contractId = 0;
						$scope.payeeId =  $scope.selfPayeeId;
					}
				 }
				});
		    	
			}
			
			 BillingGenericService.serviceAction(METHOD_GET,URL,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
			 {
				 if (response.data.status == "success")
					{
					 $scope.payeeList = response.data.listObject;
					
					}
			 });
			
		});
	}
    
    
    /*================================================ */
 
    $scope.autoRenderedServicesBillingDetails=[];
    
    $scope.addPaymentDetails = function(){
		paymentCollFactory.addPaymentDetails($scope);
	}
	$scope.removePaymentDetails = function(){
		paymentCollFactory.removePaymentDetails($scope);
	}
	
	 $scope.checkAll = function()
	 {
		 $scope.servicesSelectedList.services = $scope.autoRenderedServicesFinalList.map(function(item) { return item.serviceMasterId; });
		  
	 };
		  
			
$scope.groupWiseCheckAll= function(chkFlg) {
			$scope.disPerPopupForGroup = '';
			$scope.disAmtPopupForGroup = '';
			$scope.disCategoryIdForGroup = '';
			$scope.disPerPopupForGroupFull = 0;
			if(chkFlg == 'Y'){
				$scope.groupWiseServicesSelectedList.services = $scope.groupWiseDiscountServicesList.map(function(item) { return item.orderDetailsId; });
				}else{
					$scope.groupWiseDisFlag = true;
					$scope.groupWiseServicesSelectedList.services = [];
				}
	 };
	
	
	$scope.getSexMasterList = function(){
		$rootScope.startLoader();
		var URI = BASE_URL + ROOT_URL + "/adt/getActiveGenderList";
		BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			$scope.sexMasterList = response.data.listObject;
		});
	}
	$scope.getSexMasterList();
	
	$scope.selectItem = function(item) {

		$scope.itemSelecteds = [];
		// If checkbox is checked
		if ($scope.serviceMasterIdCheck) {
			$scope.itemSelecteds = item;
		}
		console.log(item);
	}
	
	
	
	$scope.getPatientDetailsByIndex = function(index){
		$rootScope.startLoader();
        $scope.getDisRiqFlag = true;

		$scope.patientDetails.age=($scope.patientList[index].age);
		$scope.patientDetails.patientName=($scope.patientList[index].patientName);
		$scope.patientDetails.genderCode=($scope.patientList[index].genderCode);
		$scope.patientDetails.uhIdNumber=($scope.patientList[index].uhIdNumber);
		$scope.patientDetails.patientId=($scope.patientList[index].patientId);
		$scope.patientDetails.birthDate=($scope.patientList[index].birthDate);
		$scope.patientDetails.patientProfile = ($scope.patientList[index].patientProfile);
		$scope.patientDetails.genderId = ($scope.patientList[index].genderId);
		$scope.admissionId = ($scope.patientList[index].admissionId);
		$scope.patientDetails.patientTypeId = ($scope.patientList[index].patientCategoryId);
		$scope.isBillWiseDiscountApplied =false;
		$scope.totalSelfPayble ="0.00";
		if($scope.admissionId>0)
			{
			var URI = BASE_URL + ROOT_URL + "/api/ipd/admissions/patient/"+$scope.admissionId;
			BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status == "success")
				{
					$scope.admissionDetails = response.data.object;
					$scope.admissionDetails.patientClassId = null;
					$scope.dateOfAdmission = typeof($scope.admissionDetails.doa) != "undefined" ? $scope.admissionDetails.doa : "";
					$scope.patientDischargeDate = typeof($scope.admissionDetails.pdd) != "undefined" ? $scope.admissionDetails.pdd : "";
					$scope.bedNumber = typeof($scope.admissionDetails.bedNo) != "undefined" ? $scope.admissionDetails.bedNo : "";
					$scope.doctorFullName = typeof($scope.admissionDetails.doctorName) != "undefined" ? $scope.admissionDetails.doctorName : "";
					$scope.wardNo = typeof($scope.admissionDetails.wardNo) != "undefined" ? $scope.admissionDetails.wardNo : "";
					
				    $scope.patientDetails.doctorFullName = $scope.doctorFullName;
				    $scope.patientDetails.dateOfAdmission = $scope.dateOfAdmission;
				    $scope.patientDetails.patientDischargeDate = $scope.patientDischargeDate;
				    $scope.patientDetails.wardName = $scope.wardNo;
				    $scope.patientDetails.bedNumber = $scope.bedNumber ;
				    $rootScope.stopLoader();
		      }	else if(response.data.status == "success")
		    	  {
		    	     $rootScope.stopLoader();
		    	  }
			   });	
			}

		$(".patienttSearchResult").toggle();
			$rootScope.stopLoader();
	}

	
	
	
	
	
	
	
	
	
	
	
	
	$scope.getEncounterDetails = function(encounterId)
	{
		var data = {};
		$scope.requestApporivedDiscountDiv = false;
		$scope.billWiseDiscountObj.discountPercentageBillWise = "";
		$scope.billWiseDiscountObj.discountAmountBillWise ="";
		var URI = BASE_URL + ROOT_URL + "/api/opd/encounter/"+encounterId;
		BillingGenericService.serviceAction(METHOD_GET,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			if(response.data.status == "success"){
				
				if(response.data.object!=null)
					{
						//alert();
					//	$scope.AddServiceBtn = false;
						//$("#savedisablebtn").removeClass("disabled");
					}
				$scope.encounterDetails = {
						encounterId : response.data.object.encounterId.toString(),
						encounterDateString : response.data.object.encounterDateString,
						referralId : response.data.object.referralId,
						referralDesc : response.data.object.referralDesc,
						specialityName : response.data.object.specialityName,
						doctorName : response.data.object.doctorName,
						patientCategory : response.data.object.patientCategory,
						//paymentEntitlementDesc : response.data.object.paymentEntitlementDesc,
						//companyDesc : response.data.object.companyDesc,
						//paymentEntitlementTypeId : response.data.object.paymentEntitlementTypeId,
						//payeeId : response.data.object.payeeId,
						patientId : response.data.object.patientId,
						doctorId : response.data.object.doctorId,
						modalityId : response.data.object.modalityId,
						subSpecialityName : response.data.object.subSpecialityName,
						modalityDesc : response.data.object.modalityDesc,
						specialityId : response.data.object.specialityId,
						patientClassId : response.data.object.patientClassId, 
						isTariffRateApplicable : response.data.object.isTariffRateApplicable,
						patientClassId : response.data.object.patientClassId
						
				}
				//billingBed Category Id op
				$scope.isTariffRateApplicablePopup = $scope.encounterDetails.isTariffRateApplicable;
				$scope.patientClassIdPopup = ($scope.encounterDetails.patientClassId > 0 ? $scope.encounterDetails.patientClassId : '');
				$scope.encounterDetails.bedBillingCategoryId = $scope.opdBillingCatId;
				
				angular.forEach(response.data.object.patientSponsorDetailsDtosList,function(value,index){
					if(value.priorityId == '1'){
						$scope.encounterDetails.paymentEntitlementDesc  = value.paymentEntitlementDesc;
						$scope.encounterDetails.companyDesc = value.companyDesc;
						$scope.encounterDetails.contractDesc = value.contractName;
						$scope.encounterDetails.validityFrom = value.validityFrom;
						$scope.encounterDetails.validityTo = value.validityTo;
						$scope.encounterDetails.payeeId = value.payeeId;
						$scope.payeeId = value.payeeId+"";
						$scope.encounterDetails.paymentEntitlementTypeId = value.paymentEntitlementId;
					}
				});
				
				
				$scope.getAutoRenderedServicesByPatient(response.data.object);
		
			}
		});
		
		/*$scope.requestStatusObject = {};
		var data1 = {
				encounterId : encounterId,
				requestType : 4
		}
		var URI1 = BASE_URL + ROOT_URL + "/api/billing/getDiscReqDtlsForOPBilling";
		BillingGenericService.serviceAction(METHOD_POST,URI1, data1, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			if(response.data.status == "success"){
				if(response.data.listObject.length > 0)
				{
				$scope.requestStatusObject = response.data.listObject[0];
				if($scope.requestStatusObject.reqStatus == 2)
					{
					$scope.requestApporivedDiscountDiv = true;
					$scope.discountDisabledFlagForRequest = true;
					$scope.requestAppUser = $scope.requestStatusObject.requestSentToEmployee;
					$scope.approvedAmtApp = $scope.requestStatusObject.approvedDiscAmt;
					$scope.requestAmtApp =$scope.requestStatusObject.reqDisAmt;
					$scope.statusApp = "Approved.";
					$scope.discountRadio = 2;
						
					var totalDisPercentage =((parseFloat($scope.requestStatusObject.approvedDiscAmt)/$scope.requestStatusObject.netAmount)*100).toFixed(2);
		    		if(totalDisPercentage > 0){
		    		$scope.billWiseDiscountObj.discountPercentageBillWise = totalDisPercentage;
		    		$scope.billWiseDiscountObj.discountAmountBillWise = parseFloat($scope.requestStatusObject.approvedDiscAmt).toFixed(2);
		    		//12-10-2019
		    		//$scope.billWiseDiscountObj.discountPercentageBillWiseOld = totalDisPercentage;
		    		$scope.billWiseDiscountObj.discountPercentageBillWiseOld = (parseFloat($scope.requestStatusObject.approvedDiscAmt)/$scope.requestStatusObject.netAmount)*100;
		    		$scope.billWiseDiscountObj.discountCategoryId =  $scope.requestStatusObject.discType+"";
		    		//12-10-2019
		    		}
		    		}
				else if($scope.requestStatusObject.reqStatus == 1)
					{
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
					}
				if($scope.requestStatusObject.reqStatus !=null && $scope.requestStatusObject.reqStatus != 3)
				 {
						//New added For Disc Start
						$scope.totalSumDisAmountTemp = parseFloat(0);
		    			var i = 1;
		    			angular.forEach($scope.requestStatusObject.requestDiscountDetailsDtosList,function(value,index)
								 {
							        
							        	   $scope.totalSumDisAmountTemp =$scope.totalSumDisAmountTemp + value.netAmount;
							        	
							       
									i++;	
								 });
		    			//New added For Disc End
				 }
				}
				else{
					$scope.requestStatusObject = null;
					}
				}
		});*/
		
		$scope.getActivePackagesList();
		$scope.packageDetailsList = [];
		$scope.AddServiceBtn =  $scope.billMasterIds.length>0?false:true;
	
	}
	
	
	
	//Get Admission Details By Admission Id
	$scope.getAdmissionDetails = function(admissionId)
	{
		if(admissionId != null && typeof(admissionId) != "undefined")
		{
			$rootScope.startLoader();
		var URI = BASE_URL + ROOT_URL + "/api/ipd/admissions/patient/"+admissionId;
			BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status == "success")
				{
					$scope.admissionDetails = response.data.object;
					$scope.isTariffRateApplicablePopup = $scope.admissionDetails.isTariffRateApplicable;
					$scope.patientClassIdPopup = ($scope.admissionDetails.patientClassId > 0 ? $scope.admissionDetails.patientClassId : '');
					$scope.encounterDetails.encounterId =$scope.admissionDetails.admissionId;
					$scope.encounterDetails.patientId =$scope.patientDetails.patientId;
					$scope.encounterDetails.bedBillingCategoryId = $scope.admissionDetails.bedBillingCategoryId;
					$scope.encounterDetails.paymentEntitlementTypeId = $scope.admissionDetails.paymentEntitlementId;
					$scope.encounterDetails.patientCategoryId=$scope.admissionDetails.patientCategoryId;
					response.data.object.encounterId = admissionId;
					$scope.getAutoRenderedServicesByPatient(response.data.object);
				}
				$rootScope.stopLoader();
			});
			
			/*$scope.requestStatusObject = {};
			var data1 = {
					admissionId : $scope.admissionDetails.admissionId,
					requestType : 4
			}
			var URI1 = BASE_URL + ROOT_URL + "/api/billing/getDiscReqDtlsForOPBilling/";
			BillingGenericService.serviceAction(METHOD_POST,URI1, data1, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
				{
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
						$scope.discountRadio = 2; 
						
						var totalDisPercentage =((parseFloat($scope.requestStatusObject.approvedDiscAmt)/$scope.requestStatusObject.netAmount)*100).toFixed(2);
			    		if(totalDisPercentage > 0){
			    		$scope.billWiseDiscountObj.discountPercentageBillWise = totalDisPercentage;
			    		$scope.billWiseDiscountObj.discountAmountBillWise = parseFloat($scope.requestStatusObject.approvedDiscAmt).toFixed(2);
						
			    		}
			    		}
					else if($scope.requestStatusObject.reqStatus == 1)
						{
						    $scope.discountDisabledFlagForRequest = true;
						    $scope.requestApporivedDiscountDiv = true;
						    $scope.requestAppUser = $scope.requestStatusObject.requestSentToEmployee;
							$scope.approvedAmtApp = $scope.requestStatusObject.approvedDiscAmt;
							$scope.requestAmtApp =$scope.requestStatusObject.reqDisAmt;
							$scope.statusApp = "Pendding.";
							$scope.discountRadio = 2;
						}
					else
						{
						 $scope.requestStatusObject  = null;
						}
					
					}
					
					else{
						$scope.requestStatusObject = null;
						}
					}
			});*/
			$scope.AddServiceBtn =  $scope.billMasterIds.length>0?false:true;
		}
	}
	
	
$scope.applyPatientClassPopup = function(){
		
	if(($('#patientClassIdPopup').val() == '' || $('#patientClassIdPopup').val() == null || $('#patientClassIdPopup').val() == undefined) && ($('#patientClassCancelReasonIdPopup').val() == '' || $('#patientClassCancelReasonIdPopup').val() == null || $('#patientClassCancelReasonIdPopup').val() == undefined))
	{
		

			growl.warning("Please select patient class and reason.",{
				title : WARNING_MSG
			});
			return false;
	
	}
  else if($('#patientClassIdPopup').val() == '' || $('#patientClassIdPopup').val() == null || $('#patientClassIdPopup').val() == undefined){
			
			growl.warning("Please select patient class.",{
				title : WARNING_MSG
			});
			return false;
		}else if($('#patientClassCancelReasonIdPopup').val() == '' || $('#patientClassCancelReasonIdPopup').val() == null || $('#patientClassCancelReasonIdPopup').val() == undefined){
			
			growl.warning("Please select reason.",{
				title : WARNING_MSG
			});
			return false;
			
		}else{
			$rootScope.startLoader();
			$scope.patientClassIdPopup = $('#patientClassIdPopup').val();
			$scope.patientClassCancelReasonIdPopup = $('#patientClassCancelReasonIdPopup').val();
			var visId = 0;
			if($scope.visitTypeId == 1){
			 $scope.encounterDetails.patientClassId =$scope.patientClassIdPopup;
			 visId = $scope.encounterDetails.encounterId;
			}else{
				 $scope.admissionDetails.patientClassId =$scope.patientClassIdPopup;
				 visId = $scope.admissionDetails.admissionId;
			}
			var obj={
					visitId:visId,
					visitTypeId : $scope.visitTypeId,
					reasonIdForChangePatientClass : $scope.patientClassCancelReasonIdPopup,
					organizationId : $scope.organizationId,
					unitId : $scope.unitId,
					patientClassId:$scope.patientClassIdPopup
			}
			URI = BASE_URL + ROOT_URL + "/api/billing/changePatientClassId";
			BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
					{	
						if(response.data.status == "success")
						{
							$rootScope.stopLoader();
							angular.element('#change_patient_class').modal('hide');
							var i = 1;
				    		angular.forEach($scope.autoRenderedServicesFinalList, function (service, key) 
								{
				    					
				    						$scope.autoRenderedServicesFinalList[key].concession = parseFloat(0);
				   						    $scope.autoRenderedServicesFinalList[key].patientClassPercentage = parseFloat(0);
				   						   // $scope.autoRenderedServicesFinalList[key].discount = 0;
				   						    $scope.autoRenderedServicesFinalList[key].discount = service.discount;
				   						    
					    			        var totalAmt = (parseFloat(service.quantity) * parseFloat(service.basePrice)).toFixed(2);
					    			       
					    			       // $scope.autoRenderedServicesFinalList[key].discountPercentage = 0;
					    			        $scope.autoRenderedServicesFinalList[key].discountPercentage = service.discountPercentage;
					    			        var withConDic =parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[key].discount);
					    			        
					    			    	var txpre = 0;
											if(service.taxPercentage != null || service.taxPercentage > 0){
												$scope.autoRenderedServicesFinalList[key].taxPercentage = service.taxPercentage;
												txpre = service.taxPercentage;
											}else{
													$scope.autoRenderedServicesFinalList[key].taxPercentage = 0;
													}
											$scope.autoRenderedServicesFinalList[key].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
											$scope.autoRenderedServicesFinalList[key].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[key].taxAmt)).toFixed(2);
											$scope.autoRenderedServicesFinalList[key].selfPayable = $scope.autoRenderedServicesFinalList[key].netPay;
											$scope.autoRenderedServicesFinalList[key].creditPayable =parseFloat(0);
				    					
									    
										
										if(i == $scope.autoRenderedServicesFinalList.length){
											//$scope.saveOrderPackage();
											if($scope.requestStatusObject == null){
												$scope.saveOrderPackage();
												$scope.requestDiscountDiv = false;
												$scope.requestApporivedDiscountDiv = false;
												$scope.discountDisabledFlagForRequest = false;
												$scope.requestAppUser = 0;
												$scope.approvedAmtApp = 0;
												$scope.requestAmtApp =0;
												$scope.discTypeDesc = "";
												$scope.statusApp = "";
												$scope.discountRadio = 2; 
												
												$scope.billWiseDiscountObj = {
														// "discountGivenBy":"",
														"discountGivenBy":$scope.userId,
														"discountCategoryId" : "",
														"discountAmountBillWise" : "",
														"discountPercentageBillWise" : "",
														"approvedAmountBillWise":"",
														"requestUserId":"",
														"requestAmountBillWise" : 0,
												}
												 $scope.requestStatusObject  = null;
											}else{
														$scope.getCancelDiscountReqForDiscount();
														$scope.saveOrderPackage();
												
											}
										}
										i++;
								});
				    		
						}
					});
			
		}
	}
	
		$scope.getListPackageOrder = function() {
		    if($scope.patientDetails !== undefined && $scope.patientDetails !== null){
                if($scope.patientDetails.patientId === undefined || $scope.patientDetails.patientId === null || $scope.patientDetails.patientId ===''){
                    $scope.patientDetails.patientId = 0;
                }
            }
            else{
                $scope.patientDetails.patientId = 0;
            }
			var URI = BASE_URL+ ROOT_URL
					+ "/api/packages/getPackageListForIPDPackageOrder/"+$scope.patientDetails.patientId+"/"+ $scope.encounterDetails.encounterId
					+ "/" + $scope.organizationId + "/"+ $scope.unitId+"/"+$scope.visitTypeId;
		
			GenericService.serviceAction("GET", URI).then(function(response) {
								$rootScope.stopLoader();
								$scope.listPackageOrder = [];
								$scope.listPackageOrder = response.data.listObject !=null ? response.data.listObject : []  ;
								console.log("ListPackageOrderresult",$scope.listPackageOrder);
		
							});
		}


	$scope.getAutoRenderedServicesByPatient=function(comonObj)
	{
		
		$rootScope.startLoader();
		$scope.autoRenderedServicesBillingDetails=[];
       var URI = null;
		if($scope.isPreOrPostBilling=='Y')
			URI = BASE_URL + ROOT_URL + "/api/billing/service/preBilling/orderedServices";
		else
			URI = BASE_URL + ROOT_URL + "/api/billing/serviceMaster/orderedServices";
		var obj={
				//"unitId":$scope.unitId,
				//"organizationId":$scope.organizationId,
				//"patientId":encounterObj.patientId,
				 "visitTypeId":$scope.visitTypeId,
				//"bedCategoryId":1, //By default its OPD ---> Id==1
				//"patientTypeId":1, //need to add after patient registration 
				//"paymentEntitlementTypeId":encounterObj.paymentEntitlementTypeId,
				//"doctorId":encounterObj.doctorId,
				//"defaultSelfTariffId":$scope.defaultSelfTariffId,
				"debitNoteService":true,
				"encounterId":comonObj.encounterId
				
		}
		console.log("AUTORENDER",obj);
		//return false;
		BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			if(response.data.status == "success")
			{
				var tempAutoRendServList = [];
				/*angular.forEach(response.data.listObject,function(value,index){
					if(value.isPackageService == 'Y'){
						value.apportionedPrice = value.basePrice;
						value.basePrice = 0;
						value.netPay = 0;
						value.totalAmt = 0;
						value.selfPayable = 0;
					}
					value.packageMasterId = value.packageId;
					tempAutoRendServList.push(value);
				});*/
				angular.forEach(response.data.listObject,function(value,index){
					value.packageMasterId = value.packageId;
					tempAutoRendServList.push(value);
				});
				  $scope.autoRenderedServicesList = tempAutoRendServList;
				  
				  
				  angular.element('#addServicePopup').modal('hide');
				  angular.element('#serviceCancellationModal').modal('hide');
				  angular.element('#packageForceAdd').modal('hide');
				  $rootScope.stopLoader();
				  $rootScope.stopLoaderPopup();
				  
				$scope.calculationTest($scope.autoRenderedServicesList,init);
			}else if(response.data.status == "error")
			{
				  $rootScope.stopLoader();
				  $rootScope.stopLoaderPopup();
			}
		});
		$scope.getListPackageOrder();
	}
	
	 $scope.createBillingDetailsObj = function(item){
	    	var obj = {
	    		"billingDetailsId"	:"",
	    		"billingMasterId"	:"",
	    		"serviceId"			:item.serviceMasterId,
	    		"rate"				:"",
	    		"quantity"			:item.quantity,
	    		"concession"		:item.concession,
	    		"concessionAmount"	:"",
	    		"grossAmount"		:(item.basePrice * item.quantity) - item.concession,
	    		"coPayPercentage"	:"",
	    		"unitId"			:$scope.unitId,
	    		"organizationId"	:$scope.organizationId,
	    		"status"			:"A",
	    		"createdBy"			:$scope.userId,
	    		"updatedBy"			:$scope.userId,
	    		"createdDate"		:new Date().getTime(),
	    		"updatedDate"		:new Date().getTime(),
	    		"serviceMasterId"	:item.serviceMasterId,
		    	"serviceStandardName":item.serviceStandardName,
		    	"serviceStandardCode":item.serviceStandardCode,
		    	"specialityId"		:item.specialityId,
		    	"specialityName"	:item.specialityName,
		    	"subSpecialityId"	:item.subSpecialityId,
		    	"subSpecialityName"	:item.subSpecialityName,
		    	"basePrice"			:item.basePrice,
		    	"discount"			:(item.discount == undefined || item.discount == '') ? '0' : item.discount,
		    	"discountPercentage":item.discountPercentage, // direct discount percentage
		    	"discountAmount"	:item.discountAmount, // direct discount amount
		    	"orderDetailsId"	:item.orderDetailsId
	    	}
	    	return obj;
	    }
	
	$scope.search="";
	/*$scope.searchServices = function(searchKeyword){
		$scope.servicesList=[];
		searchKeyword=searchKeyword+"---"+$scope.patientDetails.genderId;
		
		var serData = {
				organizationId : $scope.organizationId,
				unitId : $scope.unitId,
				searchKeyword : searchKeyword,
				
				defaultSelfTariffId : $scope.defaultSelfTariffId,
				visitTypeId : $scope.visitTypeId
		}
		if($scope.visitTypeId==1)
		{
			serData.billingBedCategoryId = $scope.opdBillingCatId;
			serData.patientCategoryId = $scope.patientDetails.patientTypeId;
			serData.paymentEntitlementTypeId = $scope.encounterDetails.paymentEntitlementTypeId;
			serData.encounterId = $scope.encounterDetails.encounterId;
		}else{
			serData.billingBedCategoryId = $scope.admissionDetails.bedBillingCategoryId;
			serData.patientCategoryId = $scope.admissionDetails.patientCategoryId;
			serData.paymentEntitlementTypeId = $scope.admissionDetails.paymentEntitlementId;
			serData.admissionId = $scope.admissionDetails.admissionId;
		}
		
		var URI = BASE_URL + ROOT_URL + "/api/unit/tariff/serviceMaster";
		
		//var URI = BASE_URL + ROOT_URL + "/global/serviceMaster/"+searchKeyword+"/unit/"+$scope.unitId+"/org/"+$scope.organizationId;
		return BillingGenericService.serviceAction(METHOD_POST,URI,serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			if(response.data.status == "success"){
				$scope.servicesList = response.data.listObject;
			}
			return $scope.servicesList;
		});
	}*/
	
	$scope.searchServices = function(searchKeyword){
		$scope.servicesList=[];
		searchKeyword=searchKeyword+"---"+$scope.patientDetails.genderId;
		var serData = {
				organizationId : $scope.organizationId,
				unitId : $scope.unitId,
				searchKeyword : searchKeyword
		}
		//var URI = BASE_URL + ROOT_URL + "/global/serviceMaster/"+searchKeyword+"/unit/"+$scope.unitId+"/org/"+$scope.organizationId;
		var URI = BASE_URL + ROOT_URL + "/global/serviceMaster";
		return BillingGenericService.serviceAction(METHOD_POST,URI, serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			if(response.data.status == "success"){
				$scope.servicesList = response.data.listObject;
			}
			return $scope.servicesList;
		});
	}
	
	
    var isExists;
    function checkExistsOrNot(arr, val) {
     return arr
       .some(function(arr) {
        return (val.serviceMasterId === arr.serviceMasterId)
       });
    }
    
    
    //Discount Request
	$scope.newRequestCalculates = function(){
		var totalSummary=0;
		var totalAuthSummary=0;
		angular.forEach($scope.addNewRequestList,function(value,index){
			totalSummary = parseFloat(totalSummary) + (isNaN(value.requestAmount) ? 0 : parseFloat(value.requestAmount));
			totalAuthSummary = parseFloat(totalAuthSummary) + (isNaN(value.authorizationAmount) ? 0 : parseFloat(value.authorizationAmount));
		});
		$scope.summary = $rootScope.parseFloatNum(totalSummary);
		$scope.authsummary = $rootScope.parseFloatNum(totalAuthSummary);
		$scope.authsummarySplitBill = $scope.authsummary;
  }
   
    $scope.calculationTest = function(autoRenderedServicesList,typeId)
    {
    	console.log("AUTO",autoRenderedServicesList);
    	$scope.AutoRenderedTotalAmountSummary = 0;
		$scope.AutoRenderedTotalConcessionSummary = 0;
		$scope.AutoRenderednetPayableSummary = 0;
		$scope.AutoRenderednetTotalDiscountSummary=0;
		$scope.AutoRenderednetTotalTax=0;
		$scope.AutoRenderednetTotalDiscount=0;
		$scope.AutoRenderednetTotalCreditPayble=0;
		$scope.AutoRenderednetTotalSelfPayble=0;
		$scope.AutoRenderednetTotalPackagePayble=0;
		$scope.totalSelfPaybleTemp = 0;
		$scope.roundOffAmt = 0;
		$scope.totalDisServiceWise = 0;
		$scope.totalAmountWithDiscountApplicable = 0;
		$scope.totalDiscountApp=0;
		$scope.totalPackagePaybleTemp = 0;
		
		//-----calculate amounts
		//console.log(autoRenderedServicesList);
		//return false;
		
		$scope.autoRenderedServicesFinalList = [];
        var mainCount = 1;
        
        if(autoRenderedServicesList.length == 0){
        	 $scope.getDisRiqFlag = false;
        	 $scope.requestStatusObject = null;
        }
        
		angular.forEach(autoRenderedServicesList, function (value, key) 
				{
			var newArr = {};
			angular.forEach(value.orderDetailsPackageMapperDtosList, function (value1, key) 
					{
					console.log(value.basePrice, "#", value.quantity);
					value1.serviceAmount = parseFloat(value1.serviceAmount).toFixed(2);
					newArr.orderDetailsId = value.orderDetailsId > 0 ? value.orderDetailsId : null;
					newArr.tariffId = value.tariffId > 0 ? value.tariffId : $scope.defaultSelfTariffId;
					newArr.isInvestigationOrder = (value.isInvestigationOrder=='Y')?'Y':'N';
					newArr.orderId = value.orderId > 0 ? value.orderId : null;
					newArr.orderDate = value.orderDate != null ? value.orderDate : '';
					newArr.orderNo = value.orderNo != null ? value.orderNo : '';
                    newArr.bedCategoryId = value.bedCategoryId;
                    newArr.isRateEditable = value.isRateEditable;
                    if(value.isDoctorRequired == null || value.isDoctorRequired == undefined)
                    {
                       newArr.isDoctorRequired  = 'N';
                    }
                else
                    {
                      newArr.isDoctorRequired  = value.isDoctorRequired;
                    }
                    
                    newArr.docSharePer = value.docSharePer > 0 ? value.docSharePer : 0;
                    newArr.hospitalShare = value.hospitalShare > 0 ? value.hospitalShare : 0;
                    newArr.refDocSharePer = value.refDocSharePer > 0 ? value.refDocSharePer : 0;
                    newArr.discountOwnership = value.discountOwnership > 0 ? value.discountOwnership : 0;


                    newArr.isConsultationService = value.isConsultationService;
					newArr.dummyServiceAmt = value.dummyServiceAmt;
					newArr.priorityId  = value.priorityId;
					newArr.isOutsourced = value.isOutsourced;
					newArr.specialityId = value.specialityId;
					newArr.taxId = value.taxId;
					newArr.ordCancelled = value.ordCancelled != null ? value.ordCancelled : 'N';
					newArr.ordCancelReasonId = value.ordCancelReasonId != null ? value.ordCancelReasonId  : '';
					newArr.ordCancelDatetime = value.ordCancelDatetime != null ? value.ordCancelDatetime  : '';
					newArr.ordCancelRemark = value.ordCancelRemark != null ? value.ordCancelRemark  : '';
					newArr.patientClassPercentage = value.patientClassPercentage != null ? value.patientClassPercentage  : null;
	        		newArr.markupStatus =value.markupStatus != null ? value.markupStatus  : false;
					newArr.status  = value.status;
					newArr.isAutoRender =  value.isAutoRender;
					newArr.serviceRendered = value.serviceRendered != null ? value.serviceRendered  : '0';
					newArr.oldOrdDtlId = value.oldOrdDtlId != null ? value.oldOrdDtlId  : null;
					newArr.isRateEditable = (value.isRateEditable == 'Y')?'Y':'N';	
					newArr.isRateEditable = (value.packageConsumptionAmt>0 && newArr.isRateEditable=='Y')?'N':value.isRateEditable;	
					newArr.isRateEditable =value.isPackage == true?'N':newArr.isRateEditable;
                    newArr.doctorSpecialityId = ((value.doctorSpecialityId != null || value.doctorSpecialityId > 0) ? value.doctorSpecialityId  :  value.specialityId);
                    newArr.docList=$scope.doctorsList;
					newArr.doctorSpecialityList = (value.doctorSpecialityList != undefined ? value.doctorSpecialityList : []);
					newArr.isRefDoctorShare = value.isRefDoctorShare != null ? value.isRefDoctorShare  : null;
					newArr.contractId = ((value.contractId != null || value.contractId > 0) ? value.contractId  : null);
					newArr.doctorId = value.doctorId==null?0:value.doctorId;
					newArr.serviceSpecialityId = value.serviceSpecialityId;
					newArr.serviceSubSpecialityId = value.serviceSubSpecialityId;
					newArr.serviceRendered = value.serviceRendered;
					newArr.serviceRenderingDeptId = value.serviceRenderingDeptId;
					newArr.oldNetPay = value.oldNetPay;
					newArr.discountDetailsList = value.discountDetailsList; 
					newArr.serviceMasterId = value.serviceMasterId;
					newArr.serviceStandardCode = value.serviceStandardCode;
					newArr.serviceStandardName = value.serviceStandardName;
                    newArr.discountCategoryId = value.discountCategoryId != null ? value.discountCategoryId.toString()  : null;
					newArr.procedureOrderMstId = value.procedureOrderMstId > 0 ? value.procedureOrderMstId  : null;
					newArr.isProcedureService = value.isProcedureService!=null ? value.isProcedureService  : 'N';
					newArr.isPostedWhileDischarge = value.isPostedWhileDischarge!=null ? value.isPostedWhileDischarge : 'N';
					newArr.isFromAddservice = (value.isFromAddservice != null || value.isFromAddservice != '' || value.isFromAddservice != undefined ? value.isFromAddservice :'N') ;
					/*newArr.isDiscountApplicable = (value1.isPackageService=='N')?value.isDiscountApplicable:'N'; // isDiscountApplicable
					if($scope.visitTypeId == 1){
					newArr.isDiscountApplicable = ($scope.encounterDetails.isTariffRateApplicable == 'N' ?'N':	newArr.isDiscountApplicable);
					}else{
						newArr.isDiscountApplicable = ($scope.admissionDetails.isTariffRateApplicable == 'N' ?'N':	newArr.isDiscountApplicable);
					}
					newArr.isDiscountApplicable = ( value.packageConsumptionAmt>0 ?'N':	newArr.isDiscountApplicable);
					*/
					if($scope.visitTypeId != 1){
					newArr.isDiscountApplicable = (value.packageConsumptionAmt > 0) ? 'N':value.isDiscountApplicable; // isDiscountApplicable
					if(value.packageConsumptionAmt>0 ){
						//newArr.isDiscountApplicable = 'N';
						newArr.isRateEditable = 'N';
					}else{
						//newArr.isDiscountApplicable = newArr.isDiscountApplicable;
						newArr.isRateEditable = newArr.isRateEditable;
					}
					}else{
						newArr.isDiscountApplicable = (value.packageConsumptionAmt > 0) ? 'N':value.isDiscountApplicable; // isDiscountApplicable
						if(value.packageConsumptionAmt>0 ){
							//newArr.isDiscountApplicable = 'N';
							newArr.isRateEditable = 'N';
						}else{
							//newArr.isDiscountApplicable = newArr.isDiscountApplicable;
							newArr.isRateEditable = newArr.isRateEditable;
						}
					}
					
					
					newArr.isQuantityEditable = value.isQuantityEditable;
					newArr.minRateEditable = value.minRateEditable;
					newArr.maxRateEditable = value.maxRateEditable;
					newArr.specialityName = value.specialityName;
					newArr.doctorName = value.doctorName;
					newArr.discountDisabledFlag = 'N';
					newArr.isPackage = value.isPackage;
					newArr.packageMasterId = value.packageMasterId;
					newArr.packageTypeId = value.packageTypeId;
					newArr.isPackageService = value.isPackageService;
					newArr.apportionedPrice = typeof(value.apportionedPrice) != "undefined" ? value.apportionedPrice : null;
					newArr.isCapService = value.isCapService;
	    			newArr.packageConsumptionAmt = value.packageConsumptionAmt;
	    			if(value.subSpecialityId==undefined||value.subSpecialityId==null)
    				{
    				  newArr.subSpecialityId = value.serviceRenderingDeptId;
    				}
	    			else{
    			     newArr.subSpecialityId = value.subSpecialityId;
    			    }
	    			newArr.packageId = value.packageId;
					if(value.specialityName == null)
					{
						newArr.specialityName = "";
						newArr.doctorName = "";
					}
					newArr.quantity = value.quantity;
					newArr.basePrice = parseFloat(value.basePrice).toFixed(2);
					newArr.basePriceOld = value.basePrice;
					
					 newArr.basePriceRateChange = parseFloat(value.basePrice).toFixed(2);
                     if(parseFloat(value.packageConsumptionAmt) > 0 && value1.isPackageService == 'N'){
                         newArr.basePriceRateChange = parseFloat(value1.serviceAmount).toFixed(2);
                     }

                    newArr.oldBasePrice = value.oldBasePrice;
					newArr.totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
					newArr.concession = parseFloat(value.concession).toFixed(2);
					newArr.patientClassPercentage = parseFloat(value.patientClassPercentage).toFixed(2);
					newArr.patientClassPercentage = value.patientClassPercentage != null ? parseFloat(value.patientClassPercentage).toFixed(2)  : null;
//					newArr.patientClassPercentageChange = parseFloat(value.patientClassPercentage).toFixed(2);
				//	newArr.discountCategoryId = '';
					newArr.discountPercentage = value.discountPercentage;
					newArr.discount = value.discount;
					var dis = parseFloat(value.discount).toFixed(2);
					var disPer =value.discountPercentage;
					if(value.discount == null || value.discount == 0 )
						{
							newArr.discountPercentage = 0;
							newArr.discount = 0;
							dis = 0;
							disPer = 0;
							newArr.discountDisabledFlag = 'Y';
							
						}
					
					if(value.discount > 0 && (value.discountPercentage == null || value.discountPercentage == 0 || value.discountPercentage == '')){
						//newArr.discountPercentage = isNaN(((parseFloat(value.discount)/(parseFloat(value.quantity) * parseFloat(value1.serviceAmount)))*100).toFixed(2)) ? parseFloat(0).toFixed(2) : ((parseFloat(value.discount)/(parseFloat(value.quantity) * parseFloat(value1.serviceAmount)))*100).toFixed(2);
                        newArr.discountPercentage = isNaN(((parseFloat(value.discount)/(parseFloat(value.quantity) * parseFloat(value1.serviceAmount) - parseFloat(value.concession)))*100).toFixed(2)) ? parseFloat(0).toFixed(2) : ((parseFloat(value.discount)/(parseFloat(value.quantity) * parseFloat(value1.serviceAmount) - parseFloat(value.concession)))*100).toFixed(2);

					}
						if(value.discountDetailsList == null)
						{
							value.discountDetailsList = [];
							newArr.discountDetailsList = [];
						}
					newArr.taxName = value.taxName;
					var txpre = 0;
					if(value.taxPercentage != null || value.taxPercentage > 0){
						newArr.taxPercentage = value.taxPercentage;
						txpre = value.taxPercentage;
					}
					else
						{
						 newArr.taxPercentage = 0;
						}
					var totalAmt = (parseFloat(value.quantity) * parseFloat(value1.serviceAmount)).toFixed(2);
					//var concession = (value.packageConsumptionAmt==0 ?parseFloat(value.concession).toFixed(2):	parseFloat(0));  
					var concession = (parseFloat(value.concession).toFixed(2));
					var withConDic =parseFloat(totalAmt - (parseFloat(concession) + parseFloat(dis))).toFixed(2);
					if(newArr.isDiscountApplicable == 'Y' && value1.isPackageService == 'N' &&  value.status == 'A')
                  	{
                  		$scope.totalDisServiceWise  = parseFloat($scope.totalDisServiceWise) + ((parseFloat(1) * parseFloat(value.basePrice)));
                  	}
					newArr.taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
					var gstAmt =  parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
					newArr.taxAmtOld = value.taxAmtOld;
					newArr.selfPayableOldC = value.selfPayableOldC;
					newArr.creditPayableOldC = value.creditPayableOldC;
					newArr.netPay =   parseFloat(value.netPay).toFixed(2);
		
					var selfPayableV =   "0";
					var creditPayableV =   "0";
					if(value.payeeId == $scope.selfPayeeId || value.payeeId == null )
						{
						      newArr.selfPayable =parseFloat(value.selfPayable).toFixed(2);;
						      newArr.creditPayable =parseFloat(value.creditPayable).toFixed(2);;
						      selfPayableV =  parseFloat(newArr.selfPayable).toFixed(2);
						      creditPayableV =  parseFloat(newArr.creditPayable).toFixed(2);
						     }
					
					else
						{
						newArr.payeeId= value.payeeId;
						    if(value.selfPayable>0)
							{
						    	
						    	if(parseInt(dis) > 0 )
						    		{
						    		newArr.selfPayable =   parseFloat(value.selfPayable).toFixed(2);
						            selfPayableV =   parseFloat(value.selfPayable).toFixed(2);
						    		}
						    	else
						    		{
						    		 newArr.selfPayable =parseFloat(value.selfPayableOldC).toFixed(2);
									 selfPayableV =   parseFloat(value.selfPayableOldC).toFixed(2);
						    		}
						    
							
							newArr.creditPayable =parseFloat(value.creditPayable).toFixed(2);
							creditPayableV=parseFloat(value.creditPayable).toFixed(2);
							}
						else{
							newArr.selfPayable = (value.selfPayable==null||value.selfPayable==undefined||value.selfPayable=="")?0:value.selfPayable;
							selfPayableV =   parseFloat(value.selfPayable).toFixed(2);
						   
						    newArr.creditPayable  = value.creditPayable;
						    creditPayableV = parseFloat(value.creditPayable).toFixed(2);
						    
						   }
						
						}
				
					
				
						if($scope.surchargeServiceId !=  value.serviceMasterId){
					   if(Object.keys(value.orderDetailsPayeeMapperDtosList).length > 0)
				    	 {
				    		angular.forEach(value.orderDetailsPayeeMapperDtosList, function (value1, key) {
								
				    			 if(value1.payeeId == $scope.selfPayeeId)
							       {
				    				 value1.payeeAmount =  newArr.selfPayable;
				    				 value1.selfPayable =  newArr.selfPayable;
				    				 value1.creditPayable = 0;
				    			
							       }
				    			 else{
				    				 if($scope.isSplitBillApplied)
				    					 {
				    					  value1.creditPayable =  value1.creditPayable;
					    				  value1.selfPayable =  value1.selfPayable;
				    					 }
				    				 else{
				    				     value1.creditPayable =  value1.payeeAmount;
				    				     value1.selfPayable =  0;
				    				 }
				    			 }
								
							});
				    	 }
						}
				newArr.saleId = value.saleId;		
			    newArr.orderDetailsPayeeMapperDtosList = value.orderDetailsPayeeMapperDtosList;
			    newArr.orderDetailsPackageMapperDtosList = value.orderDetailsPackageMapperDtosList;
			    
			    //package force add
			    newArr.forceAddRemoved = value.forceAddRemoved;
			    newArr.isConsumed = value.isConsumed;
			    
			    var newPatAmt = parseFloat(parseFloat(newArr.netPay)).toFixed(2);
					if(value1.isPackageService=='N' && value.status=='A')
					{
						$scope.AutoRenderedTotalAmountSummary=(parseFloat($scope.AutoRenderedTotalAmountSummary) + parseFloat(value.quantity) * parseFloat(value1.serviceAmount)).toFixed(2);
						$scope.AutoRenderedTotalConcessionSummary=(parseFloat($scope.AutoRenderedTotalConcessionSummary) +(parseFloat(value.concession))).toFixed(2);
						$scope.AutoRenderednetPayableSummary=(parseFloat($scope.AutoRenderednetPayableSummary) + parseFloat(newPatAmt)).toFixed(2);
						$scope.AutoRenderednetTotalDiscountSummary=(parseFloat($scope.AutoRenderednetTotalDiscountSummary) + parseFloat(0)).toFixed(2);
						$scope.autoRenderedServicesBillingDetails.push($scope.createBillingDetailsObj(value));
						//$scope.AutoRenderednetTotalTax = (parseFloat($scope.AutoRenderednetTotalTax) +  (value.packageConsumptionAmt==0?parseFloat(gstAmt):0)).toFixed(2);
						$scope.AutoRenderednetTotalTax = (parseFloat($scope.AutoRenderednetTotalTax) +  (parseFloat(gstAmt))).toFixed(2);
						$scope.AutoRenderednetTotalDiscount = (parseFloat($scope.AutoRenderednetTotalDiscount) + parseFloat(dis)).toFixed(2);
						$scope.AutoRenderednetTotalCreditPayble=(parseFloat($scope.AutoRenderednetTotalCreditPayble) + parseFloat(creditPayableV)).toFixed(2);
						$scope.AutoRenderednetTotalSelfPayble=(parseFloat($scope.AutoRenderednetTotalSelfPayble) + parseFloat(selfPayableV)).toFixed(2);
						$scope.AutoRenderednetTotalPackagePayble= (parseFloat($scope.AutoRenderednetTotalPackagePayble)+ parseFloat(value.packageConsumptionAmt)).toFixed(2);
					}
					
					newArr.discountRateChange = parseFloat(newArr.discount).toFixed(2);
					newArr.discountPercentageRateChange = parseFloat(newArr.discountPercentage).toFixed(2);
					newArr.conccesionPriceRateChange = parseFloat(newArr.concession).toFixed(2);
					newArr.patClsPerPriceRateChange = parseFloat(newArr.patientClassPercentage).toFixed(2);
					newArr.createdBy = value.userId;
					newArr.createdDate = value.createdDate;
					
					if(value.isDiscountApplicable=='Y')
						{
					    	$scope.totalAmountWithDiscountApplicable = parseFloat($scope.totalAmountWithDiscountApplicable)+ (parseFloat(value.quantity) * parseFloat(value.basePrice) - (parseFloat(value.concession)));
					    	$scope.totalDiscountApp = parseFloat($scope.totalDiscountApp)+(parseFloat(newArr.discount));
						}
		 });// end of loop
			$scope.autoRenderedServicesFinalList.push(newArr);
			
            if($scope.getDisRiqFlag == true && autoRenderedServicesList.length == mainCount){
                $scope.getDisRiqFlag = false;
                $scope.requestStatusObject = {};
                   var data1 = {
                           admissionId : $scope.visitTypeId != 1?$scope.admissionDetails.admissionId : $scope.encounterDetails.admissionId,
                           requestType : 4
                   }
                   var URI1 = BASE_URL + ROOT_URL + "/api/billing/getDiscReqDtlsForOPBilling";
                   BillingGenericService.serviceAction(METHOD_POST,URI1, data1, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
                       {
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
                               $scope.discTypeDesc = $scope.requestStatusObject.discTypeDesc;
                               $scope.statusApp = "Approved.";
                               $scope.discountRadio = 2; 
                               
                               
                               var totalDisPercentage =((parseFloat($scope.requestStatusObject.approvedDiscAmt)/$scope.requestStatusObject.netAmount)*100).toFixed(2);
                               if(totalDisPercentage > 0){
                               $scope.billWiseDiscountObj.discountPercentageBillWise = totalDisPercentage;
                               $scope.billWiseDiscountObj.discountPercentageBillWiseOld = (parseFloat($scope.requestStatusObject.approvedDiscAmt)/$scope.requestStatusObject.netAmount)*100;
                               $scope.billWiseDiscountObj.discountAmountBillWise = parseFloat($scope.requestStatusObject.approvedDiscAmt).toFixed(2);
                               $scope.billWiseDiscountObj.discountCategoryId =  $scope.requestStatusObject.discType.toString();
                               }
                               }
                           else if($scope.requestStatusObject.reqStatus == 1)
                               {
                                   $scope.discountDisabledFlagForRequest = true;
                                   $scope.requestApporivedDiscountDiv = true;
                                   $scope.requestAppUser = $scope.requestStatusObject.requestSentToEmployee;
                                   $scope.approvedAmtApp = $scope.requestStatusObject.approvedDiscAmt;
                                   $scope.requestAmtApp =$scope.requestStatusObject.reqDisAmt;
                                   $scope.statusApp = "Pending";
                                   $scope.discTypeDesc = $scope.requestStatusObject.discTypeDesc;
                                   $scope.discountRadio = 2;
                               }
                           else if($scope.requestStatusObject.reqStatus == 3)
                               {
                                $scope.requestStatusObject  = null;
                                
                                var reject =0;
                                  angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
                                      if(value.status == 'A'){
                                          
                                          if(parseFloat(value.discount) > 0){
                                              
                                                  $scope.autoRenderedServicesFinalList[reject].concession = parseFloat(0);
                                                  $scope.autoRenderedServicesFinalList[reject].patientClassPercentage = parseFloat(0);
                                                  
                                               var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
                                               $scope.autoRenderedServicesFinalList[reject].discountPercentage = 0;
                                               $scope.autoRenderedServicesFinalList[reject].discount = parseFloat(0).toFixed(2);
                                               var withConDic =parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[reject].discount);
                                               
                                               var txpre = 0;
                                               if(value.taxPercentage != null || value.taxPercentage > 0){
                                                   $scope.autoRenderedServicesFinalList[reject].taxPercentage = value.taxPercentage;
                                                   txpre = value.taxPercentage;
                                               }else{
                                                       $scope.autoRenderedServicesFinalList[reject].taxPercentage = 0;
                                                       }
                                               $scope.autoRenderedServicesFinalList[reject].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
                                               $scope.autoRenderedServicesFinalList[reject].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[reject].taxAmt)).toFixed(2);
                                               $scope.autoRenderedServicesFinalList[reject].selfPayable = $scope.autoRenderedServicesFinalList[reject].netPay;
                                               $scope.autoRenderedServicesFinalList[reject].creditPayable =parseFloat(0);
                                       
                                              }
                                          }
                                      if(reject + 1 == $scope.autoRenderedServicesFinalList.length){
                                          $scope.saveOrderPackage();
                                      }
                                          
                                      reject++;
                                      });
                                  
                               }
                           else{
                        	   
                        	   $scope.requestStatusObject  = null;
                           }
                           
                           $scope.totalSumDisAmountTemp = parseFloat(0);
                           if($scope.requestStatusObject != null){
                           var i = 1;
                           angular.forEach($scope.requestStatusObject.requestDiscountDetailsDtosList,function(value,index)
                                    {
                                       
                                              $scope.totalSumDisAmountTemp =$scope.totalSumDisAmountTemp + value.netAmount;
                                           
                                      
                                       i++;    
                                    });
                           }
                           
                           
                           }
                           
                           else{
                               $scope.requestStatusObject = null;
                               }
                           }
                   });
           }
           mainCount++;

	 });//end package loop
		$scope.totalAmountSummary=(parseFloat($scope.AutoRenderedTotalAmountSummary)).toFixed(2);
		$scope.totalConcessionSummary=(parseFloat($scope.AutoRenderedTotalConcessionSummary)).toFixed(2);
		$scope.netPayableSummary=(parseFloat($scope.AutoRenderednetPayableSummary)).toFixed(2);
		$scope.totalDiscountSummary=(parseFloat($scope.AutoRenderednetTotalDiscount)).toFixed(2);
		$scope.totalGstTaxSummary=(parseFloat($scope.AutoRenderednetTotalTax)).toFixed(2);
		
		$scope.totalCreditPayble=(parseFloat($scope.AutoRenderednetTotalCreditPayble)).toFixed(2);

		var amt =   isNaN(Number(parseFloat($scope.billWiseDiscountObj.discountAmountBillWise)))?0:$scope.billWiseDiscountObj.discountAmountBillWise;
		if(amt>0 && $scope.AutoRenderednetTotalDiscount>0 && $scope.isBillWiseDiscountApplied == true){
		  $scope.roundOffAmt = parseFloat(amt-$scope.AutoRenderednetTotalDiscount).toFixed(2);
		}	
		$scope.totalCreditPayble=(parseFloat($scope.AutoRenderednetTotalCreditPayble)).toFixed(2);
		$scope.totalSelfPayble=(parseFloat($scope.AutoRenderednetTotalSelfPayble)).toFixed(2);
		$scope.totalSelfPaybleTemp=(parseFloat($scope.AutoRenderednetTotalSelfPayble)).toFixed(2);
		$scope.totalSelfPayble = parseFloat(Number(parseFloat($scope.totalSelfPayble).toFixed(2))+parseFloat($scope.roundOffAmt)).toFixed(2);
		$scope.totalPackagePaybleTemp=(parseFloat($scope.AutoRenderednetTotalPackagePayble)).toFixed(2);
		
		$scope.totalAmountWithDiscountApplicable = Number(parseFloat($scope.totalAmountWithDiscountApplicable).toFixed(2));
		if($scope.autoRenderedServicesFinalList.length>0)
		{
			$("#savedisablebtn").removeClass("disabled");
			$("#savedisablebtn").addClass("enabled");
		}
		else{
			$("#savedisablebtn").removeClass("enabled");
			$("#savedisablebtn").addClass("disabled");
		}
		return 1;
    }
    
    $scope.getQuantityCal = function(itemObj,idx)
    {
    	//alert(idx);
    	 var autoRenderedServicesList = $scope.autoRenderedServicesFinalList;
    	 $scope.calculationTest(autoRenderedServicesList,quantity);
    }

    $scope.getBasePriceCal = function(itemObj,idx)
    {
    	
    	console.log("OBJ",itemObj);
    	if(itemObj.basePrice == '' || itemObj.basePrice == undefined)
		{
		  itemObj.basePrice = 0;
		}
        if(parseFloat(itemObj.basePrice) != parseFloat(itemObj.basePriceRateChange))
        {

    	/*var basepriceNew = parseFloat(itemObj.basePrice);
    	var oldBasePrice = parseFloat(itemObj.oldBasePrice);
    	var basePriceOld = parseFloat(itemObj.dummyServiceAmt!=null ? itemObj.dummyServiceAmt : itemObj.basePrice);
    	
    	var minRate = itemObj.minRateEditable;
    	var maxRate = itemObj.maxRateEditable;
    	
    	var minRatePerAmount = ((basePriceOld*minRate)/100).toFixed(2);
    	var maxRatePerAmount = ((basePriceOld*maxRate)/100).toFixed(2);
    	
    	var minPerDiff = parseFloat(basePriceOld) - parseFloat(minRatePerAmount);
    	var maxPerAdd = parseFloat(basePriceOld) + parseFloat(maxRatePerAmount);
    	
    	
    	if(minPerDiff >basepriceNew)
    	{
    		growl.warning("Unit price should be Minimum RS."+minPerDiff,{
				title : ERROR_MSG
			});
    		$scope.autoRenderedServicesFinalList[idx].basePrice = oldBasePrice;
    		 var autoRenderedServicesList = $scope.autoRenderedServicesFinalList;
        	 $scope.calculationTest(autoRenderedServicesList,unitPrice);
    		return false;
    	}
    	
    	else if(basepriceNew > maxPerAdd)
    	{
    		growl.warning("Unit price should be Maximun RS."+maxPerAdd,{
				title : ERROR_MSG
			});
    		$scope.autoRenderedServicesFinalList[idx].basePrice = oldBasePrice;
    		 var autoRenderedServicesList = $scope.autoRenderedServicesFinalList;
        	 $scope.calculationTest(autoRenderedServicesList,unitPrice);
    		return false;	
    	}else{*/
			if($scope.requestStatusObject != null)
			{
				  $scope.getCancelDiscountReqForDiscount();
				
			}
			
            $scope.autoRenderedServicesFinalList[idx].concession = parseFloat(itemObj.concession);
            $scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(itemObj.patientClassPercentage);

			$scope.autoRenderedServicesFinalList[idx].basePrice = parseFloat(itemObj.basePrice);
			
			//$scope.autoRenderedServicesFinalList[idx].discount = parseFloat(0);
    		//$scope.autoRenderedServicesFinalList[idx].discountPercentage = parseFloat(0);
            $scope.autoRenderedServicesFinalList[idx].discountPercentage = parseFloat(itemObj.discountPercentage);

    		
    		var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
	       // $scope.autoRenderedServicesFinalList[idx].discountPercentage = isNaN(((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)))*100).toFixed(2)) ? parseFloat(0).toFixed(2) : ((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)))*100).toFixed(2);
	       // $scope.autoRenderedServicesFinalList[idx].discount = parseFloat(itemObj.discount).toFixed(2);
	        //var withConDic =parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[idx].discount);
            $scope.autoRenderedServicesFinalList[idx].discount =((parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[idx].concession))*(isNaN(itemObj.discountPercentage) ? 0: itemObj.discountPercentage)/100).toFixed(2);
            var withConDic =parseFloat(totalAmt) - (parseFloat(itemObj.concession) + parseFloat($scope.autoRenderedServicesFinalList[idx].discount));

	    	var txpre = 0;
			if(itemObj.taxPercentage != null || itemObj.taxPercentage > 0){
				$scope.autoRenderedServicesFinalList[idx].taxPercentage = itemObj.taxPercentage;
				txpre = itemObj.taxPercentage;
			}else{
					$scope.autoRenderedServicesFinalList[idx].taxPercentage = 0;
					}
			$scope.autoRenderedServicesFinalList[idx].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
			$scope.autoRenderedServicesFinalList[idx].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[idx].taxAmt)).toFixed(2);
			$scope.autoRenderedServicesFinalList[idx].selfPayable = $scope.autoRenderedServicesFinalList[idx].netPay;
			$scope.autoRenderedServicesFinalList[idx].creditPayable =parseFloat(0);
			$scope.saveOrderPackage();
	    /* }*/
				$scope.billWiseDiscountObj = {
						// "discountGivenBy":"",
						"discountGivenBy":$scope.userId,
						"discountCategoryId" : "",
						"discountAmountBillWise" : "",
						"discountPercentageBillWise" : "",
						"approvedAmountBillWise":"",
						"requestUserId":"",
						"requestAmountBillWise" : 0,
				}
				$scope.requestDiscountDiv = false;
		    }
           }
    
    

    $scope.rateEditCancelDiscount = function()
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
						
						 var i =0;
				    	   angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
				   			if(value.status == 'A'){
							   		$scope.autoRenderedServicesFinalList[i].discountCategoryId = "";
							   		$scope.autoRenderedServicesFinalList[i].discountPercentage = 0;
							   		$scope.autoRenderedServicesFinalList[i].discount = 0;
							   		//for remove discount
				   			 }
				   			i++;
				   			});
				    	   
				    	   $scope.calculationTest($scope.autoRenderedServicesFinalList,discountPer);
				    	   
						growl.success("Cancelled !!!!",{
							title : 'success'
						});
						
						$scope.requestApporivedDiscountDiv = false;
						$scope.discountDisabledFlagForRequest = false;
						$scope.requestAppUser = 0;
						$scope.approvedAmtApp = 0;
						$scope.requestAmtApp =0;
						$scope.statusApp = "";
						$scope.discTypeDesc = "";
						$scope.discountRadio = 2; 
						
						$scope.billWiseDiscountObj = {
								//"discountGivenBy":"",
								"discountGivenBy":$scope.userId,
								"discountCategoryId" : "",
								"discountAmountBillWise" : "",
								"discountPercentageBillWise" : "",
								"approvedAmountBillWise":"",
								"requestUserId":"",
								"requestAmountBillWise" : 0,
						}
						 $scope.requestStatusObject  = null;
						//$scope.totalDiscountSummary = parseFloat(0)
			    		angular.element('#discountRatePopup').modal('hide');
					}
				});
    }
    
    $scope.rateEditCancelDiscountLine = function()
    {
			$rootScope.startLoaderPopup();
			$scope.autoRenderedServicesFinalList[$scope.serviceAutoRenInd].basePrice = $scope.serviceAutoRenOldBasePrice;
   		    var autoRenderedServicesList = $scope.autoRenderedServicesFinalList;
       	    $scope.calculationTest(autoRenderedServicesList,discount);
       	    $rootScope.stopLoaderPopup();
			angular.element('#discountRatePopup').modal('hide');
    }
    
/*
    $scope.getDiscountPerCal = function(itemObj,idx){
    	if(itemObj.discountPercentage == '' || itemObj.discountPercentage == undefined){
    		itemObj.discountPercentage = 0;
            itemObj.discount = 0;
    		$scope.autoRenderedServicesFinalList[idx].discount = parseFloat(0);
    		$scope.autoRenderedServicesFinalList[idx].discountPercentage = parseFloat(0);
    	}
    	if(parseFloat(itemObj.discountPercentage) > 100)
		{
    		$scope.autoRenderedServicesFinalList[idx].concession = parseFloat(itemObj.concession);
			$scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(itemObj.patientClassPercentage);
			
            $scope.autoRenderedServicesFinalList[idx].discount = parseFloat(0);
    		$scope.autoRenderedServicesFinalList[idx].discountPercentage = parseFloat(0);
    		
    		var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
	        $scope.autoRenderedServicesFinalList[idx].discountPercentage = isNaN(((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)))*100).toFixed(2)) ? parseFloat(0).toFixed(2) : ((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)))*100).toFixed(2);
	        $scope.autoRenderedServicesFinalList[idx].discount = parseFloat(itemObj.discount).toFixed(2);
	        //var withConDic =parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[idx].discount);
            var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[idx].concession) + parseFloat($scope.autoRenderedServicesFinalList[idx].discount));

	    	var txpre = 0;
			if(itemObj.taxPercentage != null || itemObj.taxPercentage > 0){
				$scope.autoRenderedServicesFinalList[idx].taxPercentage = itemObj.taxPercentage;
				txpre = itemObj.taxPercentage;
			}else{
					$scope.autoRenderedServicesFinalList[idx].taxPercentage = 0;
					}
			$scope.autoRenderedServicesFinalList[idx].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
			$scope.autoRenderedServicesFinalList[idx].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[idx].taxAmt)).toFixed(2);
			$scope.autoRenderedServicesFinalList[idx].selfPayable = $scope.autoRenderedServicesFinalList[idx].netPay;
			$scope.autoRenderedServicesFinalList[idx].creditPayable =parseFloat(0);
			$scope.saveOrderPackage();
			
			//flagDis = false;
			growl.warning("You can't give discount more than 100 %.",{
				title : ERROR_MSG
			});
			return false;
		}
    	else
    	{
    		var i = 1;
    		angular.forEach($scope.autoRenderedServicesFinalList, function (service, key) 
				{
    					if(parseFloat(service.discountPercentage) >= 0)
    					{
    						
    						$scope.autoRenderedServicesFinalList[key].concession = parseFloat(service.concession);
   						    $scope.autoRenderedServicesFinalList[key].patientClassPercentage = parseFloat(service.patientClassPercentage);
   						    
	    			        var totalAmt = (parseFloat(service.quantity) * parseFloat(service.basePrice)).toFixed(2);
	    			       
	    			        //$scope.autoRenderedServicesFinalList[key].discount =((parseFloat(totalAmt))*(isNaN(service.discountPercentage) ? 0: service.discountPercentage)/100).toFixed(2);
                            $scope.autoRenderedServicesFinalList[key].discount =((parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[key].concession))*(isNaN(service.discountPercentage) ? 0: service.discountPercentage)/100).toFixed(2);

	    			        $scope.autoRenderedServicesFinalList[key].discountPercentage = parseFloat(service.discountPercentage).toFixed(2);
	    			        //var withConDic =parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[key].discount);
                            var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[idx].concession) + parseFloat($scope.autoRenderedServicesFinalList[key].discount));

	    			    	var txpre = 0;
							if(service.taxPercentage != null || service.taxPercentage > 0){
								$scope.autoRenderedServicesFinalList[key].taxPercentage = service.taxPercentage;
								txpre = service.taxPercentage;
							}else{
									$scope.autoRenderedServicesFinalList[key].taxPercentage = 0;
									}
							$scope.autoRenderedServicesFinalList[key].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
							$scope.autoRenderedServicesFinalList[key].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[key].taxAmt)).toFixed(2);
							$scope.autoRenderedServicesFinalList[key].selfPayable = $scope.autoRenderedServicesFinalList[key].netPay;
							$scope.autoRenderedServicesFinalList[key].creditPayable =parseFloat(0);
    						}
					    
						
						if(i == $scope.autoRenderedServicesFinalList.length){
							//$scope.saveOrderPackage();
							if($scope.requestStatusObject == null){
								$scope.saveOrderPackage();
							}else{
								if($scope.requestStatusObject.reqStatus == 1){
										$scope.getCancelDiscountReqForDiscount();
										$scope.saveOrderPackage();
								}
								else{
									if(parseFloat($scope.requestStatusObject.approvedDiscAmt) > parseFloat(discountAmtTmp)){
										$scope.saveOrderPackage();
									}else{
										$scope.getCancelDiscountReqForDiscount();
										$scope.saveOrderPackage();
									}
								}
							}
						}
						i++;
				});
		}
    	
    	$scope.billWiseDiscountObj = {
				// "discountGivenBy":"",
				"discountGivenBy":$scope.userId,
				"discountCategoryId" : "",
				"discountAmountBillWise" : "",
				"discountPercentageBillWise" : "",
				"approvedAmountBillWise":"",
				"requestUserId":"",
				"requestAmountBillWise" : 0,
		}
		$scope.requestDiscountDiv = false;
    }
    

    $scope.getDiscountCal = function(itemObj,idx)
    {
    	if(itemObj.discount == '' || itemObj.discount == undefined){
    		itemObj.discount = 0;
            itemObj.discountPercentage = 0;
    		$scope.autoRenderedServicesFinalList[idx].discount = 0;
    		$scope.autoRenderedServicesFinalList[idx].discountPercentage = 0;
    	}
    	if(parseFloat(itemObj.discount) > parseFloat(itemObj.basePrice))
		{
			
			$scope.autoRenderedServicesFinalList[idx].discount = parseFloat(0);
    		$scope.autoRenderedServicesFinalList[idx].discountPercentage = parseFloat(0);
    		
    		$scope.autoRenderedServicesFinalList[idx].concession = parseFloat(itemObj.concession);
			$scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(itemObj.patientClassPercentage);
			
    		
    		var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
	        $scope.autoRenderedServicesFinalList[idx].discountPercentage = isNaN((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)))*100) ? parseFloat(0).toFixed(2) : (parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)))*100;
	        $scope.autoRenderedServicesFinalList[idx].discount = parseFloat(itemObj.discount).toFixed(2);
	        //var withConDic =parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[idx].discount);
            var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[idx].concession) + parseFloat($scope.autoRenderedServicesFinalList[idx].discount));

	    	var txpre = 0;
			if(itemObj.taxPercentage != null || itemObj.taxPercentage > 0){
				$scope.autoRenderedServicesFinalList[idx].taxPercentage = itemObj.taxPercentage;
				txpre = itemObj.taxPercentage;
			}else{
					$scope.autoRenderedServicesFinalList[idx].taxPercentage = 0;
					}
			$scope.autoRenderedServicesFinalList[idx].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
			$scope.autoRenderedServicesFinalList[idx].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[idx].taxAmt)).toFixed(2);
			$scope.autoRenderedServicesFinalList[idx].selfPayable = $scope.autoRenderedServicesFinalList[idx].netPay;
			$scope.autoRenderedServicesFinalList[idx].creditPayable =parseFloat(0);
			$scope.saveOrderPackage();
			
			//flagDis = false;
			growl.warning("You can't give discount amount more then service amount..",{
				title : ERROR_MSG
			});
			
			$scope.billWiseDiscountObj = {
					// "discountGivenBy":"",
					"discountGivenBy":$scope.userId,
					"discountCategoryId" : "",
					"discountAmountBillWise" : "",
					"discountPercentageBillWise" : "",
					"approvedAmountBillWise":"",
					"requestUserId":"",
					"requestAmountBillWise" : 0,
			}
			$scope.requestDiscountDiv = false;
			
			return false;
		}
    	else
    	{
    		
    		$scope.billWiseDiscountObj = {
    				// "discountGivenBy":"",
    				"discountGivenBy":$scope.userId,
    				"discountCategoryId" : "",
    				"discountAmountBillWise" : "",
    				"discountPercentageBillWise" : "",
    				"approvedAmountBillWise":"",
    				"requestUserId":"",
    				"requestAmountBillWise" : 0,
    		}
    		$scope.requestDiscountDiv = false;
    		
    		var i = 1;
    		var discountAmtTmp = parseFloat(0);
    		angular.forEach($scope.autoRenderedServicesFinalList, function (service, key) 
				{
    					
    					if(parseFloat(service.discount) > 0)
    					{
    						discountAmtTmp = discountAmtTmp + parseFloat(service.discount);
    						//$scope.autoRenderedServicesFinalList[key].concession = parseFloat(0);
   						    //$scope.autoRenderedServicesFinalList[key].patientClassPercentage = parseFloat(0);
                            $scope.autoRenderedServicesFinalList[key].concession = parseFloat(service.concession);
                            $scope.autoRenderedServicesFinalList[key].patientClassPercentage = parseFloat(service.patientClassPercentage);

   						    
	    			        var totalAmt = (parseFloat(service.quantity) * parseFloat(service.basePrice)).toFixed(2);
	    			       // $scope.autoRenderedServicesFinalList[key].discountPercentage = isNaN(((parseFloat(service.discount)/(parseFloat(service.quantity) * parseFloat(service.basePrice)))*100).toFixed(2)) ? parseFloat(0).toFixed(2) : ((parseFloat(service.discount)/(parseFloat(service.quantity) * parseFloat(service.basePrice)))*100).toFixed(2);
                            $scope.autoRenderedServicesFinalList[key].discountPercentage = isNaN(((parseFloat(service.discount)/((parseFloat(service.quantity) * (parseFloat(service.basePrice)  - parseFloat(service.concession) )) ))*100).toFixed(2)) ? parseFloat(0).toFixed(2) : ((parseFloat(service.discount)/(parseFloat(service.quantity) * (parseFloat(service.basePrice) - parseFloat(service.concession) )))*100).toFixed(2);

	    			        $scope.autoRenderedServicesFinalList[key].discount = parseFloat(service.discount).toFixed(2);
	    			        //var withConDic =parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[key].discount);
                            var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[key].concession) + parseFloat($scope.autoRenderedServicesFinalList[key].discount));

	    			    	var txpre = 0;
							if(service.taxPercentage != null || service.taxPercentage > 0){
								$scope.autoRenderedServicesFinalList[key].taxPercentage = service.taxPercentage;
								txpre = service.taxPercentage;
							}else{
									$scope.autoRenderedServicesFinalList[key].taxPercentage = 0;
									}
							$scope.autoRenderedServicesFinalList[key].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
							$scope.autoRenderedServicesFinalList[key].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[key].taxAmt)).toFixed(2);
							$scope.autoRenderedServicesFinalList[key].selfPayable = $scope.autoRenderedServicesFinalList[key].netPay;
							$scope.autoRenderedServicesFinalList[key].creditPayable =parseFloat(0);
    						}
					    
						
						if(i == $scope.autoRenderedServicesFinalList.length){
							if($scope.requestStatusObject == null){
								$scope.saveOrderPackage();
							}else{
								if($scope.requestStatusObject.reqStatus == 1){
										$scope.getCancelDiscountReqForDiscount();
										$scope.saveOrderPackage();
								}
								else{
									if(parseFloat($scope.requestStatusObject.approvedDiscAmt) > parseFloat(discountAmtTmp)){
										$scope.saveOrderPackage();
									}else{
										$scope.getCancelDiscountReqForDiscount();
										$scope.saveOrderPackage();
									}
								}
							}
							
							
						}
						i++;
				});
		}
    	
    	
         }*/
    

    
    $scope.getDiscountPerCal = function(itemObj,idx){
    	
    	if(parseFloat(itemObj.discountPercentage) != parseFloat(itemObj.discountPercentageRateChange))
    	{
    		
    		
    		
    	if(itemObj.discountPercentage == '' || itemObj.discountPercentage == undefined){
    		itemObj.discountPercentage = 0;
    		itemObj.discount = 0;
    		$scope.autoRenderedServicesFinalList[idx].discount = parseFloat(0);
    		$scope.autoRenderedServicesFinalList[idx].discountPercentage = parseFloat(0);
    	}
    	if(parseFloat(itemObj.discountPercentage) > 100)
		{
			
    		$scope.autoRenderedServicesFinalList[idx].concession = parseFloat(itemObj.concession);
			$scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(itemObj.patientClassPercentage);
    		
    		$scope.autoRenderedServicesFinalList[idx].discount = parseFloat(0);
    		$scope.autoRenderedServicesFinalList[idx].discountPercentage = parseFloat(0);
    		
    		var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
	        $scope.autoRenderedServicesFinalList[idx].discountPercentage = isNaN(((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)))*100).toFixed(2)) ? parseFloat(0).toFixed(2) : ((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)))*100).toFixed(2);
	        $scope.autoRenderedServicesFinalList[idx].discount = parseFloat(itemObj.discount).toFixed(2);
	        var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[idx].concession) + parseFloat($scope.autoRenderedServicesFinalList[idx].discount));
	        
	    	var txpre = 0;
			if(itemObj.taxPercentage != null || itemObj.taxPercentage > 0){
				$scope.autoRenderedServicesFinalList[idx].taxPercentage = itemObj.taxPercentage;
				txpre = itemObj.taxPercentage;
			}else{
					$scope.autoRenderedServicesFinalList[idx].taxPercentage = 0;
					}
			$scope.autoRenderedServicesFinalList[idx].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
			$scope.autoRenderedServicesFinalList[idx].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[idx].taxAmt)).toFixed(2);
			$scope.autoRenderedServicesFinalList[idx].selfPayable = $scope.autoRenderedServicesFinalList[idx].netPay;
			$scope.autoRenderedServicesFinalList[idx].creditPayable =parseFloat(0);
			$scope.saveOrderPackage();
			
			//flagDis = false;
			growl.warning("You can't give discount more than 100 %.",{
				title : ERROR_MSG
			});
			
			$scope.billWiseDiscountObj = {
					// "discountGivenBy":"",
					"discountGivenBy":$scope.userId,
					"discountCategoryId" : "",
					"discountAmountBillWise" : "",
					"discountPercentageBillWise" : "",
					"approvedAmountBillWise":"",
					"requestUserId":"",
					"requestAmountBillWise" : 0,
			}
			$scope.requestDiscountDiv = false;
			
			return false;
		}
    	else
    	{
    		$scope.billWiseDiscountObj = {
					// "discountGivenBy":"",
					"discountGivenBy":$scope.userId,
					"discountCategoryId" : "",
					"discountAmountBillWise" : "",
					"discountPercentageBillWise" : "",
					"approvedAmountBillWise":"",
					"requestUserId":"",
					"requestAmountBillWise" : 0,
			}
    		//$scope.requestStatusObject = null;
			$scope.requestDiscountDiv = false;
    		
    		var i = 1;
    		var discountAmtTmp = parseFloat(0);
    		angular.forEach($scope.autoRenderedServicesFinalList, function (service, key) 
				{
    					if(parseFloat(service.discountPercentage) >= 0)
    					{
    						
    						$scope.autoRenderedServicesFinalList[key].concession = parseFloat(service.concession);
   						    $scope.autoRenderedServicesFinalList[key].patientClassPercentage = parseFloat(service.patientClassPercentage);
   						    
	    			        var totalAmt = (parseFloat(service.quantity) * parseFloat(service.basePrice)).toFixed(2);
	    			       
	    			        $scope.autoRenderedServicesFinalList[key].discount =((parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[key].concession))*(isNaN(service.discountPercentage) ? 0: service.discountPercentage)/100).toFixed(2);
	    			        discountAmtTmp = discountAmtTmp + parseFloat($scope.autoRenderedServicesFinalList[key].discount);
	    			        $scope.autoRenderedServicesFinalList[key].discountPercentage = parseFloat(service.discountPercentage).toFixed(2);
	    			        var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[idx].concession) + parseFloat($scope.autoRenderedServicesFinalList[key].discount));
	    			        
	    			    	var txpre = 0;
							if(service.taxPercentage != null || service.taxPercentage > 0){
								$scope.autoRenderedServicesFinalList[key].taxPercentage = service.taxPercentage;
								txpre = service.taxPercentage;
							}else{
									$scope.autoRenderedServicesFinalList[key].taxPercentage = 0;
									}
							$scope.autoRenderedServicesFinalList[key].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
							$scope.autoRenderedServicesFinalList[key].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[key].taxAmt)).toFixed(2);
							$scope.autoRenderedServicesFinalList[key].selfPayable = $scope.autoRenderedServicesFinalList[key].netPay;
							$scope.autoRenderedServicesFinalList[key].creditPayable =parseFloat(0);
    						}
					    
						
						if(i == $scope.autoRenderedServicesFinalList.length){
							//$scope.saveOrderPackage();
							if($scope.requestStatusObject == null){
								$scope.saveOrderPackage();
							}else{
								if($scope.requestStatusObject.reqStatus == 1){
										$scope.getCancelDiscountReqForDiscount();
										$scope.saveOrderPackage();
								}
								else{
									if(parseFloat($scope.requestStatusObject.approvedDiscAmt) > parseFloat(discountAmtTmp)){
										$scope.saveOrderPackage();
									}else{
										$scope.getCancelDiscountReqForDiscount();
										$scope.saveOrderPackage();
									}
								}
							}
						}
						i++;
				});
		}
    	
    }
    }
    
    
    $scope.getDiscountCal = function(itemObj,idx)
    {
    	
    	if(parseFloat(itemObj.discount) != parseFloat(itemObj.discountRateChange))
    	{
    		
    	if(itemObj.discount == '' || itemObj.discount == undefined){
    		itemObj.discount = 0;
    		itemObj.discountPercentage = 0;
    		$scope.autoRenderedServicesFinalList[idx].discount = 0;
    		$scope.autoRenderedServicesFinalList[idx].discountPercentage = 0;
    	}
    	if(parseFloat(itemObj.discount) > parseFloat(itemObj.basePrice))
		{
			
			$scope.autoRenderedServicesFinalList[idx].discount = parseFloat(0);
    		$scope.autoRenderedServicesFinalList[idx].discountPercentage = parseFloat(0);
    		

			$scope.autoRenderedServicesFinalList[idx].concession = parseFloat(itemObj.concession);
			$scope.autoRenderedServicesFinalList[idx].patientClassPercentage = parseFloat(itemObj.patientClassPercentage);
			    
    		
    		var totalAmt = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
	        $scope.autoRenderedServicesFinalList[idx].discountPercentage = isNaN((parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)))*100) ? parseFloat(0).toFixed(2) : (parseFloat(itemObj.discount)/(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)))*100;
	        $scope.autoRenderedServicesFinalList[idx].discount = parseFloat(itemObj.discount).toFixed(2);
	        var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[idx].concession) + parseFloat($scope.autoRenderedServicesFinalList[idx].discount));
	        
	    	var txpre = 0;
			if(itemObj.taxPercentage != null || itemObj.taxPercentage > 0){
				$scope.autoRenderedServicesFinalList[idx].taxPercentage = itemObj.taxPercentage;
				txpre = itemObj.taxPercentage;
			}else{
					$scope.autoRenderedServicesFinalList[idx].taxPercentage = 0;
					}
			$scope.autoRenderedServicesFinalList[idx].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
			$scope.autoRenderedServicesFinalList[idx].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[idx].taxAmt)).toFixed(2);
			$scope.autoRenderedServicesFinalList[idx].selfPayable = $scope.autoRenderedServicesFinalList[idx].netPay;
			$scope.autoRenderedServicesFinalList[idx].creditPayable =parseFloat(0);
			$scope.saveOrderPackage();
			
			//flagDis = false;
			growl.warning("You can't give discount amount more then service amount..",{
				title : ERROR_MSG
			});
			
			$scope.billWiseDiscountObj = {
					// "discountGivenBy":"",
					"discountGivenBy":$scope.userId,
					"discountCategoryId" : "",
					"discountAmountBillWise" : "",
					"discountPercentageBillWise" : "",
					"approvedAmountBillWise":"",
					"requestUserId":"",
					"requestAmountBillWise" : 0,
			}
			//$scope.requestStatusObject = null;
			$scope.requestDiscountDiv = false;
			
			return false;
		}
    	else
    	{
    		
    		$scope.billWiseDiscountObj = {
    				// "discountGivenBy":"",
    				"discountGivenBy":$scope.userId,
    				"discountCategoryId" : "",
    				"discountAmountBillWise" : "",
    				"discountPercentageBillWise" : "",
    				"approvedAmountBillWise":"",
    				"requestUserId":"",
    				"requestAmountBillWise" : 0,
    		}
    		$scope.requestDiscountDiv = false;
    		
    		var i = 1;
    		var discountAmtTmp = parseFloat(0);
    		angular.forEach($scope.autoRenderedServicesFinalList, function (service, key) 
				{
    					
    					if(parseFloat(service.discount) > 0)
    					{
    						discountAmtTmp = discountAmtTmp + parseFloat(service.discount);
    						$scope.autoRenderedServicesFinalList[key].concession = parseFloat(service.concession);
   						    $scope.autoRenderedServicesFinalList[key].patientClassPercentage = parseFloat(service.patientClassPercentage);
   						    
	    			        var totalAmt = (parseFloat(service.quantity) * parseFloat(service.basePrice)).toFixed(2);
	    			        $scope.autoRenderedServicesFinalList[key].discountPercentage = isNaN((parseFloat(service.discount)/((parseFloat(service.quantity) * (parseFloat(service.basePrice)  - parseFloat(service.concession) )) ))*100) ? parseFloat(0).toFixed(2) : (parseFloat(service.discount)/(parseFloat(service.quantity) * (parseFloat(service.basePrice) - parseFloat(service.concession) )))*100;
	    			        $scope.autoRenderedServicesFinalList[key].discount = parseFloat(service.discount).toFixed(2);
	    			        var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[key].concession) + parseFloat($scope.autoRenderedServicesFinalList[key].discount));
	    			        
	    			    	var txpre = 0;
							if(service.taxPercentage != null || service.taxPercentage > 0){
								$scope.autoRenderedServicesFinalList[key].taxPercentage = service.taxPercentage;
								txpre = service.taxPercentage;
							}else{
									$scope.autoRenderedServicesFinalList[key].taxPercentage = 0;
									}
							$scope.autoRenderedServicesFinalList[key].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
							$scope.autoRenderedServicesFinalList[key].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[key].taxAmt)).toFixed(2);
							$scope.autoRenderedServicesFinalList[key].selfPayable = $scope.autoRenderedServicesFinalList[key].netPay;
							$scope.autoRenderedServicesFinalList[key].creditPayable =parseFloat(0);
    						}
					    
						
						if(i == $scope.autoRenderedServicesFinalList.length){
							if($scope.requestStatusObject == null){
								$scope.saveOrderPackage();
							}else{
								if($scope.requestStatusObject.reqStatus == 1){
										$scope.getCancelDiscountReqForDiscount();
										$scope.saveOrderPackage();
								}
								else{
									if(parseFloat($scope.requestStatusObject.approvedDiscAmt) > parseFloat(discountAmtTmp)){
										$scope.saveOrderPackage();
									}else{
										$scope.getCancelDiscountReqForDiscount();
										$scope.saveOrderPackage();
									}
								}
							}
							
							
						}
						i++;
				});
		}
    }
    	
         }
    
    $scope.getDiscountListForInfo  = function(itemObj,idx)
    {
    	console.log(itemObj);
    	$scope.disPerPopup = '';
		$scope.disAmtPopup = '';
		$scope.disCategoryId = '';
		
    	$scope.serciveNamePop = itemObj.serviceStandardName;
    	$scope.totalAmtPopup =(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
    	var totalAmtPopup = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
    	$scope.totalDiscountAmtPopup = ((parseFloat(totalAmtPopup) - parseFloat(itemObj.concession))*itemObj.discountPercentage/100).toFixed(2);
    	
    	$scope.discountDetailsArrayListPopup = itemObj.discountDetailsList;
    	
    	$scope.objectIndex = idx;
    }
    $scope.getDiscountList  = function(itemObj,idx)
    {
    	$scope.disPerPopup = '';
		$scope.disAmtPopup = '';
		$scope.disCategoryId = '';
    	$scope.serciveNamePop = itemObj.serviceStandardName;
    	$scope.totalAmtPopup =(parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
    	var totalAmtPopup = (parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice)).toFixed(2);
    	$scope.totalDiscountAmtPopup = ((parseFloat(totalAmtPopup) - parseFloat(itemObj.concession))*itemObj.discountPercentage/100).toFixed(2);
    	$scope.discountDetailsArrayListPopup = itemObj.discountDetailsList;
    	$scope.objectIndex = idx;
    }
    $scope.getDiscountListPopup  = function(itemObj,idx)
    {
    	console.log("dsdsd",itemObj);
    	
    	$scope.serciveNamePop = itemObj[idx].serviceStandardName;
    	$scope.totalAmtPopup =(parseFloat(itemObj[idx].quantity) * parseFloat(itemObj[idx].basePrice)).toFixed(2);
    	var totalAmtPopup = (parseFloat(itemObj[idx].quantity) * parseFloat(itemObj[idx].basePrice)).toFixed(2);
    	$scope.totalDiscountAmtPopup = ((parseFloat(totalAmtPopup) - parseFloat(itemObj[idx].concession))*itemObj[idx].discountPercentage/100).toFixed(2);
    	$scope.discountDetailsArrayListPopup = itemObj[idx].discountDetailsList;
    	$scope.objectIndex = idx;
    	//alert("idc"+$scope.objectIndex);
    }
    
    $scope.getDisPerPopup = function(disPer)
    {
    	var idx  = $scope.objectIndex;
    	//alert(idx);
    	//alert($scope.autoRenderedServicesFinalList[idx].totalAmt);
    	var totalAmtPopup = $scope.autoRenderedServicesFinalList[idx].totalAmt;
    	var concessionPopup = $scope.autoRenderedServicesFinalList[idx].concession;
    	var calDicAmt = ((parseFloat(totalAmtPopup) - parseFloat(concessionPopup))*disPer/100).toFixed(2);	
		$scope.disAmtPopup = calDicAmt;
		
    }
    
    $scope.getDisAmtPopup = function(disAmt)
    {
    	var idx  = $scope.objectIndex;
    	//alert(disAmt);
    	//alert($scope.autoRenderedServicesFinalList[idx].totalAmt);
    	var totalAmtPopup = $scope.autoRenderedServicesFinalList[idx].totalAmt;
    	var concessionPopup = $scope.autoRenderedServicesFinalList[idx].concession;
		var disPer = ((parseFloat(disAmt)/(totalAmtPopup - (parseFloat(concessionPopup))))*100).toFixed(2);
		//alert(disPer);
		$scope.disPerPopup = disPer;
    }
    
    $scope.addDiscountInDiscountList = function()
    {
    	if($scope.disPerPopup == '' || $scope.disAmtPopup == '')
    	{
    		growl.warning("Please Enter Discount",{
				title : ERROR_MSG
			});
    	}
    	if($scope.disPerPopup !== ''  && $scope.disCategoryId == '')
    	{
    		growl.warning("Please Select Discount Category",{
				title : ERROR_MSG
			});
    	}
    	if($scope.disAmtPopup !== ''  && $scope.disCategoryId == '')
    	{
    		growl.warning("Please Select Discount Category",{
				title : ERROR_MSG
			});
    	}
    	var idx  = $scope.objectIndex;
    	console.log(idx);
    	console.log("hhh",$scope.autoRenderedServicesFinalList[idx]);
    	//alert($scope.disPerPopup);
    	
    	var disCatName = $.grep($scope.discountCategoryList, function (disCat) {
            return disCat.id == $scope.disCategoryId;
        })[0].type;
    	
    	var popupData = {
    			"discountPercentage":$scope.disPerPopup,
    			"discountAmount":(parseFloat($scope.disAmtPopup)).toFixed(2),
    			"discountCategory":disCatName,
    			"discountCategoryId":$scope.disCategoryId,
    			"createdBy" : $scope.userId,
				"createdDate" :new Date().getTime()
    			
    	}
    	
    	$scope.autoRenderedServicesFinalList[idx].discountDetailsList.push(popupData);
    	var autoRenderedServicesList = $scope.autoRenderedServicesFinalList;
    	//$scope.getDiscountList();
    	console.log("ff",$scope.autoRenderedServicesFinalList);
    	$scope.calculationTest(autoRenderedServicesList,init);
    	
    	$scope.getDiscountListPopup($scope.autoRenderedServicesFinalList,idx);
    	
    	$scope.disPerPopup = '';
		$scope.disAmtPopup = '';
		$scope.disCategoryId = '';
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
    $scope.getservicesPop = function()
    {
    	//data-toggle="modal" data-target="#addServicePopup
        if($scope.AddServiceBtn)
        	{
        	  $('#addServicePopup').modal('hide');
        	}
        else 
        	{
        	 /* if($scope.payeeId==undefined||$scope.payeeId=="")
        		  {
        			growl.warning("Please select Payer.",{
						title : ERROR_MSG
					});
        			return false;
        		  }
        	  else
        		  {*/
        	         $scope.servicetab =1;
        		    $('#addServicePopup').modal('show');
        		  /*}*/
        	}
    	$scope.servicesListPopup = [];
    	$scope.selectedPackageList = [];
    	$scope.totalAmountSummaryPopup = parseFloat(0);
    }
    
    /*$scope.showSavePopup = function()
    {
    	if($scope.savedisablebtn)
     	{
     	  $('#savedisablebtn').modal('hide');
     	}
     else 
     	{
    	  $('#savedisablebtn').modal('show');
     	}
    }*/
    $scope.checkServiceMappedWithTest = function(serviceId,specialityId)
	{
		     $rootScope.startLoaderPopup();
		     var labSpecialityId = 0;
		     if(specialityId==$scope.labConfig.labSpecialityId)
				{
		    	   labSpecialityId = specialityId;
				}
			var URI = BASE_URL + ROOT_URL + "/global/serviceMaster/checkService/"+labSpecialityId;
			var data = {serviceMasterId:serviceId,orgId:$scope.organizationId,unitId:$scope.unitId};
		    return BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
			{
		    	if(response.data.status == "success")
				{
					$rootScope.stopLoaderPopup();

					if(response.data.object==0)

					{
					   growl
						.warning('Service is not mapped with test..!!',{
									title : 'Warning!'
								});
					   response.data.object = 1;
				     }
					 return response.data.object;
				}
				else if(response.data.status == "error"){
					  $rootScope.stopLoaderPopup();
					  return response.data.object;
				   }
			 });
	}
    	
  //Check Service duplicate
	  var isServiceIdExists;
		function checkServiceIdExistsOrNot(arr, val) {
			return arr
					.some(function(arr) {
						if(val === arr.serviceMasterId)
							{
								return true;
							}
						else
							{
								return false;
							}
					});
		}
  
    
	$scope.confirmAddDuplicateService = function()
	{
		var item = $scope.addServiceObjectForduplicate;
		    	var promise = $scope.checkServiceMappedWithTest(item.serviceMasterId,item.specialityId);
				promise.then(function(obj) 
						{
		    	             if(obj>0)
		    	           {
				if($scope.serviceSpecialityId==undefined){
				angular.forEach($scope.specialityMasterList, function (value, key) {
					if(item.specialityId==value.specialityId){
						$scope.serviceSpecialityId=item.specialityId.toString();
						$scope.getSubSpecialityListById(item.specialityId);
						}
					});		
				}
				if($scope.serviceSubSpecialityId==undefined){
					angular.forEach($scope.subSpecialityMaterList, function (value, key) {
						if(item.subSpecialityId==value.subSpecialityMasterId){
							$scope.serviceSubSpecialityId=item.subSpecialityId.toString();
							}
						});
				}
				$scope.forServicePayeeId  = $scope.selfPayeeId;
				$scope.search = '';
				$('#searchServiceId').val('');
				
				if(parseInt($scope.contractId) == 0)
				{
					$scope.contractId = null;
				}
				if($scope.visitTypeId==1)
				{
				var obj = {
						"encounterId" :$scope.encounterDetails.encounterId,
						"contractId" :$scope.contractId,
						"serviceId" :item.serviceMasterId,
						"currentDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
						"organizationId":$scope.organizationId,
						"unitId":$scope.unitId,
						"visitTypeId":1,
						"billingBedCategoryId":$scope.opdBillingCatId,
						"patientCategoryId":$scope.patientDetails.patientTypeId,
						"defaultSelfTariffId":$scope.defaultSelfTariffId,
					    "selfPayeeId" :$scope.selfPayeeId,
					    "patientClassId":$scope.encounterDetails.patientClassId,
					    "tariffId" :$scope.defaultSelfTariffId,
					    "quantity":1,
					    "patientId":$scope.patientDetails.patientId,
					    "paymentEntitlementId":$scope.encounterDetails.paymentEntitlementTypeId,
					    "taxPercentage":item.taxPercentage,
					    
					};
				}
			else if($scope.visitTypeId==2 || $scope.visitTypeId == 3 || $scope.visitTypeId == 4)
				{
				var obj = {
						"encounterId" :$scope.admissionDetails.admissionId ,
						"admissionId" :$scope.encounterDetails.admissionId,
						"contractId" :$scope.contractId,
						"serviceId" :$item.serviceMasterId,
						"currentDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
						"organizationId":$scope.organizationId,
						"unitId":$scope.unitId,
						"visitTypeId":$scope.visitTypeId,
						"billingBedCategoryId":$scope.admissionDetails.bedBillingCategoryId,
						"patientCategoryId":$scope.admissionDetails.patientCategoryId,
						"defaultSelfTariffId":$scope.defaultSelfTariffId,
					    "selfPayeeId" :$scope.selfPayeeId,
					    "patientClassId":$scope.admissionDetails.patientClassId,
					    "tariffId" :$scope.defaultSelfTariffId,
					    "quantity":1,
					    "patientId":$scope.patientDetails.patientId,
					    "paymentEntitlementId":$scope.encounterDetails.paymentEntitlementTypeId,
					    "taxPercentage":item.taxPercentage,
					};
				}
				
				console.log('Base prise',obj)
				//return false;
				$rootScope.startLoaderPopup();
//				var URI = BASE_URL + ROOT_URL + "/global/consumption/service";
				var URI = BASE_URL + ROOT_URL + "/global/consumption/getSerivceRateGlobal";
				BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status=="success"){
						//var basePriceObj=response.data.object;
						if(response.data.object != "undefined" && response.data.object != null)
    					{
    						if(response.data.object.doesNotExist==null)
							{
    							$scope.forServicePayeeId = response.data.object.payeeId;
    							item.basePrice=response.data.object.serviceRate;
    							item.oldBasePrice =response.data.object.serviceRate;
    							item.dummyServiceAmt=response.data.object.serviceRate;
    							item.quantity=1;
    							item.selfPayable = response.data.object.selfPayable;
    							item.creditPayable = response.data.object.creaditPayable;
    							item.packageConsumptionAmt = response.data.object.packageConsumptionAmt;
    							item.netPay = response.data.object.netAmount;
    							item.contractId = response.data.object.contractId;
    							item.patientClassPercentage = response.data.object.patientClassPercentage;
    							item.markupStatus = response.data.object.markupStatus;
    							item.minRateEditable = response.data.object.minRateEditable;
    							item.maxRateEditable = response.data.object.maxRateEditable;
    							item.bedCategoryId=response.data.object.billingBedCategoryId;

    							if($scope.visitTypeId==2 || $scope.visitTypeId==3 || $scope.visitTypeId==4)
    							{
    								item.renderingDate=new Date();
    							 $scope.renderingDate.push({opened:false});
    							}
    							item.orderDetailsPayeeMapperDtosList=response.data.object.orderDetailsPayeeMapperDtosList;
    							item.isRateEditable=response.data.object.isRateEditable;
    		                    if(response.data.object.isRateEditable== 'N')
    							{
    		                    	item.isRateEditable='N';
    							}
    		                    item.isQuantityEditable=response.data.object.isQuantityEditable;
    		                    item.concession = response.data.object.concession;
    		                    item.isDiscountApplicable = response.data.object.isDiscountApplicable;
    		                    item.taxAmt = response.data.object.taxAmt;
    		                    item.ordTotalAmount = response.data.object.ordTotalAmount;
    		                    item.orderDetailsPackageMapperDtosList=response.data.object.orderDetailsPackageMapperDtosList;
    		                    item.tariffId=response.data.object.tariffId;
    		                    item.isAutoRender = (response.data.object.isAutoRender == 'Y' ? 'Y':'N');
    		                    item.serviceRendered = (response.data.object.isAutoRender == 'Y'?1:0);
    		                    $scope.servicePopupCalculate(item);
    							$rootScope.stopLoaderPopup();
							}else{
								$rootScope.stopLoaderPopup();
    							growl.warning("Service is not mapped with the tariff..!!",{
    								title : WARNING_MSG
    							});
    							return false;
							}
							
						}else
							{
								$rootScope.stopLoaderPopup();
								growl.warning("Please add base price of this service and then add..!!",{
									title : ERROR_MSG
								});
								return false;
							}
						//concession
						console.log("FINAL ITEM",item);
					}
					else
						{
						  $rootScope.stopLoaderPopup();
						}
					$scope.serviceSpecialityId=undefined;
					$scope.serviceSubSpecialityId=undefined;
				});
				
				$scope.search = "";
		       }
		        else{
		        	angular.element('#confirmAddService').modal('hide');
		    	    $scope.search = '';
		    	 growl
					.warning(
							'Service is Not mapped with Test!!',
							{
								title : 'Warning!'
							});
		         }
		 });
		
	}
	
	
    $scope.addServiceToTempServiceList = function($item, $model, $label){
		
    	isServiceIdExists = checkServiceIdExistsOrNot($scope.autoRenderedServicesFinalList,$item.serviceMasterId);
    	if(isServiceIdExists)
    		{
    		$scope.serviceStandardNameDulicate = $item.serviceStandardName;
    		angular.element('#confirmAddService').modal('show');
    		$scope.addServiceObjectForduplicate = $item;
    		}else{
    			isServiceIdExists = checkServiceIdExistsOrNot($scope.servicesListPopup,$item.serviceMasterId);
    			if(isServiceIdExists){
    				$scope.serviceStandardNameDulicate = $item.serviceStandardName;
    				angular.element('#confirmAddService').modal('show');
    				$scope.addServiceObjectForduplicate = $item;
    			}
    		  else
    			{
    	       var promise = $scope.checkServiceMappedWithTest($item.serviceMasterId,$item.specialityId);
		       promise.then(function(obj) 
				{
    	             if(obj>0)
    	           {
			$scope.forServicePayeeId  = $scope.selfPayeeId;
			console.log($item);
			
			$scope.search = '';
			$('#searchServiceId').val('');

			if(parseInt($scope.contractId) == 0)
			{
			   $scope.contractId = null;
			}
			
			if($scope.visitTypeId==1)
				{
				var obj = {
						"encounterId" :$scope.encounterDetails.encounterId,
						"contractId" :$scope.contractId,
						"serviceId" :$item.serviceMasterId,
						"currentDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
						"organizationId":$scope.organizationId,
						"unitId":$scope.unitId,
						"visitTypeId":1,
						"billingBedCategoryId":$scope.opdBillingCatId,
						"patientCategoryId":$scope.patientDetails.patientTypeId,
						"defaultSelfTariffId":$scope.defaultSelfTariffId,
					    "selfPayeeId" :$scope.selfPayeeId,
					    "patientClassId":$scope.encounterDetails.patientClassId,
					    "tariffId" :$scope.defaultSelfTariffId,
					    "quantity":1,
					    "patientId":$scope.patientDetails.patientId,
					    "paymentEntitlementId":$scope.encounterDetails.paymentEntitlementTypeId,
					    "taxPercentage":$item.taxPercentage,
					    
					};
				}
			else if($scope.visitTypeId==2 || $scope.visitTypeId == 3 || $scope.visitTypeId == 4)
				{
				var obj = {
						"admissionId" :$scope.admissionDetails.admissionId,
						"encounterId" :$scope.encounterDetails.encounterId,
						"contractId" :$scope.contractId,
						"serviceId" :$item.serviceMasterId,
						"currentDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
						"organizationId":$scope.organizationId,
						"unitId":$scope.unitId,
						"visitTypeId":$scope.visitTypeId,
						"billingBedCategoryId":$scope.admissionDetails.bedBillingCategoryId,
						"patientCategoryId":$scope.admissionDetails.patientCategoryId,
						"defaultSelfTariffId":$scope.defaultSelfTariffId,
					    "selfPayeeId" :$scope.selfPayeeId,
					    "patientClassId":$scope.admissionDetails.patientClassId,
					    "tariffId" :$scope.defaultSelfTariffId,
					    "quantity":1,
					    "patientId":$scope.patientDetails.patientId,
					    "paymentEntitlementId":$scope.encounterDetails.paymentEntitlementTypeId,
					    "taxPercentage":$item.taxPercentage,
					};
				}
		
			
			console.log('Base prise',obj)
			//return false;
			$rootScope.startLoaderPopup();
			var URI = BASE_URL + ROOT_URL + "/global/consumption/getSerivceRateGlobal";
			BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status=="success"){
					//var basePriceObj=response.data.object;
					if(response.data.object != "undefined" && response.data.object != null)
						{
						$scope.forServicePayeeId = response.data.object.payeeId;
						$item.basePrice=response.data.object.serviceRate;
                        $item.oldBasePrice =response.data.object.serviceRate;
                        $item.dummyServiceAmt=response.data.object.serviceRate;
						$item.quantity=1;
						$item.selfPayable = response.data.object.selfPayable;
						$item.creditPayable = response.data.object.creaditPayable;
						$item.packageConsumptionAmt = response.data.object.packageConsumptionAmt;
						$item.netPay = response.data.object.netAmount;
						$item.contractId = response.data.object.contractId;
						$item.patientClassPercentage = response.data.object.patientClassPercentage;
						$item.markupStatus = response.data.object.markupStatus;
						$item.minRateEditable = response.data.object.minRateEditable;
						$item.maxRateEditable = response.data.object.maxRateEditable;
						$item.bedCategoryId=response.data.object.billingBedCategoryId;
						if($scope.visitTypeId==2 || $scope.visitTypeId==3 || $scope.visitTypeId==4)
						{
						 $item.renderingDate=new Date();
						 $scope.renderingDate.push({opened:false});
						}
						$item.orderDetailsPayeeMapperDtosList=response.data.object.orderDetailsPayeeMapperDtosList;
	                    $item.isRateEditable=response.data.object.isRateEditable;
	                    if(response.data.object.isRateEditable== 'N')
						{
	                    	$item.isRateEditable='N';
						}
	                    $item.isQuantityEditable=response.data.object.isQuantityEditable;
	                    $item.concession = response.data.object.concession;
						$item.isDiscountApplicable = response.data.object.isDiscountApplicable;
						$item.taxAmt = response.data.object.taxAmt;
						$item.taxPercentage = response.data.object.taxPercentage;
						$item.ordTotalAmount = response.data.object.ordTotalAmount;
	                    $item.orderDetailsPackageMapperDtosList=response.data.object.orderDetailsPackageMapperDtosList;
	                	$item.tariffId=response.data.object.tariffId;
	                	$item.isAutoRender = (response.data.object.isAutoRender == 'Y' ? 'Y':'N');
	                	$item.serviceRendered = (response.data.object.isAutoRender == 'Y'?1:0);
						$scope.servicePopupCalculate($item);
						$rootScope.stopLoaderPopup();
						}
					else
						{
						$rootScope.stopLoaderPopup();
						growl.warning("Please add base price of this service and then add..!!",{
							title : ERROR_MSG
						});
						return false;
						}
					//concession
					console.log("FINAL ITEM",$item);
				}else
					{
					   $rootScope.stopLoaderPopup();
					}
				$scope.serviceSpecialityId=undefined;
				$scope.serviceSubSpecialityId=undefined;
			  });
		$scope.search = "";
    	           }
    	             else{
    	         	  //$scope.search = '';
    	         	 growl
    	     			.warning(
    	     					'Service is Not mapped with Test!!',
    	     					{
    	     						title : 'Warning!'
    	     					});
    	              }
				 }); 
    		}
       }
	}
    


    $scope.servicePopupCalculate = function(item)
    {
    	var serviceId = typeof(item.serviceMasterId) != "undefined" ? item.serviceMasterId : item.serviceId;
    		var newArr = {};
    		newArr.dummyServiceAmt = item.dummyServiceAmt;
        	newArr.specialityId = item.specialityId;
        	newArr.serviceSpecialityId = item.specialityId;
    		newArr.serviceSubSpecialityId = item.subSpecialityId;
    		newArr.subSpecialityId = item.subSpecialityId;
        	newArr.taxId = item.taxId;
    		newArr.serviceMasterId = typeof(item.serviceMasterId) != "undefined" ? item.serviceMasterId : item.serviceId;
    		newArr.serviceStandardCode = typeof(item.serviceStandardCode) != "undefined" ? item.serviceStandardCode  : item.serviceCode ;
    		newArr.serviceStandardCode = typeof(item.isPackageService) != "undefined" && item.isPackageService == "Y" ? (newArr.serviceStandardCode + " P") : newArr.serviceStandardCode;
    		newArr.serviceStandardName = typeof(item.serviceStandardName) != "undefined" ? item.serviceStandardName : item.serviceName;
    		newArr.isDiscountApplicable = typeof(item.isDiscountApplicable) != "undefined" ? item.isDiscountApplicable : 'N';
            newArr.isQuantityEditable = typeof(item.isQuantityEditable) != "undefined" ? item.isQuantityEditable : 'N';
            newArr.isRateEditable = typeof(item.isRateEditable) != "undefined" ? item.isRateEditable : 'N';
    		newArr.minRateEditable = typeof(item.minRateEditable) != "undefined" ? item.minRateEditable : 0;
    		newArr.maxRateEditable = typeof(item.maxRateEditable) != "undefined" ? item.maxRateEditable : 0;
    		newArr.specialityName = item.specialityName;
    		newArr.subSpecialityName = item.subSpecialityName;
    		newArr.quantity =  1;
    		newArr.basePrice = typeof(item.basePrice) != "undefined" ? item.basePrice : 0;
    		newArr.basePriceOld = typeof(item.basePrice) != "undefined" ? item.basePrice: 0;
            newArr.oldBasePrice =item.oldBasePrice;
    		newArr.totalAmt = parseFloat(newArr.quantity) * parseFloat(newArr.basePrice).toFixed(2);
    		newArr.concession = typeof(item.concession) != "undefined" ? parseFloat(item.concession).toFixed(2) : parseFloat(0).toFixed(2);
    		newArr.discountPercentage = 0;
    		newArr.discount =0;
    		newArr.doctorId = '';
    		newArr.doctorSpecialityId = '';
    		//newArr.isDoctorRequired = typeof(item.isDoctorRequired) != "undefined" ?item.isDoctorRequired:item.isDocReq;
            newArr.isDoctorRequired = typeof(item.isDoctorRequired) != "undefined" ?item.isDoctorRequired:'N'; 

    		newArr.discountDetailsList = [];
    		newArr.taxName = typeof(item.taxName) != "undefined" ? item.taxName : "-";
    		newArr.taxId = typeof(item.taxId) != "undefined" ? item.taxId : null;
    		newArr.taxPercentage = typeof(item.taxPercentage) != "undefined" ? item.taxPercentage : 0;
    		//newArr.selfPayable =  "0";
    		//newArr.creditPayable =   "0";
    		newArr.selfPayable =  item.selfPayable;
    		newArr.creditPayable =    item.creditPayable;
    		newArr.netPay =    item.netPay;
    		/*if($scope.isPreOrPostBilling == 'N')
    			{
    			newArr.serviceRendered = 1;
    			}
    		else
    			{
    			 newArr.serviceRendered = "0";
    			}*/
    		//newArr.serviceRendered = "0";
    		newArr.isAutoRender =  item.isAutoRender;
    		newArr.serviceRendered = item.serviceRendered;
    		newArr.serviceRenderingDeptId = item.subSpecialityId;
    		newArr.createdBy  = $scope.userId;
    		newArr.createdDate = new Date().getTime();
    		newArr.apportionedPrice = (typeof(item.apportionedPrice) != "undefined" && item.apportionedPrice != null) ? item.apportionedPrice : null;
    		newArr.isPackageService = typeof(item.isPackageService) != "undefined" ? item.isPackageService : "";
    		newArr.packageId = (typeof(item.packageId) != "undefined" && item.packageId != null) ? item.packageId : item.packageMasterId;
    		newArr.packageTypeId = (typeof(item.packageTypeId) != "undefined" && item.packageTypeId != null) ? item.packageTypeId : null; 
    		newArr.packageEitherOrGroupId = (typeof(item.packageEitherOrGroupId) != "undefined" && item.packageEitherOrGroupId != null) ? item.packageEitherOrGroupId : null; 
    		newArr.isCapService = item.isCapService;
    		newArr.packageConsumptionAmt = item.packageConsumptionAmt;
    		newArr.status = 'A';
    		newArr.payeeId = $scope.forServicePayeeId;
    		newArr.isRefDoctorShare = item.isRefDoctorShare;
    		newArr.packageEitherOrGroupId = item.packageEitherOrGroupId;
    		newArr.coShare = item.coShare;
    		newArr.contractId = item.contractId;
    		newArr.isTariffRateApplicable = item.isTariffRateApplicable; //12-10-2019
    		newArr.patientClassPercentage = item.patientClassPercentage;
    		newArr.markupStatus = item.markupStatus;
    		newArr.orderDetailsPayeeMapperDtosList = item.orderDetailsPayeeMapperDtosList;
    		newArr.orderDetailsPackageMapperDtosList = item.orderDetailsPackageMapperDtosList;
    		newArr.tariffId = item.tariffId;
    		newArr.isFromAddservice = 'Y';
    		newArr.bedCategoryId = item.bedCategoryId;
    		$scope.servicesListPopup.push(newArr);
    	    $scope.servicePopupCalculateFinal($scope.servicesListPopup); 
    }
    
    $scope.servicePopupCalculateFinal = function(itemObj)
    {
    	$scope.AutoRenderedTotalAmountSummaryPopUp = 0;
    	angular.forEach($scope.servicesListPopup, function (value, key) {
			
			$scope.AutoRenderedTotalAmountSummaryPopUp=(parseFloat($scope.AutoRenderedTotalAmountSummaryPopUp) + parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
			
		});
		$scope.totalAmountSummaryPopup = $scope.AutoRenderedTotalAmountSummaryPopUp;
    }
    
    $scope.removeTempServicesByIndex = function(index){
		//var serviceIndex = $scope.servicesListPopup.indexOf(index);
    	//if($scope.servicesListPopup[index].contractId > 0)
    	//{
    	//	if($scope.servicesListPopup[index].creditPayable>0)
    		//	{
    			 $scope.addCapAmout($scope.servicesListPopup[index]);
    		//	}
    		
    	//}
    			 $scope.totalAmountSummaryPopup = parseFloat($scope.totalAmountSummaryPopup -  ($scope.servicesListPopup[index].basePrice * $scope.servicesListPopup[index].quantity)).toFixed(2);
		$scope.servicesListPopup.splice(index, 1);
	}
    
    
    $scope.getQuantityCalPopup = function(itemObj,idx)
    {
    	//alert(idx);
    	$scope.servicesListPopup[idx].totalAmt = parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice).toFixed(2);
    	 var autoRenderedServicesList = $scope.servicesListPopup;
    	 $scope.servicePopupCalculateFinal(autoRenderedServicesList);
    }
    
    $scope.getBasePriceCalPopup = function(itemObj,idx)
    {
    			if(itemObj.basePrice == '' || itemObj.basePrice == null || itemObj.basePrice == undefined){
    				itemObj.basePrice = 0;
    			}
	    		/*var basepriceNew = parseFloat(itemObj.basePrice);
	        	var basePriceOld = parseFloat(itemObj.basePriceOld).toFixed(2);
	        	var minRate = itemObj.minRateEditable;
	        	var maxRate = itemObj.maxRateEditable;
	        	var minRatePerAmount = ((basePriceOld*minRate)/100).toFixed(2);
	        	var maxRatePerAmount = ((basePriceOld*maxRate)/100).toFixed(2);
	        	var minPerDiff = parseFloat(basePriceOld) - parseFloat(minRatePerAmount);
	        	var maxPerAdd = parseFloat(basePriceOld) + parseFloat(maxRatePerAmount);
	        	if(minPerDiff >basepriceNew)
	        	{
	        		growl.warning("Unit price should be Minimum Rs."+minPerDiff,
	        		{
	    				title : ERROR_MSG
	    			});
	        		$scope.servicesListPopup[idx].basePrice = basePriceOld;
	        		return false;
	        	}
	        	
	        	if(basepriceNew > maxPerAdd)
	        	{
	        		growl.warning("Unit price should be Maximun Rs."+maxPerAdd,
	        		{
	    				title : ERROR_MSG
	    			});
	        		$scope.servicesListPopup[idx].basePrice = basePriceOld;
	        		return false;	
	        	}*/
	        	
	        	
	        	$scope.servicesListPopup[idx].totalAmt = parseFloat(itemObj.quantity) * parseFloat(itemObj.basePrice).toFixed(2);
    			var autoRenderedServicesList = $scope.servicesListPopup;
       	 		$scope.servicePopupCalculateFinal(autoRenderedServicesList);
    		
    }
    
    $scope.addTempServiesToServiceMasterList = function(){
    	 angular.element('#addServicePopup').modal('hide');
        //For Surgecharge start
        var indexT = $scope.autoRenderedServicesFinalList.findIndex(record => record.serviceMasterId === $scope.surchargeServiceId);
        
            if (indexT >= 0) {
                if($scope.autoRenderedServicesFinalList[indexT].orderDetailsId > 0){
                    
                    var data = {
                            orderDetailsId : $scope.autoRenderedServicesFinalList[indexT].orderDetailsId,
                            ordCancelReasonId : null,
                            userId : $scope.userId
                    }
                    
                    var URI = BASE_URL + ROOT_URL + "/api/billing/bill/service/cancel";
                    
                    BillingGenericService.serviceAction(METHOD_POST,URI,data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
                        if(response.data.status == "success"){
                        }else{
                         
                        }
                    });
                
            }else{
                $scope.autoRenderedServicesFinalList.splice(indexT, 1);
            }
    }
        //For Surgecharge end
        

            
    	$rootScope.startLoader();
    	var wp = 1;
    		$scope.disLis = {};
    		angular.forEach($scope.servicesListPopup, function (value, key) {
			
    			for(var i=0;i<value.quantity;i++)
				{
    			var docName = ''; 
    			if(value.doctorId > 0){
    	    	docName = $.grep($scope.doctorsList, function (doc) {
    	            return doc.doctorId == value.doctorId;
    	        })[0].doctorName;
    			}
    	    	
    			var temp = {
						serviceMasterId : $scope.surchargeServiceId
				}
    			
    			var newArr = {};
    			newArr.priorityId  = '2';
    			newArr.isOutsourced ='N';
    			//newArr.specialityId = value.specialityId;
    			newArr.serviceSpecialityId = value.serviceSpecialityId;
    			newArr.serviceSubSpecialityId = value.serviceSubSpecialityId;
    			newArr.doctorId = value.doctorId;
    			newArr.doctorSpecialityId = value.doctorSpecialityId;
    			newArr.specialityId = value.doctorSpecialityId;
    			newArr.bedCategoryId = value.bedCategoryId;
    			/*if($scope.isPreOrPostBilling == 'N')
    			{
    			newArr.serviceRendered = 1;
    			}
    		  else
    			{
    			 newArr.serviceRendered = "0";
    			}*/
    			
    			//newArr.serviceRendered = "0";
    			newArr.isAutoRender =  value.isAutoRender;
                newArr.serviceRendered = value.serviceRendered;
    			newArr.serviceRenderingDeptId = value.serviceRenderingDeptId;
    			newArr.serviceMasterId = value.serviceMasterId;
    			newArr.serviceStandardCode = value.serviceStandardCode;
    			newArr.serviceStandardName = value.serviceStandardName;
    			newArr.isDiscountApplicable = value.isDiscountApplicable;
    			newArr.isQuantityEditable = value.isQuantityEditable;
    			newArr.isRateEditable = value.isRateEditable;
    			newArr.maxRateEditable = value.maxRateEditable;
    			newArr.specialityName = value.specialityName;
    			newArr.doctorName =docName;
    			newArr.quantity = 1;
    			newArr.basePrice = value.basePrice;
    			newArr.basePriceOld = value.basePrice;
                newArr.oldBasePrice =value.oldBasePrice;
    			newArr.discountDisabledFlag = 'Y';
    			newArr.totalAmt = parseFloat(value.quantity) * parseFloat(value.basePrice).toFixed(2);
    			newArr.concession = parseFloat(value.concession);
    			newArr.discountPercentage = value.discountPercentage;
    			newArr.discount = value.discount;
    			var dis =value.discount;;
    			var disPer =value.discountPercentage;
    			newArr.taxId = value.taxId;
    			newArr.taxName = value.taxName;
    			newArr.taxPercentage = value.taxPercentage;
    			newArr.discountDetailsList = [];
    			newArr.selfPayable = value.selfPayable;
    			newArr.selfPayableOldC = value.selfPayable;
    			newArr.netPay =   value.netPay;
    			newArr.ordTotalAmount = parseFloat(value.ordTotalAmount); 
    			newArr.creditPayable = value.creditPayable;
    			newArr.creditPayableOldC =  value.creditPayable;
    			newArr.oldNetPay = "0";
    			newArr.createdBy  = $scope.userId;
    			newArr.createdDate = new Date().getTime();
    			newArr.isPackageService = value.isPackageService;
    			newArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
    			newArr.isCapService = value.isCapService;
    			newArr.packageConsumptionAmt = value.packageConsumptionAmt;
    			newArr.subSpecialityId = value.subSpecialityId;
    			newArr.packageId = value.packageId;
    			newArr.packageTypeId = value.packageTypeId;
    			newArr.ordCancelled = "N";
    			newArr.status = "A";
    			newArr.ordCancelReasonId  = '';
    			newArr.ordCancelDatetime  = new Date().getTime();
    			newArr.ordCancelRemark  = '';
    			newArr.payeeId  = value.payeeId;
    			newArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
    			newArr.contractId = value.contractId;
    			newArr.isRefDoctorShare = value.isRefDoctorShare;
    			newArr.patientClassPercentage = value.patientClassPercentage;
        		newArr.markupStatus =value.markupStatus;
        		newArr.orderDetailsPayeeMapperDtosList = value.orderDetailsPayeeMapperDtosList;
    			newArr.orderDetailsPayeeMapperDtosList = value.orderDetailsPayeeMapperDtosList;
    			newArr.isFromAddservice = value.isFromAddservice;
    			
    			//for pacakge
        		newArr.orderDetailsPackageMapperDtosList = [{"id":"",
    				"orderDetailsId":"",
    				"serviceId":value.serviceMasterId,
    				"packageId":'',
    				"payeeId":"",
    				"serviceAmount":value.basePrice,	
    				"packageAmount":0,
    				"packageCode":'',
    			    "packageName":'',
    				"isPackageService":"N",
    				"isCapService":"N",
    				"saleDetailsId":"",
    				"itemId":"",
    				"status":"A",
    				"unitId":$scope.unitId,
    				"organizationId": $scope.organizationId}];
    			
    			newArr.tariffId = value.tariffId;
    			newArr.isDoctorRequired = typeof(value.isDoctorRequired) != "undefined" ?value.isDoctorRequired:'N'; 
                newArr.dummyServiceAmt = value.dummyServiceAmt;
    			$scope.autoRenderedServicesFinalList.push(newArr);
    			if(wp == $scope.servicesListPopup.length)
    				{
    					$rootScope.stopLoader();
    				}
    			wp++;
				}
    			
		});
    		
    		  angular.forEach($scope.selectedPackageList, function (value, key) {
           	   var newArr = {};
  				newArr.priorityId  = '2';
  				newArr.isOutsourced ='N';
                newArr.doctorSpecialityId = value.doctorSpecialityId;
                newArr.specialityId = value.doctorSpecialityId;
  				newArr.serviceSpecialityId = value.serviceSpecialityId;
  				newArr.serviceSubSpecialityId = value.serviceSubSpecialityId;
  				newArr.bedCategoryId = value.bedCategoryId;
  				/*if($scope.isPreOrPostBilling == 'N')
      			{
      			newArr.serviceRendered = 1;
      			}
      		   else
      			{
      			 newArr.serviceRendered = "0";
      			}*/
  				 newArr.serviceRendered = "0";
  				newArr.serviceRenderingDeptId = value.serviceRenderingDeptId;
  				newArr.serviceMasterId = value.serviceMasterId;
  				newArr.serviceStandardCode = value.serviceStandardCode;
  				newArr.serviceStandardName = value.serviceStandardName;
  				newArr.isDiscountApplicable = value.isDiscountApplicable;
  				newArr.isQuantityEditable = value.isQuantityEditable;
  				newArr.isRateEditable = value.isRateEditable;
  				newArr.minRateEditable = value.minRateEditable;
  				newArr.maxRateEditable = value.maxRateEditable;
  				newArr.specialityName = value.specialityName;
  				newArr.quantity = value.quantity;
  				newArr.basePrice = value.basePrice;
  				newArr.basePriceOld = value.basePrice;
                newArr.oldBasePrice =value.oldBasePrice;
  				newArr.discountDisabledFlag = 'Y';
  				newArr.totalAmt = parseFloat(1) * parseFloat(value.basePrice).toFixed(2);
   				newArr.concession = parseFloat(value.concession);
   				newArr.patientClassPercentage = parseFloat(value.patientClassPercentage);
  				newArr.discount = value.discount;
  				var dis =value.discount;;
  				var disPer =value.discountPercentage;
  				newArr.taxId = value.taxId;
  				newArr.taxName = value.taxName;
  				newArr.taxPercentage = value.taxPercentage;
  				newArr.discountDetailsList = [];
  				newArr.selfPayable = value.selfPayable;
  				newArr.creditPayable =   value.creditPayable;
  				newArr.netPay =   value.netPay;
  				newArr.ordTotalAmount = parseFloat(value.ordTotalAmount); 
  				newArr.oldNetPay = "0";
  				newArr.createdBy  = $scope.userId;
  				newArr.createdDate = new Date().getTime();
  				newArr.isPackage = true;
  				newArr.packageMasterId = value.packageMasterId;
  				newArr.packageTypeId = value.packageTypeId;
  				newArr.packageId = value.packageId;
  				newArr.isPackageService = value.isPackageService;
  				newArr.isCapService = value.isCapService;
      			newArr.packageConsumptionAmt = value.packageConsumptionAmt;
      			newArr.subSpecialityId = value.subSpecialityId;
      			newArr.serviceRenderingDeptId = value.subSpecialityId;
      			newArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
      			newArr.orderDetailsPayeeMapperDtosList = value.orderDetailsPayeeMapperDtosList;
      			newArr.tariffId = value.tariffId;
      			newArr.isFromAddservice = value.isFromAddservice;
      			newArr.orderDetailsPackageMapperDtosList =(value.orderDetailsPackageMapperDtosList==null|| value.orderDetailsPackageMapperDtosList==undefined)?[]:value.orderDetailsPackageMapperDtosList;
       			
      			//vivek added
      			newArr.selfPayableOldC = value.selfPayable;
      			newArr.creditPayableOldC =  value.creditPayable;
                  newArr.payeeId  = value.payeeId;
                  newArr.dummyServiceAmt = value.dummyServiceAmt;
                  newArr.isDoctorRequired = typeof(value.isDoctorRequired) != "undefined" ?value.isDoctorRequired:'N';


  				$scope.autoRenderedServicesFinalList.push(newArr);
   			
   			//get services if package type is EHC
   	    	if(typeof(value.packageTypeId) != "undefined" && value.packageTypeId == "1"){
   						angular.forEach(value.packageServiceList,function(val,i){
   							
   							var docName = ''; 
   			    			if(val.doctorId > 0){
   			    	    	docName = $.grep($scope.doctorsList, function (doc) {
   			    	            return doc.doctorId == val.doctorId;
   			    	        })[0].doctorName;
   			    			}
   			    			val.doctorName = docName;
   			    			$scope.autoRenderedServicesFinalList.push(val);
   						})
   	    	}
   	    	$rootScope.stopLoader();
   		});
    		  $scope.billWiseDiscountObj = {
						// "discountGivenBy":"",
						"discountGivenBy":$scope.userId,
						"discountCategoryId" : "",
						"discountAmountBillWise" : "",
						"discountPercentageBillWise" : "",
						"approvedAmountBillWise":"",
						"requestUserId":"",
						"requestAmountBillWise" : 0,
				}
			 $scope.requestDiscountDiv = false;  
        if($scope.selectedPackageList !=null && $scope.selectedPackageList.length>0)
  		{
  			   $rootScope.startLoaderPopup();
  			   $scope.calculationTest($scope.autoRenderedServicesFinalList,addService);
  			   $scope.saveOrderPackage();
  			   $scope.selectedPackageList = [];
  			}
  		else 
  		   {
  			 angular.element('#addServicePopup').modal('hide');
  			 var listServicepopup = $scope.autoRenderedServicesFinalList;
  	    	 $scope.calculationTest(listServicepopup,addService);
  	    	 $scope.saveOrderPackage();
  		   }
   		   /*$scope.selectedPackageList = [];
    		console.log("ddfdfd",$scope.autoRenderedServicesFinalList);
    		var listServicepopup = $scope.autoRenderedServicesFinalList;
    		$scope.calculationTest(listServicepopup,addService);*/
    	

			//Ganesh
			// if(parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise)>0)
	    	// {
			//	 $scope.funDiscountPercentageBillWise();
	    	// }
    	
    }
    
    
    $scope.getRemoveDiscountPopup = function(item,idxDis)
    {
    	//var idx  = $scope.objectIndex;
    	$scope.objectIndexDiscount = idxDis;
    	$scope.discountObjectForRemove = item;
    	//alert(idx);
    	
    	/*if(item.orderDetailsDiscountId > 0)
    		{
    			var disArr = {};
    			disArr.
    		}*/
    	
    	/*var currDisAmt = $scope.autoRenderedServicesFinalList[idx].discount;
    	var currDisPer = $scope.autoRenderedServicesFinalList[idx].discountPercentage;
    	
    	var finalDisAmt = (parseFloat(currDisAmt) - parseFloat(item.discountAmount)).toFixed(2);
    	var finalDisPer = (parseFloat(currDisPer) - parseFloat(item.discountPercentage)).toFixed(2);
    	
    	$scope.autoRenderedServicesFinalList[idx].discount = finalDisAmt;
    	$scope.autoRenderedServicesFinalList[idx].discountPercentage = finalDisPer;
    	
    	var serviceIndex = $scope.autoRenderedServicesFinalList[idx].discountDetailsList.indexOf(idx);
    	$scope.autoRenderedServicesFinalList[idx].discountDetailsList.splice(serviceIndex, 1);
    	
    	
    	
    	var autoRenderedServicesList = $scope.autoRenderedServicesFinalList;
    	
    	$scope.calculationTest(autoRenderedServicesList,init);
    	$scope.getDiscountListPopup($scope.autoRenderedServicesFinalList,idx);*/
    	
    }
    
    
    $scope.removeDiscount = function()
    {
    	var idx  = $scope.objectIndex;
    	var discIdx = $scope.objectIndexDiscount;
    	var item = $scope.discountObjectForRemove;
    	
    	//return false;
    	if(item.orderDetailsDiscountId > 0)
    		{
    			var disArr = {};
    			disArr.orderDetailsDiscountId  = item.orderDetailsDiscountId;
    			disArr.orderDetailsId = item.orderDetailsId;
    			disArr.discountCategoryId = item.discountCategoryId;
    			disArr.discountPercentage = item.discountPercentage;
    			disArr.discountAmount = item.discountAmount;
    			disArr.isCancel = 'Y';
    			disArr.cancelBy  = $scope.userId;
    			disArr.cancelReasonId  = $scope.discountReasonId;
    			disArr.remark = $scope.discountRemark;
    			disArr.createdBy  = item.createdBy;
    			disArr.createdDate  = item.createdDate;
    			disArr.updatedBy  = $scope.userId;
    			disArr.updatedDate  = new Date().getTime();
    			disArr.cancelledDate = new Date().getTime();
    			disArr.orgId  = $scope.organizationId;
    			disArr.unitId  = $scope.unitId;
    			disArr.discountDate = new Date().getTime();
    			disArr.status = "I";
    			disArr.isConsessionDiscount = "N";
    			$scope.removeDiscountList.push(disArr);
    		}
    	
    	
    	var currDisAmt = $scope.autoRenderedServicesFinalList[idx].discount;
    	var currDisPer = $scope.autoRenderedServicesFinalList[idx].discountPercentage;
    	
    	var finalDisAmt = (parseFloat(currDisAmt) - parseFloat(item.discountAmount)).toFixed(2);
    	var finalDisPer = (parseFloat(currDisPer) - parseFloat(item.discountPercentage)).toFixed(2);
    	
    	$scope.autoRenderedServicesFinalList[idx].discount = finalDisAmt;
    	$scope.autoRenderedServicesFinalList[idx].discountPercentage = finalDisPer;
    	
    	var serviceIndex = $scope.autoRenderedServicesFinalList[idx].discountDetailsList.indexOf(item);
    	$scope.autoRenderedServicesFinalList[idx].discountDetailsList.splice(serviceIndex, 1);
    	
    	if($scope.autoRenderedServicesFinalList[idx].discountDetailsList.length == 0)
    		{
    		$scope.autoRenderedServicesFinalList[idx].discountCategoryId = '';
    		}
    	
    	var autoRenderedServicesList = $scope.autoRenderedServicesFinalList;
    	
    	$scope.calculationTest(autoRenderedServicesList,init);
    	$scope.getDiscountListPopup($scope.autoRenderedServicesFinalList,idx);
    	$scope.discountReasonId = '';
    	$scope.discountRemark = '';
    }
    
   
    
	$scope.addCapAmout = function(info)
	{
  if((info.contractId>0 && info.creditPayable>0)||info.orderDetailsPackageMapperDtosList.find(pkg => pkg.isPackageService=='Y')||info.orderDetailsId>0)
   	 {	
		var data = {
				specialityId : info.serviceSpecialityId,
				contractId : info.contractId,
				orderDetailsId : info.orderDetailsId,
				orderDetailsPayeeMapperDtosList : info.orderDetailsPayeeMapperDtosList,
				orderDetailsPackageMapperDtosList : info.orderDetailsPackageMapperDtosList,
				serviceId:info.serviceMasterId,
				createdBy : $scope.userId,
		}
		
    	if($scope.billObj.visitTypeId==1){
    		 data.encounterId =$scope.billObj.encounterId;
    	}
    	if($scope.billObj.visitTypeId==2 || $scope.billObj.visitTypeId==3 || $scope.billObj.visitTypeId==4)
    		{
    		 data.admissionId = $scope.billObj.admissionId;
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
							}
						});
   	  }
	}
	
	 $scope.cancelSevices = function()
	    {
	    	
	    	if($scope.servicesCancelReasonId > 0){
		    	var idx  = $scope.objectIndexService;
		    	var item = $scope.serciveObjectForRemove;
		    	var itemForNagetive = angular.copy($scope.serciveObjectForRemove);
		    	
		    	if(item.orderDetailsId > 0)
		    		{
		    		
		    		itemForNagetive.basePrice = parseFloat(-($scope.serciveObjectForRemove.basePrice));
		        	itemForNagetive.concession = parseFloat(-($scope.serciveObjectForRemove.concession));
		        	itemForNagetive.orderDetailsId = 0;
		        	itemForNagetive.orderId = 0;
		        	itemForNagetive.discountDetailsList = [];
		        	itemForNagetive.discountPercentage = parseFloat(0);
		        	itemForNagetive.discount = parseFloat(0);
		        	itemForNagetive.ordCancelled  = 'Y';
		        	itemForNagetive.oldOrdDtlId = item.orderDetailsId;
		        	itemForNagetive.status = "I";
		        	itemForNagetive.isPackage = item.isPackage;
		        	itemForNagetive.ordCancelReasonId  = $scope.servicesCancelReasonId;
		        	itemForNagetive.ordCancelDatetime  = new Date().getTime();
		        	itemForNagetive.ordCancelRemark  = $scope.servicesCancelRemark ;
		        	itemForNagetive.isPackageService = item.isPackageService;
		        	itemForNagetive.serviceRenderingDeptId = item.serviceRenderingDeptId;
		        	itemForNagetive.isCapService = item.isCapService;
		        	itemForNagetive.creditPayable = item.creditPayable*-1;
		        	itemForNagetive.selfPayable = item.selfPayable*-1;
		        	console.log("AFTERRRR",itemForNagetive);
		    		
		    			if(item.discountDetailsList.length > 0){
		    				angular.forEach(item.discountDetailsList, function (value, key) {
		    					if(value.orderDetailsDiscountId > 0)
		    		    		{
		    		    			var disArr = {};
		    		    			disArr.orderDetailsDiscountId  = value.orderDetailsDiscountId;
		    		    			disArr.orderDetailsId = value.orderDetailsId;
		    		    			disArr.discountCategoryId = value.discountCategoryId;
		    		    			disArr.discountPercentage = value.discountPercentage;
		    		    			disArr.discountAmount = value.discountAmount;
		    		    			disArr.isCancel = 'Y';
		    		    			disArr.cancelBy  = $scope.userId;
		    		    			disArr.cancelReasonId  = "";
		    		    			disArr.remark ="";
		    		    			disArr.createdBy  = value.createdBy;
		    		    			disArr.createdDate  = value.createdDate;
		    		    			disArr.updatedBy  = $scope.userId;
		    		    			disArr.updatedDate  = new Date().getTime();
		    		    			disArr.cancelledDate = new Date().getTime();
		    		    			disArr.orgId  = $scope.organizationId;
		    		    			disArr.unitId  = $scope.unitId;
		    		    			disArr.discountDate = new Date().getTime();
		    		    			disArr.status = "I";
		    		    			disArr.isConsessionDiscount = "N";
		    		    			$scope.removeDiscountList.push(disArr);
		    		    		}
		    	    			
		    	    			
		    	    		});
		    			}
		    	
		    			itemForNagetive.orderDetailsPayeeMapperDtosList = [];
		    			
		    			$scope.autoRenderedServicesFinalList[idx].discountPercentage = parseFloat(0);
		    			$scope.autoRenderedServicesFinalList[idx].discount = parseFloat(0);
		    			$scope.autoRenderedServicesFinalList[idx].ordCancelled = 'Y';
		    			$scope.autoRenderedServicesFinalList[idx].discountDetailsList = [];
		    			$scope.autoRenderedServicesFinalList[idx].ordCancelReasonId  = $scope.servicesCancelReasonId;
		    			$scope.autoRenderedServicesFinalList[idx].ordCancelDatetime  = new Date().getTime();
		    			$scope.autoRenderedServicesFinalList[idx].ordCancelRemark  = $scope.servicesCancelRemark ;
		    			$scope.autoRenderedServicesFinalList[idx].status  = 'I' ;
		    			$scope.autoRenderedServicesFinalList[idx].oldOrdDtlId = null;
		        		var autoRenderedServicesList = $scope.autoRenderedServicesFinalList;
		            	
		        		console.log("AFTER",autoRenderedServicesList);
		        		// return false;
		            	$scope.calculationTest(autoRenderedServicesList,init);
		
		            	$scope.removeServicesList = [];
		            	
		    			console.log("RE DIS LISt",$scope.removeDiscountList)
		    			console.log("RE SER LISt",$scope.removeServicesList);
		            	// test code
		            	
		    			$scope.servicesCancelReasonId = '';
		            	$scope.servicesCancelRemark = '';	
		        		
		    		}else
		    	      {
		    		      var serviceIndex = $scope.autoRenderedServicesFinalList.indexOf(item);
		    		      
		    		      $scope.autoRenderedServicesFinalList.splice(serviceIndex, 1);
		    		      
		    		      var autoRenderedServicesList = $scope.autoRenderedServicesFinalList;
		    		         
		    		      console.log("AFTER",autoRenderedServicesList);
		    		      
		    		         $scope.calculationTest(autoRenderedServicesList,init);
		    		         
		    		         $scope.servicesCancelReasonId = '';
		 	            	$scope.servicesCancelRemark = '';	
				      }
		    	
		    	
		    	if($scope.requestStatusObject != null){
					$scope.getCancelDiscountReqForDiscount();
					$scope.billWiseDiscountObj = {
							// "discountGivenBy":"",
							"discountGivenBy":$scope.userId,
							"discountCategoryId" : "",
							"discountAmountBillWise" : "",
							"discountPercentageBillWise" : "",
							"approvedAmountBillWise":"",
							"requestUserId":"",
							"requestAmountBillWise" : 0,
					}
				 $scope.requestDiscountDiv = false;
				 $scope.lineItemDiscountAmt = parseFloat(0);
				 $scope.billWiseDiscountObj.discountAmountBillWise =  parseFloat(0);
				 $scope.billWiseDiscountObj.requestAmountBillWise =   parseFloat(0);
					}
				 
		    			// $scope.addCapAmout(item);
		     // Ganesh
		   	 if(parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise)>0 && $scope.isBillWiseDiscountApplied)
			 {
				 $scope.funDiscountPercentageBillWise();
			     $scope.applyDiscount();
			 }
		   	 
		   	 /*
				 * if(item.isPackage) {
				 */
		   	       $rootScope.startLoaderPopup();
		   		   $scope.saveOrderPackage();
		   		 angular.element('#serviceCancellationModal').modal('hide');
		   /*
			 * } else{ angular.element('#serviceCancellationModal').modal('hide'); }
			 */
			
	    }else
	    	{
		    	growl.warning("Please select reason..!!",{
					title : ERROR_MSG
				});
				return false;
	    	}
	    	
	    }
	    
    
    $scope.getPayeeCreditDetails = function(itemList)
    {
    	$scope.payeeCreditDetailslList = [];
    	$scope.payeeCreditDetailslList = itemList;
    }
    
    $scope.getRemoveServicePopup = function(item,idx)
    {
    	//console.log("ITEM",item);
    	$scope.objectIndexService = idx;
    	$scope.serciveObjectForRemove = item;
    	
    	
    }
    
    $scope.funDiscountPercentageBillWise = function()
    {
    	$scope.totalSumDisAmount = parseFloat(0);
		var i = 1;
		angular.forEach($scope.autoRenderedServicesFinalList,function(value,index)
				 {
			        if(value.status == 'A' && value.isDiscountApplicable == 'Y')
			        	{
	    					
			        	var totalAmt = ((parseFloat(value.quantity) * parseFloat(value.basePrice))- parseFloat(value.concession)).toFixed(2);
	    					$scope.totalSumDisAmount =parseFloat($scope.totalSumDisAmount) + parseFloat(totalAmt);
	    					
			        	  
			        	}
			         if(i == $scope.autoRenderedServicesFinalList.length)
			        	{
			        		var totalDisAmount =(((parseFloat($scope.totalSumDisAmount))*(parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise)/100))).toFixed(2);
				    		if(totalDisAmount > 0){
				    		$scope.billWiseDiscountObj.discountPercentageBillWise = parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise).toFixed(2);
				    		$scope.billWiseDiscountObj.discountAmountBillWise = totalDisAmount;
				    		
				    		if($scope.totalSumDisAmount > 0){
				    			if($scope.billWiseDiscountObj.discountAmountBillWise > $scope.totalSumDisAmount){
								growl.warning("You can give discount on only "+$scope.totalSumDisAmount,{
									title : WARNING_MSG
								});
								$scope.billWiseDiscountObj.discountAmountBillWise=0;
								$scope.billWiseDiscountObj.discountPercentageBillWise=0;
						
				    			}
								}else{
										growl.warning("You have no service for discount ",{
											title : WARNING_MSG
										});
										$scope.billWiseDiscountObj.discountAmountBillWise=0;
										$scope.billWiseDiscountObj.discountPercentageBillWise=0;
								}
				    		
				    		 }
				    		else
				    			{
					    			$scope.billWiseDiscountObj.discountPercentageBillWise =0;
						    		$scope.billWiseDiscountObj.discountAmountBillWise =0;
						    		
						    		if($scope.totalSumDisAmount > 0){
						    			if($scope.billWiseDiscountObj.discountAmountBillWise > $scope.totalSumDisAmount){
										growl.warning("You can give discount only of amount "+$scope.totalSumDisAmount,{
											title : WARNING_MSG
										});
										$scope.billWiseDiscountObj.discountAmountBillWise=0;
										$scope.billWiseDiscountObj.discountPercentageBillWise=0;
						    			}
										}else{
												growl.warning("You have no service for discount ",{
													title : WARNING_MSG
												});
												$scope.billWiseDiscountObj.discountAmountBillWise=0;
												$scope.billWiseDiscountObj.discountPercentageBillWise=0;
										}
				    			}
			        	}
					i++;	
				 });
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
						$scope.discTypeDesc = "";
						$scope.statusApp = "";
						$scope.discountRadio = 2; 
						
						$scope.billWiseDiscountObj = {
								// "discountGivenBy":"",
								"discountGivenBy":$scope.userId,
								"discountCategoryId" : "",
								"discountAmountBillWise" : "",
								"discountPercentageBillWise" : "",
								"approvedAmountBillWise":"",
								"requestUserId":"",
								"requestAmountBillWise" : 0,
						}
						 $scope.requestStatusObject  = null;
			    		angular.element('#discountCancellationModalReq').modal('hide');
					}
				});
		
	}
    
    $scope.getCancelDiscountReq = function()
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
						
						 var i =0;
				    	   angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
				   			if(value.status == 'A'){
				   				
				   				if(parseFloat(value.discount) > 0){
				   					
						   				$scope.autoRenderedServicesFinalList[i].concession = parseFloat(0);
			   						    $scope.autoRenderedServicesFinalList[i].patientClassPercentage = parseFloat(0);
			   						    
				    			        var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
				    			        $scope.autoRenderedServicesFinalList[i].discountPercentage = 0;
				    			        $scope.autoRenderedServicesFinalList[i].discount = parseFloat(0).toFixed(2);
				    			        var withConDic =parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[i].discount);
				    			        
				    			    	var txpre = 0;
										if(value.taxPercentage != null || value.taxPercentage > 0){
											$scope.autoRenderedServicesFinalList[i].taxPercentage = value.taxPercentage;
											txpre = value.taxPercentage;
										}else{
												$scope.autoRenderedServicesFinalList[i].taxPercentage = 0;
												}
										$scope.autoRenderedServicesFinalList[i].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
										$scope.autoRenderedServicesFinalList[i].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[i].taxAmt)).toFixed(2);
										$scope.autoRenderedServicesFinalList[i].selfPayable = $scope.autoRenderedServicesFinalList[i].netPay;
										$scope.autoRenderedServicesFinalList[i].creditPayable =parseFloat(0);
								
				   					}
				   				}
				   			if(i+1 == $scope.autoRenderedServicesFinalList.length){
				   				$scope.saveOrderPackage();
				   			}
				    	   		
				   			i++;
				   			});
				    	   
				    	   
						growl.success("Cancelled !!!!",{
							title : 'success'
						});
						
						$scope.requestApporivedDiscountDiv = false;
						$scope.discountDisabledFlagForRequest = false;
						$scope.requestAppUser = 0;
						$scope.approvedAmtApp = 0;
						$scope.requestAmtApp =0;
						$scope.discTypeDesc = "";
						$scope.statusApp = "";
						$scope.discountRadio = 2; 
						
						$scope.billWiseDiscountObj = {
								// "discountGivenBy":"",
								"discountGivenBy":$scope.userId,
								"discountCategoryId" : "",
								"discountAmountBillWise" : "",
								"discountPercentageBillWise" : "",
								"approvedAmountBillWise":"",
								"requestUserId":"",
								"requestAmountBillWise" : 0,
						}
						 $scope.requestStatusObject  = null;
			    		angular.element('#discountCancellationModalReq').modal('hide');
					}
				});
		
	}
    
    $scope.funDiscountAmountBillWise = function()
    {
    	$scope.totalSumDisAmount = parseFloat(0);
		var i = 1;
		angular.forEach($scope.autoRenderedServicesFinalList,function(value,index)
			 {
		        if(value.status == 'A' && value.isDiscountApplicable == 'Y')
		        	{
		        	
		        	                var totalAmt = ((parseFloat(value.quantity) * parseFloat(value.basePrice))- parseFloat(value.concession)).toFixed(2);
    						 		$scope.totalSumDisAmount =parseFloat($scope.totalSumDisAmount) + parseFloat(totalAmt);
    						
		        	
		        	}
		         if(i == $scope.autoRenderedServicesFinalList.length)
		        	{
		        	 var totalDisPercentage =((parseFloat($scope.billWiseDiscountObj.discountAmountBillWise)/(parseFloat($scope.totalSumDisAmount)))*100).toFixed(2);
			    		if(totalDisPercentage >= 0){
			    		$scope.billWiseDiscountObj.discountPercentageBillWise = totalDisPercentage;
			    		$scope.billWiseDiscountObj.discountAmountBillWise = parseFloat($scope.billWiseDiscountObj.discountAmountBillWise).toFixed(2);
			    		if(Number($scope.billWiseDiscountObj.discountPercentageBillWise)>100)
						{
						if($scope.totalSumDisAmount > 0){
							if($scope.billWiseDiscountObj.discountAmountBillWise > $scope.totalSumDisAmount){
						growl.warning("You can give discount only of amount "+$scope.totalSumDisAmount,{
							title : WARNING_MSG
						});
						$scope.billWiseDiscountObj.discountAmountBillWise=0;
						$scope.billWiseDiscountObj.discountPercentageBillWise=0;
					
							}
						}else{
								growl.warning("You have no service for discount ",{
									title : WARNING_MSG
								});
								$scope.billWiseDiscountObj.discountAmountBillWise=0;
								$scope.billWiseDiscountObj.discountPercentageBillWise=0;
						}
			    		return false;
						}
			    		}else{
						
							if($scope.totalSumDisAmount > 0){
								if($scope.billWiseDiscountObj.discountAmountBillWise > $scope.totalSumDisAmount){
							growl.warning("You can give discount only of amount "+$scope.totalSumDisAmount,{
								title : WARNING_MSG
							});
							$scope.billWiseDiscountObj.discountAmountBillWise=0;
							$scope.billWiseDiscountObj.discountPercentageBillWise=0;
						
								}
							}else{
									growl.warning("You have no service for discount ",{
										title : WARNING_MSG
									});
									$scope.billWiseDiscountObj.discountAmountBillWise=0;
									$scope.billWiseDiscountObj.discountPercentageBillWise=0;
							}
				    		return false;
			    		}
		        	}
				i++;	
			 });
    }
    
    $scope.getApplyDiscountPopup = function()
    {
    	if($scope.billWiseDiscountObj.discountPercentageBillWise === '')
    		{
	    		growl.warning("Discount Percentage is required.",{
					title : ERROR_MSG
				});
	    		return false;
    			
    		}

    	else if($scope.billWiseDiscountObj.discountAmountBillWise === '')
    		{
	    		growl.warning("Discount Amount is required.",{
					title : ERROR_MSG
				});
	    		return false;
    		}
    	else if($scope.billWiseDiscountObj.discountAmountBillWise >= 0 && $scope.billWiseDiscountObj.discountPercentageBillWise >= 0 && $scope.billWiseDiscountObj.discountCategoryId === '')
    		{
	    		growl.warning("Discount Category is required.",{
					title : ERROR_MSG
				});
	    		return false;
    		}
    	
    	else
    		{
    		if($scope.billWiseDiscountObj.discountAmountBillWise >= 0 && $scope.billWiseDiscountObj.discountPercentageBillWise >= 0 && $scope.billWiseDiscountObj.discountCategoryId > 0)
    		{
    			// if($scope.totalSumDisAmount > 0)
    			angular.element('#applyDiscountPopup').modal('show');
    		}
    		}
    }
    
    $scope.applyDiscount  = function()
    {
    	
    	//if(parseFloat($scope.totalDisServiceWise) > parseFloat($scope.billWiseDiscountObj.discountAmountBillWise)){
		    	$scope.listOfOldDisAmt = [];
		    	$scope.isBillWiseDiscountApplied = true;
		    	
		    		if($scope.userDiscountObject != null){
		    		
					if($scope.userDiscountObject.discountType == 1){
						
						var fromDisAmt = (parseFloat($scope.totalSumDisAmount) * parseFloat($scope.userDiscountObject.fromDiscountPer))/100;
						var toDisAmt = (parseFloat($scope.totalSumDisAmount) * parseFloat($scope.userDiscountObject.toDiscountPer))/100;
						$scope.userDiscountObject.fromDiscountAmount = fromDisAmt;
						$scope.userDiscountObject.toDiscountAmount = toDisAmt;
					}
		    	}
	
	
		    	if(($scope.requestStatusObject == null) && ($scope.userDiscountObject == null || parseFloat($scope.userDiscountObject.fromDiscountAmount) < $scope.billWiseDiscountObj.discountAmountBillWise && parseFloat($scope.userDiscountObject.toDiscountAmount) < $scope.billWiseDiscountObj.discountAmountBillWise) )
		    	{
		    	   $scope.billWiseDiscountObj.requestAmountBillWise = $scope.billWiseDiscountObj.discountAmountBillWise;
		    	   var i =0;
		    	   angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
		   			if(value.status == 'A' && value.isDiscountApplicable == 'Y'){
		                var totalAmt = (parseFloat(1) * parseFloat(value.basePrice)).toFixed(2);
		
		   				/*$scope.autoRenderedServicesFinalList[i].discountCategoryId = "";
				   		$scope.autoRenderedServicesFinalList[i].discountPercentage = 0;
				   		$scope.autoRenderedServicesFinalList[i].discount = 0;
				   		
				   		$scope.autoRenderedServicesFinalList[i].taxAmt = parseFloat((parseFloat(totalAmt) * $scope.autoRenderedServicesFinalList[i].taxPercentage)/100).toFixed(2);
						$scope.autoRenderedServicesFinalList[i].netPay =(parseFloat(totalAmt) + parseFloat($scope.autoRenderedServicesFinalList[i].taxAmt)).toFixed(2);
						$scope.autoRenderedServicesFinalList[i].selfPayable =(parseFloat(totalAmt) + parseFloat($scope.autoRenderedServicesFinalList[i].taxAmt)).toFixed(2);*/
		                
		                $scope.autoRenderedServicesFinalList[i].discountCategoryId = $scope.billWiseDiscountObj.discountCategoryId.toString();
    		    		if($scope.discountRadio == 1)
    					{
    		    			
                            $scope.autoRenderedServicesFinalList[i].concession = parseFloat(value.concession);
                            $scope.autoRenderedServicesFinalList[i].patientClassPercentage = parseFloat(value.patientClassPercentage);

                            
    		    			$scope.autoRenderedServicesFinalList[i].discountPercentage = $scope.billWiseDiscountObj.discountPercentageBillWise;
    		    			var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
    		    			//$scope.disAmtCal = ((parseFloat(totalAmt)*parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise))/100).toFixed(2);
                            $scope.disAmtCal = (((parseFloat(totalAmt) - parseFloat(value.concession) ) *parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise))/100).toFixed(2);

    		    			$scope.autoRenderedServicesFinalList[i].discount = $scope.disAmtCal;
                            var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[i].concession) + parseFloat($scope.autoRenderedServicesFinalList[i].discount));

    		    			  //var withConDic =parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[i].discount);
	  	    		  	        
	  	    		  	    	var txpre = 0;
	  	    		  			if(value.taxPercentage != null || value.taxPercentage > 0){
	  	    		  				$scope.autoRenderedServicesFinalList[i].taxPercentage = value.taxPercentage;
	  	    		  				txpre = value.taxPercentage;
	  	    		  			}else{
	  	    		  					$scope.autoRenderedServicesFinalList[i].taxPercentage = 0;
	  	    		  					}
	  	    		  			$scope.autoRenderedServicesFinalList[i].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
	  	    		  			$scope.autoRenderedServicesFinalList[i].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[i].taxAmt)).toFixed(2);
	  	    		  			$scope.autoRenderedServicesFinalList[i].selfPayable = $scope.autoRenderedServicesFinalList[i].netPay;
	  	    		  			$scope.autoRenderedServicesFinalList[i].creditPayable =parseFloat(0);
    		    			
    		    			
    		    			
    					}
    					else{
    						
    						$scope.autoRenderedServicesFinalList[i].concession = parseFloat(value.concession);
	    		    		$scope.autoRenderedServicesFinalList[i].patientClassPercentage = parseFloat(value.patientClassPercentage);
	    		    		
    						var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
    					//	$scope.disAmtCal = ((parseFloat(totalAmt)*parseFloat($scope.billWiseDiscountObj.discountAmountBillWise))/$scope.totalDisServiceWise).toFixed(2);
                            //$scope.disAmtCal = ((( parseFloat(totalAmt) - parseFloat(value.concession) ) * parseFloat($scope.billWiseDiscountObj.discountAmountBillWise))/$scope.totalDisServiceWise).toFixed(2);

    						//$scope.autoRenderedServicesFinalList[i].discountPercentage = ((parseFloat($scope.disAmtCal)/parseFloat(totalAmt))*100).toFixed(2);
    						//$scope.autoRenderedServicesFinalList[i].discount = $scope.disAmtCal;

    						var disPerTmp = parseFloat(0);
    						disPerTmp = (parseFloat($scope.billWiseDiscountObj.discountAmountBillWise)/parseFloat($scope.totalDisServiceWise))*100;
    						$scope.autoRenderedServicesFinalList[i].discount = (parseFloat(totalAmt) * disPerTmp)/100;
    						$scope.autoRenderedServicesFinalList[i].discountPercentage = disPerTmp;
    						
    						  //var withConDic =parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[i].discount);
                            var withConDic =parseFloat(totalAmt) - ( parseFloat($scope.autoRenderedServicesFinalList[i].concession) + parseFloat($scope.autoRenderedServicesFinalList[i].discount));
 
	  	    		  	    	var txpre = 0;
	  	    		  			if(value.taxPercentage != null || value.taxPercentage > 0){
	  	    		  				$scope.autoRenderedServicesFinalList[i].taxPercentage = value.taxPercentage;
	  	    		  				txpre = value.taxPercentage;
	  	    		  			}else{
	  	    		  					$scope.autoRenderedServicesFinalList[i].taxPercentage = 0;
	  	    		  					}
	  	    		  			$scope.autoRenderedServicesFinalList[i].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
	  	    		  			$scope.autoRenderedServicesFinalList[i].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[i].taxAmt)).toFixed(2);
	  	    		  			$scope.autoRenderedServicesFinalList[i].selfPayable = $scope.autoRenderedServicesFinalList[i].netPay;
	  	    		  			$scope.autoRenderedServicesFinalList[i].creditPayable =parseFloat(0);
	  	    		  			
    					}
    		    		
		   			 }
		   			if(i+1 == $scope.autoRenderedServicesFinalList.length){
		   				$scope.saveOrderPackage();
		   			}
		   			i++;
		   			});
		    	   
		    	   if($scope.billWiseDiscountObj.requestAmountBillWise > 0){
		    		var URI = BASE_URL + ROOT_URL
		    		+ "/billing/getUsersListHavingDiscRights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/amt/"+$scope.billWiseDiscountObj.discountAmountBillWise+"/per/"+$scope.billWiseDiscountObj.discountPercentageBillWise;;
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
		    
				   $scope.requestDiscountDiv = true;
				   $scope.isBillWiseDiscountApplied = true;
				   $scope.finalBill = 'N';
				   growl.warning("Please sent discount request to user",{
						title : ERROR_MSG
					});
		    	}else
		    		{
		    		 $scope.requestDiscountDiv = false;
		    		}
		    		return false;
				}
		    	else{
		    		
		    		if($scope.requestStatusObject != null){
		    			if($scope.requestStatusObject.reqStatus  == 2 && parseFloat($scope.requestStatusObject.approvedDiscAmt) >= parseFloat($scope.billWiseDiscountObj.discountAmountBillWise))
			    		{
		    				// for DD
			    		$scope.billWiseDiscountObj.requestAmountBillWise = 0;
			    		$scope.billWiseDiscountObj.requestUserId = "";
			    		$scope.requestDiscountDiv = false;
			    		// For DD
				    	var i = 0;
				    	angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
				    			if(value.status == 'A' && value.isDiscountApplicable == 'Y')
				    			{
		    					$scope.requestDiscountDiv = false;
								$scope.billWiseDiscountObj.requestAmountBillWise = 0;
								$scope.billWiseDiscountObj.requestUserId = '';
								
		    					$scope.autoRenderedServicesFinalList[i].discountCategoryId = $scope.billWiseDiscountObj.discountCategoryId.toString();
		    		    		if($scope.discountRadio == 1)
		    					{
                                    $scope.autoRenderedServicesFinalList[i].concession = parseFloat(value.concession);
                                    $scope.autoRenderedServicesFinalList[i].patientClassPercentage = parseFloat(value.patientClassPercentage);

		    		    			$scope.autoRenderedServicesFinalList[i].discountPercentage = $scope.billWiseDiscountObj.discountPercentageBillWise;
		    		    			var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
		    		    			//$scope.disAmtCal = ((parseFloat(totalAmt)*parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise))/100).toFixed(2);
                                    $scope.disAmtCal = (((parseFloat(totalAmt)-parseFloat(value.concession))*parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise))/100).toFixed(2);

		    		    			$scope.autoRenderedServicesFinalList[i].discount = $scope.disAmtCal;
		    		    			
		    		    			  var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[i].discount) + parseFloat($scope.autoRenderedServicesFinalList[i].concession));
			  	    		  	        
			  	    		  	    	var txpre = 0;
			  	    		  			if(value.taxPercentage != null || value.taxPercentage > 0){
			  	    		  				$scope.autoRenderedServicesFinalList[i].taxPercentage = value.taxPercentage;
			  	    		  				txpre = value.taxPercentage;
			  	    		  			}else{
			  	    		  					$scope.autoRenderedServicesFinalList[i].taxPercentage = 0;
			  	    		  					}
			  	    		  			$scope.autoRenderedServicesFinalList[i].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
			  	    		  			$scope.autoRenderedServicesFinalList[i].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[i].taxAmt)).toFixed(2);
			  	    		  			$scope.autoRenderedServicesFinalList[i].selfPayable = $scope.autoRenderedServicesFinalList[i].netPay;
			  	    		  			$scope.autoRenderedServicesFinalList[i].creditPayable =parseFloat(0);
			  	    		  			
		    					}
		    					else{
		    						
                                    $scope.autoRenderedServicesFinalList[i].concession = parseFloat(value.concession);
                                    $scope.autoRenderedServicesFinalList[i].patientClassPercentage = parseFloat(value.patientClassPercentage);

                                    
		    						var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
		    						//$scope.disAmtCal = ((parseFloat(totalAmt)*parseFloat($scope.billWiseDiscountObj.discountAmountBillWise))/$scope.totalDisServiceWise).toFixed(2);
                                    //$scope.disAmtCal = (((parseFloat(totalAmt)-parseFloat(value.concession))*parseFloat($scope.billWiseDiscountObj.discountAmountBillWise))/$scope.totalDisServiceWise).toFixed(2);

		    						//$scope.autoRenderedServicesFinalList[i].discountPercentage = ((parseFloat($scope.disAmtCal)/parseFloat(totalAmt))*100).toFixed(2);
		    						//$scope.autoRenderedServicesFinalList[i].discount = $scope.disAmtCal;
		    						

		    						var disPerTmp = parseFloat(0);
		    						disPerTmp = (parseFloat($scope.billWiseDiscountObj.discountAmountBillWise)/parseFloat($scope.totalDisServiceWise))*100;
		    						$scope.autoRenderedServicesFinalList[i].discount = (parseFloat(totalAmt) * disPerTmp)/100;
		    						$scope.autoRenderedServicesFinalList[i].discountPercentage = disPerTmp;
		    						
		    						
		    						 var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[i].discount) + parseFloat($scope.autoRenderedServicesFinalList[i].concession));
			  	    		  	        
			  	    		  	    	var txpre = 0;
			  	    		  			if(value.taxPercentage != null || value.taxPercentage > 0){
			  	    		  				$scope.autoRenderedServicesFinalList[i].taxPercentage = value.taxPercentage;
			  	    		  				txpre = value.taxPercentage;
			  	    		  			}else{
			  	    		  					$scope.autoRenderedServicesFinalList[i].taxPercentage = 0;
			  	    		  					}
			  	    		  			$scope.autoRenderedServicesFinalList[i].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
			  	    		  			$scope.autoRenderedServicesFinalList[i].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[i].taxAmt)).toFixed(2);
			  	    		  			$scope.autoRenderedServicesFinalList[i].selfPayable = $scope.autoRenderedServicesFinalList[i].netPay;
			  	    		  			$scope.autoRenderedServicesFinalList[i].creditPayable =parseFloat(0);
			  	    		  			
		    					}
		    		    		//$scope.autoRenderedServicesFinalList[i].concession = parseFloat(0);
		    		    		//$scope.autoRenderedServicesFinalList[i].concessionPer = parseFloat(0);
		    			
					    	     }
					    			if(i+1 == $scope.autoRenderedServicesFinalList.length){
					    				$scope.saveOrderPackage();
					    			}
					    		i++;
		    		
				    		});
			    		}
		    			else{
		    				
		    				  growl.warning("You have approved only "+parseFloat($scope.requestStatusObject.approvedDiscAmt).toFixed(2),{
		  						title : ERROR_MSG
		  					});
		    				  return false;
		    			}
		    	}else{
		    		// for DD
		    		$scope.billWiseDiscountObj.requestAmountBillWise = 0;
		    		$scope.billWiseDiscountObj.requestUserId = "";
		    		$scope.requestDiscountDiv = false;
		    		// For DD
			    	var i = 0;
			    	angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
			    			if(value.status == 'A' && value.isDiscountApplicable == 'Y')
			    			{
	    					$scope.requestDiscountDiv = false;
							$scope.billWiseDiscountObj.requestAmountBillWise = 0;
							$scope.billWiseDiscountObj.requestUserId = '';
							
	    					$scope.autoRenderedServicesFinalList[i].discountCategoryId = $scope.billWiseDiscountObj.discountCategoryId.toString();
	    		    		if($scope.discountRadio == 1)
	    					{
                                $scope.autoRenderedServicesFinalList[i].concession = parseFloat(value.concession);
                                $scope.autoRenderedServicesFinalList[i].patientClassPercentage = parseFloat(value.patientClassPercentage);

	    		    			$scope.autoRenderedServicesFinalList[i].discountPercentage = $scope.billWiseDiscountObj.discountPercentageBillWise;
	    		    			var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
	    		    			//$scope.disAmtCal = ((parseFloat(totalAmt)*parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise))/100).toFixed(2);
                                $scope.disAmtCal = (((parseFloat(totalAmt)-parseFloat(value.concession))*parseFloat($scope.billWiseDiscountObj.discountPercentageBillWise))/100).toFixed(2);

	    		    			$scope.autoRenderedServicesFinalList[i].discount = $scope.disAmtCal;
	    		    			
	    		    			var withConDic =parseFloat(totalAmt) - (parseFloat($scope.autoRenderedServicesFinalList[i].concession) + parseFloat($scope.autoRenderedServicesFinalList[i].discount));
	  	    		  	        
	  	    		  	    	var txpre = 0;
	  	    		  			if(value.taxPercentage != null || value.taxPercentage > 0){
	  	    		  				$scope.autoRenderedServicesFinalList[i].taxPercentage = value.taxPercentage;
	  	    		  				txpre = value.taxPercentage;
	  	    		  			}else{
	  	    		  					$scope.autoRenderedServicesFinalList[i].taxPercentage = 0;
	  	    		  					}
	  	    		  			$scope.autoRenderedServicesFinalList[i].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
	  	    		  			$scope.autoRenderedServicesFinalList[i].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[i].taxAmt)).toFixed(2);
	  	    		  			$scope.autoRenderedServicesFinalList[i].selfPayable = $scope.autoRenderedServicesFinalList[i].netPay;
	  	    		  			$scope.autoRenderedServicesFinalList[i].creditPayable =parseFloat(0);
	  	    		  			
	    					}
	    		    		else{
	    						
	    						$scope.autoRenderedServicesFinalList[i].concession = parseFloat(value.concession);
		    		    		$scope.autoRenderedServicesFinalList[i].patientClassPercentage = parseFloat(value.patientClassPercentage);
		    		    		
	    						var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
	    						//$scope.disAmtCal = (((parseFloat(totalAmt)-parseFloat(value.concession))*parseFloat($scope.billWiseDiscountObj.discountAmountBillWise))/$scope.totalDisServiceWise).toFixed(2);
	    						//$scope.autoRenderedServicesFinalList[i].discountPercentage = ((parseFloat($scope.disAmtCal)/parseFloat(totalAmt))*100).toFixed(2);
	    						//$scope.autoRenderedServicesFinalList[i].discount = $scope.disAmtCal;
	    						var disPerTmp = parseFloat(0);
	    						disPerTmp = (parseFloat($scope.billWiseDiscountObj.discountAmountBillWise)/parseFloat($scope.totalDisServiceWise))*100;
	    						$scope.autoRenderedServicesFinalList[i].discount = (parseFloat(totalAmt) * disPerTmp)/100;
	    						$scope.autoRenderedServicesFinalList[i].discountPercentage = disPerTmp;
	    						
	    						  var withConDic =parseFloat(totalAmt) - ( parseFloat($scope.autoRenderedServicesFinalList[i].concession) + parseFloat($scope.autoRenderedServicesFinalList[i].discount));
		  	    		  	        
		  	    		  	    	var txpre = 0;
		  	    		  			if(value.taxPercentage != null || value.taxPercentage > 0){
		  	    		  				$scope.autoRenderedServicesFinalList[i].taxPercentage = value.taxPercentage;
		  	    		  				txpre = value.taxPercentage;
		  	    		  			}else{
		  	    		  					$scope.autoRenderedServicesFinalList[i].taxPercentage = 0;
		  	    		  					}
		  	    		  			$scope.autoRenderedServicesFinalList[i].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
		  	    		  			$scope.autoRenderedServicesFinalList[i].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[i].taxAmt)).toFixed(2);
		  	    		  			$scope.autoRenderedServicesFinalList[i].selfPayable = $scope.autoRenderedServicesFinalList[i].netPay;
		  	    		  			$scope.autoRenderedServicesFinalList[i].creditPayable =parseFloat(0);
	    					}
	    		    		//$scope.autoRenderedServicesFinalList[i].concession = parseFloat(0);
	    		    		//$scope.autoRenderedServicesFinalList[i].concessionPer = parseFloat(0);
	    			
				    	     }
				    			if(i+1 == $scope.autoRenderedServicesFinalList.length){
				    				$scope.saveOrderPackage();
				    			}
				    		i++;
	    		
			    		});
		    	}
		       }
    	
    }
    
    
    
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
			$scope.groupWiseDisFlag = true;
	    	$scope.disPerPopupForGroup = '';
			$scope.disAmtPopupForGroup = '';
			$scope.disCategoryIdForGroup = '';
			$scope.disPerPopupForGroupFull = 0;
	    	console.log($scope.autoRenderedServicesFinalList);
	    	$scope.groupWiseDiscountServicesList = [];
	    	angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
	    		
	    		if(value.isDiscountApplicable == 'Y'){
	    		var temp = {specialityId :value.serviceSpecialityId,specialityName:value.specialityName};
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
	    		var newArrPop = angular.copy(value);
	    		$scope.groupWiseDiscountServicesList.push(newArrPop);
	    	}
	    		
	    	});
	    	
	    	$scope.groupWiseServicesSelectedList.services = [];
	    	$scope.chkFlgGroup = 'N';
	    	
	    }
    
		 $scope.serachGroupWiseServicesClear = function()
		    {
		    	$scope.disPerPopupForGroup = '';
				$scope.disAmtPopupForGroup = '';
				$scope.disCategoryIdForGroup = '';
		    	$scope.serviceSpecialityIdForGroup = '';
		    	$scope.serviceSubSpecialityIdForGroup = '';
		    	$scope.disPerPopupForGroupFull = 0;
		    	$scope.chkFlgGroup = 'N';
		    	$scope.groupWiseServicesSelectedList.services = [];
		    	$scope.groupWiseDiscountServicesList  = [];
		    	
		    	
		    	angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
		    		
		    		if(value.isDiscountApplicable == 'Y'){
		    		var temp = {specialityId :value.serviceSpecialityId,specialityName:value.specialityName};
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
		    		var newArrPop = angular.copy(value);
		    		$scope.groupWiseDiscountServicesList.push(newArrPop);
		    	}
		    		
		    	});
		    	
		    }
			$scope.getSpecialityByDoctorMain = function(docId,idx)
			{
				if(docId > 0){
			var specDocObj = {
			    			unitId:$scope.unitId,
			    			organizationId:$scope.organizationId,
			    			doctorId:docId
			    }
			    var URI = BASE_URL + ROOT_URL
				+ GETSPECIALITYLISTBYDOCTORID;
		BillingGenericService.serviceAction(METHOD_POST, URI,specDocObj,
						NOTIFICATION_MSG_STATUS_FALSE)
				.then(function(response) {
							console.log(response);
							
							// $rootScope.startLoader();
							if (response.data.status == "success")
								{
								$scope.autoRenderedServicesFinalList[idx].doctorSpecialityList = response.data.listObject;
								if($scope.autoRenderedServicesFinalList[idx].doctorSpecialityId == '' || $scope.autoRenderedServicesFinalList[idx].doctorSpecialityId == null || $scope.autoRenderedServicesFinalList[idx].doctorSpecialityId == undefined)
									{
									$scope.autoRenderedServicesFinalList[idx].doctorSpecialityId = response.data.listObject[0].specialityId;
									$scope.autoRenderedServicesFinalList[idx].specialityName = response.data.listObject[0].specialityName;
									}
								}
						});
			}
			
		 }
			
			
			// Share Development start
		    $scope.calculateShareAmount = function(item,$index){
		        console.log(item);
		        //return false;
		        
		        
		        
		        var data = {
		            "visitTypeId" : $scope.visitTypeId,
		            "billingBedCategoryId" :($scope.visitTypeId==1)?$scope.opdBillingCatId:$scope.encounterDetails.bedBillingCategoryId,//default op,
		            "patientTypeId" : $scope.patientDetails.patientTypeId,
		            "paymentEntitlementTypeId" :  $scope.encounterDetails.paymentEntitlementTypeId,
		            "referralId" : $scope.encounterDetails.referralId,
		            listOrderDetailsMasterDto : []
		        };
		        var newArr = {
		                ordDiscount:item.discount,
		                ordConcession : item.concession,
		                ordDocId : item.doctorId,
		                ordDocSplId : item.doctorSpecialityId,
		                encounterId : $scope.visitTypeId!=1?$scope.admissionDetails.admissionId:$scope.encounterDetails.encounterId,
		                orderDetailsId : item.orderDetailsId,
		                orgId : $scope.organizationId,
		                orgUnitId : $scope.unitId,
		                serviceId : item.serviceMasterId,
		                departmentId : item.serviceSpecialityId,
		                ordTotalAmt : item.totalAmt,
		                tariffId : item.tariffId,
		                isDocReq : item.isDoctorRequired,
		                isRefDoctorShare:item.isRefDoctorShare ,
		                discountOwnership: item.discountOwnership,
		                orderDate : item.orderDate,
		                
		        };
		        var orderDetailsList = [];
		        orderDetailsList.push(newArr);
		        data.listOrderDetailsMasterDto = orderDetailsList;
		         
		        $rootScope.startLoader();
		        var URI = BASE_URL + ROOT_URL + "/api/billing/order/docShare";
		        
		        BillingGenericService.serviceAction(METHOD_POST,URI,data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
		            if(response.data.status == "success"){
		                $scope.autoRenderedServicesFinalList[$index].docSharePer = response.data.object.listOrderDetailsMasterDto[0].docSharePer  !=null?response.data.object.listOrderDetailsMasterDto[0].docSharePer: 0;
		                $scope.autoRenderedServicesFinalList[$index].hospitalShare = response.data.object.listOrderDetailsMasterDto[0].hospitalShare  !=null ?response.data.object.listOrderDetailsMasterDto[0].hospitalShare:0;
		                $scope.autoRenderedServicesFinalList[$index].refDocSharePer = response.data.object.listOrderDetailsMasterDto[0].refDocSharePer  !=null ? response.data.object.listOrderDetailsMasterDto[0].refDocSharePer : 0;
		                $scope.autoRenderedServicesFinalList[$index].discountOwnership = response.data.object.listOrderDetailsMasterDto[0].discountOwnership > 0 ? response.data.object.listOrderDetailsMasterDto[0].discountOwnership : 0;
		                //$rootScope.stopLoader();
		            }else{
		                $rootScope.stopLoader();
		            }
		        });
		    }
		    
		// SHare Development end

		    
		    
			$scope.getSpecialityByDoctorMainChange = function(docId,idx)
			{
				$scope.autoRenderedServicesFinalList[idx].doctorSpecialityId = null;
			var specDocObj = {
			    			unitId:$scope.unitId,
			    			organizationId:$scope.organizationId,
			    			doctorId:docId
			    }
			    var URI = BASE_URL + ROOT_URL
				+ GETSPECIALITYLISTBYDOCTORID;
		BillingGenericService.serviceAction(METHOD_POST, URI,specDocObj,
						NOTIFICATION_MSG_STATUS_FALSE)
				.then(function(response) {
							console.log(response);
							
							// $rootScope.startLoader();
							if (response.data.status == "success")
								{
								// $scope.docSpecialityList =
								// response.data.listObject;autoRenderedServicesFinalList
								$scope.autoRenderedServicesFinalList[idx].doctorSpecialityList = response.data.listObject;
								$scope.autoRenderedServicesFinalList[idx].doctorSpecialityId = response.data.listObject[0].specialityId;
								$scope.autoRenderedServicesFinalList[idx].specialityName = response.data.listObject[0].specialityName;
								/*$scope.docSplObj = $.grep($scope.doctorsList, function (docList) {
						            return docList.doctorId == docId;
						           })[0];
									if($scope.docSplObj!=null && $scope.docSplObj!=undefined ){
										 $scope.servicesListPopup[idx].doctorSpecialityId = $scope.docSplObj.specialityId;
									}*/
								
								$scope.calculateShareAmount($scope.autoRenderedServicesFinalList[idx],idx)
								}
						});
		 }
		  $scope.serachGroupWiseServices = function()
		    {
		    	$scope.disPerPopupForGroup = '';
				$scope.disAmtPopupForGroup = '';
				$scope.disCategoryIdForGroup = '';
				$scope.disPerPopupForGroupFull = 0;
		    	$scope.groupWiseServicesSelectedList.services = [];
		    	
		    	
		    	$scope.groupWiseDiscountServicesList = [];
		    	var v = 1;
		    	angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
		    		
		    		if(value.isDiscountApplicable == 'Y'){
		    		var temp = {specialityId :value.serviceSpecialityId,specialityName:value.specialityName};
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
		    		var newArrPop = angular.copy(value);
		    		$scope.groupWiseDiscountServicesList.push(newArrPop);
		    	}
		    		if(v == $scope.autoRenderedServicesFinalList.length){
		    			if($scope.serviceSpecialityIdForGroup > 0 && $scope.serviceSubSpecialityIdForGroup > 0)
		        		{
		        			$scope.groupWiseDiscountServicesList = [];
		        			var i = 0;
		        			angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
		            			if(value.serviceSpecialityId == $scope.serviceSpecialityIdForGroup && value.serviceSubSpecialityId == $scope.serviceSubSpecialityIdForGroup && value.isDiscountApplicable == 'Y')
		            				{
		            				var newArrPop = angular.copy($scope.autoRenderedServicesFinalList[i]);
		            	    		$scope.groupWiseDiscountServicesList.push(newArrPop);
		            				}
		        				
		        	    		i++;
		        	    	});
		        			
		        			
		        			
		        		}
		        	else if($scope.serviceSpecialityIdForGroup > 0)
		        		{
		    	    		$scope.groupWiseDiscountServicesList = [];
		    				var i = 0;
		    				angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
		    	    			if(value.serviceSpecialityId == $scope.serviceSpecialityIdForGroup && value.isDiscountApplicable == 'Y')
		    	    				{
		    	    				var newArrPop = angular.copy($scope.autoRenderedServicesFinalList[i]);
		            	    		$scope.groupWiseDiscountServicesList.push(newArrPop);
		    	    				}
		    					
		    		    		i++;
		    		    	});
		        		}
		    		}
		    		v++;
		    	});
		    }
    
		  $scope.getDisPerPopupForGroup = function(disPer)
		    {
			  if(disPer == "" || disPer == null || disPer == undefined){
		    		disPer = 0;
		    	}
		    	var len = $scope.groupWiseServicesSelectedList.services.length;
		    	if(len == 0)
				{
					growl.warning("Please select at least one service!",{
						title : ERROR_MSG
					});
					$scope.disPerPopupForGroup = '';
					$scope.disPerPopupForGroupFull = 0;
		    		return false;
				}
		    	else if(parseFloat(disPer) > 100){
		    		growl.warning("Percentage should be less then or equal to 100 %.",{
						title : ERROR_MSG
					});
		    		$scope.disPerPopupForGroup = '';
					$scope.disAmtPopupForGroup  = '';
					$scope.disCategoryIdForGroup = '';
					$scope.disPerPopupForGroupFull = 0;
		    		return false;
		    	}
		    	else
		    		{
		    		$scope.totalAmtPopupAllServices = 0;
		    		for(var i = 0;i < len;i++)
		    		{
		    			// alert($scope.groupWiseServicesSelectedList.services[i]);
			    		angular.forEach($scope.groupWiseDiscountServicesList, function (value, key) {
			    			if(value.orderDetailsId == $scope.groupWiseServicesSelectedList.services[i])
			    				{
			    			     	var finalTotalAmt = parseFloat(value.totalAmt) - parseFloat(value.concession);
			    				    $scope.totalAmtPopupAllServices =(parseFloat($scope.totalAmtPopupAllServices) + parseFloat(finalTotalAmt)).toFixed(2);
			    				}
							
				    	});
		    		}
		    	}
		    		
		    	var calDicAmt = ($scope.totalAmtPopupAllServices*disPer/100).toFixed(2);	
				$scope.disAmtPopupForGroup = calDicAmt;
				$scope.disPerPopupForGroup = disPer;
				$scope.disPerPopupForGroupFull = disPer;
		    }
  
    
		  $scope.getDisAmtPopupForGroup = function(disAmt)
		    {
			  if(disAmt == "" || disAmt == null || disAmt == undefined){
		    		disAmt = 0;
		    	}
		    	var len = $scope.groupWiseServicesSelectedList.services.length;
		    	if(len == 0)
				{
					growl.warning("Please select at least one service!",{
						title : ERROR_MSG
					});
					$scope.disAmtPopupForGroup  = '';
		    		return false;
				}
		    	else
		    		{
		    		
		    		$scope.totalAmtPopupAllServices = 0;
		    		for(var i = 0;i < len;i++)
		    		{
		    			// alert($scope.groupWiseServicesSelectedList.services[i]);
			    		angular.forEach($scope.groupWiseDiscountServicesList, function (value, key) {
			    			if(value.orderDetailsId == $scope.groupWiseServicesSelectedList.services[i])
			    				{
			    				var finalTotalAmt = parseFloat(value.totalAmt) - parseFloat(value.concession);
			    					$scope.totalAmtPopupAllServices =(parseFloat($scope.totalAmtPopupAllServices) + parseFloat(finalTotalAmt)).toFixed(2);
			    				}
							
				    	});
			    		if(i+1 == len){
			    			if(parseFloat(disAmt) > parseFloat($scope.totalAmtPopupAllServices)){
			    				growl.warning("Amount should be less then or equal to total discount amount..",{
			    					title : ERROR_MSG
			    				});
			    				$scope.disAmtPopupForGroup  = '';
			    				$scope.disPerPopupForGroup = '';
			    				$scope.disCategoryIdForGroup = '';
			    				$scope.disPerPopupForGroupFull = 0;
			    	    		return false;
			    			}
			    		}
		    		}
		    		var disPer = ((parseFloat(disAmt)/$scope.totalAmtPopupAllServices)*100).toFixed(2);
		    		$scope.disPerPopupForGroupFull = (parseFloat(disAmt)/$scope.totalAmtPopupAllServices)*100;
		    		$scope.disPerPopupForGroup = disPer;
		    		$scope.disAmtPopupForGroup = disAmt;
		    		}
		    	
				
		    }
    $scope.clearDiscount = function()
    {
    	$scope.disPerPopupForGroup = '';
		$scope.disAmtPopupForGroup = '';
		$scope.disCategoryIdForGroup = '';
		$scope.disPerPopupForGroupFull = 0;
		
		if(!$scope.checked){
			$scope.chkFlgGroup = 'N';
		}
		if(groupWiseServicesSelectedList.services.length == 0){
			 $scope.groupWiseDisFlag  = true;
		}
    }
    
    $scope.addDiscountInDiscountListForGroup = function()
    {
    	if($scope.disPerPopupForGroup == undefined || $scope.disPerPopupForGroup == '' )
    		{
	    		growl.warning("All fields are required.",{
					title : ERROR_MSG
				});
	    		return false;
    		}
    	else if($scope.disAmtPopupForGroup == undefined || $scope.disAmtPopupForGroup == '')
    		{
	    		growl.warning("All fields are required.",{
					title : ERROR_MSG
				});
	    		return false;
    		}
    	else if($scope.disCategoryIdForGroup == undefined || $scope.disCategoryIdForGroup == '')
		{
    		growl.warning("All fields are required.",{
				title : ERROR_MSG
			});
    		return false;
		}
    	else
    		{
    		var len = $scope.groupWiseServicesSelectedList.services.length;
    			if(len == 0)
    				{
	    				growl.warning("Please select at least one service!",{
	    					title : ERROR_MSG
	    				});
	    	    		return false;
    				}
    			else
    				{
    			$scope.groupWiseDisFlag = false;
    				for(var i = 0;i < len;i++)
    	    		{
    					var j = 0;
	    				angular.forEach($scope.groupWiseDiscountServicesList, function (value, key) {
	    					var totalAmtPopupForGroup =0;
	    					var concessionPopupForGroup =0;
	    					var finalTotalAmtForGroup = 0;
	    					var calDicAmtForPopup = 0;
	    	    			if(value.orderDetailsId == $scope.groupWiseServicesSelectedList.services[i])
	    	    				{
	    	    					
	    	    					//test
	    	    				
                                $scope.groupWiseDiscountServicesList[j].concession =value.concession;
                                $scope.groupWiseDiscountServicesList[j].patientClassPercentage = value.patientClassPercentage;

	    						
	    	    				    var totalAmt = (parseFloat(1) * parseFloat(value.basePrice)).toFixed(2);
	    	    				    $scope.groupWiseDiscountServicesList[j].discountCategoryId = $scope.disCategoryIdForGroup;
	    	    				    
	    	    				    
	    	    				    $scope.groupWiseDiscountServicesList[j].discountPercentage = $scope.disPerPopupForGroup;
	    	    				    $scope.groupWiseDiscountServicesList[j].discountPercentageFull = $scope.disPerPopupForGroupFull;
	        		    			//var disAmtCal = ((parseFloat(totalAmt)*parseFloat($scope.disPerPopupForGroup))/100).toFixed(2);
                                    var disAmtCal = (((parseFloat(totalAmt) - parseFloat(value.concession)) *parseFloat($scope.disPerPopupForGroupFull))/100).toFixed(2);

	        		    			$scope.groupWiseDiscountServicesList[j].discount = disAmtCal;
	        		    			
	        		    			
	        		    			 //var withConDic =parseFloat(totalAmt) - parseFloat(disAmtCal);
                                    var withConDic =parseFloat(totalAmt) - ( parseFloat(value.concession) + parseFloat(disAmtCal));

		    	    			        
		    	    			    	var txpre = 0;
		    							if(value.taxPercentage != null || value.taxPercentage > 0){
		    								$scope.groupWiseDiscountServicesList[j].taxPercentage = value.taxPercentage;
		    								txpre = value.taxPercentage;
		    							}else{
		    								  $scope.groupWiseDiscountServicesList[j].taxPercentage = 0;
		    									}
		    						$scope.groupWiseDiscountServicesList[j].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
	    	    					//$scope.groupWiseDiscountServicesList[j].netPay = parseFloat((totalAmt -(parseFloat(disAmtCal)) + parseFloat(value.taxAmt))).toFixed(2);
                                    $scope.groupWiseDiscountServicesList[j].netPay = parseFloat((totalAmt -(parseFloat(value.concession) + parseFloat(disAmtCal)) + parseFloat(value.taxAmt))).toFixed(2);
		    						$scope.groupWiseDiscountServicesList[j].selfPayable = $scope.groupWiseDiscountServicesList[j].netPay;
	    	    					$scope.groupWiseDiscountServicesList[j].creditPayable =parseFloat(0);
	    							
	    							
	    	    					
	    	    					//test
	    	    				}
	    	    			
	    	    			j++;
	    		    	});
	    				
    				}
    			}
    		}
    	
    	
    }
    
    $scope.getPayeeCreditDetails = function(itemList)
    {
    	$scope.payeeCreditDetailslList = [];
    	$scope.payeeCreditDetailslList = itemList;
    }
    
    $scope.saveGroupDiscountToFinalList = function()
    {
    	var len = $scope.groupWiseServicesSelectedList.services.length;
		if(len == 0)
			{
				growl.warning("Please select at least one service!",{
					title : ERROR_MSG
				});
	    		return false;
			}
		else if($scope.disPerPopupForGroup == undefined || $scope.disPerPopupForGroup == '' )
		{
    		growl.warning("All fields are required.",{
				title : ERROR_MSG
			});
    		return false;
		}
		else if($scope.disAmtPopupForGroup == undefined || $scope.disAmtPopupForGroup == '')
			{
	    		growl.warning("All fields are required.",{
					title : ERROR_MSG
				});
	    		return false;
			}
		else if($scope.disCategoryIdForGroup == undefined || $scope.disCategoryIdForGroup == '')
		{
			growl.warning("All fields are required.",{
				title : ERROR_MSG
			});
			return false;
		}
		else
			{
			
    	for(var i = 0;i < len;i++)
		{
			angular.forEach($scope.groupWiseDiscountServicesList, function (value, key) {
			
    			if(value.orderDetailsId == $scope.groupWiseServicesSelectedList.services[i])
    				{
    				angular.forEach($scope.autoRenderedServicesFinalList, function (mainVal, mainKey) {
    					if(value.orderDetailsId == mainVal.orderDetailsId){
    						//$scope.autoRenderedServicesFinalList[mainKey].concession = 0;
    						//$scope.autoRenderedServicesFinalList[mainKey].patientClassPercentage = 0;
                            $scope.autoRenderedServicesFinalList[mainKey].concession =value.concession;
                            $scope.autoRenderedServicesFinalList[mainKey].patientClassPercentage = value.patientClassPercentage;
                            $scope.autoRenderedServicesFinalList[mainKey].discountCategoryId = value.discountCategoryId;
    						$scope.autoRenderedServicesFinalList[mainKey].discount = value.discount;
    						$scope.autoRenderedServicesFinalList[mainKey].discountPercentage = value.discountPercentageFull;
    						$scope.autoRenderedServicesFinalList[mainKey].totalAmt = value.totalAmt;
    						$scope.autoRenderedServicesFinalList[mainKey].netPay =value.netPay;
    						$scope.autoRenderedServicesFinalList[mainKey].discountDetailsList = [];
    					}
    				});
    				}
	    	});
			if(i == len -1){
				$scope.saveOrderPackage();
				
				if($scope.requestStatusObject != null){
					$scope.getCancelDiscountReqForDiscount();
					}
				 $scope.billWiseDiscountObj = {
							// "discountGivenBy":"",
							"discountGivenBy":$scope.userId,
							"discountCategoryId" : "",
							"discountAmountBillWise" : "",
							"discountPercentageBillWise" : "",
							"approvedAmountBillWise":"",
							"requestUserId":"",
							"requestAmountBillWise" : 0,
					}
				 $scope.requestDiscountDiv = false;
				 $scope.lineItemDiscountAmt = parseFloat(0);
				 $scope.billWiseDiscountObj.discountAmountBillWise =  parseFloat(0);
				 $scope.billWiseDiscountObj.requestAmountBillWise =   parseFloat(0);
			}
			
			}
    	angular.element('#groupWiseDiscountModal').modal('hide');
    	}	
    }
    
    
$scope.cancelPatientClassPopup = function(){
		
		$rootScope.startLoaderPopup();
		$scope.patientClassIdPopup = null;
		$scope.patientClassCancelReasonIdPopup = null;
		
	
		var obj={
				visitTypeId : $scope.visitTypeId,
				reasonIdForChangePatientClass : $scope.patientClassCancelReasonIdPopup,
				organizationId : $scope.organizationId,
				unitId : $scope.unitId,
				patientClassId:null
		}
		if($scope.visitTypeId == 1){
			 $scope.encounterDetails.patientClassId =null;
			 obj.visitId = $scope.encounterDetails.encounterId;
			}else{
				 $scope.admissionDetails.patientClassId =null;
				 obj.visitId = $scope.admissionDetails.admissionId;
			}
		
		var URI = BASE_URL + ROOT_URL + "/api/billing/changePatientClassId";
		BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
				{	
					if(response.data.status == "success")
					{
						$rootScope.stopLoaderPopup();
						
						if($scope.visitTypeId == 1){
							 $scope.encounterDetails.patientClassId =null;
							}else{
								 $scope.admissionDetails.patientClassId =null;
							}
						angular.element('#change_patient_class').modal('hide');
						angular.element('#confirmCancelPatientClass').modal('hide');
						var i = 1;
			    		angular.forEach($scope.autoRenderedServicesFinalList, function (service, key) 
							{
			    					
			    						$scope.autoRenderedServicesFinalList[key].concession = parseFloat(0);
			   						    $scope.autoRenderedServicesFinalList[key].patientClassPercentage = parseFloat(0);
			   						   // $scope.autoRenderedServicesFinalList[key].discount = 0;
                                        $scope.autoRenderedServicesFinalList[key].discountPercentage = service.discountPercentage;

			   						    
				    			        var totalAmt = (parseFloat(service.quantity) * parseFloat(service.basePrice)).toFixed(2);
				    			       
				    			        //$scope.autoRenderedServicesFinalList[key].discountPercentage = 0;
				    			        //var withConDic =parseFloat(totalAmt) - parseFloat($scope.autoRenderedServicesFinalList[key].discount);
                                        $scope.autoRenderedServicesFinalList[key].discountPercentage = service.discountPercentage;
                                        var withConDic =parseFloat(totalAmt) - ( parseFloat($scope.autoRenderedServicesFinalList[key].concession) + parseFloat($scope.autoRenderedServicesFinalList[key].discount));

				    			        
				    			    	var txpre = 0;
										if(service.taxPercentage != null || service.taxPercentage > 0){
											$scope.autoRenderedServicesFinalList[key].taxPercentage = service.taxPercentage;
											txpre = service.taxPercentage;
										}else{
												$scope.autoRenderedServicesFinalList[key].taxPercentage = 0;
												}
										$scope.autoRenderedServicesFinalList[key].taxAmt = parseFloat((parseFloat(withConDic) * txpre)/100).toFixed(2);
										$scope.autoRenderedServicesFinalList[key].netPay =   (parseFloat(withConDic) + parseFloat($scope.autoRenderedServicesFinalList[key].taxAmt)).toFixed(2);
										$scope.autoRenderedServicesFinalList[key].selfPayable = $scope.autoRenderedServicesFinalList[key].netPay;
										$scope.autoRenderedServicesFinalList[key].creditPayable =parseFloat(0);
			    					
								    
									
									if(i == $scope.autoRenderedServicesFinalList.length){
										//$scope.saveOrderPackage();
										if($scope.requestStatusObject == null){
											$scope.saveOrderPackage();
											$scope.requestDiscountDiv = false;
											$scope.requestApporivedDiscountDiv = false;
											$scope.discountDisabledFlagForRequest = false;
											$scope.requestAppUser = 0;
											$scope.approvedAmtApp = 0;
											$scope.requestAmtApp =0;
											$scope.discTypeDesc = "";
											$scope.statusApp = "";
											$scope.discountRadio = 2; 
											
											$scope.billWiseDiscountObj = {
													// "discountGivenBy":"",
													"discountGivenBy":$scope.userId,
													"discountCategoryId" : "",
													"discountAmountBillWise" : "",
													"discountPercentageBillWise" : "",
													"approvedAmountBillWise":"",
													"requestUserId":"",
													"requestAmountBillWise" : 0,
											}
											 $scope.requestStatusObject  = null;
										}else{
													$scope.getCancelDiscountReqForDiscount();
													$scope.saveOrderPackage();
											
										}
									}
									i++;
							});
			    		
					}
				});
	}
	
	$scope.cancelPatientClassClosePopup = function(){
		angular.element('#confirmCancelPatientClass').modal('hide');
	}
	
    $scope.cancelDiscountForServices = function()
    {
    	var i = 1;
    	var flag = false;
    	angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
    		if(parseFloat(value.discount) > 0){
    			flag = true;
    		}
    		if(i == $scope.autoRenderedServicesFinalList.length && flag == true){
    			angular.element('#cancelDiscountPopup').modal('show');
    		}
    		i++;
    	});
    	
    }
    
    
    //final bill Function
    $scope.checkFinalBill = function()
    {
    	if(parseFloat($scope.roundOffAmt)!=0 && $scope.roundOffAmt!=undefined && $scope.roundOffAmt!=null)
    		{
    		   if($scope.finalBill=='N')
    			   {
    			   
    			   angular.forEach($scope.autoRenderedServicesFinalList, function(value, key) {
    				   $scope.servicesSelectedList.services.push(value.serviceMasterId);
    				 });
    			   $scope.billWiseDiscountObj.discountAmountBillWise = 0;
    			   $scope.billWiseDiscountObj.discountPercentageBillWise = 0;
    			   $scope.finalCancelDiscount();
    			   $scope.calculationTest($scope.autoRenderedServicesFinalList,init);
    			   
    			   if($scope.requestStatusObject == null)
    				   {
    				    $scope.billWiseDiscountObj.requestAmountBillWise = 0;
    		    		$scope.billWiseDiscountObj.requestUserId = "";
    				    $scope.requestDiscountDiv = false;
    				   }
    			   
    			   }
    		}
    }
    
    $scope.finalCancelDiscount = function()
    {
    	var cancelDiscountObjectList = [];
    	var cancelRemoveDiscountList = [];
    	
			var j = 0;
			angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
				
				var totalAmt = 0;
				var dis = 0;
				var gstAmt = 0;
				var withConDic = 0;
    		
    				$scope.autoRenderedServicesFinalList[j].discountPercentage = 0;
					$scope.autoRenderedServicesFinalList[j].discountCategoryId = '';
					$scope.autoRenderedServicesFinalList[j].discount = 0;
					totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
					
					withConDic =parseFloat(totalAmt - (parseFloat(dis))).toFixed(2);
					gstAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);

					$scope.autoRenderedServicesFinalList[j].taxAmt = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
					$scope.autoRenderedServicesFinalList[j].netPay =   parseFloat((totalAmt -(parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
					$scope.autoRenderedServicesFinalList[j].selfPayable =   parseFloat((totalAmt -(parseFloat(dis)) + parseFloat(gstAmt))).toFixed(2);
					$scope.autoRenderedServicesFinalList[j].totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
		    		$scope.autoRenderedServicesFinalList[j].discountDetailsList = [];
		    		
    			if(j+1 == $scope.autoRenderedServicesFinalList.length){
    				if($scope.requestStatusObject != null){
						$scope.getCancelDiscountReqForDiscount();
						}
    				 $scope.saveOrderPackage();

						$scope.billWiseDiscountObj = {
								// "discountGivenBy":"",
								"discountGivenBy":$scope.userId,
								"discountCategoryId" : "",
								"discountAmountBillWise" : "",
								"discountPercentageBillWise" : "",
								"approvedAmountBillWise":"",
								"requestUserId":"",
								"requestAmountBillWise" : 0,
						}
						
    				 $scope.requestDiscountDiv = false;
					 $scope.lineItemDiscountAmt = parseFloat(0);
					 $scope.billWiseDiscountObj.discountAmountBillWise =  parseFloat(0);
					 $scope.billWiseDiscountObj.requestAmountBillWise =   parseFloat(0);
    			}
    			j++;
	    	});
    }
	// function for popup ok button call
	$scope.PopupOkBtn = function() {
		$scope.popUpFlag = false;
		$scope.saveBillings();
	}
	$scope.lineItemDiscountAmt = parseFloat(0); //12-10-2019
	$scope.saveBillings = function()
	{
		if(Object.keys($scope.autoRenderedServicesFinalList).length == 0)
			{	
				growl.warning(" No service to save..Please add service..!!",{
					title : ERROR_MSG
				});
				return false;
				
			}
		else{
			if($scope.billWiseDiscountObj.requestAmountBillWise > 0 && $scope.requestStatusObject != null){
			  if($scope.billWiseDiscountObj.requestUserId == "")
				{
					growl.warning("Please select request user for discount!",{
						title : ERROR_MSG
					});
					return false;
				}
			else
				{
				$scope.saveBillngNew();
				}
			
		}else{
			
			if($scope.userDiscountObject == null || $scope.userDiscountObject == undefined)
			{
				if($scope.requestStatusObject != null || $scope.requestStatusObject != undefined){
						if($scope.requestStatusObject.reqStatus == 1) // Already
																		// Pending
							{
							$scope.finalBill = 'N';
							 growl.warning("Discount request is pending..!!",{
									title : ERROR_MSG
								});
					    		return false;
							
							}else if($scope.requestStatusObject.reqStatus == 2){
								
								if($scope.totalDiscountSummary > $scope.requestStatusObject.approvedDiscAmt)
									{
										 growl.warning("Discount approved amount is "+$scope.requestStatusObject.approvedDiscAmt+".",{
												title : ERROR_MSG
											});
								    		return false;
									}
								else
									{
									 $scope.saveBillngNew();
									}
						
					}
			}
				else{
					if(($scope.userDiscountObject == null || $scope.userDiscountObject == undefined) &&  ($scope.totalDiscountSummary == 0 ||$scope.totalDiscountSummary == 0.0 || $scope.totalDiscountSummary == '' || $scope.totalDiscountSummary == null))
							{
						         $scope.saveBillngNew();
							}
					else{
							if($scope.totalDiscountSummary > 0 && ($scope.billWiseDiscountObj.requestUserId == null || $scope.billWiseDiscountObj.requestUserId == '' || $scope.billWiseDiscountObj.requestUserId == undefined)){
								$scope.listOfOldDisAmt = [];
								 $scope.isBillWiseDiscountApplied = true;
								 $scope.lineItemDiscountAmt = $scope.totalDiscountSummary;
								 //$scope.billWiseDiscountObj.discountAmountBillWise = $scope.lineItemDiscountAmt;
								 $scope.billWiseDiscountObj.discountAmountBillWise =0;
								 $scope.billWiseDiscountObj.discountPercentageBillWise =0;
								 
								 var disPerForUser = (parseFloat($scope.totalDiscountSummary)/parseFloat($scope.totalDisServiceWise) *100).toFixed(2);
								 
								 
						    		var URI = BASE_URL + ROOT_URL
						    		+ "/billing/getUsersListHavingDiscRights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/amt/"+$scope.totalDiscountSummary+"/per/"+disPerForUser;
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
						    
						           $scope.finalBill = 'N';
								   $scope.requestDiscountDiv = true;
								   $scope.billWiseDiscountObj.requestAmountBillWise =  $scope.lineItemDiscountAmt;
								   growl.warning("Please sent discount request to user",{
										title : ERROR_MSG
									});
						    		return false;
							
							}else{
								$scope.saveBillngNew();
							}
					    		
				  }
				}
		    		
			}
			else if($scope.userDiscountObject != null || $scope.userDiscountObject != undefined)
				{	
						// if(approvedAmtApp)
				if($scope.userDiscountObject != null){
		    		
					if($scope.userDiscountObject.discountType == 1){
						var fromDisAmt = (parseFloat($scope.totalDisServiceWise) * parseFloat($scope.userDiscountObject.fromDiscountPer))/100;
						var toDisAmt = (parseFloat($scope.totalDisServiceWise) * parseFloat($scope.userDiscountObject.toDiscountPer))/100;
						$scope.userDiscountObject.fromDiscountAmount = fromDisAmt;
						$scope.userDiscountObject.toDiscountAmount = toDisAmt;
					}
		    	}
						if(($scope.userDiscountObject.toDiscountAmount >= $scope.totalDiscountSummary) && ($scope.listOfOldDisAmt == undefined || Object.keys($scope.listOfOldDisAmt).length == 0))
							{
								 if($scope.billWiseDiscountObj.requestAmountBillWise == 0)
									 {
										 $scope.listOfOldDisAmt = [];
										 $scope.isBillWiseDiscountApplied = false;
										 $scope.requestDiscountDiv = false;
										 $scope.lineItemDiscountAmt = parseFloat(0);
										 $scope.billWiseDiscountObj.discountAmountBillWise =  parseFloat(0);
										 $scope.billWiseDiscountObj.requestAmountBillWise =   parseFloat(0);
										 $scope.discountRadio = 2;
										 $scope.saveBillngNew();
									 }
								 else{
									 $scope.discountRadio = 2;
									 $scope.saveBillngNew();
								 }
						    		
							  
							}
						else if(($scope.userDiscountObject.toDiscountAmount >= $scope.totalDiscountSummary) && $scope.listOfOldDisAmt.length > 0)
							{
							   $scope.saveBillngNew();
							}
						else
							{
								if($scope.requestStatusObject != null || $scope.requestStatusObject != undefined)
								{
										
									
									if($scope.requestStatusObject.reqStatus == 1) // Already
																					// Pending
									{
										$scope.finalBill = 'N';
									 growl.warning("Discount request is pending..!!",{
											title : ERROR_MSG
										});
							    		return false;
									
									}else if($scope.requestStatusObject.reqStatus == 2){
										
										if($scope.totalDiscountSummary > $scope.requestStatusObject.approvedDiscAmt)
											{
										    	   
												 growl.warning("Discount approved amount is "+$scope.requestStatusObject.approvedDiscAmt+".",{
														title : ERROR_MSG
													});
										    		return false;
											}
										else if($scope.totalDiscountSummary <= $scope.requestStatusObject.approvedDiscAmt)
											{
											   $scope.saveBillngNew();
											}
								
							}
								
								}else
									{
									if($scope.totalDiscountSummary > 0 && ($scope.billWiseDiscountObj.requestUserId == null || $scope.billWiseDiscountObj.requestUserId == '' || $scope.billWiseDiscountObj.requestUserId == undefined)){
										$scope.listOfOldDisAmt = [];
										 $scope.isBillWiseDiscountApplied = true;
										 $scope.lineItemDiscountAmt = $scope.totalDiscountSummary;
										 $scope.billWiseDiscountObj.discountAmountBillWise = $scope.lineItemDiscountAmt;
										 $scope.billWiseDiscountObj.discountPercentageBillWise =  (parseFloat($scope.lineItemDiscountAmt)/(parseFloat($scope.totalDisServiceWise))*100).toFixed(2)
										// $scope.funDiscountPercentageBillWise();
										 $scope.discountRadio = 2;
										 $scope.finalBill = 'N';
								    		var URI = BASE_URL + ROOT_URL
								    		+ "/billing/getUsersListHavingDiscRights/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/amt/"+$scope.billWiseDiscountObj.discountAmountBillWise+"/per/"+$scope.billWiseDiscountObj.discountPercentageBillWise;
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
								    
										   $scope.requestDiscountDiv = true;
										   $scope.billWiseDiscountObj.requestAmountBillWise =  $scope.lineItemDiscountAmt;
										   growl.warning("Please sent discount request to user",{
												title : ERROR_MSG
											});
								    		return false;
										}else{
											$scope.saveBillngNew();
										}
									}
							}
				}
			else
				{
				$scope.saveBillngNew();
				}
			
		}// for DD
	  }
	}
	

	$scope.saveBillngNew = function(){
	
	    var discountDetailForLine = [];
		$scope.btnDisabled = false;
		var data={
			
				patientId :$scope.encounterDetails.patientId,
				encounterId :$scope.billObj.encounterId,
				admissionId :$scope.billObj.admissionId,
				createdBy :$scope.userId,
				createdDate :new Date().getTime(),
				updatedBy :$scope.userId,
				updatedDate :new Date().getTime(),
				orderDate :new Date().getTime(),
				organisationId :$scope.organizationId,
				unitId :$scope.unitId,
				isFinalBill : $scope.finalBill,
				ordDoctorId :$scope.encounterDetails.doctorId,
				visitTypeId : $scope.billObj.visitTypeId,
				billDateTime : new Date().getTime(),
				netAmount : parseFloat(parseFloat($scope.netPayableSummary)+parseFloat($scope.roundOffAmt)).toFixed(2),
				discountAmount : $scope.totalDiscountSummary,
				concessionAmount : $scope.totalConcessionSummary,
				gst : $scope.totalGstTaxSummary,
				totalBillAmount : $scope.totalAmountSummary,
				roundOff : $scope.roundOffAmt,
				isCreditBill:'N',
				billCancelled : 'N',
				isFinancialClearance : 'N',
				billRemark : $scope.billRemark,
				billStatusId : '1', // 1. Pending,
				selfPayable : $scope.totalSelfPayble,
				creditPayable : $scope.totalCreditPayble,
				cancelledDiscountsList : $scope.removeDiscountList,
				tariffId : $scope.defaultSelfTariffId,
				paymentEntitlementId : $scope.encounterDetails.paymentEntitlementTypeId,
				visitTypeId : $scope.billObj.visitTypeId,
				//patientTypeId : $scope.encounterDetails.patientCategoryId,
				patientTypeId :$scope.patientDetails.patientTypeId,
				billingBedCategoryId : $scope.encounterDetails.bedBillingCategoryId, 
				isInterimBill :'N',
				isDrNote:"Y",
				isInvoiceCreation:"N",	
				isIntermediateBill:"N",
				counterId:$rootScope.counterId,
		        drNoteAgainstBillId:$scope.billObj.billingMasterId,
		        encounterSpecialityId:$scope.encounterDetails.specialityId,
				lineLevelDiscountOrderList : discountDetailForLine
		    
				
		};
		
		if($scope.isBillWiseDiscountApplied == true)
		{
			data.isDiscountApplied = 'Y';
		}
	
		if($scope.requestStatusObject !=null )
		{
			data.reqDiscId = $scope.requestStatusObject.reqDiscId;
		}
	   
		//for DD

		if($scope.billWiseDiscountObj.requestAmountBillWise > 0 ){
			data.reqDisAmt = $scope.billWiseDiscountObj.requestAmountBillWise- (parseFloat($scope.roundOffAmt));
			data.requestSentTo = $scope.billWiseDiscountObj.requestUserId;
			data.discType = $scope.billWiseDiscountObj.discountCategoryId;
		}
		
		var billPayeeList =[];
		if($scope.totalSelfPayble > 0)
			{
				var billPayeeArr ={
						payeeId : $scope.selfPayeeId,
						billAmount : $scope.totalSelfPayble
				}
				billPayeeList.push(billPayeeArr);
			}
		if($scope.totalCreditPayble > 0)
			{
				var billPayeeArr ={
						payeeId : $scope.encounterDetails.payeeId,
						billAmount : $scope.totalCreditPayble
				}
				billPayeeList.push(billPayeeArr);
			}
		
		data.billPayeeDetailsDtosList = billPayeeList;
		
		console.log('$scope.autoRenderedServicesFinalList',$scope.autoRenderedServicesFinalList);
		//return false;
		
		var saveOrderDetailsObj = [];
		angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
			var saveArr = {};
			if(value.orderDetailsId > 0){
				saveArr.orderDetailsId = value.orderDetailsId;
			}
            saveArr.discountOwnership = value.discountOwnership;

			saveArr.isDocReq = value.isDoctorRequired;
			saveArr.markupStatus = value.markupStatus;
			saveArr.patientClassPercentage = value.patientClassPercentage;
			saveArr.orgId = $scope.organizationId;
			saveArr.orgUnitId  = $scope.unitId;
			saveArr.orderId = value.orderId;
			saveArr.orderQty = value.quantity;
			saveArr.serviceId = value.serviceMasterId;
			saveArr.priorityId = value.priorityId;
			saveArr.isOutsourced = value.isOutsourced;
			//saveArr.orderDate = new Date().getTime();
			saveArr.orderDate =   (value.orderDate !=null && value.orderDate!=undefined && value.orderDate!='')?value.orderDate:new Date().getTime();
			saveArr.orderSchDate = new Date().getTime();
			saveArr.serviceAmt = value.basePrice;
            saveArr.dummyServiceAmt = value.dummyServiceAmt;
			saveArr.ordConcession = value.concession;
			saveArr.ordDiscount = value.discount;
			saveArr.netAmount = value.netPay;
            saveArr.ordDocSplId = value.doctorSpecialityId;
			saveArr.taxId = value.taxId;
			saveArr.taxPer  = value.taxPercentage;
			saveArr.taxAmount = value.taxAmt;
			saveArr.selfPayable = value.selfPayable;
			saveArr.creditPayable = value.creditPayable;
			saveArr.payeeId = $scope.payeeId;
			saveArr.ordTotalAmt =value.totalAmt;
			saveArr.tariffId = value.tariffId;
			saveArr.procedureOrderMstId = value.procedureOrderMstId > 0 ? value.procedureOrderMstId  : null;
			saveArr.isProcedureService = value.isProcedureService!=null ? value.isProcedureService  : 'N';
			saveArr.isPostedWhileDischarge = value.isPostedWhileDischarge!=null ? value.isPostedWhileDischarge : 'N';
			saveArr.billingClassId = '1';
			saveArr.ordDocId = value.doctorId;
			//saveArr.serviceRendered = ($scope.billObj.visitTypeId==2||$scope.billObj.visitTypeId==3 || $scope.billObj.visitTypeId==4)?1:value.serviceRendered;
			saveArr.serviceRendered = value.serviceRendered;
			saveArr.serviceRenderingDeptId = value.serviceRenderingDeptId;
			saveArr.ordRenderDatetime = new Date().getTime();
			saveArr.serviceIsBilled = '0';
			saveArr.status = value.status;
			saveArr.updatedBy = $scope.userId;
			saveArr.updatedDate =new Date().getTime();
			saveArr.createdBy  = value.createdBy;
			saveArr.createdDate  = value.createdDate;
			saveArr.oldNetPay =  parseFloat(value.oldNetPay).toFixed(2);
			saveArr.ordCancelled = value.ordCancelled;
			
			saveArr.ordCancelReasonId  = value.ordCancelReasonId;
			saveArr.ordCancelDatetime  = value.ordCancelDatetime;
			saveArr.ordCancelRemark  = value.ordCancelRemark;
			saveArr.oldOrdDtlId = value.oldOrdDtlId;
            saveArr.discountCategoryId = value.discountCategoryId;
			//Ganesh
			saveArr.oldCreditPayable = value.creditPayableOldC;
			saveArr.oldSelfPayable = value.selfPayableOldC;
			saveArr.isInvestigationOrder = value.isInvestigationOrder;
            saveArr.isDiscountApplicable = value.isDiscountApplicable;
			saveArr.orderEmergencyFlag = 'N';
			saveArr.isDrug = 'N';
			saveArr.isPackage = typeof(value.isPackage) != "undefined" ? value.isPackage : false;
			saveArr.packageId = typeof(value.packageMasterId) != "undefined" && value.packageMasterId != null ? value.packageMasterId : value.packageId;
			saveArr.packageTypeId = typeof(value.packageTypeId) != "undefined" ? value.packageTypeId : null;
			saveArr.isPackageService = typeof(value.isPackageService) != "undefined" ? value.isPackageService : 'N';
			saveArr.apportionedPrice = typeof(value.apportionedPrice) != "undefined" ? value.apportionedPrice : null;
			saveArr.isCapService = value.isCapService;
			saveArr.packageConsumptionAmt = value.packageConsumptionAmt;
			//saveArr.departmentId = value.specialityId;
			saveArr.departmentId = value.serviceSpecialityId!=null? value.serviceSpecialityId:value.specialityId;
			saveArr.subDepartmentId = value.subSpecialityId;
			saveArr.isRefDoctorShare = value.isRefDoctorShare;
			saveArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
			saveArr.ordDocSplId = value.doctorSpecialityId;
			saveArr.serviceRenderingDeptId = (value.serviceRenderingDeptId==null)?value.subSpecialityId:value.serviceRenderingDeptId; 
			
			saveArr.encounterId = $scope.encounterDetails.encounterId;
			saveArr.discountDetailsList = [];
			saveArr.serviceAmt =  value.basePrice;
			saveArr.discountDetailsList = [];
			saveArr.bedCategoryId = value.bedCategoryId;
			saveArr.debitNoteService = true;
			
			 //package force add
			saveArr.forceAddRemoved = value.forceAddRemoved;
			saveArr.isConsumed = value.isConsumed;
			
			if($scope.billWiseDiscountObj.requestAmountBillWise > 0 && value.isDiscountApplicable == 'Y')
			{
    				saveArr.reqDisAmt  = value.discount;
			}
			
			
			if(value.discountDetailsList == null)
				{
				value.discountDetailsList = [];
				}
			
				saveArr.orderDetailsPayeeMapperDtosList  = value.orderDetailsPayeeMapperDtosList;
				saveArr.orderDetailsPackageMapperDtosList = value.orderDetailsPackageMapperDtosList;
			saveOrderDetailsObj.push(saveArr);
		});
		data.orderDetailsMasterDtosList = saveOrderDetailsObj;
		data.cancelOrderDetailsList = $scope.removeServicesList;
		data.referralId = $scope.encounterDetails.referralId;
		
		console.log("finalSaveData",data);
		//return false;
		
			if($scope.finalBill == 'Y')
			{
				
				if ($scope.popUpFlag) {
					$scope.showModal = !$scope.showModal;

				}
				else{
					   $rootScope.startLoader();
						$scope.popUpFlag = true;
						console.log("finalSaveData",data);
						console.log(JSON.stringify(data));
						//return false;
						var URI = BASE_URL + ROOT_URL + SAVEBILL;
						BillingGenericService.serviceAction(METHOD_POST,URI, data , NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
							if(response.data.status == 'success')
							{
								$state.go('debitNoteList');
								var data =  {
						                 "appName": "HIS",
						                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
						                 "reportDesignFilePath": "OPD_DebitNote.rpt",
						                 "reportFormat": "PDF",
						                 "exportFileName": "OpdDebitNote",
						                 "reportParams": [
						                   {
						                  "paramName": "@billing_master_id",
						                  "paramType": "Integer",
						                  "paramValue":response.data.object
						                }
						                 ]
						      };
								reportService.generate(REPORT_API_URI1,data);
								$rootScope.stopLoader();
					        $scope.init();
								
								//angular.element('#collectPayment').modal('show');
							}else if(response.data.status == 'error')
							{
								$rootScope.stopLoader();
							}
							
						});
						
						
				}
			}
			else
				{
				if ($scope.popUpFlag) {
					$scope.showModal = !$scope.showModal;

				}
				else{
					$rootScope.startLoader();
					console.log("finalSaveData",data);
					console.log(JSON.stringify(data));
					var URI = BASE_URL + ROOT_URL + SAVEBILL;
					BillingGenericService.serviceAction(METHOD_POST,URI, data , NOTIFICATION_MSG_STATUS_TRUE).then(function(response){
						console.log(response)
						if(response.data.status == 'success')
						{
							$scope.init();
							$rootScope.stopLoader();
						}
						else if(response.data.status == 'error')
							{
							$rootScope.stopLoader();
							}
						
					});
					
				}
					
				}
			
	}
	$scope.getSpecialityByDoctorService = function(docId,idx)
	{
	var specDocObj = {
	    			unitId:$scope.unitId,
	    			organizationId:$scope.organizationId,
	    			doctorId:docId
	    }
	    var URI = BASE_URL + ROOT_URL
		+ GETSPECIALITYLISTBYDOCTORID;
BillingGenericService.serviceAction(METHOD_POST, URI,specDocObj,
				NOTIFICATION_MSG_STATUS_FALSE)
		.then(function(response) {
					console.log(response);
					
					// $rootScope.startLoader();
					if (response.data.status == "success")
						{
						//$scope.docSpecialityList = response.data.listObject;
						$scope.autoRenderedServicesFinalList[idx].docSpecialityList = response.data.listObject;
						$scope.autoRenderedServicesFinalList[idx].doctorSpecialityList = response.data.listObject;
						$scope.autoRenderedServicesFinalList[idx].doctorSpecialityId = response.data.listObject[0].specialityId;
						/*$scope.docSplObj = $.grep($scope.doctorsList, function (docList) {
				            return docList.doctorId == docId;
				           })[0];*/
						 // $scope.autoRenderedServicesFinalList[idx].doctorSpecialityId = $scope.docSplObj.specialityId;
						/*if(response.data.listObject.length == 1){
							$scope.autoRenderedServicesFinalList[idx].doctorSpecialityId =response.data.listObject[0].specialityId;
						}else{
							$scope.autoRenderedServicesFinalList[idx].doctorSpecialityId =response.data.listObject[0].specialityId;
						}*/
						}
				});
 }
	$scope.getSpecialityByDoctorForPackage = function(packageIndex,docId,idx)
	{
	var specDocObj = {
	    			unitId:$scope.unitId,
	    			organizationId:$scope.organizationId,
	    			doctorId:docId
	    }
	    var URI = BASE_URL + ROOT_URL
		+ GETSPECIALITYLISTBYDOCTORID;
BillingGenericService.serviceAction(METHOD_POST, URI,specDocObj,
				NOTIFICATION_MSG_STATUS_FALSE)
		.then(function(response) {
					console.log(response);
					
					// $rootScope.startLoader();
					if (response.data.status == "success")
						{
						$scope.selectedPackageList[packageIndex].packageServiceList[idx].docSpecialityList = response.data.listObject;
						$scope.selectedPackageList[packageIndex].packageServiceList[idx].doctorSpecialityId = response.data.listObject[0].specialityId;
						/*$scope.docSplObj = $.grep($scope.doctorsList, function (docList) {
				            return docList.doctorId == docId;
				           })[0];
						$scope.selectedPackageList[packageIndex].packageServiceList[idx].doctorSpecialityId = $scope.docSplObj.specialityId;*/
						}
					
				});
}
	
	$scope.getSpecialityByDoctor = function(docId,idx)
	{
		 var specDocObj = {
	    			unitId:$scope.unitId,
	    			organizationId:$scope.organizationId,
	    			doctorId:docId
	    }
	    var URI = BASE_URL + ROOT_URL
		+ GETSPECIALITYLISTBYDOCTORID;
BillingGenericService.serviceAction(METHOD_POST, URI,specDocObj,
				NOTIFICATION_MSG_STATUS_FALSE)
		.then(function(response) {
					console.log(response);
					
					// $rootScope.startLoader();
					if (response.data.status == "success")
						{
						//$scope.docSpecialityList = response.data.listObject;
						  $scope.servicesListPopup[idx].docSpecialityList = response.data.listObject;
						  $scope.docSplObj = $.grep($scope.doctorsList, function (docList) {
					            return docList.doctorId == docId;
					           })[0];
						 /* if($scope.docSplObj!=null && $scope.docSplObj!=undefined ){
						      $scope.servicesListPopup[idx].doctorSpecialityId = $scope.docSplObj.specialityId;
						  }*/
						  
						  if(response.data.listObject.length == 1){
								$scope.servicesListPopup[idx].doctorSpecialityId =response.data.listObject[0].specialityId;
							}else{
								$scope.servicesListPopup[idx].doctorSpecialityId =response.data.listObject[0].specialityId;
							}
						}
						
				});
    }
	$scope.getDoctorBySpeciality = function (specialityId,idx,packageIndex) {

		var data = {
			specialityId: specialityId,
			unitId: $scope.unitId,  
			organizationId: $scope.organizationId
		};
		var URI = BASE_URL + ROOT_URL+ GETDOCTORBYSPECIALITYID;

		BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
			.then(
				function (response) {
					if (response.data.status == "success")
						{
						
						 if(packageIndex!=null && idx!=null)
							 {
							 $scope.selectedPackageList[packageIndex].packageServiceList[idx].docList =  response.data.listObject
							 }else{
								 $scope.autoRenderedServicesFinalList[idx].docList = response.data.listObject;
							 }
						}
					else if (response.data.status == "error")
					{
						$rootScope.stopLoader();
					}
				});

	}
	$scope.getDoctorBySpecialityList = function (specialityId) {

		var data = {
			specialityId: specialityId,
			unitId: $scope.unitId,  
			organizationId: $scope.organizationId
		};
		var URI = BASE_URL + ROOT_URL+ GETDOCTORBYSPECIALITYID;

		return BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
			.then(
				function (response) {
					if (response.data.status == "success")
						{
							 return $scope.docLists =  response.data.listObject
							 
						}
					
				});

	}
	
    
	 /******* START TAB FOR Payment Mode *******/
	/*First Level*/
	$scope.firstTabLevel = 1 ;
	
	$scope.setFirstTabLevel= function(newTab)
	{
		$scope.firstTabLevel = newTab;
		
		if(newTab==3)
		{
			$scope.getAuthrizationDetails();
		}
		
	};
	
	$scope.isSetFirstTabLevel = function(tabNum)
	{
		return $scope.firstTabLevel === tabNum;
	};
	 /******* END TAB FOR Payment Mode *******/
	
	/** Yogesh Start **/
	
	$scope.initPackageSearchObj = function(){
		$scope.packageSearchObj = {packageTypeId : "0", serviceCode:"",serviceName:""}
	}
	$scope.initPackageSearchObj();
	
	$scope.getPackageTypeList = function(orgId,unitId) {
		var URI = BASE_URL + ROOT_URL + "/Unit/Service/EHCpackage/getPackgeType/"+orgId+"/"+unitId;
		BillingGenericService.serviceAction("GET", URI, null).then(function(response){
			$scope.packageTypeList = response.data.listObject;
		});
	}
	$scope.getPackageTypeList($scope.organizationId,$scope.unitId);
	
	$scope.searchPackages = function(packageSearchKeyword)
	{
		    var packageSearchObj = {};
			packageSearchObj.organisationId = $scope.organizationId;
			packageSearchObj.unitId = $scope.unitId;
			packageSearchObj.genderId = $scope.patientDetails.genderId;
			packageSearchObj.visitTypeId = $scope.visitTypeId;//default op
			packageSearchObj.bedBillingCategoryId = ($scope.visitTypeId==1)?$scope.opdBillingCatId:$scope.encounterDetails.bedBillingCategoryId;//default op
			packageSearchObj.paymentEntitlementId =$scope.encounterDetails.paymentEntitlementTypeId;
			packageSearchObj.patientTypeId = $scope.patientDetails.patientTypeId;
			packageSearchObj.serviceCode = packageSearchKeyword;
			packageSearchObj.serviceName = packageSearchKeyword;

			return servicesFactory.searchPackageForBilling(packageSearchObj,$scope);
			/*var URI = BASE_URL + ROOT_URL + "/api/packages/bill/services/search";
			return BillingGenericService.serviceAction("POST", URI, packageSearchObj).then(function(response){
				if(response.data.status == "success")
					return response.data.listObject;
			});*/
	}
	
	$scope.selectPackageService = function($item, $model, $label)
	{
		
		if(typeof($item.packageTypeId) != "undefined" && $item.packageTypeId == "1"){
			var birthDateArr=$scope.patientDetails.birthDate.split("/");
			var age= $rootScope.getAge(birthDateArr[1]+"/"+birthDateArr[0]+"/"+birthDateArr[2]);
			
			if(!($item.minAge <= age && $item.maxAge >= age)){
				$scope.packageSearchObj.serviceName='';
				growl.warning("Package is not yet  configure for this age.",{
					title : WARNING_MSG
				});
				return;
			}		
		}
		$scope.packageResultObj =  $item;
		$scope.forServicePayeeId  = $scope.selfPayeeId;
		
		if(parseInt($scope.contractId) == 0)
		{
			$scope.contractId = null;
		}
		$scope.packageResultObj.encounterId = $scope.encounterDetails.encounterId;
		$scope.packageResultObj.admissionId = $scope.visitTypeId!=1?$scope.admissionDetails.admissionId:null;
		$scope.packageResultObj.contractId = $scope.contractId;
		$scope.packageResultObj.visitTypeId = $scope.visitTypeId;
		$scope.packageResultObj.billingBedCategoryId = $scope.visitTypeId==1?$scope.opdBillingCatId:$scope.admissionDetails.bedBillingCategoryId;
		$scope.packageResultObj.patientCategoryId =  $scope.visitTypeId==1?$scope.patientDetails.patientTypeId:$scope.admissionDetails.patientCategoryId;
		$scope.packageResultObj.defaultSelfTariffId = $scope.defaultSelfTariffId;
	    $scope.packageResultObj.selfPayeeId = $scope.selfPayeeId;
	    $scope.packageResultObj.organizationId = $scope.organizationId;
	    $scope.packageResultObj.unitId = $scope.unitId;
			
	    var packageIds = [];
		angular.forEach($scope.selectedPackageList,function(value,index){
			packageIds.push(value.packageMasterId);
		});
		if(packageIds.indexOf($item.packageMasterId) == -1){
			//return false;
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + "/global/consumption/package";
			BillingGenericService.serviceAction(METHOD_POST,URI, $scope.packageResultObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status=="success"){
					console.log("res",response);
					//var basePriceObj=response.data.object;
					if(response.data.object != "undefined" && response.data.object != null)
						{
						$scope.forServicePayeeId = response.data.object.payeeId;
						$item.basePrice=response.data.object.serviceRate;
						$item.quantity=1;
						$item.coPayPer = response.data.object.coShare==null?0:response.data.object.coShare;
						$item.selfPayable = response.data.object.selfPayable;
						$item.creditPayable = response.data.object.creaditPayable;
						$item.packageConsumptionAmt = response.data.object.packageConsumptionAmt;
						$item.contractId = response.data.object.contractId;
						$item.patientClassPercentage = response.data.object.patientClassPercentage;
						$item.markupStatus = response.data.object.markupStatus;
						$item.minRateEditable = response.data.object.minRateEditable;
						$item.maxRateEditable = response.data.object.maxRateEditable;
						$item.netPay = response.data.object.finalPrice;
						$item.orderDetailsPayeeMapperDtosList=response.data.object.orderDetailsPayeeMapperDtosList;
						$item.payeeId=response.data.object.payeeId;
						$item.isPackage = true;
						$item.orderDetailsPackageMapperDtosList=response.data.object.orderDetailsPackageMapperDtosList;
						$item.bedCategoryId=response.data.object.billingBedCategoryId;
						var packageServiceList = [];
						if(typeof($item.packageTypeId) != "undefined" && $item.packageTypeId == "1"){
		    	    		var packageIds = [];
		    	    		packageIds.push($item.packageMasterId);
		    	    		//get ehc package services 
		    	    		var URI = BASE_URL + ROOT_URL + "/api/packages/bill/package/"+packageIds.toString();
		    				BillingGenericService.serviceAction("GET", URI, null).then(function(response){
		    					if(response.data.status == "success")
		    					{
		    						var ehcPackageServiceList = response.data.listObject[0].listTPackageServicesDetailsDto;
		    						angular.forEach(ehcPackageServiceList,function(val,i){
		    							var newArr = {};
		    			    			newArr.priorityId  = '2';
		    			    			newArr.isOutsourced ='N';
		    			    			newArr.specialityId = val.specialityId;
		    			    			newArr.serviceSpecialityId = val.specialityId;
		    			    			newArr.serviceSubSpecialityId = val.subSpecialityId;
		    			    			/*if($scope.isPreOrPostBilling == 'N')
		    			    			{
		    			    			newArr.serviceRendered = 1;
		    			    			}
		    			    		   else
		    			    			{
		    			    			 newArr.serviceRendered = "0";
		    			    			}*/
		    			    			newArr.serviceRendered = "0";
		    			    			newArr.serviceMasterId = val.serviceId;
		    			    			newArr.serviceStandardCode = val.serviceCode;
		    			    			newArr.serviceStandardName = val.serviceName;
		    			    			
		    			    			newArr.isDoctorRequired = val.isDocReq;
		    			    			newArr.isRefDoctorShare = val.isRefDoctorShare;
		    			    			
		    			    			newArr.isDiscountApplicable = "N";
		    			    			newArr.isQuantityEditable = "N";
		    			    			newArr.isRateEditable = "N";
		    			    			newArr.minRateEditable = 0;
		    			    			newArr.maxRateEditable = 0;
		    			    			newArr.specialityName = val.specialityName;
		    			    			newArr.quantity = 1;//val.numberToBeUse;
		    			    			newArr.basePrice =  val.servicePrice;
		    			    			newArr.serviceAmt = val.servicePrice;
		    			    			newArr.totalAmt = val.servicePrice;
		    			    			newArr.netPay = 0;
		    			    			newArr.packageConsumptionAmt = val.servicePrice;
		    			    			newArr.basePriceOld = 0;//val.apportionedPrice;
		    			    			newArr.discountDisabledFlag = 'N';
		    			    			
		    			    			newArr.concession = 0;
		    			    			newArr.discountPercentage = 0;
		    			    			newArr.discount = 0;
		    			    			newArr.coPayPer = 0;
		    			    			
		    			    			var dis =0;
		    			    			var disPer =0;
		    			    			newArr.taxPercentage = 0;
		    			    			newArr.discountDetailsList = [];
		    			    			newArr.selfPayable =  "0";
		    			    			newArr.creditPayable =   "0";
		    			    			newArr.oldNetPay = "0";
		    			    			newArr.createdBy  = $scope.userId;
		    			    			newArr.createdDate = new Date().getTime();
		    			    			newArr.packageMasterId = $item.packageMasterId;
		    			    			newArr.packageTypeId = $item.packageTypeId;
		    			    			newArr.packageId = $item.packageMasterId;
		    			    			newArr.isPackageService = 'Y';
		    			    			newArr.apportionedPrice = val.apportionedPrice;
		    			    			
		    			    			newArr.subSpecialityId = val.subSpecialityId; 
		    			    			newArr.ordCancelled = 'N';
		    			    			newArr.isDocReq=val.isDocReq;
		    			    			if(val.isRefDoctorShare=='Y')
		    			    				{
		    			    				 newArr.doctorId = 0;
		    			    	    		 newArr.doctorSpecialityId = 0;
		    			    				}
		    			    			var dataMappr = [{
		    			    					contractId: null,
		    			    					creditPayable: 0,
		    			    					organizationId: $scope.organizationId,
		    			    					payeeAmount: 0,
		    			    					payeeId:$scope.selfPayeeId,
		    			    					selfPayable: 0,
		    			    					serviceId: val.serviceId,
		    			    					status: 'A',
		    			    					unitId: $scope.unitId
		    			    			}];
		    			    			if(val.consultationSpecialityId>0)
		    			    			{
		    			    				newArr.docSpecialityList=$scope.specialityMasterList;
		    			    				newArr.doctorSpecialityId=val.consultationSpecialityId;
		    			    				newArr.docList=$scope.getDoctorBySpecialityList(val.consultationSpecialityId);
		    			    			}
		    			    			else{
		    			    				newArr.docList=$scope.doctorsList;
		    			    			}
		    			    			//ganesh
		    			    			newArr.orderDetailsPayeeMapperDtosList = dataMappr;
		    			    			
		    			    			// for Package
		    			    			var packageMppr = [{"id":"",
		    			    				"orderDetailsId":"",
		    			    				"serviceId":val.serviceId,
		    			    				"packageId":$item.packageMasterId,
		    			    				"payeeId":"",
		    			    				"serviceAmount":val.servicePrice,	
		    			    				"packageAmount":val.servicePrice,
		    			    				"packageCode":$item.serviceStandardCode,
		    			    			    "packageName":$item.serviceStandardName,
		    			    				"isPackageService":"Y",
		    			    				"isCapService":"N",
		    			    				"saleDetailsId":"",
		    			    				"itemId":"",
		    			    				"status":"A",
		    			    				"unitId":$scope.unitId,
		    			    				"organizationId": $scope.organizationId}];
		    			    			
		    			    			  newArr.orderDetailsPackageMapperDtosList = packageMppr;
		    			    			  newArr.bedCategoryId = $item.bedCategoryId,
		    			    			  packageServiceList.push(newArr);
		    						 })
		    						$item.packageServiceList = packageServiceList;
				    				$scope.selectedPackageList.push($item);
		    					}
		    				});
		    				
		    				
		    	    	}
						else
						{
							$scope.selectedPackageList.push($item);
						}
					    	$scope.initPackageSearchObj();
						}
					else
						{
						growl.warning("Please add base price of this service and then add..!!",{
							title : ERROR_MSG
						});
						return false
						}
					//concession
					console.log("FINAL ITEM",$item);
				}
			});
			
			
		}else{
			growl.error("Package Already Added",{
				title : ERROR_MSG
			});
		}
	}
	
	$scope.removeSelectedPackage = function(index){
		$scope.selectedPackageList.splice(index,1);
	}
	
	
	$scope.saveOrderPackage = function()
	{
        $scope.isPatientClass = 'N';

		$rootScope.startLoaderPopup();
		var data={
				patientId :$scope.encounterDetails.patientId,
				encounterId :$scope.billObj.encounterId,
				admissionId :$scope.billObj.admissionId,
				createdBy :$scope.userId,
				createdDate :new Date().getTime(),
				updatedBy :$scope.userId,
				updatedDate :new Date().getTime(),
				orderDate :new Date().getTime(),
				organisationId :$scope.organizationId,
				unitId :$scope.unitId,
				isFinalBill : $scope.finalBill,
				ordDoctorId :$scope.encounterDetails.doctorId,
				visitTypeId : $scope.billObj.visitTypeId,
				billDateTime : new Date().getTime(),
				netAmount : parseFloat(parseFloat($scope.netPayableSummary)+parseFloat($scope.roundOffAmt)).toFixed(2),
				discountAmount : $scope.totalDiscountSummary,
				concessionAmount : $scope.totalConcessionSummary,
				gst : $scope.totalGstTaxSummary,
				totalBillAmount : $scope.totalAmountSummary,
				roundOff : $scope.roundOffAmt,
				isCreditBill:'N',
				billCancelled : 'N',
				isFinancialClearance : 'N',
				billRemark : $scope.billRemark,
				billStatusId : '1', // 1. Pending,
				selfPayable : $scope.totalSelfPayble,
				creditPayable : $scope.totalCreditPayble,
				cancelledDiscountsList : $scope.removeDiscountList,
				tariffId : $scope.defaultSelfTariffId,
				paymentEntitlementId : $scope.encounterDetails.paymentEntitlementTypeId,
				visitTypeId : $scope.billObj.visitTypeId,
				patientTypeId : $scope.patientDetails.patientTypeId,
				billingBedCategoryId : $scope.encounterDetails.bedBillingCategoryId, 
				isInterimBill :'N',
				isDrNote:"Y",
				contractId:$scope.contractId,
				counterId:$rootScope.counterId,
				encounterSpecialityId:$scope.encounterDetails.specialityId,
				lineLevelDiscountOrderList : [],
				patientClassId : $scope.encounterDetails.patientClassId,
				pharmacyServiceId:$scope.pharmacyServiceId,
				selfPayeeId : $scope.selfPayeeId
		   };
		
		var billPayeeList =[];
		if($scope.totalSelfPayble > 0)
			{
				var billPayeeArr ={
						payeeId : $scope.selfPayeeId,
						billAmount : $scope.totalSelfPayble
				}
				billPayeeList.push(billPayeeArr);
			}
		if($scope.totalCreditPayble > 0)
			{
				var billPayeeArr ={
						payeeId : $scope.encounterDetails.payeeId,
						billAmount : $scope.totalCreditPayble
				}
				billPayeeList.push(billPayeeArr);
			}
		
		data.billPayeeDetailsDtosList = billPayeeList;
		var saveOrderDetailsObj = [];
		angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
            if($scope.surchargeServiceId != value.serviceMasterId){

			var saveArr = {};
			if(value.orderDetailsId > 0){
				saveArr.orderDetailsId = value.orderDetailsId;
			}
            saveArr.discountOwnership = value.discountOwnership;
			saveArr.isConsultationService = value.isConsultationService;
			saveArr.markupStatus = value.markupStatus;
			saveArr.patientClassPercentage = value.patientClassPercentage;
			saveArr.orgId = $scope.organizationId;
			saveArr.orgUnitId  = $scope.unitId;
			saveArr.orderId = value.orderId;
			saveArr.orderQty = value.quantity;
			saveArr.serviceId = value.serviceMasterId;
			saveArr.priorityId = value.priorityId;
			saveArr.isOutsourced = value.isOutsourced;
			//saveArr.orderDate = new Date().getTime();
			saveArr.orderDate =   (value.orderDate !=null && value.orderDate!=undefined && value.orderDate!='')?value.orderDate:new Date().getTime();
			saveArr.orderSchDate = new Date().getTime();
			saveArr.dummyServiceAmt = value.dummyServiceAmt;
			saveArr.ordConcession = value.concession;
			saveArr.ordDiscount = value.discount;
			saveArr.netAmount = value.netPay;
			saveArr.ordDocSplId = value.specialityId;
			//saveArr.payeeId = $scope.encounterDetails.payeeId;
			saveArr.payeeId = value.payeeId;
			saveArr.tariffId = value.tariffId;
			saveArr.billingClassId = '1';
			saveArr.ordDocId = value.doctorId;
			saveArr.serviceRendered = value.serviceRendered;
			saveArr.serviceRenderingDeptId = value.serviceRenderingDeptId;
			saveArr.ordRenderDatetime = new Date().getTime();
			saveArr.serviceIsBilled = '0';
			saveArr.status = value.status;
			saveArr.updatedBy = $scope.userId;
			saveArr.updatedDate =new Date().getTime();
			saveArr.createdBy  = value.createdBy;
			saveArr.createdDate  = value.createdDate;
			saveArr.oldNetPay =  parseFloat(value.oldNetPay).toFixed(2);
			saveArr.ordCancelled = value.ordCancelled;
            saveArr.discountCategoryId = value.discountCategoryId;

			saveArr.ordCancelReasonId  = value.ordCancelReasonId;
			saveArr.ordCancelDatetime  = value.ordCancelDatetime;
			saveArr.ordCancelRemark  = value.ordCancelRemark;
			saveArr.oldOrdDtlId = value.oldOrdDtlId;
			
			saveArr.serviceAmt =  value.basePrice;
			saveArr.ordTotalAmt = saveArr.serviceAmt;
			saveArr.taxPer  = value.taxPercentage;
			var withConDic =parseFloat(saveArr.ordTotalAmt - (parseFloat(value.concession))).toFixed(2);
			saveArr.taxAmount = parseFloat((parseFloat(withConDic) * value.taxPercentage)/100).toFixed(2);
			saveArr.netAmount =   parseFloat(parseFloat(withConDic) +  parseFloat(saveArr.taxAmount)).toFixed(2);
			
			saveArr.dummyServiceAmt = value.dummyServiceAmt;
			saveArr.ordConcession = value.concession;
			saveArr.ordDiscount = value.discount;
            saveArr.ordDiscountPer = value.discountPercentage;
			saveArr.isFromAddservice = value.isFromAddservice;
			saveArr.isPackage = typeof(value.isPackage) != "undefined" ? value.isPackage : false;
			/*saveArr.selfPayable = saveArr.isPackage?value.selfPayable:saveArr.netAmount;
			saveArr.creditPayable = saveArr.isPackage?value.creditPayable:0;*/
			saveArr.selfPayable =   saveArr.netAmount;
			//saveArr.creditPayable = saveArr.isPackage?value.creditPayable:0;
			saveArr.creditPayable = 0;
			saveArr.oldCreditPayable = 0;
			saveArr.oldSelfPayable = 0;

			saveArr.taxId = value.taxId;
			saveArr.oldCreditPayable = value.creditPayableOldC;
			saveArr.oldSelfPayable = value.selfPayableOldC;
			saveArr.isInvestigationOrder = value.isInvestigationOrder;
            saveArr.isDiscountApplicable = value.isDiscountApplicable;
			saveArr.orderEmergencyFlag = 'N';
			saveArr.isDrug = 'N';
			saveArr.procedureOrderMstId = value.procedureOrderMstId > 0 ? value.procedureOrderMstId  : null;
			saveArr.isProcedureService = value.isProcedureService!=null ? value.isProcedureService  : 'N';
			saveArr.isPostedWhileDischarge = value.isPostedWhileDischarge!=null ? value.isPostedWhileDischarge : 'N';
			//saveArr.isPackage = typeof(value.isPackage) != "undefined" ? value.isPackage : false;

			saveArr.packageId = typeof(value.packageMasterId) != "undefined" && value.packageMasterId != null ? value.packageMasterId : value.packageId;
			saveArr.packageTypeId = typeof(value.packageTypeId) != "undefined" ? value.packageTypeId : null;
			saveArr.isPackageService = typeof(value.isPackageService) != "undefined" ? value.isPackageService : 'N';
			saveArr.apportionedPrice = typeof(value.apportionedPrice) != "undefined" ? value.apportionedPrice : null;
			saveArr.isCapService = value.isCapService;
			saveArr.packageConsumptionAmt = value.packageConsumptionAmt;
			saveArr.departmentId = value.serviceSpecialityId!=null? value.serviceSpecialityId:value.specialityId;
			saveArr.subDepartmentId = value.serviceSubSpecialityId;
			saveArr.isRefDoctorShare = value.isRefDoctorShare;
			saveArr.isDocReq =  value.isDocReq!=null &&  value.isDocReq!=undefined ? value.isDocReq :value.isDoctorRequired;
			saveArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
			
			saveArr.ordDocSplId = value.doctorSpecialityId;
			saveArr.serviceRenderingDeptId = value.subSpecialityId; 
			
			saveArr.encounterId = $scope.encounterDetails.encounterId;
			saveArr.contractId = value.contractId;
			saveArr.bedCategoryId = value.bedCategoryId;
			saveArr.discountDetailsList = [];
			saveArr.debitNoteService = true;
			
			  //package force add
			saveArr.forceAddRemoved = value.forceAddRemoved;
			saveArr.isConsumed = value.isConsumed;		
			
			if(value.discountDetailsList == null)
				{
				value.discountDetailsList = [];
				}
            
			    saveArr.saleId = value.saleId;
				saveArr.orderDetailsPayeeMapperDtosList  = value.orderDetailsPayeeMapperDtosList;
				saveArr.orderDetailsPackageMapperDtosList  = value.orderDetailsPackageMapperDtosList;
			    saveOrderDetailsObj.push(saveArr);
		}
		    });
		               data.orderDetailsMasterDtosList = saveOrderDetailsObj;
						var URI = BASE_URL + ROOT_URL +'/api/billing/upDatePackage';
						BillingGenericService.serviceAction(METHOD_POST,URI, data , NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
							if(response.data.status == 'success')
							{
								 $scope.getAutoRenderedServicesByPatient({
			    						"visitTypeId":$scope.billObj.visitTypeId,
			    						"encounterId":$scope.encounterDetails.encounterId
			    				});
                                 $scope.isPatientClass = 'N';
								 $rootScope.stopLoaderPopup();
							}
							else if(response.data.status == 'error')
								{
                                $scope.isPatientClass = 'N';
								 $rootScope.stopLoaderPopup();
								}
						});
						
	}
	
	$scope.getPackages = function(){
		var packageIds = [];
		$scope.setSecondTabLevel(21);
		angular.forEach($scope.autoRenderedServicesFinalList,function(value,key){
			if(value.isPackage && value.isPackageService != 'Y' && packageIds.indexOf(value.packageMasterId) == -1 && $scope.activePackageIds.indexOf(value.packageMasterId) == -1)
				packageIds.push(value.packageMasterId);
		});
		//get package details
		if(packageIds.length > 0){
			var URI = BASE_URL + ROOT_URL + "/api/packages/bill/package/"+packageIds.toString();
			BillingGenericService.serviceAction("GET", URI, null).then(function(response){
				if(response.data.status == "success")
					$scope.packageDetailsList = response.data.listObject;
			});
		}
	}
	$scope.getActivePackagesList = function(){
		$scope.activePackageIds = [];
		if($scope.patientDetails !== undefined && $scope.patientDetails !== null){
            if($scope.patientDetails.patientId === undefined || $scope.patientDetails.patientId === null || $scope.patientDetails.patientId ===''){
                $scope.patientDetails.patientId = 0;
            }
        }
        else{
            $scope.patientDetails.patientId = 0;
        }
		var URI =BASE_URL + ROOT_URL + "/api/packages/patientId/"+$scope.patientDetails.patientId+"/encounterId/"+$scope.encounterDetails.encounterId+"/visitTypeId/"+$scope.visitTypeId;
		var header = {organisationId: $scope.organizationId,unitId:$scope.unitId,packageId:0};
		GenericServiceParamHeader.serviceAction("GET", URI, null,NOTIFICATION_MSG_STATUS_FALSE,null,header).then(function(response){
			if(response.data.status == "success"){
				$scope.activePackagesList = response.data.listObject;
			$scope.activePackagesList.forEach(function(value,index){
				$scope.activePackageIds.push(value.packageId);
			})
		}
		});
	}
	
	$scope.currentActivePackageServices = function(activePack){
		$scope.currentActivePackage = angular.copy(activePack);
	}
	
	

	
	//package transaction view start
	$scope.getAllPackageList=function(){
		
		  $('#commonPackageDetailsPopup').modal('show');
	}
	
	$scope.forceCancelPackage=function(obj){
		var data={
				packageConsumptionMasterId:obj.packageConsumptionMasterId,
				orderDetailId:obj.orderDetailId,
				toDate : moment().format('YYYY-MM-DD HH:mm:ss'),
				unitId : $scope.unitId
		}
		$rootScope.startLoader();
		var URI = BASE_URL + ROOT_URL + "/api/packages/forceCancelPackage";
		BillingGenericService.serviceAction("POST", URI, data).then(function(response){
			if(response.data.status == "success")
				{
					growl.success("Package closed",{
						title : WARNING_MSG
					});
					$scope.getListPackageOrder();
					$rootScope.stopLoader();
				}else{
					$rootScope.stopLoader();
				}
				
		});
	}
	
	$scope.getServiceIndex = function(index)
	{
		$scope.serviceIdxPkg = index;
	}
	$scope.addToPackage = function(packageObj)
	{
		 $rootScope.startLoaderPopup();
		 var packageMppr = [];
/*		 if($scope.autoRenderedServicesFinalList[$scope.serviceIdxPkg].packageConsumptionAmt>0)
			 {
			   packageMppr = $scope.autoRenderedServicesFinalList[$scope.serviceIdxPkg].orderDetailsPackageMapperDtosList.filter(mppr => mppr.isPackageService =='Y');
			 }
		 else{
*/		    packageMppr = [{"id":"",
			"orderDetailsId":"",
			"serviceId":$scope.autoRenderedServicesFinalList[$scope.serviceIdxPkg].serviceId,
			"packageId":packageObj.packageId,
			"payeeId":"",
			"serviceAmount":$scope.autoRenderedServicesFinalList[$scope.serviceIdxPkg].basePrice,	
			"packageAmount":$scope.autoRenderedServicesFinalList[$scope.serviceIdxPkg].basePrice,
			"packageCode":"",
		    "packageName":"",
			"isPackageService":"Y",
			"isCapService":"N",
			"saleDetailsId":"",
			"itemId":"",
			"status":"A",
			"unitId":$scope.unitId,
			"organizationId": $scope.organizationId}];
		// }
		 $scope.autoRenderedServicesFinalList[$scope.serviceIdxPkg].orderDetailsPackageMapperDtosList = packageMppr;
		 $scope.autoRenderedServicesFinalList[$scope.serviceIdxPkg].forceAddRemoved =1;
		 $scope.saveOrderPackage();
	}
	
	$scope.removeFromPackage = function(serviceIdx)
	{
		 $rootScope.startLoader();
		 $scope.autoRenderedServicesFinalList[serviceIdx].forceAddRemoved =-1;
		 if($scope.autoRenderedServicesFinalList[serviceIdx].isPackageService!=null && $scope.autoRenderedServicesFinalList[serviceIdx].isPackageService=='Y'){
			 $scope.autoRenderedServicesFinalList[serviceIdx].orderDetailsPackageMapperDtosList =  [{"id":"",
					"orderDetailsId":"",
					"serviceId":$scope.autoRenderedServicesFinalList[serviceIdx].serviceId,
					"packageId":'',
					"payeeId":"",
					"serviceAmount":$scope.autoRenderedServicesFinalList[serviceIdx].basePrice,	
					"packageAmount":0,
					"packageCode":"",
				    "packageName":"",
					"isPackageService":"N",
					"isCapService":"N",
					"saleDetailsId":"",
					"itemId":"",
					"status":"A",
					"unitId":$scope.unitId,
					"organizationId": $scope.organizationId}];
		 }
		 $scope.saveOrderPackage();
	}
	
	
	$scope.getPackagesDetails = function(packageId,consumptionMasterId)
	{
		 
		var data={};
		data[consumptionMasterId]=packageId;   
	
	//get package details
		var URI = BASE_URL + ROOT_URL + "/api/packages/bill/package";
		BillingGenericService.serviceAction("POST", URI, data).then(function(response){
			if(response.data.status == "success")
				{
				
				   console.log(response.data.listObject);
				   $scope.packageTypeId = response.data.listObject[0].mPackageMasterDto.packageTypeId;
				   if($scope.packageTypeId==1 || $scope.packageTypeId==5){
					   $scope.setFirstTabLevel(101);
				   }
				   else if($scope.packageTypeId==2){
					   $scope.setFirstTabLevel(401);
				   }
				   else if($scope.packageTypeId==3){
					   $scope.setFirstTabLevel(201);
				   }else if($scope.packageTypeId==4){
					   $scope.setFirstTabLevel(301);
				   }
				   $scope.packageDetailsResponse=response.data.listObject[0];
				   $('#packageServiceDetailsPopup').modal('show');
			    
				}
				
		});
	}
	$scope.eoGroupServiceDtls=[];
	$scope.getGroupServiceDetails=function(list){
		
		$scope.eoGroupServiceDtls=list;
		 $('#qtyDetailsModal').modal('show');
		
	}
	$scope.setPackageOrderTabActive=function(){
		$scope.setFirstTabLevel(2);
		$scope.setSecondTabLevel(22);
	}

	//package transaction view END
	
	$scope.addCurrentPackageServices = function(){
			angular.forEach($scope.currentActivePackage.listPackageConsumptionServiceDetailDto,function(value,index){
				if(value.isSelected){
					var obj = {
							basePrice:0,
							concession:0,
							discount:0,
							isDiscountApplicable:"N",
							isQuantityEditable:"N",
							isRateEditable:"N",
							isTaxApplicable:"N",
							maxRateEditable:0,
							minRateEditable:0,
							packageMasterId:$scope.currentActivePackage.packageId,
							serviceMasterId:value.serviceId,
							serviceStandardCode:value.serviceCode + " (P)",
							serviceStandardName:value.serviceStandardName,
							quantity:1,
							specialityId:value.specialityId,
							specialityName:value.specialityName,
							subSpecialityId:value.subSpecialityId,
							subSpecialityName:value.subSpecialityName,
							taxPercentage:0,
							discountPercentage : 0,
							isPackageService:'Y',
							apportionedPrice : value.apportionedPrice,
							packageId : $scope.currentActivePackage.packageId,
							packageTypeId : $scope.currentActivePackage.packageTypeId
							};
					$scope.servicesListPopup.push(obj);
				}
			});
	}
	
	
	$scope.getPatientClassServiceOP = function()
	{
			// alert($scope.isPatientClass);
		
			if($scope.isPatientClass == 'Y' && $scope.encounterDetails.patientClassId > 0 )
				{
						var data = {
								organizationId:$scope.organizationId,
								unitId : $scope.unitId,
								patientCategoryId : $scope.patientDetails.patientTypeId,
								patientClassId : $scope.encounterDetails.patientClassId,
								tariffId : $scope.defaultSelfTariffId,
								serviceIds : [],
								saleIds : [],
								encounterId : $scope.encounterDetails.encounterId,
								visitTypeId : 1,
								pharmacyServiceId:$scope.pharmacyServiceId,
								paymentEntitlementTypeId:$scope.encounterDetails.paymentEntitlementTypeId
						}
				angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
					data.serviceIds.push(value.serviceMasterId);
					if(value.saleId!=null)
					    data.saleIds.push(value.saleId);
					});
				    $rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/billing/surcharge/service/details";
			    	
			    	BillingGenericService.serviceAction(METHOD_POST,URI,data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			    		$rootScope.stopLoader();
			    		if(response.data.status == "success"){
			    			
			    			var newArr = {};
			    			newArr.priorityId  = '2';
			    			newArr.isOutsourced ='N';
			    			newArr.specialityId = null;
			    			newArr.serviceSpecialityId =response.data.listObject[0].specialityId;
			    			newArr.serviceSubSpecialityId = response.data.listObject[0].subSpecialityId;
			    			newArr.doctorId = null;
			    			newArr.doctorSpecialityId = null;
			    			newArr.serviceRendered = 0;
			    			newArr.serviceRenderingDeptId = null;
			    			newArr.serviceMasterId = response.data.listObject[0].serviceMasterId;
			    			newArr.serviceStandardCode = response.data.listObject[0].serviceStandardCode;
			    			newArr.serviceStandardName = response.data.listObject[0].serviceStandardName;
			    			newArr.isDiscountApplicable = 'N';
			    			newArr.isQuantityEditable ='N';
			    			newArr.isRateEditable = 'N';
			    			newArr.minRateEditable = response.data.listObject[0].minRateEditable;
			    			newArr.maxRateEditable = response.data.listObject[0].maxRateEditable;
			    			newArr.specialityName = response.data.listObject[0].specialityName;
			    			newArr.doctorName ='';
			    			newArr.quantity = 1;
			    			newArr.discountDisabledFlag = 'N';
			    			newArr.concession = parseFloat(0);
			    			newArr.discountPercentage = parseFloat(0);
			    			newArr.discount = parseFloat(0);
			    			newArr.taxId = response.data.listObject[0].taxId;
			    			newArr.taxName = response.data.listObject[0].taxName;
			    			newArr.taxPercentage = response.data.listObject[0].taxPercentage;
			    			newArr.discountDetailsList = [];
			    			newArr.creditPayable =parseFloat(0);
			    			newArr.creditPayableOldC = parseFloat(0);
			    			newArr.oldNetPay = "0";
			    			newArr.createdBy  = $scope.userId;
			    			newArr.createdDate = new Date().getTime();
			    			newArr.isPackageService = "N";
			    			newArr.isCapService = "N";
			    			newArr.subSpecialityId = response.data.listObject[0].subSpecialityId;
			    			newArr.ordCancelled = "N";
			    			newArr.status = "A";
			    			newArr.ordCancelReasonId  = '';
			    			newArr.ordCancelDatetime  = new Date().getTime();
			    			newArr.ordCancelRemark  = '';
			    			newArr.payeeId  = $scope.selfPayeeId;
			    			newArr.coShare =0;
			    			newArr.coPayPer =0;
			    			newArr.contractId = 0;
			    			newArr.isRefDoctorShare = "N";
			    			var patientClassTotal = parseFloat(0);
			    			
			    			angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
			    				var stopFlag = true;
			    				angular.forEach(value.orderDetailsPackageMapperDtosList, function (pkgValue, key) 
						    			{
					    					if(pkgValue.isPackageService=='N')
			    					    	   {
							    				   angular.forEach(response.data.listObject[0].discountDetailsList, function (value1, key1) {
							    					if(value.serviceMasterId == value1.serviceId && stopFlag == true)
							    					{
							    						stopFlag = false;
							    					    var totalAmt = (parseFloat(value.quantity) * parseFloat(pkgValue.serviceAmount)).toFixed(2);
							    					    var calDicAmt = (parseFloat(totalAmt)*value1.discountPercentage/100).toFixed(2);	
							    					    patientClassTotal = parseFloat(patientClassTotal) + parseFloat(calDicAmt);
							    					 }
							    				  });
			    					    	   }
						               });
			    			      });
			    			newArr.basePrice = patientClassTotal;
			    			newArr.basePriceOld = patientClassTotal;
			    			newArr.selfPayable = patientClassTotal;
			    		    newArr.selfPayableOldC = patientClassTotal;
			    		    newArr.totalAmt =  patientClassTotal;
			    		    newArr.netPay  = patientClassTotal;
			    		    newArr.packageConsumptionAmt  = parseFloat(0);
			    		    newArr.concession = parseFloat(0);
							newArr.patientClassPercentage = parseFloat(0);
			    		    
			    			var dataMappr = [{
		    					contractId: null,
		    					creditPayable: 0,
		    					organizationId: $scope.organizationId,
		    					payeeAmount: patientClassTotal,
		    					payeeId:$scope.selfPayeeId,
		    					selfPayable: patientClassTotal,
		    					serviceId: response.data.listObject[0].serviceMasterId,
		    					status: 'A',
		    					unitId: $scope.unitId
		    			}];
		    			// ganesh
		    			newArr.orderDetailsPayeeMapperDtosList = dataMappr;
		    			
		    			// for Package
		    			var packageMppr = [{"id":"",
		    				"orderDetailsId":"",
		    				"serviceId":response.data.listObject[0].serviceMasterId,
		    				"packageId":"",
		    				"payeeId":"",
		    				"serviceAmount":patientClassTotal,	
		    				"packageAmount":0,
		    				"packageCode":"",
		    			    "packageName":"",
		    				"isPackageService":"N",
		    				"isCapService":"N",
		    				"saleDetailsId":"",
		    				"itemId":"",
		    				"status":"A",
		    				"unitId":$scope.unitId,
		    				"organizationId": $scope.organizationId}];
		    			
		    			  newArr.orderDetailsPackageMapperDtosList = packageMppr;
		    			
		    			  if(parseFloat(patientClassTotal) > 0 ){
			    		      $scope.autoRenderedServicesFinalList.push(newArr);
			    		      $scope.calculationTest($scope.autoRenderedServicesFinalList,1);
			    		    }
			    		    
			    		   /* $scope.autoRenderedServicesFinalList.push(newArr);
			    		    $scope.calculationTest($scope.autoRenderedServicesFinalList,1);*/
			    		}
			    	});
				}
			else
				{
					var temp = {
							serviceMasterId : $scope.surchargeServiceId
					}
					
					var indexT = $scope.autoRenderedServicesFinalList.findIndex(record => record.serviceMasterId === $scope.surchargeServiceId);
					
					
					    if (indexT >= 0) {
					    	if($scope.autoRenderedServicesFinalList[indexT].orderDetailsId > 0){
					    		
					    		var data = {
					    				orderDetailsId : $scope.autoRenderedServicesFinalList[indexT].orderDetailsId,
					    				ordCancelReasonId : null,
					    				userId : $scope.userId
					    		}
					    		
					    		var URI = BASE_URL + ROOT_URL + "/api/billing/bill/service/cancel";
						    	
						    	BillingGenericService.serviceAction(METHOD_POST,URI,data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						    		if(response.data.status == "success"){
						    			$scope.autoRenderedServicesFinalList.splice(indexT, 1);
								    	$scope.calculationTest($scope.autoRenderedServicesFinalList,1);
										$rootScope.stopLoader();
										$scope.saveOrderPackage();
						    		}else{
						    			$rootScope.stopLoader();
						    		}
						    	});
					    	
					    }else{
					    	$scope.autoRenderedServicesFinalList.splice(indexT, 1);
					    	$scope.calculationTest($scope.autoRenderedServicesFinalList,1);
							$rootScope.stopLoader();
					    }
				}
			
				}
    	
	}
	
	$scope.getPatientClassServiceIP = function()
	{
		// alert($scope.isPatientClass);
		
		if($scope.isPatientClass == 'Y' && $scope.admissionDetails.patientClassId > 0 )
			{
					var data = {
							organizationId:$scope.organizationId,
							unitId : $scope.unitId,
							patientCategoryId : $scope.admissionDetails.patientCategoryId,
							patientClassId : $scope.admissionDetails.patientClassId,
							tariffId : $scope.defaultSelfTariffId,
							serviceIds : [],
							saleIds : [],
							admissionId : $scope.admissionDetails.admissionId,
							visitTypeId : $scope.visitTypeId,
							pharmacyServiceId:$scope.pharmacyServiceId,
							paymentEntitlementTypeId:$scope.encounterDetails.paymentEntitlementTypeId
					}
			angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
				data.serviceIds.push(value.serviceMasterId);
				if(value.saleId!=null)
				    data.saleIds.push(value.saleId);
				});
			    $rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + "/billing/surcharge/service/details";
		    	
		    	BillingGenericService.serviceAction(METHOD_POST,URI,data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
		    		$rootScope.stopLoader();
		    		if(response.data.status == "success"){
		    			
		    			var newArr = {};
		    			newArr.priorityId  = '2';
		    			newArr.isOutsourced ='N';
		    			newArr.specialityId = null;
		    			newArr.serviceSpecialityId =response.data.listObject[0].specialityId;
		    			newArr.serviceSubSpecialityId = response.data.listObject[0].subSpecialityId;
		    			newArr.doctorId = null;
		    			newArr.doctorSpecialityId = null;
		    			newArr.serviceRendered = 0;
		    			newArr.serviceRenderingDeptId = null;
		    			newArr.serviceMasterId = response.data.listObject[0].serviceMasterId;
		    			newArr.serviceStandardCode = response.data.listObject[0].serviceStandardCode;
		    			newArr.serviceStandardName = response.data.listObject[0].serviceStandardName;
		    			newArr.isDiscountApplicable = 'N';
		    			newArr.isQuantityEditable ='N';
		    			newArr.isRateEditable = 'N';
		    			newArr.minRateEditable = response.data.listObject[0].minRateEditable;
		    			newArr.maxRateEditable = response.data.listObject[0].maxRateEditable;
		    			newArr.specialityName = response.data.listObject[0].specialityName;
		    			newArr.doctorName ='';
		    			newArr.quantity = 1;
		    			newArr.discountDisabledFlag = 'N';
		    			newArr.concession = parseFloat(0);
		    			newArr.discountPercentage = parseFloat(0);
		    			newArr.discount = parseFloat(0);
		    			newArr.taxId = response.data.listObject[0].taxId;
		    			newArr.taxName = response.data.listObject[0].taxName;
		    			newArr.taxPercentage = response.data.listObject[0].taxPercentage;
		    			newArr.discountDetailsList = [];
		    			newArr.creditPayable =parseFloat(0);
		    			newArr.creditPayableOldC = parseFloat(0);
		    			newArr.oldNetPay = "0";
		    			newArr.createdBy  = $scope.userId;
		    			newArr.createdDate = new Date().getTime();
		    			newArr.isPackageService = "N";
		    			newArr.isCapService = "N";
		    			newArr.subSpecialityId = response.data.listObject[0].subSpecialityId;
		    			newArr.ordCancelled = "N";
		    			newArr.status = "A";
		    			newArr.ordCancelReasonId  = '';
		    			newArr.ordCancelDatetime  = new Date().getTime();
		    			newArr.ordCancelRemark  = '';
		    			newArr.payeeId  = $scope.selfPayeeId;
		    			newArr.coShare =0;
		    			newArr.coPayPer =0;
		    			newArr.contractId = 0;
		    			newArr.isRefDoctorShare = "N";
		    			var patientClassTotal = parseFloat(0);
		    			angular.forEach($scope.autoRenderedServicesFinalList, function (value, key) {
		    				angular.forEach(response.data.listObject[0].discountDetailsList, function (value1, key1) {
		    					if(value.serviceMasterId == value1.serviceId)
		    					{
		    					   var totalAmt = (parseFloat(value.quantity) * parseFloat(value.basePrice)).toFixed(2);
		    					   var calDicAmt = (parseFloat(totalAmt)*value1.discountPercentage/100).toFixed(2);	
		    					   patientClassTotal = parseFloat(patientClassTotal) + parseFloat(calDicAmt);
		    					   
		    					}
		    				});
		    			});
		    			newArr.basePrice = patientClassTotal;
		    			newArr.basePriceOld = patientClassTotal;
		    			newArr.selfPayable = patientClassTotal;
		    		    newArr.selfPayableOldC = patientClassTotal;
		    		    newArr.totalAmt =  patientClassTotal;
		    		    newArr.netPay  = patientClassTotal;
		    		    newArr.packageConsumptionAmt  = parseFloat(0);
		    		    newArr.concession = parseFloat(0);
						newArr.patientClassPercentage = parseFloat(0);
		    		    
		    			var dataMappr = [{
	    					contractId: null,
	    					creditPayable: 0,
	    					organizationId: $scope.organizationId,
	    					payeeAmount: patientClassTotal,
	    					payeeId:$scope.selfPayeeId,
	    					selfPayable: patientClassTotal,
	    					serviceId: response.data.listObject[0].serviceMasterId,
	    					status: 'A',
	    					unitId: $scope.unitId
	    			}];
	    			// ganesh
	    			newArr.orderDetailsPayeeMapperDtosList = dataMappr;
	    			
	    			// for Package
	    			var packageMppr = [{"id":"",
	    				"orderDetailsId":"",
	    				"serviceId":response.data.listObject[0].serviceMasterId,
	    				"packageId":"",
	    				"payeeId":"",
	    				"serviceAmount":patientClassTotal,	
	    				"packageAmount":0,
	    				"packageCode":"",
	    			    "packageName":"",
	    				"isPackageService":"N",
	    				"isCapService":"N",
	    				"saleDetailsId":"",
	    				"itemId":"",
	    				"status":"A",
	    				"unitId":$scope.unitId,
	    				"organizationId": $scope.organizationId}];
	    			
	    			  newArr.orderDetailsPackageMapperDtosList = packageMppr;
	    			
	    			
		    		    
		    		    /*$scope.autoRenderedServicesFinalList.push(newArr);
		    		    $scope.calculationTest($scope.autoRenderedServicesFinalList,1);*/
	    			  if(parseFloat(patientClassTotal) > 0 ){
		    		      $scope.autoRenderedServicesFinalList.push(newArr);
		    		      $scope.calculationTest($scope.autoRenderedServicesFinalList,1);
		    		    }
		    		}
		    	});
			}
		else
			{
				var temp = {
						serviceMasterId : $scope.surchargeServiceId
				}
				
				var indexT = $scope.autoRenderedServicesFinalList.findIndex(record => record.serviceMasterId === $scope.surchargeServiceId);
				
				
				    if (indexT >= 0) {
				    	if($scope.autoRenderedServicesFinalList[indexT].orderDetailsId > 0){
				    		
				    		var data = {
				    				orderDetailsId : $scope.autoRenderedServicesFinalList[indexT].orderDetailsId,
				    				ordCancelReasonId : null,
				    				userId : $scope.userId
				    		}
				    		
				    		var URI = BASE_URL + ROOT_URL + "/api/billing/bill/service/cancel";
					    	
					    	BillingGenericService.serviceAction(METHOD_POST,URI,data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					    		if(response.data.status == "success"){
					    			$scope.autoRenderedServicesFinalList.splice(indexT, 1);
							    	$scope.calculationTest($scope.autoRenderedServicesFinalList,1);
									$rootScope.stopLoader();
									$scope.saveOrderPackage();
					    		}else{
					    			$rootScope.stopLoader();
					    		}
					    	});
				    	
				    }else{
				    	$scope.autoRenderedServicesFinalList.splice(indexT, 1);
				    	$scope.calculationTest($scope.autoRenderedServicesFinalList,1);
						$rootScope.stopLoader();
				    }
			}
		
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
	                         patSearchObj = {};
	                          $rootScope.stopLoader();
	                     
	                          $scope.totalAmountSummary = "0.00";
	                          $scope.totalConcessionSummary = "0.00";
	                          $scope.netPayableSummary = "0.00";
	                          $scope.totalDiscountSummary = "0.00";
	                          $scope.totalGstTaxSummary = "0.00";
	                          $scope.totalCreditPayble = "0.00";
	                          $scope.totalSelfPayble = "0.00";
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
	
	 $scope.getAuthrizationDetails = function()
		{
			
			if($scope.patientDetails.patientId==null)
				{
					growl.warning("Please select Patient..",{
						title : ERROR_MSG
						
					});
					return false;
				}
			$rootScope.startLoader();
			 $scope.addNewRequestList = [];
		    var param = {
		    		"organisationId":$scope.organizationId,
		    		"unitId":$scope.unitId,
		    		"patientId":$scope.patientDetails.patientId, 
		    		"isSplitStatus": "N",
		    		"authStatusId": 1,
		    };
		    
		    if($scope.encounterDetails.encounterId!=null){
		    	param.encounterId = $scope.encounterDetails.encounterId;
		    }else{
		    	param.admissionId = $scope.admissionDetails.admissionId;
		    }
		    
		    var URI = BASE_URL + ROOT_URL + "/api/ipd/getPreAuthorizationRequestListByPayeeId"
		    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
		  if (response.data.status == "success"){
		    $scope.addNewRequestList = response.data.listObject;
		    $scope.newRequestCalculates();
		  }
		     $rootScope.stopLoader();
		 });
		}
	
    
    
}]);