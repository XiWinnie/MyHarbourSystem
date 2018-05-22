package com.myharbour.controller;

import com.myharbour.pojo.Container;
import com.myharbour.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
@RequestMapping("/api/operator")
public class OperatorController {

    @Autowired
    private QueryService queryService = null;

    @RequestMapping("/get/count")
    @ResponseBody
    public Integer getCount(@RequestParam(name = "size", required = false) Integer containerSize,
                            @RequestParam(name = "type", required = false) Integer containerType,
                            @RequestParam(name = "area", required = false) Integer containerArea) {
        try {
            return queryService.getCountBySpecificParas(containerSize, containerType, containerArea);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping("/get/containers")
    public ModelAndView getContainers(@RequestParam(name = "size", required = false) Integer containerSize,
                                      @RequestParam(name = "type", required = false) Integer containerType,
                                      @RequestParam(name = "area", required = false) Integer containerArea,
                                      @RequestParam(name = "page", required = true) Integer page, ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView();
     //   if (page == null || page < 1) return modelAndView;
        try {
            List<Container> list = queryService.getContainersBySpecificParas(containerSize, containerType, containerArea, page);
            modelMap.addAttribute("containers",list);
            modelAndView.setView(new MappingJackson2JsonView());
            return modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
            return modelAndView;
        }
    }

    @RequestMapping("/get/cargos")
    public ModelAndView getCargo(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            //todo
            modelAndView.setView(new MappingJackson2JsonView());
            return modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
            return modelAndView;
        }
    }

    @RequestMapping("/export/container")
    public boolean exportContainer() {
        try {
            //todo
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/import/empty-container")
    public boolean importEmptyContainer(@RequestParam("id") Integer id,
                                        @RequestParam("row") Integer row,
                                        @RequestParam("column") Integer cloumn,
                                        @RequestParam("layer") Integer layer) {
        try {
            if (id == null || row == null || cloumn == null || layer == null) return false;
            //todo
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/import/container-with-cargo")
    public boolean importContainerWithCargo(@RequestParam("id") Integer id,
                                            @RequestParam("row") Integer row,
                                            @RequestParam("column") Integer cloumn,
                                            @RequestParam("layer") Integer layer) {
        try {
            if (id == null || row == null || cloumn == null || layer == null) return false;
            //todo
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/import/cargo-into-repo-empty-container")
    public boolean importCargoIntoRepo() {
        try {
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/import/cargo-into-repo-half-large-container")
    public boolean importCargoIntoRepoHalfLargeContainer(@RequestParam("cargoid") Integer cargoId,
                                                         @RequestParam("row") Integer row,
                                                         @RequestParam("column") Integer column,
                                                         @RequestParam("layer") Integer layer) {
        if (cargoId == null || row == null || column == null || layer == null) return false;
        try {
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/get/insertable-empty-containers")
    public ModelAndView getInsertableEmptyContainers() {
        ModelAndView modelAndView = new ModelAndView();
        //todo
        try {
            modelAndView.setView(new MappingJackson2JsonView());
            return modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
            return modelAndView;
        }
    }

    @RequestMapping("/get/insertable-half-large-containers")
    public ModelAndView getInsertableHalfLargeContainers() {
        ModelAndView modelAndView = new ModelAndView();
        //todo
        try {
            modelAndView.setView(new MappingJackson2JsonView());
            return modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
            return modelAndView;
        }
    }

    @RequestMapping("/get/insertable-area")
    public ModelAndView getInsertableArea() {
        ModelAndView modelAndView = new ModelAndView();
        //todo
        try {
            modelAndView.setView(new MappingJackson2JsonView());
            return modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
            return modelAndView;
        }
    }
}
