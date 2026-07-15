angular.module("myApp")
    .directive('numbersOnly', function () {
      return {
          require: 'ngModel',
          link: function (scope, element, attr, ngModelCtrl) {
              function fromUser(text) {
                  if (text) {
                      var transformedInput = text.replace(/[^0-9]/g, '');
                	  //var transformedInput = text.replace(/^[0-9]+(\.[0-9]{1,2})?$/gm, '');
                	 
                      if (transformedInput !== text) {
                          ngModelCtrl.$setViewValue(transformedInput);
                          ngModelCtrl.$render();
                      }
                      return transformedInput;
                  }
                  return undefined;
              }            
              ngModelCtrl.$parsers.push(fromUser);
          }
      };
  });


angular.module("myApp")
.directive('loyaltyCard', function () {
  return {
      require: 'ngModel',
      link: function (scope, element, attr, ngModelCtrl) {
          function fromUser(text) {
              if (text) {
                  var transformedInput = text.replace(/[^0-9]{16}/g, '');
            	  //var transformedInput = text.replace(/^[0-9]+(\.[0-9]{1,2})?$/gm, '');
            	 
                  if (transformedInput !== text) {
                      ngModelCtrl.$setViewValue(transformedInput);
                      ngModelCtrl.$render();
                  }
                  return transformedInput;
              }
              return undefined;
          }            
          ngModelCtrl.$parsers.push(fromUser);
      }
  };
});
angular.module("myApp").directive('ckEditor', function() {
	  return {
		    require: '?ngModel',
		    link: function(scope, elm, attr, ngModel) {
		      var ck = CKEDITOR.replace(elm[0]);
		      //ck.contentsCss = './resources/assets/css/template/drag-drop.css';
		      if (!ngModel) return;

		      ck.on('pasteState', function() {
		        scope.$apply(function() {
		          ngModel.$setViewValue(ck.getData());
		        });
		      });

		      ngModel.$render = function(value) {
		        ck.setData(ngModel.$viewValue);
		      };
		    }
		  };
		});

angular.module("myApp").directive('ckEditorField', function() {
	  return {
		    require: '?ngModel',
		    link: function(scope, elm, attr, ngModel) 
		    {
		    	var idCounter = 0;
			    var pClass = 'myCustomClass';
			    var ClassRegexp = new RegExp( pClass, 'g' );
		    	/*var basic = [
					  ['Bold', 'Italic','Underline', '-', 'NumberedList', 'BulletedList', '-', 'Link', 
						  'Unlink', '-', 'Image','Table','Styles','Forms','Paragraph','Insert',' uiColor','Maximize','About']
					];*/
		    	
			    var basic = [
					  ['Bold', 'Italic','Underline', '-', 'NumberedList', 'BulletedList', '-', 'Link', 
						  'Unlink', '-', 'Image','Table','Styles','Forms','Paragraph','Insert',' uiColor','Maximize','About']
					];
			    
		    	var ck = CKEDITOR.replace(elm[0],
		    		{
					  toolbar: basic, 
					  on: {
					        instanceReady: function() {
					            this.dataProcessor.htmlFilter.addRules({
					                elements: {
					                    p: function( element ) {
					                        // If there's no class, assing the custom one:
					                        if ( !element.attributes.class )
					                            element.attributes.class = pClass;

					                        // It there's some other class, append the custom one:
					                        else if ( !element.attributes.class.match( pClassRegexp ) )
					                            element.attributes.class += ' ' + pClass;

					                        // Add the subsequent id:
					                        if ( !element.attributes.id )
					                            element.attributes.id = 'paragraph_' + ++idCounter;
					                    }
					                }
					            });
					        }
		    	
					    }
					});
		  
		    	
		      if (!ngModel) return;

		      ck.on('pasteState', function() {
		        scope.$apply(function() {
		          ngModel.$setViewValue(ck.getData());
		        });
		      });

		      ngModel.$render = function(value) {
		        ck.setData(ngModel.$viewValue);
		      };
		      
		  
		    }
	  
		  };
		});

angular.module("myApp").directive('ngToggle', function($timeout) {
    return {
        restrict: 'E',
        replace: true,
        scope: {
            config: '=?',
            ngChange: '&?',
            ngToggleChange: '&?',
            val: '=ngModel',
            ngDefault: '=?',
            ngTrueVal: '=?',
            ngFalseVal: '=?',
            ngNullVal: '=?',
            ngWidth: '=?',
            ngHeight: '=?',
            ngTrueTip: '=?',
            ngFalseTip: '=?',
            ngNullTip: '=?',
        },
        template: '<div class="ng-toggle-wrap" ng-style="styleWrap" ng-class="{\'vertical\': vertical}"><div class="ng-toggle-switch" ng-style="styleSwitch" ng-class="{\'true\': val==ngTrueVal, \'false\': val==ngFalseVal}" ng-click="toggle()"><div class="ng-toggle-false"></div><div class="ng-toggle-true"></div><div class="ng-toggle-handle" ng-style="styleHandle"></div></div><div class="ng-toggle-tooltip" ng-show="tooltip"><span ng-class="{\'active\': showTooltip1}"><span ng-show="tooltip1">&bull;&nbsp;</span>{{tooltip1}}&nbsp;</span><br ng-show="triToggle"><span ng-class="{\'active\': showTooltip2}" ng-show="triToggle"><span ng-show="tooltip2">&bull;&nbsp;</span>{{tooltip2}}&nbsp;</span><br><span ng-class="{\'active\': showTooltip3}"><span ng-show="tooltip3">&bull;&nbsp;</span>{{tooltip3}}&nbsp;</span></div></div>',
        link: function(scope, element, attrs) {

            /* Catch Config, allow attr overrides */

            if (angular.isDefined(scope.config)) {

                /* Tri-toggle Setting */

                if (angular.isDefined(scope.config.triToggle) && !angular.isDefined(attrs.triToggle)) {
                    if (scope.config.triToggle) {
                        attrs.triToggle = scope.config.triToggle;
                    }
                }

                /* ngChange Function */

                if (angular.isDefined(scope.config.change) && !angular.isDefined(scope.ngChange)) {
                    scope.ngChange = scope.config.change;
                }

                /* ngToggleChange Function */

                if (angular.isDefined(scope.config.toggleChange) && !angular.isDefined(scope.ngToggleChange)) {
                    scope.ngToggleChange = scope.config.toggleChange;
                }

                /* Model Binding */

                if (angular.isDefined(scope.config.val) && !angular.isDefined(scope.val)) {
                    scope.val = scope.config.val;
                }

                /* Default Value */

                if (angular.isDefined(scope.config.default) && !angular.isDefined(scope.ngDefault)) {
                    scope.ngDefault = scope.config.
                    default;
                }

                /* Values */

                if (angular.isDefined(scope.config.trueVal) && !angular.isDefined(scope.ngTrueVal)) {
                    scope.ngTrueVal = scope.config.trueVal;
                }
                if (angular.isDefined(scope.config.falseVal) && !angular.isDefined(scope.ngFalseVal)) {
                    scope.ngFalseVal = scope.config.falseVal;
                }
                if (angular.isDefined(scope.config.nullVal) && !angular.isDefined(scope.ngNullVal)) {
                    scope.ngNullVal = scope.config.nullVal;
                }

                /* Width & Height */

                if (angular.isDefined(scope.config.width) && !angular.isDefined(scope.ngWidth)) {
                    scope.ngWidth = scope.config.width;
                }
                if (angular.isDefined(scope.config.height) && !angular.isDefined(scope.ngHeight)) {
                    scope.ngHeight = scope.config.height;
                }

                /* Custom Colors */

                if (angular.isDefined(scope.config.falseColor) && !angular.isDefined(attrs.ngFalseColor)) {
                    attrs.ngFalseColor = scope.config.falseColor;
                }
                if (angular.isDefined(scope.config.nullColor) && !angular.isDefined(attrs.ngNullColor)) {
                    attrs.ngNullColor = scope.config.nullColor;
                }
                if (angular.isDefined(scope.config.trueColor) && !angular.isDefined(attrs.ngTrueColor)) {
                    attrs.ngTrueColor = scope.config.trueColor;
                }

                /* Vertical Setting */

                if (angular.isDefined(scope.config.vertical) && !angular.isDefined(attrs.vertical)) {
                    attrs.vertical = scope.config.vertical;
                }

                /* ToolTips */

                if (angular.isDefined(scope.config.trueTip) && !angular.isDefined(scope.ngTrueTip)) {
                    scope.ngTrueTip = scope.config.trueTip;
                }
                if (angular.isDefined(scope.config.falseTip) && !angular.isDefined(scope.ngFalseTip)) {
                    scope.ngFalseTip = scope.config.falseTip;
                }
                if (angular.isDefined(scope.config.nullTip) && !angular.isDefined(scope.ngNullTip)) {
                    scope.ngNullTip = scope.config.nullTip;
                }
                if (!angular.isDefined(scope.ngTrueTip) && !angular.isDefined(scope.ngFalseTip) && !angular.isDefined(scope.ngNullTip)) {
                    scope.tooltip = false;
                } else {
                    scope.tooltip = true;
                }
            }

            /* Default Styling */

            scope.triToggle = false;
            scope.vertical = false;
            scope.styleWrap = {};
            scope.styleSwitch = {};
            scope.styleHandle = {};

            /* Is TriToggle? */

            if (angular.isDefined(attrs.triToggle)) {
                scope.triToggle = true;
            }

            /* Custom Container Size */

            if (angular.isDefined(scope.ngWidth)) {
                scope.styleWrap['width'] = scope.ngWidth + 6 + 'px';
            }
            if (angular.isDefined(scope.ngHeight)) {
                scope.styleWrap['height'] = scope.ngHeight + 6 + 'px';
            }

            /* Custom Switch and Handle Size */

            if (angular.isDefined(attrs.vertical) && attrs.vertical) {

                scope.vertical = true;

                scope.tooltip1 = scope.ngTrueTip;
                scope.tooltip2 = scope.ngNullTip;
                scope.tooltip3 = scope.ngFalseTip;

                if (angular.isDefined(scope.ngWidth)) {
                    scope.styleSwitch['height'] = scope.ngWidth + 'px';
                    scope.styleHandle['height'] = scope.ngWidth - 6 + 'px';
                }
                if (angular.isDefined(scope.ngHeight)) {
                    scope.styleSwitch['width'] = scope.ngHeight + 'px';
                    scope.styleHandle['width'] = scope.ngHeight * 0.428 + 'px';
                }
            } else {

                scope.tooltip1 = scope.ngFalseTip;
                scope.tooltip2 = scope.ngNullTip;
                scope.tooltip3 = scope.ngTrueTip;

                if (angular.isDefined(scope.ngWidth)) {
                    scope.styleSwitch['width'] = scope.ngWidth + 'px';
                    scope.styleHandle['width'] = scope.ngWidth * 0.428 + 'px';
                }
                if (angular.isDefined(scope.ngHeight)) {
                    scope.styleSwitch['height'] = scope.ngHeight + 'px';
                    scope.styleHandle['height'] = scope.ngHeight - 6 + 'px';
                }
            }

            /* Custom Border Radii */

            if (angular.isDefined(scope.ngWidth) && angular.isDefined(scope.ngHeight)) {
                if (scope.ngWidth <= scope.ngHeight) {
                    scope.styleSwitch['border-radius'] = (scope.ngHeight + 6) / 2 + 'px';
                    scope.styleHandle['border-radius'] = scope.ngHeight / 2 + 'px';
                } else {
                    scope.styleSwitch['border-radius'] = (scope.ngWidth + 6) / 2 + 'px';
                    scope.styleHandle['border-radius'] = scope.ngWidth / 2 + 'px';
                }
            } else if (angular.isDefined(scope.ngWidth)) {
                if (scope.ngWidth <= 30) {
                    scope.styleSwitch['border-radius'] = 18 + 'px';
                    scope.styleHandle['border-radius'] = 15 + 'px';
                } else {
                    scope.styleSwitch['border-radius'] = (scope.ngWidth + 6) / 2 + 'px';
                    scope.styleHandle['border-radius'] = scope.ngWidth / 2 + 'px';
                }
            } else if (angular.isDefined(scope.ngHeight)) {
                if (scope.ngHeight >= 56) {
                    scope.styleSwitch['border-radius'] = 31 + 'px';
                    scope.styleHandle['border-radius'] = 28 + 'px';
                } else {
                    scope.styleSwitch['border-radius'] = (scope.ngHeight + 6) / 2 + 'px';
                    scope.styleHandle['border-radius'] = scope.ngHeight / 2 + 'px';
                }
            }

            /* Custom CSS Color Overrides */

            function updateCustomColor() {
                switch (scope.val) {
                    case scope.ngTrueVal:
                        if (angular.isDefined(attrs.ngTrueColor)) {
                            scope.styleSwitch['background-color'] = attrs.ngTrueColor;
                        }
                        break;
                    case scope.ngFalseVal:
                        if (angular.isDefined(attrs.ngFalseColor)) {
                            scope.styleSwitch['background-color'] = attrs.ngFalseColor;
                        }
                        break;
                    default:
                        if (scope.triToggle) {
                            if (angular.isDefined(attrs.ngNullColor)) {
                                scope.styleSwitch['background-color'] = attrs.ngNullColor;
                            }
                        }
                        break;
                }
            }

            /* Custom Position Maintenance */

            function updatePosition() {
                if (scope.val === scope.ngFalseVal) {
                    if (angular.isDefined(scope.ngWidth)) {
                        scope.styleHandle['left'] = 3 + 'px';
                    }
                } else if (scope.val === scope.ngNullVal) {
                    if (scope.triToggle) {
                        if (angular.isDefined(attrs.vertical) && attrs.vertical) {
                            if (angular.isDefined(scope.ngHeight)) {
                                scope.styleHandle['left'] = scope.ngHeight * 0.22 + 3 + 'px';
                            }
                        } else if (angular.isDefined(scope.ngWidth)) {
                            scope.styleHandle['left'] = scope.ngWidth * 0.22 + 3 + 'px';
                        }
                    }
                } else {
                    if (angular.isDefined(attrs.vertical) && attrs.vertical) {
                        if (angular.isDefined(scope.ngHeight)) {
                            scope.styleHandle['left'] = (scope.ngHeight) * 0.517 + 'px';
                        }
                    } else if (angular.isDefined(scope.ngWidth)) {
                        scope.styleHandle['left'] = (scope.ngWidth) * 0.517 + 'px';
                    }
                }
            }

            /* Custom Tooltip */

            function updateTooltip() {
                scope.showTooltip1 = false;
                scope.showTooltip2 = false;
                scope.showTooltip3 = false;
                if (scope.val === scope.ngTrueVal) {
                    if (scope.vertical) {
                        scope.showTooltip1 = true;
                    } else {
                        scope.showTooltip3 = true;
                    }
                } else if (scope.val === scope.ngFalseVal) {
                    if (scope.vertical) {
                        scope.showTooltip3 = true;
                    } else {
                        scope.showTooltip1 = true;
                    }
                } else {
                    if (scope.triToggle) {
                        scope.showTooltip2 = true;
                    }
                }
            }

            /* Logic */

            scope.toggle = function() {
                if (scope.val === scope.ngTrueVal) {
                    scope.val = scope.ngFalseVal;
                } else if (scope.val === scope.ngFalseVal && scope.triToggle) {
                    scope.val = scope.ngNullVal;
                } else {
                    scope.val = scope.ngTrueVal;
                }
                if (typeof scope.ngToggleChange != 'undefined') {
                    $timeout(function() {
                        scope.ngToggleChange(scope.val);
                    });
                }
            };

            scope.$watch('val', function() {
                updateCustomColor();
                updatePosition();
                updateTooltip();
                if (typeof scope.ngChange != 'undefined') {
                    $timeout(function() {
                        scope.ngChange(scope.val);
                    });
                }
            });

            /* Value Configuration */

            if (!angular.isDefined(scope.ngTrueVal)) {
                scope.ngTrueVal = 1;
            }
            if (!angular.isDefined(scope.ngFalseVal)) {
                scope.ngFalseVal = 0;
            }
            if (!angular.isDefined(scope.ngNullVal)) {
                scope.ngNullVal = null;
            }
            if (!angular.isDefined(scope.val)) {
                if (angular.isDefined(scope.ngDefault)) {
                    scope.val = scope.ngDefault;
                } else {
                    scope.val = scope.ngNullVal;
                }
            }
        },
    };
});


/**CODE ADDED BY GANESH*/
angular.module("myApp").directive('sbPrecision', function(){
	  return {
	    restrict: 'A',
	    require: 'ngModel',
	    link: function(scope, element, attributes, ngModel){
	      var precision = attributes.sbPrecision;
	      console.log('precision',precision);
	   
	      function countDecimalPlaces(value){
	          var decimalPos = String(value).indexOf('.');
	            if(decimalPos === -1)
	              {
	                return 0;
	              }else
	              {
	                return 2;
	              }
	            }
	      
	      function setPrecision(){
	        var value = ngModel.$modelValue;
	        
	        //since this is just a mask, don't hide decimal values that
	        //go beyond our precision and don't format empty values
	        if(value && !isNaN(value) && countDecimalPlaces(value) <= precision){
	          var formatted = parseFloat(value).toFixed(precision);
	          console.log('formatted',formatted);
	          ngModel.$viewValue = formatted;
	          ngModel.$render();
	        }
	      }
	      
	      element.bind('blur', setPrecision);
	      setTimeout(setPrecision, 0); //after initial page render
	    }
	    
	  };
	});
angular.module("myApp").directive('numbersNotZero', function(){
    return {
      require: 'ngModel',
      link: function(scope, element, attrs, modelCtrl) {
        modelCtrl.$parsers.push(function (inputValue) {


        if(parseInt(inputValue) > 0){
          modelCtrl.$setValidity('numbersNotZero', true);
          return inputValue;
        } else {
          modelCtrl.$setValidity('numbersNotZero', false);
          return modelCtrl.$modelValue;
        }

    });
  }
};
}); 


angular.module("myApp").directive('validNumber', function() {
    return {
      require: '?ngModel',
      link: function(scope, element, attrs, ngModelCtrl) {
        if(!ngModelCtrl) {
          return; 
        }

        ngModelCtrl.$parsers.push(function(val) {
          if (angular.isUndefined(val)) {
              var val = '';
          }
          
          var clean = val.replace(/[^-0-9\.]/g, '');
          var negativeCheck = clean.split('-');
			var decimalCheck = clean.split('.');
          if(!angular.isUndefined(negativeCheck[1])) {
              negativeCheck[1] = negativeCheck[1].slice(0, negativeCheck[1].length);
              clean =negativeCheck[0] + '-' + negativeCheck[1];
              if(negativeCheck[0].length > 0) {
              	clean =negativeCheck[0];
              }
              
          }
            
          if(!angular.isUndefined(decimalCheck[1])) {
              decimalCheck[1] = decimalCheck[1].slice(0,2);
              clean =decimalCheck[0] + '.' + decimalCheck[1];
          }

          if (val !== clean) {
            ngModelCtrl.$setViewValue(clean);
            ngModelCtrl.$render();
          }
          return clean;
        });

        element.bind('keypress', function(event) {
          if(event.keyCode === 32) {
            event.preventDefault();
          }
        });
      }
    };
  });




angular.module("myApp").directive('allowNumberDash', function() {
    return {
      require: '?ngModel',
      link: function(scope, element, attrs, ngModelCtrl) {
        if(!ngModelCtrl) {
          return; 
        }

        ngModelCtrl.$parsers.push(function(val) {
          if (angular.isUndefined(val)) {
              var val = '';
          }
          if (ngModelCtrl.$viewValue == "-") {
              var val = '-';
          }
          
          if(val!=null){
        	  val = val.toString();
          }else{
        	  val = '';
          }
          
          var clean = val.replace(/[^-0-9\.\-]/g, '');
		  var decimalCheck = clean.split('.');
            
          if(!angular.isUndefined(decimalCheck[1])) {
        	  decimalCheck[1] = decimalCheck[1].slice(0,attrs.decimals);
              clean =decimalCheck[0] + '.' + decimalCheck[1];
          }

          if (val !== clean) {
            ngModelCtrl.$setViewValue(clean);
            ngModelCtrl.$render();
          }
          return clean;
        });

        element.bind('keypress', function(event) {
          if(event.keyCode === 32) {
            event.preventDefault();
          }
        });
      }
    };
  });


angular.module("myApp").directive('isNumber', function () {
	return {
		require: 'ngModel',
		link: function (scope, element, attrs, ngModel) {	
    	element.bind("keydown keypress", function (event) {
          if(event.which === 32) {
            event.returnValue = false;
            return false;
          }
       }); 
    	
    	   element.bind('blur', function (e) {
       	    if (ngModel.$viewValue !=undefined && !isNaN(ngModel.$viewValue) )
       	    {
                   ngModel.$setViewValue(Number(parseFloat(ngModel.$viewValue).toFixed(2)));
                   ngModel.$render();
   				return;
               }
       	    else{
       	    	ngModel.$setViewValue(parseFloat(0).toFixed(2));
                ngModel.$render();
                return;
       	     }
           });
			scope.$watch(attrs.ngModel, function(newValue,oldValue) {

				if(newValue!=undefined && oldValue != undefined)
					{
					 var arr =String(newValue).split("");
		                var amount = String(oldValue);
		                if (arr.length === 0) return;
		                if (arr.length === 1 && (arr[0] == '-' || arr[0] === '.' )) return;
		                if (arr.length === 2 && newValue === '-.') return;
		                if (arr.length === 2 && newValue === '-.') return;
		                if(Number(amount)<0) {
		                    ngModel.$setViewValue(0);
		    				ngModel.$render();
		    				return;
		                }
		                if (isNaN(newValue)){
//		                    ngModel.$setViewValue(oldValue);
		                    ngModel.$setViewValue(0);
		    								ngModel.$render();
		    								return;
		                }
					}
               
            });
          
		}
	};
});


angular.module("myApp").directive('isQuantity', function () {
	return {
		require: 'ngModel',
		link: function (scope, element, attrs, ngModel) {	
    	element.bind("keydown keypress", function (event) {
          if(event.which === 32) {
            event.returnValue = false;
            return false;
          }
       }); 
    	
    	   element.bind('blur', function (e) {
       	    if (ngModel.$viewValue ==undefined || ngModel.$viewValue==null || isNaN(ngModel.$viewValue)||ngModel.$viewValue==''||parseInt(ngModel.$viewValue)<0||parseInt(ngModel.$viewValue)==0)
       	    {
                   ngModel.$setViewValue(1);
                   ngModel.$render();
   				return;
               }
           });
		}
	};
});


angular.module("myApp").directive('numberBlank', function () {
	return {
		require: 'ngModel',
		link: function (scope, element, attrs, ngModel) {	
    	element.bind("keydown keypress", function (event) {
          if(event.which === 32) {
            event.returnValue = false;
            return false;
          }
       }); 
    	
    	   element.bind('blur', function (e) {
       	    if (ngModel.$viewValue ==undefined || ngModel.$viewValue==null || isNaN(ngModel.$viewValue)||ngModel.$viewValue==''||parseInt(ngModel.$viewValue)<0||parseInt(ngModel.$viewValue)==0)
       	    {
                   ngModel.$setViewValue(null);
                   ngModel.$render();
   				return;
               }
           });
		}
	};
});

angular.module("myApp").directive('allowPattern', [allowPatternDirective]);
function allowPatternDirective() {
	return {
		restrict: "A",
		compile: function(tElement, tAttrs) {
			return function(scope, element, attrs) {
        // I handle key events
				element.bind("keypress", function(event) {
					var keyCode = event.which || event.keyCode; // I safely get the keyCode pressed from the event.
					var keyCodeChar = String.fromCharCode(keyCode); // I determine the char from the keyCode.
          
          // If the keyCode char does not match the allowed Regex Pattern, then don't allow the input into the field.
					if (!keyCodeChar.match(new RegExp(attrs.allowPattern, "i"))) {
            event.preventDefault();
						return false;
					}
          
				});
			};
		}
	};
}


angular.module("myApp").directive('shortcut', function() {
	  return {
	    restrict: 'E',
	    replace: true,
	    scope: true,
	    link:    function postLink(scope, iElement, iAttrs){
	      jQuery(document).on('keypress', function(e){
	         scope.$apply(scope.keyPressed(e));
	       });
	    }
	  };
	});

