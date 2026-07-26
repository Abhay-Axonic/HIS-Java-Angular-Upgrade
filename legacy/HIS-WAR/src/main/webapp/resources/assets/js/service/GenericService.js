
angular.module('myApp').service('GenericService', [ '$http','$rootScope','$cookies','$state','growl', function($http,$rootScope,$cookies,$state,growl) {
	let paymentObj = {};
	this.serviceAction = function(httpVerb, url, obj) {
				var cookieObject = $cookies.getObject("cookieObject");
				if (cookieObject != undefined) {
					var authToken = cookieObject.authToken;
					var userId = cookieObject.userId;
					var unitId = cookieObject.unitId;
				}
				var headerObj = {
					"Authorization" : "Bearer "+authToken,
					"userId" : userId,
					"unitId" : unitId
				}
				var httpObject = {
					method : httpVerb,
					url : url,
					data : obj,
					headers : headerObj
				};
				var promise = $http(httpObject).then(
			            function (response) {
			                return response;
			            },
			            function (error) {
			                if (error.status === 401) {
			                    // Handle 401 Unauthorized - Perform logout or other actions
			                    // For example, you can clear authentication data and redirect to a login page
			                    // Assuming you have a logout function in your root scope
			                	$state.go('login');
			                	growl
								.error(
										"Token Expired !",
										{
											title : ERROR_MSG
										});
			                } else {
			                	$rootScope.stopLoader();
			                	growl
								.error(
										(error.data && (error.data.message || error.data.summary)) || NETWORK_ERROR_MSG,
										{
											title : ERROR_MSG
										});
			                }
			                return Promise.reject(error);
			            }
			        );
				return promise;
			};

	this.serviceActionForSubscription = (httpVerb, url, obj) =>{
		var cookieObject = $cookies.getObject("cookieObject");
		if (cookieObject != undefined) {
			var authToken = cookieObject.authToken;
			var userId = cookieObject.userId;
			var unitId = cookieObject.unitId;
		}
		var headerObj = {
			"Authorization" : "Bearer "+authToken,
			"userId" : userId,
			"unitId" : unitId
		}
		var httpObject = {
			method : httpVerb,
			url : url,
			data : obj,
			headers : headerObj
		};
					var promise = $http(httpObject).then(
						(response) =>
						{
							if(response.data.code==='SUB_5000'){
								$rootScope.subMsg = response.data.message;
								$rootScope.stopLoader();
								this.paymentObj = response.data.resObject;
								$rootScope.paymentObj = response.data.resObject;
								angular.element('#paymentSubscriptionModal').modal('show')
								return Promise.reject();
							}
							else if(response.data.code==='SUB_5001' || response.data.code=='SUB_5002'){
								this.paymentObj = response.data.resObject;
								$rootScope.stopLoader()
							}
							return response;
						},
						(error) => {
							if (error.status === 401) {
								$state.go('login');
								growl
									.error(
										"Token Expired !",
										{
											title : ERROR_MSG
										});
							}
							return Promise.reject(error);
						}
					);
				return promise;
	        };

	this.subScriptionPay = () =>{
		$rootScope.startLoaderPopup();
		let url= BASE_URL + ROOT_URL + "/getOrderIdUnitSubscriptionPay";
		let data = {
			unitId : this.paymentObj.unitId,
			organizationId : this.paymentObj.organizationId,
			amount:this.paymentObj.subscriptionNetAmount,
			razorPayBillAmount:this.paymentObj.subscriptionNetAmount+"00",
		}
		let orderId = null;
		var httpObject = {
			method : METHOD_POST,
			url : url,
			data : data
		};
		var promise = $http(httpObject).then(
			(response) =>
			{
				if (response.data.status == "success")
				{
					orderId= response.data.object.orderId;
					var options = {
						"key": response.data.resObject,
						"amount": this.paymentObj.subscriptionNetAmount+"00",
						"currency": "INR",
						"name": "Axonic Healthcare",
						"description": "Test Transaction",
						"image": "https://example.com/your_logo",
						"order_id": orderId,
						"callback_url": BASE_URL + ROOT_URL + "/unit/subcription",
						"prefill": {
							"name": this.paymentObj.unitName,
							"email": this.paymentObj.unitEmailId,
							"contact": this.paymentObj.unitContact
						},
						"notes": {
							"address": "Razorpay Corporate Office"
						},
						"theme": {
							"color": "#3399cc"
						}
					};
					var rzp1 = new Razorpay(options);
					rzp1.open();
					$rootScope.stopLoaderPopup();
				}else{
					$rootScope.stopLoaderPopup();
					growl.error(response.data.message,
						{
							title : 'Error!'
						});
				}


			},
			(error) => {
				$rootScope.stopLoaderPopup();
				return Promise.reject(error);
			}
		);
		return promise;
	  };


           }
	   ]);


		angular.module('myApp').service(
		'GenericServiceForWithoutLogin',
		[
				'$http',
				'$rootScope',
				'$cookies',
				'$state',
				'growl',
				function($http, $rootScope, $cookies, $state, growl) {

					this.serviceAction = function(httpVerb, url, obj) {
						var cookieObject = $cookies.getObject("cookieObject");
						if (cookieObject != undefined) {
							var authToken = cookieObject.authToken;
							var userId = cookieObject.userId;
							var unitId = cookieObject.unitId;
						}
						var headerObj = {
							"Authorization" : "Bearer " + authToken,
							"userId" : userId,
							"unitId" : unitId
						}
						var httpObject = {
							method : httpVerb,
							url : url,
							data : obj,
							headers : headerObj
						};
						var promise = $http(httpObject).then(
								function(response) {
									return response;
								},
					            function (error) {
					                if (error.status === 401) {
					                    // Handle 401 Unauthorized - Perform logout or other actions
					                    // For example, you can clear authentication data and redirect to a login page
					                    // Assuming you have a logout function in your root scope
					                	$state.go('login');
					                	growl
										.error(
												"Token Expired !",
												{
													title : ERROR_MSG
												});
					                } else {
					                	$rootScope.stopLoader();
					                	growl
									.error(
											(error.data && (error.data.message || error.data.summary)) || NETWORK_ERROR_MSG,
											{
												title : ERROR_MSG
											});
					                }
					                return Promise.reject(error);
					            });
						return promise;
					};

				}]);



		angular.module('myApp').service(
		'GenericServiceFileUpload',
				[
						'$http',
						'$rootScope',
						'$cookies',
						'$state',
						'growl',
						function($http, $rootScope, $cookies, $state, growl) {

							this.serviceAction = function(httpVerb, url, obj,headerObj) {

								var cookieObject = $cookies.getObject("cookieObject");
								if (cookieObject != undefined) {
									var authToken = cookieObject.authToken;
									var userId = cookieObject.userId;
									var unitId = cookieObject.unitId;
								}
//								var headerObj = {
//										"Authorization" : "Bearer " + authToken,
//										"userId" : userId,
//										"unitId" : unitId
//									}
                                     headerObj.Authorization= "Bearer " + authToken;
                                     headerObj.userId=userId;
                                     headerObj.unitId=unitId;

								var httpObject = {
									method : httpVerb,
									url : url,
									data : obj,
									headers : headerObj,
									transformRequest : angular.identity
								};
								// console.log(url,JSON.stringify(obj));
								var promise = $http(httpObject).then(
							            function (response) {
							                return response;
							            },
							            function (error) {
							                if (error.status === 401) {
							                    // Handle 401 Unauthorized - Perform logout or other actions
							                    // For example, you can clear authentication data and redirect to a login page
							                    // Assuming you have a logout function in your root scope
							                	$state.go('login');
							                	growl
												.error(
														"Token Expired !",
														{
															title : ERROR_MSG
														});
							                }
							                return Promise.reject(error);
							            }
							        );
								return promise;

							};

						} ]);


angular
		.module('myApp')
	.service(
			'GenericServiceParamHeader',
			[
					'$http',
					'$rootScope',
					'growl',
					'$cookies',
					'$state',
					function($http, $rootScope, growl, $cookies, $state) {

						this.serviceAction = function(httpVerb, url, obj,
								notificationStatus, paramObj, headerObj) {

							var cookieObject = $cookies
									.getObject("cookieObject");
							if (cookieObject != undefined) {
								var authToken = cookieObject.authToken;
								var userId = cookieObject.userId;
								var unitId = cookieObject.unitId;
							}
							
							if(headerObj==""||headerObj==undefined||headerObj==null){
								var headerObj = {};
								headerObj.Authorization="Bearer "+$cookies.getObject("cookieObject").authToken;
							}
							else{
								headerObj.Authorization="Bearer "+$cookies.getObject("cookieObject").authToken;
							}
							
							headerObj.userId = userId,
							headerObj.unitId = unitId
							var httpObject = {
								method : httpVerb,
								url : url,
								data : obj,
								params : paramObj,
								headers : headerObj
							};
							// console.log(url,JSON.stringify(obj));
							var promise = $http(httpObject)
									.then(
											function(response) {
												if (response.data.status == 'success') {
													if (notificationStatus == true)
														growl
																.success(
																		response.data.message,
																		{
																			title : SUCCESS_MSG
																		});
												} else if (response.data.status == 'error') {
													if (response.data.code == 409) {
														growl
																.warning(
																		response.data.message,
																		{
																			title : WARNING_MSG
																		});
													} else {
														growl
																.error(
																		response.data.message,
																		{
																			title : ERROR_MSG
																		});
													}
												}
												$rootScope.stopLoader();
												return response;

											},
											function(e) {
												
												if (error.status === 401) {
								                    // Handle 401 Unauthorized - Perform logout or other actions
								                    // For example, you can clear authentication data and redirect to a login page
								                    // Assuming you have a logout function in your root scope
								                	$state.go('login');
								                	growl
													.error(
															"Token Expired !",
															{
																title : ERROR_MSG
															});
								                }else{
												
												$rootScope.stopLoader();
												growl
														.error(
																NETWORK_ERROR_MSG,
																{
																	title : ERROR_MSG
																});
								                }
											});
							return promise;

						};

					} ]);


angular
		.module('myApp')
		.service(
				'GenericServiceParamHeaderWithouLogin',
				[
						'$http',
						'$rootScope',
						'growl',
						'$cookies',
						'$state',
						function($http, $rootScope, growl, $cookies, $state) {

							this.serviceAction = function(httpVerb, url, obj,
									notificationStatus, paramObj, headerObj) {

								var cookieObject = $cookies
										.getObject("cookieObject");
								if (cookieObject != undefined) {
									var authToken = cookieObject.authToken;
									var userId = cookieObject.userId;
									var unitId = cookieObject.unitId;
								}
								var headerObj = {
									"Authorization" : "Bearer " + authToken,
									"userId" : userId,
									"unitId" : unitId,
									"organizationId" : headerObj.organizationId
								}
								
								var httpObject = {
									method : httpVerb,
									url : url,
									data : obj,
									params : paramObj,
									headers : headerObj
								};
								// console.log(url,JSON.stringify(obj));
								var promise = $http(httpObject)
										.then(
												function(response) {
													if (response.data.status == 'success') {
														if (notificationStatus == true)
															growl
																	.success(
																			response.data.message,
																			{
																				title : SUCCESS_MSG
																			});
													} else if (response.data.status == 'error') {
														if (response.data.code == 409) {
															growl
																	.warning(
																			response.data.message,
																			{
																				title : WARNING_MSG
																			});
														} else {
															growl
																	.error(
																			response.data.message,
																			{
																				title : ERROR_MSG
																			});
														}
													}
													$rootScope.stopLoader();
													return response;

												},
												function(e) {
													$rootScope.stopLoader();
													if (error.status === 401) {
									                    // Handle 401 Unauthorized - Perform logout or other actions
									                    // For example, you can clear authentication data and redirect to a login page
									                    // Assuming you have a logout function in your root scope
									                	$state.go('login');
									                	growl
														.error(
																"Token Expired !",
																{
																	title : ERROR_MSG
																});
									                }else{
													growl
															.error(
																	NETWORK_ERROR_MSG,
																	{
																		title : ERROR_MSG
																	});
									                }
												});
								return promise;

							};

						} ]);

angular
		.module('myApp')
		.service(
				'BillingGenericService',
				[
						'$http',
						'$rootScope',
						'growl',
						'$cookies',
						'$state',
						function($http, $rootScope, growl, $cookies, $state) {

							this.serviceAction = function(httpVerb, url, obj,
									notificationStatus, responseType) {

								var cookieObject = $cookies
										.getObject("cookieObject");
								if (cookieObject != undefined) {
									var authToken = cookieObject.authToken;
									var userId = cookieObject.userId;
									var unitId = cookieObject.unitId;
								}
								var headerObj = {
									"Authorization" : "Bearer " + authToken,
									"userId" : userId,
									"unitId" : unitId
								}
								var httpObject = {
									method : httpVerb,
									url : url,
									data : obj,
									headers : headerObj
								};
								if (typeof responseType !== 'undefined'
										&& responseType !== null) {
									httpObject['responseType'] = responseType
								}
								// console.log(url,JSON.stringify(obj));
								var promise = $http(httpObject)
										.then(
												function(response) {
													if (response.data.status == 'success') {
														if (notificationStatus == true
																&& response.data.message != null)
															growl
																	.success(
																			response.data.message,
																			{
																				title : SUCCESS_MSG
																			});
													} else if (response.data.status == 'error') {
														if (notificationStatus == true) {
															if (response.data.code == 409) {
																growl
																		.warning(
																				response.data.message,
																				{
																					title : WARNING_MSG
																				});
															} else {
																growl
																		.error(
																				response.data.message,
																				{
																					title : ERROR_MSG
																				});
															}
														}
													}
													$rootScope.stopLoader();
													return response;

												},
												function(e) {
													$rootScope.stopLoader();
													if (error.status === 401) {
									                    // Handle 401 Unauthorized - Perform logout or other actions
									                    // For example, you can clear authentication data and redirect to a login page
									                    // Assuming you have a logout function in your root scope
									                	$state.go('login');
									                	growl
														.error(
																"Token Expired !",
																{
																	title : ERROR_MSG
																});
									                }else{
													
													growl
															.error(
																	NETWORK_ERROR_MSG,
																	{
																		title : ERROR_MSG
																	});
									                }
												});
								return promise;

							};
						} ]);

angular.module('myApp').service(
		'GenericServiceForFormsEngine',
		['$http',
				'$rootScope',
				'$state',
				'$cookies',
				'growl',
				function($http, $rootScope,$state,$cookies,growl) {

					this.serviceAction = function(httpVerb, url, obj, key,
							responseType) {

						var cookieObject = $cookies.getObject("cookieObject");
						if (cookieObject != undefined) {
							var authToken = cookieObject.authToken;
							var userId = cookieObject.userId;
							var unitId = cookieObject.unitId;
						}
						var headerObj = {
							"Authorization" : "Bearer " + authToken,
							"userId" : userId,
							"unitId" : unitId,
							"X-Api-Key" : key
						}

						var httpObject = {
							method : httpVerb,
							url : url,
							data : obj,
							headers : headerObj
						};

						if (typeof responseType !== 'undefined'
								&& responseType !== null) {
							httpObject['responseType'] = responseType
						}
						// console.log(url,JSON.stringify(obj));
						var promise = $http(httpObject).then(
								function(response) {

									return response;

								},
								function(e) {
									$rootScope.stopLoader();
									if (error.status === 401) {
					                    // Handle 401 Unauthorized - Perform logout or other actions
					                    // For example, you can clear authentication data and redirect to a login page
					                    // Assuming you have a logout function in your root scope
					                	$state.go('login');
					                	growl
										.error(
												"Token Expired !",
												{
													title : ERROR_MSG
												});
					                }else{
									
									growl
											.error(
													NETWORK_ERROR_MSG,
													{
														title : ERROR_MSG
													});
					                }
								});
						return promise;
					};

				} ]);

angular.module('myApp').service(
		'reportService',
		[
				'$http',
				'$rootScope',
				'$cookies',
				function($http, $rootScope, $cookies) {

					this.generate = function(URI, data) {

						var elementId = document.getElementById('reportId');
						if (elementId != null) {
							document.body.removeChild(elementId);
						}

						var cookieObject = $cookies.getObject('cookieObject');
						var orgUnitId = cookieObject.unitId;
						var orgId = cookieObject.organizationId;

						var URI = URI;
						var form = null;

						form = document.createElement("form");
						form.setAttribute("id", "reportId");
						form.setAttribute("name", "formReport");
						form.setAttribute("method", "post");
						form.setAttribute("action", URI);
						form.setAttribute("target", "_blank");

						var hiddenField1 = document.createElement("input");
						hiddenField1.setAttribute("type", "hidden");
						hiddenField1.setAttribute("name", "reportParam");
						hiddenField1
								.setAttribute("value", JSON.stringify(data));

						var hiddenField2 = document.createElement("input");
						hiddenField2.setAttribute("type", "hidden");
						hiddenField2.setAttribute("name", "orgId");
						hiddenField2.setAttribute("value", orgId);

						var hiddenField3 = document.createElement("input");
						hiddenField3.setAttribute("type", "hidden");
						hiddenField3.setAttribute("name", "unitId");
						hiddenField3.setAttribute("value", orgUnitId);

						form.appendChild(hiddenField1);
						form.appendChild(hiddenField2);
						form.appendChild(hiddenField3);

						document.body.appendChild(form);
						$("form[name='formReport']").submit();
						// $('form').submit();
						return true;

					};

				} ]);

angular.module('myApp').service('dataService',
		[ '$http', '$rootScope', function($http, $rootScope) {
			this.procReqId = 0;
			this.otimsId = 0;
			this.preAuthReqObjWithData = {};
			this.addNewRequestListByPayeeId = [];
			this.addendumBill = {};// static till impl
		} ]);

angular.module('myApp').service('pharamcyContractService',
		[ '$http', '$rootScope', function($http, $rootScope) {
			this.billObj = {};
		} ]);