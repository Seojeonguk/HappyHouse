$(function() {
	load_si_data();

	$("#search-btn").click(function() {
		var si = $("select[name='si']").val();
		var gu = $("select[name='gu']").val();
		var dong = $("select[name='dong']").val();

		str = si;
		if(gu != "") str += " " + gu;
		if(dong != "") str += " " + dong;

		// 지도 초기화, contentleft 내용 삭
		$('.content-left').children().not('.content-left-title').remove();
		
		var geocoding_str = "si=" + si + "&gun=" + gu + "&gu=" + dong;
		$.ajax({
			url : 'googlegecodoing.jsp',
			type : 'post',
			data : geocoding_str,
			async : false,
			success : function(response) {
				var lat_lng = response.trim().split("|");
				initMap(lat_lng[0],lat_lng[1]);
			},
			error : function() {
				console.log('google_geocoding error!');
			}
		});

		var category = $("select[name='category']").val();
		code_search();
		if(category == "전체"|| category=="아파트") {
			apart_search(si,gu,dong);
		}
		if(category=="전체" || category=="연립다세대") {
			housing_search(si,gu,dong);
		}
		
	});

	var domain_parameter = window.location.search.substring(1).split("&"); 
	if(domain_parameter!="") {
		$("#category option").each(function() {
			if($(this).val() == decodeURIComponent(domain_parameter[0].split("=")[1])) {
				$(this).attr("selected","selected");
			}
		});
		$("#si option").not("[name='si_init']").each(function() {
			if($(this).val() == decodeURIComponent(domain_parameter[1].split("=")[1])) {
				$(this).attr("selected","selected");
			}
		});
		load_gu_data();
		$("#gu option").not("[name='gu_init']").each(function() {
			if($(this).val() == decodeURIComponent(domain_parameter[2].split("=")[1])) {
				$(this).attr("selected","selected");
			}
		});
		load_dong_data();
		$("#dong option").not("[name='dong_init']").each(function() {
			if($(this).val() == decodeURIComponent(domain_parameter[3].split("=")[1])) {
				$(this).attr("selected","selected");
			}
		});
		$("#search-btn").trigger("click");
	} else {
		init_gu();
		init_dong();
	}
	
	$("select[name='si']").change(function() {
		load_gu_data();
	});

	$("select[name='gu']").change(function() {
		load_dong_data();
	});
});