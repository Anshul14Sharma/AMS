function saveAtt(){
        var inputEmail = $('#email').val();
        if(inputEmail == '') {
                window.alert("Email cannot be empty!");
                return false;
        }
        var obj = {
        email: inputEmail
        };
        $.ajax({
        url: "/attendance",
        data: JSON.stringify(obj),
        headers: {
        'Content-Type': 'application/json'
        },
        type: 'POST',
        success: function(res) {
        if (res == "Checked In") {
            window.alert("Checked In Successfully!");
        } else if(res == "Checked Out"){
            window.alert("Checked Out Successfully!");
        }else if(res == "404"){
            window.alert("Employee not found");
        }
        }
        });
        return false;
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
    $.ajax({
    url: "/login",
    data: JSON.stringify(obj),
    headers: {
    'Content-Type': 'application/json'
    },
    type: 'POST',
    success: function(res) {
    if (res == "200") {
    window.alert("Login Successful!");
    window.location.replace("/checkInOut");
    } else if(res == "401"){
    window.alert("Invalid Credentials");
    }
    }
    });
    return false;
}

function register(){
    var email = $('#email').val();
    var password = $('#pwd').val();
    var firstName = $('#fName').val();
    var lastName = $('#lName').val()
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
    if(lastName == ''){
        window.alert("LastName cannot be empty!");
        return false;
    }
    var obj = {
    email: email,
    password: password,
    firstName: firstName,
    lastName: lastName
    };
    $.ajax({
    url: "/register",
    data: JSON.stringify(obj),
    headers: {
    'Content-Type': 'application/json'
    },
    type: 'POST',
    success: function(res) {
    if (res == "200") {
    window.alert("Registration Successful!");
    window.location.replace("/login");
    } else if(res == "409"){
    window.alert("Employee Already Exists");
    } else{
    window.alert("Server error!");
    }
    }
    });
    return false;
    }

if(document.getElementById("emp") != null){
    $(document).ready(function () {
        $('#emp').DataTable({
            "bPaginate": true
        });
    });
}