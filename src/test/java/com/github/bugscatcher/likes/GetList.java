package com.github.bugscatcher.likes;

import com.github.bugscatcher.RegressionTests;
import com.github.bugscatcher.TestBase;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.likes.responses.GetListResponse;
import com.vk.api.sdk.queries.likes.LikesType;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.List;

import static org.junit.Assert.*;

public class GetList extends TestBase {
    @Test
    @Category(RegressionTests.class)
    public void getList() {
        try {
            vk.likes().add(actor1, LikesType.POST, postResponse.getPostId())
                    .ownerId(user1ID)
                    .execute();

            vk.likes().add(actor2, LikesType.POST, postResponse.getPostId())
                    .ownerId(user1ID)
                    .execute();

            GetListResponse getListResponse = vk.likes().getList(actor1, LikesType.POST)
                    .ownerId(user1ID)
                    .itemId(postResponse.getPostId())
                    .execute();

            Integer expectedCount = 2;
            assertEquals("incorrect count", expectedCount, getListResponse.getCount());
            assertEquals("incorrect user IDs", List.of(user2ID, user1ID), getListResponse.getItems());
        } catch (NullPointerException e) {
            LOG.warn("Something null", e);
            fail();
        } catch (ApiException e) {
            LOG.error("Business logic error", e);
            fail();
        } catch (ClientException e) {
            LOG.error("Transport layer error", e);
            fail();
        }
    }

    @Test
    @Category(RegressionTests.class)
    public void getList_negative() {
        try {
            GetListResponse getListResponse = vk.likes().getList(actor1, LikesType.SITEPAGE)
                    .itemId(postResponse.getPostId())
                    .execute();
            assertNull(getListResponse);
        } catch (ApiException e) {
            assertEquals("incorrect code", Integer.valueOf(100), e.getCode());
        } catch (ClientException e) {
            LOG.error("Transport layer error", e);
            fail();
        }
    }
}
