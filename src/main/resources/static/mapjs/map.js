var map, infowindow, ps;
var currentMarker; // 현재 위치를 나타내는 마커를 저장할 변수

function initMap() {
  console.log("Initializing map...");

  // 카카오 맵 API 로드 확인
  if (typeof kakao !== "undefined" && kakao.maps && kakao.maps.services) {
    console.log("Kakao Map API loaded successfully.");

    var mapContainer = document.getElementById("map"); // 지도를 표시할 div
    var mapOption = {
      center: new kakao.maps.LatLng(33.450701, 126.570667), // 초기 지도의 중심좌표
      level: 3 // 지도의 확대 레벨
    };

    map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

    // 장소 검색 객체를 생성합니다
    ps = new kakao.maps.services.Places();

    // 검색 결과 목록이나 마커를 클릭했을 때 장소명을 표출할 인포윈도우를 생성합니다
    infowindow = new kakao.maps.InfoWindow({ zIndex: 1 });

    // 현재 위치를 찾고 지도에 표시합니다
    locateAndDisplayCurrentPosition();

    // 검색 버튼에 이벤트 리스너 추가
    document
      .getElementById("searchButton")
      .addEventListener("click", function () {
        searchPlace();
      });

    // 키워드 입력 이벤트 리스너 추가
    document.getElementById("keyword").addEventListener("input", autoComplete);
  } else {
    console.error("Failed to load Kakao Map API.");
  }
}

// 현재 위치를 찾고 지도에 표시하는 함수
function locateAndDisplayCurrentPosition() {
  if (navigator.geolocation) {
    navigator.geolocation.watchPosition(
      function (position) {
        var lat = position.coords.latitude;
        var lon = position.coords.longitude;

        console.log("Current Position:", lat, lon);

        var locPosition = new kakao.maps.LatLng(lat, lon);
        var message =
          '<div class="custom-info-window" style="background-color: white !important; border: 1px solid #ccc !important; border-radius: 8px !important; padding: 8px 12px !important; font-size: 14px !important; color: #333 !important; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1) !important; max-width: 200px !important; text-align: center !important; white-space: nowrap !important; overflow: hidden !important; text-overflow: ellipsis !important;">현재 위치</div>';

        // 이전 마커가 있으면 지도에서 제거
        if (currentMarker) {
          currentMarker.setMap(null);
        }

        // 새로운 마커 생성 및 저장
        currentMarker = new kakao.maps.Marker({
          position: locPosition,
          map: map
        });

        infowindow.setContent(message);
        infowindow.open(map, currentMarker);

        // 상위 요소의 스타일 변경
        setTimeout(function () {
          var parentDiv = document.querySelector(".custom-info-window")
            .parentElement.parentElement;
          if (parentDiv) {
            parentDiv.style.background = "none";
            parentDiv.style.border = "none";
          }
        }, 100); // DOM이 갱신될 시간을 주기 위해 약간의 지연을 추가

        setTimeout(function () {
          var parentDiv = document.querySelector(".custom-info-window")
            .parentElement;
          if (parentDiv) {
            parentDiv.style.left = "25%";
            parentDiv.style.top = "20%";
          }
        }, 100); // DOM이 갱신될 시간을 주기 위해 약간의 지연을 추가

        setTimeout(function () {
          var infoWindowElement = document.querySelector(".custom-info-window");
          if (infoWindowElement) {
            var parentDiv = infoWindowElement.parentElement.parentElement; // 상위 요소로 이동
            var siblingElements = Array.from(parentDiv.children); // 부모의 자식 요소들

            siblingElements.forEach(function (sibling) {
              if (sibling !== infoWindowElement.parentElement) {
                // 형제 요소 스타일 제거
                sibling.style.backgroundImage = "none"; // 배경 이미지 제거
              }
            });
          }
        }, 100); // DOM이 갱신될 시간을 주기 위해 약간의 지연을 추가

        map.setCenter(locPosition);
      },
      function (error) {
        console.error("Error occurred. Error code: " + error.code);
      },
      {
        enableHighAccuracy: true, // 높은 정확도 사용
        maximumAge: 0, // 위치 정보 캐시 사용 안함
        timeout: 60000 // 60초 동안 위치 정보 가져오기를 시도
      }
    );
  } else {
    alert("Geolocation is not supported by this browser.");
  }
}

// 장소 검색을 요청하는 함수입니다
function searchPlace() {
  var keyword = document.getElementById("keyword").value;

  if (!keyword.trim()) {
    alert("Please enter a search keyword!");
    return;
  }

  console.log("Searching for:", keyword); // 디버깅 메시지 추가
  ps.keywordSearch(keyword, placesSearchCB);
}

// 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
function placesSearchCB(data, status, pagination) {
  console.log("Search status:", status); // 디버깅 메시지 추가
  if (status === kakao.maps.services.Status.OK) {
    var bounds = new kakao.maps.LatLngBounds();

    for (var i = 0; i < data.length; i++) {
      console.log("Place found:", data[i].place_name); // 디버깅 메시지 추가
      displayMarker(
        new kakao.maps.LatLng(data[i].y, data[i].x),
        data[i].place_name
      );
      bounds.extend(new kakao.maps.LatLng(data[i].y, data[i].x));
    }

    map.setBounds(bounds);
  } else {
    alert("Search was not successful.");
  }
}

// 지도에 마커를 표시하는 함수입니다
function displayMarker(position, message) {
  var marker = new kakao.maps.Marker({
    map: map,
    position: position
  });

  kakao.maps.event.addListener(marker, "click", function () {
    infowindow.setContent(
      '<div style="padding:5px;font-size:12px;">' + message + "</div>"
    );
    infowindow.open(map, marker);
  });
}

// 자동 완성 기능
function autoComplete() {
  var keyword = document.getElementById("keyword").value;

  console.log("Auto-complete for:", keyword); // 디버깅 메시지 추가

  if (!keyword.trim()) {
    clearAutoComplete();
    return;
  }

  ps.keywordSearch(keyword, function (data, status) {
    console.log("Auto-complete status:", status); // 디버깅 메시지 추가
    if (status === kakao.maps.services.Status.OK) {
      console.log("Auto-complete results:", data); // 디버깅 메시지 추가
      var list = document.getElementById("autocomplete-list");
      list.innerHTML = "";
      data.forEach(function (place, index) {
        var item = document.createElement("div");
        item.className = "autocomplete-item";
        item.innerHTML =
          place.place_name + "<small>" + place.address_name + "</small>";
        item.addEventListener("click", function () {
          document.getElementById("keyword").value = place.place_name;
          clearAutoComplete();
          moveToPlace(place);
        });
        list.appendChild(item);
      });
    } else {
      clearAutoComplete();
    }
  });
}

// 장소로 이동하는 함수
function moveToPlace(place) {
  var latLng = new kakao.maps.LatLng(place.y, place.x);
  map.setCenter(latLng);
  var marker = new kakao.maps.Marker({
    map: map,
    position: latLng
  });
  infowindow.setContent(
    '<div style="padding:5pxont-size:12px;">' + place.place_name + "</div>"
  );
  infowindow.open(map, marker);
}

// 자동 완성 목록 초기화
function clearAutoComplete() {
  var list = document.getElementById("autocomplete-list");
  list.innerHTML = "";
}

// window.onload 이벤트를 통해 initMap 함수 호출
window.onload = function () {
  console.log("Window loaded. Calling initMap...");
  initMap();
};
