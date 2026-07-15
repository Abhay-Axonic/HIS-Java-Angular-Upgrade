(function(){
	'use strict';
	
	function companyQueryResponseController($scope, $rootScope, BillingGenericService, growl, $cookies, $state){
		var self = this;
		
		function init(){
			$rootScope.loginpage = true;
			self.model = {
				fromDateOptions: {
		          formatYear: 'yyyy',
		          showWeeks: false,
		          maxDate: new Date()
		        },
		        toDateOptions: {
		          formatYear: 'yyyy',
		          showWeeks: false,
		          maxDate: new Date()
		        }};
			var cookieObject = {}; var cookieObjectAppConfig = {};
			
			[cookieObject, cookieObjectAppConfig] = $rootScope.checkCookies();
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			if(cookieObjectAppConfig == undefined){
				$state.go('login');
				return;
			}
			
			self.model.unitId = cookieObject.unitId;
			self.model.orgId = cookieObject.organizationId;
			self.model.userId = cookieObject.userId; 
			self.model.searchObj = {fromDate:new Date(),toDate:new Date(),orgId:self.model.orgId,unitId:self.model.unitId,companyId:""};
			self.model.OpenFromDate = OpenFromDate;
			self.model.OpenToDate = OpenToDate;
			self.model.getCompaniesByCompanyType = getCompaniesByCompanyType;
			self.model.getInsuranceByCompanyId = getInsuranceByCompanyId;
			self.model.getCompanyInvoiceListByFilter = getCompanyInvoiceListByFilter;
			self.model.getCompanyInvoiceDetailsByInvoiceId = getCompanyInvoiceDetailsByInvoiceId;
			self.model.calculateSummaryAndValidate = calculateSummaryAndValidate;
			self.model.calculateSummary = calculateSummary;
			self.model.invoiceDetailsSummary = {receivedAmount:$rootScope.parseFloatNum(0),tds:$rootScope.parseFloatNum(0),otherDeduction:$rootScope.parseFloatNum(0),
												disallowedAmount:$rootScope.parseFloatNum(0),securityDepositSettlement:$rootScope.parseFloatNum(0),total:$rootScope.parseFloatNum(0)};
			self.model.invoiceDetails = [];
			self.model.saveCompanyQueryResponseData = saveCompanyQueryResponseData;
			/*self.model.mCompanyInvoiceQueryMasterDto = { "companyInvoiceQueryId":"","invoiceId":"","invoiceDetailId":"",
														"companyId":"",
														"queryStatusId":"1",
														"orgId":self.model.orgId,
														"unitId":self.model.unitId,
														"updatedBy":"",
														"createdBy":self.model.userId,
														"createdDate":"",
														"updatedDate":""
														};*/
			self.model.tCompanyInvoiceQueryDetailDto = {
					                                    "invoiceDetailId":"",
					                                    "orgId":self.model.orgId,
														"unitId":self.model.unitId,
														"queryStatusId":"1",
														"companyInvoiceQueryDetailId":"",
														"queryDetails":"",
														"remark":"",
														"responseDetails":"",
														"queryDate":"",
														"responseDate":"",
														"updatedBy":"",
														"companyId":"",
														"createdBy":self.model.userId,
														"createdDate":"",
														"updatedDate":""};
			self.model.callSaveCompanyQuery=callSaveCompanyQuery;
			self.model.companyQueryObj={};
			self.model.indexObj;
			self.model.queryStatusId;
			self.model.listTCompanyInvoiceQueryDetailDto = [];
			self.model.pendingAndResolvedQueryDetails = [];
			self.model.getPendingQueryListByQueryStatusId=getPendingQueryListByQueryStatusId;
			self.model.updatePendingQueryDetails = updatePendingQueryDetails;
			self.model.detailsCompanyInvoiceCreationList  = [];
			self.model.deatilsCompanyInvoiceList=deatilsCompanyInvoiceList;
			$scope.disableInsurance = true;
		}
		
		var OpenFromDate = function () {
	      self.model.fromDateOpened = true;
	    }

	    var OpenToDate = function () {
	      self.model.toDateOpened = true;
	    }
	    
	    var getCompaniesByCompanyType = function(companyTypeId){
	    	if(companyTypeId != ""){
	    		

	    		if(companyTypeId==3){
		    		$scope.disableInsurance=true;
		    		self.model.searchObj.associateCompanyId = '';
	    		}
		    	else{
		    		$scope.disableInsurance=false;
		    	}
	    		
		    	var param = {organizationId:self.model.orgId,unitId:self.model.unitId,companyTypeId:companyTypeId};
			    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getCompanyMasterList";
			    $rootScope.startLoader();
			    BillingGenericService.serviceAction(METHOD_POST,URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						 self.model.companies = response.data.listObject;
					}
					$rootScope.stopLoader();
				});
	    	}else{
	    		self.model.searchObj.associateCompanyId = '';
	    		self.model.searchObj.companyId = '';
	    	}
	    }
	    
	    var getInsuranceByCompanyId = function(companyId){
	    	if(companyId != ""){
		    	var param = {organizationId:self.model.orgId,unitId:self.model.unitId,companyId:companyId};
			    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getAssociateCompanyMaster";
			    $rootScope.startLoader();
			    BillingGenericService.serviceAction(METHOD_POST,URI, param, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						 self.model.associateCompanies = response.data.listObject;
					}
					$rootScope.stopLoader();
				});
	    	}
	    }
	    
	    var getCompanyInvoiceListByFilter = function(){
	    	if(self.model.searchObj.companyId == ""){
	    		growl.error("Please select a company",{
	    			title : ERROR_MSG
	    		});
	    		return;
	    	}
	    	var obj = angular.copy(self.model.searchObj);
	    	obj.fromDate = $rootScope.getFormattedDate(obj.fromDate,false);
	    	obj.toDate = $rootScope.getFormattedDate(obj.toDate,false);
	    	obj.multidelete("companyTypeId","associateCompanyId");
	    	var URI = BASE_URL + ROOT_URL + "/api/billing/companyinvoices/pending";
	    	$rootScope.startLoader();
		    BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status == "success"){
					 self.model.invoices = response.data.listObject;
				}
				$rootScope.stopLoader();
			});
	    }
	    
	    var getCompanyInvoiceDetailsByInvoiceId = function(id,flag){
	    	if(flag){
	    		//get invoice details
		    	var URI = BASE_URL + ROOT_URL + "/api/billing/invoicedtls/invoice/"+id;
		    	$rootScope.startLoader();
			    BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						 self.model.invoiceDetails = self.model.invoiceDetails.concat(response.data.listObject);
					}
					$rootScope.stopLoader();
				});
	    	}else{
	    		//remove invoice details
	    		$rootScope.startLoader();
	    		for(let i = (self.model.invoiceDetails.length - 1) ; i >= 0; i--){
	    			if(self.model.invoiceDetails[i].companyInvoiceId == id)
	    				self.model.invoiceDetails.splice(i,1);
	    		}
	    		self.model.calculateSummary();
	    		$rootScope.stopLoader();
	    	}
	    }
	    
	    var calculateSummaryAndValidate = function(invdtl,key){
	    	var balanceAmount = parseFloat(invdtl.outstanding);
	    	var totalAmount = parseFloat(parseFloat($rootScope.parseFloatNum(invdtl.receivedAmount)) + parseFloat($rootScope.parseFloatNum(invdtl.tds)) + parseFloat($rootScope.parseFloatNum(invdtl.otherDeduction)) 
	    							+ parseFloat($rootScope.parseFloatNum(invdtl.disallowedAmount)) + parseFloat($rootScope.parseFloatNum(invdtl.securityDepositSettlement)));
	    	
	    	if(totalAmount > balanceAmount){
	    		growl.error("Total should be less than balance amount",{
	    			title : ERROR_MSG
	    		});
	    		invdtl.total = $rootScope.parseFloatNum(totalAmount - parseFloat(invdtl[key]));
	    		invdtl[key] = parseFloat(0);
	    	}else{
	    		invdtl.total = $rootScope.parseFloatNum(totalAmount);
	    	}
	    	self.model.calculateSummary();
	    	
	    }
		
	    var calculateSummary = function(){
	    	//calculate summary
	    	var receivedAmount = 0,tds = 0,otherDeduction = 0,disallowedAmount = 0,total = 0;
	    	self.model.invoiceDetails.forEach(function(value,index){
	    		receivedAmount = parseFloat(receivedAmount) + parseFloat($rootScope.parseFloatNum(value.receivedAmount));
	    		tds = parseFloat(tds) + parseFloat($rootScope.parseFloatNum(value.tds));
	    		otherDeduction = parseFloat(otherDeduction) + parseFloat($rootScope.parseFloatNum(value.otherDeduction));
	    		disallowedAmount = parseFloat(disallowedAmount) + parseFloat($rootScope.parseFloatNum(value.disallowedAmount));
	    		total = parseFloat(total) + parseFloat($rootScope.parseFloatNum(value.total));
	    		
	    	});
	    	self.model.invoiceDetailsSummary = {receivedAmount:$rootScope.parseFloatNum(receivedAmount),tds:$rootScope.parseFloatNum(tds),total:$rootScope.parseFloatNum(total),
	    									otherDeduction:$rootScope.parseFloatNum(otherDeduction),disallowedAmount:$rootScope.parseFloatNum(disallowedAmount)};
	    	
	    }
	    
	    
	   var callSaveCompanyQuery=function(invdtlObj ,index ){
		   self.model.companyQueryObj=invdtlObj;
		   self.model.indexObj=index;
		}
	    
	    
		var saveCompanyQueryResponseData = function(){
			
			var tCompanyInvoiceQueryDetailDtoObj = angular.copy(self.model.tCompanyInvoiceQueryDetailDto);
			tCompanyInvoiceQueryDetailDtoObj.invoiceDetailId =self.model.companyQueryObj.companyInvoiceDetailId;
			tCompanyInvoiceQueryDetailDtoObj.companyId =self.model.searchObj.companyId;
			self.model.tCompanyInvoiceQueryDetailDto.queryDate = $rootScope.getFormattedDate(self.model.tCompanyInvoiceQueryDetailDto.queryDate,false);
			//self.model.listTCompanyInvoiceQueryDetailDto=[self.model.tCompanyInvoiceQueryDetailDto];
			var param ={
					   "orgId":	self.model.orgId,
					   "unitId":self.model.unitId,
					   "tCompanyInvoiceQueryDetailDto":tCompanyInvoiceQueryDetailDtoObj
					}
			 console.log("param",JSON.stringify(param));
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + "/api/billing/saveCompanyQueryResponse";
			BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
				if(response.data.status == "success"){
					self.model.invoiceDetails[self.model.indexObj].details = response.data.object;
				}
				$rootScope.stopLoader();
			});
			
		}
		
		
       var getPendingQueryListByQueryStatusId = function(queryStatusId,companyInvoiceQueryDetailId){
			
			var param ={
					   "orgId":	self.model.orgId,
					   "unitId":self.model.unitId,
					   "queryStatusId":queryStatusId,
					   "companyInvoiceQueryDetailId":companyInvoiceQueryDetailId
					}
			$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + "/api/billing/getPendingCompanyQueryResponseDetails";
			BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
				if(response.data.status == "success"){
					self.model.pendingAndResolvedQueryDetails = response.data.listObject;
				}
				$rootScope.stopLoader();
			});
			
		}
	    
        // update pending query details
        var updatePendingQueryDetails = function(){
        	var tempList = [];
        	self.model.pendingAndResolvedQueryDetails.forEach((elem) => {if(elem.isSelected){tempList.push(elem);elem.multidelete("isSelected");}})
        	var pendingQueryDetails = angular.copy(tempList);
        	pendingQueryDetails.forEach((elem) => {
        		elem.responseDate = typeof(elem.responseDate) != "undefined" && elem.responseDate != "" ? $rootScope.getFormattedDate(elem.responseDate,false) : null;
        	});
        	
        	if(pendingQueryDetails.length > 0){
	        	$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + "/api/billing/updateCompanyQueryResponseDetailsRemark";
				BillingGenericService.serviceAction(METHOD_POST,URI, pendingQueryDetails,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
					
					$rootScope.stopLoader();
				});
        	}
        }
        
        
        var deatilsCompanyInvoiceList = function(billId,payeeId) {
			  var param = { "orgId":self.model.orgId, "orgUnitId":self.model.unitId,"billId":billId,"payeeId":payeeId};
			    var URI = BASE_URL + ROOT_URL + "/api/billing/detailsCompanyInvoiceCreationList"
			    
			    BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if(response.data.status == "success"){
						self.model.detailsCompanyInvoiceCreationList=response.data.listObject;
					}
					$rootScope.stopLoader();
				});
			 /*   BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						self.model.detailsCompanyInvoiceCreationList= response.data.listObject;
						 if($scope.detailsCompanyInvoiceCreationList!=null){
							 
							 $state.go('detailsCompanyInvoiceCreation',
										{
								          detailsCompanyInvoiceList : $scope.detailsCompanyInvoiceCreationList
										}); 
						 }else {
						    growl.error("Something went Wrong.",{title : 'Error!'});
					     }
						
					}
				});*/
			  }
        
		init();
	}
	
	function config($stateProvider){
		$stateProvider.state('companyQueryResponseMaster', {
			 url: '/companyQueryResponseMaster',
			 title: 'Company Query & Response Master',
			 templateUrl: 'views/master/UnitMaster/billingMaster/company-query-response-master.html',
			 controller: "CompanyQueryResponse.Controller",
			 controllerAs : "vm"
		});
	}
	
	angular.module("myApp").config(config).controller("CompanyQueryResponse.Controller",companyQueryResponseController);
})();