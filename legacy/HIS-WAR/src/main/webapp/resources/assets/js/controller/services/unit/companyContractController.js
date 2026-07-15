angular.module("myApp").controller("companyContractController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','GenericService','PagerService','$timeout',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,growl,GenericService,PagerService,$timeout){
	try{
		$rootScope.loginpage = true;
		
		$scope.init = function(){
		}
		var cookieObject = $cookies.getObject('cookieObject');
		if (cookieObject == undefined) {
			$state.go('login');
			return;
		}
		$scope.unitId = cookieObject.unitId;
		$scope.orgId = cookieObject.organizationId;
		$scope.userId = cookieObject.userId;
		//$scope.init();
		
		
		/** Variables for pagination */
		$scope.offset = 0;
		$scope.noOfRecordsPerPage = 10;
		$scope.totalNoOfRecords;
		$scope.searchKeyword;
		$scope.commonListCount;
		
		
		$scope.getPackageTypeList = function(orgId,unitId) {
		    var data = "";
		    var URI = BASE_URL + ROOT_URL + "/Unit/Service/EHCpackage/getPackgeType/"+orgId+"/"+unitId;
		    BillingGenericService.serviceAction("GET", URI, data).then(function(response){
		     $scope.packageTypeList = response.data.listObject;
		    });
		  }
		$scope.getPackageTypeList($scope.orgId,$scope.unitId);
		
		
		$scope.getPaymentEntitlementList = function() {
			var data = "";
			var URI = BASE_URL + ROOT_URL + "/global/paymententitlement/"+$scope.orgId;
			BillingGenericService.serviceAction("GET", URI, data).then(function(response){
				$scope.paymentEntitlementList= response.data.listObject;
				//console.log("paymentEntitlementList",JSON.stringify($scope.paymentEntitlementList));
			});
	   }
		$scope.getPaymentEntitlementList();
		
		$scope.getAssociateCompanyMasterList = function(searchPackageObj) {
			  var param = {organizationId:$scope.orgId,unitId:$scope.unitId};
			    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getAssociateCompanyMaster"
			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						 $scope.associateCompanyMasterList = response.data.listObject;
						 // console.log("associateCompanyMasterList",JSON.stringify($scope.associateCompanyMasterList));
					}
				});
			  }
			$scope.getAssociateCompanyMasterList();
			$scope.getGradeListByAssociateCompanyId = function(searchPackageObj) {
				  var param = {organizationId:$scope.orgId,unitId:$scope.unitId};
				    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getGradeListByAssociateCompanyId"
				    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success"){
							 $scope.gradeListByAssociateCompanyIdList = response.data.listObject;
							 // console.log("gradeListByAssociateCompanyIdList",JSON.stringify($scope.gradeListByAssociateCompanyIdList));
						}
					});
				  }
				$scope.getGradeListByAssociateCompanyId();
				
				$scope.getCompanyMasterList = function(searchPackageObj) {
					  var param = {organizationId:$scope.orgId,unitId:$scope.unitId};
					    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getCompanyMasterList"
					    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							if (response.data.status == "success"){
								 $scope.companyMasterList = response.data.listObject;
								// console.log("companyMasterList",JSON.stringify($scope.companyMasterList));
							}
						});
					  }
					$scope.getCompanyMasterList();
					
					
		  $scope.init = function(){
			  $scope.initSearchComapnyContarctObj();
			  $scope.searchComapnyContarctList = [];
		  }
		  $scope.initSearchComapnyContarctObj = function(){
			 $scope.companyContarctObj = {orgId:$scope.orgId,unitId:$scope.unitId,paymentEntitlementId:"",companyId:"",associateCompanyId:"",tariffId:""};
		  }
		  $scope.init();
		  
		  
		  $scope.getCompanyMasterList = function(companyTypeId) {
				if(companyTypeId==3){
					$scope.isDisable=true;
					//$scope.configPatientCategoryId = $scope.companyPatientCategory;
					
				}else{
					$scope.isDisable=false;
					//$scope.configPatientCategoryId = $scope.companyPatientCategory;
				}
					
				  var param = {organizationId:$scope.orgId,unitId:$scope.unitId,companyTypeId:companyTypeId};
					$rootScope.startLoader();
				    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getCompanyMasterList"
				    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success"){
							 $scope.companyMasterList = response.data.listObject;
							 //console.log("companyMasterList",JSON.stringify($scope.companyMasterList));
						}
						$rootScope.stopLoader();
					});
				  }
		  
		  
		  $scope.getAssociateCompanyMasterList = function(companyId) {
			  var param = {organizationId:$scope.orgId,unitId:$scope.unitId,companyId:companyId};
				$rootScope.startLoader();
			    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getAssociateCompanyMaster"
			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						 $scope.associateCompanyMasterList = response.data.listObject;
						//  console.log("associateCompanyMasterList",JSON.stringify($scope.associateCompanyMasterList));
					}
					$rootScope.stopLoader();
				});
			    $rootScope.startLoader();
			    //for company Detaills
			    var URI = BASE_URL + ROOT_URL+"/api/global/getCompanyContractGradeByCompanyId/"+companyId;
				GenericService
						.serviceAction("GET", URI, {})
						.then(function(response) {
							if (response.data.status == "success") {
								$scope.gradeListByAssociateCompanyIdList = response.data.listObject;
								
									}
							   $rootScope.stopLoader();
								});
			
			    //End For Compan Details
			  }
		  $scope.getLedgerList = function(keyword)
		    {
		        $scope.ledgerList=[];
		       var URI = BASE_URL + ROOT_URL
		       + GETGENERALLEDGERMASTERACTIVELIST+"/"+$scope.orgId;
		       var data = {
		               
		           }
		        GenericService
		       .serviceAction("POST", URI, data)
		       .then(
		               function(response) {
		                   if (response.data.status == "success") {
		                       $rootScope.stopLoader();
		                       $scope.ledgerList=response.data.listObject;
		                   }
		               });
		    }
		  $scope.getLedgerList();
		//for active/inactive
			// Function For update Contract status
			$scope.updateStatus = function(id, type) {
				$scope.currentDate = new Date();
				$scope.currentDate = moment($scope.currentDate)
						.format('DD-MM-YYYY HH:mm:ss');

				//type = (type == "A") ? "A" : "I";

			/*	var data = {
					contractId : id,
					status : type,
					updatedBy : $scope.userId,
					updatedDate : $scope.currentDate,
					organizationId : $scope.organizationId
				}
				console.log(data);*/
				var URI = BASE_URL + ROOT_URL
						+ "/unit/CompanyContract/contract/status/"+type+"/"+id;
				GenericService
						.serviceAction("GET", URI, {})
						.then(
								function(response) {

									if (response.data.status == "success") {
										growl
												.success(
														'Updated Successfully...!!',
														{
															title : 'Success!'
														});
									} else {
										growl
												.error(
														'Something went wrong...!!',
														{
															title : 'Error!'
														});
									}
								});
			}
			//End For active/inactive
			
		  
		  $scope.getInitSearchServicesList = function(orgId,unitId, offset,
					noOfRecordsPerPage) {
			  $rootScope.startLoader();
			  $scope.searchComapnyContarctList=[];
			  offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
						: 10;
			  //var param = {orgId:$scope.orgId,unitId:$scope.unitId,paymentEntitlementId:"",companyId:"",associateCompanyId:"",tariffId:""};
			  var param = {orgId:$scope.orgId,unitId:$scope.unitId,paymentEntitlementId:$scope.companyContarctObj.paymentEntitlementId,companyId:$scope.companyContarctObj.companyId,associateCompanyId:$scope.companyContarctObj.associateCompanyId,tariffId:$scope.companyContarctObj.tariffId, noOfRecordsPerPage : noOfRecordsPerPage,offset : offset};
			    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/searchCompanyContract"
			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						 $scope.searchComapnyContarctList = response.data.listObject;
						 angular.forEach($scope.searchComapnyContarctList, function(value1 , key) {
                             angular.forEach($scope.ledgerList, function(value , key) {
                                 if(value1.generalLedgerId==value.generalLedgerId)
                                     {
                                     value1.generalLedgerName=value.generalLedgerName;
                                     value1.generalLedgerId=value.generalLedgerId;
                                     }
                             });
                         })
						 $rootScope.stopLoader();
						 if(response.data.object>0)
						 $scope.commonListCount= response.data.object;
							$scope.setPage(1,false);
						/*console.log("searchComapnyContarctList",JSON.stringify($scope.searchComapnyContarctList));*/
					}
					else
						{
						 $rootScope.stopLoader();
						}
				});
			  }
		  $scope.getInitSearchServicesList($scope.orgId,$scope.unitId, $scope.offset,$scope.noOfRecordsPerPage);
		  
		  $scope.getSearchServicesList = function(orgId,unitId, offset,
					noOfRecordsPerPage) {
			  $rootScope.startLoader();
			  $scope.searchComapnyContarctList=[];
			  offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage
						: 10;
			  //var param = {orgId:$scope.orgId,unitId:$scope.unitId,paymentEntitlementId:"",companyId:"",associateCompanyId:"",tariffId:""};
			  var param = {orgId:$scope.orgId,unitId:$scope.unitId,paymentEntitlementId:$scope.companyContarctObj.paymentEntitlementId,companyId:$scope.companyContarctObj.companyId,associateCompanyId:$scope.companyContarctObj.associateCompanyId,tariffId:$scope.companyContarctObj.tariffId, noOfRecordsPerPage : noOfRecordsPerPage,offset : offset};
			    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/searchCompanyContract"
			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						 $scope.searchComapnyContarctList = response.data.listObject;
						 $rootScope.stopLoader();
                         angular.forEach($scope.searchComapnyContarctList, function(value1 , key) {
                             angular.forEach($scope.ledgerList, function(value , key) {
                                 if(value1.generalLedgerId==value.generalLedgerId)
                                     {
                                     value1.generalLedgerName=value.generalLedgerName;
                                     value1.generalLedgerId=value.generalLedgerId;
                                     }
                             });
                         })
						 if(response.data.object>0)
						 $scope.commonListCount= response.data.object;
//							$scope.setPage(1,false);
						/*console.log("searchComapnyContarctList",JSON.stringify($scope.searchComapnyContarctList));*/
					}
					else
						{
						 $rootScope.stopLoader();
						}
				});
			  }
		  
		  $scope.getCompanyContractMasterSearch = function(searchText) {
			 if(searchText!=''){
				 
			 
			  $rootScope.startLoader();
			  $scope.searchComapnyContarctList=[];
			  //var param = {orgId:$scope.orgId,unitId:$scope.unitId,paymentEntitlementId:"",companyId:"",associateCompanyId:"",tariffId:""};
			  var param = {orgId:$scope.orgId,unitId:$scope.unitId,paymentEntitlementId:$scope.companyContarctObj.paymentEntitlementId,companyId:$scope.companyContarctObj.companyId,associateCompanyId:$scope.companyContarctObj.associateCompanyId,tariffId:$scope.companyContarctObj.tariffId, noOfRecordsPerPage : $scope.noOfRecordsPerPage,offset : $scope.offset, assCompanyName:searchText};
			    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/CompanyContractListSearch"
			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						 $scope.searchComapnyContarctList = response.data.listObject;
						 $rootScope.stopLoader();
						 if(response.data.listObject.length>0)
						 $scope.commonListCount=  response.data.listObject.length;
							$scope.setPage(1,false);
						/*console.log("searchComapnyContarctList",JSON.stringify($scope.searchComapnyContarctList));*/
					}
					else
						{
						 $rootScope.stopLoader();
						}
				});
			    
			 }
			 else{
				 $scope.getInitSearchServicesList($scope.orgId,$scope.unitId, $scope.offset,$scope.noOfRecordsPerPage);
			 }
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
					$scope.getSearchServicesList($scope.organizationId,$scope.unitId, $scope.pager.startIndex, $scope.pager.pageSize);
				}
			}
			$scope.setNoOfRecords = function() {
				 $scope.getInitSearchServicesList($scope.orgId,$scope.unitId, $scope.offset,$scope.noOfRecordsPerPage);
			};
		 
			$scope.getParentCompanyContractDetails = function(info){
				
				if(info.parentContractId > 0){
					//angular.element('#contractDetailsForVersion').modal('show');
					 $rootScope.startLoaderPopup();
					 	var param = {orgId:$scope.orgId,unitId:$scope.unitId,parentContractId:info.parentContractId};
					 	var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/searchCompanyContractForVersion"
					 	
					 	BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							if (response.data.status == "success"){
								 $scope.searchComapnyContarctListForVersion = response.data.listObject;
								 $rootScope.stopLoaderPopup();
							}
							else
								{
								 $rootScope.stopLoader();
								}
						});
				}
			}
			
			
			 $scope.getCompanyContractDetailsVersion = function(info){
					 angular.element('#contractDetailsForVersion').modal('hide');
					  $timeout( function(){
						  $state.go('detailsCompanyContract',
									{
								   		companyContractObj: info
									});
	                },1000);
				}
			 
		  $scope.getCompanyContractDetails = function(info){
			 // angular.element('#contractDetailsForVersion').modal('hide');
			  
			  console.log('ddd',info);
			  $state.go('detailsCompanyContract',
						{
					   		companyContractObj: info
						});
				
			}
		  
		  $scope.showUpdateBtn = function(info){
			  console.log('ddd',info);
			  $state.go('addNewCompanyContract',
						{
					   		companyContractObj: info
						});
		  }
		  
	}catch(e){
		console.log(e.message);
	}
	
	
	
	
	
}]);