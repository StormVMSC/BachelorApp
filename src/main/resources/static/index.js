function loadData(){
    getHistorikkList();
    getHostList();
}

function getHistorikkList(){
    $.ajax({
        url: "/GetHistorikk",
        method: "GET",
        success: function(data) {
            // Handle successful response
            console.log(data); // This will print the response to the console
            formatHistorikkList(data);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            // Handle error response
            console.error(errorThrown); // This will print the error to the console
        }
    });
}

function formatHistorikkList(data){
    let ut = "<table class='table table-striped'>" +
        "<tr>" +
        '<th scope="col">#</th>'+
        '<th scope="col">Jobs</th>'+
        '<th scope="col">Status</th>'+
        '<th scope="col">Start dato</th>'+
        '<th scope="col">Slutt dato</th>'
        "</tr>";
    for (let i = 0; i < 10 && i < data.length; i++) {
        let historikk = data[i];
        ut += "<tr>" +
            "<th scope='row'>"+ historikk.id +"</th>" +
            "<td>"+ historikk.navn +"</td>" +
            "<td>"+ historikk.status +"</td>" +
            "<td>"+ historikk.startTime +"</td>" +
            "<td>"+ historikk.finnishTime +"</td>" +
            "</tr>";
    }
    ut += "</table>";
    $("#historikk").html(ut);
}

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
