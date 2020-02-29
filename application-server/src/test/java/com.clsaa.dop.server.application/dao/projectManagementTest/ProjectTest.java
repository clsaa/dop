//package com.clsaa.dop.server.application.dao.projectManagementTest;
//
//
//import com.clsaa.dop.server.application.AllApplicationTests;
//import com.clsaa.dop.server.application.dao.ProjectRepository;
//import com.clsaa.dop.server.application.model.bo.ProjectBoV1;
//import com.clsaa.dop.server.application.model.po.Project;
//import com.clsaa.dop.server.application.util.BeanUtils;
//import org.junit.Assert;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.time.LocalDateTime;
//
//
//public class ProjectTest extends AllApplicationTests {
//
//    @Autowired
//    private ProjectRepository projectRepository;
//
//    @Test
//    public void test() throws Exception {
//
//        // 创建10条记录
//
//
//        projectRepository.deleteAllInBatch();
//        LocalDateTime l1 = LocalDateTime.now().withNano(0);
//        LocalDateTime l2 = LocalDateTime.now().withNano(0);
//        projectRepository.saveAndFlush(Project.builder()
//                .title("AAA")
//                .description("郑博文kakoyi")
//                .cuser(811L)
//                .muser(6181L)
//                .is_deleted(false)
//                .ctime(l1)
//                .mtime(l1)
//                .status(Project.Status.NORMAL)
//                .organizationId(123L)
//                .build());
//
//        projectRepository.saveAndFlush(Project.builder()
//                .title("BBB")
//                .description("ylhkakoyi")
//                .cuser(8111L)
//                .muser(6182L)
//                .status(Project.Status.NORMAL)
//                .is_is_deleted(false)
//                .ctime(l2)
//                .organizationId(123L)
//                .mtime(l2)
//                .build());
//
//        // 测试findBytitle, 查询名称为FFF的Project
//        //projectRepository.findBytitle("FFF");
//        Assert.assertEquals(l2, projectRepository.findByTitle("BBB").getCtime());
//
//        // 测试findBytitleAndAge, 查询姓名为FFF并且年龄为60的Project
//        Assert.assertEquals("AAA", projectRepository.findByCuser(811L).getTitle());
//        int orgCount = projectRepository.findAll().size();
//        Assert.assertEquals("AAA", BeanUtils.convertType(projectRepository.findByCuser(811L), ProjectBoV1.class).getTitle());
//        projectRepository.delete(projectRepository.findByTitle("AAA"));
//        projectRepository.delete(projectRepository.findByCuser(8111L));
//        Assert.assertEquals(orgCount - 2, projectRepository.findAll().size());
//
//
//    }
//}
