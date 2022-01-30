const apiAuthenticate = 'http://localhost:8081/api/authenticate';
const apiRegister = 'http://localhost:8081/api/register';

function login() {
    let username = document.querySelector('#userNameLogin').value;
    let password = document.querySelector('#passwordLogin').value;

    let xhr = new XMLHttpRequest();
    xhr.open('POST', apiAuthenticate);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    var loginRequest = {
        username: username,
        password: password
    }
    xhr.send(JSON.stringify(loginRequest));
    xhr.onload = function() {
        if (xhr.status != 200) {
            alert(`Call API Error`);
        } else { // show the result
            let response = xhr.responseText;
            if (response === "http://") {
                window.location.href = "/";
            }
        }
    };

    xhr.onerror = function() {
        alert("Request failed");
    };
}

function registerAccount() {
    let email = document.querySelector('#emailRegister').value;
    let username = document.querySelector('#usernameRegister').value;
    let password = document.querySelector('#passwordRegister').value;
    let repeatPassword = document.querySelector('#repeatPasswordRegister').value;

    let xhr = new XMLHttpRequest();
    xhr.open('POST', apiRegister);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    var userRegisterRequest = {
        email: email,
        username: username,
        password: password,
        repeatPassword: repeatPassword
    }
    xhr.send(JSON.stringify(userRegisterRequest));
    xhr.onload = function() {
        if (xhr.status != 200) {
            alert(`Call API Error`);
        } else {
            // xử lý ở đoạn này nhé, khi mà response nhận được data
            let response = xhr.responseText;
            // process response of register
        }
    };

    xhr.onerror = function() {
        alert("Request failed");
    };
}
