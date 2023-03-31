package me.bvn13.test.spring.data.processing;

import me.bvn13.test.spring.data.processing.model.Product;
import me.bvn13.test.spring.data.processing.model.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class SpringDataResultSetProcessingApplicationTests {

	@Autowired
	ProductRepository productRepository;
	@Autowired
	JdbcTemplate jdbc;
	@Autowired
	NamedParameterJdbcTemplate namedJdbc;

	@Test
	void testResultSet1() {

		productsExist();

		List<Product> allProducts = jdbc.query("select * from product",
				(ResultSet rse) -> {
					List<Product> products = new ArrayList<>();
					while(rse.next()) {
						products.add(map(rse));
					}
					return products;
				});

		Assertions.assertEquals(2, allProducts.size());

	}

	@Test
	void testResultSet2() {

		productsExist();

		List<Product> allProducts = namedJdbc.query("select * from product where id > :id",
				new MapSqlParameterSource().addValue("id", 0),
				(ResultSet rse) -> {
					List<Product> products = new ArrayList<>();
					while(rse.next()) {
						products.add(map(rse));
					}
//					do {
//						products.add(map(rse));
//					} while(rse.next());
					return products;
				});

		Assertions.assertEquals(2, allProducts.size());

	}

	private void productsExist() {
		productRepository.save(new Product(1, "product 1"));
		productRepository.save(new Product(2, "product 2"));
	}

	private Product map(ResultSet rse) throws SQLException {
		return new Product(rse.getInt("id"), rse.getString("name"));
	}

}
