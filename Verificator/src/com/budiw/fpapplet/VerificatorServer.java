package com.budiw.fpapplet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import com.digitalpersona.onetouch.verification.DPFPVerificationResult;


public class VerificatorServer {
	private static DPFPVerification verificator = DPFPGlobal.getVerificationFactory().createVerification();
	private static DPFPFeatureSet uploadedFeatureSet;
	private static DPFPTemplate storedTemplate;
	
	public static void main(String args[]) throws IOException {
		if(args.length < 2) {
			System.err.println("ARGS:\n [0]: templateName\n [1]: featureName\n");
		}
		
		System.out.println(new File(".").getAbsolutePath());
		
		FileInputStream stream = null;
		try {
			//Read template file
			stream = new FileInputStream(args[0]);
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
			
			DPFPVerificationResult result = verificator.verify(uploadedFeatureSet, storedTemplate);
			if (result.isVerified()) {
				System.out.println("VERIFIED");
			} else {
				System.out.println("DENIED");
			}

		} catch(FileNotFoundException e) {
			System.out.println("File not found");
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());;
			e.printStackTrace();
		} finally {
			if(stream!=null) {
				stream.close();
			}
		}
	}
}
