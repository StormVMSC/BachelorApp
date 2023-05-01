
function getHostList(){
    $.ajax({
        url: "/GetHostList",
        method: "GET",
        success: function(data) {
            // Handle successful response
            console.log(data); // This will print the response to the console
            data.sort((a, b) => a.id - b.id);
            formatHostList(data);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            // Handle error response
            console.error(errorThrown); // This will print the error to the console
        }
    });
}

function formatHostList(data){
    let ut = "";
    for(let host of data){
        ut +="<div class='col-sm-3'>\n" +
            "    <div class='card_area'>\n" +
            "        <input class='checkbox' type='checkbox' name='host' value='"+ host.name +"'/>\n" +
            "        <div class='single_card'>\n" +
            "            <div class='card_body cluster1'>\n" +
            "                <div class='text-center'><img src='img/databaser.png' class='box-icon'></div>\n" +
            "                <div class='card_title'>\n" +
            "                    <h3>"+ host.name +"</h3>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</div>"
            ;
    }
    $("#host").html(ut);
}

function getPlaybookList(){
    $.ajax({
        url: "/GetPlaybooks",
        method: "GET",
        success: function(data) {
            // Handle successful response
            console.log(data); // This will print the response to the console
            data.sort((a, b) => a.id - b.id);
            formatPlaybookList(data);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            // Handle error response
            console.error(errorThrown); // This will print the error to the console
        }
    });
}

function formatPlaybookList(data){
    let ut = "";
    for( let playbook of data){
        ut +="<div class='col-sm-3'>\n" +
            "    <div class='card_area'>\n" +
            "        <input class='checkbox' type='radio' id='"+ playbook.navn +"' name='playbookRadio' value='"+ playbook.id +"'/>\n" +
            "        <div class='single_card'>\n" +
            "            <div class='card_body cluster1'>\n" +
            "                <div class='text-center'><img src='img/databaser.png' class='box-icon'></div>\n" +
            "                <div class='card_title'>\n" +
            "                    <h3>"+ playbook.navn +"</h3>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</div>"
            ;
    }
    $("#playbook").html(ut);
}

function postKonfigurasjon(){
    const  check1 = $('input[name=host]:checked').map(function(){
        return $(this).val();
    }).get();
    const check2 = $('input[name=playbookRadio]:checked').val()
    console.log(check1)
    console.log(check2)


    const Patch = {
        jobId : check2,
        hosts : check1
    }

    $.post("/Configure", Patch, function(){
        console.log("patched");
    })
}
