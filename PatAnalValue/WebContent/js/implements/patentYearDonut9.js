/**patentYearDonut.js
 **Author : YeongWoo Kim
 **Donut year code(hide)
 */
var patentYearDonut_Sheet9 = function(svgName, jsonName){
	/*
	var colorDataSet = new Array();
	$.getJSON("./ColorSettings.json", function(json, error){
		for(var d in json){
			colorDataSet[json[d]["classification"]] = new Array();
			for(var col in json[d]){
				if(json[d] != "classification") colorDataSet[json[d]["classification"]][col] = json[d][col];
			}
		}
	})
	*/
	$.getJSON(".//"+jsonName+".json", function(json, error){
		var data = [];
		var title;
		$.each(json, function(d, i){
			if(d == 0){
				title = i.title;
			}
			data.push({
				label: i.label,
				value: i.value
			})
		})
		//TODO : 도넛 모양(원)으로 해놓을 것
		var svg = d3.select('#'+svgName),
		    width = +svg.attr("width"),
		    height = +svg.attr("height"),
		    radius = Math.min(width, height) / 2,
		    g = svg.append("g").attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");

		var pie = d3.pie()
		            .sort(null)
		            .value(function(d) { return d.value; });
		
		var path = d3.arc()
				    .outerRadius(radius - 10)
				    .innerRadius(0);
		
		var label = d3.arc()
					    .outerRadius(radius - 40)
					    .innerRadius(radius - 40);
		
		var arc = g.selectAll(".arc")
				    .data(pie(data))
				    .enter().append("g")
				      .attr("class", "arc");
	
        arc.append("path")
	       .attr("d", path)
	       .attr("fill", function(d, i) { 
	    	   if(i != 0)
	    		   return fkColorSet[d.data.label]["100"]; 
	       })
	  	   .style("stroke", "rgb(0, 0, 0)")
	      
	    arc.append("text")
	       .attr("transform", function(d) { return "translate(" + label.centroid(d) + ")"; })
	       .attr("dy", "0.35em")
	       .text(function(d) { 
	    	   var ret = d.data.label + ' : ' + d.data.value;
	    	   return ret; 
	       })
	       .attr("font-family", "sans-serif")
	       .attr("font-size", "12px")
	       .attr("text-anchor", "middle");
	})
};