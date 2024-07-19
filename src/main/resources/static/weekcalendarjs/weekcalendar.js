  function toggleDatePicker() {
        const datePicker = document.getElementById('datePicker');
        datePicker.classList.toggle('hidden');
    }

    function navigateWeek(offset) {
        const currentDate = new Date(document.querySelector('input[type="date"]').value);
        currentDate.setDate(currentDate.getDate() + offset * 7);
        const newDate = currentDate.toISOString().split('T')[0];
        window.location.href = `/calendar/week?date=${newDate}`;
    }

    function navigateToDate() {
        const selectedDate = document.getElementById('datePicker').value;
        window.location.href = `/calendar/week?date=${selectedDate}`;
    }