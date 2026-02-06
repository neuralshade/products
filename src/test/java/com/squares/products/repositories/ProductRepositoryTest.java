package com.squares.products.repositories;

import com.squares.products.models.ProductModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.flyway.enabled=false"
})
class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @Test
    void savesAndFindsProductById() {
        ProductModel model = new ProductModel();
        model.setSku("SKU-001");
        model.setName("Caderno");
        model.setValue(12.5f);
        model.setStockQuantity(10);
        model.setMinStock(2);
        model.setCurrency("BRL");
        model.setActive(true);

        ProductModel saved = repository.save(model);

        Optional<ProductModel> found = repository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Caderno");
    }
}
