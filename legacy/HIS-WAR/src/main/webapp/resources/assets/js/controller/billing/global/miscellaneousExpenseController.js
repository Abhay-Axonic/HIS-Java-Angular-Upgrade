angular.module("myApp").controller("miscellaneousExpenseController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','$timeout','growl','paymentCollFactory','reportService','GenericService','$location','loyaltiCardCallFactory','PaginationService','$window',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,$timeout,growl,paymentCollFactory,reportService,GenericService,$location,loyaltiCardCallFactory,PaginationService,$window){
	try{
		
		$rootScope.loginpage = true;
		$scope.page = "billList";
		var date = new Date();
		$scope.dateOptions = {
				formatYear : 'yyyy',
				showWeeks : false,
				maxDate : new Date(),

				//minDate : date.setDate((new Date()).getDate() - 90),
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
			
			var URI = BASE_URL + ROOT_URL + "/api/billing/expense/list";
			BillingGenericService.serviceAction(METHOD_POST,URI,billSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if (response.data.status == "success"){
					$scope.pagination.data=[];
					$scope.miscellaneousExpenseList = response.data.listObject;
					if(!!response && !!response.data && response.data.listObject.length > 0){            
                        PaginationService.searchData($scope.miscellaneousExpenseList, $scope.pagination);
                      }
					$rootScope.stopLoader();
				}else{
					$rootScope.stopLoader();
				}
			});
		
	}
		
		$scope.initBillSearchObj = function(){
				//$scope.billSearch = {fromDate:"",toDate:"",visitTypeId:"",billNo:"",visitNo:"",billStatusId:"",patientId:"",orgId:$scope.organizationId,unitId:$scope.unitId,isOtcReg:false};
			$scope.billSearch = {fromDate:new Date(),toDate:new Date(),accountHead:"",billNumber:"",billType:"",billStatus:"",orgId:$scope.organizationId,unitId:$scope.unitId};
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
		
		
		$scope.cancelClick = function(item){
			$scope.cancelObj = {};
			$scope.cancelObj = item;
		}
		
		$scope.cancelRequest = function(){
			
			var data = {

						"expenseId":$scope.cancelObj.expenseId,
			}
			$rootScope.startLoaderPopup();
			var URI = BASE_URL + ROOT_URL + API + BILLING + "/expense/cancel";
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
						$scope.miscellaneousExpenseList[idx].userList = [];
						$scope.miscellaneousExpenseList[idx].userList = response.data.listObject;
						$scope.miscellaneousExpenseList[idx].requestSentTo = response.data.listObject[0].userId;
				}else{
				}
				});
			}else{
				$scope.miscellaneousExpenseList[idx].requestSentTo  = "";
			}
			
			
		}
		
		$scope.checkAll = function(chkAll){
			if(chkAll){
			angular.forEach($scope.miscellaneousExpenseList,function(value,index)
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
			angular.forEach($scope.miscellaneousExpenseList,function(value,index)
					 {
							value.miscellaChk = false;
							if(value.billStatus == 2 && (value.reqStatus == null || value.reqStatus == '') ){
							value.requestSentTo = "";
							}
						
					 });
		}
		}
		
		$scope.saveReuestToUser = function(){
			
			if($scope.miscellaneousExpenseList.length == 0){
				growl.warning("No list for save!!",{
					title : WARNING_MSG
				});
				return false;
			}else{
				
				var i = 1;
				var flg = false;
				var tmpList = [];
    			angular.forEach($scope.miscellaneousExpenseList,function(value,index)
						 {
					        	
					        	   if(value.miscellaChk){
					        		   flg = true; 
					        		   var tmp = {};
					        		   tmp.expenseId = value.expenseId;
					        		   tmp.amount = value.amount;
					        		   tmp.requestSentTo = value.requestSentTo;
					        		   tmpList.push(tmp);
					        	   }
					        	
					        	   if(i == $scope.miscellaneousExpenseList.length){
					        		   if(flg == true){

						        		   $rootScope.startLoader();
						        		   var data = {
						        					"unitId": $scope.unitId,
						        				    "orgId":  $scope.organizationId,
						        				    "requestSentBy": $scope.userId,
						        					"approvalDtosList" : tmpList
						        				}
						        		   
						        		   
						   			$rootScope.startLoaderPopup();
						   			var URI = BASE_URL + ROOT_URL + API + BILLING + "/expense/requet/approval";
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
					        			   growl.warning("Please check atleast one bill..!!",{
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
	                 "reportDesignFilePath": "get_miscellaneous_expense.rpt",
	                 "reportFormat": "PDF",
	                 "exportFileName": "miscellaneousExpenseReport",
	                 "reportParams": [
					                 	{
							                  "paramName": "@expense_id",
							                  "paramType": "Integer",
							                  "paramValue": item.expenseId 
						                }
						                
						              ]
			  };
			reportService.generate(REPORT_API_URI1,data);
		}
		
		
		$scope.gotoDetails = function(item){
			
			var tmpObj = {
					type:"1",
					expenseId : item.expenseId,
					accountHead : item.accountHead,
					amount : item.amount,
					billDateTime : $rootScope.getFormatedDateeShase(item.billDateTime),
					billNumber: item.billNumber,
					billStatus: item.billStatus,
					billType: item.billType,
					receivedAmount: item.receivedAmount,
					reqStatus: item.reqStatus,
					name: item.name,
					remark:item.remark
			}
			$state.go("addNewMiscellaneousExpense",{
				miscellaneousObj :tmpObj
			});
		}
		
		
		$scope.gotoUpdate = function(item){
			
			var tmpObj = {
					type:"2",
					expenseId : item.expenseId,
					accountHead : item.accountHead,
					amount : item.amount,
					billDateTime : $rootScope.getFormatedDateeShase(item.billDateTime),
					billNumber: item.billNumber,
					billStatus: item.billStatus,
					billType: item.billType,
					receivedAmount: item.receivedAmount,
					reqStatus: item.reqStatus,
					name: item.name,
					remark:item.remark
			}
			$state.go("addNewMiscellaneousExpense",{
				miscellaneousObj :tmpObj
			});
		}
		
		
		$scope.getPandCard = function(billId){

			var URI = BASE_URL + ROOT_URL + "/billing/PanCard/url/"+billId+"/"+3; //1 for Deposit screen
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
		
	}catch(e){
		console.log(e.message);
	}
}]);