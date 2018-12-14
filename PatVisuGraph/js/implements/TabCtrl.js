/**TabCtrl.js
 **Author : YeongWoo Kim
 **Html5 TabCtrl to swtiching tabcontent
 */
function openChart(evt, chartName){
	var i, tabContent, tabLinks;
	tabContent = document.getElementsByClassName("tabcontent");
	for(i=0;i<tabContent.length;i++){
		tabContent[i].style.display = "none";
	}
	tabLinks = document.getElementsByClassName("tablinks");
	for(i=0;i<tabLinks.length;i++){
		tabLinks[i].className = tabLinks[i].className.replace(" active", "");
	}
	document.getElementById(chartName).style.display = "block";
	evt.currentTarget.className += " active";
}

document.getElementById("defaultOpen").click();