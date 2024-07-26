function toggleDatePicker() {
    const datePicker = document.getElementById('datePicker');
    datePicker.classList.toggle('hidden');
}

function navigateToDate() {
    const date = document.getElementById('datePicker').value;
    if (date) {
        window.location.href = `/calendar/week?date=${date}`;
    }
}

function navigateToDateWithClick(element) {
    const date = element.getAttribute('data-date');
    if (date) {
        window.location.href = `/calendar/week?date=${date}`;
    }
}