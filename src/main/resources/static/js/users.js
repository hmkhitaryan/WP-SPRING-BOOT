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
                var usernameElement = document.createElement('span');
                var usernameText = document.createTextNode(response.username);
                usernameElement.appendChild(usernameText);

                var friendButtonElement = document.createElement('button');
                friendButtonElement.setAttribute("id", "addFriend");
                friendButtonElement.setAttribute("class", "btn btn-success custom-width");
                friendButtonElement.innerHTML = 'contact';
                friendButtonElement.style['background-color'] = '#4CAF50';
                friendButtonElement.style['margin'] = '1px 0.5px';

                var action = "addFriend";
                friendButtonElement.onclick = function () {
                    var url = action === 'addFriend' ? '/addFriend' : 'unFriend';
                    $.ajax({
                        type: "POST",
                        url: url,
                        data: {
                            receiverUsername: $('#findUsers').val()
                        },
                        success: function (response) {
                            var foundUserArea = document.querySelector('#foundUserUl');
                            if (response.status != "SUCCESS") {
                                if (action === "addFriend") {
                                    friendButtonElement.style['background-color'] = '#00ff04';
                                } else {
                                    friendButtonElement.style['background-color'] = '#ff5652';
                                }
                            } else {
                                if (action === "addFriend") {
                                    action = "unFriend";
                                    friendButtonElement.style['background-color'] = '#ff5652';

                                } else {
                                    action = "addFriend";
                                    friendButtonElement.style['background-color'] = '#00ff04';
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
                foundUserArea.appendChild(usernameElement);
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