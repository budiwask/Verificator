package com.budiw.fpapplet;

import java.io.FileInputStream;

import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import com.digitalpersona.onetouch.verification.DPFPVerificationResult;


public class VerificatorServer {
	private static DPFPVerification verificator = DPFPGlobal.getVerificationFactory().createVerification();
	private static DPFPFeatureSet uploadedFeatureSet;
	private static DPFPTemplate storedTemplate;
	
	public static void main(String args[]) {
		if(args.length < 2) {
			System.err.println("ARGS:\n [0]: featureSetName\n [1]: templateName");
		}
		
		try {
			//Read template file
			FileInputStream stream = new FileInputStream(args[0]);
			

			byte[] data = new byte[stream.available()];
			stream.read(data);
			stream.close();
			storedTemplate = DPFPGlobal.getTemplateFactory().createTemplate();
			storedTemplate.deserialize(data);

			//Read feature set file
			stream = new FileInputStream(args[1]);
			data = new byte[stream.available()];
			stream.read(data);
			stream.close();
			uploadedFeatureSet = DPFPGlobal.getFeatureSetFactory().createFeatureSet(data);

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());;
			e.printStackTrace();
		}
		
		
		
		DPFPVerificationResult result = verificator.verify(uploadedFeatureSet, storedTemplate);
		if (result.isVerified()) {
			System.out.println("VERIFIED");
		} else {
			System.out.println("DENIED");
		}
	}

}
