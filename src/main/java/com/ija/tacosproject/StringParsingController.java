package com.ija.tacosproject;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;


/*
 * This Spring REST controller class was written for Challenge 2 
 * The methods in this class invoke the methods in the
 *  SpringParsingService class.
 */

@RestController
public class StringParsingController {
	
	 private final StringParsingService stringParsingService;

	 StringParsingController(StringParsingService stringParsingService) {
	    this.stringParsingService = stringParsingService;
	 }

	 
	 @PostMapping("api/stringparsing/fromInputText")  
	 public ResponseEntity<String> generateJsonBlockFromInputText(@RequestBody String inputText){  
		 // This is a POST method where:
		 // using request body text (inputText argument), return a Json string with eligible words ordered by length,
		 // along with most used word and its frequency.
		 // This method invokes generateJsonBlockFromInputText() from StringParsingService
		 // Sample usage with CURL:  curl -d @textSample.txt -H "Content-Type: text/html; charset=UTF-8" http://localhost:8080/api/stringparsing/fromInputText
		 // (where @textSample.tx is the Request Body (representation of the text string) 
		 HttpHeaders headers = new HttpHeaders();
		 String retStr = stringParsingService.generateJsonBlockFromInputText(inputText);
		 headers.setContentType(new MediaType("text", "html"));
		 return new ResponseEntity<String>(retStr + "\nFinished generateJsonBlockFromInputText()", headers, HttpStatus.OK);
	  }
		 
	 @PostMapping("/api/stringparsing/fromFile")
	 public ResponseEntity<String> generateJsonFileFromParsedTextFile(@RequestParam("filePath") String filePath ) {
		 // This is a POST method where:
		 // (1) using file path (filePath argument), if not null we write to  textSampleResults.json file in the SAME folder as 
		 // the input file.  This calls generateJsonFileFromParsedTextFile() in StringParsingController.
		 // Sample usage with CURL:  curl -d '' "http://localhost:8080/api/stringparsing/fromFile?filePath=C://IanDocs2020//textSample.txt"
		 // 
		 // If the file path is null then we take  textSample.txt from PWVTacos app folder
		 // and write to textSampleResults.json in the SAME folder.  This calls 
		 // generateJsonFileFromParsedTextFileInApp() in StringParsingService.
		 // Sample usage with CURL:  curl -d '' http://localhost:8080/api/stringparsing/fromFile?filePath=
		 
		 // The resulting Json file contains a Json string with eligible words ordered by length,
		 // along with most used word and its frequency.
		 String retStr;
		 String methodCalledStr;
		 HttpHeaders headers = new HttpHeaders();
		 if (filePath == null || filePath.trim().length() == 0) {
		    retStr = stringParsingService.generateJsonFileFromParsedTextFileInApp();
		    methodCalledStr = "generateJsonFileFromParsedTextFileInApp()";
		 }
		 else {
			retStr = stringParsingService.generateJsonFileFromParsedTextFile(filePath); 
			methodCalledStr = "generateJsonFileFromParsedTextFile()";
		 }
		 headers.setContentType(new MediaType("text", "html"));
		 return new ResponseEntity<String>(retStr + "\nFinished " + methodCalledStr, headers, HttpStatus.OK);
	  }

}
