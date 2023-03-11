document.addEventListener("DOMContentLoaded", ()=>{
    let userForm = document.querySelector(".userForm");
    let userTable = document.querySelector(".userTable");
    let userDelTable = document.querySelector('.userDeletedTable');

    let userTableComponent = new UserTable(userTable);
    let userFormComponent = new UserForm(userForm, ()=> userTableComponent.update());
    let userDeletedTableComponent = new UserDelTable(userDelTable);
    userFormComponent.init();
    userTableComponent.init();
    userDeletedTableComponent.init();

});

class UserForm{
    constructor(form, addListener) {
        this.inputfirstName = form.querySelector(".user-first-name");
        this.inputlastName = form.querySelector(".user-last-name");
        this.button = form.querySelector("button");
        this.addListener = addListener;

    }

    init(){
        this.button.addEventListener("click", ()=> this.register());
    }

    register(){
        let firstName = this.inputfirstName.value;
        let lastName = this.inputlastName.value;

        fetch("/api/users",{
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

class UserTable{

    constructor(userTable) {
        this.inputfirstName = userTable.querySelector(".user-first-name");
        this.inputlastName = userTable.querySelector(".user-last-name");
        this.tbody = userTable.querySelector(".table-body-1");
    }

    init(){
        this.update();
        this.inputfirstName.addEventListener("change" , ()=>this.update());
        this.inputlastName.addEventListener("change" , ()=>this.update());
    }


    update(){
        let firstName = this.inputfirstName.value;
        let lastName = this.inputlastName.value;

        let uri = "/api/users";
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
        for(let user of resp.content){
            html+=`<tr><td>${user.firstName}</td><td>${user.lastName}</td><td>${user.dateCreated}</td><td><a class="open-popup" href="/users/contacts/${user.id}">Contacts</a></td><td><a class="open-popup" href="/users/remove/${user.id}">Delete</a> </td><td><a class="open-popup" href="/users/${user.id}">Update</a> </td></tr>`;
        }
        this.tbody.innerHTML= html;
    }

}

class UserDelTable{
    constructor(userDltTable) {
        this.inputfirstName = userDltTable.querySelector(".user-first-name");
        this.inputlastName = userDltTable.querySelector(".user-last-name");
        this.tbody = userDltTable.querySelector(".table-body-2");

    }

    init(){
        this.update();
        this.inputfirstName.addEventListener("change" , ()=>this.update());
        this.inputlastName.addEventListener("change" , ()=>this.update());
    }

    update(){
        let firstName = this.inputfirstName.value;
        let lastName = this.inputlastName.value;

        let uri = "/api/users?isDeleted=true";
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
        for(let user of resp.content){
            html+=`<tr><td>${user.firstName}</td><td>${user.lastName}</td><td>${user.dateDeleted}</td><td><a href="/users/restore/${user.id}">Restore</a></td></tr>`;
        }
        this.tbody.innerHTML= html;
    }

}


