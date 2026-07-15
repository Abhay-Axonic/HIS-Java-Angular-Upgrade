/**/


angular.module('myApp')
		.controller("discountRightsMasterController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl','BillingGenericService',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService){
			$rootScope.loginpage = true;
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			$scope.popUpFlag = true;
			$scope.dropDownStatus = false;
	
			/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			$scope.commonListCount;
//			$scope.organisationId = 1;
//			$scope.unitId = 1;
//			$scope.createdBy =1 ;
//			$scope.updatedBy =1 ;
			
			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			
			$scope.unitId = cookieObject.unitId;
			$scope.organisationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
			$scope.isUHIDMergeApproval=cookieObject.isUHIDMergeApproval
			
  			$scope.usersList = [];
			
			$scope.initMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
						discountRightId:"",
						userId:"",
						fromDiscountAmount:0,	
						toDiscountAmount:0,
						employeeCode:"",	
						employeeName:"",
						isFinancialClearance : "N",
					    isCreditNoteApproval:"N",
					    isExpenseApproval:"N",
					    roleId:"",
					    isBadDebit:"N",
					    isDiscountRights : "N",
					    fromBadDebitAmount:0,
					    toBadDebitAmount:0,
					    fromFinancialClearanceAmount:0,
					    toFinancialClearanceAmount:0,
					    fromCreditNoteAmount:0,
					    toCreditNoteAmount:0,
					    fromExpenseApprovalAmount:0,
					    toExpenseApprovalAmount:0,
					    isUHIDMergeApproval:"N",
					    isRefundApproval:'N',
                        fromRefundAmount:0,
                        toRefundAmount:0
					  			
				};
				console.log("Common-init",$scope.common);
				
				$scope.discountRadio = 2;
				$scope.financialClearanceRadio = 2;
				$scope.creditNoteRadio = 2;
				$scope.expenseApprovalRadio = 2;
				$scope.badDebitRadio = 2;
				$scope.isRefundApprovalRadio = 2;
			}
			$scope.initMaster();
			$scope.getActiveList = function(){
				
				var URI = BASE_URL + ROOT_URL + "/api/global/getActiveUserRoleMasterList";
				var data = {
						organisationId:$scope.organisationId,
					      unitId:$scope.unitId
				}
				GenericService.serviceAction("POST",URI,data).then(function(response){
					if(response.data.status == "success"){
					$rootScope.stopLoader();					
					$scope.getActiveList = [];
					angular.forEach(response.data.listObject,
									function(value,index) {
										var data = {};
										data.id = value.roleId;
										data.label = value.roleName;
										$scope.getActiveList.push(data);
									});
					console.log("getActiveRoleList",$scope.getActiveList);
					}
				});
				}
				$scope.getActiveList();
			//Get Users List
			$scope.getUserList = function(){
		  		
		  		var URI = BASE_URL + ROOT_URL + GET_USERS_LIST+"/"+$scope.organisationId+"/"+$scope.unitId;
		  		GenericService.serviceAction("GET",URI).then(function(response){
		  			if(response.data.status == "success"){
				  			$rootScope.stopLoader();					
				  			$scope.usersListDetails = [];
				  			$scope.usersListDetails=response.data.listObject;

		  			}
		  		});
//		  		$scope.usrDetails();
		  		}
		  		$scope.getUserList();
		  		
		  		$scope.usrDetails=function(id){
		  			angular.forEach($scope.usersList,function(value,index){
							if(value.employeeId == id){
								$scope.common.employeeCode=value.employeeCode;
								$scope.common.employeeName = value.employeeName;
							}
		  			
		  						})
		  			
		  			}
		  	//Users List end
				$scope.usrDetails=function(roleId){
					
					var URI = BASE_URL + ROOT_URL + GET_USERS_LIST_BY_ROLEID+"/"+$scope.organisationId+"/"+$scope.unitId+"/"+roleId;
			  		GenericService.serviceAction("GET",URI).then(function(response){
			  			if(response.data.status == "success"){
					  			$rootScope.stopLoader();					
					  			$scope.usersList = [];
					  			$scope.usersList=response.data.listObject;

			  			console.log("usersList",$scope.getUserList);
			  			}
			  		});
				}
				$scope.usrDetailsByUserId=function(userId){
					 angular.forEach($scope.usersList, function(value , key) 
			                    {
						 			if(value.userId==userId){
						 				$scope.common.employeeCode=value.employeeCode;
						 			}
			                    });
				}
			/**Crud Operations Start**/
		
			/**List And Pagination Start**/
			$scope.setNoOfRecords = function() {
				$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
			};
			
			$scope.initMasterList = function(organisationId,offset, noOfRecordsPerPage,unitId)
			{
				$rootScope.startLoader();
				
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
				var data1 = {
					      	organisationId:organisationId,
							offset:offset,
							noOfRecordsPerPage:noOfRecordsPerPage,
							unitId:unitId
							};
			
				
				var URI1 = BASE_URL + ROOT_URL + GET_DISCOUNT_RIGHTS_MASTER_LIST; 
				var URI2 = BASE_URL + ROOT_URL + GET_DISCOUNT_RIGHTS_MASTER_COUNT;
					
				
				GenericService.serviceAction("POST",URI1,data1).then(function(response){
					if(response.data.status == "success"){
					$rootScope.stopLoader();					
					$scope.commonList = response.data.listObject;
					console.log("commonList",$scope.commonList);
					}
				});
				
				GenericService.serviceAction("GET",URI2,"").then(function(response){
					if(response.data.status == "success"){					
						$rootScope.stopLoader();
						$scope.commonListCount = response.data.object;
						$scope.setPage(1,false);
						console.log("commonListCount",response.data.object);
					}
				});
				
			}		
			
			$scope.masterList = function(organisationId,offset, noOfRecordsPerPage,unitId) {
				$rootScope.startLoader();
				console.log("offset", offset);
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
				
				var data = {
						organisationId:organisationId,
						offset:offset,
						noOfRecordsPerPage:noOfRecordsPerPage,
						unitId:unitId
						};
				
				var URI1 = BASE_URL + ROOT_URL + GET_DISCOUNT_RIGHTS_MASTER_LIST; 
				console.log("URI", URI);
				GenericService.serviceAction("POST", URI,data).then(function(response) {
					$rootScope.stopLoader();
					$scope.commonList = response.data.listObject;
					console.log("commonList",$scope.commonList);
				});
			}

			$scope.pager = {};
			$scope.page;

			$scope.setPage = function(page, flag) {
				if (page < 1 || page > $scope.pager.totalPages)
				{
					return;
				}
				$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage,$scope.unitId);
				if (flag)
				{
					$scope.masterList($scope.organisationId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.unitId);
				}
			}

			$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
			
			/**List And Pagination End**/
			
			// function for popup ok button call
			$scope.PopupOkBtn = function() {
				$scope.popUpFlag = false;
				$scope.save();
			}
			
			$scope.save = function(){
				try{
					 $scope.$broadcast('show-errors-check-validity');
						if ($scope.discountRightForm.$valid) {
							
							if($scope.common.roleId==undefined || $scope.common.roleId==null)
							{
								growl.warning("Please select user",{
									title : 'Warning !'
									});
									return false;
							}
						if($scope.popUpFlag==false)
							{
							  $rootScope.startLoader();
							}
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');

					var data={	
							
							userId:$scope.common.userId,
							fromDiscountAmount:$scope.common.fromDiscountAmount,
							toDiscountAmount:$scope.common.toDiscountAmount,
							organisationId:$scope.organisationId,
							unitId:$scope.unitId,
							createdBy:$scope.createdBy,
							updatedBy:$scope.updatedBy,
							status:'A',
							createdDate:$scope.dt,
							updatedDate:$scope.dt,
							isFinancialClearance : $scope.common.isFinancialClearance,
							isCreditNoteApproval:$scope.common.isCreditNoteApproval,
							isExpenseApproval:$scope.common.isExpenseApproval,
							isBadDebit:$scope.common.isBadDebit,
							isDiscountRights : $scope.common.isDiscountRights,
							roleId:$scope.common.roleId,
							isUHIDMergeApproval:$scope.common.isUHIDMergeApproval,
							isRefundApproval:$scope.common.isRefundApproval,
							fromRefundAmount:$scope.common.fromRefundAmount,
							toRefundAmount:$scope.common.toRefundAmount
						}
					
					if($scope.common.isDiscountRights == 'Y'){
						
						if($scope.discountRadio == 1){
							if($scope.common.fromDiscountAmount > 100){
								growl.warning("From discount percentage should be less then or equal yo 100.",
										{
											title : 'Warning!'
										});
								$scope.common.fromDiscountAmount = 0;
								$rootScope.stopLoader();
								return false;
							}else if($scope.common.toDiscountAmount > 100){
								growl.warning("To discount percentage should be less then or equal yo 100.",
										{
											title : 'Warning!'
										});
								$scope.common.toDiscountAmount = 0;
								$rootScope.stopLoader();
								return false;
							}else{
								data.discountType = $scope.discountRadio;
								data.fromDiscountAmount = $scope.common.fromDiscountAmount;
								data.toDiscountAmount = $scope.common.toDiscountAmount;
							}
						}else{
							data.discountType = $scope.discountRadio;
							data.fromDiscountAmount = $scope.common.fromDiscountAmount;
							data.toDiscountAmount = $scope.common.toDiscountAmount;
						}
						
					}
					
						if($scope.common.isBadDebit == 'Y'){
							
							if($scope.badDebitRadio == 1){
								if($scope.common.fromBadDebitAmount > 100){
									growl.warning("From bad Debit percentage should be less then or equal yo 100.",
											{
												title : 'Warning!'
											});
									$scope.common.fromBadDebitAmount= 0;
									$rootScope.stopLoader();
									return false;
								}else if($scope.common.toBadDebitAmount > 100){
									growl.warning("To bad Debit percentage should be less then or equal yo 100.",
											{
												title : 'Warning!'
											});
									$scope.common.toBadDebitAmount = 0;
									$rootScope.stopLoader();
									return false;
								}else{
									data.badDebitType = $scope.badDebitRadio;
									data.fromBadDebitAmount = $scope.common.fromBadDebitAmount;
									data.toBadDebitAmount = $scope.common.toBadDebitAmount;
								}
							}else{
								data.badDebitType = $scope.badDebitRadio;
								data.fromBadDebitAmount = $scope.common.fromBadDebitAmount;
								data.toBadDebitAmount = $scope.common.toBadDebitAmount;
							}
							
							
							
						}
					
					if($scope.common.isFinancialClearance == 'Y'){
						
						
						if($scope.financialClearanceRadio == 1){
							if($scope.common.fromFinancialClearanceAmount > 100){
								growl.warning("From financial clearance percentage should be less then or equal yo 100.",
										{
											title : 'Warning!'
										});
								$scope.common.fromFinancialClearanceAmount = 0;
								$rootScope.stopLoader();
								return false;
							}else if($scope.common.toFinancialClearanceAmount > 100){
								growl.warning("To financial clearance percentage should be less then or equal yo 100.",
										{
											title : 'Warning!'
										});
								$scope.common.toFinancialClearanceAmount = 0;
								$rootScope.stopLoader();
								return false;
							}else{
								data.financialClearanceType = $scope.financialClearanceRadio;
								data.fromFinancialClearanceAmount = $scope.common.fromFinancialClearanceAmount;
								data.toFinancialClearanceAmount = $scope.common.toFinancialClearanceAmount;
							}
						}else{
							data.financialClearanceType = $scope.financialClearanceRadio;
							data.fromFinancialClearanceAmount = $scope.common.fromFinancialClearanceAmount;
							data.toFinancialClearanceAmount = $scope.common.toFinancialClearanceAmount;
						}
					}
					
					if($scope.common.isCreditNoteApproval == 'Y'){
						
						
						
						if($scope.creditNoteRadio == 1){
							if($scope.common.fromCreditNoteAmount > 100){
								growl.warning("From credit note percentage should be less then or equal yo 100.",
										{
											title : 'Warning!'
										});
								$scope.common.fromCreditNoteAmount = 0;
								$rootScope.stopLoader();
								return false;
							}else if($scope.common.toCreditNoteAmount > 100){
								growl.warning("To credit note  percentage should be less then or equal yo 100.",
										{
											title : 'Warning!'
										});
								$scope.common.toCreditNoteAmount = 0;
								$rootScope.stopLoader();
								return false;
							}else{
								data.creditNoteType = $scope.creditNoteRadio;
								data.fromCreditNoteAmount = $scope.common.fromCreditNoteAmount;
								data.toCreditNoteAmount = $scope.common.toCreditNoteAmount;
							}
						}else{
							data.creditNoteType = $scope.creditNoteRadio;
							data.fromCreditNoteAmount = $scope.common.fromCreditNoteAmount;
							data.toCreditNoteAmount = $scope.common.toCreditNoteAmount;
						}
						
					}
					
					if($scope.common.isExpenseApproval == 'Y'){
						
						
						if($scope.expenseApprovalRadio == 1){
							if($scope.common.fromExpenseApprovalAmount > 100){
								growl.warning("From expense approval percentage should be less then or equal yo 100.",
										{
											title : 'Warning!'
										});
								$scope.common.fromExpenseApprovalAmount = 0;
								$rootScope.stopLoader();
								return false;
							}else if($scope.common.toExpenseApprovalAmount > 100){
								growl.warning("To expense approval  percentage should be less then or equal yo 100.",
										{
											title : 'Warning!'
										});
								$scope.common.toExpenseApprovalAmount = 0;
								$rootScope.stopLoader();
								return false;
							}else{
								data.expenseApprovalType = $scope.expenseApprovalRadio; 
								data.fromExpenseApprovalAmount = $scope.common.fromExpenseApprovalAmount;
								data.toExpenseApprovalAmount = $scope.common.toExpenseApprovalAmount;
							}
						}else{
							data.expenseApprovalType = $scope.expenseApprovalRadio; 
							data.fromExpenseApprovalAmount = $scope.common.fromExpenseApprovalAmount;
							data.toExpenseApprovalAmount = $scope.common.toExpenseApprovalAmount;
						}
					}
					if($scope.common.isRefundApproval == 'Y'){


                    						if($scope.isRefundApprovalRadio == 1){
                    							if($scope.common.fromRefundAmount > 100){
                    								growl.warning("From refund approval percentage should be less than or equal yo 100.",
                    										{
                    											title : 'Warning!'
                    										});
                    								$scope.common.fromRefundAmount = 0;
                    								$rootScope.stopLoader();
                    								return false;
                    							}else if($scope.common.toRefundAmount > 100){
                    								growl.warning("To refund approval  percentage should be less than or equal yo 100.",
                    										{
                    											title : 'Warning!'
                    										});
                    								$scope.common.toRefundAmount = 0;
                    								$rootScope.stopLoader();
                    								return false;
                    							}else{
                    								data.refundType = $scope.isRefundApprovalRadio;
                    								data.fromRefundAmount = $scope.common.fromRefundAmount;
                    								data.toRefundAmount = $scope.common.toRefundAmount;
                    							}
                    						}else{
                    							data.refundType = $scope.isRefundApprovalRadio;
                    							data.fromRefundAmount = $scope.common.fromRefundAmount;
                    							data.toRefundAmount = $scope.common.toRefundAmount;
                    						}
                    					}
					
					console.log("data",data);
					//return false;
					
					if ($scope.popUpFlag) {
						$scope.showModal = !$scope.showModal;

					} else {
					var URI = BASE_URL + ROOT_URL +SAVE_DISCOUNT_RIGHTS_MASTER;
					console.log("Save-URI-",URI);
					console.log(" save OBJ",data); 
					//return false;
					GenericService.serviceAction("POST",URI,data).then(function(response){
						
						if (response.data.status == 'success')
						{
							growl.success("Added Sucessfully..",
									{
										title : 'Success!'
									});
							
//							$scope.$broadcast('show-errors-reset');
							$scope.initMaster();
							$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
						} else if(response.data.status == 'error')
						{
							if(response.data.code == 409){
								growl.warning("Already Exist.",
										{
											title : 'Warning!'
										});
							}else{
								growl.error("Something went wrong.",
										{
											title : 'Error!'
										});
							}
							
						}
					});
					$rootScope.stopLoader();
				 }
				}	
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			
			$scope.showDetailsBtn=function(data){
				$scope.configDetails=data;
			}
			
			$scope.showUpdateBtn = function(id){
				try{
					$rootScope.startLoader();
					$scope.dropDownStatus=true;
					$scope.saveBtnFlag = false;
					$scope.updateScope = true;
					$scope.updateBtnFlag = true;
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL +GET_DISCOUNT_RIGHTS_MASTER_BY_ID +S+ id +S+ $scope.organisationId +S+ $scope.unitId;
					console.log("EDIT",URI);
					GenericService.serviceAction("GET",URI).then(function(response){
						console.log("Edit",response);
						
						if(response.data.status == "success"){
							$rootScope.stopLoader();
							$scope.common = response.data.listObject[0];
							
							$scope.common.fromDiscountAmountOLD = response.data.listObject[0].fromDiscountAmount;
							$scope.common.toDiscountAmountOLD = response.data.listObject[0].toDiscountAmount;
							
							$scope.common.fromFinancialClearanceAmountOLD = response.data.listObject[0].fromFinancialClearanceAmount;
							$scope.common.toFinancialClearanceAmountOLD = response.data.listObject[0].toFinancialClearanceAmount;
							
							$scope.common.fromCreditNoteAmountOLD = response.data.listObject[0].fromCreditNoteAmount;
							$scope.common.toCreditNoteAmountOLD = response.data.listObject[0].toCreditNoteAmount;
							
							$scope.common.fromExpenseApprovalAmountOLD = response.data.listObject[0].fromExpenseApprovalAmount;
							$scope.common.toExpenseApprovalAmountOLD = response.data.listObject[0].toExpenseApprovalAmount;
							
							
							$scope.common.fromBadDebitAmountOLD = response.data.listObject[0].fromBadDebitAmount;
							$scope.common.toBadDebitAmountOLD = response.data.listObject[0].toBadDebitAmount;
							
							$scope.common.discountRightId=response.data.listObject[0].discountRightId;
							$scope.common.userId=response.data.listObject[0].userId.toString();
							
							$scope.discountRadio = response.data.listObject[0].discountType;
							$scope.financialClearanceRadio = response.data.listObject[0].financialClearanceType;
							$scope.creditNoteRadio =response.data.listObject[0].creditNoteType;
							$scope.expenseApprovalRadio = response.data.listObject[0].expenseApprovalType;
							$scope.badDebitRadio = response.data.listObject[0].badDebitType;
							
							
							$scope.discountRadioOLD = response.data.listObject[0].discountType;
							$scope.financialClearanceRadioOLD = response.data.listObject[0].financialClearanceType;
							$scope.creditNoteRadioOLD =response.data.listObject[0].creditNoteType;
							$scope.expenseApprovalRadioOLD = response.data.listObject[0].expenseApprovalType;
							$scope.badDebitRadioOLD = response.data.listObject[0].badDebitType;


							$scope.isRefundApprovalRadio = response.data.listObject[0].refundType;
							$scope.common.fromRefundAmount = response.data.listObject[0].fromRefundAmount;
                            $scope.common.toRefundAmount = response.data.listObject[0].toRefundAmount;
							
						}else if(response.data.status == 'error'){
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
					
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			
			$scope.update = function(){
				try{
				
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');	
					
					$rootScope.startLoader();
					

					var data={	
								discountRightId:$scope.common.discountRightId,
								userId:$scope.common.userId,
								fromDiscountAmount:$scope.common.fromDiscountAmount,
								toDiscountAmount:$scope.common.toDiscountAmount,
								organisationId:$scope.organisationId,
								unitId:$scope.unitId,
								updatedBy:$scope.updatedBy,
								updatedDate:$scope.dt,
								isFinancialClearance:$scope.common.isFinancialClearance,
								isCreditNoteApproval:$scope.common.isCreditNoteApproval,
								isExpenseApproval:$scope.common.isExpenseApproval,
								isBadDebit:$scope.common.isBadDebit,
								isDiscountRights:$scope.common.isDiscountRights,
								roleId:$scope.common.roleId,
								isUHIDMergeApproval:$scope.common.isUHIDMergeApproval,
								isRefundApproval:$scope.common.isRefundApproval
					}
				
					if($scope.common.isDiscountRights == 'Y'){
						
						if($scope.discountRadio == 1){
							if($scope.common.fromDiscountAmount > 100){
								growl.warning("From discount percentage should be less then or equal to 100.",
										{
											title : 'Warning!'
										});
								
								$scope.common.fromDiscountAmount = $scope.common.fromDiscountAmountOLD;

								$rootScope.stopLoader();
								return false;
							}else if($scope.common.toDiscountAmount > 100){
								growl.warning("To discount percentage should be less then or equal to 100.",
										{
											title : 'Warning!'
										});
								$scope.common.toDiscountAmount = $scope.common.toDiscountAmountOLD;
								$rootScope.stopLoader();
								return false;
							}else{
								data.discountType = $scope.discountRadio;
								data.fromDiscountAmount = $scope.common.fromDiscountAmount;
								data.toDiscountAmount = $scope.common.toDiscountAmount;
							}
						}else{
							data.discountType = $scope.discountRadio;
							data.fromDiscountAmount = $scope.common.fromDiscountAmount;
							data.toDiscountAmount = $scope.common.toDiscountAmount;
						}
						
					}else{
						data.fromDiscountAmount = 0;
						data.toDiscountAmount = 0;
					}
					
						if($scope.common.isBadDebit == 'Y'){
							
							if($scope.badDebitRadio == 1){
								if($scope.common.fromBadDebitAmount > 100){
									growl.warning("From bad Debit percentage should be less then or equal to 100.",
											{
												title : 'Warning!'
											});
									
									$scope.common.fromBadDebitAmount= $scope.common.fromBadDebitAmountOLD;
									$rootScope.stopLoader();
									return false;
								}else if($scope.common.toBadDebitAmount > 100){
									growl.warning("To bad Debit percentage should be less then or equal to 100.",
											{
												title : 'Warning!'
											});
									$scope.common.toBadDebitAmount = $scope.common.toBadDebitAmountOLD;
									$rootScope.stopLoader();
									return false;
								}else{
									data.badDebitType = $scope.badDebitRadio;
									data.fromBadDebitAmount = $scope.common.fromBadDebitAmount;
									data.toBadDebitAmount = $scope.common.toBadDebitAmount;
								}
							}else{
								data.badDebitType = $scope.badDebitRadio;
								data.fromBadDebitAmount = $scope.common.fromBadDebitAmount;
								data.toBadDebitAmount = $scope.common.toBadDebitAmount;
							}
							
							
							
						}else{
							data.fromBadDebitAmount = 0;
							data.toBadDebitAmount = 0;
						}
					
					if($scope.common.isFinancialClearance == 'Y'){
						
						
						if($scope.financialClearanceRadio == 1){
							if($scope.common.fromFinancialClearanceAmount > 100){
								growl.warning("From financial clearance percentage should be less then or equal to 100.",
										{
											title : 'Warning!'
										});
								
								$scope.common.fromFinancialClearanceAmount =$scope.common.fromFinancialClearanceAmountOLD;
								$rootScope.stopLoader();
								return false;
							}else if($scope.common.toFinancialClearanceAmount > 100){
								growl.warning("Financial clearance to percentage should be less than or equal to 100.",
										{
											title : 'Warning!'
										});
								$scope.common.toFinancialClearanceAmount = $scope.common.toFinancialClearanceAmountOLD;
								$rootScope.stopLoader();
								return false;
							}else{
								data.financialClearanceType = $scope.financialClearanceRadio;
								data.fromFinancialClearanceAmount = $scope.common.fromFinancialClearanceAmount;
								data.toFinancialClearanceAmount = $scope.common.toFinancialClearanceAmount;
							}
						}else{
							data.financialClearanceType = $scope.financialClearanceRadio;
							data.fromFinancialClearanceAmount = $scope.common.fromFinancialClearanceAmount;
							data.toFinancialClearanceAmount = $scope.common.toFinancialClearanceAmount;
						}
					}else{
						data.fromFinancialClearanceAmount = 0;
						data.toFinancialClearanceAmount = 0;
					}
					
					if($scope.common.isCreditNoteApproval == 'Y'){
						
						
						
						if($scope.creditNoteRadio == 1){
//							if($scope.common.fromCreditNoteAmount > 100){
//								growl.warning("From credit note percentage should be less then or equal yo 100.",
//										{
//											title : 'Warning!'
//										});
//								$scope.common.fromCreditNoteAmount = $scope.common.fromCreditNoteAmountOLD;
//								$rootScope.stopLoader();
//								return false;
//							}else if($scope.common.toCreditNoteAmount > 100){
//								growl.warning("Credit Note To percentage should be less than or equal to 100.",
//										{
//											title : 'Warning!'
//										});
//								$scope.common.toCreditNoteAmount = $scope.common.toCreditNoteAmountOLD;
//								$rootScope.stopLoader();
//								return false;
//							}else{
//								data.creditNoteType = $scope.creditNoteRadio;
//								data.fromCreditNoteAmount = $scope.common.fromCreditNoteAmount;
//								data.toCreditNoteAmount = $scope.common.toCreditNoteAmount;
//							}
						}else{
							data.creditNoteType = $scope.creditNoteRadio;
							data.fromCreditNoteAmount = $scope.common.fromCreditNoteAmount;
							data.toCreditNoteAmount = $scope.common.toCreditNoteAmount;
						}
						
					}else{
						data.fromCreditNoteAmount = 0;
						data.toCreditNoteAmount = 0;
					}
					
					if($scope.common.isExpenseApproval == 'Y'){
						
						
						if($scope.expenseApprovalRadio == 1){
							if($scope.common.fromExpenseApprovalAmount > 100){
								growl.warning("From expense approval percentage should be less then or equal to 100.",
										{
											title : 'Warning!'
										});
								
								$scope.common.fromExpenseApprovalAmount = $scope.common.fromExpenseApprovalAmountOLD;
								$rootScope.stopLoader();
								return false;
							}else if($scope.common.toExpenseApprovalAmount > 100){
								growl.warning("To expense approval  percentage should be less then or equal to 100.",
										{
											title : 'Warning!'
										});
								$scope.common.toExpenseApprovalAmount = $scope.common.toExpenseApprovalAmountOLD;
								$rootScope.stopLoader();
								return false;
							}else{
								data.expenseApprovalType = $scope.expenseApprovalRadio; 
								data.fromExpenseApprovalAmount = $scope.common.fromExpenseApprovalAmount;
								data.toExpenseApprovalAmount = $scope.common.toExpenseApprovalAmount;
							}
						}else{
							data.expenseApprovalType = $scope.expenseApprovalRadio; 
							data.fromExpenseApprovalAmount = $scope.common.fromExpenseApprovalAmount;
							data.toExpenseApprovalAmount = $scope.common.toExpenseApprovalAmount;
						}
					}else{
						data.fromExpenseApprovalAmount = 0;
						data.toExpenseApprovalAmount =0;
					}
					if($scope.common.isRefundApproval == 'Y'){


                                        						if($scope.isRefundApprovalRadio == 1){
                                        							if($scope.common.fromRefundAmount > 100){
                                        								growl.warning("From refund approval percentage should be less than or equal to 100.",
                                        										{
                                        											title : 'Warning!'
                                        										});
                                        								$scope.common.fromRefundAmount = 0;
                                        								$rootScope.stopLoader();
                                        								return false;
                                        							}else if($scope.common.toRefundAmount > 100){
                                        								growl.warning("To refund approval  percentage should be less than or equal to 100.",
                                        										{
                                        											title : 'Warning!'
                                        										});
                                        								$scope.common.toRefundAmount = 0;
                                        								$rootScope.stopLoader();
                                        								return false;
                                        							}else{
                                        								data.refundType = $scope.isRefundApprovalRadio;
                                        								data.fromRefundAmount = $scope.common.fromRefundAmount;
                                        								data.toRefundAmount = $scope.common.toRefundAmount;
                                        							}
                                        						}else{
                                        							data.refundType = $scope.isRefundApprovalRadio;
                                        							data.fromRefundAmount = $scope.common.fromRefundAmount;
                                        							data.toRefundAmount = $scope.common.toRefundAmount;
                                        						}
                                        					}else{
                                        						data.fromRefundAmount = 0;
                                    							data.toRefundAmount =0;
                                        					}
					/*if($scope.common.isBadDebit == 'Y'){
						data.fromBadDebitAmount = $scope.common.fromBadDebitAmount;
						data.toBadDebitAmount = $scope.common.toBadDebitAmount;
					}
					
					if($scope.common.isFinancialClearance == 'Y'){
						
						data.fromFinancialClearanceAmount = $scope.common.fromFinancialClearanceAmount;
						data.toFinancialClearanceAmount = $scope.common.toFinancialClearanceAmount;
					}
					
					if($scope.common.isCreditNoteApproval == 'Y'){
						
						data.fromCreditNoteAmount = $scope.common.fromCreditNoteAmount;
						data.toCreditNoteAmount = $scope.common.toCreditNoteAmount;
					}
					
					if($scope.common.isExpenseApproval == 'Y'){
						
						data.fromExpenseApprovalAmount = $scope.common.fromExpenseApprovalAmount;
						data.toExpenseApprovalAmount = $scope.common.toExpenseApprovalAmount;
					}
					*/
								
					var URI = BASE_URL + ROOT_URL + UPDATE_DISCOUNT_RIGHTS_MASTER;
					console.log("Update-URI-",URI);
					console.log("Update OBJ",data);
					$rootScope.startLoader(); 
					GenericService.serviceAction("POST",URI,data).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success("Updated Sucessfully..",
									{
										title : 'Success!'
									});
							$scope.dropDownStatus=false;
//							$scope.$broadcast('show-errors-reset');
							$scope.initMaster();
							$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error("Something went wrong.",
									{
										title : 'Error!'
									});
						}
					});
					
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			$scope.changesRadio = function(type){
				if(type == 1){
					 $scope.common.fromDiscountAmount = 0;
					 $scope.common.toDiscountAmount = 0;
				}
				if(type ==2){
					 $scope.common.fromFinancialClearanceAmount = 0;
					 $scope.common.toFinancialClearanceAmount = 0;
				}
				if(type ==3){
					 $scope.common.fromCreditNoteAmount = 0
					 $scope.common.toCreditNoteAmount = 0;
				}
				if(type ==4){
					 $scope.common.fromExpenseApprovalAmount = 0;
					 $scope.common.toExpenseApprovalAmount = 0;
				}
				if(type ==5){
					 $scope.common.fromBadDebitAmount = 0;
					 $scope.common.toBadDebitAmount = 0;
				}
				if(type ==6){
					 $scope.common.isUHIDMergeApproval = 0;
				}
				if(type ==7){
                	 $scope.common.fromRefundAmount = 0;
                     $scope.common.toRefundAmount = 0;
                }
			}
			
			$scope.updateStatus = function(id,status){
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					$scope.common.updatedBy=$scope.updatedBy,
					$scope.common.updatedDate=$scope.dt,
					$scope.common.status=status,
					$scope.common.discountRightId=id,
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + UPDATE_DISCOUNT_RIGHTS_MASTER_STATUS;
					console.log("update status",$scope.common);
					//return false;
					GenericService.serviceAction("POST",URI,$scope.common).then(function(response){
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
//							$scope.$broadcast('show-errors-reset');
							$scope.initMaster();
							$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
				}catch(e){
					console.log("Exception",e);
				}
			}
			

			/**Crud Operations End**/
			//check valid amount
		/*	$scope.getAmountDetails=function(amt,from){
				if(amt==null || amt==undefined){
					if(from==1){
						$scope.common.fromDiscountAmount=0;
					}else{
						$scope.common.toDiscountAmount=0;
					}
					
				}
			}*/
}]);

