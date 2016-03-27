package me.algar.cosmos;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import me.algar.cosmos.api.JenkinsRequestManager;
import me.algar.cosmos.data.Job;
import me.algar.cosmos.ui.JobListViewModel;
import rx.Observable;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class JobListViewModelTests {

    protected JenkinsRequestManager mockRequestManager;
    List<Job> mockJobsList;

    @Before
    public void makeMockRequestManager() {
        mockRequestManager = Mockito.mock(JenkinsRequestManager.class);
        mockJobsList = new ArrayList<>();
        Job item = Mockito.mock(Job.class);
        mockJobsList.add(item);
        mockJobsList.add(item);
        Mockito.doReturn(Observable.just(mockJobsList))
                .when(mockRequestManager).getJobs(0);
    }

    @Test
    public void testViewModelShouldGetDataOnCreation(){
        JobListViewModel vm = new JobListViewModel(mockRequestManager);
        verify(mockRequestManager).getJobs(0);
    }

    @Test
    public void testJobsShouldBeAdded() {
        JobListViewModel vm = new JobListViewModel(mockRequestManager);
        int initialSize = vm.getJobs().size();

        vm.addJobs(mockJobsList);

        Assert.assertEquals(vm.getJobs().size(), initialSize + mockJobsList.size());
    }

    @Test
    public void testShouldClearCacheAfterRefresh() {
        JobListViewModel vm = new JobListViewModel(mockRequestManager);

        verify(mockRequestManager).getJobs(0);  // first request initial jobs after construction

        vm.refresh();

        //should clear cache after refresh
        verify(mockRequestManager).clearCache();
        verify(mockRequestManager, times(2)).getJobs(0); // once on creation, once for refresh
    }
}