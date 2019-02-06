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
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO seller\r\n" + 
					"(Name, Email, BirthDate, BaseSalary, DepartmentId)\r\n" + 
					"VALUES\r\n" + 
					"(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rSet = st.getGeneratedKeys();
				if (rSet.next()) {
					int id = rSet.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rSet);
			}
			else {
				throw new DbException("Unexpected error! No rows affected.");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE seller\r\n" + 
					"SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ?\r\n" + 
					"WHERE Id = ?");
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM seller WHERE Id = ?");
			
			st.setInt(1, id);
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
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
		PreparedStatement statement = null;
		ResultSet rSet = null;
		try {
			statement = conn.prepareStatement(
							"SELECT seller.*,department.Name as DepName\r\n" + 
							"FROM seller INNER JOIN department\r\n" + 
							"ON seller.DepartmentId = department.Id\r\n" + 
							"ORDER BY Name");
			
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

}
