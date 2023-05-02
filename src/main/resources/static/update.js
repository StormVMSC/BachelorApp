


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
    let ut = "<table class='table table-striped'>" +
        "<tr>" +
        '<th scope="col">#</th>'+
        '<th scope="col">Host</th>'+
        "</tr>";
    for(let host of data){
        ut += "<tr>" +
            "<th scope='row'>"+ host.id +"</th>" +
            "<td>"+ host.name +"</td>" +
        "</tr>";
    }
    ut += "</table>";
    $("#host").html(ut);
}

function sendPatch() {
    let patch = {
        jobId: 22,
        username: "BrukerA",
        password: "PassordA",
        hosts: ["Host A (1)", "Host A (2)"]
    };

    $.ajax({
        type: "POST",
        url: "/PatchFlere",
        data: JSON.stringify(patch),
        contentType: "application/json",
        success: function () {
            console.log("Sendt");
        },
        error: function (xhr, status, error) {
            console.log("Feil: " + error);
        }
    });
}
