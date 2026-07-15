// RegEx for validate variable name
	var regex = /[^a-zA-Z_0-9]/g;

$(document).on('click', ".right-panel .fa-trash-o", function(){
	if (confirm('Are you sure to remove element?')) {
		$(this).closest('div').remove();
    }
});

 $(document).on('dblclick', '.table-td', function(){
   var column = prompt("Enter no of column", 2);
    tdcolspan($(this), column);
  });

function tdcolspan($obj, cols){
 for(var i=0; i<cols-1; i++){
    $obj.attr("colspan", cols);
  $obj.next("td").remove(); // removes extra td 
  }
}

function generate_table(tableName, tbl, rows, column) {
	var tblBody = document.createElement("tbody");
	// creating all cells
	for (var i = 0; i < rows; i++) {
		// creates a table row
		var row = document.createElement("tr");
		for (var j = 0; j < column; j++) {
			var cell = document.createElement("td");
			//var cellText = document.createTextNode("cell in row "+i+", column "+j);
			cell.className += ' table-td';
			cell.setAttribute( "contentEditable", "");
			cell.setAttribute("ng-model", "obj.tableName["+i+"]["+j+"]");
			//cell.appendChild(cellText);
			row.appendChild(cell);
		}
		// add the row to the end of the table body
		//console.log(row+"")
		tblBody.appendChild(row);
	}
	//alert(tblBody.innerHTML + " --" + rows + "-- "+ column);
	$(tblBody).appendTo($("#"+tbl + " table"));
}

function removeclasses(controlIndex,classPrefix){
    var classes = $("#"+controlIndex).attr("class").split(" ");
    $.each(classes,function(index) {
        if(classes[index].indexOf(classPrefix)==0) {
            $("#"+controlIndex).removeClass(classes[index]);
        }
    });
}

function removeclasseselement(controlIndex,classPrefix){
    var classes = $(controlIndex).attr("class").split(" ");
    $.each(classes,function(index) {
        if(classes[index].indexOf(classPrefix)==0) {
            $(controlIndex).removeClass(classes[index]);
        }
    });
}

//Edit Icon click event Handler
$(document).on('click', ".right-panel .fa-pencil-square-o", function(){
	if($(this).attr('id') == 'i-textbox-edit'){
		var id = $(this).parent().closest('div').attr('id');
		$('#DynamicdivId').val(id);
		$("#textbox-modal").modal();
		setTextBoxModal(this);
	}else if($(this).attr('id') == 'i-textarea-edit'){
		var id = $(this).closest('div').attr('id');
		$('#DynamictextareadivId').val(id);
		$("#textarea-modal").modal();
		setTextAreaModal(this);
	}else if($(this).attr('id') == 'i-heading-edit'){
		var id = $(this).closest('div').attr('id');
		$('#DynamicHeadingdivId').val(id);
		$("#heading-modal").modal();
		setHeadingModal(this);
	}else if($(this).attr('id') == 'i-checkbox-edit'){
		var id = $(this).closest('div').attr('id');
		$('#DynamicCheckboxdivId').val(id);
		$("#checkbox-modal").modal();
		setCheckboxModal(this);
		
	}else if($(this).attr('id') == 'i-dyna-chk-edit'){
		var id = $(this).closest('div').attr('id');
		$('#DynamicChkdivId').val(id);
		$("#dynamic-chk-modal").modal();
		setDinaicCheckBox(this);
		
	}else if($(this).attr('id') == 'i-radio-edit'){
		var id = $(this).closest('div').attr('id');
		$('#DynamicRadiodivId').val(id);
		$("#radio-modal").modal();
		setRadioModal(this);
		
	}else if($(this).attr('id') == 'i-select-edit'){
		var id = $(this).closest('div').attr('id');
		$('#DynamicSelectdivId').val(id);
		$("#select-modal").modal();
		setDropdownModal(this);
		
	}else if($(this).attr('id') == 'i-table-edit'){
		var id = $(this).closest('div').attr('id');
		$('#DynamicTabledivId').val(id);
		$("#table-modal").modal();
		setTableModal(this);
		
	}else if($(this).attr('id') == 'i-fieldset-edit'){
		var id = $(this).closest('div').attr('id');
		$('#DynamicfiledsetDivId').val(id);
		$('#fieldset-modal').modal();
		setLagendModal(this);
		
	}else if($(this).attr('id') == 'i-calendar-edit'){
		var id = $(this).closest('div').attr('id');
		$('#DynamicCalendarDivId').val(id);
		$('#calendar-modal').modal();
		setCalendarModal(this);
	}
});

// Edit Textbox Div
$(document).on('click','#btn-editTextboxDiv',function(){
	var textboxLabel = $("#textboxLabel").val();
	var DynamicdivId = $("#DynamicdivId").val();
	var textboxPlaceholder = $("#textboxPlaceholder").val();
	var colClass = $("#textbox-no-column").val();
	var textboxName = $("#textboxName").val();
	var textboxObject = $("#textboxName").val();
	$("#"+DynamicdivId).find('.textLabel').html(textboxLabel);
	
	var ngModelVal = textboxObject.indexOf('.') !== -1?textboxObject:'obj.'+textboxObject;
	
	$("#"+DynamicdivId+'  .textPlaceholder').attr({'placeholder':textboxPlaceholder,'name':textboxName,'ng-model': ngModelVal});
	removeclasses(DynamicdivId,'col-md-');
	$("#"+DynamicdivId).addClass(colClass);
	
	$("#"+DynamicdivId).find('.fa-trash-o').hide();
	$("#"+DynamicdivId).find('.fa-pencil-square-o').hide();
	hideEditDeleteOpt();
	$('#textbox-modal').modal('hide');
});

$(document).on('click','#btn-editTextareaDiv',function(){
	var textareaLabel = $("#textareaLabel").val();
	var DynamictextareadivId = $("#DynamictextareadivId").val();
	var textareaPlaceholder = $("#textareaPlaceholder").val();
	var colClass = $("#textarea-no-column").val();
	var textareaName = $('#textareaName').val();
	var textareaObject = $('#textareaName').val();
	
	var ngModelVal = textareaObject.indexOf('.') !== -1?textareaObject:'obj.'+textareaObject;
	
	$("#"+DynamictextareadivId).find('.textareaLabel').html(textareaLabel);
	$("#"+DynamictextareadivId+'  .textareaPlaceholder').attr({'placeholder':textareaPlaceholder,'name':textareaName,'ng-model':ngModelVal});
	removeclasses(DynamictextareadivId,'col-md-');
	$("#"+DynamictextareadivId).addClass(colClass);
	
	$("#"+DynamictextareadivId).find('.fa-trash-o').hide();
	$("#"+DynamictextareadivId).find('.fa-pencil-square-o').hide();
	hideEditDeleteOpt();
	$('#textarea-modal').modal('hide');
});

$(document).on('click','#btn-editHeadingDiv',function(){
	var HeaderContent = $("#heading-content").val();
	var DynamicHeadingdivId = $("#DynamicHeadingdivId").val();
	var textareaPlaceholder = $("#textareaPlaceholder").val();
	var colClass = $("#heading-no-column").val();
	
	$("#"+DynamicHeadingdivId+ ' .HeaderContent').html(HeaderContent);
	var contentId= $("#"+DynamicHeadingdivId+ ' .HeaderContent');
	removeclasseselement(contentId, 'header');
	removeclasseselement(contentId, 'heading-text-align');
	$(contentId).addClass($('#heading-class').val()).addClass($('#heading-align-class').val());
	//removeclasses($(DynamicHeadingdivId+' .HeaderContent'),'col-md-');
	removeclasses(DynamicHeadingdivId,'col-md-');
	$("#"+DynamicHeadingdivId).addClass(colClass);
	
	$("#"+DynamicHeadingdivId).find('.fa-trash-o').hide();
	$("#"+DynamicHeadingdivId).find('.fa-pencil-square-o').hide();
	hideEditDeleteOpt();
	$('#heading-modal').modal('hide');
});
$(document).on('click','#btn-editCheckboxDiv',function(){
	var DynamicCheckboxdivId = $("#DynamicCheckboxdivId").val();
	var checkboxLabel = $("#checkboxLabel").val();
	var checkboxValue = $("#checkboxValue").val();
	var checkboxName = $("#checkboxName").val();
	var colClass = $("#chk-no-column").val();
	var checkboxObject = $('#checkboxName').val();
	
	//var ngModelVal = checkboxObject.indexOf('.') !== -1?checkboxObject:'obj.'+checkboxObject;
	//ngModelVal = ngModelVal.replace(regex, '').split(' ').join('');
	var ngModelVal = checkboxObject.indexOf('.') !== -1?checkboxObject:'obj.'+DynamicCheckboxdivId;
	$("#"+DynamicCheckboxdivId+ ' label').html(checkboxLabel);
	$("#"+DynamicCheckboxdivId+ ' input').attr({'id': checkboxLabel, 'name': checkboxName,'ng-model':ngModelVal});
	$("#"+DynamicCheckboxdivId+ ' input').val(checkboxValue);
	removeclasses(DynamicCheckboxdivId,'col-md-');
	$("#"+DynamicCheckboxdivId).addClass(colClass);
	
	$("#"+DynamicCheckboxdivId).find('.fa-trash-o').hide();
	$("#"+DynamicCheckboxdivId).find('.fa-pencil-square-o').hide();
	hideEditDeleteOpt();
	$('#checkbox-modal').modal('hide');
});

$(document).on('click','#btn-editDynaChkDiv',function(){
	
	var DynamicRadiodivId = $("#DynamicChkdivId").val();
	var radioLabel = $("#DynaChkLabel").val();
	var radioName = $("#DynaChkName").val();
	var colClass = $("#dynachk-no-column").val();
	var radioObject = $('#DynaChkName').val();
	var dynamicFlag = $('#isDynamicCheckbox').prop("checked");
	
	$("#"+DynamicRadiodivId+ ' label').html(radioLabel);
	
	$("#"+DynamicRadiodivId+ ' .chkOptionDiv').empty();
	$("#"+DynamicRadiodivId+ '_select-flag').remove();
	$("#"+DynamicRadiodivId).append("<input type='hidden' id='"+DynamicRadiodivId+"_select-flag' ng-model='"+DynamicRadiodivId+".flag' value='"+dynamicFlag+"'>");
	
	if(!dynamicFlag){
	$(".checkbox-option").each(function(){
		var randomCHKId = randomString();
		$("<input type='checkbox' value='"+$(this).val()+"' />")
        .attr("id", randomCHKId)
        .attr("name", radioName)
        .attr("ng-model",DynamicRadiodivId + "."+$(this).val().replace(regex, '').split(' ').join(''))
        .appendTo("#"+DynamicRadiodivId+ ' .chkOptionDiv');
		
		$("<label for='"+randomCHKId+"' class='dragged-radio'> "+$(this).val()+"</label>")
        .appendTo("#"+DynamicRadiodivId+ ' .chkOptionDiv');
		});
	}else{
		$("#"+DynamicRadiodivId+ ' .chkOptionDiv').append('<label ng-repeat="rad in '+radioObject+'.list"><input value="{{rad}}" id="" name="'+radioObject+'" ng-model="'+radioObject+'.val" type="radio">{{rad}}</label>')
	}
	
	removeclasses(DynamicRadiodivId,'col-md-');
	$("#"+DynamicRadiodivId).addClass(colClass);
	$(".Addedcheckbox").empty();
	$("#"+DynamicRadiodivId).find('.fa-trash-o').hide();
	$("#"+DynamicRadiodivId).find('.fa-pencil-square-o').hide();
	hideEditDeleteOpt();
	$('#dynamic-chk-modal').modal('hide');
});

$(document).on('click','#btn-editRadioDiv',function(){
	var DynamicRadiodivId = $("#DynamicRadiodivId").val();
	var radioLabel = $("#radioLabel").val();
	var radioName = $("#radioName").val();
	var colClass = $("#radio-no-column").val();
	var radioObject = $('#radioName').val();
	var dynamicFlag = $('#isDynamicRadio').prop("checked");
	
	$("#"+DynamicRadiodivId+ ' label').html(radioLabel);
	
	$("#"+DynamicRadiodivId+ ' .radioOptionDiv').empty();
	$("#"+DynamicRadiodivId+ '_select-flag').remove();
	//$("#"+DynamicRadiodivId).append("<input type='hidden' id='"+DynamicRadiodivId+"_select-flag' ng-model='"+radioObject+".flag' value='"+dynamicFlag+"'>");
	$("#"+DynamicRadiodivId).append("<input type='hidden' id='"+DynamicRadiodivId+"_select-flag' ng-model='"+DynamicRadiodivId+".flag' value='"+dynamicFlag+"'>");
	
	if(!dynamicFlag){
	$(".radio-option").each(function(){
		var randomRadId = randomString();
		$("<input type='radio' value='"+$(this).val()+"' />")
        .attr("id", randomRadId)
        .attr("name", radioName)
        //.attr("ng-model",radioObject + ".val")
         .attr("ng-model",DynamicRadiodivId + ".val")
        .appendTo("#"+DynamicRadiodivId+ ' .radioOptionDiv');
		
		$("<label for='"+randomRadId+"' class='dragged-radio'>"+$(this).val()+"</label>")
        .appendTo("#"+DynamicRadiodivId+ ' .radioOptionDiv');
		});
	}else{
		$("#"+DynamicRadiodivId+ ' .radioOptionDiv').append('<label ng-repeat="rad in '+radioObject+'.list"><input value="{{rad}}" id="" name="'+radioObject+'" ng-model="'+radioObject+'.val" type="radio">{{rad}}</label>')
	}
	
	removeclasses(DynamicRadiodivId,'col-md-');
	$("#"+DynamicRadiodivId).addClass(colClass);
	$(".Addedradio").empty();
	$("#"+DynamicRadiodivId).find('.fa-trash-o').hide();
	$("#"+DynamicRadiodivId).find('.fa-pencil-square-o').hide();
	hideEditDeleteOpt();
	$('#radio-modal').modal('hide');
});

$(document).on('click','#btn-editSelectDiv',function(){
	var DynamicSelectdivId = $("#DynamicSelectdivId").val();
	var selectLabel = $("#selectLabel").val();
	var selectName = $("#selectName").val();
	var colClass = $("#select-no-column").val();
	var selectObject = $('#selectName').val();
	var dynamicFlag = $('#isDynamicSelect').prop("checked");
	
	$("#"+DynamicSelectdivId+ ' label').html(selectLabel);
	$("#"+DynamicSelectdivId+ ' select').empty();
	$("#"+DynamicSelectdivId+ '_select-flag').remove();
	
	$("#"+DynamicSelectdivId).append("<input type='hidden' id='"+DynamicSelectdivId+"_select-flag' ng-model='"+selectObject+".flag' value='"+dynamicFlag+"'>");
	
	if(!dynamicFlag){
	$("#"+DynamicSelectdivId+ ' select').attr({"name":selectName,"ng-model":selectObject + ".val"});
	$("<option  value='' >Select Option</option>")
    .appendTo("#"+DynamicSelectdivId+ ' select');
	
		$(".select-option").each(function(){
			$("<option  value='"+$(this).val()+"'>"+$(this).val()+"</option>")
	        .appendTo("#"+DynamicSelectdivId+ ' select');
		});
	}else{
		$("#"+DynamicSelectdivId+ ' select').attr({"name":selectName,"ng-model":selectObject + ".val","ng-options":"x for x in "+selectObject+".list"});
		$("<option  value='' >Select Option</option>")
	    .appendTo("#"+DynamicSelectdivId+ ' select');
	}
	
	removeclasses(DynamicSelectdivId,'col-md-');
	$("#"+DynamicSelectdivId).addClass(colClass);
	
	$("#"+DynamicSelectdivId).find('.fa-trash-o').hide();
	$("#"+DynamicSelectdivId).find('.fa-pencil-square-o').hide();
	hideEditDeleteOpt();
	$('#select-modal').modal('hide');
});

$(document).on('click','#btn-editTableDiv',function(){
	var DynamicTabledivId = $("#DynamicTabledivId").val();
	var tableLabel = $("#tableLabel").val();
	var tableName = $("#tableName").val();
	var colClass = $("#table-no-column").val();
	var noCol = $(' .noCol').val();
	var noRow = $(' .noRow').val();
	$("#"+DynamicTabledivId+ ' label').html(tableLabel);
	
	$("#"+DynamicTabledivId+ ' table').empty();
	$("#"+DynamicTabledivId+ ' table').attr('name', tableName);
	
	generate_table(tableName, DynamicTabledivId, noRow, noCol);
	removeclasses(DynamicTabledivId,'col-md-');
	$("#"+DynamicTabledivId).addClass(colClass);
	
	$("#"+DynamicTabledivId).find('.fa-trash-o').hide();
	$("#"+DynamicTabledivId).find('.fa-pencil-square-o').hide();
	hideEditDeleteOpt();
	$('#table-modal').modal('hide');
});

$(document).on('click','#btn-editFieldsetDiv',function(){
	var col = $('#fieldset-no-column').val();
	var label = $('#legendLabel').val();
	var DynamicfiledsetDivId = $('#DynamicfiledsetDivId').val();
	var domObj = $('#' + DynamicfiledsetDivId + ' .field');
	$('#' + DynamicfiledsetDivId + ' .field').attr('class','');
	$('#' + DynamicfiledsetDivId + ' .legend').text(label);
	$(domObj).attr('class','w3-container scheduler-border field ' + col);
	$(domObj).attr("style","padding-bottom:35px !important");
	hideEditDeleteOpt();
	$('#fieldset-modal').modal('hide');
});

//Edit calendar Div
$(document).on('click','#btn-editCalendarDiv',function(){
		var textboxLabel = $("#calendarLabel").val();
		var DynamicdivId = $("#DynamicCalendarDivId").val();
		var colClass = $("#calendar-no-column").val();
		var textboxName = $("#calendarName").val();
		var textboxObject = $("#calendarName").val();
		$("#"+DynamicdivId).find('.calendarLabel').html(textboxLabel);
		
		var ngModelVal = textboxObject.indexOf('.') !== -1?textboxObject:'obj.'+textboxObject;
		
		$("#"+DynamicdivId+' .textPlaceholder').attr({'name':textboxName,'ng-model': 'obj.'+DynamicdivId , 'format-date':'DD-MM-YYYY'});
		removeclasses(DynamicdivId,'col-md-');
		$("#"+DynamicdivId).addClass(colClass);
		
		$("#"+DynamicdivId).find('.fa-trash-o').hide();
		$("#"+DynamicdivId).find('.fa-pencil-square-o').hide();
		hideEditDeleteOpt();
		$('#calendar-modal').modal('hide');
});

function hideEditDeleteOpt(){
	$('.fa-trash-o').hide();
	$('.fa-pencil-square-o').hide();
}

function randomString() {
	var chars="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		var length = 32;
    var result = '';
    for (var i = length; i > 0; --i) result += chars[Math.round(Math.random() * (chars.length - 1))];
    return result;
}

$('.modal').on('hidden.bs.modal', function () {
    $(this).find('textarea,input,select').val('');
});


function setTextAreaModal(thisObj){
	$('#textareaPlaceholder').val($(thisObj).closest("div").children().find("textarea").attr("placeholder") != 'Enter Text' ?$(thisObj).closest("div").children().find("textarea").attr("placeholder") : '');
	$('#textareaLabel').val($(thisObj).closest("div").children().find("label").text() != 'Textarea Label' ? $(thisObj).closest("div").children().find("label").text() : '');
	$('#textareaName').val($(thisObj).closest("div").children().find("textarea").attr("name"));
	var columnClasses = $(thisObj).closest("div").attr('class');
	var splitedClasses = columnClasses.split(" ");
	if(splitedClasses.length == 2){
		if(splitedClasses[0] != 'col-md-12'){
			$('#textarea-no-column').val(splitedClasses[1]);
		}
	}
}

function setTextBoxModal(thisObj){
	$(thisObj).closest("div").children().find("label").text();
	$('#textboxLabel').val($(thisObj).closest("div").children().find("label").text() != 'TextBox Label' ? $(thisObj).closest("div").children().find("label").text() :'');
	$('#textboxName').val($(thisObj).closest("div").children().find("input[type=text]").attr("name"));
	$('#textboxPlaceholder').val($(thisObj).closest("div").children().find("input[type=text]").attr("placeholder"));
	var columnClasses = $(thisObj).closest("div").attr('class');
	var splitedClasses = columnClasses.split(" ");
	if(splitedClasses.length == 3){
		if(splitedClasses[0] != 'col-md-12'){
			$('#textbox-no-column').val(splitedClasses[2]);
		}
	}
}

function setHeadingModal(thisObj){
	$('#heading-content').val($(thisObj).closest("div").find('.HeaderContent').text().trim());
	var headingClasses = $(thisObj).closest("div").find('.HeaderContent').attr('class');
	var spiletdHeadingClasses = headingClasses.split(" ");
	if(spiletdHeadingClasses.length == 3){
		$('#heading-class').val(spiletdHeadingClasses[1]);
		$('#heading-align-class').val(spiletdHeadingClasses[2]);
	}
	var columnClasses = $(thisObj).closest("div").attr('class');
	var splitedClasses = columnClasses.split(" ");
	if(splitedClasses.length == 5){
	//	if(splitedClasses[4] != 'col-md-12'){
			$('#heading-no-column').val(splitedClasses[4]);
	//	}
	}
}

function setLagendModal(thisObj){
	$('#legendLabel').val($(thisObj).closest("div").children().find("legend").text().includes("Legend") ? '' : $(thisObj).closest("div").children().find("legend").text());
	var columnClasses = $(thisObj).closest("div").attr('class');
	var splitedClasses = columnClasses.split(" ");
	console.log(splitedClasses);
	if(splitedClasses.length == 5){
		//if(splitedClasses[2] != 'col-md-12'){
			$('#fieldset-no-column').val(splitedClasses[2]);
		//}
	}
}

function setTableModal(thisObj){
	$('#tableName').val($(thisObj).closest("div").find("table").attr('name'));
	$('#tableLabel').val($(thisObj).closest("div").find(".tableLabel").text().includes("Table Label:")? '' : $(thisObj).closest("div").find(".tableLabel").text());
	$('#table-modal').find('.noRow').val($(thisObj).closest("div").find("table tr").length);
	$('#table-modal').find('.noCol').val($(thisObj).closest("div").find("table tr:first > td").length);
	var columnClasses = $(thisObj).closest("div").attr('class');
	var splitedClasses = columnClasses.split(" ");
	console.log(splitedClasses);
	if(splitedClasses.length == 5){
		//if(splitedClasses[2] != 'col-md-12'){
			$('#table-no-column').val(splitedClasses[4]);
		//}
	}
}

function setCalendarModal(thisObj){
	$('#calendarLabel').val($(thisObj).closest("div").children("label").text().includes("Date Label:") ? '' : $(thisObj).closest("div").children("label").text());
	$('#calendarName').val($(thisObj).closest("div").children("input[type=date]").attr('name'));
	var columnClasses = $(thisObj).closest("div").attr('class');
	var splitedClasses = columnClasses.split(" ");
	console.log(splitedClasses);
	if(splitedClasses.length == 4){
		//if(splitedClasses[2] != 'col-md-12'){
			$('#calendar-no-column').val(splitedClasses[3]);
		//}
	}
	
}

function setCheckboxModal(thisObj){
	$('#checkboxLabel').val($(thisObj).closest("div").find('label').text().includes("CheckBox1") ? '' : $(thisObj).closest("div").find('label').text());
	$('#checkboxName').val($(thisObj).closest("div").find('input[type=checkbox]').attr('name'));
	$('#checkboxValue').val($(thisObj).closest("div").find('input[type=checkbox]').attr('value').includes('checkbox') ? '' : $(thisObj).closest("div").find('input[type=checkbox]').attr('value'));
	var columnClasses = $(thisObj).closest("div").attr('class');
	var splitedClasses = columnClasses.split(" ");
	if(splitedClasses.length == 6){
		//if(splitedClasses[2] != 'col-md-12'){
			$('#chk-no-column').val(splitedClasses[5]);
		//}
	}
}

function setDropdownModal(thisObj){
	$('#selectLabel').val($(thisObj).closest("div").find('label').text().includes("Label for Select") ? '' : $(thisObj).closest("div").find('label').text());
	$('#selectName').val($(thisObj).closest("div").find('select').attr('name').includes("option") ? '' :$(thisObj).closest("div").find('select').attr('name'));
	//$('#isDynamicSelect').val();
	var columnClasses = $(thisObj).closest("div").attr('class');
	var splitedClasses = columnClasses.split(" ");
	console.log(splitedClasses);
	if(splitedClasses.length == 7){
		//if(splitedClasses[2] != 'col-md-12'){
			$('#select-no-column').val(splitedClasses[6]);
		//}
	}
	
	$('#selectOptions > .addedDropDown').each(function(index){
				$(this).remove();
	});
	var listOptions = $(thisObj).closest("div").find('select').prop('options');
	$.each(listOptions, function (index) {
	    if(this.value != '' ){
	    if(index == 1){
	    	$("#selectOptions div:first").find('input[type=text]').val(this.value);
	    }
	    else{
			    var opt = "<div class='row addedDropDown'>"
					+ "<div class='col-md-10 padding0'>"
					+ "<label class='w3-label'>Select option</label>"
					+ "<input  class='select-option w3-input' type='text' value='"+ this.value +"'></input>"
					+ "</div>"
					+ "<div class='col-md-2 w3-padding-16'>"
					+ "<span class='pull-right'><button type='button' class='btn btn-primary removeDropDown'>-</button> </span>"
					+ "</div>"
					+ "</div>";
			    $('#selectOptions').append(opt);
	    }
	    }
	});
}

function setRadioModal(thisObj){
	$('#radioLabel').val($(thisObj).closest("div").find('label:first').text().includes('Label for Radio') ? '' : $(thisObj).closest("div").find('label:first').text());
	//$('#radioLabel').val($(thisObj).closest("div").find('label').text().includes("Label for Radio") ? '' : $(thisObj).closest("div").find('label').text());
	$('#radioName').val($(thisObj).closest("div").find('input[type=radio]').attr('name'));
	var columnClasses = $(thisObj).closest("div").attr('class');
	var splitedClasses = columnClasses.split(" ");
	if(splitedClasses.length == 6){
			$('#radio-no-column').val(splitedClasses[5]);
	}
	
	$('#radioOptions > .divaddedradio').each(function(index){
			$(this).remove();
	});
	
	$('#radiofirstdiv > .divaddedradio').each(function(index){
		$(this).remove();
});
	var listOptions = $(thisObj).closest("div").children().find("input[type=radio]");//$(thisObj).closest("div").find('select').prop('options');
	$.each(listOptions, function (index) {
	if(this.value != '' ){
	if(index == 0){
		$("#radiofirstdiv div:first").find('input[type=text]').val(this.value);
	}
	else{
		    var opt = "<div class='row divaddedradio'>"
					  + " <div class='col-md-12'>"
						+"<div class='col-md-10'>"
						+"<label class='w3-label'>Radio option</label>"
						+"<input class='radio-option w3-input' type='text' value='"+ this.value +"'>"
					+"</div>"
					+"<div class='col-md-2 w3-padding-16'>"
						+"<span class='pull-right removeradio'><button type='button' class='btn btn-primary'>-</button> </span>"
					+"</div>"
					+"</div>";
		    $('#radiofirstdiv').append(opt);
	}
	}else{
	if(index == 0){
		$("#radiofirstdiv div:first").find('input[type=text]').val(this.value);
     }
	}
	});
}

function setDinaicCheckBox(thisObj){
	$('#DynaChkLabel').val($(thisObj).closest("div").find('label:first').text().includes('Check box Label') ? '' : $(thisObj).closest("div").find('label:first').text());
	$('#DynaChkName').val($(thisObj).closest("div").find('input[type=checkbox]').attr('name'));
	//$('#isDynamicSelect').val();
	var columnClasses = $(thisObj).closest("div").attr('class');
	var splitedClasses = columnClasses.split(" ");
	if(splitedClasses.length == 6){
		//if(splitedClasses[2] != 'col-md-12'){
			$('#dynachk-no-column').val(splitedClasses[5]);
		//}
	}
	
	$('#dianamic-checkbox-list > .divaddedradio').each(function(index){
				$(this).remove();
	});
	
	$('#dia-check-firstdiv > .divaddedradio').each(function(index){
		$(this).remove();
	});
	var listOptions = $(thisObj).closest("div").children().find('input[type=checkbox]');
	$.each(listOptions, function (index) {
	    if(this.value != '' ){
	    if(index == 0){
	    	$("#dia-check-firstdiv div:first").find('input[type=text]').val(this.value);
	    }
	    else{
			   var opt = "<div class='row divaddedradio'>"
						  +"<div class='col-md-12'>"
							+"<div class='col-md-10'>"
							+"<label class='w3-label'>Checkbox option</label>"
							+"<input class='checkbox-option w3-input' type='text' value='"+ this.value +"'>"
						+"</div>"
						+"<div class='col-md-2 w3-padding-16'>"
							+"<span class='pull-right removeradio'><button type='button' class='btn btn-primary'>-</button> </span>"
						+"</div>"
						+"</div>";
			   $('#dia-check-firstdiv').append(opt);
	    }
	    }
	    
	});
}