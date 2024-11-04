const path = require('path');

module.exports = {
    mode: 'development', // 개발 모드
    entry: './src/main/resources/static/resource/js/login.js', // 번들링할 파일 경로
    output: {
        path: path.resolve(__dirname, '../../dist'), // 번들된 파일의 경로
        filename: 'login.bundle.js', // 생성될 번들 파일 이름
    },
    module: {
        rules: [
            {
                test: /\.jsx?$/, // .js 또는 .jsx 파일을 대상으로 Babel 로더 사용
                exclude: /node_modules/, // node_modules 폴더 제외
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['@babel/preset-env', '@babel/preset-react'], // 최신 JavaScript와 JSX 변환
                    },
                },
            },
            {
                test: /\.css$/, // CSS 파일 처리를 위한 로더
                use: ['style-loader', 'css-loader', 'postcss-loader'], // Tailwind CSS와 함께 사용 가능
            },
        ],
    },
    resolve: {
        extensions: ['.js', '.jsx'], // .js와 .jsx 확장자를 자동으로 처리
    },
};
