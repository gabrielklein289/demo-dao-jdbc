package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
				Department dep = instantiateDepartment(rSet);
				Seller obj = instantiateSeller(rSet, dep);
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
	
	public List<Seller> findByDepartment(Department department){
		
		PreparedStatement statement = null;
		ResultSet rSet = null;
		try {
			statement = conn.prepareStatement(
							"SELECT seller.*,department.Name as DepName\r\n" + 
							"FROM seller INNER JOIN department\r\n" + 
							"ON seller.DepartmentId = department.Id\r\n" + 
							"WHERE DepartmentId = ? \r\n" + 
							"ORDER BY Name");
			
			statement.setInt(1, department.getId());
			
			rSet = statement.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			while (rSet.next()) {
				
				Department department2 = map.get(rSet.getInt("DepartmentId"));
				
				if (department2 == null) {
					department2 = instantiateDepartment(rSet);
					map.put(rSet.getInt("DepartmentId"), department2);
				}
				
				Seller obj = instantiateSeller(rSet, department2);
				list.add(obj);
				
				}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(statement);
			DB.closeResultSet(rSet);
		}
	}

	private Seller instantiateSeller(ResultSet rSet, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rSet.getInt("Id"));
		obj.setName(rSet.getString("Name"));
		obj.setEmail(rSet.getString("Email"));
		obj.setBaseSalary(rSet.getDouble("BaseSalary"));
		obj.setBirthDate(rSet.getDate("BirthDate"));
		obj.setDepartment(dep);
		return obj;
	}

	private Department instantiateDepartment(ResultSet rSet) throws SQLException {
		Department dep = new Department();
		dep.setId(rSet.getInt("DepartmentId"));
		dep.setName(rSet.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
