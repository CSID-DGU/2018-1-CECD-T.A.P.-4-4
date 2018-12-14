/**patentNationDonut.js
 **Author : YeongWoo Kim
 **patent nation donut code here(hide)
 */
var donutDataGraph_Sheet2 = function(svgName, jsonName1, jsonName2){
	//var colorDataSet = new Array();
	var glSvg = d3.select('#'+svgName)
				.attr("transform", "translate(0, 30)");
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
	$.getJSON(".//"+jsonName1+".json", function(json, error){
		var dataset=[];
		$.each(json, function(d, i){
			dataset.push({
				label: i.label,
				value: i.value,
				classification: i.classification
			});
		})
		var layerDonut = glSvg.append("g")
								.attr("id", "NationDonut");
		
		//           id             data          cx   cy   rx   ry   h   ir(ir은 0, cx 부터 center x, center y, radius x, radius y, height, inner radius)
		Donut3D.draw("NationDonut", appearData(), 650, 500, 200, 180, 50, 0);
		
		function appearData(){
			return dataset.map(function(d){
				return {label: d.label, value: d.value, color : fkColorSet[d.classification][100]};
			});
		}
	})
	
	//도넛 function 만드는 곳
	var donutGraph_around = function(ary, className, x, y, color){
		var radius = 100,
		g = d3.select('#'+svgName)
				.append("g")
				.attr("class", className)
				.attr("width", 150)
				.attr("height", 150)
				.attr("transform", "translate("+x+", "+y+")");
		
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
				    .data(pie(ary))
				    .enter().append("g")
				      .attr("class", "arc");
		
		arc.append("path")
			.attr("d", path)
			.attr("fill", function(d, i) { 
			   switch(i%5){
			   case 0:
				   return fkColorSet[color]["30"]; 
				   break;
			   case 1:
				   return fkColorSet["blank"]["30"]; 
				   break;
			   case 2:
				   return fkColorSet[color]["50"]; 
				   break;
			   case 3:
				   return fkColorSet["blank"]["50"]; 
				   break;
			   case 4:
				   return fkColorSet[color]["100"]; 
				   break;
			   default:
				   break;
			   }
			})
			 .style("stroke", "rgb(0, 0, 0)")
		
		arc.append("text")
			.attr("transform", function(d) { return "translate(" + label.centroid(d) + ")"; })
			.attr("dy", "0.2em")
			.text(function(d) { 
			   var ret = d.data.label + ' : ' + d.data.value;
			   return ret; 
			})
			.attr("font-family", "sans-serif")
			.attr("font-size", "9px")
			.attr("text-anchor", "middle");
	};
	$.getJSON(".//"+jsonName2+".json", function(json, error){
		var i = 0, j = 0;
		var cntNation;
		var nations = new Array(), nationData = [], foreignSize = new Array(), foreignNationData = [];
		var fakeClassification = new Array();
		//데이터 배열에 입력 부분
		for(var d in json){
			if(i == 0){
				cntNation = json[d]["CntNation"];
			}
			else if(i == 1){
				for(var s in json[d]){
					nations[s] = json[d][s];
				}
			}
			else if(2<=i && i<cntNation+2){
				//첫번째 pie 그래프(외국인인지 내국인인지 확인하는 그래프의 데이터)
				nationData[i-2] = [];
				for(var s in json[d]){
					nationData[i-2].push({
						label: s.substr(2),
						value: json[d][s]
					})
					//nationData[i-2][s.substr(2)] = json[d][s];
				}
			}
			else if(i == cntNation+2){
				//fake로 색깔 입힐 곳을 정하게 해주는 그래프
				for(var s in nations){
					fakeClassification[s] = json[d][s];
				}
			}
			else{
				//두번째 pie 그래프(외국인일 경우 어느 나라인지 표시 하는 그래프의 데이터)
				foreignNationData[i-(cntNation+3)] = [];
				for(var key in json[d]){
					foreignNationData[i-(cntNation+3)].push({
						label: key.substr(key.length-2),
						value: json[d][key]
					});
					//foreignNationData[i-(cntNation+3)][key.substr(key.length-2)] = json[d][key];
				}
			}
			i++;
		}
		
		//todo : 도넛의 1~2차 그릴 위치를 잡아 줘야 함(여기서는 ary로 자동 생성 되는 도넛 위치=> 아날로그 시계로 표현)
		//glSvg 에서 percent text area 를 가져 와서 그거와 일직선 된 부분에 파이 차트를 생성, 처리 하면 됨
		//percent 라는 text는 어떻게 되어 있는가 => 3D Donut의 center x, y를 받아와서 그 차이를 저장하게 됨
		//즉, 절대적인 포지션을 확립 후에 0,0 에서 뺀 곳을 알맞게 조정 하면 됨
		//cx 와 cy는 정해져 있으므로, 파이 차트가 벗어나지 않는 곳에서만 만들어 주면 됨
		//selectAll text.percent로 get 해오면 될것
		var savedPositions = [], relativePositions = [], donutPositions = [];
		d3.select("#"+svgName).selectAll("text.percent").each(function(d, i){
			savedPositions[d3.select(this).text().substr(0,2)] = [];
			relativePositions[d3.select(this).text().substr(0,2)] = [];
			savedPositions[d3.select(this).text().substr(0,2)]["x"] = parseInt(650)+parseFloat(d3.select(this).attr("x"));
			savedPositions[d3.select(this).text().substr(0,2)]["y"] = parseInt(500)+parseFloat(d3.select(this).attr("y"));
			relativePositions[d3.select(this).text().substr(0,2)]["x"] = d3.select(this).attr("x");
			relativePositions[d3.select(this).text().substr(0,2)]["y"] = d3.select(this).attr("y");
		})
		
		for(var d in savedPositions){
			//도넛 위치 계산
			savedPositions[d]["x"] += (relativePositions[d]["x"] * 2.9);
			savedPositions[d]["y"] += (relativePositions[d]["y"] * 2.5);
			
			//도넛 위치 재계산 하여 새로운 배열에 삽입
			donutPositions[d] = new Array();
			donutPositions[d + "foreign"] = new Array();
			donutPositions[d]["x"] = savedPositions[d]["x"];
			donutPositions[d]["y"] = savedPositions[d]["y"];
			if(relativePositions[d]["x"] > relativePositions[d]["y"]){
				if(relativePositions[d]["x"] > 0 ){
					donutPositions[d + "foreign"]["x"] = savedPositions[d]["x"] + 300;
					donutPositions[d + "foreign"]["y"] = savedPositions[d]["y"];
				}
				else{
					donutPositions[d + "foreign"]["x"] = savedPositions[d]["x"];
					donutPositions[d + "foreign"]["y"] = savedPositions[d]["y"] - 300;
				}
			}
			else{
				if(relativePositions[d]["y"] > 0 ){
					donutPositions[d + "foreign"]["x"] = savedPositions[d]["x"];
					donutPositions[d + "foreign"]["y"] = savedPositions[d]["y"] + 350;
				}
				else{
					donutPositions[d + "foreign"]["x"] = savedPositions[d]["x"] - 350;
					donutPositions[d + "foreign"]["y"] = savedPositions[d]["y"];
				}
			}
		}
		
		i = 0;
		for(var d in donutPositions){
			if(i%2 == 0)donutGraph_around(nationData[i/2], d, donutPositions[d]["x"], donutPositions[d]["y"], fakeClassification[d]);
			else donutGraph_around(foreignNationData[Math.floor(i/2)], d, donutPositions[d]["x"], donutPositions[d]["y"], fakeClassification[d.substr(0,2)]);
			i++;
		}
	})
};