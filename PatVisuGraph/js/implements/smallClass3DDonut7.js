/**smallClass3DDonut.js
 **Author : YeongWoo Kim
 **Donut year code(hide)
 */

var smallClass3DDonut_Sheet7 = function(svgName, jsonName){
	$.getJSON(".//"+jsonName+".json", function(json, error){
		var Donutdata=[];
		var title;
		$.each(json, function(d, i){
			if(d == 0){
				title = i.title;
			}
			Donutdata.push({
				label: i.label,
				value: i.value
			})
		})
		
		var getClassificationsColor = function(classification){
			return fkColorSet[classification]["100"];
		}
	
		var svg = d3.select('#'+svgName)
					.append("g")
					.attr("id","DonutPat");
		Donut3D.draw("DonutPat", appearData(), 360, 190, 130, 100, 30, 0);
	
		function appearData(){
			return Donutdata.map(function(d){
				var color = getClassificationsColor(d.label);
				return {label: d.label, value: d.value, color: color};
			});
		}
	})
};

var polygonalChart_Sheet7 = function(svgName, jsonName){
	$.getJSON('.//'+jsonName+".json", function(json, error){
		var getClassificationsColor = function(classification){
			return fkColorSet[classification]["100"];
		}
		var dataset = new Array(), minYear, maxYear, i = 0, maxValue = 0, setclass=[];
		for(var d in json){
			dataset[i] = new Array();
			for(var iData in json[d]){
				if(json[d].hasOwnProperty(iData) && (iData != "classification" && iData != "minYear" && iData != "maxYear")){
					dataset[i].push({
						year: String(iData).substring(2,4),
						value: json[d][iData]
					});
					if(maxValue < json[d][iData]){
						maxValue = json[d][iData];
					}
				}
				else if(iData == "classification"){
					setclass.push({
						classification: iData,
						value: json[d][iData]
					});
				}
				else if(iData == "minYear"){
					minYear = json[d][iData];
				}
				else if(iData == "maxYear"){
					maxYear = json[d][iData];
				}
			}
			i++;
		}
		
		var svg = d3.select('#'+svgName);
		var width = parseInt(svg.style("width")) - 70;
		var height = parseInt(svg.style("height")) - 30;
		
		var svgG = svg.append("g")
						.attr("transform", "translate(30,0)");

		var xScale = d3.scaleBand()
				.domain(dataset[0].map(function(d){return d.year;}))
				.range([0,width]).padding(0.5);
		var yScale = d3.scaleLinear()
				.domain([0, maxValue + maxValue * 0.3]).nice()
				.range([height, 0]);
		
		var line = d3.line()
					.x(function(d){return xScale(d.year);})
					.y(function(d){return yScale(d.value);});
		
		for(var j = 0; j < i; j++){
			svgG.append("path")
				.data([dataset[j]])
				.attr("class", "line")
				.attr("d", line(dataset[j]))
				.style("stroke", function(d){
					for(k=0;k<i;k++){
						return getClassificationsColor(setclass[j]["value"]);
					}
				});
		}
		
		svgG.append("g")
			.attr("transform", "translate(0, " + (height) + ")")
			.call(d3.axisBottom(xScale));

		svgG.append("g")
			.attr("class", "leftAxis")
			.call(d3.axisLeft(yScale).ticks(10));
	})
}