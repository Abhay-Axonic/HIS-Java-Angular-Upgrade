angular.module("myApp").controller("companyReceiptsController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','$timeout','growl','paymentCollFactory','reportService','GenericService','$location','loyaltiCardCallFactory',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,$timeout,growl,paymentCollFactory,reportService,GenericService,$location,loyaltiCardCallFactory){
	try{
		
		$rootScope.loginpage = true;
		$scope.page = "billList";
		var date = new Date();
		$scope.dateOptions = {
				formatYear : 'yyyy',
				showWeeks : false,
				minDate : date.setDate((new Date()).getDate() - 90),
				//minDate : new Date()
			};
		
		$scope.dateOptionsTo = {
				formatYear : 'yyyy',
				showWeeks : false,
				minDate : new Date()
			};
		
		$scope.openDatePicker = function() { 
			$scope.datepicker.opened = true;
		};
		
		$scope.openDatePickerTo = function() {
			$scope.datepickerTo.opened = true;
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
		$scope.pharmacyServiceId = cookieObjectAppConfig.pharmacyServiceId;
		$scope.isAppointmentRequired = cookieObjectAppConfig.isAppointmentRequired;
		$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
		$scope.selfPayeeIdCookies = cookieObjectAppConfig.selfPayeeId;
		$scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
		$scope.isPinPadRequired = cookieObjectAppConfig.isPinPadRequired;
		$scope.pinpadAmounFlag = false;
		$scope.excessAmount = parseFloat(0).toFixed(2);	
		$scope.unitId = cookieObject.unitId;
		$scope.userId = cookieObject.userId;
		$scope.organizationId = cookieObject.organizationId;
		$scope.userId =$scope.userId;
		
		
		
		
	
		
		
		$scope.searchBills = function(billSearch)
		{
			console.log("dddd"+$scope.billSearch);
			//return false;
			$scope.miscellaneousExpenseList = [];
			$rootScope.startLoader();
			var billSearchObj = angular.copy(billSearch);

			if($scope.billSearch.fromDate != '' && $scope.billSearch.toDate != '' && typeof($scope.billSearch.fromDate) != "undefined" && typeof($scope.billSearch.toDate) != "undefined"){
				billSearchObj.fromDate = $rootScope.getFormattedDateMisc($scope.billSearch.fromDate,false);
				billSearchObj.toDate = $rootScope.getFormattedDateMisc($scope.billSearch.toDate,false);
			}
			
			var URI = BASE_URL + ROOT_URL + "/api/billing/companyReceipts/list";
			BillingGenericService.serviceAction(METHOD_POST,URI,billSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					$scope.companyReceiptsExpenseList = response.data.listObject;
					$rootScope.stopLoader();
				}else{
					$rootScope.stopLoader();
				}
			});
		
	}
		
		$scope.initBillSearchObj = function(){
				//$scope.billSearch = {fromDate:"",toDate:"",visitTypeId:"",billNo:"",visitNo:"",billStatusId:"",patientId:"",orgId:$scope.organizationId,unitId:$scope.unitId,isOtcReg:false};
			$scope.billSearch = {fromDate:new Date(),toDate:new Date(),companyTypeId:"",billNumber:"",billStatus:"",companyId:"",insuranceCompanyId:"",orgId:$scope.organizationId,unitId:$scope.unitId};
			$scope.dateOptionsTo.minDate = $scope.billSearch.fromDate;
		}
		
		$scope.init = function(){
			
			
			$scope.billSearch = {};
			$scope.initBillSearchObj();
			$scope.searchBills($scope.billSearch);
			var URI = BASE_URL + ROOT_URL
			+ GETGENERALLEDGERMASTERACTIVELIST
			+ "/" + $scope.organizationId;
	var data = {

	}
	GenericService
			.serviceAction("POST", URI, data)
			.then(
					function(response) {
						$scope.accountHeadList = [];

						if (response.data.status == "success") {
							$rootScope.stopLoader();
							$scope.accountHeadList = response.data.listObject;
						}
					});
			
		}
		$scope.init();
		$scope.initBillSearchObj();
		
		  $scope. getCompaniesByCompanyType = function(companyTypeId){
		    	
		    	
		    	if(companyTypeId != ""){
			    	
		    		if(companyTypeId==3){
			    		$scope.disableInsurance=true;
			    		$scope.insuranceCompanyId = null;
		    		}
			    	else{
			    		$scope.disableInsurance=false;
			    	}
		    		
		    		var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyTypeId:companyTypeId};
				    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getCompanyMasterList";
				    $rootScope.startLoader();
				    BillingGenericService.serviceAction(METHOD_POST,URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						if(response.data.status == "success"){
							$scope.companies = [];
							$scope.companies = response.data.listObject;
						}
						$rootScope.stopLoader();
					});
		    	}else{
		    		$scope.insuranceCompanyId = null;
		    	}
		    }
		
		   $scope.getInsuranceByCompanyId = function(companyId){
		    	if(companyId != ""){
			    	var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyId:companyId};
				    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getAssociateCompanyMaster";
				    $rootScope.startLoader();
				    BillingGenericService.serviceAction(METHOD_POST,URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						if(response.data.status == "success"){
							$scope.associateCompanies = [];
							 $scope.associateCompanies = response.data.listObject;
						}
						$rootScope.stopLoader();
					});
		    	}
		    }
		    
		
		
		$scope.cancelClick = function(item){
			$scope.cancelObj = {};
			$scope.cancelObj = item;
		}
		
		$scope.cancelRequest = function(){
			
			var data = {
						"companyReceiptsExpenseId":$scope.cancelObj.companyReceiptsExpenseId,
			}
			$rootScope.startLoaderPopup();
			var URI = BASE_URL + ROOT_URL + API + BILLING + "/CompanyContract/cancel";
			BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_TRUE).then(function(response)
			{
				if(response.data.status == "success"){
					$rootScope.stopLoaderPopup();
					$scope.init();
			}else{
					$rootScope.stopLoaderPopup();
			}
			});
		}
		
		
		$scope.getSingleCheck = function(item,idx){
			//requestSentTo
			if(item.miscellaChk){
				var URI = BASE_URL + ROOT_URL + "/billing/users/expense/rights/"+$scope.organizationId+"/"+$scope.unitId+"/"+item.amount;
				BillingGenericService.serviceAction(METHOD_GET,URI, {}, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
				{
					if(response.data.status == "success"){
						$scope.companyReceiptsExpenseList[idx].userList = [];
						$scope.companyReceiptsExpenseList[idx].userList = response.data.listObject;
						$scope.companyReceiptsExpenseList[idx].requestSentTo = response.data.listObject[0].userId;
				}else{
				}
				});
			}else{
				$scope.companyReceiptsExpenseList[idx].requestSentTo  = "";
			}
			
			
		}
		
		$scope.checkAll = function(chkAll){
			if(chkAll){
			angular.forEach($scope.companyReceiptsExpenseList,function(value,index)
					 {
						if(value.billStatus == 2 && (value.reqStatus == null || value.reqStatus == '') ){
							value.miscellaChk = true;
							
							var URI = BASE_URL + ROOT_URL + "/billing/users/expense/rights/"+$scope.organizationId+"/"+$scope.unitId+"/"+value.amount;
							BillingGenericService.serviceAction(METHOD_GET,URI, {}, NOTIFICATION_MSG_STATUS_FALSE).then(function(response)
							{
								if(response.data.status == "success"){
									value.userList = [];
									value.userList = response.data.listObject;
									value.requestSentTo = response.data.listObject[0].userId;
							}else{
							}
							});
					 }
					 });
		}else{
			angular.forEach($scope.companyReceiptsExpenseList,function(value,index)
					 {
							value.miscellaChk = false;
							if(value.billStatus == 2 && (value.reqStatus == null || value.reqStatus == '') ){
							value.requestSentTo = "";
							}
						
					 });
		}
		}
		
		$scope.saveReuestToUser = function(){
			
			if($scope.companyReceiptsExpenseList.length == 0){
				growl.warning("No list for save!!",{
					title : WARNING_MSG
				});
				return false;
			}else{
				
				var i = 1;
				var flg = false;
				var tmpList = [];
    			angular.forEach($scope.companyReceiptsExpenseList,function(value,index)
						 {
					        	
					        	   if(value.miscellaChk){
					        		   flg = true; 
					        		   var tmp = {};
					        		   tmp.companyReceiptsExpenseId = value.companyReceiptsExpenseId;
					        		   tmp.amount = value.amount;
					        		   tmp.requestSentTo = value.requestSentTo;
					        		   tmpList.push(tmp);
					        	   }
					        	
					        	   if(i == $scope.companyReceiptsExpenseList.length){
					        		   if(flg == true){

						        		   $rootScope.startLoader();
						        		   var data = {
						        					"unitId": $scope.unitId,
						        				    "orgId":  $scope.organizationId,
						        				    "requestSentBy": $scope.userId,
						        					"approvalDtosList" : tmpList
						        				}
						        		   
						        		   
						   			$rootScope.startLoaderPopup();
						   			var URI = BASE_URL + ROOT_URL + API + BILLING + "/companyReceipts/requet/cancel"; 
						   			BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_TRUE).then(function(response)
						   			{
						   				if(response.data.status == "success"){
						   					$rootScope.stopLoaderPopup();
						   					$scope.init();
						   			}else{
						   					$rootScope.stopLoaderPopup();
						   			}
						   			});
					        		   }else{
					        			   growl.warning("Please check atleast one invoice..!!",{
						    					title : WARNING_MSG
						    				});
						    				return false;
					        	   }
					        	   }
					       
							i++;	
						 });
    			
			}
		}
		
		
		$scope.printReport = function(item){
			
			var data =  
			{
		            "appName": "HIS",
		            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
		            "reportDesignFilePath": "get_miscellaneous_Company_expense.rpt",
		            "reportFormat": "PDF",
		            "exportFileName": "Expense",
		            "reportParams": [
		              {
			             "paramName": "@expense_id",
			             "paramType": "integer",
			             "paramValue": item.companyReceiptsExpenseId
		              }
		              
		            ]
		         };
			reportService.generate(REPORT_API_URI1,data);
		}
		
		
		$scope.gotoDetails = function(item){
			
			var tmpObj = {
					type:"1",
					companyReceiptsExpenseId : item.companyReceiptsExpenseId,
					companyId : item.companyId,
					companyTypeId : item.companyTypeId,
					insuranceCompanyId : item.insuranceCompanyId,
					amount : item.amount,
					billDateTime : $rootScope.getFormatedDateeShase(item.billDateTime),
					billNumber: item.billNumber,
					receivedAmount: item.receivedAmount,
					reqStatus: item.reqStatus,
					name: item.name,
					remark:item.remark
			}
			$state.go("addNewCompanyReceipts",{
				miscellaneousObj :tmpObj
			});
		}
		
		
		$scope.gotoUpdate = function(item){
			
			console.log(item);
			//return false;
			var tmpObj = {
					type:"2",
					companyReceiptsExpenseId : item.companyReceiptsExpenseId,
					companyId : item.companyId,
					companyTypeId : item.companyTypeId,
					insuranceCompanyId : item.insuranceCompanyId,
					amount : item.amount,
					billDateTime : $rootScope.getFormatedDateeShase(item.billDateTime),
					billNumber: item.billNumber,
					receivedAmount: item.receivedAmount,
					name: item.name,
					remark:item.remark
			}
			$state.go("addNewCompanyReceipts",{
				miscellaneousObj :tmpObj
			});
		}
	}catch(e){
		console.log(e.message);
	}

	$scope.getCompanyReceiptConsumption=function(obj){
		$rootScope.startLoader();
		var data={
				companyReceiptsExpenseId:obj.companyReceiptsExpenseId
			}
		var URI = BASE_URL + ROOT_URL + "/api/billing/companyReceipts/companyReceiptsConsumeptionDtls";
		BillingGenericService.serviceAction("POST",URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
		{
			$scope.companyReceiptConsumptionList = [];
			if (response.data.status == "success")
				{
				 $scope.companyReceiptConsumptionList = response.data.listObject;
				 if($scope.companyReceiptConsumptionList!=null && $scope.companyReceiptConsumptionList.length>0)
					 {
					   $('#consumedLinkModal').modal('show');
					 }
				 
				  $rootScope.stopLoader();
				}
			else if (response.data.status == "error"){
				 $rootScope.stopLoader();
			   }
		});
	}
}]);