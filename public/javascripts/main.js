function postRequest(url, obj, handleData){
    $.ajax({
    type: "POST",
    url: url,
    data: JSON.stringify(obj),
    contentType: "application/json",
    crossDomain: true,
    //            async: false,
    success: function (data) {
        handleData(data);
    }
});
}
function saveAtt(){
    postRequest("/attendance", null, function(res){
        if (res == "Checked In") {
            window.alert("Checked In Successfully!");
        } else if(res == "Checked Out"){
            window.alert("Checked Out Successfully!");
        }else if(res == "404"){
            window.alert("Employee not found");
        }
    });
}

function validateEmail(sEmail) {
    var filter = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    if (filter.test(sEmail)) {
        return true;
    }
    else {
        return false;
    }
}

function login(){
    var email = $('#email').val();
    var password = $('#pwd').val();
    if(email == ''){
        window.alert("Email cannot be empty!");
        return false;
    }
    if(!validateEmail(email)){
        window.alert("Invalid Email");
        return false;
    }
    if(password == ''){
        window.alert("Password cannot be empty!");
        return false;
    }
    var obj = {
    email: email,
    password: password,
    };
    postRequest("/login", obj, function(res){
        if (res == "200") {
            window.alert("Login Successful!");
            window.location.replace("/checkInOut");
        } else if(res == "201"){
            window.alert("Login Successful!");
            window.location.replace("/admin");
        }else if(res == "401"){
            window.alert("Invalid Credentials");
        }
    });
}

function register(){
    var email = $('#email').val();
    var password = $('#pwd').val();
    var firstName = $('#fName').val();
    var lastName = $('#lName').val()
    var role = $("#role").val();
    if(email == ''){
        window.alert("Email cannot be empty!");
        return false;
    }
    if(!validateEmail(email)){
        window.alert("Invalid Email!");
        return false;
    }
    if(password == ''){
        window.alert("Password cannot be empty!");
        return false;
    }
    if(firstName == ''){
        window.alert("FirstName cannot be empty!");
        return false;
    }
//    if(lastName == ''){
//        window.alert("LastName cannot be empty!");
//        return false;
//    }
    var obj = {
    email: email,
    password: password,
    firstName: firstName,
    lastName: lastName,
    role: role
    };
    postRequest("/register", obj, function(res){
        if (res == "200") {
            window.alert("Registration Successful!");
            window.location.replace("/login");
        } else if(res == "409"){
            window.alert("Employee Already Exists");
        } else{
            window.alert("Server error!");
        }
    });
}

if(document.getElementById("emp") != null){
    $(document).ready(function () {
        $('#emp').DataTable({
            "bPaginate": true
        });
    });
}