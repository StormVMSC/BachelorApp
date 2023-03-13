function validerBrukernavn(brukernavn){
    const regexp = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/;
    const ok = regexp.test(brukernavn);
    if(!ok){
        $("#feilBrukernavn").html("Passordet må bestå av minimum 6 tegn");
    }
    else{
        $("#feilBrukernavn").html("");
        return true;
    }
}

function validerPassord(Passord){
    const regexp = /^[a-zA-ZæøåÆØÅ\.\ \-]{2,20}$/;
    const ok = regexp.test(passord);
    if(!ok){
        $("#feilPassord").html("Passordet må bestå av minimum 6 tegn");
    }
    else{
        $("#feilPassord").html("");
        return true;
    }
}