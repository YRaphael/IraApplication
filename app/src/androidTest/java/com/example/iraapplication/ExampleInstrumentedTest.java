package com.example.iraapplication;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.iraapplication.domain.HistoryItem;
import com.example.iraapplication.repos.SqliteHistoryRepository;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void testHistorySize() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SqliteHistoryRepository repo = new SqliteHistoryRepository(appContext);
        assertTrue(repo.getHistory().size() <= 10);
    }

    @Test
    public void testHistoryElements() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SqliteHistoryRepository repo = new SqliteHistoryRepository(appContext);
        List<HistoryItem> history = repo.getHistory();
        boolean result = true;
        for (Object elem :
                history) {
            result &= (elem instanceof HistoryItem);
        }
        assertTrue(result);
    }

    @Test
    public void testUpdatingHistory() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SqliteHistoryRepository repo = new SqliteHistoryRepository(appContext);
        HistoryItem historyItem = new HistoryItem("1", "1", "1");
        repo.addHistoryItem(historyItem);
        assertEquals(historyItem, repo.getHistory().get(0));
    }

    @Test
    public void testIsCorrectSizeAfterUpdating() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SqliteHistoryRepository repo = new SqliteHistoryRepository(appContext);
        HistoryItem historyItem = new HistoryItem("1", "1", "1");
        repo.addHistoryItem(historyItem);
        assertTrue(repo.getHistory().size() <= 10);
    }

}