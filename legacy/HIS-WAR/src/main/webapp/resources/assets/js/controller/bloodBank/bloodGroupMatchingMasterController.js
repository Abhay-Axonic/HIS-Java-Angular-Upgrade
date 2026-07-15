angular
		.module('myApp')
		.controller(
				'bloodGroupMatchingMasterController',
				[
						'$scope',
						'$rootScope',
						'$state',
						'$http',
						'GenericService',
						'$sessionStorage',
						'$cookies',
						'growl',
						'$localStorage',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage) {
							
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$rootScope.loginpage = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.list = [];
							$scope.comObj ={
									compId:''
							};
							
							//get component list
							$scope.componentList = [];
							$scope.getComponentList = function(){
								try{
								
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/bloodBank/getActiveBloodComponentMaster"+"/"+ $scope.organizationId +"/"+ $scope.unitId;
									console.log("EDIT",URI);
									GenericService.serviceAction("GET",URI).then(function(response){
										console.log("Edit",response);
										
										if(response.data.status == "success"){
											$rootScope.stopLoader();
											$scope.componentList = response.data.listObject;
											
											console.log("$scope.componentList",$scope.componentList);
											
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
							
							$scope.getComponentList();
							
							//get Blood group list
							$scope.bloodGroupList = [];
							$scope.getBloodGroupList = function(){
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL+ "/global/bloodBank/getActiveBloodGroupMaster/"+$scope.organizationId +"/"+ $scope.unitId;;
									GenericService
									.serviceAction(METHOD_GET, URI )
									.then(
											function(response) {

												if (response.data.status == "success") {
													$rootScope.stopLoader();
													$scope.bloodGroupList = response.data.listObject;
													console.log("bloodGroupList ",response.data.listObject);
												}
											});
							}
							
							$scope.getBloodGroupList();
							
						
							$scope.bloodGroupMatchingList = [];
							$scope.newList=[];
							$scope.getCurrentStatus = function(checkValue,donorBloodGroupId,recipientBloodGroupId){
							
							//	alert(checkValue);
								
								var data = {
										recipientBloodGroupId:recipientBloodGroupId,
										donorBloodGroupId:donorBloodGroupId,
										availability:checkValue,
										checkValue:checkValue,
										componentId:$scope.comObj.compId,
										organizationId:$scope.organizationId,
										unitId:$scope.unitId,
										createdBy:$scope.userId,
										updatedBy:$scope.userId,
										status:'A',
										createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
								};
								
								if(checkValue==1)
									{
										$scope.bloodGroupMatchingList.push(data);
										console.log("$scope.bloodGroupMatchingList",$scope.bloodGroupMatchingList);
											
										for(var j = 0; j< $scope.newList.length; j++) {
											
											if( ($scope.newList[j].donorBloodGroupId==donorBloodGroupId) && ($scope.newList[j].recipientBloodGroupId==recipientBloodGroupId)){
												$scope.newList[j].availability=checkValue;
												$scope.newList[j].checkValue=checkValue;	
												console.log("$scope.newList1111",$scope.newList);
											
											}
										}
										
									}
								else if(checkValue==0)
									{
									
									for(var i = 0; i< $scope.bloodGroupMatchingList.length; i++) {
										
										if( ($scope.bloodGroupMatchingList[i].donorBloodGroupId==donorBloodGroupId) && ($scope.bloodGroupMatchingList[i].recipientBloodGroupId==recipientBloodGroupId)){
											$scope.bloodGroupMatchingList.splice(i,1);
											console.log("idx",i);
											console.log("$scope.bloodGroupMatchingList",$scope.bloodGroupMatchingList);
										}
									}
									if($scope.newList.length>0)
									{
									for(var j = 0; j< $scope.newList.length; j++) {
										
										if( ($scope.newList[j].donorBloodGroupId==donorBloodGroupId) && ($scope.newList[j].recipientBloodGroupId==recipientBloodGroupId)){
											$scope.newList[j].availability=0;
											$scope.newList[j].checkValue=0;
											console.log("idx",j);
											console.log("$scope.newList",$scope.newList);
										}
									}
									}
								}
							}
						
					//save blood group matching list		
						$scope.saveBloodGroupMatchingList = function()
						{
							$rootScope.startLoader();
							if( $scope.comObj.compId == undefined || $scope.comObj.compId == ""){
								$rootScope.stopLoader();
								growl.error("Please Select Component",
										{
											title : 'error'
										});
								return false;
							}
							if($scope.bloodGroupMatchingList.length>0){
								var unselectedCount = 0;
								angular.forEach($scope.bloodGroupMatchingList,function(value,index){
									if(value.checkValue == 0){
										unselectedCount++;
									}
								});
								
								if(unselectedCount == $scope.bloodGroupMatchingList.length){
									$rootScope.stopLoader();
									growl.error("Please Select Blood Group",
											{
												title : 'error'
											});
									return false;
								}
							}else{
								$rootScope.stopLoader();
								growl.error("Please Select Blood Group",
										{
											title : 'error'
										});
								return false;
							}
							
							var URI = BASE_URL + ROOT_URL+ "/api/bloodBank/saveBloodGroupMatchingMaster";
							GenericService
							.serviceAction(METHOD_POST, URI, $scope.bloodGroupMatchingList)
							.then(
									function(response) {

										if (response.data.status == 'success')
										{
											$rootScope.stopLoader();
											$scope.comObj.compId='';
											$scope.newList=[];
											growl.success(response.data.message,
													{
														title : 'Success!'
													});
											$scope.getBloodGroupList();
											$scope.$broadcast('show-errors-reset');
										} else if(response.data.status == 'error')
										{
											$rootScope.stopLoader();
											growl.error(response.data.message,
													{
														title : 'Error!'
													});
											$scope.getBloodGroupList();
										}
									});
						}
						
						
						$scope.getBloodGroupMatchingList = function(id)
						{
							$rootScope.startLoader();
							$scope.newList=[];
							
							var URI = BASE_URL + ROOT_URL+ "/api/bloodBank/getBloodGroupMatchingMasterById/"+id+"/"+$scope.organizationId+"/"+$scope.unitId;
							GenericService
							.serviceAction(METHOD_GET, URI)
							.then(
									function(response) {

										if (response.data.status == 'success')
										{
											$rootScope.stopLoader();
											$scope.list = response.data.listObject;
											$scope.bloodGroupMatchingList=$scope.list;
											
											//alert("succes");
											console.log("list",response.data.listObject);
											var flag=true;
												angular.forEach($scope.bloodGroupList,function(value,index) {//doner
													
														angular.forEach($scope.bloodGroupList,function(value1,index1) {//reciept
															
																angular.forEach($scope.list,function(value2,index2) {//database saved list
																
																	if(value.bloodGroupId == value2.donorBloodGroupId && value1.bloodGroupId == value2.recipientBloodGroupId){
																		$scope.checkValue = 1;
																		console.log("val",$scope.checkValue);
																		var newArr = {};
																		newArr.checkValue =value2.availability ; 
																		newArr.donorBloodGroupId = value2.donorBloodGroupId;
																		newArr.recipientBloodGroupId = value2.recipientBloodGroupId;
																		newArr.availability = 1;
																		$scope.newList.push(newArr);
																		flag=false;
																		
																		console.log("$scope.newLis 111fgsdfgbfghfght",$scope.newList);
																	}
																});
														if(flag){
															$scope.checkValue = 0;
																var newArr = {};
																newArr.checkValue =0 ; 
																newArr.donorBloodGroupId = value.bloodGroupId;
																newArr.recipientBloodGroupId =  value1.bloodGroupId;
																newArr.availability = 0;
																$scope.newList.push(newArr);
														}
														else{
															flag=true;
														}
														});
														
												});
												console.log("$scope.newLis fgsdfgbfghfght",$scope.newList);
										} else if(response.data.status == 'error')
										{
											$rootScope.stopLoader();
											growl.error(response.data.message,
													{
														title : 'Error!'
													});
											$scope.getBloodGroupList();
										}
									});
						}
						
			}]);
							