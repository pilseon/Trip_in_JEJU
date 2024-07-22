document.addEventListener("DOMContentLoaded", function() {
    var mapContainer = document.getElementById('map');
    var latitude = parseFloat(mapContainer.getAttribute('data-latitude'));
    var longitude = parseFloat(mapContainer.getAttribute('data-longitude'));

    var mapOption = {
        center: new kakao.maps.LatLng(latitude, longitude),
        level: 3
    };

    var map = new kakao.maps.Map(mapContainer, mapOption);

    var markerPosition = new kakao.maps.LatLng(latitude, longitude);

    var marker = new kakao.maps.Marker({
        position: markerPosition
    });

    marker.setMap(map);
});