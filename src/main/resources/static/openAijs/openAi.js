// 채팅 메시지를 표시할 DOM
const chatMessages = document.querySelector('#chat-messages');
// 사용자 입력 필드
const userInput = document.querySelector('#user-input input');
// 전송 버튼
const sendButton = document.querySelector('#user-input button');
// OpenAI API 엔드포인트 주소를 변수로 저장
const apiEndpoint = '/api/chat/send'; // 서버의 프록시 엔드포인트

const botImage = 'https://github.com/user-attachments/assets/e89358fc-34f4-446e-9f11-f277775def5b'; // 챗봇 이미지 URL

// 메시지 추가 함수에서 사용
function addMessage(message, isUser = false) {
    if (!message || (typeof message === 'string' && message.trim() === '')) return;

    // 메시지가 객체일 경우, 문자열로 변환 (이전 예제에서처럼)
    if (typeof message !== 'string') {
        message = JSON.stringify(message, null, 2);
    }

    const messageContainer = document.createElement('div');
    messageContainer.className = `message-container ${isUser ? 'user' : 'bot'} flex items-center`;

    if (!isUser) {
        // 챗봇 이미지 추가
        const imgElement = document.createElement('img');
        imgElement.src = botImage;
        imgElement.alt = 'Bot image';
        imgElement.className = 'thumbnail mr-2';
        messageContainer.appendChild(imgElement);
    }

    const messageElement = document.createElement('div');
    messageElement.className = `message ${isUser ? 'user' : 'bot'} p-2 rounded-md max-w-xs break-words`;

    // 메시지 내용 추가 (HTML 형식 허용)
    messageElement.innerHTML = formatResponseToHtml(message);
    messageContainer.appendChild(messageElement);
    chatMessages.appendChild(messageContainer);
    chatMessages.scrollTop = chatMessages.scrollHeight;
}

// 서버에서 응답을 HTML로 변환하는 함수
function formatResponseToHtml(text) {
    // 정규식을 사용해 숫자와 점 뒤에 <br> 태그를 추가하여 줄바꿈 적용
    let html = text.replace(/(\d+\.\s)/g, '<br>$1');

    // 필요에 따라 추가적인 HTML 변환 로직 추가 가능
    return html;
}

// 서버의 프록시 엔드포인트로 요청
async function fetchAIResponse(prompt) {
    console.log('Sending prompt:', prompt);

    try {
        const requestData = { prompt };

        const response = await fetch(apiEndpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        });

        if (!response.ok) {
            console.error('API 응답 오류:', response.status, response.statusText);
            return 'API 호출 중 오류 발생';
        }

        const data = await response.json();
        console.log('서버 응답:', data);

        // 데이터 유효성 검사
        if (data && data.choices && data.choices[0] && data.choices[0].message && data.choices[0].message.content) {
            return data.choices[0].message.content;
        } else {
            return '유효한 응답이 없습니다.';
        }
    } catch (error) {
        console.error('API 호출 중 오류 발생:', error);
        return 'API 호출 중 오류 발생';
    }
}

// 메시지 전송 처리 함수
async function handleMessageSend(event) {
    event.preventDefault();

    const message = userInput.value.trim();
    if (!message) return;

    addMessage(message, true);
    userInput.value = '';

    const aiResponse = await fetchAIResponse(message);
    if (aiResponse) {
        addMessage(aiResponse, false);
    }
}

// 초기화 코드
document.addEventListener('DOMContentLoaded', (event) => {
    console.log('페이지가 로드되었습니다.');

    // 초기 메시지 추가 방지: 초기화 시 불필요한 메시지 추가 방지 코드 추가 필요
    chatMessages.innerHTML = ''; // 기존 메시지 제거 또는 초기화

    // 전송 버튼 클릭 이벤트 처리
    sendButton.addEventListener('click', handleMessageSend);

    // 입력 필드에서 Enter 키 이벤트 처리
    userInput.addEventListener('keydown', (event) => {
        if (event.key === 'Enter') {
            handleMessageSend(event);
        }
    });
});