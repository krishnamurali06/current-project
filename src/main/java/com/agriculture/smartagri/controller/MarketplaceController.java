package com.agriculture.smartagri.controller;

import com.agriculture.smartagri.entity.Produce;
import com.agriculture.smartagri.service.ProduceService;
import com.agriculture.smartagri.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/marketplace")
public class MarketplaceController {

    @Autowired
    private ProduceService produceService;

    @Autowired
    private UserService userService;

    @GetMapping("/add")
    public String showProduceForm(Model model) {
        model.addAttribute("produce", new Produce());
        model.addAttribute("farmers", userService.getUsersByRole("FARMER"));
        return "farmer/marketplace";
    }

    @PostMapping
    public String saveProduce(@ModelAttribute Produce produce) {
        produceService.saveProduce(produce);
        return "redirect:/marketplace/add";
    }

    @GetMapping("/list")
    public String viewMarketplace(Model model) {
        model.addAttribute("produceList", produceService.getAllProduce());
        return "shared/marketplace-list";
    }
}
