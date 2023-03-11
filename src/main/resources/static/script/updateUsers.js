document.addEventListener("DOMContentLoaded", ()=>{
    let inputFirstName = document.querySelector('.user-first-name');
    let inputLastName = document.querySelector('.user-last-name');
    let divOldUsers = document.querySelector('.users-old');
    let btnUpdate = document.querySelector('.update');
    let uri = '/api/' + window.location.href.substring(22, window.location.href.length);
    update(uri);
    function update(uri) {
        fetch(uri).then(resp => resp.json())
            .then(resp => {
                let html = "";

                html += `<ul><li>${resp.firstName}</li><li>${resp.lastName}</li></ul>`;
                divOldUsers.innerHTML = html;

            })
    }
    btnUpdate.addEventListener('click', ()=>{
    fetch(uri,{
        method: "PUT",

        body: JSON.stringify({
            firstName: inputFirstName.value,
            lastName: inputLastName.value
        }),
        headers:{
            "Content-Type":"application/json"
        }
    })
        .then(resp => resp.json())
        .then(resp=>{
            if(resp.message){
                alert(resp.message)
            }else {
                update(uri);
            }
        })

    });
});