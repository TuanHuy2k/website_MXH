'use strict';

const url = 'http://localhost:8081';
const apiFetchUsers = 'http://localhost:8081/api/users/friends';
const apiHistoryMessage = 'http://localhost:8081/api/message/history/'

// declare stompClient
var stompClient;

let selectedUser;
let newMessages = new Map()
let currentUser;
let friends;

function onConnected() {
    var socket = new SockJS(url + '/ws');
    stompClient = Stomp.over(socket);
    currentUser = getCookie("username");
    stompClient.connect({}, function (frame) {
        console.log("connected to: " + frame);
        for (let i = 0; i < friends.length; i++) {
            var userConnect = friends[i].username;
            stompClient.subscribe("/topic/messages/" + userConnect + "/" + currentUser, function (response) {
                let data = JSON.parse(response.body);
                render(data.content, data.sender);
            });
        }
    });
}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

function sendMsg(to, text) {
    stompClient.send("/app/chat/" +currentUser + "/" + to, {}, JSON.stringify({
        type: 'CHAT',
        sender: currentUser,
        content: text
    }));
}

function fetchAllUsers() {
    // var response = callApi();
    let xhr = new XMLHttpRequest();
    let accessToken = getCookie("jwt-token")
    xhr.open('GET', apiFetchUsers, true);
    xhr.setRequestHeader('Authorization', 'Bearer ' + accessToken);
    xhr.send();
    xhr.onload = function() {
        if (xhr.status != 200) {
            alert(`Error ${xhr.status}: ${xhr.statusText}`); // e.g. 404: Not Found
        } else { // show the result
            var users = JSON.parse(this.responseText);
            friends = users;
            let usersTemplateHTML = "";
            onConnected();
            for (let i = 0; i < users.length; i++) {

                // connect to all user

                // setTimeout(function () {
                //     onConnected(users[i].username);
                // }.bind(this), 2000);
                var avatarUser = users[i].avatarUrl;

                usersTemplateHTML = usersTemplateHTML +
                    '          <li class="nav-item">\n' +
                    '              <a href="#" class="" onclick="selectUser(\'' + users[i].id + '\', \'' + users[i].username + '\') ">' +
                    '                <figure>' +
                    '                    <img src="' + users[i].avatarUrl +'" width="40px" height="40px" alt="avatar" style="width: 40px; height: 40px"/>\n' +
                    '                    <span class="status f-online"></span>' +
                    '                </figure>' +
                    '                <div class="user-name">\n' +
                    '                    <h10 id="userNameAppender_' + users[i].id + '" class="name"> ' + users[i].username + '</h10>\n' +
                    '                </div>\n' +
                    '              </a>' +
                    '          </li>';
            }
            var userList = document.querySelector('#usersList');
            let div = document.createElement('div');
            div.innerHTML = usersTemplateHTML;
            userList.appendChild(div);
        }
    };
}

function selectUser(selectedUserId, userName) {
    console.log("selecting users: " + userName);
    // remove old history
    $chatHistoryList.innerHtml = '';
    while ($chatHistoryList.firstChild) $chatHistoryList.removeChild($chatHistoryList.firstChild);

    selectedUser = selectedUserId;

    // get history message
    let xhr = new XMLHttpRequest();
    let accessToken = getCookie("jwt-token");
    let currentUserId = getCookie("userId");
    xhr.open('GET', apiHistoryMessage + currentUserId + "/" + selectedUserId , true);
    xhr.setRequestHeader('Authorization', 'Bearer ' + accessToken);
    xhr.send();
    xhr.onload = function() {
        if (xhr.status != 200) {
            alert(`Error ${xhr.status}: ${xhr.statusText}`); // e.g. 404: Not Found
        } else {
            var messages = JSON.parse(this.responseText);
            for (let i = 0; i < messages.length; i++) {
                // neu selectedUserId = receiverId => message hien thi ben trai
                // nguoc lai thi hien thi ben phai
                var message = messages[i];
                if (message.receiverId === +selectedUserId) {
                    renderHistoryMessageRight(message);
                } else {
                    renderHistoryMessageLeft(message);
                }
            }
        };
    }

    let isNew = document.getElementById("newMessage_" + selectedUserId) !== null;
    if (isNew) {
        let element = document.getElementById("newMessage_" + selectedUserId);
        element.parentNode.removeChild(element);
        render(newMessages.get(selectedUserId), selectedUserId);
    }
    document.querySelector('#selectedUserId').innerHTML = '';
    document.querySelector('#selectedUserId').append(userName);
}

