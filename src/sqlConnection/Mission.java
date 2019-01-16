package sqlConnection;

import java.sql.SQLException;

public class Mission {
	public int mission_id;
	public String description;
	
	protected int getMission_id() {
		return mission_id;
	}
	protected void setMission_id(int mission_id) {
		this.mission_id = mission_id;
	}
	protected String getDescription() {
		return description;
	}
	protected void setDescription(String description) {
		this.description = description;
	}
	
	// Konstruktor
	protected Mission(int mission_id) throws SQLException {		
		this.mission_id = mission_id;
		this.description = SqlHelper.getMissionDescription(mission_id);
	}
	
	@Override
	public String toString() {
		return "Mission: " + mission_id + "\n Description:" + description ;
	}
	
	
	
	
}
