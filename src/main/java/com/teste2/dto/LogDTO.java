package com.teste2.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class LogDTO {
	
	public LogDTO() { }
	
	private String url;
	private Timestamp timestamp;
	private String uuid;
	private Integer region;
	
	private Integer day;
	private Integer week;
	private Integer year;
	
	private Integer acessos;
	
	private void processTimestamp() {
		LocalDateTime ldt = this.timestamp.toLocalDateTime();
		this.year = ldt.getYear();
		this.day = ldt.getDayOfYear();
		WeekFields weekFields = WeekFields.of(Locale.getDefault());
		this.week = ldt.get(weekFields.weekOfWeekBasedYear());
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
		processTimestamp();
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Integer getRegion() {
		return region;
	}
	public void setRegion(Integer region) {
		this.region = region;
	}
	public Integer getDay() {
		return day;
	}
	public Integer getWeek() {
		return week;
	}
	public Integer getYear() {
		return year;
	}

	public Integer getAcessos() {
		return acessos;
	}

	public void setAcessos(Integer acessos) {
		this.acessos = acessos;
	}

}
