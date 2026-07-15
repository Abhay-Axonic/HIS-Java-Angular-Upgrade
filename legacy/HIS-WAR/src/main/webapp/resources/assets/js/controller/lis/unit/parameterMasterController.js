/**
 * @author Ganesh Chaudhari
 */

angular
		.module('myApp')
		.controller(
				"parameterMasterController",
				[
						'$scope',
						'$rootScope',
						'$state',
						'$cookies',
						'GenericService',
						'PagerService',
						'promiseFactory',
						'$sessionStorage',
						'growl',
						function($scope, $rootScope, $state, $cookies,
								GenericService, PagerService, promiseFactory,
								$sessionStorage, growl) {

							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "";
							$scope.saveBtnFlag = true;
							$scope.updateBtnFlag = false;
							$scope.updateScope = false;
							$scope.checkDelta = 'N';
							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.parameterMasterListCount;
							
							$scope.deltaNo = false;
							$scope.deltaText = false;
							
							/*** Check user is Loggedin */
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							$scope.orgUnitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;

							/* code for setting Label */
							$(".selectedPageName").text(
									" Parameter Master");

							
							$scope.parameterList = [];

							$scope.unitList;
							$scope.noOfDayList;
							$scope.ageGroupList;
							$scope.genderList;

							$scope.getUnitList = function(orgId) {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_COMMON + GET_UNIT_LIST + S
											+ orgId;
									console.log("URI", URI);
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) {
														$scope.unitList = response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							$scope.getUnitList($scope.orgId);
							
							$scope.getnoOfDayList = function(orgId) {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_COMMON + GET_DAY_LIST + S
											+ orgId;
									console.log("URI", URI);
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) {
														$scope.getnoOfDayList = response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							$scope.getnoOfDayList($scope.orgId);

							$scope.getAgeGroupList = function(orgId) {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_COMMON + GET_AGE_GROUP_LIST
											+ S + orgId;
									console.log("URI", URI);
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) {
														$scope.ageGroupList = response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							$scope.getAgeGroupList($scope.orgId);

							$scope.getGenderList = function(orgId) {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_COMMON
											+ GET_GENDER_LIST + S + orgId;
									console.log("URI", URI);
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) {
														$scope.genderList = response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							$scope.getGenderList($scope.orgId);
							
							
							$scope.getRefRangeTypeList = function(orgId,orgUnitId) {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_COMMON
											+ GET_REF_RANGE_TYPES + S + orgId+S+orgUnitId;
									console.log("URI", URI);
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) {
														$scope.refRangeTypeList = response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							$scope.getRefRangeTypeList($scope.orgId,$scope.orgUnitId);
							
							
							$scope.getTextualRangeList = function(orgId,orgUnitId) {
								try {
									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_COMMON
											+ TEXTUAL_REFERENCE_RANGES + S + orgId+S+orgUnitId;
									console.log("URI", URI);
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) {
														$scope.textualRangeList = response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							$scope.getTextualRangeList($scope.orgId,$scope.orgUnitId);
							

					/** =====================>Code for Push Pop Objects<===============*/
							
					$scope.helpValueList = [];
					$scope.refRengeList = [];
					$scope.refRengeTextualList = [];	
					$scope.refRengeMultiTextualList = [];	
					$scope.addHelpValue = function()
							{ 
						        if($scope.updateScope ==false)
						        	{
								      var helpValueMaster = angular.copy($scope.helpValueMaster);
								      $scope.listHelpValueMaster.push(helpValueMaster);
						        	}
						        else if($scope.updateScope ==true)
						        	{
						        	  var helpValueMaster = angular.copy($scope.helpValueMaster);
						        	  helpValueMaster.parameterId = $scope.parameterMaster.parameterId;
								      $scope.listHelpValueMaster.push(helpValueMaster);
						        	}
							};

					$scope.removeHelpvalue = function(deleteIndex)
							{
						  
						    if($scope.updateScope ==false)
			        	    {
								$scope.listHelpValueMaster.splice(deleteIndex, 1);
						    }
						   else if($scope.updateScope ==true)
				        	{
							   var helpValueMaster = angular.copy($scope.listHelpValueMaster[deleteIndex]);
					           helpValueMaster.parameterId = $scope.parameterMaster.parameterId;
					           helpValueMaster.isDeleted = 'Y';
					           $scope.helpValueList.push(helpValueMaster);
							   $scope.listHelpValueMaster.splice(deleteIndex, 1);
				        	}	
						}		
					$scope.addParamRefrengMaster = function()
							{
						      if($scope.updateScope ==false)
			        	        {
						    	   if($scope.parameterMaster.refrangeTypeId ==REFERENCE_VALUE)
						    	   {
							    		  if($scope.parameterMaster.genderReqiured != null && $scope.parameterMaster.genderReqiured == 'Y' &&
							    				   $scope.parameterMaster.ageGroupReqiured != null && $scope.parameterMaster.ageGroupReqiured == 'Y'){
							    			   
							    			var length =  $scope.listParamRefrengMaster.length;
							    			var dto = $scope.listParamRefrengMaster[length-1];
							    			if(dto.genderId != "" && dto.ageGroupId != ""){
							    				  var paramRefrengMast = angular.copy($scope.paramRefrengMaster);
												  $scope.listParamRefrengMaster.push(paramRefrengMast);
							    			}else{
							    				growl.warning("Please select age group and gender .",
														{
															title : 'warning!'
														});
							    				
							    				return false;
							    			}
							    			   
							    		   }else if($scope.parameterMaster.genderReqiured != null && $scope.parameterMaster.genderReqiured == 'Y'){
							    			   var length =  $scope.listParamRefrengMaster.length;
								    			var dto = $scope.listParamRefrengMaster[length-1];
								    			if(dto.genderId != ""){
								    				  var paramRefrengMast = angular.copy($scope.paramRefrengMaster);
													  $scope.listParamRefrengMaster.push(paramRefrengMast);
								    			}else{
								    				growl.warning("Please select gender .",
															{
																title : 'warning!'
															});
								    				
								    				return false;
								    			}
							    		   }else if($scope.parameterMaster.ageGroupReqiured != null && $scope.parameterMaster.ageGroupReqiured == 'Y'){
							    			   var length =  $scope.listParamRefrengMaster.length;
								    			var dto = $scope.listParamRefrengMaster[length-1];
								    			if(dto.ageGroupId != ""){
								    				  var paramRefrengMast = angular.copy($scope.paramRefrengMaster);
													  $scope.listParamRefrengMaster.push(paramRefrengMast);
								    			}else{
								    				growl.warning("Please select age group.",
															{
																title : 'warning!'
															});
								    				
								    				return false;
								    			}
							    		   }else{
							    			   
							    			   var length =  $scope.listParamRefrengMaster.length;
								    			
								    			if(length >=1){
								    				growl.warning("you can add only one recode.",
															{
																title : 'warning!'
															});
								    				
								    				return false;
								    			}
							    			   
							    		   }
							    	   
						    	   }
						    	   else if($scope.parameterMaster.refrangeTypeId ==TEXTUAL_RANGE)
						    		   {
						    		   	
							    		  if($scope.parameterMaster.genderReqiured != null && $scope.parameterMaster.genderReqiured == 'Y' &&
							    				   $scope.parameterMaster.ageGroupReqiured != null && $scope.parameterMaster.ageGroupReqiured == 'Y'){
							    			   
							    			var length =  $scope.listParamTextualRangeMaster.length;
							    			var dto = $scope.listParamTextualRangeMaster[length-1];
							    			if(dto.genderId != "" && dto.ageGroupId != ""){
							    				 var paramTextualRangeMast = angular.copy($scope.paramTextualRangeMaster);
													$scope.listParamTextualRangeMaster.push(paramTextualRangeMast);
							    			}else{
							    				growl.warning("Please select age group and gender .",
														{
															title : 'warning!'
														});
							    				
							    				return false;
							    			}
							    			   
							    		   }else if($scope.parameterMaster.genderReqiured != null && $scope.parameterMaster.genderReqiured == 'Y'){
							    			   var length =  $scope.listParamTextualRangeMaster.length;
								    			var dto = $scope.listParamTextualRangeMaster[length-1];
								    			if(dto.genderId != ""){
								    				 var paramTextualRangeMast = angular.copy($scope.paramTextualRangeMaster);
														$scope.listParamTextualRangeMaster.push(paramTextualRangeMast);
								    			}else{
								    				growl.warning("Please select gender .",
															{
																title : 'warning!'
															});
								    				
								    				return false;
								    			}
							    		   }else if($scope.parameterMaster.ageGroupReqiured != null && $scope.parameterMaster.ageGroupReqiured == 'Y'){
							    			   var length =  $scope.listParamTextualRangeMaster.length;
								    			var dto = $scope.listParamTextualRangeMaster[length-1];
								    			if(dto.ageGroupId != ""){
								    				 var paramTextualRangeMast = angular.copy($scope.paramTextualRangeMaster);
														$scope.listParamTextualRangeMaster.push(paramTextualRangeMast);
								    			}else{
								    				growl.warning("Please select age group.",
															{
																title : 'warning!'
															});
								    				
								    				return false;
								    			}
							    		   }else{
							    			   
							    			   var length =  $scope.listParamTextualRangeMaster.length;
								    			
								    			if(length >=1){
								    				growl.warning("you can add only one recode.",
															{
																title : 'warning!'
															});
								    				
								    				return false;
								    			}
							    			   
							    		   }
						    		  
						    		   }
						    	   else if($scope.parameterMaster.refrangeTypeId ==MULTIVALUED_RANGE)
						    		   {
							    		  if($scope.parameterMaster.genderReqiured != null && $scope.parameterMaster.genderReqiured == 'Y' &&
							    				   $scope.parameterMaster.ageGroupReqiured != null && $scope.parameterMaster.ageGroupReqiured == 'Y'){
							    			   
							    			var length =  $scope.listParamMultiTextualRangeMaster.length;
							    			var dto = $scope.listParamMultiTextualRangeMaster[length-1];
							    			if(dto.genderId != "" && dto.ageGroupId != ""){
							    				 var paramMultiTextualRangeMast = angular.copy($scope.paramMultiTextualRangeMaster);
													$scope.listParamMultiTextualRangeMaster.push(paramMultiTextualRangeMast);
							    			}else{
							    				growl.warning("Please select age group and gender .",
														{
															title : 'warning!'
														});
							    				
							    				return false;
							    			}
							    			   
							    		   }else if($scope.parameterMaster.genderReqiured != null && $scope.parameterMaster.genderReqiured == 'Y'){
							    			   var length =  $scope.listParamMultiTextualRangeMaster.length;
								    			var dto = $scope.listParamMultiTextualRangeMaster[length-1];
								    			if(dto.genderId != ""){
								    				 var paramMultiTextualRangeMast = angular.copy($scope.paramMultiTextualRangeMaster);
														$scope.listParamMultiTextualRangeMaster.push(paramMultiTextualRangeMast);
								    			}else{
								    				growl.warning("Please select gender .",
															{
																title : 'warning!'
															});
								    				
								    				return false;
								    			}
							    		   }else if($scope.parameterMaster.ageGroupReqiured != null && $scope.parameterMaster.ageGroupReqiured == 'Y'){
							    			   var length =  $scope.listParamMultiTextualRangeMaster.length;
								    			var dto = $scope.listParamMultiTextualRangeMaster[length-1];
								    			if(dto.ageGroupId != ""){
								    				 var paramMultiTextualRangeMast = angular.copy($scope.paramMultiTextualRangeMaster);
														$scope.listParamMultiTextualRangeMaster.push(paramMultiTextualRangeMast);
								    			}else{
								    				growl.warning("Please select age group.",
															{
																title : 'warning!'
															});
								    				
								    				return false;
								    			}
							    		   }else{
							    			   
							    			   var length =  $scope.listParamMultiTextualRangeMaster.length;
								    			
								    			if(length >=1){
								    				growl.warning("you can add only one recode.",
															{
																title : 'warning!'
															});
								    				
								    				return false;
								    			}
							    			   
							    		   }
						    		   }
						    	  
			        	       }
						      else if($scope.updateScope ==true)
					        	{
						    	  
						    	  if($scope.parameterMaster.refrangeTypeId ==REFERENCE_VALUE)
						    	   {
						    		  
						    		  if($scope.parameterMaster.genderReqiured != null && $scope.parameterMaster.genderReqiured == 'Y' &&
						    				   $scope.parameterMaster.ageGroupReqiured != null && $scope.parameterMaster.ageGroupReqiured == 'Y'){
						    			   
						    			var length =  $scope.listParamRefrengMaster.length;
						    			var dto = $scope.listParamRefrengMaster[length-1];
						    			if(dto.genderId != "" && dto.ageGroupId != ""){
						    				  var paramRefrengMast = angular.copy($scope.paramRefrengMaster);
								    		  paramRefrengMast.parameterId = $scope.parameterMaster.parameterId;
											  $scope.listParamRefrengMaster.push(paramRefrengMast);
						    			}else{
						    				growl.warning("Please select age group and gender .",
													{
														title : 'warning!'
													});
						    				
						    				return false;
						    			}
						    			   
						    		   }else if($scope.parameterMaster.genderReqiured != null && $scope.parameterMaster.genderReqiured == 'Y'){
						    			   var length =  $scope.listParamRefrengMaster.length;
							    			var dto = $scope.listParamRefrengMaster[length-1];
							    			if(dto.genderId != ""){
							    				  var paramRefrengMast = angular.copy($scope.paramRefrengMaster);
									    		  paramRefrengMast.parameterId = $scope.parameterMaster.parameterId;
												  $scope.listParamRefrengMaster.push(paramRefrengMast);
							    			}else{
							    				growl.warning("Please select gender .",
														{
															title : 'warning!'
														});
							    				
							    				return false;
							    			}
						    		   }else if($scope.parameterMaster.ageGroupReqiured != null && $scope.parameterMaster.ageGroupReqiured == 'Y'){
						    			   var length =  $scope.listParamRefrengMaster.length;
							    			var dto = $scope.listParamRefrengMaster[length-1];
							    			if(dto.ageGroupId != ""){
							    				  var paramRefrengMast = angular.copy($scope.paramRefrengMaster);
									    		  paramRefrengMast.parameterId = $scope.parameterMaster.parameterId;
												  $scope.listParamRefrengMaster.push(paramRefrengMast);
							    			}else{
							    				growl.warning("Please select age group.",
														{
															title : 'warning!'
														});
							    				
							    				return false;
							    			}
						    		   }else{
						    			   
						    			   var length =  $scope.listParamRefrengMaster.length;
							    			
							    			if(length >=1){
							    				growl.warning("you can add only one recode.",
														{
															title : 'warning!'
														});
							    				
							    				return false;
							    			}
						    			   
						    		   }
						    	   }
						    	   else if($scope.parameterMaster.refrangeTypeId ==TEXTUAL_RANGE)
						    		   {
						    		   	
							    		  if($scope.parameterMaster.genderReqiured != null && $scope.parameterMaster.genderReqiured == 'Y' &&
							    				   $scope.parameterMaster.ageGroupReqiured != null && $scope.parameterMaster.ageGroupReqiured == 'Y'){
							    			   
							    			var length =  $scope.listParamTextualRangeMaster.length;
							    			var dto = $scope.listParamTextualRangeMaster[length-1];
							    			if(dto.genderId != "" && dto.ageGroupId != ""){
							    				 var paramTextualRangeMast = angular.copy($scope.paramTextualRangeMaster);
									    		   paramTextualRangeMast.parameterId = $scope.parameterMaster.parameterId;
													$scope.listParamTextualRangeMaster.push(paramTextualRangeMast);
							    			}else{
							    				growl.warning("Please select age group and gender .",
														{
															title : 'warning!'
														});
							    				
							    				return false;
							    			}
							    			   
							    		   }else if($scope.parameterMaster.genderReqiured != null && $scope.parameterMaster.genderReqiured == 'Y'){
							    			   var length =  $scope.listParamTextualRangeMaster.length;
								    			var dto = $scope.listParamTextualRangeMaster[length-1];
								    			if(dto.genderId != ""){
								    				 var paramTextualRangeMast = angular.copy($scope.paramTextualRangeMaster);
										    		   paramTextualRangeMast.parameterId = $scope.parameterMaster.parameterId;
														$scope.listParamTextualRangeMaster.push(paramTextualRangeMast);
								    			}else{
								    				growl.warning("Please select gender .",
															{
																title : 'warning!'
															});
								    				
								    				return false;
								    			}
							    		   }else if($scope.parameterMaster.ageGroupReqiured != null && $scope.parameterMaster.ageGroupReqiured == 'Y'){
							    			   var length =  $scope.listParamTextualRangeMaster.length;
								    			var dto = $scope.listParamTextualRangeMaster[length-1];
								    			if(dto.ageGroupId != ""){
								    				 var paramTextualRangeMast = angular.copy($scope.paramTextualRangeMaster);
										    		   paramTextualRangeMast.parameterId = $scope.parameterMaster.parameterId;
														$scope.listParamTextualRangeMaster.push(paramTextualRangeMast);
								    			}else{
								    				growl.warning("Please select age group.",
															{
																title : 'warning!'
															});
								    				
								    				return false;
								    			}
							    		   }else{
							    			   
							    			   var length =  $scope.listParamTextualRangeMaster.length;
								    			
								    			if(length >=1){
								    				growl.warning("you can add only one recode.",
															{
																title : 'warning!'
															});
								    				
								    				return false;
								    			}
							    			   
							    		   }
						    		  
						    		   }
						    	   else if($scope.parameterMaster.refrangeTypeId ==MULTIVALUED_RANGE)
						    		   {
							    		  if($scope.parameterMaster.genderReqiured != null && $scope.parameterMaster.genderReqiured == 'Y' &&
							    				   $scope.parameterMaster.ageGroupReqiured != null && $scope.parameterMaster.ageGroupReqiured == 'Y'){
							    			   
							    			var length =  $scope.listParamMultiTextualRangeMaster.length;
							    			var dto = $scope.listParamMultiTextualRangeMaster[length-1];
							    			if(dto.genderId != "" && dto.ageGroupId != ""){
							    				 var paramMultiTextualRangeMast = angular.copy($scope.paramMultiTextualRangeMaster);
									    		   paramMultiTextualRangeMast.parameterId = $scope.parameterMaster.parameterId;
													$scope.listParamMultiTextualRangeMaster.push(paramMultiTextualRangeMast);
							    			}else{
							    				growl.warning("Please select age group and gender .",
														{
															title : 'warning!'
														});
							    				
							    				return false;
							    			}
							    			   
							    		   }else if($scope.parameterMaster.genderReqiured != null && $scope.parameterMaster.genderReqiured == 'Y'){
							    			   var length =  $scope.listParamMultiTextualRangeMaster.length;
								    			var dto = $scope.listParamMultiTextualRangeMaster[length-1];
								    			if(dto.genderId != ""){
								    				 var paramMultiTextualRangeMast = angular.copy($scope.paramMultiTextualRangeMaster);
										    		   paramMultiTextualRangeMast.parameterId = $scope.parameterMaster.parameterId;
														$scope.listParamMultiTextualRangeMaster.push(paramMultiTextualRangeMast);
								    			}else{
								    				growl.warning("Please select gender .",
															{
																title : 'warning!'
															});
								    				
								    				return false;
								    			}
							    		   }else if($scope.parameterMaster.ageGroupReqiured != null && $scope.parameterMaster.ageGroupReqiured == 'Y'){
							    			   var length =  $scope.listParamMultiTextualRangeMaster.length;
								    			var dto = $scope.listParamMultiTextualRangeMaster[length-1];
								    			if(dto.ageGroupId != ""){
								    				 var paramMultiTextualRangeMast = angular.copy($scope.paramMultiTextualRangeMaster);
										    		   paramMultiTextualRangeMast.parameterId = $scope.parameterMaster.parameterId;
														$scope.listParamMultiTextualRangeMaster.push(paramMultiTextualRangeMast);
								    			}else{
								    				growl.warning("Please select age group.",
															{
																title : 'warning!'
															});
								    				
								    				return false;
								    			}
							    		   }else{
							    			   
							    			   var length =  $scope.listParamMultiTextualRangeMaster.length;
								    			
								    			if(length >=1){
								    				growl.warning("you can add only one recode.",
															{
																title : 'warning!'
															});
								    				
								    				return false;
								    			}
							    			   
							    		   }
						    		   }
					        	}	
							};

					$scope.removeParamRefrengMaster = function(deleteIndex)
							{
						       if($scope.updateScope ==false)
	        	                {
						    	   
						    	   if($scope.parameterMaster.refrangeTypeId ==REFERENCE_VALUE)
						    	   {
						    		   $scope.listParamRefrengMaster.splice(deleteIndex, 1);
						    	   }
						    	   else if($scope.parameterMaster.refrangeTypeId ==TEXTUAL_RANGE)
						    	   {
						    		   $scope.listParamTextualRangeMaster.splice(deleteIndex, 1);
						    		}
						    	   else if($scope.parameterMaster.refrangeTypeId ==MULTIVALUED_RANGE)
						    		  {
						    		    $scope.listParamMultiTextualRangeMaster.splice(deleteIndex, 1);
						    		  }
								 
	        	                }
						       else if($scope.updateScope ==true)
					        	{
						    	   
						    	   if($scope.parameterMaster.refrangeTypeId ==REFERENCE_VALUE)
						    	   {
						    		   var paramRefrengMaster = angular.copy($scope.listParamRefrengMaster[deleteIndex]);
								       paramRefrengMaster.isDeleted = 'Y'
									   $scope.refRengeList.push(paramRefrengMaster);
								       $scope.listParamRefrengMaster.splice(deleteIndex, 1);
						    	   }
						    	   else if($scope.parameterMaster.refrangeTypeId ==TEXTUAL_RANGE)
						    	   {
						    		   var paramTextualRangeMst = angular.copy($scope.listParamTextualRangeMaster[deleteIndex]);
						    		   paramTextualRangeMst.isDeleted = 'Y'
									   $scope.refRengeTextualList.push(paramTextualRangeMst);
								       $scope.listParamTextualRangeMaster.splice(deleteIndex, 1);
						    		   
						    		   $scope.listParamTextualRangeMaster.splice(deleteIndex, 1);
						    		}
						    	   else if($scope.parameterMaster.refrangeTypeId ==MULTIVALUED_RANGE)
						    		  {
						    		   var paramMultiTextualRangeMst = angular.copy($scope.listParamMultiTextualRangeMaster[deleteIndex]);
						    		   paramMultiTextualRangeMst.isDeleted = 'Y'
									   $scope.refRengeMultiTextualList.push(paramMultiTextualRangeMst);
								       $scope.listParamMultiTextualRangeMaster.splice(deleteIndex, 1);
						    		  }
						    	   
						    	   
					        	}	
							};		
							
							
					$scope.showRefRange = function(refrangeTypeId)
					{
						$scope.parameterMaster.isMachineWisePanicValues= 'N';
						$scope.listParamRefrengMaster = [];
						$scope.listParamTextualRangeMaster = [];
						$scope.listParamMultiTextualRangeMaster = [];
						$scope.helpValueList = [];
						if(refrangeTypeId==REFERENCE_VALUE)
						{
						
						  var paramRefrengMast = angular.copy($scope.paramRefrengMaster);
						  if($scope.updateScope ==true)
					        	{
							      paramRefrengMast.parameterId = $scope.parameterMaster.parameterId;
						    	}
						  $scope.listParamRefrengMaster.push(paramRefrengMast);
						}
					else if(refrangeTypeId==TEXTUAL_RANGE)
						{
						 var paramTextualRangeMaster = angular.copy($scope.paramTextualRangeMaster);
						  if($scope.updateScope ==true)
				        	{
							  paramTextualRangeMaster.parameterId = $scope.parameterMaster.parameterId;
					    	}
							if($scope.listHelpValueMaster.length == 0){
					    		var newArr = {};
					    	     newArr.isDefault = false;
					    	    newArr.helpValue = "";
					    	    $scope.listHelpValueMaster.push(newArr);
					    	}
						 $scope.listParamTextualRangeMaster.push(paramTextualRangeMaster);
						}
					else if(refrangeTypeId==MULTIVALUED_RANGE)
						{
						 var paramMultiTextualRangeMaster = angular.copy($scope.paramMultiTextualRangeMaster);
						  if($scope.updateScope ==true)
				        	{
							  paramMultiTextualRangeMaster.parameterId = $scope.parameterMaster.parameterId;
					    	}
							if($scope.listHelpValueMaster.length == 0){
					    		var newArr = {};
					    	     newArr.isDefault = false;
					    	    newArr.helpValue = "";
					    	    $scope.listHelpValueMaster.push(newArr);
					    	}
						$scope.listParamMultiTextualRangeMaster.push(paramMultiTextualRangeMaster);
						}
					}
							
					$scope.showFootNote = function(testIndex)
					{
						$('#footNoteSingle').modal('show');
						$scope.testIndex  = testIndex;
					}
					$scope.showPanicValue = function(parameterId,refrangeTypeId)
					{
						$scope.refrangeTypeId = refrangeTypeId;
						try {
							var data = "";
							var URI = BASE_URL + ROOT_URL
							+ LIS_UNIT
							+ REFRANGES_LIST +S + parameterId+S+refrangeTypeId+S + $scope.orgId+S+$scope.orgUnitId;
							console.log("URI", URI);
							if(refrangeTypeId ==REFERENCE_VALUE)
							{
								GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											$scope.paramRefRengeList = response.data.listObject;
										});
							}
							else if(refrangeTypeId ==TEXTUAL_RANGE)
							{
								GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											$scope.textualParameterList = response.data.listObject;
										});
							}
							else if(refrangeTypeId ==MULTIVALUED_RANGE) 
							{ 
								GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											$scope.multiTextualList = response.data.listObject;
										});
							}
							$('#panicValueSingle').modal('show');
						} catch (e) {
							console.log(e.message)
						}
					}
					
					$scope.showHelpValue = function(parameterId)
					{
						try {
							$scope.helpValueViewList = [];
							var data = "";
							var URI = BASE_URL + ROOT_URL
							+ LIS_UNIT
							+ GET_HELP_VALUES +S + parameterId+S+S + $scope.orgId+S+$scope.orgUnitId;
							console.log("URI", URI);
								GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											$scope.helpValueViewList = response.data.listObject;
										});
								$('#helpValueSingle').modal('show');
							}
						catch (e) {
							console.log(e.message)
						}
					}
							
					
					
					
					
					
						/**=================>OBJECT DELCLARATION <========================= **/	
							
							
							$scope.listParameterMaster = [];
							$scope.listHelpValueMaster = [];
							$scope.listTestParamMpprMaster = [];
							$scope.listParamRefrengMaster = [];
							$scope.listParamTextualRangeMaster = [];
							$scope.listParamMultiTextualRangeMaster = [];

							
							$scope.initMasters = function()
							{
								
								$scope.listParameterMaster = [];
								$scope.listParamRefrengMaster = [];
								$scope.listHelpValueMaster = [];
								
								$scope.listTestParamMpprMaster = [];
								$scope.listParamTextualRangeMaster = [];
								$scope.listParamMultiTextualRangeMaster = [];
								
								$scope.listParamRefrengMaster = [];
								$scope.listParamTextualRangeMaster = [];
								$scope.listParamMultiTextualRangeMaster = [];
								
								
								
							}
							$scope.paramRefrengMaster = {
									"refrengId" : "",
									"parameterId" : "",
									"genderId" : "",
									"minValue" : "",
									"maxValue" : "",
									"lessThan" : "",
									"moreThan" : "",
									"ageGroupId" : "",
									"remark" : "",
									"refrengStatus" : "",
									"createdBy" : $scope.userId,
									"createdDate" : "",
									"updatedBy" : $scope.userId,
									"refrengStatus" : "A",
									"updatedDate" : "",
									"orgId" : $scope.orgId,
									"orgUnitId" : $scope.orgUnitId,
									"isDeleted" : "N"
								}

								$scope.helpValueMaster = {
									"helpValueId" : "",
									"parameterId" : "",
									"helpValue" : "",
									"status" : "A",
									"createdBy" : "",
									"createdDate" : "",
									"updatedBy" : $scope.userId,
									"updatedDate" : "",
									"orgId" : $scope.orgId,
									"orgUnitId" : $scope.orgUnitId,
									"isDeleted" : "N",
									"isDefault" : false,
									"isAbnormal": 'N'
								}

							    $scope.initParameterMaster = function()
							    {
								$scope.parameterMaster = {
										"parameterId" : "",
										"refrangeTypeId":"1",
										"parameterName" : "",	
										"aliesName" : "",
										"parameterCode" : "",
										"unitId" :"",
										"deltaDaysId" : "",
										"deltaPer" : "",
										"testType" : 0,
										"isMultyparameter" : "Y",
										"createdBy" : $scope.userId,
										"createdDate" :"",
										"updatedBy" : $scope.userId,
										"updatedDate" : "",
										"orgId" : $scope.orgId,
										"orgUnitId" : $scope.orgUnitId,
										"status" : "A",
										"isFormula":"N",
										"isEditable":"N",
										"paramPrecision":0,
										"listParamRefrengMaster" : $scope.listParamRefrengMaster,
										"listHelpValueMaster" : $scope.listHelpValueMaster,
										"listParamTextualRanageMaster":$scope.listParamTextualRangeMaster,
										"listParamMultiTextualRangeMaster":$scope.listParamMultiTextualRangeMaster,
										"techniqueId":"",
										"isMachineWisePanicValues": 'N',
										"ageGroupReqiured":'N',
										"genderReqiured":'N',
									}
							    }
							
							 $scope.initParameterMaster();
								$scope.paramTextualRangeMaster = {
									  "paramTextualRangeId" : "",
									  "parameterId" : "",
									  "textualRangeId" : "",
									  "textualRangeName" : "",
									  "createdBy" : $scope.userId,
									  "createdDate" : "",
									  "updatedBy" :$scope.userId,
									  "updatedDate" : "",
                                      "status" : 'A',
									  "orgId" : $scope.orgId,
									  "orgUnitId" : $scope.orgUnitId,
									  "isDeleted" : "N",
									  "ageGroupId" : "",
									  "remark" : "",
									  "genderId" : "",
									  "genderName" : "",
									  "ageGroupName" : "",
									  "ageFromDay" : "",
									  "ageToDay" : ""
									}


								$scope.paramMultiTextualRangeMaster = 	{
									  "paramMultiTextualRangeId" : "",
									  "parameterId" : "",
									  "multitextaulRange" : "",
									  "createdBy" : $scope.userId,
									  "createdDate" : "",
									  "updatedBy" : $scope.userId,
									  "updatedDate" : "",
                                      "status" : 'A',
									  "orgId" : $scope.orgId,
									  "orgUnitId" : $scope.orgUnitId,
									  "isDeleted" : "N",
									  "ageGroupId" : "",
									  "remark" : "",
									  "genderId" : "",
									  "genderName" : "",
									  "ageGroupName" : "",
									  "ageFromDay" : "",
									  "ageToDay" : ""
									}
							

							$scope.initParameter = function() 
							{
								
								
								$scope.listHelpValueMaster.push(angular.copy($scope.helpValueMaster));
								$scope.listParamRefrengMaster.push(angular.copy($scope.paramRefrengMaster));
								$scope.parameterMaster.refrangeTypeId = 1;
								/*$scope.parameterMaster = {
									"testId" : "",
									"testCode" : "",
									"testDesc" : "",
									"testAlies" : "",
									"deptId" : "",
									"sampleId" : "",
									"noOfSampleReq" : "",
									"sampleVolume" : "",
									"containerId" : "",
									"reportTypeId" : "",
									"techniqueId" : "",
									"identificationNo" : "",
									"prerequisitsId" : "",
									"proRelease" : "",
									"testStatus" : "",
									"isOutsourced" : "",
									"footNote" : "",
									"createdBy" : "",
									"createdDate" : "",
									"updatedBy" : "",
									"updatedDate" : "",
									"orgId" : $scope.orgId,
									"orgUnitId" : $scope.orgUnitId,
									"normlTatTime" : "",
									"normlTatId" : "",
									"urgentTatTime" : "",
									"urgentTatId" : "",
									"serviceId" : "",
									"testType" : 0,
									"listParameterMasterDto" : $scope.listParameterMaster,
									"listTestParamMppr" : $scope.listTestParamMpprMaster
								}*/
							}
							$scope.initParameter();
							// ====================================================CODE START FOR SINGLE PARAM MASTER LIST===========================================================

							$scope.setNoOfRecords = function() {
								$scope.initParameterMasterList($scope.orgId,$scope.orgUnitId, $scope.offset, $scope.noOfRecordsPerPage);
							};
							$scope.initParameterMasterList = function(orgId,orgUnitId, offset, noOfRecordsPerPage)
							{
								$rootScope.startLoader();
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
								var URI1 = BASE_URL + ROOT_URL + LIS_UNIT + LIST_PARAMETER_MASTER + S + orgId + S +orgUnitId+S+ offset
										+ S + noOfRecordsPerPage;
								var URI2 = BASE_URL + ROOT_URL + LIS_UNIT + GET_TOTAL_PARAMETER_RECORD + S + orgId+S+orgUnitId;
								promiseFactory.setPromises(URI1, URI2, "GET", "GET").then(function(response) 
								{
									$rootScope.stopLoader();
									$scope.parameterList = response[0].listObject;
									$scope.parameterMasterListCount = response[1].object;
									$scope.setPage(1, false);
								});
							}

							$scope.getParameterMasterList = function(orgId,orgUnitId, offset, noOfRecordsPerPage)
							{
								$rootScope.startLoader();
								var data = "";
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
								var URI = BASE_URL + ROOT_URL + LIS_UNIT + LIST_PARAMETER_MASTER + S + orgId + S +orgUnitId+S+ offset
								+ S + noOfRecordsPerPage;
								console.log("URI", URI);
								GenericService.serviceAction("GET", URI, data).then(function(response) {
									$rootScope.stopLoader();
									$scope.parameterList = response.data.listObject;
								});
							}

							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages)
								{
									return;
								}
								$scope.pager = PagerService.GetPager($scope.parameterMasterListCount, page, $scope.noOfRecordsPerPage);
								if (flag)
								{
									$scope.getParameterMasterList($scope.orgId,$scope.orgUnitId, $scope.pager.startIndex, $scope.pager.pageSize);
								}
							}

							$scope.initParameterMasterList($scope.orgId,$scope.orgUnitId, $scope.offset, $scope.noOfRecordsPerPage);

							// ====================================================CODE END FOR SINGLE PARAM LIST===========================================================
							
							
							
							/** CRUD FOR SINGLE PARAM TEST MASTER  **/
							//code for Activating Inactivating Parameter
							$scope.activeInactiveParameter = function(orgId,parameterId,status)
							{
								try {
									$rootScope.startLoader();
								/*	$scope.initParameter();*/
									var URI = BASE_URL + ROOT_URL + LIS_UNIT+ACTIVE_INACTIVE_PARAMETER_MASTER+S+orgId+S+parameterId+S+status;
									console.log("ACT_INACT_URI",URI);
									GenericService.serviceAction("GET", URI, $scope.parameterMaster).then(function(response) 
									{
										if (response.data.status == 'success')
										{
											$rootScope.stopLoader();
											growl.success(response.data.message,
													{
														title : 'Success!'
													});
											$scope.$broadcast('show-errors-reset');
											$scope.initMasters();
										} else if(response.data.status == 'error')
										{
											$rootScope.stopLoader();
											growl.error(response.data.message,
													{
														title : 'Error!'
													});
										}
									});
								} catch (e) {
									console.log("Exception",e.message);
								}
								
							}
							
							
							$scope.getParameterById = function(orgId,parameterId)
							{
								try {
									$rootScope.startLoader();
									$scope.updateScope = true;
									$scope.saveBtnFlag = false;
									$scope.updateBtnFlag = true;
									var URI = BASE_URL + ROOT_URL + LIS_UNIT+GET_PARAMETER_MASTER+S+$scope.orgId+S+$scope.orgUnitId+S+parameterId;
									console.log("EDIT",URI);
									GenericService.serviceAction("GET", URI, $scope.parameterMaster).then(function(response) 
									{
										console.log("data",response.data);
										if (response.data.status == 'success')
										{
											$rootScope.stopLoader();
											if(response.data.object.status == "I"){
												growl.warning("This entry is deactivated please active first then update.",
														{
															title : 'warning!'
														});
												$scope.saveBtnFlag = true;
												$scope.updateBtnFlag = false;
												$scope.updateScope = false;
												return false;
											}else{
											
											$scope.parameterMaster = response.data.object;
											$scope.parameterMaster.unitId =  $scope.parameterMaster.unitId+"";
											$scope.parameterMaster.parameterCode = $scope.parameterMaster.parameterCode+"";
											$scope.parameterMaster.aliesName = $scope.parameterMaster.aliesName+"";
											$scope.parameterMaster.parameterName = $scope.parameterMaster.parameterName+"";
											
											$scope.listParamRefrengMaster = response.data.object.listParamRefrengMaster;
											$scope.listParamTextualRangeMaster =response.data.object.listParamTextualRanageMaster;
											$scope.listParamMultiTextualRangeMaster = response.data.object.listParamMultiTextualRangeMaster;
											$scope.listHelpValueMaster= response.data.object.listHelpValueMaster;
											if(response.data.object.deltaDaysId != null || response.data.object.deltaPer != null ){
												$scope.checkDelta = 'Y';
											}
											
										 if($scope.parameterMaster.refrangeTypeId==TEXTUAL_RANGE)
											{
											 if($scope.listParamTextualRangeMaster==null)
												 {
												   $scope.listParamTextualRangeMaster.push(angular.copy($scope.paramTextualRangeMaster));
												 }
											 if($scope.listHelpValueMaster==null)
												{
												 $scope.listHelpValueMaster.push(angular.copy($scope.helpValueMaster));
												}
											}
										else if($scope.parameterMaster.efrangeTypeId==MULTIVALUED_RANGE)
											{
											if($scope.listParamMultiTextualRangeMaster==null)
												{
												  $scope.listParamMultiTextualRangeMaster.push(angular.copy($scope.paramMultiTextualRangeMaster));
												}
											if($scope.listHelpValueMaster==null)
												{
												 $scope.listHelpValueMaster.push(angular.copy($scope.helpValueMaster));
												}
											}
											console.log("$scope.parameterMaster",$scope.parameterMaster);
											}
										} else if(response.data.status == 'error')
										{
											$rootScope.stopLoader();
											alert("Error In Fetching Data");
										}
									});
								} catch (e) {
									console.log("Exception",e.message);
								}
								
							}
							
							
							$scope.saveParameter = function() 
							{
								$scope.$broadcast('show-errors-check-validity');
								if ($scope.parameterMasterForm.$valid)
								{
									try 
									{
										var checkvalidation = false;
										if($scope.parameterMaster.refrangeTypeId == REFERENCE_VALUE)
								    	   {
											
											 angular.forEach($scope.listParamRefrengMaster,function(val,idx){
												
												 if($scope.parameterMaster.genderReqiured != null && $scope.parameterMaster.genderReqiured == 'Y' &&
									    				   $scope.parameterMaster.ageGroupReqiured != null && $scope.parameterMaster.ageGroupReqiured == 'Y'){
									    			
									    			if(val.genderId == null || val.genderId == "" || val.ageGroupId == null || val.ageGroupId == ""){
									    				checkvalidation = true;
									    			}
									    			   
									    		   }else if($scope.parameterMaster.genderReqiured != null && $scope.parameterMaster.genderReqiured == 'Y'){
									    			  
										    			if(val.genderId == null || val.genderId == ""){
										    				checkvalidation = true;
										    			}
									    		   }else if($scope.parameterMaster.ageGroupReqiured != null && $scope.parameterMaster.ageGroupReqiured == 'Y'){
										    			if(val.ageGroupId == null || val.ageGroupId == ""){
										    				checkvalidation = true;
										    			}
									    		   }
				                			 });
											
								    	   }else if($scope.parameterMaster.refrangeTypeId == TEXTUAL_RANGE)
							    		   {
								    		   angular.forEach($scope.listParamTextualRangeMaster,function(val,idx){
								    			   if($scope.parameterMaster.genderReqiured != null && $scope.parameterMaster.genderReqiured == 'Y' &&
									    				   $scope.parameterMaster.ageGroupReqiured != null && $scope.parameterMaster.ageGroupReqiured == 'Y'){
									    			
								    				   if(val.genderId == null || val.genderId == "" || val.ageGroupId == null || val.ageGroupId == ""){
										    				checkvalidation = true;
										    			}
										    			   
										    		   }else if($scope.parameterMaster.genderReqiured != null && $scope.parameterMaster.genderReqiured == 'Y'){
										    			  
											    			if(val.genderId == null || val.genderId == ""){
											    				checkvalidation = true;
											    			}
										    		   }else if($scope.parameterMaster.ageGroupReqiured != null && $scope.parameterMaster.ageGroupReqiured == 'Y'){
											    			if(val.ageGroupId == null || val.ageGroupId == ""){
											    				checkvalidation = true;
											    			}
										    		   }
								    		   });
								    		  
							    		   } if($scope.parameterMaster.refrangeTypeId == MULTIVALUED_RANGE)
							    		   {
							    			   angular.forEach($scope.listParamMultiTextualRangeMaster,function(val,idx){
							    				   
							    				   if($scope.parameterMaster.genderReqiured != null && $scope.parameterMaster.genderReqiured == 'Y' &&
									    				   $scope.parameterMaster.ageGroupReqiured != null && $scope.parameterMaster.ageGroupReqiured == 'Y'){
									    			
							    					   if(val.genderId == null || val.genderId == "" || val.ageGroupId == null || val.ageGroupId == ""){
										    				checkvalidation = true;
										    			}
										    			   
										    		   }else if($scope.parameterMaster.genderReqiured != null && $scope.parameterMaster.genderReqiured == 'Y'){
										    			  
											    			if(val.genderId == null  || val.genderId == ""){
											    				checkvalidation = true;
											    			}
										    		   }else if($scope.parameterMaster.ageGroupReqiured != null && $scope.parameterMaster.ageGroupReqiured == 'Y'){
											    			if(val.ageGroupId == null || val.ageGroupId == ""){
											    				checkvalidation = true;
											    			}
										    		   }
								    		   });
							    			  
							    		   }
										
							    		   if(checkvalidation == true){
							    			   
							    			   growl.warning("please enter all required field.",
														{
															title : 'warning!'
														});
												
												return false; 
							    		   }
										
										$rootScope.startLoader();
										$scope.parameterMaster.listParamRefrengMaster = $scope.listParamRefrengMaster;
										$scope.parameterMaster.listParamTextualRanageMaster = $scope.listParamTextualRangeMaster;
										$scope.parameterMaster.listParamMultiTextualRangeMaster =$scope.listParamMultiTextualRangeMaster;
										$scope.parameterMaster.listHelpValueMaster =$scope.listHelpValueMaster;
										console.log("parameterMaster",JSON.stringify($scope.parameterMaster));
										var URI = BASE_URL + ROOT_URL + LIS_UNIT+ADD_PARAMETER_MASTER;
										GenericService.serviceAction("POST", URI, $scope.parameterMaster).then(function(response) 
										{	
											if (response.data.status == 'success')
											{
												$rootScope.stopLoader();
												growl.success(response.data.message,
														{
															title : 'Success!'
														});
												$scope.$broadcast('show-errors-reset');
												 $scope.initParameterMaster();
												$scope.initMasters();
												$scope.initParameter();
												$scope.initParameterMasterList($scope.orgId,$scope.orgUnitId, $scope.offset, $scope.noOfRecordsPerPage);
											} else if(response.data.status == 'error')
											{
												$rootScope.stopLoader();
												growl.error(response.data.message,
														{
															title : 'Error!'
														});
											}
										});
									} catch (e) {
										console.log("Exception",e.message);
									}
									
								}
							}
							$scope.updateParameter = function() 
							{
								$scope.$broadcast('show-errors-check-validity');
								if ($scope.parameterMasterForm.$valid)
								{
									try {
										
										var checkvalidation = false;
										if($scope.parameterMaster.refrangeTypeId == REFERENCE_VALUE)
								    	   {
											
											 angular.forEach($scope.listParamRefrengMaster,function(val,idx){

											     if($scope.parameterMaster.listHelpValueMaster.length>0){
                                                     $scope.parameterMaster.listHelpValueMaster = [];
                                                 }
												
												 if($scope.parameterMaster.genderReqiured != null && $scope.parameterMaster.genderReqiured == 'Y' &&
									    				   $scope.parameterMaster.ageGroupReqiured != null && $scope.parameterMaster.ageGroupReqiured == 'Y'){
									    			
									    			if(val.genderId == null || val.genderId == "" || val.ageGroupId == null || val.ageGroupId == ""){
									    				checkvalidation = true;
									    			}
									    			   
									    		   }else if($scope.parameterMaster.genderReqiured != null && $scope.parameterMaster.genderReqiured == 'Y'){
									    			  
										    			if(val.genderId == null  || val.genderId == ""){
										    				checkvalidation = true;
										    			}
									    		   }else if($scope.parameterMaster.ageGroupReqiured != null && $scope.parameterMaster.ageGroupReqiured == 'Y'){
										    			if(val.ageGroupId == null || val.ageGroupId == ""){
										    				checkvalidation = true;
										    			}
									    		   }
				                			 });
											
								    	   }else if($scope.parameterMaster.refrangeTypeId == 4){
								    	        if($scope.parameterMaster.listHelpValueMaster.length>0){
                                                     $scope.parameterMaster.listHelpValueMaster = [];
                                                 }
								    	   }else if($scope.parameterMaster.refrangeTypeId == TEXTUAL_RANGE)
							    		   {
								    		   angular.forEach($scope.listParamTextualRangeMaster,function(val,idx){
								    			   if($scope.parameterMaster.genderReqiured != null && $scope.parameterMaster.genderReqiured == 'Y' &&
									    				   $scope.parameterMaster.ageGroupReqiured != null && $scope.parameterMaster.ageGroupReqiured == 'Y'){
									    			
								    				   if(val.genderId == null || val.genderId == "" || val.ageGroupId == null || val.ageGroupId == ""){
										    				checkvalidation = true;
										    			}
										    			   
										    		   }else if($scope.parameterMaster.genderReqiured != null && $scope.parameterMaster.genderReqiured == 'Y'){
										    			  
											    			if(val.genderId == null  || val.genderId == ""){
											    				checkvalidation = true;
											    			}
										    		   }else if($scope.parameterMaster.ageGroupReqiured != null && $scope.parameterMaster.ageGroupReqiured == 'Y'){
											    			if(val.ageGroupId == null || val.ageGroupId == ""){
											    				checkvalidation = true;
											    			}
										    		   }
								    		   });
								    		  
							    		   } if($scope.parameterMaster.refrangeTypeId == MULTIVALUED_RANGE)
							    		   {
							    			   angular.forEach($scope.listParamMultiTextualRangeMaster,function(val,idx){
							    				   
							    				   if($scope.parameterMaster.genderReqiured != null && $scope.parameterMaster.genderReqiured == 'Y' &&
									    				   $scope.parameterMaster.ageGroupReqiured != null && $scope.parameterMaster.ageGroupReqiured == 'Y'){
									    			
							    					   if(val.genderId == null || val.genderId == "" || val.ageGroupId == null || val.ageGroupId == ""){
										    				checkvalidation = true;
										    			}
										    			   
										    		   }else if($scope.parameterMaster.genderReqiured != null && $scope.parameterMaster.genderReqiured == 'Y'){
										    			  
											    			if(val.genderId == null || val.genderId == ""){
											    				checkvalidation = true;
											    			}
										    		   }else if($scope.parameterMaster.ageGroupReqiured != null && $scope.parameterMaster.ageGroupReqiured == 'Y'){
											    			if(val.ageGroupId == null || val.ageGroupId == ""){
											    				checkvalidation = true;
											    			}
										    		   }
								    		   });
							    			  
							    		   }
										
							    		   if(checkvalidation == true){
							    			   
							    			   growl.warning("Please enter all required field.",
														{
															title : 'warning!'
														});
												
												return false; 
							    		   }
										
										
										$rootScope.startLoader();
										if($scope.parameterMaster.refrangeTypeId==REFERENCE_VALUE)
										{
											$scope.parameterMaster.listParamRefrengMaster = $scope.listParamRefrengMaster.concat($scope.refRengeList);
										}
									else if($scope.parameterMaster.refrangeTypeId==TEXTUAL_RANGE)
										{
										$scope.parameterMaster.listParamTextualRanageMaster = $scope.listParamTextualRangeMaster.concat($scope.refRengeTextualList);
										}
									else if($scope.parameterMaster.refrangeTypeId==MULTIVALUED_RANGE)
										{
										  $scope.parameterMaster.listParamMultiTextualRangeMaster =$scope.listParamMultiTextualRangeMaster.concat($scope.refRengeMultiTextualList);
										}

										if($scope.parameterMaster.refrangeTypeId != REFERENCE_VALUE && $scope.parameterMaster.refrangeTypeId != 4){
										    $scope.parameterMaster.listHelpValueMaster = $scope.listHelpValueMaster.concat($scope.helpValueList);
										}
										console.log("parameterMaster",JSON.stringify($scope.parameterMaster));
										var URI = BASE_URL + ROOT_URL + LIS_UNIT+UPDATE_PARAMETER_MASTER;
										GenericService.serviceAction("PUT", URI, $scope.parameterMaster).then(function(response) 
										{
											if (response.data.status == 'success')
											{
												$rootScope.stopLoader();
												growl.success(response.data.message,
														{
															title : 'Success!'
														});
												$scope.$broadcast('show-errors-reset');
												$scope.initMasters();
												$scope.initParameter();
												 $scope.initParameterMaster();
												$scope.initParameterMasterList($scope.orgId,$scope.orgUnitId, $scope.offset, $scope.noOfRecordsPerPage);
                                                $scope.saveBtnFlag = true;
                                                $scope.updateBtnFlag = false;
                                                $scope.updateScope = false;
											} else if(response.data.status == 'error')
											{
												$rootScope.stopLoader();
												growl.error(response.data.message,
														{
															title : 'Error!'
														});
											}
										});
									} catch (e) 
									{
										console.log("Exception",e.message);
									}
									
								}
							}
							
							 $scope.checkDeltaClick=function()
							{
								 if($scope.checkDelta == 'Y'){
									 $scope.deltaNo = true;
							            $scope.deltaText = true;
								 }else{
									 $scope.deltaNo = false;
							            $scope.deltaText = false;
								 }
							}
							 
				$scope.serachParameterMaster = function(parameterName){
					$scope.singleParamTestList=[];
					$scope.loadingMsg="Loading...";
					if(parameterName!=''){
					var URI = BASE_URL + ROOT_URL +"/LIS/unit/parameterMasterUniversalSearch"
					+ S + $scope.organizationId
					+ S + $scope.unitId
					+ S + $scope.offset
					+ S + $scope.noOfRecordsPerPage 
					+ S + parameterName ;
					console.log("URI", URI);
					GenericService.serviceAction(METHOD_GET, URI, "").then(function(response) {
										$rootScope.stopLoader();
										$scope.parameterList = response.data.listObject;
										if($scope.parameterList.length==0)
											$scope.loadingMsg="No record found...";
									});
					}else{
						$scope.initParameterMasterList($scope.orgId,$scope.orgUnitId, $scope.offset, $scope.noOfRecordsPerPage);
					}
				}
				
				$scope.selectIsDefault = function(index){
					angular.forEach($scope.listHelpValueMaster ,function(value,idx){
						if(index!=idx){
							value.isDefault=false;
						}
					})
				}
				
                $scope.getMachineList = function() {
                    var data = {
                               "organisationId" : $scope.orgId,
                               "unitId" : $scope.orgUnitId,
                           }
                       var URI = BASE_URL + ROOT_URL + LIS_GLOBAL+ GETMACHINELIST;
                       GenericService.serviceAction("POST", URI, data).then(
                                       function(response) {
                                           $scope.machineList = [];
                                           if (response.data.status == "success") {
                                               $scope.machineList = response.data.listObject;
                                           }
                                       });

                   }
                 $scope.getMachineList();
                 
                 $scope.techniqueList = [];
             	$scope.getTechniqueList = function(orgId) {
					try {
						var data = "";
						var URI = BASE_URL + ROOT_URL+ LIS_COMMON + GET_TECHNIQUE_LIST+ S + orgId;
						GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											$scope.techniqueList = response.data.listObject;
										});
					} catch (e) {
						console.log(e.message)
					}
				}
				$scope.getTechniqueList($scope.orgId);
                 
                 $scope.checckMachineWisePanicValues = function(obj){
                	 if(obj.isMachineWisePanicValues == 'N'){
                		 if(obj.refrangeTypeId == 1){
                			 angular.forEach($scope.listParamRefrengMaster,function(val,idx){
                				 val.machineId = null;
                			 });
                		 }else if(obj.refrangeTypeId == 2){
                			 angular.forEach($scope.listParamTextualRangeMaster,function(val,idx){
                				 val.machineId = null;
                			 });
                		 }else if(obj.refrangeTypeId == 3){
                			 angular.forEach($scope.listParamMultiTextualRangeMaster,function(val,idx){
                				 val.machineId = null;
                			 });
                		 }
                	 }
                 }
	/**=============================>Errors Messages  <=======================================**/
		$scope.parameterNameReq = 'Parameter name is required.';
		$scope.parameterCodeReq = 'Parameter code is required.';
		$scope.parameterAliesReq = 'Alies name is required.';
		$scope.minValReq = 'Required Min Val.';
		$scope.maxValReq = 'Required Max Val.';
		$scope.lessValReq = 'Required Less Val.';
		$scope.moreValReq = 'Required More Val.';
		$scope.helpValueReq = 'Required Help Val.';
		$scope.remarkValReq = 'Required Remark Val.';
		$scope.codeMaxLengthReq = 'Maxmimum 10 character are allowed .';
		$scope.nameMaxLengthReq = 'Maxmimum 15 character are allowed .';
		$scope.fromReq = 'From is Required.';
		$scope.toReq = 'To is Required.';
		$scope.dayReq = 'Please Select Day.';
		$scope.deltaPer = 'Required delta no in %.';
		
		$scope.plzSelectUnit = 'Please Select Unit.';
		$scope.plzSelectAgeGroup = 'Please Select AgeGroup.';
		$scope.plzSelectSex='Please Select Sex.';
		$scope.refRangeType = 'Please Select Reference Range.';
		$scope.precision = 'Please Select Precision.';
						} ]);