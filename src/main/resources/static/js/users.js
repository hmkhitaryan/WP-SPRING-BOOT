$(document).ready(function () {
    $('#findUsers').blur(function () {
        initUsernamefind('foundUserUl', 'noUserFoundUl', 'No user found with that Username');
        initAddFriend();
    });


});

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];
function initAddFriend() {
    $.ajax({
        type: "POST",
        url: '/addFriend',
        data: {
            receiverUsername: $('#findUsers').val()
        },
        success: function (response) {
            if (response.status != "SUCCESS") {
                $('#' + errorDiv).html(errorMessage);
                $('#' + errorDiv).show('slow');
            } else {
                $('#' + errorDiv).html("");
            }
        },
        error: function (responseText) {
            $('#' + errorUl).text(responseText);
        }
    });
}
function initUsernamefind(successUl, errorUl, errorMessage) {
    $.ajax({
        type: "POST",
        url: '/findUser',
        data: {
            username: $('#findUsers').val()
        },
        success: function (response) {
            var username = response.username;
            var noUserFoundArea = document.querySelector('#noUserFoundUl');
            if (username === undefined) {
                var errorMessageText = document.createTextNode(errorMessage);
                noUserFoundArea.style['background-color'] = '#ff5652';
                noUserFoundArea.appendChild(errorMessageText);
                // $('#' + errorUl).show('slow');
            } else {
                noUserFoundArea.classList.add('hidden');
                var foundUserArea = document.querySelector('#foundUserUl');
                // $('#' + errorUl).html("");
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
                friendButtonElement.setAttribute("class", "btn btn-lg btn-primary btn-block");
                friendButtonElement.setAttribute("value", "Contact");
                friendButtonElement.style['background-color'] = '#4CAF50';
                friendButtonElement.style['margin'] = '4px 2px';
                usernameElement.appendChild(friendButtonElement);
                usernameTextElement.appendChild(usernameElement);
                foundUserArea.appendChild(usernameTextElement);
                // $('#' + successUl).html(messageElement);
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