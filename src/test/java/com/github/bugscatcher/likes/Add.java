package com.github.bugscatcher.likes;

import com.github.bugscatcher.TestBase;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.likes.responses.AddResponse;
import com.vk.api.sdk.queries.likes.LikesType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class Add extends TestBase {
    @Test
    public void add() throws ClientException, ApiException {
        AddResponse addResponse = vk.likes().add(actor2, LikesType.POST, postResponse.getPostId())
                .ownerId(USER_ID_1)
                .execute();

        Integer expectedLikes = 1;
        assertEquals("incorrect likes count", addResponse.getLikes(), expectedLikes);
    }

    @Test
    public void add_negative() {
        AddResponse addResponse = null;
        try {
            addResponse = vk.likes().add(actor2, LikesType.AUDIO, postResponse.getPostId())
                    .ownerId(USER_ID_1)
                    .execute();
        } catch (ApiException e) {
            assertEquals("incorrect code", Integer.valueOf(100), e.getCode());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        assertNull(addResponse);
    }
}
