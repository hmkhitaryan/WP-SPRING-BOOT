$(document).ready(function() {
	$('#passwordConfirm').blur(function() {
		$.ajax({
		    type: "POST",
			url : 'checkEquality',
			data : {
				password : $('#password').val(),
				passwordConfirm : $('#passwordConfirm').val(),
			},
			success : function(response) {
                if(response.status != "SUCCESS"){
                   $('#ajaxGetPasswordConfirmResponse').html("Password and passwordConfirm do not match");
                   $('#ajaxGetPasswordConfirmResponse').show('slow');
                } else {
                   $('#ajaxGetPasswordConfirmResponse').html("");
                }
            },
			error: function(responseText){
			     $('#ajaxGetPasswordConfirmResponse').text(responseText);
            }
		});
	});

	$('#password').blur(function() {
    	$.ajax({
    	    type: "POST",
   			url : 'checkLength',
   			data : {
   				password : $('#password').val(),
   			},

   			success : function(response) {
                if(response.status != "SUCCESS"){
                    $('#ajaxGetPasswordResponse').html("Please use password between 6 and 32 characters.");
                    $('#ajaxGetPasswordResponse').show('slow');
                } else {
                    $('#ajaxGetPasswordResponse').html("");
                }
            },

    		error: function(responseText){
    			 $('#ajaxGetPasswordResponse').text(responseText);
            }
    	});
    });

    $('#email').blur(function() {
        	$.ajax({
        	    type: "POST",
       			url : 'checkEmail',
       			data : {
       				email : $('#email').val(),
       			},

       			success : function(response) {
                    if(response.status != "SUCCESS"){
                        $('#ajaxGetEmailResponse').html("invalid email");
                        $('#ajaxGetEmailResponse').show('slow');
                    } else {
                        $('#ajaxGetEmailResponse').html("");
                    }
                },

        		error: function(responseText){
        			 $('#ajaxGetEmailResponse').text(responseText);
                }
        	});
        });

});