package mil.pusdalops.domain;

import mil.pusdalops.domain.kejadian.AttributeEncryptor;

public class App_EncryptorUse {

	public static void main(String[] args) throws Exception {
		
		AttributeEncryptor attributeEncryptor = new AttributeEncryptor();
		
		String toDBCol = attributeEncryptor.convertToDatabaseColumn("This is a dog");
		System.out.println(toDBCol);
		
		String toEntity = attributeEncryptor.convertToEntityAttribute("This is a dog");
		System.out.println(toEntity);
	}

}
