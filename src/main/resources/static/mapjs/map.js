var map, infowindow, ps;
var currentMarker;
var currentPosition;

let visitStartTime;
let checkingInterval;
let currentLocation;
let foodLocations = []; // 서버에서 가져온 음식점 위치 데이터를 저장
let lastCheckedFoodId = null;

function initMap() {
    console.log("Initializing map...");

    if (typeof kakao !== "undefined" && kakao.maps && kakao.maps.services) {
        console.log("Kakao Map API loaded successfully.");

        var mapContainer = document.getElementById("map");
        var mapOption = {
            center: new kakao.maps.LatLng(33.450701, 126.570667),
            level: 3
        };

        map = new kakao.maps.Map(mapContainer, mapOption);

        ps = new kakao.maps.services.Places();
        infowindow = new kakao.maps.InfoWindow({ zIndex: 1 });

        locateAndDisplayCurrentPosition();
        loadLocationsAndDisplayMarkers();
        startLocationTracking();

        document.getElementById("searchButton").addEventListener("click", searchPlace);
        document.getElementById("keyword").addEventListener("input", autoComplete);

        // 현재 위치로 이동 버튼에 이벤트 추가
        document.getElementById("moveToCurrentPosition").addEventListener("click", function () {
            if (currentPosition) {
                map.setCenter(currentPosition);
                map.setLevel(3);  // 지도의 줌 레벨을 3으로 설정 (원하는 대로 조정 가능)
            } else {
                console.error("Current position is not available.");
            }
        });

    } else {
        console.error("Failed to load Kakao Map API.");
    }
}

function startLocationTracking() {
    if (navigator.geolocation) {
        navigator.geolocation.watchPosition(position => {
            currentLocation = {
                latitude: position.coords.latitude,
                longitude: position.coords.longitude
            };

            console.log("Current Position:", currentLocation);

            let inRange = false;

            foodLocations.forEach(food => {
                let distance = calculateDistance(currentLocation.latitude, currentLocation.longitude, food.latitude, food.longitude);
                console.log(`Distance to foodId ${food.id}: ${distance} meters`);

                if (distance <= 30 && lastCheckedFoodId !== food.id) { // 거리를 30미터로 설정
                    inRange = true;

                    if (!visitStartTime || lastCheckedFoodId !== food.id) {
                        visitStartTime = new Date();
                        lastCheckedFoodId = food.id;
                        clearInterval(checkingInterval);
                        checkingInterval = setInterval(checkVisitTime, 1000, food.id);
                    }
                }
            });

            if (!inRange) {
                clearInterval(checkingInterval);
                visitStartTime = null;
                lastCheckedFoodId = null;
            }
        },
        function (error) {
            console.error("Error occurred. Error code: " + error.code);
        },
        {
            enableHighAccuracy: true,
            maximumAge: 0,
            timeout: 60000
        });
    } else {
        alert("Geolocation is not supported by this browser.");
    }
}

function calculateDistance(lat1, lon1, lat2, lon2) {
    const R = 6371e3; // metres
    const φ1 = lat1 * Math.PI/180;
    const φ2 = lat2 * Math.PI/180;
    const Δφ = (lat2-lat1) * Math.PI/180;
    const Δλ = (lon1-lon2) * Math.PI/180;

    const a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
              Math.cos(φ1) * Math.cos(φ2) *
              Math.sin(Δλ/2) * Math.sin(Δλ/2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

    return R * c;
}

function checkVisitTime(foodId) {
    console.log(`Checking visit time for foodId: ${foodId}`);
    let currentTime = new Date();
    let timeSpent = (currentTime - visitStartTime) / 1000;

    console.log(`Time spent at foodId ${foodId}: ${timeSpent} seconds`);

    if (timeSpent >= 20) { // 시간을 20초로 설정
        console.log(`Time spent is sufficient, registering visit for foodId: ${foodId}`);
        registerVisit(foodId);
        clearInterval(checkingInterval);
        visitStartTime = null;
        lastCheckedFoodId = null;
    }
}

function getMemberIdFromServer() {
    return fetch('/api/getMemberId')
        .then(response => {
            if (!response.ok) {
                throw new Error("Failed to fetch member ID, status: " + response.status);
            }
            return response.json();
        })
        .then(data => {
            console.log("Member ID retrieved:", data);
            return data;
        })
        .catch(error => {
            console.error('Error fetching member ID:', error);
            return null;
        });
}

function getMemberId() {
    return localStorage.getItem('memberId');
}

function onLoginSuccess(memberId) {
    // 로그인 성공 후 memberId를 localStorage에 저장
    if (memberId) {
        localStorage.setItem('memberId', memberId);
        console.log("memberId가 localStorage에 저장되었습니다:", memberId);
    } else {
        console.error("로그인 성공 후 memberId가 제공되지 않았습니다.");
    }
}

function saveMemberId(memberId) {
    localStorage.setItem('memberId', memberId);
    console.log("Saved memberId to localStorage:", memberId);
}

function registerVisit(foodId) {
    console.log(`Registering visit for foodId: ${foodId}`);

    getMemberIdFromServer().then(memberId => {
        if (!memberId) {
            console.log("Member ID not found, skipping registration.");
            return;
        }

        const visitData = {
            foodId: foodId,
            memberId: memberId
        };

        console.log("Sending visit data to server:", visitData);

        fetch('/api/visitRecord', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(visitData)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to register visit, status: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            console.log("Visit registered successfully:", data);
        })
        .catch(error => {
            console.error('Error registering visit:', error);
        });
    });
}

function loadLocationsAndDisplayMarkers() {
    fetch('/food/locations')
        .then(response => response.json())
        .then(data => {
            console.log('Fetched food locations data:', data);
            if (Array.isArray(data)) {
                data.forEach(food => {
                    if (food && food.latitude && food.longitude) {
                        console.log(`Food data: ${JSON.stringify(food)}`);  // food 객체 로그 추가

                        var markerPosition = new kakao.maps.LatLng(food.latitude, food.longitude);
                        var marker = new kakao.maps.Marker({
                            map: map,
                            position: markerPosition
                        });

                        var infowindow = new kakao.maps.InfoWindow({
                            content: `<div style="padding:5px;">${food.title} (Category: ${food.category})</div>`
                        });

                        kakao.maps.event.addListener(marker, 'click', function() {
                            infowindow.open(map, marker);
                        });

                        foodLocations.push(food);

                        // 위치가 근처에 있을 때 타이머 시작
                        if (calculateDistance(currentLocation.latitude, currentLocation.longitude, food.latitude, food.longitude) <= 10000) {
                            console.log(`Starting timer for foodId: ${food.id}`);  // food.id가 올바르게 전달되는지 확인
                            startTimerForVisit(food.id);
                        }
                    } else {
                        console.error(`Invalid food data: ${JSON.stringify(food)}`);  // 오류 원인 출력
                    }
                });

            } else {
                console.error('Food locations data is not an array:', data);
            }
        })
        .catch(error => {
            console.error('Error fetching food locations:', error);
        });
}

function startTimerForVisit(foodId) {
    if (!visitStartTime) {
        visitStartTime = new Date();
        console.log(`Starting timer for foodId: ${foodId}`);  // foodId 로그 추가
        checkingInterval = setInterval(checkVisitTime, 1000, foodId); // foodId 전달
    } else {
        console.log(`Timer already started for foodId: ${foodId}`);
    }
}

function locateAndDisplayCurrentPosition() {
    if (navigator.geolocation) {
        navigator.geolocation.watchPosition(
            function (position) {
                var lat = position.coords.latitude;
                var lon = position.coords.longitude;

                console.log("Current Position:", lat, lon);

                currentPosition = new kakao.maps.LatLng(lat, lon);

                var message =
                    '<div class="custom-info-window" style="background-color: white !important; border: 1px solid #ccc !important; border-radius: 8px !important; padding: 8px 12px !important; font-size: 14px !important; color: #333 !important; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1) !important; max-width: 200px !important; text-align: center !important; white-space: nowrap !important; overflow: hidden !important; text-overflow: ellipsis !important;">현재 위치</div>';

                if (currentMarker) {
                    currentMarker.setMap(null);
                }

                currentMarker = new kakao.maps.Marker({
                    position: currentPosition,
                    map: map
                });

                infowindow.setContent(message);
                infowindow.open(map, currentMarker);

                setTimeout(function () {
                    var parentDiv = document.querySelector(".custom-info-window")
                        .parentElement.parentElement;
                    if (parentDiv) {
                        parentDiv.style.background = "none";
                        parentDiv.style.border = "none";
                    }
                }, 100);

                setTimeout(function () {
                    var parentDiv = document.querySelector(".custom-info-window")
                        .parentElement;
                    if (parentDiv) {
                        parentDiv.style.left = "25%";
                        parentDiv.style.top = "20%";
                    }
                }, 100);

                setTimeout(function () {
                    var infoWindowElement = document.querySelector(".custom-info-window");
                    if (infoWindowElement) {
                        var parentDiv = infoWindowElement.parentElement.parentElement;
                        var siblingElements = Array.from(parentDiv.children);

                        siblingElements.forEach(function (sibling) {
                            if (sibling !== infoWindowElement.parentElement) {
                                sibling.style.backgroundImage = "none";
                            }
                        });
                    }
                }, 100);
            },
            function (error) {
                console.error("Error occurred. Error code: " + error.code);
            },
            {
                enableHighAccuracy: true,
                maximumAge: 0,
                timeout: 60000
            }
        );
    } else {
        alert("Geolocation is not supported by this browser.");
    }
}

function searchPlace() {
    var keyword = document.getElementById("keyword").value;

    if (!keyword.trim()) {
        alert("Please enter a search keyword!");
        return;
    }

    ps.keywordSearch(keyword, placesSearchCB);
}

function placesSearchCB(data, status, pagination) {
    if (status === kakao.maps.services.Status.OK) {
        var bounds = new kakao.maps.LatLngBounds();

        for (var i = 0; i < data.length; i++) {
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

function autoComplete() {
    var keyword = document.getElementById("keyword").value;

    if (!keyword.trim()) {
        clearAutoComplete();
        return;
    }

    ps.keywordSearch(keyword, function (data, status) {
        if (status === kakao.maps.services.Status.OK) {
            var list = document.getElementById("autocomplete-list");
            list.innerHTML = "";
            data.forEach(function (place) {
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

function moveToPlace(place) {
    var latLng = new kakao.maps.LatLng(place.y, place.x);
    map.setCenter(latLng);
    var marker = new kakao.maps.Marker({
        map: map,
        position: latLng
    });
    infowindow.setContent(
        '<div style="padding:5px;font-size:12px;">' + place.place_name + "</div>"
    );
    infowindow.open(map, marker);
}

function clearAutoComplete() {
    var list = document.getElementById("autocomplete-list");
    list.innerHTML = "";
}

window.onload = function () {
    console.log("Window loaded. Calling initMap...");

    getMemberIdFromServer().then(memberId => {
        if (memberId) {
            console.log('Member ID:', memberId);
            initMap();
        } else {
            console.error('Failed to get member ID');
        }
    });
};