package com.ija.tacosproject;

import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;

import java.io.IOException;
import java.io.File;
import java.util.*;

/*
 * This service class was written for Challenge 1 and simulates the logic in models/Taco.js of the tacos-master
 * NodeJS app
 * The public methods in this service class are invoked by the methods in the
 *  TacoModelController REST controller class.
 */

@Service
public class TacoModelService {
	
	// db.json is stored under the PWVTacos directory and was copied from the tacos-master aapp
	public final static String JSONFILE = "db.json";  
    TacoModelBean tacoModelBean;
    ObjectMapper jsonMapper;

	
	@PostConstruct
    public void init() throws IOException {
		refreshTacoModel();
    }
	
	private void refreshTacoModel() {
		// puts the tacoModelBean in sync with what is currenty in db.json
		try {
			File jsonFile = new File(JSONFILE);
			//System.out.println("File path: " + jsonFile.getAbsolutePath());
			jsonMapper = new ObjectMapper();
			jsonMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			tacoModelBean = jsonMapper.readValue(jsonFile, new TypeReference<TacoModelBean>() {});
		} catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public String updateTaco(TacoJsonRecord inputTaco) {
		// simulates functionality of the updateTaco() method in Taco.js of taco-master.
		// It will replace the record in db.json with the inputTaco where the name attribute
		// of inputTaco matches that of the db.json record.
		refreshTacoModel();
		if (tacoModelBean != null) {
		   ListIterator<TacoJsonRecord> iterator = tacoModelBean.getTacoRecords().listIterator();
		   while (iterator.hasNext()) {
			   TacoJsonRecord tacoRecord = iterator.next();
			   if (tacoRecord.getName().equals(inputTaco.getName())) {
				   iterator.set(inputTaco);
				   writeRecordsToTacoDB();
				   break;
			   }
		   }
		}
		return "done";
	}
	
	public String removeTaco(String tacoName) {
		// simulates functionality of the removeTaco() method in Taco.js of taco-master.
		// It will delete the record in db.json the name attribute matches {name} (tacoName arg)
		// under /api/taco/{name}
		refreshTacoModel();
		if (tacoModelBean != null && tacoName != null && tacoName.length() > 0) {
		   ListIterator<TacoJsonRecord> iterator = tacoModelBean.getTacoRecords().listIterator();
		   while (iterator.hasNext()) {
			   TacoJsonRecord tacoRecord = iterator.next();
			   if (tacoRecord.getName().equals(tacoName)) {
				   iterator.remove();
				   writeRecordsToTacoDB();
				   break;
			   }
		   }
		}	
		return "done";
	}
	
	public TacoJsonRecord getTaco(String tacoName) {
		// simulates functionality of the getTaco() method in Taco.js of taco-master
		// it will retrieve the record in db.json where name attribute matches {name} (tacoName arg)
		// under /api/taco/{name}. If no record found then returns null
		refreshTacoModel();
		if (tacoModelBean != null && tacoName != null && tacoName.length() > 0) {
			for (TacoJsonRecord jsonRecord : tacoModelBean.getTacoRecords()) {
				if (jsonRecord.getName().equals(tacoName)) {
					return jsonRecord;
				}
			}
		}
		return null;
	}
	
    public List<TacoJsonRecord> getTacos() {
    	// simulates functionality of the getTacos() method in Taco.js of taco-master
    	// it will retrieve the taco records in db.json using the /api/tacos path.  If
    	// no records are in db.json then returns null
    	refreshTacoModel();
    	if (tacoModelBean != null) {
    	   return tacoModelBean.getTacoRecords();
    	}
    	return null;
    }
    
    private void writeRecordsToTacoDB() {
    	// utility method to write changes to the tacoModelBean to the db.json file.
    	// This method is invoked by updateTaco() and removeTaco()
        try {
            jsonMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            File jsonFile = new File(JSONFILE);
            jsonMapper.writeValue(jsonFile, tacoModelBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    	
    }
}
