(function(){
	'use strict';
	
	function settledInvoicesMasterController($scope, $rootScope, BillingGenericService, growl, $cookies, $state, paymentCollFactory,reportService ,PaginationService ){
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
		        },
		        pagination: {
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
		        };
			
			
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
			self.model.searchObj = {fromDate:new Date(),toDate:new Date(),orgId:self.model.orgId,unitId:self.model.unitId,companyId:"",companyObj:{},insuranceCompanyObj:{}};
			self.model.OpenFromDate = OpenFromDate;
			self.model.OpenToDate = OpenToDate;
			self.model.getCompaniesByCompanyType = getCompaniesByCompanyType;
			self.model.getInsuranceByCompanyId = getInsuranceByCompanyId;
			self.model.getCompanyInvoiceListByFilter = getCompanyInvoiceListByFilter;
			self.model.getCompanyInvoiceDetailsByInvoiceId = getCompanyInvoiceDetailsByInvoiceId;
			
			self.model.invoiceDetails = [];
			
		
			$scope.dateOptions = {
					formatYear : 'yyyy',
					showWeeks : false
				};
			$scope.datepicker = {opened : false};
			$scope.page = "companyInvoiceSettlement";
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
		    		self.model.searchObj.insuranceCompanyObj = null;
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
	    		self.model.searchObj.insuranceCompanyObj = null;
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
	    	if(self.model.searchObj.companyObj == null ||  self.model.searchObj.companyObj.companyId == ""){
	    		growl.error("Please select a company",{
	    			title : ERROR_MSG
	    		});
	    		return;
	    	}
	    	//var obj = angular.copy(self.model.searchObj);
	    	var obj = {};
	    	obj.orgId = self.model.searchObj.orgId;
	    	obj.unitId = self.model.searchObj.unitId;
	    	obj.companyId = self.model.searchObj.companyObj.id;
	    	obj.companyTypeId = parseInt(self.model.searchObj.companyTypeId);
	    	obj.fromDate  = self.model.searchObj.fromDate;
	    	obj.toDate  = self.model.searchObj.toDate;
	    	obj.fromDate = moment(obj.fromDate).format('YYYY-MM-DD');
	    	obj.toDate = moment(obj.toDate).format('YYYY-MM-DD');
	    	obj.multidelete("associateCompanyId");
	    	var URI = BASE_URL + ROOT_URL + "/api/billing/companyinvoices/settleDetails";
	    	$rootScope.startLoader();
		    BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status == "success"){
					
					 self.model.pagination.data=[];
					 self.model.invoices = response.data.listObject;
					 
					 if(self.model.invoices.length > 0){            
                         PaginationService.searchData(self.model.invoices, self.model.pagination);
                       }
				}
				$rootScope.stopLoader();
			});
	    }
	    
	    
	    $scope.printBill=function(info)
	    {
			//alert(bill.billingMasterId);
			if(info.billType==0)
			{
				var URIVALI = BASE_URL + ROOT_URL + "/api/billing/printLog/1/"+$scope.userId+"/"+info.billId;
				BillingGenericService.serviceAction(METHOD_GET,URIVALI, {},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
				{
					if (response.data.status == "success")
					{

						if(info.visitTypeId==1)
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
					                  "paramValue": info.billId
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
						                 "reportDesignFilePath": "Ipdbill.rpt",
						                 "reportFormat": "PDF",
						                 "exportFileName": "IPDBillReciept",
						                 "reportParams": [
						                	 {
								                  "paramName": "Billid",
								                  "paramType": "Integer",
								                  "paramValue": info.billId
							                },
							                {
								                  "paramName": "@patient_id",
								                  "paramType": "Integer",
								                  "paramValue": info.patientId
							                },
							                {
								                  "paramName": "IsSummary",
								                  "paramType": "String",
								                  "paramValue": 'N'
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
				            "paramValue": info.billId
				          }
				        ]
				      };
				      reportService.generate(REPORT_API_URI1, data);
			}
			
			
		}
	    $scope.printInvoice=function(info)
	    {
	    	var data = {
				        "appName": "HIS",
				        "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
				        "reportDesignFilePath": "companyInvoice.rpt",
				        "reportFormat": "PDF",
				        "exportFileName": "CompanyInvoice",
				        "reportParams": [
				          {
				            "paramName": "invoiceId",
				            "paramType": "Integer",
				            "paramValue": info.companyInvoiceId
				          }
				        ]
				      };
	    	reportService.generate(REPORT_API_URI1, data);
		}
	    
	    var getCompanyInvoiceDetailsByInvoiceId = function(id,flag){
	    	if(flag){
	    		//get invoice details
	    		self.model.invoiceDetails = [];
		    	var URI = BASE_URL + ROOT_URL + "/api/billing/companyinvoices/settledetails/byInvoiceId";
		    	$rootScope.startLoader();
		    	var data={
		    			companyInvoiceId:id
		    	}
		    	
			    BillingGenericService.serviceAction("POST",URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						 self.model.invoiceDetails = self.model.invoiceDetails.concat(response.data.listObject);
						 console.log( self.model.invoiceDetails);
					}
					$rootScope.stopLoader();
				});
	    	}
	    	
	    }
		
		init();
	}
	
	function config($stateProvider){
		$stateProvider.state('settledInvoices', {
			 url: '/settledInvoices',
			 title:'Settled Invoices',
			 templateUrl: 'views/master/UnitMaster/billingMaster/settled-invoices.html',
			 controller: "SettlementInvoices.Controller",
			 controllerAs : "vm"
		});
	}
	
	angular.module("myApp").config(config).controller("SettlementInvoices.Controller",settledInvoicesMasterController);
})();