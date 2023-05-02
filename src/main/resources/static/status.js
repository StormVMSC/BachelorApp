
function getHostList(){
    $.ajax({
        url: "/GetHostList",
        method: "GET",
        success: function(data) {
            // Handle successful response
            console.log(data); // This will print the response to the console
            data.sort((a, b) => a.id - b.id);
            formatStatusList(data);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            // Handle error response
            console.error(errorThrown); // This will print the error to the console
        }
    });
}

function formatStatusList(data){
    let ut = "<table class='table table-striped'>" +
        "<tr>" +
        '<th scope="col">Cluster</th>'+
        '<th scope="col"> Sist jobb status </th>' +
        '<th scope="col"> Sist Konfigurasjon </th>'+

        "</tr>";
    for(let host of data){
        ut += "<tr>" +
            "<td>"+ host.name +"</td>" +
            "<td>"+ host.status +"</td>" +
            "<td>"+ host.date +"</td>" +
            "</tr>";
    }
    ut += "</table>";
    $("#hostStatus").html(ut);
}
