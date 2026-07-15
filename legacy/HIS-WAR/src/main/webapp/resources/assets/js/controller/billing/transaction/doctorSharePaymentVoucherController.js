angular.module("myApp").controller("doctorSharePaymentVoucherController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','PaginationService',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,growl,PaginationService){
	try{
		$rootScope.loginpage = true;
		
		$scope.init = function(){
		}
		var cookieObject = $cookies.getObject('cookieObject');
		if (cookieObject == undefined) {
			$state.go('login');
			return;
		}
		
		
		var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
		$scope.isPinPadRequired = cookieObjectAppConfig.isPinPadRequired;
		if(cookieObjectAppConfig == undefined){
			$state.go('applicationConfiguration');
			return;
		}
		$scope.doctorSlabTDS = cookieObjectAppConfig.doctorSlipTDS;
		//alert($scope.doctorSlabTDS);
		$scope.unitId = cookieObject.unitId;
		$scope.orgId = cookieObject.organizationId;
		$scope.userId = cookieObject.userId;
		//$scope.init();
		
		
		/**DatePicker Code  */
		
		$scope.fromDate = moment().startOf('day').toDate();
	    $scope.toDate = moment().startOf('day').toDate();
	    $scope.completed='N';
		$scope.pending = 'N';
		$scope.outsourced = 'N';
		$scope.remark = "";
		$scope.dateOptions =
		{
			formatYear : 'yyyy',
			showWeeks : false,
			maxDate : new Date(),
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
		$scope.tDocSharePaymentVoucherLoggerDto={
				"voucherLoggerId":  "",
				"processedShareAmt":"",
				"voucherNo":"",
				"orgUnitId": $scope.unitId,
				"orgId": $scope.orgId,
				"status": "A",
				"createdDate": " ",
				"createdBy": $scope.userId,
				"processedShareAmt": " ",
				"retainerShipAmt": " ",
			    "paidAmt":""
		  }
		
		
		  $scope.init = function(){
			  $scope.initDoctorShareProcessObj();
			  $scope.doctorSharePaymentVoucherList = [];
			  $scope.getSpecialityList();
			  $scope.firstTabLevel = 1 ;
			  //$scope.getdoctorSharePaymentVoucherList();
		  }
		  $scope.initDoctorShareProcessObj = function(){
			   $scope.doctorShareProcessObj =$scope.doctorShareProcessObj = {orgId:$scope.orgId,"visitTypeId":"","fromDate":"","toDate":""};
			  //,unitId:$scope.unitId
		  }
		
		  
		  $scope.getdoctorSharePaymentVoucherList = function() {
			  
			/*  $scope.fromDate==undefined?"":moment($scope.fromDate.getTime()).format('YYYY-MM-DD');
			  $scope.toDate==undefined?"":moment($scope.toDate.getTime()).format('YYYY-MM-DD');
			  */
			  $scope.setFirstTabLevel(1);
			  var fromDate=undefined?"":$rootScope.getFormattedDate($scope.fromDate.getTime(),false);
			  var toDate=undefined?"":$rootScope.getFormattedDate($scope.toDate.getTime(),false);
			
			  if ($scope.specialityId == '' || typeof($scope.specialityId) == "undefined" ){
					growl.warning("Please select speciality..!!",{
						title : WARNING_MSG
					});
					
					return;
				}
			  if ($scope.doctorId == '' || typeof($scope.doctorId) == "undefined" ){
					growl.warning("Please select doctor..!!",{
						title : WARNING_MSG
					});
					
					return;
				}
			
			    var param = {"orgId":$scope.orgId,"orgUnitId":$scope.unitId,"fromDate":fromDate,"toDate":toDate,"doctorId": $scope.doctorId}; //,unitId:$scope.unitId
				$rootScope.startLoader();	
			    var URI = BASE_URL + ROOT_URL + "/api/billing/getDoctorSharePaymentVoucherList"
			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						 $scope.doctorSharePaymentVoucherListpaginatiom = response.data.listObject;
						 $scope.pagination.data=[];
						 if($scope.doctorSharePaymentVoucherListpaginatiom.length > 0){            
	                         PaginationService.searchData($scope.doctorSharePaymentVoucherListpaginatiom, $scope.pagination);
	                       }
						 $scope.selectedAll = false;
						 $scope.doctorCreditDetails = [];
						 $scope.doctorSharePaymentVoucherListFinal = [];
						 $scope.getDoctorCrDetailsTotal();
						 //console.log("$scope.doctorSharePaymentVoucherList",JSON.stringify($scope.doctorSharePaymentVoucherList));
					}
					$rootScope.stopLoader();
				});
			  }
		  
		  
		  /*========================= Serach Tab Funcationality Start==================*/
		  
		  

		  $scope.getSpecialityList = function() {
				
			  var param = {organizationId:$scope.orgId,unitId:$scope.unitId}; 
			   $rootScope.startLoader();	
				var URI = BASE_URL + ROOT_URL
				+ GETACTIVESPECIALITY;
			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						$scope.specialityList = response.data.listObject;
					}
					$rootScope.stopLoader();
				});
	     }
		  
		  $scope.init();
		  
		  
		$scope.getDoctorBySpeciality = function(id) {
			if(id > 0){
				var data = {
					specialityId : id,
					unitId : $scope.unitId,
					organizationId : $scope.orgId
				};
				$rootScope.startLoader();	
				var URI = BASE_URL + ROOT_URL
						+ GETDOCTORBYSPECIALITYID;
				
				BillingGenericService
						.serviceAction(METHOD_POST, URI, data,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									//console.log(response);
									$scope.doctorList = [];
									// $rootScope.startLoader();
									if (response.data.status == "success")
										$scope.doctorList = response.data.listObject;
									
									$rootScope.stopLoader();
								});
		}

			}
		
		 $scope.getdoctorVoucherSlabDetails = function(dochShareDtls) 
		 {
	        	
      			$rootScope.startLoader();
      			$scope.getdoctorVoucherSlabDetailsList = [];
        		var URI = BASE_URL + ROOT_URL + "/api/billing/doctorSlab/voucherSlabConsumptionDetails/"+dochShareDtls.docShareVoucherId;
			    BillingGenericService.serviceAction(METHOD_GET,URI,"",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						 $scope.getdoctorVoucherSlabDetailsList = response.data.listObject;
					}
					$rootScope.stopLoader();
				});
		 }
		
		  
         $scope.getdoctorSharePaymentVoucherDtls = function(dochShareDtls) {
        	
        	 $scope.type = dochShareDtls.typeId;
        	 
        	 if(dochShareDtls.typeId==2){
        		    var param = {orgId:$scope.orgId,orgUnitId:$scope.unitId,referralShareId:dochShareDtls.referralShareId};//
        			$rootScope.startLoader();	
        		  var URI = BASE_URL + ROOT_URL + "/api/billing/getReferralShareVoucherDtlsByReferralShareId"
      		 }
        	 else if(dochShareDtls.typeId==3){
      			 $rootScope.startLoader();
        		  var param = {orgId:$scope.orgId,orgUnitId:$scope.unitId,docShareVoucherId:dochShareDtls.docShareVoucherId};
        		  var URI = BASE_URL + ROOT_URL + "/api/billing/doctorSlab/voucherDetails"
      		 }
        	 else{
      			 $rootScope.startLoader();
        		  var param = {orgId:$scope.orgId,orgUnitId:$scope.unitId,docShareVoucherId:dochShareDtls.docShareVoucherId};
        		  var URI = BASE_URL + ROOT_URL + "/api/billing/getDoctorSharePaymentVoucherDtlsByDoctorShareId"
      		 }
			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						 $scope.doctorSharePaymentVoucherDtls = response.data.listObject;
						// $scope.initCalculateAmounts();
						 //console.log("$scope.doctorSharePaymentVoucherDtls",JSON.stringify($scope.doctorSharePaymentVoucherDtls));
					}
					$rootScope.stopLoader();
				});
    		 }
		  
		  $scope.getCalculatepaidAmt = function(item,idx)
		  {
			  //$scope.doctorSharePaymentVoucherList
			  if(item.doctorCredit > 50000 )
				  {
				  		//warning
				  }
			  else
				  {
				  $scope.doctorSharePaymentVoucherList[idx].paidAmount = parseFloat(item.paidAmount) - parseFloat(item.doctorCredit);
				  }
		  }
		  
		  $scope.checkPaidAmountCalculates = function(docShare){
				/*var retainershipFee=0;*/
				var paidAmount=0;
			    var totalAmount = 0;
			    var processedShareAmt = 0;
			    var totalDueShareAmt = 0;
			    //var totalDoctorCredit = 0;
			    var i = 0;
				angular.forEach($scope.doctorSharePaymentVoucherList,function(value,index){
					if(value.doctorShare == true){
						
							if($scope.totalDoctorCredit >= value.paidAmount)
								{
								  $scope.totalDoctorCredit = parseFloat($scope.totalDoctorCredit) - (isNaN(value.paidAmount) ? 0 : parseFloat(value.paidAmount));
								  $scope.doctorSharePaymentVoucherList[i].docCreditTemp = value.paidAmount;
								  var paidAmtTemp = value.paidAmount;
								  angular.forEach($scope.doctorCreditDetails,function(value1,index){
									 // value1.amountPaid = (isNaN(value1.amountPaid) ? 0 : parseFloat(value1.amountPaid));
									  if(value1.amountPaid != null && value1.amountPaid > 0 )
										  {
										  value1.amountPaid = value1.amountPaid;
										  }else
											  {
											  value1.amountPaid = 0;
											  }
									 if(paidAmtTemp >= value1.availableCredit)
										 {
										 	
										 	paidAmtTemp = paidAmtTemp - value1.availableCredit;
										 	value1.consumedCredit = value1.consumedCredit + value1.availableCredit;
										 	value1.consumedCreditOld = value1.consumedCredit;
										 	value1.amountPaid = value1.amountPaid + value1.availableCredit;
										 	value1.amountPaidOld =value1.amountPaid;
										 	value1.availableCredit = parseFloat(0);
										 }
									 else{
										    value1.availableCredit =value1.availableCredit - paidAmtTemp;
										    value1.consumedCredit = value1.consumedCredit + paidAmtTemp;
										    value1.consumedCreditOld = value1.consumedCredit;
										    value1.amountPaid =  value1.amountPaid +paidAmtTemp;
										    value1.amountPaidOld =value1.amountPaid;
										 	paidAmtTemp = paidAmtTemp - paidAmtTemp;
									 }
								  });
								  value.paidAmount = paidAmtTemp;
								  
								  
								}else{
									$scope.doctorSharePaymentVoucherList[i].docCreditTemp = $scope.totalDoctorCredit;
									$scope.totalDoctorCredit = parseFloat(0);
									 var paidAmtTemp = value.paidAmount;
									 angular.forEach($scope.doctorCreditDetails,function(value1,index){
										 if(value1.amountPaid != null && value1.amountPaid > 0 )
										  {
										  value1.amountPaid = value1.amountPaid;
										  }else
											  {
											  value1.amountPaid = 0;
											  }
												 if(paidAmtTemp >= value1.availableCredit)
												 {
												 	
												 	paidAmtTemp = paidAmtTemp - value1.availableCredit;
												 	value1.consumedCredit = value1.consumedCredit + value1.availableCredit;
												 	value1.consumedCreditOld = value1.consumedCredit;
													value1.amountPaid = value1.amountPaid + value1.availableCredit;
													value1.amountPaidOld = value1.amountPaid;
												 	value1.availableCredit = parseFloat(0);
												 }
											 else{
												    value1.availableCredit =value1.availableCredit - paidAmtTemp;
												    value1.consumedCredit = value1.consumedCredit + paidAmtTemp;
												    value1.consumedCreditOld =value1.consumedCredit;
													value1.amountPaid = value1.amountPaid + paidAmtTemp;
													value1.amountPaidOld =value1.amountPaid;
												 	paidAmtTemp = paidAmtTemp - paidAmtTemp;
											 }
									 });
									 value.paidAmount = paidAmtTemp;
								}
						  processedShareAmt = parseFloat(processedShareAmt) + (isNaN(value.processedShareAmt) ? 0 : parseFloat(value.processedShareAmt));
						  totalAmount = parseFloat(totalAmount) + (isNaN(value.totalAmount) ? 0 : parseFloat(value.totalAmount));
						/*retainershipFee = parseFloat(retainershipFee) + (isNaN(value.retainershipFee) ? 0 : parseFloat(value.retainershipFee));*/
						  paidAmount = parseFloat(paidAmount) + (isNaN(value.paidAmount) ? 0 : parseFloat(value.paidAmount));
						  totalDueShareAmt = parseFloat(totalDueShareAmt) + (isNaN(value.dueShareAmt) ? 0 : parseFloat(value.dueShareAmt));
						//totalDoctorCredit = parseFloat(totalDueShareAmt) + (isNaN(value.dueShareAmt) ? 0 : parseFloat(value.dueShareAmt));
						  value.docCreditTemp = (isNaN(value.totalAmount) ? 0 : parseFloat(value.totalAmount)) - parseFloat(value.paidAmount);
					}
					i++;
				});
				$scope.total = $rootScope.parseFloatNum(totalAmount);
				//$scope.retainershipFee = $rootScope.parseFloatNum(retainershipFee);
				$scope.paidAmount = paidAmount;
				$scope.processedShareAmt = $rootScope.parseFloatNum(processedShareAmt);
				$scope.totalDueShareAmt = $rootScope.parseFloatNum(totalDueShareAmt);
				//$scope.totalDoctorCredit = $rootScope.parseFloatNum(totalDoctorCredit);
		  }
		  
		 $scope.singleCheckPREV = function(item,idx)
		 {
			 //alert(type);
			 if(item.doctorShare)
				 {
				   $scope.checkPaidAmountCalculates();
				 }
			 else{
				 $rootScope.startLoader();
				 $scope.selectedAll = false;
				 var tempAmtRev = 0;
				 tempAmtRev = $scope.doctorSharePaymentVoucherList[idx].totalAmount  - $scope.doctorSharePaymentVoucherList[idx].paidAmount;
				 $scope.doctorSharePaymentVoucherList[idx].paidAmount =  $scope.doctorSharePaymentVoucherList[idx].totalAmount;
				 $scope.totalDoctorCredit = $scope.totalDoctorCredit + tempAmtRev;
				 $scope.paidAmount = $scope.paidAmount + $scope.doctorSharePaymentVoucherList[idx].docCreditTemp;
				 $scope.doctorSharePaymentVoucherList[idx].docCreditTemp = $scope.doctorSharePaymentVoucherList[idx].docCreditTemp - tempAmtRev;
				 $scope.total =  $scope.total -  $scope.doctorSharePaymentVoucherList[idx].totalAmount;
				 $scope.processedShareAmt = $scope.processedShareAmt - $scope.doctorSharePaymentVoucherList[idx].processedShareAmt;
				 $scope.totalDueShareAmt =  $scope.totalDueShareAmt - $scope.doctorSharePaymentVoucherList[idx].dueShareAmt;
				 var v = 1;
				 angular.forEach($scope.doctorCreditDetails,function(value1,index){
					
							 if(value1.consumedCredit >= tempAmtRev)
							 {
							 	
								 if(value1.consumedCredit > 0 && tempAmtRev > 0){
							 	value1.consumedCredit = value1.consumedCredit - tempAmtRev;
							 	value1.consumedCreditOld = value1.consumedCredit;
								value1.amountPaid = value1.consumedCredit;
								value1.amountPaidOld = value1.amountPaid;
							 	value1.availableCredit =tempAmtRev;
							 	tempAmtRev = 0;
								 }
							 }
						 else{
							 	if(value1.consumedCredit > 0){
							 	tempAmtRev = tempAmtRev - value1.consumedCredit;
							    value1.availableCredit = value1.doctorCredit;
							    value1.consumedCredit = 0;
							    value1.consumedCreditOld = 0;
								value1.amountPaid = 0;
								value1.amountPaidOld = 0;
								
								}
						 }
							 if(v == $scope.doctorCreditDetails.length)
								 {
								   $rootScope.stopLoader();
								 }
				 });
				 
			 }
		 }
		 
		 
			//Check item
			
			var isItemExists;
			function checkItemIdExistsOrNot(arr) {
				return arr
						.some(function(arr) {
							if(arr.doctorShare  == true)
								{
									return true;
								}
							else
								{
									return false;
								}
						});
			}
			//End
		 
		 $scope.singleCheck = function(item,idx)
		 {
			
			 if(item.doctorShare)
				 {
					 /*if($scope.doctorSharePaymentVoucherListFinal.length > 0)
					 {
						 $scope.doctorSharePaymentVoucherListFinal[0].totalAmount = parseFloat($scope.doctorSharePaymentVoucherListFinal[0].totalAmount) + parseFloat($scope.doctorSharePaymentVoucherList[idx].totalAmount);
						 $scope.doctorSharePaymentVoucherListFinal[0].processedShareAmt = parseFloat($scope.doctorSharePaymentVoucherListFinal[0].processedShareAmt) + parseFloat($scope.doctorSharePaymentVoucherList[idx].processedShareAmt);
						 $scope.doctorSharePaymentVoucherListFinal[0].dueShareAmt = parseFloat($scope.doctorSharePaymentVoucherListFinal[0].dueShareAmt) + parseFloat($scope.doctorSharePaymentVoucherList[idx].dueShareAmt);
						 $scope.doctorSharePaymentVoucherListFinal[0].retainershipFee = parseFloat($scope.doctorSharePaymentVoucherListFinal[0].retainershipFee) + parseFloat($scope.doctorSharePaymentVoucherList[idx].retainershipFee);
						 $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = parseFloat($scope.doctorSharePaymentVoucherListFinal[0].paidAmount) + parseFloat($scope.doctorSharePaymentVoucherList[idx].paidAmount);
						 
						 
						 var param = {   
						  			doctorId:$scope.doctorId,
						  			orgUnitId:$scope.unitId
							    }
							 //console.log("param",JSON.stringify(param));
							$rootScope.startLoaderPopup();
							var URI = BASE_URL + ROOT_URL + "/api/docShare/cr/doctorSharePaymentVoucher/doctorCrDetails";
							BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
								if (response.data.status == "success")
									{
									  $scope.doctorCreditDetails = response.data.listObject;
									  var totalDoctorCredit = 0;
									  var totalDoctorCreditOld = 0;
									  var t= 1;
									  angular.forEach(response.data.listObject,function(value,index){
										  totalDoctorCredit = parseFloat(totalDoctorCredit) + (isNaN(value.doctorCredit) ? 0 : parseFloat(value.doctorCredit));
										  totalDoctorCreditOld = parseFloat(totalDoctorCreditOld) + (isNaN(value.balanceCredit) ? 0 : parseFloat(value.balanceCredit));
										  if(t == $scope.doctorCreditDetails.length)
											  {
											     $scope.totalDoctorCredit = totalDoctorCredit;
											     $scope.totalDoctorCreditOld = totalDoctorCreditOld;
											     
											     var totalPaidAmount = $scope.doctorSharePaymentVoucherListFinal[0].paidAmount;
												 var totalConsumnedTemp = 0;
												 if($scope.totalDoctorCredit >=totalPaidAmount)
													{
													  $scope.totalDoctorCredit = parseFloat($scope.totalDoctorCredit) - (isNaN(totalPaidAmount) ? 0 : parseFloat(totalPaidAmount));
													  //$scope.doctorSharePaymentVoucherList[i].docCreditTemp = value.totalPaidAmount;
													 // var paidAmtTemp = totalPaidAmount;
													  var j  =1;
													  angular.forEach($scope.doctorCreditDetails,function(value1,index){
														  if(value1.amountPaid != null && value1.amountPaid > 0 )
															  {
															  value1.amountPaid = value1.amountPaid;
															  }else
																  {
																  value1.amountPaid = 0;
																  }
														 if(totalPaidAmount >= value1.availableCredit)
															 {
															 	
															   totalPaidAmount = totalPaidAmount - value1.availableCredit;
															 	value1.consumedCredit = value1.consumedCredit + value1.availableCredit;
															 	value1.consumedCreditOld = value1.consumedCredit;
															 	totalConsumnedTemp = totalConsumnedTemp + value1.consumedCredit;
															 	value1.amountPaid = value1.amountPaid + value1.availableCredit;
															 	value1.amountPaidOld =value1.amountPaid;
															 	value1.availableCredit = parseFloat(0);
															 }
														 else{
															    value1.availableCredit =value1.availableCredit - totalPaidAmount;
															    value1.consumedCredit = value1.consumedCredit + totalPaidAmount;
															    value1.consumedCreditOld = value1.consumedCredit;
															    totalConsumnedTemp = totalConsumnedTemp + value1.consumedCredit;
															    value1.amountPaid =  value1.amountPaid +totalPaidAmount;
															    value1.amountPaidOld =value1.amountPaid;
															    totalPaidAmount = totalPaidAmount - totalPaidAmount;
														 }
														 if(j == $scope.doctorCreditDetails.length)
															 {
															 $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = totalPaidAmount;
															 $scope.doctorSharePaymentVoucherListFinal[0].totalDoctorConsumedCredit = totalConsumnedTemp;
															
															 }
														 j++;
													  });
													  //value.paidAmount = paidAmtTemp;
													  
													  
													}else{
														$scope.totalDoctorCredit = parseFloat(0);
														// var paidAmtTemp = value.paidAmount;
														 var k  =1;
														 angular.forEach($scope.doctorCreditDetails,function(value1,index){
															 if(value1.amountPaid != null && value1.amountPaid > 0 )
															  {
															  value1.amountPaid = value1.amountPaid;
															  }else
																  {
																  value1.amountPaid = 0;
																  }
																	 if(totalPaidAmount >= value1.availableCredit)
																	 {
																	 	
																		 totalPaidAmount = totalPaidAmount - value1.availableCredit;
																	 	value1.consumedCredit = value1.consumedCredit + value1.availableCredit;
																	 	value1.consumedCreditOld = value1.consumedCredit;
																	 	 totalConsumnedTemp = totalConsumnedTemp + value1.consumedCredit;
																		value1.amountPaid = value1.amountPaid + value1.availableCredit;
																		value1.amountPaidOld = value1.amountPaid;
																	 	value1.availableCredit = parseFloat(0);
																	 }
																 else{
																	    value1.availableCredit =value1.availableCredit - totalPaidAmount;
																	    value1.consumedCredit = value1.consumedCredit + totalPaidAmount;
																	    value1.consumedCreditOld =value1.consumedCredit;
																	    totalConsumnedTemp = totalConsumnedTemp + value1.consumedCredit;
																		value1.amountPaid = value1.amountPaid + totalPaidAmount;
																		value1.amountPaidOld =value1.amountPaid;
																		totalPaidAmount = totalPaidAmount - totalPaidAmount;
																 }
																	 if(k== $scope.doctorCreditDetails.length)
																	 {
																		 $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = totalPaidAmount;
																		 $scope.doctorSharePaymentVoucherListFinal[0].totalDoctorConsumedCredit = totalConsumnedTemp;
																		
																	 }
																 k++;
														 });
														 //value.paidAmount = paidAmtTemp;
													}
											  }
										  t++;
									  });
									  
									  
									}
								
								$rootScope.stopLoaderPopup();
							});
					/* }
					 else{*/ //com1
						 $scope.doctorSharePaymentVoucherListFinal = [];
						 
						 var doc = $.grep($scope.doctorList, function (doc) {
				   	           return doc.doctorId== $scope.doctorId;
				   	       })[0];
						var temp = {
								doctorId:$scope.doctorId,
								doctorName:doc.doctorName,
								totalDoctorCredit:$scope.totalDoctorCredit,
								totalDoctorConsumedCredit:0,
								totalAdvancedDoctorAmt:$scope.advancedDoctorAmount,
								totalAdvancedDoctorConsumedAmt : 0,
								totalAdvancedDoctorConsumedAmtOld :0
						};
						
						var totalShareAmount = 0;
						var totalProcessAmount = 0;
						var totalDueShareAmount = 0;
						var totalRetainershipFee = 0;
						var tdsAmt = 0;
						var totalPaidAmount = 0;
					
						/*totalShareAmount = parseFloat(totalShareAmount)  + parseFloat($scope.doctorSharePaymentVoucherList[idx].totalAmount);
						totalProcessAmount = parseFloat(totalProcessAmount) + parseFloat($scope.doctorSharePaymentVoucherList[idx].processedShareAmt);
						totalDueShareAmount = parseFloat(totalDueShareAmount) + parseFloat($scope.doctorSharePaymentVoucherList[idx].dueShareAmt);
						totalRetainershipFee = parseFloat(totalRetainershipFee) + parseFloat($scope.doctorSharePaymentVoucherList[idx].retainershipFee);*/
						//totalPaidAmount = parseFloat(totalPaidAmount) +  parseFloat($scope.doctorSharePaymentVoucherList[idx].paidAmount);
						
						
						//
						$scope.doctorSharePaymentVoucherList = $scope.pagination.data;
						var totalPaidAmountTeemp = 0;
						var x = 1;
						angular.forEach($scope.doctorSharePaymentVoucherList,function(value,index) {
							if(value.doctorShare){
							var tmpTds = 0;
							tmpTds = parseFloat((parseFloat(value.paidAmount) * parseFloat($scope.doctorSlabTDS)/100));
							totalPaidAmountTeemp = totalPaidAmountTeemp + value.paidAmount;
							totalShareAmount = parseFloat(totalShareAmount)  + parseFloat(value.totalAmount);
							totalProcessAmount = parseFloat(totalProcessAmount) + parseFloat(value.processedShareAmt);
							totalDueShareAmount = parseFloat(totalDueShareAmount) + parseFloat(value.dueShareAmt);
							totalRetainershipFee = parseFloat(totalRetainershipFee) + parseFloat(value.retainershipFee);
							tdsAmt = parseFloat(tdsAmt) + parseFloat(tmpTds);
							
							}
							if(x == $scope.doctorSharePaymentVoucherList.length)
								{
								totalPaidAmount = totalPaidAmountTeemp;
								temp.totalAmount = totalShareAmount;
								temp.processedShareAmt = totalProcessAmount;
								temp.totalAmount = totalShareAmount;
								temp.dueShareAmt = totalDueShareAmount;
								temp.retainershipFee = totalRetainershipFee;
								 temp.retainershipFeeOld = totalRetainershipFee;
								 temp.tdsAmt = tdsAmt;
								 temp.tdsAmtOld = tdsAmt;
								 temp.paidAmount = totalPaidAmount - tdsAmt;
								$scope.doctorSharePaymentVoucherListFinal.push(temp);
								
								
								 
								 
								 var param = {   
								  			doctorId:$scope.doctorId,
								  			orgUnitId:$scope.unitId
									    }
									 //console.log("param",JSON.stringify(param));
									$rootScope.startLoaderPopup();
									var URI = BASE_URL + ROOT_URL + "/api/docShare/cr/doctorSharePaymentVoucher/doctorCrDetails";
									BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										if (response.data.status == "success")
											{
											$scope.doctorCreditDetails = [];
											  $scope.doctorCreditDetails = response.data.listObject;
											  if($scope.doctorCreditDetails.length > 0){
											  var totalDoctorCredit = 0;
											  var totalDoctorCreditOld = 0;
											  var t= 1;
											  angular.forEach(response.data.listObject,function(value,index){
												  totalDoctorCredit = parseFloat(totalDoctorCredit) + (isNaN(value.doctorCredit) ? 0 : parseFloat(value.doctorCredit));
												  totalDoctorCreditOld = parseFloat(totalDoctorCreditOld) + (isNaN(value.balanceCredit) ? 0 : parseFloat(value.balanceCredit));
												  if(t == $scope.doctorCreditDetails.length)
													  {
													     $scope.totalDoctorCredit = totalDoctorCredit;
													     $scope.totalDoctorCreditOld = totalDoctorCreditOld;
													     $scope.doctorSharePaymentVoucherListFinal[0].totalDoctorCredit = $scope.totalDoctorCredit;
													     var totalPaidAmount = $scope.doctorSharePaymentVoucherListFinal[0].paidAmount;
														 var totalConsumnedTemp = 0;
														 if($scope.totalDoctorCredit >=totalPaidAmount)
															{
															  $scope.totalDoctorCredit = parseFloat($scope.totalDoctorCredit) - (isNaN(totalPaidAmount) ? 0 : parseFloat(totalPaidAmount));
															  //$scope.doctorSharePaymentVoucherList[i].docCreditTemp = value.totalPaidAmount;
															 // var paidAmtTemp = totalPaidAmount;
															  var j  =1;
															  angular.forEach($scope.doctorCreditDetails,function(value1,index){
																  if(value1.amountPaid != null && value1.amountPaid > 0 )
																	  {
																	  value1.amountPaid = value1.amountPaid;
																	  }else
																		  {
																		  value1.amountPaid = 0;
																		  }
																 if(totalPaidAmount >= value1.availableCredit)
																	 {
																	 	
																	   totalPaidAmount = totalPaidAmount - value1.availableCredit;
																	 	value1.consumedCredit = value1.consumedCredit + value1.availableCredit;
																	 	value1.consumedCreditOld = value1.consumedCredit;
																	 	totalConsumnedTemp = totalConsumnedTemp + value1.consumedCredit;
																	 	value1.amountPaid = value1.amountPaid + value1.availableCredit;
																	 	value1.amountPaidOld =value1.amountPaid;
																	 	value1.availableCredit = parseFloat(0);
																	 }
																 else{
																	    value1.availableCredit =value1.availableCredit - totalPaidAmount;
																	    value1.consumedCredit = value1.consumedCredit + totalPaidAmount;
																	    value1.consumedCreditOld = value1.consumedCredit;
																	    totalConsumnedTemp = totalConsumnedTemp + value1.consumedCredit;
																	    value1.amountPaid =  value1.amountPaid +totalPaidAmount;
																	    value1.amountPaidOld =value1.amountPaid;
																	    totalPaidAmount = totalPaidAmount - totalPaidAmount;
																 }
																 if(j == $scope.doctorCreditDetails.length)
																	 {
																	 $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = totalPaidAmount;
																	 $scope.doctorSharePaymentVoucherListFinal[0].totalDoctorConsumedCredit = totalConsumnedTemp;
																	 
																	 if($scope.advancedDoctorAmount >= totalPaidAmount)
																	 {
																	   $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = 0;
																	  // $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt = $scope.advancedDoctorAmount - totalPaidAmount;
																	   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt = totalPaidAmount;
																	   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmtOld = totalPaidAmount;
																	 }else{
																		 
																		   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt = $scope.advancedDoctorAmount;
																		   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmtOld= $scope.advancedDoctorAmount;
																		   $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = totalPaidAmount - $scope.advancedDoctorAmount;
																	 }
																	 
																	 
																	
																	 }
																 j++;
															  });
															  //value.paidAmount = paidAmtTemp;
															  
															  
															}else{
																$scope.totalDoctorCredit = parseFloat(0);
																// var paidAmtTemp = value.paidAmount;
																 var k  =1;
																 angular.forEach($scope.doctorCreditDetails,function(value1,index){
																	 if(value1.amountPaid != null && value1.amountPaid > 0 )
																	  {
																	  value1.amountPaid = value1.amountPaid;
																	  }else
																		  {
																		  value1.amountPaid = 0;
																		  }
																			 if(totalPaidAmount >= value1.availableCredit)
																			 {
																			 	
																				 totalPaidAmount = totalPaidAmount - value1.availableCredit;
																			 	value1.consumedCredit = value1.consumedCredit + value1.availableCredit;
																			 	value1.consumedCreditOld = value1.consumedCredit;
																			 	 totalConsumnedTemp = totalConsumnedTemp + value1.consumedCredit;
																				value1.amountPaid = value1.amountPaid + value1.availableCredit;
																				value1.amountPaidOld = value1.amountPaid;
																			 	value1.availableCredit = parseFloat(0);
																			 }
																		 else{
																			    value1.availableCredit =value1.availableCredit - totalPaidAmount;
																			    value1.consumedCredit = value1.consumedCredit + totalPaidAmount;
																			    value1.consumedCreditOld =value1.consumedCredit;
																			    totalConsumnedTemp = totalConsumnedTemp + value1.consumedCredit;
																				value1.amountPaid = value1.amountPaid + totalPaidAmount;
																				value1.amountPaidOld =value1.amountPaid;
																				totalPaidAmount = totalPaidAmount - totalPaidAmount;
																		 }
																			 if(k== $scope.doctorCreditDetails.length)
																			 {
																				 $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = totalPaidAmount;
																				 $scope.doctorSharePaymentVoucherListFinal[0].totalDoctorConsumedCredit = totalConsumnedTemp;
																				 
																				 if($scope.advancedDoctorAmount >= totalPaidAmount)
																				 {
																				   $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = 0;
																				  // $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt = $scope.advancedDoctorAmount - totalPaidAmount;
																				   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt = totalPaidAmount;
																				   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmtOld = totalPaidAmount;
																				 }else{
																					 
																					   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt = $scope.advancedDoctorAmount;
																					   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmtOld= $scope.advancedDoctorAmount;
																					   $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = totalPaidAmount - $scope.advancedDoctorAmount;
																				 }
																				 
																				
																			 }
																		 k++;
																 });
																 //value.paidAmount = paidAmtTemp;
															}
													  }
												  t++;
											  });
											  
											  
											  //
													}
												else{
														if($scope.doctorSharePaymentVoucherListFinal[0].paidAmount > 0)
														 {
															  if($scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorAmt > 0){
															  if($scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorAmt >= $scope.doctorSharePaymentVoucherListFinal[0].paidAmount)
																 {
																 
																  $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt =$scope.doctorSharePaymentVoucherListFinal[0].paidAmount;
																   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmtOld = $scope.doctorSharePaymentVoucherListFinal[0].paidAmount ;
																   $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = 0;
																  
																 }else{
																	 $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = $scope.doctorSharePaymentVoucherListFinal[0].paidAmount - $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorAmt;
																	 $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt =$scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorAmt;
																	   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmtOld = $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorAmt;
																 }
															  }
													  }
														  $rootScope.stopLoaderPopup();
												  
												}
											  //
											  
											}
										
										$rootScope.stopLoaderPopup();
									});
									
								}
							
							x++;
						});
						//
						
						
							
							
						
					// } //cm1
				 }
			 else{
				     $scope.selectedAll = false;
					 if($scope.doctorSharePaymentVoucherListFinal.length > 0)
					 {
						 //
                           $scope.doctorSharePaymentVoucherListFinal = [];
						 
						 var doc = $.grep($scope.doctorList, function (doc) {
				   	           return doc.doctorId== $scope.doctorId;
				   	       })[0];
						var temp = {
								doctorId:$scope.doctorId,
								doctorName:doc.doctorName,
								totalDoctorCredit:$scope.totalDoctorCredit,
								totalDoctorConsumedCredit:0,
								totalAdvancedDoctorAmt:$scope.advancedDoctorAmount,
								totalAdvancedDoctorConsumedAmt : 0,
								totalAdvancedDoctorConsumedAmtOld:0
						};
						
						var totalShareAmount = 0;
						var totalProcessAmount = 0;
						var totalDueShareAmount = 0;
						var totalRetainershipFee = 0;
						var tdsAmt = 0;
						var totalPaidAmount = 0;
						
						//totalPaidAmount = parseFloat(totalPaidAmount) +  parseFloat($scope.doctorSharePaymentVoucherList[idx].paidAmount);
					
						var totalPaidAmountTeemp = 0;
						var x = 1;
						angular.forEach($scope.doctorSharePaymentVoucherList,function(value,index) {
							if(value.doctorShare){
								
								var tmpTds = 0;
								tmpTds = parseFloat((parseFloat(value.paidAmount) * parseFloat($scope.doctorSlabTDS)/100));
								
							totalPaidAmountTeemp = totalPaidAmountTeemp + value.paidAmount;
							totalShareAmount = parseFloat(totalShareAmount)  + parseFloat(value.totalAmount);
							totalProcessAmount = parseFloat(totalProcessAmount) + parseFloat(value.processedShareAmt);
							totalDueShareAmount = parseFloat(totalDueShareAmount) + parseFloat(value.dueShareAmt);
							totalRetainershipFee = parseFloat(totalRetainershipFee) + parseFloat(value.retainershipFee);
							tdsAmt = parseFloat(tdsAmt) + parseFloat(tmpTds);
							}
							if(x == $scope.doctorSharePaymentVoucherList.length)
								{
								totalPaidAmount = totalPaidAmountTeemp;
								temp.totalAmount = totalShareAmount;
								temp.processedShareAmt = totalProcessAmount;
								temp.totalAmount = totalShareAmount;
								temp.dueShareAmt = totalDueShareAmount;
								temp.retainershipFee = totalRetainershipFee;
								temp.retainershipFeeOld = totalRetainershipFee;
								temp.tdsAmt = tdsAmt;
								temp.tdsAmtOld = tdsAmt;
								temp.paidAmount = totalPaidAmount - tdsAmt;
								$scope.doctorSharePaymentVoucherListFinal.push(temp);
								
								
								 
								 
								 var param = {   
								  			doctorId:$scope.doctorId,
								  			orgUnitId:$scope.unitId
									    }
									 //console.log("param",JSON.stringify(param));
									$rootScope.startLoaderPopup();
									var URI = BASE_URL + ROOT_URL + "/api/docShare/cr/doctorSharePaymentVoucher/doctorCrDetails";
									BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										if (response.data.status == "success")
											{
											$scope.doctorCreditDetails = [];
											  $scope.doctorCreditDetails = response.data.listObject;
											  if($scope.doctorCreditDetails.length > 0){
											  var totalDoctorCredit = 0;
											  var totalDoctorCreditOld = 0;
											  var t= 1;
											  angular.forEach(response.data.listObject,function(value,index){
												  totalDoctorCredit = parseFloat(totalDoctorCredit) + (isNaN(value.doctorCredit) ? 0 : parseFloat(value.doctorCredit));
												  totalDoctorCreditOld = parseFloat(totalDoctorCreditOld) + (isNaN(value.balanceCredit) ? 0 : parseFloat(value.balanceCredit));
												  if(t == $scope.doctorCreditDetails.length)
													  {
													     $scope.totalDoctorCredit = totalDoctorCredit;
													     $scope.totalDoctorCreditOld = totalDoctorCreditOld;
													     $scope.doctorSharePaymentVoucherListFinal[0].totalDoctorCredit = $scope.totalDoctorCredit;
													     var totalPaidAmount = $scope.doctorSharePaymentVoucherListFinal[0].paidAmount;
														 var totalConsumnedTemp = 0;
														 if($scope.totalDoctorCredit >=totalPaidAmount)
															{
															  $scope.totalDoctorCredit = parseFloat($scope.totalDoctorCredit) - (isNaN(totalPaidAmount) ? 0 : parseFloat(totalPaidAmount));
															  //$scope.doctorSharePaymentVoucherList[i].docCreditTemp = value.totalPaidAmount;
															 // var paidAmtTemp = totalPaidAmount;
															  var j  =1;
															  angular.forEach($scope.doctorCreditDetails,function(value1,index){
																  if(value1.amountPaid != null && value1.amountPaid > 0 )
																	  {
																	  value1.amountPaid = value1.amountPaid;
																	  }else
																		  {
																		  value1.amountPaid = 0;
																		  }
																 if(totalPaidAmount >= value1.availableCredit)
																	 {
																	 	
																	   totalPaidAmount = totalPaidAmount - value1.availableCredit;
																	 	value1.consumedCredit = value1.consumedCredit + value1.availableCredit;
																	 	value1.consumedCreditOld = value1.consumedCredit;
																	 	totalConsumnedTemp = totalConsumnedTemp + value1.consumedCredit;
																	 	value1.amountPaid = value1.amountPaid + value1.availableCredit;
																	 	value1.amountPaidOld =value1.amountPaid;
																	 	value1.availableCredit = parseFloat(0);
																	 }
																 else{
																	    value1.availableCredit =value1.availableCredit - totalPaidAmount;
																	    value1.consumedCredit = value1.consumedCredit + totalPaidAmount;
																	    value1.consumedCreditOld = value1.consumedCredit;
																	    totalConsumnedTemp = totalConsumnedTemp + value1.consumedCredit;
																	    value1.amountPaid =  value1.amountPaid +totalPaidAmount;
																	    value1.amountPaidOld =value1.amountPaid;
																	    totalPaidAmount = totalPaidAmount - totalPaidAmount;
																 }
																 if(j == $scope.doctorCreditDetails.length)
																	 {
																	 $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = totalPaidAmount;
																	 $scope.doctorSharePaymentVoucherListFinal[0].totalDoctorConsumedCredit = totalConsumnedTemp;
																	 
																	 if($scope.advancedDoctorAmount >= totalPaidAmount)
																	 {
																	   $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = 0;
																	  // $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt = $scope.advancedDoctorAmount - totalPaidAmount;
																	   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt = totalPaidAmount;
																	   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmtOld = totalPaidAmount;
																	 }else{
																		 
																		   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt = $scope.advancedDoctorAmount;
																		   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmtOld= $scope.advancedDoctorAmount;
																		   $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = totalPaidAmount - $scope.advancedDoctorAmount;
																	 }
																	 
																	
																	 }
																 j++;
															  });
															  //value.paidAmount = paidAmtTemp;
															  
															  
															}else{
																$scope.totalDoctorCredit = parseFloat(0);
																// var paidAmtTemp = value.paidAmount;
																 var k  =1;
																 angular.forEach($scope.doctorCreditDetails,function(value1,index){
																	 if(value1.amountPaid != null && value1.amountPaid > 0 )
																	  {
																	  value1.amountPaid = value1.amountPaid;
																	  }else
																		  {
																		  value1.amountPaid = 0;
																		  }
																			 if(totalPaidAmount >= value1.availableCredit)
																			 {
																			 	
																				 totalPaidAmount = totalPaidAmount - value1.availableCredit;
																			 	value1.consumedCredit = value1.consumedCredit + value1.availableCredit;
																			 	value1.consumedCreditOld = value1.consumedCredit;
																			 	 totalConsumnedTemp = totalConsumnedTemp + value1.consumedCredit;
																				value1.amountPaid = value1.amountPaid + value1.availableCredit;
																				value1.amountPaidOld = value1.amountPaid;
																			 	value1.availableCredit = parseFloat(0);
																			 }
																		 else{
																			    value1.availableCredit =value1.availableCredit - totalPaidAmount;
																			    value1.consumedCredit = value1.consumedCredit + totalPaidAmount;
																			    value1.consumedCreditOld =value1.consumedCredit;
																			    totalConsumnedTemp = totalConsumnedTemp + value1.consumedCredit;
																				value1.amountPaid = value1.amountPaid + totalPaidAmount;
																				value1.amountPaidOld =value1.amountPaid;
																				totalPaidAmount = totalPaidAmount - totalPaidAmount;
																		 }
																			 if(k== $scope.doctorCreditDetails.length)
																			 {
																				 $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = totalPaidAmount;
																				 $scope.doctorSharePaymentVoucherListFinal[0].totalDoctorConsumedCredit = totalConsumnedTemp;
																				 
																				 if($scope.advancedDoctorAmount >= totalPaidAmount)
																				 {
																				   $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = 0;
																				  // $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt = $scope.advancedDoctorAmount - totalPaidAmount;
																				   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt = totalPaidAmount;
																				   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmtOld = totalPaidAmount;
																				 }else{
																					 
																					   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt = $scope.advancedDoctorAmount;
																					   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmtOld= $scope.advancedDoctorAmount;
																					   $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = totalPaidAmount - $scope.advancedDoctorAmount;
																				 }
																				 
																				
																			 }
																		 k++;
																 });
																 //value.paidAmount = paidAmtTemp;
															}
													  }
												  t++;
											  });
											  
											  //
											}
										else{
												if($scope.doctorSharePaymentVoucherListFinal[0].paidAmount > 0)
												 {
													  if($scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorAmt > 0){
													  if($scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorAmt >= $scope.doctorSharePaymentVoucherListFinal[0].paidAmount)
														 {
														 
														  $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt =$scope.doctorSharePaymentVoucherListFinal[0].paidAmount;
														   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmtOld = $scope.doctorSharePaymentVoucherListFinal[0].paidAmount ;
														   $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = 0;
														  
														 }else{
															 $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = $scope.doctorSharePaymentVoucherListFinal[0].paidAmount - $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorAmt;
															 $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt =$scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorAmt;
															   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmtOld = $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorAmt;
														 }
													  }
											  }
												  $rootScope.stopLoaderPopup();
										  
										}
									  //
											}
										
										$rootScope.stopLoaderPopup();
									});
								}
							
							x++;
						});
						 //
						 /*$scope.doctorSharePaymentVoucherListFinal[0].totalAmount = parseFloat($scope.doctorSharePaymentVoucherListFinal[0].totalAmount) - parseFloat($scope.doctorSharePaymentVoucherList[idx].totalAmount);
						 $scope.doctorSharePaymentVoucherListFinal[0].processedShareAmt = parseFloat($scope.doctorSharePaymentVoucherListFinal[0].processedShareAmt) - parseFloat($scope.doctorSharePaymentVoucherList[idx].processedShareAmt);
						 $scope.doctorSharePaymentVoucherListFinal[0].dueShareAmt = parseFloat($scope.doctorSharePaymentVoucherListFinal[0].dueShareAmt) - parseFloat($scope.doctorSharePaymentVoucherList[idx].dueShareAmt);
						 $scope.doctorSharePaymentVoucherListFinal[0].retainershipFee = parseFloat($scope.doctorSharePaymentVoucherListFinal[0].retainershipFee) - parseFloat($scope.doctorSharePaymentVoucherList[idx].retainershipFee);
						 if(parseFloat($scope.doctorSharePaymentVoucherListFinal[0].paidAmount) > 0)
								 {
							 
						 $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = parseFloat($scope.doctorSharePaymentVoucherListFinal[0].paidAmount) + parseFloat($scope.doctorSharePaymentVoucherList[idx].paidAmount);
						 }
						 else{
							 $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = parseFloat($scope.doctorSharePaymentVoucherList[idx].paidAmount);
						 }
						 
						 var param = {   
						  			doctorId:$scope.doctorId,
						  			orgUnitId:$scope.unitId
							    }
							 //console.log("param",JSON.stringify(param));
							$rootScope.startLoaderPopup();
							var URI = BASE_URL + ROOT_URL + "/api/docShare/cr/doctorSharePaymentVoucher/doctorCrDetails";
							BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
								if (response.data.status == "success")
									{$scope.doctorCreditDetails = [];
									  $scope.doctorCreditDetails = response.data.listObject;
									  var totalDoctorCredit = 0;
									  var totalDoctorCreditOld = 0;
									  var t= 1;
									  angular.forEach(response.data.listObject,function(value,index){
										  totalDoctorCredit = parseFloat(totalDoctorCredit) + (isNaN(value.doctorCredit) ? 0 : parseFloat(value.doctorCredit));
										  totalDoctorCreditOld = parseFloat(totalDoctorCreditOld) + (isNaN(value.balanceCredit) ? 0 : parseFloat(value.balanceCredit));
										  if(t == $scope.doctorCreditDetails.length)
											  {
											     $scope.totalDoctorCredit = totalDoctorCredit;
											     $scope.totalDoctorCreditOld = totalDoctorCreditOld;
											     
											     var totalPaidAmount = $scope.doctorSharePaymentVoucherListFinal[0].dueShareAmt;
												 var totalConsumnedTemp = 0;
												 if($scope.totalDoctorCredit >=totalPaidAmount)
													{
													  $scope.totalDoctorCredit = parseFloat($scope.totalDoctorCredit) - (isNaN(totalPaidAmount) ? 0 : parseFloat(totalPaidAmount));
													  //$scope.doctorSharePaymentVoucherList[i].docCreditTemp = value.totalPaidAmount;
													 // var paidAmtTemp = totalPaidAmount;
													  var j  =1;
													  angular.forEach($scope.doctorCreditDetails,function(value1,index){
														  if(value1.amountPaid != null && value1.amountPaid > 0 )
															  {
															  value1.amountPaid = value1.amountPaid;
															  }else
																  {
																  value1.amountPaid = 0;
																  }
														 if(totalPaidAmount >= value1.availableCredit)
															 {
															 	
															   totalPaidAmount = totalPaidAmount - value1.availableCredit;
															 	value1.consumedCredit = value1.consumedCredit + value1.availableCredit;
															 	value1.consumedCreditOld = value1.consumedCredit;
															 	totalConsumnedTemp = totalConsumnedTemp + value1.consumedCredit;
															 	value1.amountPaid = value1.amountPaid + value1.availableCredit;
															 	value1.amountPaidOld =value1.amountPaid;
															 	value1.availableCredit = parseFloat(0);
															 }
														 else{
															    value1.availableCredit =value1.availableCredit - totalPaidAmount;
															    value1.consumedCredit = value1.consumedCredit + totalPaidAmount;
															    value1.consumedCreditOld = value1.consumedCredit;
															    totalConsumnedTemp = totalConsumnedTemp + value1.consumedCredit;
															    value1.amountPaid =  value1.amountPaid +totalPaidAmount;
															    value1.amountPaidOld =value1.amountPaid;
															    totalPaidAmount = totalPaidAmount - totalPaidAmount;
														 }
														 if(j == $scope.doctorCreditDetails.length)
															 {
															 //$scope.doctorSharePaymentVoucherListFinal[0].paidAmount = totalPaidAmount;
															 $scope.doctorSharePaymentVoucherListFinal[0].totalDoctorConsumedCredit = totalConsumnedTemp;
															 isItemExists=checkItemIdExistsOrNot($scope.doctorSharePaymentVoucherList);
															  if (!isItemExists) {
																  
																  $scope.doctorSharePaymentVoucherListFinal = [];
															  }
															 }
														 j++;
													  });
													  //value.paidAmount = paidAmtTemp;
													  
													  
													}else{
														$scope.totalDoctorCredit = parseFloat(0);
														// var paidAmtTemp = value.paidAmount;
														 var k  =1;
														 angular.forEach($scope.doctorCreditDetails,function(value1,index){
															 if(value1.amountPaid != null && value1.amountPaid > 0 )
															  {
															  value1.amountPaid = value1.amountPaid;
															  }else
																  {
																  value1.amountPaid = 0;
																  }
																	 if(totalPaidAmount >= value1.availableCredit)
																	 {
																	 	
																		 totalPaidAmount = totalPaidAmount - value1.availableCredit;
																	 	value1.consumedCredit = value1.consumedCredit + value1.availableCredit;
																	 	value1.consumedCreditOld = value1.consumedCredit;
																	 	 totalConsumnedTemp = totalConsumnedTemp + value1.consumedCredit;
																		value1.amountPaid = value1.amountPaid + value1.availableCredit;
																		value1.amountPaidOld = value1.amountPaid;
																	 	value1.availableCredit = parseFloat(0);
																	 }
																 else{
																	    value1.availableCredit =value1.availableCredit - totalPaidAmount;
																	    value1.consumedCredit = value1.consumedCredit + totalPaidAmount;
																	    value1.consumedCreditOld =value1.consumedCredit;
																	    totalConsumnedTemp = totalConsumnedTemp + value1.consumedCredit;
																		value1.amountPaid = value1.amountPaid + totalPaidAmount;
																		value1.amountPaidOld =value1.amountPaid;
																		totalPaidAmount = totalPaidAmount - totalPaidAmount;
																 }
																	 if(k== $scope.doctorCreditDetails.length)
																	 {
																		// $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = totalPaidAmount;
																		 $scope.doctorSharePaymentVoucherListFinal[0].totalDoctorConsumedCredit = totalConsumnedTemp;
																		 isItemExists=checkItemIdExistsOrNot($scope.doctorSharePaymentVoucherList);
																		  if (!isItemExists) {
																			  
																			  $scope.doctorSharePaymentVoucherListFinal = [];
																		  }
																	 }
																 k++;
														 });
														 //value.paidAmount = paidAmtTemp;
													}
											  }
										  t++;
									  });
									  
									  
									}
								
								$rootScope.stopLoaderPopup();
							});*/
							
						
						 
					 }
					 else{
						 $scope.doctorSharePaymentVoucherListFinal = [];
					 }
					 
			 }
			/* else{
				 $rootScope.startLoader();
				 $scope.selectedAll = false;
				 var tempAmtRev = 0;
				 tempAmtRev = $scope.doctorSharePaymentVoucherList[idx].totalAmount  - $scope.doctorSharePaymentVoucherList[idx].paidAmount;
				 $scope.doctorSharePaymentVoucherList[idx].paidAmount =  $scope.doctorSharePaymentVoucherList[idx].totalAmount;
				 $scope.totalDoctorCredit = $scope.totalDoctorCredit + tempAmtRev;
				 $scope.paidAmount = $scope.paidAmount + $scope.doctorSharePaymentVoucherList[idx].docCreditTemp;
				 $scope.doctorSharePaymentVoucherList[idx].docCreditTemp = $scope.doctorSharePaymentVoucherList[idx].docCreditTemp - tempAmtRev;
				 $scope.total =  $scope.total -  $scope.doctorSharePaymentVoucherList[idx].totalAmount;
				 $scope.processedShareAmt = $scope.processedShareAmt - $scope.doctorSharePaymentVoucherList[idx].processedShareAmt;
				 $scope.totalDueShareAmt =  $scope.totalDueShareAmt - $scope.doctorSharePaymentVoucherList[idx].dueShareAmt;
				 var v = 1;
				 angular.forEach($scope.doctorCreditDetails,function(value1,index){
					
							 if(value1.consumedCredit >= tempAmtRev)
							 {
							 	
								 if(value1.consumedCredit > 0 && tempAmtRev > 0){
							 	value1.consumedCredit = value1.consumedCredit - tempAmtRev;
							 	value1.consumedCreditOld = value1.consumedCredit;
								value1.amountPaid = value1.consumedCredit;
								value1.amountPaidOld = value1.amountPaid;
							 	value1.availableCredit =tempAmtRev;
							 	tempAmtRev = 0;
								 }
							 }
						 else{
							 	if(value1.consumedCredit > 0){
							 	tempAmtRev = tempAmtRev - value1.consumedCredit;
							    value1.availableCredit = value1.doctorCredit;
							    value1.consumedCredit = 0;
							    value1.consumedCreditOld = 0;
								value1.amountPaid = 0;
								value1.amountPaidOld = 0;
								
								}
						 }
							 if(v == $scope.doctorCreditDetails.length)
								 {
								   $rootScope.stopLoader();
								 }
				 });
				 
			 }*/
		 }
		  $scope.getDoctorCrDetailsTotal = function()
		  {
			  var param = {   
			  			doctorId:$scope.doctorId,
			  			orgUnitId:$scope.unitId
				    }
				 //console.log("param",JSON.stringify(param));
				$rootScope.startLoaderPopup();
				var URI = BASE_URL + ROOT_URL + "/api/docShare/cr/doctorSharePaymentVoucher/doctorCrDetails";
				BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success")
						{
						$scope.doctorCreditDetails = [];
						  $scope.doctorCreditDetails = response.data.listObject;
						  var totalDoctorCredit = 0;
						  var totalDoctorCreditOld = 0;
						  $scope.advancedDoctorAmount = 0;
						  angular.forEach(response.data.listObject,function(value,index){
							  totalDoctorCredit = parseFloat(totalDoctorCredit) + (isNaN(value.doctorCredit) ? 0 : parseFloat(value.doctorCredit));
							  totalDoctorCreditOld = parseFloat(totalDoctorCreditOld) + (isNaN(value.balanceCredit) ? 0 : parseFloat(value.balanceCredit));
							  
						  });
						  if(response.data.object != null)
							  {
								  $scope.advancedDoctorAmount  = parseFloat(response.data.object.doctorAdvance);
								  $scope.doctorAdvanceId  = parseFloat(response.data.object.doctorAdvanceId);
							  }else{
								  $scope.advancedDoctorAmount  = parseFloat(0);
								  $scope.doctorAdvanceId  = null;
							  }
						  $scope.totalDoctorCredit = totalDoctorCredit;
						  $scope.totalDoctorCreditOld = totalDoctorCreditOld;
						  
						}
					
					$rootScope.stopLoaderPopup();
				});
		  }
		  
		   $scope.getCalculationAmount = function(item,idx)
		  {
			  console.log(item);
			  console.log($scope.doctorCreditDetails);
			  if(item.amountPaid == '' || item.amountPaid == null)
				  {
				     item.amountPaid = 0;
				  }
			  if(item.amountPaid > item.doctorCredit)
				  {
						growl.error("Consume Credit should't greater then doctor credit..!!",{
							title : ERROR_MSG
						});
						$scope.doctorCreditDetails[idx].amountPaid = item.amountPaidOld;
						return false;
				  }
			  else
				  {
				  	if(item.amountPaid != item.amountPaidOld){
				  		if(item.amountPaid > item.amountPaidOld)
				  			{
				  				if($scope.doctorSharePaymentVoucherListFinal[0].paidAmount == 0)
				  					{
					  					 growl.warning("No paid amount to apply, you can select another from details tab and apply again..!!",{	
												title : WARNING_MSG
											});
					  					$scope.doctorCreditDetails[idx].amountPaid = item.amountPaidOld;
					  					 return false;
				  					}
				  				else
				  					{
				  					   var diff = item.amountPaid - item.amountPaidOld; 
				  					    $scope.doctorSharePaymentVoucherListFinal[0].totalDoctorConsumedCredit = $scope.doctorSharePaymentVoucherListFinal[0].totalDoctorConsumedCredit + diff;
				  					    $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = $scope.doctorSharePaymentVoucherListFinal[0].paidAmount - diff; 
				  					    $scope.doctorCreditDetails[idx].amountPaid = item.amountPaid;
							  			$scope.doctorCreditDetails[idx].amountPaidOld = item.amountPaid;
							  			$scope.doctorCreditDetails[idx].availableCredit = $scope.doctorCreditDetails[idx].availableCredit - diff;
							  			$scope.doctorCreditDetails[idx].consumedCredit = $scope.doctorCreditDetails[idx].consumedCredit + diff;
							  			$scope.doctorCreditDetails[idx].consumedCreditOld = $scope.doctorCreditDetails[idx].consumedCredit;
							  			
							  			
							  			
							  			if($scope.doctorSharePaymentVoucherListFinal[0].paidAmount > 0){
							  				var tempDiffAdvan = parseFloat($scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorAmt) - parseFloat($scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt); 
								  			 if(parseFloat(tempDiffAdvan) >= parseFloat($scope.doctorSharePaymentVoucherListFinal[0].paidAmount))
											 {
								  			   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt = parseFloat($scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt)  + parseFloat($scope.doctorSharePaymentVoucherListFinal[0].paidAmount);
								  			 $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmtOld = parseFloat($scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt);
								  			   $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = 0;
											  // $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt = $scope.advancedDoctorAmount - totalPaidAmount;
											
											 }else{
												 
												   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt = $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt + tempDiffAdvan;
												   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmtOld = $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt;
												   $scope.doctorSharePaymentVoucherListFinal[0].paidAmount =  $scope.doctorSharePaymentVoucherListFinal[0].paidAmount - tempDiffAdvan;
											 }
							  			}
							  			 
				  					}
					  			 /*var totalConsumedTemp = 0;
					  			 var i = 1;
								  angular.forEach($scope.doctorCreditDetails,function(value,index){
									  totalConsumedTemp = parseFloat(totalConsumedTemp) + (isNaN(value.amountPaid) ? 0 : parseFloat(value.amountPaid));
									  if(i == $scope.doctorCreditDetails.length){
										  if(item.totalConsumedTemp > $scope.totalDoctorCredit)
											  {
												  growl.error("Consume Credit should't greater then doctor credit..!!",{
														title : ERROR_MSG
													});
													$scope.doctorCreditDetails[idx].amountPaid = item.amountPaidOld;
													return false;
											  }
									  }
								  });*/
				  			}
				  		else{
				  			var diff = parseFloat(item.amountPaidOld) - parseFloat(item.amountPaid);
				  			$scope.doctorSharePaymentVoucherListFinal[0].totalDoctorConsumedCredit = parseFloat($scope.doctorSharePaymentVoucherListFinal[0].totalDoctorConsumedCredit) - parseFloat(diff);
				  			$scope.doctorSharePaymentVoucherListFinal[0].paidAmount = parseFloat($scope.doctorSharePaymentVoucherListFinal[0].paidAmount) + parseFloat(diff);
				  			$scope.doctorCreditDetails[idx].amountPaid = parseFloat(item.amountPaid);
				  			$scope.doctorCreditDetails[idx].amountPaidOld = parseFloat(item.amountPaid);
				  			$scope.doctorCreditDetails[idx].availableCredit = parseFloat($scope.doctorCreditDetails[idx].availableCredit) + parseFloat(diff);
				  			$scope.doctorCreditDetails[idx].consumedCredit = parseFloat($scope.doctorCreditDetails[idx].consumedCredit) - parseFloat(diff);
				  			$scope.doctorCreditDetails[idx].consumedCreditOld = parseFloat($scope.doctorCreditDetails[idx].consumedCredit);
				  			
				  			if(parseFloat($scope.doctorSharePaymentVoucherListFinal[0].paidAmount) > 0){
				  				var tempDiffAdvan = parseFloat($scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorAmt) - parseFloat($scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt); 
					  			 if(parseFloat(tempDiffAdvan) >= parseFloat($scope.doctorSharePaymentVoucherListFinal[0].paidAmount))
								 {
					  				 $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt = parseFloat($scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt)  + parseFloat($scope.doctorSharePaymentVoucherListFinal[0].paidAmount);
					  				$scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmtOld =  parseFloat($scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt);
					  				 $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = 0;
								  // $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt = $scope.advancedDoctorAmount - totalPaidAmount;
								  
								 }else{
									 
									   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt = parseFloat($scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt) + parseFloat(tempDiffAdvan);
									   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmtOld =  parseFloat($scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt);
									   $scope.doctorSharePaymentVoucherListFinal[0].paidAmount =  parseFloat($scope.doctorSharePaymentVoucherListFinal[0].paidAmount) - parseFloat(tempDiffAdvan);
								 }
				  			}
				  			
				  		}
				  		
				  		//$scope.doctorSharePaymentVoucherListFinal[0].
				  		//totalDoctorConsumedCredit
				  		//paidAmount
				  		//$scope.doctorCreditDetails
				  		//totalDoctorCredit
				  	}
					 
				  	
				  }
			  
		  }
		  
		  $scope.paidAmountCalculates = function(docShare){
				var retainershipFee=0;
				var paidAmount=0;
				angular.forEach($scope.doctorSharePaymentVoucherList,function(value,index){
					if(value.doctorShare == true){
						retainershipFee = parseFloat(retainershipFee) + ((value.retainershipFee == "" || isNaN(value.retainershipFee)) ? 0 : parseFloat(value.retainershipFee));
						paidAmount = parseFloat(paidAmount) + (value.paidAmount == "" || (isNaN(value.paidAmount)) ? 0 : parseFloat(value.paidAmount));
					}
				});
				$scope.retainershipFee = $rootScope.parseFloatNum(retainershipFee);
				$scope.paidAmount =paidAmount;
		  }
		  
		  
		  $scope.getDoctorCrDetails = function(info)
		  {
			  $scope.doctorCreditDetails = [];	
			  /*var param = {   
			  			doctorId:info.docId,
			  			docShareVoucheId:info.docShareVoucherId
				    }
				 //console.log("param",JSON.stringify(param));
				$rootScope.startLoaderPopup();
				var URI = BASE_URL + ROOT_URL + "/api/docShare/cr/doctorSharePaymentVoucher/doctorCrDetails";
				BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success")
						{
							$scope.doctorCreditDetails = [];
							
							angular.forEach(response.data.listObject,function(value,index){
								 //paidAmount
								//currentConsumedCredit
								//balanceCredit
								//consumedCredit
								//
							});
							
						}
					
					$rootScope.stopLoaderPopup();
				});*/
		  }
		  
		  $scope.calculateAmountsPREV = function(docShare){
			  if (docShare != undefined) 
				  if(docShare.retainershipFee>docShare.dueShareAmt){
					  growl.error("Amount not allowed..",{	
							title : ERROR_MSG
						});
					  docShare.retainershipFee = parseFloat(0);
					  docShare.paidAmount = parseFloat(docShare.dueShareAmt).toFixed(2);
						return false;
				  }
				  else{
			  docShare.paidAmount = (docShare.dueShareAmt - docShare.retainershipFee) >=0 ? docShare.dueShareAmt - docShare.retainershipFee : 0;
			/*  if(docShare.dueShareAmt == 0)
				  docShare.retainershipFee = docShare.tempDueShareAmt;*/
			  
				/*var retainershipFee=0;
				var paidAmount=0;
				angular.forEach($scope.doctorSharePaymentVoucherList,function(value,index){
					  if(value.doctorShare == true){
							
							retainershipFee = parseFloat(retainershipFee) + ((value.retainershipFee == "" || isNaN(value.retainershipFee)) ? 0 : parseFloat(value.retainershipFee));
							paidAmount = parseFloat(paidAmount) + (value.paidAmount == "" || (isNaN(value.paidAmount)) ? 0 : parseFloat(value.paidAmount));
				       }
				});
				$scope.retainershipFee = $rootScope.parseFloatNum(retainershipFee);
				$scope.paidAmount = paidAmount;*/
		  }
		  }
		  
		  $scope.calculateAmounts = function(docShare){
			  
			  if(docShare.retainershipFee == '' || docShare.retainershipFee == null)
				  {
				    docShare.retainershipFee = 0;
				  }
			  
			  if(docShare.paidAmount > 0){
				  if(docShare.retainershipFee > docShare.paidAmount)
					  {
					  
					  //totalDoctorConsumedCredit
					  //totalAdvancedDoctorConsumedAmt
					  //paidAmount
					  //paidAmount
					  	//Test S
					  		//var tmp = parseFloat(docShare.totalAmount) - parseFloat(docShare.totalDoctorConsumedCredit) - parseFloat(docShare.totalAdvancedDoctorConsumedAmt) -  parseFloat(docShare.tdsAmt);
					  var tmp = parseFloat(docShare.dueShareAmt) - parseFloat(docShare.totalDoctorConsumedCredit) - parseFloat(docShare.totalAdvancedDoctorConsumedAmt) -  parseFloat(docShare.tdsAmt);
					  		if(docShare.retainershipFee > tmp)
					  			{
							  		  growl.warning("RetainershipFee should be equal or less then paid amount..",{	
											title : WARNING_MSG
										});
									  $scope.doctorSharePaymentVoucherListFinal[0].retainershipFee =docShare.retainershipFeeOld;
									  return false;
					  			}
					  		else{
					  			 
				  				  $scope.doctorSharePaymentVoucherListFinal[0].retainershipFeeOld= $scope.doctorSharePaymentVoucherListFinal[0].retainershipFee;
				  			      $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = tmp - $scope.doctorSharePaymentVoucherListFinal[0].retainershipFee;
					  		}
					  	
					    //Test E
					  
					/*	  growl.warning("RetainershipFee should be equal or less then paid amount..",{	
								title : WARNING_MSG
							});
						  $scope.doctorSharePaymentVoucherListFinal[0].retainershipFee =docShare.retainershipFeeOld;
						  return false;*/
					  }
				  else
					  {
					  
					  
					  if(parseFloat(docShare.retainershipFee) > parseFloat(docShare.retainershipFeeOld))
					  {
					  	if($scope.doctorSharePaymentVoucherListFinal[0].paidAmount > 0)
					  		{
					  		  var diffAdv =  docShare.retainershipFee - docShare.retainershipFeeOld;
					  			if(diffAdv >= $scope.doctorSharePaymentVoucherListFinal[0].paidAmount)
					  				{
					  				
					  				  $scope.doctorSharePaymentVoucherListFinal[0].retainershipFee = $scope.doctorSharePaymentVoucherListFinal[0].paidAmount;
					  				  $scope.doctorSharePaymentVoucherListFinal[0].retainershipFeeOld= $scope.doctorSharePaymentVoucherListFinal[0].retainershipFee;
					  			      $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = 0;
					  				}
					  			else{
					  				$scope.doctorSharePaymentVoucherListFinal[0].paidAmount = $scope.doctorSharePaymentVoucherListFinal[0].paidAmount - diffAdv;
					  				$scope.doctorSharePaymentVoucherListFinal[0].retainershipFee = docShare.retainershipFee;
					  				 $scope.doctorSharePaymentVoucherListFinal[0].retainershipFeeOld= $scope.doctorSharePaymentVoucherListFinal[0].retainershipFee;
					  			}
					  		
					  		}
					  	else
					  		{
						  		 growl.warning("Paid amount is zero(0).",{	
										title : WARNING_MSG
									});
								  $scope.doctorSharePaymentVoucherListFinal[0].retainershipFee =docShare.retainershipFeeOld;
								  return false;
					  		}
					  }
					  else if(parseFloat(docShare.retainershipFee) < parseFloat(docShare.retainershipFeeOld))
					  {
					     var diffAdv = docShare.retainershipFeeOld  - docShare.retainershipFee;
					     $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = $scope.doctorSharePaymentVoucherListFinal[0].paidAmount + diffAdv;
					     $scope.doctorSharePaymentVoucherListFinal[0].retainershipFee = docShare.retainershipFee;
					     $scope.doctorSharePaymentVoucherListFinal[0].retainershipFeeOld = docShare.retainershipFee;
					     
					  }
					  
					  
					  
					  /*$scope.doctorSharePaymentVoucherListFinal[0].paidAmount = $scope.doctorSharePaymentVoucherListFinal[0].paidAmount - docShare.retainershipFee;
					  $scope.doctorSharePaymentVoucherListFinal[0].retainershipFee = docShare.retainershipFee;
					  $scope.doctorSharePaymentVoucherListFinal[0].retainershipFeeOld =docShare.retainershipFee;*/
					  }
			  }else
				  {
				  	if(parseFloat(docShare.retainershipFee) >= 0){
				  		
				  		//
				  	  if(parseFloat(docShare.retainershipFee) > parseFloat(docShare.retainershipFeeOld))
					  {
				  		 /* growl.warning("Retainership Fee should be equal or less than "+ docShare.retainershipFeeOld,{	
								title : WARNING_MSG
							});*/
						  $scope.doctorSharePaymentVoucherListFinal[0].retainershipFee =docShare.retainershipFeeOld;
						  return false;
					  		
					  	
					  }
					  else if(parseFloat(docShare.retainershipFee) < parseFloat(docShare.retainershipFeeOld))
					  {
					     var diffAdv = docShare.retainershipFeeOld  - docShare.retainershipFee;
					     $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = $scope.doctorSharePaymentVoucherListFinal[0].paidAmount + diffAdv;
					     $scope.doctorSharePaymentVoucherListFinal[0].retainershipFee = docShare.retainershipFee;
					     $scope.doctorSharePaymentVoucherListFinal[0].retainershipFeeOld = docShare.retainershipFee;
					     
					  }
				  		
				  		//
				  		
				  	}else{
					  growl.warning("Paid amount is zero(0).",{	
							title : WARNING_MSG
						});
					  $scope.doctorSharePaymentVoucherListFinal[0].retainershipFee =docShare.retainershipFeeOld;
					  return false;
				  	}
				  }
		  }
		  
		  
 $scope.calculateAmountsTds = function(docShare){
			  
			  if(docShare.tdsAmt == '' || docShare.tdsAmt == null)
				  {
				    docShare.tdsAmt = 0;
				  }
			  
			  if(docShare.paidAmount > 0){
				  if(docShare.tdsAmt > docShare.paidAmount)
					  {
					  
					  //totalDoctorConsumedCredit
					  //totalAdvancedDoctorConsumedAmt
					  //paidAmount
					  //paidAmount
					  	//Test S
					  		//var tmp = parseFloat(docShare.totalAmount) - parseFloat(docShare.totalDoctorConsumedCredit) - parseFloat(docShare.totalAdvancedDoctorConsumedAmt) - parseFloat(docShare.retainershipFee);
					  
					  var tmp = parseFloat(docShare.dueShareAmt) - parseFloat(docShare.totalDoctorConsumedCredit) - parseFloat(docShare.totalAdvancedDoctorConsumedAmt) - parseFloat(docShare.retainershipFee);
					  		if(docShare.tdsAmt > tmp)
					  			{
							  		  growl.warning("TDS Amount should be equal or less then paid amount..",{	
											title : WARNING_MSG
										});
									  $scope.doctorSharePaymentVoucherListFinal[0].tdsAmt =docShare.tdsAmtOld;
									  return false;
					  			}
					  		else{
					  			 
				  				  $scope.doctorSharePaymentVoucherListFinal[0].tdsAmtOld= $scope.doctorSharePaymentVoucherListFinal[0].tdsAmt;
				  			      $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = tmp - $scope.doctorSharePaymentVoucherListFinal[0].tdsAmt;
					  		}
					  	
					    //Test E
					  
					/*	  growl.warning("RetainershipFee should be equal or less then paid amount..",{	
								title : WARNING_MSG
							});
						  $scope.doctorSharePaymentVoucherListFinal[0].retainershipFee =docShare.retainershipFeeOld;
						  return false;*/
					  }
				  else
					  {
					  
					  
					  if(parseFloat(docShare.tdsAmt) > parseFloat(docShare.tdsAmtOld))
					  {
					  	if($scope.doctorSharePaymentVoucherListFinal[0].paidAmount > 0)
					  		{
					  		  var diffAdv =  docShare.tdsAmt - docShare.tdsAmtOld;
					  			if(diffAdv >= $scope.doctorSharePaymentVoucherListFinal[0].paidAmount)
					  				{
					  				
					  				  $scope.doctorSharePaymentVoucherListFinal[0].tdsAmt = $scope.doctorSharePaymentVoucherListFinal[0].paidAmount;
					  				  $scope.doctorSharePaymentVoucherListFinal[0].tdsAmtOld= $scope.doctorSharePaymentVoucherListFinal[0].tdsAmt;
					  			      $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = 0;
					  				}
					  			else{
					  				$scope.doctorSharePaymentVoucherListFinal[0].paidAmount = $scope.doctorSharePaymentVoucherListFinal[0].paidAmount - diffAdv;
					  				$scope.doctorSharePaymentVoucherListFinal[0].tdsAmt = docShare.tdsAmt;
					  				 $scope.doctorSharePaymentVoucherListFinal[0].tdsAmtOld= $scope.doctorSharePaymentVoucherListFinal[0].tdsAmt;
					  			}
					  		
					  		}
					  	else
					  		{
						  		 growl.warning("Paid amount is zero(0).",{	
										title : WARNING_MSG
									});
								  $scope.doctorSharePaymentVoucherListFinal[0].tdsAmt =docShare.tdsAmtOld;
								  return false;
					  		}
					  }
					  else if(parseFloat(docShare.tdsAmt) < parseFloat(docShare.tdsAmtOld))
					  {
					     var diffAdv = docShare.tdsAmtOld  - docShare.tdsAmt;
					     $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = $scope.doctorSharePaymentVoucherListFinal[0].paidAmount + diffAdv;
					     $scope.doctorSharePaymentVoucherListFinal[0].tdsAmt = docShare.tdsAmt;
					     $scope.doctorSharePaymentVoucherListFinal[0].tdsAmtOld = docShare.tdsAmt;
					     
					  }
					  
					  
					  
					  /*$scope.doctorSharePaymentVoucherListFinal[0].paidAmount = $scope.doctorSharePaymentVoucherListFinal[0].paidAmount - docShare.retainershipFee;
					  $scope.doctorSharePaymentVoucherListFinal[0].retainershipFee = docShare.retainershipFee;
					  $scope.doctorSharePaymentVoucherListFinal[0].retainershipFeeOld =docShare.retainershipFee;*/
					  }
			  }else
				  {
				  	if(parseFloat(docShare.tdsAmt) >= 0){
				  		
				  		//
				  	  if(parseFloat(docShare.tdsAmt) > parseFloat(docShare.tdsAmtOld))
					  {
				  		 /* growl.warning("Retainership Fee should be equal or less than "+ docShare.retainershipFeeOld,{	
								title : WARNING_MSG
							});*/
						  $scope.doctorSharePaymentVoucherListFinal[0].tdsAmt =docShare.tdsAmtOld;
						  return false;
					  		
					  	
					  }
					  else if(parseFloat(docShare.tdsAmt) < parseFloat(docShare.tdsAmtOld))
					  {
					     var diffAdv = docShare.tdsAmtOld  - docShare.tdsAmt;
					     $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = $scope.doctorSharePaymentVoucherListFinal[0].paidAmount + diffAdv;
					     $scope.doctorSharePaymentVoucherListFinal[0].tdsAmt = docShare.tdsAmt;
					     $scope.doctorSharePaymentVoucherListFinal[0].tdsAmtOld = docShare.tdsAmt;
					     
					  }
				  		
				  		//
				  		
				  	}else{
					  growl.warning("Paid amount is zero(0).",{	
							title : WARNING_MSG
						});
					  $scope.doctorSharePaymentVoucherListFinal[0].tdsAmt =docShare.tdsAmtOld;
					  return false;
				  	}
				  }
		  }
 
 
		  $scope.calculateAmountsForDoctorCredit = function(docShare)
		  {
			  console.log(docShare);
			  if(docShare.totalAdvancedDoctorConsumedAmt == '' || docShare.totalAdvancedDoctorConsumedAmt == null)
				  {
				    docShare.totalAdvancedDoctorConsumedAmt = 0;
				  }
			  if(docShare.totalAdvancedDoctorConsumedAmt <= docShare.totalAdvancedDoctorAmt){
			  if(parseFloat(docShare.totalAdvancedDoctorConsumedAmt) > parseFloat(docShare.totalAdvancedDoctorConsumedAmtOld))
				  {
				  	if($scope.doctorSharePaymentVoucherListFinal[0].paidAmount > 0)
				  		{
				  		  var diffAdv =  docShare.totalAdvancedDoctorConsumedAmt - docShare.totalAdvancedDoctorConsumedAmtOld;
				  			if(diffAdv >= $scope.doctorSharePaymentVoucherListFinal[0].paidAmount)
				  				{
				  				
				  				  $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt = parseFloat($scope.doctorSharePaymentVoucherListFinal[0].paidAmount) + parseFloat(docShare.totalAdvancedDoctorConsumedAmtOld);
				  				  $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmtOld= $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt;
				  			      $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = 0;
				  				}
				  			else{
				  				$scope.doctorSharePaymentVoucherListFinal[0].paidAmount = $scope.doctorSharePaymentVoucherListFinal[0].paidAmount - diffAdv;
				  				 $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmtOld= $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt;
				  			}
				  		
				  		}
				  	else
				  		{
					  		 growl.warning("Paid amount is zero(0).",{	
									title : WARNING_MSG
								});
							  $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt =docShare.totalAdvancedDoctorConsumedAmtOld;
							  return false;
				  		}
				  }
			  else if(docShare.totalAdvancedDoctorConsumedAmt < docShare.totalAdvancedDoctorConsumedAmtOld)
				  {
				     var diffAdv = docShare.totalAdvancedDoctorConsumedAmtOld  - docShare.totalAdvancedDoctorConsumedAmt;
				     $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = $scope.doctorSharePaymentVoucherListFinal[0].paidAmount + diffAdv;
				     $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt = docShare.totalAdvancedDoctorConsumedAmt;
				     $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmtOld = docShare.totalAdvancedDoctorConsumedAmt;
				     
				  }
			  }
			  else{
					  growl.warning("You can't add amount above "+docShare.totalAdvancedDoctorAmt,{	
							title : WARNING_MSG
						});
					  $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt =docShare.totalAdvancedDoctorConsumedAmtOld;
					  return false;
			  }
		  }
			/** code for setting all checkboxes */
			$scope.checkAllPREV = function() {
				if ($scope.selectedAll) {
					$scope.selectedAll = true;
					angular
					.forEach(
							$scope.doctorSharePaymentVoucherList,
							function(doctorShareProcessObj) {
								doctorShareProcessObj.doctorShare = $scope.selectedAll;
								$scope.checkPaidAmountCalculates(doctorShareProcessObj);
							});
					
				} else {
					$scope.selectedAll = false;
					$scope.getdoctorSharePaymentVoucherList();
				}
				
			}
			
			
			$scope.doctorSharePaymentVoucherListFinal = [];
			
			$scope.checkAll = function() {
				
				if ($scope.selectedAll) {
					$scope.doctorSharePaymentVoucherListFinal = [];
					$scope.selectedAll = true;
				
					var doc = $.grep($scope.doctorList, function (doc) {
			   	           return doc.doctorId== $scope.doctorId;
			   	       })[0];
					var temp = {
							doctorId:$scope.doctorId,
							doctorName:doc.doctorName,
							totalDoctorCredit:$scope.totalDoctorCredit,
							totalDoctorConsumedCredit : 0,
							totalAdvancedDoctorAmt:$scope.advancedDoctorAmount,
							totalAdvancedDoctorConsumedAmt : 0,
							totalAdvancedDoctorConsumedAmtOld : 0
							
					};
					
					var totalShareAmount = 0;
					var totalProcessAmount = 0;
					var totalDueShareAmount = 0;
					var totalRetainershipFee = 0;
					var tdsAmt = 0;
					var totalPaidAmount = 0;
					var i = 1;
					$scope.doctorSharePaymentVoucherList = $scope.pagination.data;
					angular.forEach($scope.doctorSharePaymentVoucherList,function(value,index) {
						        value.doctorShare = $scope.selectedAll;
								totalShareAmount = parseFloat(totalShareAmount) + parseFloat(value.totalAmount);
								totalProcessAmount = parseFloat(totalProcessAmount) + parseFloat(value.processedShareAmt);
								totalDueShareAmount = parseFloat(totalDueShareAmount) + parseFloat(value.dueShareAmt);
								totalRetainershipFee = parseFloat(totalRetainershipFee) + parseFloat(value.retainershipFee);
								
								var tmpTds = 0;
								tmpTds = parseFloat((parseFloat(value.paidAmount) * parseFloat($scope.doctorSlabTDS)/100));
								
								tdsAmt = parseFloat(tdsAmt) + parseFloat(tmpTds);
								totalPaidAmount = parseFloat(totalPaidAmount) + parseFloat(value.paidAmount);
								//$scope.checkPaidAmountCalculates(doctorShareProcessObj);
								if(i == $scope.doctorSharePaymentVoucherList.length)
									{
									 temp.totalAmount = totalShareAmount;
									 temp.processedShareAmt = totalProcessAmount;
									 temp.totalAmount = totalShareAmount;
									 temp.dueShareAmt = totalDueShareAmount;
									 temp.retainershipFee = totalRetainershipFee;
									 temp.retainershipFeeOld = totalRetainershipFee;
									 temp.tdsAmt = tdsAmt;
									 temp.tdsAmtOld = tdsAmt;
									 temp.paidAmount = totalPaidAmount - tdsAmt ;
									 $scope.doctorSharePaymentVoucherListFinal.push(temp);
									 
									 //Test
									 var param = {   
									  			doctorId:$scope.doctorId,
									  			orgUnitId:$scope.unitId
										    }
										 //console.log("param",JSON.stringify(param));
										$rootScope.startLoaderPopup();
										var URI = BASE_URL + ROOT_URL + "/api/docShare/cr/doctorSharePaymentVoucher/doctorCrDetails";
										BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
											if (response.data.status == "success")
												{
												$scope.doctorCreditDetails = [];
												 $scope.doctorCreditDetails = response.data.listObject;
												 
												  if( $scope.doctorCreditDetails.length > 0){
													  var totalDoctorCredit = 0;
													  var totalDoctorCreditOld = 0;
												  var t= 1;
												  angular.forEach(response.data.listObject,function(value,index){
													  totalDoctorCredit = parseFloat(totalDoctorCredit) + (isNaN(value.doctorCredit) ? 0 : parseFloat(value.doctorCredit));
													  totalDoctorCreditOld = parseFloat(totalDoctorCreditOld) + (isNaN(value.balanceCredit) ? 0 : parseFloat(value.balanceCredit));
													  if(t == $scope.doctorCreditDetails.length)
														  {
														     $scope.totalDoctorCredit = totalDoctorCredit;
														     $scope.totalDoctorCreditOld = totalDoctorCreditOld;
														     $scope.doctorSharePaymentVoucherListFinal[0].totalDoctorCredit = $scope.totalDoctorCredit;
														     var totalPaidAmount = $scope.doctorSharePaymentVoucherListFinal[0].paidAmount;
															 var totalConsumnedTemp = 0;
															 if($scope.totalDoctorCredit >=totalPaidAmount)
																{
																  $scope.totalDoctorCredit = parseFloat($scope.totalDoctorCredit) - (isNaN(totalPaidAmount) ? 0 : parseFloat(totalPaidAmount));
																  //$scope.doctorSharePaymentVoucherList[i].docCreditTemp = value.totalPaidAmount;
																 // var paidAmtTemp = totalPaidAmount;
																  var j  =1;
																  angular.forEach($scope.doctorCreditDetails,function(value1,index){
																	  if(value1.amountPaid != null && value1.amountPaid > 0 )
																		  {
																		  value1.amountPaid = value1.amountPaid;
																		  }else
																			  {
																			  value1.amountPaid = 0;
																			  }
																	 if(totalPaidAmount >= value1.availableCredit)
																		 {
																		 	
																		   totalPaidAmount = totalPaidAmount - value1.availableCredit;
																		 	value1.consumedCredit = value1.consumedCredit + value1.availableCredit;
																		 	value1.consumedCreditOld = value1.consumedCredit;
																		 	totalConsumnedTemp = totalConsumnedTemp + value1.consumedCredit;
																		 	value1.amountPaid = value1.amountPaid + value1.availableCredit;
																		 	value1.amountPaidOld =value1.amountPaid;
																		 	value1.availableCredit = parseFloat(0);
																		 }
																	 else{
																		    value1.availableCredit =value1.availableCredit - totalPaidAmount;
																		    value1.consumedCredit = value1.consumedCredit + totalPaidAmount;
																		    value1.consumedCreditOld = value1.consumedCredit;
																		    totalConsumnedTemp = totalConsumnedTemp + value1.consumedCredit;
																		    value1.amountPaid =  value1.amountPaid +totalPaidAmount;
																		    value1.amountPaidOld =value1.amountPaid;
																		    totalPaidAmount = totalPaidAmount - totalPaidAmount;
																	 }
																	 if(j == $scope.doctorCreditDetails.length)
																		 {
																		 $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = totalPaidAmount;
																		 $scope.doctorSharePaymentVoucherListFinal[0].totalDoctorConsumedCredit = totalConsumnedTemp;
																		 
																		 if($scope.advancedDoctorAmount >= totalPaidAmount)
																			 {
																			   $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = 0;
																			  // $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt = $scope.advancedDoctorAmount - totalPaidAmount;
																			   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt = totalPaidAmount;
																			   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmtOld = totalPaidAmount;
																			 }else{
																				 
																				   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt = $scope.advancedDoctorAmount;
																				   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmtOld = $scope.advancedDoctorAmount;
																				   $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = totalPaidAmount - $scope.advancedDoctorAmount;
																			 }
																		 
																		 }
																	 j++;
																  });
																  //value.paidAmount = paidAmtTemp;
																  
																  
																}else{
																	$scope.totalDoctorCredit = parseFloat(0);
																	// var paidAmtTemp = value.paidAmount;
																	 var k  =1;
																	 angular.forEach($scope.doctorCreditDetails,function(value1,index){
																		 if(value1.amountPaid != null && value1.amountPaid > 0 )
																		  {
																		  value1.amountPaid = value1.amountPaid;
																		  }else
																			  {
																			  value1.amountPaid = 0;
																			  }
																				 if(totalPaidAmount >= value1.availableCredit)
																				 {
																				 	
																					 totalPaidAmount = totalPaidAmount - value1.availableCredit;
																				 	value1.consumedCredit = value1.consumedCredit + value1.availableCredit;
																				 	value1.consumedCreditOld = value1.consumedCredit;
																				 	 totalConsumnedTemp = totalConsumnedTemp + value1.consumedCredit;
																					value1.amountPaid = value1.amountPaid + value1.availableCredit;
																					value1.amountPaidOld = value1.amountPaid;
																				 	value1.availableCredit = parseFloat(0);
																				 }
																			 else{
																				    value1.availableCredit =value1.availableCredit - totalPaidAmount;
																				    value1.consumedCredit = value1.consumedCredit + totalPaidAmount;
																				    value1.consumedCreditOld =value1.consumedCredit;
																				    totalConsumnedTemp = totalConsumnedTemp + value1.consumedCredit;
																					value1.amountPaid = value1.amountPaid + totalPaidAmount;
																					value1.amountPaidOld =value1.amountPaid;
																					totalPaidAmount = totalPaidAmount - totalPaidAmount;
																			 }
																				 if(k== $scope.doctorCreditDetails.length)
																				 {
																					 $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = totalPaidAmount;
																					 $scope.doctorSharePaymentVoucherListFinal[0].totalDoctorConsumedCredit = totalConsumnedTemp;
																					 
																					 if($scope.advancedDoctorAmount >= totalPaidAmount)
																					 {
																					   $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = 0;
																					  // $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt = $scope.advancedDoctorAmount - totalPaidAmount;
																					   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt = totalPaidAmount;
																					   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmtOld = totalPaidAmount;
																					 }else{
																						 
																						   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt = $scope.advancedDoctorAmount;
																						   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmtOld = $scope.advancedDoctorAmount;
																						   $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = totalPaidAmount - $scope.advancedDoctorAmount;
																					 }
																					
																				 }
																			 k++;
																	 });
																	 //value.paidAmount = paidAmtTemp;
																}
														  }
													  t++;
												  });
												  $rootScope.stopLoaderPopup();
												}
												  else{
													  if($scope.doctorSharePaymentVoucherListFinal[0].paidAmount > 0)
														  {
														  if($scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorAmt > 0){
														  if($scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorAmt >= $scope.doctorSharePaymentVoucherListFinal[0].paidAmount)
															 {
															 
															  $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt =$scope.doctorSharePaymentVoucherListFinal[0].paidAmount;
															   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmtOld = $scope.doctorSharePaymentVoucherListFinal[0].paidAmount ;
															   $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = 0;
															  
															 }else{
																 $scope.doctorSharePaymentVoucherListFinal[0].paidAmount = $scope.doctorSharePaymentVoucherListFinal[0].paidAmount - $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorAmt;
																 $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt =$scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorAmt;
																   $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmtOld = $scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorAmt;
															 }
														  }
												  }
													  $rootScope.stopLoaderPopup();
												  }
												}
										});
									 //Test end
									/* var totalPaidAmount = $scope.doctorSharePaymentVoucherListFinal[0].dueShareAmt;
									 var totalConsumnedTemp = 0;
									 //For Credit List Loop
									 if($scope.totalDoctorCredit >=totalPaidAmount)
										{
										  $scope.totalDoctorCredit = parseFloat($scope.totalDoctorCredit) - (isNaN(totalPaidAmount) ? 0 : parseFloat(totalPaidAmount));
										  //$scope.doctorSharePaymentVoucherList[i].docCreditTemp = totalPaidAmount;
										 // var paidAmtTemp = totalPaidAmount;
										  var j  =1;
										  angular.forEach($scope.doctorCreditDetails,function(value1,index){
											  if(value1.amountPaid != null && value1.amountPaid > 0 )
												  {
												  value1.amountPaid = value1.amountPaid;
												  }else
													  {
													  value1.amountPaid = 0;
													  }
											 if(totalPaidAmount >= value1.availableCredit)
												 {
												 	
												   totalPaidAmount = totalPaidAmount - value1.availableCredit;
												 	value1.consumedCredit = value1.consumedCredit + value1.availableCredit;
												 	value1.consumedCreditOld = value1.consumedCredit;
												 	totalConsumnedTemp = totalConsumnedTemp + value1.consumedCredit;
												 	value1.amountPaid = value1.amountPaid + value1.availableCredit;
												 	value1.amountPaidOld =value1.amountPaid;
												 	value1.availableCredit = parseFloat(0);
												 }
											 else{
												    value1.availableCredit =value1.availableCredit - totalPaidAmount;
												    value1.consumedCredit = value1.consumedCredit + totalPaidAmount;
												    value1.consumedCreditOld = value1.consumedCredit;
												    totalConsumnedTemp = totalConsumnedTemp + value1.consumedCredit;
												    value1.amountPaid =  value1.amountPaid +totalPaidAmount;
												    value1.amountPaidOld =value1.amountPaid;
												    totalPaidAmount = totalPaidAmount - totalPaidAmount;
											 }
											 if(j == $scope.doctorCreditDetails.length)
												 {
												 temp.paidAmount = totalPaidAmount;
												 temp.totalDoctorConsumedCredit = totalConsumnedTemp;
												 }
											 j++;
										  });
										  //value.paidAmount = paidAmtTemp;
										  
										  
										}else{
											$scope.totalDoctorCredit = parseFloat(0);
											// var paidAmtTemp = value.paidAmount;
											 var k  =1;
											 angular.forEach($scope.doctorCreditDetails,function(value1,index){
												 if(value1.amountPaid != null && value1.amountPaid > 0 )
												  {
												  value1.amountPaid = value1.amountPaid;
												  }else
													  {
													  value1.amountPaid = 0;
													  }
														 if(totalPaidAmount >= value1.availableCredit)
														 {
														 	
															 totalPaidAmount = totalPaidAmount - value1.availableCredit;
														 	value1.consumedCredit = value1.consumedCredit + value1.availableCredit;
														 	value1.consumedCreditOld = value1.consumedCredit;
														 	 totalConsumnedTemp = totalConsumnedTemp + value1.consumedCredit;
															value1.amountPaid = value1.amountPaid + value1.availableCredit;
															value1.amountPaidOld = value1.amountPaid;
														 	value1.availableCredit = parseFloat(0);
														 }
													 else{
														    value1.availableCredit =value1.availableCredit - totalPaidAmount;
														    value1.consumedCredit = value1.consumedCredit + totalPaidAmount;
														    value1.consumedCreditOld =value1.consumedCredit;
														    totalConsumnedTemp = totalConsumnedTemp + value1.consumedCredit;
															value1.amountPaid = value1.amountPaid + totalPaidAmount;
															value1.amountPaidOld =value1.amountPaid;
															totalPaidAmount = totalPaidAmount - totalPaidAmount;
													 }
														 if(k== $scope.doctorCreditDetails.length)
														 {
														 temp.paidAmount = totalPaidAmount;
														 temp.totalDoctorConsumedCredit = totalConsumnedTemp;
														 }
													 k++;
											 });
											 //value.paidAmount = paidAmtTemp;
										}*/
									 
									 
									 
									}
								i++;
							});
					
				} else {
					$scope.selectedAll = false;
					$scope.getdoctorSharePaymentVoucherList();
					$scope.doctorSharePaymentVoucherListFinal = [];
				}
				
			}
			
			/*First Level*/
	    	
			
			$scope.setFirstTabLevel= function(newTab)
			{
				$scope.firstTabLevel = newTab;
			};
			
			$scope.isSetFirstTabLevel = function(tabNum)
			{
				return $scope.firstTabLevel === tabNum;
			};
			
		  
		  $scope.saveDoctorSharePaymentVoucher = function(){
			  $scope.listTDocSharePaymentVoucherLoggerDto=[];
			  angular
				.forEach(
						$scope.doctorSharePaymentVoucherList,
						function(value, key) {
							if (value.doctorShare == true) {
								var tDocVoucherLoggerDto = angular.copy($scope.tDocSharePaymentVoucherLoggerDto);
								tDocVoucherLoggerDto.orgUnitId=$scope.unitId;
								tDocVoucherLoggerDto.voucherLoggerId=value.docShareVoucherId;
								tDocVoucherLoggerDto.orgId=$scope.orgId; 
								tDocVoucherLoggerDto.status="A";
								tDocVoucherLoggerDto.createdDate=value.createdDate;
								tDocVoucherLoggerDto.createdBy= $scope.userId;
								tDocVoucherLoggerDto.processedShareAmt=value.processedShareAmt;
								//tDocVoucherLoggerDto.retainerShipAmt=value.retainershipFee;
								tDocVoucherLoggerDto.paidAmt=value.paidAmount; 
								tDocVoucherLoggerDto.voucherNo=value.voucherNo; 
								$scope.listTDocSharePaymentVoucherLoggerDto.push(tDocVoucherLoggerDto);
							}
						});
			 
				var param = {   
						 		listTDocSharePaymentVoucherLoggerDto:$scope.listTDocSharePaymentVoucherLoggerDto,
						 		doctorShareCrNoteDetailsDtosList:$scope.doctorCreditDetails,
								orgId:$scope.orgId,
								unitId:$scope.unitId,
								retainerShipAmt:$scope.doctorSharePaymentVoucherListFinal[0].retainershipFee,
								tdsAmt:$scope.doctorSharePaymentVoucherListFinal[0].tdsAmt,
								consumedDocAdvance:$scope.doctorSharePaymentVoucherListFinal[0].totalAdvancedDoctorConsumedAmt,
								consumedDocCredit:$scope.doctorSharePaymentVoucherListFinal[0].totalDoctorConsumedCredit,
								doctorAdvanceId : $scope.doctorAdvanceId,
								paidAmt : $scope.doctorSharePaymentVoucherListFinal[0].paidAmount,
								processedAmt : $scope.doctorSharePaymentVoucherListFinal[0].processedShareAmt,
								doctorId : $scope.doctorId
						    }
				
				//doctorCreditDetails
				//doctorSharePaymentVoucherListFinal
				
				 console.log("param",JSON.stringify(param));
				// return false;
				 $rootScope.startLoader();
				 var URI = BASE_URL + ROOT_URL + "/api/billing/saveDoctorSharePaymentVoucherList";
				 BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
					if (response.data.status == "success")
						$scope.init();
					$scope.doctorSharePaymentVoucherListFinal = [];
					$scope.selectedAll = false;
					$scope.getdoctorSharePaymentVoucherList();
					$rootScope.stopLoader();
				});
			}
			
	}catch(e){
		console.log(e.message);
	}
	
}]);