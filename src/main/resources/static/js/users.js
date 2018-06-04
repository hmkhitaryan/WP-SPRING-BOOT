$(document).ready(function () {
    $('#findUsers').blur(function () {
        initUsernamefind('foundUserUl', 'noUserFoundUl', 'No user found with that Username');
    });
});

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function initAddFriend() {
    $.ajax({
        type: "POST",
        url: "/addFriend",
        data: {
            receiverUsername: $('#findUsers').val()
        },
        success: function (response) {
            if (response.status != "SUCCESS") {
                var foundUserArea = document.querySelector('#foundUserUl');
                foundUserArea.style['background-color'] = '#ff5652';
            } else {
                foundUserArea.style['background-color'] = '#00ff04';
            }
        },
        error: function (responseText) {
            $('#' + 'foundUserUl').text(responseText);
        }
    });
}

function initUsernamefind(successUl, errorUl, errorMessage) {
    var userPrincipalText = $('#userPrincipal').text();
    var userPrincipal = userPrincipalText.substr(userPrincipalText.indexOf(' ') + 1, userPrincipalText.length);
    var userToFind = $('#findUsers').val();
    if (userPrincipal.trim() === userToFind) {
        $('#' + errorUl).text('');
        $('#' + errorUl).text('Can not find yourself');
        return false;
    }
    $.ajax({
        type: "POST",
        url: 'findUser',
        data: {
            username: userToFind
        },
        success: function (response) {
            var username = response.username;
            var noUserFoundArea = document.querySelector('#noUserFoundUl');
            if (username === undefined) {
                $('#' + errorUl).text('');
                var errorMessageText = document.createTextNode(errorMessage);
                noUserFoundArea.style['background-color'] = '#ff5652';
                noUserFoundArea.appendChild(errorMessageText);
            } else {
                noUserFoundArea.classList.add('hidden');
                var foundUserArea = document.querySelector('#foundUserUl');

                var usernameTextElement = document.createElement('li');
                var avatarElement = document.createElement('i');
                var avatarText = document.createTextNode(username[0]);
                avatarElement.appendChild(avatarText);
                avatarElement.style['background-color'] = getAvatarColor(username);
                usernameTextElement.appendChild(avatarElement);

                var usernameElement = document.createElement('span');
                var usernameText = document.createTextNode(response.username);
                usernameElement.appendChild(usernameText);

                var friendButtonElement = document.createElement('button');
                friendButtonElement.setAttribute("id", "addFriend");
                // friendButtonElement.setAttribute("class", "btn btn-lg btn-primary btn-block");
                // friendButtonElement.setAttribute("value", "Contact");
                friendButtonElement.innerHTML = 'contact';
                friendButtonElement.style['background-color'] = '#4CAF50';
                friendButtonElement.style['margin'] = '1px 0.5px';
                friendButtonElement.onclick = function () {
                    $.ajax({
                        type: "POST",
                        url: "/addFriend",
                        data: {
                            receiverUsername: $('#findUsers').val()
                        },
                        success: function (response) {
                            var foundUserArea = document.querySelector('#foundUserUl');
                            if (response.status != "SUCCESS") {
                                foundUserArea.style['background-color'] = '#ff5652';
                            } else {
                                // foundUserArea.style['background-color'] = '#00ff04';

                                var successLiElement = document.createElement('li');
                                var successElement = document.createElement('i');
                                successElement.style['background-color'] = '#00ff04';
                                successLiElement.appendChild(successElement);
                                foundUserArea.appendChild(successLiElement);
                                friendButtonElement.style['background-color'] = '#ff5652';
                                friendButtonElement.innerHTML = 'unfriend';
                                friendButtonElement.onclick = function () {
                                    $.ajax({
                                        type: "POST",
                                        url: "/unFriend",
                                        data: {
                                            receiverUsername: $('#findUsers').val()
                                        },
                                        success: function (response) {
                                            var foundUserArea = document.querySelector('#foundUserUl');
                                            if (response.status != "SUCCESS") {
                                                friendButtonElement.style['background-color'] = '#ff5652';
                                            } else {
                                                friendButtonElement.style['background-color'] = '#00ff04';
                                            }
                                        },
                                        error: function (responseText) {
                                            $('#' + 'foundUserUl').text(responseText);
                                        }
                                    });
                                }
                            }
                        },
                        error: function (responseText) {
                            $('#' + 'foundUserUl').text(responseText);
                        }
                    });
                    return false;
                };
                usernameElement.appendChild(friendButtonElement);
                usernameTextElement.appendChild(usernameElement);
                foundUserArea.appendChild(usernameTextElement);
            }
        },
        error: function (responseText) {
            $('#' + errorUl).text(responseText);
        }
    });
}

function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);

    return colors[index];
}