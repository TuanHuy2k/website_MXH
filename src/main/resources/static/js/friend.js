const apiApproveFriend = 'http://localhost:8081/api/users/friends/approve/';
const apiAddFriend = 'http://localhost:8081/api/users/friends/';
const apiCancelFriend = 'http://localhost:8081/api/users/friends/cancel-friend/';
const urlFriend = 'http://localhost:8081/friends';

function approveFriend(friendId) {
    let xhr = new XMLHttpRequest();
    let accessToken = getCookie("jwt-token")
    xhr.open('GET', apiApproveFriend + friendId);
    xhr.setRequestHeader('Authorization', 'Bearer ' + accessToken);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.send();
    xhr.onload = function() {
        if (xhr.status != 200) {
            alert(`Call API Error`);
        } else { // show the result
            let response = xhr.responseText;
            if (response != null) {
                window.location.href = urlFriend;
            }
        }
    };
}

function addFriend(friendId) {
    let xhr = new XMLHttpRequest();
    let accessToken = getCookie("jwt-token")
    xhr.open('POST', apiAddFriend + friendId);
    xhr.setRequestHeader('Authorization', 'Bearer ' + accessToken);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.send();
    xhr.onload = function() {
        if (xhr.status != 200) {
            alert(`Call API Error`);
        } else { // show the result
            let response = xhr.responseText;
            if (response != null) {
                window.location.href = urlFriend;
            }
        }
    };
}

function cancelFriend(friendId) {
    let xhr = new XMLHttpRequest();
    let accessToken = getCookie("jwt-token")
    xhr.open('GET', apiCancelFriend + friendId);
    xhr.setRequestHeader('Authorization', 'Bearer ' + accessToken);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.send();
    xhr.onload = function() {
        if (xhr.status != 200) {
            alert(`Call API Error`);
        } else { // show the result
            let response = xhr.responseText;
            if (response != null) {
                window.location.href = urlFriend;
            }
        }
    };
}
