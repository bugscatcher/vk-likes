package com.github.bugscatcher;

import com.github.bugscatcher.likes.Add;
import com.github.bugscatcher.likes.Delete;
import com.github.bugscatcher.likes.GetList;
import com.github.bugscatcher.likes.IsLiked;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        Add.class,
        Delete.class,
        GetList.class,
        IsLiked.class
})

public class TestSuite {
}
