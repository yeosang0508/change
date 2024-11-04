// AJAX 요청을 보내는 함수
function deleteLocation(dogId) {
    $.ajax({
        url: '/usr/gpsAlert/deleteLocation',
        type: 'POST',
        data: {
            dogId: dogId
        },
        success: function() {
            alert("삭제되었습니다.");
            window.location.reload();
        },
        error: function(xhr, status, error) {
            alert(xhr.responseText);
            console.log(xhr.responseText);
        }
    });
}