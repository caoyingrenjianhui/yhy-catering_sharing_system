package com.example.controller;


import com.example.domain.Dish;
import com.example.service.IDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 尹洪运
 * @since 2024-02-29
 */
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private IDishService dishService;

    @PostMapping("/add")
    public Result add(@RequestBody Dish dish){
        return dishService.add(dish);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Dish dish){
        return dishService.update(dish);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        return dishService.delete(id);
    }

    @GetMapping("/getAll")
    public Result getAll() {
        return dishService.getAll();
    }

    @GetMapping("/getByMerchant/{id}")
    public Result getByMerchant(@PathVariable Integer id) {
        return dishService.getByMerchant(id);
    }
}

