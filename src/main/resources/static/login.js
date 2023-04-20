function logInn() {




      var brukernavn = $("#brukernavn").val();
      var passord = $("#passord").val();

     $.post("/login",{username: brukernavn, password: passord}, function (OK) {
         if (OK) {
             window.location.href = 'index.html';
         } else {
             $("#feil").html("Feil brukernavn eller passord");
         }
     }).fail(function () {
             $("#feil").html("Feil på server, prøv igjen senere");
     });
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

