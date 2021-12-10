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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
public class WorkServiceTest {

    private static final String WORK_NAME_TEST = "Test work name";

    @InjectMocks
    private WorkService workService;

    @Mock
    private WorkRepository workRepository;

    @Mock
    private StatusRepository statusRepository;

    @Before
    public void before(){
        List<Work> works = new ArrayList<>();
        Work work = new Work();
        work.setId(1L);
        work.setWorkName(WORK_NAME_TEST);
        work.setStartDate(LocalDate.now());
        work.setEndDate(LocalDate.now());
        Status status = new Status();
        status.setId(1);
        status.setDescription("Planning");
        work.setStatus(status);
        works.add(work);
        Page<Work> pagedResponse = new PageImpl<>(works);
        Mockito.when(workRepository.findAll(Mockito.any(Pageable.class))).thenReturn(pagedResponse);

        Mockito.when(workRepository.save(Mockito.any(Work.class))).thenReturn(work);
        Mockito.when(statusRepository.findById(Mockito.any())).thenReturn(Optional.of(status));
        Mockito.when(workRepository.findById(Mockito.any())).thenReturn(Optional.of(work));

    }

    @Test
    public void getWorks_Success() {
        PageRequestBase pageRequest = new PageRequestBase();
        pageRequest.setPageNumber(0);
        pageRequest.setPageSize(10);
        pageRequest.setSortColumn("id");
        pageRequest.setSortOrder("asc");
        ResponseModel responseModel = workService.getWorks(pageRequest);
        Assert.assertNotNull(responseModel);
        Assert.assertTrue(responseModel.getSuccess());
        PageResult pageResult = (PageResult) responseModel.getResult();
        Assert.assertTrue(pageResult.getContent().size() > 0);
    }

    @Test
    public void getWorks_SuccessNonSort() {
        PageRequestBase pageRequest = new PageRequestBase();
        pageRequest.setPageNumber(0);
        pageRequest.setPageSize(10);
        ResponseModel responseModel = workService.getWorks(pageRequest);
        Assert.assertNotNull(responseModel);
        Assert.assertTrue(responseModel.getSuccess());
        PageResult pageResult = (PageResult) responseModel.getResult();
        Assert.assertTrue(pageResult.getContent().size() > 0);
    }

    @Test
    public void getWorks_RequestPagingWrong() {
        PageRequestBase pageRequest = new PageRequestBase();
        pageRequest.setPageNumber(-1);
        pageRequest.setPageSize(10);
        pageRequest.setSortColumn("id");
        pageRequest.setSortOrder("asc");
        ResponseModel responseModel = workService.getWorks(pageRequest);
        Assert.assertNotNull(responseModel);
        Assert.assertFalse(responseModel.getSuccess());
        Assert.assertEquals(responseModel.getErrorMessage(), WorkErrorCode.ERROR_CODE_2007.getDescription());
        Assert.assertNull(responseModel.getResult());
    }

    @Test
    public void getWorks_RequestOrderWrong() {
        PageRequestBase pageRequest = new PageRequestBase();
        pageRequest.setPageNumber(0);
        pageRequest.setPageSize(10);
        pageRequest.setSortColumn("AD");
        pageRequest.setSortOrder("asc");
        ResponseModel responseModel = workService.getWorks(pageRequest);
        Assert.assertNotNull(responseModel);
        Assert.assertFalse(responseModel.getSuccess());
        Assert.assertEquals(responseModel.getErrorMessage(), WorkErrorCode.ERROR_CODE_2008.getDescription());
        Assert.assertNull(responseModel.getResult());
    }

    @Test
    public void getWorks_RequestOrderWrong_1() {
        PageRequestBase pageRequest = new PageRequestBase();
        pageRequest.setPageNumber(0);
        pageRequest.setPageSize(10);
        pageRequest.setSortColumn("id");
        pageRequest.setSortOrder("");
        ResponseModel responseModel = workService.getWorks(pageRequest);
        Assert.assertNotNull(responseModel);
        Assert.assertFalse(responseModel.getSuccess());
        Assert.assertEquals(responseModel.getErrorMessage(), WorkErrorCode.ERROR_CODE_2008.getDescription());
        Assert.assertNull(responseModel.getResult());
    }

    @Test
    public void getWorks_RequestSortWrong() {
        PageRequestBase pageRequest = new PageRequestBase();
        pageRequest.setPageNumber(0);
        pageRequest.setPageSize(10);
        pageRequest.setSortColumn("id");
        pageRequest.setSortOrder("AD");
        ResponseModel responseModel = workService.getWorks(pageRequest);
        Assert.assertNotNull(responseModel);
        Assert.assertFalse(responseModel.getSuccess());
        Assert.assertEquals(responseModel.getErrorMessage(), WorkErrorCode.ERROR_CODE_2008.getDescription());
        Assert.assertNull(responseModel.getResult());
    }

    @Test
    public void getWorks_RequestSortWrong_1() {
        PageRequestBase pageRequest = new PageRequestBase();
        pageRequest.setPageNumber(0);
        pageRequest.setPageSize(10);
        pageRequest.setSortColumn("");
        pageRequest.setSortOrder("ASC");
        ResponseModel responseModel = workService.getWorks(pageRequest);
        Assert.assertNotNull(responseModel);
        Assert.assertFalse(responseModel.getSuccess());
        Assert.assertEquals(responseModel.getErrorMessage(), WorkErrorCode.ERROR_CODE_2008.getDescription());
        Assert.assertNull(responseModel.getResult());
    }

    @Test
    public void createWorks_Success() {
        WorkRequestModel workRequestModel = new WorkRequestModel();
        workRequestModel.setWorkName(WORK_NAME_TEST);
        workRequestModel.setStartDate("07/12/2021");
        workRequestModel.setEndDate("08/12/2021");
        workRequestModel.setStatus(1);
        ResponseModel responseModel = workService.createWork(workRequestModel);
        Assert.assertNotNull(responseModel);
        Assert.assertTrue(responseModel.getSuccess());
        WorkModel work = (WorkModel) responseModel.getResult();
        Assert.assertEquals(work.getWorkName(), WORK_NAME_TEST);
    }

    @Test
    public void createWorks_WorkNameBlank() {
        WorkRequestModel workRequestModel = new WorkRequestModel();
        workRequestModel.setWorkName("");
        workRequestModel.setStartDate("07/12/2021");
        workRequestModel.setEndDate("08/12/2021");
        workRequestModel.setStatus(1);
        ResponseModel responseModel = workService.createWork(workRequestModel);
        Assert.assertNotNull(responseModel);
        Assert.assertFalse(responseModel.getSuccess());
        Assert.assertEquals(responseModel.getErrorMessage(), WorkErrorCode.ERROR_CODE_2005.getDescription());
        Assert.assertNull(responseModel.getResult());
    }

    @Test
    public void createWorks_StartDateWrong() {
        WorkRequestModel workRequestModel = new WorkRequestModel();
        workRequestModel.setWorkName(WORK_NAME_TEST);
        workRequestModel.setStartDate("07/27/2021");
        workRequestModel.setEndDate("08/12/2021");
        workRequestModel.setStatus(1);
        ResponseModel responseModel = workService.createWork(workRequestModel);
        Assert.assertNotNull(responseModel);
        Assert.assertFalse(responseModel.getSuccess());
        Assert.assertEquals(responseModel.getErrorMessage(), WorkErrorCode.ERROR_CODE_2003.getDescription());
        Assert.assertNull(responseModel.getResult());
    }

    @Test
    public void createWorks_EndDateWrong() {
        WorkRequestModel workRequestModel = new WorkRequestModel();
        workRequestModel.setWorkName(WORK_NAME_TEST);
        workRequestModel.setStartDate("07/27/2021");
        workRequestModel.setEndDate("08/12/2021");
        workRequestModel.setStatus(1);
        ResponseModel responseModel = workService.createWork(workRequestModel);
        Assert.assertNotNull(responseModel);
        Assert.assertFalse(responseModel.getSuccess());
        Assert.assertEquals(responseModel.getErrorMessage(), WorkErrorCode.ERROR_CODE_2003.getDescription());
        Assert.assertNull(responseModel.getResult());
    }

    @Test
    public void createWorks_EndDateBeforeStartDate () {
        WorkRequestModel workRequestModel = new WorkRequestModel();
        workRequestModel.setWorkName(WORK_NAME_TEST);
        workRequestModel.setStartDate("07/12/2021");
        workRequestModel.setEndDate("05/12/2021");
        workRequestModel.setStatus(1);
        ResponseModel responseModel = workService.createWork(workRequestModel);
        Assert.assertNotNull(responseModel);
        Assert.assertFalse(responseModel.getSuccess());
        Assert.assertEquals(responseModel.getErrorMessage(), WorkErrorCode.ERROR_CODE_2004.getDescription());
        Assert.assertNull(responseModel.getResult());
    }

    @Test
    public void createWorks_StatusWrong() {
        Mockito.when(statusRepository.findById(15)).thenReturn(Optional.empty());
        WorkRequestModel workRequestModel = new WorkRequestModel();
        workRequestModel.setWorkName(WORK_NAME_TEST);
        workRequestModel.setStartDate("07/12/2021");
        workRequestModel.setEndDate("08/12/2021");
        workRequestModel.setStatus(15);
        ResponseModel responseModel = workService.createWork(workRequestModel);
        Assert.assertNotNull(responseModel);
        Assert.assertFalse(responseModel.getSuccess());
        Assert.assertEquals(responseModel.getErrorMessage(), WorkErrorCode.ERROR_CODE_2006.getDescription());
        Assert.assertNull(responseModel.getResult());
    }

    @Test
    public void updateWork_Success() {
        WorkRequestModel workRequestModel = new WorkRequestModel();
        workRequestModel.setId(1l);
        workRequestModel.setWorkName(WORK_NAME_TEST);
        workRequestModel.setStartDate("07/12/2021");
        workRequestModel.setEndDate("08/12/2021");
        workRequestModel.setStatus(1);
        ResponseModel responseModel = workService.updateWork(workRequestModel);
        Assert.assertNotNull(responseModel);
        Assert.assertTrue(responseModel.getSuccess());
        WorkModel work = (WorkModel) responseModel.getResult();
        Assert.assertEquals(work.getWorkName(), WORK_NAME_TEST);
    }

    @Test
    public void updateWork_IdBlank() {
        WorkRequestModel workRequestModel = new WorkRequestModel();
        workRequestModel.setId(null);
        workRequestModel.setWorkName(WORK_NAME_TEST);
        workRequestModel.setStartDate("07/12/2021");
        workRequestModel.setEndDate("08/12/2021");
        workRequestModel.setStatus(1);
        ResponseModel responseModel = workService.updateWork(workRequestModel);
        Assert.assertNotNull(responseModel);
        Assert.assertFalse(responseModel.getSuccess());
        Assert.assertEquals(responseModel.getErrorMessage(), WorkErrorCode.ERROR_CODE_2001.getDescription());
        Assert.assertNull(responseModel.getResult());
    }

    @Test
    public void updateWork_IdNotExist() {
        WorkRequestModel workRequestModel = new WorkRequestModel();
        workRequestModel.setId(15l);
        workRequestModel.setWorkName(WORK_NAME_TEST);
        workRequestModel.setStartDate("07/12/2021");
        workRequestModel.setEndDate("08/12/2021");
        workRequestModel.setStatus(1);
        Mockito.when(workRepository.findById(15l)).thenReturn(Optional.empty());
        ResponseModel responseModel = workService.updateWork(workRequestModel);
        Assert.assertNotNull(responseModel);
        Assert.assertFalse(responseModel.getSuccess());
        Assert.assertEquals(responseModel.getErrorMessage(), WorkErrorCode.ERROR_CODE_2002.getDescription());
        Assert.assertNull(responseModel.getResult());
    }

    @Test
    public void updateWork_WorkNameBlank() {
        WorkRequestModel workRequestModel = new WorkRequestModel();
        workRequestModel.setId(1l);
        workRequestModel.setWorkName("");
        workRequestModel.setStartDate("07/12/2021");
        workRequestModel.setEndDate("08/12/2021");
        workRequestModel.setStatus(1);
        ResponseModel responseModel = workService.updateWork(workRequestModel);
        Assert.assertNotNull(responseModel);
        Assert.assertFalse(responseModel.getSuccess());
        Assert.assertEquals(responseModel.getErrorMessage(), WorkErrorCode.ERROR_CODE_2005.getDescription());
        Assert.assertNull(responseModel.getResult());
    }

    @Test
    public void updateWork_StartDateWrong() {
        WorkRequestModel workRequestModel = new WorkRequestModel();
        workRequestModel.setId(1l);
        workRequestModel.setWorkName(WORK_NAME_TEST);
        workRequestModel.setStartDate("07/27/2021");
        workRequestModel.setEndDate("08/12/2021");
        workRequestModel.setStatus(1);
        ResponseModel responseModel = workService.updateWork(workRequestModel);
        Assert.assertNotNull(responseModel);
        Assert.assertFalse(responseModel.getSuccess());
        Assert.assertEquals(responseModel.getErrorMessage(), WorkErrorCode.ERROR_CODE_2003.getDescription());
        Assert.assertNull(responseModel.getResult());
    }

    @Test
    public void updateWork_EndDateWrong() {
        WorkRequestModel workRequestModel = new WorkRequestModel();
        workRequestModel.setId(1l);
        workRequestModel.setWorkName(WORK_NAME_TEST);
        workRequestModel.setStartDate("07/27/2021");
        workRequestModel.setEndDate("08/12/2021");
        workRequestModel.setStatus(1);
        ResponseModel responseModel = workService.updateWork(workRequestModel);
        Assert.assertNotNull(responseModel);
        Assert.assertFalse(responseModel.getSuccess());
        Assert.assertEquals(responseModel.getErrorMessage(), WorkErrorCode.ERROR_CODE_2003.getDescription());
        Assert.assertNull(responseModel.getResult());
    }

    @Test
    public void updateWork_EndDateBeforeStartDate () {
        WorkRequestModel workRequestModel = new WorkRequestModel();
        workRequestModel.setId(1l);
        workRequestModel.setWorkName(WORK_NAME_TEST);
        workRequestModel.setStartDate("07/12/2021");
        workRequestModel.setEndDate("05/12/2021");
        workRequestModel.setStatus(1);
        ResponseModel responseModel = workService.updateWork(workRequestModel);
        Assert.assertNotNull(responseModel);
        Assert.assertFalse(responseModel.getSuccess());
        Assert.assertEquals(responseModel.getErrorMessage(), WorkErrorCode.ERROR_CODE_2004.getDescription());
        Assert.assertNull(responseModel.getResult());
    }

    @Test
    public void updateWork_StatusWrong() {
        Mockito.when(statusRepository.findById(15)).thenReturn(Optional.empty());
        WorkRequestModel workRequestModel = new WorkRequestModel();
        workRequestModel.setId(1l);
        workRequestModel.setWorkName(WORK_NAME_TEST);
        workRequestModel.setStartDate("07/12/2021");
        workRequestModel.setEndDate("08/12/2021");
        workRequestModel.setStatus(15);
        ResponseModel responseModel = workService.updateWork(workRequestModel);
        Assert.assertNotNull(responseModel);
        Assert.assertFalse(responseModel.getSuccess());
        Assert.assertEquals(responseModel.getErrorMessage(), WorkErrorCode.ERROR_CODE_2006.getDescription());
        Assert.assertNull(responseModel.getResult());
    }

    @Test
    public void deleteWork_Success() {

        ResponseModel responseModel = workService.deleteWork(1l);
        Assert.assertNotNull(responseModel);
        Assert.assertTrue(responseModel.getSuccess());
    }

    @Test
    public void deleteWork_Wrong() {

        Mockito.when(workRepository.findById(15l)).thenReturn(Optional.empty());
        ResponseModel responseModel = workService.deleteWork(15l);
        Assert.assertNotNull(responseModel);
        Assert.assertFalse(responseModel.getSuccess());
    }

}
