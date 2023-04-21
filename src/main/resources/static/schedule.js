function getCheckboxValues() {
    let checkedValues = [];
    let checkBoxes = document.querySelectorAll('input[type="checkbox"]:checked');

    for(let i = 0; i < checkBoxes.length; i++) {
        checkedValues.push(checkBoxes[i].value);
    }
    return checkedValues;
}


function sendSchedule() {
    let schedule = {
        playbookId: "22",
        frekvens: document.getElementById("inpFrekven").value,
        intervall: document.getElementById("inpIntervall").value,
        navn: document.getElementById("inpNavn").value,
        hosts: getCheckboxValues(),
        dato: document.getElementById("inpDato").value,
        tid: document.getElementById("inpTid").value
    };

    console.log(schedule);

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