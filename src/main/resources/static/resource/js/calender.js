document.addEventListener('DOMContentLoaded', function () {
    var calendarEl = document.getElementById('calendar');

    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        events: 'calendar', // API 엔드포인트
        eventClick: function(info) {
            alert('Event: ' + info.event.title);
        },
        eventDataTransform: function(eventData) {
            // eventData의 title이나 다른 속성을 기반으로 className을 설정
            if (eventData.title && eventData.title.includes("약 복용")) {
                eventData.className = "medicineEvent"; // 약 복용 이벤트에 대한 스타일 설정
            } else {
                eventData.className = "diaryEvent"; // 기본 일기 이벤트에 대한 스타일 설정
            }
            return eventData;
        },
            eventTimeFormat: { // 24시간제로 시간 형식 설정
                hour: '2-digit',
                    minute: '2-digit',
                    hour12: false
            },

        headerToolbar: {
            left: 'prev', // 왼쪽 버튼 그룹
            center: 'title', // 중앙에 제목
            right: 'next' // 오른쪽 버튼 그룹
        },
        eventLimit:true,
        eventLimitClick:'day',
        dayMaxEventRows:2

    });

    calendar.render(); // 달력 렌더링
});