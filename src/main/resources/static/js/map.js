$(document).ready(function () {
    disableButton('uploadDoc');
});

function disableButton(buttonId) {
    $('#' + buttonId).prop("disabled", true);
    $('input[type="file"]').change(function (e) {
        $('#' + buttonId).prop('disabled', false);
    });
}
