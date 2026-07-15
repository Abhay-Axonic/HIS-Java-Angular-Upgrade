/**
 * 
 */

angular.module('myApp')
		.controller("QMSTokenDisplayMasterController",
		['$scope','$rootScope','$state','$cookies','GenericServiceForWithoutLogin','PagerService','promiseFactory','$sessionStorage','growl','$interval','Pubnub','$cookieStore','$location','orgId','unitId','cookieskioskId','userId',
		function($scope,$rootScope,$state, $cookies, GenericServiceForWithoutLogin,PagerService,promiseFactory,$sessionStorage,growl,$interval,Pubnub,$cookieStore,$location,orgId,unitId,cookieskioskId,userId){
				
	
			/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			$scope.commonListCount;
			$scope.initQMSMaster = function() {
				
				
				$scope.orgUnitId = unitId;
				$scope.orgId = orgId;
				$scope.userId = userId;

				/** Variables for pagination */
				$scope.offset = 0;
				$scope.noOfRecordsPerPage = 10;
				$scope.totalNoOfRecords;
				$scope.searchKeyword;
				$scope.commonListCount;

				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;

				// for popup
				$scope.showModal = false;
				$scope.buttonClicked = "";
				$scope.popUpFlag = true;
				// end for popup
				$scope.kioskName="";
				$scope.tokenNumber="";
				$scope.common = {
						qmsKioskMasterId:"",
						qmsDeptName:"",
						createdBy:$scope.userId,
						updatedBy:$scope.userId,
						organisationId:$scope.orgId,
						unitId:$scope.orgUnitId					
					};
				
				$scope.deptList = [];
				$scope.unitBillingAppConfig = {};
				var URI = BASE_URL + ROOT_URL
				+ "/api/unit/applicationConfiguration/billing/orgId/"+$scope.orgId+"/unitId/"+$scope.orgUnitId;
				GenericServiceForWithoutLogin.serviceAction(METHOD_GET, URI,"",
						NOTIFICATION_MSG_STATUS_FALSE)
				.then(function(response) {
							console.log(response);
							if (response.data.status == "success")
								{
									$scope.unitBillingAppConfig = response.data.object;
								}
								
						});
				
				
			}
			$scope.initQMSMaster();
			
		
            $scope.cookieskioskId=cookieskioskId;
          
		
			
			$scope.confirmQMS=function(){
				if($scope.cookieskioskId!=undefined){	
					var data = {
								organisationId:$scope.orgId,
								unitId:$scope.orgUnitId	,
								qmsKioskMasterId:$scope.cookieskioskId
						    };
			
					var URI = BASE_URL + ROOT_URL + "/api/pharmacy/getActiveDeptListByKioskId";
					GenericServiceForWithoutLogin.serviceAction("POST", URI, data).then(function(response) {
						$rootScope.stopLoader();
						$scope.activeQMSDeptList = response.data.listObject;
						
						angular.forEach($scope.activeQMSDeptList,function(value,key){
								$scope.deptList.push(value.qmsDeptMasterId);
						})
						
						$scope.kioskName=response.data.listObject[0].qmsDesc;
					});
				
				}				
			}

			       $scope.firstImg;
                   $scope.imageBackground = function(){
                                   var URI = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/screen-images/"+$scope.orgId+"/3"
                                       GenericServiceForWithoutLogin.serviceAction("GET", URI).then(function(response) {
                                    	   if(response.data.object != null){
                                       $scope.firstImg=response.data.object.imageURL;
                                       console.log($scope.firstImg);
                                    	   }
                                       });
                                   }
                        $scope.imageBackground();
			
			$scope.confirmQMS();
			//get token list for display
			$scope.deptNumberFirstD = null;
			$scope.deptNumberSecondD = null;
			$scope.deptNumberThirdD = null;
			$scope.deptNumberFourD = null;
			
			$scope.deptNumberFirstListD = [];
			$scope.deptNumberSecondListD = [];
			$scope.deptNumberThirdListD = [];
			$scope.deptNumberFourListD = [];
			$scope.callCounter=undefined;
			$scope.callCounterToDisplay=[];
			$scope.getTokenList = function() {	
				if( $scope.cookieskioskId!=undefined){	
					var data = {
								organisationId:$scope.orgId,
								unitId:$scope.orgUnitId,
								qmsKioskMasterId: $scope.cookieskioskId
							};
					var URI = BASE_URL + ROOT_URL + "/api/pharmacy/getTokenListForDisplay";
					GenericServiceForWithoutLogin.serviceAction("POST", URI, data).then(function(response) {
						if(response.data.status=="success"){
							$scope.tokenDisplayList=[];
							
							$scope.deptNumberFirst = null;
							$scope.deptNumberSecond = null;
							$scope.deptNumberThird = null;
							$scope.deptNumberFour = null;
							
							$scope.deptNumberFirstList = [];
							$scope.deptNumberSecondList = [];
							$scope.deptNumberThirdList = [];
							$scope.deptNumberFourList = [];
							if(response.data.listObject != null){
								angular.forEach(response.data.listObject,function(value,key){
									if(value.isCall == null || value.isCall !='A')
										$scope.tokenDisplayList.push(value);
								})
								
								angular.forEach(response.data.listObject,function(value,key){
									if(value.isCall != null && value.isCall =='A')
										$scope.tokenDisplayList.push(value);
									if(value.isCall != null && value.isCall =='A')
										{
										if (!$scope.callCounterToDisplay.filter(e => e.tokenNumberWithStartLetter === value.tokenNumberWithStartLetter).length > 0) {
											$scope.speech(' Token number, '+value.tokenNumberWithStartLetter+', please visit '+value.qmsCounterDesc+' counter, thank you .');
											$scope.callCounterToDisplay.push(value);
										}
										}
									if(value.isCall !='A')
									{
								if ($scope.callCounterToDisplay.filter(e => e.tokenNumberWithStartLetter === value.tokenNumberWithStartLetter).length > 0) {
                                    var index = $scope.callCounterToDisplay.findIndex(x => x.tokenNumberWithStartLetter ===value.tokenNumberWithStartLetter);
									$scope.callCounterToDisplay.splice(index,1);
								}
									}
								})
								
								var index = 0;
								
								angular.forEach($scope.deptList,function(value,key){
									
									angular.forEach($scope.tokenDisplayList,function(value1,key1){
										
										if(value==value1.assignedDeptId ){
											
											if(index == 0){
												$scope.deptNumberFirst = value;
												if(value1.isCall == null || value1.isCall == 'N')
													{
													$scope.deptNumberFirstList.push(value1);
													}
											}
											if(index == 1){
												$scope.deptNumberSecond = value;
												if(value1.isCall == null || value1.isCall == 'N')
												{
													$scope.deptNumberSecondList.push(value1);
												}
											}
											if(index == 2){
												$scope.deptNumberThird = value;
												if(value1.isCall == null || value1.isCall == 'N')
												{
													$scope.deptNumberThirdList.push(value1);
												}
											}
											if(index == 3){
												$scope.deptNumberFour = value;
												if(value1.isCall == null || value1.isCall == 'N')
												{
													$scope.deptNumberFourList.push(value1);
												}
											}
											
										}
										
									})
									index = index +1;
								})
								
								
								$scope.deptNumberFirstD = $scope.deptNumberFirst;
								$scope.deptNumberSecondD = $scope.deptNumberSecond;
								$scope.deptNumberThirdD = $scope.deptNumberThird;
								$scope.deptNumberFourD = $scope.deptNumberFour;
								
								$scope.deptNumberFirstListD = $scope.deptNumberFirstList;
								$scope.deptNumberSecondListD = $scope.deptNumberSecondList;
								$scope.deptNumberThirdListD = $scope.deptNumberThirdList;
								$scope.deptNumberFourListD = $scope.deptNumberFourList;
								
								
							}
						}
						
					});
				}
			}


			
			$scope.speech = function (theText) {
			      window.speechSynthesis.speak(new SpeechSynthesisUtterance(theText));
			  };
			//get token list for display
			$scope.getTokenMsg = function() {	
				if( $scope.cookieskioskId!=undefined){	
					var data = {
								organisationId:$scope.orgId,
								unitId:$scope.orgUnitId,
								qmsKioskMasterId: $scope.cookieskioskId
							};
					var URI = BASE_URL + ROOT_URL + "/api/pharmacy/getTokenDisplayMessage";
					GenericServiceForWithoutLogin.serviceAction("POST", URI, data).then(function(response) {
						if(response.data.status=="success"){
							if(response.data.listObject.length > 0){
								$scope.tokenObject = response.data.listObject[0];
								console.log("tokenList",$scope.tokenObject);
								$scope.speech($scope.tokenObject.qmsCallMassage);
								}
						}
						
					});
				}
			}
			$scope.startTokenList=	$interval(function() {
				
				$scope.getTokenList();
				
	          }, 2000);

			$scope.startTokenMessageList=	$interval(function() {
				
				$scope.getTokenMsg();
				
	          }, 2000);
			
}]);