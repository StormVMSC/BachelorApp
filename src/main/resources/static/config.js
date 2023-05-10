
let inventoryAndHostList = [];



function getInventoryList(){
    $.get("/getInventory", function(data){
        let promises = [];
        console.log(data)
       for(let inventory of data){
           let promise = new Promise(function(resolve){
               $.ajax( {
                   type: "POST",
                   url: "/postInventoryHost",
                   data: JSON.stringify(inventory.id),
                   contentType: "application/json",
                   success: function (hostList) {
                       addInventoryList(inventory, hostList)
                       resolve();
                   }
               });
           });
           promises.push(promise);
       }
       Promise.all(promises).then(function(){
           formatInventoryList();
       });
    });
}

function addInventoryList(inventory, hostList){
    let object ={
        inventory: inventory,
        hostList: hostList
    }
    console.log(object)
    inventoryAndHostList.push(object);
    console.log(inventoryAndHostList)
}

 function formatInventoryList() {
    let ut = '';
    for(let inventoryList of inventoryAndHostList){
        if(inventoryList.inventory == null){
            continue
        }
        ut += ' <div class="accordion" id="accordion'+ inventoryList.inventory.id +'">\n' +
            '                        <div class="accordion-item">';
        ut += '<h2 class="accordion-header">\n' +
            '       <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#collapse'+ inventoryList.inventory.id +'" aria-expanded="true" aria-controls="collapseOne">\n' +
            inventoryList.inventory.name +
            '       </button>\n' +
            '   </h2>'+
            '<div id="collapse'+ inventoryList.inventory.id +'" class="accordion-collapse collapse" data-bs-parent="#accordion'+ inventoryList.inventory.id +'">\n' +
            '                                <div class="accordion-body table-responsive">\n' +
            '                                    <table class="table">';
        ut += '<thead>\n' +
            '    <tr>\n' +
            '        <th scope="col" class="col-md-4">#</th>\n' +
            '        <th scope="col" class="col-md-4">host</th>\n' +
            '        <th scope="col" class="col-md-4">\n' +
            '            <input class="form-check-input primary" type="checkbox" id="inventory'+ inventoryList.inventory.id +'">\n' +
            '            <label class="form-check-label" for="inventory'+ inventoryList.inventory.id +'">\n' +
            '                all\n' +
            '            </label>\n' +
            '        </th>\n' +
            '    </tr>\n' +
            '    </thead>' +
            '<tbody>';
        for(let host of inventoryList.hostList){
            ut += '<tr>\n' +
                '      <th scope="row">'+ host.id +'</th>\n' +
                '      <td>'+ host.name +'</td>\n' +
                '      <td>\n' +
                '          <input class="form-check-input secondary" type="checkbox" id="'+ host.name +'">\n' +
                '          <label class="form-check-label" for="'+ host.name +'">\n' +
                '          </label>\n' +
                '      </td>\n' +
                '   </tr>'
        }
        ut += '                                         </tbody>\n' +
            '                                    </table>\n' +
            '                                </div>\n' +
            '                            </div>\n' +
            '                        </div>\n' +
            '                    </div>';
    }
    $("#inventory").html(ut);

     checkButton();
}

function checkButton(){
    $(".form-check-input").not(".primary").on("change", function() {
        var $primaryCheckbox = $(this).closest(".table").find(".primary");
        var checkedSecondaryCheckboxes = $(this).closest(".table").find(".form-check-input").not(".primary:checked").length;
        if (checkedSecondaryCheckboxes > 0) {
            $primaryCheckbox.prop("indeterminate", true);
        } else {
            $primaryCheckbox.prop("indeterminate", false);
            $primaryCheckbox.prop("checked", true);
        }
    });


    $('.primary').click(function(){
        var table = $(this).closest('table');
        var secondary = table.find('.secondary');
        secondary.prop('checked', $(this).prop('checked'));
    })
}

$(function(){
    $("#datepicker").datepicker();
});

function postKonfigurasjon(){

    const checked = [];
    $('.secondary[type=checkbox].secondary:checked').each(function(){
        checked.push($(this).attr('id'));
    });

    const playbook = $("#inpPlaybook").val();
    const Patch = {
        jobId : playbook,
        hosts : checked
    }

    $.post("/Configure", Patch, function(){
        console.log("patched");
    })
}