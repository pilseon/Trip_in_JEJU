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

function toggleActionSheet() {
  const actionSheet = document.getElementById("actionSheet");
  const arrowIcon = document.getElementById("arrowIcon");
  if (actionSheet.classList.contains("show")) {
    actionSheet.classList.remove("visible");
    arrowIcon.classList.remove("rotate");
    setTimeout(() => {
      actionSheet.classList.remove("show");
    }, 300); // transition 시간과 일치하도록 지연 시간을 줍니다.
  } else {
    actionSheet.classList.add("show");
    setTimeout(() => {
      actionSheet.classList.add("visible");
      arrowIcon.classList.add("rotate");
    }, 10); // 약간의 지연을 주어 transition이 제대로 작동하도록 합니다.
  }
}

function toggleSubMenu(event) {
  event.preventDefault();
  const subMenu = event.currentTarget.nextElementSibling;
  const plusIcon = event.currentTarget.querySelector('#plusIcon');

  if (subMenu.classList.contains('hidden')) {
    subMenu.classList.remove('hidden');
    subMenu.style.maxHeight = subMenu.scrollHeight + "px";
    plusIcon.classList.remove('fa-plus');
    plusIcon.classList.add('fa-minus');
  } else {
    subMenu.style.maxHeight = subMenu.scrollHeight + "px";
    setTimeout(() => {
      subMenu.style.maxHeight = 0;
    }, 0); // 바로 maxHeight를 0으로 설정하여 부드럽게 줄어들도록 함
    plusIcon.classList.remove('fa-minus');
    plusIcon.classList.add('fa-plus');
    setTimeout(() => {
      subMenu.classList.add('hidden');
    }, 300); // transition 시간과 일치하도록 지연 시간을 줍니다.
  }
}