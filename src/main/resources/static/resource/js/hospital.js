$(document).ready(function () {
    // 페이지 로드 시 시 데이터를 서버에서 가져옴
    loadCities();
});

// 시 데이터 로드
function loadCities() {
    $.ajax({
        url: '/cities',
        method: 'GET',
        success: function (cities) {
            const citySelect = document.getElementById('city-select');
            cities.forEach(city => {
                const option = document.createElement('option');
                option.value = city;
                option.text = city;
                citySelect.appendChild(option);
            });
        }
    });
}

// 선택한 시에 맞는 군/구 데이터를 로드
function loadCounties() {
    const selectedCity = document.getElementById('city-select').value;
    const countySelect = document.getElementById('county-select');

    if (!selectedCity) {
        countySelect.innerHTML = '<option value="">시/군/구 선택</option>';
        return;  // 시가 선택되지 않았으면 실행하지 않음
    }


    // 세종특별자치시 처리 (군/구 선택 없음)
    if (selectedCity === "세종특별자치시") {
        countySelect.innerHTML = '<option value="">시/군/구 없음</option>';
        countySelect.disabled = true;  // 군/구 선택 비활성화
    } else {
        countySelect.disabled = false;
        $.ajax({
            url: '/counties',
            method: 'GET',
            data: {city: selectedCity},
            success: function (counties) {
                countySelect.innerHTML = '<option value="">시/군/구 선택</option>';  // 기존 군 데이터 초기화

                counties.forEach(county => {
                    const option = document.createElement('option');
                    option.value = county;
                    option.text = county;
                    countySelect.appendChild(option);
                });
            }
        });
    }
}


// 전역 변수로 선언된 map이 이미 존재할 경우 중복 선언하지 않음
if (typeof map === 'undefined') {
    var map;  // let 대신 var로 전역 변수 선언을 수정하여 재선언 방지
}

if (typeof service === 'undefined') {
    var service;
}

async function initMap() {
    // Google Maps와 Places 라이브러리를 비동기로 불러옴
    const {Map} = await google.maps.importLibrary("maps");
    const {Place, SearchNearbyRankPreference} = await google.maps.importLibrary("places");

    // 지도 중심 좌표 설정 (서울)
    let center = new google.maps.LatLng(37.5665, 126.9780);

    // 지도를 초기화합니다.
    map = new Map(document.getElementById("map"), {
        center: center,
        zoom: 14,
        mapId: google_mapId,
    });

    // 마커를 저장할 배열 생성
    const markers = [];

    // 서버에서 병원 데이터 가져오기 (비동기 요청)
    fetch('/hospitals')
        .then(response => response.json())
        .then(data => {
            // 폐업하지 않은 병원만 필터링
            const activeHospitals = data.filter(hospital => hospital.businessStatus !== '폐업');

            // 마커 생성
            activeHospitals.forEach(hospital => {
                if (hospital.latitude && hospital.longitude) {
                    const marker = new google.maps.Marker({
                        position: {lat: parseFloat(hospital.latitude), lng: parseFloat(hospital.longitude)},
                        map: map,
                        title: hospital.name
                    });

                    // 마커를 markers 배열에 추가
                    markers.push(marker);

                    // 클릭 이벤트 추가 (필요 시)
                    const infoWindow = new google.maps.InfoWindow({
                        content: `<h3>${hospital.name}</h3><p>주소: ${hospital.roadAddress}</p><p>전화번호: ${hospital.callNumber}</p>`
                    });

                    marker.addListener('click', () => {
                        infoWindow.open(map, marker);
                    });
                }
            });

            // 마커 클러스터링 적용
            new MarkerClusterer(map, markers, {
                imagePath: 'https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/m',
            });

        })
        .catch(error => console.error('Error fetching hospital data:', error));
}

// 신) 사용자가 시/군/구를 선택하여 주소를 좌표로 변환
function geocodeAddress() {
    const selectedCity = document.getElementById('city-select').value;
    const selectedCounty = document.getElementById('county-select').value;

    if (!selectedCity || (selectedCity !== "세종특별자치시" && !selectedCounty)) {
        alert("시와 군/구를 모두 선택하세요.");
        return;
    }

    const geocoder = new google.maps.Geocoder();
    const address = selectedCity + (selectedCity !== "세종특별자치시" ? " " + selectedCounty : "");

    // 주소 값을 콘솔에 출력하여 확인
    console.log("Geocoding 주소:", address);

    geocoder.geocode({address}, function (results, status) {
        if (status === 'OK') {

            const location = results[0].geometry.location;  // 위치 정보 가져오기

            const lat = location.lat();
            const lng = location.lng();

            if (lat && lng) {
                console.log("Geocoded location (lat, lng):", lat, lng);
                map.setCenter(location);
                map.setZoom(14);  // 줌 설정

                // 동물 병원 검색 (Google Places API 사용)
                // findAnimalHospitals(location);
            } else {
                console.error('위도 및 경도 값이 null입니다.');
            }

        } else {
            alert("주소 변환 실패: " + status);
        }
    });
}


//신

// 동물 병원을 찾는 함수 (Places API - New 사용)
async function findAnimalHospitals(location) {
    const {Place, SearchNearbyRankPreference} = await google.maps.importLibrary("places");
    const {AdvancedMarkerElement} = await google.maps.importLibrary("marker");

    const request = {
        // 검색할 위치와 반경 설정
        fields: ["displayName", "location", "businessStatus"],
        locationRestriction: {
            center: location,
            radius: 5000,  // 반경 5km 내에서 검색
        },
        includedPrimaryTypes: ["veterinary_care"],  // 동물 병원 검색
        maxResultCount: 20,  // 최대 결과
        rankPreference: SearchNearbyRankPreference.POPULARITY,  // 인기순 정렬
    };

    try {
        const {places} = await Place.searchNearby(request);  // 장소 검색

        if (places.length) {
            const {LatLngBounds} = await google.maps.importLibrary("core");
            const bounds = new LatLngBounds();

            // 검색된 장소마다 마커 생성
            places.forEach((place) => {
                const marker = new AdvancedMarkerElement({
                    map,
                    position: place.location,  // 장소 위치에 마커 설정
                    title: place.displayName,  // 마커 제목 설정
                });
                bounds.extend(place.location);  // 장소 위치를 지도에 맞춰 확장
            });

            map.fitBounds(bounds);  // 검색된 장소에 맞게 지도 범위 조정
        } else {
            console.log("검색 결과가 없습니다.");
        }
    } catch (error) {
        console.error("Nearby Search 오류 발생:", error);
    }
}
