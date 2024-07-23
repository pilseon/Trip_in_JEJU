document.addEventListener('DOMContentLoaded', function() {
                var calendarEl = document.getElementById('calendar');
                var calendar = new FullCalendar.Calendar(calendarEl, {
                    initialView: 'dayGridMonth',
                    headerToolbar: {
                        left: 'prev,next today',
                        center: 'title',
                        right: 'dayGridMonth,timeGridWeek,timeGridDay'
                    },
                    selectable: true,
                    selectHelper: true,
                    events: '/calendar/events',
                    select: function(info) {
                        var title = prompt('Event Title:');
                        if (title) {
                            var eventData = {
                                title: title,
                                start: info.startStr,
                                end: info.endStr
                            };
                            calendar.addEvent(eventData);

                            $.ajax({
                                url: '/calendar/calendars',
                                type: 'POST',
                                contentType: 'application/json',
                                data: JSON.stringify(eventData),
                                success: function() {
                                    alert('Event saved successfully.');
                                }
                            });
                        }
                        calendar.unselect();
                    }
                });
                calendar.render();
            });