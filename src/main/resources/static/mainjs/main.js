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
document.addEventListener('DOMContentLoaded', function () {
    const sliderWrapper = document.querySelector('.slider-wrapper');
    const prevButton = document.querySelector('.slider-button-prev');
    const nextButton = document.querySelector('.slider-button-next');

    let offset = 0;
    const itemWidth = document.querySelector('.slider-item').offsetWidth + 16; // 아이템 너비 + 마진
    const slideIntervalTime = 2000; // 자동 슬라이드 시간 (밀리초)
    let slideInterval;
    const totalSlides = document.querySelectorAll('.slider-item').length;
    const visibleSlides = 5; // 화면에 보이는 슬라이드 수 (변경 필요)
    const sliderWidth = totalSlides * itemWidth;

    // 슬라이드를 이동시키는 함수
    function moveSlide(direction) {
        if (direction === 'next') {
            offset -= itemWidth;
            if (-offset >= sliderWidth / 2) {
                offset = 0; // 처음으로 되돌리기
            }
        } else if (direction === 'prev') {
            offset += itemWidth;
            if (offset > 0) {
                offset = - (sliderWidth / 2 - itemWidth); // 마지막으로 되돌리기
            }
        }
        sliderWrapper.style.transform = `translateX(${offset}px)`;
    }

    // 다음 버튼 클릭 이벤트
    nextButton.addEventListener('click', () => {
        moveSlide('next');
        resetSlideInterval();
    });

    // 이전 버튼 클릭 이벤트
    prevButton.addEventListener('click', () => {
        moveSlide('prev');
        resetSlideInterval();
    });

    // 자동 슬라이드 함수
    function startSlideInterval() {
        slideInterval = setInterval(() => {
            moveSlide('next');
        }, slideIntervalTime);
    }

    // 자동 슬라이드를 리셋하는 함수 (사용자가 버튼을 클릭했을 때 자동 슬라이드를 멈췄다가 재개)
    function resetSlideInterval() {
        clearInterval(slideInterval);
        startSlideInterval();
    }

    // 자동 슬라이드 시작
    startSlideInterval();
});
