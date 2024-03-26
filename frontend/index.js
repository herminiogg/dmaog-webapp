var editor = YASHEML(document.querySelector("#shexmlEditor"), {
    lineNumbers: true,
    lineWrapping: true,
    theme: "default",
    viewportMargin: Infinity
});

editor.setValue("");

var rdfEditor = CodeMirror(document.querySelector("#rdfEditor"), {
    lineNumbers: true,
    lineWrapping: true,
    theme: "default",
    viewportMargin: Infinity
});

$('#inputType').on('change', function() {
    var selection = this.value;
    $("#shexmlEditor").hide();
    $("#rdfEditor").hide();
    if(selection === "RDF" || selection === "RML") {
       $("#rdfEditor").toggle();
    }
    if(selection === "ShExML") {
        $("#shexmlEditor").toggle();
    }
});

$("#generateButton").on('click', function() {
    var shexmlDocument = editor.getValue();
    var rdfContent = rdfEditor.getValue();
    var content = {
        shexml: shexmlDocument,
        rdf: rdfContent,  
        conversionType: $("#inputType").val(),
    };
    $.ajax("https://dmaog.herminiogarcia.com/dmaogAPI/generateCodeWithDMAOG", {
        "data": JSON.stringify(content),
        "type": "POST",
        "processData": false,
        "contentType": "application/json",
        "xhrFields": {
            "responseType": 'blob' 
        },
        "success": function(data) {
            const url = window.URL.createObjectURL(data);
            const a = document.createElement('a');
            a.style.display = 'none';
            a.href = url;
            a.download = 'results.zip';
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
            nonLoadingButton();
            goToWhatsNext();
        },
        "error": function(jqXHR, textStatus, errorThrown) {
            createAlert(jqXHR.responseText);
            nonLoadingButton();
        }
    });
    loadingButton();
});

function loadingButton() {
    $("#generateButton").toggle();
    $("#loadingButton").toggle();
}

function nonLoadingButton() {
    $("#generateButton").toggle();
    $("#loadingButton").toggle();
}

function createAlert(errorMessage) {
    var alertHTML = '<div class="alert alert-danger alert-dismissible fade show" role="alert">' +
        errorMessage + 
        '<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close">'
        '</button>' +
        '</div>';
    console.log(errorMessage);
    var alertElement = jQuery(alertHTML);
    $(".container").prepend(alertElement);
}

function goToWhatsNext() {
    $("#nextDiv").show();

    const a = document.createElement('a');
    a.style.display = 'none';
    a.href = "#next";
    document.body.appendChild(a);
    a.addEventListener('click', smoothScroll);
    a.click();
}


function smoothScroll() {
    if (location.pathname.replace(/^\//,'') == this.pathname.replace(/^\//,'') && location.hostname == this.hostname) {
    var target = $(this.hash);
    target = target.length ? target : $('[name=' + this.hash.slice(1) +']');
    if (target.length) {
        $('html,body').animate({
        scrollTop: target.offset().top
        }, 1000);
        return false;
    }
    }
};