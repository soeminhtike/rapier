package me.tdm.logic;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.tdm.dao.EntityService;
import me.tdm.entity.DataEntry;

@Service
@Transactional(readOnly = true)
public class DataEntryService {

	@Autowired
	private EntityService entityService;

	@Transactional(readOnly = false)
	public void save(DataEntry dataEntry) {
		entityService.save(dataEntry);
	}

	@Transactional(readOnly = true)
	public DataEntry create(File file) {
		DataEntry dataEntry = new DataEntry();
		dataEntry.setInternalName(System.nanoTime() + "");
		dataEntry.setLocation(file.getAbsolutePath());
		return dataEntry;
	}
	
	public List<DataEntry> getAll() {
		return entityService.findByString("from DataEntry d ", null, DataEntry.class);
	}

	@Transactional(readOnly = false)
	public DataEntry findByName(String name) {
		String query = "from DataEntry de where de.internalName=:dataInput";
		List<DataEntry> dataEntryList = entityService.findByString(query, name, DataEntry.class);
		return dataEntryList.get(0);
	}
}
