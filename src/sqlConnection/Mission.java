package sqlConnection;

import java.sql.SQLException;

public class Mission {
	public int missionId;
	public String description;
	
	protected int getMission_id() {
		return missionId;
	}
	protected void setMission_id(int missionId) {
		this.missionId = missionId;
	}
	protected String getDescription() {
		return description;
	}
	protected void setDescription(String description) {
		this.description = description;
	}
	
	// Konstruktor
	protected Mission(int missionId) throws SQLException {		
		this.missionId = missionId;
		this.description = SqlHelper.getMissionDescription(missionId);
	}
	
	@Override
	public String toString() {
		return "Mission: " + missionId + "\n Description:" + description ;
	}
		
}
