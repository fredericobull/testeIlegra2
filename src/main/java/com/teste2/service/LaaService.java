package com.teste2.service;

import java.io.InputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.teste2.dto.LogDTO;

@Path("/laa")
public class LaaService {
	
	private ProcessamentoService processamentoService = new ProcessamentoService();
	private AnaliseLogsService analiseLogsService= new AnaliseLogsService();
	
	
	@Path("/health")
	@GET
	public Response health() {
		if (processamentoService.getLogs() == null) {
			return Response.status(500).build();
		}
		return Response.status(200).build();
	}
	
	@Path("/ingest")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response ingest(InputStream input) {
		return processamentoService.receberLogs(input);
	}
	
	@Path("/metrics/{metrica}/{timeParam}/{tipo}")
	@GET
	public Response metrics(@PathParam("metrica") String metrica, @PathParam("timeParam") int timeParam, @PathParam("tipo") String tipo) {
		List<LogDTO> logs = processamentoService.recuperarLogs();
		String analise = analiseLogsService.analisarLogs(logs, metrica, timeParam, tipo);
		return Response.status(200).entity(analise).build();
	}
	

}
