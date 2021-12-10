package com.nals.work.service;

import com.nals.work.entity.Status;
import com.nals.work.entity.Work;
import com.nals.work.model.PageRequestBase;
import com.nals.work.model.PageResult;
import com.nals.work.model.ResponseModel;
import com.nals.work.model.code.WorkErrorCode;
import com.nals.work.model.work.WorkModel;
import com.nals.work.model.work.WorkRequestModel;
import com.nals.work.repository.StatusRepository;
import com.nals.work.repository.WorkRepository;
import com.nals.work.util.DateTimeConverter;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
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

    private static final List<String> orderColumn = Arrays.asList("id", "workName", "startDate", "endDate", "status");

    @Autowired
    private WorkRepository workRepository;
    @Autowired
    private StatusRepository statusRepository;

    /**
     * Get works
     *
     * @param pageRequest the PageRequestBase
     * @return ResponseModel
     */
    public ResponseModel getWorks(PageRequestBase pageRequest) {

        WorkErrorCode workErrorCode = validatePageRequest(pageRequest);

        if (Objects.nonNull(workErrorCode)) {
            return new ResponseModel(Boolean.FALSE, workErrorCode.getDescription(), null);
        }

        Pageable pageable;
        if (Strings.isBlank(pageRequest.getSortColumn()) && Strings.isBlank(pageRequest.getSortOrder())) {
            pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), Sort.by("id").descending());

        } else {
            pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), Sort.by(Sort.Direction.fromString(pageRequest.getSortOrder()), pageRequest.getSortColumn()));
        }

        Page<Work> page = workRepository.findAll(pageable);

        PageResult pageResult = new PageResult<>(
                page.stream().map(work -> convertToWorkModel(work)).collect(Collectors.toList())
                , page.getTotalPages()
                , page.getTotalElements()
                , pageRequest.getPageNumber());

        return new ResponseModel(Boolean.TRUE, null, pageResult);
    }

    /**
     * Create work.
     *
     * @param workModel the information of Work.
     * @return ResponseModel
     */
    public ResponseModel createWork(WorkRequestModel workModel) {
        WorkErrorCode workErrorCode = validateWork(workModel);
        if (Objects.nonNull(workErrorCode)) {
            return new ResponseModel(Boolean.FALSE, workErrorCode.getDescription(), null);
        }

        Work work = workRepository.save(convertToWork(new Work(), workModel));
        return new ResponseModel(Boolean.TRUE, null, convertToWorkModel(work));
    }

    /**
     * Update work.
     *
     * @param workModel the information of Work.
     * @return ResponseModel
     */
    public ResponseModel updateWork(WorkRequestModel workModel) {
        WorkErrorCode workErrorCode = validateWork(workModel);

        if (Objects.nonNull(workErrorCode)) {
            return new ResponseModel(Boolean.FALSE, workErrorCode.getDescription(), null);
        }

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
     * Validate data paging.
     *
     * @param pageRequest the PageRequestBase
     * @return WorkErrorCode
     */
    private WorkErrorCode validatePageRequest(PageRequestBase pageRequest) {
        if (pageRequest.getPageNumber() < 0 || pageRequest.getPageSize() < 1) {
            return WorkErrorCode.ERROR_CODE_2007;
        }

        if (!Strings.isBlank(pageRequest.getSortOrder())) {
            if(!Arrays.asList("ASC", "DESC").contains(pageRequest.getSortOrder().toUpperCase()) || Strings.isBlank(pageRequest.getSortColumn())){
                return WorkErrorCode.ERROR_CODE_2008;
            }
        }

        if (!Strings.isBlank(pageRequest.getSortColumn())) {
            if(!orderColumn.contains(pageRequest.getSortColumn()) || Strings.isBlank(pageRequest.getSortOrder())){
                return WorkErrorCode.ERROR_CODE_2008;
            }
        }

        return null;
    }

    /**
     * Validate data for work info.
     *
     * @param workModel the WorkRequestModel
     * @return WorkErrorCode
     */
    private WorkErrorCode validateWork(WorkRequestModel workModel) {

        if (Strings.isBlank(workModel.getWorkName())) {
            return WorkErrorCode.ERROR_CODE_2005;
        }

        try {
            LocalDate startDate = null;
            if (Strings.isNotBlank(workModel.getStartDate())) {
                startDate = DateTimeConverter.parseStringToDate(workModel.getStartDate(), DateTimeConverter.DEFAULT_DATE_FORMAT);
            }
            LocalDate endDate = null;
            if (Strings.isNotBlank(workModel.getEndDate())) {
                endDate = DateTimeConverter.parseStringToDate(workModel.getEndDate(), DateTimeConverter.DEFAULT_DATE_FORMAT);
            }
            if (Objects.nonNull(startDate) && Objects.nonNull(endDate) && endDate.isBefore(startDate)) {
                return WorkErrorCode.ERROR_CODE_2004;
            }
        } catch (DateTimeException e) {
            return WorkErrorCode.ERROR_CODE_2003;
        }

        Optional<Status> status = statusRepository.findById(workModel.getStatus());
        if (!status.isPresent()) {
            return WorkErrorCode.ERROR_CODE_2006;
        }

        return null;
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
