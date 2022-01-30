
const apiPostFile = 'http://localhost:8081/api/files/upload';
const apiUpdateFile = 'http://localhost:8081/api/users/profile/cover-image/';
const urlProfile = 'http://localhost:8081/profile';

var loadFileCover = function(event) {
    var reader = new FileReader();
    reader.onload = function(){
        var output = document.getElementById('cover_image');
        output.src = reader.result;
    };
    reader.readAsDataURL(event.target.files[0]);
    uploadFileCover();
};


function uploadFileCover() {
    let photo = document.getElementById('UpImageCover').files[0];

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
            let fileId = document.querySelector('#coverImgId');
            fileId.value = response.id;
        }
    };
}


var loadFileAvatar = function(event) {
    var reader = new FileReader();
    reader.onload = function(){
        var output = document.getElementById('avatar');
        output.src = reader.result;
    };
    reader.readAsDataURL(event.target.files[0]);
    uploadFileAvatar();
};


function uploadFileAvatar() {
    let photo = document.getElementById('UpImageAvatar').files[0];

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
            let fileId = document.querySelector('#avatarImgId');
            fileId.value = response.id;
        }
    };
}

function updateAvatar() {
    var avatarId = document.querySelector('#avatarImgId').value;
    updateFile(avatarId, "AVATAR");
}

function updateAvatarCover() {
    var coverAvatarId = document.querySelector('#coverImgId').value;
    updateFile(coverAvatarId, "COVER");
}

function updateFile(fileId, type) {
    let xhr = new XMLHttpRequest();
    let accessToken = getCookie("jwt-token")
    xhr.open('PUT', apiUpdateFile + fileId + "/" + type);
    xhr.setRequestHeader('Authorization', 'Bearer ' + accessToken);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.send();
    xhr.onload = function() {
        if (xhr.status != 200) {
            alert(`Call API Error`);
        } else { // show the result
            let response = xhr.responseText;
            if (response != null) {
                window.location.href = urlProfile;
            }
        }
    };
}
