package ra.service;

import org.springframework.web.multipart.MultipartFile;
import ra.model.Product;

public interface IProductService extends IGenericService<Product,Long> {
    void save(Product p, MultipartFile file);
    void update(Product p, MultipartFile file);
}
