package com.ija.tacosproject;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

/*
 * This Spring REST controller class was written for Challenge 1 and simulates the logic 
 * in routes/tacoApi.js of the tacos-master  NodeJS app
 * The methods in this class  invoke the methods in the TacoModelService class.
 * 
 * Please note that for each of the methods, I provided a CURL usage example test.
 * The results of these tests are IDENTICAL to those produced when doing these tests when
 * the taco-master NODEJs app is running which provides proof of equivalence in functionality.
 */

@RestController
public class TacoModelController {
	
	 private final TacoModelService tacoService;

	 TacoModelController(TacoModelService tacoService) {
	    this.tacoService= tacoService;
	  }
	 
	  @PutMapping("/api/taco/{name}")
	  ResponseEntity<String> updateTaco(@RequestBody TacoJsonRecord inputTaco)	{   
		 // This PUT method simulates  app.put('/api/taco/:name', (req, res) => in the routes/tacoApi.js
		 // of taco-master. It will replace the taco record in db.json corresponding to the same name as
		 // the taco record in the inputTaco argument of this method.
		 // It will  return as Response a 'done' message as well as a 'Finished updateTaco()' message.
		 // It invokes updateTaco() in the TacoModelService class (which returns a "done" string)
		  
		 // Sample usage with CURL:  curl -d @request.json -H "Content-Type: application/json"  -X PUT  http://localhost:8080/api/taco/chicken%20taco
		 // (where @request.json is the Request Body (representation of the Json record)
		 // NOTE: I included in this project a sample request.json file which has an altered record for
		 // name = 'chicken taco'.  If this CURL example is used as a test it'll alter the name='chicken.taco'
		 // record in db.json.
		 HttpHeaders headers = new HttpHeaders();
		 String retStr = tacoService.updateTaco(inputTaco);
		 headers.setContentType(new MediaType("text", "html"));
		 return new ResponseEntity<String>(retStr + "\nFinished updateTaco()", headers, HttpStatus.OK);
	  }
	  
	  @DeleteMapping("/api/taco/{name}")
	  ResponseEntity<String> removeTaco(@PathVariable String name) {
		 // This DELETE method simulates app.delete('/api/taco/:name', (req, res) => in the routes/tacoApi.js
		 // of taco-master. It will delete from db.json the record corresponding to :name (the arg of this method)
		 // It will  return as Response a 'done' message as well as a 'Finished removeTaco()' message.
		 // It invokes removeTaco() in the TacoModelService class (which returns a "done" string)
		  
		 // Sample usage with CURL:  curl -v http://localhost:8080/api/tacos		
		HttpHeaders headers = new HttpHeaders();
		String retStr = tacoService.removeTaco(name);
	    headers.setContentType(new MediaType("text", "html"));
		return new ResponseEntity<String>(retStr + "\nFinished removeTaco()", headers, HttpStatus.OK);
	  }
	 
	 @GetMapping("/api/taco/{name}")
     ResponseEntity<String> getTaco(@PathVariable String name) {
		 // This GET method simulates app.get('/api/tacos', (req, res)=> in the routes/tacoApi.js
		 // of taco-master.   It will return as Response the json representation of the 
		 // object in db.json corresponding to name same as :name (the arg of this metho)
		 // as well as a 'Finished getTacos()' message.
		 // It invokes getTaco() in the TacoModelService class
		 
		 // Sample usage with CURL:  curl -v http://localhost:8080/api/tacos
		 HttpHeaders headers = new HttpHeaders();
		 ObjectMapper mapper = new ObjectMapper();
		 TacoJsonRecord tacoRec = tacoService.getTaco(name);
		 String jsonString = "";
		 try {
		    jsonString = (tacoRec != null) ? mapper.writeValueAsString(tacoRec) : "not found";
		 } catch (JsonProcessingException e) {
			 e.printStackTrace();
		 }
		 headers.setContentType(new MediaType("text", "html"));
		 return new ResponseEntity<String>(jsonString +"\nFinished getTaco()", headers, HttpStatus.OK);
	  }
	 
	 @GetMapping("/api/tacos")
	 ResponseEntity<String> getTacos() {
		 // This GET method simulates app.get('/api/taco/:name', (req, res) => in the routes/tacoApi.js
		 // of taco-master.   It will return as Response the json representation of the 
		 // taco object in db.json with name same as :name as well as a 'Finished getTaco()' message.
		 //It invokes getTaco() in the TacoModelService class
		 
		 // Sample usage with CURL:  curl -v "http://localhost:8080/api/taco/chicken%20taco"
		 HttpHeaders headers = new HttpHeaders();
		 ObjectMapper mapper = new ObjectMapper();
		 List<TacoJsonRecord> tacoList = tacoService.getTacos();
		 String jsonString = "";
		 try {
		    jsonString = (tacoList != null) ? mapper.writeValueAsString(tacoList) : "not found";
		 } catch (JsonProcessingException e) {
			 e.printStackTrace();
		 }
		 headers.setContentType(new MediaType("text", "html"));
		 return new ResponseEntity<String>(jsonString + "\nFinished getTacos()", headers, HttpStatus.OK);
	  }

}
