/**createSVGDynamically.js
 **Author : YeongWoo Kim
 **Html5 SVG creation counting dynamically
 */
var svgCreation = function(appenddivName, headerName, svgWidth, svgHeight, svgName) {
	$('#'+appenddivName).append('<div class="header">'+headerName+'</div>')
	$('#'+appenddivName).append('<svg width = "'+svgWidth+'" height = "'+svgHeight+'" id= "'+svgName+'"></svg>');
	$('#'+appenddivName).append('</div>');
};