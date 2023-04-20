$(() => {
    check();
})

function check(){
    $.get("isLoggedIn", function(OK){
        if(OK){

        }else{
            window.location.href = "login.html";
        }
    })
}

function logout(){
    $.post("/logout", function(OK){
        if(OK){
            window.location.href = "login.html";
        }else{
            console.log("noe galt med serveren");
        }
    })
}