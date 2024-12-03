package trs.souvenir.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import trs.souvenir.data.Product;
import trs.souvenir.data.Type;
import trs.souvenir.services.ProductService;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@Controller
public class MainController {

    ProductService productService;


    // Відображення сторінки зі списком усіх товарів
    @GetMapping("/products")
    public String showListOfProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        List<Type> types = productService.getAllTypes();
        model.addAttribute("products", products);
        model.addAttribute("types", types);
        return "products";
    }

    // Обробка запиту для оновлення даних про товар
    @PostMapping("/product/update")
    public String updateProduct(@RequestParam("productId")int productId, @RequestParam("name") String name,
                                @RequestParam("price") double price, @RequestParam("quantity") int quantity,
                                @RequestParam("size") String size,
                                @RequestParam("description") String description,
                                @RequestParam("lifespan") int lifespan,
                                @RequestParam("origin") String origin) {
        productService.updateProduct(productId, name, price, quantity, size, description, lifespan, origin);
        return "redirect:/products";
    }

    // Обробка запиту для видалення товару за ID
    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }

    // Обробка запиту для додавання нового товару
    @PostMapping("/product/add")
    public String addProduct(@RequestParam("nameN") String name, @RequestParam("priceN") double price,
                             @RequestParam("quantityN") int quantity, @RequestParam("sizeN") String size,
                             @RequestParam("descriptionN") String description, @RequestParam("lifespanN") int lifespan,
                             @RequestParam("originN") String origin,
                             @RequestParam("typeIdN") int typeId) {
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        productService.addProduct(name, typeId, price, quantity, size, description, createdAt, lifespan, origin);
        return "redirect:/products";
    }
}
