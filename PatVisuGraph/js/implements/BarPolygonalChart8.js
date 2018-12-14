/**patentNationDonut.js
 **Author : YeongWoo Kim
 **Bar Polygonal Chart Sheet8 code
 */

var BarPolygonalChart_Sheet8 = function(svgName, jsonName){
	//var colorDataSet = new Array();
	var classification;
	/*
	$.getJSON("./ColorSettings.json", function(json, error){
		for(var d in json){
			colorDataSet[json[d]["classification"]] = new Array();
			for(var col in json[d]){
				if(col != "classification") colorDataSet[json[d]["classification"]][col] = json[d][col];
			}
		}
	})
	*/
	$.getJSON(".//"+jsonName+".json", function(json, error){
		var outer=[], inner=[];
		$.each(json, function(d, i){
			if(String(i.year).substring(0,1) == "O"){
				outer.push({
					year: String(i.year).substring(3,5),
					value : i.value
				})
			}
			else if(String(i.year).substring(0,1) == "I"){
				inner.push({
					year: String(i.year).substring(3,5),
					value: i.value
				})
			}
			else{
				classification = json[d]["classification"];
			}
		})
		
		var maxValueInner = d3.max(inner, function(d){return d.value;});
		var maxValueOuter = d3.max(outer, function(d){return d.value;});
		var maxValue = Math.max(maxValueInner, maxValueOuter);
		
		var svg = d3.select('#'+svgName);
		var width = parseInt(svg.style("width")) - 70;
		var height = parseInt(svg.style("height")) - 30;
		
		var svgG = svg.append("g")
					.attr("transform", "translate(30,0)");
		
		var xScale = d3.scaleBand()
						.domain(inner.map(function(d){return d.year;}))
						.range([0,width]).padding(0.5);
		var yScale = d3.scaleLinear()
						.domain([0, maxValue + maxValue * 0.3]).nice()
						.range([height, 0]);
		
		var line = d3.line()
					.x(function(d){return xScale(d.year);})
					.y(function(d){return yScale(d.value);});
		
		svgG.selectAll("rect.bar")
			.data(inner)
			.enter().append("rect")
			.attr("class", "bar")
			.attr("height", function(d, i){return height-yScale(d.value);})
			.attr("width", xScale.bandwidth())
			.attr("x", function(d, i) {return xScale(d.year);})
			.attr("y", function(d, i) {return yScale(d.value);})
			.attr("fill", function(d) {return fkColorSet[classification]["100"];});
		
		svgG.append("path")
			.data([outer])
			.attr("class", "line")
			.attr("d", line(outer));

		svgG.append("g")
			.attr("transform", "translate(0, " + (height) + ")")
			.call(d3.axisBottom(xScale));

		svgG.append("g")
			.attr("class", "leftAxis")
			.call(d3.axisLeft(yScale).ticks(10));
	})
};