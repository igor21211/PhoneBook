document.addEventListener("DOMContentLoaded", ()=>{
    let inputFirstName = document.querySelector('.contact-first-name');
    let inputLastName = document.querySelector('.contact-last-name');
    let divOldUsers = document.querySelector('.contact-old');
    let btnUpdate = document.querySelector('.update');
    let btnBack = document.querySelector('.go-back');
    let uri = '/api/' + window.location.href.substring(22, window.location.href.length-1);


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

    btnBack.addEventListener('click', ()=>{
        let referrer_url = document.referrer
        document.location.href = referrer_url;
    })
});