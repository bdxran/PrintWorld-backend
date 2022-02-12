package com.rbl.printworld.controllers;

import com.rbl.printworld.Application;
import com.rbl.printworld.services.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShutdownController {

	private final ToolService toolService;

	@Autowired
	public ShutdownController(ToolService toolService) {
		this.toolService = toolService;
	}

	@GetMapping("/shutdown")
	public void shutdown() {
		if (this.toolService.saveMetaCounter())
			Application.shutdown();
	}

	@GetMapping("/restart")
	public void restart() {
		if (this.toolService.saveMetaCounter())
			Application.restart();
	}
}
