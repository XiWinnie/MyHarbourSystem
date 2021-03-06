package com.myharbour.controller;

import com.myharbour.pojo.Cargo;
import com.myharbour.pojo.Container;
import com.myharbour.pojo.Position;
import com.myharbour.pojo.ResultantCargoInfo;
import com.myharbour.service.ExportService;
import com.myharbour.service.ImportService;
import com.myharbour.service.InsertablePositionService;
import com.myharbour.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@Controller
@RequestMapping("/api/operator")
public class OperatorController {

    @Autowired
    private QueryService queryService = null;

    @Autowired
    private ExportService exportService = null;

    @Autowired
    private ImportService importService = null;

    @RequestMapping("/get/containers-count")
    @ResponseBody
    public Integer getContainersCount(@RequestParam(name = "size", required = false) Integer containerSize,
                                      @RequestParam(name = "type", required = false) Integer containerType,
                                      @RequestParam(name = "area", required = false) Integer containerArea) {
        try {
            return queryService.getContainersCountBySpecificParas(containerSize, containerType, containerArea);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping("/get/cargos-count")
    @ResponseBody
    public Integer getCargosCount(@RequestParam(name = "type", required = false) Integer containerType) {
        try {
            return queryService.getCargosCountByBySpecificParas(null, containerType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping("/get/container-view")
    public ModelAndView getContainerView(@RequestParam("area") Integer area, ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView();
        if (!(area == Container.AREA_EMPTY
                || area == Container.AREA_ORDINARY
                || area == Container.AREA_FREEZE
                || area == Container.AREA_HAZARD)) return modelAndView;
        try {
            List<Container> list = queryService.getContainersBySpecificParas(null, null, area, null, 0);
            modelMap.addAttribute("containers", list);
            modelAndView.setView(new MappingJackson2JsonView());
            return modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
            return modelAndView;
        }
    }

    @RequestMapping("/get/containers")
    public ModelAndView getContainers(@RequestParam(name = "size", required = false) Integer containerSize,
                                      @RequestParam(name = "type", required = false) Integer containerType,
                                      @RequestParam(name = "area", required = false) Integer containerArea,
                                      @RequestParam(name = "page", required = true) Integer page, ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView();
        if (page < 1) return modelAndView;
        try {
            List<Container> list = queryService.getContainersBySpecificParas(containerSize, containerType, containerArea, null, page);
            modelMap.addAttribute("containers", list);
            modelAndView.setView(new MappingJackson2JsonView());
            return modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
            return modelAndView;
        }
    }

    @RequestMapping("/get/cargos-by-containerid")
    public ModelAndView getCargosByContainerId(@RequestParam("id") Integer id, ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView();
        if (id == null) return modelAndView;
        try {
            List<ResultantCargoInfo> list = queryService.getResultantCargoInfoBySpecificParas(id, null, null, 0);
            // List<Cargo> list = queryService.getCargosByContainerId(id);
            modelMap.addAttribute("cargos", list);
            modelAndView.setView(new MappingJackson2JsonView());
            return modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
            return modelAndView;
        }
    }

    @RequestMapping("/get/bulk-cargos")
    public ModelAndView getCargo(@RequestParam(name = "type", required = false) Integer containerType,
                                 @RequestParam(name = "page") Integer page,
                                 ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView();
        if (page == null) return modelAndView;
        if (page < 1) return modelAndView;
        if (containerType != null && !(containerType == 0 || containerType == 1 || containerType == 2))
            return modelAndView;
        try {
            List<ResultantCargoInfo> resultantCargoInfos = queryService.getResultantCargoInfoBySpecificParas(null, containerType, null, page);
            modelMap.addAttribute("cargos", resultantCargoInfos);
            modelAndView.setView(new MappingJackson2JsonView());
            return modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
            return modelAndView;
        }
    }

    @RequestMapping("/export/container")
    @ResponseBody
    public boolean exportAContainer(@RequestParam("id") Integer containerId) {
        if (containerId == null) return false;
        try {
            exportService.exportAContainer(containerId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/import/load-a-cargo-into-an-empty-container")
    @ResponseBody
    public boolean loadACargoIntoAnEmptyContainer(@RequestParam("cargoId") Integer cargoId,
                                                  @RequestParam("containerId") Integer containerId,
                                                  @RequestParam("row") Integer row,
                                                  @RequestParam("column") Integer cloumn,
                                                  @RequestParam("layer") Integer layer,
                                                  @RequestParam("area") Integer area) {
        if (cargoId == null || containerId == null || row == null || cloumn == null || layer == null || area == null)
            return false;
        Position position = new Position();
        position.setArea(area);
        position.setRow(row);
        position.setLayer(layer);
        position.setColumn(cloumn);
        try {
            importService.loadACargoIntoAnEmptyContainer(cargoId, containerId, position);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/import/container")
    @ResponseBody
    public boolean importContainer(@RequestParam("id") Integer id,
                                   @RequestParam("row") Integer row,
                                   @RequestParam("column") Integer cloumn,
                                   @RequestParam("layer") Integer layer,
                                   @RequestParam("area") Integer area) {
        try {
            if (id == null || row == null || cloumn == null || layer == null || area == null) return false;
            Position position = new Position();
            position.setRow(row);
            position.setColumn(cloumn);
            position.setLayer(layer);
            position.setArea(area);
            importService.importAContainer(id, position);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping("/move/container")
    @ResponseBody
    public boolean moveContainer(@RequestParam("id") Integer id,
                                   @RequestParam("row") Integer row,
                                   @RequestParam("column") Integer cloumn,
                                   @RequestParam("layer") Integer layer) {
        try {
            if (id == null || row == null || cloumn == null || layer == null) return false;
            Position position = new Position();
            position.setRow(row);
            position.setColumn(cloumn);
            position.setLayer(layer);
            importService.movePosition(id,position);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

//    @RequestMapping("/import/container-with-cargo")
//    public boolean importContainerWithCargo(@RequestParam("id") Integer id,
//                                            @RequestParam("row") Integer row,
//                                            @RequestParam("column") Integer cloumn,
//                                            @RequestParam("layer") Integer layer) {
//        try {
//            if (id == null || row == null || cloumn == null || layer == null) return false;
//            //todo
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

//    @RequestMapping("/import/cargo-into-repo-empty-container")
//    public boolean importCargoIntoRepo() {
//        try {
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

//    @RequestMapping("/import/cargo-into-repo-half-large-container")
//    public boolean importCargoIntoRepoHalfLargeContainer(@RequestParam("cargoid") Integer cargoId,
//                                                         @RequestParam("row") Integer row,
//                                                         @RequestParam("column") Integer column,
//                                                         @RequestParam("layer") Integer layer) {
//        if (cargoId == null || row == null || column == null || layer == null) return false;
//        try {
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

//    @RequestMapping("/get/insertable-empty-containers")
//    public ModelAndView getInsertableEmptyContainers() {
//        ModelAndView modelAndView = new ModelAndView();
//        //todo
//        try {
//            modelAndView.setView(new MappingJackson2JsonView());
//            return modelAndView;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return modelAndView;
//        }
//    }
//
//    @RequestMapping("/get/insertable-half-large-containers")
//    public ModelAndView getInsertableHalfLargeContainers() {
//        ModelAndView modelAndView = new ModelAndView();
//        //todo
//        try {
//            modelAndView.setView(new MappingJackson2JsonView());
//            return modelAndView;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return modelAndView;
//        }
//    }

    @RequestMapping("get/loadable-empty-containers")
    public ModelAndView getLoadableEmptyContainers(@RequestParam("id") Integer cargoId, ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView();
        if (cargoId == null) return modelAndView;
        try {
            List<Container> containers = queryService.getLoadableEmptyContainers(cargoId);
            modelMap.addAttribute("containers", containers);
            modelAndView.setView(new MappingJackson2JsonView());
            return modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
            return modelAndView;
        }
    }

    @RequestMapping("/get/import-insertable-area")
    public ModelAndView getImportInsertableArea(@RequestParam("id") Integer id, ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView();
        if (id == null) return modelAndView;
        try {
            List<Position> positionList =
                    queryService.getInsertablePosition(id, InsertablePositionService.OP_IMPORT);
            modelMap.addAttribute("postition", positionList);
            modelAndView.setView(new MappingJackson2JsonView());
            return modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
            return modelAndView;
        }
    }

    @RequestMapping("/get/load-insertable-area")
    public ModelAndView getLoadInsertableArea(@RequestParam("id") Integer id, ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView();
        if (id == null) return modelAndView;
        try {
            List<Position> positionList =
                    queryService.getInsertablePosition(id, InsertablePositionService.OP_LOAD);
            modelMap.addAttribute("postition", positionList);
            modelAndView.setView(new MappingJackson2JsonView());
            return modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
            return modelAndView;
        }
    }

    @RequestMapping("/get/move-insertable-area")
    public ModelAndView getMoveInsertableArea(@RequestParam("id") Integer id, ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView();
        if (id == null) return modelAndView;
        try {
            List<Position> positionList =
                    queryService.getInsertablePosition(id, InsertablePositionService.OP_MOVE);
            modelMap.addAttribute("postition", positionList);
            modelAndView.setView(new MappingJackson2JsonView());
            return modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
            return modelAndView;
        }
    }
}
