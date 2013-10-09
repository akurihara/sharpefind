var eatery = 'ratty';
var time = 'dinner';
var jsonUrl = "http://sharpefind.phpfogapp.com/menu.php";

$.ajaxSetup ({  
        cache: false  
    });

var ajax_loader = "<img src='ajax-loader.gif' alt='Loading...' />"; 

function refresh(buttonType){
	if (buttonType == undefined){
		$('.button').removeClass('button_down');
	} else {
		var buttons = '#' + buttonType + " .button";
		$(buttons).removeClass('button_down');
	}

	if (eatery === 'ratty'){
		$('#time_select').show();
		$('.vdub').hide();
		$('.ratty').hide();
		type = "." + time;
		$(type).show();
	} else if (eatery === 'vdub'){
		$('#time_select').hide();
		$('.vdub').show();
		$('.ratty').hide();
	}
}

function getDate(){
	$.getJSON(jsonUrl, {'menu': 'date'},  
        function(data){        	
        	var date_div = $('#date');
        	var str = 'Menu for ' + data;

        	date_div.append(str);
        }
    ); 
}

function getMenus(){

	var ul = $('<ul>');
	ul.addClass('loader');
	ul.append(ajax_loader);
	$('.collapsible').append(ul);

	$.getJSON(jsonUrl, {'menu': 'ratty_breakfast', 'assoc' : 'false'},  
        function(data){        	
        	var lists = $('.ratty.breakfast');

        	buildMenu(lists, data, 0);
        	buildMenu(lists, data, 1);
        	buildMenu(lists, data, 2);
        	buildMenu(lists, data, 3);
        }
    ); 

    $.getJSON(jsonUrl, {'menu': 'ratty_lunch', 'assoc' : 'false'},  
        function(data){        	
        	var lists = $('.ratty.lunch');

        	buildMenu(lists, data, 0);
        	buildMenu(lists, data, 1);
        	buildMenu(lists, data, 2);
        	buildMenu(lists, data, 3);
        }
    ); 

    $.getJSON(jsonUrl, {'menu': 'ratty_dinner', 'assoc' : 'false'},  
        function(data){        	
        	var lists = $('.ratty.dinner');

        	buildMenu(lists, data, 0);
        	buildMenu(lists, data, 1);
        	buildMenu(lists, data, 2);
        	buildMenu(lists, data, 3);
        }
    );

    $.getJSON(jsonUrl, {'menu': 'vdub', 'assoc' : 'false'},  
        function(data){        	
        	var lists = $('.vdub');

        	buildMenu(lists, data, 0);
        	buildMenu(lists, data, 1);
        	buildMenu(lists, data, 2);
        }
    ); 
}

function buildMenu(lists, data, listIndex){
	var ul = $('<ul>');

	if (data == 'empty'){
		var li = $('<li>');
		li.append('No Breakfast Today');
		ul.append(li);
	} else {
		$.each(data[listIndex], function(index, value){
					var li = $('<li>');
					li.append(value);
	            	ul.append(li);
				});
	}
	$(lists[listIndex]).find('ul').remove();
	$(lists[listIndex]).append(ul);
}

function setEatery(e){
	if (e !== eatery){
		eatery = e;
		refresh('eatery_select');

		var id = '#btn_' + e;
		$(id).addClass('button_down');
	}
}

function setTime(t){
	if (t !== time){
		time = t;
		refresh('time_select');
		var id = '#btn_' + t;
		$(id).addClass('button_down');
	}
}

$(document).ready(function(){
	refresh();
	$('#btn_ratty').addClass('button_down');
	$('#btn_dinner').addClass('button_down');
	getDate();
	getMenus();
});