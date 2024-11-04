import React, { useState } from 'react';
import ReactDOM from 'react-dom';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';

function App() {
    const [date, setDate] = useState(new Date());

    const handleDateChange = (newDate) => {
        setDate(newDate);
        console.log("선택한 날짜:", newDate.toISOString().split('T')[0]); // 선택된 날짜를 로그로 출력
    };

    return (
        <div>
            <div id="Calendar">
                <Calendar onChange={handleDateChange} value={date} />
            </div>
        </div>
    );
}

// React 컴포넌트를 HTML 요소에 렌더링
ReactDOM.render(<App />, document.getElementById('root'));