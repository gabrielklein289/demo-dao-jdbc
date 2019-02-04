package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection conn;
	
	public SellerDaoJDBC(Connection connection) {
		this.conn = connection;
	}
	
	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement statement = null;
		ResultSet rSet = null;
		try {
			statement = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName\r\n" + 
					"FROM seller INNER JOIN department\r\n" + 
					"ON seller.DepartmentId = department.Id\r\n" + 
					"WHERE seller.Id = ?");
			
			statement.setInt(1, id);
			rSet = statement.executeQuery();
			if (rSet.next()) {
				Department dep = new Department();
				dep.setId(rSet.getInt("DepartmentId"));
				dep.setName(rSet.getString("DepName"));
				
				Seller obj = new Seller();
				obj.setId(rSet.getInt("Id"));
				obj.setName(rSet.getString("Name"));
				obj.setEmail(rSet.getString("Email"));
				obj.setBaseSalary(rSet.getDouble("BaseSalary"));
				obj.setBirthDate(rSet.getDate("BirthDate"));
				obj.setDepartment(dep);
				return obj;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(statement);
			DB.closeResultSet(rSet);
		}
	}

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
