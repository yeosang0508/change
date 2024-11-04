import React, {useState} from 'react';
import {createRoot} from 'react-dom/client';
import {Button, TextField, Container, Typography, Box, Divider, Link} from '@mui/material';
import Head from './head'; // 헤더 컴포넌트 import

function LoginForm() {
    const [loginId, setLoginId] = useState('');
    const [loginPw, setLoginPw] = useState('');

    const checkLogin = async (e) => {
        e.preventDefault();

        // URL 인코딩 방식으로 데이터 생성

        const formData = new URLSearchParams();
        formData.append('loginId', loginId);
        formData.append('loginPw', loginPw);

        try {
            const response = await fetch('/usr/member/doLogin', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: formData.toString(),
            });

            const data = await response.json();

            // jsAction에 포함된 JavaScript 코드 실행
            if (data.jsAction) {
                eval(data.jsAction);
            }
        } catch (error) {
            console.error('로그인 중 오류가 발생했습니다:', error);
            alert('로그인 중 오류가 발생했습니다.');
        }
    };


    return (
        <Box sx={{
            backgroundColor: '#FFFEF0',
            minHeight: '100vh',
            display: 'flex',
            alignItems: 'flex-start',
            justifyContent: 'center',
            pt: 20,
        }}>
            <Container maxWidth="sm">
                <Typography variant="h4" component="h1" gutterBottom>
                    Login
                </Typography>
                <form id="login" method="POST" action="/usr/member/doLogin" onSubmit={checkLogin}>
                    <TextField label="아이디" name="loginId" variant="outlined" fullWidth margin="normal"
                               color="success" onChange={(e) => setLoginId(e.target.value)}/>
                    <TextField label="비밀번호" name="loginPw" type="password" variant="outlined" fullWidth
                               margin="normal"
                               color="success" onChange={(e) => setLoginPw(e.target.value)}/>
                    <Box display="flex" justifyContent="flex-end" mt={2}>
                        <Button variant="contained" color="success" type="submit">
                            로그인
                        </Button>
                    </Box>
                </form>
                <Box textAlign="center" mt={4}>
                    <Typography variant="body2">
                        Don't have an account?{' '}
                        <Link href="#" underline="hover" color="success">
                            Sign up
                        </Link>
                    </Typography>
                </Box>
                <Box display="flex" alignItems="center" mt={2}>
                    <Divider sx={{flexGrow: 1}}/>
                    <Typography variant="body2" sx={{mx: 2}}>
                        or
                    </Typography>
                    <Divider sx={{flexGrow: 1}}/>
                </Box>
                <Box textAlign="center" mt={2}>
                    <Button variant="outlined" color="success" fullWidth
                            sx={{textTransform: 'none', borderColor: '#d3d3d3', color: "success"}}>
                        Sign in with Google
                    </Button>
                </Box>
            </Container>
        </Box>
    );
}

// 헤더 컴포넌트를 별도로 렌더링
document.addEventListener("DOMContentLoaded", function () {
    const headRootElement = document.getElementById('head-root');
    if (headRootElement) {
        const headRoot = createRoot(headRootElement);
        headRoot.render(<Head/>);
    } else {
        console.error("Target container 'head-root' not found.");
    }

    // 로그인 폼 컴포넌트를 별도로 렌더링
    const rootElement = document.getElementById('login-root');
    if (rootElement) {
        const root = createRoot(rootElement);
        root.render(<LoginForm/>);

        document.body.style.overflow = 'hidden';
    } else {
        console.error("Target container 'login-root' not found.");
    }
});
