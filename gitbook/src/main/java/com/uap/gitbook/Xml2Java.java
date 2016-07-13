/**
 * 
 */
package com.uap.gitbook;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

/**
 * @author Frank
 *
 */
public class Xml2Java {
	@SuppressWarnings("unchecked")
	public static <T> T xmltojava(Class<T> cls, String file) throws Exception {
		File f = new File(file);
		JAXBContext ctx = JAXBContext.newInstance(cls);
		Unmarshaller us = ctx.createUnmarshaller();
		return (T) us.unmarshal(f);
	}

}
