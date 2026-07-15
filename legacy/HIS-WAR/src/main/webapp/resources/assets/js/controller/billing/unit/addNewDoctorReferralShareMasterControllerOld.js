angular.module("myApp").controller("addNewDoctorReferralShareMasterController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','servicesFactory',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,growl,servicesFactory){
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
		$scope.docReferralId=1;
		$scope.init = function(){
			$scope.addNewDoctorReferralShareList=[];
			$scope.getVisitTypeList();
			 $scope.getReferralMasterList();
		}
		
		
		// for datePicker
		$scope.dateOptions = {
			formatYear : 'yyyy',
			showWeeks : false
		};
		$scope.openDatePicker = function() {
			$scope.datepicker.opened = true;
		};

		$scope.datepicker = {
			opened : false
		};

		
		   $scope.firstTabLevel = 1 ;
			
			$scope.setFirstTabLevel= function(newTab)
			{
				
				$scope.docReferralId=newTab;
				if(newTab==$rootScope.DoctorReferralShareMasterRevenueWise){
					$scope.firstTabLevel = newTab;
				}
				
			  };
			$scope.isSetFirstTabLevel = function(tabNum)
			{
			
				return $scope.firstTabLevel === tabNum;
			};
		
		  $scope.getVisitTypeList = function(searchPackageObj) {
			  var param = {organizationId:$scope.orgId,unitId:$scope.unitId};
			    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getVisitTypeList"
			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						 $scope.visityTypeList = response.data.listObject;
						  console.log("visityTypeList",JSON.stringify($scope.visityTypeList));
					}
				});
			  }
		  
		  $scope.getReferralMasterList = function(referralTypeId){
				var URI = BASE_URL + ROOT_URL + "/adt/getReferralListByReferralTypeId";
				var obj=
				      { 
						 organizationId:$scope.orgId,
						 referralTypeId:1
				      };
					BillingGenericService.serviceAction(METHOD_POST, URI, obj).then(function(response) {
							$scope.referralMasterList = [];
							if (response.data.status == "success") {
								$scope.referralMasterList = response.data.listObject;
							}
					});
				};
			//$scope.getVisitTypeList();
			$scope.init();
			
			 $scope.visitTypeName = function(id){
					for(var i = 0; i < $scope.visityTypeList.length ; i++){
						if($scope.visityTypeList[i].id == id)
						return $scope.visityTypeList[i].name;
					}
			   }
			 
			 $scope.getReferralDocName = function(id){
					for(var i = 0; i < $scope.referralMasterList.length ; i++){
						if($scope.referralMasterList[i].referralId == id)
						return $scope.referralMasterList[i].desc;
					}
			   }
			 $scope.doctorRefShare={
					 referralDocId:"",
					 visitTypeId:"",
					 duration:"",
					 effectiveDate:""
			 }
			
			 $scope.addDocRefShareDetails = function(){				  
						  
				/*	if($scope.speciality.specialityId == '' || typeof($scope.speciality.specialityId) == "undefined"){
						growl.error("Please select a group",{
							title : ERROR_MSG
						});
						return;
					}*/
				 if($scope.doctorRefShare.referralDocId == '' || typeof($scope.doctorRefShare.referralDocId) == "undefined"){
						growl.error("Please select referred doctor",{
							title : ERROR_MSG
						});
						return;
					}else if($scope.doctorRefShare.visitTypeId == '' || typeof($scope.doctorRefShare.visitTypeId) == "undefined"){
						growl.error("Please select visit type",{
							title : ERROR_MSG
						});
						return;
					}else if($scope.doctorRefShare.duration == '' || typeof($scope.doctorRefShare.duration) == "undefined"){
						growl.error("Please select duration",{
							title : ERROR_MSG
						});
						return;
					}else if($scope.doctorRefShare.effectiveDate == '' || typeof($scope.doctorRefShare.effectiveDate) == "undefined"){
						growl.error("Please select effective date",{
							title : ERROR_MSG
						});
						return;
					}
				
						var tempObj={referralDocId:"",referralDocName:"",visitTypeId:"" ,docReferralId:"",visitTypeName:"",duration:"",noOfCases:"",revenue:"",
								share:"",effectiveDate:""};
						
						var newTempObj = angular.copy(tempObj);
						newTempObj.referralDocId=$scope.doctorRefShare.referralDocId;
						newTempObj.referralDocName=$scope.getReferralDocName($scope.doctorRefShare.referralDocId);
						newTempObj.visitTypeId=$scope.doctorRefShare.visitTypeId;
						newTempObj.visitTypeName=$scope.visitTypeName($scope.doctorRefShare.visitTypeId);
						newTempObj.duration=$scope.doctorRefShare.duration;
						newTempObj.noOfCases=$scope.doctorRefShare.noOfCases;
						newTempObj.share=$scope.doctorRefShare.share;
						newTempObj.effectiveDate=new Date($scope.doctorRefShare.effectiveDate).getTime();
						newTempObj.noOfRevenue=$scope.doctorRefShare.noOfRevenue;
						newTempObj.revenue=$scope.doctorRefShare.revenue;
						
						$scope.addNewDoctorReferralShareList.push(newTempObj);
						console.log("length=",$scope.addNewDoctorReferralShareList.length);
					console.log("addNewDoctorReferralShareList",JSON.stringify($scope.addNewDoctorReferralShareList));
					//checking list contain the obj or not , if len >0 then we are hiding the add button 
					if($scope.addNewDoctorReferralShareList.length>0){
						$("#adddBtn").removeClass("enabled");
						$("#adddBtn").addClass("disabled");						
						
					}else{
						$("#adddBtn").removeClass("disabled");
						$("#adddBtn").addClass("enabled");
					}
					
				}
			  $scope.removeDoctorRefShareList = function(index,obj){
					 $scope.addNewDoctorReferralShareList.splice(index,1);
					//checking list contain the obj or not , if len ==0 then we are showing the add button 
					 if($scope.addNewDoctorReferralShareList.length==0){
						 $("#adddBtn").removeClass("disabled");
						 $("#adddBtn").addClass("enabled");										
							
						}else{
							 $("#adddBtn").removeClass("enabled");
							 $("#adddBtn").addClass("disabled");	
						}
				
				}
			  $scope.addDoctorList=function(dtls){
				  var tempObj={referralDocId:dtls.referralDocId,referralDocName:dtls.referralDocName,visitTypeId:dtls.visitTypeId ,docReferralId:dtls.docReferralId,visitTypeName:dtls.visitTypeName,duration:"",noOfCases:"",revenue:"",
							share:"",effectiveDate:""};
					
					var newTempObj = angular.copy(tempObj);
					newTempObj.referralDocId=$scope.doctorRefShare.referralDocId;
					newTempObj.referralDocName=$scope.getReferralDocName($scope.doctorRefShare.referralDocId);
					newTempObj.visitTypeId=$scope.doctorRefShare.visitTypeId;
					newTempObj.visitTypeName=$scope.visitTypeName($scope.doctorRefShare.visitTypeId);
					newTempObj.duration=$scope.doctorRefShare.duration;
					newTempObj.noOfCases=$scope.doctorRefShare.noOfCases;
					newTempObj.share=$scope.doctorRefShare.share;
					newTempObj.effectiveDate=new Date($scope.doctorRefShare.effectiveDate).getTime();
					newTempObj.noOfRevenue=$scope.doctorRefShare.noOfRevenue;
					newTempObj.revenue=$scope.doctorRefShare.revenue;
					
					$scope.addNewDoctorReferralShareList.push(newTempObj);
			  }
			  
			  $scope.saveDoctorReferralShareDetails = function(){
					
					
					//Doctor Referral Share Service  details
					var listMDoctorReferralShareDto = [];
					angular.forEach($scope.addNewDoctorReferralShareList,function(value,index){
						var temp = {
								referraledDocId:value.referralDocId,
								referralShareId:value.referralShareId,
								docReferralId:$scope.docReferralId,
								orgUnitId :value.orgUnitId,
								orgId : value.orgId,
								noOfCases:value.noOfCases,
								revenue : value.revenue,
								share : value.share,
								effectiveDate : new Date(value.effectiveDate).getTime(),
//								visitTypeId:value.visitTypeId,
//								duration : value.duration,
								createdBy:$scope.userId,
								updatedBy:$scope.userId
								
						};
						listMDoctorReferralShareDto.push(temp);
					});
				
					var param = {
							listMDoctorReferralShareDto:listMDoctorReferralShareDto	,
							visitTypeId:$scope.doctorRefShare.visitTypeId,
							duration:$scope.doctorRefShare.duration,
							referraledDocId:$scope.doctorRefShare.referralDocId,
							docReferralId:$scope.docReferralId,
							orgId:$scope.orgId,
							unitId:$scope.unitId,
							createdBy:$scope.userId,
							updatedBy:$scope.userId,
							effectiveDate: moment($scope.doctorRefShare.effectiveDate).format('DD-MM-YYYY HH:mm:ss')
							}

					 //console.log("param",JSON.stringify(param));
					// console.log("listMDoctorReferralShareDto",JSON.stringify(listMDoctorReferralShareDto));
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/unit/DoctorShare/saveDoctorReferralShare";
					BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
						if (response.data.status == "success")
							$scope.doctorRefShare={};
							$scope.init();
							
								 $("#adddBtn").removeClass("disabled");
								 $("#adddBtn").addClass("enabled");										
						$rootScope.stopLoader();
					});
				}
		
		  
	}catch(e){
		console.log(e.message);
	}
	
	
}]);


