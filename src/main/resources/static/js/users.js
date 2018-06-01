$(document).ready(function () {
    $('#findUsers').blur(function () {
        initUsernamefind('foundUserUl', 'noUserFoundUl', 'No user found with that Username');
    });


});

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];
var foundUserUl = document.querySelector('#foundUserUl');
var noUserFoundUl = document.querySelector('#noUserFoundUl');

function initUsernamefind(successUl, errorUl, errorMessage) {
    $.ajax({
        type: "POST",
        url: '/findUser',
        data: {
            username: $('#findUsers').val()
        },
        success: function (response) {
            var username = response.username;
            if (username === undefined) {
                $('#' + errorUl).html(errorMessage);
                $('#' + errorUl).show('slow');
            } else {
                $('#' + errorUl).html("");
                $('#' + successUl).html(response.username);
                var messageElement = document.createElement('li');
                var avatarElement = document.createElement('i');
                var avatarText = document.createTextNode(username[0]);
                avatarElement.appendChild(avatarText);
                avatarElement.style['background-color'] = getAvatarColor(username);
                messageElement.appendChild(avatarElement);
                foundUserUl.appendChild(messageElement);
            }
        },
        error: function (responseText) {
            alert("error");
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