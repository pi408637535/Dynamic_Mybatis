package aboudou.ibatis.app;

import aboudou.ibatis.model.Employee;
import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;


public class IbatisInsert {

	static Logger logger = LoggerFactory.getLogger(IbatisInsert.class);
	
	public static void main(String[] args) throws IOException, SQLException {
		Reader reader = Resources.getResourceAsReader("SqlMapConfig.xml");
		SqlMapClient sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(reader);
		
		logger.info("Going to insert one record");
		Employee employee = new Employee("DOE", "John", 8000);
		sqlMapClient.insert("Employee.insert", employee);
		logger.info("Record Inserted Successfully!");
	}
}
