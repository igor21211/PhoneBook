document.addEventListener("DOMContentLoaded", ()=>{
    let contactTable = document.querySelector(".contactTable");
    let contactForm = document.querySelector(".contactForm");
    let contactDelTable = document.querySelector('.contactDeletedTable');
    let btnBack = document.querySelector('.back-to-users');


    let contactTableComponent = new ContactTable(contactTable);
    let contactFormComponent = new ContactForm(contactForm, ()=> contactTableComponent.update());
    let contactDeletedTableComponent = new ContactDelTable(contactDelTable);
    contactFormComponent.init();
    contactTableComponent.init();
    contactDeletedTableComponent.init();

    btnBack.addEventListener("click", ()=>{
        document.location.href = "http://localhost:8080/";
    });

    const pagination = document.querySelector('.pagination');

    function createPagination(totalPages, currentPage) {

        pagination.innerHTML = '';

        if (currentPage > 0) {
            const prevButton = createButton(currentPage - 1, 'Назад');
            pagination.appendChild(prevButton);
        }

        for (let i = 0; i <= totalPages; i++) {
            const pageButton = createButton(i, i+1);
            if (i === currentPage) {
                pageButton.classList.add('active');
            }
            pagination.appendChild(pageButton);
        }

        if (currentPage < totalPages) {
            const nextButton = createButton(currentPage + 1, 'Вперед');
            pagination.appendChild(nextButton);
        }
    }

    function createButton(page, text) {
        const button = document.createElement('button');
        button.innerText = text;
        button.addEventListener('click', () => {
            getData(page);
        });
        return button;
    }

    function getData(page) {
        let uri = "/api/users/contacts/"+ window.location.href.substring(37,window.location.href.length)+"?page="+page;
        fetch(uri).then(resp=>resp.json()).then(resp=>{
            createPagination(resp.totalPages, resp.number);
            contactTableComponent.show(resp);
        })

    }

    fetch('/api/'+window.location.href.substring(22,window.location.href.length)).then(resp=>resp.json()).then(resp=>{
        createPagination(resp.totalPages, resp.number);
    })

});

class ContactForm{
    constructor(form, addListener) {
        this.inputFirstName = form.querySelector(".contact-first-name");
        this.inputLastName = form.querySelector(".contact-last-name");
        this.button = form.querySelector("button");
        this.addListener = addListener;
    }
    init(){
        this.button.addEventListener("click", ()=> this.register());
    }

    register(){
        let firstName = this.inputFirstName.value;
        let lastName = this.inputLastName.value;

        fetch("/api/users/contact/"+ window.location.href.substring(37,window.location.href.length),{
            method: "POST",
            body: JSON.stringify({
                firstName,lastName
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

class ContactTable{
    constructor(contactTable) {
        this.inputfirstName = contactTable.querySelector(".contact-first-name");
        this.inputlastName = contactTable.querySelector(".contact-last-name");
        this.tbody = contactTable.querySelector(".table-body-1");
    }
    init(){
        this.update();
        this.inputfirstName.addEventListener("change" , ()=>this.update());
        this.inputlastName.addEventListener("change" , ()=>this.update());
    }

    update(){
        let firstName = this.inputfirstName.value;
        let lastName = this.inputlastName.value;

        let uri = '/api/'+window.location.href.substring(22,window.location.href.length);
        if(firstName!=""|| lastName!=""){
            let params =[];
            if(firstName.trim()!=""){
                firstName = encodeURI(firstName.trim());
                params.push(`firstName=${firstName}`);
            }
            if(lastName.trim()!=""){
                lastName = encodeURI(lastName.trim());
                params.push(`lastName=${lastName}`)
            }
            uri+="?"+(params.join("&"));
        }

        fetch(uri)
            .then(resp=>resp.json())
            .then(resp=>{
                this.show(resp);

            })
    }

    show(resp){
        let html = "";
        for(let contact of resp.content){
            html+=`<tr><td>${contact.firstName}</td><td>${contact.lastName}</td><td>${contact.dateCreated}</td><td><a href="/users/contact/email/${contact.id}">Emails</a></td><td><a href="/users/contact/phone/${contact.id}">Phone</a></td><td><a class="open-popup" href="/users/contact/${contact.id}">Delete</a></td><td><a class="open-popup" href="/users/contact/${contact.id}/">Update</a></td></tr>`;
        }
        this.tbody.innerHTML= html;
    }


}

class ContactDelTable{
    constructor(contactDelTable) {
        this.inputfirstName = contactDelTable.querySelector(".contact-first-name");
        this.inputlastName = contactDelTable.querySelector(".contact-last-name");
        this.tbody = contactDelTable.querySelector(".table-body-2");

    }

    init(){
        this.update();
        this.inputfirstName.addEventListener("change" , ()=>this.update());
        this.inputlastName.addEventListener("change" , ()=>this.update());
    }

    update(){
        let firstName = this.inputfirstName.value;
        let lastName = this.inputlastName.value;

        let uri = '/api/'+window.location.href.substring(22,window.location.href.length)+'?isDeleted=true';
        if(firstName!=""|| lastName!=""){
            let params =[];
            if(firstName.trim()!=""){
                firstName = encodeURI(firstName.trim());
                params.push(`firstName=${firstName}`);
            }
            if(lastName.trim()!=""){
                lastName = encodeURI(lastName.trim());
                params.push(`lastName=${lastName}`)
            }
            uri+="?"+(params.join("&"));
        }

        fetch(uri)
            .then(resp=>resp.json())
            .then(resp=>{
                this.show(resp);
            })
    }
    show(resp){
        let html = "";
        for(let contact of resp.content){
            html+=`<tr><td>${contact.firstName}</td><td>${contact.lastName}</td><td>${contact.dateDeleted}</td><td><a href="/users/contact/restore/${contact.id}">Restore</a></td></tr>`;
        }
        this.tbody.innerHTML= html;
    }
}