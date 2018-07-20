var stompClient = null;
var principalUsername = null;
var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];
var action = "/addFriend";
var friendButtonElement;

$(document).ready(function () {
    connect();
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
    })
});

function sendToUser(errorDiv, url) {
    $.ajax({
        type: "POST",
        url: url,
        success: function (response) {
            if (response.status != "SUCCESS") {
                $('#' + errorDiv).html(response.message);
                $('#' + errorDiv).show('slow');
            } else {
                $('#' + errorDiv).html("");
            }
        },
        error: function (responseText) {
            $('#' + errorDiv).text(responseText);
        }
    });
}

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

/**
 * Display the notification message.
 */
function notify(message) {
    console.log("------------------------ notify !!!!!!");
    var noteElements = document.querySelector("#notifications-area");
    var spanElement = document.createElement('span');
    var spanTxt = document.createTextNode("Notifications");
    spanElement.appendChild(spanTxt);
    noteElements.appendChild(spanElement);
    buildTable(message);
    return;
}

function buildTable(data) {
    var table = document.createElement("table");
    table.className = "table table-hover";
    var thead = document.createElement("thead");
    var tbody = document.createElement("tbody");
    var headRow = document.createElement("tr");
    // ["Name","Height","Country"].forEach(function(el) {
    //     var th=document.createElement("th");
    //     th.appendChild(document.createTextNode(el));
    //     headRow.appendChild(th);
    // });
    thead.appendChild(headRow);
    table.appendChild(thead);
    var tr = document.createElement("tr");
    tr.setAttribute("class", "alert alert-info");
    for (var o in data) {
        var td = document.createElement("td");
        td.appendChild(document.createTextNode(data[o]));
        tr.appendChild(td);
    }
    tbody.appendChild(tr);
    table.appendChild(tbody);
    return table;
}

function initUsernameFind(errorUl) {
    var userPrincipalText = $('#userPrincipal').text();
    principalUsername = userPrincipalText.substr(userPrincipalText.indexOf(' ') + 1, userPrincipalText.length);
    var userToFind = $('#findUsers').val();
    var noUserFoundArea = document.querySelector('#noUserFoundUl');
    if (principalUsername.trim() === userToFind) {
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
            var usernameElementSpan = document.createElement('span');
            var foundUserArea = document.querySelector('#foundUserUl');
            var usernameText = document.createTextNode(response.username);
            usernameElementSpan.appendChild(usernameText);
            addSpace(usernameElementSpan, 1);
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

                // button onclick event
                friendButtonElement.onclick = function () {
                    var url = action === '/addFriend' ? '/sendFriendRequest' : '/unFriend';
                    var isFriendRequest = url === '/sendFriendRequest';
                    sendStompMessage((isFriendRequest ? "friend request" : "unFriend request"), (isFriendRequest ? 'FR_REQUEST' : "UN_FR_REQUEST"), "/app/message");
                    $.ajax({
                        type: "POST",
                        url: url,
                        data: {
                            receiverUsername: $('#findUsers').val()
                        },
                        success: function (response) {
                            var modalHeader = document.querySelector('#frModalHeader');
                            var modalText = document.querySelector('#frModalText');
                            resolveServerData(response, friendButtonElement, modalHeader, modalText);
                        },
                        error: function (responseText) {
                            $('#' + 'foundUserUl').text(responseText);
                        }
                    });
                    return false;
                };
            }

            usernameElementSpan.appendChild(friendButtonElement);
            var messageElement = makeChatView(response.username[0]);
            addSpace(messageElement, 3);
            messageElement.appendChild(usernameElementSpan);
            foundUserArea.appendChild(messageElement);
            addSpace(foundUserArea, 4);
            addLineBreak(foundUserArea, 1);
        },
        error: function (responseText) {
            $('#' + errorUl).text(responseText);
        }
    });
}

function resolveServerData(response, friendButtonElement, modalHeader, modalText) {
    if (response.status !== "SUCCESS") {
        var noUserFoundArea = document.querySelector('#noUserFoundUl');
        if (action === "/addFriend") {
            setElementStyle(noUserFoundArea, noUserFoundArea, 'color', '#ff5652', response.message);
            noUserFoundArea.classList.remove('hidden');
            setElementStyle(friendButtonElement, friendButtonElement, 'background-color', '#337ab7', 'Friend');
        } else {
            setElementStyle(friendButtonElement, friendButtonElement, 'background-color', '#ff5652', 'UnFriend');
        }
    } else {
        if (action === "/addFriend") {
            action = "/unFriend";
            adjustModal(modalHeader, modalText, false);
            processModal();
            setElementStyle(friendButtonElement, friendButtonElement, 'background-color', '#ff5652', 'UnFriend');
        } else {
            adjustModal(modalHeader, modalText, true);
            processModal();
            action = "/addFriend";
            setElementStyle(friendButtonElement, friendButtonElement, 'background-color', '#337ab7', 'Friend');
        }
    }
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

function makeChatView(avatarLetter) {
    var messageElement = document.createElement('li');
    messageElement.classList.add('chat-message');
    var avatarElement = document.createElement('i');
    var avatarText = document.createTextNode(avatarLetter);
    avatarElement.appendChild(avatarText);
    avatarElement.style['background-color'] = getAvatarColor(avatarLetter);

    messageElement.appendChild(avatarElement);

    return messageElement;
}

/**
 * Open the web socket connection and subscribe the "/notify" channel.
 */
function connect() {
    console.log("CONNECTED -------------------------");
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function () {
        stompClient.subscribe("/user/queue/reply", function (notification) {
            console.log("------------------------ subscribed !!!!!!");
            notify(JSON.parse(notification.body).content);
        });
    });
}

function sendStompMessage(message, request, url) {
    var chatMessage = {
        sender: principalUsername,
        content: message,
        type: request
    };
    stompClient.send(url, {}, JSON.stringify(chatMessage));
}

function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}