function fetchSuggestions() {
    const query = document.getElementById('search-input').value;
    if (query.length < 1) {
        document.getElementById('suggestions').classList.add('hidden');
        return;
    }

    fetch(`/search/suggestions?query=${query}`)
        .then(response => response.text())
        .then(html => {
            const suggestionsList = document.getElementById('suggestions');
            suggestionsList.innerHTML = html;
            suggestionsList.classList.remove('hidden');
        });
}