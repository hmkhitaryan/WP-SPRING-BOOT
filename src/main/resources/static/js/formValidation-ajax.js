$(document).ready(function () {
    $('#username').blur(function () {
        validateField('ajaxGetUsernameResponse', {username: $('#username').val()}, 'checkUsername');
    });

    $('#email').blur(function () {
        validateField('ajaxGetEmailResponse', {email: $('#email').val()}, 'checkEmail');
    });

    $('#password').blur(function () {
        validateField('ajaxGetPasswordResponse', {password: $('#password').val()}, 'checkPassword');
    });

    $('#passwordConfirm').blur(function () {
        validateField('ajaxGetPasswordConfirmResponse', {password: $('#password').val(), passwordConfirm: $('#passwordConfirm').val()}, 'checkPasswordConfirm');
    });
});

function validateField(errorDiv, data, url) {
    $.ajax({
        type: "POST",
        url: url,
        data: data,
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