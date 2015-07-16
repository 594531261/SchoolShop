package com.school.bean;

import org.json.JSONObject;

public class AddressBean extends BaseBean{
	//{"data":{"address_uphone":"15200066250","school_address":"","address_uname":"何大为",
	//"school_name":"","address_id":"4",
		//"school_id":"","address_detail":"河北农大8号楼805"},"msg":"地址获取成功","code":"1"}
	public String address_uphone;
	public String school_address;
	public String address_uname;
	public String school_name;
	public String address_id;
	public String school_id;
	public String address_detail;

	@Override
	public void parse(JSONObject oj) {
		school_id = getStringFromJson(oj, "school_id");
		address_uphone = getStringFromJson(oj, "address_uphone");
		school_address = getStringFromJson(oj, "school_address");
		address_uname = getStringFromJson(oj, "address_uname");
		school_name = getStringFromJson(oj, "school_name");
		address_id = getStringFromJson(oj, "address_id");
		address_detail = getStringFromJson(oj, "address_detail");
		
	}

}
