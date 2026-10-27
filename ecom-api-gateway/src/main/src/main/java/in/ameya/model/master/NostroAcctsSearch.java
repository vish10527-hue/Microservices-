package in.ameya.model.master;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import in.ameya.model.Search;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NostroAcctsSearch extends Search {

	private String nostroAcctName;
	private String nostroAcctNo;

	public List<NostroAcctsDetails> nostroAccts;

	@Override
	public String toString() {
		return "NostroAcctsSearch [nostroAcctName=" + nostroAcctName + ", nostroAcctNo=" + nostroAcctNo + ", nostroAccts="
				+ nostroAccts + ", " + searchToString() + "]";
	}


	public List<NostroAcctsDetails> getNostroAccts() {
		return nostroAccts;
	}

	public void setNostroAccts(List<NostroAcctsDetails> nostroAccts) {
		this.nostroAccts = nostroAccts;
	}



	public String getNostroAcctName() {
		return nostroAcctName;
	}



	public void setNostroAcctName(String nostroAcctName) {
		this.nostroAcctName = nostroAcctName;
	}



	public String getNostroAcctNo() {
		return nostroAcctNo;
	}


	public void setNostroAcctNo(String nostroAcctNo) {
		this.nostroAcctNo = nostroAcctNo;
	}

}