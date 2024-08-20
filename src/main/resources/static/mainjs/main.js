document.addEventListener("DOMContentLoaded", function () {

    // 이벤트 슬라이드 처리
    handleEventSlides();
    handleTodayPickSlides();

    var swiper = new Swiper('.swiper-container', {
            slidesPerView: 3,
            spaceBetween: 30,
            centeredSlides: true,
            loop: true, // 슬라이드를 무한 반복
            autoplay: {
                delay: 2000, // 2초마다 슬라이드 넘김
                disableOnInteraction: false, // 사용자 상호작용 후에도 자동 재생 유지
            },
            navigation: {
                nextEl: '.swiper-button-next',
                prevEl: '.swiper-button-prev',
            }
        });

    function handleEventSlides() {
        let currentIndex = 0;
        const slides = document.querySelectorAll(".event-slide");
        const slideIndicator = document.getElementById("slideIndicator");
        const slideControls = document.getElementById("slideControls");

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

            setInterval(nextSlide, 7000); // 7초마다 슬라이드 전환

            updateSlides();
        } else {
            // 슬라이드가 없을 경우 컨트롤을 숨깁니다.
            if (slideControls) {
                slideControls.style.display = "none";
            }
        }
    }

    // "오늘의 Pick" 슬라이드 처리
    function handleTodayPickSlides() {
        const track = document.querySelector('.carousel-track');
        const items = document.querySelectorAll('.carousel-item');
        const prevButton = document.querySelector('.carousel-prev');
        const nextButton = document.querySelector('.carousel-next');

        console.log(track); // 트랙 요소 확인
        console.log(items); // 슬라이드 아이템 확인

        if (items.length === 0) return;

        const itemWidth = items[0].getBoundingClientRect().width;

        let currentIndex = 0;

        function moveToSlide(index) {
            track.style.transform = `translateX(-${index * itemWidth}px)`;
            currentIndex = index;
        }

        function nextSlide() {
            if (currentIndex < items.length - 1) {
                moveToSlide(currentIndex + 1);
            } else {
                moveToSlide(0); // 마지막 슬라이드에서 첫 번째 슬라이드로 이동
            }
        }

        function prevSlide() {
            if (currentIndex > 0) {
                moveToSlide(currentIndex - 1);
            } else {
                moveToSlide(items.length - 1); // 첫 번째 슬라이드에서 마지막 슬라이드로 이동
            }
        }

        // 자동 슬라이드 전환
        setInterval(nextSlide, 5000); // 5초마다 슬라이드 전환

        // 버튼 클릭으로 슬라이드 이동
        nextButton.addEventListener('click', nextSlide);
        prevButton.addEventListener('click', prevSlide);
    }
});
