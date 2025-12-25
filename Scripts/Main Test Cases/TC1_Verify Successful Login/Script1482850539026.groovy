import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable

WebUI.comment('Story: Login to CURA system')

WebUI.comment('Given that the user has the valid login information')

'open browser'
WebUI.openBrowser(GlobalVariable.G_SiteURL)

'click the button of Make Appointment'
WebUI.click(findTestObject('Page_CuraHomepage/btn_MakeAppointment'))

'type in User name'
WebUI.setText(findTestObject('Page_Login/txt_UserName'), Username)

'type in Password'
WebUI.setText(findTestObject('Page_Login/txt_Password'), Password)

WebUI.comment('When he logins to CURA system')

'click the button Login'
WebUI.click(findTestObject('Page_Login/btn_Login'))

WebUI.comment('Then he should be able to login successfully')

'make sure that we are transferred to the landing page where a string "Appointment" is displayed'
landingPage = WebUI.verifyElementPresent(findTestObject('Page_CuraAppointment/div_Appointment'), GlobalVariable.G_Timeout)

'close browser'
WebUI.closeBrowser()

