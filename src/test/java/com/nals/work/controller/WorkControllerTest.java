package com.nals.work.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nals.work.entity.Status;
import com.nals.work.entity.Work;
import com.nals.work.model.PageRequestBase;
import com.nals.work.model.code.WorkErrorCode;
import com.nals.work.model.work.WorkRequestModel;
import com.nals.work.repository.StatusRepository;
import com.nals.work.repository.WorkRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class WorkControllerTest {

    private static final String WORK_NAME_TEST = "Test work name";
    private static final String WORK_NAME_TEST_CREATE = "Test work name create";
    private static final String WORK_NAME_TEST_UPDATE = "Test work name update";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WorkRepository workRepository;
    @Autowired
    private StatusRepository statusRepository;

    @Before
    public void before() {
        Work work = new Work();
        work.setWorkName(WORK_NAME_TEST);
        work.setStartDate(LocalDate.now());
        work.setEndDate(LocalDate.now());
        List<Status> statuses = statusRepository.findAll();
        work.setStatus(statuses.get(1));
        workRepository.save(work);
    }

    @Test
    public void findWorksSuccess() throws Exception {

        PageRequestBase pageRequest = new PageRequestBase();
        pageRequest.setPageNumber(0);
        pageRequest.setPageSize(10);
        pageRequest.setSortColumn("id");
        pageRequest.setSortOrder("asc");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(pageRequest);
        mvc.perform(post("/NALS/work/findWorks")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("success", is(Boolean.TRUE)))
                .andExpect(jsonPath("result.totalPage", is(1)));
    }


    @Test
    public void findWorksError() throws Exception {
        Work work = new Work();
        work.setWorkName("Test 1");
        work.setStartDate(LocalDate.now());
        work.setEndDate(LocalDate.now());
        List<Status> statuses = statusRepository.findAll();
        work.setStatus(statuses.get(1));
        workRepository.save(work);

        PageRequestBase pageRequest = new PageRequestBase();
        pageRequest.setPageNumber(-1);
        pageRequest.setPageSize(10);
        pageRequest.setSortColumn("id");
        pageRequest.setSortOrder("asc");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(pageRequest);
        mvc.perform(post("/NALS/work/findWorks")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("success", is(Boolean.FALSE)))
                .andExpect(jsonPath("errorMessage", is(WorkErrorCode.ERROR_CODE_2007.getDescription())));
    }

    @Test
    public void createWorkSuccess() throws Exception {
        WorkRequestModel workRequestModel = new WorkRequestModel();
        workRequestModel.setWorkName(WORK_NAME_TEST_CREATE);
        workRequestModel.setStartDate("07/12/2021");
        workRequestModel.setEndDate("08/12/2021");
        workRequestModel.setStatus(1);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(workRequestModel);
        mvc.perform(post("/NALS/work")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("success", is(Boolean.TRUE)))
                .andExpect(jsonPath("result.workName", is(WORK_NAME_TEST_CREATE)));
    }

    @Test
    public void createWorkError() throws Exception {

        WorkRequestModel workRequestModel = new WorkRequestModel();
        workRequestModel.setWorkName("");
        workRequestModel.setStartDate("07/12/2021");
        workRequestModel.setEndDate("08/12/2021");
        workRequestModel.setStatus(1);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(workRequestModel);
        mvc.perform(post("/NALS/work")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("success", is(Boolean.FALSE)))
                .andExpect(jsonPath("errorMessage", is(WorkErrorCode.ERROR_CODE_2005.getDescription())));
    }

    @Test
    public void updateWorkSuccess() throws Exception {
        List<Work> works = workRepository.findAll();
        WorkRequestModel workRequestModel = new WorkRequestModel();
        workRequestModel.setId(works.get(0).getId());
        workRequestModel.setWorkName(WORK_NAME_TEST_UPDATE);
        workRequestModel.setStartDate("07/12/2021");
        workRequestModel.setEndDate("08/12/2021");
        workRequestModel.setStatus(1);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(workRequestModel);
        mvc.perform(put("/NALS/work")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("success", is(Boolean.TRUE)))
                .andExpect(jsonPath("result.workName", is(WORK_NAME_TEST_UPDATE)));
    }

    @Test
    public void updateWorkError() throws Exception {

        List<Work> works = workRepository.findAll();
        WorkRequestModel workRequestModel = new WorkRequestModel();
        workRequestModel.setId(works.get(0).getId());
        workRequestModel.setWorkName("");
        workRequestModel.setStartDate("07/12/2021");
        workRequestModel.setEndDate("08/12/2021");
        workRequestModel.setStatus(1);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(workRequestModel);
        mvc.perform(put("/NALS/work")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("success", is(Boolean.FALSE)))
                .andExpect(jsonPath("errorMessage", is(WorkErrorCode.ERROR_CODE_2005.getDescription())));
    }

    @Test
    public void deleteWorkSuccess() throws Exception {

        List<Work> works = workRepository.findAll();

        mvc.perform(delete("/NALS/work/" + works.get(0).getId().toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("success", is(Boolean.TRUE)));
    }

    @Test
    public void deleteWorkError() throws Exception {

        mvc.perform(delete("/NALS/work/12345")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("success", is(Boolean.FALSE)))
                .andExpect(jsonPath("errorMessage", is(WorkErrorCode.ERROR_CODE_2002.getDescription())));
    }
}
