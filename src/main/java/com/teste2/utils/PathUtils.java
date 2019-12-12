package com.teste2.utils;

import java.io.File;

public class PathUtils {
		
    public static String getHomePath() {
    	return System.getenv("HOMEPATH");
    }
    
    public static String getDiretorioLogs() {
    	mkdir(getHomePath()+"/data/logs/");
    	return getHomePath()+"/data/logs/";
    }
    
    public static void mkdir(String path) {
		File fp = new File(path);
		if (!fp.exists()) {
			fp.mkdirs();
		}
	}
    
}
