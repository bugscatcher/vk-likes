package com.github.bugscatcher;

import com.github.bugscatcher.likes.Add;
import com.github.bugscatcher.likes.Delete;
import com.github.bugscatcher.likes.GetList;
import com.github.bugscatcher.likes.IsLiked;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;

@RunWith(Categories.class)
@Categories.IncludeCategory({RegressionTests.class, SmokeTests.class})
@Categories.SuiteClasses({
        Add.class,
        Delete.class,
        GetList.class,
        IsLiked.class
})

public class TestSuite {
}
