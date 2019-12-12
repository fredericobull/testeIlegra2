package com.teste2.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.teste2.dto.LogDTO;

public class AnaliseLogsService {
	
	private List<LogDTO> logs = new ArrayList<LogDTO>();
	
	public String analisarLogs(List<LogDTO> logs, String metrica, int timeParam, String tipo) {
		this.logs = logs;
		switch (metrica) {
		case "1":
			return definirTop3URLMundo();
		case "2":
			return definirTop3URLPorRegiao();
		case "3":
			return definirTopURLmenorAcessoMundo();
		case "4":
			return definirTop3AcessosPor(tipo, timeParam);
		case "5":
			return definirMinutosMaisAcessosURLs();
		default:
			return "";
		}
	}
	
	private String definirTop3URLMundo() {
		Map<String, Integer> topURLs = new HashMap<String, Integer>();
		for (LogDTO log : logs) {
			topURLs.put(log.getUrl(), (topURLs.containsKey(log.getUrl()) ? topURLs.get(log.getUrl()) : 0) + 1);
		}
		topURLs = ordenarTop3(topURLs);
		
		return topURLs.toString();
	}
	
	private String definirTop3URLPorRegiao() {
		Map<String, Map<String, Integer>> topURLs = new HashMap<String, Map<String,Integer>>();  
		for (LogDTO log : logs) {
			String region = log.getRegion().toString();
			if (!topURLs.containsKey(region)) {
				topURLs.put(log.getRegion().toString(), new HashMap<String, Integer>());
			}
			topURLs.get(region).put(
					log.getUrl(), 
					(topURLs.get(region).containsKey(log.getUrl()) ? topURLs.get(region).get(log.getUrl()) : 0) + 1);
		}
		String top3Regions = "";
		
		for (Map.Entry<String, Map<String, Integer>> entry : topURLs.entrySet()) {
			Map<String, Integer> map = ordenarTop3(entry.getValue());
			top3Regions += "Region "+entry.getKey()+": "+map.toString()+" \n ";
		}
		return top3Regions;
	}
	
	private String definirTopURLmenorAcessoMundo() {
		Map<String, Integer> topURLs = new HashMap<String, Integer>();
		for (LogDTO log : logs) {
			topURLs.put(log.getUrl(), (topURLs.containsKey(log.getUrl()) ? topURLs.get(log.getUrl()) : 0) + 1);
		}
		topURLs = topURLs.entrySet().stream()
		        .sorted((Map.Entry.<String, Integer>comparingByValue())).limit(1)
		        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		
		return topURLs.toString();
	}
	
	private String definirTop3AcessosPor(String tipo, int timeParam) {
		Map<String, Integer> topURLs = new HashMap<String, Integer>();
		for (LogDTO log : filtrarListaPor(tipo, timeParam)) {
			topURLs.put(log.getUrl(), (topURLs.containsKey(log.getUrl()) ? topURLs.get(log.getUrl()) : 0) + 1);
		}
		topURLs = ordenarTop3(topURLs);
		
		return topURLs.toString();
	}
	
	private String definirMinutosMaisAcessosURLs() {
		Map<String, Map<String, Integer>> topURLs = new HashMap<String, Map<String,Integer>>();  
		for (LogDTO log : logs) {
			String url = log.getUrl();
			String timestamp = log.getTimestamp().toString();
			if (!topURLs.containsKey(url)) {
				topURLs.put(url, new HashMap<String, Integer>());
			}
			topURLs.get(url).put(
					timestamp, 
					(topURLs.get(url).containsKey(timestamp) ? topURLs.get(url).get(timestamp) : 0) + 1);
		}
		String topAccessURL = "";
		
		for (Map.Entry<String, Map<String, Integer>> entry : topURLs.entrySet()) {
			Map<String, Integer> map = ordenarTop(entry.getValue());
			topAccessURL += "URL "+entry.getKey()+": "+map.toString()+" \n ";
		}
		return topAccessURL;
	}
	
	private Map<String, Integer> ordenarTop3(Map<String, Integer> map) {
		return ordenarTop(map).entrySet().stream().limit(3)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}

	private Map<String, Integer> ordenarTop(Map<String, Integer> map) {
		return map.entrySet().stream()
				.sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}
	
	private List<LogDTO> filtrarListaPor(String tipo, int timeParam) {
		Predicate<LogDTO> filtro = null;
		if ("D".equals(tipo)) {
			filtro = l -> l.getDay() == timeParam;
		}
		if ("W".equals(tipo)) {
			filtro = l -> l.getWeek() == timeParam;
		}
		if ("Y".equals(tipo)) {
			filtro = l -> l.getYear() == timeParam;
		}
		return logs.stream().filter(filtro).collect(Collectors.toList());
	}

}
