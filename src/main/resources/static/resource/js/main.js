$(document).ready(function () {
    var $slider = $('.slider_container'); // 슬라이더 컨테이너
    var index = 0; // 현재 인덱스
    var totalItems = $('.misssing_item').length - 2; // 총 이미지 개수

    // 왼쪽 버튼 클릭 시 이벤트
    $('.left_btn').on('click', function() {
        index = (index > 0) ? index - 1 : totalItems - 1; // 인덱스 감소, 맨 처음이면 마지막으로 이동
        updateSlider();
    });

    // 오른쪽 버튼 클릭 시 이벤트
    $('.right_btn').on('click', function() {
        index = (index < totalItems - 1) ? index + 1 : 0; // 인덱스 증가, 맨 끝이면 처음으로 이동
        updateSlider();
    });

    // 슬라이드 업데이트 함수
    function updateSlider() {
        var slideWidth = $('.misssing_item').outerWidth(true); // 각 슬라이드의 너비 (마진 포함)
        var newTransformValue = -index * slideWidth; // 새로운 변환 값 계산
        $slider.css('transform', 'translateX(' + newTransformValue + 'px)'); // 슬라이드 이동
    }

    // 설정 시간마다 오른쪽 버튼 클릭 효과 추가
    setInterval(function() {
        index = (index < totalItems - 1) ? index + 1 : 0; // 인덱스 증가, 맨 끝이면 처음으로 이동
        updateSlider();
    }, 3000);

    let timeout;

    $(document).mousemove(function() {
        // 버튼을 보이게 합니다.
        $('.left_btn').fadeIn();
        $('.right_btn').fadeIn();

        // 이전 타이머가 있다면 클리어합니다.
        clearTimeout(timeout);

        // 설정 시간 후에 버튼을 숨기는 타이머 설정
        timeout = setTimeout(function() {
            $('.left_btn').fadeOut();
            $('.right_btn').fadeOut();
        }, 1000);
    });
});

function gpsChackOn() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
            const latitude = position.coords.latitude;
            const longitude = position.coords.longitude;

            // 위치 정보를 서버로 전송
            $.ajax({
                url: '/usr/gpsChack/on',
                type: 'POST',
                data: {
                    latitude: latitude,
                    longitude: longitude
                },
                success: function (response) {
                    if (response.success) {
                        $('.gps_popup').fadeOut();
                        alert('위치 정보가 저장되었습니다.');
                    } else {
                        alert('위치 정보를 저장하지 못했습니다.');
                    }
                },
                error: function (xhr, status, error) {
                    console.error('Error:', error);
                    alert('위치 정보 저장 중 오류가 발생했습니다.');
                }
            });
        }, function (error) {
            alert('위치 정보를 가져올 수 없습니다. \n먼저 위치 정보 권한을 켜주세요');
            console.error(error);
        });
    } else {
        alert('이 브라우저는 위치 정보를 지원하지 않습니다.');
    }
}

function gpsChackOff() {
    $.ajax({
        url: '/usr/gpsChack/off',
        type: 'POST',
        success: function (response) {
            if (response.success) {
                $('.gps_popup').fadeOut();
                alert('정보 수신을 거절하였습니다');
            }
        },
        error: function (xhr, status, error) {
            console.error('Error:', error);
            alert('정보 수신 거절 중 오류가 발생했습니다.');
        }
    });
}

function gpsChackUpdate() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
            const latitude = position.coords.latitude;
            const longitude = position.coords.longitude;

            // 위치 정보를 서버로 전송
            $.ajax({
                url: '/usr/gpsChack/update',
                type: 'POST',
                data: {
                    latitude: latitude,
                    longitude: longitude
                },
                success: function (response) {
                    if (response.success) {
                        $('.gps_popup2').fadeOut();
                        alert('위치 정보가 저장되었습니다.');
                    } else {
                        alert('위치 정보를 저장하지 못했습니다.');
                    }
                },
                error: function (xhr, status, error) {
                    console.error('Error:', error);
                    alert('위치 정보 저장 중 오류가 발생했습니다.');
                }
            });
        }, function (error) {
            alert('위치 정보를 가져올 수 없습니다. \n먼저 위치 정보 권한을 켜주세요');
            console.error(error);
        });
    } else {
        alert('이 브라우저는 위치 정보를 지원하지 않습니다.');
    }
}

function gpsChackCancel() {
    $('.gps_popup2').fadeOut();
    alert('정보 수신을 거절하였습니다');
}