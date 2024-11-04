import React from 'react';
import 'font-awesome/css/font-awesome.min.css';

function Head() {
    return (
        <div style={{ position: 'fixed', top: 0, left: 0, height: '60px', width: '100%', zIndex: 20, backgroundColor: '#4D3E3E', color: 'white', fontFamily: 'Arial, sans-serif', margin: 0, padding: 0 }}>
            <div style={{ display: 'flex', alignItems: 'center', height: '100%' }}>
                <div style={{ marginLeft: '10px' }}>
                    <a href="../home/main" style={{ color: 'white', fontSize: '24px', textDecoration: 'none' }}>TailsRoute</a>
                </div>
                <div style={{ flexGrow: 1, display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100%' }}>
                    <ul style={{ display: 'flex', listStyleType: 'none', padding: '0 20px', margin: 0, height: '100%' }}>
                        <li style={{ flex: 'none', height: '100%' }}>
                            <a href="../hospital/main" style={{ color: 'white', padding: '0 20px', display: 'flex', alignItems: 'center', justifyContent: 'center', textDecoration: 'none', height: '100%' }}>병원</a>
                        </li>
                        <li style={{ flex: 'none', height: '100%' }}>
                            <a href="../article/community" style={{ color: 'white', padding: '0 20px', display: 'flex', alignItems: 'center', justifyContent: 'center', textDecoration: 'none', height: '100%' }}>커뮤니티</a>
                        </li>
                        <li style={{ flex: 'none', height: '100%' }}>
                            <a href="../Records/page" style={{ color: 'white', padding: '0 20px', display: 'flex', alignItems: 'center', justifyContent: 'center', textDecoration: 'none', height: '100%' }}>일상기록</a>
                        </li>
                        <li style={{ flex: 'none', height: '100%' }}>
                            <a href="../shopping/page" style={{ color: 'white', padding: '0 20px', display: 'flex', alignItems: 'center', justifyContent: 'center', textDecoration: 'none', height: '100%' }}>쇼핑</a>
                        </li>
                        <li style={{ flex: 'none', height: '100%' }}>
                            <a href="../health/page" style={{ color: 'white', padding: '0 20px', display: 'flex', alignItems: 'center', justifyContent: 'center', textDecoration: 'none', height: '100%' }}>건강관리</a>
                        </li>
                        <li style={{ flex: 'none', height: '100%' }}>
                            <a href="../missing/list" style={{ color: 'white', padding: '0 20px', display: 'flex', alignItems: 'center', justifyContent: 'center', textDecoration: 'none', height: '100%' }}>실종</a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    );
}

export default Head;