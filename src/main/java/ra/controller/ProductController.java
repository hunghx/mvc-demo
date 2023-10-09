package ra.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ra.model.Catalog;
import ra.model.Product;
import ra.service.ICatalogService;
import ra.service.IProductService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping("/admin/product")
public class ProductController {
    private static final Gson GSON = new GsonBuilder().create();
    @Autowired
    private ICatalogService catalogService;
    @Autowired
    private IProductService productService;
    @GetMapping
    public String product(Model model){
        List<Product> listPro = productService.getAll();
        List<Catalog> listCat = catalogService.getAll();
        model.addAttribute("listPro",listPro);
        model.addAttribute("listCat",listCat);
        model.addAttribute("pro",new Product());
        return "admin/product";
    }
    @PostMapping("/add")
    public  String doAdd(@ModelAttribute("pro") Product pro, @RequestParam("file")MultipartFile file,@RequestParam String catalogId){
        Catalog c = catalogService.findById(Long.valueOf(catalogId));
        pro.setCatalog(c);
        productService.save(pro,file);
        return "redirect:/admin/product";
    }
    @GetMapping(value = "/edit/{id}")
    public void edit(HttpServletResponse response, @PathVariable("id") Long id){
        Product pro = productService.findById(id);
        String data = GSON.toJson(pro);
        response.setHeader("Content-Type","application/json");
        response.setStatus(200);
        PrintWriter out=null;
        try {
            out = response.getWriter();
            out.write(data);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            out.close();
        }
    }
    @PostMapping("/update")
    public String doUpdate(@ModelAttribute Product product , @RequestParam MultipartFile file,@RequestParam  String catalogId){
        Catalog c = catalogService.findById(Long.valueOf(catalogId));
        product.setCatalog(c);
        productService.update(product,file);
        return "redirect:/admin/product";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        productService.deleteById(id);
        return "redirect:/admin/product";
    }
}
