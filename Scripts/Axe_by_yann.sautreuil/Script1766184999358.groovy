import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import groovy.json.JsonSlurper

WebUI.openBrowser('')
WebUI.navigateToUrl('https://katalon-demo-cura.herokuapp.com/')

WebUI.executeJavaScript("""
    window.__axeResults = null;
    window.__axeError = null;

    (function(){
        function runAxe(){
            axe.run(document).then(function(results){
                window.__axeResults = results;
            }).catch(function(err){
                window.__axeError = err.message || 'Unknown error';
            });
        }

        if (window.axe) {
            runAxe();
        } else {
            var script = document.createElement('script');
            script.src = 'https://cdnjs.cloudflare.com/ajax/libs/axe-core/4.7.0/axe.min.js';
            script.async = true;
            script.onload = runAxe;
            script.onerror = function(e){ window.__axeError = 'Failed to load axe-core'; };
            document.head.appendChild(script);
        }
    })();
""", null)
	
// Wait for axe to finish (adjust delay if needed)
WebUI.delay(5) // 5 seconds
	
// Retrieve results from the browser
String resultsJson = WebUI.executeJavaScript("""
    return JSON.stringify(window.__axeResults || { error: window.__axeError || 'No results yet' });
""", null)
	
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
