  function toggleDatePicker() {
            var datePicker = document.getElementById('datePicker');
            datePicker.classList.toggle('hidden');
        }

        function navigateToDate() {
            var selectedDate = document.getElementById('datePicker').value;
            window.location.href = '/member/myPage?date=' + selectedDate;
        }

        function navigateToDateWithClick(element) {
            var selectedDate = element.getAttribute('data-date');
            window.location.href = '/member/myPage?date=' + selectedDate;
        }

        document.addEventListener("DOMContentLoaded", function() {
            const buttons = document.querySelectorAll(".nav-button");
            const tabContents = document.querySelectorAll(".tab-content");

            function updateTabVisibility(selectedButton) {
                const target = 'tab-' + selectedButton.id;

                // 선택된 탭 표시를 위한 active 클래스 추가/제거
                buttons.forEach(btn => {
                    if (btn === selectedButton) {
                        btn.classList.add("active");
                    } else {
                        btn.classList.remove("active");
                    }
                });

                // 탭 내용 표시
                tabContents.forEach(tabContent => {
                    if (tabContent.id === target) {
                        tabContent.style.display = "block";
                    } else {
                        tabContent.style.display = "none";
                    }
                });
            }

            buttons.forEach(button => {
                button.addEventListener("click", () => {
                    updateTabVisibility(button);
                });
            });

            // 페이지가 처음 로드될 때 일정 탭을 활성화
            updateTabVisibility(document.getElementById("schedule"));
        });
