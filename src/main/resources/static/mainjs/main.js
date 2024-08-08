document.addEventListener("DOMContentLoaded", function () {
    let currentIndex = 0;
    const slides = document.querySelectorAll(".event-slide");
    const slideIndicator = document.getElementById("slideIndicator");

    if (slides.length > 0) {
        function updateSlides() {
            slides.forEach((slide, index) => {
                slide.classList.toggle("opacity-100", index === currentIndex);
                slide.classList.toggle("opacity-0", index !== currentIndex);
            });
            slideIndicator.textContent = `${currentIndex + 1}/${slides.length}`;
        }

        function nextSlide() {
            currentIndex = (currentIndex + 1) % slides.length;
            updateSlides();
        }

        function prevSlide() {
            currentIndex = (currentIndex - 1 + slides.length) % slides.length;
            updateSlides();
        }

        document.getElementById("nextButton").addEventListener("click", nextSlide);
        document.getElementById("prevButton").addEventListener("click", prevSlide);

        setInterval(nextSlide, 10000); // 3초마다 슬라이드 전환

        updateSlides();
    }
});