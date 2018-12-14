/**
 * CircleGrouping.js *Author : YeongWoo Kim *Grouping same code with zoomable
 * circle
 */
var CircleGrouping_sheet11 = function(svgName, divName,jsonName){
	var svg = d3.select('#'+svgName),
	  margin = 20,
	  diameter = +svg.attr("width"),
	  g = svg.append("g").attr("transform", "translate(" + diameter / 2 + "," + diameter / 2 + ")");

	var color = d3.scaleLinear()
	  .domain([-1, 5])
	  .range(["hsl(152,80%,80%)", "hsl(228,30%,40%)"])
	  .interpolate(d3.interpolateHcl);

	var pack = d3.pack()
	  .size([diameter - margin, diameter - margin])
	  .padding(2);
	
	var root = [];
	
	$.getJSON(jsonName, function(json, error){
		var j = 0;
		root = json;
		

		root = d3.hierarchy(root)
				.sum(function(d) {
					return d.size;
				})
				.sort(function(a, b) {
					return b.value - a.value;
				});
		
		var focus = root,
		nodes = pack(root).descendants(),
		view;
		
		var circle = g.selectAll("circle")
				  .data(nodes)
				  .enter().append("circle")
				  .attr("class", function(d) {
				    return d.parent ? d.children ? "node" : "node node--leaf" : "node node--root";
				  })
				  .style("fill", function(d) {
				    return d.children ? color(d.depth) : d.data.Color;
				  })
				  .on("click", function(d) {
					  if(d === focus || !d.children) {								// 현재
																					// 있는
																					// d랑
																					// 클릭
																					// 하는
																					// focus
																					// 가 같을
																					// 경우
																					// stopPropagation을
																					// 사용하여
																					// zoom
																					// 함수
																					// 호출
																					// 중단
						  
						  d3.event.stopPropagation();
						  
						  if(d["data"]["cls"] == "aaa"){
							  
							  $('#'+divName).empty();
							  $('#'+divName)
							 
							    .append('<tr>')
								.append('<th width="300"></th>')
								.append('<th style = "text-align:center;" width="660" colspan="3">비고</th>')
								.append('</tr>')
								.append('<tr>')
								.append('<td>기술군 코드 번호</td>')
								.append('<td colspan="3" id = "td_skill_code"><a id ="link_a"></a></td>')
								.append('</tr>')
								.append('<tr>')
								.append('<td>기술군 이름</td>')
								.append('<td colspan="3" id = "td_skill_name"></td>')
								.append('</tr>')
								.append('<tr>')
								.append('<td>가중 평균</td>')
								.append('<td colspan="3" id = "td_skill_aver"></td>')
								.append('</tr>')
								.append('<tr>')
								.append('<td>흐름값</td>')
								.append('<td colspan="3" id = "td_skill_flowv"></td>')
								.append('</tr>')
								.append('<tr>')
								.append('<td>분석 결과</td>')
								.append('<td colspan="3" id = "td_skill_analRes"><a id ="res"></a></td>')
								.append('</tr>')
								.append('</table>')
							  
							  $('#link_a').text(d["data"]["skillNum"]);
							  $('#res').text("결과 보기");
						
							  $('#td_skill_name').text(d["data"]["name"]);	
							  $('#td_skill_aver').text(d["data"]["aver"]);	
							  $('#td_skill_flowv').text(d["data"]["flowV"]);
							  $('#link_a').attr("href",function(){return "ProcessLink?ipcpc="+d["data"]["ipcpc"]+"&skillNum="+d["data"]["skillNum"]+"&val=list"});
							  $('#res').attr("href",function(){return "ProcessAnal?"+"skillNum="+d["data"]["skillNum"]});
						  }
						  
						  if(d["data"]["cls"] == "aa"){
							  
							
							  $('#'+divName).empty();
							  $('#'+divName).append('<tr>')
				
								.append('<th width="300"></th>')
								.append('<th style = "text-align:center;" width="660" colspan="3">비고</th>')
								.append('</tr>')
								.append('<tr>')
								.append('<td>ipc 코드 번호</td>')
								.append('<td colspan="3" id = "td_ipc_code"><a id ="link_a"></a></td>')
								.append('</tr>')
								.append('<tr>')
								.append('<td>세부 기술군 이름</td>')
								.append('<td colspan="3" id = "td_detailSkill_client"></td>')
								.append('</tr>')
								.append('<tr>')
								.append('<td>가중 평균</td>')
								.append('<td colspan="3" id = "td_detailSkill_aver"></td>')
								.append('</tr>')
								.append('<tr>')
								.append('<td>흐름값</td>')
								.append('<td colspan="3" id = "td_detailSkill_flowv"></td>')
								.append('</tr>')
								.append('<tr>')
							  
							 
							  $('#link_a').text(d["data"]["ipcpc"]);	
							  $('#td_detailSkill_client').text(d["data"]["name"]);	
							  $('#td_detailSkill_aver').text(d["data"]["aver"]);	
							  $('#td_detailSkill_flowv').text(d["data"]["flowV"]);	
							  $('#link_a').attr("href",function(){return "ProcessLink?ipcpc="+d["data"]["ipcpc"]+"&skillNum="+d["data"]["skillNum"]+"&val=listing"});
							 
						  }
						  
						  if(d["data"]["cls"] == "a"){
							  
							  $('#'+divName).empty();
							  $('#'+divName).append('<tr>')
								.append('<th width="300"></th>')
								.append('<th style = "text-align:center;" width="660" colspan="3">비고</th>')
								.append('</tr>')
								.append('<tr>')
								.append('<td>특허 번호</td>')
								.append('<td colspan="3" id = "td_patent_code"></td>')
								.append('</tr>')
								.append('<tr>')
								.append('<td>특허 이름</td>')
								.append('<td colspan="3" id = "td_patent_name"></td>')
								.append('</tr>')
								.append('<tr>')
								.append('<td>특허 등급</td>')
								.append('<td colspan="3" id = "td_patent_grade"></td>')
								.append('</tr>')
								.append('<tr>')
								.append('<td>특허 점수</td>')
								.append('<td colspan="3" id = "td_patent_score"></td>')
								.append('</tr>')
								.append('<tr>')
							  
							  $('#td_patent_code').text(d["data"]["patNum"]);	
							  $('#td_patent_name').text(d["data"]["name"]);	
							  $('#td_patent_score').text(d["data"]["score"]);	
							  $('#td_patent_grade').text(d["data"]["grade"]);		
						  }
						  
						  if(d["data"]["cls"] == "bbb"){
							  
							  $('#'+divName).empty();
							  $('#'+divName)
							 
							    .append('<tr>')
								.append('<th width="300"></th>')
								.append('<th style = "text-align:center;" width="660" colspan="3">비고</th>')
								.append('</tr>')
								.append('<tr>')
								.append('<td>기술군 코드 번호</td>')
								.append('<td colspan="3" id = "td_skill_code"></td>')
								.append('</tr>')
								.append('<tr>')
								.append('<td>기술군 이름</td>')
								.append('<td colspan="3" id = "td_skill_name"></td>')
								.append('</tr>')
								.append('<tr>')
								.append('<td>가중 평균</td>')
								.append('<td colspan="3" id = "td_skill_aver"></td>')
								.append('</tr>')
								.append('<tr>')
								.append('<td>흐름값</td>')
								.append('<td colspan="3" id = "td_skill_flowv"></td>')
								.append('</tr>')
								.append('</table>')
							  
							  
							 $('#td_skill_code').text(d["data"]["skillNum"]);	
							  $('#td_skill_name').text(d["data"]["name"]);	
							  $('#td_skill_aver').text(d["data"]["aver"]);	
							  $('#td_skill_flowv').text(d["data"]["flowV"]);
							
						  }
						  
						  if(d["data"]["cls"] == "bb"){
							  
							
							  $('#'+divName).empty();
							  $('#'+divName).append('<tr>')
				
								.append('<th width="300"></th>')
								.append('<th style = "text-align:center;" width="660" colspan="3">비고</th>')
								.append('</tr>')
								.append('<tr>')
								.append('<td>ipc 코드 번호</td>')
								.append('<td colspan="3" id = "td_ipc_code"</td>')
								.append('</tr>')
								.append('<tr>')
								.append('<td>세부 기술군 이름</td>')
								.append('<td colspan="3" id = "td_detailSkill_client"></td>')
								.append('</tr>')
								.append('<tr>')
								.append('<td>가중 평균</td>')
								.append('<td colspan="3" id = "td_detailSkill_aver"></td>')
								.append('</tr>')
								.append('<tr>')
								.append('<td>흐름값</td>')
								.append('<td colspan="3" id = "td_detailSkill_flowv"></td>')
								.append('</tr>')
								.append('<tr>')
							  
							 
							
							
							  $('#td_ipc_code').text(d["data"]["ipcpc"]);	
							  $('#td_detailSkill_client').text(d["data"]["name"]);	
							  $('#td_detailSkill_aver').text(d["data"]["aver"]);	
							  $('#td_detailSkill_flowv').text(d["data"]["flowV"]);	
							  
							 
						  }
						  
						  if(d["data"]["cls"] == "b"){
							  
							  $('#'+divName).empty();
							  $('#'+divName).append('<tr>')
								.append('<th width="300"></th>')
								.append('<th style = "text-align:center;" width="660" colspan="3">비고</th>')
								.append('</tr>')
								.append('<tr>')
								.append('<td>특허 번호</td>')
								.append('<td colspan="3" id = "td_patent_code"></td>')
								.append('</tr>')
								.append('<tr>')
								.append('<td>특허 이름</td>')
								.append('<td colspan="3" id = "td_patent_name"></td>')
								.append('</tr>')
								.append('<tr>')
								.append('<td>특허 등급</td>')
								.append('<td colspan="3" id = "td_patent_grade"></td>')
								.append('</tr>')
								.append('<tr>')
								.append('<td>특허 점수</td>')
								.append('<td colspan="3" id = "td_patent_score"></td>')
								.append('</tr>')
								.append('<tr>')
							  
							  $('#td_patent_code').text(d["data"]["patNum"]);	
							  $('#td_patent_name').text(d["data"]["name"]);	
							  $('#td_patent_score').text(d["data"]["score"]);	
							  $('#td_patent_grade').text(d["data"]["grade"]);		
						  }
						 
						  
					  }
					  else zoom(d), d3.event.stopPropagation();		// zoom을 한
																	// 뒤에 값을 멈춰
																	// 놓는 것(즉
																	// 확대/축소 하면서
																	// 값을 멈춰 놓는
																	// 것)
				  });
		
		var text = g.selectAll("text")
					.data(nodes)
					.enter().append("text")
					.attr("class", "label")
					.style("fill-opacity", function(d) {
						return d.parent === root ? 1 : 0;
					})
					.style("display", function(d) {
						return d.parent === root ? "inline" : "none";
					})
					.style("font-family", "sans-serif")
					.style("font-size", "20px")
					.style("font-weight", "bold")
					.text(function(d) {
						return d.data.name;
					});
		
		var node = g.selectAll("circle,text");
		
		svg.style("background", color(-1))
			.on("click", function() {
				zoom(root);
			});
		
		zoomTo([root.x, root.y, root.r * 2 + margin]);
		
		function zoom(d) {
			var focus0 = focus;
			focus = d;
			
			var transition = d3.transition()
						    .duration(d3.event.altKey ? 7500 : 750)
						    .tween("zoom", function(d) {
						      var i = d3.interpolateZoom(view, [focus.x, focus.y, focus.r * 2 + margin]);
						      return function(t) {
						        zoomTo(i(t));
						      };
						    });
			
			transition.selectAll("text")
					.filter(function(d) {
					  if(!(d===undefined)) return d.parent === focus || this.style.display === "inline";
					})
					.style("fill-opacity", function(d) {
						if(!(d===undefined)) return d.parent === focus ? 1 : 0;
					})
					.on("start", function(d) {
						if(!(d===undefined)) if (d.parent === focus) this.style.display = "inline";
					})
					.on("end", function(d) {
						if(!(d===undefined)) if (d.parent !== focus) this.style.display = "none";
					});
		}
		
		function zoomTo(v) {
			var k = diameter / v[2];
			view = v;
			node.attr("transform", function(d) {
				return "translate(" + (d.x - v[0]) * k + "," + (d.y - v[1]) * k + ")";
			});
			circle.attr("r", function(d) {
				return d.r * k;
			});
		}
	})
};
