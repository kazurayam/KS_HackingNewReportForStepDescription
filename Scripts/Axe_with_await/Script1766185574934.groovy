import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import groovy.json.JsonSlurper

WebUI.openBrowser('')
WebUI.navigateToUrl('https://katalon-demo-cura.herokuapp.com/')

String script = """
async function main() {
	function loadScriptOnDemand(fileUrl, callback) {
		// 1. Create a new script element
		let newScript = document.createElement('script');
		// 2. Set the necessary attributes
		newScript.setAttribute('src', fileUrl);
		newScript.setAttribute('type', 'text/javascript');
		newScript.setAttribute('async', true); // Allows the script to load without blocking the page rendering
		// 3. Monitor the script's loading events
		newScript.onload = () => {
			console.log(fileUrl + ' loaded successfully');
			if (callback) {
				callback(); // Execute the callback function if one was provided
			}
		}
		newScript.onerror = () => {
			console.error('Error loading script: ' + fileUrl);
		}
		// 4. Append the script element to the document's head.
		// Appending it to the head is generally a good practice
		// though the body also works
		document.head.appendChild(script);
	}
	loadScriptOnDemand('https://cdnjs.cloudflare.com/ajax/libs/axe-core/4.7.0/axe.min.js');
	// Wait for the script to be loaded
	const delay = ms => new Promise(res => setTimeout(res, ms));
	await delay(5000);
	// Run Axe
	let axeResults = null;
	let axeError = null;
	await axe.run(document)
		.then(response => { axeResults = response })
		.catch(error => axeError = error.message || 'Unknown error');
	// Return a JSON string
	return JSON.stringify(axeResults || axeError);
};
return main();
""";

String resultsJson = WebUI.executeJavaScript(script, null);
println(resultsJson)
	
// Parse and print violations
def parsed = new JsonSlurper().parseText(resultsJson)
if (parsed?.violations) {
	println("Violations count: ${parsed.violations.size()}")
	parsed.violations.each { v ->
		println("- ${v.id}: ${v.description} (impact: ${v.impact})")
	}
} else {
	println("Error or no violations: ${parsed.error}")
}

WebUI.closeBrowser()

