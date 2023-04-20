function logInn() {


    const brukernavnOK = validerBrukernavn($("#brukernavn").val());
    const passordOK = validerPassord($("#passord").val);
    if (brukernavnOK && passordOK) {
        const bruker = {
            brukernavn: $("#brukernavn").val(),
            passord: $("#passord").val()
        }

        $.post("/login", bruker, function (OK) {
            if (OK) {
                window.location.href = 'index.html';
            } else {
                $("#feil").html("Feil brukernavn eller passord");
            }
        })
            .fail(function () {
                $("#feil").html("Feil på server, prøv igjen senere");
            });
    }
}

function registrer(){

    const brukernavnOK = validerBrukernavn($("#brukernavn").val());
    const passordOK = validerPassord($("#passord").val);
    if (brukernavnOK && passordOK) {

    }
    var username = $("#brukernavn").val();
    var password = $("#passord").val();

    $.post("/registrer", {username: username, password: password}, function (OK) {
        if (OK) {
            window.location.href = 'login.html';
        } else {
            $("#feil").html("Feil på server, prøv igjen senere");
        }
    }).fail(function () {
            $("#feil").html("Feil på server, prøv igjen senere");
    });

}

