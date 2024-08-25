var map, marker, infowindow, ps;

document.addEventListener('DOMContentLoaded', function() {
    initMap();
    generateTimeOptions(); // ensure this function is called
    updateButtonStyles(document.getElementById('category').value);

    document.getElementById('category').addEventListener('change', function() {
        updateButtonStyles(this.value);
    });

    function updateButtonStyles(selectedCategory) {
        const timeInputs = document.getElementById('timeInputs');
        const periodInputs = document.getElementById('periodInputs');

        if (selectedCategory === '축제') {
            timeInputs.style.display = 'none';
            periodInputs.style.display = 'block';
        } else {
            timeInputs.style.display = 'block';
            periodInputs.style.display = 'none';
        }
    }

    // 기본 선택된 버튼 스타일 추가
    updateButtonStyles(document.getElementById('category').value);

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

        // Prevent the form from submitting until we get the coordinates
        return false;
    }

    function searchPlace() {
        var keyword = document.getElementById('place').value;

        if (!keyword.trim()) {
            alert('Please enter a search keyword!');
            return;
        }

        console.log('Searching for:', keyword); // 디버깅 메시지 추가
        var options = {
            location: new kakao.maps.LatLng(33.499621, 126.531188), // 제주특별시 중심 좌표
            radius: 20000 // 검색 반경 (단위: 미터)
        };
        ps.keywordSearch(keyword, placesSearchCB, options);
    }

    // 장소검색이 완료됐을 때 호출되는 콜백함수입니다
    function placesSearchCB(data, status, pagination) {
        console.log('Search status:', status); // 디버깅 메시지 추가
        if (status === kakao.maps.services.Status.OK) {

            var bounds = new kakao.maps.LatLngBounds();

            for (var i = 0; i < data.length; i++) {
                if (data[i].address_name.includes('제주특별자치도')) { // 제주특별자치도에 해당하는 장소만 필터링
                    console.log('Place found:', data[i].place_name); // 디버깅 메시지 추가
                    displayMarker(data[i]);
                    bounds.extend(new kakao.maps.LatLng(data[i].y, data[i].x));
                }
            }

            map.setBounds(bounds);
        } else {
            alert('Search was not successful.');
        }
    }

    // 지도에 마커를 표시하는 함수입니다
    function displayMarker(place) {
        // 마커를 생성하고 지도에 표시합니다
        var marker = new kakao.maps.Marker({
            map: map,
            position: new kakao.maps.LatLng(place.y, place.x)
        });

        // 마커에 클릭이벤트를 등록합니다
        kakao.maps.event.addListener(marker, 'click', function () {
            // 마커를 클릭하면 장소명이 인포윈도우에 표출됩니다
            infowindow.setContent('<div style="padding:5px;font-size:12px;">' + place.place_name + '</div>');
            infowindow.open(map, marker);
        });
    }

    // 자동 완성 기능
    function autoComplete() {
        var keyword = document.getElementById('place').value;

        if (!keyword.trim()) {
            clearAutoComplete();
            return;
        }

        var options = {
            location: new kakao.maps.LatLng(33.499621, 126.531188) // 제주특별자치도 중심 좌표
        };
        ps.keywordSearch(keyword, function (data, status) {
            if (status === kakao.maps.services.Status.OK) {
                var list = document.getElementById('autocomplete-list');
                list.innerHTML = '';
                data.forEach(function (place) {
                    var item = document.createElement('div');
                    item.className = 'autocomplete-item';
                    item.innerHTML = place.place_name + '<small>' + place.address_name + '</small>';
                    item.addEventListener('click', function () {
                        document.getElementById('place').value = place.place_name;
                        document.getElementById('address').value = place.address_name; // 여기서 address 값을 설정
                        clearAutoComplete();
                        moveToPlace(place);
                    });
                    list.appendChild(item);
                });
            } else {
                clearAutoComplete();
            }
        }, options);
    }

    // 장소로 이동하는 함수
    function moveToPlace(place) {
        var latLng = new kakao.maps.LatLng(place.y, place.x);
        map.setCenter(latLng);
        var marker = new kakao.maps.Marker({
            map: map,
            position: latLng
        });
        infowindow.setContent('<div style="padding:5px;font-size:12px;">' + place.place_name + '</div>');
        infowindow.open(map, marker);

        // 좌표를 숨겨진 필드에 저장
        document.getElementById('latitude').value = place.y;
        document.getElementById('longitude').value = place.x;
        document.getElementById('address').value = place.address_name; // 주소도 숨겨진 필드에 저장
    }

    // 자동 완성 목록 초기화
    function clearAutoComplete() {
        var list = document.getElementById('autocomplete-list');
        list.innerHTML = '';
    }

    function generateTimeOptions() {
        const startSelect = document.getElementById('businessHoursStart');
        const endSelect = document.getElementById('businessHoursEnd');

        for (let hour = 0; hour < 24; hour++) {
            const hourString = hour.toString().padStart(2, '0');
            ['00', '30'].forEach(minutes => {
                const option = document.createElement('option');
                option.value = `${hourString}:${minutes}`;
                option.text = `${hourString}:${minutes}`;
                startSelect.add(option.cloneNode(true));
                endSelect.add(option.cloneNode(true));
            });
        }
    }

    // Initialize the map and other elements
    function initMap() {
        console.log("Initializing map...");

        // 카카오 맵 API 로드 확인
        if (typeof kakao !== 'undefined' && kakao.maps && kakao.maps.services) {
            console.log("Kakao Map API loaded successfully.");

            var mapContainer = document.getElementById('map'); // 지도를 표시할 div
            var mapOption = {
                center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
                level: 3 // 지도의 확대 레벨
            };

            map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

            // 장소 검색 객체를 생성합니다
            ps = new kakao.maps.services.Places();

            // 검색 결과 목록이나 마커를 클릭했을 때 장소명을 표출할 인포윈도우를 생성합니다
            infowindow = new kakao.maps.InfoWindow({ zIndex: 1 });

            // 키워드 입력 이벤트 리스너 추가
            document.getElementById('place').addEventListener('input', autoComplete);
        } else {
            console.error("Failed to load Kakao Map API.");
        }
    }

    // 전화번호 입력 시 자동으로 형식을 맞추는 함수
    function formatPhoneNumber(input) {
        const phoneNumber = input.value.replace(/\D/g, ''); // 숫자만 추출
        let formattedNumber = '';

        if (phoneNumber.length <= 3) {
            formattedNumber = phoneNumber;
        } else if (phoneNumber.length <= 7) {
            formattedNumber = `${phoneNumber.slice(0, 3)}-${phoneNumber.slice(3)}`;
        } else if (phoneNumber.length <= 11) {
            formattedNumber = `${phoneNumber.slice(0, 3)}-${phoneNumber.slice(3, 7)}-${phoneNumber.slice(7)}`;
        } else {
            formattedNumber = `${phoneNumber.slice(0, 3)}-${phoneNumber.slice(3, 7)}-${phoneNumber.slice(7, 11)}`;
        }

        input.value = formattedNumber;
    }

    // 입력 필드에 입력할 때마다 형식을 맞추도록 이벤트 리스너 추가
    document.getElementById('phoneNumber').addEventListener('input', function(event) {
        formatPhoneNumber(event.target);
    });

    // 폼 제출 시 전화번호 형식이 올바른지 확인하는 함수
    function validatePhoneNumber() {
        const phoneNumber = document.getElementById('phoneNumber').value;
        const phonePattern = /^\d{3}-\d{3,4}-\d{4}$/;

        if (!phonePattern.test(phoneNumber)) {
            alert('전화번호 형식이 올바르지 않습니다. 올바른 형식: 000-0000-0000');
            return false;
        }

        return true;
    }

    // 폼 제출 시 전화번호 유효성 검사
    document.querySelector('form').addEventListener('submit', function(event) {
        if (!document.getElementById('address').value) {
            alert('주소가 올바르게 설정되지 않았습니다.');
            event.preventDefault(); // 유효성 검사 실패 시 폼 제출을 막음
        }

        if (!validatePhoneNumber()) {
            event.preventDefault(); // 유효성 검사 실패 시 폼 제출을 막음
        }
    });

    document.querySelector('form').addEventListener('submit', function(event) {
        const checkboxes = document.querySelectorAll('input[name="closedDay[]"]');
        let isChecked = false;

        checkboxes.forEach(function(checkbox) {
            if (checkbox.checked) {
                isChecked = true;
            }
        });

        if (!isChecked) {
            // 휴무일이 선택되지 않은 경우 "연중무휴" 값을 설정
            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = 'closedDay[]';
            input.value = '연중무휴';
            this.appendChild(input);
        }
    });
});