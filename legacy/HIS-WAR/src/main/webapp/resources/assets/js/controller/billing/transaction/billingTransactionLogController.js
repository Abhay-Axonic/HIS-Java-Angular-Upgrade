(function(){
	'use strict';
	
	function billingTransactionLogController($scope,$rootScope,BillingGenericService,growl,$cookies,$state,PagerService){
		var self = this;
		
		function init(){
			$rootScope.loginpage = true;
			
			self.model ={
					fromDate:new Date(),
					toDate:new Date(),
					userId:"",
					userName:""
				}
			
			//self.model.numPerPage = 10;
			//self.model.searchTransactions = searchTransactions;
			self.model.initSearchTransactions =initSearchTransactions;
			self.model.transactionLogsList =[];
			self.model.getUsers = getUsers;
			self.model.selectUserId = selectUserId;
			//self.model.getBillTransactionList = getBillTransactionList;

			/**Variables for pagination */
			self.model.offset = 0;
			self.model.noOfRecordsPerPage = 10;
			self.model.totalNoOfRecords;
			self.model.searchKeyword;
			self.model.currentPage = 1;
			self.model.maxSize = 5;
			//self.model.numPages = numPages;
			self.model.filteredTransactions = [];
			self.model.allTransactionLogs = [];
			
			
			/*** Check user is Loggedin */
			var cookieObject,cookieObjectAppConfig;
			[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
			self.model.unitId = cookieObject.unitId;
			self.model.orgId = cookieObject.organizationId;
			self.model.userId = cookieObject.userId;
			
			//$scope.setPage(1, false);
		}
		
		var initSearchTransactions = function(offset, noOfRecordsPerPage){
			$rootScope.startLoader();
			console.log("offset", offset);
			var data = "";
			offset = offset != null ? offset : 0;
			noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
			
			var url = BASE_URL + ROOT_URL + "/api/billing/transaction/logs";
			var fromDate = moment(self.model.fromDate.getTime()).format('DD-MM-YYYY');
			var toDate = moment(self.model.toDate.getTime()).format('DD-MM-YYYY');
			
			var param = {fromDateTransaction:fromDate,toDateTransaction:toDate,createdUserId:self.model.userId,offset:offset,noOfRecords:noOfRecordsPerPage};
			BillingGenericService.serviceAction(METHOD_POST,url,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status == "success"){
					self.model.transactionLogsList = response.data.listObject;
					self.model.allTransactionLogs = response.data.listObject;
					console.log("transaction log ",self.model.allTransactionLogs);
					//self.model.totalNoOfRecords = self.model.transactionLogsList.length;
					$scope.setPage(1, false);
					$rootScope.stopLoader();
				}
			});
			$rootScope.stopLoader();
		}
		
		
		
		
		/*var searchTransactions = function(offset, noOfRecordsPerPage){
			self.model.transactionLogsList = [];
			$rootScope.startLoader();
			console.log("offset", offset);
			var data = "";
			offset = offset != null ? offset : 0;
			noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
			
			var url = BASE_URL + ROOT_URL + "/api/billing/transaction/logs";
			var fromDate = moment(self.model.fromDate.getTime()).format('DD-MM-YYYY');
			var toDate = moment(self.model.toDate.getTime()).format('DD-MM-YYYY');
			
			var param = {fromDateTransaction:fromDate,toDateTransaction:toDate,createdUserId:self.model.userId,offset:offset,noOfRecords:noOfRecordsPerPage};
			BillingGenericService.serviceAction(METHOD_POST,url,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				if(response.data.status == "success"){
					self.model.transactionLogsList = response.data.listObject;
					//self.model.totalNoOfRecords = self.model.transactionLogsList.length;
					//$scope.setPage(1, false);
					$rootScope.stopLoader();
				}
			});
			$rootScope.stopLoader();
		}*/
		
		var getUsers = function(key){
			
			var obj = {
						userName : key,
						organizationId :self.model.orgId,
						unitId:self.model.unitId
					};
			
			var URI = BASE_URL + ROOT_URL + "/adt/getUsersByName";
			
			return BillingGenericService
			.serviceAction(METHOD_POST, URI,
					obj,
					NOTIFICATION_MSG_STATUS_FALSE)
			.then(
					function(response) {
						console.log(response);
						if (response.data.status == "success")
							return response.data.listObject;
					});
		}
		
		var selectUserId = function(info,model,label){
			self.model.userId = info.userId;
			self.model.userName = info.userName;
		}
		
		
		  
		  	$scope.pager = {};
			$scope.page;

			$scope.setPage = function(page, flag) {
				/*if (page < 1 || page > $scope.pager.totalPages)
				{
					return;
				}*/
				/*if(self.model.noOfRecordsPerPage != 10 && page > 1){
					page = 1;
				}*/
				var startRecord = ( page * self.model.noOfRecordsPerPage ) - self.model.noOfRecordsPerPage;
				var endRecord = ( page * self.model.noOfRecordsPerPage );
				self.model.transactionLogsList = self.model.allTransactionLogs.slice(startRecord,endRecord); 
				$scope.pager = PagerService.GetPager(self.model.allTransactionLogs.length, page, self.model.noOfRecordsPerPage);
				/*if (flag)
				{
					self.model.searchTransactions($scope.pager.startIndex, $scope.pager.pageSize);
				}*/
			}
			
			
			$scope.setNoOfRecords = function(page,flag){
				if(self.model.noOfRecordsPerPage != 10 && page > 1){
					page = 1;
				}
				var startRecord = ( page * self.model.noOfRecordsPerPage ) - self.model.noOfRecordsPerPage;
				var endRecord = ( page * self.model.noOfRecordsPerPage );
				self.model.transactionLogsList = self.model.allTransactionLogs.slice(startRecord,endRecord); 
				$scope.pager = PagerService.GetPager(self.model.allTransactionLogs.length, page, self.model.noOfRecordsPerPage);
			}
			
		
		init();
	}
	function config($stateProvider){
		$stateProvider.state('transactionLog',{
			url:'/transactionLog',
			templateUrl:'views/master/GlobalMaster/transaction-log.html',
			controller:'TransactionLog.Controller',
			controllerAs:'vm',
			title: 'Transaction Log',
		});
	}

	angular.module("myApp").config(config).controller('TransactionLog.Controller',billingTransactionLogController);
})();