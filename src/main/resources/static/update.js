function getData() {
    const promise1 = $.ajax({ url: "/GetPlaybooks", method: "GET" });
    const promise2 = $.ajax({ url: "/GetHostList", method: "GET" });

    Promise.all([promise1, promise2]).then(([playbook, host]) => {
        formaterData(playbook, host);
    }).catch((error) => {
        console.log(error);
    });
}


function formaterData (playbook, host){
    let hostList = [];
    let ut = "<table class='table table-striped'>" +
        "<tr>" +
        '<th scope="col">#</th>'+
        '<th scope="col">Host</th>'+
        '<th scope="col">Oppdater</th>'+
        "</tr>";
    for(let hosts of host){
        ut += "<tr>" +
            "<th scope='row'>"+ hosts.id +"</th>" +
            "<td>"+ hosts.name +"</td>" +
            "<td><button onclick='sendPatch(\""+ playbook[0].id + "\", \"" + hosts.name +"\")' class='btn-success costumbtn'>Oppdater</button></td>" +
        "</tr>";
        hostList.push(hosts.name);
    }
    console.log(hostList);
    ut += "</table>" +
    "<button onclick='sendPatch(\""+ playbook[0].id + "\", \"" + hostList +"\")' class='btn-success costumbtn'>Oppdater alle</button>";
    $("#host").html(ut);
}

function sendPatch(playId, hostName) {
    let patch = {
        jobId: playId,
        hosts: hostName
    };

    $.post("/Configure", patch, function(){
        console.log("patched");
    })
}
