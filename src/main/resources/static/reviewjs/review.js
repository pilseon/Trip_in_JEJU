let isAtLocation = false;
let stayTimer;

function startStayTimer() {
    if (stayTimer) {
        clearTimeout(stayTimer);
    }
    stayTimer = setTimeout(() => {
        validateStayAtLocation();
    }, 20000); // 20초
}

function validateStayAtLocation() {
    if (isAtLocation) {
        fetch('/location/check-visit-status', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        .then(response => response.json())
        .then(data => {
            if (data.isVisited) {
                enableReviewForm();
            } else {
                alert("리뷰 작성 권한이 없습니다.");
            }
        });
    }
}

function monitorLocation(foodId) {
    if (navigator.geolocation) {
        fetch(`/location/location-info/${foodId}`)
            .then(response => response.json())
            .then(location => {
                const targetLat = location.latitude;
                const targetLon = location.longitude;

                navigator.geolocation.watchPosition(
                    function (position) {
                        const lat = position.coords.latitude;
                        const lon = position.coords.longitude;

                        if (isWithinProximity(lat, lon, targetLat, targetLon)) {
                            if (!isAtLocation) {
                                isAtLocation = true;
                                startStayTimer();
                            }
                        } else {
                            isAtLocation = false;
                            clearTimeout(stayTimer);
                        }
                    },
                    function (error) {
                        console.error('Error occurred. Error code: ' + error.code);
                    },
                    { enableHighAccuracy: true, maximumAge: 0, timeout: 60000 }
                );
            })
            .catch(error => {
                console.error('Failed to fetch location info:', error);
            });
    } else {
        alert('Geolocation is not supported by this browser.');
    }
}

// 페이지 로드 시 위치 추적 시작
window.onload = function() {
    const foodId = /* 해당 음식 ID를 할당 */;
    monitorLocation(foodId);
};