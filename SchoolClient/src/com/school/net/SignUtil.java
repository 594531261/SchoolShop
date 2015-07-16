package com.school.net;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import net.tsz.afinal.http.AjaxParams;

import org.apache.commons.codec.binary.Hex;

public class SignUtil {
	private static String appsecret="BQ62KGBhchC7Cz4e";
	/**
	 * 获取签名的网络请求参数
	 * @param srcList
	 * @param map
	 * @return
	 */
	public static AjaxParams getSignatureAjaxParams(TreeMap<String , String> map ) {
		AjaxParams params = new AjaxParams();
		
		String signature = null;
//			srcList = new ArrayList<String>();
//			srcList.add("13401034954");
		// 按照字典序逆序拼接参数
		StringBuilder sb = new StringBuilder();
		
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			String value  = map.get(key);
			sb.append(value);
			sb.append("||");
			
			params.put(key, value);
		}
		System.out.println("sb---------------->"+sb.toString());
		String sss= sb.toString()+appsecret;
		signature = digest(sss, "MD5");
		
		params.put("sign", signature.substring(5,signature.length()-5));
		return params;
	}
	public static String generateSignature(List<String> srcList,HashMap<String , String> map ) {
		String signature = null;
//			srcList = new ArrayList<String>();
//			srcList.add("13401034954");
			// 按照字典序逆序拼接参数
			Collections.sort(srcList);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < srcList.size(); i++) {
				String key = srcList.get(i).toString();
				String value  = map.get(key);
				
				sb.append(value);
				sb.append("||");
			}
			System.out.println("sb---------------->"+sb.toString());
			String sss= sb.toString()+appsecret;
			signature = digest(sss, "MD5");
			srcList.clear();
			srcList = null;
		return signature;
	}
	
	private static String digest(String strSrc, String encName) {
		MessageDigest md = null;
		String strDes = null;
		byte[] bt = strSrc.getBytes();
		try {
			if (encName == null || encName.equals("")) {
				encName = "MD5";
			}
			md = MessageDigest.getInstance(encName);
			md.update(bt);
			
			strDes = new String(Hex.encodeHex(md.digest())); 
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		return strDes;
	}
	
	/*public static void main(String[] args) {
		String sign = generateSignature("sf","asfsaf","asfsf",0);
		System.out.println(sign);
	}*/

}
