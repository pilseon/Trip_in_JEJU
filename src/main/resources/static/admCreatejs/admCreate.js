var map, marker, infowindow, ps;

document.addEventListener('DOMContentLoaded', function() {
    // 페이지가 로드되면 초기화 작업 수행
    initMap();
    updateButtonStyles(document.getElementById('category').value);

    document.getElementById('foodButton').addEventListener('click', () => {
        document.getElementById('category').value = 'food';
        updateButtonStyles('food');
    });

    document.getElementById('dessertButton').addEventListener('click', () => {
        document.getElementById('category').value = 'dessert';
        updateButtonStyles('dessert');
    });
});

function updateButtonStyles(selectedCategory) {
    const foodButton = document.getElementById('foodButton');
    const dessertButton = document.getElementById('dessertButton');

    if (selectedCategory === 'food') {
        foodButton.classList.add('selected');
        dessertButton.classList.remove('selected');
    } else {
        dessertButton.classList.add('selected');
        foodButton.classList.remove('selected');
    }
}

function getCoordinates() {
    var place = document.getElementById('place').value;
    if (!place.trim()) {
        alert('Please enter a location!');
        return false;
    }

    var geocoder = new kakao.maps.services.Geocoder();
    geocoder.addressSearch(place, function(result, status) {
        if (status === kakao.maps.services.Status.OK) {
            var coords = result[0];
            document.getElementById('latitude').value = coords.y;
            document.getElementById('longitude').value = coords.x;
            document.querySelector('form').submit();
        } else {
            alert('Failed to get location coordinates.');
        }
    });

    return false; // Prevent form submission until coordinates are retrieved
}

function searchPlace() {
    var keyword = document.getElementById('place').value;

    if (!keyword.trim()) {
        alert('Please enter a search keyword!');
        return;
    }

    console.log('Searching for:', keyword); // Debugging message
    var options = {
        location: new kakao.maps.LatLng(33.499621, 126.531188), // 제주특별시 중심 좌표
        radius: 20000 // 검색 반경 (단위: 미터)
    };
    ps.keywordSearch(keyword, placesSearchCB, options);
}

function placesSearchCB(data, status, pagination) {
    console.log('Search status:', status); // Debugging message
    if (status === kakao.maps.services.Status.OK) {

        var bounds = new kakao.maps.LatLngBounds();

        for (var i = 0; i < data.length; i++) {
            if (data[i].address_name.includes('제주특별자치도')) { // 제주특별자치도에 해당하는 장소만 필터링
                console.log('Place found:', data[i].place_name); // Debugging message
                displayMarker(data[i]);
                bounds.extend(new kakao.maps.LatLng(data[i].y, data[i].x));
            }
        }

        map.setBounds(bounds);
    } else {
        alert('Search was not successful.');
    }
}

function displayMarker(place) {
    var marker = new kakao.maps.Marker({
        map: map,
        position: new kakao.maps.LatLng(place.y, place.x)
    });

    kakao.maps.event.addListener(marker, 'click', function () {
        infowindow.setContent('<div style="padding:5px;font-size:12px;">' + place.place_name + '</div>');
        infowindow.open(map, marker);
    });
}

function autoComplete() {
    var keyword = document.getElementById('place').value;

    console.log('Auto-complete for:', keyword); // Debugging message

    if (!keyword.trim()) {
        clearAutoComplete();
        return;
    }

    var options = {
        location: new kakao.maps.LatLng(33.499621, 126.531188), // 제주특별시 중심 좌표
        radius: 20000 // 검색 반경 (단위: 미터)
    };
    ps.keywordSearch(keyword, function (data, status) {
        console.log('Auto-complete status:', status); // Debugging message
        if (status === kakao.maps.services.Status.OK) {
            console.log('Auto-complete results:', data); // Debugging message
            var list = document.getElementById('autocomplete-list');
            list.innerHTML = '';
            data.forEach(function (place) {
                if (place.address_name.includes('제주특별자치도')) { // 제주특별자치도에 해당하는 장소만 필터링
                    var item = document.createElement('div');
                    item.className = 'autocomplete-item';
                    item.innerHTML = place.place_name + '<small>' + place.address_name + '</small>';
                    item.addEventListener('click', function () {
                        document.getElementById('place').value = place.place_name;
                        clearAutoComplete();
                        moveToPlace(place);
                    });
                    list.appendChild(item);
                }
            });
        } else {
            clearAutoComplete();
        }
    }, options);
}

function moveToPlace(place) {
    var latLng = new kakao.maps.LatLng(place.y, place.x);
    map.setCenter(latLng);
    var marker = new kakao.maps.Marker({
        map: map,
        position: latLng
    });
    infowindow.setContent('<div style="padding:5px;font-size:12px;">' + place.place_name + '</div>');
    infowindow.open(map, marker);

    document.getElementById('latitude').value = place.y;
    document.getElementById('longitude').value = place.x;
}

function clearAutoComplete() {
    var list = document.getElementById('autocomplete-list');
    list.innerHTML = '';
}

function initMap() {
    console.log("Initializing map...");

    if (typeof kakao !== 'undefined' && kakao.maps && kakao.maps.services) {
        console.log("Kakao Map API loaded successfully.");

        var mapContainer = document.getElementById('map'); // 지도를 표시할 div
        var mapOption = {
            center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
            level: 3 // 지도의 확대 레벨
        };

        map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

        ps = new kakao.maps.services.Places();

        infowindow = new kakao.maps.InfoWindow({ zIndex: 1 });

        document.getElementById('place').addEventListener('input', autoComplete);
    } else {
        console.error("Failed to load Kakao Map API.");
    }
}
