package tn.esprit.devops_project.services;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.ProductCategory;
import tn.esprit.devops_project.entities.Stock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")
class ProductServiceImplTest {

    @Autowired
    private ProductServiceImpl productService;


   @Test
    @DatabaseSetup({"/data-set/product-data.xml", "/data-set/stock-data.xml"})
   @Order(1)
    void addProduct() {
        final Product product = new Product();
        product.setTitle("Product 2");
        product.setCategory(ProductCategory.CLOTHING);
        product.setPrice(20f);
        product.setQuantity(30);
        this.productService.addProduct(product,1L);
        assertEquals(this.productService.retreiveAllProduct().size(),2);
        assertEquals(this.productService.retrieveProduct(product.getIdProduct()).getTitle(),"Product 2");
    }

    @Test
    @DatabaseSetup("/data-set/product-data.xml")
    void retreiveAllProduct() {
        assertEquals(this.productService.retreiveAllProduct().size(),1);

    }
    @Test
    @DatabaseSetup("/data-set/product-data.xml")
    void retreiveProduct() {
        final Product product = this.productService.retrieveProduct(1L);
        assertEquals(product.getTitle(), "product 1");

    }

    @Test
    @DatabaseSetup("/data-set/product-data.xml")
    void retrieveProductByCategory() {
        final Product product = this.productService.retrieveProductByCategory(ProductCategory.CLOTHING).get(0);
        assertEquals(product.getTitle(), "product 1");
    }

    @Test
    @DatabaseSetup("/data-set/product-data.xml")
    void deleteProduct() {
        this.productService.deleteProduct(1L);
        assertEquals(this.productService.retreiveAllProduct().size(), 0);
    }

    @Test
    @DatabaseSetup({"/data-set/product-data.xml", "/data-set/stock-data.xml"})
    @Order(2)

    void retreiveProductStock() {
        final Product product = new Product();
        product.setTitle("Product 2");
        product.setCategory(ProductCategory.CLOTHING);
        product.setPrice(20f);
        product.setQuantity(30);
        this.productService.addProduct(product,1L);
        List<Product> productsInStock=  this.productService.retreiveProductStock(1l);
        assertEquals(productsInStock.size(), 1);

    }
}