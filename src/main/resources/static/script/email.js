document.addEventListener("DOMContentLoaded", ()=>{
    let emailTable = document.querySelector(".emailTable");
    let emailForm = document.querySelector(".emailForm");


    let emailTableComponent = new EmailTable(emailTable);
    let emailFormComponent = new EmailForm(emailForm, ()=> emailTableComponent.update());
    emailFormComponent.init();
    emailTableComponent.init();

    document.addEventListener('click', (e)=>{
               emailTableComponent.update();
    });
});

class EmailForm{
    constructor(form, addListener) {
        this.inputEmailName = form.querySelector(".email-name");
        this.button = form.querySelector(".save-email");
        this.addListener = addListener;
    }
    init(){
        this.button.addEventListener("click", ()=> this.register());
    }

    register(){
        let email = this.inputEmailName.value;

        fetch("/api/users/contact/email/"+ window.location.href.substring(42,window.location.href.length),{
            method: "POST",
            body: JSON.stringify({
                email
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
                    this.addListener();
                }
            })
    }
}

class EmailTable{
    constructor(emailTable) {
        this.tbody = emailTable.querySelector("table>tbody");
    }
    init(){
        this.update();
    }

    update(){
        let uri = '/api/'+window.location.href.substring(22,window.location.href.length);
        fetch(uri)
            .then(resp=>resp.json())
            .then(resp=>{
                this.show(resp);

            })
    }

    show(resp){
        let html = "";
        for(let email of resp){
            html+=`<tr><td>${email.email}</td><td><button value="${email.id}" class="delete-button" onclick="fetch('/api/users/contact/email/'+${email.id},{method:'PATCH'}).then(resp=>resp.json()).then(resp=>{
                if(resp.message){
                    alert(resp.message)
                }else {
                    this.update();
                }
            })">Delete</button></td></tr>`;
        }
        this.tbody.innerHTML= html;
    }


}

