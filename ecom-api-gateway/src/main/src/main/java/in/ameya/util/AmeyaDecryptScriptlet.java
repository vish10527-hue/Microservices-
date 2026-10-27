package in.ameya.util;

import in.ameya.securities.passwordutility.AsciiEncryption;
import in.ameya.securities.passwordutility.DecryptionNotSupportedException;
import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;
import net.sf.jasperreports.engine.fill.JRFillField;

public class AmeyaDecryptScriptlet extends JRDefaultScriptlet {

	@Override
	public void afterPageInit() throws JRScriptletException {
		AsciiEncryption ameyaCrypt = new AsciiEncryption();
		
		JRFillField panField = (JRFillField) this.fieldsMap.get("cst_applicant_pan");
		if(panField != null && panField.getValue() != null) {
			String panValue = panField.getValue().toString().strip();
			try {
				if(panValue.hashCode() != 0)
					panField.setValue(ameyaCrypt.decrypt(panValue));
			} catch (DecryptionNotSupportedException e) {
				System.out.println("AmeyaDecryptScriptlet.afterPageInit(PAN): "+ panValue);
			}
		}
		
		JRFillField kycField = (JRFillField) this.fieldsMap.get("ckyc_no");
		if(kycField != null && kycField.getValue() != null) {
			String kycValue = kycField.getValue().toString().strip();
			try {
				if(kycValue.hashCode() != 0)
					kycField.setValue(ameyaCrypt.decrypt(kycValue));
			} catch (DecryptionNotSupportedException e) {
				System.out.println("AmeyaDecryptScriptlet.afterPageInit(KYC): "+ kycValue);
			}
		}
		
	}
	
}
