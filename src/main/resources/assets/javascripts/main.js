var m = {};

m.setupSidebar = function () {
    var hamburgerButton = $('#hamburger-button');
    hamburgerButton.sideNav();
    hamburgerButton.sideNav('show');
    debugger;
};

m.initializeHub = function () {
    $.post('initialize').done(function (response) {
        $('body').html(response);
    });
};

m.activityClicked = function (obj) {
    $('.btn-primary').each(function () {
        $(this).removeClass('btn-primary green-text');
        $(this).addClass('btn-default black-text');
    });
    $('.panel-primary').each(function () {
        $(this).removeClass('panel-primary');
        $(this).addClass('panel-default');
        $(this).addClass('hide');
    });
    $(obj).removeClass('btn-default black-text');
    $(obj).addClass('btn-primary green-text');

    var activityId = obj.id.substring('btn_activity_'.length, obj.id.length);
    $(obj).toggleClass('active');
    m.updateHeaderText(obj.name);
    m.callCommand(activityId, '{"command":"switch", "deviceId":"' + activityId + '"}');
};

m.callCommand = function (activityId, command) {
    $.ajax({
        method: 'POST',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        url: 'command',
        data: command
    }).done(function (msg, status, request) {
        if (typeof msg !== 'undefined') {
            if (msg.status != 'OK') {
                Materialize.toast('OOOPS State: ' + msg.status + ' Message: ' + msg.message, 3000, 'rounded');
            } else {
                if (msg.command == 'switch') {
                    if (activityId == -1) {
                        return;
                    }
                    var panel = $('#panel_activity_' + activityId);
                    panel.removeClass('hide');
                    panel.addClass('panel-primary');
                }
            }
        } else {
            Materialize.toast('Error ' + request.status + ': ' + request.statusText, 3000, 'rounded');
        }
    }).fail(function (request, status, error) {
        console.log(request, status, error);
        Materialize.toast('Failed to send command to harmony hub!', 3000, 'rounded');
    });
};

m.updateHeaderText = function (text) {
    $('#activityHeader').html(text);
};