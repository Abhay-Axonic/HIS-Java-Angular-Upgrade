angular
		.module('myApp')
		.controller(
				'createTabularDataTemplatesController',
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
									compId:'',
									numberOfRow:'',
									numberOfCoulmn:''
									
							};
							
							//get component list
							$scope.componentList = [];
							$scope.getComponentList = function(){
								try{
								
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/api/global/unit/template/tabular/getActiveTabularTemplateMaster"+"/"+ $scope.organizationId +"/"+ $scope.unitId;
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
							
							$scope.rowList = [];
							$scope.addRow = function(){
								$scope.rowList = [];
								for(var i = 0; i < $scope.comObj.numberOfRow ;i++){
									var newArr = {};
									newArr.rowId = i;
									newArr.coulmnId = i;
									newArr.rowName = '';
									newArr.inputbox = false;
									newArr.tabularTemplateId = $scope.comObj.tabularTemplateId;
									$scope.rowList.push(newArr);
							     }
								console.log("$scope.rowList",$scope.rowList);
							}
							
							
							$scope.columnList = [];
							$scope.columnHeaderList = [];
							$scope.addColumn = function(){
								$scope.columnList = [];
								$scope.columnHeaderList = []
								for(var i = 0; i < $scope.comObj.numberOfCoulmn ;i++){
									var newArr = {};
									newArr.coulmnId = i;
									newArr.rowId = i;
									newArr.columnName = '';
									newArr.inputValue = '';
									newArr.inputbox = false;
									newArr.tabularTemplateId = $scope.comObj.tabularTemplateId;
									$scope.columnList.push(newArr);
									$scope.columnHeaderList.push(newArr);
							     }
								console.log("$scope.columnList",$scope.columnList);
								$scope.savedList=[];
								angular.forEach($scope.rowList,function(value1,key1){
									angular.forEach($scope.columnList,function(value2,key2){
										var data = {
												inputValue:"",
												coulmnId:value2.coulmnId,
												tableName:$scope.tableName,
												columnName:value2.columnName,
												rowId:value1.rowId,
												rowName:value1.rowName,
												tabularTemplateId:$scope.comObj.tabularTemplateId,
												numberOfRow:$scope.comObj.numberOfRow,
												numberOfCoulmn:$scope.comObj.numberOfCoulmn,
												organizationId:$scope.organizationId,
												unitId:$scope.unitId,
												createdBy:$scope.userId,
												updatedBy:$scope.userId,
												status:'A',
												createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
												updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
										   };
												$scope.savedList.push(data);
									});
								});
								console.log("$scope.savedList",$scope.savedList);
								
							}
							$scope.ForTableName = false;
							$scope.openInputBoxForTableName = function(){
								$scope.ForTableName = true;
								angular.forEach($scope.columnHeaderList,function(value3){
										value3.inputbox = false;
								});
								angular.forEach($scope.rowList,function(value4){
										value4.inputbox = false;
								});
							}
							$scope.closeTableName = function(){
								$scope.ForTableName = false;
							}
							$scope.closeRowAndColumn = function(){
								angular.forEach($scope.columnHeaderList,function(value3){
									value3.inputbox = false;
							});
							angular.forEach($scope.rowList,function(value4){
									value4.inputbox = false;
							});
							}
							
							$scope.openInputBox = function(info){
								$scope.ForTableName = false;
								angular.forEach($scope.columnHeaderList,function(value3,index3){
									if(info.coulmnId == value3.coulmnId){
										value3.inputbox = true;
									}else{
										value3.inputbox = false;
									}
								});
						    }
							
							$scope.openInputBoxForRow = function(info){
								$scope.ForTableName = false;
								angular.forEach($scope.rowList,function(value3,index3){
									if(info.rowId == value3.rowId){
										value3.inputbox = true;
									}else{
										value3.inputbox = false;
									}
								});
						    }
							
							$scope.pushRowName = function(id,text){
									angular.forEach($scope.rowList,function(value1,index1) {
										if(value1.rowId == id){
											value1.rowName = text;
										}
									});
									
									if($scope.savedList.length > 0){
									angular.forEach($scope.savedList,function(value3,index3){
										if(value3.rowId == id){
											value3.rowName = text;
										}
									});
									}
							}
							
							$scope.pushColumnName = function(id,text){
								angular.forEach($scope.columnList,function(value1,index1) {
									if(value1.coulmnId == id){
										value1.columnName = text;
									}
								});
								
								if($scope.savedList.length > 0){
								angular.forEach($scope.savedList,function(value3,index3){
									if(value3.coulmnId == id){
										value3.columnName = text;
									}
								});
								}
						}
							
							$scope.savedList = [];
							$scope.newList=[];
							$scope.getCurrentStatus = function(inputValue,coulmnId,columnName,rowId,rowName){
						/*		if(inputValue != undefined){
									var data = {
											inputValue:inputValue,
											coulmnId:coulmnId,
											tableName:$scope.tableName,
											columnName:columnName,
											rowId:rowId,
											rowName:rowName,
											tabularTemplateId:$scope.comObj.tabularTemplateId,
											numberOfRow:$scope.comObj.numberOfRow,
											numberOfCoulmn:$scope.comObj.numberOfCoulmn,
											organizationId:$scope.organizationId,
											unitId:$scope.unitId,
											createdBy:$scope.userId,
											updatedBy:$scope.userId,
											status:'A',
											createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
									   };
											$scope.savedList.push(data);
											console.log("$scope.savedList",$scope.savedList);
								}*/
								if(inputValue != undefined){
								angular.forEach($scope.savedList,function(value3,index3){
									if(value3.coulmnId == coulmnId && value3.rowId == rowId){
										value3.inputValue=inputValue,
										value3.coulmnId = coulmnId;
										value3.tableName = $scope.tableName;
										value3.columnName = columnName;
										value3.rowId = rowId;
										value3.rowName= rowName;
									}
								});
								console.log("$scope.savedList",$scope.savedList);
								}
							}
							
							
							//save blood group matching list		
							$scope.saveTabularDataOfTemplate = function()
							{
								
								angular.forEach($scope.savedList,function(value3,index3){
									value3.tableName=$scope.tableName;
								});
								
								var data ={
										toSaveList:$scope.savedList,
										tabularTemplateId : $scope.comObj.tabularTemplateId,
								}
								$rootScope.startLoader();
								
								var URI = BASE_URL + ROOT_URL+ "/api/global/unit/template/tabular/mapper/saveTabularTemplateDataMapper";
								GenericService
								.serviceAction(METHOD_POST, URI, data)
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
							
							$scope.savedEditedList = [];
							
							$scope.getTemplateList = function(id)
							{
								$rootScope.startLoader();
								$scope.rowList = [];
								$scope.columnList = [];
								$scope.columnHeaderList = [];
								$scope.savedEditedList = [];
								$scope.savedList=[];
								$scope.savedEditedList = [];
								var URI = BASE_URL + ROOT_URL+ "/api/global/unit/template/tabular/mapper/getTabularTemplateDataMapperById/"+id+"/"+$scope.organizationId+"/"+$scope.unitId;
								GenericService
								.serviceAction(METHOD_GET, URI)
								.then(
										function(response) {

											if (response.data.status == 'success')
											{$rootScope.stopLoader();
												if(response.data.listObject.length > 0){
												$rootScope.stopLoader();
												$scope.rowList = [];
												$scope.columnList = [];
												$scope.columnHeaderList = [];
												$scope.savedEditedList = [];
												$scope.savedEditedList = response.data.listObject;
												$scope.savedList = response.data.listObject;
												angular.forEach($scope.savedEditedList,function(value1,key1){
													value1.organizationId=$scope.organizationId;
													value1.unitId=$scope.unitId;
													value1.createdBy=$scope.userId;
													value1.updatedBy=$scope.userId;
													value1.status='A';
													value1.createdDate= moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
													value1.updatedDate= moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
												});
												console.log("$scope.columnList",$scope.columnList);
												$scope.comObj.numberOfRow = response.data.listObject[0].numberOfRow;
												$scope.comObj.numberOfCoulmn = response.data.listObject[0].numberOfCoulmn;
												$scope.tableName = response.data.listObject[0].tableName;
												
													for(var i = 0; i < $scope.comObj.numberOfRow ;i++){
														var newArr = {};
														newArr.rowId = i;
														newArr.coulmnId = i;
														newArr.rowName = '';
														newArr.inputbox = false;
														newArr.tabularTemplateId = $scope.comObj.tabularTemplateId;
														$scope.rowList.push(newArr);
												     }
													console.log("$scope.rowList",$scope.rowList);
													
													for(var i = 0; i < $scope.comObj.numberOfCoulmn ;i++){
														var newArr = {};
														newArr.coulmnId = i;
														newArr.rowId = i;
														newArr.columnName = '';
														newArr.inputValue = '';
														newArr.inputbox = false;
														newArr.tabularTemplateId = $scope.comObj.tabularTemplateId;
														$scope.columnList.push(newArr);
														$scope.columnHeaderList.push(newArr);
												     }
													console.log("$scope.columnList",$scope.columnList);
													$scope.tableName = response.data.listObject[0].tableName;
													
													angular.forEach($scope.rowList,function(value,key){
														angular.forEach($scope.savedEditedList,function(value1,key1){
															if(value.rowId == value1.rowId){
																value.rowName = value1.rowName;
															}
														});
													});
													angular.forEach($scope.columnList,function(value,key){
														angular.forEach($scope.savedEditedList,function(value1,key1){
															if(value.coulmnId == value1.coulmnId){
																value.columnName = value1.columnName;
															}
														});
													});
													angular.forEach($scope.columnHeaderList,function(value,key){
														angular.forEach($scope.savedEditedList,function(value1,key1){
															if(value.coulmnId == value1.coulmnId){
																value.columnName = value1.columnName;
															}
														});
													});
						
												}
											} else if(response.data.status == 'error')
											{
												$rootScope.stopLoader();
												growl.error(response.data.message,
														{
															title : 'Error!'
														});
											}
										});
							}
							
			}]);
							