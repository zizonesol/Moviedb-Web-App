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
	

	console.log(y);
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
 * 
 */

/*
 * This function is called by the library when it needs to look up a query
 *  
 * The parameter query is the query string
 * The doneCallback is a callback function provided by the library, after you get the
 * 	suggestion list from the AJAX, you need to call this function to let the library know
 */
function handleLookup(query, doneCallback)
{
	console.log("autocomplete initiated")
	console.log("sending AJAX request to backend Java Servlet")
	
	jQuery.ajax
	({
		"method": "GET",
		// generate the request url from the query.
		// escape the query string to avoid errors caused by special characters
		"url": "movie-suggestion?query=" + escape(query),
		"success": function(data)
		{
			handleLookupAjaxSuccess(data, query, doneCallback)
		},
		"error": function(errorData)
		{
			console.log("lookup ajax error")
			console.log(errorData);
		}
	})
}
