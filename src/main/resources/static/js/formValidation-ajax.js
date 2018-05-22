$(document).ready(function () {
    $('#username').blur(function () {
        initUsernameCheck('ajaxGetUsernameResponse', 'Invalid Username');
    });

    $('#email').blur(function () {
        initEmailCheck('ajaxGetEmailResponse', 'Invalid Email');
    });

    $('#password').blur(function () {
        initPasswordCheck('ajaxGetPasswordResponse', 'Invalid Password');
    });

    $('#passwordConfirm').blur(function () {
        initPasswordConfirmCheck('ajaxGetPasswordConfirmResponse', 'Password and PasswordConfirm do not match');
    });
});

function initEmailCheck(errorDiv, errorMessage) {
    $.ajax({
        type: "POST",
        url: 'checkEmail',
        data: {
            email: $('#email').val()
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
            $('#' + errorDiv).text(responseText);
        }
    });
}

function initUsernameCheck(errorDiv, errorMessage) {
    $.ajax({
        type: "POST",
        url: 'checkUsername',
        data: {
            username: $('#username').val()
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
            $('#' + errorDiv).text(responseText);
        }
    });
}

function initPasswordCheck(errorDiv, errorMessage) {
    $.ajax({
        type: "POST",
        url: 'checkPassword',
        data: {
            password: $('#password').val()
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
            $('#' + errorDiv).text(responseText);
        }
    });
}

function initPasswordConfirmCheck(errorDiv, errorMessage) {
    $.ajax({
        type: "POST",
        url: 'checkPasswordConfirm',
        data: {
            password: $('#password').val(),
            passwordConfirm: $('#passwordConfirm').val()
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
            $('#' + errorDiv).text(responseText);
        }
    });
}