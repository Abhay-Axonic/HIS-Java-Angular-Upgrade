
/**
 * @Author Sneha Dalave
 * @name myApp.controller:corporateStatementController
 * @description #corporateStatementController Controller of the myApp
 */
(function () {
	"use strict";
	function corporateStatementMasterController($scope,$rootScope,BillingGenericService,$cookies,$state,growl,
			servicesFactory,itemFactory,reportService,PaginationService){
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
		
		
		var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
		 if(cookieObjectAppConfig == undefined){
			 $state.go('login');
		  return;
		 }
		 else
		  {
		   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
		  }
		 $scope.isConsolidatedBill=cookieObjectAppConfig.isConsolidatedBill;
		 
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
			$scope.popup7 =
			{
				opened : false
			};
			
			$scope.open1 = function() 
			{
				$scope.popup1.opened = true;
			};
			$scope.open7 = function() 
			{
				$scope.popup7.opened = true;
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
				$scope.getCompanyMasterList(3);//companyId 3
				$scope.companyInvoiceObj.paymentEntitlementId="";
				$scope.companyInvoiceObj.companyId="";
				$scope.detailsCompanyInvoiceCreationList=null;
				
			}
		
			 $scope.searchObj = {
							 fromDate:new Date(),
							 toDate:new Date(),
							 orgId:$scope.orgId,
							 unitId:$scope.unitId,
							 companyId:"",
							 uhidNumber:""
						 };
			 $scope.visitTypeList = [
				{
						id:3,
						label:"Day Care"
					},
					{
						id:4,
						label:"ER"
					},
					{
						id:2,
						label:"IP"
					},
					{
						id:1,
						label:"OP"
					},
				];
				$scope.getPaymentEntitlementList = function() {
					var data = "";
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/global/paymententitlement/"+$scope.orgId;
					BillingGenericService.serviceAction("GET", URI, data).then(function(response){
						$scope.paymentEntitlementList= response.data.listObject;
						//console.log("paymentEntitlementList",JSON.stringify($scope.paymentEntitlementList));
						
						$rootScope.stopLoader();
					});
					
			}
				
				$scope.getCompanyMasterList = function(companyTypeId) {
					if(companyTypeId != ""){
					if(companyTypeId==3){
						
						$scope.isDisable=true;
						$scope.associateCompanyId = '';
			    		$scope.companyId = '';
						
					}else{
						$scope.isDisable=false;
					}
					$rootScope.startLoader();
					  var param = {organizationId:$scope.orgId,unitId:$scope.unitId,companyTypeId:companyTypeId};
					    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getCompanyMasterList"
					    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							if (response.data.status == "success"){
								 $scope.companyMasterList = response.data.listObject;
								 //console.log("companyMasterList",JSON.stringify($scope.companyMasterList));
							}
							$rootScope.stopLoader();
						});
					}else{
						$scope.associateCompanyId = '';
			    		$scope.companyId = '';
					}
					  }
				
				$scope.getAssociateCompanyMasterList = function(companyId) {
					$scope.tempcompanyId=companyId;
					$rootScope.startLoader();
					  var param = {organizationId:$scope.orgId,unitId:$scope.unitId,companyId:companyId};
					    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getAssociateCompanyMaster"
					    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							if (response.data.status == "success"){
								 $scope.associateCompanyMasterList = response.data.listObject;
							}
							$rootScope.stopLoader();
						});
					  }
			 
				
				
				$scope.getCompanyInvoiceListByFilter = function(){
			    	if($scope.searchObj.companyId == ""){
			    		growl.error("Please select a company",{
			    			title : ERROR_MSG
			    		});
			    		return;
			    	}
			    	var obj = angular.copy($scope.searchObj);
			    	obj.fromDate = $rootScope.getFormattedDate(obj.fromDate,false);
			    	obj.toDate = $rootScope.getFormattedDate(obj.toDate,false);
			    	obj.multidelete("paymentEntitlementId","companyTypeId","associateCompanyId");
			    	var URI = BASE_URL + ROOT_URL + "/api/billing/companyinvoices/pending";
			    	$rootScope.startLoader();
				    BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						if(response.data.status == "success"){
							$scope.companyInvoiceList = response.data.listObject;
							 if($scope.companyInvoiceList.length > 0){            
		                            PaginationService.searchData($scope.companyInvoiceList , $scope.pagination);
		                          }
						}
						$rootScope.stopLoader();
					});
			    }
			    
			    $scope.getCompanyInvoiceDetailsByInvoiceId = function(item){
				    	var URI = BASE_URL + ROOT_URL + "/api/billing/invoicedtls/invoice/"+item.companyInvoiceId;
				    	$rootScope.startLoader();
					    BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
							if(response.data.status == "success"){
								 $scope.invoiceDetails = self.model.invoiceDetails.concat(response.data.listObject);
								 $scope.NavigateToDetail(item);
							}
							$rootScope.stopLoader();
						});
			    }
				
			    
			  
			 $scope.NavigateToDetail = function (item) {
					var corporateStatmentObject = 
					{
							"companyInvoiceId" :item.companyInvoiceId,
							"invoiceDate" :item.invoiceDate,
							"invoiceNumber" :item.invoiceNumber,
							"companyName" :item.companyName,
							
					}
					 sessionStorage.setItem('corporateStatmentObject', JSON.stringify(corporateStatmentObject));
		            $state.go('detailsCorporateStatement');
		        }
			 
			 $scope.init();
			 
		
	}catch(e){
		console.log(e.message);
	}
}
function config($stateProvider) {
	$stateProvider.state('corporateStatement', {
		url: '/corporateStatement',
		templateUrl: 'views/master/UnitMaster/billingMaster/corporate-statement.html',
		controller: "corporateStatementMasterController",
		title: 'Corporate Statement',
	})
}

angular
.module("myApp")
.config(config)
.controller("corporateStatementMasterController", corporateStatementMasterController);
})();