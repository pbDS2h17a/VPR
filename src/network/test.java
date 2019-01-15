package network;

import java.sql.SQLException;

import sqlConnection.*;

public class test
{

	public static void main(String[] args) throws SQLException, ClassNotFoundException
	{
		SqlHelper.setStatement(SqlHelper.loginStringArray);
		System.out.println(SqlHelper.getLobbies());

	}

}
