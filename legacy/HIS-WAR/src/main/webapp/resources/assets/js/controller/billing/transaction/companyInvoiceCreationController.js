angular.module("myApp").controller("companyInvoiceCreationController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','servicesFactory','itemFactory','reportService','PaginationService',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,growl,servicesFactory,itemFactory,reportService,PaginationService){
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
			$scope.getCompanyMasterList(3);//companyId 3
			$scope.companyInvoiceObj.paymentEntitlementId="";
			$scope.companyInvoiceObj.companyId="";
			$scope.detailsCompanyInvoiceCreationList=null;
		}
		 
		 
		 $scope.unitApplicationData = function(tdsPer){
				var URI = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/getUnitApplicationBillingConfigList";
				var obj = {
				            organizationId :$scope.orgId,
				            unitId :$scope.unitId
				        }
				BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
				    if(response.data.status == "success"){
				    	 $scope.unitConfigurationObject ={};
				         $scope.unitConfigurationObject = response.data.listObject[0];
				    }
				});
		    } 
		    $scope.unitApplicationData();
		
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
		
		
		$scope.getAssociateCompanyMasterList = function(companyId) {
			$scope.tempcompanyId=companyId;
			$rootScope.startLoader();
			  var param = {organizationId:$scope.orgId,unitId:$scope.unitId,companyId:companyId};
			    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getAssociateCompanyMaster"
			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						 $scope.associateCompanyMasterList = response.data.listObject;
						 // console.log("associateCompanyMasterList",JSON.stringify($scope.associateCompanyMasterList));
					}
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
			
			$scope.searchCompanyInvoice = function() {
				
				 if($scope.paymentEntitlementId == '' || typeof($scope.paymentEntitlementId) == "undefined"){
						growl.warning("Please select payment entitlement...!!",{
							title : 'Warning'
						});
						
						return false;
					}else{
						
						if($scope.paymentEntitlementId==3){
							$scope.associateCompanyId = '';
							 if($scope.companyId == '' || typeof($scope.companyId) == "undefined"){
									growl.warning("Please select company...!!",{
										title : 'Warning'
									});
									return false;
								}
						}else{
							if( $scope.companyId == '' || typeof($scope.companyId) == "undefined"){
									growl.warning("Please select company and insurance company...!!",{
										title : 'Warning'
									});
									return false;
								}
							if( $scope.associateCompanyId == '' || typeof($scope.associateCompanyId) == "undefined"){
								growl.warning("Please select insurance company...!!",{
									title : 'Warning'
								});
								return false;
							}
						}
						
					}
				 
				
				  var fromDate=undefined?"":$rootScope.getFormattedDate($scope.fromDate.getTime(),false);
				  var toDate=undefined?"":$rootScope.getFormattedDate($scope.toDate.getTime(),false);
				  
				/*
				var fromDate=undefined?"":moment($scope.fromDate.getTime()).format('DD-MM-YYYY HH:mm:ss');
			   var toDate=undefined?"":moment($scope.toDate.getTime()).format('DD-MM-YYYY HH:mm:ss');
				*/
				  $rootScope.startLoader();
				var payeeId= $scope.companyId;
				var paymentEntitlementId=$scope.paymentEntitlementId; 
				
				$scope.companyInvoiceList = [];
				$scope.companyIdForGenerateBill = $scope.companyId;
				$scope.paymentEntitlementIdForGenerateBill = $scope.paymentEntitlementId;
				
				  var param = {
						  "orgId":$scope.orgId,
						  "orgUnitId":$scope.unitId,
						  "payeeId":payeeId,
						  "associateCompanyId":$scope.associateCompanyId,
						  "fromDate":fromDate,
						  "toDate":toDate,
						  "visitTypeId" :$scope.visitTypeId,
					};
						  
				    var URI = BASE_URL + ROOT_URL + "/api/billing/companyInvoiceCreationList"
				    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success"){
							
							 $scope.pagination.data = [];
							 $scope.companyInvoiceList = response.data.listObject;
							 
							 if($scope.companyInvoiceList.length > 0){            
		                            PaginationService.searchData($scope.companyInvoiceList , $scope.pagination);
		                          }
								
							 
							  console.log("companyInvoiceList",JSON.stringify($scope.companyInvoiceList));
						}
						$rootScope.stopLoader();
					});
				  }
			
			$scope.deatilsCompanyInvoiceList = function(billId,payeeId) {
				  $scope.detailsCompanyInvoiceCreationList =[];
				  $rootScope.startLoader();
				  var param = {
						  orgId:$scope.orgId,
						  orgUnitId:$scope.unitId,
						  billId:billId,
						  payeeId:payeeId,
						  associateCompanyId:$scope.associateCompanyId,
				  };
				    var URI = BASE_URL + ROOT_URL + "/api/billing/detailsCompanyInvoiceCreationList"
				    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success"){
							 $scope.detailsCompanyInvoiceCreationList = response.data.listObject;
							 if($scope.detailsCompanyInvoiceCreationList!=null){
								 
								 /*$state.go('detailsCompanyInvoiceCreation',
											{
									          detailsCompanyInvoiceList : $scope.detailsCompanyInvoiceCreationList
											}); */
							 }else {
							    growl.error("Something went Wrong.",{title : 'Error!'});
						     }
							
						}
						$rootScope.stopLoader();
					});
				  }
			
			
			
			/** code for setting all checkboxes */
			$scope.checkAll = function() {
				if ($scope.selectedAll) {
					$scope.selectedAll = true;
				} else {
					$scope.selectedAll = false;
				}
				
				if($scope.searchKeyword == undefined || $scope.searchKeyword == ""){
					angular.forEach($scope.companyInvoiceList,
							function(companyMasterObj) {
								companyMasterObj.companyInvoiceSelected = $scope.selectedAll;
							});
				}else{
					var chkStatus= $scope.companyInvoiceList.filter(function(item)
							{ 
								return (item.billDate == $scope.searchKeyword
	                                    || item.billNumber == $scope.searchKeyword 
	                                    || item.uhidNumber == $scope.searchKeyword 
	                                    || item.patientName == $scope.searchKeyword 
	                                    || item.visitNo == $scope.searchKeyword 
	                                    );
									})
									if(chkStatus.length>0){
										angular.forEach($scope.companyInvoiceList, function(companyMasterObj) {
														if(chkStatus[0].billId == companyMasterObj.billId ){
															companyMasterObj.companyInvoiceSelected = $scope.selectedAll;
														}
												});
									}
				}
			}
			
			$scope.okClick = function() {
				$scope.detailsCompanyInvoiceCreationList=null;
			}
			

			//$scope.searchCompanyInvoice(1);
			/*$scope.searchCompanyInvoice=function(){
				$scope.searchCompanyInvoice($scope.tempcompanyId);
				
			}*/
			
			 $scope.printBill=function(info)
			    {
					//alert(bill.billingMasterId);
					if(info.pharamcy==false)
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
			
			
			
			  $scope.saveCompanyInvoiceCreation = function(){
				  
				  
				  if($scope.paymentEntitlementId == '' || typeof($scope.paymentEntitlementId) == "undefined")
				  {
						growl.warning("Please select Payment Entitlement Type",{
							title : 'Warning'
						});
						return false;
				  }else{
					  if($scope.paymentEntitlementId==3)
						{
								
								 if($scope.companyId == '' || typeof($scope.companyId) == "undefined"){
										growl.warning("Please select Company",{
											title : 'Warning'
										});
										return false;
									}
							
						}else if($scope.paymentEntitlementId==2)
						{
							if( $scope.companyId == '' || typeof($scope.companyId) == "undefined"){
									growl.warning("Please select Company and Insurance Company",{
										title : 'Warning'
									});
									return false;
								}
							if( $scope.associateCompanyId == '' || typeof($scope.associateCompanyId) == "undefined"){
								growl.warning("Please select  Insurance Company",{
									title : 'Warning'
								});
								return false;
							}
						}
							
							var totalAmount = 0;
						    var dueAmount = 0;
						    var crNetAmount = 0;
							angular.forEach($scope.companyInvoiceList,function(value,index){
								if (value.companyInvoiceSelected == true) {
										totalAmount = parseFloat(totalAmount) + (isNaN(value.billAmount) ? 0 : parseFloat(value.billAmount));
										dueAmount = parseFloat(dueAmount) + (isNaN(value.billAmount) ? 0 : parseFloat(value.billAmount));
										crNetAmount = parseFloat(crNetAmount) + (isNaN(value.crNetAmount) ? 0 : parseFloat(value.crNetAmount));
								}
								
							});
							  $scope.mCompanyInvoiceMasterDto={
									  "companyInvoiceId":"",
									  "invoiceNumber":"1",
									  "paymentEntitlementId":$scope.paymentEntitlementIdForGenerateBill,
									  "payeeId":$scope.companyIdForGenerateBill,
									  "associateCompanyId":$scope.associateCompanyId,
									  "totalAmount":totalAmount+crNetAmount,
									  "dueAmount":dueAmount,
									  "crNetAmount":crNetAmount,
									  "unitId":$scope.unitId,
									  "orgId":$scope.orgId,
									  "createdBy":$scope.userId,
									  "updatedBy":"",
									  "invoiceDate":"",
									  "createdDate":"",
									  "updatedDate":"",
									  "invoiceCreationInsurance":$scope.unitConfigurationObject.invoiceCreationInsurance,
									  "invoiceCreationCorporate":$scope.unitConfigurationObject.invoiceCreationCorporate,
									
									}
							
							var listTCompanyInvoiceDetailsDto = [];
							angular.forEach($scope.companyInvoiceList,function(value,index){
								if (value.companyInvoiceSelected == true) 
								{
									var temp ={
											   "companyInvoiceDetailId":"",
											   "companyInvoiceId":"",
											   "billPayeeId":value.payeeId,
											   "billId":(value.pharamcy==false)?value.billId:'',
											   "tSaleId":(value.pharamcy==true)?value.billId:'',
											   "payableAmount":value.billAmount+value.crNetAmount,//amount from bill payee details
											   "dueAmount":value.billAmount,//amount from bill payee details
											   "billAmount":value.netAmount,//amount from bill master
											   "crNetAmount":value.crNetAmount,
											   "unitId":$scope.unitId,
											   "orgId":$scope.orgId,
											   "createdBy":$scope.userId,
											   "updatedBy":"",
											   "createdDate":"",
											   "updatedDate":"",
											   "billPayeeDetailsId":value.billPayeeDetailsId,
											   "associateCompanyId":value.associateCompanyId
											}
									listTCompanyInvoiceDetailsDto.push(temp);
								}
								
							});
						
							if(listTCompanyInvoiceDetailsDto.length == 0 ){
								growl.warning("Please select at least single bill...!!",{
									title : ERROR_MSG
								});
								return false;
							}else{
								$rootScope.startLoader();
								var param = {
								        mCompanyInvoiceMasterDto:$scope.mCompanyInvoiceMasterDto,
								        listTCompanyInvoiceDetailsDto:listTCompanyInvoiceDetailsDto,
										orgId:$scope.orgId,
										unitId:$scope.unitId,
										isConsolidatedBill : $scope.isConsolidatedBill 
								    }
								 console.log("param",JSON.stringify(param));
								
								var URI = BASE_URL + ROOT_URL + "/api/billing/saveCompanyInvoiceCreationList";
								BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
									if (response.data.status == "success"){
										
										
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
										            "paramValue": response.data.object
										          }
										        ]
										      };
							    	reportService.generate(REPORT_API_URI1, data);
										
										
										
										
										$scope.searchCompanyInvoice(1);
									}else if (response.data.status == "error"){
										/*growl.warning(response.data.message,{
											title : 'Warning'
										});*/
									}
									$rootScope.stopLoader();
								});
							}
							
						}
				    
					}

				$scope.init();
			
			
		
	}catch(e){
		console.log(e.message);
	}
}]);	