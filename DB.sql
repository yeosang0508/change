DROP DATABASE IF EXISTS `tails_route`;
CREATE DATABASE `tails_route`;
USE `tails_route`;

## 회원정보 테이블
CREATE TABLE `member`(
                         id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '식별번호',
                         regDate DATETIME NOT NULL COMMENT '가입 날짜',
                         updateDate DATETIME NOT NULL COMMENT '수정 날짜',
                         loginId CHAR(30) NOT NULL COMMENT '아이디',
                         loginPw CHAR(100) NOT NULL COMMENT '비밀번호',
                         authLevel SMALLINT(2) UNSIGNED DEFAULT 3 COMMENT '권한 레벨 (3=일반, 7=관리자)',
                         `name` CHAR(20) NOT NULL COMMENT '오프라인 이름',
                         nickname CHAR(20) NOT NULL COMMENT '온라인 이름',
                         gender TINYINT(1) UNSIGNED NOT NULL COMMENT '성별 (0=여자, 1=남자)',
                         cellphoneNum CHAR(20) NOT NULL COMMENT '전화번호',
                         delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '탈퇴 여부 (0=탈퇴 전, 1=탈퇴 후)',
                         delDate DATETIME COMMENT '탈퇴 날짜'
);

## 회원정보 테이블 테스트 데이터

INSERT INTO `member` SET
                         regDate = '2024-02-05 14:15:00',
                         updateDate = '2024-03-10 16:30:00',
                         loginId = 'admin',
                         loginPw = 'pw_hash2',
                         authLevel = 7,
                         `name` = '관리자',
                         nickname = '강아지왕',
                         gender = 1,
                         cellphoneNum = '010-8765-4321',
                         delStatus = 0;

INSERT INTO `member` SET
                         regDate = '2024-01-10 10:30:00',
                         updateDate = '2024-02-10 12:00:00',
                         loginId = 'user01',
                         loginPw = 'pw_hash1',
                         authLevel = 3,
                         `name` = '김서준',
                         nickname = '콩이의대장',
                         gender = 1,
                         cellphoneNum = '010-1234-5678',
                         delStatus = 0;


INSERT INTO `member` SET
                         regDate = '2024-03-20 09:45:00',
                         updateDate = '2024-04-25 14:15:00',
                         loginId = 'user02',
                         loginPw = 'pw_hash3',
                         authLevel = 3,
                         `name` = '이지아',
                         nickname = '바둑이의수호자',
                         gender = 0,
                         cellphoneNum = '010-1111-2222',
                         delStatus = 1,
                         delDate = '2024-05-01 10:30:00';

INSERT INTO `member` SET
                         regDate = '2024-04-18 16:00:00',
                         updateDate = '2024-05-20 09:00:00',
                         loginId = 'user03',
                         loginPw = 'pw_hash4',
                         authLevel = 3,
                         `name` = '박도윤',
                         nickname = '두부의행복전도사',
                         gender = 1,
                         cellphoneNum = '010-3333-4444',
                         delStatus = 0;

INSERT INTO `member` SET
                         regDate = '2024-05-22 11:30:00',
                         updateDate = '2024-06-10 15:45:00',
                         loginId = 'user04',
                         loginPw = 'pw_hash5',
                         authLevel = 3,
                         `name` = '최하은',
                         nickname = '뭉치의천사',
                         gender = 0,
                         cellphoneNum = '010-5555-6666',
                         delStatus = 0;

INSERT INTO `member` SET
                         regDate = '2024-07-22 12:20:00',
                         updateDate = '2024-08-01 12:40:00',
                         loginId = 'asd',
                         loginPw = 'asd',
                         authLevel = 3,
                         `name` = '유은희',
                         nickname = '꾸미엄마',
                         gender = 0,
                         cellphoneNum = '010-7698-1532',
                         delStatus = 0;


## 반려견 테이블
CREATE TABLE `dog`(
                      id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '식별번호',
                      regDate DATETIME NOT NULL COMMENT '생성 날짜',
                      updateDate DATETIME NOT NULL COMMENT '수정 날짜',
                      memberId INT(10) UNSIGNED NOT NULL COMMENT '주인 식별번호',
                      `name` CHAR(20) NOT NULL DEFAULT '이름 없음' COMMENT '이름',
                      weight CHAR(20) NOT NULL DEFAULT '모름' COMMENT '체중',
                      photo CHAR(50) NOT NULL COMMENT '사진',
                      `type` CHAR(20) NOT NULL COMMENT '소형, 중형, 대형',
                      comPortName CHAR(20) COMMENT 'GPS 기기 연결 포트'
);

INSERT INTO dog SET
                    regDate = '2024-01-01 10:00:00',
                    updateDate = '2024-01-01 10:00:00',
                    memberId = 1,
                    weight = 5,
                    photo = '/resource/photo/dog1.png',
                    `type` = '소형';

INSERT INTO dog SET
                    regDate = '2024-02-15 14:30:00',
                    updateDate = '2024-02-15 14:30:00',
                    memberId = 3,
                    `name` = '바둑이',
                    photo = '/resource/photo/dog2.png',
                    `type` = '중형';

INSERT INTO dog SET
                    regDate = '2024-03-20 09:15:00',
                    updateDate = '2024-03-20 09:15:00',
                    memberId = 5,
                    `name` = '뭉치',
                    weight = 15,
                    photo = '/resource/photo/dog3.png',
                    `type` = '대형';

INSERT INTO dog SET
                    regDate = '2024-04-25 16:45:00',
                    updateDate = '2024-04-25 16:45:00',
                    memberId = 6,
                    `name` = '꾸미',
                    weight = 4,
                    photo = '/resource/photo/dog4.png',
                    `type` = '소형',
                    comPortName = "COM7";

## 게시글 테이블
CREATE TABLE article(
                        id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '식별번호',
                        regDate DATETIME NOT NULL COMMENT '작성 날짜',
                        updateDate DATETIME NOT NULL COMMENT '수정 날짜',
                        memberId INT(10) UNSIGNED NOT NULL COMMENT '작성자 식별번호',
                        boardId INT(10) UNSIGNED NOT NULL COMMENT '게시판 식별번호',
                        title CHAR(100) NOT NULL COMMENT '제목',
                        `body` TEXT NOT NULL COMMENT '내용',
                        hitCount INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '조회수',
                        goodReactionPoint int(10) unsigned not null default 0 COMMENT '좋아요',
                        badReactionPoint INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '싫어요'
);

## 게시판 테이블
CREATE TABLE board(
                      id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '식별번호',
                      regDate DATETIME NOT NULL COMMENT '생성 날짜',
                      updateDate DATETIME NOT NULL COMMENT '수정 날짜',
                      `code` CHAR(100) NOT NULL UNIQUE COMMENT 'notice(공지사항) free(자유) Q&A(질의응답)',
                      `name` CHAR(20) NOT NULL UNIQUE COMMENT '이름',
                      delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '삭제 여부 (0=삭제 전, 1=삭제 후)',
                      delDate DATETIME COMMENT '삭제 날짜'
);

## 게시판 테스트 데이터
INSERT INTO board
SET regDate = NOW(),
updateDate = NOW(),
`code` = 'NOTICE',
`name` = '공지사항';

INSERT INTO board
SET regDate = NOW(),
updateDate = NOW(),
`code` = 'FREE',
`name` = '자유';

INSERT INTO board
SET regDate = NOW(),
updateDate = NOW(),
`code` = 'QnA',
`name` = '질의응답';

INSERT INTO board
SET regDate = NOW(),
updateDate = NOW(),
`code` = 'recommend',
`name` = '추천합니다';

## 리액션(좋아요, 싫어요) 테이블
CREATE TABLE reactionPoint(
                              id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '식별번호',
                              regDate DATETIME NOT NULL COMMENT '추천 날짜',
                              updateDate DATETIME NOT NULL COMMENT '수정 날짜',
                              memberId INT(10) UNSIGNED NOT NULL COMMENT '추천자 식별번호',
                              relTypeCode CHAR(20) NOT NULL COMMENT '추천대상 식별코드',
                              relId INT(10) UNSIGNED NOT NULL COMMENT '추천대상 식별번호',
                              `point` INT(10) COMMENT '좋아요, 싫어요 여부'
);

## 댓글 테이블
CREATE TABLE reply (
                       id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '식별번호',
                       regDate DATETIME NOT NULL COMMENT '작성 날짜',
                       updateDate DATETIME NOT NULL COMMENT '수정 날짜',
                       memberId INT(10) UNSIGNED NOT NULL COMMENT '작성자 식별번호',
                       relTypeCode CHAR(50) NOT NULL COMMENT '작성대상 식별코드',
                       relId INT(10) UNSIGNED NOT NULL COMMENT '작성대상 식별번호',
                       `body` TEXT NOT NULL COMMENT '내용',
                       goodReactionPoint int(10) unsigned not null default 0 COMMENT '좋아요',
                       badReactionPoint INT(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '싫어요'
);

## 알람 테이블
CREATE TABLE alarm (
                       id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '식별번호',
                       regDate DATETIME NOT NULL COMMENT '생성 날짜',
                       updateDate DATETIME NOT NULL COMMENT '수정 날짜',
                       memberId INT(10) UNSIGNED NOT NULL COMMENT '생성자 식별번호',
                       alarm_date DATE COMMENT '알람이 울릴 날짜',
                       alarm_day ENUM('월요일', '화요일', '수요일', '목요일', '금요일', '토요일', '일요일') COMMENT '알람이 울릴 요일',
                       message TEXT NOT NULL COMMENT '알람 메시지',
                       site TEXT NOT NULL COMMENT '어느 사이트에서 왔는지 여부',
                       is_sent BOOLEAN DEFAULT FALSE COMMENT '알람이 이미 전송되었는지 여부',
                       FOREIGN KEY (memberId) REFERENCES MEMBER(id),
                       CHECK ((alarm_date IS NOT NULL) OR (alarm_day IS NOT NULL))
);

## 생필품 테이블
CREATE TABLE essentials (
                            id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '식별번호',
                            regDate DATETIME NOT NULL COMMENT '생성 날짜',
                            updateDate DATETIME NOT NULL COMMENT '수정 날짜',
                            memberId INT(10) UNSIGNED NOT NULL COMMENT '생성자 식별번호',
                            itemType CHAR(20) NOT NULL COMMENT '생필품 종류',
                            purchaseDate DATE NOT NULL COMMENT '구매 날짜',
                            usageCycle INT(10) NOT NULL COMMENT '사용주기',
                            timing INT(10) NOT NULL COMMENT '알림 시기'
);

## 약 체크 테이블
CREATE TABLE medicationLog (
                               id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '식별번호',
                               regDate DATETIME NOT NULL COMMENT '생성 날짜',
                               memberId INT(10) UNSIGNED NOT NULL COMMENT '사용자 식별번호',
                               medicationDate DATE NOT NULL COMMENT '복용 날짜'
);

## 실종 테이블
CREATE TABLE missing(
                        id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '식별번호',
                        memberId INT(10) UNSIGNED NOT NULL COMMENT '신고자 식별번호',
                        `name` CHAR(30) NOT NULL COMMENT '강아지 이름',
                        reportDate DATETIME NOT NULL COMMENT '실종 날짜',
                        missingLocation VARCHAR(100) NOT NULL COMMENT '실종 장소',
                        breed CHAR(30) NOT NULL COMMENT '품종',
                        color CHAR(30) NOT NULL COMMENT '색상',
                        gender CHAR(30) NOT NULL COMMENT '성별',
                        age CHAR(30) DEFAULT '모름' COMMENT '나이',
                        photo TEXT NOT NULL COMMENT '사진',
                        RFID CHAR(30) DEFAULT '없음' COMMENT '마이크로칩 번호',
                        trait TEXT NOT NULL COMMENT '특징'
);

## 실종 테스트 데이터
INSERT INTO `missing` SET
                          memberId = 1,
                          `name` = '바둑이',
                          reportDate = '2024-10-05 09',
                          missingLocation  = '경기도 포천시 창수면 옥수로 320-64',
                          breed = '믹스견',
                          color = 'BLACK TAN&WHITE',
                          gender = '암컷',
                          age = '108',
                          photo = '/resource/photo/missing1.png',
                          trait = '흰 바탕에 표범같은 작은 점들이 많이 있고 귀 엉덩이 허리부분에 진갈색 과 검정색이 섞인 큰 얼룩이 있어요';

INSERT INTO `missing` SET
                          memberId = 2,
                          `name` = '몽이',
                          reportDate = '2024-10-10 08',
                          missingLocation  = '서울특별시 강서구 강서로45길 113 (내발산동)올라가는길',
                          breed = '포메라니안',
                          color = '흰색',
                          gender = '암컷',
                          age = '192',
                          photo = '/resource/photo/missing2.png',
                          trait = '포메스피츠 믹스견';

INSERT INTO `missing` SET
                          memberId = 3,
                          `name` = '콩이',
                          reportDate = '2024-10-10 09',
                          missingLocation  = '강원특별자치도 강릉시 주문진읍 신리천로 4-1 (해안연립)19동201호',
                          breed = '말티즈',
                          color = '흰색',
                          gender = '수컷',
                          age = '7',
                          photo = '/resource/photo/missing3.png',
                          RFID = '410100012908279',
                          trait = '흰색 미니 칩심어져있음 사람에게 호의적';

INSERT INTO `missing` SET
                          memberId = 4,
                          `name` = '로또',
                          reportDate = '2024-10-06 20',
                          missingLocation  = '전북특별자치도 김제시 부량면 벽골제로 320-13벽골제 지평선축제장',
                          breed = '시바',
                          color = '흑갈색',
                          gender = '수컷',
                          age = '34',
                          photo = '/resource/photo/missing4.png',
                          RFID = '410160010746866',
                          trait = '전체적으로 갈색검정털에 입주위 가슴 등쪽 날개모양 흰털 검은코 동그랗게 말린 꼬리';

INSERT INTO `missing` SET
                          memberId = 5,
                          `name` = '하루',
                          reportDate = '2024-10-05 16',
                          missingLocation  = '경상남도 남해군 설천면 설천로775번길 256-17남해양떼목장양모리학교',
                          breed = '보더 콜리',
                          color = '검정&흰색',
                          gender = '수컷',
                          photo = '/resource/photo/missing5.png',
                          trait = '오른쪽뒷다리를다쳐서 절음';

INSERT INTO `missing` SET
                          memberId = 6,
                          `name` = '멍이',
                          reportDate = '2024-10-05 22',
                          missingLocation  = '울산광역시 동구 등대로 95 (일산동)대왕암공원 주차장 일대',
                          breed = '비글',
                          color = '세가지색',
                          gender = '수컷',
                          age = '10',
                          photo = '/resource/photo/missing6.png',
                          trait = '목뒤 M자 무늬가 있음';

INSERT INTO `missing` SET
                          memberId = 1,
                          `name` = '구름이',
                          reportDate = '2024-10-05 07',
                          missingLocation  = '대전광역시 중구 보문로 341 (선화동, 현대아파트)101동',
                          breed = '기타',
                          color = '흑색&회색',
                          gender = '암컷',
                          age = '46',
                          photo = '/resource/photo/missing7.png',
                          trait = '사람을 엄청나게 경계함 지금은 사진보다 털이 많이 자란상태임';

INSERT INTO `missing` SET
                          memberId = 2,
                          `name` = '누비',
                          reportDate = '2024-10-03 10',
                          missingLocation  = '경기도 김포시 하성면 원통로28번길 37',
                          breed = '기타',
                          color = '흰색',
                          gender = '수컷',
                          age = '52',
                          photo = '/resource/photo/missing8.png',
                          trait = '폼피츠 이고 특별한 특징은 없읍니다';

INSERT INTO `missing` SET
                          memberId = 3,
                          `name` = '모카',
                          reportDate = '2024-09-28 15',
                          missingLocation  = '경상북도 문경시 굴모리길 14 (불정동)초록울타리집',
                          breed = '진도견',
                          color = '황색',
                          gender = '암컷',
                          age = '148',
                          photo = '/resource/photo/missing9.png',
                          trait = '귀 속에 진도견인증도장찍혀있음 ㆍ선꼬리';

INSERT INTO `missing` SET
                          memberId = 4,
                          `name` = '초코',
                          reportDate = '2024-09-25 06',
                          missingLocation  = '전라남도 강진군 강진읍 사의재길 31-23동성리 전 침례교회 아래집',
                          breed = '믹스견',
                          color = '황갈색',
                          gender = '수컷',
                          age = '96',
                          photo = '/resource/photo/missing10.png',
                          trait = '이름 만득이 진도 믹스 파랑빨강 목줄 꼬리 말아 올려져 있음 다리 길고 귀 쫑긋 털이 지저분함';

INSERT INTO `missing` SET
                          memberId = 5,
                          `name` = '복실이',
                          reportDate = '2024-09-20 06',
                          missingLocation  = '충청북도 청주시 흥덕구 풍년로198번길 45-6 (가경동)3층',
                          breed = '말티즈',
                          color = '흰색',
                          gender = '수컷',
                          age = '122',
                          photo = '/resource/photo/missing11.png',
                          trait = '심장이 안좋아 자꾸 켁켁 거려요';

##건강기록 테이블
CREATE TABLE doghealth(
                          memberId INT(10) UNSIGNED NOT NULL COMMENT 'member번호',
                          dogWeight FLOAT NOT NULL COMMENT '강아지 체중',
                          vaccinationDate DATETIME NOT NULL COMMENT '예방 접종 날짜',
                          checkupDate DATETIME NOT NULL COMMENT '건강 검진 날짜',
                          activityLevel FLOAT NOT NULL COMMENT '활동량(평균걸음수)'
);

## 일지작성  테이블
CREATE TABLE Diary(
                      Id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '일지 고유번호',
                      regDate DATETIME NOT NULL COMMENT '생성일',
                      updateDate DATETIME NOT NULL COMMENT'수정일',
                      memberId INT(10) NOT NULL COMMENT'회원번호',
                      title CHAR(200) NOT NULL COMMENT'제목',
                      `body` TEXT NOT NULL COMMENT'내용',
                      imagePath CHAR(200) NOT NULL COMMENT '이미지 저장경로',
                      startDate DATE NOT NULL COMMENT '약복용 시작일',
                      endDate DATE NOT NULL COMMENT '약복용 종료일',
                      takingTime TIME NOT NULL COMMENT '복용 시간',
                      information TEXT NOT NULL COMMENT '복용약 특이사항'
);

##반려견 행동범위 지정 테이블
CREATE TABLE gpsAlert(
                         id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '식별번호',
                         regDate DATETIME NOT NULL COMMENT '등록 날짜',
                         updateDate DATETIME NOT NULL COMMENT '수정 날짜',
                         dogId INT(10) UNSIGNED NOT NULL COMMENT '반려견 식별번호',
                         latitude DECIMAL(10, 7) NOT NULL COMMENT '설정한 위도',
                         longitude DECIMAL(10, 7) NOT NULL COMMENT '설정한 경도',
                         chack INT(1) UNSIGNED DEFAULT 0 COMMENT '범위 벗어났는지 여부',
                         switch INT(1) UNSIGNED DEFAULT 0 COMMENT 'gps 알림 온오프 여부'
);

## 일지작성 테스트데이터
INSERT INTO Diary (regDate, updateDate, memberId, title, BODY, imagePath, startDate, endDate, takingTime, information) VALUES
('2023-01-01 10:00:00', '2023-01-01 10:00:00', 1, 'First Diary Entry', 'Today I started my medication.', '/images/entry1.jpg', '2023-01-01', '2023-01-10', '08:00:00', 'Take with food.'),
('2023-01-02 11:00:00', '2023-01-02 11:00:00', 1, 'Second Diary Entry', 'Feeling good so far.', '/images/entry2.jpg', '2023-01-01', '2023-01-10', '08:00:00', 'No side effects noted.'),
('2023-01-03 12:00:00', '2023-01-03 12:00:00', 2, 'New Member Diary', 'Just started taking medication.', '/images/entry3.jpg', '2023-01-03', '2023-01-20', '09:00:00', 'Monitor for headaches.'),
('2023-01-04 13:00:00', '2023-01-04 13:00:00', 3, 'Medication Update', 'Had to change my dosage.', '/images/entry4.jpg', '2023-01-05', '2023-01-15', '07:30:00', 'Increase dosage as advised.'),
('2023-01-05 14:00:00', '2023-01-05 14:00:00', 1, 'Dietary Changes', 'Made some dietary changes to support my health.', '/images/entry5.jpg', '2023-01-01', '2023-01-10', '08:00:00', 'Avoid dairy while on medication.'),
('2023-01-06 15:00:00', '2023-01-06 15:00:00', 2, 'Feeling Tired', 'Noticed I am feeling more tired lately.', '/images/entry6.jpg', '2023-01-03', '2023-01-20', '09:00:00', 'Consult doctor if fatigue persists.'),
('2023-01-07 16:00:00', '2023-01-07 16:00:00', 3, 'Midway Check', 'Halfway through my medication course.', '/images/entry7.jpg', '2023-01-05', '2023-01-15', '07:30:00', 'Feeling hopeful about results.'),
('2023-01-08 17:00:00', '2023-01-08 17:00:00', 1, 'Final Days', 'Last few days of medication.', '/images/entry8.jpg', '2023-01-01', '2023-01-10', '08:00:00', 'Reflecting on my journey.'),
('2023-01-09 18:00:00', '2023-01-09 18:00:00', 2, 'Follow-up Appointment', 'Had a follow-up appointment today.', '/images/entry9.jpg', '2023-01-03', '2023-01-20', '09:00:00', 'Doctor is pleased with progress.'),
('2023-01-10 19:00:00', '2023-01-10 19:00:00', 3, 'Completion', 'Finished my medication course.', '/images/entry10.jpg', '2023-01-05', '2023-01-15', '07:30:00', 'Celebrate the achievement!');


## 병원 테이블
CREATE TABLE hospital(
                         id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '고유 병원 ID',
                         `name` TEXT NOT NULL COMMENT '병원 이름',
                         callNumber VARCHAR(20) DEFAULT NULL COMMENT '소재지전화번호',
                         jibunAddress TEXT COMMENT '병원의 지번 주소',
                         roadAddress TEXT COMMENT '병원의 도로명 주소',
                         latitude VARCHAR(20) DEFAULT NULL COMMENT '좌표정보(x)',
                         longitude VARCHAR(20) DEFAULT NULL COMMENT '좌표정보(y)',
                         businessStatus ENUM('영업', '폐업') DEFAULT '영업' COMMENT '영업 상태',
                         `type` ENUM('일반', '야간', '24시간') NOT NULL DEFAULT '일반' COMMENT '병원 타입'
);
CREATE TABLE cart(
                     id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '식별번호',
                     regDate DATETIME NOT NULL COMMENT '가입 날짜',
                     updateDate DATETIME NOT NULL COMMENT '수정 날짜',
                     memberId INT(10) UNSIGNED NOT NULL COMMENT '생성자 식별번호',
                     itemName TEXT NOT NULL COMMENT '제품이름',
                     itemprice INT(10) UNSIGNED NOT NULL COMMENT '제품가격',
                     itemlink TEXT NOT NULL COMMENT '제품사이트'
);

## GPS 수신 정보 동의 테이블
CREATE TABLE gpsChack(
                         id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '식별번호',
                         memberId INT(10) UNSIGNED NOT NULL COMMENT '접속자 식별번호',
                         regDate DATETIME NOT NULL COMMENT '등록 날짜',
                         updateDate DATETIME NOT NULL COMMENT '수정 날짜',
                         latitude DECIMAL(10, 7) COMMENT '현재 위도',
                         longitude DECIMAL(10, 7) COMMENT '현재 경도',
                         location VARCHAR(100) COMMENT '장소'
);

USE `tails_route`;
SHOW TABLES;

INSERT INTO article
SET
    regDate = NOW() + INTERVAL FLOOR(RAND() * 100000000) SECOND,
updateDate = NOW() + INTERVAL FLOOR(RAND() * 100000000) SECOND,
memberId = FLOOR(1 + RAND() * 6),
boardId = FLOOR(1 + RAND() * 3),
title = CONCAT('제목', FLOOR(RAND() * 10000)),
`body` = CONCAT('내용', FLOOR(RAND() * 10000));