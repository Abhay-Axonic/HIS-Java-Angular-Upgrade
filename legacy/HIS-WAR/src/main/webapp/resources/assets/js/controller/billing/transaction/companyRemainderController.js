angular.module("myApp").controller("companyRemainderController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','servicesFactory','itemFactory','PaginationService',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,growl,servicesFactory,itemFactory,PaginationService){
	try{
		$rootScope.loginpage = true;
		
		var cookieObject = $cookies.getObject('cookieObject');
		if (cookieObject == undefined) {
			$state.go('login');
			return;
		}
		$scope.unitId = cookieObject.unitId;
		$scope.orgId = cookieObject.organizationId;
		$scope.userId = cookieObject.userId;
		
		/**DatePicker Code  */
		
		$scope.fromDate = moment().startOf('day').toDate();
	    $scope.toDate = moment().startOf('day').toDate();
	    $scope.completed='N';
		$scope.pending = 'N';
		$scope.outsourced = 'N';
		$scope.isDisable=false;
		$scope.dateOptions =
		{
			formatYear : 'yyyy',
			showWeeks : false,
			maxDate : "",
		};

		$scope.open = function() {
			$scope.popup.opened = true;
		};

		$scope.popup =
		{
			opened : false
		};
		
		$scope.popup1 =
		{
			opened : false
		};
		
		$scope.open1 = function() 
		{
			$scope.popup1.opened = true;
		};

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
			
		$scope.init = function(){
			$scope.getPaymentEntitlementList();
			$scope.getCompanyMasterList(3);
			$scope.paymentEntitlementId="";
			$scope.companyId="";
		}
		
		
		
		$scope.getPaymentEntitlementList = function() {
			var data = "";
			var URI = BASE_URL + ROOT_URL + "/global/paymententitlement/"+$scope.orgId;
			BillingGenericService.serviceAction("GET", URI, data).then(function(response){
				$scope.paymentEntitlementList= response.data.listObject;
				console.log("paymentEntitlementList",JSON.stringify($scope.paymentEntitlementList));
			});
	}
		
		
		$scope.getAssociateCompanyMasterList = function(companyId) {
			$scope.tempcompanyId=companyId;
			  var param = {organizationId:$scope.orgId,unitId:$scope.unitId,companyId:companyId};
			    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getAssociateCompanyMaster"
			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						 $scope.associateCompanyMasterList = response.data.listObject;
						  console.log("associateCompanyMasterList",JSON.stringify($scope.associateCompanyMasterList));
					}
				});
			  }
			
			$scope.getCompanyMasterList = function(companyTypeId) {
				
				if(companyTypeId != ""){
				if(companyTypeId==3){
					$scope.isDisable=true;
					$scope.companyContarctObj.associateCompanyId = '';
					$scope.companyId = '';
					
				}else{
					$scope.isDisable=false;
				}
					
				  var param = {organizationId:$scope.orgId,unitId:$scope.unitId,companyTypeId:companyTypeId};
				    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getCompanyMasterList"
				    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success"){
							 $scope.companyMasterList = response.data.listObject;
							 console.log("companyMasterList",JSON.stringify($scope.companyMasterList));
						}
					});
				}else{
					$scope.companyContarctObj.associateCompanyId = '';
					$scope.companyId = '';
				}
				  }
			
			$scope.searchCompanyRemainder = function() {
				
				var fromDate=undefined?"":moment($scope.fromDate.getTime()).format('DD-MM-YYYY HH:mm:ss');
			   var toDate=undefined?"":moment($scope.toDate.getTime()).format('DD-MM-YYYY HH:mm:ss');
				
				var payeeId= $scope.companyId;
				var paymentEntitlementId=$scope.paymentEntitlementId; //"paymentEntitlementId":paymentEntitlementId,
				  var param = {"orgId":$scope.orgId,"orgUnitId":$scope.unitId,"payeeId":payeeId,
						  "fromDate":fromDate,"toDate":toDate};
				    var URI = BASE_URL + ROOT_URL + "/api/billing/getcompanyRemainderList"
				    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success"){
							 $scope.pagination.data = [];
							 
							 $scope.companyRemainderList = response.data.listObject;
							 
							 if($scope.companyRemainderList.length > 0){            
		                            PaginationService.searchData($scope.companyRemainderList , $scope.pagination);
		                          }
							  console.log("companyInvoiceList",JSON.stringify($scope.companyInvoiceList));
						}
					});
				  }
			
			$scope.deatilsCompanyInvoiceList = function(billId,payeeId) {
				  var param = {orgId:$scope.orgId,orgUnitId:$scope.unitId,billId:billId,payeeId:payeeId};
				    var URI = BASE_URL + ROOT_URL + "/api/billing/detailsCompanyInvoiceCreationList"
				    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success"){
							 $scope.detailsCompanyInvoiceCreationList = response.data.listObject;
							 if($scope.detailsCompanyInvoiceCreationList!=null){
								 
								 $state.go('detailsCompanyInvoiceCreation',
											{
									          detailsCompanyInvoiceList : $scope.detailsCompanyInvoiceCreationList
											}); 
							 }else {
							    growl.error("Something went Wrong.",{title : 'Error!'});
						     }
							
							  console.log("detailsCompanyInvoiceCreationList",JSON.stringify($scope.detailsCompanyInvoiceCreationList));
						}
					});
				  }
			
			
			
			/** code for setting all checkboxes */
			$scope.checkAll = function() {
				if ($scope.selectedAll) {
					$scope.selectedAll = true;
				} else {
					$scope.selectedAll = false;
				}
				angular
						.forEach(
								 $scope.companyInvoiceList,
								function(companyMasterObj) {
									companyMasterObj.doctorShare = $scope.selectedAll;
								});
			}
			

			$scope.searchCompanyRemainder(1);
			
			$scope.saveCompanyReminder=function(info ,index ){
				$scope.remiderObj={};
				$scope.remiderObj=info;
				$scope.indexObj=index;
			}
			
			$scope.saveCompanyReminderData=function(){
				
				var param ={
						   "companyInvoiceReminderId":"",
						   "companyTypeId":$scope.remiderObj.companyTypeId,
						   "companyId":$scope.remiderObj.companyId,
						   "payeeId":$scope.remiderObj.companyId,
						   "invoiceId":$scope.remiderObj.invoiceId,
						   "reminderDate":$rootScope.getFormattedDate($scope.reminderDate,false),
						   "remark":$scope.noteRemark,
						   "response":"",
						   "unitId":$scope.unitId,
						   "orgId":$scope.orgId,
						   "createdBy":$scope.userId,
						   "updatedBy":"",
						   "createdDate":"",
						   "updatedDate":"", 
						
						}
				 console.log("param",JSON.stringify(param));
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + "/api/billing/saveCompanyReminder";
				BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
					if(response.data.status == "success"){
						$scope.companyRemainderList[$scope.indexObj].details = response.data.object;
					}
				});
				
			}
			
			$scope.showDetails = function(details,indexObj){
				if (details != undefined) {
					$scope.reminderDetails = angular.copy(details);
				}else{
					$scope.reminderDetails = angular.copy($scope.companyRemainderList[indexObj]);
					/*var tempData={
							$scope.reminderDetails = angular.copy($scope.companyRemainderList[indexObj]);
							//"remark":$scope.companyRemainderList[indexObj].remark,
							//"createdDate":$scope.companyRemainderList[indexObj].response.data.listObject.createdDate
					}*/
					
					
				}
				
				
			}
			
	
			
			$scope.updateCompanyRemarkData=function(reminderDetails){
				var param ={
						   "companyInvoiceReminderId":reminderDetails.companyInvoiceReminderId,
						   "response":reminderDetails.response,
						}
				 console.log("param",JSON.stringify(param));
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + "/api/billing/updateCompanyReminderRemark";
				BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
					if(response.data.status == "success"){
						$scope.companyRemainderList[$scope.indexObj].details = response.data.object;
					}
				});
				
			}

				$scope.init();
			
			
		
	}catch(e){
		console.log(e.message);
	}
}]);	