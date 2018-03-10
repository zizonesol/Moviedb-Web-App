/*
 * CS 122B Project 4. Autocomplete Example.
 * 
 * This Javascript code uses this library: https://github.com/devbridge/jQuery-Autocomplete
 * 
 * This example implements the basic features of the autocomplete search, features that are 
 *   not implemented are mostly marked as "TODO" in the codebase as a suggestion of how to implement them.
 * 
 * To read this code, start from the line "$('#autocomplete').autocomplete" and follow the callback functions.
 * 
 */

var jdict = {};

/*
 * This function is called by the library when it needs to lookup a query.
 * 
 * The parameter query is the query string.
 * The doneCallback is a callback function provided by the library, after you get the
 *   suggestion list from AJAX, you need to call this function to let the library know.
 */
function handleLookup(query, doneCallback) {
	console.log("autocomplete initiated")
	console.log("sending AJAX request to backend Java Servlet")
	
	var notfound = true;
	var y = query;
	var z;
	y.replace(" ","+");

	
	if(!(jdict.hasOwnProperty(y)))
		{
			// sending the HTTP GET request to the Java Servlet endpoint hero-suggestion
			// with the query data
			jQuery.ajax({
				"method": "GET",
				// generate the request url from the query.
				// escape the query string to avoid errors caused by special characters 
				"url": "movie_suggestion?query=" + escape(y),
				"success": function(data) {
					console.log("look up from ajax")
					handleLookupAjaxSuccess(data, query, doneCallback) 
				},
				"error": function(errorData) {
					console.log("lookup ajax error")
					console.log(errorData)
				}
			})
		}
	else
		{
		console.log("look up from cache");
		doneCallback( { suggestions: jdict[y] } );
		}
}


/*
 * This function is used to handle the ajax success callback function.
 * It is called by our own code upon the success of the AJAX request
 * 
 * data is the JSON data string you get from your Java Servlet
 * 
 */
function handleLookupAjaxSuccess(data, query, doneCallback) {
	console.log("lookup ajax successful")
	
	// parse the string into JSON
	var jsonData = JSON.parse(data);
	console.log(jsonData);
	var z = query;
	jdict[z.replace(" ","+")] = jsonData;
	console.log(jdict);
	
	// TODO: if you want to cache the result into a global variable you can do it here

	// call the callback function provided by the autocomplete library
	// add "{suggestions: jsonData}" to satisfy the library response format according to
	//   the "Response Format" section in documentation
	doneCallback( { suggestions: jsonData } );
}


/*
 * This function is the select suggestion hanlder function. 
 * When a suggestion is selected, this function is called by the library.
 * 
 * You can redirect to the page you want using the suggestion data.
 */
function handleSelectSuggestion(suggestion) {
	// TODO: jump to the specific result page based on the selected suggestion
	
	console.log("you select " + suggestion["value"])
	var url = suggestion["data"]["category"] + "-movie" + "?id=";
	
	
	if(suggestion["data"]["category"] == "movie")
		{
			window.location.replace("http://localhost:8080/project3/servlet/movieinfo?movie_title="+suggestion["value"]);
			console.log("http://localhost:8080/project3/servlet/movieinfo?movie_title="+suggestion["value"])
		}
	else
		{
			window.location.replace("http://localhost:8080/project3/servlet/starinfo?star_name="+suggestion["value"]);
			console.log("http://localhost:8080/project3/servlet/starinfo?star_name="+suggestion["value"])
		}
	
}


/*
 * This statement binds the autocomplete library with the input box element and 
 *   sets necessary parameters of the library.
 * 
 * The library documentation can be find here: 
 *   https://github.com/devbridge/jQuery-Autocomplete
 *   https://www.devbridge.com/sourcery/components/jquery-autocomplete/
 * 
 */
// $('#autocomplete') is to find element by the ID "autocomplete"
$('#autocomplete').autocomplete({
	// documentation of the lookup function can be found under the "Custom lookup function" section
    lookup: function (query, doneCallback) {
    		handleLookup(query, doneCallback)
    },
    onSelect: function(suggestion) {
    		handleSelectSuggestion(suggestion)
    },
    // set the groupby name in the response json data field
    groupBy: "category",
    // set delay time
    deferRequestBy:300,
    minChars:3
    // there are some other parameters that you might want to use to satisfy all the requirements
    // TODO: add other parameters, such as mininum characters
});


/*
 * do normal full text search if no suggestion is selected 
 */
function handleNormalSearch(query) {
	console.log("doing normal search with query: " + query);
	var x = query;
	x.replace(" ","+")
	window.location.replace("http://localhost:8080/project3/servlet/searchpage?title="+x)
	// TODO: you should do normal search here
}

// bind pressing enter key to a hanlder function
$('#autocomplete').keypress(function(event) {
	// keyCode 13 is the enter key
	if (event.keyCode == 13) {
		// pass the value of the input box to the hanlder function
		handleNormalSearch($('#autocomplete').val())
	}
})

// TODO: if you have a "search" button, you may want to bind the onClick event as well of that button


