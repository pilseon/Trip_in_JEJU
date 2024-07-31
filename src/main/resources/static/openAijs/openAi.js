// 채팅 메시지를 표시할 DOM
const chatMessages = document.querySelector('#chat-messages');
// 사용자 입력 필드
const userInput = document.querySelector('#user-input input');
// 전송 버튼
const sendButton = document.querySelector('#user-input button');
// OpenAI API 엔드포인트 주소를 변수로 저장
const apiEndpoint = '/api/chat/send'; // 서버의 프록시 엔드포인트

function addMessage(sender, message, isUser = false) {
    if (!message || message.trim() === '') return; // message가 null이거나 빈 문자열이면 반환

    const messageElement = document.createElement('div');
    messageElement.className = `message ${isUser ? 'user' : 'bot'} p-2 rounded-md max-w-xs break-words`;
    messageElement.textContent = `${sender}: ${message}`;
    chatMessages.appendChild(messageElement);
    chatMessages.scrollTop = chatMessages.scrollHeight; // 새 메시지가 추가되면 스크롤을 아래로
}

// 서버의 프록시 엔드포인트로 요청
async function fetchAIResponse(prompt) {
    try {
        const response = await fetch(apiEndpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ prompt })
        });

        if (!response.ok) {
            console.error('API 응답 오류:', response.status, response.statusText);
            return 'API 호출 중 오류 발생';
        }

        const data = await response.text(); // 또는 .json()
        console.log('서버 응답:', data); // 디버깅용 로그
        return data;
    } catch (error) {
        console.error('API 호출 중 오류 발생:', error);
        return 'API 호출 중 오류 발생';
    }
}

// 메시지 전송 처리 함수
async function handleMessageSend(event) {
    event.preventDefault(); // 기본 동작(새로고침) 방지

    const message = userInput.value.trim();
    if (!message) return;

    addMessage('나', message, true);
    userInput.value = '';

    const aiResponse = await fetchAIResponse(message);
    addMessage('챗봇', aiResponse);
}

// 전송 버튼 클릭 이벤트 처리
sendButton.addEventListener('click', handleMessageSend);

// 사용자 입력 필드에서 Enter 키 이벤트를 처리
userInput.addEventListener('keydown', (event) => {
    if (event.key === 'Enter') {
        handleMessageSend(event);
    }
});
document.getElementById('user-input').addEventListener('submit', async (event) => {
    event.preventDefault(); // 기본 동작(새로고침) 방지

    const message = userInput.value.trim();
    if (!message) return;

    addMessage('나', message, true);
    userInput.value = '';

    const aiResponse = await fetchAIResponse(message);
    addMessage('챗봇', aiResponse);
});