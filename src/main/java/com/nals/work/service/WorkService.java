package com.nals.work.service;

import com.nals.work.entity.Status;
import com.nals.work.entity.Work;
import com.nals.work.model.PageRequestBase;
import com.nals.work.model.ResponseModel;
import com.nals.work.model.code.WorkErrorCode;
import com.nals.work.model.work.WorkModel;
import com.nals.work.model.work.WorkRequestModel;
import com.nals.work.repository.StatusRepository;
import com.nals.work.repository.WorkRepository;
import com.nals.work.util.DateTimeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service handle work.
 *
 * @author HanhLe
 */
@Service
public class WorkService {

    @Autowired
    private WorkRepository workRepository;
    @Autowired
    private StatusRepository statusRepository;

    /**
     * Get works
     *
     * @param pageRequestBase the PageRequestBase
     * @return ResponseModel
     */
    public ResponseModel getWorks(PageRequestBase pageRequestBase) {
        return new ResponseModel(Boolean.TRUE, null, workRepository.findAll().stream().map(work -> convertToWorkModel(work)).collect(Collectors.toList()));
    }

    /**
     * Create work.
     *
     * @param workModel the information of Work.
     * @return ResponseModel
     */
    public ResponseModel createWork(WorkRequestModel workModel) {
        Work work = workRepository.save(convertToWork(new Work(), workModel));
        return new ResponseModel(Boolean.TRUE, null, work);
    }

    /**
     * Update work.
     *
     * @param workModel the information of Work.
     * @return ResponseModel
     */
    public ResponseModel updateWork(WorkRequestModel workModel) {
        if (Objects.isNull(workModel.getId())) {
            return new ResponseModel(Boolean.FALSE, WorkErrorCode.ERROR_CODE_2001.getDescription(), null);
        }

        Optional<Work> workOptional = workRepository.findById(workModel.getId());
        if (!workOptional.isPresent()) {
            return new ResponseModel(Boolean.FALSE, WorkErrorCode.ERROR_CODE_2002.getDescription(), null);
        }
        Work work = convertToWork(workOptional.get(), workModel);
        workRepository.save(work);
        return new ResponseModel(Boolean.TRUE, null, convertToWorkModel(work));
    }

    /**
     * Delete work.
     *
     * @param workId the id of Work.
     * @return ResponseModel
     */
    public ResponseModel deleteWork(Long workId) {
        Optional<Work> workOptional = workRepository.findById(workId);
        if (!workOptional.isPresent()) {
            return new ResponseModel(Boolean.FALSE, WorkErrorCode.ERROR_CODE_2002.getDescription(), null);
        }
        workRepository.deleteById(workId);
        return new ResponseModel(Boolean.TRUE, null, null);
    }

    /**
     * Convert work model to entity
     *
     * @param work      the Work
     * @param workModel the WorkRequestModel
     * @return Work
     */
    private Work convertToWork(Work work, WorkRequestModel workModel) {
        work.setWorkName(workModel.getWorkName());
        work.setStartDate(DateTimeConverter.parseStringToDate(workModel.getStartDate(), DateTimeConverter.DEFAULT_DATE_FORMAT));
        work.setEndDate(DateTimeConverter.parseStringToDate(workModel.getEndDate(), DateTimeConverter.DEFAULT_DATE_FORMAT));
        work.setStatus(statusRepository.getById(workModel.getStatus()));
        return work;
    }

    /**
     * Convert entity of work to model
     *
     * @param work the entity of work
     * @return WorkModel
     */
    private WorkModel convertToWorkModel(Work work) {
        WorkModel workModel = new WorkModel();
        workModel.setId(work.getId());
        workModel.setWorkName(work.getWorkName());
        workModel.setStartDate(DateTimeConverter.parseDateToString(work.getStartDate(), DateTimeConverter.DEFAULT_DATE_FORMAT));
        workModel.setEndDate(DateTimeConverter.parseDateToString(work.getEndDate(), DateTimeConverter.DEFAULT_DATE_FORMAT));
        workModel.setStatus(work.getStatus());
        return workModel;
    }

}
