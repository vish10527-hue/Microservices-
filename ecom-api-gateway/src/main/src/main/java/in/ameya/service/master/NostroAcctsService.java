package in.ameya.service.master;

import java.io.IOException;

import in.ameya.model.DBResponse;
import in.ameya.model.ExportResponse;
import in.ameya.model.master.NostroAcctsDetails;
import in.ameya.model.master.NostroAcctsSearch;

public interface NostroAcctsService {
	
	DBResponse getNostroAcctsList(NostroAcctsSearch nostroAcctsSearch);

	DBResponse addNostroAccts(NostroAcctsDetails nostroAccts, Integer loggedInKey);

	DBResponse updateNostroAccts(NostroAcctsDetails nostroAccts, Integer loggedInKey);

	DBResponse discardNostroAccts(NostroAcctsDetails nostroAccts, Integer loggedInKey);

	DBResponse verifyNostroAcctsRecord(NostroAcctsSearch nostroAcctsSearch, Integer loggedInKey);

	String exportNostroAcctsCSV(NostroAcctsSearch nostroAcctsSearch) throws IOException;

	ExportResponse exportNostroAcctsExcel(NostroAcctsSearch nostroAcctsSearch) throws IOException;

	NostroAcctsDetails getNostroAcctsDetailsById(Integer nostroAcctsKey, Integer userId);

}
