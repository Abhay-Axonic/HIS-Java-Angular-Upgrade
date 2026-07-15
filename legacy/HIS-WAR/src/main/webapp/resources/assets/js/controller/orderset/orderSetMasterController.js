(function(){
	'use strict';
	
	function ordersetMasterController($scope, $rootScope, BillingGenericService, growl, $cookies, $state, dataService,$location,GenericServiceParamHeader,promiseFactoryWithObject,PagerService){
		var self = this;
		function init(){
			$rootScope.loginpage = true;
			self.model = {};
			var cookieObject = {}; var cookieObjectAppConfig = {};
			[cookieObject, cookieObjectAppConfig] = $rootScope.checkCookies();
			self.model.userId = cookieObject.userId;
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
			
			self.model.getOrderSetList = getOrderSetList;
			self.model.masterList = masterList;
			self.model.getOrderSetListSearch = getOrderSetListSearch;
			self.model.getOrderSetDetails = getOrderSetDetails;
			self.model.changeStatusOrderSet = changeStatusOrderSet;
			self.model.ordersetList = [];
			self.model.getOrderSetList(0,10);
			$scope.serchKeyWord = '';
			//chetana
			self.model.getOrderSetDetailsByIdForEdit = getOrderSetDetailsByIdForEdit;
			self.model.orderset = {
					                ordersetName:"",
					                ordersetTypeId:1,
					                specialityId:null,
					                doctorId:"",
					                status:'A',
					                createdBy:self.model.userId,
					                diagnosisId:"",
					                orgId:self.model.orgId,
					                unitId:self.model.unitId
					               }
			self.model.investigation = {
					                     serviceId:null,
					                     specialityId:null,
					                     subSpecilaityId:null,
					                     orgId:self.model.orgId,
					                     unitId:self.model.unitId,
					                     createdBy:self.model.userId
					                    };
			self.model.medication = 
		    {itemMasterId:null,
				brand:"",
				genericName:"",
				dose:"",
				routeId:null,
				doseUnitId:null ,
				frequencyId:null,
				genericId: null,
				formulationTypeId:null,
				frequency:"",
				instructionId:null,
				instruction:"",
				orgId:self.model.orgId,
				unitId:self.model.unitId,
				createdBy:self.model.userId
				};
	
	
		self.model.formulationTypeId;
		self.model.genericId;
		self.model.specialityList = [];
		self.model.investigations = [];
		self.model.medications = [];
		self.model.doseList = [];
		self.model.raoList = [];
		$scope.medicationInstructions = [];
			

		}
		
		$scope.setNoOfRecords = function() {
			getOrderSetList($scope.offset, $scope.noOfRecordsPerPage);
		};
		
		 var masterList = function(offset,recordPerPage) {
			var offset;
			var recordPerPage;
		  $rootScope.startLoader();
		  var URI1 = BASE_URL + ROOT_URL + "/api/orderset/orderSetsList";
		  var URI2 = BASE_URL + ROOT_URL + "/api/orderset/orderSetsCount";
		  var param = { 
				  orgId:self.model.orgId,
				  unitId:self.model.unitId,
				  offset:offset,
				  recordPerPage:recordPerPage};
		  promiseFactoryWithObject.setPromisesWithObject(URI1, URI2, METHOD_POST,METHOD_POST,param,param).then(function(response)
		    {
	    		 self.model.ordersetList = response[0].listObject;
	    		 $scope.commonListCount = response[1].object;

	    		 self.model.ordersetListForICPC = response[0].listObject2;
	    		 $scope.commonListCountForICPC = response[1].resObject;
	    		 $rootScope.stopLoader();
		    		
		    });
		    
	  }
		
		$scope.pager = {};
		$scope.page;

		$scope.setPage = function(page, flag) {
			if (page < 1 || page > $scope.pager.totalPages) {
				return;
			}
			$scope.pager = PagerService.GetPager(
					$scope.commonListCount, page,
					$scope.noOfRecordsPerPage);
			if (flag) {
				masterList($scope.pager.startIndex,
						$scope.pager.pageSize);
				
			}
		}	
	  var getOrderSetList = function(offset,recordPerPage)
	  {
			var offset;
			var recordPerPage;
		  $rootScope.startLoader();
		  var URI1 = BASE_URL + ROOT_URL + "/api/orderset/orderSetsList";
		  var URI2 = BASE_URL + ROOT_URL + "/api/orderset/orderSetsCount";
		  var param = { 
				  orgId:self.model.orgId,
				  unitId:self.model.unitId,
				  offset:offset,
				  recordPerPage:recordPerPage};
		  promiseFactoryWithObject.setPromisesWithObject(URI1, URI2, METHOD_POST,METHOD_POST,param,param).then(function(response)
		    {
	    		 self.model.ordersetList = response[0].listObject;
	    		 $scope.commonListCount = response[1].object;

	    		 self.model.ordersetListForICPC = response[0].listObject2;
	    		 $scope.commonListCountForICPC = response[1].resObject;

	    		 $scope.setPage(1,false);
	    		 $rootScope.stopLoader();
		    		
		    });
		    
	  }
	  

	  
	  var getOrderSetListSearch = function()
	  {
		  $rootScope.startLoader();
		  if($scope.serchKeyWord!=''&&$scope.serchKeyWord!=undefined &&$scope.serchKeyWord!=null)
			  {
			  var URI = BASE_URL + ROOT_URL + "/api/orderset/orderSetSearch/"+self.model.unitId+'/'+$scope.serchKeyWord+'/'+$scope.orderSetActiveTab;
			    BillingGenericService.serviceAction("GET", URI, {}).then(function(response)
			    {
			    	if(response.data.status == "success")
			    		{
			    		if($scope.orderSetActiveTab==1){
			    		    self.model.ordersetList = response.data.listObject;
			    		} else{
			    		   self.model.ordersetListForICPC = response.data.listObject;
			    		}
			    		 $rootScope.stopLoader();
			    		}
			    	else if(response.data.status == "error")
			    		{
			    		 $rootScope.stopLoader();
			    		}
			    });
			  }
		  else{
			  growl
				.error(
						'Please enter a search keyword.',
						{
							title : 'Error!'
						});
				   $rootScope.stopLoader();
				}
		 
	  }
	  var getOrderSetDetails = function(ordersetMasterId)
	  {
		  $rootScope.startLoader();
		  var URI = BASE_URL + ROOT_URL + "/api/orderset/orderSetsDetails";
		  var param = {orgId:self.model.orgId,unitId:self.model.unitId,ordersetMasterId:ordersetMasterId,orderSetActiveTab:$scope.orderSetActiveTab};
		    BillingGenericService.serviceAction("POST", URI, param).then(function(response)
		    {
		    	if(response.data.status == "success")
		    		{
		    		 $scope.orderSetObj = response.data.object;
		    		 $("#ordersetDetailsModal").modal('show');
		    		 $scope.setFirstTabLevel(1)
		    		 $rootScope.stopLoader();
		    		}
		    	else if(response.data.status == "error")
		    		{
		    		 $rootScope.stopLoader();
		    		}
		    });
	  }
	  var getOrderSetDetailsByIdForEdit = function(ordersetMasterId)
	  {
		  $rootScope.startLoader();
		  var URI = BASE_URL + ROOT_URL + "/api/orderset/orderSetsDetails";
		  var param = {orgId:self.model.orgId,unitId:self.model.unitId,ordersetMasterId:ordersetMasterId,orderSetActiveTab:$scope.orderSetActiveTab};
		    BillingGenericService.serviceAction("POST", URI, param).then(function(response)
		    {
		    	if(response.data.status == "success")
		    		{
		    		 $scope.orderSetObjd = response.data.object;
		    		 $scope.orderSetObjd.ordersetMaster.orgId = $scope.orderSetActiveTab;
		    		 
		    		 $state.go('addNewOrderSetMaster',
								{
								 obj :  $scope.orderSetObjd,
								});
		    		 $rootScope.stopLoader();
		    		}
		    	else if(response.data.status == "error")
		    		{
		    		 $rootScope.stopLoader();
		    		}
		    });
	  }
	  
	  var changeStatusOrderSet = function(ordersetMasterId,status)
	  {
		  $rootScope.startLoader();
		  var URI = BASE_URL + ROOT_URL + "/api/orderset/inactivateOrderSet/"+ordersetMasterId+'/'+status;
		  var param = {ordersetMasterId:ordersetMasterId,status:status};
		    BillingGenericService.serviceAction("POST", URI, param).then(function(response)
		    {
		    	if(response.data.status == "success")
		    		{
		    		growl.success(response.data.message,
							{
								title : 'Success!'
							});
		    		self.model.getOrderSetList(0,10);
		    		 $rootScope.stopLoader();
		    		}
		    	else if(response.data.status == "error")
		    		{
		    		growl.error(response.data.message,
							{
								title : 'Error!'
							});
		    		 $rootScope.stopLoader();
		    		}
		    });
	  }
	 
	  init();

//	  ####################################################################Complaint Mapping##############################################################################

	   $scope.orderSetActiveTab = 1;
       $scope.setDiagnosisTab = function () {
          $scope.orderSetActiveTab = 1;
       };

       $scope.setComplaintTab = function () {
          $scope.orderSetActiveTab = 2;
       };

       $scope.goToDefineOrderset = function () {
          $scope.objPass = {
                  orderSetActiveTab:$scope.orderSetActiveTab
          };
          $state.go('addNewOrderSetMaster', {
                   obj : $scope.orderSetActiveTab,
          });
       };


	}  
	function config($stateProvider){
		$stateProvider.state('orderSetMaster', {
			 url: '/orderSetMaster',
			 templateUrl: 'views/master/UnitMaster/organizationMaster/orderset-master.html',
			 controller: "OrdersetMaster.Controller",
			 title: 'Order Set Master',
				resolve : {

		   			data: function($q, $state, $timeout) {
		   		        var deferred = $q.defer();
		   		
		   		        $timeout(function() {
		   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
		   				 	.modules.filter(function(item){
		   	            	   return (item.id == 698 || item.id == 707);
		   				 	});
		   					if(arr.length <= 0){
		   						$state.go("accessDenied")
		   						deferred.reject();
		   					 } else {
		   						deferred.resolve();
		   					 }
		   		        });
		   		
		   		        return deferred.promise;
		   		      }
		   		 },
			 controllerAs : "vm",
			 params: {
		 	        obj: {}
		 	    }
			})
	}


	angular.module("myApp").config(config).controller("OrdersetMaster.Controller",ordersetMasterController);
})();