package com.dodola.model;

import java.util.List;

import com.school.bean.Produce;

public class Infos {
	private String			newsLast	= "0";
	private int				type		= 0;
	private List<Produce>	newsInfos;

	public String getNewsLast() {
		return newsLast;
	}

	public void setNewsLast(String newsLast) {
		this.newsLast = newsLast;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<Produce> getNewsInfos() {
		return newsInfos;
	}

	public void setNewsInfos(List<Produce> newsInfos) {
		this.newsInfos = newsInfos;
	}

}
