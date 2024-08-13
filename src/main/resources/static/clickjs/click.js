function clickEffect(e) {
  var d = document.createElement("div");
  d.className = "clickEffect";
  d.style.top = e.clientY + "px";
  d.style.left = e.clientX + "px";
  document.body.appendChild(d);
  d.addEventListener(
    "animationend",
    function () {
      d.parentElement.removeChild(d);
    }.bind(this)
  );
}
document.addEventListener("click", clickEffect);

// 기존 toggleActionSheet 함수 수정
function toggleActionSheet() {
  const actionSheet = document.getElementById("actionSheet");
  const arrowIcon = document.getElementById("arrowIcon");

  if (actionSheet.classList.contains("show")) {
    actionSheet.classList.remove("visible");
    arrowIcon.classList.remove("rotate");
    setTimeout(() => {
      actionSheet.classList.remove("show");
    }, 300); // transition 시간과 일치하도록 지연 시간을 줍니다.

    // document 클릭 이벤트 제거
    document.removeEventListener('click', handleClickOutside);
  } else {
    actionSheet.classList.add("show");
    setTimeout(() => {
      actionSheet.classList.add("visible");
      arrowIcon.classList.add("rotate");
    }, 10); // 약간의 지연을 주어 transition이 제대로 작동하도록 합니다.

    // document 클릭 이벤트 추가
    setTimeout(() => {
      document.addEventListener('click', handleClickOutside);
    }, 300); // transition 완료 후 이벤트 리스너 추가
  }
}

function toggleSubMenu(event) {
  event.preventDefault();
  const subMenu = event.currentTarget.nextElementSibling;
  const plusIcon = event.currentTarget.querySelector('#plusIcon');

  if (subMenu.classList.contains('hidden')) {
    subMenu.classList.remove('hidden');
    subMenu.style.maxHeight = subMenu.scrollHeight + "px"; // maxHeight를 컨텐츠 높이로 설정
    plusIcon.classList.remove('fa-plus');
    plusIcon.classList.add('fa-minus');
  } else {
    subMenu.style.maxHeight = 0;
    plusIcon.classList.remove('fa-minus');
    plusIcon.classList.add('fa-plus');
    setTimeout(() => {
      subMenu.classList.add('hidden');
    }, 300); // 애니메이션 후에 숨김 처리
  }
}

function handleClickOutside(event) {
  const actionSheet = document.getElementById("actionSheet");
  const arrowIcon = document.getElementById("arrowIcon");

  if (!actionSheet.contains(event.target) && !arrowIcon.contains(event.target)) {
    toggleActionSheet();
  }
}

// 아이콘 클릭 시 이벤트 전파 방지 및 toggleActionSheet 호출
document.getElementById("arrowIcon").addEventListener("click", function (event) {
  event.stopPropagation();
  toggleActionSheet();
});

document.getElementById('chat-icon').addEventListener('click', function(event) {
    event.preventDefault();
    // 필요한 경우 링크로 이동하는 코드
     window.location.href = "/api/chat/openai";
});

document.getElementById('close-icon').addEventListener('click', function(event) {
    event.stopPropagation(); // 부모의 클릭 이벤트 전파를 막음
    var chatIcon = document.getElementById('chat-icon');
    chatIcon.classList.add('slide-down');

    // 아이콘이 사라진 후에 DOM에서 완전히 제거
    setTimeout(function() {
        chatIcon.style.display = 'none';
    }, 500); // 0.5초 후에 제거 (애니메이션 시간과 맞춤)
});
