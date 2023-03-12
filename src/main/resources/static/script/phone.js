document.addEventListener("DOMContentLoaded", ()=>{
    let phoneTable = document.querySelector(".phoneTable");
    let phoneForm = document.querySelector(".phoneForm");
    let btnBack = document.querySelector('.back-to-contact');


    let phoneTableComponent = new PhoneTable(phoneTable);
    let phoneFormComponent = new PhoneForm(phoneForm, ()=> phoneTableComponent.update());
    phoneFormComponent.init();
    phoneTableComponent.init();

    document.addEventListener('click', (e)=>{
        phoneTableComponent.update();
    });
    btnBack.addEventListener('click', ()=>{
        let referrer_url = document.referrer
        document.location.href = referrer_url;
    })

});

class PhoneForm{
    constructor(form, addListener) {
        this.inputPhoneName = form.querySelector(".phone-number");
        this.button = form.querySelector(".save-phone");
        this.addListener = addListener;
    }
    init(){
        this.button.addEventListener("click", ()=> this.register());
    }

    register(){
        let phoneOfNumber = this.inputPhoneName.value;

        fetch("/api/users/contact/phone/"+ window.location.href.substring(42,window.location.href.length),{
            method: "POST",
            body: JSON.stringify({
                phoneOfNumber
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

class PhoneTable{
    constructor(phoneTable) {
        this.tbody = phoneTable.querySelector("table>tbody");
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
        for(let phone of resp){
            html+=`<tr><td>${phone.phoneOfNumber}</td><td><button value="${phone.id}" class="delete-button" onclick="fetch('/api/users/contact/phone/'+${phone.id},{method:'PATCH'}).then(resp=>resp.json()).then(resp=>{
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

