// DOM이 준비되면 실행
$(document).ready(function () {
    const $dropArea = $('.drop_area'); // 드롭 영역 선택
    const $inputFile = $('.dog_photo'); // 파일 입력 필드 선택
    const $preview = $('.preview'); // 미리보기 이미지 선택
    const $uploadButton = $('.image_button'); // 사진 업로드 버튼 선택

    // 드래그 오버 이벤트
    $dropArea.on('dragover', function (event) {
        event.preventDefault(); // 기본 동작 방지
        $(this).addClass('highlight'); // 드롭 영역 강조
    });

    // 드래그 리브 이벤트
    $dropArea.on('dragleave', function () {
        $(this).removeClass('highlight'); // 드롭 영역 강조 제거

        const files = event.originalEvent.dataTransfer.files; // 드롭된 파일 가져오기
        if (files.length > 0) {
            $inputFile[0].files = files; // 파일 입력 필드에 드롭된 파일 설정
            updatePreview(files[0]); // 미리보기 업데이트 함수 호출
        }
    });

    // 드롭 이벤트
    $dropArea.on('drop', function (event) {
        event.preventDefault(); // 기본 동작 방지
        $(this).removeClass('highlight'); // 드롭 영역 강조 제거

        const files = event.originalEvent.dataTransfer.files; // 드롭된 파일 가져오기
        if (files.length > 0) {
            $inputFile[0].files = files; // 파일 입력 필드에 드롭된 파일 설정
            updatePreview(files[0]); // 미리보기 업데이트 함수 호출
        }
    });

    // 사진 업로드 버튼 클릭 시 파일 입력 필드 클릭
    $uploadButton.on('click', function () {
        $inputFile.click(); // 파일 입력 필드 클릭
    });

    // 파일 입력 필드 변경 시 미리보기 업데이트
    $inputFile.on('change', function () {
        const files = this.files; // 선택된 파일 가져오기
        if (files.length > 0) {
            updatePreview(files[0]); // 미리보기 업데이트 함수 호출
        }
    });

    // 미리보기 업데이트 함수
    function updatePreview(file) {
        const fileReader = new FileReader();
        fileReader.onload = function (e) {
            $preview.attr('src', e.target.result); // 미리보기 이미지 src 설정
            $preview.show(); // 이미지 표시
        }
        fileReader.readAsDataURL(file); // 파일을 Data URL로 읽기
    }
});

function preview_clear() {
    const $preview = $('.preview'); // 미리보기 이미지 선택
    $preview.css('display', 'none');  // 미리보기 이미지 숨기기
}
