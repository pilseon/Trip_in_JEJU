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


            updateTabVisibility(document.getElementById("schedule"));
        });


    function changeDate(days) {

              var storedDate = localStorage.getItem('selectedDate');
              var currentDate = storedDate ? new Date(storedDate) : new Date();


              currentDate.setDate(currentDate.getDate() + days);
              var formattedDate = currentDate.toISOString().split('T')[0];


              localStorage.setItem('selectedDate', formattedDate);


              window.location.href = '/member/myPage?date=' + formattedDate;
          }

          window.onload = function() {
              var storedDate = localStorage.getItem('selectedDate');
              if (storedDate) {
                  // 저장된 날짜를 사용하여 필요한 작업을 수행
                  console.log('저장된 날짜:', storedDate);
              } else {
                  // 저장된 날짜가 없는 경우
                  console.log('저장된 날짜가 없습니다.');
              }
          };

function showDatePicker() {
    const datePicker = document.getElementById('datePicker');
    datePicker.showPicker(); // 직접 캘린더를 보여주는 메서드 호출
}