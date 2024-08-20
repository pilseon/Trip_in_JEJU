// 캔버스 세팅
let canvas = document.getElementById("gameCanvas");
let ctx = canvas.getContext("2d");

let backgroundImage, spaceshipImage, bulletImage, enemyImage, gameoverImage, gamestartImage, oneImage, twoImage, threeImage, restartImage, endImage;
let gameOver = false; // true이면 게임이 끝남, false이면 게임이 안 끝남
let score = 0;

// 플레이어 좌표
let spaceshipX = canvas.width / 2 - 24;
let spaceshipY = canvas.height - 48;

let bulletList = []; // 총알들을 저장하는 리스트
function Bullet() {
  this.x = 0;
  this.y = 0;
  this.init = function() {
    this.x = spaceshipX + 16;
    this.y = spaceshipY;
    this.alive = true; // true면 살아있는 총알 false면 죽은 총알
    bulletList.push(this);
  };

  this.update = function() {
    this.y -= 5;
  };

  this.checkHit = function() {
    for (let i = 0; i < enemyList.length; i++) {
      if (this.y <= enemyList[i].y && this.x >= enemyList[i].x && this.x <= enemyList[i].x + 40) {
        // 총알이 죽게됨, 적군의 우주선이 없어짐, 점수 획득
        score++;
        this.alive = false; // 죽은 총알
        enemyList.splice(i, 1);
      }
    }
  };
}

function generateRandomValue(min, max) {
  let randomNum = Math.floor(Math.random() * (max - min + 1)) + min;
  return randomNum;
}

let enemyList = [];
function Enemy() {
  this.x = 0;
  this.y = 0;
  this.init = function() {
    this.y = 0;
    this.x = generateRandomValue(0, canvas.width - 48);
    enemyList.push(this);
  };
  this.update = function() {
    this.y += 8;
    if (this.y >= canvas.height - 48) {
      gameOver = true;
    }
  };
}

function loadImage() {
  backgroundImage = new Image();
  backgroundImage.src = "/images/background.jpg";

  spaceshipImage = new Image();
  spaceshipImage.src = "/images/spaceship.png";

  bulletImage = new Image();
  bulletImage.src = "/images/bullet.png";

  enemyImage = new Image();
  enemyImage.src = "/images/enemy.png";

  gameoverImage = new Image();
  gameoverImage.src = "/images/gameover.png";

  gamestartImage = new Image();
  gamestartImage.src = "/images/gamestart.png";

  oneImage = new Image();
  oneImage.src = "/images/one.png";

  twoImage = new Image();
  twoImage.src = "/images/two.png";

  threeImage = new Image();
  threeImage.src = "/images/three.png";

  restartImage = new Image();
  restartImage.src = "/images/restart.png";

    endImage = new Image();
    endImage.src = "/images/end.png";
}

let keysDown = {};
function setupKeyboardListener() {
  document.addEventListener("keydown", function(event) { // 키보드에서 키를 눌렀을 때
    keysDown[event.keyCode] = true;
  });
  document.addEventListener("keyup", function(event) { // 키보드에서 키를 떼었을때
    delete keysDown[event.keyCode];
    if (event.keyCode == 32) {
      createBullet(); // 총알 생성 함수
    }
  });
}

function createBullet() {
  let b = new Bullet(); // 총알 하나 생성
  b.init();
}

function createEnemy() {
  const interval = setInterval(function() {
    let e = new Enemy();
    e.init();
    if (gameOver) clearInterval(interval); // 게임 오버 시 적군 생성 중지
  }, 200);
}

function update() {
  if (39 in keysDown) {
    spaceshipX += 7;
  } // right
  if (37 in keysDown) {
    spaceshipX -= 7;
  } // left

  if (spaceshipX <= 0) {
    spaceshipX = 0;
  }
  if (spaceshipX >= canvas.width - 48) {
    spaceshipX = canvas.width - 48;
  }

  // 총알의 y좌표 업데이트하는 함수 호출
  bulletList = bulletList.filter(bullet => bullet.alive); // 살아있는 총알만 남기기
  for (let i = 0; i < bulletList.length; i++) {
    bulletList[i].update();
    bulletList[i].checkHit();
  }

  for (let i = 0; i < enemyList.length; i++) {
    enemyList[i].update();
  }
}

function render() {
  ctx.drawImage(backgroundImage, 0, 0, canvas.width, canvas.height);
  ctx.drawImage(spaceshipImage, spaceshipX, spaceshipY);
  ctx.fillText(`score: ${score}`, 20, 30);
  ctx.fillStyle = "white";
  ctx.font = "20px Arial";
  for (let i = 0; i < bulletList.length; i++) {
    if (bulletList[i].alive) {
      ctx.drawImage(bulletImage, bulletList[i].x, bulletList[i].y);
    }
  }
  for (let i = 0; i < enemyList.length; i++) {
    ctx.drawImage(enemyImage, enemyList[i].x, enemyList[i].y);

  }
}

// member 찾아오기
function getMemberInfo(username) {
    return fetch(`/api/member/info?username=${username}`)
        .then(response => response.json())
        .then(data => data)
        .catch(error => console.error('Error fetching member info:', error));
}

let memberId;

function initializeGame() {
    const username = localStorage.getItem('username'); // 예: 로그인 시 저장된 사용자 이름을 가져옴
    getMemberInfo(username).then(member => {
        memberId = member.id;
        startGame(); // 사용자 정보를 가져온 후 게임을 시작합니다.
    });
}

function main() {
    if (!gameOver) {
        update(); // 좌표값을 업데이트하고
        render(); // 그려주고
        requestAnimationFrame(main); // 재생성
    } else {
        ctx.drawImage(gameoverImage, 10, 100, 380, 380);
        saveScore(memberId, score); // 게임 종료 시 memberId를 사용하여 점수를 저장합니다.
        ctx.drawImage(restartImage, canvas.width / 2 - 250, canvas.height / 2 + 50, 300, 150);
        ctx.drawImage(endImage, canvas.width / 2 - 50, canvas.height / 2 + 50, 300, 150);
    }
}
canvas.addEventListener('click', function(event) {
    let x = event.pageX - canvas.offsetLeft;
    let y = event.pageY - canvas.offsetTop;

    if (gameOver) {
        // Restart 버튼의 위치와 크기
        let restartX = canvas.width / 2 - 150;
        let restartY = canvas.height / 2 + 75;
        let restartWidth = 100;
        let restartHeight = 100;

        // End 버튼의 위치와 크기
        let endX = canvas.width / 2 + 50;
        let endY = canvas.height / 2 + 75;
        let endWidth = 100;
        let endHeight = 100;

        // Restart 버튼이 클릭되었는지 확인
        if (x >= restartX && x <= restartX + restartWidth &&
            y >= restartY && y <= restartY + restartHeight) {
            startGame(); // 게임 재시작
        }

        // End 버튼이 클릭되었는지 확인
        if (x >= endX && x <= endX + endWidth &&
            y >= endY && y <= endY + endHeight) {
            window.location.href = '/'; // 메인 페이지로 이동
        }
    }
});

function startGame() {
  setupKeyboardListener();
  spaceshipX = canvas.width / 2 - 24;
  spaceshipY = canvas.height - 48;
  bulletList = [];
  enemyList = [];
  gameOver = false;
  score = 0;
  createEnemy();
  main();
}

function startCountdown(callback) {
  let countdown = 3;

  const interval = setInterval(() => {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    ctx.drawImage(backgroundImage, 0, 0, canvas.width, canvas.height);
    if (countdown === 3) {
      ctx.drawImage(threeImage, canvas.width / 2 - 50, canvas.height / 2 - 50, 100, 100);
    } else if (countdown === 2) {
      ctx.drawImage(twoImage, canvas.width / 2 - 50, canvas.height / 2 - 50, 100, 100);
    } else if (countdown === 1) {
      ctx.drawImage(oneImage, canvas.width / 2 - 50, canvas.height / 2 - 50, 100, 100);
    }
    countdown--;
    if (countdown < 0) {
      clearInterval(interval);
      callback();
    }
  }, 1000);
}

// 게임 시작 버튼 클릭 이벤트 리스너
document.getElementById("startButton").addEventListener("click", function() {
  this.style.display = "none"; // 버튼 숨기기
  startCountdown(startGame); // 카운트다운 후 게임 시작
});

// 게임 초기화 및 첫 화면 렌더링
function initializeGame() {
  loadImage();
  render(); // 첫 화면 렌더링
  document.getElementById("startButton").style.display = "block"; // 게임 시작 버튼 표시
}

initializeGame(); // 게임 초기화 및 시작

// 점수 저장
function saveScore(memberId, score) {
    fetch('/game/score', { // 서버의 `/game/score` 경로로 POST 요청
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ memberId, score })
    })
    .then(response => response.json())
    .then(data => {
        console.log('Score saved:', data);
    })
    .catch(error => console.error('Error:', error));
}

function loadTopScores() {
    fetch('/game/top-scores') // 서버의 `/game/top-scores` 경로로 GET 요청
        .then(response => response.json())
        .then(data => {
            console.log('Top scores:', data);
            const scoreBoard = document.getElementById('scoreBoard'); // 랭킹을 표시할 요소
            scoreBoard.innerHTML = ''; // 기존 내용을 지우고
            data.forEach((game, index) => {
                const scoreItem = document.createElement('div');
                scoreItem.textContent = `${index + 1}. ${game.member.nickname}: ${game.score}`;
                scoreBoard.appendChild(scoreItem);
            });
        })
        .catch(error => console.error('Error:', error));
}

// 페이지 로드 시 랭킹을 로드
window.onload = loadTopScores;