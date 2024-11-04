let map; // 구글 지도 객체
let marker; // 마커 객체
let geocoder; // Geocoder 객체

// 구글 지도를 초기화하는 함수
window.initMap = function() { // 전역으로 설정
    let initialLocation = { lat: 37.5665, lng: 126.9780 }; // 서울 시청의 초기 위치

    console.log(latitude);
    console.log(longitude);

    // 기본값이 아닌 경우에만 초기 위치를 변경
    if (latitude !== null && longitude !== null) {
        initialLocation = { lat: latitude, lng: longitude }; // 등록 위치
    }

    const mapDiv = document.getElementById("map");

    if (!mapDiv) {
        console.error("Map div not found!");
        return;
    }

    // 구글 지도 객체 생성
    map = new google.maps.Map(mapDiv, {
        center: initialLocation,
        zoom: 15,
    });

    // 초기 위치에 마커 생성
    marker = new google.maps.Marker({
        position: initialLocation,
        map: map,
    });

    // Geocoder 초기화
    geocoder = new google.maps.Geocoder();

    // 지도 클릭 시 마커를 클릭한 위치로 이동시키는 이벤트 리스너 추가
    map.addListener("click", (event) => {
        marker.setPosition(event.latLng); // 클릭한 위치로 마커 이동
        updateCenter(); // 중앙 좌표 업데이트
    });

    // 지도 드래그가 끝난 후 중앙 좌표 업데이트
    map.addListener("dragend", updateCenter);
};

// 주소 검색 기능
function searchPlace() {
    const searchInput = $('#searchInput').val(); // jQuery를 사용하여 입력값 가져오기

    // Geocoding 요청
    geocoder.geocode({ address: searchInput }, (results, status) => {
        if (status === google.maps.GeocoderStatus.OK) {
            const place = results[0]; // 첫 번째 결과 사용
            map.setCenter(place.geometry.location); // 지도 중심을 결과 위치로 변경
            marker.setPosition(place.geometry.location); // 마커 위치 변경
            map.setZoom(17); // 원하는 확대 수준으로 설정

            // 위도와 경도 값을 숨겨진 input에 저장
            $('#lat').val(place.geometry.location.lat());
            $('#lng').val(place.geometry.location.lng());
        } else {
            alert("주소를 찾을 수 없습니다: " + status + ". 올바른 주소를 입력했는지 확인해주세요.");
        }
    });
}

// 지도 중앙 위치 업데이트
function updateCenter() {
    const center = map.getCenter(); // 현재 지도 중앙 위치
    $('#lat').val(center.lat()); // 위도 값을 숨겨진 input에 저장
    $('#lng').val(center.lng()); // 경도 값을 숨겨진 input에 저장
}

function getCenterCoordinates(dogId, chack) {
    const lat = $('#lat').val(); // 위도 가져오기
    const lng = $('#lng').val(); // 경도 가져오기
    if(chack == true){
        sendLocation(dogId, lat, lng);
    }else {
        updateLocation(dogId, lat, lng);
    }
    // dogId, 위도, 경도를 함께 전송
}

// AJAX 요청을 보내는 함수
function sendLocation(dogId, lat, lng) {
    $.ajax({
        url: '/usr/gpsAlert/saveLocation',
        type: 'POST',
        data: {
            dogId: dogId, // dogId를 매개변수로 사용
            latitude: lat,
            longitude: lng
        },
        success: function() {
            alert("등록되었습니다.");
            window.location.href = '../member/myPage'; // 리다이렉트
        },
        error: function(xhr, status, error) {
            alert(xhr.responseText);
            console.log(xhr.responseText);
        }
    });
}

// AJAX 요청을 보내는 함수
function updateLocation(dogId, lat, lng) {
    $.ajax({
        url: '/usr/gpsAlert/updateLocation',
        type: 'POST',
        data: {
            dogId: dogId, // dogId를 매개변수로 사용
            latitude: lat,
            longitude: lng
        },
        success: function() {
            alert("수정되었습니다.");
            window.location.href = '../member/myPage'; // 리다이렉트
        },
        error: function(xhr, status, error) {
            alert(xhr.responseText);
            console.log(xhr.responseText);
        }
    });
}

// 문서가 준비되었을 때 실행
$(document).ready(function () {
    $('#searchInput').on('keypress', function (event) {
        if (event.which === 13) { // 13은 엔터 키의 키 코드
            event.preventDefault(); // 기본 동작 방지 (폼 제출 등)
            searchPlace(); // searchPlace() 함수 호출
        }
    });
});
