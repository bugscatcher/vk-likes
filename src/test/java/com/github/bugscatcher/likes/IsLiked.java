package com.github.bugscatcher.likes;

import com.github.bugscatcher.RegressionTests;
import com.github.bugscatcher.TestBase;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.likes.responses.IsLikedResponse;
import com.vk.api.sdk.queries.likes.LikesType;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.*;

public class IsLiked extends TestBase {
    @Test
    @Category(RegressionTests.class)
    public void isLiked() {
        try {
            vk.likes().add(actor2, LikesType.POST, postResponse.getPostId())
                    .ownerId(user1ID)
                    .execute();

            IsLikedResponse isLikedResponse = vk.likes().isLiked(actor1, LikesType.POST, postResponse.getPostId())
                    .userId(user2ID)
                    .ownerId(user1ID)
                    .execute();

            assertTrue("incorrect isLiked", isLikedResponse.isLiked());
            assertFalse("incorrect isCopied", isLikedResponse.isCopied());
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
    public void isLiked_negative() {
        int fakeID = -1;
        try {
            IsLikedResponse isLikedResponse = vk.likes().isLiked(actor1, LikesType.POST, fakeID)
                    .userId(user2ID)
                    .ownerId(user1ID)
                    .execute();
            assertNull("incorrect response", isLikedResponse);
        } catch (ApiException e) {
            assertEquals("incorrect code", Integer.valueOf(100), e.getCode());
        } catch (ClientException e) {
            LOG.error("Transport layer error", e);
            fail();
        }
    }
}
