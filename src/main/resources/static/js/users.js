$(document).ready(function () {
    $('#searchUser').click(function () {
        initUsernamefind('foundUserUl', 'noUserFoundUl', 'No user found with that Username');
    });
});

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

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
                var ulText = document.getElementById("foundUserUl").textContent;
                if (ulText.includes(response.username)) {
                    return false;
                }
                var usernameElement = document.createElement('span');
                var usernameText = document.createTextNode(response.username);
                usernameElement.appendChild(usernameText);
                var space = document.createTextNode('\u00A0');
                usernameElement.appendChild(space);

                var friendButtonElement = document.createElement('button');
                friendButtonElement.setAttribute("id", "addFriend");
                friendButtonElement.setAttribute("class", "btn btn-primary btn-sm");
                friendButtonElement.innerHTML = 'Contact';

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
                                    friendButtonElement.style['background-color'] = '#337ab7';
                                } else {
                                    friendButtonElement.style['background-color'] = '#ff5652';
                                    friendButtonElement.innerHTML = 'unFriend';
                                }
                            } else {
                                if (action === "addFriend") {
                                    action = "unFriend";
                                    $('#exampleModalCenter').modal('show');
                                    setTimeout(function () {
                                        $('#exampleModalCenter').modal('hide');
                                    }, 5000);

                                    friendButtonElement.style['background-color'] = '#ff5652';
                                    friendButtonElement.innerHTML = 'unFriend';

                                } else {
                                    action = "addFriend";
                                    friendButtonElement.style['background-color'] = '#337ab7';
                                    friendButtonElement.innerHTML = 'Contact';
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
                var linebreak = document.createElement("br");
                foundUserArea.appendChild(linebreak);
            }
        },
        error: function (responseText) {
            $('#' + errorUl).text(responseText);
        }
    });
}