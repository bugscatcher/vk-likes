package com.github.bugscatcher.likes;

import com.github.bugscatcher.TestBase;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.likes.responses.GetListResponse;
import com.vk.api.sdk.queries.likes.LikesType;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GetList extends TestBase {
    @Test
    public void getList() throws ClientException, ApiException {
        vk.likes().add(actor1, LikesType.POST, postResponse.getPostId())
                .ownerId(USER_ID_1)
                .execute();

        vk.likes().add(actor2, LikesType.POST, postResponse.getPostId())
                .ownerId(USER_ID_1)
                .execute();

        GetListResponse getListResponse = vk.likes().getList(actor1, LikesType.POST)
                .ownerId(USER_ID_1)
                .itemId(postResponse.getPostId())
                .execute();

        Integer expectedCount = 2;
        assertEquals("incorrect count", getListResponse.getCount(), expectedCount);
        assertEquals("incorrect user IDs", getListResponse.getItems(), List.of(USER_ID_2, USER_ID_1));
    }

    @Test
    public void getList_negative() {
        GetListResponse getListResponse = null;
        try {
            getListResponse = vk.likes().getList(actor1, LikesType.SITEPAGE)
                    .itemId(postResponse.getPostId())
                    .execute();
        } catch (ApiException e) {
            assertEquals("incorrect code", Integer.valueOf(100), e.getCode());
        } catch (ClientException e) {
            e.printStackTrace();
        }
        assertNull(getListResponse);
    }
}
