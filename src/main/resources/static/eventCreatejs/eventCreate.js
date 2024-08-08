document.getElementById("addStep").addEventListener("click", function () {
  var stepContainer = document.getElementById("stepContainer");
  var newStep = document.createElement("input");
  newStep.type = "file";
  newStep.name = "steps";
  newStep.multiple = true;
  newStep.className =
        "block w-full text-sm text-gray-900 border border-gray-300 rounded-lg cursor-pointer bg-gray-50 focus:outline-none";
  stepContainer.appendChild(newStep);
});
