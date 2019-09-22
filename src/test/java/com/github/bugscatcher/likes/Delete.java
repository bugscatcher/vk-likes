package com.github.bugscatcher.likes;

import com.github.bugscatcher.TestBase;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.likes.responses.DeleteResponse;
import com.vk.api.sdk.queries.likes.LikesType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class Delete extends TestBase {
    @Test
    public void delete() throws ClientException, ApiException {
        vk.likes().add(actor2, LikesType.POST, postResponse.getPostId())
                .ownerId(USER_ID_1)
                .execute();


        DeleteResponse deleteResponse = vk.likes().delete(actor2, LikesType.POST, postResponse.getPostId())
                .ownerId(USER_ID_1)
                .execute();

        Integer expectedLikes = 0;
        assertEquals("incorrect likes count", deleteResponse.getLikes(), expectedLikes);
    }

    @Test
    public void delete_negative() {
        DeleteResponse deleteResponse = null;
        try {
            deleteResponse = vk.likes().delete(actor2, LikesType.POST, postResponse.getPostId())
                    .ownerId(USER_ID_1)
                    .execute();
        } catch (ApiException e) {
            assertEquals("incorrect code", Integer.valueOf(15), e.getCode());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        assertNull(deleteResponse);
    }
}
