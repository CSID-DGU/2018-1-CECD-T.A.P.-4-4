/**createSVGDynamically.js
 **Author : YeongWoo Kim
 **Html5 SVG creation counting dynamically
 */
var svgCreation = function(appenddivName, headerName, svgWidth, svgHeight, svgName) {
	if(appenddivName.substr(0, 6) != 'sheet6' && appenddivName.substr(0, 6) != 'sheet2' )$('#'+appenddivName).append('<div class="header">'+headerName+'</div>');
	else if(appenddivName.substr(0, 6)  != 'sheet2' ) $('#'+appenddivName).append('<div style="height:50px; width:1100px; text-align:center; font-size:1.5em;">'+headerName+'</div>');
	else $('#'+appenddivName).append('<div style="height:50px; width:1300px; text-align:center; font-size:1.5em;">'+headerName+'</div>');
	$('#'+appenddivName).append('<svg width = "'+svgWidth+'" height = "'+svgHeight+'" id= "'+svgName+'"></svg>');
	$('#'+appenddivName).append('</div>');
};