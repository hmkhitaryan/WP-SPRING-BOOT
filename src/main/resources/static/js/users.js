$(document).ready(function () {
    $('#searchUser').click(function () {
        initUsernameFind('noUserFoundUl');
    });
    $('#confirmFriendButton').click(function () {
        var noteId = $('#confirmFriendButton').attr("data-id");
        initProcessingFriendRequest(noteId, 'confirmError', 'confirmFriend');
    });
    $('#ignoreFriendButton').click(function () {
        var noteId = $('#ignoreFriendButton').attr("data-id");
        initProcessingFriendRequest(noteId, 'confirmError', 'ignoreFriend');
    });
});

function initProcessingFriendRequest(noteId, errorDiv, url) {
    $.ajax({
        type: "POST",
        url: url,
        data: {
            noteId: noteId
        },
        success: function (response) {
            if (response.status != "SUCCESS") {
                $('#' + errorDiv).html(response.message);
                $('#' + errorDiv).show('slow');
            } else {
                $('#' + errorDiv).html("");
                $('#noteRow' + noteId).addClass('hidden');
            }
        },
        error: function (responseText) {
            $('#' + errorDiv).text(responseText);
        }
    });
}

function setElementStyle(htmlElement1, htmlElement2, attribute, attributeValue, textValue) {
    htmlElement1.style[attribute] = attributeValue;
    htmlElement1.style['text-align'] = 'center';
    htmlElement2.innerHTML = textValue;
}

function adjustModal(frModalHeader, frModalText, unFriend) {
    if (unFriend === true) {
        setElementStyle(frModalHeader, frModalText, 'background-color', '#ff5652', 'You just sent unFriend request to that user');
    } else {
        setElementStyle(frModalHeader, frModalText, 'background-color', '#337ab7', 'You just sent friend request to that user, and waiting for response');
    }

}

function processModal() {
    $('#exampleModalCenter').modal('show');
    setTimeout(function () {
        $('#exampleModalCenter').modal('hide');
    }, 5000);
}

function getFriendButton(isFriend) {
    var friendButtonElement = document.createElement('button');
    friendButtonElement.setAttribute("id", "addFriend");
    friendButtonElement.setAttribute("class", "btn btn-primary btn-sm");
    friendButtonElement.innerHTML = isFriend ? 'UnFriend' : 'Contact';

    return friendButtonElement;
}

function initUsernameFind(errorUl) {
    var userPrincipalText = $('#userPrincipal').text();
    var userPrincipal = userPrincipalText.substr(userPrincipalText.indexOf(' ') + 1, userPrincipalText.length);
    var userToFind = $('#findUsers').val();
    var noUserFoundArea = document.querySelector('#noUserFoundUl');
    if (userPrincipal.trim() === userToFind) {
        setElementStyle(noUserFoundArea, noUserFoundArea, 'color', '#ff5652', 'Can not find yourself !!!!');
        return false;
    }

    if (!userToFind) {
        setElementStyle(noUserFoundArea, noUserFoundArea, 'color', 'red', 'Please enter a username');
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
            var friendButtonElement;
            var usernameElement = document.createElement('span');
            var foundUserArea = document.querySelector('#foundUserUl');
            var usernameText = document.createTextNode(response.username);
            usernameElement.appendChild(usernameText);
            addSpace(usernameElement, 1);
            var action = "addFriend";
            if (username === undefined) {
                $('#' + errorUl).text('');
                setElementStyle(noUserFoundArea, noUserFoundArea, 'color', 'red', 'No user found with that username');
            } else {
                noUserFoundArea.classList.add('hidden');
                var ulText = document.getElementById("foundUserUl").textContent;
                if (ulText.includes(response.username)) {
                    return false;
                }
                var isFriend = response.friend;
                friendButtonElement = getFriendButton(!!isFriend);
                setElementStyle(friendButtonElement, friendButtonElement, 'background-color', (isFriend ? '#ff5652' : '#337ab7'), (isFriend ? 'UnFriend' : 'Friend'));
                friendButtonElement.onclick = function () {
                    var url = action === 'addFriend' ? '/sendFriendRequest' : 'unFriend';
                    $.ajax({
                        type: "POST",
                        url: url,
                        data: {
                            receiverUsername: $('#findUsers').val()
                        },
                        success: function (response) {
                            var foundUserArea = document.querySelector('#foundUserUl');
                            var modalHeader = document.querySelector('#frModalHeader');
                            var modalText = document.querySelector('#frModalText');
                            if (response.status !== "SUCCESS") {
                                var noUserFoundArea = document.querySelector('#noUserFoundUl');
                                if (action === "addFriend") {
                                    setElementStyle(noUserFoundArea, noUserFoundArea, 'color', 'red', response.message);
                                    noUserFoundArea.classList.remove('hidden');
                                    setElementStyle(friendButtonElement, friendButtonElement, 'background-color', '#337ab7', 'Friend');
                                } else {
                                    setElementStyle(friendButtonElement, friendButtonElement, 'background-color', '#ff5652', 'UnFriend');
                                }
                            } else {
                                if (action === "addFriend") {
                                    action = "unFriend";
                                    adjustModal(modalHeader, modalText, false);
                                    processModal();
                                    setElementStyle(friendButtonElement, friendButtonElement, 'background-color', '#ff5652', 'UnFriend');
                                } else {
                                    adjustModal(modalHeader, modalText, true);
                                    processModal();
                                    action = "addFriend";
                                    setElementStyle(friendButtonElement, friendButtonElement, 'background-color', '#337ab7', 'Friend');
                                }
                            }
                        },
                        error: function (responseText) {
                            $('#' + 'foundUserUl').text(responseText);
                        }
                    });
                    return false;
                };
            }

            usernameElement.appendChild(friendButtonElement);
            var messageElement = makeChatView(response.username[0], '#337ab7');
            foundUserArea.appendChild(messageElement);
            addSpace(foundUserArea, 4);
            foundUserArea.appendChild(usernameElement);
            addLineBreak(foundUserArea, 1);
        },
        error: function (responseText) {
            $('#' + errorUl).text(responseText);
        }
    });
}

function addLineBreak(element, number) {
    for (var i = 0; i < number; i++) {
        var linebreak = document.createElement("br");
        element.appendChild(linebreak);
    }
}

function addSpace(element, number) {
    for (var i = 0; i < number; i++) {
        var space = document.createTextNode('\u00A0');
        element.appendChild(space);
    }
}

function makeChatView(avatarLetter, color) {
    var messageElement = document.createElement('li');
    messageElement.classList.add('chat-message');
    var avatarElement = document.createElement('i');
    var avatarText = document.createTextNode(avatarLetter);
    avatarElement.appendChild(avatarText);
    avatarElement.style['background-color'] = color;

    messageElement.appendChild(avatarElement);

    return messageElement;
}
