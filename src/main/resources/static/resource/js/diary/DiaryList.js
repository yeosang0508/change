function setSortValue() {
    const sortValue = localStorage.getItem('sort') || 'newest'; // 기본값 설정
    document.getElementById('sortSelect').value = sortValue;
}

function saveSortValue() {
    const sortValue = document.getElementById('sortSelect').value;
    localStorage.setItem('sort', sortValue);
}

window.onload = setSortValue;