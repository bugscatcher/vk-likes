package com.github.bugscatcher.likes;

import com.github.bugscatcher.TestBase;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.likes.responses.IsLikedResponse;
import com.vk.api.sdk.queries.likes.LikesType;
import org.junit.Test;

import static org.junit.Assert.*;

public class IsLiked extends TestBase {
    @Test
    public void isLiked() throws ClientException, ApiException {
        vk.likes().add(actor2, LikesType.POST, postResponse.getPostId())
                .ownerId(USER_ID_1)
                .execute();

        IsLikedResponse isLikedResponse = vk.likes().isLiked(actor1, LikesType.POST, postResponse.getPostId())
                .userId(USER_ID_2)
                .ownerId(USER_ID_1)
                .execute();

        assertTrue("incorrect isLiked", isLikedResponse.isLiked());
        assertFalse("incorrect isCopied", isLikedResponse.isCopied());
    }

    @Test
    public void isLiked_negative() {
        int fakeID = -1;
        IsLikedResponse isLikedResponse = null;
        try {
            isLikedResponse = vk.likes().isLiked(actor1, LikesType.POST, fakeID)
                    .userId(USER_ID_2)
                    .ownerId(USER_ID_1)
                    .execute();
        } catch (ApiException e) {
            assertEquals("incorrect code", Integer.valueOf(100), e.getCode());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        assertNull("incorrect response", isLikedResponse);
    }
}
