/**
 * 
 */

angular.module('myApp')
		.controller("QMSCallMasterController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl','Pubnub','$cookieStore','$interval','$window','$timeout',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,Pubnub,$cookieStore,$interval,$window,$timeout){
				
	
			/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			$scope.commonListCount;
			
			$scope.initQMSCallMaster = function() {
				
				var cookieObject = $cookies.getObject('cookieObject');
				if(cookieObject == undefined){
					$state.go('login');
					return;
				}
				
				$scope.orgUnitId = cookieObject.unitId;
				$scope.orgId = cookieObject.organizationId;
				$scope.userId = cookieObject.userId;

				$scope.hospitalName = cookieObject.unitNme;
				
//				console.log("name date time",$scope.hospitalName+","+$scope.currentDate+","+$scope.currentTime);
				/** Variables for pagination */
				$scope.offset = 0;
				$scope.noOfRecordsPerPage = 10;
				$scope.totalNoOfRecords;
				$scope.searchKeyword;
				$scope.commonListCount;

				$rootScope.loginpage = true;
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;

				// for popup
				$scope.showModal = false;
				$scope.popUpFlag = true;
				// end for popup
				$scope.common = {
						qmsDeptMasterId:""	,
						qmsCounterMasterId:"",
						qmsKioskMasterId:"",
						qmsTokenMasterId:"",
						qmsTransferDeptMasterId:"",
						previousQMSTokenMasterId:"",
							
					};
				
			}
			$scope.SetCookies = function () {
                $cookies.put("kioskId", $scope.common.qmsKioskMasterId);
            };
            $scope.getCookie = function(key) {
                return $cookieStore.get(key);
            };
            $scope.cookieskioskId=$scope.getCookie("kioskId");
            console.log("cookies Id",$scope.cookieskioskId);
            
			$scope.initQMSCallMaster();
			//for getting kiosk list
			$scope.getQMSMasterList = function() {				
				var data = {
							organisationId:$scope.orgId,
							unitId:$scope.orgUnitId						
						};
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + "/api/pharmacy/getActiveQMSMasterList";
				GenericService.serviceAction("POST", URI, data).then(function(response) {
					if($scope.cookieskioskId==undefined){
						angular.element('#qmsKioskDetailsModal').modal('show');
					}else{
						$scope.common.qmsKioskMasterId=$scope.cookieskioskId;
						$scope.confirmQMS();
					}
					
					$scope.activeQMSList = response.data.listObject;
					$rootScope.stopLoader();
				});
			}
			$scope.getQMSMasterList();					
			$scope.selectQMSKiosk=function(kioskId){
				console.log("kioskId",kioskId);
				$scope.SetCookies();
			}
			
			//if kiosk Id get from cook
			//after selecting kiosk , we getting the dept list
			   
			$scope.confirmQMS=function(){
				if($scope.common.qmsKioskMasterId!="" && $scope.common.qmsKioskMasterId!=undefined ){					
				
						$scope.activeQMSDeptList=[];
						$scope.callTokenNumber="";
						$scope.commonObj="";
						$scope.indexForNextCall="";
						console.log("kiosk Id",$scope.common.qmsKioskMasterId);
						var data = {
								organisationId:$scope.orgId,
								unitId:$scope.orgUnitId	,
								qmsKioskMasterId:$scope.common.qmsKioskMasterId
							};
						$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL + "/api/pharmacy/getActiveDeptListByKioskId";
					GenericService.serviceAction("POST", URI, data).then(function(response) {
							$scope.activeQMSDeptList = response.data.listObject;
							$scope.kioskName=response.data.listObject[0].qmsDesc;
							angular.element('#qmsCallModal').modal('show');
							$rootScope.stopLoader();
						
					});
				}else{
					 growl.warning(" Please select kiosk..!!",{title : "WARNING!"});
					   return false;
				}
				
			}			
				// by selecting dept list , we r fetching counter list of the dept 
			$scope.isReception="";
			 $scope.isVoice="";
			$scope.getCounterListByDeptId=function(deptId){
				$scope.counterList=[];
				var data = {
						organisationId:$scope.orgId,
						unitId:$scope.orgUnitId,
						qmsDeptMasterId:deptId
					};
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + "/api/pharmacy/getQMSCounterListByDeptId";
				GenericService.serviceAction("POST", URI, data).then(function(response) {
					$scope.counterList = response.data.listObject;	
					$rootScope.stopLoader();					
					console.log("counter list",$scope.counterList);
				});
			}
			
			$scope.getTokenList=function(){
				//get token number list
				if($scope.selectedDeptId!=undefined && $scope.selectedDeptId!=""){
						var data = {
								organisationId:$scope.orgId,
								unitId:$scope.orgUnitId,
								qmsKioskMasterId:$scope.common.qmsKioskMasterId,
								qmsDeptMasterId:$scope.selectedDeptId,
							};
						
						var URI = BASE_URL + ROOT_URL + "/api/pharmacy/getTokenList";
						GenericService.serviceAction("POST", URI, data).then(function(response) {
							$scope.tokenList = response.data.listObject;	
							
							console.log("tokenList",$scope.tokenList);
						});
				}
			}
			
			// after selecting dept and counter , fetching token list for displaying
			$scope.selectedDeptId="";
			$scope.selectedCounterId="";
			$scope.saveDeptCounterDetails=function(){
				if($scope.common.qmsDeptMasterId=="" || $scope.common.qmsDeptMasterId==null || $scope.common.qmsDeptMasterId==undefined){
                    growl.warning(" Please select department..!!",{title : "WARNING!"});
					   return false;
				}
				if($scope.common.qmsCounterMasterId=="" || $scope.common.qmsCounterMasterId==undefined || $scope.common.qmsCounterMasterId==null){
					 growl.warning(" Please select counter..!!",{title : "WARNING!"});
					   return false;
				}
//				getting dept and counter name
				if($scope.common.qmsCounterMasterId!=null && $scope.common.qmsCounterMasterId!=undefined){
					$scope.counterName = $.grep($scope.counterList, function (cntr) {
						if(cntr.qmsCounterMasterId == $scope.common.qmsCounterMasterId){
							$scope.selectedCounterId=$scope.common.qmsCounterMasterId;
						}
		                return cntr.qmsCounterMasterId == $scope.common.qmsCounterMasterId;
		                
		            })[0].qmsCounterDesc;
					console.log("$scope.Counter Name & ID",$scope.counterName+"  "+$scope.selectedCounterId);
				}
				if($scope.common.qmsDeptMasterId!=null && $scope.common.qmsDeptMasterId!=undefined){
					$scope.deptName = $.grep($scope.activeQMSDeptList, function (dept) {
						if(dept.qmsDeptMasterId == $scope.common.qmsDeptMasterId){
							 $scope.isReception=dept.isReception;
				              $scope.isVoice=dept.isVoice;
							$scope.selectedDeptId=$scope.common.qmsDeptMasterId;
						}
		                return dept.qmsDeptMasterId == $scope.common.qmsDeptMasterId;
		            })[0].qmsDeptName;
					console.log("$scope.Dept name & ID",$scope.deptName+"  "+$scope.selectedDeptId);
					console.log("isReception", $scope.isReception);
					console.log("isVoice", $scope.isVoice);
				}
				$scope.callTokenNumber="";
				$scope.commonObj="";
				$scope.indexForNextCall="";
				$scope.getTokenList();
				
				
				//disabled served call button
				$("#servedId").removeClass("enabled");
				$("#servedId").addClass("disabled");
				
				//disabled the transfer dept drop down
				$("#transferDeptId").removeClass("enabled");
				$("#transferDeptId").addClass("disabled");
				
				// disabled show call button
				$("#noShowId").removeClass("enabled");
				$("#noShowId").addClass("disabled");
				
				// disabled CANCEL END button
				$("#cancelEndId").removeClass("enabled");
				$("#cancelEndId").addClass("disabled");
				//for disabled the recall button
				$("#recallId").removeClass("enabled");
				$("#recallId").addClass("disabled");
				
				//disabled served call button
				$("#callNextId").removeClass("disabled");
				$("#callNextId").addClass("enabled");
				
				// hide counter and department model 
				$("#qmsCallModal").modal("hide");
				
				
			}
			//saving call time in DB
			$scope.saveCallTokenTime=function(logStatusId){
				  var data={
							  qmsTokenMasterId:$scope.commonObj.qmsTokenMasterId,
							  qmsCounterMasterId:$scope.selectedCounterId,
							  qmsKioskMasterId:$scope.common.qmsKioskMasterId,
							  qmsDeptMasterId:$scope.common.qmsDeptMasterId,
							  qmsTokenCallLogId:$scope.commonObj.qmsTokenCallLogId,
							  organisationId:$scope.orgId,
							  unitId:$scope.orgUnitId,
							  isCall:'C', //s =served
							  callLogStatus:logStatusId,
							  qmsCallMassage:$scope.speechMsg
							  
				  			}
				  $rootScope.startLoader();
				  var URI = BASE_URL + ROOT_URL + "/api/pharmacy/updateToken";
					GenericService.serviceAction("POST", URI, data).then(function(response) {
						if (response.data.status == "success") {	
							$scope.getTokenList();
							$scope.speechMsg="";
						}
						$rootScope.stopLoader();
					});
			  }
			//call for token number to be served
		
			$scope.call=function(obj,indx,flag){	
			
				
				
				console.log("Object Details",obj);
				var data={
						qmsKioskMasterId:$scope.common.qmsKioskMasterId,					
						qmsDeptMasterId:$scope.selectedDeptId,
						qmsCounterMasterId:$scope.selectedCounterId,
						organisationId:$scope.unitId,
						unitId:$scope.orgUnitId
					}
				$rootScope.startLoader();
			 var URI = BASE_URL + ROOT_URL + "/api/pharmacy/releaseTokenNumber";
				GenericService.serviceAction("POST", URI, data).then(function(response) {
					if (response.data.status == "success") {
						console.log("updated log status with zero");
						
				 var URI = BASE_URL + ROOT_URL + "/api/pharmacy/checkingIsCalledOrNot";
					GenericService.serviceAction("POST", URI, obj).then(function(response) {
						if (response.data.status == "success") {	
							$scope.checkList = response.data.listObject[0];	
							console.log("$scope.checkList",$scope.checkList);
							
							//new code start
							if($scope.checkList.qmsCounterMasterId == null || $scope.selectedCounterId ==$scope.checkList.qmsCounterMasterId)
								{
									if(($scope.checkList.callLogStatus != 2 && $scope.checkList.callLogStatus != 3) || flag == true )
										{
										
										//enabled served call button
										$("#servedId").removeClass("disabled");
										$("#servedId").addClass("enabled");
										
										//disabled the transfer dept drop down
										$("#transferDeptId").removeClass("enabled");
										$("#transferDeptId").addClass("disabled");
										
										// enabled show call button
										$("#noShowId").removeClass("disabled");
										$("#noShowId").addClass("enabled");
										
										// enabled CANCEL END button
										$("#cancelEndId").removeClass("disabled");
										$("#cancelEndId").addClass("enabled");										
										
										//for disabled the recall button
										$("#recallId").removeClass("disabled");
										$("#recallId").addClass("enabled");	
										
										//for disabled the recall button
										$("#callNextId").removeClass("disabled");
										$("#callNextId").addClass("enabled");	
										
												$scope.indexForNextCall=indx;
												$scope.commonObj=obj;
												$scope.speechMsg="";
												if($scope.isVoice=='A'){
					//								$scope.speech(' Token number, '+obj.tokenNumberWithStartLetter+', please visit '+$scope.counterName+' counter, thank you .');
													$scope.speechMsg=' Token number, '+obj.tokenNumberWithStartLetter+', please visit '+$scope.counterName+' counter, thank you .';
												}
												
												$scope.callTokenNumber=obj.tokenNumberWithStartLetter;
												$scope.saveCallTokenTime(1); //1 for call
												console.log("commonObj",$scope.commonObj);
										}
								}
							
							else if($scope.checkList.qmsCounterMasterId == null || $scope.selectedCounterId !=$scope.checkList.qmsCounterMasterId)
							{
								if($scope.checkList.callLogStatus == 0)
									{
									
									//enabled served call button
									$("#servedId").removeClass("disabled");
									$("#servedId").addClass("enabled");
									
									//disabled the transfer dept drop down
									$("#transferDeptId").removeClass("enabled");
									$("#transferDeptId").addClass("disabled");
									
									// enabled show call button
									$("#noShowId").removeClass("disabled");
									$("#noShowId").addClass("enabled");
									
									// enabled CANCEL END button
									$("#cancelEndId").removeClass("disabled");
									$("#cancelEndId").addClass("enabled");
									
									//for enabled the nextCall button
									$("#callNextId").removeClass("disabled");
									$("#callNextId").addClass("enabled");
									
									//for disabled the recall button
									$("#recallId").removeClass("disabled");
									$("#recallId").addClass("enabled");									
											
											$scope.indexForNextCall=indx;
											$scope.commonObj=obj;
											$scope.speechMsg="";
											if($scope.isVoice=='A'){
				//								$scope.speech(' Token number, '+obj.tokenNumberWithStartLetter+', please visit '+$scope.counterName+' counter, thank you .');
												$scope.speechMsg=' Token number, '+obj.tokenNumberWithStartLetter+', please visit '+$scope.counterName+' counter, thank you .';
											}
											
											$scope.callTokenNumber=obj.tokenNumberWithStartLetter;
											$scope.saveCallTokenTime(1); //1 for call
											console.log("commonObj",$scope.commonObj);
//											console.log("previous obj",$scope.previousObj);
									}
								if($scope.checkList.callLogStatus == 1)
								{
									  growl.warning(" already called by another counter..!!",{title : "WARNING!"});
									   return false;
								}
							}
							//new code end
						}
						$rootScope.stopLoader();
					});
					}
					});
				
				
				/*
				$scope.commonObj=obj;
				$scope.speechMsg="";
				if($scope.isVoice=='A'){
//					$scope.speech(' Token number, '+obj.tokenNumberWithStartLetter+', please visit '+$scope.counterName+' counter, thank you .');
					$scope.speechMsg=' Token number, '+obj.tokenNumberWithStartLetter+', please visit '+$scope.counterName+' counter, thank you .';
				}
				
				$scope.callTokenNumber=obj.tokenNumberWithStartLetter;
				$scope.saveCallTokenTime(1); //1 for call
				console.log("commonObj",$scope.commonObj);
				console.log("previous obj",$scope.previousObj)*/
			}
			$scope.recall=function(){
				
				if( $scope.commonObj!=""){
					
					
					$scope.speechMsg="";
					if($scope.isVoice=='A'){
//							$scope.speech(' Token number, '+$scope.commonObj.tokenNumberWithStartLetter+', please visit '+$scope.counterName+' counter, thank you .');
							$scope.speechMsg=' Token number, '+$scope.commonObj.tokenNumberWithStartLetter+', please visit '+$scope.counterName+' counter, thank you .';
						
						}
						$scope.callTokenNumber=$scope.commonObj.tokenNumberWithStartLetter;
						$scope.saveCallTokenTime(1); //1 for call
						console.log("commonObj",$scope.commonObj);
				}else{
					growl.error("Please call first..!!",{title : "WARNING!"});
					  return false;
				}
			}
			$scope.nextCall=function(){	
			
				
				$scope.previousObj="";
				$scope.nextToCallList=[];
				$scope.previousIndex="";
				if($scope.indexForNextCall === ""){
					$scope.indexForNextCall=0;
				}					
				else{					
					$scope.indexForNextCall++;
				}	
				$scope.tokenAvailableList=[];
				 angular.forEach($scope.tokenList, function(value , key){
					 	if(value.callLogStatus==0 || value.callLogStatus==1){
					 		$scope.tokenAvailableList.push(value);
				 		}
		            });
				if($scope.tokenAvailableList.length==0){
					$scope.indexForNextCall="";
					 growl.warning("Token Number not available..!!",{title : "WARNING!"});
					   return false;
				}
				$scope.commonObj=$scope.tokenList[$scope.indexForNextCall];
					//if no record found for next index
					if($scope.commonObj==undefined){
						$scope.indexForNextCall=0;
						$scope.commonObj=$scope.tokenList[0];
					}
					$scope.commonObj.qmsDeptMasterId=$scope.selectedDeptId;
					$scope.commonObj.qmsCounterMasterId=$scope.selectedCounterId;
					var data={
							qmsKioskMasterId:$scope.common.qmsKioskMasterId,					
							qmsDeptMasterId:$scope.selectedDeptId,
							qmsCounterMasterId:$scope.selectedCounterId,
							organisationId:$scope.unitId,
							unitId:$scope.orgUnitId
						}
					$rootScope.startLoader();
				 var URI = BASE_URL + ROOT_URL + "/api/pharmacy/releaseTokenNumber";
					GenericService.serviceAction("POST", URI, data).then(function(response) {
						if (response.data.status == "success") {
							console.log("updated log status with zero");
							//calling token list 
								//checking already call or not
								var URI = BASE_URL + ROOT_URL + "/api/pharmacy/checkingIsCalledOrNot";
								GenericService.serviceAction("POST", URI, $scope.commonObj).then(function(response) {
									if (response.data.status == "success") {	
											$scope.checkList = response.data.listObject[0];	
											 console.log("$scope.checkList",$scope.checkList);
											//new code start
												if($scope.checkList.qmsCounterMasterId == null || $scope.selectedCounterId ==$scope.checkList.qmsCounterMasterId)
													{
														if($scope.checkList.callLogStatus != 2 && $scope.checkList.callLogStatus != 3)
															{
															$scope.speechMsg="";
															if($scope.isVoice=='A' && $scope.commonObj!=""){
	//															$scope.speech(' Token number, '+$scope.commonObj.tokenNumberWithStartLetter+', please visit '+$scope.counterName+' counter, thank you .');
																$scope.speechMsg=' Token number, '+$scope.commonObj.tokenNumberWithStartLetter+', please visit '+$scope.counterName+' counter, thank you .';
															}
															
															$scope.callTokenNumber=$scope.commonObj.tokenNumberWithStartLetter;
															console.log("commonObj",$scope.commonObj);
															$scope.saveCallTokenTime(1); //1 for call
															
															//enabled served call button
															$("#servedId").removeClass("disabled");
															$("#servedId").addClass("enabled");
															
															//disabled the transfer dept drop down
															$("#transferDeptId").removeClass("enabled");
															$("#transferDeptId").addClass("disabled");
															
															// enabled show call button
															$("#noShowId").removeClass("disabled");
															$("#noShowId").addClass("enabled");
															
															// enabled CANCEL END button
															$("#cancelEndId").removeClass("disabled");
															$("#cancelEndId").addClass("enabled");
															//for enabled the recall button
															$("#recallId").removeClass("disabled");
															$("#recallId").addClass("enabled");
																
															}															
														
													}
												
												else if($scope.checkList.qmsCounterMasterId == null || $scope.selectedCounterId !=$scope.checkList.qmsCounterMasterId)
												{
													if($scope.checkList.callLogStatus == 0)
														{
															$scope.speechMsg="";
															if($scope.isVoice=='A' && $scope.commonObj!=""){
	//															$scope.speech(' Token number, '+$scope.commonObj.tokenNumberWithStartLetter+', please visit '+$scope.counterName+' counter, thank you .');
																$scope.speechMsg=' Token number, '+$scope.commonObj.tokenNumberWithStartLetter+', please visit '+$scope.counterName+' counter, thank you .';
															}
															
															$scope.callTokenNumber=$scope.commonObj.tokenNumberWithStartLetter;
															console.log("commonObj",$scope.commonObj);
//															console.log("previous obj",$scope.previousObj)
															$scope.saveCallTokenTime(1); //1 for call
															
															//enabled served call button
															$("#servedId").removeClass("disabled");
															$("#servedId").addClass("enabled");
															
															//disabled the transfer dept drop down
															$("#transferDeptId").removeClass("enabled");
															$("#transferDeptId").addClass("disabled");
															
															// enabled show call button
															$("#noShowId").removeClass("disabled");
															$("#noShowId").addClass("enabled");
															
															// enabled CANCEL END button
															$("#cancelEndId").removeClass("disabled");
															$("#cancelEndId").addClass("enabled");
															//for enabled the recall button
															$("#recallId").removeClass("disabled");
															$("#recallId").addClass("enabled");
														}
													if($scope.checkList.callLogStatus == 1)
													{
														$scope.nextCall();
//														  growl.warning("Please already called by another counter..!!",{title : "WARNING!"});
//														   return false;
													}
												}
												//new code end
										
										
									}
								});
						}
						$rootScope.stopLoader();
					});
					
					
				/*$scope.speechMsg="";
				if($scope.isVoice=='A' && $scope.commonObj!=""){
//					$scope.speech(' Token number, '+$scope.commonObj.tokenNumberWithStartLetter+', please visit '+$scope.counterName+' counter, thank you .');
					$scope.speechMsg=' Token number, '+$scope.commonObj.tokenNumberWithStartLetter+', please visit '+$scope.counterName+' counter, thank you .';
				}
				
				$scope.callTokenNumber=$scope.commonObj.tokenNumberWithStartLetter;
				$scope.commonObj.previousQMSTokenMasterId=$scope.previousObj.qmsTokenMasterId;
				console.log("commonObj",$scope.commonObj);
				console.log("previous obj",$scope.previousObj)
				$scope.saveCallTokenTime(1); //1 for call
				
				//enabled served call button
				$("#servedId").removeClass("disabled");
				$("#servedId").addClass("enabled");
				
				//disabled the transfer dept drop down
				$("#transferDeptId").removeClass("enabled");
				$("#transferDeptId").addClass("disabled");
				
				// enabled show call button
				$("#noShowId").removeClass("disabled");
				$("#noShowId").addClass("enabled");
				
				// enabled CANCEL END button
				$("#cancelEndId").removeClass("disabled");
				$("#cancelEndId").addClass("enabled");
				//for enabled the recall button
				$("#recallId").removeClass("disabled");
				$("#recallId").addClass("enabled");*/
			}
			$scope.servedRecall=function(obj){
//				$scope.indexForNextCall=indx;
				$scope.commonObj=obj;
				/*if($scope.isVoice=='A'){
					$scope.speech(' Token number, '+obj.tokenNumberWithStartLetter+', please visit '+$scope.counterName+' counter, thank you .');
				}*/
				
				//disabled served call button
				$("#servedId").removeClass("enabled");
				$("#servedId").addClass("disabled");
				// enabled show call button
				$("#noShowId").removeClass("enabled");
				$("#noShowId").addClass("disabled");
				
				// enabled CANCEL END button
				$("#cancelEndId").removeClass("disabled");
				$("#cancelEndId").addClass("enabled");

				// disabled call next button
				$("#callNextId").removeClass("enabled");
				$("#callNextId").addClass("disabled");
				$scope.callTokenNumber=obj.tokenNumberWithStartLetter;
//				$scope.saveCallTokenTime(2); //2 for served
				console.log("commonObj",$scope.commonObj);
				//disabled the transfer dept drop down
				$("#transferDeptId").removeClass("disabled");
				$("#transferDeptId").addClass("enabled");
			}
			
			$scope.speech = function (theText) {
			      window.speechSynthesis.speak(new SpeechSynthesisUtterance(theText));
			  };
			  
			  $scope.servedToken=function(){
				  if(($scope.commonObj.startLetter==undefined||$scope.commonObj.startLetter=="") && ($scope.commonObj.tokenNumber==undefined ||$scope.commonObj.tokenNumber=="" )){
					  growl.error("Please Select Token number..!!",{title : "WARNING!"});
					  return false;
				  }
					  
				  var data={
							  
						  		  qmsTokenCallLogId:$scope.commonObj.qmsTokenCallLogId,
								  qmsTokenMasterId:$scope.commonObj.qmsTokenMasterId,
								  qmsCounterMasterId:$scope.selectedCounterId,
								  qmsKioskMasterId:$scope.common.qmsKioskMasterId,
								  qmsDeptMasterId:$scope.common.qmsDeptMasterId,
								  organisationId:$scope.orgId,
								  unitId:$scope.orgUnitId,
								  isCall:'S', //s =served
								  callLogStatus:2 // served call
							  
				  			}		
					
					
					//disabled served call button
					$("#servedId").removeClass("enabled");
					$("#servedId").addClass("disabled");			
					
					
					// disabled show call button
					$("#noShowId").removeClass("enabled");
					$("#noShowId").addClass("disabled");
					
					// disabled CANCEL END button
					$("#cancelEndId").removeClass("disabled");
					$("#cancelEndId").addClass("enabled");				
				
					
					//for disabled the recall button
					$("#recallId").removeClass("enabled");
					$("#recallId").addClass("disabled");	
					
					//for disabled the callNext button
					/*$("#callNextId").removeClass("enabled");
					$("#callNextId").addClass("disabled");*/
					
					
					//if we save the token then it call calling next of next token  , fr this issue this code is added as trial and error purpose ---START
						if($scope.indexForNextCall != "" || $scope.indexForNextCall != undefined){
							$scope.indexForNextCall--;
						}
					// END 16-01-2020
					
				  $rootScope.startLoader();
				  var URI = BASE_URL + ROOT_URL + "/api/pharmacy/updateToken";
					GenericService.serviceAction("POST", URI, data).then(function(response) {
						if (response.data.status == "success") {
							if(response.data.status=="success"){
								growl.success("served successfully",
										{
											title : 'Success!'
										});
								//for enabled the transfer list 
								$("#transferDeptId").removeClass("disabled");
								$("#transferDeptId").addClass("enabled");								
							}
							$scope.getTokenList();
							$rootScope.stopLoader();
						}
					});
				  
			  }
			  $scope.cancelToken=function(){
				  $scope.callTokenNumber="";
//					$scope.commonObj="";
				  
				//for disabled transter list 
					$("#transferDeptId").removeClass("enabled");
					$("#transferDeptId").addClass("disabled");
					
					//for disabled  the recall button
					$("#recallId").removeClass("enabled");
					$("#recallId").addClass("disabled");
					
					//disabled served call button
					$("#servedId").removeClass("enabled");
					$("#servedId").addClass("disabled");							
					
					
					// disabled show call button
					$("#noShowId").removeClass("enabled");
					$("#noShowId").addClass("disabled");
					
					// disabled CANCEL END button
					$("#cancelEndId").removeClass("enabled");
					$("#cancelEndId").addClass("disabled");
					
					// enabled call next END button
					$("#callNextId").removeClass("disabled");
					$("#callNextId").addClass("enabled");
				  var data={
							 						
						  qmsTokenCallLogId:$scope.commonObj.qmsTokenCallLogId,
						  qmsTokenMasterId:$scope.commonObj.qmsTokenMasterId,
						  qmsCounterMasterId:$scope.selectedCounterId,
						  qmsKioskMasterId:$scope.common.qmsKioskMasterId,
						  qmsDeptMasterId:$scope.common.qmsDeptMasterId,
						  organisationId:$scope.orgId,
						  unitId:$scope.orgUnitId,
						  isCall:'C', //s =served
						  callLogStatus:3// cancel / end call
				  			}
				  console.log("data",data);
				  $rootScope.startLoader();
				  var URI = BASE_URL + ROOT_URL + "/api/pharmacy/updateToken";
					GenericService.serviceAction("POST", URI, data).then(function(response) {
						if (response.data.status == "success") {
							if(response.data.status=="success"){
								growl.success("cancel successfully",
										{
											title : 'Success!'
										});
							}
							//for enableding  the recall button
							/*$("#recallId").removeClass("disabled");
							$("#recallId").addClass("enabled");*/
							$scope.callTokenNumber="";
							$scope.commonObj="";
							$scope.getTokenList();
							$rootScope.stopLoader();
						}
					});
			  }
			  $scope.getDeptForTransfer=function(deptId){
				  if(deptId!=""){
					  angular.element('#deptChangePopup').modal('show');
				  }
			  }
			  
			  $scope.changeDept=function(){
				  console.log("dept id=",$scope.common.qmsDeptMasterId);
				  if(($scope.commonObj.startLetter==undefined||$scope.commonObj.startLetter=="") && ($scope.commonObj.tokenNumber==undefined ||$scope.commonObj.tokenNumber=="" )){
					  growl.error("Please Select Token number..!!",{title : "WARNING!"});
					  return false;
				  }
				  var data = {						
							qmsKioskMasterId:$scope.common.qmsKioskMasterId,
							startLetter:$scope.commonObj.startLetter,
							startNumber:$scope.commonObj.tokenNumber,
							tokenNumber:$scope.commonObj.tokenNumber,
//							qmsCounterMasterId:$scope.selectedCounterId,
							qmsDeptMasterId:$scope.common.qmsTransferDeptMasterId,
							qmsTokenMasterId:$scope.commonObj.qmsTokenMasterId,
							createdBy:$scope.userId,
							updatedBy:$scope.userId,
							organisationId:$scope.orgId,
							unitId:$scope.orgUnitId	,
							callLogStatus:0
						};
				  console.log("data",data); 
				  //return false;
					 $rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL + "/api/pharmacy/transferToken";
						GenericService.serviceAction("POST", URI, data).then(function(response) {
							if(response.data.status=="success"){
								growl.success("transefer successfully",
										{
											title : 'Success!'
										});
								$scope.common.qmsTransferDeptMasterId="";
								//for disabled transter list 
								$("#transferDeptId").removeClass("enabled");
								$("#transferDeptId").addClass("disabled");
								
								//for disabled  the recall button
								$("#recallId").removeClass("enabled");
								$("#recallId").addClass("disabled");
								
								//disabled served call button
								$("#servedId").removeClass("enabled");
								$("#servedId").addClass("disabled");							
								
								
								// disabled show call button
								$("#noShowId").removeClass("enabled");
								$("#noShowId").addClass("disabled");
								
								// disabled CANCEL END button
								$("#cancelEndId").removeClass("enabled");
								$("#cancelEndId").addClass("disabled");
								
								// enabled call next END button
								$("#callNextId").removeClass("disabled");
								$("#callNextId").addClass("enabled");								
							
							}
							 $scope.callTokenNumber="";
								$scope.commonObj="";
							$scope.getTokenList();
							$rootScope.stopLoader();
						});
			  }
			  
			  // for printing token number start
			  $scope.deptDetails="";
			  $scope.getDeptDetails=function(obj){
				  $scope.deptDetails=obj;
			  }
			  $scope.saveToken=function(){
					$scope.tokenNumber="wait...";
					console.log("generate token number",$scope.deptDetails);
					
					var data = {						
							qmsKioskMasterId:$scope.deptDetails.qmsKioskMasterId,
							startLetter:$scope.deptDetails.startLetter,
							startNumber:$scope.deptDetails.startNumber,
							qmsDeptMasterId:$scope.deptDetails.qmsDeptMasterId,
							createdBy:$scope.userId,
							updatedBy:$scope.userId,
							organisationId:$scope.orgId,
							unitId:$scope.orgUnitId	,
						};
					 $rootScope.startLoader();
						 $("#tokenPrintId").removeClass("enabled");
						 $("#tokenPrintId").addClass("disabled");
						var URI = BASE_URL + ROOT_URL + "/api/pharmacy/saveToken";
						GenericService.serviceAction("POST", URI, data).then(function(response) {
							$rootScope.stopLoader();
							$scope.tokenNumber = response.data.object;
							$("#tokenPrintId").removeClass("disabled");
							$("#tokenPrintId").addClass("enabled");
							$scope.getTokenList();
							$rootScope.stopLoader();
						});
					
				}
				$scope.printTokenNumberReport=function(){
					console.log("print token number");
//					$('#qmsTokenPrint').modal('show');
					$scope.currentDate = new Date();
					$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY');
					$scope.currentTime = moment(new Date()).format('HH:mm:ss');
					var printContents = $window.document.getElementById('qmsTokenPrintingId').innerHTML;
			        var popupWin = $window.open('', '_blank', 'width=400,height=400');
			        popupWin.document.open();
			        popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print()">' + printContents + '</body></html>');
			        popupWin.document.close();
				}
				// for printing token number end
				
				$interval(function() {
					$scope.getTokenList();
					//alert('d');
		          }, 10000);
			
}]);