package com.yroots.lambda.configs;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.yroots.lambda.models.KeyValue;
import com.yroots.lambda.services.StaticContextProvider;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

public class Util {

	
	public static Map<String,Object> getParams(List<KeyValue> data){
		Map<String,Object> params=new HashMap<>();
		if(data!=null) {
			data.forEach(d->{
				params.put(d.getKey(),d.getValue());
			});
		}
		return params;
	}
	
	public static String getFormattedText(String message,Map<String,Object> params) {
		String msg="";
		Template template=null; 
		try {
			if(message.endsWith(".ftl"))
				template = StaticContextProvider.getFreemarkerConfig().getTemplate(message);
			else
				template=new Template(UUID.randomUUID().toString(),new StringReader(message),StaticContextProvider.getFreemarkerConfig());			
			msg = FreeMarkerTemplateUtils.processTemplateIntoString(template, params);
		} catch (TemplateNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return msg;
	}
	public static String getFormattedFileName(String srvName,String catName,String fileName) {
		StringBuilder sb=new StringBuilder();
		sb.append(srvName.toLowerCase().trim());
		sb.append("-_-");		
		sb.append(catName.toUpperCase().trim());
		sb.append("-_-");		
		sb.append(fileName);
		return sb.toString();
	}
}
