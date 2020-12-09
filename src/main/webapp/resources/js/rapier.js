class Rapier {

}

Rapier.readHtmlFile = function (element, displayId) {
    const file = element.files[0];
    readFile(file, displayId)
}

Rapier.uploadHtml = function (element, displayId) {
    const file = element.files[0];
    const formData = new FormData();
    formData.append('file', file);
    readFile(file, displayId)
    $.ajax({
        url: "",
        data: formData,
        processData: false,
        contentType: false,
        type: 'POST',
        success: success
    });

    function success(data) {
        $("#process-html-name").val(data);
    }
}

Rapier.removeTag = function (selector) {
    const name = $("#process-html-name").val();
    $.post("remove-tag/" + name).then(success);

    function success(data) {
        $(selector).text(data);
        $("#regular-expression-store").prop("disabled", false);
    }
}

Rapier.loadStoreFile = function() {
    $.get("stored-files").then(data => {
       $("#data-extraction-stored-file").html(data);
    })
}

Rapier.store = function() {
    $.post("store/" + $("#process-html-name").val()).then( data=> {
        alert(data);
        $("#regular-expression-store").prop("disabled", true);
    })
}

Rapier.extract = function(selector) {
    const name = $("#process-html-name").val();
    $.post('extract/'+name, success);

    function success(data) {
        $(selector).text(data);
    }
}

Rapier.updateContent = function(element) {
    $("#data-extraction-stored-file div").removeClass("active");
    $(element).addClass("active");
    const name = $(element).attr("name");
    $.get("store-file/origin-content/" + name ).then(data => {
        $("#preprocessing-output").text(data);
        $("#process-html-name").prop("value", name);
    });

    $.get("store-file/extracted-content/" + name).then(data => {
        $("#remove-tag-html").text(data);
    })
}

Rapier.uploadRule = function(element) {
    const file = element.files[0];
    const formData = new FormData();
    formData.append('file', file);
    $.ajax({
        url: "preprocessing-rule",
        data: formData,
        processData: false,
        contentType: false,
        type: 'POST',
        success: success
    });

    function success(data) {
        console.log('here');
    }
}

function uploadPreprocessingFile(element) {
    let file = element.files[0];
    let formData = new FormData();
    formData.append('file', file);

    let xhr = new XMLHttpRequest();
    readFile(file, "#preprocessing-output");
    $("#preprocessing-file-display").val(element.value);
    xhr.onreadystatechange = () => {
        if (xhr.readyState != 4)
            return;

        if (xhr.status != 200) {
            console.log("error");
            return;
        }
        showMessage("Uploaded");
    }
    xhr.open("POST", "preprocessing-rule");
    xhr.send(formData);
}

function uploadHTMLFile(element) {
    upload(element.files[0], "extract").then(message => {
        $("#result-text").text(message);
    });
}

function readFile(file, target) {
    let fileReader = new FileReader();
    fileReader.onload = () => {
        $(target).text(fileReader.result);
    };
    fileReader.readAsText(file);
}

function upload(file, url) {
    let promise = new Promise((resolve, reject) => {
        let formData = new FormData();
        formData.append('file', file);
        let xhr = new XMLHttpRequest();
        readFile(file, "#input-text")
        // $("#preprocessing-file-display").val(file);
        xhr.onreadystatechange = function () {
            if (xhr.readyState != 4)
                return;
            if (xhr.status != 200) {
                console.log("error");
                reject('error');
                return;
            }
            alert("Uploaded");
            resolve(xhr.responseText);
        }
        xhr.open("POST", url);
        xhr.send(formData);
    });
    return promise;
}

function showMessage(message) {
    window.alert(message);
}
