<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" type="text/css" href="/resources/css/kakaomap.css">

<div class="clearfix"></div>
<section class="search-box">
    <div class="container-fluid">
	<div class="row">
		<div class="col-md-5 listing-block">
<!--//////////  -->
	<c:forEach items="${ hostSearch }" var="hitem">
	<form action="/user/apply?pageNum=1&hinum=${ hitem.hinum }" method="post" class="formId">
		<button type="submit" style="border:none; width:100%; text-align: left;" class="btn btn-light">
			<input type="hidden" value="${ hitem.addr }" name="addr_detail">
			<input type="hidden" value="${ hitem.title }" name="title">
			<input type="hidden" value="${ hitem.lat }" name="lat">
			<input type="hidden" value="${ hitem.lnt }" name="lnt">
			<input type="hidden" value="${ hitem.price }" name="price">
			<input type="hidden" value="${ start_day }" name="start_day">
			<input type="hidden" value="${ end_day }" name="end_day">
			<input type="hidden" value="${ people_count }" name="people_count">
		        <div class="media">
		              <img class="d-flex align-self-start" src="/resources/img/house_img/${ hitem.img }">
		              <div class="media-body pl-3">
		                <div class="price">￦${ hitem.price }원<small>${ hitem.addr }</small></div>
		                <div class="address">${ hitem.addr_detail }</div>
		                <div class="titlw"><big>${ hitem.title }</big></div>
		                <div type="hidden" value="${ hitem.addr_detail }" id="locationAdress"></div>
		                <div class="stats">
		                    <span><i class="fa fa-users" aria-hidden="true"></i>${ hitem.max_n }</span>
		                    <span><i class="fa fa-calendar" aria-hidden="true"></i>${ hitem.startdate } ~ ${ hitem.enddate }</span>
		                </div>
		              </div>
		        </div>
     	</button>
	 </form>
   </c:forEach>
   <!-- Pagning -->

   <nav aria-label="Page navigation example">
	  <ul class="pagination justify-content-center">
	   <c:choose>
	    	<c:when test="${ pageUtil.pageBlockCount > pageUtil.totalPageCount || pageUtil.pageNum ==1 }">
		    	<li class="page-item disabled">
			      <a class="page-link" href="#" tabindex="-1">이전</a>
			    </li>
	    	</c:when>
	    	<c:otherwise>
	    	<c:if test="${ all_h == 'false' }">
	    		<li class="page-item">
			      <a class="page-link" href="/search/host?pageNum=${ pageUtil.endPageNum - 1 }&locationAdress=${addr}&start_day=${start_day}&end_day=${end_day}&people_count=${people_count}" tabindex="-1">이전</a>
			    </li>
			</c:if>
			<c:if test="${ all_h == 'true' }">
	    		<li class="page-item">
			      <a class="page-link" href="/search/host/all?pageNum=${ pageUtil.endPageNum - 1 }" tabindex="-1">이전</a>
			    </li>
			</c:if>
	    	</c:otherwise>
	   </c:choose>
	    
	    <c:forEach var="i" begin="${ pageUtil.startPageNum }" end="${ pageUtil.endPageNum }">
    		<c:choose>
    			<c:when test="${ pageUtil.pageNum == i }">
    			<c:if test="${ all_h == 'false' }">
    				<li class="page-item active"><a class="page-link" href="/search/host?pageNum=${ i }&locationAdress=${addr}&start_day=${start_day}&end_day=${end_day}&people_count=${people_count}">${ i }</a></li>   	
    			</c:if>
    			<c:if test="${ all_h == 'true' }">
    				<li class="page-item active"><a class="page-link" href="/search/host/all?pageNum=${ i }">${ i }</a></li> 
    			</c:if>
    			</c:when>
    			<c:otherwise>
    				<c:if test="${ all_h == 'false' }">
    					<li class="page-item"><a class="page-link" href="/search/host?pageNum=${ i }&locationAdress=${addr}&start_day=${start_day}&end_day=${end_day}&people_count=${people_count}">${ i }</a></li>   	
    				</c:if>
    				<c:if test="${ all_h == 'true' }">
    					<li class="page-item"><a class="page-link" href="/search/host/all?pageNum=${ i }">${ i }</a></li>
    				</c:if>
    			</c:otherwise>
    		</c:choose>
	    </c:forEach>
		<c:if test="${ pageUtil.pageNum == pageUtil.totalPageCount + 1 }">
			<c:if test="${ all_h == 'false' }">
				<li class="page-item active"><a class="page-link" href="/search/host?pageNum=${ i }&locationAdress=${addr}&start_day=${start_day}&end_day=${end_day}&people_count=${pageUtil.pageNum}">${ pageUtil.pageNum }</a></li>   	
			</c:if>
			<c:if test="${ all_h == 'true' }">
				<li class="page-item active"><a class="page-link" href="/search/host/all?pageNum=${ i }">${ pageUtil.pageNum }</a></li>
			</c:if>
		</c:if>
	    <c:choose>
	    	<c:when test="${ pageUtil.pageBlockCount < pageUtil.totalPageCount }">
	    		<c:if test="${ all_h == 'false' }">
		    		<li class="page-item page-item">
				   		<a class="page-link" href="/search/host?pageNum=${ pageUtil.endPageNum + 1 }&locationAdress=${addr}&start_day=${start_day}&end_day=${end_day}&people_count=${people_count}">다음</a>
					</li>
				</c:if>
				<c:if test="${ all_h == 'true' }">
					<li class="page-item page-item">
				   		<a class="page-link" href="/search/host/all?pageNum=${ pageUtil.endPageNum + 1 }">다음</a>
					</li>
				</c:if>
	    	</c:when>
	    	<c:otherwise>
	    		<li class="page-item page-item  disabled">
			   		<a class="page-link" href="#">다음</a>
				</li>
	    	</c:otherwise>
	    </c:choose>
	  </ul>
	</nav>
   <!--//////////  -->
		</div>
		<div class="col-md-7 map-box mx-0 px-0">
			<div id="map" style="width:100%;height:100%;"></div>
		</div>
	</div>
</div>
<%-- <input type="hidden" value="${ hostSearch.hinum }" id="locationAdress"> --%>
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=406ad69f366b925b30509f2bc766e47f"></script>
<!-- <script type="text/javascript" src="/resources/js/kakao_map/hostSearch_loadmap.js"></script> -->
<script type="text/javascript">
	var lat = $("input[name='lat']");
	var lnt = $("input[name='lnt']");
	var price = $("input[name='price']");
	var addr_detail = $("input[name='addr_detail']");
	
	var mapContainer = document.getElementById('map'); // 지도를 표시할 div  
	console.log($(lat[0]).val());
	console.log($(lnt[0]).val());
    mapOption = { 
        center: new kakao.maps.LatLng($(lat[0]).val(), $(lnt[0]).val()), // 지도의 중심좌표
        level: 6 // 지도의 확대 레벨
    };

var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다


// 마커를 표시할 위치와 title 객체 배열입니다 
var positions = [];

for(var i=0; i<lat.length; i++){
	positions.push(
		{
			title: $(addr_detail[i]).val(),
			latlng: new kakao.maps.LatLng($(lat[i]).val(), $(lnt[i]).val())
		}		
	);
}

//console.log("loc : " + positions[0].title);

// 마커 이미지의 이미지 주소입니다
var imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png"; 
    
for (var i = 0; i < positions.length; i ++) {
    
    // 마커 이미지의 이미지 크기 입니다
    var imageSize = new kakao.maps.Size(24, 35); 
    
    // 마커 이미지를 생성합니다    
    var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize); 
    
    // 마커를 생성합니다
    var marker = new kakao.maps.Marker({
        map: map, // 마커를 표시할 지도
        position: positions[i].latlng, // 마커를 표시할 위치
        title : positions[i].title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
        image : markerImage // 마커 이미지 
    });
	
	//커스텀 오버레이에 표시할 내용입니다     
	//HTML 문자열 또는 Dom Element 입니다 
	var content = '<div class ="label"><span class="left"></span><span class="center">￦' + $(price[i]).val() + '</span><span class="right"></span></div>';
	
	//커스텀 오버레이가 표시될 위치입니다 
	var position = new kakao.maps.LatLng($(lat[i]).val(), $(lnt[i]).val());  
	
	//커스텀 오버레이를 생성합니다
	var customOverlay = new kakao.maps.CustomOverlay({
	 position: position,
	 content: content   
	});
	
	//커스텀 오버레이를 지도에 표시합니다
	customOverlay.setMap(map);

}

</script>