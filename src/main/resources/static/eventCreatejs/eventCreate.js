// 이미지 추가 기능
document.getElementById("addStep").addEventListener("click", function () {
    var stepContainer = document.getElementById("stepContainer");

    // 새로운 파일 입력 필드 생성
    var newStep = document.createElement("input");
    newStep.type = "file";
    newStep.name = "steps";
    newStep.className = "block w-full text-sm text-gray-900 border border-gray-300 rounded-lg cursor-pointer bg-gray-50 focus:outline-none mb-2";

    // stepContainer에 새로운 파일 입력 필드 추가
    stepContainer.appendChild(newStep);
});

// 기간 유효성 검사 함수
        function validatePeriod() {
            const periodStart = document.getElementById('periodStart').value;
            const periodEnd = document.getElementById('periodEnd').value;

            if (periodStart && periodEnd && new Date(periodStart) > new Date(periodEnd)) {
                alert('시작 날짜는 종료 날짜보다 이후일 수 없습니다.');
                return false;
            }
            return true;
        }

        document.addEventListener('DOMContentLoaded', function() {
            // 기간 입력 필드에 이벤트 리스너를 추가합니다.
            document.getElementById('periodStart').addEventListener('change', validatePeriod);
            document.getElementById('periodEnd').addEventListener('change', validatePeriod);
        });

        // 폼 제출 시 유효성 검사
        document.getElementById('eventForm').addEventListener('submit', function(event) {
            if (!validatePeriod()) {
                event.preventDefault(); // 유효성 검사 실패 시 폼 제출을 막음
            }
        });
