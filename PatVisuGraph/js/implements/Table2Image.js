/**Table2Image.js
 **Author : YeongWoo Kim
 **Html5 Table Img Downloading(Implementationing...)
 */
$(function(){
	$("#saveImg").click(function(){
		html2canvas($("#printDiv"),{
			onrendered: function(canvas){
				saveAs(canvas.toDataURL(), 'table.png');
			}
		});
	});
	function saveAs(uri, filename){
		var link = document.createElement('a');
		if(typeof link.download == 'string'){
			link.href = uri;
			link.download = filename;
			
			document.body.appendChild(link);
			
			link.click();
			
			document.body.removeChild(link);
		}
		else{
			window.open(uri);
		}
	}
})