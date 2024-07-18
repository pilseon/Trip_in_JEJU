document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');
    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        events: function(fetchInfo, successCallback, failureCallback) {
            fetch('/calendar/events')
                .then(response => response.json())
                .then(data => {
                    data.forEach(event => {
                        event.backgroundColor = getRandomColor();
                        event.borderColor = getRandomColor();
                    });
                    successCallback(data);
                });
        },
        locale: 'ko',
        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay'
        },
        dayCellContent: function(e) {
            e.dayNumberText = e.dayNumberText.replace('일', ''); // '일' 제거
        }
    });
    calendar.render();

    function getRandomColor() {
        var letters = '0123456789ABCDEF';
        var color = '#';
        for (var i = 0; i < 6; i++) {
            color += letters[Math.floor(Math.random() * 16)];
        }
        return color;
    }
});