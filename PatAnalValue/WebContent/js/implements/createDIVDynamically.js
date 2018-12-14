/**createDIVDynamically.js
 **Author : YeongWoo Kim
 **Html5 DIV creation counting dynamically
 */
var divCreation = function(divName, Name){
	if(Name.substr(0, 6) == 'sheet6') $('#'+divName).append('<div id = "'+Name+'" height = "1300" width = "1300">');
	else $('#'+divName).append('<div id = "'+Name+'" height = "1100" width = "1100">');
};