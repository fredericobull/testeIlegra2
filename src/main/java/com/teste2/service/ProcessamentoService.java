package com.teste2.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import com.teste2.dto.LogDTO;
import com.teste2.utils.PathUtils;

public class ProcessamentoService {
	
	private List<LogDTO> logs = new ArrayList<LogDTO>();
	
	public Response receberLogs(InputStream input) {
		if (input != null) {
			try {
				OutputStream out = null;
				int read = 0;
				byte[] bytes = new byte[1024];
				out = new FileOutputStream(new File(PathUtils.getDiretorioLogs()+"log"+LocalDateTime.now().hashCode()+".log"),true);
				while ((read = input.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
				out.flush();
				out.close();
			} catch (Exception e) {
				return Response.status(500).entity("Erro ao gravar log").build();
			}
		} else {
			return Response.status(400).entity("Log invalido").build();
		}
		return Response.status(200).build();
	}
	
	public List<LogDTO> recuperarLogs() {
		File diretorio = new File(PathUtils.getDiretorioLogs());

        File arquivos[] = diretorio.listFiles(new FileFilter() {  
            public boolean accept(File pathname) {  
                return pathname.getName().toLowerCase().endsWith(".log");  
            }  
        });  
        for (int i = 0; i < arquivos.length; i++) {  
            File file = arquivos[i];  
            processarLogs(file);  
        } 
        return logs;
	}
	
	private void processarLogs(File file) {
		if (file.getName().indexOf(".log") != -1)  {
			try {
				String path = PathUtils.getDiretorioLogs()+file.getName();
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));
				String linha = reader.readLine();
				while (linha != null) {
					if (linha.startsWith("/")) {
						LogDTO log = new LogDTO();
						String[] lineParts = linha.split(" ");
						log.setUrl(lineParts[0]);
						log.setTimestamp(new Timestamp(Long.parseLong(lineParts[1])));
						log.setUuid(lineParts[2]);
						log.setRegion(Integer.valueOf(lineParts[3]));
						logs.add(log);
					}
					linha = reader.readLine();
				}
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<LogDTO> getLogs() {
		return logs;
	}

}
