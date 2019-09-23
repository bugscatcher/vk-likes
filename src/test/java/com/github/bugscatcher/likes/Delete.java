package com.github.bugscatcher.likes;

import com.github.bugscatcher.SmokeTests;
import com.github.bugscatcher.TestBase;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.likes.responses.DeleteResponse;
import com.vk.api.sdk.queries.likes.LikesType;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.*;

public class Delete extends TestBase {
    @Test
    @Category(SmokeTests.class)
    public void delete() {
        try {
            vk.likes().add(actor2, LikesType.POST, postResponse.getPostId())
                    .ownerId(user1ID)
                    .execute();


            DeleteResponse deleteResponse = vk.likes().delete(actor2, LikesType.POST, postResponse.getPostId())
                    .ownerId(user1ID)
                    .execute();

            Integer expectedLikes = 0;
            assertEquals("incorrect likes count", expectedLikes, deleteResponse.getLikes());
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
    @Category(SmokeTests.class)
    public void delete_negative() {
        try {
            DeleteResponse deleteResponse = vk.likes().delete(actor2, LikesType.POST, postResponse.getPostId())
                    .ownerId(user1ID)
                    .execute();
            assertNull(deleteResponse);
        } catch (ApiException e) {
            assertEquals("incorrect code", Integer.valueOf(15), e.getCode());
        } catch (ClientException e) {
            LOG.error("Transport layer error", e);
            fail();
        }
    }
}
