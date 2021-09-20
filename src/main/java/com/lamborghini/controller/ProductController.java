package com.lamborghini.controller;

import com.lamborghini.model.Product;
import com.lamborghini.model.ProductForm;
import com.lamborghini.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/product")
@PropertySource("classpath:upload-file.properties")
public class ProductController {
    @Value("${file-upload}")
    private String fileUpload;

    @Autowired
    private IProductService productService;

    @GetMapping("")
    public ModelAndView showAllList() {
        ModelAndView modelAndView = new ModelAndView("/index");
        modelAndView.addObject("products", productService.findAll());
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/create");
        modelAndView.addObject("productForm", new ProductForm());
        return modelAndView;
    }

    @PostMapping("/product")
    public String createProduct(@ModelAttribute("productForm") Product product) {
        productService.save(product);
        return "redirect:/product";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute ProductForm productForm) {
        //B1: Lưu file vào vùng nhớ
        MultipartFile multipartFile = productForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(productForm.getImage().getBytes(), new File(fileUpload + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //B2: Tạo mới đối tượng từ ProductForm
        Product product = new Product(productForm.getId(), productForm.getName(), productForm.getPrice(),
                productForm.getDescription(), fileName);
        //B3: Save lại Product
        productService.save(product);
        return "redirect:/product";
    }

    @GetMapping("/{id}/edit")
    public ModelAndView showFormEdit(@PathVariable Long id) {
        Product product = productService.findById(id);
        ModelAndView modelAndView = new ModelAndView("/edit");
        modelAndView.addObject("editForm", product);
        return modelAndView;
    }

    @PostMapping("/edit")
    public  String createProduct(@ModelAttribute("editForm") Product product, MultipartFile newImage, RedirectAttributes redirect){
        String newImageName = newImage.getOriginalFilename();
        try {
            FileCopyUtils.copy(newImage.getBytes(), new File(fileUpload + newImageName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(newImageName != ""){
            product.setImage(newImageName);
        }
        productService.save(product);
        redirect.addFlashAttribute("message", "Edit product successfully");
        return "redirect:/product";
    }

    @GetMapping("/{id}/detail")
    public ModelAndView detailCustomer(@PathVariable Long id) {
        Product product = productService.findById(id);
        ModelAndView modelAndView = new ModelAndView("/detail");
        modelAndView.addObject("detailProduct", product);
        return modelAndView;
    }

    @GetMapping("/{id}/delete")
    public ModelAndView showFormDelete(@PathVariable Long id) {
        Product product = productService.findById(id);
        ModelAndView modelAndView = new ModelAndView("/delete");
        modelAndView.addObject("deleteForm", product);
        return modelAndView;
    }

    @PostMapping("/delete")
    public String deleteCustomer(@ModelAttribute("deleteForm") Product product) {
        productService.remove(product.getId());
        return "redirect:/product";
    }
}
