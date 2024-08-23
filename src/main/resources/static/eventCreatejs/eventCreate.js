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
