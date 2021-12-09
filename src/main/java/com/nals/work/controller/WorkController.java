package com.nals.work.controller;

import com.nals.work.model.PageRequestBase;
import com.nals.work.model.ResponseModel;
import com.nals.work.model.work.WorkRequestModel;
import com.nals.work.service.WorkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest controller for manager Work.
 *
 * @author HanhLe
 */
@RestController
@RequestMapping("/NALS/work")
@Slf4j
public class WorkController {

    @Autowired
    private WorkService workService;

    /**
     * Api get works
     *
     * @return ResponseEntity<ResponseModel>
     */
    @PostMapping("/findWorks")
    public ResponseEntity<ResponseModel> getWorks(@RequestBody PageRequestBase pageRequestBase) {
        return ResponseEntity.ok(workService.getWorks(pageRequestBase));
    }

    /**
     * Api create WorkModel
     *
     * @param workModel the WorkModel to create
     * @return ResponseEntity<ResponseModel>
     */
    @PostMapping
    public ResponseEntity<ResponseModel> createWork(@RequestBody WorkRequestModel workModel) {
        return ResponseEntity.ok(workService.createWork(workModel));
    }

    /**
     * Api update WorkModel
     *
     * @param workModel the WorkModel to update
     * @return ResponseEntity<ResponseModel>
     */
    @PutMapping
    public ResponseEntity<ResponseModel> updateWork(@RequestBody WorkRequestModel workModel) {
        return ResponseEntity.ok(workService.updateWork(workModel));
    }

    /**
     * Api delete WorkModel
     *
     * @param workId the id of WorkModel
     * @return ResponseEntity<ResponseModel>
     */
    @DeleteMapping("/{workId}")
    public ResponseEntity<ResponseModel> deleteWork(@PathVariable Long workId) {
        return ResponseEntity.ok(workService.deleteWork(workId));
    }
}
