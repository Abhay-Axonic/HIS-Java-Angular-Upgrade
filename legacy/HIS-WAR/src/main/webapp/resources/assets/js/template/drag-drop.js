	
function allowDrop(ev) {
    ev.preventDefault();
}
var count=0;
function drag(ev) {
 draggedItem = this;
    ev.dataTransfer.setData("text", ev.target.id);
}

function drop(ev) {
    ev.preventDefault();
    var data = ev.dataTransfer.getData("text");
    //ev.target.appendChild(document.getElementById(data));
	
 	// Code to change order is remain
  
  if($("#"+data).hasClass('source-data')){
	  if($("#"+data).hasClass('data-object')){
			var dataObjectId = $("#"+data).attr('id');
			var objName = $("#"+data).text();
			//var GET_BY_ID_URL = "http://35.154.76.183:3100/dataObject/"+listId;
			var GET_BY_ID_URL = BASE_URL + ROOT_URL + "/api/global/dataobject/"+dataObjectId;
			//console.log('GET_BY_ID_URL : '+GET_BY_ID_URL);
			
          var authToken=document.getElementById('accessToken').value;
			$.ajax({
				type : "get",
				url : GET_BY_ID_URL,
				async : false,
				headers: {
					"Authorization":"Bearer "+authToken,
			    },
				success:function(response){
					var obj = response.object;
					var dataObject = JSON.parse(obj.dataObject);
					var htmlContent = dataObject.html;
					if(ev.target.id.match(/dynamicHtmlContent.*/)){
						var index = randomString();
						obj["randomId"] = index;
						angular.element(document.getElementById('dropContainer')).scope().setSectionObject(ev.target.id,obj);
						var test = '<div class="col-md-12 dataObjectDiv"><span style="float:right">'+
						'<i class="fa fa-trash-o readonly" aria-hidden="true" style="display: none;" id='+ev.target.id+'%%'+index+'></i></span>';
						test = test+htmlContent+'</div>';
						$('#'+ev.target.id).append(test);
					}else
						alert('Unable to drag object, Please drag at bottom!!');
				}
			});
		  }else{
			  var numberOfLastElement= typeof $("[id*='dynamicId']").last().attr('id') != 'undefined' ? parseInt($("[id*='dynamicId']").last().attr('id').substring(9)) : 0;
			  //var newId = (numberOfLastElement + 1);
			  if(numberOfLastElement==0){
				  count=numberOfLastElement;
			  }
			 var cookieData=document.cookie.split(';');
			 var id= cookieData.indexOf(' updateFlagDataObj=false')
			 if(id==-1){
				 count=numberOfLastElement;
			 }
			  count++;
			  var newId=count;
			  if(data=="tableDiv"){
				  $("#"+data).clone().removeClass('source-data').addClass('copy-table-data').attr("id","dynamicId"+newId).appendTo(ev.target);
			  }else{
			  $("#"+data).clone().removeClass('source-data').addClass('copy-data').attr("id","dynamicId"+newId).appendTo(ev.target);
		  }
			  //$("#dynamicId"+newId+" > label").clone().attr("onclick","showEditOption()").appendTo(ev.target);
		  }
		  }else{
	  	/**
	  	 * For now this part of code is not working
	  	 * - IT was planned for create an clone of dragged element.
	  	 */
            var copy= $("#"+data).clone(true,true);
            $("#"+data).replaceWith($(draggedItem).clone(true,true));
            $(draggedItem).replaceWith($(copy).clone(true,true));
  }
  
  
  ev.stopPropagation();
        return false;
}

function randomString() {
	var chars="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		var length = 32;
    var result = '';
    for (var i = length; i > 0; --i) result += chars[Math.round(Math.random() * (chars.length - 1))];
    return result;
}