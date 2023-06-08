package service;

import dao.DaoTask;
import domain.model.Account;
import domain.model.Task;
import domain.services.impl.ServicesTaskImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServiceTaskTest {

    @InjectMocks
    private ServicesTaskImpl servicesTask;

    @Mock
    private DaoTask daoTask;

    @Nested
    @DisplayName("Add")
    class addTest {
        @Test
        @DisplayName("Add task")
        void addTest1() {
            Task task = new Task();
            task.setTaskName("test");
            task.setUsername("test");
            task.setCompleted(0);
            task.setEndTime(LocalDateTime.now());
            task.setInitTime(LocalDateTime.now());
            when(daoTask.add(task)).thenReturn(task);
            assertEquals(task, servicesTask.add(task));
            verify(daoTask).add(task);
        }

        @Test
        @DisplayName("Add task with null name")
        void addTest2() {
            Task task = new Task();
            task.setTaskName(null);
            task.setUsername("test");
            task.setCompleted(0);
            task.setEndTime(LocalDateTime.now());
            task.setInitTime(LocalDateTime.now());
            when(daoTask.add(task)).thenReturn(null);
            assertNull(servicesTask.add(task));
            verify(daoTask).add(task);
        }

    }

    @Nested
    @DisplayName("Get")
    class getTest {
        @Test
        @DisplayName("Get task")
        void getTest1() {
            Task task = new Task();
            task.setTaskName("test");
            task.setUsername("test");
            task.setCompleted(0);
            task.setEndTime(LocalDateTime.now());
            task.setInitTime(LocalDateTime.now());
            when(daoTask.get(task)).thenReturn(task);
            assertEquals(task, servicesTask.get(task));
            verify(daoTask).get(task);
        }

    }

    @Nested
    @DisplayName("Delete")
    class deleteTest {
        @Test
        @DisplayName("Delete task")
        void deleteTest1() {
            Task task = new Task();
            task.setTaskName("test");
            task.setUsername("test");
            task.setCompleted(0);
            task.setEndTime(LocalDateTime.now());
            task.setInitTime(LocalDateTime.now());
            when(daoTask.delete(task)).thenReturn(task);
            assertEquals(task, servicesTask.delete(task));
            verify(daoTask).delete(task);
        }
    }

    @Nested
    @DisplayName("GetTasks")
    class GetTasksTest {
        @Test
        @DisplayName("Get tasks")
        void getTasksTest1() {
            Task task = new Task();
            task.setUsername("test");
            when(daoTask.getAllByUser(new Account(task.getUsername()))).thenReturn(Collections.singletonList(task));
            assertEquals(Collections.singletonList(task), servicesTask.get(new Account(task.getUsername())));
            verify(daoTask).getAllByUser(new Account(task.getUsername()));
        }
    }

    @Nested
    @DisplayName("Update")
    class updateTest {
        @Test
        @DisplayName("Update task")
        void updateTest1() {
            Task task = new Task();
            task.setTaskName("test");
            task.setUsername("test");
            task.setCompleted(0);
            task.setEndTime(LocalDateTime.now());
            task.setInitTime(LocalDateTime.now());
            when(daoTask.update(task)).thenReturn(task);
            assertEquals(task, servicesTask.update(task));
            verify(daoTask).update(task);
        }
    }

    @Nested
    @DisplayName("DeleteCompletedTasks")
    class deleteCompletedTasksTest {
        @Test
        @DisplayName("Delete completed tasks")
        void deleteCompletedTasksTest1() {
            Task task = new Task();
            task.setUsername("test");
            when(daoTask.deleteCompletedTasks(new Account(task.getUsername()))).thenReturn(1);
            assertEquals(1, servicesTask.deleteCompletedTasks(new Account(task.getUsername())));
            verify(daoTask).deleteCompletedTasks(new Account(task.getUsername()));
        }
    }


}
