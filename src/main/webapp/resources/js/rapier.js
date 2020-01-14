function uploadPreprocessingFile(element) {
    let file = element.files[0];
    let formData = new FormData();
    formData.append('file', file);
    let xhr = new XMLHttpRequest();
    $("#preprocessing-file-display").val(element.value);
    xhr.onreadystatechange = res => {
        if (res.readyState != 4)
            return;
        if (res.status != 200) {
            console.log("error");
            return;
        }
        alert("Uploaded");
    }
    xhr.open("POST", "preprocessing-rule");
    xhr.send(formData);
}

function uploadHTMLFile(element) {
    upload(element.files[0], "extract").then(message => {
        console.log(message);
    });
}

function upload(file, url) {
    let promise = new Promise((resolve, reject) => {
        let formData = new FormData();
        formData.append('file', file);
        let xhr = new XMLHttpRequest();
        // $("#preprocessing-file-display").val(file);
        xhr.onreadystatechange = res => {
            if (res.readyState != 4)
                return;
            if (res.status != 200) {
                console.log("error");
                reject('error');
                return;
            }
            alert("Uploaded");
            resolve('success');
        }
        xhr.open("POST", url);
        xhr.send(formData);
    });
    return promise;
}