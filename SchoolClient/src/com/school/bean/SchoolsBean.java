package com.school.bean;

import org.json.JSONObject;

public class SchoolsBean extends BaseBean {
	public String school_id;
	public String name;
	public String school_address;

	public void parse(JSONObject oj) {
		this.school_id = getStringFromJson(oj, "school_id");
		this.name = getStringFromJson(oj, "name");
		this.school_address = getStringFromJson(oj, "school_address");
	}

}
