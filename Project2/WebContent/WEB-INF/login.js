function handleLoginResult(resultDataString)
{
	resultDaretaJson = JSON.parse(resultDataString);
	
	console.log("handle login response");
	console.log(resultDataString);
	console.log(resultDataJson["status"]);
	
	// if login success, redirect to index.html page
	if (resultDataJson["status"] == "success")
		window.location.replace("/project2-login-example/index.html");
	else
	{
		console.log("show error message");
		console.log(resultDataJson["message"]);
		jQuery("#login_error_message").text(resultDataJson["message"]);
	}
}


function submitLoginForm(formSubmitEvent)
{
	console.log("submit login form");
	
	// important: disable the default action of submitting the form
	//	which will cause the page to refresh
	//	see jQuery reference for details: https://api.jquery.com/submit/
	formSubmitEvent.preventDefault();
	
	jQuery.post(
			"/project2-login/Login",
			// serialize the login form to the data sent by POST request
ey=
			(resultDataString) => handleLoginResult(resultDataString));
}

// bind the submit action of the form to a handler function
jQuery("#login_form").submit((event) => submitLoginForm(event));