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