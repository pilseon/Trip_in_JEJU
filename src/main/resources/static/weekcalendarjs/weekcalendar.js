function previousWeekDate() {
            let date = new Date(document.querySelector('h1').innerText.split(' ~ ')[0]);
            date.setDate(date.getDate() - 7);
            return date.toISOString().split('T')[0];
        }

        function nextWeekDate() {
            let date = new Date(document.querySelector('h1').innerText.split(' ~ ')[0]);
            date.setDate(date.getDate() + 7);
            return date.toISOString().split('T')[0];
        }