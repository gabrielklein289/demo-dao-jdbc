package application;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		
		DepartmentDao dao = DaoFactory.createDepartmentDao();
		Department department = new Department(25, "vigia");
		dao.insert(department);
		
		
		
	}

}
