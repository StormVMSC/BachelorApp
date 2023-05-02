function loadSchedule() {
    getScheduleList();
    getPlaybookList();
    getHostList();
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

function getPlaybookList() {
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


function getScheduleList() {
    $.ajax({
        url: "/GetSchedules",
        method: "GET",
        success: function(data) {
            // Handle successful response
            console.log(data); // This will print the response to the console
            data.sort((a, b) => a.id - b.id);
            formatScheduleList(data);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            // Handle error response
            console.error(errorThrown); // This will print the error to the console
        }
    });
}

function formatPlaybookList(data){
    let ut = "<label for=\"inpPlaybook\">Ønsket Playbook</label>" +
        "<select id=\"inpPlaybook\" class=\"form-control text-center\">" +
        "<option selected>Velg playbook</option>";

    for(let playbook of data){
        ut += "<option value='"+ playbook.id +"'> " + playbook.navn + "</option>";
    }
    ut += "</select>";
    $("#playbook").html(ut);
}

function formatHostList(data){
    let ut = "";
    let i = 0;
    for(let host of data){
        i++;
        ut += "<div class=\"form-check form-check-inline\">" +
            "<input class='form-check-input' type='checkbox' id='" + "inlineCheckbox" + i + "' value='"+ host.name +"'>" +
            "<label class='form-check-label' for='"+ "inlineCheckbox" + i +"'>"+ host.name +"</label>" +
            "</div>";
    }
    $("#host").html(ut);
}


function formatScheduleList(data){
    let ut = "<table class='table table-striped'>" +
        "<tr>" +
        '<th scope="col">#</th>'+
        '<th scope="col">Navn</th>'+
        '<th scope="col">Start dato</th>'+
        '<th scope="col">Tidspunkt</th>'+
        '<th scope="col">Frekvens</th>' +
        '<th scope="col">Intervall</th>'+
        '<th scope="col">Host(s)</th>' +
        '<th scope="col">PlaybookId</th>' +
        "</tr>";
    for (let i = 0; i < 10 && i < data.length; i++) {
        let schedules = data[i];
        const extractedRrule = extractRruleData(schedules.playbookId);
        ut += "<tr>" +
            "<th scope='row'>"+ schedules.id +"</th>" +
            "<td>"+ schedules.rrule +"</td>" +
            "<td>"+ extractedRrule.dato +"</td>" +
            "<td>"+ extractedRrule.tid +"</td>" +
            "<td>"+ extractedRrule.frekvens +"</td>" +
            "<td>"+ extractedRrule.intervall +"</td>" +
            "<td>"+ schedules.hosts +"</td>" +
            "<td>"+ schedules.navn +"</td>" +
            "<td><button onclick='deleteSchedule("+ schedules.id +")' class='delete-button'>Delete</button></td>" +
            "</tr>";
    }
    ut += "</table>";
    $("#schedules").html(ut);
}

function extractRruleData(rrule) {
    const dato = rrule.match(/:(\d{4})(\d{2})(\d{2})T/);
    const datoString = `${dato[3]}.${dato[2]}.${dato[1]}`;

    const tid = rrule.match(/T(\d{2})(\d{2})(\d{2})/);
    const tidString = `${tid[1]}:${tid[2]}:${tid[3]}`;

    const frekvens = rrule.match(/FREQ=([^;]+)/);
    const frekvensString = frekvens[1];

    const intervall = rrule.match(/INTERVAL=(\d+)/);
    const intervallString = intervall[1];

    const extractedData = {
        dato: datoString,
        tid: tidString,
        frekvens: frekvensString,
        intervall: intervallString
    };

    return extractedData;
}


function getCheckboxValues() {
    let checkedValues = [];
    let checkBoxes = document.querySelectorAll('input[type="checkbox"]:checked');

    for(let i = 0; i < checkBoxes.length; i++) {
        checkedValues.push(checkBoxes[i].value);
    }
    return checkedValues;
}


function sendSchedule() {

    let frekvens = document.getElementById("inpFrekven").value;
    let intervall = document.getElementById("inpIntervall").value;
    let dato = document.getElementById("inpDato").value;
    let tid = document.getElementById("inpTid").value;

    let schedule = {
        playbookId: document.getElementById("inpPlaybook").value,
        navn: document.getElementById("inpNavn").value,
        hosts: getCheckboxValues(),
        rrule: "DTSTART;TZID=Europe/Oslo:" + dato + "T" + tid + " RRULE:FREQ=" + frekvens + ";INTERVAL=" + intervall
    };



    $.ajax( {
        type: "POST",
        url: "/schedulePatch",
        data: JSON.stringify(schedule),
        contentType: "application/json",
        success: function () {
            console.log("schedule sendt");
        },
        error: function (xhr, status, error) {
            console.log("Feil" + error);
        }
    });
}

function deleteSchedule(id) {
    $.ajax( {
        type: "POST",
        url: "/deleteSchedule",
        data: JSON.stringify(id),
        contentType: "application/json",
        success: function () {
            console.log("schedule slettet");
        },
        error: function (xhr, status, error) {
            console.log("Feil" + error);
        }
    });
}