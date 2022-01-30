const apiPostFile = 'http://localhost:8081/api/files/upload';
const apiPosting = 'http://localhost:8081/api/posting';
const apiWriteComment = 'http://localhost:8081/api/comment';
const urlSearchUser = 'http://localhost:8081/users';

let $comment;
let $buttonComment;
let $index;
let $avatarUrl;

function initPosting() {
    cacheDOMPosting();
    bindEventsPosting();
}

function cacheDOMPosting() {
    $comment = document.querySelector('#write-comment');
    $buttonComment = document.querySelector('#btn-send-comment');
}

function addCommentEnter(event) {
    var postingId = document.querySelector('#posting-id-hidden').value;
    var contentComment = document.querySelector('#write-comment').value;
    if (event.keyCode === 13) {
        writeComment(postingId, contentComment);
    }
}

function bindEventsPosting() {
    if ($comment != undefined) {
        $comment.addEventListener('keyup', addCommentEnter.bind(this));
    }

    if ($buttonComment != undefined) {
        $buttonComment.addEventListener('click', addCommentEnter.bind(this));
    }
}

function writeComment(postingId, contentComment) {

    if (contentComment === "" || contentComment == undefined) {
        return;
    }

    let accessToken = getCookie("jwt-token")

    let xhr = new XMLHttpRequest();
    xhr.open('POST', apiWriteComment);
    xhr.setRequestHeader('Authorization', 'Bearer ' + accessToken);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    var commentRequest = {
        content: contentComment,
        postingId: postingId
    }
    xhr.send(JSON.stringify(commentRequest));
    xhr.onload = function() {
        if (xhr.status != 200) {
            alert(`Call API Error`);
        } else { // show the result
            let response = xhr.responseText;
            if (response != null) {
                let username = getCookie("username")
                var template = Handlebars.compile(document.querySelector("#comment-template").innerHTML);
                var context = {
                    username: username,
                    contentComment: contentComment,
                    avatarUrl: $avatarUrl
                };

                var commentHistory = document.querySelectorAll("#comment-history")[+$index];
                var commentChat = document.querySelectorAll("#write-comment")[+$index];
                commentChat.value = '';

                commentHistory.insertAdjacentHTML('beforeend', template(context));
                scrollToBottom(commentHistory);
            }
        }
    };
}

function sendComment(postingId, index, avatarUrl){
    var contentComment = document.querySelectorAll('#write-comment')[+index].value;
    $index = index;
    $avatarUrl = avatarUrl;
    writeComment(postingId, contentComment);
}

var loadFile = function(event) {
    var reader = new FileReader();
    reader.onload = function(){
        var output = document.getElementById('output');
        output.src = reader.result;
    };
    reader.readAsDataURL(event.target.files[0]);
    uploadFile();
};

function uploadFile() {
    let photo = document.getElementById('file-image').files[0];

    // get token from cookie
    let accessToken = getCookie("jwt-token")
    let userId = getCookie("userId");

    let formData = new FormData();
    formData.append("file", photo);
    formData.append("ownerId", userId);
    formData.append("ownerType", "user");

    // api post file
    let xhr = new XMLHttpRequest();
    xhr.open('POST', apiPostFile);
    xhr.setRequestHeader('Authorization', 'Bearer ' + accessToken);
    xhr.send(formData);

    xhr.onload = function() {
        if (xhr.status != 200) {
            alert(`Call API Error`);
        } else {
            let response = JSON.parse(this.responseText);
            let fileId = document.querySelector('#fileId');
            fileId.value = response.id;
        }
    };
}

function postNewStatus() {
    var content = document.querySelector('#new_status').value;
    var fileId = document.querySelector('#fileId').value;

    let accessToken = getCookie("jwt-token")

    let xhr = new XMLHttpRequest();
    xhr.open('POST', apiPosting);
    xhr.setRequestHeader('Authorization', 'Bearer ' + accessToken);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    var postingRequest = {
        content: content,
        fileId: fileId
    }
    xhr.send(JSON.stringify(postingRequest));
    xhr.onload = function() {
        if (xhr.status != 200) {
            alert(`Call API Error`);
        } else { // show the result
            let response = xhr.responseText;
            if (response != null) {
                window.location.href = "/";
            }
        }
    };
}

function searchUser(event) {
    var keyword = document.querySelector("#nav-search").value;
    if (event.keyCode === 13) {
        window.location.href = urlSearchUser + "?keyword=" + keyword;
    }
}

initPosting();


function scrollToBottom(commentHistory) {
    commentHistory.scrollTop = 100;
}
