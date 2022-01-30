let $chatHistory;
let $button;
let $textarea;
let $chatHistoryList;

function init() {
    cacheDOM();
    bindEvents();
}

function bindEvents() {
    $button.addEventListener('click', addMessage.bind(this));
    $textarea.addEventListener('keyup', addMessageEnter.bind(this));
}

function cacheDOM() {
    $chatHistory = document.querySelector('.chat-history');
    $button = document.querySelector('#sendBtn');
    $textarea = document.querySelector('#message-to-send');
    $chatHistoryList = document.querySelector('.chat-history > ul');
}

function render(message, sender) {
    scrollToBottom();
    // responses
    var templateResponse = Handlebars.compile(document.querySelector('#message-response-template').innerHTML);
    var contextResponse = {
        response: message,
        time: getCurrentTime(),
        userName: sender
    };

    setTimeout(function () {
        $chatHistoryList.insertAdjacentHTML('beforeend', templateResponse(contextResponse));
        scrollToBottom();
    }.bind(this), 1000);
}

function sendMessage(message) {
    let username = document.querySelector('#selectedUserId').textContent;
    sendMsg(username, message);
    scrollToBottom();
    if (message.trim() !== '') {
        var template = Handlebars.compile(document.querySelector("#message-template").innerHTML);
        var context = {
            messageOutput: message,
            time: getCurrentTime(),
            toUserName: selectedUser
        };

        $chatHistoryList.insertAdjacentHTML('beforeend', template(context));
        scrollToBottom();
        $textarea.value = '';
    }
}

function scrollToBottom() {
    $chatHistory.scrollTop = 100;
}

function getCurrentTime() {
    return new Date().toLocaleTimeString().replace(/([\d]+:[\d]{2})(:[\d]{2})(.*)/, "$1$3");
}

function parseLongTime(timeMilliseconds) {
    return new Date(timeMilliseconds).toLocaleDateString().replace(/([\d]+:[\d]{2})(:[\d]{2})(.*)/, "$1$3")
}

function addMessage() {
    sendMessage($textarea.value);
}

function addMessageEnter(event) {
    // enter was pressed
    if (event.keyCode === 13) {
        addMessage();
    }
}

function renderHistoryMessageLeft(message) {
    scrollToBottom();
    // responses
    var templateResponse = Handlebars.compile(document.querySelector('#message-response-template').innerHTML);
    var contextResponse = {
        response: message.messageContent,
        time: parseLongTime(message.sendingTime * 1000),
        userName: message.receiverId
    };

    $chatHistoryList.insertAdjacentHTML('beforeend', templateResponse(contextResponse));
}

function renderHistoryMessageRight(message) {
    scrollToBottom();
    var template = Handlebars.compile(document.querySelector("#message-template").innerHTML);
    var context = {
        messageOutput: message.messageContent,
        time: parseLongTime(message.sendingTime * 1000),
        toUserName: message.senderId
    };

    $chatHistoryList.insertAdjacentHTML('beforeend', template(context));
    scrollToBottom();
    $textarea.value = '';
}

init();

