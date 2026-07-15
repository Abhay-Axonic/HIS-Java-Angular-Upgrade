/**
 * @author Ganesh Chaudhari
 */
angular.module("myApp").directive("mathjaxBind", function() {
    return {
        restrict: "A",
        controller: ["$scope", "$element", "$attrs", function($scope, $element, $attrs) {
            $scope.$watch($attrs.mathjaxBind, function(value) {
                var $script = angular.element("<script type='math/tex'>")
                    .html(value == undefined ? "" : value);
                $element.html("");
                $element.append($script);
                MathJax.Hub.Queue(["Reprocess", MathJax.Hub, $element[0]]);
            });
        }]
    };
});
angular.module("myApp").directive("decimals", function ($filter) {
    return {
        restrict: "A", // Only usable as an attribute of another HTML element
        require: "?ngModel",
        scope: {
            decimals: "@",
            resultTypeFlag:"@"
        },
        link: function (scope, element, attr, ngModel)
         {
           
        	
           if(scope.resultTypeFlag==1)
           {
                 var decimalCount = parseInt(scope.decimals) || 0;
                 var decimalPoint = ".";
            
            // Run when the model is first rendered and when the model is changed from code
            ngModel.$render = function() {

                if (ngModel.$modelValue != null && ngModel.$modelValue >= 0 &&!isNaN(ngModel.$modelValue)) {
                    if (typeof decimalCount === "number" && decimalCount>0) 
                    {
                        element.val(Number.parseFloat(ngModel.$modelValue).toFixed(decimalCount).toString().replace(",", "."));

                    } else {
                        element.val(ngModel.$modelValue.toString().replace(",", "."));
                    }
                }
                if(isNaN(ngModel.$modelValue))
                {
                	  if(ngModel.$modelValue == '-'){
                          element.val("-");
                      }else{
                           element.val(null);
                      }
                }
            }

            // Run when the view value changes - after each keypress
            // The returned value is then written to the model
            ngModel.$parsers.unshift(function(newValue) {
                if (typeof decimalCount === "number") {
                    var floatValue = parseFloat(newValue.replace(",", "."));
                    if (decimalCount === 0 && !isNaN(newValue) && !isNaN(floatValue) ) {
                    	var arr = newValue.split(".");
                    	if(arr!=undefined && arr!=null){
                    		return arr[0];
                    	}else{
                    		return newValue;
                    	}
                    }
                    else if(isNaN(floatValue))
                    	{ 
                    	 return null;
                    	}
                    else{
                    	 return parseFloat(floatValue.toFixed(decimalCount));
                        }
                }
                else{
                	 return parseFloat(newValue.replace(",", "."));
                  }
            });

            
            
            // Formats the displayed value when the input field loses focus
            element.on("change", function(e) {
            	
            	
                var floatValue = parseFloat(element.val().replace(",", "."));
                var originalValArr = element.val().split(".");
                if (!isNaN(floatValue) && typeof decimalCount === "number") {
                    if (decimalCount === 0) {
                    	if(originalValArr!=undefined && originalValArr!=null){
                    		element.val(originalValArr[0]);
                    	}else{
                    		element.val();
                    	}
                    		
                      //  element.val(parseInt(floatValue));
                      
                    } else {
                      /*  var strValue = floatValue.toFixed(decimalCount);
                        element.val(strValue.replace(".", decimalPoint));*/
                    	  if(Number(floatValue) === floatValue && floatValue % 1 !== 0){
                              element.val(floatValue.toFixed(decimalCount));
                         }else{
                        	 element.val(floatValue.toFixed(decimalCount));
                         }
                        
                    }
                }
                else{
                	 var inputName = ngModel.$$parentForm.$$parentForm.$name;
                     ngModel.$$parentForm.$$parentForm.$valid =false;
                     
                     var s = element.val();
                     if(s == "-"){
                        element.val("-");
                     }else{
                        element.val(null);     
                     }
               	     
                }
            });
              
           }
            else if(scope.resultTypeFlag==2||scope.resultTypeFlag==3){
                
                element.val(ngModel.$modelValue);
              }
        }
    }
});

