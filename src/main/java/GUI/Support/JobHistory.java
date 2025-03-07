package GUI.Support;

import Preferences.AppSettings;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;

public class JobHistory {
    public JobHistory() {
        this.jobHistoryList = new ConcurrentLinkedDeque<>();
    }

    private ConcurrentLinkedDeque<Job> jobHistoryList;

    public void addJob(Job newJob) {
        for (Job job : jobHistoryList) {
            if (job.matchesLink(newJob))
                return;
        }
        jobHistoryList.addLast(newJob);
        save();
    }

    public void clear() {
        jobHistoryList = new ConcurrentLinkedDeque<>();
        save();
    }

    public boolean exists(String link) {
        for (Job job : jobHistoryList) {
            if (job.getLink().equals(link)) {
                return true;
            }
        }
        return false;
    }

    public Job getJob(String link) {
        for (Job job : jobHistoryList) {
            if (job.matchesLink(link))
                return job;
        }
        return null;
    }

    private void save() {
        removeDuplicates();
        AppSettings.set.jobHistory(this);
    }

    private void removeDuplicates() {
        LinkedList<Job> removeList = new LinkedList<>();
        for (Job jobSource : jobHistoryList) {
            int dupeCount = 0;
            for (Job job : jobHistoryList) {
                boolean duplicateFound = jobSource.matchesLink(job);
                if (duplicateFound) {
                    dupeCount++;
                }
                if (duplicateFound && dupeCount > 1) {
                    removeList.addLast(job);
                }
            }
        }
        for (Job job : removeList) {
            jobHistoryList.remove(job);
        }
    }
}
