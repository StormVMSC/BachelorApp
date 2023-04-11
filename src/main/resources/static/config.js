
function getInventoryList(){
    $.ajax({
        url: "/GetInventory",
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
    let ut = "<select class='select' data-mdb-filter='true'>";
    for(let inventory of data){
        ut +=
            "<option value="+ inventory.id +">" + inventory.name + "</option>"
    }
    ut += "</select>";
    $("#inventory").html(ut);
}