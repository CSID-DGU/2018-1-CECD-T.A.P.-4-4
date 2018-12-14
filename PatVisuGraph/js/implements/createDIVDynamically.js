/**createDIVDynamically.js
 **Author : YeongWoo Kim
 **Html5 DIV creation counting dynamically
 */
var divCreation = function(divName, Name){
	if(Name.substr(0, 6) === 'sheet6') $('#'+divName).append('<div id = "'+Name+'" height = "1400" width = "1400" class="container" style="margin-left: auto; margin-right:auto; border: 3px solid blue;">');
	else if(Name.substr(0, 6) === 'sheet2') $('#'+divName).append('<div id = "'+Name+'" height = "1300" width = "1300" class="container" style="width: 1300px; margin-left: auto; margin-right:auto; border: 3px solid blue; padding-left: 0px; padding-right:0px;">');
	else $('#'+divName).append('<div id = "'+Name+'" height = "1100" width = "1100" class="container" style="margin-left: auto; margin-right:auto; border: 3px solid blue;">');
};